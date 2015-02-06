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
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import org.mitre.icarus.cps.assessment.data_model.base.DataType;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.AbstractTaskMetrics;

import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.CFAMetric;
import org.mitre.icarus.cps.exam.base.response.ResponseGeneratorData;
import org.mitre.icarus.cps.exam.phase_2.testing.IcarusTestTrial_Phase2;
import org.mitre.icarus.cps.exam.phase_2.testing.Mission;

/**
 * Contains Phase 2 assessment metrics for a mission in a Phase 2 exam.
 * 
 * @author CBONACETO
 *
 */
public class MissionMetrics extends AbstractTaskMetrics<TrialData> implements Serializable {

    /** Anchoring and Adjustment score using the SMR (Simple Match Rate) method */
    protected Double AA_smr_score;

    /** Persistence of Discredited Evidences score using the SMR (Simple Match Rate) method */
    protected Double PDE_smr_score;

    /** Representativeness score using the SMR (Simple Match Rate) method */
    protected Double RR_smr_score;

    /** Availability score score using the SMR (Simple Match Rate) method */
    protected Double AV_smr_score;    
    
    /** The frequency with which the participant selected the normative Blue option across trials 
     in the mission ( the "n" number for the mission) */
    protected Double PM_normativeBlueOptionSelectionFrequency;
    protected Double PM_normativeBlueOptionSelectionFrequency_std;    
    
    /** Probability Matching metrics for the mission, 
     * computed using the MSR (Marginal Success Rate) method */
    protected CFAMetric PM_metrics;
    
    /** The frequency with which the participant selected the normative SIGINT
     * location across trials in the mission (the "f" number for the mission) */
    protected Double CS_sigintHighestPaSelectionFrequency;
    protected Double CS_sigintHighestPaSelectionFrequency_std;    
    
    /** Confirmation Bias metric for the mission,
     computed using the MSR (Marginal Success Rate) method */
    protected CFAMetric CS_metrics;
    
    /** The number of times Red tactics changed during the mission */
    protected Integer CB_numRedTacticsChanges;
    
    /** Whether each Red tactics change was detected before the end of the mission */
    protected List<Boolean> CB_redTacticsChangedDetected;
    
    /** The average number of trials it took the participant to identify each
     Red tactics change (the "b" number for the mission) */
    protected Double CB_trialsNeededToDetectRedTacticChanges_avg;    
    protected Double CB_trialsNeededToDetectRedTacticChanges_std;
    
    /** Change blindness metrics for the mission, computed using the MSR
     * (Marginal Success Rate) method */
    protected CFAMetric CB_metrics;
   
    /** The average percent of trials reviewed by the participant when creating a batch plot
     across batch plot trials in the mission (the "s" number for the mission) */
    protected Double SS_percentTrialsReviewedInBatchPlot_avg;
    protected Double SS_percentTrialsReviewedInBatchPlot_avg_std;    
    
    /** Satisfaction of search metrics for the mission, 
     * computed using the MSR (Marginal Success Rate) method */
     protected CFAMetric SS_metrics;

    /** RSR and ASR average scores */
    protected RSRAndASRMissionMetrics RSR_ASR;
    
    /** RMR (Relative Match Rate) average score and STD just based on SIGINT */
    protected Double RMR_sigint_avg;
    protected Double RMR_sigint_std;
    
    /** RMR (Relative Match Rate) average score and STD just based on Blue Action selections */
    protected Double RMR_blueAction_avg;    
    protected Double RMR_blueAction_std;    
    
    /** RMR (Relative Match Rate) average score and STD based on both SIGINT 
     * selections (if available) and Blue Action selections */
    protected Double RMR_avg;
    protected Double RMR_std;
    
    /** The average values of P(Attack|Chatter) and P(Attack|Silent) for the
     mission (in percent format) */
    protected SubjectSigintProbabilities sigintProbs_avg;
    
    public MissionMetrics() {}
    
    public MissionMetrics(Mission<?> mission, ResponseGeneratorData responseGenerator, DataType data_type,
            String exam_id, Integer mission_number) {
        super(responseGenerator, data_type, exam_id, mission != null ? mission.getId() : null, mission_number);        
        if(mission != null) {
            if(mission.getTestTrials() != null && !mission.getTestTrials().isEmpty()) {
                trials = new ArrayList<TrialData>(mission.getTestTrials().size());
                //for (int i = 0; i < mission.getTestTrials().size(); i++) {                    
                for(IcarusTestTrial_Phase2 trial : mission.getTestTrials()) {
                    trials.add(new TrialData(trial, responseGenerator, data_type, 
                            exam_id, mission.getId(), mission_number));
                    //trials.add(new TrialData());
                }
            }
        }
    }

    public Double getAA_smr_score() {
        return AA_smr_score;
    }

    public void setAA_smr_score(Double AA_smr_score) {
        this.AA_smr_score = AA_smr_score;
    }

    public Double getPDE_smr_score() {
        return PDE_smr_score;
    }

    public void setPDE_smr_score(Double PDE_smr_score) {
        this.PDE_smr_score = PDE_smr_score;
    }

    public Double getRR_smr_score() {
        return RR_smr_score;
    }

    public void setRR_smr_score(Double RR_smr_score) {
        this.RR_smr_score = RR_smr_score;
    }

    public Double getAV_smr_score() {
        return AV_smr_score;
    }

    public void setAV_smr_score(Double AV_smr_score) {
        this.AV_smr_score = AV_smr_score;
    }

    public Double getPM_normativeBlueOptionSelectionFrequency() {
        return PM_normativeBlueOptionSelectionFrequency;
    }

    public void setPM_normativeBlueOptionSelectionFrequency(Double PM_normativeBlueOptionSelectionFrequency) {
        this.PM_normativeBlueOptionSelectionFrequency = PM_normativeBlueOptionSelectionFrequency;
    }

    public Double getPM_normativeBlueOptionSelectionFrequency_std() {
        return PM_normativeBlueOptionSelectionFrequency_std;
    }

    public void setPM_normativeBlueOptionSelectionFrequency_std(Double PM_normativeBlueOptionSelectionFrequency_std) {
        this.PM_normativeBlueOptionSelectionFrequency_std = PM_normativeBlueOptionSelectionFrequency_std;
    }    
    
    public CFAMetric getPM_metrics() {
        return PM_metrics;
    }

    public void setPM_metrics(CFAMetric PM_metrics) {
        this.PM_metrics = PM_metrics;
    }

    public Double getCS_sigintHighestPaSelectionFrequency() {
        return CS_sigintHighestPaSelectionFrequency;
    }

    public void setCS_sigintHighestPaSelectionFrequency(Double CS_sigintHighestPaSelectionFrequency) {
        this.CS_sigintHighestPaSelectionFrequency = CS_sigintHighestPaSelectionFrequency;
    }

    public Double getCS_sigintHighestPaSelectionFrequency_std() {
        return CS_sigintHighestPaSelectionFrequency_std;
    }

    public void setCS_sigintHighestPaSelectionFrequency_std(Double CS_sigintHighestPaSelectionFrequency_std) {
        this.CS_sigintHighestPaSelectionFrequency_std = CS_sigintHighestPaSelectionFrequency_std;
    }
    
    public CFAMetric getCS_metrics() {
        return CS_metrics;
    }

    public void setCS_metrics(CFAMetric CS_metrics) {
        this.CS_metrics = CS_metrics;
    }

    public Integer getCB_numRedTacticsChanges() {
        return CB_numRedTacticsChanges;
    }

    public void setCB_numRedTacticsChanges(Integer CB_numRedTacticsChanges) {
        this.CB_numRedTacticsChanges = CB_numRedTacticsChanges;
    }

    public List<Boolean> getCB_redTacticsChangedDetected() {
        return CB_redTacticsChangedDetected;
    }

    public void setCB_redTacticsChangedDetected(List<Boolean> CB_redTacticsChangedDetected) {
        this.CB_redTacticsChangedDetected = CB_redTacticsChangedDetected;
    }

    public Double getCB_trialsNeededToDetectRedTacticChanges_avg() {
        return CB_trialsNeededToDetectRedTacticChanges_avg;
    }

    public void setCB_trialsNeededToDetectRedTacticChanges_avg(Double CB_trialsNeededToDetectRedTacticChanges_avg) {
        this.CB_trialsNeededToDetectRedTacticChanges_avg = CB_trialsNeededToDetectRedTacticChanges_avg;
    }

    public Double getCB_trialsNeededToDetectRedTacticChanges_std() {
        return CB_trialsNeededToDetectRedTacticChanges_std;
    }

    public void setCB_trialsNeededToDetectRedTacticChanges_std(Double CB_trialsNeededToDetectRedTacticChanges_std) {
        this.CB_trialsNeededToDetectRedTacticChanges_std = CB_trialsNeededToDetectRedTacticChanges_std;
    }

    /*public List<Double> getCB_trialsNeededToDetectRedTacticChanges() {
        return CB_trialsNeededToDetectRedTacticChanges;
    }

    public void setCB_trialsNeededToDetectRedTacticChanges(List<Double> CB_trialsNeededToDetectRedTacticChanges) {
        this.CB_trialsNeededToDetectRedTacticChanges = CB_trialsNeededToDetectRedTacticChanges;
    }

    public List<Double> getCB_trialsNeededToDetectRedTacticChanges_std() {
        return CB_trialsNeededToDetectRedTacticChanges_std;
    }

    public void setCB_trialsNeededToDetectRedTacticChanges_std(List<Double> CB_trialsNeededToDetectRedTacticChanges_std) {
        this.CB_trialsNeededToDetectRedTacticChanges_std = CB_trialsNeededToDetectRedTacticChanges_std;
    }*/

    /*public Double getCB_msr_score() {
    return CB_msr_score;
    }
    public void setCB_msr_score(Double CB_msr_score) {
    this.CB_msr_score = CB_msr_score;
    } */
    
    public CFAMetric getCB_metrics() {
        return CB_metrics;
    }

    public void setCB_metrics(CFAMetric CB_metrics) {
        this.CB_metrics = CB_metrics;
    }

    public Double getSS_percentTrialsReviewedInBatchPlot_avg() {
        return SS_percentTrialsReviewedInBatchPlot_avg;
    }

    public void setSS_percentTrialsReviewedInBatchPlot_avg(Double SS_percentTrialsReviewedInBatchPlot_avg) {
        this.SS_percentTrialsReviewedInBatchPlot_avg = SS_percentTrialsReviewedInBatchPlot_avg;
    }

    public Double getSS_percentTrialsReviewedInBatchPlot_avg_std() {
        return SS_percentTrialsReviewedInBatchPlot_avg_std;
    }

    public void setSS_percentTrialsReviewedInBatchPlot_avg_std(Double SS_percentTrialsReviewedInBatchPlot_avg_std) {
        this.SS_percentTrialsReviewedInBatchPlot_avg_std = SS_percentTrialsReviewedInBatchPlot_avg_std;
    }
    
    /*public Double getSS_msr_score() {
    return SS_msr_score;
    }
    public void setSS_msr_score(Double SS_msr_score) {
    this.SS_msr_score = SS_msr_score;
    }*/
    
    public CFAMetric getSS_metrics() {
        return SS_metrics;
    }

    public void setSS_metrics(CFAMetric SS_metrics) {
        this.SS_metrics = SS_metrics;
    }    

    public RSRAndASRMissionMetrics getRSR_ASR() {
        return RSR_ASR;
    }

    public void setRSR_ASR(RSRAndASRMissionMetrics RSR_ASR) {
        this.RSR_ASR = RSR_ASR;
    }

    public Double getRMR_sigint_avg() {
        return RMR_sigint_avg;
    }

    public void setRMR_sigint_avg(Double RMR_sigint_avg) {
        this.RMR_sigint_avg = RMR_sigint_avg;
    }

    public Double getRMR_sigint_std() {
        return RMR_sigint_std;
    }

    public void setRMR_sigint_std(Double RMR_sigint_std) {
        this.RMR_sigint_std = RMR_sigint_std;
    }

    public Double getRMR_blueAction_avg() {
        return RMR_blueAction_avg;
    }

    public void setRMR_blueAction_avg(Double RMR_blueAction_avg) {
        this.RMR_blueAction_avg = RMR_blueAction_avg;
    }

    public Double getRMR_blueAction_std() {
        return RMR_blueAction_std;
    }

    public void setRMR_blueAction_std(Double RMR_blueAction_std) {
        this.RMR_blueAction_std = RMR_blueAction_std;
    }

    public Double getRMR_avg() {
        return RMR_avg;
    }

    public void setRMR_avg(Double RMR_avg) {
        this.RMR_avg = RMR_avg;
    }

    public Double getRMR_std() {
        return RMR_std;
    }

    public void setRMR_std(Double RMR_std) {
        this.RMR_std = RMR_std;
    }

    public SubjectSigintProbabilities getSigintProbs_avg() {
        return sigintProbs_avg;
    }

    public void setSigintProbs_avg(SubjectSigintProbabilities sigintProbs_avg) {
        this.sigintProbs_avg = sigintProbs_avg;
    }

    @Override
    @XmlElement
    public void setTrials(List<TrialData> trials) {
        super.setTrials(trials);
    }

    @Override
    public List<TrialData> getTrials() {
        return super.getTrials();
    }
    
    
}