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
package org.mitre.icarus.cps.exam.phase_1.training;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.base.training.IcarusTutorialPhase;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupType;

/**
 * A Phase 1 tutorial phase contains a series of Phase 1 tutorial pages.
 * FOR HUMAN SUBJECT USE ONLY.
 * 
 * @author CBONACETO
 *
 */
@XmlRootElement(name="TutorialPhase_CPD1", namespace="IcarusCPD_1")
@XmlType(name="TutorialPhase_CPD1", namespace="IcarusCPD_1", 
		propOrder={"probabilityEntryGroups", "probabilityEntryTitles", "autoNormalizeProbabilities", "tutorialPages"} )
public class TutorialPhase extends IcarusTutorialPhase<TutorialPage> {	
	
	/** The default groups for the probability entry panel fields. The
	 * default groups will only be used if groups or titles aren't specified in a tutorial page. */
	protected ArrayList<GroupType> probabilityEntryGroups;
	
	/** The default titles for the probability entry panel fields (e.g., groups or locations).
	 * The default titles will only be used if groups or titles aren't specified in a tutorial page.
	 */
	protected ArrayList<String> probabilityEntryTitles;	
	
	/** Whether probability entries should be auto-normalized */
	protected Boolean autoNormalizeProbabilities = false;	
	
	/**
	 * Get the default titles for the probability entry panel fields (e.g., groups or locations).
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
	 * Set the default titles for the probability entry panel fields (e.g., groups or locations).
	 * The default titles will only be used if titles aren't specified in a tutorial page.
	 * 
	 * @param probabilityEntryTitles the default probability entry titles
	 */
	public void setProbabilityEntryTitles(ArrayList<String> probabilityEntryTitles) {
		this.probabilityEntryTitles = probabilityEntryTitles;
	}	
	
	/**
	 * Get the groups for the probability entry panel fields.
	 * 
	 * @return the groups
	 */
	@XmlElement(name="ProbabilityEntryGroups")
	@XmlList
	public ArrayList<GroupType> getProbabilityEntryGroups() {
		return probabilityEntryGroups;
	}

	/**
	 *  Set the groups for the probability entry panel fields.
	 * 
	 * @param probabilityEntryGroups the groups
	 */
	public void setProbabilityEntryGroups(ArrayList<GroupType> probabilityEntryGroups) {
		this.probabilityEntryGroups = probabilityEntryGroups;
	}

	/**
	 * Get whether probabilities should be auto-normalized.
	 * 
	 * @return whether probabilities should be auto-normalized.
	 */
	@XmlElement(name="AutoNormalizeProbabilities")
	public Boolean isAutoNormalizeProbabilities() {
		return autoNormalizeProbabilities;
	}

	/**
	 * Set whether probabilities should be auto-normalized.
	 * 
	 * @param autoNormalizeProbabilities whether probabilities should be auto-normalized.
	 */
	public void setAutoNormalizeProbabilities(Boolean autoNormalizeProbabilities) {
		this.autoNormalizeProbabilities = autoNormalizeProbabilities;
	}
	
	/**
	 * Get the tutorial pages.
	 * 
	 * @return the tutorial pages
	 */
	@Override
	@XmlElementWrapper(name="TutorialPages")
	@XmlElement(name="TutorialPage")
	public List<TutorialPage> getTutorialPages() {
		return tutorialPages;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.base.training.IcarusTutorialPhase#setTutorialPages(java.util.List)
	 */
	@Override
	public void setTutorialPages(List<TutorialPage> tutorialPages) {
		this.tutorialPages = tutorialPages;
	}	
}