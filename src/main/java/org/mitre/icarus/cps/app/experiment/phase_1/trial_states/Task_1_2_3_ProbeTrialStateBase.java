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

import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.GroupProbeTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.SurpriseProbeTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.TroopSelectionMultiGroupTrialPartState;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_1_2_3_ProbeTrialBase;

/**
 * Abstract base class for the trial state for the probe trial at the end of a presentation trial block
 * in Tasks 1-3.
 * 
 * @author CBONACETO
 *
 */
public abstract class Task_1_2_3_ProbeTrialStateBase extends TrialState_Phase1 {
		
	/** The group probe */
	protected GroupProbeTrialPartState groupProbe;
	
	/** The troop selection probe */
	protected TroopSelectionMultiGroupTrialPartState troopProbe;
	
	/** The ground truth surprise probe */
	protected SurpriseProbeTrialPartState surpriseProbe;

	public Task_1_2_3_ProbeTrialStateBase(int trialNumber) {
		super(trialNumber);
	}	
	
	/**
	 * Updates the response in the trial object with the group probe settings and 
	 * surprise probe settings.  Also updates the overall trial time.
	 * 
	 * @param trial the trial object to update
	 */
	protected void updateTrialResponseData(Task_1_2_3_ProbeTrialBase trial) {
		if(trial.getTrialResponse() != null) {
			if(groupProbe != null) {
				trial.getTrialResponse().setAttackLocationResponse(groupProbe.getResponse());
			}
			if(troopProbe != null) {
				trial.getTrialResponse().setTroopSelectionResponse(troopProbe.getResponse());
			}
			if(surpriseProbe != null) {
				trial.getTrialResponse().setGroundTruthSurpriseResponse(surpriseProbe.getResponse());
			}
			updateTimingData(trial);
		}		
	}
}