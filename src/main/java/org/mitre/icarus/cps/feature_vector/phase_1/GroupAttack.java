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
package org.mitre.icarus.cps.feature_vector.phase_1;

import java.util.HashMap;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * Contains information about the location of a group attack.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="GroupAttack", namespace="IcarusCPD_1")
public class GroupAttack extends Phase1Feature implements Comparable<GroupAttack> {	
	
	/** The group responsible for the attack (if known). */
	private GroupType group;	
	
	/** The Constant icons. */
	@SuppressWarnings({ "serial" })
	private static final HashMap<String,String> icons = new HashMap<String, String>() {
		{
			put( GroupType.A.toString(), "#blue" );
			put( GroupType.B.toString(), "#red" );
			put( GroupType.C.toString(), "#green" );
			put( GroupType.D.toString(), "#yellow" );
			put( GroupType.X.toString(), "#dark-grey" );
			put( GroupType.O.toString(), "#light-grey" );
		}
	};
	
	/**
	 * Instantiates an empty new group attack.
	 */
	public GroupAttack() {}
	
	public GroupAttack(GroupType group) {
		this(group, null, null);
	}
	
	/**
	 * Instantiates a new group attack.
	 *
	 * @param group the group
	 * @param attackLocation the attack location
	 */
	public GroupAttack(GroupType group, GridLocation2D attackLocation) {
		this(group, attackLocation, null);
	}

	/**
	 * Instantiates a new group attack.
	 *
	 * @param group the group
	 * @param attackLocation the attack location
	 * @param locationIntel the location intel
	 */
	public GroupAttack(GroupType group, GridLocation2D attackLocation, LocationIntelReport locationIntel) {
		super( (group != null) ? group.toString() : attackLocation.getLocationId(), attackLocation, locationIntel );
		this.group = group;
	}

	/**
	 * Gets the group responsible for the attack.
	 *
	 * @return the group
	 */
	@XmlAttribute(name="Group")
	public GroupType getGroup() {
		return group;
	}

	/**
	 * Sets the group responsible for the attack.
	 *
	 * @param group the new group
	 */
	public void setGroup(GroupType group) {
		this.group = group;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GroupAttack [group=" + group + ", id=" + id + ", location="
				+ location + ", intelReport=" + intelReport + "]";
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.feature_vector.phase_1.Phase1Feature#getIcons()
	 */
	@Override
	public HashMap<String, String> getIcons() {
		return icons;
	}

	@Override
	public int compareTo(GroupAttack o) {
		if(o != null) {
			if(this.group != null && o.group != null) {
				return this.group.compareTo(o.group);
			}
			else {
				if(this.location != null && this.location.getLocationId() != null) {
					if(o.location != null && o.location.getLocationId() != null) {
						return this.location.getLocationId().compareTo(o.location.getLocationId());
					} else {
						return 1;
					}
				} else {
					if(o.location != null && o.location.getLocationId() != null) {
						return -1;
					} else {
						return 0;
					}
				}
			}
		}		
		return 1;
	}
}