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

import org.mitre.icarus.cps.feature_vector.phase_1.GroupType;

/**
 * A probe where the subject/model is given an attacking group 
 * and asked to estimate the probability of attack at each location.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="AttackLocationProbe_MultiLocation", namespace="IcarusCPD_1",
		propOrder={"attackGroup", "locations"})
public class AttackLocationProbe_MultiLocation extends MultiLocationProbe {
	
	/** The group */
	protected GroupType attackGroup;	
	
	/**
	 * No arg constructor.
	 */
	public AttackLocationProbe_MultiLocation() {}
	
	/**
	 * Constructor that takes the attackGroup and locations.
	 * 
	 * @param attackGroup the attack group
	 * @param locations the locations
	 */
	public AttackLocationProbe_MultiLocation(GroupType attackGroup, ArrayList<String> locations) {
		super(locations);
		this.attackGroup = attackGroup;
	}

	/**
	 * Get the attack group.
	 * 
	 * @return the attack group
	 */
	@XmlElement(name="AttackGroup")
	public GroupType getAttackGroup() {
		return attackGroup;
	}

	/**
	 * Set the attack group.
	 * 
	 * @param attackGroup the attack group
	 */
	public void setAttackGroup(GroupType attackGroup) {
		this.attackGroup = attackGroup;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.MultiLocationProbe#getLocations()
	 */
	@XmlElement(name="Locations")
	@XmlList
	@Override
	public ArrayList<String> getLocations() {
		return super.getLocations();
	}	
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.MultiLocationProbe#setLocations(java.util.ArrayList)
	 */
	@Override
	public void setLocations(ArrayList<String> locations) {
		super.setLocations(locations);
	}
}