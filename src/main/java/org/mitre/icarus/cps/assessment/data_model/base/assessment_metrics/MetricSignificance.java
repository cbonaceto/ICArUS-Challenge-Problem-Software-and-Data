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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author CBONACETO
 *
 */
@Entity
public class MetricSignificance implements Serializable {

    private static final long serialVersionUID = -584288336899261367L;

    private int metricSignifcanceId;

    protected String test_name;

    protected Double p_value;

    protected Double maximum_p_value;

    protected Boolean significant;
    
    public MetricSignificance() {}
    
    public MetricSignificance(String test_name, Double p_value, Double maximum_p_value,
            Boolean significant) {
        this.test_name = test_name;
        this.p_value = p_value;
        this.maximum_p_value = maximum_p_value;
        this.significant = significant;
    }

    @Id
    @GeneratedValue
    public int getMetricSignifcanceId() {
        return metricSignifcanceId;
    }

    public void setMetricSignifcanceId(int metricSignifcanceId) {
        this.metricSignifcanceId = metricSignifcanceId;
    }

    public String getTest_name() {
        return test_name;
    }

    public void setTest_name(String test_name) {
        this.test_name = test_name;
    }

    public Double getP_value() {
        return p_value;
    }

    public void setP_value(Double p_value) {
        this.p_value = p_value;
    }

    public Double getMaximum_p_value() {
        return maximum_p_value;
    }

    public void setMaximum_p_value(Double maximum_p_value) {
        this.maximum_p_value = maximum_p_value;
    }

    public Boolean isSignificant() {
        return significant;
    }

    public void setSignificant(Boolean significant) {
        this.significant = significant;
    }
}
