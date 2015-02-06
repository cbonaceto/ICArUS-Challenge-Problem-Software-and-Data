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
package org.mitre.icarus.cps.assessment.score_computer.phase_2;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.xml.bind.JAXBException;
import org.mitre.icarus.cps.app.util.CPSFileUtils;
import org.mitre.icarus.cps.app.util.ProbabilityUtils;

import org.mitre.icarus.cps.app.widgets.progress_monitor.IProgressMonitor;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.SubjectSigintProbabilities;
import org.mitre.icarus.cps.assessment.score_computer.ScoreComputerBase;
import org.mitre.icarus.cps.assessment.score_computer.phase_1.ErrorString;
import org.mitre.icarus.cps.exam.phase_2.IcarusExam_Phase2;
import org.mitre.icarus.cps.exam.phase_2.feedback.TrialFeedback_Phase2;
import org.mitre.icarus.cps.exam.phase_2.loader.IcarusExamLoader_Phase2;
import org.mitre.icarus.cps.exam.phase_2.testing.DatumType;
import org.mitre.icarus.cps.exam.phase_2.testing.IcarusTestTrial_Phase2;
import org.mitre.icarus.cps.exam.phase_2.testing.Mission;
import org.mitre.icarus.cps.exam.phase_2.testing.Mission_1_2_3;
import org.mitre.icarus.cps.exam.phase_2.testing.Mission_1_2_3_Trial;
import org.mitre.icarus.cps.exam.phase_2.testing.Mission_4_5_6;
import org.mitre.icarus.cps.exam.phase_2.testing.Mission_4_5_6_Trial;
import org.mitre.icarus.cps.exam.phase_2.testing.PlayerType;
import org.mitre.icarus.cps.exam.phase_2.testing.ShowdownWinner;
import org.mitre.icarus.cps.exam.phase_2.testing.bluebook.BlueBook;
import org.mitre.icarus.cps.exam.phase_2.testing.bluebook.PayoffMatrix;
import org.mitre.icarus.cps.exam.phase_2.testing.bluebook.RedTactic;
import org.mitre.icarus.cps.exam.phase_2.testing.bluebook.RedTacticParameters;
import org.mitre.icarus.cps.exam.phase_2.testing.bluebook.RedTacticType;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.AttackProbability;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.AttackProbabilityReportProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.BatchPlotProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.BlueAction;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.RedAction;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.BlueAction.BlueActionType;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.BlueActionSelectionProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.RedAction.RedActionType;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbeType;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.int_presentation.SigintPresentationProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.int_presentation.SigintSelectionProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.red_tactics_probes.AbstractRedTacticsProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.red_tactics_probes.MostLikelyRedTacticProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.red_tactics_probes.RedTacticProbability;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.red_tactics_probes.RedTacticsProbabilityReportProbe;
import org.mitre.icarus.cps.examples.phase_2.ParsingExample;
import org.mitre.icarus.cps.feature_vector.phase_2.BlueLocation;
import org.mitre.icarus.cps.feature_vector.phase_2.int_datum.HumintDatum;
import org.mitre.icarus.cps.feature_vector.phase_2.int_datum.ImintDatum;
import org.mitre.icarus.cps.feature_vector.phase_2.int_datum.OsintDatum;
import org.mitre.icarus.cps.feature_vector.phase_2.int_datum.SigintReliability;

/**
 * The Phase 2 score computer. Used to compute scores (points) and normative
 * solutions for the Phase 2 Challenge Problem.
 *
 * @author CBONACETO
 *
 */
public class ScoreComputer_Phase2 extends ScoreComputerBase {

    /** Whether to display debug messages */
    protected boolean debug = true;

    /** Random number generator */
    protected static final Random random = new Random(1);
    
    /** Default value for epsilon (in decimal/probability format, percent is 1%) */
    public static final double EPSILON_PHASE2 = 0.01d;

    /**
     * Construct a new score computer.
     */
    public ScoreComputer_Phase2() {
    }

    /**
     * Get whether in debug mode. If true, output will be displayed in the console.
     * 
     * @return whether in debug mode
     */
    public boolean isDebug() {
        return debug;
    }

    /**
     * Set whether in debug mode. If true, output will be displayed in the console.
     * 
     * @param debug whether in debug mode
     */
    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    /**
     * Computes the Red and Blue points awarded for each trial in the mission.
     * Returns a list of trial feedback objects for each trial containing the
     * Red and Blue points awarded and other feedback information.
     *
     * @param mission the mission
     * @param payoffMatrix The payoff matrix to use. If null, a default payoff
     * matrix will be created.
     * @param checkForMissingResponseData whether to check if response data is missing and generate warnings if it is
     * @param progressMonitor an optional progress monitor to track progress
     * @return list of feedback objects for each trial in the mission
     */
    public List<TrialFeedback_Phase2> computeScoreForMission(Mission<?> mission, PayoffMatrix payoffMatrix,
            boolean checkForMissingResponseData, IProgressMonitor progressMonitor) {
        //Compute the score for each trial in the mission		
        if (mission.getTestTrials() == null || mission.getTestTrials().isEmpty()) {
            return null;
        }

        List<TrialFeedback_Phase2> trialFeedback = new ArrayList<TrialFeedback_Phase2>(mission.getTestTrials().size());

        //Create default payoff matrix instance if null
        if (payoffMatrix == null) {
            payoffMatrix = PayoffMatrix.createDefaultPayoffMatrix();
        }

        int trialNum = 1;
        if (progressMonitor != null) {
            progressMonitor.setMinimum(1);
            progressMonitor.setMaximum(mission.getTestTrials().size());
            progressMonitor.setProgress(0);
        }
        Double currentRedScore = 0D; //Current Red score
        Double currentBlueScore = 0D; //Current Blue score
        Integer batchPlotsCreated = 0; //Current number of batch plots created
        Integer maxNumBatchPlots = mission instanceof Mission_4_5_6 ? ((Mission_4_5_6) mission).getMaxNumBatchPlots() : null;
        for (IcarusTestTrial_Phase2 trial : mission.getTestTrials()) {
            if (progressMonitor != null) {
                progressMonitor.setNote("Scoring trial " + trialNum);
            }
            TrialFeedback_Phase2 feedbackForTrial = computeScoreForTrial(trial,
                    currentRedScore, currentBlueScore, payoffMatrix, batchPlotsCreated,
                    maxNumBatchPlots, checkForMissingResponseData);
            trialFeedback.add(feedbackForTrial);
            if (feedbackForTrial != null) {
                if (feedbackForTrial.getBluePointsGained() != null) {
                    currentBlueScore += feedbackForTrial.getBluePointsGained();
                }
                if (feedbackForTrial.getRedPointsGained() != null) {
                    currentRedScore += feedbackForTrial.getRedPointsGained();
                }
                if (feedbackForTrial.isBatchPlotCreated() != null
                        && feedbackForTrial.isBatchPlotCreated()) {
                    batchPlotsCreated++;
                }
                feedbackForTrial.setResponseWellFormed(feedbackForTrial.getErrors() == null || feedbackForTrial.getErrors().isEmpty());
                trial.setResponseFeedBack(feedbackForTrial);
            }
            if (progressMonitor != null) {
                progressMonitor.setProgress(trialNum);
            }
            trialNum++;
        }
        mission.setBlueScore(currentBlueScore);
        mission.setRedScore(currentRedScore);
        return trialFeedback;
    }

    /**
     * Computes the Red and Blue points awarded for the trial. Returns a trial
     * feedback object for containing the Red and Blue points awarded and other
     * feedback information.
     *
     * @param trial the trial
     * @param currentRedScore the current Red score
     * @param currentBlueScore the current Blue score
     * @param payoffMatrix The payoff matrix to use. If null, a default payoff
     * matrix will be created.
     * @param numBatchPlotsUsed the number of batch plots created on previous
     * trials in the mission (Missions 4-5 only)
     * @param maxNumBatchPlots The maximum number of batch plots that may be
     * created in the mission. A null value indicates no limit.
     * @param checkForMissingResponseData whether to check if response data is missing and generate warnings if it is 
     * @return feedback for the trial
     */
    //TODO: Warn if probabilities reported in decimal and not percent format
    public TrialFeedback_Phase2 computeScoreForTrial(IcarusTestTrial_Phase2 trial, 
            Double currentRedScore, Double currentBlueScore, PayoffMatrix payoffMatrix,
            Integer numBatchPlotsUsed, Integer maxNumBatchPlots,
            boolean checkForMissingResponseData) {
        TrialFeedback_Phase2 feedback = new TrialFeedback_Phase2();
        ErrorString errors = new ErrorString();
        if (trial == null) {
            errors.append("Trial missing");
            feedback.setErrors(errors.toString());
            feedback.setResponseWellFormed(false);
            return feedback;
        }
        feedback.setTrialNum(trial.getTrialNum());

        //Check for missing response data
        if (checkForMissingResponseData) {
            if (trial instanceof Mission_1_2_3_Trial) {
                MostLikelyRedTacticProbe redTacticsProbe = ((Mission_1_2_3_Trial) trial).getMostLikelyRedTacticProbe();
                if (redTacticsProbe != null
                        && (redTacticsProbe.isDataProvidedToParticipant() == null || !redTacticsProbe.isDataProvidedToParticipant())
                        && !redTacticsProbe.isResponsePresent()) {
                    errors.append("Warning, missing response for the " + redTacticsProbe.getName() + " probe");
                }
            } else if (trial instanceof Mission_4_5_6_Trial) {
                AbstractRedTacticsProbe redTacticsProbe = ((Mission_4_5_6_Trial) trial).getRedTacticsProbe();
                if (redTacticsProbe != null
                        && (redTacticsProbe.isDataProvidedToParticipant() == null || !redTacticsProbe.isDataProvidedToParticipant())
                        && !redTacticsProbe.isResponsePresent()) {
                    errors.append("Warning, missing response for the " + redTacticsProbe.getName() + " probe");
                }
            }
            if (trial.getAttackPropensityProbe_Pp() != null
                    && (trial.getAttackPropensityProbe_Pp().isDataProvidedToParticipant() == null
                    || !trial.getAttackPropensityProbe_Pp().isDataProvidedToParticipant())
                    && !trial.getAttackPropensityProbe_Pp().isResponsePresent()) {
                errors.append("Warning, missing response for the " + trial.getAttackPropensityProbe_Pp().getName() + " probe");
            }
            if (trial.getAttackProbabilityProbe_Ppc() != null
                    && (trial.getAttackProbabilityProbe_Ppc().isDataProvidedToParticipant() == null
                    || !trial.getAttackProbabilityProbe_Ppc().isDataProvidedToParticipant())
                    && !trial.getAttackProbabilityProbe_Ppc().isResponsePresent()) {
                errors.append("Warning, missing response for the " + trial.getAttackProbabilityProbe_Ppc().getName() + " probe");
            }
            if (trial.getSigintSelectionProbe() != null
                    && (trial.getSigintSelectionProbe().isDataProvidedToParticipant() == null
                    || !trial.getSigintSelectionProbe().isDataProvidedToParticipant())
                    && !trial.getSigintSelectionProbe().isResponsePresent()) {
                errors.append("Warning, missing response for the " + trial.getSigintSelectionProbe().getName() + " probe");
            }
            if (trial.getAttackProbabilityProbe_Pt() != null
                    && (trial.getAttackProbabilityProbe_Pt().isDataProvidedToParticipant() == null
                    || !trial.getAttackProbabilityProbe_Pt().isDataProvidedToParticipant())
                    && !trial.getAttackProbabilityProbe_Pt().isResponsePresent()) {
                errors.append("Warning, missing response for the " + trial.getAttackProbabilityProbe_Pt().getName() + " probe");
            }
            if (trial.getAttackProbabilityProbe_Ptpc() != null
                    && (trial.getAttackProbabilityProbe_Ptpc().isDataProvidedToParticipant() == null
                    || !trial.getAttackProbabilityProbe_Ptpc().isDataProvidedToParticipant())
                    && !trial.getAttackProbabilityProbe_Ptpc().isResponsePresent()) {
                errors.append("Warning, missing response for the " + trial.getAttackProbabilityProbe_Ptpc().getName() + " probe");
            }
        }

        //Create a default payoff matrix if the payoff matrix is null
        if (payoffMatrix == null) {
            payoffMatrix = PayoffMatrix.createDefaultPayoffMatrix();
        }

        //Get the Blue action(s)
        boolean blueActionsValid = true;
        List<BlueAction> blueActions = trial.getBlueActionSelection() != null ? trial.getBlueActionSelection().getBlueActions() : null;
        feedback.setBlueActions(blueActions);
        if (blueActions != null && !blueActions.isEmpty()) {
            for (BlueAction action : blueActions) {
                if (action == null || action.getLocationId() == null || action.getAction() == null) {
                    errors.append("Missing Blue action for location " + 
                            (action != null && action.getLocationId() != null ? action.getLocationId() : null));
                    blueActionsValid = false;
                    break;
                }
            }
        } else {
            errors.append("Missing Blue action(s)");
            blueActionsValid = false;
        }

        //Update the Blue and Red scores
        if (blueActionsValid) {
            RedAction redAction = trial.getRedActionSelection() != null ? trial.getRedActionSelection().getRedAction() : null;
            feedback.setRedAction(redAction);
            ShowdownWinner showdownWinner = null;
            if (redAction == null || redAction.getLocationId() == null || redAction.getAction() == null) {
                errors.append("Missing Red action");
            } else {
                Double redPoints = 0D;
                Double bluePoints = 0D;
                for (BlueAction blueAction : blueActions) {
                    OsintDatum osint = trial.getOsint(blueAction.getLocationId());
                    ImintDatum imint = trial.getImint(blueAction.getLocationId());
                    if (osint == null || osint.getRedVulnerability_P() == null
                            || imint == null || imint.getRedOpportunity_U() == null) {
                        errors.append("Missint INT datum at location " + blueAction.getLocationId());
                    } else {
                        //Get the showdown winner at the location, or randomly select the showdown winner
                        ShowdownWinner showdownWinnerAtLocation = null;
                        if (trial.getShowdownWinner() != null && !trial.getShowdownWinner().isEmpty()) {
                            for (ShowdownWinner winner : trial.getShowdownWinner()) {
                                if (blueAction.getLocationId().equals(winner.getLocationId())) {
                                    //playerWhoWillWinShowdown = winner.getShowdownWinner();
                                    showdownWinnerAtLocation = winner;
                                    break;
                                }
                            }
                        }
                        PlayerType playerWhoWillWinShowdown = showdownWinnerAtLocation == null
                                || showdownWinnerAtLocation.getShowdownWinner() == null
                                ? PayoffMatrix.determineShowdownWinner(osint.getRedVulnerability_P(), random)
                                : showdownWinnerAtLocation.getShowdownWinner();
                        RedBluePayoff payoff;
                        if (blueAction.getLocationId().equals(redAction.getLocationId())) {
                            payoff = computePayoff(blueAction.getAction(), redAction.getAction(),
                                    playerWhoWillWinShowdown, imint.getRedOpportunity_U(), payoffMatrix);
                        } else {
                            payoff = computePayoff(blueAction.getAction(), RedActionType.Do_Not_Attack,
                                    playerWhoWillWinShowdown, imint.getRedOpportunity_U(), payoffMatrix);
                        }
                        if (payoff.isShowDown()) {
                            showdownWinner = showdownWinnerAtLocation;
                        }
                        redPoints += payoff.redPoints;
                        bluePoints += payoff.bluePoints;
                    }
                }
                feedback.setShowdownWinner(showdownWinner);
                feedback.setRedPointsGained(redPoints);
                feedback.setBluePointsGained(bluePoints);
                feedback.setRedScore(currentRedScore != null ? redPoints + currentRedScore : redPoints);
                feedback.setBlueScore(currentBlueScore != null ? bluePoints + currentBlueScore : bluePoints);
            }
        }

        //Update the number of batch plots remaining
        if (trial instanceof Mission_4_5_6_Trial) {
            Mission_4_5_6_Trial mission456Trial = (Mission_4_5_6_Trial) trial;
            if (mission456Trial.getRedTacticsProbe() != null
                    && mission456Trial.getRedTacticsProbe().getBatchPlotProbe() != null) {
                BatchPlotProbe batchPlotProbe = mission456Trial.getRedTacticsProbe().getBatchPlotProbe();
                if (batchPlotProbe.getNumPreviousTrialsSelected() != null
                        && batchPlotProbe.getNumPreviousTrialsSelected() > 0) {
                    feedback.setBatchPlotCreated(true);
                    feedback.setBatchPlotsRemaining(maxNumBatchPlots - numBatchPlotsUsed - 1);
                    if (feedback.getBatchPlotsRemaining() < 0) {
                        errors.append("A batch plot was created, but the number of batch plots exceeds"
                                + " the maximum allowable number of batch plots");
                        feedback.setBatchPlotsRemaining(0);
                    }
                } else {
                    feedback.setBatchPlotCreated(false);
                }
            }
        }
        
        //Create an error an incorrect number of locations were selected to obtain SIGINT at
        if (trial.getSigintSelectionProbe() != null
                && (trial.getSigintSelectionProbe().isDataProvidedToParticipant() == null
                || !trial.getSigintSelectionProbe().isDataProvidedToParticipant())
                && trial.getSigintSelectionProbe().isResponsePresent()) {
            int numLocations = trial.getSigintSelectionProbe().getSelectedLocationIds() != null
                    ? trial.getSigintSelectionProbe().getSelectedLocationIds().size() : 0;
            if (trial.getSigintSelectionProbe().getNumSigintSelections() != null
                    && numLocations != trial.getSigintSelectionProbe().getNumSigintSelections()) {
                errors.append("SIGINT was selected at " + numLocations + "locations, but "
                        + "should have been selected at " + trial.getSigintSelectionProbe().getNumSigintSelections()
                        + " locations.");
            }
        }

        if (!errors.isEmpty()) {
            feedback.setErrors(errors.toString());
            feedback.setResponseWellFormed(false);
        } else {
            feedback.setResponseWellFormed(true);
        }
        return feedback;
    }

    /**
     * Compute the points awarded to Red and Blue given the Red action selected,
     * Blue action selected, show-down winner, "U" value, and payoff matrix.
     *
     * @param blueAction the Blue action selected at the location
     * @param redAction the Red action selected at the location
     * @param showdownWinner the show-down winner (if there is a show-down)
     * @param redOpportunity_U the "U" value at the location
     * @param payoffMatrix The payoff matrix to use. If null, a default payoff
     * matrix will be created.
     * @return a RedBluePayoff object containing the points awarded to Red and Blue at the location
     */
    public static RedBluePayoff computePayoff(BlueActionType blueAction, RedActionType redAction,
            PlayerType showdownWinner, Integer redOpportunity_U, PayoffMatrix payoffMatrix) {
        if (payoffMatrix == null) {
            payoffMatrix = PayoffMatrix.createDefaultPayoffMatrix();
        }
        return payoffMatrix.computePayoff(blueAction, redAction, showdownWinner, redOpportunity_U);
    }

    /**
     * Computes the points awarded to Red and Blue at each location in the given
     * trial.
     *
     * @param trial the trial, which contain the locations, the Blue actions at each location,
     * and the Red actions at each location
     * @param currentRedScore the current Red score
     * @param currentBlueScore the current Blue score
     * @param payoffMatrix The payoff matrix to use. If null, a default payoff
     * matrix will be created.
     * @return a RedBluePayoffAtEachLocation object containing the points awarded to Red and Blue at each location and
     * the overall Red and Blue scores
     */
    public static RedBluePayoffAtEachLocation computePayoffAtEachLocation(IcarusTestTrial_Phase2 trial,
            Double currentRedScore, Double currentBlueScore, PayoffMatrix payoffMatrix) {
        if (payoffMatrix == null) {
            payoffMatrix = PayoffMatrix.createDefaultPayoffMatrix();
        }
        List<RedBluePayoff> payoffAtLocations = null;
        ShowdownWinner showdownWinner = null;
        Double redPoints = 0D;
        Double bluePoints = 0D;
        if (trial != null) {
            List<BlueAction> blueActions = trial.getBlueActionSelection() != null ? trial.getBlueActionSelection().getBlueActions() : null;
            RedAction redAction = trial.getRedActionSelection() != null ? trial.getRedActionSelection().getRedAction() : null;
            if (blueActions != null && !blueActions.isEmpty() && redAction != null) {
                payoffAtLocations = new ArrayList<RedBluePayoff>(blueActions.size());
                for (BlueAction blueAction : blueActions) {
                    OsintDatum osint = trial.getOsint(blueAction.getLocationId());
                    ImintDatum imint = trial.getImint(blueAction.getLocationId());
                    if (osint == null || osint.getRedVulnerability_P() == null
                            || imint == null || imint.getRedOpportunity_U() == null) {
                        payoffAtLocations.add(new RedBluePayoff(0D, 0D, false, null));
                    } else {                        
                        //Get the showdown winner at the location, or randomly select the showdown winner
                        ShowdownWinner showdownWinnerAtLocation = null;
                        if (trial.getShowdownWinner() != null && !trial.getShowdownWinner().isEmpty()) {
                            for (ShowdownWinner winner : trial.getShowdownWinner()) {
                                if (blueAction.getLocationId().equals(winner.getLocationId())) {
                                    //playerWhoWillWinShowdown = winner.getShowdownWinner();
                                    showdownWinnerAtLocation = winner;
                                    break;
                                }
                            }
                        }
                        PlayerType playerWhoWillWinShowdown = showdownWinnerAtLocation == null
                                || showdownWinnerAtLocation.getShowdownWinner() == null
                                ? PayoffMatrix.determineShowdownWinner(osint.getRedVulnerability_P(), random)
                                : showdownWinnerAtLocation.getShowdownWinner();
                        RedBluePayoff payoff;
                        if (blueAction.getLocationId().equals(redAction.getLocationId())) {
                            payoff = computePayoff(blueAction.getAction(), redAction.getAction(),
                                    playerWhoWillWinShowdown, imint.getRedOpportunity_U(), payoffMatrix);
                        } else {
                            payoff = computePayoff(blueAction.getAction(), RedActionType.Do_Not_Attack,
                                    playerWhoWillWinShowdown, imint.getRedOpportunity_U(), payoffMatrix);
                        }
                        payoffAtLocations.add(payoff);
                        if (payoff.isShowDown()) {
                            showdownWinner = showdownWinnerAtLocation;
                        }
                        redPoints += payoff.redPoints;
                        bluePoints += payoff.bluePoints;
                    }
                }
            }
        }
        return new RedBluePayoffAtEachLocation(payoffAtLocations, showdownWinner, redPoints, bluePoints,
                currentRedScore != null ? redPoints + currentRedScore : redPoints,
                currentBlueScore != null ? bluePoints + currentBlueScore : bluePoints);
    }

    /**
     * Compute normative responses for the given trial (cumulative and incremental 
     * Bayesian Red attack probabilities, SIGINT selections based on participant probs 
     * and cumulative Bayesian probs, Blue action selections based on participant probs 
     * and cumulative Bayesian probs).
     *
     * @param mission the mission, which contains previous trials that may be used to compute
     * "quasi-Bayesian" Red attack probabilities for Missions where the Red tactic is not known
     * to the participant.
     * @param trial the trial in the mission to compute normative responses for
     * @param sigintReliability the SIGINT reliabilities. If null, the default
     * SIGINT reliabilities will be used.
     * @param blueBook the BLUEBOOK. If null, the default BLUEBOOK will be used
     * @param computeRedTacticProbs whether to compute the probability that Red is playing with each tactic and use
     * these probabilities when computing the Bayesian attack probabilities. If false, uses the actual (ground truth)
     * tactic Red is playing with to compute the Bayesian attack probabilities). Only relevant for missions where the
     * Red tactic is not known to the participant.
     * @param computeNormativeSigintSelections whether to compute the normative SIGINT selection, which is returned
     * in the SIGINT selection probe in the trial
     * @param computeNormativeBlueActionSelections whether to compute the normative Blue action selections
     * at each location, which is returned in the Blue action selection probe in the trial
     * @param progressMonitor an optional progress monitor to track progress
     * @return the Bayesian (cumulative and incremental) Red attack probabilities
     */
    public CumulativeAndIncrementalRedAttackProbabilities computeNormativeDataForTrial(
            Mission<?> mission, IcarusTestTrial_Phase2 trial,
            SigintReliability sigintReliability, BlueBook blueBook,
            boolean computeRedTacticProbs, boolean computeNormativeSigintSelections,
            boolean computeNormativeBlueActionSelections, IProgressMonitor progressMonitor) {
        return computeNormativeDataForTrial(mission, trial, sigintReliability, null, 
                blueBook, computeRedTacticProbs, computeNormativeSigintSelections, 
                computeNormativeBlueActionSelections, progressMonitor);
    }
    
    /**
     * Compute normative responses for the given trial (cumulative and incremental 
     * Bayesian Red attack probabilities, SIGINT selections based on participant probs 
     * and cumulative Bayesian probs, Blue action selections based on participant probs 
     * and cumulative Bayesian probs).
     * 
     * @param mission the mission, which contains previous trials that may be used to compute
     * "quasi-Bayesian" Red attack probabilities for Missions where the Red tactic is not known
     * to the participant.
     * @param trial the trial in the mission to compute normative responses for
     * @param sigintReliability the SIGINT reliabilities. If null, the default
     * SIGINT reliabilities will be used.
     * @param subjectSigintProbs the values to use for P(Attack|SIGINT) when computing
     * the incremental Bayesian attack probabilities for P(Attack|SIGINT, Capability, Propensity)
     * and the subject was not probed for P(Attack|SIGINT) on a trial in the mission
     * @param blueBook the BLUEBOOK. If null, the default BLUEBOOK will be used
     * @param computeRedTacticProbs whether to compute the probability that Red is playing with each tactic and use
     * these probabilities when computing the Bayesian attack probabilities. If false, uses the actual (ground truth)
     * tactic Red is playing with to compute the Bayesian attack probabilities). Only relevant for missions where the
     * Red tactic is not known to the participant.
     * @param computeNormativeSigintSelections whether to compute the normative SIGINT selection, which is returned
     * in the SIGINT selection probe in the trial
     * @param computeNormativeBlueActionSelections whether to compute the normative Blue action selections
     * at each location, which is returned in the Blue action selection probe in the trial
     * @param progressMonitor an optional progress monitor to track progress
     * @return the Bayesian (cumulative and incremental) Red attack probabilities
     */
    public CumulativeAndIncrementalRedAttackProbabilities
            computeNormativeDataForTrial(Mission<?> mission, IcarusTestTrial_Phase2 trial,
                    SigintReliability sigintReliability, SubjectSigintProbabilities subjectSigintProbs,
                    BlueBook blueBook, boolean computeRedTacticProbs, boolean computeNormativeSigintSelections,
                    boolean computeNormativeBlueActionSelections, IProgressMonitor progressMonitor) {
        if (blueBook == null) {
            blueBook = BlueBook.createDefaultBlueBook();
        }
        List<RedTacticParameters> redTactics = null;
        List<Double> redTacticProbs = null;
        if (computeRedTacticProbs) {
            List<RedTactic> possibleRedTactics = getPossibleRedTactics(mission, blueBook);
            if (possibleRedTactics != null && !possibleRedTactics.isEmpty()) {
                redTactics = new ArrayList<RedTacticParameters>(possibleRedTactics.size());
                List<RedTacticType> redTacticTypes = new ArrayList<RedTacticType>(possibleRedTactics.size());
                for (RedTactic tactic : possibleRedTactics) {
                    redTactics.add(tactic.getTacticParameters());
                    redTacticTypes.add(tactic.getTacticType());
                }
                redTacticProbs = computeNormativeRedTacticProbabilitiesForTrial(
                        mission, redTacticTypes, trial);
            }
        } else {
            RedTacticType redTactic = getActualRedTacticType(trial, mission);
            if (redTactic != null) {
                redTactics = Arrays.asList(redTactic.getTacticParameters());
                //redTactics = Arrays.asList(redTactic);
                redTacticProbs = Arrays.asList(1.d);
            }
        }
        if (redTactics != null) {
            CumulativeAndIncrementalRedAttackProbabilities attackProbs
                    = computeNormativeRedAttackProbabilitiesForTrial(
                            trial, redTactics, redTacticProbs, sigintReliability,
                            subjectSigintProbs);
            if (attackProbs != null) {
                if (computeNormativeSigintSelections && trial.getSigintSelectionProbe() != null) {
                    computeNormativeSigintSelectionsForTrial(trial, attackProbs.cumulativeProbs);
                }
                if (computeNormativeBlueActionSelections) {
                    computeNormativeBlueActionsForTrial(trial, attackProbs.cumulativeProbs);
                }
            }
            return attackProbs;
        } else {
            return null;
        }
    }

    /**
     * Get the tactic Red is actually playing with (ground truth) on the given trial in the
     * given mission.
     *
     * @param trial the trial, which contains the actual (ground truth) tactic for Missions 4 and 5
     * @param mission the mission, which contains the actual (ground truth) tactic for Mission 2
     * @return the tactic Red is actually playing with (ground truth) on the given trial in the
     * given mission
     */
    public static RedTacticType getActualRedTacticType(IcarusTestTrial_Phase2 trial, Mission<?> mission) {
        if (mission instanceof Mission_1_2_3) {
            return ((Mission_1_2_3) mission).getRedTactic();
        } else if (trial instanceof Mission_4_5_6_Trial) {
            return ((Mission_4_5_6_Trial) trial).getRedTactic();
        }
        return null;
    }

    /**
     * Get the tactics Red may be playing with on the given mission from the
     * BLUEBOOK.
     *
     * @param mission the mission, which determines which tactics Red may be playing with
     * @param blueBook the BLUEBOOK
     * @return the tactics Red may be playing with
     */
    public static List<RedTactic> getPossibleRedTactics(Mission<?> mission, BlueBook blueBook) {
        if (mission.getMissionType() != null) {
            return blueBook.getRedTaticsForMission(mission.getMissionType());
        }
        return null;
    }

    /**
     * Computes the normative (cumulative Bayesian) Red attack probabilities for each trial in the
     * mission. Probabilities are returned in decimal and not percent format. Also populates
     * the normative probabilities in each AttackProbabilityReportProbe present in each trial.
     *
     * @param mission the mission, which contains the trials
     * @param redTactics the tactics Red may be playing with for each trial in the
     * mission. If the list contains only 1 Red tactic, that Red tactic will be
     * used to compute probabilities for all trials in the mission.
     * @param computeRedTacticProbs whether to compute the probability that Red is playing with each tactic and use
     * these probabilities when computing the Bayesian attack probabilities. If false, uses the actual (ground truth)
     * tactic Red is playing with to compute the Bayesian attack probabilities).
     * @param sigintReliability the SIGINT reliabilities. If null, the default
     * SIGINT reliabilities will be used.
     * @param subjectSigintProbs the values to use for P(Attack|SIGINT) when computing
     * the incremental Bayesian attack probabilities for P(Attack|SIGINT, Capability, Propensity)
     * and the subject was not probed for P(Attack|SIGINT) on a trial in the mission     * 
     * @param progressMonitor an optional progress monitor to track progress
     */
    public void computeNormativeRedAttackProbabilitiesForMission(Mission<?> mission,
            List<RedTacticType> redTactics, boolean computeRedTacticProbs,
            SigintReliability sigintReliability, SubjectSigintProbabilities subjectSigintProbs,
            IProgressMonitor progressMonitor) {
        //Compute the Red attack probabilities for each trial in the mission		
        if (mission.getTestTrials() == null || mission.getTestTrials().isEmpty()) {
            return;
        }
        if (computeRedTacticProbs) {
            if (redTactics == null || redTactics.isEmpty()) {
                throw new IllegalArgumentException("Red tactics cannot be empty.");
            }
        } else if (redTactics == null || (redTactics.size() != 1 && 
                redTactics.size() != mission.getTestTrials().size())) {
            throw new IllegalArgumentException("Red tactics cannot be empty and must contain exactly 1 Red tactic "
                    + "or a Red tactic for each trial in the mission");
        }

        //Create default SIGINT reliabilities if null
        if (sigintReliability == null) {
            SigintReliability.createDefaultSigintReliability();
        }

        RedTacticType redTactic = redTactics.size() == 1 ? redTactics.get(0) : null;
        List<Double> redTacticProbs = computeRedTacticProbs ? null : Arrays.asList(1.d);
        List<RedTacticParameters> redTacticsForTrial = computeRedTacticProbs ? 
                extractRedTacticParameters(redTactics) : null;

        int numAttacks = 0;
        int trialNum = 1;
        if (progressMonitor != null) {
            progressMonitor.setMinimum(1);
            progressMonitor.setMaximum(mission.getTestTrials().size());
            progressMonitor.setProgress(0);
        }
        for (IcarusTestTrial_Phase2 trial : mission.getTestTrials()) {
            //Compute the normative Red attack probabilities for the trial
            if (progressMonitor != null) {
                progressMonitor.setNote("Computing normative attack probabilities for trial " + trialNum);
            }
            if (computeRedTacticProbs) {
                //First compute the probabilities that Red is playing with each tactic on this trial
                redTacticProbs = computeNormativeRedTacticProbabilitiesForTrial(mission, redTactics, trial, 
                        trialNum - 1, numAttacks);                
            } else {
                redTacticsForTrial = Arrays.asList(redTactic == null ? redTactics.get(trialNum - 1).getTacticParameters() : 
                        redTactic.getTacticParameters());
            }            
            //Compute the normative cumualtive and incremental Red attack probabilities
            computeNormativeRedAttackProbabilitiesForTrial(trial, redTacticsForTrial, 
                    redTacticProbs, sigintReliability, subjectSigintProbs);

            //Compute the normative participant and normative Bayesian Blue actions for the trial
            if (progressMonitor != null) {
                progressMonitor.setNote("Computing normaitve Blue actions for trial " + trialNum);
            }
            computeNormativeBlueActionsForTrial(trial, redTacticsForTrial, redTacticProbs, sigintReliability);

            if (progressMonitor != null) {
                progressMonitor.setProgress(trialNum);
            }

            if (trial.getRedActionSelection() != null
                    && trial.getRedActionSelection().getRedAction().getAction() == RedActionType.Attack) {
                numAttacks++;
            }

            trialNum++;
        }
    }

    /**
     * Computes the probability that Red is playing with each tactic based on
     * the history of Red attacks. See the Test Specification for details on this calculation.
     *
     * @param mission the mission, which contains the trials with the history of Red attacks
     * @param redTactics the tactics Red may be playing with
     * @param currentTrial the current trial in the mission. Trials before this trial
     * will be included when looking at the history of Red attacks.
     * @return the probability that Red is playing with each tactic (in decimal format)
     */
    public List<Double> computeNormativeRedTacticProbabilitiesForTrial(Mission<?> mission, 
            List<RedTacticType> redTactics, IcarusTestTrial_Phase2 currentTrial) {
        if (redTactics == null || redTactics.isEmpty()) {
            return null;
        } else if (redTactics.size() == 1) {
            return Arrays.asList(1.d);
        } else {
            //Find the current trial and compute the number of attacks on previous trials
            int numPreviousTrials = 0;
            int numAttacks = 0;
            Iterator<? extends IcarusTestTrial_Phase2> trialIter = mission.getTestTrials().iterator();
            boolean currentTrialFound = false;
            while (trialIter.hasNext() && !currentTrialFound) {
                IcarusTestTrial_Phase2 trial = trialIter.next();
                if (trial == currentTrial) {
                    currentTrialFound = true;
                } else {
                    if (trial.getRedActionSelection() != null
                            && trial.getRedActionSelection().getRedAction().getAction() == RedActionType.Attack) {
                        numAttacks++;
                    }
                    numPreviousTrials++;
                }
            }
            return computeNormativeRedTacticProbabilitiesForTrial(mission, 
                    redTactics, currentTrial, numPreviousTrials, numAttacks);
        }
    }

    /**
     * Computes the probability that Red is playing with each tactic based on
     * the history of Red attacks. See the Test Specification for details on
     * this calculation.
     * 
     * @param mission the mission, which contains the trials with the history of Red attacks
     * @param redTactics the tactics Red may be playing with
     * @param currentTrial the current trial in the mission
     * @param numTrials the number of previous trials that have been played
     * @param numAttacks the total number of Red attacks that occurred on
     * previous trials
     * @return the probability that Red is playing with each tactic (in decimal format)
     */
    public List<Double> computeNormativeRedTacticProbabilitiesForTrial(Mission<?> mission,
            List<RedTacticType> redTactics, IcarusTestTrial_Phase2 currentTrial, int numTrials,
            int numAttacks) {
        //Compute the probabilities that Red is playing with each tactic on the current trial       
        List<Double> redTacticProbs = RedTacticProbsComputer.computeNormativeRedTacticProbabilities(
                mission.getMissionType(), redTactics, mission.getTestTrials(), numTrials, numAttacks);
        if (redTacticProbs != null && !redTacticProbs.isEmpty()
                && currentTrial instanceof Mission_4_5_6_Trial) {
            Mission_4_5_6_Trial mission456Trial = (Mission_4_5_6_Trial) currentTrial;
            if (mission456Trial.getRedTacticsProbe() != null
                    && mission456Trial.getRedTacticsProbe() instanceof RedTacticsProbabilityReportProbe) {
                RedTacticsProbabilityReportProbe probe
                        = (RedTacticsProbabilityReportProbe) mission456Trial.getRedTacticsProbe();
                probe = ensureRedTacticsProbabilityReportProbeInitialized(probe, null, redTactics);
                mission456Trial.setRedTacticsProbe(probe);
                int i = 0;
                for (Double prob : redTacticProbs) {
                    probe.getProbabilities().get(i).setNormativeProbability(prob);
                    i++;
                }
            }
        }
        return redTacticProbs;
    }
    
    /**
     * Create a list of the Red tactic parameters for the given Red tactic types.
     *
     * @param redTactics the Red tactic types
     * @return redTactics a list of the Red tactic parameters for the given Red tactic types
     */
    public static List<RedTacticParameters> extractRedTacticParameters(List<RedTacticType> redTactics) {
        if (redTactics != null && !redTactics.isEmpty()) {
            List<RedTacticParameters> tacticParameters
                    = new ArrayList<RedTacticParameters>(redTactics.size());
            for (RedTacticType redTactic : redTactics) {
                tacticParameters.add(redTactic.getTacticParameters());
            }
            return tacticParameters;
        }
        return null;
    }
    

    /**
     * Computes the normative (cumulative Bayesian and incremental Bayesian) 
     * Red attack probabilities for a trial. Probabilities are returned in decimal 
     * and not percent format. Also populates he normative cumulative and 
     * normative incremental probabilities in each AttackProbabilityReportProbe 
     * present in the trial.
     *
     * @param trial the trial, which contains the Blue locations and AttackProbabilityReportProbe probes
     * @param redTactic the tactic Red is playing with for the trial
     * @param sigintReliability The SIGINT reliabilities. If null, the default
     * SIGINT reliabilities will be used.
     * @param subjectSigintProbs the values to use for P(Attack|SIGINT) when computing
     * the incremental Bayesian attack probabilities for P(Attack|SIGINT, Capability, Propensity)
     * and the subject was not probed for P(Attack|SIGINT) on a trial in the mission
     * @return the normative (cumulative Bayesian and incremental Bayesian) Red 
     * attack probabilities at each location (in decimal format). Also populates 
     * the normative cumulative and normative incremental probabilities 
     * in each AttackProbabilityReportProbe present in the trial.
     */
    public CumulativeAndIncrementalRedAttackProbabilities 
        computeNormativeRedAttackProbabilitiesForTrial(IcarusTestTrial_Phase2 trial,
            RedTacticParameters redTactic, SigintReliability sigintReliability,
            SubjectSigintProbabilities subjectSigintProbs) {
        return computeNormativeRedAttackProbabilitiesForTrial(trial, Arrays.asList(redTactic),
                Arrays.asList(1.d), sigintReliability, subjectSigintProbs);
    }

    /**
     * Computes the normative (cumulative Bayesian and incremental Bayesian) 
     * Red attack probabilities for a trial. Probabilities are returned in decimal 
     * and not percent format. Also populates he normative cumulative and 
     * normative incremental probabilities in each AttackProbabilityReportProbe 
     * present in the trial.
     *
     * @param trial the trial, which contains the Blue locations and AttackProbabilityReportProbe probes
     * @param redTactics the tactics Red may be playing with
     * @param redTacticProbs the probability that Red is playing with each tactic
     * @param sigintReliability the SIGINT reliabilities. If null, the default
     * SIGINT reliabilities will be used.
     * @param subjectSigintProbs
     * @return the normative (cumulative Bayesian and incremental Bayesian) Red 
     * attack probabilities at each location (in decimal format). Also populates 
     * the normative cumulative and normative incremental probabilities 
     * in each AttackProbabilityReportProbe present in the trial.
     */
    public CumulativeAndIncrementalRedAttackProbabilities computeNormativeRedAttackProbabilitiesForTrial(
            IcarusTestTrial_Phase2 trial, List<RedTacticParameters> redTactics, 
            List<Double> redTacticProbs, SigintReliability sigintReliability,
            SubjectSigintProbabilities subjectSigintProbs) {       
        if (trial != null && trial.getBlueLocations() != null && !trial.getBlueLocations().isEmpty()) {
            //Compute the cumulative Bayesian probabilities
            List<RedAttackProbabilities> cumAttackProbs = computeNormativeCumulativeRedAttackProbabilities(trial.getBlueLocations(),
                    redTactics, redTacticProbs, trial.getHumint(), sigintReliability, 
                    getSigintLocationsForTrial(trial));
            //Compute the incremental Bayesian probabilities
            List<RedAttackProbabilities> incAttackProbs = computeNormativeIncrementalRedAttackProbabilities(
                    trial, trial.getBlueLocations().size(), subjectSigintProbs,
                    cumAttackProbs);
            if ((cumAttackProbs != null && !cumAttackProbs.isEmpty()) ||
                    incAttackProbs != null && !incAttackProbs.isEmpty()) {
                AttackProbabilityReportProbe probe_Pp = null;
                if (trial.getAttackPropensityProbe_Pp() != null) {
                    probe_Pp = ensureAttackProbabilityReportProbeInitialized(
                            trial.getAttackPropensityProbe_Pp(),
                            TrialPartProbeType.AttackProbabilityReport_Pp, "Pp",
                            trial.getBlueLocations());
                    if (trial.getAttackPropensityProbe_Pp() != probe_Pp) {
                        trial.setAttackPropensityProbe_Pp(probe_Pp);
                    }
                }
                AttackProbabilityReportProbe probe_Ppc = null;
                if (trial.getAttackProbabilityProbe_Ppc() != null) {
                    probe_Ppc = ensureAttackProbabilityReportProbeInitialized(
                            trial.getAttackProbabilityProbe_Ppc(),
                            TrialPartProbeType.AttackProbabilityReport_Ppc, "Ppc",
                            trial.getBlueLocations());
                    if (trial.getAttackProbabilityProbe_Ppc() != probe_Ppc) {
                        trial.setAttackProbabilityProbe_Ppc(probe_Ppc);
                    }
                }
                AttackProbabilityReportProbe probe_Pt = null;
                if (trial.getAttackProbabilityProbe_Pt() != null) {
                    probe_Pt = ensureAttackProbabilityReportProbeInitialized(
                            trial.getAttackProbabilityProbe_Pt(),
                            TrialPartProbeType.AttackProbabilityReport_Pt, "Pt",
                            trial.getBlueLocations());
                    if (trial.getAttackProbabilityProbe_Pt() != probe_Pt) {
                        trial.setAttackProbabilityProbe_Pt(probe_Pt);
                    }
                }
                AttackProbabilityReportProbe probe_Ptpc = null;
                if (trial.getAttackProbabilityProbe_Ptpc() != null) {
                    probe_Ptpc = ensureAttackProbabilityReportProbeInitialized(
                            trial.getAttackProbabilityProbe_Ptpc(),
                            TrialPartProbeType.AttackProbabilityReport_Ptpc, "Ptpc",
                            trial.getBlueLocations());
                    if (trial.getAttackProbabilityProbe_Ptpc() != probe_Ptpc) {
                        trial.setAttackProbabilityProbe_Ptpc(probe_Ptpc);
                    }
                }                
                
                for(int i = 0; i < trial.getBlueLocations().size(); i++) {                    
                    RedAttackProbabilities cumProbs = cumAttackProbs != null && i < cumAttackProbs.size() ?
                            cumAttackProbs.get(i) : null;
                    RedAttackProbabilities incProbs = incAttackProbs != null && i < incAttackProbs.size() ?
                            incAttackProbs.get(i) : null;
                    
                    if (probe_Pp != null) {
                        probe_Pp.getProbabilities().get(i).setNormativeProbability(
                                cumProbs != null ? cumProbs.pPropensity : null);                        
                        probe_Pp.getProbabilities().get(i).setNormativeIncrementalProbability(
                                incProbs != null ? incProbs.pPropensity : null);
                    }
                    if (probe_Ppc != null) {
                        probe_Ppc.getProbabilities().get(i).setNormativeProbability(
                                cumProbs != null ? cumProbs.pPropensityCapability : null);
                        probe_Ppc.getProbabilities().get(i).setNormativeIncrementalProbability(
                                incProbs != null ? incProbs.pPropensityCapability : null);    
                    }
                    if (probe_Pt != null) {                    	
                        probe_Pt.getProbabilities().get(i).setNormativeProbability(
                                cumProbs != null ? cumProbs.pActivity : null);
                        probe_Pt.getProbabilities().get(i).setNormativeIncrementalProbability(
                                incProbs != null ? incProbs.pActivity : null);
                    }
                    if (probe_Ptpc != null) {
                        probe_Ptpc.getProbabilities().get(i).setNormativeProbability(
                                cumProbs != null ? cumProbs.pActivityPropensityCapability : null);
                        probe_Ptpc.getProbabilities().get(i).setNormativeIncrementalProbability(
                                incProbs != null ? incProbs.pActivityPropensityCapability : null);
                    }
                }
            }            
           return new CumulativeAndIncrementalRedAttackProbabilities(cumAttackProbs, incAttackProbs);
        } else {
           return null;
        }
    }

    /**
     * Computes the normative (cumulative Bayesian) Red attack probabilities for each of the given
     * Blue locations. Probabilities are returned in decimal and not percent
     * format.
     *
     * @param locations the Blue locations
     * @param redTactic the tactic Red is playing with
     * @param humint the current HUMINT information on Red's capability to attack
     * @param sigintReliability the SIGINT reliabilities. If null, the default
     * SIGINT reliabilities will be used.
     * @param sigintLocations the IDs of the locations at which SIGINT information is
     * known to the participant
     * @return the normative Red attack probabilities at each location (in decimal format)
     */
    public List<RedAttackProbabilities> computeNormativeCumulativeRedAttackProbabilities(List<BlueLocation> locations,
            RedTacticParameters redTactic, HumintDatum humint, SigintReliability sigintReliability,
            List<String> sigintLocations) {
        return computeNormativeCumulativeRedAttackProbabilities(locations, Arrays.asList(redTactic),
                Arrays.asList(1.d), humint, sigintReliability, sigintLocations);
    }

    /**
     * Computes the normative (cumulative Bayesian) Red attack probabilities for each of the given
     * Blue locations. Probabilities are returned in decimal and not percent
     * format.
     * 
     * @param locations the locations
     * @param redTactics the tactics Red may be playing with
     * @param redTacticProbs the probability that Red is playing with each tactic
     * @param humint the current HUMINT information on Red's capability to
     * attack
     * @param sigintReliability the SIGINT reliabilities. If null, the default
     * SIGINT reliabilities will be used.
     * @param sigintLocations the IDs of the locations at which SIGINT information is
     * known to the participant
     * @return the normative (cumulative Bayesian) Red attack probabilities at each location (in decimal format)
     */
    public List<RedAttackProbabilities> computeNormativeCumulativeRedAttackProbabilities(
            List<BlueLocation> locations, List<RedTacticParameters> redTactics, 
            List<Double> redTacticProbs, HumintDatum humint, SigintReliability sigintReliability,
            List<String> sigintLocations) {
        if (sigintLocations != null && sigintLocations.size() > 1) {
            throw new IllegalArgumentException("Error, SIGINT can only be selected at one location");
        }
        if (redTactics != null && !redTactics.isEmpty() && locations != null && !locations.isEmpty()) {
            int numLocations = locations.size();
            Double[] priors = new Double[numLocations + 1];
            int sigintLocationIndex = -1;  
            Double sigintLikelihood = 0.d;            
            double priorsSum = 0;
            List<RedAttackProbabilities> attackProbabilities = 
                    new ArrayList<RedAttackProbabilities>(numLocations);                      
            int i = 0;
            for (BlueLocation location : locations) {                
                RedAttackProbabilities probs = new RedAttackProbabilities();
                attackProbabilities.add(probs);
                if (redTactics.size() > 1 && redTacticProbs != null && redTacticProbs.size() == redTactics.size()) {
                    //Compute P(Propensity) based on the probability that Red is playing with each tactic and the given
                    //values of P and U
                    Iterator<RedTacticParameters> redTacticsIter = redTactics.iterator();
                    Iterator<Double> redTacticProbsIter = redTacticProbs.iterator();
                    probs.pPropensity = 0d;
                    while (redTacticsIter.hasNext()) {
                        RedTacticParameters redTactic = redTacticsIter.next();
                        Double redTacticProb = redTacticProbsIter.next();
                        probs.pPropensity += redTactic.getAttackProbability(
                                location.getOsint() != null ? location.getOsint().getRedVulnerability_P() : 0,
                                location.getImint() != null ? location.getImint().getRedOpportunity_U() : 0)
                                * redTacticProb;
                    }
                } else {
                    //Get P(Propensity) from the Red tactic(s) given vulnerability (P) and utility (U) at the location
                    probs.pPropensity = redTactics.get(0).getAttackProbability(
                            location.getOsint() != null ? location.getOsint().getRedVulnerability_P() : 0,
                            location.getImint() != null ? location.getImint().getRedOpportunity_U() : 0);
                }

                //Compute P(Propensity, Capability) as P(Propensity) * P(Capability) (from HUMINT)
                probs.pPropensityCapability = humint != null && humint.getRedCapability_Pc() != null
                        ? probs.pPropensity * humint.getRedCapability_Pc() : probs.pPropensity;
                priors[i] = probs.pPropensityCapability;
                priorsSum += priors[i] != null ? priors[i] : 0d;

                //Compute P(Activity) if SIGINT is available at the location
                if (sigintReliability != null && location.getSigint() != null
                        && location.getSigint().isRedActivityDetected() != null
                        && isSigintAvailableAtLocation(location, sigintLocations)) {
                    //Chatter: Pt = P(y|Y) = P(Y|y) / [P(Y|y) + P(Y|n)]
                    //Silence: Pt = P(y|N) = P(N|y) / [P(N|y) + P(N|n)]
                    probs.pActivity = location.getSigint().isRedActivityDetected()
                            ? sigintReliability.getChatterLikelihood_attack()
                            / (sigintReliability.getChatterLikelihood_attack() + sigintReliability.getChatterLikelihood_noAttack())
                            : sigintReliability.getSilenceLikelihood_attack()
                            / (sigintReliability.getSilenceLikelihood_attack() + sigintReliability.getSilenceLikelihood_noAttack());
                    sigintLikelihood = probs.pActivity;
                    sigintLocationIndex = i;
                    //DEBUG CODE
                    /*if(numLocations > 1) {
                    	System.out.println("SIGINT obtained at location: " + location.getId() + ", SIGINT Locations: " + sigintLocations);
                    }*/
                }
                i++;
            }
            
            //Compute P(Activity, Propensity, Capability) using priors P(Propensity, Capability) and 
            //likelihoods P(Activity) (if SIGINT is available)
            if(sigintLocationIndex >= 0 && sigintLikelihood != null) {
                //Case where SIGINT was obtained at a location
                priors[numLocations] = 1.d - priorsSum;
                List<Double> posteriors = new ArrayList<Double>(numLocations + 1);                
                for(i = 0; i < numLocations + 1; i++) {
                    double likelihood;
                    if(i == sigintLocationIndex) {                        
                        likelihood = sigintLikelihood;
                    } else {                     
                        likelihood = (1-sigintLikelihood) / numLocations;
                        if(i < locations.size()) {
                            attackProbabilities.get(i).pActivity = likelihood;
                        }
                    }                  
                    posteriors.add(priors[i] * likelihood);
                }
                posteriors = ProbabilityUtils.normalizeDecimalProbabilities(posteriors, posteriors);
                for(i = 0; i < numLocations; i++) {
                    attackProbabilities.get(i).pActivityPropensityCapability = posteriors.get(i);
                }                
            } else {
                //Case for which SIGINT was not obtained              
                for(RedAttackProbabilities probs : attackProbabilities) {
                    probs.pActivityPropensityCapability = probs.pPropensityCapability;
                }
            }            
            return attackProbabilities;
        }
        return null;
    }

    /**
     * Computes the normative (incremental Bayesian) Red attack probabilities for each of the given
     * Blue locations. Probabilities are returned in decimal and not percent
     * format. Incremental probabilities are computed using the participant probabilities
     * from the AttackProbabilityReportProbes present in the trial. Details for each computation:
     * P(Propensity) = Bayesian P(Propensity) using the BLUEBOOK, or "quasi-Bayesian" P(Propensity) for Missions where the Red tactic is not known
     * P(Propensity, Capability) = Participant P(Propensity) * P(Capability) (from HUMINT)
     * P(Activity) = Bayesian P(Activity) computing using the SIGINT reliabilities
     * P(Activity, Propensity, Capability) = Bayesian combination of Participant P(Propensity, Capability) 
     * and Participant P(Activity).
     *
     * @param trial the trial, which contains the AttackProbabilityReportProbes containing
     * participant probabilities (in percent format) and HUMINT information.
     * @param numLocations the number of Blue locations in the trial
     * @param subjectSigintProbs TODO: Document this
     * @param cumBayesianRedAttackProbabilities TODO: Document this
     * @return the normative (incremental Bayesian) Red attack probabilities at each location (in decimal format)
     */
    public List<RedAttackProbabilities> computeNormativeIncrementalRedAttackProbabilities(
            IcarusTestTrial_Phase2 trial, int numLocations, SubjectSigintProbabilities subjectSigintProbs,
            List<RedAttackProbabilities> cumBayesianRedAttackProbabilities) {
        if (trial != null) {
            List<RedAttackProbabilities> participantRedAttackProbabilities
                    = new ArrayList<RedAttackProbabilities>(numLocations);
            for (int i = 0; i < numLocations; i++) {
                participantRedAttackProbabilities.add(new RedAttackProbabilities());
            }
            if (trial.getAttackPropensityProbe_Pp() != null
                    && trial.getAttackPropensityProbe_Pp().getProbabilities() != null
                    && !trial.getAttackPropensityProbe_Pp().getProbabilities().isEmpty()) {
                List<AttackProbability> probs = trial.getAttackPropensityProbe_Pp().getProbabilities();
                for (int i = 0; i < numLocations; i++) {
                    if (i < probs.size()) {
                        AttackProbability prob = probs.get(i);
                        participantRedAttackProbabilities.get(i).pPropensity
                                = prob != null  && prob.getProbability() != null ? prob.getProbability() / 100.d : null;
                    }
                }
            }
            if (trial.getAttackProbabilityProbe_Ppc() != null
                    && trial.getAttackProbabilityProbe_Ppc().getProbabilities() != null
                    && !trial.getAttackProbabilityProbe_Ppc().getProbabilities().isEmpty()) {
                List<AttackProbability> probs = trial.getAttackProbabilityProbe_Ppc().getProbabilities();
                for (int i = 0; i < numLocations; i++) {
                    if (i < probs.size()) {
                        AttackProbability prob = probs.get(i);
                        participantRedAttackProbabilities.get(i).pPropensityCapability
                                = prob != null && prob.getProbability() != null ? prob.getProbability() / 100.d : null;
                    }
                }
            }
            List<AttackProbability> sigintProbs = null;
            if (trial.getAttackProbabilityProbe_Pt() != null
                    && trial.getAttackProbabilityProbe_Pt().getProbabilities() != null
                    && !trial.getAttackProbabilityProbe_Pt().getProbabilities().isEmpty()) {
                sigintProbs = trial.getAttackProbabilityProbe_Pt().getProbabilities();
            } else if (subjectSigintProbs != null && subjectSigintProbs.getPtChatter() != null
                    && subjectSigintProbs.getPtSilent() != null) {
                sigintProbs = createSigintProbabilitiesForTrial(
                        trial, subjectSigintProbs);
            }
            if (sigintProbs != null && !sigintProbs.isEmpty()) {
                for (int i = 0; i < numLocations; i++) {
                    if (i < sigintProbs.size()) {
                        AttackProbability prob = sigintProbs.get(i);
                        participantRedAttackProbabilities.get(i).pActivity
                                = prob != null && prob.getProbability() != null
                                ? prob.getProbability() / 100.d : null;
                    }
                }
            }
            return computeNormativeIncrementalRedAttackProbabilities(
                    participantRedAttackProbabilities, cumBayesianRedAttackProbabilities,
                    trial.getHumint());
        }
        return null;
    }
    
    /**
     * Creates a list of attack probabilities indicating P(Attack|SIGINT) at each
     * location using the subject-indicated values of P(Attack|Chatter) and 
     * P(Attack|Silent) based on the location at which SIGINT is available.
     * 
     * @param trial the trial, which contains either a SIGINT selection probe
     * or a SIGINT presentation probe indicating the location at which SIGINT is available. Also
     * contains the Blue locations and the SIGINT information at each location
     * @param subjectSigintProbs the subject values of P(Attack|Chatter) and P(Attack|Silent) 
     * @return List of attack probabilities indicating P(Attack|SIGINT) at each location.
     */
    public List<AttackProbability> createSigintProbabilitiesForTrial(IcarusTestTrial_Phase2 trial,
            SubjectSigintProbabilities subjectSigintProbs) {
        List<AttackProbability> participantPt = null;
        if (subjectSigintProbs != null && 
                (trial.getSigintPresentation() != null || trial.getSigintSelectionProbe() != null)) {
            //Get the locations at which SIGINT is available for the trial
            List<String> locationIds = getSigintLocationsForTrial(trial);            
            if(locationIds != null && locationIds.size() > 1) {
                throw new IllegalArgumentException("Error, SIGINT can only be selected at one location");
            }
            String sigintLocation = locationIds != null && !locationIds.isEmpty() ? locationIds.get(0) : null;            
            if (sigintLocation != null) {
                int numLocations = trial.getBlueLocations().size();
                participantPt = new ArrayList<AttackProbability>(numLocations);                
                int i = 0;
                for (BlueLocation location : trial.getBlueLocations()) {
                    if (sigintLocation.equalsIgnoreCase(location.getId()) || numLocations == 1) {
                        AttackProbability prob = new AttackProbability(location.getId(), i,
                                RedActionType.Attack);
                        participantPt.add(prob);
                        prob.setProbability(location.getSigint() != null
                                && location.getSigint().isRedActivityDetected() != null
                                && location.getSigint().isRedActivityDetected() ? 
                                    subjectSigintProbs.getPtChatter() : subjectSigintProbs.getPtSilent());
                    } else {
                        participantPt.add(null);
                    }
                    i++;
                }
            }
        }
        return participantPt;
    }
    
    /**
     * Computes the normative (incremental Bayesian) Red attack probabilities for each of the given
     * Blue locations. Probabilities are returned in decimal and not percent
     * format. Incremental probabilities are computed using the given participant probabilities.
     * Details for each computation:
     * P(Propensity) = Bayesian P(Propensity) using the BLUEBOOK, or "quasi-Bayesian" P(Propensity) for Missions where the Red tactic is not known
     * P(Propensity, Capability) = Participant P(Propensity) * P(Capability) (from HUMINT)
     * P(Activity) = Bayesian P(Activity) computing using the SIGINT reliabilities
     * P(Activity, Propensity, Capability) = Bayesian combination of Participant P(Propensity, Capability) 
     * and Participant P(Activity).
     * 
     * @param participantRedAttackProbabilities the participant Red attack
     * probabilities at each location (in decimal format)
     * @param cumBayesianRedAttackProbabilities TODO: Document this
     * @param humint the HUMINT datum
     * @return the normative (incremental Bayesian) Red attack probabilities at each location (in decimal format)
     */
    public List<RedAttackProbabilities> computeNormativeIncrementalRedAttackProbabilities(
            List<RedAttackProbabilities> participantRedAttackProbabilities, 
            List<RedAttackProbabilities> cumBayesianRedAttackProbabilities,
            HumintDatum humint) {
        if(participantRedAttackProbabilities != null && !participantRedAttackProbabilities.isEmpty() &&
                cumBayesianRedAttackProbabilities != null && 
                participantRedAttackProbabilities.size() == cumBayesianRedAttackProbabilities.size()) {            
            int numLocations = participantRedAttackProbabilities.size();
            Double[] priors = new Double[numLocations + 1];
            int sigintLocationIndex = -1;  
            Double participantSigintLikelihood = 0.d;            
            Double bayesianSigintLikelihood = 0.d;            
            double priorsSum = 0;
            List<RedAttackProbabilities> attackProbabilities = 
                    new ArrayList<RedAttackProbabilities>(numLocations);
            int i = 0;
            boolean pPropensityCapabilityAvailable = true;
            Iterator<RedAttackProbabilities> participantProbsIter = participantRedAttackProbabilities.iterator();
            Iterator<RedAttackProbabilities> cumProbsIter = cumBayesianRedAttackProbabilities.iterator();
            while (participantProbsIter.hasNext()) {
                //for(RedAttackProbabilities participantProbs : participantRedAttackProbabilities) {
                RedAttackProbabilities participantProbs = participantProbsIter.next();
                RedAttackProbabilities cumProbs = cumProbsIter.next();
                RedAttackProbabilities probs = new RedAttackProbabilities();
                attackProbabilities.add(probs);
                //Incremental P(Propensity)is the cumulative Bayesian value of P(Propensity)
                probs.pPropensity = cumProbs.pPropensity; //participantProbs.pPropensity;                
                //Compute Incremental P(Propensity, Capability) as 
                //participant value of P(Propensity) * P(Capability) (from HUMINT)                
                probs.pPropensityCapability = participantProbs.pPropensity == null ? null
                        : humint != null && humint.getRedCapability_Pc() != null
                        ? participantProbs.pPropensity * humint.getRedCapability_Pc()
                        : participantProbs.pPropensity;              
                if (participantProbs.pPropensityCapability != null) {                    
                    priors[i] = participantProbs.pPropensityCapability;
                    priorsSum += priors[i] != null ? priors[i] : 0d;
                } else {
                    pPropensityCapabilityAvailable = false;
                }
                if (participantProbs.pActivity != null) {
                    //Incremental P(Activity) is the cumulative Bayesian value of P(Activity)
                    sigintLocationIndex = i;
                    probs.pActivity = cumProbs.pActivity;
                    bayesianSigintLikelihood = cumProbs.pActivity;
                    participantSigintLikelihood = participantProbs.pActivity;                    
                }
                i++;
            }
            //Compute P(Activity, Propensity, Capability) using participant priors P(Propensity, Capability) and 
            //participant likelihood P(Activity) (if P(Activity) is available)
            if(sigintLocationIndex >= 0 && participantSigintLikelihood != null && pPropensityCapabilityAvailable) {
                //Case where P(Activity) and P(Propensity, Capability) are available
                priors[numLocations] = 1.d - priorsSum;                
                List<Double> posteriors = new ArrayList<Double>(numLocations + 1);                
                for(i = 0; i < numLocations + 1; i++) {
                    double likelihood;
                    if(i == sigintLocationIndex) {                        
                        likelihood = participantSigintLikelihood;
                    } else {                     
                        likelihood = (1-participantSigintLikelihood) / numLocations;
                        if(i < numLocations) {
                            if(bayesianSigintLikelihood != null) {                                
                                attackProbabilities.get(i).pActivity = 
                                        (1-bayesianSigintLikelihood) / numLocations;
                            }
                        }
                    }                  
                    posteriors.add(priors[i] * likelihood);
                }
                posteriors = ProbabilityUtils.normalizeDecimalProbabilities(posteriors, posteriors);
                for(i = 0; i < numLocations; i++) {
                    attackProbabilities.get(i).pActivityPropensityCapability = posteriors.get(i);
                }                
            } else {
                //Case where P(Activity) or P(Propensity, Capability) is not available        
                for(RedAttackProbabilities probs : attackProbabilities) {
                    probs.pActivityPropensityCapability = probs.pPropensityCapability;
                }
            }        
            return attackProbabilities;
        }
        return null;
    }
    
    /**
     * Ensure an attack probability probe is properly initialized.
     *
     * @param probe the attack probability probe
     * @param type the type of attack probability probe
     * @param id the probe ID
     * @param locations the Blue locations that are being probed
     * @return the properly initialized probe
     */
    public static AttackProbabilityReportProbe ensureAttackProbabilityReportProbeInitialized(
            AttackProbabilityReportProbe probe, TrialPartProbeType type,
            String id, List<BlueLocation> locations) {
        if (probe == null) {
            probe = new AttackProbabilityReportProbe(type, id, null, null);
        }
        if (locations != null && !locations.isEmpty()) {
            List<AttackProbability> probs = probe.getProbabilities();
            if (probs == null || probs.size() != locations.size()) {
                probs = new ArrayList<AttackProbability>(locations.size());
                for (int i = 0; i < locations.size(); i++) {
                    probs.add(new AttackProbability());
                }
            }
            int i = 0;
            for (BlueLocation location : locations) {
                AttackProbability attackProb = probs.get(i);
                if(attackProb == null) {
                	attackProb = new AttackProbability();
                	probs.set(i, attackProb);
                }
                attackProb.setLocationId(location.getId());
                attackProb.setLocationIndex(i);
                attackProb.setRedAction(RedActionType.Attack);
                i++;
            }
        }
        return probe;
    }    
    
    /**
     * Ensure a Red tactics probability probe is properly initialized.
     *
     * @param probe the Red tactics probability probe
     * @param id the probe ID
     * @param possibleRedTactics the tactics Red may be playing with
     * @return the properly initialized probe
     */
    public static RedTacticsProbabilityReportProbe ensureRedTacticsProbabilityReportProbeInitialized(
            RedTacticsProbabilityReportProbe probe, String id, 
            List<RedTacticType> possibleRedTactics) {
        if (probe == null) {
            probe = new RedTacticsProbabilityReportProbe();
            probe.setId(id);
        }
        if (possibleRedTactics != null && !possibleRedTactics.isEmpty()) {
            List<RedTacticProbability> probs = probe.getProbabilities();
            if (probs == null || probs.size() != possibleRedTactics.size()) {
                probs = new ArrayList<RedTacticProbability>(possibleRedTactics.size());
                for (RedTacticType redTactic : possibleRedTactics) {
                    probs.add(new RedTacticProbability(redTactic));
                }
            }            
        }
        return probe;
    }

    /**
     * Get whether SIGINT has been shown to the participant at a location.
     *
     * @param location the Blue location
     * @param sigintLocations the IDs of the locations at which SIGINT information is
     * known to the participant
     * @return whether SIGINT has been shown to the participant at the given location
     */
     public static boolean isSigintAvailableAtLocation(BlueLocation location,
            List<String> sigintLocations) {
    	if(sigintLocations != null && !sigintLocations.isEmpty()) {
    		for (String locationId : sigintLocations) {
                if (location.getId().equals(locationId)) {
                    return true;
                }
            }
    	}
    	return false;
    }

    /**
     * Computes the normative (best) SIGINT selection, which is to select SIGINT at
     * the location with the highest probability of Red attack. Computes the normative 
     * (cumulative Bayesian) Red attack probabilities, which are used to determine 
     * the best SIGINT location.
     * 
     * @param trial the trial, which contains the SIGINT selection probe and the participant's probabilities of Red attack at each location
     * @param redTactic the tactic Red is playing with
     * @param sigintReliability the SIGINT reliabilities. If null, the default
     * SIGINT reliabilities will be used.
     * @return the best location, or locations, at which to obtain SIGINT based on the normative 
     * (cumulative Bayesian) Red attack probabilities. Will return numSigintSelections locations.
     * Also populates the normative participant SIGINT selections in the SIGINT selection probe 
     * if the SIGINT selection probe is present and the participant's Red attack probabilities 
     * are present.
     */
    public List<String> computeNormativeSigintSelectionsForTrial(IcarusTestTrial_Phase2 trial,
            RedTacticParameters redTactic, SigintReliability sigintReliability) {
        return computeNormativeSigintSelectionsForTrial(trial, Arrays.asList(redTactic),
                Arrays.asList(1.d), sigintReliability);
    }

    /**
     * Computes the normative (best) SIGINT selection, which is to select SIGINT at
     * the location with the highest probability of Red attack. Computes the normative 
     * (cumulative Bayesian) Red attack probabilities, which are used to determine 
     * the best SIGINT location.
     * 
     * @param trial the trial, which contains the SIGINT selection probe and the participant's probabilities of Red attack at each location
     * @param redTactics the tactics Red may be playing with
     * @param redTacticProbs the probability that Red is playing with each tactic
     * @param sigintReliability the SIGINT reliabilities. If null, the default
     * SIGINT reliabilities will be used.
     * @return the best location, or locations, at which to obtain SIGINT based on the normative 
     * (cumulative Bayesian) Red attack probabilities. Will return numSigintSelections locations.
     * Also populates the normative participant SIGINT selections in the SIGINT selection probe 
     * if the SIGINT selection probe is present and the participant's Red attack probabilities 
     * are present.
     */
    public List<String> computeNormativeSigintSelectionsForTrial(IcarusTestTrial_Phase2 trial,
            List<RedTacticParameters> redTactics, List<Double> redTacticProbs,
            SigintReliability sigintReliability) {
        return computeNormativeSigintSelectionsForTrial(trial,
                computeNormativeCumulativeRedAttackProbabilities(trial.getBlueLocations(),
                        redTactics, redTacticProbs, trial.getHumint(),
                        sigintReliability, getSigintLocationsForTrial(trial)));
    }

    /**
     * Computes the normative (best) SIGINT selection, which is to select SIGINT at
     * the location with the highest probability of Red attack. 
     * 
     * @param trial the trial, which contains the SIGINT selection probe and the participant's probabilities of Red attack at each location
     * @param attackProbabilities the probability of Red attack at each location
     * @return the best location, or locations, at which to obtain SIGINT based on the given attackProbabilities. 
     * Will return numSigintSelections locations. Also populates the normative participant SIGINT
     * selections in the SIGINT selection probe if the SIGINT selection probe is present and the participant's
     * Red attack probabilities are present.
     */
    public List<String> computeNormativeSigintSelectionsForTrial(IcarusTestTrial_Phase2 trial,
            List<RedAttackProbabilities> attackProbabilities) {
        if (trial.getSigintSelectionProbe() != null) {
            SigintSelectionProbe sigintSelectionProbe = trial.getSigintSelectionProbe();

            //Get the probabilities from the attack probabilities probe
            AttackProbabilityReportProbe attackProbabilityProbe = null;
            DatumType attackProbabilityType = null;
            if (trial.getAttackProbabilityProbe_Ppc() != null) {
                attackProbabilityProbe = trial.getAttackProbabilityProbe_Ppc();
                attackProbabilityType = DatumType.AttackProbabilityReport_Capability_Propensity;
            } else if (trial.getAttackPropensityProbe_Pp() != null) {
                attackProbabilityProbe = trial.getAttackPropensityProbe_Pp();
                attackProbabilityType = DatumType.AttackProbabilityReport_Propensity;
            }

            if (attackProbabilityProbe != null && attackProbabilityProbe.isResponsePresent()) {
                //Get the participant's Red attack probabilities
                List<Double> participantProbs = new ArrayList<Double>();
                for (AttackProbability prob : attackProbabilityProbe.getProbabilities()) {
                    participantProbs.add(prob.getProbability() / 100d);
                }

                //Compute the normative participant SIGINT selection based on the participant's Red attack probabilities
                sigintSelectionProbe.setNormativeParticipantLocationIds(
                        computeNormativeSigintSelections(sigintSelectionProbe.getLocationIds(),
                                sigintSelectionProbe.getNumSigintSelections(), participantProbs));
            }

            //Compute the normative Bayesian SIGINT selection			
            if (attackProbabilities != null && !attackProbabilities.isEmpty()) {
                List<Double> bayesianProbs = new ArrayList<Double>();
                for (RedAttackProbabilities prob : attackProbabilities) {
                    if (attackProbabilityType == null
                            || attackProbabilityType == DatumType.AttackProbabilityReport_Capability_Propensity) {
                        bayesianProbs.add(prob.pPropensityCapability);
                    } else {
                        bayesianProbs.add(prob.pPropensity);
                    }
                }
                List<String> locationIds = computeNormativeSigintSelections(sigintSelectionProbe.getLocationIds(),
                        sigintSelectionProbe.getNumSigintSelections(), bayesianProbs);
                sigintSelectionProbe.setNormativeBayesianLocationIds(locationIds);
                return locationIds;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Computes the normative (best) SIGINT selection, which is to select SIGINT at
     * the location with the highest probability of Red attack.
     * 
     * @param locationIds the IDs of each Blue location     
     * @param numSigintSelections the number of locations at which to obtain SIGINT (always just 1 for the Phase 2 CP).
     * @param attackProbabilities the probability of Red attack at each location
     * @return the best location, or locations, at which to obtain SIGINT based on 
     * the given attackProbabilities. Will return numSigintSelections locations.
     */
    public List<String> computeNormativeSigintSelections(List<String> locationIds, 
            int numSigintSelections, List<Double> attackProbabilities) {
        if (numSigintSelections > 1) {
            throw new IllegalArgumentException("Error, SIGINT can only be selected at one location");
        }
        if (locationIds != null && !locationIds.isEmpty()
                && attackProbabilities != null && attackProbabilities.size() == locationIds.size()) {
            Double maxAttackProb = null;
            //List<String> bestLocations = null;
            String bestLocation = null;
            Iterator<String> locationIter = locationIds.iterator();
            Iterator<Double> attackProbIter = attackProbabilities.iterator();
            while (locationIter.hasNext()) {
                Double attackProb = attackProbIter.next();
                String locationId = locationIter.next();
                if (maxAttackProb == null || attackProb > maxAttackProb) {
                    maxAttackProb = attackProb;
                    bestLocation = locationId;
                    //bestLocations.clear();
                    //bestLocations.add(locationId);
                } //else if(attackProb == maxAttackProb) {
                   // bestLocations.add(locationId);
                //}
            }
            List<String> normativeLocations = new ArrayList<String>(numSigintSelections);
            for (int i = 0; i < numSigintSelections; i++) {
                normativeLocations.add(bestLocation);
            }
            return normativeLocations;
        }
        return null;
    }

    /**
     * Computes the selection of Blue actions at each Blue location with the highest 
     * expected utility (see the Test Specification for details on this calculation).
     * First computes the normative (cumulative Bayesian) Red attack probabilities, which are used to determine 
     * the expected utilities of each possible Blue action selection. Also computes 
     * the normative Blue actions based on the participant's Red attack probabilities, which are populated
     * in the Blue Action selection probe if present in the trial.
     * 
     * @param trial the trial, which contains the Blue locations, the Blue Action selection probe,
     * and the participant's probabilities of Red attack at each location
     * @param redTactic the tactic Red is playing with
     * @param sigintReliability the SIGINT reliabilities. If null, the default
     * SIGINT reliabilities will be used.
     * @return the Blue actions at each location with the highest expected utility, 
     * and the expected utility. Also populates the normative participant Blue action
     * selections in the Blue Action selection probe if the Blue Action selection probe is present and the participant's
     * Red attack probabilities are present.
     */
    public BlueActionsExpectedUtility computeNormativeBlueActionsForTrial(IcarusTestTrial_Phase2 trial,
            RedTacticParameters redTactic, SigintReliability sigintReliability) {
        if (redTactic != null) {
            return computeNormativeBlueActionsForTrial(trial, Arrays.asList(redTactic),
                    Arrays.asList(1.d), sigintReliability);
        } else {
            if (debug) {
                System.out.println("Warning, no Red tactic for trial when computing normative Blue actions.");
            }
            return null;
        }
    }

    /**
     * Computes the selection of Blue actions at each Blue location with the highest 
     * expected utility (see the Test Specification for details on this calculation).
     * First computes the normative (cumulative Bayesian) Red attack probabilities, which are used to determine 
     * the expected utilities of each possible Blue action selection. Also computes 
     * the normative Blue actions based on the participant's Red attack probabilities, which are populated
     * in the Blue Action selection probe if present in the trial.
     * 
     * @param trial the trial, which contains the Blue locations, the Blue Action selection probe,
     * and the participant's probabilities of Red attack at each location
     * @param redTactics the tactics Red may be playing with
     * @param redTacticProbs the probability that Red is playing with each tactic
     * @param sigintReliability the SIGINT reliabilities. If null, the default
     * SIGINT reliabilities will be used.
     * @return the Blue actions at each location with the highest expected utility, 
     * and the expected utility.  Also populates the normative participant Blue action
     * selections in the Blue Action selection probe if the Blue Action selection probe is present and the participant's
     * Red attack probabilities are present.
     */
    public BlueActionsExpectedUtility computeNormativeBlueActionsForTrial(IcarusTestTrial_Phase2 trial,
            List<RedTacticParameters> redTactics, List<Double> redTacticProbs,
            SigintReliability sigintReliability) {    	
        return computeNormativeBlueActionsForTrial(trial,
                computeNormativeCumulativeRedAttackProbabilities(trial.getBlueLocations(),
                        redTactics, redTacticProbs, trial.getHumint(), sigintReliability,
                        getSigintLocationsForTrial(trial)));
    }
    
    /**
     * Gets the locations at which SIGINT information is available to the participant, 
     * either from the location selected in a SIGINT selection probe, or from
     * the location presented in a SIGINT presentation probe.
     * 
     * @param trial the trial
     * @return the IDs of the locations at which SIGINT information is available to the participant
     */
    protected static List<String> getSigintLocationsForTrial(IcarusTestTrial_Phase2 trial) {
    	List<String> sigintLocations = null;
    	if(trial.getSigintSelectionProbe() != null) {
    		sigintLocations = trial.getSigintSelectionProbe().getSelectedLocationIds();
    	} else if(trial.getSigintPresentation() != null && !trial.getSigintPresentation().isEmpty()) {
    		sigintLocations = new ArrayList<String>();
    		for (SigintPresentationProbe sigint : trial.getSigintPresentation()) {
                if (sigint.getLocationIds() != null) {
                    sigintLocations.addAll(sigint.getLocationIds());
                }
    		}
    	}
    	return sigintLocations;
    }

    /**
     * Computes the selection of Blue actions at each Blue location with the highest 
     * expected utility (see the Test Specification for details on this calculation). 
     * Computes the normative Blue actions based on the participant's Red attack 
     * probabilities and the normative Blue actions based on the given Red attack
     * probabilities.
     * 
     * @param trial the trial, which contains the Blue locations, the Blue Action selection probe,
     * and the participant's probabilities of Red attack at each location
     * @param attackProbabilities the probability of Red attack at each location
     * @return the Blue actions at each location with the highest expected utility, 
     * and the expected utility. Also populates the normative participant Blue action
     * selections in the Blue Action selection probe if the Blue Action selection probe is present and the participant's
     * Red attack probabilities are present.
     */
    public BlueActionsExpectedUtility computeNormativeBlueActionsForTrial(IcarusTestTrial_Phase2 trial,
            List<RedAttackProbabilities> attackProbabilities) {
        if (trial.getBlueActionSelection() != null && trial.getBlueLocations() != null
                && !trial.getBlueLocations().isEmpty()) {
            BlueActionSelectionProbe blueActionProbe = trial.getBlueActionSelection();

            //Get the probabilities from the last attack probabilities probe
            AttackProbabilityReportProbe attackProbabilityProbe = null;
            DatumType attackProbabilityType = null;
            if (trial.getAttackProbabilityProbe_Ptpc() != null) {
                attackProbabilityProbe = trial.getAttackProbabilityProbe_Ptpc();
                attackProbabilityType = DatumType.AttackProbabilityReport_Activity_Capability_Propensity;
            } else if (trial.getAttackProbabilityProbe_Ppc() != null) {
                attackProbabilityProbe = trial.getAttackProbabilityProbe_Ppc();
                attackProbabilityType = DatumType.AttackProbabilityReport_Capability_Propensity;
            } else if (trial.getAttackPropensityProbe_Pp() != null) {
                attackProbabilityProbe = trial.getAttackPropensityProbe_Pp();
                attackProbabilityType = DatumType.AttackProbabilityReport_Propensity;
            }

            if (attackProbabilityProbe != null && attackProbabilityProbe.isResponsePresent()) {
                //Get the participant's Red attack probabilities
                List<Double> participantProbs = new ArrayList<Double>();
                for (AttackProbability prob : attackProbabilityProbe.getProbabilities()) {
                    participantProbs.add(prob.getProbability() / 100d);
                }		

                //Compute expected utility of participant Blue actions based on participant's Red attack probabilities
                if (blueActionProbe.isResponsePresent()) {
                    blueActionProbe.setExpectedUtility(computeBlueActionsExpectedUtility(
                            blueActionProbe.getBlueActions(), trial.getBlueLocations(),
                            participantProbs));
                }

                //Compute the normative participant Blue actions and their expected utilities
                BlueActionsExpectedUtility normativeParticipantBlueActions
                        = computeNormativeBlueActions(trial.getBlueLocations(),
                                participantProbs);
                if (normativeParticipantBlueActions != null) {
                    blueActionProbe.setNormativeParticipantBlueActions(normativeParticipantBlueActions.blueActions);
                    blueActionProbe.setNormativeParticipantExpectedUtility(normativeParticipantBlueActions.expectedUtility);
                }
            }

            //Compute the normative Bayesian Blue actions and their expected utilities			 
            if (attackProbabilities != null && !attackProbabilities.isEmpty()) {
                List<Double> bayesianProbs = new ArrayList<Double>();
                for (RedAttackProbabilities prob : attackProbabilities) {
                    if (attackProbabilityType == null
                            || attackProbabilityType == DatumType.AttackProbabilityReport_Activity_Capability_Propensity) {
                        bayesianProbs.add(prob.pActivityPropensityCapability);
                    } else if (attackProbabilityType == DatumType.AttackProbabilityReport_Capability_Propensity) {
                        bayesianProbs.add(prob.pPropensityCapability);
                    } else {
                        bayesianProbs.add(prob.pPropensity);
                    }
                }
                BlueActionsExpectedUtility normativeBayesianBlueActions
                        = computeNormativeBlueActions(trial.getBlueLocations(), bayesianProbs);
                if (normativeBayesianBlueActions != null) {
                    blueActionProbe.setNormativeBayesianBlueActions(normativeBayesianBlueActions.blueActions);
                    blueActionProbe.setNormativeBayesianExpectedUtility(normativeBayesianBlueActions.expectedUtility);
                }
                return normativeBayesianBlueActions;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Computes the selection of Blue actions at each Blue location with the highest 
     * expected utility (see the Test Specification for details on this calculation).
     * 
     * @param blueLocations the Blue locations
     * @param attackProbabilities the probability of Red attack at each location
     * @return the Blue actions at each location with the highest expected utility, 
     * and the expected utility
     */
    public BlueActionsExpectedUtility computeNormativeBlueActions(List<BlueLocation> blueLocations,
            List<Double> attackProbabilities) {
        /*
         * Thus Blue has four options across the points [1,2] as follows: 
         * 	A = [d1,d2], B = [d1,~d2], C = [~d1,d2], and D = [~d1,~d2]. 
         * And Red has three options across the points [1,2] as follows: 
         * 	A = [a1,~a2], B = [~a1,a2], and C = [~a1,~a2].
         * Each value of Pp can then be combined with Pc (which is the same for each Blue point) and Pt 
         * (if SIGINT is available at the point, see Foraging below), to compute the probability of attack 
         * at each point: 
         * 	Pa1 = Pt,p,c1 and Pa2 = Pt,p,c2; also the probability of no attack, which is equal to 1-Pa1-Pa2.
         * 		  
         * This gives Blue the probability of each Red option (A, B, C).
         * Using these three probabilities, Blue can use the payoff matrix along with known values of probabilities (P1, P2) 
         * and utilities (U1, U2) to compute the expected utility for each of his four options: 
         * 	A = [d1,d2], B = [d1,~d2], C = [~d1,d2], and D = [~d1,~d2]. 
         * Given the resulting vector of expected utilities [UA, UB, UC, UD], the optimal Blue decision is to
         * always choose the option with the highest expected utility.   
         */        
        if (blueLocations != null && !blueLocations.isEmpty()
                && attackProbabilities != null && attackProbabilities.size() == blueLocations.size()) {
            //Generate all permutations of Blue actions and compute their expected utility based on the Red attack probabilities
            //With two locations, the Blue options are: A = [d1,d2], B = [d1,~d2], C = [~d1,d2], and D = [~d1,~d2]. 
            List<List<BlueAction>> blueActionOptions = enumerateBlueActionPermutations(blueLocations);           
            List<BlueActionsExpectedUtility> blueActionOptionsUtility = 
                    new ArrayList<BlueActionsExpectedUtility>(blueActionOptions.size());           
            Iterator<List<BlueAction>> blueOptionsIter = blueActionOptions.iterator();           
            while (blueOptionsIter.hasNext()) {                
                List<BlueAction> blueOption = blueOptionsIter.next();
                blueActionOptionsUtility.add(new BlueActionsExpectedUtility(blueOption,
                        computeBlueActionsExpectedUtility(blueOption, blueLocations, 
                                attackProbabilities)));                
            }
            //Return the Blue actions with the highest expected utility
            BlueActionsExpectedUtility bestBlueActionsUtility = null;
            for (BlueActionsExpectedUtility blueActionsUtility : blueActionOptionsUtility) {
                if (bestBlueActionsUtility == null
                        || blueActionsUtility.getExpectedUtility() > bestBlueActionsUtility.getExpectedUtility()) {
                    bestBlueActionsUtility = blueActionsUtility;
                }
            }
            return bestBlueActionsUtility;
        }
        return null;
    }

    /**
     * Computes the expected utility of taking the specified Blue actions at each Blue location.
     * See the Test Specification for details on this calculation.
     * 
     * @param blueActions the Blue actions
     * @param blueLocations the Blue locations
     * @param attackProbabilities the probability of Red attack at each location
     * @return the expected utility of the Blue actions
     */
    public Double computeBlueActionsExpectedUtility(List<BlueAction> blueActions,
            List<BlueLocation> blueLocations, List<Double> attackProbabilities) {            
        if (blueActions != null && !blueActions.isEmpty() 
                && blueLocations != null && blueLocations.size() == blueActions.size()
                && attackProbabilities != null && attackProbabilities.size() == blueActions.size()) {
            //Determine all of Red's possible options at each location
            List<List<RedAction>> possibleRedActions = enumerateRedActionPermutations(blueLocations);            
            //Given the Red attack probabilities at each location, compute the probability of each Red option
            List<Double> possibleRedActionProbs = new ArrayList<Double>(possibleRedActions.size());
            for(List<RedAction> redActions : possibleRedActions) {
                Double pActions = 1d;
                Iterator<RedAction> redActionIter = redActions.iterator();
                Iterator<Double> attackProbsIter = attackProbabilities.iterator();
                while(redActionIter.hasNext()) {
                   RedActionType redAction = redActionIter.next().getAction();
                   Double pAttack = attackProbsIter.next();                           
                   pActions *= redAction == RedActionType.Attack ? pAttack : 1 - pAttack;
                }
                possibleRedActionProbs.add(pActions);
            }
            possibleRedActionProbs = ProbabilityUtils.normalizeDecimalProbabilities(
                    possibleRedActionProbs, possibleRedActionProbs);
            //Compute Blue's expected utility
            return computeBlueActionsExpectedUtility(blueActions, blueLocations, 
                    possibleRedActions, possibleRedActionProbs);
        }
        return null;        
        /*if (blueActions != null && !blueActions.isEmpty() 
                && blueLocations != null && blueLocations.size() == blueActions.size()
                && attackProbabilities != null && attackProbabilities.size() == blueActions.size()) {
            Double utility = 0d;
            Iterator<BlueAction> actionIter = blueActions.iterator();
            Iterator<BlueLocation> locationIter = blueLocations.iterator();
            Iterator<Double> attackProbsIter = attackProbabilities.iterator();
            while (actionIter.hasNext()) {
                BlueActionType action = actionIter.next().getAction();
                BlueLocation location = locationIter.next();
                Double attackProb = attackProbsIter.next();
                if (action == BlueActionType.Divert) {
                    utility += attackProb - 1;
                } else {
                    Double P = location.getOsint() != null ? location.getOsint().getRedVulnerability_P() : 0;
                    Integer U = location.getImint() != null ? location.getImint().getRedOpportunity_U() : 0;
                    utility += attackProb * (U * (2 * P - 1));
                }
            }
            return utility;
        }
        return null;*/
    }
    
    /**
     * Computes the expected utility of taking the specified Blue actions at each Blue location given
     * Red's options at each location and the probability that Red selects each option.
     * See the Test Specification for details on this calculation.
     * 
     * @param blueActions the Blue actions
     * @param blueLocations the Blue locations
     * @param possibleRedActions the possible permutations of Red actions
     * @param possibleRedActionProbabilities the probability of Red selecting each action permutation
     * @return the expected utility of the Blue actions
     */
    public Double computeBlueActionsExpectedUtility(List<BlueAction> blueActions,
            List<BlueLocation> blueLocations, List<List<RedAction>> possibleRedActions, 
            List<Double> possibleRedActionProbabilities) {
        if (blueActions != null && !blueActions.isEmpty()
                && blueLocations != null && blueLocations.size() == blueActions.size()
                && possibleRedActions != null && !possibleRedActions.isEmpty()
                && possibleRedActionProbabilities != null
                && possibleRedActionProbabilities.size() == possibleRedActions.size()) {
            Iterator<List<RedAction>> redActionsIter = possibleRedActions.iterator();
            Iterator<Double> redActionsProbIter = possibleRedActionProbabilities.iterator();
            double blueExpectedUtility = 0d;
            while (redActionsIter.hasNext()) {
                //Compute the expected utility to Blue if Red chooses this option
                List<RedAction> redActions = redActionsIter.next();                
                Iterator<RedAction> redActionIter = redActions.iterator();
                Iterator<BlueAction> blueActionIter = blueActions.iterator();
                Iterator<BlueLocation> locationIter = blueLocations.iterator();                               
                double bluePayoff = 0d;                
                while (blueActionIter.hasNext()) {
                    BlueActionType blueAction = blueActionIter.next().getAction();
                    RedActionType redAction = redActionIter.next().getAction();
                    BlueLocation location = locationIter.next();
                    if (blueAction == BlueActionType.Divert) {
                        if(redAction == RedActionType.Attack) {
                            //Blue diverts, Red attacks, Blue payoff = 0
                            bluePayoff += 0;
                        } else {
                            //Blue diverts, Red does not attack, Blue payoff = -1
                            bluePayoff += -1;
                        }
                    } else {
                        if(redAction == RedActionType.Attack) {
                            //Blue does not divert, Red attacks, showdown
                            //Blue payoff in a showdown is an expected payoff computed as U * (2 * P - 1)
                            Double P = location.getOsint() != null ? location.getOsint().getRedVulnerability_P() : 0;
                            Integer U = location.getImint() != null ? location.getImint().getRedOpportunity_U() : 0;
                            bluePayoff += U * (2 * P - 1);                            
                        } else {
                            //Blue does not divert, Red does not attack, Blue payoff = 0
                            bluePayoff += 0;
                        }
                    }
                }
                //Update Blue's expected utility given the probability that Red selects the current option
                Double prob = redActionsProbIter.next();
                blueExpectedUtility += prob * bluePayoff;
            }
            return blueExpectedUtility;
        }
        return null;
    }

    /**
     * Enumerates all permutations of Blue actions when there are 1 or 2 locations.
     * For one location, A = [d1], B = [~d1]
     * For two locations, A = [d1,d2], B = [d1,~d2], C = [~d1,d2], and D = [~d1,~d2]
     * 
     * @param blueLocations the locations
     * @return the possible permutations of Blue actions
     */
    public static List<List<BlueAction>> enumerateBlueActionPermutations(
            List<BlueLocation> blueLocations) {        
        return enumerateBlueActionPermutations_locationIds(extractLocationIds(blueLocations));
    }
    
    /**
     * Enumerates all permutations of Blue actions when there are 1 or 2 locations.
     * For one location, A = [d1], B = [~d1]
     * For two locations, A = [d1,d2], B = [d1,~d2], C = [~d1,d2], and D = [~d1,~d2]
     * 
     * @param blueLocations the location IDs
     * @return the possible permutations of Blue actions
     */
    public static List<List<BlueAction>> enumerateBlueActionPermutations_locationIds(
            List<String> blueLocations) {  
        if(blueLocations != null && !blueLocations.isEmpty()) {
            if(blueLocations.size() == 1) {
                //One location, A = [d1], B = [~d1]
                String loc = blueLocations.get(0);
                List<List<BlueAction>> actions = new ArrayList<List<BlueAction>>(2);
                actions.add( //[d1]
                        Arrays.asList(new BlueAction(loc, 0, BlueActionType.Divert)));
                actions.add( //[~d1]
                        Arrays.asList(new BlueAction(loc, 0, BlueActionType.Do_Not_Divert)));
                return actions;
            } else if(blueLocations.size() == 2) {
                //Two locations, A = [d1,d2], B = [d1,~d2], C = [~d1,d2], and D = [~d1,~d2]
                String loc1 = blueLocations.get(0);
                String loc2 = blueLocations.get(1);
                List<List<BlueAction>> actions = new ArrayList<List<BlueAction>>(4);                
                actions.add(Arrays.asList( //[d1, d2]
                        new BlueAction(loc1, 0, BlueActionType.Divert),
                        new BlueAction(loc2, 1, BlueActionType.Divert)));                
                actions.add(Arrays.asList( //[d1, ~d2]
                        new BlueAction(loc1, 0, BlueActionType.Divert),
                        new BlueAction(loc2, 1, BlueActionType.Do_Not_Divert)));
                actions.add(Arrays.asList( //[~d1, d2]
                        new BlueAction(loc1, 0, BlueActionType.Do_Not_Divert),
                        new BlueAction(loc2, 1, BlueActionType.Divert)));
                actions.add(Arrays.asList( //[~d1, ~d2]
                        new BlueAction(loc1, 0, BlueActionType.Do_Not_Divert),
                        new BlueAction(loc2, 1, BlueActionType.Do_Not_Divert)));
                return actions;
            } else {
                throw new IllegalArgumentException("Error, currently enumerates Blue"
                        + " actions for 1 or 2 locations per trial");
            }
        } else {
            return null;
        }
    }
    
    /**
     * Returns a list of the ID of each Blue location.
     * 
     * @param blueLocations the Blue locations
     * @return a list containing the ID of each location
     */
    public static List<String> extractLocationIds(List<BlueLocation> blueLocations) {
        if(blueLocations != null && !blueLocations.isEmpty()) {
            List<String> locationIds = new ArrayList<String>(blueLocations.size());
            for(BlueLocation location : blueLocations) {
                locationIds.add(location.getId());
            }
            return locationIds;
        }
        return null;
    }
    
    /**
     * Get the 0-based index of the given Blue action permutation out of all possible
     * Blue action permutations.
     * 
     * @param blueActions the Blue action permutation
     * @return the index of the permutation
     */
    public static Integer getBlueActionPermutationIndex_BlueAction(List<BlueAction> blueActions) {
        return getBlueActionPermutationIndex_BlueActionType(extractBlueActionTypes(blueActions));
    }
    
    /**
     * Get the 0-based index of the given Blue action permutation out of all possible
     * Blue action permutations.
     * 
     * @param blueActions the Blue action permutation
     * @return the index of the permutation
     */
    public static Integer getBlueActionPermutationIndex_BlueActionType(List<BlueActionType> blueActions) {
        if (blueActions != null && !blueActions.isEmpty()) {
            if (blueActions.size() == 1) {
                BlueActionType action = blueActions.get(0);
                return action == BlueActionType.Divert ? 0 : 1;
            } else if (blueActions.size() == 2) {
                BlueActionType action1 = blueActions.get(0);
                BlueActionType action2 = blueActions.get(1);
                if(action1 == BlueActionType.Divert) {
                    return action2 == BlueActionType.Divert ? 0 : 1;
                } else {
                    return action2 == BlueActionType.Divert ? 2 : 3;
                }
            } else {
                throw new IllegalArgumentException("Error, currently one supports 1 or 2"
                        + " Blue locations per trial");
            }
        }
        return null;
    }
    
    /**
     * Gets the Blue action taken at each Blue location.
     * 
     * @param blueActions the Blue actions
     * @return a list of BlueActionType values 
     */
    public static List<BlueActionType> extractBlueActionTypes(List<BlueAction> blueActions) {
        if(blueActions != null && !blueActions.isEmpty()) {
            List<BlueActionType> blueActionTypes = new ArrayList<BlueActionType>(blueActions.size());
            for(BlueAction blueAction : blueActions) {
                blueActionTypes.add(blueAction.getAction());
            }
            return blueActionTypes;
        }
        return null;
    }
    
    /**
     * Enumerates all permutations of Red actions when there are 1 or 2 locations.
     * For one location, A = [a1], B = [~a1]
     * For two locations, A = [a1,~a2], B = [~a1,a2], and C = [~a1,~a2]
     * 
     * @param blueLocations the locations
     * @return the possible permutations of Red actions
     */
    public static List<List<RedAction>> enumerateRedActionPermutations(
        List<BlueLocation> blueLocations) {
        if(blueLocations != null && !blueLocations.isEmpty()) {
            if(blueLocations.size() == 1) {
                //One location, A = [a1], B = [~a1]
                BlueLocation loc = blueLocations.get(0);
                List<List<RedAction>> actions = new ArrayList<List<RedAction>>(2);
                actions.add( //[a1]
                        Arrays.asList(new RedAction(loc.getId(), 0, RedActionType.Attack)));
                actions.add( //[~a1]
                        Arrays.asList(new RedAction(loc.getId(), 0, RedActionType.Do_Not_Attack)));
                return actions;
            } else if(blueLocations.size() == 2) {
                //Two locations, A = [a1,~a2], B = [~a1,a2], and C = [~a1,~a2].
                BlueLocation loc1 = blueLocations.get(0);
                BlueLocation loc2 = blueLocations.get(1);
                List<List<RedAction>> actions = new ArrayList<List<RedAction>>(3);                
                actions.add(Arrays.asList( //[a1, ~a2]
                        new RedAction(loc1.getId(), 0, RedActionType.Attack),
                        new RedAction(loc2.getId(), 1, RedActionType.Do_Not_Attack)));                
                actions.add(Arrays.asList( //[~a1, a2]
                        new RedAction(loc1.getId(), 0, RedActionType.Do_Not_Attack),
                        new RedAction(loc2.getId(), 1, RedActionType.Attack)));
                actions.add(Arrays.asList( //[~a1, ~a2]
                        new RedAction(loc1.getId(), 0, RedActionType.Do_Not_Attack),
                        new RedAction(loc2.getId(), 1, RedActionType.Do_Not_Attack)));                
                return actions;
            } else {
                throw new IllegalArgumentException("Error, currently enumerates Red actions "
                        + "for 1 or 2 locations per trial");
            }
        } else {
            return null;
        }        
    }

    /**
     * Compute the probability that Red has the capability to attack (HUMINT).
     * Approximates the Poisson by using the following numbers: 1 trial after
     * attack: P=0.4; 2 trials after attack: P=0.7; 3 trials after attack:
     * P=0.9; 4 or more trials after attack: P=1.0;
     *
     * Note: Probabilities are returned in decimal and not percent format.
     *
     * @param numTrialsSinceLastAttack the number of trials since the last Red attack
     * @return the probability in decimal format
     */
    public static Double computeRedCapability_Pc(int numTrialsSinceLastAttack) {
        switch (numTrialsSinceLastAttack) {
            case 1:
                return 0.4;
            case 2:
                return 0.7;
            case 3:
                return 0.9;
            default:
                return 1.0;
        }
    }

    /**
     * Given an exam and a mission with responses from a participant (human or
     * model), adds responses to each trial in the mission to responses in the
     * corresponding trial in the corresponding mission in the exam.
     *
     * @param exam the exam
     * @param missionWithResponses a mission containing responses to one or more
     * trials
     * @return the mission from the exam with responses in the
     * missionWithResponses object
     * @throws Exception
     */
    public static Mission<?> addParticipantResponsesToExamMission(IcarusExam_Phase2 exam, Mission<?> missionWithResponses) throws Exception {
        Mission<?> examMission = null;
        for (Mission<?> m : exam.getMissions()) {
            if ((m.getId() != null && m.getId().equals(missionWithResponses.getId())
                    || (m.getName() != null && m.getName().equals(missionWithResponses.getName())))) {
                examMission = m;
            }
        }
        if (examMission != null && examMission.getClass().equals(missionWithResponses.getClass())) {
            examMission.setStartTime(missionWithResponses.getStartTime());
            examMission.setEndTime(missionWithResponses.getEndTime());
            examMission.setResponseGenerator(missionWithResponses.getResponseGenerator());
            if (missionWithResponses instanceof Mission_1_2_3) {
                List<Mission_1_2_3_Trial> participantTrials = ((Mission_1_2_3) missionWithResponses).getTestTrials();
                List<Mission_1_2_3_Trial> examTrials = ((Mission_1_2_3) examMission).getTestTrials();
                int numTrials = Math.min(participantTrials.size(), examTrials.size());
                for (int i = 0; i < numTrials; i++) {
                    examTrials.get(i).copyResponseData(participantTrials.get(i));
                }
            } else if (missionWithResponses instanceof Mission_4_5_6) {
                List<Mission_4_5_6_Trial> participantTrials = ((Mission_4_5_6) missionWithResponses).getTestTrials();
                List<Mission_4_5_6_Trial> examTrials = ((Mission_4_5_6) examMission).getTestTrials();
                int numTrials = Math.min(participantTrials.size(), examTrials.size());
                for (int i = 0; i < numTrials; i++) {
                    examTrials.get(i).copyResponseData(participantTrials.get(i));
                }
            }
        } else {
            throw new IllegalArgumentException("Error: Mission " + missionWithResponses.getName() + " not found in exam.");
        }
        return examMission;
    }
    
    public static void main(String[] args) {
        String fileName = "data/Phase_2_CPD/exams/HRL/Final_Response.xml";        
        IcarusExam_Phase2 exam = null;
        URL examFileURL = null;
        try {
            examFileURL = CPSFileUtils.createFile(fileName).toURI().toURL();
            exam = ParsingExample.loadExamAndFeatureVectors(examFileURL);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        ScoreComputer_Phase2 sc = new ScoreComputer_Phase2();
        for (int mission = 0; mission < 5; mission++) {
            List<TrialFeedback_Phase2> tf = sc.computeScoreForMission(exam.getMissions().get(mission),
                    exam.getPayoffMatrix(), true, null);
            System.out.println("------ Mission " + (mission + 1) + " ---------------");
            int i = 1;
            for (TrialFeedback_Phase2 feedback : tf) {
                System.out.println("Trial " + i + ": ");
                System.out.println(feedbackToString(feedback));
                i++;
            }
        }
    }
    
    /**
     * Turn a feedback object into a string to display.
     *
     * @param feedback The feedback object
     * @return the string form
     */
    public static String feedbackToString(TrialFeedback_Phase2 feedback) {
        try {
            return IcarusExamLoader_Phase2.marshalTrialFeedback(feedback);
        } catch (JAXBException e) {
            return "An error occurred: " + e;
        }
    }

    /**
     * Contains Red attack probabilities at a location, including P(Propensity),
     * P(Propensity, Capability), P(Activity), and P(Activity, Propensity,
     * Capability).
     *
     * @author CBONACETO
     *
     */
    public static class RedAttackProbabilities {

        /**
         * Propensity of Red to attack at a location given Red vulnerability (P)
         * and opportunity (U)
         */
        public Double pPropensity;

        /**
         * Probability of Red attack at a location given Red propensity to
         * attack and Red capability (from HUMINT)
         */
        public Double pPropensityCapability;

        /**
         * Probability of Red attack at a location given Red activity (from
         * SIGINT)
         */
        public Double pActivity;

        /**
         * Probability of Red attack at a location given Red activity,
         * propensity, and capability
         */
        public Double pActivityPropensityCapability;

        public Double getpPropensity() {
            return pPropensity;
        }

        public void setpPropensity(Double pPropensity) {
            this.pPropensity = pPropensity;
        }

        public Double getpPropensityCapability() {
            return pPropensityCapability;
        }

        public void setpPropensityCapability(Double pPropensityCapability) {
            this.pPropensityCapability = pPropensityCapability;
        }

        public Double getpActivity() {
            return pActivity;
        }

        public void setpActivity(Double pActivity) {
            this.pActivity = pActivity;
        }

        public Double getpActivityPropensityCapability() {
            return pActivityPropensityCapability;
        }

        public void setpActivityPropensityCapability(
                Double pActivityPropensityCapability) {
            this.pActivityPropensityCapability = pActivityPropensityCapability;
        }
    }
    
    /**
     * Contains cumulative Bayesian and incremental Bayesian Red attack probabilities. 
     *
     * @author CBONACETO
     *
     */
    public static class CumulativeAndIncrementalRedAttackProbabilities {
        
        /** Cumulative Bayesian Red attack probabilities */
        protected List<RedAttackProbabilities> cumulativeProbs;
        
        /** Incremental Bayesian Red attack probabilities */
        protected List<RedAttackProbabilities> incrementalProbs;
        
        public CumulativeAndIncrementalRedAttackProbabilities() {}
        
        public CumulativeAndIncrementalRedAttackProbabilities(
            List<RedAttackProbabilities> cumulativeProbs, 
            List<RedAttackProbabilities> incrementalProbs) {
            this.cumulativeProbs = cumulativeProbs;
            this.incrementalProbs = incrementalProbs;
        }

        public List<RedAttackProbabilities> getCumulativeProbs() {
            return cumulativeProbs;
        }

        public void setCumulativeProbs(List<RedAttackProbabilities> cumulativeProbs) {
            this.cumulativeProbs = cumulativeProbs;
        }

        public List<RedAttackProbabilities> getIncrementalProbs() {
            return incrementalProbs;
        }

        public void setIncrementalProbs(List<RedAttackProbabilities> incrementalProbs) {
            this.incrementalProbs = incrementalProbs;
        }        
    }

    /**
     * Contains the Red and Blue points gained (or lost) for a trial or at a
     * location.
     *
     */
    public static class RedBluePayoff {

        protected Double redPoints;

        protected Double bluePoints;

        protected boolean showDown;

        protected PlayerType winner;

        public RedBluePayoff() {
        }

        public RedBluePayoff(Double redPoints, Double bluePoints, boolean showDown,
                PlayerType winner) {
            this.redPoints = redPoints;
            this.bluePoints = bluePoints;
            this.showDown = showDown;
            this.winner = winner;
        }

        public Double getRedPoints() {
            return redPoints;
        }

        public void setRedPoints(Double redPoints) {
            this.redPoints = redPoints;
        }

        public Double getBluePoints() {
            return bluePoints;
        }

        public void setBluePoints(Double bluePoints) {
            this.bluePoints = bluePoints;
        }

        public boolean isShowDown() {
            return showDown;
        }

        public void setShowDown(boolean showDown) {
            this.showDown = showDown;
        }

        public PlayerType getWinner() {
            return winner;
        }

        public void setWinner(PlayerType winner) {
            this.winner = winner;
        }
    }

    /**
     * Contains the Red/Blue payoff at each location, the Blue points gained,
     * the Red points gained, and the new Blue and Red total scores after a
     * trial.
     *
     */
    public static class RedBluePayoffAtEachLocation {

        protected List<RedBluePayoff> payoffAtLocations;

        protected ShowdownWinner showdownWinner;

        protected Double redPointsGained;

        protected Double bluePointsGained;

        protected Double redScore;

        protected Double blueScore;

        public RedBluePayoffAtEachLocation() {
        }

        public RedBluePayoffAtEachLocation(List<RedBluePayoff> payoffAtLocations,
                ShowdownWinner showdownWinner, Double redPointsGained,
                Double bluePointsGained, Double redScore, Double blueScore) {
            this.payoffAtLocations = payoffAtLocations;
            this.showdownWinner = showdownWinner;
            this.redPointsGained = redPointsGained;
            this.bluePointsGained = bluePointsGained;
            this.redScore = redScore;
            this.blueScore = blueScore;
        }

        public List<RedBluePayoff> getPayoffAtLocations() {
            return payoffAtLocations;
        }

        public void setPayoffAtLocations(List<RedBluePayoff> payoffAtLocations) {
            this.payoffAtLocations = payoffAtLocations;
        }

        public ShowdownWinner getShowdownWinner() {
            return showdownWinner;
        }

        public void setShowdownWinner(ShowdownWinner showdownWinner) {
            this.showdownWinner = showdownWinner;
        }

        public Double getRedPointsGained() {
            return redPointsGained;
        }

        public void setRedPointsGained(Double redPointsGained) {
            this.redPointsGained = redPointsGained;
        }

        public Double getBluePointsGained() {
            return bluePointsGained;
        }

        public void setBluePointsGained(Double bluePointsGained) {
            this.bluePointsGained = bluePointsGained;
        }

        public Double getRedScore() {
            return redScore;
        }

        public void setRedScore(Double redScore) {
            this.redScore = redScore;
        }

        public Double getBlueScore() {
            return blueScore;
        }

        public void setBlueScore(Double blueScore) {
            this.blueScore = blueScore;
        }
    }
}