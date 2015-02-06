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
 * Contains the expected utility of choosing an INT layer.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="INTLayerExpectedUtility", namespace="IcarusCPD_1", 
propOrder={"expectedUtility_cumulativeBayesian", "expectedUtility_incrementalBayesian", "expectedUtility_subjectProbs", "layerType"})
public class INTLayerExpectedUtility {
	
	/** The INT layer type */
	protected IntLayer layerType;
	
	/** The expected utility of selecting the INT layer given the current cumulative Bayesian probabilities */
	protected Double expectedUtility_cumulativeBayesian;
	
	/** The expected utility of selecting the INT layer given the current incremental Bayesian probabilities */
	protected Double expectedUtility_incrementalBayesian;
	
	/** The expected utility of selecting the INT layer given the current subject/model probabilities */
	protected Double expectedUtility_subjectProbs;
	
	public INTLayerExpectedUtility() {}
	
	public INTLayerExpectedUtility(IntLayer layerType, Double expectedUtility_cumulativeBayesian,
			Double expectedUtility_incrementalBayesian, Double expectedUtility_subjectProbs) {
		this.layerType = layerType;
		this.expectedUtility_cumulativeBayesian = expectedUtility_cumulativeBayesian; 
		this.expectedUtility_incrementalBayesian = expectedUtility_incrementalBayesian;
		this.expectedUtility_subjectProbs = expectedUtility_subjectProbs; 
	}
	
	/**
	 * Get the layer type.
	 * 
	 * @return the layer type
	 */
	@XmlElement(name="LayerType")
	public IntLayer getLayerType() {
		return layerType;
	}

	/**
	 * Set the layer type.
	 * 
	 * @param layerType the layer type
	 */
	public void setLayerType(IntLayer layerType) {
		this.layerType = layerType;
	}

	/**
	 * Get the expected utility of selecting the INT layer given the current cumulative Bayesian probabilities.
	 * 
	 * @return the expected utility
	 */
	@XmlAttribute(name="expectedUtility_cumulative")
	public Double getExpectedUtility_cumulativeBayesian() {
		return expectedUtility_cumulativeBayesian;
	}

	/**
	 * Set the expected utility of selecting the INT layer given the current cumulative Bayesian probabilities.
	 * 
	 * @param expectedUtility_cumulativeBayesian the expected utility
	 */
	public void setExpectedUtility_cumulativeBayesian(Double expectedUtility_cumulativeBayesian) {
		this.expectedUtility_cumulativeBayesian = expectedUtility_cumulativeBayesian;
	}

	/**
	 * Get the expected utility of selecting the INT layer given the current incremental Bayesian probabilities.
	 * 
	 * @return the expected utility
	 */
	@XmlAttribute(name="expectedUtility_incremental")
	public Double getExpectedUtility_incrementalBayesian() {
		return expectedUtility_incrementalBayesian;
	}

	/**
	 * Set the expected utility of selecting the INT layer given the current incremental Bayesian probabilities.
	 * 
	 * @param expectedUtility_incrementalBayesian the expected utility
	 */
	public void setExpectedUtility_incrementalBayesian(Double expectedUtility_incrementalBayesian) {
		this.expectedUtility_incrementalBayesian = expectedUtility_incrementalBayesian;
	}

	/**
	 * Get the expected utility of selecting the INT layer given the current subject/model probabilities.
	 * 
	 * @return the expected utility
	 */
	@XmlAttribute(name="expectedUtility_subjectProbs")
	public Double getExpectedUtility_subjectProbs() {
		return expectedUtility_subjectProbs;
	}

	/**
	 * Set the expected utility of selecting the INT layer given the current subject/model probabilities.
	 * 
	 * @param expectedUtility_subjectProbs the expected utility
	 */
	public void setExpectedUtility_subjectProbs(Double expectedUtility_subjectProbs) {
		this.expectedUtility_subjectProbs = expectedUtility_subjectProbs;
	}	
}