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

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.AbstractSubjectMetrics;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.CFAMetric;

/**
 * Phase 1 subject metrics. Subject metrics are metrics that span multiple (2 or
 * more) missions.
 *
 * @author cbonaceto
 *
 */
@Entity
public class SubjectMetrics extends AbstractSubjectMetrics implements Serializable {

    private static final long serialVersionUID = 935017420960136106L;

    protected Double Fh_all_trials;	//Fh over all trials for which PM_F is computed
    protected Double Ph_all_trials;	 //Ph over all trials for which PM_F is computed
    protected Double Fh_Ph_all_trials;
    protected Double Fh_Ph_std;
    protected Double Fh_1_all_trials;
    protected Double Fh_1_std;
    protected CFAMetric PM_F;

    protected Integer sigint_selections_all_trials;
    protected Integer sigint_highest_group_selections_all_trials;
    protected Integer sigint_highest_best_selections_all_trials;
    protected Integer C_num_subjects;
    protected Double C_all_trials;
    protected Double C_std;
    protected CFAMetric CS;

    public Double getFh_all_trials() {
        return Fh_all_trials;
    }

    public void setFh_all_trials(Double fh_all_trials) {
        Fh_all_trials = fh_all_trials;
    }

    public Double getPh_all_trials() {
        return Ph_all_trials;
    }

    public void setPh_all_trials(Double ph_all_trials) {
        Ph_all_trials = ph_all_trials;
    }

    public Double getFh_Ph_all_trials() {
        return Fh_Ph_all_trials;
    }

    public void setFh_Ph_all_trials(Double fh_Ph_all_trials) {
        Fh_Ph_all_trials = fh_Ph_all_trials;
    }

    public Double getFh_Ph_std() {
        return Fh_Ph_std;
    }

    public void setFh_Ph_std(Double fh_Ph_std) {
        Fh_Ph_std = fh_Ph_std;
    }

    public Double getFh_1_all_trials() {
        return Fh_1_all_trials;
    }

    public void setFh_1_all_trials(Double fh_1_all_trials) {
        Fh_1_all_trials = fh_1_all_trials;
    }

    public Double getFh_1_std() {
        return Fh_1_std;
    }

    public void setFh_1_std(Double fh_1_std) {
        Fh_1_std = fh_1_std;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    public CFAMetric getPM_F() {
        return PM_F;
    }

    public void setPM_F(CFAMetric pM_F) {
        PM_F = pM_F;
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

    public Integer getC_num_subjects() {
        return C_num_subjects;
    }

    public void setC_num_subjects(Integer c_num_subjects) {
        C_num_subjects = c_num_subjects;
    }

    public Double getC_all_trials() {
        return C_all_trials;
    }

    public void setC_all_trials(Double c_all_trials) {
        C_all_trials = c_all_trials;
    }

    public Double getC_std() {
        return C_std;
    }

    public void setC_std(Double c_std) {
        C_std = c_std;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    public CFAMetric getCS() {
        return CS;
    }

    public void setCS(CFAMetric cS) {
        CS = cS;
    }
}