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
package org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.feature_vector.phase_1.GroupType;

/**
 * Subject/model indication of the probability a certain group is 
 * responsible for an attack at a location.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="GroupAttackProbabilityResponse", namespace="IcarusCPD_1", 
		propOrder={"group", "attackLocationId", "probability", "rawProbability"})
public class GroupAttackProbabilityResponse {
	
	/** The group */
	protected GroupType group;
	
	/** The attack location Id */
	protected String attackLocationId;
	
	/** The probability that the group is responsible for the attack (normalized) */
	protected Double probability;
	
	/** The probability that the group is responsible for the attack (raw) */
	protected Double rawProbability;
	
	/** The time spent on this probability judgment (e.g., time subject spent adjusting probability setting) (milliseconds) */
	protected Long time_ms;
	
	/**
	 * No arg constructor.
	 */
	public GroupAttackProbabilityResponse() {}
	
	/**
	 * Constructor that takes a group and normalized probability.
	 * 
	 * @param group the group
	 * @param probability the normalized probability
	 */
	public GroupAttackProbabilityResponse(GroupType group, Double probability) {
		this(group, probability, null);
	}
	
	/**
	 * Constructor that takes a group, the normalized probability, and the raw probability.
	 * FOR HUMAN SUBJECT USE ONLY
	 * 
	 * @param group the group
	 * @param probability the normalized probability
	 * @param rawProbability the raw probability
	 */
	public GroupAttackProbabilityResponse(GroupType group, Double probability, Double rawProbability) {
		this.group = group;
		this.probability = probability;
		this.rawProbability = rawProbability;
	}
	
	/**
	 * Constructor that takes an attackLocationId and normalized probability.
	 * 
	 * @param attackLocationId the attack location ID
	 * @param probability the normalized probability
	 */
	public GroupAttackProbabilityResponse(String attackLocationId, Double probability) {
		this(attackLocationId, probability, null);
	}
	
	/**
	 * Constructor that takes an attackLocationId, a normalized probability, and a raw probability.
	 * 
	 * @param attackLocationId the attack location ID
	 * @param probability the normalized probability
	 * @param rawProbability the raw probability
	 */
	public GroupAttackProbabilityResponse(String attackLocationId, Double probability, Double rawProbability) {
		this.attackLocationId = attackLocationId;
		this.probability = probability;
		this.rawProbability = rawProbability;
	}

	/**
	 * Get the group.
	 * 
	 * @return the group
	 */
	@XmlAttribute(name="group")
	public GroupType getGroup() {
		return group;
	}

	/**
	 * Set the group.
	 * 
	 * @param group the group
	 */
	public void setGroup(GroupType group) {
		this.group = group;
	}

	/**
	 * Get the attack location ID.
	 * 
	 * @return the attack location ID
	 */
	@XmlAttribute(name="locationId")	
	public String getAttackLocationId() {
		return attackLocationId;
	}

	/**
	 * Set the attack location ID.
	 * 
	 * @param attackLocationId the attack location ID
	 */
	public void setAttackLocationId(String attackLocationId) {
		this.attackLocationId = attackLocationId;
	}

	/**
	 * Get the probability (normalized).
	 * 
	 * @return the normalized probability
	 */
	@XmlAttribute(name="probability")
	public Double getProbability() {
		return probability;
	}
	
	/**
	 * Get the probability (not normalized).
	 * FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @param probability the non-normalized probability
	 */
	public void setProbability(Double probability) {
		this.probability = probability;
	}

	/**
	 * Get the probability (not normalized).
	 * FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @return the non-normalized probability
	 */
	@XmlAttribute(name="rawProbability")
	public Double getRawProbability() {
		return rawProbability;
	}

	/**
	 * Set the probability (not normalized).
	 * FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @param rawProbability the non-normalized probability
	 */
	public void setRawProbability(Double rawProbability) {
		this.rawProbability = rawProbability;
	}
	
	/**
	 * Get the time spent on this probability judgment (e.g., time subject spent adjusting probability setting) (milliseconds).
	 * FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @return the time in milliseconds
	 */
	@XmlAttribute(name="time_ms")
	public Long getTime_ms() {
		return time_ms;
	}

	/**
	 * Set the time spent on this probability judgment (e.g., time subject spent adjusting probability setting) (milliseconds).
	 * FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @param time_ms the time in milliseconds
	 */
	public void setTime_ms(Long time_ms) {
		this.time_ms = time_ms;
	}
}