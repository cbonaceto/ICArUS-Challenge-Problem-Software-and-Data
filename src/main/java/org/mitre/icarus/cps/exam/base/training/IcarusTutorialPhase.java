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
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.base.IcarusExamPhase;
import org.mitre.icarus.cps.exam.phase_1.training.TutorialPhase;
import org.mitre.icarus.cps.exam.phase_2.training.IcarusExamTutorial_Phase2;
import org.mitre.icarus.cps.experiment_core.condition.InstructionsPage;

/**
 * Abstract base class for tutorial phases in an exam. A tutorial phase contains a series of tutorial pages.
 * FOR HUMAN SUBJECT USE ONLY.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="IcarusTutorialPhase", namespace="IcarusCPD_Base")
@XmlSeeAlso({TutorialPhase.class, IcarusExamTutorial_Phase2.class})
public class IcarusTutorialPhase<T extends InstructionsPage> extends IcarusExamPhase {
	
	/** The tutorial page */
	protected List<T> tutorialPages;
	
	/** The tutorial navigation tree */
	protected TutorialNavigationTree tutorialNavigationTree;
	
	/**
	 * Get the tutorial pages.
	 * 
	 * @return the tutorial pages
	 */
	@XmlTransient
	public List<T> getTutorialPages() {
		return tutorialPages;
	}

	/**
	 * Set the tutorial pages.
	 * 
	 * @param tutorialPages the tutorial pages
	 */
	public void setTutorialPages(List<T> tutorialPages) {
		this.tutorialPages = tutorialPages;
	}

	/**
	 * Get the tutorial navigation tree.
	 * 
	 * @return the tutorial navigation tree
	 */
	@XmlElement(name="TutorialNavigationTree")
	public TutorialNavigationTree getTutorialNavigationTree() {
		return tutorialNavigationTree;
	}

	/**
	 * Set the tutorial navigation tree.
	 * 
	 * @param tutorialNavigationTree the tutorial navigation tree
	 */
	public void setTutorialNavigationTree(
			TutorialNavigationTree tutorialNavigationTree) {
		this.tutorialNavigationTree = tutorialNavigationTree;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment_core.condition.Condition#getNumTrials()
	 */
	@Override
	public int getNumTrials() {
		return tutorialPages != null ? tutorialPages.size() : 0;
	}
}