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
package org.mitre.icarus.cps.assessment.assessment_metrics_computer.common;

import java.util.ArrayList;
import java.util.List;

import org.mitre.icarus.cps.app.util.ProbabilityUtils;
import org.mitre.icarus.cps.assessment.assessment_metrics_computer.phase_1.MetricsUtils;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.CPAMetric;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.MetricInfo;
import org.mitre.icarus.cps.assessment.score_computer.ScoreComputerBase;

/**
 * Computes ASR (Absolute Success Rate) and RSR (Relative Success Rate) metrics.
 *
 * @author CBONACETO
 */
public class ASRandRSRComputer {
    /**
     * Computes the standard version of RSR (Relative Success Rate).
     * 
     * @param modelProbs
     * @param comparisonProbs
     * @param RSR     
     * @param RSR_info
     * @param taskNum
     * @param trialNum
     * @param stage
     * @return
     */
    public CPAMetric computeRSR_Standard(List<Double> modelProbs, List<Double> comparisonProbs,
            CPAMetric RSR, MetricInfo RSR_info,
            Integer taskNum, Integer trialNum, Integer stage) {
        return computeRSR_Standard(computeSimilarityToComparisonProbs(modelProbs, comparisonProbs),
                computeSimilarityToUniformProbs(comparisonProbs),
                RSR, RSR_info, taskNum, trialNum, stage);
    }

    /**
     * Computes the standard version of RSR (Relative Success Rate).
     * 
     * @param Spm
     * @param Spr
     * @param RSR
     * @param RSR_info
     * @param taskNum
     * @param trialNum
     * @param stage
     * @return
     */
    public CPAMetric computeRSR_Standard(Double Spm, Double Spr, CPAMetric RSR, MetricInfo RSR_info,
            Integer taskNum, Integer trialNum, Integer stage) {
        if (RSR == null) {
            RSR = new CPAMetric(RSR_info.getName());
        }

        Double rsr = computeRSR(Spm, Spr);
        RSR.score = rsr;
        if (rsr != null) {
            RSR.pass = rsr >= RSR_info.getTrial_pass_threshold();
        } else {
            RSR.pass = null;
        }
        RSR.assessed = RSR_info.isAssessedForStage(taskNum, trialNum, stage);
		//DEBUG CODE
		/*if(!RSR.isAssessed()) {
         System.out.println("Skipping RSR for " + taskNum + "-" + trialNum + "-" + (stage+1));
         }*/
        /////
        return RSR;
    }

    /**
     * Computes a version of RSR where the Bayesian probabilities are used for
     * the null model probabilities instead of the uniform probabilities.
     * 
     * @param modelProbs
     * @param comparisonProbs
     * @param normativeProbs
     * @param RSR_Bayesian
     * @param RSR_Bayesian_info
     * @param taskNum
     * @param trialNum
     * @param stage
     * @return
     */
    public CPAMetric computeRSR_Bayesian(List<Double> modelProbs, List<Double> comparisonProbs, List<Double> normativeProbs,
            CPAMetric RSR_Bayesian, MetricInfo RSR_Bayesian_info, Integer taskNum, Integer trialNum, Integer stage) {
        return computeRSR_Bayesian(computeSimilarityToComparisonProbs(modelProbs, comparisonProbs),
                computeSimilarityToComparisonProbs(comparisonProbs, normativeProbs),
                RSR_Bayesian, RSR_Bayesian_info, taskNum, trialNum, stage);
    }
   
    /**
     * Computes a version of RSR where the Bayesian probabilities are used for
     * the null model probabilities instead of the uniform probabilities.
     * 
     * @param Spm
     * @param Spq
     * @param RSR_Bayesian
     * @param RSR_Bayesian_info
     * @param taskNum
     * @param trialNum
     * @param stage
     * @return
     */
    public CPAMetric computeRSR_Bayesian(Double Spm, Double Spq, CPAMetric RSR_Bayesian, MetricInfo RSR_Bayesian_info,
            Integer taskNum, Integer trialNum, Integer stage) {
        if (RSR_Bayesian == null) {
            RSR_Bayesian = new CPAMetric(RSR_Bayesian_info.getName());
        }

        Double rsr = computeRSR(Spm, Spq);
        RSR_Bayesian.score = rsr;
        if (rsr != null) {
            RSR_Bayesian.pass = rsr >= RSR_Bayesian_info.getTrial_pass_threshold();
        } else {
            RSR_Bayesian.pass = null;
        }
        RSR_Bayesian.assessed = RSR_Bayesian_info.isAssessedForStage(taskNum, trialNum, stage);

        return RSR_Bayesian;
    }

    /**
     * Computes RSR alternative 1, which is RSR-RMSE (Root Mean Squared Error).
     * 
     * @param modelProbs
     * @param comparisonProbs
     * @param normativeProbs
     * @param RSR_alt_1
     * @param RSR_alt_1_info
     * @param taskNum
     * @param trialNum
     * @param stage
     * @return
     */
    public CPAMetric computeRSR_alt_1(List<Double> modelProbs, List<Double> comparisonProbs, List<Double> normativeProbs,
            CPAMetric RSR_alt_1, MetricInfo RSR_alt_1_info, Integer taskNum, Integer trialNum, Integer stage) {
        if (RSR_alt_1 == null) {
            RSR_alt_1 = new CPAMetric(RSR_alt_1_info.getName());
        }

        Double rsr = computeRSR_RMSE(modelProbs, comparisonProbs);
        RSR_alt_1.score = rsr;
        if (rsr != null) {
            RSR_alt_1.pass = rsr >= RSR_alt_1_info.getTrial_pass_threshold();
        } else {
            RSR_alt_1.pass = null;
        }
        RSR_alt_1.assessed = RSR_alt_1_info.isAssessedForStage(taskNum, trialNum, stage);

        return RSR_alt_1;
    }

    /**
     * Computes RSR alternative 2, which is RSR-RMSE(Bayesian) (Root Mean Squared Error).
     * 
     * @param modelProbs
     * @param comparisonProbs
     * @param normativeProbs
     * @param RSR_alt_2
     * @param RSR_alt_2_info
     * @param taskNum
     * @param trialNum
     * @param stage
     * @return
     */
    public CPAMetric computeRSR_alt_2(List<Double> modelProbs, List<Double> comparisonProbs, List<Double> normativeProbs,
            CPAMetric RSR_alt_2, MetricInfo RSR_alt_2_info, Integer taskNum, Integer trialNum, Integer stage) {
        if (RSR_alt_2 == null) {
            RSR_alt_2 = new CPAMetric(RSR_alt_2_info.getName());
        }

        Double rsr = computeRSR_RMSE(modelProbs, comparisonProbs, normativeProbs);
        RSR_alt_2.score = rsr;
        if (rsr != null) {
            RSR_alt_2.pass = rsr >= RSR_alt_2_info.getTrial_pass_threshold();
        } else {
            RSR_alt_2.pass = null;
        }
        RSR_alt_2.assessed = RSR_alt_2_info.isAssessedForStage(taskNum, trialNum, stage);

        return RSR_alt_2;
    }

    /**
     * Computes the similarity of model probabilities to human probabilities
     * using Kullback-Liebler Divergence (KLD).
     * 
     * @param modelProbs
     * @param humanProbs
     * @return
     */
    public Double computeSpm(List<Double> modelProbs, List<Double> humanProbs) {
        return computeSimilarityToComparisonProbs(modelProbs, humanProbs);
    }

    /**
     * Computes the similarity of human probabilities to uniform probabilities
     * using Kullback-Liebler Divergence (KLD).
     * 
     * @param humanProbs
     * @return
     */
    public Double computeSpr(List<Double> humanProbs) {
        return computeSimilarityToUniformProbs(humanProbs);
    }

    /**
     * Computes the similarity of human probabilities to Bayesian probabilities
     * using Kullback-Liebler Divergence (KLD).
     * 
     * @param humanProbs
     * @param normativeProbs
     * @return
     */
    public Double computeSpq(List<Double> humanProbs, List<Double> normativeProbs) {
        return computeSimilarityToComparisonProbs(normativeProbs, humanProbs);
    }

    

    /**
     * Computes the similarity of the given probabilities to uniform probabilities
     * using Kullback-Liebler Divergence (KLD).
     * 
     * @param comparisonProbs
     * @return
     */
    public Double computeSimilarityToUniformProbs(List<Double> comparisonProbs) {
        if (comparisonProbs != null && !comparisonProbs.isEmpty()) {
            //Compute similarity of comparisonProbs to uniform probability distribution
            ArrayList<Double> uniformProbs = ProbabilityUtils.createProbabilities_Double(comparisonProbs.size(), 1.d / comparisonProbs.size());
            return computeSimilarityToComparisonProbs(uniformProbs, comparisonProbs);
        }
        return null;
    }

    /**
     * Computes the similarity of model probabilities to the given comparison 
     * probabilities using Kullback-Liebler Divergence (KLD).
     * 
     * @param modelProbs
     * @param comparisonProbs
     * @return
     */
    public Double computeSimilarityToComparisonProbs(List<Double> modelProbs, List<Double> comparisonProbs) {
        if (modelProbs != null && !modelProbs.isEmpty() && comparisonProbs != null
                && modelProbs.size() == comparisonProbs.size()) {
            //Compute similarity of modelProbs to comparisonProbs            
            return 100d * StrictMath.pow(2, -1 * ScoreComputerBase.computeKLDivergence(comparisonProbs, modelProbs));
        }
        return null;
    }

    /**
     * Computes the standard version of RSR (Relative Success Rate), which is
     * (Spm-Spr)/(100-Spr) * 100.
     * 
     * @param modelProbs
     * @param humanProbs
     * @return
     */
    public Double computeRSR(List<Double> modelProbs, List<Double> humanProbs) {
        //RSR = (Spm-Spr)/(100-Spr) * 100
        return computeRSR(computeSimilarityToComparisonProbs(modelProbs, humanProbs),
                computeSimilarityToUniformProbs(humanProbs));
    }

    /**
     * Computes RSR (Relative Success Rate) using the given
     * null model probabilities, which is RSR = (Spm-Spn)/(100-Spn) * 100.
     * 
     * @param modelProbs
     * @param humanProbs
     * @param nullModelProbs
     * @return
     */
    public Double computeRSR(List<Double> modelProbs, List<Double> humanProbs, List<Double> nullModelProbs) {
        //RSR = (Spm-Spn)/(100-Spn) * 100
        return computeRSR(computeSimilarityToComparisonProbs(modelProbs, humanProbs),
                computeSimilarityToComparisonProbs(nullModelProbs, humanProbs));
    }

    /**
     * Computes RSR (Relative Success Rate) using the given values of Spm and Spn
     * values, which is RSR = (Spm-Spn)/(100-Spn) * 100.
     * 
     * @param Spm
     * @param Spn
     * @return
     */
    public Double computeRSR(Double Spm, Double Spn) {
        if (Spm != null && Spn != null) {
            //RSR = (Spm-Spn)/(100-Spn) * 100
            Double rsr = Math.max(0d, (Spm - Spn) / (100 - Spn) * 100d);
            return !rsr.isNaN() ? rsr : 0d;
            //return Math.max(0d, (Spm - Spn)/(100 - Spn) * 100d);
        }
        return null;
    }

    /** 
     * Computes RSR using the RMSE (Root Mean Squared Error) method.
     * 
     * @param modelProbs
     * @param humanProbs
     * @return
     */
    public Double computeRSR_RMSE(List<Double> modelProbs, List<Double> humanProbs) {
        if (humanProbs != null && !humanProbs.isEmpty()) {
            return computeRSR_RMSE(modelProbs, humanProbs,
                    ProbabilityUtils.createProbabilities_Double(humanProbs.size(), 1.d / humanProbs.size()));
        }
        return null;
    }

    /**
     * Computes RSR using the RMSE (Root Mean Squared Error) method.
     * 
     * @param modelProbs
     * @param humanProbs
     * @param nullModelProbs
     * @return
     */
    public Double computeRSR_RMSE(List<Double> modelProbs, List<Double> humanProbs, List<Double> nullModelProbs) {
        if (modelProbs != null && !modelProbs.isEmpty() && humanProbs != null && nullModelProbs != null
                && modelProbs.size() == humanProbs.size() && modelProbs.size() == nullModelProbs.size()) {
            //Compute RMSE of model to human
            Double rmseModelToHuman = MetricsUtils.computeRMSE(modelProbs, humanProbs);

            //Compute RMSE of null model to human		
            Double rmseNullModelToHuman = MetricsUtils.computeRMSE(nullModelProbs, humanProbs);

            //Compute RER(RMSE) as RMSE(model-to-human) / RMSE(null model-to-human)
            //TODO: Fix this
            Double rerRmse = 100d * (rmseModelToHuman / rmseNullModelToHuman);

            //Compute RSR(RMSE) as 100% - RER(RMSE)
            Double rsr_rmse = Math.max(0, 100d - rerRmse);
            return !rsr_rmse.isNaN() ? rsr_rmse : 0d;            
        }
        return null;
    }

    /**
     * Computes ASR (Absolute Success Rate) as max[0%, (100% * numHypotheses * RMS) ], 
     * where RMS = Root Mean Squared Error (human-model).
     * 
     * @param modelProbs
     * @param comparisonProbs
     * @param ASR
     * @param ASR_info
     * @param taskNum
     * @param trialNum
     * @param stage
     * @return
     */
    public CPAMetric computeASR(List<Double> modelProbs, List<Double> comparisonProbs,
            CPAMetric ASR, MetricInfo ASR_info, Integer taskNum, Integer trialNum, Integer stage) {
        if (ASR == null) {
            ASR = new CPAMetric(ASR_info.getName());
        }

        Double asr = computeASR(modelProbs, comparisonProbs);
        ASR.score = asr;
        if (asr != null) {
            ASR.pass = asr >= ASR_info.getTrial_pass_threshold();
        } else {
            ASR.pass = null;
        }
        ASR.assessed = ASR_info.isAssessedForStage(taskNum, trialNum, stage);

        return ASR;
    }

    /**
     * Computes ASR (Absolute Success Rate) as max[0%, (100% * numHypotheses * RMS) ], 
     * where RMS = Root Mean Squared Error (human-model).
     *
     * @param modelProbs
     * @param humanProbs
     * @return ASR of model probs to human probs
     */
    public Double computeASR(List<Double> modelProbs, List<Double> humanProbs) {
        //ASR = max[0%, (100% * numHypotheses * RMS) ],
        // where: RMS = Root Mean Squared error (human-model)
        if (modelProbs != null && !modelProbs.isEmpty() && humanProbs != null
                && modelProbs.size() == humanProbs.size()) {
            //Compute RMSE of human to model
            Double rmseHumanToModel = MetricsUtils.computeRMSE(humanProbs, modelProbs);

            //Compute ASR
            double rmseMultiplier = computeRmsMultiplierForASR(modelProbs.size());
            Double asr = 100.d * Math.max(0, 1 - rmseMultiplier * rmseHumanToModel);
            return !asr.isNaN() ? asr : 0d;
        }
        return null;
    }

    /**
     * Computes the RMSE multiplier to use in ASR calculations based on the number
     * of possible hypotheses.
     * 
     * @param numHypotheses
     * @return
     */
    public double computeRmsMultiplierForASR(int numHypotheses) {
        if (numHypotheses > 1) {
            //Compute the RMS difference between the maximum and minimum entropy hypotheses
            ArrayList<Double> maxEntropyDist = new ArrayList<Double>(numHypotheses);
            ArrayList<Double> minEntropyDist = new ArrayList<Double>(numHypotheses);
            double maxEntropyProb = 1.d / numHypotheses;
            for (int i = 0; i < numHypotheses; i++) {
                maxEntropyDist.add(maxEntropyProb);
                minEntropyDist.add(i == 0 ? 1d : 0d);
            }
            return 1d / MetricsUtils.computeRMSE(maxEntropyDist, minEntropyDist);
        } else {
            return 1d;
        }
    }
}
