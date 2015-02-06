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
import org.mitre.icarus.cps.app.util.ProbabilityUtils;
import org.mitre.icarus.cps.exam.phase_2.testing.IcarusTestTrial_Phase2;
import org.mitre.icarus.cps.exam.phase_2.testing.Mission;
import org.mitre.icarus.cps.exam.phase_2.testing.bluebook.RedTacticType;

/**
 * An implementation of a Red tactics probability computer model component that 
 * always sets the probabilities to the uniform probabilities. Never creates a batch plot.
 * 
 * @author CBONACETO
 */
public class RandomRedTacticsProbabilityComponent implements IRedTacticsProbabilityComponent {

    @Override
    public RedTacticProbabilitiesAndBatchPlot computeRedTacticProbabilities(Mission<?> mission,
            List<RedTacticType> redTactics, IcarusTestTrial_Phase2 currentTrial,
            int numTrials, int numAttacks, boolean canCreateBatchPlot,
            List<Integer> previousTrialsToReview) {
        ArrayList<Double> redTacticProbs = ProbabilityUtils.createProbabilities_Double(redTactics.size(), 1.d / redTactics.size());
        //System.out.println(redTacticProbs);
        return new RedTacticProbabilitiesAndBatchPlot(redTacticProbs);
    }
}