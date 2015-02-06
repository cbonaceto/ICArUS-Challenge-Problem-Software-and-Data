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
package org.mitre.icarus.cps.assessment.assessment_metrics_computer.phase_2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.mitre.icarus.cps.assessment.assessment_metrics_computer.common.ASRandRSRComputer;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.CPAMetric;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.MetricInfo;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.BlueActionSelectionFrequency;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.SelectionFrequency;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.SigintSelectionFrequency;

/**
 * Computes Phase 2 CPA (Comparative Performance Assessment) metrics, including
 * RSR, variants of RSR, ASR, and RMR.
 * 
 * @author CBONACETO
 */
public class CPAMetricsComputer extends ASRandRSRComputer {
    
    /**
     * Computes RMR (Relative Match Rate) using the SIGINT location selection.    
     * 
     * @param locationIndex
     * @param selectionFrequencies
     * @param RMR
     * @param RMR_info
     * @param taskNum
     * @param trialNum
     * @return
     */
    public CPAMetric computeRMR_sigint(Integer locationIndex, 
            List<SigintSelectionFrequency> selectionFrequencies,
            CPAMetric RMR, MetricInfo RMR_info, Integer taskNum, Integer trialNum) {
        return computeRMR(locationIndex, 
                extractSelectionFrequencies(selectionFrequencies), 
                RMR, RMR_info, taskNum, trialNum);
    }    
    
    /**
     * Computes RMR (Relative Match Rate) using the Blue action selection.    
     * 
     * @param blueActionsIndex
     * @param selectionFrequencies
     * @param RMR
     * @param RMR_info
     * @param taskNum
     * @param trialNum
     * @return
     */
    public CPAMetric computeRMR_blueActions(Integer blueActionsIndex,
            List<BlueActionSelectionFrequency> selectionFrequencies,
            CPAMetric RMR, MetricInfo RMR_info, Integer taskNum, Integer trialNum) {
        return computeRMR(blueActionsIndex, 
                extractSelectionFrequencies(selectionFrequencies), 
                RMR, RMR_info, taskNum, trialNum);
    }
    
    public static List<Double> extractSelectionFrequencies(
            List<? extends SelectionFrequency> selectionFrequencies) {
        if (selectionFrequencies != null && !selectionFrequencies.isEmpty()) {
            List<Double> frequencies = new ArrayList<Double>(selectionFrequencies.size());
            for(SelectionFrequency selectionFrequency : selectionFrequencies) {
                frequencies.add(selectionFrequency.getSelectionPercent());
            }
            return frequencies;
        }
        return null;
    }
    
     /**
     * Computes RMR (Relative Match Rate), which is the ratio f_mod/f_max,
     * where f_mod is the frequency of the option that was selected by the
     * model, and f_max is the maximum frequency value.    
     * 
     * @param f_index
     * @param f
     * @param RMR
     * @param RMR_info
     * @param taskNum
     * @param trialNum
     * @return
     */
    public CPAMetric computeRMR(Integer f_index, List<Double> f, 
            CPAMetric RMR, MetricInfo RMR_info, Integer taskNum, Integer trialNum) {
        if (RMR == null) {
            RMR = new CPAMetric(RMR_info.getName());
        }

        Double rmr = computeRMR(f_index, f);
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
     * Computes RMR (Relative Match Rate), which is the ratio f_mod/f_max,
     * where f_mod is the frequency of the option that was selected by the
     * model, and f_max is the maximum frequency value.      
     * 
     * @param f_index
     * @param f
     * @return
     */
    public Double computeRMR(Integer f_index, List<Double> f) {
        if (f != null) {
            return computeRMR(f_index, f, Collections.max(f));
        }
        return null;
    }

    /**
     * Computes RMR (Relative Match Rate), which is the ratio f_mod/comparison_f,
     * where f_mod is the frequency of the option that was selected by the
     * model.
     *
     * @param f_index
     * @param f
     * @param comaprison_f
     * @return
     */
    public Double computeRMR(Integer f_index, List<Double> f, Double comaprison_f) {
        if (f_index != null && f != null && !f.isEmpty() && f_index < f.size()) {
            if(comaprison_f == null || comaprison_f.isNaN()) {
                comaprison_f = Collections.max(f);
            }
            return (f.get(f_index) / comaprison_f) * 100d;
        }
        return null;
    }
}