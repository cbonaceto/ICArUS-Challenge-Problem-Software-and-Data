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
package org.mitre.icarus.cps.exam.phase_05.training;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

/**
 * Rule training trial.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="Rules", namespace="IcarusCPD_05")
public class RuleTrainingTrial extends IcarusTrainingTrial {
	/** The rules */
	protected ArrayList<Rule> rules;

	@Override
	public TrainingTrialType getTrainingTrialType() {
		return TrainingTrialType.Rule;
	}

	/**
	 * Get the rules.
	 * 
	 * @return
	 */
	@XmlElementWrapper(name="Rules")
	@XmlElement(name="Rule")
	public ArrayList<Rule> getRules() {
		return rules;
	}

	/**
	 * Set the rules.
	 * 
	 * @param rules
	 */
	public void setRules(ArrayList<Rule> rules) {
		this.rules = rules;
	}	
}
