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
import java.util.LinkedList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * @author CBONACETO
 *
 */
@XmlType(name="LayerPresentation", namespace="IcarusUIStudy",
propOrder={"hits", "hitsPerSecond", "instructionText", "itemProbabilities"})
public class SpatialPresentationBlock {
	
	/** The instruction text for the block (if any) */	
	protected String instructionText;
	
	/** The total number of points in the spatial distribution */
	protected Integer hits;
	
	/** The locations of each point in the spatial distribution for each item */
	protected ArrayList<ArrayList<Point>> hitLocations;
	
	/** The ordered sequence to visit each quadrant when showing hit locations */
	protected LinkedList<Integer> quadrantVisitSequence;
	
	/** The number of points to show per second */
	protected Integer hitsPerSecond;
	
	/** The time to blank the display before entering probabilities */
	protected Integer blankSeconds = 2;
	
	/** The spatial distribution for each item */
	protected ArrayList<ItemProbability> itemProbabilities;	

	@XmlElement(name="InstructionText")
	public String getInstructionText() {
		return instructionText;
	}

	public void setInstructionText(String instructionText) {
		this.instructionText = instructionText;
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
	
	@XmlTransient	
	public LinkedList<Integer> getQuadrantVisitSequence() {
		return quadrantVisitSequence;
	}

	public void setQuadrantVisitSequence(LinkedList<Integer> quadrantVisitSequence) {
		this.quadrantVisitSequence = quadrantVisitSequence;
	}

	@XmlAttribute(name="HitsPerSecond")
	public Integer getHitsPerSecond() {
		return hitsPerSecond;
	}

	public void setHitsPerSecond(Integer hitsPerSecond) {
		this.hitsPerSecond = hitsPerSecond;
	}
	
	@XmlAttribute(name="BlankSeconds")
	public Integer getBlankSeconds() {
		return blankSeconds;
	}

	public void setBlankSeconds(Integer blankSeconds) {
		this.blankSeconds = blankSeconds;
	}

	public ArrayList<String> getItemProbabilityIds() {
		if(itemProbabilities != null && !itemProbabilities.isEmpty()) {
			ArrayList<String> ids = new ArrayList<String>(itemProbabilities.size());
			for(ItemProbability item : itemProbabilities) {
				ids.add(item.getItemId());
			}
			return ids;
		}
		return null;
	}

	@XmlElementWrapper(name="ProbabilityData")
	@XmlElement(name="ItemProbability")
	public ArrayList<ItemProbability> getItemProbabilities() {
		return itemProbabilities;
	}

	public void setItemProbabilities(ArrayList<ItemProbability> itemProbabilities) {
		this.itemProbabilities = itemProbabilities;
	}
}