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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;

import org.mitre.icarus.cps.app.util.CPSUtils;
import org.mitre.icarus.cps.app.util.ProbabilityUtils;
import org.mitre.icarus.cps.assessment.assessment_processor.phase_1.BatchFileProcessor;
import org.mitre.icarus.cps.assessment.score_computer.ScoreComputerBase.ProbabilityType;
import org.mitre.icarus.cps.assessment.score_computer.phase_1.ScoreComputer;
import org.mitre.icarus.cps.assessment.score_computer.phase_1.ScoreComputerException;
import org.mitre.icarus.cps.exam.phase_1.IcarusExam_Phase1;
import org.mitre.icarus.cps.exam.phase_1.loader.IcarusExamLoader_Phase1;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.INTLayerExpectedUtility;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.Task_5_6_AttackLocationProbeResponseAfterINT;
import org.mitre.icarus.cps.exam.phase_1.testing.AttackLocationPresentationTrial;
import org.mitre.icarus.cps.exam.phase_1.testing.TaskTestPhase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_1_2_3_AttackDispersionParameters;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_1_2_3_PhaseBase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_1_2_3_ProbeTrialBase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_1_2_3_TrialBlockBase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_1_ProbeTrial;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_2_ProbeTrial;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_3_Phase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_3_ProbeTrial;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_4_Phase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_4_Trial;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_5_Phase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_5_Trial;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_6_Phase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_6_Trial;
import org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.HumintLayer;
import org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.INTLayerPresentationProbeBase;
import org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.ImintLayer;
import org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.IntLayer;
import org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.MovintLayer;
import org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.SigintLayer;
import org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.SocintLayer;
import org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.Task_5_6_INTLayerPresentationProbe;
import org.mitre.icarus.cps.exam.phase_1.testing.probability_rules.ProbabilityRules;
import org.mitre.icarus.cps.exam.phase_1.testing.probability_rules.update_strategy.AbUpdateStrategy;
import org.mitre.icarus.cps.exam.phase_1.testing.probability_rules.update_strategy.BayesianUpdateStrategy;
import org.mitre.icarus.cps.exam.phase_1.testing.probability_rules.update_strategy.ProbabilityUpdateStrategy;
import org.mitre.icarus.cps.feature_vector.phase_1.GridLocation2D;
import org.mitre.icarus.cps.feature_vector.phase_1.GridSize;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupCenter;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupType;
import org.mitre.icarus.cps.feature_vector.phase_1.IntType;
import org.mitre.icarus.cps.feature_vector.phase_1.road_network.RoadDistanceCalculator;

/**
 * Creates CSV files with exam parameters (e.g., probabilities for each trial)
 * for use in test models and exam design assessment.
 *
 * @author CBONACETO
 *
 */
public class ExamParamtersFileAggregator {

    public static final int LIKELIHOODS = 0;

    public static final int POSTERIORS = 1;

    /**
     * The separator character to use when writing files
     */
    protected static String separator = ",";

    /**
     * * Generative model parameters for Tasks 1-3 for Pilot Exam **
     */
    /**
     * Parameters used in the Gaussian generative model to generate data for
     * Tasks 1
     */
    private static EnumMap<GroupType, Task_1_2_3_AttackDispersionParameters> TASK_1_PARAMETERS_PILOT;
    /**
     * Parameters used in the Gaussian generative model to generate data for
     * Tasks 2
     */
    private static EnumMap<GroupType, Task_1_2_3_AttackDispersionParameters> TASK_2_PARAMETERS_PILOT;
    /**
     * Parameters used in the Gaussian generative model to generate data for
     * Tasks 3
     */
    private static EnumMap<GroupType, Task_1_2_3_AttackDispersionParameters> TASK_3_PARAMETERS_PILOT;

    //Initialize the generative model parameters for Tasks 1-3	

    static {
        TASK_1_PARAMETERS_PILOT = new EnumMap<GroupType, Task_1_2_3_AttackDispersionParameters>(GroupType.class);
        TASK_1_PARAMETERS_PILOT.put(GroupType.A, new Task_1_2_3_AttackDispersionParameters(GroupType.A, 0.67d, new GridLocation2D(50, 50),
                20, 20, 0));
        TASK_1_PARAMETERS_PILOT.put(GroupType.B, new Task_1_2_3_AttackDispersionParameters(GroupType.B, 0.33d, new GridLocation2D(30, 30),
                10, 10, 0));

        TASK_2_PARAMETERS_PILOT = new EnumMap<GroupType, Task_1_2_3_AttackDispersionParameters>(GroupType.class);
        TASK_2_PARAMETERS_PILOT.put(GroupType.A, new Task_1_2_3_AttackDispersionParameters(GroupType.A, 0.10d, new GridLocation2D(50, 50),
                20, 20, 0));
        TASK_2_PARAMETERS_PILOT.put(GroupType.B, new Task_1_2_3_AttackDispersionParameters(GroupType.B, 0.20d, new GridLocation2D(25, 25),
                5, 5, 0));
        TASK_2_PARAMETERS_PILOT.put(GroupType.C, new Task_1_2_3_AttackDispersionParameters(GroupType.C, 0.30d, new GridLocation2D(75, 75),
                20, 20, 0));
        TASK_2_PARAMETERS_PILOT.put(GroupType.D, new Task_1_2_3_AttackDispersionParameters(GroupType.D, 0.40d, new GridLocation2D(75, 25),
                15, 15, 0));

        TASK_3_PARAMETERS_PILOT = new EnumMap<GroupType, Task_1_2_3_AttackDispersionParameters>(GroupType.class);
        TASK_3_PARAMETERS_PILOT.put(GroupType.A, new Task_1_2_3_AttackDispersionParameters(GroupType.A, 0.10d, new GridLocation2D(60, 30)));
        TASK_3_PARAMETERS_PILOT.put(GroupType.B, new Task_1_2_3_AttackDispersionParameters(GroupType.B, 0.20d, new GridLocation2D(43, 69)));
        TASK_3_PARAMETERS_PILOT.put(GroupType.C, new Task_1_2_3_AttackDispersionParameters(GroupType.C, 0.30d, new GridLocation2D(29, 28)));
        TASK_3_PARAMETERS_PILOT.put(GroupType.D, new Task_1_2_3_AttackDispersionParameters(GroupType.D, 0.40d, new GridLocation2D(64, 81)));
    }
    /**
     * *************************************************************
     */

    /**
     * * Generative model parameters for Tasks 1-3 for Final Exam **
     */
    /**
     * Parameters used in the Gaussian generative model to generate data for
     * Tasks 1
     */
    private static EnumMap<GroupType, Task_1_2_3_AttackDispersionParameters> TASK_1_PARAMETERS_FINAL;
    /**
     * Parameters used in the Gaussian generative model to generate data for
     * Tasks 2
     */
    private static EnumMap<GroupType, Task_1_2_3_AttackDispersionParameters> TASK_2_PARAMETERS_FINAL;

    /**
     * Parameters used in the Gaussian generative model to generate data for
     * Tasks 3
     */
	//private static EnumMap<GroupType, Task_1_2_3_AttackDispersionParameters> TASK_3_PARAMETERS_FINAL;	
    //Initialize the generative model parameters for Tasks 1-3	
    static {
        TASK_1_PARAMETERS_FINAL = new EnumMap<GroupType, Task_1_2_3_AttackDispersionParameters>(GroupType.class);
        TASK_1_PARAMETERS_FINAL.put(GroupType.A, new Task_1_2_3_AttackDispersionParameters(GroupType.A, 0.6d, new GridLocation2D(60, 60),
                10, 10, 0));
        TASK_1_PARAMETERS_FINAL.put(GroupType.B, new Task_1_2_3_AttackDispersionParameters(GroupType.B, 0.4d, new GridLocation2D(50, 50),
                20, 20, 0));

        TASK_2_PARAMETERS_FINAL = new EnumMap<GroupType, Task_1_2_3_AttackDispersionParameters>(GroupType.class);
        TASK_2_PARAMETERS_FINAL.put(GroupType.A, new Task_1_2_3_AttackDispersionParameters(GroupType.A, 0.40d, new GridLocation2D(50, 50),
                20, 20, 0));
        TASK_2_PARAMETERS_FINAL.put(GroupType.B, new Task_1_2_3_AttackDispersionParameters(GroupType.B, 0.10d, new GridLocation2D(70, 30),
                5, 5, 0));
        TASK_2_PARAMETERS_FINAL.put(GroupType.C, new Task_1_2_3_AttackDispersionParameters(GroupType.C, 0.20d, new GridLocation2D(30, 30),
                10, 10, 0));
        TASK_2_PARAMETERS_FINAL.put(GroupType.D, new Task_1_2_3_AttackDispersionParameters(GroupType.D, 0.30d, new GridLocation2D(70, 70),
                15, 15, 0));

        /*TASK_3_PARAMETERS_FINAL = new EnumMap<GroupType, Task_1_2_3_AttackDispersionParameters>(GroupType.class);
         TASK_3_PARAMETERS_FINAL.put(GroupType.A, new Task_1_2_3_AttackDispersionParameters(GroupType.A, 0.10d, new GridLocation2D(60, 30)));
         TASK_3_PARAMETERS_FINAL.put(GroupType.B, new Task_1_2_3_AttackDispersionParameters(GroupType.B, 0.20d, new GridLocation2D(43, 69)));
         TASK_3_PARAMETERS_FINAL.put(GroupType.C, new Task_1_2_3_AttackDispersionParameters(GroupType.C, 0.30d, new GridLocation2D(29, 28)));
         TASK_3_PARAMETERS_FINAL.put(GroupType.D, new Task_1_2_3_AttackDispersionParameters(GroupType.D, 0.40d, new GridLocation2D(64, 81)));*/
    }

    /**
     * *************************************************************
     */

    /**
     * @param task
     * @param normativeProbsFile
     * @param attackHistoryFile
     * @param centersAndSigmasFile
     * @param distancesFile
     * @param layerUtilitiesFile
     * @param numStages
     * @param writeHeader
     * @param rules
     * @param gridSize
     * @throws IOException
     */
    public static void createModelTestInputFiles(TaskTestPhase<?> task,
            File normativeLikelihoodsFile, File normativePosteriorsFile,
            File attackHistoryFile, File centersAndSigmasFile,
            File distancesFile,
            int numStages, boolean writeHeader,
            ProbabilityRules rules, GridSize gridSize) throws IOException {
        if (task instanceof Task_1_2_3_PhaseBase) {
            if (normativeLikelihoodsFile != null) {
                createProbabilitiesFile((Task_1_2_3_PhaseBase<?>) task, normativeLikelihoodsFile, writeHeader,
                        rules, gridSize, LIKELIHOODS, false);
            }
            if (normativePosteriorsFile != null) {
                createProbabilitiesFile((Task_1_2_3_PhaseBase<?>) task, normativePosteriorsFile, writeHeader,
                        rules, gridSize, POSTERIORS, false);
            }
            if (attackHistoryFile != null) {
                createAttackHistoryFile((Task_1_2_3_PhaseBase<?>) task, attackHistoryFile, writeHeader);
            }
            if (centersAndSigmasFile != null) {
                createCentersAndSigmasFile((Task_1_2_3_PhaseBase<?>) task, centersAndSigmasFile, writeHeader, gridSize);
            }
        } else if (task instanceof Task_4_Phase) {
            if (normativeLikelihoodsFile != null) {
                createProbabilitiesFile((Task_4_Phase) task, normativeLikelihoodsFile, numStages, writeHeader,
                        rules, rules, gridSize, LIKELIHOODS);
            }
            if (normativePosteriorsFile != null) {
                createProbabilitiesFile((Task_4_Phase) task, normativePosteriorsFile, numStages, writeHeader,
                        rules, rules, gridSize, POSTERIORS);
            }
            if (distancesFile != null) {
                createDistancesFile((Task_4_Phase) task, distancesFile, writeHeader, gridSize);
            }
        } else if (task instanceof Task_6_Phase) {
            if (normativeLikelihoodsFile != null) {
                createProbabilitiesFile((Task_6_Phase) task, normativeLikelihoodsFile, numStages, writeHeader,
                        rules, rules, gridSize, LIKELIHOODS);
            }
            if (normativePosteriorsFile != null) {
                createProbabilitiesFile((Task_6_Phase) task, normativePosteriorsFile, numStages, writeHeader,
                        rules, rules, gridSize, POSTERIORS);
            }
            if (distancesFile != null) {
                createDistancesFile((Task_6_Phase) task, distancesFile, writeHeader, gridSize);
            }
        } else if (task instanceof Task_5_Phase) {
            if (normativeLikelihoodsFile != null) {
                createProbabilitiesFile((Task_5_Phase) task, normativeLikelihoodsFile, numStages, writeHeader,
                        rules, rules, gridSize, LIKELIHOODS);
            }
            if (normativePosteriorsFile != null) {
                createProbabilitiesFile((Task_5_Phase) task, normativePosteriorsFile, numStages, writeHeader,
                        rules, rules, gridSize, POSTERIORS);
            }
            if (distancesFile != null) {
                createDistancesFile((Task_5_Phase) task, distancesFile, writeHeader, gridSize);
            }
        }
    }

    /**
     * @param task
     * @param file
     * @param writeHeader
     * @param rules
     * @param gridSize
     * @param probabilityType
     * @param alwaysUseFinalTrialDispersionParameters
     * @throws IOException
     */
    public static void createProbabilitiesFile(Task_1_2_3_PhaseBase<?> task, File file,
            boolean writeHeader, ProbabilityRules rules, GridSize gridSize, int probabilityType,
            boolean alwaysUseFinalTrialDispersionParameters) throws IOException {
        if (task.getTrialBlocks() != null && !task.getTrialBlocks().isEmpty()) {
            //rules.setProbabilityUpdateStrategy(new BayesianUpdateStrategy());
            FileWriter writer = new FileWriter(file);
            if (writeHeader) {
                writeGroupsFileHeader(writer,
                        task.getTrialBlocks().get(0).getProbeTrial().getAttackLocationProbe().getGroups(),
                        probabilityType == POSTERIORS);
            }
            RoadDistanceCalculator distanceCalculator = null;
            if (task instanceof Task_3_Phase) {
                distanceCalculator = new RoadDistanceCalculator(((Task_3_Phase) task).getRoads(), gridSize);
            }
            int trialNum = 1;
            ArrayList<AttackLocationPresentationTrial> groupAttacks = new ArrayList<AttackLocationPresentationTrial>();
            EnumMap<GroupType, Task_1_2_3_AttackDispersionParameters> dispersionParameters = null;
            if (alwaysUseFinalTrialDispersionParameters) {
                /*if(task instanceof Task_1_Phase) {
                 //dispersionParameters = TASK_1_PARAMETERS_PILOT;
                 dispersionParameters = TASK_1_PARAMETERS_FINAL;
                 } else if(task instanceof Task_2_Phase) {
                 //dispersionParameters = TASK_2_PARAMETERS_PILOT;
                 dispersionParameters = TASK_2_PARAMETERS_FINAL;
                 } else {
                 dispersionParameters = TASK_3_PARAMETERS_PILOT;
                 }*/
                Task_1_2_3_TrialBlockBase lastBlock = task.getTrialBlocks().get(task.getTrialBlocks().size() - 1);
                if (lastBlock != null && lastBlock.getProbeTrial() != null && lastBlock.getProbeTrial().getAttackDispersionParameters() != null) {
                    dispersionParameters = new EnumMap<GroupType, Task_1_2_3_AttackDispersionParameters>(GroupType.class);
                    for (Task_1_2_3_AttackDispersionParameters parameters : lastBlock.getProbeTrial().getAttackDispersionParameters()) {
                        dispersionParameters.put(parameters.getGroup(), parameters);
                    }
                }
            }
            for (Task_1_2_3_TrialBlockBase trialBlock : task.getTrialBlocks()) {
                if (trialBlock.getGroupAttackPresentations() != null) {
                    for (AttackLocationPresentationTrial groupAttack : trialBlock.getGroupAttackPresentations()) {
                        groupAttacks.add(groupAttack);
                    }
                }
                Task_1_2_3_ProbeTrialBase trial = trialBlock.getProbeTrial();
                try {
                    ArrayList<Double> likelihoods = null;
                    /*if(alwaysUseFinalTrialDispersionParameters && dispersionParameters != null) {
                     //Set base rate to current base rate
                     if(trial.getAttackDispersionParameters() != null) {
                     for(Task_1_2_3_AttackDispersionParameters parameters : trial.getAttackDispersionParameters()) {
                     dispersionParameters.get(parameters.getGroup()).setBaseRate(parameters.getBaseRate());							
                     }
                     }
                     } else*/
                    if ((!alwaysUseFinalTrialDispersionParameters || dispersionParameters == null) && trial.getAttackDispersionParameters() != null) {
                        dispersionParameters = new EnumMap<GroupType, Task_1_2_3_AttackDispersionParameters>(GroupType.class);
                        for (Task_1_2_3_AttackDispersionParameters parameters : trial.getAttackDispersionParameters()) {
                            dispersionParameters.put(parameters.getGroup(), parameters);
                        }
                    }
                    if (trial instanceof Task_1_ProbeTrial || trial instanceof Task_2_ProbeTrial) {
                        if (dispersionParameters != null) {
                            likelihoods = ScoreComputer.computeGroupProbabilities_Task_1_2(
                                    trial.getAttackLocationProbe().getAttackLocation(),
                                    trial.getAttackLocationProbe().getGroups(), rules, dispersionParameters);
                        } else {
                            likelihoods = ScoreComputer.computeGroupProbabilities_Task_1_2(
                                    trial.getAttackLocationProbe().getAttackLocation(),
                                    trial.getAttackLocationProbe().getGroups(), rules, groupAttacks);
                        }
                    } else if (trial instanceof Task_3_ProbeTrial) {
                        if (dispersionParameters != null) {
                            likelihoods = ScoreComputer.computeGroupProbabilities_Task_3(
                                    trial.getAttackLocationProbe().getAttackLocation(),
                                    trial.getAttackLocationProbe().getGroups(),
                                    rules, gridSize, distanceCalculator, dispersionParameters, null);
                        } else {
                            likelihoods = ScoreComputer.computeGroupProbabilities_Task_3(
                                    trial.getAttackLocationProbe().getAttackLocation(),
                                    trial.getAttackLocationProbe().getGroups(),
                                    rules, gridSize, distanceCalculator,
                                    groupAttacks, true, null);
                        }
                    }
                    if (likelihoods != null) {
                        Double ne = null;
                        if (probabilityType == POSTERIORS) {
                            ne = ScoreComputer.computeNegentropy(likelihoods, ProbabilityType.Percent);
                        }
                        appendLikelihoods(trialNum, 1, ne, likelihoods, writer);
                        writer.write("\n");
                    }
                    trialNum++;
                } catch (ScoreComputerException e) {
                    e.printStackTrace();
                }
            }
            writer.flush();
            writer.close();
        }
    }

    /**
     * @param task
     * @param file
     * @param writeHeader
     * @throws IOException
     */
    public static void createAttackHistoryFile(Task_1_2_3_PhaseBase<?> task, File file, boolean writeHeader) throws IOException {
        if (task.getTrialBlocks() != null && !task.getTrialBlocks().isEmpty()) {
            FileWriter writer = new FileWriter(file);
            if (writeHeader) {
                writer.write("group,x,y");
                writer.write("\n");
            }
            for (Task_1_2_3_TrialBlockBase trialBlock : task.getTrialBlocks()) {
                if (trialBlock.getGroupAttackPresentations() != null) {
                    for (AttackLocationPresentationTrial groupAttack : trialBlock.getGroupAttackPresentations()) {
                        writer.write(Integer.toString(groupAttack.getGroupAttack().getGroup().ordinal() + 1));
                        writer.write(separator);
                        writer.write(Integer.toString(groupAttack.getGroupAttack().getLocation().getX().intValue()));
                        writer.write(separator);
                        writer.write(Integer.toString(groupAttack.getGroupAttack().getLocation().getY().intValue()));
                        writer.write("\n");
                    }
                }
                Task_1_2_3_ProbeTrialBase trial = trialBlock.getProbeTrial();
                if (trial.getAttackLocationProbe() != null && trial.getAttackLocationProbe().getAttackLocation() != null
                        && trial.getGroundTruth() != null && trial.getGroundTruth().getResponsibleGroup() != null) {
                    writer.write(Integer.toString(trial.getGroundTruth().getResponsibleGroup().ordinal() + 1));
                    writer.write(separator);
                    writer.write(Integer.toString(trial.getAttackLocationProbe().getAttackLocation().getX().intValue()));
                    writer.write(separator);
                    writer.write(Integer.toString(trial.getAttackLocationProbe().getAttackLocation().getY().intValue()));
                    writer.write("\n");
                }
            }
            writer.flush();
            writer.close();
        }
    }

    /**
     * @param task
     * @param file
     * @param writeHeader
     * @throws IOException
     */
    public static void createCentersAndSigmasFile(Task_1_2_3_PhaseBase<?> task, File file, boolean writeHeader, GridSize gridSize) throws IOException {
        if (task.getTrialBlocks() != null && !task.getTrialBlocks().isEmpty()) {
            FileWriter writer = new FileWriter(file);
            if (writeHeader) {
                if (task instanceof Task_3_Phase) {
                    writer.write("trial,group,centerX,centerY,distance");
                } else {
                    writer.write("trial,group,centerX,centerY,sigma");
                }
                writer.write("\n");
            }
            RoadDistanceCalculator distanceCalculator = null;
            if (task instanceof Task_3_Phase) {
                distanceCalculator = new RoadDistanceCalculator(((Task_3_Phase) task).getRoads(), gridSize);
            }
            for (Task_1_2_3_TrialBlockBase trialBlock : task.getTrialBlocks()) {
                Task_1_2_3_ProbeTrialBase trial = trialBlock.getProbeTrial();
                ArrayList<Double> distances = null; //distances from normative centers to the attack location (Task 3 only)
                if (task instanceof Task_3_Phase) {
                    ArrayList<GroupCenter> groupCenters = new ArrayList<GroupCenter>(trial.getAttackDispersionParameters().size());
                    for (Task_1_2_3_AttackDispersionParameters parameters : trial.getAttackDispersionParameters()) {
                        groupCenters.add(new GroupCenter(parameters.getGroup(), parameters.getCenterLocation()));
                    }
                    try {
                        distances = ScoreComputer.computeCentersToAttackDistances(trial.getAttackLocationProbe().getAttackLocation(),
                                groupCenters, gridSize, distanceCalculator);
                    } catch (ScoreComputerException e) {
                        e.printStackTrace();
                    }
                }
                int i = 0;
                for (Task_1_2_3_AttackDispersionParameters parameters : trial.getAttackDispersionParameters()) {
                    writer.write(trial.getTrialNum().toString());
                    writer.write(separator);
                    writer.write(Integer.toString(parameters.getGroup().ordinal() + 1));
                    writer.write(separator);
                    writer.write(parameters.getCenterLocation().getX().toString());
                    writer.write(separator);
                    writer.write(parameters.getCenterLocation().getY().toString());
                    writer.write(separator);
                    if (task instanceof Task_3_Phase) {
                        if (distances != null) {
                            writer.write(distances.get(i).toString());
                        }
                    } else if (parameters.getSigmaX() != null) {
                        writer.write(parameters.getSigmaX().toString());
                    }
                    writer.write("\n");
                    i++;
                }
            }
            writer.flush();
            writer.close();
        }
    }

    /**
     * @param task
     * @param file
     * @param numStages
     * @param writeHeader
     * @param rules
     * @param gridSize
     * @throws IOException
     */
    public static void createProbabilitiesFile(Task_4_Phase task, File file, int numStages,
            boolean writeHeader, ProbabilityRules rulesStage1, ProbabilityRules rulesStageN,
            GridSize gridSize, int probabilityType) throws IOException {
        if (task.getTestTrials() != null && !task.getTestTrials().isEmpty()) {
            //rules.setProbabilityUpdateStrategy(new BayesianUpdateStrategy());
            FileWriter writer = new FileWriter(file);
            if (writeHeader) {
                writeLocationsFileHeader(writer, task.getTestTrials().get(0).getPossibleAttackLocations().size(),
                        probabilityType == POSTERIORS);
            }
            int trialNum = 1;
            for (Task_4_Trial trial : task.getTestTrials()) {
                writeTask_4_Probabilities(trial, trialNum, writer, numStages, rulesStage1, rulesStageN, gridSize, probabilityType);
                trialNum++;
            }
            writer.flush();
            writer.close();
        }
    }

    /**
     * @param task
     * @param file
     * @param writeHeader
     * @param gridSize
     * @throws IOException
     */
    public static void createDistancesFile(Task_4_Phase task, File file, boolean writeHeader, GridSize gridSize) throws IOException {
        if (task.getTestTrials() != null && !task.getTestTrials().isEmpty()) {
            FileWriter writer = new FileWriter(file);
            if (writeHeader) {
                writeDistancesFileHeader(writer, task.getTestTrials().get(0).getPossibleAttackLocations().size());
            }
            int trialNum = 1;
            for (Task_4_Trial trial : task.getTestTrials()) {
                writeTask_4_Distances(trial, trialNum, writer, gridSize);
                trialNum++;
            }
            writer.flush();
            writer.close();
        }
    }

    protected static void writeLocationsFileHeader(FileWriter writer, int numAttackLocations,
            boolean writeNegentropyColumn) throws IOException {
        writer.write("trial,stage,");
        if (writeNegentropyColumn) {
            writer.write("negentropy,");
        }
        for (int i = 1; i <= numAttackLocations; i++) {
            writer.write(Integer.toString(i));
            if (i <= numAttackLocations - 1) {
                writer.write(separator);
            }
        }
        writer.write("\n");
    }

    protected static void writeDistancesFileHeader(FileWriter writer, int numAttackLocations) throws IOException {
        writer.write("trial,");
        for (int i = 1; i <= numAttackLocations; i++) {
            writer.write(Integer.toString(i));
            if (i <= numAttackLocations - 1) {
                writer.write(separator);
            }
        }
        writer.write("\n");
    }

    protected static void writeDistancesFileHeader(FileWriter writer, Collection<GroupType> groups) throws IOException {
        writer.write("trial,");
        for (int i = 1; i <= groups.size(); i++) {
            writer.write(Integer.toString(i));
            if (i <= groups.size() - 1) {
                writer.write(separator);
            }
        }
        writer.write("\n");
    }

    /**
     * @param trial
     * @param trialNum
     * @param writer
     * @param numStages
     * @param rules
     * @param gridSize
     * @param probabilityType
     * @throws IOException
     */
    protected static void writeTask_4_Probabilities(Task_4_Trial trial, int trialNum, FileWriter writer, int numStages,
            ProbabilityRules rulesStage1, ProbabilityRules rulesStageN, GridSize gridSize, int probabilityType) throws IOException {
        //Compute center to attack distances if they haven't been computed yet				
        ArrayList<Double> centerToAttackDistances = trial.getCenterToAttackDistances();
        if (centerToAttackDistances == null || centerToAttackDistances.isEmpty()) {
            try {
                centerToAttackDistances = ScoreComputer.computeCenterToAttackDistances(trial.getGroupCenter().getLocation(),
                        trial.getPossibleAttackLocations(), gridSize,
                        new RoadDistanceCalculator(trial.getRoads(), gridSize));
            } catch (ScoreComputerException e) {
                e.printStackTrace();
            }
            trial.setCenterToAttackDistances(centerToAttackDistances);
        }

        //Compute initial probabilities from HUMINT
        ArrayList<Double> currentProbs = ProbabilityUtils.createDefaultInitialProbabilities_Double(4);
        try {
            //ArrayList<Double> likelihoods = CPSUtils.createDefaultInitialProbabilities_Double(4);
            ScoreComputer.updateLocationProbabilities_Task_4(trial.getPossibleAttackLocations(),
                    trial.getGroupCenter(), centerToAttackDistances, new HumintLayer(),
                    currentProbs, null, rulesStage1);

            //Compute negentropy
            Double ne = null;
            if (probabilityType == POSTERIORS) {
                ne = ScoreComputer.computeNegentropy(currentProbs, ProbabilityType.Percent);
            }
            appendLikelihoods(trialNum, 1, ne, currentProbs, writer);
            writer.write("\n");
        } catch (ScoreComputerException e) {
            e.printStackTrace();
        }

        //Compute likelihoods and updated posteriors for each INT layer based on rules for the layer
        if (numStages > 1 && trial.getIntLayers() != null && !trial.getIntLayers().isEmpty()) {
            for (int stage = 0; stage < numStages - 1; stage++) {
                if (stage < trial.getIntLayers().size()) {
                    INTLayerPresentationProbeBase layer = trial.getIntLayers().get(stage);
                    try {
                        //ArrayList<Double> likelihoods = CPSUtils.createDefaultInitialProbabilities_Double(4);
                        if (probabilityType == LIKELIHOODS) {
                            currentProbs = ProbabilityUtils.createDefaultInitialProbabilities_Double(4);
                        }
                        ScoreComputer.updateLocationProbabilities_Task_4(trial.getPossibleAttackLocations(),
                                trial.getGroupCenter(), centerToAttackDistances,
                                layer.getLayerType(),
                                currentProbs, null, rulesStageN);
                        //Compute negentropy
                        Double ne = null;
                        if (probabilityType == POSTERIORS) {
                            ne = ScoreComputer.computeNegentropy(currentProbs, ProbabilityType.Percent);
                        }
                        appendLikelihoods(trialNum, stage + 2, ne, currentProbs, writer);
                        writer.write("\n");
                    } catch (ScoreComputerException e) {
                        e.printStackTrace();
                    }
                } else {
                    break;
                }
            }
        }
    }

    /**
     * @param trial
     * @param trialNum
     * @param writer
     * @param gridSize
     * @throws IOException
     */
    protected static void writeTask_4_Distances(Task_4_Trial trial, int trialNum, FileWriter writer, GridSize gridSize) throws IOException {
        //Compute center to attack distances if they haven't been computed yet				
        ArrayList<Double> centerToAttackDistances = trial.getCenterToAttackDistances();
        if (centerToAttackDistances == null || centerToAttackDistances.isEmpty()) {
            try {
                centerToAttackDistances = ScoreComputer.computeCenterToAttackDistances(trial.getGroupCenter().getLocation(),
                        trial.getPossibleAttackLocations(), gridSize,
                        new RoadDistanceCalculator(trial.getRoads(), gridSize));
            } catch (ScoreComputerException e) {
                e.printStackTrace();
            }
            trial.setCenterToAttackDistances(centerToAttackDistances);
        }
        //Write the center to attack distances
        if (centerToAttackDistances != null && !centerToAttackDistances.isEmpty()) {
            appendDistances(trialNum, centerToAttackDistances, writer);
        }
        writer.write("\n");
    }

    /**
     * @param task
     * @param file
     * @param numStages
     * @param writeHeader
     * @param rules
     * @param gridSize
     * @throws IOException
     */
    @SuppressWarnings("deprecation")
    public static void createProbabilitiesFile(Task_5_Phase task, File file, int numStages,
            boolean writeHeader, ProbabilityRules rulesStage1, ProbabilityRules rulesStageN, GridSize gridSize, int probabilityType) throws IOException {
        if (task.getTestTrials() != null && !task.getTestTrials().isEmpty()) {
            //rules.setProbabilityUpdateStrategy(new BayesianUpdateStrategy());
            FileWriter writer = new FileWriter(file);
            Task_5_Trial trial1 = task.getTestTrials().get(0);
            if (writeHeader) {
                if (trial1.getInitialHumintReport() != null) {
                    writeGroupsFileHeader(writer, trial1.getInitialHumintReport().getGroups(),
                            probabilityType == POSTERIORS);
                } else if (trial1.getAttackLocationProbe_initial() != null) {
                    writeGroupsFileHeader(writer, trial1.getAttackLocationProbe_initial().getGroups(),
                            probabilityType == POSTERIORS);
                }
            }
            int trialNum = 1;
            for (Task_5_Trial trial : task.getTestTrials()) {
                writeTask_5_6_Probabilities(trial, trialNum, writer, numStages, rulesStage1, rulesStageN,
                        gridSize, probabilityType);
                trialNum++;
            }
            writer.flush();
            writer.close();
        }
    }

    /**
     * @param task
     * @param file
     * @param writeHeader
     * @param gridSize
     * @throws IOException
     */
    @SuppressWarnings("deprecation")
    public static void createDistancesFile(Task_5_Phase task, File file, boolean writeHeader, GridSize gridSize) throws IOException {
        if (task.getTestTrials() != null && !task.getTestTrials().isEmpty()) {
            FileWriter writer = new FileWriter(file);
            if (writeHeader) {
                Task_5_Trial trial1 = task.getTestTrials().get(0);
                if (trial1.getInitialHumintReport() != null) {
                    writeDistancesFileHeader(writer, trial1.getInitialHumintReport().getGroups());
                } else if (trial1.getAttackLocationProbe_initial() != null) {
                    writeDistancesFileHeader(writer, trial1.getAttackLocationProbe_initial().getGroups());
                }
            }
            int trialNum = 1;
            for (Task_5_Trial trial : task.getTestTrials()) {
                writeTask_5_6_Distances(trial, trialNum, writer, gridSize);
                trialNum++;
            }
            writer.flush();
            writer.close();
        }
    }

    /**
     * @param trial
     * @param trialNum
     * @param writer
     * @param gridSize
     * @throws IOException
     */
    protected static void writeTask_5_6_Distances(Task_5_Trial trial, int trialNum, FileWriter writer, GridSize gridSize) throws IOException {
        //Compute centers to attack distances if they haven't been computed yet				
        ArrayList<Double> centersToAttackDistances = trial.getCentersToAttackDistances();
        if (centersToAttackDistances == null || centersToAttackDistances.isEmpty()) {
            try {
                centersToAttackDistances = ScoreComputer.computeCentersToAttackDistances(trial.getAttackLocation().getLocation(),
                        trial.getGroupCenters(), gridSize,
                        new RoadDistanceCalculator(trial.getRoads(), gridSize));
            } catch (ScoreComputerException e) {
                e.printStackTrace();
            }
            trial.setCentersToAttackDistances(centersToAttackDistances);
        }
        //Write the centers to attack distances
        if (centersToAttackDistances != null && !centersToAttackDistances.isEmpty()) {
            appendDistances(trialNum, centersToAttackDistances, writer);
        }
        writer.write("\n");
    }

    /**
     * @param task
     * @param file
     * @param numStages
     * @param writeHeader
     * @param rules.,,
     * @param gridSize
     * @throws IOException
     */
    @SuppressWarnings("deprecation")
    public static void createProbabilitiesFile(Task_6_Phase task, File file, int numStages,
            boolean writeHeader, ProbabilityRules rulesStage1, ProbabilityRules rulesStageN, GridSize gridSize, int probabilityType) throws IOException {
        if (task.getTestTrials() != null && !task.getTestTrials().isEmpty()) {
            //rules.setProbabilityUpdateStrategy(new BayesianUpdateStrategy());
            FileWriter writer = new FileWriter(file);
            Task_6_Trial trial1 = task.getTestTrials().get(0);
            if (writeHeader) {
                if (trial1.getInitialHumintReport() != null) {
                    writeGroupsFileHeader(writer, trial1.getInitialHumintReport().getGroups(),
                            probabilityType == POSTERIORS);
                } else if (trial1.getAttackLocationProbe_initial() != null) {
                    writeGroupsFileHeader(writer, trial1.getAttackLocationProbe_initial().getGroups(),
                            probabilityType == POSTERIORS);
                }
            }
            int trialNum = 1;
            for (Task_6_Trial trial : task.getTestTrials()) {
                writeTask_5_6_Probabilities(trial, trialNum, writer, numStages, rulesStage1, rulesStageN,
                        gridSize, probabilityType);
                trialNum++;
            }
            writer.flush();
            writer.close();
        }
    }

    /**
     * @param task
     * @param file
     * @param writeHeader
     * @param gridSize
     * @throws IOException
     */
    @SuppressWarnings("deprecation")
    public static void createDistancesFile(Task_6_Phase task, File file, boolean writeHeader, GridSize gridSize) throws IOException {
        if (task.getTestTrials() != null && !task.getTestTrials().isEmpty()) {
            FileWriter writer = new FileWriter(file);
            if (writeHeader) {
                Task_5_Trial trial1 = task.getTestTrials().get(0);
                if (trial1.getInitialHumintReport() != null) {
                    writeDistancesFileHeader(writer, trial1.getInitialHumintReport().getGroups());
                } else if (trial1.getAttackLocationProbe_initial() != null) {
                    writeDistancesFileHeader(writer, trial1.getAttackLocationProbe_initial().getGroups());
                }
            }
            int trialNum = 1;
            for (Task_5_Trial trial : task.getTestTrials()) {
                writeTask_5_6_Distances(trial, trialNum, writer, gridSize);
                trialNum++;
            }
            writer.flush();
            writer.close();
        }
    }

    /**
     *
     *
     * @param task
     * @param bayesianBayesianLayerFile
     * @param bayesianHumanLayerFile
     * @param humanBayesianLayerFile
     * @param allPermutationslayerUtilitiesFile
     * @param normativeLayersFile
     * @param modalHumanLayers
     * @param humanUpdateStrategy
     * @param numStages
     * @param writeHeader
     * @param rules
     * @param gridSize
     * @throws IOException
     */
    public static void creatLayerChoicesFilesTask6(Task_6_Phase task,
            File bayesianBayesianLayerFile, File humanBayesianLayerFile, File bayesianHumanLayerFile,
            File allPermutationslayerUtilitiesFile, File normativeLayersFile,
            ArrayList<IntType> modalHumanLayers, AbUpdateStrategy humanUpdateStrategy,
            int numStages, boolean writeHeader, GridSize gridSize) throws IOException {
        ProbabilityRules rules = ProbabilityRules.createDefaultProbabilityRules();
        if (bayesianBayesianLayerFile != null) {
            //Create a layer selection utilities file using the Bayesian layer selection strategy and Bayesian updating
            rules.setProbabilityUpdateStrategy(new BayesianUpdateStrategy());
            rules.getProbabilityUpdateStrategy().setName("Bayesian-Updating");
            createLayerChoicesUtilityFile(task, bayesianBayesianLayerFile, writeHeader, rules, gridSize,
                    "Bayesian-Selection", true, null);
        }
        if (humanBayesianLayerFile != null) {
            //Create a layer selection utilities file using the modal human layer selection strategy and Bayesian updating
            rules.setProbabilityUpdateStrategy(new BayesianUpdateStrategy());
            rules.getProbabilityUpdateStrategy().setName("Bayesian-Updating");
            ArrayList<ArrayList<IntType>> layerSelections = new ArrayList<ArrayList<IntType>>(task.getNumTrials());
            for (int i = 0; i < task.getNumTrials(); i++) {
                layerSelections.add(modalHumanLayers);
            }
            createLayerChoicesUtilityFile(task, humanBayesianLayerFile, writeHeader, rules, gridSize,
                    "Human-Selection", false, layerSelections);
        }
        if (bayesianHumanLayerFile != null) {
            //Create a layer selection utilities file using the Bayesian layer selection strategy and human updating
            rules.setProbabilityUpdateStrategy(new BayesianUpdateStrategy());
            ScoreComputer sc = new ScoreComputer();
            sc.computeSolutionAndScoreForTask(task, rules, gridSize, false, null);
            rules.setProbabilityUpdateStrategy(humanUpdateStrategy);
            rules.getProbabilityUpdateStrategy().setName("Human-Updating");
            ArrayList<ArrayList<IntType>> layerSelections = new ArrayList<ArrayList<IntType>>(task.getNumTrials());
            for (Task_6_Trial trial : task.getTestTrials()) {
                ArrayList<Task_5_6_AttackLocationProbeResponseAfterINT> intResponses
                        = trial.getTrialResponse().getAttackLocationResponses_afterINTs();
                ArrayList<IntType> layers = new ArrayList<IntType>(intResponses.size());
                layerSelections.add(layers);
                for (Task_5_6_AttackLocationProbeResponseAfterINT intResponse : intResponses) {
                    layers.add(intResponse.getIntLayerShown().getLayerType().getLayerType());
                }
            }
            createLayerChoicesUtilityFile(task, bayesianHumanLayerFile, writeHeader, rules, gridSize,
                    "Bayesian-Selection", false, layerSelections);
        }
        if (allPermutationslayerUtilitiesFile != null) {
            createLayerChoicesUtilityFile((Task_6_Phase) task, allPermutationslayerUtilitiesFile, writeHeader, rules, gridSize);
        }
        if (normativeLayersFile != null) {
            createNormativeLayerChoicesFile((Task_6_Phase) task, normativeLayersFile, writeHeader, rules, gridSize);
        }
    }

    /**
     * Creates a file with the utility of each layer selection at each stage.
     *
     * @param task
     * @param file
     * @param writeHeader
     * @param rules
     * @param gridSize
     * @param layerStrategyName
     * @param selectBestLayer
     * @param layerSelections
     * @throws IOException
     */
    protected static void createLayerChoicesUtilityFile(Task_6_Phase task, File file, boolean writeHeader,
            ProbabilityRules rules, GridSize gridSize,
            String layerStrategyName, boolean selectBestLayer, ArrayList<ArrayList<IntType>> layerSelections) throws IOException {
        //Columns: trial #, layer selection strategy name, probability update strategy name, layer 1, utility 1, layer 2, utility 2, layer 3, utility 3
        if (task.getTestTrials() != null && !task.getTestTrials().isEmpty()) {
            BayesianUpdateStrategy bayesianStrategy = new BayesianUpdateStrategy();
            bayesianStrategy.setName("Bayesian");
            if (rules.getProbabilityUpdateStrategy() == null) {
                rules.setProbabilityUpdateStrategy(bayesianStrategy);
            }
            ProbabilityUpdateStrategy origStrategy = rules.getProbabilityUpdateStrategy();
            int numLayers = task.getTestTrials().get(0).getNumLayersToShow();
            FileWriter writer = new FileWriter(file);
            if (writeHeader) {
                writer.write("trial,layer selection strategy, probability update strategy,");
                for (int i = 0; i < numLayers; i++) {
                    writer.write("layer " + Integer.toString(i + 1));
                    writer.write(separator);
                    writer.write("utility " + Integer.toString(i + 1));
                    if (i < numLayers - 1) {
                        writer.write(separator);
                    }
                }
                writer.write("\n");
            }
            int trialNum = 1;
            for (Task_6_Trial trial : task.getTestTrials()) {
                //Compute centers to attack distances if they haven't been computed yet				
                ArrayList<Double> centersToAttackDistances = trial.getCentersToAttackDistances();
                if (centersToAttackDistances == null || centersToAttackDistances.isEmpty()) {
                    try {
                        centersToAttackDistances = ScoreComputer.computeCentersToAttackDistances(trial.getAttackLocation().getLocation(),
                                trial.getGroupCenters(), gridSize,
                                new RoadDistanceCalculator(trial.getRoads(), gridSize));
                    } catch (ScoreComputerException e) {
                        e.printStackTrace();
                    }
                    trial.setCentersToAttackDistances(centersToAttackDistances);
                }

                //Compute initial probabilities from HUMINT	
                ArrayList<Double> currentProbs = ProbabilityUtils.createDefaultInitialProbabilities_Double(4);
                try {
                    //We always use Bayesian updating to compute the initial probabilities
                    rules.setProbabilityUpdateStrategy(bayesianStrategy);
                    ScoreComputer.updateGroupProbabilities_Task_5_6(trial.getGroupCenters(),
                            trial.getAttackLocation(), centersToAttackDistances, new HumintLayer(),
                            currentProbs, null, null, rules);
                    rules.setProbabilityUpdateStrategy(origStrategy);
                    //System.out.println("Trial " + trialNum + ", Initial Probs: " + currentProbs);
                } catch (ScoreComputerException e) {
                    e.printStackTrace();
                }

                //Compute expected informatic utility at each stage
                ArrayList<IntLayer> selectedLayers = new ArrayList<IntLayer>(numLayers); //The layers selected
                ArrayList<Double> utilities = new ArrayList<Double>(numLayers); //The informatic utilities of each selection
                List<IntLayer> remainingLayers = null; //The remaining layers to select from when selecting the best layer
                if (selectBestLayer) {
                    remainingLayers = new LinkedList<IntLayer>();
                    for (Task_5_6_INTLayerPresentationProbe layer : trial.getIntLayers()) {
                        remainingLayers.add(layer.getLayerType());
                    }
                }
                for (int layerIndex = 0; layerIndex < numLayers; layerIndex++) {
                    List<IntLayer> layers = null;
                    if (selectBestLayer) {
                        //Any of the remaining layers are candidates for the best layer
                        layers = remainingLayers;
                    } else {
                        //The next layer specified is the only candidate for the best layer
                        layers = new LinkedList<IntLayer>();
                        IntType layer = layerSelections.get(trialNum - 1).get(layerIndex);
                        if (layer == IntType.SIGINT) {
                            for (GroupCenter group : trial.getGroupCenters()) {
                                layers.add(new SigintLayer(group.getGroup()));
                            }
                        } else {
                            switch (layer) {
                                case IMINT:
                                    layers.add(new ImintLayer());
                                    break;
                                case MOVINT:
                                    layers.add(new MovintLayer());
                                    break;
                                case SOCINT:
                                    layers.add(new SocintLayer());
                                    break;
                                default:
                                    break;
                            }
                        }
                    }

                    //Compute the informatic utility of selecting each layer and select the best one
                    double layerUtility = Double.MIN_VALUE;
                    IntLayer layerToSelect = null;
                    for (IntLayer intLayer : layers) {
                        try {
                            double u = ScoreComputer.computeExpectedUtilityOfINT_Task_6(trial.getGroupCenters(),
                                    trial.getAttackLocation(),
                                    centersToAttackDistances, intLayer,
                                    currentProbs, null, null, rules).cumulativeProb;
                            if (u > layerUtility) {
                                layerUtility = u;
                                layerToSelect = intLayer;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                    //Select the layer and update probabilities
                    selectedLayers.add(layerToSelect);
                    utilities.add(layerUtility);
                    try {
						//DEBUG CODE
                        //if(rules.getProbabilityUpdateStrategy() instanceof AbUpdateStrategy) {
                        //	System.out.println("Probs before: " + currentProbs);
                        //}
                        //END DEBUG CODE
                        ScoreComputer.updateGroupProbabilities_Task_5_6(trial.getGroupCenters(),
                                trial.getAttackLocation(), centersToAttackDistances,
                                layerToSelect, currentProbs, null, null, rules);
						//DEBUG CODE
                        //if(rules.getProbabilityUpdateStrategy() instanceof AbUpdateStrategy) {
                        //	System.out.println("Probs after: " + currentProbs);
                        //}
                        //END DEBUG CODE
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    if (selectBestLayer) {
                        //Remove the selected layer from the remaining layers
                        if (layerToSelect.getLayerType() == IntType.SIGINT) {
                            //Remove all SIGINT layers
                            for (IntLayer layer : remainingLayers.toArray(new IntLayer[remainingLayers.size()])) {
                                if (layer.getLayerType() == IntType.SIGINT) {
                                    remainingLayers.remove(layer);
                                }
                            }
                        } else {
                            remainingLayers.remove(layerToSelect);
                        }
                    }
                }

				//Write the layers selected and the informatic utilities
                //Columns: trial #, layer selection strategy name, probability update strategy name, layer 1, utility 1, layer 2, utility 2, layer 3, utility 3
                writer.write(Integer.toString(trialNum) + separator);
                writer.write(layerStrategyName + separator);
                writer.write(rules.getProbabilityUpdateStrategy().getName() + separator);
                for (int i = 0; i < selectedLayers.size(); i++) {
                    writer.write(selectedLayers.get(i).getLayerType().toString() + separator);
                    writer.write(utilities.get(i).toString() + separator);
                }
                writer.write("\n");

                trialNum++;
            }
            writer.flush();
            writer.close();
        }
    }

    /**
     * Creates a file with the overall utility of every possible layer selection
     * permutation in Task 6.
     *
     * @param task
     * @param file
     * @param writeHeader
     * @param rules
     * @param gridSize
     * @throws IOException
     */
    protected static void createLayerChoicesUtilityFile(Task_6_Phase task, File file, boolean writeHeader,
            ProbabilityRules rules, GridSize gridSize) throws IOException {
        //Columns: trial #, layer 1, layer 2, layer 3, utility		
        if (task.getTestTrials() != null && !task.getTestTrials().isEmpty()) {
            rules.setProbabilityUpdateStrategy(new BayesianUpdateStrategy());
            int numLayers = task.getTestTrials().get(0).getNumLayersToShow();
            FileWriter writer = new FileWriter(file);
            if (writeHeader) {
                writer.write("trial,permutation,");
                for (int i = 0; i < numLayers; i++) {
                    writer.write("layer " + Integer.toString(i + 1));
                    if (i < numLayers - 1) {
                        writer.write(separator);
                    }
                }
                writer.write("\n");
            }
            int trialNum = 1;
            for (Task_6_Trial trial : task.getTestTrials()) {
                //Compute centers to attack distances if they haven't been computed yet				
                ArrayList<Double> centersToAttackDistances = trial.getCentersToAttackDistances();
                if (centersToAttackDistances == null || centersToAttackDistances.isEmpty()) {
                    try {
                        centersToAttackDistances = ScoreComputer.computeCentersToAttackDistances(trial.getAttackLocation().getLocation(),
                                trial.getGroupCenters(), gridSize,
                                new RoadDistanceCalculator(trial.getRoads(), gridSize));
                    } catch (ScoreComputerException e) {
                        e.printStackTrace();
                    }
                    trial.setCentersToAttackDistances(centersToAttackDistances);
                }

                //Compute initial probabilities from HUMINT	
                ArrayList<Double> initialProbs = ProbabilityUtils.createDefaultInitialProbabilities_Double(4);
                try {
                    ScoreComputer.updateGroupProbabilities_Task_5_6(trial.getGroupCenters(),
                            trial.getAttackLocation(), centersToAttackDistances, new HumintLayer(),
                            initialProbs, null, null, rules);
                } catch (ScoreComputerException e) {
                    e.printStackTrace();
                }

                //Iterate over all possible layer selection permutations and compute the informatic utility of each permutation			
                IntType[] intLayers = new IntType[]{IntType.IMINT, IntType.MOVINT, IntType.SIGINT, IntType.SOCINT};
                int[] currentPerm = new int[]{0, 1, 2, 3};
                int numPerms = CPSUtils.factorial(intLayers.length);
                for (int layerPerm = 0; layerPerm < numPerms; layerPerm++) {
                    writer.write(Integer.toString(trialNum) + separator); //Output the trial number
                    //writer.write(Integer.toString(layerPerm+1) + separator); //Output the permutation index
                    ArrayList<Double> currentProbs = ProbabilityUtils.cloneProbabilities_Double(initialProbs);
                    double utility = 0;
                    for (int layerIndex = 0; layerIndex < numLayers; layerIndex++) {
                        IntType layer = intLayers[currentPerm[layerIndex]];
                        writer.write(layer.toString() + separator); //Output the layer name
                        //Compute the expected utility of choosing the INT
                        //In the case of SIGINT, we get SIGINT on the group that yields the highest expected utility
                        List<IntLayer> layers = new LinkedList<IntLayer>();
                        if (layer == IntType.SIGINT) {
                            for (GroupCenter group : trial.getGroupCenters()) {
                                layers.add(new SigintLayer(group.getGroup()));
                            }
                        } else {
                            switch (layer) {
                                case IMINT:
                                    layers.add(new ImintLayer());
                                    break;
                                case MOVINT:
                                    layers.add(new MovintLayer());
                                    break;
                                case SOCINT:
                                    layers.add(new SocintLayer());
                                    break;
                                default:
                                    break;
                            }
                        }
                        double layerUtility = Double.MIN_VALUE;
                        IntLayer layerToSelect = null;
                        //Compute the expected utility of choosing the INT
                        for (IntLayer intLayer : layers) {
                            try {
                                double u = ScoreComputer.computeExpectedUtilityOfINT_Task_6(trial.getGroupCenters(),
                                        trial.getAttackLocation(),
                                        centersToAttackDistances, intLayer,
                                        currentProbs, null, null, rules).cumulativeProb;
                                if (u > layerUtility) {
                                    layerUtility = u;
                                    layerToSelect = intLayer;
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                        utility += layerUtility;

                        //"Choose" the INT and update probabilities
                        if (layerToSelect != null) {
                            try {
                                ScoreComputer.updateGroupProbabilities_Task_5_6(trial.getGroupCenters(),
                                        trial.getAttackLocation(), centersToAttackDistances,
                                        layerToSelect, currentProbs, null, null, rules);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                    //Output the utility of the current permutation
                    writer.write(Double.toString(utility));
                    writer.write("\n");

                    //Get the next permutation
                    currentPerm = CPSUtils.nextPermutation(currentPerm);
                }
                trialNum++;
            }
            writer.flush();
            writer.close();
        }
    }

    /**
     * Create file with the normative layer choices for each trial in Task 6
     * based on the "greedy grab" strategy of selecting the highest utility
     * layer at each stage.
     *
     * @param task
     * @param file
     * @param writeHeader
     * @param rules
     * @param gridSize
     * @throws IOException
     */
    protected static void createNormativeLayerChoicesFile(Task_6_Phase task, File file, boolean writeHeader,
            ProbabilityRules rules, GridSize gridSize) throws IOException {
        //Columns: trial #, layer 1, layer 2, layer 3
        if (task.getTestTrials() != null && !task.getTestTrials().isEmpty()) {
            int numLayers = task.getTestTrials().get(0).getNumLayersToShow();
            FileWriter writer = new FileWriter(file);
            if (writeHeader) {
                writer.write("trial,");
                for (int i = 0; i < numLayers; i++) {
                    writer.write("layer " + Integer.toString(i + 1));
                    if (i < numLayers - 1) {
                        writer.write(separator);
                    }
                }
                writer.write('\n');
            }
            for (Task_6_Trial trial : task.getTestTrials()) {
                trial.setTrialResponse(null);
            }
            ScoreComputer sc = new ScoreComputer();
            rules.setProbabilityUpdateStrategy(new BayesianUpdateStrategy());
            //rules.setProbabilityUpdateStrategy(new AbUpdateStrategy(0.9, 0.5));
            sc.computeSolutionAndScoreForTask(task, rules, gridSize, false, null);
            int trialNum = 1;
            for (Task_6_Trial trial : task.getTestTrials()) {
                writer.write(Integer.toString(trialNum) + separator); //Output the trial number
                int i = 0;
                //DEBUG CODE
                double u = 0;
                ArrayList<String> layers = new ArrayList<String>();
                //END DEBUG CODE
                for (Task_5_6_AttackLocationProbeResponseAfterINT intLayerResponse : trial.getTrialResponse().getAttackLocationResponses_afterINTs()) {
                    String layer = intLayerResponse.getIntLayerShown().getLayerType().getLayerType().toString();
                    writer.write(layer); //Output the layer name
                    if (i < numLayers - 1) {
                        writer.write(separator);
                    }
                    //DEBUG CODE
                    IntLayer layerType = intLayerResponse.getIntLayerShown().getLayerType();
                    int index = 0;
                    for (INTLayerExpectedUtility intLayerUtility : intLayerResponse.getIntLayerExpectedUtilities()) {
                        if (intLayerUtility.getLayerType() == layerType) {
                            u += intLayerResponse.getIntLayerExpectedUtilities().get(index).getExpectedUtility_cumulativeBayesian();
                            break;
                        }
                        index++;
                    }
                    layers.add(layer);
                    //END DEBUG CODE
                    i++;
                }
                //DEBUG CODE
                System.out.println("Trial " + trialNum + ": " + layers + ", " + u);
                //END DEBUG CODE
                writer.write("\n");
                trialNum++;
            }
            writer.flush();
            writer.close();
        }
    }

    protected static void writeGroupsFileHeader(FileWriter writer, ArrayList<GroupType> groups,
            boolean writeNegentropyColumn) throws IOException {
        writer.write("trial,stage,");
        if (writeNegentropyColumn) {
            writer.write("negentropy,");
        }
        for (int i = 0; i < groups.size(); i++) {
            writer.write(groups.get(i).getGroupNameAbbreviated());
            if (i < groups.size() - 1) {
                writer.write(separator);
            }
        }
        writer.write('\n');
    }

    /**
     * @param trial
     * @param trialNum
     * @param writer
     * @param numStages
     * @param rules
     * @param gridSize
     * @throws IOException
     */
    protected static void writeTask_5_6_Probabilities(Task_5_Trial trial, int trialNum, FileWriter writer,
            int numStages, ProbabilityRules rulesStage1, ProbabilityRules rulesStageN,
            GridSize gridSize, int probabilityType) throws IOException {
        //Compute centers to attack distances if they haven't been computed yet				
        ArrayList<Double> centersToAttackDistances = trial.getCentersToAttackDistances();
        if (centersToAttackDistances == null || centersToAttackDistances.isEmpty()) {
            try {
                centersToAttackDistances = ScoreComputer.computeCentersToAttackDistances(trial.getAttackLocation().getLocation(),
                        trial.getGroupCenters(), gridSize,
                        new RoadDistanceCalculator(trial.getRoads(), gridSize));
            } catch (ScoreComputerException e) {
                e.printStackTrace();
            }
            trial.setCentersToAttackDistances(centersToAttackDistances);
        }

        //Compute initial probabilities from HUMINT
        ArrayList<Double> currentProbs = ProbabilityUtils.createDefaultInitialProbabilities_Double(4);
        try {
            ScoreComputer.updateGroupProbabilities_Task_5_6(trial.getGroupCenters(),
                    trial.getAttackLocation(), centersToAttackDistances, new HumintLayer(),
                    currentProbs, null, null, rulesStage1);
            //Compute negentropy
            Double ne = null;
            if (probabilityType == POSTERIORS) {
                ne = ScoreComputer.computeNegentropy(currentProbs, ProbabilityType.Percent);
            }
            appendLikelihoods(trialNum, 1, ne, currentProbs, writer);
            writer.write("\n");
        } catch (ScoreComputerException e) {
            e.printStackTrace();
        }

        //Compute likelihoods for each INT layer based on rules for the layer
        if (numStages > 1 && trial.getIntLayers() != null && !trial.getIntLayers().isEmpty()) {
            for (int stage = 0; stage < numStages - 1; stage++) {
                if (stage < trial.getIntLayers().size()) {
                    INTLayerPresentationProbeBase layer = trial.getIntLayers().get(stage);
                    try {
                        //ArrayList<Double> likelihoods = CPSUtils.createDefaultInitialProbabilities_Double(4);
                        if (probabilityType == LIKELIHOODS) {
                            currentProbs = ProbabilityUtils.createDefaultInitialProbabilities_Double(4);
                        }
                        ScoreComputer.updateGroupProbabilities_Task_5_6(trial.getGroupCenters(),
                                trial.getAttackLocation(), centersToAttackDistances,
                                layer.getLayerType(),
                                currentProbs, null, null, rulesStageN);
                        //Compute negentropy
                        Double ne = null;
                        if (probabilityType == POSTERIORS) {
                            ne = ScoreComputer.computeNegentropy(currentProbs, ProbabilityType.Percent);
                        }
                        appendLikelihoods(trialNum, stage + 2, ne, currentProbs, writer);
                        writer.write("\n");
                    } catch (ScoreComputerException e) {
                        e.printStackTrace();
                    }
                } else {
                    break;
                }
            }
        }
    }

    protected static void appendLikelihoods(int trial, int stage,
            Double negentropy, ArrayList<Double> likelihoods, FileWriter writer) throws IOException {
        writer.write(Integer.toString(trial));
        writer.write(separator);
        writer.write(Integer.toString(stage));
        writer.write(separator);
        if (negentropy != null) {
            writer.write(negentropy.toString());
            writer.write(separator);
        }
        for (int i = 0; i < likelihoods.size(); i++) {
            writer.write(Double.toString(likelihoods.get(i) / 100.d));
            if (i < likelihoods.size() - 1) {
                writer.write(separator);
            }
        }
    }

    protected static void appendDistances(int trial, ArrayList<Double> distances, FileWriter writer) throws IOException {
        writer.write(Integer.toString(trial));
        writer.write(separator);
        for (int i = 0; i < distances.size(); i++) {
            writer.write(Double.toString(distances.get(i)));
            if (i < distances.size() - 1) {
                writer.write(separator);
            }
        }
    }

    public static void createAllExamParameterFiles(String examFileName, File outputFolder) {
        List<Integer> tasks = Arrays.asList(1, 2, 3, 4, 5, 6);
        //List<Integer> tasks = Arrays.asList(3);
        boolean writeHeader = false;

        try {
            //Load the exam
            URL examFile;
            examFile = new File(examFileName).toURI().toURL();
            IcarusExam_Phase1 exam = IcarusExamLoader_Phase1.unmarshalExam(examFile);
            exam.setOriginalPath(examFile);

            //Create input files for tasks		
            for (Integer taskNum : tasks) {
                TaskTestPhase<?> task = exam.getTasks().get(taskNum - 1);
                IcarusExamLoader_Phase1.initializeTaskFeatureVectorData(task,
                        exam.getOriginalPath(), exam.getGridSize(), false, null);
                File normativeLikelihoodsFile = new File(outputFolder, "Task_" + taskNum + "_Normative_Probabilities.csv");
                File normativePosteriorsFile = new File(outputFolder, "Task_" + taskNum + "_Normative_Posteriors.csv");
                File attackHistoryFile = null;
                File centersAndSigmasFile = null;
                File distancesFile = null;
                File bayesianBayesianLayerFile = null;
                File humanBayesianLayerFile = null;
                File bayesianHumanLayerFile = null;
                File allPermutationsLayerUtilitiesFile = null;
                File normativeLayersFile = null;
                if (taskNum < 4) {
                    attackHistoryFile = new File(outputFolder, "Task_" + taskNum + "_Attacks.csv");
                    if (taskNum == 3) {
                        centersAndSigmasFile = new File(outputFolder, "Task_" + taskNum + "_Normative_Centers_Distances.csv");
                    } else {
                        centersAndSigmasFile = new File(outputFolder, "Task_" + taskNum + "_Normative_Centers_Sigmas.csv");
                    }
                } else {
                    distancesFile = new File(outputFolder, "Task_" + taskNum + "_Distances.csv");
                    if (taskNum == 6) {
                        bayesianBayesianLayerFile = new File(outputFolder, "Task_" + taskNum + "_BB_Layer_Utilities.csv");
                        humanBayesianLayerFile = new File(outputFolder, "Task_" + taskNum + "_HB_Layer_Utilities.csv");
                        bayesianHumanLayerFile = new File(outputFolder, "Task_" + taskNum + "_BH_Layer_Utilities.csv");
                        allPermutationsLayerUtilitiesFile = new File(outputFolder, "Task_" + taskNum + "_All_Layer_Utilities.csv");
                        normativeLayersFile = new File(outputFolder, "Task_" + taskNum + "_Normative_Layer_Choices.csv");
                    }
                }
                int numStages = 1;
                switch (taskNum) {
                    case 4:
                        numStages = 2;
                        break;
                    case 5:
                        numStages = 5;
                        break;
                    case 6:
                        numStages = 1;
                        break;
                }
                createModelTestInputFiles(task,
                        normativeLikelihoodsFile, normativePosteriorsFile,
                        attackHistoryFile, centersAndSigmasFile, distancesFile,
                        numStages, writeHeader,
                        ProbabilityRules.createDefaultProbabilityRules(), exam.getGridSize());
                if (task instanceof Task_6_Phase) {
                    creatLayerChoicesFilesTask6((Task_6_Phase) task,
                            bayesianBayesianLayerFile, humanBayesianLayerFile, bayesianHumanLayerFile,
                            allPermutationsLayerUtilitiesFile, normativeLayersFile,
                            new ArrayList<IntType>(Arrays.asList(IntType.IMINT, IntType.MOVINT, IntType.SIGINT)),
                            new AbUpdateStrategy(0.9, 0.5),
                            numStages, writeHeader, exam.getGridSize());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createDesignAssessmentFiles(String examFileName, File outputFolder) {
        //List<Integer> tasks = Arrays.asList(1,2,3,4,5,6);		
        List<Integer> tasks = Arrays.asList(2);
        boolean writeHeader = false;

        try {
            //Load the exam
            URL examFile;
            examFile = new File(examFileName).toURI().toURL();
            IcarusExam_Phase1 exam = IcarusExamLoader_Phase1.unmarshalExam(examFile);
            exam.setOriginalPath(examFile);
            ProbabilityRules rulesStage1 = ProbabilityRules.createDefaultProbabilityRules();
            ProbabilityRules rulesStageN = ProbabilityRules.createDefaultProbabilityRules();
            BayesianUpdateStrategy bayesianUpdateStrategy = new BayesianUpdateStrategy();

			//Create files containing normative (Bayesian) probabilities and probabilities generated using the given
            //A-B update strategy for each task			
            for (Integer taskNum : tasks) {
                TaskTestPhase<?> task = exam.getTasks().get(taskNum - 1);
                IcarusExamLoader_Phase1.initializeTaskFeatureVectorData(task,
                        exam.getOriginalPath(), exam.getGridSize(), false, null);
                File normativePosteriorsFile = new File(outputFolder, "Task_" + taskNum + "_Normative_Posteriors.csv");
                File abPosteriorsFile = new File(outputFolder, "Task_" + taskNum + "_AB_Posteriors.csv");

                int numStages = 1;
                switch (taskNum) {
                    case 4:
                        numStages = 2;
                        break;
                    case 5:
                        numStages = 5;
                        break;
                    case 6:
                        numStages = 1;
                        break;
                }

                if (task instanceof Task_1_2_3_PhaseBase) {
                    //Create file with normative probabilities
                    rulesStage1.setProbabilityUpdateStrategy(bayesianUpdateStrategy);
					//createProbabilitiesFile((Task_1_2_3_PhaseBase)task, normativePosteriorsFile, writeHeader, 
                    //		rulesStage1, exam.getGridSize(), POSTERIORS, false); //TODO: DEBUGGING 			
                    //Create file with A-B update strategy probabilities
                    if (taskNum == 1) {
                        rulesStage1.setProbabilityUpdateStrategy(new AbUpdateStrategy(0.3, 0.3)); //Best fit pilot
                        //rulesStage1.setProbabilityUpdateStrategy(new AbUpdateStrategy(.5d, .5d)); //Conservative
                        //rulesStage1.setProbabilityUpdateStrategy(new AbUpdateStrategy(1.d, 1.d)); //Bayesian
                    } else if (taskNum == 2) {
                        rulesStage1.setProbabilityUpdateStrategy(new AbUpdateStrategy(0.0, 0.4)); //Best fit pilot
                        //rulesStage1.setProbabilityUpdateStrategy(new AbUpdateStrategy(.5d, .5d)); //Conservative
                        //rulesStage1.setProbabilityUpdateStrategy(new AbUpdateStrategy(1.d, 1.d)); //Bayesian
                    } else if (taskNum == 3) {
                        rulesStage1.setProbabilityUpdateStrategy(new AbUpdateStrategy(0.7, 0.8)); //Best fit pilot
                        //rulesStage1.setProbabilityUpdateStrategy(new AbUpdateStrategy(.5d, .5d)); //Conservative
                        //rulesStage1.setProbabilityUpdateStrategy(new AbUpdateStrategy(1.d, 1.d)); //Bayesian
                    }
                    createProbabilitiesFile((Task_1_2_3_PhaseBase<?>) task, abPosteriorsFile, writeHeader,
                            rulesStage1, exam.getGridSize(), POSTERIORS, false);
                } else if (task instanceof Task_4_Phase) {
                    //Create file with normative probabilities
                    rulesStage1.setProbabilityUpdateStrategy(bayesianUpdateStrategy);
                    rulesStageN.setProbabilityUpdateStrategy(bayesianUpdateStrategy);
                    createProbabilitiesFile((Task_4_Phase) task, normativePosteriorsFile, numStages, writeHeader,
                            rulesStage1, rulesStageN, exam.getGridSize(), POSTERIORS);
                    //Create file with A-B update strategy probabilities				
                    rulesStage1.setProbabilityUpdateStrategy(new AbUpdateStrategy(1.d, 0.5)); //Best fit pilot
                    //rulesStage1.setProbabilityUpdateStrategy(new AbUpdateStrategy(.5d, .5d)); //Conservative
                    //rulesStage1.setProbabilityUpdateStrategy(new AbUpdateStrategy(1.d, 1.d)); //Bayesian
                    rulesStageN.setProbabilityUpdateStrategy(new AbUpdateStrategy(1.d, 1.d)); //Best fit pilot
                    //rulesStageN.setProbabilityUpdateStrategy(new AbUpdateStrategy(.5d, .5d)); //Conservative
                    //rulesStageN.setProbabilityUpdateStrategy(new AbUpdateStrategy(1.d, 1.d)); //Bayesian
                    createProbabilitiesFile((Task_4_Phase) task, abPosteriorsFile, numStages, writeHeader,
                            rulesStage1, rulesStageN, exam.getGridSize(), POSTERIORS);
                } else if (task instanceof Task_6_Phase) {
                    //Create file with normative probabilities
                    rulesStage1.setProbabilityUpdateStrategy(bayesianUpdateStrategy);
                    rulesStageN.setProbabilityUpdateStrategy(bayesianUpdateStrategy);
                    createProbabilitiesFile((Task_6_Phase) task, normativePosteriorsFile, numStages, writeHeader,
                            rulesStage1, rulesStageN, exam.getGridSize(), POSTERIORS);
					//Create file with A-B update strategy probabilities				
                    //rules.setProbabilityUpdateStrategy(new AbUpdateStrategy(1.0, 0.5));					
                    //createProbabilitiesFile((Task_4_Phase)task, abPosteriorsFile, numStages, writeHeader, 
                    //		rules, exam.getGridSize(), POSTERIORS);
                } else if (task instanceof Task_5_Phase) {
                    //Create file with normative probabilities
                    rulesStage1.setProbabilityUpdateStrategy(bayesianUpdateStrategy);
                    rulesStageN.setProbabilityUpdateStrategy(bayesianUpdateStrategy);
                    createProbabilitiesFile((Task_5_Phase) task, normativePosteriorsFile, numStages, writeHeader,
                            rulesStage1, rulesStageN, exam.getGridSize(), POSTERIORS);
                    //Create file with A-B update strategy probabilities				
                    rulesStageN.setProbabilityUpdateStrategy(new AbUpdateStrategy(0.9, 0.5)); //Best fit pilot
                    //rulesStageN.setProbabilityUpdateStrategy(new AbUpdateStrategy(.5d, .5d)); //Conservative
                    //rulesStageN.setProbabilityUpdateStrategy(new AbUpdateStrategy(1.d, 1.d)); //Bayesian
                    createProbabilitiesFile((Task_5_Phase) task, abPosteriorsFile, numStages, writeHeader,
                            rulesStage1, rulesStageN, exam.getGridSize(), POSTERIORS);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
	//String examFileName = BatchFileProcessor.assessmentFolder + "Pilot_Exam/Exam_Data/PilotExam.xml";
        //File outputFolder = new File(BatchFileProcessor.assessmentFolder + "Pilot_Exam/Exam_Design_Assessment");

        String examFileName = BatchFileProcessor.assessmentFolder + "Final_Exam/Exam_Data/FinalExam.xml";
        File outputFolder = new File(BatchFileProcessor.assessmentFolder + "Final_Exam/Exam_Design_Assessment");

        //createAllExamParameterFiles(examFileName, outputFolder);
        createDesignAssessmentFiles(examFileName, outputFolder);
    }
}
