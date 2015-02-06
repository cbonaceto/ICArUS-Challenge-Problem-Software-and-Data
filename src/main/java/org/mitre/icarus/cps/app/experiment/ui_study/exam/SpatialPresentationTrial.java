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

import java.awt.Point;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * @author CBONACETO
 *
 */
@XmlType(name="SpatialPresentation", namespace="IcarusUIStudy")
public class SpatialPresentationTrial extends SingleBlockTrial {

	/** The total number of points in the spatial distribution */
	protected Integer hits;
	
	/** The locations of each point in the spatial distribution for each item */
	protected ArrayList<ArrayList<Point>> hitLocations;
	
	@XmlElementWrapper(name="UIDefaults")
	@XmlElement(name="ItemProbability")
	@Override
	public ArrayList<ItemProbability> getUiDefaults() {
		return uiDefaults;
	}

	@Override
	public void setUiDefaults(ArrayList<ItemProbability> uiDefaults) {
		this.uiDefaults = uiDefaults;
	}

	@XmlElementWrapper(name="ProbabilityData")
	@XmlElement(name="ItemProbability")
	@Override
	public ArrayList<ItemProbability> getItemProbabilities() {
		return itemProbabilities;
	}

	@Override
	public void setItemProbabilities(ArrayList<ItemProbability> itemProbabilities) {
		this.itemProbabilities = itemProbabilities;
	}

	@XmlAttribute(name="Hits")
	public Integer getHits() {
		return hits;
	}

	public void setHits(Integer hits) {
		this.hits = hits;
	}

	@XmlTransient
	public ArrayList<ArrayList<Point>> getHitLocations() {
		return hitLocations;
	}

	public void setHitLocations(ArrayList<ArrayList<Point>> hitLocations) {
		this.hitLocations = hitLocations;
	}

	@XmlElement(name="TrialResponse")
	@Override
	public SingleBlockTrialResponse getTrialResponse() {
		return trialResponse;
	}

	@Override
	public void setTrialResponse(SingleBlockTrialResponse trialResponse) {
		this.trialResponse = trialResponse;
	}	
}