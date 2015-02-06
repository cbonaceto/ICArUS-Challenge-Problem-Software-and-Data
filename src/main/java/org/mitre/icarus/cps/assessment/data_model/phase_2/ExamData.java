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
package org.mitre.icarus.cps.assessment.data_model.phase_2;

import java.io.Serializable;
import java.util.ArrayList;
import org.mitre.icarus.cps.assessment.data_model.base.AbstractExamData;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.MetricsInfo;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.MissionMetrics;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.SubjectMetrics;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.TrialData;
import org.mitre.icarus.cps.assessment.data_model.phase_2.data_sets.AverageHumanDataSet_Phase2;
import org.mitre.icarus.cps.exam.phase_2.IcarusExam_Phase2;
import org.mitre.icarus.cps.exam.phase_2.testing.Mission;

/**
 *
 * Data associated with a Phase 2 exam.
 * 
 * @author CBONACETO
 */
public class ExamData extends AbstractExamData<AverageHumanDataSet_Phase2, TrialData, MissionMetrics, SubjectMetrics, MetricsInfo> implements Serializable {

    private static final long serialVersionUID = -4394718908032403418L;

    public ExamData() {
    }

    /**
     * Initialize fields using the given exam.
     *
     * @param exam
     * @param metricsInfo
     */
    public ExamData(IcarusExam_Phase2 exam, MetricsInfo metricsInfo) {        
        super(exam != null ? exam.getId() : null, exam != null ? exam.getName() : null);
        if(exam != null) {
            if(exam.getMissions() != null && !exam.getMissions().isEmpty()) {
                num_tasks = exam.getMissions().size();
                task_names = new ArrayList<String>(num_tasks);
                task_ids = new ArrayList<String>(num_tasks);
                task_numbers = new ArrayList<Integer>(num_tasks);
                for(Mission<?> mission : exam.getMissions()) {
                    task_names.add(mission.getName());
                    task_ids.add(mission.getId());
                    task_numbers.add(getTask_number(mission));
                }
            }
        }
        this.metricsInfo = metricsInfo;
    }

    /**
     * @param mission
     * @return
     */
    public static Integer getTask_number(Mission<?> mission) {
        if(mission != null && mission.getMissionType() != null) {
            return mission.getMissionType().getMissionNum();
        }
        return null;
    }
}