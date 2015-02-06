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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


//import org.mitre.icarus.cps.experiment_core.subject_data.SubjectConditionData;

/**
 * Abstract base class for subject or model response data for a phase in an exam.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="IcarusExamPhaseResponse", namespace="IcarusCPD_Phase05")
@XmlSeeAlso({IcarusTestPhaseResponse_Phase05.class, IcarusTrainingPhaseResponse.class})
public abstract class IcarusExamPhaseResponse {
	
	/** Name of the phase the response is for */
	protected String phaseName;
	
	/** URL to the file where the response is stored if it's not specified inline 
	 * in the document */
	protected String phaseResponseFileUrl;
	
	/**
	 * Get the phase name.
	 * 
	 * @return
	 */
	@XmlAttribute(name="name")
	public String getPhaseName() {
		return phaseName;
	}

	/**
	 * Set the phase name.
	 * 
	 * @param phaseName
	 */
	public void setPhaseName(String phaseName) {
		this.phaseName = phaseName;
	}

	/**
	 * Get the URL to the file defining the phase.  This is not
	 * currently used.
	 * 
	 * @return
	 */
	@XmlElement(name="PhaseResponseFileUrl")
	public String getPhaseResponseFileUrl() {
		return phaseResponseFileUrl;
	}	

	/**
	 * Set the URL to the file defining the phase.  This is not
	 * currently used.
	 * 
	 * @param phaseResponseFileUrl
	 */
	public void setPhaseResponseFileUrl(String phaseResponseFileUrl) {
		this.phaseResponseFileUrl = phaseResponseFileUrl;
	}	
}
