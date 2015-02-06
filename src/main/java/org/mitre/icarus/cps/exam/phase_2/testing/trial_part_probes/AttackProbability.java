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
package org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.RedAction.RedActionType;

/**
 * Contains the probability that Red will take a certain action (attack, not attack) at a location.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="AttackProbability", namespace="IcarusCPD_2")
public class AttackProbability extends Probability {
	
	/** The Blue location ID */
	protected String locationId;
	
	/** The Blue location index */
	protected Integer locationIndex;
	
	/** The Red action the probability is for (attack, no attack) */
	protected RedActionType redAction;
	
	/**
	 * Construct an empty AttackProbability.
	 */
	public AttackProbability() {}
	
	/**
	 * Construct a AttackProbability with the given location ID, location index, and red action.
	 * 
	 * @param locationId the ID of the location
	 * @param locationIndex the index of the location
	 * @param redAction the Red action the probability is for (attack, not attack)
	 */
	public AttackProbability(String locationId, Integer locationIndex, RedActionType redAction) {
		this.locationId = locationId;
		this.locationIndex = locationIndex;
		this.redAction = redAction;
	}

	/**
	 * Get the ID of the location.
	 * 
	 * @return the ID of the location
	 */
	@XmlAttribute(name="locationId")
	public String getLocationId() {
		return locationId;
	}

	/**
	 * Set the ID of the location.
	 * 
	 * @param locationId the ID of the location
	 */
	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	/**
	 * Get the index of the location.
	 * 
	 * @return the index of the location
	 */
	@XmlAttribute(name="locationIndex")
	public Integer getLocationIndex() {
		return locationIndex;
	}

	/**
	 * Set the index of the location.
	 * 
	 * @param locationIndex the index of the location
	 */
	public void setLocationIndex(Integer locationIndex) {
		this.locationIndex = locationIndex;
	}

	/**
	 * Get the Red action the probability is for (attack, not attack).
	 * 
	 * @return the Red action the probability is for (attack, not attack)
	 */
	@XmlAttribute(name="redAction")
	public RedActionType getRedAction() {
		return redAction;
	}

	/**
	 * Set the Red action the probability is for (attack, not attack).
	 * 
	 * @param redAction the Red action the probability is for (attack, not attack)
	 */
	public void setRedAction(RedActionType redAction) {
		this.redAction = redAction;
	}	
}