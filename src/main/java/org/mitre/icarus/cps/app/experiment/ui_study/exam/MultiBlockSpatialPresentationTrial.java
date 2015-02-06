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
import javax.xml.bind.annotation.XmlType;

/**
 * @author CBONACETO
 *
 */
@XmlType(name="MultiBlockSpatialPresentation", namespace="IcarusUIStudy")
public class MultiBlockSpatialPresentationTrial extends UIStudyTrial {
	
	/** The spatial presentation blocks */
	protected ArrayList<SpatialPresentationBlock> spatialPresentationBlocks;
	
	/** The response to the trial */
	protected MultiBlockTrialResponse trialResponse;
	
	@XmlElementWrapper(name="UIDefaults")
	@XmlElement(name="ItemProbability")
	@Override
	public ArrayList<ItemProbability> getUiDefaults() {
		return uiDefaults;
	}

	@Override
	public void setUiDefaults(ArrayList<ItemProbability> uiDefaults) {
		this.uiDefaults = uiDefaults;
	}

	@XmlElement(name="LayerPresentation")
	public ArrayList<SpatialPresentationBlock> getSpatialPresentationBlocks() {
		return spatialPresentationBlocks;
	}

	public void setSpatialPresentationBlocks(
			ArrayList<SpatialPresentationBlock> spatialPresentationBlocks) {
		this.spatialPresentationBlocks = spatialPresentationBlocks;
	}

	@XmlElement(name="TrialResponse")
	@Override
	public MultiBlockTrialResponse getTrialResponse() {
		return trialResponse;
	}
	
	public void setTrialResponse(MultiBlockTrialResponse trialResponse) {
		this.trialResponse = trialResponse;
	}	
}