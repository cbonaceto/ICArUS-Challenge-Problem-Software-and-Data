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
package org.mitre.icarus.cps.exam.base.training;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author CBONACETO
 *
 */
@XmlType(name="TutorialNavigationTree", namespace="IcarusCPD_Base")
public class TutorialNavigationTree {
	
	/** The tutorial name */
	protected String tutorialName;
	
	/** The top-level nodes */
	protected List<TutorialTreeParentNode> tutorialSections;
	
	public TutorialNavigationTree() {}
	
	public TutorialNavigationTree(String tutorialName) {
		this(tutorialName, null);
	}
	
	public TutorialNavigationTree(List<TutorialTreeParentNode> tutorialSections) {
		this(null, tutorialSections);
	}
	
	public TutorialNavigationTree(String tutorialName, List<TutorialTreeParentNode> tutorialSections) {
		this.tutorialName = tutorialName;
		this.tutorialSections = tutorialSections;
	}

	//TODO: Add to schema
	@XmlElement(name="TutorialName")
	public String getTutorialName() {
		return tutorialName;
	}

	public void setTutorialName(String tutorialName) {
		this.tutorialName = tutorialName;
	}

	@XmlElement(name="TutorialSection")
	public List<TutorialTreeParentNode> getTutorialSections() {
		return tutorialSections;
	}

	public void setTutorialSections(List<TutorialTreeParentNode> tutorialSections) {
		this.tutorialSections = tutorialSections;
	}
}