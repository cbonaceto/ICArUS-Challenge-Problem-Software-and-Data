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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.feature_vector.phase_1.GroupType;

/**
 * Abstract base class for probes that elicit a response about one or more groups.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="MultiGroupProbe", namespace="IcarusCPD_1")
@XmlSeeAlso({AttackLocationProbe_MultiGroup.class, GroupCirclesProbe.class, GroupCentersProbe.class,
	TroopAllocationProbe_MultiGroup.class})
public abstract class MultiGroupProbe extends TrialPartProbe {
	
	/** The groups to probe */
	protected ArrayList<GroupType> groups;
	
	/**
	 * No arg constructor.
	 */
	protected MultiGroupProbe() {}
	
	/**
	 * Constructor that takes the groups to probe.
	 * 
	 * @param groups the groups to probe
	 */
	protected MultiGroupProbe(ArrayList<GroupType> groups) {
		this.groups = groups;
	}
	
	/**
	 * Get the groups to probe.
	 * 
	 * @return the groups to probe
	 */
	@XmlTransient
	public ArrayList<GroupType> getGroups() {
		return groups;
	}
	
	/**
	 * Set the groups to probe.
	 * 
	 * @param groups the groups to probe
	 */
	public void setGroups(ArrayList<GroupType> groups) {
		this.groups = groups;
	}		

	/**
	 * 
	 * 
	 * @author CBONACETO
	 *
	 */
	public static class GroupList {
		/** The ID for this group list */
		protected String listId;
		
		/** The groups */
		protected ArrayList<GroupType> groups;
		
		public GroupList() {}
		
		public GroupList(String listId) {
			this.listId = listId;
		}
		
		public GroupList(ArrayList<GroupType> groups) {
			this.groups = groups;
		}
		
		public GroupList(String listId, ArrayList<GroupType> groups) {
			this.listId = listId;
			this.groups = groups;
		}

		@XmlAttribute(name="listId")		
		public String getListId() {
			return listId;
		}

		public void setListId(String listId) {
			this.listId = listId;
		}

		@XmlElement(name="Groups")
		@XmlList	
		public ArrayList<GroupType> getGroups() {
			return groups;
		}

		public void setGroups(ArrayList<GroupType> groups) {
			this.groups = groups;
		}		
	}
}