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

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.assessment.score_computer.phase_2.ScoreComputer_Phase2.RedBluePayoff;
import org.mitre.icarus.cps.exam.phase_2.testing.AbstractDatum;
import org.mitre.icarus.cps.exam.phase_2.testing.DatumType;
import org.mitre.icarus.cps.exam.phase_2.testing.PlayerType;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.BlueAction.BlueActionType;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.RedAction.RedActionType;
import org.mitre.icarus.cps.experiment_core.condition.InstructionsPage;

/**
 * The "payoff matrix" for an exam describes the payoffs to Red and Blue based on the decisions made by 
 * Red and Blue and whether or not there is a show-down.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="PayoffMatrix", namespace="IcarusCPD_2")
public class PayoffMatrix extends AbstractDatum  {
	
	/** The payoff matrix instructions page */
	protected InstructionsPage payoffMatrixInstructions;
	
	/** The points gained (or lost) for Red for each scenario. A value of NaN indicates a show-down */
	protected List<Double> redPayoffs;
	
	/** The points gained (or lost) for Blue for each scenario. A value of NaN indicates a show-down */
	protected List<Double> bluePayoffs;	
	
	/**
	 * Construct an exmpty payoff matrix. 
	 */
	public PayoffMatrix() {}
	
	/**
	 * Construct a payoff matrix with the given Red and Blue payoffs. 
	 * The payoffs are an ordered list of Double values of the form:
	 *   Element 0: Points awarded on show-down (when Blue doesn't divert and Red attacks) is always NaN because this value is calculated using P, U, and a random number generator.
	 *   Element 1: Points awarded to Red and Blue when Blue does not divert and Red does not attack. Default is 0 for Red and 0 for Blue.
	 *   Element 2: Points awarded to Red and Blue when Blue diverts and Red attacks. Default is 0 for Red and 0 for Blue.
	 *   Element 3: Points awarded to Red and Blue when Blue does diverts and Red does not attack. Default is 1 for Red and -1 for Blue.
	 * 
	 * @param redPayoffs the Red payoffs  
	 * @param bluePayoffs the Blue payoffs
	 */
	public PayoffMatrix(List<Double> redPayoffs, List<Double> bluePayoffs) {
		this.redPayoffs = redPayoffs;
		this.bluePayoffs = bluePayoffs;
	}
	
	/**
	 * Construct a payoff matrix using default values for the Red and Blue payoffs, which are:
	 * 	Element 0: Points awarded on show-down (when Blue doesn't divert and Red attacks) is always NaN because this value 
	 *  is calculated using P, U, and a random number generator as described in the Phase 2 Test Specification.
	 *  Element 1: Points awarded to Red and Blue when Blue does not divert and Red does not attack. Default is 0 for Red and 0 for Blue.
	 *  Element 2: Points awarded to Red and Blue when Blue diverts and Red attacks. Default is 0 for Red and 0 for Blue.
	 *  Element 3: Points awarded to Red and Blue when Blue does diverts and Red does not attack. Default is 1 for Red and -1 for Blue.
	 * 
	 * @return a default payoff matrix
	 */
	public static PayoffMatrix createDefaultPayoffMatrix() {
		return new PayoffMatrix(createDefaultRedPayoffs(), createDefaultBluePayoffs());
	}
	
	/**
	 * Create the default Red payoff values.
	 * 
	 * @return the default Red payoff values
	 */
	public static List<Double> createDefaultRedPayoffs() {
		return Arrays.asList(Double.NaN, 0D, 0D, 1D);
	}
	
	/**
	 * Create the default Blue payoff values.
	 * 
	 * @return the default Blue payoff values.
	 */
	public static List<Double> createDefaultBluePayoffs() {
		return Arrays.asList(Double.NaN, 0D, 0D, -1D);
	}
	
	/**
	 * Convenience method to get the element index in the Blue or Red payoff values 
	 * list for the given Blue and Red action.
	 * 
	 * @param blueAction the Blue action
	 * @param redAction the Red action
	 * @return the index in the Blue or Red payoff list for the Blue or Red payoff when the given Blue and Red action are taken
	 */
	public int getPayoffIndex(BlueActionType blueAction, RedActionType redAction) {
		int index = 0;
		if(blueAction == BlueActionType.Do_Not_Divert) {
			if(redAction == RedActionType.Attack) {
				index = 0; //Blue does not divert, Red attacks
			} else {
				index = 1; //Blue does not divert, Red does not attack
			}
		} else {
			if(redAction == RedActionType.Attack) {
				index = 2; //Blue diverts, Red attacks
			} else {
				index = 3; //Blue diverts, Red does not attack
			}
		}
		return index;
	}

	/**
	 * Compute the points awarded to Blue and Red based on the actions selected by Blue and Red
	 * and the Red and Blue payoff values. 
	 * 
	 * @param blueAction the Blue action selected at a location
	 * @param redAction the Red action selected at a location
	 * @param redOpportunity_U the "U" value at a location (used to determine points when there is a show-down)
	 * @param redVulnerability_P the "P" value at a location (used to randomly select a winner when there is a show-down)
	 * @param random the random number generator to use when there is a show-down.
	 * @return the points awarded to Blue and Red
	 */
	public RedBluePayoff computePayoff(BlueActionType blueAction, RedActionType redAction,
			Integer redOpportunity_U, Double redVulnerability_P, Random random) {
		//Randomly select showdown winner
		if(random == null) {random = new Random(System.currentTimeMillis());}
		return computePayoff(blueAction, redAction, 
				determineShowdownWinner(redVulnerability_P, random),
				redOpportunity_U);
	}
	
	/**
	 * Randomly selected a show-down winner using the given "P" value at a location and a random number generator.
	 * 
	 * @param redVulnerability_P the "P" value at a location
	 * @param random the random number generator to use. A new random number generator seeded with the current time will be
	 *        created if a null value is given
	 * @return the randomly selected show-down winner
	 */
	public static PlayerType determineShowdownWinner(Double redVulnerability_P, Random random) {
		if(random == null) {random = new Random(System.currentTimeMillis());}
		return random.nextDouble() < redVulnerability_P ? PlayerType.Blue : PlayerType.Red;
	}
	
	/**
	 * Compute the points awarded to Blue and Red based on the actions selected by Blue and Red, 
	 * the Red and Blue payoff values, and the winner of the show-down (if there was a showdown).
	 * 
	 * @param blueAction the Blue action selected at a location
	 * @param redAction the Red action selected at a location
	 * @param showdownWinner the show-down winner at a location (if there was a show-down)
	 * @param redOpportunity_U the "U" value at a location (used to determine points when there is a show-down)
	 * @return the points awarded to Blue and Red
	 */
	public RedBluePayoff computePayoff(BlueActionType blueAction, RedActionType redAction,
			PlayerType showdownWinner, Integer redOpportunity_U) {		
		List<Double> redPayoffs = this.redPayoffs != null ? this.redPayoffs : createDefaultRedPayoffs();
		List<Double> bluePayoffs = this.bluePayoffs != null ? this.bluePayoffs : createDefaultBluePayoffs();		
		int index = getPayoffIndex(blueAction, redAction);
		Double redPayoff = redPayoffs.get(index);
		if(redPayoff == null || redPayoff.isNaN()) {
			//Show-down!
			boolean blueWins = showdownWinner == PlayerType.Blue;
			return new RedBluePayoff(
					(double)(blueWins ? -redOpportunity_U : redOpportunity_U),
					(double)(blueWins ? redOpportunity_U : -redOpportunity_U), true,
					showdownWinner);			
		} else {
			return new RedBluePayoff(redPayoff, bluePayoffs.get(index), false, 
					redAction == RedActionType.Attack ? (blueAction == BlueActionType.Divert ? PlayerType.Blue : PlayerType.Red): null);
		}		
	}	

	/**
	 * Get the payoff matrix instructions page shown to participants when reviewing the payoff matrix.
	 * FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @return the payoff matrix instructions page
	 */
	@XmlElement(name="PayoffMatrixInstructions")
	public InstructionsPage getPayoffMatrixInstructions() {
		return payoffMatrixInstructions;
	}

	/**
	 * Set the payoff matrix instructions page shown to participants when reviewing the payoff matrix.
	 * FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @param payoffMatrixInstructions the payoff matrix instructions page
	 */
	public void setPayoffMatrixInstructions(InstructionsPage payoffMatrixInstructions) {
		this.payoffMatrixInstructions = payoffMatrixInstructions;
	}

	/**
	 * Get the points gained (or lost) for Red for each scenario. Values are an ordered list of the form:	 * 
	 * Element 0: Points awarded to Red when Blue doesn't divert and Red attacks. NaN indicates a show-down should take place.
	 * Element 1: Points awarded to Red when Blue does not divert and Red does not attack.
	 * Element 2: Points awarded to Red when Blue diverts and Red attacks.
	 * Element 3: Points awarded to Red when Blue does diverts and Red does not attack.
	 * 
	 * @return the Red Payoffs
	 */
	@XmlElement(name="RedPayoffs")
	@XmlList
	public List<Double> getRedPayoffs() {
		return redPayoffs;
	}

	/**
	 * Set the points gained (or lost) for Red for each scenario. Values are an ordered list of the form:	 * 
	 * Element 0: Points awarded to Red when Blue doesn't divert and Red attacks. NaN indicates a show-down should take place.
	 * Element 1: Points awarded to Red when Blue does not divert and Red does not attack.
	 * Element 2: Points awarded to Red when Blue diverts and Red attacks.
	 * Element 3: Points awarded to Red when Blue does diverts and Red does not attack.
	 * 
	 * @param redPayoffs the Red payoffs
	 */
	public void setRedPayoffs(List<Double> redPayoffs) {
		this.redPayoffs = redPayoffs;
	}

	/**
	 * Get the points gained (or lost) for Blue for each scenario. Values are an ordered list of the form: 
	 * Element 0: Points awarded to Blue when Blue doesn't divert and Red attacks. NaN indicates a show-down should take place.
	 * Element 1: Points awarded to Blue when Blue does not divert and Red does not attack.
	 * Element 2: Points awarded to Blue when Blue diverts and Red attacks.
	 * Element 3: Points awarded to Blue when Blue does diverts and Red does not attack.
	 * 
	 * @return the Blue payoffs
	 */
	@XmlElement(name="BluePayoffs")
	@XmlList
	public List<Double> getBluePayoffs() {
		return bluePayoffs;
	}

	/**
	 * Set the points gained (or lost) for Blue for each scenario. Values are an ordered list of the form: 
	 * Element 0: Points awarded to Blue when Blue doesn't divert and Red attacks. NaN indicates a show-down should take place.
	 * Element 1: Points awarded to Blue when Blue does not divert and Red does not attack.
	 * Element 2: Points awarded to Blue when Blue diverts and Red attacks.
	 * Element 3: Points awarded to Blue when Blue does diverts and Red does not attack.
	 * 
	 * @param bluePayoffs the Blue payoffs
	 */
	public void setBluePayoffs(List<Double> bluePayoffs) {
		this.bluePayoffs = bluePayoffs;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.IDatum#getDatumType()
	 */
	@Override
	public DatumType getDatumType() {
		return DatumType.PayoffMatrix;
	}
}