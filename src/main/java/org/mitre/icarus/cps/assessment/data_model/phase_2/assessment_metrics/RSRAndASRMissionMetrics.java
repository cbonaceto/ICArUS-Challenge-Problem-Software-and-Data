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

/**
 * @author CBONACETO
 *
 */
public class RSRAndASRMissionMetrics {
    
     /** 
     * The names of each stage that RSR and ASR were computed for     * 
     */
    protected List<String> stageNames;
    
    protected Double ASR_avg;
    protected List<Double> ASR_stage_avg;
    protected Double ASR_std;

    /*protected Double Kpm_avg;
    protected List<Double> Kpm_stage_avg;
    protected Double Kpm_std;

    protected Double Kpr_avg;
    protected List<Double> Kpr_stage_avg;
    protected Double Kpr_std;

    protected List<Double> Kpq;
    protected List<Double> Kpq_stage_avg;
    protected Double Kpq_std;*/

    protected Double Spm_avg;
    protected List<Double> Spm_stage_avg;
    protected Double Spm_std;

    protected Double Spr_avg;
    protected List<Double> Spr_stage_avg;
    protected Double Spr_std;

    protected Double Spq_avg;
    protected List<Double> Spq_stage_avg;
    protected Double Spq_std;

    protected Double RSR_avg;
    protected List<Double> RSR_stage_avg;
    protected Double RSR_std;

    protected Double RSR_Bayesian_avg;
    protected List<Double> RSR_Bayesian_stage_avg;
    protected Double RSR_Bayesian_std;

    protected Double RSR_Spm_Spr_avg;
    protected List<Double> RSR_Spm_Spr_stage_avg;
    protected Double RSR_Spm_Spr_std;

    protected Double RSR_Spm_Spq_avg;
    protected List<Double> RSR_Spm_Spq_stage_avg;
    protected Double RSR_Spm_Spq_std;

    protected Double RSR_alt_1_avg;
    protected List<Double> RSR_alt_1_stage_avg;
    protected Double RSR_alt_1_std;

    protected Double RSR_alt_2_avg;
    protected List<Double> RSR_alt_2_stage_avg;
    protected Double RSR_alt_2_std;

    public List<String> getStageNames() {
        return stageNames;
    }

    public void setStageNames(List<String> stageNames) {
        this.stageNames = stageNames;
    }    

    public Double getASR_avg() {
        return ASR_avg;
    }

    public void setASR_avg(Double ASR_avg) {
        this.ASR_avg = ASR_avg;
    }

    public List<Double> getASR_stage_avg() {
        return ASR_stage_avg;
    }

    public void setASR_stage_avg(List<Double> ASR_stage_avg) {
        this.ASR_stage_avg = ASR_stage_avg;
    }

    public Double getASR_std() {
        return ASR_std;
    }

    public void setASR_std(Double ASR_std) {
        this.ASR_std = ASR_std;
    }

    public Double getSpm_avg() {
        return Spm_avg;
    }

    public void setSpm_avg(Double Spm_avg) {
        this.Spm_avg = Spm_avg;
    }

    public List<Double> getSpm_stage_avg() {
        return Spm_stage_avg;
    }

    public void setSpm_stage_avg(List<Double> Spm_stage_avg) {
        this.Spm_stage_avg = Spm_stage_avg;
    }

    public Double getSpm_std() {
        return Spm_std;
    }

    public void setSpm_std(Double Spm_std) {
        this.Spm_std = Spm_std;
    }

    public Double getSpr_avg() {
        return Spr_avg;
    }

    public void setSpr_avg(Double Spr_avg) {
        this.Spr_avg = Spr_avg;
    }

    public List<Double> getSpr_stage_avg() {
        return Spr_stage_avg;
    }

    public void setSpr_stage_avg(List<Double> Spr_stage_avg) {
        this.Spr_stage_avg = Spr_stage_avg;
    }

    public Double getSpr_std() {
        return Spr_std;
    }

    public void setSpr_std(Double Spr_std) {
        this.Spr_std = Spr_std;
    }

    public Double getSpq_avg() {
        return Spq_avg;
    }

    public void setSpq_avg(Double Spq_avg) {
        this.Spq_avg = Spq_avg;
    }

    public List<Double> getSpq_stage_avg() {
        return Spq_stage_avg;
    }

    public void setSpq_stage_avg(List<Double> Spq_stage_avg) {
        this.Spq_stage_avg = Spq_stage_avg;
    }

    public Double getSpq_std() {
        return Spq_std;
    }

    public void setSpq_std(Double Spq_std) {
        this.Spq_std = Spq_std;
    }

    public Double getRSR_avg() {
        return RSR_avg;
    }

    public void setRSR_avg(Double RSR_avg) {
        this.RSR_avg = RSR_avg;
    }

    public List<Double> getRSR_stage_avg() {
        return RSR_stage_avg;
    }

    public void setRSR_stage_avg(List<Double> RSR_stage_avg) {
        this.RSR_stage_avg = RSR_stage_avg;
    }

    public Double getRSR_std() {
        return RSR_std;
    }

    public void setRSR_std(Double RSR_std) {
        this.RSR_std = RSR_std;
    }

    public Double getRSR_Bayesian_avg() {
        return RSR_Bayesian_avg;
    }

    public void setRSR_Bayesian_avg(Double RSR_Bayesian_avg) {
        this.RSR_Bayesian_avg = RSR_Bayesian_avg;
    }

    public List<Double> getRSR_Bayesian_stage_avg() {
        return RSR_Bayesian_stage_avg;
    }

    public void setRSR_Bayesian_stage_avg(List<Double> RSR_Bayesian_stage_avg) {
        this.RSR_Bayesian_stage_avg = RSR_Bayesian_stage_avg;
    }

    public Double getRSR_Bayesian_std() {
        return RSR_Bayesian_std;
    }

    public void setRSR_Bayesian_std(Double RSR_Bayesian_std) {
        this.RSR_Bayesian_std = RSR_Bayesian_std;
    }

    public Double getRSR_Spm_Spr_avg() {
        return RSR_Spm_Spr_avg;
    }

    public void setRSR_Spm_Spr_avg(Double RSR_Spm_Spr_avg) {
        this.RSR_Spm_Spr_avg = RSR_Spm_Spr_avg;
    }

    public List<Double> getRSR_Spm_Spr_stage_avg() {
        return RSR_Spm_Spr_stage_avg;
    }

    public void setRSR_Spm_Spr_stage_avg(List<Double> RSR_Spm_Spr_stage_avg) {
        this.RSR_Spm_Spr_stage_avg = RSR_Spm_Spr_stage_avg;
    }

    public Double getRSR_Spm_Spr_std() {
        return RSR_Spm_Spr_std;
    }

    public void setRSR_Spm_Spr_std(Double RSR_Spm_Spr_std) {
        this.RSR_Spm_Spr_std = RSR_Spm_Spr_std;
    }

    public Double getRSR_Spm_Spq_avg() {
        return RSR_Spm_Spq_avg;
    }

    public void setRSR_Spm_Spq_avg(Double RSR_Spm_Spq_avg) {
        this.RSR_Spm_Spq_avg = RSR_Spm_Spq_avg;
    }

    public List<Double> getRSR_Spm_Spq_stage_avg() {
        return RSR_Spm_Spq_stage_avg;
    }

    public void setRSR_Spm_Spq_stage_avg(List<Double> RSR_Spm_Spq_stage_avg) {
        this.RSR_Spm_Spq_stage_avg = RSR_Spm_Spq_stage_avg;
    }

    public Double getRSR_Spm_Spq_std() {
        return RSR_Spm_Spq_std;
    }

    public void setRSR_Spm_Spq_std(Double RSR_Spm_Spq_std) {
        this.RSR_Spm_Spq_std = RSR_Spm_Spq_std;
    }

    public Double getRSR_alt_1_avg() {
        return RSR_alt_1_avg;
    }

    public void setRSR_alt_1_avg(Double RSR_alt_1_avg) {
        this.RSR_alt_1_avg = RSR_alt_1_avg;
    }

    public List<Double> getRSR_alt_1_stage_avg() {
        return RSR_alt_1_stage_avg;
    }

    public void setRSR_alt_1_stage_avg(List<Double> RSR_alt_1_stage_avg) {
        this.RSR_alt_1_stage_avg = RSR_alt_1_stage_avg;
    }

    public Double getRSR_alt_1_std() {
        return RSR_alt_1_std;
    }

    public void setRSR_alt_1_std(Double RSR_alt_1_std) {
        this.RSR_alt_1_std = RSR_alt_1_std;
    }

    public Double getRSR_alt_2_avg() {
        return RSR_alt_2_avg;
    }

    public void setRSR_alt_2_avg(Double RSR_alt_2_avg) {
        this.RSR_alt_2_avg = RSR_alt_2_avg;
    }

    public List<Double> getRSR_alt_2_stage_avg() {
        return RSR_alt_2_stage_avg;
    }

    public void setRSR_alt_2_stage_avg(List<Double> RSR_alt_2_stage_avg) {
        this.RSR_alt_2_stage_avg = RSR_alt_2_stage_avg;
    }

    public Double getRSR_alt_2_std() {
        return RSR_alt_2_std;
    }

    public void setRSR_alt_2_std(Double RSR_alt_2_std) {
        this.RSR_alt_2_std = RSR_alt_2_std;
    }
}