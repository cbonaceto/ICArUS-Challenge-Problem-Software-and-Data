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
package org.mitre.icarus.cps.app.experiment.phase_2.trial_states;

import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbe;

/**
 * A trial part state to transition to the beginning of a new trial.
 * 
 * @author CBONACETO
 *
 */
public class BeginTrialPartState extends TrialPartState_Phase2 {
	
	/** The number of Blue locations for the trial */
	protected int numBlueLocations = 1;

	public BeginTrialPartState(int trialNumber, int trialPartNumber, int numBlueLocations) {
		super(trialNumber, trialPartNumber);
		this.numBlueLocations = numBlueLocations;
	}

	public int getNumBlueLocations() {
		return numBlueLocations;
	}

	public void setNumBlueLocations(int numBlueLocations) {
		this.numBlueLocations = numBlueLocations;
	}

	@Override
	public TrialPartProbe getProbe() {
		//Always returns null
		return null;
	}

	@Override
	public boolean validateAndUpdateProbeResponseData() {
		//Does nothing, always returns true
		return true;
	}
	
	@Override
	public TrialPartType getTrialPartType() {
		return TrialPartType.BeginTrial;
	}
}