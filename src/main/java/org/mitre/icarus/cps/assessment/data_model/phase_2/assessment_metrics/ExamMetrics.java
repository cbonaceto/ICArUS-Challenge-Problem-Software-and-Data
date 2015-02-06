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
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.AbstractExamMetrics;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.OverallCFACPAMetric;
import org.mitre.icarus.cps.assessment.data_model.phase_2.ExamData;
import org.mitre.icarus.cps.exam.base.response.ResponseGeneratorData;
import org.mitre.icarus.cps.exam.phase_2.IcarusExam_Phase2;
import org.mitre.icarus.cps.exam.phase_2.testing.Mission;

/**
 * Contains Phase 2 assessment metrics for an entire exam.
 * 
 * @author CBONACETO
 */
public class ExamMetrics extends AbstractExamMetrics<MissionMetrics> implements Serializable {

    /** Whether all metrics are stale and need to be re-calculated */
    protected Boolean data_stale;
    
    /** Whether Anchoring & Adjustment metrics are stale */
    protected Boolean AA_stale;
    
    /** Whether Persistence of Discredited Evidence metrics are stale */
    protected Boolean PDE_stale;
    
    /** Whether Representativeness metrics are stale */
    protected Boolean RR_stale;
    
    /** Whether Availability metrics are stale */
    protected Boolean AV_stale;
    
    /** Whether Probability Matching metrics are stale */
    protected Boolean PM_stale;
    
    /** Whether Confirmation Bias metrics are stale */
    protected Boolean CS_stale;
    
    /** Whether Change Blindness metrics are stale */
    protected Boolean CB_stale;
    
    /** Whether Satisfaction of Search metrics are stale */
    protected Boolean SS_stale;
   
     /** Whether ASR metrics are stale and need to be re-calculated */
    protected Boolean ASR_stale;
    
    /** Whether any RSR-related metrics are stale and need to be re-calculated */
    protected Boolean RSR_stale;
    
    /** Whether RMR metrics are stale and need to be re-calculated */
    protected Boolean RMR_stale;
    
    
    /***** CFA metrics *****/    
    /** Anchoring and Adjustment overall metrics for the exam */
    protected OverallCFACPAMetric AA;
    
    /** Persistence of Discredited Evidences overall metrics for the exam */
    protected OverallCFACPAMetric PDE;
    
    /** Representativeness overall metrics for the exam */
    protected OverallCFACPAMetric RR;
    
    /** Availability overall metrics for the exam */
    protected OverallCFACPAMetric AV;
    
    /** Probability Matching overall metrics for the exam */
    protected OverallCFACPAMetric PM;
    
    /** Confirmation Bias overall metrics for the exam */
    protected OverallCFACPAMetric CS;
    
    /** Change blindness overall metrics for the exam */
    protected OverallCFACPAMetric CB;
    
    /** Satisfaction of Search overall metrics for the exam */
    protected OverallCFACPAMetric SS;

    /** The number of CFA credits earned */
    protected Double CFA_credits_earned;
    
    /** The total number of possible CFA credits */
    protected Double CFA_credits_possible;
    
    /** The CFA score */
    protected Double CFA_score;
    
    /** Whether the model passed CFA */
    protected Boolean CFA_pass;    
    /***** End CFA metrics *****/

    
    /***** CPA metrics *****/    
    /** ASR overall metrics for the exam */
    protected OverallCFACPAMetric ASR;
    
    /** Average Spm, Spr, and Spq for the exam */
    protected Double Spm_avg;
    protected Double Spm_std;
    protected Double Spr_avg;
    protected Double Spr_std;
    protected Double Spq_avg;
    protected Double Spq_std;    

    /** RSR overall metrics for the exam */
    protected OverallCFACPAMetric RSR;
    
    /** RSR-Bayesian overall metrics for the exam */
    protected OverallCFACPAMetric RSR_Bayesian;

    /** RSR based on the average Spm and Spr overall metrics for the exam*/
    protected OverallCFACPAMetric RSR_Spm_Spr_avg;
    
    /** RSR based on the average Spm and Spq overall metrics for the exam */
    protected OverallCFACPAMetric RSR_Spm_Spq_avg;    
    
    /** RSR alternative 1 [RSR(RMSE)] overall metrics for the exam */
    protected OverallCFACPAMetric RSR_alt_1;
    
    /** RSR alternative 2 [RSR(RMSE)-Bayesian] overall metrics for the exam */
    protected OverallCFACPAMetric RSR_alt_2;    

    /** RMR overall metrics for the exam */
    protected OverallCFACPAMetric RMR;

    /** The CPA score */
    protected Double CPA_score;
    
    /** Whether the model passed CPA */
    protected Boolean CPA_pass;
    /***** End CPA metrics *****/
    
    public ExamMetrics() { }
    
    /**
     * @param exam
     * @param responseGenerator
     * @param data_type
     */
    public ExamMetrics(IcarusExam_Phase2 exam, ResponseGeneratorData responseGenerator, DataType data_type) {
        super(exam != null ? exam.getId() : null, responseGenerator);
        if (exam != null && exam.getMissions() != null && !exam.getMissions().isEmpty()) {
            tasks = new ArrayList<MissionMetrics>(exam.getMissions().size());
            for (Mission<?> mission : exam.getMissions()) {
                tasks.add(new MissionMetrics(mission, responseGenerator, data_type,
                        exam.getId(), ExamData.getTask_number(mission)));
            }
        }
    }

    public Boolean isData_stale() {
        return data_stale;
    }

    public void setData_stale(Boolean data_stale) {
        this.data_stale = data_stale;
    }

    public Boolean isAA_stale() {
        return AA_stale;
    }

    public void setAA_stale(Boolean AA_stale) {
        this.AA_stale = AA_stale;
    }

    public Boolean isPDE_stale() {
        return PDE_stale;
    }

    public void setPDE_stale(Boolean PDE_stale) {
        this.PDE_stale = PDE_stale;
    }

    public Boolean isRR_stale() {
        return RR_stale;
    }

    public void setRR_stale(Boolean RR_stale) {
        this.RR_stale = RR_stale;
    }

    public Boolean isAV_stale() {
        return AV_stale;
    }

    public void setAV_stale(Boolean AV_stale) {
        this.AV_stale = AV_stale;
    }

    public Boolean isPM_stale() {
        return PM_stale;
    }

    public void setPM_stale(Boolean PM_stale) {
        this.PM_stale = PM_stale;
    }

    public Boolean isCS_stale() {
        return CS_stale;
    }

    public void setCS_stale(Boolean CS_stale) {
        this.CS_stale = CS_stale;
    }

    public Boolean isCB_stale() {
        return CB_stale;
    }

    public void setCB_stale(Boolean CB_stale) {
        this.CB_stale = CB_stale;
    }

    public Boolean isSS_stale() {
        return SS_stale;
    }

    public void setSS_stale(Boolean SS_stale) {
        this.SS_stale = SS_stale;
    }
    
    public Boolean isASR_stale() {
        return ASR_stale;
    }

    public void setASR_stale(Boolean ASR_stale) {
        this.ASR_stale = ASR_stale;
    }

    public Boolean isRSR_stale() {
        return RSR_stale;
    }

    public void setRSR_stale(Boolean RSR_stale) {
        this.RSR_stale = RSR_stale;
    }
    

    public Boolean isRMR_stale() {
        return RMR_stale;
    }

    public void setRMR_stale(Boolean RMR_stale) {
        this.RMR_stale = RMR_stale;
    }

    public OverallCFACPAMetric getAA() {
        return AA;
    }

    public void setAA(OverallCFACPAMetric AA) {
        this.AA = AA;
    }

    public OverallCFACPAMetric getPDE() {
        return PDE;
    }

    public void setPDE(OverallCFACPAMetric PDE) {
        this.PDE = PDE;
    }

    public OverallCFACPAMetric getRR() {
        return RR;
    }

    public void setRR(OverallCFACPAMetric RR) {
        this.RR = RR;
    }

    public OverallCFACPAMetric getAV() {
        return AV;
    }

    public void setAV(OverallCFACPAMetric AV) {
        this.AV = AV;
    }

    public OverallCFACPAMetric getPM() {
        return PM;
    }

    public void setPM(OverallCFACPAMetric PM) {
        this.PM = PM;
    }

    public OverallCFACPAMetric getCS() {
        return CS;
    }

    public void setCS(OverallCFACPAMetric CS) {
        this.CS = CS;
    }

    public OverallCFACPAMetric getCB() {
        return CB;
    }

    public void setCB(OverallCFACPAMetric CB) {
        this.CB = CB;
    }

    public OverallCFACPAMetric getSS() {
        return SS;
    }

    public void setSS(OverallCFACPAMetric SS) {
        this.SS = SS;
    }

    public Double getCFA_credits_earned() {
        return CFA_credits_earned;
    }

    public void setCFA_credits_earned(Double CFA_credits_earned) {
        this.CFA_credits_earned = CFA_credits_earned;
    }

    public Double getCFA_credits_possible() {
        return CFA_credits_possible;
    }

    public void setCFA_credits_possible(Double CFA_credits_possible) {
        this.CFA_credits_possible = CFA_credits_possible;
    }

    public Double getCFA_score() {
        return CFA_score;
    }

    public void setCFA_score(Double CFA_score) {
        this.CFA_score = CFA_score;
    }

    public Boolean isCFA_pass() {
        return CFA_pass;
    }

    public void setCFA_pass(Boolean CFA_pass) {
        this.CFA_pass = CFA_pass;
    }
    
    public OverallCFACPAMetric getASR() {
        return ASR;
    }

    public void setASR(OverallCFACPAMetric ASR) {
        this.ASR = ASR;
    }

    public Double getSpm_avg() {
        return Spm_avg;
    }

    public void setSpm_avg(Double Spm_avg) {
        this.Spm_avg = Spm_avg;
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

    public Double getSpq_std() {
        return Spq_std;
    }

    public void setSpq_std(Double Spq_std) {
        this.Spq_std = Spq_std;
    }

    public OverallCFACPAMetric getRSR() {
        return RSR;
    }

    public void setRSR(OverallCFACPAMetric RSR) {
        this.RSR = RSR;
    }

    public OverallCFACPAMetric getRSR_Bayesian() {
        return RSR_Bayesian;
    }

    public void setRSR_Bayesian(OverallCFACPAMetric RSR_Bayesian) {
        this.RSR_Bayesian = RSR_Bayesian;
    }

    public OverallCFACPAMetric getRSR_Spm_Spr_avg() {
        return RSR_Spm_Spr_avg;
    }

    public void setRSR_Spm_Spr_avg(OverallCFACPAMetric RSR_Spm_Spr_avg) {
        this.RSR_Spm_Spr_avg = RSR_Spm_Spr_avg;
    }

    public OverallCFACPAMetric getRSR_Spm_Spq_avg() {
        return RSR_Spm_Spq_avg;
    }

    public void setRSR_Spm_Spq_avg(OverallCFACPAMetric RSR_Spm_Spq_avg) {
        this.RSR_Spm_Spq_avg = RSR_Spm_Spq_avg;
    }

    public OverallCFACPAMetric getRSR_alt_1() {
        return RSR_alt_1;
    }

    public void setRSR_alt_1(OverallCFACPAMetric RSR_alt_1) {
        this.RSR_alt_1 = RSR_alt_1;
    }

    public OverallCFACPAMetric getRSR_alt_2() {
        return RSR_alt_2;
    }

    public void setRSR_alt_2(OverallCFACPAMetric RSR_alt_2) {
        this.RSR_alt_2 = RSR_alt_2;
    }   

    public OverallCFACPAMetric getRMR() {
        return RMR;
    }

    public void setRMR(OverallCFACPAMetric RMR) {
        this.RMR = RMR;
    }

    public Double getCPA_score() {
        return CPA_score;
    }

    public void setCPA_score(Double CPA_score) {
        this.CPA_score = CPA_score;
    }

    public Boolean isCPA_pass() {
        return CPA_pass;
    }

    public void setCPA_pass(Boolean CPA_pass) {
        this.CPA_pass = CPA_pass;
    }   

    @XmlElement
    @Override
    public void setTasks(List<MissionMetrics> tasks) {
        super.setTasks(tasks);
    }

    @Override
    public List<MissionMetrics> getTasks() {
        return super.getTasks();
    }
}