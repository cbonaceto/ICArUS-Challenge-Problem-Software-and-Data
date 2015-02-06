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

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Subject/model response to a multi-group attack location probe.
 * That is, the subject/model indicates the probability that 
 * each group is responsible for an attack at a certain location.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="AttackLocationProbeResponse_MultiGroup", namespace="IcarusCPD_1")
public class AttackLocationProbeResponse_MultiGroup extends ProbabilityProbeTrialPartResponse {
	
	/** The probability that each group is responsible for the attack 
	 * at the given location */
	protected ArrayList<GroupAttackProbabilityResponse> groupAttackProbabilities;
	
	/** The attack location ID */
	protected String attackLocationId;
	
	/**
	 * No arg constructor.
	 */
	public AttackLocationProbeResponse_MultiGroup() {}	
	
	/**
	 * Constructor that takes groupAttackProbabilities and an attackLocationId.
	 * 
	 * @param groupAttackProbabilities the group attack probabilities
	 * @param attackLocationId the attack location Id
	 */
	public AttackLocationProbeResponse_MultiGroup(ArrayList<GroupAttackProbabilityResponse> groupAttackProbabilities,
			String attackLocationId) {		
		this.groupAttackProbabilities = groupAttackProbabilities;
		this.attackLocationId = attackLocationId;
	}

	/**
	 * Get the attack probabilities for each group.
	 * 
	 * @return the attack probabilities
	 */
	@XmlElement(name="GroupAttackProbability")
	public ArrayList<GroupAttackProbabilityResponse> getGroupAttackProbabilities() {
		return groupAttackProbabilities;
	}	

	/**
	 * Set the attack probabilities for each group.
	 * 
	 * @param groupAttackProbabilities the attack probabilities
	 */
	public void setGroupAttackProbabilities(
			ArrayList<GroupAttackProbabilityResponse> groupAttackProbabilities) {
		this.groupAttackProbabilities = groupAttackProbabilities;
	}

	/**
	 * Get the ID of the attack location.
	 * 
	 * @return the attack location ID
	 */
	@XmlAttribute(name="locationId")
		public String getAttackLocationId() {
		return attackLocationId;
	}

	/**
	 * Set the ID of the attack location.
	 * 
	 * @param attackLocationId the attack location ID
	 */
	public void setAttackLocationId(String attackLocationId) {
		this.attackLocationId = attackLocationId;
	}		
}