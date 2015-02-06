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
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.phase_2.testing.bluebook.RedTacticParameters.RedTacticConsiderationData;

/**
 * An enumeration of pre-defined Red tactic types.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="RedTacticType", namespace="IcarusCPD_2")
public enum RedTacticType {	
	/**
	 * Mission 1 Red tactic.
	 */
	@XmlEnumValue("Mission_1")
	Mission_1("Netural", new RedTacticParameters(
			RedTacticConsiderationData.P_And_U, 0.2, 0.4, 0.6, 0.8, 0.25, 3)),
	
	/**
	 * Mission 2 passive Red tactic.
	 */
	@XmlEnumValue("Mission_2_Passive")
	Mission_2_Passive("Passive", new RedTacticParameters(
			RedTacticConsiderationData.P_And_U, 0.2, 0.3, 0.4, 0.5, 0.25, 3)),	
	
	/**
	 * Mission 2 agressive Red tactic.
	 */
	@XmlEnumValue("Mission_2_Aggressive")
	Mission_2_Aggressive("Aggressive", new RedTacticParameters(
			RedTacticConsiderationData.P_And_U, 0.5, 0.6, 0.7, 0.8, 0.25, 3)),
	
	/**
	 * Mission 3 Red tactic.
	 */
	@XmlEnumValue("Mission_3")
	Mission_3("Neutral", new RedTacticParameters(
			RedTacticConsiderationData.P_And_U, 0.1, 0.2, 0.3, 0.4, 0.25, 3)),
	
	/**
	 * Mission 4 passive Red tactic.
	 */
	@XmlEnumValue("Mission_4_Passive")
	Mission_4_Passive("Passive", new RedTacticParameters(
			RedTacticConsiderationData.P_And_U, 0.2, 0.3, 0.4, 0.5, 0.25, 3)),	
	
	/**
	 * Mission 4 agressive Red tactic.
	 */
	@XmlEnumValue("Mission_4_Aggressive")
	Mission_4_Aggressive("Aggressive", new RedTacticParameters(
			RedTacticConsiderationData.P_And_U, 0.5, 0.6, 0.7, 0.8, 0.25, 3)),	
	
	/**
	 * Mission 5 P-sensitive Red tactic.
	 */
	@XmlEnumValue("Mission_5_Psensitive")
	Mission_5_Psensitive("P-Sensitive", new RedTacticParameters(
			RedTacticConsiderationData.P_Only, 0.4, 0.4, 0.6, 0.6, 0.25, 3)),
	
	/**
	 * Mission 5 U-sensitive Red tactic.
	 */
	@XmlEnumValue("Mission_5_Usensitive")
	Mission_5_Usensitive("U-Sensitive", new RedTacticParameters(
			RedTacticConsiderationData.U_Only, 0.2, 0.8, 0.2, 0.8, 0.25, 3)),
	
	/** Possible Mission 6 Red tactics. */	
	@XmlEnumValue("Mission_6_1")
	Mission_6_1("Mission 6-1", new RedTacticParameters(
			RedTacticConsiderationData.P_Only, 0.2, 0.4, 0.6, 0.8, 0.25, 3)),
	@XmlEnumValue("Mission_6_2")
	Mission_6_2("Mission 6-1", new RedTacticParameters(
			RedTacticConsiderationData.P_Only, 0.2, 0.4, 0.6, 0.8, 0.25, 3)),
	@XmlEnumValue("Mission_6_3")
	Mission_6_3("Mission 6-3", new RedTacticParameters(
			RedTacticConsiderationData.P_Only, 0.2, 0.4, 0.6, 0.8, 0.25, 3)),
	@XmlEnumValue("Mission_6_4")
	Mission_6_4("Mission 6-4", new RedTacticParameters(
			RedTacticConsiderationData.P_Only, 0.2, 0.4, 0.6, 0.8, 0.25, 3)),
	@XmlEnumValue("Mission_6_5")
	Mission_6_5("Mission 6-5", new RedTacticParameters(
			RedTacticConsiderationData.P_Only, 0.2, 0.4, 0.6, 0.8, 0.25, 3));	

	/** The Red tactic */
	@XmlTransient
	protected RedTactic tactic;		

	/**
	 * Construct a RedTacticType with the given name and tactic parameters.
	 * 
	 * @param name the tactic name
	 * @param tacticParameters the tactic parameters
	 */
	private RedTacticType(String name, RedTacticParameters tacticParameters) {
		this.tactic = new RedTactic(name, this, tacticParameters);
	}

	/**
	 * Get the tactic name.
	 * 
	 * @return the name the tactic name
	 */
	@XmlAttribute(name="name")
	public String getName() {
		return tactic != null ? tactic.getName() : null;
	}

	/**
	 * Set the tactic name.
	 * 
	 * @param name the tactic name
	 */
	public void setName(String name) {
		if(tactic == null) {tactic = new RedTactic();}
		tactic.name = name;
	}

	/**
	 * Get the tactic parameters.
	 * 
	 * @return the tactic parameters
	 */
	@XmlElement(name="TacticParameters")
	public RedTacticParameters getTacticParameters() {
		return tactic != null ? tactic.tacticParameters : null;
	}	

	/**
	 * Set the tactic parameters.
	 * 
	 * @param tacticParameters the tactic parameters
	 */
	public void setTacticParameters(RedTacticParameters tacticParameters) {
		if(tactic == null) {tactic = new RedTactic();}
		tactic.tacticParameters = tacticParameters;
	}
	
	/**
	 * Create a RedTactic instance.
	 * 
	 * @return a RedTactic instance
	 */
	public RedTactic createTactic() {
		return new RedTactic(tactic.name, this, tactic.tacticParameters);
	}
}