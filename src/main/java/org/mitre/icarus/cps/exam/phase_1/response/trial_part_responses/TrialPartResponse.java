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
package org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

/**
 * Base class for responses to a stage/part of a trial (e.g., the surprise report).
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="TrialPartResponse", namespace="IcarusCPD_1")
@XmlSeeAlso({AttackLocationProbeResponse_MultiGroup.class, AttackLocationProbeResponse_MultiLocation.class,
	GroupCentersProbeResponse.class, GroupCirclesProbeResponse.class, SurpriseReportProbeResponse.class})
public abstract class TrialPartResponse {
	
	/** Amount of time spent on the trial part in ms (collected for human subjects only) */
	protected Long trialPartTime_ms;

	/**
	 * Get the amount of time spent on the trial in milliseconds.
	 * FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @return the trial part time in milliseconds
	 */
	@XmlAttribute(name="trialPartTime_ms")
	public Long getTrialPartTime_ms() {
		return trialPartTime_ms;
	}

	/**
	 * Set the amount of time spent on the trial in milliseconds.
	 * FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @param trialPartTime_ms the trial part time in milliseconds
	 */
	public void setTrialPartTime_ms(Long trialPartTime_ms) {
		this.trialPartTime_ms = trialPartTime_ms;
	}
}