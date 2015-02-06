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

import org.mitre.icarus.cps.exam.phase_05.response.LayerData;


public class LayerSelectTrialState extends TrialState {

	protected ArrayList<LayerData> layerChoices;
	
	protected LayerData selectedLayer;
	
	public LayerSelectTrialState(int trialNum, int trialPhase) {
		super(trialNum, trialPhase, TrialType.LayerSelect);
		layerChoices = new ArrayList<LayerData>();
	}
	
	public LayerData getSelectedLayer() {
		return selectedLayer;
	}
	
	public void setSelectedLayer(LayerData selectedLayer) {
		this.selectedLayer = selectedLayer;
	}
	
	public ArrayList<LayerData> getLayerChoices() {
		return layerChoices;
	}
	
	public void setLayerChoices(ArrayList<LayerData> layerChoices) {
		this.layerChoices = layerChoices;
	}
}
