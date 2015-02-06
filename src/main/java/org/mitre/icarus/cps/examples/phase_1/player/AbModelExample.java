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
package org.mitre.icarus.cps.examples.phase_1.player;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import javax.swing.SwingWorker;

import org.mitre.icarus.cps.app.experiment.phase_1.player.PlayerController;
import org.mitre.icarus.cps.app.util.ProbabilityUtils;
import org.mitre.icarus.cps.assessment.model_simulator.phase_1.AbModel;
import org.mitre.icarus.cps.assessment.score_computer.phase_1.ScoreComputer;
import org.mitre.icarus.cps.assessment.score_computer.phase_1.ScoreComputerException;
import org.mitre.icarus.cps.exam.phase_1.IcarusExam_Phase1;
import org.mitre.icarus.cps.exam.phase_1.playback.ExamPlaybackDataSource_Phase1;
import org.mitre.icarus.cps.exam.phase_1.response.Task_1_ProbeTrialResponse;
import org.mitre.icarus.cps.exam.phase_1.response.Task_2_ProbeTrialResponse;
import org.mitre.icarus.cps.exam.phase_1.response.Task_3_ProbeTrialResponse;
import org.mitre.icarus.cps.exam.phase_1.response.Task_4_TrialResponse;
import org.mitre.icarus.cps.exam.phase_1.response.Task_5_6_TrialResponse;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.AttackLocationProbeResponse_MultiGroup;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.AttackLocationProbeResponse_MultiLocation;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.INTLayerData;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.Task_4_7_AttackLocationProbeResponseAfterINT;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.Task_5_6_AttackLocationProbeResponseAfterINT;
import org.mitre.icarus.cps.exam.phase_1.testing.IcarusTestTrial_Phase1;
import org.mitre.icarus.cps.exam.phase_1.testing.TaskTestPhase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_1_2_3_AttackDispersionParameters;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_1_2_3_PhaseBase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_1_2_3_TrialBlockBase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_1_ProbeTrial;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_1_TrialBlock;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_2_ProbeTrial;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_2_TrialBlock;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_3_Phase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_3_ProbeTrial;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_3_TrialBlock;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_4_Phase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_4_Trial;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_5_Phase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_5_Trial;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_6_Phase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_6_Trial;
import org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.HumintLayer;
import org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.Task_4_INTLayerPresentationProbe;
import org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.Task_5_6_INTLayerPresentationProbe;
import org.mitre.icarus.cps.exam.phase_1.testing.probability_rules.ProbabilityRules;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupType;
import org.mitre.icarus.cps.feature_vector.phase_1.Road;
import org.mitre.icarus.cps.feature_vector.phase_1.road_network.RoadDistanceCalculator;

/**
 * An example that demonstrates how to programmatically control the player and view a 
 * model run in real-time as responses are generated. The model uses an A-B strategy
 * to assess probabilities.
 * 
 * @author CBONACETO
 *
 */
public class AbModelExample extends AbModel {
	
	/** The player controller */
	protected final PlayerController playerController;
	
	/** The exam response data source containing the exam and responses */
	protected final ExamPlaybackDataSource_Phase1 examResponse;
	
	/** Score computer is used to compute S1 and S2 scores */
	protected ScoreComputer scoreComputer = new ScoreComputer();
	
	/** Probability rules used to compute scores */
	protected ProbabilityRules examRules;
	
	/** Time to wait before advancing to the next trial stage (milliseconds, default is 3000) */
	protected long stagePauseLength = 3000;
	
	private boolean advancingStage = false;	
	
	/**
	 * Create an A-B model with the default A-B parameters for each Task.
	 */
	public AbModelExample(PlayerController playerController, ExamPlaybackDataSource_Phase1 examResponse) {			
		super();
		this.playerController = playerController;
		this.examResponse = examResponse;
		examRules = examResponse != null && examResponse.getExam() != null && examResponse.getExam().getProbabilityRules() != null ?
				examResponse.getExam().getProbabilityRules() : ProbabilityRules.createDefaultProbabilityRules();
	}
	
	/**
	 * Create an A-B model with the given A-B parameters for each Task.
	 * 
	 * @param abTaskParameters
	 */
	public AbModelExample(AbParameters task_1_params, AbParameters task_2_params, AbParameters task_3_params,
			AbParameters task_4_1_params, AbParameters task_4_N_params, AbParameters task_5_params, AbParameters task_6_params,
			PlayerController playerController, ExamPlaybackDataSource_Phase1 examResponse) {
		super(task_1_params, task_2_params, task_3_params, task_4_1_params, task_4_N_params, task_5_params, task_6_params);
		this.playerController = playerController;
		this.examResponse = examResponse;
		examRules = examResponse != null && examResponse.getExam() != null && examResponse.getExam().getProbabilityRules() != null ?
				examResponse.getExam().getProbabilityRules() : ProbabilityRules.createDefaultProbabilityRules();
	}		

	/**
	 * @return
	 */
	public long getStagePauseLength() {
		return stagePauseLength;
	}

	/**
	 * @param stagePauseLength
	 */
	public void setStagePauseLength(long stagePauseLength) {
		this.stagePauseLength = stagePauseLength;
	}

	/**
	 * Step through all stages of all trials of all tasks in the exam and generate responses.
	 * 
	 */
	public void performExam() {
		//Run the model in a background thread
		SwingWorker<Object, Object> worker = new SwingWorker<Object, Object>() {
			@Override
			protected Object doInBackground() throws Exception {
				IcarusExam_Phase1 exam = examResponse.getExam();
				advancingStage = false;
				if(exam != null && exam.getTasks() != null && !exam.getTasks().isEmpty()) {
					initializeExam(exam);
					for(TaskTestPhase<?> task : exam.getTasks()) {
						boolean firstTrial = true;
						if(task instanceof Task_1_2_3_PhaseBase) {
							//Process Mission 1, 2, or 3
							Task_1_2_3_PhaseBase<?> task123 = (Task_1_2_3_PhaseBase<?>)task;
							List<Road> roads = null;
							if(task instanceof Task_3_Phase) {
								roads = ((Task_3_Phase) task).getRoads();
							}
							if(task123.getTrialBlocks() != null && !task123.getTrialBlocks().isEmpty()) {
								for(Task_1_2_3_TrialBlockBase trialBlock : task123.getTrialBlocks()) {
									try {
										if(trialBlock instanceof Task_1_TrialBlock) {
											processTask_1_Trial(((Task_1_TrialBlock)trialBlock).getProbeTrial(), firstTrial,
													task, task.getId(), task.getConditionNum());
										} else if (trialBlock instanceof Task_2_TrialBlock) {
											processTask_2_Trial(((Task_2_TrialBlock)trialBlock).getProbeTrial(), firstTrial,
													task, task.getId(), task.getConditionNum());
										} else if (trialBlock instanceof Task_3_TrialBlock) {
											processTask_3_Trial(((Task_3_TrialBlock)trialBlock).getProbeTrial(), roads, firstTrial,
													task, task.getId(), task.getConditionNum());
										}
									} catch(Exception ex) {
										ex.printStackTrace();
									}
									firstTrial = false;
								}
							}
						} else if(task instanceof Task_4_Phase) {
							//Process Mission 4
							Task_4_Phase task4 = (Task_4_Phase)task;
							if(task4.getTestTrials() != null && !task4.getTestTrials().isEmpty()) {
								for(Task_4_Trial trial : task4.getTestTrials()) {									
									try {
										processTask_4_Trial(trial, firstTrial,
												task, task.getId(), task.getConditionNum());
									} catch(Exception ex) {
										ex.printStackTrace();
									}
									firstTrial = false;
								}
							}
						} else if(task instanceof Task_6_Phase) {
							//Process Mission 6
							Task_6_Phase task6 = (Task_6_Phase)task;
							if(task6.getTestTrials() != null && !task6.getTestTrials().isEmpty()) {
								for(Task_6_Trial trial : task6.getTestTrials()) {
									try {
										processTask_5_6_Trial(trial, firstTrial,
												task, task.getId(), task.getConditionNum());
									} catch(Exception ex) {
										ex.printStackTrace();
									}
									firstTrial = false;
								}
							}
						} else if(task instanceof Task_5_Phase) {
							//Process Mission 5
							Task_5_Phase task5 = (Task_5_Phase)task;
							if(task5.getTestTrials() != null && !task5.getTestTrials().isEmpty()) {
								for(Task_5_Trial trial : task5.getTestTrials()) {
									try {
										processTask_5_6_Trial(trial, firstTrial,
												task, task.getId(), task.getConditionNum());
									} catch(Exception ex) {
										ex.printStackTrace();
									}
									firstTrial = false;
								}
							}
						}
						//Pause while showing the overall scores for the task
						pauseAndAdvanceStage();
						long endTime = System.currentTimeMillis() + stagePauseLength;
						while(System.currentTimeMillis() < endTime) {
							try { Thread.sleep(25); } catch(Exception ex) {}
						}
					}					
				}
				return null;
			}
		};
		worker.execute();
	}

	/**
	 * Process a trial in Mission 1.
	 * 
	 * @param trial
	 * @param firstTrial
	 * @param taskResponse
	 * @param taskId
	 * @param taskNum
	 */
	protected void processTask_1_Trial(Task_1_ProbeTrial trial, boolean firstTrial, TaskTestPhase<?> taskResponse, 
			String taskId, Integer taskNum) {
		Task_1_ProbeTrialResponse response = trial.getTrialResponse();
		if(response == null) {
			response = new Task_1_ProbeTrialResponse();
			trial.setTrialResponse(response);
		}		
		
		//Compute probabilities
		rules.setProbabilityUpdateStrategy(task_1_Strategy);		
		EnumMap<GroupType, Task_1_2_3_AttackDispersionParameters> dispersionParameters = 
				new EnumMap<GroupType, Task_1_2_3_AttackDispersionParameters>(GroupType.class);
		for(Task_1_2_3_AttackDispersionParameters parameters : trial.getAttackDispersionParameters()) {
			dispersionParameters.put(parameters.getGroup(), parameters);							
		}	
		ArrayList<Double> probs = null;
		try {
			probs = ScoreComputer.computeGroupProbabilities_Task_1_2(
					trial.getAttackLocationProbe().getAttackLocation(), 
					trial.getAttackLocationProbe().getGroups(), rules, 
					dispersionParameters);
			ProbabilityUtils.normalizePercentProbabilities_Double(probs, probs);
		} catch (ScoreComputerException e) {			
			e.printStackTrace();
		}
		if(response.getAttackLocationResponse() == null) {
			response.setAttackLocationResponse(new AttackLocationProbeResponse_MultiGroup(
					createGroupAttackProbabilities(trial.getAttackLocationProbe().getGroups(), probs),
					trial.getAttackLocationProbe().getAttackLocation().getLocationId()));
		} else {
			response.getAttackLocationResponse().setGroupAttackProbabilities(
					createGroupAttackProbabilities(trial.getAttackLocationProbe().getGroups(), probs));
		}
		
		//Update the trial data and advance to the probability probe stage after pausing
		updateTrialDataAndAdvanceStage(trial, false, firstTrial, taskResponse, taskId, taskNum);			
		
		//Generate all-in troop allocation response
		response.setTroopSelectionResponse(generateTask_1_2_3_TroopSelectionResponse(probs));
		
		//Update the trial data and advance to the troop allocation stage after pausing
		updateTrialDataAndAdvanceStage(trial, false, false, taskResponse, taskId, taskNum);	
		
		//Generate random surprise response
		response.setGroundTruthSurpriseResponse(generateSurpriseResponse(trial.getGroundTruthSurpriseProbe()));
		
		//Update the trial data and advance to the surprise stage after pausing	
		updateTrialDataAndAdvanceStage(trial, true, false, taskResponse, taskId, taskNum);
	}
	
	/**
	 * Process a trial in Mission 2.
	 * 
	 * @param trial
	 * @param firstTrial
	 * @param taskResponse
	 * @param taskId
	 * @param taskNum
	 */
	protected void processTask_2_Trial(Task_2_ProbeTrial trial, boolean firstTrial, TaskTestPhase<?> taskResponse, 
			String taskId, Integer taskNum) {
		Task_2_ProbeTrialResponse response = trial.getTrialResponse(); 
		if(response == null) {
			response = new Task_2_ProbeTrialResponse();
			trial.setTrialResponse(response);
		}
		
		//Generate random group circles
		response.setGroupCirclesResponse(generateGroupCirclesResponse(trial.getAttackLocationProbe().getGroups()));
		
		//Update the trial data and advance to the group circles stage after pausing
		updateTrialDataAndAdvanceStage(trial, false, firstTrial, taskResponse, taskId, taskNum);
		
		//Compute probabilities
		rules.setProbabilityUpdateStrategy(task_2_Strategy);
		EnumMap<GroupType, Task_1_2_3_AttackDispersionParameters> dispersionParameters = 
				new EnumMap<GroupType, Task_1_2_3_AttackDispersionParameters>(GroupType.class);
		for(Task_1_2_3_AttackDispersionParameters parameters : trial.getAttackDispersionParameters()) {
			dispersionParameters.put(parameters.getGroup(), parameters);							
		}	
		ArrayList<Double> probs = null;
		try {
			probs = ScoreComputer.computeGroupProbabilities_Task_1_2(
					trial.getAttackLocationProbe().getAttackLocation(), 
					trial.getAttackLocationProbe().getGroups(), rules, 
					dispersionParameters);
			ProbabilityUtils.normalizePercentProbabilities_Double(probs, probs);			
		} catch (ScoreComputerException e) {			
			e.printStackTrace();
		}
		if(response.getAttackLocationResponse() == null) {
			response.setAttackLocationResponse(new AttackLocationProbeResponse_MultiGroup(
					createGroupAttackProbabilities(trial.getAttackLocationProbe().getGroups(), probs),
					trial.getAttackLocationProbe().getAttackLocation().getLocationId()));
		} else {
			response.getAttackLocationResponse().setGroupAttackProbabilities(
					createGroupAttackProbabilities(trial.getAttackLocationProbe().getGroups(), probs));
		}		
		
		//Update the trial data and advance to the probability probe stage after pausing
		updateTrialDataAndAdvanceStage(trial, false, false, taskResponse, taskId, taskNum);
		
		//Generate all-in troop allocation response
		response.setTroopSelectionResponse(generateTask_1_2_3_TroopSelectionResponse(probs));
		
		//Update the trial data and advance to the troop allocation stage after pausing
		updateTrialDataAndAdvanceStage(trial, false, false, taskResponse, taskId, taskNum);
		
		//Generate random surprise response
		response.setGroundTruthSurpriseResponse(generateSurpriseResponse(trial.getGroundTruthSurpriseProbe()));
				
		//Update the trial data and advance to the surprise stage after pausing	
		updateTrialDataAndAdvanceStage(trial, true, false, taskResponse, taskId, taskNum);
	}
	
	/**
	 * Process a trial in Mission 3.
	 * 
	 * @param trial
	 * @param firstTrial
	 * @param taskResponse
	 * @param taskId
	 * @param taskNum
	 */
	protected void processTask_3_Trial(Task_3_ProbeTrial trial, List<Road> roads, boolean firstTrial, 
			TaskTestPhase<?> taskResponse, String taskId, Integer taskNum) {
		Task_3_ProbeTrialResponse response = trial.getTrialResponse(); 
		if(response == null) {
			response = new Task_3_ProbeTrialResponse();
			trial.setTrialResponse(response);
		}
		
		//Generate random group centers
		response.setGroupCentersResponse(generateGroupCentersResponse(trial.getAttackLocationProbe().getGroups()));
		
		//Update the trial data and advance to the group centers stage after pausing
		updateTrialDataAndAdvanceStage(trial, false, firstTrial, taskResponse, taskId, taskNum);
		
		//Compute probabilities
		rules.setProbabilityUpdateStrategy(task_3_Strategy);		
		EnumMap<GroupType, Task_1_2_3_AttackDispersionParameters> dispersionParameters = 
				new EnumMap<GroupType, Task_1_2_3_AttackDispersionParameters>(GroupType.class);
		for(Task_1_2_3_AttackDispersionParameters parameters : trial.getAttackDispersionParameters()) {
			dispersionParameters.put(parameters.getGroup(), parameters);							
		}	
		ArrayList<Double> probs = null;
		try {
			probs = ScoreComputer.computeGroupProbabilities_Task_3(
					trial.getAttackLocationProbe().getAttackLocation(),
					trial.getAttackLocationProbe().getGroups(),
					rules, gridSize, task3DistanceCalculator, dispersionParameters, null);	
			ProbabilityUtils.normalizePercentProbabilities_Double(probs, probs);
		} catch (ScoreComputerException e) {			
			e.printStackTrace();
		}
		if(response.getAttackLocationResponse() == null) {
			response.setAttackLocationResponse(new AttackLocationProbeResponse_MultiGroup(
					createGroupAttackProbabilities(trial.getAttackLocationProbe().getGroups(), probs),
					trial.getAttackLocationProbe().getAttackLocation().getLocationId()));
		} else {
			response.getAttackLocationResponse().setGroupAttackProbabilities(
					createGroupAttackProbabilities(trial.getAttackLocationProbe().getGroups(), probs));
		}		
		
		//Update the trial data and advance to the probability probe stage after pausing
		updateTrialDataAndAdvanceStage(trial, false, false, taskResponse, taskId, taskNum);
		
		//Generate all-in troop allocation response
		response.setTroopSelectionResponse(generateTask_1_2_3_TroopSelectionResponse(probs));
		
		//Update the trial data and advance to the troop allocation stage after pausing
		updateTrialDataAndAdvanceStage(trial, false, false, taskResponse, taskId, taskNum);
		
		//Generate random surprise response
		response.setGroundTruthSurpriseResponse(generateSurpriseResponse(trial.getGroundTruthSurpriseProbe()));	
			
		//Update the trial data and advance to the surprise stage after pausing	
		updateTrialDataAndAdvanceStage(trial, true, false, taskResponse, taskId, taskNum);
	}
	
	/**
	 * Process a trial in Mission 4.
	 * 
	 * @param trial
	 * @param firstTrial
	 * @param taskResponse
	 * @param taskId
	 * @param taskNum
	 */
	protected void processTask_4_Trial(Task_4_Trial trial, boolean firstTrial, TaskTestPhase<?> taskResponse, 
			String taskId, Integer taskNum) {
		Task_4_TrialResponse response = trial.getTrialResponse();
		if(response == null) {
			response = new Task_4_TrialResponse();
			trial.setTrialResponse(response);
		}
		
		//Compute center to attack distances if they haven't been computed yet				
		ArrayList<Double> centerToAttackDistances = trial.getCenterToAttackDistances();
		if(centerToAttackDistances == null || centerToAttackDistances.isEmpty()) {
			try {
				centerToAttackDistances = ScoreComputer.computeCenterToAttackDistances(trial.getGroupCenter().getLocation(), 
						trial.getPossibleAttackLocations(), gridSize, 
						new RoadDistanceCalculator(trial.getRoads(), gridSize));
			} catch (ScoreComputerException e) {				
				e.printStackTrace();
			}
			trial.setCenterToAttackDistances(centerToAttackDistances);
		}

		//Compute initial probabilities from HUMINT
		ArrayList<Double> currentProbs = ProbabilityUtils.createDefaultInitialProbabilities_Double(4);
		try {			
			rules.setProbabilityUpdateStrategy(task_4_1_Strategy);
			ScoreComputer.updateLocationProbabilities_Task_4(trial.getPossibleAttackLocations(), 
					trial.getGroupCenter(), centerToAttackDistances, new HumintLayer(), 
					currentProbs, null, rules);
			ArrayList<Double> probs = ProbabilityUtils.cloneProbabilities_Double(currentProbs);
			ProbabilityUtils.normalizePercentProbabilities_Double(probs, probs);
			if(response.getAttackLocationResponse_initial() == null) {			
				response.setAttackLocationResponse_initial(new AttackLocationProbeResponse_MultiLocation(
						createLocationAttackProbabilities(trial.getAttackLocationProbe_initial().getLocations(), probs),
						trial.getAttackLocationProbe_initial().getAttackGroup()));
			} else {
				response.getAttackLocationResponse_initial().setGroupAttackProbabilities(
						createLocationAttackProbabilities(trial.getAttackLocationProbe_initial().getLocations(), probs));
			}
		} catch (ScoreComputerException e) {
			e.printStackTrace();
		}				
		
		//Update the trial data and advance to the initial probability probe after pausing
		updateTrialDataAndAdvanceStage(trial, false, firstTrial, taskResponse, taskId, taskNum);

		//Compute likelihoods and updated posteriors for each INT layer based on rules for the layer
		if(trial.getIntLayers() != null && !trial.getIntLayers().isEmpty()) {			
			rules.setProbabilityUpdateStrategy(task_4_N_Strategy);
			ArrayList<Task_4_7_AttackLocationProbeResponseAfterINT> intResponses = response.getAttackLocationResponses_afterINTs();
			if(intResponses == null || intResponses.size() != trial.getIntLayers().size()) {
				intResponses = new ArrayList<Task_4_7_AttackLocationProbeResponseAfterINT>(trial.getIntLayers().size());
				response.setAttackLocationResponses_afterINTs(intResponses);
				for(int layerIndex = 0; layerIndex < trial.getIntLayers().size(); layerIndex++) {
					intResponses.add(null);
				}				
			}
			int layerIndex = 0;
			for(Task_4_INTLayerPresentationProbe layer : trial.getIntLayers()) {
				try {											
					ScoreComputer.updateLocationProbabilities_Task_4(trial.getPossibleAttackLocations(), 
							trial.getGroupCenter(), centerToAttackDistances, 
							layer.getLayerType(), 
							currentProbs, null, rules);	
					ArrayList<Double> probs = ProbabilityUtils.cloneProbabilities_Double(currentProbs);
					ProbabilityUtils.normalizePercentProbabilities_Double(probs, probs);
					Task_4_7_AttackLocationProbeResponseAfterINT intResponse = intResponses.get(layerIndex);
					if(intResponse == null) {
						intResponse = new Task_4_7_AttackLocationProbeResponseAfterINT(
								new INTLayerData(layer.getLayerType(), false),
								new AttackLocationProbeResponse_MultiLocation(
										createLocationAttackProbabilities(trial.getAttackLocationProbe_initial().getLocations(), probs),
										trial.getAttackLocationProbe_initial().getAttackGroup()));
						intResponses.set(layerIndex, intResponse);
					} else {
						intResponse.getAttackLocationResponse().setGroupAttackProbabilities(
								createLocationAttackProbabilities(trial.getAttackLocationProbe_initial().getLocations(), probs));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				layerIndex++;
				
				//Update the trial data and advance to the probability probe after the INT layer after pausing
				updateTrialDataAndAdvanceStage(trial, false, false, taskResponse, taskId, taskNum);
			}
		}

		//Generate probability-matched troop allocation response
		response.setTroopAllocationResponse(generate_Task_4_TroopAllocationResponse(
				trial.getAttackLocationProbe_initial().getLocations(), currentProbs));
		
		//Update the trial data and advance to the troop allocation stage after pausing
		updateTrialDataAndAdvanceStage(trial, false, false, taskResponse, taskId, taskNum);

		//Generate random surprise response
		response.setGroundTruthSurpriseResponse(generateSurpriseResponse(trial.getGroundTruthSurpriseProbe()));		
					
		//Update the trial data and advance to the surprise stage after pausing	
		updateTrialDataAndAdvanceStage(trial, true, false, taskResponse, taskId, taskNum);
	}
	
	/**
	 * Process a trial in Mission 5 or 6.
	 * 
	 * @param trial
	 * @param firstTrial
	 * @param taskResponse
	 * @param taskId
	 * @param taskNum
	 */
	protected void processTask_5_6_Trial(Task_5_Trial trial, boolean firstTrial, TaskTestPhase<?> taskResponse, 
			String taskId, Integer taskNum) {
		Task_5_6_TrialResponse response = trial.getTrialResponse();
		if(response == null) {
			response = new Task_5_6_TrialResponse();
			trial.setTrialResponse(response);
		}
		
		//Compute centers to attack distances if they haven't been computed yet				
		ArrayList<Double> centersToAttackDistances = trial.getCentersToAttackDistances();
		if(centersToAttackDistances == null || centersToAttackDistances.isEmpty()) {
			try {
				centersToAttackDistances = ScoreComputer.computeCentersToAttackDistances(trial.getAttackLocation().getLocation(),
						trial.getGroupCenters(), gridSize, 
						new RoadDistanceCalculator(trial.getRoads(), gridSize));
			} catch (ScoreComputerException e) {				
				e.printStackTrace();
			}
			trial.setCentersToAttackDistances(centersToAttackDistances);
		}		

		//Compute initial probabilities from HUMINT		
		rules.setProbabilityUpdateStrategy(bayesianStrategy);
		ArrayList<Double> currentProbs = ProbabilityUtils.createDefaultInitialProbabilities_Double(4);
		try {
			ScoreComputer.updateGroupProbabilities_Task_5_6(trial.getGroupCenters(), 
					trial.getAttackLocation(), centersToAttackDistances, new HumintLayer(), 
					currentProbs, null, null, rules);			
		} catch (ScoreComputerException e) {
			e.printStackTrace();
		}		

		//Compute likelihoods for each INT layer based on rules for the layer
		boolean task6Trial = (trial instanceof Task_6_Trial);
		if(task6Trial) {
			rules.setProbabilityUpdateStrategy(task_6_Strategy);
		} else {
			rules.setProbabilityUpdateStrategy(task_5_Strategy);
		}
		if(trial.getIntLayers() != null && !trial.getIntLayers().isEmpty()) {
			int numLayers = trial.getIntLayers().size();
			if(task6Trial) {
				numLayers = ((Task_6_Trial)trial).getNumLayersToShow();
			}
			ArrayList<Task_5_6_AttackLocationProbeResponseAfterINT> intResponses = response.getAttackLocationResponses_afterINTs();
			if(intResponses == null || intResponses.size() != numLayers) {
				intResponses = new ArrayList<Task_5_6_AttackLocationProbeResponseAfterINT>(trial.getIntLayers().size());				
				response.setAttackLocationResponses_afterINTs(intResponses);
				for(int layerIndex = 0; layerIndex < numLayers; layerIndex++) {
					intResponses.add(null);
				}
			}
			//Get or select the INT layers (task 6)			
			for(int layerIndex = 0; layerIndex < numLayers; layerIndex++) {
				Task_5_6_INTLayerPresentationProbe layer = null;
				if(task6Trial) {
					//Select the next layer					
					layer = selectLayer(trial.getIntLayers(), task6Layers.get(layerIndex), currentProbs);
				} else {
					//Get the next layer
					layer = trial.getIntLayers().get(layerIndex);
				}
				try {
					ScoreComputer.updateGroupProbabilities_Task_5_6(trial.getGroupCenters(), 
							trial.getAttackLocation(), centersToAttackDistances, 
							layer.getLayerType(), 
							currentProbs, null, null, rules);
					ArrayList<Double> probs = ProbabilityUtils.cloneProbabilities_Double(currentProbs);
					ProbabilityUtils.normalizePercentProbabilities_Double(probs, probs);
					Task_5_6_AttackLocationProbeResponseAfterINT intResponse = intResponses.get(layerIndex);
					if(intResponse == null) {
						intResponse = new Task_5_6_AttackLocationProbeResponseAfterINT(
								new INTLayerData(layer.getLayerType(), task6Trial),
								new AttackLocationProbeResponse_MultiGroup(
										createGroupAttackProbabilities(layer.getAttackLocationProbe().getGroups(), probs),
										layer.getAttackLocationProbe().getAttackLocation().getLocationId()));
						intResponses.set(layerIndex, intResponse);
					} else {
						intResponse.setIntLayerShown(new INTLayerData(layer.getLayerType(), task6Trial));
						intResponse.getAttackLocationResponse().setGroupAttackProbabilities(
								createGroupAttackProbabilities(layer.getAttackLocationProbe().getGroups(), probs));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				//Update the trial data and advance to the probability probe after the INT layer after pausing
				//(If task 6, we need to update the normative probabilities after every layer selection since they were not pre-computed)
				updateTrialDataAndAdvanceStage(trial, task6Trial, firstTrial && layerIndex == 0, taskResponse, taskId, taskNum);
			}
		}

		//Generate probability-matched troop allocation response
		response.setTroopAllocationResponse(generate_Task_5_6_TroopAllocationResponse(
				getGroups(trial.getGroupCenters()), currentProbs));
		
		//Update the trial data and advance to the troop allocation stage after pausing
		updateTrialDataAndAdvanceStage(trial, false, false, taskResponse, taskId, taskNum);

		//Generate random surprise response
		response.setGroundTruthSurpriseResponse(generateSurpriseResponse(trial.getGroundTruthSurpriseProbe()));		
					
		//Update the trial data and advance to the surprise stage after pausing	
		updateTrialDataAndAdvanceStage(trial, true, false, taskResponse, taskId, taskNum);
	}
	
	/**
	 * Updates the trial data, pauses for stagePauseLength milliseconds, and 
	 * then has the controller advance to the next stage in a trial. 
	 */
	protected void updateTrialDataAndAdvanceStage(IcarusTestTrial_Phase1 trial,	
			boolean scoreTrialIfFeedbackMissingOrIncomplete, boolean initializeTask, 
			TaskTestPhase<?> taskResponse, String taskId, Integer taskNum) {
		//Update the trial data and advance to the next stage after pausing		
		examResponse.updateTrialResponse(taskResponse, trial, 
				trial.getTrialResponse() != null ? trial.getTrialResponse().getResponseFeedBack() : null,
				scoreTrialIfFeedbackMissingOrIncomplete, taskId, taskNum, trial.getTrialNum());		
		if(initializeTask) {
			//Go to the first stage of the first trial in the Mission
			playerController.advanceToTrial(taskId, -1);
			while(!playerController.isExperimentRunning()) {
				try { Thread.sleep(25); } catch(Exception ex) {}
			}
			pauseAndAdvanceStage();
			//Wait for the controller to initialize the Mission
			while(!playerController.isConditionRunning()) {
				try { Thread.sleep(25); } catch(Exception ex) {}
			}
		} else {
			//Advance to the next stage after pausing
			pauseAndAdvanceStage();
		}
	}
	
	/**
	 * Pauses for stagePauseLength milliseconds and then has the controller advance to the next stage in a trial. 
	 */
	protected void pauseAndAdvanceStage() {
		long endTime = System.currentTimeMillis() + stagePauseLength;
		while(System.currentTimeMillis() < endTime) {
			try { Thread.sleep(25); } catch(Exception ex) {}
		}		
		while(advancingStage) {
			try { Thread.sleep(25); } catch(Exception ex) {}
		}
		advancingStage = true;
		try {
			playerController.advanceToNextStage();
		} catch(Exception ex) {}
		finally {
			advancingStage = false;
		}
	}
}