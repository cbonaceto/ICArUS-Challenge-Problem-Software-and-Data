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

import java.util.List;

import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.AbstractMetricsInfo;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.AbstractTaskMetrics;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.AbstractTrialData;
import org.mitre.icarus.cps.exam.base.testing.IcarusTestPhase;
import org.mitre.icarus.cps.exam.base.testing.IcarusTestTrial;
import org.mitre.icarus.cps.experiment_core.condition.Condition;

/**
 * 
 * Contains participant response data (human or model) and CFA and CPA metrics for a test phase (task) in an exam.
 * 
 * @author CBONACETO
 *
 * @param <P>
 * @param <D>
 * @param <T>
 * @param <I>
 */
public class TestPhaseResponseData<
	P extends IcarusTestPhase<?>,
	D extends AbstractTrialData<?>, 
	T extends AbstractTaskMetrics<D>, 
	I extends AbstractMetricsInfo> extends Condition {	
	
	/** The test phase (task) containing the participant (human or model) responses to that phase */
	protected final P testPhase;
	
	/** The participant (human or model) metrics for the task */
	protected final T participantTaskMetrics;	
	
	/** The average human metrics for the task */
	protected final T avgHumanTaskMetrics;
	
	/** The metrics info for the exam that the task is contained in */
	protected final I metricsInfo;
	
	public TestPhaseResponseData(P testPhase, T participantTaskMetrics, T avgHumanTaskMetrics, I metricsInfo) {
		this.testPhase = testPhase;
		this.participantTaskMetrics = participantTaskMetrics;
		this.avgHumanTaskMetrics = avgHumanTaskMetrics;
		this.metricsInfo = metricsInfo;
	}

	public P getTestPhase() {
		return testPhase;
	}

	public T getParticipantTaskMetrics() {
		return participantTaskMetrics;
	}

	public T getAvgHumanTaskMetrics() {
		return avgHumanTaskMetrics;
	}

	public I getMetricsInfo() {
		return metricsInfo;
	}
	
	public IcarusTestTrial getTrial(Integer trialIndex) {
		if(testPhase != null && testPhase.getTestTrials() != null && trialIndex < testPhase.getTestTrials().size()) {
			return testPhase.getTestTrials().get(trialIndex);
		}
		return null;
	}
	
	public List<? extends IcarusTestTrial> getTestTrials() {
		if(testPhase != null) {			
			return testPhase.getTestTrials();
		}
		return null;
	}
	
	public D getParticipantTrialData(Integer trialIndex) {
		if(participantTaskMetrics != null && participantTaskMetrics.getTrials() != null && trialIndex < participantTaskMetrics.getTrials().size()) {
			return participantTaskMetrics.getTrials().get(trialIndex);
		}
		return null;
	}
	
	public D getAvgHumanTrialData(Integer trialIndex) {
		if(avgHumanTaskMetrics != null && avgHumanTaskMetrics.getTrials() != null && trialIndex < avgHumanTaskMetrics.getTrials().size()) {
			return avgHumanTaskMetrics.getTrials().get(trialIndex);
		}
		return null;
	}

	@Override
	public String getName() {
		if(testPhase != null) {
			return testPhase.getName();
		}
		return null;
	}

	@Override
	public void setName(String name) {
		super.setName(name);
		if(testPhase != null) {
			testPhase.setName(name);
		}
	}

	@Override
	public void setConditionNum(Integer conditionNum) {
		super.setConditionNum(conditionNum);
		if(testPhase != null) {
			testPhase.setConditionNum(conditionNum);
		}
	}

	@Override
	public int getNumTrials() {
		if(testPhase != null) {			
			return testPhase.getNumTrials();
		}
		return 0;
	}
}