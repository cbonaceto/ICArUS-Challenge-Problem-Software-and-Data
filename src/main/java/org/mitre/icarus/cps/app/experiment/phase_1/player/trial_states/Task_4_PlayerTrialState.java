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
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.Task_4_TrialState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.LocationProbeTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.SurpriseProbeTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.TroopAllocationMultiLocationTrialPartState;
import org.mitre.icarus.cps.app.util.ProbabilityUtils;
import org.mitre.icarus.cps.app.window.phase_1.PlayerUtils;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.TrialData;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_4_Trial;
import org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.Task_4_INTLayerPresentationProbe;
import org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.AttackLocationProbe_MultiLocation;
import org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.TroopAllocationProbe_MultiLocation;

/**
 * Trial state for a Task 4 trial.
 * 
 * @author CBONACETO
 *
 */
public class Task_4_PlayerTrialState extends Task_4_TrialState implements IPlayerTrialState {	
	
	/** The locations probe(s) after each INT layer presentation */
	protected ArrayList<LocationProbeTrialPartState> intLayerProbes;
	
	/** Trial data and metrics for the participant (also includes normative data) */
	protected TrialData participantTrialData;
	
	/** Trial data and metrics for average human */
	protected TrialData avgHumanTrialData;	
	
	public Task_4_PlayerTrialState(int trialNum, Task_4_Trial trial, 
			TrialData participantTrialData, TrialData avgHumanTrialData) {
		super(trialNum);
		this.trial = trial;
		this.participantTrialData = participantTrialData;
		this.avgHumanTrialData = avgHumanTrialData;
		if(participantTrialData != null) {
			setScore_s1(participantTrialData.getS1_score());
			setScore_s2(participantTrialData.getS2_score());
		}
		if(trial != null) {
			createTrialPartStates();
		}
		else {
			trialParts = new ArrayList<TrialPartState>();
		}
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

	public List<LocationProbeTrialPartState> getIntLayerProbes() {
		return intLayerProbes;
	}
	
	/** Return the participant's final probabilities set after the last INT layer was shown. */
	public List<Integer> getFinalSettings() {
		if(intLayerProbes != null && !intLayerProbes.isEmpty()) {
			return intLayerProbes.get(intLayerProbes.size()-1).getCurrentSettings();
		}
		return null;
	}
	
	public TroopAllocationMultiLocationTrialPartState getTroopProbe() {
		return troopProbe;
	}

	/**
	 * Create trial part states for the initial location probe, the INT layer presentation(s),
	 * the troop allocation probe, and the ground truth surprise probe.
	 * 
	 * @param normalizationMode
	 * @param showScore
	 */
	protected void createTrialPartStates() {
		trialParts = new ArrayList<TrialPartState>();
		int trialPartNum = -1;
		
		//Add the initial location probe				
		AttackLocationProbe_MultiLocation locationProbe = trial.getAttackLocationProbe_initial();
		int numProbs = locationProbe.getLocations() != null ? locationProbe.getLocations().size() : 4;
		if(locationProbe != null) {
			locationProbeInitial = new LocationProbeTrialPartState(trialNumber, ++trialPartNum, locationProbe);			
			if(participantTrialData != null) {
				locationProbeInitial.setCurrentSettings(PlayerUtils.getPercentProbabilities(participantTrialData.getProbs(), numProbs, 0));
				locationProbeInitial.setNormativeSettings(PlayerUtils.getPercentProbabilities(participantTrialData.getNormative_probs(), numProbs, 0));
			} else {
				locationProbeInitial.setCurrentSettings(PlayerUtils.createPercentProbabilities(numProbs, 0));
			}
			if(avgHumanTrialData != null) {
				locationProbeInitial.setAvgHumanSettings(PlayerUtils.getPercentProbabilities(avgHumanTrialData.getProbs(), numProbs, 0));
			} 			
			locationProbeInitial.setPreviousSettings(ProbabilityUtils.createDefaultInitialProbabilities(
					locationProbeInitial.getCurrentSettings().size()));			
			trialParts.add(locationProbeInitial);			
		}
		
		//Add a location probe for each INT layer presentation
		if(trial.getIntLayers() != null && !trial.getIntLayers().isEmpty()) {
			intLayerProbes = new ArrayList<LocationProbeTrialPartState>(trial.getIntLayers().size());
			int i = 0;
			for(Task_4_INTLayerPresentationProbe intLayer: trial.getIntLayers()) {
				locationProbe = intLayer.getAttackLocationProbe();
				LocationProbeTrialPartState locationProbeAfterInt = 
					new LocationProbeTrialPartState(trialNumber, ++trialPartNum, locationProbe);
				locationProbeAfterInt.setLayerToAdd(intLayer.getLayerType());
				if(i == 0) {
					locationProbeAfterInt.setPreviousSettings(locationProbeInitial.getCurrentSettings());
				} else {
					locationProbeAfterInt.setPreviousSettings(intLayerProbes.get(i-1).getCurrentSettings());
				}
				if(participantTrialData != null) {
					locationProbeAfterInt.setCurrentSettings(PlayerUtils.getPercentProbabilities(participantTrialData.getProbs(), numProbs, i+1));
					locationProbeAfterInt.setNormativeSettings(PlayerUtils.getPercentProbabilities(participantTrialData.getNormative_probs(), numProbs, i+1));
				} else {
					locationProbeAfterInt.setCurrentSettings(PlayerUtils.createPercentProbabilities(numProbs, 0));
				}
				if(avgHumanTrialData != null) {
					locationProbeAfterInt.setAvgHumanSettings(PlayerUtils.getPercentProbabilities(avgHumanTrialData.getProbs(), numProbs, i+1));
				}				
				intLayerProbes.add(locationProbeAfterInt);
				trialParts.add(locationProbeAfterInt);				
				i++;
			}
		}
		
		//Add the troop allocation probe
		if(trial.getTroopAllocationProbe() != null) {
			TroopAllocationProbe_MultiLocation troopProbeTrial = trial.getTroopAllocationProbe();
			troopProbe = new TroopAllocationMultiLocationTrialPartState(
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
		
		//Add the ground truth surprise probe
		if(trial.getGroundTruthSurpriseProbe() != null) {
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
		
		//Update the initial location probe data
		AttackLocationProbe_MultiLocation locationProbe = trial.getAttackLocationProbe_initial();
		int numProbs = locationProbe.getLocations() != null ? locationProbe.getLocations().size() : 4;
		if(locationProbeInitial != null) {
			if(participantTrialData != null) {
				locationProbeInitial.setCurrentSettings(PlayerUtils.getPercentProbabilities(participantTrialData.getProbs(), numProbs, 0));
				locationProbeInitial.setNormativeSettings(PlayerUtils.getPercentProbabilities(participantTrialData.getNormative_probs(), numProbs, 0));
			} else {
				locationProbeInitial.setCurrentSettings(PlayerUtils.createPercentProbabilities(numProbs, 0));
			}
			if(avgHumanTrialData != null) {
				locationProbeInitial.setAvgHumanSettings(PlayerUtils.getPercentProbabilities(avgHumanTrialData.getProbs(), numProbs, 0));
			}
		}
		
		//Update the location probe data after each INT layer presentation
		if(intLayerProbes != null && !intLayerProbes.isEmpty()) {
			int i = 0;
			for(LocationProbeTrialPartState locationProbeAfterInt : intLayerProbes) {
				if(i == 0) {
					locationProbeAfterInt.setPreviousSettings(locationProbeInitial.getCurrentSettings());
				} else {
					locationProbeAfterInt.setPreviousSettings(intLayerProbes.get(i-1).getCurrentSettings());
				}
				if(participantTrialData != null) {
					locationProbeAfterInt.setCurrentSettings(PlayerUtils.getPercentProbabilities(participantTrialData.getProbs(), numProbs, i+1));
					locationProbeAfterInt.setNormativeSettings(PlayerUtils.getPercentProbabilities(participantTrialData.getNormative_probs(), numProbs, i+1));
				} else {
					locationProbeAfterInt.setCurrentSettings(PlayerUtils.createPercentProbabilities(numProbs, 0));
				}
				if(avgHumanTrialData != null) {
					locationProbeAfterInt.setAvgHumanSettings(PlayerUtils.getPercentProbabilities(avgHumanTrialData.getProbs(), numProbs, i+1));
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