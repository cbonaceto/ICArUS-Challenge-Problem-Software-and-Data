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


import java.util.List;

import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.Task_1_2_3_ProbeTrialStateBase;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.GroupProbeTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.SurpriseProbeTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.TroopAllocationMultiGroupTrialPartState;
import org.mitre.icarus.cps.app.window.phase_1.PlayerUtils;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.TrialData;
import org.mitre.icarus.cps.exam.phase_1.testing.GroundTruth;
import org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.AttackLocationProbe_MultiGroup;
import org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.SurpriseReportProbe;
import org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.TroopAllocationProbe_MultiGroup;
import org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.TroopSelectionProbe_MultiGroup;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupAttack;

/**
 * Abstract base class for the trial state for the probe trial at the end of a presentation trial block
 * in Tasks 1-3.
 * 
 * @author CBONACETO
 *
 */
public abstract class Task_1_2_3_PlayerProbeTrialStateBase extends Task_1_2_3_ProbeTrialStateBase implements IPlayerTrialState {
		
	/** All of the attack locations that have been observed up to this probe trial */
	protected List<GroupAttack> groupAttacks;
	
	/** For the player, we treat the troop selection probe as an allocation */
	protected TroopAllocationMultiGroupTrialPartState troopProbe;
	
	/** Trial data and metrics for the participant (also includes normative data) */
	protected TrialData participantTrialData;
	
	/** Trial data and metrics for average human */
	protected TrialData avgHumanTrialData;	
	
	protected int defaultNumGroups;

	public Task_1_2_3_PlayerProbeTrialStateBase(int trialNumber, TrialData participantTrialData, TrialData avgHumanTrialData, int defaultNumGroups) {
		super(trialNumber);
		this.participantTrialData = participantTrialData;
		this.avgHumanTrialData = avgHumanTrialData;
		this.defaultNumGroups = defaultNumGroups;
		if(participantTrialData != null) {
			setScore_s1(participantTrialData.getS1_score());
			setScore_s2(participantTrialData.getS2_score());
		}
	}

	public List<GroupAttack> getGroupAttacks() {
		return groupAttacks;
	}

	public void setGroupAttacks(List<GroupAttack> groupAttacks) {
		this.groupAttacks = groupAttacks;
	}
	
	public GroupProbeTrialPartState getGroupProbe() {
		return groupProbe;
	}
	
	public TroopAllocationMultiGroupTrialPartState getTroopProbe() {
		return troopProbe;
	}
	
	public SurpriseProbeTrialPartState getSurpriseProbe() {
		return surpriseProbe;
	}

	@Override
	public boolean isTrialComplete() {
		return participantTrialData != null && participantTrialData.isTrial_complete() != null ?
				participantTrialData.isTrial_complete() : false;
	}

	@Override
	public TrialData getParticipantTrialData() {
		return participantTrialData;
	}	

	@Override
	public TrialData getAvgHumanTrialData() {
		return avgHumanTrialData;
	}
	
	@Override
	public Double getAvgHumanS1_score() {
		if(avgHumanTrialData != null) {
			return avgHumanTrialData.getS1_score();
		}
		return null;
	}

	@Override
	public Double getAvgHumanS2_score() {
		if(avgHumanTrialData != null) {
			return avgHumanTrialData.getS2_score();
		}			
		return null;
	}

	@Override
	public void updateTrialResponseData() {}
	
	/** Create the group probe */
	protected int createGroupProbe(AttackLocationProbe_MultiGroup probe, int trialPartNum, List<Integer> previousSettings) {
		if(probe != null) {
			int numProbs = probe.getGroups() != null ? probe.getGroups().size() : defaultNumGroups;
			groupProbe = new GroupProbeTrialPartState(trialNumber, trialPartNum, probe);
			if(participantTrialData != null) {
				groupProbe.setCurrentSettings(PlayerUtils.getPercentProbabilities(participantTrialData.getProbs(), numProbs, 0));
				groupProbe.setNormativeSettings(PlayerUtils.getPercentProbabilities(participantTrialData.getNormative_probs(), numProbs, 0));
			} else {
				groupProbe.setCurrentSettings(PlayerUtils.createPercentProbabilities(numProbs, 0));
			}
			if(avgHumanTrialData != null) {
				groupProbe.setAvgHumanSettings(PlayerUtils.getPercentProbabilities(avgHumanTrialData.getProbs(), numProbs, 0));
			}
			groupProbe.setPreviousSettings(previousSettings != null && !previousSettings.isEmpty() ? previousSettings : 
				PlayerUtils.createDefaultPercentProbabilities(numProbs));
			trialParts.add(groupProbe);		
			return trialPartNum + 1;
		}
		return trialPartNum;
	}
	
	/** Create the troop allocation probe */
	protected int createTroopAllocationProbe(TroopSelectionProbe_MultiGroup probe, int trialPartNum) {
		if(probe != null) {
			int numProbs = probe.getGroups() != null ? probe.getGroups().size() : defaultNumGroups;
			troopProbe = new TroopAllocationMultiGroupTrialPartState(trialNumber, trialPartNum, 
					new TroopAllocationProbe_MultiGroup(probe.getGroups()));
			if(participantTrialData != null) {
				troopProbe.setCurrentSettings(PlayerUtils.getPercentSettings(participantTrialData.getAllocation(), numProbs));
				troopProbe.setNormativeSettings(PlayerUtils.createNormativeTroopAllocation(groupProbe.getCurrentSettings()));
			} else {
				troopProbe.setCurrentSettings(PlayerUtils.createPercentProbabilities(numProbs, 0));
			}
			if(avgHumanTrialData != null) {
				troopProbe.setAvgHumanSettings(PlayerUtils.getPercentSettings(avgHumanTrialData.getAllocation(), numProbs));
			}
			trialParts.add(troopProbe);
			return trialPartNum + 1;
		}
		return trialPartNum;
	}
	
	/** Create the surprise probe */
	protected int createSurpriseProbe(SurpriseReportProbe probe, GroundTruth groundTruth, int trialPartNum) {
		if(probe != null) {
			surpriseProbe = new SurpriseProbeTrialPartState(trialNumber, trialPartNum, probe, groundTruth);
			trialParts.add(surpriseProbe);
			if(participantTrialData != null) {
				surpriseProbe.setCurrentSurprise(PlayerUtils.getSurpriseSetting(participantTrialData.getSurprise()));
			} else {
				surpriseProbe.setCurrentSurprise(0);
			}
			if(avgHumanTrialData != null) {
				surpriseProbe.setAvgHumanSurprise(PlayerUtils.getSurpriseSetting(avgHumanTrialData.getSurprise()));
			}
			return trialPartNum + 1;
		} 
		return trialPartNum;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.player.phase_1.data_model.trial_states.IPlayerTrialState#updateTrialData(org.mitre.icarus.cpa.phase_1.data_model.TrialData, org.mitre.icarus.cpa.phase_1.data_model.TrialData)
	 */
	@Override
	public void updateTrialData(TrialData participantTrialData, TrialData avgHumanTrialData) {
		this.participantTrialData = participantTrialData;
		this.avgHumanTrialData = avgHumanTrialData;
		
		//Update score
		if(participantTrialData != null) {
			setScore_s1(participantTrialData.getS1_score());
			setScore_s2(participantTrialData.getS2_score());
		}
		
		//Update the group probe data
		if(groupProbe != null) {	
			int numProbs = groupProbe.getProbe() != null ? groupProbe.getProbe().getGroups().size() : defaultNumGroups;
			if(participantTrialData != null) {
				groupProbe.setCurrentSettings(PlayerUtils.getPercentProbabilities(participantTrialData.getProbs(), numProbs, 0));
				groupProbe.setNormativeSettings(PlayerUtils.getPercentProbabilities(participantTrialData.getNormative_probs(), numProbs, 0));
			} else {
				groupProbe.setCurrentSettings(PlayerUtils.createPercentProbabilities(numProbs, 0));
			}
			if(avgHumanTrialData != null) {
				groupProbe.setAvgHumanSettings(PlayerUtils.getPercentProbabilities(avgHumanTrialData.getProbs(), numProbs, 0));
			}
		}
		
		//Update the troop probe data
		if(troopProbe != null) {			
			int numProbs = troopProbe.getProbe() != null ? troopProbe.getProbe().getGroups().size() : defaultNumGroups;
			if(participantTrialData != null) {
				troopProbe.setCurrentSettings(PlayerUtils.getPercentSettings(participantTrialData.getAllocation(), numProbs));
				troopProbe.setNormativeSettings(PlayerUtils.createNormativeTroopAllocation(groupProbe.getCurrentSettings()));
			} else {
				troopProbe.setCurrentSettings(PlayerUtils.createPercentProbabilities(numProbs, 0));
			}
			if(avgHumanTrialData != null) {
				troopProbe.setAvgHumanSettings(PlayerUtils.getPercentSettings(avgHumanTrialData.getAllocation(), numProbs));
			}
		}
		
		//Update the surprise probe data
		if(surpriseProbe != null) {			
			if(participantTrialData != null) {				
				surpriseProbe.setCurrentSurprise(PlayerUtils.getSurpriseSetting(participantTrialData.getSurprise()));
			} else {
				surpriseProbe.setCurrentSurprise(0);
			}
			if(avgHumanTrialData != null) {
				surpriseProbe.setAvgHumanSurprise(PlayerUtils.getSurpriseSetting(avgHumanTrialData.getSurprise()));
			}
		}
	}
}