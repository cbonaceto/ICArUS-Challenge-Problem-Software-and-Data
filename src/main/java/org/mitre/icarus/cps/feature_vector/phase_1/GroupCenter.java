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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Contains information about the location of a group center.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="GroupCenter", namespace="IcarusCPD_1")
public class GroupCenter extends Phase1Feature {
	
	/** The group. */
	private GroupType group;
	
	/** The Constant icons. */
	@SuppressWarnings({ "serial" })
	private static final HashMap<String,String> icons = new HashMap<String,String>() {
		{
			put( GroupType.A.toString(), "#A" );
			put( GroupType.B.toString(), "#B" );
			put( GroupType.C.toString(), "#C" );
			put( GroupType.D.toString(), "#D" );
			
			put( SigintType.Silent.toString(),  "#Silent" );
			put( SigintType.Chatter.toString(), "#Chatter" );
		}
	};
	
	/**
	 * Instantiates an empty new group center.
	 */
	public GroupCenter() {}
	
	/**
	 * Instantiates a new group center.
	 *
	 * @param group the group
	 * @param centerLocation the group center location
	 */
	public GroupCenter(GroupType group, GridLocation2D centerLocation) {
		this( group, centerLocation, null );
	}
	
	/**
	 * Instantiates a new group center.
	 *
	 * @param group the group
	 * @param centerLocation the group center location
	 * @param locationIntel the location intel report
	 */
	public GroupCenter(GroupType group, GridLocation2D centerLocation, LocationIntelReport locationIntel) {
		super( group.toString(), centerLocation, locationIntel );
		this.group = group;
	}

	/**
	 * Gets the group.
	 *
	 * @return the group
	 */
	@XmlAttribute(name="group")
	public GroupType getGroup() {
		return group;
	}

	/**
	 * Sets the group.
	 *
	 * @param group the new group
	 */
	public void setGroup(GroupType group) {
		this.group = group;
	}	
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.feature_vector.phase_1.Phase1Feature#getLocation()
	 */
	@XmlElement(name="CenterLocation")
	@Override
	public GridLocation2D getLocation() {
		return super.getLocation();
	}	

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.feature_vector.phase_1.Phase1Feature#setLocation(org.mitre.icarus.cps.feature_vector.phase_1.GridLocation2D)
	 */
	@Override
	public void setLocation(GridLocation2D location) {
		super.setLocation(location);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GroupCenter [group=" + group + ", location=" + location
				+ ", intelReport=" + intelReport + "]";
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.feature_vector.phase_1.Phase1Feature#getIcons()
	 */
	@Override
	public HashMap<String, String> getIcons() {
		return icons;
	}
}