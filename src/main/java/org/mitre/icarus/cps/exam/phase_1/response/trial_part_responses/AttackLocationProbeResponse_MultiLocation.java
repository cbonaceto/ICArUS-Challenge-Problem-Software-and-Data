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

import org.mitre.icarus.cps.feature_vector.phase_1.GroupType;

/**
 * Subject/model response to a multi-location attack location probe.
 * That is, the subject/model indicates the probability that 
 * a certain group is responsible for an attack at a set of locations.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="AttackLocationProbeResponse_MultiLocation", namespace="IcarusCPD_1")
public class AttackLocationProbeResponse_MultiLocation extends ProbabilityProbeTrialPartResponse {
	
	/** The probability that the group is responsible for the attack at each location */
	protected ArrayList<GroupAttackProbabilityResponse> groupAttackProbabilities;
	
	/** The group */
	protected GroupType group;
	
	/**
	 * No arg constructor.
	 */
	public AttackLocationProbeResponse_MultiLocation() {}
	
	/**
	 * Constructor that takes groupAttackProbabilities and a group
	 * 
	 * @param groupAttackProbabilities the group attack probabilities
	 * @param group the group
	 */
	public AttackLocationProbeResponse_MultiLocation(ArrayList<GroupAttackProbabilityResponse> groupAttackProbabilities,
			GroupType group) {
		this.groupAttackProbabilities = groupAttackProbabilities;
		this.group = group;
	}

	/**
	 * Get the attack probabilities for each location.
	 * 
	 * @return the attack probabilities
	 */
	@XmlElement(name="GroupAttackProbability")
	public ArrayList<GroupAttackProbabilityResponse> getGroupAttackProbabilities() {
		return groupAttackProbabilities;
	}

	/**
	 * Set the group attack probabilities for each location.
	 * 
	 * @param groupAttackProbabilities the attack probabilities
	 */
	public void setGroupAttackProbabilities(
			ArrayList<GroupAttackProbabilityResponse> groupAttackProbabilities) {
		this.groupAttackProbabilities = groupAttackProbabilities;
	}

	/**
	 * Get the group.
	 * 
	 * @return the group
	 */
	@XmlAttribute(name="Group")
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
}