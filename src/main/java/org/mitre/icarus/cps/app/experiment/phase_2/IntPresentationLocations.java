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
package org.mitre.icarus.cps.app.experiment.phase_2;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.mitre.icarus.cps.app.widgets.phase_2.experiment.LocationDescriptor;
import org.mitre.icarus.cps.exam.phase_2.testing.DatumType;

/**
 * Contains lists of locations that each INT has been presented at.
 * 
 * @author CBONACETO
 *
 */
public class IntPresentationLocations {
	
	/** Locations that OSINT has been presented at */
	private Set<String> osintPresentationLocations = new HashSet<String>();
	
	/** Locations that IMINT has been presented at */
	private Set<String> imintPresentationLocations = new HashSet<String>();		
	
	/** Locations that HUMINT has been presented at */
	private Set<String> humintPresentationLocations = new HashSet<String>();
	
	/** Locations that SIGINT has been presented at */
	private Set<String> sigintPresentationLocations = new HashSet<String>();
	
	/** Locations that SIGINT was selected at */
	private Set<String> sigintSelectionLocations = new HashSet<String>();
	
	/** Clear all INT presentations and selections */
	public void clearAllIntPresentationAndSelections() {
		osintPresentationLocations.clear();
		imintPresentationLocations.clear();
		humintPresentationLocations.clear();
		sigintPresentationLocations.clear();
		sigintSelectionLocations.clear();
	}
	
	/**
	 * Mark that an INT was presented at a location.
	 * 
	 * @param intType
	 * @param locationId
	 */
	public void setIntPresented(DatumType intType, String locationId) {
		switch(intType) {			
		case OSINT:
			osintPresentationLocations.add(locationId);
			break;
		case IMINT:
			imintPresentationLocations.add(locationId);
			break;
		case HUMINT:
			humintPresentationLocations.add(locationId);
			break;			
		case SIGINT:
			sigintPresentationLocations.add(locationId);
			break;
		default: 
			break;
		}
	}
	
	/**
	 * Get whether an INT was presented at a location.
	 * 
	 * @param intType
	 * @param locationId
	 */
	public boolean isIntPresented(DatumType intType, String locationId) {
		switch(intType) {			
		case OSINT:
			return osintPresentationLocations.contains(locationId);
		case IMINT:
			return imintPresentationLocations.contains(locationId);
		case HUMINT:
			return humintPresentationLocations.contains(locationId);			
		case SIGINT:
			return sigintPresentationLocations.contains(locationId);
		default: 
			return false;
		}
	}
	
	public void clearSigintSelections() {
		sigintSelectionLocations.clear();
	}
	
	/**
	 * Mark that SIGINT was selected at a location.
	 * 
	 * @param locationId
	 */
	public void setSigintSelected(String locationId) {
		sigintSelectionLocations.add(locationId);
	}
	
	/**
	 * Mark that SIGINT was selected at the given locations.
	 * 
	 * @param locations
	 * @param clearPreviousSelections
	 */
	public void setSigintSelected(Collection<LocationDescriptor> locations, boolean clearPreviousSelections) {
		if(clearPreviousSelections) {
			sigintSelectionLocations.clear();
		}
		if(locations != null && !locations.isEmpty()) {
			for(LocationDescriptor location : locations) {
				sigintSelectionLocations.add(location.getLocationId());
			}
		}
	}
	
	/**
	 * Get whether SIGINT was selected at a location.
	 * 
	 * @param locationId
	 * @return
	 */
	public boolean isSigintSelected(String locationId) {
		return sigintSelectionLocations.contains(locationId);
	}
}