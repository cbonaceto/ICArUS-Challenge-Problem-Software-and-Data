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
import java.util.List;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.CFAMetric;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.MetricInfo;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.MetricType;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.TrialMetrics;

/** 
 * Computes Phase 2 CFA (Cognitive Fidelity Assessment) metrics, including
 * Anchoring & Adjustment (AA), Persistence of Discredited Evidence (PDE),
 * Representativeness (RR), Availability (AV), Probability Matching (PM),
 * Confirmation Bias (CS), Change Blindness (CB), and Satisfaction of Search (SS).
 * 
 * @author CBONACETO
 */
public class CFAMetricsComputer {    
   
    /**
     * Computes Anchoring & Adjustment bias (AA), computed as Nq - Np,
     * exhibited if magnitude > 0 (Np < Nq).
     * 
     * @param Np
     * @param Nq
     * @param AA
     * @param comparisonMetrics
     * @param AA_info
     * @param taskNum
     * @param trialNum
     * @return
     */
    public CFAMetric computeAA(Double Np, Double Nq, CFAMetric AA,
            TrialMetrics comparisonMetrics, MetricInfo AA_info, Integer taskNum, Integer trialNum) {
        if (AA == null) {
            AA = new CFAMetric(MetricType.AA.toString());
        }

        if (Np != null && Nq != null) {
            //AA computed as Nq - Np, exhibited if Np < Nq (magnitude > 0)
            AA.magnitude = Nq - Np;
            AA.exhibited = checkGreaterThanZeroWithTolerance(AA.magnitude);
        } 

        CFAMetric comparisonAA = null;
        if (comparisonMetrics != null && comparisonMetrics.getAA_metrics() != null) {
            comparisonAA = comparisonMetrics.getAA_metrics();
            AA.pass = AA.exhibited != null && AA.exhibited == comparisonAA.exhibited;
            AA.score = AA.pass ? 100D : 0D;
        }
        AA.assessed = (comparisonAA == null || comparisonAA.assessed == null
                || comparisonAA.assessed) && AA_info.isAssessedForStage(taskNum, trialNum, null);
        return AA;
    }
    
    /**
     * Computes Persistence of Discredited Evidence bias (PDE), computed as Nq - Np,
     * exhibited if magnitude > 0 (Np < Nq).
     * 
     * @param Np
     * @param Nq
     * @param PDE
     * @param comparisonMetrics
     * @param PDE_info
     * @param taskNum
     * @param trialNum
     * @return
     */
    public CFAMetric computePDE(Double Np, Double Nq, CFAMetric PDE,
            TrialMetrics comparisonMetrics, MetricInfo PDE_info, Integer taskNum, Integer trialNum) {        
        if (PDE == null) {
            PDE = new CFAMetric(MetricType.AA.toString());
        }

        if (Np != null && Nq != null) {
            //PDE computed as Nq - Np, exhibited if Np < Nq (magnitude > 0)            
            PDE.magnitude = Nq - Np;
            //System.out.println("PDE Np: " + Np + ", Nq: " + Nq + ", " + (PDE.magnitude > 0));
            PDE.exhibited = checkGreaterThanZeroWithTolerance(PDE.magnitude);
        } 

        CFAMetric comparisonPDE = null;
        if (comparisonMetrics != null && comparisonMetrics.getPDE_metrics() != null) {
            comparisonPDE = comparisonMetrics.getPDE_metrics();
            PDE.pass = PDE.exhibited != null && PDE.exhibited == comparisonPDE.exhibited;
            PDE.score = PDE.pass ? 100D : 0D;
        }
        PDE.assessed = (comparisonPDE == null || comparisonPDE.assessed == null
                || comparisonPDE.assessed) && PDE_info.isAssessedForStage(taskNum, trialNum, null);
        return PDE;
    }    
    
    /**
     * Computes Representativeness bias (RR), computed as P - Q, exhibited if
     * magnitude > 0 (P > Q).
     *
     * @param P
     * @param Q
     * @param RR
     * @param comparisonMetrics
     * @param RR_info
     * @param taskNum
     * @param trialNum
     * @return
     */
    public List<CFAMetric> computeRR(List<Double> P, List<Double> Q, List<CFAMetric> RR,
            TrialMetrics comparisonMetrics, MetricInfo RR_info, Integer taskNum, Integer trialNum) {
        if (P != null && !P.isEmpty() && Q != null && P.size() == Q.size()) {
            int numLocations = P.size() - 1;
            if (RR == null || RR.size() != numLocations) {
                RR = new ArrayList<CFAMetric>(numLocations);
                for (int location = 0; location < numLocations; location++) {
                    RR.add(new CFAMetric(MetricType.RR.toString()));
                }
            }

            List<CFAMetric> comparisonRR = null;
            if (comparisonMetrics != null && comparisonMetrics.getRR_metrics() != null
                    && comparisonMetrics.getRR_metrics().size() == numLocations) {
                comparisonRR = comparisonMetrics.getRR_metrics();
            }

            for (int location = 0; location < numLocations; location++) {
                //RR computed as P - Q, exhibited if magnitude > 0 (P > Q)
                CFAMetric currentRR = RR.get(location);
                Double p = P.get(location);
                Double q = Q.get(location);
                if (p != null && q != null) {
                    currentRR.magnitude = p - q;
                    currentRR.exhibited = checkGreaterThanZeroWithTolerance(currentRR.magnitude);
                }
                CFAMetric currentComparisonRR = null;
                if (comparisonRR != null) {
                    currentComparisonRR = comparisonRR.get(location);
                    if (currentComparisonRR != null) {
                        currentRR.pass = currentRR.exhibited != null
                                && currentRR.exhibited == currentComparisonRR.exhibited;
                        currentRR.score = (currentRR.pass ? 100D : 0D);
                    }
                }                
                currentRR.assessed = (currentComparisonRR == null
                        || currentComparisonRR.assessed == null || currentComparisonRR.assessed)
                        && RR_info.isAssessedForStage(taskNum, trialNum, location);
            }
        }
        return RR;
    }

    /**
     * Computes Availability bias (AV), computed as Nq - Np, 
     * exhibited if magnitude > 0 (Np < Nq).
     * 
     * @param Np
     * @param Nq
     * @param AV
     * @param comparisonMetrics
     * @param AV_info
     * @param taskNum
     * @param trialNum
     * @return
     */
    public CFAMetric computeAV(Double Np, Double Nq, CFAMetric AV,
            TrialMetrics comparisonMetrics, MetricInfo AV_info, Integer taskNum, Integer trialNum) {
        if (AV == null) {
            AV = new CFAMetric(MetricType.AV.toString());
        }

        if (Np != null && Nq != null) {
            //AV computed as Nq - Np (at stage 1), exhibited if magnitude > 0
            AV.magnitude = Nq - Np;
            AV.exhibited = checkGreaterThanZeroWithTolerance(AV.magnitude);
        }

        CFAMetric comparisonAV = null;
        if (comparisonMetrics != null && comparisonMetrics.getAV_metrics() != null) {
            comparisonAV = comparisonMetrics.getAV_metrics();
            AV.pass = (AV.exhibited == comparisonAV.exhibited);
            AV.score = (AV.pass ? 100D : 0D);
        }
        AV.assessed = (comparisonAV == null || comparisonAV.assessed == null || comparisonAV.assessed)
                && AV_info.isAssessedForStage(taskNum, trialNum, 0);
        return AV;
    } 

    /**
     * Computes Probability Matching (PM) as 1 - n, 
     * exhibited if magnitude > 0 (n < 1). 
     * 
     * @param n
     * @param comparison_n
     * @param PM
     * @param comparisonPM
     * @param PM_info
     * @return
     */
    public CFAMetric computePM(Double n, Double comparison_n, CFAMetric PM, 
            CFAMetric comparisonPM, MetricInfo PM_info) {
        if (PM == null) {
            PM = new CFAMetric(MetricType.PM.toString());
        }

        //PM computed as 1 - n, exhibited if magnitude > 0 (n < 1)
        if (n != null) {
            PM.magnitude = 1 - n;
            PM.exhibited = checkGreaterThanZeroWithTolerance(PM.magnitude);
        }

        if (comparison_n != null) {
            //The PM score is computed using MSR, where MSR = max[0, 1 - (|nH - nM| / nH)].            
            PM.score = computeMSR(n, comparison_n);
            PM.pass = null; //No definition of pass using MSR
        }
        PM.assessed = PM_info.isAssessed() && (comparisonPM == null 
                || comparisonPM.assessed == null || comparisonPM.assessed);
        return PM;
    }

    /**
     * Computes Confirmation Bias (CS) as 1 - f, 
     * exhibited if magnitude > 0 (f < 1).
     * 
     * @param f
     * @param comparison_f
     * @param CS
     * @param comparisonCS
     * @param CS_info
     * @return
     */
    public CFAMetric computeCS(Double f, Double comparison_f, CFAMetric CS, 
            CFAMetric comparisonCS, MetricInfo CS_info) {
        if (CS == null) {
            CS = new CFAMetric(MetricType.CS.toString());
        }

        //CS computed as 1 - f, exhibited if magnitude > 0 (f < 1)
        if (f != null) {
            CS.magnitude = 1 - f;
            CS.exhibited = checkGreaterThanZeroWithTolerance(CS.magnitude);
        }

       if (comparison_f != null) {
            //The CS score is computed using MSR, where MSR = max[0, 1 - (|nH - nM| / nH)].            
            CS.score = computeMSR(f, comparison_f);
            CS.pass = null; //No definition of pass using MSR
        }
        CS.assessed = CS_info.isAssessed() && (comparisonCS == null 
                || comparisonCS.assessed == null || comparisonCS.assessed);
        return CS;
    }

    /**
     * Computes Change Blindness (CB) as b - 1, 
     * exhibited if magnitude > 0 (b > 1). 
     * 
     * @param b the number of trials to identify the change in Red tactics
     * @param comparison_b
     * @param CB
     * @param comparisonCB
     * @param CB_info
     * @return
     */
    public CFAMetric computeCB(Double b, Double comparison_b, CFAMetric CB, 
            CFAMetric comparisonCB, MetricInfo CB_info) {
        if (CB == null) {
            CB = new CFAMetric("CB");
        }

        //CB computed as b - 1, exhibited if magnitude > 0 (b > 1)
        if (b != null) {
            CB.magnitude = b - 1;
            CB.exhibited = checkGreaterThanZeroWithTolerance(CB.magnitude);
        }

       if (comparison_b != null) {
            //The CB score is computed using MSR, where MSR = max[0, 1 - (|bH - bM| / bH)].           
            CB.score = computeMSR(b, comparison_b);
            CB.pass = null; //No definition of pass using MSR
        }
        CB.assessed = CB_info.isAssessed() && (comparisonCB == null 
                || comparisonCB.assessed == null || comparisonCB.assessed);
        return CB;
    }

    /**
     * Computes Satisfaction of Search (SS) as 1 - s, 
     * exhibited if magnitude > 0 (s < 1). 
     * 
     * @param s
     * @param comparison_s
     * @param SS
     * @param comparisonSS
     * @param SS_info
     * @return
     */
    public CFAMetric computeSS(Double s, Double comparison_s, CFAMetric SS, 
            CFAMetric comparisonSS, MetricInfo SS_info) {
        if (SS == null) {
            SS = new CFAMetric(MetricType.SS.toString());
        }

        //SS computed as 1 - s, exhibited if magnitude > 0 (s < 1)
        if (s != null) {
            SS.magnitude = 1 - s;
            SS.exhibited = checkGreaterThanZeroWithTolerance(SS.magnitude);
        }

       if (comparison_s != null) {
            //The SS score is computed using MSR, where MSR = max[0, 1 - (|nH - nM| / nH)].            
            SS.score = computeMSR(s, comparison_s);
            SS.pass = null; //No definition of pass using MSR
        }
        SS.assessed = SS_info.isAssessed() && (comparisonSS == null 
                || comparisonSS.assessed == null || comparisonSS.assessed);
        return SS;
    }
    
    /**
     * Returns true if the value is greater than 0.00001.
     * 
     * @param value
     * @return
     */
    public static boolean checkGreaterThanZeroWithTolerance(double value) {
        return value > 0.00001d;
    }
   
    /**
     * Computes MSR (Marginal Success Rate) given Nm (model value) and Nh (human value).
     * 
     * @param Nm
     * @param Nh
     * @return
     */
    public static Double computeMSR(Double Nm, Double Nh) {
        if(Nm != null && Nh != null) {
            return Math.max(0.d, 1.d - (Math.abs(Nh - Nm) / Nh)) * 100.d;
        }
        return null;
    }    
    
    public static void main(String[] args) {
        System.out.println(computeMSR(3.d, 6.d));
    }
}
