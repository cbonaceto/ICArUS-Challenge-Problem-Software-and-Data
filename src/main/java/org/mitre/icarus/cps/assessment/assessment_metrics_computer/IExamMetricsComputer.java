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

import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.AbstractExamMetrics;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.AbstractMetricsInfo;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.AbstractSubjectMetrics;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.AbstractTaskMetrics;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.AbstractTrialData;

/**
 * Interface for implementations that compute exam metrics.
 * 
 * @author CBONACETO
 *
 * @param <D>
 * @param <T>
 * @param <M>
 * @param <S>
 * @param <I>
 */
public interface IExamMetricsComputer<D extends AbstractTrialData<?>, T extends AbstractTaskMetrics<D>, M extends AbstractExamMetrics<T>, S extends AbstractSubjectMetrics, I extends AbstractMetricsInfo> {

    /**
     * @param examMetrics
     * @param tasks
     * @return
     */
    public M updateCompletionStatus(M examMetrics, List<T> tasks);

    /**
     * @param examMetrics
     * @param tasks
     * @param modifiedTask
     * @return
     */
    public M updateCompletionStatus(M examMetrics, List<T> tasks, T modifiedTask);

    /**
     * @param examMetrics
     * @param tasks
     * @param subjectMetrics
     * @param metricsInfo
     */
    public M updateAllMetrics(M examMetrics, List<T> tasks, S subjectMetrics, I metricsInfo);

    /**
     * @param examMetrics
     * @param tasks
     * @param subjectMetrics
     * @param metricsInfo
     * @param modifiedTask
     */
    public M updateStaleMetricsBasedOnModifiedTask(M examMetrics, List<T> tasks, S subjectMetrics,
            I metricsInfo, T modifiedTask);
}
