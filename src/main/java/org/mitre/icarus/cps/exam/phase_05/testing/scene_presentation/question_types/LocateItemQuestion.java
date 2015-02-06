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
 * A locate item question.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="LocateItem", namespace="IcarusCPD_05")
public class LocateItemQuestion extends Question {
	/** The ID of the scene item to locate */
	private Integer sceneItemToProbe;
	
	/** The IDs of each sector to probe */
	private List<Integer> sectorsToProbe;

	/**
	 * Get the ID of the scene item to locate.
	 * 
	 * @return
	 */
	@XmlElement(name="SceneItemToProbe", required=true)	
	public Integer getSceneItemToProbe() {
		return sceneItemToProbe;
	}

	/**
	 * Set the ID of the scene item to locate.
	 * 
	 * @param sceneItemToProbe
	 */
	public void setSceneItemToProbe(Integer sceneItemToProbe) {
		this.sceneItemToProbe = sceneItemToProbe;
	}

	/**
	 * Get the IDs of each sector to probe. 
	 * 
	 * @return
	 */
	@XmlElementWrapper(name="SectorsToProbe", required=true)
	@XmlElement(name="SectorId")
	public List<Integer> getSectorsToProbe() {
		return sectorsToProbe;
	}

	/**
	 * Set the IDs of each sector to probe.
	 * 
	 * @param sectorsToProbe
	 */
	public void setSectorsToProbe(List<Integer> sectorsToProbe) {
		this.sectorsToProbe = sectorsToProbe;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.testing.scene_presentation.question_types.Question#getQuestionType()
	 */
	@Override
	public QuestionType getQuestionType() {
		return QuestionType.LocateItem;
	}
}
