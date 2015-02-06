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
 * The Enum FeatureType.
 *
 * @author Lily Wong
 */
public enum FeatureType {	
	Task,
	Road,
	Region,
	Socint;
	
	/**
	 * Checks if this FeatureType is a task.
	 *
	 * @return true, if is task
	 */
	public boolean isTask() {
		return this == Task;
	}
	
	/**
	 * Checks if this FeatureType is a road.
	 *
	 * @return true, if is road
	 */
	public boolean isRoad() {
		return this == Road;
	}
	
	/**
	 * Checks if this FeatureType is a region.
	 *
	 * @return true, if is region
	 */
	public boolean isRegion() {
		return this == Region;
	}
	
	/**
	 * Checks if this FeatureType is a socint.
	 *
	 * @return true, if is socint
	 */
	public boolean isSocint() {
		return this == Socint;
	}
}