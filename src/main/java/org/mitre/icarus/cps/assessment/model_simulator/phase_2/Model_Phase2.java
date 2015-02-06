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
package org.mitre.icarus.cps.assessment.model_simulator.phase_2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.mitre.icarus.cps.assessment.model_simulator.Model;
import org.mitre.icarus.cps.assessment.model_simulator.phase_2.model_components.AbRedAttackProbabilityComponent;
import org.mitre.icarus.cps.assessment.model_simulator.phase_2.model_components.VariableFrequencyBlueActionSelectionComponent;
import org.mitre.icarus.cps.assessment.model_simulator.phase_2.model_components.FixedFrequencySigintSelectionComponent;
import org.mitre.icarus.cps.assessment.model_simulator.phase_2.model_components.BayesianRedTacticsProbabilityComponent;
import org.mitre.icarus.cps.assessment.model_simulator.phase_2.model_components.IBlueActionSelectionComponent;
import org.mitre.icarus.cps.assessment.model_simulator.phase_2.model_components.IRedAttackProbabilityComponent;
import org.mitre.icarus.cps.assessment.model_simulator.phase_2.model_components.IRedTacticsProbabilityComponent;
import org.mitre.icarus.cps.assessment.model_simulator.phase_2.model_components.ISigintSelectionComponent;
import org.mitre.icarus.cps.assessment.model_simulator.phase_2.model_components.RedTacticProbabilitiesAndBatchPlot;
import org.mitre.icarus.cps.assessment.score_computer.phase_2.ScoreComputer_Phase2;
import org.mitre.icarus.cps.exam.phase_2.IcarusExam_Phase2;
import org.mitre.icarus.cps.exam.phase_2.testing.IcarusTestTrial_Phase2;
import org.mitre.icarus.cps.exam.phase_2.testing.Mission;
import org.mitre.icarus.cps.exam.phase_2.testing.Mission_1_2_3_Trial;
import org.mitre.icarus.cps.exam.phase_2.testing.Mission_4_5_6_Trial;
import org.mitre.icarus.cps.exam.phase_2.testing.bluebook.BlueBook;
import org.mitre.icarus.cps.exam.phase_2.testing.bluebook.RedTacticParameters;
import org.mitre.icarus.cps.exam.phase_2.testing.bluebook.RedTacticType;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.AttackProbabilityReportProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.RedAction;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbeType;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.int_presentation.SigintPresentationProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.red_tactics_probes.AbstractRedTacticsProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.red_tactics_probes.MostLikelyRedTacticProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.red_tactics_probes.RedTacticsProbabilityReportProbe;
import org.mitre.icarus.cps.feature_vector.phase_2.BlueLocation;
import org.mitre.icarus.cps.feature_vector.phase_2.int_datum.SigintReliability;

/**
 * Implementation of the IModel_Phase2 interface .
 * 
 * @author CBONACETO
 */
public class Model_Phase2 extends Model<IcarusExam_Phase2> implements IModel_Phase2 {    
    
    /** Description of the model */
    protected String description;
    
    /** The Red tactics probability assessment model component */    
    protected IRedTacticsProbabilityComponent redTacticsProbabilityComponent;
    
    /** The Red attack probability assessment model component */
    protected IRedAttackProbabilityComponent redAttackProbabilityComponent;    
    
    /** The SIGINT location selection model component */
    protected ISigintSelectionComponent sigintSelectionComponent;
    
    /** The Blue action selection model component */
    protected IBlueActionSelectionComponent blueActionSelectionComponent;    
    
    /** The current BLUEBOOK */
    protected BlueBook bluebook;
    
    /** The current SIGINT reliabilities */
    protected SigintReliability sigintReliability;    
    
    /** The current mission */
    protected Mission<?> mission;
    
    /** The current tactics Red may be playing with (or is playing with if there is only 1) */
    protected List<RedTacticType> redTactics;
    protected List<RedTacticParameters> redTacticParameters;
    
    /** The current number of red attacks that have been observed */
    protected int numAttacks;
    
    /** The current trial number */
    protected int trialNum;
    
    /**
     *
     */
    public Model_Phase2() {
        this(new BayesianRedTacticsProbabilityComponent(), 
                new AbRedAttackProbabilityComponent(),
                new FixedFrequencySigintSelectionComponent(),
                new VariableFrequencyBlueActionSelectionComponent());
    }
    
    /**
     *
     * @param redTacticsProbabilityComponent
     * @param redAttackProbabilityComponent
     * @param sigintSelectionComponent
     * @param blueActionSelectionComponent
     */
    public Model_Phase2(
            IRedTacticsProbabilityComponent redTacticsProbabilityComponent,
            IRedAttackProbabilityComponent redAttackProbabilityComponent,
            ISigintSelectionComponent sigintSelectionComponent,
            IBlueActionSelectionComponent blueActionSelectionComponent) {
        this.redTacticsProbabilityComponent = redTacticsProbabilityComponent;
        this.redAttackProbabilityComponent = redAttackProbabilityComponent;
        this.sigintSelectionComponent = sigintSelectionComponent;
        this.blueActionSelectionComponent = blueActionSelectionComponent;
    }    

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }    

    @Override
    public IRedTacticsProbabilityComponent getRedTacticsProbabilityComponent() {
        return redTacticsProbabilityComponent;
    }

    @Override
    public void setRedTacticsProbabilityComponent(IRedTacticsProbabilityComponent redTacticsProbabilityComponent) {
        this.redTacticsProbabilityComponent = redTacticsProbabilityComponent;
    }    

    @Override
    public IRedAttackProbabilityComponent getRedAttackProbabilityComponent() {
        return redAttackProbabilityComponent;
    }

    @Override
    public void setRedAttackProbabilityComponent(IRedAttackProbabilityComponent redAttackProbabilityComponent) {
        this.redAttackProbabilityComponent = redAttackProbabilityComponent;
    }

    @Override
    public ISigintSelectionComponent getSigintSelectionComponent() {
        return sigintSelectionComponent;
    }

    @Override
    public void setSigintSelectionComponent(ISigintSelectionComponent sigintSelectionComponent) {
        this.sigintSelectionComponent = sigintSelectionComponent;
    }

    @Override
    public IBlueActionSelectionComponent getBlueActionSelectionComponent() {
        return blueActionSelectionComponent;
    }

    @Override
    public void setBlueActionSelectionComponent(IBlueActionSelectionComponent blueActionSelectionComponent) {
        this.blueActionSelectionComponent = blueActionSelectionComponent;
    }    

    @Override
    public IcarusExam_Phase2 generateExamResponses(IcarusExam_Phase2 exam) {
        if (exam.getMissions() != null && !exam.getMissions().isEmpty()) {
            initializeExam(exam);
            Iterator<Mission<?>> missionIter = exam.getMissions().iterator();
            while(missionIter.hasNext()) {
                mission = missionIter.next();
                if (mission.getTestTrials() != null && !mission.getTestTrials().isEmpty()) {
                    initializeMission(mission);
                    for (IcarusTestTrial_Phase2 trial : mission.getTestTrials()) {
                        processTrial(trial);
                    }
                }
            }
        }
        return exam;
    }
    
    /**
     * @param exam
     */
    protected void initializeExam(IcarusExam_Phase2 exam) {
        bluebook = exam.getBlueBook() != null ? exam.getBlueBook() : 
                BlueBook.createDefaultBlueBook();
        sigintReliability = exam.getSigintReliability() != null ? exam.getSigintReliability() :
                SigintReliability.createDefaultSigintReliability();
    }
    
    /**
     *
     * @param mission
     */
    protected void initializeMission(Mission<?> mission) {
        redTactics = BlueBook.extractRedTacticTypes(
                bluebook.getRedTaticsForMission(mission.getMissionType()));
        redTacticParameters = ScoreComputer_Phase2.extractRedTacticParameters(redTactics);
        numAttacks = 0;
        trialNum = 1;
    }
    
    /**
     *
     * @param trial
     */
    protected void processTrial(IcarusTestTrial_Phase2 trial) {
        //If there is more than 1 Red tactic, compute the probability that Red is
        //playing with each tactic
        AbstractRedTacticsProbe redTacticsProbe = null;
        if (trial instanceof Mission_1_2_3_Trial) {
            redTacticsProbe = ((Mission_1_2_3_Trial) trial).getMostLikelyRedTacticProbe();
        } else if (trial instanceof Mission_4_5_6_Trial) {
            redTacticsProbe = ((Mission_4_5_6_Trial) trial).getRedTacticsProbe();
        }
        boolean canCreateBatchPlot = false;
        List<Integer> previousTrialsToReview = null;
        if (redTacticsProbe != null && redTacticsProbe.getBatchPlotProbe() != null) {
            canCreateBatchPlot = true;
            previousTrialsToReview = redTacticsProbe.getBatchPlotProbe().getPreviousTrials();
        }
        RedTacticProbabilitiesAndBatchPlot redTacticProbsAndBatchPlot
                = new RedTacticProbabilitiesAndBatchPlot(Arrays.asList(1.d));
        if (redTactics.size() > 1) {            
            redTacticProbsAndBatchPlot = redTacticsProbabilityComponent.computeRedTacticProbabilities(
                    mission, redTactics, trial, trialNum - 1, numAttacks, canCreateBatchPlot,
                    previousTrialsToReview);
        }

        //Generate a response to the Red tactics probe (Missions 2, 4, 5)      
        if (redTacticsProbe != null) {
            if (redTacticsProbe instanceof MostLikelyRedTacticProbe) {
                ((MostLikelyRedTacticProbe) redTacticsProbe).setMostLikelyRedTactic(
                        getMostLikelyRedTactic(redTactics,
                                redTacticProbsAndBatchPlot.getRedTacticProbabilities()));
            } else if (redTacticsProbe instanceof RedTacticsProbabilityReportProbe) {
                RedTacticsProbabilityReportProbe redTacticsProbabilityProbe
                        = (RedTacticsProbabilityReportProbe) redTacticsProbe;
                ScoreComputer_Phase2.ensureRedTacticsProbabilityReportProbeInitialized(
                        redTacticsProbabilityProbe, redTacticsProbabilityProbe.getId(), redTactics);
                int i = 0;
                for (Double prob : redTacticProbsAndBatchPlot.getRedTacticProbabilities()) {
                    redTacticsProbabilityProbe.getProbabilities().get(i).setProbability(prob);
                    i++;
                }
            }
            if (canCreateBatchPlot) {
                redTacticsProbe.getBatchPlotProbe().setNumPreviousTrialsSelected(
                        redTacticProbsAndBatchPlot.getNumPreviousTrialsReviewed());
            }
        }

        //Generate a response to the P(Attack|IMINT, OSINT) probe if present
        List<ScoreComputer_Phase2.RedAttackProbabilities> redAttackProbabilities = null;
        List<Double> currentRedAttackProbs = null;
        if (trial.getAttackPropensityProbe_Pp() != null) {
            redAttackProbabilities = redAttackProbabilityComponent.computeRedAttackProbs(
                    redAttackProbabilities, TrialPartProbeType.AttackProbabilityReport_Pp,
                    trial.getBlueLocations(), redTacticParameters,
                    redTacticProbsAndBatchPlot.getRedTacticProbabilities(),
                    null, null, null);
            currentRedAttackProbs = getCurrentRedAttackProbs(
                    redAttackProbabilities, TrialPartProbeType.AttackProbabilityReport_Pp);
            populateAttackProbabilityReportProbe(trial.getAttackPropensityProbe_Pp(),
                    TrialPartProbeType.AttackProbabilityReport_Pp, currentRedAttackProbs,
                    trial.getBlueLocations());
        }

        //Generate a response to the P(Attack|HUMINT, IMINT, OSINT) probe if present
        if (trial.getAttackProbabilityProbe_Ppc() != null) {
            redAttackProbabilities = redAttackProbabilityComponent.computeRedAttackProbs(
                    redAttackProbabilities, TrialPartProbeType.AttackProbabilityReport_Ppc,
                    trial.getBlueLocations(), redTacticParameters,
                    redTacticProbsAndBatchPlot.getRedTacticProbabilities(),
                    trial.getHumint(), null, null);
            currentRedAttackProbs = getCurrentRedAttackProbs(
                    redAttackProbabilities, TrialPartProbeType.AttackProbabilityReport_Ppc);
            populateAttackProbabilityReportProbe(trial.getAttackProbabilityProbe_Ppc(),
                    TrialPartProbeType.AttackProbabilityReport_Ppc, currentRedAttackProbs,
                    trial.getBlueLocations());
        }

        //Generate a response to the SIGINT selection probe if present
        List<String> sigintLocations = null;
        if (trial.getSigintSelectionProbe() != null) {
            sigintLocations = Arrays.asList(sigintSelectionComponent.selectSigintLocation(
                    trial.getBlueLocations(), currentRedAttackProbs));            
            trial.getSigintSelectionProbe().setSelectedLocationIds(sigintLocations);
        } else if (trial.getSigintPresentation() != null && !trial.getSigintPresentation().isEmpty()) {
            sigintLocations = new ArrayList<String>();
            for (SigintPresentationProbe sigintPresentation : trial.getSigintPresentation()) {
                sigintLocations.addAll(sigintPresentation.getLocationIds());
            }
        }

        //Generate a response to the P(Attack|SIGINT) probe if present
        if (trial.getAttackProbabilityProbe_Pt() != null) {
            redAttackProbabilities = redAttackProbabilityComponent.computeRedAttackProbs(
                    redAttackProbabilities, TrialPartProbeType.AttackProbabilityReport_Pt,
                    trial.getBlueLocations(), redTacticParameters,
                    redTacticProbsAndBatchPlot.getRedTacticProbabilities(),
                    trial.getHumint(), sigintReliability, sigintLocations);
            List<Double> sigintProbs = getCurrentRedAttackProbs(
                    redAttackProbabilities, TrialPartProbeType.AttackProbabilityReport_Pt);
            populateAttackProbabilityReportProbe(trial.getAttackProbabilityProbe_Pt(),
                    TrialPartProbeType.AttackProbabilityReport_Pt, sigintProbs,
                    trial.getBlueLocations());
        }

        //Generate a response to the P(Attack|SIGINT, HUMINT, IMINT, OSINT) probe if present
        if (trial.getAttackProbabilityProbe_Ptpc() != null) {
            redAttackProbabilities = redAttackProbabilityComponent.computeRedAttackProbs(
                    redAttackProbabilities, TrialPartProbeType.AttackProbabilityReport_Ptpc,
                    trial.getBlueLocations(), redTacticParameters,
                    redTacticProbsAndBatchPlot.getRedTacticProbabilities(),
                    trial.getHumint(), sigintReliability, sigintLocations);
            currentRedAttackProbs = getCurrentRedAttackProbs(
                    redAttackProbabilities, TrialPartProbeType.AttackProbabilityReport_Ptpc);
            populateAttackProbabilityReportProbe(trial.getAttackProbabilityProbe_Ptpc(),
                    TrialPartProbeType.AttackProbabilityReport_Ptpc, currentRedAttackProbs,
                    trial.getBlueLocations());
        }

        //Generate a response to the Blue action selection probe if present
        if (trial.getBlueActionSelection() != null
                && !trial.getBlueActionSelection().isDataProvidedToParticipant()) {
            trial.getBlueActionSelection().setBlueActions(
                    blueActionSelectionComponent.selectBlueActions(
                        trial.getBlueLocations(), currentRedAttackProbs));
        }

        //Update the number of Red attacks that have been observed
        if (trial.getRedActionSelection() != null
                && trial.getRedActionSelection().getRedAction().getAction() == RedAction.RedActionType.Attack) {
            numAttacks++;
        }
        trialNum++;  
    }
    
    /**
     *
     * @param redTactics
     * @param redTacticProbs
     * @return
     */
    protected static RedTacticType getMostLikelyRedTactic(List<RedTacticType> redTactics,
            List<Double> redTacticProbs) {
        RedTacticType mostLikelyTactic = null;
        if (redTactics != null && !redTactics.isEmpty()
                && redTacticProbs != null && redTacticProbs.size() == redTactics.size()) {
            Double maxProb = Double.MIN_VALUE;
            Iterator<RedTacticType> redTacticsIter = redTactics.iterator();
            Iterator<Double> probsIter = redTacticProbs.iterator();
            while (redTacticsIter.hasNext()) {
                RedTacticType redTactic = redTacticsIter.next();
                Double prob = probsIter.next();
                if (prob > maxProb) {
                    maxProb = prob;
                    mostLikelyTactic = redTactic;
                }
            }
        }
        return mostLikelyTactic;
    }
    
    /**
     *
     * @param probe
     * @param type
     * @param redAttackProbabilities
     * @param locations
     */
    protected static void populateAttackProbabilityReportProbe(AttackProbabilityReportProbe probe,
            TrialPartProbeType type, List<Double> redAttackProbabilities,
            List<BlueLocation> locations) {
        if (probe != null && redAttackProbabilities != null) {
            ScoreComputer_Phase2.ensureAttackProbabilityReportProbeInitialized(
                    probe, type, probe.getId(), locations);
            for (int i = 0; i < redAttackProbabilities.size(); i++) {
                Double prob = redAttackProbabilities.get(i);
                /*switch (type) {
                    case AttackProbabilityReport_Pp:
                        prob = redAttackProbabilities.get(i).pPropensity;
                        break;
                    case AttackProbabilityReport_Ppc:
                        prob = redAttackProbabilities.get(i).pPropensityCapability;
                        break;
                    case AttackProbabilityReport_Pt:
                        prob = redAttackProbabilities.get(i).pActivity;
                        break;
                    case AttackProbabilityReport_Ptpc:
                        prob = redAttackProbabilities.get(i).pActivityPropensityCapability;
                        break;
                }*/
                probe.getProbabilities().get(i).setProbability(prob * 100.d);  //TODO: * 100?                   
            }
        }
    }
    
    /**
     *
     * @param redAttackProbabilities
     * @param type
     * @return
     */
    protected static List<Double> getCurrentRedAttackProbs(
            List<ScoreComputer_Phase2.RedAttackProbabilities> redAttackProbabilities,
            TrialPartProbeType type) {
        List<Double> probs = null;
        if (redAttackProbabilities != null && !redAttackProbabilities.isEmpty()) {
            probs = new ArrayList<Double>(redAttackProbabilities.size());
            for (int i = 0; i < redAttackProbabilities.size(); i++) {
                Double prob = null;
                switch (type) {
                    case AttackProbabilityReport_Pp:
                        prob = redAttackProbabilities.get(i).pPropensity;
                        break;
                    case AttackProbabilityReport_Ppc:
                        prob = redAttackProbabilities.get(i).pPropensityCapability;
                        break;
                    case AttackProbabilityReport_Pt:
                        prob = redAttackProbabilities.get(i).pActivity;
                        break;
                    case AttackProbabilityReport_Ptpc:
                        prob = redAttackProbabilities.get(i).pActivityPropensityCapability;
                        break;
                }
                probs.add(prob);
            }
        }
        return probs;
    }
}