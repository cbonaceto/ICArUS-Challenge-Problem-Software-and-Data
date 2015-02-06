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

import java.net.URL;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.base.response.ResponseGeneratorData;
import org.mitre.icarus.cps.exam.phase_05.IcarusExam_Phase05;
import org.mitre.icarus.cps.exam.phase_1.IcarusExam_Phase1;
import org.mitre.icarus.cps.experiment_core.Experiment;

/**
 * Abstract base class for exams.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="IcarusEvaluationBase", namespace="IcarusCPD_Base", 
		propOrder={"normalizationMode", "responseGenerator"})
@XmlSeeAlso({IcarusExam_Phase05.class, IcarusExam_Phase1.class})
public abstract class IcarusExam<P extends IcarusExamPhase> extends Experiment<P> {
	
	/** Probability normalization modes include: 
	 * NormalizeDuringInstaneous - Automatically normalize probability entries as they are made
	 * NormalizeDuringManual - Be sure probability entries are normalized before going to the next trial
	 * NormalizeAfter - Normalize probability entries automatically after the subject clicks next,
	 *   and then advance to the next trial without confirming the normalized settings.
	 * NormalizeAfterAndConfirm: Normalize probability entries automatically after the subject clicks next, 
	 *   and confirm the normalized probabilities before going to the next trial.
	 * NormalizeOff: Don't perform normalization. 
	 * FOR HUMAN SUBJECT USE ONLY.
	 * */
	@XmlType(name="NormalizationMode", namespace="IcarusCPD_Base")
	public static enum NormalizationMode {NormalizeDuringInstaneous, NormalizeDuringManual, NormalizeAfter, 
		NormalizeAfterAndConfirm, NormalizeOff};
		
	/** The ID of the Challenge Problem phase the exam is for (e.g., 1, 2, 3) */
	protected String programPhaseId;
	
	/** Exam name */
	private String name;
	
	/** Exam ID */
	private String id;	
	
	/** The exam time stamp */
	protected Date examTimeStamp;
	
	/** Original URL to the xml file, used for resolving relative paths */
	protected URL originalPath;	
	
	/** The normalization mode. FOR HUMAN SUBJECT USE ONLY.  */
	protected NormalizationMode normalizationMode = NormalizationMode.NormalizeAfter;
	
	/** The response generator information (human subject or model) */
	protected ResponseGeneratorData responseGenerator;
	
	/** The time the exam was started */
	protected Date startTime;
	
	/** The time the exam was completed */
	protected Date endTime;	
	
	/** Whether the response data for this exam is to be considered the official model run for this exam for the
	 * purposes of assessment. */
	protected Boolean officialModelRun;
	
	/**
	 * Get the ID of the Challenge Problem phase the exam is for (e.g., 1, 2, 3).
	 * 
	 * @return the program phase ID
	 */
	@XmlAttribute(name="programPhaseId")
	public String getProgramPhaseId() {
		return programPhaseId;
	}
	
	/**
	 * Set the ID of the Challenge Problem phase the exam is for (e.g., 1, 2, 3).
	 * 
	 * @param programPhaseId the program phase Id
	 */
	public void setProgramPhaseId(String programPhaseId) {
		this.programPhaseId = programPhaseId;
	}
	
	/**
	 * Get the exam name.
	 * 
	 * @return the exam name
	 */
	@XmlAttribute(name= "name")
	public String getName() {
		return name;
	}

	/**
	 * Set the exam name.
	 * 
	 * @param name the exam name
	 */
	public void setName(String name) {
		this.name = name;
	}	
	
	/**
	 * Get the exam ID.
	 * 
	 * @return the exam ID
	 */
	@XmlAttribute(name= "id")
	public String getId() {
		return id;
	}

	/**
	 * Set the exam ID
	 * 
	 * @param id the exam ID
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Get the exam time stamp that indicates when the exam file was last modified.
	 * 
	 * @return the time stamp
	 */
	@XmlAttribute(name="examTimeStamp")
	public Date getExamTimeStamp() {
		return examTimeStamp;
	}

	/**
	 * Set the exam time stamp that indicates when the exam file was last modified.
	 * 
	 * @param examTimeStamp the time stamp
	 */
	public void setExamTimeStamp(Date examTimeStamp) {
		this.examTimeStamp = examTimeStamp;
	}	
	
	/**
	 * Get the base URL to the exam file location.
	 * 
	 * @return the base URL
	 */
	@XmlTransient
	public URL getOriginalPath() {
		return originalPath;
	}
	
	/**
	 * Set the base URL to the exam file location.
	 * 
	 * @param originalPath the base URL
	 */
	public void setOriginalPath(URL originalPath) {
		this.originalPath = originalPath;
	}

	/**
	 * Get the normalization mode. FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @return the normalization mode
	 */
	@XmlElement(name="NormalizationMode")
	public NormalizationMode getNormalizationMode() {
		return normalizationMode;
	}

	/**
	 * Set the normalization mode. FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @param normalizationMode the normalization mode
	 */
	public void setNormalizationMode(NormalizationMode normalizationMode) {
		this.normalizationMode = normalizationMode;
	}
	
	/**
	 * Get the response generator that generated the response to the exam. 
	 * 
	 * @return the response generator
	 */
	@XmlElement(name="ResponseGenerator")
	public ResponseGeneratorData getResponseGenerator() {
		return responseGenerator;
	}

	/**
	 * Set the response generator that generated the response to the exam.
	 * 
	 * @param responseGenerator the response generator
	 */
	public void setResponseGenerator(ResponseGeneratorData responseGenerator) {
		this.responseGenerator = responseGenerator;
	}

	/**
	 * Get the time the exam was started.
	 * 
	 * @return the start time
	 */
	@XmlAttribute(name="startTime")
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * Set the time the exam was started.
	 * 
	 * @param startTime the start time
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * Get the time the exam was completed.
	 * 
	 * @return the end time
	 */
	@XmlAttribute(name="endTime")
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * Set the time the exam was completed.
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
	 * Set whether the response data for this exam is to be considered the official model run for this exam for the
	 * purposes of assessment.
	 * 
	 * @param officialModelRun whether this is the official model run
	 */
	public void setOfficialModelRun(Boolean officialModelRun) {
		this.officialModelRun = officialModelRun;
	}

	@Override
	public abstract List<P> getConditions();
}