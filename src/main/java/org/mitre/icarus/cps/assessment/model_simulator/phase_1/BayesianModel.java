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
package org.mitre.icarus.cps.assessment.model_simulator.phase_1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import org.mitre.icarus.cps.exam.phase_1.testing.probability_rules.update_strategy.BayesianUpdateStrategy;
import org.mitre.icarus.cps.feature_vector.phase_1.IntType;

/**
 * A Phase 1 model that uses a Bayesian strategy to update probabilities for each
 * task.
 * 
 * @author CBONACETO
 *
 */
public class BayesianModel extends AbModel {

    public BayesianModel() {
        rand = new Random(1L);
        task6Layers = new ArrayList<IntType>(Arrays.asList(IntType.IMINT, IntType.MOVINT, IntType.SIGINT));
        BayesianUpdateStrategy bayesianStrategy = new BayesianUpdateStrategy();
        task_1_Strategy = bayesianStrategy;
        task_2_Strategy = bayesianStrategy;
        task_3_Strategy = bayesianStrategy;
        task_4_1_Strategy = bayesianStrategy;
        task_4_N_Strategy = bayesianStrategy;
        task_5_Strategy = bayesianStrategy;
        task_6_Strategy = bayesianStrategy;
    }
}
