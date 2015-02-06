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
package org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states;

import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.TrialPartResponse;
import org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.TrialPartProbe;

/**
 * A trial part state to confirm normalized probability settings for a group probe, location probe,
 * or multi-group or multi-location troop allocation probe.
 * 
 * @author CBONACETO
 *
 */
public class ConfirmSettingsTrialPartState extends TrialPartState_Phase1 {

	/** The probe to confirm settings for */
	protected final ProbabilityProbeTrialPartState probabiltyProbe;
	
	/**
	 * Constructor takes the trial number, trial part number, and the probability probe to confirm settings for.
	 * 
	 * @param trialNumber the trial number
	 * @param trialPartNumber the trial part number
	 * @param probe the probability probe to confirm normalized settings for
	 */
	public ConfirmSettingsTrialPartState(int trialNumber, int trialPartNumber, ProbabilityProbeTrialPartState probe) {
		super(trialNumber, trialPartNumber);
		this.probabiltyProbe = probe;
	}
	
	/**
	 * Get the probability probe to confirm normalized settings for.
	 * 
	 * @return the probability probe
	 */
	public ProbabilityProbeTrialPartState getProbabiltyProbe() {
		return probabiltyProbe;
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment.phase_1.trial_states.trial_part_states.TrialPartState#getProbe()
	 */
	@Override
	public TrialPartProbe getProbe() {
		if(probabiltyProbe != null) {
			return probabiltyProbe.getProbe();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment.phase_1.trial_states.trial_part_states.TrialPartState#getResponse()
	 */
	@Override
	public TrialPartResponse getResponse() {
		if(probabiltyProbe != null) {
			return probabiltyProbe.getResponse();
		}
		return null;
	}
}