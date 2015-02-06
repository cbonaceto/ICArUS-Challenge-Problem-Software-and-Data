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
 * Response for a single IdentifyItem trial.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="IdentifyItemResponse", namespace="IcarusCPD_05", 
		propOrder={"sectorID", "sceneItemProbabilityData"})
public class IdentifyItemTrialResponse extends ScenePresentationTrialResponse {
	/** The ID of the sector that was probed */
	private Integer sectorID;
	
	/** The probabilities for each scene item that was probed is present 
	 * in the sector that was probed after each layer presentation or selection*/
	private List<SceneItemProbabilityResponseData> sceneItemProbabilityData;

	/**
	 * Get the ID of the sector that was probed 
	 * 
	 * @return
	 */
	@XmlElement(name="SectorId", required=true)
	public Integer getSectorID() {
		return sectorID;
	}

	/**
	 * Set the ID of the sector that was probed 
	 * 
	 * @param sectorID
	 */
	public void setSectorID(Integer sectorID) {
		this.sectorID = sectorID;
	}
	
	/**
	 * Get the probabilities for each scene item that was probed is present 
	 * in the sector that was probed after each layer presentation or selection. 
	 * 
	 * @return
	 */
	@XmlElement(name="SceneItemProbabilityResponse")
	public List<SceneItemProbabilityResponseData> getSceneItemProbabilityData() {
		return sceneItemProbabilityData;
	}

	/**
	 * Set the probabilities for each scene item that was probed is present 
	 * in the sector that was probed after each layer presentation or selection.
	 * 
	 * @param sceneItemProbabilityData
	 */
	public void setSceneItemProbabilityData(
			List<SceneItemProbabilityResponseData> sceneItemProbabilityData) {
		this.sceneItemProbabilityData = sceneItemProbabilityData;
	}	
}
