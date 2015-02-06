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

import java.util.List;

import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.MetricsInfo;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.TaskMetrics;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.TrialData;
import org.mitre.icarus.cps.exam.phase_1.testing.IcarusTestTrial_Phase1;
import org.mitre.icarus.cps.exam.phase_1.testing.TaskTestPhase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_1_2_3_PhaseBase;
import org.mitre.icarus.cps.experiment_core.condition.Condition;

/**
 * @author CBONACETO
 *
 */
public class TaskResponseData_Old<T extends TaskTestPhase<?>> extends Condition {
	
	/** The task that the responses are for */
	protected final T task;
	
	/** The participant (human or model) metrics for the task */
	protected final TaskMetrics participantTaskMetrics;	
	
	/** The average human metrics for the task */
	protected final TaskMetrics avgHumanTaskMetrics;
	
	/** The metrics info for the exam the task is contained in */
	protected final MetricsInfo metricsInfo;
	
	public TaskResponseData_Old(T task, TaskMetrics participantTaskMetrics, TaskMetrics avgHumanTaskMetrics, MetricsInfo metricsInfo) {
		this.task = task;
		this.participantTaskMetrics = participantTaskMetrics;
		this.avgHumanTaskMetrics = avgHumanTaskMetrics;
		this.metricsInfo = metricsInfo;
	}

	public T getTask() {
		return task;
	}
	
	public TaskMetrics getParticipantTaskMetrics() {
		return participantTaskMetrics;
	}

	public TaskMetrics getAvgHumanTaskMetrics() {
		return avgHumanTaskMetrics;
	}

	public MetricsInfo getMetricsInfo() {
		return metricsInfo;
	}

	public IcarusTestTrial_Phase1 getTrial(Integer trialIndex) {
		if(task != null && task.getTestTrials() != null && trialIndex < task.getTestTrials().size()) {
			return task.getTestTrials().get(trialIndex);
		}
		return null;
	}
	
	public List<? extends IcarusTestTrial_Phase1> getTestTrials() {
		if(task != null) {
			if(task instanceof Task_1_2_3_PhaseBase) {
				Task_1_2_3_PhaseBase<?> task123 = (Task_1_2_3_PhaseBase<?>)task;
				return task123.getProbeTrials();
			} else {
				return task.getTestTrials();
			}
		}
		return null;
	}
	
	public TrialData getParticipantTrialData(Integer trialIndex) {
		if(participantTaskMetrics != null && participantTaskMetrics.getTrials() != null && trialIndex < participantTaskMetrics.getTrials().size()) {
			return participantTaskMetrics.getTrials().get(trialIndex);
		}
		return null;
	}
	
	public TrialData getAvgHumanTrialData(Integer trialIndex) {
		if(avgHumanTaskMetrics != null && avgHumanTaskMetrics.getTrials() != null && trialIndex < avgHumanTaskMetrics.getTrials().size()) {
			return avgHumanTaskMetrics.getTrials().get(trialIndex);
		}
		return null;
	}

	@Override
	public String getName() {
		if(task != null) {
			return task.getName();
		}
		return null;
	}

	@Override
	public void setName(String name) {
		super.setName(name);
		if(task != null) {
			task.setName(name);
		}
	}

	@Override
	public void setConditionNum(Integer conditionNum) {
		super.setConditionNum(conditionNum);
		if(task != null) {
			task.setConditionNum(conditionNum);
		}
	}

	@Override
	public int getNumTrials() {
		if(task != null) {
			/*if(task instanceof Task_1_2_3_PhaseBase) {
				Task_1_2_3_PhaseBase task123 = (Task_1_2_3_PhaseBase)task;
				return task123.getTrialBlocks() != null ? task123.getTrialBlocks().size() : 0;
			} else {*/
			return task.getNumTrials();
			//}
		}
		return 0;
	}
}