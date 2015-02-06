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
package org.mitre.icarus.cps.app.experiment.phase_1;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.Task_5_6_TrialState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.TrialState_Phase1;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.ConfirmSettingsTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.GroupProbeTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.LayerSelectTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.ProbabilityProbeTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.ShowHumintTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.ShowScoreTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.SurpriseProbeTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.TroopAllocationMultiGroupTrialPartState;
import org.mitre.icarus.cps.app.util.ProbabilityUtils;
import org.mitre.icarus.cps.app.widgets.WidgetConstants;
import org.mitre.icarus.cps.app.widgets.ImageManager.IconSize;
import org.mitre.icarus.cps.app.widgets.map.phase_1.MapPanelContainer;
import org.mitre.icarus.cps.app.widgets.map.phase_1.PlacemarkFactory.AttackLocationType;
import org.mitre.icarus.cps.app.widgets.map.phase_1.objects.AttackLocationPlacemark;
import org.mitre.icarus.cps.app.widgets.phase_1.ColorManager_Phase1;
import org.mitre.icarus.cps.app.widgets.phase_1.ImageManager_Phase1;
import org.mitre.icarus.cps.app.widgets.phase_1.experiment.LayerSelectionPanel;
import org.mitre.icarus.cps.exam.phase_1.testing.GroundTruth;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_4_5_6_PhaseBase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_4_5_6_TrialBase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_5_Trial;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_6_Trial;
import org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.Task_5_6_INTLayerPresentationProbe;
import org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.SurpriseReportProbe;
import org.mitre.icarus.cps.experiment_core.event.ConditionEvent;
import org.mitre.icarus.cps.experiment_core.event.SubjectActionEvent;
import org.mitre.icarus.cps.experiment_core.gui.INavButtonPanel.ButtonType;
import org.mitre.icarus.cps.experiment_core.subject_data.SubjectConditionData;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupCenter;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupType;
import org.mitre.icarus.cps.feature_vector.phase_1.IntType;

/**
 * Controller for Tasks 5 and 6.
 * 
 * @author CBONACETO
 *
 */
public class Task_5_6_Controller extends Task_4_5_6_7_ControllerBase<Task_4_5_6_PhaseBase<?>> {
	
	/** The trial states for each trial in the task */
	protected List<Task_5_6_TrialState> trialStates;
	
	/** The trial state for the current trial */
	protected Task_5_6_TrialState currentTrialState;
	
	/** The layer selection panel (Task 6) */
	protected LayerSelectionPanel<Task_5_6_INTLayerPresentationProbe> layerPanel;
	
	protected boolean initialLayersSet = false;	
	
	protected int numLayersShown;

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment.phase_1.TaskController#initializeTaskController(org.mitre.icarus.cps.exam.phase_1.testing.TaskTestPhase, org.mitre.icarus.cps.gui.phase_1.experiment.ConditionPanel_Phase1)
	 */
	@Override
	public void initializeTask(Task_4_5_6_PhaseBase<?> task) {	
		this.task = task;		
		Boolean reverse = task.isReverseLayerOrder();
		reverseLayerOrder = (reverse != null) ? reverse : false;
		if(layerPanel == null) {
			layerPanel = new LayerSelectionPanel<Task_5_6_INTLayerPresentationProbe>("layers");
			layerPanel.setButtonActionListener(this);
		}
		taskInputPanel.setLayerSelectionPanel(layerPanel);
		
		//Create the trial states for each trial
		trialStates = new ArrayList<Task_5_6_TrialState>();
		locationsPresent.clear();
		groupCenterGroupsPresent.clear();
		intLayersPresent.clear();
		socintGroupsPresent.clear();
		if(task.getTestTrials() != null && !task.getTestTrials().isEmpty()) {
			int trialNum = 0;
			for(Task_4_5_6_TrialBase trial : task.getTestTrials()) {
				if(trial instanceof Task_6_Trial) {
					Task_6_Trial task6Trial = (Task_6_Trial)trial;
					task6Trial.setTrialResponse(null);
					trialStates.add(new Task_5_6_TrialState(trialNum, task6Trial,
							exam.getNormalizationMode(), showScore));
					locationsPresent.add(task6Trial.getAttackLocation());
					groupCenterGroupsPresent.addAll(getGroupCenterGroups(task6Trial.getGroupCenters()));
				}
				else if(trial instanceof Task_5_Trial) {
					Task_5_Trial task5Trial = (Task_5_Trial)trial;
					task5Trial.setTrialResponse(null);
					trialStates.add(new Task_5_6_TrialState(trialNum, task5Trial,
							exam.getNormalizationMode(), showScore));
					locationsPresent.add(task5Trial.getAttackLocation());
					groupCenterGroupsPresent.addAll(getGroupCenterGroups(task5Trial.getGroupCenters()));
				}
				intLayersPresent.addAll(getIntLayerTypesForTrial((Task_5_Trial)trial));
				if(trial.getRegionsOverlay() != null && trial.getRegionsOverlay().getGroups() != null) {
					socintGroupsPresent.addAll(trial.getRegionsOverlay().getGroups());
				}
				trialNum++;
			}			
		}
	}	
	
	protected List<IntType> getIntLayerTypesForTrial(Task_5_Trial trial) {
		ArrayList<IntType> intLayers = null;
		if(trial.getIntLayers() != null && !trial.getIntLayers().isEmpty()) {
			intLayers = new ArrayList<IntType>(trial.getIntLayers().size());
			for(Task_5_6_INTLayerPresentationProbe layer : trial.getIntLayers()) {
				intLayers.add(layer.getLayerType().getLayerType());
			}
		}
		return intLayers;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment.phase_1.TaskController#getTaskWithResponseData()
	 */
	@Override
	public Task_4_5_6_PhaseBase<?> getTaskWithResponseData() {
		if(trialStates != null && !trialStates.isEmpty()) {
			for(TrialState_Phase1 trialState : trialStates) {
				trialState.updateTrialResponseData();
			}	
		}		
		return task;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment_core.controller.IConditionController#startCondition(int, org.mitre.icarus.cps.experiment_core.controller.IExperimentController, org.mitre.icarus.cps.experiment_core.subject_data.SubjectConditionData)
	 */
	@Override
	public void startCondition(int firstTrial, IcarusExamController_Phase1 experimentController,
			SubjectConditionData subjectConditionData) {
		conditionRunning = true;		
		this.examController = experimentController;
		
		if(examController != null) {
			examController.setNavButtonEnabled(ButtonType.Back, false);
			examController.setNavButtonEnabled(ButtonType.Next, false);
		}
		
		//Clear the map
		MapPanelContainer mapPanel = conditionPanel.getMapPanel();
		groupCenters = null;
		mapPanel.resetMap();
		mapPanel.setLayerInstructionsEnabled(true);	
		
		//Add the roads layer to the map
		mapPanel.setRoadLayerEnabled(true);
		mapPanel.setRoadsLegendItemVisible(true);

		//Add the SIGACTs layer to the map and call it "Attack Location"
		mapPanel.setSigactLayerEnabled(true);
		mapPanel.setSigactsLayerAndLegendName("Attack Location");
		
		//Add the SIGACTs legend items to the map for each location
		mapPanel.setSigactsLegendItemVisible(!locationsPresent.isEmpty());
		mapPanel.setSigactLocationsInLegend(locationsPresent);
		
		//Add the group centers layer to the map
		mapPanel.setGroupCentersLayerEnabled(true);
		mapPanel.setGroupCentersLayerEditable(false);
		mapPanel.setGroupCentersLayerAndLegendName("HUMINT");
		
		//Add the group centers legend item to the map
		mapPanel.setGroupCentersLegendItemVisible(!groupCenterGroupsPresent.isEmpty());
		mapPanel.setGroupCenterGroupsForLegend(groupCenterGroupsPresent);
		
		//Add the legend items for each INT layer present
		addIntLayersToLegend(intLayersPresent, reverseLayerOrder);
		
		mapPanel.setShowScale(true);
		mapPanel.redrawMap();
		
		//Clear the task input panel
		conditionPanel.setTaskInputPanelVisible(true);
		taskInputPanel.setAllSubPanelsVisible(false);
		conditionPanel.showTaskScreen();
		
		//Load the first trial
		currentTrialState = null;
		currentTrialPart = null;
		currentTrial = -1;
		trialPhase = -1;
		nextTrial();		
	}
	
	/*@Override
	public void stopCondition() {
		super.stopCondition();
		if(layerPanel != null) {
			layerPanel.removeButtonActionListener();
		}
	}*/
	
	/**
	 * Go to the next trial or trial part in the task.
	 */
	@SuppressWarnings("unchecked")
	protected void nextTrial() {		
		if(!conditionRunning) {
			return;
		}		
	
		examController.setNavButtonEnabled(ButtonType.Back, false);	
		examController.setNavButtonEnabled(ButtonType.Next, false);
		
		long currentTime = System.currentTimeMillis();
		
		//Save data, validate data, and perform clean-up from the previous trial part
		boolean responseValid = true;
		if(currentTrialPart != null) {
			currentTrialPart.setTrialPartTime_ms(currentTrialPart.getTrialPartTime_ms() + 
					(currentTime - trialPartStartTime));
			if(currentTrialPart instanceof GroupProbeTrialPartState) {
				if(taskInputPanel.getGroupProbeComponent() != null) {
					GroupProbeTrialPartState groupProbe = (GroupProbeTrialPartState)currentTrialPart;
					groupProbe.setInteractionTimes(taskInputPanel.getGroupProbeComponent().getInteractionTimes());
					if(!adjustingNormalizedSettings) {
						groupProbe.setPreviousSettings(groupProbe.getCurrentSettings());
					}
					groupProbe.setCurrentSettings(taskInputPanel.getGroupProbeComponent().getCurrentSettings());
					if(groupProbe.getCurrentNormalizedSettings() == null || 
							groupProbe.getCurrentNormalizedSettings().size() != groupProbe.getCurrentSettings().size()) {
						groupProbe.setCurrentNormalizedSettings(
								ProbabilityUtils.createDefaultInitialProbabilities(groupProbe.getCurrentSettings().size()));
					}
					ProbabilityUtils.normalizePercentProbabilities(groupProbe.getCurrentSettings(), groupProbe.getCurrentNormalizedSettings());
					previousSettings = groupProbe.getCurrentNormalizedSettings();					
				}
			} else if(currentTrialPart instanceof LayerSelectTrialPartState) {				
				Task_5_6_INTLayerPresentationProbe selectedLayer = layerPanel.getSelectedLayer();
				if(selectedLayer != null) {					
					LayerSelectTrialPartState<Task_5_6_INTLayerPresentationProbe> layerSelect = 
						(LayerSelectTrialPartState<Task_5_6_INTLayerPresentationProbe>)currentTrialPart;
					layerSelect.setSelectedLayers(Collections.singleton(selectedLayer));
					/*if(layerSelect.getSurpriseProbe() != null) {
						//The surprise probe is being phased out
						layerSelect.getSurpriseProbe().setLayerToAdd(selectedLayer.getLayerType());
						layerSelect.getSurpriseProbe().setProbe(selectedLayer.getSurpriseProbe());
					}*/
					GroupProbeTrialPartState groupProbe = (GroupProbeTrialPartState)layerSelect.getProbabilityProbe();
					groupProbe.setProbe(selectedLayer.getAttackLocationProbe());
					//if(layerSelect.getSurpriseProbe() == null) {
					groupProbe.setLayerToAdd(selectedLayer.getLayerType());
					//}
					
					//If a SIGINT layer was selected disable all SIGINT layers.  Otherwise, just disable the selected layer
					if(selectedLayer.getLayerType().getLayerType() == IntType.SIGINT) {
						layerPanel.setSigintLayersEnabled(false);
						//layerPanel.removeSigintLayers();
					} else {
						layerPanel.setLayerEnabled(selectedLayer, false);
						//layerPanel.removeLayer(selectedLayer);
					}
				} else {
					responseValid = false;
				}
			} else if(currentTrialPart instanceof TroopAllocationMultiGroupTrialPartState) {
				if(taskInputPanel.getTroopAllocationMultiGroupComponent() != null) {
					TroopAllocationMultiGroupTrialPartState troopProbe = (TroopAllocationMultiGroupTrialPartState)currentTrialPart;
					troopProbe.setInteractionTimes(taskInputPanel.getTroopAllocationMultiGroupComponent().getInteractionTimes());
					troopProbe.setCurrentSettings(taskInputPanel.getTroopAllocationMultiGroupComponent().getCurrentSettings());
					if(troopProbe.getCurrentNormalizedSettings() == null || 
							troopProbe.getCurrentNormalizedSettings().size() != troopProbe.getCurrentSettings().size()) {
						troopProbe.setCurrentNormalizedSettings(
								ProbabilityUtils.createDefaultInitialProbabilities(troopProbe.getCurrentSettings().size()));
					}
					ProbabilityUtils.normalizePercentProbabilities(troopProbe.getCurrentSettings(), troopProbe.getCurrentNormalizedSettings());
					previousTroopAllocations = troopProbe.getCurrentNormalizedSettings();
					int groundTruthIndex = troopProbe.getProbe().getGroups().indexOf(currentTrialState.getTrial().getGroundTruth().getResponsibleGroup());
					if(groundTruthIndex >= 0) {
						if(examController.getScoreComputer() != null) {
							currentTrialState.setScore_s2(examController.getScoreComputer().computeTroopAllocationScoreS2(
									troopProbe.getCurrentNormalizedSettings(), groundTruthIndex));
						}
					}
				}
			}
			else if(currentTrialPart instanceof SurpriseProbeTrialPartState) {
				if(taskInputPanel.getSurpriseEntryComponent() != null) {
					//Validate that a surprise value was entered
					if(taskInputPanel.getSurpriseEntryComponent().getSelectedIndex() < 0) {
						//conditionPanel.setInstructionBannerText("Please indicate your surprise and click Next to continue.");
						responseValid = false;
					}
					else {
						SurpriseProbeTrialPartState surpriseProbe = (SurpriseProbeTrialPartState)currentTrialPart;
						surpriseProbe.setCurrentSurprise(taskInputPanel.getSurpriseEntryComponent().getSelectedIndex());
						taskInputPanel.getSurpriseEntryComponent().setEnabled(false);
					}
				}
			}
		}				
		
		if(!responseValid) {
			//Don't advance if the response wasn't valid
			examController.setNavButtonEnabled(ButtonType.Next, true);
			trialPartStartTime = currentTime;
			return;
		}
		
		//Advance the trial phase counter
		trialPhase++;
		trialPartStartTime = currentTime;
		
		//Advance the trial counter if we're at the first trial, the current trial doesn't have any trial parts,
		//or we're at the last trial part of the current trial.
		if(currentTrial == -1 || (currentTrialState != null && 
				(currentTrialState.getTrialParts() == null || trialPhase >= currentTrialState.getTrialParts().size()))) {
			if(currentTrialState != null) {
				currentTrialState.setTrialTime_ms(currentTime - trialStartTime);
			}
			currentTrial++;
			trialStartTime = currentTime;
			trialPhase = 0;			
			if(trialStates == null || currentTrial >= trialStates.size()) {
				//We're at the end of the task
				conditionRunning = false;
				
				if(taskInputPanel.getSurpriseEntryComponent() != null) {
					taskInputPanel.getSurpriseEntryComponent().removeButtonActionListener(this);
				}
				/*if(layerPanel != null) {
					layerPanel.removeButtonActionListener(this);
				}*/

				// Fire condition completed event
				fireConditionEvent(new ConditionEvent(ConditionEvent.CONDITION_COMPLETED, 
						currentTrial, this));
				
				return;
			}
			else {				
				//Set the current trial
				currentTrialState = trialStates.get(currentTrial);
				currentTrialPart = null;
				previousSettings = null;
				showPreviousSettings = false;
				initialLayersSet = false;				
				numLayersShown = 0;
				//super.setTrial(currentTrial, trialPhase, currentTrialState.getNumTrialParts());	
				
				//Clear the INT layers from the map
				conditionPanel.getMapPanel().setAdditionalINTLayersEnabled(false);
				
				//Add the group centers to the map
				groupCenters = conditionPanel.getMapPanel().setGroupCenters(currentTrialState.getTrial().getGroupCenters(), false);
				if(currentTrialState.getTrial().getGroupCenters() != null && !currentTrialState.getTrial().getGroupCenters().isEmpty()) {
					ArrayList<GroupType> groups = new ArrayList<GroupType>(currentTrialState.getTrial().getGroupCenters().size());
					for(GroupCenter groupCenter : currentTrialState.getTrial().getGroupCenters()) {
						groups.add(groupCenter.getGroup());
					}
					//conditionPanel.getMapPanel().setGroupCenterGroupsForLegend(groups);
				}				
				
				//Add the attack location for the trial to the map
				attackLocations = conditionPanel.getMapPanel().setSigactLocations(
						Collections.singleton(currentTrialState.getTrial().getAttackLocation()), AttackLocationType.LOCATION, false, false);
				//conditionPanel.getMapPanel().setSigactLocationsInLegend(
				//		Collections.singleton(currentTrialState.getTrial().getAttackLocation()));				
				
				//Add the roads for the trial to the map
				conditionPanel.getMapPanel().setRoads(currentTrialState.getTrial().getRoads());
				
				conditionPanel.getMapPanel().redrawMap();
			}
		}
		
		//Fire a trial changed event
		super.setTrial(currentTrial, trialPhase, currentTrialState.getNumTrialParts());

		//Show the trial part			
		currentTrialPart = currentTrialState.getTrialParts().get(trialPhase);

		if(currentTrialPart instanceof ShowHumintTrialPartState) {		
			ShowHumintTrialPartState showHumintState = (ShowHumintTrialPartState)currentTrialPart;
			StringBuilder instructions = createTrialNumberString(currentTrial+1, trialPhase+1, 
					currentTrialState.getNumTrialParts(), "HUMINT Report");
			instructions.append("Probabilities have been initialized from HUMINT reports. Click Next to continue.");
			previousSettings = ProbabilityUtils.roundPercentProbabilities(showHumintState.getHumintReport().getHumintProbabilities());
			showInitialHumintProbabilities(showHumintState.getHumintReport().getGroups(), 
					previousSettings, "initial probabilities (from HUMINT)");
			conditionPanel.setInstructionBannerText(instructions.toString());
		}
		else if(currentTrialPart instanceof GroupProbeTrialPartState) {			
			GroupProbeTrialPartState groupProbe = (GroupProbeTrialPartState)currentTrialPart;
			StringBuilder instructions = createTrialNumberString(currentTrial+1, trialPhase+1, 
					currentTrialState.getNumTrialParts(), "Group Probe");
			String previousSettingsTitle = null;
			String currentSettingsTitle = null;
			//Add an INT layer if present
			if(groupProbe.getLayerToAdd() != null) {
				showPreviousSettings = true;
				numLayersShown++;
				if(groupProbe.getLayerToAdd().getLayerType() == IntType.SOCINT) {					
					addRegions(currentTrialState.getTrial().getRegionsOverlay(), false, true); 
				}
				else if(groupProbe.getLayerToAdd().getLayerType() == IntType.SIGINT) {
					addINTLayer(groupProbe.getLayerToAdd(), true, false);
				}
				else {
					addINTLayer(groupProbe.getLayerToAdd(), false, true);
				}
				instructions.append("The <b> " + groupProbe.getLayerToAdd().getLayerType() + " layer</b> is now shown. " +
						"Indicate the updated probability that ");
			} else {
				instructions.append("Indicate the probability that ");
			}
			//instructions.append("Indicate the probability that ");
			
			if(numLayersShown > 1) {				
				currentSettingsTitle = "your updated probabilities";
				previousSettingsTitle = "your previous probabilities";				
			}
			else {
				currentSettingsTitle = "your probabilities";
				previousSettingsTitle = "the previous probabilities";
			}

			//Show the group probe
			if(!adjustingNormalizedSettings && previousSettings != null) {
				groupProbe.setCurrentSettings(previousSettings);				
			}
			showGroupProbe(groupProbe.getProbe().getGroups(), groupProbe.getCurrentSettings(),
					currentSettingsTitle,
					(adjustingNormalizedSettings || previousSettings == null) ? 
							groupProbe.getPreviousSettings() : previousSettings,
							previousSettingsTitle, showPreviousSettings);
			instructions.append("each group is responsible for the attack at <b>Location " + groupProbe.getProbe().getAttackLocation().getLocationId() +  
					"</b>, and click Next to continue. ");
			//instructions.append("Probabilities must sum to 100%.");
			conditionPanel.setInstructionBannerText(instructions.toString());
		}	
		else if(currentTrialPart instanceof LayerSelectTrialPartState) {
			//Show the layer selection
			@SuppressWarnings("rawtypes")
			LayerSelectTrialPartState layerSelect = (LayerSelectTrialPartState)currentTrialPart;
			StringBuilder sb = createTrialNumberString(currentTrial+1, trialPhase+1, 
					currentTrialState.getNumTrialParts(), "Layer Select");
			showPreviousSettings = true;
			String previousSettingsTitle = null;
			if(layerSelect.getInitialHumintReport() != null) {			
				previousSettingsTitle = "initial probabilities (from HUMINT)";
				taskInputPanel.setGroupsForPreviousSettingsComponent(layerSelect.getInitialHumintReport().getGroups());
				previousSettings = ProbabilityUtils.roundPercentProbabilities(layerSelect.getInitialHumintReport().getHumintProbabilities());
				sb.append("Probabilities have been initialized from HUMINT reports. ");
			}
			else {				
				previousSettingsTitle = "your previous probabilities";
			}			
			if(!initialLayersSet) {
				layerPanel.setLayers(currentTrialState.getTrial().getIntLayers(), false, reverseLayerOrder);
				initialLayersSet = true;
				sb.append("Select the INT layer you would like to see and click Next to continue.");
			}
			else {
				sb.append("Select the next INT layer you would like to see and click Next to continue.");
			}
			showLayerSelection(previousSettings, previousSettingsTitle, showPreviousSettings);
			conditionPanel.setInstructionBannerText(sb.toString());
		}
		else if(currentTrialPart instanceof ConfirmSettingsTrialPartState) {
			//Confirm the normalized settings for the last group probe or troop allocation probe
			adjustingNormalizedSettings = false;
			ConfirmSettingsTrialPartState confirmSettingsProbe = (ConfirmSettingsTrialPartState)currentTrialPart;
			StringBuilder sb = createTrialNumberString(currentTrial+1, trialPhase+1, 
					currentTrialState.getNumTrialParts(), "Confirm Settings");
			if(confirmSettingsProbe.getProbabiltyProbe() instanceof GroupProbeTrialPartState) {
				ProbabilityProbeTrialPartState groupProbe = confirmSettingsProbe.getProbabiltyProbe();
				taskInputPanel.getGroupProbeComponent().setCurrentSettings(groupProbe.getCurrentNormalizedSettings());
				taskInputPanel.getGroupProbeComponent().showConfirmedProbabilities();
				sb.append("Your probabilities have been normalized to sum to 100%. Click Back to adjust them or click Next to continue.");
			}
			else if(confirmSettingsProbe.getProbabiltyProbe() instanceof TroopAllocationMultiGroupTrialPartState) {
				ProbabilityProbeTrialPartState troopProbe = confirmSettingsProbe.getProbabiltyProbe();
				taskInputPanel.getTroopAllocationMultiGroupComponent().setCurrentSettings(
						troopProbe.getCurrentNormalizedSettings());
				taskInputPanel.getTroopAllocationMultiGroupComponent().showConfirmedProbabilities();
				sb.append("Your troop allocations have been normalized to sum to 100%. Click Back to adjust them or click Next to continue.");
			}
			conditionPanel.setInstructionBannerText(sb.toString());				
			examController.setNavButtonEnabled(ButtonType.Back, true);
		}
		else if(currentTrialPart instanceof TroopAllocationMultiGroupTrialPartState) {
			//Show the troop allocation probe			
			TroopAllocationMultiGroupTrialPartState troopProbe = (TroopAllocationMultiGroupTrialPartState)currentTrialPart;			
			showTroopAllocationProbe(troopProbe.getProbe().getGroups(), troopProbe.getCurrentSettings(), 
					troopProbe.getPreviousSettings(), previousSettings, true);
			
			StringBuilder sb = createTrialNumberString(currentTrial+1, trialPhase+1, 
					currentTrialState.getNumTrialParts(), "Troop Allocation");
			sb.append("Allocate troops to each group, and click Next to continue.");
			conditionPanel.setInstructionBannerText(sb.toString());			
		}
		else if(currentTrialPart instanceof SurpriseProbeTrialPartState) {
			//Show a surprise probe
			SurpriseProbeTrialPartState surpriseProbe = (SurpriseProbeTrialPartState)currentTrialPart;
			boolean showTroopAllocation = true;
			String previousSettingsTitle = null;			
			if(surpriseProbe.getLayerToAdd() != null) {
				//Show a surprise probe after presenting an INT layer (This has been removed)		
				showPreviousSettings = true;
				numLayersShown++;
				if(numLayersShown > 1) {				
					previousSettingsTitle = "your previous probabilities";
				}
				else {
					previousSettingsTitle = "initial probabilities (from HUMINT)";
				}
				if(surpriseProbe.getLayerToAdd().getLayerType() == IntType.SOCINT) {					
					addRegions(currentTrialState.getTrial().getRegionsOverlay(), false, true); 
				}
				else if(surpriseProbe.getLayerToAdd().getLayerType() == IntType.SIGINT) {
					addINTLayer(surpriseProbe.getLayerToAdd(), true, false);
				}
				else {
					addINTLayer(surpriseProbe.getLayerToAdd(), false, true);
				}				
				StringBuilder sb = createTrialNumberString(currentTrial+1, trialPhase+1, 
						currentTrialState.getNumTrialParts(), "Surprise");
				sb.append("The <b>" + surpriseProbe.getLayerToAdd().getLayerType() + " layer</b> is now shown. " + 
						"Indicate your surprise below by selecting a radio button and click Next to continue.");
				conditionPanel.setInstructionBannerText(sb.toString());
				showTroopAllocation = false;
			} else {
				//Show the ground truth surprise probe			
				previousSettingsTitle = "your probabilities";
				GroupType responsibleGroup = revealGroundTruth(surpriseProbe.getGroundTruth());
				StringBuilder sb = createTrialNumberString(currentTrial+1, trialPhase+1, 
						currentTrialState.getNumTrialParts(), "Surprise");
				if(responsibleGroup == null) {
					sb.append("An unknown group was responsible for the attack. " +
							"Indicate your surprise below by selecting a radio button and click Next to continue.");
				}
				else {
					sb.append("<b>Group " + responsibleGroup.getGroupNameFull() + "</b> " +
						" was responsible for the attack. Indicate your surprise on the scale and click Next to continue.");
				}
				conditionPanel.setInstructionBannerText(sb.toString());
			}

			//Show the surprise probe			
			SurpriseReportProbe probe = surpriseProbe.getProbe();
			if(probe != null) {
				taskInputPanel.configureSurpriseEntryComponent(
						probe.getMinSurpriseValue(), probe.getMaxSurpriseValue(), 
						probe.getSurpriseValueIncrement());
				if(!taskInputPanel.getSurpriseEntryComponent().isButtonActionListenerPresent(this)) {
					taskInputPanel.getSurpriseEntryComponent().addButtonActionListener(this);
				}
				showSurpriseProbe(previousTroopAllocations, showTroopAllocation, previousSettings, 
						previousSettingsTitle, showPreviousSettings);
			}			
		}
		else if(currentTrialPart instanceof ShowScoreTrialPartState) {
			//Show the score (S2) for the trial
			ShowScoreTrialPartState showScoreState = (ShowScoreTrialPartState)currentTrialPart;
			TrialState_Phase1 trial = showScoreState.getTrial();
			StringBuilder sb = createTrialNumberString(currentTrial+1, trialPhase+1, 
					currentTrialState.getNumTrialParts(), "Score");			
			long score = 0;
			if(trial.getScore_s2() != null) {
				score = Math.round(trial.getScore_s2());
			}
			if(showScoreState.getGroundTruth() != null) {
				//Also show ground truth
				GroupType responsibleGroup = revealGroundTruth(showScoreState.getGroundTruth());
				if(responsibleGroup == null) {
					sb.append("An unknown group was responsible for the attack. ");
				}
				else {
					sb.append("<b>Group " + responsibleGroup.getGroupNameFull() + "</b> " +
							" was responsible for the attack. ");
				}
			}
			sb.append("You received<b> " + score + " </b>out of 100 points on this trial for " +
				"allocating troops. Click Next to continue.");
			conditionPanel.setInstructionBannerText(sb.toString());				
		}

		if(currentTrialPart instanceof SurpriseProbeTrialPartState || currentTrialPart instanceof LayerSelectTrialPartState) {
			//Disable next button until surprise value is entered or a layer is selected
			examController.setNavButtonEnabled(ButtonType.Next, false);	
		}
		else {
			//Pause before allowing a next button press
			pauseBeforeNextTrial();
		}
	}
	
	protected GroupType revealGroundTruth(GroundTruth groundTruth) {
		GroupType responsibleGroup = null;				
		if(groundTruth != null && groundTruth.getResponsibleGroup() != null) {
			responsibleGroup = groundTruth.getResponsibleGroup();								
			//Show the responsible group at the attack location
			if(attackLocations != null) {
				AttackLocationPlacemark placemark = attackLocations.get(currentTrialState.getTrial().getAttackLocation().getId());
				if(placemark != null) {
					placemark.setName(responsibleGroup.toString());
					Color groupColor = ColorManager_Phase1.getGroupCenterColor(responsibleGroup);
					placemark.setBorderColor(groupColor);
					placemark.setForegroundColor(groupColor);
					if(WidgetConstants.USE_GROUP_SYMBOLS) {
						placemark.setMarkerIcon(ImageManager_Phase1.getGroupSymbolImage(responsibleGroup, IconSize.Small));
						placemark.setShowName(false);
					}
					conditionPanel.getMapPanel().redrawMap();
				}
			}
		}	
		return responsibleGroup;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment_core.event.SubjectActionListener#subjectActionPerformed(org.mitre.icarus.cps.experiment_core.event.SubjectActionEvent)
	 */
	@Override
	public void subjectActionPerformed(SubjectActionEvent event) {
		if(event.eventType == SubjectActionEvent.NEXT_BUTTON_PRESSED) {
			//Advance to the next trial or trial part in the task			
			nextTrial();
		}	
		else if(event.eventType == SubjectActionEvent.BACK_BUTTON_PRESSED) {
			//Go to the previous trial part.  This is only allowed to adjust probabilities.
			if(currentTrialPart instanceof ConfirmSettingsTrialPartState) {
				adjustingNormalizedSettings = true;
				trialPhase -= 2;
				nextTrial();
			}
		}
	}
	
	protected void showInitialHumintProbabilities(List<GroupType> groups, List<Integer> humintSettings, 
			String previousSettingsTitle) {
		taskInputPanel.setGroupsForPreviousSettingsComponent(groups);
		taskInputPanel.getPreviousSettingsComponent().setCurrentSettings(humintSettings);
		taskInputPanel.getPreviousSettingsComponent().setTopTitle(previousSettingsTitle);
		taskInputPanel.getPreviousSettingsComponent().showConfirmedProbabilities();
		taskInputPanel.setSubPanelComponent(0, taskInputPanel.getPreviousSettingsComponent());
		taskInputPanel.setSubPanelVisible(0, true);		
		taskInputPanel.setSubPanelVisible(1, false);
		taskInputPanel.setSubPanelVisible(2, false);
		taskInputPanel.setSubPanelVisible(3, false);
	}
	
	protected void showLayerSelection(List<Integer> previousSettings, String previousSettingsTitle,
			boolean showPreviousSettings) {
		taskInputPanel.setSubPanelComponent(0, layerPanel, BorderLayout.CENTER);
		if(showPreviousSettings) {			
			taskInputPanel.getPreviousSettingsComponent().setCurrentSettings(previousSettings);
			taskInputPanel.getPreviousSettingsComponent().setTopTitle(previousSettingsTitle);
			taskInputPanel.getPreviousSettingsComponent().showConfirmedProbabilities();
			taskInputPanel.setSubPanelComponent(1, taskInputPanel.getPreviousSettingsComponent());			
		}		
		taskInputPanel.setSubPanelVisible(0, true);		
		taskInputPanel.setSubPanelVisible(1, showPreviousSettings);
		taskInputPanel.setSubPanelVisible(2, false);
		taskInputPanel.setSubPanelVisible(3, false);
	}	
	
	protected void showGroupProbe(List<GroupType> groups,
			List<Integer> currentSettings, String currentSettingsTitle,
			List<Integer> previousSettings, String previousSettingsTitle, 
			boolean showPreviousSettings) {		
		taskInputPanel.setGroupsForGroupProbeComponent(groups);
		taskInputPanel.getGroupProbeComponent().setCurrentSettings(currentSettings);
		taskInputPanel.getGroupProbeComponent().setPreviousSettings(previousSettings);
		taskInputPanel.getGroupProbeComponent().setSumVisible(true);
		taskInputPanel.getGroupProbeComponent().setTopTitle(currentSettingsTitle);
		taskInputPanel.getGroupProbeComponent().showEditableProbabilities();
		taskInputPanel.setSubPanelComponent(0, taskInputPanel.getGroupProbeComponent());			
		if(showPreviousSettings) {			
			taskInputPanel.setGroupsForPreviousSettingsComponent(groups);
			taskInputPanel.getPreviousSettingsComponent().setCurrentSettings(previousSettings);
			taskInputPanel.getPreviousSettingsComponent().setTopTitle(previousSettingsTitle);
			taskInputPanel.getPreviousSettingsComponent().showConfirmedProbabilities();
			taskInputPanel.setSubPanelComponent(1, taskInputPanel.getPreviousSettingsComponent());
		}
		if(!adjustingNormalizedSettings) {
			taskInputPanel.getGroupProbeComponent().resetInteractionTimes();
		}	
		taskInputPanel.setSubPanelVisible(0, true);		
		taskInputPanel.setSubPanelVisible(1, showPreviousSettings);
		taskInputPanel.setSubPanelVisible(2, false);
		taskInputPanel.setSubPanelVisible(3, false);
	}
	
	protected void showTroopAllocationProbe(List<GroupType> groups, 
			List<Integer> currentAllocations,  List<Integer> previousAllocations,
			List<Integer> previousSettings, boolean showPreviousSettings) {
		taskInputPanel.setGroupsForTroopAllocationMultiGroupComponent(groups);
		taskInputPanel.setGroupsForPreviousTroopAllocationComponent(groups);
		taskInputPanel.getTroopAllocationMultiGroupComponent().setCurrentSettings(currentAllocations);
		taskInputPanel.getTroopAllocationMultiGroupComponent().setPreviousSettings(previousAllocations);
		taskInputPanel.getTroopAllocationMultiGroupComponent().showEditableProbabilities();
		taskInputPanel.getTroopAllocationMultiGroupComponent().setSumVisible(true);
		taskInputPanel.setSubPanelComponent(0, taskInputPanel.getTroopAllocationMultiGroupComponent());		
		if(showPreviousSettings) {
			taskInputPanel.getPreviousSettingsComponent().setCurrentSettings(previousSettings);
			taskInputPanel.getPreviousSettingsComponent().setTopTitle("your probabilities");
			taskInputPanel.getPreviousSettingsComponent().showConfirmedProbabilities();
			taskInputPanel.setSubPanelComponent(1, taskInputPanel.getPreviousSettingsComponent());
		}
		if(!adjustingNormalizedSettings) {
			taskInputPanel.getTroopAllocationMultiGroupComponent().resetInteractionTimes();
		}	
		taskInputPanel.setSubPanelVisible(0, true);
		taskInputPanel.setSubPanelVisible(1, showPreviousSettings);
		taskInputPanel.setSubPanelVisible(2, false);
		taskInputPanel.setSubPanelVisible(3, false);
	}
	
	/** Show the ground truth surprise probe. */
	protected void showSurpriseProbe(List<Integer> previousTroopAllocations, boolean showTroopAllocation, 
			List<Integer> previousSettings, String previousSettingsTitle, boolean showPreviousSettings) { 
		taskInputPanel.setSubPanelComponent(0, taskInputPanel.getSurpriseEntryComponent());		
		taskInputPanel.getSurpriseEntryComponent().setEnabled(true);		
		if(showTroopAllocation) {
			/*taskInputPanel.getTroopAllocationMultiGroupComponent().showConfirmedProbabilities();
			taskInputPanel.getTroopAllocationMultiGroupComponent().setSumVisible(false);
			taskInputPanel.setSubPanelComponent(1, taskInputPanel.getTroopAllocationMultiGroupComponent());*/
			taskInputPanel.getPreviousTroopAllocationComponent().setCurrentSettings(previousTroopAllocations);
			taskInputPanel.setSubPanelComponent(1, taskInputPanel.getPreviousTroopAllocationComponent());
		}		
		if(showPreviousSettings) {
			taskInputPanel.getPreviousSettingsComponent().setCurrentSettings(previousSettings);
			taskInputPanel.getPreviousSettingsComponent().setTopTitle(previousSettingsTitle);			
			taskInputPanel.getPreviousSettingsComponent().showConfirmedProbabilities();
			taskInputPanel.setSubPanelComponent(2, taskInputPanel.getPreviousSettingsComponent());
		}		
		taskInputPanel.setSubPanelVisible(0, true);
		taskInputPanel.setSubPanelVisible(1, showTroopAllocation);
		taskInputPanel.setSubPanelVisible(2, showPreviousSettings);
		taskInputPanel.setSubPanelVisible(3, false);
	}
}