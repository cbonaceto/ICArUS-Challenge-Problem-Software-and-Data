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
package org.mitre.icarus.cps.exam.base.playback;

import java.util.LinkedList;
import java.util.List;	

import org.mitre.icarus.cps.assessment.assessment_processor.IAssessmentProcessor;
import org.mitre.icarus.cps.assessment.data_model.base.AbstractExamData;
import org.mitre.icarus.cps.assessment.data_model.base.DataType;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.AbstractExamMetrics;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.AbstractMetricsInfo;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.AbstractSubjectMetrics;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.AbstractTaskMetrics;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.AbstractTrialData;
import org.mitre.icarus.cps.assessment.data_model.base.data_sets.AverageHumanDataSet;
import org.mitre.icarus.cps.assessment.data_model.base.data_sets.SingleParticipantDataSet;
import org.mitre.icarus.cps.exam.base.IcarusExam;
import org.mitre.icarus.cps.exam.base.feedback.TestTrialFeedback;
import org.mitre.icarus.cps.exam.base.response.ResponseGeneratorData;
import org.mitre.icarus.cps.exam.base.testing.IcarusTestPhase;
import org.mitre.icarus.cps.exam.base.testing.IcarusTestTrial;
import org.mitre.icarus.cps.experiment_core.Experiment;

/**
 * @author CBONACETO
 *
 * @param <ED>
 * @param <AHDS>
 * @param <SPDS>
 * @param <TPPD>
 * @param <TRIAL>
 * @param <P>
 * @param <E>
 * @param <D>
 * @param <T>
 * @param <M>
 * @param <S>
 * @param <I>
 */
public abstract class ExamPlaybackDataSource<
	ED extends AbstractExamData<AHDS, D, T, S, I>,
	AHDS extends AverageHumanDataSet<D, T, S, I>,
	SPDS extends SingleParticipantDataSet<D, T, M, S>,
	TPRD extends TestPhaseResponseData<?, D, T, I>,
	AP extends IAssessmentProcessor<SPDS, AHDS, R, P, E, F, D, T, M, S, I>,
	R extends IcarusTestTrial,
	F extends TestTrialFeedback<R>,
	P extends IcarusTestPhase<?>,
	E extends IcarusExam<?>,
	D extends AbstractTrialData<?>, 
	T extends AbstractTaskMetrics<D>,
	M extends AbstractExamMetrics<T>,
	S extends AbstractSubjectMetrics,
	I extends AbstractMetricsInfo> extends Experiment<TPRD> {
	
	/** The exam the response is for */
	protected E exam;
	
	/** Exam data describing the exam. Also contains the average human data. */
	protected ED examData;
	
	/** The participant (human or model) response and assessment data for the exam */
	protected SPDS participantResponse;		
	
	/** Response data and metrics for each test phase extracted from the participant response data */
	protected List<TPRD> testPhaseResponses;		
	
	/** Used to calculate metrics */
	protected AP assessmentProcessor;
	
	/** Listeners registered to listen for changes in exam response data */
	protected transient List<ExamPlaybackChangeListener> listeners;
	
	public ExamPlaybackDataSource() {
		//assessmentProcessor = new AssessmentProcessor();
		assessmentProcessor = createAssessmentProcessor();
		listeners = new LinkedList<ExamPlaybackChangeListener>();
	}
	
	protected abstract AP createAssessmentProcessor();
	
	/**
	 * @param listener
	 */
	public void addExamPlaybackChangeListener(ExamPlaybackChangeListener listener) {
		listeners.add(listener);
	}
	
	/**
	 * @param listener
	 */
	public void removeExamPlaybackChangeListener(ExamPlaybackChangeListener listener) {
		listeners.remove(listener);
	}
	
	/**
	 * @return
	 */
	public E getExam() {
		return exam;
	}	
	
	/**
	 * @return
	 */
	public ED getExamData() {
		return examData;
	}
	
	/**
	 * @return
	 */
	public DataType getParticipantDataType() {
		return participantResponse != null ? participantResponse.getData_type() : null;
	}
	
	/**
	 * @return
	 */
	public M getParticipantExamMetrics() {
		return participantResponse != null ? participantResponse.getExamMetrics() : null;
	}
	
	/**
	 * @return
	 */
	public S getParticipantSubjectMetrics() {
		return participantResponse != null ? participantResponse.getSubjectMetrics() : null;
	}
	
	/**
	 * @return
	 */
	public List<T> getAvgHumanTaskMetrics() {
		return examData != null && examData.getAvgHumanDataSet() != null ?
				examData.getAvgHumanDataSet().getTaskMetrics() : null;
	}	
	
	/**
	 * @return
	 */
	public S getAvgHumanSubjectMetrics() {
		return examData != null && examData.getAvgHumanDataSet() != null ?
				examData.getAvgHumanDataSet().getSubjectMetrics() : null;
	}
	
	/**
	 * @return
	 */
	public List<TPRD> getTestPhaseResponses() {
		return testPhaseResponses;
	}
	
	/**
	 * 
	 */
	public ResponseGeneratorData getResponseGeneratorData() {
		String subjectId = "?";
		String siteId = "?";
		Boolean humanSubject = null;
		M metrics = getParticipantExamMetrics();
		if(metrics != null) {			
			subjectId = metrics.getResponse_generator_id() != null ? metrics.getResponse_generator_id() : subjectId;
			siteId = metrics.getSite_id() != null ? metrics.getSite_id() : siteId;
			if(metrics.getData_type() != null) {
				humanSubject = (metrics.getData_type() == DataType.Human_Avg || 
						metrics.getData_type() == DataType.Human_Multiple || 
						metrics.getData_type() == DataType.Human_Single);
			}
		}
		return new ResponseGeneratorData(subjectId, siteId, humanSubject);
	}

	/**
	 * @param taskId
	 * @return
	 */
	public TPRD getTestPhaseResponse(String testPhaseId) {
		if(testPhaseResponses != null && !testPhaseResponses.isEmpty()) {
			for(TPRD testPhaseResponse : testPhaseResponses) {
				if(testPhaseResponse.getTestPhase() != null) {
					if(testPhaseId.equals(testPhaseResponse.getTestPhase().getId())) {
						return testPhaseResponse;
					}
				} else if(testPhaseResponse.getParticipantTaskMetrics() != null && 
						testPhaseId.equals(testPhaseResponse.getParticipantTaskMetrics().getTask_id())) {
					return testPhaseResponse;
				}
			}
		}
		return null;
	}
	
	/**
	 * @param taskId
	 * @return
	 */
	public int getTestPhaseIndex(String testPhaseId) {
		if(testPhaseResponses != null && !testPhaseResponses.isEmpty()) {
			int index = 0;
			for(TPRD testPhaseResponse : testPhaseResponses) {
				if(testPhaseResponse.getTestPhase() != null) {
					if(testPhaseId.equals(testPhaseResponse.getTestPhase().getId())) {
						return index;
					}
				} else if(testPhaseResponse.getParticipantTaskMetrics() != null && 
						testPhaseId.equals(testPhaseResponse.getParticipantTaskMetrics().getTask_id())) {
					return index;
				} 
				index++;
			}
		}
		return -1;
	}
		
	/**
	 * @param exam
	 * @param participantExamResponse
	 * @param avgHumanResponse
	 * @param metricsInfo
	 */
	public void initializeExamResponseData(E exam, E participantExamResponse, AHDS avgHumanResponse, I metricsInfo) {
		ED examData = createExamData(exam, metricsInfo);
		SPDS participantResponse = null;
		if(exam != null) {						
			//Extract trial data and compute trial, task, and exam metrics
			participantResponse = assessmentProcessor.buildSingleParticipantDataSet(exam, 
					participantExamResponse, examData.getTask_ids(), examData.getTask_numbers(), metricsInfo, 
					participantExamResponse != null ? participantExamResponse.getResponseGenerator() : null, 
					true, avgHumanResponse);
		} 
		initializeExamResponseData(exam, examData, participantResponse, avgHumanResponse);
	}
	
	/**
	 * @param exam
	 * @param participantResponse
	 * @param avgHumanResponse
	 * @param metricsInfo
	 */
	public void initializeExamResponseData(E exam, SPDS participantResponse, AHDS avgHumanResponse, I metricsInfo) {
		initializeExamResponseData(exam, createExamData(exam, metricsInfo), participantResponse, avgHumanResponse);
	}
	
	protected void initializeExamResponseData(E exam, ED examData, SPDS participantResponse, AHDS avgHumanResponse) {
		this.exam = exam;
		this.examData = examData;
		this.examData.setAvgHumanDataSet(avgHumanResponse);
		this.participantResponse = participantResponse;
		I metricsInfo = examData.getMetricsInfo() != null ? examData.getMetricsInfo() : createDefaultMetricsInfo();
		testPhaseResponses = extractTestPhaseResponses(exam, examData, avgHumanResponse, metricsInfo);
		
		//Notify listeners that the exam response changed
		fireListenerEvent(true, null, null, null);
	}
	
	/**
	 * @return
	 */
	protected abstract I createDefaultMetricsInfo();
	
	/**
	 * @param exam
	 * @param MetricsInfo
	 * @return
	 */
	protected abstract ED createExamData(E exam, I metricsInfo);
	
	/**
	 * @param exam
	 * @param examData
	 * @param metricsInfo
	 */
	protected abstract List<TPRD> extractTestPhaseResponses(E exam, ED examData, AHDS avgHumanResponse, I metricsInfo);
	
	/**
	 * @param taskResponse
	 * @param trialResponse
	 * @param trialFeedback
	 * @param scoreTrialIfFeedbackMissingOrIncomplete
	 * @param taskId
	 * @param taskNum
	 * @param trialNum
	 */
	public void updateTrialResponse(P testPhaseResponse, R trialResponse, F trialFeedback,
			boolean scoreTrialIfFeedbackMissingOrIncomplete, String taskId, Integer taskNum, Integer trialNum) {
		//Update trial, task, and exam metrics		
		assessmentProcessor.processTrialResponse(exam, testPhaseResponse, trialResponse, trialFeedback, 
				scoreTrialIfFeedbackMissingOrIncomplete, taskId, taskNum, trialNum, 
			examData != null ? examData.getMetricsInfo() : null, participantResponse, 
					examData != null ? examData.getAvgHumanDataSet() : null);
		
		//Notify listeners that a trial changed
		fireListenerEvent(false, taskId, taskNum, trialNum);
	}	
	
	/**
	 * @param examResponseChanged
	 * @param taskId
	 * @param taskNum
	 * @param trialNum
	 */
	protected void fireListenerEvent(boolean examResponseChanged, String taskId, Integer taskNum, Integer trialNum) {
		if(!listeners.isEmpty()) {
			ExamPlaybackChangeListener[] immutableListeners = new ExamPlaybackChangeListener[listeners.size()]; 
			immutableListeners = listeners.toArray(immutableListeners);
			for(ExamPlaybackChangeListener listener : immutableListeners) {
				if(examResponseChanged) {
					listener.examResponseChanged();
				} else {
					listener.trialResponseChanged(taskId, taskNum, trialNum);
				}
			}
		}
	}

	@Override
	public List<TPRD> getConditions() {
		return testPhaseResponses;
	}	
}