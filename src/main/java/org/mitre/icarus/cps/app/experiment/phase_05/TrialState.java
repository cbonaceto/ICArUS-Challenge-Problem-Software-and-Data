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
package org.mitre.icarus.cps.app.experiment.phase_05;

import java.util.ArrayList;
import java.util.List;

import org.mitre.icarus.cps.app.widgets.probability_entry.distinct_bars.BoxContainer.BoxOrientation;
import org.mitre.icarus.cps.exam.phase_05.response.LayerData;

/**
 * @author Jing Hu
 *
 */
public class TrialState {
	public enum TrialType {ScenePresentation, LayerSelect, ConfirmNormalized, Assessment, Other};
	
	/** Raw probability data */
	protected List<Integer> rawData;
	
	/** Normalized probability data */
	protected List<Integer> normalizedData;
	
	/** Layers selected */
	protected List<LayerData> selectedLayers;
	
	/** Probability box orientation */
	protected BoxOrientation boxOrientation;
	
	/** The number of probability boxes */
	protected int numBoxes = 4;
	
	/** Whether or not this trial state is just to "confirm" normalized settings */
	//protected boolean confirmNormalizedTrial = false;
	
	/** Trial number */
	protected final int trialNumber;
	
	/** Trial phase */
	protected final int trialPhase;	
	
	/** Time spent on the trial phase */
	protected long trialPhaseTime_ms = 0;	
	
	/** The trial type */
	protected final TrialType trialType;

	public TrialState(int trialNumber, int trialPhase, TrialType trialType) {
		this.trialNumber = trialNumber;
		this.trialPhase = trialPhase;
		this.trialType = trialType;
		rawData = new ArrayList<Integer>();		
		selectedLayers = new ArrayList<LayerData>();
		boxOrientation = BoxOrientation.HORIZONTAL_LINE;
	}	
	
	public int getTrialNumber() {
		return trialNumber;
	}

	public int getTrialPhase() {
		return trialPhase;
	}

	public List<Integer> getRawData() {
		return rawData;
	}
	
	public void setRawData(List<Integer> rawData) {
		this.rawData = rawData;
	}
	
	public List<Integer> getNormalizedData() {
		return normalizedData;
	}

	public void setNormalizedData(List<Integer> normalizedData) {
		this.normalizedData = normalizedData;
	}

	public List<LayerData> getSelectedLayers() {
		return selectedLayers;
	}
	
	public void setSelectedLayers(List<LayerData> selectedLayers) {
		this.selectedLayers = selectedLayers;
	}
	
	/** Return whether the layer with the given ID has been selected */
	public boolean isLayerSelected(Integer layerId) {
		if(selectedLayers != null) {
			for(LayerData layer : selectedLayers) {
				if(layer.getLayerID() == layerId) {
					return true;
				}
			}
		}
		return false;
	}

	public BoxOrientation getBoxOrientation() {
		return boxOrientation;
	}

	public void setBoxOrientation(BoxOrientation boxOrientation) {
		this.boxOrientation = boxOrientation;
	}

	public int getNumBoxes() {
		return numBoxes;
	}

	public void setNumBoxes(int numBoxes) {
		this.numBoxes = numBoxes;
	}

	/*public boolean isConfirmNormalizedTrial() {
		return confirmNormalizedTrial;
	}

	public void setConfirmNormalizedTrial(boolean confirmNormalizedTrial) {
		this.confirmNormalizedTrial = confirmNormalizedTrial;
	}*/

	public long getTrialPhaseTime_ms() {
		return trialPhaseTime_ms;
	}

	public void setTrialPhaseTime_ms(long trialPhaseTime_ms) {
		this.trialPhaseTime_ms = trialPhaseTime_ms;
	}

	public TrialType getTrialType() {
		return trialType;
	}		
}
