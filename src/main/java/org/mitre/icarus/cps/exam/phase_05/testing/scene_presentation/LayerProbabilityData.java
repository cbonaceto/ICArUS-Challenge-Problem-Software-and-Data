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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.phase_05.testing.scene_presentation.layer_presentation_types.LayerList;

/**
 * Contains probability data for each scene item being present in each sector given that 
 * each layer(s) in layers have been shown.
 * 
 * FOR HUMAN SUBJECT USE ONLY.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="LayerProbabilityData", namespace="IcarusCPD_05")
public class LayerProbabilityData {
	/** The layers shown */
	private LayerList layers;
	
	/** The probability data for each sector (indicates the probability that each type of facility,
	 * object, or event is present in each sector) given the layers shown. */
	ArrayList<SectorProbabilityData> sectorProbabilityData;
	
	/** A maps each sector to the probability data for that sector */
	private HashMap<Integer, SectorProbabilityData> sectorProbabilityDataMap;
	
	public LayerProbabilityData() {}
	
	public LayerProbabilityData(LayerList layers) {
		this.layers = layers;
	}
	
	/** Get the probability data for each scene item being present in the given sector. */
	public ArrayList<Double> getProbabilityDataForSector(Integer sectorId, List<Integer> sceneItems) {
		if(sectorProbabilityDataMap == null) {
			//Initialize sectorProbabilityDataMap
			initSectorProbabilityDataMap();
		}
		
		SectorProbabilityData sectorData = sectorProbabilityDataMap.get(sectorId);
		if(sectorData != null) {
			return sectorData.getProbabilityForAllSceneItems(sceneItems);
		}		
		return null;
	}
	
	/** Get the probability for the given scene item being present in each sector. */
	public ArrayList<Double> getProbabilityDataForSceneItem(Integer sceneItemId, List<Integer> sectors) {
		if(sectorProbabilityDataMap == null) {
			//Initialize sectorProbabilityDataMap
			initSectorProbabilityDataMap();
		}
		
		if(sectors == null) {
			return null;
		}
		
		ArrayList<Double> probabilities = new ArrayList<Double>(sectors.size());
		for(Integer sectorId : sectors) {
			SectorProbabilityData sectorData = sectorProbabilityDataMap.get(sectorId);
			if(sectorData != null) {
				probabilities.add(sectorData.getProbabilityForSceneItem(sceneItemId));
			}	
			else {
				probabilities.add(null);
			}
		}
		return probabilities;
	}
	
	/** Initialize sectorProbabilityDataMap */
	public void initSectorProbabilityDataMap() {
		sectorProbabilityDataMap = new HashMap<Integer, SectorProbabilityData>();
		if(sectorProbabilityData != null) {
			for(SectorProbabilityData sectorProbability : sectorProbabilityData) {
				sectorProbabilityDataMap.put(sectorProbability.getSectorID(), 
						sectorProbability);
			}
		}
	}
	
	@XmlElement(name="LayersShown")
	public LayerList getLayers() {
		return layers;
	}

	public void setLayers(LayerList layers) {
		this.layers = layers;
	}	
	
	@XmlElement(name="ProbabilitiesForSector")
	public ArrayList<SectorProbabilityData> getSectorProbabilityData() {
		return sectorProbabilityData;
	}	

	public void setSectorProbabilityData(
			ArrayList<SectorProbabilityData> sectorProbabilityData) {
		this.sectorProbabilityData = sectorProbabilityData;
		sectorProbabilityDataMap = null;
	}
}
