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
package org.mitre.icarus.cps.app.widgets.phase_2.experiment;

/**
 * Contains the ID and index of a Blue location. The location index gives the number of the location (e.g., 0, 1) in an ordered list of locations.
 * The location index is only used by the GUI to keep track of locations.
 * 
 * @author CBONACETO
 *
 */
public class LocationDescriptor {
	/** The location Id */
	protected final String locationId;
	
	/** The location index */
	protected final Integer locationIndex;
	
	/**
	 * Construct a new LocationDescriptor with the given location ID and location index.
	 * 
	 * @param locationId
	 * @param locationIndex
	 */
	public LocationDescriptor(String locationId, Integer locationIndex) {
		this.locationId = locationId;
		this.locationIndex = locationIndex;
	}

	public String getLocationId() {
		return locationId;
	}		

	public Integer getLocationIndex() {
		return locationIndex;
	}

	@Override
	public String toString() {
		return "[locationId: " + locationId + ", locationIndex: " + locationIndex + "]";
	}	
}