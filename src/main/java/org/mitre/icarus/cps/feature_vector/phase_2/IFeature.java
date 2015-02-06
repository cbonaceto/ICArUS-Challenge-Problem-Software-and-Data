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

/**
 * Interface for Phase 2 feature objects. All features support serialization to an equivalent KML representation 
 * and have a unique feature ID.
 * 
 * @author CBONACETO
 *
 */
public interface IFeature<T extends de.micromata.opengis.kml.v_2_2_0.AbstractObject> {
	
	/**
	 * Get the feature ID.
	 * 
	 * @return the feature ID
	 */
	public String getId();
	
	/**
	 * Set the feature ID.
	 * 
	 * @param id the feature ID
	 */
	public void setId(String id);
	
	/**
	 * Get the KML representation of the feature.
	 * 
	 * @return the KML representation of the feature
	 */
	public T getKMLGeometry();
	
	
	//public T getKMLRepresentation();	
}