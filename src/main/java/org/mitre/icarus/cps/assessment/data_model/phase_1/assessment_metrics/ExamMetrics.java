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
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlElement;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.mitre.icarus.cps.assessment.data_model.base.DataType;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.AbstractExamMetrics;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.OverallCFACPAMetric;
import org.mitre.icarus.cps.assessment.data_model.phase_1.ExamData;
import org.mitre.icarus.cps.exam.base.response.ResponseGeneratorData;
import org.mitre.icarus.cps.exam.phase_1.IcarusExam_Phase1;
import org.mitre.icarus.cps.exam.phase_1.testing.TaskTestPhase;

/**
 * Contains Phase 1 assessment metrics for an entire exam.
 *
 * @author CBONACETO
 *
 */
@Entity
public class ExamMetrics extends AbstractExamMetrics<TaskMetrics> implements Serializable {

    private static final long serialVersionUID = 1L;

    protected Boolean data_stale;
    protected Boolean RR_stale;
    protected Boolean AI_stale;
    protected Boolean CW_stale;
    protected Boolean PM_F_stale;
    protected Boolean PM_RMS_stale;
    protected Boolean CS_stale;
    protected Boolean RSR_stale;
    protected Boolean ASR_stale;
    protected Boolean RMR_stale;

    protected OverallCFACPAMetric RR;
    protected OverallCFACPAMetric AI;
    protected OverallCFACPAMetric CW;
    protected OverallCFACPAMetric PM_F;
    protected OverallCFACPAMetric PM_RMS;
    protected OverallCFACPAMetric PM;
    protected OverallCFACPAMetric CS;

    protected Double CFA_credits_earned;
    protected Double CFA_credits_possible;
    protected Double CFA_score;
    protected Boolean CFA_pass;

    protected Double Spm_avg;
    protected Double Spm_std; //TODO: New var added, need to calculate
    protected Double Spr_avg;
    protected Double Spr_std; //TODO: New var added, need to calculate
    protected Double Spq_avg;
    protected Double Spq_std; //TODO: New var added, need to calculate

    protected OverallCFACPAMetric RSR;
    protected OverallCFACPAMetric RSR_Bayesian;

    protected OverallCFACPAMetric RSR_Spm_Spr_avg;
    protected OverallCFACPAMetric RSR_Spm_Spq_avg;

    protected OverallCFACPAMetric RSR_alt_1;
    protected OverallCFACPAMetric RSR_alt_2;

    protected OverallCFACPAMetric ASR;

    protected OverallCFACPAMetric RMR;

    protected Double CPA_score;
    protected Boolean CPA_pass;

    public ExamMetrics() {
    }

    /**
     * @param exam
     * @param responseGenerator
     * @param data_type
     */
    public ExamMetrics(IcarusExam_Phase1 exam, ResponseGeneratorData responseGenerator, DataType data_type) {
        super(exam != null ? exam.getId() : null, responseGenerator);
        if (exam != null && exam.getTasks() != null && !exam.getTasks().isEmpty()) {
            tasks = new ArrayList<TaskMetrics>(exam.getTasks().size());
            for (TaskTestPhase<?> task : exam.getTasks()) {
                tasks.add(new TaskMetrics(task, responseGenerator, data_type,
                        exam.getId(), ExamData.getTask_number(task)));
            }
        }
    }    

    public Boolean isData_stale() {
        return data_stale;
    }

    public void setData_stale(Boolean data_stale) {
        this.data_stale = data_stale;
    }

    public Boolean isRR_stale() {
        return RR_stale;
    }

    public void setRR_stale(Boolean rR_stale) {
        RR_stale = rR_stale;
    }

    public Boolean isAI_stale() {
        return AI_stale;
    }

    public void setAI_stale(Boolean aI_stale) {
        AI_stale = aI_stale;
    }

    public Boolean isCW_stale() {
        return CW_stale;
    }

    public void setCW_stale(Boolean cW_stale) {
        CW_stale = cW_stale;
    }

    public Boolean isPM_F_stale() {
        return PM_F_stale;
    }

    public void setPM_F_stale(Boolean pM_F_stale) {
        PM_F_stale = pM_F_stale;
    }

    public Boolean isPM_RMS_stale() {
        return PM_RMS_stale;
    }

    public void setPM_RMS_stale(Boolean pM_RMS_stale) {
        PM_RMS_stale = pM_RMS_stale;
    }

    public Boolean isCS_stale() {
        return CS_stale;
    }

    public void setCS_stale(Boolean cS_stale) {
        CS_stale = cS_stale;
    }

    public Boolean isRSR_stale() {
        return RSR_stale;
    }

    public void setRSR_stale(Boolean rSR_stale) {
        RSR_stale = rSR_stale;
    }

    public Boolean isASR_stale() {
        return ASR_stale;
    }

    public void setASR_stale(Boolean aSR_stale) {
        ASR_stale = aSR_stale;
    }

    public Boolean isRMR_stale() {
        return RMR_stale;
    }

    public void setRMR_stale(Boolean rMR_stale) {
        RMR_stale = rMR_stale;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    public OverallCFACPAMetric getRR() {
        return RR;
    }

    public void setRR(OverallCFACPAMetric rR) {
        RR = rR;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    public OverallCFACPAMetric getAI() {
        return AI;
    }

    public void setAI(OverallCFACPAMetric aI) {
        AI = aI;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    public OverallCFACPAMetric getCW() {
        return CW;
    }

    public void setCW(OverallCFACPAMetric cW) {
        CW = cW;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    public OverallCFACPAMetric getPM_F() {
        return PM_F;
    }

    public void setPM_F(OverallCFACPAMetric pM_F) {
        PM_F = pM_F;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    public OverallCFACPAMetric getPM_RMS() {
        return PM_RMS;
    }

    public void setPM_RMS(OverallCFACPAMetric pM_RMS) {
        PM_RMS = pM_RMS;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    public OverallCFACPAMetric getPM() {
        return PM;
    }

    public void setPM(OverallCFACPAMetric pM) {
        PM = pM;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    public OverallCFACPAMetric getCS() {
        return CS;
    }

    public void setCS(OverallCFACPAMetric cS) {
        CS = cS;
    }

    public Double getCFA_credits_earned() {
        return CFA_credits_earned;
    }

    public void setCFA_credits_earned(Double cFA_credits_earned) {
        CFA_credits_earned = cFA_credits_earned;
    }

    public Double getCFA_credits_possible() {
        return CFA_credits_possible;
    }

    public void setCFA_credits_possible(Double cFA_credits_possible) {
        CFA_credits_possible = cFA_credits_possible;
    }

    public Double getCFA_score() {
        return CFA_score;
    }

    public void setCFA_score(Double cFA_score) {
        CFA_score = cFA_score;
    }

    public Boolean isCFA_pass() {
        return CFA_pass;
    }

    public void setCFA_pass(Boolean cFA_pass) {
        CFA_pass = cFA_pass;
    }

    public Double getSpm_avg() {
        return Spm_avg;
    }

    public void setSpm_avg(Double spm_avg) {
        Spm_avg = spm_avg;
    }

    public Double getSpm_std() {
        return Spm_std;
    }

    public void setSpm_std(Double spm_std) {
        Spm_std = spm_std;
    }

    public Double getSpr_avg() {
        return Spr_avg;
    }

    public void setSpr_avg(Double spr_avg) {
        Spr_avg = spr_avg;
    }

    public Double getSpr_std() {
        return Spr_std;
    }

    public void setSpr_std(Double spr_std) {
        Spr_std = spr_std;
    }

    public Double getSpq_avg() {
        return Spq_avg;
    }

    public void setSpq_avg(Double spq_avg) {
        Spq_avg = spq_avg;
    }

    public Double getSpq_std() {
        return Spq_std;
    }

    public void setSpq_std(Double spq_std) {
        Spq_std = spq_std;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    public OverallCFACPAMetric getRSR() {
        return RSR;
    }

    public void setRSR(OverallCFACPAMetric rSR) {
        RSR = rSR;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    public OverallCFACPAMetric getRSR_Bayesian() {
        return RSR_Bayesian;
    }

    public void setRSR_Bayesian(OverallCFACPAMetric rSR_Bayesian) {
        RSR_Bayesian = rSR_Bayesian;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    public OverallCFACPAMetric getRSR_Spm_Spr_avg() {
        return RSR_Spm_Spr_avg;
    }

    public void setRSR_Spm_Spr_avg(OverallCFACPAMetric rSR_Spm_Spr_avg) {
        RSR_Spm_Spr_avg = rSR_Spm_Spr_avg;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    public OverallCFACPAMetric getRSR_Spm_Spq_avg() {
        return RSR_Spm_Spq_avg;
    }

    public void setRSR_Spm_Spq_avg(OverallCFACPAMetric rSR_Spm_Spq_avg) {
        RSR_Spm_Spq_avg = rSR_Spm_Spq_avg;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    public OverallCFACPAMetric getRSR_alt_1() {
        return RSR_alt_1;
    }

    public void setRSR_alt_1(OverallCFACPAMetric rSR_alt_1) {
        RSR_alt_1 = rSR_alt_1;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    public OverallCFACPAMetric getRSR_alt_2() {
        return RSR_alt_2;
    }

    public void setRSR_alt_2(OverallCFACPAMetric rSR_alt_2) {
        RSR_alt_2 = rSR_alt_2;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    public OverallCFACPAMetric getASR() {
        return ASR;
    }

    public void setASR(OverallCFACPAMetric aSR) {
        ASR = aSR;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    public OverallCFACPAMetric getRMR() {
        return RMR;
    }

    public void setRMR(OverallCFACPAMetric rMR) {
        RMR = rMR;
    }

    public Double getCPA_score() {
        return CPA_score;
    }

    public void setCPA_score(Double cPA_score) {
        CPA_score = cPA_score;
    }

    public Boolean isCPA_pass() {
        return CPA_pass;
    }

    public void setCPA_pass(Boolean cPA_pass) {
        CPA_pass = cPA_pass;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "examMetricsTaskMetrics")
    @Fetch(value = FetchMode.SUBSELECT)
    @XmlElement
    @Override
    public List<TaskMetrics> getTasks() {
        return tasks;
    }

    @Override
    public void setTasks(List<TaskMetrics> tasks) {
        this.tasks = tasks;
    }
}