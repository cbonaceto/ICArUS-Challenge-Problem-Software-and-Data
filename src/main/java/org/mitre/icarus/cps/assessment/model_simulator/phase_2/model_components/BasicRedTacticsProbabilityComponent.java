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
package org.mitre.icarus.cps.assessment.model_simulator.phase_2.model_components;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.mitre.icarus.cps.app.util.ProbabilityUtils;
import org.mitre.icarus.cps.exam.phase_2.testing.IcarusTestTrial_Phase2;
import org.mitre.icarus.cps.exam.phase_2.testing.Mission;
import org.mitre.icarus.cps.exam.phase_2.testing.MissionType;
import org.mitre.icarus.cps.exam.phase_2.testing.bluebook.RedTacticType;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.RedAction;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.RedAction.RedActionType;
import org.mitre.icarus.cps.feature_vector.phase_2.BlueLocation;

/**
 * An implementation of a Red tactics probability computer model component that 
 * counts the number of times each tactic "correctly" predicts attack/not attack,
 * and uses that frequency as the probability Red is playing with the tactic.
 * By "correctly", we mean if P > 0.5 and Red attacked, or P < 0.5 and red 
 * did not attack. Never creates a batch plot.
 * 
 * @author CBONACETO
 */
public class BasicRedTacticsProbabilityComponent implements IRedTacticsProbabilityComponent {

    @Override
    public RedTacticProbabilitiesAndBatchPlot computeRedTacticProbabilities(Mission<?> mission,
            List<RedTacticType> redTactics, IcarusTestTrial_Phase2 currentTrial,
            int numTrials, int numAttacks, boolean canCreateBatchPlot,
            List<Integer> previousTrialsToReview) {
        ArrayList<Double> redTacticProbs = new ArrayList<Double>(redTactics.size());
        if (numTrials > 0) {
            //Count the number of times each tactic "correctly" predicted attack/not attack,
            //and use that frequency as the probability Red is playing with the tactic.
            //By "correctly", we mean if P > 0.5 and Red attacked, or P < 0.5 and red 
            //did not attack
            boolean considerHumint = mission.getMissionType() == MissionType.Mission_2;
            int[] correctClassificationCounts = new int[redTactics.size()];
            int[] totalCounts = new int[redTactics.size()];
            Iterator<? extends IcarusTestTrial_Phase2> trialIter = mission.getTestTrials().iterator();
            for (int i = 0; i < numTrials; i++) {
                IcarusTestTrial_Phase2 trial = trialIter.next();
                if (trial.getBlueLocations() != null && !trial.getBlueLocations().isEmpty()) {
                    double redCapability_Pc = considerHumint && trial.getHumint() != null
                            && trial.getHumint().getRedCapability_Pc() != null
                            ? trial.getHumint().getRedCapability_Pc() : 1;
                    RedAction redAction = trial.getRedActionSelection() != null
                            ? trial.getRedActionSelection().getRedAction() : null;
                    for (BlueLocation location : trial.getBlueLocations()) {
                        double redVulnerability_P = location.getOsint() != null ? location.getOsint().getRedVulnerability_P() : 0;
                        int redOpportunity_U = location.getImint() != null ? location.getImint().getRedOpportunity_U() : 0;
                        int tacticIndex = 0;
                        boolean redAttacked = redAction != null && redAction.getAction() == RedActionType.Attack
                                && (trial.getBlueLocations().size() == 1
                                || location.getId().equalsIgnoreCase(redAction.getLocationId()));
                        for (RedTacticType tactic : redTactics) {
                            Double pAttack = tactic.getTacticParameters().getAttackProbability(
                                redVulnerability_P, redOpportunity_U) * redCapability_Pc;
                            if ((pAttack > 0.5 && redAttacked) || (pAttack < 0.5 && !redAttacked)) {
                                correctClassificationCounts[tacticIndex]++;
                            }
                            if (pAttack != 0.5) {
                                totalCounts[tacticIndex]++;
                            }
                            tacticIndex++;
                        }
                    }
                }
            }
            for (int i = 0; i < redTactics.size(); i++) {
                double tacticProb = totalCounts[i] > 0
                        ? (double) correctClassificationCounts[i] / totalCounts[i] : 0d;                
                redTacticProbs.add(tacticProb);
            }
        } else {
            //On the first trial, there is an equal probability that Red is playing with each tactic
            double tacticProb = 1.d / redTactics.size();
            for (int i = 0; i < redTactics.size(); i++) {
                redTacticProbs.add(tacticProb);
            }
        }

        //Normalize the Red tactic probabilities and return them
        ProbabilityUtils.normalizeDecimalProbabilities(redTacticProbs, redTacticProbs);
        //System.out.println("Trial " + numTrials + ", Red tactic probs: " + redTacticProbs);
        //System.out.println(redTacticProbs);
        return new RedTacticProbabilitiesAndBatchPlot(redTacticProbs);
    }
}