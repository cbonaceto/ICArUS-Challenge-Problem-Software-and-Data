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
package org.mitre.icarus.cps.app.experiment.ui_study.exam;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.base.training.IcarusTutorialPhase;

/**
 * @author CBONACETO
 *
 */
@XmlRootElement(name="TutorialPhase_UIStudy", namespace="IcarusUIStudy")
@XmlType(name="TutorialPhase_UIStudy", namespace="IcarusUIStudy", 
		propOrder={"probabilityEntryTitles", "tutorialPages"})
public class TutorialPhase extends IcarusTutorialPhase<TutorialPage> {	
	
	/** The default titles for the probability entry panel fields (e.g., 1-4).
	 * The default titles will only be used if titles aren't specified in a tutorial page.
	 */
	protected ArrayList<String> probabilityEntryTitles;	

	/**
	 * Get the tutorial pages.
	 * 
	 * @return the tutorial pages
	 */
	@XmlElementWrapper(name="TutorialPages")
	@XmlElement(name="TutorialPage")
	@Override
	public List<TutorialPage> getTutorialPages() {
		return tutorialPages;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.base.training.IcarusTutorialPhase#setTutorialPages(java.util.List)
	 */
	@Override
	public void setTutorialPages(List<TutorialPage> tutorialPages) {
		super.setTutorialPages(tutorialPages);
	}	
	
	/**
	 * Get the default titles for the probability entry panel fields (e.g., 1-4).
	 * The default titles will only be used if titles aren't specified in a tutorial page.
	 * 
	 * @return the default probability entry titles
	 */
	@XmlElement(name="ProbabilityEntryTitles")
	@XmlList
	public ArrayList<String> getProbabilityEntryTitles() {
		return probabilityEntryTitles;
	}

	/**
	 * Set the default titles for the probability entry panel fields (e.g., 1-4).
	 * The default titles will only be used if titles aren't specified in a tutorial page.
	 * 
	 * @param probabilityEntryTitles the default probability entry titles
	 */
	public void setProbabilityEntryTitles(ArrayList<String> probabilityEntryTitles) {
		this.probabilityEntryTitles = probabilityEntryTitles;
	}		

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment_core.condition.Condition#getNumTrials()
	 */
	@Override
	public int getNumTrials() {
		if(tutorialPages != null) {
			return tutorialPages.size();
		}
		return 0;
	}
}