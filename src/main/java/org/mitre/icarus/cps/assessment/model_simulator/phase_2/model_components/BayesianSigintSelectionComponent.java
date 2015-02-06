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

import java.util.List;
import java.util.Random;
import org.mitre.icarus.cps.assessment.score_computer.phase_2.ScoreComputer_Phase2;
import org.mitre.icarus.cps.feature_vector.phase_2.BlueLocation;

/**
 * A SIGINT selection model always selects SIGINT
 * at the location with highest P(Attack).
 * 
 * @author CBONACETO
 */
public class BayesianSigintSelectionComponent implements ISigintSelectionComponent {
    
    private static final ScoreComputer_Phase2 sc = new ScoreComputer_Phase2();
    
    /** The random number generator to use */
    private Random rand;
    
    public BayesianSigintSelectionComponent() {
        this(new Random(1));
    }
   
    public BayesianSigintSelectionComponent(Random rand) {
        this.rand = rand == null ? new Random(1) : rand;
    }    
    
    @Override
    public String selectSigintLocation(List<BlueLocation> locations, 
            List<Double> attackProbabilities) {        
        //Selects the location with the highest P(Attack)
        if (locations != null && !locations.isEmpty()
                && attackProbabilities != null && attackProbabilities.size() == locations.size()) {
            rand.nextDouble(); //Get the next random to maintain the same sequence of random numbers when using this model component    
            return sc.computeNormativeSigintSelections(ScoreComputer_Phase2.extractLocationIds(locations), 
                1, attackProbabilities).get(0);
        }
        return null;
    }
}
