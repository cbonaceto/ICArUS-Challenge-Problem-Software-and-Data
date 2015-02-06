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
package org.mitre.icarus.cps.assessment.score_computer.phase_1;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;


import org.mitre.icarus.cps.app.experiment.IcarusSubjectData;
import org.mitre.icarus.cps.assessment.data_aggregator.DataAggregatorUtils;
import org.mitre.icarus.cps.exam.phase_1.IcarusExam_Phase1;
import org.mitre.icarus.cps.exam.phase_1.loader.IcarusExamLoader_Phase1;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.Task_5_6_AttackLocationProbeResponseAfterINT;
import org.mitre.icarus.cps.exam.phase_1.testing.TaskTestPhase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_1_Phase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_1_TrialBlock;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_2_Phase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_2_TrialBlock;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_3_Phase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_3_TrialBlock;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_4_Phase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_4_Trial;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_5_Phase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_5_Trial;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_6_Phase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_6_Trial;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_7_Phase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_7_Trial;
import org.mitre.icarus.cps.exam.phase_1.testing.probability_rules.ProbabilityRules;
import org.mitre.icarus.cps.feature_vector.phase_1.GridSize;

/**
 *
 *
 * @author CBONACETO
 *
 */
public class BatchScoreComputer {

    private static final ScoreComputer scoreComputer = new ScoreComputer();

    /**
     * Strips normative solution data from task files.
     */
    public static void sanitizeTasks(String examId, Collection<IcarusSubjectData> subjects, Collection<Integer> taskNumbers,
            URL inputDirectory, URL outputDirectory, boolean useSubjectFolders) {
        for (Integer taskNumber : taskNumbers) {
            for (IcarusSubjectData subject : subjects) {
                //Load the task file for the current subject and task
                URL subjectInputDirectory = inputDirectory;
                URL subjectOutputDirectory = outputDirectory;
                if (useSubjectFolders) {
                    try {
                        String folderName = null;
                        if (subject.getSite() != null && subject.getSite().getTag() != null) {
                            folderName = "S_" + subject.getSite().getTag() + "_" + subject.getSubjectId();
                        } else {
                            folderName = "S" + subject.getSubjectId();
                        }
                        subjectInputDirectory = new URL(inputDirectory.toString() + "/" + folderName + "/");
                        subjectOutputDirectory = new URL(outputDirectory.toString() + "/" + folderName + "/");
                        File folder = new File(subjectOutputDirectory.toURI());
                        if (!folder.exists()) {
                            folder.mkdir();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                TaskTestPhase<?> task = null;
                String fileName = null;
                if (subject.getSite() != null && subject.getSite().getTag() != null) {
                    fileName = "S_" + subject.getSite().getTag() + "_" + subject.getSubjectId() + "_" + examId + "_Task" + taskNumber.toString() + ".xml";
                } else {
                    fileName = "S" + subject.getSubjectId() + "_" + examId + "_Task" + taskNumber.toString() + ".xml";
                }
                try {
                    task = IcarusExamLoader_Phase1.unmarshalTask(new URL(subjectInputDirectory, fileName), false);
                } catch (Exception ex) {
                    System.err.println("Error reading file: " + fileName);
                    ex.printStackTrace();
                }

                if (task != null) {
                    //Sanitize the task
                    scoreComputer.sanitizeTask(task);

                    //Write the sanitized task back to the output directory
                    try {
                        String xml = IcarusExamLoader_Phase1.marshalTask(task);
                        BufferedWriter out = new BufferedWriter(new FileWriter(new File(
                                URLDecoder.decode(new URL(subjectOutputDirectory, fileName).getFile(), "UTF-8"))));
                        out.write(xml);
                        out.close();
                    } catch (Exception ex) {
                        System.err.println("Error writing file: " + fileName);
                        ex.printStackTrace();
                    }
                }
            }
        }
        System.out.println("Sanitization Complete");
    }

    /**
     * @param examFile
     * @param subjects
     * @param taskNumbers
     * @param inputDirectory
     * @param outputDirectory
     * @param outputProgress
     */
    public static void computeScores(URL examFile, Collection<IcarusSubjectData> subjects, Collection<Integer> taskNumbers,
            URL inputDirectory, URL outputDirectory, boolean useSubjectFolders, boolean outputProgress) {
        if (examFile != null) {
            try {
                IcarusExam_Phase1 exam = IcarusExamLoader_Phase1.unmarshalExam(examFile);
                exam.setOriginalPath(examFile);
                for (int taskNum : taskNumbers) {
                    TaskTestPhase<?> task = exam.getTasks().get(taskNum);
                    IcarusExamLoader_Phase1.initializeTaskFeatureVectorData(task,
                            examFile, exam.getGridSize(),
                            false, null);
                }
                computeScores(exam, examFile, subjects, taskNumbers,
                        inputDirectory, outputDirectory, useSubjectFolders, outputProgress);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * @param exam
     * @param examFileFolder
     * @param subjects
     * @param taskNumbers
     * @param inputDirectory
     * @param outputDirectory
     * @param outputProgress
     */
    public static void computeScores(IcarusExam_Phase1 exam, URL examFileFolder,
            Collection<IcarusSubjectData> subjects, Collection<Integer> taskNumbers,
            URL inputDirectory, URL outputDirectory,
            boolean useSubjectFolders, boolean outputProgress) {
        //Create default probability rules
        ProbabilityRules rules = ProbabilityRules.createDefaultProbabilityRules();

        //Get the grid size and exam name from the exam
        GridSize gridSize = exam.getGridSize();
        String examId = exam.getId();
        String examName = exam.getName();

        for (Integer taskNumber : taskNumbers) {
            if (taskNumber < 8) {
                for (IcarusSubjectData subject : subjects) {
                    //Load the task file for the current subject and task
                    URL subjectInputDirectory = inputDirectory;
                    URL subjectOutputDirectory = outputDirectory;
                    if (useSubjectFolders) {
                        try {
                            String folderName = null;
                            if (subject.getSite() != null && subject.getSite().getTag() != null) {
                                folderName = "S_" + subject.getSite().getTag() + "_" + subject.getSubjectId();
                            } else {
                                folderName = "S" + subject.getSubjectId();
                            }
                            subjectInputDirectory = new URL(inputDirectory.toString() + "/" + folderName + "/");
                            subjectOutputDirectory = new URL(outputDirectory.toString() + "/" + folderName + "/");
                            File folder = new File(subjectOutputDirectory.toURI());
                            if (!folder.exists()) {
                                folder.mkdir();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    TaskTestPhase<?> task = null;
                    String fileName = null;
                    //System.out.println(subject.getSite().getShortName());
                    if (subject.getSite() != null && subject.getSite().getTag() != null) {
                        fileName = "S_" + subject.getSite().getTag() + "_" + subject.getSubjectId() + "_" + examId + "_Task" + taskNumber.toString() + ".xml";
						//if(!new File(fileName).exists()) {
                        //	fileName = "S_" + subject.getSite().getShortName() + "_" + subject.getSubjectId() + "_" + examName + "_Task" + taskNumber.toString() + ".xml";
                        //}
                    } else {
                        fileName = "S" + subject.getSubjectId() + "_" + examId + "_Task" + taskNumber.toString() + ".xml";
                        if (!new File(fileName).exists()) {
                            fileName = "S" + subject.getSubjectId() + "_" + examName + "_Task" + taskNumber.toString() + ".xml";
                        }
                    }
                    if (outputProgress) {
                        System.out.print("Scoring " + fileName + "...");
                    }
                    try {
                        //System.err.println(subjectInputDirectory);
                        task = IcarusExamLoader_Phase1.unmarshalTask(new URL(subjectInputDirectory, fileName), false);
                        IcarusExamLoader_Phase1.initializeTaskFeatureVectorData(task, examFileFolder, gridSize,
                                false, null);
                    } catch (Exception ex) {
                        System.err.println("Error reading file: " + fileName);
                        ex.printStackTrace();
                    }

                    boolean complete = false;
                    if (task != null) {
						//Add subject responses to tasks in the "gold standard" exam so the data used to calculate parameters
                        //comes from the gold standard exam
                        TaskTestPhase<?> taskToScore = task;
                        try {
                            taskToScore = addSubjectResponsesToExamTask(exam, task);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        //Make sure user selected flag set to true for all INT layer selections in task 6
                        if (taskToScore instanceof Task_6_Phase) {
                            for (Task_6_Trial trial : ((Task_6_Phase) taskToScore).getTestTrials()) {
                                if (trial.getTrialResponse() != null
                                        && trial.getTrialResponse().getAttackLocationResponses_afterINTs() != null) {
                                    for (Task_5_6_AttackLocationProbeResponseAfterINT intResponse
                                            : trial.getTrialResponse().getAttackLocationResponses_afterINTs()) {
                                        intResponse.getIntLayerShown().setUserSelected(true);
                                    }
                                }
                            }
                        }

						//Score the task
						/*if(taskToScore instanceof Task_4_Phase) {
                         for(Task_4_Trial trial : ((Task_4_Phase)taskToScore).getTestTrials()) {
                         scoreComputer.computeSolutionAndScoreForTrial(trial, rules, gridSize, true);
                         }
                         }*/
                        scoreComputer.computeSolutionAndScoreForTask(taskToScore, rules, gridSize, true, null);
                        /*TaskFeedback feedback = scoreComputer.computeSolutionAndScoreForTask(taskToScore, rules, gridSize, true, null);
                         System.out.println("task score: " + feedback.getProbabilitiesScore_s1());
                         for(TrialFeedback_Phase1 tf : feedback.getTrialFeedback()) {
                         System.out.println(tf.getProbabilitiesScore_s1());
                         }*/

                        //Write the scored task back to the output directory
                        try {
                            String xml = IcarusExamLoader_Phase1.marshalTask(taskToScore);
                            BufferedWriter out = new BufferedWriter(new FileWriter(new File(
                                    URLDecoder.decode(new URL(subjectOutputDirectory, fileName).getFile(), "UTF-8"))));
                            out.write(xml);
                            out.close();
                            complete = true;
                        } catch (Exception ex) {
                            System.err.println("Error writing file: " + fileName);
                            ex.printStackTrace();
                        }
                    }
                    if (outputProgress) {
                        if (complete) {
                            System.out.println("Complete");
                        } else {
                            System.out.println("Failed");
                        }
                    }
                }
            }
        }
        System.out.println("Scoring Complete");
    }

    /**
     * @param exam
     * @param subjectTask
     * @return
     */
    public static TaskTestPhase<?> addSubjectResponsesToExamTask(IcarusExam_Phase1 exam, TaskTestPhase<?> subjectTask) throws Exception {
        TaskTestPhase<?> examTask = null;
        for (TaskTestPhase<?> t : exam.getTasks()) {
            if (t.getName().equals(subjectTask.getName())
                    || getTaskNumber(t.getName()) == getTaskNumber(subjectTask.getName())
                    || (t.getId() != null && t.getId().equals(subjectTask.getId()))) {
                examTask = t;
            }
        }
        if (examTask != null && examTask.getClass().equals(subjectTask.getClass())) {
            examTask.setStartTime(subjectTask.getStartTime());
            examTask.setEndTime(subjectTask.getEndTime());
            examTask.setResponseGenerator(subjectTask.getResponseGenerator());
            if (subjectTask instanceof Task_1_Phase) {
                ArrayList<Task_1_TrialBlock> subjectTrialBlocks = ((Task_1_Phase) subjectTask).getTrialBlocks();
                ArrayList<Task_1_TrialBlock> examTrialBlocks = ((Task_1_Phase) examTask).getTrialBlocks();
                int numTrials = Math.min(subjectTrialBlocks.size(), examTrialBlocks.size());
                for (int i = 0; i < numTrials; i++) {
                    examTrialBlocks.get(i).getProbeTrial().setTrialResponse(subjectTrialBlocks.get(i).getProbeTrial().getTrialResponse());
                }
            } else if (subjectTask instanceof Task_2_Phase) {
                ArrayList<Task_2_TrialBlock> subjectTrialBlocks = ((Task_2_Phase) subjectTask).getTrialBlocks();
                ArrayList<Task_2_TrialBlock> examTrialBlocks = ((Task_2_Phase) examTask).getTrialBlocks();
                int numTrials = Math.min(subjectTrialBlocks.size(), examTrialBlocks.size());
                for (int i = 0; i < numTrials; i++) {
                    examTrialBlocks.get(i).getProbeTrial().setTrialResponse(subjectTrialBlocks.get(i).getProbeTrial().getTrialResponse());
                }
            } else if (subjectTask instanceof Task_3_Phase) {
                ArrayList<Task_3_TrialBlock> subjectTrialBlocks = ((Task_3_Phase) subjectTask).getTrialBlocks();
                ArrayList<Task_3_TrialBlock> examTrialBlocks = ((Task_3_Phase) examTask).getTrialBlocks();
                int numTrials = Math.min(subjectTrialBlocks.size(), examTrialBlocks.size());
                for (int i = 0; i < numTrials; i++) {
                    examTrialBlocks.get(i).getProbeTrial().setTrialResponse(subjectTrialBlocks.get(i).getProbeTrial().getTrialResponse());
                }
            } else if (subjectTask instanceof Task_4_Phase) {
                ArrayList<Task_4_Trial> subjectTrials = ((Task_4_Phase) subjectTask).getTestTrials();
                ArrayList<Task_4_Trial> examTrials = ((Task_4_Phase) examTask).getTestTrials();
                int numTrials = Math.min(subjectTrials.size(), examTrials.size());
                for (int i = 0; i < numTrials; i++) {
                    examTrials.get(i).setTrialResponse(subjectTrials.get(i).getTrialResponse());
                }
            } else if (subjectTask instanceof Task_5_Phase) {
                ArrayList<Task_5_Trial> subjectTrials = ((Task_5_Phase) subjectTask).getTestTrials();
                ArrayList<Task_5_Trial> examTrials = ((Task_5_Phase) examTask).getTestTrials();
                int numTrials = Math.min(subjectTrials.size(), examTrials.size());
                for (int i = 0; i < numTrials; i++) {
                    examTrials.get(i).setTrialResponse(subjectTrials.get(i).getTrialResponse());
                }
            } else if (subjectTask instanceof Task_6_Phase) {
                ArrayList<Task_6_Trial> subjectTrials = ((Task_6_Phase) subjectTask).getTestTrials();
                ArrayList<Task_6_Trial> examTrials = ((Task_6_Phase) examTask).getTestTrials();
                int numTrials = Math.min(subjectTrials.size(), examTrials.size());
                for (int i = 0; i < numTrials; i++) {
                    examTrials.get(i).setTrialResponse(subjectTrials.get(i).getTrialResponse());
                }
            } else if (subjectTask instanceof Task_7_Phase) {
                ArrayList<Task_7_Trial> subjectTrials = ((Task_7_Phase) subjectTask).getTestTrials();
                ArrayList<Task_7_Trial> examTrials = ((Task_7_Phase) examTask).getTestTrials();
                int numTrials = Math.min(subjectTrials.size(), examTrials.size());
                for (int i = 0; i < numTrials; i++) {
                    examTrials.get(i).setTrialResponse(subjectTrials.get(i).getTrialResponse());
                }
            } else {
                throw new IllegalArgumentException("Error, unrecognized task type: " + subjectTask.getClass().getName());
            }
        } else {
            throw new IllegalArgumentException("Error: subject task " + subjectTask.getName() + " not found in exam.");
        }
        return examTask;
    }

    /**
     * @param taskName
     * @return
     */
    public static int getTaskNumber(String taskName) {
        int endIndex = taskName.length() - 1;
        try {
            return Integer.parseInt(taskName.substring(endIndex));
        } catch (Exception ex) {
            return -1;
        }
    }

    /**
     * Score all subject data files and write updated XML data files for the
     * Pilot Exam.
     */
    protected static void scorePilotExamSubjects() {
        try {
            //Load the pilot exam
            List<Integer> tasks = Arrays.asList(1, 2, 3, 4, 5, 6, 7);
			//List<Integer> tasks = Arrays.asList(6);
            //List<Integer> tasks = Arrays.asList(2);
            URL examFile = new File("data/Phase_1_CPD/Pilot_Experiment/PilotExam.xml").toURI().toURL();
            IcarusExam_Phase1 exam = IcarusExamLoader_Phase1.unmarshalExam(examFile);
            for (int taskNum : tasks) {
                TaskTestPhase<?> task = exam.getTasks().get(taskNum - 1);
                IcarusExamLoader_Phase1.initializeTaskFeatureVectorData(task,
                        examFile, exam.getGridSize(),
                        false, null);
            }

            //Get the subjects
            Set<IcarusSubjectData> subjects = DataAggregatorUtils.getSubjectsInFolder(
                    new File("data/response_data/March 2012 Pilot"), true);
			//List<String> subjects = Arrays.asList("S002", "S003", "S004", "S005", "S006", "S007", "S008",
            //		"S009", "S010", "S011");
            //List<String> subjects = Arrays.asList("SM113");			
            System.out.println(subjects);

			//Strip normative solutions from the tasks
			/*sanitizeTasks(exam.getName(), 
             subjects, tasks, 
             new File("data/response_data/March 2012 Pilot").toURI().toURL(), 
             new File("data/response_data/March 2012 Pilot/_Sanitized Tasks").toURI().toURL(), true);*/
            //Score the tasks
            computeScores(exam,
                    new File("data/Phase_1_CPD/Pilot_Experiment").toURI().toURL(),
                    subjects, tasks,
                    new File("data/response_data/March 2012 Pilot").toURI().toURL(),
                    new File("data/response_data/March 2012 Pilot/_Scored Tasks").toURI().toURL(), true, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Score all subject data files and write updated XML data files for the
     * Final Exam.
     */
    protected static void scoreFinalExamSubjects() {
        try {
            //Load the final exam
            List<Integer> tasks = Arrays.asList(1, 2, 3, 4, 5, 6, 7);
            URL examFile = new File("data/Phase_1_CPD/Final_Experiment/FinalExam.xml").toURI().toURL();
            IcarusExam_Phase1 exam = IcarusExamLoader_Phase1.unmarshalExam(examFile);
            for (int taskNum : tasks) {
                TaskTestPhase<?> task = exam.getTasks().get(taskNum - 1);
                IcarusExamLoader_Phase1.initializeTaskFeatureVectorData(task,
                        examFile, exam.getGridSize(),
                        false, null);
            }

            boolean scoreAllSubjects = true;
            boolean scoreStandardOrderSubjects = true;
            boolean scoreReverseLayerSubjects = true;
            try {
                Set<IcarusSubjectData> standardOrderSubjects = null;
                if (scoreAllSubjects || scoreStandardOrderSubjects) {
                    standardOrderSubjects = DataAggregatorUtils.getSubjectsInFolder(
                            new File("data/response_data/Phase 1 Final Exam August 2012/Standard Layer Order Subjects"), true);
                }
                if (scoreAllSubjects) {
                    //For Missions 1=5, score all subjects (both standard and reverse layer order, N=103)
                    Set<IcarusSubjectData> allSubjects = DataAggregatorUtils.getSubjectsInFolder(
                            new File("data/response_data/Phase 1 Final Exam August 2012/All Subjects"), true);
                    System.out.println("All Subjects: " + allSubjects.size());
                    computeScores(exam,
                            new File("data/Phase_1_CPD/Final_Experiment").toURI().toURL(),
                            allSubjects, tasks,
                            new File("data/response_data/Phase 1 Final Exam August 2012/All Subjects").toURI().toURL(),
                            new File("data/response_data/Phase 1 Final Exam August 2012/Scored Tasks/All Subjects").toURI().toURL(), true, true);
                }
                if (scoreStandardOrderSubjects) {
                    //Score subjects who took the exam with the layers in standard order (N=79)
                    System.out.println("Standard Order Subjects: " + standardOrderSubjects.size());
                    computeScores(exam,
                            new File("data/Phase_1_CPD/Final_Experiment").toURI().toURL(),
                            standardOrderSubjects, tasks,
                            new File("data/response_data/Phase 1 Final Exam August 2012/Standard Layer Order Subjects").toURI().toURL(),
                            new File("data/response_data/Phase 1 Final Exam August 2012/Scored Tasks/Standard Layer Order Subjects").toURI().toURL(), true, true);
                }
                if (scoreReverseLayerSubjects) {
                    //Score subjects who took the exam with the layers in reverse order (N=24)
                    Set<IcarusSubjectData> reverseLayerSubjects = DataAggregatorUtils.getSubjectsInFolder(
                            new File("data/response_data/Phase 1 Final Exam August 2012/Reverse Layer Order Subjects"), true);
                    System.out.println("Reverse Layer Subjects: " + reverseLayerSubjects.size());
                    computeScores(exam,
                            new File("data/Phase_1_CPD/Final_Experiment").toURI().toURL(),
                            reverseLayerSubjects, tasks,
                            new File("data/response_data/Phase 1 Final Exam August 2012/Reverse Layer Order Subjects").toURI().toURL(),
                            new File("data/response_data/Phase 1 Final Exam August 2012/Scored Tasks/Reverse Layer Order Subjects").toURI().toURL(), true, true);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        //scorePilotExamSubjects();
        scoreFinalExamSubjects();
    }
}
