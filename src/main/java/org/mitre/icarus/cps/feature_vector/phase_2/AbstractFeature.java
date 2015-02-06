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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import de.micromata.opengis.kml.v_2_2_0.AbstractObject;

/**
 * Abstract implementation of the Phase 2 feature interface. Provides getter and setter for the feature ID. 
 * All features support serialization to an equivalent KML representation.
 * 
 * @author CBONACETO
 *
 * @param <T>
 */
@XmlType(name="AbstractFeature", namespace="IcarusCPD_2")
@XmlSeeAlso({LinearRing.class, LineString.class, Point.class, Polygon.class})
public abstract class AbstractFeature<T extends AbstractObject> implements IFeature<T> {

	/** The feature ID */
	protected String id;

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.feature_vector.phase_2.IFeature#getId()
	 */
	@XmlAttribute(name="id")
	@Override
	public String getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.feature_vector.phase_2.IFeature#setId(java.lang.String)
	 */
	@Override
	public void setId(String id) {
		this.id = id;
	}
}