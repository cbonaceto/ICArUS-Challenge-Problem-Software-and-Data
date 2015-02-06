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

import java.util.LinkedList;
import java.util.List;

import org.mitre.icarus.cps.assessment.assessment_metrics_computer.ISubjectMetricsComputer;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.MetricsInfo;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.SubjectMetrics;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.TaskMetrics;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.TrialData;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.TrialMetrics;

/**
 * Computes Phase 1 subject metrics.
 * 
 * @author cbonaceto
 *
 */
public class SubjectMetricsComputer extends MetricsComputer implements
        ISubjectMetricsComputer<TrialData, TaskMetrics, SubjectMetrics, MetricsInfo> {

    /* (non-Javadoc)
     * @see org.mitre.icarus.cpa.metrics_computer.ISubjectMetricsComputer#updateSubjectMetrics_Tasks(org.mitre.icarus.cpa.data_model.AbstractSubjectMetrics, java.util.List, org.mitre.icarus.cpa.data_model.cfa_cpa_metrics.AbstractMetricsInfo, org.mitre.icarus.cpa.data_model.AbstractSubjectMetrics)
     */
    @Override
    public SubjectMetrics updateSubjectMetrics_Tasks(
            SubjectMetrics subjectMetrics, List<TaskMetrics> tasks,
            MetricsInfo metricsInfo, SubjectMetrics comparisonMetrics) {
        return updateSubjectMetrics_Tasks(subjectMetrics, tasks, metricsInfo, comparisonMetrics, true, true);
    }

    /**
     * @param subjectMetrics
     * @param tasks
     * @param metricsInfo
     * @param comparisonMetrics
     * @param pm_f_stale
     * @param cs_stale
     * @return
     */
    public SubjectMetrics updateSubjectMetrics_Tasks(SubjectMetrics subjectMetrics, List<TaskMetrics> tasks,
            MetricsInfo metricsInfo, SubjectMetrics comparisonMetrics, boolean pm_f_stale, boolean cs_stale) {
        List<TrialData> trials = null;
        if (tasks != null && !tasks.isEmpty()) {
            trials = new LinkedList<TrialData>();
            for (TaskMetrics task : tasks) {
                trials.addAll(task.getTrials());
            }
        }
        return updateSubjectMetrics_Trials(subjectMetrics, trials, metricsInfo, comparisonMetrics, pm_f_stale, cs_stale);
    }

    /* (non-Javadoc)
     * @see org.mitre.icarus.cpa.metrics_computer.ISubjectMetricsComputer#updateSubjectMetrics_Trials(org.mitre.icarus.cpa.data_model.AbstractSubjectMetrics, java.util.List, org.mitre.icarus.cpa.data_model.cfa_cpa_metrics.AbstractMetricsInfo, org.mitre.icarus.cpa.data_model.AbstractSubjectMetrics)
     */
    @Override
    public SubjectMetrics updateSubjectMetrics_Trials(SubjectMetrics subjectMetrics, List<TrialData> trials,
            MetricsInfo metricsInfo, SubjectMetrics comparisonMetrics) {
        return updateSubjectMetrics_Trials(subjectMetrics, trials, metricsInfo, comparisonMetrics, true, true);
    }

    /**
     * Compute PM_F and CS across trials in multiple tasks for which they are
     * computed (PM_F: Tasks 1-3, CS: Just Task 6).
     *
     * @param subjectMetrics
     * @param trials
     * @param metricsInfo
     * @param comparisonMetrics
     * @param pm_f_stale
     * @param cs_stale
     * @return
     */
    public SubjectMetrics updateSubjectMetrics_Trials(SubjectMetrics subjectMetrics, List<TrialData> trials,
            MetricsInfo metricsInfo, SubjectMetrics comparisonMetrics, boolean pm_f_stale, boolean cs_stale) {
        if (subjectMetrics == null) {
            subjectMetrics = new SubjectMetrics();
        }
        if (metricsInfo == null) {
            metricsInfo = MetricsInfo.createDefaultMetricsInfo();
        }

        subjectMetrics.setNum_subjects(1);
        subjectMetrics.setMetrics_stale(true);

        if (trials != null && !trials.isEmpty()) {
            Boolean PM_F_stale = pm_f_stale && metricsInfo.getPM_F_info().isCalculated();
            Boolean CS_stale = cs_stale && metricsInfo.getCS_info().isCalculated();

            Double Fh_all_trials = null; //Fh over all trials for which PM_F is computed
            Integer Fh_count = null;
            Double Ph_all_trials = null; //Ph over all trials for which PM_F is computed
            Integer Ph_count = null;
            if (PM_F_stale) {
                Fh_all_trials = 0d;
                Fh_count = 0;
                Ph_all_trials = 0d;
                Ph_count = 0;
            }

            Integer sigint_selections_all_trials = null; //Number of times SIGINT was selected over all trials
            Integer sigint_highest_group_selections_all_trials = null; //Number of times SIGINT was selected on the group with the highest prob over all trials
            //Integer sigint_highest_best_selections_all_trials = null;
            Integer C_num_subjects = null;
            if (CS_stale) {
                sigint_selections_all_trials = 0;
                sigint_highest_group_selections_all_trials = 0;
				//sigint_highest_best_selections_all_trials = 0;
                //C_num_subjects = 0;
            }

            for (TrialData trial : trials) {
                TrialMetrics trialMetrics = (trial != null) ? trial.getMetrics() : null;
                if (trial != null && trialMetrics != null) {
                    Integer taskNum = trial.getTask_number();
                    Integer trialNum = trial.getTrial_number();

                    if (PM_F_stale && metricsInfo.getPM_F_info().isAssessedForStage(taskNum, trialNum, 0)) {
                        //Update Fh and Ph (probability matching for Tasks 1-3)						
                        if (trialMetrics.getFh() != null) {
                            Fh_all_trials += trialMetrics.getFh();
                            Fh_count++;
                        }
                        if (trialMetrics.getPh() != null) {
                            Ph_all_trials += trialMetrics.getPh();
                            Ph_count++;
                        }
                    }

                    if (CS_stale && metricsInfo.getCS_info().isAssessedForStage(taskNum, trialNum, 0)) {
                        //Update sigint selection metrics (confirmation bias for Task 6)
                        sigint_selections_all_trials
                                += trialMetrics.getSigint_selections() != null ? trialMetrics.getSigint_selections() : 0;
                        sigint_highest_group_selections_all_trials
                                += trialMetrics.getSigint_highest_group_selections() != null ? trialMetrics.getSigint_highest_group_selections() : 0;
                    }
                }
            }

            //Update Fh_Ph, Fh_1, and PM (probability matching for Tasks 1-3)
            if (PM_F_stale) {
                Fh_all_trials = Fh_all_trials / Fh_count;
                Ph_all_trials = Ph_all_trials / Ph_count;
                subjectMetrics.setFh_all_trials(Fh_all_trials);
                subjectMetrics.setPh_all_trials(Ph_all_trials);
                subjectMetrics.setFh_Ph_all_trials(Fh_all_trials - Ph_all_trials);
                subjectMetrics.setFh_1_all_trials(Fh_all_trials - 1);
                subjectMetrics.setPM_F(cfaMetricsComputer.computePM_F(subjectMetrics.getFh_Ph_all_trials(),
                        subjectMetrics.getFh_1_all_trials(), subjectMetrics.getPM_F(),
                        comparisonMetrics != null ? comparisonMetrics.getPM_F() : null,
                        metricsInfo.getPM_F_info()));
            } else {
                subjectMetrics.setFh_all_trials(null);
                subjectMetrics.setPh_all_trials(null);
                subjectMetrics.setFh_Ph_all_trials(null);
                subjectMetrics.setFh_1_all_trials(null);
                subjectMetrics.setPM_F(null);
            }

            //Update CS (confirmation bias for Task 6)
            if (CS_stale) {
                subjectMetrics.setSigint_selections_all_trials(sigint_selections_all_trials);
                subjectMetrics.setSigint_highest_group_selections_all_trials(sigint_highest_group_selections_all_trials);
                subjectMetrics.setC_num_subjects(C_num_subjects);
                subjectMetrics.setC_all_trials(
                        ((double) sigint_highest_group_selections_all_trials / sigint_selections_all_trials) * 100d);
                subjectMetrics.setCS(cfaMetricsComputer.computeCS(subjectMetrics.getC_all_trials(), metricsInfo.getC_threshold(), subjectMetrics.getCS(),
                        comparisonMetrics != null ? comparisonMetrics.getCS() : null,
                        metricsInfo.getCS_info()));
            } else {
                subjectMetrics.setSigint_selections_all_trials(null);
                subjectMetrics.setSigint_highest_group_selections_all_trials(null);
                subjectMetrics.setC_num_subjects(null);
                subjectMetrics.setC_all_trials(null);
                subjectMetrics.setCS(null);
            }
        }

        subjectMetrics.setMetrics_stale(false);

        return subjectMetrics;
    }

    /**
     * @param subjectMetrics
     * @param averageMetrics
     * @param comparisonMetrics
     * @param metricsInfo
     * @return
     */
    @Override
    public SubjectMetrics updateAverageSubjectMetrics(List<SubjectMetrics> subjectMetrics, SubjectMetrics averageMetrics,
            SubjectMetrics comparisonMetrics, MetricsInfo metricsInfo) {
        if (averageMetrics == null) {
            averageMetrics = new SubjectMetrics();
        }

        averageMetrics.setMetrics_stale(true);

        if (subjectMetrics != null && !subjectMetrics.isEmpty()) {
            int numSubjects = subjectMetrics.size();
            averageMetrics.setNum_subjects(numSubjects);

            //****************** Compute Means ******************/
            Double Fh_all_trials = 0d;
            Double Ph_all_trials = 0d;

            Integer sigint_selections_all_trials = 0;
            Integer sigint_highest_group_selections_all_trials = 0;
            Integer C_num_subjects = 0;

            for (SubjectMetrics currMetrics : subjectMetrics) {
                Fh_all_trials += currMetrics.getFh_all_trials() != null ? currMetrics.getFh_all_trials() : 0d;
                Ph_all_trials += currMetrics.getPh_all_trials() != null ? currMetrics.getPh_all_trials() : 0d;

                if (currMetrics.getSigint_selections_all_trials() != null && currMetrics.getSigint_selections_all_trials() > 0) {
                    sigint_selections_all_trials += currMetrics.getSigint_selections_all_trials();
                    sigint_highest_group_selections_all_trials
                            += currMetrics.getSigint_highest_group_selections_all_trials() != null ? currMetrics.getSigint_highest_group_selections_all_trials() : 0;
                    C_num_subjects++;
                }
            }
            Fh_all_trials = Fh_all_trials / numSubjects;
            Ph_all_trials = Ph_all_trials / numSubjects;
            averageMetrics.setFh_all_trials(Fh_all_trials);
            averageMetrics.setPh_all_trials(Ph_all_trials);
            averageMetrics.setFh_Ph_all_trials(Fh_all_trials - Ph_all_trials);
            averageMetrics.setFh_1_all_trials(Fh_all_trials - 1);

            averageMetrics.setSigint_selections_all_trials(sigint_selections_all_trials);
            averageMetrics.setSigint_highest_group_selections_all_trials(sigint_highest_group_selections_all_trials);
            averageMetrics.setC_num_subjects(C_num_subjects);
            averageMetrics.setC_all_trials(sigint_selections_all_trials > 0
                    ? ((double) sigint_highest_group_selections_all_trials / sigint_selections_all_trials) * 100d : 0d);

			//****************** Compute Standard Deviations ******************/
            //TODO: Compute STD
        }

        //Compute PM_F and CS
        averageMetrics.setPM_F(cfaMetricsComputer.computePM_F(averageMetrics.getFh_Ph_all_trials(),
                averageMetrics.getFh_1_all_trials(), averageMetrics.getPM_F(),
                comparisonMetrics != null ? comparisonMetrics.getPM_F() : null,
                metricsInfo.getPM_F_info()));
        averageMetrics.setCS(cfaMetricsComputer.computeCS(averageMetrics.getC_all_trials(), metricsInfo.getC_threshold(), averageMetrics.getCS(),
                comparisonMetrics != null ? comparisonMetrics.getCS() : null,
                metricsInfo.getCS_info()));

        averageMetrics.setMetrics_stale(false);

        return averageMetrics;
    }
}
