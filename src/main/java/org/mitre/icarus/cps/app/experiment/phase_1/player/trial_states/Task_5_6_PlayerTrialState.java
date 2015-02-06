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
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.Task_5_6_TrialState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.GroupProbeTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.SurpriseProbeTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.TroopAllocationMultiGroupTrialPartState;
import org.mitre.icarus.cps.app.window.phase_1.PlayerUtils;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.TrialData;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_5_Trial;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_6_Trial;
import org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.Task_5_6_INTLayerPresentationProbe;
import org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.AttackLocationProbe_MultiGroup;
import org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.TroopAllocationProbe_MultiGroup;

/**
 * The trial state for a Task 5 or 6 trial.
 * 
 * @author CBONACETO
 *
 */
public class Task_5_6_PlayerTrialState extends Task_5_6_TrialState implements IPlayerTrialState {
	
	/** The group probes after each INT layer presentation or selection */
	protected ArrayList<GroupProbeTrialPartState> intLayerProbes;	
	
	/** Trial data and metrics for the participant (also includes normative data) */
	protected TrialData participantTrialData;
	
	/** Trial data and metrics for average human */
	protected TrialData avgHumanTrialData;	

	public Task_5_6_PlayerTrialState(int trialNumber, Task_5_Trial trial, 
			TrialData participantTrialData, TrialData avgHumanTrialData) {
		super(trialNumber);
		this.trial = trial;
		this.participantTrialData = participantTrialData;
		this.avgHumanTrialData = avgHumanTrialData;
		if(participantTrialData != null) {
			setScore_s1(participantTrialData.getS1_score());
			setScore_s2(participantTrialData.getS2_score());
		}
		createTrialPartStates(false, trial.getIntLayers() != null ? trial.getIntLayers().size() : 0);
	}
	
	public Task_5_6_PlayerTrialState(int trialNumber, Task_6_Trial trial, 
			TrialData participantTrialData, TrialData avgHumanTrialData) {
		super(trialNumber);		
		this.trial = (Task_5_Trial)trial;
		this.participantTrialData = participantTrialData;
		this.avgHumanTrialData = avgHumanTrialData;
		if(participantTrialData != null) {
			setScore_s1(participantTrialData.getS1_score());
			setScore_s2(participantTrialData.getS2_score());
		}
		createTrialPartStates(true, trial.getNumLayersToShow());
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
	
	public int getNumIntLayers() {
		return intLayerProbes != null ? intLayerProbes.size() : 0;
	}
	
	public List<GroupProbeTrialPartState> getIntLayerProbes() {
		return intLayerProbes;
	}
	
	/** Return the participant's final probabilities set after the last INT layer was shown. */
	public List<Integer> getFinalSettings() {
		if(intLayerProbes != null && !intLayerProbes.isEmpty()) {
			return intLayerProbes.get(intLayerProbes.size()-1).getCurrentSettings();
		}
		return null;
	}
	
	public TroopAllocationMultiGroupTrialPartState getTroopProbe() {
		return troopProbe;
	}

	/**
	 * Create trial part states for the initial group probe, the INT layer presentation(s),
	 * the troop allocation probe, and the ground truth surprise probe.
	 * 
	 * @param normalizationMode
	 * @param showScore
	 * @param userSelectLayers
	 * @param numLayersToPresent
	 */
	protected void createTrialPartStates(boolean userSelectLayers, int numLayersToPresent) {
		trialParts = new ArrayList<TrialPartState>();
		int trialPartNum = -1;
		int numProbs = trial.getInitialHumintReport() != null && trial.getInitialHumintReport().getGroups() != null ? 
				trial.getInitialHumintReport().getGroups().size() : 4;
		
		//Add a group probe for each INT layer presentation
		if(trial.getIntLayers() != null && !trial.getIntLayers().isEmpty()) {
			intLayerProbes = new ArrayList<GroupProbeTrialPartState>(trial.getIntLayers().size());
			for(int i=0; i<numLayersToPresent; i++) {
				Task_5_6_INTLayerPresentationProbe intLayer = trial.getIntLayers().get(i);
				GroupProbeTrialPartState groupProbeAfterInt = null;
				AttackLocationProbe_MultiGroup groupProbe = intLayer.getAttackLocationProbe();
				groupProbeAfterInt = new GroupProbeTrialPartState(trialNumber, ++trialPartNum, groupProbe);				
				groupProbeAfterInt.setLayerToAdd(intLayer.getLayerType());				
				if(i == 0) {
					groupProbeAfterInt.setPreviousSettings(PlayerUtils.getPercentSettings(
							trial.getInitialHumintReport() != null ? trial.getInitialHumintReport().getHumintProbabilities() : null, numProbs));
				} else {
					groupProbeAfterInt.setPreviousSettings(intLayerProbes.get(i-1).getCurrentSettings());
				}
				if(participantTrialData != null) {				
					groupProbeAfterInt.setCurrentSettings(PlayerUtils.getPercentProbabilities(participantTrialData.getProbs(), numProbs, i+1));
					groupProbeAfterInt.setNormativeSettings(PlayerUtils.getPercentProbabilities(participantTrialData.getNormative_probs(), numProbs, i+1));
				} else {
					groupProbeAfterInt.setCurrentSettings(PlayerUtils.createPercentProbabilities(numProbs, 0));
				}
				if(avgHumanTrialData != null) {
					groupProbeAfterInt.setAvgHumanSettings(PlayerUtils.getPercentProbabilities(avgHumanTrialData.getProbs(), numProbs, i+1));
				} 				
				intLayerProbes.add(groupProbeAfterInt);					
				trialParts.add(groupProbeAfterInt);					
			}	
		}
		
		//Add the troop allocation probe
		if(trial.getTroopAllocationProbe() != null) {
			TroopAllocationProbe_MultiGroup troopProbeTrial = trial.getTroopAllocationProbe();
			troopProbe = new TroopAllocationMultiGroupTrialPartState(
					trialNumber, ++trialPartNum, troopProbeTrial);
			if(participantTrialData != null) {
				troopProbe.setCurrentSettings(PlayerUtils.getPercentSettings(participantTrialData.getAllocation(), numProbs));
				troopProbe.setNormativeSettings(PlayerUtils.createNormativeTroopAllocation(getFinalSettings()));
			} else {
				troopProbe.setCurrentSettings(PlayerUtils.createPercentProbabilities(numProbs, 0));
			}
			if(avgHumanTrialData != null) {
				troopProbe.setAvgHumanSettings(PlayerUtils.getPercentSettings(avgHumanTrialData.getAllocation(), numProbs));
			} 
			trialParts.add(troopProbe);
		}		
		
		if(trial.getGroundTruthSurpriseProbe() != null) {
			//Add the ground truth surprise probe
			surpriseProbe = new SurpriseProbeTrialPartState(trialNumber, ++trialPartNum,
					trial.getGroundTruthSurpriseProbe(), trial.getGroundTruth());
			if(participantTrialData != null) {
				surpriseProbe.setCurrentSurprise(PlayerUtils.getSurpriseSetting(participantTrialData.getSurprise()));
			} else {
				surpriseProbe.setCurrentSurprise(0);
			}
			if(avgHumanTrialData != null) {
				surpriseProbe.setAvgHumanSurprise(PlayerUtils.getSurpriseSetting(avgHumanTrialData.getSurprise()));
			} 
			trialParts.add(surpriseProbe);
		}		
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
		
		//Update the group probe data after each INT layer presentation
		int numProbs = trial.getInitialHumintReport() != null && trial.getInitialHumintReport().getGroups() != null ? 
				trial.getInitialHumintReport().getGroups().size() : 4;
		if(intLayerProbes != null && !intLayerProbes.isEmpty()) {
			int i = 0;
			for(GroupProbeTrialPartState groupProbeAfterInt : intLayerProbes) {
				if(i == 0) {
					groupProbeAfterInt.setPreviousSettings(PlayerUtils.getPercentSettings(
							trial.getInitialHumintReport() != null ? trial.getInitialHumintReport().getHumintProbabilities() : null, numProbs));
				} else {
					groupProbeAfterInt.setPreviousSettings(intLayerProbes.get(i-1).getCurrentSettings());
				}
				if(participantTrialData != null) {
					groupProbeAfterInt.setCurrentSettings(PlayerUtils.getPercentProbabilities(participantTrialData.getProbs(), numProbs, i+1));
					groupProbeAfterInt.setNormativeSettings(PlayerUtils.getPercentProbabilities(participantTrialData.getNormative_probs(), numProbs, i+1));
				} else {
					groupProbeAfterInt.setCurrentSettings(PlayerUtils.createPercentProbabilities(numProbs, 0));
				}
				if(avgHumanTrialData != null) {
					groupProbeAfterInt.setAvgHumanSettings(PlayerUtils.getPercentProbabilities(avgHumanTrialData.getProbs(), numProbs, i+1));
				}
				i++;
			}
		}
		
		//Update the troop probe data
		if(troopProbe != null) {
			if(participantTrialData != null) {
				troopProbe.setCurrentSettings(PlayerUtils.getPercentSettings(participantTrialData.getAllocation(), numProbs));
				troopProbe.setNormativeSettings(PlayerUtils.createNormativeTroopAllocation(getFinalSettings()));
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