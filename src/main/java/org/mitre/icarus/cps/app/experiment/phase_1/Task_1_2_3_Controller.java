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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JRadioButton;

import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.AttackPresentationTrialState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.Task_1_ProbeTrialState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.Task_2_ProbeTrialState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.Task_3_ProbeTrialState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.TrialState_Phase1;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.ConfirmSettingsTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.GroupCentersProbeTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.GroupCirclesProbeTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.GroupProbeTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.ProbabilityProbeTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.ShowScoreTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.SurpriseProbeTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.TroopSelectionMultiGroupTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.GroupCirclesProbeTrialPartState.GroupCircleShape;
import org.mitre.icarus.cps.app.util.ProbabilityUtils;
import org.mitre.icarus.cps.app.widgets.WidgetConstants;
import org.mitre.icarus.cps.app.widgets.ImageManager.IconSize;
import org.mitre.icarus.cps.app.widgets.map.phase_1.MapConstants_Phase1;
import org.mitre.icarus.cps.app.widgets.map.phase_1.MapPanelContainer;
import org.mitre.icarus.cps.app.widgets.map.phase_1.MapPanelContainer.LayerType;
import org.mitre.icarus.cps.app.widgets.map.phase_1.PlacemarkFactory.AttackLocationType;
import org.mitre.icarus.cps.app.widgets.map.phase_1.objects.AttackLocationPlacemark;
import org.mitre.icarus.cps.app.widgets.map.phase_1.objects.GroupCenterPlacemark;
import org.mitre.icarus.cps.app.widgets.phase_1.ColorManager_Phase1;
import org.mitre.icarus.cps.app.widgets.phase_1.ImageManager_Phase1;
import org.mitre.icarus.cps.app.widgets.phase_1.experiment.GroupSelectionPanel;
import org.mitre.icarus.cps.app.widgets.probability_entry.IProbabilityEntryContainer;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.GroupCirclesProbeResponse.GroupCircle;
import org.mitre.icarus.cps.exam.phase_1.testing.AttackLocationPresentationTrial;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_1_2_3_PhaseBase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_1_2_3_ProbeTrialBase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_1_2_3_TrialBlockBase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_1_ProbeTrial;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_2_ProbeTrial;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_3_Phase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_3_ProbeTrial;
import org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.SurpriseReportProbe;
import org.mitre.icarus.cps.experiment_core.event.ConditionEvent;
import org.mitre.icarus.cps.experiment_core.event.SubjectActionEvent;
import org.mitre.icarus.cps.experiment_core.gui.INavButtonPanel.ButtonType;
import org.mitre.icarus.cps.experiment_core.subject_data.SubjectConditionData;
import org.mitre.icarus.cps.feature_vector.phase_1.GridLocation2D;
import org.mitre.icarus.cps.feature_vector.phase_1.GridSize;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupAttack;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupCenter;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupType;
import org.mitre.icarus.cps.feature_vector.phase_1.Road;

/**
 * Controller for Tasks 1, 2, and 3.
 * 
 * @author CBONACETO
 *
 */
public class Task_1_2_3_Controller extends TaskController<Task_1_2_3_PhaseBase<?>> implements ActionListener {	
	
	/** The trial states for each trial in the task */
	protected List<TrialState_Phase1> trialStates;
	
	/** The trial state for the current trial */
	protected TrialState_Phase1 currentTrialState;
	
	/** Whether we're at the beginning of a trial block */
	protected boolean blockStart;	
	
	/** The current trial block number */
	protected int blockNum;	
	
	/** The previous probability settings */
	protected List<Integer> previousSettings;
	
	/** The last attack location that was shown */
	protected AttackLocationPlacemark prevAttackLocation;
	
	/** The probe trial attack location */
	protected AttackLocationPlacemark probeAttackLocation;
	
	/** The group selection panel (used to select a group to send troops against) */
	protected GroupSelectionPanel groupSelectionPanel;
	
	/** The group circles for each group (Task 2) */
	protected ArrayList<GroupCircleShape> groupCircles;
	
	/** The group centers for each group (Task 3) */
	protected ArrayList<GroupCenterPlacemark> groupCenters;	
	
	protected boolean warnIfCirclesOrCentersNotEdited = false;
		
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment.phase_1.TaskController#initializeTaskController(org.mitre.icarus.cps.exam.phase_1.testing.TaskTestPhase<?>, org.mitre.icarus.cps.gui.phase_1.experiment.ConditionPanel_Phase1)
	 */
	@Override
	public void initializeTask(Task_1_2_3_PhaseBase<?> task) {
		this.task = task;		
		if(groupSelectionPanel == null) {
			groupSelectionPanel = new GroupSelectionPanel("groups");
		}
		
		//Show the S2 score for Tasks 1 - 3
		showScore = true;
		
		//Show the map scale for Task 3
		if(task instanceof Task_3_Phase) {
			conditionPanel.getMapPanel().setShowScale(true);
		}
		else {
			conditionPanel.getMapPanel().setShowScale(false);
		}
		
		locationsPresent.clear();
		groupCenterGroupsPresent.clear();
		
		//Create the trial states for each trial
		trialStates = new ArrayList<TrialState_Phase1>();
		if(task.getTrialBlocks() != null && !task.getTrialBlocks().isEmpty()) {
			int trialNum = 0;	
			for(Task_1_2_3_TrialBlockBase trialBlock : task.getTrialBlocks()) {				
				//Create the presentation trial states
				if(trialBlock.getGroupAttackPresentations() != null) {
					for(AttackLocationPresentationTrial attackPresentation : trialBlock.getGroupAttackPresentations()) {
						trialStates.add(new AttackPresentationTrialState(trialNum, attackPresentation));
						locationsPresent.add(attackPresentation.getGroupAttack());
						trialNum++;
					}					
				}				
				//Create the probe trial state			
				if(trialBlock.getProbeTrial() != null) {
					if(trialBlock.getProbeTrial() instanceof Task_1_ProbeTrial) {
						((Task_1_ProbeTrial)trialBlock.getProbeTrial()).setTrialResponse(null);
						trialStates.add(new Task_1_ProbeTrialState(trialNum, (Task_1_ProbeTrial)trialBlock.getProbeTrial(),
								exam.getNormalizationMode(), showScore));
					}
					else if(trialBlock.getProbeTrial() instanceof Task_2_ProbeTrial) {
						((Task_2_ProbeTrial)trialBlock.getProbeTrial()).setTrialResponse(null);
						trialStates.add(new Task_2_ProbeTrialState(trialNum, (Task_2_ProbeTrial)trialBlock.getProbeTrial(),
								exam.getNormalizationMode(), showScore));
					}
					else if (trialBlock.getProbeTrial() instanceof Task_3_ProbeTrial) {
						Task_3_ProbeTrial task3ProbeTrial = (Task_3_ProbeTrial)trialBlock.getProbeTrial();						
						task3ProbeTrial.setTrialResponse(null);						
						trialStates.add(new Task_3_ProbeTrialState(trialNum, task3ProbeTrial,
								exam.getNormalizationMode(), showScore));
						groupCenterGroupsPresent.addAll(task3ProbeTrial.getGroupCentersProbe().getGroups());
					}
					trialNum++;
				}
			}			
		}
		locationsPresent.add(new GroupAttack(GroupType.Unknown));
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment.phase_1.TaskController#getTaskWithResponseData()
	 */
	@Override
	public Task_1_2_3_PhaseBase<?> getTaskWithResponseData() {
		ArrayList<? extends Task_1_2_3_TrialBlockBase> trialBlocks = task.getTrialBlocks();
		int currentTrialBlock = -1;
		ArrayList<Long> attackPresentationTimes = null;
		if(trialBlocks != null && !trialBlocks.isEmpty()) {
			attackPresentationTimes = new ArrayList<Long>();
			currentTrialBlock = 0;
		}
		if(trialStates != null && !trialStates.isEmpty()) {
			for(TrialState_Phase1 trialState : trialStates) {
				trialState.updateTrialResponseData();
				if(currentTrialBlock >= 0 && currentTrialBlock < trialBlocks.size()) {
					if(trialState instanceof AttackPresentationTrialState) {
						attackPresentationTimes.add(trialState.getTrialTime_ms());
					}
					else {
						trialBlocks.get(currentTrialBlock).setGroupAttackPresentationTimes_ms(attackPresentationTimes);
						attackPresentationTimes = new ArrayList<Long>();
						currentTrialBlock++;
					}
				}
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
		prevAttackLocation = null;
		probeAttackLocation = null;
		MapPanelContainer mapPanel = conditionPanel.getMapPanel();
		mapPanel.resetMap();
		if(task instanceof Task_3_Phase) {
			mapPanel.setLayerInstructionsEnabled(true);
		}
		else {
			mapPanel.setLayerInstructionsEnabled(false);
		}
		
		if(task instanceof Task_3_Phase) {
			//Add the roads layer and roads to the map for Task 3
			mapPanel.setRoadLayerEnabled(true);
			mapPanel.setRoadsLegendItemVisible(true);		
			if(((Task_3_Phase) task).getRoads() != null) {
				mapPanel.setRoads(((Task_3_Phase) task).getRoads());
			}			
		}

		//Add the SIGACTs layer and to the map and call it "Attacks"
		mapPanel.setSigactLayerEnabled(true);
		mapPanel.setSigactsLayerAndLegendName("Attacks");
		
		//Add the SIGACTs legend items to the map for each group
		mapPanel.setSigactsLegendItemVisible(true);
		mapPanel.setSigactLocationsInLegend(locationsPresent);
		
		if(task instanceof Task_3_Phase) {
			//Add the group centers legend item to the map for Task 3
			mapPanel.setGroupCentersLegendItemVisible(true);
			mapPanel.setGroupCenterGroupsForLegend(groupCenterGroupsPresent);
			mapPanel.setGroupCentersLayerAndLegendName("Centers");
		}

		mapPanel.redrawMap();
		
		//Clear the task input panel
		conditionPanel.setTaskInputPanelVisible(true);
		taskInputPanel.setAllSubPanelsVisible(false);
		conditionPanel.showTaskScreen();
		
		//Load the first trial
		currentTrialState = null;
		currentTrialPart = null;
		blockStart = true;
		blockNum = 0;
		currentTrial = -1;
		trialPhase = -1;
		nextTrial();		
	}	
	
	@Override
	protected void performCleanup() {
		if(taskInputPanel.getSurpriseEntryComponent() != null) {
			taskInputPanel.getSurpriseEntryComponent().removeButtonActionListener(this);
		}
		if(groupSelectionPanel != null) {
			groupSelectionPanel.removeButtonActionListener(this);
		}
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
			} else if(currentTrialPart instanceof TroopSelectionMultiGroupTrialPartState) {
				//Validate that a group was selected
				GroupType selectedGroup = groupSelectionPanel.getSelectedGroup();
				if(selectedGroup == null) {
					responseValid = false;
				}
				else {
					TroopSelectionMultiGroupTrialPartState troopProbe = (TroopSelectionMultiGroupTrialPartState)currentTrialPart;
					troopProbe.setTroopSelectionGroup(groupSelectionPanel.getSelectedGroup());
					GroupType responsibleGroup = 
						((Task_1_2_3_ProbeTrialBase)currentTrialState.getTrial()).getGroundTruth().getResponsibleGroup();	
					if(examController.getScoreComputer() != null) {
						currentTrialState.setScore_s2(examController.getScoreComputer().computeTroopAllocationScoreS2_SingleGroupAllocation(
								troopProbe.getTroopSelectionGroup(), responsibleGroup));
					}
				}
			} else if(currentTrialPart instanceof GroupCirclesProbeTrialPartState) {
				//Warn if group circles haven't been edited and we haven't already shown a warning
				if(warnIfCirclesOrCentersNotEdited) {
					warnIfCirclesOrCentersNotEdited = false;
					boolean circlesEdited = false;
					for(GroupCircleShape groupCircle : groupCircles) {
						if(groupCircle.circle.isShapeEdited()) {
							circlesEdited = true;
						}
						groupCircle.circle.setShapeEdited(false);
					}
					if(!circlesEdited) {
						//Show warning message
						if(JOptionPane.showConfirmDialog(examController.getParentWindow(), 
								"<html><center>You have not edited the group circles.<br>" +
								"Would you like to continue anyway?</center></html>", 
								"", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION) {
							responseValid = false;
						}
					}
				}				
				if(responseValid) {
					conditionPanel.getMapPanel().setGroupCirclesLayerEditable(false);					
				}
			} else if(currentTrialPart instanceof GroupCentersProbeTrialPartState) {
				//Warn if group centers haven't been edited and we haven't already shown a warning
				if(warnIfCirclesOrCentersNotEdited) {
					warnIfCirclesOrCentersNotEdited = false;
					boolean centersEdited = false;
					for(GroupCenterPlacemark groupCenter : groupCenters) {
						if(groupCenter.isShapeEdited()) {
							centersEdited = true;
						}
						groupCenter.setShapeEdited(false);
					}
					if(!centersEdited) {
						//Show warning message
						if(JOptionPane.showConfirmDialog(examController.getParentWindow(), 
								"<html><center>You have not edited the group centers.<br>" +
								"Would you like to continue anyway?</center></html>", 
								"", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION) {
							responseValid = false;
						}
					}
				}
				
				if(responseValid) {
					//First validate that the group centers are on roads
					double radius = conditionPanel.getMapPanel().getPlacemarkSize_gridUnits()/2.d;
					GroupCentersProbeTrialPartState centersProbe = (GroupCentersProbeTrialPartState)currentTrialPart;
					ArrayList<Road> roads = ((Task_3_Phase) task).getRoads();
					LinkedList<GroupCenterPlacemark> invalidCenters = new LinkedList<GroupCenterPlacemark>();
					for(GroupCenterPlacemark center : centersProbe.getGroupCenters()) {
						boolean onRoad = false;
						for(Road road : roads) {
							GridLocation2D closestRoadPoint = road.getClosestPoint(center.getCenterLocation(), radius);
							if(closestRoadPoint != null) {
								//Make sure the group center is centered on the road if it was slightly off
								center.setCenterLocation(closestRoadPoint);
								onRoad = true;
								break;
							}							
						}
						if(!onRoad) {
							invalidCenters.add(center);
						}
					}		
					conditionPanel.getMapPanel().redrawMap();
					if(!invalidCenters.isEmpty()) {
						//One or more group centers is not on a road
						StringBuilder sb = createTrialNumberString(currentTrial+1, trialPhase+1, 
								currentTrialState.getNumTrialParts(), "Centers");
						if(invalidCenters.size() == 1) {
							sb.append("Please position the group center for ");
						}
						else {
							sb.append("Please position group centers for ");
						}
						int index = 0;
						for(GroupCenterPlacemark center : invalidCenters) {
							sb.append(" <b>Group " + 
									center.getFeature().getGroup().getGroupNameFull() + "</b>");						
							if(index < invalidCenters.size() - 1) {
								if(index == invalidCenters.size() - 2) {
									sb.append(", and");
								}
								else {
									sb.append(", ");
								}
							}						
							index++;
						}
						sb.append(" on a road.");
						conditionPanel.setInstructionBannerText(sb.toString());					
						responseValid = false;
					}
					else {
						conditionPanel.getMapPanel().setGroupCentersLayerEditable(false);
					}
				}
			} else if(currentTrialPart instanceof SurpriseProbeTrialPartState) {
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
				if(groupSelectionPanel != null) {
					groupSelectionPanel.removeButtonActionListener(this);
				}

				// Fire condition completed event
				fireConditionEvent(new ConditionEvent(ConditionEvent.CONDITION_COMPLETED, 
						currentTrial, this));				
				return;
			} else {			
				//Set the current trial
				currentTrialState = trialStates.get(currentTrial);
				currentTrialPart = null;							
			}
		}
		
		//Fire a trial changed event
		super.setTrial(currentTrial, trialPhase, currentTrialState.getNumTrialParts());

		//Show the current trial or trial part
		if(currentTrialState instanceof AttackPresentationTrialState) {
			GroupAttack groupAttack = ((AttackLocationPresentationTrial)currentTrialState.getTrial()).getGroupAttack();
			
			if(blockStart) {
				//Reset the map and hide the task input panel at the beginning of a block
				MapPanelContainer mapPanel = conditionPanel.getMapPanel();
				mapPanel.restoresLayerZOrder();
				mapPanel.setGroupCirclesLayerEditable(false);
				mapPanel.setGroupCentersLayerEditable(false);
				//Make group circles/centers semi-transparent
				if(groupCenters != null && !groupCenters.isEmpty()) {
					for(GroupCenterPlacemark groupCenter : groupCenters) {
						groupCenter.setTransparency(0.7f);
					}
				}
				if(groupCircles != null && !groupCircles.isEmpty()) {
					for(GroupCircleShape groupCircle : groupCircles) {
						groupCircle.circle.setTransparency(0.7f);
					}
				}
				mapPanel.redrawMap();
				
				probeAttackLocation = null;
				taskInputPanel.setAllSubPanelsVisible(false);			
				
				StringBuilder sb = createTrialNumberString(currentTrial+1, trialPhase+1, 
						currentTrialState.getNumTrialParts(), "Attack");				
				/*if(currentTrial > 0) {
					sb.append("A new attack block has begun, and an attack by <b>Group " + 
							groupAttack.getGroup().getGroupNameFull() + " </b>has occured. Click Next to continue.");					
				}
				else {*/
				sb.append("An attack by <b>Group " + 
						groupAttack.getGroup().getGroupNameFull() + " </b>has occured. Click Next to continue.");
				//}
				conditionPanel.setInstructionBannerText(sb.toString());
				blockStart = false;
				blockNum++;
			}
			else {
				StringBuilder sb = createTrialNumberString(currentTrial+1, trialPhase+1, 
						currentTrialState.getNumTrialParts(), "Attack");
				sb.append("An attack by <b>Group " + 
						groupAttack.getGroup().getGroupNameFull() + " </b>has occured. Click Next to continue.");
				conditionPanel.setInstructionBannerText(sb.toString());
			}
			
			//Append the attack location
			appendAttackLocation(groupAttack, false);			
		}
		else {
			blockStart = true;
			
			//Go to the next part of the probe trial
			conditionPanel.setTaskInputPanelVisible(true);
			
			currentTrialPart = currentTrialState.getTrialParts().get(trialPhase);
			
			if(currentTrialPart instanceof GroupProbeTrialPartState) {								
				GroupProbeTrialPartState groupProbe = (GroupProbeTrialPartState)currentTrialPart;
				
				//Append the unknown attack location
				if(probeAttackLocation == null) {
					appendAttackLocation(new GroupAttack(GroupType.Unknown, groupProbe.getProbe().getAttackLocation()), true);
					probeAttackLocation = prevAttackLocation;
				}
				
				//Show the group probe
				showGroupProbe(true, groupProbe.getProbe().getGroups(), groupProbe.getCurrentSettings(),
						groupProbe.getPreviousSettings());				
				StringBuilder sb = createTrialNumberString(currentTrial+1, trialPhase+1, 
						currentTrialState.getNumTrialParts(), "Group Probe");
				sb.append("An attack by an unknown group <b>(?)</b> has occurred. " +
						"Indicate the probability that each group is responsible for the attack, and click Next to continue.");
				conditionPanel.setInstructionBannerText(sb.toString());
			}
			else if(currentTrialPart instanceof TroopSelectionMultiGroupTrialPartState) {
				//Show the troop selection probe
				TroopSelectionMultiGroupTrialPartState troopProbe = (TroopSelectionMultiGroupTrialPartState)currentTrialPart;			
				showTroopSelectionProbe(troopProbe.getProbe().getGroups(), previousSettings, true);
				if(!groupSelectionPanel.isButtonActionListenerPresent(this)) {
					groupSelectionPanel.addButtonActionListener(this);
				}
				StringBuilder sb = createTrialNumberString(currentTrial+1, trialPhase+1, 
						currentTrialState.getNumTrialParts(), "Send Troops");
				sb.append("Using the radio buttons, allocate troops to one of the groups, and click Next to continue.");
				conditionPanel.setInstructionBannerText(sb.toString());			
			}			
			else if(currentTrialPart instanceof ConfirmSettingsTrialPartState) {
				//Confirm the normalized settings for the group probe
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
				conditionPanel.setInstructionBannerText(sb.toString());				
				examController.setNavButtonEnabled(ButtonType.Back, true);				
			}
			else if(currentTrialPart instanceof GroupCirclesProbeTrialPartState) {
				//Show the group circles probe
				//Un-highlight the previous location
				if(prevAttackLocation != null && prevAttackLocation.isHighlighted()) {
					prevAttackLocation.setShowMarker(false);
					prevAttackLocation.setHighlighted(false);
				}
				warnIfCirclesOrCentersNotEdited = true;
				taskInputPanel.getGroupProbeComponent().showConfirmedProbabilities();				
				GroupCirclesProbeTrialPartState circlesProbe = (GroupCirclesProbeTrialPartState)currentTrialPart;
				if(circlesProbe.getProbe() != null) {
					showGroupCirclesProbe(circlesProbe);
				}
				StringBuilder sb = createTrialNumberString(currentTrial+1, trialPhase+1, 
						currentTrialState.getNumTrialParts(), "Circles");
				sb.append("Indicate your judgment of each group's spread by drawing a circle that captures the 2-to-1 boundary of attack. " +
						"Each group�s circle should enclose an area containing 2/3 of the attacks by that group; " +
						"1/3 of the attacks will be outside the circle. To move a circle, click and drag the middle point. " +
						"To resize a circle, click and drag the edge. Circles may overlap, and their edge may be placed off the screen. " +
						"Click Next to continue when you are ready.");				
				//sb.append("Draw the 2-to-1 circle boundary for each group by clicking and dragging each" +
				//		" group circle.  Mouse over a circle and click and drag the middle point to move the circle.  Click and drag the circle " +
				//		"edge to resize the circle.");
				conditionPanel.setInstructionBannerText(sb.toString());				
			}
			else if(currentTrialPart instanceof GroupCentersProbeTrialPartState) {
				//Show the group centers probe
				//showGroupProbe();
				//Un-highlight the previous location
				if(prevAttackLocation != null && prevAttackLocation.isHighlighted()) {
					prevAttackLocation.setShowMarker(false);
					prevAttackLocation.setHighlighted(false);
				}
				warnIfCirclesOrCentersNotEdited = true;
				taskInputPanel.getGroupProbeComponent().showConfirmedProbabilities();				
				GroupCentersProbeTrialPartState centersProbe = (GroupCentersProbeTrialPartState)currentTrialPart;
				if(centersProbe.getProbe() != null) {
					showGroupCentersProbe(centersProbe);
				}
				StringBuilder sb = createTrialNumberString(currentTrial+1, trialPhase+1, 
						currentTrialState.getNumTrialParts(), "Centers");
				sb.append("Locate the center of gravity for each group by clicking and dragging each" +
					" group center. The group centers of gravity should be positioned on a road. " +
					"Click Next to continue when you are ready.");
				conditionPanel.setInstructionBannerText(sb.toString());
			}						
			else if(currentTrialPart instanceof SurpriseProbeTrialPartState) {
				//Stop editing any group circles or centers
				conditionPanel.getMapPanel().setGroupCirclesLayerEditable(false);
				conditionPanel.getMapPanel().setGroupCentersLayerEditable(false);				
				
				//Show the group responsible for the attack
				SurpriseProbeTrialPartState surpriseProbe = (SurpriseProbeTrialPartState)currentTrialPart;
				GroupType group = null;
				if(surpriseProbe.getGroundTruth() != null && surpriseProbe.getGroundTruth().getResponsibleGroup() != null 
						&& probeAttackLocation != null) {
					group = surpriseProbe.getGroundTruth().getResponsibleGroup();
					probeAttackLocation.setName(group.toString());
					Color color = ColorManager_Phase1.getGroupCenterColor(group);
					probeAttackLocation.setBorderColor(color);
					probeAttackLocation.setForegroundColor(color);
					if(WidgetConstants.USE_GROUP_SYMBOLS) {
						probeAttackLocation.setMarkerIcon(ImageManager_Phase1.getGroupSymbolImage(group, IconSize.Small));
						probeAttackLocation.setShowName(false);
					}
					conditionPanel.getMapPanel().redrawMap();
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
					showSurpriseProbe(true, true, true);
				}
				
				StringBuilder sb = createTrialNumberString(currentTrial+1, trialPhase+1, 
						currentTrialState.getNumTrialParts(), "Surprise");
				if(group != null) {
					sb.append("<b>Group " + group.getGroupNameFull() + "</b> was responsible for the attack. " +
							"Indicate your surprise below by selecting a radio button and click Next to continue.");
				}
				else {
					sb.append("Indicate your surprise below by selecting a radio button and click Next to continue.");
				}
				conditionPanel.setInstructionBannerText(sb.toString());
			}
			else if(currentTrialPart instanceof ShowScoreTrialPartState) {
				//Show the score (S2) for the probe trial
				TrialState_Phase1 trial = ((ShowScoreTrialPartState)currentTrialPart).getTrial();
				long score = 0;
				if(trial.getScore_s2() != null) {
					score = Math.round(trial.getScore_s2());
				}
				StringBuilder sb = createTrialNumberString(currentTrial+1, trialPhase+1, 
						currentTrialState.getNumTrialParts(), "Score");
				sb.append("You received<b> " + score + " </b>out of 100 points on this trial for " +
					"allocating troops. Click Next to continue.");
				conditionPanel.setInstructionBannerText(sb.toString());			
			}
		}

		//Pause before allowing a next button press
		if(currentTrialPart instanceof GroupProbeTrialPartState) {
			if(!adjustingNormalizedSettings) {
				//Increase pause length when we start the probe trial
				pauseBeforeNextTrial(2000);
			}
			else {
				//Pause for the standard amount of time
				pauseBeforeNextTrial();
			}
		}
		else if(currentTrialPart instanceof TroopSelectionMultiGroupTrialPartState) {
			//Disable next button until group is selected 
			examController.setNavButtonEnabled(ButtonType.Next, false);
		}
		else if(currentTrialPart instanceof SurpriseProbeTrialPartState) {
			//Disable next button until surprise value is entered 
			examController.setNavButtonEnabled(ButtonType.Next, false);			
		}
		else {
			//Pause for the standard amount of time
			pauseBeforeNextTrial();
		}		
	}	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() instanceof JRadioButton) {			
			//IProbabilityEntryContainer troopComponent = taskInputPanel.getTroopAllocationMultiGroupComponent();
			IProbabilityEntryContainer troopComponent = taskInputPanel.getPreviousTroopAllocationComponent();
			List<Integer> troopAllocations = troopComponent.getCurrentSettings();
			GroupType selectedGroup = groupSelectionPanel.getSelectedGroup();			
			int selectedGroupIndex = -1;			
			if(selectedGroup != null) {
				selectedGroupIndex = groupSelectionPanel.getGroups().indexOf(selectedGroup);
			}
			for(int i=0; i<troopAllocations.size(); i++) {
				if(i == selectedGroupIndex) {
					troopAllocations.set(i, 100);
				}
				else {
					troopAllocations.set(i, 0);
				}
			}								
			troopComponent.setCurrentSettings(troopAllocations);
		}
		
		examController.setNavButtonEnabled(ButtonType.Next, true);
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
	
	/**
	 * Append a SIGACT attack location to the map
	 * 
	 * @param currAttackLocation
	 */
	protected void appendAttackLocation(GroupAttack currAttackLocation, boolean putAttacksLayerOnTop) {		
		//Un-highlight the previous location
		if(prevAttackLocation != null && prevAttackLocation.isHighlighted()) {
			prevAttackLocation.setShowMarker(false);
			prevAttackLocation.setHighlighted(false);
		}
		
		//Append the new location		
		prevAttackLocation = conditionPanel.getMapPanel().addSigactLocation(
				currAttackLocation, AttackLocationType.GROUP_ATTACK, true, false);

		//Add a legend item for the group if it hasn't been added yet
		/*if(currAttackLocation.getGroup() != null && !groupsShown.containsKey(currAttackLocation.getGroup())) {
			groupsShown.put(currAttackLocation.getGroup(), currAttackLocation);
			conditionPanel.getMapPanel().setSigactLocationsInLegend(groupsShown.values());			
		}*/
		
		//Make the attacks layer the last layer in the z-order
		if(putAttacksLayerOnTop) {
			conditionPanel.getMapPanel().moveLayerToBottomOfZOrder(LayerType.SIGACTS);
		}				

		conditionPanel.getMapPanel().redrawMap();		
	}
	
	protected void showGroupProbe() {
		showGroupProbe(false, null, null, null);
	}
	
	protected void showGroupProbe(boolean settingsChanged, List<GroupType> groups, List<Integer> currentSettings,
			List<Integer> previousSettings) {
		if(settingsChanged) {
			taskInputPanel.setGroupsForGroupProbeComponent(groups);
			taskInputPanel.getGroupProbeComponent().setCurrentSettings(currentSettings);
			taskInputPanel.getGroupProbeComponent().setPreviousSettings(previousSettings);
		}
		if(!adjustingNormalizedSettings) {
			//System.out.println("resetting interaction times");
			taskInputPanel.getGroupProbeComponent().resetInteractionTimes();
		}
		taskInputPanel.setGroupsForPreviousSettingsComponent(groups);
		taskInputPanel.getGroupProbeComponent().setTopTitle("your probabilities");		
		taskInputPanel.getGroupProbeComponent().showEditableProbabilities();
		taskInputPanel.getGroupProbeComponent().setSumVisible(true);
		taskInputPanel.setSubPanelComponent(0, taskInputPanel.getGroupProbeComponent());
		taskInputPanel.setSubPanelVisible(0, true);
		taskInputPanel.setSubPanelVisible(1, false);
		taskInputPanel.setSubPanelVisible(2, false);
		taskInputPanel.setSubPanelVisible(3, false);
	}
	
	protected void showTroopSelectionProbe(List<GroupType> groups, List<Integer> previousSettings, boolean showPreviousSettings) {
		groupSelectionPanel.setGroups(groups);
		taskInputPanel.setSubPanelComponent(0, groupSelectionPanel, BorderLayout.CENTER);				
		
		/*taskInputPanel.setGroupsForTroopAllocationMultiGroupComponent(groups);
		taskInputPanel.getTroopAllocationMultiGroupComponent().setCurrentSettings(
				CPSUtils.createProbabilities(groups.size(), 0));
		taskInputPanel.getTroopAllocationMultiGroupComponent().showConfirmedProbabilities();
		taskInputPanel.getTroopAllocationMultiGroupComponent().setSumVisible(false);
		taskInputPanel.setSubPanelComponent(1, taskInputPanel.getTroopAllocationMultiGroupComponent());*/		
		taskInputPanel.setGroupsForPreviousTroopAllocationComponent(groups);
		taskInputPanel.getPreviousTroopAllocationComponent().setCurrentSettings(
				ProbabilityUtils.createProbabilities(groups.size(), 0));
		taskInputPanel.setSubPanelComponent(1, taskInputPanel.getPreviousTroopAllocationComponent());
		
		if(showPreviousSettings) {
			taskInputPanel.getPreviousSettingsComponent().setTopTitle("your probabilities");
			taskInputPanel.getPreviousSettingsComponent().setCurrentSettings(previousSettings);
			taskInputPanel.getPreviousSettingsComponent().showConfirmedProbabilities();
			taskInputPanel.setSubPanelComponent(2, taskInputPanel.getPreviousSettingsComponent());
		}
		
		taskInputPanel.setSubPanelVisible(0, true);
		taskInputPanel.setSubPanelVisible(1, true);
		taskInputPanel.setSubPanelVisible(2, showPreviousSettings);
		taskInputPanel.setSubPanelVisible(3, false);
	}	
	
	protected void showGroupCirclesProbe(GroupCirclesProbeTrialPartState circlesProbe) {		
		ArrayList<GroupType> groups = null;
		if(circlesProbe.getProbe() != null) {
			groups = circlesProbe.getProbe().getGroups();
		}
		
		//Add the group circles layer
		MapPanelContainer mapPanel = conditionPanel.getMapPanel();
		mapPanel.setGroupCirclesLayerEnabled(true);
		mapPanel.setGroupCirclesLayerEditable(true);
		
		//Add initial group circles for each group at the bottom of the map
		if(blockNum == 1) {
			if(groups != null && !groups.isEmpty()) {
				mapPanel.removeAllGroupCircles();
				groupCircles = new ArrayList<GroupCircleShape>(groups.size());
				GridSize gridSize = mapPanel.getGridSize();
				double radius = gridSize.getGridWidth() / 20;
				double diameter = radius * 2;			
				double spacing = 3;
				double xPos = (gridSize.getGridWidth() - (groups.size() * diameter + (groups.size() - 1) * spacing)) / 2 + radius;
				double yPos = radius + (spacing * 2);
				for(GroupType group : groups) {
					GroupCircle groupCircle = new GroupCircle(group, 
							new GridLocation2D(xPos, yPos), (double)radius);
					groupCircles.add(new GroupCircleShape(group, 
							mapPanel.addGroupCircle(groupCircle, true)));
					xPos += diameter + spacing;
				}
			}
		}
		else {
			//Clone the current group circles, and reset the edited state and edit time for each circle
			mapPanel.removeAllGroupCircles();
			ArrayList<GroupCircleShape> groupCirclesCopy = new ArrayList<GroupCircleShape>(groupCircles.size());
			for(GroupCircleShape groupCircle : groupCircles) {
				GroupCircle groupCircleCopy = new GroupCircle(groupCircle.getGroup(), 
						new GridLocation2D(groupCircle.circle.getCenterLocation()), 
						groupCircle.circle.getRadius());
				groupCirclesCopy.add(new GroupCircleShape(groupCircle.getGroup(), 
						mapPanel.addGroupCircle(groupCircleCopy, true)));
				//groupCircle.circle.setShapeEdited(false);
				//groupCircle.circle.setEditTime(0);
				//groupCircle.circle.setTransparency(1.f);
			}
			groupCircles = groupCirclesCopy;
		}
		circlesProbe.setGroupCircles(groupCircles);
		mapPanel.redrawMap();
	}
	
	protected void showGroupCentersProbe(GroupCentersProbeTrialPartState centersProbe) {
		ArrayList<GroupType> groups = null;
		if(centersProbe.getProbe() != null) {
			groups = centersProbe.getProbe().getGroups();
		}		
		
		//Add the group centers layer and legend items
		MapPanelContainer mapPanel = conditionPanel.getMapPanel();
		mapPanel.setGroupCentersLayerEnabled(true);
		mapPanel.setGroupCentersLayerEditable(true);
		//mapPanel.setGroupCentersLegendItemVisible(true);
		//mapPanel.setGroupCenterGroupsForLegend(groups);
		
		//Add initial group centers for each group at the bottom of the map
		if(blockNum == 1) {
			mapPanel.removeAllGroupCenters();
			if(groups != null && !groups.isEmpty()) {
				groupCenters = new ArrayList<GroupCenterPlacemark>(groups.size());
				GridSize gridSize = mapPanel.getGridSize();
				double diameter = (int)mapPanel.translateToGridUnits(MapConstants_Phase1.PLACEMARK_SIZE_PIXELS);
				double radius = diameter / 2;						
				double spacing = 3;
				double xPos = (gridSize.getGridWidth() - (groups.size() * diameter + (groups.size() - 1) * spacing)) / 2 + radius;
				double yPos = radius + (spacing * 2);
				for(GroupType group : groups) {
					GroupCenter groupCenter = new GroupCenter(group, new GridLocation2D(xPos, yPos));
					groupCenters.add(mapPanel.addGroupCenter(groupCenter, false, true));
					xPos += diameter + spacing;
				}				
			}
		}
		else {
			//Clone the current group centers, and reset the edited state and edit time for each center
			mapPanel.removeAllGroupCenters();
			ArrayList<GroupCenterPlacemark> groupCentersCopy = new ArrayList<GroupCenterPlacemark>(groupCenters.size());
			for(GroupCenterPlacemark groupCenter : groupCenters) {
				GroupCenter groupCenterCopy = new GroupCenter(groupCenter.getFeature().getGroup(), 
						new GridLocation2D(groupCenter.getCenterLocation()));
				groupCentersCopy.add(mapPanel.addGroupCenter(groupCenterCopy, false, true));
				//groupCenter.setShapeEdited(false);
				//groupCenter.setEditTime(0);
				//groupCenter.setTransparency(1.f);
			}
			groupCenters = groupCentersCopy;
		}
		centersProbe.setGroupCenters(groupCenters);
		mapPanel.redrawMap();
	}
	
	protected void showSurpriseProbe(boolean showTroopAllocation, boolean showPreviousSettings,
			boolean showPreviousSettinsAsFinalSettings) {
		taskInputPanel.setSubPanelComponent(0, taskInputPanel.getSurpriseEntryComponent());
		taskInputPanel.getSurpriseEntryComponent().setEnabled(true);
		
		if(showTroopAllocation) {			
			/*taskInputPanel.getTroopAllocationMultiGroupComponent().showConfirmedProbabilities();
			taskInputPanel.getTroopAllocationMultiGroupComponent().setSumVisible(false);
			taskInputPanel.setSubPanelComponent(1, taskInputPanel.getTroopAllocationMultiGroupComponent());*/
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