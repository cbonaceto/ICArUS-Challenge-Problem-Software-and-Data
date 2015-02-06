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
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.mitre.icarus.cps.app.util.ProbabilityUtils;
import org.mitre.icarus.cps.assessment.assessment_metrics_computer.phase_2.TrialMetricsComputer;

import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.NameValuePair;
import org.mitre.icarus.cps.assessment.score_computer.phase_2.ScoreComputer_Phase2;
import org.mitre.icarus.cps.exam.phase_2.testing.bluebook.RedTacticType;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.red_tactics_probes.AbstractRedTacticsProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.red_tactics_probes.MostLikelyRedTacticProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.red_tactics_probes.RedTacticProbability;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.red_tactics_probes.RedTacticsChange;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.red_tactics_probes.RedTacticsChangesProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.red_tactics_probes.RedTacticsProbabilityReportProbe;

/**
 * Contains response data for a Red tactics report probe.
 * 
 * @author CBONACETO
 *
 */
public class RedTacticsReportData extends TrialPartData<AbstractRedTacticsProbe> {

    private static final long serialVersionUID = 4088235376645111882L;

    /**
     * The tactics Red may be playing with (Missions 2 and 4-5)
     */
    protected List<RedTacticType> possibleRedTactics;

    /**
     * The most likely tactics Red is playing with (Mission 2)
     */
    protected RedTacticType mostLikelyRedTactic;    

    /**
     * The probability that Red is playing with each Red tactic (Missions 4-5)
     */
    protected List<Double> redTacticProbabilities;
    
    /** Standard deviations of the Red tactic probabilities */
    protected List<Double> redTacticProbabilities_std; 
    
    /**
     * The normative (quasi-Bayesian) probability that Red is playing with each Red tactic (Missions 4-5)
     */
    protected List<Double> redTacticProbabilities_normative;
    
    /** Standard deviations of the normative (quasi-Bayesian) Red tactic probabilities */
    protected List<Double> redTacticProbabilities_normative_std; 

    /**
     * The initial Red tactics attack probabilities (Mission 6)
     */
    protected RedTacticAttackProbabilitiesData initialRedAttackProbabilities;

    /**
     * The Red tactics for each trial on which the Red tactics changed (Mission
     * 6)
     */
    protected List<RedTacticAttackProbabilitiesData> redTacticsChanges;
    
    /** Whether it was possible to create a batch plot on the trial */
    protected Boolean batchPlotProbePresent;

    /**
     * Whether a batch plot was created
     */
    protected Boolean batchPlotCreated;

    /**
     * For batch plots, the total number of previous trials that may be reviewed
     */
    protected Integer batchPlotNumTrialsToReview;

    /**
     * If a batch blot was created, the number of previous trials reviewed
     */
    protected Integer batchPlotNumTrialsReviewed;

    /**
     * Ground truth (the tactic Red is actually playing with)
     */
    protected RedTacticType actualRedTactic;

    public RedTacticsReportData() {
    }

    public RedTacticsReportData(AbstractRedTacticsProbe redTacticsProbe) {
        super(redTacticsProbe);
    }

    public List<RedTacticType> getPossibleRedTactics() {
        return possibleRedTactics;
    }

    public void setPossibleRedTactics(List<RedTacticType> possibleRedTactics) {
        this.possibleRedTactics = possibleRedTactics;
    }

    public RedTacticType getMostLikelyRedTactic() {
        return mostLikelyRedTactic;
    }

    public void setMostLikelyRedTactic(RedTacticType mostLikelyRedTactic) {
        this.mostLikelyRedTactic = mostLikelyRedTactic;
    }

    public List<Double> getRedTacticProbabilities() {
        return redTacticProbabilities;
    }

    public void setRedTacticProbabilities(List<Double> redTacticProbabilities) {
        this.redTacticProbabilities = redTacticProbabilities;
    }
    
    public List<Double> getRedTacticProbabilities_std() {
        return redTacticProbabilities_std;
    }

    public void setRedTacticProbabilities_std(List<Double> redTacticProbabilities_std) {
        this.redTacticProbabilities_std = redTacticProbabilities_std;
    }

    public List<Double> getRedTacticProbabilities_normative() {
        return redTacticProbabilities_normative;
    }
    
    public void setRedTacticProbabilities_normative(List<Double> redTacticProbabilities_normative) {
        this.redTacticProbabilities_normative = redTacticProbabilities_normative;
    }      

    public List<Double> getRedTacticProbabilities_normative_std() {
        return redTacticProbabilities_normative_std;
    }

    public void setRedTacticProbabilities_normative_std(List<Double> redTacticProbabilities_normative_std) {
        this.redTacticProbabilities_normative_std = redTacticProbabilities_normative_std;
    }    

    public RedTacticAttackProbabilitiesData getInitialRedAttackProbabilities() {
        return initialRedAttackProbabilities;
    }

    public void setInitialRedAttackProbabilities(
            RedTacticAttackProbabilitiesData initialRedAttackProbabilities) {
        this.initialRedAttackProbabilities = initialRedAttackProbabilities;
    }

    public List<RedTacticAttackProbabilitiesData> getRedTacticsChanges() {
        return redTacticsChanges;
    }

    public void setRedTacticsChanges(List<RedTacticAttackProbabilitiesData> redTacticsChanges) {
        this.redTacticsChanges = redTacticsChanges;
    }

    public Boolean isBatchPlotProbePresent() {
        return batchPlotProbePresent;
    }

    public void setBatchPlotProbePresent(Boolean batchPlotProbePresent) {
        this.batchPlotProbePresent = batchPlotProbePresent;
    }

    public Boolean getBatchPlotCreated() {
        return batchPlotCreated;
    }

    public void setBatchPlotCreated(Boolean batchPlotCreated) {
        this.batchPlotCreated = batchPlotCreated;
    }

    public Integer getBatchPlotNumTrialsToReview() {
        return batchPlotNumTrialsToReview;
    }

    public void setBatchPlotNumTrialsToReview(Integer batchPlotNumTrialsToReview) {
        this.batchPlotNumTrialsToReview = batchPlotNumTrialsToReview;
    }

    public Integer getBatchPlotNumTrialsReviewed() {
        return batchPlotNumTrialsReviewed;
    }

    public void setBatchPlotNumTrialsReviewed(Integer batchPlotNumTrialsReviewed) {
        this.batchPlotNumTrialsReviewed = batchPlotNumTrialsReviewed;
    }

    public RedTacticType getActualRedTactic() {
        return actualRedTactic;
    }

    public void setActualRedTactic(RedTacticType actualRedTactic) {
        this.actualRedTactic = actualRedTactic;
    }

    @Override
    protected void initializeTrialPartData(AbstractRedTacticsProbe trialPartProbe) {
        if (trialPartProbe.getBatchPlotProbe() != null) {
            batchPlotProbePresent = true;           
            batchPlotCreated = trialPartProbe.getBatchPlotProbe().getNumPreviousTrialsSelected() != null
                   && trialPartProbe.getBatchPlotProbe().getNumPreviousTrialsSelected() > 0;
            batchPlotNumTrialsReviewed = trialPartProbe.getBatchPlotProbe().getNumPreviousTrialsSelected();
            batchPlotNumTrialsToReview = trialPartProbe.getBatchPlotProbe().getPreviousTrials() != null
                    ? trialPartProbe.getBatchPlotProbe().getPreviousTrials().size() : 0;
        }

        if (trialPartProbe instanceof MostLikelyRedTacticProbe) {
            possibleRedTactics = extractRedTactics(
                    ((MostLikelyRedTacticProbe) trialPartProbe).getRedTactics());
            mostLikelyRedTactic = ((MostLikelyRedTacticProbe) trialPartProbe).getMostLikelyRedTactic();
        } else if (trialPartProbe instanceof RedTacticsProbabilityReportProbe) {
            RedTacticsProbabilityReportProbe redTacticsProbabilityProbe
                    = (RedTacticsProbabilityReportProbe) trialPartProbe;
            if (redTacticsProbabilityProbe.getProbabilities() != null
                    && !redTacticsProbabilityProbe.getProbabilities().isEmpty()) {
                possibleRedTactics = new ArrayList<RedTacticType>();
                redTacticProbabilities = new ArrayList<Double>();
                redTacticProbabilities_normative = new ArrayList<Double>();
                for (RedTacticProbability prob : redTacticsProbabilityProbe.getProbabilities()) {
                    //System.out.println(prob.getRedTactic() + ", " + prob.getProbability());
                    possibleRedTactics.add(prob.getRedTactic());
                    redTacticProbabilities.add(prob.getProbability() != null ? 
                            prob.getProbability() / 100.d : null);
                    redTacticProbabilities_normative.add(prob.getNormativeProbability());
                }
                //Normalize probabilities
                if (TrialMetricsComputer.checkProbsNotNullOrEmpty(redTacticProbabilities)) {
                    ProbabilityUtils.normalizeDecimalProbabilities(redTacticProbabilities,
                            redTacticProbabilities, ScoreComputer_Phase2.EPSILON_PHASE2);
                }
                if (TrialMetricsComputer.checkProbsNotNullOrEmpty(redTacticProbabilities_normative)) {
                    ProbabilityUtils.normalizeDecimalProbabilities(redTacticProbabilities_normative,
                            redTacticProbabilities_normative, ScoreComputer_Phase2.EPSILON_PHASE2);
                }
            }
        } else if (trialPartProbe instanceof RedTacticsChangesProbe) {
            RedTacticsChangesProbe redTacticsChangesProbe = (RedTacticsChangesProbe) trialPartProbe;
            if (redTacticsChangesProbe.getInitialRedTactics() != null) {
                initialRedAttackProbabilities = new RedTacticAttackProbabilitiesData(
                        redTacticsChangesProbe.getInitialRedTactics().getAttackProbabilities());
            }
            if (redTacticsChangesProbe.getRedTacticsChanges() != null
                    && !redTacticsChangesProbe.getRedTacticsChanges().isEmpty()) {
                redTacticsChanges = new ArrayList<RedTacticAttackProbabilitiesData>();
                for (RedTacticsChange tactics : redTacticsChangesProbe.getRedTacticsChanges()) {
                    redTacticsChanges.add(new RedTacticAttackProbabilitiesData(tactics.getAttackProbabilities(),
                            tactics.getTrialNum()));
                }
            }
        }
    }

    @Override
    protected List<NameValuePair> getAdditionalDataValuesAsString(
            List<NameValuePair> dataValues) {
        String id = "red_tactics";
        if (dataValues == null) {
            dataValues = new LinkedList<NameValuePair>();
        }
        switch (trialPartType) {
            case MostLikelyRedTacticSelection:
                dataValues.add(new NameValuePair(
                        id + "_most_likely_tactic",
                        mostLikelyRedTactic != null ? mostLikelyRedTactic.getName() : null));
                break;
            case RedTacticsProbabilityReport:
                if (redTacticProbabilities != null && !redTacticProbabilities.isEmpty()) {
                    int i = 1;                   
                    for (Double probability : redTacticProbabilities) {                        
                        dataValues.add(new NameValuePair(
                                id + "_probs_tactic_" + i, probability != null ? probability.toString() : null));
                        i++;
                    }
                }
                if (redTacticProbabilities_normative != null && !redTacticProbabilities_normative.isEmpty()) {
                    int i = 1;
                    for (Double probability : redTacticProbabilities_normative) {
                        dataValues.add(new NameValuePair(
                                id + "_probs_normative_tactic_" + i, probability != null ? probability.toString() : null));
                        i++;
                    }
                }               
                break;
            case RedTacticsChangesReport:
                if (initialRedAttackProbabilities != null && !initialRedAttackProbabilities.isEmpty()) {
                    int i = 1;
                    for (Double probability : initialRedAttackProbabilities) {
                        dataValues.add(new NameValuePair(
                                id + "_attack_probs_initial_" + i, probability.toString()));
                        i++;
                    }
                }
                if (redTacticsChanges != null && !redTacticsChanges.isEmpty()) {
                    int changeNum = 1;
                    for (RedTacticAttackProbabilitiesData redTacticsChange : redTacticsChanges) {
                        if (redTacticsChange != null && !redTacticsChange.isEmpty()) {
                            dataValues.add(new NameValuePair(id + "_attack_change_trial_",
                                    redTacticsChange.trialNum != null ? redTacticsChange.trialNum.toString() : null));
                            int i = 1;
                            for (Double probability : redTacticsChange) {
                                dataValues.add(new NameValuePair(
                                        id + "_attack_change_" + changeNum + "_probs_" + i, probability.toString()));
                                i++;
                            }
                        }
                        changeNum++;
                    }
                }
                break;
            default:
                break;
        }
        if (batchPlotCreated != null) {
            dataValues.add(new NameValuePair(
                    id + "_batch_plot_trials_reviewed",
                    batchPlotNumTrialsReviewed != null ? batchPlotNumTrialsReviewed.toString() : null));
            dataValues.add(new NameValuePair(
                    id + "_batch_plot_trials_total",
                    batchPlotNumTrialsToReview != null ? batchPlotNumTrialsToReview.toString() : null));
        }
        if (actualRedTactic != null) {
            dataValues.add(new NameValuePair(
                    id + "_actual_red_tactic", actualRedTactic.getName()));
        }
        return dataValues;
    }

    /**
     * @param redTactics
     * @return
     */
    protected List<RedTacticType> extractRedTactics(
            Collection<RedTacticType> redTactics) {
        List<RedTacticType> tactics = null;
        if (redTactics != null && !redTactics.isEmpty()) {
            tactics = new ArrayList<RedTacticType>();
            for (RedTacticType tactic : redTactics) {
                tactics.add(tactic);
            }
        }
        return tactics;
    }
}
