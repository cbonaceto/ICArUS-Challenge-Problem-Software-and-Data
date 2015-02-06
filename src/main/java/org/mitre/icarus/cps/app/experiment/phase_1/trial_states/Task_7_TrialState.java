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
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.GroupProbeTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.LayerSelectTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.LocationProbeTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.ShowScoreTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.TroopAllocationMultiLocationTrialPartState;
import org.mitre.icarus.cps.app.util.ProbabilityUtils;
import org.mitre.icarus.cps.exam.base.IcarusExam.NormalizationMode;
import org.mitre.icarus.cps.exam.phase_1.response.Task_7_TrialResponse;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.Task_7_INTLayerPurchase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_7_Trial;
import org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.Task_7_INTLayerPresentationProbe;
import org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.AttackLocationProbe_MultiGroup;
import org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.AttackLocationProbe_MultiLocation;
import org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.TroopAllocationProbe_MultiLocation;

/**
 * The trial state for a Task 7 trial.
 * 
 * @author CBONACETO
 *
 */
public class Task_7_TrialState extends TrialState_Phase1 {
	
	/** The task 7 trial */
	protected Task_7_Trial trial;
	
	/** The number of credits remaining */
	protected double creditsRemaining;
	
	/** The group probe at the start of the trial */
	protected GroupProbeTrialPartState groupProbeInitial;
	
	/** The location probe */
	protected LocationProbeTrialPartState locationProbeInitial;
	
	/** The troop allocation probe */
	protected TroopAllocationMultiLocationTrialPartState troopProbe;
	
	/** The layer foraging probe */
	protected LayerSelectTrialPartState<Task_7_INTLayerPresentationProbe> layerForageProbe;
	
	public Task_7_TrialState(int trialNumber, Task_7_Trial trial, 
			NormalizationMode normalizationMode, boolean createLayerForageProbe) {
		super(trialNumber);
		this.trial = trial;
		createTrialPartStates(normalizationMode, createLayerForageProbe);
	}
	
	protected Task_7_TrialState(int trialNumber) {
		super(trialNumber);
	}
	
	/**
	 * Create trial part states for the group probe, the location probe, the troop probe,
	 * and the layer forage probe.
	 * 
	 * @param normalizationMode
	 */
	protected void createTrialPartStates(NormalizationMode normalizationMode, boolean createLayerForageProbe) {
		trialParts = new ArrayList<TrialPartState>();
		int trialPartNum = -1;
		
		//Add the initial group probe
		if(trial.getAttackLocationProbe_groups() != null) {
			AttackLocationProbe_MultiGroup groupProbe = trial.getAttackLocationProbe_groups();
			groupProbeInitial = new GroupProbeTrialPartState(trialNumber, ++trialPartNum, groupProbe);
			groupProbeInitial.setCurrentSettings(ProbabilityUtils.createDefaultInitialProbabilities(
					groupProbe != null && groupProbe.getGroups() != null ? groupProbe.getGroups().size() : 0));
			groupProbeInitial.setPreviousSettings(ProbabilityUtils.createDefaultInitialProbabilities(
					groupProbeInitial.getCurrentSettings().size()));
			trialParts.add(groupProbeInitial);
			
			//Add a state to confirm the normalized group probe settings
			if(normalizationMode != null && normalizationMode == NormalizationMode.NormalizeAfterAndConfirm) {
				trialParts.add(new ConfirmSettingsTrialPartState(trialNumber, ++trialPartNum, groupProbeInitial));
			}
		}		
		
		//Add the location probe
		if(trial.getAttackLocationProbe_locations() != null) {
			AttackLocationProbe_MultiLocation locationProbe = trial.getAttackLocationProbe_locations();
			locationProbeInitial = new LocationProbeTrialPartState(trialNumber, ++trialPartNum, locationProbe);
			locationProbeInitial.setCurrentSettings(ProbabilityUtils.createDefaultInitialProbabilities(
					locationProbe != null && locationProbe.getLocations() != null ? locationProbe.getLocations().size() : 0));
			locationProbeInitial.setPreviousSettings(ProbabilityUtils.createDefaultInitialProbabilities(
					locationProbeInitial.getCurrentSettings().size()));
			trialParts.add(locationProbeInitial);

			//Add a state to confirm the normalized location probe settings
			if(normalizationMode != null && normalizationMode == NormalizationMode.NormalizeAfterAndConfirm) {
				trialParts.add(new ConfirmSettingsTrialPartState(trialNumber, ++trialPartNum, locationProbeInitial));
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
		
		//Add state to show ground truth and the number of credits awarded	
		trialParts.add(new ShowScoreTrialPartState(trialNumber, ++trialPartNum, this));		
		
		//Add the layer forage probe if we're not at the last trial
		if(createLayerForageProbe) {
			layerForageProbe = new LayerSelectTrialPartState<Task_7_INTLayerPresentationProbe>(trialNumber, ++trialPartNum);
			trialParts.add(layerForageProbe);
		}
	}

	/**
	 * Get the layer forage probe trial part state.
	 * 
	 * @return the layer forage probe trial part state
	 */
	public LayerSelectTrialPartState<Task_7_INTLayerPresentationProbe> getLayerForageProbe() {
		return layerForageProbe;
	}	

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment.phase_1.trial_states.TrialState#getTrial()
	 */
	@Override
	public Task_7_Trial getTrial() {
		return trial;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment.phase_1.trial_states.TrialState#updateTrialResponseData()
	 */
	@Override
	public void updateTrialResponseData() {
		if(trial.getTrialResponse() == null) {
			trial.setTrialResponse(new Task_7_TrialResponse());
		}
		if(groupProbeInitial != null) {
			trial.getTrialResponse().setAttackLocationResponse_groups(groupProbeInitial.getResponse());
		}
		if(locationProbeInitial != null) {
			trial.getTrialResponse().setAttackLocationResponse_locations(locationProbeInitial.getResponse());
		}
		if(troopProbe != null) {
			trial.getTrialResponse().setTroopAllocationResponse(troopProbe.getResponse());
		}
		if(layerForageProbe != null && layerForageProbe.getSelectedLayers() != null &&
				!layerForageProbe.getSelectedLayers().isEmpty()) {
			ArrayList<Task_7_INTLayerPurchase> intPurchases = 
				new ArrayList<Task_7_INTLayerPurchase>(layerForageProbe.getSelectedLayers().size());
			trial.getTrialResponse().setIntLayerPurchases(intPurchases);
			trial.getTrialResponse().setIntLayerPurchaseTime_ms(layerForageProbe.getTrialPartTime_ms());
			for(Task_7_INTLayerPresentationProbe layer : layerForageProbe.getSelectedLayers()) {
				intPurchases.add(new Task_7_INTLayerPurchase(layer.getLayerType(),
						layer.getCostCredits()));
			}
		}		
		updateTimingData(trial);		
	}		
}