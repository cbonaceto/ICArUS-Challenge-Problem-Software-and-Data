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
 * A probe trial where the subject/model is asked to draw circles
 * representing the "two-to-one boundary" for a group.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="GroupCirclesProbe", namespace="IcarusCPD_1")
public class GroupCirclesProbe extends MultiGroupProbe {
	
	/**
	 * No arg constructor.
	 */
	public GroupCirclesProbe() {}	
	
	/**
	 * Constructor that takes the groups to probe.
	 * 
	 * @param groups the groups
	 */
	public GroupCirclesProbe(ArrayList<GroupType> groups) {
		super(groups);
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