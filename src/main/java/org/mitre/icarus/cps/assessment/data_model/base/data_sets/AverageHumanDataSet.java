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
package org.mitre.icarus.cps.assessment.data_model.base.data_sets;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlTransient;

import org.mitre.icarus.cps.assessment.data_model.base.DataType;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.AbstractMetricsInfo;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.AbstractSubjectMetrics;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.AbstractTaskMetrics;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.AbstractTrialData;

/**
 * Data set containing the average human data.
 * 
 * @author CBONACETO
 *
 */
@Entity
public abstract class AverageHumanDataSet<D extends AbstractTrialData<?>, 
        T extends AbstractTaskMetrics<D>, S extends AbstractSubjectMetrics, I extends AbstractMetricsInfo> extends DataSet {

    /** Time stamp indicating when the data set was generated */
    protected Long time_stamp;

    /** The average human task metrics for each task */
    protected List<T> taskMetrics;

    /** The average human subject metrics */
    protected S subjectMetrics;

    /**
     * The metrics info associated with the data set
     */
    protected I metricsInfo;

    @Id
    @GeneratedValue
    @Column(name = "averageHumanDataSetId")
    @Override
    public int getId() {
        return id;
    }

    public Long getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(Long time_stamp) {
        this.time_stamp = time_stamp;
    }

    /**
     * @param phase_id
     * @param trial_number
     * @return
     */
    public D getTrial(String task_id, Integer trial_number) {
        T task = getTaskMetrics(task_id);
        if (task != null && task.getTrials() != null && !task.getTrials().isEmpty()) {
            for (D trial : task.getTrials()) {
                if (trial.getTrial_number() == trial_number) {
                    return trial;
                }
            }
        }
        return null;
    }

    /**
     * @param phase_id
     * @return
     */
    public T getTaskMetrics(String task_id) {
        if (taskMetrics != null && !taskMetrics.isEmpty()) {
            for (T task : taskMetrics) {
                if (task.getTask_id().equals(task_id)) {
                    return task;
                }
            }
        }
        return null;
    }

    //@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
    //@JoinColumn(name="averageHumanDataSetTaskMetrics")
    //@Fetch(value = FetchMode.SUBSELECT)
    @XmlTransient
    public List<T> getTaskMetrics() {
        return taskMetrics;
    }

    public void setTaskMetrics(List<T> taskMetrics) {
        this.taskMetrics = taskMetrics;
    }

    //@ManyToOne(cascade=CascadeType.ALL) 
    @XmlTransient
    public S getSubjectMetrics() {
        return subjectMetrics;
    }

    public void setSubjectMetrics(S subjectMetrics) {
        this.subjectMetrics = subjectMetrics;
    }

    //@ManyToOne(cascade=CascadeType.ALL) 
    @XmlTransient
    public I getMetricsInfo() {
        return metricsInfo;
    }

    public void setMetricsInfo(I metricsInfo) {
        this.metricsInfo = metricsInfo;
    }

    @Override
    public DataType getData_type() {
        return DataType.Human_Avg;
    }
}
