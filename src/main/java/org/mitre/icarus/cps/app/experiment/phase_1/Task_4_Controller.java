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

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.SwingUtilities;

import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.Task_4_TrialState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.TrialState_Phase1;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.ConfirmSettingsTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.LocationProbeTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.ProbabilityProbeTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.ShowScoreTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.SurpriseProbeTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.TroopAllocationMultiLocationTrialPartState;
import org.mitre.icarus.cps.app.util.ProbabilityUtils;
import org.mitre.icarus.cps.app.widgets.map.phase_1.MapPanelContainer;
import org.mitre.icarus.cps.app.widgets.map.phase_1.PlacemarkFactory.AttackLocationType;
import org.mitre.icarus.cps.app.widgets.map.phase_1.objects.AttackLocationPlacemark;
import org.mitre.icarus.cps.exam.phase_1.testing.GroundTruth;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_4_Phase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_4_Trial;
import org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.Task_4_INTLayerPresentationProbe;
import org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.SurpriseReportProbe;
import org.mitre.icarus.cps.experiment_core.event.ConditionEvent;
import org.mitre.icarus.cps.experiment_core.event.SubjectActionEvent;
import org.mitre.icarus.cps.experiment_core.gui.INavButtonPanel.ButtonType;
import org.mitre.icarus.cps.experiment_core.subject_data.SubjectConditionData;
import org.mitre.icarus.cps.feature_vector.phase_1.IntType;

/**
 * Controller for Task 4.
 * 
 * @author CBONACETO
 *
 */
public class Task_4_Controller extends Task_4_5_6_7_ControllerBase<Task_4_Phase> {
	
	/** The trial states for each trial in the task */
	protected List<Task_4_TrialState> trialStates;
	
	/** The trial state for the current trial */
	protected Task_4_TrialState currentTrialState;
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment.phase_1.TaskController#initializeTaskController(org.mitre.icarus.cps.exam.phase_1.testing.TaskTestPhase, org.mitre.icarus.cps.gui.phase_1.experiment.ConditionPanel_Phase1)
	 */
	@Override
	public void initializeTask(Task_4_Phase task) {		
		this.task = task;
		Boolean reverse = task.isReverseLayerOrder();
		reverseLayerOrder = (reverse != null) ? reverse : false;
		
		//Create the trial states for each trial
		trialStates = new ArrayList<Task_4_TrialState>();		
		locationsPresent.clear();
		groupCenterGroupsPresent.clear();
		intLayersPresent.clear();
		socintGroupsPresent.clear();
		if(task.getTestTrials() != null && !task.getTestTrials().isEmpty()) {
			int trialNum = 0;
			for(Task_4_Trial trial : task.getTestTrials()) {
				trial.setTrialResponse(null);
				trialStates.add(new Task_4_TrialState(trialNum, trial, exam.getNormalizationMode(), showScore));
				locationsPresent.addAll(trial.getPossibleAttackLocations());
				groupCenterGroupsPresent.add(trial.getAttackLocationProbe_initial().getAttackGroup());
				intLayersPresent.addAll(getIntLayerTypesForTrial(trial));
				if(trial.getRegionsOverlay() != null && trial.getRegionsOverlay().getGroups() != null) {
					socintGroupsPresent.addAll(trial.getRegionsOverlay().getGroups());
				}
				trialNum++;
			}			
		}
	}
	
	protected List<IntType> getIntLayerTypesForTrial(Task_4_Trial trial) {
		ArrayList<IntType> intLayers = null;
		if(trial.getIntLayers() != null && !trial.getIntLayers().isEmpty()) {
			intLayers = new ArrayList<IntType>(trial.getIntLayers().size());
			for(Task_4_INTLayerPresentationProbe layer : trial.getIntLayers()) {
				intLayers.add(layer.getLayerType().getLayerType());
			}
		}
		return intLayers;
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment.phase_1.TaskController#getTaskWithResponseData()
	 */
	@Override
	public Task_4_Phase getTaskWithResponseData() {
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
		attackLocations = null;
		mapPanel.resetMap();
		mapPanel.setLayerInstructionsEnabled(true);
		
		//Add the roads layer to the map
		mapPanel.setRoadLayerEnabled(true);
		mapPanel.setRoadsLegendItemVisible(true);
		
		//Add the SIGACTs layer to the map and call it Locations
		mapPanel.setSigactLayerEnabled(true);
		mapPanel.setSigactsLayerAndLegendName("Locations");
		
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
		
		conditionPanel.getMapPanel().setShowScale(true);
		conditionPanel.getMapPanel().redrawMap();
		
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

	/**
	 * Go to the next trial or trial part in the task.
	 */
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
			if(currentTrialPart instanceof LocationProbeTrialPartState) {
				if(taskInputPanel.getLocationProbeComponent() != null) {
					LocationProbeTrialPartState locationProbe = (LocationProbeTrialPartState)currentTrialPart;
					locationProbe.setInteractionTimes(taskInputPanel.getLocationProbeComponent().getInteractionTimes());
					if(!adjustingNormalizedSettings) {
						locationProbe.setPreviousSettings(locationProbe.getCurrentSettings());
					}
					locationProbe.setCurrentSettings(taskInputPanel.getLocationProbeComponent().getCurrentSettings());
					if(locationProbe.getCurrentNormalizedSettings() == null || 
							locationProbe.getCurrentNormalizedSettings().size() != locationProbe.getCurrentSettings().size()) {
						locationProbe.setCurrentNormalizedSettings(
								ProbabilityUtils.createDefaultInitialProbabilities(locationProbe.getCurrentSettings().size()));
					}
					ProbabilityUtils.normalizePercentProbabilities(locationProbe.getCurrentSettings(), locationProbe.getCurrentNormalizedSettings());
					previousSettings = locationProbe.getCurrentNormalizedSettings();					
				}
			}			
			else if(currentTrialPart instanceof TroopAllocationMultiLocationTrialPartState) {
				if(taskInputPanel.getTroopAllocationMultiLocationComponent() != null) {
					TroopAllocationMultiLocationTrialPartState troopProbe = (TroopAllocationMultiLocationTrialPartState)currentTrialPart;
					troopProbe.setInteractionTimes(taskInputPanel.getTroopAllocationMultiLocationComponent().getInteractionTimes());
					troopProbe.setCurrentSettings(taskInputPanel.getTroopAllocationMultiLocationComponent().getCurrentSettings());
					if(troopProbe.getCurrentNormalizedSettings() == null || 
							troopProbe.getCurrentNormalizedSettings().size() != troopProbe.getCurrentSettings().size()) {
						troopProbe.setCurrentNormalizedSettings(
								ProbabilityUtils.createDefaultInitialProbabilities(troopProbe.getCurrentSettings().size()));
					}
					ProbabilityUtils.normalizePercentProbabilities(troopProbe.getCurrentSettings(), troopProbe.getCurrentNormalizedSettings());
					previousTroopAllocations = troopProbe.getCurrentNormalizedSettings();
					int groundTruthIndex = troopProbe.getProbe().getLocations().indexOf(currentTrialState.getTrial().getGroundTruth().getAttackLocationId());
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

				// Fire condition completed event
				fireConditionEvent(new ConditionEvent(ConditionEvent.CONDITION_COMPLETED, 
						currentTrial, this));
				
				return;
			}
			else {				
				//Set the current trial
				currentTrialState = trialStates.get(currentTrial);
				currentTrialPart = null;
				showPreviousSettings = false;
				previousSettings = null;
				//super.setTrial(currentTrial, trialPhase, currentTrialState.getNumTrialParts());	
				
				//Clear the INT layers from the map
				conditionPanel.getMapPanel().setAdditionalINTLayersEnabled(false);
				
				//Add the possible attack locations for the trial to the map
				attackLocations = conditionPanel.getMapPanel().setSigactLocations(
						currentTrialState.getTrial().getPossibleAttackLocations(), AttackLocationType.LOCATION, false, false);
				//conditionPanel.getMapPanel().setSigactLocationsInLegend(currentTrialState.getTrial().getPossibleAttackLocations());
				
				//Add the group center to the map
				groupCenters = conditionPanel.getMapPanel().setGroupCenters(
						Collections.singleton(currentTrialState.getTrial().getGroupCenter()), false);
				//conditionPanel.getMapPanel().setGroupCenterGroupsForLegend(
				//		Collections.singleton(currentTrialState.getTrial().getGroupCenter().getGroup()));
				
				//Add the roads for the trial to the map
				conditionPanel.getMapPanel().setRoads(currentTrialState.getTrial().getRoads());
				
				conditionPanel.getMapPanel().redrawMap();
				
				//Show the HUMINT rules
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						conditionPanel.getMapPanel().showGroupCentersLayerInstructions();		
					}
				});			
			}
		}
		
		//Fire a trial changed event
		super.setTrial(currentTrial, trialPhase, currentTrialState.getNumTrialParts());

		//Show the current trial part			
		currentTrialPart = currentTrialState.getTrialParts().get(trialPhase);
		if(currentTrialPart instanceof LocationProbeTrialPartState) {			
			LocationProbeTrialPartState locationProbe = (LocationProbeTrialPartState)currentTrialPart;
			StringBuilder instructions = createTrialNumberString(currentTrial+1, trialPhase+1, 
					currentTrialState.getNumTrialParts(), "Location Probe");

			//Add an INT layer if present
			if(locationProbe.getLayerToAdd() != null) {				
				showPreviousSettings = true;
				instructions.append("The <b> " + locationProbe.getLayerToAdd().getLayerType() + " layer</b> is now shown. " +
						"Indicate the updated probability that the attack will occur at each location, ");
				if(locationProbe.getLayerToAdd().getLayerType() == IntType.SOCINT) {					
					addRegions(currentTrialState.getTrial().getRegionsOverlay(), false, true); 
				}
				else if(locationProbe.getLayerToAdd().getLayerType() == IntType.SIGINT) {
					addINTLayer(locationProbe.getLayerToAdd(), true, false);
				}
				else {
					addINTLayer(locationProbe.getLayerToAdd(), false, true);
				}
			}
			else {
				if(previousSettings == null) {
					instructions.append("<b>Group " + locationProbe.getProbe().getAttackGroup().getGroupNameFull() + 
							" </b> will attack at one of the locations below. " +
							"Indicate the probability that the attack will occur at each location, ");
				} else {
					instructions.append("Indicate the updated probability that <b>Group " + locationProbe.getProbe().getAttackGroup().getGroupNameFull() + 
							" </b> will attack at each location, ");
					//instructions.append("Indicate the updated probability that the attack will occur at each location, ");
				}
			}

			//Show the location probe
			if(!adjustingNormalizedSettings && previousSettings != null) {
				locationProbe.setCurrentSettings(previousSettings);				
			}
			showLocationProbe(locationProbe.getProbe().getLocations(), locationProbe.getCurrentSettings(), 
					(adjustingNormalizedSettings || previousSettings == null) ? 
							locationProbe.getPreviousSettings() : previousSettings, showPreviousSettings);
			
			instructions.append("and click Next to continue.");
			//instructions.append("<b>Group " + locationProbe.getProbe().getAttackGroup().getGroupNameFull() + 
			//		" </b> is responsible for the attack at each location, and click Next to continue. ");
			//instructions.append("Probabilities must sum to 100%.");
			conditionPanel.setInstructionBannerText(instructions.toString());
		}	
		else if(currentTrialPart instanceof ConfirmSettingsTrialPartState) {
			//Confirm the normalized settings for the last location probe or troop allocation probe
			adjustingNormalizedSettings = false;
			ConfirmSettingsTrialPartState confirmSettingsProbe = (ConfirmSettingsTrialPartState)currentTrialPart;
			StringBuilder sb = createTrialNumberString(currentTrial+1, trialPhase+1, 
					currentTrialState.getNumTrialParts(), "Confirm Settings");
			if(confirmSettingsProbe.getProbabiltyProbe() instanceof LocationProbeTrialPartState) {
				ProbabilityProbeTrialPartState locationProbe = confirmSettingsProbe.getProbabiltyProbe();
				taskInputPanel.getLocationProbeComponent().setCurrentSettings(locationProbe.getCurrentNormalizedSettings());
				taskInputPanel.getLocationProbeComponent().showConfirmedProbabilities();
				sb.append("Your probabilities have been normalized to sum to 100%. Click Back to adjust them or click Next to continue.");
			}
			else if(confirmSettingsProbe.getProbabiltyProbe() instanceof TroopAllocationMultiLocationTrialPartState) {
				ProbabilityProbeTrialPartState troopProbe = confirmSettingsProbe.getProbabiltyProbe();
				taskInputPanel.getTroopAllocationMultiLocationComponent().setCurrentSettings(
						troopProbe.getCurrentNormalizedSettings());
				taskInputPanel.getTroopAllocationMultiLocationComponent().showConfirmedProbabilities();
				sb.append("Your troop allocations have been normalized to sum to 100%. Click Back to adjust them or click Next to continue.");
			}
			conditionPanel.setInstructionBannerText(sb.toString());
			examController.setNavButtonEnabled(ButtonType.Back, true);
		}
		else if(currentTrialPart instanceof TroopAllocationMultiLocationTrialPartState) {
			//Show the troop allocation probe
			TroopAllocationMultiLocationTrialPartState troopProbe = (TroopAllocationMultiLocationTrialPartState)currentTrialPart;			
			showTroopAllocationProbe(troopProbe.getProbe().getLocations(), troopProbe.getCurrentSettings(), 
					troopProbe.getPreviousSettings(), previousSettings, true);
			
			StringBuilder instructions = createTrialNumberString(currentTrial+1, trialPhase+1, 
					currentTrialState.getNumTrialParts(), "Troop Allocation");
			instructions.append("Allocate troops to each location, and click Next to continue.");			
			conditionPanel.setInstructionBannerText(instructions.toString());			
		}
		else if(currentTrialPart instanceof SurpriseProbeTrialPartState) {
			//Show a surprise probe
			SurpriseProbeTrialPartState surpriseProbe = (SurpriseProbeTrialPartState)currentTrialPart;
			boolean showTroopAllocation = true;
			boolean showPreviousSettingsAsFinalSettings = true;
			if(surpriseProbe.getLayerToAdd() != null) {
				//Show a surprise probe after presenting an INT layer
				showPreviousSettings = true;
				showPreviousSettingsAsFinalSettings = false;
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
				String attackLocation = revealGroundTruth(surpriseProbe.getGroundTruth());
				StringBuilder instructions = createTrialNumberString(currentTrial+1, trialPhase+1, 
						currentTrialState.getNumTrialParts(), "Surprise");
				if(attackLocation == null) {
					instructions.append("The attack occured at an unknown location. ");
				} else {
					instructions.append("The attack occured at <b>Location " + attackLocation + "</b>. ");
				}
				instructions.append("Indicate your surprise below by selecting a radio button and click Next to continue.");
				conditionPanel.setInstructionBannerText(instructions.toString());
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
						showPreviousSettings, showPreviousSettingsAsFinalSettings);
			}	
		}
		else if(currentTrialPart instanceof ShowScoreTrialPartState) {
			//Show the score (S2) for the trial
			ShowScoreTrialPartState showScoreState = (ShowScoreTrialPartState)currentTrialPart;
			TrialState_Phase1 trial = ((ShowScoreTrialPartState)currentTrialPart).getTrial();
			StringBuilder instructions = createTrialNumberString(currentTrial+1, trialPhase+1, 
					currentTrialState.getNumTrialParts(), "Score");
			long score = 0;
			if(trial.getScore_s2() != null) {
				score = Math.round(trial.getScore_s2());
			}
			if(showScoreState.getGroundTruth() != null) {
				//Also show ground truth
				String attackLocation = revealGroundTruth(showScoreState.getGroundTruth());
				if(attackLocation == null) {
					instructions.append("The attack occured at an unknown location. ");
				} else {
					instructions.append("The attack occured at <b>Location " + attackLocation + "</b>. ");
				}
			}
			instructions.append("You received<b> " + score + " </b>out of 100 points on this trial for " +
					"allocating troops. Click Next to continue.");
			conditionPanel.setInstructionBannerText(instructions.toString());				
		}

		if(currentTrialPart instanceof SurpriseProbeTrialPartState) {
			//Disable next button until surprise value is entered 
			examController.setNavButtonEnabled(ButtonType.Next, false);	
		}
		else {
			//Pause before allowing a next button press
			pauseBeforeNextTrial();
		}
	}
	
	protected String revealGroundTruth(GroundTruth groundTruth) {
		String attackLocation = null;
		if(groundTruth != null && groundTruth.getAttackLocationId() != null) {
			attackLocation = groundTruth.getAttackLocationId();
			AttackLocationPlacemark placemark = attackLocations.get(attackLocation);
			if(placemark != null) {
				placemark.setHighlighted(true);
				placemark.setBorderColor(Color.black);											
				conditionPanel.getMapPanel().redrawMap();
			}
		}
		return attackLocation;
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
	
	protected void showLocationProbe(List<String> locations,
			List<Integer> currentSettings, List<Integer> previousSettings, 
			boolean showPreviousSettings) {		
		taskInputPanel.setLocationsForLocationProbeComponent(locations);
		taskInputPanel.getLocationProbeComponent().setCurrentSettings(currentSettings);
		taskInputPanel.getLocationProbeComponent().setPreviousSettings(previousSettings);
		taskInputPanel.getLocationProbeComponent().setSumVisible(true);
		taskInputPanel.setSubPanelComponent(0, taskInputPanel.getLocationProbeComponent());
		if(showPreviousSettings) {
			taskInputPanel.getLocationProbeComponent().setTopTitle("your updated probabilities");
			taskInputPanel.setLocationsForPreviousSettingsComponent(locations);
			taskInputPanel.getPreviousSettingsComponent().setCurrentSettings(previousSettings);
			taskInputPanel.getPreviousSettingsComponent().setTopTitle("your previous probabilities");
			taskInputPanel.getPreviousSettingsComponent().showConfirmedProbabilities();
			taskInputPanel.setSubPanelComponent(1, taskInputPanel.getPreviousSettingsComponent());
		}
		else {
			taskInputPanel.getLocationProbeComponent().setTopTitle("your probabilities");
		}
		if(!adjustingNormalizedSettings) {
			//System.out.println("resetting interaction times");
			taskInputPanel.getLocationProbeComponent().resetInteractionTimes();
		}
		taskInputPanel.getLocationProbeComponent().showEditableProbabilities();
		
		taskInputPanel.setSubPanelVisible(0, true);		
		taskInputPanel.setSubPanelVisible(1, showPreviousSettings);
		taskInputPanel.setSubPanelVisible(2, false);
		taskInputPanel.setSubPanelVisible(3, false);
	}
	
	protected void showTroopAllocationProbe(List<String> locations, 
			List<Integer> currentAllocations, List<Integer> previousAllocations,			
			List<Integer> previousSettings, boolean showPreviousSettings) {
		taskInputPanel.setLocationsForTroopAllocationMultiLocationComponent(locations);
		taskInputPanel.setLocationsForPreviousTroopAllocationComponent(locations);
		taskInputPanel.getTroopAllocationMultiLocationComponent().setCurrentSettings(currentAllocations);
		taskInputPanel.getTroopAllocationMultiLocationComponent().setPreviousSettings(previousAllocations);
		taskInputPanel.getTroopAllocationMultiLocationComponent().showEditableProbabilities();
		taskInputPanel.getTroopAllocationMultiLocationComponent().setSumVisible(true);
		taskInputPanel.setSubPanelComponent(0, taskInputPanel.getTroopAllocationMultiLocationComponent());		
		if(showPreviousSettings) {
			taskInputPanel.getPreviousSettingsComponent().setCurrentSettings(previousSettings);
			taskInputPanel.getPreviousSettingsComponent().setTopTitle("your probabilities");
			taskInputPanel.getPreviousSettingsComponent().showConfirmedProbabilities();
			taskInputPanel.setSubPanelComponent(1, taskInputPanel.getPreviousSettingsComponent());
		}
		if(!adjustingNormalizedSettings) {
			//System.out.println("resetting interaction times");
			taskInputPanel.getTroopAllocationMultiLocationComponent().resetInteractionTimes();
		}		
		taskInputPanel.setSubPanelVisible(0, true);
		taskInputPanel.setSubPanelVisible(1, showPreviousSettings);
		taskInputPanel.setSubPanelVisible(2, false);
		taskInputPanel.setSubPanelVisible(3, false);
	}
	
	/** Show the ground truth surprise probe or an after-INT surprise probe. */
	protected void showSurpriseProbe(List<Integer> previousTroopAllocations, boolean showTroopAllocation,
			List<Integer> previousSettings, boolean showPreviousSettings, 
			boolean showPreviousSettinsAsFinalSettings) {
		taskInputPanel.setSubPanelComponent(0, taskInputPanel.getSurpriseEntryComponent());		
		taskInputPanel.getSurpriseEntryComponent().setEnabled(true);
		
		if(showTroopAllocation) {
			taskInputPanel.getPreviousTroopAllocationComponent().setCurrentSettings(previousTroopAllocations);
			taskInputPanel.setSubPanelComponent(1, taskInputPanel.getPreviousTroopAllocationComponent());
		}
		
		if(showPreviousSettings) {
			taskInputPanel.getPreviousSettingsComponent().setCurrentSettings(previousSettings);
			if(showPreviousSettinsAsFinalSettings) {
				taskInputPanel.getPreviousSettingsComponent().setTopTitle("your probabilities");
			}
			else {
				taskInputPanel.getPreviousSettingsComponent().setTopTitle("your previous probabilities");
			}
			taskInputPanel.getPreviousSettingsComponent().showConfirmedProbabilities();
			taskInputPanel.setSubPanelComponent(2, taskInputPanel.getPreviousSettingsComponent());
		}		
		
		taskInputPanel.setSubPanelVisible(0, true);
		taskInputPanel.setSubPanelVisible(1, showTroopAllocation);
		taskInputPanel.setSubPanelVisible(2, showPreviousSettings);
		taskInputPanel.setSubPanelVisible(3, false);
	}
}