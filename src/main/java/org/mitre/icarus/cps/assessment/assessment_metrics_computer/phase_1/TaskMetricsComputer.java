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

import java.util.List;

import org.mitre.icarus.cps.assessment.assessment_metrics_computer.ITaskMetricsComputer;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.CFAMetric;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.CPAMetric;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.MetricsInfo;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.TaskMetrics;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.TrialData;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.TrialMetrics;

/**
 * Computes Phase 1 task metrics.
 *
 * @author cbonaceto
 *
 */
public class TaskMetricsComputer extends MetricsComputer implements
        ITaskMetricsComputer<TrialData, TaskMetrics, MetricsInfo> {

    /**
     * @param taskMetrics
     * @param trials
     * @return
     */
    @Override
    public TaskMetrics updateCompletionStatus(TaskMetrics taskMetrics, List<TrialData> trials) {
        return updateCompletionStatus(taskMetrics, trials, null);
    }

    /**
     * @param taskMetrics
     * @param trials
     * @param modifiedTrial
     * @return
     */
    @Override
    public TaskMetrics updateCompletionStatus(TaskMetrics taskMetrics, List<TrialData> trials, TrialData modifiedTrial) {
        if (taskMetrics == null) {
            taskMetrics = new TaskMetrics();
            taskMetrics.setTrials(trials);
        }

        //Update all_trials_valid flag
        Boolean all_trials_valid = taskMetrics.isAll_trials_valid();
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
        taskMetrics.setAll_trials_valid(all_trials_valid);

        //Update task_complete flag
        Boolean task_complete = taskMetrics.isTask_complete();
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
        taskMetrics.setTask_complete(task_complete);

        return taskMetrics;
    }

    /**
     * @param taskMetrics
     * @param trials
     * @param metricsInfo
     * @param comparisonMetrics
     * @param taskNum
     * @return
     */
    @Override
    //TODO: Need to compute standard deviations of metrics
    public TaskMetrics updateTaskMetrics(TaskMetrics taskMetrics, Double task_time, List<TrialData> trials, MetricsInfo metricsInfo,
            TaskMetrics comparisonMetrics, int taskNum) {
	//Information needed: Which metrics to compute, performance metrics for each trial for
        //which metrics are computed for
        if (taskMetrics == null) {
            taskMetrics = new TaskMetrics();
            taskMetrics.setTask_number(taskNum);
            taskMetrics.setTrials(trials);
        }
        if (metricsInfo == null) {
            metricsInfo = MetricsInfo.createDefaultMetricsInfo();
        }

        taskMetrics.setTask_time(task_time);
        taskMetrics.setMetrics_stale(true);

        if (trials != null && !trials.isEmpty()) {
            int numStages = trials.get(0) != null && trials.get(0).getMetrics() != null && trials.get(0).getMetrics().getProbs() != null
                    ? trials.get(0).getMetrics().getProbs().size() : 0;
            Boolean calc_RR = metricsInfo.getRR_info() != null && metricsInfo.getRR_info().isAssessedForTask(taskNum)
                    && metricsInfo.getRR_info().isCalculated();
            Boolean calc_AI = metricsInfo.getAI_info() != null && metricsInfo.getAI_info().isAssessedForTask(taskNum)
                    && metricsInfo.getAI_info().isCalculated();
            Boolean calc_PM_F = metricsInfo.getPM_F_info() != null && metricsInfo.getPM_F_info().isAssessedForTask(taskNum)
                    && metricsInfo.getPM_F_info().isCalculated();
            Boolean calc_PM_RMS = metricsInfo.getPM_RMS_info() != null && metricsInfo.getPM_RMS_info().isAssessedForTask(taskNum)
                    && metricsInfo.getPM_RMS_info().isCalculated();
            Boolean calc_CS = metricsInfo.getCS_info() != null && metricsInfo.getCS_info().isAssessedForTask(taskNum)
                    && metricsInfo.getCS_info().isCalculated();
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
            Boolean calc_ASR = metricsInfo.getASR_info() != null && metricsInfo.getASR_info().isAssessedForTask(taskNum)
                    && metricsInfo.getASR_info().isCalculated();
            Boolean calc_RMR = metricsInfo.getRMR_info() != null && metricsInfo.getRMR_info().isAssessedForTask(taskNum)
                    && metricsInfo.getRMR_info().isCalculated();

            Double RR_avg_score = null; //Average RR score over all trials
            Integer RR_avg_score_count = null;
            if (calc_RR) {
                RR_avg_score = 0d;
                RR_avg_score_count = 0;
            }
            Double AI_avg_score = null; //Average AI score over all trials/stages
            Integer AI_avg_score_count = null;
            if (calc_AI) {
                AI_avg_score = 0d;
                AI_avg_score_count = 0;
            }

            Double Fh = null; //Number of times troops were allocated against the group with the current highest probability
            Integer Fh_count = null;
            Double Ph = null; //Average probability of the group with the highest probability	
            Integer Ph_count = null;
            if (calc_PM_F) {
                Fh = 0d;
                Fh_count = 0;
                Ph = 0d;
                Ph_count = 0;
            }

            Double RMS_F_P_avg = null;
            Integer RMS_F_P_count = null;
            Double RMS_F_I_avg = null;
            Integer RMS_F_I_count = null;
            Double PM_RMS_avg_score = null; //Average PM_RMS score over all trials
            Integer PM_RMS_avg_score_count = null;
            if (calc_PM_RMS) {
                RMS_F_P_avg = 0d;
                RMS_F_P_count = 0;
                RMS_F_I_avg = 0d;
                RMS_F_I_count = 0;
                PM_RMS_avg_score = 0d;
                PM_RMS_avg_score_count = 0;
            }

            Integer sigint_selections_all_trials = null;
            Integer sigint_highest_group_selections_all_trials = null;
			//Integer sigint_highest_best_selections_all_trials = null;	
            //Integer C_num_subjects = null;			
            Double C_trials_avg = null;
            Integer C_trials_avg_count = null;
            if (calc_CS) {
                sigint_selections_all_trials = 0;
                sigint_highest_group_selections_all_trials = 0;
				//Integer sigint_highest_best_selections_all_trials = 0;	
                //C_num_subjects = 0;	
                C_trials_avg = 0d;
                C_trials_avg_count = 0;
            }

            Double RSR_avg = null;
            Integer RSR_avg_count = null;
            List<Double> RSR_stage_avg = null;
            List<Integer> RSR_stage_avg_count = null;
            Double Spm_avg = null;
            Integer Spm_avg_count = null;
            List<Double> Spm_stage_avg = null;
            List<Integer> Spm_stage_avg_count = null;
            Double Spr_avg = null;
            Integer Spr_avg_count = null;
            List<Double> Spr_stage_avg = null;
            List<Integer> Spr_stage_avg_count = null;
            Double Spq_avg = null;
            Integer Spq_avg_count = null;
            List<Double> Spq_stage_avg = null;
            List<Integer> Spq_stage_avg_count = null;
            if (calc_RSR) {
                RSR_avg = 0d;
                RSR_avg_count = 0;
                RSR_stage_avg = MetricsUtils.initializeDoubleList(taskMetrics.getRSR_stage_avg(), numStages);
                taskMetrics.setRSR_stage_avg(RSR_stage_avg);
                RSR_stage_avg_count = MetricsUtils.initializeIntegerList(RSR_stage_avg_count, numStages);
                Spm_avg = 0d;
                Spm_avg_count = 0;
                Spm_stage_avg = MetricsUtils.initializeDoubleList(taskMetrics.getSpm_stage_avg(), numStages);
                taskMetrics.setSpm_stage_avg(Spm_stage_avg);
                Spm_stage_avg_count = MetricsUtils.initializeIntegerList(Spm_stage_avg_count, numStages);
                Spr_avg = 0d;
                Spr_avg_count = 0;
                Spr_stage_avg = MetricsUtils.initializeDoubleList(taskMetrics.getSpr_stage_avg(), numStages);
                taskMetrics.setSpr_stage_avg(Spr_stage_avg);
                Spr_stage_avg_count = MetricsUtils.initializeIntegerList(Spr_stage_avg_count, numStages);
                Spq_avg = 0d;
                Spq_avg_count = 0;
                Spq_stage_avg = MetricsUtils.initializeDoubleList(taskMetrics.getSpq_stage_avg(), numStages);
                taskMetrics.setSpq_stage_avg(Spq_stage_avg);
                Spq_stage_avg_count = MetricsUtils.initializeIntegerList(Spq_stage_avg_count, numStages);
            }

            Double RSR_Bayesian_avg = null;
            Integer RSR_Bayesian_avg_count = null;
            List<Double> RSR_Bayesian_stage_avg = null;
            List<Integer> RSR_Bayesian_stage_avg_count = null;
            if (calc_RSR_Bayesian) {
                RSR_Bayesian_avg = 0d;
                RSR_Bayesian_avg_count = 0;
                RSR_Bayesian_stage_avg = MetricsUtils.initializeDoubleList(taskMetrics.getRSR_Bayesian_stage_avg(), numStages);
                taskMetrics.setRSR_Bayesian_stage_avg(RSR_Bayesian_stage_avg);
                RSR_Bayesian_stage_avg_count = MetricsUtils.initializeIntegerList(RSR_Bayesian_stage_avg_count, numStages);
            }

            Double RSR_alt_1_avg = null;
            Integer RSR_alt_1_avg_count = null;
            List<Double> RSR_alt_1_stage_avg = null;
            List<Integer> RSR_alt_1_stage_avg_count = null;
            if (calc_RSR_alt_1) {
                RSR_alt_1_avg = 0d;
                RSR_alt_1_avg_count = 0;
                RSR_alt_1_stage_avg = MetricsUtils.initializeDoubleList(taskMetrics.getRSR_alt_1_stage_avg(), numStages);
                taskMetrics.setRSR_alt_1_stage_avg(RSR_alt_1_stage_avg);
                RSR_alt_1_stage_avg_count = MetricsUtils.initializeIntegerList(RSR_alt_1_stage_avg_count, numStages);
            }

            Double RSR_alt_2_avg = null;
            Integer RSR_alt_2_avg_count = null;
            List<Double> RSR_alt_2_stage_avg = null;
            List<Integer> RSR_alt_2_stage_avg_count = null;
            if (calc_RSR_alt_2) {
                RSR_alt_2_avg = 0d;
                RSR_alt_2_avg_count = 0;
                RSR_alt_2_stage_avg = MetricsUtils.initializeDoubleList(taskMetrics.getRSR_alt_2_stage_avg(), numStages);
                taskMetrics.setRSR_alt_2_stage_avg(RSR_alt_2_stage_avg);
                RSR_alt_2_stage_avg_count = MetricsUtils.initializeIntegerList(RSR_alt_2_stage_avg_count, numStages);
            }

            Double ASR_avg = null;
            Integer ASR_avg_count = null;
            List<Double> ASR_stage_avg = null;
            List<Integer> ASR_stage_avg_count = null;
            if (calc_ASR) {
                ASR_avg = 0d;
                ASR_avg_count = 0;
                ASR_stage_avg = MetricsUtils.initializeDoubleList(taskMetrics.getASR_stage_avg(), numStages);
                taskMetrics.setASR_stage_avg(ASR_stage_avg);
                ASR_stage_avg_count = MetricsUtils.initializeIntegerList(ASR_stage_avg_count, numStages);
            }

            Double RMR_avg = null;
            Integer RMR_avg_count = null;
            List<Integer> F_LS_all_trials_count = null;//taskMetrics.getF_LS_all_trials_count();
            if (calc_RMR) {
                RMR_avg = 0d;
                RMR_avg_count = 0;
                F_LS_all_trials_count = MetricsUtils.initializeIntegerList(
                        taskMetrics.getF_LS_all_trials_count(), MetricsUtils_Phase1.LAYER_SEQUENCES.size());
                taskMetrics.setF_LS_all_trials_count(F_LS_all_trials_count);
            }

            //TODO: Consider adding trials_stages_present info to TaskMetrics
            for (TrialData trial : trials) {
                TrialMetrics trialMetrics = (trial != null) ? trial.getMetrics() : null;
                if (trialMetrics != null) {
                    int trialNum = trial.getTrial_number();

                    if (calc_RR && trialMetrics.getRR() != null && trialMetrics.getRR().score != null
                            && metricsInfo.getRR_info().isAssessedForStage(taskNum, trialNum, 0)) {
                        RR_avg_score += trialMetrics.getRR().score;
                        RR_avg_score_count++;
                    }

                    if (calc_AI && trialMetrics.getAI() != null && !trialMetrics.getAI().isEmpty()) {
                        int i = 0;
                        for (CFAMetric AI : trialMetrics.getAI()) {
                            if (AI != null && AI.score != null
                                    && metricsInfo.getAI_info().isAssessedForStage(taskNum, trialNum, i)) {
                                AI_avg_score += AI.score;
                                AI_avg_score_count++;
                            }
                            i++;
                        }
                    }

                    if (calc_PM_F && metricsInfo.getPM_F_info().isAssessedForStage(taskNum, trialNum, 0)) {
						//Update Fh and Ph (probability matching for Tasks 1-3)
                        //Fh += trialMetrics.getFh() != null ? trialMetrics.getFh() : 0;
                        if (trialMetrics.getFh() != null) {
                            Fh += trialMetrics.getFh();
                            Fh_count++;
                        }
                        if (trialMetrics.getPh() != null) {
                            Ph += trialMetrics.getPh();
                            Ph_count++;
                        }
                    }

                    if (calc_PM_RMS && metricsInfo.getPM_RMS_info().isAssessedForStage(taskNum, trialNum, 0)) {
                        //Update avg RMS_F_P and RMS_F_I (probability matching for Tasks 4-6)
                        if (trialMetrics.getRMS_F_P() != null) {
                            RMS_F_P_avg += trialMetrics.getRMS_F_P();
                            RMS_F_P_count++;
                        }
                        if (trialMetrics.getRMS_F_I() != null) {
                            RMS_F_I_avg += trialMetrics.getRMS_F_I();
                            RMS_F_I_count++;
                        }
                        if (trialMetrics.getPM() != null && trialMetrics.getPM().score != null) {
                            PM_RMS_avg_score += trialMetrics.getPM().score;
                            PM_RMS_avg_score_count++;
                        }
                    }

                    if (calc_CS && metricsInfo.getCS_info().isAssessedForStage(taskNum, trialNum, 0)) {
                        //Update sigint selection metrics (confirmation bias for Task 6)
                        sigint_selections_all_trials
                                += trialMetrics.getSigint_selections() != null ? trialMetrics.getSigint_selections() : 0;
                        sigint_highest_group_selections_all_trials
                                += trialMetrics.getSigint_highest_group_selections() != null ? trialMetrics.getSigint_highest_group_selections() : 0;
                        if (trialMetrics.getC() != null) {
                            C_trials_avg += trialMetrics.getC();
                            C_trials_avg_count++;
                        }
                    }

                    if (calc_RSR) {
                        //Update avg RSR (standard), Spm, Spr, and Spq
                        if (trialMetrics.getRSR() != null && !trialMetrics.getRSR().isEmpty()) {
                            for (int i = 0; i < trialMetrics.getRSR().size(); i++) {
                                CPAMetric rsr = trialMetrics.getRSR().get(i);
                                if (rsr != null && rsr.assessed) {
                                    //Update avg RSR
                                    if (rsr.score != null) {
                                        RSR_avg += rsr.score;
                                        RSR_avg_count++;
                                        RSR_stage_avg.set(i, RSR_stage_avg.get(i) + rsr.score);
                                        RSR_stage_avg_count.set(i, RSR_stage_avg_count.get(i) + 1);
                                    }
                                    //Update avg Spm, Spr, and Spq
                                    if (i < trialMetrics.getSpm().size()) {
                                        Double Spm = trialMetrics.getSpm().get(i);
                                        if (Spm != null) {
                                            Spm_avg += Spm;
                                            Spm_avg_count++;
                                            Spm_stage_avg.set(i, Spm_stage_avg.get(i) + Spm);
                                            Spm_stage_avg_count.set(i, Spm_stage_avg_count.get(i) + 1);
                                        }
                                    }
                                    if (i < trialMetrics.getSpr().size()) {
                                        Double Spr = trialMetrics.getSpr().get(i);
                                        if (Spr != null) {
                                            Spr_avg += Spr;
                                            Spr_avg_count++;
                                            Spr_stage_avg.set(i, Spr_stage_avg.get(i) + Spr);
                                            Spr_stage_avg_count.set(i, Spr_stage_avg_count.get(i) + 1);
                                        }
                                    }
                                    if (i < trialMetrics.getSpq().size()) {
                                        Double Spq = trialMetrics.getSpq().get(i);
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
                        //Update avg RSR_Bayesian
                        if (trialMetrics.getRSR_Bayesian() != null && !trialMetrics.getRSR_Bayesian().isEmpty()) {
                            int i = 0;
                            for (CPAMetric rsr_bayesian : trialMetrics.getRSR_Bayesian()) {
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
                        if (trialMetrics.getRSR_alt_1() != null && !trialMetrics.getRSR_alt_1().isEmpty()) {
                            int i = 0;
                            for (CPAMetric rsr_alt_1 : trialMetrics.getRSR_alt_1()) {
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
                        if (trialMetrics.getRSR_alt_2() != null && !trialMetrics.getRSR_alt_2().isEmpty()) {
                            int i = 0;
                            for (CPAMetric rsr_alt_2 : trialMetrics.getRSR_alt_2()) {
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

                    if (calc_ASR) {
                        //Update ASR						
                        if (trialMetrics.getASR() != null && !trialMetrics.getASR().isEmpty()) {
                            int i = 0;
                            for (CPAMetric asr : trialMetrics.getASR()) {
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

                    if (calc_RMR) {
                        //Update avg RMR
                        CPAMetric RMR = trialMetrics.getRMR();
                        if (RMR != null && RMR.assessed && RMR.score != null) {
                            RMR_avg += RMR.score;
                            RMR_avg_count++;
                        }

                        //Update aggregate layer selection frequencies over all trials in the task
                        List<Integer> F_LS_trial = trialMetrics.getF_LS_count();
                        if (F_LS_trial != null && F_LS_trial.size() == F_LS_all_trials_count.size()) {
                            for (int i = 0; i < trialMetrics.getF_LS_count().size(); i++) {
                                F_LS_all_trials_count.set(i, F_LS_all_trials_count.get(i) + F_LS_trial.get(i));
                            }
                        } else if (trial.getLs_index() != null && trial.getLs_index() < F_LS_all_trials_count.size()) {
                            F_LS_all_trials_count.set(trial.getLs_index(), F_LS_all_trials_count.get(trial.getLs_index()) + 1);
                        }
                    }
                }
            }

            //Update RR_avg_score
            if (calc_RR) {
                taskMetrics.setRR_avg_score(RR_avg_score_count > 0 ? RR_avg_score / RR_avg_score_count : null);
            } else {
                taskMetrics.setRR_avg_score(null);
            }

            //Update AI_avg_score
            if (calc_AI) {
                taskMetrics.setAI_avg_score(AI_avg_score_count > 0 ? AI_avg_score / AI_avg_score_count : null);
            } else {
                taskMetrics.setAI_avg_score(null);
            }

            //Update Fh_Ph, Fh_1, and PM (probability matching for Tasks 1-3)
            if (calc_PM_F) {
                Fh = Fh_count > 0 ? Fh / Fh_count : 0;
                Ph = Ph_count > 0 ? Ph / Ph_count : 0;
                taskMetrics.setFh_Ph_avg(Fh - Ph);
                taskMetrics.setFh_1_avg(Fh - 1);
                taskMetrics.setPM_F(cfaMetricsComputer.computePM_F(taskMetrics.getFh_Ph_avg(),
                        taskMetrics.getFh_1_avg(), taskMetrics.getPM_F(),
                        comparisonMetrics != null ? comparisonMetrics.getPM_F() : null,
                        metricsInfo.getPM_F_info()));
            } else {
                taskMetrics.setFh_Ph_avg(null);
                taskMetrics.setFh_1_avg(null);
                taskMetrics.setPM_F(null);
            }

            //Update RMS_F_P_avg and RMS_F_I_avg, and PM_RMS_avg_score (probability matching for Tasks 1-3)
            if (calc_PM_RMS) {
                taskMetrics.setRMS_F_P_avg(RMS_F_P_count > 0 ? RMS_F_P_avg / RMS_F_P_count : null);
                taskMetrics.setRMS_F_I_avg(RMS_F_I_count > 0 ? RMS_F_I_avg / RMS_F_I_count : null);
                taskMetrics.setPM_RMS_avg_score(PM_RMS_avg_score_count > 0 ? PM_RMS_avg_score / PM_RMS_avg_score_count : null);
            } else {
                taskMetrics.setRMS_F_P_avg(null);
                taskMetrics.setRMS_F_I_avg(null);
                taskMetrics.setPM_RMS_avg_score(null);
            }

            //Update CS (confirmation bias for Task 6)
            if (calc_CS) {
                taskMetrics.setSigint_selections_all_trials(sigint_selections_all_trials);
                taskMetrics.setSigint_highest_group_selections_all_trials(sigint_highest_group_selections_all_trials);
                //taskMetrics.setC_num_subjects(C_num_subjects);
                taskMetrics.setC_all_trials(sigint_selections_all_trials > 0
                        ? ((double) sigint_highest_group_selections_all_trials / sigint_selections_all_trials) * 100d : 0d);
                taskMetrics.setC_trials_avg(C_trials_avg_count > 0 ? C_trials_avg / C_trials_avg_count : null);
                taskMetrics.setCS(cfaMetricsComputer.computeCS(taskMetrics.getC_all_trials(),
                        metricsInfo.getC_threshold(), taskMetrics.getCS(),
                        comparisonMetrics != null ? comparisonMetrics.getCS() : null,
                        metricsInfo.getCS_info()));
            } else {
                taskMetrics.setSigint_selections_all_trials(null);
                taskMetrics.setSigint_highest_group_selections_all_trials(null);
                //taskMetrics.setC_num_subjects(null);
                taskMetrics.setC_all_trials(null);
                taskMetrics.setC_trials_avg(null);
                taskMetrics.setCS(null);
            }

            //Update RSR_avg (standard), Spm_avg, Spr_avg, and Spq_avg
            if (calc_RSR) {
                Spm_avg = Spm_avg_count > 0 ? Spm_avg / Spm_avg_count : 0d;
                Spr_avg = Spr_avg_count > 0 ? Spr_avg / Spr_avg_count : 0d;
                Spq_avg = Spq_avg_count > 0 ? Spq_avg / Spq_avg_count : 0d;
                taskMetrics.setSpm_avg(Spm_avg);
                taskMetrics.setSpr_avg(Spr_avg);
                taskMetrics.setSpq_avg(Spq_avg);
                taskMetrics.setRSR_avg(RSR_avg_count > 0 ? RSR_avg / RSR_avg_count : null);
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
                taskMetrics.setSpm_avg(null);
                taskMetrics.setSpm_stage_avg(null);
                taskMetrics.setSpr_avg(null);
                taskMetrics.setSpr_stage_avg(null);
                taskMetrics.setSpq_avg(null);
                taskMetrics.setSpq_stage_avg(null);
                taskMetrics.setRSR_avg(null);
                taskMetrics.setRSR_stage_avg(null);
            }

            //Update RSR_Bayesian_avg
            if (calc_RSR_Bayesian) {
                taskMetrics.setRSR_Bayesian_avg(RSR_Bayesian_avg_count > 0 ? RSR_Bayesian_avg / RSR_Bayesian_avg_count : null);
                for (int i = 0; i < numStages; i++) {
                    int count = RSR_Bayesian_stage_avg_count.get(i);
                    RSR_Bayesian_stage_avg.set(i, count > 0 ? RSR_Bayesian_stage_avg.get(i) / count : null);
                }
            } else {
                taskMetrics.setRSR_Bayesian_avg(null);
                taskMetrics.setRSR_Bayesian_stage_avg(null);
            }

            //Update RSR_Spm_Spr_avg and RSR_Spm_Spq_avg
            if (calc_RSR_Spm_Spr_avg) {
                taskMetrics.setRSR_Spm_Spr_avg(cpaMetricsComputer.computeRSR(Spm_avg, Spr_avg));
                List<Double> RSR_Spm_Spr_stage_avg = MetricsUtils.initializeDoubleList(taskMetrics.getRSR_Spm_Spr_stage_avg(), numStages);
                taskMetrics.setRSR_Spm_Spr_stage_avg(RSR_Spm_Spr_stage_avg);
                for (int i = 0; i < numStages; i++) {
                    RSR_Spm_Spr_stage_avg.set(i, cpaMetricsComputer.computeRSR(Spm_stage_avg.get(i), Spr_stage_avg.get(i)));
                }
            } else {
                taskMetrics.setRSR_Spm_Spr_avg(null);
                taskMetrics.setRSR_Spm_Spr_stage_avg(null);
            }
            if (calc_RSR_Spm_Spq_avg) {
                taskMetrics.setRSR_Spm_Spq_avg(cpaMetricsComputer.computeRSR(Spm_avg, Spq_avg));
                List<Double> RSR_Spm_Spq_stage_avg = MetricsUtils.initializeDoubleList(taskMetrics.getRSR_Spm_Spq_stage_avg(), numStages);
                taskMetrics.setRSR_Spm_Spq_stage_avg(RSR_Spm_Spq_stage_avg);
                for (int i = 0; i < numStages; i++) {
                    RSR_Spm_Spq_stage_avg.set(i, cpaMetricsComputer.computeRSR(Spm_stage_avg.get(i), Spq_stage_avg.get(i)));
                }
            } else {
                taskMetrics.setRSR_Spm_Spq_avg(null);
                taskMetrics.setRSR_Spm_Spq_stage_avg(null);
            }

            //Update RSR_alt_1_avg
            if (calc_RSR_alt_1) {
                taskMetrics.setRSR_alt_1_avg(RSR_alt_1_avg_count > 0 ? RSR_alt_1_avg / RSR_alt_1_avg_count : null);
                for (int i = 0; i < numStages; i++) {
                    int count = RSR_alt_1_stage_avg_count.get(i);
                    RSR_alt_1_stage_avg.set(i, count > 0 ? RSR_alt_1_stage_avg.get(i) / count : null);
                }
            } else {
                taskMetrics.setRSR_alt_1_avg(null);
                taskMetrics.setRSR_alt_1_stage_avg(null);
            }

            //Update RSR_alt_2_avg
            if (calc_RSR_alt_2) {
                taskMetrics.setRSR_alt_2_avg(RSR_alt_2_avg_count > 0 ? RSR_alt_2_avg / RSR_alt_2_avg_count : null);
                for (int i = 0; i < numStages; i++) {
                    int count = RSR_alt_2_stage_avg_count.get(i);
                    RSR_alt_2_stage_avg.set(i, count > 0 ? RSR_alt_2_stage_avg.get(i) / count : null);
                }
            } else {
                taskMetrics.setRSR_alt_2_avg(null);
                taskMetrics.setRSR_alt_2_stage_avg(null);
            }

            //Update ASR_avg
            if (calc_ASR) {
                taskMetrics.setASR_avg(ASR_avg_count > 0 ? ASR_avg / ASR_avg_count : null);
                for (int i = 0; i < numStages; i++) {
                    int count = ASR_stage_avg_count.get(i);
                    ASR_stage_avg.set(i, count > 0 ? ASR_stage_avg.get(i) / count : null);
                }
            } else {
                taskMetrics.setASR_avg(null);
                taskMetrics.setASR_stage_avg(null);
            }

            //Update RMR avg
            if (calc_RMR) {
                taskMetrics.setRMR_avg(RMR_avg_count > 0 ? RMR_avg / RMR_avg_count : null);
                //Update layer frequency percents
                taskMetrics.setF_LS_all_trials_percent(MetricsUtils_Phase1.computePercentsFromCounts(
                        F_LS_all_trials_count, taskMetrics.getF_LS_all_trials_percent()));
            } else {
                taskMetrics.setRMR_avg(null);
            }
        }

        taskMetrics.setMetrics_stale(false);

        return taskMetrics;
    }
}
