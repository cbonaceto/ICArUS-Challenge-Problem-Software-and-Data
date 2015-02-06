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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.base.response.IcarusTrialResponse;


/**
 * Base class for LocateItem and ScenePresentation trial responses.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="ScenePresentationTrialResponse", namespace="IcarusCPD_05")
public abstract class ScenePresentationTrialResponse extends IcarusTrialResponse {
	/** The score (average across all layer presentations) for the trial.
	 * FOR HUMAN SUBJECT USE ONLY. */
	private Double score;

	/**
	 * Get the score, FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @return
	 */
	@XmlElement(name="TrialScore")
	public Double getScore() {
		return score;
	}

	/**
	 * Set the score, FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @param score
	 */
	public void setScore(Double score) {
		this.score = score;
	}	
}
