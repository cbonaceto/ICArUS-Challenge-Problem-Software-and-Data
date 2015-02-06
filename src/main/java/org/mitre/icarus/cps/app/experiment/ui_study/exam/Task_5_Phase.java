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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author CBONACETO
 *
 */
@XmlRootElement(name="Task5", namespace="IcarusUIStudy")
@XmlType(name="Task5", namespace="IcarusUIStudy", propOrder={"uiDefaults", "testTrials"})
public class Task_5_Phase extends Task_2_3_5_PhaseBase {
	
	/** The default initial probabilities ("anchors") */
	//TODO: Remove this
	protected ArrayList<ItemProbability> uiDefaults;

	@XmlElementWrapper(name="UIDefaults")
	@XmlElement(name="ItemProbability")
	public ArrayList<ItemProbability> getUiDefaults() {
		return uiDefaults;
	}

	public void setUiDefaults(ArrayList<ItemProbability> uiDefaults) {
		this.uiDefaults = uiDefaults;
	}

	@Override
	@XmlElement(name="Trial")
	public ArrayList<SpatialPresentationTrial> getTestTrials() {
		return testTrials;
	}

	@Override
	public void setTestTrials(ArrayList<SpatialPresentationTrial> testTrials) {
		this.testTrials = testTrials;
	}
}