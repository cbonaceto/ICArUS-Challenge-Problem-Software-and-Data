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
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.ShowHumintTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.ShowScoreTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.SurpriseProbeTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.TroopAllocationMultiGroupTrialPartState;
import org.mitre.icarus.cps.app.util.ProbabilityUtils;
import org.mitre.icarus.cps.exam.base.IcarusExam.NormalizationMode;
import org.mitre.icarus.cps.exam.phase_1.response.Task_5_6_TrialResponse;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.INTLayerData;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.Task_5_6_AttackLocationProbeResponseAfterINT;
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
public class Task_5_6_TrialState extends TrialState_Phase1 {
	
	/** The task 5 or 6 trial */
	protected Task_5_Trial trial;
	
	/** The group probes after each INT layer presentation or selection */
	protected ArrayList<TrialPartState> intLayerProbes;	
	
	/** The troop allocation probe */
	protected TroopAllocationMultiGroupTrialPartState troopProbe;
	
	/** The ground truth surprise probe */
	protected SurpriseProbeTrialPartState surpriseProbe;	

	public Task_5_6_TrialState(int trialNumber, Task_5_Trial trial,
			NormalizationMode normalizationMode, boolean showScore) {
		super(trialNumber);
		this.trial = trial;
		createTrialPartStates(normalizationMode, showScore, false, 
				trial.getIntLayers() != null ? trial.getIntLayers().size() : 0);
	}
	
	public Task_5_6_TrialState(int trialNumber, Task_6_Trial trial,
			NormalizationMode normalizationMode, boolean showScore) {
		super(trialNumber);		
		this.trial = (Task_5_Trial)trial;
		createTrialPartStates(normalizationMode, showScore, true, trial.getNumLayersToShow());
	}
	
	protected Task_5_6_TrialState(int trialNumber) {
		super(trialNumber);
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
	protected void createTrialPartStates(NormalizationMode normalizationMode, boolean showScore, 
			boolean userSelectLayers, int numLayersToPresent) {
		trialParts = new ArrayList<TrialPartState>();
		int trialPartNum = -1;
		
		//Add the initial group probe
		/*AttackLocationProbe_MultiGroup groupProbe = trial.getAttackLocationProbe_initial();
		if(groupProbe != null) {
			groupProbeInitial = new GroupProbeTrialPartState(trialNumber, ++trialPartNum, groupProbe);
			groupProbeInitial.setCurrentSettings(CPSUtils.createDefaultInitialProbabilities(
					groupProbe != null && groupProbe.getGroups() != null ? groupProbe.getGroups().size() : 0));
			groupProbeInitial.setPreviousSettings(CPSUtils.createDefaultInitialProbabilities(
					groupProbeInitial.getCurrentSettings().size()));
			//previousSettings = CPSUtils.createDefaultInitialProbabilities(groupProbeInitial.getCurrentSettings().size());
			trialParts.add(groupProbeInitial);

			//Add a state to confirm the normalized location probe settings
			if(normalizationMode != null && normalizationMode == NormalizationMode.NormalizeAfterAndConfirm) {
				trialParts.add(new ConfirmSettingsTrialPartState(trialNumber, ++trialPartNum, groupProbeInitial));
			}
		}*/
		
		//Add a group probe for each INT layer presentation
		if(trial.getIntLayers() != null && !trial.getIntLayers().isEmpty()) {
			intLayerProbes = new ArrayList<TrialPartState>();
			//intLayerProbesMap = new HashMap<IntType, Task_5_6_IntLayerPresentationProbe>();
			for(int i=0; i<numLayersToPresent; i++) {
				Task_5_6_INTLayerPresentationProbe intLayer = trial.getIntLayers().get(i);
				GroupProbeTrialPartState groupProbeAfterInt = null;
				if(userSelectLayers) {					
					//Add a user layer selection followed by a generic group probe. 
					//The actual group probe will be selected from the map that maps layers to probes.
					LayerSelectTrialPartState<Task_5_6_INTLayerPresentationProbe> layerSelect = 
						new LayerSelectTrialPartState<Task_5_6_INTLayerPresentationProbe>(trialNumber, ++trialPartNum);
					trialParts.add(layerSelect);
					intLayerProbes.add(layerSelect);
					if(i == 0 && trial.getInitialHumintReport() != null) {
						//Add the initial HUMINT report
						layerSelect.setInitialHumintReport(trial.getInitialHumintReport());
					}					
					
					//Add the group probe
					AttackLocationProbe_MultiGroup groupProbe = intLayer.getAttackLocationProbe();
					groupProbeAfterInt = new GroupProbeTrialPartState(trialNumber, ++trialPartNum);
					groupProbeAfterInt.setCurrentSettings(ProbabilityUtils.createDefaultInitialProbabilities(
							groupProbe != null && groupProbe.getGroups() != null ? groupProbe.getGroups().size() : 0));
					groupProbeAfterInt.setPreviousSettings(ProbabilityUtils.createDefaultInitialProbabilities(
							groupProbeAfterInt.getCurrentSettings().size()));
					layerSelect.setProbabilityProbe(groupProbeAfterInt);
					trialParts.add(groupProbeAfterInt);
					intLayerProbes.add(groupProbeAfterInt);	
				}
				else {		
					if(i == 0 && trial.getInitialHumintReport() != null) {
						//Add the initial HUMINT report
						trialParts.add(new ShowHumintTrialPartState(trialNumber, ++trialPartNum,
								trial.getInitialHumintReport()));
					}					
					
					//Add a group probe
					AttackLocationProbe_MultiGroup groupProbe = intLayer.getAttackLocationProbe();
					groupProbeAfterInt = new GroupProbeTrialPartState(trialNumber, ++trialPartNum, groupProbe);
					groupProbeAfterInt.setCurrentSettings(ProbabilityUtils.createDefaultInitialProbabilities(
							groupProbe != null && groupProbe.getGroups() != null ? groupProbe.getGroups().size() : 0));
					groupProbeAfterInt.setPreviousSettings(ProbabilityUtils.createDefaultInitialProbabilities(
							groupProbeAfterInt.getCurrentSettings().size()));
					groupProbeAfterInt.setLayerToAdd(intLayer.getLayerType());
					intLayerProbes.add(groupProbeAfterInt);					
					trialParts.add(groupProbeAfterInt);					
				}				
									
				//Add a state to confirm the normalized group probe settings
				if(normalizationMode != null && normalizationMode == NormalizationMode.NormalizeAfterAndConfirm) {
					trialParts.add(new ConfirmSettingsTrialPartState(trialNumber, ++trialPartNum, groupProbeAfterInt));
				}
			}
		}
		
		//Add the troop allocation probe
		if(trial.getTroopAllocationProbe() != null) {
			TroopAllocationProbe_MultiGroup troopProbeTrial = trial.getTroopAllocationProbe();
			troopProbe = new TroopAllocationMultiGroupTrialPartState(
					trialNumber, ++trialPartNum, troopProbeTrial);
			troopProbe.setCurrentSettings(ProbabilityUtils.createDefaultInitialProbabilities(
					troopProbeTrial != null && troopProbeTrial.getGroups() != null ? troopProbeTrial.getGroups().size() : 0));
			troopProbe.setPreviousSettings(ProbabilityUtils.createDefaultInitialProbabilities(troopProbe.getCurrentSettings().size()));
			trialParts.add(troopProbe);

			//Add a state to confirm the normalized troop allocations
			if(normalizationMode != null && normalizationMode == NormalizationMode.NormalizeAfterAndConfirm) {
				trialParts.add(new ConfirmSettingsTrialPartState(trialNumber, ++trialPartNum, troopProbe));
			}
		}		
		
		if(trial.getGroundTruthSurpriseProbe() != null) {
			//Add the ground truth surprise probe
			surpriseProbe = new SurpriseProbeTrialPartState(trialNumber, ++trialPartNum,
					trial.getGroundTruthSurpriseProbe(), trial.getGroundTruth());
			trialParts.add(surpriseProbe);
		} 
		/*else {
			//Add a ground truth presentation
			trialParts.add(new GroundTruthTrialPartState(trialNumber, ++trialPartNum, trial.getGroundTruth()));
		}*/
		
		//Add state to show the score
		if(showScore) {
			ShowScoreTrialPartState showScoreState = new ShowScoreTrialPartState(trialNumber, ++trialPartNum, this);
			if(trial.getGroundTruthSurpriseProbe() == null) {
				//Reveal ground truth with the score
				showScoreState.setGroundTruth(trial.getGroundTruth());
			}
			trialParts.add(showScoreState);
		}
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment.phase_1.trial_states.TrialState#getTrial()
	 */
	@Override
	public Task_5_Trial getTrial() {
		return trial;
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment.phase_1.trial_states.TrialState#updateTrialResponseData()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void updateTrialResponseData() {
		if(trial.getTrialResponse() == null) {
			trial.setTrialResponse(new Task_5_6_TrialResponse());
		}
		//if(groupProbeInitial != null) {
		//	trial.getTrialResponse().setAttackLocationResponse_initial(groupProbeInitial.getResponse());
		//}
		if(intLayerProbes != null && !intLayerProbes.isEmpty()) {
			ArrayList<Task_5_6_AttackLocationProbeResponseAfterINT> afterIntResponses = 
				new ArrayList<Task_5_6_AttackLocationProbeResponseAfterINT>(intLayerProbes.size());
			trial.getTrialResponse().setAttackLocationResponses_afterINTs(afterIntResponses);
			int index = 0;
			while(index < intLayerProbes.size()) {
				Task_5_6_AttackLocationProbeResponseAfterINT intResponse = new Task_5_6_AttackLocationProbeResponseAfterINT();
				afterIntResponses.add(intResponse);
				TrialPartState currPart = intLayerProbes.get(index);
				
				boolean layerAdded = false;
				if(currPart instanceof LayerSelectTrialPartState) {
					LayerSelectTrialPartState<Task_5_6_INTLayerPresentationProbe> layerSelect = 
						(LayerSelectTrialPartState<Task_5_6_INTLayerPresentationProbe>)currPart;
					Task_5_6_INTLayerPresentationProbe selectedLayer = layerSelect.getFirstSelectedLayer();
					if(selectedLayer != null) {
						layerAdded = true;
						intResponse.setIntLayerShown(new INTLayerData(selectedLayer.getLayerType(), true,
							layerSelect.getTrialPartTime_ms()));
						//intResponse.getIntLayerShown().setUserSelected(true);
					}
					index++;
					if(index < intLayerProbes.size()) {
						currPart = intLayerProbes.get(index);
					}
				}
				/*if(currPart instanceof SurpriseProbeTrialPartState) {
					SurpriseProbeTrialPartState surpriseProbe = (SurpriseProbeTrialPartState)currPart;
					if(intResponse.getIntLayerShown() == null && surpriseProbe.getLayerToAdd() != null) {
						intResponse.setIntLayerShown(new INTLayerData(surpriseProbe.getLayerToAdd(), false));
					}
					intResponse.setSurpriseReportResponse(surpriseProbe.getResponse());
					index++;
					if(index < intLayerProbes.size()) {
						currPart = intLayerProbes.get(index);
					}
				}*/
				if(currPart instanceof GroupProbeTrialPartState) {
					GroupProbeTrialPartState groupProbe = (GroupProbeTrialPartState)currPart;
					if(groupProbe.getLayerToAdd() != null && !layerAdded) {
						intResponse.setIntLayerShown(new INTLayerData(groupProbe.getLayerToAdd(), false));
					}
					intResponse.setAttackLocationResponse(groupProbe.getResponse());
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