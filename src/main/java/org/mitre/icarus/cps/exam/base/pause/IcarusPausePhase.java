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
package org.mitre.icarus.cps.exam.base.pause;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.base.IcarusExamPhase;

/**
 * A pause for human subjects in an exam.  May or may not be required.  
 * If not required, subjects can hit next to skip when they're ready.
 * 
 * FOR HUMAN SUBJECTS ONLY.
 * 
 * @author CBONACETO
 *
 */
@XmlRootElement(name="PausePhase", namespace="IcarusCPD_Base")
@XmlType(name="Pause", namespace="IcarusCPD_Base")
public class IcarusPausePhase extends IcarusExamPhase {
	
	/** Whether the pause is required (default is true) */
	private Boolean required = true;
	
	/** Whether to show the count-down timer */
	private Boolean showCountdown = true;
	
	/** Pause length in seconds (default is 0) */
	private Integer length_seconds = 0;	
	
	@XmlAttribute(name="Required")
	public Boolean isRequired() {
		return required;
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}	

	@XmlAttribute(name="ShowCountdown")
	public Boolean isShowCountdown() {
		return showCountdown;
	}

	public void setShowCountdown(Boolean showCountdown) {
		this.showCountdown = showCountdown;
	}

	@XmlAttribute(name="Length_seconds")
	public Integer getLength_seconds() {
		return length_seconds;
	}

	public void setLength_seconds(Integer length_seconds) {
		this.length_seconds = length_seconds;
	}

	@Override
	public int getNumTrials() {
		return 0;
	}	
}
