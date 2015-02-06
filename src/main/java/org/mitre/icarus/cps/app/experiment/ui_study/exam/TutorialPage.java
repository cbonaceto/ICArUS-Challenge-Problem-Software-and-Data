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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.app.experiment.ui_study.exam.UIStudyPhase.ModalityType;
import org.mitre.icarus.cps.app.experiment.ui_study.exam.UIStudyPhase.NormalizationMode;
import org.mitre.icarus.cps.app.experiment.ui_study.exam.UIStudyPhase.WidgetType;
import org.mitre.icarus.cps.experiment_core.condition.InstructionsPage;

/**
 * A tutorial instructions page.  Also includes an optional probability entry component.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="TutorialPage", namespace="IcarusUIStudy")
public class TutorialPage extends InstructionsPage {
	
	@XmlType(name="TutorialPageProbabilityType", namespace="IcarusUIStudy")
	public static enum ProbabilityType {Initial, Current, Normalized};	
	
	/** The type of probability panel to show (if any) */
	protected WidgetType widget;
	
	/** The input modality to use for the probability panel (if any) */
	protected ModalityType modality;
	
	/** The normalization mode to use for the probability panel (if any) */
	protected NormalizationMode normalizationMode;
	
	/** Titles to use for the probability entry panel fields or troop allocation fields */
	protected ArrayList<String> probabilityEntryTitles;
	
	/** Whether the probability panel should be editable */
	protected Boolean probabilityPanelEditable = false;	
	
	/** The type of probabilities to show in the probability entry panel */	
	protected ProbabilityType probabilityType = ProbabilityType.Initial;	
	
	/** Whether the back button should be enabled */
	protected Boolean enableBackButton = true;
	
	@XmlAttribute(name="widget")
	public WidgetType getWidget() {
		return widget;
	}

	public void setWidget(WidgetType widget) {
		this.widget = widget;
	}

	@XmlAttribute(name="modality")
	public ModalityType getModality() {
		return modality;
	}

	public void setModality(ModalityType modality) {
		this.modality = modality;
	}

	@XmlAttribute(name="normalization")
	public NormalizationMode getNormalizationMode() {
		return normalizationMode;
	}

	public void setNormalizationMode(NormalizationMode normalizationMode) {
		this.normalizationMode = normalizationMode;
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