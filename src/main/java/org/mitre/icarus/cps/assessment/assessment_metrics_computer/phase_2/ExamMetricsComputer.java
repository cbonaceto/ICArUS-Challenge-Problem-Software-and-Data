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

import java.util.List;
import static org.mitre.icarus.cps.assessment.assessment_metrics_computer.ExamMetricsComputerUtils.computeOverallCFACPAMetric;
import static org.mitre.icarus.cps.assessment.assessment_metrics_computer.ExamMetricsComputerUtils.getTaskWeight;
import static org.mitre.icarus.cps.assessment.assessment_metrics_computer.ExamMetricsComputerUtils.initializeOverallCFACPAMetric;
import static org.mitre.icarus.cps.assessment.assessment_metrics_computer.ExamMetricsComputerUtils.updateOverallCFACPAMetric;
import org.mitre.icarus.cps.assessment.assessment_metrics_computer.IExamMetricsComputer;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.CFAMetric;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.CPAMetric;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.OverallCFACPAMetric;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.ExamMetrics;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.MetricsInfo;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.MissionMetrics;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.RSRAndASRMissionMetrics;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.RSRAndASRTrialMetrics;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.SubjectMetrics;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.TrialData;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.TrialMetrics;

/**
 * Computes Phase 2 exam metrics.
 * 
 * @author CBONACETO
 */
public class ExamMetricsComputer extends MetricsComputer implements 
	IExamMetricsComputer<TrialData, MissionMetrics, ExamMetrics, SubjectMetrics, MetricsInfo> {

    @Override
    public ExamMetrics updateCompletionStatus(ExamMetrics examMetrics,
            List<MissionMetrics> missions) {
        return updateCompletionStatus(examMetrics, missions, null);
    }

    @Override
    public ExamMetrics updateCompletionStatus(ExamMetrics examMetrics,
            List<MissionMetrics> missions, MissionMetrics modifiedMission) {
        if (examMetrics == null) {
            examMetrics = new ExamMetrics();
            examMetrics.setTasks(missions);
        }

        Boolean exam_complete = examMetrics.isExam_complete();
        if (modifiedMission != null && exam_complete != null && exam_complete == true) {
            if (modifiedMission.isTask_complete() == null || !modifiedMission.isTask_complete()) {
                exam_complete = false;
            }
        } else {
            exam_complete = true;
            if (missions != null && !missions.isEmpty()) {
                for (MissionMetrics mission : missions) {
                    if (mission == null || mission.isTask_complete() == null
                            || !mission.isTask_complete()) {
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

    @Override
    public ExamMetrics updateAllMetrics(ExamMetrics examMetrics, 
            List<MissionMetrics> missions, SubjectMetrics subjectMetrics, 
            MetricsInfo metricsInfo) {
         return updateStaleMetrics(examMetrics, missions, subjectMetrics, metricsInfo,
                true, true, true, true, true, true, true, true, true, true, true);
    }

    @Override
    public ExamMetrics updateStaleMetricsBasedOnModifiedTask(ExamMetrics examMetrics,
            List<MissionMetrics> missions, SubjectMetrics subjectMetrics, 
            MetricsInfo metricsInfo, MissionMetrics modifiedMission) {
       if (modifiedMission != null) {
            int taskNum = modifiedMission.getTask_number();
            boolean aa_stale = metricsInfo.getAA_info() != null 
                    && metricsInfo.getAA_info().isAssessedForTask(taskNum);
            boolean pde_stale = metricsInfo.getPDE_info() != null 
                    && metricsInfo.getPDE_info().isAssessedForTask(taskNum);
            boolean rr_stale = metricsInfo.getRR_info() != null 
                    && metricsInfo.getRR_info().isAssessedForTask(taskNum);
            boolean av_stale = metricsInfo.getAV_info() != null 
                    && metricsInfo.getAV_info().isAssessedForTask(taskNum);
            boolean pm_stale = metricsInfo.getPM_info() != null 
                    && metricsInfo.getPM_info().isAssessedForTask(taskNum);
            boolean cs_stale = metricsInfo.getCS_info() != null 
                    && metricsInfo.getCS_info().isAssessedForTask(taskNum);
            boolean cb_stale = metricsInfo.getCB_info() != null 
                    && metricsInfo.getCB_info().isAssessedForTask(taskNum);
            boolean ss_stale = metricsInfo.getSS_info() != null 
                    && metricsInfo.getSS_info().isAssessedForTask(taskNum);
            boolean asr_stale = metricsInfo.getASR_info() != null 
                    && metricsInfo.getASR_info().isAssessedForTask(taskNum);
            boolean rsr_stale = metricsInfo.getRSR_info() != null 
                    && metricsInfo.getRSR_info().isAssessedForTask(taskNum);            
            boolean rmr_stale = metricsInfo.getRMR_info() != null 
                    && metricsInfo.getRMR_info().isAssessedForTask(taskNum);
            return updateStaleMetrics(examMetrics, missions, subjectMetrics, metricsInfo, 
                    aa_stale, pde_stale, rr_stale, av_stale, pm_stale, cs_stale, cb_stale,
                    ss_stale, asr_stale, rsr_stale, rmr_stale);
        } else {
            return examMetrics;
        }
    }

    /**
     *
     * @param examMetrics
     * @param missions
     * @param subjectMetrics
     * @param metricsInfo
     * @param aa_stale
     * @param pde_stale
     * @param rr_stale
     * @param av_stale
     * @param pm_stale
     * @param cs_stale
     * @param cb_stale
     * @param ss_stale
     * @param asr_stale
     * @param rsr_stale
     * @param rmr_stale
     * @return
     */
    public ExamMetrics updateStaleMetrics(ExamMetrics examMetrics, List<MissionMetrics> missions, 
            SubjectMetrics subjectMetrics, MetricsInfo metricsInfo, boolean aa_stale, 
            boolean pde_stale, boolean rr_stale, boolean av_stale, boolean pm_stale,
            boolean cs_stale, boolean cb_stale, boolean ss_stale, boolean asr_stale, 
            boolean rsr_stale, boolean rmr_stale) {
        if (examMetrics == null) {
            examMetrics = new ExamMetrics();
            examMetrics.setTasks(missions);
        }
        if (metricsInfo == null) {
            metricsInfo = MetricsInfo.createDefaultMetricsInfo();
        }

        //Determine metrics that are stale and need to be updated
        Boolean AA_stale = aa_stale && metricsInfo.getAA_info() != null 
                && metricsInfo.getAA_info().isCalculated();
        examMetrics.setAA_stale(AA_stale);        
        Boolean PDE_stale = pde_stale && metricsInfo.getPDE_info() != null 
                && metricsInfo.getPDE_info().isCalculated();
        examMetrics.setPDE_stale(pde_stale);
        Boolean RR_stale = rr_stale && metricsInfo.getRR_info() != null 
                && metricsInfo.getRR_info().isCalculated();
        examMetrics.setRR_stale(RR_stale);
        Boolean AV_stale = av_stale && metricsInfo.getAV_info() != null 
                && metricsInfo.getAV_info().isCalculated();
        examMetrics.setAV_stale(AV_stale);
        Boolean PM_stale = pm_stale && metricsInfo.getPM_info() != null 
                && metricsInfo.getPM_info().isCalculated();
        examMetrics.setPM_stale(PM_stale);        
        Boolean CS_stale = cs_stale && metricsInfo.getCS_info() != null 
                && metricsInfo.getCS_info().isCalculated();
        examMetrics.setCS_stale(CS_stale);
        Boolean CB_stale = cb_stale && metricsInfo.getCB_info() != null 
                && metricsInfo.getCB_info().isCalculated();
        examMetrics.setCB_stale(CB_stale);
        Boolean SS_stale = ss_stale && metricsInfo.getSS_info() != null 
                && metricsInfo.getSS_info().isCalculated();
        examMetrics.setSS_stale(SS_stale);
        Boolean ASR_stale = asr_stale && metricsInfo.getASR_info() != null
                && metricsInfo.getASR_info().isCalculated();
        examMetrics.setASR_stale(ASR_stale);
        Boolean RSR_default_stale = rsr_stale
                && (metricsInfo.getRSR_info() != null && metricsInfo.getRSR_info().isCalculated()
                || metricsInfo.getRSR_Spm_Spr_avg_info() != null && metricsInfo.getRSR_Spm_Spr_avg_info().isCalculated()
                || metricsInfo.getRSR_Spm_Spq_avg_info() != null && metricsInfo.getRSR_Spm_Spq_avg_info().isCalculated());
        Boolean RSR_Bayesian_stale = rsr_stale && metricsInfo.getRSR_Bayesian_info() != null 
                && metricsInfo.getRSR_Bayesian_info().isCalculated();
        Boolean RSR_Spm_Spr_avg_stale = rsr_stale && metricsInfo.getRSR_Spm_Spr_avg_info() != null
                && metricsInfo.getRSR_Spm_Spr_avg_info().isCalculated();
        Boolean RSR_Spm_Spq_avg_stale = rsr_stale && metricsInfo.getRSR_Spm_Spq_avg_info() != null
                && metricsInfo.getRSR_Spm_Spq_avg_info().isCalculated();
        Boolean RSR_alt_1_stale = rsr_stale && metricsInfo.getRSR_alt_1_info() != null 
                && metricsInfo.getRSR_alt_1_info().isCalculated();
        Boolean RSR_alt_2_stale = rsr_stale && metricsInfo.getRSR_alt_2_info() != null 
                && metricsInfo.getRSR_alt_2_info().isCalculated();
        Boolean RSR_stale = RSR_default_stale || RSR_Bayesian_stale || RSR_Spm_Spr_avg_stale 
                || RSR_Spm_Spq_avg_stale || RSR_alt_1_stale || RSR_alt_2_stale;
        examMetrics.setRSR_stale(RSR_stale);        
        Boolean RMR_stale = rmr_stale && metricsInfo.getRMR_info() != null 
                && metricsInfo.getRMR_info().isCalculated();
        examMetrics.setRMR_stale(RMR_stale);

        OverallCFACPAMetric AA = examMetrics.getAA();
        OverallCFACPAMetric PDE = examMetrics.getPDE();
        OverallCFACPAMetric RR = examMetrics.getRR();
        OverallCFACPAMetric AV = examMetrics.getAV();
        OverallCFACPAMetric PM = examMetrics.getPM();
        OverallCFACPAMetric CS = examMetrics.getCS();
        OverallCFACPAMetric CB = examMetrics.getCB();
        OverallCFACPAMetric SS = examMetrics.getSS();        
        OverallCFACPAMetric ASR = examMetrics.getASR();
        OverallCFACPAMetric RSR = examMetrics.getRSR();
        OverallCFACPAMetric RSR_Bayesian = examMetrics.getRSR_Bayesian();
        OverallCFACPAMetric RSR_Spm_Spr_avg = examMetrics.getRSR_Spm_Spr_avg();
        OverallCFACPAMetric RSR_Spm_Spq_avg = examMetrics.getRSR_Spm_Spq_avg();
        OverallCFACPAMetric RSR_alt_1 = examMetrics.getRSR_alt_1();
        OverallCFACPAMetric RSR_alt_2 = examMetrics.getRSR_alt_2();
        Double Spm_avg = examMetrics.getSpm_avg();
        Double Spr_avg = examMetrics.getSpr_avg();
        Double Spq_avg = examMetrics.getSpq_avg();        
        OverallCFACPAMetric RMR = examMetrics.getRMR();
        if (AA_stale || PDE_stale || RR_stale || AV_stale || PM_stale || CS_stale
                || CB_stale || SS_stale || ASR_stale || RSR_stale || RMR_stale) {
            if (AA_stale) {
                //Initialize AA
                AA = initializeOverallCFACPAMetric(AA, metricsInfo.getAA_info());
                examMetrics.setAA(AA);
            }
            if (PDE_stale) {
                //Initialize PDE
                PDE = initializeOverallCFACPAMetric(PDE, metricsInfo.getPDE_info());
                examMetrics.setPDE(PDE);
            }
            if (RR_stale) {
                //Initialize RR				
                RR = initializeOverallCFACPAMetric(RR, metricsInfo.getRR_info());
                examMetrics.setRR(RR);
            }  
            if (AV_stale) {
                //Initialize AV
                AV = initializeOverallCFACPAMetric(AV, metricsInfo.getAV_info());
                examMetrics.setAV(AV);
            }
            if (PM_stale) {
                //Initialize PM
                PM = initializeOverallCFACPAMetric(PM, metricsInfo.getPM_info());
                examMetrics.setPM(PM);
            }
            if (CS_stale) {
                //Initialize CS
                CS = initializeOverallCFACPAMetric(CS, metricsInfo.getCS_info());
                examMetrics.setCS(CS);
            }            
            if (CB_stale) {
                //Initialize CB
                CB = initializeOverallCFACPAMetric(CB, metricsInfo.getCB_info());
                examMetrics.setCB(CB);
            }  
            if (SS_stale) {
                //Initialize CS
                SS = initializeOverallCFACPAMetric(SS, metricsInfo.getSS_info());
                examMetrics.setSS(SS);
            } 
            
            if (ASR_stale) {
                //Initialize ASR
                ASR = initializeOverallCFACPAMetric(ASR, metricsInfo.getASR_info());
                examMetrics.setASR(ASR);
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
            if (RMR_stale) {
                //Initialize RMR
                RMR = initializeOverallCFACPAMetric(RMR, metricsInfo.getRMR_info());
                examMetrics.setRMR(RMR);
            }

            if (missions != null && !missions.isEmpty()) {
                for (MissionMetrics mission : missions) {                    
                    int taskNum = mission.getTask_number();
                    Boolean calc_AA = AA_stale && metricsInfo.getAA_info().isAssessedForTask(taskNum);
                    Boolean calc_PDE = PDE_stale && metricsInfo.getPDE_info().isAssessedForTask(taskNum);
                    Boolean calc_RR = RR_stale && metricsInfo.getRR_info().isAssessedForTask(taskNum);
                    Boolean calc_AV = AV_stale && metricsInfo.getAV_info().isAssessedForTask(taskNum);                    
                    Boolean calc_PM = PM_stale && metricsInfo.getPM_info().isAssessedForTask(taskNum);                    
                    Boolean calc_CS = CS_stale && metricsInfo.getCS_info().isAssessedForTask(taskNum);
                    Boolean calc_CB = CB_stale && metricsInfo.getCB_info().isAssessedForTask(taskNum);
                    Boolean calc_SS = SS_stale && metricsInfo.getSS_info().isAssessedForTask(taskNum);
                    Boolean calc_ASR = ASR_stale && metricsInfo.getASR_info().isAssessedForTask(taskNum);
                    Boolean calc_RSR_Default = RSR_default_stale && metricsInfo.getRSR_info().isAssessedForTask(taskNum);
                    Boolean calc_RSR_Bayesian = RSR_Bayesian_stale && metricsInfo.getRSR_Bayesian_info().isAssessedForTask(taskNum);
                    Boolean calc_RSR_Spm_Spr_avg = RSR_Spm_Spr_avg_stale && metricsInfo.getRSR_Spm_Spr_avg_info().isAssessedForTask(taskNum);
                    Boolean calc_RSR_Spm_Spq_avg = RSR_Spm_Spq_avg_stale && metricsInfo.getRSR_Spm_Spq_avg_info().isAssessedForTask(taskNum);
                    Boolean calc_RSR_alt_1 = RSR_alt_1_stale && metricsInfo.getRSR_alt_1_info().isAssessedForTask(taskNum);
                    Boolean calc_RSR_alt_2 = RSR_alt_2_stale && metricsInfo.getRSR_alt_2_info().isAssessedForTask(taskNum);
                    Boolean calc_RSR = calc_RSR_Default || calc_RSR_Bayesian || calc_RSR_Spm_Spr_avg || calc_RSR_Spm_Spq_avg || calc_RSR_alt_1 || calc_RSR_alt_2;                    
                    Boolean calc_RMR = RMR_stale && metricsInfo.getRMR_info().isAssessedForTask(taskNum);
                    
                    if (calc_AA && metricsInfo.getAA_info().isUseTaskWeights() != null
                            && metricsInfo.getAA_info().isUseTaskWeights()
                            && mission.getAA_smr_score() != null) {
                        //Update AA    
                        Double taskWeight = getTaskWeight(metricsInfo.getAA_info(), taskNum);                        
                        AA.tasks_present.add(taskNum);                        
                        AA.score += mission.getAA_smr_score() * taskWeight;
                    }
                    if (calc_PDE && metricsInfo.getPDE_info().isUseTaskWeights() != null
                            && metricsInfo.getPDE_info().isUseTaskWeights()
                            && mission.getPDE_smr_score() != null) {
                        //Update PDE   
                        Double taskWeight = getTaskWeight(metricsInfo.getPDE_info(), taskNum);                        
                        PDE.tasks_present.add(taskNum);                        
                        PDE.score += mission.getPDE_smr_score() * taskWeight;
                    }
                    if (calc_RR && metricsInfo.getRR_info().isUseTaskWeights() != null
                            && metricsInfo.getRR_info().isUseTaskWeights()
                            && mission.getRR_smr_score() != null) {
                        //Update RR    
                        Double taskWeight = getTaskWeight(metricsInfo.getRR_info(), taskNum);                        
                        RR.tasks_present.add(taskNum);                        
                        RR.score += mission.getRR_smr_score() * taskWeight;
                    }
                    if (calc_AV && metricsInfo.getAV_info().isUseTaskWeights() != null
                            && metricsInfo.getAV_info().isUseTaskWeights()
                            && mission.getAV_smr_score() != null) {
                        //Update AV 
                        Double taskWeight = getTaskWeight(metricsInfo.getAV_info(), taskNum);                        
                        AV.tasks_present.add(taskNum);                        
                        AV.score += mission.getAV_smr_score() * taskWeight;
                    }   
                    if (calc_PM && mission.getPM_metrics() != null
                            && mission.getPM_metrics().score != null) {
                        //Update PM                        
                        Double taskWeight = getTaskWeight(metricsInfo.getPM_info(), taskNum);
                        PM.tasks_present.add(taskNum);                        
                        PM.score += mission.getPM_metrics().score * taskWeight;
                    }                    
                    if (calc_CS && mission.getCS_metrics() != null
                            && mission.getCS_metrics().score != null) {
                        //Update CS                        
                        Double taskWeight = getTaskWeight(metricsInfo.getCS_info(), taskNum);
                        CS.tasks_present.add(taskNum);                        
                        CS.score += mission.getCS_metrics().score * taskWeight;
                    }                    
                    if (calc_CB && mission.getCB_metrics() != null
                            && mission.getCB_metrics().score != null) {
                        //Update CB
                        Double taskWeight = getTaskWeight(metricsInfo.getCB_info(), taskNum);
                        CB.tasks_present.add(taskNum);                        
                        CB.score += mission.getCB_metrics().score * taskWeight;
                    }
                    if (calc_SS && mission.getSS_metrics() != null
                            && mission.getSS_metrics().score != null) {
                        //Update SS
                        Double taskWeight = getTaskWeight(metricsInfo.getSS_info(), taskNum);
                        SS.tasks_present.add(taskNum);                        
                        SS.score += mission.getSS_metrics().score * taskWeight;
                    }                    

                    //When using task weights, ASR is calculated at the overall task level and averaged over tasks
                    //TODO: Make task weights part of ASR MetricInfo. Also do this for RSR
                    RSRAndASRMissionMetrics rsrAsrMetrics = mission.getRSR_ASR();      
                    if (calc_ASR && metricsInfo.isUse_rsr_asr_task_weights() != null
                            && metricsInfo.isUse_rsr_asr_task_weights() 
                            && rsrAsrMetrics != null && rsrAsrMetrics.getASR_avg() != null) {
                        //Update ASR                        
                        Double taskWeight = metricsInfo.getRsr_asr_task_weights().get(taskNum - 1); 
                        ASR.tasks_present.add(taskNum);
                        ASR.score += rsrAsrMetrics.getASR_avg() * taskWeight;
                    }

                    //When using task weights, RSR and its variants are calculated at the overall task level and averaged over tasks
                    if (calc_RSR && metricsInfo.isUse_rsr_asr_task_weights() != null
                            && metricsInfo.isUse_rsr_asr_task_weights()
                            && rsrAsrMetrics != null) {
                        //Calculate RSR averaged by task
                        Double taskWeight = metricsInfo.getRsr_asr_task_weights().get(taskNum - 1);
                        if (calc_RSR_Default && rsrAsrMetrics.getRSR_avg() != null) {
                            //Update RSR, Spm_avg, Spr_avg, and Spq_avg
                            RSR.tasks_present.add(taskNum);
                            RSR.score += rsrAsrMetrics.getRSR_avg() * taskWeight;
                            if (rsrAsrMetrics.getSpm_avg() != null) {
                                Spm_avg += rsrAsrMetrics.getSpm_avg() * taskWeight;
                            }
                            if (rsrAsrMetrics.getSpr_avg() != null) {
                                Spr_avg += rsrAsrMetrics.getSpr_avg() * taskWeight;
                            }
                            if (rsrAsrMetrics.getSpq_avg() != null) {
                                Spq_avg += rsrAsrMetrics.getSpq_avg() * taskWeight;
                            }
                        }
                        if (calc_RSR_Bayesian && rsrAsrMetrics.getRSR_Bayesian_avg() != null) {
                            //Update RSR_Bayesian
                            RSR_Bayesian.tasks_present.add(taskNum);
                            RSR_Bayesian.score += rsrAsrMetrics.getRSR_Bayesian_avg() * taskWeight;
                        }
                        if (calc_RSR_Spm_Spr_avg && rsrAsrMetrics.getRSR_Spm_Spr_avg() != null) {
                            //Update RSR_Spm_Spr_avg
                            RSR_Spm_Spr_avg.tasks_present.add(taskNum);
                            RSR_Spm_Spr_avg.score += rsrAsrMetrics.getRSR_Spm_Spr_avg() * taskWeight;
                        }
                        if (calc_RSR_Spm_Spq_avg && rsrAsrMetrics.getRSR_Spm_Spq_avg() != null) {
                            //Update RSR_Spm_Spq_avg
                            RSR_Spm_Spq_avg.tasks_present.add(taskNum);
                            RSR_Spm_Spq_avg.score += rsrAsrMetrics.getRSR_Spm_Spq_avg() * taskWeight;
                        }
                        if (calc_RSR_alt_1 && rsrAsrMetrics.getRSR_alt_1_avg() != null) {
                            //Update RSR_alt_1
                            RSR_alt_1.tasks_present.add(taskNum);
                            RSR_alt_1.score += rsrAsrMetrics.getRSR_alt_1_avg() * taskWeight;
                        }
                        if (calc_RSR_alt_2 && rsrAsrMetrics.getRSR_alt_2_avg() != null) {
                            //Update RSR_alt_2
                            RSR_alt_2.tasks_present.add(taskNum);
                            RSR_alt_2.score += rsrAsrMetrics.getRSR_alt_2_avg() * taskWeight;
                        }
                    }
                    
                    //When using task weights, RMR is calculated at the overall task level and averaged over tasks
                    if( calc_RMR && metricsInfo.getRMR_info().isUseTaskWeights() != null
                            && metricsInfo.getRMR_info().isUseTaskWeights()
                            && mission.getRMR_avg() != null) {
                        //Update RMR
                        Double taskWeight = getTaskWeight(metricsInfo.getRMR_info(), taskNum);
                        RMR.tasks_present.add(taskNum);
                        RMR.score += mission.getRMR_avg() * taskWeight;
                    }

                    if (mission.getTrials() != null && !mission.getTrials().isEmpty()) {
                        for (TrialData trial : mission.getTrials()) {
                            if (trial != null && trial.getMetrics() != null) {
                                TrialMetrics trialMetrics = trial.getMetrics();                                
                                if (calc_AA) {
                                    //Update AA
                                    updateOverallCFACPAMetric(AA, trialMetrics.getAA_metrics(), 
                                            metricsInfo.getAA_info(), taskNum,
                                            metricsInfo.getAA_info().isUseTaskWeights() == null
                                            || !metricsInfo.getAA_info().isUseTaskWeights());                               
                                }                                
                                if (calc_PDE) {
                                    //Update PDE
                                    updateOverallCFACPAMetric(PDE, trialMetrics.getPDE_metrics(), 
                                            metricsInfo.getPDE_info(), taskNum,
                                            metricsInfo.getPDE_info().isUseTaskWeights() == null
                                            || !metricsInfo.getPDE_info().isUseTaskWeights());
                                }  
                                if (calc_RR) {
                                    //Update RR                                                                            
                                    if (trialMetrics.getRR_metrics() != null
                                            && !trialMetrics.getRR_metrics().isEmpty()) {
                                        for (CFAMetric rr : trialMetrics.getRR_metrics()) {
                                            updateOverallCFACPAMetric(RR, rr,
                                                    metricsInfo.getRR_info(), taskNum,
                                                    metricsInfo.getRR_info().isUseTaskWeights() == null
                                                    || !metricsInfo.getRR_info().isUseTaskWeights());
                                        }
                                    }
                                    /*updateOverallCFACPAMetric(RR, trialMetrics.getRR_metrics(),
                                     metricsInfo.getRR_info(), taskNum,
                                     metricsInfo.getRR_info().isUseTaskWeights() == null
                                     || !metricsInfo.getRR_info().isUseTaskWeights());*/
                                }
                                if (calc_AV) {
                                    //Update AV
                                    updateOverallCFACPAMetric(AV, trialMetrics.getAV_metrics(), 
                                            metricsInfo.getAV_info(), taskNum,
                                            metricsInfo.getAV_info().isUseTaskWeights() == null
                                            || !metricsInfo.getAV_info().isUseTaskWeights());
                                } 
                                if(calc_PM && trialMetrics.getPM_normativeBlueOptionSelections() != null) {
                                    //Update PM trials present                                    
                                    PM.trials_stages_present++;
                                }
                                if(calc_CS && trialMetrics.getCS_sigintAtHighestPaLocationSelections() != null) {
                                    //Update CS trials present
                                    CS.trials_stages_present++;
                                }                                
                                if(calc_SS && trialMetrics.getSS_percentTrialsReviewedInBatchPlot() != null) {
                                    //Update SS trials present
                                    SS.trials_stages_present++;
                                }                  
                                
                                RSRAndASRTrialMetrics rsrAsrTrialMetrics = trialMetrics.getRSRandASR_metrics();
                                if (calc_ASR && rsrAsrTrialMetrics != null) {
                                    //Update ASR
                                    if (rsrAsrTrialMetrics.getASR() != null
                                            && !rsrAsrTrialMetrics.getASR().isEmpty()) {
                                        for (CPAMetric asr : rsrAsrTrialMetrics.getASR()) {
                                            updateOverallCFACPAMetric(ASR, asr,
                                                    metricsInfo.getASR_info(), taskNum,
                                                    !metricsInfo.isUse_rsr_asr_task_weights());
                                        }
                                    }
                                }

                                //Update RSR and its variants on a trial-by-trial basis								
                                if (calc_RSR_Default && rsrAsrTrialMetrics != null) {
                                    //Update RSR (standard), Spm_avg, Spr_avg, and Spq_avg
                                    if (rsrAsrTrialMetrics.getRSR() != null 
                                            && !rsrAsrTrialMetrics.getRSR().isEmpty()) {
                                        for (int i = 0; i < rsrAsrTrialMetrics.getRSR().size(); i++) {
                                            //Update RSR
                                            CPAMetric rsr = rsrAsrTrialMetrics.getRSR().get(i);
                                            if (rsr != null) {
                                                if (!metricsInfo.isUse_rsr_asr_task_weights()) {
                                                    //Update RSR trial-by-trial average and trials present
                                                    updateOverallCFACPAMetric(RSR, rsr, metricsInfo.getRSR_info(), taskNum);
                                                }
                                                if (rsr.assessed != null && rsr.assessed) {
                                                    if (metricsInfo.isUse_rsr_asr_task_weights() && rsr.score != null) {
                                                        //Update trials present for RSR since it wasn't updated above
                                                        RSR.trials_stages_present++;
                                                    }

                                                    //Update Spm, Spr, and Spq and trials present for Spm, Spr, and Spq
                                                    if (i < (rsrAsrTrialMetrics.getSpm().size() - 1)) {
                                                        if (!metricsInfo.isUse_rsr_asr_task_weights()) {
                                                            Spm_avg += rsrAsrTrialMetrics.getSpm().get(i);
                                                        }
                                                        spm_trials_present++;
                                                    }
                                                    if (i < (rsrAsrTrialMetrics.getSpr().size() - 1)) {
                                                        if (!metricsInfo.isUse_rsr_asr_task_weights()) {
                                                            Spr_avg += rsrAsrTrialMetrics.getSpr().get(i);
                                                        }
                                                        spr_trials_present++;
                                                    }
                                                    if (i < (rsrAsrTrialMetrics.getSpq().size() - 1)) {
                                                        if (!metricsInfo.isUse_rsr_asr_task_weights()) {
                                                            Spq_avg += rsrAsrTrialMetrics.getSpq().get(i);
                                                        }
                                                        spq_trials_present++;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                if (calc_RSR_Bayesian && rsrAsrTrialMetrics != null) {
                                    //Update RSR_Bayesian
                                    if (rsrAsrTrialMetrics.getRSR_Bayesian() != null
                                            && !rsrAsrTrialMetrics.getRSR_Bayesian().isEmpty()) {
                                        for (CPAMetric rsr_bayesian : rsrAsrTrialMetrics.getRSR_Bayesian()) {
                                            if (rsr_bayesian != null) {
                                                updateOverallCFACPAMetric(RSR_Bayesian, rsr_bayesian,
                                                        metricsInfo.getRSR_Bayesian_info(), taskNum,
                                                        !metricsInfo.isUse_rsr_asr_task_weights());
                                            }
                                        }
                                    }
                                }

                                if (calc_RSR_alt_1 && rsrAsrTrialMetrics != null) {
                                    //Update RSR_alt_1
                                    if (rsrAsrTrialMetrics.getRSR_alt_1() != null
                                            && !rsrAsrTrialMetrics.getRSR_alt_1().isEmpty()) {
                                        for (CPAMetric rsr_alt_1 : rsrAsrTrialMetrics.getRSR_alt_1()) {
                                            if (rsr_alt_1 != null) {
                                                updateOverallCFACPAMetric(RSR_alt_1, rsr_alt_1,
                                                        metricsInfo.getRSR_alt_1_info(), taskNum,
                                                        !metricsInfo.isUse_rsr_asr_task_weights());
                                            }
                                        }
                                    }
                                }

                                if (calc_RSR_alt_2 && rsrAsrTrialMetrics != null) {
                                    //Update RSR_alt_2
                                    if (rsrAsrTrialMetrics.getRSR_alt_2() != null
                                            && !rsrAsrTrialMetrics.getRSR_alt_2().isEmpty()) {
                                        for (CPAMetric rsr_alt_2 : rsrAsrTrialMetrics.getRSR_alt_2()) {
                                            updateOverallCFACPAMetric(RSR_alt_2, rsr_alt_2,
                                                    metricsInfo.getRSR_alt_2_info(), taskNum,
                                                    !metricsInfo.isUse_rsr_asr_task_weights());
                                        }
                                    }
                                }                       

                                if (calc_RMR) {
                                    //Update RMR
                                    if (trialMetrics.getRMR_blueAction_metrics() != null) {
                                        updateOverallCFACPAMetric(RMR, trialMetrics.getRMR_blueAction_metrics(),
                                                metricsInfo.getRMR_info(), taskNum,
                                                metricsInfo.getRMR_info().isUseTaskWeights() == null
                                                || !metricsInfo.getRMR_info().isUseTaskWeights());
                                    }
                                    if (trialMetrics.getRMR_sigint_metrics() != null) {
                                        updateOverallCFACPAMetric(RMR, trialMetrics.getRMR_sigint_metrics(),
                                                metricsInfo.getRMR_info(), taskNum,
                                                metricsInfo.getRMR_info().isUseTaskWeights() == null
                                                || !metricsInfo.getRMR_info().isUseTaskWeights());
                                    }
                                }
                            }
                        }
                    }
                }
            }

            boolean useWeights;
            if (AA_stale) {
                //Compute overall AA score
                useWeights = metricsInfo.getAA_info().isUseTaskWeights() != null 
                        && metricsInfo.getAA_info().isUseTaskWeights();
                computeOverallCFACPAMetric(AA, metricsInfo.getAA_info(), 
                        useWeights ? null : AA.score);
                AA_stale = false;
            }
            if (PDE_stale) {
                //Compute overall PDE score
                useWeights = metricsInfo.getPDE_info().isUseTaskWeights() != null 
                        && metricsInfo.getPDE_info().isUseTaskWeights();
                computeOverallCFACPAMetric(PDE, metricsInfo.getPDE_info(), 
                        useWeights ? null : PDE.score);
                PDE_stale = false;
            }
            if (RR_stale) {
                //Compute overall RR score
                useWeights = metricsInfo.getRR_info().isUseTaskWeights() != null 
                        && metricsInfo.getRR_info().isUseTaskWeights();
                computeOverallCFACPAMetric(RR, metricsInfo.getRR_info(), 
                        useWeights ? null : RR.score);
                RR_stale = false;
            }
            if (AV_stale) {
                //Compute overall AV score
                useWeights = metricsInfo.getAV_info().isUseTaskWeights() != null 
                        && metricsInfo.getAV_info().isUseTaskWeights();
                computeOverallCFACPAMetric(AV, metricsInfo.getAV_info(), 
                        useWeights ? null : AV.score);
                AV_stale = false;
            }                 
            if (PM_stale) {
		//Compute overall PM score
                computeOverallCFACPAMetric(PM, metricsInfo.getPM_info(), null);                
                PM_stale = false;
            }
            if (CS_stale) {
		//Compute overall CS score
                computeOverallCFACPAMetric(CS, metricsInfo.getCS_info(), null);                
                CS_stale = false;
            }
            if (CB_stale) {
		//Compute overall CB score
                computeOverallCFACPAMetric(CB, metricsInfo.getCB_info(), null);                
                CB_stale = false;
            }
            if (SS_stale) {
		//Compute overall SS score
                computeOverallCFACPAMetric(SS, metricsInfo.getSS_info(), null);                
                SS_stale = false;
            }            

            useWeights = metricsInfo.isUse_rsr_asr_task_weights();
            if (ASR_stale) {
                //Compute overall ASR score
                computeOverallCFACPAMetric(ASR, metricsInfo.getASR_info(),
                        useWeights ? null : ASR.score);
                ASR_stale = false;
            }
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

            if (RMR_stale) {
                //Compute overall RMR score
                useWeights = metricsInfo.getRMR_info().isUseTaskWeights() != null 
                        && metricsInfo.getRMR_info().isUseTaskWeights();
                computeOverallCFACPAMetric(RMR, metricsInfo.getRMR_info(), 
                        useWeights ? null : RMR.score);
                RMR_stale = false;
            }

            examMetrics.setAA_stale(AA_stale);
            examMetrics.setPDE_stale(PDE_stale);
            examMetrics.setRR_stale(RR_stale);
            examMetrics.setAV_stale(AV_stale);
            examMetrics.setPM_stale(PM_stale);
            examMetrics.setCS_stale(CS_stale);
            examMetrics.setCB_stale(CB_stale);
            examMetrics.setSS_stale(SS_stale);
            examMetrics.setASR_stale(ASR_stale);
            examMetrics.setRSR_stale(RSR_stale);            
            examMetrics.setRMR_stale(RMR_stale);

            //Compute overall CFA score
            double cfa_credits_earned = 0d;
            double cfa_credits_possible = 0d;
            if (metricsInfo.getAA_info().isAssessed()) {
                cfa_credits_earned += AA.creditsEarned != null ? AA.creditsEarned : 0d;
                cfa_credits_possible++;
            }
            if (metricsInfo.getPDE_info().isAssessed()) {
                cfa_credits_earned += PDE.creditsEarned != null ? PDE.creditsEarned : 0d;
                cfa_credits_possible++;
            }
            if (metricsInfo.getRR_info().isAssessed()) {
                cfa_credits_earned += RR.creditsEarned != null ? RR.creditsEarned : 0d;
                cfa_credits_possible++;
            }         
            if (metricsInfo.getAV_info().isAssessed()) {
                cfa_credits_earned += AV.creditsEarned != null ? AV.creditsEarned : 0d;
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
            if (metricsInfo.getCB_info().isAssessed()) {
                cfa_credits_earned += CB.creditsEarned != null ? CB.creditsEarned : 0d;
                cfa_credits_possible++;
            }            
            if (metricsInfo.getSS_info().isAssessed()) {
                cfa_credits_earned += SS.creditsEarned != null ? SS.creditsEarned : 0d;
                cfa_credits_possible++;
            }
            examMetrics.setCFA_credits_earned(cfa_credits_earned);
            examMetrics.setCFA_credits_possible(cfa_credits_possible);
            double cfa_score = (cfa_credits_earned / cfa_credits_possible) * 100d;
            examMetrics.setCFA_score(cfa_score);
            examMetrics.setCFA_pass(cfa_score >= metricsInfo.getCfa_pass_threshold());

            //Compute overall CPA score
            double asr_score = (ASR != null && ASR.score != null) ? ASR.score : 0d;
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