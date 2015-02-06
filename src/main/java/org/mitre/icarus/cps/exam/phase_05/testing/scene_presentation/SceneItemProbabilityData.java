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
package org.mitre.icarus.cps.exam.phase_05.testing.scene_presentation;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * Probability data for scene items being present in a scene.
 * 
 * FOR HUMAN SUBJECT USE ONLY.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="SceneItemProbability", namespace="IcarusCPD_05", propOrder={"itemId", "probability"})
public class SceneItemProbabilityData {
	/** The scene item ID */
	protected Integer itemId;

	/** The probability the scene item is present (normalized) */
	protected Double probability;
	
	/** The probability the scene item is present (raw, not normalized) */
	protected Double rawProbability;

	public SceneItemProbabilityData() {}
	
	public SceneItemProbabilityData(Integer itemId, Double probability) {
		this(itemId, probability, null);
	}

	public SceneItemProbabilityData(Integer itemId, Double probability, Double rawProbability) {
		this.itemId = itemId;
		this.probability = probability;
		this.rawProbability = rawProbability;
	}

	@XmlAttribute(name="ItemId", required=true)
	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	@XmlAttribute(name="Probability", required=true)
	public Double getProbability() {
		return probability;
	}

	public void setProbability(Double probability) {
		this.probability = probability;
	}
	
	@XmlAttribute(name="RawProbability")
	public Double getRawProbability() {
		return rawProbability;
	}

	public void setRawProbability(Double rawProbability) {
		this.rawProbability = rawProbability;
	}
}
