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
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.BlueAction;
import org.mitre.icarus.cps.feature_vector.phase_2.BlueLocation;

/**
 * Interface for model component implementations that select a Blue action at each
 * location.
 * 
 * @author CBONACETO
 */
public interface IBlueActionSelectionComponent {

    /**
     * Selects the Blue actions at each location.
     * 
     * @param locations
     * @param attackProbabilities
     * @return
     */
    List<BlueAction> selectBlueActions(List<BlueLocation> locations,
            List<Double> attackProbabilities);
}
