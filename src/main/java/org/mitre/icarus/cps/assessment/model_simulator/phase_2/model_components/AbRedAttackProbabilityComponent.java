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
package org.mitre.icarus.cps.assessment.model_simulator.phase_2.model_components;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.mitre.icarus.cps.app.util.ProbabilityUtils;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.SubjectSigintProbabilities;
import org.mitre.icarus.cps.assessment.score_computer.phase_2.ScoreComputer_Phase2;
import org.mitre.icarus.cps.exam.phase_2.testing.bluebook.RedTacticParameters;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbeType;
import org.mitre.icarus.cps.feature_vector.phase_2.BlueLocation;
import org.mitre.icarus.cps.feature_vector.phase_2.int_datum.HumintDatum;
import org.mitre.icarus.cps.feature_vector.phase_2.int_datum.SigintReliability;

/**
 * Model component that assesses the probability of Red attack using an A-B model.
 * Note that when A = 1 and B = 1, the A-B model is a normative Bayesian model. 
 * 
 * @author CBONACETO
 */
public class AbRedAttackProbabilityComponent implements IRedAttackProbabilityComponent {
    
    /** The "A" parameter (prior discounting factor, default is 1.0) */
    private double a = 1.d;
    
    /** The "B" parameter (likelihood discounting factor, default is 1.0) */
    private double b = 1.d;
    
    /** P(Attack|SIGINT) for the model */   
    private SubjectSigintProbabilities modelSigintProbabilities;
    
    public AbRedAttackProbabilityComponent() {}
    
    /**
     *
     * @param a
     * @param b
     * @param modelSigintProbabilities
     */
    public AbRedAttackProbabilityComponent(double a, double b, 
            SubjectSigintProbabilities modelSigintProbabilities) {
        this.a = a;
        this.b = b;
        this.modelSigintProbabilities = modelSigintProbabilities;
    }

    public double getA() {
        return a;
    }

    public void setA(double a) {
        this.a = a;
    }

    public double getB() {
        return b;
    }

    public void setB(double b) {
        this.b = b;
    }    

    public SubjectSigintProbabilities getModelSigintProbabilities() {
        return modelSigintProbabilities;
    }

    public void setModelSigintProbabilities(SubjectSigintProbabilities modelSigintProbabilities) {
        this.modelSigintProbabilities = modelSigintProbabilities;
    }    
    
    @Override
    public List<ScoreComputer_Phase2.RedAttackProbabilities> computeRedAttackProbs(
            List<ScoreComputer_Phase2.RedAttackProbabilities> attackProbabilities, TrialPartProbeType currStage,
            List<BlueLocation> locations, List<RedTacticParameters> redTactics, 
            List<Double> redTacticProbs, HumintDatum humint, SigintReliability sigintReliability,
            List<String> sigintLocations) {
        if (sigintLocations != null && sigintLocations.size() > 1) {
            throw new IllegalArgumentException("Error, SIGINT can only be selected at one location");
        }
        if (redTactics != null && !redTactics.isEmpty() && locations != null && !locations.isEmpty()) {
            int currStageNum = getAttackProbabilityStageNum(currStage);
            int numLocations = locations.size();
            Double[] priors = new Double[numLocations + 1];
            int sigintLocationIndex = -1;
            Double sigintLikelihood = 0.d;
            double priorsSum = 0;
            int i;
            if(attackProbabilities == null || attackProbabilities.size() != locations.size()) {
                attackProbabilities = new ArrayList<ScoreComputer_Phase2.RedAttackProbabilities>(numLocations);
                for (i = 0; i < locations.size(); i++) {                    
                    ScoreComputer_Phase2.RedAttackProbabilities probs = new ScoreComputer_Phase2.RedAttackProbabilities();
                    attackProbabilities.add(probs);
                }
            }            
            i = 0;            
            for (BlueLocation location : locations) {
                ScoreComputer_Phase2.RedAttackProbabilities probs = attackProbabilities.get(i);
                
                //Compute P(Attack|Propensity)
                if (currStage == TrialPartProbeType.AttackProbabilityReport_Pp 
                        || (currStageNum > 0 && probs.getpPropensity() == null)) {
                    if (redTactics.size() > 1 && redTacticProbs != null && redTacticProbs.size() == redTactics.size()) {
                        //Compute P(Attack|Propensity) based on the probability that Red is playing with each tactic and the given
                        //values of P and U
                        Iterator<RedTacticParameters> redTacticsIter = redTactics.iterator();
                        Iterator<Double> redTacticProbsIter = redTacticProbs.iterator();
                        double pPropensity = 0.d;                        
                        while (redTacticsIter.hasNext()) {
                            RedTacticParameters redTactic = redTacticsIter.next();
                            Double redTacticProb = redTacticProbsIter.next();
                            pPropensity += redTactic.getAttackProbability(
                                    location.getOsint() != null ? location.getOsint().getRedVulnerability_P() : 0,
                                    location.getImint() != null ? location.getImint().getRedOpportunity_U() : 0)
                                    * redTacticProb;
                        }
                        probs.pPropensity = pPropensity;
                    } else {
                        //Get P(Propensity) from the Red tactic(s) given vulnerability (P) and utility (U) at the location
                        probs.pPropensity = redTactics.get(0).getAttackProbability(
                                location.getOsint() != null ? location.getOsint().getRedVulnerability_P() : 0,
                                location.getImint() != null ? location.getImint().getRedOpportunity_U() : 0);
                    }
                }                

                //Compute P(Attack|Propensity, Capability)
                if (currStage == TrialPartProbeType.AttackProbabilityReport_Ppc 
                        || (currStageNum > 1 && probs.getpPropensityCapability() == null)) {
                    //Compute P(Attack|Propensity, Capability) as P(Attack|Propensity) * P(Capability) (from HUMINT)
                    probs.pPropensityCapability = humint != null && humint.getRedCapability_Pc() != null
                            ? Math.pow(probs.getpPropensity(), a) * Math.pow(humint.getRedCapability_Pc(), b) 
                            : probs.getpPropensity();                    
                }
                
                if(currStage == TrialPartProbeType.AttackProbabilityReport_Ptpc) {
                    priors[i] = probs.getpPropensityCapability();
                    priorsSum += priors[i] != null ? priors[i] : 0d;
                }

                //Compute P(Attack|Activity) if SIGINT is available at the location
                if (currStage == TrialPartProbeType.AttackProbabilityReport_Pt
                        || (currStageNum > 2 && probs.getpActivity() == null)) {                    
                    if (location.getSigint() != null
                            && location.getSigint().isRedActivityDetected() != null
                            && ScoreComputer_Phase2.isSigintAvailableAtLocation(location, sigintLocations)) {
                        if (modelSigintProbabilities != null) {
                            probs.pActivity = location.getSigint().isRedActivityDetected()
                                    ? modelSigintProbabilities.getPtChatter() : modelSigintProbabilities.getPtSilent();                                    
                        } else if (sigintReliability != null) {
                            //Chatter: Pt = P(y|Y) = P(Y|y) / [P(Y|y) + P(Y|n)]
                            //Silence: Pt = P(y|N) = P(N|y) / [P(N|y) + P(N|n)]                        
                            probs.pActivity = location.getSigint().isRedActivityDetected()
                                    ? sigintReliability.getChatterLikelihood_attack()
                                    / (sigintReliability.getChatterLikelihood_attack() + sigintReliability.getChatterLikelihood_noAttack())
                                    : sigintReliability.getSilenceLikelihood_attack()
                                    / (sigintReliability.getSilenceLikelihood_attack() + sigintReliability.getSilenceLikelihood_noAttack());
                        }
                        sigintLikelihood = probs.pActivity;
                        sigintLocationIndex = i;                    
                    }
                } else if(currStage == TrialPartProbeType.AttackProbabilityReport_Ptpc &&
                        location.getSigint() != null
                            && location.getSigint().isRedActivityDetected() != null
                            && ScoreComputer_Phase2.isSigintAvailableAtLocation(location, sigintLocations)) {
                    sigintLikelihood = probs.pActivity;
                    sigintLocationIndex = i;                    
                }
                i++;
            }

            //Compute P(Attack|Activity, Propensity, Capability)
            if (currStage == TrialPartProbeType.AttackProbabilityReport_Ptpc) {
                //Compute P(Activity, Propensity, Capability) using priors P(Propensity, Capability) and 
                //likelihoods P(Activity) (if SIGINT is available)
                if (sigintLocationIndex >= 0 && sigintLikelihood != null) {
                    //Case where SIGINT was obtained at a location
                    priors[numLocations] = 1.d - priorsSum;
                    List<Double> posteriors = new ArrayList<Double>(numLocations + 1);
                    for (i = 0; i < numLocations + 1; i++) {
                        double likelihood;
                        if (i == sigintLocationIndex) {
                            likelihood = sigintLikelihood;
                        } else {
                            likelihood = (1 - sigintLikelihood) / numLocations;
                            if (i < locations.size()) {
                                attackProbabilities.get(i).pActivity = likelihood;
                            }
                        }                        
                        posteriors.add(Math.pow(priors[i], a) * Math.pow(likelihood, b));
                    }
                    posteriors = ProbabilityUtils.normalizeDecimalProbabilities(posteriors, posteriors);
                    for (i = 0; i < numLocations; i++) {
                        attackProbabilities.get(i).pActivityPropensityCapability = posteriors.get(i);
                    }
                } else {
                    //Case for which SIGINT is not available            
                    for (ScoreComputer_Phase2.RedAttackProbabilities probs : attackProbabilities) {
                        probs.pActivityPropensityCapability = probs.pPropensityCapability;
                    }
                }
            }
            return attackProbabilities;
        } else {
            return null;
        }
    }
    
    /**
     *
     * @param currStage
     * @return
     */
    protected static int getAttackProbabilityStageNum(TrialPartProbeType currStage) {
        if (currStage == TrialPartProbeType.AttackProbabilityReport_Pp) {
            return 0;
        } else if (currStage == TrialPartProbeType.AttackProbabilityReport_Ppc) {
            return 1;
        } else if (currStage == TrialPartProbeType.AttackProbabilityReport_Pt) {
            return 2;
        } else if (currStage == TrialPartProbeType.AttackProbabilityReport_Ptpc) {
            return 3;
        } else {
            return 0;
        }
    }
}