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
package org.mitre.icarus.cps.assessment.assessment_processor.phase_1;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.mitre.icarus.cps.app.experiment.IcarusSubjectData;
import org.mitre.icarus.cps.app.experiment.data_recorder.SubjectDataRecorderUtils;
import org.mitre.icarus.cps.assessment.assessment_metrics_computer.phase_1.ExamMetricsComputer;
import org.mitre.icarus.cps.assessment.assessment_metrics_computer.phase_1.SubjectMetricsComputer;
import org.mitre.icarus.cps.assessment.assessment_metrics_computer.phase_1.TaskMetricsComputer;
import org.mitre.icarus.cps.assessment.assessment_metrics_computer.phase_1.TrialMetricsComputer;
import org.mitre.icarus.cps.assessment.data_aggregator.DataAggregatorUtils;
import org.mitre.icarus.cps.assessment.data_model.base.DataType;
import org.mitre.icarus.cps.assessment.data_model.base.data_sets.AverageHumanDataSet;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.ExamMetrics;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.MetricsInfo;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.SubjectMetrics;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.TaskMetrics;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.TrialData;
import org.mitre.icarus.cps.assessment.data_model.phase_1.data_sets.AverageHumanDataSet_Phase1;
import org.mitre.icarus.cps.assessment.data_model.phase_1.data_sets.SingleModelDataSet_Phase1;
import org.mitre.icarus.cps.assessment.model_simulator.Model;
import org.mitre.icarus.cps.assessment.model_simulator.phase_1.AbModel;
import org.mitre.icarus.cps.assessment.model_simulator.phase_1.AbstractModel_Phase1;
import org.mitre.icarus.cps.assessment.model_simulator.phase_1.BayesianModel;
import org.mitre.icarus.cps.assessment.persistence.phase_1.spreadsheet.SpreadsheetCPADataPersister;
import org.mitre.icarus.cps.assessment.persistence.phase_1.xml.XMLCPADataPersister;
import org.mitre.icarus.cps.assessment.score_computer.phase_1.BatchScoreComputer;
import org.mitre.icarus.cps.assessment.score_computer.phase_1.ScoreComputer;
import org.mitre.icarus.cps.exam.base.response.ResponseGeneratorData;
import org.mitre.icarus.cps.exam.phase_1.IcarusExam_Phase1;
import org.mitre.icarus.cps.exam.phase_1.loader.IcarusExamLoader_Phase1;
import org.mitre.icarus.cps.exam.phase_1.testing.AttackLocationPresentationTrial;
import org.mitre.icarus.cps.exam.phase_1.testing.IcarusTestTrial_Phase1;
import org.mitre.icarus.cps.exam.phase_1.testing.TaskTestPhase;

/**
 * Processes Phase 1 human subject data files in batch and generates the average
 * human response. Also processes model data files and generates model
 * assessment results.
 *
 * @author CBONACETO
 *
 */
public class BatchFileProcessor {
    
    /** The assessment data folder */
    public static String assessmentFolder = "data/Phase_1_CPD/assessment/";

    /** The trial response processor */
    protected TrialResponseProcessor trialResponseProcessor;

    /** The trial metrics computer */
    protected TrialMetricsComputer trialMetricsComputer;

    /** The subject metrics computer */
    protected SubjectMetricsComputer subjectMetricsComputer;

    /** The task metrics computer */
    protected TaskMetricsComputer taskMetricsComputer;

    /** The exam metrics computer */
    protected ExamMetricsComputer examMetricsComputer;

     /** Phase 1 score computer instance */
    protected ScoreComputer scoreComputer;

    public BatchFileProcessor() {
        trialResponseProcessor = new TrialResponseProcessor();
        trialMetricsComputer = new TrialMetricsComputer();
        subjectMetricsComputer = new SubjectMetricsComputer();
        taskMetricsComputer = new TaskMetricsComputer();
        examMetricsComputer = new ExamMetricsComputer();
        scoreComputer = new ScoreComputer();
    }

    /**
     * Process the overall data set for humans as a batch and generate the
     * average human data set
     *
     * @param examId
     * @param taskIds
     * @param taskNumbers
     * @param examFile
     * @param dataDir
     * @param subjects
     * @param metricsInfo
     * @param outputProgress
     * @return
     */
    public AverageHumanDataSet_Phase1 buildAverageHumanDataSet(String examId, List<String> taskIds, List<Integer> taskNumbers,
            File examFile, File dataDir, List<Set<IcarusSubjectData>> subjects, MetricsInfo metricsInfo, boolean outputProgress) {
        //Load the exam
        IcarusExam_Phase1 exam = null;
        try {
            exam = IcarusExamLoader_Phase1.unmarshalExam(examFile.toURI().toURL(), false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (exam == null) {
            return null;
        }
        exam.setId(examId);
        return buildAverageHumanDataSet(exam, taskIds, taskNumbers, dataDir, subjects, metricsInfo, outputProgress);
    }

    /**
     * Process the overall data set for humans as a batch and generate the
     * average human data set
     *
     * @param exam
     * @param taskIds
     * @param taskNumbers
     * @param dataDir
     * @param subjects
     * @param metricsInfo
     * @param outputProgress
     * @return
     */
    public AverageHumanDataSet_Phase1 buildAverageHumanDataSet(
            IcarusExam_Phase1 exam, List<String> taskIds, List<Integer> taskNumbers,
            File dataDir, List<Set<IcarusSubjectData>> subjects, MetricsInfo metricsInfo, boolean outputProgress) {
        if (!dataDir.isDirectory() || !dataDir.exists()) {
            throw new IllegalArgumentException(dataDir.getPath() + " is not a directory or cannot be found.");
        }
        if (taskIds == null || taskIds.isEmpty() || taskIds.size() != taskNumbers.size()) {
            throw new IllegalArgumentException("Task IDs and Task Numbers must be specified");
        }

        metricsInfo = metricsInfo == null ? MetricsInfo.createDefaultMetricsInfo() : metricsInfo;
        int numTasks = taskIds.size();
        String examId = exam.getId();

        //Trial metrics for each subject on each trial of each task (Task->Trials->Subjects)
        List<List<List<TrialData>>> trialDataByTask = new ArrayList<List<List<TrialData>>>(numTasks);
        for (TaskTestPhase<?> task : exam.getTasks()) {
            if (taskIds.contains(task.getId())) {
                List<List<TrialData>> currTaskTrialData = new ArrayList<List<TrialData>>(task.getNumTrials());
                trialDataByTask.add(currTaskTrialData);
                for (int i = 0; i < task.getNumTrials(); i++) {
                    currTaskTrialData.add(new LinkedList<TrialData>());
                }
            }
        }

        //Trial Data for each subject on Task 6 (Task for which subject metrics are computed, Subject->Trials)
        List<List<TrialData>> trialDataBySubject_Task_6 = new LinkedList<List<TrialData>>();

        //Overall subject metrics for each subject (Computed using only Task 6 data)
        List<SubjectMetrics> subjectMetrics = new LinkedList<SubjectMetrics>();

        //Average time on task for each task
        List<Double> timeOnTask = new ArrayList<Double>(numTasks);

        //Process subject data for each task
        int taskIndex = 0;
        for (String taskId : taskIds) {
            double taskTime = 0d;
            int numTimesOnTask = 0;
            int taskNum = taskNumbers.get(taskIndex);
            List<List<TrialData>> currTaskTrialData = trialDataByTask.get(taskIndex);
            for (IcarusSubjectData subject : subjects.get(taskIndex)) {
                //Load the task data for the current subject on the current task
                File file = new File(dataDir, "/" + SubjectDataRecorderUtils.formatSubjectFolderName(subject) + "/"
                        + SubjectDataRecorderUtils.formatDataFileName(subject, examId, taskId));
                TaskTestPhase<?> taskResponse = null;
                try {
                    taskResponse = IcarusExamLoader_Phase1.unmarshalTask(file.toURI().toURL(), false);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                //Process the task data for the current subject
                if (taskResponse != null && taskResponse.getTestTrials() != null && !taskResponse.getTestTrials().isEmpty()) {
                    //Update average time on task
                    //System.out.println(taskResponse.getStartTime() + ", "+  taskResponse.getEndTime());
                    Double time = (taskResponse.getStartTime() != null && taskResponse.getEndTime() != null)
                            ? new Double(taskResponse.getEndTime().getTime() - taskResponse.getStartTime().getTime()) : null;
                    if (time != null) {
                        taskTime += time;
                        numTimesOnTask++;
                    }
                    //Compute trial metrics for each trial
                    if (outputProgress) {
                        System.out.println("Processing trial metrics for: " + subject.getSubjectId() + ", " + taskId);
                    }
                    List<TrialData> task6TrialData = null;
                    if (taskNum == 6) {
                        task6TrialData = new LinkedList<TrialData>();
                        trialDataBySubject_Task_6.add(task6TrialData);
                    }
                    ResponseGeneratorData responseGenerator = taskResponse.getResponseGenerator();
                    int trialIndex = 0;
                    for (IcarusTestTrial_Phase1 trialResponse : taskResponse.getTestTrials()) {
                        if (!(trialResponse instanceof AttackLocationPresentationTrial)) { //Ignore presentation trials in Tasks 1-3
                            TrialData currTrialData = trialResponseProcessor.updateTrialData(exam, taskResponse, trialResponse,
                                    trialResponse.getTrialResponse() != null ? trialResponse.getTrialResponse().getResponseFeedBack() : null,
                                    false, null, examId, taskId, taskNum, responseGenerator);
                            trialResponseProcessor.updateTrialMetrics(currTrialData, metricsInfo, null);
                            currTrialData.setData_type(DataType.Human_Single);
                            currTaskTrialData.get(trialIndex).add(currTrialData);
                            if (taskNum == 6) {
                                task6TrialData.add(currTrialData);
                            }
                            trialIndex++;
                        }
                    }
                } else {
                    System.err.println("Warning, no trials found for file: " + file.getName());
                }
            }
            if (numTimesOnTask > 0) {
                timeOnTask.add(taskTime / numTimesOnTask);
            } else {
                timeOnTask.add(0d);
            }
            taskIndex++;
        }

        //Compute overall subject metrics for each subject
        for (List<TrialData> task6TrialData : trialDataBySubject_Task_6) {
            SubjectMetrics currSubjectMetrics = subjectMetricsComputer.updateSubjectMetrics_Trials(
                    null, task6TrialData, metricsInfo, null, false, true);
            if (task6TrialData != null && !task6TrialData.isEmpty()) {
                TrialData trial1Data = task6TrialData.get(0);
                if (trial1Data != null) {
                    currSubjectMetrics.setSite_id(trial1Data.getSite_id());
                    currSubjectMetrics.setResponse_generator_id(trial1Data.getResponse_generator_id());
                }
            }
            currSubjectMetrics.setData_type(DataType.Human_Single);
            currSubjectMetrics.setHuman(true);
            currSubjectMetrics.setExam_id(examId);
            subjectMetrics.add(currSubjectMetrics);
        }

        //Compute the average subject metrics over all subjects
        SubjectMetrics averageSubjectMetrics = subjectMetricsComputer.updateAverageSubjectMetrics(
                subjectMetrics, null, null, metricsInfo);
        averageSubjectMetrics.setData_type(DataType.Human_Avg);
        averageSubjectMetrics.setHuman(true);
        averageSubjectMetrics.setExam_id(examId);

        //Compute the average trial metrics over all subjects for each trial in each task
        List<TaskMetrics> averageTaskMetrics = new ArrayList<TaskMetrics>();
        for (taskIndex = 0; taskIndex < numTasks; taskIndex++) {
            int taskNum = taskNumbers.get(taskIndex);
            List<List<TrialData>> trialData = trialDataByTask.get(taskIndex);
            String taskId = taskIds.get(taskIndex);
            TaskMetrics taskMetrics = new TaskMetrics();
            averageTaskMetrics.add(taskMetrics);
            List<TrialData> avgTrialData = new ArrayList<TrialData>();
            taskMetrics.setTrials(avgTrialData);
            taskMetrics.setData_type(DataType.Human_Avg);
            taskMetrics.setHuman(true);
            taskMetrics.setExam_id(examId);
            taskMetrics.setTask_id(taskId);
            taskMetrics.setTask_number(taskNum);
            taskMetrics.setTask_complete(true);
            taskMetrics.setAll_trials_valid(true);
            taskMetrics.setNum_subjects(null);

            for (List<TrialData> subjectTrials : trialData) {
                if (taskMetrics.getNum_subjects() == null) {
                    taskMetrics.setNum_subjects(subjectTrials.size());
                }
                //Compute average trial over each of the subject trials
                TrialData averageTrial = new TrialData();
                averageTrial.setData_type(DataType.Human_Avg);
                averageTrial.setHuman(true);
                averageTrial.setExam_id(examId);
                averageTrial.setTask_id(taskId);
                averageTrial.setTask_number(taskNum);
                averageTrial.setTrial_number((subjectTrials != null && !subjectTrials.isEmpty()) ? subjectTrials.get(0).getTrial_number() : null);
                averageTrial.setTrial_complete(true);
                averageTrial.setTrial_valid(true);
                averageTrial = trialMetricsComputer.updateAverageTrialMetrics(
                        subjectTrials, averageTrial, null, metricsInfo, taskNum);
                avgTrialData.add(averageTrial);
            }

            //Compute the average task metrics using the average trials
            taskMetricsComputer.updateTaskMetrics(taskMetrics, timeOnTask.get(taskIndex), avgTrialData, metricsInfo,
                    null, taskNum);
        }

        if (outputProgress) {
            System.out.println("Complete");
        }

        AverageHumanDataSet_Phase1 dataSet = new AverageHumanDataSet_Phase1();
        dataSet.setExam_id(examId);
        dataSet.setTime_stamp(System.currentTimeMillis());
        dataSet.setMetricsInfo(metricsInfo);
        dataSet.setTaskMetrics(averageTaskMetrics);
        dataSet.setSubjectMetrics(averageSubjectMetrics);
        return dataSet;
    }

    /**
     * @param examId
     * @param taskIds
     * @param taskNumbers
     * @param examFile
     * @param examResponseFile
     * @param metricsInfo
     * @param comparisonData
     * @param outputProgress
     * @return
     */
    public SingleModelDataSet_Phase1 buildModelDataSet(String examId, List<String> taskIds, List<Integer> taskNumbers,
            File examFile, File examResponseFile, MetricsInfo metricsInfo,
            AverageHumanDataSet<TrialData, TaskMetrics, SubjectMetrics, MetricsInfo> comparisonData,
            boolean outputProgress) {
        //Load the exam and feature vector data (used to compute normative solutions)
        IcarusExam_Phase1 exam = null;
        try {
            exam = IcarusExamLoader_Phase1.unmarshalExam(examFile.toURI().toURL(), false);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (exam == null) {
            return null;
        }
        exam.setId(examId);
        for (TaskTestPhase<?> task : exam.getTasks()) {
            try {
                IcarusExamLoader_Phase1.initializeTaskFeatureVectorData(task, examFile.toURI().toURL(), exam.getGridSize(),
                        false, null);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        //Load the model exam response file (contains responses to all Tasks)
        IcarusExam_Phase1 examResponse = null;
        try {
            examResponse = IcarusExamLoader_Phase1.unmarshalExam(examResponseFile.toURI().toURL(), false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (examResponse == null || examResponse.getTasks() == null || examResponse.getTasks().isEmpty()) {
            System.err.println("Exam response not loaded or did not contain any tasks");
            return null;
        }
        return buildModelDataSet(exam, examResponse, taskIds, taskNumbers, metricsInfo, comparisonData,
                outputProgress);
    }

    /**
     * Create a model data set for the given exam and model exam response.
     * 
     * @param exam
     * @param examResponse
     * @param taskIds
     * @param taskNumbers
     * @param metricsInfo
     * @param comparisonData
     * @param outputProgress
     * @return
     */
    public SingleModelDataSet_Phase1 buildModelDataSet(IcarusExam_Phase1 exam, IcarusExam_Phase1 examResponse,
            List<String> taskIds, List<Integer> taskNumbers,
            MetricsInfo metricsInfo,
            AverageHumanDataSet<TrialData, TaskMetrics, SubjectMetrics, MetricsInfo> comparisonData,
            boolean outputProgress) {
        String examId = exam.getId();

        //Score the exam to compute feedback and normative solutions
        for (TaskTestPhase<?> task : examResponse.getTasks()) {
            try {
                TaskTestPhase<?> scoredTask = BatchScoreComputer.addSubjectResponsesToExamTask(exam, task);
                scoreComputer.computeSolutionAndScoreForTask(scoredTask, exam.getProbabilityRules(), exam.getGridSize(), true, null);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        exam.setResponseGenerator(examResponse.getResponseGenerator());
        examResponse = exam;

        //DEBUG CODE
        //try {
        //	System.out.println(IcarusExamLoader_Phase1.marshalExam(examResponse));
        //} catch(Exception ex) {}
        metricsInfo = (metricsInfo == null) ? MetricsInfo.createDefaultMetricsInfo() : metricsInfo;
        ResponseGeneratorData responseGenerator = examResponse.getResponseGenerator();

        //Task and trial metrics for each task
        List<TaskMetrics> taskMetrics = new ArrayList<TaskMetrics>(taskIds.size());

        //Trial Data for each Task 6 trial (Task for which subject metrics are computed, Subject->Trials)
        List<TrialData> task6TrialData = null;

        //Overall subject metrics (Computed using only Task 6 data)
        SubjectMetrics subjectMetrics = null;

        //Process model response data for each task
        int taskIndex = 0;
        for (String taskId : taskIds) {
            //Find the task in the exam response
            TaskTestPhase<?> taskResponse = null;
            for (TaskTestPhase<?> task : examResponse.getTasks()) {
                if (task.getId().equalsIgnoreCase(taskId)) {
                    taskResponse = task;
                    break;
                }
            }
            //Process the task response data 
            if (taskResponse != null && taskResponse.getTestTrials() != null && !taskResponse.getTestTrials().isEmpty()) {
                ResponseGeneratorData currResponseGenerator = responseGenerator != null ? responseGenerator : taskResponse.getResponseGenerator();
                String generatorName = currResponseGenerator != null ? currResponseGenerator.getSiteId() + "-" + currResponseGenerator.getResponseGeneratorId() : "";
                int taskNum = taskNumbers.get(taskIndex);
                TaskMetrics currTaskMetrics = new TaskMetrics();
                taskMetrics.add(currTaskMetrics);
                List<TrialData> currTaskTrialData = new ArrayList<TrialData>(taskResponse.getTestTrials().size());
                currTaskMetrics.setTrials(currTaskTrialData);
                if (currResponseGenerator != null) {
                    currTaskMetrics.setSite_id(currResponseGenerator.getSiteId());
                    currTaskMetrics.setResponse_generator_id(currResponseGenerator.getResponseGeneratorId());
                }
                currTaskMetrics.setData_type(DataType.Model_Single);
                currTaskMetrics.setHuman(false);
                currTaskMetrics.setExam_id(examId);
                currTaskMetrics.setTask_id(taskId);
                currTaskMetrics.setTask_number(taskNum);
                currTaskMetrics.setNum_subjects(1);

                //Get the comparison task metrics for the current task if available (e.g., the average human task metrics)
                TaskMetrics comparisonTaskMetrics = null;
                if (comparisonData != null && comparisonData.getTaskMetrics() != null && !comparisonData.getTaskMetrics().isEmpty()) {
                    for (TaskMetrics metrics : comparisonData.getTaskMetrics()) {
                        if (metrics.getTask_id() != null && metrics.getTask_id().equalsIgnoreCase(taskId)) {
                            comparisonTaskMetrics = metrics;
                            break;
                        }
                    }
                }

                //Compute trial metrics for each trial
                if (outputProgress) {
                    System.out.println("Processing trial metrics for: " + generatorName + ", " + taskId);
                }
                if (taskNum == 6) {
                    task6TrialData = new LinkedList<TrialData>();
                }
                int trialIndex = 0;
                for (IcarusTestTrial_Phase1 trialResponse : taskResponse.getTestTrials()) {
                    if (!(trialResponse instanceof AttackLocationPresentationTrial)) { //Ignore presentation trials in Tasks 1-3
                        TrialData currTrialData = trialResponseProcessor.updateTrialData(exam, taskResponse, trialResponse,
                                trialResponse.getTrialResponse() != null ? trialResponse.getTrialResponse().getResponseFeedBack() : null,
                                false, null, examId, taskId, taskNum, currResponseGenerator);
                        trialResponseProcessor.updateTrialMetrics(currTrialData, metricsInfo,
                                comparisonTaskMetrics != null && comparisonTaskMetrics.getTrials() != null
                                && trialIndex < comparisonTaskMetrics.getTrials().size() ? comparisonTaskMetrics.getTrials().get(trialIndex) : null);
                        currTrialData.setData_type(DataType.Model_Single);
                        currTaskTrialData.add(currTrialData);
                        if (taskNum == 6) {
                            task6TrialData.add(currTrialData);
                        }
                        trialIndex++;
                    }
                }

                //Compute the task metrics for the current task
                taskMetricsComputer.updateCompletionStatus(currTaskMetrics, currTaskTrialData);
                if (currTaskMetrics.isTask_complete()) {
                    Double taskTime = (taskResponse.getStartTime() != null && taskResponse.getEndTime() != null)
                            ? new Double(taskResponse.getEndTime().getTime() - taskResponse.getStartTime().getTime()) : null;
                    taskMetricsComputer.updateTaskMetrics(currTaskMetrics, taskTime, currTaskTrialData, metricsInfo,
                            comparisonTaskMetrics, taskNum);
                } else {
                    System.err.println("Warning, task not complete: " + taskId);
                }

            } else {
                System.err.println("Warning, task missining or did not contain any trials: " + taskId);
            }
            taskIndex++;
        }

        //Compute the overall subject metrics
        if (task6TrialData != null && !task6TrialData.isEmpty()) {
            subjectMetrics = subjectMetricsComputer.updateSubjectMetrics_Trials(
                    null, task6TrialData, metricsInfo,
                    comparisonData != null ? comparisonData.getSubjectMetrics() : null,
                    false, true);
            if (responseGenerator != null) {
                subjectMetrics.setSite_id(responseGenerator.getSiteId());
                subjectMetrics.setResponse_generator_id(responseGenerator.getResponseGeneratorId());
            }
            subjectMetrics.setData_type(DataType.Model_Single);
            subjectMetrics.setHuman(false);
            subjectMetrics.setExam_id(examId);
            subjectMetrics.setNum_subjects(1);
        }

        //Compute the overall exam metrics
        ExamMetrics examMetrics = new ExamMetrics();
        examMetrics.setTasks(taskMetrics);
        examMetrics.setExam_id(examId);
        if (responseGenerator != null) {
            examMetrics.setSite_id(responseGenerator.getSiteId());
            examMetrics.setResponse_generator_id(responseGenerator.getResponseGeneratorId());
        }
        examMetricsComputer.updateCompletionStatus(examMetrics, taskMetrics);
        examMetricsComputer.updateAllMetrics(examMetrics, taskMetrics, subjectMetrics, metricsInfo);

        if (outputProgress) {
            System.out.println("Complete");
        }

        SingleModelDataSet_Phase1 dataSet = new SingleModelDataSet_Phase1();
        dataSet.setExam_id(examId);
        dataSet.setSite_id(examMetrics.getSite_id());
        dataSet.setResponse_generator_id(examMetrics.getResponse_generator_id());
        dataSet.setTime_stamp(System.currentTimeMillis());
        dataSet.setExamMetrics(examMetrics);
        dataSet.setSubjectMetrics(subjectMetrics);
        return dataSet;
    }

    /**
     * Create the average human data set for the Phase 1 Final Exam.
     * 
     * @return 
     */
    protected static AverageHumanDataSet_Phase1 buildFinalExamAverageHumanDataSet() {
        String examId = "Final-Exam-1";
        List<String> taskIds = new ArrayList<String>(Arrays.asList("Task1", "Task2", "Task3", "Task4", "Task5", "Task6"));
        List<Integer> taskNumbers = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6));
        //List<String> taskIds = new ArrayList<String>(Arrays.asList("Task6"));
        //List<Integer> taskNumbers = new ArrayList<Integer>(Arrays.asList(6));
        File examFile = new File(assessmentFolder + "Final_Exam/Exam_Data/FinalExam.xml");
        File dataDir = new File(assessmentFolder + "Final_Exam/Human_Data/All Subjects");
        File outputDir = new File(assessmentFolder + "Final_Exam/Assessment_Results");

        AverageHumanDataSet_Phase1 dataSet = null;
        try {
            //Use all subjects for Tasks 1-5 (N=103)
            Set<IcarusSubjectData> allSubjects = DataAggregatorUtils.getSubjectsInFolder(dataDir, true);
            System.out.println("num subjects: " + allSubjects.size());
            /*Set<IcarusSubjectData> allSubjects = new HashSet<IcarusSubjectData>(Arrays.asList(
             new IcarusSubjectData("101", new Site("BED", "BED")), 
             new IcarusSubjectData("102", new Site("BED", "BED")), 
             new IcarusSubjectData("103", new Site("BED", "BED"))));*/

            //Use only standard layer order subjects for Task 6 (N=79)
            File standardLayerOrderDir = new File(assessmentFolder + "Final_Exam/Human_Data/Standard Layer Order Subjects");
            Set<IcarusSubjectData> standardOrderSubjects = DataAggregatorUtils.getSubjectsInFolder(standardLayerOrderDir, true);
            //Set<IcarusSubjectData> standardOrderSubjects = allSubjects;

            List<Set<IcarusSubjectData>> subjects = new ArrayList<Set<IcarusSubjectData>>();
            for (int i = 0; i < 6; i++) {
                if (i < 5) {
                    subjects.add(allSubjects);
                } else {
                    subjects.add(standardOrderSubjects);
                }
            }

            //Process the data set
            BatchFileProcessor bfp = new BatchFileProcessor();
            dataSet = bfp.buildAverageHumanDataSet(examId, taskIds, taskNumbers, examFile, dataDir, subjects,
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
     * Create a model data set for the Phase 1 Final Exam.
     * 
     * @param siteId
     * @param responseGeneratorId
     * @param comparisonData
     * @return
     */
    protected static SingleModelDataSet_Phase1 buildFinalExamModelDataSet(
            String siteId, String responseGeneratorId,
            AverageHumanDataSet<TrialData, TaskMetrics, SubjectMetrics, MetricsInfo> comparisonData) {
        String examId = "Final-Exam-1";
        List<String> taskIds = new ArrayList<String>(Arrays.asList("Task1", "Task2", "Task3", "Task4", "Task5", "Task6"));
        List<Integer> taskNumbers = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6));
        //List<String> taskIds = new ArrayList<String>(Arrays.asList("Task1"));
        //List<Integer> taskNumbers = new ArrayList<Integer>(Arrays.asList(1));
        File examFile = new File(assessmentFolder + "Final_Exam/Exam_Data/FinalExam.xml");
        File outputDir = new File(assessmentFolder + "Final_Exam/Assessment_Results");

        //Get the exam response file for the site
        File examResponseFile = null;
        if (siteId.equals("BBN")) {
            examResponseFile = new File(assessmentFolder + "Final_Exam/Model_Data/BBN/Run_Final/"
                    + SubjectDataRecorderUtils.formatExamFileName(siteId, responseGeneratorId, examId));
        } else if (siteId.equals("HRL")) {
            examResponseFile = new File(assessmentFolder + "Final_Exam/Model_Data/HRL/Run_Final/"
                    + SubjectDataRecorderUtils.formatExamFileName(siteId, responseGeneratorId, examId));
        } else if (siteId.equals("LMC")) {
            examResponseFile = new File(assessmentFolder + "Final_Exam/Model_Data/LMC/Run_Final/"
                    + SubjectDataRecorderUtils.formatExamFileName(siteId, responseGeneratorId, examId));
        } else {
            System.err.println("Unrecognized site: " + siteId);
        }

        SingleModelDataSet_Phase1 dataSet = null;
        if (examResponseFile != null) {
            BatchFileProcessor bfp = new BatchFileProcessor();
            dataSet = bfp.buildModelDataSet(examId, taskIds, taskNumbers,
                    examFile, examResponseFile, MetricsInfo.createDefaultMetricsInfo(), comparisonData,
                    true);
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
     * Create a model data set using the given model for the Phase 1 Final Exam.
     * 
     * @param model
     * @param comparisonData
     * @return
     */
    protected static SingleModelDataSet_Phase1 buildFinalExamSampleModelDataSet(
            AbstractModel_Phase1 model, AverageHumanDataSet<TrialData, TaskMetrics, SubjectMetrics, MetricsInfo> comparisonData) {
        String examId = "Final-Exam-1";
        List<String> taskIds = new ArrayList<String>(Arrays.asList("Task1", "Task2", "Task3", "Task4", "Task5", "Task6"));
        List<Integer> taskNumbers = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6));
        //List<String> taskIds = new ArrayList<String>(Arrays.asList("Task1"));
        //List<Integer> taskNumbers = new ArrayList<Integer>(Arrays.asList(1));
        File examFile = new File(assessmentFolder + "Final_Exam/Exam_Data/FinalExam.xml");
        File outputDir = new File(assessmentFolder + "Final_Exam/Assessment_Results");

        //Load the exam and feature vector data
        IcarusExam_Phase1 exam = null;
        try {
            exam = IcarusExamLoader_Phase1.unmarshalExam(examFile.toURI().toURL(), false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (exam == null) {
            return null;
        }
        exam.setId(examId);
        for (TaskTestPhase<?> task : exam.getTasks()) {
            try {
                IcarusExamLoader_Phase1.initializeTaskFeatureVectorData(task, examFile.toURI().toURL(), exam.getGridSize(),
                        false, null);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        //Generate the exam responses using the sample model
        IcarusExam_Phase1 examResponse = model.generateExamResponses(exam);
        examResponse.setResponseGenerator(new ResponseGeneratorData(model.getSiteId(), model.getResponseGeneratorId(), false));

        BatchFileProcessor bfp = new BatchFileProcessor();
        SingleModelDataSet_Phase1 dataSet = bfp.buildModelDataSet(exam, examResponse, taskIds, taskNumbers,
                MetricsInfo.createDefaultMetricsInfo(), comparisonData, true);

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

    public static void main(String[] args) {
        //Create or load human data set
        XMLCPADataPersister xml = null;
        AverageHumanDataSet_Phase1 humanDataSet = null;
        //humanDataSet = buildFinalExamAverageHumanDataSet();
        try {
            xml = new XMLCPADataPersister(new File(assessmentFolder + "Final_Exam/Assessment_Results").toURI().toURL());
            humanDataSet = xml.loadAverageHumanDataSet("Final-Exam-1");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //Create data sets for teams
        SingleModelDataSet_Phase1 dataSet = buildFinalExamModelDataSet("BBN", "Insight23", humanDataSet);
        //SingleModelDataSet_Phase1 dataSet = buildFinalExamModelDataSet("LMC", "Model0-MITRE", humanDataSet);
        //SingleModelDataSet_Phase1 dataSet = buildFinalExamModelDataSet("HRL", "aggregatedResponseTestMITRE", humanDataSet);       
        //SingleModelDataSet_Phase1 dataSet = buildFinalExamModelDataSet("BBN", "Insight23-Alpha0.0", humanDataSet);
        //SingleModelDataSet_Phase1 dataSet = buildFinalExamModelDataSet("BBN", "Insight23-Alpha1.0", humanDataSet);
        //SingleModelDataSet_Phase1 dataSet = buildFinalExamModelDataSet("BBN", "Insight23-Alpha0.7", humanDataSet);		

	//SingleModelDataSet_Phase1 dataSet = buildFinalExamModelDataSet("HRL", "aggregatedResponseTest3", humanDataSet);
        //SingleModelDataSet_Phase1 dataSet = buildFinalExamModelDataSet("HRL", "aggregatedResponseTestMITRE", humanDataSet);
        //SingleModelDataSet_Phase1 dataSet = buildFinalExamModelDataSet("LMC", "Model0-Run1", humanDataSet);
        //SingleModelDataSet_Phase1 dataSet = buildFinalExamModelDataSet("LMC", "Model0-Run2", humanDataSet);
        //SingleModelDataSet_Phase1 dataSet = buildFinalExamModelDataSet("LMC", "Model0-MITRE", humanDataSet);		
        
         //Create the assessment spreadsheet
        SpreadsheetCPADataPersister spreadsheetPersister;
        try {
            spreadsheetPersister = new SpreadsheetCPADataPersister(
                    new File(assessmentFolder + "Final_Exam/Assessment_Results").toURI().toURL(), humanDataSet);
            spreadsheetPersister.persistSingleModelDataSet(dataSet);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        //Create data sets for sample models created by MITRE
        BayesianModel bayesianModel = new BayesianModel(); 
        bayesianModel.setResponseGeneratorId("Bayesian");
        bayesianModel.setSiteId("MITRE");
        buildFinalExamSampleModelDataSet(bayesianModel, humanDataSet);        
        AbModel abModel = new AbModel(); 
        abModel.setResponseGeneratorId("AB-Pilot");		
        abModel.setSiteId("MITRE");
        buildFinalExamSampleModelDataSet(abModel, humanDataSet);
        
        /*
         //DEBUG CODE
         //Display RR results
         System.out.println("RR:");
         for(int task = 0; task <= 2; task++) {
         TaskMetrics taskMetrics = dataSet.getExamMetrics().getTasks().get(task);
         System.out.println("Task " + (task+1) + " RR Average Score: " + taskMetrics.getRR_avg_score());
         List<TrialData> trialData = dataSet.getExamMetrics().getTasks().get(task).getTrials();
         System.out.println("Pass/Fail:");
         for(TrialData trial : trialData) {
         boolean assessed = trial.getMetrics().getRR().assessed;
         if(assessed) {
         System.out.println(trial.getMetrics().getRR().pass ? "Pass" : "Fail");
         } else {
         System.out.println("N/A");
         }
         }
         System.out.println("Bias Observed for AbstractModel_Phase1:");
         for(TrialData trial : trialData) {
         System.out.println(trial.getMetrics().getRR().exhibited ? "Yes" : "No");
         }
         System.out.println("Bias Magnitude for AbstractModel_Phase1:");
         for(TrialData trial : trialData) {
         System.out.println(trial.getMetrics().getRR().magnitude);
         }
         }
         System.out.println();		
		
         //Display AA results
         System.out.println("AA:");
         TaskMetrics taskMetrics = dataSet.getExamMetrics().getTasks().get(4);
         System.out.println("Task 5 AA Average Score: " + taskMetrics.getAI_avg_score());
         List<TrialData> task5trialData = taskMetrics.getTrials();
         System.out.println("Pass/Fail:");
         for(TrialData trial : task5trialData) {
         List<CFAMetric> AI = trial.getMetrics().getAI();
         for(int stage = 1; stage < 5; stage++) {
         boolean assessed = AI.get(stage).assessed;
         if(assessed) {
         System.out.println(AI.get(stage).pass ? "Pass" : "Fail");
         } else {
         System.out.println("N/A");
         }				
         }
			
         }
         System.out.println("Bias Observed for AbstractModel_Phase1");
         for(TrialData trial : task5trialData) {
         List<CFAMetric> AI = trial.getMetrics().getAI();
         for(int stage = 1; stage < 5; stage++) {
         System.out.println(AI.get(stage).exhibited ? "Yes" : "No");				
         }			
         }
         System.out.println("Bias Magnitude for AbstractModel_Phase1");
         for(TrialData trial : task5trialData) {
         List<CFAMetric> AI = trial.getMetrics().getAI();
         for(int stage = 1; stage < 5; stage++) {
         System.out.println(AI.get(stage).magnitude);				
         }			
         }
         System.out.println("|delta Np| < |delta Nq| for AbstractModel_Phase1");
         for(TrialData trial : task5trialData) {
         List<Double> Np_delta = trial.getMetrics().getNp_delta();
         List<Double> Nq_delta = trial.getMetrics().getNq_delta();			
         for(int stage = 1; stage < 5; stage++) {
         System.out.println(Math.abs(Np_delta.get(stage)) < Math.abs(Nq_delta.get(stage)) ? "Yes" : "No");
         }			
         }
         System.out.println("Sign(delta Np) = Sign(delta Nq) for AbstractModel_Phase1");
         for(TrialData trial : task5trialData) {
         List<Double> Np_delta = trial.getMetrics().getNp_delta();
         List<Double> Nq_delta = trial.getMetrics().getNq_delta();			
         for(int stage = 1; stage < 5; stage++) {
         System.out.println(Math.signum(Np_delta.get(stage)) == Math.signum(Nq_delta.get(stage)) ? "Yes" : "No");
         }			
         }
         System.out.println();
		
         //Display PM results
         System.out.println("PM:");
         for(int task = 3; task <= 5; task++) {
         taskMetrics = dataSet.getExamMetrics().getTasks().get(task);
         System.out.println("Task " + (task+1) + " PM Average Score: " + taskMetrics.getPM_RMS_avg_score());
         List<TrialData> trialData = dataSet.getExamMetrics().getTasks().get(task).getTrials();
         System.out.println("Pass/Fail:");
         for(TrialData trial : trialData) {
         boolean assessed = trial.getMetrics().getPM().assessed;
         if(assessed) {
         System.out.println(trial.getMetrics().getPM().pass ? "Pass" : "Fail");
         } else {
         System.out.println("N/A");
         }
         }
         System.out.println("Bias Observed for AbstractModel_Phase1:");
         for(TrialData trial : trialData) {
         System.out.println(trial.getMetrics().getPM().exhibited ? "Yes" : "No");
         }
         System.out.println("Bias Magnitude for AbstractModel_Phase1:");
         for(TrialData trial : trialData) {
         System.out.println(trial.getMetrics().getPM().magnitude);
         }
         }
         System.out.println();
		
         //Display RSR results
         System.out.println("RSR:");
         for(int task = 0; task <= 4; task++) {
         taskMetrics = dataSet.getExamMetrics().getTasks().get(task);
         System.out.println(taskMetrics.getRSR_avg());
         if(taskMetrics.getRSR_stage_avg() != null && taskMetrics.getRSR_stage_avg().size() > 1) {
         for(Double rsr : taskMetrics.getRSR_stage_avg()) {
         if(rsr != null) {
         System.out.println(rsr);
         }
         }
         } 
         }
         System.out.println(dataSet.getExamMetrics().getRSR().score);
         System.out.println();
		
         //Display ASR results
         System.out.println("ASR:");
         for(int task = 0; task <= 4; task++) {
         taskMetrics = dataSet.getExamMetrics().getTasks().get(task);
         System.out.println(taskMetrics.getASR_avg());
         if(taskMetrics.getASR_stage_avg() != null && taskMetrics.getASR_stage_avg().size() > 1) {
         for(Double asr : taskMetrics.getASR_stage_avg()) {
         if(asr != null) {
         System.out.println(asr);
         }
         }
         } 
         }
         System.out.println(dataSet.getExamMetrics().getASR().score);
         System.out.println();
		
         //Display RMR results
         System.out.println("RMR:");
         List<TrialData> task6trialData = dataSet.getExamMetrics().getTasks().get(5).getTrials();
         for(TrialData trial : task6trialData) {
         System.out.println(trial.getMetrics().getRMR().score);
         }
         System.out.println(dataSet.getExamMetrics().getRMR().score);
         System.out.println();
         //END DEBUG CODE */
    }
}
