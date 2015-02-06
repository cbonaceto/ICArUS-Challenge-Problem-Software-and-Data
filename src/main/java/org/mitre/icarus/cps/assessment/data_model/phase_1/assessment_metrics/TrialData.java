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
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.AbstractTrialData;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.Probabilities;
import org.mitre.icarus.cps.exam.base.response.ResponseGeneratorData;
import org.mitre.icarus.cps.exam.phase_1.testing.IcarusTestTrial_Phase1;

/**
 * Contains response data for a trial in a task in a Phase 1 exam.
 *
 * @author CBONACETO
 *
 */
@Entity
public class TrialData extends AbstractTrialData<TrialMetrics> implements Serializable {

    private static final long serialVersionUID = 8771832060595736075L;

    protected Double s1_score;
    protected Double s2_score;
    protected Double credits;

    protected Boolean data_stale;
    protected Boolean cfa_metrics_stale;
    protected Boolean cpa_metrics_stale;

    protected List<Double> normative_base_rate;
    protected List<Double> probs_time;

    /**
     * The normalized participant probabilities (single human, average human,
     * model) probabilities in percent format
     */
    protected List<Probabilities> probs;
    protected List<Probabilities> normative_probs;

    protected List<String> layer_type;
    protected Integer ls_index;
    protected Integer normative_ls_index;

    protected Double circles_centers_time;
    protected List<Double> center_x;
    protected List<Double> center_x_std;
    protected List<Double> center_y;
    protected List<Double> center_y_std;
    protected List<Double> normative_center_x;
    protected List<Double> normative_center_y;
    protected List<Double> circle_r;
    protected List<Double> sigma;
    protected List<Double> sigma_std;
    protected List<Double> normative_sigma;

    protected Double allocation_time;
    protected List<Double> allocation;

    protected Double surprise_time;
    protected Double surprise;
    protected Double surprise_std;

    protected String ground_truth;

    /**
     * Default constructor required for querying
     */
    public TrialData() {
    }

    /**
     *
     * @param trial
     * @param responseGenerator
     * @param data_type
     * @param exam_id
     * @param task_id
     * @param task_number
     */
    public TrialData(IcarusTestTrial_Phase1 trial, ResponseGeneratorData responseGenerator, DataType data_type,
            String exam_id, String task_id, Integer task_number) {
        super(responseGenerator, data_type, exam_id, task_id, task_number);
        if (trial != null) {
            trial_number = trial.getTrialNum();
        }
    }

    /**
     * Constructor specifying fields needed to compute primary key hash code.
     */
    public TrialData(String site_id, String response_generator_id,
            String exam_id, String task_id, Integer trial_number) {
        super(site_id, response_generator_id, exam_id, task_id, trial_number);
    }

    public Double getS1_score() {
        return s1_score;
    }

    public void setS1_score(Double s1_score) {
        this.s1_score = s1_score;
    }

    public Double getS2_score() {
        return s2_score;
    }

    public void setS2_score(Double s2_score) {
        this.s2_score = s2_score;
    }

    public Double getCredits() {
        return credits;
    }

    public void setCredits(Double credits) {
        this.credits = credits;
    }

    public Boolean isData_stale() {
        return data_stale;
    }

    public void setData_stale(Boolean data_stale) {
        this.data_stale = data_stale;
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

    @ElementCollection
    public List<Double> getNormative_base_rate() {
        return normative_base_rate;
    }

    public void setNormative_base_rate(List<Double> normative_base_rate) {
        this.normative_base_rate = normative_base_rate;
    }

    @ElementCollection
    public List<Double> getProbs_time() {
        return probs_time;
    }

    public void setProbs_time(List<Double> probs_time) {
        this.probs_time = probs_time;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "trialDataProbs")
    @Fetch(value = FetchMode.SUBSELECT)
    public List<Probabilities> getProbs() {
        return probs;
    }

    public void setProbs(List<Probabilities> probs) {
        this.probs = probs;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "trialDataNormativeProbs")
    @Fetch(value = FetchMode.SUBSELECT)
    public List<Probabilities> getNormative_probs() {
        return normative_probs;
    }

    public void setNormative_probs(List<Probabilities> normative_probs) {
        this.normative_probs = normative_probs;
    }

    @ElementCollection
    public List<String> getLayer_type() {
        return layer_type;
    }

    public void setLayer_type(List<String> layer_type) {
        this.layer_type = layer_type;
    }

    public Integer getLs_index() {
        return ls_index;
    }

    public void setLs_index(Integer ls_index) {
        this.ls_index = ls_index;
    }

    public Integer getNormative_ls_index() {
        return normative_ls_index;
    }

    public void setNormative_ls_index(Integer normative_ls_index) {
        this.normative_ls_index = normative_ls_index;
    }

    public Double getCircles_centers_time() {
        return circles_centers_time;
    }

    public void setCircles_centers_time(Double circles_centers_time) {
        this.circles_centers_time = circles_centers_time;
    }

    @ElementCollection
    public List<Double> getCenter_x() {
        return center_x;
    }

    public void setCenter_x(List<Double> center_x) {
        this.center_x = center_x;
    }

    @ElementCollection
    public List<Double> getCenter_x_std() {
        return center_x_std;
    }

    public void setCenter_x_std(List<Double> center_x_std) {
        this.center_x_std = center_x_std;
    }

    @ElementCollection
    public List<Double> getCenter_y() {
        return center_y;
    }

    public void setCenter_y(List<Double> center_y) {
        this.center_y = center_y;
    }

    @ElementCollection
    public List<Double> getCenter_y_std() {
        return center_y_std;
    }

    public void setCenter_y_std(List<Double> center_y_std) {
        this.center_y_std = center_y_std;
    }

    @ElementCollection
    public List<Double> getNormative_center_x() {
        return normative_center_x;
    }

    public void setNormative_center_x(List<Double> normative_center_x) {
        this.normative_center_x = normative_center_x;
    }

    @ElementCollection
    public List<Double> getNormative_center_y() {
        return normative_center_y;
    }

    public void setNormative_center_y(List<Double> normative_center_y) {
        this.normative_center_y = normative_center_y;
    }

    @ElementCollection
    public List<Double> getCircle_r() {
        return circle_r;
    }

    public void setCircle_r(List<Double> circle_r) {
        this.circle_r = circle_r;
    }

    @ElementCollection
    public List<Double> getSigma() {
        return sigma;
    }

    public void setSigma(List<Double> sigma) {
        this.sigma = sigma;
    }

    @ElementCollection
    public List<Double> getSigma_std() {
        return sigma_std;
    }

    public void setSigma_std(List<Double> sigma_std) {
        this.sigma_std = sigma_std;
    }

    @ElementCollection
    public List<Double> getNormative_sigma() {
        return normative_sigma;
    }

    public void setNormative_sigma(List<Double> normative_sigma) {
        this.normative_sigma = normative_sigma;
    }

    public Double getAllocation_time() {
        return allocation_time;
    }

    public void setAllocation_time(Double allocation_time) {
        this.allocation_time = allocation_time;
    }

    @ElementCollection
    public List<Double> getAllocation() {
        return allocation;
    }

    public void setAllocation(List<Double> allocation) {
        this.allocation = allocation;
    }

    public Double getSurprise_time() {
        return surprise_time;
    }

    public void setSurprise_time(Double surprise_time) {
        this.surprise_time = surprise_time;
    }

    public Double getSurprise() {
        return surprise;
    }

    public void setSurprise(Double surprise) {
        this.surprise = surprise;
    }

    public Double getSurprise_std() {
        return surprise_std;
    }

    public void setSurprise_std(Double surprise_std) {
        this.surprise_std = surprise_std;
    }

    public String getGround_truth() {
        return ground_truth;
    }

    public void setGround_truth(String ground_truth) {
        this.ground_truth = ground_truth;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @XmlElement
    @Override
    public TrialMetrics getMetrics() {
        return metrics;
    }

    @Override
    public void setMetrics(TrialMetrics metrics) {
        this.metrics = metrics;
    }
}
