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
package org.mitre.icarus.cps.exam.phase_1.testing;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.feature_vector.phase_1.GroupType;

/**
 * Contains ground truth information about an attack (e.g., the responsible group,
 * the location where the attack occurred).
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="GroundTruth", namespace="IcarusCPD_1",
		propOrder={"responsibleGroup", "attackLocationId"})
public class GroundTruth {
	
	/** The group responsible for the attack */
	protected GroupType responsibleGroup;	
	
	/** The location of the attack */
	protected String attackLocationId;
	
	/**
	 * No arg constructor.
	 */
	public GroundTruth() {}
	
	/**
	 * Constructor that takes the responsibleGroup.
	 * 
	 * @param responsibleGroup the group responsible for the attack
	 */
	public GroundTruth(GroupType responsibleGroup) {
		this(responsibleGroup, null);
	}
	
	/**
	 * Constructor that takes the attackLocationId
	 * 
	 * @param attackLocationId the ID of the actual attack location
	 */
	public GroundTruth(String attackLocationId) {
		this(null, attackLocationId);
	}
	
	/**
	 * Constructor that takes the responsbileGroup and attackLocationIds
	 * 
	 * @param responsibleGroup the group responsible for the attack
	 * @param attackLocationId the ID of the actual attack location
	 */
	public GroundTruth(GroupType responsibleGroup, String attackLocationId) {
		this.responsibleGroup = responsibleGroup;
		this.attackLocationId = attackLocationId;
	}

	/**
	 * Get the group responsible for the attack
	 * 
	 * @return the group
	 */
	@XmlAttribute(name="responsibleGroup")
	public GroupType getResponsibleGroup() {
		return responsibleGroup;
	}

	/**
	 * Set the group responsible for the attack
	 * 
	 * @param responsibleGroup the group
	 */
	public void setResponsibleGroup(GroupType responsibleGroup) {
		this.responsibleGroup = responsibleGroup;
	}

	/**
	 * Get the actual attack location ID.
	 * 
	 * @return the location ID
	 */
	@XmlAttribute(name="attackLocationId")
	public String getAttackLocationId() {
		return attackLocationId;
	}

	/**
	 * Set the actual attack location ID.
	 * 
	 * @param attackLocationId the location ID
	 */
	public void setAttackLocationId(String attackLocationId) {
		this.attackLocationId = attackLocationId;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if(responsibleGroup != null) {
			sb.append("Responsible Group: " + responsibleGroup);
		}
		if(attackLocationId != null) {
			if(responsibleGroup != null) {
				sb.append(", ");
			}
			sb.append("Attack Location ID: " + attackLocationId);
		}
		return sb.toString();
	}	
}