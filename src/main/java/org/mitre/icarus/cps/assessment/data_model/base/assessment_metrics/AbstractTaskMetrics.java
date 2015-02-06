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
package org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlTransient;

import org.mitre.icarus.cps.assessment.data_model.base.DataType;
import org.mitre.icarus.cps.exam.base.response.ResponseGeneratorData;

/**
 *
 * @author CBONACETO
 *
 */
public abstract class AbstractTaskMetrics<D extends AbstractTrialData<?>> implements Serializable {

    private static final long serialVersionUID = -4265579567417516214L;

    private int id;

    protected String site_id;

    protected String response_generator_id;

    protected DataType data_type;

    protected Boolean human;

    protected String exam_id;

    protected String task_id;

    protected Integer task_number;

    protected Boolean task_complete;

    protected Boolean all_trials_valid;

    protected Double task_time;

    protected Integer num_subjects;

    protected Boolean metrics_stale;

    /**
     * The trials
     */
    protected List<D> trials;

    public AbstractTaskMetrics() {
    }
   
    /**
     *
     * @param responseGenerator
     * @param data_type
     * @param exam_id
     * @param task_id
     * @param task_number
     */
    public AbstractTaskMetrics(ResponseGeneratorData responseGenerator, DataType data_type,
            String exam_id, String task_id, Integer task_number) {
        if (responseGenerator != null) {
            site_id = responseGenerator.getSiteId();
            response_generator_id = responseGenerator.getResponseGeneratorId();
            human = responseGenerator.isHumanSubject();
        }
        this.data_type = data_type;
        this.exam_id = exam_id;
        this.task_id = task_id;
        this.task_number = task_number;
    }

    @Id
    @GeneratedValue
    @Column(name = "taskMetricsId")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSite_id() {
        return site_id;
    }

    public void setSite_id(String site_id) {
        this.site_id = site_id;
    }

    public String getResponse_generator_id() {
        return response_generator_id;
    }

    public void setResponse_generator_id(String response_generator_id) {
        this.response_generator_id = response_generator_id;
    }

    @Column(name = "taskMetricsDataType")
    public DataType getData_type() {
        return data_type;
    }

    public void setData_type(DataType data_type) {
        this.data_type = data_type;
    }

    public Boolean isHuman() {
        return human;
    }

    public void setHuman(Boolean human) {
        this.human = human;
    }

    public String getExam_id() {
        return exam_id;
    }

    public void setExam_id(String exam_id) {
        this.exam_id = exam_id;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public Integer getTask_number() {
        return task_number;
    }

    public void setTask_number(Integer task_number) {
        this.task_number = task_number;
    }

    public Boolean isTask_complete() {
        return task_complete;
    }

    public void setTask_complete(Boolean task_complete) {
        this.task_complete = task_complete;
    }

    public Boolean isAll_trials_valid() {
        return all_trials_valid;
    }

    public void setAll_trials_valid(Boolean all_trials_valid) {
        this.all_trials_valid = all_trials_valid;
    }

    public Double getTask_time() {
        return task_time;
    }

    public void setTask_time(Double task_time) {
        this.task_time = task_time;
    }

    public Integer getNum_subjects() {
        return num_subjects;
    }

    public void setNum_subjects(Integer num_subjects) {
        this.num_subjects = num_subjects;
    }

    public Boolean isMetrics_stale() {
        return metrics_stale;
    }

    public void setMetrics_stale(Boolean metrics_stale) {
        this.metrics_stale = metrics_stale;
    }

    //@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER) 
    //@JoinColumn(name="taskMetricsTrialData")
    //@Fetch(value = FetchMode.SUBSELECT)
    @XmlTransient
    public List<D> getTrials() {
        return trials;
    }

    public void setTrials(List<D> trials) {
        this.trials = trials;
    }
}
