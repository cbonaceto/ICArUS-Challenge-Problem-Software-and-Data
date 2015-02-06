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
package org.mitre.icarus.cps.exam.phase_05.response;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;



/**
 * Response for a single LocateItem trial.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="LocateItemResponse", namespace="IcarusCPD_05")
public class LocateItemTrialResponse extends ScenePresentationTrialResponse {
	/** The ID of the scene item that was probed */
	private Integer sceneItemID;
	
	/** The probabilities that the scene item that was probed
	 *  was present in each each sector that was probed after 
	 *  each layer presentation or selection */
	private List<SectorProbabilityResponseData> sectorProbabilityData;

	/**
	 * Get the ID of the scene item that was probed.
	 * 
	 * @return
	 */
	@XmlElement(name="SceneItemId", required=true)
	public Integer getSceneItemID() {
		return sceneItemID;
	}

	/**
	 * Set the ID of the scene item that was probed
	 * 
	 * @param sceneItemID
	 */
	public void setSceneItemID(Integer sceneItemID) {
		this.sceneItemID = sceneItemID;
	}
	
	/**
	 * Get the probabilities that the scene item that was probed
	 * was present in each each sector that was probed after 
	 * each layer presentation or selection
	 * 
	 * @return
	 */
	@XmlElement(name="SectorProbabilityResponse")
	public List<SectorProbabilityResponseData> getSectorProbabilityData() {
		return sectorProbabilityData;
	}

	/**
	 * Set the probabilities that the scene item that was probed
	 * was present in each each sector that was probed after 
	 * each layer presentation or selection
	 * 
	 * @param sectorProbabilityData
	 */
	public void setSectorProbabilityData(
			List<SectorProbabilityResponseData> sectorProbabilityData) {
		this.sectorProbabilityData = sectorProbabilityData;
	}
}

