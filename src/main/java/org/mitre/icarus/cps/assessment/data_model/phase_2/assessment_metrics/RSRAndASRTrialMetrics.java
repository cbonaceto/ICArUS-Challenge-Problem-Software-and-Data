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
package org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics;

import java.util.List;

import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.CPAMetric;

/**
 * Contains RSR and ASR metrics for a trial in a Phase 2 mission.
 *
 * @author CBONACETO
 *
 */
public class RSRAndASRTrialMetrics {
    
    /** 
     * The names of each stage that RSR and ASR were computed for     * 
     */
    protected List<String> stageNames;
    
     /**
     * ASR (computed using RMSE only) for each stage.
     */
    protected List<CPAMetric> ASR;

    /**
     * K (Kullback-Liebler divergence) between the human distribution P and
     * model distribution M for each stage
     */
    //protected List<Double> Kpm;

    /**
     * K (Kullback-Liebler divergence) between the human distribution P and a
     * random (maximum entropy) distribution R for each stage
     */
    //protected List<Double> Kpr;

    /**
     * K (Kullback-Liebler divergence) between the human distribution (P) and
     * the normative (Bayesian) distribution (Q) for each stage
     */
    //protected List<Double> Kpq;

    /**
     * Similarity between the human distribution (P) and the model distribution
     * (M) for each stage
     */
    protected List<Double> Spm;

    /**
     * Similarity between the human distribution (P) and a random (maximum
     * entropy) distribution (R) for each stage
     */
    protected List<Double> Spr;

    /**
     * Similarity between the human distribution (P) and the normative
     * (Bayesian) distribution (Q) for each stage
     */
    protected List<Double> Spq;

    /**
     * Root mean squared error between the human distribution and model
     * distribution for each stage
     */
    protected List<Double> RMSE;

    /**
     * Root mean squared error between the human distribution and normative
     * (Bayesian) distribution for each stage
     */
    protected List<Double> RMSE_normative;

    /**
     * RSR computed using Spm and Spr for each stage
     */
    protected List<CPAMetric> RSR;

    /**
     * RSR computed using Spm and Spq for each stage
     */
    protected List<CPAMetric> RSR_Bayesian;

    /**
     * RSR computed using alternative method 1 (e.g., using RMSE) for each stage
     */
    protected List<CPAMetric> RSR_alt_1; //E.g., RSR(RMSE)

    /**
     * RSR computed using alternative method 2 (e.g., using RMSE_normative) for
     * each stage
     */
    protected List<CPAMetric> RSR_alt_2; //E.g., RSR(RMSE_normative)   

    public List<String> getStageNames() {
        return stageNames;
    }

    public void setStageNames(List<String> stageNames) {
        this.stageNames = stageNames;
    }
    
    public List<CPAMetric> getASR() {
        return ASR;
    }

    public void setASR(List<CPAMetric> ASR) {
        this.ASR = ASR;
    }

    public List<Double> getSpm() {
        return Spm;
    }

    public void setSpm(List<Double> spm) {
        Spm = spm;
    }

    public List<Double> getSpr() {
        return Spr;
    }

    public void setSpr(List<Double> spr) {
        Spr = spr;
    }

    public List<Double> getSpq() {
        return Spq;
    }

    public void setSpq(List<Double> spq) {
        Spq = spq;
    }

    public List<Double> getRMSE() {
        return RMSE;
    }

    public void setRMSE(List<Double> rMSE) {
        RMSE = rMSE;
    }

    public List<Double> getRMSE_normative() {
        return RMSE_normative;
    }

    public void setRMSE_normative(List<Double> rMSE_normative) {
        RMSE_normative = rMSE_normative;
    }

    public List<CPAMetric> getRSR() {
        return RSR;
    }

    public void setRSR(List<CPAMetric> rSR) {
        RSR = rSR;
    }

    public List<CPAMetric> getRSR_Bayesian() {
        return RSR_Bayesian;
    }

    public void setRSR_Bayesian(List<CPAMetric> RSR_Bayesian) {
        this.RSR_Bayesian = RSR_Bayesian;
    }

    public List<CPAMetric> getRSR_alt_1() {
        return RSR_alt_1;
    }

    public void setRSR_alt_1(List<CPAMetric> rSR_alt_1) {
        RSR_alt_1 = rSR_alt_1;
    }

    public List<CPAMetric> getRSR_alt_2() {
        return RSR_alt_2;
    }

    public void setRSR_alt_2(List<CPAMetric> rSR_alt_2) {
        RSR_alt_2 = rSR_alt_2;
    }    
}