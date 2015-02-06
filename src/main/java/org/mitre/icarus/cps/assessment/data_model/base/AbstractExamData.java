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
package org.mitre.icarus.cps.assessment.data_model.base;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlTransient;

import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.AbstractMetricsInfo;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.AbstractSubjectMetrics;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.AbstractTaskMetrics;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.AbstractTrialData;
import org.mitre.icarus.cps.assessment.data_model.base.data_sets.AverageHumanDataSet;

/**
 *
 * Base class for data associated with an exam.
 * 
 * @author cbonaceto
 *
 */
public abstract class AbstractExamData<
	AHDS extends AverageHumanDataSet<D, T, S, I>, D extends AbstractTrialData<?>, T extends AbstractTaskMetrics<D>, S extends AbstractSubjectMetrics, I extends AbstractMetricsInfo> implements Serializable {

    private static final long serialVersionUID = -4394718908032403418L;

    protected int id;

    protected int program_phase_id;

    protected String examId;

    protected String examName;

    protected Boolean official_exam;

    protected Integer num_tasks;

    protected List<String> task_names;

    protected List<String> task_ids;

    protected List<Integer> task_numbers;

    protected I metricsInfo;

    /**
     * The average human data set for the exam
     */
    protected AHDS avgHumanDataSet;

    public AbstractExamData() {
    }

    /**
     * Initialize fields using the given exam.
     *
     * @param exam
     */
    public AbstractExamData(String examId, String examName) {
        this.examId = examId;
        this.examName = examName;
    }

    @Id
    @GeneratedValue
    @Column(name = "examDataId")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public int getProgram_phase_id() {
        return program_phase_id;
    }

    public void setProgram_phase_id(int program_phase_id) {
        this.program_phase_id = program_phase_id;
    }

    public Boolean getOfficial_exam() {
        return official_exam;
    }

    public void setOfficial_exam(Boolean official_exam) {
        this.official_exam = official_exam;
    }

    public Integer getNum_tasks() {
        return num_tasks;
    }

    public void setNum_tasks(Integer num_tasks) {
        this.num_tasks = num_tasks;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    @ElementCollection
    public List<String> getTask_names() {
        return task_names;
    }

    public void setTask_names(List<String> task_names) {
        this.task_names = task_names;
    }

    @ElementCollection
    public List<String> getTask_ids() {
        return task_ids;
    }

    public void setTask_ids(List<String> task_ids) {
        this.task_ids = task_ids;
    }

    @ElementCollection
    public List<Integer> getTask_numbers() {
        return task_numbers;
    }

    public void setTask_numbers(List<Integer> task_numbers) {
        this.task_numbers = task_numbers;
    }

    //@ManyToOne(cascade=CascadeType.ALL)
    @XmlTransient
    public I getMetricsInfo() {
        return metricsInfo;
    }

    public void setMetricsInfo(I metricsInfo) {
        this.metricsInfo = metricsInfo;
    }

    //@ManyToOne(cascade=CascadeType.ALL)
    @XmlTransient
    public AHDS getAvgHumanDataSet() {
        return avgHumanDataSet;
    }

    public void setAvgHumanDataSet(AHDS avgHumanDataSet) {
        this.avgHumanDataSet = avgHumanDataSet;
    }
}
