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

import org.mitre.icarus.cps.app.experiment.TrialPartState;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.TrialPartResponse;
import org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.TrialPartProbe;

/**
 * @author CBONACETO
 *
 */
public abstract class TrialPartState_Phase1 extends TrialPartState {
	
	public TrialPartState_Phase1(int trialNumber, int trialPartNumber) {
		super(trialNumber, trialPartNumber);
	}

	/**
	 * Get the trial part probe this trial part state is for.
	 * 
	 * @return the trial part probe
	 */
	public abstract TrialPartProbe getProbe();
	
	/**
	 * Get the trial part response for this trial part state. 
	 * Subclasses may override this method to populate the
	 * response object with data stored in the state.
	 * 
	 * @return the response
	 */
	public abstract TrialPartResponse getResponse();	
}