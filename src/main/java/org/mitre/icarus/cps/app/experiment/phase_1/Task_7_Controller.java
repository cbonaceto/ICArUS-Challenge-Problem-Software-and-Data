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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.Task_7_TrialState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.TrialState_Phase1;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.ConfirmSettingsTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.GroupProbeTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.LayerSelectTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.LocationProbeTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.ProbabilityProbeTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.ShowScoreTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.TroopAllocationMultiLocationTrialPartState;
import org.mitre.icarus.cps.app.util.ProbabilityUtils;
import org.mitre.icarus.cps.app.widgets.WidgetConstants;
import org.mitre.icarus.cps.app.widgets.ImageManager.IconSize;
import org.mitre.icarus.cps.app.widgets.map.phase_1.MapConstants_Phase1;
import org.mitre.icarus.cps.app.widgets.map.phase_1.MapPanelContainer;
import org.mitre.icarus.cps.app.widgets.map.phase_1.PlacemarkFactory.AttackLocationType;
import org.mitre.icarus.cps.app.widgets.map.phase_1.objects.AttackLocationPlacemark;
import org.mitre.icarus.cps.app.widgets.phase_1.ColorManager_Phase1;
import org.mitre.icarus.cps.app.widgets.phase_1.ImageManager_Phase1;
import org.mitre.icarus.cps.app.widgets.phase_1.experiment.AttackHistoryPanel;
import org.mitre.icarus.cps.app.widgets.phase_1.experiment.LayerSelectionPanel;
import org.mitre.icarus.cps.app.widgets.probability_entry.IProbabilityEntryContainer;
import org.mitre.icarus.cps.exam.base.IcarusExam.NormalizationMode;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_7_Phase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_7_Trial;
import org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.Task_7_INTLayerPresentationProbe;
import org.mitre.icarus.cps.experiment_core.event.ConditionEvent;
import org.mitre.icarus.cps.experiment_core.event.SubjectActionEvent;
import org.mitre.icarus.cps.experiment_core.gui.INavButtonPanel.ButtonType;
import org.mitre.icarus.cps.experiment_core.gui.JPanelConditionComponent;
import org.mitre.icarus.cps.experiment_core.subject_data.SubjectConditionData;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupCenter;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupType;
import org.mitre.icarus.cps.feature_vector.phase_1.IntType;

/**
 * Controller for Task 7.
 * 
 * @author CBONACETO
 *
 */
public class Task_7_Controller extends Task_4_5_6_7_ControllerBase<Task_7_Phase> {
	
	/** The trial states for each trial in the task */
	protected List<Task_7_TrialState> trialStates;
	
	/** The trial state for the current trial */
	protected Task_7_TrialState currentTrialState;
	
	/** Panel with button to show the group probe */
	protected ShowGroupProbePanel groupProbePanel;
	protected boolean updateGroupProbeButtonPushed = false;
	
	/** The layer selection panel */
	protected LayerForagePanel layerPanel;
	
	/** The attack history panel */
	protected AttackHistoryPanel attackHistoryPanel;
	
	/** Whether to show the attack history panel */
	protected boolean showAttackHistoryPanel = false;
	
	/** The previous group probe probability settings */
	protected List<Integer> previousGroupProbeSettings;	
	protected GroupProbeTrialPartState previousGroupProbe;
	
	protected boolean showPreviousGroupProbeSettings;
	
	/** The previous location probe probability settings */
	protected List<Integer> previousLocationProbeSettings;
	
	protected boolean showPreviousLocationProbeSettings;
	
	/** The troop allocation probe settings */
	protected List<Integer> troopAllocation;
	
	/** Historic attack locations from previous trials */
	protected LinkedList<AttackLocationPlacemark> attackLocationHistory;
	
	/** The maximum number of historic attack locations to show */
	protected int maxNumberHistoricAttackLocations = 5;
	
	/** The previous attack location shown */
	protected AttackLocationPlacemark prevAttackLocation;
	protected String prevAttackLocationName;
	
	/** The number of credits remaining */
	protected double creditsRemaining;
	
	/** Whether the layers should be presented in the reverse order */
	protected boolean reverseLayerOrder = false;
	
	protected static final DecimalFormat creditsFormatter = new DecimalFormat("###.##");
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment.phase_1.TaskController#initializeTaskController(org.mitre.icarus.cps.exam.phase_1.testing.TaskTestPhase, org.mitre.icarus.cps.gui.phase_1.experiment.ConditionPanel_Phase1)
	 */
	@Override
	public void initializeTask(Task_7_Phase task) {
		attackLocationHistory = new LinkedList<AttackLocationPlacemark>();
		this.task = task;
		reverseLayerOrder = (task.isReverseLayerOrder() != null) ? task.isReverseLayerOrder() : false;
		if(showAttackHistoryPanel) {
			if(attackHistoryPanel == null) {
				attackHistoryPanel = new AttackHistoryPanel(5, "Attack<br>History");
			}
			else {
				//Clear the attack history panel
				attackHistoryPanel.clearAttackHistory();
			}
		}
		if(groupProbePanel == null) {
			groupProbePanel = new ShowGroupProbePanel(taskInputPanel.getGroupProbeComponent());
		}
		if(layerPanel == null) {
			layerPanel = new LayerForagePanel();
			layerPanel.purchaseButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					purchaseSelectedLayer();
				}
			});
		}		
		taskInputPanel.setLayerSelectionPanel(layerPanel.layerPanel);
		
		//Create the trial states for each trial
		trialStates = new ArrayList<Task_7_TrialState>();
		locationsPresent.clear();
		groupCenterGroupsPresent.clear();
		intLayersPresent.clear();
		if(task.getTestTrials() != null && !task.getTestTrials().isEmpty()) {
			int trialNum = 0;
			for(Task_7_Trial trial : task.getTestTrials()) {
				trial.setTrialResponse(null);
				trialStates.add(new Task_7_TrialState(trialNum, trial, exam.getNormalizationMode(),
						trialNum < task.getTestTrials().size() - 1));
				locationsPresent.addAll(trial.getPossibleAttackLocations());
				groupCenterGroupsPresent.addAll(getGroupCenterGroups(trial.getGroupCenters()));
				intLayersPresent.addAll(getIntLayerTypesForTrial(trial));				
				trialNum++;
			}			
		}
	}	
	
	protected List<IntType> getIntLayerTypesForTrial(Task_7_Trial trial) {
		ArrayList<IntType> intLayers = null;
		if(trial.getIntLayers() != null && !trial.getIntLayers().isEmpty()) {
			intLayers = new ArrayList<IntType>(trial.getIntLayers().size());
			for(Task_7_INTLayerPresentationProbe layer : trial.getIntLayers()) {
				intLayers.add(layer.getLayerType().getLayerType());
			}
		}
		return intLayers;
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment.phase_1.TaskController#getTaskWithResponseData()
	 */
	@Override
	public Task_7_Phase getTaskWithResponseData() {
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
		prevAttackLocation = null;
		groupCenters = null;
		attackLocations = null;
		mapPanel.resetMap();
		mapPanel.setLayerInstructionsEnabled(true);			
		
		//Add the roads layer to the map
		mapPanel.setRoadLayerEnabled(true);
		mapPanel.setRoadsLegendItemVisible(true);
		
		//Add the roads for the task to the map (they are fixed for the entire task)
		conditionPanel.getMapPanel().setRoads(task.getRoads());
		
		//Add the SIGACTs layer to the map and call it "Locations"
		mapPanel.setSigactLayerEnabled(true);		
		mapPanel.setSigactsLayerAndLegendName("Locations");
		
		//Add the SIGACTs legend items to the map for each location
		mapPanel.setSigactsLegendItemVisible(!locationsPresent.isEmpty());
		mapPanel.setSigactLocationsInLegend(locationsPresent);		
		
		//Add the group centers layer to the map
		mapPanel.setGroupCentersLayerEnabled(true);
		mapPanel.setGroupCentersLayerEditable(false);
		
		//Add the group centers legend item to the map
		mapPanel.setGroupCentersLegendItemVisible(!groupCenterGroupsPresent.isEmpty());
		mapPanel.setGroupCenterGroupsForLegend(groupCenterGroupsPresent);
		mapPanel.setGroupCentersLayerAndLegendName("HUMINT");
		
		//Add the SOCINT layer and overlay to the map (SOCINT regions are fixed for the entire task)
		if(task.getRegionsOverlay() != null) {
			mapPanel.setSocintRegionsOverlay(task.getRegionsOverlay());
			mapPanel.setSocintRegionsLayerEnabled(true);
			mapPanel.setSocintLegendItemVisible(true);
			if(task.getRegionsOverlay().getGroups() != null) {
				mapPanel.setSocintGroupsForLegend(task.getRegionsOverlay().getGroups());	
			}
		}		
		
		//Add the legend items for each INT layer present
		addIntLayersToLegend(intLayersPresent, reverseLayerOrder);
		
		mapPanel.setShowScale(true);
		mapPanel.redrawMap();
		
		//Clear the task input panel
		creditsRemaining = task.getInitialCredits(); 
		conditionPanel.setTaskInputPanelVisible(true);
		taskInputPanel.setAllSubPanelsVisible(false);
		if(showAttackHistoryPanel) {
			taskInputPanel.setSubPanelComponent(3, attackHistoryPanel);
			taskInputPanel.setSubPanelVisible(3, true, GridBagConstraints.SOUTH);
		}		
		conditionPanel.showTaskScreen();
		
		//Load the first trial
		previousGroupProbeSettings = null;
		previousGroupProbe = null;
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
			if(currentTrialPart instanceof GroupProbeTrialPartState) {
				if(taskInputPanel.getGroupProbeComponent() != null) {
					GroupProbeTrialPartState groupProbe = (GroupProbeTrialPartState)currentTrialPart;
					groupProbe.setInteractionTimes(taskInputPanel.getGroupProbeComponent().getInteractionTimes());
					previousGroupProbe = groupProbe;
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
					previousGroupProbeSettings = groupProbe.getCurrentNormalizedSettings();
					//Add the highest-probability group to the attack history display
					//if(exam.getNormalizationMode() != NormalizationMode.NormalizeAfterAndConfirm) {
					//	attackHistoryPanel.addAttack(getHighestProbabilityGroup(groupProbe), currentTrial+1);
					//}
				}
			} else if(currentTrialPart instanceof LocationProbeTrialPartState) {
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
					previousLocationProbeSettings = locationProbe.getCurrentNormalizedSettings();
				}
			} else if(currentTrialPart instanceof TroopAllocationMultiLocationTrialPartState) {
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
					troopAllocation = troopProbe.getCurrentNormalizedSettings();
					int groundTruthIndex = troopProbe.getProbe().getLocations().indexOf(currentTrialState.getTrial().getGroundTruth().getAttackLocationId());
					if(groundTruthIndex >= 0) {
						if(examController.getScoreComputer() != null) {
							currentTrialState.setScore_s2(examController.getScoreComputer().computeTroopAllocationScoreS2(
									troopProbe.getCurrentNormalizedSettings(), groundTruthIndex));
						}
					}
				}
			} else if(currentTrialPart instanceof LayerSelectTrialPartState) {
				//Warn if a layer is selected but hasn't been purchased
				if(layerPanel.showingLayers && layerPanel.layerPanel.getSelectedLayer() != null) {
					if(JOptionPane.showConfirmDialog(examController.getParentWindow(), 
							"<html><center>You have selected a layer but haven't purchased it.<br>" +
							"Would you like to continue anyway?</center></html>", 
							"", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION) {
						responseValid = false;
					}
				}
			}
			/*else if(currentTrialPart instanceof ConfirmSettingsTrialPartState && !adjustingNormalizedSettings) {
				ConfirmSettingsTrialPartState confirmState = (ConfirmSettingsTrialPartState)currentTrialPart;
				if(confirmState.getProbabiltyProbe() instanceof GroupProbeTrialPartState) {
					//Add the highest-probability group to the attack history display
					attackHistoryPanel.addAttack(getHighestProbabilityGroup(
							(GroupProbeTrialPartState)confirmState.getProbabiltyProbe()), currentTrial+1);
				}
			}*/
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

				// Fire condition completed event
				fireConditionEvent(new ConditionEvent(ConditionEvent.CONDITION_COMPLETED, 
						currentTrial, this));
				
				return;
			} else {				
				//Set the current trial
				currentTrialState = trialStates.get(currentTrial);
				currentTrialPart = null;
				showPreviousGroupProbeSettings = false;
				previousLocationProbeSettings = null;
				showPreviousLocationProbeSettings = false;						
				
				//Remove the possible attack locations from the last trial
				if(attackLocations != null) {
					for(AttackLocationPlacemark attackLocation : attackLocations.values()) {
						if(attackLocation != prevAttackLocation) {							
							conditionPanel.getMapPanel().removeSigactLocation(attackLocation);
						}
					}
				}				
				
				//Unhighlight the previous attack location and remove its INTs
				if(prevAttackLocation != null) {
					prevAttackLocation.setToolTipText(prevAttackLocationName);
					
					prevAttackLocation.removeImintAnnotation();
					prevAttackLocation.removeMovintAnnotation();
					prevAttackLocation.removeSigintAnnotation();
					prevAttackLocation.setBackgroundColor(null);
					prevAttackLocation.setBorderLineWidth(0);
					prevAttackLocation = null;
				}
				
				//Add the group centers to the map
				groupCenters = conditionPanel.getMapPanel().setGroupCenters(
						currentTrialState.getTrial().getGroupCenters(), false);
				if(currentTrialState.getTrial().getGroupCenters() != null && 
						!currentTrialState.getTrial().getGroupCenters().isEmpty()) {
					ArrayList<GroupType> groups = new ArrayList<GroupType>(
							currentTrialState.getTrial().getGroupCenters().size());
					for(GroupCenter groupCenter : currentTrialState.getTrial().getGroupCenters()) {
						groups.add(groupCenter.getGroup());
					}
					//conditionPanel.getMapPanel().setGroupCenterGroupsForLegend(groups);
				}		
				
				//Clear the INT layers (except SOCINT) from the map
				conditionPanel.getMapPanel().setAdditionalINTLayersEnabled(false);				
				conditionPanel.getMapPanel().setSocintRegionsLayerEnabled(true);
				//conditionPanel.getMapPanel().setImintLegendItemVisible(false);
				//conditionPanel.getMapPanel().setMovintLegendItemVisible(false);
				//conditionPanel.getMapPanel().setSigintLegendItemVisible(false);				
				conditionPanel.getMapPanel().redrawMap();
				
				//Add the highest-probability group from the last trial to the attack history display
				if(showAttackHistoryPanel && previousGroupProbe != null) {
					attackHistoryPanel.addAttack(getHighestProbabilityGroup(previousGroupProbe), currentTrial);
				}
			}
		}
		
		//Fire a trial changed event
		super.setTrial(currentTrial, trialPhase, currentTrialState.getNumTrialParts());

		//Show the trial part			
		currentTrialPart = currentTrialState.getTrialParts().get(trialPhase);

		if(currentTrialPart instanceof GroupProbeTrialPartState) {	
			//Show the group probe
			GroupProbeTrialPartState groupProbe = (GroupProbeTrialPartState)currentTrialPart;
			StringBuilder instructions = createTrialNumberString(currentTrial+1, trialPhase+1, 
					currentTrialState.getNumTrialParts(), "Group Probe");

			String groupProbeTitle = null;
			if(currentTrial == 0 && task.getInitialResponsibleGroup() != null) {
				//Add the initial responsible group to the attack history
				if(showAttackHistoryPanel && !adjustingNormalizedSettings) {
					attackHistoryPanel.addAttack(task.getInitialResponsibleGroup(), 0);
				}
				instructions.append("<b>Group " + task.getInitialResponsibleGroup().getGroupNameFull() + 
						"</b> was the last group to attack. Group probabilities have been initialized based on that intelligence. " +
						"Click Next to continue.");	
				//instructions.append("Indicate the probability that each group is responsible for the current attack" +  
				//"</b>, and click Next to continue. ");
				groupProbePanel.enableButton.setEnabled(false);
				groupProbeTitle = "initial group probabilities";
			} else {
				instructions.append("Given the attacks you have seen, indicate the probability that each" +
						" group is responsible for the current attack. Click Next to continue when you are ready. ");
				//instructions.append("Probabilities must sum to 100%.");				
				groupProbePanel.enableButton.setEnabled(true);
				groupProbeTitle = "your group probabilities";
			}
		
			if(currentTrial == 0 && !adjustingNormalizedSettings && previousGroupProbeSettings == null) {
				//Initialize probabilities based on first responsible group
				previousGroupProbeSettings = ProbabilityUtils.createProbabilities(groupProbe.getProbe().getGroups().size(), 5);
				int groupIndex = groupProbe.getProbe().getGroups().indexOf(task.getInitialResponsibleGroup());				
				if(groupIndex >= 0) {
					previousGroupProbeSettings.set(groupIndex, 85);
					/*if(groupIndex+1 >= previousGroupProbeSettings.size()) {
						previousGroupProbeSettings.set(groupIndex-1, 5);
					}
					else {
						previousGroupProbeSettings.set(groupIndex+1, 5);
					}*/
					ProbabilityUtils.normalizePercentProbabilities(previousGroupProbeSettings, previousGroupProbeSettings);
				}
			}
			//Show the group probe
			if(!adjustingNormalizedSettings && previousGroupProbeSettings != null) {
				groupProbe.setCurrentSettings(previousGroupProbeSettings);				
			}
			groupProbePanel.instructionText = instructions.toString();
			showGroupProbe(!adjustingNormalizedSettings, // && currentTrial > 0,
					groupProbe.getProbe().getGroups(), groupProbe.getCurrentSettings(), 
					(adjustingNormalizedSettings || previousGroupProbeSettings == null) ? 
							groupProbe.getPreviousSettings() : previousGroupProbeSettings,
							groupProbeTitle);			
			if(!adjustingNormalizedSettings) { // && currentTrial > 0) {
				updateGroupProbeButtonPushed = false;
				if(currentTrial > 0) {
					StringBuilder sb = createTrialNumberString(currentTrial+1, trialPhase+1, 
							currentTrialState.getNumTrialParts(), "Group Probe");
					sb.append("Click 'Update Group Judgment' to update the probability that each group will attack, " +
							"or click Next to continue.");								
					conditionPanel.setInstructionBannerText(sb.toString());
				} else {
					conditionPanel.setInstructionBannerText(instructions.toString());
				}
			} else {
				updateGroupProbeButtonPushed = true;
				conditionPanel.setInstructionBannerText(instructions.toString());	
			}			
		} else if(currentTrialPart instanceof LocationProbeTrialPartState) {
			showPreviousGroupProbeSettings = false;
			//Add the attack locations for the trial to the map
			if(!adjustingNormalizedSettings) {
				attackLocations = conditionPanel.getMapPanel().setSigactLocations(
						currentTrialState.getTrial().getPossibleAttackLocations(), AttackLocationType.LOCATION, false, true);
				//conditionPanel.getMapPanel().setSigactLocationsInLegend(
				//		currentTrialState.getTrial().getPossibleAttackLocations());
				conditionPanel.getMapPanel().redrawMap();
			}			
			//Show the location probe
			LocationProbeTrialPartState locationProbe = (LocationProbeTrialPartState)currentTrialPart;
			if(!adjustingNormalizedSettings && previousSettings != null) {
				locationProbe.setCurrentSettings(previousSettings);				
			}
			showLocationProbe(locationProbe.getProbe().getLocations(), previousGroupProbe.getProbe().getGroups(),
					locationProbe.getCurrentSettings(), 
					(adjustingNormalizedSettings || previousSettings == null) ? 
							locationProbe.getPreviousSettings() : previousLocationProbeSettings,							
					previousGroupProbeSettings, showPreviousGroupProbeSettings);
			
			StringBuilder sb = createTrialNumberString(currentTrial+1, trialPhase+1, 
					currentTrialState.getNumTrialParts(), "Location Probe");
			sb.append("Indicate the probability of attack at each location, and click Next to continue.");
			conditionPanel.setInstructionBannerText(sb.toString());			
		} else if(currentTrialPart instanceof ConfirmSettingsTrialPartState) {
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
			} else if(confirmSettingsProbe.getProbabiltyProbe() instanceof LocationProbeTrialPartState) {
				ProbabilityProbeTrialPartState locationProbe = confirmSettingsProbe.getProbabiltyProbe();
				taskInputPanel.getLocationProbeComponent().setCurrentSettings(locationProbe.getCurrentNormalizedSettings());
				taskInputPanel.getLocationProbeComponent().showConfirmedProbabilities();
				sb.append("Your probabilities have been normalized to sum to 100%. Click Back to adjust them or click Next to continue.");
			} else if(confirmSettingsProbe.getProbabiltyProbe() instanceof TroopAllocationMultiLocationTrialPartState) {
				ProbabilityProbeTrialPartState troopProbe = confirmSettingsProbe.getProbabiltyProbe();
				taskInputPanel.getTroopAllocationMultiLocationComponent().setCurrentSettings(
						troopProbe.getCurrentNormalizedSettings());
				taskInputPanel.getTroopAllocationMultiLocationComponent().showConfirmedProbabilities();
				sb.append("Your troop allocations have been normalized to sum to 100%. Click Back to adjust them or click Next to continue.");
			}			
			conditionPanel.setInstructionBannerText(sb.toString());
			examController.setNavButtonEnabled(ButtonType.Back, true);
		} else if(currentTrialPart instanceof TroopAllocationMultiLocationTrialPartState) {
			showPreviousLocationProbeSettings = true;
			showPreviousGroupProbeSettings = false;
			TroopAllocationMultiLocationTrialPartState troopProbe = (TroopAllocationMultiLocationTrialPartState)currentTrialPart;			
			showTroopAllocationProbe(troopProbe.getProbe().getLocations(), 
					troopProbe.getCurrentSettings(), troopProbe.getPreviousSettings(),
					previousLocationProbeSettings, showPreviousLocationProbeSettings);
			StringBuilder sb = createTrialNumberString(currentTrial+1, trialPhase+1, 
					currentTrialState.getNumTrialParts(), "Troop Allocation");
			sb.append("Allocate troops to each location, and click Next to continue.");
			conditionPanel.setInstructionBannerText(sb.toString());				
		} else if(currentTrialPart instanceof ShowScoreTrialPartState) {			
			//Show the score (S2) for the trial and ground truth
			StringBuilder instructions = createTrialNumberString(currentTrial+1, trialPhase+1, 
					currentTrialState.getNumTrialParts(), "Ground Truth");
			instructions.append("The attack occured at <b>Location " + 
					currentTrialState.getTrial().getGroundTruth().getAttackLocationId() + " </b>");
			GroupType responsibleGroup = null;
			if(currentTrialState.getTrial().isResponsibleGroupShown()) {
				responsibleGroup = currentTrialState.getTrial().getGroundTruth().getResponsibleGroup();
				instructions.append(", and <b> Group " + responsibleGroup.getGroupNameFull() + "</b> was responsible. ");
			} else {
				instructions.append(", and the group responsible is not known (<b>?</b>). ");
			}			
			
			if(attackLocations != null) {
				AttackLocationPlacemark placemark = attackLocations.get(
						currentTrialState.getTrial().getGroundTruth().getAttackLocationId());
				if(placemark != null) {
					//Highlight the actual attack location and show the group at that location if the group is shown
					placemark.setHighlighted(true);
					if(responsibleGroup != null) {
						placemark.setName(responsibleGroup.toString());
						Color groupColor = ColorManager_Phase1.getGroupCenterColor(responsibleGroup);
						placemark.setBorderColor(groupColor);
						placemark.setForegroundColor(groupColor);
						if(WidgetConstants.USE_GROUP_SYMBOLS) {
							placemark.setMarkerIcon(ImageManager_Phase1.getGroupSymbolImage(responsibleGroup, IconSize.Small));
							placemark.setShowName(false);
						}
						prevAttackLocationName = "<html><u>" + responsibleGroup.getGroupNameAbbreviated() + "</u>" + 
							responsibleGroup.getGroupNameFull().substring(1) + " Attack</html>";
					} else {
						//placemark.setForegroundColor(MapConstants.HIGHLIGHT_COLOR);
						placemark.setBorderColor(Color.red);
						placemark.setForegroundColor(Color.red);
						placemark.setName("?");						
						prevAttackLocationName = "<html>Unknown group attack</html>";
					}
					placemark.setTextFont(MapConstants_Phase1.PLACEMARK_FONT_LARGE);
					placemark.setBorderLineWidth(2.f);
					placemark.setToolTipText(prevAttackLocationName);
					
					//Add the attack location to the attack location history and remove the oldest historic attack location if necessary
					prevAttackLocation = placemark;
					attackLocationHistory.add(placemark);			
					if(attackLocationHistory.size() > maxNumberHistoricAttackLocations) {
						AttackLocationPlacemark eldestEntry = attackLocationHistory.poll();
						conditionPanel.getMapPanel().removeSigactLocation(eldestEntry);
					}
					
					conditionPanel.getMapPanel().redrawMap();
				}	
			}
			
			//Compute the number of credits to award
			double credits = 0.d;
			int groundTruthIndex = currentTrialState.getTrial().getAttackLocationProbe_locations().getLocations().indexOf(
					currentTrialState.getTrial().getGroundTruth().getAttackLocationId());
			if(groundTruthIndex >= 0) {
				credits = (troopAllocation.get(groundTruthIndex)/100.d) * task.getCorrectPredictionCredits();
			}
			creditsRemaining += credits;
			instructions.append("Your received<b> " + creditsFormatter.format(credits) + " " + ((credits == 1) ? "credit" : "credits") + "</b> on this trial for " +
			"allocating troops. Click Next to continue.");
			conditionPanel.setInstructionBannerText(instructions.toString());
		} else if(currentTrialPart instanceof LayerSelectTrialPartState) {
			showPreviousGroupProbeSettings = false;			
			//Show the layer selection			
			showLayerSelection(previousGroupProbeSettings, showPreviousGroupProbeSettings);
			StringBuilder sb = createTrialNumberString(currentTrial+1, trialPhase+1, 
					currentTrialState.getNumTrialParts(), "Layer Forage");
			sb.append("Next, you will have a chance to update the probability that each group will attack. " +
					"If you'd like to purchase intelligence first, click 'Purchase Layers'.");
			conditionPanel.setInstructionBannerText(sb.toString());
		}

		//Pause before allowing a next button press
		pauseBeforeNextTrial();
	}	

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment_core.event.SubjectActionListener#subjectActionPerformed(org.mitre.icarus.cps.experiment_core.event.SubjectActionEvent)
	 */
	@Override
	public void subjectActionPerformed(SubjectActionEvent event) {
		if(event.eventType == SubjectActionEvent.NEXT_BUTTON_PRESSED) {			
			//Advance to the next trial or trial part in the task
			if(currentTrialPart != null && currentTrialPart instanceof GroupProbeTrialPartState &&
					!updateGroupProbeButtonPushed && exam.getNormalizationMode() == NormalizationMode.NormalizeAfterAndConfirm) {
				//Skip normalized confirmation if user didn't choose to update group probabilities
				currentTrialPart.setTrialPartTime_ms(currentTrialPart.getTrialPartTime_ms() + 
						(System.currentTimeMillis() - trialPartStartTime));
				if(taskInputPanel.getGroupProbeComponent() != null) {
					GroupProbeTrialPartState groupProbe = (GroupProbeTrialPartState)currentTrialPart;
					groupProbe.setPreviousSettings(groupProbe.getCurrentSettings());
					groupProbe.setCurrentSettings(taskInputPanel.getGroupProbeComponent().getCurrentSettings());
					if(groupProbe.getCurrentNormalizedSettings() == null || 
							groupProbe.getCurrentNormalizedSettings().size() != groupProbe.getCurrentSettings().size()) {
						groupProbe.setCurrentNormalizedSettings(
								ProbabilityUtils.createDefaultInitialProbabilities(groupProbe.getCurrentSettings().size()));
					}
					ProbabilityUtils.normalizePercentProbabilities(groupProbe.getCurrentSettings(), groupProbe.getCurrentNormalizedSettings());
					previousGroupProbeSettings = groupProbe.getCurrentNormalizedSettings();
					//Add the highest-probability group to the attack history display
					//attackHistoryPanel.addAttack(getHighestProbabilityGroup(groupProbe), currentTrial+1);
				}
				trialPhase++;
			}
			nextTrial();
		} else if(event.eventType == SubjectActionEvent.BACK_BUTTON_PRESSED) {
			//Go to the previous trial part.  This is only allowed to adjust probabilities.
			if(currentTrialPart instanceof ConfirmSettingsTrialPartState) {
				adjustingNormalizedSettings = true;
				trialPhase -= 2;
				nextTrial();
			}
		}
	}
	
	/**
	 * Get the group with the highest probability assessment.
	 * 
	 * @param groupProbeSettings
	 * @return
	 */
	protected GroupType getHighestProbabilityGroup(GroupProbeTrialPartState groupProbe) {
		int maxProb = 0;
		int maxProbIndex = 0;
		if(groupProbe.getCurrentNormalizedSettings() != null) {
			int index = 0;
			for(Integer prob : groupProbe.getCurrentNormalizedSettings()) {
				if(prob > maxProb) {
					maxProb = prob;
					maxProbIndex = index;
				}
				index++;
			}
		}
		if(groupProbe.getProbe() != null && groupProbe.getProbe().getGroups() != null &&
				!groupProbe.getProbe().getGroups().isEmpty() &&
				maxProbIndex < groupProbe.getProbe().getGroups().size()) {
			return groupProbe.getProbe().getGroups().get(maxProbIndex);
		}
		return null;
	}
	
	/**
	 * Purchase the currently selected layer
	 */
	protected void purchaseSelectedLayer() {
		Task_7_INTLayerPresentationProbe selectedLayer = layerPanel.layerPanel.getSelectedLayer();
		if(selectedLayer != null) {			
			if(currentTrialState.getLayerForageProbe().getSelectedLayers() == null) {
				currentTrialState.getLayerForageProbe().setSelectedLayers(new LinkedList<Task_7_INTLayerPresentationProbe>());
			}
			currentTrialState.getLayerForageProbe().getSelectedLayers().add(selectedLayer);
			
			//Show the selected layer			
			if(selectedLayer.getLayerType().getLayerType() == IntType.SIGINT) {
				addINTLayer(selectedLayer.getLayerType(), true, false);
			} else {
				addINTLayer(selectedLayer.getLayerType(), false, true);
			}	
			
			//If a SIGINT layer was selected disable all SIGINT layers.  Otherwise, just disable the selected layer
			if(selectedLayer.getLayerType().getLayerType() == IntType.SIGINT) {
				layerPanel.layerPanel.setSigintLayersEnabled(false);
				//layerPanel.layerPanel.removeSigintLayers();
			} else {
				layerPanel.layerPanel.setLayerEnabled(selectedLayer, false);
				//layerPanel.layerPanel.removeLayer(selectedLayer);
			}
			
			//Deduct the cost of the layer and update any layers that are no longer able to be purchased
			creditsRemaining -= selectedLayer.getCostCredits();
			layerPanel.setRemainingCredits(creditsRemaining);
			
			StringBuilder sb = createTrialNumberString(currentTrial+1, trialPhase+1, 
					currentTrialState.getNumTrialParts(), "Layer Forage");
			if(updatePurchasableLayers() == 0) {
				sb.append("The <b> " + selectedLayer.getLayerType().getLayerType() + " layer</b> is now shown. " +
						"There are no additional layers that may be purchased. Click Next to continue.");
			} else {
				sb.append("The <b> " + selectedLayer.getLayerType().getLayerType() + " layer</b> is now shown. " +
						"You may continue purchasing layers, or click Next to continue.");
			}
			conditionPanel.setInstructionBannerText(sb.toString());
		}		
	}
	
	protected int updatePurchasableLayers() {
		int numAvailableLayers = 0;
		if(layerPanel.layerPanel.getNumLayers() > 0) {
			Collection<Task_7_INTLayerPresentationProbe> layers = layerPanel.layerPanel.getEnabledLayers();
			numAvailableLayers = layers.size();				
			for(Task_7_INTLayerPresentationProbe layer : layers) {
				if(layer.getCostCredits() > creditsRemaining) {
					layerPanel.layerPanel.setLayerEnabled(layer, false);
					numAvailableLayers--;
				}
			}
		}			
		layerPanel.purchaseButton.setEnabled(numAvailableLayers > 0);
		return numAvailableLayers;
	}
	
	protected void showGroupProbe(boolean showEnableButton, List<GroupType> groups,
			List<Integer> currentSettings, List<Integer> previousGroupProbeSettings,
			String groupProbeTitle) {		
		taskInputPanel.setGroupsForGroupProbeComponent(groups);
		IProbabilityEntryContainer groupProbe = taskInputPanel.getGroupProbeComponent();
		groupProbe.setCurrentSettings(currentSettings);
		groupProbe.setPreviousSettings(previousGroupProbeSettings);
		groupProbe.setTopTitle(groupProbeTitle);
		groupProbe.showEditableProbabilities();
		taskInputPanel.getGroupProbeComponent().setSumVisible(true);
		
		taskInputPanel.setSubPanelComponent(0, groupProbePanel);
		groupProbePanel.setGroupProbeComponent(groupProbe);
		if(showEnableButton) {
			groupProbePanel.showEnableGroupProbeButton();
		} else {
			groupProbePanel.showGroupProbe();
		}
		if(!adjustingNormalizedSettings) {
			groupProbe.resetInteractionTimes();
		}
		
		taskInputPanel.setSubPanelVisible(0, true);		
		taskInputPanel.setSubPanelVisible(1, false);
		taskInputPanel.setSubPanelVisible(2, false);
		if(!showAttackHistoryPanel) {
			taskInputPanel.setSubPanelVisible(3, false);
		}
	}
	
	protected void showLocationProbe(List<String> locations, List<GroupType> groups,
			List<Integer> currentSettings, List<Integer> previousLocationProbeSettings,
			List<Integer> previousGroupProbeSettings, boolean showPreviousGroupProbeSettings) {		
		taskInputPanel.setLocationsForLocationProbeComponent(locations);
		taskInputPanel.getLocationProbeComponent().setCurrentSettings(currentSettings);
		taskInputPanel.getLocationProbeComponent().setPreviousSettings(previousLocationProbeSettings);
		taskInputPanel.getLocationProbeComponent().showEditableProbabilities();
		taskInputPanel.getLocationProbeComponent().setSumVisible(true);
		taskInputPanel.getLocationProbeComponent().setTopTitle("your location probabilities");
		taskInputPanel.setSubPanelComponent(0, taskInputPanel.getLocationProbeComponent());		
		if(showPreviousGroupProbeSettings) {
			/*taskInputPanel.getGroupProbeComponent().setCurrentSettings(previousGroupProbeSettings);
			taskInputPanel.getGroupProbeComponent().showConfirmedProbabilities();
			taskInputPanel.getGroupProbeComponent().setSumVisible(false);
			groupProbePanel.enableButton.setVisible(false);
			taskInputPanel.setSubPanelComponent(1, groupProbePanel);*/
			taskInputPanel.setGroupsForPreviousSettingsComponent(groups);
			taskInputPanel.getPreviousSettingsComponent().setCurrentSettings(previousGroupProbeSettings);
			taskInputPanel.setSubPanelComponent(1, taskInputPanel.getPreviousSettingsComponent());
		}
		if(!adjustingNormalizedSettings) {
			taskInputPanel.getLocationProbeComponent().resetInteractionTimes();
		}		
		taskInputPanel.setSubPanelVisible(0, true);		
		taskInputPanel.setSubPanelVisible(1, showPreviousGroupProbeSettings);
		taskInputPanel.setSubPanelVisible(2, false);
		if(!showAttackHistoryPanel) {
			taskInputPanel.setSubPanelVisible(3, false);
		}
	}
	
	protected void showTroopAllocationProbe(List<String> locations, 
			List<Integer> currentAllocations,  List<Integer> previousAllocations,
			List<Integer> previousLocationProbeSettings, boolean showPreviousLocationProbeSettings) {
			//ArrayList<Integer> previousGroupProbeSettings, boolean showPreviousGroupProbeSettings) {
		taskInputPanel.setLocationsForTroopAllocationMultiLocationComponent(locations);
		taskInputPanel.getTroopAllocationMultiLocationComponent().setCurrentSettings(currentAllocations);
		taskInputPanel.getTroopAllocationMultiLocationComponent().setPreviousSettings(previousAllocations);
		taskInputPanel.getTroopAllocationMultiLocationComponent().showEditableProbabilities();
		taskInputPanel.getTroopAllocationMultiLocationComponent().setSumVisible(true);
		taskInputPanel.setSubPanelComponent(0, taskInputPanel.getTroopAllocationMultiLocationComponent());		
		if(showPreviousLocationProbeSettings) {
			/*taskInputPanel.getLocationProbeComponent().setCurrentSettings(previousLocationProbeSettings);
			taskInputPanel.getLocationProbeComponent().showConfirmedProbabilities();
			taskInputPanel.getLocationProbeComponent().setSumVisible(false);
			taskInputPanel.setSubPanelComponent(1, taskInputPanel.getLocationProbeComponent());*/
			taskInputPanel.setLocationsForPreviousSettingsComponent(locations);
			taskInputPanel.getPreviousSettingsComponent().setCurrentSettings(previousLocationProbeSettings);
			taskInputPanel.setSubPanelComponent(1, taskInputPanel.getPreviousSettingsComponent());
		}	
		if(!adjustingNormalizedSettings) {
			taskInputPanel.getTroopAllocationMultiLocationComponent().resetInteractionTimes();
		}
		/*if(showPreviousGroupProbeSettings) {
			taskInputPanel.getGroupProbeComponent().setCurrentSettings(previousGroupProbeSettings);
			taskInputPanel.getGroupProbeComponent().showConfirmedProbabilities();
			taskInputPanel.getGroupProbeComponent().setSumVisible(false);
			groupProbePanel.enableButton.setVisible(false);
			taskInputPanel.setSubPanelComponent(2, groupProbePanel);
		}*/
		
		taskInputPanel.setSubPanelVisible(0, true);
		taskInputPanel.setSubPanelVisible(1, showPreviousLocationProbeSettings);
		taskInputPanel.setSubPanelVisible(2, showPreviousGroupProbeSettings);
		if(!showAttackHistoryPanel) {
			taskInputPanel.setSubPanelVisible(3, false);
		}
	}
	
	protected void showLayerSelection(List<Integer> previousGroupProbeSettings, boolean showPreviousGroupProbeSettings) {		
		layerPanel.layerPanel.setLayers(currentTrialState.getTrial().getIntLayers(), true, reverseLayerOrder);	
		layerPanel.showEnableLayersButton();
		layerPanel.setRemainingCredits(creditsRemaining);
		taskInputPanel.setSubPanelComponent(0, layerPanel, BorderLayout.CENTER);
		
		/*if(showPreviousTroopAllocation) {
			taskInputPanel.getTroopAllocationMultiLocationComponent().setCurrentSettings(previousTroopAllocation);
			taskInputPanel.getTroopAllocationMultiLocationComponent().showConfirmedProbabilities();
			taskInputPanel.setSubPanelComponent(1, taskInputPanel.getTroopAllocationMultiLocationComponent());
		}
				
		if(showPreviousLocationProbeSettings) {
			taskInputPanel.getLocationProbeComponent().setCurrentSettings(previousLocationProbeSettings);
			taskInputPanel.getLocationProbeComponent().showConfirmedProbabilities();
			taskInputPanel.setSubPanelComponent(2, taskInputPanel.getLocationProbeComponent());
		}*/	
		
		if(showPreviousGroupProbeSettings) {
			taskInputPanel.getGroupProbeComponent().setCurrentSettings(previousGroupProbeSettings);
			taskInputPanel.getGroupProbeComponent().showConfirmedProbabilities();
			taskInputPanel.getGroupProbeComponent().setSumVisible(false);
			groupProbePanel.enableButton.setVisible(false);
			taskInputPanel.setSubPanelComponent(1, groupProbePanel);
		}		
		
		taskInputPanel.setSubPanelVisible(0, true);
		taskInputPanel.setSubPanelVisible(1, showPreviousGroupProbeSettings);
		taskInputPanel.setSubPanelVisible(2, false);
		if(!showAttackHistoryPanel) {
			taskInputPanel.setSubPanelVisible(3, false);
		}
	}
	
	protected class ShowGroupProbePanel extends JPanelConditionComponent {
		
		private static final long serialVersionUID = 1L;
		
		IProbabilityEntryContainer groupProbeComponent;
		
		JButton enableButton;		
		
		String instructionText;		

		public ShowGroupProbePanel(IProbabilityEntryContainer groupProbeComponent) {
			super("show_group_probe");
			this.groupProbeComponent = groupProbeComponent;
			setLayout(new GridBagLayout());
			
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridy = 0;
			gbc.anchor = GridBagConstraints.NORTH;
			
			if(groupProbeComponent != null) {
				add(groupProbeComponent.getComponent(), gbc);
			}			
			
			enableButton = new JButton("Update Group Judgment");
			enableButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					updateGroupProbeButtonPushed = true;
					conditionPanel.setInstructionBannerText(instructionText);
					showGroupProbe();
				}
			});
			gbc.gridy = 1;
			gbc.weighty = 1;
			gbc.insets.top = 10;
			add(enableButton, gbc);
		}
		
		public IProbabilityEntryContainer getGroupProbeComponent() {
			return groupProbeComponent;
		}

		public void setGroupProbeComponent(IProbabilityEntryContainer groupProbeComponent) {
			if(groupProbeComponent != this.groupProbeComponent) {
				if(this.groupProbeComponent != null) {
					remove(this.groupProbeComponent.getComponent());
				}
				this.groupProbeComponent = groupProbeComponent;
				GridBagConstraints gbc = new GridBagConstraints();
				gbc.gridy = 0;
				gbc.anchor = GridBagConstraints.NORTH;	
				if(groupProbeComponent != null) {
					add(groupProbeComponent.getComponent(), gbc);
					revalidate();
					repaint();
				}		
			}
		}		

		public void showEnableGroupProbeButton() {
			if(groupProbeComponent != null) {
				groupProbeComponent.showConfirmedProbabilities();
			}			
			enableButton.setVisible(true);
			revalidate();
			repaint();
		}
		
		public void showGroupProbe() {
			//Show attack wave instructions
			conditionPanel.getMapPanel().showInstructionsPage(IntType.WAVEINT.toString());
			
			//Enable editing on group probe probability entry panel
			if(groupProbeComponent != null) {
				groupProbeComponent.showEditableProbabilities();
			}
			enableButton.setVisible(false);
			revalidate();
			repaint();
		}
	}
	
	/**
	 * Adds a purchase button and credits remaining label to a LayerSelectionPanel.
	 *
	 */
	protected class LayerForagePanel extends JPanelConditionComponent {

		private static final long serialVersionUID = 1L;
		
		JButton enableButton;
		
		JPanel purchasePanel;
		
		boolean showingLayers;
		
		LayerSelectionPanel<Task_7_INTLayerPresentationProbe> layerPanel;		
		
		JButton purchaseButton;
		
		JLabel remainingCreditsLabel;
		
		public LayerForagePanel() {
			super("layers");
			setLayout(new GridBagLayout());
			
			enableButton = new JButton("Purchase Layers");
			enableButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					StringBuilder sb = createTrialNumberString(currentTrial+1, trialPhase+1, 
							currentTrialState.getNumTrialParts(), "Layer Forage");
					if(updatePurchasableLayers() == 0) {
						sb.append("There are no additional layers that may be purchased. Click Next to continue.");
					}
					else {
						sb.append("Select a layer in the list and click 'Purchase Layer'. " +
							"Click Next to continue when you are done. Note: When you click Next, you will not" +
							"be able to go back to view the intelligence you have purchased.");
					}
					conditionPanel.setInstructionBannerText(sb.toString());
					showLayers();
				}
			});			
			
			purchasePanel = new JPanel(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.anchor = GridBagConstraints.NORTHWEST;
			
			layerPanel = new LayerSelectionPanel<Task_7_INTLayerPresentationProbe>("layersPanel");
			//gbc.insets.bottom = 10;
			gbc.insets.top = 8;
			purchasePanel.add(layerPanel, gbc);
			
			purchaseButton = new JButton("Purchase Layer");
			gbc.gridy++;
			gbc.anchor = GridBagConstraints.NORTH;
			gbc.insets.top = 8;
			gbc.insets.bottom = 5;
			purchasePanel.add(purchaseButton, gbc);
			
			JPanel creditsPanel = new JPanel(new GridBagLayout());
			GridBagConstraints creditsPanelConstraints = new GridBagConstraints();
			creditsPanelConstraints.gridx = 0;
			creditsPanelConstraints.anchor = GridBagConstraints.NORTHWEST;
			creditsPanelConstraints.insets.right = 10;
			creditsPanel.add(new JLabel("Credits Remaining:"), creditsPanelConstraints);
			remainingCreditsLabel = new JLabel();
			//remainingCreditsLabel.setPreferredSize(remainingCreditsLabel.getPreferredSize());
			//remainingCreditsLabel.setText("");
			creditsPanelConstraints.gridx = 1;
			creditsPanelConstraints.insets.right = 0;
			creditsPanel.add(remainingCreditsLabel, creditsPanelConstraints);
			gbc.gridy++;
			gbc.anchor = GridBagConstraints.NORTH;
			gbc.insets.bottom = 2;
			purchasePanel.add(creditsPanel, gbc);
			
			add(purchasePanel);
			showingLayers = true;
		}
		
		public void showEnableLayersButton() {
			if(showingLayers) {
				removeAll();
				add(enableButton);
				revalidate();
				repaint();
				showingLayers = false;
			}
		}
		
		public void showLayers() {
			if(!showingLayers) {
				removeAll();
				add(purchasePanel);
				revalidate();
				repaint();
				showingLayers = true;
			}
		}
		
		public void setRemainingCredits(double remainingCredits) {
			remainingCreditsLabel.setText("<html><b>" + creditsFormatter.format(remainingCredits)
					+ "</b></html>");
		}
	}
}