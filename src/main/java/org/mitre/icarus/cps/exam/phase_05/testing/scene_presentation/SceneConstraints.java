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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

/**
 * Defines the min/max possible number of scene items in the overall scene and 
 * in each sector.
 * 
 * FOR HUMAN SUBJECT USE ONLY.
 * 
 * @author CBONACETO
 *
 */
@XmlType(namespace="IcarusCPD_05")
public class SceneConstraints {
	/** The minimum number of facilities present in the scene */
	protected Integer minFacilitiesInScene;
	
	/** The maximum number of facilities present in the scene */
	protected Integer maxFacilitiesInScene;
	
	/** The minimum number of objects present in the scene */
	protected Integer minObjectsInScene;
	
	/** The maximum number of objects present in the scene */
	protected Integer maxObjectsInScene;
	
	/** The minimum number of events present in the scene */
	protected Integer minEventsInScene;
	
	/** The maximum number of events present in the scene */
	protected Integer maxEventsInScene;
	
	protected ArrayList<SectorConstraints> sectorConstraints;

	@XmlElement(name="MinFacilitiesInScene")
	public Integer getMinFacilitiesInScene() {
		return minFacilitiesInScene;
	}

	public void setMinFacilitiesInScene(Integer minFacilitiesInScene) {
		this.minFacilitiesInScene = minFacilitiesInScene;
	}

	@XmlElement(name="MaxFacilitiesInScene")
	public Integer getMaxFacilitiesInScene() {
		return maxFacilitiesInScene;
	}

	public void setMaxFacilitiesInScene(Integer maxFacilitiesInScene) {
		this.maxFacilitiesInScene = maxFacilitiesInScene;
	}

	@XmlElement(name="MinObjectsInScene")
	public Integer getMinObjectsInScene() {
		return minObjectsInScene;
	}

	public void setMinObjectsInScene(Integer minObjectsInScene) {
		this.minObjectsInScene = minObjectsInScene;
	}

	@XmlElement(name="MaxObjectsInScene")
	public Integer getMaxObjectsInScene() {
		return maxObjectsInScene;
	}

	public void setMaxObjectsInScene(Integer maxObjectsInScene) {
		this.maxObjectsInScene = maxObjectsInScene;
	}

	@XmlElement(name="MinEventsInScene")
	public Integer getMinEventsInScene() {
		return minEventsInScene;
	}

	public void setMinEventsInScene(Integer minEventsInScene) {
		this.minEventsInScene = minEventsInScene;
	}

	@XmlElement(name="MaxEventsInScene")
	public Integer getMaxEventsInScene() {
		return maxEventsInScene;
	}

	public void setMaxEventsInScene(Integer maxEventsInScene) {
		this.maxEventsInScene = maxEventsInScene;
	}

	@XmlElementWrapper(name="SectorConstraints")
	@XmlElement(name="SectorConstraint")
	public ArrayList<SectorConstraints> getSectorConstraints() {
		return sectorConstraints;
	}

	public void setSectorConstraints(ArrayList<SectorConstraints> sectorConstraints) {
		this.sectorConstraints = sectorConstraints;
	}
}
