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
import org.mitre.icarus.cps.app.experiment.TrialState;
import org.mitre.icarus.cps.app.experiment.ui_study.exam.UIStudyTrial;

/**
 * Abstract base class for trial states.  Trial states may contain one or more
 * trial part states for each trial part probe in the trial.
 * 
 * @author CBONACETO
 *
 */
public abstract class TrialState_UIStudy extends TrialState<TrialPartState> {

	public TrialState_UIStudy(int trialNumber) {
		super(trialNumber);
	}	
	
	/**
	 * Get the trial this state is for.
	 * 
	 * @return the trial
	 */
	public abstract UIStudyTrial getTrial();	
	
	/**
	 * Set the overall trial time in the response contained in the trial object
	 * to the time stored in this trial state.
	 * 
	 * @param trial the trial
	 */
	protected void updateTimingData(UIStudyTrial trial) {
		if(trial != null && trial.getTrialResponse() != null) {
			trial.getTrialResponse().setTrialTime_ms(trialTime_ms);
		}
	}		
}