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

import java.util.Collection;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.base.testing.IcarusTestTrial;
import org.mitre.icarus.cps.exam.phase_1.feedback.TaskFeedback;

/**
 * Base class for feedback provided by the test harness to an ICARuS system after submitting
 * its responses to a test phase in an exam (e.g., Task 3). 
 * 
 * @author CBONACETO
 *
 */
@XmlRootElement(name="PhaseFeedback", namespace="IcarusCPD_Base")
@XmlType(name="PhaseFeedback", namespace="IcarusCPD_Base")
@XmlSeeAlso({TaskFeedback.class})
public abstract class TestPhaseFeedback<T extends IcarusTestTrial, F extends TestTrialFeedback<T>> {
	
	/** The ID of the Challenge Problem phase the exam phase is for (e.g., 1, 2, 3) */
	protected String programPhaseId;
	
	/** The ID of the exam containing this phase */
	protected String examId;
	
	/** The ID of the exam phase */
	protected String phaseId;	
	
	/** The Id of the response generator this feedback is for 
	 * (ICArUS system ID or subject ID) */
	protected String responseGeneratorId;
	
	/** Whether the response received by the test harness was well-formed */
	protected Boolean responseWellFormed;
	
	/** Any errors in the response */
	protected String errors;
	
	/**
	 * Get the ID of the Challenge Problem phase the exam phase is for (e.g., 1, 2, 3).
	 * 
	 * @return the program phase ID
	 */
	@XmlAttribute(name="programPhaseId")
	public String getProgramPhaseId() {
		return programPhaseId;
	}
	
	/**
	 * Set the ID of the Challenge Problem phase the exam phase is for (e.g., 1, 2, 3).
	 * 
	 * @param programPhaseId the program phase Id
	 */
	public void setProgramPhaseId(String programPhaseId) {
		this.programPhaseId = programPhaseId;
	}

	/**
	 * Get the ID of the exam the phase is in.
	 * 
	 * @return the exam ID
	 */
	@XmlAttribute(name="examId")
	public String getExamId() {
		return examId;
	}

	/**
	 * Set the ID of the exam the phase is in.
	 * 
	 * @param examId the exam ID
	 */
	public void setExamId(String examId) {
		this.examId = examId;
	}
	
	/**
	 * Get the ID of the exam phase.
	 * 
	 * @return the phase ID
	 */
	@XmlAttribute(name="phaseId")	
	public String getPhaseId() {
		return phaseId;
	}

	/**
	 * Set the ID of the exam phase.
	 * 
	 * @param phaseId the phase ID
	 */
	public void setPhaseId(String phaseId) {
		this.phaseId = phaseId;
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
	 * Get the feedback for each trial in the phase.
	 * 
	 * @return the feedback for each trial
	 * 
	 */
	@XmlTransient
	public abstract Collection<F> getTrialFeedback();
}