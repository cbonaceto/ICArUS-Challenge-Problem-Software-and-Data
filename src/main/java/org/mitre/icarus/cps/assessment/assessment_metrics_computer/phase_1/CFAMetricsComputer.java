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

import java.util.ArrayList;
import java.util.List;

import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.CFAMetric;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.MetricInfo;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.TrialMetrics;

/**
 * Computes Phase 1 CFA (Cognitive Fidelity Assessment) metrics, including
 * Representativeness (RR), Anchoring & Adjustment (AI), Confirmation Bias in
 * Weighing Evidence (CW), Probability Matching (PM_F, PM_RMS), and Confirmation
 * Bias in Seeking Evidence (CS).
 *
 * @author cbonaceto
 *
 */
public class CFAMetricsComputer {
    
    /**
     * Computes Representativeness bias (RR), computed as Nq - Np (at stage 1), 
     * exhibited if magnitude > 0 (Np > Nq).
     * 
     * @param Np
     * @param Nq
     * @param RR
     * @param comparisonMetrics
     * @param RR_info
     * @param taskNum
     * @param trialNum
     * @return
     */
    public CFAMetric computeRR(List<Double> Np, List<Double> Nq, CFAMetric RR,
            TrialMetrics comparisonMetrics, MetricInfo RR_info, Integer taskNum, Integer trialNum) {
        if (RR == null) {
            RR = new CFAMetric("RR");
        }

        if (Np != null && !Np.isEmpty() && Nq != null && !Nq.isEmpty()) {
            //RR computed as Nq - Np (at stage 1), exhibited if magnitude > 0
            RR.magnitude = Nq.get(0) - Np.get(0);
            RR.exhibited = RR.magnitude > 0;
        }

        CFAMetric comparisonRR = null;
        if (comparisonMetrics != null && comparisonMetrics.getRR() != null) {
            comparisonRR = comparisonMetrics.getRR();
            RR.pass = (RR.exhibited == comparisonRR.exhibited);
            RR.score = (RR.pass ? 100D : 0D);
        }
        RR.assessed = (comparisonRR == null || comparisonRR.assessed == null || comparisonRR.assessed)
                && RR_info.isAssessedForStage(taskNum, trialNum, 0);

        return RR;
    }
   
    /**
     * Computes Anchoring & Adjustment bias (AI), computed as |delta Nq| - |delta Np|,
     * exhibited if the sign of delta Nq and delta Np are the same and 
     * magnitude > 0 (|delta Np| < |delta Nq|).
     * 
     * @param Np_delta
     * @param Nq_delta
     * @param AI
     * @param comparisonMetrics
     * @param AI_info
     * @param taskNum
     * @param trialNum
     * @return
     */
    public List<CFAMetric> computeAI(List<Double> Np_delta, List<Double> Nq_delta, List<CFAMetric> AI,
            TrialMetrics comparisonMetrics, MetricInfo AI_info, Integer taskNum, Integer trialNum) {
        if (Np_delta != null && !Np_delta.isEmpty() && Nq_delta != null && Np_delta.size() == Nq_delta.size()) {
            List<CFAMetric> comparisonAI = null;
            if (comparisonMetrics != null && comparisonMetrics.getAI() != null
                    && comparisonMetrics.getAI().size() == Np_delta.size()) {
                comparisonAI = comparisonMetrics.getAI();
            }
            int numStages = Np_delta.size();
            if (AI == null || AI.size() != numStages) {
                AI = new ArrayList<CFAMetric>(numStages);
                for (int stage = 0; stage < numStages; stage++) {
                    AI.add(new CFAMetric("AI"));
                }
            }
            for (int stage = 1; stage < numStages; stage++) {
                //AI computed as |delta Nq| - |delta Np|
                CFAMetric currentAI = AI.get(stage);
                double deltaNq = Nq_delta.get(stage);
                double deltaNp = Np_delta.get(stage);
                currentAI.magnitude = Math.abs(deltaNq) - Math.abs(deltaNp);

                //For AI to be exhibited, the sign of delta Nq and delta Np must be the same
                //and |delta Np| < |delta Nq| (magnitude > 0)
                currentAI.exhibited = Math.signum(deltaNq) == Math.signum(deltaNp)
                        && currentAI.magnitude > 0;

                CFAMetric currentComparisonAI = null;
                if (comparisonAI != null) {
                    currentComparisonAI = comparisonAI.get(stage);
                    if (currentComparisonAI != null) {
                        //currentAI.setAssessed(currentComparisonAI.isAssessed());
                        currentAI.pass = currentAI.exhibited == currentComparisonAI.exhibited;
                        currentAI.score = currentAI.pass ? 100D : 0D;
                    }
                }
                currentAI.assessed = (currentComparisonAI == null || currentComparisonAI.assessed == null || currentComparisonAI.assessed)
                        && AI_info.isAssessedForStage(taskNum, trialNum, stage);

				//DEBUG CODE
				/*if(!currentAI.assessed) {
                 System.out.println("Skipping AI for " + taskNum + "-" + trialNum + "-" + (stage+1) + ", " 
                 + currentComparisonAI.assessed + ", " + AI_info.isAssessedForStage(taskNum, trialNum, stage));
                 }*/
                ////
            }
        }
        return AI;
    }

    /**
     * Computes Confirmation Bias in weighing evidence (CW) as |delta Np| - |delta Nq|,
     * exhibited if the sign of delta Nq and delta Np are the same and 
     * magnitude > 0 (|delta Np| > |delta Nq|).
     * 
     * @param Np_delta
     * @param Nq_delta
     * @param CW
     * @param comparisonMetrics
     * @param CW_info
     * @param taskNum
     * @param trialNum
     * @return
     */
    public List<CFAMetric> computeCW(List<Double> Np_delta, List<Double> Nq_delta, List<CFAMetric> CW,
            TrialMetrics comparisonMetrics, MetricInfo CW_info, Integer taskNum, Integer trialNum) {
        if (Np_delta != null && !Np_delta.isEmpty() && Nq_delta != null && Np_delta.size() == Nq_delta.size()) {
            List<CFAMetric> comparisonCW = null;
            if (comparisonMetrics != null && comparisonMetrics.getCW() != null
                    && comparisonMetrics.getCW().size() == Np_delta.size()) {
                comparisonCW = comparisonMetrics.getCW();
            }
            int numStages = Np_delta.size();
            if (CW == null || CW.size() != numStages) {
                CW = new ArrayList<CFAMetric>(numStages);
                for (int stage = 0; stage < numStages; stage++) {
                    CW.add(new CFAMetric("CW"));
                }
            }
            for (int stage = 1; stage < numStages; stage++) {
                //CW computed as |delta Np| - |delta Nq|
                CFAMetric currentCW = CW.get(stage);
                double deltaNq = Nq_delta.get(stage);
                double deltaNp = Np_delta.get(stage);
                currentCW.magnitude = Math.abs(deltaNp) - Math.abs(deltaNq);

		//For CW to be exhibited, the sign of delta Nq and delta Np must be the same
                //and |delta Np| > |delta Nq| (magnitude > 0)
                currentCW.exhibited = Math.signum(deltaNq) == Math.signum(deltaNp)
                        && currentCW.magnitude > 0;

                CFAMetric currentComparisonCW = null;
                if (comparisonCW != null) {
                    currentComparisonCW = comparisonCW.get(stage);
                    if (currentComparisonCW != null) {
                        //currentCW.setAssessed(currentComparisonCW.assessed);
                        currentCW.pass = currentCW.exhibited == currentComparisonCW.exhibited;
                        currentCW.score = currentCW.pass ? 100D : 0D;
                    }
                }
                currentCW.assessed = (currentComparisonCW == null || currentComparisonCW.assessed == null || currentComparisonCW.assessed)
                        && CW_info.isAssessedForStage(taskNum, trialNum, stage);
            }
        }
        return CW;
    }

    /**
     * Computes Probability Matching (PM) as |Fh-1| - |Fh-Ph|, 
     * exhibited if magnitude > 0 (|Fh-1| > |Fh-Ph|).
     * 
     * @param Fh_Ph
     * @param Fh_1
     * @param PM_F
     * @param comparisonPM
     * @param PM_F_info
     * @return
     */
    public CFAMetric computePM_F(Double Fh_Ph, Double Fh_1, CFAMetric PM_F, CFAMetric comparisonPM, MetricInfo PM_F_info) {
        if (PM_F == null) {
            PM_F = new CFAMetric("PM");
        }

        //PM computed as |Fh-1| - |Fh-Ph|, exhibited if magnitude > 0 (|Fh-1| > |Fh-Ph|)
        if (Fh_Ph != null && Fh_1 != null) {
            PM_F.magnitude = Math.abs(Fh_1 - Fh_Ph);
            PM_F.exhibited = PM_F.magnitude > 0;
        }

        if (comparisonPM != null) {
            PM_F.pass = PM_F.exhibited == comparisonPM.exhibited;
            PM_F.score = PM_F.pass ? 100D : 0D;
        }
        PM_F.assessed = PM_F_info.isAssessed() && (comparisonPM == null || comparisonPM.assessed == null || comparisonPM.assessed);

        return PM_F;
    }
    
    /**
     * Computes Probability Matching (PM) as RMS_F_I - RMS_F_P, 
     * exhibited if magnitude > 0 (RMS_F_I > RMS_F_P).
     * 
     * @param RMS_F_P
     * @param RMS_F_I
     * @param PM
     * @param comparisonMetrics
     * @param PM_RMS_info
     * @param taskNum
     * @param trialNum
     * @return
     */
    public CFAMetric computePM_RMS(Double RMS_F_P, Double RMS_F_I, CFAMetric PM, TrialMetrics comparisonMetrics,
            MetricInfo PM_RMS_info, Integer taskNum, Integer trialNum) {
        if (PM == null) {
            PM = new CFAMetric("PM");
        }

        //PM computed as RMS_F_I - RMS_F_P, exhibited if magnitude > 0 (RMS_F_I > RMS_F_P)
        if (RMS_F_P != null && RMS_F_I != null) {
            PM.magnitude = RMS_F_I - RMS_F_P;
            PM.exhibited = PM.magnitude > 0;
        }

        CFAMetric comparisonPM = null;
        if (comparisonMetrics != null && comparisonMetrics.getPM() != null) {
            comparisonPM = comparisonMetrics.getPM();
            //PM.setAssessed(comparisonPM.assessed);
            PM.pass = PM.exhibited == comparisonPM.exhibited;
            PM.score = PM.pass ? 100D : 0D;
        }
        PM.assessed = (comparisonPM == null || comparisonPM.assessed == null || comparisonPM.assessed)
                && PM_RMS_info.isAssessedForStage(taskNum, trialNum, 0);

        return PM;
    }

    /**
     * Computes Confirmation Bias in seeking evidence (CS) as C - 0.5, 
     * exhibited if magnitude > 0 (C > 0.5).
     * 
     * @param C
     * @param C_threshold
     * @param CS
     * @param comparisonCS
     * @param CS_info
     * @return
     */
    public CFAMetric computeCS(Double C, Double C_threshold, CFAMetric CS, 
            CFAMetric comparisonCS, MetricInfo CS_info) {
        if (CS == null) {
            CS = new CFAMetric("CS");
        }

        //CS computed as C - 0.5, exhibited if magnitude > 0 (C > 0.5)
        if (C != null) {
            CS.magnitude = C - C_threshold;
            CS.exhibited = CS.magnitude > 0;
        }

        if (comparisonCS != null) {
            CS.pass = CS.exhibited == comparisonCS.exhibited;
            CS.score = CS.pass ? 100D : 0D;
        }
        CS.assessed = CS_info.isAssessed() && (comparisonCS == null || comparisonCS.assessed == null || comparisonCS.assessed);

        return CS;
    }
}