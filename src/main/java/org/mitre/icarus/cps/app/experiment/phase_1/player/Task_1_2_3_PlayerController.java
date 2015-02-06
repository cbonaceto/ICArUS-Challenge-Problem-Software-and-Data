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
import java.util.LinkedList;
import java.util.List;

import org.mitre.icarus.cps.app.experiment.phase_1.player.trial_states.Task_1_2_3_PlayerProbeTrialStateBase;
import org.mitre.icarus.cps.app.experiment.phase_1.player.trial_states.Task_1_PlayerProbeTrialState;
import org.mitre.icarus.cps.app.experiment.phase_1.player.trial_states.Task_2_PlayerProbeTrialState;
import org.mitre.icarus.cps.app.experiment.phase_1.player.trial_states.Task_3_PlayerProbeTrialState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.GroupCentersProbeTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.GroupCirclesProbeTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.GroupProbeTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.SurpriseProbeTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states.TroopAllocationMultiGroupTrialPartState;
import org.mitre.icarus.cps.app.widgets.WidgetConstants;
import org.mitre.icarus.cps.app.widgets.ImageManager.IconSize;
import org.mitre.icarus.cps.app.widgets.map.LineStyle;
import org.mitre.icarus.cps.app.widgets.map.phase_1.MapPanelContainer;
import org.mitre.icarus.cps.app.widgets.map.phase_1.MapPanelContainer.LayerType;
import org.mitre.icarus.cps.app.widgets.map.phase_1.PlacemarkFactory.AttackLocationType;
import org.mitre.icarus.cps.app.widgets.map.phase_1.objects.AttackLocationPlacemark;
import org.mitre.icarus.cps.app.widgets.map.phase_1.objects.CircleShape;
import org.mitre.icarus.cps.app.widgets.map.phase_1.objects.GroupCenterPlacemark;
import org.mitre.icarus.cps.app.widgets.phase_1.ColorManager_Phase1;
import org.mitre.icarus.cps.app.widgets.phase_1.ImageManager_Phase1;
import org.mitre.icarus.cps.app.window.phase_1.PlayerUtils;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.CPAMetric;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.TaskMetrics;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.TrialMetrics;
import org.mitre.icarus.cps.exam.phase_1.playback.TaskResponseData_Phase1;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.GroupCirclesProbeResponse.GroupCircle;
import org.mitre.icarus.cps.exam.phase_1.testing.AttackLocationPresentationTrial;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_1_2_3_PhaseBase;
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
import org.mitre.icarus.cps.feature_vector.phase_1.GroupAttack;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupCenter;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupType;
import org.mitre.icarus.cps.feature_vector.phase_1.Road;

/**
 * @author CBONACETO
 *
 */
public class Task_1_2_3_PlayerController extends PlayerTaskController<Task_1_2_3_PhaseBase<?>> {
	/** Dotted line style for drawing average human circles */
	public static final LineStyle DASHED_LINE = new LineStyle(1.f, LineStyle.CAP_SQUARE, LineStyle.JOIN_MITER, 
			10.f, new float[] {2.f}, 0.f);
	
	/** Dotted line style for drawing average human centers */
	public static final LineStyle DASHED_LINE_BOLD = new LineStyle(2.f, LineStyle.CAP_SQUARE, LineStyle.JOIN_MITER, 
			10.f, new float[] {5.f}, 0.f);
	
	/** The trial states for each trial in the task */
	protected List<Task_1_2_3_PlayerProbeTrialStateBase> trialStates;
	
	/** The trial state for the current trial */
	protected Task_1_2_3_PlayerProbeTrialStateBase currentTrialState;		
	
	/** The probe trial attack location */
	protected AttackLocationPlacemark probeAttackLocation;	
	
	protected TaskMetrics participantTaskMetrics;
	
	protected TaskMetrics avgHumanTaskMetrics;
	
	protected boolean groupCirclesCentersShown = false;
	
	/** Whether to show the average human circles and centers on the map (the default is true) */
	protected boolean showAverageHumanCirclesCenters = true;
		
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment.phase_1.TaskController#initializeTaskController(org.mitre.icarus.cps.exam.phase_1.testing.TaskTestPhase, org.mitre.icarus.cps.gui.phase_1.experiment.ConditionPanel_Phase1)
	 */
	@Override
	public void initializeTask(TaskResponseData_Phase1<Task_1_2_3_PhaseBase<?>> taskResponse) {
		if(conditionRunning) {
			stopCondition();
		}		
		this.taskResponse = taskResponse;
		this.task = taskResponse.getTestPhase();		
		
		//Show the S2 score for Tasks 1 - 3
		showScore = true;
		
		//Show the map scale for Task 3
		ArrayList<Road> roads = null;				
		if(task instanceof Task_3_Phase) {
			conditionPanel.getMapPanel().setShowScale(true);
			roads = ((Task_3_Phase)task).getRoads();
		}
		else {
			conditionPanel.getMapPanel().setShowScale(false);
		}
		
		locationsPresent.clear();
		groupCenterGroupsPresent.clear();
		
		//Create the trial states for each trial		
		trialStates = new ArrayList<Task_1_2_3_PlayerProbeTrialStateBase>();
		List<GroupAttack> groupAttacks = null;
		//List<Integer> previousSettings = ProbabilityUtils.;
		if(task.getTrialBlocks() != null && !task.getTrialBlocks().isEmpty()) {
			int trialIndex = 0;			
			for(Task_1_2_3_TrialBlockBase trialBlock : task.getTrialBlocks()) {
				if(trialIndex  > 0) {
					groupAttacks = new LinkedList<GroupAttack>(groupAttacks);
				} else {
					groupAttacks = new LinkedList<GroupAttack>();
				}
				//Add the group attack locations to the list of group attacks
				if(trialBlock.getGroupAttackPresentations() != null) {
					for(AttackLocationPresentationTrial attackPresentation : trialBlock.getGroupAttackPresentations()) {
						groupAttacks.add(attackPresentation.getGroupAttack());
						locationsPresent.add(attackPresentation.getGroupAttack());
					}					
				}
				//Create the probe trial state			
				if(trialBlock.getProbeTrial() != null) {
					int trialNum = trialBlock.getProbeTrial().getTrialNum();
					if(trialBlock.getProbeTrial() instanceof Task_1_ProbeTrial) {						
						trialStates.add(new Task_1_PlayerProbeTrialState(trialNum, (Task_1_ProbeTrial)trialBlock.getProbeTrial(),
								taskResponse.getParticipantTrialData(trialIndex), taskResponse.getAvgHumanTrialData(trialIndex),
								groupAttacks, null));
					}
					else if(trialBlock.getProbeTrial() instanceof Task_2_ProbeTrial) {						
						trialStates.add(new Task_2_PlayerProbeTrialState(trialNum, (Task_2_ProbeTrial)trialBlock.getProbeTrial(),
								taskResponse.getParticipantTrialData(trialIndex), taskResponse.getAvgHumanTrialData(trialIndex),
								groupAttacks, null));
					}
					else if (trialBlock.getProbeTrial() instanceof Task_3_ProbeTrial) {
						Task_3_ProbeTrial task3ProbeTrial = (Task_3_ProbeTrial)trialBlock.getProbeTrial();
						Task_3_PlayerProbeTrialState trialState = new Task_3_PlayerProbeTrialState(trialNum, task3ProbeTrial,
								taskResponse.getParticipantTrialData(trialIndex), taskResponse.getAvgHumanTrialData(trialIndex),
								groupAttacks, null);
						trialStates.add(trialState);
						groupCenterGroupsPresent.addAll(task3ProbeTrial.getGroupCentersProbe().getGroups());
						//Make sure the average group centers are on roads
						if(roads != null && !roads.isEmpty() && trialState.getGroupCentersProbe() != null && 
								trialState.getGroupCentersProbe().getAvgHumanGroupCenters() != null &&
								!trialState.getGroupCentersProbe().getAvgHumanGroupCenters().isEmpty()) {
							for(GroupCenter groupCenter : trialState.getGroupCentersProbe().getAvgHumanGroupCenters()) {
								boolean roadPointFound = false;
								int numTries = 1;
								double searchRadius = 5;
								while(!roadPointFound && numTries < 4) {
									for(Road road : roads) {
										GridLocation2D closestRoadPoint = road.getClosestPoint(groupCenter.getLocation(), searchRadius);
										if(closestRoadPoint != null) {
											roadPointFound = true;
											groupCenter.setLocation(closestRoadPoint);						
											break;
										}							
									}
									numTries++;
									searchRadius = 5 * numTries;
								}
							}
						}
					}
					groupAttacks.add(new GroupAttack(trialBlock.getProbeTrial().getGroundTruth().getResponsibleGroup(),
							trialBlock.getProbeTrial().getAttackLocationProbe().getAttackLocation()));
					trialIndex++;
				}
			}			
		}
		locationsPresent.add(new GroupAttack(GroupType.Unknown));
	}	
	
	@Override
	public void trialResponseChanged(Integer trialNum) {
		//Update the trial state for the trial whose response changed		
		//System.out.println("Updating trial response for trial: " + trialNum);
		if(trialNum != null && trialStates != null && !trialStates.isEmpty()) {			
			int trialIndex = 0;
			for(Task_1_2_3_PlayerProbeTrialStateBase trialState : trialStates) {
				if(trialState.getTrial().getTrialNum() == trialNum) {		
					//System.out.println("Updated trial: " + trialNum);
					trialState.updateTrialData(taskResponse.getParticipantTrialData(trialIndex),
							taskResponse.getAvgHumanTrialData(trialIndex));
					break;
				} else {
					trialIndex++;
				}
			}
		}			
	}

	public boolean isShowAverageHumanCirclesCenters() {
		return showAverageHumanCirclesCenters;
	}

	public void setShowAverageHumanCirclesCenters(boolean showAverageHumanCirclesCenters) {
		this.showAverageHumanCirclesCenters = showAverageHumanCirclesCenters;
	}

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
		groundTruthRevealed = false;
		groupCirclesCentersShown = false;
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
						currentTrialState.getTrialNumber()-1, this));				
				return;
			} else {
				currentTrialState = null;
			}
		}			
				
		if(currentTrialState == null) {
			//Initialize the current trial
			currentTrialState = trialStates.get(currentTrial);
			currentTrialPart = null;			
			probeAttackLocation = null;				
			groundTruthRevealed = false;
			groupCirclesCentersShown = false;

			//Reset the map
			MapPanelContainer mapPanel = conditionPanel.getMapPanel();
			mapPanel.restoresLayerZOrder();				
			mapPanel.removeAllSigactLocations();
			mapPanel.removeAllGroupCircles();
			mapPanel.removeAllGroupCenters();

			//Add all of the attack locations that have been observed to the map
			appendAttackLocations(((Task_1_2_3_PlayerProbeTrialStateBase)currentTrialState).getGroupAttacks());				
			mapPanel.redrawMap();
		}
		
		//Fire a trial changed event		
		super.setTrial(currentTrialState.getTrialNumber()-1, trialPhase, currentTrialState.getNumTrialParts());

		//Show the next part of the probe trial
		conditionPanel.setTaskResponsePanelVisible(true);

		currentTrialPart = currentTrialState.getTrialParts().get(trialPhase);	

		if(currentTrialPart instanceof GroupProbeTrialPartState) {
			GroupProbeTrialPartState groupProbe = (GroupProbeTrialPartState)currentTrialPart;
		
			if(probeAttackLocation == null) {
				//Add the probe attack location to the map
				appendProbeAttackLocation(new GroupAttack(GroupType.Unknown, groupProbe.getProbe().getAttackLocation()));
			} else if(groundTruthRevealed) {
				//Make sure ground truth isn't revealed
				removeProbeAttackLocation();
				appendProbeAttackLocation(new GroupAttack(GroupType.Unknown, groupProbe.getProbe().getAttackLocation()));
				groundTruthRevealed = false;
			}
			
			//Make sure the group circles or centers are shown
			if(!groupCirclesCentersShown) {
				if(currentTrialState instanceof Task_2_PlayerProbeTrialState) {
					showGroupCirclesProbe(((Task_2_PlayerProbeTrialState)currentTrialState).getGroupCirclesProbe());
				} else if(currentTrialState instanceof Task_3_PlayerProbeTrialState) {
					showGroupCentersProbe(((Task_3_PlayerProbeTrialState)currentTrialState).getGroupCentersProbe());
				}
			}

			//Show the group probe
			showGroupProbe(groupProbe);				
			StringBuilder sb = createTrialNumberString(currentTrial+1, trialPhase+1, 
					currentTrialState.getNumTrialParts(), "Group Probe", currentTrialState.isTrialComplete());
			if(trialPhase == 0) {
				sb.append("A new trial has started. ");
			}
			sb.append("An attack by an unknown group <b>(?)</b> has occurred, " +
					"and the " + participantLabel + " has indicated the probability that each group is responsible for the attack.");
			if(groupProbe.isNormativeSettingsPresent()) {
				if(groupProbe.isAvgHumanSettingsPresent()) {
					sb.append(" The normative probabilities and the average human probabilities are also shown.");
				} else {
					sb.append(" The normative probabilities are also shown.");
				}
			} else if(groupProbe.isAvgHumanSettingsPresent()) {
				sb.append(" The average human probabilities are also shown.");
			}
			if(showScore && currentTrialState.getParticipantTrialData() != null && 
					currentTrialState.getParticipantTrialData().getMetrics() != null) {
				TrialMetrics metrics = currentTrialState.getParticipantTrialData().getMetrics();				
				if(taskResponse.getMetricsInfo().getRSR_info().isAssessedForTask(task.getConditionNum()) 
						&& metrics.getRSR() != null && !metrics.getRSR().isEmpty()) {
					CPAMetric rsr = metrics.getRSR().get(0);
					if(rsr != null && rsr.score != null) {
						sb.append(" The <b>RSR</b> score for this trial is <b>" + PlayerUtils.formatDoubleAsString(rsr.score) + "%</b>");
						if(!rsr.assessed) {
							sb.append(", but RSR was not assessed for this trial.");
						} else {
							sb.append(".");
						}
					}
				}
				if(taskResponse.getMetricsInfo().getRR_info().isAssessedForTask(task.getConditionNum()) 
						&& metrics.getRR() != null) {
					create_RR_AssessmentString(sb, metrics, 
							currentTrialState.getAvgHumanTrialData() != null ? currentTrialState.getAvgHumanTrialData().getMetrics() : null);
				}
			}
			conditionPanel.setInstructionBannerText(sb.toString());
		}
		else if(currentTrialPart instanceof TroopAllocationMultiGroupTrialPartState) {
			//Show the troop selection probe
			TroopAllocationMultiGroupTrialPartState troopProbe = (TroopAllocationMultiGroupTrialPartState)currentTrialPart;	
			
			//Make sure ground truth isn't revealed 
			if(groundTruthRevealed) {
				//Make sure ground truth isn't revealed
				removeProbeAttackLocation();
				appendProbeAttackLocation(new GroupAttack(GroupType.Unknown, currentTrialState.getGroupProbe().getProbe().getAttackLocation()));
				groundTruthRevealed = false;
			}
			
			//Make sure the group circles or centers are shown
			if(!groupCirclesCentersShown) {
				if(currentTrialState instanceof Task_2_PlayerProbeTrialState) {
					showGroupCirclesProbe(((Task_2_PlayerProbeTrialState)currentTrialState).getGroupCirclesProbe());
				} else if(currentTrialState instanceof Task_3_PlayerProbeTrialState) {
					showGroupCentersProbe(((Task_3_PlayerProbeTrialState)currentTrialState).getGroupCentersProbe());
				}
			}
					
			showTroopSelectionProbe(currentTrialState.getGroupProbe().getProbe().getGroups(),
					currentTrialState.getGroupProbe().getCurrentSettings(), true, troopProbe);			
			StringBuilder sb = createTrialNumberString(currentTrial+1, trialPhase+1, 
					currentTrialState.getNumTrialParts(), "Send Troops", currentTrialState.isTrialComplete());
			sb.append("The " + participantLabel + " has allocated troops to each group. The " + participantLabel + "'s probabilities");
			if(troopProbe.isNormativeSettingsPresent()) {
				if(troopProbe.isAvgHumanSettingsPresent()) {
					sb.append(", the normative troop allocations, " +
							"and the average human troop allocations are also shown.");
				} else {
					sb.append(" and the normative troop allocations are also shown.");
				}
			} else if(troopProbe.isAvgHumanSettingsPresent()) {
				sb.append(" and the average human troop allocations are also shown.");
			} else {
				sb.append(" are also shown.");
			}
			conditionPanel.setInstructionBannerText(sb.toString());			
		} else if(currentTrialPart instanceof GroupCirclesProbeTrialPartState) {
			//Show the group circles probe	
			GroupCirclesProbeTrialPartState circlesProbe = (GroupCirclesProbeTrialPartState)currentTrialPart;
			
			//Remove the probe attack location from the map if it's present
			removeProbeAttackLocation();						
						
			showGroupCirclesProbe(circlesProbe);
			StringBuilder sb = createTrialNumberString(currentTrial+1, trialPhase+1, 
					currentTrialState.getNumTrialParts(), "Circles", currentTrialState.isTrialComplete());
			if(trialPhase == 0) {
				sb.append("A new trial has started. ");
			}
			sb.append("The " + participantLabel + " has been shown the below sequence of attacks and has drawn circles that encompass " +
					"the 2-to-1 attack boundary for each group. The circles should encompass an area enclosing 2/3 of the attacks by each group.");
			if(showAverageHumanCirclesCenters && circlesProbe.isAvgHumanGroupCirclesPresent()) {
				sb.append(" The average human circles are also shown using dotted lines.");
			}
					
			conditionPanel.setInstructionBannerText(sb.toString());				
		}
		else if(currentTrialPart instanceof GroupCentersProbeTrialPartState) {
			//Show the group centers probe
			GroupCentersProbeTrialPartState centersProbe = (GroupCentersProbeTrialPartState)currentTrialPart;
			
			//Remove the probe attack location from the map if it's present
			removeProbeAttackLocation();							
						
			showGroupCentersProbe(centersProbe);
			StringBuilder sb = createTrialNumberString(currentTrial+1, trialPhase+1, 
					currentTrialState.getNumTrialParts(), "Centers", currentTrialState.isTrialComplete());
			if(trialPhase == 0) {
				sb.append("A new trial has started. ");
			}
			sb.append("The " + participantLabel + " has located the centers of gravity for each group.");
			if(showAverageHumanCirclesCenters && centersProbe.isAvgHumanGroupCentersPresent()) {
				sb.append(" The average human centers are also shown using dotted lines.");
			}						
			conditionPanel.setInstructionBannerText(sb.toString());
		}						
		else if(currentTrialPart instanceof SurpriseProbeTrialPartState) {
			//Make sure the group circles or centers are shown
			if(!groupCirclesCentersShown) {
				if(currentTrialState instanceof Task_2_PlayerProbeTrialState) {
					showGroupCirclesProbe(((Task_2_PlayerProbeTrialState)currentTrialState).getGroupCirclesProbe());
				} else if(currentTrialState instanceof Task_3_PlayerProbeTrialState) {
					showGroupCentersProbe(((Task_3_PlayerProbeTrialState)currentTrialState).getGroupCentersProbe());
				}
			}			
			
			//Show the group responsible for the attack (ground truth)
			SurpriseProbeTrialPartState surpriseProbe = (SurpriseProbeTrialPartState)currentTrialPart;
			GroupType group = null;
			if(!groundTruthRevealed && surpriseProbe.getGroundTruth() != null && surpriseProbe.getGroundTruth().getResponsibleGroup() != null 
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
				groundTruthRevealed = true;
			}				

			//Show the surprise probe			
			SurpriseReportProbe probe = surpriseProbe.getProbe();
			if(probe != null) {				
				showSurpriseProbe(true, true, currentTrialState.getGroupProbe().getProbe().getGroups(),
						currentTrialState.getGroupProbe().getCurrentSettings(), 
						currentTrialState.getTroopProbe().getCurrentSettings(),
						surpriseProbe);
			}
			StringBuilder sb = createTrialNumberString(currentTrial+1, trialPhase+1, 
					currentTrialState.getNumTrialParts(), "Surprise", currentTrialState.isTrialComplete());
			if(group != null) {
				sb.append("The " + participantLabel + " has been shown that <b>Group " + group.getGroupNameFull() + "</b> was responsible for the attack " +
						"and has indicated surprise.");
			} else {
				sb.append("The " + participantLabel + " has been shown the group responsible for the attack and has indicated surprise.");
			}
			if(surpriseProbe.getCurrentSurprise() == null) {
				sb.append("<font color=\"red\"><i> The " + participantLabel + " did not indicate surprise for this trial. </i></font>");
			}
			if(surpriseProbe.getAvgHumanSurpsie() != null) {
				sb.append(" The average human surprise, the " + participantLabel + "'s probabilities,");
			} else {
				sb.append(" The " + participantLabel + "'s probabilities");
			}
			sb.append(" and the " + participantLabel + "'s troop allocations are also shown.");		
			createTrialAssessmentResultsString(currentTrialState, sb);			
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
	
	/** Add a list of attack locations to the map (unhighlighted). */
	protected void appendAttackLocations(List<GroupAttack> attackLocations) {
		if(attackLocations != null && !attackLocations.isEmpty()) {
			for(GroupAttack attackLocation : attackLocations) {
				AttackLocationPlacemark placemark = conditionPanel.getMapPanel().addSigactLocation(
						attackLocation, AttackLocationType.GROUP_ATTACK, false, false);
				placemark.setShowMarker(false);
			}
		}	
	}
	
	/** Add the probe attack location to the map and highlight it */
	protected void appendProbeAttackLocation(GroupAttack currAttackLocation) {		
		//Add the probe attack location location		
		probeAttackLocation = conditionPanel.getMapPanel().addSigactLocation(
				currAttackLocation, AttackLocationType.GROUP_ATTACK, true, false);		
		
		//Make the attacks layer the last layer in the z-order
		conditionPanel.getMapPanel().moveLayerToBottomOfZOrder(LayerType.SIGACTS);

		conditionPanel.getMapPanel().redrawMap();		
	}
	
	/** Remove the probe attack location from the map */
	protected void removeProbeAttackLocation() {
		if(probeAttackLocation != null) {
			conditionPanel.getMapPanel().removeSigactLocation(probeAttackLocation);
			conditionPanel.getMapPanel().redrawMap();
			probeAttackLocation = null;
		}
	}
	
	protected void showGroupProbe(GroupProbeTrialPartState groupProbe) {	
		List<GroupType> groups = groupProbe.getProbe().getGroups();
		//Show participant probabilities
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
		
		//Show normative probabilities
		if(groupProbe.isNormativeSettingsPresent()) {
			taskResponsePanel.setGroupsForNormativeProbsComponent(groups);
			taskResponsePanel.getNormativeProbsComponent().setCurrentSettings(groupProbe.getNormativeSettings());
			taskResponsePanel.setSubPanelComponent(1, taskResponsePanel.getNormativeProbsComponent());
			taskResponsePanel.setSubPanelVisible(1, true);
		} else {
			taskResponsePanel.setSubPanelVisible(1, false);
		}
		
		//Show average human probabilities
		if(groupProbe.isAvgHumanSettingsPresent()) {
			taskResponsePanel.setGroupsForAvgHumanProbsComponent(groups);
			taskResponsePanel.getAvgHumanProbsComponent().setCurrentSettings(groupProbe.getAvgHumanSettings());
			taskResponsePanel.setSubPanelComponent(2, taskResponsePanel.getAvgHumanProbsComponent());
			taskResponsePanel.setSubPanelVisible(2, true);
		} else {
			taskResponsePanel.setSubPanelVisible(2, false);	
		}
		
		taskResponsePanel.setSubPanelVisible(3, false);
	}

	protected void showTroopSelectionProbe(List<GroupType> groups, List<Integer> previousSettings, boolean showPreviousSettings,
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
	
	protected void showGroupCirclesProbe(GroupCirclesProbeTrialPartState circlesProbe) {	
		taskResponsePanel.setAllSubPanelsVisible(false);
		
		//Reset group circles layer
		MapPanelContainer mapPanel = conditionPanel.getMapPanel();		
		mapPanel.removeAllGroupCircles();
		mapPanel.setGroupCirclesLayerEnabled(true);
		
		//Add the average human group circles to the map using dotted lines
		if(showAverageHumanCirclesCenters && circlesProbe.getAvgHumanGroupCircles() != null &&
				!circlesProbe.getAvgHumanGroupCircles().isEmpty()) {
			//System.out.println(circlesProbe.getAvgHumanGroupCircles().size());
			for(GroupCircle groupCircle : circlesProbe.getAvgHumanGroupCircles()) {
				//System.out.println(groupCircle.getCenterLocation() + ", " + groupCircle.getRadius());
				CircleShape circle = mapPanel.addGroupCircle(groupCircle, false);
				circle.setBorderLineStyle(DASHED_LINE);
			}
		}
		
		//Add the participant group circles to the map
		if(circlesProbe.getParticipantGroupCircles() != null && !circlesProbe.getParticipantGroupCircles().isEmpty()) {
			for(GroupCircle groupCircle : circlesProbe.getParticipantGroupCircles()) {				
				mapPanel.addGroupCircle(groupCircle, false);
			}
		}						
		
		groupCirclesCentersShown = true;
		mapPanel.redrawMap();
	}
	
	protected void showGroupCentersProbe(GroupCentersProbeTrialPartState centersProbe) {
		taskResponsePanel.setAllSubPanelsVisible(false);
		
		//Reset the group centers layer
		MapPanelContainer mapPanel = conditionPanel.getMapPanel();			
		mapPanel.removeAllGroupCenters();		
		mapPanel.setGroupCentersLayerEnabled(true);
		
		//Add the average human group centers to the map using dotted lines
		if(showAverageHumanCirclesCenters && centersProbe.getAvgHumanGroupCenters() != null &&
				!centersProbe.getAvgHumanGroupCenters().isEmpty()) {			
			for(GroupCenter groupCenter : centersProbe.getAvgHumanGroupCenters()) {
				GroupCenterPlacemark gcp = mapPanel.addGroupCenter(groupCenter, false, false);
				gcp.setBorderLineStyle(DASHED_LINE_BOLD);
				gcp.setTransparency(0.8f);
			}
		}
		
		//Add the participant group centers to the map		
		if(centersProbe.getParticipantGroupCenters() != null && !centersProbe.getParticipantGroupCenters().isEmpty()) {
			for(GroupCenter groupCenter : centersProbe.getParticipantGroupCenters()) {				
				mapPanel.addGroupCenter(groupCenter, false, false);
			}
		}		

		groupCirclesCentersShown = true;
		mapPanel.redrawMap();
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