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
package org.mitre.icarus.cps.assessment.score_computer.phase_1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.mitre.icarus.cps.app.util.ProbabilityUtils;
import org.mitre.icarus.cps.app.widgets.progress_monitor.IProgressMonitor;
import org.mitre.icarus.cps.assessment.score_computer.ScoreComputerBase;
import org.mitre.icarus.cps.exam.phase_1.feedback.TaskFeedback;
import org.mitre.icarus.cps.exam.phase_1.feedback.TrialFeedback_Phase1;
import org.mitre.icarus.cps.exam.phase_1.response.Task_1_2_3_ProbeTrialResponseBase;
import org.mitre.icarus.cps.exam.phase_1.response.Task_1_ProbeTrialResponse;
import org.mitre.icarus.cps.exam.phase_1.response.Task_2_ProbeTrialResponse;
import org.mitre.icarus.cps.exam.phase_1.response.Task_3_ProbeTrialResponse;
import org.mitre.icarus.cps.exam.phase_1.response.Task_4_TrialResponse;
import org.mitre.icarus.cps.exam.phase_1.response.Task_5_6_TrialResponse;
import org.mitre.icarus.cps.exam.phase_1.response.Task_7_TrialResponse;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.AttackLocationProbeResponse_MultiGroup;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.AttackLocationProbeResponse_MultiLocation;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.GroupAttackProbabilityResponse;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.GroupCentersProbeResponse;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.INTLayerExpectedUtility;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.GroupCentersProbeResponse.GroupCenterResponse;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.GroupCirclesProbeResponse;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.GroupCirclesProbeResponse.GroupCircle;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.INTLayerData;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.Task_4_7_AttackLocationProbeResponseAfterINT;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.Task_5_6_AttackLocationProbeResponseAfterINT;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.Task_7_INTLayerPurchase;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.TroopAllocation;
import org.mitre.icarus.cps.exam.phase_1.testing.AttackLocationPresentationTrial;
import org.mitre.icarus.cps.exam.phase_1.testing.GroundTruth;
import org.mitre.icarus.cps.exam.phase_1.testing.IcarusTestTrial_Phase1;
import org.mitre.icarus.cps.exam.phase_1.testing.TaskTestPhase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_1_2_3_AttackDispersionParameters;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_1_2_3_ProbeTrialBase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_1_ProbeTrial;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_2_ProbeTrial;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_3_Phase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_3_ProbeTrial;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_4_Trial;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_5_Trial;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_6_Trial;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_7_Phase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_7_Trial;
import org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.HumintLayer;
import org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.IntLayer;
import org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.SigintLayer;
import org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.SocintLayer;
import org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.Task_4_INTLayerPresentationProbe;
import org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.Task_5_6_INTLayerPresentationProbe;
import org.mitre.icarus.cps.exam.phase_1.testing.probability_rules.HumintRules;
import org.mitre.icarus.cps.exam.phase_1.testing.probability_rules.ImintRule;
import org.mitre.icarus.cps.exam.phase_1.testing.probability_rules.MovintRule;
import org.mitre.icarus.cps.exam.phase_1.testing.probability_rules.ProbabilityRules;
import org.mitre.icarus.cps.exam.phase_1.testing.probability_rules.SigintRule;
import org.mitre.icarus.cps.exam.phase_1.testing.probability_rules.SocintRule;
import org.mitre.icarus.cps.exam.phase_1.testing.probability_rules.update_strategy.BayesianUpdateStrategy;
import org.mitre.icarus.cps.exam.phase_1.testing.probability_rules.update_strategy.ProbabilityUpdateStrategy;
import org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.HumintReport;
import org.mitre.icarus.cps.feature_vector.phase_1.GridLocation2D;
import org.mitre.icarus.cps.feature_vector.phase_1.GridSize;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupAttack;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupCenter;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupType;
import org.mitre.icarus.cps.feature_vector.phase_1.ImintType;
import org.mitre.icarus.cps.feature_vector.phase_1.IntType;
import org.mitre.icarus.cps.feature_vector.phase_1.LocationIntelReport;
import org.mitre.icarus.cps.feature_vector.phase_1.MovintType;
import org.mitre.icarus.cps.feature_vector.phase_1.Road;
import org.mitre.icarus.cps.feature_vector.phase_1.SigintType;
import org.mitre.icarus.cps.feature_vector.phase_1.road_network.IRoadDistanceCalculator;
import org.mitre.icarus.cps.feature_vector.phase_1.road_network.RoadDistanceCalculator;

/**
 * Default CPD Phase 1 score computer implementation. 
 * Used to compute normative solutions and scores for the Phase 1 Challenge Problem.
 * 
 * @author CBONACETO
 *
 */
public class ScoreComputer extends ScoreComputerBase implements IScoreComputer {
	
	protected static final double SQRT_2_PI = StrictMath.sqrt(2*StrictMath.PI);
	
	/** Dispersion parameter types for Tasks 1-3 */                                                                                                                                        
	public static enum DispersionParameterType {
		//GenerativeModelParameters, //Use dispersion parameters used in the generative model and stored statically in this class
		ExamFileParameters, //Use dispersion parameters stored in an exam
		ComputeDynamically //Compute dispersion parameters on-the-fly
	}
	
	/** Whether we're debugging */
	//Make sure this is false when building distributions
	public static boolean DEBUG = false;
	
	/** Whether to compute the "incremental" Bayesian probabilities that use the subject's probabilities as priors */
	protected static boolean computeIncrementalProbabilities = false;
	
	/** The dispersion parameters to use when computing scores for Tasks 1-3 */
	//Set to ExamFileParameters normally, or ComputeDynamically to compute new parameters when data changes
	protected DispersionParameterType dispersionType = DispersionParameterType.ExamFileParameters;	
	//protected DispersionParameterType dispersionType = DispersionParameterType.ComputeDynamically;
	
	/** The default probability rules */
	protected static final ProbabilityRules DEFAULT_RULES = ProbabilityRules.createDefaultProbabilityRules();
	
	/** Whether to use max likelioods or min distances as the optimization constraint when finding the best-fit road points for Task 3 */
	//Set to true when buidling distributions
	protected static boolean useMaxLikelihoods = true; 
	
	/**
	 * @return
	 */
	public DispersionParameterType getDispersionType() {
		return dispersionType;
	}

	/**
	 * @param dispersionType
	 */
	public void setDispersionType(DispersionParameterType dispersionType) {
		this.dispersionType = dispersionType;
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment.phase_1.score_computer.IScoreComputer#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
		return true;
	}	

	/**
	 * Removes the normative solution from all trials in a task.
	 * 
	 * @param task The task to remove normative solutions from.
	 */
	@SuppressWarnings("deprecation")
	public void sanitizeTask(TaskTestPhase<?> task) {
		for(IcarusTestTrial_Phase1 trial : task.getTestTrials()) {
			if(trial instanceof Task_1_2_3_ProbeTrialBase) {
				Task_1_2_3_ProbeTrialResponseBase response = ((Task_1_2_3_ProbeTrialBase)trial).getTrialResponse();				
				if(response != null) {
					if(response.getAttackLocationResponse() != null) {
						response.getAttackLocationResponse().setNormativeProbs_cumulativeBayesian(null);
						response.getAttackLocationResponse().setNormativeProbs_incrementalBayesian(null);
					}
				}
			} else if(trial instanceof Task_4_Trial) {		
				Task_4_TrialResponse response = ((Task_4_Trial)trial).getTrialResponse();
				if(response != null) {
					if(response.getAttackLocationResponse_initial() != null) {
						response.getAttackLocationResponse_initial().setNormativeProbs_cumulativeBayesian(null);
						response.getAttackLocationResponse_initial().setNormativeProbs_incrementalBayesian(null);
					}
					if(response.getAttackLocationResponses_afterINTs() != null) {
						for(Task_4_7_AttackLocationProbeResponseAfterINT intResponse : response.getAttackLocationResponses_afterINTs()) {
							if(intResponse.getAttackLocationResponse() != null) {
								intResponse.getAttackLocationResponse().setNormativeProbs_cumulativeBayesian(null);
								intResponse.getAttackLocationResponse().setNormativeProbs_incrementalBayesian(null);
							}
						}
					}
				}
			} else if(trial instanceof Task_5_Trial) {	
				Task_5_6_TrialResponse response = ((Task_5_Trial)trial).getTrialResponse();
				boolean layerUserSelected = trial instanceof Task_6_Trial;
				if(response != null) {
					if(response.getAttackLocationResponse_initial() != null) {
						response.getAttackLocationResponse_initial().setNormativeProbs_cumulativeBayesian(null);
						response.getAttackLocationResponse_initial().setNormativeProbs_incrementalBayesian(null);
					}
					if(response.getAttackLocationResponses_afterINTs() != null) {
						for(Task_5_6_AttackLocationProbeResponseAfterINT intResponse : response.getAttackLocationResponses_afterINTs()) {
							if(intResponse.getAttackLocationResponse() != null) {
								intResponse.getAttackLocationResponse().setNormativeProbs_cumulativeBayesian(null);
								intResponse.getAttackLocationResponse().setNormativeProbs_incrementalBayesian(null);
							}
							intResponse.getIntLayerShown().setUserSelected(layerUserSelected);
							intResponse.setIntLayerExpectedUtilities(null);
						}
					}
				}
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment.phase_1.score_computer.IScoreComputer#computeSolutionAndScoreForTask(org.mitre.icarus.cps.exam.phase_1.testing.TaskTestPhase, org.mitre.icarus.cps.exam.phase_1.testing.probability_rules.ProbabilityRules, org.mitre.icarus.cps.feature_vector.phase_1.GridSize, boolean, org.mitre.icarus.cps.app.progress_monitor.IProgressMonitor)
	 */
	@Override
	public TaskFeedback computeSolutionAndScoreForTask(TaskTestPhase<?> task, ProbabilityRules rules, GridSize gridSize,
			boolean computeScore, IProgressMonitor progressMonitor) {
		//Compute the score for each trial and the average S1 and S2 scores
		if(DEBUG) {
			System.out.println("Computing score for: " + task.getName());
		}
		TaskFeedback feedback = new TaskFeedback();	
		ErrorString taskErrors = new ErrorString();
		if(task.getTestTrials() == null || task.getTestTrials().isEmpty()) {
			taskErrors.append("Trials missing from task");
			feedback.setErrors(taskErrors.toString());
			return feedback;
		}
		int numS1Scores = 0;
		Double scoreSum_s1 = 0D;
		int numS2Scores = 0;
		Double scoreSum_s2 = 0D;
		List<TrialFeedback_Phase1> trialFeedback = new ArrayList<TrialFeedback_Phase1>(task.getTestTrials().size());
		feedback.setTrialFeedback(trialFeedback);
		
		//Create road network distance caclulator
		RoadDistanceCalculator distanceCalculator = null;
		if(task instanceof Task_3_Phase) {
			Task_3_Phase task3 = (Task_3_Phase)task;
			distanceCalculator = new RoadDistanceCalculator(task3.getRoads(), gridSize);
		}
		
		//Create default probability rules instance if null
		if(rules == null) {
			rules = ProbabilityRules.createDefaultProbabilityRules();
		}
		
		int trialNum = 1;
		if(progressMonitor != null) {
			progressMonitor.setMinimum(1);
			progressMonitor.setMaximum(task.getTestTrials().size());
			progressMonitor.setProgress(0);
		}		
		ArrayList<AttackLocationPresentationTrial> groupAttacks = new ArrayList<AttackLocationPresentationTrial>(); //Contains the group attacks seen for Tasks 1-3
		double currentCredits = 10; //the current credits for Task 7 
		int correctPredictionCredits = 1; //The credits awarded for a correct prediction for Task 7
		if(task instanceof Task_7_Phase) {
			Task_7_Phase task7 = (Task_7_Phase)task;
			if(task7.getInitialCredits() != null) {
				currentCredits = task7.getInitialCredits().doubleValue();
			}
			if(task7.getCorrectPredictionCredits() != null) {
				correctPredictionCredits = task7.getCorrectPredictionCredits();
			}
		}
		for(IcarusTestTrial_Phase1 trial : task.getTestTrials()) {
			if(progressMonitor != null) {
				progressMonitor.setNote("Scoring trial " + trialNum);
			}
			TrialFeedback_Phase1 feedbackForTrial = null;
			if(trial instanceof AttackLocationPresentationTrial) {
				//Add an attack to the attack history
				groupAttacks.add((AttackLocationPresentationTrial)trial);
			} else {			
				if(DEBUG) {
					System.out.println("Trial: " + trialNum);
				}
				if(trial instanceof Task_1_2_3_ProbeTrialBase) {				
					Task_1_2_3_ProbeTrialBase probeTrial = (Task_1_2_3_ProbeTrialBase)trial;
					feedbackForTrial = computeSolutionAndScoreForTrial(probeTrial, groupAttacks, 
							rules, gridSize, distanceCalculator, computeScore);
					//Add attack from the probe trial to the attack history				
					groupAttacks.add(new AttackLocationPresentationTrial(new GroupAttack(probeTrial.getGroundTruth().getResponsibleGroup(),
							probeTrial.getAttackLocationProbe().getAttackLocation())));
				} else if(trial instanceof Task_4_Trial) {				
					feedbackForTrial = computeSolutionAndScoreForTrial((Task_4_Trial)trial, rules, gridSize, computeScore);
				} else if(trial instanceof Task_6_Trial) {				
					feedbackForTrial = computeSolutionAndScoreForTrial((Task_6_Trial)trial, rules, gridSize, computeScore);
				} else if(trial instanceof Task_5_Trial) {
					feedbackForTrial = computeSolutionAndScoreForTrial((Task_5_Trial)trial, rules, gridSize, computeScore);
				} else if(trial instanceof Task_7_Trial) {
					feedbackForTrial = computeSolutionAndScoreForTrial((Task_7_Trial)trial, currentCredits, correctPredictionCredits);
					if(feedbackForTrial != null && feedbackForTrial.getNumCreditsRemaining() != null) {
						currentCredits = feedbackForTrial.getNumCreditsRemaining();
					}
				}
				if(feedbackForTrial != null) {
					trialFeedback.add(feedbackForTrial);
					if(feedbackForTrial.getProbabilitiesScore_s1() != null) {
						numS1Scores++;
						scoreSum_s1 += feedbackForTrial.getProbabilitiesScore_s1();
					}
					if(feedbackForTrial.getTroopAllocationScore_s2() != null) {
						numS2Scores++;
						scoreSum_s2 += feedbackForTrial.getTroopAllocationScore_s2();
					}
					if(trial.getTrialResponse() != null) {
						feedbackForTrial.setResponseWellFormed(feedbackForTrial.getErrors() == null || feedbackForTrial.getErrors().isEmpty());
						trial.getTrialResponse().setResponseFeedBack(feedbackForTrial);
					}
				}
			}
			if(progressMonitor != null) {
				progressMonitor.setProgress(trialNum);
			}
			trialNum++;
		}
		if(numS1Scores > 0) {
			feedback.setProbabilitiesScore_s1(scoreSum_s1/numS1Scores);
		} else {
			feedback.setProbabilitiesScore_s1(null);
		}
		
		if(numS2Scores > 0) {
			feedback.setTroopAllocationScore_s2(scoreSum_s2/numS2Scores);
		} else {
			feedback.setTroopAllocationScore_s2(null);
		}
		feedback.setErrors(taskErrors.toString());
		return feedback;
	}	
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment.phase_1.score_computer.IScoreComputer#computeSolutionAndScoreForTrial(org.mitre.icarus.cps.exam.phase_1.testing.Task_1_2_3_ProbeTrialBase, org.mitre.icarus.cps.exam.phase_1.testing.probability_rules.ProbabilityRules, org.mitre.icarus.cps.feature_vector.phase_1.GridSize, java.util.ArrayList, boolean)
	 */
	@Override
	public TrialFeedback_Phase1 computeSolutionAndScoreForTrial(Task_1_2_3_ProbeTrialBase trial, ProbabilityRules rules, 
			GridSize gridSize, List<Road> roads, boolean computeScore) {
		return computeSolutionAndScoreForTrial(trial, null, rules, gridSize, 
				roads != null ? new RoadDistanceCalculator(roads, gridSize) : null, computeScore);
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment.phase_1.score_computer.IScoreComputer#computeSolutionAndScoreForTrial(org.mitre.icarus.cps.exam.phase_1.testing.Task_1_2_3_ProbeTrialBase, java.util.ArrayList, org.mitre.icarus.cps.exam.phase_1.testing.probability_rules.ProbabilityRules, org.mitre.icarus.cps.feature_vector.phase_1.GridSize, org.mitre.icarus.cps.feature_vector.phase_1.road_network.IRoadDistanceCalculator, boolean)
	 */
	@Override
	public TrialFeedback_Phase1 computeSolutionAndScoreForTrial(Task_1_2_3_ProbeTrialBase trial, 
			List<AttackLocationPresentationTrial> groupAttacks, ProbabilityRules rules, 
			GridSize gridSize, IRoadDistanceCalculator distanceCalculator, boolean computeScore) {
		ErrorString errors = new ErrorString();
		TrialFeedback_Phase1 feedback = new TrialFeedback_Phase1();			
		boolean troopSelectionScorable = true;
		boolean groupProbeScorable = true;		
		boolean groupProbeComputable = true;
		boolean responseMissing = false;
		if(computeScore && trial.getTrialResponse() == null) {
			errors.append("Trial response missing, cannot compute S1 or S2 score");
			responseMissing = true;
			troopSelectionScorable = false;
			groupProbeScorable = false;
		}
		if(trial.getTroopSelectionProbe() == null || trial.getTroopSelectionProbe().getGroups() == null) {
			errors.append("Troop selection probe missing from trial");
		}
		if(trial.getGroundTruth() == null || trial.getGroundTruth().getResponsibleGroup() == null) {
			errors.append("Ground truth missing, cannot compute troop allocation score S2");
			troopSelectionScorable = false;
		}

		if(trial.getTrialResponse() == null) {			
			if(trial instanceof Task_1_ProbeTrial) {
				Task_1_ProbeTrialResponse response = new Task_1_ProbeTrialResponse();
				response.setAttackLocationResponse(new AttackLocationProbeResponse_MultiGroup());
				((Task_1_ProbeTrial)trial).setTrialResponse(response);
			} else if(trial instanceof Task_2_ProbeTrial) {
				Task_2_ProbeTrialResponse response = new Task_2_ProbeTrialResponse();
				response.setAttackLocationResponse(new AttackLocationProbeResponse_MultiGroup());
				((Task_2_ProbeTrial)trial).setTrialResponse(response);
			} else {
				Task_3_ProbeTrialResponse response = new Task_3_ProbeTrialResponse();
				response.setAttackLocationResponse(new AttackLocationProbeResponse_MultiGroup());
				((Task_3_ProbeTrial)trial).setTrialResponse(response);
			}
		}		
		Task_1_2_3_ProbeTrialResponseBase response = trial.getTrialResponse();
		
		if(computeScore && !responseMissing && (response.getTroopSelectionResponse() == null || 
				response.getTroopSelectionResponse().getGroup() == null)) {				
			errors.append("Troop selection response missing, cannot compute troop allocation score S2");
			troopSelectionScorable = false;
		}
		if(computeScore && !responseMissing && response.getAttackLocationResponse() == null) {
			errors.append("Group probe response missing, cannot compute probabilities score S1");
			groupProbeScorable = false;
		}		
		if(computeScore && !responseMissing && groupProbeScorable &&
				(response.getAttackLocationResponse().getGroupAttackProbabilities() == null ||
				response.getAttackLocationResponse().getGroupAttackProbabilities().size() !=
				trial.getAttackLocationProbe().getGroups().size())) {
			errors.append("Not enough probabilities in the group probe response, cannot compute probabilities score S1");
			groupProbeScorable = false;
		}
		if(trial.getAttackLocationProbe().getAttackLocation() == null) {
			errors.append("Trial missing the attack location that was probed, cannot compute probabilities score S1 or compute the normative solution");
			groupProbeScorable = false;
			groupProbeComputable = false;
		}
		
		//Create default probability rules instance if null
		if(rules == null) {
			rules = ProbabilityRules.createDefaultProbabilityRules();
		}

		//Compute the normative probabilities and probabilities score for the trial (S1)
		if(groupProbeComputable) {
			ArrayList<Double> cumulativeProbs = null; //normative probabilities based on the "best fit" group centers
			ArrayList<Double> incrementalProbs = null; //normative probabilities based on the subject group centers for Tasks 2-3
			EnumMap<GroupType, Task_1_2_3_AttackDispersionParameters> dispersionParameters = null;
			/*if(dispersionType == DispersionParameterType.GenerativeModelParameters) {
				//Use the static generative model dispersion parameters
				if(trial instanceof Task_1_ProbeTrial) {
					dispersionParameters = TASK_1_PARAMETERS;
				} else if(trial instanceof Task_2_ProbeTrial) {
					dispersionParameters = TASK_2_PARAMETERS;
				} else if(trial instanceof Task_3_ProbeTrial) {
					dispersionParameters = TASK_3_PARAMETERS;
				}
			} else*/
			if(dispersionType == DispersionParameterType.ExamFileParameters) {
				//Use the dispersion parameters from the exam
				if(trial.getAttackDispersionParameters() != null && !trial.getAttackDispersionParameters().isEmpty()) {
					dispersionParameters = new EnumMap<GroupType, Task_1_2_3_AttackDispersionParameters>(GroupType.class);
					for(Task_1_2_3_AttackDispersionParameters parameters : trial.getAttackDispersionParameters()) {
						dispersionParameters.put(parameters.getGroup(), parameters);
					}
				} else {
					errors.append("Warning, trial missing attack dispersion parameters. These will be computed.");
				}
			}
			try {
				if(trial instanceof Task_1_ProbeTrial || trial instanceof Task_2_ProbeTrial) {
					if(DEBUG) {
						System.out.println("Attack Location: (" + trial.getAttackLocationProbe().getAttackLocation().getX()  + "," + 
								trial.getAttackLocationProbe().getAttackLocation().getY() + ")");
					}
					if(dispersionParameters == null) {
						//Compute "best-fit" dispersion parameters and group centers						
						dispersionParameters = computeGroupParameters_Task_1_2(trial.getAttackLocationProbe().getGroups(), groupAttacks);
						trial.setAttackDispersionParameters(new ArrayList<Task_1_2_3_AttackDispersionParameters>(
								dispersionParameters.values()));												
					}
					//Compute probabilities based on "best fit" group centers
					cumulativeProbs = computeGroupProbabilities_Task_1_2(trial.getAttackLocationProbe().getAttackLocation(), 
							trial.getAttackLocationProbe().getGroups(), rules, dispersionParameters);
					//Compute probabilities based on subject group centers for Task 2
					if(computeIncrementalProbabilities && trial instanceof Task_2_ProbeTrial && computeScore && !responseMissing) {						
						GroupCirclesProbeResponse groupCircles = ((Task_2_ProbeTrial)trial).getTrialResponse().getGroupCirclesResponse();
						if(groupCircles != null && groupCircles.getGroupCircles() != null && 
								groupCircles.getGroupCircles().size() == trial.getAttackLocationProbe().getGroups().size()) {
							EnumMap<GroupType, Task_1_2_3_AttackDispersionParameters> subjectParameters = 
									new EnumMap<GroupType, Task_1_2_3_AttackDispersionParameters>(GroupType.class);
							for(GroupCircle groupCircle : groupCircles.getGroupCircles()) {
								Task_1_2_3_AttackDispersionParameters params = dispersionParameters.get(groupCircle.getGroup());
								if(params != null) {
									//Use the actual base rate, the subject center as mean X,Y and the subject radius/1.33 as sigma X,Y
									subjectParameters.put(groupCircle.getGroup(), 
											new Task_1_2_3_AttackDispersionParameters(groupCircle.getGroup(), params.getBaseRate(),
													groupCircle.getCenterLocation(),
													groupCircle.getRadius()/1.33, groupCircle.getRadius()/1.33, 0));
								}							
							}							
							incrementalProbs = computeGroupProbabilities_Task_1_2(trial.getAttackLocationProbe().getAttackLocation(), 
									trial.getAttackLocationProbe().getGroups(), rules, subjectParameters);							
						}
					}
				} else if(trial instanceof Task_3_ProbeTrial) {
					Task_3_ProbeTrial task3Trial = (Task_3_ProbeTrial)trial;					
					if(dispersionParameters == null) {
						//Computer "best-fit" dispersion parameters and group centers
						ArrayList<GroupType> groups = trial.getAttackLocationProbe().getGroups();
						dispersionParameters = computeGroupParameters_Task_3(groups, groupAttacks, gridSize,
								distanceCalculator, rules.getHumintRules(), useMaxLikelihoods);
						trial.setAttackDispersionParameters(new ArrayList<Task_1_2_3_AttackDispersionParameters>(
								dispersionParameters.values()));
					}
					//Compute probabilities based on "best fit" group centers		
					if(task3Trial.getCentersToAttackDistances() == null || 
							task3Trial.getCentersToAttackDistances().size() != trial.getAttackLocationProbe().getGroups().size()) {
						task3Trial.setCentersToAttackDistances(
								ProbabilityUtils.createProbabilities_Double(trial.getAttackLocationProbe().getGroups().size(), 0D));
					}
					cumulativeProbs = computeGroupProbabilities_Task_3(trial.getAttackLocationProbe().getAttackLocation(), 
							trial.getAttackLocationProbe().getGroups(), rules, gridSize, distanceCalculator, 
							dispersionParameters, task3Trial.getCentersToAttackDistances());
					//Compute probabilities based on subject group centers
					if(computeIncrementalProbabilities && computeScore && !responseMissing) {
						GroupCentersProbeResponse groupCenters = task3Trial.getTrialResponse().getGroupCentersResponse();
						if(groupCenters != null && groupCenters.getGroupCenters() != null && 
								groupCenters.getGroupCenters().size() == trial.getAttackLocationProbe().getGroups().size()) {
							EnumMap<GroupType, Task_1_2_3_AttackDispersionParameters> subjectParameters = 
									new EnumMap<GroupType, Task_1_2_3_AttackDispersionParameters>(GroupType.class);
									for(GroupCenterResponse groupCenter : groupCenters.getGroupCenters()) {
										Task_1_2_3_AttackDispersionParameters params = dispersionParameters.get(groupCenter.getGroup());
										if(params != null) {
											subjectParameters.put(groupCenter.getGroup(), 
													new Task_1_2_3_AttackDispersionParameters(groupCenter.getGroup(), params.getBaseRate(),
															groupCenter.getLocation()));
										}							
									}
									if(task3Trial.getTrialResponse().getCentersToAttackDistances() == null || 
											task3Trial.getTrialResponse().getCentersToAttackDistances().size() != 
											trial.getAttackLocationProbe().getGroups().size()) {
										task3Trial.getTrialResponse().setCentersToAttackDistances(
												ProbabilityUtils.createProbabilities_Double(trial.getAttackLocationProbe().getGroups().size(), 0D));
									}
									incrementalProbs = computeGroupProbabilities_Task_3(trial.getAttackLocationProbe().getAttackLocation(), 
											trial.getAttackLocationProbe().getGroups(), rules, gridSize, distanceCalculator, 
											subjectParameters, task3Trial.getTrialResponse().getCentersToAttackDistances());
						}
					}
				}
			} catch(Exception ex) {
				ex.printStackTrace();
				errors.append("Error computing normative probabilities for group probe: " + ex.toString());
			}
			
			if(cumulativeProbs != null && !cumulativeProbs.isEmpty()) {
				response.getAttackLocationResponse().setNormativeProbs_cumulativeBayesian(cumulativeProbs);
				if(computeScore && groupProbeScorable) {
					//Scoring is based on the "cumulative" probabilities computed using the "best fit" group centers
					feedback.setProbabilitiesScore_s1(
							computeProbabilitiesScoreS1_FromResponses(
									response.getAttackLocationResponse().getGroupAttackProbabilities(), cumulativeProbs));				
					response.getAttackLocationResponse().setProbabilitiesScore_s1(feedback.getProbabilitiesScore_s1());
				}
				if(DEBUG) {
					System.out.print("Cumulative Probabilities: ");
					int i = 0;
					for(GroupType group : trial.getAttackLocationProbe().getGroups()) {
						System.out.print(group + ": " + cumulativeProbs.get(i) + ", ");
						i++;
					}
					System.out.println("Negentropy: " + computeNegentropy(cumulativeProbs, ProbabilityType.Percent));
				}
			}
			if(incrementalProbs != null && !incrementalProbs.isEmpty()) {
				response.getAttackLocationResponse().setNormativeProbs_incrementalBayesian(incrementalProbs);				
				if(DEBUG) {
					System.out.print("Incremental Probabilities: ");
					int i = 0;
					for(GroupType group : trial.getAttackLocationProbe().getGroups()) {
						System.out.print(group + ": " + incrementalProbs.get(i) + ", ");
						i++;
					}
					System.out.println();
				}
			}
		}

		//Compute the troop allocation score for the trial (S2)	
		if(computeScore && troopSelectionScorable) {
			feedback.setGroundTruth(trial.getGroundTruth());
			feedback.setTroopAllocationScore_s2(computeTroopAllocationScoreS2_SingleGroupAllocation(
					response.getTroopSelectionResponse().getGroup(), 
					trial.getGroundTruth().getResponsibleGroup()));
			response.getTroopSelectionResponse().setTroopAllocationScore_s2(feedback.getTroopAllocationScore_s2());
		}
		feedback.setErrors(errors.toString());
		return feedback;
	}
	
	/**
	 * Computes the base rate for each group and fits a 2D Guassian function for each group based on the 
	 * distribution of attack locations and uses those base rates and Guassians to compute attack likelihoods 
	 * for each group in Tasks 1 and 2.
	 * 
	 * @param location the location to compute attack likelihoods for
	 * @param groups the groups 
	 * @param rules the probability rules may contain the probability updating strategy to use. If null, Bayesian updating is used.
	 * @param groupAttacks the distribution of attacks that have been observed
	 * @return the attack likelihoods for each group attacking the given location
	 * @throws ScoreComputerException
	 */
	public static ArrayList<Double> computeGroupProbabilities_Task_1_2(GridLocation2D location, List<GroupType> groups,
			ProbabilityRules rules, List<AttackLocationPresentationTrial> groupAttacks) throws ScoreComputerException {		
		return computeGroupProbabilities_Task_1_2(location, groups, rules,
				computeGroupParameters_Task_1_2(groups, groupAttacks));
	}
	
	/**
	 * Computes the attack dispersion parameters (meanX, meanY, sigmaX, sigmaY) for each group given
	 * the distribution of attacks that have been observed.
	 * Note 1: The previous way was to compute sigmaX and sigmaY the same by averaging the actual sigmaX and sigmaY values.
	 *         The new way is to compute sigmaXY by treating the x and y values as a single collection of values. Both sigmaX and sigmaY are set to sigmaXY.
	 * 
	 * @param groups the groups
	 * @param groupAttacks the distribution of attacks that have been observed
	 * @return the attack dispersion parameters for each group
	 */
	public static EnumMap<GroupType, Task_1_2_3_AttackDispersionParameters> computeGroupParameters_Task_1_2(List<GroupType> groups,
			List<AttackLocationPresentationTrial> groupAttacks) throws ScoreComputerException {
		//Compute the mean x,y values and base rates for each group
		EnumMap<GroupType, Task_1_2_3_AttackDispersionParameters> groupParameters = 
				new EnumMap<GroupType, Task_1_2_3_AttackDispersionParameters>(GroupType.class);
		EnumMap<GroupType, GroupAttackDispersionParameters> dispersionParameters = 
				new EnumMap<GroupType, GroupAttackDispersionParameters>(GroupType.class);		
		try {
			for(GroupType group : groups) {
				dispersionParameters.put(group, new GroupAttackDispersionParameters());
				groupParameters.put(group, new Task_1_2_3_AttackDispersionParameters(group));
			}
			for(AttackLocationPresentationTrial groupAttack : groupAttacks) {
				GridLocation2D location = groupAttack.getGroupAttack().getLocation();
				GroupAttackDispersionParameters parameters = dispersionParameters.get(groupAttack.getGroupAttack().getGroup()); 
				parameters.meanX += location.x;
				parameters.meanY += location.y;
				parameters.numAttacks++;
			}		
			ArrayList<Double> baseRates = new ArrayList<Double>(groups.size());
			for(GroupType group : groups) {
				GroupAttackDispersionParameters parameters = dispersionParameters.get(group);
				if(parameters.numAttacks > 0) {
					parameters.meanX = parameters.meanX/parameters.numAttacks;
					parameters.meanY = parameters.meanY/parameters.numAttacks;
				}
				baseRates.add((double)parameters.numAttacks);
			}
			ProbabilityUtils.normalizePercentProbabilities_Double(baseRates, baseRates);
			for(int i=0; i<baseRates.size(); i++) {
				baseRates.set(i, baseRates.get(i)/100d);
			}

			//Compute standard deviations (sigmas) for each group and create group parameters instances		
			for(AttackLocationPresentationTrial groupAttack : groupAttacks) {
				GridLocation2D location = groupAttack.getGroupAttack().getLocation();
				GroupAttackDispersionParameters parameters = dispersionParameters.get(groupAttack.getGroupAttack().getGroup());
				//Treat the x, y coordinates as a single set to comptue sigmaXY
				parameters.sigmaXY += StrictMath.pow(location.getX() - parameters.meanX, 2) + 
						StrictMath.pow(location.getY() - parameters.meanY, 2);
				//We previously computed sigmaX and sigmaY separately and averaged them
				//parameters.sigmaX += StrictMath.pow(location.getX() - parameters.meanX, 2);
				//parameters.sigmaY += StrictMath.pow(location.getY() - parameters.meanY, 2);				
			}
			int i = 0;
			for(GroupType group : groups) {
				GroupAttackDispersionParameters parameters = dispersionParameters.get(group);
				if(parameters.numAttacks > 0) {
					parameters.sigmaXY = StrictMath.sqrt(parameters.sigmaXY/(parameters.numAttacks*2));
					//We previously took the average of sigma x and sigma y
					//double sigma = (parameters.sigmaX + parameters.sigmaY)/2;  
					//parameters.sigmaX = sigma;
					//parameters.sigmaY = sigma;
				}
				Task_1_2_3_AttackDispersionParameters groupParams = new Task_1_2_3_AttackDispersionParameters(group, baseRates.get(i),
						new GridLocation2D(parameters.meanX, parameters.meanY), 
						parameters.sigmaXY, parameters.sigmaXY, 0d);
				groupParameters.put(group, groupParams);
				i++;
			}	
		} catch(Exception ex) {
			//ex.printStackTrace();
			throw new ScoreComputerException("Error computing attack dispersion parameters: " + ex.toString());
		}
		return groupParameters;
	}
	
	/**
	 * Uses the given attack dispersion parameters (mean X, mean Y, sigma X, sigma Y)
	 * to compute attack likelihoods for each group in Tasks 1 and 2.
	 * 
	 * @param location the location to compute attack likelihoods for
	 * @param groups the groups
	 * @param rules the probability rules may contain the probability updating strategy to use. If null, Bayesian updating is used.
	 * @param groupParameters the dispersion parameters for each group
	 * @return the attack likelihoods for each group attacking the given location
	 * @throws ScoreComputerException
	 */
	public static ArrayList<Double> computeGroupProbabilities_Task_1_2(GridLocation2D location, List<GroupType> groups,
			ProbabilityRules rules,
			Map<GroupType, Task_1_2_3_AttackDispersionParameters> groupParameters) throws ScoreComputerException {
		ArrayList<Double> bayesianProbs = null;
		try {
			if(groups != null && !groups.isEmpty()) {
				ProbabilityUpdateStrategy updateStrategy = rules.getProbabilityUpdateStrategy();
				if(updateStrategy == null) {
					updateStrategy = new BayesianUpdateStrategy();
					rules.setProbabilityUpdateStrategy(updateStrategy);
				}
				
				//Compute likelihoods using dispersion parameters for each group and the attack location,
				//and get the base rates
				ArrayList<Double> likelihoods = new ArrayList<Double>(groups.size());
				ArrayList<Double> baseRates = new ArrayList<Double>(groups.size());
				for(GroupType group : groups) {
					Task_1_2_3_AttackDispersionParameters params = groupParameters.get(group);
					baseRates.add(params.getBaseRate());
					//Compute attack likelihood from given group parameters
					double likelihood = params.computePxy(location);					
					if(Double.isNaN(likelihood)) {
						likelihoods.add(0d);
					} else {
						likelihoods.add(likelihood);						
					}
					if(DEBUG) {
						System.out.println("Group " + group + ": baseRate:" + 
								params.getBaseRate() + ", likelihood: " + likelihood +  
								", location: (" + params.getCenterLocation().getX() + "," + 
								params.getCenterLocation().getY() + "), sigmas: (" + params.getSigmaX() + "," + 
								params.getSigmaY() + ")");
					}
				}
				//Normalize likelihoods
				//ProbabilityUtils.normalizeProbabilitiesSumTo1_Double(likelihoods, likelihoods);
				
				//Compute posteriors
				bayesianProbs = updateStrategy.computePosteriors(baseRates, likelihoods);
				
				//Normalize probabilities
				ProbabilityUtils.normalizePercentProbabilities_Double(bayesianProbs, bayesianProbs);
				
				/*bayesianProbs = new ArrayList<Double>(groups.size());
				int index = 0;
				for(GroupType group : groups) {
					Task_1_2_3_AttackDispersionParameters params = groupParameters.get(group);
					Double posterior = 0d;
					Double likelihood = likelihoods.get(index);
					if(!Double.isNaN(likelihood)) {						
						//bayesianProbs.add(likelihood * params.getBaseRate());
						posterior = updateStrategy.computePosterior(params.getBaseRate(), likelihood);
					}					
					bayesianProbs.add(posterior);
					index++;
					if(DEBUG) {
						System.out.println("Group " + group + ": baseRate:" + 
								params.getBaseRate() + ", likelihood: " + likelihood + ", posterior: " + posterior + 
								", location: (" + params.getCenterLocation().getX() + "," + 
								params.getCenterLocation().getY() + "), sigmas: (" + params.getSigmaX() + "," + 
								params.getSigmaY() + ")");
					}					
				}*/
				//Normalize probabilities
				//ProbabilityUtils.normalizeProbabilities_Double(bayesianProbs, bayesianProbs);	
				if(DEBUG) {
					System.out.println("Posteriors: " + bayesianProbs);
				}
			}		
		} catch(Exception ex) {
			//ex.printStackTrace();
			throw new ScoreComputerException("Error computing probabilities: " + ex.toString(), ex);
		}
		return bayesianProbs;
	}
	
	/**
	 * Computes the base rates and "best fit" center locations for each group given the distribution of attack locations. 
	 * The algorithm computes the "best fit" center location by choosing a road point that maximizes
	 * the mean likelihood of the distance from road point to each attack location computed using the HUMINT Gaussian if 
	 * useMaxeLikelhoods is true. Otherwise, it chooses a road point that minimizes the mean distance from the road point 
	 * to each attack location. It then uses those base rates, center locations, and the HUMINT Gaussian to compute attack 
	 * likelihoods for each group in Task 3.	 * 
	 * 
	 * @param location the location to compute attack likelihoods for
	 * @param groups the groups
	 * @param rules the probability rules contains the Humint Gaussian to use. May also contain the probability updating strategy to use. If null, Bayesian updating is used.
	 * @param gridSize the grid size
	 * @param distanceCalculator the road distance calculator
	 * @param groupAttacks the distribution of attacks that have been observed
	 * @param useMaxLikelihoods if false, algorithm uses min distances to each attack location as the optimization constraint, otherwise use max likelihoods
	 * @param distances the computed distances from the group centers to the attack location are returned in this array if it isn't null and it is the same size as the groups array. Units are miles.
	 * @return the attack likelihoods for each group attacking the given location
	 * @throws ScoreComputerException
	 */
	public static ArrayList<Double> computeGroupProbabilities_Task_3(GridLocation2D location, 
			List<GroupType> groups,
			ProbabilityRules rules, GridSize gridSize, IRoadDistanceCalculator distanceCalculator,
			List<AttackLocationPresentationTrial> groupAttacks, boolean useMaxLikelihoods, 
			List<Double> distances) throws ScoreComputerException {
		return computeGroupProbabilities_Task_3(location, groups, rules, 
				gridSize, distanceCalculator,
				computeGroupParameters_Task_3(groups, groupAttacks, gridSize, 
						distanceCalculator, rules.getHumintRules(), useMaxLikelihoods),
				distances);
	}	
	
	/**
	 * Computes the base rates and "best fit" center locations for each group given the distribution of attack locations. 
	 * The algorithm computes the "best fit" center location by choosing a road point that maximizes
	 * the mean likelihood of the distance from road point to each attack location computed using the HUMINT Gaussian if 
	 * useMaxeLikelhoods is true. Otherwise, it chooses a road point that minimizes the mean distance from the road point 
	 * to each attack location. 
	 * 
	 * @param groups the groups
	 * @param groupAttacks distribution of attacks that have been observed
	 * @param distanceCalculator the road distance calculator
	 * @param humintRules the HUMINT Gaussian
	 * @param useMaxLikelihoods if false, algorithm uses min distances to each attack location as the optimization constraint, otherwise use max likelihoods
	 * @return the base rates and best fit center locations for each group
	 */
	public static EnumMap<GroupType, Task_1_2_3_AttackDispersionParameters> computeGroupParameters_Task_3(List<GroupType> groups,
			List<AttackLocationPresentationTrial> groupAttacks, GridSize gridSize, IRoadDistanceCalculator distanceCalculator,
			HumintRules humintRules, boolean useMaxLikelihoods) throws ScoreComputerException {		
		ArrayList<Double> baseRates = new ArrayList<Double>(groups.size());
		boolean baseRatesComputed = false;
		//Use min distances or max likelihoods as the optimization constraint (currently using max likelihoods)
		ArrayList<GroupAttackDispersionParameters> bestDispersionParameters = new ArrayList<GroupAttackDispersionParameters>(groups.size());
		ArrayList<Double> minDistances = new ArrayList<Double>(groups.size());
		ArrayList<Double> maxLikelihoods = new ArrayList<Double>(groups.size());
		for(int i=0; i<groups.size(); i++) {
			minDistances.add(Double.MAX_VALUE);
			maxLikelihoods.add(Double.MIN_VALUE);
			bestDispersionParameters.add(new GroupAttackDispersionParameters());
		}
			
		try {
			//Iterate over all road points and evaluate them as candidate group centers	
			for(Road road : distanceCalculator.getRoads()) {
				for(GridLocation2D location : road.getVertices()) {
					//Compute the mean, sigma, and likelihood of the distances to all attack locations from the road point for each group					
					EnumMap<GroupType, GroupAttackDispersionParameters> dispersionParameters = 
							new EnumMap<GroupType, GroupAttackDispersionParameters>(GroupType.class);					
					ArrayList<Double> distances = new ArrayList<Double>(groupAttacks.size());
					for(GroupType group : groups) {
						dispersionParameters.put(group, new GroupAttackDispersionParameters());
					}
					for(AttackLocationPresentationTrial groupAttack : groupAttacks) {
						GroupAttackDispersionParameters parameters = dispersionParameters.get(groupAttack.getGroupAttack().getGroup());
						/*if(DEBUG) {							
							System.out.println("Computing distance for location: " + groupAttack.getGroupAttack().getLocation() + ", closest road point: " + 
									distanceCalculator.getClosestLocationOnRoad(groupAttack.getGroupAttack().getLocation()));
						}*/
						Double distance = null;
						try {
							distance = distanceCalculator.computeShortestDistanceFromStartLocationToEndLocation(						
								location, groupAttack.getGroupAttack().getLocation());
						} catch(Exception ex) {ex.printStackTrace();}						
						if(distance != null && !distance.isNaN()) {
							distance = gridSize.toMiles(distance);
							distances.add(distance);
							parameters.likelihoodXY += humintRules.getAttackLikelihood(distance);
							parameters.totalDistance += distance;
							parameters.meanX += distance;							
							parameters.numAttacks++;
						} else {
							distances.add(null);
							if(DEBUG) {
								System.out.println("Warning, distance null for Location: " + groupAttack.getGroupAttack().getLocation() + 
										" to Road point: " + location);
							}
						}
					}
					for(GroupType group : groups) {
						GroupAttackDispersionParameters parameters = dispersionParameters.get(group);
						parameters.meanX = parameters.meanX/parameters.numAttacks;
						parameters.likelihoodXY = parameters.likelihoodXY/parameters.numAttacks;
						if(!baseRatesComputed) {
							baseRates.add((double)parameters.numAttacks);
						}
					}
					if(!baseRatesComputed) {
						ProbabilityUtils.normalizePercentProbabilities_Double(baseRates, baseRates);
						for(int i=0; i<baseRates.size(); i++) {
							baseRates.set(i, baseRates.get(i)/100d);
						}
						baseRatesComputed = true;
					}
					
					//Compute sigmas
					int i = 0;
					for(AttackLocationPresentationTrial groupAttack : groupAttacks) {
						GroupAttackDispersionParameters parameters = dispersionParameters.get(groupAttack.getGroupAttack().getGroup());		
						Double distance = distances.get(i);
						if(distance != null) {
							parameters.sigmaX += StrictMath.pow(distances.get(i) - parameters.meanX, 2);
						}
						i++;
					}

					//Determine if this road point is the best point so far for each group
					i = 0;
					for(GroupType group : groups) {
						GroupAttackDispersionParameters parameters = dispersionParameters.get(group);
						
						if(parameters.numAttacks > 0) {
							parameters.sigmaX = StrictMath.sqrt(parameters.sigmaX/parameters.numAttacks);
						}
				
						if(useMaxLikelihoods) {
							//Use this point if it maximizes the mean likelihood (computed using HUMINT rules) of the distances to all attacks
							if(parameters.likelihoodXY > maxLikelihoods.get(i)) {
								maxLikelihoods.set(i, parameters.likelihoodXY);
								parameters.centerX = location.getX();
								parameters.centerY = location.getY();
								bestDispersionParameters.set(i, parameters);
							}
						} else {						
							//Use this point if its total distance to all attacks is the shortest so far
							if(parameters.totalDistance < minDistances.get(i)) {
								minDistances.set(i, parameters.totalDistance);
								parameters.centerX = location.getX();
								parameters.centerY = location.getY();
								bestDispersionParameters.set(i, parameters);
							}	
						}
						
						i++;
					}								
				}
			}

			EnumMap<GroupType, Task_1_2_3_AttackDispersionParameters> groupParameters = new EnumMap<GroupType, Task_1_2_3_AttackDispersionParameters>(GroupType.class);
			int i = 0;
			for(GroupType group : groups) {
				GroupAttackDispersionParameters parameters = bestDispersionParameters.get(i);
				Task_1_2_3_AttackDispersionParameters groupParams = 
						new Task_1_2_3_AttackDispersionParameters(group, baseRates.get(i), 
								new GridLocation2D(group.toString(), parameters.centerX, parameters.centerY));
				if(DEBUG) {
					System.out.println("Best road point for Group " +  group + ": (" + 
							parameters.centerX + "," + parameters.centerY + 
							"), mean distance: " + parameters.meanX + ", sigma: " + parameters.sigmaX +
							", mean likelihood: " + parameters.likelihoodXY);
				}
				groupParameters.put(group, groupParams);
				i++;
			}		
			return groupParameters;
		} catch(Exception ex) {
			//ex.printStackTrace();
			throw new ScoreComputerException("Error computing attack dispersion parameters: " + ex.toString(), ex);
		}
	}
	
	/**
	 * Uses the group centers and base rates from the given attack dispersion parameters 
	 * and the HUMINT Gaussian to compute attack likelihoods for each group in Task 3.
	 * 
	 * @param location the location to compute attack likelihoods for
	 * @param groups the groups
	 * @param rules the probability rules contains the Humint Gaussian to use. May also contain the probability updating strategy to use. If null, Bayesian updating is used.
	 * @param gridSize the grid size
	 * @param roads the roads
	 * @param groupParameters the group centers and base rates for each group
	 * @param distances the computed distances from the group centers to the attack location are returned in this array if it isn't null and it is the same size as the groups array. Units are miles.
	 * @return the attack likelihoods for each group attacking the given location
	 */
	public static ArrayList<Double> computeGroupProbabilities_Task_3(GridLocation2D location, List<GroupType> groups, 
			ProbabilityRules rules, GridSize gridSize, List<Road> roads,
			Map<GroupType, Task_1_2_3_AttackDispersionParameters> groupParameters,
			List<Double> distances) throws ScoreComputerException {
		return computeGroupProbabilities_Task_3(location, groups, rules, gridSize,
				new RoadDistanceCalculator(roads, gridSize), groupParameters, distances);
	}
	
	/**
	 * Uses the group centers and base rates from the given attack dispersion parameters 
	 * and the HUMINT Gaussian to compute attack likelihoods for each group in Task 3.
	 * 
	 * @param location the location to compute attack likelihoods for
	 * @param groups the groups
	 * @param rules the probability rules contains the Humint Gaussian to use. May also contain the probability updating strategy to use. If null, Bayesian updating is used.
	 * @param gridSize the grid size
	 * @param distanceCalculator the road distance calculator
	 * @param groupParameters the group centers and base rates for each group
	 * @param distances the computed distances from the group centers to the attack location are returned in this array if it isn't null and it is the same size as the groups array. Units are miles.
	 * @return the attack likelihoods for each group attacking the given location
	 * @throws ScoreComputerException
	 */
	public static ArrayList<Double> computeGroupProbabilities_Task_3(GridLocation2D location, List<GroupType> groups, 
			ProbabilityRules rules, GridSize gridSize, IRoadDistanceCalculator distanceCalculator, 
			Map<GroupType, Task_1_2_3_AttackDispersionParameters> groupParameters,
			List<Double> distances) throws ScoreComputerException {
		ArrayList<Double> bayesianProbs = null;
		try {
			if(groups != null && !groups.isEmpty()) {
				ProbabilityUpdateStrategy updateStrategy = rules.getProbabilityUpdateStrategy();
				if(updateStrategy == null) {
					updateStrategy = new BayesianUpdateStrategy();
					rules.setProbabilityUpdateStrategy(updateStrategy);
				}
				
				//Compute likelihoods based on road network distances from group centers to the attack location,
				//and get the base rates				
				ArrayList<Double> likelihoods = new ArrayList<Double>(groups.size());
				ArrayList<Double> baseRates = new ArrayList<Double>(groups.size());
				int i = 0;
				for(GroupType group : groups) {					
					Task_1_2_3_AttackDispersionParameters params = groupParameters.get(group);
					baseRates.add(params.getBaseRate());
					//Compute road network distance (in miles) from group center to location				
					Double distance = distanceCalculator.computeShortestDistanceFromStartLocationToEndLocation(							
							params.getCenterLocation(), location) * gridSize.getMilesPerGridUnit();
					//Compute attack probability using likelihood from Humint rule
					likelihoods.add(rules.getHumintLikelihood(distance));
					if(distances != null && i < distances.size()) {
						distances.set(i, distance);
					}
					if(DEBUG) {						
						System.out.println("Group " + group + ": baseRate:" + 
								params.getBaseRate() + ", likelihood: " + likelihoods.get(i) + ", group center: (" + params.getCenterLocation().getX() + "," + 
								params.getCenterLocation().getY() + "), distance: " + distance);
					}
				}				
				
				//Compute posteriors
				bayesianProbs = updateStrategy.computePosteriors(baseRates, likelihoods);
				
				//Normalize probabilities
				ProbabilityUtils.normalizePercentProbabilities_Double(bayesianProbs, bayesianProbs);
				
				/*i = 0;
				bayesianProbs = new ArrayList<Double>(groups.size());			
				for(GroupType group : groups) {
					Task_1_2_3_AttackDispersionParameters params = groupParameters.get(group);									
					//bayesianProbs.add(likelihood * params.getBaseRate());
					bayesianProbs.add(updateStrategy.computePosterior(params.getBaseRate(), likelihoods.get(i)));
					if(DEBUG) {
						double distance = 0;
						distance = (distances != null) ? distances.get(i) : distanceCalculator.computeShortestDistanceFromStartLocationToEndLocation(							
								params.getCenterLocation(), location) * gridSize.getMilesPerGridUnit();
						System.out.println("Group " + group + ": baseRate:" + 
								params.getBaseRate() + ", likelihood: " + likelihoods.get(i) + ", group center: (" + params.getCenterLocation().getX() + "," + 
								params.getCenterLocation().getY() + "), distance: " + distance);
					}
					i++;
				}*/
				//Normalize probabilities
				//ProbabilityUtils.normalizeProbabilities_Double(bayesianProbs, bayesianProbs);
				if(DEBUG) {
					System.out.println("Posteriors: " + bayesianProbs);
				}
			}
		} catch(Exception ex) {
			//ex.printStackTrace();
			throw new ScoreComputerException("Error computing probabilities: " + ex.toString(), ex);
		}
		return bayesianProbs;		
	}	
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment.phase_1.score_computer.IScoreComputer#computeSolutionAndScoreForTrial(org.mitre.icarus.cps.exam.phase_1.testing.Task_4_Trial, org.mitre.icarus.cps.exam.phase_1.testing.probability_rules.ProbabilityRules, org.mitre.icarus.cps.feature_vector.phase_1.GridSize, boolean)
	 */
	@Override
	public TrialFeedback_Phase1 computeSolutionAndScoreForTrial(Task_4_Trial trial, ProbabilityRules rules, GridSize gridSize, boolean computeScore) {
		ErrorString errors = new ErrorString();
		TrialFeedback_Phase1 feedback = new TrialFeedback_Phase1();
		boolean initialLocationProbeScorable = true;
		boolean initialLocationProbeComputable = true;
		//boolean intLayerProbesScorable = true;
		boolean intLayerProbesComputable = true;
		boolean responseMissing = false;
		boolean distancesComputable = true;
		if(computeScore && trial.getTrialResponse() == null) {
			errors.append("Trial response missing, cannot compute S1 or S2 score");
			responseMissing = true;
			initialLocationProbeScorable = false;
			//intLayerProbesScorable = false;
		}
		if(trial.getGroupCenter() == null || trial.getGroupCenter().getLocation() == null) {
			errors.append("Trial missing group center, cannot compute distances from group center to attack locations");
			distancesComputable = false;
		}
		if(trial.getPossibleAttackLocations() == null || trial.getPossibleAttackLocations().isEmpty()) {
			errors.append("Trial missing attack locations, cannot compute distances from group center to attack locations");
			distancesComputable = false;
		}
		if(trial.getAttackLocationProbe_initial() == null || trial.getAttackLocationProbe_initial().getLocations() == null ||
				trial.getAttackLocationProbe_initial().getLocations().isEmpty()) {
			errors.append("Trial missing location probe, cannot compute the normative solution or probabilities score S1");
			initialLocationProbeScorable = false;
			initialLocationProbeComputable = false;
		}
		if(trial.getIntLayers() == null || trial.getIntLayers().isEmpty()) {
			errors.append("Trial missing INT layers");
			//intLayerProbesScorable = false;
			intLayerProbesComputable = false;
		}
		
		if(trial.getTrialResponse() == null) {
			//return feedback;
			Task_4_TrialResponse response = new Task_4_TrialResponse();
			trial.setTrialResponse(response);
			response.setAttackLocationResponse_initial(new AttackLocationProbeResponse_MultiLocation());
			if(intLayerProbesComputable) {
				ArrayList<Task_4_7_AttackLocationProbeResponseAfterINT> intResponses = 
						new ArrayList<Task_4_7_AttackLocationProbeResponseAfterINT>(trial.getIntLayers().size());
				response.setAttackLocationResponses_afterINTs(intResponses);
				for(Task_4_INTLayerPresentationProbe layer : trial.getIntLayers()) {
					Task_4_7_AttackLocationProbeResponseAfterINT intResponse = new Task_4_7_AttackLocationProbeResponseAfterINT();
					intResponse.setIntLayerShown(new INTLayerData(layer.getLayerType(), false));
					intResponse.setAttackLocationResponse(new AttackLocationProbeResponse_MultiLocation());
					intResponses.add(intResponse);						
				}
			}
		}		
		Task_4_TrialResponse response = trial.getTrialResponse();
		
		if(computeScore && !responseMissing) {
			if(response.getAttackLocationResponse_initial() == null) {
				errors.append("Response to the inital location probe missing");
				initialLocationProbeScorable = false;
			}
			else if(response.getAttackLocationResponse_initial().getGroupAttackProbabilities() == null ||
					response.getAttackLocationResponse_initial().getGroupAttackProbabilities().size() !=
					trial.getAttackLocationProbe_initial().getLocations().size()) {
				errors.append("Not enough probabilities in the initial location probe response");
				initialLocationProbeScorable = false;
			}		
		}
		
		//Compute distances from each location to the group center if they haven't been computed yet (HUMINT)
		if(DEBUG && trial.getCenterToAttackDistances() != null) {
			System.out.println("Distances from file: " + trial.getCenterToAttackDistances());
		}
		if(trial.getCenterToAttackDistances() == null || trial.getCenterToAttackDistances().isEmpty()) {
			if(distancesComputable) {
				try {
				trial.setCenterToAttackDistances(computeCenterToAttackDistances(trial.getGroupCenter().getLocation(), 
						trial.getPossibleAttackLocations(), gridSize, 
						new RoadDistanceCalculator(trial.getRoads(), gridSize)));
				if(DEBUG) {
					System.out.println("Distances from distance calculator: " + trial.getCenterToAttackDistances());	
				}	
				} catch(Exception ex) {
					ex.printStackTrace();
					errors.append("Distances from the group center to the attack locations could not be computed, details: " + ex.toString());
					initialLocationProbeComputable = false;
				}
			} else {
				errors.append("Distances from the group center to the attack locations could not be computed");
				initialLocationProbeComputable = false;
			}
		} else if(DEBUG && distancesComputable) {
			try {
				System.out.println("Distances from distance calculator: " + 
						computeCenterToAttackDistances(trial.getGroupCenter().getLocation(), 
								trial.getPossibleAttackLocations(), gridSize, 
								new RoadDistanceCalculator(trial.getRoads(), gridSize)));
			} catch(Exception ex) {}
		}

		//Compute the initial cumulative Bayesian probabilities based on HUMINT distances
		ArrayList<Double> cumulativeProbs = null;
		if(initialLocationProbeComputable) {
			cumulativeProbs = ProbabilityUtils.createDefaultInitialProbabilities_Double(
					trial.getAttackLocationProbe_initial().getLocations().size()); //the current cumulative Bayesian probs
			try {
				updateLocationProbabilities_Task_4(trial.getPossibleAttackLocations(), trial.getGroupCenter(), 
						trial.getCenterToAttackDistances(), new HumintLayer(), cumulativeProbs, null, rules);
				response.getAttackLocationResponse_initial().setNormativeProbs_cumulativeBayesian(				
						ProbabilityUtils.cloneProbabilities_Double(cumulativeProbs));
				if(DEBUG) {
					System.out.println("Initial probabilities given HUMINT: " + cumulativeProbs);
				}
			} catch(ScoreComputerException e) {
				e.printStackTrace();
				errors.append("Error computing the initial normative probabilities based on HUMINT distances:" + e.toString());
				initialLocationProbeScorable = false;
				initialLocationProbeComputable = false;
				//intLayerProbesScorable = false;
				intLayerProbesComputable = false;
			}
		}
		else {
			//intLayerProbesScorable = false;
			intLayerProbesComputable = false;
		}
		
		//Initialize the current incremental Bayesian probabilities based on the initial location probe response
		ArrayList<Double> incrementalProbs = null;
		if(computeIncrementalProbabilities && computeScore && initialLocationProbeScorable && cumulativeProbs != null) {
			incrementalProbs = new ArrayList<Double>(cumulativeProbs.size()); //the current incremental Bayesian probs 
			for(GroupAttackProbabilityResponse prob : response.getAttackLocationResponse_initial().getGroupAttackProbabilities()) {
				incrementalProbs.add(prob.getProbability());
			}
			ProbabilityUtils.normalizePercentProbabilities_Double(incrementalProbs, incrementalProbs);
			response.getAttackLocationResponse_initial().setNormativeProbs_incrementalBayesian(
					ProbabilityUtils.cloneProbabilities_Double(incrementalProbs));	
		} else {
			response.getAttackLocationResponse_initial().setNormativeProbs_incrementalBayesian(null);
		}
		
		//Compute the score for the initial location probe based on the cumulative Bayesian probabilities
		int numScoreParts = 0;
		Double scoreSum = 0D;
		if(computeScore && initialLocationProbeScorable) {
			numScoreParts = 1;
			Double score = computeProbabilitiesScoreS1_FromResponses(
					response.getAttackLocationResponse_initial().getGroupAttackProbabilities(), cumulativeProbs);
			scoreSum = score;
			response.getAttackLocationResponse_initial().setProbabilitiesScore_s1(score);
		}
		
		//Compute the updated probabilities and score after each INT presentation
		if(response.getAttackLocationResponses_afterINTs() == null) {
			errors.append("Response to the probes after INT layers missing");
			//intLayerProbesScorable = false;
			intLayerProbesComputable = false;
		}		
		if(response.getAttackLocationResponses_afterINTs().size() != trial.getIntLayers().size()) {
			errors.append("Responses to the probes after INT layers does not match number of INT layers");
			//intLayerProbesScorable = false;
			intLayerProbesComputable = false;
		}
		if(intLayerProbesComputable) {  //intLayerProbesScorable) {
			int index = 0;
			for(Task_4_7_AttackLocationProbeResponseAfterINT intResponse : response.getAttackLocationResponses_afterINTs()) {
				Task_4_INTLayerPresentationProbe probe = trial.getIntLayers().get(index);
				boolean intLayerProbeScorable = true;
				boolean intLayerProbeComputable = true;
				if(probe.getLayerType() == null) {
					errors.append("Missing INT layer type");
					intLayerProbeScorable = false;
					intLayerProbeComputable = false;
				}
				if(probe.getAttackLocationProbe() == null || probe.getAttackLocationProbe().getLocations() == null ||
						probe.getAttackLocationProbe().getLocations().isEmpty()) {
					errors.append("Missing location probe after " + probe.getLayerType().getLayerType() + " layer");
					intLayerProbeScorable = false;
					intLayerProbeComputable = false;
				}
				if(intResponse.getIntLayerShown() == null || intResponse.getIntLayerShown().getLayerType() == null) {
					errors.append("INT layer missing for response after INT shown");
					intLayerProbeScorable = false;
					intLayerProbeComputable = false;
				}
				if(computeScore && !responseMissing && intResponse.getAttackLocationResponse() == null) {
					errors.append("Response to the location probe after the " + 
							intResponse.getIntLayerShown().getLayerType().getLayerType() +  " layer missing");
					intLayerProbeScorable = false;
				}
				if(computeScore && !responseMissing && intLayerProbeScorable && 
						(intResponse.getAttackLocationResponse().getGroupAttackProbabilities() == null ||
						intResponse.getAttackLocationResponse().getGroupAttackProbabilities().size() !=
						probe.getAttackLocationProbe().getLocations().size())) {
					errors.append("Not enough probabilities in the location probe response after the " + 
							intResponse.getIntLayerShown().getLayerType().getLayerType() +  " layer shown");
					intLayerProbeScorable = false;
				}

				//Compute the updated probabilities
				if(intLayerProbeComputable && cumulativeProbs != null) {
					try {
						updateLocationProbabilities_Task_4(trial.getPossibleAttackLocations(), trial.getGroupCenter(), 
								trial.getCenterToAttackDistances(), intResponse.getIntLayerShown().getLayerType(), 
								cumulativeProbs, incrementalProbs, rules);
						intResponse.getAttackLocationResponse().setNormativeProbs_cumulativeBayesian(				
								ProbabilityUtils.cloneProbabilities_Double(cumulativeProbs));
						if(incrementalProbs != null) {
							intResponse.getAttackLocationResponse().setNormativeProbs_incrementalBayesian(
									ProbabilityUtils.cloneProbabilities_Double(incrementalProbs));
							//TODO: Set incremental probabilities to current subject probabilities
						} else {
							intResponse.getAttackLocationResponse().setNormativeProbs_incrementalBayesian(null);
						}
						if(DEBUG) {
							System.out.println("Updated probabilities given " + intResponse.getIntLayerShown().getLayerType().getLayerType() + 
									": " + cumulativeProbs);
						}

						//Compute the score based on the cumulative Bayesian probabilities
						if(computeScore && !responseMissing && intLayerProbeScorable) {
							numScoreParts++;
							Double score = computeProbabilitiesScoreS1_FromResponses(
									intResponse.getAttackLocationResponse().getGroupAttackProbabilities(), cumulativeProbs);
							intResponse.getAttackLocationResponse().setProbabilitiesScore_s1(score);
							scoreSum += score;
						}
					} catch(ScoreComputerException e) {
						e.printStackTrace();
						errors.append("Error computing updating probabilities after INT layer shown:" + e.toString());
					}	
				}	
				
				index++;
			}
		}
		
		if(DEBUG && cumulativeProbs != null && !cumulativeProbs.isEmpty()) {
			System.out.println("Final Probabilities: " + cumulativeProbs + ", Negentropy: " 
					+ computeNegentropy(cumulativeProbs, ProbabilityType.Percent));
		}
		
		if(computeScore) {
			//Compute the average probabilities score for the trial (S1)
			if(numScoreParts > 0) {
				feedback.setProbabilitiesScore_s1(scoreSum/numScoreParts);
			}

			//Compute the troop allocation score for the trial (S2)
			if(trial.getTroopAllocationProbe() == null || trial.getTroopAllocationProbe().getLocations() == null) {
				errors.append("Troop allocation missing, cannot compute troop allocation score S2");
				feedback.setErrors(errors.toString());
				return feedback;
			}
			if(response.getTroopAllocationResponse() == null || 
					response.getTroopAllocationResponse().getTroopAllocations() == null ||
					response.getTroopAllocationResponse().getTroopAllocations().size() != 
					trial.getTroopAllocationProbe().getLocations().size()) {
				errors.append("Troop allocation response missing, cannot compute troop allocation score S2");
				feedback.setErrors(errors.toString());
				return feedback;
			}
			if(trial.getGroundTruth() == null || trial.getGroundTruth().getAttackLocationId() == null) {
				errors.append("Ground truth missing, cannot compute troop allocation score S2");
				feedback.setErrors(errors.toString());
				return feedback;
			}
			feedback.setGroundTruth(trial.getGroundTruth());
			feedback.setTroopAllocationScore_s2(computeTroopAllocationScoreS2_MultiLocation(
					response.getTroopAllocationResponse().getTroopAllocations(), 
					trial.getGroundTruth().getAttackLocationId()));
			response.getTroopAllocationResponse().setTroopAllocationScore_s2(feedback.getTroopAllocationScore_s2());
		}
		feedback.setErrors(errors.toString());
		return feedback;
	}
	
	/**
	 * Compute the distances in miles from the given group center to each attack location.
	 * 
	 * @param groupCenterLocation the group center
	 * @param attackLocations the attack locations to compute distances for
	 * @param gridSize the grid size
	 * @param distanceCalculator the road distance calculator
	 * @return the distances from the group center to each attack location in miles
	 */
	public static ArrayList<Double> computeCenterToAttackDistances(GridLocation2D groupCenterLocation, ArrayList<GroupAttack> attackLocations, 
			GridSize gridSize, IRoadDistanceCalculator distanceCalculator) throws ScoreComputerException {		
		if(groupCenterLocation == null || attackLocations == null || attackLocations.isEmpty()) {
			throw new ScoreComputerException("Distances could not be computed because the group center or attack locations were missing");
		}
		try {
			ArrayList<Double> centerToAttackDistances = new ArrayList<Double>(attackLocations.size());
			for(GroupAttack location : attackLocations) {
				//System.out.println("computing distance from (" + groupCenterLocation.getX() + "," + groupCenterLocation.getY() + ") to (" +
				//		location.getLocation().getX() + "," + location.getLocation().getY() + ")");
				centerToAttackDistances.add(distanceCalculator.computeShortestDistanceFromStartLocationToEndLocation(							
						groupCenterLocation, location.getLocation()) * gridSize.getMilesPerGridUnit());				
				//centerToAttackDistances.add(SortingUtils.getDist(groupCenterLocation, location.getLocation()));
			}
			return centerToAttackDistances;
		} catch(Exception ex) {			
			throw new ScoreComputerException("Error computing distances: " + ex.toString(), ex);
		}
	}
	
	/**
	 * Updates location probabilities (for Task 4) given the layer that was shown.
	 * Throws a ScoreComputerException if an error is encountered.
	 * 
	 * @param locations the locations
	 * @param groupCenter the group center
	 * @param centerToAttackDistances the distances from the group center to each location (miles)
	 * @param layerShown the INT layer that was shown
	 * @param cumulativeProbs the current cumulative Bayesian attack probabilities for each group
	 * @param incrementalProbs the current incremental Bayesian attack probabilities for each group (if any)
	 * @param rules the probability rules to use. May also contain the probability updating strategy to use. If null, Bayesian updating is used.
	 * @throws ScoreComputerException
	 */
	public static void updateLocationProbabilities_Task_4(ArrayList<GroupAttack> locations, 
			GroupCenter groupCenter, 
			ArrayList<Double> centerToAttackDistances,
			IntLayer layerShown, 
			ArrayList<Double> cumulativeProbs,
			ArrayList<Double> incrementalProbs, 
			ProbabilityRules rules) throws ScoreComputerException {
		try {			
			ProbabilityUpdateStrategy updateStrategy = rules.getProbabilityUpdateStrategy();
			if(updateStrategy == null) {
				updateStrategy = new BayesianUpdateStrategy();
				rules.setProbabilityUpdateStrategy(updateStrategy);
			}
			//System.out.println(rules.getProbabilityUpdateStrategy());
			
			//Normalize probabilities			
			if(cumulativeProbs != null && !cumulativeProbs.isEmpty()) {
				ProbabilityUtils.normalizePercentProbabilities_Double(cumulativeProbs, cumulativeProbs);
			}
			if(incrementalProbs != null && !incrementalProbs.isEmpty()) {
				ProbabilityUtils.normalizePercentProbabilities_Double(incrementalProbs, incrementalProbs);
			}			
			
			//Get the likelihoods based on the INT layer that was shown
			ArrayList<Double> likelihoods = new ArrayList<Double>(locations.size());
			GroupType group = groupCenter.getGroup();
			int index = 0;
			switch(layerShown.getLayerType()) {		
			case HUMINT:
				if(centerToAttackDistances == null || centerToAttackDistances.isEmpty()) {
					throw new ScoreComputerException("Error, center to attack distances array cannot be null for HUMINT calculations");			
				}
				for(; index < locations.size(); index++) {
					if(index >= centerToAttackDistances.size()) {
						throw new ScoreComputerException("Error, index out of bounds for center to attack distances array");
					}
					likelihoods.add(rules.getHumintLikelihood(centerToAttackDistances.get(index)));
					/*double likelihood = rules.getHumintLikelihood(centerToAttackDistances.get(index));
					if(cumulativeProbs != null && index < cumulativeProbs.size()) {
						cumulativeProbs.set(index, updateStrategy.computePosterior(cumulativeProbs.get(index)/100.d, likelihood));
					}
					if(incrementalProbs != null && index < incrementalProbs.size()) {
						incrementalProbs.set(index, updateStrategy.computePosterior(incrementalProbs.get(index)/100.d, likelihood));
					}*/					
				}
				break;
			case IMINT:
				ImintRule imintRule = rules.getImintRule(group);
				if(imintRule == null) {
					throw new ScoreComputerException("Error, missing IMINT rule for Group " + group);
				}
				for(GroupAttack location : locations) {
					if(location.getIntelReport() == null || location.getIntelReport().getImintInfo() == null) {
						throw new ScoreComputerException("Error, missing IMINT for location " + location.getId());
					}
					likelihoods.add(imintRule.getAttackLikelihood(location.getIntelReport().getImintInfo()));
					/*double likelihood = imintRule.getAttackLikelihood(location.getIntelReport().getImintInfo());
					if(cumulativeProbs != null && index < cumulativeProbs.size()) {
						cumulativeProbs.set(index, updateStrategy.computePosterior(cumulativeProbs.get(index)/100.d, likelihood));
						//cumulativeProbs.set(index, (cumulativeProbs.get(index)/100.d * likelihood) * 100);						
					}					
					if(incrementalProbs != null && index < incrementalProbs.size()) {
						incrementalProbs.set(index, updateStrategy.computePosterior(incrementalProbs.get(index)/100.d, likelihood));
						//incrementalProbs.set(index, (incrementalProbs.get(index)/100.d * likelihood) * 100);						
					}
					index++;*/				
				}
				break;
			case MOVINT:
				MovintRule movintRule = rules.getMovintRule(group);
				if(movintRule == null) {
					throw new ScoreComputerException("Error, missing MOVINT rule for Group " + group);
				}
				for(GroupAttack location : locations) {
					if(location.getIntelReport() == null || location.getIntelReport().getMovintInfo() == null) {
						throw new ScoreComputerException("Error, missing MOVINT for location " + location.getId());
					}
					likelihoods.add(movintRule.getAttackLikelihood(location.getIntelReport().getMovintInfo()));
					/*double likelihood = movintRule.getAttackLikelihood(location.getIntelReport().getMovintInfo());
					if(cumulativeProbs != null && index < cumulativeProbs.size()) {
						cumulativeProbs.set(index, updateStrategy.computePosterior(cumulativeProbs.get(index)/100.d, likelihood));
					}
					if(incrementalProbs != null && index < incrementalProbs.size()) {
						incrementalProbs.set(index, updateStrategy.computePosterior(incrementalProbs.get(index)/100.d, likelihood));
					}
					index++;*/				
				}
				break;
			case SOCINT:
				SocintRule socintRule = rules.getSocintRule(group);
				if(socintRule == null) {
					throw new ScoreComputerException("Error, missing SOCINT rule for Group " + group);
				}
				for(GroupAttack location : locations) {
					if(location.getIntelReport() == null || location.getIntelReport().getSocintInfo() == null) {
						throw new ScoreComputerException("Error, missing SOCINT for location " + location.getId());
					}
					if(location.getIntelReport().getSocintInfo() == group) {
						likelihoods.add(socintRule.getAttackLikelihood_inRegion());
					} else {
						likelihoods.add(socintRule.getAttackLikelihood_outsideRegion());
					}										
					/*double likelihood = socintRule.getAttackLikelihood_outsideRegion();
					if(location.getIntelReport().getSocintInfo() == group) {
						likelihood = socintRule.getAttackLikelihood_inRegion();
					}
					if(cumulativeProbs != null && index < cumulativeProbs.size()) {
						cumulativeProbs.set(index, updateStrategy.computePosterior(cumulativeProbs.get(index)/100.d, likelihood));
						//cumulativeProbs.set(index, (cumulativeProbs.get(index)/100.d * likelihood) * 100);
					}
					if(incrementalProbs != null && index < incrementalProbs.size()) {
						incrementalProbs.set(index, updateStrategy.computePosterior(incrementalProbs.get(index)/100.d, likelihood));
						//incrementalProbs.set(index, (incrementalProbs.get(index)/100.d * likelihood) * 100);
					}
					index++;*/				
				}
				break;
			default:
				break;
			}			
						
			//Compute posteriors using the cumulative probabilities as the priors and the likelihoods			
			if(cumulativeProbs != null && !cumulativeProbs.isEmpty()) {				
				cumulativeProbs = updateStrategy.computePosteriors(cumulativeProbs, likelihoods, cumulativeProbs);
				ProbabilityUtils.normalizePercentProbabilities_Double(cumulativeProbs, cumulativeProbs);
			}			
			//Compute posteriors using the incremental probabilities as the priors and the likelihoods
			if(incrementalProbs != null && !incrementalProbs.isEmpty()) {
				incrementalProbs = updateStrategy.computePosteriors(incrementalProbs, likelihoods, incrementalProbs);
				ProbabilityUtils.normalizePercentProbabilities_Double(incrementalProbs, incrementalProbs);
			}
		} catch(Exception ex) {
			//ex.printStackTrace();
			throw new ScoreComputerException("Error computing updated probabilities: " + ex.toString(), ex);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment.phase_1.score_computer.IScoreComputer#computeScoreForTrial(org.mitre.icarus.cps.exam.phase_1.testing.Task_6_Trial, org.mitre.icarus.cps.exam.phase_1.testing.probability_rules.ProbabilityRules)
	 */
	@Override
	public TrialFeedback_Phase1 computeSolutionAndScoreForTrial(Task_6_Trial trial, ProbabilityRules rules, GridSize gridSize, boolean computeScore) {
		return computeSolutionAndScoreForTrial((Task_5_Trial)trial, rules, gridSize, computeScore);
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment.phase_1.score_computer.IScoreComputer#computeSolutionAndScoreForTrial(org.mitre.icarus.cps.exam.phase_1.testing.Task_5_Trial, org.mitre.icarus.cps.exam.phase_1.testing.probability_rules.ProbabilityRules, org.mitre.icarus.cps.feature_vector.phase_1.GridSize, boolean)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public TrialFeedback_Phase1 computeSolutionAndScoreForTrial(Task_5_Trial trial, ProbabilityRules rules, GridSize gridSize, boolean computeScore) {
		ErrorString errors = new ErrorString();
		TrialFeedback_Phase1 feedback = new TrialFeedback_Phase1();
		boolean usingInitialGroupProbe = false;		
		boolean initialGroupProbeScorable = true;
		boolean initialProbabilitiesComputable = true;		
		//boolean intLayerProbesScorable = true;
		boolean intLayerProbesComputable = true;
		boolean responseMissing = false;
		boolean distancesComputable = true;
		boolean task6Trial = trial instanceof Task_6_Trial;
		if(computeScore && trial.getTrialResponse() == null) {
			errors.append("Trial response missing, cannot computer S1 or S2 score");
			responseMissing = true;
			initialGroupProbeScorable = false;
		}
		if(trial.getGroupCenters() == null || trial.getGroupCenters().isEmpty()) {
			errors.append("Trial missing group centers, cannot compute distances from attack location to group centers");
			distancesComputable = false;
		}
		if(trial.getAttackLocation() == null || trial.getAttackLocation().getLocation() == null) {
			errors.append("Trial missing attack location, cannot comptue distances from attack location to group centers");
			distancesComputable = false;
		}
		if(trial.getAttackLocationProbe_initial() != null) {
			if(trial.getAttackLocationProbe_initial().getGroups() == null ||
					trial.getAttackLocationProbe_initial().getGroups().isEmpty()) {
				errors.append("Trial missing groups in older-style exam with initial group probe");
				initialProbabilitiesComputable = false;
				initialGroupProbeScorable = false;
				//intLayerProbesScorable = false;
				intLayerProbesComputable = false;
			} else {			
				errors.append("Warning, older-style exam with initial group probe");
			}
			usingInitialGroupProbe = true;
		} else if(trial.getInitialHumintReport() == null || trial.getInitialHumintReport().getGroups() == null ||
				trial.getInitialHumintReport().getGroups().isEmpty()) {
			if(trial.getGroupCenters() == null || trial.getGroupCenters().isEmpty()) {
				errors.append("Trial missing initial HUMINT groups and group centers, cannot compute normative solution or S1 or S2 score");
				initialProbabilitiesComputable = false;
				intLayerProbesComputable = false;
			} else {
				ArrayList<GroupType> groups = new ArrayList<GroupType>(trial.getGroupCenters().size());
				for(GroupCenter groupCenter : trial.getGroupCenters()) {
					groups.add(groupCenter.getGroup());
				}
				if(trial.getInitialHumintReport() == null) {
					trial.setInitialHumintReport(new HumintReport());
					trial.getInitialHumintReport().setGroups(groups);
				} else {
					trial.getInitialHumintReport().setGroups(groups);
				}
			}
		}
		/*else if(trial.getInitialHumintReport() == null || trial.getInitialHumintReport().getGroups() == null ||
				trial.getInitialHumintReport().getGroups().isEmpty()) {
			errors.append("Trial missing initial HUMINT groups, cannot compute normative solution or S1 or S2 score");
			//intLayerProbesScorable = false;
			initialProbabilitiesComputable = false;
			intLayerProbesComputable = false;
		}*/
		if(trial.getIntLayers() == null || trial.getIntLayers().isEmpty()) {
			errors.append("Trial Missing INT layers");
			intLayerProbesComputable = false;
		}
		
		if(trial.getTrialResponse() == null) {
			Task_5_6_TrialResponse response = new Task_5_6_TrialResponse();
			trial.setTrialResponse(response);
			if(intLayerProbesComputable) {				
				ArrayList<Task_5_6_AttackLocationProbeResponseAfterINT> intResponses = 
						new ArrayList<Task_5_6_AttackLocationProbeResponseAfterINT>(trial.getIntLayers().size());
				response.setAttackLocationResponses_afterINTs(intResponses);
				int numIntLayers = task6Trial ? ((Task_6_Trial)trial).getNumLayersToShow() : trial.getIntLayers().size();				
				for(int i =0; i<numIntLayers; i++) {
					Task_5_6_AttackLocationProbeResponseAfterINT intResponse = new Task_5_6_AttackLocationProbeResponseAfterINT();
					if(!task6Trial) {
						//For Task 6 trials, we'll select the next INT layer that maximizes expected utility
						intResponse.setIntLayerShown(new INTLayerData(trial.getIntLayers().get(i).getLayerType(), false));
					}
					intResponse.setAttackLocationResponse(new AttackLocationProbeResponse_MultiGroup());
					intResponses.add(intResponse);						
				}
			}
		}
		Task_5_6_TrialResponse response = trial.getTrialResponse();
		
		if(computeScore && usingInitialGroupProbe && !responseMissing && response.getAttackLocationResponse_initial() == null) {
			errors.append("Response to the inital group probe missing, cannot compute probabilities score S1");
			initialGroupProbeScorable = false;
		}	
		
		//Compute distances from each group center to the attack location if they haven't been computed yet (HUMINT)
		if(DEBUG && trial.getCentersToAttackDistances() != null) {
			System.out.println("Distances from file: " + trial.getCentersToAttackDistances());
		}
		if(trial.getCentersToAttackDistances() == null || trial.getCentersToAttackDistances().isEmpty()) {
			if(distancesComputable) {
				try {
					trial.setCentersToAttackDistances(computeCentersToAttackDistances(trial.getAttackLocation().getLocation(), 
							trial.getGroupCenters(), gridSize, new RoadDistanceCalculator(trial.getRoads(), gridSize)));
				} catch(Exception ex) {
					ex.printStackTrace();
					errors.append("Distances from the attack location to the group centers could not be computed: " + ex.toString());
					initialProbabilitiesComputable = false;
					intLayerProbesComputable = false;
				}
				if(DEBUG) {
					System.out.println("Distances from distance calculator: " + trial.getCentersToAttackDistances());	
				}			
			} else {
				errors.append("The attack location or group centers were missing, distances could not be computed");
				initialProbabilitiesComputable = false;
				intLayerProbesComputable = false;
			}
		} else if(DEBUG && distancesComputable) {
			try {
				System.out.println("Distances from distance calculator: " + 
						computeCentersToAttackDistances(trial.getAttackLocation().getLocation(), 
								trial.getGroupCenters(), gridSize, new RoadDistanceCalculator(trial.getRoads(), gridSize)));
			} catch(Exception ex) {}
		}
		
		//Compute the initial cumulative Bayesian probabilities based on the distances from HUMINT
		ArrayList<Double> cumulativeProbs = null; //the current cumulative Bayesian probs		
		if(initialProbabilitiesComputable) {
			if(usingInitialGroupProbe && initialGroupProbeScorable) {
				cumulativeProbs = ProbabilityUtils.createDefaultInitialProbabilities_Double(
								trial.getAttackLocationProbe_initial().getGroups().size()); 
			} else {
				cumulativeProbs = ProbabilityUtils.createDefaultInitialProbabilities_Double(
								trial.getInitialHumintReport().getGroups().size());
			}
			try {
				updateGroupProbabilities_Task_5_6(trial.getGroupCenters(), trial.getAttackLocation(), 
						trial.getCentersToAttackDistances(), new HumintLayer(), cumulativeProbs, null, null, rules);
				response.setInitialProbabilities(ProbabilityUtils.cloneProbabilities_Double(cumulativeProbs));
				if(usingInitialGroupProbe) {
					response.getAttackLocationResponse_initial().setNormativeProbs_cumulativeBayesian(				
							ProbabilityUtils.cloneProbabilities_Double(cumulativeProbs));	
				}
			} catch(ScoreComputerException e) {
				e.printStackTrace();
				errors.append("Error computing the initial normative probabilities based on HUMINT distances:" + e.toString());
				intLayerProbesComputable = false;
			}
		}
		
		if(DEBUG && trial.getInitialHumintReport() != null) {	
			System.out.println("Initial probabilities given HUMINT: " + cumulativeProbs + ", stored HUMINT probabilities: "
					+ trial.getInitialHumintReport().getHumintProbabilities());
		}		
		
		//Initialize the initial HUMINT report probabilities if they were null or missing
		if(trial.getInitialHumintReport() != null && (trial.getInitialHumintReport().getHumintProbabilities() == null ||
				trial.getInitialHumintReport().getHumintProbabilities().isEmpty())) {
			trial.getInitialHumintReport().setHumintProbabilities(ProbabilityUtils.cloneProbabilities_Double(cumulativeProbs));
		}
		
		//Initialize the current incremental Bayesian probabilities based on the initial cumulative Bayesian probs from HUMINT
		ArrayList<Double> incrementalProbs = null;		
		if(computeIncrementalProbabilities && computeScore && cumulativeProbs != null) {
			if(usingInitialGroupProbe && initialGroupProbeScorable) {
				incrementalProbs = new ArrayList<Double>(cumulativeProbs.size()); 
				for(GroupAttackProbabilityResponse prob : response.getAttackLocationResponse_initial().getGroupAttackProbabilities()) {
					incrementalProbs.add(prob.getProbability());
				}
				ProbabilityUtils.normalizePercentProbabilities_Double(incrementalProbs, incrementalProbs);
				response.getAttackLocationResponse_initial().setNormativeProbs_incrementalBayesian(
						ProbabilityUtils.cloneProbabilities_Double(incrementalProbs));	
			} else {			
				incrementalProbs = ProbabilityUtils.cloneProbabilities_Double(cumulativeProbs);
			}
		} else if(usingInitialGroupProbe) {
			response.getAttackLocationResponse_initial().setNormativeProbs_incrementalBayesian(null);	
		}
		
		//Compute the score for the initial group probe based on the cumulative Bayesian probabilities
		int numScoreParts = 0;
		Double scoreSum = 0D;	
		ArrayList<Double> subjectProbs = null; //the current subject probs (only used to computer INT layer expected utilities)
		if(computeScore) {
			if(usingInitialGroupProbe && initialGroupProbeScorable) {
				if(computeIncrementalProbabilities) {
					subjectProbs = createSubjectProbs(response.getAttackLocationResponse_initial().getGroupAttackProbabilities());
				}
				numScoreParts++;
				Double score = computeProbabilitiesScoreS1_FromResponses(
						response.getAttackLocationResponse_initial().getGroupAttackProbabilities(), cumulativeProbs);
				scoreSum += score;	
				response.getAttackLocationResponse_initial().setProbabilitiesScore_s1(score);
			} else if(computeIncrementalProbabilities) {
				subjectProbs = ProbabilityUtils.cloneProbabilities_Double(cumulativeProbs);
			}
		}
		
		//Compute the updated probabilities and score after each INT presentation
		if(response.getAttackLocationResponses_afterINTs() == null) {
			errors.append("Response to the probes after INT layers missing");
			//intLayerProbesScorable = false;
			intLayerProbesComputable = false;
		}
		int numIntLayers = task6Trial ? ((Task_6_Trial)trial).getNumLayersToShow() : trial.getIntLayers().size();				
		if(response.getAttackLocationResponses_afterINTs().size() != numIntLayers) {
			errors.append("Responses to the probes after INT layers does not match number of INT layers");
			//intLayerProbesScorable = false;
			intLayerProbesComputable = false;
		}		
		if(intLayerProbesComputable) {
			int index = 0;
			List<IntLayer> remainingLayers = null;
			if(task6Trial) {
				remainingLayers = new LinkedList<IntLayer>();
				for(Task_5_6_INTLayerPresentationProbe layer : trial.getIntLayers()) {
					remainingLayers.add(layer.getLayerType());
				}
			}
			for(Task_5_6_AttackLocationProbeResponseAfterINT intResponse : response.getAttackLocationResponses_afterINTs()) {				
				//First compute the expected utility of choosing each remaining INT layer for Task 6
				IntLayer highestUtilityLayer = null;
				if(task6Trial && !remainingLayers.isEmpty()) {
					ArrayList<INTLayerExpectedUtility> intLayerUtilities = new ArrayList<INTLayerExpectedUtility>(remainingLayers.size());
					intResponse.setIntLayerExpectedUtilities(intLayerUtilities);
					double maxUtility = Double.MIN_VALUE;
					for(IntLayer layer : remainingLayers) {						
						try {
							ProbabilitySet utility = computeExpectedUtilityOfINT_Task_6(trial.getGroupCenters(), trial.getAttackLocation(), 
									trial.getCentersToAttackDistances(), layer,
									cumulativeProbs, incrementalProbs, subjectProbs,
									rules);
							intLayerUtilities.add(new INTLayerExpectedUtility(layer, utility.cumulativeProb, 
									utility.incrementalProb, utility.subjectProb));
							if(utility.cumulativeProb > maxUtility) {
								highestUtilityLayer = layer;
								maxUtility = utility.cumulativeProb;
							}
						} catch (ScoreComputerException e) {
							e.printStackTrace();
							errors.append("Error computing the expected utility of choosing an INT layer:" + e.toString());
						}
					}
				}
				
				Task_5_6_INTLayerPresentationProbe probe = trial.getIntLayers().get(index);
				boolean intLayerProbeScorable = true;
				boolean intLayerProbeComputable = true;
				if(probe.getLayerType() == null) {
					errors.append("Missing INT layer type");
					intLayerProbeScorable = false;
					intLayerProbeComputable = false;
				} 
				if(probe.getAttackLocationProbe() == null || probe.getAttackLocationProbe().getGroups() == null ||
						probe.getAttackLocationProbe().getGroups().isEmpty()) {
					errors.append("Missing group probe after " + probe.getLayerType().getLayerType() + " layer");
					intLayerProbeScorable = false;
					intLayerProbeComputable = false;
				}
				
				//DEBUG CODE
				/*if(task6Trial && intResponse != null && intResponse.getIntLayerExpectedUtilities() != null &&
						subjectProbs != null) {
					double maxUtility = Double.MIN_VALUE;
					double maxProb = Double.MIN_VALUE;					
					SigintLayer highestUtilitySigintLayer = null;
					GroupType highestProbGroup = null;
					for(INTLayerExpectedUtility utility : intResponse.getIntLayerExpectedUtilities()) {
						if(utility.getLayerType() instanceof SigintLayer && utility.getExpectedUtility_subjectProbs() != null &&
								utility.getExpectedUtility_subjectProbs() >= maxUtility) {
							highestUtilitySigintLayer = (SigintLayer)utility.getLayerType();
							maxUtility = utility.getExpectedUtility_subjectProbs();
						}
					}
					int i = 0;
					for(Double prob : subjectProbs) {
						if(prob >= maxProb) {
							highestProbGroup = GroupType.values()[i];
							maxProb = prob;
						}
						i++;
					}
					if(highestUtilitySigintLayer != null) {
						System.out.println("Highest Utility SIGINT Group: " + highestUtilitySigintLayer.getGroup() + ", Group with Highest Prob: " + highestProbGroup);						
						if(highestUtilitySigintLayer.getGroup() != highestProbGroup && 
								subjectProbs.get(highestUtilitySigintLayer.getGroup().ordinal()) < maxProb) {
							System.out.println("Found mismatch: Trial: " + trial.getTrialNum() + "-" + (index+1) + ", Highest Utility SIGINT Group: " +
							highestUtilitySigintLayer.getGroup() + ", Group with Highest Prob: " + highestProbGroup + ", Subject Probs: " + subjectProbs);
						}
					}
					//System.out.println("highest utility layer: " + highestUtilityLayer + ", " + intResponse.getIntLayerShown() + ", " + responseMissing + ", " + computeScore);
				}*/
				//END DEBUG CODE
				
				if(intResponse.getIntLayerShown() == null && (task6Trial && !computeScore)) {
					//Select the INT layer with the highest expected utility
					if(DEBUG) {
						System.out.println("highest utility layer: " + formatLayerName(highestUtilityLayer));
					}
					intResponse.setIntLayerShown(new INTLayerData(highestUtilityLayer, true));
				}
				if(intResponse.getIntLayerShown() == null || intResponse.getIntLayerShown().getLayerType() == null) {
					errors.append("INT layer missing for response after INT shown");
					intLayerProbeScorable = false;
					intLayerProbeComputable = false;
				} else if(remainingLayers != null && !remainingLayers.isEmpty()) {
					if(intResponse.getIntLayerShown().getLayerType().getLayerType() == IntType.SIGINT) {
						//Remove all SIGINT layers
						for(IntLayer layer : remainingLayers.toArray(new IntLayer[remainingLayers.size()])) {
							if(layer.getLayerType() == IntType.SIGINT) {
								remainingLayers.remove(layer);
							}
						}
					} else {
						remainingLayers.remove(intResponse.getIntLayerShown().getLayerType());
					}
				}
				if(computeScore && !responseMissing && intResponse.getAttackLocationResponse() == null) {
					errors.append("Response to the group probe after the " + 
							(intResponse.getIntLayerShown() != null && intResponse.getIntLayerShown().getLayerType() != null ? 
									intResponse.getIntLayerShown().getLayerType().getLayerType() : "null") +  " layer missing");
					intLayerProbeScorable = false;
				}
				if(computeScore && !responseMissing && (intResponse.getAttackLocationResponse().getGroupAttackProbabilities() == null ||
						intResponse.getAttackLocationResponse().getGroupAttackProbabilities().size() !=
						probe.getAttackLocationProbe().getGroups().size())) {
					errors.append("Not enough probabilities in the group probe response after the " + 
							(intResponse.getIntLayerShown() != null && intResponse.getIntLayerShown().getLayerType() != null ? 
									intResponse.getIntLayerShown().getLayerType().getLayerType() : "null") +  " layer shown");
					intLayerProbeScorable = false;
				}

				//Compute the updated probabilities
				if(intLayerProbeComputable && cumulativeProbs != null) {
					try {
						updateGroupProbabilities_Task_5_6(trial.getGroupCenters(), trial.getAttackLocation(), 
								trial.getCentersToAttackDistances(), intResponse.getIntLayerShown().getLayerType(), 
								cumulativeProbs, incrementalProbs, null, rules);						
						intResponse.getAttackLocationResponse().setNormativeProbs_cumulativeBayesian(				
								ProbabilityUtils.cloneProbabilities_Double(cumulativeProbs));
						if(incrementalProbs != null) {
							intResponse.getAttackLocationResponse().setNormativeProbs_incrementalBayesian(
									ProbabilityUtils.cloneProbabilities_Double(incrementalProbs));
							//TODO: Set current incremental probabilities to subject probabilities
						} else {
							intResponse.getAttackLocationResponse().setNormativeProbs_incrementalBayesian(null);
						}
						if(DEBUG) {
							System.out.println("Updated probabilities given " + intResponse.getIntLayerShown().getLayerType().getLayerType() +  
									": " + cumulativeProbs);
						}

						//Compute the score based on the cumulative Bayesian probabilities
						if(computeScore && !responseMissing && intLayerProbeScorable) {
							subjectProbs = createSubjectProbs(intResponse.getAttackLocationResponse().getGroupAttackProbabilities());
							numScoreParts++;
							Double score = computeProbabilitiesScoreS1_FromResponses(
									intResponse.getAttackLocationResponse().getGroupAttackProbabilities(), cumulativeProbs);
							scoreSum += score;
							intResponse.getAttackLocationResponse().setProbabilitiesScore_s1(score);
						}
					} catch(ScoreComputerException e) {
						e.printStackTrace();
						errors.append("Error computing updated probabilities after an INT layer was shown:" + e.toString());						
					}
				} 				
				index++;
			}
		}
		
		if(DEBUG && cumulativeProbs != null && !cumulativeProbs.isEmpty()) {
			System.out.println("Final Probabilities: " + cumulativeProbs + ", Negentropy: " 
					+ computeNegentropy(cumulativeProbs, ProbabilityType.Percent));
		}
		
		if(computeScore) {
			//Compute the average probabilities score for the trial (S1)
			if(numScoreParts > 0) {
				feedback.setProbabilitiesScore_s1(scoreSum/numScoreParts);
			}

			//Compute the troop allocation score for the trial (S2)
			if(trial.getTroopAllocationProbe() == null || trial.getTroopAllocationProbe().getGroups() == null) {
				errors.append("Troop allocation missing, cannot compute troop allocation score S2");
				feedback.setErrors(errors.toString());
				return feedback;
			}
			if(response.getTroopAllocationResponse() == null || 
					response.getTroopAllocationResponse().getTroopAllocations() == null ||
					response.getTroopAllocationResponse().getTroopAllocations().size() != 
					trial.getTroopAllocationProbe().getGroups().size()) {
				errors.append("Troop allocation response missing, cannot compute troop allocation score S2");
				feedback.setErrors(errors.toString());
				return feedback;
			}
			if(trial.getGroundTruth() == null || trial.getGroundTruth().getResponsibleGroup() == null) {
				errors.append("Ground truth missing, cannot compute troop allocation score S2");
				feedback.setErrors(errors.toString());
				return feedback;
			}
			feedback.setGroundTruth(trial.getGroundTruth());
			feedback.setTroopAllocationScore_s2(computeTroopAllocationScoreS2_MultiGroup(
					response.getTroopAllocationResponse().getTroopAllocations(), 
					trial.getGroundTruth().getResponsibleGroup()));
			response.getTroopAllocationResponse().setTroopAllocationScore_s2(feedback.getTroopAllocationScore_s2());
		}
		feedback.setErrors(errors.toString());
		return feedback;
	}	
	
	/**
	 * Compute the distances in miles from the given attack location to each group center.
	 * 
	 * @param attackLocation the attack location
	 * @param groupCenters the group centers
	 * @param gridSize the grid size
	 * @param distanceCalculator the road distance calculator
	 * @return the distance in miles from the attack location to each group center
	 */
	public static ArrayList<Double> computeCentersToAttackDistances(GridLocation2D attackLocation, ArrayList<GroupCenter> groupCenters,
			GridSize gridSize, IRoadDistanceCalculator distanceCalculator) throws ScoreComputerException {
		if(attackLocation == null || groupCenters == null || groupCenters.isEmpty()) {
			throw new ScoreComputerException("Distances could not be computed because the attack location or group centers were missing");
		}
		try {
			ArrayList<Double> centersToAttackDistances = new ArrayList<Double>(groupCenters.size());		
			for(GroupCenter groupCenter : groupCenters) {
				//Compute road network distance	
				centersToAttackDistances.add(distanceCalculator.computeShortestDistanceFromStartLocationToEndLocation(							
						attackLocation, groupCenter.getLocation()) * gridSize.getMilesPerGridUnit());			
				//centersToAttackDistances.add(SortingUtils.getDist(attackLocation, groupCenter.getLocation()));
			}		
			return centersToAttackDistances;
		} catch(Exception ex) {
			//ex.printStackTrace();
			throw new ScoreComputerException("Error computing distances: " + ex.toString(), ex);
		}
	}
	
	/**
	 * Updates group probabilities (for Tasks 5-6) given the layer that was shown.
	 * Throws a ScoreComputerException if an error is encountered.
	 * 
	 * @param groupCenters the group centers
	 * @param location the attack location 
	 * @param centersToAttackDistances the distances from the attack location to each group center (miles)
	 * @param layerShown the layer that was shown
	 * @param cumulativeProbs the current cumulative Bayesian attack probabilities for each group
	 * @param incrementalProbs the current incremental Bayesian attack probabilities for each group (if any)
	 * @param subjectProbs the current subject/model probabilities for each group (if any)
	 * @param rules the probability rules to use. May also contain the probability updating strategy to use. If null, Bayesian updating is used.
	 * @throws ScoreComputerException
	 */
	public static void updateGroupProbabilities_Task_5_6(ArrayList<GroupCenter> groupCenters, 
			GroupAttack location, 
			ArrayList<Double> centersToAttackDistances,
			IntLayer layerShown, 
			ArrayList<Double> cumulativeProbs,
			ArrayList<Double> incrementalProbs,
			ArrayList<Double> subjectProbs,
			ProbabilityRules rules) throws ScoreComputerException {
		try {
			ProbabilityUpdateStrategy updateStrategy = rules.getProbabilityUpdateStrategy();
			if(updateStrategy == null) {
				updateStrategy = new BayesianUpdateStrategy();
				rules.setProbabilityUpdateStrategy(updateStrategy);
			}
			
			//Normalize probabilities
			if(cumulativeProbs != null && !cumulativeProbs.isEmpty()) {
				ProbabilityUtils.normalizePercentProbabilities_Double(cumulativeProbs, cumulativeProbs);
			}
			if(incrementalProbs != null && !incrementalProbs.isEmpty()) {
				ProbabilityUtils.normalizePercentProbabilities_Double(incrementalProbs, incrementalProbs);
			}
			if(subjectProbs != null && !subjectProbs.isEmpty()) {
				ProbabilityUtils.normalizePercentProbabilities_Double(subjectProbs, subjectProbs);
			}			
			
			//Get the likelihoods based on the INT layer that was shown
			ArrayList<Double> likelihoods = new ArrayList<Double>(groupCenters.size());
			int index = 0;
			switch(layerShown.getLayerType()) {		
			case HUMINT:
				if(centersToAttackDistances == null || centersToAttackDistances.isEmpty()) {
					throw new ScoreComputerException("Error, centers to attack distances array cannot be null for HUMINT calculations");			
				}
				for(; index < groupCenters.size(); index++) {
					if(index >= centersToAttackDistances.size()) {
						throw new ScoreComputerException("Error, index out of bounds for center to attack distances array");
					}
					likelihoods.add(rules.getHumintLikelihood(centersToAttackDistances.get(index)));
					/*double likelihood = rules.getHumintLikelihood(centersToAttackDistances.get(index));
					if(cumulativeProbs != null && index < cumulativeProbs.size()) {
						cumulativeProbs.set(index, updateStrategy.computePosterior(cumulativeProbs.get(index)/100.d, likelihood));
					}
					if(incrementalProbs != null && index < incrementalProbs.size()) {
						incrementalProbs.set(index, updateStrategy.computePosterior(incrementalProbs.get(index)/100.d, likelihood));
						//incrementalProbs.set(index, (incrementalProbs.get(index)/100.d * likelihood) * 100);						
					}	
					if(subjectProbs != null && index < subjectProbs.size()) {
						subjectProbs.set(index, updateStrategy.computePosterior(subjectProbs.get(index)/100.d, likelihood));
						//subjectProbs.set(index, (subjectProbs.get(index)/100.d * likelihood) * 100);						
					}*/	
				}
				break;
			case IMINT:
				if(location.getIntelReport() == null || location.getIntelReport().getImintInfo() == null) {
					throw new ScoreComputerException("Error, missing IMINT for location " + location.getId());
				}	
				for(GroupCenter groupCenter : groupCenters) {					
					GroupType group = groupCenter.getGroup();
					ImintRule rule = rules.getImintRule(group);
					if(rule == null) {
						throw new ScoreComputerException("Error, missing IMINT rule for Group " + group);
					}
					likelihoods.add(rule.getAttackLikelihood(location.getIntelReport().getImintInfo()));
					/*double likelihood = rule.getAttackLikelihood(location.getIntelReport().getImintInfo());
					if(cumulativeProbs != null && index < cumulativeProbs.size()) {
						cumulativeProbs.set(index, updateStrategy.computePosterior(cumulativeProbs.get(index)/100.d, likelihood));
					}					
					if(incrementalProbs != null && index < incrementalProbs.size()) { 
						incrementalProbs.set(index, updateStrategy.computePosterior(incrementalProbs.get(index)/100.d, likelihood));
					}
					if(subjectProbs != null && index < subjectProbs.size()) {
						subjectProbs.set(index, updateStrategy.computePosterior(subjectProbs.get(index)/100.d, likelihood));
					}	
					index++;*/				
				}
				break;
			case MOVINT:
				if(location.getIntelReport() == null || location.getIntelReport().getMovintInfo() == null) {
					throw new ScoreComputerException("Error, missing MOVINT for location " + location.getId());
				}	
				for(GroupCenter groupCenter : groupCenters) {					
					GroupType group = groupCenter.getGroup();
					MovintRule rule = rules.getMovintRule(group);
					if(rule == null) {
						throw new ScoreComputerException("Error, missing MOVINT rule for Group " + group);
					}
					likelihoods.add(rule.getAttackLikelihood(location.getIntelReport().getMovintInfo()));
					/*double likelihood = rule.getAttackLikelihood(location.getIntelReport().getMovintInfo());
					if(cumulativeProbs != null && index < cumulativeProbs.size()) {
						cumulativeProbs.set(index, updateStrategy.computePosterior(cumulativeProbs.get(index)/100.d, likelihood));
					}
					if(incrementalProbs != null && index < incrementalProbs.size()) {
						incrementalProbs.set(index, updateStrategy.computePosterior(incrementalProbs.get(index)/100.d, likelihood));
					}
					if(subjectProbs != null && index < subjectProbs.size()) {
						subjectProbs.set(index, updateStrategy.computePosterior(subjectProbs.get(index)/100.d, likelihood));
					}	
					index++;*/				
				}
				break;
			case SIGINT:
				GroupType probedGroup = ((SigintLayer)layerShown).getGroup();
				GroupCenter probedGroupCenter = null;
				if(probedGroup != null) {
					for(GroupCenter groupCenter : groupCenters) {
						if(groupCenter.getGroup() == probedGroup) {
							probedGroupCenter = groupCenter;
							break;
						}
					}					
				}
				if(probedGroup == null || probedGroupCenter == null) {
					throw new ScoreComputerException("Error, missing Group for SIGINT layer");
				}
				if(probedGroupCenter.getIntelReport() == null || probedGroupCenter.getIntelReport().getSigintInfo() == null) {
					throw new ScoreComputerException("Error, missing SIGINT for Group " + probedGroup);
				}	
				for(GroupCenter groupCenter : groupCenters) {					
					GroupType group = groupCenter.getGroup();									
					SigintRule rule = rules.getSigintRule(group);					
					if(rule == null) {
						throw new ScoreComputerException("Error, missing SIGINT rule for Group " + group);
					}
					likelihoods.add(rule.getAttackLikelihood(probedGroupCenter.getIntelReport().getSigintInfo(), 
							group == probedGroup));
					/*double likelihood = rule.getAttackLikelihood(probedGroupCenter.getIntelReport().getSigintInfo(), 
							group == probedGroup);
					if(cumulativeProbs != null && index < cumulativeProbs.size()) {
						cumulativeProbs.set(index, updateStrategy.computePosterior(cumulativeProbs.get(index)/100.d, likelihood));
					}
					if(incrementalProbs != null && index < incrementalProbs.size()) {
						incrementalProbs.set(index, updateStrategy.computePosterior(incrementalProbs.get(index)/100.d, likelihood));
					}
					if(subjectProbs != null && index < subjectProbs.size()) {
						subjectProbs.set(index, updateStrategy.computePosterior(subjectProbs.get(index)/100.d, likelihood));
					}	
					index++;*/				
				}
				break;
			case SOCINT:
				if(location.getIntelReport() == null || location.getIntelReport().getSocintInfo() == null) {
					throw new ScoreComputerException("Error, missing SOCINT for location " + location.getId());
				}	
				for(GroupCenter groupCenter : groupCenters) {					
					GroupType group = groupCenter.getGroup();
					SocintRule rule = rules.getSocintRule(group);
					if(rule == null) {
						throw new ScoreComputerException("Error, missing SOCINT rule for Group " + group);
					}
					if(location.getIntelReport().getSocintInfo() == group) {
						likelihoods.add(rule.getAttackLikelihood_inRegion());
					} else {
						likelihoods.add(rule.getAttackLikelihood_outsideRegion());
					}
					/*double likelihood = rule.getAttackLikelihood_outsideRegion();
					if(location.getIntelReport().getSocintInfo() == group) {
						likelihood = rule.getAttackLikelihood_inRegion();
					}
					if(cumulativeProbs != null && index < cumulativeProbs.size()) {
						cumulativeProbs.set(index, updateStrategy.computePosterior(cumulativeProbs.get(index)/100.d, likelihood));
					}
					if(incrementalProbs != null && index < incrementalProbs.size()) {
						incrementalProbs.set(index, updateStrategy.computePosterior(incrementalProbs.get(index)/100.d, likelihood));
					}
					if(subjectProbs != null && index < subjectProbs.size()) {
						subjectProbs.set(index, updateStrategy.computePosterior(subjectProbs.get(index)/100.d, likelihood));
					}	
					index++;*/				
				}
				break;
			default:
				break;
			}			
			
			//Compute posteriors using the cumulative probabilities as the priors and the likelihoods
			if(cumulativeProbs != null && !cumulativeProbs.isEmpty()) {
				cumulativeProbs = updateStrategy.computePosteriors(cumulativeProbs, likelihoods, cumulativeProbs);
				ProbabilityUtils.normalizePercentProbabilities_Double(cumulativeProbs, cumulativeProbs);
			}
			//Compute posteriors using the incremental probabilities as the priors and the likelihoods
			if(incrementalProbs != null && !incrementalProbs.isEmpty()) {
				incrementalProbs = updateStrategy.computePosteriors(incrementalProbs, likelihoods, incrementalProbs);
				ProbabilityUtils.normalizePercentProbabilities_Double(incrementalProbs, incrementalProbs);
			}
			//Compute posteriors using the subject probabilities as the priors and the likelihoods
			if(subjectProbs != null && !subjectProbs.isEmpty()) {
				subjectProbs = updateStrategy.computePosteriors(subjectProbs, likelihoods, subjectProbs);
				ProbabilityUtils.normalizePercentProbabilities_Double(subjectProbs, subjectProbs);
			}
		} catch(Exception ex) {
			//ex.printStackTrace();
			throw new ScoreComputerException("Error computing updated probabilities: " + ex.toString(), ex);
		}
	}
	
	/**
	 * Computes the expected utility of choosing the given INT in Task 6. Expected utility is computed as 
	 * the sum of the information gain of each possible outcome of the INT times the probability of that outcome.
	 * 
	 * @param groupCenters the group centers
	 * @param location the attack location
	 * @param centersToAttackDistances the distances from each group center to the attack location (miles)
	 * @param intLayer the INT layer 
	 * @param cumulativeProbs the current cumulative Bayesian attack probabilities for each group
	 * @param incrementalProbs the current incremental Bayesian attack probabilities for each group
	 * @param subjectProbs the current subject/model probabilities for each group
	 * @param rules the probability rules to use
	 * @return the utility based on the cumulative and incremental Bayesian probabilities
	 * @throws ScoreComputerException
	 */
	public static ProbabilitySet computeExpectedUtilityOfINT_Task_6(ArrayList<GroupCenter> groupCenters, 
			GroupAttack location, 
			ArrayList<Double> centersToAttackDistances,
			IntLayer intLayer, 
			ArrayList<Double> cumulativeProbs,
			ArrayList<Double> incrementalProbs,
			ArrayList<Double> subjectProbs,
			ProbabilityRules rules) throws ScoreComputerException {
		if(groupCenters == null || groupCenters.isEmpty()) {
			throw new ScoreComputerException("Error computing expected utility of choosing " + intLayer.getLayerType() +
					": group centers missing");
		}
		
		//if(cumulativeProbs == null || cumulativeProbs.isEmpty()) {
		//	throw new ScoreComputerException("Error computing expected utility of choosing " + intLayer.getLayerType() + 
		//			": cumulative probabilities missing");
		//}
		GroupCenter sigintGroupCenter = null;
		ArrayList<GroupCenter> groupCentersCopy = groupCenters;
		GroupAttack locationCopy = location;
		if(intLayer.getLayerType() == IntType.SIGINT) {
			//Make a copy of the group centers as we will be changing the SIGINT report for the SIGINT group
			GroupType sigintGroup = ((SigintLayer)intLayer).getGroup();
			if(sigintGroup == null) {
				throw new ScoreComputerException("Error computing expected utility of choosing SIGINT: SIGINT group missing");
			}
			groupCentersCopy = new ArrayList<GroupCenter>(groupCenters.size());
			for(GroupCenter groupCenter : groupCenters) {
				if(groupCenter.getGroup() == sigintGroup) {
					sigintGroupCenter = new GroupCenter(groupCenter.getGroup(), groupCenter.getLocation(),
							new LocationIntelReport(groupCenter.getIntelReport()));
					groupCentersCopy.add(sigintGroupCenter);
				} else {
					groupCentersCopy.add(groupCenter);
				}
			}
		} else {
			//Make a copy of the location as we will be changing the IMINT, MOVINT, or SOCINT report at the location						
			locationCopy = new GroupAttack(location.getGroup(), location.getLocation(), 
					new LocationIntelReport(location.getIntelReport()));
		}
		
		//Compute the probabilities of each possible outcome of the INT
		Map<Object, ProbabilitySet> probsOfInt = computeProbsOfInt_Task_5_6(groupCentersCopy, 
				location, intLayer, cumulativeProbs, incrementalProbs, subjectProbs, rules);
		
		//Compute the expected utility of choosing the INT based on the expected information gain for each possible outcome of the INT
		//and the probability of each possible outcome of the INT
		Double cumulativeUtility = null;		
		Double incrementalUtility = null;
		Double subjectUtility = null;
		if(cumulativeProbs != null) {
			cumulativeUtility = 0d;
		}
		if(incrementalProbs != null) {
			incrementalUtility = 0d;
		}
		if(subjectProbs != null) {
			subjectUtility = 0d;
		}
		for(Map.Entry<Object, ProbabilitySet> entry : probsOfInt.entrySet()) {
			//Make a copy of the current cumulative Bayesian, incremental Bayesian, and subject probabilities
			ArrayList<Double> updatedCumulativeProbs = null;
			ArrayList<Double> updatedIncrementalProbs = null;
			ArrayList<Double> updatedSubjectProbs = null;
			if(cumulativeProbs != null) {
				updatedCumulativeProbs = ProbabilityUtils.cloneProbabilities_Double(cumulativeProbs);
			}
			if(incrementalProbs != null) {
				updatedIncrementalProbs = ProbabilityUtils.cloneProbabilities_Double(incrementalProbs);
			}
			if(subjectProbs != null) {
				updatedSubjectProbs = ProbabilityUtils.cloneProbabilities_Double(subjectProbs);
			}
					
			//Compute the updated probabilities given that the INT was observed
			switch(intLayer.getLayerType()) {
			case IMINT:			
				locationCopy.getIntelReport().setImintInfo((ImintType)entry.getKey());
				break;
			case MOVINT:
				locationCopy.getIntelReport().setMovintInfo((MovintType)entry.getKey());
				break;
			case SIGINT:
				sigintGroupCenter.getIntelReport().setSigintInfo((SigintType)entry.getKey());
				break;
			case SOCINT:			
				locationCopy.getIntelReport().setSocintInfo((GroupType)entry.getKey());
				break;
			default:
				break;
			}			
			updateGroupProbabilities_Task_5_6(groupCentersCopy, locationCopy, centersToAttackDistances, 
					intLayer, updatedCumulativeProbs, updatedIncrementalProbs, updatedSubjectProbs, rules);
			
			//Compute KLD (information gain measure) between the current probabilities and the updated probabilities,
			//and compute the expected utility (KLD * probability of seeing the INT)
			if(cumulativeProbs != null) {
				double infoGain = computeKLDivergence(ProbabilityUtils.normalizePercentProbabilities_Double(cumulativeProbs, null, EPSILON), 
						ProbabilityUtils.normalizePercentProbabilities_Double(updatedCumulativeProbs, null, EPSILON));
				cumulativeUtility += infoGain *	(entry.getValue().cumulativeProb/100d);
				if(DEBUG) {
					System.out.println("Information gain for " + formatLayerName(intLayer) + "-" + entry.getKey() + ": " + infoGain + ", probability: " + 
							(entry.getValue().cumulativeProb/100d) + ", expected utility: " + (infoGain * (entry.getValue().cumulativeProb)/100d) + 
							", probs: " + updatedCumulativeProbs);
				}
			}			
			if(incrementalProbs != null) {
				double infoGain = computeKLDivergence(ProbabilityUtils.normalizePercentProbabilities_Double(incrementalProbs, null, EPSILON), 
						ProbabilityUtils.normalizePercentProbabilities_Double(updatedIncrementalProbs, null, EPSILON));
				incrementalUtility += infoGain * (entry.getValue().incrementalProb/100d);
			}
			if(subjectProbs != null) {
				double infoGain = computeKLDivergence(ProbabilityUtils.normalizePercentProbabilities_Double(subjectProbs, null, EPSILON), 
						ProbabilityUtils.normalizePercentProbabilities_Double(updatedSubjectProbs, null, EPSILON));
				subjectUtility += infoGain * (entry.getValue().subjectProb/100d);	
				/*if(infoGain < 0) {
					System.err.println("Info gain negative: " + infoGain + ", Probs 1: " + ScoreComputer.normalizeProbabilities(subjectProbs, EPSILON) + 
							", Probs 2: " + ScoreComputer.normalizeProbabilities(updatedSubjectProbs, EPSILON));
				}*/
			}
		}	
		if(DEBUG) {
			System.out.println("Expected information gain for " + formatLayerName(intLayer) + ": " + cumulativeUtility);
		}
		return new ProbabilitySet(cumulativeUtility, incrementalUtility, subjectUtility);
	}
	
	public static String formatLayerName(IntLayer layer) {
		if(layer.getLayerType() == IntType.SIGINT) {
			return layer.getLayerType().toString() + "-" +  
				((SigintLayer)layer).getGroup().toString();
		}
		return layer.getLayerType().toString();
	}
	
	public static void main(String[] args) {
		System.out.println(ScoreComputer.computeKLDivergence(
				new ArrayList<Double>(Arrays.asList(.25d, .25d, .25d, .25d)), 
			new ArrayList<Double>(Arrays.asList(.40d, .40d, 0.10d, 0.10d))));
		ArrayList<GroupCenter> groupCenters = new ArrayList<GroupCenter>(Arrays.asList(
				new GroupCenter(GroupType.A, new GridLocation2D(), new LocationIntelReport()),
				new GroupCenter(GroupType.B, new GridLocation2D(), new LocationIntelReport()),
				new GroupCenter(GroupType.C, new GridLocation2D(), new LocationIntelReport()),
				new GroupCenter(GroupType.D, new GridLocation2D(), new LocationIntelReport())));
		GroupAttack location = new GroupAttack(null, new GridLocation2D("1"), new LocationIntelReport());
		ArrayList<Double> centersToAttackDistances = new ArrayList<Double>(Arrays.asList(5d, 5d, 5d, 5d));
		//IntLayer intLayer = new ImintLayer();
		//IntLayer intLayer = new MovintLayer();
		//IntLayer intLayer = new SigintLayer(GroupType.A);		
		IntLayer intLayer = new SocintLayer();
		ArrayList<Double> cumulativeProbs = ProbabilityUtils.createProbabilities_Double(4, 10D);
		cumulativeProbs.set(0, 70D);
		System.out.println(cumulativeProbs);
		ProbabilityRules rules = ProbabilityRules.createDefaultProbabilityRules();		
		try {
			ScoreComputer.computeExpectedUtilityOfINT_Task_6(groupCenters, location, centersToAttackDistances, intLayer, 
					cumulativeProbs, null, null, rules);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		/*//Compute normative layer selections
		computer.computeSolutionAndScoreForTrial(task6Trial, ProbabilityRules.createDefaultProbabilityRules(), new GridSize(), false);
		//Change first layer selection to IMINT
		Task_5_6_AttackLocationProbeResponseAfterINT intResponse = task6Trial.getTrialResponse().getAttackLocationResponses_afterINTs().get(0);
		intResponse.setIntLayerShown(new INTLayerData(new ImintLayer(), true));
		intResponse.setAttackLocationResponse(...); //Set to model's probabilities
		//Call computeSolutionAndScoreForTrial again to get new normative probabilities after selecting IMINT
		computer.computeSolutionAndScoreForTrial(task6Trial, ProbabilityRules.createDefaultProbabilityRules(), new GridSize(), false);*/
	}
	
	/**
	 * Compute the probability of each possible outcome of the given INT layer.
	 * 
	 * @param groupCenters the group centers
	 * @param location the attack location
	 * @param intLayer the INT layer
	 * @param cumulativeProbs the current cumulative Bayesian attack probabilities for each group
	 * @param incrementalProbs the current incremental Bayesian attack probabilities for each group (if any)
	 * @param subjectProbs the current incremental subject/model attack probabilities for each group (if any)
	 * @param rules the probability rules to use
	 * @return a map that maps each possible INT outcome (e.g., government or military facility for IMINT) to the probability of that outcome
	 */
	protected static Map<Object, ProbabilitySet> computeProbsOfInt_Task_5_6(ArrayList<GroupCenter> groupCenters, 
			GroupAttack location, IntLayer intLayer, ArrayList<Double> cumulativeProbs,	ArrayList<Double> incrementalProbs, 
			ArrayList<Double> subjectProbs, ProbabilityRules rules) {
		Map<Object, ProbabilitySet> probsOfInt = null;
		ArrayList<Double> cumulativeProbsOfInt = null;
		ArrayList<Double> incrementalProbsOfInt = null;
		ArrayList<Double> subjectProbsOfInt = null;
		GroupType sigintGroup = null;
		Object[] intPossibilities = null;		
		switch(intLayer.getLayerType()) {
		case IMINT:
			intPossibilities = ImintType.values();
			break;
		case MOVINT:
			intPossibilities = MovintType.values();
			break;
		case SIGINT:
			intPossibilities = SigintType.values();
			sigintGroup = ((SigintLayer)intLayer).getGroup();
			break;
		case SOCINT:
			if(groupCenters != null && !groupCenters.isEmpty()) {
				intPossibilities = new GroupType[groupCenters.size()];
				int i = 0;
				for(GroupCenter groupCenter : groupCenters) {
					intPossibilities[i] = groupCenter.getGroup();
					i++;
				}
			}
			break;
		default:
			break;
		}
		
		if(intPossibilities != null) {
			if(cumulativeProbs != null && !cumulativeProbs.isEmpty()) {
				cumulativeProbsOfInt = new ArrayList<Double>(intPossibilities.length);
			}
			if(incrementalProbs != null && !incrementalProbs.isEmpty()) {
				incrementalProbsOfInt = new ArrayList<Double>(intPossibilities.length);
			}
			if(subjectProbs != null && !subjectProbs.isEmpty()) {
				subjectProbsOfInt = new ArrayList<Double>(intPossibilities.length);
			}
			for(Object intPossibility : intPossibilities) {
				Double cumulativeProbOfInt = null;
				Double incrementalProbOfInt = null;
				Double subjectProbOfInt = null;
				if(cumulativeProbs != null) {
					cumulativeProbOfInt = 0d;
				}
				if(incrementalProbs != null) {
					incrementalProbOfInt = 0d;
				}
				if(subjectProbs != null) {
					subjectProbOfInt = 0d;
				}
				int i = 0;
				for(GroupCenter group : groupCenters) {
					double likelihood = 0d;
					switch(intLayer.getLayerType()) {
					case IMINT:						
						likelihood = rules.getImintRule(group.getGroup()).getAttackLikelihood((ImintType)intPossibility);
						break;
					case MOVINT:
						likelihood = rules.getMovintRule(group.getGroup()).getAttackLikelihood((MovintType)intPossibility);
						break;
					case SIGINT:
						if(group.getGroup() == sigintGroup) {
							likelihood = rules.getSigintRule(sigintGroup).getAttackLikelihood(
								(SigintType)intPossibility, true);
						} else {
							likelihood = rules.getSigintRule(group.getGroup()).getAttackLikelihood(
								(SigintType)intPossibility, false);
						}
						break;
					case SOCINT:
						GroupType socintGroup = (GroupType)intPossibility;
						if(group.getGroup() == socintGroup) {
							likelihood = rules.getSocintRule(group.getGroup()).getAttackLikelihood_inRegion();
						} else {
							likelihood = rules.getSocintRule(group.getGroup()).getAttackLikelihood_outsideRegion();
						}
						break;
					default:
						break;
					}
					if(cumulativeProbs != null) {
						cumulativeProbOfInt += likelihood * cumulativeProbs.get(i);
					}
					if(incrementalProbs != null) {
						incrementalProbOfInt += likelihood * incrementalProbs.get(i);
					}
					if(subjectProbs != null) {
						subjectProbOfInt += likelihood * subjectProbs.get(i);
					}
					i++;
				}
				if(cumulativeProbsOfInt != null) {
					cumulativeProbsOfInt.add(cumulativeProbOfInt);
				}				
				if(incrementalProbsOfInt != null) {
					incrementalProbsOfInt.add(incrementalProbOfInt);
				}
				if(subjectProbsOfInt != null) {
					subjectProbsOfInt.add(subjectProbOfInt);
				}
			}
			if(cumulativeProbsOfInt != null) {
				ProbabilityUtils.normalizePercentProbabilities_Double(cumulativeProbsOfInt, cumulativeProbsOfInt);
			}
			if(incrementalProbsOfInt != null) {
				ProbabilityUtils.normalizePercentProbabilities_Double(incrementalProbsOfInt, incrementalProbsOfInt);
			}
			if(subjectProbsOfInt != null) {
				ProbabilityUtils.normalizePercentProbabilities_Double(subjectProbsOfInt, subjectProbsOfInt);
			}
			probsOfInt = new HashMap<Object, ProbabilitySet>(intPossibilities.length);
			int i = 0;
			for(Object intPossibility : intPossibilities) {
				probsOfInt.put(intPossibility, new ProbabilitySet(
						cumulativeProbsOfInt !=  null ? cumulativeProbsOfInt.get(i) : null,
						incrementalProbsOfInt !=  null ? incrementalProbsOfInt.get(i) : null,
						subjectProbsOfInt !=  null ? subjectProbsOfInt.get(i) : null));
				i++;
			}
		}				
		return probsOfInt;
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment.phase_1.score_computer.IScoreComputer#computeSolutionAndScoreForTrial(org.mitre.icarus.cps.exam.phase_1.testing.Task_7_Trial, double, int)
	 */
	@Override
	public TrialFeedback_Phase1 computeSolutionAndScoreForTrial(Task_7_Trial trial, double startingCredits, int correctPredictionCredits) {
		TrialFeedback_Phase1 feedback = new TrialFeedback_Phase1();
		if(trial.getTrialResponse() == null) {
			feedback.setErrors("Trial response missing");
			return feedback;
		}		
		Task_7_TrialResponse response = trial.getTrialResponse();
		//Compute the troop allocation score for the trial (S2)
		if(trial.getTroopAllocationProbe() == null || trial.getTroopAllocationProbe().getLocations() == null) {
			feedback.setErrors("Troop allocation missing, cannot compute credits to award");
			return feedback;
		}
		if(response.getTroopAllocationResponse() == null || 
				response.getTroopAllocationResponse().getTroopAllocations() == null ||
				response.getTroopAllocationResponse().getTroopAllocations().size() != 
				trial.getTroopAllocationProbe().getLocations().size()) {
			feedback.setErrors("Troop allocation response missing, cannot compute credits to award");
			return feedback;
		}
		if(trial.getGroundTruth() == null || trial.getGroundTruth().getAttackLocationId() == null) {
			feedback.setErrors("Ground truth missing, cannot compute credits to award");
			return feedback;
		}
		feedback.setTroopAllocationScore_s2(computeTroopAllocationScoreS2_MultiLocation(
				response.getTroopAllocationResponse().getTroopAllocations(), 
				trial.getGroundTruth().getAttackLocationId()));
		response.getTroopAllocationResponse().setTroopAllocationScore_s2(feedback.getTroopAllocationScore_s2());
		
		//Always reveal the attack location in the ground truth object, but only reveal the attack group if true
		//feedback.setGroundTruth(trial.getGroundTruth());
		if(trial.getGroundTruth() != null) {
			GroundTruth groundTruth = new GroundTruth(trial.getGroundTruth().getAttackLocationId());
			if(trial.isResponsibleGroupShown()) {
				groundTruth.setResponsibleGroup(trial.getGroundTruth().getResponsibleGroup());
			}
			feedback.setGroundTruth(groundTruth);
		}				

		//Compute the number of credits awarded and add it to the starting credits
		double creditsRemaining = startingCredits;
		if(feedback.getTroopAllocationScore_s2() != null) {
			feedback.setNumCreditsAwarded((feedback.getTroopAllocationScore_s2()/100.d) * correctPredictionCredits);
			creditsRemaining += feedback.getNumCreditsAwarded();				
		} else {
			feedback.setErrors("Missing one or more troop allocations");
		}

		//Deducts the credits used purchasing INT layers
		double creditsUsed = 0;
		if(response.getIntLayerPurchases() != null && !response.getIntLayerPurchases().isEmpty()) {
			for(Task_7_INTLayerPurchase intLayer : response.getIntLayerPurchases()) {
				creditsUsed += intLayer.getCostCredits();
			}
		}
		creditsRemaining -= creditsUsed;
		feedback.setNumCreditsUsed(creditsUsed);
		feedback.setNumCreditsRemaining(creditsRemaining);
		if(creditsRemaining < 0) {
			feedback.setErrors("Credits remaining is negative.");
		}	

		return feedback;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment.phase_1.score_computer.IScoreComputer#computeProbabilitiesScoreS1(java.util.ArrayList, java.util.ArrayList)
	 */
	@Override
	public Double computeProbabilitiesScoreS1(List<Integer> subjectProbs, List<Double> actualProbs) {
		if(actualProbs != null && !actualProbs.isEmpty()) {
			return computeScore(subjectProbs, ProbabilityUtils.convertPercentProbsToDecimalProbs_Double(actualProbs),
					EPSILON);
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment.phase_1.score_computer.IScoreComputer#computeProbabilitiesScoreS1_FromResponses(java.util.ArrayList, java.util.ArrayList)
	 */
	@Override
	public Double computeProbabilitiesScoreS1_FromResponses(List<GroupAttackProbabilityResponse> subjectProbs, 
			List<Double> actualProbs) {
		if(subjectProbs != null && !subjectProbs.isEmpty()) {
			ArrayList<Integer> probs = new ArrayList<Integer>(subjectProbs.size());
			for(GroupAttackProbabilityResponse prob : subjectProbs) {
				probs.add((int)Math.round(prob.getProbability()));
			}
			return computeProbabilitiesScoreS1(probs, actualProbs);
		}
		return null;
	}	
	
	/**
	 * Creates a Double array of probabilities given subject probability responses.
	 * 
	 * @param subjectProbs
	 * @return
	 */
	protected ArrayList<Double> createSubjectProbs(ArrayList<GroupAttackProbabilityResponse> subjectProbs) {
		ArrayList<Double> probs = null;
		if(subjectProbs != null && !subjectProbs.isEmpty()) {
			probs = new ArrayList<Double>(subjectProbs.size());
			for(GroupAttackProbabilityResponse prob : subjectProbs) {
				probs.add(prob.getProbability());
			}
		}
		return probs;
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment.phase_1.score_computer.IScoreComputer#computeTroopAllocationScoreS2_SingleGroupAllocation(org.mitre.icarus.cps.feature_vector.phase_1.GroupType, org.mitre.icarus.cps.feature_vector.phase_1.GroupType)
	 */
	@Override
	public Double computeTroopAllocationScoreS2_SingleGroupAllocation(GroupType troopSelectionGroup, GroupType responsibleGroup) {
		if(troopSelectionGroup != null && responsibleGroup != null &&
				troopSelectionGroup == responsibleGroup) {
			return 100D;
		}
		return 0D;
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment.phase_1.score_computer.IScoreComputer#computeTroopAllocationScoreS2(java.util.ArrayList, int)
	 */
	@Override
	public Double computeTroopAllocationScoreS2(List<Integer> troopAllocations, int groundTruthIndex) {
		ProbabilityUtils.normalizePercentProbabilities(troopAllocations, troopAllocations);
		//normalizeSubjectProbabilities(troopAllocations, 0.d);
		if(troopAllocations != null && groundTruthIndex >= 0 && groundTruthIndex < troopAllocations.size()) {
			return troopAllocations.get(groundTruthIndex).doubleValue();
		}
		return null;
	}	
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment.phase_1.score_computer.IScoreComputer#computeTroopAllocationScoreS2_MultiGroup(java.util.ArrayList, org.mitre.icarus.cps.feature_vector.phase_1.GroupType)
	 */
	@Override
	public Double computeTroopAllocationScoreS2_MultiGroup(List<TroopAllocation> troopAllocations,
			GroupType responsibleGroup) {
		if(troopAllocations != null && !troopAllocations.isEmpty()) {
			ArrayList<Integer> allocations = new ArrayList<Integer>(troopAllocations.size());
			int groundTruthIndex = -1;
			int index = 0;
			for(TroopAllocation allocation : troopAllocations) {
				allocations.add((int)Math.round(allocation.getAllocation()));
				if(groundTruthIndex == -1 && allocation.getGroup() == responsibleGroup) {
					groundTruthIndex = index;
				}
				index++;
			}
			if(groundTruthIndex != -1) {
				return computeTroopAllocationScoreS2(allocations, groundTruthIndex);
			}			
		}
		return null;
	}	
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment.phase_1.score_computer.IScoreComputer#computeTroopAllocationScoreS2_MultiLocation(java.util.List, java.lang.String)
	 */
	@Override
	public Double computeTroopAllocationScoreS2_MultiLocation(List<TroopAllocation> troopAllocations,
			String actualLocationId) {		  
		if(troopAllocations != null && !troopAllocations.isEmpty()) {
			ArrayList<Integer> allocations = new ArrayList<Integer>(troopAllocations.size());
			int groundTruthIndex = -1;
			int index = 0;
			for(TroopAllocation allocation : troopAllocations) {
				allocations.add((int)Math.round(allocation.getAllocation()));
				if(groundTruthIndex == -1 && allocation.getLocationId().equals(actualLocationId)) {
					groundTruthIndex = index;
				}
				index++;
			}
			if(groundTruthIndex != -1) {
				return computeTroopAllocationScoreS2(allocations, groundTruthIndex);
			}			
		}
		return null;		
	}	
	
	/**
	 * Contains attack dispersion parameters for Tasks 1-3.
	 * 
	 * @author CBONACETO
	 *
	 */
	protected static class GroupAttackDispersionParameters {
		
		public double centerX = 0;
		
		public double centerY = 0;
		
		public double meanX = 0; //Mean x value
		
		public double meanY = 0; //Mean y value
		
		public double sigmaX = 0; //Sigma of the x values
		
		public double sigmaY = 0; //Sigma of the y values
		
		public double sigmaXY = 0; //Sigma computed by treating the X,Y values as a single collection of values
		
		public double likelihoodXY = 0;
		
		public int numAttacks = 0;
		
		public double totalDistance = 0;
	}
	
	/**
	 * Contains a cumulative Bayesian probability, an incremental Bayesian probability, and a subject probability.
	 *  
	 * @author CBONACETO
	 *
	 */
	public static class ProbabilitySet {
		public Double cumulativeProb;		
		public Double incrementalProb;
		public Double subjectProb;
		
		public ProbabilitySet() {}
		
		public ProbabilitySet(Double cumulativeProb, Double incrementalProb, Double subjectProb) {
			this.cumulativeProb = cumulativeProb;
			this.incrementalProb = incrementalProb;
			this.subjectProb = subjectProb;
		}

		public Double getCumulativeProb() {
			return cumulativeProb;
		}

		public Double getIncrementalProb() {
			return incrementalProb;
		}

		public Double getSubjectProb() {
			return subjectProb;
		}
	}
}