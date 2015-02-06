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

import java.util.ArrayList;
import java.util.Iterator;

import de.micromata.opengis.kml.v_2_2_0.Coordinate;

/**
 * Defines a SOCINT region as a polygon. This format is not currently used to represent SOCINT regions,
 * the @see SocintOverlay format is currently used. 
 * 
 * @author CBONACETO 
 *
 */
public class SocintPolygon {
	
	/** The group this SOCINT region is for. */
	protected GroupType group;
	
	/** The SOCINT boundary coordinates for SOCINT regions specified as polygons. */
	protected ArrayList<GridLocation2D> vertices;
	
	/**
	 * Instantiates a new socint region.
	 *
	 * @param id the id
	 */
	public SocintPolygon( String id ) {
		this.group = GroupType.valueOf(id);
		vertices = new ArrayList<GridLocation2D>();
	}

	/**
	 * Gets the group.
	 *
	 * @return the group
	 */
	public GroupType getGroup() {
		return group;
	}

	/**
	 * Sets the group.
	 *
	 * @param group the new group
	 */
	public void setGroup(GroupType group) {
		this.group = group;
	}

	/**
	 * Gets the vertices.
	 *
	 * @return the vertices
	 */
	public ArrayList<GridLocation2D> getVertices() {
		return vertices;
	}

	/**
	 * Sets the vertices.
	 *
	 * @param vertices the new vertices
	 */
	public void setVertices(ArrayList<GridLocation2D> vertices) {
		this.vertices = vertices;
	}	
	
	/**
	 * Gets the vertices as a KML coordinate list.
	 *
	 * @return the coordinate list
	 */
	public ArrayList<Coordinate> getCoordinateList() {
		Iterator<GridLocation2D> iter = vertices.iterator();
		ArrayList<Coordinate> coordinates = new ArrayList<Coordinate>();
		while ( iter.hasNext() ) {
			coordinates.add( iter.next().toKmlCoordinate() );
		}
		// sort the vertices to ensure a square
		
		// last coordinate must be the same as first, to "close" 
		coordinates.add( coordinates.get(0));
		return coordinates;
	}
}