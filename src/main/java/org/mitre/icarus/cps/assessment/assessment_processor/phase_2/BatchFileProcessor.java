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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.xml.bind.JAXBException;

import org.mitre.icarus.cps.app.experiment.IcarusSubjectData;
import org.mitre.icarus.cps.app.experiment.data_recorder.SubjectDataRecorderUtils;
import org.mitre.icarus.cps.assessment.assessment_metrics_computer.phase_2.ExamMetricsComputer;
import org.mitre.icarus.cps.assessment.assessment_metrics_computer.phase_2.MetricSignificanceComputer;
import org.mitre.icarus.cps.assessment.assessment_metrics_computer.phase_2.MissionMetricsComputer;
import org.mitre.icarus.cps.assessment.assessment_metrics_computer.phase_2.TrialMetricsComputer;
import org.mitre.icarus.cps.assessment.data_aggregator.DataAggregatorUtils;
import org.mitre.icarus.cps.assessment.data_model.base.DataType;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.CFAMetric;
import org.mitre.icarus.cps.assessment.data_model.base.data_sets.AverageHumanDataSet;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.ExamMetrics;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.MetricsInfo;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.MissionMetrics;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.RSRAndASRMissionMetrics;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.SubjectMetrics;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.SubjectSigintProbabilities;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.TrialData;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.significance.ExamSignificanceReport;
import org.mitre.icarus.cps.assessment.data_model.phase_2.data_sets.AverageHumanDataSet_Phase2;
import org.mitre.icarus.cps.assessment.data_model.phase_2.data_sets.SingleModelDataSet_Phase2;
import org.mitre.icarus.cps.assessment.model_simulator.phase_2.ModelFactory;
import org.mitre.icarus.cps.assessment.model_simulator.phase_2.Model_Phase2;
import org.mitre.icarus.cps.assessment.persistence.phase_2.spreadsheet.SpreadsheetCPADataPersister;
import org.mitre.icarus.cps.assessment.persistence.phase_2.xml.XMLCPADataPersister;
import org.mitre.icarus.cps.assessment.score_computer.phase_2.ScoreComputer_Phase2;
import org.mitre.icarus.cps.assessment.utils.phase_2.AssessmentUtils_Phase2;
import org.mitre.icarus.cps.exam.base.loader.IcarusExamLoader;
import org.mitre.icarus.cps.exam.base.response.ResponseGeneratorData;
import org.mitre.icarus.cps.exam.phase_2.IcarusExam_Phase2;
import org.mitre.icarus.cps.exam.phase_2.loader.IcarusExamLoader_Phase2;
import org.mitre.icarus.cps.exam.phase_2.testing.IcarusTestTrial_Phase2;
import org.mitre.icarus.cps.exam.phase_2.testing.Mission;
import org.mitre.icarus.cps.exam.phase_2.testing.MissionType;

/**
 * Processes Phase 2 human subject data files in batch and generates the average
 * human response. Also processes model data files and generates model assessment
 * results.
 * 
 * @author CBONACETO
 */
public class BatchFileProcessor {
    
    /** The assessment data folder */
    public static String assessmentFolder = "data/Phase_2_CPD/assessment/";
    
    /** The trial response processor */
    protected TrialResponseProcessor trialResponseProcessor;

    /** The trial metrics computer */
    protected TrialMetricsComputer trialMetricsComputer;    

    /** The mission metrics computer */
    protected MissionMetricsComputer missionMetricsComputer;

    /** The exam metrics computer */
    protected ExamMetricsComputer examMetricsComputer;

    /** Phase 2 score computer instance */
    protected ScoreComputer_Phase2 scoreComputer;
    
    /** The Phase 2 exam loader */
    protected IcarusExamLoader_Phase2 examLoader;

    public BatchFileProcessor() {
        trialMetricsComputer = new TrialMetricsComputer();        
        scoreComputer = new ScoreComputer_Phase2();
        trialResponseProcessor = new TrialResponseProcessor(
                trialMetricsComputer, scoreComputer);        
        missionMetricsComputer = new MissionMetricsComputer();
        examMetricsComputer = new ExamMetricsComputer();        
        examLoader = IcarusExamLoader_Phase2.getInstance();
    }

    /**
     * Process the overall data set for humans as a batch and generate the
     * average human data set.
     * 
     * @param examId
     * @param missionIds
     * @param missionNumbers
     * @return
     */
    public static AverageHumanDataSet_Phase2 buildAndPersistAverageHumanDataSet(
            String examId, List<String> missionIds, List<Integer> missionNumbers) {        
        /*List<String> missionIds = new ArrayList<String>(Arrays.asList("Mission4", "Mission5"));
        List<Integer> missionNumbers = new ArrayList<Integer>(Arrays.asList(4, 5));*/
        /*List<String> missionIds = new ArrayList<String>(Arrays.asList("Mission1", 
                "Mission2", "Mission3", "Mission4", "Mission5"));
        List<Integer> missionNumbers = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5));*/
        File examFile = new File(assessmentFolder + examId + "/Exam_Data/" + examId + ".xml");
        File dataDir = new File(assessmentFolder + examId + "/Human_Data/All Subjects");
        File outputDir = new File(assessmentFolder + examId + "/Assessment_Results");

        AverageHumanDataSet_Phase2 dataSet = null;
        try {            
            Set<IcarusSubjectData> allSubjects = DataAggregatorUtils.getSubjectsInFolder(dataDir, true);
            System.out.println("Num subjects: " + allSubjects.size());            

            List<Set<IcarusSubjectData>> subjects = new ArrayList<Set<IcarusSubjectData>>();
            for (int i = 0; i < 5; i++) {
                subjects.add(allSubjects);
            }

            //Process the data set
            BatchFileProcessor bfp = new BatchFileProcessor();
            dataSet = bfp.buildAverageHumanDataSet(examId, missionIds, missionNumbers, examFile, dataDir, subjects,
                    MetricsInfo.createDefaultMetricsInfo(), true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //Serialize data set to XML and write file
        if (dataSet != null) {
            try {
                XMLCPADataPersister xmlPersister = new XMLCPADataPersister(outputDir.toURI().toURL());
                xmlPersister.persistAverageHumanDataSet(dataSet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return dataSet;
    }

    /**
     * Process the overall data set for humans as a batch and generate the
     * average human data set.
     *
     * @param examId
     * @param missionIds
     * @param missionNumbers
     * @param examFile
     * @param dataDir
     * @param subjects
     * @param metricsInfo
     * @param outputProgress
     * @return
     */
    public AverageHumanDataSet_Phase2 buildAverageHumanDataSet(String examId, 
            List<String> missionIds, List<Integer> missionNumbers,
            File examFile, File dataDir, List<Set<IcarusSubjectData>> subjects, 
            MetricsInfo metricsInfo, boolean outputProgress) {
        //Load the exam and feature vector files
        IcarusExam_Phase2 exam = null;
        try {
            URL examFileUrl = examFile.toURI().toURL();
            exam = IcarusExamLoader_Phase2.unmarshalExam(examFileUrl, false);
            exam.setId(examId);            
            IcarusExamLoader_Phase2.initializeExamFeatureVectorData(exam, examFileUrl, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (exam == null) {
            return null;
        }
        return buildAverageHumanDataSet(exam, missionIds, missionNumbers, dataDir, 
                subjects, metricsInfo, outputProgress);
    }

    /**
     * Process the overall data set for humans as a batch and generate the
     * average human data set
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
    public AverageHumanDataSet_Phase2 buildAverageHumanDataSet(
            IcarusExam_Phase2 exam, List<String> missionIds, List<Integer> missionNumbers,
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

        //Mission metrics for each subject on each mission (Mission->Subject Mission Metrics)
        List<List<MissionMetrics>> missionMetricsBySubject = new ArrayList<List<MissionMetrics>>(numMissions);
        //Trial metrics for each subject on each trial of each mission (Mission->Trials->Subject Trial Data)
        List<List<List<TrialData>>> trialDataByMission = new ArrayList<List<List<TrialData>>>(numMissions);
        for (Mission<?> mission : exam.getMissions()) {
            if (missionIds.contains(mission.getId())) {
                List<List<TrialData>> currMissionTrialData = new ArrayList<List<TrialData>>(mission.getNumTrials());
                trialDataByMission.add(currMissionTrialData);
                for (int i = 0; i < mission.getNumTrials(); i++) {
                    currMissionTrialData.add(new LinkedList<TrialData>());
                }
            }
        }
        
        //Subject SIGINT probabilities for each subject (computed using Mission 1 P(Attack|SIGINT) reports)
        HashMap<String, SubjectSigintProbabilities> subjectSigintProbabilities = 
                new HashMap<String, SubjectSigintProbabilities>();

        //Process subject data for each mission
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
                List<MissionMetrics> currMissionMetrics = new LinkedList<MissionMetrics>(); //Mission metrics for each subject on the current mission
                missionMetricsBySubject.add(currMissionMetrics);
                List<List<TrialData>> currMissionTrialData = trialDataByMission.get(missionIndex);
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
                                currMissionTrialData.get(trialIndex).add(currTrialData);
                                trialIndex++;
                            }

                            //Compute mission metrics for the mission  
                            Double time = (missionResponse.getStartTime() != null && missionResponse.getEndTime() != null)
                                    ? new Double(missionResponse.getEndTime().getTime() - missionResponse.getStartTime().getTime()) : null;
                            MissionMetrics subjectMissionMetrics = missionMetricsComputer.updateTaskMetrics(
                                    null, time, currSubjectTrialData, metricsInfo, null, missionNum);
                            subjectMissionMetrics.setTask_id(missionId);
                            currMissionMetrics.add(subjectMissionMetrics);
                            
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

        //Compute the average trial metrics over all subjects for each trial in each mission
        List<MissionMetrics> averageMissionMetrics = new ArrayList<MissionMetrics>();
        for (missionIndex = 0; missionIndex < numMissions; missionIndex++) {
            int missionNum = missionNumbers.get(missionIndex);
            List<List<TrialData>> trialData = trialDataByMission.get(missionIndex);
            String missionId = missionIds.get(missionIndex);
            MissionMetrics missionMetrics = new MissionMetrics();
            averageMissionMetrics.add(missionMetrics);
            List<TrialData> avgTrialData = new ArrayList<TrialData>();
            missionMetrics.setTrials(avgTrialData);
            missionMetrics.setData_type(DataType.Human_Avg);
            missionMetrics.setHuman(true);
            missionMetrics.setExam_id(examId);
            missionMetrics.setTask_id(missionId);
            missionMetrics.setTask_number(missionNum);
            missionMetrics.setTask_complete(true);
            missionMetrics.setAll_trials_valid(true);
            missionMetrics.setNum_subjects(null);
            if (outputProgress) {
                System.out.println("Computing average trial and mission metrics for " + missionId);
            }
            for (List<TrialData> subjectTrials : trialData) {
                if (missionMetrics.getNum_subjects() == null && subjectTrials != null) {
                    missionMetrics.setNum_subjects(subjectTrials.size());
                }
                //Compute average trial over each of the subject trials
                TrialData averageTrial = new TrialData();
                averageTrial.setData_type(DataType.Human_Avg);
                averageTrial.setHuman(true);
                averageTrial.setExam_id(examId);
                averageTrial.setTask_id(missionId);
                averageTrial.setTask_number(missionNum);
                averageTrial.setTrial_number((subjectTrials != null && !subjectTrials.isEmpty()) ? subjectTrials.get(0).getTrial_number() : null);
                averageTrial.setTrial_complete(true);
                averageTrial.setTrial_valid(true);
                averageTrial = trialMetricsComputer.updateAverageTrialMetrics(
                        subjectTrials, averageTrial, null, metricsInfo, missionNum);
                avgTrialData.add(averageTrial);
            }
            
            //Compute the average mission metrics over all subjects for each mission using the average
            //trial data for trial-based metrics and the mission metrics for each subject for mission-based            
            missionMetricsComputer.updateAverageTaskMetrics(missionMetricsBySubject.get(missionIndex), 
                    missionMetrics, avgTrialData, metricsInfo, missionMetrics, missionNum);
            
            /*//Compute the average mission metrics using the average trials
            missionMetricsComputer.updateTaskMetrics(missionMetrics, timeOnTask.get(missionIndex), 
                    avgTrialData, metricsInfo, null, missionNum);*/
        }        

        if (outputProgress) {
            System.out.println("Complete");
        }

        AverageHumanDataSet_Phase2 dataSet = new AverageHumanDataSet_Phase2();
        dataSet.setExam_id(examId);
        dataSet.setTime_stamp(System.currentTimeMillis());
        dataSet.setMetricsInfo(metricsInfo);
        dataSet.setTaskMetrics(averageMissionMetrics);
        return dataSet;
    }
    
    /**
     *
     * @param examId
     * @param missionIds
     * @param missionNumbers
     * @param siteId
     * @param responseGeneratorId
     * @param comparisonData
     * @return
     */
    public static SingleModelDataSet_Phase2 buildAndPersistModelDataSet(String examId,
            List<String> missionIds, List<Integer> missionNumbers, String siteId, String responseGeneratorId, 
            AverageHumanDataSet<TrialData, MissionMetrics, SubjectMetrics, MetricsInfo> comparisonData) {
        /*String examId = "Final-Exam-1";
        List<String> missionIds = new ArrayList<String>(Arrays.asList("Mission1", 
                "Mission2", "Mission3", "Mission4", "Mission5"));
        List<Integer> missionNumbers = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5));*/
        File examFile = new File(assessmentFolder + examId + "/Exam_Data/" + examId + ".xml");
        File outputDir = new File(assessmentFolder + examId + "/Assessment_Results");

        //Get the exam response file for the site
        File examResponseFile = null;
        if (siteId.equals("BBN")) {
            examResponseFile = new File(assessmentFolder + examId + "/Model_Data/BBN/Run_Final/"
                    + SubjectDataRecorderUtils.formatExamFileName(siteId, responseGeneratorId, examId));
        } else if (siteId.equals("HRL")) {
            examResponseFile = new File(assessmentFolder + examId + "/Model_Data/HRL/Run_Final/"
                    + SubjectDataRecorderUtils.formatExamFileName(siteId, responseGeneratorId, examId));
        } else if (siteId.equals("LMC")) {
            examResponseFile = new File(assessmentFolder + examId + "/Model_Data/LMC/Run_Final/"
                    + SubjectDataRecorderUtils.formatExamFileName(siteId, responseGeneratorId, examId));
        } else {
            System.err.println("Unrecognized site: " + siteId);
        }

        SingleModelDataSet_Phase2 dataSet = null;
        if (examResponseFile != null) {
            //System.out.println("Opening response file: " + examResponseFile);
            BatchFileProcessor bfp = new BatchFileProcessor();
            dataSet = bfp.buildModelDataSet(examId, missionIds, missionNumbers,
                    examFile, examResponseFile, MetricsInfo.createDefaultMetricsInfo(), 
                    comparisonData, true, true);
        }

        //Serialize data set to XML and write file
        if (dataSet != null) {
            try {
                XMLCPADataPersister xmlPersister = new XMLCPADataPersister(outputDir.toURI().toURL());
                xmlPersister.persistSingleModelDataSet(dataSet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return dataSet;
    }
    
    /**
     *
     * @param examId
     * @param missionIds
     * @param missionNumbers
     * @param model
     * @param comparisonData
     * @return
     */
    public static SingleModelDataSet_Phase2 buildAndPersistSampleModelDataSet(
            String examId, List<String> missionIds, List<Integer> missionNumbers,
            Model_Phase2 model, 
            AverageHumanDataSet<TrialData, MissionMetrics, SubjectMetrics, MetricsInfo> comparisonData) {
        //String examId = "Final-Exam-1";
        /*List<String> missionIds = new ArrayList<String>(Arrays.asList("Mission1", 
                "Mission2", "Mission3", "Mission4", "Mission5"));
        List<Integer> missionNumbers = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5));*/
        File examFile = new File(assessmentFolder + examId + "/Exam_Data/" + examId + ".xml");
        File outputDir = new File(assessmentFolder + examId + "/Assessment_Results");

        //Load the exam and feature vector data
        IcarusExam_Phase2 exam = null;
        try {
            URL examFileUrl = examFile.toURI().toURL();
            exam = IcarusExamLoader_Phase2.unmarshalExam(examFileUrl, false);
            exam.setId(examId);
            IcarusExamLoader_Phase2.initializeExamFeatureVectorData(exam, examFileUrl, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (exam == null) {
            return null;
        }

        //Generate the exam responses using the sample model
        IcarusExam_Phase2 examResponse = model.generateExamResponses(exam);
        examResponse.setResponseGenerator(new ResponseGeneratorData(model.getSiteId(), model.getResponseGeneratorId(), false));
        
        //Save the model exam response file        
        try {
            //data\Phase_2\Final-Exam-1\Model_Data\MITRE\Run_Final
            File examResponseFile = new File(assessmentFolder + examId + "/Model_Data/" + model.getSiteId() + "/Run_Final/" +
                    SubjectDataRecorderUtils.formatExamFileName(model.getSiteId(), model.getResponseGeneratorId(), examId));
            IcarusExamLoader.writeFile(IcarusExamLoader_Phase2.marshalExam(examResponse), examResponseFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }
        
        //Generate the model data set
        BatchFileProcessor bfp = new BatchFileProcessor();
        SingleModelDataSet_Phase2 dataSet = bfp.buildModelDataSet(exam, examResponse, 
                missionIds, missionNumbers, MetricsInfo.createDefaultMetricsInfo(), 
                comparisonData, false, true);

        //Serialize data set to XML and write file
        if (dataSet != null) {
            try {
                XMLCPADataPersister xmlPersister = new XMLCPADataPersister(outputDir.toURI().toURL());
                xmlPersister.persistSingleModelDataSet(dataSet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return dataSet;
    }

    /**
     *
     * @param examId
     * @param missionIds
     * @param missionNumbers
     * @param examFile
     * @param examResponseFile
     * @param metricsInfo
     * @param comparisonData
     * @param checkForDecimalProbs
     * @param outputProgress
     * @return
     */    
    public SingleModelDataSet_Phase2 buildModelDataSet(String examId, 
            List<String> missionIds, List<Integer> missionNumbers,
            File examFile, File examResponseFile, MetricsInfo metricsInfo,
            AverageHumanDataSet<TrialData, MissionMetrics, SubjectMetrics, 
            MetricsInfo> comparisonData, boolean checkForDecimalProbs, 
            boolean outputProgress) {
        
        //Load the exam and feature vector data (used to compute normative solutions)
        IcarusExam_Phase2 exam = null;
        try {
            URL examFileUrl = examFile.toURI().toURL();
            exam = IcarusExamLoader_Phase2.unmarshalExam(examFileUrl, false);
            exam.setId(examId);
            IcarusExamLoader_Phase2.initializeExamFeatureVectorData(exam, examFileUrl, null);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (exam == null) {
            return null;
        }

        //Load the model exam response file (contains responses to all missions)
        IcarusExam_Phase2 examResponse = null;
        try {
            examResponse = IcarusExamLoader_Phase2.unmarshalExam(examResponseFile.toURI().toURL(), false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (examResponse == null || examResponse.getMissions() == null || examResponse.getMissions().isEmpty()) {
            System.err.println("Exam response not loaded or did not contain any missions");
            return null;
        }
        return buildModelDataSet(exam, examResponse, missionIds, missionNumbers, metricsInfo, comparisonData,
                checkForDecimalProbs, outputProgress);
    }
    
    /**
     *
     * @param exam
     * @param examResponse
     * @param missionIds
     * @param missionNumbers
     * @param metricsInfo
     * @param comparisonData
     * @param checkForDecimalProbs
     * @param outputProgress
     * @return
     */
    public SingleModelDataSet_Phase2 buildModelDataSet(IcarusExam_Phase2 exam, 
            IcarusExam_Phase2 examResponse, List<String> missionIds, 
            List<Integer> missionNumbers, MetricsInfo metricsInfo,
            AverageHumanDataSet<TrialData, MissionMetrics, SubjectMetrics, MetricsInfo> comparisonData,
            boolean checkForDecimalProbs, boolean outputProgress) {
        String examId = exam.getId();
        
        //Convert decimal probabilities to percent probabilities if necessary
        if(checkForDecimalProbs) {
            boolean decimalProbs = AssessmentUtils_Phase2.convertDecimalProbsToPercentProbs(examResponse);
            if(outputProgress && decimalProbs) {
                System.out.println("Converted model probabilities from decimal to percent probabilities.");
            }
        }

        //Add the model respones of each mission to the exam missions and score the mission to compute feedback
        for (Mission<?> mission : examResponse.getMissions()) {
            try {
                Mission<?> scoredMission = AssessmentUtils_Phase2.addSubjectResponsesToExamMission(exam, mission);
                scoreComputer.computeScoreForMission(scoredMission, exam.getPayoffMatrix(), true, null);                
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        exam.setResponseGenerator(examResponse.getResponseGenerator());
        examResponse = exam;

        //DEBUG CODE
        //try {
        //	System.out.println(IcarusExamLoader_Phase2.marshalExam(examResponse));
        //} catch(Exception ex) {}
        metricsInfo = (metricsInfo == null) ? MetricsInfo.createDefaultMetricsInfo() : metricsInfo;
        ResponseGeneratorData responseGenerator = examResponse.getResponseGenerator();

        //Mission and trial metrics for each mission
        List<MissionMetrics> missionMetrics = new ArrayList<MissionMetrics>(missionIds.size());        

        //Process model response data for each mission
        int missionIndex = 0;
        for (String missionId : missionIds) {
            //Find the mission in the exam response
            Mission<?> missionResponse = null;
            for (Mission<?> mission : examResponse.getMissions()) {
                if (mission.getId().equalsIgnoreCase(missionId)) {
                    missionResponse = mission;
                    break;
                }
            }
            //Process the mission response data 
            if (missionResponse != null && missionResponse.getTestTrials() != null 
                    && !missionResponse.getTestTrials().isEmpty()) {
                ResponseGeneratorData currResponseGenerator = responseGenerator != null ? 
                        responseGenerator : missionResponse.getResponseGenerator();
                String generatorName = currResponseGenerator != null ? 
                        currResponseGenerator.getSiteId() + "-" 
                        + currResponseGenerator.getResponseGeneratorId() : "";
                int missionNum = missionNumbers.get(missionIndex);
                MissionMetrics currMissionMetrics = new MissionMetrics();
                missionMetrics.add(currMissionMetrics);
                List<TrialData> currMissionTrialData = new ArrayList<TrialData>(
                        missionResponse.getTestTrials().size());
                currMissionMetrics.setTrials(currMissionTrialData);
                if (currResponseGenerator != null) {
                    currMissionMetrics.setSite_id(currResponseGenerator.getSiteId());
                    currMissionMetrics.setResponse_generator_id(currResponseGenerator.getResponseGeneratorId());
                }
                currMissionMetrics.setData_type(DataType.Model_Single);
                currMissionMetrics.setHuman(false);
                currMissionMetrics.setExam_id(examId);
                currMissionMetrics.setTask_id(missionId);
                currMissionMetrics.setTask_number(missionNum);
                currMissionMetrics.setNum_subjects(1);

                //Get the comparison mission metrics for the current mission if available 
                //(e.g., the average human mission metrics)
                MissionMetrics comparisonMissionMetrics = null;
                if (comparisonData != null && comparisonData.getTaskMetrics() != null && 
                        !comparisonData.getTaskMetrics().isEmpty()) {
                    for (MissionMetrics metrics : comparisonData.getTaskMetrics()) {
                        if ((metrics.getTask_id() != null && metrics.getTask_id().equalsIgnoreCase(missionId))
                                || (metrics.getTask_number() != null && metrics.getTask_number() == missionNum)) {
                            comparisonMissionMetrics = metrics;
                            break;
                        }
                    }
                }
                //System.out.println("Comparison mission metrics: " + comparisonMissionMetrics);

                //Compute trial metrics for each trial
                if (outputProgress) {
                    System.out.println("Processing trial metrics for: " + generatorName + ", " + missionId);
                }                
                int trialIndex = 0;
                for (IcarusTestTrial_Phase2 trialResponse : missionResponse.getTestTrials()) {                                  
                    TrialData currTrialData = trialResponseProcessor.updateTrialData(
                            exam, missionResponse, trialResponse, trialResponse.getResponseFeedBack(),
                            false, null, examId, missionId, missionNum, currResponseGenerator);
                    trialResponseProcessor.updateTrialMetrics(currTrialData, metricsInfo,
                            comparisonMissionMetrics != null && comparisonMissionMetrics.getTrials() != null
                            && trialIndex < comparisonMissionMetrics.getTrials().size() ? 
                                    comparisonMissionMetrics.getTrials().get(trialIndex) : null);
                    currTrialData.setData_type(DataType.Model_Single);
                    currMissionTrialData.add(currTrialData);                    
                    trialIndex++;
                }

                //Compute the mission metrics for the current mission
                missionMetricsComputer.updateCompletionStatus(currMissionMetrics, currMissionTrialData);
                if (currMissionMetrics.isTask_complete()) {
                    Double taskTime = (missionResponse.getStartTime() != null && missionResponse.getEndTime() != null)
                            ? new Double(missionResponse.getEndTime().getTime() - missionResponse.getStartTime().getTime()) : null;
                    missionMetricsComputer.updateTaskMetrics(currMissionMetrics, taskTime, currMissionTrialData, metricsInfo,
                            comparisonMissionMetrics, missionNum);

                    //If Mission 1, store the average SIGINT probabilities, which will be used to compute
                    //incremental Bayesian solutions in Missions 2 & 3                                
                    if (missionResponse.getMissionType() == MissionType.Mission_1) {
                       trialResponseProcessor.setSubjectSigintProbs(currMissionMetrics.getSigintProbs_avg());
                    }
                } else {
                    System.err.println("Warning, mission not complete: " + missionId);
                }

            } else {
                System.err.println("Warning, mission missining or did not contain any trials: " + missionId);
            }
            missionIndex++;
        }        

        //Compute the overall exam metrics
        ExamMetrics examMetrics = new ExamMetrics();
        examMetrics.setTasks(missionMetrics);
        examMetrics.setExam_id(examId);
        if (responseGenerator != null) {
            examMetrics.setSite_id(responseGenerator.getSiteId());
            examMetrics.setResponse_generator_id(responseGenerator.getResponseGeneratorId());
        }
        examMetricsComputer.updateCompletionStatus(examMetrics, missionMetrics);
        examMetricsComputer.updateAllMetrics(examMetrics, missionMetrics, null, metricsInfo);

        if (outputProgress) {
            System.out.println("Complete");
        }

        SingleModelDataSet_Phase2 dataSet = new SingleModelDataSet_Phase2();
        dataSet.setExam_id(examId);
        dataSet.setSite_id(examMetrics.getSite_id());
        dataSet.setResponse_generator_id(examMetrics.getResponse_generator_id());
        dataSet.setTime_stamp(System.currentTimeMillis());
        dataSet.setExamMetrics(examMetrics);        
        return dataSet;
    }      

    public static void main(String[] args) {        
        String examId = "Final-Exam-1";
        //List<String> missionIds = new ArrayList<String>(Arrays.asList("Mission1"));
        //List<Integer> missionNumbers = new ArrayList<Integer>(Arrays.asList(1));
        List<String> missionIds = new ArrayList<String>(Arrays.asList("Mission1", 
                "Mission2", "Mission3", "Mission4", "Mission5"));
        List<Integer> missionNumbers = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5));
        if (args != null && args.length > 0) {
            examId = args[0];
        }
        boolean computeHumanDataSet = false;
        if (args != null && args.length > 1) {
            try {
                computeHumanDataSet = Boolean.parseBoolean(args[1]);
            } catch (Exception ex) {
            }
        }
        
        //Create or load the average human data set
        XMLCPADataPersister xml;
        AverageHumanDataSet_Phase2 humanDataSet = null;        
        if (computeHumanDataSet) {
            //Compute the average human data set
            humanDataSet = buildAndPersistAverageHumanDataSet(examId, missionIds,
                    missionNumbers);
        } else {
            try {
                //Load the average human data set
                xml = new XMLCPADataPersister(new File(assessmentFolder + examId + "/Assessment_Results").toURI().toURL());
                humanDataSet = xml.loadAverageHumanDataSet(examId);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }        
        
        //Compute and display a metric significance report for the average human data set
        ExamSignificanceReport significanceReport = 
                MetricSignificanceComputer.computeMetricSignificance(humanDataSet, 0.05);
        if(significanceReport != null) {
            //System.out.println(significanceReport.generateExamSignificanceReport(false));
        }

        //Create data set for HRL model
        //String modelName = "aggregatedResponse-MITRE-Run";
        String modelName = "aggregatedResponseTest";
        SingleModelDataSet_Phase2 hrlDataSet = buildAndPersistModelDataSet(examId, 
                missionIds, missionNumbers, "HRL", modelName, humanDataSet);        
        
        //Create data set for MITRE default A-B model
        SingleModelDataSet_Phase2 mitreDataSet = buildAndPersistSampleModelDataSet(
                examId, missionIds, missionNumbers,
                ModelFactory.createDefaultAbModel(0.5d, 0.5d, "MITRE", "A-B"), 
                humanDataSet);
        
        //Create data set for MITRE Bayesian model
        SingleModelDataSet_Phase2 bayesianDataSet = buildAndPersistSampleModelDataSet(
                examId, missionIds, missionNumbers,
                ModelFactory.createDefaultBayesianModel("MITRE", "Bayesian"), 
                humanDataSet);

        //Create data set for MITRE Random (Null) model
         SingleModelDataSet_Phase2 randomDataSet = buildAndPersistSampleModelDataSet(
                examId, missionIds, missionNumbers,
                ModelFactory.createDefaultRandomModel("MITRE", "Random"), 
                humanDataSet);
        
        //Generate results spreadsheet for the HRL model and MITRE model(s)       
        SpreadsheetCPADataPersister spreadsheetPersister = null;
        try {
            spreadsheetPersister = new SpreadsheetCPADataPersister(
                    new File(assessmentFolder + examId + "/Assessment_Results").toURI().toURL(), humanDataSet);
        } catch (MalformedURLException e) {           
            e.printStackTrace();
        }
        
         //Generate results spreadsheet for the HRL model
        if(spreadsheetPersister != null) {
           spreadsheetPersister.persistSingleModelDataSet(hrlDataSet);
        }
        
        //Generate results spreadsheet for MITRE default A-B model
        if(spreadsheetPersister != null) {
           spreadsheetPersister.persistSingleModelDataSet(mitreDataSet);
        }
        
         //Generate results spreadsheet for MITRE Bayesian mmodel
        if(spreadsheetPersister != null) {
           spreadsheetPersister.persistSingleModelDataSet(bayesianDataSet);
        }
        
        //Generate results spreadsheet for MITRE Random(null) model
        if(spreadsheetPersister != null) {
           spreadsheetPersister.persistSingleModelDataSet(randomDataSet);
        }
        
         //DEBUG CODE
//        outputModelResults(dataSet, humanDataSet);
        //END DEBUG CODE 
    }
    
    /**
     * Debug code to output model and human results that are displayed in a model spreadsheet.
     * 
     * @param dataSet
     * @param humanDataSet
     */
    public static void outputModelResults(SingleModelDataSet_Phase2 dataSet, 
            AverageHumanDataSet_Phase2 humanDataSet) {        
        //Display AA results
        System.out.println("AA:");
        for (int mission = 0; mission <= 1; mission++) {
            MissionMetrics missionMetrics = dataSet.getExamMetrics().getTasks().get(mission);
            MissionMetrics humanMetrics = humanDataSet != null ? humanDataSet.getTaskMetrics().get(mission) : null;            
            List<TrialData> trialData = missionMetrics.getTrials();            
            List<TrialData> humanTrialData = humanMetrics != null ? humanMetrics.getTrials() : null;
            System.out.println("Mission " + (mission+1) + " AA SMR Score: " + missionMetrics.getAA_smr_score());
            System.out.println("Pass/Fail:");            
            for (TrialData trial : trialData) {
                CFAMetric AA = trial.getMetrics().getAA_metrics();
                boolean assessed = AA.assessed;
                if (assessed) {
                    System.out.println(AA.pass ? "Pass" : "Fail");
                } else {
                    System.out.println("N/A");
                }
            }
            if (humanTrialData != null) {
                System.out.println("Bias Observed for Humans:");
                for (TrialData trial : humanTrialData) {
                    CFAMetric AA = trial.getMetrics().getAA_metrics();
                    System.out.println(AA.exhibited ? "Yes" : "No");
                }
            }
            System.out.println("Bias Observed for Model:");
            for (TrialData trial : trialData) {
                CFAMetric AA = trial.getMetrics().getAA_metrics();
                System.out.println(AA.exhibited ? "Yes" : "No");
            }
            if (humanTrialData != null) {
                System.out.println("Bias Magnitude for Humans:");
                for (TrialData trial : humanTrialData) {
                    CFAMetric AA = trial.getMetrics().getAA_metrics();
                    System.out.println(AA.magnitude);
                }
            }
            System.out.println("Bias Magnitude for Model:");
            for (TrialData trial : trialData) {
                CFAMetric AA = trial.getMetrics().getAA_metrics();
                System.out.println(AA.magnitude);
            }            
        }
        System.out.println();
        
        //Display PDE results
        System.out.println("PDE:");
        for (int mission = 3; mission <= 4; mission++) {
            MissionMetrics missionMetrics = dataSet.getExamMetrics().getTasks().get(mission);
            MissionMetrics humanMetrics = humanDataSet != null ? humanDataSet.getTaskMetrics().get(mission) : null;            
            List<TrialData> trialData = missionMetrics.getTrials();            
            List<TrialData> humanTrialData = humanMetrics != null ? humanMetrics.getTrials() : null;
            System.out.println("Mission " + (mission+1) + " PDE SMR Score: " + missionMetrics.getPDE_smr_score());            
            System.out.println("Pass/Fail:");            
            for (TrialData trial : trialData) {
                CFAMetric PDE = trial.getMetrics().getPDE_metrics();
                if (PDE != null && PDE.assessed != null) {
                    boolean assessed = PDE.assessed;
                    if (assessed) {
                        System.out.println(PDE.pass ? "Pass" : "Fail");
                    } else {
                        System.out.println("N/A");
                    }
                }
            }
            if (humanTrialData != null) {
                System.out.println("Bias Observed for Humans:");
                for (TrialData trial : humanTrialData) {
                    CFAMetric PDE = trial.getMetrics().getPDE_metrics();
                    if (PDE != null && PDE.assessed != null) {
                        System.out.println(PDE.exhibited ? "Yes" : "No");
                    }
                }
            }
            System.out.println("Bias Observed for Model:");
            for (TrialData trial : trialData) {
                CFAMetric PDE = trial.getMetrics().getPDE_metrics();
                if (PDE != null && PDE.assessed != null) {
                    System.out.println(PDE.exhibited ? "Yes" : "No");
                }
            }
            if (humanTrialData != null) {
                System.out.println("Bias Magnitude for Humans:");
                for (TrialData trial : humanTrialData) {
                    CFAMetric PDE = trial.getMetrics().getPDE_metrics();
                    if (PDE != null && PDE.assessed != null) {
                        System.out.println(PDE.magnitude);
                    }
                }
            }
            System.out.println("Bias Magnitude for Model:");
            for (TrialData trial : trialData) {
                CFAMetric PDE = trial.getMetrics().getPDE_metrics();
                if (PDE != null && PDE.assessed != null) {
                    System.out.println(PDE.magnitude);
                }
            }            
        }
        System.out.println();
        
        //Display RR results
        System.out.println("RR:");
        for (int mission = 0; mission <= 2; mission++) {
            MissionMetrics missionMetrics = dataSet.getExamMetrics().getTasks().get(mission);
            MissionMetrics humanMetrics = humanDataSet != null ? humanDataSet.getTaskMetrics().get(mission) : null;            
            List<TrialData> trialData = missionMetrics.getTrials();            
            List<TrialData> humanTrialData = humanMetrics != null ? humanMetrics.getTrials() : null;
            System.out.println("Mission " + (mission + 1) + " RR SMR Score: " + missionMetrics.getRR_smr_score());            
            System.out.println("Pass/Fail:");
            for (TrialData trial : trialData) {
                List<CFAMetric> RR = trial.getMetrics().getRR_metrics();
                for (int location = 0; location < RR.size(); location++) {
                    boolean assessed = RR.get(location).assessed;
                    if (assessed) {
                        System.out.println(RR.get(location).pass ? "Pass" : "Fail");
                    } else {
                        System.out.println("N/A");
                    }
                }
            }
            if (humanTrialData != null) {
                System.out.println("Bias Observed for Humans:");
                for (TrialData trial : humanTrialData) {
                    List<CFAMetric> RR = trial.getMetrics().getRR_metrics();
                    for (int location = 0; location < RR.size(); location++) {
                        System.out.println(RR.get(location).exhibited ? "Yes" : "No");
                    }
                }
            }
            System.out.println("Bias Observed for Model:");
            for (TrialData trial : trialData) {
                List<CFAMetric> RR = trial.getMetrics().getRR_metrics();
                for (int location = 0; location < RR.size(); location++) {
                    System.out.println(RR.get(location).exhibited ? "Yes" : "No");
                }
            }
            if (humanTrialData != null) {
                System.out.println("Bias Magnitude for Humans:");
                for (TrialData trial : humanTrialData) {
                    List<CFAMetric> RR = trial.getMetrics().getRR_metrics();
                    for (int location = 0; location < RR.size(); location++) {
                        System.out.println(RR.get(location).magnitude);
                    }
                }
            }
            System.out.println("Bias Magnitude for Model:");
            for (TrialData trial : trialData) {
                List<CFAMetric> RR = trial.getMetrics().getRR_metrics();
                for (int location = 0; location < RR.size(); location++) {
                    System.out.println(RR.get(location).magnitude);
                }
            }
        }
        System.out.println();        
        
        //Display AV results
        System.out.println("AV:");
        for (int mission = 0; mission <= 0; mission++) {
            MissionMetrics missionMetrics = dataSet.getExamMetrics().getTasks().get(mission);
            MissionMetrics humanMetrics = humanDataSet != null ? humanDataSet.getTaskMetrics().get(mission) : null;            
            List<TrialData> trialData = missionMetrics.getTrials();            
            List<TrialData> humanTrialData = humanMetrics != null ? humanMetrics.getTrials() : null;
            System.out.println("Mission " + (mission+1) + " AV SMR Score: " + missionMetrics.getAV_smr_score());            
            System.out.println("Pass/Fail:");            
            for (TrialData trial : trialData) {
                CFAMetric AV = trial.getMetrics().getAV_metrics();
                boolean assessed = AV.assessed;
                if (assessed) {
                    System.out.println(AV.pass ? "Pass" : "Fail");
                } else {
                    System.out.println("N/A");
                }
            }
            if (humanTrialData != null) {
                System.out.println("Bias Observed for Humans:");
                for (TrialData trial : humanTrialData) {
                    CFAMetric AV = trial.getMetrics().getAV_metrics();
                    System.out.println(AV.exhibited ? "Yes" : "No");
                }
            }
            System.out.println("Bias Observed for Model:");
            for (TrialData trial : trialData) {
                CFAMetric AV = trial.getMetrics().getAV_metrics();
                System.out.println(AV.exhibited ? "Yes" : "No");
            }
            if (humanTrialData != null) {
                System.out.println("Bias Magnitude for Humans:");
                for (TrialData trial : humanTrialData) {
                    CFAMetric AV = trial.getMetrics().getAV_metrics();
                    System.out.println(AV.magnitude);
                }
            }
            System.out.println("Bias Magnitude for Model:");
            for (TrialData trial : trialData) {
                CFAMetric AV = trial.getMetrics().getAV_metrics();
                System.out.println(AV.magnitude);
            }            
        }
        System.out.println();

        //Display PM results
        System.out.println("PM:");
        if (humanDataSet != null) {
            System.out.println("Bias Observed for Humans:");
            for (int mission = 1; mission <= 4; mission++) {
                MissionMetrics missionMetrics = humanDataSet.getTaskMetrics().get(mission);
                CFAMetric PM = missionMetrics.getPM_metrics();
                System.out.println(PM.exhibited ? "Yes" : "No");
            }
        }
        System.out.println("Bias Observed for Model:");
        for (int mission = 1; mission <= 4; mission++) {
            MissionMetrics missionMetrics = dataSet.getExamMetrics().getTasks().get(mission);
            CFAMetric PM = missionMetrics.getPM_metrics();
            System.out.println(PM.exhibited ? "Yes" : "No");
        }
        if (humanDataSet != null) {
            System.out.println("n for Humans (nH):");
            for (int mission = 1; mission <= 4; mission++) {
                MissionMetrics missionMetrics = humanDataSet.getTaskMetrics().get(mission);
                System.out.println(missionMetrics.getPM_normativeBlueOptionSelectionFrequency());
            }
        }
        System.out.println("n for Model (nM):");
        for (int mission = 1; mission <= 4; mission++) {
            MissionMetrics missionMetrics = dataSet.getExamMetrics().getTasks().get(mission);
            System.out.println(missionMetrics.getPM_normativeBlueOptionSelectionFrequency());
        }
        System.out.println("PM MSR Score:");
        for (int mission = 1; mission <= 4; mission++) {
            MissionMetrics missionMetrics = dataSet.getExamMetrics().getTasks().get(mission);
            CFAMetric PM = missionMetrics.getPM_metrics();
            System.out.println(PM.score);            
        }
        System.out.println(dataSet.getExamMetrics().getPM().score);
        for (int mission = 1; mission <= 4; mission++) {
            MissionMetrics missionMetrics = dataSet.getExamMetrics().getTasks().get(mission);
            MissionMetrics humanMetrics = humanDataSet != null ? humanDataSet.getTaskMetrics().get(mission) : null;            
            List<TrialData> trialData = missionMetrics.getTrials();            
            List<TrialData> humanTrialData = humanMetrics != null ? humanMetrics.getTrials() : null;
            System.out.print("Mission " + (mission+1) + ", n for Model (nm): " + 
                    missionMetrics.getPM_normativeBlueOptionSelectionFrequency());
            if(humanMetrics != null) {
                System.out.print(", n for Humans (nH): " + 
                        humanMetrics.getPM_normativeBlueOptionSelectionFrequency());
            }
            System.out.println();
            if (humanTrialData != null) {
                System.out.println("n for Humans (nH):");
                for (TrialData trial : humanTrialData) {
                    System.out.println(trial.getMetrics().getPM_normativeBlueOptionSelections());
                }
            }
            System.out.println("n for Model (nM):");
            for (TrialData trial : trialData) {
                System.out.println(trial.getMetrics().getPM_normativeBlueOptionSelections());
            }
        }
        System.out.println();
        
        //Display CS results
        System.out.println("CS:");
        if (humanDataSet != null) {
            System.out.println("Bias Observed for Humans:");
            for (int mission = 2; mission <= 2; mission++) {
                MissionMetrics missionMetrics = humanDataSet.getTaskMetrics().get(mission);
                CFAMetric CS = missionMetrics.getCS_metrics();
                System.out.println(CS.exhibited ? "Yes" : "No");
            }
        }
        System.out.println("Bias Observed for Model:");
        for (int mission = 2; mission <= 2; mission++) {
            MissionMetrics missionMetrics = dataSet.getExamMetrics().getTasks().get(mission);
            CFAMetric CS = missionMetrics.getCS_metrics();
            System.out.println(CS.exhibited ? "Yes" : "No");
        }
        if (humanDataSet != null) {
            System.out.println("f for Humans (fH):");
            for (int mission = 2; mission <= 2; mission++) {
                MissionMetrics missionMetrics = humanDataSet.getTaskMetrics().get(mission);
                System.out.println(missionMetrics.getCS_sigintHighestPaSelectionFrequency());
            }
        }
        System.out.println("f for Model (fM):");
        for (int mission = 2; mission <= 2; mission++) {
            MissionMetrics missionMetrics = dataSet.getExamMetrics().getTasks().get(mission);
            System.out.println(missionMetrics.getCS_sigintHighestPaSelectionFrequency());
        }
        System.out.println("CS MSR Score:");
        for (int mission = 2; mission <= 2; mission++) {
            MissionMetrics missionMetrics = dataSet.getExamMetrics().getTasks().get(mission);
            CFAMetric CS = missionMetrics.getCS_metrics();
            System.out.println(CS.score);            
        }
        System.out.println(dataSet.getExamMetrics().getCS().score);
        for (int mission = 2; mission <= 2; mission++) {
            MissionMetrics missionMetrics = dataSet.getExamMetrics().getTasks().get(mission);
            MissionMetrics humanMetrics = humanDataSet != null ? humanDataSet.getTaskMetrics().get(mission) : null;            
            List<TrialData> trialData = missionMetrics.getTrials();            
            List<TrialData> humanTrialData = humanMetrics != null ? humanMetrics.getTrials() : null;
            System.out.print("Mission " + (mission+1) + ", f for Model (fM): " + 
                    missionMetrics.getCS_sigintHighestPaSelectionFrequency());            
            if(humanMetrics != null) {
                System.out.print(", f for Humans (fH): " + 
                        humanMetrics.getCS_sigintHighestPaSelectionFrequency());
            }
            System.out.println();
            if (humanTrialData != null) {
                System.out.println("f for Humans (fH):");
                for (TrialData trial : humanTrialData) {
                    System.out.println(trial.getMetrics().getCS_sigintAtHighestPaLocationSelections());
                }
            }
            System.out.println("f for Model (fM):");
            for (TrialData trial : trialData) {
                System.out.println(trial.getMetrics().getCS_sigintAtHighestPaLocationSelections());
            }
        }
        System.out.println();
        
        //Display SS results
        System.out.println("SS:");
        if (humanDataSet != null) {
            System.out.println("Bias Observed for Humans:");
            for (int mission = 3; mission <= 4; mission++) {
                MissionMetrics missionMetrics = humanDataSet.getTaskMetrics().get(mission);
                CFAMetric SS = missionMetrics.getSS_metrics();
                System.out.println(SS.exhibited ? "Yes" : "No");
            }
        }
        System.out.println("Bias Observed for Model:");
        for (int mission = 3; mission <= 4; mission++) {
            MissionMetrics missionMetrics = dataSet.getExamMetrics().getTasks().get(mission);
            CFAMetric SS = missionMetrics.getSS_metrics();
            System.out.println(SS.exhibited ? "Yes" : "No");
        }
        if (humanDataSet != null) {
            System.out.println("s for Humans (sH):");
            for (int mission = 3; mission <= 4; mission++) {
                MissionMetrics missionMetrics = humanDataSet.getTaskMetrics().get(mission);
                System.out.println(missionMetrics.getSS_percentTrialsReviewedInBatchPlot_avg());
            }
        }
        System.out.println("s for Model (sM):");
        for (int mission = 3; mission <= 4; mission++) {
            MissionMetrics missionMetrics = dataSet.getExamMetrics().getTasks().get(mission);
            System.out.println(missionMetrics.getSS_percentTrialsReviewedInBatchPlot_avg());
        }
        System.out.println("SS MSR Score:");
        for (int mission = 3; mission <= 4; mission++) {
            MissionMetrics missionMetrics = dataSet.getExamMetrics().getTasks().get(mission);
            CFAMetric SS = missionMetrics.getSS_metrics();
            System.out.println(SS.score);            
        }
        System.out.println(dataSet.getExamMetrics().getSS().score);
        for (int mission = 3; mission <= 4; mission++) {
            MissionMetrics missionMetrics = dataSet.getExamMetrics().getTasks().get(mission);
            MissionMetrics humanMetrics = humanDataSet != null ? humanDataSet.getTaskMetrics().get(mission) : null;            
            List<TrialData> trialData = missionMetrics.getTrials();            
            List<TrialData> humanTrialData = humanMetrics != null ? humanMetrics.getTrials() : null;
            System.out.print("Mission " + (mission+1) + ", s for Model (sM): " + 
                    missionMetrics.getSS_percentTrialsReviewedInBatchPlot_avg());
            if(humanMetrics != null) {
                System.out.print(", s for Humans (sH): " + 
                        humanMetrics.getSS_percentTrialsReviewedInBatchPlot_avg());
            }
            System.out.println();
            if (humanTrialData != null) {
                System.out.println("s for Humans (sH):");
                for (TrialData trial : humanTrialData) {
                    if (trial.getMetrics().getSS_percentTrialsReviewedInBatchPlot() != null) {
                        System.out.println(trial.getMetrics().getSS_percentTrialsReviewedInBatchPlot());
                    }
                }
            }
            System.out.println("s for Model (sM):");
            for (TrialData trial : trialData) {
                if (trial.getMetrics().getSS_percentTrialsReviewedInBatchPlot() != null) {
                    System.out.println(trial.getMetrics().getSS_percentTrialsReviewedInBatchPlot());
                }
            }
        }
        System.out.println();
        
        //Display CB results
        System.out.println("CB:");        
        if (humanDataSet != null) {
            System.out.println("Bias Observed for Humans:");
            for (int mission = 3; mission <= 3; mission++) {
                MissionMetrics missionMetrics = humanDataSet.getTaskMetrics().get(mission);
                CFAMetric CB = missionMetrics.getCB_metrics();
                System.out.println(CB.exhibited ? "Yes" : "No");
            }
        }
        System.out.println("Bias Observed for Model:");
        for (int mission = 3; mission <= 3; mission++) {
            MissionMetrics missionMetrics = dataSet.getExamMetrics().getTasks().get(mission);
            CFAMetric CB = missionMetrics.getCB_metrics();
            System.out.println(CB.exhibited ? "Yes" : "No");
        }
        System.out.println("Change Detected by Model");
        for (int mission = 3; mission <= 3; mission++) {
            MissionMetrics missionMetrics = dataSet.getExamMetrics().getTasks().get(mission);
            System.out.println(missionMetrics.getCB_redTacticsChangedDetected().get(0) ? "Yes" : "No");
        }
        if (humanDataSet != null) {
            System.out.println("b for Humans (bH):");
            for (int mission = 3; mission <= 3; mission++) {
                MissionMetrics missionMetrics = humanDataSet.getTaskMetrics().get(mission);
                System.out.println(missionMetrics.getCB_trialsNeededToDetectRedTacticChanges_avg());
            }
        }
        System.out.println("b for Model (bM):");
        for (int mission = 3; mission <= 3; mission++) {
            MissionMetrics missionMetrics = dataSet.getExamMetrics().getTasks().get(mission);
            System.out.println(missionMetrics.getCB_trialsNeededToDetectRedTacticChanges_avg());
        }
        System.out.println("CB MSR Score:");
        for (int mission = 3; mission <= 3; mission++) {
            MissionMetrics missionMetrics = dataSet.getExamMetrics().getTasks().get(mission);
            CFAMetric CB = missionMetrics.getCB_metrics();
            System.out.println(CB.score);            
        }        
        System.out.println();
        
        //Display ASR results
        System.out.println("ASR:");
        for (int mission = 0; mission <= 4; mission++) {
            MissionMetrics missionMetrics = dataSet.getExamMetrics().getTasks().get(mission);
            if (missionMetrics.getRSR_ASR() != null) {
                RSRAndASRMissionMetrics rsrAsrMetrics = missionMetrics.getRSR_ASR();
                System.out.println(rsrAsrMetrics.getASR_avg());
                if (rsrAsrMetrics.getASR_stage_avg() != null && rsrAsrMetrics.getASR_stage_avg().size() > 1) {
                    for (Double asr : rsrAsrMetrics.getASR_stage_avg()) {
                        if (asr != null) {
                            System.out.println(asr);
                        }
                    }
                }
            }
        }
        System.out.println(dataSet.getExamMetrics().getASR().score);
        System.out.println();

        //Display RSR results
        System.out.println("RSR:");
        for (int mission = 0; mission <= 4; mission++) {
            MissionMetrics missionMetrics = dataSet.getExamMetrics().getTasks().get(mission);
            if (missionMetrics.getRSR_ASR() != null) {
                RSRAndASRMissionMetrics rsrAsrMetrics = missionMetrics.getRSR_ASR();
                System.out.println(rsrAsrMetrics.getRSR_avg());
                if (rsrAsrMetrics.getRSR_stage_avg() != null && rsrAsrMetrics.getRSR_stage_avg().size() > 1) {
                    for (Double rsr : rsrAsrMetrics.getRSR_stage_avg()) {
                        if (rsr != null) {
                            System.out.println(rsr);
                        }
                    }
                }
            }
        }
        System.out.println(dataSet.getExamMetrics().getRSR().score);
        System.out.println();        

        //Display RMR results
        System.out.println("RMR:");
        for (int mission = 1; mission <= 4; mission++) {
            MissionMetrics missionMetrics = dataSet.getExamMetrics().getTasks().get(mission);
            if (missionMetrics.getRMR_avg() != null) {
                if (missionMetrics.getRMR_sigint_avg() != null) {
                    System.out.println(missionMetrics.getRMR_sigint_avg());
                    System.out.println(missionMetrics.getRMR_blueAction_avg());
                    System.out.println(missionMetrics.getRMR_avg());
                } else {
                    System.out.println(missionMetrics.getRMR_avg());
                }
            }
        }
        System.out.println(dataSet.getExamMetrics().getRMR().score);
        System.out.println();
    }
}