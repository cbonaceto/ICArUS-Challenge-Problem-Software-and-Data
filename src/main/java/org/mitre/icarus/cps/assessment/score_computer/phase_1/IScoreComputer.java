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

import java.util.List;

import org.mitre.icarus.cps.app.widgets.progress_monitor.IProgressMonitor;
import org.mitre.icarus.cps.exam.phase_1.feedback.TaskFeedback;
import org.mitre.icarus.cps.exam.phase_1.feedback.TrialFeedback_Phase1;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.GroupAttackProbabilityResponse;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.TroopAllocation;
import org.mitre.icarus.cps.exam.phase_1.testing.AttackLocationPresentationTrial;
import org.mitre.icarus.cps.exam.phase_1.testing.TaskTestPhase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_1_2_3_ProbeTrialBase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_4_Trial;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_5_Trial;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_6_Trial;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_7_Trial;
import org.mitre.icarus.cps.exam.phase_1.testing.probability_rules.ProbabilityRules;
import org.mitre.icarus.cps.feature_vector.phase_1.GridSize;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupType;
import org.mitre.icarus.cps.feature_vector.phase_1.Road;
import org.mitre.icarus.cps.feature_vector.phase_1.road_network.IRoadDistanceCalculator;

/**
 * Interface for implementations that compute normative solutions and S1 and S2 scores
 * for trials and tasks (1-7) in the Phase 1 Challenge Problem.
 * 
 * @author CBONACETO
 *
 */
public interface IScoreComputer {
	
	/**
	 * Get whether the score computer is enabled.
	 * 
	 * @return whether the score computer is enabled
	 */
	public boolean isEnabled();
	
	/**
	 * Computes the normative solution for each trial in the task and stores it in the response
	 * object for the trial. If computeScore is true, also computes the average probability 
	 * score (S1) (for Tasks 1-6) and the average troop allocation score (S2) (for Tasks 1-6) for all trials 
	 * in the task, and returns the score results in a task feedback object. The response object for each trial 
	 * will also be populated with the feedback that trial. Any errors encountered are also tracked 
	 * in the feedback objects for each trial and the task. For Tasks 1-3, the attack dispersion
	 * parameters stored in each probe trial are used if present to compute normative solutions. 
	 * If they are not present,they are computed on-the-fly. This computation is time-consuming for Task 3. 
	 * 
	 * @param task the task to compute feedback for 
	 * @param rules the probability rules to use
	 * @param gridSize the grid size to use
	 * @param computeScore whether to compute the score
	 * @param progressMonitor an optional progress monitor
	 * @return the feedback for the task
	 */
	public TaskFeedback computeSolutionAndScoreForTask(TaskTestPhase<?> task, ProbabilityRules rules, GridSize gridSize,
			boolean computeScore, IProgressMonitor progressMonitor);	
	
	/**
	 * Computes the normative solution for a Task 1, 2, or 3 probe trial and stores it in the response
	 * object for the trial. The attack dispersion parameters stored in the trial are used if present to compute 
	 * the normative solution. If they are not present, they are computed on-the-fly. This computation is time-consuming 
	 * for Task 3. If computeScore is true, also computes the probability score (S1) and troop allocation score (S2) for the trial
	 * and returns them in the trial feedback object. Any errors encountered are also noted in the feedback object.
	 * 
	 * @param trial the trial
	 * @param rules the probability rules to use
	 * @param gridSize the grid size to use
	 * @param roads the roads (Task 3 only)
	 * @param computeScore whether to compute the score
	 * @return the feedback for the trial
	 */
	public TrialFeedback_Phase1 computeSolutionAndScoreForTrial(Task_1_2_3_ProbeTrialBase trial, ProbabilityRules rules,
			GridSize gridSize, List<Road> roads, boolean computeScore);
	
	/** 
	 * Computes the normative solution for a Task 1, 2, or 3 probe trial and stores it in the response
	 * object for the trial. The attack dispersion parameters stored in the trial are used if present to compute 
	 * the normative solution. If they are not present, they are computed on-the-fly. This computation is time-consuming 
	 * for Task 3. If computeScore is true, also computes the probability score (S1) and troop allocation score (S2) for the trial.	 * 
	 * 	 
	 * @param trial the trial
	 * @param groupAttacks the attacks that have been observed
	 * @param rules the probability rules to use
	 * @param gridSize the grid size to use
	 * @param distanceCalculator the road distance calculator to use (Task 3 only)
	 * @param computeScore whether to compute the score
	 * @return the feedback for the trial
	 */
	public TrialFeedback_Phase1 computeSolutionAndScoreForTrial(Task_1_2_3_ProbeTrialBase trial, 
			List<AttackLocationPresentationTrial> groupAttacks, ProbabilityRules rules, 
			GridSize gridSize, IRoadDistanceCalculator distanceCalculator, boolean computeScore);
	
	/**
	 * Computes the normative solution for each step in a Task 4 trial and stores it in the response
	 * object for the trial. If computeScore is true, also computes the probability score (S1) and 
	 * the troop allocation score (S2) for the trial. Any errors encountered are also noted in the feedback object.
	 * 
	 * @param trial the Task 4 trial
	 * @param rules the probability rules to use
	 * @param gridSize the grid size to use
	 * @param computeScore whether to compute the score
	 * @return the feedback for the trial
	 */
	public TrialFeedback_Phase1 computeSolutionAndScoreForTrial(Task_4_Trial trial, ProbabilityRules rules, GridSize gridSize, boolean computeScore);
	
	/**
	 * Computes the normative solution for each step in a Task 5 trial and stores it in the response
	 * object for the trial. If computeScore is true, also Computes the probability score (S1) and 
	 * troop allocation score (S2) for the trial. Any errors encountered are also noted in the feedback object.
	 * 
	 * @param trial the Task 5 trial
	 * @param rules the probability rules to use
	 * @param gridSize the grid size to use
	 * @param computeScore whether to compute the score
	 * @return the feedback for the trial
	 */
	public TrialFeedback_Phase1 computeSolutionAndScoreForTrial(Task_5_Trial trial, ProbabilityRules rules, GridSize gridSize, boolean computeScore);
	
	/**
	 * Computes the normative solution for each step in a Task 6 trial and stores it in the response
	 * object for the trial. If computeScore is true, also computes the probability score (S1) and 
	 * troop allocation score (S2) for the trial. Any errors encountered are also noted in the feedback object.
	 * 
	 * @param trial the Task 6 trial
	 * @param rules the probability rules to use
	 * @param gridSize the grid size to use
	 * @param computeScore whether to compute the score
	 * @return the feedback for the trial
	 */
	public TrialFeedback_Phase1 computeSolutionAndScoreForTrial(Task_6_Trial trial, ProbabilityRules rules, GridSize gridSize, boolean computeScore);
	
	/**
	 * Computes the troop allocation score, credits awarded, credits used, and credits remaining for a Task 7 trial.
	 * Does not compute normative solutions for Task 7.
	 *
	 * @param trial the Task 7 trial
	 * @param startingCredits the number of credits going into the given trial
	 * @param correctPredictionCredits the number of credits awarded for a correct prediction (the default is 1)
	 * @return the feedback for the trial
	 */
	public TrialFeedback_Phase1 computeSolutionAndScoreForTrial(Task_7_Trial trial, double startingCredits, int correctPredictionCredits); 
	
	/**
	 * Compute the probability score (S1) given the subject probabilities and actual (Bayesian) probabilities.
	 * The score is computed using the formula:
	 * S1 = 100 * 2^(-KLD(subjectProbs, actualProbs))  
	 * 
	 * @param subjectProbs the subject probabilities. Integer values in the range [0 100].
	 * @param actualProbs the actual probabilities. Double values in the range [0 100].
	 * @return the S1 score
	 */ 
	public Double computeProbabilitiesScoreS1(List<Integer> subjectProbs, List<Double> actualProbs);
	
	/**
	 * Computes the probability score (S1) given the subject probabilities and actual (Bayesian) probabilities.
	 * The score is computed using the formula:
	 * S1 = 100 * 2^(-KLD(subjectProbs, actualProbs))  
	 * 
	 * @param subjectProbs the subject probabilities. Integer values in the range [0 100].
	 * @param actualProbs the actual (Bayesian) probabilities. Double values in the range [0 100].
	 * @return the S1 score
	 */
	public Double computeProbabilitiesScoreS1_FromResponses(List<GroupAttackProbabilityResponse> subjectProbs, 
			List<Double> actualProbs);
	
	/**
	 * Compute the troop allocation score (S2) given a the group troops were sent against and
	 * the actual group responsible for the attack.  Returns 100 if the groups match, or 0 if they don't (Tasks 1-3).
	 * 
	 * @param troopSelectionGroup the group troops were sent against
	 * @param responsibleGroup the group actually responsible for the attack
	 * @return the S2 score
	 */
	public Double computeTroopAllocationScoreS2_SingleGroupAllocation(GroupType troopSelectionGroup, GroupType responsibleGroup);
	
	/**
	 * Computes the troop allocation score (S2) given a list of troop allocations and the index in the list
	 * that corresponds to ground truth (Tasks 4-6).
	 * 
	 * @param troopAllocations the troop allocations. Integer values in the range [0 100].
	 * @param groundTruthIndex ground truth index
	 * @return the S2 score
	 */
	public Double computeTroopAllocationScoreS2(List<Integer> troopAllocations, int groundTruthIndex);
	
	/**
	 * Computes the troop allocation score (S2) given a list of troop allocations against groups and the responsible group.
	 * 
	 * @param troopAllocations the troop allocations against each group
	 * @param responsibleGroup the responsible group
	 * @return the S2 score
	 */
	public Double computeTroopAllocationScoreS2_MultiGroup(List<TroopAllocation> troopAllocations,
			GroupType responsibleGroup);
	
	/**
	 * Computes the troop allocation score (S2) given a list of troop allocations at locations and the actual 
	 * attack location.
	 * 
	 * @param troopAllocations the troop allocations at each location
	 * @param actualLocationId the actual attack location
	 * @return the S2 score
	 */
	public Double computeTroopAllocationScoreS2_MultiLocation(List<TroopAllocation> troopAllocations,
			String actualLocationId);	
}