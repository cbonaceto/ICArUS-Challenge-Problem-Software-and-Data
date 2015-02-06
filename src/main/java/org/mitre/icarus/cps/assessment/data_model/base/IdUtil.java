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
package org.mitre.icarus.cps.assessment.data_model.base;

import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.AbstractTaskMetrics;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.AbstractTrialData;

/**
 * Generates hash ids for entities to represent primary keys.
 * @author LWONG
 */
public class IdUtil {
	/*
	 * TrialData
	 * 
	 * 		String exam_id
	 * 		String phase_id
	 * 		Integer trial_number
	 * 		String site_id
	 * 		String response_generator
	 */
	public static int calculateTrialDataId(AbstractTrialData<?> trialData) {
		int code = 17;
		code = 31 * code + trialData.getExam_id().hashCode();
		code = 37 * code + trialData.getTask_id().hashCode();
		code = 31 * code + trialData.getTrial_number().hashCode();
		code = 37 * code + trialData.getSite_id().hashCode();
		code = 31 * code + trialData.getResponse_generator_id().hashCode();
		return code;
	}
	public static int calculateTrialDataId(String site_id, String response_generator_id, 
			String exam_id, String task_id, Integer trial_number) {
		int code = 17;
		code = 31 * code + exam_id.hashCode();
		code = 37 * code + task_id.hashCode();
		code = 31 * code + trial_number.hashCode();
		code = 37 * code + site_id.hashCode();
		code = 31 * code + response_generator_id.hashCode();
		return code;
	}
	
	/*
	 * TaskMetrics
	 * 
	 * 		String exam_id
	 *		String phase_id
	 *		String site_id
	 *		Boolean isHuman
	 */
	public static int calculateTaskMetricsId(AbstractTaskMetrics<?> taskMetrics) {
		int code = 17; 
		code = 31 * code + taskMetrics.getExam_id().hashCode();
		code = 37 * code + taskMetrics.getTask_id().hashCode();
		code = 31 * code + taskMetrics.getSite_id().hashCode();
		code = 37 * code + taskMetrics.isHuman().hashCode();
		return code;
	}
	public static int calculateTaskMetricsId(String exam_id, String phase_id, 
			String site_id, Boolean isHuman) {
		int code = 17;
		code = 31 * code + exam_id.hashCode();
		code = 37 * code + phase_id.hashCode();
		code = 31 * code + site_id.hashCode();
		code = 37 * code + isHuman.hashCode();
		return code;
	}
}
