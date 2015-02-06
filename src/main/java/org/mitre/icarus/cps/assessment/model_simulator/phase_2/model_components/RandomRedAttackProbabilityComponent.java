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
import java.util.List;
import org.mitre.icarus.cps.assessment.score_computer.phase_2.ScoreComputer_Phase2;
import org.mitre.icarus.cps.exam.phase_2.testing.bluebook.RedTacticParameters;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbeType;
import org.mitre.icarus.cps.feature_vector.phase_2.BlueLocation;
import org.mitre.icarus.cps.feature_vector.phase_2.int_datum.HumintDatum;
import org.mitre.icarus.cps.feature_vector.phase_2.int_datum.SigintReliability;

/**
 * Model component that assesses the probability of Red attack using a random(null) model.
 * Always sets probabilities to uniform values (e.g., all 0.5, all 0.25, etc). 
 * 
 * @author CBONACETO
 */
public class RandomRedAttackProbabilityComponent implements IRedAttackProbabilityComponent {
    
    public RandomRedAttackProbabilityComponent() {}    
    
    @Override
    public List<ScoreComputer_Phase2.RedAttackProbabilities> computeRedAttackProbs(
            List<ScoreComputer_Phase2.RedAttackProbabilities> attackProbabilities, TrialPartProbeType currStage,
            List<BlueLocation> locations, List<RedTacticParameters> redTactics, 
            List<Double> redTacticProbs, HumintDatum humint, SigintReliability sigintReliability,
            List<String> sigintLocations) {
        if (locations != null && !locations.isEmpty()) {
            int currStageNum = getAttackProbabilityStageNum(currStage);
            int numLocations = locations.size();
            if (attackProbabilities == null || attackProbabilities.size() != locations.size()) {
                attackProbabilities = new ArrayList<ScoreComputer_Phase2.RedAttackProbabilities>(numLocations);
                for (int i = 0; i < locations.size(); i++) {
                    ScoreComputer_Phase2.RedAttackProbabilities probs = new ScoreComputer_Phase2.RedAttackProbabilities();
                    attackProbabilities.add(probs);
                }
            }
            Double uniformProb = 1.d / (locations.size() + 1);
            for (ScoreComputer_Phase2.RedAttackProbabilities probs : attackProbabilities) {
                if (currStage == TrialPartProbeType.AttackProbabilityReport_Pp
                        || (currStageNum > 0 && probs.getpPropensity() == null)) {
                    probs.pPropensity = uniformProb;
                }
                if (currStage == TrialPartProbeType.AttackProbabilityReport_Ppc
                        || (currStageNum > 1 && probs.getpPropensityCapability() == null)) {
                    probs.pPropensityCapability = uniformProb;
                }
                if (currStage == TrialPartProbeType.AttackProbabilityReport_Pt
                        || (currStageNum > 2 && probs.getpActivity() == null)) {
                    probs.pActivity = uniformProb;
                }
                if (currStage == TrialPartProbeType.AttackProbabilityReport_Ptpc) {
                    probs.pActivityPropensityCapability = uniformProb;
                }
            }
        }
        return attackProbabilities;
    }
    
    /**
     *
     * @param currStage
     * @return
     */
    protected static int getAttackProbabilityStageNum(TrialPartProbeType currStage) {
        if (currStage == TrialPartProbeType.AttackProbabilityReport_Pp) {
            return 0;
        } else if (currStage == TrialPartProbeType.AttackProbabilityReport_Ppc) {
            return 1;
        } else if (currStage == TrialPartProbeType.AttackProbabilityReport_Pt) {
            return 2;
        } else if (currStage == TrialPartProbeType.AttackProbabilityReport_Ptpc) {
            return 3;
        } else {
            return 0;
        }
    }
}