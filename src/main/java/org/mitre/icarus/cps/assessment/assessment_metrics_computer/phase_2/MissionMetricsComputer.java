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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.mitre.icarus.cps.assessment.assessment_metrics_computer.ITaskMetricsComputer;
import org.mitre.icarus.cps.assessment.assessment_metrics_computer.MetricUtils;
import org.mitre.icarus.cps.assessment.assessment_metrics_computer.MetricUtils.DistributionStats;
import org.mitre.icarus.cps.assessment.assessment_metrics_computer.phase_1.MetricsUtils;
import org.mitre.icarus.cps.assessment.data_model.base.DataType;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.CFAMetric;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.CPAMetric;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.MetricsInfo;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.MissionMetrics;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.RSRAndASRMissionMetrics;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.RSRAndASRTrialMetrics;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.SubjectSigintProbabilities;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.TrialData;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.TrialMetrics;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.trial_part_data.AttackProbabilityReportData;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.trial_part_data.SigintSelectionData;
import org.mitre.icarus.cps.exam.phase_2.testing.bluebook.RedTacticType;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbeType;

/**
 * Computes Phase 2 mission metrics.
 *
 * @author CBONACETO
 *
 */
public class MissionMetricsComputer extends MetricsComputer implements 
      ITaskMetricsComputer<TrialData, MissionMetrics, MetricsInfo>  {

    @Override
    public MissionMetrics updateCompletionStatus(MissionMetrics missionMetrics, 
            List<TrialData> trials) {
        return updateCompletionStatus(missionMetrics, trials, null);
    }

    @Override
    public MissionMetrics updateCompletionStatus(MissionMetrics missionMetrics, 
            List<TrialData> trials, TrialData modifiedTrial) {
        if (missionMetrics == null) {
            missionMetrics = new MissionMetrics();
            missionMetrics.setTrials(trials);
        }

        //Update all_trials_valid flag
        Boolean all_trials_valid = missionMetrics.isAll_trials_valid();
        if (modifiedTrial != null && all_trials_valid != null && all_trials_valid == true) {
            if (modifiedTrial.isTrial_valid() != null && !modifiedTrial.isTrial_valid()) {
                all_trials_valid = false;
            }
        } else {
            all_trials_valid = true;
            if (trials != null && !trials.isEmpty()) {
                for (TrialData trial : trials) {
                    if (trial == null || (trial.isTrial_valid() != null && !trial.isTrial_valid())) {
                        all_trials_valid = false;
                        break;
                    }
                }
            } else {
                all_trials_valid = false;
            }
        }
        missionMetrics.setAll_trials_valid(all_trials_valid);

        //Update task_complete flag
        Boolean task_complete = missionMetrics.isTask_complete();
        if (modifiedTrial != null && task_complete != null && task_complete == true) {
            if (modifiedTrial.isTrial_complete() == null || !modifiedTrial.isTrial_complete()) {
                task_complete = false;
            }
        } else {
            task_complete = true;
            if (trials != null && !trials.isEmpty()) {
                for (TrialData trial : trials) {
                    if (trial == null || trial.isTrial_complete() == null || !trial.isTrial_complete()) {
                        task_complete = false;
                        break;
                    }
                }
            } else {
                task_complete = false;
            }
        }
        missionMetrics.setTask_complete(task_complete);

        return missionMetrics;
    }
    
    @SuppressWarnings("null")
    @Override
    public MissionMetrics updateTaskMetrics(MissionMetrics missionMetrics, Double task_time, 
            List<TrialData> trials, MetricsInfo metricsInfo, MissionMetrics comparisonMetrics, 
            int taskNum) {                         
        if (missionMetrics == null) {
            missionMetrics = new MissionMetrics();
            missionMetrics.setTask_number(taskNum);
            missionMetrics.setTrials(trials);
        } else {
            if(missionMetrics.getTrials() != trials) {
                missionMetrics.setTrials(trials);
            }
        }
        if (metricsInfo == null) {
            metricsInfo = MetricsInfo.createDefaultMetricsInfo();
        }

        missionMetrics.setTask_time(task_time);
        missionMetrics.setMetrics_stale(true);

        if (trials != null && !trials.isEmpty()) {
            //Determine which metrics to compute
            boolean avgData = DataType.isAvgData(missionMetrics.getData_type());
            Boolean calc_AA = metricsInfo.getAA_info() != null && metricsInfo.getAA_info().isAssessedForTask(taskNum)
                    && metricsInfo.getAA_info().isCalculated();
            Boolean calc_PDE = metricsInfo.getPDE_info() != null && metricsInfo.getPDE_info().isAssessedForTask(taskNum)
                    && metricsInfo.getPDE_info().isCalculated();
            Boolean calc_RR = metricsInfo.getRR_info() != null && metricsInfo.getRR_info().isAssessedForTask(taskNum)
                    && metricsInfo.getRR_info().isCalculated();
            Boolean calc_AV = metricsInfo.getAV_info() != null && metricsInfo.getAV_info().isAssessedForTask(taskNum)
                    && metricsInfo.getAV_info().isCalculated();
            Boolean calc_PM = metricsInfo.getPM_info() != null && metricsInfo.getPM_info().isAssessedForTask(taskNum)
                    && metricsInfo.getPM_info().isCalculated();
            Boolean calc_CS = metricsInfo.getCS_info() != null && metricsInfo.getCS_info().isAssessedForTask(taskNum)
                    && metricsInfo.getCS_info().isCalculated();
            Boolean calc_CB = metricsInfo.getCB_info() != null && metricsInfo.getCB_info().isAssessedForTask(taskNum)
                    && metricsInfo.getCB_info().isCalculated();
            Boolean calc_SS = metricsInfo.getSS_info() != null && metricsInfo.getSS_info().isAssessedForTask(taskNum)
                    && metricsInfo.getSS_info().isCalculated();
            Boolean calc_ASR = metricsInfo.getASR_info() != null && metricsInfo.getASR_info().isAssessedForTask(taskNum)
                    && metricsInfo.getASR_info().isCalculated();
            Boolean calc_RSR_standard = metricsInfo.getRSR_info() != null && metricsInfo.getRSR_info().isAssessedForTask(taskNum)
                    && metricsInfo.getRSR_info().isCalculated();
            Boolean calc_RSR_Bayesian = metricsInfo.getRSR_Bayesian_info() != null
                    && metricsInfo.getRSR_Bayesian_info().isAssessedForTask(taskNum)
                    && metricsInfo.getRSR_Bayesian_info().isCalculated();
            Boolean calc_RSR_Spm_Spr_avg = metricsInfo.getRSR_Spm_Spr_avg_info() != null
                    && metricsInfo.getRSR_Spm_Spr_avg_info().isAssessedForTask(taskNum)
                    && metricsInfo.getRSR_Spm_Spq_avg_info().isCalculated();
            Boolean calc_RSR_Spm_Spq_avg = metricsInfo.getRSR_Spm_Spq_avg_info() != null
                    && metricsInfo.getRSR_Spm_Spq_avg_info().isAssessedForTask(taskNum) && metricsInfo.getRSR_Spm_Spq_avg_info().isCalculated();
            Boolean calc_RSR_alt_1 = metricsInfo.getRSR_alt_1_info() != null
                    && metricsInfo.getRSR_alt_1_info().isAssessedForTask(taskNum) && metricsInfo.getRSR_alt_1_info().isCalculated();
            Boolean calc_RSR_alt_2 = metricsInfo.getRSR_alt_2_info() != null
                    && metricsInfo.getRSR_alt_2_info().isAssessedForTask(taskNum) && metricsInfo.getRSR_alt_2_info().isCalculated();
            Boolean calc_RSR = calc_RSR_standard || calc_RSR_Bayesian || calc_RSR_Spm_Spr_avg || calc_RSR_Spm_Spq_avg
                    || calc_RSR_alt_1 || calc_RSR_alt_2;
            Boolean calc_RMR = metricsInfo.getRMR_info() != null && metricsInfo.getRMR_info().isAssessedForTask(taskNum)
                    && metricsInfo.getRMR_info().isCalculated();            
            
            //Initialize avg SIGINT probabilities if subjects provided them in the mission
            Boolean calc_SIGINT_avg = metricsInfo.getSigint_avg_info() != null
                    && metricsInfo.getSigint_avg_info().isAssessedForTask(taskNum);            
            Double ptChatter_avg = null;
            Integer ptChatter_count = null;
            Double ptSilent_avg = null;
            Integer ptSilent_count = null; 
            if(calc_SIGINT_avg) {
                ptChatter_avg = 0d;
                ptChatter_count = 0;
                ptSilent_avg = 0d;
                ptSilent_count = 0;
            }
            
            //Initialize metrics
            Double AA_smr_score = null; //AA SMR (Simple Match Rate) score over all trials
            Integer AA_smr_score_count = null;
            if (calc_AA) {
                AA_smr_score = 0d;
                AA_smr_score_count = 0;
            }            
            Double PDE_smr_score = null; //PDE SMR (Simple Match Rate) score over all trials
            Integer PDE_smr_score_count = null;
            if (calc_PDE) {
                PDE_smr_score = 0d;
                PDE_smr_score_count = 0;
            }
            Double RR_smr_score = null; //RR SMR (Simple Match Rate) score over all trials/locations
            Integer RR_smr_score_count = null;
            if (calc_RR) {
                RR_smr_score = 0d;
                RR_smr_score_count = 0;
            }
            Double AV_smr_score = null; //AV SMR (Simple Match Rate) score over all trials
            Integer AV_smr_score_count = null;
            if (calc_AV) {
                AV_smr_score = 0d;
                AV_smr_score_count = 0;
            }
            Double PM_normativeBlueOptionSelectionFrequency = null; //Frequency with which the normative Blue option selections across all trials
            Integer PM_normativeBlueOptionSelectionFrequency_count = null;
            if (!avgData && calc_PM) {
                PM_normativeBlueOptionSelectionFrequency = 0d;
                PM_normativeBlueOptionSelectionFrequency_count = 0;
            }
            Double CS_sigintHighestPSelectionFrequency = null; //Frequency with which the normative SIGINT location selections across all trials
            Integer CS_sigintHighestPSelectionFrequency_count = null;
            if (!avgData && calc_CS) {
                CS_sigintHighestPSelectionFrequency = 0d;
                CS_sigintHighestPSelectionFrequency_count = 0;
            }            
            List<Double> CB_trialsNeededToDetectRedTacticChanges = null; //Number of trials needed to detect each Red tactics change                        
            List<Boolean> CB_redTacticsChangedDetected = null; //Whether each Red tactics change was detected
            Double SS_percentTrialsReviewedInBatchPlot_avg = null; //Average percent of trials reviewed in batch plots across all batch plot trials
            Integer SS_percentTrialsReviewedInBatchPlot_count = null;
            if (!avgData && calc_SS) {
                SS_percentTrialsReviewedInBatchPlot_avg = 0d;
                SS_percentTrialsReviewedInBatchPlot_count = 0;
            }
            RSRAndASRMissionMetrics rsrAsrMetrics = missionMetrics.getRSR_ASR();            
            int numStages = 0;
            if (calc_RSR || calc_ASR) {
                TrialData trial = trials.get(0);
                List<String> stageNames = trial != null && trial.getMetrics() != null 
                        && trial.getMetrics().getRSRandASR_metrics() != null ? 
                        trial.getMetrics().getRSRandASR_metrics().getStageNames() : null;
                numStages = stageNames != null ? stageNames.size() : 0;
                if(rsrAsrMetrics == null) {
                    rsrAsrMetrics = new RSRAndASRMissionMetrics();                
                    missionMetrics.setRSR_ASR(rsrAsrMetrics);
                }
                rsrAsrMetrics.setStageNames(stageNames);
            }       
            Double ASR_avg = null; //Average ASR across all stages of all trials
            Integer ASR_avg_count = null;
            List<Double> ASR_stage_avg = null;
            List<Integer> ASR_stage_avg_count = null;
            if (calc_ASR) {
                ASR_avg = 0d;
                ASR_avg_count = 0;
                ASR_stage_avg = MetricsUtils.initializeDoubleList(rsrAsrMetrics.getASR_stage_avg(), numStages);
                rsrAsrMetrics.setASR_stage_avg(ASR_stage_avg);
                ASR_stage_avg_count = MetricsUtils.initializeIntegerList(ASR_stage_avg_count, numStages);
            }
            Double RSR_avg = null; //Average RSR across all stages of all trials
            Integer RSR_avg_count = null; 
            List<Double> RSR_stage_avg = null; //Average RSR for each stage (across all trials)
            List<Integer> RSR_stage_avg_count = null;
            Double Spm_avg = null; //Average Spm across all stages of all trials
            Integer Spm_avg_count = null;
            List<Double> Spm_stage_avg = null; //Average Spm for each stage (across all trials)
            List<Integer> Spm_stage_avg_count = null;
            Double Spr_avg = null; //Average Spr across all stages of all trials
            Integer Spr_avg_count = null;
            List<Double> Spr_stage_avg = null; //Average Spr for each stage (across all trials)
            List<Integer> Spr_stage_avg_count = null;
            Double Spq_avg = null; //Average Spq across all stages of all trials
            Integer Spq_avg_count = null;
            List<Double> Spq_stage_avg = null; //Average Spq for each stage (across all trials)
            List<Integer> Spq_stage_avg_count = null;
            if (calc_RSR) {
                RSR_avg = 0d;
                RSR_avg_count = 0;
                RSR_stage_avg = MetricsUtils.initializeDoubleList(rsrAsrMetrics.getRSR_stage_avg(), numStages);
                rsrAsrMetrics.setRSR_stage_avg(RSR_stage_avg);
                RSR_stage_avg_count = MetricsUtils.initializeIntegerList(RSR_stage_avg_count, numStages);
                Spm_avg = 0d;
                Spm_avg_count = 0;
                Spm_stage_avg = MetricsUtils.initializeDoubleList(rsrAsrMetrics.getSpm_stage_avg(), numStages);
                rsrAsrMetrics.setSpm_stage_avg(Spm_stage_avg);
                Spm_stage_avg_count = MetricsUtils.initializeIntegerList(Spm_stage_avg_count, numStages);
                Spr_avg = 0d;
                Spr_avg_count = 0;
                Spr_stage_avg = MetricsUtils.initializeDoubleList(rsrAsrMetrics.getSpr_stage_avg(), numStages);
                rsrAsrMetrics.setSpr_stage_avg(Spr_stage_avg);
                Spr_stage_avg_count = MetricsUtils.initializeIntegerList(Spr_stage_avg_count, numStages);
                Spq_avg = 0d;
                Spq_avg_count = 0;
                Spq_stage_avg = MetricsUtils.initializeDoubleList(rsrAsrMetrics.getSpq_stage_avg(), numStages);
                rsrAsrMetrics.setSpq_stage_avg(Spq_stage_avg);
                Spq_stage_avg_count = MetricsUtils.initializeIntegerList(Spq_stage_avg_count, numStages);
            }
            Double RSR_Bayesian_avg = null; //Average RSR-Bayesian across all stages of all trials
            Integer RSR_Bayesian_avg_count = null;
            List<Double> RSR_Bayesian_stage_avg = null; //Average RSR-Bayesian for each stage (across all trials)
            List<Integer> RSR_Bayesian_stage_avg_count = null;
            if (calc_RSR_Bayesian) {
                RSR_Bayesian_avg = 0d;
                RSR_Bayesian_avg_count = 0;
                RSR_Bayesian_stage_avg = MetricsUtils.initializeDoubleList(rsrAsrMetrics.getRSR_Bayesian_stage_avg(), numStages);
                rsrAsrMetrics.setRSR_Bayesian_stage_avg(RSR_Bayesian_stage_avg);
                RSR_Bayesian_stage_avg_count = MetricsUtils.initializeIntegerList(RSR_Bayesian_stage_avg_count, numStages);
            }
            Double RSR_alt_1_avg = null; //Average RSR-alt-1 across all stages of all trials
            Integer RSR_alt_1_avg_count = null;
            List<Double> RSR_alt_1_stage_avg = null; //Average RSR-alt-1 for each stage (across all trials)
            List<Integer> RSR_alt_1_stage_avg_count = null;
            if (calc_RSR_alt_1) {
                RSR_alt_1_avg = 0d;
                RSR_alt_1_avg_count = 0;
                RSR_alt_1_stage_avg = MetricsUtils.initializeDoubleList(rsrAsrMetrics.getRSR_alt_1_stage_avg(), numStages);
                rsrAsrMetrics.setRSR_alt_1_stage_avg(RSR_alt_1_stage_avg);
                RSR_alt_1_stage_avg_count = MetricsUtils.initializeIntegerList(RSR_alt_1_stage_avg_count, numStages);
            }
            Double RSR_alt_2_avg = null; //Average RSR-alt-2 across all stages of all trials
            Integer RSR_alt_2_avg_count = null;
            List<Double> RSR_alt_2_stage_avg = null; //Average RSR-alt-2 for each stage (across all trials)
            List<Integer> RSR_alt_2_stage_avg_count = null;
            if (calc_RSR_alt_2) {
                RSR_alt_2_avg = 0d;
                RSR_alt_2_avg_count = 0;
                RSR_alt_2_stage_avg = MetricsUtils.initializeDoubleList(rsrAsrMetrics.getRSR_alt_2_stage_avg(), numStages);
                rsrAsrMetrics.setRSR_alt_2_stage_avg(RSR_alt_2_stage_avg);
                RSR_alt_2_stage_avg_count = MetricsUtils.initializeIntegerList(RSR_alt_2_stage_avg_count, numStages);
            }
            //protected Double RMR_sigint_avg;
            //protected Double RMR_sigint_std;
            //protected Double RMR_blueAction_avg;    
            //protected Double RMR_blueAction_std;
            Double RMR_sigint_avg = null; //RMR average score across all trials (for just SIGINT selections, if available)
            Integer RMR_sigint_avg_count = null;            
            if (calc_RMR) {
                RMR_sigint_avg = 0d;
                RMR_sigint_avg_count = 0;
            }
            Double RMR_blueAction_avg = null; //RMR average score across all trials (for just Blue Action selections)
            Integer RMR_blueAction_avg_count = null;            
            if (calc_RMR) {
                RMR_blueAction_avg = 0d;
                RMR_blueAction_avg_count = 0;
            }
            Double RMR_avg = null; //RMR average score across all trials (for SIGINT selections + Blue Action selections)
            Integer RMR_avg_count = null;            
            if (calc_RMR) {
                RMR_avg = 0d;
                RMR_avg_count = 0;
            }
            
            //Calculate metrics
            if (calc_CB) {
                //Calculate number of trials needed to detect each Red tactics change,
                //and whether the changes were detected
                RedTacticsChangeDetections changeDetections = 
                        computeRedTacticChangeDetections(trials, true);
                if(changeDetections != null) {
                    CB_trialsNeededToDetectRedTacticChanges = changeDetections.trialsToDetectChanges;
                    CB_redTacticsChangedDetected = changeDetections.changesDetected;                    
                }
                //CB_trialsNeededToDetectRedTacticChanges = 
                //       computeTrialsNeededToDetectRedTacticChanges(trials, true);
            }            
            for (TrialData trial : trials) {                
                //Update average SIGINT probabilities
                if (calc_SIGINT_avg) {
                    SigintProbability sigintProb = MissionMetricsComputer.getSigintProbability(
                            trial.getRedActivityDetected(), trial.getBlueLocationIds(),
                            trial.getSigintSelection(),
                            trial.getAttackProbabilityReport(TrialPartProbeType.AttackProbabilityReport_Pt));
                    //System.out.println("SIGINT Prob: " + sigintProb != null ? sigintProb.sigintProb : null);
                    if (sigintProb != null && sigintProb.sigintProb != null) {
                        if (sigintProb.redActivityDetected != null && sigintProb.redActivityDetected) {
                            //Update P(Attack|Chatter) average
                            ptChatter_avg += sigintProb.sigintProb;
                            ptChatter_count++;
                        } else {
                            //Update P(Attack|Silent) average
                            ptSilent_avg += sigintProb.sigintProb;
                            ptSilent_count++;
                        }
                    }
                }
                TrialMetrics trialMetrics = (trial != null) ? trial.getMetrics() : null;
                if (trial != null && trialMetrics != null) {
                    int trialNum = trial.getTrial_number();                   
                    
                    //Update CFA Metrics
                    if (calc_AA && trialMetrics.getAA_metrics()!= null && trialMetrics.getAA_metrics().score != null
                            && metricsInfo.getAA_info().isAssessedForStage(taskNum, trialNum, null)) {
                        //Update AA (Anchoring & Adjustment)
                        AA_smr_score += trialMetrics.getAA_metrics().score;
                        AA_smr_score_count++;
                    }
                    if (calc_PDE && trialMetrics.getPDE_metrics()!= null && trialMetrics.getPDE_metrics().score != null
                            && metricsInfo.getPDE_info().isAssessedForStage(taskNum, trialNum, null)) {
                        //Update PDE (Persistentence of Discredited Evidence)
                        PDE_smr_score += trialMetrics.getPDE_metrics().score;
                        PDE_smr_score_count++;
                    }
                    if (calc_RR && trialMetrics.getRR_metrics() != null && !trialMetrics.getRR_metrics().isEmpty()) {
                        //Update RR (Representativeness)
                        int i = 1;
                        for (CFAMetric RR : trialMetrics.getRR_metrics()) {                            
                            if (RR != null && RR.score != null
                                    && metricsInfo.getRR_info().isAssessedForStage(taskNum, trialNum, i)) {                                
                                RR_smr_score += RR.score;
                                RR_smr_score_count++;
                            }
                            i++;
                        }
                    }                               
                    if (calc_AV && trialMetrics.getAV_metrics()!= null && trialMetrics.getAV_metrics().score != null
                            && metricsInfo.getAV_info().isAssessedForStage(taskNum, trialNum, null)) {
                        //Update AV (Availability)
                        AV_smr_score += trialMetrics.getAV_metrics().score;
                        AV_smr_score_count++;
                    }                    
                    if (!avgData && calc_PM && trialMetrics.getPM_normativeBlueOptionSelections() != null
                            && metricsInfo.getPM_info().isAssessedForStage(taskNum, trialNum, null)) {
                        //Update PM (Probability Matching)
                        PM_normativeBlueOptionSelectionFrequency += trialMetrics.getPM_normativeBlueOptionSelections();
                        PM_normativeBlueOptionSelectionFrequency_count++;
                    }
                    if (!avgData && calc_CS && trialMetrics.getCS_sigintAtHighestPaLocationSelections() != null
                            && metricsInfo.getCS_info().isAssessedForStage(taskNum, trialNum, null)) {
                        //Update CS (Confirmation Bias in Seeking Evidence)
                        CS_sigintHighestPSelectionFrequency += 
                                trialMetrics.getCS_sigintAtHighestPaLocationSelections();
                        CS_sigintHighestPSelectionFrequency_count++;
                    }
                    if (!avgData && calc_SS && trialMetrics.getSS_percentTrialsReviewedInBatchPlot() != null
                            && metricsInfo.getSS_info().isAssessedForStage(taskNum, trialNum, null)) {
                        //Update SS (Satisfaction of Search)
                        //System.out.println("Updating SS: " + trialMetrics.getSS_percentTrialsReviewedInBatchPlot());
                        SS_percentTrialsReviewedInBatchPlot_avg += 
                                trialMetrics.getSS_percentTrialsReviewedInBatchPlot();
                        SS_percentTrialsReviewedInBatchPlot_count++;
                    }
                    
                    //Update CPA metrics
                    RSRAndASRTrialMetrics rsrAsrTrialMetrics = trialMetrics.getRSRandASR_metrics();
                    if (rsrAsrTrialMetrics != null) {
                        if (calc_ASR) {
                            //Update ASR						
                            if (rsrAsrTrialMetrics.getASR() != null && !rsrAsrTrialMetrics.getASR().isEmpty()) {
                                int i = 0;
                                for (CPAMetric asr : rsrAsrTrialMetrics.getASR()) {
                                    if (asr != null && asr.assessed && asr.score != null) {
                                        ASR_avg += asr.score;
                                        ASR_avg_count++;
                                        ASR_stage_avg.set(i, ASR_stage_avg.get(i) + asr.score);
                                        ASR_stage_avg_count.set(i, ASR_stage_avg_count.get(i) + 1);
                                    }
                                    i++;
                                }
                            }
                        }
                        if (calc_RSR) {
                            //Update avg RSR (standard), Spm, Spr, and Spq
                            if (rsrAsrTrialMetrics.getRSR() != null && !rsrAsrTrialMetrics.getRSR().isEmpty()) {
                                for (int i = 0; i < rsrAsrTrialMetrics.getRSR().size(); i++) {
                                    CPAMetric rsr = rsrAsrTrialMetrics.getRSR().get(i);
                                    if (rsr != null && rsr.assessed) {
                                        //Update RSR
                                        if (rsr.score != null) {
                                            RSR_avg += rsr.score;
                                            RSR_avg_count++;
                                            RSR_stage_avg.set(i, RSR_stage_avg.get(i) + rsr.score);
                                            RSR_stage_avg_count.set(i, RSR_stage_avg_count.get(i) + 1);
                                        }
                                        //Update Spm, Spr, and Spq
                                        if (rsrAsrTrialMetrics.getSpm() != null
                                                && i < rsrAsrTrialMetrics.getSpm().size()) {
                                            Double Spm = rsrAsrTrialMetrics.getSpm().get(i);
                                            if (Spm != null) {
                                                Spm_avg += Spm;
                                                Spm_avg_count++;
                                                Spm_stage_avg.set(i, Spm_stage_avg.get(i) + Spm);
                                                Spm_stage_avg_count.set(i, Spm_stage_avg_count.get(i) + 1);
                                            }
                                        }
                                        if (rsrAsrTrialMetrics.getSpr() != null
                                                && i < rsrAsrTrialMetrics.getSpr().size()) {
                                            Double Spr = rsrAsrTrialMetrics.getSpr().get(i);
                                            if (Spr != null) {
                                                Spr_avg += Spr;
                                                Spr_avg_count++;
                                                Spr_stage_avg.set(i, Spr_stage_avg.get(i) + Spr);
                                                Spr_stage_avg_count.set(i, Spr_stage_avg_count.get(i) + 1);
                                            }
                                        }
                                        if (rsrAsrTrialMetrics.getSpq() != null 
                                                && i < rsrAsrTrialMetrics.getSpq().size()) {
                                            Double Spq = rsrAsrTrialMetrics.getSpq().get(i);
                                            if (Spq != null) {
                                                Spq_avg += Spq;
                                                Spq_avg_count++;
                                                Spq_stage_avg.set(i, Spq_stage_avg.get(i) + Spq);
                                                Spq_stage_avg_count.set(i, Spq_stage_avg_count.get(i) + 1);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (calc_RSR_Bayesian) {
                            //Update RSR_Bayesian
                            if (rsrAsrTrialMetrics.getRSR_Bayesian() != null
                                    && !rsrAsrTrialMetrics.getRSR_Bayesian().isEmpty()) {
                                int i = 0;
                                for (CPAMetric rsr_bayesian : rsrAsrTrialMetrics.getRSR_Bayesian()) {
                                    if (rsr_bayesian != null && rsr_bayesian.assessed && rsr_bayesian.score != null) {
                                        RSR_Bayesian_avg += rsr_bayesian.score;
                                        RSR_Bayesian_avg_count++;
                                        RSR_Bayesian_stage_avg.set(i, RSR_Bayesian_stage_avg.get(i) + rsr_bayesian.score);
                                        RSR_Bayesian_stage_avg_count.set(i, RSR_Bayesian_stage_avg_count.get(i) + 1);
                                    }
                                    i++;
                                }
                            }
                        }
                        if (calc_RSR_alt_1) {
                            //Update RSR_alt_1
                            if (rsrAsrTrialMetrics.getRSR_alt_1() != null 
                                    && !rsrAsrTrialMetrics.getRSR_alt_1().isEmpty()) {
                                int i = 0;
                                for (CPAMetric rsr_alt_1 : rsrAsrTrialMetrics.getRSR_alt_1()) {
                                    if (rsr_alt_1 != null && rsr_alt_1.assessed && rsr_alt_1.score != null) {
                                        RSR_alt_1_avg += rsr_alt_1.score;
                                        RSR_alt_1_avg_count++;
                                        RSR_alt_1_stage_avg.set(i, RSR_alt_1_stage_avg.get(i) + rsr_alt_1.score);
                                        RSR_alt_1_stage_avg_count.set(i, RSR_alt_1_stage_avg_count.get(i) + 1);
                                    }
                                    i++;
                                }
                            }
                        }
                        if (calc_RSR_alt_2) {
                            //Update RSR_alt_2
                            if (rsrAsrTrialMetrics.getRSR_alt_2() != null
                                    && !rsrAsrTrialMetrics.getRSR_alt_2().isEmpty()) {
                                int i = 0;
                                for (CPAMetric rsr_alt_2 : rsrAsrTrialMetrics.getRSR_alt_2()) {
                                    if (rsr_alt_2 != null && rsr_alt_2.assessed && rsr_alt_2.score != null) {
                                        RSR_alt_2_avg += rsr_alt_2.score;
                                        RSR_alt_2_avg_count++;
                                        RSR_alt_2_stage_avg.set(i, RSR_alt_2_stage_avg.get(i) + rsr_alt_2.score);
                                        RSR_alt_2_stage_avg_count.set(i, RSR_alt_2_stage_avg_count.get(i) + 1);
                                    }
                                    i++;
                                }
                            }
                        }
                        if (calc_RMR) {
                            //Update RMR                            
                            CPAMetric RMR_sigint = trialMetrics.getRMR_sigint_metrics();
                            if(RMR_sigint != null && RMR_sigint.assessed && RMR_sigint.score != null) {
                                RMR_sigint_avg += RMR_sigint.score;
                                RMR_sigint_avg_count++;
                                RMR_avg += RMR_sigint.score;
                                RMR_avg_count++;
                            }
                            CPAMetric RMR_blueActions = trialMetrics.getRMR_blueAction_metrics();
                            if(RMR_blueActions != null && RMR_blueActions.assessed 
                                    && RMR_blueActions.score != null) {
                                RMR_blueAction_avg += RMR_blueActions.score;
                                RMR_blueAction_avg_count++;
                                RMR_avg += RMR_blueActions.score;
                                RMR_avg_count++;
                            }
                        }
                    }
                }
            } //end for (TrialData trial : trials)            
            
            //Calculate average SIGINT probabilities for the mission            
            if (calc_SIGINT_avg) {
                ptChatter_avg = ptChatter_count > 0 ? ptChatter_avg / ptChatter_count : null;
                ptSilent_avg = ptSilent_count > 0 ? ptSilent_avg / ptSilent_count : null;
                if (missionMetrics.getSigintProbs_avg() == null) {
                    missionMetrics.setSigintProbs_avg(new SubjectSigintProbabilities(
                        ptChatter_avg * 100d, ptSilent_avg * 100d));
                } else {
                    missionMetrics.getSigintProbs_avg().setPtChatter(ptChatter_avg * 100.d);
                    missionMetrics.getSigintProbs_avg().setPtSilent(ptSilent_avg * 100.d);
                }
            }

            //Calcuate AA score for the mission
            if (calc_AA) {
                missionMetrics.setAA_smr_score(AA_smr_score_count > 0 ? AA_smr_score / AA_smr_score_count : null);
            } else {
                missionMetrics.setAA_smr_score(null);
            }

            //Calcuate PDE score for the mission
            if (calc_PDE) {
                missionMetrics.setPDE_smr_score(PDE_smr_score_count > 0 ? PDE_smr_score / PDE_smr_score_count : null);
            } else {
                missionMetrics.setPDE_smr_score(null);
            }
            
            //Calcuate RR score for the mission
            if (calc_RR) {
                missionMetrics.setRR_smr_score(RR_smr_score_count > 0 ? RR_smr_score / RR_smr_score_count : null);
            } else {
                missionMetrics.setRR_smr_score(null);
            }
            
            //Calcuate AV score for the mission
            if (calc_AV) {
                missionMetrics.setAV_smr_score(AV_smr_score_count > 0 ? AV_smr_score / AV_smr_score_count : null);
            } else {
                missionMetrics.setAV_smr_score(null);
            }
            
            //Calcuate PM score for the mission
            if (calc_PM) {
                if (!avgData) {
                    missionMetrics.setPM_normativeBlueOptionSelectionFrequency(
                            PM_normativeBlueOptionSelectionFrequency_count > 0
                            ? PM_normativeBlueOptionSelectionFrequency / PM_normativeBlueOptionSelectionFrequency_count : null);
                }
                missionMetrics.setPM_metrics(cfaMetricsComputer.computePM(
                        missionMetrics.getPM_normativeBlueOptionSelectionFrequency(),
                        comparisonMetrics != null ? comparisonMetrics.getPM_normativeBlueOptionSelectionFrequency() : null,
                        missionMetrics.getPM_metrics(),
                        comparisonMetrics != null ? comparisonMetrics.getPM_metrics() : null, 
                        metricsInfo.getPM_info()));
            } else {
                missionMetrics.setPM_normativeBlueOptionSelectionFrequency(null);
                missionMetrics.setPM_metrics(null);
            } 
            
            //Calculate CS score for the mission
            if (calc_CS) {
                if (!avgData) {
                    missionMetrics.setCS_sigintHighestPaSelectionFrequency(
                            CS_sigintHighestPSelectionFrequency_count > 0
                            ? CS_sigintHighestPSelectionFrequency / CS_sigintHighestPSelectionFrequency_count : null);
                }
                missionMetrics.setCS_metrics(cfaMetricsComputer.computeCS(
                        missionMetrics.getCS_sigintHighestPaSelectionFrequency(),
                        comparisonMetrics != null ? comparisonMetrics.getCS_sigintHighestPaSelectionFrequency() : null,
                        missionMetrics.getCS_metrics(),
                        comparisonMetrics != null ? comparisonMetrics.getCS_metrics() : null, 
                        metricsInfo.getCS_info()));
            } else {
                missionMetrics.setCS_sigintHighestPaSelectionFrequency(null);
                missionMetrics.setCS_metrics(null);
            } 
            
            //Calculate CB score for the mission
            if (calc_CB) {
                Double CB_trialsNeededToDetectRedTacticChanges_avg = null;
                Integer CB_trialsNeededToDetectRedTacticChanges_count = null;
                if(CB_trialsNeededToDetectRedTacticChanges != null && 
                        !CB_trialsNeededToDetectRedTacticChanges.isEmpty()) {
                    CB_trialsNeededToDetectRedTacticChanges_avg = 0.d;
                    CB_trialsNeededToDetectRedTacticChanges_count = 0;
                    for(Double trialsNeeded : CB_trialsNeededToDetectRedTacticChanges) {
                        if(trialsNeeded != null) {
                            CB_trialsNeededToDetectRedTacticChanges_avg += trialsNeeded;
                            CB_trialsNeededToDetectRedTacticChanges_count++;
                        }
                    }              
                }
                missionMetrics.setCB_numRedTacticsChanges(CB_trialsNeededToDetectRedTacticChanges != null ?
                        CB_trialsNeededToDetectRedTacticChanges.size() : null);
                missionMetrics.setCB_trialsNeededToDetectRedTacticChanges_avg(
                        CB_trialsNeededToDetectRedTacticChanges_avg != null && CB_trialsNeededToDetectRedTacticChanges_count > 0 ?
                                  CB_trialsNeededToDetectRedTacticChanges_avg / CB_trialsNeededToDetectRedTacticChanges_count : null);
                missionMetrics.setCB_redTacticsChangedDetected(CB_redTacticsChangedDetected);
                missionMetrics.setCB_metrics(cfaMetricsComputer.computeCB(
                        missionMetrics.getCB_trialsNeededToDetectRedTacticChanges_avg(),
                        comparisonMetrics != null ? comparisonMetrics.getCB_trialsNeededToDetectRedTacticChanges_avg() : null,
                        missionMetrics.getCB_metrics(),
                        comparisonMetrics != null ? comparisonMetrics.getCB_metrics() : null, 
                        metricsInfo.getCB_info()));
            } else {
                missionMetrics.setCB_numRedTacticsChanges(null);
                missionMetrics.setCB_trialsNeededToDetectRedTacticChanges_avg(null);
                missionMetrics.setCB_metrics(null);
            }            
            
            //Calculate SS score for the mission
            if (calc_SS) {
                if (!avgData) {
                    missionMetrics.setSS_percentTrialsReviewedInBatchPlot_avg(
                            SS_percentTrialsReviewedInBatchPlot_count > 0
                            ? SS_percentTrialsReviewedInBatchPlot_avg / SS_percentTrialsReviewedInBatchPlot_count : null);
                }
                missionMetrics.setSS_metrics(cfaMetricsComputer.computeSS(
                        missionMetrics.getSS_percentTrialsReviewedInBatchPlot_avg(),
                        comparisonMetrics != null ? comparisonMetrics.getSS_percentTrialsReviewedInBatchPlot_avg() : null,
                        missionMetrics.getSS_metrics(),
                        comparisonMetrics != null ? comparisonMetrics.getSS_metrics() : null, 
                        metricsInfo.getSS_info()));
            } else {
                missionMetrics.setSS_percentTrialsReviewedInBatchPlot_avg(null);
                missionMetrics.setSS_metrics(null);
            }
            
            //Calculate average ASR for the mission
            if (calc_ASR) {
                rsrAsrMetrics.setASR_avg(ASR_avg_count > 0 ? ASR_avg / ASR_avg_count : null);
                for (int i = 0; i < numStages; i++) {
                    int count = ASR_stage_avg_count.get(i);
                    ASR_stage_avg.set(i, count > 0 ? ASR_stage_avg.get(i) / count : null);
                }
            } else {
                rsrAsrMetrics.setASR_avg(null);
                rsrAsrMetrics.setASR_stage_avg(null);
            }
            
            //Cacluate average RSR, Spm, Spr, and Spq for the mission
            if (calc_RSR) {
                Spm_avg = Spm_avg_count > 0 ? Spm_avg / Spm_avg_count : 0d;
                Spr_avg = Spr_avg_count > 0 ? Spr_avg / Spr_avg_count : 0d;
                Spq_avg = Spq_avg_count > 0 ? Spq_avg / Spq_avg_count : 0d;
                rsrAsrMetrics.setSpm_avg(Spm_avg);
                rsrAsrMetrics.setSpr_avg(Spr_avg);
                rsrAsrMetrics.setSpq_avg(Spq_avg);
                rsrAsrMetrics.setRSR_avg(RSR_avg_count > 0 ? RSR_avg / RSR_avg_count : null);
                for (int i = 0; i < numStages; i++) {
                    int count = RSR_stage_avg_count.get(i);
                    RSR_stage_avg.set(i, count > 0 ? RSR_stage_avg.get(i) / count : null);
                    count = Spm_stage_avg_count.get(i);
                    Spm_stage_avg.set(i, count > 0 ? Spm_stage_avg.get(i) / count : null);
                    count = Spr_stage_avg_count.get(i);
                    Spr_stage_avg.set(i, count > 0 ? Spr_stage_avg.get(i) / count : null);
                    count = Spq_stage_avg_count.get(i);
                    Spq_stage_avg.set(i, count > 0 ? Spq_stage_avg.get(i) / count : null);
                }
            } else {
                rsrAsrMetrics.setSpm_avg(null);
                rsrAsrMetrics.setSpm_stage_avg(null);
                rsrAsrMetrics.setSpr_avg(null);
                rsrAsrMetrics.setSpr_stage_avg(null);
                rsrAsrMetrics.setSpq_avg(null);
                rsrAsrMetrics.setSpq_stage_avg(null);
                rsrAsrMetrics.setRSR_avg(null);
                rsrAsrMetrics.setRSR_stage_avg(null);
            }

            //Calculate average RSR_Bayesian for the mission
            if (calc_RSR_Bayesian) {
                rsrAsrMetrics.setRSR_Bayesian_avg(RSR_Bayesian_avg_count > 0 ? RSR_Bayesian_avg / RSR_Bayesian_avg_count : null);
                for (int i = 0; i < numStages; i++) {
                    int count = RSR_Bayesian_stage_avg_count.get(i);
                    RSR_Bayesian_stage_avg.set(i, count > 0 ? RSR_Bayesian_stage_avg.get(i) / count : null);
                }
            } else {
                rsrAsrMetrics.setRSR_Bayesian_avg(null);
                rsrAsrMetrics.setRSR_Bayesian_stage_avg(null);
            }

            //Calculate RSR_Spm_Spr_avg and RSR_Spm_Spq_avg for the mission
            if (calc_RSR_Spm_Spr_avg) {
                rsrAsrMetrics.setRSR_Spm_Spr_avg(cpaMetricsComputer.computeRSR(Spm_avg, Spr_avg));
                List<Double> RSR_Spm_Spr_stage_avg = MetricsUtils.initializeDoubleList(
                        rsrAsrMetrics.getRSR_Spm_Spr_stage_avg(), numStages);
                rsrAsrMetrics.setRSR_Spm_Spr_stage_avg(RSR_Spm_Spr_stage_avg);
                for (int i = 0; i < numStages; i++) {
                    RSR_Spm_Spr_stage_avg.set(i, cpaMetricsComputer.computeRSR(Spm_stage_avg.get(i), Spr_stage_avg.get(i)));
                }
            } else {
                rsrAsrMetrics.setRSR_Spm_Spr_avg(null);
                rsrAsrMetrics.setRSR_Spm_Spr_stage_avg(null);
            }
            if (calc_RSR_Spm_Spq_avg) {
                rsrAsrMetrics.setRSR_Spm_Spq_avg(cpaMetricsComputer.computeRSR(Spm_avg, Spq_avg));
                List<Double> RSR_Spm_Spq_stage_avg = MetricsUtils.initializeDoubleList(
                        rsrAsrMetrics.getRSR_Spm_Spq_stage_avg(), numStages);
                rsrAsrMetrics.setRSR_Spm_Spq_stage_avg(RSR_Spm_Spq_stage_avg);
                for (int i = 0; i < numStages; i++) {
                    RSR_Spm_Spq_stage_avg.set(i, cpaMetricsComputer.computeRSR(Spm_stage_avg.get(i), Spq_stage_avg.get(i)));
                }
            } else {
                rsrAsrMetrics.setRSR_Spm_Spq_avg(null);
                rsrAsrMetrics.setRSR_Spm_Spq_stage_avg(null);
            }

            //Calculate average RSR_alt_1 for the mission
            if (calc_RSR_alt_1) {
                rsrAsrMetrics.setRSR_alt_1_avg(RSR_alt_1_avg_count > 0 ? RSR_alt_1_avg / RSR_alt_1_avg_count : null);
                for (int i = 0; i < numStages; i++) {
                    int count = RSR_alt_1_stage_avg_count.get(i);
                    RSR_alt_1_stage_avg.set(i, count > 0 ? RSR_alt_1_stage_avg.get(i) / count : null);
                }
            } else {
                rsrAsrMetrics.setRSR_alt_1_avg(null);
                rsrAsrMetrics.setRSR_alt_1_stage_avg(null);
            }

            //Calculate average RSR_alt_2 for the mission
            if (calc_RSR_alt_2) {
                rsrAsrMetrics.setRSR_alt_2_avg(RSR_alt_2_avg_count > 0 ? RSR_alt_2_avg / RSR_alt_2_avg_count : null);
                for (int i = 0; i < numStages; i++) {
                    int count = RSR_alt_2_stage_avg_count.get(i);
                    RSR_alt_2_stage_avg.set(i, count > 0 ? RSR_alt_2_stage_avg.get(i) / count : null);
                }
            } else {
                rsrAsrMetrics.setRSR_alt_2_avg(null);
                rsrAsrMetrics.setRSR_alt_2_stage_avg(null);
            }            

            //Calculate average RMR for the mission
            if (calc_RMR) {
                missionMetrics.setRMR_sigint_avg(RMR_sigint_avg_count > 0 ? RMR_sigint_avg / RMR_sigint_avg_count : null);
                missionMetrics.setRMR_blueAction_avg(RMR_blueAction_avg_count > 0 ? RMR_blueAction_avg / RMR_blueAction_avg_count : null);                
                missionMetrics.setRMR_avg(RMR_avg_count > 0 ? RMR_avg / RMR_avg_count : null);                
            } else {
                missionMetrics.setRMR_sigint_avg(null);
                missionMetrics.setRMR_blueAction_avg(null);
                missionMetrics.setRMR_avg(null);
            }
        } //end if (trials != null && !trials.isEmpty()) 
        
        missionMetrics.setMetrics_stale(false);
        
        return missionMetrics;
    }
    
    /**
     * Compute the number of trials needed to detect each Red tactics change,
     * and whether each change was detected. Used for computing CB (Change Blindness). 
     * If checking for consistency in detections of the change, the "false alarm rate"
     * before the change must be less than 50%, and the "miss rate" after the 
     * change must also be less than 50%.
     *
     * @param trials
     * @param checkConsistency
     * @return
     */
    protected static RedTacticsChangeDetections computeRedTacticChangeDetections(
            List<TrialData> trials, boolean checkConsistency) {
        RedTacticsChangeDetections result = new RedTacticsChangeDetections();
        if (trials != null && !trials.isEmpty()) {
            //First, find the trials on which the Red tactics changed (typically, there 
            //will be just one tactics change per mission)            
            RedTacticType lastTactic = null;
            List<Integer> changeTrials = new LinkedList<Integer>();
            List<RedTacticType> changeToTactics = new LinkedList<RedTacticType>();
            int i = 0;
            for (TrialData trial : trials) {
                if (trial.getRedTacticsReport() != null
                        && trial.getRedTacticsReport().getActualRedTactic() != null) {
                    RedTacticType currentTactic = trial.getRedTacticsReport().getActualRedTactic();
                    if (lastTactic == null) {
                        lastTactic = currentTactic;
                    } else if (lastTactic != currentTactic && i < (trials.size() - 1)) {
                        changeTrials.add(i);
                        changeToTactics.add(currentTactic);
                        lastTactic = currentTactic;
                    }
                }
                i++;
            }

            //Now, measure the number of trials it took to identify each change
            if (!changeTrials.isEmpty()) {                
                List<Double> trialsToDetectChanges = new ArrayList<Double>(changeTrials.size());
                List<Boolean> changesDetected = new ArrayList<Boolean>(changeTrials.size());
                Iterator<RedTacticType> changeToTacticsIter = changeToTactics.iterator();
                for (int changeTrial : changeTrials) {
                    double trialsToDetectChange = 1.d;
                    boolean changeFound = false;
                    boolean consistencyCheckFailed = false;
                    RedTacticType changeToTactic = changeToTacticsIter.next();

                    //First, verify that P <= 0.5 Red playing with changed-to 
                    //tactic on at least 50% of trials before the change occurred
                    if (checkConsistency && changeTrial > 3) {
                        int numFalseAlarms = 0;
                        for (int currCheckTrial = 1; currCheckTrial < changeTrial; currCheckTrial++) {
                            TrialData checkTrial = trials.get(currCheckTrial);
                            List<Double> redTacticProbs = checkTrial.getRedTacticsReport() != null
                                    ? checkTrial.getRedTacticsReport().getRedTacticProbabilities() : null;
                            List<RedTacticType> possibleTactics = checkTrial.getRedTacticsReport() != null
                                    ? checkTrial.getRedTacticsReport().getPossibleRedTactics() : null;
                            Boolean changeToTacticDetected = TrialMetricsComputer.isRedTacticCorrectlyDetected(
                                    redTacticProbs, possibleTactics, changeToTactic);
                            if (changeToTacticDetected != null && changeToTacticDetected) {
                                numFalseAlarms++;
                            }
                        }
                        if ((double) numFalseAlarms / changeTrial > 0.5d) {
                            //Consistency check failed                            
                            trialsToDetectChange = trials.size() - changeTrial - 1;
                            consistencyCheckFailed = true;
                            //System.out.println("Consistency check failed: " + changeTrial + ", " + trialsToDetectChange);
                        }
                    }

                    if (!consistencyCheckFailed) {                                                  
                        //System.out.println("Change trial: " + changeTrial);
                        int currTrial = changeTrial + 1;
                        while (!changeFound && currTrial < trials.size()) {
                            TrialData trial = trials.get(currTrial);
                            List<Double> redTacticProbs = trial.getRedTacticsReport() != null
                                    ? trial.getRedTacticsReport().getRedTacticProbabilities() : null;
                            List<RedTacticType> possibleTactics = trial.getRedTacticsReport() != null
                                    ? trial.getRedTacticsReport().getPossibleRedTactics() : null;
                            RedTacticType actualTactic = trial.getRedTacticsReport() != null
                                    ? trial.getRedTacticsReport().getActualRedTactic() : null;
                            Boolean tacticDetected = TrialMetricsComputer.isRedTacticCorrectlyDetected(
                                    redTacticProbs, possibleTactics, actualTactic);
                            if (tacticDetected != null && tacticDetected) {
                                changeFound = true;
                                if (checkConsistency && currTrial < (trials.size() - 3)) {
                                    //Verify that P > 0.5 for changed to tactic
                                    //for at least 50% of the remaining trials
                                    int numMisses = 0;
                                    for (int currCheckTrial = currTrial + 1; currCheckTrial < trials.size(); currCheckTrial++) {
                                        TrialData checkTrial = trials.get(currTrial);
                                        List<Double> redTacticProbsTemp = checkTrial.getRedTacticsReport() != null
                                                ? checkTrial.getRedTacticsReport().getRedTacticProbabilities() : null;
                                        List<RedTacticType> possibleTacticsTemp = checkTrial.getRedTacticsReport() != null
                                                ? checkTrial.getRedTacticsReport().getPossibleRedTactics() : null;
                                        Boolean changeToTacticDetected = TrialMetricsComputer.isRedTacticCorrectlyDetected(
                                                redTacticProbsTemp, possibleTacticsTemp, actualTactic);
                                        if (changeToTacticDetected != null && !changeToTacticDetected) {
                                            numMisses++;
                                        }
                                    }
                                    if ((double) numMisses / (trials.size() - currTrial - 1) > 0.5d) {
                                        //Consistency check failed, advance to the next trial                                       
                                        changeFound = false;
                                        trialsToDetectChange++;
                                        currTrial++;
                                    }
                                }
                            } else {
                                trialsToDetectChange++;
                                currTrial++;
                            }
                        }
                    }
                    trialsToDetectChanges.add(trialsToDetectChange);
                    changesDetected.add(changeFound);
                }
                result.trialsToDetectChanges = trialsToDetectChanges;
                result.changesDetected = changesDetected;
            }
        }
        return result;
    }
    
    private static class RedTacticsChangeDetections {
        /** The number of trials needed to detect each Red tactics change */
        List<Double> trialsToDetectChanges;
        
        /** Whether each Red tactics change was detected */
        List<Boolean> changesDetected;
    }
    
    /**
     *
     * @param redActivityDetected
     * @param blueLocationIds
     * @param sigintSelection
     * @param sigintProbs
     * @return
     */
    protected static SigintProbability getSigintProbability(List<Boolean> redActivityDetected, 
            List<String> blueLocationIds, SigintSelectionData sigintSelection, 
            AttackProbabilityReportData sigintProbs) {
        //Need SIGINT location, SIGINT activity at location, return P(Attack|SIGINT) [SIGINT type, prob]        
        if(redActivityDetected != null && !redActivityDetected.isEmpty() &&
                sigintProbs != null && sigintProbs.getProbabilities() != null) {
            int sigintLocationIndex = 0;
            if(redActivityDetected.size() > 1 && sigintSelection != null &&
                    sigintSelection.getSigintLocations() != null && 
                    sigintSelection.getSigintLocations().size() == 1 &&
                    blueLocationIds != null) {
                int i = 0;
                String sigintLocationId = sigintSelection.getSigintLocations().get(0);
                for (String locationId : blueLocationIds) {
                    if (sigintLocationId.equalsIgnoreCase(locationId)) {
                        sigintLocationIndex = i;
                        break;
                    } else {
                        i++;
                    }
                }                
            }
            Double sigintProb = sigintLocationIndex < sigintProbs.getProbabilities().size() ?
                    sigintProbs.getProbabilities().get(sigintLocationIndex) : null;
            Boolean activityDetected = sigintLocationIndex < redActivityDetected.size() ?
                    redActivityDetected.get(sigintLocationIndex) : false;
            return new SigintProbability(sigintProb, activityDetected);
        }
        return null;
    }
    
    /**
     *     
     * @param missions
     * @param averageMission
     * @param metricsInfo
     * @param comparisonMetrics
     * @param taskNum
     * @return
     */
    public MissionMetrics updateAverageTaskMetrics(List<MissionMetrics> missions, 
            MissionMetrics averageMission, MetricsInfo metricsInfo, 
            MissionMetrics comparisonMetrics, int taskNum) {
        return this.updateAverageTaskMetrics(missions, averageMission, 
                averageMission != null ? averageMission.getTrials() : null, 
                metricsInfo, comparisonMetrics, taskNum);        
    }
    
    /**
     *     
     * @param missions
     * @param averageMission
     * @param averageTrials
     * @param metricsInfo     
     * @param comparisonMetrics     
     * @param taskNum     
     * @return
     */
    public MissionMetrics updateAverageTaskMetrics(List<MissionMetrics> missions, 
            MissionMetrics averageMission, List<TrialData> averageTrials, 
            MetricsInfo metricsInfo, MissionMetrics comparisonMetrics, int taskNum) {
        if (missions != null && !missions.isEmpty()
                && averageTrials != null && !averageTrials.isEmpty()) {
            MissionMetrics mission0 = missions.get(0);
            averageMission = intializeAverageMissionMetrics(averageMission,
                    averageTrials, mission0);
            averageMission.setMetrics_stale(true);
            
            //***************** Compute means and standard deviations *****************
            DistributionStats normativeBlueOptionSelectionFrequency_stats = null;
            if(mission0.getPM_normativeBlueOptionSelectionFrequency() != null) {
                normativeBlueOptionSelectionFrequency_stats = new DistributionStats();
            }
            
            DistributionStats sigintHighestPaSelectionFrequency_stats = null;
            if(mission0.getCS_sigintHighestPaSelectionFrequency() != null) {
                sigintHighestPaSelectionFrequency_stats = new DistributionStats();
            }            
            
            DistributionStats percentTrialsReviewedInBatchPlot_stats = null;
            if(mission0.getSS_percentTrialsReviewedInBatchPlot_avg() != null) {
                percentTrialsReviewedInBatchPlot_stats = new DistributionStats();
            }
            
            DistributionStats missionTime_stats = new DistributionStats(); 
            
            for(MissionMetrics mission : missions) {
                //Update average normative blue option selection frequency
                if (normativeBlueOptionSelectionFrequency_stats != null
                        && mission.getPM_normativeBlueOptionSelectionFrequency() != null) {
                    normativeBlueOptionSelectionFrequency_stats = MetricUtils.updateRunningDistStats(
                            normativeBlueOptionSelectionFrequency_stats, 
                            mission.getPM_normativeBlueOptionSelectionFrequency(), true);
                }
                
                //Update average normative SIGINT location selection frequency
                if (sigintHighestPaSelectionFrequency_stats != null
                        && mission.getCS_sigintHighestPaSelectionFrequency() != null) {
                    sigintHighestPaSelectionFrequency_stats = MetricUtils.updateRunningDistStats(
                            sigintHighestPaSelectionFrequency_stats, 
                            mission.getCS_sigintHighestPaSelectionFrequency(), true);
                }
                
                //Update average percent trials reviewed in batch plots
                if (percentTrialsReviewedInBatchPlot_stats != null
                        && mission.getSS_percentTrialsReviewedInBatchPlot_avg() != null) {
                    percentTrialsReviewedInBatchPlot_stats = MetricUtils.updateRunningDistStats(
                            percentTrialsReviewedInBatchPlot_stats, 
                            mission.getSS_percentTrialsReviewedInBatchPlot_avg(), true);
                }            
                
                //Update average time on mission
                if (mission.getTask_time() != null) {
                    missionTime_stats = MetricUtils.updateRunningDistStats(
                            missionTime_stats, mission.getTask_time(), false);
                }
            }
            
            //Compute average, standard deviation blue option selection frequency
            if (normativeBlueOptionSelectionFrequency_stats != null) {
                normativeBlueOptionSelectionFrequency_stats = MetricUtils.distStats(
                        normativeBlueOptionSelectionFrequency_stats, true);
                averageMission.setPM_normativeBlueOptionSelectionFrequency(
                        normativeBlueOptionSelectionFrequency_stats.mean);
                averageMission.setPM_normativeBlueOptionSelectionFrequency_std(
                        normativeBlueOptionSelectionFrequency_stats.std);
            }
            
            //Compute average, standard deviation SIGINT location selection frequency
            if (sigintHighestPaSelectionFrequency_stats != null) {
                sigintHighestPaSelectionFrequency_stats = MetricUtils.distStats(
                        sigintHighestPaSelectionFrequency_stats, true);
                averageMission.setCS_sigintHighestPaSelectionFrequency(
                        sigintHighestPaSelectionFrequency_stats.mean);
                averageMission.setCS_sigintHighestPaSelectionFrequency_std(
                        sigintHighestPaSelectionFrequency_stats.std);
            }
            
            //Compute average, standard deviation percent trials reviewed in batch plots
            if (percentTrialsReviewedInBatchPlot_stats != null) {
                percentTrialsReviewedInBatchPlot_stats = MetricUtils.distStats(
                        percentTrialsReviewedInBatchPlot_stats, true);
                averageMission.setSS_percentTrialsReviewedInBatchPlot_avg(
                    percentTrialsReviewedInBatchPlot_stats.mean);
                averageMission.setSS_percentTrialsReviewedInBatchPlot_avg_std(
                    percentTrialsReviewedInBatchPlot_stats.std);
            }
            
            //Compute average time on mission
            missionTime_stats = MetricUtils.distStats(missionTime_stats, false);
            
            //***************** Compute average mission metrics data using average mission *****************
            updateTaskMetrics(averageMission, missionTime_stats.mean, averageTrials, metricsInfo, 
                    comparisonMetrics, taskNum);
        }        
        averageMission.setMetrics_stale(false);
        return averageMission;
    }
    
    protected static MissionMetrics intializeAverageMissionMetrics(MissionMetrics averageMission,
            List<TrialData> averageTrials, MissionMetrics mission) {
        if(averageMission == null) {
            averageMission = new MissionMetrics();
            averageMission.setTask_id(mission.getTask_id());
            averageMission.setTask_number(mission.getTask_number());
            averageMission.setExam_id(mission.getExam_id());
        }
        if(averageMission.getTrials() != averageTrials) {
            averageMission.setTrials(averageTrials);
        }
        averageMission.setData_type(DataType.Human_Avg);        
        return averageMission;
    }
    
    /** Contains P(Attack|SIGINT) and the SIGINT type (chatter/silent) */
    protected static class SigintProbability {        
        public Double sigintProb;
        
        public Boolean redActivityDetected;
        
        public SigintProbability(Double sigintProb, Boolean redActivityDetected) {
            this.sigintProb = sigintProb;
            this.redActivityDetected = redActivityDetected;
        }
    }
}