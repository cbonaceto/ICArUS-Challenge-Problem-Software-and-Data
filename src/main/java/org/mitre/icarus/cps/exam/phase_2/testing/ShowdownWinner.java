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
package org.mitre.icarus.cps.exam.phase_2.testing;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * Contains the pre-determined player who will win if there is a show-down at a location.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="ShowdownWinner", namespace="IcarusCPD_2")
public class ShowdownWinner {
	/** The location Id */
	protected String locationId;
	
	/** The location index */
	protected Integer locationIndex;
	
	/** The player (Blue or Red) who will win if there is a showdown at the location */
	protected PlayerType showdownWinner;
	
	/**
	 * Construct an empty ShowdownWinner.
	 */
	public ShowdownWinner() {}
	
	/**
	 * Construct a ShowdownWinner with the given locationId, locationIndex, and showdownWinner.
	 * 
	 * @param locationId the location ID
	 * @param locationIndex the location index
	 * @param showdownWinner the player who will win the showdown at the location (if there is a showdown).
	 */
	public ShowdownWinner(String locationId, Integer locationIndex, PlayerType showdownWinner) {
		this.locationId = locationId;
		this.locationIndex = locationIndex;
		this.showdownWinner = showdownWinner;
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
	 * Get the player who will win the showdown at the location (if there is showdown).
	 * 
	 * @return the showdown winner
	 */
	@XmlAttribute(name="winner")
	public PlayerType getShowdownWinner() {
		return showdownWinner;
	}

	/**
	 * Set the player who will win the showdown at the location (if there is showdown).
	 * 
	 * @param showdownWinner the showdown winner
	 */
	public void setShowdownWinner(PlayerType showdownWinner) {
		this.showdownWinner = showdownWinner;
	}
}