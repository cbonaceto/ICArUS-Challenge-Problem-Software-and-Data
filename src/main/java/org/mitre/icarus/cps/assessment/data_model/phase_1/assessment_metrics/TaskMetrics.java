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
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlElement;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.mitre.icarus.cps.assessment.data_model.base.DataType;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.AbstractTaskMetrics;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.CFAMetric;
import org.mitre.icarus.cps.exam.base.response.ResponseGeneratorData;
import org.mitre.icarus.cps.exam.phase_1.testing.TaskTestPhase;

/**
 * Contains Phase 1 assessment metrics for a task in a Phase 1 exam.
 *
 * @author CBONACETO
 *
 */
@Entity
public class TaskMetrics extends AbstractTaskMetrics<TrialData> implements Serializable {

    private static final long serialVersionUID = -4265579567417516214L;

    protected Double Fh_Ph_avg;
    protected Double Fh_Ph_std;
    protected Double Fh_1_avg;
    protected Double Fh_1_std;

    protected Double RMS_F_P_avg;
    protected Double RMS_F_P_std;
    protected Double RMS_F_I_avg;
    protected Double RMS_F_I_std;

    protected Integer sigint_selections_all_trials;
    protected Integer sigint_highest_group_selections_all_trials;
    protected Integer sigint_highest_best_selections_all_trials;
    protected Double C_all_trials;
    protected Double C_all_trials_std;
    protected Double C_highest_best_all_trials;
    protected Double C_highest_best_all_trials_std;
    protected Double C_trials_avg;
    protected Double C_trials_std;
    protected Double C_trials_highest_best_avg;
    protected Double C_trials_highest_best_std;

    protected Double RR_avg_score;
    protected Double AI_avg_score;
    protected Double PM_RMS_avg_score;
    protected CFAMetric PM_F;
    protected CFAMetric CS;

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

    protected Double ASR_avg;
    protected List<Double> ASR_stage_avg;
    protected Double ASR_std;

    protected Double RMR_avg;
    protected Double RMR_std;

    protected List<Integer> F_LS_all_trials_count;
    protected List<Double> F_LS_all_trials_percent;
    protected List<Double> F_LS_std;

    public TaskMetrics() {
    }

    /**
     * @param task
     * @param responseGenerator
     * @param data_type
     * @param exam_id
     * @param task_number
     * @param phase_number
     */
    public TaskMetrics(TaskTestPhase<?> task, ResponseGeneratorData responseGenerator, DataType data_type,
            String exam_id, Integer task_number) {
        super(responseGenerator, data_type, exam_id, task != null ? task.getId() : null, task_number);
        if (task != null) {
            if (task.getTestTrials() != null && !task.getTestTrials().isEmpty()) {
                trials = new ArrayList<TrialData>(task.getTestTrials().size());
                for (int i = 0; i < task.getTestTrials().size(); i++) {
                    //trials.add(new TrialData(trial, responseGenerator, data_type, exam_id, exam_id, phase_number));
                    trials.add(new TrialData());
                }
            }
        }
    }

    public Double getFh_Ph_avg() {
        return Fh_Ph_avg;
    }

    public void setFh_Ph_avg(Double fh_Ph_avg) {
        Fh_Ph_avg = fh_Ph_avg;
    }

    public Double getFh_Ph_std() {
        return Fh_Ph_std;
    }

    public void setFh_Ph_std(Double fh_Ph_std) {
        Fh_Ph_std = fh_Ph_std;
    }

    public Double getFh_1_avg() {
        return Fh_1_avg;
    }

    public void setFh_1_avg(Double fh_1_avg) {
        Fh_1_avg = fh_1_avg;
    }

    public Double getFh_1_std() {
        return Fh_1_std;
    }

    public void setFh_1_std(Double fh_1_std) {
        Fh_1_std = fh_1_std;
    }

    public Double getRMS_F_P_avg() {
        return RMS_F_P_avg;
    }

    public void setRMS_F_P_avg(Double rMS_F_P_avg) {
        RMS_F_P_avg = rMS_F_P_avg;
    }

    public Double getRMS_F_P_std() {
        return RMS_F_P_std;
    }

    public void setRMS_F_P_std(Double rMS_F_P_std) {
        RMS_F_P_std = rMS_F_P_std;
    }

    public Double getRMS_F_I_avg() {
        return RMS_F_I_avg;
    }

    public void setRMS_F_I_avg(Double rMS_F_I_avg) {
        RMS_F_I_avg = rMS_F_I_avg;
    }

    public Double getRMS_F_I_std() {
        return RMS_F_I_std;
    }

    public void setRMS_F_I_std(Double rMS_F_I_std) {
        RMS_F_I_std = rMS_F_I_std;
    }

    public Integer getSigint_selections_all_trials() {
        return sigint_selections_all_trials;
    }

    public void setSigint_selections_all_trials(Integer sigint_selections_all_trials) {
        this.sigint_selections_all_trials = sigint_selections_all_trials;
    }

    public Integer getSigint_highest_group_selections_all_trials() {
        return sigint_highest_group_selections_all_trials;
    }

    public void setSigint_highest_group_selections_all_trials(
            Integer sigint_highest_group_selections_all_trials) {
        this.sigint_highest_group_selections_all_trials = sigint_highest_group_selections_all_trials;
    }

    public Integer getSigint_highest_best_selections_all_trials() {
        return sigint_highest_best_selections_all_trials;
    }

    public void setSigint_highest_best_selections_all_trials(
            Integer sigint_highest_best_selections_all_trials) {
        this.sigint_highest_best_selections_all_trials = sigint_highest_best_selections_all_trials;
    }

    /*public Integer getC_num_subjects() {
     return C_num_subjects;
     }

     public void setC_num_subjects(Integer c_num_subjects) {
     C_num_subjects = c_num_subjects;
     }*/
    public Double getC_all_trials() {
        return C_all_trials;
    }

    public void setC_all_trials(Double c_all_trials) {
        C_all_trials = c_all_trials;
    }

    public Double getC_all_trials_std() {
        return C_all_trials_std;
    }

    public void setC_all_trials_std(Double c_all_trials_std) {
        C_all_trials_std = c_all_trials_std;
    }

    public Double getC_highest_best_all_trials() {
        return C_highest_best_all_trials;
    }

    public void setC_highest_best_all_trials(Double c_highest_best_all_trials) {
        C_highest_best_all_trials = c_highest_best_all_trials;
    }

    public Double getC_highest_best_all_trials_std() {
        return C_highest_best_all_trials_std;
    }

    public void setC_highest_best_all_trials_std(
            Double c_highest_best_all_trials_std) {
        C_highest_best_all_trials_std = c_highest_best_all_trials_std;
    }

    public Double getC_trials_avg() {
        return C_trials_avg;
    }

    public void setC_trials_avg(Double c_trials_avg) {
        C_trials_avg = c_trials_avg;
    }

    public Double getC_trials_std() {
        return C_trials_std;
    }

    public void setC_trials_std(Double c_trials_std) {
        C_trials_std = c_trials_std;
    }

    public Double getC_trials_highest_best_avg() {
        return C_trials_highest_best_avg;
    }

    public void setC_trials_highest_best_avg(Double c_trials_highest_best_avg) {
        C_trials_highest_best_avg = c_trials_highest_best_avg;
    }

    public Double getC_trials_highest_best_std() {
        return C_trials_highest_best_std;
    }

    public void setC_trials_highest_best_std(Double c_trials_highest_best_std) {
        C_trials_highest_best_std = c_trials_highest_best_std;
    }

    public Double getRR_avg_score() {
        return RR_avg_score;
    }

    public void setRR_avg_score(Double rR_avg_score) {
        RR_avg_score = rR_avg_score;
    }

    public Double getAI_avg_score() {
        return AI_avg_score;
    }

    public void setAI_avg_score(Double aI_avg_score) {
        AI_avg_score = aI_avg_score;
    }

    public Double getPM_RMS_avg_score() {
        return PM_RMS_avg_score;
    }

    public void setPM_RMS_avg_score(Double pM_RMS_avg_score) {
        PM_RMS_avg_score = pM_RMS_avg_score;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    public CFAMetric getPM_F() {
        return PM_F;
    }

    public void setPM_F(CFAMetric pM_F) {
        PM_F = pM_F;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    public CFAMetric getCS() {
        return CS;
    }

    public void setCS(CFAMetric cS) {
        CS = cS;
    }

    public Double getSpm_avg() {
        return Spm_avg;
    }

    public void setSpm_avg(Double spm_avg) {
        Spm_avg = spm_avg;
    }

    @ElementCollection
    public List<Double> getSpm_stage_avg() {
        return Spm_stage_avg;
    }

    public void setSpm_stage_avg(List<Double> spm_stage_avg) {
        Spm_stage_avg = spm_stage_avg;
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

    @ElementCollection
    public List<Double> getSpr_stage_avg() {
        return Spr_stage_avg;
    }

    public void setSpr_stage_avg(List<Double> spr_stage_avg) {
        Spr_stage_avg = spr_stage_avg;
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

    @ElementCollection
    public List<Double> getSpq_stage_avg() {
        return Spq_stage_avg;
    }

    public void setSpq_stage_avg(List<Double> spq_stage_avg) {
        Spq_stage_avg = spq_stage_avg;
    }

    public Double getSpq_std() {
        return Spq_std;
    }

    public void setSpq_std(Double spq_std) {
        Spq_std = spq_std;
    }

    public Double getRSR_avg() {
        return RSR_avg;
    }

    public void setRSR_avg(Double rSR_avg) {
        RSR_avg = rSR_avg;
    }

    @ElementCollection
    public List<Double> getRSR_stage_avg() {
        return RSR_stage_avg;
    }

    public void setRSR_stage_avg(List<Double> rSR_stage_avg) {
        RSR_stage_avg = rSR_stage_avg;
    }

    public Double getRSR_std() {
        return RSR_std;
    }

    public void setRSR_std(Double rSR_std) {
        RSR_std = rSR_std;
    }

    public Double getRSR_Bayesian_avg() {
        return RSR_Bayesian_avg;
    }

    public void setRSR_Bayesian_avg(Double rSR_Bayesian_avg) {
        RSR_Bayesian_avg = rSR_Bayesian_avg;
    }

    @ElementCollection
    public List<Double> getRSR_Bayesian_stage_avg() {
        return RSR_Bayesian_stage_avg;
    }

    public void setRSR_Bayesian_stage_avg(List<Double> rSR_Bayesian_stage_avg) {
        RSR_Bayesian_stage_avg = rSR_Bayesian_stage_avg;
    }

    public Double getRSR_Bayesian_std() {
        return RSR_Bayesian_std;
    }

    public void setRSR_Bayesian_std(Double rSR_Bayesian_std) {
        RSR_Bayesian_std = rSR_Bayesian_std;
    }

    public Double getRSR_Spm_Spr_avg() {
        return RSR_Spm_Spr_avg;
    }

    public void setRSR_Spm_Spr_avg(Double rSR_Spm_Spr_avg) {
        RSR_Spm_Spr_avg = rSR_Spm_Spr_avg;
    }

    @ElementCollection
    public List<Double> getRSR_Spm_Spr_stage_avg() {
        return RSR_Spm_Spr_stage_avg;
    }

    public void setRSR_Spm_Spr_stage_avg(List<Double> rSR_Spm_Spr_stage_avg) {
        RSR_Spm_Spr_stage_avg = rSR_Spm_Spr_stage_avg;
    }

    public Double getRSR_Spm_Spr_std() {
        return RSR_Spm_Spr_std;
    }

    public void setRSR_Spm_Spr_std(Double rSR_Spm_Spr_std) {
        RSR_Spm_Spr_std = rSR_Spm_Spr_std;
    }

    public Double getRSR_Spm_Spq_avg() {
        return RSR_Spm_Spq_avg;
    }

    public void setRSR_Spm_Spq_avg(Double rSR_Spm_Spq_avg) {
        RSR_Spm_Spq_avg = rSR_Spm_Spq_avg;
    }

    @ElementCollection
    public List<Double> getRSR_Spm_Spq_stage_avg() {
        return RSR_Spm_Spq_stage_avg;
    }

    public void setRSR_Spm_Spq_stage_avg(List<Double> rSR_Spm_Spq_stage_avg) {
        RSR_Spm_Spq_stage_avg = rSR_Spm_Spq_stage_avg;
    }

    public Double getRSR_Spm_Spq_std() {
        return RSR_Spm_Spq_std;
    }

    public void setRSR_Spm_Spq_std(Double rSR_Spm_Spq_std) {
        RSR_Spm_Spq_std = rSR_Spm_Spq_std;
    }

    public Double getRSR_alt_1_avg() {
        return RSR_alt_1_avg;
    }

    public void setRSR_alt_1_avg(Double rSR_alt_1_avg) {
        RSR_alt_1_avg = rSR_alt_1_avg;
    }

    @ElementCollection
    public List<Double> getRSR_alt_1_stage_avg() {
        return RSR_alt_1_stage_avg;
    }

    public void setRSR_alt_1_stage_avg(List<Double> rSR_alt_1_stage_avg) {
        RSR_alt_1_stage_avg = rSR_alt_1_stage_avg;
    }

    public Double getRSR_alt_1_std() {
        return RSR_alt_1_std;
    }

    public void setRSR_alt_1_std(Double rSR_alt_1_std) {
        RSR_alt_1_std = rSR_alt_1_std;
    }

    public Double getRSR_alt_2_avg() {
        return RSR_alt_2_avg;
    }

    public void setRSR_alt_2_avg(Double rSR_alt_2_avg) {
        RSR_alt_2_avg = rSR_alt_2_avg;
    }

    @ElementCollection
    public List<Double> getRSR_alt_2_stage_avg() {
        return RSR_alt_2_stage_avg;
    }

    public void setRSR_alt_2_stage_avg(List<Double> rSR_alt_2_stage_avg) {
        RSR_alt_2_stage_avg = rSR_alt_2_stage_avg;
    }

    public Double getRSR_alt_2_std() {
        return RSR_alt_2_std;
    }

    public void setRSR_alt_2_std(Double rSR_alt_2_std) {
        RSR_alt_2_std = rSR_alt_2_std;
    }

    public Double getASR_avg() {
        return ASR_avg;
    }

    public void setASR_avg(Double aSR_avg) {
        ASR_avg = aSR_avg;
    }

    @ElementCollection
    public List<Double> getASR_stage_avg() {
        return ASR_stage_avg;
    }

    public void setASR_stage_avg(List<Double> aSR_stage_avg) {
        ASR_stage_avg = aSR_stage_avg;
    }

    public Double getASR_std() {
        return ASR_std;
    }

    public void setASR_std(Double aSR_std) {
        ASR_std = aSR_std;
    }

    public Double getRMR_avg() {
        return RMR_avg;
    }

    public void setRMR_avg(Double rMR_avg) {
        RMR_avg = rMR_avg;
    }

    public Double getRMR_std() {
        return RMR_std;
    }

    public void setRMR_std(Double rMR_std) {
        RMR_std = rMR_std;
    }

    @ElementCollection
    public List<Integer> getF_LS_all_trials_count() {
        return F_LS_all_trials_count;
    }

    public void setF_LS_all_trials_count(List<Integer> f_LS_all_trials_count) {
        F_LS_all_trials_count = f_LS_all_trials_count;
    }

    @ElementCollection
    public List<Double> getF_LS_all_trials_percent() {
        return F_LS_all_trials_percent;
    }

    public void setF_LS_all_trials_percent(List<Double> f_LS_all_trials_percent) {
        F_LS_all_trials_percent = f_LS_all_trials_percent;
    }

    @ElementCollection
    public List<Double> getF_LS_std() {
        return F_LS_std;
    }

    public void setF_LS_std(List<Double> f_LS_std) {
        F_LS_std = f_LS_std;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "taskMetricsTrialData")
    @Fetch(value = FetchMode.SUBSELECT)
    @XmlElement
    @Override
    public List<TrialData> getTrials() {
        return trials;
    }

    @Override
    public void setTrials(List<TrialData> trials) {
        this.trials = trials;
    }
}
