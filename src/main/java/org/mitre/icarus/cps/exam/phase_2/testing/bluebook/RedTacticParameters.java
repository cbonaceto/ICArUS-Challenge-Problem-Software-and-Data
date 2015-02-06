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
package org.mitre.icarus.cps.exam.phase_2.testing.bluebook;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlType;

/**
 * Contains the probabilities that Red will attack for each permutation of "P" and "U" values. 
 * Probabilities are in decimal and not percent format.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="RedTacticParameters", namespace="IcarusCPD_2")
public class RedTacticParameters implements Cloneable {	
	
	/**
	 * An enumeration containing each permutation of "P" (High/Low) and "U" (Large/Small).
	 * 
	 * @author CBONACETO
	 *
	 */
	@XmlType(name="RedTacticQuadrant", namespace="IcarusCPD_2")	
	public static enum RedTacticQuadrant {
		High_P_Small_U, High_P_Large_U,  
		Low_P_Small_U, Low_P_Large_U
	}	
	
	/**
	 * An enumeration containing the data Red may consider, including:	 * 
	 * P_Only: 
	 * 		Red only considers P (and not also U). 
	 * 		The High_P_Small_U attack probability will be the same as the High_P_Large_U attack probability, and
	 *		the Low_P_Small_U attack probability will be the same as the Low_P_Large_U attack probability.   
	 * U_Only: 
	 * 		Red only considers U (and not also P).
	 * 		The High_P_Large_U attack probability will be the same as the Low_P_Large_U attack probability, and
	 *		the High_P_Small_U attack probability will be the same as the Low_P_Small_U attack probability.
	 * P_And_U: 
	 * 		Red considers both P and U.
	 * 
	 * @author CBONACETO
	 *
	 */
	@XmlType(name="RedTacticConsiderationData", namespace="IcarusCPD_2")
	public static enum RedTacticConsiderationData {
		P_Only, U_Only, P_And_U
	}
	
	/** The default High P threshold */
	public static final double DEFAULT_HIGH_P_THRESHOLD = 0.25d;	
	
	/** The default Large U threshold */
	public static final Integer DEFAULT_LARGE_U_THRESHOLD = 3;	

	/** The attack probabilities for each case:
	 * 0: High P, Small U
	 * 1: High P, Large U
	 * 2: Low P, Small U
	 * 3: Low P, Large U */
	protected List<Double> attackProbabilities;
	
	/** The types of data Red considers (P_Only, U_Only, or P_And_U) */
	protected RedTacticConsiderationData dataConsidered;
	
	/** The threshold for what Red considers High P. Anything
	 * > this threshold is considered High P. */
	protected Double high_P_Threshold;	
	
	/** The threshold for what Red considers Large U. Anything
	 * > this threshold is considered Large U. */
	protected Integer large_U_Threshold;	
	
	/**
	 * Construct an empty RedTacticParameters.
	 */
	public RedTacticParameters() {}
	
	/**
	 * Construct a RedTacticParameters with the given attack probabilities and thresholds for High "P" and Low "U".
	 * 
	 * @param dataConsidered The types of data Red considers (P_Only, U_Only, or P_And_U)
	 * @param high_P_Small_U_Probabilitiy the Red attack probability when P is High and U is Small
	 * @param high_P_Large_U_Probabilitiy the Red attack probability when P is High and U is Large
	 * @param low_P_Small_U_Probabilitiy the Red attack probability when P is Low and U is Small
	 * @param low_P_Large_U_Probabilitiy the Red attack probability when P is Low and U is Large
	 * @param high_P_Threshold The threshold for high P. P values > this threshold are considered High, and values <= the threshold are Low.
	 * @param large_U_Threshold The threshold for large U. U values > this threshold are considered Large, and values <= the threshold are Small.
	 */	
	public RedTacticParameters(RedTacticConsiderationData dataConsidered, 
			Double high_P_Small_U_Probabilitiy, Double high_P_Large_U_Probabilitiy,
			Double low_P_Small_U_Probabilitiy, Double low_P_Large_U_Probabilitiy,
			Double high_P_Threshold, Integer large_U_Threshold) {
		this(dataConsidered, Arrays.asList(high_P_Small_U_Probabilitiy, high_P_Large_U_Probabilitiy,
				low_P_Small_U_Probabilitiy, low_P_Large_U_Probabilitiy),
			high_P_Threshold, large_U_Threshold);
	}	
	
	/**
	 * Construct a RedTacticParameters with the given attack probabilities and thresholds for High "P" and Low "U".
	 * 
	 * @param dataConsidered The types of data Red considers (P_Only, U_Only, or P_And_U)
	 * @param attackProbabilities An ordered list containing Red attack probabilities of the form:
	 *   0: High P, Small U
	 *   1: High P, Large U
	 *   2: Low P, Small U
	 *   3: Low P, Large U	 *   
	 * @param high_P_Threshold The threshold for high P. P values > this threshold are considered high, and values <= the threshold are low.
	 * @param large_U_Threshold The threshold for large U. U values > this threshold are considered large, and values <= the threshold are small.
	 */
	public RedTacticParameters(RedTacticConsiderationData dataConsidered,
			List<Double> attackProbabilities,
			Double high_P_Threshold, Integer large_U_Threshold) {
		this.dataConsidered = dataConsidered;
		this.attackProbabilities = attackProbabilities;
		this.high_P_Threshold = high_P_Threshold;
		this.large_U_Threshold = large_U_Threshold;
	}
	
	/**
	 * The copy constructor.
	 * 
	 * @param copy a RedTacticParameters instance to copy member variable values from.
	 */
	public RedTacticParameters(RedTacticParameters copy) {
		this.dataConsidered = copy.dataConsidered;
		this.high_P_Threshold = copy.high_P_Threshold;
		this.large_U_Threshold = copy.large_U_Threshold;
		if(copy.attackProbabilities != null) {
			this.attackProbabilities = new ArrayList<Double>();
			for(Double prob : copy.attackProbabilities) {
				this.attackProbabilities.add(prob);
			}
		}
	}
	
	/**
	 * Get the Red attack probabilities.
	 * 
	 * @return An ordered list containing Red attack probabilities of the form:
	 *   0: High P, Small U
	 *   1: High P, Large U
	 *   2: Low P, Small U
	 *   3: Low P, Large U
	 */
	@XmlElement(name="AttackProbabilities")
	@XmlList
	public List<Double> getAttackProbabilities() {
		return attackProbabilities;
	}

	/**
	 * Set the Red attack probabilities.
	 * 
	 * @param attackProbabilities An ordered list containing Red attack probabilities of the form:
	 *   0: High P, Small U
	 *   1: High P, Large U
	 *   2: Low P, Small U
	 *   3: Low P, Large U
	 */
	public void setAttackProbabilities(List<Double> attackProbabilities) {
		this.attackProbabilities = attackProbabilities;
	}
	
	/**
	 * Convenience method to get the attack probability given values of P and U based on the
	 * high_P_Threshold and large_U_Threshold.
	 * 
	 * @param p a "P" value
	 * @param u a "U" value
	 * @return the Red attack probability for the "P" and "U" value
	 */
	public Double getAttackProbability(Double p, Integer u) {
		return getAttackProbability(getRedTacticQuadrant(p, u));
	}
	
	/**
	 * Get the quadrant for the given values of P and U.
	 * 
	 * @param p a "P" value
	 * @param u a "U" value
	 * @return the quadrant for the given values of P and U
	 */
	public RedTacticQuadrant getRedTacticQuadrant(Double p, Integer u) {
		boolean highP = p != null && p > high_P_Threshold;
		boolean largeU = u != null && u > large_U_Threshold;
		return getRedTacticQuadrant(highP, largeU);
	}
	
	/**
	 * Get the quadrant for the given values of P and U.
	 * 
	 * @param p a "P" value
	 * @param u a "U" value
	 * @param high_P_Threshold the threshold for high P
	 * @param large_U_Threshold The threshold for large U
	 * @return the quadrant for the given values of P and U
	 */
	public static RedTacticQuadrant getRedTacticQuadrant(Double p, Integer u, 
			Double high_P_Threshold, Integer large_U_Threshold) {
		boolean highP = p != null && p > high_P_Threshold;
		boolean largeU = u != null && u > large_U_Threshold;
		return getRedTacticQuadrant(highP, largeU);
	}
	
	protected static RedTacticQuadrant getRedTacticQuadrant(boolean highP, boolean largeU) {
		if(highP) {
			if(largeU) {
				//High P, Large U
				return RedTacticQuadrant.High_P_Large_U;
			} else {
				//High P, Small U
				return RedTacticQuadrant.High_P_Small_U;
			}
		} else {
			if(largeU) {
				//Low P, Large U
				return RedTacticQuadrant.Low_P_Large_U;
			} else {
				//Low P, Small U
				return RedTacticQuadrant.Low_P_Small_U;
			}
		}
	}

	/**
	 * Convenience method to get the Red attack probability for the given quadrant.
	 * 
	 * @param quadrant the quadrant
	 * @return the Red attack probability for the quadrant
	 */
	public Double getAttackProbability(RedTacticQuadrant quadrant) {
		if(quadrant != null && attackProbabilities != null &&
				quadrant.ordinal() < attackProbabilities.size()) {
			return attackProbabilities.get(quadrant.ordinal());
		} else {
			return null;
		}
	}

	/**
	 * Get the types of data Red considers, which include:
	 *     P_Only: Red only considers P (and not also U).
	 *     U_Only: Red only considers U (and not also P). 
	 *     P_And_U: Red considers both P and U.
	 * 
	 * @return the types of data Red considers
	 */
	@XmlAttribute(name="dataConsidered")
	public RedTacticConsiderationData getDataConsidered() {
		return dataConsidered;
	}

	/**
	 * Set the types of data Red considers, which include:
	 *     P_Only: Red only considers P (and not also U).
	 *     U_Only: Red only considers U (and not also P). 
	 *     P_And_U: Red considers both P and U.
	 * 
	 * @param dataConsidered the types of data Red considers
	 */
	public void setDataConsidered(RedTacticConsiderationData dataConsidered) {
		this.dataConsidered = dataConsidered;
	}

	/**
	 * Get the High "P" threshold. P values > this threshold are considered High, and values <= the threshold are Low.
	 * 
	 * @return the High "P" threshold
	 */
	@XmlAttribute(name="high_P_Threshold")
	public Double getHigh_P_Threshold() {
		return high_P_Threshold;
	}

	/**
	 * Set the High "P" threshold. P values > this threshold are considered High, and values <= the threshold are Low.
	 * 
	 * @param high_P_Threshold the High "P" threshold
	 */
	public void setHigh_P_Threshold(Double high_P_Threshold) {
		this.high_P_Threshold = high_P_Threshold;
	}		

	/**
	 * Get the large "U" threshold. U values > this threshold are considered large, and values <= the threshold are small.
	 * 
	 * @return the large "U" threshold
	 */
	@XmlAttribute(name="large_U_Threshold")
	public Integer getLarge_U_Threshold() {
		return large_U_Threshold;
	}

	/**
	 * Set the large "U" threshold. U values > this threshold are considered large, and values <= the threshold are small.
	 * 
	 * @param large_U_Threshold the large "U" threshold
	 */
	public void setLarge_U_Threshold(Integer large_U_Threshold) {
		this.large_U_Threshold = large_U_Threshold;
	}	
	
	@Override
	protected RedTacticParameters clone() throws CloneNotSupportedException {
		return new RedTacticParameters(this);
	}	

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[High P, Small U: " + getAttackProbability(RedTacticQuadrant.High_P_Small_U));
		sb.append(", High P, Large U: " + getAttackProbability(RedTacticQuadrant.High_P_Large_U));
		sb.append(", Low P, Small U: " + getAttackProbability(RedTacticQuadrant.Low_P_Small_U));
		sb.append(", Low P, Large U: " + getAttackProbability(RedTacticQuadrant.Low_P_Large_U));
		sb.append("]");
		return sb.toString();
	}

	/** Test main */
	public static void main(String[] args) {
		BlueBook blueBook = BlueBook.createDefaultBlueBook();
		RedTacticParameters rtp = blueBook.getMission_1_Tactics().get(0).getTacticParameters();
		System.out.println("High P, Small U: " + rtp.getAttackProbability(RedTacticQuadrant.High_P_Small_U));
		System.out.println("High P, Large U: " + rtp.getAttackProbability(RedTacticQuadrant.High_P_Large_U));
		System.out.println("Low P, Small U: " + rtp.getAttackProbability(RedTacticQuadrant.Low_P_Small_U));
		System.out.println("Low P, Large U: " + rtp.getAttackProbability(RedTacticQuadrant.Low_P_Large_U));
	}
}