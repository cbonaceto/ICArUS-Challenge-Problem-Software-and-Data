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

/**
 *
 * @author CBONACETO
 */
public class RedTacticProbabilitiesAndBatchPlot {
    
    /** The Red tactic probabilities */
    private List<Double> redTacticProbabilities;
    
    /** The number of previous trials reviewed if a batch plot was created */
    private Integer numPreviousTrialsReviewed;
    
    public RedTacticProbabilitiesAndBatchPlot() {}
    
    public RedTacticProbabilitiesAndBatchPlot(List<Double> redTacticProbabilities) {
        this(redTacticProbabilities, null);
    }
    
    public RedTacticProbabilitiesAndBatchPlot(List<Double> redTacticProbabilities,
            Integer numPreviousTrialsReviewed) {
        this.redTacticProbabilities = redTacticProbabilities;
        this.numPreviousTrialsReviewed = numPreviousTrialsReviewed;
    }

    public List<Double> getRedTacticProbabilities() {
        return redTacticProbabilities;
    }

    public void setRedTacticProbabilities(List<Double> redTacticProbabilities) {
        this.redTacticProbabilities = redTacticProbabilities;
    }

    public Integer getNumPreviousTrialsReviewed() {
        return numPreviousTrialsReviewed;
    }

    public void setNumPreviousTrialsReviewed(Integer numPreviousTrialsReviewed) {
        this.numPreviousTrialsReviewed = numPreviousTrialsReviewed;
    }
}
