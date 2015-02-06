/* 
 * NOTICE
 * This software was produced for the office of the Director of National Intelligence (ODNI)
 * Intelligence Advanced Research Projects Activity (IARPA) ICArUS program, 
 * BAA number IARPA-BAA-10-04, under contract 2009-0917826-016, and is subject 
 * to the Rights in Data-General Clause 52.227-14, Alt. IV (DEC 2007).
 * 
 * This software and data is provided strictly to support demonstrations of ICArUS challenge problems
 * and to assist in the development of cognitive-neuroscience architectures. It is not intended to be used
 * in operational systems or environments.
 * 
 * Copyright (C) 2015 The MITRE Corporation. All Rights Reserved.
 * 
 */
package org.mitre.icarus.cps.assessment.assessment_processor.phase_2;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.bind.JAXBException;
import org.mitre.icarus.cps.app.experiment.IcarusSubjectData;
import org.mitre.icarus.cps.app.experiment.data_recorder.SubjectDataRecorderUtils;
import org.mitre.icarus.cps.assessment.assessment_metrics_computer.phase_2.MissionMetricsComputer;
import org.mitre.icarus.cps.assessment.data_aggregator.DataAggregatorUtils;
import org.mitre.icarus.cps.assessment.data_model.base.DataType;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.MetricsInfo;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.MissionMetrics;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.SubjectSigintProbabilities;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.TrialData;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.trial_part_data.AttackProbabilityReportData;
import org.mitre.icarus.cps.assessment.utils.phase_2.AssessmentUtils_Phase2;
import org.mitre.icarus.cps.exam.base.response.ResponseGeneratorData;
import org.mitre.icarus.cps.exam.phase_2.IcarusExam_Phase2;
import org.mitre.icarus.cps.exam.phase_2.loader.IcarusExamLoader_Phase2;
import org.mitre.icarus.cps.exam.phase_2.testing.IcarusTestTrial_Phase2;
import org.mitre.icarus.cps.exam.phase_2.testing.Mission;
import org.mitre.icarus.cps.exam.phase_2.testing.MissionType;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.BlueAction.BlueActionType;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbeType;

/**
 * Contains static methods to compute metrics assessing whether a subject properly understood
 * each mission in an exam.
 * 
 * @author CBONACETO
 */
public class HumanSubjectPruner {
    
    /**
     * 1) Count the number of times the subject entered the same probability (+/- 1).
     * 2) Count the number of times the subject entered a probability more than 10% different from the correct BLUEBOOK value (Missions 1, 3)
     * 3) Count the number of times the subject selected the same SIGINT location.
     * 4) Count the number of times the subject selected the same Blue Action.
     * 5) If any 2 of the above frequencies are greater than 0.95, remove the subject. From the mission.     * 
     * 
     * @param exam
     * @param missionIds
     * @param missionNumbers
     * @param dataDir
     * @param subjects
     * @param metricsInfo
     * @param outputProgress
     * @return
     */
        
    public static List<List<SubjectScore>> computeSubjectScores(IcarusExam_Phase2 exam, 
            List<String> missionIds, List<Integer> missionNumbers,
            File dataDir, List<Set<IcarusSubjectData>> subjects, MetricsInfo metricsInfo,
            boolean outputProgress) {
         if (!dataDir.isDirectory() || !dataDir.exists()) {
            throw new IllegalArgumentException(dataDir.getPath() + " is not a directory or cannot be found.");
        }
        if (missionIds == null || missionNumbers == null || missionIds.isEmpty() 
                || missionIds.size() != missionNumbers.size()) {
            throw new IllegalArgumentException("Mission IDs and Mission Numbers must be specified");
        }

        metricsInfo = metricsInfo == null ? MetricsInfo.createDefaultMetricsInfo() : metricsInfo;        
        int numMissions = missionIds.size();
        String examId = exam.getId();
        
        //Contains the subject scores for each subject on each mission (Missions->Subjects)
        List<List<SubjectScore>> subjectScores = new ArrayList<List<SubjectScore>>(missionIds.size());
        for(int i = 0; i < missionIds.size(); i++) {
            subjectScores.add(new LinkedList<SubjectScore>());
        }
        
        //Subject SIGINT probabilities for each subject (computed using Mission 1 P(Attack|SIGINT) reports)
        HashMap<String, SubjectSigintProbabilities> subjectSigintProbabilities = 
                new HashMap<String, SubjectSigintProbabilities>();        
        
        //Process subject data for each mission
        TrialResponseProcessor trialResponseProcessor = new TrialResponseProcessor();
        MissionMetricsComputer missionMetricsComputer = new MissionMetricsComputer();
        IcarusExamLoader_Phase2 examLoader = IcarusExamLoader_Phase2.getInstance();
        int missionIndex = 0;
        for (String missionId : missionIds) {            
            int missionNum = missionNumbers.get(missionIndex);
            //Find the mission in the exam
            Mission<?> mission = null;
            Iterator<Mission<?>> missionIter = exam.getMissions().iterator();
            while (missionIter.hasNext() && mission == null) {
                Mission<?> m = missionIter.next();
                if (missionId.equals(m.getId())) {
                    mission = m;
                }
            }
            if (mission != null) {
                List<SubjectScore> currSubjectScores = subjectScores.get(missionIndex);
                for (IcarusSubjectData subject : subjects.get(missionIndex)) {                    
                    trialResponseProcessor.setSubjectSigintProbs(
                            subjectSigintProbabilities.get(subject.getSubjectId()));
                    //Load the mission data for the current subject on the current mission
                    File file = new File(dataDir, "/" + SubjectDataRecorderUtils.formatSubjectFolderName(subject) + "/"
                            + SubjectDataRecorderUtils.formatDataFileName(subject, examId, missionId));
                    Mission<?> missionResponse = null;
                    try {
                        missionResponse = examLoader.unmarshalExamPhase(file.toURI().toURL(), false);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    //Process the mission data for the current subject
                    if (missionResponse != null && missionResponse.getTestTrials() != null
                            && !missionResponse.getTestTrials().isEmpty()) {
                        //Populate the Blue locations for each trial in the mission
                        Iterator<? extends IcarusTestTrial_Phase2> missionTrialIter = mission.getTestTrials().iterator();
                        Iterator<? extends IcarusTestTrial_Phase2> missionResponseTrialIter = missionResponse.getTestTrials().iterator();
                        while (missionTrialIter.hasNext() && missionResponseTrialIter.hasNext()) {
                            IcarusTestTrial_Phase2 missionTrial = missionTrialIter.next();
                            IcarusTestTrial_Phase2 missionResponseTrial = missionResponseTrialIter.next();
                            missionResponseTrial.setBlueLocations(missionTrial.getBlueLocations());
                        }
                        //Validate that the stimuli in the subject response matches the stimuli in the offical exam                        
                        IcarusExam_Phase2.MissionComparisonReport comparisonReport
                                = IcarusExam_Phase2.compareMissions(mission, missionResponse);
                        boolean dataValid = comparisonReport.missionsEqual;
                        if (outputProgress && !dataValid) {
                            System.err.println("Warning, stimuli for Subject: " + subject.getSubjectId() + ", Mission: "
                                    + missionId + " did not match the exam stimuli and will not be used. Mismatched Trials: "
                                    + comparisonReport.mismatchedTrials + ", Contain Same Number of Trials: "
                                    + comparisonReport.missionsContainSameNumberOfTrials);
                        }
                        if (dataValid) {
                            //Extract the trial data and compute trial metrics for each trial
                            if (outputProgress) {
                                System.out.println("Processing trial metrics for: " + subject.getSubjectId() + ", " + missionId);
                            }
                            ResponseGeneratorData responseGenerator = missionResponse.getResponseGenerator();
                            int trialIndex = 0;
                            List<TrialData> currSubjectTrialData = new LinkedList<TrialData>();
                            for (IcarusTestTrial_Phase2 trialResponse : missionResponse.getTestTrials()) {
                                TrialData currTrialData = trialResponseProcessor.updateTrialData(
                                        exam, missionResponse, trialResponse,
                                        trialResponse.getResponseFeedBack(), false, null,
                                        examId, missionId, missionNum, responseGenerator);
                                trialResponseProcessor.updateTrialMetrics(currTrialData, metricsInfo, null);
                                currTrialData.setData_type(DataType.Human_Single);
                                currSubjectTrialData.add(currTrialData);                                
                                trialIndex++;
                            }
                            
                            //Compute the subject score for the mission
                            SubjectScore subjectScore = computeSubjectScore(currSubjectTrialData,
                                    missionResponse.getMissionType());
                            subjectScore.subject = subject;                            
                            currSubjectScores.add(subjectScore);

                            //Compute mission metrics for the mission  
                            Double time = (missionResponse.getStartTime() != null && missionResponse.getEndTime() != null)
                                    ? new Double(missionResponse.getEndTime().getTime() - missionResponse.getStartTime().getTime()) : null;
                            MissionMetrics subjectMissionMetrics = missionMetricsComputer.updateTaskMetrics(
                                    null, time, currSubjectTrialData, metricsInfo, null, missionNum);
                            subjectMissionMetrics.setTask_id(missionId);
                            
                            //If Mission 1, store the average SIGINT probabilities, which will be used to compute
                            //incremental Bayesian solutions in Missions 2 & 3                                
                            if (missionResponse.getMissionType() == MissionType.Mission_1) {                                
                                subjectSigintProbabilities.put(subject.getSubjectId(), 
                                        subjectMissionMetrics.getSigintProbs_avg());
                            }
                        }
                    } else {
                        System.err.println("Warning, no trials found for file: " + file.getName());
                    }
                }
            }
            missionIndex++;
        }
        
        return subjectScores;
    }
    
    /**
     *
     * @param trialData
     * @return
     */
    public static SubjectScore computeSubjectScore(List<TrialData> trialData, MissionType missionType) {
        /*
        1) Count the number of times the subject entered the same probability (+/- 1).
        2) Count the number of times the subject entered a probability more than 10% different from the correct BLUEBOOK value (Missions 1, 3)
        3) Count the number of times the subject selected the same SIGINT location.
        4) Count the number of times the subject selected the same Blue Action.
        5) If any 2 of the above frequencies are greater than 0.95, remove the subject. From the mission.*/    
        SubjectScore sc = new SubjectScore();
        if(trialData != null && !trialData.isEmpty()) {
            HashMap<Integer, Integer> probabilityCounts = new HashMap<Integer, Integer>();
            int probabiltyTotalCount = 0;            
            int incorrectBluebookCount = 0;
            int bluebookTotalCount = 0;
            HashMap<Integer, Integer> sigintLocationCounts = new HashMap<Integer, Integer>();
            int sigingLocationTotalCount = 0;
            HashMap<BlueActionType, Integer> blueActionCounts = new HashMap<BlueActionType, Integer>();
            int blueActionsTotalCount = 0;            
            for(TrialData trial : trialData) {
                //Update probability counts
                AttackProbabilityReportData probs_Pp = null;
                if(trial.getRedTacticsReport() != null && trial.getRedTacticsReport().getRedTacticProbabilities() != null
                        && !trial.getRedTacticsReport().getRedTacticProbabilities().isEmpty()) {
                    for(Double prob : trial.getRedTacticsReport().getRedTacticProbabilities()) {
                        if(prob != null) {
                            updateProbabilityCounts(probabilityCounts, prob);
                            probabiltyTotalCount++;
                        }
                    }
                }
                if(trial.getAttackProbabilityReports() != null && !trial.getAttackProbabilityReports().isEmpty()) {
                   for(AttackProbabilityReportData probReport: trial.getAttackProbabilityReports()) {
                       if(probReport.getProbabilities() != null && !probReport.getProbabilities().isEmpty()) {
                           if(probReport.getTrialPartType() == TrialPartProbeType.AttackProbabilityReport_Pp) {
                               probs_Pp = probReport;
                           }
                           for(Double prob : probReport.getProbabilities()) {
                               if (prob != null) {
                                   updateProbabilityCounts(probabilityCounts, prob);
                                   probabiltyTotalCount++;
                               }
                           }
                       }
                   }
                }
                
                //Update BLUEBOOK mistake counts
                if ((missionType == MissionType.Mission_1 || missionType == MissionType.Mission_3) &&
                        probs_Pp != null && probs_Pp.getProbabilities_normative_incremental() != null
                        && probs_Pp.getProbabilities().size()
                        == probs_Pp.getProbabilities_normative_incremental().size()) {
                    for (int i = 0; i < probs_Pp.getProbabilities().size(); i++) {
                        Double prob = probs_Pp.getProbabilities().get(i);
                        Double bluebookProb = probs_Pp.getProbabilities_normative_incremental().get(i);
                        if(prob != null && bluebookProb != null) {
                            if(Math.abs(prob - bluebookProb) >= 0.1) {  
                                incorrectBluebookCount++;
                            }
                            bluebookTotalCount++;
                        }
                    }
                }

                //Update SIGINT location count
                if (trial.getSigintSelection() != null
                        && trial.getSigintSelection().getSigintLocations() != null
                        && !trial.getSigintSelection().getSigintLocations().isEmpty()) {
                    for (String sigintLocation : trial.getSigintSelection().getSigintLocations()) {
                        int locationIndex = AssessmentUtils_Phase2.parseLocationIndexAsInt(sigintLocation);
                        if (sigintLocationCounts.get(locationIndex) == null) {
                            sigintLocationCounts.put(locationIndex, 1);
                        } else {
                            sigintLocationCounts.put(locationIndex,
                                    sigintLocationCounts.get(locationIndex) + 1);
                        }
                        sigingLocationTotalCount++;
                    }
                }
                
                //Update Blue action selection count
                if (trial.getBlueActionSelection() != null                         
                        && trial.getBlueActionSelection().getBlueActions() != null
                        && !trial.getBlueActionSelection().getBlueActions().isEmpty()) {
                    for(BlueActionType blueAction : trial.getBlueActionSelection().getBlueActions()) {
                        if (blueActionCounts.get(blueAction) == null) {
                            blueActionCounts.put(blueAction, 1);
                        } else {
                            blueActionCounts.put(blueAction,
                                    blueActionCounts.get(blueAction) + 1);
                        }
                        blueActionsTotalCount++;
                    }
                }
            }
            
            sc.sameProbabilityFrequency = computeMaxFrequency(probabilityCounts,
                    probabiltyTotalCount);
            sc.incorrectBluebookProbabilityFrequency = bluebookTotalCount > 0 ?
                   (double) incorrectBluebookCount / bluebookTotalCount : 0.d;
            sc.sameSigintLocationFrequency = computeMaxFrequency(sigintLocationCounts,
                    sigingLocationTotalCount);
            sc.sameBlueActionFrequency = computeMaxFrequency(blueActionCounts, 
                    blueActionsTotalCount);
        }
        return sc;
    }
    
    private static void updateProbabilityCounts(HashMap<Integer, Integer> probabilityCounts, Double prob) {
        if (prob != null) {
            Integer roundedProb = (int) Math.round(prob);
            if (probabilityCounts.get(roundedProb) == null) {
                probabilityCounts.put(roundedProb, 1);
            } else {
                probabilityCounts.put(roundedProb,
                        probabilityCounts.get(roundedProb) + 1);
            }
        }
    }
    
    private static Double computeMaxFrequency(Map<?, Integer> counts, int totalCount) {
        Double maxFreq = 0.d;
        if(totalCount > 0 && counts != null && !counts.isEmpty()) {
            for(Integer count : counts.values()) {
                double freq = (double) count / totalCount;
                if(freq > maxFreq) {
                    maxFreq = freq;
                }
            }
        }
        return maxFreq;
    }
    
    public static void main(String[] args) {
        String examId = "Final-Exam-1";
        //List<String> missionIds = new ArrayList<String>(Arrays.asList("Mission4"));
        //List<Integer> missionNumbers = new ArrayList<Integer>(Arrays.asList(4));
        List<String> missionIds = new ArrayList<String>(Arrays.asList("Mission1", 
               "Mission2", "Mission3", "Mission4", "Mission5"));
        List<Integer> missionNumbers = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5));
        
        File examFile = new File(BatchFileProcessor.assessmentFolder + examId + "/Exam_Data/" + examId + ".xml");
        File dataDir = new File(BatchFileProcessor.assessmentFolder + examId + "/Human_Data/All Subjects");        
        
        Set<IcarusSubjectData> allSubjects = null;
        List<Set<IcarusSubjectData>> subjects = null;
        try {
            allSubjects = DataAggregatorUtils.getSubjectsInFolder(dataDir, true);
            subjects = new ArrayList<Set<IcarusSubjectData>>();
            for (int i = 0; i < 5; i++) {
                subjects.add(allSubjects);
            }
        } catch (IOException ex) {
            ex.printStackTrace();;
        }
        
        //Load the exam and feature vector files
        IcarusExam_Phase2 exam = null;
        try {
            URL examFileUrl = examFile.toURI().toURL();
            exam = IcarusExamLoader_Phase2.unmarshalExam(examFileUrl, false);
            exam.setId(examId);            
            IcarusExamLoader_Phase2.initializeExamFeatureVectorData(exam, examFileUrl, null);
        } catch (JAXBException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }        
        
        if(allSubjects != null && subjects != null && exam != null) {
            double probThreshold = 0.9d;
            double bluebookThreshold = 0.5d;
            double sigintThreshold = 0.9d;
            double blueActionThreshold = 0.9d;
            System.out.println("Num subjects: " + allSubjects.size());
            List<List<SubjectScore>> subjectScores = computeSubjectScores(exam, 
                missionIds, missionNumbers, dataDir, subjects, 
                MetricsInfo.createDefaultMetricsInfo(), true);
            if (subjectScores != null) {
                for (int i = 0; i < subjectScores.size(); i++) {
                    String missionId = missionIds.get(i);
                    System.out.println();
                    System.out.println("--------- Mission " + missionId + " ---------");
                    for (SubjectScore subjectScore : subjectScores.get(i)) {
                        if (subjectScore.sameProbabilityFrequency > probThreshold
                                || subjectScore.incorrectBluebookProbabilityFrequency > bluebookThreshold
                                || subjectScore.sameSigintLocationFrequency > sigintThreshold
                                || subjectScore.sameBlueActionFrequency > blueActionThreshold) {
                            System.out.println("Subject: "
                                    + (subjectScore.subject.getSite() != null
                                    ? subjectScore.subject.getSite().getName() : "Null")
                                    + "-" + subjectScore.subject.getSubjectId() + "");
                            System.out.println("Same Prob Freq: " + subjectScore.sameProbabilityFrequency);
                            System.out.println("Incorrect Bluebook Freq: " + subjectScore.incorrectBluebookProbabilityFrequency);
                            System.out.println("Same SIGINT location Freq: " + subjectScore.sameSigintLocationFrequency);
                            System.out.println("Same Blue action Freq: " + subjectScore.sameBlueActionFrequency);
                            System.out.println();
                        }
                    }
                }
            }
        }
    }    
    
    /**
     * Contains metrics that may indicate a subject didn't properly understand the exam.
     */
    public static class SubjectScore {
        
        /** The subject */
        protected IcarusSubjectData subject;
        
        /** The frequency with which the subject entered the same probability */
        protected Double sameProbabilityFrequency;
        
        /** The frequency with which the subject entered an incorrect BLUEBOOK probability */
        protected Double incorrectBluebookProbabilityFrequency;
        
        /** The frequency with which the subject selected the same location to obtain SIGINT at */
        protected Double sameSigintLocationFrequency;
        
        /** The frequency with which the subject selected the same Blue action */
        protected Double sameBlueActionFrequency;
        
        public SubjectScore() {}
        
        public SubjectScore(IcarusSubjectData subject) {
            this.subject = subject;
        }

        public IcarusSubjectData getSubject() {
            return subject;
        }

        public void setSubject(IcarusSubjectData subject) {
            this.subject = subject;
        }

        public Double getSameProbabilityFrequency() {
            return sameProbabilityFrequency;
        }

        public void setSameProbabilityFrequency(Double sameProbabilityFrequency) {
            this.sameProbabilityFrequency = sameProbabilityFrequency;
        }

        public Double getIncorrectBluebookProbabilityFrequency() {
            return incorrectBluebookProbabilityFrequency;
        }

        public void setIncorrectBluebookProbabilityFrequency(Double incorrectBluebookProbabilityFrequency) {
            this.incorrectBluebookProbabilityFrequency = incorrectBluebookProbabilityFrequency;
        }

        public Double getSameSigintLocationFrequency() {
            return sameSigintLocationFrequency;
        }

        public void setSameSigintLocationFrequency(Double sameSigintLocationFrequency) {
            this.sameSigintLocationFrequency = sameSigintLocationFrequency;
        }

        public Double getSameBlueActionFrequency() {
            return sameBlueActionFrequency;
        }

        public void setSameBlueActionFrequency(Double sameBlueActionFrequency) {
            this.sameBlueActionFrequency = sameBlueActionFrequency;
        }
    }
}