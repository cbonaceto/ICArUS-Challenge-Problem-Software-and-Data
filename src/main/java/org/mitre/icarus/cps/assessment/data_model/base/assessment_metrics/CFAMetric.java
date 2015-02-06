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

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * CFA (Cognitive Fidelity Assessment) metric.
 *
 * @author CBONACETO 
 */
@Entity
public class CFAMetric extends Metric {

    private static final long serialVersionUID = -6792968768948948873L;

    private int cfaMetricId;

    /** Magnitude of the bias */
    public Double magnitude;

    /** Whether the bias was exhibited */
    public Boolean exhibited;

    /** Results of the significance testing of the bias using significance test 1 */
    protected MetricSignificance significance_1;

    /** Results of the significance testing of the bias using significance test 2 */
    protected MetricSignificance significance_2;

    public CFAMetric() {
    }

    public CFAMetric(String name) {
        super(name);
    }

    @Id
    @GeneratedValue
    public int getCfaMetricId() {
        return cfaMetricId;
    }

    public void setCfaMetricId(int cfaMetricId) {
        this.cfaMetricId = cfaMetricId;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    public MetricSignificance getSignificance_1() {
        return significance_1;
    }

    public void setSignificance_1(MetricSignificance significance_1) {
        this.significance_1 = significance_1;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    public MetricSignificance getSignificance_2() {
        return significance_2;
    }

    public void setSignificance_2(MetricSignificance significance_2) {
        this.significance_2 = significance_2;
    }
}
