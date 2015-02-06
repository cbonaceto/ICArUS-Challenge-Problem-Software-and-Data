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
package org.mitre.icarus.cps.assessment.data_model.phase_1;

import java.io.Serializable;
import java.util.ArrayList;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.mitre.icarus.cps.assessment.data_model.base.AbstractExamData;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.MetricsInfo;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.SubjectMetrics;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.TaskMetrics;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.TrialData;
import org.mitre.icarus.cps.assessment.data_model.phase_1.data_sets.AverageHumanDataSet_Phase1;
import org.mitre.icarus.cps.exam.phase_1.IcarusExam_Phase1;
import org.mitre.icarus.cps.exam.phase_1.testing.TaskTestPhase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_1_Phase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_2_Phase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_3_Phase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_4_Phase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_5_Phase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_6_Phase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_7_Phase;

/**
 * Data associated with a Phase 1 exam.
 *
 * @author cbonaceto
 *
 */
@Entity
public class ExamData extends AbstractExamData<AverageHumanDataSet_Phase1, TrialData, TaskMetrics, SubjectMetrics, MetricsInfo> implements Serializable {

    private static final long serialVersionUID = -4394718908032403418L;

    public ExamData() {
    }

    /**
     * Initialize fields using the given exam.
     *
     * @param exam
     */
    public ExamData(IcarusExam_Phase1 exam, MetricsInfo metricsInfo) {
        super(exam != null ? exam.getId() : null, exam != null ? exam.getName() : null);
        if (exam != null) {
            if (exam.getTasks() != null && !exam.getTasks().isEmpty()) {
                num_tasks = exam.getTasks().size();
                task_names = new ArrayList<String>(num_tasks);
                task_ids = new ArrayList<String>(num_tasks);
                task_numbers = new ArrayList<Integer>(num_tasks);
                for (TaskTestPhase<?> phase : exam.getTasks()) {
                    task_names.add(phase.getName());
                    task_ids.add(phase.getId());
                    task_numbers.add(getTask_number(phase));
                }
            }
        }
        this.metricsInfo = metricsInfo;
    }

    /**
     * @param phase
     * @return
     */
    public static Integer getTask_number(TaskTestPhase<?> phase) {
        if (phase instanceof Task_1_Phase) {
            return 1;
        } else if (phase instanceof Task_2_Phase) {
            return 2;
        } else if (phase instanceof Task_3_Phase) {
            return 3;
        } else if (phase instanceof Task_4_Phase) {
            return 4;
        } else if (phase instanceof Task_6_Phase) {
            return 6;
        } else if (phase instanceof Task_5_Phase) {
            return 5;
        } else if (phase instanceof Task_7_Phase) {
            return 7;
        }
        return null;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @Override
    public MetricsInfo getMetricsInfo() {
        return metricsInfo;
    }

    @Override
    public void setMetricsInfo(MetricsInfo metricsInfo) {
        this.metricsInfo = metricsInfo;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @Override
    public AverageHumanDataSet_Phase1 getAvgHumanDataSet() {
        return avgHumanDataSet;
    }

    @Override
    public void setAvgHumanDataSet(AverageHumanDataSet_Phase1 avgHumanDataSet) {
        this.avgHumanDataSet = avgHumanDataSet;
    }
}
