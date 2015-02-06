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
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.LocationProbeTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.ShowScoreTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.SurpriseProbeTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.TroopAllocationMultiLocationTrialPartState;
import org.mitre.icarus.cps.app.util.ProbabilityUtils;
import org.mitre.icarus.cps.exam.base.IcarusExam.NormalizationMode;
import org.mitre.icarus.cps.exam.phase_1.response.Task_4_TrialResponse;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.INTLayerData;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.Task_4_7_AttackLocationProbeResponseAfterINT;
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
public class Task_4_TrialState extends TrialState_Phase1 {
	
	/** The task 4 trial */
	protected Task_4_Trial trial;	
	
	/** The initial location probe */
	protected LocationProbeTrialPartState locationProbeInitial;
	
	/** The locations probe(s) after each INT layer presentation */
	protected ArrayList<LocationProbeTrialPartState> intLayerProbes;
	
	/** The troop allocation probe */
	protected TroopAllocationMultiLocationTrialPartState troopProbe;
	
	/** The ground truth surprise probe */
	protected SurpriseProbeTrialPartState surpriseProbe;	
	
	public Task_4_TrialState(int trialNum, Task_4_Trial trial,
			NormalizationMode normalizationMode, boolean showScore) {
		super(trialNum);
		this.trial = trial;
		if(trial != null) {
			createTrialPartStates(normalizationMode, showScore);
		}
		else {
			trialParts = new ArrayList<TrialPartState>();
		}
	}
	
	protected Task_4_TrialState(int trialNum) {
		super(trialNum);
	}
	
	/**
	 * Create trial part states for the initial location probe, the INT layer presentation(s),
	 * the troop allocation probe, and the ground truth surprise probe.
	 * 
	 * @param normalizationMode
	 * @param showScore
	 */
	protected void createTrialPartStates(NormalizationMode normalizationMode, boolean showScore) {
		trialParts = new ArrayList<TrialPartState>();
		int trialPartNum = -1;
		
		//Add the initial location probe				
		AttackLocationProbe_MultiLocation locationProbe = trial.getAttackLocationProbe_initial();
		if(locationProbe != null) {
			locationProbeInitial = new LocationProbeTrialPartState(trialNumber, ++trialPartNum, locationProbe);
			locationProbeInitial.setCurrentSettings(ProbabilityUtils.createDefaultInitialProbabilities(
					locationProbe != null && locationProbe.getLocations() != null ? locationProbe.getLocations().size() : 0));
			locationProbeInitial.setPreviousSettings(ProbabilityUtils.createDefaultInitialProbabilities(
					locationProbeInitial.getCurrentSettings().size()));
			//previousSettings = CPSUtils.createDefaultInitialProbabilities(locationProbeInitial.getCurrentSettings().size());
			trialParts.add(locationProbeInitial);

			//Add a state to confirm the normalized location probe settings
			if(normalizationMode != null && normalizationMode == NormalizationMode.NormalizeAfterAndConfirm) {
				trialParts.add(new ConfirmSettingsTrialPartState(trialNumber, ++trialPartNum, locationProbeInitial));
			}
		}
		
		//Add a location probe for each INT layer presentation
		if(trial.getIntLayers() != null && !trial.getIntLayers().isEmpty()) {
			intLayerProbes = new ArrayList<LocationProbeTrialPartState>(trial.getIntLayers().size() );			
			for(Task_4_INTLayerPresentationProbe intLayer: trial.getIntLayers()) {				
				//Add a location probe that will also present the INT layer
				locationProbe = intLayer.getAttackLocationProbe();
				LocationProbeTrialPartState locationProbeAfterInt = 
					new LocationProbeTrialPartState(trialNumber, ++trialPartNum, locationProbe);
				locationProbeAfterInt.setCurrentSettings(ProbabilityUtils.createDefaultInitialProbabilities(
						locationProbe != null && locationProbe.getLocations() != null ? locationProbe.getLocations().size() : 0));
				locationProbeAfterInt.setPreviousSettings(ProbabilityUtils.createDefaultInitialProbabilities(
						locationProbeAfterInt.getCurrentSettings().size()));
				locationProbeAfterInt.setLayerToAdd(intLayer.getLayerType());
				intLayerProbes.add(locationProbeAfterInt);
				trialParts.add(locationProbeAfterInt);
									
				//Add a state to confirm the normalized location probe settings
				if(normalizationMode != null && normalizationMode == NormalizationMode.NormalizeAfterAndConfirm) {
					trialParts.add(new ConfirmSettingsTrialPartState(trialNumber, ++trialPartNum, locationProbeAfterInt));
				}
			}
		}
		
		//Add the troop allocation probe
		if(trial.getTroopAllocationProbe() != null) {
			TroopAllocationProbe_MultiLocation troopProbeTrial = trial.getTroopAllocationProbe();
			troopProbe = new TroopAllocationMultiLocationTrialPartState(
					trialNumber, ++trialPartNum, troopProbeTrial);
			troopProbe.setCurrentSettings(ProbabilityUtils.createDefaultInitialProbabilities(
					troopProbeTrial != null && troopProbeTrial.getLocations() != null ? troopProbeTrial.getLocations().size() : 0));
			troopProbe.setPreviousSettings(ProbabilityUtils.createDefaultInitialProbabilities(troopProbe.getCurrentSettings().size()));
			trialParts.add(troopProbe);

			//Add a state to confirm the normalized troop allocations
			if(normalizationMode != null && normalizationMode == NormalizationMode.NormalizeAfterAndConfirm) {
				trialParts.add(new ConfirmSettingsTrialPartState(trialNumber, ++trialPartNum, troopProbe));
			}
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
	public Task_4_Trial getTrial() {
		return trial;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment.phase_1.trial_states.TrialState#updateTrialResponseData()
	 */
	@Override
	public void updateTrialResponseData() {
		if(trial.getTrialResponse() == null) {
			trial.setTrialResponse(new Task_4_TrialResponse());
		}
		if(locationProbeInitial != null) {
			trial.getTrialResponse().setAttackLocationResponse_initial(locationProbeInitial.getResponse());
		}
		if(intLayerProbes != null && !intLayerProbes.isEmpty()) {
			ArrayList<Task_4_7_AttackLocationProbeResponseAfterINT> afterIntResponses = 
				new ArrayList<Task_4_7_AttackLocationProbeResponseAfterINT>(intLayerProbes.size());
			trial.getTrialResponse().setAttackLocationResponses_afterINTs(afterIntResponses);
			int index = 0;
			while(index < intLayerProbes.size()) {
				Task_4_7_AttackLocationProbeResponseAfterINT intResponse = new Task_4_7_AttackLocationProbeResponseAfterINT();
				afterIntResponses.add(intResponse);
				TrialPartState currPart = intLayerProbes.get(index);		
				
				if(currPart instanceof LocationProbeTrialPartState) {
					LocationProbeTrialPartState locationProbe = (LocationProbeTrialPartState)currPart;
					if(locationProbe.getLayerToAdd() != null) {
						intResponse.setIntLayerShown(new INTLayerData(locationProbe.getLayerToAdd(), false));
					}
					intResponse.setAttackLocationResponse(locationProbe.getResponse());
				}
				index++;
			}
		} else {
			trial.getTrialResponse().setAttackLocationResponses_afterINTs(null);	
		}		
		if(troopProbe != null) {
			trial.getTrialResponse().setTroopAllocationResponse(troopProbe.getResponse());
		}
		if(surpriseProbe != null) {
			trial.getTrialResponse().setGroundTruthSurpriseResponse(surpriseProbe.getResponse());
		}
		updateTimingData(trial);
	}
}