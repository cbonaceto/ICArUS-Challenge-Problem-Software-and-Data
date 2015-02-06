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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.feature_vector.phase_1.GroupType;

/**
 * Subject/model response to a troop selection decision where troops are sent
 * against a single group.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="TroopSelectionResponse_MultiGroup", namespace="IcarusCPD_1")
public class TroopSelectionResponse_MultiGroup extends TrialPartResponse {
	
	/** The group the subject/model chose to send troops against */
	protected GroupType group;
	
	/** The troop allocation score (S2) */
	protected Double troopAllocationScore_s2;

	/**
	 * Set the group the subject/model chose to send troops against.
	 * 
	 * @return the group
	 */
	@XmlAttribute(name="group")
	public GroupType getGroup() {
		return group;
	}

	/**
	 * Get the group the subject/model chose to send troops against
	 * 
	 * @param group the group
	 */
	public void setGroup(GroupType group) {
		this.group = group;
	}

	/**
	 * Get the troop allocation score (S2).
	 * 
	 * @return the troop allocation score
	 */
	@XmlElement(name="TroopAllocationScore_s2")
	public Double getTroopAllocationScore_s2() {
		return troopAllocationScore_s2;
	}

	/** 
	 * Set the troop allocation score (S2).
	 * 
	 * @param troopAllocationScore_s2 the troop allocation score
	 */
	public void setTroopAllocationScore_s2(Double troopAllocationScore_s2) {
		this.troopAllocationScore_s2 = troopAllocationScore_s2;
	}	
}