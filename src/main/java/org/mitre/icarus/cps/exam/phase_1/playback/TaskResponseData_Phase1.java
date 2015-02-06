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

import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.MetricsInfo;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.TaskMetrics;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.TrialData;
import org.mitre.icarus.cps.exam.base.playback.TestPhaseResponseData;
import org.mitre.icarus.cps.exam.phase_1.testing.TaskTestPhase;

/**
 * 
 * @author CBONACETO
 *
 */
//P, D, T, I
public class TaskResponseData_Phase1<T extends TaskTestPhase<?>> extends 
	TestPhaseResponseData<T, TrialData, TaskMetrics, MetricsInfo> {	
	
	public TaskResponseData_Phase1(T task, TaskMetrics participantTaskMetrics, 
			TaskMetrics avgHumanTaskMetrics, MetricsInfo metricsInfo) {
		super(task, participantTaskMetrics, avgHumanTaskMetrics, metricsInfo);
	}
}