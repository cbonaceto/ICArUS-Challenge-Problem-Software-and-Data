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

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.mitre.icarus.cps.app.experiment.phase_1.player.trial_states.Task_5_6_PlayerTrialState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.GroupProbeTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.SurpriseProbeTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.TroopAllocationMultiGroupTrialPartState;
import org.mitre.icarus.cps.app.widgets.WidgetConstants;
import org.mitre.icarus.cps.app.widgets.ImageManager.IconSize;
import org.mitre.icarus.cps.app.widgets.map.phase_1.MapPanelContainer;
import org.mitre.icarus.cps.app.widgets.map.phase_1.PlacemarkFactory.AttackLocationType;
import org.mitre.icarus.cps.app.widgets.phase_1.ColorManager_Phase1;
import org.mitre.icarus.cps.app.widgets.phase_1.ImageManager_Phase1;
import org.mitre.icarus.cps.app.window.phase_1.PlayerUtils;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.CPAMetric;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.TrialMetrics;
import org.mitre.icarus.cps.exam.phase_1.playback.TaskResponseData_Phase1;
import org.mitre.icarus.cps.exam.phase_1.testing.GroundTruth;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_4_5_6_PhaseBase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_4_5_6_TrialBase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_5_Trial;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_6_Phase;
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
public class Task_5_6_PlayerController extends Task_4_5_6_PlayerControllerBase<Task_4_5_6_PhaseBase<?>> {
	
	/** The trial states for each trial in the task */
	protected List<Task_5_6_PlayerTrialState> trialStates;
	
	/** The trial state for the current trial */
	protected Task_5_6_PlayerTrialState currentTrialState;	
	
	/** Whether to show the average human probabilities (not shown for Task 6) */
	protected boolean showAvgHumanSettings;

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment.phase_1.TaskController#initializeTaskController(org.mitre.icarus.cps.exam.phase_1.testing.TaskTestPhase, org.mitre.icarus.cps.gui.phase_1.experiment.ConditionPanel_Phase1)
	 */
	@Override
	public void initializeTask(TaskResponseData_Phase1<Task_4_5_6_PhaseBase<?>> taskResponse) {	
		if(conditionRunning) {
			stopCondition();
		}	
		this.taskResponse = taskResponse;
		task = taskResponse.getTestPhase();
		Boolean reverse = task.isReverseLayerOrder();
		reverseLayerOrder = (reverse != null) ? reverse : false;
		showAvgHumanSettings = !(task instanceof Task_6_Phase);
		
		//Create the trial states for each trial
		trialStates = new ArrayList<Task_5_6_PlayerTrialState>();
		locationsPresent.clear();
		groupCenterGroupsPresent.clear();
		intLayersPresent.clear();
		socintGroupsPresent.clear();
		if(task.getTestTrials() != null && !task.getTestTrials().isEmpty()) {
			int trialNum = 0;
			for(Task_4_5_6_TrialBase trial : task.getTestTrials()) {
				if(trial instanceof Task_6_Trial) {
					Task_6_Trial task6Trial = (Task_6_Trial)trial;					
					trialStates.add(new Task_5_6_PlayerTrialState(trialNum, task6Trial,
							taskResponse.getParticipantTrialData(trialNum),
							taskResponse.getAvgHumanTrialData(trialNum)));
					locationsPresent.add(task6Trial.getAttackLocation());
					groupCenterGroupsPresent.addAll(getGroupCenterGroups(task6Trial.getGroupCenters()));
				}
				else if(trial instanceof Task_5_Trial) {
					Task_5_Trial task5Trial = (Task_5_Trial)trial;				
					trialStates.add(new Task_5_6_PlayerTrialState(trialNum, task5Trial,
							taskResponse.getParticipantTrialData(trialNum),
							taskResponse.getAvgHumanTrialData(trialNum)));
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
	
	@Override
	public void trialResponseChanged(Integer trialNum) {
		//Update the trial state for the trial whose response changed
		if(trialNum != null && trialStates != null && !trialStates.isEmpty()) {
			int trialIndex = 0;
			for(Task_5_6_PlayerTrialState trialState : trialStates) {
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

				//Fire condition completed event and exit
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
			
			//Clear the INT layers from the map
			resetIntLayersForTrial();
			conditionPanel.getMapPanel().setAdditionalINTLayersEnabled(false);
			
			//Add the group centers to the map
			groupCenters = conditionPanel.getMapPanel().setGroupCenters(currentTrialState.getTrial().getGroupCenters(), false);
			if(currentTrialState.getTrial().getGroupCenters() != null && !currentTrialState.getTrial().getGroupCenters().isEmpty()) {
				ArrayList<GroupType> groups = new ArrayList<GroupType>(currentTrialState.getTrial().getGroupCenters().size());
				for(GroupCenter groupCenter : currentTrialState.getTrial().getGroupCenters()) {
					groups.add(groupCenter.getGroup());
				}
			}				
			
			//Add the attack location for the trial to the map
			attackLocations = conditionPanel.getMapPanel().setSigactLocations(
					Collections.singleton(currentTrialState.getTrial().getAttackLocation()), AttackLocationType.LOCATION, false, false);								
			
			//Add the roads for the trial to the map
			conditionPanel.getMapPanel().setRoads(currentTrialState.getTrial().getRoads());			
			conditionPanel.getMapPanel().redrawMap();
		}
		
		//Fire a trial changed event
		super.setTrial(currentTrial, trialPhase, currentTrialState.getNumTrialParts());
		
		//Show the correct INT layers based on the current stage of the trial
		showIntLayers(Math.min(trialPhase, currentTrialState.getNumIntLayers() - 1), 
				currentTrialState.getIntLayerProbes(), currentTrialState.getTrial());

		//Show the trial part			
		currentTrialPart = currentTrialState.getTrialParts().get(trialPhase);

		if(currentTrialPart instanceof GroupProbeTrialPartState) {
			//Show the group probe
			GroupProbeTrialPartState groupProbe = (GroupProbeTrialPartState)currentTrialPart;
			StringBuilder instructions = createTrialNumberString(currentTrial+1, trialPhase+1, 
					currentTrialState.getNumTrialParts(), "Group Probe", currentTrialState.isTrialComplete());					
			if(trialPhase == 0) {
				instructions.append("A new trial has started. ");
			}			
			if(task instanceof Task_6_Phase) {
				instructions.append("The " + participantLabel + " has selected ");
			} else {
				instructions.append("The " + participantLabel + " has been shown ");
			}
			instructions.append(formatLayerName(groupProbe.getLayerToAdd(), currentTrialState.getTrial()) + "," +
					"and has updated the probability that the attack will occur at each location.");
			if(trialPhase == 0) {
				instructions.append(" The previous probabilities (provided from HUMINT)");
			} else {
				instructions.append(" The " + participantLabel + "'s previous probabilities");
			}
			if(groupProbe.isNormativeSettingsPresent()) {
				if(showAvgHumanSettings && groupProbe.isAvgHumanSettingsPresent()) {
					instructions.append(", the normative probabilities, and the average human probabilities are also shown.");
				} else {
					instructions.append(" and the normative probabilities are also shown.");
				}
			} else if(showAvgHumanSettings && groupProbe.isAvgHumanSettingsPresent()) {
				instructions.append(" and the average human probabilities are also shown.");
			}				
			if(showScore && currentTrialState.getParticipantTrialData() != null && 
					currentTrialState.getParticipantTrialData().getMetrics() != null) {
				TrialMetrics metrics = currentTrialState.getParticipantTrialData().getMetrics();
				if(taskResponse.getMetricsInfo().getRSR_info().isAssessedForTask(task.getConditionNum()) 
						&& metrics.getRSR() != null && trialPhase+1 < metrics.getRSR().size()) {
					CPAMetric rsr = metrics.getRSR().get(trialPhase+1);
					if(rsr != null && rsr.score != null) {
						instructions.append(" The <b>RSR</b> score for this stage is <b>" + PlayerUtils.formatDoubleAsString(rsr.score) + "%</b>");
						if(!rsr.assessed) {
							instructions.append(", but RSR was not assessed for this stage.");
						} else {
							instructions.append(".");
						}
					}
				}				
				if(trialPhase > 0 && taskResponse.getMetricsInfo().getAI_info().isAssessedForTask(task.getConditionNum()) 
						&& metrics.getAI() != null) {
					create_AI_AssessmentString(instructions, metrics, 
							currentTrialState.getAvgHumanTrialData() != null ? currentTrialState.getAvgHumanTrialData().getMetrics() : null,
									trialPhase);
				}
				if(taskResponse.getMetricsInfo().getCS_info().isAssessedForTask(task.getConditionNum()) 
						&& groupProbe.getLayerToAdd() != null && groupProbe.getLayerToAdd().getLayerType() == IntType.SIGINT
						&& metrics.getC() != null) {
					if(metrics.getC() > 0) {
						instructions.append(" The " + participantLabel + " <b>exhibited</b> a <b>Confirmation Bias</b> " +
								"by selecting SIGINT on the group with the highest probability.");
					} else {
						instructions.append(" The " + participantLabel + " <b>did not exhibit</b> a <b>Confirmation Bias</b> " +
								"by not selecting SIGINT on the group with the highest probability.");
					}
				}
			}
			conditionPanel.setInstructionBannerText(instructions.toString());
			
			if(groundTruthRevealed) {
				//Make sure ground truth isn't revealed
				hideGroundTruth();
			}									
			showGroupProbe(groupProbe, true, 
					trialPhase == 0 ? "HUMINT probabilities" : "previous probabilities");			
		} else if(currentTrialPart instanceof TroopAllocationMultiGroupTrialPartState) {
			//Show the troop allocation probe			
			TroopAllocationMultiGroupTrialPartState troopProbe = (TroopAllocationMultiGroupTrialPartState)currentTrialPart;			
			StringBuilder instructions = createTrialNumberString(currentTrial+1, trialPhase+1, 
					currentTrialState.getNumTrialParts(), "Troop Allocation", currentTrialState.isTrialComplete());
			instructions.append("The " + participantLabel + " has allocated troops to each group. The " + participantLabel + "'s probabilities");
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
			showTroopAllocationProbe(troopProbe.getProbe().getGroups(), currentTrialState.getFinalSettings(), 
					true, troopProbe);							
		} else if(currentTrialPart instanceof SurpriseProbeTrialPartState) {
			//Show the group responsible for the attack (ground truth)
			SurpriseProbeTrialPartState surpriseProbe = (SurpriseProbeTrialPartState)currentTrialPart;
			GroupType group = revealGroundTruth(surpriseProbe.getGroundTruth());
			StringBuilder instructions = createTrialNumberString(currentTrial+1, trialPhase+1, 
					currentTrialState.getNumTrialParts(), "Surprise", currentTrialState.isTrialComplete());
			if(group != null) {
				instructions.append("The " + participantLabel + " has been shown that <b>Group " + group.getGroupNameFull() + "</b> was responsible for the attack " +
						"and has indicated surprise.");
			} else {
				instructions.append("The " + participantLabel + " has been shown the group responsible for the attack and has indicated surprise.");
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
				showSurpriseProbe(true, true, currentTrialState.getTroopProbe().getProbe().getGroups(),
						currentTrialState.getFinalSettings(), 
						currentTrialState.getTroopProbe().getCurrentSettings(),
						surpriseProbe);
			}			
		} 

		//Pause before allowing a next button press
		pauseBeforeNextTrial();
	}
	
	/** Highlight the ground truth location placemark. */
	protected GroupType revealGroundTruth(GroundTruth groundTruth) {
		GroupType responsibleGroup = null;				
		if(groundTruth != null && groundTruth.getResponsibleGroup() != null) {
			responsibleGroup = groundTruth.getResponsibleGroup();								
			//Show the responsible group at the attack location
			if(attackLocations != null) {
				groundTruthPlacemark = attackLocations.get(currentTrialState.getTrial().getAttackLocation().getId());
				if(groundTruthPlacemark != null) {
					groundTruthRevealed = true;
					groundTruthPlacemark.setName(responsibleGroup.toString());
					Color groupColor = ColorManager_Phase1.getGroupCenterColor(responsibleGroup);
					groundTruthPlacemark.setBorderColor(groupColor);
					groundTruthPlacemark.setForegroundColor(groupColor);
					if(WidgetConstants.USE_GROUP_SYMBOLS) {
						groundTruthPlacemark.setMarkerIcon(ImageManager_Phase1.getGroupSymbolImage(responsibleGroup, IconSize.Small));
						groundTruthPlacemark.setShowName(false);
					}
					conditionPanel.getMapPanel().redrawMap();
				}
			}
		}	
		return responsibleGroup;
	}
	
	/** Un-highlight the ground truth location placemark. */
	public void hideGroundTruth() {
		if(groundTruthRevealed) {
			groundTruthRevealed = false;
			if(groundTruthPlacemark != null) {
				groundTruthPlacemark.setShowName(true);
				groundTruthPlacemark.setName(currentTrialState.getTrial().getAttackLocation().getId());
				groundTruthPlacemark.setBorderColor(Color.black);
				groundTruthPlacemark.setForegroundColor(Color.black);
				groundTruthPlacemark.setMarkerIcon(null);				
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
	
	protected void showInitialHumintProbabilities(List<GroupType> groups, List<Integer> humintSettings, 
			String previousSettingsTitle) {
		taskResponsePanel.setGroupsForPreviousSettingsComponent(groups);
		taskResponsePanel.getPreviousSettingsComponent().setCurrentSettings(humintSettings);
		taskResponsePanel.getPreviousSettingsComponent().setTopTitle(previousSettingsTitle);
		taskResponsePanel.getPreviousSettingsComponent().showConfirmedProbabilities();
		taskResponsePanel.setSubPanelComponent(0, taskResponsePanel.getPreviousSettingsComponent());
		taskResponsePanel.setSubPanelVisible(0, true);		
		taskResponsePanel.setSubPanelVisible(1, false);
		taskResponsePanel.setSubPanelVisible(2, false);
		taskResponsePanel.setSubPanelVisible(3, false);
	}
	
	protected void showGroupProbe(GroupProbeTrialPartState groupProbe, boolean showPreviousSettings, String previousSettingsTitle) {	
		List<GroupType> groups = groupProbe.getProbe().getGroups();
		//Show participant current probabilities
		taskResponsePanel.setGroupsForGroupProbeComponent(groups);
		taskResponsePanel.getGroupProbeComponent().setCurrentSettings(groupProbe.getCurrentSettings());
		if(groupProbe.getPreviousSettings() != null) {
			taskResponsePanel.getGroupProbeComponent().setPreviousSettings(groupProbe.getPreviousSettings());
		}
		taskResponsePanel.getGroupProbeComponent().setTopTitle(participantLabel + "'s probabilities");		
		taskResponsePanel.getGroupProbeComponent().showConfirmedProbabilities();
		taskResponsePanel.getGroupProbeComponent().setSumVisible(false);
		taskResponsePanel.setSubPanelComponent(0, taskResponsePanel.getGroupProbeComponent());
		taskResponsePanel.setSubPanelVisible(0, true);
		
		//Show participant previous probabilities
		if(showPreviousSettings && groupProbe.getPreviousSettings() != null) {
			taskResponsePanel.setGroupsForPreviousSettingsComponent(groups);
			taskResponsePanel.getPreviousSettingsComponent().setTopTitle(previousSettingsTitle);
			taskResponsePanel.getPreviousSettingsComponent().setCurrentSettings(groupProbe.getPreviousSettings());
			taskResponsePanel.getPreviousSettingsComponent().showConfirmedProbabilities();
			taskResponsePanel.setSubPanelComponent(1, taskResponsePanel.getPreviousSettingsComponent());
			taskResponsePanel.setSubPanelVisible(1, true);	
		} else {
			taskResponsePanel.setSubPanelVisible(1, false);
		}
		
		//Show normative probabilities
		if(groupProbe.isNormativeSettingsPresent()) {
			taskResponsePanel.setGroupsForNormativeProbsComponent(groups);
			taskResponsePanel.getNormativeProbsComponent().setCurrentSettings(groupProbe.getNormativeSettings());
			taskResponsePanel.setSubPanelComponent(2, taskResponsePanel.getNormativeProbsComponent());
			taskResponsePanel.setSubPanelVisible(2, true);
		} else {
			taskResponsePanel.setSubPanelVisible(2, false);
		}
		
		//Show average human probabilities
		if(showAvgHumanSettings && groupProbe.isAvgHumanSettingsPresent()) {
			taskResponsePanel.setGroupsForAvgHumanProbsComponent(groups);
			taskResponsePanel.getAvgHumanProbsComponent().setCurrentSettings(groupProbe.getAvgHumanSettings());
			taskResponsePanel.setSubPanelComponent(3, taskResponsePanel.getAvgHumanProbsComponent());
			taskResponsePanel.setSubPanelVisible(3, true);
		} else {
			taskResponsePanel.setSubPanelVisible(3, false);	
		}
	}	
	
	protected void showTroopAllocationProbe(List<GroupType> groups, List<Integer> previousSettings, boolean showPreviousSettings,
			TroopAllocationMultiGroupTrialPartState troopProbe) {
		//Show participant's troop allocation
		taskResponsePanel.setGroupsForTroopAllocationMultiGroupComponent(groups);
		taskResponsePanel.getTroopAllocationMultiGroupComponent().setTopTitle(participantLabel + "'s troop allocations");
		taskResponsePanel.getTroopAllocationMultiGroupComponent().showConfirmedProbabilities();
		taskResponsePanel.getTroopAllocationMultiGroupComponent().setSumVisible(false);
		taskResponsePanel.getTroopAllocationMultiGroupComponent().setCurrentSettings(troopProbe.getCurrentSettings());
		taskResponsePanel.setSubPanelComponent(0, taskResponsePanel.getTroopAllocationMultiGroupComponent());
		taskResponsePanel.setSubPanelVisible(0, true);
		
		if(showPreviousSettings) {
			//Show participant's probabilities
			taskResponsePanel.setGroupsForPreviousSettingsComponent(groups);
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
			taskResponsePanel.setGroupsForNormativeTroopAllocationComponent(groups);
			taskResponsePanel.getNormativeTroopAllocationComponent().setCurrentSettings(troopProbe.getNormativeSettings());
			taskResponsePanel.setSubPanelComponent(2, taskResponsePanel.getNormativeTroopAllocationComponent());
			taskResponsePanel.setSubPanelVisible(2, true);
		} else {
			taskResponsePanel.setSubPanelVisible(2, false);
		}
		
		//Show average human troop allocation
		if(troopProbe.isAvgHumanSettingsPresent()) {
			taskResponsePanel.setGroupsForAvgHumanTroopAllocationComponent(groups);
			taskResponsePanel.getAvgHumanTroopAllocationComponent().setCurrentSettings(troopProbe.getAvgHumanSettings());
			taskResponsePanel.setSubPanelComponent(3, taskResponsePanel.getAvgHumanTroopAllocationComponent());
			taskResponsePanel.setSubPanelVisible(3, true);
		} else {
			taskResponsePanel.setSubPanelVisible(3, false);
		}
	}
	
	protected void showSurpriseProbe(boolean showTroopAllocation, boolean showPreviousSettings,
			List<GroupType> groups, List<Integer> previousSettings, List<Integer> troopAllocation, 
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
			taskResponsePanel.setGroupsForPreviousSettingsComponent(groups);
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
			taskResponsePanel.setGroupsForPreviousTroopAllocationComponent(groups);
			taskResponsePanel.getPreviousTroopAllocationComponent().setCurrentSettings(troopAllocation);
			taskResponsePanel.getPreviousTroopAllocationComponent().setTopTitle(participantLabel + "'s troop allocations");
			taskResponsePanel.setSubPanelComponent(3, taskResponsePanel.getPreviousTroopAllocationComponent());			
			taskResponsePanel.setSubPanelVisible(3, true);
		} else {
			taskResponsePanel.setSubPanelVisible(3, false);
		}
	}
}