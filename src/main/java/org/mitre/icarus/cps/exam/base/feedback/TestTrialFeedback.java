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
package org.mitre.icarus.cps.exam.base.feedback;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.base.testing.IcarusTestTrial;
import org.mitre.icarus.cps.exam.phase_1.feedback.TrialFeedback_Phase1;
import org.mitre.icarus.cps.exam.phase_2.feedback.TrialFeedback_Phase2;

/**
 * Base class for feedback provided by the test harness to an ICARuS system after submitting
 * its response to a trial in a phase in an exam (e.g., a Task 3 trial). 
 * 
 * @author CBONACETO
 *
 */
@XmlRootElement(name="TrialFeedback", namespace="IcarusCPD_Base")
@XmlType(name="TrialFeedback", namespace="IcarusCPD_Base")
@XmlSeeAlso({TrialFeedback_Phase1.class, TrialFeedback_Phase2.class})
public abstract class TestTrialFeedback<T extends IcarusTestTrial> {
	
	/** The trial that the feedback is for */
	protected T trial;
	
	/** The ID of the Challenge Problem phase the trial is for (e.g., 1, 2, 3) */
	protected String programPhaseId; 
	
	/** The ID of the exam the feedback is for */
	protected String examId;
	
	/** The ID of the exam phase (task) the feedback is for */
	protected String phaseId;
	
	/** The trial number the feedback is for */
	protected Integer trialNum;
	
	/** The site ID of the response generator thsi feedback is for */
	protected String siteId;
	
	/** The ID of the response generator this feedback is for */
	protected String responseGeneratorId;
	
	/** Whether the response received by the test harness was well-formed */
	protected Boolean responseWellFormed;
	
	/** Any errors in the response */
	protected String errors;
	
	/** Any warnings about the response */
	protected String warnings;
	
	/**
	 * Get the trial that the feedback is for.
	 * 
	 * @return the trial that the feedback is for
	 */
	@XmlTransient
	public T getTrial() {
		return trial;
	}

	/**
	 * Set the trial that the feedback is for.
	 * 
	 * @param trial the trial that the feedback is for
	 */
	public void setTrial(T trial) {
		this.trial = trial;
	}
	
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
	 * Get the ID of the exam phase (e.g., task) the trial is in.
	 * 
	 * @return the phase ID
	 */
	@XmlAttribute(name="phaseId")	
	public String getPhaseId() {
		return phaseId;
	}

	/**
	 * Set the ID of the exam phase (e.g., task) the trial is in.
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
	 * Get the the response generator ID.
	 * 
	 * @return the response generator ID
	 */
	@XmlAttribute(name="responseGeneratorId")
	public String getResponseGeneratorId() {
		return responseGeneratorId;
	}

	/**
	 * Set the the response generator ID.
	 * 
	 * @param responseGeneratorId the response generator ID
	 */
	public void setResponseGeneratorId(String responseGeneratorId) {
		this.responseGeneratorId = responseGeneratorId;
	}

	/**
	 * Get whether the response received by the test harness was well-formed.
	 * 
	 * @return whether the response was well-formed.
	 */
	@XmlAttribute(name="responseWellFormed")
	public Boolean isResponseWellFormed() {
		return responseWellFormed;
	}

	/**
	 * Set whether the response received by the test harness was well-formed.
	 * 
	 * @param responseWellFormed whether the response was well-formed.
	 */
	public void setResponseWellFormed(Boolean responseWellFormed) {
		this.responseWellFormed = responseWellFormed;
	}

	/**
	 * Get any errors in the response received by the test harness. 
	 * 
	 * @return the errors
	 */
	@XmlElement(name="Errors")
	public String getErrors() {
		return errors;
	}

	/**
	 * Set any errors in the response received by the test harness. 
	 * 
	 * @param errors the errors
	 */
	public void setErrors(String errors) {
		this.errors = errors;
	}

	/**
	 * Get any warnings about the response.
	 * 
	 * @return the warnings
	 */
	@XmlElement(name="Warnings")
	public String getWarnings() {
		return warnings;
	}

	/**
	 * Set any warnings about the response
	 * 
	 * @param warnings the warnings
	 */
	public void setWarnings(String warnings) {
		this.warnings = warnings;
	}	
}