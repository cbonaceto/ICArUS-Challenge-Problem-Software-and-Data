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
 * Contains a location identifier and the Blue action at the location.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="BlueAction", namespace="IcarusCPD_2")
public class BlueAction {		
	
	/** Blue action types (divert, not divert) */
	@XmlType(name="BlueActionType", namespace="IcarusCPD_2")
	public static enum BlueActionType {Divert, Do_Not_Divert};
	
	/** The location ID */
	protected String locationId;
	
	/** The location index */
	protected Integer locationIndex;
	
	/** The Blue action at the location (divert, not divert) */
	protected BlueActionType action;	
	
	/**
	 * Construct an empty BlueAction.
	 */
	public BlueAction() {}
	
	/**
	 * Construct a BlueAction with the given locationId, locationIndex, and action.
	 * 
	 * @param locationId the location ID
	 * @param locationIndex the location index
	 * @param action the Blue action taken at the location
	 */
	public BlueAction(String locationId, Integer locationIndex, BlueActionType action) {
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
	 * Get the Blue action taken at the location.
	 * Response data provided by the participant (except in Mission 1).
	 * 
	 * @return the Blue action taken
	 */
	@XmlAttribute(name="action")
	public BlueActionType getAction() {
		return action;
	}

	/**
	 * Set the Blue action taken at the location.
	 * Response data provided by the participant (except in Mission 1).
	 * 
	 * @param action the Blue action taken
	 */
	public void setAction(BlueActionType action) {
		this.action = action;
	}	

	/**
	 * Get the string representation of the given Blue action ("Diver" or "Not Divert").
	 * 
	 * @param action the Blue action
	 * @return the string representation of the Blue action
	 */
	public static String getBlueActionString(BlueActionType action) {
		if(action == null) {
			return "";
		} else {
			switch(action) {
			case Divert:
				return "Divert";
			case Do_Not_Divert:
				return "Not Divert";
			default:
				return "";
			}
		}
	}
}