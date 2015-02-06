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
package org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * Contains a location identifier and the Red action at the location.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="RedAction", namespace="IcarusCPD_2")
public class RedAction {
	/** Red action types (attack, not attack) */
	@XmlType(name="RedActionType", namespace="IcarusCPD_2")
	public static enum RedActionType {Attack, Do_Not_Attack};
	
	/** The location ID */
	protected String locationId;
	
	/** The location index */
	protected Integer locationIndex;
	
	/** The Red action at the location (attack, do not attack) */
	protected RedActionType action;
	
	
	/**
	 * Construct an empty RedAction. 
	 */
	public RedAction() {}
	
	/**
	 * Construct a RedAction with the given locationId, locationIndex, and action.
	 * 
	 * @param locationId the location ID
	 * @param locationIndex the location index
	 * @param action the Red action taken at the location
	 */
	public RedAction(String locationId, Integer locationIndex, RedActionType action) {
		this.locationId = locationId;
		this.locationIndex = locationIndex;
		this.action = action;
	}

	/**
	 * Get the location ID.
	 * 
	 * @return the location ID
	 */
	@XmlAttribute(name="locationId")
	public String getLocationId() {
		return locationId;
	}

	/**
	 * Set the location ID.
	 * 
	 * @param locationId the location ID
	 */
	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	/**
	 * Get the location index.
	 * 
	 * @return the location index
	 */
	@XmlAttribute(name="locationIndex")
	public Integer getLocationIndex() {
		return locationIndex;
	}

	/**
	 * Set the location index.
	 * 
	 * @param locationIndex the location index
	 */
	public void setLocationIndex(Integer locationIndex) {
		this.locationIndex = locationIndex;
	}

	/**
	 * Get the Red action taken at the location.
	 * 
	 * @return the Red action taken
	 */
	@XmlAttribute(name="action")
	public RedActionType getAction() {
		return action;
	}

	/**
	 * Set the Red action taken at the location.
	 * 
	 * @param action the Red action taken
	 */
	public void setAction(RedActionType action) {
		this.action = action;
	}
	
	/**
	 * Get the string representation of the given Red action ("Attack" or "Not Attack").
	 * 
	 * @param action the Red action
	 * @return the string representation of the Red action
	 */
	public static String getRedActionString(RedActionType action) {
		switch(action) {
		case Attack:
			return "Attack";
		case Do_Not_Attack:
			return "Not Attack";
		default:
			return "";
		}		
	}
}