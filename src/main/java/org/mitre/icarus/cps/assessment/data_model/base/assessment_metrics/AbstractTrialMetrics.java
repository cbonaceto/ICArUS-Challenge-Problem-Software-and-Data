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

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Base class for classes containing trial metrics.
 * 
 * @author CBONACETO
 *
 */
public abstract class AbstractTrialMetrics implements Serializable {

    private static final long serialVersionUID = -5410927443698639372L;

    private int trialMetricsId;

    protected Integer num_subjects;

    @Id
    @GeneratedValue
    public int getTrialMetricsId() {
        return trialMetricsId;
    }

    public void setTrialMetricsId(int trialMetricsId) {
        this.trialMetricsId = trialMetricsId;
    }

    public Integer getNum_subjects() {
        return num_subjects;
    }

    public void setNum_subjects(Integer num_subjects) {
        this.num_subjects = num_subjects;
    }
}