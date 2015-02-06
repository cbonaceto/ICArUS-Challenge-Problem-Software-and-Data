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

import java.util.HashSet;
import java.util.List;
import static org.mitre.icarus.cps.assessment.assessment_metrics_computer.ExamMetricsComputerUtils.computeOverallCFACPAMetric;
import static org.mitre.icarus.cps.assessment.assessment_metrics_computer.ExamMetricsComputerUtils.getTasksMissing;
import static org.mitre.icarus.cps.assessment.assessment_metrics_computer.ExamMetricsComputerUtils.initializeOverallCFACPAMetric;
import static org.mitre.icarus.cps.assessment.assessment_metrics_computer.ExamMetricsComputerUtils.updateOverallCFACPAMetric;

import org.mitre.icarus.cps.assessment.assessment_metrics_computer.IExamMetricsComputer;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.CFAMetric;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.CPAMetric;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.OverallCFACPAMetric;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.ExamMetrics;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.MetricsInfo;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.SubjectMetrics;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.TaskMetrics;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.TrialData;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.TrialMetrics;

/**
 * Computes Phase 1 exam metrics.
 *
 * @author cbonaceto
 *
 */
public class ExamMetricsComputer extends MetricsComputer implements
        IExamMetricsComputer<TrialData, TaskMetrics, ExamMetrics, SubjectMetrics, MetricsInfo> {

    /**
     * @param examMetrics
     * @param tasks
     * @return
     */
    @Override
    public ExamMetrics updateCompletionStatus(ExamMetrics examMetrics, List<TaskMetrics> tasks) {
        return updateCompletionStatus(examMetrics, tasks, null);
    }

    /**
     * @param examMetrics
     * @param tasks
     * @param modifiedTask
     * @return
     */
    @Override
    public ExamMetrics updateCompletionStatus(ExamMetrics examMetrics, 
            List<TaskMetrics> tasks, TaskMetrics modifiedTask) {
        if (examMetrics == null) {
            examMetrics = new ExamMetrics();
            examMetrics.setTasks(tasks);
        }

        Boolean exam_complete = examMetrics.isExam_complete();
        if (modifiedTask != null && exam_complete != null && exam_complete == true) {
            if (modifiedTask.isTask_complete() == null || !modifiedTask.isTask_complete()) {
                exam_complete = false;
            }
        } else {
            exam_complete = true;
            if (tasks != null && !tasks.isEmpty()) {
                for (TaskMetrics task : tasks) {
                    if (task == null || task.isTask_complete() == null || !task.isTask_complete()) {
                        exam_complete = false;
                        break;
                    }
                }
            } else {
                exam_complete = false;
            }
        }
        examMetrics.setExam_complete(exam_complete);

        return examMetrics;
    }

    /**
     * @param examMetrics
     * @param tasks
     * @param metricsInfo
     * @return 
     */
    @Override
    public ExamMetrics updateAllMetrics(ExamMetrics examMetrics, List<TaskMetrics> tasks,
            SubjectMetrics subjectMetrics, MetricsInfo metricsInfo) {
        return updateStaleMetrics(examMetrics, tasks, subjectMetrics, metricsInfo,
                true, true, true, true, true, true, true, true);
    }

    /**
     * @param examMetrics
     * @param tasks
     * @param metricsInfo
     * @param modifiedTask
     * @return 
     */
    @Override
    public ExamMetrics updateStaleMetricsBasedOnModifiedTask(ExamMetrics examMetrics, 
            List<TaskMetrics> tasks, SubjectMetrics subjectMetrics,
            MetricsInfo metricsInfo, TaskMetrics modifiedTask) {
        if (modifiedTask != null) {
            int taskNum = modifiedTask.getTask_number();
            boolean rr_stale = metricsInfo.getRR_info() != null && metricsInfo.getRR_info().isAssessedForTask(taskNum);
            boolean ai_cw_stale = (metricsInfo.getAI_info() != null && metricsInfo.getAI_info().isAssessedForTask(taskNum))
                    || (metricsInfo.getCW_info() != null && metricsInfo.getCW_info().isAssessedForTask(taskNum));
            boolean pm_f_stale = metricsInfo.getPM_F_info() != null && metricsInfo.getPM_F_info().isAssessedForTask(taskNum);
            boolean pm_rms_stale = metricsInfo.getPM_RMS_info() != null && metricsInfo.getPM_RMS_info().isAssessedForTask(taskNum);
            boolean cs_stale = metricsInfo.getCS_info() != null && metricsInfo.getCS_info().isAssessedForTask(taskNum);
            boolean rsr_stale = metricsInfo.getRSR_info() != null && metricsInfo.getRSR_info().isAssessedForTask(taskNum);
            boolean asr_stale = metricsInfo.getASR_info() != null && metricsInfo.getASR_info().isAssessedForTask(taskNum);
            boolean rmr_stale = metricsInfo.getRMR_info() != null && metricsInfo.getRMR_info().isAssessedForTask(taskNum);
            return updateStaleMetrics(examMetrics, tasks, subjectMetrics, metricsInfo, rr_stale, ai_cw_stale,
                    pm_f_stale, pm_rms_stale, cs_stale, rsr_stale, asr_stale, rmr_stale);
        } else {
            return examMetrics;
        }
    }

    /**
     * @param examMetrics
     * @param tasks
     * @param subjectMetrics
     * @param metricsInfo
     * @param rr_stale
     * @param ai_cw_stale
     * @param pm_f_stale
     * @param pm_rms_stale
     * @param cs_stale
     * @param rsr_stale
     * @param asr_stale
     * @param rmr_stale
     * @return
     */
    public ExamMetrics updateStaleMetrics(ExamMetrics examMetrics, List<TaskMetrics> tasks, 
            SubjectMetrics subjectMetrics, MetricsInfo metricsInfo,
            boolean rr_stale, boolean ai_cw_stale, boolean pm_f_stale, boolean pm_rms_stale, boolean cs_stale,
            boolean rsr_stale, boolean asr_stale, boolean rmr_stale) {
	//Update overall performance summary for metrics based on modified task, 
        //or update all metrics, or update metrics currently flagged as stale
        //Information needed: Which metrics to compute, which tasks to compute them on, performance metrics for each trial for
        //which metrics are computed for
        if (examMetrics == null) {
            examMetrics = new ExamMetrics();
            examMetrics.setTasks(tasks);
        }
        if (metricsInfo == null) {
            metricsInfo = MetricsInfo.createDefaultMetricsInfo();
        }

        //Determine metrics that are stale and need to be updated
        Boolean RR_stale = rr_stale && metricsInfo.getRR_info() != null && metricsInfo.getRR_info().isCalculated();
        examMetrics.setRR_stale(RR_stale);
        Boolean AI_stale = ai_cw_stale && metricsInfo.getAI_info() != null && metricsInfo.getAI_info().isCalculated();
        examMetrics.setAI_stale(AI_stale);
        Boolean CW_stale = ai_cw_stale && metricsInfo.getCW_info() != null && metricsInfo.getCW_info().isCalculated();
        examMetrics.setCW_stale(CW_stale);
        Boolean PM_F_stale = pm_f_stale && metricsInfo.getPM_F_info() != null && metricsInfo.getPM_F_info().isCalculated();
        examMetrics.setPM_F_stale(PM_F_stale);
        Boolean PM_RMS_stale = pm_rms_stale && metricsInfo.getPM_RMS_info() != null && metricsInfo.getPM_RMS_info().isCalculated();
        examMetrics.setPM_RMS_stale(PM_RMS_stale);
        Boolean CS_stale = cs_stale && metricsInfo.getCS_info() != null && metricsInfo.getCS_info().isCalculated();
        examMetrics.setCS_stale(CS_stale);
        Boolean RSR_default_stale = rsr_stale
                && (metricsInfo.getRSR_info() != null && metricsInfo.getRSR_info().isCalculated()
                || metricsInfo.getRSR_Spm_Spr_avg_info() != null && metricsInfo.getRSR_Spm_Spr_avg_info().isCalculated()
                || metricsInfo.getRSR_Spm_Spq_avg_info() != null && metricsInfo.getRSR_Spm_Spq_avg_info().isCalculated());
        Boolean RSR_Bayesian_stale = rsr_stale && metricsInfo.getRSR_Bayesian_info() != null && metricsInfo.getRSR_Bayesian_info().isCalculated();
        Boolean RSR_Spm_Spr_avg_stale = rsr_stale && metricsInfo.getRSR_Spm_Spr_avg_info() != null
                && metricsInfo.getRSR_Spm_Spr_avg_info().isCalculated();
        Boolean RSR_Spm_Spq_avg_stale = rsr_stale && metricsInfo.getRSR_Spm_Spq_avg_info() != null
                && metricsInfo.getRSR_Spm_Spq_avg_info().isCalculated();
        Boolean RSR_alt_1_stale = rsr_stale && metricsInfo.getRSR_alt_1_info() != null && metricsInfo.getRSR_alt_1_info().isCalculated();
        Boolean RSR_alt_2_stale = rsr_stale && metricsInfo.getRSR_alt_2_info() != null && metricsInfo.getRSR_alt_2_info().isCalculated();
        Boolean RSR_stale = RSR_default_stale || RSR_Bayesian_stale || RSR_Spm_Spr_avg_stale || RSR_Spm_Spq_avg_stale
                || RSR_alt_1_stale || RSR_alt_2_stale;
        examMetrics.setRSR_stale(RSR_stale);
        Boolean ASR_stale = asr_stale && metricsInfo.getASR_info() != null && metricsInfo.getASR_info().isCalculated();
        examMetrics.setASR_stale(ASR_stale);
        Boolean RMR_stale = rmr_stale && metricsInfo.getRMR_info() != null && metricsInfo.getRMR_info().isCalculated();
        examMetrics.setRMR_stale(RMR_stale);

        OverallCFACPAMetric RR = examMetrics.getRR();
        OverallCFACPAMetric AI = examMetrics.getAI();
        OverallCFACPAMetric CW = examMetrics.getCW();
        OverallCFACPAMetric PM_F = examMetrics.getPM_F();
        OverallCFACPAMetric PM_RMS = examMetrics.getPM_RMS();
        OverallCFACPAMetric PM = examMetrics.getPM();
        OverallCFACPAMetric CS = examMetrics.getCS();
        OverallCFACPAMetric RSR = examMetrics.getRSR();
        OverallCFACPAMetric RSR_Bayesian = examMetrics.getRSR_Bayesian();
        OverallCFACPAMetric RSR_Spm_Spr_avg = examMetrics.getRSR_Spm_Spr_avg();
        OverallCFACPAMetric RSR_Spm_Spq_avg = examMetrics.getRSR_Spm_Spq_avg();
        OverallCFACPAMetric RSR_alt_1 = examMetrics.getRSR_alt_1();
        OverallCFACPAMetric RSR_alt_2 = examMetrics.getRSR_alt_2();
        Double Spm_avg = examMetrics.getSpm_avg();
        Double Spr_avg = examMetrics.getSpr_avg();
        Double Spq_avg = examMetrics.getSpq_avg();
        OverallCFACPAMetric ASR = examMetrics.getASR();
        OverallCFACPAMetric RMR = examMetrics.getRMR();
        if (RR_stale || AI_stale || CW_stale || PM_F_stale || PM_RMS_stale || CS_stale
                || RSR_stale || ASR_stale || RMR_stale) {
            if (RR_stale) {
                //Initialize RR				
                RR = initializeOverallCFACPAMetric(RR, metricsInfo.getRR_info());
                examMetrics.setRR(RR);
            }
            if (AI_stale) {
                //Initialize AI
                AI = initializeOverallCFACPAMetric(AI, metricsInfo.getAI_info());
                examMetrics.setAI(AI);
            }
            if (CW_stale) {
                //Initialize CW
                CW = initializeOverallCFACPAMetric(CW, metricsInfo.getCW_info());
                examMetrics.setCW(CW);
            }
            if (PM_F_stale) {
                //Initialize PM_F
                PM_F = initializeOverallCFACPAMetric(PM_F, metricsInfo.getPM_F_info());
                examMetrics.setPM_F(PM_F);
            }
            if (PM_RMS_stale) {
                //Initialize PM_RMS
                PM_RMS = initializeOverallCFACPAMetric(PM_RMS, metricsInfo.getPM_RMS_info());
                examMetrics.setPM_RMS(PM_RMS);
            }
            if (PM_F_stale || PM_RMS_stale) {
                //Initialize PM
                PM = initializeOverallCFACPAMetric(PM, metricsInfo.getPM_info());
                examMetrics.setPM(PM);
            }
            if (CS_stale) {
                //Initialize CS
                CS = initializeOverallCFACPAMetric(CS, metricsInfo.getCS_info());
                examMetrics.setCS(CS);
            }

            int spm_trials_present = 0;
            int spr_trials_present = 0;
            int spq_trials_present = 0;

            if (RSR_default_stale) {
                //Initialize RSR
                RSR = initializeOverallCFACPAMetric(RSR, metricsInfo.getRSR_info());
                examMetrics.setRSR(RSR);

                //Initialize Spm_avg, Spr_avg, Spq_avg				
                Spm_avg = 0D;
                Spr_avg = 0D;
                Spq_avg = 0D;
            }
            if (RSR_Bayesian_stale) {
                //Initialize RSR_Bayesian
                RSR_Bayesian = initializeOverallCFACPAMetric(RSR_Bayesian, metricsInfo.getRSR_Bayesian_info());
                examMetrics.setRSR_Bayesian(RSR_Bayesian);
            }
            if (RSR_Spm_Spr_avg_stale) {
                //Initialize RSR_Spm_Spr_avg
                RSR_Spm_Spr_avg = initializeOverallCFACPAMetric(RSR_Spm_Spr_avg, metricsInfo.getRSR_Spm_Spr_avg_info());
                examMetrics.setRSR_Spm_Spr_avg(RSR_Spm_Spr_avg);
            }
            if (RSR_Spm_Spq_avg_stale) {
                //Initialize RSR_Spm_Spq_avg
                RSR_Spm_Spq_avg = initializeOverallCFACPAMetric(RSR_Spm_Spq_avg, metricsInfo.getRSR_Spm_Spq_avg_info());
                examMetrics.setRSR_Spm_Spq_avg(RSR_Spm_Spq_avg);
            }
            if (RSR_alt_1_stale) {
                //Initialize RSR_alt_1
                RSR_alt_1 = initializeOverallCFACPAMetric(RSR_alt_1, metricsInfo.getRSR_alt_1_info());
                examMetrics.setRSR_alt_1(RSR_alt_1);
            }
            if (RSR_alt_2_stale) {
                //Initialize RSR_alt_2
                RSR_alt_2 = initializeOverallCFACPAMetric(RSR_alt_2, metricsInfo.getRSR_alt_2_info());
                examMetrics.setRSR_alt_2(RSR_alt_2);
            }
            if (ASR_stale) {
                //Initialize ASR
                ASR = initializeOverallCFACPAMetric(ASR, metricsInfo.getASR_info());
                examMetrics.setASR(ASR);
            }
            if (RMR_stale) {
                //Initialize RMR
                RMR = initializeOverallCFACPAMetric(RMR, metricsInfo.getRMR_info());
                examMetrics.setRMR(RMR);
            }

            if (subjectMetrics != null) {
                if (PM_F_stale) {
                    //Update PM_F (computed at the overall subject metrics level, not trial-by-trial)
                    CFAMetric PM_F_subject = subjectMetrics.getPM_F();
                    if (PM_F_subject != null && PM_F_subject.score != null) {
                        PM_F.score = PM_F_subject.score;
                        PM_F.pass = PM_F.score > metricsInfo.getPM_F_info().getOverall_pass_threshold();
                    } else {
                        PM_F.pass = false;
                    }
                }

                if (CS_stale) {
                    //Update CS (computed at the overall subject metrics level, not trial-by-trial)
                    CFAMetric CS_subject = subjectMetrics.getCS();
                    if (CS_subject != null && CS_subject.score != null) {
                        CS.score = CS_subject.score;
                        CS.pass = CS.score > metricsInfo.getCS_info().getOverall_pass_threshold();
                    } else {
                        CS.pass = false;
                    }
                }
            }

            if (tasks != null && !tasks.isEmpty()) {
                for (TaskMetrics task : tasks) {                    
                    int taskNum = task.getTask_number();
                    Boolean calc_RR = RR_stale && metricsInfo.getRR_info().isAssessedForTask(taskNum);
                    Boolean calc_AI = AI_stale && metricsInfo.getAI_info().isAssessedForTask(taskNum);
                    Boolean calc_CW = CW_stale && metricsInfo.getCW_info().isAssessedForTask(taskNum);
                    Boolean calc_PM_F = PM_F_stale && metricsInfo.getPM_F_info().isAssessedForTask(taskNum);
                    Boolean calc_PM_RMS = PM_RMS_stale && metricsInfo.getPM_RMS_info().isAssessedForTask(taskNum);
                    Boolean calc_CS = CS_stale && metricsInfo.getCS_info().isAssessedForTask(taskNum);
                    Boolean calc_RSR_Default = RSR_default_stale && metricsInfo.getRSR_info().isAssessedForTask(taskNum);
                    Boolean calc_RSR_Bayesian = RSR_Bayesian_stale && metricsInfo.getRSR_Bayesian_info().isAssessedForTask(taskNum);
                    Boolean calc_RSR_Spm_Spr_avg = RSR_Spm_Spr_avg_stale && metricsInfo.getRSR_Spm_Spr_avg_info().isAssessedForTask(taskNum);
                    Boolean calc_RSR_Spm_Spq_avg = RSR_Spm_Spq_avg_stale && metricsInfo.getRSR_Spm_Spq_avg_info().isAssessedForTask(taskNum);
                    Boolean calc_RSR_alt_1 = RSR_alt_1_stale && metricsInfo.getRSR_alt_1_info().isAssessedForTask(taskNum);
                    Boolean calc_RSR_alt_2 = RSR_alt_2_stale && metricsInfo.getRSR_alt_2_info().isAssessedForTask(taskNum);
                    Boolean calc_RSR = calc_RSR_Default || calc_RSR_Bayesian || calc_RSR_Spm_Spr_avg || calc_RSR_Spm_Spq_avg || calc_RSR_alt_1 || calc_RSR_alt_2;
                    Boolean calc_ASR = ASR_stale && metricsInfo.getASR_info().isAssessedForTask(taskNum);
                    Boolean calc_RMR = RMR_stale && metricsInfo.getRMR_info().isAssessedForTask(taskNum);

                    if (calc_PM_F && task.getFh_Ph_avg() != null && task.getFh_1_avg() != null) {
                        //Update PM_F tasks present
                        PM_F.tasks_present.add(taskNum);
                    }

                    if (calc_CS && task.getC_all_trials() != null) {
                        //Update CS tasks present
                        CS.tasks_present.add(taskNum);
                    }

                    //When using task weights, RSR and its variants are calculated at the overall task level and averaged over tasks
                    if (calc_RSR && metricsInfo.isUse_rsr_asr_task_weights() != null
                            && metricsInfo.isUse_rsr_asr_task_weights()) {
                        //Calculate RSR averaged by task
                        Double taskWeight = metricsInfo.getRsr_asr_task_weights().get(taskNum - 1);
                        if (calc_RSR_Default && task.getRSR_avg() != null) {
                            //Update RSR, Spm_avg, Spr_avg, and Spq_avg
                            RSR.tasks_present.add(taskNum);
                            RSR.score += task.getRSR_avg() * taskWeight;
                            if (task.getSpm_avg() != null) {
                                Spm_avg += task.getSpm_avg() * taskWeight;
                            }
                            if (task.getSpr_avg() != null) {
                                Spr_avg += task.getSpr_avg() * taskWeight;
                            }
                            if (task.getSpq_avg() != null) {
                                Spq_avg += task.getSpq_avg() * taskWeight;
                            }
                        }
                        if (calc_RSR_Bayesian && task.getRSR_Bayesian_avg() != null) {
                            //Update RSR_Bayesian
                            RSR_Bayesian.tasks_present.add(taskNum);
                            RSR_Bayesian.score += task.getRSR_Bayesian_avg() * taskWeight;
                        }
                        if (calc_RSR_Spm_Spr_avg && task.getRSR_Spm_Spr_avg() != null) {
                            //Update RSR_Spm_Spr_avg
                            RSR_Spm_Spr_avg.tasks_present.add(taskNum);
                            RSR_Spm_Spr_avg.score += task.getRSR_Spm_Spr_avg() * taskWeight;
                        }
                        if (calc_RSR_Spm_Spq_avg && task.getRSR_Spm_Spq_avg() != null) {
                            //Update RSR_Spm_Spq_avg
                            RSR_Spm_Spq_avg.tasks_present.add(taskNum);
                            RSR_Spm_Spq_avg.score += task.getRSR_Spm_Spq_avg() * taskWeight;
                        }
                        if (calc_RSR_alt_1 && task.getRSR_alt_1_avg() != null) {
                            //Update RSR_alt_1
                            RSR_alt_1.tasks_present.add(taskNum);
                            RSR_alt_1.score += task.getRSR_alt_1_avg() * taskWeight;
                        }
                        if (calc_RSR_alt_2 && task.getRSR_alt_2_avg() != null) {
                            //Update RSR_alt_2
                            RSR_alt_2.tasks_present.add(taskNum);
                            RSR_alt_2.score += task.getRSR_alt_2_avg() * taskWeight;
                        }
                    }

                    //When using task weights, ASR is also calculated at the overall task level and averaged over tasks
                    if (calc_ASR && metricsInfo.isUse_rsr_asr_task_weights() != null
                            && metricsInfo.isUse_rsr_asr_task_weights()
                            && task.getASR_avg() != null) {
                        //Update ASR
                        Double taskWeight = metricsInfo.getRsr_asr_task_weights().get(taskNum - 1);
                        ASR.tasks_present.add(taskNum);
                        ASR.score += task.getASR_avg() * taskWeight;
                    }

                    if (task.getTrials() != null && !task.getTrials().isEmpty()) {
                        for (TrialData trial : task.getTrials()) {
                            if (trial != null && trial.getMetrics() != null) {
                                TrialMetrics trialMetrics = trial.getMetrics();

                                if (calc_RR) {
                                    //Update RR
                                    updateOverallCFACPAMetric(RR, trialMetrics.getRR(), metricsInfo.getRR_info(), taskNum);
                                }

                                if (calc_AI) {
                                    //Update AI
                                    if (trialMetrics.getAI() != null && !trialMetrics.getAI().isEmpty()) {
                                        for (CFAMetric ai : trialMetrics.getAI()) {
                                            updateOverallCFACPAMetric(AI, ai, metricsInfo.getAI_info(), taskNum);
                                        }
                                    }
                                }

                                if (calc_CW) {
                                    //Update CW
                                    if (trialMetrics.getCW() != null && !trialMetrics.getCW().isEmpty()) {
                                        for (CFAMetric cw : trialMetrics.getCW()) {
                                            updateOverallCFACPAMetric(CW, cw, metricsInfo.getCW_info(), taskNum);
                                        }
                                    }
                                }

                                //Update PM_F trials present
                                if (calc_PM_F && trialMetrics.getFh_Ph() != null && trialMetrics.getFh_1() != null) {
                                    PM_F.trials_stages_present++;
                                }

								//if(calc_PM_F) {
                                //Update PM_F
                                //	updateOverallCFACPAMetric(PM_F, trialMetrics.getPM(), metricsInfo.getPM_F_info());
                                //} else if(calc_PM_RMS) {								
                                if (calc_PM_RMS) {
                                    //Update PM_RMS
                                    updateOverallCFACPAMetric(PM_RMS, trialMetrics.getPM(), metricsInfo.getPM_RMS_info(), taskNum);
                                }

                                //Update CS trials present
                                if (calc_CS && trialMetrics.getC() != null) {
                                    CS.trials_stages_present++;
                                }

                                //Update RSR and its variants on a trial-by-trial basis								
                                if (calc_RSR_Default) {
                                    //Update RSR (standard), Spm_avg, Spr_avg, and Spq_avg
                                    if (trialMetrics.getRSR() != null && !trialMetrics.getRSR().isEmpty()) {
                                        for (int i = 0; i < trialMetrics.getRSR().size(); i++) {
                                            //Update RSR
                                            CPAMetric rsr = trialMetrics.getRSR().get(i);
                                            if (rsr != null) {
                                                if (!metricsInfo.isUse_rsr_asr_task_weights()) {
                                                    //Update RSR trial-by-trial average and trials present
                                                    updateOverallCFACPAMetric(RSR, rsr, metricsInfo.getRSR_info(), taskNum);
                                                }
                                                if (rsr.assessed) {
                                                    if (metricsInfo.isUse_rsr_asr_task_weights() && rsr.score != null) {
                                                        //Update trials present for RSR since it wasn't updated above
                                                        RSR.trials_stages_present++;
                                                    }

                                                    //Update Spm, Spr, and Spq and trials present for Spm, Spr, and Spq
                                                    if (i < (trialMetrics.getSpm().size() - 1)) {
                                                        if (!metricsInfo.isUse_rsr_asr_task_weights()) {
                                                            Spm_avg += trialMetrics.getSpm().get(i);
                                                        }
                                                        spm_trials_present++;
                                                    }
                                                    if (i < (trialMetrics.getSpr().size() - 1)) {
                                                        if (!metricsInfo.isUse_rsr_asr_task_weights()) {
                                                            Spr_avg += trialMetrics.getSpr().get(i);
                                                        }
                                                        spr_trials_present++;
                                                    }
                                                    if (i < (trialMetrics.getSpq().size() - 1)) {
                                                        if (!metricsInfo.isUse_rsr_asr_task_weights()) {
                                                            Spq_avg += trialMetrics.getSpq().get(i);
                                                        }
                                                        spq_trials_present++;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                if (calc_RSR_Bayesian) {
                                    //Update RSR_Bayesian
                                    if (trialMetrics.getRSR_Bayesian() != null && !trialMetrics.getRSR_Bayesian().isEmpty()) {
                                        for (CPAMetric rsr_bayesian : trialMetrics.getRSR_Bayesian()) {
                                            if (rsr_bayesian != null) {
                                                if (!metricsInfo.isUse_rsr_asr_task_weights()) {
                                                    //Update RSR_Bayesian trial-by-trial average and trials present
                                                    updateOverallCFACPAMetric(RSR_Bayesian, rsr_bayesian, metricsInfo.getRSR_Bayesian_info(), taskNum);
                                                } else if (rsr_bayesian.assessed && rsr_bayesian.score != null) {
                                                    //Just update trials present for RSR_Bayesian
                                                    RSR_Bayesian.trials_stages_present++;
                                                }
                                            }
                                        }
                                    }
                                }

                                if (calc_RSR_alt_1) {
                                    //Update RSR_alt_1
                                    if (trialMetrics.getRSR_alt_1() != null && !trialMetrics.getRSR_alt_1().isEmpty()) {
                                        for (CPAMetric rsr_alt_1 : trialMetrics.getRSR_alt_1()) {
                                            if (rsr_alt_1 != null) {
                                                if (!metricsInfo.isUse_rsr_asr_task_weights()) {
                                                    //Update RSR_alt_1 trial-by-trial average and trials present
                                                    updateOverallCFACPAMetric(RSR_alt_1, rsr_alt_1, metricsInfo.getRSR_alt_1_info(), taskNum);
                                                } else if (rsr_alt_1.assessed && rsr_alt_1.score != null) {
                                                    //Just update trials present for RSR_alt_1
                                                    RSR_alt_1.trials_stages_present++;
                                                }
                                            }
                                        }
                                    }
                                }

                                if (calc_RSR_alt_2) {
                                    //Update RSR_alt_2
                                    if (trialMetrics.getRSR_alt_2() != null && !trialMetrics.getRSR_alt_2().isEmpty()) {
                                        for (CPAMetric rsr_alt_2 : trialMetrics.getRSR_alt_2()) {
                                            if (!metricsInfo.isUse_rsr_asr_task_weights()) {
                                                //Update RSR_alt_2 trial-by-trial average and trials present
                                                updateOverallCFACPAMetric(RSR_alt_2, rsr_alt_2, metricsInfo.getRSR_alt_2_info(), taskNum);
                                            } else if (rsr_alt_2.assessed && rsr_alt_2.score != null) {
                                                //Just update trials present for RSR_alt_2
                                                RSR_alt_2.trials_stages_present++;
                                            }
                                        }
                                    }
                                }

                                if (calc_ASR) {
                                    //Update ASR
                                    if (trialMetrics.getASR() != null && !trialMetrics.getASR().isEmpty()) {
                                        for (CPAMetric asr : trialMetrics.getASR()) {
                                            if (!metricsInfo.isUse_rsr_asr_task_weights()) {
                                                //Update ASR trial-by-trial average and trials present
                                                updateOverallCFACPAMetric(ASR, asr, metricsInfo.getASR_info(), taskNum);
                                            } else if (asr.assessed && asr.score != null) {
                                                //Just update trials present for ASR
                                                ASR.trials_stages_present++;
                                            }
                                        }
                                    }
                                }

                                if (calc_RMR) {
                                    //Update RMR
                                    updateOverallCFACPAMetric(RMR, trialMetrics.getRMR(), metricsInfo.getRMR_info(), taskNum);
                                }
                            }
                        }
                    }
                }
            }

            if (RR_stale) {
                //Compute overall RR score
                computeOverallCFACPAMetric(RR, metricsInfo.getRR_info(), RR.score);
                RR_stale = false;
            }
            if (AI_stale) {
                //Compute overall AI score
                computeOverallCFACPAMetric(AI, metricsInfo.getAI_info(), AI.score);
                AI_stale = false;
            }
            if (CW_stale) {
                //Compute overall CW score
                computeOverallCFACPAMetric(CW, metricsInfo.getCW_info(), CW.score);
                CW_stale = false;
            }
            if (PM_F_stale || PM_RMS_stale) {
                //Compute overall PM_F, PM_RMS, and PM scores
                if (PM_F_stale) {
					//Compute overall PM_F score
					/*if(PM_F.tasks_present.size() > 0) {
                     PM_F.score = PM_F.score/PM_F.tasks_present.size();
                     }
                     PM_F.pass = PM_F.score >= metricsInfo.getPM_F_info().getOverall_pass_threshold();*/
                    PM_F.creditsEarned = (PM_F.pass != null && PM_F.pass) ? 1d : 0d;
                    PM_F.tasks_missing = getTasksMissing(PM_F.tasks_present, metricsInfo.getPM_F_info().getTasks());
                    PM_F.trials_stages_missing = metricsInfo.getPM_F_info().getNum_trials_stages() - PM_F.trials_stages_present;
                    PM_F_stale = false;
                }
                if (PM_RMS_stale) {
                    //Compute overall PM_RMS score
                    computeOverallCFACPAMetric(PM_RMS, metricsInfo.getPM_RMS_info(), PM_RMS.score);
                    PM_RMS_stale = false;
                }
                if (metricsInfo.getPM_F_info().isAssessed() && metricsInfo.getPM_RMS_info().isAssessed()) {
                    //Both PM_F and PM_RMS are assessed, compute PM using both scores
                    PM_F.creditsEarned = (PM_F.pass != null && PM_F.pass) ? 0.5 : 0d;
                    PM_RMS.creditsEarned = (PM_RMS.pass != null && PM_RMS.pass) ? 0.5d : 0d;
                    PM.creditsEarned = PM_F.creditsEarned + PM_RMS.creditsEarned;
                    PM.score = ((PM_F.score != null ? PM_F.score : 0d) + (PM_RMS.score != null ? PM_RMS.score : 0d)) / 2d;
                    PM.trials_stages_present = PM_F.trials_stages_present + PM_RMS.trials_stages_present;
                    PM.trials_stages_missing = PM_F.trials_stages_missing + PM_RMS.trials_stages_missing;
                    PM.tasks_present = new HashSet<Integer>();
                    PM.tasks_missing = new HashSet<Integer>();
                    PM.tasks_present.addAll(PM_F.tasks_present);
                    PM.tasks_present.addAll(PM_RMS.tasks_present);
                    PM.tasks_missing.addAll(PM_F.tasks_missing);
                    PM.tasks_missing.addAll(PM_RMS.tasks_missing);
                } else if (metricsInfo.getPM_F_info().isAssessed()) {
                    //Only PM_F is assessed, compute PM just using PM_F
                    PM = PM_F;
                } else {
                    //Only PM_RMS is assessed, compute PM just using PM_RMF
                    PM = PM_RMS;
                }
            }
            if (CS_stale) {
				//Compute overall CS score
				/*if(CS.tasks_present.size() > 0) {
                 CS.score = CS.score/CS.tasks_present.size();
                 }					
                 CS.pass = CS.score >= metricsInfo.getCS_info().getOverall_pass_threshold();*/
                CS.creditsEarned = (CS.pass != null && CS.pass) ? 1d : 0d;
                CS.tasks_missing = getTasksMissing(CS.tasks_present, metricsInfo.getCS_info().getTasks());
                CS.trials_stages_missing = metricsInfo.getCS_info().getNum_trials_stages() - CS.trials_stages_present;
                CS_stale = false;
            }

            boolean useWeights = metricsInfo.isUse_rsr_asr_task_weights();
            if (RSR_stale) {
                //Compute overall RSR scores				
                if (RSR_default_stale) {
                    //Compute RSR standard, Spm_avg, Spr_avg, and Spq_avg
                    computeOverallCFACPAMetric(RSR, metricsInfo.getRSR_info(), useWeights ? null : RSR.score);
                    if (!useWeights) {
                        Spm_avg = Spm_avg / spm_trials_present;
                        Spr_avg = Spr_avg / spr_trials_present;
                        Spq_avg = Spq_avg / spq_trials_present;
                    }
                    examMetrics.setSpm_avg(Spm_avg);
                    examMetrics.setSpr_avg(Spr_avg);
                    examMetrics.setSpq_avg(Spq_avg);
                }

                if (RSR_Bayesian_stale) {
                    //Compute RSR Bayesian
                    computeOverallCFACPAMetric(RSR_Bayesian, metricsInfo.getRSR_Bayesian_info(),
                            useWeights ? null : RSR_Bayesian.score);
                }

                if (RSR_Spm_Spr_avg_stale) {
                    //Compute RSR_Spm_Spr
                    if (!useWeights) {
                        RSR_Spm_Spr_avg.score = cpaMetricsComputer.computeRSR(Spm_avg, Spr_avg);
                    }
                    computeOverallCFACPAMetric(RSR_Spm_Spr_avg, metricsInfo.getRSR_Spm_Spr_avg_info(), null);
                }

                if (RSR_Spm_Spq_avg_stale) {
                    //Compute RSR_Spm_Spq
                    if (!useWeights) {
                        RSR_Spm_Spq_avg.score = cpaMetricsComputer.computeRSR(Spm_avg, Spq_avg);
                    }
                    computeOverallCFACPAMetric(RSR_Spm_Spq_avg, metricsInfo.getRSR_Spm_Spq_avg_info(), null);
                }

                if (RSR_alt_1_stale) {
                    //Compute RSR_alt_1
                    computeOverallCFACPAMetric(RSR_alt_1, metricsInfo.getRSR_alt_1_info(),
                            useWeights ? null : RSR_alt_1.score);
                }

                if (RSR_alt_2_stale) {
                    //Compute RSR_alt_2
                    computeOverallCFACPAMetric(RSR_alt_2, metricsInfo.getRSR_alt_2_info(),
                            useWeights ? null : RSR_alt_2.score);
                }

                RSR_stale = false;
            }

            if (ASR_stale) {
                //Compute overall ASR score
                computeOverallCFACPAMetric(ASR, metricsInfo.getASR_info(),
                        useWeights ? null : ASR.score);
                ASR_stale = false;
            }

            if (RMR_stale) {
                //Compute overall RMR score
                computeOverallCFACPAMetric(RMR, metricsInfo.getRMR_info(), RMR.score);
                RMR_stale = false;
            }

            examMetrics.setRR_stale(RR_stale);
            examMetrics.setAI_stale(AI_stale);
            examMetrics.setCW_stale(CW_stale);
            examMetrics.setPM_F_stale(PM_F_stale);
            examMetrics.setPM_RMS_stale(PM_RMS_stale);
            examMetrics.setCS_stale(CS_stale);
            examMetrics.setRSR_stale(RSR_stale);
            examMetrics.setASR_stale(ASR_stale);
            examMetrics.setRMR_stale(RMR_stale);

            //Compute overall CFA score
            double cfa_credits_earned = 0d;
            double cfa_credits_possible = 0d;
            if (metricsInfo.getRR_info().isAssessed()) {
                cfa_credits_earned += RR.creditsEarned != null ? RR.creditsEarned : 0d;
                cfa_credits_possible++;
            }
            if (metricsInfo.getAI_info().isAssessed()) {
                cfa_credits_earned += AI.creditsEarned != null ? AI.creditsEarned : 0d;
                cfa_credits_possible++;
            }
            if (metricsInfo.getCW_info().isAssessed()) {
                cfa_credits_earned += CW.creditsEarned != null ? CW.creditsEarned : 0d;
                cfa_credits_possible++;
            }
            if (metricsInfo.getPM_info().isAssessed()) {
                cfa_credits_earned += PM.creditsEarned != null ? PM.creditsEarned : 0d;
                cfa_credits_possible++;
            }
            if (metricsInfo.getCS_info().isAssessed()) {
                cfa_credits_earned += CS.creditsEarned != null ? CS.creditsEarned : 0d;
                cfa_credits_possible++;
            }
            examMetrics.setCFA_credits_earned(cfa_credits_earned);
            examMetrics.setCFA_credits_possible(cfa_credits_possible);
            double cfa_score = (cfa_credits_earned / cfa_credits_possible) * 100d;
            examMetrics.setCFA_score(cfa_score);
            examMetrics.setCFA_pass(cfa_score >= metricsInfo.getCfa_pass_threshold());

            //Compute overall CPA score
            OverallCFACPAMetric RSR_official;
            switch (metricsInfo.getEvaluated_RSR_Type()) {
                case RSR_Standard:
                    RSR_official = RSR;
                    break;
                case RSR_Bayesian:
                    RSR_official = RSR_Bayesian;
                    break;
                case RSR_Spm_Spr_avg:
                    RSR_official = RSR_Spm_Spr_avg;
                    break;
                case RSR_Spm_Spq_avg:
                    RSR_official = RSR_Spm_Spq_avg;
                    break;
                case RSR_alt_1:
                    RSR_official = RSR_alt_1;
                    break;
                case RSR_alt_2:
                    RSR_official = RSR_alt_2;
                    break;
                default:
                    RSR_official = RSR;
                    break;
            }
            double rsr_score = (RSR_official != null && RSR_official.score != null) ? RSR_official.score : 0d;
            double asr_score = (ASR != null && ASR.score != null) ? ASR.score : 0d;
            double rmr_score = (RMR != null && RMR.score != null) ? RMR.score : 0d;

            double cpa_score = ((metricsInfo.isUse_asr_as_evaluation_metric() != null
                    && metricsInfo.isUse_asr_as_evaluation_metric() ? asr_score : rsr_score) * metricsInfo.getRsr_asr_weight())
                    + (rmr_score * metricsInfo.getRmr_weight());
            examMetrics.setCPA_score(cpa_score);
            examMetrics.setCPA_pass(cpa_score >= metricsInfo.getCpa_pass_threshold());
        }

        return examMetrics;
    }
}