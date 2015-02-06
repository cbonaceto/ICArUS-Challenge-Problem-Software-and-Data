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
package org.mitre.icarus.cps.exam.base;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Abstract class for ICArUS trials (e.g., scene presentation trials, analogic reasoning probes, training trials).
 * We may add to this class to contain information common to all trial types in the future.
 * 
 * @author CBONACETO
 *
 */
@XmlRootElement(name="IcarusTrial", namespace="IcarusCPD_Base")
@XmlType(name="IcarusTrial", namespace="IcarusCPD_Base")
public abstract class IcarusTrial {
	
	/** The ID of the Challenge Problem phase the trial is for (e.g., 1, 2, 3) */
	protected String programPhaseId;
	
	/** The ID of the exam containing the trial */
	protected String examId;
	
	/** The ID of the exam phase containing the trial */
	protected String phaseId;
	
	/** The trial number */
	private Integer trialNum;
	
	
	/**
	 * Get the ID of the Challenge Problem phase the trial is for (e.g., 1, 2, 3).
	 * 
	 * @return the program phase ID
	 */
	@XmlAttribute(name="programPhaseId")
	public String getProgramPhaseId() {
		return programPhaseId;
	}
	
	/**
	 * Set the ID of the Challenge Problem phase the trial is for (e.g., 1, 2, 3).
	 * 
	 * @param programPhaseId the program phase Id
	 */
	public void setProgramPhaseId(String programPhaseId) {
		this.programPhaseId = programPhaseId;
	}

	/**
	 * Get the ID of the exam the trial is in.
	 * 
	 * @return the exam ID
	 */
	@XmlAttribute(name="examId")
	public String getExamId() {
		return examId;
	}

	/**
	 * Set the ID of the exam the trial is in.
	 * 
	 * @param examId the exam ID
	 */
	public void setExamId(String examId) {
		this.examId = examId;
	}
	
	/**
	 * Get the ID of the exam phase the trial is in.
	 * 
	 * @return the phase ID
	 */
	@XmlAttribute(name="phaseId")	
	public String getPhaseId() {
		return phaseId;
	}

	/**
	 * Set the ID of the exam phase the trial is in.
	 * 
	 * @param phaseId the phase ID
	 */
	public void setPhaseId(String phaseId) {
		this.phaseId = phaseId;
	}

	/**
	 * Get the trial number.
	 * 
	 * @return the trial number
	 */
	@XmlAttribute(name = "trialNum")
	public Integer getTrialNum() {
		return trialNum;
	}

	/**
	 * Set the trial number.
	 * 
	 * @param trialNum the trial number
	 */
	public void setTrialNum(Integer trialNum) {
		this.trialNum = trialNum;
	}
}