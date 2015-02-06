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
package org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.red_tactics_probes;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.phase_2.testing.bluebook.RedTacticType;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.Probability;

/**
 * Contains the probability that Red is playing with a certain tactic.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="RedTacticProbability", namespace="IcarusCPD_2")
public class RedTacticProbability extends Probability {
	
	/** The Red tactic the probability is for */
	protected RedTacticType redTactic;
	
	/**
	 * Construct an empty RedTacticProbability.
	 */
	public RedTacticProbability() {}
	
	/**
	 * Construct a RedTacticProbability with the given Red tactic that the probability is for.
	 * 
	 * @param redTactic the Red tactic that the probability is for
	 */
	public RedTacticProbability(RedTacticType redTactic) {
		this.redTactic = redTactic;
	}

	/**
	 * Get the Red tactic that the probability is for.
	 * 
	 * @return the Red tactic that the probability is for
	 */
	@XmlAttribute(name="redTactic")
	public RedTacticType getRedTactic() {
		return redTactic;
	}

	/**
	 * Set the Red tactic that the probability is for.
	 * 
	 * @param redTactic the Red tactic that the probability is for
	 */
	public void setRedTactic(RedTacticType redTactic) {
		this.redTactic = redTactic;
	}	
}	