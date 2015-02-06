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
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.feature_vector.phase_2.coordinate.GeoCoordinate;


/**
 * A polygon is defined is define by an outer boundary (linear ring) and may contain 0
 * or more nested inner boundaries (also linear rings).
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="Polygon", namespace="IcarusCPD_2")
@XmlSeeAlso({Region.class, Building.class, Terrain.class})
public class Polygon extends AbstractFeature<de.micromata.opengis.kml.v_2_2_0.Polygon> {
	
	/** The polygon outer boundary vertices */
	protected LinearRing outerBoundary;
	
	/** Any nested inner boundaries */
	protected List<LinearRing> innerBoundary;

	/**
	 * Get the polygon outer boundary vertices.
	 * 
	 * @return 
	 */
	@XmlElement(name="OuterBounds")
	public LinearRing getOuterBoundary() {
		return outerBoundary;
	}

	/**
	 * Set the polygon outer boundary vertices.
	 * 
	 * @param outerBoundary the polygon outer boundary vertices
	 */
	public void setOuterBoundary(LinearRing outerBoundary) {
		this.outerBoundary = outerBoundary;
	}

	/**
	 * Get any nested inner boundaries.
	 * 
	 * @return any nested inner boundaries
	 */
	@XmlElement(name="InnerBounds")
	public List<LinearRing> getInnerBoundary() {
		return innerBoundary;
	}

	/**
	 * Set any nested inner boundaries.
	 * 
	 * @param innerBoundary any nested inner boundaries
	 */
	public void setInnerBoundary(List<LinearRing> innerBoundary) {
		this.innerBoundary = innerBoundary;
	}

	/**
	 * Convenience method to get the coordinates of the outer boundary (a linear ring).
	 * 
	 * @return the coordinates of the outer boundary
	 */
	@XmlTransient
	public List<GeoCoordinate> getVertices() {
		if(outerBoundary != null) {
			return outerBoundary.getCoordinates();
		}
		return null;
	}
	
	/**
	 * Convenience method to set the coordinates of the outer boundary (a linear ring).
	 * 
	 * @param vertices the coordinates of the outer boundary
	 */
	public void setVertices(List<GeoCoordinate> vertices) {
		if(outerBoundary == null) {
			outerBoundary = new LinearRing();
		}
		outerBoundary.setCoordinates(vertices);				
	}

	@Override
	/**
	 * Only sets outer boundary.
	 */
	public de.micromata.opengis.kml.v_2_2_0.Polygon getKMLGeometry() {
		de.micromata.opengis.kml.v_2_2_0.Polygon poly = 
				new de.micromata.opengis.kml.v_2_2_0.Polygon();
		de.micromata.opengis.kml.v_2_2_0.Boundary boundary = poly.createAndSetOuterBoundaryIs();
		boundary.setLinearRing(outerBoundary.getKMLGeometry());
		return poly;
	}
	
	/**
	 * Get a KML placemark object containing this polygon.
	 * 
	 * @return a KML placemark object containing this polygon.
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