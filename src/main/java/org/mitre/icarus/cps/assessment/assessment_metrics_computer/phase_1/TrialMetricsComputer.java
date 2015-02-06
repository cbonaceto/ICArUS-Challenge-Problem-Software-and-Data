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
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.mitre.icarus.cps.app.util.ProbabilityUtils;
import org.mitre.icarus.cps.assessment.assessment_metrics_computer.ITrialMetricsComputer;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.CPAMetric;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.Probabilities;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.MetricsInfo;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.TrialData;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.TrialMetrics;
import org.mitre.icarus.cps.assessment.score_computer.ScoreComputerBase;
import org.mitre.icarus.cps.assessment.score_computer.ScoreComputerBase.ProbabilityType;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupType;

/**
 * Computes Phase 1 trial metrics.
 *
 * @author cbonaceto
 *
 */
public class TrialMetricsComputer extends MetricsComputer implements
        ITrialMetricsComputer<TrialMetrics, TrialData, MetricsInfo> {
   
    @Override
    public TrialMetrics updateTrialMetrics(TrialData trialData, MetricsInfo metricsInfo,
            TrialData comparisonTrialData) {
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

        trialData.setCfa_metrics_stale(true);
        trialData.setCpa_metrics_stale(true);

        int taskNum = trialData.getTask_number();
        int trialNum = trialData.getTrial_number();

        //Compute normalized decimal probabilities (with floor applied), negentropies 
        //and delta negentropies of participant and normative probabilities (Used for RR and AI)
        List<Double> probsLastStage = null;
        int numStages = 0;
        if (trialData.getProbs() != null && !trialData.getProbs().isEmpty()) {
            numStages = trialData.getProbs().size();
            ArrayList<Probabilities> decimalProbs = new ArrayList<Probabilities>(numStages);
            ArrayList<Double> Np = new ArrayList<Double>(numStages);
            ArrayList<Double> Np_delta = new ArrayList<Double>(numStages);
            trialMetrics.setProbs(decimalProbs);
            trialMetrics.setNp(Np);
            trialMetrics.setNp_delta(Np_delta);
            int stage = 0;
            for (Probabilities probs : trialData.getProbs()) {
                List<Double> currentDecimalProbs = (probs != null && probs.getProbs() != null && !probs.getProbs().isEmpty())
                        ? ProbabilityUtils.normalizePercentProbabilities_Double(probs.getProbs(), null, ScoreComputerBase.EPSILON) : null;
                decimalProbs.add(new Probabilities(currentDecimalProbs));
                if (currentDecimalProbs == null) {
                    Np.add(0d);
                    Np_delta.add(0d);
                } else {
                    double ne = ScoreComputerBase.computeNegentropy(currentDecimalProbs, ProbabilityType.Decimal);
                    Np.add(ne);
                    if (stage > 0) {
                        Np_delta.add(ne - Np.get(stage - 1));
                    } else {
                        Np_delta.add(0d);
                    }
                }
                if (stage == numStages - 1) {
                    probsLastStage = currentDecimalProbs;
                }
                stage++;
            }
        }
        if (trialData.getNormative_probs() != null && !trialData.getNormative_probs().isEmpty()) {
            ArrayList<Probabilities> decimalProbs = new ArrayList<Probabilities>(numStages);
            ArrayList<Double> Nq = new ArrayList<Double>(numStages);
            ArrayList<Double> Nq_delta = new ArrayList<Double>(numStages);
            trialMetrics.setNormative_probs(decimalProbs);
            trialMetrics.setNq(Nq);
            trialMetrics.setNq_delta(Nq_delta);
            int stage = 0;
            for (Probabilities probs : trialData.getNormative_probs()) {
                List<Double> currentDecimalProbs = (probs != null && probs.getProbs() != null && !probs.getProbs().isEmpty())
                        ? ProbabilityUtils.normalizePercentProbabilities_Double(probs.getProbs(), null, ScoreComputerBase.EPSILON) : null;
                decimalProbs.add(new Probabilities(currentDecimalProbs));
                if (currentDecimalProbs == null) {
                    Nq.add(0d);
                    Nq_delta.add(0d);
                } else {
                    double ne = ScoreComputerBase.computeNegentropy(currentDecimalProbs, ProbabilityType.Decimal);
                    Nq.add(ne);
                    if (stage > 0) {
                        Nq_delta.add(ne - Nq.get(stage - 1));
                    } else {
                        Nq_delta.add(0d);
                    }
                }
                stage++;
            }
        }

        if (trialData.getAllocation() != null && !trialData.getAllocation().isEmpty()) {
            //Compute decimal troop allocations			
            ArrayList<Double> decimalAllocation = new ArrayList<Double>(trialData.getAllocation().size());
            trialMetrics.setAllocation(decimalAllocation);
            for (Double allocation : trialData.getAllocation()) {
                decimalAllocation.add(allocation * 0.01d);
            }

            if (probsLastStage != null) {
                if (taskNum <= 3) {
                    //Compute probability matching metrics for Tasks 1-3				
                    Double maxProb = Collections.max(probsLastStage);
                    trialMetrics.setPh(maxProb);
                    Set<Integer> maxProbIndices = MetricsUtils_Phase1.getTargetValIndices(probsLastStage, maxProb);
                    int maxAllocationIndex = MetricsUtils_Phase1.getMaxValIndex(decimalAllocation);
                    if (maxProbIndices.contains(maxAllocationIndex)) {
                        trialMetrics.setFh(1D);
                    } else {
                        trialMetrics.setFh(0D);
                    }
                    trialMetrics.setFh_Ph(trialMetrics.getFh() - maxProb);
                    trialMetrics.setFh_1(trialMetrics.getFh() - 1);
                } else if (taskNum <= 6) {
                    //Compute probability matching metrics for Tasks 4-6
                    //Create a distribution I where 1 is assigned to the
                    //group or location with the highest subject probability
                    //IF 2 OR MORE GROUPS TIED, SHOULD WE MODIFY THE I DISTRIBUTION?
                    ArrayList<Double> I = MetricsUtils_Phase1.createIDistribution(probsLastStage);
                    //Compute RMS between troop allocations and subject probabilities
                    trialMetrics.setRMS_F_P(MetricsUtils.computeRMSE(decimalAllocation, probsLastStage));
                    //Compute RMS between troop allocations and I distribution
                    trialMetrics.setRMS_F_I(MetricsUtils.computeRMSE(decimalAllocation, I));
                }
            }
        }

        if (trialData.getLayer_type() != null && !trialData.getLayer_type().isEmpty() && taskNum == 6) {
            //Compute confirmation preference metrics
            //Determine if SIGINT was selected
            GroupType sigintGroup = null;
            int sigintIndex = 0;
            for (String layer : trialData.getLayer_type()) {
                if (layer == null) {
                    break;
                } else if (layer.toLowerCase().contains("sig")) {
                    sigintGroup = MetricsUtils_Phase1.parseSigintGroup(layer);
                    break;
                } else {
                    sigintIndex++;
                }
            }
            if (sigintGroup != null) {
                //SIGINT was selected, determine if it was selected on one of the groups with the current highest subject probability
                trialMetrics.setSigint_selections(1);
                List<Double> probsSigintStage = null;
                if (trialData.getProbs() != null && sigintIndex < numStages) {
                    probsSigintStage = trialData.getProbs().get(sigintIndex).getProbs();
                    //System.out.println(probsSigintStage);
                }
                if (probsSigintStage != null) {
                    Set<Integer> maxProbIndices = MetricsUtils_Phase1.getMaxValIndices(probsSigintStage);
                    if (maxProbIndices.contains(sigintGroup.ordinal())) {
                        trialMetrics.setSigint_highest_group_selections(1);
                    } else {
                        trialMetrics.setSigint_highest_group_selections(0);
                    }
                }
                trialMetrics.setC((trialMetrics.getSigint_highest_group_selections()
                        / trialMetrics.getSigint_selections().doubleValue()) * 100d);
				//trialMetrics.setC_highest_best(trialMetrics.getSigint_highest_best_selections() / 
                //		trialMetrics.getSigint_selections().doubleValue());
            } else {
                trialMetrics.setSigint_selections(0);
                trialMetrics.setSigint_highest_group_selections(0);
                trialMetrics.setC(0D);
				//trialMetrics.setSigint_highest_best_selections(0);				
                //trialMetrics.setC_highest_best(0D);
            }
        }

        //Compute CFA bias metrics (RR, AI, CW, PM)
        if (metricsInfo.getRR_info() != null && metricsInfo.getRR_info().isAssessedForTask(taskNum)
                && metricsInfo.getRR_info().isCalculated()) {
            //Compute RR
            trialMetrics.setRR(cfaMetricsComputer.computeRR(trialMetrics.getNp(), trialMetrics.getNq(),
                    trialMetrics.getRR(), comparisonMetrics, metricsInfo.getRR_info(), taskNum, trialNum));
        }
        if (metricsInfo.getAI_info() != null && metricsInfo.getAI_info().isAssessedForTask(taskNum)
                && metricsInfo.getAI_info().isCalculated()) {
            //Compute AI
            trialMetrics.setAI(cfaMetricsComputer.computeAI(trialMetrics.getNp_delta(), trialMetrics.getNq_delta(),
                    trialMetrics.getAI(), comparisonMetrics, metricsInfo.getAI_info(), taskNum, trialNum));
        }
        if (metricsInfo.getCW_info() != null && metricsInfo.getCW_info().isAssessedForTask(taskNum)
                && metricsInfo.getCW_info().isCalculated()) {
            //Compute CW
            trialMetrics.setCW(cfaMetricsComputer.computeCW(trialMetrics.getNp_delta(), trialMetrics.getNq_delta(),
                    trialMetrics.getCW(), comparisonMetrics, metricsInfo.getCW_info(), taskNum, trialNum));
        }
        if (metricsInfo.getPM_RMS_info() != null && metricsInfo.getPM_RMS_info().isAssessedForTask(taskNum)
                && metricsInfo.getPM_RMS_info().isCalculated()) {
            //Compute PM for Tasks 4-6
            trialMetrics.setPM(cfaMetricsComputer.computePM_RMS(trialMetrics.getRMS_F_P(), trialMetrics.getRMS_F_I(),
                    trialMetrics.getPM(), comparisonMetrics, metricsInfo.getPM_RMS_info(), taskNum, trialNum));
        }

        //Compute CPA metrics (RSR, ASR, RMR)
        if (comparisonMetrics != null) {
            //Compute RSR, RSR_Bayesian, RSR_alt_1, RSR_alt_2, and ASR
            computeRSRandASRMetrics(trialMetrics, comparisonMetrics, metricsInfo, taskNum, trialNum);

            if (metricsInfo.getRMR_info() != null && metricsInfo.getRMR_info().isAssessedForTask(taskNum)
                    && metricsInfo.getRMR_info().isCalculated()) {
                //Compute RMR
                CPAMetric rmr = cpaMetricsComputer.computeRMR(trialData.getLs_index(), comparisonMetrics.getF_LS_percent(),
                        trialMetrics.getRMR(), metricsInfo.getRMR_info(), taskNum, trialNum);
                trialMetrics.setRMR(rmr);
            }
        }

        trialData.setCfa_metrics_stale(false);
        trialData.setCpa_metrics_stale(false);

        return trialMetrics;
    }

    /**
     * @param trialMetrics
     * @param comparisonMetrics
     * @param metricsInfo
     * @param taskNum
     * @param trialNum
     */
    protected void computeRSRandASRMetrics(TrialMetrics trialMetrics, TrialMetrics comparisonMetrics, MetricsInfo metricsInfo,
            Integer taskNum, Integer trialNum) {
        if (comparisonMetrics != null) {
            //TODO: also compute for Red tactic probs
            int numStages = trialMetrics.getProbs() != null ? trialMetrics.getProbs().size() : 0;
            if (numStages > 0 && metricsInfo.getRSR_info().isAssessedForTask(taskNum) && metricsInfo.getRSR_info().isCalculated()
                    && comparisonMetrics.getProbs() != null && comparisonMetrics.getProbs().size() == numStages) {
                //Compute RSR metrics
                List<Probabilities> probs = trialMetrics.getProbs();
                List<Probabilities> comparisonProbs = comparisonMetrics.getProbs();

                List<Double> Spm = trialMetrics.getSpm();
                if (Spm == null || Spm.size() != numStages) {
                    Spm = new ArrayList<Double>();
                    trialMetrics.setSpm(Spm);
                    for (int stage = 0; stage < numStages; stage++) {
                        Spm.add(null);
                    }
                }
                List<Double> Spr = trialMetrics.getSpr();
                if (Spr == null || Spr.size() != numStages) {
                    Spr = new ArrayList<Double>();
                    trialMetrics.setSpr(Spr);
                    for (int stage = 0; stage < numStages; stage++) {
                        Spr.add(null);
                    }
                }
                List<CPAMetric> RSR = trialMetrics.getRSR();
                if (RSR == null || RSR.size() != numStages) {
                    RSR = new ArrayList<CPAMetric>(numStages);
                    trialMetrics.setRSR(RSR);
                    String name = metricsInfo.getRSR_info().getName();
                    for (int stage = 0; stage < numStages; stage++) {
                        RSR.add(new CPAMetric(name));
                    }
                }
                List<CPAMetric> RSR_alt_1 = null;
                if (metricsInfo.getRSR_alt_1_info() != null && metricsInfo.getRSR_alt_1_info().isAssessedForTask(taskNum)
                        && metricsInfo.getRSR_alt_1_info().isCalculated()) {
                    RSR_alt_1 = trialMetrics.getRSR_alt_1();
                    if (RSR_alt_1 == null || RSR_alt_1.size() != numStages) {
                        RSR_alt_1 = new ArrayList<CPAMetric>(numStages);
                        trialMetrics.setRSR_alt_1(RSR_alt_1);
                        String name = metricsInfo.getRSR_alt_1_info().getName();
                        for (int stage = 0; stage < numStages; stage++) {
                            RSR_alt_1.add(new CPAMetric(name));
                        }
                    }
                }

                List<Probabilities> normativeProbs = null;
                List<Double> Spq = null;
                List<CPAMetric> RSR_Bayesian = null;
                List<CPAMetric> RSR_alt_2 = null;
                if (comparisonMetrics.getNormative_probs() != null
                        && comparisonMetrics.getNormative_probs().size() == numStages) {
                    normativeProbs = comparisonMetrics.getNormative_probs();
                    Spq = trialMetrics.getSpq();
                    if (Spq == null || Spq.size() != numStages) {
                        Spq = new ArrayList<Double>();
                        trialMetrics.setSpq(Spq);
                        for (int stage = 0; stage < numStages; stage++) {
                            Spq.add(null);
                        }
                    }
                    if (metricsInfo.getRSR_Bayesian_info() != null && metricsInfo.getRSR_Bayesian_info().isAssessedForTask(taskNum)
                            && metricsInfo.getRSR_Bayesian_info().isCalculated()) {
                        RSR_Bayesian = trialMetrics.getRSR_Bayesian();
                        if (RSR_Bayesian == null || RSR_Bayesian.size() != numStages) {
                            RSR_Bayesian = new ArrayList<CPAMetric>(numStages);
                            trialMetrics.setRSR_Bayesian(RSR_Bayesian);
                            String name = metricsInfo.getRSR_Bayesian_info().getName();
                            for (int stage = 0; stage < numStages; stage++) {
                                RSR_Bayesian.add(new CPAMetric(name));
                            }
                        }
                    }
                    if (metricsInfo.getRSR_alt_2_info() != null && metricsInfo.getRSR_alt_2_info().isAssessedForTask(taskNum)
                            && metricsInfo.getRSR_alt_2_info().isCalculated()) {
                        RSR_alt_2 = trialMetrics.getRSR_alt_2();
                        if (RSR_alt_2 == null || RSR_alt_2.size() != numStages) {
                            RSR_alt_2 = new ArrayList<CPAMetric>(numStages);
                            trialMetrics.setRSR_alt_2(RSR_alt_2);
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
                    ASR = trialMetrics.getASR();
                    if (ASR == null || ASR.size() != numStages) {
                        ASR = new ArrayList<CPAMetric>(numStages);
                        trialMetrics.setASR(ASR);
                        String name = metricsInfo.getASR_info().getName();
                        for (int stage = 0; stage < numStages; stage++) {
                            ASR.add(new CPAMetric(name));
                        }
                    }
                }

                for (int stage = 0; stage < numStages; stage++) {
                    List<Double> currentProbs = probs.get(stage).getProbs();
                    List<Double> currentComparisonProbs = comparisonProbs.get(stage).getProbs();
                    List<Double> currentNormativeProbs = (normativeProbs != null) ? normativeProbs.get(stage).getProbs() : null;

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

                    if (normativeProbs != null) {
                        //Compute Spq
                        Double spq = cpaMetricsComputer.computeSpq(currentComparisonProbs, currentNormativeProbs);
                        if(Spq != null) {
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
                        ASR.set(stage, cpaMetricsComputer.computeASR(currentProbs, currentComparisonProbs,
                                ASR.get(stage), metricsInfo.getASR_info(), taskNum, trialNum, stage));
                    }
                }
            }
        }
    }

    /**
     *
     * @param trials
     * @param averageTrial
     * @param averageComparisonTrialData
     * @param metricsInfo
     * @param taskNum
     * @return
     */
    public TrialData updateAverageTrialMetrics(List<TrialData> trials, TrialData averageTrial,
            TrialData averageComparisonTrialData, MetricsInfo metricsInfo, int taskNum) {
        if (averageTrial == null) {
            averageTrial = new TrialData();
        }
        TrialMetrics averageMetrics = averageTrial.getMetrics();
        if (averageMetrics == null) {
            averageMetrics = new TrialMetrics();
            averageTrial.setMetrics(averageMetrics);
        }

        averageTrial.setData_stale(true);

        if (trials != null && !trials.isEmpty()) {
            //***************** Compute means *****************
            averageMetrics.setNum_subjects(trials.size());
            int numTrials = trials.size(); //TODO: Consider storing numStages and numProbs in ExamData or MetricsInfo
            int numStages = trials.get(0).getProbs().size();
            int numProbs = trials.get(0).getProbs().get(0).getProbs().size();

            Double trial_time_avg = 0d;
            Double s1_score_avg = 0d;
            Double s2_score_avg = 0d;
            List<Double> probs_time_avg = MetricsUtils.initializeDoubleList(averageTrial.getProbs_time(), numStages);
            averageTrial.setProbs_time(probs_time_avg);

            List<Probabilities> percent_probs_avg = MetricsUtils_Phase1.initializeProbabilities(averageTrial.getProbs(), numStages, numProbs);
            averageTrial.setProbs(percent_probs_avg);
            List<Probabilities> normative_percent_probs_avg = MetricsUtils_Phase1.initializeProbabilities(averageTrial.getNormative_probs(), numStages, numProbs);
            averageTrial.setNormative_probs(normative_percent_probs_avg);

            Double circles_centers_time_avg = null;
            List<Double> center_x_avg = null;
            List<Double> center_y_avg = null;
            List<Double> circle_r_avg = null;
            List<Double> sigma_avg = null;
            if (taskNum == 2 || taskNum == 3) {
                circles_centers_time_avg = 0d;
                center_x_avg = MetricsUtils.initializeDoubleList(averageTrial.getCenter_x(), numProbs);
                averageTrial.setCenter_x(center_x_avg);
                center_y_avg = MetricsUtils.initializeDoubleList(averageTrial.getCenter_y(), numProbs);
                averageTrial.setCenter_y(center_y_avg);
                if (taskNum == 2) {
                    circle_r_avg = MetricsUtils.initializeDoubleList(averageTrial.getCircle_r(), numProbs);
                    averageTrial.setCircle_r(circle_r_avg);
                    sigma_avg = MetricsUtils.initializeDoubleList(averageTrial.getSigma(), numProbs);
                    averageTrial.setSigma(sigma_avg);
                }
            }

            List<Double> percent_allocation_avg = MetricsUtils.initializeDoubleList(averageTrial.getAllocation(), numProbs);
            averageTrial.setAllocation(percent_allocation_avg);
            Double allocation_time_avg = 0d;

            Double surprise_time_avg = 0d;
            Double surprise_avg = 0d;

            List<Integer> F_LS_count = null;
            Integer sigint_selections = null;
            Integer sigint_highest_group_selections = null;
            if (taskNum == 6) {
                F_LS_count = MetricsUtils.initializeIntegerList(averageMetrics.getF_LS_count(),
                        MetricsUtils_Phase1.LAYER_SEQUENCES.size());
                averageMetrics.setF_LS_count(F_LS_count);
                sigint_selections = 0;
                sigint_highest_group_selections = 0;
            }

            //Compute average trial data
            for (TrialData trial : trials) {
                trial_time_avg += trial.getTrial_time() != null ? trial.getTrial_time() : 0;
                s1_score_avg += trial.getS1_score() != null ? trial.getS1_score() : 0;
                s2_score_avg += trial.getS2_score() != null ? trial.getS2_score() : 0;
                probs_time_avg = MetricsUtils_Phase1.add(trial.getProbs_time(), probs_time_avg, probs_time_avg);

                List<Probabilities> probs = trial.getProbs();
                List<Probabilities> normativeProbs = trial.getNormative_probs();
                for (int stage = 0; stage < numStages; stage++) {
                    //System.out.println("stage " + (stage+1) + " of " + numStages); //DEBUG CODE
                    if (probs != null && stage < probs.size()) {
                        percent_probs_avg.set(stage, MetricsUtils_Phase1.add(probs.get(stage),
                                percent_probs_avg.get(stage),
                                percent_probs_avg.get(stage)));
                    }
                    if (normativeProbs != null && stage < normativeProbs.size()) {
                        normative_percent_probs_avg.set(stage, MetricsUtils_Phase1.add(normativeProbs.get(stage),
                                normative_percent_probs_avg.get(stage),
                                normative_percent_probs_avg.get(stage)));
                    }
                }

                if (taskNum == 2 || taskNum == 3) {
                    circles_centers_time_avg += trial.getCircles_centers_time() != null ? trial.getCircles_centers_time() : 0d;
                    center_x_avg = MetricsUtils_Phase1.add(trial.getCenter_x(), center_x_avg, center_x_avg);
                    center_y_avg = MetricsUtils_Phase1.add(trial.getCenter_y(), center_y_avg, center_y_avg);
                    if (taskNum == 2) {
                        circle_r_avg = MetricsUtils_Phase1.add(trial.getCircle_r(), circle_r_avg, circle_r_avg);
                        sigma_avg = MetricsUtils_Phase1.add(trial.getSigma(), sigma_avg, sigma_avg);
                    }
                }

                if (taskNum == 6) {
                    if (trial.getLs_index() != null) {
                        //Update layer sequence frequencies
                        if(F_LS_count != null) {
                            F_LS_count.set(trial.getLs_index(), F_LS_count.get(trial.getLs_index()) + 1);
                        }
                    }
                    if (trial.getMetrics() != null) {
                        //Update SIGINT selections
                        sigint_selections
                                += trial.getMetrics().getSigint_selections() != null ? trial.getMetrics().getSigint_selections() : 0;
                        sigint_highest_group_selections
                                += trial.getMetrics().getSigint_highest_group_selections() != null ? trial.getMetrics().getSigint_highest_group_selections() : 0;
                    }
                }

                percent_allocation_avg = MetricsUtils_Phase1.add(trial.getAllocation(), percent_allocation_avg,
                        percent_allocation_avg);
                allocation_time_avg += trial.getAllocation_time() != null ? trial.getAllocation_time() : 0d;

                surprise_time_avg += trial.getSurprise_time() != null ? trial.getSurprise_time() : 0d;
                surprise_avg += trial.getSurprise() != null ? trial.getSurprise() : 0d;
            }
            trial_time_avg = trial_time_avg / numTrials;
            averageTrial.setTrial_time(trial_time_avg);
            s1_score_avg = s1_score_avg / numTrials;
            averageTrial.setS1_score(s1_score_avg);
            s2_score_avg = s2_score_avg / numTrials;
            averageTrial.setS2_score(s2_score_avg);
            MetricsUtils.mean(probs_time_avg, numTrials);
            for (int stage = 0; stage < numStages; stage++) {
                MetricsUtils_Phase1.mean(percent_probs_avg.get(stage), numTrials);
                MetricsUtils_Phase1.mean(normative_percent_probs_avg.get(stage), numTrials);
            }
            if (taskNum == 2 || taskNum == 3) {
                circles_centers_time_avg = circles_centers_time_avg / numTrials;
                averageTrial.setCircles_centers_time(circles_centers_time_avg);
                MetricsUtils.mean(center_x_avg, numTrials);
                MetricsUtils.mean(center_y_avg, numTrials);
                if (taskNum == 2) {
                    MetricsUtils.mean(circle_r_avg, numTrials);
                    MetricsUtils.mean(sigma_avg, numTrials);
                }
            }
            averageMetrics.setF_LS_percent(MetricsUtils_Phase1.computePercentsFromCounts(
                    F_LS_count, averageMetrics.getF_LS_percent()));
            MetricsUtils.mean(percent_allocation_avg, numTrials);
            allocation_time_avg = allocation_time_avg / numTrials;
            averageTrial.setAllocation_time(allocation_time_avg);
            surprise_time_avg = surprise_time_avg / numTrials;
            averageTrial.setSurprise_time(surprise_time_avg);
            surprise_avg = surprise_avg / numTrials;
            averageTrial.setSurprise(surprise_avg);

            //***************** Compute average trial metrics data using average trial data *****************
            averageMetrics = updateTrialMetrics(averageTrial, metricsInfo, averageComparisonTrialData);
            if (taskNum == 6) {
                averageMetrics.setSigint_highest_group_selections(sigint_highest_group_selections);
                averageMetrics.setSigint_selections(sigint_selections);
                averageMetrics.setC((averageMetrics.getSigint_highest_group_selections()
                        / averageMetrics.getSigint_selections().doubleValue()) * 100d);
            }

			//***************** Compute standard deviations *****************			
            //TODO: Finish this
            //List<Double> center_x_std;
            //List<Double> center_y_std;		
            //List<Double> sigma_std;		
            //Double surprise_std;			
            //List<Probabilities> decimal_pro_std = null;
            //List<Probabilities> normative_decimal_probs_std = null;
            //List<Double> decimal_allocation_std = null;
        }

        averageTrial.setData_stale(false);

        return averageTrial;
    }
}