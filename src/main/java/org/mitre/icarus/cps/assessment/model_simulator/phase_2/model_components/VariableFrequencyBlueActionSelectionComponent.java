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
import java.util.Random;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.BlueAction;
import org.mitre.icarus.cps.feature_vector.phase_2.BlueLocation;

/**
 * A basic Blue action selection model component implementation that selects 
 * Divert with frequency P(Attack) at the location.
 * 
 * @author CBONACETO
 */
public class VariableFrequencyBlueActionSelectionComponent implements IBlueActionSelectionComponent {
    
    /** The random number generator to use */
    private Random rand;
    
    public VariableFrequencyBlueActionSelectionComponent() {
        this(new Random(1));
    }
    
    public VariableFrequencyBlueActionSelectionComponent(Random rand) {
        this.rand = rand == null ? new Random(1) : rand;
    }
    
    @Override
    public List<BlueAction> selectBlueActions(List<BlueLocation> locations,
            List<Double> attackProbabilities) {
        //Choose divert P(Attack) % of the time
        if (locations != null && !locations.isEmpty()
                && attackProbabilities != null && attackProbabilities.size() == locations.size()) {
            List<BlueAction> blueActions = new ArrayList<BlueAction>(locations.size());
            Iterator<BlueLocation> locationIter = locations.iterator();
            Iterator<Double> attackProbIter = attackProbabilities.iterator();
            int i = 0;
            while (locationIter.hasNext()) {
                Double attackProb = attackProbIter.next();                
                BlueAction.BlueActionType action = attackProb >= rand.nextDouble() ? 
                        BlueAction.BlueActionType.Divert : BlueAction.BlueActionType.Do_Not_Divert;
                blueActions.add(new BlueAction(locationIter.next().getId(), i, action));
                i++;
            }
            return blueActions;
        }
        return null;
    }
}
