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

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

/**
 * Contains the list of items present in a sector.
 * FOR HUMAN SUBJECT USE ONLY.
 * 
 * @author CBONACETO
 *
 */
@XmlType(namespace="IcarusCPD_05")
public class SectorTruthData {
	/** The ID of the sector the truth data is for */
	protected Integer sectorID;
	
	/** The IDs of each scene item present in the sector */
	protected List<Integer> sceneItemsPresent;
	
	public SectorTruthData() {}
	
	public SectorTruthData(Integer sectorID) {
		this.sectorID = sectorID;
	}

	@XmlAttribute(name="SectorID")
	public Integer getSectorID() {
		return sectorID;
	}

	public void setSectorID(Integer sectorID) {
		this.sectorID = sectorID;
	}

	@XmlElementWrapper(name="SceneItemsPresent")
	@XmlElement(name="SceneItem")
	public List<Integer> getSceneItemsPresent() {
		return sceneItemsPresent;
	}

	public void setSceneItemsPresent(List<Integer> sceneItemsPresent) {
		this.sceneItemsPresent = sceneItemsPresent;
	}	
}
