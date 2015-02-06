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

import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.AbstractTrialMetrics;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.CFAMetric;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.CPAMetric;

/**
 * Contains Phase 2 assessment metrics for a trial in a mission in a Phase 2
 * exam.
 *
 * @author CBONACETO
 *
 */
public class TrialMetrics extends AbstractTrialMetrics {

    private static final long serialVersionUID = -9043939822505971126L;

    //Actions: assess probabilities, create batch plots, assess styles (most likely style, P of each style), 
    //select SIGINT locations, select Blue actions at locations
    
    /** Whether the CFA (Cognitive Fidelity Assessment) metrics are stale and
     * need to be re-calculated */
    protected Boolean cfa_metrics_stale;

    /** Whether the CPA (Cognitive Performance Assessment) metrics are stale and
     * need to be re-calculated */
    protected Boolean cpa_metrics_stale;    
    
    /** Negentropy metrics. Contains participant and Bayesian negentropies for 
     probability reports in the trial */
    protected NegentropyMetrics negentropyMetrics;    

    /** Anchoring and Adjustment metrics for the trial */
    protected CFAMetric AA_metrics;

    /** Persistence of Discredited Evidence metrics the trial */
    protected CFAMetric PDE_metrics;

    /** Representativness metrics for the trial at each location (Computed for the Ppc stage at each location) */
    protected List<CFAMetric> RR_metrics;

    /** Availability metrics for the trial (Computed for the Pt stage) */
    protected CFAMetric AV_metrics;

    /** The frequency with which the participant selected the normative Blue
     * option (used to compute Probability Matching for the mission, the "n" value) */
    protected Double PM_normativeBlueOptionSelections; 
    protected Double PM_normativeBlueOptionSelections_std;

    /** The frequency with which the participant selected the normative SIGINT
     * location (used to compute Confirmation Bias for the mission, the "f" value) */
    protected Double CS_sigintAtHighestPaLocationSelections;
    protected Double CS_sigintAtHighestPaLocationSelections_std;

    /** Whether the Red tactic changed on the trial (used to compute Change
     * Blindness for the mission). True if the Red tactics on this trial are
     different from the tactics on the previous trial */
    protected Boolean CB_redTacticChanged;
    
    /** True if the participant assigned the highest probability to the tactic
     Red is actually playing with on the trial (used to compute Change
     * Blindness for the mission) */
    protected Boolean CB_redTacticCorrectlyDetected;   

    /** The percent of previous trials the participant reviewed when creating a
     * batch plot (used to compute Satisfaction of Search for the mission) */
    protected Double SS_percentTrialsReviewedInBatchPlot;
    protected Double SS_percentTrialsReviewedInBatchPlot_std;

    /** RSR and ASR metrics for the trial */
    protected RSRAndASRTrialMetrics RSRandASR_metrics;

    /** The frequency with which SIGINT was obtained at each location */
    protected List<SigintSelectionFrequency> RMR_frequencySigintSelectedAtEachLocation;

    /** The frequency with which each Blue action permutation was selected */
    protected List<BlueActionSelectionFrequency> RMR_frequencyBlueActionsSelected;

    /** RMR metrics computed based on the SIGINT selection */
    protected CPAMetric RMR_sigint_metrics;
    
    /** RMR metrics computed based on the Blue action selection */
    protected CPAMetric RMR_blueAction_metrics;    

    public List<NameValuePair> getMetricValuesAsString() {
        //TODO: Implement this
        return null;
    }

    public Boolean isCfa_metrics_stale() {
        return cfa_metrics_stale;
    }

    public void setCfa_metrics_stale(Boolean cfa_metrics_stale) {
        this.cfa_metrics_stale = cfa_metrics_stale;
    }

    public Boolean isCpa_metrics_stale() {
        return cpa_metrics_stale;
    }

    public void setCpa_metrics_stale(Boolean cpa_metrics_stale) {
        this.cpa_metrics_stale = cpa_metrics_stale;
    }

    public NegentropyMetrics getNegentropyMetrics() {
        return negentropyMetrics;
    }

    public void setNegentropyMetrics(NegentropyMetrics negentropyMetrics) {
        this.negentropyMetrics = negentropyMetrics;
    }

    public CFAMetric getAA_metrics() {
        return AA_metrics;
    }

    public void setAA_metrics(CFAMetric AA_metrics) {
        this.AA_metrics = AA_metrics;
    }

    public CFAMetric getPDE_metrics() {
        return PDE_metrics;
    }

    public void setPDE_metrics(CFAMetric PDE_metrics) {
        this.PDE_metrics = PDE_metrics;
    }    

    /*public CFAMetric getRR_metrics() {
    return RR_metrics;
    }
    public void setRR_metrics(CFAMetric RR_metrics) {
    this.RR_metrics = RR_metrics;
    }*/
    public List<CFAMetric> getRR_metrics() {
        return RR_metrics;
    }

    public void setRR_metrics(List<CFAMetric> RR_metrics) {
        this.RR_metrics = RR_metrics;
    } 

    public CFAMetric getAV_metrics() {
        return AV_metrics;
    }

    public void setAV_metrics(CFAMetric AV_metrics) {
        this.AV_metrics = AV_metrics;
    }

    public Double getPM_normativeBlueOptionSelections() {
        return PM_normativeBlueOptionSelections;
    }

    public void setPM_normativeBlueOptionSelections(Double PM_normativeBlueOptionSelections) {
        this.PM_normativeBlueOptionSelections = PM_normativeBlueOptionSelections;
    }

    public Double getPM_normativeBlueOptionSelections_std() {
        return PM_normativeBlueOptionSelections_std;
    }

    public void setPM_normativeBlueOptionSelections_std(Double PM_normativeBlueOptionSelections_std) {
        this.PM_normativeBlueOptionSelections_std = PM_normativeBlueOptionSelections_std;
    } 

    public Double getCS_sigintAtHighestPaLocationSelections() {
        return CS_sigintAtHighestPaLocationSelections;
    }

    public void setCS_sigintAtHighestPaLocationSelections(Double CS_sigintAtHighestPaLocationSelections) {
        this.CS_sigintAtHighestPaLocationSelections = CS_sigintAtHighestPaLocationSelections;
    }

    public Double getCS_sigintAtHighestPaLocationSelections_std() {
        return CS_sigintAtHighestPaLocationSelections_std;
    }

    public void setCS_sigintAtHighestPaLocationSelections_std(Double CS_sigintAtHighestPaLocationSelections_std) {
        this.CS_sigintAtHighestPaLocationSelections_std = CS_sigintAtHighestPaLocationSelections_std;
    }

    public Boolean isCB_redTacticChanged() {
        return CB_redTacticChanged;
    }

    public void setCB_redTacticChanged(Boolean CB_redTacticChanged) {
        this.CB_redTacticChanged = CB_redTacticChanged;
    }    

    public Boolean isCB_redTacticCorrectlyDetected() {
        return CB_redTacticCorrectlyDetected;
    }

    public void setCB_redTacticCorrectlyDetected(Boolean CB_redTacticCorrectlyDetected) {
        this.CB_redTacticCorrectlyDetected = CB_redTacticCorrectlyDetected;
    }  

    public Double getSS_percentTrialsReviewedInBatchPlot() {
        return SS_percentTrialsReviewedInBatchPlot;
    }

    public void setSS_percentTrialsReviewedInBatchPlot(Double SS_percentTrialsReviewedInBatchPlot) {
        this.SS_percentTrialsReviewedInBatchPlot = SS_percentTrialsReviewedInBatchPlot;
    }

    public Double getSS_percentTrialsReviewedInBatchPlot_std() {
        return SS_percentTrialsReviewedInBatchPlot_std;
    }

    public void setSS_percentTrialsReviewedInBatchPlot_std(Double SS_percentTrialsReviewedInBatchPlot_std) {
        this.SS_percentTrialsReviewedInBatchPlot_std = SS_percentTrialsReviewedInBatchPlot_std;
    }

    public RSRAndASRTrialMetrics getRSRandASR_metrics() {
        return RSRandASR_metrics;
    }

    public void setRSRandASR_metrics(RSRAndASRTrialMetrics RSRandASR_metrics) {
        this.RSRandASR_metrics = RSRandASR_metrics;
    }

    public List<SigintSelectionFrequency> getRMR_frequencySigintSelectedAtEachLocation() {
        return RMR_frequencySigintSelectedAtEachLocation;
    }

    public void setRMR_frequencySigintSelectedAtEachLocation(List<SigintSelectionFrequency> RMR_frequencySigintSelectedAtEachLocation) {
        this.RMR_frequencySigintSelectedAtEachLocation = RMR_frequencySigintSelectedAtEachLocation;
    }    

    public List<BlueActionSelectionFrequency> getRMR_frequencyBlueActionsSelected() {
        return RMR_frequencyBlueActionsSelected;
    }

    public void setRMR_frequencyBlueActionsSelected(List<BlueActionSelectionFrequency> RMR_frequencyBlueActionsSelected) {
        this.RMR_frequencyBlueActionsSelected = RMR_frequencyBlueActionsSelected;
    }   

    public CPAMetric getRMR_sigint_metrics() {
        return RMR_sigint_metrics;
    }

    public void setRMR_sigint_metrics(CPAMetric RMR_sigint_metrics) {
        this.RMR_sigint_metrics = RMR_sigint_metrics;
    }

    public CPAMetric getRMR_blueAction_metrics() {
        return RMR_blueAction_metrics;
    }

    public void setRMR_blueAction_metrics(CPAMetric RMR_blueAction_metrics) {
        this.RMR_blueAction_metrics = RMR_blueAction_metrics;
    }   
}