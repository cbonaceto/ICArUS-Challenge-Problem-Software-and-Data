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
package org.mitre.icarus.cps.assessment.assessment_processor;

import java.util.List;

import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.AbstractExamMetrics;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.AbstractMetricsInfo;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.AbstractSubjectMetrics;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.AbstractTaskMetrics;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.AbstractTrialData;
import org.mitre.icarus.cps.assessment.data_model.base.data_sets.AllHumansDataSet;
import org.mitre.icarus.cps.assessment.data_model.base.data_sets.AverageHumanDataSet;
import org.mitre.icarus.cps.assessment.data_model.base.data_sets.SingleParticipantDataSet;
import org.mitre.icarus.cps.exam.base.IcarusExam;
import org.mitre.icarus.cps.exam.base.feedback.TestTrialFeedback;
import org.mitre.icarus.cps.exam.base.response.ResponseGeneratorData;
import org.mitre.icarus.cps.exam.base.testing.IcarusTestPhase;
import org.mitre.icarus.cps.exam.base.testing.IcarusTestTrial;

/**
 * Interface for implementations that process human and model responses and
 * compute CFA and CPA metrics.
 *
 * @author CBONACETO
 * @param <SPDS>
 * @param <AHDS>
 * @param <R>
 * @param <P>
 * @param <E>
 * @param <F>
 *
 * @param <D>
 * @param <T>
 * @param <M>
 * @param <S>
 * @param <I>
 */
public interface IAssessmentProcessor<
	SPDS extends SingleParticipantDataSet<D, T, M, S>, AHDS extends AverageHumanDataSet<D, T, S, I>, R extends IcarusTestTrial, P extends IcarusTestPhase<?>, E extends IcarusExam<?>, F extends TestTrialFeedback<R>, D extends AbstractTrialData<?>, T extends AbstractTaskMetrics<D>, M extends AbstractExamMetrics<T>, S extends AbstractSubjectMetrics, I extends AbstractMetricsInfo> {

    /**
     * Create the data set for a single participant.
     * 
     * @param exam
     * @param examResponse
     * @param taskIds
     * @param taskNumbers
     * @param metricsInfo
     * @param responseGenerator
     * @param computeTaskAndExamMetricsForHumanSubject
     * @param comparisonData
     * @return
     */
    public SPDS buildSingleParticipantDataSet(E exam, E examResponse, List<String> taskIds,
            List<Integer> taskNumbers, I metricsInfo, ResponseGeneratorData responseGenerator,
            boolean computeTaskAndExamMetricsForHumanSubject, AHDS comparisonData);

    /**
     * Create the average human data set.
     * 
     * @param humanDataSet
     * @return
     */
    public AHDS buildAverageHumanDataSet(AllHumansDataSet<D, S> humanDataSet);

    /**
     * Process a trial response.
     * 
     * @param exam
     * @param task
     * @param trial
     * @param trialFeedback
     * @param scoreTrialIfFeedbackMissingOrIncomplete
     * @param taskId
     * @param taskNum
     * @param trialNum
     * @param metricsInfo
     * @param participantData
     * @param comparisonData
     */
    public void processTrialResponse(E exam, P task, R trial, F trialFeedback,
            boolean scoreTrialIfFeedbackMissingOrIncomplete,
            String taskId, Integer taskNum, Integer trialNum, I metricsInfo,
            SPDS participantData, AHDS comparisonData);
}
