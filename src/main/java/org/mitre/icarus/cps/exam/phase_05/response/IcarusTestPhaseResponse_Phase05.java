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
package org.mitre.icarus.cps.exam.phase_05.response;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.base.response.IcarusTrialResponse;

/**
 * Contains subject or model responses to the test phase of an exam.
 * 
 * @author CBONACETO
 *
 */
@XmlRootElement(name="TestPhaseResponse", namespace="IcarusCPD_05")
@XmlType(name="TestPhaseResponse", namespace="IcarusCPD_05")
public class IcarusTestPhaseResponse_Phase05 extends IcarusExamPhaseResponse {
	
	/** Responses to each test trial */
	protected List<IcarusTrialResponse> trialResponses;
	
	/** Average score for the phase.
	 * FOR HUMAN SUBJECT USE ONLY. */
	protected Double averageScore;
	
	/**
	 * Get the responses to each test trial.
	 * 
	 * @return
	 */
	@XmlElement(name="TrialResponse")
	public List<IcarusTrialResponse> getTrialResponses() {
		return trialResponses;
	}

	/**
	 * Set the responses to each test trial.
	 * 
	 * @param trialResponses
	 */
	public void setTrialResponses(List<IcarusTrialResponse> trialResponses) {
		this.trialResponses = trialResponses;
	}

	/**
	 * Get the average score for the phase, FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @return
	 */
	@XmlTransient	
	public Double getAverageScore() {
		return averageScore;
	}

	/**
	 * Set the average score for the phase, FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @param averageScore
	 */
	public void setAverageScore(Double averageScore) {
		this.averageScore = averageScore;
	}
}
