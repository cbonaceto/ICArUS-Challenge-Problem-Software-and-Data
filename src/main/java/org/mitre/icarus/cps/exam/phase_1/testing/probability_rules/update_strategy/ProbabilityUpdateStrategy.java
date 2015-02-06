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
package org.mitre.icarus.cps.exam.phase_1.testing.probability_rules.update_strategy;

import java.util.ArrayList;

/**
 * @author CBONACETO
 *
 */
public abstract class ProbabilityUpdateStrategy {
	public static final int BAYESIAN = 0;
	public static final int AB_MODEL = 1;
	public static final int NEXT_STRATEGY_TYPE = 2;
	
	/** The strategy name */
	protected String name;	
	
	/**
	 * Get the strategy name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the strategy name.
	 * 
	 * @param name the name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Compute the posterior probability using the update strategy given a prior and likelihood.
	 * 
	 * @param prior the prior
	 * @param likelihood the likelihood
	 * @return
	 */
	//public abstract Double computePosterior(Double prior, Double likelihood);	
	
	/**
	 * Computes the posterior probabilities using the update strategy given the priors and likelihoods.
	 * 
	 * @param priors the priors 
	 * @param likelihoods the likelihoods
	 * @return the posteriors
	 */
	public abstract ArrayList<Double> computePosteriors(ArrayList<Double> priors, ArrayList<Double> likelihoods);
	
	/**
	 * Computes the posterior probabilities using the update strategy given the priors and likelihoods.
	 * If the posteriors array passed in isn't null and is the correct size, populates the posteriors array, otherwise allocates a new array. 
	 * 
	 * @param priors
	 * @param likelihoods
	 * @param posteriors
	 */
	public abstract ArrayList<Double> computePosteriors(ArrayList<Double> priors, ArrayList<Double> likelihoods, ArrayList<Double> posteriors);
	
	/**
	 * Get the strategy type.
	 * 
	 * @return
	 */
	public abstract int getStrategyType();	
}