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
package org.mitre.icarus.cps.feature_vector.phase_2.int_datum;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.phase_2.testing.DatumType;

/**
 * Contains IMINT information at a location, which is the utility of attack for Red ("U"), 
 * based on the building density at the location.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="ImintDatum", namespace="IcarusCPD_2")
public class ImintDatum extends IntDatum {	
	/** The minimum U value */
	public static final int MIN_U = 2;
	
	/** The maximum U value */
	public static final int MAX_U = 5;
	
	/** Radius about which building density was computed */
	protected Double radius_miles;
	
	/** Building density at the location */
	protected Double buildingDensity;
	
	/** The utility of attack for Red, based on building density at the location (U) (2 - 5) */
	protected Integer redOpportunity_U;	
	
	/**
	 * Construct an empty ImintDatum.
	 */
	public ImintDatum() {}
	
	/**
	 * Construct an ImintDatum with the given radius (in miles), building density, and "U" value.
	 * 
	 * @param radius_miles the radius (in miles) over which building density was computed around the location
	 * @param buildingDensity The building density at the location. A value in the range [0 1].
	 * @param redOpportunity_U the utility of attack for Red ("U"), based on the building density at the location
	 */
	public ImintDatum(Double radius_miles, Double buildingDensity, Integer redOpportunity_U) {
		this.radius_miles = radius_miles;
		this.buildingDensity = buildingDensity;
		this.redOpportunity_U = redOpportunity_U;
	}

	/**
	 * Get the radius (in miles) over which building density was computed around the location.
	 * 
	 * @return the radius in miles
	 */
	@XmlAttribute(name="radius_miles")
	public Double getRadius_miles() {
		return radius_miles;
	}

	/**
	 * Set the radius (in miles) over which building density was computed around the location.
	 * 
	 * @param radius_miles the radius in miles
	 */
	public void setRadius_miles(Double radius_miles) {
		this.radius_miles = radius_miles;
	}

	/**
	 * Get the building density at the location. A value in the range [0 1].
	 * 
	 * @return the building density
	 */
	@XmlAttribute(name="density")
	public Double getBuildingDensity() {
		return buildingDensity;
	}

	/**
	 * Set the building density at the location. A value in the range [0 1].
	 * 
	 * @param buildingDensity the building density
	 */
	public void setBuildingDensity(Double buildingDensity) {
		this.buildingDensity = buildingDensity;
	}

	/**
	 * Get the utility of attack for Red ("U"), based on the building density at the location.
	 * 
	 * @return the "U" value
	 */
	@XmlAttribute(name="opportunity")
	public Integer getRedOpportunity_U() {
		return redOpportunity_U;
	}

	/**
	 * Set the utility of attack for Red ("U"), based on the building density at the location.
	 * 
	 * @param redOpportunity_U the "U" value
	 */
	public void setRedOpportunity_U(Integer redOpportunity_U) {
		this.redOpportunity_U = redOpportunity_U;
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.IDatum#getDatumType()
	 */
	@Override
	public DatumType getDatumType() {
		return DatumType.IMINT;
	}
}