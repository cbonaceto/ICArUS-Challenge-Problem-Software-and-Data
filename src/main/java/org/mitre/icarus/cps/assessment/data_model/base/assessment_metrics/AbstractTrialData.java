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

import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlTransient;

import org.mitre.icarus.cps.assessment.data_model.base.DataType;
import org.mitre.icarus.cps.assessment.data_model.base.IdUtil;
import org.mitre.icarus.cps.exam.base.response.ResponseGeneratorData;

/**
 *
 * @author CBONACETO
 * @param <T>
 *
 */
public abstract class AbstractTrialData<T extends AbstractTrialMetrics> implements Serializable {

    private static final long serialVersionUID = 8771832060595736075L;

    private int id;

    protected String site_id;

    protected String response_generator_id;

    protected DataType data_type;

    protected Boolean human;

    protected String exam_id;

    protected String task_id;

    protected Integer task_number;

    protected Integer trial_number;

    protected Long time_stamp;

    protected Boolean trial_complete;

    protected Boolean trial_valid;

    protected Double trial_time;

    protected String errors;

    protected String warnings;

    protected String response_file_url;

    protected T metrics;

    /**
     * Default constructor required for querying
     */
    public AbstractTrialData() {
    }

    /**
     *
     * @param responseGenerator
     * @param data_type
     * @param exam_id
     * @param task_id
     * @param task_number
     */
    public AbstractTrialData(ResponseGeneratorData responseGenerator, DataType data_type,
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
        this.id = IdUtil.calculateTrialDataId(this);
    }

    /**
     * Constructor specifying fields needed to compute primary key hash code.
     * 
     * @param site_id
     * @param response_generator_id
     * @param exam_id
     * @param task_id
     * @param trial_number
     */
    public AbstractTrialData(String site_id, String response_generator_id,
            String exam_id, String task_id, Integer trial_number) {
        this.site_id = site_id;
        this.response_generator_id = response_generator_id;
        this.exam_id = exam_id;
        this.task_id = task_id;
        this.trial_number = trial_number;
        this.id = IdUtil.calculateTrialDataId(this);
    }

    @Id
    @Column(name = "trialDataId")
    @GeneratedValue
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

    @Enumerated
    @Column(name = "trialDataDataType")
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

    public Long getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(Long time_stamp) {
        this.time_stamp = time_stamp;
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

    public Integer getTrial_number() {
        return trial_number;
    }

    public void setTrial_number(Integer trial_number) {
        this.trial_number = trial_number;
    }

    public Boolean isTrial_complete() {
        return trial_complete;
    }

    public void setTrial_complete(Boolean trial_complete) {
        this.trial_complete = trial_complete;
    }

    public Boolean isTrial_valid() {
        return trial_valid;
    }

    public void setTrial_valid(Boolean trial_valid) {
        this.trial_valid = trial_valid;
    }

    public Double getTrial_time() {
        return trial_time;
    }

    public void setTrial_time(Double trial_time) {
        this.trial_time = trial_time;
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }

    public String getWarnings() {
        return warnings;
    }

    public void setWarnings(String warnings) {
        this.warnings = warnings;
    }

    public String getResponse_file_url() {
        return response_file_url;
    }

    public void setResponse_file_url(String response_file_url) {
        this.response_file_url = response_file_url;
    }

    //@ManyToOne(cascade=CascadeType.ALL)
    @XmlTransient
    public T getMetrics() {
        return metrics;
    }

    public void setMetrics(T metrics) {
        this.metrics = metrics;
    }
}
