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

import org.mitre.icarus.cps.app.experiment.TrialPartState;
import org.mitre.icarus.cps.app.experiment.TrialState;
import org.mitre.icarus.cps.exam.phase_1.testing.IcarusTestTrial_Phase1;

/**
 * Abstract base class for trial states.  Trial states may contain one or more
 * trial part states for each trial part probe in the trial.
 * 
 * @author CBONACETO
 *
 */
public abstract class TrialState_Phase1 extends TrialState<TrialPartState> {
	
	/** The S1 score for the trial (if any) */
	protected Double score_s1;
	
	/** The S2 score for the trial (if any) */
	protected Double score_s2;
	
	/**
	 * Constructor takes the trial number for this trial state.
	 * 
	 * @param trialNumber the trial number
	 */
	public TrialState_Phase1(int trialNumber) {
		super(trialNumber);
	}
	
	/**
	 * Get the trial this state is for.
	 * 
	 * @return the trial
	 */
	public abstract IcarusTestTrial_Phase1 getTrial();	
	
	
	/**
	 * Set the overall trial time in the response contained in the trial object
	 * to the time stored in this trial state.
	 * 
	 * @param trial the trial
	 */
	protected void updateTimingData(IcarusTestTrial_Phase1 trial) {
		if(trial != null && trial.getTrialResponse() != null) {
			trial.getTrialResponse().setTrialTime_ms(trialTime_ms);
		}
	}	

	/**
	 * Get the probability score (S1) for the trial.
	 * 
	 * @return the probability score
	 */
	public Double getScore_s1() {
		return score_s1;
	}

	/**
	 * Set the probability score (S1) for the trial.
	 * 
	 * @param score_s1 the probability score
	 */
	public void setScore_s1(Double score_s1) {
		this.score_s1 = score_s1;
	}

	/**
	 * Get the troop allocation score (S2) for the trial.
	 * 
	 * @return the troop allocation score
	 */
	public Double getScore_s2() {
		return score_s2;
	}

	/**
	 * Set the troop allocation score (S2) for the trial.
	 * 
	 * @param score_s2 the troop allocation score
	 */
	public void setScore_s2(Double score_s2) {
		this.score_s2 = score_s2;
	}	
}