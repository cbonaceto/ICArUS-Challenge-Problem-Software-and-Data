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

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import org.mitre.icarus.cps.assessment.score_computer.phase_2.ScoreComputer_Phase2;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.BlueAction;
import org.mitre.icarus.cps.feature_vector.phase_2.BlueLocation;

/**
 * A Blue action selection model component implementation that selects 
 * the normative (Bayesian) blue actions at each location.
 * 
 * @author CBONACETO
 */
public class BayesianBlueActionSelectionComponent implements IBlueActionSelectionComponent {
    
    private static final ScoreComputer_Phase2 sc = new ScoreComputer_Phase2();
    
    /** The random number generator to use */
    private Random rand;
    
    public BayesianBlueActionSelectionComponent() {
        this(new Random(1));
    }
    
    public BayesianBlueActionSelectionComponent(Random rand) {
        this.rand = rand == null ? new Random(1) : rand;
    }
    
    @Override
    public List<BlueAction> selectBlueActions(List<BlueLocation> locations,
            List<Double> attackProbabilities) {
        //Returns the normative (Bayesian) blue actions at each location.
        if (locations != null && !locations.isEmpty()
                && attackProbabilities != null && attackProbabilities.size() == locations.size()) {            
            Iterator<BlueLocation> locationIter = locations.iterator();
            while (locationIter.hasNext()) {
                rand.nextDouble(); //Get the next random to maintain the same sequence of random numbers when using this model component 
                locationIter.next();
            }            
            return sc.computeNormativeBlueActions(locations, attackProbabilities).getBlueActions();
        }
        return null;
    }
}
