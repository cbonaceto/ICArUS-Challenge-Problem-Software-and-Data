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
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;

/**
 * @author cbonaceto
 *
 */
@Entity
public class TrialIdentifier implements Serializable, Comparable<TrialIdentifier> {

    private static final long serialVersionUID = -8684294503727950937L;

    private int id;

    protected Integer task_number;

    protected Integer trial_number;

    protected SortedSet<Integer> stages;

    public TrialIdentifier() {
    }

	//public TrialIdentifier(int task_number, int trial_number) {
    //	this(task_number, trial_number, null);
    //}
    public TrialIdentifier(int task_number, int trial_number, int stage) {
        this(task_number, trial_number, new TreeSet<Integer>(Collections.singleton(stage)));
    }

    public TrialIdentifier(int task_number, int trial_number, SortedSet<Integer> stages) {
        this.task_number = task_number;
        this.trial_number = trial_number;
        this.stages = stages;
    }

    @Id
    @GeneratedValue
    @Column(name = "trialIdentifierId")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @ElementCollection
    @Sort(type = SortType.NATURAL)
    public SortedSet<Integer> getStages() {
        return stages;
    }

    public void setStages(SortedSet<Integer> stages) {
        this.stages = stages;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TrialIdentifier) {
            return (compareTo((TrialIdentifier) obj) == 0);
        } else {
            return false;
        }
    }

    @Override
    public int compareTo(TrialIdentifier o) {
        if (o != null) {
            if (task_number == o.task_number) {
                if (trial_number != null) {
                    return trial_number.compareTo(o.trial_number);
                } else {
                    return 0;
                }
            } else {
                return task_number.compareTo(o.task_number);
            }
        }
        return -1;
    }
}
