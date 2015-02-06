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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.mitre.icarus.cps.assessment.assessment_metrics_computer.phase_1.ExamMetricsComputer;
import org.mitre.icarus.cps.assessment.assessment_metrics_computer.phase_1.SubjectMetricsComputer;
import org.mitre.icarus.cps.assessment.assessment_metrics_computer.phase_1.TaskMetricsComputer;
import org.mitre.icarus.cps.assessment.assessment_processor.IAssessmentProcessor;
import org.mitre.icarus.cps.assessment.data_model.base.DataType;
import org.mitre.icarus.cps.assessment.data_model.base.data_sets.AllHumansDataSet;
import org.mitre.icarus.cps.assessment.data_model.base.data_sets.AverageHumanDataSet;
import org.mitre.icarus.cps.assessment.data_model.base.data_sets.SingleParticipantDataSet;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.ExamMetrics;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.MetricsInfo;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.SubjectMetrics;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.TaskMetrics;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.TrialData;
import org.mitre.icarus.cps.assessment.data_model.phase_1.data_sets.AverageHumanDataSet_Phase1;
import org.mitre.icarus.cps.assessment.data_model.phase_1.data_sets.SingleHumanDataSet_Phase1;
import org.mitre.icarus.cps.assessment.data_model.phase_1.data_sets.SingleModelDataSet_Phase1;
import org.mitre.icarus.cps.assessment.score_computer.phase_1.BatchScoreComputer;
import org.mitre.icarus.cps.assessment.score_computer.phase_1.ScoreComputer;
import org.mitre.icarus.cps.exam.base.response.ResponseGeneratorData;
import org.mitre.icarus.cps.exam.phase_1.IcarusExam_Phase1;
import org.mitre.icarus.cps.exam.phase_1.feedback.TrialFeedback_Phase1;
import org.mitre.icarus.cps.exam.phase_1.loader.IcarusExamLoader_Phase1;
import org.mitre.icarus.cps.exam.phase_1.testing.AttackLocationPresentationTrial;
import org.mitre.icarus.cps.exam.phase_1.testing.IcarusTestTrial_Phase1;
import org.mitre.icarus.cps.exam.phase_1.testing.TaskTestPhase;

/**
 * Uses: (1) Process human data set, compute trial and task metrics for human
 * data set (2) Process model run/model data set, compute trial, task, and
 * overall performance metrics
 *
 * @author CBONACETO
 *
 */
public class AssessmentProcessor implements IAssessmentProcessor<
	SingleParticipantDataSet<TrialData, TaskMetrics, ExamMetrics, SubjectMetrics>, AverageHumanDataSet_Phase1, IcarusTestTrial_Phase1, TaskTestPhase<?>, IcarusExam_Phase1, TrialFeedback_Phase1, TrialData, TaskMetrics, ExamMetrics, SubjectMetrics, MetricsInfo> {

    public static enum DependentMeasureUpdateStrategy {

        Do_Nothing, Flag_As_Stale, Add_To_Batch, Perform_Update
    };

    protected TrialResponseProcessor trialResponseProcessor;

    protected SubjectMetricsComputer subjectMetricsComputer;

    protected TaskMetricsComputer taskMetricsComputer;

    protected ExamMetricsComputer examMetricsComputer;

    protected ScoreComputer scoreComputer;

    public static boolean DEBUG = false;

    public AssessmentProcessor() {
        trialResponseProcessor = new TrialResponseProcessor();
        subjectMetricsComputer = new SubjectMetricsComputer();
        taskMetricsComputer = new TaskMetricsComputer();
        examMetricsComputer = new ExamMetricsComputer();
        scoreComputer = new ScoreComputer();
    }

    /**
     * @param exam
     * @param examResponse
     * @param taskIds
     * @param taskNumbers
     * @param metricsInfo
     * @param responseGenerator
     * @param computeTaskAndExamMetricsForHumanSubject
     * @param comparisonData
     * @return
     */
    @Override
    public SingleParticipantDataSet<TrialData, TaskMetrics, ExamMetrics, SubjectMetrics> buildSingleParticipantDataSet(
            IcarusExam_Phase1 exam, IcarusExam_Phase1 examResponse,
            List<String> taskIds, List<Integer> taskNumbers, MetricsInfo metricsInfo,
            ResponseGeneratorData responseGenerator,
            boolean computeTaskAndExamMetricsForHumanSubject,
            AverageHumanDataSet_Phase1 comparisonData) {
        if (exam == null) {
            return null;
        }

        boolean humanSubject = responseGenerator != null ? responseGenerator.isHumanSubject() : false;
        DataType dataType = humanSubject ? DataType.Human_Single : DataType.Model_Single;
        String examId = exam.getId();

        boolean responsePresent = false;
        if (examResponse != null) {
            responsePresent = true;
            //Score the exam to compute feedback and normative solutions
            for (TaskTestPhase<?> task : examResponse.getTasks()) {
                try {
                    TaskTestPhase<?> scoredTask = BatchScoreComputer.addSubjectResponsesToExamTask(exam, task);
                    scoreComputer.computeSolutionAndScoreForTask(scoredTask, exam.getProbabilityRules(), exam.getGridSize(), true, null);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            exam.setResponseGenerator(responseGenerator);
        }
        examResponse = exam;
		//DEBUG CODE
        //try {
        //	System.out.println(IcarusExamLoader_Phase1.marshalExam(examResponse));
        //} catch(Exception ex) {}

        metricsInfo = (metricsInfo == null) ? MetricsInfo.createDefaultMetricsInfo() : metricsInfo;

        //Task and trial metrics for each task
        List<TaskMetrics> taskMetrics = new ArrayList<TaskMetrics>(taskIds.size());

        //Trial Data for each Task 6 trial (Task for which subject metrics are computed, Subject->Trials)
        List<TrialData> task6TrialData = null;

        //Overall subject metrics (Computed using only Task 6 data)
        SubjectMetrics subjectMetrics = null;

        //Process response data for each task if present. Otherwise just create initial trial data objects.
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
                currTaskMetrics.setData_type(dataType);
                currTaskMetrics.setHuman(humanSubject);
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

                //Compute trial metrics for each trial or just create initial trial data if the responses aren't present
                if (DEBUG) {
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
                        if (responsePresent) {
                            trialResponseProcessor.updateTrialMetrics(currTrialData, metricsInfo,
                                    comparisonTaskMetrics != null && comparisonTaskMetrics.getTrials() != null
                                    && trialIndex < comparisonTaskMetrics.getTrials().size() ? comparisonTaskMetrics.getTrials().get(trialIndex) : null);
                        }
                        currTrialData.setData_type(dataType);
                        currTaskTrialData.add(currTrialData);
                        if (taskNum == 6) {
                            task6TrialData.add(currTrialData);
                        }
                        trialIndex++;
                    }
                }

                //Compute the task metrics for the current task
                taskMetricsComputer.updateCompletionStatus(currTaskMetrics, currTaskTrialData);
                if (currTaskMetrics.isTask_complete() && (!humanSubject || computeTaskAndExamMetricsForHumanSubject)) {
                    Double taskTime = (taskResponse.getStartTime() != null && taskResponse.getEndTime() != null)
                            ? new Double(taskResponse.getEndTime().getTime() - taskResponse.getStartTime().getTime()) : null;
                    taskMetricsComputer.updateTaskMetrics(currTaskMetrics, taskTime, currTaskTrialData, metricsInfo,
                            comparisonTaskMetrics, taskNum);
                } else {
                    //System.err.println("Warning, task not complete: " + taskId);
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
            subjectMetrics.setData_type(dataType);
            subjectMetrics.setHuman(humanSubject);
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
        if (!humanSubject || computeTaskAndExamMetricsForHumanSubject) {
            examMetricsComputer.updateAllMetrics(examMetrics, taskMetrics, subjectMetrics, metricsInfo);
        }

        if (DEBUG) {
            System.out.println("Complete");
        }

        //Create and return either a SingleModelDataSet instance or a SingleHumanDataSet instance
        SingleParticipantDataSet<TrialData, TaskMetrics, ExamMetrics, SubjectMetrics> dataSet = null;
        if (humanSubject) {
            dataSet = new SingleHumanDataSet_Phase1();
        } else {
            dataSet = new SingleModelDataSet_Phase1();
        }
        dataSet.setExam_id(examId);
        dataSet.setSite_id(examMetrics.getSite_id());
        dataSet.setResponse_generator_id(examMetrics.getResponse_generator_id());
        dataSet.setTime_stamp(System.currentTimeMillis());
        dataSet.setExamMetrics(examMetrics);
        dataSet.setSubjectMetrics(subjectMetrics);
        return dataSet;
    }

    /**
     * @param humanDataSet
     * @return
     */
    @Override
    public AverageHumanDataSet_Phase1 buildAverageHumanDataSet(AllHumansDataSet<TrialData, SubjectMetrics> humanDataSet) {
        //TODO: Implement this
        return null;
    }

    /**
     * @param exam
     * @param task
     * @param trial
     * @param trialFeedback
     * @param scoreTrialIfFeedbackMissingOrIncomplete
     * @param taskId
     * @param taskNum
     * @param trialNum
     * @param metricsInfo
     * @param participantData
     * @param comparisonData
     */
    @Override
    public void processTrialResponse(IcarusExam_Phase1 exam, TaskTestPhase<?> task, IcarusTestTrial_Phase1 trial,
            TrialFeedback_Phase1 trialFeedback,
            boolean scoreTrialIfFeedbackMissingOrIncomplete, String taskId, Integer taskNum, Integer trialNum, MetricsInfo metricsInfo,
            SingleParticipantDataSet<TrialData, TaskMetrics, ExamMetrics, SubjectMetrics> participantData,
            AverageHumanDataSet_Phase1 comparisonData) {
        if (trial != null && participantData != null && participantData.getExamMetrics() != null) {
            TaskMetrics taskMetrics = participantData.getExamMetrics().getTask(taskId);
            if (taskMetrics != null) {
                //Get the existing trial data object (if any) from task metrics
                TrialData trialData = null;
                int trialIndex = 0;
                for (TrialData trialInTask : taskMetrics.getTrials()) {
                    if (trialInTask.getTrial_number() == trialNum) {
                        trialData = trialInTask;
                        break;
                    } else {
                        trialIndex++;
                    }
                }

                //Extract response data into trial data object
                trialData = trialResponseProcessor.updateTrialData(exam, task, trial, trialFeedback,
                        scoreTrialIfFeedbackMissingOrIncomplete, trialData,
                        participantData.getExam_id(), taskId, taskNum,
                        new ResponseGeneratorData(participantData.getSite_id(), participantData.getResponse_generator_id(),
                                participantData.getData_type() != null && participantData.getData_type() == DataType.Human_Single ? true : false));
                taskMetrics.getTrials().set(trialIndex, trialData);

                //Update trial metrics, task metrics, and exam metrics
                Double taskTime = (task != null && task.getStartTime() != null && task.getEndTime() != null)
                        ? new Double(task.getEndTime().getTime() - task.getStartTime().getTime()) : null;
                processTrialResponse(trialData, taskMetrics, taskTime, participantData.getExamMetrics(),
                        participantData.getSubjectMetrics(), metricsInfo, comparisonData);
            }
        }
    }

    /**
     * @param trialData
     * @param taskMetrics
     * @param examMetrics
     * @param subjectMetrics
     * @param metricsInfo
     * @param comparisonData
     */
    protected void processTrialResponse(TrialData trialData, TaskMetrics taskMetrics, Double taskTime, ExamMetrics examMetrics,
            SubjectMetrics subjectMetrics, MetricsInfo metricsInfo,
            AverageHumanDataSet<TrialData, TaskMetrics, SubjectMetrics, MetricsInfo> comparisonData) {
        //Update trial metrics
        trialData = trialResponseProcessor.updateTrialMetrics(trialData, metricsInfo,
                comparisonData != null ? comparisonData.getTrial(trialData.getTask_id(), trialData.getTrial_number()) : null);

        if (examMetrics != null && taskMetrics != null && taskMetrics.getTrials() != null && !taskMetrics.getTrials().isEmpty()) {
            //Update task completion status			
            taskMetricsComputer.updateCompletionStatus(taskMetrics, taskMetrics.getTrials());
            //System.out.println("Task complete: " + taskMetrics.isTask_complete());
            if (taskMetrics.isTask_complete() != null && taskMetrics.isTask_complete()) {
                //Update task metrics since task complete or completion status changed
                taskMetricsComputer.updateTaskMetrics(taskMetrics, taskTime, taskMetrics.getTrials(), metricsInfo,
                        comparisonData != null ? comparisonData.getTaskMetrics(trialData.getTask_id()) : null,
                        trialData.getTask_number());

                //Update exam completion status
                examMetricsComputer.updateCompletionStatus(examMetrics, examMetrics.getTasks());

                //Update exam metrics
                examMetricsComputer.updateStaleMetricsBasedOnModifiedTask(examMetrics, examMetrics.getTasks(),
                        subjectMetrics, metricsInfo, taskMetrics);
            }
        }
    }

    public static void main(String[] args) {
        //Test processing a single human response

        //Load task data for a single task for the subject
        String dataFolder = BatchFileProcessor.assessmentFolder + "Final_Exam/Human_Data/All Subjects";
        String subjectId = "S_BED_101";
        Integer task = 2;
        //ArrayList<Integer> tasks = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5)); 
        String examId = "Final-Exam-1";

        TrialResponseProcessor trialProcessor = new TrialResponseProcessor();
        MetricsInfo metricsInfo = MetricsInfo.createDefaultMetricsInfo();
        try {
            //Load the data file for the task
            TaskTestPhase<?> response = null;
            String fileName = dataFolder + "/" + subjectId + "/" + (subjectId + "_" + examId + "_Task" + Integer.toString(task) + ".xml");
            File file = new File(fileName);
            response = IcarusExamLoader_Phase1.unmarshalTask(file.toURI().toURL(), false);

            //Process the first trial in the response and compute metrics
            if (response.getTestTrials() != null && !response.getTestTrials().isEmpty()) {
                IcarusTestTrial_Phase1 trial = response.getTestTrials().get(0);
                TrialData trialData = trialProcessor.updateTrialData(null, response, trial,
                        trial.getTrialResponse().getResponseFeedBack(), false,
                        null, examId, "Task" + Integer.toString(task),
                        task, response.getResponseGenerator());
                trialProcessor.updateTrialMetrics(trialData, metricsInfo, null);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
