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
package org.mitre.icarus.cps.assessment.score_computer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.mitre.icarus.cps.app.util.ProbabilityUtils;

/**
 * Base class for implementations that compute the subject/model score on a
 * trial.
 *
 * @author CBONACETO
 *
 */
public class ScoreComputerBase {

    /**
     * Probability format types
     */
    public static enum ProbabilityType {
        Percent, Decimal
    };

    /** Default value for epsilon (in decimal/probability format, percent is 1%) */
    public static final double EPSILON = 0.01d;

    protected static final double log2 = StrictMath.log(2);

    /**
     * Compute the score given subject probabilities and actual (Bayesian)
     * probabilities. The score is computed based on the KL Divergence between
     * the subject probabilities and actual probabilities using the formula:
     * Score = 100 * 2^(-KLD(actualProbs, subjectProbs))
     *
     * @param subjectProbs the subject probabilities. Integers values in the
     * range [0 100].
     * @param actualProbs the actual probabilities. Double values in the range
     * [0 1].
     * @param epsilon
     * @return the score
     */
    public static Double computeScore(List<Integer> subjectProbs, List<Double> actualProbs, double epsilon) {
        if (subjectProbs == null || subjectProbs.isEmpty()
                || actualProbs == null || actualProbs.isEmpty()
                || subjectProbs.size() != actualProbs.size()) {
            return null;
        }

        //Normalize subject probabilities
        List<Double> normalizedSubjectProbs = ProbabilityUtils.normalizePercentProbabilities(subjectProbs, null, epsilon);
        if (normalizedSubjectProbs == null) {
            return null;
        }

        //Normalize actual probabilities
        List<Double> normalizedActualProbs = ProbabilityUtils.normalizeDecimalProbabilities(actualProbs, null, epsilon);
        if (normalizedActualProbs == null) {
            return null;
        }

        //Compute and return the score
        return 100 * StrictMath.pow(2, -1 * computeKLDivergence(normalizedActualProbs, normalizedSubjectProbs));
    }

    /**
     * Computes symmetric KL divergence between two lists of probabilities using
     * the formula: KLD Symmetric = (KLD(probs1, probs2) + KLD(probs2, probs))/2
     *
     * @param probs1 the first probability list (decimal format). Double values
     * in the range (0 1).
     * @param probs2 the second probability list (decimal format). Double values
     * in the range (0 1).
     * @return the symmetric KL divergence
     */
    public static Double computeKLDivergenceSymmetric(List<Double> probs1, List<Double> probs2) {
        return (computeKLDivergence(probs1, probs2) + computeKLDivergence(probs2, probs1)) / 2;
    }

    /**
     * Computes KL divergence between two lists of probabilities.
     *
     * @param probs1 the first probability list (decimal format) (The "P"
     * distribution that is considered the true distribution that divergence is
     * measured from, e.g. normative probs). Double values in the range (0 1).
     * @param probs2 the second probability list (decimal format) (The "Q"
     * distribution, e.g. subject or model probs). Double values in the range (0
     * 1).
     * @return the KL divergence
     */
    public static Double computeKLDivergence(List<Double> probs1, List<Double> probs2) {
        double kld = 0;
        for (int i = 0; i < probs1.size(); i++) {
            double prob1 = probs1.get(i);
            double prob2 = probs2.get(i);
            if (prob2 > 0) {
                kld += prob1 * log2(prob1 / prob2);
            }
        }
        return kld;
    }

    /**
     * Computes KL divergence between two lists of probabilities using cross
     * entropy and entropy. This is equivalent to the computeKLDivergence
     * method.
     *
     * @param probs1 the first probability list (decimal format) (The "P"
     * distribution that is considered the true distribution that divergence is
     * measured from, e.g. normative probs). Double values in the range (0 1).
     * @param probs2 the second probability list (decimal format) (The "Q"
     * distribution, e.g. subject or model probs). Double values in the range (0
     * 1).
     * @return the KL divergence
     */
    protected static Double computeKLDivergenceInformationTheoretic(List<Double> probs1, List<Double> probs2) {
        double crossEntropy = 0;
        double entropy = 0;
        for (int i = 0; i < probs1.size(); i++) {
            double prob1 = probs1.get(i);
            double prob2 = probs2.get(i);
            crossEntropy += prob1 * log2(prob2);
            entropy += prob1 * log2(prob1);
        }
        return -crossEntropy + entropy;
    }

    /**
     * Computes the Jensen-Shannon divergence (JSD) between two lists of
     * probabilities using the formula: JSD = (KLD(probs1, M) + KLD(probs2,
     * M))/2, where M = 1/2(probs1 + probs2) [a distribution that is the average
     * of probs1 and probs2]
     *
     * @param probs1 the first probability list (decimal format). Double values
     * in the range (0 1).
     * @param probs2 the second probability list (decimal format). Double values
     * in the range (0 1).
     * @return the Jensen-Shannon divergence
     */
    public static Double computeJensenShannonDivergence(List<Double> probs1, List<Double> probs2) {
        ArrayList<Double> m = new ArrayList<Double>(probs1.size());
        for (int i = 0; i < probs1.size(); i++) {
            m.add((probs1.get(i) + probs2.get(i)) / 2);
        }
        return (computeKLDivergence(probs1, m) + computeKLDivergence(probs2, m)) / 2;
    }
       
    /**
     * Computes negentropy for the given probability distribution.
     *
     * @param probs the probabilities
     * @param probType
     * @return negentropy
     */
    public static double computeNegentropy(List<Double> probs, ProbabilityType probType) {
        double me = computeMaxEntropy(probs.size());
        double e = computeEntropy(probs, probType);
        return (me - e) / me;
    }

    /**
     * Computes entropy for the given probability distribution.
     *
     * @param probs the probabilities
     * @param probType
     * @return entropy
     */
    public static double computeEntropy(List<Double> probs, ProbabilityType probType) {
        List<Double> decProbs = (probType == ProbabilityType.Percent) ? ProbabilityUtils.convertPercentProbsToDecimalProbs_Double(probs) : probs;
        double e = 0;
        for (Double prob : decProbs) {
            e += prob * log2(prob);
        }
        return -e;
    }

    /**
     * Computes the maximum entropy of a probability distribution with the given
     * number of hypotheses.
     *
     * @param numHypotheses the number of hypotheses in the distribution
     * @return the maximum entropy of the distribution
     */
    public static double computeMaxEntropy(int numHypotheses) {
        if (numHypotheses <= 0) {
            return 0;
        } else {
            return computeEntropy(ProbabilityUtils.createProbabilities_Double(numHypotheses,
                    1.d / numHypotheses), ProbabilityType.Decimal);
        }
    }

    /**
     * Compute average score given array of double values.
     *
     * @param scores the scores
     * @return the average score
     */
    public static Double computeAverageScore(List<Double> scores) {
        if (scores == null || scores.isEmpty()) {
            return null;
        }
        Double sum = 0D;
        int numValues = 0;
        for (Double score : scores) {
            if (score != null) {
                sum += score;
                numValues++;
            }
        }
        return sum / numValues;
    }

    /**
     * Computes a base 2 log
     *
     * @param x
     * @return the base 2 log of x
     */
    public static double log2(double x) {
        if (x == 0) {
            return 0;
        }
        return StrictMath.log(x) / log2;
    }

    /**
     * Compute a Bayesian posterior given a prior and likelihood in percent
     * format.
     *
     * @param prior the prior (percent format)
     * @param likelihood the likelihood (percent format)
     * @return the posterior
     */
    public static int computePosterior(int prior, int likelihood) {
        float pl = (float) prior * likelihood;
        if (pl == 0) {
            return 0;
        } else {
            return Math.round(pl / (pl + (100 - prior) * (100 - likelihood)) * 100);
        }
    }

    /**
     * Compute a Bayesian posterior given a prior and likelihood in decimal
     * format.
     *
     * @param prior the prior (decimal format)
     * @param likelihood the likelihood (decimal format)
     * @return the posterior (percent format)
     */
    public static double computePosterior_Double(double prior, double likelihood) {
        double pl = prior * likelihood;
        if (pl == 0) {
            return 0;
        } else {
            return pl / (pl + (1.d - prior) * (1.d - likelihood)) * 100.d;
        }
    }

    /**
     * Computes n!.
     *
     * @param n an integer
     * @return n!
     */
    public static double factorial(long n) {
        double fac = 1.d;
        for (long i = 1; i <= n; i++) {
            fac = fac * i;
        }
        return fac;
    }

    public static void main(String[] args) {
		//ArrayList<Double> humanProbs = new ArrayList<Double>(
        //		Arrays.asList(13D, 1D, 73D, 13D));
        //ArrayList<Double> modelProbs = new ArrayList<Double>(
        //		Arrays.asList(13D, 0D, 73D, 14D));

        System.out.println("5! = " + factorial(5));

        ArrayList<Double> humanProbs = new ArrayList<Double>(Arrays.asList(22.54, 58.12, 6.43, 12.91));
        ArrayList<Double> modelProbs = new ArrayList<Double>(Arrays.asList(25.16, 53.52, 0.15, 21.17));
        System.out.println(computeJensenShannonDivergence(humanProbs, modelProbs));
        System.out.println(computeJensenShannonDivergence(modelProbs, humanProbs));
		//System.out.println("Human Probs: " + humanProbs + ", Model Probs: " + modelProbs + ", RSR: " + computeRSR(humanProbs, modelProbs));

        /*ArrayList<Integer> subjectProbs = new ArrayList<Integer>(Arrays.asList(20, 10, 10, 60));
         ArrayList<Double> actualProbs1 = new ArrayList<Double>(Arrays.asList(28.78681212029385, 0.7569108478492069, 2.370834995767765E-8, 70.45627700814859));
         System.out.println(ScoreComputerBase.computeScore(subjectProbs, actualProbs1));*/
        ArrayList<Integer> subjectProbs = new ArrayList<Integer>(Arrays.asList(0, 25, 25, 25));
        ArrayList<Double> actualProbs1 = new ArrayList<Double>(Arrays.asList(.50D, .25D, .25D, 0.01D));
        System.out.println(ScoreComputerBase.computeKLDivergence(
                ProbabilityUtils.normalizePercentProbabilities(subjectProbs, null, ScoreComputerBase.EPSILON), actualProbs1));
        System.out.println(ScoreComputerBase.computeKLDivergenceInformationTheoretic(
                ProbabilityUtils.normalizePercentProbabilities(subjectProbs, null, ScoreComputerBase.EPSILON), actualProbs1));

        ArrayList<Integer> subjectProbs2 = new ArrayList<Integer>(Arrays.asList(25, 53, 1, 21));
        ArrayList<Double> currentProbs = new ArrayList<Double>(Arrays.asList(0.2516, 0.5352, 0.0015, 0.2117));
        System.out.println("Score: " + ScoreComputerBase.computeScore(subjectProbs2, currentProbs, 0.01));

        /*ArrayList<Integer> modelProbsInts = new ArrayList<Integer>(Arrays.asList(11, 89))
         ArrayList<Double> actualProbs2 = new ArrayList<Double>(Arrays.asList(64.4692D, 35.5038D));
         System.out.println(ScoreComputerBase.computeScore(modelProbsInts, actualProbs2));
         ScoreComputer sc = new ScoreComputer();
         System.out.println(sc.computeProbabilitiesScoreS1(modelProbsInts, actualProbs2));
         System.out.println(.11 * log2(.11/.644962) + .89 * log2(.89/.355038)); 
         System.out.println(.644962 * log2(.644962/.11) + .355038 * log2(.355038/.89));*/
        /*ArrayList<Double> probs1 = normalizeProbabilities(new ArrayList<Double>(Arrays.asList(33D, 67D, 0D, 0D)), EPSILON);
         ArrayList<Double> probs2 = normalizeProbabilities(new ArrayList<Double>(Arrays.asList(32.99999999999999, 67.0, 0.0, 0.0)), EPSILON);
         System.out.println(ScoreComputer.computeKLDivergence(probs1, probs2) + ", " + probs1 + ", " + probs2);*/
        /*System.out.println(normalizeProbabilities(new ArrayList<Double>(
         Arrays.asList(100D, 0D, 0D, 0D)), EPSILON));
         System.out.println(computeNegentropy(new ArrayList<Double>(
         Arrays.asList(99.99D, .001D, .001D, .001D))));
         System.out.println(computeMaxEntropy(4));*/
    }
}
