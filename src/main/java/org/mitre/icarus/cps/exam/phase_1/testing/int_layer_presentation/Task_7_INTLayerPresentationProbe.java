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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * Contains the INT layer and cost for an INT layer available for purchase in Task 7. 
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="Task_7_IntLayerPresentationProbe", namespace="IcarusCPD_1")
public class Task_7_INTLayerPresentationProbe extends INTLayerPresentationProbeBase {
	
	/** The cost (in credits) of the INT layer */
	protected Integer costCredits;
	
	/**
	 * No arg constructor.
	 */
	public Task_7_INTLayerPresentationProbe() {}
	
	/**
	 * Constructor that takes the INT layerType and costCredits of the INT layer.
	 * 
	 * @param layerType the INT layer type
	 * @param costCredits the cost in credits to purchase the layer
	 */
	public Task_7_INTLayerPresentationProbe(IntLayer layerType,
			Integer costCredits) {		
		super(layerType);
		this.costCredits = costCredits;
	}
	
	/**
	 * Get the INT layer cost in credits.
	 * 
	 * @return the cost in credits
	 */
	@XmlAttribute(name="costCredits")
	public Integer getCostCredits() {
		return costCredits;
	}

	/**
	 *  Set the INT layer cost in credits.
	 * 
	 * @param costCredits the cost in credits
	 */
	public void setCostCredits(Integer costCredits) {
		this.costCredits = costCredits;
	}
}