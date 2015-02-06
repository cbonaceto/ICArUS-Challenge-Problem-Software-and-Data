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
package org.mitre.icarus.cps.exam.phase_2.testing;

import java.util.Arrays;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbeType;

/**
 * An enumeration of each Phase 2 mission type (Missions 1, 2, 3, 4, 5, and 6).
 * The mission type contains information about the structure and probes of the
 * mission.
 *
 *
 * @author CBONACETO
 *
 */
@XmlType(name = "MissionType", namespace = "IcarusCPD_2")
@XmlEnum
public enum MissionType {

    @XmlEnumValue("Mission_1")
    Mission_1(1, PlayerType.Blue, 1, true,
            Collections.unmodifiableSortedSet(new TreeSet<TrialPartProbeType>(
                            Arrays.asList(TrialPartProbeType.AttackProbabilityReport_Pp,
                                    TrialPartProbeType.AttackProbabilityReport_Ppc,
                                    TrialPartProbeType.AttackProbabilityReport_Pt,
                                    TrialPartProbeType.AttackProbabilityReport_Ptpc)))),
    @XmlEnumValue("Mission_2")
    Mission_2(2, PlayerType.Blue, 1, true,
            Collections.unmodifiableSortedSet(new TreeSet<TrialPartProbeType>(
                            Arrays.asList(TrialPartProbeType.MostLikelyRedTacticSelection,
                                    TrialPartProbeType.AttackProbabilityReport_Pp,
                                    TrialPartProbeType.AttackProbabilityReport_Ppc,
                                    TrialPartProbeType.AttackProbabilityReport_Ptpc,
                                    TrialPartProbeType.BlueActionSelection)))),
    @XmlEnumValue("Mission_3")
    Mission_3(3, PlayerType.Blue, 2, true,
            Collections.unmodifiableSortedSet(new TreeSet<TrialPartProbeType>(
                            Arrays.asList(TrialPartProbeType.AttackProbabilityReport_Pp,
                                    TrialPartProbeType.AttackProbabilityReport_Ppc,
                                    TrialPartProbeType.SigintSelection,
                                    TrialPartProbeType.AttackProbabilityReport_Ptpc,
                                    TrialPartProbeType.BlueActionSelection)))),
    @XmlEnumValue("Mission_4")
    Mission_4(4, PlayerType.Blue, 1, false,
            Collections.unmodifiableSortedSet(new TreeSet<TrialPartProbeType>(
                            Arrays.asList(TrialPartProbeType.RedTacticsProbabilityReport,
                                    TrialPartProbeType.BatchPlotProbe,
                                    TrialPartProbeType.AttackProbabilityReport_Pp,
                                    TrialPartProbeType.BlueActionSelection)))),
    @XmlEnumValue("Mission_5")
    Mission_5(5, PlayerType.Blue, 1, false,
            Collections.unmodifiableSortedSet(new TreeSet<TrialPartProbeType>(
                            Arrays.asList(TrialPartProbeType.RedTacticsProbabilityReport,
                                    TrialPartProbeType.BatchPlotProbe,
                                    TrialPartProbeType.AttackProbabilityReport_Pp,
                                    TrialPartProbeType.BlueActionSelection)))),
    @XmlEnumValue("Mission_6")
    Mission_6(6, PlayerType.Blue, 1, true,
            Collections.unmodifiableSortedSet(new TreeSet<TrialPartProbeType>(
                            Arrays.asList(TrialPartProbeType.RedTacticsChangesReport,
                                    TrialPartProbeType.BatchPlotProbe))));

    /**
     * The mission number
     */
    private final Integer missionNum;

    /**
     * Whether the participant plays as Red or Blue or an Observer (Mission 0
     * only)
     */
    private final PlayerType playerType;

    /**
     * The number of blue locations per trial in the mission
     */
    private final Integer locationsPerTrial;
    
    /** 
     * Whether Red's capability to attack is considered when computing the
     * Red probability of attack.
     */
    private final Boolean considerRedCapability_Pc;

    /**
     * The probe types in the mission
     */
    private final SortedSet<TrialPartProbeType> probes;

    /**
     * Construct a MissionType with the given settings.
     *
     * @param missionNum
     * @param playerType
     * @param locationsPerTrial
     * @param probes
     */
    private MissionType(Integer missionNum, PlayerType playerType,
            Integer locationsPerTrial, Boolean considerRedCapability_Pc, 
            SortedSet<TrialPartProbeType> probes) {
        this.missionNum = missionNum;
        this.playerType = playerType;        
        this.locationsPerTrial = locationsPerTrial;
        this.considerRedCapability_Pc = considerRedCapability_Pc;
        this.probes = probes;
        /*this.participantSelectsSigint = participantSelectsSigint;
         this.participantSelectsActions = participantSelectsActions;
         this.participantAssessesTactics = participantAssessesTactics;
         this.participantAbductsTactics = participantAbductsTactics;*/
    }

    /**
     * Get the mission number.
     *
     * @return the mission number
     */
    public Integer getMissionNum() {
        return missionNum;
    }

    /**
     * Get the type of player (Red or Blue) the player plays as.
     *
     * @return the type of player
     */
    public PlayerType getPlayerType() {
        return playerType;
    }

    /**
     * Get the number of locations for each trial.
     *
     * @return the number of locations for each trial.
     */
    public Integer getLocationsPerTrial() {
        return locationsPerTrial;
    }

    /**
     * Get whether Red's capability to attack is considered when computing the
     * Red probability of attack.
     * 
     * @return
     */
    public Boolean isConsiderRedCapability_Pc() {
        return considerRedCapability_Pc;
    }

    /**
     * Get the probe types in the mission.
     *
     * @return the probe types in the mission
     */
    public SortedSet<TrialPartProbeType> getProbes() {
        return probes;
    }
}
