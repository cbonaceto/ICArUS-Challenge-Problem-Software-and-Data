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
package org.mitre.icarus.cps.app.experiment.ui_study.exam;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlType;

/**
 * @author CBONACETO
 *
 */
@XmlType(name="ProbabilityResponse", namespace="IcarusUIStudy")
public class ProbabilityResponse {
	
	/** The probability responses for each item */
	protected List<ItemProbabilityResponse> itemProbabilityResponses;
	
	/** The normative probabilities for each item */
	protected List<Double> normativeProbabilities;
	
	/** Total time spent setting probabilities */
	protected Long time_ms;
	
	@XmlElement(name="ItemProbabilityResponse")
	public List<ItemProbabilityResponse> getItemProbabilityResponses() {
		return itemProbabilityResponses;
	}

	public void setItemProbabilityResponses(List<ItemProbabilityResponse> itemProbabilityResponses) {
		this.itemProbabilityResponses = itemProbabilityResponses;
	}
	
	@XmlElement(name="NormativeProbabilities")
	@XmlList
	public List<Double> getNormativeProbabilities() {
		return normativeProbabilities;
	}

	public void setNormativeProbabilities(List<Double> normativeProbabilities) {
		this.normativeProbabilities = normativeProbabilities;
	}

	@XmlAttribute(name="time_ms")
	public Long getTime_ms() {
		return time_ms;
	}

	public void setTime_ms(Long time_ms) {
		this.time_ms = time_ms;
	}
}