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
package org.mitre.icarus.cps.assessment.data_aggregator.phase_2;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.xml.bind.JAXBException;

import org.mitre.icarus.cps.app.experiment.IcarusSubjectData;
import org.mitre.icarus.cps.app.experiment.data_recorder.SubjectDataRecorderUtils;
import org.mitre.icarus.cps.assessment.assessment_metrics_computer.phase_2.MissionMetricsComputer;
import org.mitre.icarus.cps.assessment.assessment_metrics_computer.phase_2.TrialMetricsComputer;
import org.mitre.icarus.cps.assessment.assessment_processor.phase_2.BatchFileProcessor;
import org.mitre.icarus.cps.assessment.assessment_processor.phase_2.TrialResponseProcessor;
import org.mitre.icarus.cps.assessment.data_aggregator.DataAggregatorUtils;
import org.mitre.icarus.cps.assessment.data_model.base.DataType;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.CFAMetric;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.MetricsInfo;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.MissionMetrics;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.NameValuePair;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.SubjectSigintProbabilities;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.TrialData;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.TrialMetrics;
import org.mitre.icarus.cps.assessment.score_computer.phase_2.ScoreComputer_Phase2;
import org.mitre.icarus.cps.assessment.survey.ISurveyFileNameFormatter;
import org.mitre.icarus.cps.assessment.survey.SurveyFileNameFormatterFormat1;
import org.mitre.icarus.cps.assessment.survey.SurveyFileNameFormatterFormat2;
import org.mitre.icarus.cps.assessment.utils.phase_2.AssessmentUtils_Phase2;
import org.mitre.icarus.cps.exam.base.response.ResponseGeneratorData;
import org.mitre.icarus.cps.exam.phase_2.IcarusExam_Phase2;
import org.mitre.icarus.cps.exam.phase_2.IcarusExam_Phase2.MissionComparisonReport;
import org.mitre.icarus.cps.exam.phase_2.loader.IcarusExamLoader_Phase2;
import org.mitre.icarus.cps.exam.phase_2.testing.IcarusTestTrial_Phase2;
import org.mitre.icarus.cps.exam.phase_2.testing.Mission;import org.mitre.icarus.cps.exam.phase_2.testing.MissionType;
;

/**
 * Aggregates XML data files from subject responses into CSV data files for the
 * Phase 2 Challenge Problem.
 *
 * @author CBONACETO
 *
 */
@SuppressWarnings("unused")
public class CsvAggregator {

    /**
     * Extracts trial response data from trials and computes trial metrics
     */
    protected TrialResponseProcessor trialResponseProcessor;

    /**
     * Computes mission metrics
     */
    protected MissionMetricsComputer missionMetricsComputer;

    /**
     * Computes normative solutions
     */
    protected ScoreComputer_Phase2 scoreComputer;

    protected static final IcarusExamLoader_Phase2 examLoader = IcarusExamLoader_Phase2.getInstance();

    /**
     * Whether to output the time taken on each part of a trial.
     */
    protected boolean includeTrialPartName;

     /**
     * Whether to output the time taken on each trial.
     */
    protected boolean includeTrialPartTime;

     /**
     * Whether to output a Boolean flag indicating that the participant generated
     * the response for a part of a trial.
     */
    protected boolean includeParticipantGeneratedResponeFlag;

     /**
     * Whether to output the metrics for a trial.
     */
    protected boolean includeTrialMetrics;

    protected String separatorChar = ",";

    protected String nullValueString = "NaN";

    public CsvAggregator() {
        scoreComputer = new ScoreComputer_Phase2();
        trialResponseProcessor = new TrialResponseProcessor(
                new TrialMetricsComputer(), scoreComputer);
        missionMetricsComputer = new MissionMetricsComputer();
        includeTrialPartName = false;
        includeTrialPartTime = true;
        includeParticipantGeneratedResponeFlag = false;
        includeTrialMetrics = false;
    }

    public boolean isIncludeTrialPartName() {
        return includeTrialPartName;
    }

    public void setIncludeTrialPartName(boolean includeTrialPartName) {
        this.includeTrialPartName = includeTrialPartName;
    }

    public boolean isIncludeTrialPartTime() {
        return includeTrialPartTime;
    }

    public void setIncludeTrialPartTime(boolean includeTrialPartTime) {
        this.includeTrialPartTime = includeTrialPartTime;
    }

    public boolean isIncludeParticipantGeneratedResponeFlag() {
        return includeParticipantGeneratedResponeFlag;
    }

    public void setIncludeParticipantGeneratedResponeFlag(
            boolean includeParticipantGeneratedResponeFlag) {
        this.includeParticipantGeneratedResponeFlag = includeParticipantGeneratedResponeFlag;
    }

    public boolean isIncludeTrialMetrics() {
        return includeTrialMetrics;
    }

    public void setIncludeTrialMetrics(boolean includeTrialMetrics) {
        this.includeTrialMetrics = includeTrialMetrics;
    }

    public String getSeparatorChar() {
        return separatorChar;
    }

    public void setSeparatorChar(String separatorChar) {
        this.separatorChar = separatorChar;
    }

    public String getNullValueString() {
        return nullValueString;
    }

    public void setNullValueString(String nullValueString) {
        this.nullValueString = nullValueString;
    }

    /**
     * Aggregates subject data from each mission into a CSV file. Also aggregates 
     * data on the frequency with which each bias was exhibited into a CSV file.
     * 
     * @param exam
     * @param missionIds
     * @param missionNumbers
     * @param metricsInfo
     * @param subjects
     * @param dataDir
     * @param appending
     * @param outputDir
     * @param outputProgress
     * @throws IOException
     */
    public void aggregateSubjectData(IcarusExam_Phase2 exam,
            List<String> missionIds, List<Integer> missionNumbers,
            MetricsInfo metricsInfo, List<Set<IcarusSubjectData>> subjects,
            File dataDir, File outputDir, boolean appending, boolean outputProgress) throws IOException {
        aggregateSubjectData(exam, missionIds, missionNumbers, metricsInfo, subjects,
                dataDir, outputDir, appending, outputProgress, false, null, true);
    }

    /**
     * Aggregates subject data from each mission into a CSV file. Also aggregates 
     * data on the frequency with which each bias was exhibited into a CSV file
     * if aggregateBiasesExhibitedData is true.
     * 
     * @param exam
     * @param missionIds
     * @param missionNumbers
     * @param metricsInfo
     * @param subjects
     * @param dataDir
     * @param outputDir
     * @param appending
     * @param outputProgress
     * @param validateStimuliMatches
     * @param subjectExam
     * @param aggregateBiasesExhibitedData
     * @throws IOException
     */
    public void aggregateSubjectData(IcarusExam_Phase2 exam,
            List<String> missionIds, List<Integer> missionNumbers,
            MetricsInfo metricsInfo, List<Set<IcarusSubjectData>> subjects,
            File dataDir, File outputDir, boolean appending, boolean outputProgress,
            boolean validateStimuliMatches, IcarusExam_Phase2 subjectExam,
            boolean aggregateBiasesExhibitedData) throws IOException {
        if (exam == null || exam.getId() == null || exam.getMissions() == null
                || exam.getMissions().isEmpty()) {
            throw new IllegalArgumentException("The exam cananot be null and must contain an ID and 1 or more missions.");
        }
        if (missionIds == null || missionIds.isEmpty() || missionNumbers == null
                || missionNumbers.size() != missionIds.size()) {
            throw new IllegalArgumentException("At least one mission ID must be specified");
        }
        if (!dataDir.isDirectory() || !dataDir.exists()) {
            throw new IllegalArgumentException(dataDir.getPath() + " is not a directory or cannot be found.");
        }
        if (!outputDir.isDirectory() || !outputDir.exists()) {
            throw new IllegalArgumentException(outputDir.getPath() + " is not a directory or cannot be found.");
        }
        String examId = exam.getId();
        int numMissions = missionIds.size();
        metricsInfo = metricsInfo == null ? MetricsInfo.createDefaultMetricsInfo() : metricsInfo;

        //Average time on mission for each mission
        List<Double> timeOnMission = new ArrayList<Double>(numMissions);

        //Subject SIGINT probabilities for each subject
        HashMap<String, SubjectSigintProbabilities> sigintProbabilities
                = new HashMap<String, SubjectSigintProbabilities>();

        //Process subject data for each mission and write results to CSV files (one per mission containing all subject data)
        //TODO: Also output time on mission and time on exam for each subject
        int missionIndex = 0;
        for (String missionId : missionIds) {
            int missionNum = missionNumbers.get(missionIndex);
            double missionTime = 0d;
            int numTimesOnMission = 0;
            //Find the mission in the exam
            Mission<?> mission = null;
            Iterator<Mission<?>> missionIter = exam.getMissions().iterator();
            while (missionIter.hasNext() && mission == null) {
                Mission<?> m = missionIter.next();
                if (missionId.equals(m.getId())) {
                    mission = m;
                }
            }
            //Find the mission in the subject exam (used to validate that the stimuli matches)
            Mission<?> subjectMission = null;
            if (validateStimuliMatches && subjectExam != null) {
                Iterator<Mission<?>> subjectMissionIter = subjectExam.getMissions().iterator();
                while (subjectMissionIter.hasNext() && subjectMission == null) {
                    Mission<?> m = subjectMissionIter.next();
                    if (missionId.equals(m.getId())) {
                        subjectMission = m;
                    }
                }
            }
            if (mission != null) {
                if (outputProgress) {
                    System.out.println("Aggregating data for " + missionId);
                }
                FileWriter fw = null;
                FileWriter fwBe = null;
                try {
                    //Create the CSV file for the mission
                    fw = new FileWriter(new File(outputDir,
                            "allresponses_mission_" + missionNum + ".csv"), appending);
                    if (aggregateBiasesExhibitedData) {
                        //Create the biases exhibited CSV file for the mission
                        fwBe = new FileWriter(new File(outputDir,
                                "biases_exhibited_mission_" + missionNum + ".csv"), appending);
                    }
                    boolean firstSubjectTrial = true;
                    for (IcarusSubjectData subject : subjects.get(missionIndex)) {
                        trialResponseProcessor.setSubjectSigintProbs(sigintProbabilities.get(subject.getSubjectId()));
                        if (trialResponseProcessor.getSubjectSigintProbs() != null) {
                            System.out.println("Using Subject SIGINT Probs: PtChatter: "
                                    + trialResponseProcessor.getSubjectSigintProbs().getPtChatter() + ", PtSilent: "
                                    + trialResponseProcessor.getSubjectSigintProbs().getPtSilent());
                        }
                        //Load the task data for the current subject on the current mission
                        File file = new File(dataDir, "/" + SubjectDataRecorderUtils.formatSubjectFolderName(subject) + "/"
                                + SubjectDataRecorderUtils.formatDataFileName(subject, examId, missionId));
                        Mission<?> missionResponse = null;
                        try {
                            missionResponse = examLoader.unmarshalExamPhase(file.toURI().toURL(), false);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        //Process the mission data for the current subject and write the data to the CSV file
                        if (missionResponse != null && missionResponse.getTestTrials() != null
                                && !missionResponse.getTestTrials().isEmpty()) {
                            //Populate the Blue locations for each trial in the mission
                            Iterator<? extends IcarusTestTrial_Phase2> missionTrialIter = subjectMission != null
                                    ? subjectMission.getTestTrials().iterator() : mission.getTestTrials().iterator();
                            Iterator<? extends IcarusTestTrial_Phase2> missionResponseTrialIter = missionResponse.getTestTrials().iterator();
                            while (missionTrialIter.hasNext() && missionResponseTrialIter.hasNext()) {
                                IcarusTestTrial_Phase2 missionTrial = missionTrialIter.next();
                                IcarusTestTrial_Phase2 missionResponseTrial = missionResponseTrialIter.next();
                                missionResponseTrial.setBlueLocations(missionTrial.getBlueLocations());
                            }
                            //Validate that the stimuli in the subject response matches the stimuli in the offical exam
                            boolean dataValid = true;
                            if (subjectMission != null) {
                                MissionComparisonReport comparisonReport
                                        = IcarusExam_Phase2.compareMissions(mission, missionResponse);
                                dataValid = comparisonReport.missionsEqual;
                                if (outputProgress && !dataValid) {
                                    System.err.println("Warning, stimuli for Subject: " + subject.getSubjectId() + ", Mission: "
                                            + missionId + " did not match the exam stimuli and will not be used. Mismatched Trials: "
                                            + comparisonReport.mismatchedTrials + ", Contain Same Number of Trials: "
                                            + comparisonReport.missionsContainSameNumberOfTrials);
                                }
                            }
                            if (dataValid) {
                                //Update average time on mission						
                                Double time = (missionResponse.getStartTime() != null && missionResponse.getEndTime() != null) ? new Double(missionResponse.getEndTime().getTime() - missionResponse.getStartTime().getTime()) : null;
                                if (time != null) {
                                    missionTime += time;
                                    numTimesOnMission++;
                                }
                                //Extract the trial data and compute trial metrics for each trial
                                if (outputProgress) {
                                    System.out.println("Processing trial data for Subject: " + subject.getSubjectId() + ", Mission: " + missionId);
                                }
                                ResponseGeneratorData responseGenerator = missionResponse.getResponseGenerator();
                                List<TrialData> trials = new LinkedList<TrialData>();
                                for (IcarusTestTrial_Phase2 trialResponse : missionResponse.getTestTrials()) {
                                    if (trialResponse.isAttackHistoryTrial() == null
                                            || !trialResponse.isAttackHistoryTrial()) { //Ignore attack history trials in Mission 6										
                                        TrialData currTrialData = trialResponseProcessor.updateTrialData(
                                                exam, missionResponse, trialResponse, trialResponse.getResponseFeedBack(),
                                                false, null, examId, missionId, missionNum, responseGenerator);
                                        trials.add(currTrialData);
                                        //Compute the metrics
                                        if (includeTrialMetrics || aggregateBiasesExhibitedData) {
                                            trialResponseProcessor.updateTrialMetrics(currTrialData, metricsInfo, null);
                                        }
                                        currTrialData.setData_type(DataType.Human_Single);
                                        List<NameValuePair> trialDataValues = currTrialData.getDataAndMetricValuesAsString(
                                                mission.getMissionType(), includeTrialPartName, includeTrialPartTime,
                                                includeParticipantGeneratedResponeFlag, includeTrialMetrics);
                                        if (firstSubjectTrial && !appending) {
                                            //Write the CSV file header
                                            fw.append(createHeaderString(trialDataValues));
                                            fw.append("\n");
                                            //Write the biases exhibited CSV file header
                                            if (fwBe != null && aggregateBiasesExhibitedData) {
                                                fwBe.append(createBiasesExhibitedHeaderString(
                                                        currTrialData.getNumBlueLocations()));
                                                fwBe.append("\n");
                                            }
                                            firstSubjectTrial = false;
                                        }
                                        //Write the CSV file row
                                        fw.append(createDataRowString(trialDataValues));
                                        fw.append("\n");
                                        //Write the biases exhibited CSV file row
                                        if (fwBe != null && aggregateBiasesExhibitedData) {
                                            fwBe.append(createBiasesExhibitedDataRowString(currTrialData, metricsInfo));
                                            fwBe.append("\n");
                                        }
                                    }
                                }
                                //If Mission 1, compute the average SIGINT probabilities, which will be used to compute
                                //incremental Bayesian solutions in Missions 2 & 3                                
                                if (missionResponse.getMissionType() == MissionType.Mission_1) {
                                    MissionMetrics missionMetrics = missionMetricsComputer.updateTaskMetrics(null, time, trials,
                                            metricsInfo, null, missionNum);
                                    if (missionMetrics != null) {
                                        System.out.println("Computed Suject SIGINT Probs: PtChatter: "
                                                + missionMetrics.getSigintProbs_avg().getPtChatter() + ", PtSilent: "
                                                + missionMetrics.getSigintProbs_avg().getPtSilent());
                                        sigintProbabilities.put(subject.getSubjectId(), missionMetrics.getSigintProbs_avg());
                                    }
                                }
                            }
                        } else {
                            System.err.println("Warning, no trials found for file: " + file.getName());
                        }
                    }
                    if (numTimesOnMission > 0) {
                        timeOnMission.add(missionTime / numTimesOnMission);
                    } else {
                        timeOnMission.add(0d);
                    }
                } catch (IOException ex) {
                    throw ex;
                } finally {
                    if (fw != null) {
                        fw.flush();
                        fw.close();
                    }
                    if (fwBe != null) {
                        fwBe.flush();
                        fwBe.close();
                    }
                }
                if (outputProgress) {
                    System.out.println("Finished aggregating data for " + missionId);
                }
            }
            missionIndex++;
        }
    }

    /**
     * Creates the column headers for the aggregated subject data CSV file.
     * 
     * @param trialDataValues
     * @return
     */
    protected String createHeaderString(List<NameValuePair> trialDataValues) {
        StringBuilder sb = new StringBuilder();
        if (trialDataValues != null && !trialDataValues.isEmpty()) {
            int i = 0;
            for (NameValuePair trialDataValue : trialDataValues) {
                sb.append(trialDataValue.getName());
                if (i < trialDataValues.size() - 1) {
                    sb.append(separatorChar);
                }
                i++;
            }
        }
        return sb.toString();
    }

    /**
     * Creates a row for the aggregated subject data CSV file.
     * 
     * @param trialDataValues
     * @return
     */
    protected String createDataRowString(List<NameValuePair> trialDataValues) {
        StringBuilder sb = new StringBuilder();
        if (trialDataValues != null && !trialDataValues.isEmpty()) {
            int i = 0;
            for (NameValuePair trialDataValue : trialDataValues) {
                sb.append(trialDataValue.getValue() == null ? nullValueString : trialDataValue.getValue());
                if (i < trialDataValues.size() - 1) {
                    sb.append(separatorChar);
                }
                i++;
            }
        }
        return sb.toString();
    }

    /**
     * Creates the column headers for the CSV file containing the frequency with which 
     * each bias was exhibited.
     * 
     * @param numLocations
     * @return
     */
    @SuppressWarnings("StringConcatenationInsideStringBufferAppend")
    protected String createBiasesExhibitedHeaderString(int numLocations) {
        StringBuilder sb = new StringBuilder();
        sb.append("site_id");
        sb.append(separatorChar);
        sb.append("response_generator_id");
        sb.append(separatorChar);
        sb.append("data_type");
        sb.append(separatorChar);
        sb.append("exam_id");
        sb.append(separatorChar);
        sb.append("task_number");
        sb.append(separatorChar);
        sb.append("trial_number");
        sb.append(separatorChar);
        String[] biases = {"AA", "PDE", "RR", "AV"};
        int[] numBiasStages = {1, 1, numLocations, 1};
        int i = 0;
        for (String bias : biases) {
            for (int stage = 0; stage < numBiasStages[i]; stage++) {
                sb.append(bias);
                if (numBiasStages[i] > 1) {
                    sb.append("_" + Integer.toString(stage + 1));
                }
                sb.append("_measured");
                sb.append(separatorChar);
                sb.append(bias);
                if (numBiasStages[i] > 1) {
                    sb.append("_" + Integer.toString(stage + 1));
                }
                sb.append("_magnitude");
                sb.append(separatorChar);
                sb.append(bias);
                if (numBiasStages[i] > 1) {
                    sb.append("_" + Integer.toString(stage + 1));
                }
                sb.append("_exhibited");
                if (stage < numBiasStages[i] - 1 || i < biases.length - 1) {
                    sb.append(separatorChar);
                }
            }
            i++;
        }
        return sb.toString();
    }

    /**
     * Creates a row for the CSV file containing the frequency with which 
     * each bias was exhibited.
     * 
     * @param trialData
     * @param metricsInfo
     * @return
     */
    protected String createBiasesExhibitedDataRowString(TrialData trialData, MetricsInfo metricsInfo) {
        StringBuilder sb = new StringBuilder();
        int task = trialData.getTask_number();
        sb.append(trialData.getSite_id() == null ? nullValueString : trialData.getSite_id());
        sb.append(separatorChar);
        sb.append(trialData.getResponse_generator_id() == null ? nullValueString
                : trialData.getResponse_generator_id());
        sb.append(separatorChar);
        sb.append(trialData.getData_type() == null ? nullValueString
                : trialData.getData_type().toString());
        sb.append(separatorChar);
        sb.append(trialData.getExam_id() == null ? nullValueString : trialData.getExam_id());
        sb.append(separatorChar);
        sb.append(trialData.getTask_number() == null ? nullValueString : trialData.getTask_number());
        sb.append(separatorChar);
        sb.append(trialData.getTrial_number() == null ? nullValueString : trialData.getTrial_number());
        sb.append(separatorChar);
        TrialMetrics metrics = trialData.getMetrics();
        //System.out.println(metrics);
        ArrayList<List<CFAMetric>> biases = new ArrayList<List<CFAMetric>>();
        biases.add(Arrays.asList(metrics.getAA_metrics()));
        biases.add(Arrays.asList(metrics.getPDE_metrics()));
        int numLocations = trialData.getNumBlueLocations();
        List<CFAMetric> rrMetrics = new ArrayList<CFAMetric>();
        for (int i = 0; i < numLocations; i++) {
            if (metrics.getRR_metrics() != null && i < metrics.getRR_metrics().size()) {
                rrMetrics.add(metrics.getRR_metrics().get(i));
            } else {
                rrMetrics.add(null);
            }
        }
        biases.add(rrMetrics);
        biases.add(Arrays.asList(metrics.getAV_metrics()));
        boolean[] biasMeasured = {
            metricsInfo.getAA_info().isAssessedForTask(task),
            metricsInfo.getPDE_info().isAssessedForTask(task),
            metricsInfo.getRR_info().isAssessedForTask(task),
            metricsInfo.getAV_info().isAssessedForTask(task),};
        int i = 0;
        for (List<CFAMetric> biasStages : biases) {
            int stage = 0;
            for (CFAMetric bias : biasStages) {
                if (i == 1 && trialData.getTrial_number() == 1) {
                    //PDE is not measured on trial 1
                    sb.append(0);
                } else {
                    sb.append(biasMeasured[i] ? "1" : "0");
                }
                sb.append(separatorChar);
                if (bias == null || !biasMeasured[i]) {
                    sb.append(nullValueString);
                    sb.append(separatorChar);
                    sb.append(nullValueString);
                } else {
                    sb.append(bias.magnitude == null ? nullValueString : bias.magnitude.toString());
                    sb.append(separatorChar);
                    sb.append(bias.exhibited == null ? nullValueString : bias.exhibited ? "1" : "0");
                }
                if (stage < biasStages.size() - 1 || i < biases.size() - 1) {
                    sb.append(separatorChar);
                }
                stage++;
            }
            i++;
        }
        return sb.toString();
    }

    /**
     * Aggregates subject data from each mission into a CSV file. Also aggregates 
     * data on the frequency with which each bias was exhibited into a CSV file
     * if aggregateBiasesExhibitedData is true.
     * 
     * @param missionNumbers
     * @param surveyName
     * @param surveyFileNameFormatter
     * @param dataDir
     * @param outputDir
     * @param examFile
     * @param appending
     * @param validateStimuliMatches
     * @param aggregateMissionData
     * @param aggregateTimeData
     * @param aggregateBiasesExhibitedData
     */
    public static void aggregateSubjectData(List<Integer> missionNumbers,
            String surveyName, ISurveyFileNameFormatter surveyFileNameFormatter,
            File dataDir, File outputDir, File examFile, boolean appending,
            boolean validateStimuliMatches, boolean aggregateMissionData,
            boolean aggregateTimeData, boolean aggregateBiasesExhibitedData) {
        //Get all of the subjects
        try {
            Set<IcarusSubjectData> allSubjects = DataAggregatorUtils.getSubjectsInFolder(dataDir, true);
            List<Set<IcarusSubjectData>> subjectsByMission = new ArrayList<Set<IcarusSubjectData>>();
            for (int missionNum : missionNumbers) {
                subjectsByMission.add(allSubjects);
            }
            aggregateSubjectData(missionNumbers, allSubjects, subjectsByMission, surveyName,
                    surveyFileNameFormatter, dataDir, outputDir, examFile, appending,
                    validateStimuliMatches, aggregateMissionData, aggregateTimeData,
                    aggregateBiasesExhibitedData);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Aggregates subject data from each mission into a CSV file. Also aggregates 
     * data on the frequency with which each bias was exhibited into a CSV file
     * if aggregateBiasesExhibitedData is true.
     * 
     * @param missionNumbers
     * @param allSubjects
     * @param subjectsByMission
     * @param surveyName
     * @param surveyFileNameFormatter
     * @param dataDir
     * @param outputDir
     * @param examFile
     * @param appending
     * @param validateStimuliMatches
     * @param aggregateMissionData
     * @param aggregateTimeData
     * @param aggregateBiasesExhibitedData
     */
    public static void aggregateSubjectData(List<Integer> missionNumbers,
            Set<IcarusSubjectData> allSubjects,
            List<Set<IcarusSubjectData>> subjectsByMission,
            String surveyName, ISurveyFileNameFormatter surveyFileNameFormatter,
            File dataDir, File outputDir, File examFile, boolean appending,
            boolean validateStimuliMatches, boolean aggregateMissionData,
            boolean aggregateTimeData, boolean aggregateBiasesExhibitedData) {
        try {
            CsvAggregator aggregator = new CsvAggregator();

            //Load the exam
            IcarusExam_Phase2 exam = IcarusExamLoader_Phase2.unmarshalExam(
                    examFile.toURI().toURL());
            IcarusExamLoader_Phase2.initializeExamFeatureVectorData(exam, examFile.toURI().toURL(), null);
            List<String> missionIds = new ArrayList<String>(missionNumbers.size());
            for (Integer missionNumber : missionNumbers) {
                missionIds.add("Mission" + missionNumber.toString());
            }

            //Create the CSV files
            if (aggregateMissionData) {
                aggregator.aggregateSubjectData(exam, missionIds, missionNumbers, null,
                        subjectsByMission, dataDir, outputDir, appending, true,
                        validateStimuliMatches, exam, aggregateBiasesExhibitedData);
            }
            if (aggregateTimeData) {
                aggregator.aggregateTimeData(exam.getId(), missionIds, missionNumbers, surveyName, "pdf",
                        surveyFileNameFormatter, allSubjects, dataDir, outputDir,
                        appending, true);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Aggregates subject timing data into a CSV file.
     * 
     * @param examId
     * @param missionIds
     * @param missionNumbers
     * @param surveyName
     * @param surveyfileType
     * @param surveyFileNameFormatter
     * @param subjects
     * @param dataDir
     * @param outputDir
     * @param appending
     * @param outputProgress
     * @throws IOException
     */
    public void aggregateTimeData(String examId, List<String> missionIds, List<Integer> missionNumbers,
            String surveyName, String surveyfileType, ISurveyFileNameFormatter surveyFileNameFormatter,
            Collection<IcarusSubjectData> subjects, File dataDir, File outputDir,
            boolean appending, boolean outputProgress) throws IOException {
        //As a kludge, currently get the time stamp of the questionnaire and the time stamp of the last
        //mission completed to compute total time on exam. We also use the mission time stamps to get
        //the time spent on each mission, including reviewing training materials
        FileWriter fw = null;
        try {
            //Create the CSV file for the exam and mission times
            fw = new FileWriter(new File(outputDir,
                    "exam_times" + ".csv"), appending);
            fw.append("site_id" + separatorChar);
            fw.append("response_generator_id" + separatorChar);
            for (Integer missionNum : missionNumbers) {
                fw.append("mission_" + missionNum + "_training_time" + separatorChar);
                fw.append("mission_" + missionNum + "_time" + separatorChar);
            }
            fw.append("exam_training_time" + separatorChar);
            fw.append("exam_time\n");
            long maxTimeBetweenEvents = 3 * 60 * 60 * 1000;
            for (IcarusSubjectData subject : subjects) {
                long examTime = 0;
                long examTrainingTime = 0;

                //Get the time stamp for the survey
                Long surveyTimeStamp = 0L;
                String siteId = subject.getSite() != null ? subject.getSite().getTag() : null;
                String subjectId = subject.getSubjectId();
                if (outputProgress) {
                    System.out.println("Aggregating time data for subject " + subjectId);
                }
                fw.append(siteId);
                fw.append(separatorChar + subjectId);
                File file = new File(dataDir, "/" + SubjectDataRecorderUtils.formatSubjectFolderName(subject) + "/"
                        + surveyFileNameFormatter.formatSurveyFileName(surveyName, surveyfileType,
                                siteId, subjectId));
                if (file.exists()) {
                    surveyTimeStamp = file.lastModified();
                }

                //Get the time stamp for each mission
                boolean firstMission = true;
                Long lastTimeStamp = surveyTimeStamp;
                for (String missionId : missionIds) {
                    Long missionTimeStamp = 0L;
                    long timeOnMission = 0L;
                    file = new File(dataDir, "/" + SubjectDataRecorderUtils.formatSubjectFolderName(subject) + "/"
                            + SubjectDataRecorderUtils.formatDataFileName(subject, examId, missionId));
                    if (file.exists()) {
                        missionTimeStamp = file.lastModified();
                        Mission<?> missionResponse = null;
                        try {
                            missionResponse = examLoader.unmarshalExamPhase(file.toURI().toURL(), false);
                            timeOnMission = (missionResponse.getStartTime() != null && missionResponse.getEndTime() != null)
                                    ? new Long(missionResponse.getEndTime().getTime() - missionResponse.getStartTime().getTime()) : null;
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        long missionTotalTime = missionTimeStamp < lastTimeStamp ? timeOnMission : missionTimeStamp - lastTimeStamp;
                        if (missionTotalTime > maxTimeBetweenEvents) {
                            missionTotalTime = timeOnMission;
                        }
                        long missionTrainingTime = timeOnMission < missionTotalTime ? missionTotalTime - timeOnMission : 0;
                        if (firstMission) {
                            examTrainingTime = missionTrainingTime;
                        }
                        examTime += missionTotalTime;
                        fw.append(separatorChar + Long.toString(missionTrainingTime));
                        fw.append(separatorChar + Long.toString(timeOnMission));
                        lastTimeStamp = missionTimeStamp;
                        firstMission = false;
                    }
                }
                fw.append(separatorChar + Long.toString(examTrainingTime));
                fw.append(separatorChar + Long.toString(examTime) + "\n");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (fw != null) {
                    fw.flush();
                    fw.close();
                }
            } catch (Exception ex) {
            }
        }
    }

    /**
     * Aggregates model data into a CSV file.
     * 
     * @param examId
     * @param examFile
     * @param examResponseFile
     * @param metricsInfo
     * @param outputDir
     * @param outputProgress
     * @throws IOException
     * @throws JAXBException
     */
    public void aggregateModelData(String examId, File examFile, File examResponseFile,
            MetricsInfo metricsInfo, File outputDir, boolean outputProgress) throws IOException, JAXBException {
        //Load the exam        
        URL examFileUrl = examFile.toURI().toURL();
        IcarusExam_Phase2 exam = IcarusExamLoader_Phase2.unmarshalExam(examFileUrl, false);
        if (exam != null) {
            //Load feature vector data (used to compute normative solutions)
            exam.setId(examId);
            IcarusExamLoader_Phase2.initializeExamFeatureVectorData(exam, examFileUrl, null);

            //Load the model exam response file (contains responses to all missions)
            IcarusExam_Phase2 examResponse = IcarusExamLoader_Phase2.unmarshalExam(
                    examResponseFile.toURI().toURL(), false);
            if (examResponse == null || examResponse.getMissions() == null || examResponse.getMissions().isEmpty()) {
                System.err.println("Exam response not loaded or did not contain any missions");
            } else {
                aggregateModelData(exam, examResponse, metricsInfo, outputDir, outputProgress);
            }
        }
    }

    /**
     * Aggregates model data into a CSV file.
     *  
     * @param exam
     * @param examResponse
     * @param metricsInfo
     * @param outputDir
     * @param outputProgress
     * @throws IOException
     */
    public void aggregateModelData(IcarusExam_Phase2 exam, IcarusExam_Phase2 examResponse,
            MetricsInfo metricsInfo, File outputDir, boolean outputProgress) throws IOException {
        if (!outputDir.isDirectory() || !outputDir.exists()) {
            throw new IllegalArgumentException(outputDir.getPath() + " is not a directory or cannot be found.");
        }

        //Convert decimal probabilities to percent probabilities if necessary        
        boolean decimalProbs = AssessmentUtils_Phase2.convertDecimalProbsToPercentProbs(examResponse);
        if (outputProgress && decimalProbs) {
            System.out.println("Converted model probabilities from decimal to percent probabilities.");
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
        String examId = exam.getId();
        ResponseGeneratorData responseGenerator = examResponse.getResponseGenerator();
        exam.setResponseGenerator(responseGenerator);
        examResponse = exam;
        metricsInfo = (metricsInfo == null) ? MetricsInfo.createDefaultMetricsInfo() : metricsInfo;

        //Output model responses to CSV files for each mission        
        for (Mission<?> missionResponse : examResponse.getMissions()) {
            FileWriter fw = null;
            try {
                String missionId = missionResponse != null ? missionResponse.getId() : "null";
                if (missionResponse != null && missionResponse.getTestTrials() != null
                        && !missionResponse.getTestTrials().isEmpty()) {
                    //Create the CSV file for the mission
                    int missionNum = missionResponse.getMissionType().getMissionNum();
                    String siteName = responseGenerator != null
                            && responseGenerator.getSiteId() != null
                            ? responseGenerator.getSiteId() : "unknown";
                    String modelName = responseGenerator != null
                            && responseGenerator.getResponseGeneratorId() != null
                            ? responseGenerator.getResponseGeneratorId() : "model";
                    fw = new FileWriter(new File(outputDir,
                            siteName + "_" + modelName + "_mission_" + missionNum + ".csv"), false);

                    //Process the mission response data and write the data to the CSV file                    
                    if (outputProgress) {
                        System.out.println("Processing trial data for Mission: " + missionId);
                    }
                    //Extract the trial data and compute trial metrics for each trial
                    List<TrialData> trials = new LinkedList<TrialData>();
                    boolean firstTrial = true;
                    for (IcarusTestTrial_Phase2 trialResponse : missionResponse.getTestTrials()) {
                        if (trialResponse.isAttackHistoryTrial() == null
                                || !trialResponse.isAttackHistoryTrial()) { //Ignore attack history trials in Mission 6										
                            TrialData currTrialData = trialResponseProcessor.updateTrialData(
                                    exam, missionResponse, trialResponse, trialResponse.getResponseFeedBack(),
                                    false, null, examId, missionId, missionNum, responseGenerator);
                            trials.add(currTrialData);
                            //Compute the metrics
                            if (includeTrialMetrics) {
                                trialResponseProcessor.updateTrialMetrics(currTrialData, metricsInfo, null);
                            }
                            currTrialData.setData_type(DataType.Model_Single);
                            List<NameValuePair> trialDataValues = currTrialData.getDataAndMetricValuesAsString(
                                    missionResponse.getMissionType(), includeTrialPartName, includeTrialPartTime,
                                    includeParticipantGeneratedResponeFlag, includeTrialMetrics);

                            //Write the CSV file header
                            if (firstTrial) {
                                fw.append(createHeaderString(trialDataValues));
                                fw.append("\n");
                                firstTrial = false;
                            }

                            //Write the CSV file row
                            fw.append(createDataRowString(trialDataValues));
                            fw.append("\n");
                        }
                    }

                    //If Mission 1, compute the average SIGINT probabilities, which will be used to compute
                    //incremental Bayesian solutions in Missions 2 & 3                                
                    if (missionResponse.getMissionType() == MissionType.Mission_1) {
                        MissionMetrics missionMetrics = missionMetricsComputer.updateTaskMetrics(null, null, trials,
                                metricsInfo, null, missionNum);
                        if (missionMetrics != null) {
                            System.out.println("Computed Suject SIGINT Probs: PtChatter: "
                                    + missionMetrics.getSigintProbs_avg().getPtChatter() + ", PtSilent: "
                                    + missionMetrics.getSigintProbs_avg().getPtSilent());
                            trialResponseProcessor.setSubjectSigintProbs(
                                    missionMetrics.getSigintProbs_avg());
                        }
                    }
                } else {
                    System.err.println("Warning, no trials found for mission: " + missionId);
                }
            } catch (IOException ex) {
                throw ex;
            } finally {
                if (fw != null) {
                    fw.flush();
                    fw.close();
                }
            }
        }
    }
    
    /**
     * Aggregates data and create CSV files for the PSU Mission 1-3 Pilot Study.
     */
    public static void aggregateHumanDataSampleExam_1() {
        //Aggregate data and create CSV files for the PSU Mission 1-3 Pilot Study		
        List<Integer> missions = Arrays.asList(1, 2, 3);
        File dataDir = new File(BatchFileProcessor.assessmentFolder + "Sample-Exam-1/Human_Data/All Subjects");
        File outputDir = new File(BatchFileProcessor.assessmentFolder + "Sample-Exam-1/Human_Data/Aggregated Data");
        File examFile = new File(BatchFileProcessor.assessmentFolder + "Sample-Exam-1/Exam_Data/Sample-Exam-1.xml");
        aggregateSubjectData(missions, "Pre-test Questionnaire",
                new SurveyFileNameFormatterFormat1(), dataDir, outputDir, examFile,
                false, true, true, false, true);
    }

    /**
     * Aggregates data and creates CSV files for the PSU Mission 1-5 Pilot Study.
     */
    public static void aggregateHumanDataSampleExam_2() {
        //Aggregate data and create CSV files for the PSU Mission 1-5 Pilot Study
        List<Integer> missions = Arrays.asList(1, 2, 3, 4, 5);
        File dataDir = new File(BatchFileProcessor.assessmentFolder + "Sample-Exam-2/Human_Data/All Subjects");
        File outputDir = new File(BatchFileProcessor.assessmentFolder + "Sample-Exam-2/Human_Data/Aggregated Data");
        File examFile = new File(BatchFileProcessor.assessmentFolder + "Sample-Exam-2/Exam_Data/Sample-Exam-2.xml");        
        aggregateSubjectData(missions,  "Pre-test Questionnaire _ ICArUS Study _ Jul 2013 _ 000-1",
                new SurveyFileNameFormatterFormat2(), dataDir, outputDir, examFile,
                true, true, true, false, true);
    }

    /**
     * Aggregates data and creates CSV files by combining the PSU Mission 1-3 Pilot Study data
     * and Mission 1-5 Pilot Study Data (just the first three missions).		
     */
    public static void aggregateHumanDataSampleExam_1_2_Combined() {
        //Aggregate data and create CSV files by combining the PSU Mission 1-3 Pilot Study data
        //and Mission 1-5 Pilot Study Data (just the first three missions)		
        List<Integer> missions = Arrays.asList(1, 2, 3);
        File outputDir = new File(BatchFileProcessor.assessmentFolder + "Sample-Exam-1-2-Combined/Human_Data/Aggregated Data");
        File dataDir = new File(BatchFileProcessor.assessmentFolder + "Sample-Exam-1/Human_Data/All Subjects");
        File examFile = new File(BatchFileProcessor.assessmentFolder + "Sample-Exam-1/Exam_Data/Sample-Exam-1.xml");
        aggregateSubjectData(missions, "Pre-test Questionnaire",
                new SurveyFileNameFormatterFormat1(), dataDir, outputDir, examFile,
                false, true, true, false, true);
        dataDir = new File(BatchFileProcessor.assessmentFolder + "Sample-Exam-2/Human_Data/All Subjects");
        examFile = new File(BatchFileProcessor.assessmentFolder + "Sample-Exam-2/Exam_Data/Sample-Exam-2.xml");
        aggregateSubjectData(missions, "Pre-test Questionnaire _ ICArUS Study _ Jul 2013 _ 000-1",
                new SurveyFileNameFormatterFormat2(), dataDir, outputDir, examFile,
                true, true, true, false, true);
    }

    /**
     * Aggregates data for the Final Exam into a CSV file. 
     */
    public static void aggregateHumanDataFinalExam() {
        //Aggregate data and create CSV files for the Phase 2 Final Exam (Final-Exam-1)        
        //NOTE: Must always process Mission 1 to compute SIGINT probabilities
        List<Integer> missions = Arrays.asList(1, 2, 3, 4, 5);
        File dataDir = new File(BatchFileProcessor.assessmentFolder + "Final-Exam-1/Human_Data/All Subjects");
        File outputDir = new File(BatchFileProcessor.assessmentFolder + "Final-Exam-1/Human_Data/Aggregated Data");
        File examFile = new File(BatchFileProcessor.assessmentFolder + "Final-Exam-1/Exam_Data/Final-Exam-1.xml");
        aggregateSubjectData(missions, "Questionnaire",
                new SurveyFileNameFormatterFormat2(), dataDir, outputDir, examFile,
                false, true, true, false, true);
    }

    /**
     * Aggregates model data for the given exam into a CSV file.
     * 
     * @param examId
     * @param siteId
     * @param responseGeneratorId
     */
    public static void aggregateModelData(String examId, String siteId, String responseGeneratorId) {
        File outputDir = new File(BatchFileProcessor.assessmentFolder + examId + "/Model_Data/" + siteId + "/Run_Final");
        File examFile = new File(BatchFileProcessor.assessmentFolder + examId + "/Exam_Data/" + examId + ".xml");

        //Get the exam response file for the site
        File examResponseFile = null;
        if (siteId.equals("BBN")) {
            examResponseFile = new File(BatchFileProcessor.assessmentFolder + examId + "/Model_Data/BBN/Run_Final/"
                    + SubjectDataRecorderUtils.formatExamFileName(siteId, responseGeneratorId, examId));
        } else if (siteId.equals("HRL")) {
            examResponseFile = new File(BatchFileProcessor.assessmentFolder + examId + "/Model_Data/HRL/Run_Final/"
                    + SubjectDataRecorderUtils.formatExamFileName(siteId, responseGeneratorId, examId));
        } else if (siteId.equals("LMC")) {
            examResponseFile = new File(BatchFileProcessor.assessmentFolder + examId + "/Model_Data/LMC/Run_Final/"
                    + SubjectDataRecorderUtils.formatExamFileName(siteId, responseGeneratorId, examId));
        } else if (siteId.equals("MITRE")) {
            examResponseFile = new File(BatchFileProcessor.assessmentFolder + examId + "/Model_Data/MITRE/Run_Final/"
                    + SubjectDataRecorderUtils.formatExamFileName(siteId, responseGeneratorId, examId));
        } else {
            System.err.println("Unrecognized site: " + siteId);
        }

        //Aggregate the model data
        try {
            new CsvAggregator().aggregateModelData("Final-Exam-1", examFile, examResponseFile,
                    MetricsInfo.createDefaultMetricsInfo(), outputDir, true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        //aggregateHumanDataSampleExam_1();
        //aggregateHumanDataSampleExam_2();
        //aggregateHumanDataSampleExam_1_2_Combined();
        aggregateHumanDataFinalExam();
        //aggregateModelData("Final-Exam-1", "HRL", "aggregatedResponseTest");
        //aggregateModelData("Final-Exam-1", "MITRE", "A-B");
    }
}
