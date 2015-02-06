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
package org.mitre.icarus.cps.assessment.model_simulator.phase_1;

import org.mitre.icarus.cps.exam.phase_1.IcarusExam_Phase1;
import org.mitre.icarus.cps.exam.phase_1.testing.TaskTestPhase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_1_2_3_PhaseBase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_1_2_3_TrialBlockBase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_1_ProbeTrial;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_1_TrialBlock;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_2_ProbeTrial;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_2_TrialBlock;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_3_ProbeTrial;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_3_TrialBlock;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_4_Phase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_4_Trial;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_5_Phase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_5_Trial;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_6_Phase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_6_Trial;

/**
 * Base class for Phase 1 models.
 *
 * @author CBONACETO
 *
 */
public abstract class AbstractModel_Phase1 extends org.mitre.icarus.cps.assessment.model_simulator.Model<IcarusExam_Phase1> {

    /* (non-Javadoc)
     * @see org.mitre.icarus.cpa.phase_1.model_simulator.Model#generateExamResponses(org.mitre.icarus.cps.exam.phase_1.IcarusExam_Phase1)
     */
    @Override
    public IcarusExam_Phase1 generateExamResponses(IcarusExam_Phase1 exam) {
        if (exam.getTasks() != null && !exam.getTasks().isEmpty()) {
            initializeExam(exam);
            for (TaskTestPhase<?> task : exam.getTasks()) {
                if (task instanceof Task_1_2_3_PhaseBase) {
                    Task_1_2_3_PhaseBase<?> task123 = (Task_1_2_3_PhaseBase<?>) task;
                    if (task123.getTrialBlocks() != null && !task123.getTrialBlocks().isEmpty()) {
                        for (Task_1_2_3_TrialBlockBase trialBlock : task123.getTrialBlocks()) {
                            if (trialBlock instanceof Task_1_TrialBlock) {
                                processTask_1_Trial(((Task_1_TrialBlock) trialBlock).getProbeTrial());
                            } else if (trialBlock instanceof Task_2_TrialBlock) {
                                processTask_2_Trial(((Task_2_TrialBlock) trialBlock).getProbeTrial());
                            } else if (trialBlock instanceof Task_3_TrialBlock) {
                                processTask_3_Trial(((Task_3_TrialBlock) trialBlock).getProbeTrial());
                            }
                        }
                    }
                } else if (task instanceof Task_4_Phase) {
                    Task_4_Phase task4 = (Task_4_Phase) task;
                    if (task4.getTestTrials() != null && !task4.getTestTrials().isEmpty()) {
                        for (Task_4_Trial trial : task4.getTestTrials()) {
                            processTask_4_Trial(trial);
                        }
                    }
                } else if (task instanceof Task_6_Phase) {
                    Task_6_Phase task6 = (Task_6_Phase) task;
                    if (task6.getTestTrials() != null && !task6.getTestTrials().isEmpty()) {
                        for (Task_6_Trial trial : task6.getTestTrials()) {
                            processTask_5_6_Trial(trial);
                        }
                    }
                } else if (task instanceof Task_5_Phase) {
                    Task_5_Phase task5 = (Task_5_Phase) task;
                    if (task5.getTestTrials() != null && !task5.getTestTrials().isEmpty()) {
                        for (Task_5_Trial trial : task5.getTestTrials()) {
                            processTask_5_6_Trial(trial);
                        }
                    }
                }
            }
        }
        return exam;
    }

    /**
     * @param exam
     */
    protected abstract void initializeExam(IcarusExam_Phase1 exam);

    /**
     * @param trial
     */
    protected abstract void processTask_1_Trial(Task_1_ProbeTrial trial);

    /**
     * @param trial
     */
    protected abstract void processTask_2_Trial(Task_2_ProbeTrial trial);

    /**
     * @param trial
     */
    protected abstract void processTask_3_Trial(Task_3_ProbeTrial trial);

    /**
     * @param trial
     */
    protected abstract void processTask_4_Trial(Task_4_Trial trial);

    /**
     * @param trial
     */
    protected abstract void processTask_5_6_Trial(Task_5_Trial trial);
}
