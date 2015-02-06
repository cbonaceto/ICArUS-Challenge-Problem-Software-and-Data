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
package org.mitre.icarus.cps.exam.phase_05.testing.scene_presentation.question_types;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;



/**
 * An identify item question.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="IdentifyItem", namespace="IcarusCPD_05")
public class IdentifyItemQuestion extends Question {
	/** The ID of the sector to probe */
	private Integer sectorToProbe;
	
	/** The ids of each scene item to probe */
	private List<Integer> sceneItemsToProbe;

	/**
	 * Get the ID of the sector to probe.  
	 * 
	 * @return
	 */
	@XmlElement(name="SectorToProbe", required=true)
	public Integer getSectorToProbe() {
		return sectorToProbe;
	}

	/**
	 * Set the ID of the sector to probe.
	 * 
	 * @param sectorToProbe
	 */
	public void setSectorToProbe(Integer sectorToProbe) {
		this.sectorToProbe = sectorToProbe;
	}

	/**
	 * Get the IDs of each scene item to probe.
	 * 
	 * @return the IDs of each scene item
	 */
	@XmlElementWrapper(name="SceneItemsToProbe", required=true)
	@XmlElement(name="ItemId")
	public List<Integer> getSceneItemsToProbe() {
		return sceneItemsToProbe;
	}

	/**
	 * Set the IDs of each scene item to probe.
	 * 
	 * @param sceneItemsToProbe 
	 */
	public void setSceneItemsToProbe(List<Integer> sceneItemsToProbe) {
		this.sceneItemsToProbe = sceneItemsToProbe;
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.testing.scene_presentation.question_types.Question#getQuestionType()
	 */
	@Override
	public QuestionType getQuestionType() {
		return QuestionType.IdentifyItem;
	}
}
