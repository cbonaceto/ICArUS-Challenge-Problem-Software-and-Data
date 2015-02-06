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
package org.mitre.icarus.cps.assessment.assessment_metrics_computer;

import java.util.List;

import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.AbstractMetricsInfo;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.AbstractSubjectMetrics;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.AbstractTaskMetrics;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.AbstractTrialData;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.SubjectMetrics;

/**
 * Interface for implementations that compute overall subject metrics.
 *
 * @author CBONACETO
 * @param <D>
 * @param <T>
 * @param <S>
 * @param <I>
 *
 */
public interface ISubjectMetricsComputer<D extends AbstractTrialData<?>, T extends AbstractTaskMetrics<D>, S extends AbstractSubjectMetrics, I extends AbstractMetricsInfo> {

    /**
     * @param subjectMetrics
     * @param tasks
     * @param metricsInfo
     * @param comparisonMetrics
     * @return
     */
    public SubjectMetrics updateSubjectMetrics_Tasks(S subjectMetrics, List<T> tasks,
            I metricsInfo, S comparisonMetrics);

    /**
     * @param subjectMetrics
     * @param trials
     * @param metricsInfo
     * @param comparisonMetrics
     * @return
     */
    public SubjectMetrics updateSubjectMetrics_Trials(S subjectMetrics, List<D> trials,
            I metricsInfo, S comparisonMetrics);

    /**
     * @param subjectMetrics
     * @param averageMetrics
     * @param comparisonMetrics
     * @param metricsInfo
     * @return
     */
    public SubjectMetrics updateAverageSubjectMetrics(List<S> subjectMetrics, S averageMetrics,
            S comparisonMetrics, I metricsInfo);
}
