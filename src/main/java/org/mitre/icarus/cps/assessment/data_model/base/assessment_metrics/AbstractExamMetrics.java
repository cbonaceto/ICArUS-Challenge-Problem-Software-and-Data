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
 * Base class for exam metrics.
 * 
 * 
 * @author CBONACETO
 * @param <T>
 *
 */
public abstract class AbstractExamMetrics<T extends AbstractTaskMetrics<?>> implements Serializable {

    private static final long serialVersionUID = 1L;

    protected int id;
    protected String site_id;
    protected String response_generator_id;
    protected DataType data_type;
    protected Boolean human;
    protected String exam_id;
    protected Boolean exam_complete;
    protected Boolean all_tasks_valid;

    /**
     * The tasks in the exam
     */
    protected List<T> tasks;

    public AbstractExamMetrics() { }
    
    public AbstractExamMetrics(String exam_id, ResponseGeneratorData responseGenerator) {
        if (responseGenerator != null) {
            site_id = responseGenerator.getResponseGeneratorId();
            response_generator_id = responseGenerator.getResponseGeneratorId();
            human = responseGenerator.isHumanSubject();
        }
        this.exam_id = exam_id;
    }        

    /**
     * Get the TaskMetrics object for the task with the given task ID.
     *
     * @param task_id
     * @return
     */
    public T getTask(String task_id) {
        if (tasks != null && !tasks.isEmpty()) {
            for(T task : tasks) {
                if (task.getTask_id().equals(task_id)) {
                    return task;
                }
            }
        }
        return null;
    }

    @Id
    @GeneratedValue
    @Column(name = "examMetricsId")
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

    public Boolean isExam_complete() {
        return exam_complete;
    }

    public void setExam_complete(Boolean exam_complete) {
        this.exam_complete = exam_complete;
    }

    public Boolean isAll_tasks_valid() {
        return all_tasks_valid;
    }

    public void setAll_tasks_valid(Boolean all_tasks_valid) {
        this.all_tasks_valid = all_tasks_valid;
    }

	//@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    //@JoinColumn(name="examMetricsTaskMetrics")
    //@Fetch(value = FetchMode.SUBSELECT)
    @XmlTransient
    public List<T> getTasks() {
        return tasks;
    }

    public void setTasks(List<T> tasks) {
        this.tasks = tasks;
    }
}
