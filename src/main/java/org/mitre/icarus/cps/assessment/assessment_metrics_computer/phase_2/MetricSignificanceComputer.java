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
import org.mitre.icarus.cps.assessment.assessment_metrics_computer.common.TTest;
import org.mitre.icarus.cps.assessment.assessment_metrics_computer.common.TTest.TailType;
import org.mitre.icarus.cps.assessment.assessment_metrics_computer.common.TTest.VarianceType;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.CFAMetric;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.CFAMetricSignificanceReport;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.MetricSignificance;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.TrialIdentifier;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.significance.MissionSignificanceReport;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.MissionMetrics;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.NegentropyMetrics;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.TrialData;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.TrialMetrics;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.significance.ExamSignificanceReport;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.significance.TrialSignificanceReport;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.trial_part_data.AttackProbabilityReportData;
import org.mitre.icarus.cps.assessment.data_model.phase_2.data_sets.AverageHumanDataSet_Phase2;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbeType;

/**
 * Computes whether each Phase 2 CFA metric is statistically significant given
 * an average human data set.
 *
 * @author CBONACETO
 */
public class MetricSignificanceComputer {

    /**
     * Computes whether each Phase 2 CFA metric is statistically significant
     * given an average human data set.
     *
     * @param dataSet
     * @return
     */
    public static ExamSignificanceReport computeMetricSignificance(
            AverageHumanDataSet_Phase2 dataSet, double pCrit) {
        if (dataSet.getTaskMetrics() != null && !dataSet.getTaskMetrics().isEmpty()) {
            ExamSignificanceReport examReport = new ExamSignificanceReport(dataSet.getExam_id());
            examReport.setTime_stamp(System.currentTimeMillis());
            examReport.setMissionSignificanceReports(new ArrayList<MissionSignificanceReport>(
                    dataSet.getTaskMetrics().size()));
            for (MissionMetrics missionMetrics : dataSet.getTaskMetrics()) {
                if (missionMetrics.getNum_subjects() != null && missionMetrics.getNum_subjects() > 0) {
                    int numSubjects = missionMetrics.getNum_subjects();
                    MissionSignificanceReport missionReport = new MissionSignificanceReport(
                            missionMetrics.getTask_number(), missionMetrics.getTask_id(), numSubjects);
                    examReport.getMissionSignificanceReports().add(missionReport);

                    //***** Compute significance of mission-level metrics ******
                    //Compute significance of PM (Probability Matching)
                    if (missionMetrics.getPM_normativeBlueOptionSelectionFrequency() != null
                            && missionMetrics.getPM_normativeBlueOptionSelectionFrequency_std() != null) {
                        //Determine whether nH is significanctly less than 1, using a One-Way, One-Tailed T-Test
                        //oneSampleTTest(Double mean1, Double s1, int n1, Double u, TTest.TailType tailType)
                        Double p = TTest.oneSampleTTest(
                                missionMetrics.getPM_normativeBlueOptionSelectionFrequency(),
                                missionMetrics.getPM_normativeBlueOptionSelectionFrequency_std(),
                                numSubjects, 1.d, TailType.Left);
                        MetricSignificance significance = new MetricSignificance(
                                "One-Sample, One-Way T-Test", p, pCrit, p < pCrit);
                        //CFAMetric metric, Double humanValue,Double comparisonValue, TrialIdentifier trial, MetricSignificance significance
                        missionReport.setPM_significance(new CFAMetricSignificanceReport(
                                missionMetrics.getPM_metrics(),
                                missionMetrics.getPM_normativeBlueOptionSelectionFrequency(),
                                1.d, null, significance));
                        if (missionMetrics.getPM_metrics() != null) {
                            missionMetrics.getPM_metrics().setSignificance_1(significance);
                        }
                    }

                    //Compute significance of CS (Confirmation Bias)
                    if (missionMetrics.getCS_sigintHighestPaSelectionFrequency() != null
                            && missionMetrics.getCS_sigintHighestPaSelectionFrequency_std() != null) {
                        //Determine whether fH is significanctly less than 1, using a One-Way, One-Tailed T-Test
                        Double p = TTest.oneSampleTTest(
                                missionMetrics.getCS_sigintHighestPaSelectionFrequency(),
                                missionMetrics.getCS_sigintHighestPaSelectionFrequency_std(),
                                numSubjects, 1.d, TailType.Left);
                        MetricSignificance significance = new MetricSignificance(
                                "One-Sample, One-Way T-Test", p, pCrit, p < pCrit);
                        missionReport.setCS_significance(new CFAMetricSignificanceReport(
                                missionMetrics.getCS_metrics(),
                                missionMetrics.getCS_sigintHighestPaSelectionFrequency(),
                                1.d, null, significance));
                        if (missionMetrics.getCS_metrics() != null) {
                            missionMetrics.getCS_metrics().setSignificance_1(significance);
                        }
                    }

                    //Compute significance of SS (Satisfaction of Search)
                    if (missionMetrics.getSS_percentTrialsReviewedInBatchPlot_avg() != null
                            && missionMetrics.getSS_percentTrialsReviewedInBatchPlot_avg_std() != null) {
                        //Determine whether sH is significanctly less than 1, using a One-Sample, One-Tailed T-Test                        
                        Double p = TTest.oneSampleTTest(
                                missionMetrics.getSS_percentTrialsReviewedInBatchPlot_avg(),
                                missionMetrics.getSS_percentTrialsReviewedInBatchPlot_avg_std(),
                                numSubjects, 1.d, TailType.Left);
                        MetricSignificance significance = new MetricSignificance(
                                "One-Sample, One-Way T-Test", p, pCrit, p < pCrit);
                        missionReport.setSS_significance(new CFAMetricSignificanceReport(
                                missionMetrics.getSS_metrics(),
                                missionMetrics.getSS_percentTrialsReviewedInBatchPlot_avg(),
                                1.d, null, significance));
                        if (missionMetrics.getSS_metrics() != null) {
                            missionMetrics.getSS_metrics().setSignificance_1(significance);
                        }
                    }

                    //Compute significance of CB (Change Blindness)
                    //TODO: Add this
                    
                    //***** Comptue significance of trial-level metrics ******
                    if (missionMetrics.getTrials() != null && !missionMetrics.getTrials().isEmpty()) {
                        List<TrialSignificanceReport> trialReports = new ArrayList<TrialSignificanceReport>(
                                missionMetrics.getTrials().size());
                        missionReport.setTrialSignificanceReports(trialReports);
                        for (TrialData trial : missionMetrics.getTrials()) {
                            TrialSignificanceReport trialReport = new TrialSignificanceReport();
                            trialReports.add(trialReport);
                            TrialMetrics trialMetrics = trial.getMetrics();
                            if (trialMetrics != null) {
                                NegentropyMetrics neMetrics = trialMetrics.getNegentropyMetrics();
                                //Compute significance of AA (Anchoring and Adjustment)
                                //System.out.println(neMetrics.getNp_Ptpc() + ", " + neMetrics.getNp_Ptpc_std() + ", " + neMetrics.getNq_Ptpc() + ", " + neMetrics.getNq_Ptpc_std());
                                if (neMetrics != null && neMetrics.getNp_Ptpc() != null
                                        && neMetrics.getNp_Ptpc_std() != null
                                        && neMetrics.getNq_Ptpc() != null
                                        && neMetrics.getNq_Ptpc_std() != null) {
                                    //Determine whether Np_Ptpc is significantly less than Nq_Ptpc using a Two-Sample, One-Tailed T-Test
                                    //twoSampleTTest(Double mean1, Double s1, int n1, Double mean2, Double s2, int n2, TailType tailType, VarianceType varType)                                     
                                    Double p = TTest.twoSampleTTest(neMetrics.getNp_Ptpc(),
                                            neMetrics.getNp_Ptpc_std(), numSubjects,
                                            neMetrics.getNq_Ptpc(), neMetrics.getNq_Ptpc_std(), numSubjects,
                                            TailType.Left, VarianceType.Equal);
                                    MetricSignificance significance = new MetricSignificance(
                                            "Two-Sample, One-Way T-Test", p, pCrit, p < pCrit);
                                    trialReport.setAA_significance(new CFAMetricSignificanceReport(
                                            trialMetrics.getAA_metrics(),
                                            neMetrics.getNp_Ptpc(), neMetrics.getNq_Ptpc(),
                                            new TrialIdentifier(missionMetrics.getTask_number(), trial.getTrial_number(), null),
                                            significance));
                                    if (trialMetrics.getAA_metrics() != null) {
                                        trialMetrics.getAA_metrics().setSignificance_1(significance);
                                    }
                                }

                                //Compute significance of PDE (Persistence of Discredited Evidence)                                
                                if (neMetrics != null && neMetrics.getNp_redTacticProbs() != null
                                        && neMetrics.getNp_redTacticProbs_std() != null
                                        && neMetrics.getNq_redTacticProbs() != null
                                        && neMetrics.getNq_redTacticProbs_std() != null) {
                                    //Determine whether Np_redTacticProbs is significantly less than 
                                    //Nq_redTacicProbs using a Two-Sample, One-Tailed T-Test                                    
                                    Double p = TTest.twoSampleTTest(neMetrics.getNp_redTacticProbs(),
                                            neMetrics.getNp_redTacticProbs_std(), numSubjects,
                                            neMetrics.getNq_redTacticProbs(),
                                            neMetrics.getNq_redTacticProbs_std(), numSubjects,
                                            TailType.Left, VarianceType.Equal);
                                    MetricSignificance significance = new MetricSignificance(
                                            "Two-Sample, One-Way T-Test", p, pCrit, p < pCrit);
                                    trialReport.setPDE_significance(new CFAMetricSignificanceReport(
                                            trialMetrics.getPDE_metrics(),
                                            neMetrics.getNp_redTacticProbs(),
                                            neMetrics.getNq_redTacticProbs(),
                                            new TrialIdentifier(missionMetrics.getTask_number(), trial.getTrial_number(), null),
                                            significance));
                                    if (trialMetrics.getPDE_metrics() != null) {
                                        trialMetrics.getPDE_metrics().setSignificance_1(significance);
                                    }
                                }

                                //Compute significance of RR (Representativeness)
                                //participantProbsPpcStage, normativeProbsPpcStage
                                if (trial.getAttackProbabilityReports() != null
                                        && !trial.getAttackProbabilityReports().isEmpty()) {
                                    AttackProbabilityReportData attackProbabilityReport_Ppc = null;
                                    for (AttackProbabilityReportData probs : trial.getAttackProbabilityReports()) {
                                        if (probs.getTrialPartType() != null
                                                && probs.getTrialPartType()
                                                == TrialPartProbeType.AttackProbabilityReport_Ppc) {
                                            attackProbabilityReport_Ppc = probs;
                                            break;
                                        }
                                    }
                                    if (attackProbabilityReport_Ppc != null
                                            && TrialMetricsComputer.checkProbsNotNullOrEmpty(
                                                    attackProbabilityReport_Ppc.getProbabilities())
                                            && TrialMetricsComputer.checkProbsNotNullOrEmpty(
                                                    attackProbabilityReport_Ppc.getProbabilities_std())
                                            && TrialMetricsComputer.checkProbsNotNullOrEmpty(
                                                    attackProbabilityReport_Ppc.getProbabilities_normative_incremental())
                                            && TrialMetricsComputer.checkProbsNotNullOrEmpty(
                                                    attackProbabilityReport_Ppc.getProbabilities_normative_incremental_std())) {
                                        int numLocations = attackProbabilityReport_Ppc.getProbabilities().size() - 1;
                                        List<CFAMetricSignificanceReport> rrSignifiance
                                                = new ArrayList<CFAMetricSignificanceReport>(numLocations);
                                        trialReport.setRR_significance(rrSignifiance);
                                        for (int i = 0; i < numLocations; i++) {
                                            Double Ppc = attackProbabilityReport_Ppc.getProbabilities().get(i);
                                            Double Ppc_std = attackProbabilityReport_Ppc.getProbabilities_std().get(i);
                                            Double Qpc = attackProbabilityReport_Ppc.getProbabilities_normative_incremental().get(i);
                                            Double Qpc_std = attackProbabilityReport_Ppc.getProbabilities_normative_incremental_std().get(i);
                                            //Determine whether participant Ppc is significantly greater than normative Ppc 
                                            //using a Two-Sample, One-Tailed T-Test
                                            Double p = TTest.twoSampleTTest(Ppc,
                                                    Ppc_std, numSubjects, Qpc,
                                                    Qpc_std, numSubjects,
                                                    TailType.Right, VarianceType.Equal);
                                            MetricSignificance significance = new MetricSignificance(
                                                    "Two-Sample, One-Way T-Test", p, pCrit, p < pCrit);
                                            CFAMetric rrMetrics = null;
                                            if (trialMetrics.getRR_metrics() != null && i < trialMetrics.getRR_metrics().size()) {
                                                rrMetrics = trialMetrics.getRR_metrics().get(i);
                                            }
                                            rrSignifiance.add(new CFAMetricSignificanceReport(
                                                    rrMetrics, Ppc, Qpc,
                                                    new TrialIdentifier(missionMetrics.getTask_number(), trial.getTrial_number(), null),
                                                    significance));
                                            if (rrMetrics != null) {
                                                rrMetrics.setSignificance_1(significance);
                                            }
                                        }
                                    }
                                }

                                //Compute significance of AV (Availability)
                                if (neMetrics != null && neMetrics.getNp_Pt() != null
                                        && neMetrics.getNp_Pt_std() != null
                                        && neMetrics.getNq_Pt() != null
                                        && neMetrics.getNq_Pt_std() != null) {
                                    //Determine whether Np_pt is significantly less than 
                                    //Nq_Pt using a Two-Sample, One-Tailed T-Test                                    
                                    Double p = TTest.twoSampleTTest(neMetrics.getNp_Pt(),
                                            neMetrics.getNp_Pt_std(), numSubjects,
                                            neMetrics.getNq_Pt(),
                                            neMetrics.getNq_Pt_std(), numSubjects,
                                            TailType.Left, VarianceType.Equal);
                                    MetricSignificance significance = new MetricSignificance(
                                            "Two-Sample, One-Way T-Test", p, pCrit, p < pCrit);
                                    trialReport.setAV_significance(new CFAMetricSignificanceReport(
                                            trialMetrics.getAV_metrics(),
                                            neMetrics.getNp_Pt(),
                                            neMetrics.getNq_Pt(),
                                            new TrialIdentifier(missionMetrics.getTask_number(), trial.getTrial_number(), null),
                                            significance));
                                    if (trialMetrics.getAV_metrics() != null) {
                                        trialMetrics.getAV_metrics().setSignificance_1(significance);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return examReport;
        }
        return null;
    }
}