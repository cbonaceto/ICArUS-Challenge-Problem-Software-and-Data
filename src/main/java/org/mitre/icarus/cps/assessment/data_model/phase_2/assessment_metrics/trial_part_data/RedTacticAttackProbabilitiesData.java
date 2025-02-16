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
package org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.trial_part_data;

import java.util.Iterator;
import java.util.List;

/**
 * 
 * 
 * @author CBONACETO
 *
 */
public class RedTacticAttackProbabilitiesData implements Iterable<Double> {

    /** The first trial on which the attack probabilities specified were used by Red */
    protected Integer trialNum;

    /** The Red attack probabilities */
    protected List<Double> attackProbabilities;    

    public RedTacticAttackProbabilitiesData() {
    }

    public RedTacticAttackProbabilitiesData(List<Double> attackProbabilities) {
        this.attackProbabilities = attackProbabilities;
    }

    public RedTacticAttackProbabilitiesData(List<Double> attackProbabilities, Integer trialNum) {
        this.attackProbabilities = attackProbabilities;
        this.trialNum = trialNum;
    }

    public Integer getTrialNum() {
        return trialNum;
    }

    public void setTrialNum(Integer trialNum) {
        this.trialNum = trialNum;
    }

    public List<Double> getAttackProbabilities() {
        return attackProbabilities;
    }

    public void setAttackProbabilities(List<Double> attackProbabilities) {
        this.attackProbabilities = attackProbabilities;
    }  

    public boolean isEmpty() {
        return attackProbabilities != null ? attackProbabilities.isEmpty() : true;
    }

    public int size() {
        return attackProbabilities != null ? attackProbabilities.size() : 0;
    }

    @Override
    public Iterator<Double> iterator() {
        return attackProbabilities != null ? attackProbabilities.iterator() : null;
    }
}