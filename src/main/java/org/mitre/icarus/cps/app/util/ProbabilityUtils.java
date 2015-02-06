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
package org.mitre.icarus.cps.app.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.mitre.icarus.cps.assessment.score_computer.ScoreComputerBase.ProbabilityType;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.NormalizationConstraintType;

public class ProbabilityUtils {
	
	/**
	 * @param numProbs
	 * @return
	 */
	public static Integer getDefaultInitialProbability(int numProbs) {
		return numProbs > 0 ? 100/numProbs : 0;
	}
	
	/**
	 * @param numProbs
	 * @return
	 */
	public static Double getDefaultInitialProbability_Double(int numProbs) {
		return numProbs > 0 ? 100d/numProbs : 0d;
	}
	
	/**
	 * @param numProbs
	 * @return
	 */
	public static ArrayList<Integer> createDefaultInitialProbabilities(int numProbs) {
		if(numProbs > 0) {
			return createProbabilities(numProbs, 100/numProbs);
		}
		return null;
	}	
	
	/**
	 * @param numProbs
	 * @return
	 */
	public static ArrayList<Double> createDefaultInitialProbabilities_Double(int numProbs) {
		if(numProbs > 0) {
			ArrayList<Double> probs = new ArrayList<Double>(numProbs);
			double prob = 100d/numProbs;
			for(int i=0; i<numProbs; i++) {
				probs.add(prob);
			}
			return probs;
		}
		return null;
	}
	
	/**
	 * @param numProbs
	 * @param setting
	 * @return
	 */
	public static ArrayList<Integer> createProbabilities(int numProbs, Integer setting) {
		if(numProbs > 0) {
			ArrayList<Integer> probs = new ArrayList<Integer>(numProbs);
			for(int i=0; i<numProbs; i++) {
				probs.add(setting);
			}
			return probs;
		}
		return null;
	}
	
	/**
	 * @param numProbs
	 * @param setting
	 * @return
	 */
	public static ArrayList<Double> createProbabilities_Double(int numProbs, Double setting) {
		if(numProbs > 0) {
			ArrayList<Double> probs = new ArrayList<Double>(numProbs);
			for(int i=0; i<numProbs; i++) {
				probs.add(setting);
			}
			return probs;
		}
		return null;
	}
	
	/**
	 * Converts a probability in percent format to a probability in decimal format.
	 * 
	 * @param percentProb probability in percent format (Integer value in the range [0 100]).
	 * @return the probability in decimal format (Double value in the range [0 1]).
	 */
	public static Double convertPercentProbToDecimalProb(Integer percentProb) {
		return percentProb / 100.D;
	}
	
	/**
	 * Converts a probability in decimal format to a probability in percent format.
	 * 
	 * @param decimalProb probability in decimal format (Double value in the range [0 1]).
	 * @return the probability in percent format (Integer value in the range [0 100]).
	 */
	public static Integer convertDecimalProbToPercentProb(Double decimalProb) {
		return Math.round(decimalProb.floatValue() * 100);
	}
	
	/**
	 * Converts probabilities in percent format to probabilities in decimal format. 
	 * 
	 * @param percentProbs Probabilities in percent format. Integer values in the range [0 100].
	 * @return the probabilities in decimal format (Double values in the range [0 1]).
	 */
	public static ArrayList<Double> convertPercentProbsToDecimalProbs(List<Integer> percentProbs) {
		ArrayList<Double> decimalProbs = null;		
		if(percentProbs != null && !percentProbs.isEmpty()) {
			decimalProbs = new ArrayList<Double>(percentProbs.size());
			for(Integer prob : percentProbs) {
				decimalProbs.add(prob/100.D);
			}
		}
		return decimalProbs;
	}
	
	/**
	 * Converts probabilities in percent format to probabilities in decimal format. 
	 * 
	 * @param percentProbs Probabilities in percent format (Double values in the range [0 100]).
	 * @return the probabilities in decimal format (Double values in the range [0 1]).
	 */
	public static ArrayList<Double> convertPercentProbsToDecimalProbs_Double(List<Double> percentProbs) {
		ArrayList<Double> decimalProbs = null;		
		if(percentProbs != null && !percentProbs.isEmpty()) {
			decimalProbs = new ArrayList<Double>(percentProbs.size());
			for(Double prob : percentProbs) {
				decimalProbs.add(prob/100.D);
			}
		}
		return decimalProbs;
	}
       

    /**
     * Returns true if probabilities are in decimal and not percent format,
     * false otherwise.
     *
     * @param probs
     * @return
     */
    public static boolean checkIfDecimalProbs(List<Double> probs) {
        if (probs != null && !probs.isEmpty()) {
            for (Double prob : probs) {
                if (prob < 1.d && prob != 0.d) {
                    //Return true if the probabilitiy is < 1 and not equal to 0
                    return true;
                }
            }
        }
        return false;
    }
    
     /**
     * Returns true if the given probabilities list is not null, not empty, 
     * and contains no null values, or false otherwise.
     * 
     * @param probs
     * @return
     */
    public static boolean checkDecimalProbsNotNullOrEmpty(List<Double> probs) {
        if(probs != null && !probs.isEmpty()) {
            for(Double prob : probs) {
                if(prob == null) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    
    /**
     * Returns true if the given probabilities list is not null, not empty, 
     * and contains no null values, or false otherwise.
     * 
     * @param probs
     * @return
     */
    public static boolean checkPercentProbsNotNullOrEmpty(List<Integer> probs) {
        if(probs != null && !probs.isEmpty()) {
            for(Integer prob : probs) {
                if(prob == null) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
	
	/**
	 * @param probs
	 * @return
	 */
	public static int computeSum(List<Integer> probs) {
		int sum = 0;
		for(Integer probability : probs) {
			sum += probability != null ? probability : 0;
		}
		return sum;
	}
	
	/**
	 * @param probs
	 * @return
	 */
	public static double computeSum_Double(List<Double> probs) {
		double sum = 0;
		for(Double probability : probs) {
			sum += probability != null ? probability : 0;
		}
		return sum;
	}
	
	/**
	 * @param probs
	 * @return
	 */
	public static ArrayList<Integer> roundPercentProbabilities(List<Double> probs) {
		ArrayList<Integer> roundedProbs = new ArrayList<Integer>(probs.size());
		for(Double prob : probs) {
			roundedProbs.add((int)Math.round(prob));
		}
		normalizePercentProbabilities(roundedProbs, roundedProbs);
		return roundedProbs;
	}
	
	//public static void main(String[] args) {
	//	System.out.println(CPSUtils.roundProbabilities(probs));
	//}
	
	/**
	 * @param targetProbs
	 * @return
	 */
	public static ArrayList<Integer> cloneProbabilities(List<Integer> targetProbs) {
		if(targetProbs != null && !targetProbs.isEmpty()) {
			ArrayList<Integer> copy = new ArrayList<Integer>(targetProbs.size());
			for(int index=0; index < targetProbs.size(); index++) {
				copy.add(targetProbs.get(index));
			}
			return copy;
		} else {
			return null;
		}
	}
	
	/**
	 * @param targetProbs
	 * @return
	 */
	public static ArrayList<Double> cloneProbabilities_Double(List<Double> targetProbs) {
		if(targetProbs != null && !targetProbs.isEmpty()) {
			ArrayList<Double> copy = new ArrayList<Double>(targetProbs.size());
			for(int index=0; index < targetProbs.size(); index++) {
				copy.add(targetProbs.get(index));
			}
			return copy;
		} else {
			return null;
		}
	}
	
	/** Copy the values of target probs into destination probs */
	public static void copyProbabilities(List<Integer> targetProbs, List<Integer> destinationProbs) {
		for(int index=0; index < targetProbs.size(); index++) {
			destinationProbs.set(index, targetProbs.get(index));
		}
	}
	
	/** Copy the values of target probs into destination probs */
	public static void copyProbabilities_Double(List<Double> targetProbs, List<Double> destinationProbs) {
		for(int index=0; index < targetProbs.size(); index++) {
			destinationProbs.set(index, targetProbs.get(index));
		}
	}
	
	/** If given a 0, return epsilon.  If given a 100, return 100-epsilon, otherwise return prob */
	public static double normalizeProbability(int prob, double epsilon) {
		if(prob == 0) {
			return epsilon;
		}
		if(prob == 100) {
			return 100 - epsilon;
		}
		return prob;
	}
	
	/**
	 * Normalizes probabilities by making them actual probabilities instead of (probabilities*100).  
	 * Then, replaces any probabilities that are < epsilon with epsilon, or greater than 1-epsilon with
	 * 1-epsilon, and re-normalizes them.
	 * 
	 * @param probs the probabilities to normalize. Double values in the range [0 100].
	 * @param epsilon
	 * @return the normalized probabilities in the range [epsilon 1-epsilon]
	 */
	/*public static ArrayList<Double> normalizeProbabilities(ArrayList<Double> probs, double epsilon, ProbabilityType probType) {
		ArrayList<Double> normalizedProbs = new ArrayList<Double>(probs.size());		
		double sum = 0;
		for(Double prob : probs) {
			if(prob == null) { 
				return null;
			}
			double probD = prob * 0.01d;
			if(probD < epsilon) {
				probD = epsilon;
			}
			else if(probD > (1-epsilon)) {
				probD = 1-epsilon;
			}
			normalizedProbs.add(probD);			
			sum += probD;
		}		
		
		//Renormalize
		for(int i=0; i<normalizedProbs.size(); i++) {
			normalizedProbs.set(i, normalizedProbs.get(i)/sum);
		}
		return normalizedProbs;	
	}*/
	
	/**
	 * @param probs
	 * @param epsilon
	 * @param probType
	 * @return
	 */
	public static List<Double> normalizeProbabilities(List<Double> probs, List<Double> normalizedProbs, 
			double epsilon, ProbabilityType probType) {
		if(probType == ProbabilityType.Percent) {
			return normalizePercentProbabilities_Double(probs, normalizedProbs, epsilon);
		} else {
			return normalizeDecimalProbabilities(probs, normalizedProbs, epsilon);
		}
	}
	
	/**
	 * Normalizes subject probabilities by making them actual probabilities instead of (probabilities*100).  
	 * Then, replaces any subject probabilities that are < epsilon with epsilon, or greater than 1-epsilon with
	 * 1-epsilon, and re-normalizes them to ensure they sum to 1.
	 * 
	 * @param probs the subject probabilities (percent format) to normalize. Integer values in the range [0 100].
	 * @param normalizedProbs the normalized probabilities (decimal format) in the range [epsilon 1-epsilon]
	 * @param epsilon
	 * @return the normalized probabilities (decimal format) in the range [epsilon 1-epsilon]
	 */
	public static List<Double> normalizePercentProbabilities(List<Integer> probs, List<Double> normalizedProbs, double epsilon) {
		return normalizeDecimalProbabilities(ProbabilityUtils.convertPercentProbsToDecimalProbs(probs), 
				normalizedProbs, epsilon);
		/*if(normalizedProbs == null || normalizedProbs.size() != probs.size()) {
			normalizedProbs = createDefaultInitialProbabilities_Double(4);
		}		
		double sum = 0;
		for(Integer prob : probs) {
			if(prob == null) { 
				return null;
			}
			double probD = prob * 0.01d;
			if(probD < epsilon) {
				probD = epsilon;
			}
			else if(probD > (1-epsilon)) {
				probD = 1-epsilon;
			}
			normalizedProbs.add(probD);			
			sum += probD;
		}		
		
		//Renormalize
		for(int i=0; i<normalizedProbs.size(); i++) {
			normalizedProbs.set(i, normalizedProbs.get(i)/sum);
		}
		return normalizedProbs;*/
	}
	
	
	
	/**
	 * Normalizes probabilities (percent format) by making them decimal probabilities instead of (probabilities*100).  
	 * Then, replaces any subject probabilities that are < epsilon with epsilon, or greater than 1-epsilon with
	 * 1-epsilon, and re-normalizes them to ensure they sum to 1.
	 * 
	 * @param probs the probabilities (percent format) to normalize. Double values in the range [0 100].
	 * @param epsilon
	 * @return the normalized probabilities (decimal format) in the range [epsilon 1-epsilon]
	 */	
	public static List<Double> normalizePercentProbabilities_Double(List<Double> probs, List<Double> normalizedProbs, double epsilon) {
		return normalizeDecimalProbabilities(ProbabilityUtils.convertPercentProbsToDecimalProbs_Double(probs), 
				normalizedProbs, epsilon);
		/*ArrayList<Double> normalizedProbs = new ArrayList<Double>(probs.size());		
		double sum = 0;
		for(Double prob : probs) {
			if(prob == null) { 
				return null;
			}
			double probD = prob * 0.01d;
			if(probD < epsilon) {
				probD = epsilon;
			}
			else if(probD > (1-epsilon)) {
				probD = 1-epsilon;
			}
			normalizedProbs.add(probD);			
			sum += probD;
		}		
		
		//Renormalize
		for(int i=0; i<normalizedProbs.size(); i++) {
			normalizedProbs.set(i, normalizedProbs.get(i)/sum);
		}
		return normalizedProbs;*/
	}	
	
	/**
	 * Calculates normalized probabilities if they aren't normalized already. 
	 * Returns the amount away from 100 the original sum was.
	 * 
	 * @param currentProbs the probabilities (percent format) to normalize. Integer values in the range [0 100].
	 * @param normalizedProbs the normalized probabilities (percent format). Integer values in the range [0 100].
	 * @return
	 */
	public static int normalizePercentProbabilities(List<Integer> currentProbs, List<Integer> normalizedProbs) {
		return normalizePercentProbabilities(currentProbs, normalizedProbs, 100, NormalizationConstraintType.EqualTo);
	}
	
	/**
	 * Calculates normalized probabilities if they aren't normalized already. 
	 * Return the amount away from target sum the original sum was.
	 * 
	 * @param currentProbs the probabilities (percent format) to normalize. Integer values in the range [0 100].
	 * @param normalizedProbs  the normalized probabilities (percent format). Integer values in the range [0 100].
	 * @param targetSum the target sum (typically 100)
	 * @return
	 */
	public static int normalizePercentProbabilities(List<Integer> currentProbs, List<Integer> normalizedProbs, 
			Integer targetSum, NormalizationConstraintType normalizationConstraint) {
		int sum = computeSum(currentProbs);		
		targetSum = targetSum != null ? targetSum : 100;
		if(!isNormalizationConstraintMet(sum, targetSum, normalizationConstraint)) {		
			//Normalize probabilities
			int newSum = 0;			
			int index = 0;
			boolean all_0_or_100 = true;
			for(Integer probability : currentProbs) {
				int prob = (int)((float)probability/sum*100);
				if(prob != 0 && prob != 100) {
					all_0_or_100 = false;
				}
				normalizedProbs.set(index, prob);
				newSum += prob;
				index++;
			}
			
			index = 0;
			int underAmount = targetSum - newSum;
			for(; underAmount > 0; underAmount--) {
				int prob = normalizedProbs.get(index);
				if(all_0_or_100 || (prob > 0 && prob < 100)) {
					normalizedProbs.set(index, prob+1);
				} else {
					underAmount++;
				}
				index++;
				if(index > normalizedProbs.size() - 1) {
					index = 0;
				}
			}
		} else {
			//Probabilities are already normalized
			int index = 0;
			for(Integer probability : currentProbs) {
				normalizedProbs.set(index, probability);
				index++;
			}
		}
		
		return sum - targetSum;
	}
	
	protected static boolean isNormalizationConstraintMet(int sum, int targetSum, 
			NormalizationConstraintType normalizationConstraint) {
		if(normalizationConstraint == null || normalizationConstraint == NormalizationConstraintType.EqualTo) {
			return sum == targetSum;
		} else {
			return sum <= targetSum;
		}
	}
	
	/**
	 * 
	 * Calculates normalized probabilities if they aren't normalized already. 
	 * Returns the amount away from 100 the original sum was.
	 * 
	 * @param currentProbs the probabilities (percent format) to normalize. Double values in the range [0 100].
	 * @param normalizedProbs the normalized probabilities (percent format). Double values in the range [0 100].
	 * @return
	 */
	public static double normalizePercentProbabilities_Double(List<Double> currentProbs, List<Double> normalizedProbs) {
		double sum = computeSum_Double(currentProbs);
		
		double amountOff = Math.abs(sum-100.d);
		
		if(amountOff > 0.01d) {
			//Normalize probabilities
			double newSum = 0;			
			int index = 0;
			for(Double probability : currentProbs) {
				normalizedProbs.set(index, probability/sum*100.d);
				newSum += normalizedProbs.get(index);
				index++;
			}
			
			double underAmount = (100.d - newSum)/normalizedProbs.size();
			if(underAmount > 0) {
				//System.out.println(underAmount + ", " + newSum);
				for(index=0; index < normalizedProbs.size(); index++) {
					normalizedProbs.set(index, normalizedProbs.get(index)+underAmount);
				}
			}
		} else {
			//Probabilities are already normalized
			int index = 0;
			for(Double probability : currentProbs) {
				normalizedProbs.set(index, probability);
				index++;
			}
		}
		
		return amountOff;
	}
	
	/**
	 * Calculates normalized probabilities if they aren't normalized already. 
	 * Return the amount away from target sum the original sum was.
	 * 
	 * @param currentProbs the probabilities (percent format) to normalize. Double values in the range [0 100].
	 * @param normalizedProbs  the normalized probabilities (percent format). Double values in the range [0 100].
	 * @param targetSum the target sum (typically 100)
	 * @return
	 */
	public static double normalizePercentProbabilities_Double(List<Double> currentProbs, List<Double> normalizedProbs, 
			Double targetSum, NormalizationConstraintType normalizationConstraint) {
		double sum = computeSum_Double(currentProbs);		
		targetSum = targetSum != null ? targetSum : 100D;
		if(!isNormalizationConstraintMet_Double(sum, targetSum, normalizationConstraint)) {		
			//Normalize probabilities
			double newSum = 0;			
			int index = 0;
			for(Double probability : currentProbs) {
				normalizedProbs.set(index, probability/sum*100.d);
				newSum += normalizedProbs.get(index);
				index++;
			}			
			double underAmount = (targetSum - newSum)/normalizedProbs.size();
			if(underAmount > 0) {				
				for(index=0; index < normalizedProbs.size(); index++) {
					normalizedProbs.set(index, normalizedProbs.get(index)+underAmount);
				}
			}
		} else {
			//Probabilities are already normalized
			int index = 0;
			for(Double probability : currentProbs) {
				normalizedProbs.set(index, probability);
				index++;
			}
		}
		
		return sum - targetSum;
	}
	
	protected static boolean isNormalizationConstraintMet_Double(double sum, double targetSum, 
			NormalizationConstraintType normalizationConstraint) {		
		if(normalizationConstraint == null || normalizationConstraint == NormalizationConstraintType.EqualTo) {
			//retun sum == targetSum;
			return Math.abs(sum - targetSum) < 0.01d;
		} else {
			return sum <= targetSum;
		}
	}
	
	/**
	 * Calculates normalized probabilities if they aren't normalized already while attempting to leave any locked
	 * probabilities at their current values. Returns the amount away from 100 the original sum was.
	 * 
	 * @param currentProbs the probabilities (percent format) to normalize. Integer values in the range [0 100].
	 * @param normalizedProbs the normalized probabilities (percent format). Integer values in the range [0 100].
	 * @param lockSettings true/false Array indicating any probabilities that are currently locked
	 * @return
	 */
	public static int normalizePercentProbabilities(List<Integer> currentProbs, List<Integer> normalizedProbs, List<Boolean> lockSettings) {
		int sum = computeSum(currentProbs);
		
		int amountOff = Math.abs(sum-100);
		
		if(sum != 100) {
			//Normalize probabilities
			int lockedSum = 0;
			int unlockedSum = 0;
			boolean ignoringLocks = (lockSettings == null || lockSettings.size() != currentProbs.size());
			boolean allLocked = true;
			if(!ignoringLocks) {
				int index = 0;
				for(Integer probability : currentProbs) {
					if(lockSettings.get(index)) {
						lockedSum += probability;
					} else {
						unlockedSum += probability;
						allLocked = false;
					}
					index++;
				}
				if(lockedSum > 100 || (allLocked && lockedSum != 100)) {
					ignoringLocks = true;
				}
			}
			
			int newSum = 0;			
			int index = 0;
			boolean all_0_or_100 = true;
			for(Integer probability : currentProbs) {
				int prob = probability;
				if(!ignoringLocks) {
					if(!lockSettings.get(index)) {
						prob = (int)((float)probability/unlockedSum*(100-lockedSum));
						if(prob != 0 && prob != 100) {
							all_0_or_100 = false;
						}
					}
				} else {
					prob = (int)((float)probability/sum*100);
					if(prob != 0 && prob != 100) {
						all_0_or_100 = false;
					}
				}				
				normalizedProbs.set(index, prob);
				newSum += prob;
				index++;
			}
			
			index = 0;
			int underAmount = 100 - newSum;
			//System.out.println(underAmount + ", " + newSum);
			for(; underAmount > 0; underAmount--) {
				int prob = normalizedProbs.get(index);
				if((ignoringLocks || !lockSettings.get(index)) && (all_0_or_100 || (prob > 0 && prob < 100))) {
					normalizedProbs.set(index, prob+1);
				} else {
					underAmount++;
				}
				index++;
				if(index > normalizedProbs.size() - 1) {
					index = 0;
				}
			}
		} else {
			//Probabilities are already normalized
			int index = 0;
			for(Integer probability : currentProbs) {
				normalizedProbs.set(index, probability);
				index++;
			}
		}
		
		return amountOff;
	}
	 
	/**
	 * Normalize decimal probabilities.
	 * 
	 * @param currentProbs the probabilities (decimal format). Double values in the range [0 1].
	 * @param normalizedProbs the normalized probabilities (decimal format). Double values in the range [0 1].
	 */
	public static List<Double> normalizeDecimalProbabilities(List<Double> currentProbs, List<Double> normalizedProbs) {
		if(normalizedProbs == null || normalizedProbs.size() != currentProbs.size()) {
			normalizedProbs = createDefaultInitialProbabilities_Double(currentProbs.size());
		}
		double sum = computeSum_Double(currentProbs);
		int index = 0;
		for(Double currentProb : currentProbs) {
			normalizedProbs.set(index, currentProb/sum);
			index++;
		}
		return normalizedProbs;
	}
	
	/**
	 * Normalizes decimal probabilities by replacing any probabilities that are < epsilon with epsilon, 
	 * or greater than 1-epsilon with 1-epsilon, and re-normalizes them to ensure they sum to 1.
	 * 
	 * @param currentProbs the probabilities (decimal format). Double values in the range [0 1].
	 * @param normalizedProbs the normalized probabilities (decimal format). Double values in the range [epsilon 1-epsilon]
	 * @param epsilon
	 */
	public static List<Double> normalizeDecimalProbabilities(List<Double> currentProbs, List<Double> normalizedProbs, double epsilon) {
		if(normalizedProbs == null || normalizedProbs.size() != currentProbs.size()) {
			normalizedProbs = createDefaultInitialProbabilities_Double(currentProbs.size());
		}
		if(epsilon < 0) {epsilon = 0;}
		
		//Perform initial normalization
		LinkedList<Integer> epsilonBins = new LinkedList<Integer>();
		LinkedList<Integer> nonEpsilonBins = new LinkedList<Integer>();
		double sum = computeSum_Double(currentProbs);
		int index = 0;
		for(Double currentProb : currentProbs) {
			Double normalizedProb  = currentProb/sum;
			if(normalizedProb < epsilon) {
				//Replace normalized prob with epsilon
				normalizedProb = epsilon;
				epsilonBins.add(index);
			} else {
				nonEpsilonBins.add(index);
			}
			normalizedProbs.set(index, normalizedProb);			
			index++;
		}
		
		if(!epsilonBins.isEmpty()) {
			//Normalize probs again since some values were replace with epsilon
			double overAmt = computeSum_Double(normalizedProbs) - 1;
			if(overAmt > 0) {
				double nonEpsilonBinSum = 0;
				for(int index1 : nonEpsilonBins) {
					nonEpsilonBinSum += normalizedProbs.get(index1);
				}
				for(int index2 : nonEpsilonBins) {
					//normalizedProbs.set(index1, normalizedProbs.get(index1) - (overAmt/nonEpsilonBins.size()));
					double origProb = normalizedProbs.get(index2);
					normalizedProbs.set(index2, origProb - (overAmt * (origProb/nonEpsilonBinSum)));
				}
			}			
		}	
		
		return normalizedProbs;
	}	
	
	public static void main(String[] args) {
		//ArrayList<Double> currentProbs = new ArrayList<Double>(Arrays.asList(.001, .001, .998));
		ArrayList<Double> currentProbs = new ArrayList<Double>(Arrays.asList(0.2516, 0.5352, 0.0015, 0.2117));
		ArrayList<Double> normalizedProbs = createDefaultInitialProbabilities_Double(4);
		normalizeDecimalProbabilities(currentProbs, normalizedProbs, .01);
		System.out.println(normalizedProbs);
	}
}