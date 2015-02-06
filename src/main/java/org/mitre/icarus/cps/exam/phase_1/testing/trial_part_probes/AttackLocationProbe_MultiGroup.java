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
package org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.feature_vector.phase_1.GridLocation2D;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupType;

/**
 * A probe where the subject/model is shown an attack location 
 * and asked to estimate the probability of attack by each group.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="AttackLocationProbe_MultiGroup", namespace="IcarusCPD_1", 
		propOrder={"attackLocation", "groups"})
public class AttackLocationProbe_MultiGroup extends MultiGroupProbe {
	
	/** The attack location */
	protected GridLocation2D attackLocation;	
	
	/**
	 * No arg constructor.
	 */
	public AttackLocationProbe_MultiGroup() {}
	
	/**
	 * Constructor that takes the attackLocation and groups.
	 * 
	 * @param attackLocation the attack location
	 * @param groups the groups
	 */
	public AttackLocationProbe_MultiGroup(GridLocation2D attackLocation, ArrayList<GroupType> groups) {
		super(groups);
		this.attackLocation = attackLocation;		
	}	

	/**
	 * Get the attack location.
	 * 
	 * @return the attack location
	 * 
	 */
	@XmlElement(name="AttackLocation")
	public GridLocation2D getAttackLocation() {
		return attackLocation;
	}

	/**
	 * Set the attack location.
	 * 
	 * @param attackLocation the attack location
	 */
	public void setAttackLocation(GridLocation2D attackLocation) {
		this.attackLocation = attackLocation;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.MultiGroupProbe#getGroups()
	 */
	@XmlElement(name="Groups")
	@XmlList
	@Override
	public ArrayList<GroupType> getGroups() {
		return super.getGroups();
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.MultiGroupProbe#setGroups(java.util.ArrayList)
	 */
	@Override
	public void setGroups(ArrayList<GroupType> groups) {
		super.setGroups(groups);
	}		
}