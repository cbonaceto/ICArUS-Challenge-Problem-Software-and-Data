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
package org.mitre.icarus.cps.app.experiment.ui_study.trial_states;

import java.util.ArrayList;

import org.mitre.icarus.cps.app.experiment.TrialPartState;
import org.mitre.icarus.cps.app.experiment.ui_study.exam.ItemProbability;
import org.mitre.icarus.cps.app.experiment.ui_study.exam.SpatialPresentationTrial;
import org.mitre.icarus.cps.app.experiment.ui_study.exam.UIStudyPhase.NormalizationMode;

/**
 * @author CBONACETO
 *
 */
public class SpatialPresentationTrialState extends SingleBlockTrialState {
	
	/** The spatial presentation trial */
	protected SpatialPresentationTrial trial;

	public SpatialPresentationTrialState(int trialNumber, SpatialPresentationTrial trial,
			NormalizationMode normalizationMode, ArrayList<ItemProbability> initialSettings,
			int defaultSetting) {
		super(trialNumber);	
		this.trial = trial;
		if(trial != null) {
			createTrialPartStates(trial, normalizationMode, initialSettings, defaultSetting);
		}
		else {
			trialParts = new ArrayList<TrialPartState>();
		}
	}	
	
	@Override
	public SpatialPresentationTrial getTrial() {
		return trial;
	}	
}