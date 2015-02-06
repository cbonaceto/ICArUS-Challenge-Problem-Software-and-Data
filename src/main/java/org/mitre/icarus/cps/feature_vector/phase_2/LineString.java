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

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.feature_vector.phase_2.coordinate.GeoCoordinate;


/**
 * A line feature. A line is a series of connected coordinates.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="LineString", namespace="IcarusCPD_2")
@XmlSeeAlso({Road.class})
public class LineString extends AbstractFeature<de.micromata.opengis.kml.v_2_2_0.LineString> {

	/** The line coordinates */
	protected List<GeoCoordinate> coordinates;

	/**
	 * Get the line coordinates.
	 * 
	 * @return the line coordinates
	 */
	@XmlElement(name = "Coordinate")
	public List<GeoCoordinate> getCoordinates() {
		return coordinates;
	}

	/**
	 * Set the line coordinates.
	 * 
	 * @param coordinates the line coordinates
	 */
	public void setCoordinates(List<GeoCoordinate> coordinates) {
		this.coordinates = coordinates;
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.feature_vector.phase_2.IFeature#getKMLGeometry()
	 */
	@Override
	public de.micromata.opengis.kml.v_2_2_0.LineString getKMLGeometry() {
		de.micromata.opengis.kml.v_2_2_0.LineString lineString = 
				new de.micromata.opengis.kml.v_2_2_0.LineString();
		List<de.micromata.opengis.kml.v_2_2_0.Coordinate> kmlCoordinates =
				lineString.createAndSetCoordinates();
		for (GeoCoordinate geoCoordinate : coordinates) {
			kmlCoordinates.add( geoCoordinate.getKMLRepresentation() );
		}
		return lineString;
	}
	
	/**
	 * Get a KML placemark object containing this line string.
	 * 
	 * @return the KML placemark containing this line string
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