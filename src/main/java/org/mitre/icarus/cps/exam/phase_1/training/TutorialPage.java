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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.experiment_core.condition.InstructionsPage;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupType;

/**
 * A tutorial instructions page.  Also includes an optional probability entry component,
 * troop allocation component, or surprise entry component.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="TutorialPage", namespace="IcarusCPD_1")
public class TutorialPage extends InstructionsPage {
	
	@XmlType(name="TutorialPageInputType", namespace="IcarusCPD_1")
	public static enum InputType {ProbabilityPanel, TroopSelectionPanel, TroopAllocationPanel, SurprisePanel};
	
	@XmlType(name="TutorialPageProbabilityType", namespace="IcarusCPD_1")
	public static enum ProbabilityType {Initial, Current, Normalized};	
	
	/** The type of input panel to show (if any) */
	protected InputType inputPanelType;
	
	/** Titles to use for the probability entry panel fields or troop allocation fields */
	protected ArrayList<String> probabilityEntryTitles;
	
	/** Groups to use for the probability entry panel fields or troop allocation fields */
	protected ArrayList<GroupType> probabilityEntryGroups;	
	
	/** Whether the probability panel should be editable */
	protected Boolean probabilityPanelEditable = false;	
	
	/** The type of probabilities to show in the probability entry panel */	
	protected ProbabilityType probabilityType = ProbabilityType.Initial;	
	
	/** Whether the back button should be enabled */
	protected Boolean enableBackButton = true;
	
	@XmlAttribute(name="inputPanelType")
	public InputType getInputPanelType() {
		return inputPanelType;
	}

	public void setInputPanelType(InputType inputPanelType) {
		this.inputPanelType = inputPanelType;
	}

	/**
	 * Get the titles for the probability entry panel fields (e.g., groups or locations).
	 * 
	 * @return the titles
	 */
	@XmlElement(name="ProbabilityEntryTitles")
	@XmlList
	public ArrayList<String> getProbabilityEntryTitles() {
		return probabilityEntryTitles;
	}

	/**
	 * Set the titles for the probability entry panel fields (e.g., groups or locations).
	 * 
	 * @param probabilityEntryTitles the titles
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
	 * Get whether the probability panel is editable.
	 * 
	 * @return whether the probability panel is editable.
	 */
	@XmlAttribute(name="probabilityPanelEditable")
	public Boolean isProbabilityPanelEditable() {
		return probabilityPanelEditable;
	}

	/**
	 * Set whether the probability panel is editable.
	 * 
	 * @param probabilityPanelEditable
	 */
	public void setProbabilityPanelEditable(Boolean probabilityPanelEditable) {
		this.probabilityPanelEditable = probabilityPanelEditable;
	}

	/**
	 * Get the type of probabilities to show in the probability entry panel.
	 * 
	 * @return the type of probabilities to show in the probability entry panel
	 */
	@XmlAttribute(name="probabilityType")
	public ProbabilityType getProbabilityType() {
		return probabilityType;
	}

	/**
	 * Set the type of probabilities to show in the probability entry panel.
	 * 
	 * @param probabilityType
	 */
	public void setProbabilityType(ProbabilityType probabilityType) {
		this.probabilityType = probabilityType;
	}		

	/**
	 * Get whether the back button should be enabled (default is true).
	 * 
	 * @return Whether the back button should be enabled
	 */
	@XmlAttribute(name="enableBackButton")
	public Boolean isEnableBackButton() {
		return enableBackButton;
	}

	/**
	 * Set whether the back button should be enabled.
	 * 
	 * @param enableBackButton
	 */
	public void setEnableBackButton(Boolean enableBackButton) {
		this.enableBackButton = enableBackButton;
	}
}