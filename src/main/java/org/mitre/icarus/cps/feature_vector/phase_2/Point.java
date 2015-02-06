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
package org.mitre.icarus.cps.feature_vector.phase_2;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.feature_vector.phase_2.coordinate.GeoCoordinate;

/**
 * A geographic feature defined as a point on the earth.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="Point", namespace="IcarusCPD_2")
@XmlSeeAlso({BlueLocation.class})
public class Point extends AbstractFeature<de.micromata.opengis.kml.v_2_2_0.Point> {
	
	/** Location of the point */
	protected GeoCoordinate location;	

	/**
	 * Get the point location.
	 * 
	 * @return the point location
	 */
	@XmlElement(name = "Coordinate")
	public GeoCoordinate getLocation() {
		return location;
	}

	/**
	 * Set the point location.
	 * 
	 * @param location the point location
	 */
	public void setLocation(GeoCoordinate location) {
		this.location = location;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.feature_vector.phase_2.IFeature#getKMLGeometry()
	 */
	@Override
	public de.micromata.opengis.kml.v_2_2_0.Point getKMLGeometry() {
		de.micromata.opengis.kml.v_2_2_0.Point point = 
				new de.micromata.opengis.kml.v_2_2_0.Point();
		point.createAndSetCoordinates().add(location.getKMLRepresentation());
		return point;
	}
	
	/**
	 * Get a KML placemark object containing the point.
	 * 
	 * @return a KML placemark object
	 */
	public de.micromata.opengis.kml.v_2_2_0.Placemark getKMLRepresentation() {
		de.micromata.opengis.kml.v_2_2_0.Placemark placemark = 
				new de.micromata.opengis.kml.v_2_2_0.Placemark();
		placemark.setId(id);
		placemark.setName(id);
		placemark.setGeometry(getKMLGeometry());
		return placemark;
	}
}