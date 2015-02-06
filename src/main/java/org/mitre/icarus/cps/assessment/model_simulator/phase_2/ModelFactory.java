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
package org.mitre.icarus.cps.assessment.model_simulator.phase_2;

import java.util.Random;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.SubjectSigintProbabilities;
import org.mitre.icarus.cps.assessment.model_simulator.phase_2.model_components.AbRedAttackProbabilityComponent;
import org.mitre.icarus.cps.assessment.model_simulator.phase_2.model_components.BasicRedTacticsProbabilityComponent;
import org.mitre.icarus.cps.assessment.model_simulator.phase_2.model_components.BayesianBlueActionSelectionComponent;
import org.mitre.icarus.cps.assessment.model_simulator.phase_2.model_components.BayesianRedTacticsProbabilityComponent;
import org.mitre.icarus.cps.assessment.model_simulator.phase_2.model_components.BayesianSigintSelectionComponent;
import org.mitre.icarus.cps.assessment.model_simulator.phase_2.model_components.FixedThresholdBlueActionSelectionComponent;
import org.mitre.icarus.cps.assessment.model_simulator.phase_2.model_components.RandomRedAttackProbabilityComponent;
import org.mitre.icarus.cps.assessment.model_simulator.phase_2.model_components.RandomRedTacticsProbabilityComponent;
import org.mitre.icarus.cps.assessment.model_simulator.phase_2.model_components.VariableFrequencySigintSelectionComponent;

/**
 * Factory class for creating different types of models.
 * 
 * @author CBONACETO
 */
public class ModelFactory {
    
    /**
     * Create the default "CAB" model that uses an A-B update strategy for computing
     * attack probabilities where A = 0.5 and B = 0.5.
     * 
     * @param a
     * @param b
     * @param siteId
     * @param modelId
     * @return
     */
    public static Model_Phase2 createDefaultAbModel(double a, double b, String siteId, String modelId) {
        Random rand = new Random(1);
        Model_Phase2 model = new Model_Phase2(
                new BasicRedTacticsProbabilityComponent(),
                new AbRedAttackProbabilityComponent(a, b, createMaxAvailabilitySigintProbabilities()),
                new VariableFrequencySigintSelectionComponent(rand),
                new FixedThresholdBlueActionSelectionComponent(rand, 0.5d)
                //new VariableFrequencyBlueActionSelectionComponent(rand)
        );
        model.setSiteId(siteId);
        model.setResponseGeneratorId(modelId);
        model.setDescription("Default A-B Model, where A=" + a + " and B=" + b);
        return model;
    }
    
     /**
     * Create a "CAB" model that uses a random(null model) strategy for computing
     * attack probabilities.
     * 
     * @param siteId
     * @param modelId
     * @return
     */
    public static Model_Phase2 createDefaultRandomModel(String siteId, String modelId) {
        Random rand = new Random(1);
        Model_Phase2 model = new Model_Phase2(
                new RandomRedTacticsProbabilityComponent(),
                new RandomRedAttackProbabilityComponent(),
                new VariableFrequencySigintSelectionComponent(rand),
                new FixedThresholdBlueActionSelectionComponent(rand, 0.5d)
                //new VariableFrequencyBlueActionSelectionComponent(rand)
        );
        model.setSiteId(siteId);
        model.setResponseGeneratorId(modelId);
        model.setDescription("Random(Null) Model");
        return model;
    }
    
    /**
     * Creates a "CAB" model that uses a Bayesian update strategy for computing
     * attack probabilities and a Bayesian strategy for computing Red tactic
     * probabilities.
     * 
     * @param siteId
     * @param modelId
     * @return
     */
    public static Model_Phase2 createDefaultBayesianModel(String siteId, String modelId) {
        Random rand = new Random(1);
        Model_Phase2 model = new Model_Phase2(
                new BayesianRedTacticsProbabilityComponent(),
                new AbRedAttackProbabilityComponent(1.d, 1.d, createBayesianSigintProbabilities()),
                new BayesianSigintSelectionComponent(rand),
                new BayesianBlueActionSelectionComponent(rand)
                //new VariableFrequencyBlueActionSelectionComponent(rand)
        );
        model.setSiteId(siteId);
        model.setResponseGeneratorId(modelId);
        model.setDescription("Bayesian Model");
        return model;
    }

    /**
     *
     * @return
     */
    public static SubjectSigintProbabilities createMaxAvailabilitySigintProbabilities() {
               /*
            chatterLikelihood_attack = 0.6
	 * chatterLikelihood_noAttack = 0.2
	 * silenceLikelihood_attack = 0.4
	 * silenceLikelihood_noAttack = 0.8
            */
        return new SubjectSigintProbabilities(0.6, 0.4d);
    }
    
    /**
     *
     * @return
     */
    public static SubjectSigintProbabilities createBayesianSigintProbabilities() {
               /*
            chatterLikelihood_attack = 0.6
	 * chatterLikelihood_noAttack = 0.2
	 * silenceLikelihood_attack = 0.4
	 * silenceLikelihood_noAttack = 0.8
            */
        return new SubjectSigintProbabilities(0.75, 0.33d);
    }
     
}
