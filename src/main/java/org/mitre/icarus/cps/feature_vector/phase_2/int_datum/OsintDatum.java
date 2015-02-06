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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.phase_2.testing.DatumType;
import org.mitre.icarus.cps.feature_vector.phase_2.coordinate.GeoCoordinate;

/**
 * Contains OSINT information at a location, which is the probability that Blue will defeat Red at the location ("P"), 
 * based on the distance of the location from the Blue border.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="OsintDatum", namespace="IcarusCPD_2")
public class OsintDatum extends IntDatum {
	
	/** The point on the Blue region border closest to the Blue location */
	protected GeoCoordinate closestBlueRegionPoint;
	
	/** Normalized radius (between 0-1) from the Blue location to the closes Blue region point */
	protected Double radius;
	
	/** The probability that Blue will defeat Red if Red attacks ("P"), a measure of Red's vulnerability,
	 * based on distance of the blue location to blue border */
	protected Double redVulnerability_P;
	
	/**
	 * Construct an empty OsintDatum.
	 */
	public OsintDatum() {}
	
	/**
	 * Construct an OSINTDatum with the given closest blue border point and "P" value.
	 * 
	 * @param closestBlueRegionPoint the point on the Blue border closest to the Blue location
	 * @param redVulnerability_P the "P" value at the location, a measure of Red's vulnerability based on distance of the blue location to blue border
	 */
	public OsintDatum(GeoCoordinate closestBlueRegionPoint, Double redVulnerability_P) {
		this.closestBlueRegionPoint = closestBlueRegionPoint;
		this.redVulnerability_P = redVulnerability_P;
	}

	/**
	 * Get the point on the Blue border closest to the Blue location.
	 * 
	 * @return the closest Blue border point
	 */
	@XmlElement(name="BlueRegionCoordinate")
	public GeoCoordinate getClosestBlueRegionPoint() {
		return closestBlueRegionPoint;
	}

	/**
	 * Set the point on the Blue border closest to the Blue location.
	 * 
	 * @param closestBlueRegionPoint the closest Blue border point
	 */
	public void setClosestBlueRegionPoint(GeoCoordinate closestBlueRegionPoint) {
		this.closestBlueRegionPoint = closestBlueRegionPoint;
	}

	/**
	 * Get the normalized radius distance (between 0-1) from the Blue location to the closes Blue region point.
	 * This distance is used to compute the "P" value.
	 * 
	 * @return the normalized radius
	 */
	@XmlAttribute(name="radius")
	public Double getRadius() {
		return radius;
	}

	/**
	 * Set the normalized radius distance (between 0-1) from the Blue location to the closes Blue region point.
	 * This distance is used to compute the "P" value.
	 * 
	 * @param radius the normalized radius
	 */
	public void setRadius(Double radius) {
		this.radius = radius;
	}

	/**
	 * Get the "P" value at the location, a measure of Red's vulnerability based on distance of the blue location to blue border.
	 * 
	 * @return the "P" value at the location
	 */
	@XmlAttribute(name="vulnerability")	
	public Double getRedVulnerability_P() {
		return redVulnerability_P;
	}

	/**
	 * Set the "P" value at the location, a measure of Red's vulnerability based on distance of the blue location to blue border.
	 * 
	 * @param redVulnerability_P the "P" value at the location
	 */
	public void setRedVulnerability_P(Double redVulnerability_P) {
		this.redVulnerability_P = redVulnerability_P;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.IDatum#getDatumType()
	 */
	@Override
	public DatumType getDatumType() {
		return DatumType.OSINT;
	}
}