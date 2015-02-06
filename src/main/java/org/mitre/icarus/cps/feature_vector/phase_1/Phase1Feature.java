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

import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * Describes a feature associated with a location.
 * 
 * @author Lily Wong
 *
 */
@XmlType(name="Feature", namespace="IcarusCPD_1")
public abstract class Phase1Feature {

	/** The id. */
	protected String id; 
	
	/** The location of this feature. */
	protected GridLocation2D location;
	
	/** Intel information at this location (e.g., facility type, SIGINT chatter, etc) */
	protected LocationIntelReport intelReport;
	
	/**
	 * Instantiates an empty new phase1 feature.
	 */
	public Phase1Feature() {}

	/**
	 * Instantiates a new phase1 feature.
	 *
	 * @param id the object id
	 * @param location the location
	 * @param intelReport the intel report
	 */
	public Phase1Feature(String id, GridLocation2D location, LocationIntelReport intelReport) {
		this.id = id;
		this.location = location;
		this.intelReport = intelReport;
	}
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Gets the location.
	 *
	 * @return the location
	 */
	@XmlTransient
	public GridLocation2D getLocation() {
		return location;
	}	

	/**
	 * Sets the location.
	 *
	 * @param location the new location
	 */
	public void setLocation(GridLocation2D location) {
		this.location = location;
	}

	/**
	 * Gets the intel report.
	 *
	 * @return the intel report
	 */
	@XmlTransient
	public LocationIntelReport getIntelReport() {
		return intelReport;
	}
	
	/**
	 * Checks if the intel report exists.
	 *
	 * @return true, if successful
	 */
	public boolean hasIntelReport() {
		return (intelReport != null);
	}
	
	/**
	 * Gets the KML style icons.
	 *
	 * @return the icons
	 */
	public abstract HashMap<String,String> getIcons();
}