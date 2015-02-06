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
package org.mitre.icarus.cps.test_harness;

import org.mitre.icarus.cps.exam.base.IcarusExam;
import org.mitre.icarus.cps.exam.base.feedback.TestTrialFeedback;
import org.mitre.icarus.cps.exam.base.testing.IcarusTestPhase;

/**
 * @author CBONACETO
 *
 */
public abstract class TestHarnessScoreComputerBase {
	
	/**
	 * @param feedbackForTrial
	 * @param examWithResponses
	 */
	protected static void setFeedbackForTrialFields(TestTrialFeedback<?> feedbackForTrial, IcarusExam<?> examWithResponses) {
		if(examWithResponses != null) {
			feedbackForTrial.setExamId(examWithResponses.getId());
			if(examWithResponses.getResponseGenerator() != null) {
				feedbackForTrial.setResponseGeneratorId(examWithResponses.getResponseGenerator().getResponseGeneratorId());
			}
		}
	}
	
	/**
	 * @param feedbackForTrial
	 * @param testPhaseWithResponses
	 */
	protected static void setFeedbackForTrialFields(TestTrialFeedback<?> feedbackForTrial, IcarusTestPhase<?> testPhaseWithResponses) {
		if(testPhaseWithResponses != null) {
			feedbackForTrial.setExamId(testPhaseWithResponses.getExamId());
			if(testPhaseWithResponses.getResponseGenerator() != null) {
				feedbackForTrial.setResponseGeneratorId(testPhaseWithResponses.getResponseGenerator().getResponseGeneratorId());
			}
			feedbackForTrial.setPhaseId(testPhaseWithResponses.getId());
		}
	}	
}