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
package org.mitre.icarus.cps.assessment.utils.phase_2;

import java.util.ArrayList;
import java.util.Iterator;
import org.mitre.icarus.cps.exam.phase_2.IcarusExam_Phase2;
import org.mitre.icarus.cps.exam.phase_2.testing.IcarusTestTrial_Phase2;
import org.mitre.icarus.cps.exam.phase_2.testing.Mission;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.AttackProbability;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.AttackProbabilityReportProbe;

/**
 * Contains static utility methods for Phase 2 assessment.
 * 
 * @author CBONACETO
 *
 */
public class AssessmentUtils_Phase2 {

    private AssessmentUtils_Phase2() {
    }

    /**
     * Gets the location index number from the given location ID.
     * 
     * @param locationId
     * @return
     */
    public static int parseLocationIndexAsInt(String locationId) throws NumberFormatException {
        if (locationId != null) {
            if (locationId.contains("-")) {
                String[] strings = locationId.split("-");
                if (strings != null && strings.length > 1) {
                    return Integer.parseInt(strings[1]);
                }
            } else {
                return Integer.parseInt(locationId);
            }
        }
        return -1;
    }

    /**
     * Gets the location index number as a string from the given location ID.
     * 
     * @param locationId
     * @return
     */
    public static String parseLocationIndexAsString(String locationId) {
        if (locationId != null && locationId.contains("-")) {
            String[] strings = locationId.split("-");
            if (strings != null && strings.length > 1) {
                return strings[1];
            }
        }
        return locationId;
    }

    /**
     * Add the subject responses in the given subjectMission to the
     * corresponding mission in the given exam.
     *
     * @param exam the exam
     * @param subjectMission the subject responses to a mission in the exam
     * @return the exam mission with the subject responses to the mission
     * @throws Exception
     */
    public static Mission<?> addSubjectResponsesToExamMission(IcarusExam_Phase2 exam,
            Mission<?> subjectMission) throws Exception {
        Mission<?> examMission = null;
        for (Mission<?> t : exam.getMissions()) {
            if ((t.getId() != null && t.getId().equals(subjectMission.getId()))
                    || (t.getMissionType() != null && t.getMissionType().equals(subjectMission.getMissionType()))
                    || (t.getName() != null && t.getName().equals(subjectMission.getName()))) {
                examMission = t;
            }
        }
        if (examMission != null) {
            examMission.setStartTime(subjectMission.getStartTime());
            examMission.setEndTime(subjectMission.getEndTime());
            examMission.setResponseGenerator(subjectMission.getResponseGenerator());
            ArrayList<? extends IcarusTestTrial_Phase2> subjectTrials = subjectMission.getTestTrials();
            ArrayList<? extends IcarusTestTrial_Phase2> examTrials = examMission.getTestTrials();
            int numTrials = Math.min(subjectTrials != null ? subjectTrials.size() : 0,
                    examTrials != null ? examTrials.size() : 0);
            for (int i = 0; i < numTrials; i++) {
                examTrials.get(i).copyResponseData(subjectTrials.get(i));
            }
        } else {
            throw new IllegalArgumentException("Error: subject task " + subjectMission.getName() + " not found in exam.");
        }
        return examMission;
    }

    /**
     * Convert participant Red attack probabilities from decimal format (if in
     * decimal format) to percent format. Returns true if probabilities needed
     * to be converted.
     *
     * @param examResponse
     */
    public static boolean convertDecimalProbsToPercentProbs(IcarusExam_Phase2 examResponse) {
        boolean decimalProbs = false;
        if (examResponse != null && examResponse.getMissions() != null
                && !examResponse.getMissions().isEmpty()) {
            Iterator<Mission<?>> missionIter = examResponse.getMissions().iterator();
            while (!decimalProbs && missionIter.hasNext()) {
                Mission<?> mission = missionIter.next();
                if (mission.getTestTrials() != null && !mission.getTestTrials().isEmpty()) {
                    Iterator<? extends IcarusTestTrial_Phase2> trialIter = mission.getTestTrials().iterator();
                    while (!decimalProbs && trialIter.hasNext()) {
                        IcarusTestTrial_Phase2 trial = trialIter.next();
                        if (checkDecimalProbs(trial.getAttackPropensityProbe_Pp())) {
                            decimalProbs = true;
                        } else if (checkDecimalProbs(trial.getAttackProbabilityProbe_Ppc())) {
                            decimalProbs = true;
                        } else if (checkDecimalProbs(trial.getAttackProbabilityProbe_Pt())) {
                            decimalProbs = true;
                        } else if (checkDecimalProbs(trial.getAttackProbabilityProbe_Ptpc())) {
                            decimalProbs = true;
                        }
                    }
                }
            }
            if (decimalProbs) {
                //Convert all probabilities to percent probabilities
                for (Mission<?> mission : examResponse.getMissions()) {
                    if (mission.getTestTrials() != null && !mission.getTestTrials().isEmpty()) {
                        for (IcarusTestTrial_Phase2 trial : mission.getTestTrials()) {
                            convertDecimalProbsToPercentProbs(trial.getAttackPropensityProbe_Pp());
                            convertDecimalProbsToPercentProbs(trial.getAttackProbabilityProbe_Ppc());
                            convertDecimalProbsToPercentProbs(trial.getAttackProbabilityProbe_Pt());
                            convertDecimalProbsToPercentProbs(trial.getAttackProbabilityProbe_Ptpc());
                        }
                    }
                }
            }
        }
        return decimalProbs;
    }

    /**
     * Check whether the probabilities are in decimal format (and not percent format).
     * 
     * @param probReport
     * @return
     */
    public static boolean checkDecimalProbs(AttackProbabilityReportProbe probReport) {
        if (probReport != null && probReport.getProbabilities() != null
                && !probReport.getProbabilities().isEmpty()) {
            for (AttackProbability prob : probReport.getProbabilities()) {
                if (prob != null && prob.getProbability() != null
                        && prob.getProbability() < 1.d
                        && prob.getProbability() > 0.d) {
                    //Found a decimal probability
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Convert probabilities in the given probability report from decimal to
     * percent probabilities.
     *
     * @param probReport
     */
    public static void convertDecimalProbsToPercentProbs(AttackProbabilityReportProbe probReport) {
        if (probReport != null && probReport.getProbabilities() != null
                && !probReport.getProbabilities().isEmpty()) {
            for (AttackProbability prob : probReport.getProbabilities()) {
                if (prob != null && prob.getProbability() != null) {
                    prob.setProbability(prob.getProbability() * 100.d);
                }
            }
        }
    }
}
