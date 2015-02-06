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
package org.mitre.icarus.cps.exam.phase_2.generation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.mitre.icarus.cps.assessment.score_computer.phase_2.ScoreComputer_Phase2;
import org.mitre.icarus.cps.assessment.score_computer.phase_2.ScoreComputer_Phase2.RedAttackProbabilities;
import org.mitre.icarus.cps.exam.phase_2.testing.bluebook.RedTacticParameters;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.BlueAction;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.BlueAction.BlueActionType;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.int_presentation.SigintPresentationProbe;
import org.mitre.icarus.cps.feature_vector.phase_2.BlueLocation;
import org.mitre.icarus.cps.feature_vector.phase_2.int_datum.HumintDatum;
import org.mitre.icarus.cps.feature_vector.phase_2.int_datum.SigintReliability;

/**
 * Generates Blue actions using an optimal strategy given Red's tactics.
 * 
 * @author CBONACETO
 *
 */
public class BlueAgent {
	
	/** Score computer */
	protected final ScoreComputer_Phase2 scoreComputer; 
	
	/** Random number generator */
	protected Random random;	
	
	/**
	 * Construct a new BlueAgent using a random number generator seeded with 1.
	 */
	public BlueAgent() {
		this(new Random(1), new ScoreComputer_Phase2());
	}
	
	/**
	 * Construct a new BlueAgent using a random number generator seeded with 1 and
	 * the given score computer.
	 * 
	 * @param scoreComputer the score computer
	 */
	public BlueAgent(ScoreComputer_Phase2 scoreComputer) {
		this(new Random(1), scoreComputer);
	}
	
	/**
	 * Construct a new BlueAgent using the given random number generator.
	 * 
	 * @param random the random number generator
	 */
	public BlueAgent(Random random) {
		this(random, new ScoreComputer_Phase2());
	}
	
	/**
	 * Construct a new BlueAgent using the given random number generator and score computer.
	 * 
	 * @param random the random number generator
	 * @param scoreComputer the score computer
	 */
	public BlueAgent(Random random, ScoreComputer_Phase2 scoreComputer) {
		this.random = random != null ? random : new Random(1);
		this.scoreComputer = scoreComputer == null ? new ScoreComputer_Phase2() : scoreComputer;
	}

	/**
	 * Set a new random number generator to use.
	 * 
	 * @param random the random number generator
	 */
	public void setRandom(Random random) {
		this.random = random;
	}
	
	/**
	 * Generate a Blue action at each location based on the probability of Red attack at each location. The 
	 * probability P(Attack | SIGINT, HUMINT, IMINT, OSINT) is computed at each location. A random number in the range [0, 1) is
	 * then generated. Blue diverts if P(Attack) at the location is greater than the random number; otherwise,
	 * Blue does not divert.
	 * 
	 * @param locations the locations
	 * @param redTactic the Red tactic parameters Red is currently playing with
	 * @param humint the HUMINT information
	 * @param sigintReliability the SIGINT reliabilities
	 * @param sigintPresentations the locations at which SIGINT is available
	 * @return the Blue action at each location
	 */
	public List<BlueAction> selectBlueActions(List<BlueLocation> locations, RedTacticParameters redTactic,
			HumintDatum humint, SigintReliability sigintReliability,
			List<SigintPresentationProbe> sigintPresentations) {
		return selectBlueActions(locations,
				scoreComputer.computeNormativeCumulativeRedAttackProbabilities(
				locations, redTactic, humint, sigintReliability, getSigintLocations(sigintPresentations)));
	}
	
	/**
	 * Generate a Blue action at each location based on the probability of Red attack at each location. The 
	 * probability P(Attack | SIGINT, HUMINT, IMINT, OSINT) is computed at each location. A random number in the range [0, 1) is
	 * then generated. Blue diverts if P(Attack) at the location is greater than the random number; otherwise,
	 * Blue does not divert.
	 * 
	 * @param locations the locations
	 * @param redTactics the possible Red tactic parameters that Red is playing with
	 * @param redTacticProbs the probability that Red is playing with each Red tactic
	 * @param humint the HUMINT information
	 * @param sigintReliability the SIGINT reliabilities
	 * @param sigintPresentations the locations at which SIGINT is available
	 * @return the Blue action at each location
	 */
	public List<BlueAction> selectBlueActions(List<BlueLocation> locations, List<RedTacticParameters> redTactics,
			List<Double> redTacticProbs, HumintDatum humint, SigintReliability sigintReliability,
			List<SigintPresentationProbe> sigintPresentations) {
		return selectBlueActions(locations,
				scoreComputer.computeNormativeCumulativeRedAttackProbabilities(
				locations, redTactics, redTacticProbs, humint, sigintReliability,
				getSigintLocations(sigintPresentations)));
	}	
	
	/**
	 * Generate a Blue action at each location based on the probability of Red attack at each location. 
	 * A random number in the range [0, 1) is generated, and Blue diverts if P(Attack) at the location is 
	 * greater than the random number; otherwise, Blue does not divert.
	 * 
	 * @param locations the locations
	 * @param redAttackProbabilities the Red attack probabilities at each location
	 * @return the Blue action at each location
	 */
	public List<BlueAction> selectBlueActions(List<BlueLocation> locations, 
			List<RedAttackProbabilities> redAttackProbabilities) {				
		if(random == null) {random = new Random(1);}		
		if(locations != null && !locations.isEmpty()) {						
			ArrayList<BlueAction> blueActions = new ArrayList<BlueAction>(locations.size());
			int locationIndex = 0;
			for(BlueLocation location : locations) {
				//Get P(Activity, Capability, Propensity) [the overall probability of Red attack] at the location
				Double pRedAttack = redAttackProbabilities != null && locationIndex < redAttackProbabilities.size() ? 
					redAttackProbabilities.get(locationIndex).getpActivityPropensityCapability() : 0.d;
				//System.out.println("Red attack prob at location " + location.getId() + ": " + pRedAttack);
				
				//Select a Blue action based on the probability of Red attack
				BlueActionType blueAction = null;
				if(pRedAttack > random.nextDouble()) {
					//Divert at the location					
					blueAction = BlueActionType.Divert;
				} else {
					//Do not divert at the location
					blueAction = BlueActionType.Do_Not_Divert;
				}
				//System.out.println("Blue action at location " + location.getId() + ": " + blueAction);
				blueActions.add(new BlueAction(location.getId(), locationIndex, blueAction));
				locationIndex++;
				//DEBUG CODE
				//System.out.println("P(Activity, Capability, Propensity): " + pRedAttack + ", Blue Action: " + blueAction);
				//END DEBUG CODE
			}			
			return blueActions;
		}
		return null;
	}
	
	protected static List<String> getSigintLocations(List<SigintPresentationProbe> sigintPresentations) {
		List<String> sigintLocations = null;
		if(sigintPresentations != null && !sigintPresentations.isEmpty()) {
			sigintLocations = new ArrayList<String>();
			for (SigintPresentationProbe sigint : sigintPresentations) {
				if (sigint.getLocationIds() != null) {
					sigintLocations.addAll(sigintLocations);
				}
			}
		}
		return sigintLocations;
	}
}