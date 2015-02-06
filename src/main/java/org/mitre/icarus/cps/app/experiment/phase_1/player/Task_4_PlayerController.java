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
package org.mitre.icarus.cps.app.experiment.phase_1.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.mitre.icarus.cps.app.experiment.phase_1.player.trial_states.Task_4_PlayerTrialState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.LocationProbeTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.SurpriseProbeTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.TroopAllocationMultiLocationTrialPartState;
import org.mitre.icarus.cps.app.widgets.map.phase_1.MapPanelContainer;
import org.mitre.icarus.cps.app.widgets.map.phase_1.PlacemarkFactory.AttackLocationType;
import org.mitre.icarus.cps.app.window.phase_1.PlayerUtils;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.CPAMetric;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.TrialMetrics;
import org.mitre.icarus.cps.exam.phase_1.playback.TaskResponseData_Phase1;
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
public class Task_4_PlayerController extends Task_4_5_6_PlayerControllerBase<Task_4_Phase> {
	
	/** The trial states for each trial in the task */
	protected List<Task_4_PlayerTrialState> trialStates;
	
	/** The trial state for the current trial */
	protected Task_4_PlayerTrialState currentTrialState;
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment.phase_1.TaskController#initializeTaskController(org.mitre.icarus.cps.exam.phase_1.testing.TaskTestPhase, org.mitre.icarus.cps.gui.phase_1.experiment.ConditionPanel_Phase1)
	 */
	@Override
	public void initializeTask(TaskResponseData_Phase1<Task_4_Phase> taskResponse) {	
		if(conditionRunning) {
			stopCondition();
		}	
		this.taskResponse = taskResponse;
		task = taskResponse.getTestPhase();
		Boolean reverse = task.isReverseLayerOrder();
		reverseLayerOrder = (reverse != null) ? reverse : false;
		
		//Create the trial states for each trial
		trialStates = new ArrayList<Task_4_PlayerTrialState>();		
		locationsPresent.clear();
		groupCenterGroupsPresent.clear();
		intLayersPresent.clear();
		socintGroupsPresent.clear();
		if(task.getTestTrials() != null && !task.getTestTrials().isEmpty()) {
			int trialNum = 0;
			for(Task_4_Trial trial : task.getTestTrials()) {				
				trialStates.add(new Task_4_PlayerTrialState(trialNum, trial, taskResponse.getParticipantTrialData(trialNum),
						taskResponse.getAvgHumanTrialData(trialNum)));
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
	
	@Override
	public void trialResponseChanged(Integer trialNum) {
		//Update the trial state for the trial whose response changed
		if(trialNum != null && trialStates != null && !trialStates.isEmpty()) {
			int trialIndex = 0;
			for(Task_4_PlayerTrialState trialState : trialStates) {
				if(trialState.getTrial().getTrialNum() == trialNum) {					
					trialState.updateTrialData(taskResponse.getParticipantTrialData(trialIndex),
							taskResponse.getAvgHumanTrialData(trialIndex));
					break;
				} else {
					trialIndex++;
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment_core.controller.IConditionController#startCondition(int, org.mitre.icarus.cps.experiment_core.controller.IExperimentController, org.mitre.icarus.cps.experiment_core.subject_data.SubjectConditionData)
	 */
	@Override
	public void startCondition(int firstTrial, boolean startAtEndOfFirstTrial,
			PlayerController experimentController, SubjectConditionData subjectConditionData, String participantLabel) {
		conditionRunning = true;		
		this.examController = experimentController;
		this.participantLabel = participantLabel != null ? participantLabel : "participant";
		taskResponsePanel.setParticipantLabel(this.participantLabel);
		
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
		
		//Clear the task response panel
		conditionPanel.setTaskResponsePanelVisible(true);
		taskResponsePanel.setAllSubPanelsVisible(false);
		conditionPanel.showTaskScreen();
		
		//Load the first trial
		currentTrialState = null;
		currentTrialPart = null;
		currentTrial = firstTrial <= 0 ? -1 : firstTrial;		
		//currentTrial = -1;
		if(startAtEndOfFirstTrial && currentTrial >= 0 && trialStates != null && currentTrial < trialStates.size()) {
			//Start at the end of the current trial (at the last stage)
			trialPhase = trialStates.get(currentTrial).getNumTrialParts() - 2;
		} else {
			trialPhase = -1;
		}
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
		
		//Advance the trial phase counter
		trialPhase++;
		
		//Advance the trial counter if we're at the first trial, the current trial doesn't have any trial parts,
		//or we're at the last trial part of the current trial.
		if(currentTrial < 0 || (currentTrialState != null && 
				(currentTrialState.getTrialParts() == null || trialPhase >= currentTrialState.getTrialParts().size()))) {
			currentTrial++;
			trialPhase = 0;			
			if(trialStates == null || currentTrial >= trialStates.size()) {
				//We're at the end of the task
				conditionRunning = false;				

				// Fire condition completed event and exit
				fireConditionEvent(new ConditionEvent(ConditionEvent.CONDITION_COMPLETED, 
						currentTrial, this));				
				return;
			} else {
				currentTrialState = null;
			}
		}
		
		if(currentTrialState == null) {
			//Initialize the current trial
			currentTrialState = trialStates.get(currentTrial);
			currentTrialPart = null;
			//showPreviousSettings = false;
			//previousSettings = null;					

			//Clear the INT layers from the map
			resetIntLayersForTrial();
			conditionPanel.getMapPanel().setAdditionalINTLayersEnabled(false);

			//Add the possible attack locations for the trial to the map
			attackLocations = conditionPanel.getMapPanel().setSigactLocations(
					currentTrialState.getTrial().getPossibleAttackLocations(), AttackLocationType.LOCATION, false, false);

			//Add the group center to the map
			groupCenters = conditionPanel.getMapPanel().setGroupCenters(
					Collections.singleton(currentTrialState.getTrial().getGroupCenter()), false);				

			//Add the roads for the trial to the map
			conditionPanel.getMapPanel().setRoads(currentTrialState.getTrial().getRoads());
			conditionPanel.getMapPanel().redrawMap();						
		}
		
		//Fire a trial changed event
		super.setTrial(currentTrial, trialPhase, currentTrialState.getNumTrialParts());
		
		//Show the correct INT layers based on the current stage of the trial
		showIntLayers(Math.min(trialPhase - 1, currentTrialState.getNumIntLayers() - 1), 
				currentTrialState.getIntLayerProbes(), currentTrialState.getTrial());

		//Show the current trial part			
		currentTrialPart = currentTrialState.getTrialParts().get(trialPhase);		
		if(currentTrialPart instanceof LocationProbeTrialPartState) {		
			//Show the location probe
			LocationProbeTrialPartState locationProbe = (LocationProbeTrialPartState)currentTrialPart;
			StringBuilder instructions = createTrialNumberString(currentTrial+1, trialPhase+1, 
					currentTrialState.getNumTrialParts(), "Location Probe", currentTrialState.isTrialComplete());			
			boolean showPreviousSettings = false;
			if(locationProbe.getLayerToAdd() != null) {
				instructions.append("The " + participantLabel + " has been shown " + 
						formatLayerName(locationProbe.getLayerToAdd(), currentTrialState.getTrial()) + "," +
						"and has updated the probability that the attack will occur at each location.");
				instructions.append(" The " + participantLabel + "'s previous probabilities");
				showPreviousSettings = true;
			} else {			
				if(trialPhase == 0) {
					instructions.append("A new trial has started. ");
				}
				instructions.append("The " + participantLabel + " has indicated the probability that <b>Group Dimaond " +
						locationProbe.getProbe().getAttackGroup().getGroupNameFull() + "</b> will attack at each location.");
			}
			if(locationProbe.isNormativeSettingsPresent()) {
				if(locationProbe.isAvgHumanSettingsPresent()) {
					if(showPreviousSettings) {
						instructions.append(", the normative probabilities, and the average human probabilities are also shown.");
					} else {
						instructions.append(" The normative probabilities and the average human probabilities are also shown.");
					}
				} else {
					if(showPreviousSettings) {
						instructions.append(" and the normative probabilities are also shown.");
					} else {
						instructions.append(" The normative probabilities are also shown.");
					}
				}
			} else if(locationProbe.isAvgHumanSettingsPresent()) {
				if(showPreviousSettings) {
					instructions.append(" and the average human probabilities are also shown.");
				} else {
					instructions.append(" The average human probabilities are also shown.");
				}
			}
			if(showScore && currentTrialState.getParticipantTrialData() != null && 
					currentTrialState.getParticipantTrialData().getMetrics() != null) {
				TrialMetrics metrics = currentTrialState.getParticipantTrialData().getMetrics();
				if(taskResponse.getMetricsInfo().getRSR_info().isAssessedForTask(task.getConditionNum()) 
						&& metrics.getRSR() != null && trialPhase < metrics.getRSR().size()) {
					CPAMetric rsr = metrics.getRSR().get(trialPhase);
					if(rsr != null && rsr.score != null) {
						instructions.append(" The <b>RSR</b> score for this stage is <b>" + PlayerUtils.formatDoubleAsString(rsr.score) + "%</b>");
						if(!rsr.assessed) {
							instructions.append(", but RSR was not assessed for this stage.");
						} else {
							instructions.append(".");
						}
					}
				}
				if(taskResponse.getMetricsInfo().getRR_info().isAssessedForTask(task.getConditionNum()) 
						&& metrics.getRR() != null) {
					create_RR_AssessmentString(instructions, metrics, 
							currentTrialState.getAvgHumanTrialData() != null ? currentTrialState.getAvgHumanTrialData().getMetrics() : null);
				}
			}
			conditionPanel.setInstructionBannerText(instructions.toString());
			
			if(groundTruthRevealed) {
				//Make sure ground truth isn't revealed
				hideGroundTruth();
			}			
			showLocationProbe(locationProbe, showPreviousSettings);			
		} else if(currentTrialPart instanceof TroopAllocationMultiLocationTrialPartState) {
			//Show the troop allocation probe
			TroopAllocationMultiLocationTrialPartState troopProbe = (TroopAllocationMultiLocationTrialPartState)currentTrialPart;
			StringBuilder instructions = createTrialNumberString(currentTrial+1, trialPhase+1, 
					currentTrialState.getNumTrialParts(), "Troop Allocation", currentTrialState.isTrialComplete());
			instructions.append("The " + participantLabel + " has allocated troops to each location. The " + participantLabel + "'s probabilities");
			if(troopProbe.isNormativeSettingsPresent()) {
				if(troopProbe.isAvgHumanSettingsPresent()) {
					instructions.append(", the normative troop allocations, " +
							"and the average human troop allocations are also shown.");
				} else {
					instructions.append(" and the normative troop allocations are also shown.");
				}
			} else if(troopProbe.isAvgHumanSettingsPresent()) {
				instructions.append(" and the average human troop allocations are also shown.");
			} else {
				instructions.append(" are also shown.");
			}
			if(showScore && currentTrialState.getParticipantTrialData() != null && 
					currentTrialState.getParticipantTrialData().getMetrics() != null) {
				TrialMetrics metrics = currentTrialState.getParticipantTrialData().getMetrics();
				if(taskResponse.getMetricsInfo().getPM_RMS_info().isAssessedForTask(task.getConditionNum()) 
						&& metrics.getPM() != null) {
					create_PM_RMS_AssessmentString(instructions, metrics, 
							currentTrialState.getAvgHumanTrialData() != null ? currentTrialState.getAvgHumanTrialData().getMetrics() : null);
				}
			}
			conditionPanel.setInstructionBannerText(instructions.toString());
			
			if(groundTruthRevealed) {
				//Make sure ground truth isn't revealed
				hideGroundTruth();
			}
			showTroopAllocationProbe(troopProbe.getProbe().getLocations(), currentTrialState.getFinalSettings(), 
					true, troopProbe);			
		}
		else if(currentTrialPart instanceof SurpriseProbeTrialPartState) {
			//Show the group responsible for the attack (ground truth)
			SurpriseProbeTrialPartState surpriseProbe = (SurpriseProbeTrialPartState)currentTrialPart;
			String attackLocation = revealGroundTruth(surpriseProbe.getGroundTruth());
			StringBuilder instructions = createTrialNumberString(currentTrial+1, trialPhase+1, 
					currentTrialState.getNumTrialParts(), "Surprise", currentTrialState.isTrialComplete());
			if(attackLocation != null) {
				instructions.append("The " + participantLabel + " has been shown that the attack occurred at <b>Location " + attackLocation + "</b> " +
						"and has indicated surprise.");
			} else {
				instructions.append("The " + participantLabel + " has been shown the location where the attack occurred and has indicated surprise.");
			}
			if(surpriseProbe.getCurrentSurprise() == null) {
				instructions.append("<font color=\"red\"><i> The " + participantLabel + " did not indicate surprise for this trial. </i></font>");
			}
			if(surpriseProbe.getAvgHumanSurpsie() != null) {
				instructions.append(" The average human surprise, the " + participantLabel + "'s probabilities,");
			} else {
				instructions.append(" The " + participantLabel + "'s probabilities");
			}
			instructions.append(" and the " + participantLabel + "'s troop allocations are also shown.");
			createTrialAssessmentResultsString(currentTrialState, instructions);
			conditionPanel.setInstructionBannerText(instructions.toString());

			//Show the surprise probe			
			SurpriseReportProbe probe = surpriseProbe.getProbe();
			if(probe != null) {				
				showSurpriseProbe(true, true, currentTrialState.getTroopProbe().getProbe().getLocations(),
						currentTrialState.getFinalSettings(), 
						currentTrialState.getTroopProbe().getCurrentSettings(),
						surpriseProbe);
			}	
		}

		//Pause before allowing a next button press
		pauseBeforeNextTrial();
	}
	
	/** Highlight the ground truth location placemark. */
	protected String revealGroundTruth(GroundTruth groundTruth) {		
		String attackLocation = null;
		if(groundTruth != null && groundTruth.getAttackLocationId() != null) {
			attackLocation = groundTruth.getAttackLocationId();
			groundTruthPlacemark = attackLocations.get(attackLocation);
			if(groundTruthPlacemark != null) {
				groundTruthRevealed = true;
				groundTruthPlacemark.setHighlighted(true);
				//groundTruthPlacemark.setBorderColor(Color.black);											
				conditionPanel.getMapPanel().redrawMap();				
			}
		}
		return attackLocation;
	}
	
	/** Un-highlight the ground truth location placemark. */
	public void hideGroundTruth() {
		if(groundTruthRevealed) {
			groundTruthRevealed = false;
			if(groundTruthPlacemark != null) {
				groundTruthPlacemark.setHighlighted(false);
				//groundTruthPlacemark.setBorderColor(Color.black);											
				conditionPanel.getMapPanel().redrawMap();
			}
		}
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
			//Advance to the previous trial or trial part in the task
			trialPhase -= 2;
			if(trialPhase < -1) {
				//We're at the previous trial
				currentTrial -= 1;
				currentTrialState = null;
				if(currentTrial >= 0 && trialStates != null && currentTrial < trialStates.size()) {
					trialPhase = trialStates.get(currentTrial).getNumTrialParts() - 2;					
				}
			} 			
			if(currentTrial < 0) {
				//We're past the beginning of the task (went back to the previous task), fire a condition completed event			
				conditionRunning = false;
				fireConditionEvent(new ConditionEvent(ConditionEvent.CONDITION_COMPLETED, -1, this));
			} else {
				nextTrial();
			}
		}
	}
	
	protected void showLocationProbe(LocationProbeTrialPartState locationProbe, boolean showPreviousSettings) {	
		List<String> locations = locationProbe.getProbe().getLocations();
		//Show participant current probabilities
		taskResponsePanel.setLocationsForLocationProbeComponent(locations);
		taskResponsePanel.getLocationProbeComponent().setCurrentSettings(locationProbe.getCurrentSettings());
		if(locationProbe.getPreviousSettings() != null) {
			taskResponsePanel.getLocationProbeComponent().setPreviousSettings(locationProbe.getPreviousSettings());
		}
		taskResponsePanel.getLocationProbeComponent().setTopTitle(participantLabel + "'s probabilities");		
		taskResponsePanel.getLocationProbeComponent().showConfirmedProbabilities();
		taskResponsePanel.getLocationProbeComponent().setSumVisible(false);
		taskResponsePanel.setSubPanelComponent(0, taskResponsePanel.getLocationProbeComponent());
		taskResponsePanel.setSubPanelVisible(0, true);
		
		//Show participant previous probabilities
		if(showPreviousSettings && locationProbe.getPreviousSettings() != null) {
			taskResponsePanel.setLocationsForPreviousSettingsComponent(locations);
			taskResponsePanel.getPreviousSettingsComponent().setTopTitle("previous probabilities");
			taskResponsePanel.getPreviousSettingsComponent().setCurrentSettings(locationProbe.getPreviousSettings());
			taskResponsePanel.getPreviousSettingsComponent().showConfirmedProbabilities();
			taskResponsePanel.setSubPanelComponent(1, taskResponsePanel.getPreviousSettingsComponent());
			taskResponsePanel.setSubPanelVisible(1, true);
		} else {
			taskResponsePanel.setSubPanelVisible(1, false);
		}
		
		//Show normative probabilities
		if(locationProbe.isNormativeSettingsPresent()) {
			taskResponsePanel.setLocationsForNormativeProbsComponent(locations);
			taskResponsePanel.getNormativeProbsComponent().setCurrentSettings(locationProbe.getNormativeSettings());
			taskResponsePanel.setSubPanelComponent(2, taskResponsePanel.getNormativeProbsComponent());
			taskResponsePanel.setSubPanelVisible(2, true);
		} else {
			taskResponsePanel.setSubPanelVisible(2, false);
		}
		
		//Show average human probabilities
		if(locationProbe.isAvgHumanSettingsPresent()) {
			taskResponsePanel.setLocationsForAvgHumanProbsComponent(locations);
			taskResponsePanel.getAvgHumanProbsComponent().setCurrentSettings(locationProbe.getAvgHumanSettings());
			taskResponsePanel.setSubPanelComponent(3, taskResponsePanel.getAvgHumanProbsComponent());
			taskResponsePanel.setSubPanelVisible(3, true);
		} else {
			taskResponsePanel.setSubPanelVisible(3, false);	
		}		
	}
	
	protected void showTroopAllocationProbe(List<String> locations, List<Integer> previousSettings, boolean showPreviousSettings,
			TroopAllocationMultiLocationTrialPartState troopProbe) {
		//Show participant's troop allocation
		taskResponsePanel.setLocationsForTroopAllocationMultiLocationComponent(locations);
		taskResponsePanel.getTroopAllocationMultiLocationComponent().setTopTitle(participantLabel + "'s troop allocations");
		taskResponsePanel.getTroopAllocationMultiLocationComponent().showConfirmedProbabilities();
		taskResponsePanel.getTroopAllocationMultiLocationComponent().setSumVisible(false);
		taskResponsePanel.getTroopAllocationMultiLocationComponent().setCurrentSettings(troopProbe.getCurrentSettings());
		taskResponsePanel.setSubPanelComponent(0, taskResponsePanel.getTroopAllocationMultiLocationComponent());
		taskResponsePanel.setSubPanelVisible(0, true);
		
		if(showPreviousSettings) {
			//Show participant's probabilities
			taskResponsePanel.setLocationsForPreviousSettingsComponent(locations);
			taskResponsePanel.getPreviousSettingsComponent().setTopTitle(participantLabel + "'s probabilities");
			taskResponsePanel.getPreviousSettingsComponent().setCurrentSettings(previousSettings);
			taskResponsePanel.getPreviousSettingsComponent().showConfirmedProbabilities();
			taskResponsePanel.setSubPanelComponent(1, taskResponsePanel.getPreviousSettingsComponent());
			taskResponsePanel.setSubPanelVisible(1, true);
		} else {
			taskResponsePanel.setSubPanelVisible(1, false);
		}
		
		//Show normative troop allocation
		if(troopProbe.isNormativeSettingsPresent()) {
			taskResponsePanel.setLocationsForNormativeTroopAllocationComponent(locations);
			taskResponsePanel.getNormativeTroopAllocationComponent().setCurrentSettings(troopProbe.getNormativeSettings());
			taskResponsePanel.setSubPanelComponent(2, taskResponsePanel.getNormativeTroopAllocationComponent());
			taskResponsePanel.setSubPanelVisible(2, true);
		} else {
			taskResponsePanel.setSubPanelVisible(2, false);
		}
		
		//Show average human troop allocation
		if(troopProbe.isAvgHumanSettingsPresent()) {
			taskResponsePanel.setLocationsForAvgHumanTroopAllocationComponent(locations);
			taskResponsePanel.getAvgHumanTroopAllocationComponent().setCurrentSettings(troopProbe.getAvgHumanSettings());
			taskResponsePanel.setSubPanelComponent(3, taskResponsePanel.getAvgHumanTroopAllocationComponent());
			taskResponsePanel.setSubPanelVisible(3, true);
		} else {
			taskResponsePanel.setSubPanelVisible(3, false);
		}
	}	
	
	protected void showSurpriseProbe(boolean showTroopAllocation, boolean showPreviousSettings,
			List<String> locations, List<Integer> previousSettings, List<Integer> troopAllocation, 
			SurpriseProbeTrialPartState surpriseProbe) {		
		//Show participant's surprise
		SurpriseReportProbe probe = surpriseProbe.getProbe();
		taskResponsePanel.configureSurpriseEntryComponent(probe.getMinSurpriseValue(), 
				probe.getMaxSurpriseValue(), probe.getSurpriseValueIncrement());
		taskResponsePanel.getSurpriseEntryComponent().setTitleVisible(true);
		taskResponsePanel.getSurpriseEntryComponent().setTitleText(participantLabel + "'s surprise");
		if(surpriseProbe.getCurrentSurprise() != null) {
			taskResponsePanel.getSurpriseEntryComponent().setSelectedIndex(surpriseProbe.getCurrentSurprise());
		} 
		taskResponsePanel.getSurpriseEntryComponent().setEnabled(false);
		taskResponsePanel.setSubPanelComponent(0, taskResponsePanel.getSurpriseEntryComponent());
		taskResponsePanel.setSubPanelVisible(0, true);
		
		//Show average human surprise
		if(surpriseProbe.getAvgHumanSurpsie() != null) {			
			taskResponsePanel.configureAvgHumanSurpriseEntryComponent(probe.getMinSurpriseValue(), 
					probe.getMaxSurpriseValue(), probe.getSurpriseValueIncrement());
			taskResponsePanel.getAvgHumanSurpriseEntryComponent().setSelectedIndex(surpriseProbe.getAvgHumanSurpsie());
			taskResponsePanel.getAvgHumanSurpriseEntryComponent().setTitleVisible(true);
			taskResponsePanel.getAvgHumanSurpriseEntryComponent().setTitleText("human surprise");
			taskResponsePanel.getAvgHumanSurpriseEntryComponent().setEnabled(false);
			taskResponsePanel.setSubPanelComponent(1, taskResponsePanel.getAvgHumanSurpriseEntryComponent());
			taskResponsePanel.setSubPanelVisible(1, true);
		} else {
			taskResponsePanel.setSubPanelVisible(1, false);
		}		
		
		if(showPreviousSettings) {
			//Show participant's probabilities
			taskResponsePanel.setLocationsForPreviousSettingsComponent(locations);
			taskResponsePanel.getPreviousSettingsComponent().setCurrentSettings(previousSettings);			
			taskResponsePanel.getPreviousSettingsComponent().setTopTitle(participantLabel + "'s probabilities");			
			taskResponsePanel.getPreviousSettingsComponent().showConfirmedProbabilities();
			taskResponsePanel.setSubPanelComponent(2, taskResponsePanel.getPreviousSettingsComponent());
			taskResponsePanel.setSubPanelVisible(2, true);
		} else {
			taskResponsePanel.setSubPanelVisible(2, false);
		}
		
		if(showTroopAllocation) {
			//Show participant's troop allocation
			taskResponsePanel.setLocationsForPreviousTroopAllocationComponent(locations);
			taskResponsePanel.getPreviousTroopAllocationComponent().setCurrentSettings(troopAllocation);
			taskResponsePanel.getPreviousTroopAllocationComponent().setTopTitle(participantLabel + "'s troop allocations");			
			taskResponsePanel.setSubPanelComponent(3, taskResponsePanel.getPreviousTroopAllocationComponent());			
			taskResponsePanel.setSubPanelVisible(3, true);
		} else {
			taskResponsePanel.setSubPanelVisible(3, false);
		}
	}
}