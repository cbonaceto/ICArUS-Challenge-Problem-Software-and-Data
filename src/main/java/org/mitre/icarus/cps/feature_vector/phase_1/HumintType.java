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

/**
 * Distance from a point.
 * 
 * @author Lily Wong
 *
 */
public class HumintType {
	
	/** The distance. */
	private Integer distance;
	
	/** The source point. */
	private GridLocation2D sourcePoint;
	
	/**
	 * Instantiates a new humint type.
	 *
	 * @param distance the distance
	 * @param sourcePoint the source point
	 */
	public HumintType(GridLocation2D sourcePoint, Integer distance) {
		this.distance = distance;
		this.sourcePoint = sourcePoint;
	}
	
	/**
	 * Gets the distance.
	 *
	 * @return the distance
	 */
	public Integer getDistance() {
		return distance;
	}
	
	/**
	 * Sets the distance.
	 *
	 * @param distance the new distance
	 */
	public void setDistance(Integer distance) {
		this.distance = distance;
	}
	
	/**
	 * Gets the source point.
	 *
	 * @return the source point
	 */
	public GridLocation2D getSourcePoint() {
		return sourcePoint;
	}

	@Override
	public String toString() {
		return "HumintType [distance=" + distance + "]";
	}
}