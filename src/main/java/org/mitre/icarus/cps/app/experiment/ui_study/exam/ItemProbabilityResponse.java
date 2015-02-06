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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.app.widgets.probability_entry.TimeData;

@XmlType(name="ItemProbabilityResponse", namespace="IcarusUIStudy")
public class ItemProbabilityResponse {
	
	/** The item ID */
	protected String itemId;
	
	/** The probability (normalized) */
	protected Double probability;
	
	/** The probability (raw) */
	protected Double rawProbability;
	
	/** The total time spent on this probability judgment (e.g., time subject spent adjusting probability setting) (milliseconds) */
	protected Long time_ms;
	
	/** Contains detailed timing data about user interaction times with various probability entry modalities */
	protected TimeData interactionTimeData;
	
	public ItemProbabilityResponse() {}
	
	public ItemProbabilityResponse(String itemId, Double probability, Double rawProbability) {
		this.itemId = itemId;
		this.probability = probability;
		this.rawProbability = rawProbability;
	}	

	@XmlAttribute(name="ItemId")
	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	@XmlAttribute(name="probability")
	public Double getProbability() {
		return probability;
	}

	public void setProbability(Double probability) {
		this.probability = probability;
	}

	@XmlAttribute(name="rawProbability")
	public Double getRawProbability() {
		return rawProbability;
	}

	public void setRawProbability(Double rawProbability) {
		this.rawProbability = rawProbability;
	}

	@XmlAttribute(name="time_ms")
	public Long getTime_ms() {
		return time_ms;
	}

	public void setTime_ms(Long time_ms) {
		this.time_ms = time_ms;
	}

	@XmlElement(name="InteractionTimeData")
	public TimeData getInteractionTimeData() {
		return interactionTimeData;
	}

	public void setInteractionTimeData(TimeData interactionTimeData) {
		this.interactionTimeData = interactionTimeData;
	}	
}