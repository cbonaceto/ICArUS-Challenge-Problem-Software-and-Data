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
package org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.AbstractMetricsInfo;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.MetricInfo;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.TrialIdentifier;

/**
 * Contains descriptions of each Phase 1 assessment metric that will be
 * calculated.
 *
 * @author CBONACETO
 *
 */
@Entity
public class MetricsInfo extends AbstractMetricsInfo implements Serializable {

    private static final long serialVersionUID = -5230008230535855532L;

    /**
     * CFA bias metrics
     */
    protected MetricInfo RR_info;

    protected MetricInfo AI_info;

    protected MetricInfo CW_info;

    protected MetricInfo PM_F_info;

    protected MetricInfo PM_RMS_info;

    protected MetricInfo PM_info;

    protected MetricInfo CS_info;

    protected Double C_threshold;

    /**
     * CPA RSR (Relative Success Rate) metrics
     */
    protected MetricInfo RSR_info;

    protected MetricInfo RSR_Bayesian_info;

    protected MetricInfo RSR_Spm_Spr_avg_info;

    protected MetricInfo RSR_Spm_Spq_avg_info;

    protected MetricInfo RSR_alt_1_info; //RSR(RMSE)

    protected MetricInfo RSR_alt_2_info; //RSR(RMSE_Bayesian)
    /**
     * ***
     */

    /**
     * CPA ASR (Absolute Success Rate) metric
     */
    protected MetricInfo ASR_info;

    /**
     * CPA RMR metric
     */
    protected MetricInfo RMR_info;

    /**
     * The "official" version of RSR that will be used for scoring a model if
     * RSR is used (and not ASR)
     */
    protected MetricType evaluated_RSR_Type;

    /**
     * Whether ASR is used instead of RSR as the official CPA metric for scoring
     * a model
     */
    protected Boolean use_asr_as_evaluation_metric;

    /**
     * RSR, ASR, and RMR task weights
     */
    protected List<Double> rsr_asr_task_weights;

    /**
     * Whether to compute RSR, ASR, and RMR averages using the task weights
     */
    protected Boolean use_rsr_asr_task_weights;

    /**
     * Weight of RSR or ASR when computing the overall CPA score
     */
    protected Double rsr_asr_weight;

    /**
     * Weight of RMR when computing the overall CPA score
     */
    protected Double rmr_weight;

    /**
     * The CFA pass threshold
     */
    protected Double cfa_pass_threshold;

    /**
     * The CPA pass threshold
     */
    protected Double cpa_pass_threshold;

    /**
     * Create a MetricsInfo instance with the current default settings.
     *
     * @return
     */
    public static MetricsInfo createDefaultMetricsInfo() {
        MetricsInfo mi = new MetricsInfo();
        int id = 0;
        double cfaMetricsThreshold = 50d;
        double cpaMetricsThreshold = 50d;

        mi.RR_info = new MetricInfo("Representativeness", MetricType.RR.ordinal());
        mi.RR_info.setDescription("Bias Magnitude = Nq - Np, Bias Exhibited when Np less than Nq");
        mi.RR_info.setTasks(new TreeSet<Integer>(Arrays.asList(1, 2, 3)));
        mi.RR_info.setNum_trials_stages(20);
        mi.RR_info.setCalculated(true);
        mi.RR_info.setAssessed(true);
        mi.RR_info.setOverall_pass_threshold(cfaMetricsThreshold);

        mi.AI_info = new MetricInfo("Anchoring & Adjustment", MetricType.AI.ordinal());
        mi.AI_info.setDescription("Bias Magnitude = |delta Nq| - |delta Np|, Bias Exhibited When |delta Np| less than |delta Nq| and sign(delta Np) = sign(delta Nq)");
        mi.AI_info.setTasks(new TreeSet<Integer>(Arrays.asList(5)));
        mi.AI_info.setExcludedTrials(new ArrayList<TrialIdentifier>(Arrays.asList(
                new TrialIdentifier(5, 1, new TreeSet<Integer>(Arrays.asList(2, 3))),
                new TrialIdentifier(5, 2, new TreeSet<Integer>(Arrays.asList(3))),
                new TrialIdentifier(5, 3, new TreeSet<Integer>(Arrays.asList(1))),
                new TrialIdentifier(5, 4, new TreeSet<Integer>(Arrays.asList(4))),
                new TrialIdentifier(5, 5, new TreeSet<Integer>(Arrays.asList(1, 3))),
                new TrialIdentifier(5, 6, new TreeSet<Integer>(Arrays.asList(1))),
                new TrialIdentifier(5, 9, new TreeSet<Integer>(Arrays.asList(1))),
                new TrialIdentifier(5, 10, new TreeSet<Integer>(Arrays.asList(1, 4)))
        )));
        mi.AI_info.setNum_trials_stages(29);
        mi.AI_info.setCalculated(true);
        mi.AI_info.setAssessed(true);
        mi.AI_info.setOverall_pass_threshold(cfaMetricsThreshold);

        //We don't calculate CW since AI is the dominant bias
        mi.CW_info = new MetricInfo("Confirmation Bias (Weighing Evidence)", MetricType.CW.ordinal());
        mi.CW_info.setDescription("Bias Magnitude = |delta Np| - |delta Nq|, Bias Exhibited When |delta Np| greater than |delta Nq| and sign(delta Np) = sign(delta Nq)");
        mi.CW_info.setCalculated(false);
        mi.CW_info.setAssessed(false);
        mi.CW_info.setOverall_pass_threshold(cfaMetricsThreshold);

        //We're no longer calculating PM_F
        mi.PM_F_info = new MetricInfo("Probability Matching", MetricType.PM_F_ID);
        mi.PM_F_info.setDescription("Probability Matching Tasks 1-3");
        mi.PM_F_info.setTasks(new TreeSet<Integer>(Arrays.asList(1, 2, 3)));
        mi.PM_F_info.setNum_trials_stages(20);
        mi.PM_F_info.setCalculated(false);
        mi.PM_F_info.setAssessed(false);
        mi.PM_F_info.setOverall_pass_threshold(cfaMetricsThreshold);

        mi.PM_RMS_info = new MetricInfo("Probability Matching", MetricType.PM_RMS_ID);
        mi.PM_RMS_info.setDescription("Bias Magnitude = RMS(F-I) - RMS(F-P), Bias Exhibited when RMS(F-I) greater than RMS(F-P)");
        mi.PM_RMS_info.setTasks(new TreeSet<Integer>(Arrays.asList(4, 5, 6)));
        mi.PM_RMS_info.setNum_trials_stages(30);
        mi.PM_RMS_info.setCalculated(true);
        mi.PM_RMS_info.setAssessed(true);
        mi.PM_RMS_info.setOverall_pass_threshold(cfaMetricsThreshold);

        mi.PM_info = new MetricInfo("Probability Matching", MetricType.PM_ID);
        mi.PM_info.setDescription("Probability Matching");
        mi.PM_info.setTasks(new TreeSet<Integer>(Arrays.asList(4, 5, 6)));
        mi.PM_info.setNum_trials_stages(30);
        mi.PM_info.setCalculated(true);
        mi.PM_info.setAssessed(true);
        mi.PM_info.setOverall_pass_threshold(cfaMetricsThreshold);

        mi.CS_info = new MetricInfo("Confirmation Bias (Seeking)", MetricType.CS_ID);
        mi.CS_info.setDescription("Bias Magnitude = C - 50%, Bias Exhibited When C greater than 50%");
        mi.CS_info.setTasks(new TreeSet<Integer>(Arrays.asList(6)));
        mi.CS_info.setNum_trials_stages(10);
        mi.CS_info.setCalculated(true);
        mi.CS_info.setAssessed(true);
        mi.CS_info.setTask_pass_threshold(cfaMetricsThreshold);
        mi.CS_info.setOverall_pass_threshold(cfaMetricsThreshold);
        mi.C_threshold = 50d;

        mi.RSR_info = createDefaultRSRInfo(++id, "Relative Success Rate (RSR)", MetricType.RSR_Standard,
                "Relative Success Rate using KLD and uniform null model", cpaMetricsThreshold);
        mi.RSR_Bayesian_info = createDefaultRSRInfo(++id, "RSR(Bayesian Null Model)", MetricType.RSR_Bayesian,
                "Relative Success Rate using KLD and Bayesian null model", cpaMetricsThreshold);
        mi.RSR_Spm_Spr_avg_info = createDefaultRSRInfo(++id, "RSR(Spm-Spr-avg)", MetricType.RSR_Spm_Spr_avg,
                "Relative Success Rate using average Spm and Spr", cpaMetricsThreshold);
        mi.RSR_Spm_Spq_avg_info = createDefaultRSRInfo(++id, "RSR(Spm-Spq-avg)", MetricType.RSR_Spm_Spq_avg,
                "Relative Success Rate using average Spm and Spq", cpaMetricsThreshold);
        mi.RSR_alt_1_info = createDefaultRSRInfo(++id, "RSR(Random Null Model)(RMSE)", MetricType.RSR_alt_1,
                "Relative Success Rate using RMSE and uniform null model", cpaMetricsThreshold);
        mi.RSR_alt_2_info = createDefaultRSRInfo(++id, "RSR(Bayesian Null Model)(RMSE)", MetricType.RSR_alt_2,
                "Relative Success Rate using RMSE and Bayesian null model", cpaMetricsThreshold);

        mi.ASR_info = createDefaultRSRInfo(++id, "Abolute Success Rate (ASR)", MetricType.ASR,
                "Absolute Success Rate", cpaMetricsThreshold);

        mi.RMR_info = new MetricInfo("Relative Match Rate (RMR)", MetricType.RMR_ID);
        mi.RMR_info.setDescription("Relative Match Rate in layer selections");
        mi.RMR_info.setTasks(new TreeSet<Integer>(Arrays.asList(6)));
        mi.RMR_info.setNum_trials_stages(10);
        mi.RMR_info.setCalculated(true);
        mi.RMR_info.setAssessed(true);
        mi.RMR_info.setTrial_pass_threshold(cpaMetricsThreshold);
        mi.RMR_info.setTask_pass_threshold(cpaMetricsThreshold);
        mi.RMR_info.setOverall_pass_threshold(cpaMetricsThreshold);

        mi.evaluated_RSR_Type = MetricType.RSR_Standard;
        mi.use_asr_as_evaluation_metric = false;
        mi.use_rsr_asr_task_weights = true;
        mi.rsr_asr_task_weights = new ArrayList<Double>(Arrays.asList(.05 / .85d, .1 / .85d, .15 / .85d, .15 / .85d, .4 / .85d));
        mi.rsr_asr_weight = 0.85;
        mi.rmr_weight = 0.15;

        mi.cfa_pass_threshold = 50d;
        mi.cpa_pass_threshold = 50d;

        return mi;
    }

    /**
     * @return
     */
    protected static MetricInfo createDefaultRSRInfo(int id, String name, MetricType rsrType, String description, Double passThreshold) {
        MetricInfo rsr_info = new MetricInfo(name, rsrType.ordinal());
        rsr_info.setDescription(description);
        rsr_info.setTasks(new TreeSet<Integer>(Arrays.asList(1, 2, 3, 4, 5)));
        rsr_info.setExcludedTrials(new ArrayList<TrialIdentifier>(Arrays.asList(
                new TrialIdentifier(1, 90, 0),
                new TrialIdentifier(5, 1, 0),
                new TrialIdentifier(5, 2, 0),
                new TrialIdentifier(5, 3, 0),
                new TrialIdentifier(5, 4, 0),
                new TrialIdentifier(5, 5, 0),
                new TrialIdentifier(5, 6, 0),
                new TrialIdentifier(5, 7, 0),
                new TrialIdentifier(5, 8, 0),
                new TrialIdentifier(5, 9, 0),
                new TrialIdentifier(5, 10, 0))));
        rsr_info.setNum_trials_stages(79);
        rsr_info.setCalculated(true);
        rsr_info.setAssessed(true);
        rsr_info.setTrial_pass_threshold(passThreshold);
        rsr_info.setTask_pass_threshold(passThreshold);
        rsr_info.setOverall_pass_threshold(passThreshold);
        return rsr_info;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    public MetricInfo getRR_info() {
        return RR_info;
    }

    public void setRR_info(MetricInfo rR_info) {
        RR_info = rR_info;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    public MetricInfo getAI_info() {
        return AI_info;
    }

    public void setAI_info(MetricInfo aI_info) {
        AI_info = aI_info;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    public MetricInfo getCW_info() {
        return CW_info;
    }

    public void setCW_info(MetricInfo cW_info) {
        CW_info = cW_info;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    public MetricInfo getPM_F_info() {
        return PM_F_info;
    }

    public void setPM_F_info(MetricInfo pM_F_info) {
        PM_F_info = pM_F_info;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    public MetricInfo getPM_RMS_info() {
        return PM_RMS_info;
    }

    public void setPM_RMS_info(MetricInfo pM_RMS_info) {
        PM_RMS_info = pM_RMS_info;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    public MetricInfo getPM_info() {
        return PM_info;
    }

    public void setPM_info(MetricInfo pM_info) {
        PM_info = pM_info;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    public MetricInfo getCS_info() {
        return CS_info;
    }

    public void setCS_info(MetricInfo cS_info) {
        CS_info = cS_info;
    }

    public Double getC_threshold() {
        return C_threshold;
    }

    public void setC_threshold(Double c_threshold) {
        C_threshold = c_threshold;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    public MetricInfo getRSR_info() {
        return RSR_info;
    }

    public void setRSR_info(MetricInfo rSR_info) {
        RSR_info = rSR_info;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    public MetricInfo getRSR_Bayesian_info() {
        return RSR_Bayesian_info;
    }

    public void setRSR_Bayesian_info(MetricInfo rSR_Bayesian_info) {
        RSR_Bayesian_info = rSR_Bayesian_info;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    public MetricInfo getRSR_Spm_Spr_avg_info() {
        return RSR_Spm_Spr_avg_info;
    }

    public void setRSR_Spm_Spr_avg_info(MetricInfo rSR_Spm_Spr_avg_info) {
        RSR_Spm_Spr_avg_info = rSR_Spm_Spr_avg_info;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    public MetricInfo getRSR_Spm_Spq_avg_info() {
        return RSR_Spm_Spq_avg_info;
    }

    public void setRSR_Spm_Spq_avg_info(MetricInfo rSR_Spm_Spq_avg_info) {
        RSR_Spm_Spq_avg_info = rSR_Spm_Spq_avg_info;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    public MetricInfo getRSR_alt_1_info() {
        return RSR_alt_1_info;
    }

    public void setRSR_alt_1_info(MetricInfo rSR_alt_1_info) {
        RSR_alt_1_info = rSR_alt_1_info;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    public MetricInfo getRSR_alt_2_info() {
        return RSR_alt_2_info;
    }

    public void setRSR_alt_2_info(MetricInfo rSR_alt_2_info) {
        RSR_alt_2_info = rSR_alt_2_info;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    public MetricInfo getASR_info() {
        return ASR_info;
    }

    public void setASR_info(MetricInfo aSR_info) {
        ASR_info = aSR_info;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    public MetricInfo getRMR_info() {
        return RMR_info;
    }

    public void setRMR_info(MetricInfo rMR_info) {
        RMR_info = rMR_info;
    }

    public MetricType getEvaluated_RSR_Type() {
        return evaluated_RSR_Type;
    }

    public void setEvaluated_RSR_Type(MetricType evaluated_RSR_Type) {
        this.evaluated_RSR_Type = evaluated_RSR_Type;
    }

    public Boolean isUse_asr_as_evaluation_metric() {
        return use_asr_as_evaluation_metric;
    }

    public void setUse_asr_as_evaluation_metric(Boolean use_asr_as_evaluation_metric) {
        this.use_asr_as_evaluation_metric = use_asr_as_evaluation_metric;
    }

    public Boolean isUse_rsr_asr_task_weights() {
        return use_rsr_asr_task_weights;
    }

    public void setUse_rsr_asr_task_weights(Boolean use_rsr_asr_task_weights) {
        this.use_rsr_asr_task_weights = use_rsr_asr_task_weights;
    }

    @ElementCollection
    public List<Double> getRsr_asr_task_weights() {
        return rsr_asr_task_weights;
    }

    public void setRsr_asr_task_weights(List<Double> rsr_asr_task_weights) {
        this.rsr_asr_task_weights = rsr_asr_task_weights;
    }

    public Double getRsr_asr_weight() {
        return rsr_asr_weight;
    }

    public void setRsr_asr_weight(Double rsr_asr_weight) {
        this.rsr_asr_weight = rsr_asr_weight;
    }

    public Double getRmr_weight() {
        return rmr_weight;
    }

    public void setRmr_weight(Double rmr_weight) {
        this.rmr_weight = rmr_weight;
    }

    public Double getCfa_pass_threshold() {
        return cfa_pass_threshold;
    }

    public void setCfa_pass_threshold(Double cfa_pass_threshold) {
        this.cfa_pass_threshold = cfa_pass_threshold;
    }

    public Double getCpa_pass_threshold() {
        return cpa_pass_threshold;
    }

    public void setCpa_pass_threshold(Double cpa_pass_threshold) {
        this.cpa_pass_threshold = cpa_pass_threshold;
    }
}
