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
package org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes;

/**
 * Contains a normalization constraint type and targetSum. Defines how a list of probabilities should
 * be normalized.
 * 
 * @author CBONACETO
 *
 */
public class NormalizationConstraint {
	
	/** The target targetSum of the probabilities of each hypothesis */
	protected Double targetSum;
	
	/** The normalization constraint with respect to the target targetSum */
	protected NormalizationConstraintType normalizationConstraint;
	
	/**
	 * Construct an empty NormalizationConstraint.
	 */
	public NormalizationConstraint() {}
	
	/**
	 * Construct a NormalizationConstraint with the given target sum and normalization constraint type.
	 * 
	 * @param targetSum the target sum of the probabilities (e.g., 100%).
	 * @param normalizationConstraint the normalization constraint with respect to the target sum (e.g., <=, =)
	 */
	public NormalizationConstraint(Double targetSum, NormalizationConstraintType normalizationConstraint) {
		this.targetSum = targetSum;
		this.normalizationConstraint = normalizationConstraint;
	}
	
	/**
	 * Create a default NormalizationConstraint with a target sum of 100% and constraint type of equal to.
	 * 
	 * @return a default NormalizationConstraint instance
	 */
	public static NormalizationConstraint createDefaultNormalizationConstraint() {
		return new NormalizationConstraint(100D, NormalizationConstraintType.EqualTo);
	}

	/**
	 * Get the target sum of the probabilities (e.g., 100%).
	 * 
	 * @return the target sum of the probabilities (e.g., 100%)
	 */
	public Double getTargetSum() {
		return targetSum;
	}

	/**
	 * Set the target sum of the probabilities (e.g., 100%).
	 * 
	 * @param targetSum the target sum of the probabilities (e.g., 100%)
	 */
	public void setTargetSum(Double targetSum) {
		this.targetSum = targetSum;
	}

	/**
	 * Get the normalization constraint with respect to the target sum (e.g., <=, =).
	 * 
	 * @return the normalization constraint with respect to the target sum (e.g., <=, =)
	 */
	public NormalizationConstraintType getNormalizationConstraint() {
		return normalizationConstraint;
	}

	/**
	 * Set the normalization constraint with respect to the target sum (e.g., <=, =).
	 * 
	 * @param normalizationConstraint the normalization constraint with respect to the target sum (e.g., <=, =)
	 */
	public void setNormalizationConstraint(NormalizationConstraintType normalizationConstraint) {
		this.normalizationConstraint = normalizationConstraint;
	}
	
	/**
	 * Get whether the given sum meets the normalization constraint specified in this class.
	 * 
	 * @param sum the sum of a list of probabilities
	 * @return whether the sum meets the normalization constraint
	 */
	public boolean isNormalizationConstraintMet(Double sum) {
		return isNormalizationConstraintMet(sum, targetSum, normalizationConstraint);
	}
	
	/**
	 * Get whether the given sum meets the normalization constraint specified with the given target
	 * sum and normalization constraint type.
	 * 
	 * @param sum the sum of a list of probabilities
	 * @param targetSum the target sum of the probabilities
	 * @param normalizationConstraint the normalization constraint with respect to the target sum (e.g., <=, =)
	 * @return
	 */
	public static boolean isNormalizationConstraintMet(Double sum, Double targetSum, 
			NormalizationConstraintType normalizationConstraint) {
		if(targetSum != null) {
			switch(normalizationConstraint) {
			case EqualTo:
				return sum != null ? sum.equals(targetSum): false;
			case LessThanOrEqualTo:
				return sum != null ? sum <= targetSum : false;				
			default:
				return false;
			}
		}	
		return false;
	}
}