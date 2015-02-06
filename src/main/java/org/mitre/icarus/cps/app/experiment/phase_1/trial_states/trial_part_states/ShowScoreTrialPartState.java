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

import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.TrialState_Phase1;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.TrialPartResponse;
import org.mitre.icarus.cps.exam.phase_1.testing.GroundTruth;
import org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.TrialPartProbe;

/**
 * A trial part state to display the S1 and/or S2 score(s) on a trial to the subject.
 * 
 * @author CBONACETO
 *
 */
public class ShowScoreTrialPartState extends TrialPartState_Phase1 {
	
	/** The trial the score is for */
	protected final TrialState_Phase1 trial;
	
	/** The ground truth information if ground truth is revealed along with the score. */
	protected GroundTruth groundTruth;
	
	public ShowScoreTrialPartState(int trialNumber, int trialPartNumber, TrialState_Phase1 trial) {
		super(trialNumber, trialPartNumber);
		this.trial = trial;
	}
	
	public ShowScoreTrialPartState(int trialNumber, int trialPartNumber, TrialState_Phase1 trial,
			GroundTruth groundTruth) {
		super(trialNumber, trialPartNumber);
		this.trial = trial;
		this.groundTruth = groundTruth;
	}

	/**
	 * Get the trial this show score trial part state is for.
	 * 
	 * @return the trial
	 */
	public TrialState_Phase1 getTrial() {
		return trial;
	}
	
	public GroundTruth getGroundTruth() {
		return groundTruth;
	}

	public void setGroundTruth(GroundTruth groundTruth) {
		this.groundTruth = groundTruth;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment.phase_1.trial_states.trial_part_states.TrialPartState#getProbe()
	 */
	@Override
	public TrialPartProbe getProbe() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment.phase_1.trial_states.trial_part_states.TrialPartState#getResponse()
	 */
	@Override
	public TrialPartResponse getResponse() {
		return null;
	}	
}