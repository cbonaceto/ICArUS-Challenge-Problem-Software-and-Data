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
package org.mitre.icarus.cps.app.experiment.phase_1.trial_states;

import org.mitre.icarus.cps.exam.phase_1.testing.AttackLocationPresentationTrial;

/**
 * Trial state for an attack presentation trial in Tasks 1- 3.
 * 
 * @author CBONACETO
 *
 */
public class AttackPresentationTrialState extends TrialState_Phase1 {
	
	/** The attack presentation trial */
	protected final AttackLocationPresentationTrial trial;	
	
	public AttackPresentationTrialState(int trialNumber, AttackLocationPresentationTrial trial) {
		super(trialNumber);
		this.trial = trial;
	}

	@Override
	public AttackLocationPresentationTrial getTrial() {
		return trial;
	}

	@Override
	public void updateTrialResponseData() {
		updateTimingData(trial);
	}
}