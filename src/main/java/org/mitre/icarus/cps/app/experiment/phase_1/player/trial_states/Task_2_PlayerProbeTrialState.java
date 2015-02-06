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
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.GroupCirclesProbeTrialPartState;
import org.mitre.icarus.cps.app.window.phase_1.PlayerUtils;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.TrialData;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_2_ProbeTrial;
import org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.GroupCirclesProbe;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupAttack;

/**
 * Trial state for the probe trial at the end of a Task 2 trial block.
 * 
 * @author CBONACETO
 *
 */
public class Task_2_PlayerProbeTrialState extends Task_1_2_3_PlayerProbeTrialStateBase {
	
	/** The task 2 probe trial */
	protected final Task_2_ProbeTrial trial;
	
	/** The group circles probe */
	protected GroupCirclesProbeTrialPartState groupCirclesProbe;

	public Task_2_PlayerProbeTrialState(int trialNumber, Task_2_ProbeTrial trial, TrialData participantTrialData,
			TrialData avgHumanTrialData, List<GroupAttack> groupAttacks, List<Integer> previousSettings) {
		super(trialNumber, participantTrialData, avgHumanTrialData, 4);		
		this.trial = trial;
		this.participantTrialData = participantTrialData;
		this.avgHumanTrialData = avgHumanTrialData;
		this.groupAttacks = groupAttacks;
		if(trial != null) {
			createTrialPartStates(previousSettings);
		}
		else {
			trialParts = new ArrayList<TrialPartState>();
		}
	}	
	
	/**
	 * Create trial part states for the group probe, group circles probe, and surprise probe.
	 * 
	 * @param normalizationMode
	 * @param showScore
	 */
	protected void createTrialPartStates(List<Integer> previousSettings) {		
		trialParts = new ArrayList<TrialPartState>();
		int trialPartNum = 0;
		
		//Add the group circles probe
		if(trial.getGroupCirclesProbe() != null) {
			GroupCirclesProbe probe = trial.getGroupCirclesProbe();
			int numGroups = probe.getGroups() != null ? probe.getGroups().size() : 4;
			groupCirclesProbe = new GroupCirclesProbeTrialPartState(trialNumber, ++trialPartNum, probe);
			if(participantTrialData != null) {
				groupCirclesProbe.setParticipantGroupCircles(PlayerUtils.createGroupCircles(
						participantTrialData.getCenter_x(), participantTrialData.getCenter_y(), 
						participantTrialData.getSigma(), probe.getGroups(), numGroups));
				groupCirclesProbe.setNormativeGroupCircles(PlayerUtils.createGroupCircles(
						participantTrialData.getNormative_center_x(), participantTrialData.getNormative_center_y(), 
						participantTrialData.getNormative_sigma(), probe.getGroups(), numGroups));
			}
			if(avgHumanTrialData != null) {
				groupCirclesProbe.setAvgHumanGroupCircles(PlayerUtils.createGroupCircles(
						avgHumanTrialData.getCenter_x(), avgHumanTrialData.getCenter_y(), 
						avgHumanTrialData.getSigma(), probe.getGroups(), numGroups));
			}
			trialParts.add(groupCirclesProbe);
		}
		
		//Add the group probe
		trialPartNum = createGroupProbe(trial.getAttackLocationProbe(), trialPartNum, previousSettings);

		//Add the troop allocation probe
		trialPartNum = createTroopAllocationProbe(trial.getTroopSelectionProbe(), trialPartNum);		

		//Add the ground truth surprise probe
		trialPartNum = createSurpriseProbe(trial.getGroundTruthSurpriseProbe(), trial.getGroundTruth(), trialPartNum);			
		
		/*//Add state to show the score
		if(showScore) {
			trialParts.add(new ShowScoreTrialPartState(trialNumber, ++trialPartNum, this));
		}*/
	}
	
	public GroupCirclesProbeTrialPartState getGroupCirclesProbe() {
		return groupCirclesProbe;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment.phase_1.trial_states.TrialState#getTrial()
	 */
	@Override
	public Task_2_ProbeTrial getTrial() {
		return trial;
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.player.phase_1.data_model.trial_states.Task_1_2_3_PlayerProbeTrialStateBase#updateTrialData(org.mitre.icarus.cpa.phase_1.data_model.TrialData, org.mitre.icarus.cpa.phase_1.data_model.TrialData)
	 */
	@Override
	public void updateTrialData(TrialData participantTrialData, TrialData avgHumanTrialData) {
		super.updateTrialData(participantTrialData, avgHumanTrialData);
		
		//Update the group circles probe data
		if(groupCirclesProbe != null) {
			GroupCirclesProbe probe = trial.getGroupCirclesProbe();
			int numGroups = probe.getGroups() != null ? probe.getGroups().size() : 4;
			if(participantTrialData != null) {
				groupCirclesProbe.setParticipantGroupCircles(PlayerUtils.createGroupCircles(
						participantTrialData.getCenter_x(), participantTrialData.getCenter_y(), 
						participantTrialData.getSigma(), probe.getGroups(), numGroups));
				groupCirclesProbe.setNormativeGroupCircles(PlayerUtils.createGroupCircles(
						participantTrialData.getNormative_center_x(), participantTrialData.getNormative_center_y(), 
						participantTrialData.getNormative_sigma(), probe.getGroups(), numGroups));
			}
			if(avgHumanTrialData != null) {
				groupCirclesProbe.setAvgHumanGroupCircles(PlayerUtils.createGroupCircles(
						avgHumanTrialData.getCenter_x(), avgHumanTrialData.getCenter_y(), 
						avgHumanTrialData.getSigma(), probe.getGroups(), numGroups));
			}
		}
	}
}