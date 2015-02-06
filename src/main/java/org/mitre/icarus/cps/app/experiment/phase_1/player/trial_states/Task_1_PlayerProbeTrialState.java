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
package org.mitre.icarus.cps.app.experiment.phase_1.player.trial_states;

import java.util.ArrayList;
import java.util.List;

import org.mitre.icarus.cps.app.experiment.TrialPartState;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.TrialData;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_1_ProbeTrial;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupAttack;

/**
 * Trial state for the probe trial at the end of a Task 1 trial block.
 * 
 * @author CBONACETO
 *
 */
public class Task_1_PlayerProbeTrialState extends Task_1_2_3_PlayerProbeTrialStateBase {
	
	/** The task 1 probe trial */
	protected final Task_1_ProbeTrial trial;
	
	public Task_1_PlayerProbeTrialState(int trialNumber, Task_1_ProbeTrial trial, TrialData participantTrialData,
			TrialData avgHumanTrialData, List<GroupAttack> groupAttacks, List<Integer> previousSettings) {
		super(trialNumber, participantTrialData, avgHumanTrialData, 2);		
		this.trial = trial;		
		this.groupAttacks = groupAttacks;
		if(trial != null) {
			createTrialPartStates(previousSettings);
		}
		else {
			trialParts = new ArrayList<TrialPartState>();
		}
	}	
	
	/**
	 * Create trial part states for the group probe and surprise probe.
	 * 
	 * @param normalizationMode
	 * @param showScore
	 */
	protected void createTrialPartStates(List<Integer> previousSettings) {		
		trialParts = new ArrayList<TrialPartState>();
		int trialPartNum = 0;
		
		//Add the group probe
		trialPartNum = createGroupProbe(trial.getAttackLocationProbe(), trialPartNum, previousSettings);
		
		//Add the troop allocation probe
		trialPartNum = createTroopAllocationProbe(trial.getTroopSelectionProbe(), trialPartNum);		
	
		//Add the ground truth surprise probe
		trialPartNum = createSurpriseProbe(trial.getGroundTruthSurpriseProbe(), trial.getGroundTruth(), trialPartNum);		
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment.phase_1.trial_states.TrialState#getTrial()
	 */
	@Override
	public Task_1_ProbeTrial getTrial() {		
		return trial;
	}		
}