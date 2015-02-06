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
package org.mitre.icarus.cps.exam.phase_1.response;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.base.response.IcarusTrialResponse;
import org.mitre.icarus.cps.exam.phase_1.feedback.TrialFeedback_Phase1;

/**
 * Abstract base class for responses to test trials for Phase 1 Tasks.  Phase 1 responses
 * also contain feedback on the response to the trial provided by the test harness.
 * 
 * @author CBONACETO
 *
 */
@XmlRootElement(name="IcarusTrialResponse_CPD1", namespace="IcarusCPD_1")
@XmlType(name="IcarusTrialResponse_CPD1", namespace="IcarusCPD_1")
@XmlSeeAlso({Task_1_2_3_ProbeTrialResponseBase.class, Task_4_TrialResponse.class, Task_5_6_TrialResponse.class,
	Task_7_TrialResponse.class})
public class IcarusTrialResponse_Phase1 extends IcarusTrialResponse {

	/** Feedback provided by the test harness on the subject/model response to the trial */
	protected TrialFeedback_Phase1 responseFeedBack;
	
	/**
	 * Get the feedback provided by the test harness on the subject/model response to the trial.
	 * 
	 * @return the feedback 
	 */
	@XmlElement(name="ResponseFeedback")
	public TrialFeedback_Phase1 getResponseFeedBack() {
		return responseFeedBack;
	}

	/**
	 * Set the feedback provided by the test harness on the subject/model response to the trial.
	 * 
	 * @param responseFeedBack the feedback
	 */
	public void setResponseFeedBack(TrialFeedback_Phase1 responseFeedBack) {
		this.responseFeedBack = responseFeedBack;
	}	
}