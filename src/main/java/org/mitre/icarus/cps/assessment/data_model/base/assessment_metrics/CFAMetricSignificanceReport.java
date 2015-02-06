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

/**
 *
 * @author CBONACETO
 */
public class CFAMetricSignificanceReport {
    
    /** The CFA metric */
    private CFAMetric metric;
    
    /** The human value */
    private Double humanValue;
    
    /** The value against which the human value was measured */
    private Double comparisonValue;
    
    /** The trial/stage */
    private TrialIdentifier trial;
    
    /** The metric significance results */
    private MetricSignificance significance;
    
    public CFAMetricSignificanceReport() {}
    
    public CFAMetricSignificanceReport(CFAMetric metric, Double humanValue,
            Double comparisonValue, TrialIdentifier trial, MetricSignificance significance) {
        this.metric = metric;
        this.humanValue = humanValue;
        this.comparisonValue = comparisonValue;
        this.trial = trial;
        this.significance = significance;
    }

    public CFAMetric getMetric() {
        return metric;
    }

    public void setMetric(CFAMetric metric) {
        this.metric = metric;
    }

    public Double getHumanValue() {
        return humanValue;
    }

    public void setHumanValue(Double humanValue) {
        this.humanValue = humanValue;
    }

    public Double getComparisonValue() {
        return comparisonValue;
    }

    public void setComparisonValue(Double comparisonValue) {
        this.comparisonValue = comparisonValue;
    }

    public TrialIdentifier getTrial() {
        return trial;
    }

    public void setTrial(TrialIdentifier trial) {
        this.trial = trial;
    }

    public MetricSignificance getSignificance() {
        return significance;
    }

    public void setSignificance(MetricSignificance significance) {
        this.significance = significance;
    }
}
