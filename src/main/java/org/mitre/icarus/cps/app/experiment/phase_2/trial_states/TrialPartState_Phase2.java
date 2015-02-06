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

import org.mitre.icarus.cps.app.experiment.TrialPartState;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbe;

/**
 * Base class for Phase 2 trial part states.
 * 
 * @author cbonaceto
 *
 */
public abstract class TrialPartState_Phase2 extends TrialPartState {
	
	/** Trial part state types */
	public static enum TrialPartType{BeginTrial, 
		IntPresentation,
		SigintSelection,
		MostLikelyRedTacticSelection,
		RedTacticsProbabilityProbe,
		RedTacticParametersProbe,
		AttackProbabilityReport, 
		AttackProbabilityReport_Propensity,
		AttackProbabilityReport_Capability_Propensity,
		AttackProbabilityReport_Activity,
		AttackProbabilityReport_Activity_Capability_Propensity, 
		NormalizationCorrectionOrConfirmation, 
		BlueActionSelectionOrPresentation, 
		RedActionPresentation};

	public TrialPartState_Phase2(int trialNumber, int trialPartNumber) {
		super(trialNumber, trialPartNumber);
	}
	
	/**
	 * Get the trial part probe this trial part state is for.
	 * 
	 * @return the trial part probe
	 */
	public abstract TrialPartProbe getProbe();
	
	/**
	 * Get the name of the trial part probe.
	 * 
	 * @return the trial part probe name
	 */
	public String getTrialPartProbeName() {
		TrialPartProbe probe = getProbe();
		return probe != null ? probe.getName() : null;
	}
	
	/**
	 * @return
	 */
	public TrialPartProbeResponse getProbeWithUpdatedResponseData() {
		TrialPartProbe probe = getProbe();
		boolean responseValid = false;
		if(probe != null) {
			responseValid = validateAndUpdateProbeResponseData();
			updateTimingData(probe);
		}
		return new TrialPartProbeResponse(probe, responseValid);
	}
	
	/**
	 * 
	 * 
	 */
	protected abstract boolean validateAndUpdateProbeResponseData();
	
	public abstract TrialPartType getTrialPartType();
}