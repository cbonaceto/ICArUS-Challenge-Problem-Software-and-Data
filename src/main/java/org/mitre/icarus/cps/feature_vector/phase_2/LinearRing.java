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
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.feature_vector.phase_2.coordinate.GeoCoordinate;

/**
 * A linear ring feature. A linear ring is a series of connect coordinates that enclose an area. 
 * The last coordinate connects to the first coordinate.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="LinearRing", namespace="IcarusCPD_2")
public class LinearRing extends AbstractFeature<de.micromata.opengis.kml.v_2_2_0.LinearRing> {
	
	/** The linear ring coordinates */
	protected List<GeoCoordinate> coordinates;

	/**
	 * Get the linear ring coordinates. The last coordinate in the list connects to the first coordinate.
	 * 
	 * @return the linear ring coordinates
	 */
	@XmlElement(name = "Coordinate")
	public List<GeoCoordinate> getCoordinates() {
		return coordinates;
	}

	/**
	 * Set the linear ring coordinates. The last coordinate in the list connects to the first coordinate.
	 * 
	 * @param coordinates the linear ring coordinates
	 */
	public void setCoordinates(List<GeoCoordinate> coordinates) {
		this.coordinates = coordinates;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.feature_vector.phase_2.IFeature#getKMLGeometry()
	 */
	@Override
	public de.micromata.opengis.kml.v_2_2_0.LinearRing getKMLGeometry() {
		de.micromata.opengis.kml.v_2_2_0.LinearRing linearRing = 
				new de.micromata.opengis.kml.v_2_2_0.LinearRing();
		List<de.micromata.opengis.kml.v_2_2_0.Coordinate> kmlCoordinates =
				linearRing.createAndSetCoordinates();
		for (GeoCoordinate geoCoordinate : coordinates) {
			kmlCoordinates.add(geoCoordinate.getKMLRepresentation());
		}
		return linearRing;
	}
}