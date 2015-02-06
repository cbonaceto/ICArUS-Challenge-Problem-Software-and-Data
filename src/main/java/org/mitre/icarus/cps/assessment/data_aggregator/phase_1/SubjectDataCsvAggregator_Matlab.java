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
package org.mitre.icarus.cps.assessment.data_aggregator.phase_1;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.mitre.icarus.cps.app.experiment.IcarusSubjectData;
import org.mitre.icarus.cps.app.util.ProbabilityUtils;
import org.mitre.icarus.cps.assessment.assessment_processor.phase_1.BatchFileProcessor;
import org.mitre.icarus.cps.assessment.data_aggregator.DataAggregatorUtils;
import org.mitre.icarus.cps.assessment.data_aggregator.TaskFileAttributes;
import org.mitre.icarus.cps.exam.phase_1.loader.IcarusExamLoader_Phase1;
import org.mitre.icarus.cps.exam.phase_1.response.Task_1_2_3_ProbeTrialResponseBase;
import org.mitre.icarus.cps.exam.phase_1.response.Task_2_ProbeTrialResponse;
import org.mitre.icarus.cps.exam.phase_1.response.Task_3_ProbeTrialResponse;
import org.mitre.icarus.cps.exam.phase_1.response.Task_4_TrialResponse;
import org.mitre.icarus.cps.exam.phase_1.response.Task_5_6_TrialResponse;
import org.mitre.icarus.cps.exam.phase_1.response.Task_7_TrialResponse;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.GroupAttackProbabilityResponse;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.GroupCentersProbeResponse;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.GroupCentersProbeResponse.GroupCenterResponse;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.GroupCirclesProbeResponse;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.GroupCirclesProbeResponse.GroupCircle;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.INTLayerExpectedUtility;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.Task_4_7_AttackLocationProbeResponseAfterINT;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.Task_5_6_AttackLocationProbeResponseAfterINT;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.TroopAllocation;
import org.mitre.icarus.cps.exam.phase_1.testing.IcarusTestTrial_Phase1;
import org.mitre.icarus.cps.exam.phase_1.testing.TaskTestPhase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_1_2_3_AttackDispersionParameters;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_1_2_3_ProbeTrialBase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_2_ProbeTrial;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_3_ProbeTrial;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_4_Trial;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_5_Trial;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_6_Trial;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_7_Trial;
import org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.ImintLayer;
import org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.IntLayer;
import org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.MovintLayer;
import org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.SigintLayer;
import org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.SocintLayer;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupType;
import org.mitre.icarus.cps.feature_vector.phase_1.IntType;

/**
 * Aggregates XML data files from subject responses into CSV data files for the
 * Phase 1 Challenge Problem.
 *
 * @author CBONACETO
 *
 */
public class SubjectDataCsvAggregator_Matlab {

    protected static String separator = ",";

    /**
     * The maximum number of probability response trial parts
     */
    protected static int numProbabilitySets = 5;

    /**
     * The maximum number of probabilities in a probability response trial part
     */
    protected static int maxProbsInSet = 4;

    /**
     * The maximum number of groups
     */
    protected static int numGroups = 4;

    /**
     * Whether to append dispersion parameters to the CSV files for Tasks 2-3
     */
    protected static boolean appendDispersionParameters = true;

    /**
     * Whether to append distances from subject group centers to the attack
     * location for Task 3
     */
    protected static boolean appendDistances = true;

    /**
     * Whether to append the best layer information for Task 6
     */
    protected static boolean appendBestLayers = true;

    /**
     * Whether to append expected information gain for each layer in Task 6
     */
    protected static boolean appendLayerInformationGain = false;
    protected static final LinkedList<IntLayer> INT_LAYERS = new LinkedList<IntLayer>();

    static {
        INT_LAYERS.add(new ImintLayer());
        INT_LAYERS.add(new MovintLayer());
        for (GroupType group : Arrays.asList(GroupType.A, GroupType.B, GroupType.C, GroupType.D)) {
            INT_LAYERS.add(new SigintLayer(group));
        }
        INT_LAYERS.add(new SocintLayer());
    }

    /**
     * Aggregates subject data from each exam into a CSV file.
     *
     * @param dataDir
     * @param outputDir
     * @param examIds
     * @param useSubjectFolders
     * @param outputProgress
     * @throws Exception
     */
    public static void aggregateSubjectData(File dataDir, File outputDir, Set<String> examIds, boolean useSubjectFolders,
            boolean outputProgress) throws Exception {
        aggregateSubjectData(dataDir, outputDir, examIds, useSubjectFolders, null, null, outputProgress);
    }

    /**
     * Aggregates subject data from each exam into a CSV file.
     *
     * @param dataDir
     * @param outputDir
     * @param examIds
     * @param useSubjectFolders
     * @param outputProgress
     * @param tasks
     * @throws Exception
     */
    public static void aggregateSubjectData(File dataDir, File outputDir, Set<String> examIds, boolean useSubjectFolders,
            ArrayList<Integer> tasks, boolean outputProgress) throws Exception {
        aggregateSubjectData(dataDir, outputDir, examIds, useSubjectFolders, tasks, null, outputProgress);
    }

    /**
     * Aggregates subject data from each exam into a CSV file.
     *
     * @param dataDir
     * @param outputDir
     * @param examIds
     * @param useSubjectFolders
     * @param tasks
     * @param subjects
     * @param outputProgress
     * @throws Exception
     */
    public static void aggregateSubjectData(File dataDir, File outputDir, Set<String> examIds, boolean useSubjectFolders,
            ArrayList<Integer> tasks, Set<IcarusSubjectData> subjects, boolean outputProgress) throws Exception {
        if (!dataDir.isDirectory() || !dataDir.exists()) {
            throw new IllegalArgumentException(dataDir.getPath() + " is not a directory or cannot be found.");
        }
        if (!outputDir.isDirectory() || !outputDir.exists()) {
            throw new IllegalArgumentException(outputDir.getPath() + " is not a directory or cannot be found.");
        }

        HashMap<String, TaskFile> taskFiles = new HashMap<String, TaskFile>();
        if (tasks == null || tasks.isEmpty()) {
            for (int i = 1; i <= 7; i++) {
                TaskFile taskFile = new TaskFile();
                taskFiles.put("Task" + Integer.toString(i), taskFile);
                taskFiles.put("Mission" + Integer.toString(i), taskFile);
                //taskFiles.put("Task" + Integer.toString(i), new TaskFile());				
            }
        } else {
            for (Integer task : tasks) {
                TaskFile taskFile = new TaskFile();
                taskFiles.put("Task" + Integer.toString(task), taskFile);
                taskFiles.put("Mission" + Integer.toString(task), taskFile);
                //taskFiles.put("Task" + Integer.toString(task), new TaskFile());
            }
        }

        File[] files = dataDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (useSubjectFolders) {
                    if (file.isDirectory()) {
                        //if(file.isDirectory() && 
                        //		(subjects == null || subjects.contains(file.getName()))) {
                        File[] subjectFiles = file.listFiles();
                        if (subjectFiles != null) {
                            for (File subjectFile : subjectFiles) {
                                //Process subject data file
                                processSubjectFile(subjectFile, examIds, subjects, taskFiles, outputDir, outputProgress);
                            }
                        }
                    } else if (outputProgress) {
                        System.out.println("Ignoring file or folder: " + file.getName());
                    }
                } else if (file.isFile()) {
                    //Process subject data file
                    processSubjectFile(file, examIds, subjects, taskFiles, outputDir, outputProgress);
                }
            }
            for (TaskFile taskFile : taskFiles.values()) {
                try {
                    if (taskFile.writer != null) {
                        taskFile.writer.flush();
                        taskFile.writer.close();
                    }
                } catch (IOException ex) {
                }
            }
        }
        if (outputProgress) {
            System.out.println("Complete");
        }
    }

    /**
     * Processes a subject response data file.
     *
     * @param file
     * @param examIds
     * @param subjects
     * @param taskFiles
     * @param outputDir
     * @param outputProgress
     * @throws IOException
     */
    protected static void processSubjectFile(File file, Set<String> examIds,
            Set<IcarusSubjectData> subjects, Map<String, TaskFile> taskFiles, File outputDir,
            boolean outputProgress) throws IOException {
        TaskFileAttributes fileAttributes = null;
        if (file.isFile()) {
            try {
                fileAttributes = TaskFileAttributes.parseTaskFileAttributes(file.getName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
		//DEBUG CODE
		/*if(fileAttributes != null) {
         System.out.println(examIds.contains(fileAttributes.examName) + ", " +
         taskFiles.containsKey(fileAttributes.taskName) + ", " +  
         (subjects == null || subjects.contains(fileAttributes.subjectData)));
         //System.out.println(fileAttributes.subjectData.getSubjectId() + subjects);
         } else {
         System.out.println("fileAttributes is null");
         }*/
        //END DEBUG CODE
        //if(fileAttributes != null && examName.equalsIgnoreCase(fileAttributes.examName) &&
        if (fileAttributes != null && examIds.contains(fileAttributes.examName)
                && taskFiles.containsKey(fileAttributes.taskName)
                && (subjects == null || subjects.contains(fileAttributes.subjectData))) {
            //(subjects == null || subjects.contains(fileAttributes.subjectId))) {
            //Parse the subject response file
            TaskTestPhase<?> response = null;
            try {
                response = IcarusExamLoader_Phase1.unmarshalTask(file.toURI().toURL(), false);
            } catch (Exception ex) {
                if (outputProgress) {
                    System.err.println("Error parsing file: " + file.getName());
                }
                ex.printStackTrace();
            }

            //Append the subject response to the CSV file for the task
            if (response != null) {
                TaskFile outputFile = taskFiles.get(fileAttributes.taskName);
                try {
                    if (outputFile.writer == null) {
                        outputFile.writer = new BufferedWriter(new FileWriter(new File(outputDir,
                                "allresponses_task_" + fileAttributes.taskNum + ".csv")));
                        writeHeader(outputFile.writer);
                    }
                    String siteId = fileAttributes.subjectData.getSite() != null ? fileAttributes.subjectData.getSite().getTag() : null;
                    //String subjectId = fileAttributes.subjectId;
                    String subjectId = fileAttributes.subjectData.getSubjectId();
                    if (response.getResponseGenerator() != null) {
                        if (response.getResponseGenerator().getSiteId() != null) {
                            siteId = response.getResponseGenerator().getSiteId();
                        }
                        if (response.getResponseGenerator().getResponseGeneratorId() != null) {
                            subjectId = response.getResponseGenerator().getResponseGeneratorId();
                        }
                    }
                    appendTaskResponse(outputFile.writer, response, fileAttributes.taskNum, siteId, subjectId);
                    if (outputProgress) {
                        System.out.println("Appended data from file: " + file.getName());
                    }
                } catch (Exception ex) {
                    if (outputProgress) {
                        System.err.println("Error appending data from file: " + file.getName());
                    }
                    ex.printStackTrace();
                }
            } else if (outputProgress) {
                System.out.println("File did not contain valid data: " + file.getName());
            }
        } else if (outputProgress) {
            System.out.println("Ignoring file: " + file.getName());
        }
    }

    /**
     * Writes the column headers to the subject data CSV file.
     *
     * @param writer
     * @throws IOException
     */
    protected static void writeHeader(Writer writer) throws IOException {
        //Headers:
        //site_id,response_generator_id,is_human,time_stamp,exam_id,phase_id,phase_number,trial_number,trial_time,ground_truth,surprise,surprise_time,s1_score,s2_score,number_probability_sets,number_groups,max_probs_in_set,layer_type_1,layer_type_2,layer_type_3,layer_type_4,layer_type_5,layer_best_int_1,layer_best_int_2,layer_best_int_3,layer_best_int_4,layer_best_int_5,layer_best_sigint_1,layer_best_sigint_2,layer_best_sigint_3,layer_best_sigint_4,layer_best_sigint_5,layer_surprise_1,layer_surprise_2,layer_surprise_3,layer_surprise_4,layer_surprise_5,layer_surprise_time_1,layer_surprise_time_2,layer_surprise_time_3,layer_surprise_time_4,layer_surprise_time_5,response_probs_1_1,response_probs_1_2,response_probs_1_3,response_probs_1_4,response_probs_2_1,response_probs_2_2,response_probs_2_3,response_probs_2_4,response_probs_3_1,response_probs_3_2,response_probs_3_3,response_probs_3_4,response_probs_4_1,response_probs_4_2,response_probs_4_3,response_probs_4_4,response_probs_5_1,response_probs_5_2,response_probs_5_3,response_probs_5_4,raw_response_probs_1_1,raw_response_probs_1_2,raw_response_probs_1_3,raw_response_probs_1_4,raw_response_probs_2_1,raw_response_probs_2_2,raw_response_probs_2_3,raw_response_probs_2_4,raw_response_probs_3_1,raw_response_probs_3_2,raw_response_probs_3_3,raw_response_probs_3_4,raw_response_probs_4_1,raw_response_probs_4_2,raw_response_probs_4_3,raw_response_probs_4_4,raw_response_probs_5_1,raw_response_probs_5_2,raw_response_probs_5_3,raw_response_probs_5_4,normative_probs_1_1,normative_probs_1_2,normative_probs_1_3,normative_probs_1_4,normative_probs_2_1,normative_probs_2_2,normative_probs_2_3,normative_probs_2_4,normative_probs_3_1,normative_probs_3_2,normative_probs_3_3,normative_probs_3_4,normative_probs_4_1,normative_probs_4_2,normative_probs_4_3,normative_probs_4_4,normative_probs_5_1,normative_probs_5_2,normative_probs_5_3,normative_probs_5_4,probs_time_1,probs_time_2,probs_time_3,probs_time_4,probs_time_5,norm_allocation_1,norm_allocation_2,norm_allocation_3,norm_allocation_4,raw_allocation_1,raw_allocation_2,raw_allocation_3,raw_allocation_4,allocation_time,center_x_1,center_x_2,center_x_3,center_x_4,center_y_1,center_y_2,center_y_3,center_y_4,circle_r_1,circle_r_2,circle_r_3,circle_r_4,normative_center_x_1,normative_center_x_2,normative_center_x_3,normative_center_x_4,normative_center_y_1,normative_center_y_2,normative_center_y_3,normative_center_y_4,baserate_1,baserate_2,baserate_3,baserate_4,sigma_x_1,sigma_x_2,sigma_x_3,sigma_x_4,sigma_y_1,sigma_y_2,sigma_y_3,sigma_y_4,centers_circles_time,distance_1,distance_2,distance_3,distance_4,response_distance_1,response_distance_2,response_distance_3,response_distance_4

        writer.write("Site_id,Subject_id,Time_stamp,Phase_number,Trial_number,Trial_time,Ground_Truth,Surprise,Surprise_time,Probabilities_score,"
                + "Allocation_score,Number_probability_sets,Number_groups,Max_probs_in_set,");
        for (int i = 1; i <= numProbabilitySets; i++) {
            writer.write("Layer_type_" + Integer.toString(i) + separator);
        }
        if (appendBestLayers) {
            for (int i = 1; i <= numProbabilitySets; i++) {
                writer.write("Layer_Best_INT_" + Integer.toString(i) + separator);
            }
            for (int i = 1; i <= numProbabilitySets; i++) {
                writer.write("Layer_Best_SIGINT_" + Integer.toString(i) + separator);
            }
        }
        if (appendLayerInformationGain) {
            for (int i = 1; i <= numProbabilitySets; i++) {
                for (IntLayer layer : INT_LAYERS) {
                    writer.write(formatLayerName(layer) + "_Info_" + Integer.toString(i) + separator);
                }
            }
        }
        for (int i = 1; i <= numProbabilitySets; i++) {
            writer.write("Layer_Surprise_" + Integer.toString(i) + separator);
        }
        for (int i = 1; i <= numProbabilitySets; i++) {
            writer.write("Layer_Surprise_Time_" + Integer.toString(i) + separator);
        }
        for (int i = 1; i <= numProbabilitySets; i++) {
            for (int j = 1; j <= maxProbsInSet; j++) {
                writer.write("Human_probs_" + Integer.toString(i) + "_" + Integer.toString(j) + separator);
            }
        }
        for (int i = 1; i <= numProbabilitySets; i++) {
            for (int j = 1; j <= maxProbsInSet; j++) {
                writer.write("Raw_human_probs_" + Integer.toString(i) + "_" + Integer.toString(j) + separator);
            }
        }
        for (int i = 1; i <= numProbabilitySets; i++) {
            for (int j = 1; j <= maxProbsInSet; j++) {
                writer.write("Normative_probs_" + Integer.toString(i) + "_" + Integer.toString(j) + separator);
            }
        }
        for (int i = 1; i <= numProbabilitySets; i++) {
            writer.write("Probs_Time_" + Integer.toString(i) + separator);
        }
        for (int i = 1; i <= maxProbsInSet; i++) {
            writer.write("Norm_allocation_" + Integer.toString(i) + separator);
        }
        for (int i = 1; i <= maxProbsInSet; i++) {
            writer.write("Raw_allocation_" + Integer.toString(i) + separator);
        }
        writer.write("Allocation_Time" + separator);
        for (int i = 1; i <= numGroups; i++) {
            writer.write("Center_X_" + Integer.toString(i) + separator);
        }
        for (int i = 1; i <= numGroups; i++) {
            writer.write("Center_Y_" + Integer.toString(i) + separator);
        }
        for (int i = 1; i <= numGroups; i++) {
            writer.write("Circle_R_" + Integer.toString(i) + separator);
        }
        if (appendDispersionParameters) {
            for (int i = 1; i <= numGroups; i++) {
                writer.write("Normative_Center_X_" + Integer.toString(i) + separator);
            }
            for (int i = 1; i <= numGroups; i++) {
                writer.write("Normative_Center_Y_" + Integer.toString(i) + separator);
            }
            for (int i = 1; i <= numGroups; i++) {
                writer.write("BaseRate_" + Integer.toString(i) + separator);
            }
            for (int i = 1; i <= numGroups; i++) {
                writer.write("Sigma_X_" + Integer.toString(i) + separator);
            }
            for (int i = 1; i <= numGroups; i++) {
                writer.write("Sigma_Y_" + Integer.toString(i) + separator);
            }
        }
        writer.write("Centers_Circles_Time");
        if (appendDistances) {
            writer.write(separator);
            for (int i = 1; i <= maxProbsInSet; i++) {
                writer.write("Distance_" + Integer.toString(i) + separator);
            }
            for (int i = 1; i <= maxProbsInSet; i++) {
                writer.write("Subject_Distance_" + Integer.toString(i));
                if (i < maxProbsInSet) {
                    writer.write(separator);
                }
            }
        }
        writer.write("\n");
    }

    /**
     * Appends a subject response to the given subject data CSV file writer.
     * 
     * @param writer
     * @param task
     * @param taskNum
     * @param siteId
     * @param subjectId
     * @throws Exception
     */
    protected static void appendTaskResponse(Writer writer, TaskTestPhase<?> task, int taskNum,
            String siteId, String subjectId) throws Exception {
        int trialNum = 1;
        //int trialBlockNum = 0;
        //int maxNumTrialBlocks = 5;		
        for (IcarusTestTrial_Phase1 trial : task.getTestTrials()) {
            writeData(writer, siteId);
            writeData(writer, subjectId);
            writer.write("NaN" + separator + Integer.toString(taskNum) + separator);
            writer.write(Integer.toString(trialNum) + separator);
            if (trial instanceof Task_1_2_3_ProbeTrialBase) {
                //if(trialBlockNum < maxNumTrialBlocks) {
                //writer.write(subjectId + separator + Integer.toString(taskNum) + separator);
                //writer.write(Integer.toString(trialNum) + separator);
                appendTask_1_2_3_TrialResponse(writer, (Task_1_2_3_ProbeTrialBase) trial);
                //}
                //trialBlockNum++;
            } else if (trial instanceof Task_4_Trial) {
                //writer.write(subjectId + separator + Integer.toString(taskNum) + separator);
                //writer.write(Integer.toString(trialNum) + separator);
                appendTask_4_TrialResponse(writer, (Task_4_Trial) trial);
            } else if (trial instanceof Task_5_Trial) {
                //writer.write(subjectId + separator + Integer.toString(taskNum) + separator);
                //writer.write(Integer.toString(trialNum) + separator);
                appendTask_5_6_TrialResponse(writer, (Task_5_Trial) trial);
            } else if (trial instanceof Task_6_Trial) {
                //writer.write(subjectId + separator + Integer.toString(taskNum) + separator);
                //writer.write(Integer.toString(trialNum) + separator);
                appendTask_5_6_TrialResponse(writer, (Task_6_Trial) trial);
            } else if (trial instanceof Task_7_Trial) {
                appendTask_7_TrialResponse(writer, (Task_7_Trial) trial);
            }
            trialNum++;
        }
    }

    /**
     * Appends a subject response for Tasks 1-3 to the given subject data CSV file writer.
     * 
     * @param writer
     * @param trial
     * @throws Exception
     */
    protected static void appendTask_1_2_3_TrialResponse(Writer writer, Task_1_2_3_ProbeTrialBase trial) throws Exception {
        if (trial.getTrialResponse() != null) {
            Task_1_2_3_ProbeTrialResponseBase response = trial.getTrialResponse();
            writeData(writer, response.getTrialTime_ms());
            if (trial.getGroundTruth() != null && trial.getGroundTruth().getResponsibleGroup() != null) {
                writeData(writer, trial.getGroundTruth().getResponsibleGroup());
            } else {
                writer.write("NaN" + separator);
            }
            writeData(writer, response.getGroundTruthSurpriseResponse().getSurpriseVal());
            writeData(writer, response.getGroundTruthSurpriseResponse().getTrialPartTime_ms());
            if (response.getResponseFeedBack() != null) {
                writeData(writer, response.getResponseFeedBack().getProbabilitiesScore_s1());
                writeData(writer, response.getResponseFeedBack().getTroopAllocationScore_s2());
            } else {
                writer.write("NaN" + separator);
                writer.write("NaN" + separator);
            }
            writer.write("1" + separator);
            writer.write(response.getAttackLocationResponse().getGroupAttackProbabilities().size() + separator);
            writer.write(Integer.toString(maxProbsInSet) + separator);

            //Write NaNs for Layer_type
            for (int j = 1; j <= numProbabilitySets; j++) {
                writer.write("NaN" + separator);
            }

            if (appendBestLayers) {
                //Write NaNs for Layer_Best_INT, Layer_Best_SIGINT
                for (int i = 0; i < 2; i++) {
                    for (int j = 1; j <= numProbabilitySets; j++) {
                        writer.write("NaN" + separator);
                    }
                }
            }

            if (appendLayerInformationGain) {
                //Write NaNs for Layer Information Gain
                int i = 0;
                for (; i < numProbabilitySets; i++) {
                    for (int layerIndex = 0; layerIndex < INT_LAYERS.size(); layerIndex++) {
                        writer.write("NaN" + separator);
                    }
                }
            }

            //Write NaNs for Layer_Surprise and Layer_Surprise_Time
            for (int i = 0; i < 2; i++) {
                for (int j = 1; j <= numProbabilitySets; j++) {
                    writer.write("NaN" + separator);
                }
            }

            //Write probabilities
            List<List<GroupAttackProbabilityResponse>> subjectProbs = new LinkedList<List<GroupAttackProbabilityResponse>>();
            subjectProbs.add(response.getAttackLocationResponse().getGroupAttackProbabilities());
            List<List<Double>> normativeProbs = new LinkedList<List<Double>>();
            normativeProbs.add(response.getAttackLocationResponse().getNormativeProbs_cumulativeBayesian());
            appendProbabilities(writer, subjectProbs, normativeProbs);

            //Write probability entry time
            writeData(writer, response.getAttackLocationResponse().getTrialPartTime_ms());
            for (int i = 1; i < numProbabilitySets; i++) {
                writer.write("NaN" + separator);
            }

            //Write troop selections if present
            if (response.getTroopSelectionResponse() != null) {
                for (int j = 0; j < 2; j++) {
                    int i = 0;
                    for (GroupType group : trial.getTroopSelectionProbe().getGroups()) {
                        if (group == response.getTroopSelectionResponse().getGroup()) {
                            writer.write("100" + separator);
                        } else {
                            writer.write("0" + separator);
                        }
                        i++;
                    }
                    for (; i < maxProbsInSet; i++) {
                        writer.write("NaN" + separator);
                    }
                }
                //Write troop selection time
                writeData(writer, response.getTroopSelectionResponse().getTrialPartTime_ms());
            } else {
                for (int j = 0; j < 2; j++) {
                    for (int i = 0; i < maxProbsInSet; i++) {
                        writer.write("NaN" + separator);
                    }
                }
                writer.write("NaN" + separator);
            }

            //Write group circles or centers if present, or NaNs otherwise
            Long centersCirclesTime = null;
            if (trial instanceof Task_2_ProbeTrial) {
                GroupCirclesProbeResponse groupCircles = ((Task_2_ProbeTrialResponse) response).getGroupCirclesResponse();
                centersCirclesTime = groupCircles.getTrialPartTime_ms();
                int i = 0;
                for (GroupCircle circle : groupCircles.getGroupCircles()) {
                    writeData(writer, circle.getCenterLocation().getX());
                    i++;
                }
                for (; i < numGroups; i++) {
                    writer.write("NaN" + separator);
                }
                i = 0;
                for (GroupCircle circle : groupCircles.getGroupCircles()) {
                    writeData(writer, circle.getCenterLocation().getY());
                    i++;
                }
                for (; i < numGroups; i++) {
                    writer.write("NaN" + separator);
                }
                i = 0;
                for (GroupCircle circle : groupCircles.getGroupCircles()) {
                    writeData(writer, circle.getRadius());
                    i++;
                }
                for (; i < numGroups; i++) {
                    writer.write("NaN" + separator);
                }
            } else if (trial instanceof Task_3_ProbeTrial) {
                GroupCentersProbeResponse groupCenters = ((Task_3_ProbeTrialResponse) response).getGroupCentersResponse();
                centersCirclesTime = groupCenters.getTrialPartTime_ms();
                int i = 0;
                for (GroupCenterResponse center : groupCenters.getGroupCenters()) {
                    writeData(writer, center.getLocation().getX());
                    i++;
                }
                for (; i < numGroups; i++) {
                    writer.write("NaN" + separator);
                }
                i = 0;
                for (GroupCenterResponse center : groupCenters.getGroupCenters()) {
                    writeData(writer, center.getLocation().getY());
                    i++;
                }
                for (; i < numGroups; i++) {
                    writer.write("NaN" + separator);
                }
                for (i = 1; i <= numGroups; i++) {
                    writer.write("NaN" + separator);
                }
            } else {
                for (int i = 0; i < 3; i++) {
                    for (int j = 1; j <= numGroups; j++) {
                        writer.write("NaN" + separator);
                    }
                }
            }

            //Write actual dispersion parameters if present (actual group centers, sigmas)
            if (appendDispersionParameters) {
                appendDispersionParameters(writer, trial.getAttackDispersionParameters());
            }

            //Write time to locate group centers or circles if present (Tasks 2 & 3)
            writeData(writer, centersCirclesTime);

            if (appendDistances) {
                if (trial instanceof Task_3_ProbeTrial) {
                    Task_3_ProbeTrialResponse task3Response = (Task_3_ProbeTrialResponse) response;
                    //Write distances from group centers to the attack location
                    ArrayList<Double> distances = ((Task_3_ProbeTrial) trial).getCentersToAttackDistances();
                    if (distances != null) {
                        int i = 0;
                        for (Double distance : distances) {
                            writeData(writer, distance);
                            i++;
                        }
                        for (; i < maxProbsInSet; i++) {
                            writer.write("NaN" + separator);
                        }
                    } else {
                        for (int i = 0; i < maxProbsInSet; i++) {
                            writer.write("NaN" + separator);
                        }
                    }

                    //Write distances from subject group centers to the attack location if present
                    if (task3Response.getCentersToAttackDistances() != null) {
                        int i = 0;
                        for (Double distance : task3Response.getCentersToAttackDistances()) {
                            writeData(writer, distance);
                            i++;
                        }
                        for (; i < maxProbsInSet; i++) {
                            writer.write("NaN" + separator);
                        }
                    } else {
                        for (int i = 0; i < maxProbsInSet; i++) {
                            writer.write("NaN" + separator);
                        }
                    }
                } else {
                    for (int i = 0; i < maxProbsInSet; i++) {
                        writer.write("NaN" + separator);
                    }
                    for (int i = 0; i < maxProbsInSet; i++) {
                        writer.write("NaN" + separator);
                    }
                }
            }
            writer.write("\n");
        }
    }

    /**
     * Appends a subject response for Tasks 4 to the given subject data CSV file writer.
     * 
     * @param writer
     * @param trial
     * @throws Exception
     */
    protected static void appendTask_4_TrialResponse(Writer writer, Task_4_Trial trial) throws Exception {
        if (trial.getTrialResponse() != null) {
            //Trial_time, Ground_Truth, Surprise, Surprise_time, Probabilities_score, 
            //Allocation_score, Number_probability_sets, Number_groups, Max_probs_in_set,			
            Task_4_TrialResponse response = trial.getTrialResponse();
            writeData(writer, response.getTrialTime_ms());
            if (trial.getGroundTruth() != null && trial.getGroundTruth().getAttackLocationId() != null) {
                writeData(writer, trial.getGroundTruth().getAttackLocationId());
            } else {
                writer.write("NaN" + separator);
            }
            if (response.getGroundTruthSurpriseResponse() != null) {
                writeData(writer, response.getGroundTruthSurpriseResponse().getSurpriseVal());
                writeData(writer, response.getGroundTruthSurpriseResponse().getTrialPartTime_ms());
            } else {
                writer.write("NaN" + separator);
                writer.write("NaN" + separator);
            }
            if (response.getResponseFeedBack() != null) {
                writeData(writer, response.getResponseFeedBack().getProbabilitiesScore_s1());
                writeData(writer, response.getResponseFeedBack().getTroopAllocationScore_s2());
            } else {
                writer.write("NaN" + separator);
                writer.write("NaN" + separator);
            }
            writer.write(Integer.toString(1 + response.getAttackLocationResponses_afterINTs().size()) + separator);
            writer.write(response.getTroopAllocationResponse().getTroopAllocations().size() + separator);
            writer.write(Integer.toString(maxProbsInSet) + separator);

            //Write Layer_type
            int i = 0;
            for (Task_4_7_AttackLocationProbeResponseAfterINT intResponse : response.getAttackLocationResponses_afterINTs()) {
                writeData(writer, formatLayerName(intResponse.getIntLayerShown().getLayerType()));
                i++;
            }
            for (; i < numProbabilitySets; i++) {
                writer.write("NaN" + separator);
            }

            if (appendBestLayers) {
                //Write NaNs for Layer_Best_INT and Layer_Best_SIGINT
                for (i = 0; i < 2; i++) {
                    for (int j = 1; j <= numProbabilitySets; j++) {
                        writer.write("NaN" + separator);
                    }
                }
            }

            if (appendLayerInformationGain) {
                //Write NaNs for Layer Information Gain
                for (i = 0; i < numProbabilitySets; i++) {
                    for (int layerIndex = 0; layerIndex < INT_LAYERS.size(); layerIndex++) {
                        writer.write("NaN" + separator);
                    }
                }
            }

            //Write Layer_Surprise
            i = 0;
            for (; i < numProbabilitySets; i++) {
                writer.write("NaN" + separator);
            }
            /*for(Task_4_7_AttackLocationProbeResponseAfterINT intResponse : response.getAttackLocationResponses_afterINTs()) {				
             if(intResponse.getSurpriseReportResponse() != null) {
             writeData(writer, intResponse.getSurpriseReportResponse().getSurpriseVal());
             } else {
             writer.write("NaN" + separator);
             }
             i++;
             }*/

            //Write Layer_Surprise_Time
            i = 0;
            for (; i < numProbabilitySets; i++) {
                writer.write("NaN" + separator);
            }
            /*for(Task_4_7_AttackLocationProbeResponseAfterINT intResponse : response.getAttackLocationResponses_afterINTs()) {			
             if(intResponse.getSurpriseReportResponse() != null) {
             writeData(writer, intResponse.getSurpriseReportResponse().getTrialPartTime_ms());
             } else {
             writer.write("NaN" + separator);
             }
             i++;
             }*/

            //Write probabilities
            List<List<GroupAttackProbabilityResponse>> subjectProbs = new LinkedList<List<GroupAttackProbabilityResponse>>();
            List<List<Double>> normativeProbs = new LinkedList<List<Double>>();
            subjectProbs.add(response.getAttackLocationResponse_initial().getGroupAttackProbabilities());
            normativeProbs.add(response.getAttackLocationResponse_initial().getNormativeProbs_cumulativeBayesian());
            for (Task_4_7_AttackLocationProbeResponseAfterINT intProbs : response.getAttackLocationResponses_afterINTs()) {
                subjectProbs.add(intProbs.getAttackLocationResponse().getGroupAttackProbabilities());
                normativeProbs.add(intProbs.getAttackLocationResponse().getNormativeProbs_cumulativeBayesian());
            }
            appendProbabilities(writer, subjectProbs, normativeProbs);

            //Write probability entry times
            i = 0;
            writeData(writer, response.getAttackLocationResponse_initial().getTrialPartTime_ms());
            i++;
            for (Task_4_7_AttackLocationProbeResponseAfterINT intResponse : response.getAttackLocationResponses_afterINTs()) {
                writeData(writer, intResponse.getAttackLocationResponse().getTrialPartTime_ms());
                i++;
            }
            for (; i < numProbabilitySets; i++) {
                writer.write("NaN" + separator);
            }

            //Write troop allocations
            i = 0;
            for (TroopAllocation allocation : response.getTroopAllocationResponse().getTroopAllocations()) {
                writeData(writer, allocation.getAllocation());
                i++;
            }
            for (; i < maxProbsInSet; i++) {
                writer.write("NaN" + separator);
            }
            i = 0;
            for (TroopAllocation allocation : response.getTroopAllocationResponse().getTroopAllocations()) {
                writeData(writer, allocation.getAllocation_raw());
                i++;
            }
            for (; i < maxProbsInSet; i++) {
                writer.write("NaN" + separator);
            }

            //Write troop allocation time
            writeData(writer, response.getTroopAllocationResponse().getTrialPartTime_ms());

            //Write NaNs for group circles and centers
            for (i = 0; i < 3; i++) {
                for (int j = 1; j <= numGroups; j++) {
                    writer.write("NaN" + separator);
                }
            }

            //Write NaNs for dispersion parameters
            if (appendDispersionParameters) {
                appendDispersionParameters(writer, null);
            }
            writer.write("NaN" + separator);

            //Write NaNs for distances
            if (appendDistances) {
                for (i = 0; i < maxProbsInSet; i++) {
                    writer.write("NaN" + separator);
                }
                for (i = 0; i < maxProbsInSet; i++) {
                    writer.write("NaN" + separator);
                }
            }
            writer.write("\n");
        }
    }

    /**
     * Appends a subject response for Task 5 to the given subject data CSV file writer.
     * 
     * @param writer
     * @param trial
     * @throws Exception
     */
    @SuppressWarnings({"deprecation", "unused"})
    protected static void appendTask_5_6_TrialResponse(Writer writer, Task_5_Trial trial) throws Exception {
        if (trial.getTrialResponse() != null) {
            Task_5_6_TrialResponse response = trial.getTrialResponse();
            //Trial_time, Ground_Truth, Surprise, Surprise_time, Probabilities_score, 
            //Allocation_score, Number_probability_sets, Number_groups, Max_probs_in_set,
            writeData(writer, response.getTrialTime_ms());
            if (trial.getGroundTruth() != null && trial.getGroundTruth().getResponsibleGroup() != null) {
                writeData(writer, trial.getGroundTruth().getResponsibleGroup());
            } else {
                writer.write("NaN" + separator);
            }
            if (response.getGroundTruthSurpriseResponse() != null) {
                writeData(writer, response.getGroundTruthSurpriseResponse().getSurpriseVal());
                writeData(writer, response.getGroundTruthSurpriseResponse().getTrialPartTime_ms());
            } else {
                writer.write("NaN" + separator);
                writer.write("NaN" + separator);
            }
            if (response.getResponseFeedBack() != null) {
                writeData(writer, response.getResponseFeedBack().getProbabilitiesScore_s1());
                writeData(writer, response.getResponseFeedBack().getTroopAllocationScore_s2());
            } else {
                writer.write("NaN" + separator);
                writer.write("NaN" + separator);
            }
            writer.write(Integer.toString(1 + response.getAttackLocationResponses_afterINTs().size()) + separator);
            writer.write(response.getTroopAllocationResponse().getTroopAllocations().size() + separator);
            writer.write(Integer.toString(maxProbsInSet) + separator);

            //Write Layer_type
            int i = 0;
            for (Task_5_6_AttackLocationProbeResponseAfterINT intResponse : response.getAttackLocationResponses_afterINTs()) {
                writeData(writer, formatLayerName(intResponse.getIntLayerShown().getLayerType()));
                i++;
            }
            for (; i < numProbabilitySets; i++) {
                writer.write("NaN" + separator);
            }

            if (appendBestLayers) {
                //Write Layer_Best_INT
                i = 0;
                for (Task_5_6_AttackLocationProbeResponseAfterINT intResponse : response.getAttackLocationResponses_afterINTs()) {
                    if (intResponse.getIntLayerExpectedUtilities() != null) {
                        writeData(writer, formatLayerName(getBestINT(intResponse.getIntLayerExpectedUtilities())));
                    } else {
                        writer.write("NaN" + separator);
                    }
                    i++;
                }
                for (; i < numProbabilitySets; i++) {
                    writer.write("NaN" + separator);
                }

                //Write Layer_Best_SIGINT
                i = 0;
                for (Task_5_6_AttackLocationProbeResponseAfterINT intResponse : response.getAttackLocationResponses_afterINTs()) {
                    if (intResponse.getIntLayerExpectedUtilities() != null) {
                        writeData(writer, formatLayerNames(getBestSIGINT(intResponse.getIntLayerExpectedUtilities())));
                    } else {
                        writer.write("NaN" + separator);
                    }
                    i++;
                }
                for (; i < numProbabilitySets; i++) {
                    writer.write("NaN" + separator);
                }
            }

            //Write Layer Information Gain
            if (appendLayerInformationGain) {
                i = 0;
                for (Task_5_6_AttackLocationProbeResponseAfterINT intResponse : response.getAttackLocationResponses_afterINTs()) {
                    if (intResponse.getIntLayerExpectedUtilities() != null) {
                        for (IntLayer layer : INT_LAYERS) {
                            INTLayerExpectedUtility utility = null;
                            for (INTLayerExpectedUtility u : intResponse.getIntLayerExpectedUtilities()) {
                                IntLayer uLayer = u.getLayerType();
                                if (uLayer.getLayerType() == layer.getLayerType()) {
                                    if (uLayer.getLayerType() == IntType.SIGINT) {
                                        if (((SigintLayer) uLayer).getGroup() == ((SigintLayer) layer).getGroup()) {
                                            utility = u;
                                            break;
                                        }
                                    } else {
                                        utility = u;
                                        break;
                                    }
                                }
                            }
                            if (i == 0) {
                                writeData(writer, utility != null ? utility.getExpectedUtility_cumulativeBayesian() : null);
                            } else {
                                writeData(writer, utility != null ? utility.getExpectedUtility_subjectProbs() : null);
                            }
                        }
                    } else {
                        for (int layerIndex = 0; layerIndex < INT_LAYERS.size(); layerIndex++) {
                            writer.write("NaN" + separator);
                        }
                    }
                    i++;
                }
                for (; i < numProbabilitySets; i++) {
                    for (int layerIndex = 0; layerIndex < INT_LAYERS.size(); layerIndex++) {
                        writer.write("NaN" + separator);
                    }
                }
            }

            //Write NaNs for Layer_Surprise
            i = 0;
            for (Task_5_6_AttackLocationProbeResponseAfterINT intResponse : response.getAttackLocationResponses_afterINTs()) {
                //writeData(writer, intResponse.getSurpriseReportResponse().getSurpriseVal());
                writer.write("NaN" + separator);
                i++;
            }
            for (; i < numProbabilitySets; i++) {
                writer.write("NaN" + separator);
            }

            //Write NaNs for Layer_Surprise_Time
            i = 0;
            for (Task_5_6_AttackLocationProbeResponseAfterINT intResponse : response.getAttackLocationResponses_afterINTs()) {
                //writeData(writer, intResponse.getSurpriseReportResponse().getTrialPartTime_ms());
                writer.write("NaN" + separator);
                i++;
            }
            for (; i < numProbabilitySets; i++) {
                writer.write("NaN" + separator);
            }

            //Write probabilities
            List<List<GroupAttackProbabilityResponse>> subjectProbs = new LinkedList<List<GroupAttackProbabilityResponse>>();
            List<List<Double>> normativeProbs = new LinkedList<List<Double>>();
            if (response.getAttackLocationResponse_initial() != null) {
                subjectProbs.add(response.getAttackLocationResponse_initial().getGroupAttackProbabilities());
                normativeProbs.add(response.getAttackLocationResponse_initial().getNormativeProbs_cumulativeBayesian());
            } else if (trial.getInitialHumintReport() != null) {
                ArrayList<GroupAttackProbabilityResponse> humintProbs = new ArrayList<GroupAttackProbabilityResponse>(
                        trial.getInitialHumintReport().getHumintProbabilities().size());
                int probIndex = 0;
                for (Double prob : trial.getInitialHumintReport().getHumintProbabilities()) {
                    humintProbs.add(new GroupAttackProbabilityResponse(
                            trial.getInitialHumintReport().getGroups().get(probIndex), prob, prob));
                    probIndex++;
                }
                subjectProbs.add(humintProbs);
                normativeProbs.add(trial.getInitialHumintReport().getHumintProbabilities());
            }
            for (Task_5_6_AttackLocationProbeResponseAfterINT intProbs : response.getAttackLocationResponses_afterINTs()) {
                subjectProbs.add(intProbs.getAttackLocationResponse().getGroupAttackProbabilities());
                normativeProbs.add(intProbs.getAttackLocationResponse().getNormativeProbs_cumulativeBayesian());
            }
            appendProbabilities(writer, subjectProbs, normativeProbs);

            //Write probability entry times
            i = 0;
            if (response.getAttackLocationResponse_initial() != null) {
                writeData(writer, response.getAttackLocationResponse_initial().getTrialPartTime_ms());
                i++;
            }
            for (Task_5_6_AttackLocationProbeResponseAfterINT intResponse : response.getAttackLocationResponses_afterINTs()) {
                writeData(writer, intResponse.getAttackLocationResponse().getTrialPartTime_ms());
                i++;
            }
            for (; i < numProbabilitySets; i++) {
                writer.write("NaN" + separator);
            }

            //Write troop allocations
            i = 0;
            for (TroopAllocation allocation : response.getTroopAllocationResponse().getTroopAllocations()) {
                writeData(writer, allocation.getAllocation());
                i++;
            }
            for (; i < maxProbsInSet; i++) {
                writer.write("NaN" + separator);
            }
            i = 0;
            for (TroopAllocation allocation : response.getTroopAllocationResponse().getTroopAllocations()) {
                writeData(writer, allocation.getAllocation_raw());
                i++;
            }
            for (; i < maxProbsInSet; i++) {
                writer.write("NaN" + separator);
            }

            //Write troop allocation time
            writeData(writer, response.getTroopAllocationResponse().getTrialPartTime_ms());

            //Write NaNs for group circles and centers
            for (i = 0; i < 3; i++) {
                for (int j = 1; j <= numGroups; j++) {
                    writer.write("NaN" + separator);
                }
            }

            //Write NaNs for dispersion parameters
            if (appendDispersionParameters) {
                appendDispersionParameters(writer, null);
            }
            writer.write("NaN" + separator);

            //Write NaNs for distances
            if (appendDistances) {
                for (i = 0; i < maxProbsInSet; i++) {
                    writer.write("NaN" + separator);
                }
                for (i = 0; i < maxProbsInSet; i++) {
                    writer.write("NaN" + separator);
                }
            }
            writer.write("\n");
        }
    }

    /**
     * Appends a subject response for Tasks7 to the given subject data CSV file writer.
     * Only timing data for Task 7 is currently appended
     *
     * @param writer
     * @param trial
     * @throws Exception
     */
    protected static void appendTask_7_TrialResponse(Writer writer, Task_7_Trial trial) throws Exception {
        if (trial.getTrialResponse() != null) {
            Task_7_TrialResponse response = trial.getTrialResponse();
            writeData(writer, response.getTrialTime_ms());
            if (trial.getGroundTruth() != null && trial.getGroundTruth().getResponsibleGroup() != null) {
                writeData(writer, trial.getGroundTruth().getResponsibleGroup());
            } else {
                writer.write("NaN" + separator);
            }
            writer.write("NaN" + separator);
            writer.write("NaN" + separator);
            /*if(response.getResponseFeedBack() != null) {
             writeData(writer, response.getResponseFeedBack().getProbabilitiesScore_s1());
             writeData(writer, response.getResponseFeedBack().getTroopAllocationScore_s2());
             } else {*/
            writer.write("NaN" + separator);
            writer.write("NaN" + separator);
            //}
            //writer.write(Integer.toString(1+response.getAttackLocationResponses_afterINTs().size()) + separator);
            writer.write("2" + separator);
            writer.write(response.getTroopAllocationResponse().getTroopAllocations().size() + separator);
            writer.write(Integer.toString(maxProbsInSet) + separator);

            //Write NaNs for Layer_type
            int i = 0;
            for (; i < numProbabilitySets; i++) {
                writer.write("NaN" + separator);
            }

            if (appendBestLayers) {
                //Write NaNs for Layer_Best_INT
                i = 0;
                for (; i < numProbabilitySets; i++) {
                    writer.write("NaN" + separator);
                }

                //Write NaNs for Layer_Best_SIGINT
                i = 0;
                for (; i < numProbabilitySets; i++) {
                    writer.write("NaN" + separator);
                }
            }

            if (appendLayerInformationGain) {
                //Write NaNs for Layer Information Gain
                i = 0;
                for (; i < numProbabilitySets; i++) {
                    for (int layerIndex = 0; layerIndex < INT_LAYERS.size(); layerIndex++) {
                        writer.write("NaN" + separator);
                    }
                }
            }

            //Write NaNs for Layer_Surprise
            i = 0;
            for (; i < numProbabilitySets; i++) {
                writer.write("NaN" + separator);
            }

            //Write NaNs for Layer_Surprise_Time
            i = 0;
            for (; i < numProbabilitySets; i++) {
                writer.write("NaN" + separator);
            }

            //Write probabilities
            List<List<GroupAttackProbabilityResponse>> subjectProbs = new LinkedList<List<GroupAttackProbabilityResponse>>();
            List<List<Double>> normativeProbs = new LinkedList<List<Double>>();
            if (response.getAttackLocationResponse_groups() != null) {
                subjectProbs.add(response.getAttackLocationResponse_groups().getGroupAttackProbabilities());
                normativeProbs.add(ProbabilityUtils.createProbabilities_Double(maxProbsInSet, null));
            }
            if (response.getAttackLocationResponse_locations() != null) {
                subjectProbs.add(response.getAttackLocationResponse_groups().getGroupAttackProbabilities());
                normativeProbs.add(ProbabilityUtils.createProbabilities_Double(maxProbsInSet, null));
            }
            appendProbabilities(writer, subjectProbs, normativeProbs);

            //Write probability entry times
            i = 0;
            if (response.getAttackLocationResponse_groups() != null) {
                writeData(writer, response.getAttackLocationResponse_groups().getTrialPartTime_ms());
                i++;
            }
            if (response.getAttackLocationResponse_locations() != null) {
                writeData(writer, response.getAttackLocationResponse_locations().getTrialPartTime_ms());
                i++;
            }
            for (; i < numProbabilitySets; i++) {
                writer.write("NaN" + separator);
            }

            //Write troop allocations
            i = 0;
            for (TroopAllocation allocation : response.getTroopAllocationResponse().getTroopAllocations()) {
                writeData(writer, allocation.getAllocation());
                i++;
            }
            for (; i < maxProbsInSet; i++) {
                writer.write("NaN" + separator);
            }
            i = 0;
            for (TroopAllocation allocation : response.getTroopAllocationResponse().getTroopAllocations()) {
                writeData(writer, allocation.getAllocation_raw());
                i++;
            }
            for (; i < maxProbsInSet; i++) {
                writer.write("NaN" + separator);
            }

            //Write troop allocation time
            writeData(writer, response.getTroopAllocationResponse().getTrialPartTime_ms());

            //Write NaNs for group circles and centers
            for (i = 0; i < 3; i++) {
                for (int j = 1; j <= numGroups; j++) {
                    writer.write("NaN" + separator);
                }
            }

            //Write NaNs for dispersion parameters
            if (appendDispersionParameters) {
                appendDispersionParameters(writer, null);
            }
            writer.write("NaN" + separator);

            //Write NaNs for distances
            if (appendDistances) {
                for (i = 0; i < maxProbsInSet; i++) {
                    writer.write("NaN" + separator);
                }
                for (i = 0; i < maxProbsInSet; i++) {
                    writer.write("NaN" + separator);
                }
            }
            writer.write("\n");
        }
    }

    public static String formatLayerName(IntLayer layer) {
        if (layer != null) {
            if (layer instanceof SigintLayer) {
                SigintLayer sigintLayer = (SigintLayer) layer;
                return layer.getLayerType() + "-" + sigintLayer.getGroup().toString();
            } else {
                return layer.getLayerType().toString();
            }
        }
        return null;
    }

    public static String formatLayerNames(Collection<? extends IntLayer> layers) {
        if (layers != null && !layers.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            int i = 0;
            for (IntLayer layer : layers) {
                if (layer instanceof SigintLayer) {
                    SigintLayer sigintLayer = (SigintLayer) layer;
                    sb.append(layer.getLayerType() + "-" + sigintLayer.getGroup().toString());
                } else {
                    sb.append(layer.getLayerType().toString());
                }
                if (i < layers.size() - 1) {
                    sb.append(" ");
                }
                i++;
            }
            return sb.toString();
        }
        return null;
    }

    /**
     * Get the best INT layer to choose based on the expected utility of each
     * layer computed using the subject/model probabilities.
     *
     * @param intLayerUtilities
     * @return
     */
    protected static IntLayer getBestINT(ArrayList<INTLayerExpectedUtility> intLayerUtilities) {
        IntLayer bestInt = null;
        double maxUtility = Double.MIN_VALUE;
        if (intLayerUtilities != null && !intLayerUtilities.isEmpty()) {
            for (INTLayerExpectedUtility intLayerUtility : intLayerUtilities) {
                Double utility = null;
                if (intLayerUtility.getExpectedUtility_subjectProbs() != null) {
                    utility = intLayerUtility.getExpectedUtility_subjectProbs();
                } else {
                    utility = intLayerUtility.getExpectedUtility_cumulativeBayesian();
                }
                if (utility != null
                        && utility > maxUtility) {
                    bestInt = intLayerUtility.getLayerType();
                    maxUtility = utility;
                }
            }
        }
        return bestInt;
    }

    /**
     * Get the best SIGINT type(s) (on group A, B, C, or D) to choose based on
     * the expected utility of each layer computed using the subject/model
     * probabilities. Multiple types may be returned if their utilities are
     * within 0.001 of each other.
     *
     * @param intLayerUtilities
     * @return
     */
    protected static List<SigintLayer> getBestSIGINT(ArrayList<INTLayerExpectedUtility> intLayerUtilities) {
        //SigintLayer bestSigint = null;
        List<SigintLayer> bestSigints = new LinkedList<SigintLayer>();
        double maxUtility = Double.MIN_VALUE;
        if (intLayerUtilities != null && !intLayerUtilities.isEmpty()) {
            for (INTLayerExpectedUtility intLayerUtility : intLayerUtilities) {
                if (intLayerUtility.getLayerType() instanceof SigintLayer
                        && intLayerUtility.getExpectedUtility_subjectProbs() != null
                        && intLayerUtility.getExpectedUtility_subjectProbs() > maxUtility) {
                    Double utility = null;
                    if (intLayerUtility.getExpectedUtility_subjectProbs() != null) {
                        utility = intLayerUtility.getExpectedUtility_subjectProbs();
                    } else {
                        utility = intLayerUtility.getExpectedUtility_cumulativeBayesian();
                    }
                    if (utility != null && utility > maxUtility) {
                        //bestSigint = (SigintLayer)intLayerUtility.getLayerType();
                        maxUtility = utility;
                    }
                }
            }
            for (INTLayerExpectedUtility intLayerUtility : intLayerUtilities) {
                if (intLayerUtility.getLayerType() instanceof SigintLayer) {
                    Double utility = null;
                    if (intLayerUtility.getExpectedUtility_subjectProbs() != null) {
                        utility = intLayerUtility.getExpectedUtility_subjectProbs();
                    } else {
                        utility = intLayerUtility.getExpectedUtility_cumulativeBayesian();
                    }
                    if (utility != null && utility + 0.001 >= maxUtility) {
                        bestSigints.add((SigintLayer) intLayerUtility.getLayerType());
                    }
                }
            }
			//DEBUG CODE
			/*if(bestSigints.size() > 1) {
             List<Double> utilities = new LinkedList<Double>();
             for(INTLayerExpectedUtility intLayerUtility : intLayerUtilities) {
             if(intLayerUtility.getLayerType() instanceof SigintLayer) {						
             utilities.add(intLayerUtility.getExpectedUtility_subjectProbs());
             }
             }
             System.out.println(utilities);
             }*/
            //END DEBUG CODE
        }
        return bestSigints;
    }

    protected static void writeData(Writer writer, Object data) throws IOException {
        writer.write((data == null) ? "NaN" : data.toString());
        writer.write(separator);
    }

    protected static void appendProbabilities(Writer writer, List<List<GroupAttackProbabilityResponse>> subjectProbs,
            List<List<Double>> normativeProbs) throws Exception {
        //Write normalized subject probabilities
        int j = 0;
        for (List<GroupAttackProbabilityResponse> probs : subjectProbs) {
            int i = 0;
            if (probs != null) {
                for (GroupAttackProbabilityResponse prob : probs) {
                    writeData(writer, prob.getProbability());
                    i++;
                }
            }
            for (; i < maxProbsInSet; i++) {
                writer.write("NaN" + separator);
            }
            j++;
        }
        for (; j < numProbabilitySets; j++) {
            for (int k = 0; k < maxProbsInSet; k++) {
                writer.write("NaN" + separator);
            }
        }

        //Write "raw" subject probabilities
        j = 0;
        for (List<GroupAttackProbabilityResponse> probs : subjectProbs) {
            int i = 0;
            if (probs != null) {
                for (GroupAttackProbabilityResponse prob : probs) {
                    writeData(writer, prob.getRawProbability());
                    i++;
                }
            }
            for (; i < maxProbsInSet; i++) {
                writer.write("NaN" + separator);
            }
            j++;
        }
        for (; j < numProbabilitySets; j++) {
            for (int k = 0; k < maxProbsInSet; k++) {
                writer.write("NaN" + separator);
            }
        }

        //Write normative probabilities
        j = 0;
        for (List<Double> probs : normativeProbs) {
            int i = 0;
            if (probs != null) {
                for (Double prob : probs) {
                    writeData(writer, prob);
                    i++;
                }
            }
            for (; i < maxProbsInSet; i++) {
                writer.write("NaN" + separator);
            }
            j++;
        }
        for (; j < numProbabilitySets; j++) {
            for (int k = 0; k < maxProbsInSet; k++) {
                writer.write("NaN" + separator);
            }
        }
    }

    protected static void appendDispersionParameters(Writer writer, ArrayList<Task_1_2_3_AttackDispersionParameters> parameters) throws IOException {
        if (parameters != null) {
            int i = 0;
            for (Task_1_2_3_AttackDispersionParameters params : parameters) {
                writeData(writer, params.getCenterLocation().getX());
                i++;
            }
            for (; i < numGroups; i++) {
                writer.write("NaN" + separator);
            }
            i = 0;
            for (Task_1_2_3_AttackDispersionParameters params : parameters) {
                writeData(writer, params.getCenterLocation().getY());
                i++;
            }
            for (; i < numGroups; i++) {
                writer.write("NaN" + separator);
            }
            i = 0;
            for (Task_1_2_3_AttackDispersionParameters params : parameters) {
                writeData(writer, params.getBaseRate());
                i++;
            }
            for (; i < numGroups; i++) {
                writer.write("NaN" + separator);
            }
            i = 0;
            for (Task_1_2_3_AttackDispersionParameters params : parameters) {
                if (params.getSigmaX() != null) {
                    writeData(writer, params.getSigmaX());
                    i++;
                }
            }
            for (; i < numGroups; i++) {
                writer.write("NaN" + separator);
            }
            i = 0;
            for (Task_1_2_3_AttackDispersionParameters params : parameters) {
                if (params.getSigmaY() != null) {
                    writeData(writer, params.getSigmaY());
                    i++;
                }
            }
            for (; i < numGroups; i++) {
                writer.write("NaN" + separator);
            }
        } else {
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < numGroups; j++) {
                    writer.write("NaN" + separator);
                }
            }
        }
    }   

    /**
     * Aggregates human subject data for the Final Exam.
     */
    public static void processFinalExamSubjects() {
        boolean processAllSubjects = true;
        boolean processStandardOrderSubjects = false;
        boolean processReverseLayerSubjects = false;
        try {
            Set<IcarusSubjectData> standardOrderSubjects = null;
            if (processAllSubjects || processStandardOrderSubjects) {
                standardOrderSubjects = DataAggregatorUtils.getSubjectsInFolder(
                        new File(BatchFileProcessor.assessmentFolder + "Final_Exam/Human_Data/Standard Layer Order Subjects"), true);
            }
            if (processAllSubjects) {
                //For Missions 1-5, aggregate all subjects (both standard and reverse layer order, N=103)
                Set<IcarusSubjectData> allSubjects = DataAggregatorUtils.getSubjectsInFolder(
                        new File(BatchFileProcessor.assessmentFolder + "Final_Exam/Human_Data/All Subjects"), true);
                System.out.println("All Subjects: " + allSubjects.size());
                SubjectDataCsvAggregator_Matlab.aggregateSubjectData(
                        new File(BatchFileProcessor.assessmentFolder + "Final_Exam/Human_Data/All Subjects"),
                        new File(BatchFileProcessor.assessmentFolder + "Final_Exam/Human_Data/Aggregated Data/All Subjects (N=103)"),
                        new TreeSet<String>(Arrays.asList("Final-Exam-1")), true,
                        //new ArrayList<Integer>(Arrays.asList(7)),
                        new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5)),
                        allSubjects, true);

                //For Missions 6-7, only aggregate subjects who took the exam in the standard layer order (N=79)
                System.out.println("Standard Order Subjects: " + standardOrderSubjects.size());
                SubjectDataCsvAggregator_Matlab.aggregateSubjectData(
                        new File(BatchFileProcessor.assessmentFolder + "Final_Exam/Human_Data/Standard Layer Order Subjects"),
                        new File(BatchFileProcessor.assessmentFolder + "Final_Exam/Human_Data/Aggregated Data/All Subjects (N=103)"),
                        new TreeSet<String>(Arrays.asList("Final-Exam-1")), true,
                        new ArrayList<Integer>(Arrays.asList(6, 7)),
                        standardOrderSubjects, true);
            }
            if (processStandardOrderSubjects) {
                //Generate aggregate data files for subjects who took the exam with the layers in standard order (N=79)
                System.out.println("Standard Order Subjects: " + standardOrderSubjects.size());
                SubjectDataCsvAggregator_Matlab.aggregateSubjectData(
                        new File(BatchFileProcessor.assessmentFolder + "Final_Exam/Human_Data/Standard Layer Order Subjects"),
                        new File(BatchFileProcessor.assessmentFolder + "Final_Exam/Human_Data/Aggregated Data/Standard Layer Subjects (N=79)"),
                        new TreeSet<String>(Arrays.asList("Final-Exam-1")), true,
                        //new ArrayList<Integer>(Arrays.asList(6)),
                        new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6, 7)),
                        standardOrderSubjects, true);
            }
            if (processReverseLayerSubjects) {
                //Generate aggregate data files for subjects who took the exam with the layers in reverse order (N=24)
                Set<IcarusSubjectData> reverseLayerSubjects = DataAggregatorUtils.getSubjectsInFolder(
                        new File(BatchFileProcessor.assessmentFolder + "Final_Exam/Human_Data/Reverse Layer Order Subjects"), true);
                System.out.println("Reverse Layer Subjects: " + reverseLayerSubjects.size());
                SubjectDataCsvAggregator_Matlab.aggregateSubjectData(
                        new File(BatchFileProcessor.assessmentFolder + "Final_Exam/Human_Data/Reverse Layer Order Subjects"),
                        new File(BatchFileProcessor.assessmentFolder + "Final_Exam/Human_Data/Aggregated Data/Reverse Layer Subjects (N=24)"),
                        new TreeSet<String>(Arrays.asList("Final-Exam-1")), true,
                        //new ArrayList<Integer>(Arrays.asList(6)),
                        new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6, 7)),
                        reverseLayerSubjects, true);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Test main
     */
    public static void main(String[] args) {
        try {
            processFinalExamSubjects();

            //System.out.println(TaskFileAttributes.parseTaskFileAttributes("MITRE_001_Pilot Exam_Task5_Trial1_07312012")); //S001_Pilot Exam_Task5 or S_MITRE_001_Pilot Exam_Task5 or MITRE_001_Pilot Exam_Task5_Trial1_07312012			
            /*Set<IcarusSubjectData> allSubjects = SubjectDataCsvAggregator_OLD.getSubjectsInFolder(
             new File("data/response_data/Pilot-Exam-TH-Test/HRL"), false);
             System.out.println("All Subjects: " + allSubjects.size());
             SubjectDataCsvAggregator_OLD.aggregateSubjectData(
             new File("data/response_data/Pilot-Exam-TH-Test/HRL"), 
             new File("data/response_data/Pilot-Exam-TH-Test/HRL/Aggregated Data"), 
             new TreeSet<String>(Arrays.asList("PilotExam-TH-Test")), false,
             //new ArrayList<Integer>(Arrays.asList(7)),
             new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6, 7)), 
             allSubjects, true);*/
            /*Set<IcarusSubjectData> allSubjects = SubjectDataCsvAggregator.getSubjectsInFolder(
             new File("data/response_data/March 2012 Pilot/_Scored Tasks"), true);
             System.out.println("All Subjects: " + allSubjects.size());
             SubjectDataCsvAggregator.aggregateSubjectData(
             new File("data/response_data/March 2012 Pilot/_Scored Tasks"), 
             new File("data/response_data/March 2012 Pilot/Aggregated Data"), 
             new TreeSet<String>(Arrays.asList("Pilot Exam")), true,
             //new ArrayList<Integer>(Arrays.asList(7)),
             new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6, 7)), 
             allSubjects, true);*/
			//"S115", "S116", "S117", 
            //"SM112", "SM113", "SM114", "SM115", "SM116", "SM117", "SM118", "SM119", "SM120", 
            //"SM122", "SM123", "SM124", "SM125"));
			/*Set<IcarusSubjectData> correctGroupCenterSubjects = new TreeSet<IcarusSubjectData>(Arrays.asList(
             new IcarusSubjectData("115", new Site("MITRE")), new IcarusSubjectData("116", new Site("MITRE")),
             new IcarusSubjectData("117", new Site("MITRE")), new IcarusSubjectData("M112", new Site("MITRE")),
             new IcarusSubjectData("M113", new Site("MITRE")), new IcarusSubjectData("M114", new Site("MITRE")),
             new IcarusSubjectData("M115", new Site("MITRE")), new IcarusSubjectData("M116", new Site("MITRE")),
             new IcarusSubjectData("M117", new Site("MITRE")), new IcarusSubjectData("M118", new Site("MITRE")),
             new IcarusSubjectData("M119", new Site("MITRE")), new IcarusSubjectData("M120", new Site("MITRE")),
             new IcarusSubjectData("M122", new Site("MITRE")), new IcarusSubjectData("M123", new Site("MITRE")),
             new IcarusSubjectData("M124", new Site("MITRE")), new IcarusSubjectData("M125", new Site("MITRE"))));					
             System.out.println("Correct Group Center Subjects: " + correctGroupCenterSubjects.size());
             SubjectDataCsvAggregator.aggregateSubjectData(
             new File("data/response_data/March 2012 Pilot/_Scored Tasks"), 
             new File("data/response_data/March 2012 Pilot/Aggregated Data/Subjects_With_Correct_Group_Centers"), 
             new TreeSet<String>(Arrays.asList("Pilot Exam")), true,
             new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6, 7)),					 
             correctGroupCenterSubjects, true);*/
            //Set<IcarusSubjectData> reverseLayerSubjects = SubjectDataCsvAggregator.getSubjectsInFolder(
            //		new File("data/response_data/May 2012 Mission 6 Reverse Layers Pilot"), true);
			/*Set<IcarusSubjectData> reverseLayerSubjects = new TreeSet<IcarusSubjectData>(Arrays.asList(
             new IcarusSubjectData("M126", new Site("MITRE")),
             new IcarusSubjectData("M127", new Site("MITRE")),
             new IcarusSubjectData("M128", new Site("MITRE")),
             new IcarusSubjectData("M129", new Site("MITRE")),
             new IcarusSubjectData("M130", new Site("MITRE"))));
             System.out.println("Reverse Layer Subjects: " + reverseLayerSubjects);
             SubjectDataCsvAggregator.aggregateSubjectData(
             new File("data/response_data/May 2012 Mission 6 Reverse Layers Pilot"), 
             new File("data/response_data/May 2012 Mission 6 Reverse Layers Pilot/Aggregated Data"), 
             new TreeSet<String>(Arrays.asList("Pilot-Exam")), true,
             new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6)), 
             reverseLayerSubjects, true);*/
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected static class TaskFile {

        BufferedWriter writer;
    }
}
