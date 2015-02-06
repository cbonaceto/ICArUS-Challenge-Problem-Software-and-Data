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
package org.mitre.icarus.cps.assessment.assessment_metrics_computer.phase_1;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.mitre.icarus.cps.app.util.ProbabilityUtils;

import org.mitre.icarus.cps.assessment.assessment_metrics_computer.common.ASRandRSRComputer;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.CPAMetric;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.MetricInfo;

/**
 * Computes Phase 1 CPA (Comparative Performance Assessment) metrics, including
 * RSR, variants of RSR, ASR, and RMR.
 *
 * @author cbonaceto
 *
 */
public class CPAMetricsComputer extends ASRandRSRComputer {   
 
    /**
     * Computes RMR (Relative Match Rate) for INT layer selections in Phase 1.
     * Keep with Phase 1.
     * 
     * @param ls_index
     * @param F_LS_percent
     * @param RMR
     * @param RMR_info
     * @param taskNum
     * @param trialNum
     * @return
     */
    public CPAMetric computeRMR(Integer ls_index, List<Double> F_LS_percent, 
            CPAMetric RMR, MetricInfo RMR_info, Integer taskNum, Integer trialNum) {
        if (RMR == null) {
            RMR = new CPAMetric(RMR_info.getName());
        }

        Double rmr = computeRMR(ls_index, F_LS_percent);
        RMR.score = rmr;
        if (rmr != null) {
            RMR.pass = rmr >= RMR_info.getTrial_pass_threshold();
        } else {
            RMR.pass = null;
        }
        RMR.assessed = RMR_info.isAssessedForStage(taskNum, trialNum, 1);

        return RMR;
    }

    /**
     * Computes RMR (Relative Match Rate) for INT layer selections in Phase 1.
     * Keep with Phase 1.
     * 
     * @param ls_index
     * @param F_LS_percent
     * @return
     */
    public Double computeRMR(Integer ls_index, List<Double> F_LS_percent) {
        if (F_LS_percent != null) {
            return computeRMR(ls_index, F_LS_percent, Collections.max(F_LS_percent));
        }
        return null;
    }

    /**
     * Computes RMR (Relative Match Rate) for INT layer selections.
     * Keep with Phase 1.
     * 
     * @param ls_index
     * @param F_LS_percent
     * @param modalFrequency
     * @return
     */
    public Double computeRMR(Integer ls_index, List<Double> F_LS_percent, Double modalFrequency) {
        if (ls_index != null && F_LS_percent != null && !F_LS_percent.isEmpty() && ls_index < F_LS_percent.size()) {
            if (modalFrequency == null || modalFrequency.isNaN()) {
                modalFrequency = Collections.max(F_LS_percent);
            }
            return (F_LS_percent.get(ls_index) / modalFrequency) * 100d;
        }
        return null;
    }
    
    public static void main(String[] args) {
        /*List<Double> humanProbs = ProbabilityUtils.normalizePercentProbabilities_Double(
         Arrays.asList(70d, 30d), null, 0.01);
         List<Double> modelProbs = ProbabilityUtils.normalizePercentProbabilities_Double(
         Arrays.asList(53d, 47d), null, 0.01);
         List<Double> uniformProbs = ProbabilityUtils.createProbabilities_Double(2, 0.5d);		
         List<Double> normativeProbs = ProbabilityUtils.normalizePercentProbabilities_Double(
         Arrays.asList(50d, 50d), null, 0.01);*/

        List<Double> humanProbs = ProbabilityUtils.normalizePercentProbabilities_Double(
                Arrays.asList(22.54, 58.12, 6.43, 12.91), null, 0.01);
        List<Double> modelProbs = ProbabilityUtils.normalizePercentProbabilities_Double(
                Arrays.asList(24.95, 53.06, 1.00, 20.99), null, 0.01);
        List<Double> uniformProbs = ProbabilityUtils.createProbabilities_Double(4, 0.25d);
        List<Double> normativeProbs = ProbabilityUtils.normalizePercentProbabilities_Double(
                Arrays.asList(25d, 25d, 25d, 25d), null, 0.01);

        CPAMetricsComputer mc = new CPAMetricsComputer();
        Double spm = mc.computeSpm(modelProbs, humanProbs);
        Double spr = mc.computeSpr(humanProbs);
        Double spq = mc.computeSpq(humanProbs, normativeProbs);
        System.out.println("Spm: " + spm);
        System.out.println("Spr: " + spr);
        System.out.println("Spq: " + spq);
        System.out.println("RSR: " + mc.computeRSR(modelProbs, humanProbs));
        System.out.println("RSR: " + mc.computeRSR(spm, spr));
        System.out.println("RSR: " + mc.computeRSR(modelProbs, humanProbs, uniformProbs));
        System.out.println("RSR_Bayesian: " + mc.computeRSR(modelProbs, humanProbs, normativeProbs));
        System.out.println("RSR_Bayesian: " + mc.computeRSR(spm, spq));
        System.out.println("RSR_RMSE: " + mc.computeRSR_RMSE(modelProbs, humanProbs));
        System.out.println("RSR_RMSE_Bayesian: " + mc.computeRSR_RMSE(modelProbs, humanProbs, normativeProbs));
        System.out.println("ASR: " + mc.computeASR(modelProbs, humanProbs));
        System.out.println("ASR RMS Multiplier for 4 hypotheses: " + mc.computeRmsMultiplierForASR(4));
    }
}
