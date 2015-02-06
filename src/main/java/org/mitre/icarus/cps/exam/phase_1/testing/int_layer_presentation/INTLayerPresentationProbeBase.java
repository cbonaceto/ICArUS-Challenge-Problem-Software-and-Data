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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Abstract base class for INT layer presentation probes.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="IntLayerPresentationProbeBase", namespace="IcarusCPD_1")
public abstract class INTLayerPresentationProbeBase {
	
	/** The INT layer to present (Tasks 4-5), select (Task 6), or purchase (Task 7) */
	protected IntLayer layerType;	
	
	/**
	 * No arg constructor
	 */
	public INTLayerPresentationProbeBase() {}
	
	/**
	 * Constructor that takes the INT layer type to present, select, or purchase.
	 * 
	 * @param layerType the INT layer type
	 */
	public INTLayerPresentationProbeBase(IntLayer layerType) {
		this.layerType = layerType;
	}

	/**
	 * Get the INT layer type to present, select, or purchase.
	 * 
	 * @return the INT layer type
	 */
	@XmlElement(name="LayerType")
	public IntLayer getLayerType() {
		return layerType;
	}

	/**
	 * Set the INT layer type to present, select, or purchase.
	 * 
	 * @param layerType the INT layer type
	 */
	public void setLayerType(IntLayer layerType) {
		this.layerType = layerType;
	}	
}