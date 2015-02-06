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
package org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.trial_part_data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.mitre.icarus.cps.exam.phase_2.testing.MissionType;
import org.mitre.icarus.cps.exam.phase_2.testing.bluebook.RedTacticType;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.AttackProbability;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.AttackProbabilityReportProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.BatchPlotProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.BlueAction;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.BlueActionSelectionProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.RedAction.RedActionType;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbeType;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.int_presentation.SigintSelectionProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.red_tactics_probes.MostLikelyRedTacticProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.red_tactics_probes.RedTacticProbability;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.red_tactics_probes.RedTacticsProbabilityReportProbe;

/**
 * Factory class for creating TrialPartData instances.
 * 
 * @author CBONACETO
 */
public class TrialPartDataFactory {

    /**
     * @param type
     * @param missionType
     * @param trialNum
     * @return
     */
    public static TrialPartData<?> createTrialPartData(TrialPartProbeType type, 
            MissionType missionType, Integer trialNum) {        
        switch (type) {
            case MostLikelyRedTacticSelection:
                MostLikelyRedTacticProbe mlrtp = new MostLikelyRedTacticProbe();
                mlrtp.setRedTactics(getRedTacticsForMission(missionType));
                return new RedTacticsReportData(mlrtp);
            case AttackProbabilityReport_Pp:
                AttackProbabilityReportProbe pp = new AttackProbabilityReportProbe();
                pp.setType(TrialPartProbeType.AttackProbabilityReport_Pp);
                pp.setId("Pp");
                pp.setProbabilities(createAttackProbabilities(missionType, trialNum));
                return new AttackProbabilityReportData(pp);
            case AttackProbabilityReport_Ppc:
                AttackProbabilityReportProbe ppc = new AttackProbabilityReportProbe();
                ppc.setType(TrialPartProbeType.AttackProbabilityReport_Ppc);
                ppc.setId("Ppc");
                ppc.setProbabilities(createAttackProbabilities(missionType, trialNum));
                return new AttackProbabilityReportData(ppc);
            case AttackProbabilityReport_Pt:
                AttackProbabilityReportProbe pt = new AttackProbabilityReportProbe();
                pt.setType(TrialPartProbeType.AttackProbabilityReport_Pt);
                pt.setId("Pt");
                pt.setProbabilities(createAttackProbabilities(missionType, trialNum));
                return new AttackProbabilityReportData(pt);
            case AttackProbabilityReport_Ptpc:
                AttackProbabilityReportProbe ptpc = new AttackProbabilityReportProbe();
                ptpc.setType(TrialPartProbeType.AttackProbabilityReport_Ptpc);
                ptpc.setId("Ptpc");
                ptpc.setProbabilities(createAttackProbabilities(missionType, trialNum));
                return new AttackProbabilityReportData(ptpc);
            case BlueActionSelection:
                BlueActionSelectionProbe basp = new BlueActionSelectionProbe();
                List<BlueAction> blueActions = new ArrayList<BlueAction>(
                        missionType.getLocationsPerTrial());
                List<BlueAction> normativeParticipantBlueActions = new ArrayList<BlueAction>(
                        missionType.getLocationsPerTrial());
                List<BlueAction> normativeBayesianBlueActions = new ArrayList<BlueAction>(
                        missionType.getLocationsPerTrial());
                for (int i = 1; i <= missionType.getLocationsPerTrial(); i++) {
                    blueActions.add(new BlueAction(trialNum + "-" + i, i, null));
                    normativeParticipantBlueActions.add(new BlueAction(trialNum + "-" + i, i, null));
                    normativeBayesianBlueActions.add(new BlueAction(trialNum + "-" + i, i, null));
                }
                basp.setBlueActions(blueActions);
                basp.setNormativeParticipantBlueActions(normativeParticipantBlueActions);
                basp.setNormativeBayesianBlueActions(normativeBayesianBlueActions);
                return new BlueActionSelectionData(basp);
            case RedTacticsChangesReport:
                //TODO: Implement this
                return null;
            case RedTacticsProbabilityReport:
                RedTacticsProbabilityReportProbe rtpp = new RedTacticsProbabilityReportProbe(
                        null, null, Arrays.asList(new RedTacticProbability()));
                rtpp.setBatchPlotProbe(new BatchPlotProbe());
                SortedSet<RedTacticType> redTactics = getRedTacticsForMission(missionType);
                if (redTactics != null && !redTactics.isEmpty()) {
                    List<RedTacticProbability> probabilities = new ArrayList<RedTacticProbability>(
                            redTactics.size());
                    rtpp.setProbabilities(probabilities);
                    for (RedTacticType redTactic : redTactics) {
                        probabilities.add(new RedTacticProbability(redTactic));
                    }
                }
                return new RedTacticsReportData(rtpp);
            case SigintSelection:
                SigintSelectionProbe ssp = new SigintSelectionProbe();
                ssp.setSelectedLocationIds(Arrays.asList((String) null));
                return new SigintSelectionData(ssp);
            default:
                return null;
        }
    }

    /**
     * @param missionType
     * @param trialNum
     * @return
     */
    protected static List<AttackProbability> createAttackProbabilities(
            MissionType missionType, Integer trialNum) {
        List<AttackProbability> probabilities = new ArrayList<AttackProbability>(
                missionType.getLocationsPerTrial());
        for (int i = 0; i <= missionType.getLocationsPerTrial(); i++) {
            probabilities.add(new AttackProbability(trialNum + "-" + i, i,
                    RedActionType.Attack));
        }
        return probabilities;
    }

    /**
     * @param missionType
     * @return
     */
    protected static SortedSet<RedTacticType> getRedTacticsForMission(
            MissionType missionType) {
        switch (missionType) {
            case Mission_1:
                return new TreeSet<RedTacticType>(Arrays.asList(
                        RedTacticType.Mission_1));
            case Mission_2:
                return new TreeSet<RedTacticType>(Arrays.asList(
                        RedTacticType.Mission_2_Passive,
                        RedTacticType.Mission_2_Aggressive));
            case Mission_3:
                return new TreeSet<RedTacticType>(Arrays.asList(
                        RedTacticType.Mission_3));
            case Mission_4:
                return new TreeSet<RedTacticType>(Arrays.asList(
                        RedTacticType.Mission_4_Passive,
                        RedTacticType.Mission_4_Aggressive));
            case Mission_5:
                return new TreeSet<RedTacticType>(Arrays.asList(
                        RedTacticType.Mission_5_Psensitive,
                        RedTacticType.Mission_5_Usensitive));
            case Mission_6:
                return new TreeSet<RedTacticType>(Arrays.asList(
                        RedTacticType.Mission_6_1,
                        RedTacticType.Mission_6_2,
                        RedTacticType.Mission_6_3,
                        RedTacticType.Mission_6_4,
                        RedTacticType.Mission_6_5));
            default:
                return null;
        }
    }
}
