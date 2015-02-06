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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.AbstractMetricsInfo;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.MetricInfo;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.TrialIdentifier;

/**
 * Contains descriptions of each Phase 2 assessment metric that will be calculated.
 *
 * @author CBONACETO
 *
 */
public class MetricsInfo extends AbstractMetricsInfo implements Serializable {

    private static final long serialVersionUID = -3002661415799580471L;
    
    /** SIGINT probabilities metric info (contains missions for which average
     P(Attack|SIGINT) was provided by participants) */
    protected MetricInfo sigint_avg_info;

    /**** CFA metrics ****/
    /** Anchoring & Adjustment metric info */
    protected MetricInfo AA_info;
    
    /** Persistence of Discredited Evidence metric info */
    protected MetricInfo PDE_info;
    
    /** Representativeness metric info */
    protected MetricInfo RR_info;
    
    /** Availability metric info */
    protected MetricInfo AV_info;
    
    /** Probability Matching metric info */
    protected MetricInfo PM_info;

    /** Confirmation Bias metric info */
    protected MetricInfo CS_info;    
    
    /** Change Blindness metric info */
    protected MetricInfo CB_info;
    
    /** Satisfaction of Search metric info */
    protected MetricInfo SS_info;
    /**** End CFA metrics ****/

    /**** CPA RSR (Relative Success Rate) metrics  *****/
    /** RSR metric info */
    protected MetricInfo RSR_info;

    /** RSR-Bayesian metric info */
    protected MetricInfo RSR_Bayesian_info;

    /** RSR based on the average Spm and Spr metric info */
    protected MetricInfo RSR_Spm_Spr_avg_info;

    /** RSR based on the average Spm and Spq metric info */
    protected MetricInfo RSR_Spm_Spq_avg_info;

    /** RSR alternative 1 [RSR(RMSE)] metric info */
    protected MetricInfo RSR_alt_1_info; //RSR(RMSE)

    /** RSR alternative 2 [RSR(RMSE)-Bayesian] metric info */
    protected MetricInfo RSR_alt_2_info; //RSR(RMSE_Bayesian)
    /**** End CPA RSR (Relative Success Rate) metrics  ****/

    /** CPA ASR (Absolute Success Rate) metric info */
    protected MetricInfo ASR_info;

    /** CPA RMR metric info  */
    protected MetricInfo RMR_info;

    /**The "official" version of RSR that will be used for scoring a model if
     * RSR is used (and not ASR)  */
    protected MetricType evaluated_RSR_Type;

    /** Whether ASR is used instead of RSR as the official CPA metric for scoring
     * a model */
    protected Boolean use_asr_as_evaluation_metric;

    /** RSR, ASR, and RMR task weights */
    protected List<Double> rsr_asr_task_weights;

    /** Whether to compute RSR, ASR, and RMR averages using the task weights */
    protected Boolean use_rsr_asr_task_weights;

    /** Weight of RSR or ASR when computing the overall CPA score  */
    protected Double rsr_asr_weight;

    /** Weight of RMR when computing the overall CPA score  */
    protected Double rmr_weight;

    /** The CFA pass threshold */
    protected Double cfa_pass_threshold;

    /** The CPA pass threshold */
    protected Double cpa_pass_threshold;
    
    /**
     * Create a MetricsInfo instance with the current default settings.
     *
     * @return
     */
    public static MetricsInfo createDefaultMetricsInfo() {
        MetricsInfo mi = new MetricsInfo();
        int id = 0;
        double cfaMetricsThreshold = 65d;
        double cpaMetricsThreshold = 65d;
        
        //Trials per mission in final exam: 1-10, 2-10, 3-10 (2 locations), 4-30, 5-40
        
        mi.sigint_avg_info = new MetricInfo();
        mi.sigint_avg_info.setTasks(new TreeSet<Integer>(Arrays.asList(1)));
        mi.sigint_avg_info.setNum_trials_stages(10);
        
        mi.AA_info = new MetricInfo("Anchoring & Adjustment (AA)", MetricType.AA_ID);
        mi.AA_info.setDescription("Bias Magnitude = Nq - Np, Bias Exhibited When Np < Nq");
        //Excluded trials: Mission 1, Trials 1, 3, 5, and 10. 
        //                 Mission 2, Trials 2, 5, 6, 9, and 10        
        mi.AA_info.setTasks(new TreeSet<Integer>(Arrays.asList(1, 2)));
        mi.AA_info.setExcludedTrials(Arrays.asList(
                new TrialIdentifier(1, 1, 0),
                new TrialIdentifier(1, 3, 0),
                new TrialIdentifier(1, 5, 0),
                new TrialIdentifier(1, 10, 0),
                new TrialIdentifier(2, 2, 0),
                new TrialIdentifier(2, 5, 0),
                new TrialIdentifier(2, 6, 0),
                new TrialIdentifier(2, 9, 0),
                new TrialIdentifier(2, 10, 0)));
        mi.AA_info.setUseTaskWeights(true);
        mi.AA_info.setTaskWeights(Arrays.asList(1.d/2, 1.d/2, 0.d, 0.d, 0.d));        
        mi.AA_info.setNum_trials_stages(11);
        mi.AA_info.setCalculated(true);
        mi.AA_info.setAssessed(true);
        mi.AA_info.setOverall_pass_threshold(cfaMetricsThreshold);
        
        mi.PDE_info = new MetricInfo("Persistence of Discredited Evidence (PDE)", MetricType.PDE_ID);
        mi.PDE_info.setDescription("Bias Magnitude = Nq - Np, Bias Exhibited When Np < Nq");
        mi.PDE_info.setTasks(new TreeSet<Integer>(Arrays.asList(4, 5)));  
        //Excluded trials: Mission 4, Trials 1, 3, 4, 5, 7, 8, 9, 15, 16. 
        //                 Mission 5: Trials 1, 4, 23
        mi.PDE_info.setExcludedTrials(Arrays.asList(
                new TrialIdentifier(4, 1, 0),
                new TrialIdentifier(4, 3, 0),
                new TrialIdentifier(4, 4, 0),
                new TrialIdentifier(4, 5, 0),
                new TrialIdentifier(4, 7, 0),
                new TrialIdentifier(4, 8, 0),
                new TrialIdentifier(4, 9, 0),
                new TrialIdentifier(4, 15, 0),
                new TrialIdentifier(4, 16, 0),
                new TrialIdentifier(5, 1, 0),
                new TrialIdentifier(5, 4, 0),
                new TrialIdentifier(5, 23, 0)));
        mi.PDE_info.setUseTaskWeights(true);
        mi.PDE_info.setTaskWeights(Arrays.asList(0d, 0d, 0.d, 1.d/2, 1.d/2));
        mi.PDE_info.setNum_trials_stages(58);
        mi.PDE_info.setCalculated(true);
        mi.PDE_info.setAssessed(true);
        mi.PDE_info.setOverall_pass_threshold(cfaMetricsThreshold);         
        
        mi.RR_info = new MetricInfo("Representativeness (RR)", MetricType.RR_ID);
        mi.RR_info.setDescription("Bias Magnitude = P - Q, Bias Exhibited when P > Q");
        mi.RR_info.setTasks(new TreeSet<Integer>(Arrays.asList(1, 2, 3)));
        mi.RR_info.setUseTaskWeights(true);
        mi.RR_info.setTaskWeights(Arrays.asList(1.d/3, 1.d/3, 1.d/3, 0.d, 0.d));        
        mi.RR_info.setNum_trials_stages(40);
        mi.RR_info.setCalculated(true);
        mi.RR_info.setAssessed(true);
        mi.RR_info.setOverall_pass_threshold(cfaMetricsThreshold);         
        
        mi.AV_info = new MetricInfo("Availability (AV)", MetricType.AV_ID);
        mi.AV_info.setDescription("Bias Magnitude = Nq - Np, Bias Exhibited When Np < Nq");
        mi.AV_info.setTasks(new TreeSet<Integer>(Arrays.asList(1)));
        mi.AV_info.setUseTaskWeights(true);
        mi.AV_info.setTaskWeights(Arrays.asList(1.d, 0.d, 0.d, 0.d, 0.d));        
        mi.AV_info.setNum_trials_stages(10);
        mi.AV_info.setCalculated(true);
        mi.AV_info.setAssessed(true);
        mi.AV_info.setOverall_pass_threshold(cfaMetricsThreshold);         

        mi.PM_info = new MetricInfo("Probability Matching (PM)", MetricType.PM_ID);
        mi.PM_info.setDescription("Bias Magnitude = 1 - n, Bias Exhibited When n < 1 (n = frequency with which normative Blue action selected)");
        mi.PM_info.setTasks(new TreeSet<Integer>(Arrays.asList(2, 3, 4, 5)));
        mi.PM_info.setUseTaskWeights(true);
        mi.PM_info.setTaskWeights(Arrays.asList(0.d, 1.d/4, 1.d/4, 1.d/4, 1.d/4));        
        mi.PM_info.setNum_trials_stages(90);
        mi.PM_info.setCalculated(true);
        mi.PM_info.setAssessed(true);
        mi.PM_info.setOverall_pass_threshold(cfaMetricsThreshold);

        mi.CS_info = new MetricInfo("Confirmation Bias (Seeking) (CS)", MetricType.CS_ID);
        mi.CS_info.setDescription("Bias Magnitude = 1 - f, Bias Exhibited When f < 1 (f = frequency with which normative SIGINT location selected)");
        mi.CS_info.setTasks(new TreeSet<Integer>(Arrays.asList(3)));
        mi.CS_info.setUseTaskWeights(true);
        mi.CS_info.setTaskWeights(Arrays.asList(0.d, 0.d, 1.d, 0.d, 0.d));        
        mi.CS_info.setNum_trials_stages(10);
        mi.CS_info.setCalculated(true);
        mi.CS_info.setAssessed(true);
        mi.CS_info.setTask_pass_threshold(cfaMetricsThreshold);
        mi.CS_info.setOverall_pass_threshold(cfaMetricsThreshold);
        
        mi.CB_info = new MetricInfo("Change Blindness (CB)", MetricType.CB_ID);
        mi.CB_info.setDescription("Bias Magnitude = b - 1, Bias Exhibited When b > 1 (b = number of trials needed to detect a change in Red tactics)");        
        mi.CB_info.setTasks(new TreeSet<Integer>(Arrays.asList(4)));
        mi.CB_info.setUseTaskWeights(true);
        mi.CB_info.setTaskWeights(Arrays.asList(0d, 0d, 0.d, 1.d, 0.d));
        mi.CB_info.setNum_trials_stages(null);
        mi.CB_info.setCalculated(true);
        mi.CB_info.setAssessed(true);
        mi.CB_info.setOverall_pass_threshold(cfaMetricsThreshold);        
        
        mi.SS_info = new MetricInfo("Satisfaction of Search (SS)", MetricType.SS_ID);
        mi.SS_info.setDescription("Bias Magnitude = 1 - s, Bias Exhibited When s < 1 (s = fraction of trials reviewed when creating a batch plot)");
        mi.SS_info.setTasks(new TreeSet<Integer>(Arrays.asList(4, 5)));
        mi.SS_info.setUseTaskWeights(true);
        mi.SS_info.setTaskWeights(Arrays.asList(0d, 0d, 0.d, 1.d/2, 1.d/2));
        mi.SS_info.setNum_trials_stages(7);
        mi.SS_info.setCalculated(true);
        mi.SS_info.setAssessed(true);
        mi.SS_info.setOverall_pass_threshold(cfaMetricsThreshold);
        
        mi.ASR_info = createDefaultASRorRSRInfo(++id, "Abolute Success Rate (ASR)", MetricType.ASR,
                "Absolute Success Rate", cpaMetricsThreshold);
        mi.RSR_info = createDefaultASRorRSRInfo(++id, "Relative Success Rate (RSR)", MetricType.RSR_Standard,
                "Relative Success Rate using KLD and uniform null model", cpaMetricsThreshold);
        mi.RSR_Bayesian_info = createDefaultASRorRSRInfo(++id, "RSR(Bayesian Null Model)", MetricType.RSR_Bayesian,
                "Relative Success Rate using KLD and Bayesian null model", cpaMetricsThreshold);
        mi.RSR_Spm_Spr_avg_info = createDefaultASRorRSRInfo(++id, "RSR(Spm-Spr-avg)", MetricType.RSR_Spm_Spr_avg,
                "Relative Success Rate using average Spm and Spr", cpaMetricsThreshold);
        mi.RSR_Spm_Spq_avg_info = createDefaultASRorRSRInfo(++id, "RSR(Spm-Spq-avg)", MetricType.RSR_Spm_Spq_avg,
                "Relative Success Rate using average Spm and Spq", cpaMetricsThreshold);
        mi.RSR_alt_1_info = createDefaultASRorRSRInfo(++id, "RSR(Random Null Model)(RMSE)", MetricType.RSR_alt_1,
                "Relative Success Rate using RMSE and uniform null model", cpaMetricsThreshold);
        mi.RSR_alt_2_info = createDefaultASRorRSRInfo(++id, "RSR(Bayesian Null Model)(RMSE)", MetricType.RSR_alt_2,
                "Relative Success Rate using RMSE and Bayesian null model", cpaMetricsThreshold);       

        mi.RMR_info = new MetricInfo("Relative Match Rate (RMR)", MetricType.RMR_ID);
        mi.RMR_info.setDescription("Relative Match Rate");
        mi.RMR_info.setTasks(new TreeSet<Integer>(Arrays.asList(2, 3, 4, 5)));
        mi.RMR_info.setUseTaskWeights(true);
        mi.RMR_info.setTaskWeights(Arrays.asList(0d, 1.d/4, 1.d/4, 1.d/4, 1.d/4));
        mi.RMR_info.setNum_trials_stages(100);
        mi.RMR_info.setCalculated(true);
        mi.RMR_info.setAssessed(true);
        mi.RMR_info.setTrial_pass_threshold(cpaMetricsThreshold);
        mi.RMR_info.setTask_pass_threshold(cpaMetricsThreshold);
        mi.RMR_info.setOverall_pass_threshold(cpaMetricsThreshold);

        mi.evaluated_RSR_Type = MetricType.RSR_Standard;
        mi.use_asr_as_evaluation_metric = true;
        mi.use_rsr_asr_task_weights = true;
        mi.rsr_asr_task_weights = new ArrayList<Double>(Arrays.asList(1.d/5, 1.d/5, 1.d/5, 1.d/5, 1.d/5));
        mi.rsr_asr_weight = 0.5d;
        mi.rmr_weight = 0.5d;

        mi.cfa_pass_threshold = cfaMetricsThreshold;
        mi.cpa_pass_threshold = cpaMetricsThreshold;

        return mi;
    }

    /**
     *
     * @param id
     * @param name
     * @param rsrType
     * @param description
     * @param passThreshold
     * @return
     */
    protected static MetricInfo createDefaultASRorRSRInfo(int id, String name, MetricType rsrType, String description, Double passThreshold) {
        MetricInfo rsr_info = new MetricInfo(name, rsrType.ordinal());
        rsr_info.setDescription(description);
        rsr_info.setTasks(new TreeSet<Integer>(Arrays.asList(1, 2, 3, 4, 5)));
        rsr_info.setExcludedTrials(Arrays.asList(
                new TrialIdentifier(4, 1, 1), //No Red tactics probe on trial 1
                new TrialIdentifier(5, 1, 1))); //No Red tactics probe on trial 1
        rsr_info.setNum_trials_stages(100);
        rsr_info.setCalculated(true);
        rsr_info.setAssessed(true);
        rsr_info.setTrial_pass_threshold(passThreshold);
        rsr_info.setTask_pass_threshold(passThreshold);
        rsr_info.setOverall_pass_threshold(passThreshold);
        return rsr_info;
    }

    public MetricInfo getSigint_avg_info() {
        return sigint_avg_info;
    }

    public void setSigint_avg_info(MetricInfo sigint_avg_info) {
        this.sigint_avg_info = sigint_avg_info;
    }

    public MetricInfo getAA_info() {
        return AA_info;
    }

    public void setAA_info(MetricInfo AA_info) {
        this.AA_info = AA_info;
    }

    public MetricInfo getPDE_info() {
        return PDE_info;
    }

    public void setPDE_info(MetricInfo PDE_info) {
        this.PDE_info = PDE_info;
    }

    public MetricInfo getRR_info() {
        return RR_info;
    }

    public void setRR_info(MetricInfo RR_info) {
        this.RR_info = RR_info;
    }

    public MetricInfo getAV_info() {
        return AV_info;
    }

    public void setAV_info(MetricInfo AV_info) {
        this.AV_info = AV_info;
    }

    public MetricInfo getPM_info() {
        return PM_info;
    }

    public void setPM_info(MetricInfo PM_info) {
        this.PM_info = PM_info;
    }

    public MetricInfo getCS_info() {
        return CS_info;
    }

    public void setCS_info(MetricInfo CS_info) {
        this.CS_info = CS_info;
    }    

    public MetricInfo getCB_info() {
        return CB_info;
    }

    public void setCB_info(MetricInfo CB_info) {
        this.CB_info = CB_info;
    }

    public MetricInfo getSS_info() {
        return SS_info;
    }

    public void setSS_info(MetricInfo SS_info) {
        this.SS_info = SS_info;
    }

    public MetricInfo getRSR_info() {
        return RSR_info;
    }

    public void setRSR_info(MetricInfo RSR_info) {
        this.RSR_info = RSR_info;
    }

    public MetricInfo getRSR_Bayesian_info() {
        return RSR_Bayesian_info;
    }

    public void setRSR_Bayesian_info(MetricInfo RSR_Bayesian_info) {
        this.RSR_Bayesian_info = RSR_Bayesian_info;
    }

    public MetricInfo getRSR_Spm_Spr_avg_info() {
        return RSR_Spm_Spr_avg_info;
    }

    public void setRSR_Spm_Spr_avg_info(MetricInfo RSR_Spm_Spr_avg_info) {
        this.RSR_Spm_Spr_avg_info = RSR_Spm_Spr_avg_info;
    }

    public MetricInfo getRSR_Spm_Spq_avg_info() {
        return RSR_Spm_Spq_avg_info;
    }

    public void setRSR_Spm_Spq_avg_info(MetricInfo RSR_Spm_Spq_avg_info) {
        this.RSR_Spm_Spq_avg_info = RSR_Spm_Spq_avg_info;
    }

    public MetricInfo getRSR_alt_1_info() {
        return RSR_alt_1_info;
    }

    public void setRSR_alt_1_info(MetricInfo RSR_alt_1_info) {
        this.RSR_alt_1_info = RSR_alt_1_info;
    }

    public MetricInfo getRSR_alt_2_info() {
        return RSR_alt_2_info;
    }

    public void setRSR_alt_2_info(MetricInfo RSR_alt_2_info) {
        this.RSR_alt_2_info = RSR_alt_2_info;
    }

    public MetricInfo getASR_info() {
        return ASR_info;
    }

    public void setASR_info(MetricInfo ASR_info) {
        this.ASR_info = ASR_info;
    }

    public MetricInfo getRMR_info() {
        return RMR_info;
    }

    public void setRMR_info(MetricInfo RMR_info) {
        this.RMR_info = RMR_info;
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

    public List<Double> getRsr_asr_task_weights() {
        return rsr_asr_task_weights;
    }

    public void setRsr_asr_task_weights(List<Double> rsr_asr_task_weights) {
        this.rsr_asr_task_weights = rsr_asr_task_weights;
    }

    public Boolean isUse_rsr_asr_task_weights() {
        return use_rsr_asr_task_weights;
    }

    public void setUse_rsr_asr_task_weights(Boolean use_rsr_asr_task_weights) {
        this.use_rsr_asr_task_weights = use_rsr_asr_task_weights;
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