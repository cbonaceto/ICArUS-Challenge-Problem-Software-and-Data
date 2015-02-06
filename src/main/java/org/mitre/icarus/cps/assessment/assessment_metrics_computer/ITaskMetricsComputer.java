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
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.AbstractTaskMetrics;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.AbstractTrialData;

/**
 * Interface for implementations that compute task metrics.
 *
 * @author CBONACETO
 * @param <D>
 * @param <T>
 * @param <I>
 *
 */
public interface ITaskMetricsComputer<D extends AbstractTrialData<?>, T extends AbstractTaskMetrics<D>, I extends AbstractMetricsInfo> {

    /**
     * 
     * 
     * @param taskMetrics
     * @param trials
     * @return
     */
    public T updateCompletionStatus(T taskMetrics, List<D> trials);

    /**
     * @param taskMetrics
     * @param trials
     * @param modifiedTrial
     * @return
     */
    public T updateCompletionStatus(T taskMetrics, List<D> trials, D modifiedTrial);

    /**
     * @param taskMetrics
     * @param task_time
     * @param trials
     * @param metricsInfo
     * @param comparisonMetrics
     * @param taskNum
     * @return
     */
    public T updateTaskMetrics(T taskMetrics, Double task_time, List<D> trials, I metricsInfo,
            T comparisonMetrics, int taskNum);
}
