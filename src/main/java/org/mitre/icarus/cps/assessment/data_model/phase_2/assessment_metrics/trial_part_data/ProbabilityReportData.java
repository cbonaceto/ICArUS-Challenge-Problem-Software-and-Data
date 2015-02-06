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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.mitre.icarus.cps.app.util.ProbabilityUtils;

import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.NameValuePair;
import org.mitre.icarus.cps.assessment.score_computer.phase_2.ScoreComputer_Phase2;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.Probability;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.ProbabilityReportProbe;

/**
 * Contains response data for a probability report probe.
 * 
 * @author CBONACETO
 * @param <T>
 */
public class ProbabilityReportData<T extends ProbabilityReportProbe<?>> extends TrialPartData<T> {

    private static final long serialVersionUID = -2724540315336394398L;

    /** The ID of the probability report probe */
    protected String probabilitiesId;
    
    /** The name of the probability report probe */
    protected String probabilitiesName;

    /** The probabilities (normalized, in decimal format) */
    protected List<Double> probabilities;

    /** Standard deviations of the probabilities */
    protected List<Double> probabilities_std;
    
    /** Negentropy of the probabilities  */
    //protected double Np;
    
    /** The cumulative Bayesian normative probabilities (normalized, in decimal format)  */
    protected List<Double> probabilities_normative;
    
    /** Standard deviations of the cumulative Bayesian normative probabilities */
    protected List<Double> probabilities_normative_std;
    
    /** The incremental Bayesian normative probabilities (normalized, in decimal format)  */
     protected List<Double> probabilities_normative_incremental;
     
    /** Standard deviations of the incremental Bayesian normative probabilities */
    protected List<Double> probabilities_normative_incremental_std;
    
    /** Negentropy of the normative probabilities */
    //protected Double Nq;
    
    public ProbabilityReportData() {
    }

    public ProbabilityReportData(T trialPartProbe) {
        super(trialPartProbe);
    }

    public String getProbabilitiesId() {
        return probabilitiesId;
    }

    public void setProbabilitiesId(String probabilitiesId) {
        this.probabilitiesId = probabilitiesId;
    }

    public String getProbabilitiesName() {
        return probabilitiesName;
    }

    public void setProbabilitiesName(String probabilitiesName) {
        this.probabilitiesName = probabilitiesName;
    }    

    public List<Double> getProbabilities() {
        return probabilities;
    }

    public void setProbabilities(List<Double> probabilities) {
        this.probabilities = probabilities;
    }

    public List<Double> getProbabilities_std() {
        return probabilities_std;
    }

    public void setProbabilities_std(List<Double> probabilities_std) {
        this.probabilities_std = probabilities_std;
    }    

    public List<Double> getProbabilities_normative() {
        return probabilities_normative;
    }

    public void setProbabilities_normative(List<Double> probabilities_normative) {
        this.probabilities_normative = probabilities_normative;
    }

    public List<Double> getProbabilities_normative_std() {
        return probabilities_normative_std;
    }

    public void setProbabilities_normative_std(List<Double> probabilities_normative_std) {
        this.probabilities_normative_std = probabilities_normative_std;
    }    

    public List<Double> getProbabilities_normative_incremental() {
        return probabilities_normative_incremental;
    }

    public void setProbabilities_normative_incremental(List<Double> probabilities_normative_incremental) {
        this.probabilities_normative_incremental = probabilities_normative_incremental;
    }

    public List<Double> getProbabilities_normative_incremental_std() {
        return probabilities_normative_incremental_std;
    }

    public void setProbabilities_normative_incremental_std(List<Double> probabilities_normative_incremental_std) {
        this.probabilities_normative_incremental_std = probabilities_normative_incremental_std;
    }    

    @Override
    protected void initializeTrialPartData(T trialPartProbe) {
        if (trialPartProbe != null) {
            probabilitiesId = trialPartProbe.getId() != null ? trialPartProbe.getId()
                    : trialPartProbe.getType() != null ? trialPartProbe.getType().getId() : null;
            probabilitiesName = trialPartProbe.getName();
            if (trialPartProbe.getProbabilities() != null
                    && !trialPartProbe.getProbabilities().isEmpty()) {
                probabilities = new ArrayList<Double>();
                probabilities_normative = new ArrayList<Double>();
                probabilities_normative_incremental = new ArrayList<Double>();
                for (Probability probability : trialPartProbe.getProbabilities()) {
                    probabilities.add(probability.getProbability() / 100.d);
                    probabilities_normative.add(probability.getNormativeProbability()); 
                    probabilities_normative_incremental.add(probability.getNormativeIncrementalProbability());
                }
                //Normalize probabilities
                ProbabilityUtils.normalizeDecimalProbabilities(probabilities, probabilities,
                        ScoreComputer_Phase2.EPSILON_PHASE2);
                ProbabilityUtils.normalizeDecimalProbabilities(probabilities_normative, 
                        probabilities_normative, ScoreComputer_Phase2.EPSILON_PHASE2);   
                ProbabilityUtils.normalizeDecimalProbabilities(probabilities_normative_incremental, 
                        probabilities_normative_incremental, ScoreComputer_Phase2.EPSILON_PHASE2);   
            }
        }
    }

    @Override
    protected List<NameValuePair> getAdditionalDataValuesAsString(
            List<NameValuePair> dataValues) {
        //String id = trialPartType != null ? trialPartType.getId() : "probs";
        if (dataValues == null) {
            dataValues = new LinkedList<NameValuePair>();
        }
        if (probabilities != null && !probabilities.isEmpty()) {
            int index = 1;
            for (Double probability : probabilities) {
                dataValues.add(new NameValuePair(
                        createProbName(index, false, false),
                        probability != null ? probability.toString() : null));
                index++;
            }
        }
        if (probabilities_normative != null && !probabilities_normative.isEmpty()) {
            int index = 1;
            for (Double probability : probabilities_normative) {
                dataValues.add(new NameValuePair(
                        createProbName(index, true, false),
                        probability != null ? probability.toString() : null));
                index++;
            }
        }
        if (probabilities_normative_incremental != null && !probabilities_normative_incremental.isEmpty()) {
            int index = 1;
            for (Double probability : probabilities_normative_incremental) {                
                dataValues.add(new NameValuePair(
                        createProbName(index, false, true),
                        probability != null ? probability.toString() : null));
                index++;
            }
        }
        return dataValues;
    }

    protected String createProbName(int index, boolean normativeProb, 
            boolean normativeIncrementalProb) {
        //StringBuilder sb = new StringBuilder(id);
        StringBuilder sb = new StringBuilder("probs_");
        if (probabilitiesId != null) {
            sb.append(probabilitiesId);
            sb.append("_");
        }
        if (normativeProb) {
            sb.append("normative_");
        } else if(normativeIncrementalProb) {
            sb.append("normative_inc_");
        }
        sb.append(Integer.toString(index));
        return sb.toString();
    }
}