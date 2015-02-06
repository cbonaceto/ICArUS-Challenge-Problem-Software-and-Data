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
package org.mitre.icarus.cps.exam.phase_2.generation;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

import org.mitre.icarus.cps.assessment.score_computer.phase_2.ScoreComputer_Phase2;
import org.mitre.icarus.cps.exam.base.IcarusExam.NormalizationMode;
import org.mitre.icarus.cps.exam.base.loader.IcarusExamLoader;
import org.mitre.icarus.cps.exam.base.training.TutorialNavigationTree;
import org.mitre.icarus.cps.exam.base.training.TutorialTreeNode;
import org.mitre.icarus.cps.exam.base.training.TutorialTreeParentNode;
import org.mitre.icarus.cps.exam.phase_2.IcarusExam_Phase2;
import org.mitre.icarus.cps.exam.phase_2.loader.IcarusExamLoader_Phase2;
import org.mitre.icarus.cps.exam.phase_2.testing.DatumIdentifier;
import org.mitre.icarus.cps.exam.phase_2.testing.DatumType;
import org.mitre.icarus.cps.exam.phase_2.testing.IcarusTestTrial_Phase2;
import org.mitre.icarus.cps.exam.phase_2.testing.Mission;
import org.mitre.icarus.cps.exam.phase_2.testing.MissionType;
import org.mitre.icarus.cps.exam.phase_2.testing.Mission_1_2_3;
import org.mitre.icarus.cps.exam.phase_2.testing.Mission_1_2_3_Trial;
import org.mitre.icarus.cps.exam.phase_2.testing.Mission_4_5_6;
import org.mitre.icarus.cps.exam.phase_2.testing.Mission_4_5_6_Trial;
import org.mitre.icarus.cps.exam.phase_2.testing.ShowdownWinner;
import org.mitre.icarus.cps.exam.phase_2.testing.bluebook.BlueBook;
import org.mitre.icarus.cps.exam.phase_2.testing.bluebook.PayoffMatrix;
import org.mitre.icarus.cps.exam.phase_2.testing.bluebook.RedTacticParameters;
import org.mitre.icarus.cps.exam.phase_2.testing.bluebook.RedTacticParameters.RedTacticConsiderationData;
import org.mitre.icarus.cps.exam.phase_2.testing.bluebook.RedTacticParameters.RedTacticQuadrant;
import org.mitre.icarus.cps.exam.phase_2.testing.bluebook.RedTacticType;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.AttackProbability;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.AttackProbabilityReportProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.BatchPlotProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.BlueAction;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.BlueActionSelectionProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.NormalizationConstraintType;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.RedAction;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.RedActionSelectionProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.RedAction.RedActionType;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbeType;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.int_presentation.HumintPresentationProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.int_presentation.ImintPresentationProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.int_presentation.OsintPresentationProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.int_presentation.SigintPresentationProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.int_presentation.SigintSelectionProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.red_tactics_probes.AbstractRedTacticsProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.red_tactics_probes.MostLikelyRedTacticProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.red_tactics_probes.RedTacticProbability;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.red_tactics_probes.RedTacticsChangesProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.red_tactics_probes.RedTacticsProbabilityReportProbe;
import org.mitre.icarus.cps.exam.phase_2.training.IcarusExamTutorialPage_Phase2;
import org.mitre.icarus.cps.exam.phase_2.training.IcarusExamTutorial_Phase2;
import org.mitre.icarus.cps.experiment_core.condition.InstructionsPage;
import org.mitre.icarus.cps.feature_vector.phase_2.BlueLocation;
import org.mitre.icarus.cps.feature_vector.phase_2.FeatureContainer;
import org.mitre.icarus.cps.feature_vector.phase_2.FeatureVectorFileDescriptor;
import org.mitre.icarus.cps.feature_vector.phase_2.int_datum.SigintDatum;
import org.mitre.icarus.cps.feature_vector.phase_2.int_datum.SigintDatum.SigintType;
import org.mitre.icarus.cps.feature_vector.phase_2.int_datum.HumintDatum;
import org.mitre.icarus.cps.feature_vector.phase_2.int_datum.SigintReliability;
import org.mitre.icarus.cps.feature_vector.phase_2.loader.FeatureVectorLoader;

/**
 *
 * Generates Phase 2 exams.
 *
 * @author CBONACETO
 *
 */
@SuppressWarnings("Convert2Diamond")
public class ExamGenerator {

    /**
     * Exam loader
     */
    protected static final IcarusExamLoader_Phase2 examLoader = IcarusExamLoader_Phase2.getInstance();

    /**
     * Feature vector loader
     */
    protected static final FeatureVectorLoader featureVectorLoader = FeatureVectorLoader.getInstance();

    /**
     * Random number generator
     */
    protected Random random;

    /**
     * Red agent (generates Red actions for all Missions)
     */
    protected final RedAgent redAgent = new RedAgent();

    /**
     * Blue agent (generates Blue actions for Mission 1)
     */
    protected final BlueAgent blueAgent = new BlueAgent();

    /**
     * Whether HUMINT and SIGINT should be considered when assessing the
     * probability of Red attack in Missions 4-6
     */
    protected static final boolean considerHumintSigintForMissions456 = false;

    /**
     * The tactics Red initially plays with on Mission 4
     */
    protected static final RedTacticType MISSION_4_INITIAL_TACTICS = RedTacticType.Mission_4_Aggressive;

    /**
     * The tactics Red switches to on Mission 4
     */
    protected static final RedTacticType MISSION_4_SWITCH_TO_TACTICS = RedTacticType.Mission_4_Passive;

    /**
     * The tactics Red initially plays with on Mission 5
     */
    protected static final RedTacticType MISSION_5_INITIAL_TACTICS = RedTacticType.Mission_5_Psensitive;

    /**
     * The tactics Red switches to on Mission 5
     */
    protected static final RedTacticType MISSION_5_SWITCH_TO_TACTICS = RedTacticType.Mission_5_Usensitive;

    /**
     * The tactics Red initially plays with on Mission 6
     */
    protected static final RedTacticType MISSION_6_INITIAL_TACTICS = RedTacticType.Mission_6_1;

    /**
     * The trials on which Red switches Tactics in Mission 6
     */
    protected static final List<Integer> MISSION_6_SWITCH_TACTICS_TRIALS = Arrays.asList(8, 34, 66);

    /**
     * The tactics Red switches to on Mission 6 on each of the trials on which
     * Red switches tactics
     */
    protected static final List<RedTacticType> MISSION_6_SWITCH_TO_TACTICS = Arrays.asList(
            RedTacticType.Mission_6_2, RedTacticType.Mission_6_3, RedTacticType.Mission_6_4);

    public ExamGenerator() {
        this(new Random(1));
    }

    public ExamGenerator(Random random) {
        this.random = random != null ? random : new Random(1);
    }

    public Random getRandom() {
        return random;
    }

    public void setRandom(Random random) {
        this.random = random;
    }

    /**
     *
     * @param examName
     * @param examId
     * @param examUrl
     * @param createTutorials
     * @param generateSigint
     * @param missionsToGenerate
     * @param batchPlotProbeInterval
     * @param batchPlotProbeOptional
     * @param mission_4_maxNumBatchPlots
     * @param mission_5_maxNumBatchPlots
     * @param mission_4_switchTacticsTrial
     * @param mission_5_switchTacticsTrial
     * @return
     * @throws Exception
     */
    public IcarusExam_Phase2 generateExam(String examName, String examId, URL examUrl,
            boolean createTutorials, boolean generateSigint, List<Boolean> missionsToGenerate,
            int batchPlotProbeInterval, boolean batchPlotProbeOptional,
            int mission_4_maxNumBatchPlots, int mission_5_maxNumBatchPlots,
            int mission_4_switchTacticsTrial, int mission_5_switchTacticsTrial) throws Exception {
        IcarusExam_Phase2 exam = new IcarusExam_Phase2();
        exam.setProgramPhaseId("2");
        exam.setName(examName);
        exam.setId(examId);
        exam.setNormalizationMode(NormalizationMode.NormalizeDuringManual);
        exam.setExamTimeStamp(Calendar.getInstance().getTime());

        //Create the exam tutorial
        if (createTutorials) {
            IcarusExamTutorial_Phase2 tutorial = new IcarusExamTutorial_Phase2();
            exam.setTutorial(tutorial);
            tutorial.setName("Exam Tutorial");
            tutorial.setId("Exam_Tutorial");
            ArrayList<IcarusExamTutorialPage_Phase2> tutorialPages = new ArrayList<IcarusExamTutorialPage_Phase2>();
            tutorial.setTutorialPages(tutorialPages);
            for (int i = 1; i <= 25; i++) {
                tutorialPages.add(new IcarusExamTutorialPage_Phase2("../tutorial/Slide" + i + ".PNG"));
            }
            tutorial.setTutorialNavigationTree(new TutorialNavigationTree("Exam Tutorial",
                    Collections.unmodifiableList(Arrays.asList(
                                    new TutorialTreeParentNode(
                                            "Exam Tutorial", "Overview of the Task", 0),
                                    new TutorialTreeParentNode(
                                            "Exam Tutorial", "Scoring and Tactics", 8,
                                            Collections.unmodifiableList(Arrays.asList(
                                                            new TutorialTreeNode("Exam Tutorial", "Showdown", 9),
                                                            new TutorialTreeNode("Exam Tutorial", "Scoring", 10),
                                                            new TutorialTreeNode("Exam Tutorial", "Tactics", 11)))),
                                    new TutorialTreeParentNode(
                                            "Exam Tutorial", "Intelligence Reports", 12,
                                            Collections.unmodifiableList(Arrays.asList(
                                                            new TutorialTreeNode("Exam Tutorial", "OSINT", 14),
                                                            new TutorialTreeNode("Exam Tutorial", "IMINT", 16),
                                                            new TutorialTreeNode("Exam Tutorial", "BLUEBOOK", 18),
                                                            new TutorialTreeNode("Exam Tutorial", "HUMINT", 20),
                                                            new TutorialTreeNode("Exam Tutorial", "SIGINT", 22),
                                                            new TutorialTreeNode("Exam Tutorial", "Notation for P(Attack|INTS)", 24))))
                            ))));
        }

        //Create the BlueBook, SIGINT Reliability info, and Payoff matrix
        BlueBook blueBook = BlueBook.createDefaultBlueBook();
        exam.setBlueBook(blueBook);
        /*blueBook.setMission_1_TacticsInstructions(new InstructionsPage("tutorial/Mission_1_RedTactics.png"));
         blueBook.setMission_2_TacticsInstructions(new InstructionsPage("tutorial/Mission_2_RedTactics.png"));
         blueBook.setMission_3_TacticsInstructions(new InstructionsPage("tutorial/Mission_3_RedTactics.png"));		
         blueBook.setMission_4_TacticsInstructions(new InstructionsPage("tutorial/Mission_4_RedTactics.png"));*/

        PayoffMatrix payoffMatrix = PayoffMatrix.createDefaultPayoffMatrix();
        exam.setPayoffMatrix(payoffMatrix);
        //payoffMatrix.setPayoffMatrixInstructions(new InstructionsPage("tutorial/PayoffMatrix.png"));		

        SigintReliability sigintReliability = SigintReliability.createDefaultSigintReliability();
        exam.setSigintReliability(sigintReliability);
		//sigintReliability.setSigintReliabilityInstructions(new InstructionsPage("tutorial/SigintReliability.png"));	

        //Create the missions and mission tutorials
        ArrayList<Mission<?>> missions = new ArrayList<Mission<?>>(3);
        exam.setMissions(missions);
        ArrayList<InstructionsPage> instructions;

        //Create Mission 1
        if (missionsToGenerate == null || missionsToGenerate.size() < 1 || missionsToGenerate.get(0)) {
            Mission<?> mission1 = generateMission(MissionType.Mission_1, examId, examUrl,
                    RedTacticType.Mission_1, sigintReliability, generateSigint, null, false);
            missions.add(mission1);
            if (createTutorials) {
                instructions = new ArrayList<InstructionsPage>();
                mission1.setInstructionPages(instructions);
                for (int i = 27; i <= 33; i++) {
                    instructions.add(new InstructionsPage("../tutorial/Slide" + i + ".PNG"));
                }
            }
        }

        //Create Mission 2
        if (missionsToGenerate == null || missionsToGenerate.size() < 2 || missionsToGenerate.get(1)) {
            Mission<?> mission2 = generateMission(MissionType.Mission_2, examId, examUrl,
                    RedTacticType.Mission_2_Aggressive, sigintReliability,
                    generateSigint, Arrays.asList(RedTacticType.Mission_2_Passive,
                            RedTacticType.Mission_2_Aggressive), false);
            missions.add(mission2);
            if (createTutorials) {
                instructions = new ArrayList<InstructionsPage>();
                mission2.setInstructionPages(instructions);
                for (int i = 34; i <= 42; i++) {
                    instructions.add(new InstructionsPage("../tutorial/Slide" + i + ".PNG"));
                }
            }
        }

        //Create Mission 3
        if (missionsToGenerate == null || missionsToGenerate.size() < 3 || missionsToGenerate.get(2)) {
            Mission<?> mission3 = generateMission(MissionType.Mission_3, examId, examUrl,
                    RedTacticType.Mission_3, sigintReliability, generateSigint, null, false);
            missions.add(mission3);
            if (createTutorials) {
                instructions = new ArrayList<InstructionsPage>();
                mission3.setInstructionPages(instructions);
                for (int i = 43; i <= 51; i++) {
                    instructions.add(new InstructionsPage("../tutorial/Slide" + i + ".PNG"));
                }
            }
        }

        //Create Mission 4
        if (missionsToGenerate == null || missionsToGenerate.size() < 4 || missionsToGenerate.get(3)) {
			//First, generate the Red tactic to use for each trial
            //Red starts out aggressive and switches to passive at trial 21
            String blueLocationsFileName = "Mission4_Blue_Locations.xml";
            FeatureContainer<BlueLocation> blueLocations = featureVectorLoader.unmarshalBlueLocations(
                    IcarusExamLoader_Phase2.createUrl(examUrl, blueLocationsFileName), false,
                    null, null);
            int numTrials = blueLocations != null ? blueLocations.size() : 1;
            List<RedTacticType> redTacticsForTrials = new ArrayList<RedTacticType>(numTrials);
            for (int trial = 0; trial < numTrials; trial++) {
                redTacticsForTrials.add(
                        trial + 1 >= mission_4_switchTacticsTrial
                        ? ExamGenerator.MISSION_4_SWITCH_TO_TACTICS
                        : ExamGenerator.MISSION_4_INITIAL_TACTICS);
            }
            //Generate the mission
            Mission<?> mission4 = generateMission(MissionType.Mission_4, "Mission 4", "Mission4", examId,
                    redTacticsForTrials, sigintReliability, generateSigint,
                    Arrays.asList(RedTacticType.Mission_4_Passive, RedTacticType.Mission_4_Aggressive),
                    false, 1, batchPlotProbeInterval, batchPlotProbeOptional, mission_4_maxNumBatchPlots,
                    "Mission4_Area_Of_Interest.xml", blueLocationsFileName,
                    blueLocations != null ? blueLocations.getFeatureList() : null);
            missions.add(mission4);
            if (createTutorials) {
                instructions = new ArrayList<InstructionsPage>();
                mission4.setInstructionPages(instructions);
                for (int i = 52; i <= 64; i++) {
                    instructions.add(new InstructionsPage("../tutorial/Slide" + i + ".PNG"));
                }
            }
        }

        //Create Mission 5
        if (missionsToGenerate == null || missionsToGenerate.size() < 5 || missionsToGenerate.get(4)) {
			//First, generate the Red tactic to use for each trial
            //Red starts out Psensitive and switches to Usensitive at trial 9
            String blueLocationsFileName = "Mission5_Blue_Locations.xml";
            FeatureContainer<BlueLocation> blueLocations = featureVectorLoader.unmarshalBlueLocations(
                    IcarusExamLoader_Phase2.createUrl(examUrl, blueLocationsFileName), false,
                    null, null);
            int numTrials = blueLocations != null ? blueLocations.size() : 1;
            List<RedTacticType> redTacticsForTrials = new ArrayList<RedTacticType>(numTrials);
            for (int trial = 0; trial < numTrials; trial++) {
                redTacticsForTrials.add(
                        trial + 1 >= mission_5_switchTacticsTrial
                        ? ExamGenerator.MISSION_5_SWITCH_TO_TACTICS
                        : ExamGenerator.MISSION_5_INITIAL_TACTICS);
            }
            //Generate the mission
            Mission<?> mission5 = generateMission(MissionType.Mission_5, "Mission 5", "Mission5", examId,
                    redTacticsForTrials, sigintReliability, generateSigint,
                    Arrays.asList(RedTacticType.Mission_5_Psensitive, RedTacticType.Mission_5_Usensitive),
                    false, 1, batchPlotProbeInterval, batchPlotProbeOptional, mission_5_maxNumBatchPlots,
                    "Mission5_Area_Of_Interest.xml", blueLocationsFileName,
                    blueLocations != null ? blueLocations.getFeatureList() : null);
            missions.add(mission5);
            if (createTutorials) {
                instructions = new ArrayList<InstructionsPage>();
                mission5.setInstructionPages(instructions);
                for (int i = 65; i <= 72; i++) {
                    instructions.add(new InstructionsPage("../tutorial/Slide" + i + ".PNG"));
                }
            }
        }

        //Create Mission 6
        if (missionsToGenerate == null || missionsToGenerate.size() < 6 || missionsToGenerate.get(5)) {
            //First, generate the Red tactic to use for each trial
            String blueLocationsFileName = "Mission6_Blue_Locations.xml";
            FeatureContainer<BlueLocation> blueLocations = featureVectorLoader.unmarshalBlueLocations(
                    IcarusExamLoader_Phase2.createUrl(examUrl, blueLocationsFileName), false,
                    null, null);
            int numTrials = blueLocations != null ? blueLocations.size() : 1;
            List<RedTacticType> redTacticsForTrials = new ArrayList<RedTacticType>(numTrials);
            int nextSwitchToTrialIndex = 0;
            RedTacticType currentTactic = ExamGenerator.MISSION_6_INITIAL_TACTICS;
            for (int trial = 0; trial < numTrials; trial++) {
                if (MISSION_6_SWITCH_TACTICS_TRIALS != null
                        && nextSwitchToTrialIndex < MISSION_6_SWITCH_TACTICS_TRIALS.size()
                        && trial + 1 >= MISSION_6_SWITCH_TACTICS_TRIALS.get(nextSwitchToTrialIndex)) {
                    currentTactic = ExamGenerator.MISSION_6_SWITCH_TO_TACTICS.get(nextSwitchToTrialIndex);
                    nextSwitchToTrialIndex++;
                }
                redTacticsForTrials.add(currentTactic);
            }
            //Generate the mission
            Mission<?> mission6 = generateMission(MissionType.Mission_6, "Mission 6", "Mission6", examId,
                    redTacticsForTrials, sigintReliability, generateSigint,
                    null, false, 0, 0, false, null, "Mission6_Area_Of_Interest.xml", blueLocationsFileName,
                    blueLocations != null ? blueLocations.getFeatureList() : null);
            missions.add(mission6);
            /*if(createTutorials) {
             instructions = new ArrayList<InstructionsPage>();
             mission6.setInstructionPages(instructions);
             for(int i=73; i<=80; i++) {
             instructions.add(new InstructionsPage("../tutorial/Slide" + i + ".PNG"));
             }
             }*/
        }

        //Display the P and U distribution for each mission
        for (Mission<?> mission : missions) {
            System.out.println("P and U Distribution for " + mission.getId());
            int[] quadrantCounts = calculatePandUdistribution(mission);
            int totalLocations = mission.getTestTrials().size() * mission.getMissionType().getLocationsPerTrial();
            for (RedTacticQuadrant quadrant : RedTacticQuadrant.values()) {
                System.out.println(quadrant.toString() + ": " + quadrantCounts[quadrant.ordinal()] + "/"
                        + totalLocations);
            }
            System.out.println();
        }

        return exam;
    }

    /**
     * @param missionType
     * @param examId
     * @param examUrl
     * @param redTacticForMission
     * @param possibleRedTactics
     * @param sigintReliability
     * @param generateSigint
     * @param showRedTacticProbeOnFirstTrial
     * @return
     * @throws Exception
     */
    public Mission<? extends IcarusTestTrial_Phase2> generateMission(
            MissionType missionType, String examId, URL examUrl,
            RedTacticType redTacticForMission,
            SigintReliability sigintReliability, boolean generateSigint,
            List<RedTacticType> possibleRedTactics,
            boolean showRedTacticProbeOnFirstTrial) throws Exception {
        return generateMission(missionType, examId, examUrl, redTacticForMission,
                sigintReliability, generateSigint, possibleRedTactics,
                showRedTacticProbeOnFirstTrial, 1, 1, false, null);
    }

    /**
     * @param missionType
     * @param examId
     * @param examUrl
     * @param redTacticForMission
     * @param sigintReliability
     * @param generateSigint
     * @param possibleRedTactics
     * @param showRedTacticProbeOnFirstTrial
     * @param redTacticProbeInterval
     * @param batchPlotProbeInterval
     * @param batchPlotProbeOptional
     * @param maxNumBatchPlots
     * @return
     * @throws Exception
     */
    public Mission<? extends IcarusTestTrial_Phase2> generateMission(
            MissionType missionType, String examId, URL examUrl,
            RedTacticType redTacticForMission, SigintReliability sigintReliability,
            boolean generateSigint, List<RedTacticType> possibleRedTactics,
            boolean showRedTacticProbeOnFirstTrial, int redTacticProbeInterval,
            int batchPlotProbeInterval, boolean batchPlotProbeOptional, Integer maxNumBatchPlots) throws Exception {
        String missionName = "";
        String missionId = "";
        switch (missionType) {
            case Mission_1:
                missionName = "Mission 1";
                missionId = "Mission1";
                break;
            case Mission_2:
                missionName = "Mission 2";
                missionId = "Mission2";
                break;
            case Mission_3:
                missionName = "Mission 3";
                missionId = "Mission3";
                break;
            case Mission_4:
                missionName = "Mission 4";
                missionId = "Mission4";
                break;
            case Mission_5:
                missionName = "Mission 5";
                missionId = "Mission5";
                break;
            case Mission_6:
                missionName = "Mission 6";
                missionId = "Mission6";
                break;
        }
        String blueLocationsFileName = missionId + "_Blue_Locations.xml";
        FeatureContainer<BlueLocation> blueLocations = featureVectorLoader.unmarshalBlueLocations(
                IcarusExamLoader_Phase2.createUrl(examUrl, blueLocationsFileName), false,
                null, null);
        return generateMission(missionType, missionName, missionId, examId,
                Collections.singletonList(redTacticForMission),
                sigintReliability, generateSigint, possibleRedTactics, showRedTacticProbeOnFirstTrial,
                redTacticProbeInterval, batchPlotProbeInterval, batchPlotProbeOptional, maxNumBatchPlots,
                missionId + "_Area_Of_Interest.xml", blueLocationsFileName,
                blueLocations != null ? blueLocations.getFeatureList() : null);
    }

    /**
     * @param missionType
     * @param missionName
     * @param missionId
     * @param examId
     * @param redTacticsForTrials
     * @param sigintReliability
     * @param generateSigint
     * @param possibleRedTactics
     * @param showRedTacticProbeOnFirstTrial
     * @param redTacticProbeInterval
     * @param batchPlotProbeInterval
     * @param batchPlotProbeOptional
     * @param maxNumBatchPlots
     * @param aoiFileName
     * @param blueLocationsFileName
     * @param blueLocations
     * @return
     * @throws Exception
     */    
    public Mission<? extends IcarusTestTrial_Phase2> generateMission(
            MissionType missionType, String missionName, String missionId, String examId,
            List<RedTacticType> redTacticsForTrials, SigintReliability sigintReliability, boolean generateSigint,
            List<RedTacticType> possibleRedTactics, boolean showRedTacticProbeOnFirstTrial,
            int redTacticProbeInterval, int batchPlotProbeInterval, boolean batchPlotProbeOptional,
            Integer maxNumBatchPlots, String aoiFileName, String blueLocationsFileName,
            List<BlueLocation> blueLocations) throws Exception {
        boolean isMission123 = missionType == MissionType.Mission_1 || missionType == MissionType.Mission_2
                || missionType == MissionType.Mission_3;
        boolean isMission2 = missionType == MissionType.Mission_2;
        boolean isMission456 = missionType == MissionType.Mission_4 || missionType == MissionType.Mission_5
                || missionType == MissionType.Mission_6;
        boolean isMission6 = missionType == MissionType.Mission_6;
        Mission<? extends IcarusTestTrial_Phase2> mission = isMission123 ? new Mission_1_2_3() : new Mission_4_5_6();
        mission.setProgramPhaseId("2");
        mission.setExamId(examId);
        mission.setMissionType(missionType);
        mission.setName(missionName);
        mission.setId(missionId);
        mission.setAoiFile(new FeatureVectorFileDescriptor(aoiFileName));
        mission.setBlueLocationsFile(new FeatureVectorFileDescriptor(blueLocationsFileName));
        mission.setBlueLocations(blueLocations);

        //Generate trials based on the Blue locations		
        if (blueLocations != null && !blueLocations.isEmpty()) {
            int locationsPerTrial = mission.getMissionType() != null ? mission.getMissionType().getLocationsPerTrial() : 1;
            int numTrials = blueLocations.size() / locationsPerTrial;
            if (isMission6) {
                numTrials++;
            }
            int numTrialsSinceLastAttack = 0;
            double redCapability_Pc;
            int numTrialsSinceRedTacticProbe = 1;
            int numTrialsSinceBatchPlotProbe = 1;
            int numBatchPlots = 0;

            RedTacticType redTacticForMission = null;
            ArrayList<Mission_1_2_3_Trial> mission123Trials = null;
            ArrayList<Mission_4_5_6_Trial> mission456Trials = null;
            if (isMission123) {
                Mission_1_2_3 mission123 = (Mission_1_2_3) mission;
                mission123Trials = new ArrayList<Mission_1_2_3_Trial>(numTrials);
                mission123.setTestTrials(mission123Trials);
                if (redTacticsForTrials != null && !redTacticsForTrials.isEmpty()) {
                    redTacticForMission = redTacticsForTrials.get(0);
                    mission123.setRedTactic(redTacticForMission);
                }
            } else if (isMission456) {
                Mission_4_5_6 mission456 = (Mission_4_5_6) mission;
                mission456Trials = new ArrayList<Mission_4_5_6_Trial>(numTrials);
                mission456.setTestTrials(mission456Trials);
                if (!isMission6) {
                    mission456.setMaxNumBatchPlots(maxNumBatchPlots == null || maxNumBatchPlots > numTrials ? null : maxNumBatchPlots);
                }
                if (redTacticsForTrials != null && redTacticsForTrials.size() == 1) {
                    redTacticForMission = redTacticsForTrials.get(0);
                }
            }

            Iterator<BlueLocation> locationIter = blueLocations.iterator();
            for (int i = 0; i < numTrials; i++) {
                List<BlueLocation> locationsForTrial = null;
                RedTacticType redTactic = null;
                RedAction redAction = null;
                redCapability_Pc = ScoreComputer_Phase2.computeRedCapability_Pc(numTrialsSinceLastAttack);
                if (!(isMission6 && i == numTrials - 1)) {
                    //Get the locations for the trial
                    locationsForTrial = new ArrayList<BlueLocation>(locationsPerTrial);
                    int locationIndex = 0;
                    while (locationIter.hasNext() && locationIndex < locationsPerTrial) {
                        locationsForTrial.add(locationIter.next());
                        locationIndex++;
                    }

                    //Generate the Red action for the trial
                    redTactic = redTacticForMission != null ? redTacticForMission
                            : redTacticsForTrials != null && i < redTacticsForTrials.size() ? redTacticsForTrials.get(i)
                            : RedTacticType.Mission_1;
                    redAction = redAgent.selectRedAction(locationsForTrial, redTactic.getTacticParameters(), 
                            missionType.isConsiderRedCapability_Pc() ? redCapability_Pc : null);
                }

                //Generate SIGINT data based on the Red action if necessary
                if (generateSigint) {
                    generateSigintAtLocations(locationsForTrial, redAction, sigintReliability, false);
                }

                //Create the trial
                boolean showRedTacticsProbe = false;
                boolean showBatchPlotProbe = false;
                if (isMission6) {
                    if (i == numTrials - 1) {
                        //The red tactics probe is on the last trial in Mission 6
                        showRedTacticsProbe = true;
                        showBatchPlotProbe = i > 0;
                    }
                } else if (isMission456 || isMission2) {
                    if ((redTacticProbeInterval == 0 || numTrialsSinceRedTacticProbe >= redTacticProbeInterval)
                            && (showRedTacticProbeOnFirstTrial || i > 0)) {
                        showRedTacticsProbe = true;
                        numTrialsSinceRedTacticProbe = 1;
                        if (batchPlotProbeInterval == 0 || numTrialsSinceBatchPlotProbe >= batchPlotProbeInterval
                                && (batchPlotProbeOptional || maxNumBatchPlots == null
                                || maxNumBatchPlots == 0 || numBatchPlots < maxNumBatchPlots)) {
                            showBatchPlotProbe = true;
                            numTrialsSinceBatchPlotProbe = 1;
                            numBatchPlots++;
                        } else {
                            numTrialsSinceBatchPlotProbe++;
                        }
                    } else {
                        numTrialsSinceRedTacticProbe++;
                        numTrialsSinceBatchPlotProbe++;
                    }
                }
                IcarusTestTrial_Phase2 trial = generateTrial(i + 1,
                        isMission6 && i < numTrials - 1, isMission6 && i == numTrials - 1,
                        missionType, locationsForTrial, redTactic,
                        sigintReliability, numTrialsSinceLastAttack, redCapability_Pc, true, showRedTacticsProbe,
                        possibleRedTactics, showBatchPlotProbe, batchPlotProbeOptional, redAction);
                if (mission456Trials != null) {
                    mission456Trials.add((Mission_4_5_6_Trial) trial);
                } else if (mission123Trials != null) {
                    mission123Trials.add((Mission_1_2_3_Trial) trial);
                }

                //Determine who will win the showdown at each location if there is a showdown at a location
                if (locationsForTrial != null && !locationsForTrial.isEmpty()) {
                    ArrayList<ShowdownWinner> showdownWinner = new ArrayList<ShowdownWinner>(locationsForTrial.size());
                    trial.setShowdownWinner(showdownWinner);
                    int locationIndex = 0;
                    for (BlueLocation location : locationsForTrial) {
                        showdownWinner.add(new ShowdownWinner(location.getId(), locationIndex,
                                PayoffMatrix.determineShowdownWinner(
                                        location.getOsint() != null ? location.getOsint().getRedVulnerability_P() : 0D, random)));
                        locationIndex++;
                    }
                }

                //Update the number of trials since the last attack
                if (trial.getRedActionSelection() != null
                        && trial.getRedActionSelection().getRedAction().getAction() == RedActionType.Attack) {
                    numTrialsSinceLastAttack = 1;
                } else {
                    if (numTrialsSinceLastAttack > 0) {
                        numTrialsSinceLastAttack++;
                    }
                }
            }
        }
        return mission;
    }

    /**
     * @param trialNum
     * @param attackHistoryTrial
     * @param redTacticsChangesProbeOnlyTrial
     * @param missionType
     * @param blueLocations
     * @param redTactic
     * @param sigintReliability
     * @param numTrialsSinceLastAttack
     * @param redCapability_Pc
     * @param presentIntsSimultaneously
     * @param showRedTacticsProbe
     * @param possibleRedTactics
     * @param showBatchPlotProbe
     * @param batchPlotProbeOptional
     * @return
     */
    public IcarusTestTrial_Phase2 generateTrial(int trialNum,
            boolean attackHistoryTrial, boolean redTacticsChangesProbeOnlyTrial,
            MissionType missionType, List<BlueLocation> blueLocations,
            RedTacticType redTactic, SigintReliability sigintReliability,
            int numTrialsSinceLastAttack, Double redCapability_Pc, boolean presentIntsSimultaneously,
            boolean showRedTacticsProbe, List<RedTacticType> possibleRedTactics,
            boolean showBatchPlotProbe, boolean batchPlotProbeOptional) {
        return generateTrial(trialNum, attackHistoryTrial, redTacticsChangesProbeOnlyTrial,
                missionType, blueLocations, redTactic, sigintReliability,
                numTrialsSinceLastAttack, redCapability_Pc, presentIntsSimultaneously,
                showRedTacticsProbe, possibleRedTactics, showBatchPlotProbe, batchPlotProbeOptional,
                redAgent.selectRedAction(blueLocations, redTactic.getTacticParameters(), 
                        missionType.isConsiderRedCapability_Pc() ? redCapability_Pc : null));
    }

    /**
     * @param trialNum
     * @param attackHistoryTrial
     * @param redTacticsChangesProbeOnlyTrial
     * @param missionType
     * @param blueLocations
     * @param redTactic
     * @param sigintReliability
     * @param numTrialsSinceLastAttack
     * @param redCapability_Pc
     * @param presentIntsSimultaneously
     * @param showRedTacticsProbe
     * @param possibleRedTactics
     * @param showBatchPlotProbe
     * @param batchPlotProbeOptional
     * @param redAction
     * @return
     */
    public IcarusTestTrial_Phase2 generateTrial(int trialNum,
            boolean attackHistoryTrial, boolean redTacticsChangesProbeOnlyTrial,
            MissionType missionType, List<BlueLocation> blueLocations, RedTacticType redTactic,
            SigintReliability sigintReliability, int numTrialsSinceLastAttack, Double redCapability_Pc,
            boolean presentIntsSimultaneously, boolean showRedTacticsProbe,
            List<RedTacticType> possibleRedTactics, boolean showBatchPlotProbe,
            boolean batchPlotProbeOptional, RedAction redAction) {
        boolean isMission123 = missionType == MissionType.Mission_1 || missionType == MissionType.Mission_2
                || missionType == MissionType.Mission_3;
        boolean isMission456 = missionType == MissionType.Mission_4 || missionType == MissionType.Mission_5
                || missionType == MissionType.Mission_6;
        IcarusTestTrial_Phase2 trial = isMission123 ? new Mission_1_2_3_Trial() : new Mission_4_5_6_Trial();
        trial.setTrialNum(trialNum);
        if (!redTacticsChangesProbeOnlyTrial
                && (blueLocations == null || blueLocations.isEmpty())) {
            throw new IllegalArgumentException("Error generating trial: Blue locations was missing or empty.");
        }
        if (isMission456 && !redTacticsChangesProbeOnlyTrial) {
            ((Mission_4_5_6_Trial) trial).setRedTactic(redTactic);
        }

        //Create the HUMINT data
        HumintDatum humint = new HumintDatum(numTrialsSinceLastAttack,
                ScoreComputer_Phase2.computeRedCapability_Pc(numTrialsSinceLastAttack));

        if (redTacticsChangesProbeOnlyTrial) {
            //Create a Red tactics changes probe for Mission 6
            RedTacticsChangesProbe redTacticsProbe = new RedTacticsChangesProbe();
            ((Mission_4_5_6_Trial) trial).setRedTacticsProbe(redTacticsProbe);
            redTacticsProbe.setName("Red Tactics Changes");
            redTacticsProbe.setDataConsidered(RedTacticConsiderationData.P_Only);
            redTacticsProbe.setMinNumRedTacticsChanges(1);
            redTacticsProbe.setMaxNumRedTacticsChanges(5);
            //if(trialNum > 1) {
            if (showBatchPlotProbe) {
                redTacticsProbe.setBatchPlotProbe(new BatchPlotProbe(
                        createPreviousTrialsList(trialNum), 1,
                        batchPlotProbeOptional));
            }
        } else {
            trial.setBlueLocations(blueLocations);
            int numLocations = blueLocations.size();          
            List<String> locationIds = new ArrayList<String>(numLocations);            
            List<Integer> locationIndexes = new ArrayList<Integer>(numLocations);
            int locationIndex = 0;
            for (BlueLocation location : blueLocations) {
                locationIds.add(location.getId());
                locationIndexes.add(locationIndex);
                locationIndex++;
            }

            if (attackHistoryTrial) {
                trial.setAttackHistoryTrial(true);
                //Create the Blue action presentation
                BlueActionSelectionProbe blueActionSelection = new BlueActionSelectionProbe();
                trial.setBlueActionSelection(blueActionSelection);
                blueActionSelection.setDataProvidedToParticipant(true);
                blueActionSelection.setBlueActions(blueAgent.selectBlueActions(blueLocations,
                        redTactic.getTacticParameters(), humint, null, null));
                //Create the Red action presentation
                trial.setRedActionSelection(new RedActionSelectionProbe(redAction));
            } else {
                //Set the HUMINT data
                trial.setHumint(humint);

                //Create the OSINT presentation(s)
                if (isMission123) {
                    if (presentIntsSimultaneously) {
                        trial.setOsintPresentation(Collections.singletonList(
                                new OsintPresentationProbe(locationIds, locationIndexes)));
                    } else {
                        ArrayList<OsintPresentationProbe> osintPresentation = new ArrayList<OsintPresentationProbe>(numLocations);
                        trial.setOsintPresentation(osintPresentation);
                        Integer index = 0;
                        for (String locationId : locationIds) {
                            osintPresentation.add(new OsintPresentationProbe(Arrays.asList(locationId), Arrays.asList(index)));
                            index++;
                        }
                    }
                }

                //Create the IMINT presentation(s)
                if (isMission123) {
                    if (presentIntsSimultaneously) {
                        trial.setImintPresentation(Collections.singletonList(
                                new ImintPresentationProbe(locationIds, locationIndexes)));
                    } else {
                        ArrayList<ImintPresentationProbe> imintPresentation = new ArrayList<ImintPresentationProbe>(numLocations);
                        trial.setImintPresentation(imintPresentation);
                        int index = 0;
                        for (String locationId : locationIds) {
                            imintPresentation.add(new ImintPresentationProbe(Arrays.asList(locationId), Arrays.asList(index)));
                            index++;
                        }
                    }
                }

                if (showRedTacticsProbe) {
                    if (missionType == MissionType.Mission_2) {
                        //Create the most likely Red tactic probe for Mission 2
                        if (possibleRedTactics != null && !possibleRedTactics.isEmpty()) {
                            ((Mission_1_2_3_Trial) trial).setMostLikelyRedTacticProbe(new MostLikelyRedTacticProbe(
                                    new TreeSet<RedTacticType>(possibleRedTactics)));
                            ((Mission_1_2_3_Trial) trial).getMostLikelyRedTacticProbe().setName("Red Style Selection");
                        }
                    } else if (missionType == MissionType.Mission_4 || missionType == MissionType.Mission_5) {
                        //Create the Red tactics probability probe (Missions 4 and 5)
                        List<DatumIdentifier> datumList = new ArrayList<DatumIdentifier>();
                        //if(trialNum > 1) {
                        if (showBatchPlotProbe) {
                            datumList.add(new DatumIdentifier(DatumType.BatchPlots));
                        }
                        if (missionType == MissionType.Mission_4 || missionType == MissionType.Mission_5) {
                            datumList.add(new DatumIdentifier(DatumType.BlueBook));
                        }
                        AbstractRedTacticsProbe redTacticsProbe = null;
                        if (missionType == MissionType.Mission_4 || missionType == MissionType.Mission_5) {
                            //Create the Red tactics probability probe (Missions 4 and 5)
                            if (possibleRedTactics != null && !possibleRedTactics.isEmpty()) {
                                ArrayList<RedTacticProbability> redTacticProbs = new ArrayList<RedTacticProbability>(
                                        possibleRedTactics.size());
                                for (RedTacticType possibleRedTactic : possibleRedTactics) {
                                    redTacticProbs.add(new RedTacticProbability(possibleRedTactic));
                                }
                                RedTacticsProbabilityReportProbe probe
                                        = new RedTacticsProbabilityReportProbe(datumList, redTacticProbs);
                                probe.setName("P(Red Style)");
                                probe.setTargetSum(100D);
                                probe.setNormalizationConstraint(NormalizationConstraintType.EqualTo);
                                redTacticsProbe = probe;
                            }
                        }
                        if (redTacticsProbe != null) {
                            //if(trialNum > 1) {
                            if (showBatchPlotProbe) {
                                redTacticsProbe.setBatchPlotProbe(new BatchPlotProbe(
                                        createPreviousTrialsList(trialNum), numLocations,
                                        batchPlotProbeOptional));
                            }
                            ((Mission_4_5_6_Trial) trial).setRedTacticsProbe(redTacticsProbe);
                        }
                    }
                }

                //Create the attack probability probe after OSINT and IMINT
                if (isMission123 || !considerHumintSigintForMissions456) {
                    ArrayList<DatumIdentifier> datumList = new ArrayList<DatumIdentifier>(numLocations);
                    ArrayList<AttackProbability> probs = new ArrayList<AttackProbability>(numLocations);
                    int index = 0;
                    for (String locationId : locationIds) {
                        datumList.add(new DatumIdentifier(locationId, null, DatumType.OSINT));
                        datumList.add(new DatumIdentifier(locationId, null, DatumType.IMINT));
                        probs.add(new AttackProbability(locationId, index, RedActionType.Attack));
                        index++;
                    }
                    datumList.add(new DatumIdentifier(DatumType.BlueBook));
                    AttackProbabilityReportProbe pp = new AttackProbabilityReportProbe(
                            TrialPartProbeType.AttackProbabilityReport_Pp,
                            "Pp", datumList, probs);
                    pp.setName("P(Attack|IMINT, OSINT)");
                    pp.setTargetSum(100D);
                    pp.setNormalizationConstraint(NormalizationConstraintType.LessThanOrEqualTo);
                    trial.setAttackPropensityProbe_Pp(pp);
                }

                if (isMission123 || considerHumintSigintForMissions456) {
                    //Create the HUMINT presentation(s)
                    if (presentIntsSimultaneously) {
                        trial.setHumintPresentation(Collections.singletonList(
                                new HumintPresentationProbe(locationIds, locationIndexes)));
                    } else {
                        ArrayList<HumintPresentationProbe> humintPresentation = new ArrayList<HumintPresentationProbe>(numLocations);
                        trial.setHumintPresentation(humintPresentation);
                        int index = 0;
                        for (String locationId : locationIds) {
                            humintPresentation.add(new HumintPresentationProbe(Arrays.asList(locationId), Arrays.asList(index)));
                            index++;
                        }
                    }

                    //Create the attack probability probe after HUMINT		
                    ArrayList<DatumIdentifier> datumList = new ArrayList<DatumIdentifier>(numLocations);
                    ArrayList<AttackProbability> probs = new ArrayList<AttackProbability>(numLocations);
                    int index = 0;
                    for (String locationId : locationIds) {
                        if (trial.getAttackPropensityProbe_Pp() != null) {
                            datumList.add(new DatumIdentifier(locationId, "Pp", DatumType.AttackProbabilityReport_Propensity));
                        }
                        datumList.add(new DatumIdentifier(locationId, null, DatumType.HUMINT));
                        probs.add(new AttackProbability(locationId, index, RedActionType.Attack));
                        index++;
                    }
                    AttackProbabilityReportProbe pc = new AttackProbabilityReportProbe(
                            TrialPartProbeType.AttackProbabilityReport_Ppc,
                            "Ppc", datumList, probs);
                    pc.setName("P(Attack|HUMINT, IMINT, OSINT)");
                    pc.setTargetSum(100D);
                    pc.setNormalizationConstraint(NormalizationConstraintType.LessThanOrEqualTo);
                    trial.setAttackProbabilityProbe_Ppc(pc);
                }

                //Create the SIGINT selection probe
                if (missionType == MissionType.Mission_3) {
                    trial.setSigintSelectionProbe(new SigintSelectionProbe(locationIds, locationIndexes, 1));
                    trial.getSigintSelectionProbe().setName("SIGINT Selection");
                }

                //Create the SIGINT presentation(s)
                int index = 0;
                if (isMission123) {
                    if (missionType == MissionType.Mission_3) {
                        trial.setSigintPresentation(Collections.singletonList(new SigintPresentationProbe()));
                    } else {
                        if (presentIntsSimultaneously) {
                            trial.setSigintPresentation(Collections.singletonList(
                                    new SigintPresentationProbe(locationIds, locationIndexes)));
                        } else {
                            ArrayList<SigintPresentationProbe> sigintPresentation = new ArrayList<SigintPresentationProbe>();
                            trial.setSigintPresentation(sigintPresentation);
                            for (String locationId : locationIds) {
                                sigintPresentation.add(new SigintPresentationProbe(Arrays.asList(locationId), Arrays.asList(index)));
                                index++;
                            }
                        }
                    }
                }

                //Create the attack probability probe after SIGINT
                boolean reportPt = missionType == MissionType.Mission_1 && numLocations == 1;
                if (reportPt) {
                    ArrayList<DatumIdentifier> datumList = new ArrayList<DatumIdentifier>(numLocations);
                    ArrayList<AttackProbability> probs = new ArrayList<AttackProbability>(numLocations);
                    index = 0;
                    for (String locationId : locationIds) {
                        datumList.add(new DatumIdentifier(locationId, null, DatumType.SIGINT));
                        probs.add(new AttackProbability(locationId, index, RedActionType.Attack));
                        index++;
                    }
                    datumList.add(new DatumIdentifier(null, null, DatumType.SIGINTReliability));
                    AttackProbabilityReportProbe pt = new AttackProbabilityReportProbe(
                            TrialPartProbeType.AttackProbabilityReport_Pt,
                            "Pt", datumList, probs);
                    pt.setName("P(Attack|SIGINT)");
                    pt.setTargetSum(100D);
                    pt.setNormalizationConstraint(NormalizationConstraintType.LessThanOrEqualTo);
                    trial.setAttackProbabilityProbe_Pt(pt);
                }

                //Create the attack probability probe after OSINT, IMINT, HUMINT, and SIGINT
                if (isMission123 || considerHumintSigintForMissions456) {
                    ArrayList<DatumIdentifier> datumList = new ArrayList<DatumIdentifier>(numLocations);
                    ArrayList<AttackProbability> probs = new ArrayList<AttackProbability>(numLocations);
                    index = 0;
                    for (String locationId : locationIds) {
                        if (trial.getAttackProbabilityProbe_Ppc() != null) {
                            datumList.add(new DatumIdentifier(locationId, "Ppc", DatumType.AttackProbabilityReport_Capability_Propensity));
                        }
                        if (isMission456) {
                            datumList.add(new DatumIdentifier(locationId, null, DatumType.IMINT));
                            datumList.add(new DatumIdentifier(locationId, null, DatumType.OSINT));
                            datumList.add(new DatumIdentifier(locationId, null, DatumType.HUMINT));
                        }
                        if (reportPt) {
                            datumList.add(new DatumIdentifier(locationId, "Pt", DatumType.AttackProbabilityReport_Activity));
                        } else {
                            datumList.add(new DatumIdentifier(locationId, null, DatumType.SIGINT));
                        }
                        if (isMission456) {
                            datumList.add(new DatumIdentifier(DatumType.BlueBook));
                        }
                        probs.add(new AttackProbability(locationId, index, RedActionType.Attack));
                        index++;
                    }
                    if (!reportPt) {
                        datumList.add(new DatumIdentifier(null, null, DatumType.SIGINTReliability));
                    }
                    AttackProbabilityReportProbe tpc = new AttackProbabilityReportProbe(
                            TrialPartProbeType.AttackProbabilityReport_Ptpc,
                            "Ptpc", datumList, probs);
                    tpc.setTargetSum(100D);
                    tpc.setNormalizationConstraint(NormalizationConstraintType.LessThanOrEqualTo);
                    tpc.setName("P(Attack|SIGINT, HUMINT, IMINT, OSINT)");
                    trial.setAttackProbabilityProbe_Ptpc(tpc);
                }

                //Create the blue action selection or presentation probe
                BlueActionSelectionProbe blueActionSelection = new BlueActionSelectionProbe();
                trial.setBlueActionSelection(blueActionSelection);
                blueActionSelection.setName("Blue Action Selection");
                boolean systemSelectsBlueAction = missionType == MissionType.Mission_1;
                blueActionSelection.setDataProvidedToParticipant(systemSelectsBlueAction);
                if (!systemSelectsBlueAction) {
                    List<BlueAction> blueActions = new ArrayList<BlueAction>();
                    //ArrayList<DatumIdentifier> datumList = new ArrayList<DatumIdentifier>(numLocations);
                    locationIndex = 0;
                    for (String locationId : locationIds) {
                        blueActions.add(new BlueAction(locationId, locationIndex, null));
                        /*datumList.add(new DatumIdentifier(locationId, null, DatumType.IMINT));
                         if(isMission456 && !considerHumintSigintForMissions456) {
                         datumList.add(new DatumIdentifier(locationId, "Pp", 
                         DatumType.AttackProbabilityReport_Propensity));
                         } else {
                         datumList.add(new DatumIdentifier(locationId, "Ptpc", 
                         DatumType.AttackProbabilityReport_Activity_Capability_Propensity));
                         }*/
                        locationIndex++;
                    }
                    blueActionSelection.setBlueActions(blueActions);
                    //blueActionSelection.setDatumList(datumList);
                } else {
                    blueActionSelection.setBlueActions(blueAgent.selectBlueActions(blueLocations,
                            redTactic.getTacticParameters(), trial.getHumint(),
                            sigintReliability, trial.getSigintPresentation()));
                }

                //Create the red action presentation
                trial.setRedActionSelection(new RedActionSelectionProbe(redAction));
            }
        }

        return trial;
    }

    protected List<Integer> createPreviousTrialsList(int trialNum) {
        List<Integer> trials = null;
        if (trialNum > 1) {
            trials = new LinkedList<Integer>();
            for (int i = trialNum - 1; i >= 1; i--) {
                trials.add(i);
            }
        }
        return trials;
    }

    /**
     * @param blueLocations
     * @param redAction
     * @param sigintReliability
     * @param generateOnlyIfSigintMissing
     */
    public void generateSigintAtLocations(List<BlueLocation> blueLocations, RedAction redAction,
            SigintReliability sigintReliability, boolean generateOnlyIfSigintMissing) {
        if (blueLocations != null && !blueLocations.isEmpty()) {
            if (sigintReliability == null) {
                sigintReliability = SigintReliability.createDefaultSigintReliability();
            }
            for (BlueLocation location : blueLocations) {
                if (!generateOnlyIfSigintMissing
                        || (location.getSigint() == null || location.getSigint().getRedAttackProbability_Pt() == null)) {
                    location.setSigint(new SigintDatum(generateSigint(
                            redAction != null && redAction.getAction() != null
                            && location.getId().equals(redAction.getLocationId())
                            ? redAction.getAction() : RedActionType.Do_Not_Attack, sigintReliability)));
                }
            }
        }
    }

    /**
     * Generate a SIGINT return (Chatter or Silence) given a Red action and the
     * SIGINT reliabilities.
     *
     * @param redAction
     * @param sigintReliability
     * @return
     */
    public SigintType generateSigint(RedActionType redAction, SigintReliability sigintReliability) {
        if (random == null) {
            random = new Random(1);
        }
        if (redAction == RedActionType.Attack) {
            //Red attacked
            if (sigintReliability.getChatterLikelihood_attack() >= random.nextDouble()) {
                //Report chatter at the location
                return SigintType.Chatter;
            } else {
                //Report silence at the location
                return SigintType.Silence;
            }
        } else {
            //Red did not attack
            if (sigintReliability.getChatterLikelihood_noAttack() >= random.nextDouble()) {
                //Report chatter at the location
                return SigintType.Chatter;
            } else {
                //Report silence at the location
                return SigintType.Silence;
            }
        }
    }

    /**
     * @param mission
     * @return
     */
    protected static int[] calculatePandUdistribution(Mission<?> mission) {
        int[] quadrantCounts = new int[4];
        if (mission != null && mission.getTestTrials() != null) {
            for (IcarusTestTrial_Phase2 trial : mission.getTestTrials()) {
                //System.out.println(trial.getBlueLocations());
                if (trial.getBlueLocations() != null) {
                    for (BlueLocation location : trial.getBlueLocations()) {
                        if (location.getOsint() != null && location.getOsint().getRedVulnerability_P() != null
                                && location.getImint() != null && location.getImint().getRedOpportunity_U() != null) {
                            RedTacticQuadrant quadrant = RedTacticParameters.getRedTacticQuadrant(
                                    location.getOsint().getRedVulnerability_P(),
                                    location.getImint().getRedOpportunity_U(),
                                    RedTacticParameters.DEFAULT_HIGH_P_THRESHOLD,
                                    RedTacticParameters.DEFAULT_LARGE_U_THRESHOLD);
                            if (quadrant != null) {
                                quadrantCounts[quadrant.ordinal()] += 1;
                            }
                        }
                    }
                }
            }
        }
        return quadrantCounts;
    }

    public static void main(String[] args) {
        //Generate Sample Exam 1, 2, or 3, or the Final Exam, or the developer's guide exam
        String examPrefix = "Final"; //"Sample";
        String examSuffix = "1";
        String examName = examPrefix + " Exam " + examSuffix;
        String examId = examPrefix + "-Exam-" + examSuffix;
        String examFolderName = examPrefix + "-Exam-" + examSuffix;
        List<Boolean> missionsToGenerate = null;
        boolean createTutorials = true;
        int batchPlotProbeInterval = 1;
        boolean batchPlotProbeOptional = true;
        //int maxNumBatchPlots = 3;
        int mission_4_maxNumBatchPlots = 3;
        int mission_5_maxNumBatchPlots = 4;
        int mission_4_switchTacticsTrial = 21;
        int mission_5_switchTacticsTrial = 9;
        if (examPrefix.equals("Sample")) {
            //Sample (Pilot and DG) exams
            if (examSuffix.equals("1")) {
                missionsToGenerate = Arrays.asList(true, true, true, false, false, false);
            } else if (examSuffix.equals("3")) {
                batchPlotProbeInterval = 10;
                batchPlotProbeOptional = false;
                missionsToGenerate = Arrays.asList(true, true, true, true, true, false);
            } else if (examSuffix.equals("DG")) {
                missionsToGenerate = Arrays.asList(true, true, true, true, true, false);
                createTutorials = false;
                batchPlotProbeOptional = false;
                mission_4_maxNumBatchPlots = 1;
                mission_5_maxNumBatchPlots = 1;
            } else {
                missionsToGenerate = Arrays.asList(true, true, true, true, true, false);
            }
        } else {
            //Final exam
            batchPlotProbeInterval = 10;
            batchPlotProbeOptional = false;
            mission_4_switchTacticsTrial = 12;
            mission_5_switchTacticsTrial = 25;
            missionsToGenerate = Arrays.asList(true, true, true, true, true, false);
        }
        try {
            boolean generateSigint = true;

            //Generate the exam
            ExamGenerator examGenerator = new ExamGenerator();
            File examFolder = new File("data/Phase_2_CPD/exams/" + examFolderName);
            URL examFolderUrl = examFolder.toURI().toURL();
            IcarusExam_Phase2 exam = examGenerator.generateExam(examName, examId, examFolderUrl,
                    createTutorials, generateSigint, missionsToGenerate,
                    batchPlotProbeInterval, batchPlotProbeOptional, mission_4_maxNumBatchPlots,
                    mission_5_maxNumBatchPlots, mission_4_switchTacticsTrial,
                    mission_5_switchTacticsTrial);
            IcarusExamLoader.writeFile(IcarusExamLoader_Phase2.marshalExam(exam),
                    new File(examFolder, examId + ".xml"));

            //Write Blue locations files with updated SIGINT
            if (generateSigint && exam != null && exam.getMissions() != null) {
                for (Mission<?> mission : exam.getMissions()) {
                    if (mission.getBlueLocations() != null && !mission.getBlueLocations().isEmpty()) {
                        String xml = FeatureVectorLoader.getInstance().marshalBlueLocations(
                                new FeatureContainer<BlueLocation>(mission.getBlueLocations()));
                        IcarusExamLoader.writeFile(xml, new File(examFolder,
                                mission.getBlueLocationsFile().getFileUrl()));
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
