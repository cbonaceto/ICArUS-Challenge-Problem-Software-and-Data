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
package org.mitre.icarus.cps.exam.phase_2.testing.bluebook;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Contains the parameters defining a Red tactic.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="RedTactic", namespace="IcarusCPD_2")
public class RedTactic {
	
	/** The tactic name */
	protected String name;
	
	/** The Red tactic type */
	protected RedTacticType tacticType;

	/** The tactic parameters */
	protected RedTacticParameters tacticParameters;	
	
	/**
	 * Construct an empty RedTactic.
	 */
	public RedTactic() {}
	
	/**
	 * Construct a RedTactic with the given name, tactic type, and tactic parameters.
	 * 
	 * @param name the tactic name
	 * @param tacticType the tactic type
	 * @param tacticParameters the tactic parameters
	 */
	public RedTactic(String name, RedTacticType tacticType, RedTacticParameters tacticParameters) {
		this.name = name;
		this.tacticType = tacticType;
		this.tacticParameters = tacticParameters;
	}

	/**
	 * Get the tactic name.
	 * 
	 * @return the tactic name
	 */
	@XmlAttribute(name="name")
	public String getName() {
		return name;
	}

	/**
	 * Set the tactic name.
	 * 
	 * @param name the tactic name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the tactic type.
	 * 
	 * @return the tactic type
	 */
	@XmlAttribute(name="type")
	public RedTacticType getTacticType() {
		return tacticType;
	}

	/**
	 * Set the tactic type.
	 * 
	 * @param tacticType the tactic type
	 */
	public void setTacticType(RedTacticType tacticType) {
		this.tacticType = tacticType;
	}

	/**
	 * Get the tactic parameters.
	 * 
	 * @return the tactic parameters
	 */
	@XmlElement(name="TacticParameters")
	public RedTacticParameters getTacticParameters() {
		return tacticParameters;
	}

	/**
	 * Set the tactic parameters.
	 * 
	 * @param tacticParameters the tactic parameters
	 */
	public void setTacticParameters(RedTacticParameters tacticParameters) {
		this.tacticParameters = tacticParameters;
	}	
}