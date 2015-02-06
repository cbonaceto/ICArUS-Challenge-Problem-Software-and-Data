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

import java.util.ArrayList;

import org.mitre.icarus.cps.app.experiment.TrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.ConfirmSettingsTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.GroupCirclesProbeTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.GroupProbeTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.ShowScoreTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.SurpriseProbeTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.TroopSelectionMultiGroupTrialPartState;
import org.mitre.icarus.cps.app.util.ProbabilityUtils;
import org.mitre.icarus.cps.exam.base.IcarusExam.NormalizationMode;
import org.mitre.icarus.cps.exam.phase_1.response.Task_2_ProbeTrialResponse;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_2_ProbeTrial;
import org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.AttackLocationProbe_MultiGroup;
import org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.TroopSelectionProbe_MultiGroup;

/**
 * Trial state for the probe trial at the end of a Task 2 trial block.
 * 
 * @author CBONACETO
 *
 */
public class Task_2_ProbeTrialState extends Task_1_2_3_ProbeTrialStateBase {
	
	/** The task 2 probe trial */
	protected Task_2_ProbeTrial trial;
	
	/** The group circles probe */
	protected GroupCirclesProbeTrialPartState groupCirclesProbe;

	public Task_2_ProbeTrialState(int trialNumber, Task_2_ProbeTrial trial, 
			NormalizationMode normalizationMode, boolean showScore) {
		super(trialNumber);		
		this.trial = trial;
		if(trial != null) {
			createTrialPartStates(normalizationMode, showScore);
		}
		else {
			trialParts = new ArrayList<TrialPartState>();
		}
	}	
	
	protected Task_2_ProbeTrialState(int trialNumber) {
		super(trialNumber);
	}
	
	/**
	 * Create trial part states for the group probe, group circles probe, and surprise probe.
	 * 
	 * @param normalizationMode
	 * @param showScore
	 */
	protected void createTrialPartStates(NormalizationMode normalizationMode, boolean showScore) {
		trialParts = new ArrayList<TrialPartState>();
		int trialPartNum = -1;
		
		//Add the group circles probe
		if(trial.getGroupCirclesProbe() != null) {
			groupCirclesProbe = new GroupCirclesProbeTrialPartState(trialNumber, ++trialPartNum, 
					trial.getGroupCirclesProbe());
			trialParts.add(groupCirclesProbe);
		}
		
		//Add the group probe
		AttackLocationProbe_MultiGroup probe = trial.getAttackLocationProbe();
		if(probe != null) {
			groupProbe = new GroupProbeTrialPartState(trialNumber, ++trialPartNum, probe);
			groupProbe.setCurrentSettings(ProbabilityUtils.createDefaultInitialProbabilities(
					probe != null && probe.getGroups() != null ? probe.getGroups().size() : 0));
			groupProbe.setPreviousSettings(ProbabilityUtils.createDefaultInitialProbabilities(
					groupProbe.getCurrentSettings().size()));
			trialParts.add(groupProbe);

			//Add a state to confirm the normalized settings
			if(normalizationMode != null && normalizationMode == NormalizationMode.NormalizeAfterAndConfirm) {
				trialParts.add(new ConfirmSettingsTrialPartState(trialNumber, ++trialPartNum, groupProbe));
			}
		}
		
		//Add the troop selection probe
		if(trial.getTroopSelectionProbe() != null) {
			TroopSelectionProbe_MultiGroup troopProbeTrial = trial.getTroopSelectionProbe();
			troopProbe = new TroopSelectionMultiGroupTrialPartState(
					trialNumber, ++trialPartNum, troopProbeTrial);
			trialParts.add(troopProbe);
		}
		
		//Add the ground truth surprise probe
		if(trial.getGroundTruthSurpriseProbe() != null) {
			surpriseProbe = new SurpriseProbeTrialPartState(trialNumber, ++trialPartNum,
					trial.getGroundTruthSurpriseProbe(), trial.getGroundTruth());
			trialParts.add(surpriseProbe);
		}
		
		//Add state to show the score
		if(showScore) {
			trialParts.add(new ShowScoreTrialPartState(trialNumber, ++trialPartNum, this));
		}
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment.phase_1.trial_states.TrialState#getTrial()
	 */
	@Override
	public Task_2_ProbeTrial getTrial() {
		return trial;
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment.phase_1.trial_states.TrialState#updateTrialResponseData()
	 */
	@Override
	public void updateTrialResponseData() {
		if(trial.getTrialResponse() == null) {
			trial.setTrialResponse(new Task_2_ProbeTrialResponse());
		}
		if(groupCirclesProbe != null) {			
			trial.getTrialResponse().setGroupCirclesResponse(groupCirclesProbe.getResponse());
		}
		updateTrialResponseData(trial);
	}
}