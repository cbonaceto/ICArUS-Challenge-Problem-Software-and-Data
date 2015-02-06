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
package org.mitre.icarus.cps.assessment.assessment_metrics_computer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.Metric;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.MetricInfo;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.OverallCFACPAMetric;

/**
 * Contains static methods for use in exam metrics computer implementations.
 * 
 * @author CBONACETO
 */
public class ExamMetricsComputerUtils {
    
    private ExamMetricsComputerUtils() {}
    
    /**
     * @param metric
     * @param metricInfo
     * @return
     */
    public static OverallCFACPAMetric initializeOverallCFACPAMetric(OverallCFACPAMetric metric, 
            MetricInfo metricInfo) {
        if (metric == null) {
            metric = new OverallCFACPAMetric(0D, new HashSet<Integer>(), 0);
            metric.setName(metricInfo.getName());
        } else {
            metric.trials_stages_present = 0;
            metric.score = 0D;
            metric.pass = false;
            metric.tasks_present = new HashSet<Integer>();
            metric.tasks_missing = null;
        }
        return metric;
    }

    /**
     * @param overallMetric
     * @param stageMetric
     * @param metricInfo
     * @param taskNum
     */
    public static void updateOverallCFACPAMetric(OverallCFACPAMetric overallMetric, 
            Metric stageMetric, MetricInfo metricInfo, int taskNum) {
        /*if(stageMetric != null) {
         System.out.println(stageMetric.getName() + ", " + stageMetric.assessed);
         } else {
         System.out.println("stage metric null for " + overallMetric.getName());
         }*/
        updateOverallCFACPAMetric(overallMetric, stageMetric, metricInfo, taskNum, true);
    }
    
    /**
     *
     * @param overallMetric
     * @param stageMetric
     * @param metricInfo
     * @param taskNum
     * @param updateScore
     */
    public static void updateOverallCFACPAMetric(OverallCFACPAMetric overallMetric,
            Metric stageMetric, MetricInfo metricInfo, int taskNum, boolean updateScore) {
        if (stageMetric != null && stageMetric.assessed != null && stageMetric.assessed && stageMetric.score != null) {
            overallMetric.trials_stages_present++;
            overallMetric.tasks_present.add(taskNum);
            if (updateScore) {
                overallMetric.score += stageMetric.score;
            }
        }
    }

    /**
     * @param overallMetric
     * @param metricInfo
     */
    public static void computeOverallCFACPAMetric(OverallCFACPAMetric overallMetric,
            MetricInfo metricInfo) {
        computeOverallCFACPAMetric(overallMetric, metricInfo, null);
    }

    /**
     * @param overallMetric
     * @param scoreSum
     * @param metricInfo
     */
    public static void computeOverallCFACPAMetric(OverallCFACPAMetric overallMetric, 
            MetricInfo metricInfo, Double scoreSum) {
        computeOverallCFACPAMetric(overallMetric, metricInfo, scoreSum, 
                overallMetric.trials_stages_present);
        /*if (score_sum != null) {
            //Compute average score
            if (overallMetric.trials_stages_present > 0) {
                overallMetric.score = score_sum / overallMetric.trials_stages_present;
            } else {
                overallMetric.score = 0D;
            }
        }
        overallMetric.pass = overallMetric.score != null && overallMetric.score >= metricInfo.getOverall_pass_threshold();
        overallMetric.creditsEarned = overallMetric.pass ? 1d : 0d;
        overallMetric.tasks_missing = getTasksMissing(overallMetric.tasks_present, metricInfo.getTasks());
        overallMetric.trials_stages_missing = metricInfo.getNum_trials_stages() - overallMetric.trials_stages_present;*/
    }
    
    /**
     *
     * @param overallMetric
     * @param metricInfo     *
     * @param scoreSum
     * @param scoreCount
     */
    public static void computeOverallCFACPAMetric(OverallCFACPAMetric overallMetric,
            MetricInfo metricInfo, Double scoreSum, int scoreCount) {
        if (scoreSum != null) {
            //Compute average score
            if (scoreCount > 0) {
                overallMetric.score = scoreSum / scoreCount;
            } else {
                overallMetric.score = 0D;
            }
        }
        overallMetric.pass = overallMetric.score != null
                && overallMetric.score >= metricInfo.getOverall_pass_threshold();
        overallMetric.creditsEarned = overallMetric.pass ? 1d : 0d;
        overallMetric.tasks_missing = getTasksMissing(overallMetric.tasks_present, metricInfo.getTasks());
        if (metricInfo.getNum_trials_stages() != null && overallMetric.trials_stages_present != null) {
            overallMetric.trials_stages_missing = Math.max(0,
                    metricInfo.getNum_trials_stages() - overallMetric.trials_stages_present);
        } else {
            overallMetric.trials_stages_missing = null;
        }
    }

    /**
     * @param tasksPresent
     * @param tasksExpected
     * @return
     */
    public static Set<Integer> getTasksMissing(Set<Integer> tasksPresent, Set<Integer> tasksExpected) {
        Set<Integer> tasksMissing = null;
        if (tasksExpected != null && !tasksExpected.isEmpty()) {
            for (Integer taskExpected : tasksExpected) {
                if (tasksPresent == null || !tasksPresent.contains(taskExpected)) {
                    if (tasksMissing == null) {
                        tasksMissing = new HashSet<Integer>();
                    }
                    tasksMissing.add(taskExpected);
                }
            }
        }
        return tasksMissing;
    }
    
    /**
     *
     * @param metricInfo
     * @param taskNum
     * @return
     */
    public static double getTaskWeight(MetricInfo metricInfo, int taskNum) {
        Double weight = null;
        if (metricInfo != null) {  
            List<Double> taskWeights = metricInfo.getTaskWeights();
            if (taskWeights != null && (taskNum - 1) < taskWeights.size()) {
                weight = taskWeights.get(taskNum - 1);
            } else if(metricInfo.getTasks() != null &&
                    !metricInfo.getTasks().isEmpty()) {
                weight = 1.d / metricInfo.getTasks().size();
            }
        }
        return weight == null ? 1.d : weight;
    }

    /*
     public static double getTaskWeight(List<Double> taskWeights, int taskNum) {
        Double weight = null;
        if(taskWeights != null && (taskNum - 1) < taskWeights.size()) {
            weight = taskWeights.get(taskNum - 1);
        } 
        return weight == null ? 1.d : weight;
    }
    */
}
