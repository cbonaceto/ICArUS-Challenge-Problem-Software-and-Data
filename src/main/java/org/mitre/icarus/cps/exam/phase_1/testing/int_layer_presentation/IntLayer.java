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
package org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation;

import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.feature_vector.phase_1.IntType;

/**
 * Abstract base class for INT layer types.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="IntLayer", namespace="IcarusCPD_1")
@XmlSeeAlso({ImintLayer.class, MovintLayer.class, SigintLayer.class, SocintLayer.class})
public abstract class IntLayer {
	
	/**
	 * Get the INT layer type.
	 * 
	 * @return the INT type
	 */
	public abstract IntType getLayerType();	
}