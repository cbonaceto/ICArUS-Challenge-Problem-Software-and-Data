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
package org.mitre.icarus.cps.assessment.assessment_processor.phase_2;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import org.mitre.icarus.cps.assessment.assessment_metrics_computer.phase_2.TrialMetricsComputer;
import org.mitre.icarus.cps.assessment.assessment_processor.ITrialResponseProcessor;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.MetricsInfo;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.SubjectSigintProbabilities;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.TrialData;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.trial_part_data.AttackProbabilityReportData;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.trial_part_data.BlueActionSelectionData;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.trial_part_data.RedTacticsReportData;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.trial_part_data.SigintSelectionData;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.trial_part_data.TrialPartDataFactory;
import org.mitre.icarus.cps.assessment.score_computer.phase_2.ScoreComputer_Phase2;
import org.mitre.icarus.cps.exam.base.response.ResponseGeneratorData;
import org.mitre.icarus.cps.exam.phase_2.IcarusExam_Phase2;
import org.mitre.icarus.cps.exam.phase_2.feedback.TrialFeedback_Phase2;
import org.mitre.icarus.cps.exam.phase_2.testing.IcarusTestTrial_Phase2;
import org.mitre.icarus.cps.exam.phase_2.testing.Mission;
import org.mitre.icarus.cps.exam.phase_2.testing.Mission_1_2_3;
import org.mitre.icarus.cps.exam.phase_2.testing.Mission_1_2_3_Trial;
import org.mitre.icarus.cps.exam.phase_2.testing.Mission_4_5_6_Trial;
import org.mitre.icarus.cps.exam.phase_2.testing.bluebook.BlueBook;
import org.mitre.icarus.cps.exam.phase_2.testing.bluebook.RedTacticType;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbeType;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.red_tactics_probes.AbstractRedTacticsProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.red_tactics_probes.MostLikelyRedTacticProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.red_tactics_probes.RedTacticsChangesProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.red_tactics_probes.RedTacticsProbabilityReportProbe;
import org.mitre.icarus.cps.feature_vector.phase_2.BlueLocation;
import org.mitre.icarus.cps.feature_vector.phase_2.int_datum.SigintReliability;
import org.mitre.icarus.cps.test_harness.TestHarnessScoreComputer_Phase2;

/**
 * Trial response processor for Phase 2 trials. Extracts data from the trial
 * response into a TrialData object.
 * 
 * @author CBONACETO
 *
 */
public class TrialResponseProcessor implements ITrialResponseProcessor<IcarusTestTrial_Phase2, Mission<?>, IcarusExam_Phase2, TrialFeedback_Phase2, TrialData> {

    /**
     * The trial metrics computer
     */
    protected TrialMetricsComputer trialMetricsComputer;

    /**
     * The score computer
     */
    protected ScoreComputer_Phase2 scoreComputer;

    /**
     * When Red is playing with multiple possible tactics, whether to compute
     * the probability that Red is playing with each tactic
     */
    protected boolean computeRedTacticProbs = true;

    /**
     * The BLUEBOOK to use when determining possible Red tactic types for a
     * mission and computing cumulative Bayesian attack probabilities
     */
    protected BlueBook blueBook;

    /**
     * The SIGINT reliabilities table to use when computing normative solutions
     * (using cumulative Bayesian attack probabilities)
     */
    protected SigintReliability sigintReliability;
    
    /**
     * The values to use for the participant assessment of P(Attack|SIGINT) when
     * computing incremental Bayesian attack probabilities on missions for which the 
     * participant was not asked P(Attack|SIGINT) (Missions 2 & 3)
     */
    protected SubjectSigintProbabilities subjectSigintProbs;

    public TrialResponseProcessor() {
        this(null, null);
    }
    
    public TrialResponseProcessor(TrialMetricsComputer trialMetricsComputer,
            ScoreComputer_Phase2 scoreComputer) {
        this.trialMetricsComputer = trialMetricsComputer == null
                ? new TrialMetricsComputer() : trialMetricsComputer;
        this.scoreComputer = scoreComputer == null ? new ScoreComputer_Phase2()
                : scoreComputer;
        blueBook = BlueBook.createDefaultBlueBook();
        sigintReliability = SigintReliability.createDefaultSigintReliability();
    }

    public boolean isComputeRedTacticProbs() {
        return computeRedTacticProbs;
    }

    public void setComputeRedTacticProbs(boolean computeRedTacticProbs) {
        this.computeRedTacticProbs = computeRedTacticProbs;
    }

    public BlueBook getBlueBook() {
        return blueBook;
    }

    public void setBlueBook(BlueBook blueBook) {
        this.blueBook = blueBook;
    }

    public SigintReliability getSigintReliability() {
        return sigintReliability;
    }

    public void setSigintReliability(SigintReliability sigintReliability) {
        this.sigintReliability = sigintReliability;
    }

    public SubjectSigintProbabilities getSubjectSigintProbs() {
        return subjectSigintProbs;
    }

    public void setSubjectSigintProbs(SubjectSigintProbabilities subjectSigintProbs) {
        this.subjectSigintProbs = subjectSigintProbs;
    }    

    /* (non-Javadoc)
     * @see org.mitre.icarus.cps.assessment.assessment_processor.ITrialResponseProcessor#updateTrialData(org.mitre.icarus.cps.exam.base.IcarusExam, org.mitre.icarus.cps.exam.base.testing.IcarusTestPhase, org.mitre.icarus.cps.exam.base.testing.IcarusTestTrial, org.mitre.icarus.cps.exam.base.feedback.TestTrialFeedback, boolean, org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.AbstractTrialData, java.lang.String, java.lang.String, java.lang.Integer, org.mitre.icarus.cps.exam.base.response.ResponseGeneratorData)
     */
    @Override
    public TrialData updateTrialData(IcarusExam_Phase2 exam, Mission<?> mission,
            IcarusTestTrial_Phase2 trial, TrialFeedback_Phase2 trialFeedback,
            boolean scoreTrialIfFeedbackMissingOrIncomplete,
            TrialData trialData, String exam_id, String task_id,
            Integer task_number, ResponseGeneratorData responseGenerator) {
        if (trialData == null) {
            trialData = new TrialData();
        }
        trialData.setData_stale(true);

        if (responseGenerator != null) {
            trialData.setSite_id(responseGenerator.getSiteId());
            trialData.setResponse_generator_id(responseGenerator.getResponseGeneratorId());
            trialData.setHuman(responseGenerator.isHumanSubject());
        }
        trialData.setTrial(trial);
        trialData.setExam_id(exam_id);
        trialData.setTask_id(task_id);
        trialData.setTask_number(task_number);

        if (scoreTrialIfFeedbackMissingOrIncomplete
                && (trialFeedback == null || trialFeedback.isResponseWellFormed() == null
                || !trialFeedback.isResponseWellFormed())
                && trial != null && trial.isResponsePresent()) {
            //Attempt to score or re-score the trial if the feedback is null or the trial is marked as incomplete
            try {
                trialFeedback = TestHarnessScoreComputer_Phase2.scoreTrial(mission, trial, exam.getPayoffMatrix());
                if (trialFeedback != null) {
                    trial.setResponseFeedBack(trialFeedback);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (trialFeedback != null) {
            trialData.setTrial_complete(trialFeedback.isResponseWellFormed());
            trialData.setTrial_valid(trialFeedback.isResponseWellFormed());
            trialData.setErrors(trialFeedback.getErrors());
            trialData.setWarnings(trialFeedback.getWarnings());
        } else {
            trialData.setTrial_complete(null);
            trialData.setTrial_valid(null);
            trialData.setErrors(null);
        }

        if (trial != null) {
            //Compute normative responses for the trial (attack probabilities, SIGINT selections, Blue action selections)
            scoreComputer.computeNormativeDataForTrial(mission, trial, sigintReliability, 
                    subjectSigintProbs, blueBook, computeRedTacticProbs, true, true, null);            
            
            //Extract the trial data
            Integer trialNum = trial.getTrialNum();
            trialData.setTrial_number(trialNum);
            trialData.setTrial_time(trial.getTrialTime_ms() != null ? trial.getTrialTime_ms().doubleValue() : null);
            trialData.setTime_stamp(trial.getEndTime() != null ? trial.getEndTime().getTime() : null);
            SortedSet<TrialPartProbeType> expectedProbes = mission.getMissionType().getProbes();

            //Extract the HUMINT data
            trialData.setRedCapability_Pc(trial.getHumint() != null ? trial.getHumint().getRedCapability_Pc() : null);

            //Extract the INT data (OSINT, IMINT, and SIGINT) for each location            
            trialData.setRedVulnerability_P(new ArrayList<Double>());
            trialData.setRedOpportunity_U(new ArrayList<Integer>());
            trialData.setRedActivityDetected(new ArrayList<Boolean>());
            if (trial.getBlueLocations() != null && !trial.getBlueLocations().isEmpty()) {                
                trialData.setNumBlueLocations(trial.getBlueLocations().size());           
                trialData.setBlueLocationIds(new ArrayList<String>(trialData.getNumBlueLocations()));
                for (BlueLocation location : trial.getBlueLocations()) {
                    trialData.getBlueLocationIds().add(location.getId());
                    if (location.getOsint() != null) {
                        trialData.getRedVulnerability_P().add(location.getOsint().getRedVulnerability_P());
                    } else {
                        trialData.getRedVulnerability_P().add(null);
                    }
                    if (location.getImint() != null) {
                        trialData.getRedOpportunity_U().add(location.getImint().getRedOpportunity_U());
                    } else {
                        trialData.getRedOpportunity_U().add(null);
                    }
                    if (location.getSigint() != null) {
                        trialData.getRedActivityDetected().add(location.getSigint().isRedActivityDetected());
                    } else {
                        trialData.getRedActivityDetected().add(null);
                    }
                }
            } else {                
                trialData.setNumBlueLocations(mission.getMissionType().getLocationsPerTrial());
                trialData.setBlueLocationIds(new ArrayList<String>(trialData.getNumBlueLocations()));
                for (int i = 0; i < trialData.getNumBlueLocations(); i++) {
                    trialData.getBlueLocationIds().add(trial.getTrialNum() + "-" + Integer.toString(i));
                    trialData.getRedVulnerability_P().add(null);
                    trialData.getRedOpportunity_U().add(null);
                    trialData.getRedActivityDetected().add(null);
                }
            }

            //Extract the Red tactics probe data			
            AbstractRedTacticsProbe redTacticsProbe = null;
            RedTacticType redTactic = null;
            if (trial instanceof Mission_1_2_3_Trial) {
                redTacticsProbe = ((Mission_1_2_3_Trial) trial).getMostLikelyRedTacticProbe();
                redTactic = ((Mission_1_2_3) mission).getRedTactic();
            } else if (trial instanceof Mission_4_5_6_Trial) {
                redTacticsProbe = ((Mission_4_5_6_Trial) trial).getRedTacticsProbe();
                redTactic = ((Mission_4_5_6_Trial) trial).getRedTactic();
            }
            if (redTacticsProbe != null) {
                trialData.setRedTacticsReport(new RedTacticsReportData());
                trialData.getRedTacticsReport().setTrialPartProbe(redTacticsProbe);
                if (trialData.getRedTacticsReport().getBatchPlotCreated() == null
                        && expectedProbes.contains(TrialPartProbeType.BatchPlotProbe)) {
                    trialData.getRedTacticsReport().setBatchPlotCreated(false);
                    trialData.getRedTacticsReport().setBatchPlotNumTrialsReviewed(0);
                    trialData.getRedTacticsReport().setBatchPlotNumTrialsToReview(0);
                }
                if (trialData.getRedTacticsReport().getTrialPartType() == null) {
                    if (redTacticsProbe instanceof MostLikelyRedTacticProbe) {
                        trialData.getRedTacticsReport().setTrialPartType(
                                TrialPartProbeType.MostLikelyRedTacticSelection);
                    } else if (redTacticsProbe instanceof RedTacticsProbabilityReportProbe) {
                        trialData.getRedTacticsReport().setTrialPartType(
                                TrialPartProbeType.RedTacticsProbabilityReport);
                    } else if (redTacticsProbe instanceof RedTacticsChangesProbe) {
                        trialData.getRedTacticsReport().setTrialPartType(
                                TrialPartProbeType.RedTacticsChangesReport);
                    }
                }
            } else if (expectedProbes.contains(TrialPartProbeType.MostLikelyRedTacticSelection)) {
                trialData.setRedTacticsReport((RedTacticsReportData) TrialPartDataFactory.createTrialPartData(
                        TrialPartProbeType.MostLikelyRedTacticSelection,
                        mission.getMissionType(), trialNum));
            } else if (expectedProbes.contains(TrialPartProbeType.RedTacticsProbabilityReport)) {
                trialData.setRedTacticsReport((RedTacticsReportData) TrialPartDataFactory.createTrialPartData(
                        TrialPartProbeType.RedTacticsProbabilityReport,
                        mission.getMissionType(), trialNum));
            } else if (expectedProbes.contains(TrialPartProbeType.RedTacticsChangesReport)) {
                trialData.setRedTacticsReport((RedTacticsReportData) TrialPartDataFactory.createTrialPartData(
                        TrialPartProbeType.RedTacticsChangesReport,
                        mission.getMissionType(), trialNum));
            } else {
                trialData.setRedTacticsReport(null);
            }
            if (trialData.getRedTacticsReport() != null) {
                trialData.getRedTacticsReport().setActualRedTactic(redTactic);
            }

            //Extract the BLUEBOOK Red attack probabilities data			
            trialData.setBlueBookData(mission.getMissionType(), blueBook,
                    trialData.getNumBlueLocations(),
                    trialData.getRedVulnerability_P(),
                    trialData.getRedOpportunity_U());

            //Extract the attack probability report(s) probe data			
            List<AttackProbabilityReportData> attackProbabilityReports
                    = new ArrayList<AttackProbabilityReportData>();
            if (trial.getAttackPropensityProbe_Pp() != null) {
                AttackProbabilityReportData probe
                        = new AttackProbabilityReportData(trial.getAttackPropensityProbe_Pp());
                if (probe.getTrialPartType() == null) {
                    probe.setTrialPartType(TrialPartProbeType.AttackProbabilityReport_Pp);
                }
                attackProbabilityReports.add(probe);
            } else if (expectedProbes.contains(TrialPartProbeType.AttackProbabilityReport_Pp)) {
                //Add null data if there was supposed to be a Pp probe
                AttackProbabilityReportData probe = (AttackProbabilityReportData) TrialPartDataFactory.createTrialPartData(
                        TrialPartProbeType.AttackProbabilityReport_Pp,
                        mission.getMissionType(), trialNum);
                attackProbabilityReports.add(probe);
            }
            if (trial.getAttackProbabilityProbe_Ppc() != null) {
                AttackProbabilityReportData probe = new AttackProbabilityReportData(
                        trial.getAttackProbabilityProbe_Ppc());
                if (probe.getTrialPartType() == null) {
                    probe.setTrialPartType(TrialPartProbeType.AttackProbabilityReport_Ppc);
                }
                attackProbabilityReports.add(probe);
            } else if (expectedProbes.contains(TrialPartProbeType.AttackProbabilityReport_Ppc)) {
                //Add null data if there was supposed to be a Ppc probe
                attackProbabilityReports.add((AttackProbabilityReportData) TrialPartDataFactory.createTrialPartData(
                        TrialPartProbeType.AttackProbabilityReport_Ppc,
                        mission.getMissionType(), trialNum));
            }
            if (trial.getAttackProbabilityProbe_Pt() != null) {
                AttackProbabilityReportData probe = new AttackProbabilityReportData(
                        trial.getAttackProbabilityProbe_Pt());
                if (probe.getTrialPartType() == null) {
                    probe.setTrialPartType(TrialPartProbeType.AttackProbabilityReport_Pt);
                }
                attackProbabilityReports.add(probe);
            } else if (expectedProbes.contains(TrialPartProbeType.AttackProbabilityReport_Pt)) {
                //Add null data if there was supposed to be a Pt probe
                attackProbabilityReports.add((AttackProbabilityReportData) TrialPartDataFactory.createTrialPartData(
                        TrialPartProbeType.AttackProbabilityReport_Pt,
                        mission.getMissionType(), trialNum));
            }
            if (trial.getAttackProbabilityProbe_Ptpc() != null) {
                AttackProbabilityReportData probe
                        = new AttackProbabilityReportData(trial.getAttackProbabilityProbe_Ptpc());
                if (probe.getTrialPartType() == null) {
                    probe.setTrialPartType(TrialPartProbeType.AttackProbabilityReport_Ptpc);
                }
                attackProbabilityReports.add(probe);
            } else if (expectedProbes.contains(TrialPartProbeType.AttackProbabilityReport_Ptpc)) {
                //Add null data if there was supposed to be a Ptpc probe
                attackProbabilityReports.add((AttackProbabilityReportData) TrialPartDataFactory.createTrialPartData(
                        TrialPartProbeType.AttackProbabilityReport_Ptpc,
                        mission.getMissionType(), trialNum));
            }
            if (!attackProbabilityReports.isEmpty()) {
                trialData.setAttackProbabilityReports(attackProbabilityReports);
            } else {
                trialData.setAttackProbabilityReports(null);
            }

            //Extract the SIGINT selection probe data
            if (trial.getSigintSelectionProbe() != null) {
                trialData.setSigintSelection(new SigintSelectionData(
                        trial.getSigintSelectionProbe()));
                if (trialData.getSigintSelection().getTrialPartType() == null) {
                    trialData.getSigintSelection().setTrialPartType(
                            TrialPartProbeType.SigintSelection);
                }
            } else if (expectedProbes.contains(TrialPartProbeType.SigintSelection)) {
                trialData.setSigintSelection((SigintSelectionData) TrialPartDataFactory.createTrialPartData(
                        TrialPartProbeType.SigintSelection,
                        mission.getMissionType(), trialNum));
            } else {
                trialData.setSigintSelection(null);
            }

            //Extract the Blue action selection probe data
            if (trial.getBlueActionSelection() != null) {
                trialData.setBlueActionSelection(new BlueActionSelectionData(
                        trial.getBlueActionSelection()));
                if (trialData.getBlueActionSelection().getTrialPartType() == null) {
                    trialData.getBlueActionSelection().setTrialPartType(
                            TrialPartProbeType.BlueActionSelection);
                }
            } else if (expectedProbes.contains(TrialPartProbeType.BlueActionSelection)) {
                trialData.setBlueActionSelection((BlueActionSelectionData) TrialPartDataFactory.createTrialPartData(
                        TrialPartProbeType.BlueActionSelection,
                        mission.getMissionType(), trialNum));
            } else {
                trialData.setBlueActionSelection(null);
            }
        }

        trialData.setData_stale(false);
        return trialData;
    }    
    
    /**
     * @param trialData
     * @param metricsInfo
     * @param comparisonTrialData
     * @return
     */
    public TrialData updateTrialMetrics(TrialData trialData, MetricsInfo metricsInfo,
            TrialData comparisonTrialData) {
        if (trialData != null) {
            trialData.setMetrics(trialMetricsComputer.updateTrialMetrics(
                    trialData, metricsInfo, comparisonTrialData));
        }
        return trialData;
    }
}