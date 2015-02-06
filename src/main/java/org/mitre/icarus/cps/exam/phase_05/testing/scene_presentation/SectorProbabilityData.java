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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Contains the probability that a set of scene items is present in a sector.
 * FOR HUMAN SUBJECT USE ONLY.
 * 
 * @author CBONACETO
 *
 */
@XmlType(namespace="IcarusCPD_05")
public class SectorProbabilityData {
	/** The ID of the sector the probability data is for */
	protected Integer sectorID;
	
	/** The probability of each facility, object, or event being present in the sector */
	protected ArrayList<SceneItemProbabilityData> sceneItemProbabilityData;
	
	/** Maps each scene item to each probability */
	private HashMap<Integer, Double> sceneItemProbabilityDataMap;
	
	public SectorProbabilityData() {}
	
	public SectorProbabilityData(Integer sectorID) {
		this.sectorID = sectorID;
	}
	
	/** Get the probability data for each scene item being present in the sector. */
	public ArrayList<Double> getProbabilityForAllSceneItems(List<Integer> sceneItems) {
		if(sceneItemProbabilityDataMap == null) {
			//Initialize sceneItemProbabilityDataMap
			initSceneItemProbabilityDataMap();
		}
		
		if(sceneItems == null) {
			return null;
		}
		
		ArrayList<Double> probabilities = new ArrayList<Double>(sceneItems.size());	
		for(Integer sceneItemId : sceneItems) {
			probabilities.add(sceneItemProbabilityDataMap.get(sceneItemId));
		}
		return probabilities;
	}
	
	/** Get the probability for the given scene item being present in the sector. */
	public Double getProbabilityForSceneItem(Integer sceneItemId) {
		if(sceneItemProbabilityDataMap == null) {
			//Initialize sceneItemProbabilityDataMap
			initSceneItemProbabilityDataMap();
		}
		
		return sceneItemProbabilityDataMap.get(sceneItemId);
	}
	
	/** Initialize sceneItemProbabilityDataMap */
	public void initSceneItemProbabilityDataMap() {
		sceneItemProbabilityDataMap = new HashMap<Integer, Double>();
		if(sceneItemProbabilityData != null) {
			for(SceneItemProbabilityData sceneItemProbability : sceneItemProbabilityData) {
				sceneItemProbabilityDataMap.put(sceneItemProbability.getItemId(), 
						sceneItemProbability.getProbability());
			}
		}
	}
	
	@XmlAttribute(name = "SectorID")
	public Integer getSectorID() {
		return sectorID;
	}

	public void setSectorID(Integer sectorID) {
		this.sectorID = sectorID;
	}
	
	@XmlElement(name="SceneItemProbability")
	public ArrayList<SceneItemProbabilityData> getSceneItemProbabilityData() {
		return sceneItemProbabilityData;
	}
	
	public void setSceneItemProbabilityData(ArrayList<SceneItemProbabilityData> sceneItemProbabilityData) {
		this.sceneItemProbabilityData = sceneItemProbabilityData;
		sceneItemProbabilityDataMap = null;
	}	
}
