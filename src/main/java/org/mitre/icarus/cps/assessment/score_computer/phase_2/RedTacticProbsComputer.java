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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
//import org.apache.commons.math3.stat.inference.ChiSquareTest;
import org.mitre.icarus.cps.app.util.ProbabilityUtils;
import static org.mitre.icarus.cps.assessment.score_computer.ScoreComputerBase.factorial;
import static org.mitre.icarus.cps.assessment.score_computer.phase_2.ScoreComputer_Phase2.extractRedTacticParameters;
import org.mitre.icarus.cps.exam.phase_2.testing.IcarusTestTrial_Phase2;
import org.mitre.icarus.cps.exam.phase_2.testing.MissionType;
import org.mitre.icarus.cps.exam.phase_2.testing.bluebook.RedTacticParameters;
import org.mitre.icarus.cps.exam.phase_2.testing.bluebook.RedTacticParameters.RedTacticQuadrant;
import org.mitre.icarus.cps.exam.phase_2.testing.bluebook.RedTacticType;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.RedAction;
import org.mitre.icarus.cps.feature_vector.phase_2.BlueLocation;

/**
 * Contains methods to compute the probability that Red is playing with each tactic. 
 * Used on Missions where Red plays with more than 1 tactic (Missions 2, 4, and 5).
 * 
 * @author CBONACETO
 */
public class RedTacticProbsComputer {
    
    /**
     * Compute the probabilities that Red is playing with each tactic given the
     * history of Red attacks.
     *
     * @param missionType the type of mission
     * @param redTactics the tactics Red may be playing with
     * @param trials the trials for the mission
     * @param numTrials the number of previous trials that have been played
     * @param numAttacks the total number of Red attacks that occurred on
     * previous trials
     * @return the probability that Red is playing with each tactic (in decimal
     * and not percent format)
     */
    public static List<Double> computeNormativeRedTacticProbabilities(MissionType missionType, List<RedTacticType> redTactics,
            List<? extends IcarusTestTrial_Phase2> trials, int numTrials, int numAttacks) { //, boolean useChiSquareTest) {        
        //Determine the bins to use based on the tactic types
        List<List<RedTacticQuadrantsBin>> redTacticBins = 
                new ArrayList<List<RedTacticQuadrantsBin>>(redTactics.size());
        for (RedTacticType tactic : redTactics) {            
            redTacticBins.add(createRedTacticBins(tactic));
        }
        boolean considerHumint = missionType == MissionType.Mission_2;
        return computeNormativeRedTacticProbabilities(extractRedTacticParameters(redTactics),
                redTacticBins, trials, numTrials, numAttacks, considerHumint);
    }
    
    /**
     * Create the default bins for the given Red tactic, which are used to bin 
     * Red attacks into when comparing expected and observed attack frequencies.
     * 
     * @param tactic the Red tactic
     * @return the bins to bin Red attacks into when comparing expected and observed attack frequencies
     */
    public static List<RedTacticQuadrantsBin> createRedTacticBins(RedTacticType tactic) {
        List<RedTacticQuadrantsBin> bins;
        switch (tactic) {
            case Mission_2_Passive:
                Mission_2_Agressive:
                bins = Arrays.asList(RedTacticQuadrantsBin.createAllQuadrantsBin());
                break;
            case Mission_4_Passive:
                Mission_4_Agressive:
                bins = Arrays.asList(RedTacticQuadrantsBin.createAllQuadrantsBin());
                /*bins = new ArrayList<RedTacticQuadrantsBin>(4);
                 for(RedTacticQuadrant quadrant : RedTacticQuadrant.values()) {
                 bins.add(new RedTacticQuadrantsBin(quadrant));
                 }*/
                break;
            case Mission_5_Psensitive:
                /*bins = Arrays.asList(RedTacticQuadrantsBin.createLowPBin(), 
                 RedTacticQuadrantsBin.createHighPBin());*/
                bins = new ArrayList<RedTacticQuadrantsBin>(4);
                for (RedTacticQuadrant quadrant : RedTacticQuadrant.values()) {
                    bins.add(new RedTacticQuadrantsBin(quadrant));
                }
                return bins;
            case Mission_5_Usensitive:
                /*bins = Arrays.asList(RedTacticQuadrantsBin.createSmallUBin(), 
                 RedTacticQuadrantsBin.createLargeUBin());*/
                bins = new ArrayList<RedTacticQuadrantsBin>(4);
                for (RedTacticQuadrant quadrant : RedTacticQuadrant.values()) {
                    bins.add(new RedTacticQuadrantsBin(quadrant));
                }
                break;
            default:
                bins = Arrays.asList(RedTacticQuadrantsBin.createAllQuadrantsBin());
                break;
        }
        return bins;
    }

    /**
     * Compute the probabilities that Red is playing with each tactic given the
     * history of Red attacks.
     *
     * @param redTactics the tactics Red may be playing with
     * @param redTacticBins the bins to bin Red attacks into when comparing
     * expected and observed attack frequencies.
     * @param trials the trials for the mission
     * @param numTrials the number of previous trials that have been played
     * @param numAttacks the total number of Red attacks that occurred on
     * previous trials
     * @param considerHumint whether Red's capability to attack (from HUMINT)
     * should be considered (not considered in Missions 4 and 5)
     * @return the probability that Red is playing with each tactic (in decimal
     * and not percent format)
     */
    public static List<Double> computeNormativeRedTacticProbabilities(List<RedTacticParameters> redTactics,
            List<List<RedTacticQuadrantsBin>> redTacticBins, List<? extends IcarusTestTrial_Phase2> trials,
            int numTrials, int numAttacks, boolean considerHumint) {
        if (redTactics == null || redTactics.isEmpty() || redTacticBins == null
                || redTacticBins.size() != redTactics.size()) {
            return null;
        } else if (redTactics.size() == 1) {
            return Arrays.asList(1.d);
        } else {
            //ChiSquareTest cs = new ChiSquareTest();                        
            ArrayList<Double> redTacticProbs = new ArrayList<Double>(redTactics.size());
            int i = 0;
            for (RedTacticParameters tactic : redTactics) {
                List<RedTacticQuadrantsBin> tacticBins = redTacticBins.get(i);
                double tacticProb;
                if (numTrials > 0) {
                    //if (!useChiSquareTest || numAttacks == 0) {
                    //Compute the observed attack counts for the current tactic
                    long[] observedCounts = computeObservedAttackCounts(tactic, 
                            tacticBins, trials, numTrials);
                    //System.out.println("Observed attack counts for " + (i+1) + ": " + longArrToString(observedCounts) + ", num trials: " + numTrials);                        

                    //Compute the expected attack frequencies for the current tactic
                    double[] expectedFrequencies = computeExpectedAttackFrequencies(
                            tactic, tacticBins, trials, numTrials, 0.d, considerHumint);
                        //System.out.println("Expected attack frequencies for " + (i+1) + ": " + doubleArrToString(expectedFrequencies) + ", num trials: " + numTrials);
                    //System.out.println("Expected attack counts for " + (i+1) + ": " + 
                    //        doubleArrToString(computeExpectedAttackCounts(
                    //        tactic, tacticBins, trials, numTrials, 0, considerHumint)) + ", num trials: " + numTrials);

                    //Compute the total number of trials for each bin
                    long[] trialsPerBin = computeNumTrialsPerBin(tactic, 
                            tacticBins, trials, numTrials);
                        //System.out.println("Trials per bin for " + (i+1) + ": " + longArrToString(trialsPerBin) + ", num trials: " + numTrials);

                        //Compute the probability that Red is playing with the current tactic using the binomial function
                    //t = number of trials, n = number of attacks, f = expected attack frequency
                    //tacticProb = [ t! / (n! (t-n)!)] * f^n * (1-f)^(t-n)	                      
                    tacticProb = 1;
                    for (int bin = 0; bin < tacticBins.size(); bin++) {
                        tacticProb *= computeBinomialProbability(
                                trialsPerBin[bin], observedCounts[bin], expectedFrequencies[bin]);                        
                    }
                     /*} else {
                     if (numAttacks == 0) {
                     tacticProb = 1.d / redTactics.size();
                     } else {
                     //Compute the observed attack counts
                     long[] observedCounts = computeObservedAttackCounts(tactic, tacticBins, trials, numTrials);
                     //System.out.println("Observed attack counts for " + tactic + ": " + longArrToString(observedCounts) + ", num trials: " + numTrials);
                            
                     //Compute the expected attack counts for the current tactic
                     double[] expectedCounts = computeExpectedAttackCounts(
                     tactic, tacticBins, trials, numTrials, 0.00000001d, considerHumint);
                     //System.out.println("Expected attack counts for " + tactic + ": " + doubleArrToString(expectedCounts) + ", num trials: " + numTrials);

                     //Compute the probability that Red is playing with the current tactic using the Chi Squared Goodness of Fit test                       
                     tacticProb = cs.chiSquareTest(expectedCounts, observedCounts);
                     }
                     }*/
                } else {
                    //On the first trial, there is an equal probability that Red is playing with each tactic
                    tacticProb = 1.d / redTactics.size();
                }
                //System.out.println("Probability for " + (i+1) + ": " + tacticProb);
                redTacticProbs.add(tacticProb);
                i++;
            }
            //Normalize the Red tactic probabilities and return them
            ProbabilityUtils.normalizeDecimalProbabilities(redTacticProbs, redTacticProbs);
            //System.out.println("Probability Red is playing with each tactic: " + redTacticProbs
            //		+ ", num trials: " + numTrials + ", num attacks: " + numAttacks);
            return redTacticProbs;
        }
    }

    /**
     * Computes the probability of observing a number of occurrences of an event (observedCount) given
     * the total number of trials (numTrials) and the expected frequency of the event (expectedFrequency, 
     * probability that the event would occur on each trial) using the binomial distribution.
     * 
     * @param numTrials
     * @param observedCount
     * @param expectedFrequency
     * @return
     */
    public static double computeBinomialProbability(long numTrials, long observedCount, double expectedFrequency) {
        return (factorial(numTrials) / (factorial(observedCount) * factorial(numTrials - observedCount)))
            * Math.pow(expectedFrequency, observedCount) * Math.pow(1 - expectedFrequency, numTrials - observedCount);
    }
    
    /**
     * Converts an array of double values into a string.
     * 
     * @param arr
     * @return
     */
    protected static String doubleArrToString(double[] arr) {
        StringBuilder sb = new StringBuilder("[");        
        for(int i = 0; i < arr.length; i++) {
            sb.append(Double.toString(arr[i]));
            if(i < arr.length - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
    
    /**
     * Converts an array of long values into a string.
     * 
     * @param arr
     * @return
     */
    protected static String longArrToString(long[] arr) {
        StringBuilder sb = new StringBuilder("[");        
        for(int i = 0; i < arr.length; i++) {
            sb.append(Long.toString(arr[i]));
            if(i < arr.length - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
   
    /**
     * Computes the expected frequency of Red attacks on the previous trials.
     * 
     * @param tactic
     * @param trials
     * @param numTrials
     * @param considerHumint
     * @return
     */
    public static double computeExpectedAttackFrequency(RedTacticParameters tactic,            
            List<? extends IcarusTestTrial_Phase2> trials, int numTrials, boolean considerHumint) {
        double expectedFrequency = 0d;
        if (trials != null && !trials.isEmpty() && numTrials > 0 && numTrials < trials.size()) {
            //Compute the expected attack frequency for the current tactic based on the values of
            //P and U for the previous trials and the BLUEBOOK attack probabilities for the tactic			
            Iterator<? extends IcarusTestTrial_Phase2> trialIter = trials.iterator();
            for (int i = 0; i < numTrials; i++) {
                IcarusTestTrial_Phase2 trial = trialIter.next();
                if (trial.getBlueLocations() != null && !trial.getBlueLocations().isEmpty()) {
                    double redCapability_Pc = considerHumint && trial.getHumint() != null && 
                            trial.getHumint().getRedCapability_Pc() != null ?
                            trial.getHumint().getRedCapability_Pc() : 1.d;
                    for (BlueLocation location : trial.getBlueLocations()) {                       
                        expectedFrequency += tactic.getAttackProbability(
                                location.getOsint() != null ? location.getOsint().getRedVulnerability_P() : 0,
                                location.getImint() != null ? location.getImint().getRedOpportunity_U() : 0) * redCapability_Pc;
                    }
                }
            }
            expectedFrequency = expectedFrequency / numTrials;
        } else {
            //Compute the expected attack frequency of attack as the average of the 
            //BLUEBOOK attack probabilities for the tactic
            for (Double probability : tactic.getAttackProbabilities()) {
                expectedFrequency += probability;
            }
            expectedFrequency = expectedFrequency / tactic.getAttackProbabilities().size();
        }
        return expectedFrequency;
    }
    
    /**
     * Computes the expected frequency of Red attacks in each bin on the previous trials
     * given the Red tactic.
     * 
     * @param tactic
     * @param tacticBins
     * @param trials
     * @param numTrials
     * @param minFrequency
     * @param considerHumint
     * @return
     */
    public static double[] computeExpectedAttackFrequencies(RedTacticParameters tactic,
            List<RedTacticQuadrantsBin> tacticBins, List<? extends IcarusTestTrial_Phase2> trials, 
            int numTrials, double minFrequency, boolean considerHumint) { 
        return computeExpectedAttackCountsOrFrequencies(tactic, tacticBins, trials, 
                numTrials, minFrequency, considerHumint, false);
    }
    
    /**
     * Computes the expected of number of Red attacks in each bin on the previous trials
     * given the Red tactic.
     * 
     * @param tactic
     * @param tacticBins
     * @param trials
     * @param numTrials
     * @param minFrequency
     * @param considerHumint
     * @return
     */
    public static double[] computeExpectedAttackCounts(RedTacticParameters tactic,
            List<RedTacticQuadrantsBin> tacticBins, List<? extends IcarusTestTrial_Phase2> trials, 
            int numTrials, double minFrequency, boolean considerHumint) {
        return computeExpectedAttackCountsOrFrequencies(tactic, tacticBins,
                trials, numTrials, minFrequency, considerHumint, true);
    }
    
    /**
     * Computes the expected of number (or frequency) of Red attacks in each bin on the previous trials
     * given the Red tactic.
     * 
     * @param tactic
     * @param tacticBins
     * @param trials
     * @param numTrials
     * @param minFrequency
     * @param considerHumint
     * @param returnCounts
     * @return
     */
    public static double[] computeExpectedAttackCountsOrFrequencies(RedTacticParameters tactic,
            List<RedTacticQuadrantsBin> tacticBins, List<? extends IcarusTestTrial_Phase2> trials, 
            int numTrials, double minFrequency, boolean considerHumint, boolean returnCounts) {       
        double[] expectedCounts= new double[tacticBins.size()];
        double[] expectedFrequencies = new double[tacticBins.size()];        
        if (((trials == null || trials.isEmpty()) || numTrials <= 0) || numTrials >= trials.size()) {
            throw new IllegalArgumentException("Trials cannot be empty and numTrials must be > 0");
            //Compute the expected attack counts and frequencies using the BLUEBOOK attack probabilities for the tactic            
            /*for(int i = 0; i < 4; i++) {
                Double attackProb = tactic.getAttackProbabilities().get(i);
                expectedCounts[i] = numTrials * attackProb;
            }*/
        } else {
            //Compute the expected attack counts and frequencies for the current tactic based on the values of
            //P and U for the previous trials and the BLUEBOOK attack probabilities for the tactic		
            Iterator<? extends IcarusTestTrial_Phase2> trialIter = trials.iterator();
            for (int i = 0; i < numTrials; i++) {
                IcarusTestTrial_Phase2 trial = trialIter.next();               
                if (trial.getBlueLocations() != null && !trial.getBlueLocations().isEmpty()) {
                    double redCapability_Pc = considerHumint && trial.getHumint() != null && 
                            trial.getHumint().getRedCapability_Pc() != null ?
                            trial.getHumint().getRedCapability_Pc() : 1;
                    for (BlueLocation location : trial.getBlueLocations()) {
                        double redVulnerability_P = location.getOsint() != null ? location.getOsint().getRedVulnerability_P() : 0;
                        int redOpportunity_U = location.getImint() != null ? location.getImint().getRedOpportunity_U() : 0;                        
                        int quadrant = tactic.getRedTacticQuadrant(redVulnerability_P, redOpportunity_U).ordinal();
                        int binIndex = findBinIndex(quadrant, tacticBins);
                        expectedCounts[binIndex] = expectedCounts[binIndex] + 1;
                        expectedFrequencies[binIndex] += tactic.getAttackProbability(
                                redVulnerability_P, redOpportunity_U) * redCapability_Pc;
                    }
                }
            }
            for(int i = 0; i < tacticBins.size(); i++) {                
                expectedFrequencies[i] = expectedCounts[i] > 0 ? 
                        expectedFrequencies[i] / expectedCounts[i] : expectedFrequencies[i];
                expectedCounts[i] = Math.max(expectedCounts[i] * expectedFrequencies[i], minFrequency);
                //Double attackProb = tactic.getAttackProbabilities().get(i);
                //expectedCounts[i] = Math.max(expectedCounts[i] * attackProb, minFrequency);
            }
        }
        return returnCounts ? expectedCounts : expectedFrequencies;
    }
     
    /**
     * Computes the number of Red attacks that occurred on the previous trials.
     * 
     * @param trials
     * @param numTrials
     * @return
     */
    public static int computeObservedAttackCount(List<? extends IcarusTestTrial_Phase2> trials,
            int numTrials) {
        int count = 0;
        if (trials != null && !trials.isEmpty() && numTrials > 0 && numTrials <= trials.size()) {
            Iterator<? extends IcarusTestTrial_Phase2> trialIter = trials.iterator();
            for (int i = 0; i < numTrials; i++) {
                IcarusTestTrial_Phase2 trial = trialIter.next();
                RedAction redAction = trial.getRedActionSelection() != null
                        ? trial.getRedActionSelection().getRedAction() : null;
                if (redAction != null && redAction.getAction() == RedAction.RedActionType.Attack) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Computes the number of Red attacks that occurred in each bin on the previous trials.
     * 
     * @param tactic
     * @param tacticBins
     * @param trials
     * @param numTrials
     * @return
     */
    public static long[] computeObservedAttackCounts(RedTacticParameters tactic,
            List<RedTacticQuadrantsBin> tacticBins, 
            List<? extends IcarusTestTrial_Phase2> trials, int numTrials) {
        long[] observedCounts = new long[tacticBins.size()];
        if (trials != null && !trials.isEmpty() && numTrials > 0 && numTrials <= trials.size()) {
            Iterator<? extends IcarusTestTrial_Phase2> trialIter = trials.iterator();
            for (int i = 0; i < numTrials; i++) {
                IcarusTestTrial_Phase2 trial = trialIter.next();
                RedAction redAction = trial.getRedActionSelection() != null
                        ? trial.getRedActionSelection().getRedAction() : null;
                if (redAction != null && redAction.getAction() == RedAction.RedActionType.Attack
                        && redAction.getLocationId() != null && trial.getBlueLocations() != null
                        && !trial.getBlueLocations().isEmpty()) {
                    for (BlueLocation location : trial.getBlueLocations()) {
                        if (redAction.getLocationId().equalsIgnoreCase(location.getId())) {
                            int quadrant = tactic.getRedTacticQuadrant(
                                    location.getOsint() != null ? location.getOsint().getRedVulnerability_P() : 0,
                                    location.getImint() != null ? location.getImint().getRedOpportunity_U() : 0).ordinal();
                            int binIndex = findBinIndex(quadrant, tacticBins);
                            observedCounts[binIndex] = observedCounts[binIndex] + 1;
                        }
                    }
                }
            }
        }
        return observedCounts;
    }
    
    /**
     * Computes the total number of trials in each bin.
     * 
     * @param tactic
     * @param tacticBins
     * @param trials
     * @param numTrials
     * @return
     */
    public static long[] computeNumTrialsPerBin(RedTacticParameters tactic, 
            List<RedTacticQuadrantsBin> tacticBins,
            List<? extends IcarusTestTrial_Phase2> trials, int numTrials) {
        long[] trialsPerBin = new long[tacticBins.size()];
        if (trials != null && !trials.isEmpty() && numTrials > 0 && numTrials <= trials.size()) {
            Iterator<? extends IcarusTestTrial_Phase2> trialIter = trials.iterator();
            for (int i = 0; i < numTrials; i++) {
                IcarusTestTrial_Phase2 trial = trialIter.next();
                if (trial.getBlueLocations() != null && !trial.getBlueLocations().isEmpty()) {
                    for (BlueLocation location : trial.getBlueLocations()) {
                        int quadrant = tactic.getRedTacticQuadrant(
                                location.getOsint() != null ? location.getOsint().getRedVulnerability_P() : 0,
                                location.getImint() != null ? location.getImint().getRedOpportunity_U() : 0).ordinal();
                        int binIndex = findBinIndex(quadrant, tacticBins);
                        trialsPerBin[binIndex] = trialsPerBin[binIndex] + 1;
                    }
                }
            }
        }
        return trialsPerBin;
    }
    
    /**
     * Gets the bin the given Red tactics quadrant falls in.
     * 
     * @param quadrant
     * @param tacticBins
     * @return
     */
    protected static int findBinIndex(int quadrant, List<RedTacticQuadrantsBin> tacticBins) {        
        int i = 0;
        for(RedTacticQuadrantsBin bin : tacticBins) {
            if(bin.isQuadrantInBin(quadrant)) {
                return i;
            } else {
                i++;
            }
        }
        return -1;
    }    
    
    /**
     * Contains a Red tactics "bin". The bin specifies the indices of each
     * Red tactics quadrant that are included.
     */
    public static class RedTacticQuadrantsBin {        
        
        /** The indices of the quadrants to include in this bin */
        private final boolean[] quadrantInBin;
        
        /**
         *
         * @param quadrant
         */
        public RedTacticQuadrantsBin(RedTacticQuadrant quadrant) {
            this(new RedTacticQuadrant[] {quadrant});
        }
        
        /**
         *
         * @param quadrants
         */
        public RedTacticQuadrantsBin(RedTacticQuadrant[] quadrants) {
            quadrantInBin = new boolean[RedTacticQuadrant.values().length];
            if(quadrants != null) {
                for(RedTacticQuadrant quadrant : quadrants) {
                    quadrantInBin[quadrant.ordinal()] = true;
                }
            }
        }
        
        /**
         *
         * @return
         */
        public static RedTacticQuadrantsBin createAllQuadrantsBin() {
            return new RedTacticQuadrantsBin(RedTacticQuadrant.values());
        }
        
        /**
         *
         * @return
         */
        public static RedTacticQuadrantsBin createHighPBin() {
            return new RedTacticQuadrantsBin(new RedTacticQuadrant[] {
                RedTacticQuadrant.High_P_Large_U,
                RedTacticQuadrant.High_P_Small_U});
        }
        
        /**
         *
         * @return
         */
        public static RedTacticQuadrantsBin createLowPBin() {
            return new RedTacticQuadrantsBin(new RedTacticQuadrant[] {
                RedTacticQuadrant.Low_P_Large_U,
                RedTacticQuadrant.Low_P_Small_U});
        }
        
        /**
         *
         * @return
         */
        public static RedTacticQuadrantsBin createLargeUBin() {
            return new RedTacticQuadrantsBin(new RedTacticQuadrant[] {
                RedTacticQuadrant.High_P_Large_U,
                RedTacticQuadrant.Low_P_Large_U});
        }
        
        /**
         *
         * @return
         */
        public static RedTacticQuadrantsBin createSmallUBin() {
            return new RedTacticQuadrantsBin(new RedTacticQuadrant[] {
                RedTacticQuadrant.High_P_Small_U,
                RedTacticQuadrant.Low_P_Small_U});
        }
        
        /**
         *
         * @param quadrant
         * @return
         */
        public boolean isQuadrantInBin(RedTacticQuadrant quadrant) {
            return quadrant != null ? isQuadrantInBin(quadrant.ordinal()) : false;
        }
        
        /**
         *
         * @param quadrant
         * @return
         */
        public boolean isQuadrantInBin(int quadrant) {
            return quadrantInBin[quadrant];
        }        
    }
}
