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

import org.mitre.icarus.cps.app.experiment.TrialPartState;

/**
 * A trial part state to confirm normalized probability settings for "Delayed" normalization mode.
 * 
 * @author CBONACETO
 *
 */
public class ConfirmSettingsTrialPartState extends TrialPartState {

	/** The probability entry state to confirm settings for */
	protected final ProbabilityEntryTrialPartState probabiltyProbe;
	
	/**
	 * Constructor takes the trial number, trial part number, and the probability probe to confirm settings for.
	 * 
	 * @param trialNumber the trial number
	 * @param trialPartNumber the trial part number
	 * @param probe the probability probe to confirm normalized settings for
	 */
	public ConfirmSettingsTrialPartState(int trialNumber, int trialPartNumber, ProbabilityEntryTrialPartState probe) {
		super(trialNumber, trialPartNumber);
		this.probabiltyProbe = probe;
	}
	
	/**
	 * Get the probability probe to confirm normalized settings for.
	 * 
	 * @return the probability probe
	 */
	public ProbabilityEntryTrialPartState getProbabiltyProbe() {
		return probabiltyProbe;
	}	
}