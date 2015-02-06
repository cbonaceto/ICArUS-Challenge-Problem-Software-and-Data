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

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.base.pause.IcarusPausePhase;
import org.mitre.icarus.cps.exam.base.response.ResponseGeneratorData;
import org.mitre.icarus.cps.exam.base.testing.IcarusTestPhase;
import org.mitre.icarus.cps.exam.base.training.IcarusTutorialPhase;
import org.mitre.icarus.cps.exam.phase_05.training.IcarusTrainingPhase;
import org.mitre.icarus.cps.experiment_core.condition.Condition;

/**
 * Abstract base class for test and training phases in an exam.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="IcarusExamPhase", namespace="IcarusCPD_Base",
		propOrder={"programPhaseId", "id", "examId", "phaseFileUrl", "instructionText", "responseGenerator", "startTime", "endTime"}) 
@XmlSeeAlso({IcarusTestPhase.class, IcarusTrainingPhase.class, IcarusTutorialPhase.class, IcarusPausePhase.class})
public abstract class IcarusExamPhase extends Condition {
	
	/** The ID of the Challenge Problem exam phase is for (e.g., 1, 2, 3) */
	protected String programPhaseId;
	
	/** The phase ID */
	protected String id;
	
	/** The ID of the exam containing the phase */
	protected String examId;	
	
	/** URL to file where data is specified if not specified inline 
	 * in the exam document */
	private String phaseFileUrl;
	
	/** Any instruction text to show before beginning the phase */
	private String instructionText;
	
	/** The response generator information for the phase (human subject or ICArUS system) */
	protected ResponseGeneratorData responseGenerator;
	
	/** The time the phase was started */
	protected Date startTime;
	
	/** The time the phase was completed */
	protected Date endTime;
	
	/** Whether the response data for this exam phase is to be considered the official model run for this
	 * exam phase for the purposes of assessment. */
	protected Boolean officialModelRun;
	
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
	 * Get the phase ID.
	 * 
	 * @return the phase ID
	 */
	@XmlAttribute(name= "id")
	public String getId() {
		return id;
	}

	/**
	 * Set the phase ID
	 * 
	 * @param id the phase ID
	 */
	public void setId(String id) {
		this.id = id;
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
	 * @param examId the exam id
	 */
	public void setExamId(String examId) {
		this.examId = examId;
	}

	/**
	 * Get URL to a file where data is specified if not specified inline in the exam document. Not currently used.
	 * 
	 * @return the URL
	 */
	@XmlElement(name="PhaseFileUrl")
	public String getPhaseFileUrl() {
		return phaseFileUrl;
	}
	
	/**
	 * Set URL to a file where data is specified if not specified inline in the exam document. Not currently used.
	 * 
	 * @param phaseFileUrl the URL
	 */
	public void setPhaseFileUrl(String phaseFileUrl) {
		this.phaseFileUrl = phaseFileUrl;
	}

	/**
	 * Get instruction text to show human subjects at the beginning of the phase.
	 * FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @return the instruction text
	 */
	@XmlElement(name="InstructionText")
	public String getInstructionText() {
		return instructionText;
	}

	/**
	 * Set instruction text to show human subjects at the beginning of the phase.
	 * FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @param instructionText the instruction text
	 */
	public void setInstructionText(String instructionText) {
		this.instructionText = instructionText;
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
	 * Get the time the phase was started.
	 * 
	 * @return the start time
	 */
	@XmlAttribute(name="startTime")
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * Set the time the phase was started.
	 * 
	 * @param startTime the start time
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * Get the time the phase was completed.
	 * 
	 * @return the end time
	 */
	@XmlAttribute(name="endTime")
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * Set the time the phase was completed.
	 * 
	 * @param endTime the end time
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	/**
	 * Get whether the response data for this exam is to be considered the official model run for this exam for the
	 * purposes of assessment.
	 * 
	 * @return whether this is the official model run
	 */
	@XmlAttribute(name="officialModelRun")
	public Boolean isOfficialModelRun() {
		return officialModelRun;
	}

	/**
	 * Set whether the response data for this exam phase is to be considered the official model run for this exam phase  for the
	 * purposes of assessment.
	 * 
	 * @param officialModelRun whether this is the official model run
	 */
	public void setOfficialModelRun(Boolean officialModelRun) {
		this.officialModelRun = officialModelRun;
	}
}