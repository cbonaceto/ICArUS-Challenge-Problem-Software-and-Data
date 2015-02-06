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
import javax.xml.bind.annotation.XmlType;

/**
 * Subject/model response to a surprise report probe.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="SurpriseReportProbeResponse", namespace="IcarusCPD_1")
public class SurpriseReportProbeResponse extends TrialPartResponse {

	/** The surprise */
	protected Integer surpriseVal;
	
	/**
	 * No arg constructor. 
	 */
	public SurpriseReportProbeResponse() {}
	
	/**
	 * Constructor that takes the surprise.
	 * 
	 * @param surpriseVal the surprise
	 */
	public SurpriseReportProbeResponse(Integer surpriseVal) {
		this.surpriseVal = surpriseVal;
	}

	/**
	 * Get the surprise.
	 * 
	 * @return the surprise
	 */
	@XmlAttribute(name="surprise")
	public Integer getSurpriseVal() {
		return surpriseVal;
	}

	/**
	 * Set the surprise.
	 * 
	 * @param surpriseVal the surpirse
	 */
	public void setSurpriseVal(Integer surpriseVal) {
		this.surpriseVal = surpriseVal;
	}
}