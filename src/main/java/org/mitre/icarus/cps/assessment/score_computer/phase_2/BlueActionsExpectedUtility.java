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
package org.mitre.icarus.cps.assessment.score_computer.phase_2;

import java.util.List;

import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.BlueAction;

/**
 * Contains a permutation of Blue actions at one or more locations and the expected utility
 * of choosing those Blue actions.
 * 
 * @author CBONACETO
 *
 */
public class BlueActionsExpectedUtility {

    /**
     * The permutation of blue actions at each location
     */
    protected List<BlueAction> blueActions;

    /**
     * The expected utility of choosing the actions
     */
    protected Double expectedUtility;

    public BlueActionsExpectedUtility() {
    }

    public BlueActionsExpectedUtility(List<BlueAction> blueActions) {
        this.blueActions = blueActions;
    }
    
    public BlueActionsExpectedUtility(List<BlueAction> blueActions,
            Double expectedUtility) {
        this.blueActions = blueActions;
        this.expectedUtility = expectedUtility;
    }

    /**
     * Get the permutation of blue actions at each location.
     * 
     * @return the permutation of blue actions at each location
     */
    public List<BlueAction> getBlueActions() {
        return blueActions;
    }

    /**
     * Set permutation of blue actions at each location
     * 
     * @param blueActions The permutation of blue actions at each location
     */
    public void setBlueActions(List<BlueAction> blueActions) {
        this.blueActions = blueActions;
    }

    /**
     * Get the expected utility of choosing the actions
     * 
     * @return the expected utility of choosing the actions
     */
    public Double getExpectedUtility() {
        return expectedUtility;
    }

    /**
     * Set the expected utility of choosing the actions
     * 
     * @param expectedUtility the expected utility of choosing the actions
     */
    public void setExpectedUtility(Double expectedUtility) {
        this.expectedUtility = expectedUtility;
    }
}
