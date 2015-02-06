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
 * Score computer implementation that does nothing except compute the troop allocation score.
 * 
 * @author CBONACETO
 *
 */
public class DoNothingScoreComputer implements IScoreComputer {
	
	@Override
	public boolean isEnabled() {
		return false;
	}


	@Override
	public TaskFeedback computeSolutionAndScoreForTask(TaskTestPhase<?> task,
			ProbabilityRules rules, GridSize gridSize, boolean computeScore,
			IProgressMonitor progressMonitor) {
		return null;
	}

	@Override
	public TrialFeedback_Phase1 computeSolutionAndScoreForTrial(
			Task_1_2_3_ProbeTrialBase trial, ProbabilityRules rules,
			GridSize gridSize, List<Road> roads, boolean computeScore) {
		return null;
	}

	@Override
	public TrialFeedback_Phase1 computeSolutionAndScoreForTrial(
			Task_1_2_3_ProbeTrialBase trial,
			List<AttackLocationPresentationTrial> groupAttacks,
			ProbabilityRules rules, GridSize gridSize,
			IRoadDistanceCalculator distanceCalculator, boolean computeScore) {
		return null;
	}

	@Override
	public TrialFeedback_Phase1 computeSolutionAndScoreForTrial(
			Task_4_Trial trial, ProbabilityRules rules, GridSize gridSize,
			boolean computeScore) {
		return null;
	}

	@Override
	public TrialFeedback_Phase1 computeSolutionAndScoreForTrial(
			Task_5_Trial trial, ProbabilityRules rules, GridSize gridSize,
			boolean computeScore) {
		return null;
	}

	@Override
	public TrialFeedback_Phase1 computeSolutionAndScoreForTrial(
			Task_6_Trial trial, ProbabilityRules rules, GridSize gridSize,
			boolean computeScore) {
		return null;
	}

	@Override
	public TrialFeedback_Phase1 computeSolutionAndScoreForTrial(
			Task_7_Trial trial, double startingCredits,
			int correctPredictionCredits) {
		return null;
	}	


	@Override
	public Double computeProbabilitiesScoreS1(List<Integer> subjectProbs,
			List<Double> actualProbs) {	
		return null;
	}

	@Override
	public Double computeProbabilitiesScoreS1_FromResponses(
			List<GroupAttackProbabilityResponse> subjectProbs,
			List<Double> actualProbs) {		
		return null;
	}
	
	private static List<Double> normalizeSubjectProbabilities(List<Integer> probs, double epsilon) {
		List<Double> normalizedProbs = new ArrayList<Double>(probs.size());		
		double sum = 0;
		for(Integer prob : probs) {
			if(prob == null) { 
				return null;
			}
			double probD = prob * 0.01d;
			if(probD < epsilon) {
				probD = epsilon;
			}
			else if(probD > (1-epsilon)) {
				probD = 1-epsilon;
			}
			normalizedProbs.add(probD);			
			sum += probD;
		}		
		
		//Renormalize
		for(int i=0; i<normalizedProbs.size(); i++) {
			normalizedProbs.set(i, normalizedProbs.get(i)/sum);
		}
		return normalizedProbs;		
	}	

	@Override
	public Double computeTroopAllocationScoreS2_SingleGroupAllocation(GroupType troopSelectionGroup, GroupType responsibleGroup) {		
		if(troopSelectionGroup != null && responsibleGroup != null &&
				troopSelectionGroup == responsibleGroup) {
			return 100D;
		}
		return 0D;
	}

	@Override
	public Double computeTroopAllocationScoreS2(List<Integer> troopAllocations, int groundTruthIndex) {		
		normalizeSubjectProbabilities(troopAllocations, 0.d);
		if(troopAllocations != null && groundTruthIndex >= 0 && groundTruthIndex < troopAllocations.size()) {
			return troopAllocations.get(groundTruthIndex).doubleValue();
		}
		return null;
	}

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
}