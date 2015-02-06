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
package org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.IntLayer;

/**
 * Subject/model response after an INT layer is purchased in a Task 7 trial.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="Task_7_INTLayerPurchase", namespace="IcarusCPD_1",
		propOrder={"costCredits", "intLayer"})
public class Task_7_INTLayerPurchase extends TrialPartResponse {
	
	/** The INT layer that was purchased */
	protected IntLayer intLayer;
	
	/** The cost of the INT layer (credits) */
	protected Integer costCredits;
	
	/**
	 * No arg constructor.
	 */
	public Task_7_INTLayerPurchase() {}
	
	/**
	 * Constructor that takes the intLayer and costCredits.
	 * 
	 * @param intLayer the INT layer that was purchased
	 * @param costCredits the cost (in credits) of the INT layer
	 */
	public Task_7_INTLayerPurchase(IntLayer intLayer, Integer costCredits) {
		this.intLayer = intLayer;
		this.costCredits = costCredits;
	}	

	/**
	 * Get the INT layer that was purchased.
	 * 
	 * @return the INT layer
	 */
	@XmlElement(name="INTLayer")
	public IntLayer getIntLayer() {
		return intLayer;
	}

	/**
	 * Set the INT layer that was purchased.
	 * 
	 * @param intLayer the INT layer
	 */
	public void setIntLayer(IntLayer intLayer) {
		this.intLayer = intLayer;
	}

	/**
	 * Get the cost (in credits) of the INT layer.
	 * 
	 * @return the cost (in credits)
	 */
	@XmlAttribute(name="costCredits")
	public Integer getCostCredits() {
		return costCredits;
	}

	/**
	 * Set the cost (in credits) of the INT layer.
	 * 
	 * @param costCredits the cost (in credits)
	 */
	public void setCostCredits(Integer costCredits) {
		this.costCredits = costCredits;
	}	
}