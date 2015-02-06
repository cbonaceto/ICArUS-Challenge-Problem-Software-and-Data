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
package org.mitre.icarus.cps.exam.phase_1.playback;

import java.util.ArrayList;
import java.util.List;

import org.mitre.icarus.cps.assessment.assessment_processor.phase_1.AssessmentProcessor;
import org.mitre.icarus.cps.assessment.data_model.base.data_sets.SingleParticipantDataSet;
import org.mitre.icarus.cps.assessment.data_model.phase_1.ExamData;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.ExamMetrics;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.MetricsInfo;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.SubjectMetrics;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.TaskMetrics;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.TrialData;
import org.mitre.icarus.cps.assessment.data_model.phase_1.data_sets.AverageHumanDataSet_Phase1;
import org.mitre.icarus.cps.exam.base.playback.ExamPlaybackDataSource;
import org.mitre.icarus.cps.exam.phase_1.IcarusExam_Phase1;
import org.mitre.icarus.cps.exam.phase_1.feedback.TrialFeedback_Phase1;
import org.mitre.icarus.cps.exam.phase_1.testing.IcarusTestTrial_Phase1;
import org.mitre.icarus.cps.exam.phase_1.testing.TaskTestPhase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_1_Phase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_2_Phase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_3_Phase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_4_Phase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_5_Phase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_6_Phase;

/**
 * Coordinates receiving responses in real-time to the trials in the tasks in the exam. Notifies
 * listeners when response data is updated.
 * 
 * @author CBONACETO
 *
 */
public class ExamPlaybackDataSource_Phase1 extends ExamPlaybackDataSource<
	ExamData, 
	AverageHumanDataSet_Phase1, 
	SingleParticipantDataSet<TrialData, TaskMetrics, ExamMetrics, SubjectMetrics>,
	TaskResponseData_Phase1<?>,
	AssessmentProcessor,
	IcarusTestTrial_Phase1,
	TrialFeedback_Phase1,
	TaskTestPhase<?>, 
	IcarusExam_Phase1, 
	TrialData, 
	TaskMetrics, 
	ExamMetrics, 
	SubjectMetrics, 
	MetricsInfo> {		
	
	@Override
	protected AssessmentProcessor createAssessmentProcessor() {
		return new AssessmentProcessor();
	}

	@Override
	protected MetricsInfo createDefaultMetricsInfo() {
		return MetricsInfo.createDefaultMetricsInfo();
	}

	@Override
	protected ExamData createExamData(IcarusExam_Phase1 exam, MetricsInfo metricsInfo) {
		return new ExamData(exam, metricsInfo);
	}

	@Override
	protected List<TaskResponseData_Phase1<?>> extractTestPhaseResponses(IcarusExam_Phase1 exam, ExamData examData, 
			AverageHumanDataSet_Phase1 avgHumanResponse, MetricsInfo metricsInfo) {		
		List<TaskTestPhase<?>> examTasks = null;
		if(exam != null) {
			examTasks = exam.getTasks();
		}
		
		List<TaskResponseData_Phase1<?>> taskResponses = null;
		if(examTasks != null && !examTasks.isEmpty()) {
			taskResponses = new ArrayList<TaskResponseData_Phase1<?>>(examTasks.size());
			List<TaskMetrics> participantTasks = null;
			List<TaskMetrics> avgHumanTasks = null;
			if(participantResponse != null && participantResponse.getExamMetrics() != null) {
				participantTasks = participantResponse.getExamMetrics().getTasks();
			}
			if(avgHumanResponse != null && avgHumanResponse.getTaskMetrics() != null && !avgHumanResponse.getTaskMetrics().isEmpty()) {
				avgHumanTasks = avgHumanResponse.getTaskMetrics();
			}
			for(int i=0; i<examTasks.size(); i++) {
				TaskTestPhase<?> examTask = examTasks.get(i);
				TaskMetrics participantTask = participantTasks != null && i < participantTasks.size() ? participantTasks.get(i) : null;
				TaskMetrics avgHumanTask = avgHumanTasks != null && i < avgHumanTasks.size() ? avgHumanTasks.get(i) : null;
				if(examTask instanceof Task_1_Phase) {
					examTask.setConditionNum(1);
					taskResponses.add(new TaskResponseData_Phase1<Task_1_Phase>((Task_1_Phase)examTask, participantTask, avgHumanTask, metricsInfo));
				} else if(examTask instanceof Task_2_Phase) {
					examTask.setConditionNum(2);
					taskResponses.add(new TaskResponseData_Phase1<Task_2_Phase>((Task_2_Phase)examTask, participantTask, avgHumanTask, metricsInfo));
				} else if(examTask instanceof Task_3_Phase) {
					examTask.setConditionNum(3);
					taskResponses.add(new TaskResponseData_Phase1<Task_3_Phase>((Task_3_Phase)examTask, participantTask, avgHumanTask, metricsInfo));
				} else if(examTask instanceof Task_4_Phase) {
					examTask.setConditionNum(4);
					taskResponses.add(new TaskResponseData_Phase1<Task_4_Phase>((Task_4_Phase)examTask, participantTask, avgHumanTask, metricsInfo));
				} else if(examTask instanceof Task_6_Phase) {
					examTask.setConditionNum(6);
					taskResponses.add(new TaskResponseData_Phase1<Task_6_Phase>((Task_6_Phase)examTask, participantTask, avgHumanTask, metricsInfo));
				} else if(examTask instanceof Task_5_Phase) {
					examTask.setConditionNum(5);
					taskResponses.add(new TaskResponseData_Phase1<Task_5_Phase>((Task_5_Phase)examTask, participantTask, avgHumanTask, metricsInfo));
				}
			}
		}		
		return taskResponses;
	}
	
	/**
	 * @param exam
	 * @param participantExamResponse
	 * @param avgHumanResponse
	 * @param metricsInfo
	 */
	/*public void initializeExamResponseData(IcarusExam_Phase1 exam, IcarusExam_Phase1 participantExamResponse, 
			AverageHumanDataSet_Phase1 avgHumanResponse, MetricsInfo metricsInfo) {
		ExamData examData = new ExamData(exam, metricsInfo);
		SingleParticipantDataSet<TrialData, TaskMetrics, ExamMetrics, SubjectMetrics> participantResponse = null;
		if(exam != null) {						
			//Extract trial data and compute trial, task, and exam metrics
			participantResponse = assessmentProcessor.buildSingleParticipantDataSet(exam, 
					participantExamResponse, examData.getTask_ids(), examData.getTask_numbers(), metricsInfo, 
					participantExamResponse != null ? participantExamResponse.getResponseGenerator() : null, 
					true, avgHumanResponse);
		} 
		initializeExamResponseData(exam, examData, participantResponse, avgHumanResponse);
	}*/


	/**
	 * @param exam
	 * @param participantResponse
	 * @param avgHumanResponse
	 * @param metricsInfo
	 */
	/*public void initializeExamResponseData(IcarusExam_Phase1 exam, 
			SingleParticipantDataSet<TrialData, TaskMetrics, ExamMetrics, SubjectMetrics> participantResponse, 
			AverageHumanDataSet_Phase1 avgHumanResponse, MetricsInfo metricsInfo) {
		initializeExamResponseData(exam, new ExamData(exam, metricsInfo), participantResponse, avgHumanResponse);
	}*/
	/*
	protected void initializeExamResponseData(IcarusExam_Phase1 exam, ExamData examData, 
			SingleParticipantDataSet<TrialData, TaskMetrics, ExamMetrics, SubjectMetrics> participantResponse, 
			AverageHumanDataSet_Phase1 avgHumanResponse) {
		this.exam = exam;
		this.examData = examData;
		this.examData.setAvgHumanDataSet(avgHumanResponse);
		this.participantResponse = participantResponse;		
		//examResponseData.setAvgHumanResponse(avgHumanResponse);
		List<TaskTestPhase<?>> examTasks = null;
		if(exam != null) {
			examTasks = exam.getTasks();
		}
		
		taskResponses = null;
		if(examTasks != null && !examTasks.isEmpty()) {
			MetricsInfo metricsInfo = examData.getMetricsInfo() != null ? examData.getMetricsInfo() : MetricsInfo.createDefaultMetricsInfo();			
			taskResponses = new ArrayList<TaskResponse<? extends TaskTestPhase<?>>>(examTasks.size());
			List<TaskMetrics> participantTasks = null;
			List<TaskMetrics> avgHumanTasks = null;
			if(participantResponse != null && participantResponse.getExamMetrics() != null) {
				participantTasks = participantResponse.getExamMetrics().getTasks();
			}
			if(avgHumanResponse != null && avgHumanResponse.getTaskMetrics() != null && !avgHumanResponse.getTaskMetrics().isEmpty()) {
				avgHumanTasks = avgHumanResponse.getTaskMetrics();
			}
			for(int i=0; i<examTasks.size(); i++) {
				TaskTestPhase<?> examTask = examTasks.get(i);
				TaskMetrics participantTask = participantTasks != null && i < participantTasks.size() ? participantTasks.get(i) : null;
				TaskMetrics avgHumanTask = avgHumanTasks != null && i < avgHumanTasks.size() ? avgHumanTasks.get(i) : null;
				if(examTask instanceof Task_1_Phase) {
					examTask.setConditionNum(1);
					taskResponses.add(new TaskResponse<Task_1_Phase>((Task_1_Phase)examTask, participantTask, avgHumanTask, metricsInfo));
				} else if(examTask instanceof Task_2_Phase) {
					examTask.setConditionNum(2);
					taskResponses.add(new TaskResponse<Task_2_Phase>((Task_2_Phase)examTask, participantTask, avgHumanTask, metricsInfo));
				} else if(examTask instanceof Task_3_Phase) {
					examTask.setConditionNum(3);
					taskResponses.add(new TaskResponse<Task_3_Phase>((Task_3_Phase)examTask, participantTask, avgHumanTask, metricsInfo));
				} else if(examTask instanceof Task_4_Phase) {
					examTask.setConditionNum(4);
					taskResponses.add(new TaskResponse<Task_4_Phase>((Task_4_Phase)examTask, participantTask, avgHumanTask, metricsInfo));
				} else if(examTask instanceof Task_6_Phase) {
					examTask.setConditionNum(6);
					taskResponses.add(new TaskResponse<Task_6_Phase>((Task_6_Phase)examTask, participantTask, avgHumanTask, metricsInfo));
				} else if(examTask instanceof Task_5_Phase) {
					examTask.setConditionNum(5);
					taskResponses.add(new TaskResponse<Task_5_Phase>((Task_5_Phase)examTask, participantTask, avgHumanTask, metricsInfo));
				}
			}
		}
		
		//Notify listeners that the exam response changed
		fireListenerEvent(true, null, null, null);
	}*/	
	
	/**
	 * @param taskResponse
	 * @param trialResponse
	 * @param trialFeedback
	 * @param scoreTrialIfFeedbackMissingOrIncomplete
	 * @param taskId
	 * @param taskNum
	 * @param trialNum
	 */
	/*public void updateTrialResponse(TaskTestPhase<?> taskResponse, IcarusTestTrial_Phase1 trialResponse, TrialFeedback_Phase1 trialFeedback,
			boolean scoreTrialIfFeedbackMissingOrIncomplete, String taskId, Integer taskNum, Integer trialNum) {
		//Update trial, task, and exam metrics		
		assessmentProcessor.processTrialResponse(exam, taskResponse, trialResponse, trialFeedback, 
				scoreTrialIfFeedbackMissingOrIncomplete, taskId, taskNum, trialNum, 
			examData != null ? examData.getMetricsInfo() : null, participantResponse, 
					examData != null ? examData.getAvgHumanDataSet() : null);
		
		//Notify listeners that a trial changed
		fireListenerEvent(false, taskId, taskNum, trialNum);
	}*/
}