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
package org.mitre.icarus.cps.exam.base.response;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.phase_05.response.AssessmentTrialResponse;
import org.mitre.icarus.cps.exam.phase_05.response.IdentifyItemTrialResponse;
import org.mitre.icarus.cps.exam.phase_05.response.LocateItemTrialResponse;
import org.mitre.icarus.cps.exam.phase_1.response.IcarusTrialResponse_Phase1;


/**
 * Abstract base class for responses to a trial in an exam.
 * 
 * @author CBONACETO
 *
 */
@XmlRootElement(name="IcarusTrialResponse", namespace="IcarusCPD_Base")
@XmlType(name="IcarusTrialResponse", namespace="IcarusCPD_Base",
		propOrder={"programPhaseId", "examId", "phaseId", "trialNum", "startTime", "endTime", 
			"trialTime_ms", "responseGenerator"})
@XmlSeeAlso({IdentifyItemTrialResponse.class, LocateItemTrialResponse.class, AssessmentTrialResponse.class, 
	IcarusTrialResponse_Phase1.class})
public abstract class IcarusTrialResponse {
	
	/** The ID of the Challenge Problem phase the trial response is for (e.g., 1, 2, 3) */
	protected String programPhaseId; 
	
	/** The exam the response is for */
	private String examId;
	
	/** The exam phase the response is for */
	private String phaseId;
	
	/** The trial number the response is for */
	private Integer trialNum;
	
	/** The response generator for the trial (ICAruS system or human subject) */
	private ResponseGeneratorData responseGenerator;
	
	/** The trial start time */
	private Date startTime;
	
	/** The trial end time */
	private Date endTime;
	
	/** The total amount of time spent on the trial in ms
	 * (collected for human subjects only) */
	private Long trialTime_ms;
	
	/**
	 * Get the ID of the Challenge Problem phase the trial response is for (e.g., 1, 2, 3).
	 * 
	 * @return the program phase ID
	 */
	@XmlAttribute(name="programPhaseId")
	public String getProgramPhaseId() {
		return programPhaseId;
	}
	
	/**
	 * Set the ID of the Challenge Problem phase the trial response is for (e.g., 1, 2, 3).
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
	@XmlAttribute(name="trialNum")
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

	/**
	 * Get the response generator that generated the response to the trial.
	 * 
	 * @return the response generator
	 */
	@XmlElement(name="ResponseGenerator")
	public ResponseGeneratorData getResponseGenerator() {
		return responseGenerator;
	}

	/**
	 * Set the response generator that generated the response to the trial.
	 * 
	 * @param responseGenerator the response generator
	 */
	public void setResponseGenerator(ResponseGeneratorData responseGenerator) {
		this.responseGenerator = responseGenerator;
	}

	/**
	 * Get the time the trial was started.
	 * 
	 * @return the start time
	 */
	@XmlAttribute(name="startTime")
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * Set the time the trial was started.
	 * 
	 * @param startTime the start time
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * Get the time the trial was completed.
	 * 
	 * @return the end time
	 */
	@XmlAttribute(name="endTime")
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * Set the time the trial was completed.
	 * 
	 * @param endTime the end time
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/**
	 * Get the time spent on the trial in milliseconds.
	 * FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @return the trial time in milliseconds
	 */
	@XmlAttribute(name="trialTime_ms")
	public Long getTrialTime_ms() {
		return trialTime_ms;
	}

	/**
	 * Set the time spent on the trial in milliseconds.
	 * FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @param trialTime_ms the trial time in milliseconds
	 */
	public void setTrialTime_ms(Long trialTime_ms) {
		this.trialTime_ms = trialTime_ms;
	}
}