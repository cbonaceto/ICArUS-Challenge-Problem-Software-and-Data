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
import org.mitre.icarus.cps.assessment.assessment_metrics_computer.ITrialMetricsComputer;
import org.mitre.icarus.cps.assessment.assessment_metrics_computer.MetricUtils;
import org.mitre.icarus.cps.assessment.assessment_metrics_computer.MetricUtils.DistributionStats;
import org.mitre.icarus.cps.assessment.data_model.base.DataType;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.CPAMetric;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.BlueActionSelectionFrequency;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.MetricsInfo;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.NegentropyMetrics;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.RSRAndASRTrialMetrics;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.SigintSelectionFrequency;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.TrialData;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.TrialMetrics;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.trial_part_data.AttackProbabilityReportData;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.trial_part_data.ProbabilityReportData;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.trial_part_data.RedTacticsReportData;
import org.mitre.icarus.cps.assessment.score_computer.ScoreComputerBase;
import org.mitre.icarus.cps.assessment.score_computer.ScoreComputerBase.ProbabilityType;
import org.mitre.icarus.cps.assessment.score_computer.phase_2.ScoreComputer_Phase2;
import org.mitre.icarus.cps.exam.phase_2.testing.bluebook.RedTacticType;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.BlueAction;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbeType;

/**
 * Computes Phase 2 trial metrics.
 *
 * @author CBONACETO
 *
 */
public class TrialMetricsComputer extends MetricsComputer
        implements ITrialMetricsComputer<TrialMetrics, TrialData, MetricsInfo> {

    /* (non-Javadoc)
     * @see org.mitre.icarus.cps.assessment.assessment_metrics_computer.ITrialMetricsComputer#updateTrialMetrics(org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.AbstractTrialData, org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.AbstractMetricsInfo, org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.AbstractTrialData)
     */
    @Override   
    public TrialMetrics updateTrialMetrics(TrialData trialData,
            MetricsInfo metricsInfo, TrialData comparisonTrialData) {
        if (trialData == null) {
            return null;
        }
        if (metricsInfo == null) {
            metricsInfo = MetricsInfo.createDefaultMetricsInfo();
        }

        TrialMetrics trialMetrics = trialData.getMetrics();
        if (trialMetrics == null) {
            trialMetrics = new TrialMetrics();
            trialData.setMetrics(trialMetrics);
        }
        TrialMetrics comparisonMetrics = comparisonTrialData != null ? comparisonTrialData.getMetrics() : null;

        trialMetrics.setCfa_metrics_stale(true);
        trialMetrics.setCpa_metrics_stale(true);

        int taskNum = trialData.getTask_number();
        int trialNum = trialData.getTrial_number();

        //Compute negentropies and delta negentropies of participant and normative attack probabilities 
        //(for an average trial, these are the mean participant and normative probabilities)
        NegentropyMetrics neMetrics = trialMetrics.getNegentropyMetrics();
        List<Double> participantProbsPpcStage = null;
        List<Double> normativeProbsPpcStage = null;
        if (trialData.getAttackProbabilityReports() != null
                && !trialData.getAttackProbabilityReports().isEmpty()) {
            if (neMetrics == null) {
                neMetrics = new NegentropyMetrics();
                trialMetrics.setNegentropyMetrics(neMetrics);
            }
            ArrayList<Double> Np_delta = new ArrayList<Double>();
            ArrayList<Double> Nq_delta = new ArrayList<Double>();
            neMetrics.setNp_delta(Np_delta);
            neMetrics.setNq_delta(Nq_delta);
            Double lastNp = null;
            Double lastNq = null;
            for (AttackProbabilityReportData probs : trialData.getAttackProbabilityReports()) {
                List<Double> currentParticipantProbs = probs.getProbabilities();
                List<Double> currentNormativeProbs = probs.getProbabilities_normative_incremental();
                if (probs.getTrialPartType() != null) {
                    Double np = currentParticipantProbs != null
                            ? ScoreComputerBase.computeNegentropy(
                                    currentParticipantProbs, ProbabilityType.Decimal) : null;
                    Double nq = currentNormativeProbs != null
                            ? ScoreComputerBase.computeNegentropy(
                                    currentNormativeProbs, ProbabilityType.Decimal) : null;
                    switch (probs.getTrialPartType()) {
                        case AttackProbabilityReport_Pp:
                            neMetrics.setNp_Pp(np);
                            neMetrics.setNq_Pp(nq);
                            break;
                        case AttackProbabilityReport_Ppc:
                            neMetrics.setNp_Ppc(np);
                            neMetrics.setNq_Ppc(nq);
                            participantProbsPpcStage = currentParticipantProbs;
                            normativeProbsPpcStage = currentNormativeProbs;
                            break;
                        case AttackProbabilityReport_Pt:
                            neMetrics.setNp_Pt(np);
                            neMetrics.setNq_Pt(nq);
                            break;
                        case AttackProbabilityReport_Ptpc:
                            neMetrics.setNp_Ptpc(np);                            
                            neMetrics.setNq_Ptpc(nq);                            
                            break;
                    }
                    if (probs.getTrialPartType() != TrialPartProbeType.AttackProbabilityReport_Pt) {
                        //Compute delta Ne
                        Np_delta.add(np != null && lastNp != null ? np - lastNp : 0d);
                        Nq_delta.add(nq != null && lastNq != null ? nq - lastNq : 0d);
                        lastNp = np;
                        lastNq = nq;
                    }
                }
            }
        }

        //Compute Anchoring & Adjustment (AA) metrics
        if (metricsInfo.getAA_info() != null && metricsInfo.getAA_info().isAssessedForTask(taskNum)
                && metricsInfo.getAA_info().isCalculated()) {
            trialMetrics.setAA_metrics(cfaMetricsComputer.computeAA(neMetrics.getNp_Ptpc(),
                    neMetrics.getNq_Ptpc(), trialMetrics.getAA_metrics(),
                    comparisonMetrics, metricsInfo.getAA_info(), taskNum, trialNum));
        }

        //Compute Representativeness (RR) metrics
        if (metricsInfo.getRR_info() != null && metricsInfo.getRR_info().isAssessedForTask(taskNum)
                && metricsInfo.getRR_info().isCalculated()) {
            trialMetrics.setRR_metrics(cfaMetricsComputer.computeRR(
                    participantProbsPpcStage, normativeProbsPpcStage,
                    trialMetrics.getRR_metrics(), comparisonMetrics,
                    metricsInfo.getRR_info(), taskNum, trialNum));
        }

        //Compute Availability (AV) metrics
        if (metricsInfo.getAV_info() != null && metricsInfo.getAV_info().isAssessedForTask(taskNum)
                && metricsInfo.getAV_info().isCalculated()) {
            trialMetrics.setAV_metrics(cfaMetricsComputer.computeAV(neMetrics.getNp_Pt(),
                    neMetrics.getNq_Pt(), trialMetrics.getAV_metrics(), comparisonMetrics,
                    metricsInfo.getAV_info(), taskNum, trialNum));
        }

        //Compute normative Blue action selection frequencies and Blue action selection frequencies
        //for Probability Matching (PM) metrics and RMR metrics (not computed for an average trial)        
        Integer blueActionsIndex = null;
        if (!DataType.isAvgData(trialData.getData_type()) && trialData.getBlueActionSelection() != null
                && trialData.getBlueActionSelection().getBlueActions() != null
                && !trialData.getBlueActionSelection().getBlueActions().isEmpty()) {
            ArrayList<BlueActionSelectionFrequency> frequencyBlueActionsSelected = null;
            List<List<BlueAction>> blueActionPermutations
                    = ScoreComputer_Phase2.enumerateBlueActionPermutations_locationIds(trialData.getBlueLocationIds());
            if (blueActionPermutations != null) {
                frequencyBlueActionsSelected = new ArrayList<BlueActionSelectionFrequency>(
                        blueActionPermutations.size());
                for (List<BlueAction> blueActions : blueActionPermutations) {
                    frequencyBlueActionsSelected.add(new BlueActionSelectionFrequency(blueActions, 0.d, 0.d));
                }
            }
            trialMetrics.setRMR_frequencyBlueActionsSelected(frequencyBlueActionsSelected);
            //Determine the index of the Blue action permutation that was selected
            blueActionsIndex = ScoreComputer_Phase2.getBlueActionPermutationIndex_BlueActionType(
                    trialData.getBlueActionSelection().getBlueActions());
            //Determine the index of the normative Blue action permutation (based on the participant probabilities)
            Integer normativeBlueActionsIndex = ScoreComputer_Phase2.getBlueActionPermutationIndex_BlueActionType(
                    trialData.getBlueActionSelection().getBlueActions_normativeParticipant());
            if (blueActionsIndex != null) {
                if (frequencyBlueActionsSelected != null && blueActionsIndex < frequencyBlueActionsSelected.size()) {
                    frequencyBlueActionsSelected.get(blueActionsIndex).setSelectionCount(1.d);
                    frequencyBlueActionsSelected.get(blueActionsIndex).setSelectionPercent(1.d);
                }
                if (normativeBlueActionsIndex != null) {
                    trialMetrics.setPM_normativeBlueOptionSelections(
                            blueActionsIndex.equals(normativeBlueActionsIndex) ? 1.d : 0.d);
                } else {
                    trialMetrics.setPM_normativeBlueOptionSelections(null);
                }
            } else {
                trialMetrics.setPM_normativeBlueOptionSelections(null);
            }
        }

        //Compute frequencies SIGINT selected at locations with the highest P(Attack) for
        //Confirmative Bias (CS) metrics and overall selection frequencies at each location for RMR metrics (not computed for average trial)
        Integer sigintLocationIndex = null;
        if (!DataType.isAvgData(trialData.getData_type()) && trialData.getSigintSelection() != null
                && trialData.getSigintSelection().getSigintLocations() != null
                && !trialData.getSigintSelection().getSigintLocations().isEmpty()
                && trialData.getBlueLocationIds() != null
                && !trialData.getBlueLocationIds().isEmpty()) {
            int numLocations = trialData.getBlueLocationIds().size();
            ArrayList<SigintSelectionFrequency> freqnecySigintSelectedAtEachLocation
                    = new ArrayList<SigintSelectionFrequency>(numLocations);
            trialMetrics.setRMR_frequencySigintSelectedAtEachLocation(freqnecySigintSelectedAtEachLocation);
            String sigintLocation = trialData.getSigintSelection().getSigintLocations().get(0);
            String normativeSigintLocation = trialData.getSigintSelection().getSigintLocationsParticipantOptimal() != null
                    && !trialData.getSigintSelection().getSigintLocationsParticipantOptimal().isEmpty()
                    ? trialData.getSigintSelection().getSigintLocationsParticipantOptimal().get(0) : null;
            if (sigintLocation != null) {
                int i = 0;
                for (String locationId : trialData.getBlueLocationIds()) {
                    if (sigintLocation.equalsIgnoreCase(locationId)) {
                        sigintLocationIndex = i;
                        freqnecySigintSelectedAtEachLocation.add(new SigintSelectionFrequency(
                                locationId, 1.d, 1.d));
                    } else {
                        freqnecySigintSelectedAtEachLocation.add(new SigintSelectionFrequency(
                                locationId, 0.d, 0.d));
                    }
                    i++;
                }
                if (normativeSigintLocation != null) {
                    trialMetrics.setCS_sigintAtHighestPaLocationSelections(
                            sigintLocation.equalsIgnoreCase(normativeSigintLocation) ? 1.d : 0.d);
                } else {
                    trialMetrics.setCS_sigintAtHighestPaLocationSelections(null);
                }
            } else {
                for (String locationId : trialData.getBlueLocationIds()) {
                    freqnecySigintSelectedAtEachLocation.add(
                            new SigintSelectionFrequency(locationId, null, null));
                }
                trialMetrics.setCS_sigintAtHighestPaLocationSelections(null);
            }
        }

        //Compute Change Blindness (CB) and Persistence of Discredited Evidence (PDE) metrics
        if (trialData.getRedTacticsReport() != null
                && checkProbsNotNullOrEmpty(trialData.getRedTacticsReport().getRedTacticProbabilities())) {
            //Compute negentropy of participant and normative Red tactic probabilities
            //(for an average trial, these are the mean participant and normative Red tactic probabilities)
            List<Double> participantProbs = trialData.getRedTacticsReport().getRedTacticProbabilities();
            List<Double> normativeProbs = trialData.getRedTacticsReport().getRedTacticProbabilities_normative();            
            neMetrics.setNp_redTacticProbs(participantProbs != null
                    ? ScoreComputerBase.computeNegentropy(participantProbs, ProbabilityType.Decimal) : null);
            neMetrics.setNq_redTacticProbs(normativeProbs != null
                    ? ScoreComputerBase.computeNegentropy(normativeProbs, ProbabilityType.Decimal) : null);

            //Compute Persistence of Discredited Evidence (PDE) metrics            
            if (metricsInfo.getPDE_info() != null && metricsInfo.getPDE_info().isAssessedForTask(taskNum)
                    && metricsInfo.getPDE_info().isCalculated()) {
                trialMetrics.setPDE_metrics(cfaMetricsComputer.computePDE(
                        neMetrics.getNp_redTacticProbs(), neMetrics.getNq_redTacticProbs(),
                        trialMetrics.getPDE_metrics(), comparisonMetrics,
                        metricsInfo.getPDE_info(), taskNum, trialNum));
            }

            //Compute whether the Red tactic was correctly detected for Change Blindness (CB) metrics             
            List<RedTacticType> possibleTactics = trialData.getRedTacticsReport().getPossibleRedTactics();
            RedTacticType actualTactic = trialData.getRedTacticsReport().getActualRedTactic();
            trialMetrics.setCB_redTacticCorrectlyDetected(isRedTacticCorrectlyDetected(
                    participantProbs, possibleTactics, actualTactic));
        }

        //Compute Satisfaction of Search (SS) metrics (not computed for an average trial)
        if (!DataType.isAvgData(trialData.getData_type()) && trialData.getRedTacticsReport() != null
                && trialData.getRedTacticsReport().getBatchPlotNumTrialsToReview() != null
                && trialData.getRedTacticsReport().getBatchPlotNumTrialsToReview() > 0) {
            //System.out.println("SS: " + trialData.getRedTacticsReport().getBatchPlotNumTrialsReviewed() + ", " + 
             //       trialData.getRedTacticsReport().getBatchPlotNumTrialsToReview());
            //System.out.println("Num batch plots reviewed: " + trialData.getRedTacticsReport().getBatchPlotNumTrialsReviewed());
            double batchPlotNumTrialsReviewed = trialData.getRedTacticsReport().getBatchPlotNumTrialsReviewed() != null
                    ? trialData.getRedTacticsReport().getBatchPlotNumTrialsReviewed() : 0.d;
            trialMetrics.setSS_percentTrialsReviewedInBatchPlot(
                    batchPlotNumTrialsReviewed / trialData.getRedTacticsReport().getBatchPlotNumTrialsToReview());
        }

        if (comparisonMetrics != null) {
            //Compute Relative Success Rate (RSR) and Absoulte Success Rate (ASR) metrics
            //RSR and ASR are computed at any stage where probablities are entered, which include
            //Red tatctics probability reports and attack probability reports
            computeRSRandASRMetrics(trialData, comparisonTrialData, metricsInfo, taskNum, trialNum);

            //Compute Relative Match Rate (RMR) metrics
            //RMR is computed using both SIGINT selections (if available) and Blue action selections
            //protected List<SigintSelectionFrequency> RMR_freqnecySigintSelectedAtEachLocation;
            //protected List<BlueActionSelectionFrequency> RMR_frequencyBlueActionsSelected;
            //protected CPAMetric RMR_metric;
            if (metricsInfo.getRMR_info() != null && metricsInfo.getRMR_info().isAssessedForTask(taskNum)
                    && metricsInfo.getRMR_info().isCalculated()) {
                if (sigintLocationIndex != null) {
                    //Compute RMR based on SIGINT selections
                    CPAMetric rmr = cpaMetricsComputer.computeRMR_sigint(sigintLocationIndex,
                            comparisonMetrics.getRMR_frequencySigintSelectedAtEachLocation(),
                            trialMetrics.getRMR_sigint_metrics(), metricsInfo.getRMR_info(),
                            taskNum, trialNum);
                    trialMetrics.setRMR_sigint_metrics(rmr);
                }
                if (blueActionsIndex != null) {
                    //Compute RMR based on the Blue action selection
                    CPAMetric rmr = cpaMetricsComputer.computeRMR_blueActions(blueActionsIndex,
                            comparisonMetrics.getRMR_frequencyBlueActionsSelected(),
                            trialMetrics.getRMR_blueAction_metrics(), metricsInfo.getRMR_info(),
                            taskNum, trialNum);
                    trialMetrics.setRMR_blueAction_metrics(rmr);
                }

            }
        }

        trialMetrics.setCfa_metrics_stale(false);
        trialMetrics.setCpa_metrics_stale(false);
        return trialMetrics;
    }

    /**
     * Returns true if the given probabilities list is not null, not empty, 
     * and contains no null values, or false otherwise.
     * 
     * @param probs
     * @return
     */
    public static boolean checkProbsNotNullOrEmpty(List<Double> probs) {
        if(probs != null && !probs.isEmpty()) {
            for(Double prob : probs) {
                if(prob == null) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Get the number of attack probability report stages (the Pt stage is not
     * counted).
     *
     * @param attackProbabilityReports
     * @return
     */
    protected static int getNumStagesForNegentropyMetrics(List<AttackProbabilityReportData> attackProbabilityReports) {
        int numStages = 0;
        if (attackProbabilityReports != null && !attackProbabilityReports.isEmpty()) {
            for (AttackProbabilityReportData report : attackProbabilityReports) {
                if (report.getTrialPartType() == null
                        || report.getTrialPartType() != TrialPartProbeType.AttackProbabilityReport_Pt) {
                    numStages++;
                }
            }
        }
        return numStages;
    }

    /**
     * Gets the probability report stages used in RSR and ASR metrics (the Pt
     * attack probability stage is not used).
     *
     * @param redTacticsReport
     * @param attackProbabilityReports
     * @return
     */
    protected static List<ProbabilityReportData> getProbalityReportsForRSRandASRMetrics(
            RedTacticsReportData redTacticsReport,
            List<AttackProbabilityReportData> attackProbabilityReports) {
        ArrayList<ProbabilityReportData> probabilityReports = new ArrayList<ProbabilityReportData>();
        if (redTacticsReport != null
                && redTacticsReport.getTrialPartType() == TrialPartProbeType.RedTacticsProbabilityReport) {
            ProbabilityReportData redTacticsProbs = new ProbabilityReportData();
            redTacticsProbs.setProbabilitiesId("P_RedStyle");
            redTacticsProbs.setProbabilitiesName("P(Red Styles)");
            redTacticsProbs.setProbabilities(redTacticsReport.getRedTacticProbabilities());
            redTacticsProbs.setProbabilities_normative(redTacticsReport.getRedTacticProbabilities_normative());
            redTacticsProbs.setProbabilities_normative_incremental(redTacticsReport.getRedTacticProbabilities_normative());
            probabilityReports.add(redTacticsProbs);
        }
        if (attackProbabilityReports != null && !attackProbabilityReports.isEmpty()) {
            for (AttackProbabilityReportData report : attackProbabilityReports) {
                //if(report.getTrialPartType() == null || 
                //        report.getTrialPartType() != TrialPartProbeType.AttackProbabilityReport_Pt) {
                probabilityReports.add(report);
                //}                    
            }
        }
        return probabilityReports;
    }

    /**
     * Compute RSR and ASR metrics for the trial.
     *
     * @param trialData
     * @param comparisonTrialData
     * @param metricsInfo
     * @param taskNum
     * @param trialNum
     */
    protected void computeRSRandASRMetrics(TrialData trialData, TrialData comparisonTrialData,
            MetricsInfo metricsInfo, Integer taskNum, Integer trialNum) {
        if (trialData != null && trialData.getMetrics() != null
                && comparisonTrialData != null && comparisonTrialData.getMetrics() != null) {
            List<ProbabilityReportData> probs = getProbalityReportsForRSRandASRMetrics(
                    trialData.getRedTacticsReport(), trialData.getAttackProbabilityReports());
            int numStages = probs != null ? probs.size() : 0;
            List<ProbabilityReportData> comparisonProbs = getProbalityReportsForRSRandASRMetrics(
                    comparisonTrialData.getRedTacticsReport(), 
                    comparisonTrialData.getAttackProbabilityReports());
            if (probs != null && checkStagesMatch(probs, comparisonProbs)
                    && metricsInfo.getRSR_info().isAssessedForTask(taskNum)
                    && metricsInfo.getRSR_info().isCalculated()) {
                RSRAndASRTrialMetrics rsrAsrMetrics = trialData.getMetrics().getRSRandASR_metrics();
                if (rsrAsrMetrics == null) {
                    rsrAsrMetrics = new RSRAndASRTrialMetrics();
                    trialData.getMetrics().setRSRandASR_metrics(rsrAsrMetrics);
                }
                rsrAsrMetrics.setStageNames(getStageNames(probs));

                //Compute RSR metrics               
                List<Double> Spm = rsrAsrMetrics.getSpm();
                if (Spm == null || Spm.size() != numStages) {
                    Spm = new ArrayList<Double>();
                    rsrAsrMetrics.setSpm(Spm);
                    for (int stage = 0; stage < numStages; stage++) {
                        Spm.add(null);
                    }
                }
                List<Double> Spr = rsrAsrMetrics.getSpr();
                if (Spr == null || Spr.size() != numStages) {
                    Spr = new ArrayList<Double>();
                    rsrAsrMetrics.setSpr(Spr);
                    for (int stage = 0; stage < numStages; stage++) {
                        Spr.add(null);
                    }
                }
                List<CPAMetric> RSR = rsrAsrMetrics.getRSR();
                if (RSR == null || RSR.size() != numStages) {
                    RSR = new ArrayList<CPAMetric>(numStages);
                    rsrAsrMetrics.setRSR(RSR);
                    String name = metricsInfo.getRSR_info().getName();
                    for (int stage = 0; stage < numStages; stage++) {
                        RSR.add(new CPAMetric(name));
                    }
                }
                List<CPAMetric> RSR_alt_1 = null;
                if (metricsInfo.getRSR_alt_1_info() != null && metricsInfo.getRSR_alt_1_info().isAssessedForTask(taskNum)
                        && metricsInfo.getRSR_alt_1_info().isCalculated()) {
                    RSR_alt_1 = rsrAsrMetrics.getRSR_alt_1();
                    if (RSR_alt_1 == null || RSR_alt_1.size() != numStages) {
                        RSR_alt_1 = new ArrayList<CPAMetric>(numStages);
                        rsrAsrMetrics.setRSR_alt_1(RSR_alt_1);
                        String name = metricsInfo.getRSR_alt_1_info().getName();
                        for (int stage = 0; stage < numStages; stage++) {
                            RSR_alt_1.add(new CPAMetric(name));
                        }
                    }
                }

                List<Double> Spq = null;
                List<CPAMetric> RSR_Bayesian = null;
                List<CPAMetric> RSR_alt_2 = null;
                boolean normativeProbsPresent = isNormativeProbabilitiesPresent(probs)
                        && isNormativeProbabilitiesPresent(comparisonProbs);
                if (normativeProbsPresent) {
                    //normativeProbs = comparisonMetrics.getNormative_probs();
                    Spq = rsrAsrMetrics.getSpq();
                    if (Spq == null || Spq.size() != numStages) {
                        Spq = new ArrayList<Double>();
                        rsrAsrMetrics.setSpq(Spq);
                        for (int stage = 0; stage < numStages; stage++) {
                            Spq.add(null);
                        }
                    }
                    if (metricsInfo.getRSR_Bayesian_info() != null 
                            && metricsInfo.getRSR_Bayesian_info().isAssessedForTask(taskNum)
                            && metricsInfo.getRSR_Bayesian_info().isCalculated()) {
                        RSR_Bayesian = rsrAsrMetrics.getRSR_Bayesian();
                        if (RSR_Bayesian == null || RSR_Bayesian.size() != numStages) {
                            RSR_Bayesian = new ArrayList<CPAMetric>(numStages);
                            rsrAsrMetrics.setRSR_Bayesian(RSR_Bayesian);
                            String name = metricsInfo.getRSR_Bayesian_info().getName();
                            for (int stage = 0; stage < numStages; stage++) {
                                RSR_Bayesian.add(new CPAMetric(name));
                            }
                        }
                    }
                    if (metricsInfo.getRSR_alt_2_info() != null 
                            && metricsInfo.getRSR_alt_2_info().isAssessedForTask(taskNum)
                            && metricsInfo.getRSR_alt_2_info().isCalculated()) {
                        RSR_alt_2 = rsrAsrMetrics.getRSR_alt_2();
                        if (RSR_alt_2 == null || RSR_alt_2.size() != numStages) {
                            RSR_alt_2 = new ArrayList<CPAMetric>(numStages);
                            rsrAsrMetrics.setRSR_alt_2(RSR_alt_2);
                            String name = metricsInfo.getRSR_alt_2_info().getName();
                            for (int stage = 0; stage < numStages; stage++) {
                                RSR_alt_2.add(new CPAMetric(name));
                            }
                        }
                    }
                }

                List<CPAMetric> ASR = null;
                if (metricsInfo.getASR_info() != null && metricsInfo.getASR_info().isAssessedForTask(taskNum)
                        && metricsInfo.getASR_info().isCalculated()) {
                    ASR = rsrAsrMetrics.getASR();
                    if (ASR == null || ASR.size() != numStages) {
                        ASR = new ArrayList<CPAMetric>(numStages);
                        rsrAsrMetrics.setASR(ASR);
                        String name = metricsInfo.getASR_info().getName();
                        for (int stage = 0; stage < numStages; stage++) {
                            ASR.add(new CPAMetric(name));
                        }
                    }
                }

                for (int stage = 0; stage < numStages; stage++) {
                    List<Double> currentProbs = probs.get(stage).getProbabilities();
                    List<Double> currentComparisonProbs = comparisonProbs.get(stage).getProbabilities();
                    List<Double> currentNormativeProbs = probs.get(stage).getProbabilities_normative_incremental();
                    if (checkProbsNotNullOrEmpty(currentProbs)) {
                        //Compute Spm (similarity of currentProbs to comparisonProbs)
                        Double spm = cpaMetricsComputer.computeSpm(currentProbs, currentComparisonProbs);
                        Spm.set(stage, spm);

                        //Compute Spr (similarity of comparisonProbs to uniform (random) probs)
                        Double spr = cpaMetricsComputer.computeSpr(currentComparisonProbs);
                        Spr.set(stage, spr);

                        //Compute standard RSR using uniform probabilities as the null model probabilities
                        RSR.set(stage, cpaMetricsComputer.computeRSR_Standard(spm, spr, RSR.get(stage), metricsInfo.getRSR_info(),
                                taskNum, trialNum, stage));

                        //Compute RSR(RMSE) using uniform probabilities as the null model probabilities
                        if (RSR_alt_1 != null) {
                            RSR_alt_1.set(stage, cpaMetricsComputer.computeRSR_alt_1(currentProbs, currentComparisonProbs, currentNormativeProbs,
                                    RSR_alt_1.get(stage), metricsInfo.getRSR_alt_1_info(), taskNum, trialNum, stage));
                        }

                        if (normativeProbsPresent) {
                            //Compute Spq
                            Double spq = cpaMetricsComputer.computeSpq(currentComparisonProbs, currentNormativeProbs);
                            if (Spq != null) {
                                Spq.set(stage, spq);
                            }

                            //Computer RSR using the normative probabilities as the null model probabilities
                            if (RSR_Bayesian != null) {
                                RSR_Bayesian.set(stage, cpaMetricsComputer.computeRSR_Bayesian(spm, spq,
                                        RSR_Bayesian.get(stage), metricsInfo.getRSR_Bayesian_info(),
                                        taskNum, trialNum, stage));
                            }

                            //Compute RSR(RMSE) using the normative probabilities as the null model probabilities
                            if (RSR_alt_2 != null) {
                                RSR_alt_2.set(stage, cpaMetricsComputer.computeRSR_alt_2(currentProbs, currentComparisonProbs, currentNormativeProbs,
                                        RSR_alt_2.get(stage), metricsInfo.getRSR_alt_2_info(), taskNum, trialNum, stage));
                            }
                        }
                        
                        //Compute ASR
                        if (ASR != null) {
                            //System.out.println("ASR: " + currentProbs + ", " + currentComparisonProbs);
                            ASR.set(stage, cpaMetricsComputer.computeASR(currentProbs, currentComparisonProbs,
                                    ASR.get(stage), metricsInfo.getASR_info(), taskNum, trialNum, stage));
                        }
                    } else {
                        //RSR.get(stage).assessed = false;                        
                        RSR.set(stage, null);
                        if(RSR_alt_1 != null) {
                            RSR_alt_1.set(stage, null);
                        }
                        if(RSR_alt_2 != null) {
                            RSR_alt_2.set(stage, null);
                        }
                        if(RSR_Bayesian != null) {
                            RSR_Bayesian.set(stage, null);
                        }
                        if(ASR != null) {
                            ASR.set(stage, null);
                        }
                    }
                }
            }
        }
    }

    /**
     * Checks whether normative probabilities are present in each probability
     * report.
     *
     * @param probabilityReports
     * @return
     */
    protected static boolean isNormativeProbabilitiesPresent(List<ProbabilityReportData> probabilityReports) {
        if (probabilityReports != null && !probabilityReports.isEmpty()) {
            for (ProbabilityReportData probabilityReport : probabilityReports) {
                if (probabilityReport.getProbabilities_normative_incremental() == null
                        || probabilityReport.getProbabilities_normative_incremental().isEmpty()) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Get the probability stage names from the list of probability reports.
     *
     * @param probs
     * @return
     */
    protected static List<String> getStageNames(List<ProbabilityReportData> probs) {
        List<String> stageNames = null;
        if (probs != null && !probs.isEmpty()) {
            stageNames = new ArrayList<String>(probs.size());
            for (ProbabilityReportData probReport : probs) {
                stageNames.add(probReport.getProbabilitiesName());
            }
        }
        return stageNames;
    }

    /**
     * Check whether the probability report stages in probs and comparison probs
     * match.
     *
     * @param probs
     * @param comparisonProbs
     * @return
     */
    protected static boolean checkStagesMatch(List<ProbabilityReportData> probs,
            List<ProbabilityReportData> comparisonProbs) {
        if (probs != null && !probs.isEmpty() && comparisonProbs != null
                && probs.size() == comparisonProbs.size()) {
            for (int i = 0; i < probs.size(); i++) {
                ProbabilityReportData probReport = probs.get(i);
                ProbabilityReportData comparisonProbReport = comparisonProbs.get(i);
                if (probReport.getTrialPartType() != comparisonProbReport.getTrialPartType()) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Returns true if the Red tactic with the highest probability matches the
     * actual Red tactic (and no other probability is the same as the highest probability), 
     * false otherwise.
     *
     * @param redTacticProbs
     * @param possibleTactics
     * @param actualTactic
     * @return
     */
    protected static Boolean isRedTacticCorrectlyDetected(List<Double> redTacticProbs,
            List<RedTacticType> possibleTactics, RedTacticType actualTactic) {
        if (possibleTactics != null && redTacticProbs != null
                && possibleTactics.size() == redTacticProbs.size()
                && actualTactic != null) {
            RedTacticType mostLikelyTactic = null;
            Double mostLikelyTacticProb = Double.MIN_VALUE;
            boolean mostLikelyTacticProbUnique = true;
            for (int i = 0; i < possibleTactics.size(); i++) {
                Double prob = redTacticProbs.get(i);
                if (prob != null && prob > mostLikelyTacticProb) {
                    mostLikelyTacticProb = prob;
                    mostLikelyTactic = possibleTactics.get(i);
                    mostLikelyTacticProbUnique = true;
                } else if(prob != null && prob.equals(mostLikelyTacticProb)) {
                     mostLikelyTacticProbUnique = false;
                }
            }
            return mostLikelyTacticProbUnique && mostLikelyTactic == actualTactic;
        } else {
            return null;
        }
    }

    /**
     * Computes the metrics for the average trial given a collection of trial
     * data (all for the same trial).
     *
     * @param trials
     * @param averageTrial
     * @param comparisonTrialData
     * @param metricsInfo
     * @param taskNum
     * @return
     */   
    public TrialData updateAverageTrialMetrics(List<TrialData> trials, TrialData averageTrial,
            TrialData comparisonTrialData, MetricsInfo metricsInfo, int taskNum) {
        if (trials != null && !trials.isEmpty()) {
            TrialData trial0 = trials.get(0);
            TrialMetrics trialMetrics0 = trial0.getMetrics();
            averageTrial = initializeAverageTrial(averageTrial, trial0);
            TrialMetrics averageMetrics = averageTrial.getMetrics();
            if (averageMetrics == null) {
                averageMetrics = new TrialMetrics();
                averageTrial.setMetrics(averageMetrics);
            }
            averageTrial.setData_stale(true);

            //***************** Compute means and standard deviations *****************
            DistributionStats[] redTacticProbs_stats = null;
            DistributionStats[] redTacticProbs_normative_stats = null;
            DistributionStats Np_redTacticProbs_stats = null;
            DistributionStats Nq_redTacticProbs_stats = null;
            int numRedTacticProbs = 0;
            if (trial0.getRedTacticsReport() != null
                    && trial0.getRedTacticsReport().getRedTacticProbabilities() != null
                    && trial0.getRedTacticsReport().getRedTacticProbabilities().size() > 0) {
                numRedTacticProbs = trial0.getRedTacticsReport().getRedTacticProbabilities().size();
                redTacticProbs_stats = new DistributionStats[numRedTacticProbs];
                redTacticProbs_normative_stats = new DistributionStats[numRedTacticProbs];
                Np_redTacticProbs_stats = new DistributionStats();
                Nq_redTacticProbs_stats = new DistributionStats();
            }

            DistributionStats[][] redAttackProbs_stats = null;
            DistributionStats[][] redAttackProbs_normative_stats = null;
            DistributionStats Np_Pp_stats = null;
            DistributionStats Nq_Pp_stats = null;
            DistributionStats Np_Ppc_stats = null;
            DistributionStats Nq_Ppc_stats = null;
            DistributionStats Np_Pt_stats = null;
            DistributionStats Nq_Pt_stats = null;
            DistributionStats Np_Ptpc_stats = null;
            DistributionStats Nq_Ptpc_stats = null;
            DistributionStats[] Np_delta_stats = null;
            DistributionStats[] Nq_delta_stats = null;
            int numAttackProbs = 0;
            if (trial0.getAttackProbabilityReports() != null
                    && !trial0.getAttackProbabilityReports().isEmpty()) {
                numAttackProbs = trial0.getAttackProbabilityReports().get(0).getProbabilities().size();
                if (numAttackProbs > 0) {
                    int numReports = trial0.getAttackProbabilityReports().size();
                    redAttackProbs_stats = new DistributionStats[numReports][numAttackProbs];
                    redAttackProbs_normative_stats = new DistributionStats[numReports][numAttackProbs];
                }
            }
            if (trialMetrics0.getNegentropyMetrics() != null) {
                if (trialMetrics0.getNegentropyMetrics().getNp_Pp() != null) {
                    Np_Pp_stats = new DistributionStats();
                    Nq_Pp_stats = new DistributionStats();
                }
                if (trialMetrics0.getNegentropyMetrics().getNp_Ppc() != null) {
                    Np_Ppc_stats = new DistributionStats();
                    Nq_Ppc_stats = new DistributionStats();
                }
                if (trialMetrics0.getNegentropyMetrics().getNp_Pt() != null) {
                    Np_Pt_stats = new DistributionStats();
                    Nq_Pt_stats = new DistributionStats();
                }
                if (trialMetrics0.getNegentropyMetrics().getNp_Ptpc() != null) {
                    Np_Ptpc_stats = new DistributionStats();
                    Nq_Ptpc_stats = new DistributionStats();
                }
                if (trialMetrics0.getNegentropyMetrics().getNp_delta() != null
                        && !trialMetrics0.getNegentropyMetrics().getNp_delta().isEmpty()) {
                    int numStages = trialMetrics0.getNegentropyMetrics().getNp_delta().size();
                    Np_delta_stats = new DistributionStats[numStages];
                    Nq_delta_stats = new DistributionStats[numStages];
                }
            }

            DistributionStats normativeBlueOptionSelections_stats = null;
            if (trialMetrics0.getPM_normativeBlueOptionSelections() != null) {
                normativeBlueOptionSelections_stats = new DistributionStats();
            }

            DistributionStats sigintAtHighestPaLocationSelections_stats = null;
            if (trialMetrics0.getCS_sigintAtHighestPaLocationSelections() != null) {
                sigintAtHighestPaLocationSelections_stats = new DistributionStats();
            }

            /*DistributionStats redTacticCorrectlyDetected_stats = null;
             if(trialMetrics0.isCB_redTacticCorrectlyDetected() != null) {
             redTacticCorrectlyDetected_stats = new DistributionStats();
             }*/
            
            DistributionStats percentTrialsReviewedInBatchPlot_stats = null;
            if (trialMetrics0.getSS_percentTrialsReviewedInBatchPlot() != null) {
                percentTrialsReviewedInBatchPlot_stats = new DistributionStats();
            }

            DistributionStats[] frequencySigintSelectedAtEachLocation_stats = null;
            String[] sigintLocationIds = null;
            if (trialMetrics0.getRMR_frequencySigintSelectedAtEachLocation() != null
                    && !trialMetrics0.getRMR_frequencySigintSelectedAtEachLocation().isEmpty()) {
                int numLocations = trialMetrics0.getRMR_frequencySigintSelectedAtEachLocation().size();
                frequencySigintSelectedAtEachLocation_stats = new DistributionStats[numLocations];
                sigintLocationIds = new String[numLocations];
                int i = 0;
                for (SigintSelectionFrequency freq : trialMetrics0.getRMR_frequencySigintSelectedAtEachLocation()) {
                    sigintLocationIds[i] = freq.getLocationId();
                }
                i++;
            }

            DistributionStats[] frequencyBlueActionsSelected_stats = null;
            ArrayList<List<BlueAction>> blueOptions = null;
            if (trialMetrics0.getRMR_frequencyBlueActionsSelected() != null
                    && !trialMetrics0.getRMR_frequencyBlueActionsSelected().isEmpty()) {
                int numBlueOptions = trialMetrics0.getRMR_frequencyBlueActionsSelected().size();
                frequencyBlueActionsSelected_stats = new DistributionStats[numBlueOptions];
                blueOptions = new ArrayList<List<BlueAction>>(numBlueOptions);
                for (BlueActionSelectionFrequency freq : trialMetrics0.getRMR_frequencyBlueActionsSelected()) {
                    blueOptions.add(freq.getBlueActions());
                }
            }

            //Compute average trial data
            int i;
            for (TrialData trial : trials) {
                TrialMetrics trialMetrics = trial.getMetrics();
                if (trialMetrics != null) {
                    //Update average human and normative Red tactic probabilities and negentropies
                    if (redTacticProbs_stats != null
                            && trial.getRedTacticsReport() != null) {
                        List<Double> currentParticipantProbs = trial.getRedTacticsReport().getRedTacticProbabilities();
                        List<Double> currentNormativeProbs = trial.getRedTacticsReport().getRedTacticProbabilities_normative();                        
                        if (currentParticipantProbs != null) {
                            i = 0;
                            for (Double prob : currentParticipantProbs) {
                                redTacticProbs_stats[i] = MetricUtils.updateRunningDistStats(
                                        redTacticProbs_stats[i], prob, true);
                                i++;
                            }
                        }
                        if (currentNormativeProbs != null) {
                            i = 0;
                            for (Double prob : currentNormativeProbs) {
                                redTacticProbs_normative_stats[i] = MetricUtils.updateRunningDistStats(
                                        redTacticProbs_normative_stats[i], prob, true);
                                i++;
                            }
                        }
                        if (trialMetrics.getNegentropyMetrics() != null) {
                            Np_redTacticProbs_stats = MetricUtils.updateRunningDistStats(
                                    Np_redTacticProbs_stats, trialMetrics.getNegentropyMetrics().getNp_redTacticProbs(), true);
                            Nq_redTacticProbs_stats = MetricUtils.updateRunningDistStats(
                                    Nq_redTacticProbs_stats, trialMetrics.getNegentropyMetrics().getNq_redTacticProbs(), true);
                        }
                    }

                    //Update average human and normative Red attack probabilities and negentropies
                    if (redAttackProbs_stats != null && trial.getAttackProbabilityReports() != null
                            && !trial.getAttackProbabilityReports().isEmpty()) {
                        i = 0;
                        for (AttackProbabilityReportData probs : trial.getAttackProbabilityReports()) {
                            List<Double> currentParticipantProbs = probs.getProbabilities();
                            List<Double> currentNormativeProbs = probs.getProbabilities_normative_incremental();                            
                            if (currentParticipantProbs != null) {
                                int j = 0;
                                for (Double prob : currentParticipantProbs) {
                                    redAttackProbs_stats[i][j] = MetricUtils.updateRunningDistStats(
                                            redAttackProbs_stats[i][j], prob, true);
                                    j++;
                                }
                            }
                            if (currentNormativeProbs != null) {
                                int j = 0;
                                for (Double prob : currentNormativeProbs) {
                                    redAttackProbs_normative_stats[i][j] = MetricUtils.updateRunningDistStats(
                                            redAttackProbs_normative_stats[i][j], prob, true);
                                    j++;
                                }
                            }
                            i++;
                        }
                        if (trialMetrics.getNegentropyMetrics() != null) {
                            Np_Pp_stats = MetricUtils.updateRunningDistStats(Np_Pp_stats,
                                    trialMetrics.getNegentropyMetrics().getNp_Pp(), true);
                            Nq_Pp_stats = MetricUtils.updateRunningDistStats(Nq_Pp_stats,
                                    trialMetrics.getNegentropyMetrics().getNq_Pp(), true);
                            Np_Ppc_stats = MetricUtils.updateRunningDistStats(Np_Ppc_stats,
                                    trialMetrics.getNegentropyMetrics().getNp_Ppc(), true);
                            Nq_Ppc_stats = MetricUtils.updateRunningDistStats(Nq_Ppc_stats,
                                    trialMetrics.getNegentropyMetrics().getNq_Ppc(), true);
                            Np_Pt_stats = MetricUtils.updateRunningDistStats(Np_Pt_stats,
                                    trialMetrics.getNegentropyMetrics().getNp_Pt(), true);
                            Nq_Pt_stats = MetricUtils.updateRunningDistStats(Nq_Pt_stats,
                                    trialMetrics.getNegentropyMetrics().getNq_Pt(), true);
                            Np_Ptpc_stats = MetricUtils.updateRunningDistStats(Np_Ptpc_stats,
                                    trialMetrics.getNegentropyMetrics().getNp_Ptpc(), true);
                            Nq_Ptpc_stats = MetricUtils.updateRunningDistStats(Nq_Ptpc_stats,
                                    trialMetrics.getNegentropyMetrics().getNq_Ptpc(), true);
                            if (trialMetrics.getNegentropyMetrics().getNp_delta() != null) {
                                i = 0;
                                for (Double ne : trialMetrics.getNegentropyMetrics().getNp_delta()) {
                                    Np_delta_stats[i] = MetricUtils.updateRunningDistStats(
                                            Np_delta_stats[i], ne, true);
                                    i++;
                                }
                            }
                            if (trialMetrics.getNegentropyMetrics().getNq_delta() != null) {
                                i = 0;
                                for (Double ne : trialMetrics.getNegentropyMetrics().getNq_delta()) {
                                    Nq_delta_stats[i] = MetricUtils.updateRunningDistStats(
                                            Nq_delta_stats[i], ne, true);
                                    i++;
                                }
                            }
                        }
                    }

                    //Update average normative Blue action selection frequencies
                    if (normativeBlueOptionSelections_stats != null
                            && trialMetrics.getPM_normativeBlueOptionSelections() != null) {
                        normativeBlueOptionSelections_stats = MetricUtils.updateRunningDistStats(
                                normativeBlueOptionSelections_stats,
                                trialMetrics.getPM_normativeBlueOptionSelections(), true);
                    }

                    //Update average normative SIGINT location selection frequencies
                    if (sigintAtHighestPaLocationSelections_stats != null
                            && trialMetrics.getCS_sigintAtHighestPaLocationSelections() != null) {
                        sigintAtHighestPaLocationSelections_stats = MetricUtils.updateRunningDistStats(
                                sigintAtHighestPaLocationSelections_stats,
                                trialMetrics.getCS_sigintAtHighestPaLocationSelections(), true);
                    }

                    //Update the average perecent of trials reviewed in a batch plot
                    if (percentTrialsReviewedInBatchPlot_stats != null
                            && trialMetrics.getSS_percentTrialsReviewedInBatchPlot() != null) {
                        percentTrialsReviewedInBatchPlot_stats = MetricUtils.updateRunningDistStats(
                                percentTrialsReviewedInBatchPlot_stats,
                                trialMetrics.getSS_percentTrialsReviewedInBatchPlot(), true);
                    }

                    //Update the SIGINT location selection frequencies
                    if (frequencySigintSelectedAtEachLocation_stats != null
                            && trialMetrics.getRMR_frequencySigintSelectedAtEachLocation() != null) {
                        i = 0;
                        for (SigintSelectionFrequency freq : trialMetrics.getRMR_frequencySigintSelectedAtEachLocation()) {
                            if (freq != null) {
                                frequencySigintSelectedAtEachLocation_stats[i] = MetricUtils.updateRunningDistStats(
                                        frequencySigintSelectedAtEachLocation_stats[i],
                                        freq.getSelectionPercent(), true);
                            }
                            i++;
                        }
                    }

                    //Update average Blue action selection frequencies
                    if (frequencyBlueActionsSelected_stats != null
                            && trialMetrics.getRMR_frequencyBlueActionsSelected() != null) {
                        i = 0;
                        for (BlueActionSelectionFrequency freq : trialMetrics.getRMR_frequencyBlueActionsSelected()) {
                            if (freq != null) {
                                frequencyBlueActionsSelected_stats[i] = MetricUtils.updateRunningDistStats(
                                        frequencyBlueActionsSelected_stats[i],
                                        freq.getSelectionPercent(), true);
                            }
                            i++;
                        }
                    }
                }
            }

            //***************** Set means and standard deviations in the average trial *****************
            //Compute average, standard deviation human and normative Red tactic probabilities and negentropies
            if (redTacticProbs_stats != null) {
                List<Double> probs = new ArrayList<Double>(numRedTacticProbs);
                List<Double> probs_std = new ArrayList<Double>(numRedTacticProbs);
                List<Double> probs_normative = new ArrayList<Double>(numRedTacticProbs);
                List<Double> probs_normative_std = new ArrayList<Double>(numRedTacticProbs);
                for (DistributionStats stats : redTacticProbs_stats) {
                    stats = MetricUtils.distStats(stats, true);
                    probs.add(stats.mean);
                    probs_std.add(stats.std);
                }
                for (DistributionStats stats : redTacticProbs_normative_stats) {
                    stats = MetricUtils.distStats(stats, true);
                    probs_normative.add(stats.mean);
                    probs_normative_std.add(stats.std);
                }
                averageTrial.getRedTacticsReport().setRedTacticProbabilities(probs);
                averageTrial.getRedTacticsReport().setRedTacticProbabilities_std(probs_std);
                averageTrial.getRedTacticsReport().setRedTacticProbabilities_normative(probs_normative);
                averageTrial.getRedTacticsReport().setRedTacticProbabilities_normative_std(probs_normative_std);
                if (averageMetrics.getNegentropyMetrics() == null) {
                    averageMetrics.setNegentropyMetrics(new NegentropyMetrics());
                }
                Np_redTacticProbs_stats = MetricUtils.distStats(Np_redTacticProbs_stats, true);
                Nq_redTacticProbs_stats = MetricUtils.distStats(Nq_redTacticProbs_stats, true);
                averageMetrics.getNegentropyMetrics().setNp_redTacticProbs(Np_redTacticProbs_stats.mean);
                averageMetrics.getNegentropyMetrics().setNp_redTacticProbs_std(Np_redTacticProbs_stats.std);
                averageMetrics.getNegentropyMetrics().setNq_redTacticProbs(Nq_redTacticProbs_stats.mean);
                averageMetrics.getNegentropyMetrics().setNq_redTacticProbs_std(Nq_redTacticProbs_stats.std);
            }

            //Compute average, standard deviation human and normative Red attack probabilities and negentropies
            if (redAttackProbs_stats != null) {
                for (int reportIndex = 0; reportIndex < redAttackProbs_stats.length; reportIndex++) {
                    List<Double> probs = new ArrayList<Double>(numAttackProbs);
                    List<Double> probs_std = new ArrayList<Double>(numAttackProbs);
                    List<Double> probs_normative = new ArrayList<Double>(numAttackProbs);
                    List<Double> probs_normative_std = new ArrayList<Double>(numAttackProbs);
                    for (DistributionStats stats : redAttackProbs_stats[reportIndex]) {
                        stats = MetricUtils.distStats(stats, true);
                        probs.add(stats.mean);
                        probs_std.add(stats.std);
                    }
                    for (DistributionStats stats : redAttackProbs_normative_stats[reportIndex]) {
                        stats = MetricUtils.distStats(stats, true);
                        probs_normative.add(stats.mean);
                        probs_normative_std.add(stats.std);
                    }
                    AttackProbabilityReportData report = averageTrial.getAttackProbabilityReports().get(reportIndex);
                    report.setProbabilities(probs);
                    report.setProbabilities_std(probs_std);
                    //report.setProbabilities_normative(probs_normative);
                    report.setProbabilities_normative_incremental(probs_normative);
                    //report.setProbabilities_normative_std(probs_normative_std);                   
                    report.setProbabilities_normative_incremental_std(probs_normative_std);
                    if (averageMetrics.getNegentropyMetrics() == null) {
                        averageMetrics.setNegentropyMetrics(new NegentropyMetrics());
                    }
                    if (Np_Pp_stats != null) {
                        Np_Pp_stats = MetricUtils.distStats(Np_Pp_stats, true);
                        Nq_Pp_stats = MetricUtils.distStats(Nq_Pp_stats, true);
                        averageMetrics.getNegentropyMetrics().setNp_Pp(Np_Pp_stats.mean);
                        averageMetrics.getNegentropyMetrics().setNp_Pp_std(Np_Pp_stats.std);
                        averageMetrics.getNegentropyMetrics().setNq_Pp(Nq_Pp_stats.mean);
                        averageMetrics.getNegentropyMetrics().setNq_Pp_std(Nq_Pp_stats.std);
                    }
                    if (Np_Ppc_stats != null) {
                        Np_Ppc_stats = MetricUtils.distStats(Np_Ppc_stats, true);
                        Nq_Ppc_stats = MetricUtils.distStats(Nq_Ppc_stats, true);
                        averageMetrics.getNegentropyMetrics().setNp_Ppc(Np_Ppc_stats.mean);
                        averageMetrics.getNegentropyMetrics().setNp_Ppc_std(Np_Ppc_stats.std);
                        averageMetrics.getNegentropyMetrics().setNq_Ppc(Nq_Ppc_stats.mean);
                        averageMetrics.getNegentropyMetrics().setNq_Ppc_std(Nq_Ppc_stats.std);
                    }
                    if (Np_Pt_stats != null) {
                        Np_Pt_stats = MetricUtils.distStats(Np_Pt_stats, true);
                        Nq_Pt_stats = MetricUtils.distStats(Nq_Pt_stats, true);
                        averageMetrics.getNegentropyMetrics().setNp_Pt(Np_Pt_stats.mean);
                        averageMetrics.getNegentropyMetrics().setNp_Pt_std(Np_Pt_stats.std);
                        averageMetrics.getNegentropyMetrics().setNq_Pt(Nq_Pt_stats.mean);
                        averageMetrics.getNegentropyMetrics().setNq_Pt_std(Nq_Pt_stats.std);
                    }
                    if (Np_Ptpc_stats != null) {
                        Np_Ptpc_stats = MetricUtils.distStats(Np_Ptpc_stats, true);
                        Nq_Ptpc_stats = MetricUtils.distStats(Nq_Ptpc_stats, true);
                        averageMetrics.getNegentropyMetrics().setNp_Ptpc(Np_Ptpc_stats.mean);
                        averageMetrics.getNegentropyMetrics().setNp_Ptpc_std(Np_Ptpc_stats.std);
                        averageMetrics.getNegentropyMetrics().setNq_Ptpc(Nq_Ptpc_stats.mean);                        
                        averageMetrics.getNegentropyMetrics().setNq_Ptpc_std(Nq_Ptpc_stats.std);                        
                    }
                }
            }

            //Compute average, standard deviation normative Blue action selection frequencies
            if (normativeBlueOptionSelections_stats != null) {
                normativeBlueOptionSelections_stats = MetricUtils.distStats(
                        normativeBlueOptionSelections_stats, true);
                averageMetrics.setPM_normativeBlueOptionSelections(
                        normativeBlueOptionSelections_stats.mean);
                averageMetrics.setPM_normativeBlueOptionSelections_std(
                        normativeBlueOptionSelections_stats.std);
            }

            //Compute average, standard deviation normative SIGINT location selection frequencies
            if (sigintAtHighestPaLocationSelections_stats != null) {
                sigintAtHighestPaLocationSelections_stats = MetricUtils.distStats(
                        sigintAtHighestPaLocationSelections_stats, true);
                averageMetrics.setCS_sigintAtHighestPaLocationSelections(
                        sigintAtHighestPaLocationSelections_stats.mean);
                averageMetrics.setCS_sigintAtHighestPaLocationSelections_std(
                        sigintAtHighestPaLocationSelections_stats.std);
            }

            //Compute average, standard deviation perecent of trials reviewed in a batch plot
            if (percentTrialsReviewedInBatchPlot_stats != null) {
                percentTrialsReviewedInBatchPlot_stats = MetricUtils.distStats(
                        percentTrialsReviewedInBatchPlot_stats, true);
                averageMetrics.setSS_percentTrialsReviewedInBatchPlot(
                        percentTrialsReviewedInBatchPlot_stats.mean);
                averageMetrics.setSS_percentTrialsReviewedInBatchPlot_std(
                        percentTrialsReviewedInBatchPlot_stats.std);
            }

            //Compute average, standard deviation SIGINT location selection frequencies
            if (frequencySigintSelectedAtEachLocation_stats != null) {
                List<SigintSelectionFrequency> freqs = new ArrayList<SigintSelectionFrequency>(
                        frequencySigintSelectedAtEachLocation_stats.length);
                int locationIndex = 0;
                for (DistributionStats stats : frequencySigintSelectedAtEachLocation_stats) {
                    double count = stats.mean;
                    stats = MetricUtils.distStats(stats, true);
                    freqs.add(new SigintSelectionFrequency(sigintLocationIds[locationIndex],
                            count, stats.mean, stats.std));
                    locationIndex++;
                }
                averageMetrics.setRMR_frequencySigintSelectedAtEachLocation(freqs);
            }

            //Compute average, standard deviation Blue action selection frequencies
            if (frequencyBlueActionsSelected_stats != null) {
                List<BlueActionSelectionFrequency> freqs = new ArrayList<BlueActionSelectionFrequency>(
                        frequencyBlueActionsSelected_stats.length);
                int optionIndex = 0;
                for (DistributionStats stats : frequencyBlueActionsSelected_stats) {
                    double count = stats.mean;
                    stats = MetricUtils.distStats(stats, true);
                    freqs.add(new BlueActionSelectionFrequency(
                            blueOptions != null ? blueOptions.get(optionIndex) : null,
                            count, stats.mean, stats.std));
                    optionIndex++;
                }
                averageMetrics.setRMR_frequencyBlueActionsSelected(freqs);
            }

            //***************** Compute average trial metrics data using average trial data *****************
            updateTrialMetrics(averageTrial, metricsInfo, comparisonTrialData);
        }
        averageTrial.setData_stale(false);
        return averageTrial;
    }

    /**
     *
     * @param averageTrial
     * @param trialData
     * @return
     */
    protected static TrialData initializeAverageTrial(TrialData averageTrial, TrialData trialData) {
        if (averageTrial == null) {
            averageTrial = new TrialData();
            averageTrial.setTrial_number(trialData.getTrial_number());
            averageTrial.setTask_id(trialData.getTask_id());
            averageTrial.setTask_number(trialData.getTask_number());
            averageTrial.setExam_id(trialData.getExam_id());
        }
        averageTrial.setData_type(DataType.Human_Avg);        
        averageTrial.setNumBlueLocations(trialData.getNumBlueLocations());
        averageTrial.setBlueLocationIds(trialData.getBlueLocationIds());
        averageTrial.setRedCapability_Pc(trialData.getRedCapability_Pc());
        averageTrial.setRedVulnerability_P(trialData.getRedVulnerability_P());
        averageTrial.setRedOpportunity_U(trialData.getRedOpportunity_U());
        averageTrial.setRedActivityDetected(trialData.getRedActivityDetected());
        averageTrial.setBluebookProbabilities(trialData.getBluebookProbabilities());
        if (trialData.getRedTacticsReport() != null) {
            RedTacticsReportData redTacticsReport = new RedTacticsReportData();
            redTacticsReport.setTrialPartType(trialData.getRedTacticsReport().getTrialPartType());
            redTacticsReport.setPossibleRedTactics(trialData.getRedTacticsReport().getPossibleRedTactics());
            redTacticsReport.setActualRedTactic(trialData.getRedTacticsReport().getActualRedTactic());
            averageTrial.setRedTacticsReport(redTacticsReport);
        }
        if (trialData.getAttackProbabilityReports() != null
                && !trialData.getAttackProbabilityReports().isEmpty()) {
            List<AttackProbabilityReportData> attackProbabilityReports = 
                    new ArrayList<AttackProbabilityReportData>(trialData.getAttackProbabilityReports().size());
            for (AttackProbabilityReportData probReport : trialData.getAttackProbabilityReports()) {
                AttackProbabilityReportData report = new AttackProbabilityReportData();
                report.setTrialPartType(probReport.getTrialPartType());
                report.setProbabilitiesId(probReport.getProbabilitiesId());
                report.setProbabilitiesName(probReport.getProbabilitiesName());
                attackProbabilityReports.add(report);
            }
            averageTrial.setAttackProbabilityReports(attackProbabilityReports);
        }
        return averageTrial;
    }
}
