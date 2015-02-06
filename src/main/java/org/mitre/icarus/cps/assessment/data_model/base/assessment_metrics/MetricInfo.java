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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;


/**
 * Contains information about a CFA or CPA metric.
 * 
 * @author CBONACETO
 *
 */
@Entity
public class MetricInfo implements Serializable {

    private static final long serialVersionUID = 3744364160395991726L;

    /** The metric's unique ID */
    protected int id;

    /** The metric type identifier */
    protected Integer metricTypeId;

    /** The metric name */
    protected String name;

    /** Short description of the metric */
    protected String description;

    /** Full description of the metric */
    protected String longDescription;

    /** The tasks the metric is computed for */
    protected SortedSet<Integer> tasks;  
    
    /** The weight of each task. Specified for all tasks, and
     set to 0 if the metric is not computed for that task */
    protected List<Double> taskWeights;
    
    /** Whether to use the task weights when computing the metric */
    protected Boolean useTaskWeights;
    
    /** The total number of trials/stages the metric was computed over (total 
     number of individual data points) */
    protected Integer num_trials_stages;

    /**
     * List of excluded trials for which the metric is not computed
     */
    protected List<TrialIdentifier> excludedTrials;

    @XmlTransient
    private int lastSortedSize = 0;

    /** Whether the metric is calculated */
    protected Boolean calculated;

    /** Whether the metric is used in the assessment of a model */
    protected Boolean assessed;

    /** The threshold for passing the metric on a trial */
    protected Double trial_pass_threshold;

    /** The threshold for passing the metric on a task */
    protected Double task_pass_threshold;

    /** The overall threshold for passing the metric*/
    protected Double overall_pass_threshold;

    public MetricInfo() {
    }

    public MetricInfo(String name, Integer metricTypeId) {
        this.name = name;
        this.metricTypeId = metricTypeId;
    }

    /*public MetricInfo(int id, String name, MetricType metricType) {
     //this.id = id;
     this.name = name;		
     this.metricType = metricType;
     }*/
    public Boolean isAssessedForTask(Integer phase_number) {
        if (tasks != null) {
            return tasks.contains(phase_number);
        }
        return false;
    }

    /**
     * @param phase_number
     * @param trial_number
     * @param stage
     * @return
     */
    public Boolean isAssessedForStage(Integer phase_number, Integer trial_number, Integer stage) {
        if (excludedTrials != null) {
            if (lastSortedSize != excludedTrials.size()) {
                Collections.sort(excludedTrials);
                lastSortedSize = excludedTrials.size();
            }
            int index = Collections.binarySearch(excludedTrials, new TrialIdentifier(phase_number, trial_number, null));
            if (index >= 0) {
                TrialIdentifier match = excludedTrials.get(index);
                return !(stage == null || match.stages == null || match.stages.contains(stage));
            }
        }
        return true;
    }

    public static void main(String[] args) {
        MetricInfo mi = new MetricInfo();
        mi.setExcludedTrials(new ArrayList<TrialIdentifier>(Arrays.asList(
                new TrialIdentifier(5, 1, new TreeSet<Integer>(Arrays.asList(2, 3))),
                new TrialIdentifier(5, 2, new TreeSet<Integer>(Arrays.asList(3))),
                new TrialIdentifier(5, 3, new TreeSet<Integer>(Arrays.asList(1))),
                new TrialIdentifier(5, 4, new TreeSet<Integer>(Arrays.asList(2, 4))),
                new TrialIdentifier(5, 5, new TreeSet<Integer>(Arrays.asList(1, 3))),
                new TrialIdentifier(5, 6, new TreeSet<Integer>(Arrays.asList(1))),
                new TrialIdentifier(5, 9, new TreeSet<Integer>(Arrays.asList(1))),
                new TrialIdentifier(5, 10, new TreeSet<Integer>(Arrays.asList(1, 4))))));
        /*mi.setExcludedTrials(new ArrayList<TrialIdentifier>(Arrays.asList(
         new TrialIdentifier(1, 90, 0),
         new TrialIdentifier(5, 1, 0),
         new TrialIdentifier(5, 2, 0),
         new TrialIdentifier(5, 3, 0),
         new TrialIdentifier(5, 4, 0),
         new TrialIdentifier(5, 5, 0),
         new TrialIdentifier(5, 6, 0),
         new TrialIdentifier(5, 7, 0),
         new TrialIdentifier(5, 8, 0),
         new TrialIdentifier(5, 9, 0),
         new TrialIdentifier(5, 10, null))));*/
        System.out.println(mi.isAssessedForStage(5, 1, 5));
    }

    @Id
    @GeneratedValue
    @Column(name = "metricInfoId")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getMetricTypeId() {
        return metricTypeId;
    }

    public void setMetricTypeId(Integer metricTypeId) {
        this.metricTypeId = metricTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    /*public MetricType getMetricType() {
     return metricType;
     }

     public void setMetricType(MetricType metricType) {
     this.metricType = metricType;
     }*/
    @ElementCollection
    @Sort(type = SortType.NATURAL)
    public SortedSet<Integer> getTasks() {
        return tasks;
    }

    public void setTasks(SortedSet<Integer> tasks) {
        this.tasks = tasks;
    }

    public List<Double> getTaskWeights() {
        return taskWeights;
    }

    public void setTaskWeights(List<Double> taskWeights) {
        this.taskWeights = taskWeights;
    }

    public Boolean isUseTaskWeights() {
        return useTaskWeights;
    }

    public void setUseTaskWeights(Boolean useTaskWeights) {
        this.useTaskWeights = useTaskWeights;
    }   

    public Integer getNum_trials_stages() {
        return num_trials_stages;
    }

    public void setNum_trials_stages(Integer num_trials_stages) {
        this.num_trials_stages = num_trials_stages;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "metricInfoTrialIdentifiers")
    @Fetch(value = FetchMode.SUBSELECT)
    public List<TrialIdentifier> getExcludedTrials() {
        return excludedTrials;
    }

    public void setExcludedTrials(List<TrialIdentifier> excludedTrials) {
        this.excludedTrials = excludedTrials;
        if (excludedTrials != null) {
            Collections.sort(excludedTrials);
            lastSortedSize = excludedTrials.size();
        }
    }

    public Boolean isCalculated() {
        return calculated;
    }

    public void setCalculated(Boolean calculated) {
        this.calculated = calculated;
    }

    public Boolean isAssessed() {
        return assessed;
    }

    public void setAssessed(Boolean assessed) {
        this.assessed = assessed;
    }

    public Double getTrial_pass_threshold() {
        return trial_pass_threshold;
    }

    public void setTrial_pass_threshold(Double trial_pass_threshold) {
        this.trial_pass_threshold = trial_pass_threshold;
    }

    public Double getTask_pass_threshold() {
        return task_pass_threshold;
    }

    public void setTask_pass_threshold(Double task_pass_threshold) {
        this.task_pass_threshold = task_pass_threshold;
    }

    public Double getOverall_pass_threshold() {
        return overall_pass_threshold;
    }

    public void setOverall_pass_threshold(Double overall_pass_threshold) {
        this.overall_pass_threshold = overall_pass_threshold;
    }
}