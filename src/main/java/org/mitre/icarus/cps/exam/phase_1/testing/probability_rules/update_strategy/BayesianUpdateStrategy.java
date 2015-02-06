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

import org.mitre.icarus.cps.app.util.ProbabilityUtils;

/**
 * Bayesian probability update strategy.
 * 
 * @author CBONACETO
 *
 */
public class BayesianUpdateStrategy extends ProbabilityUpdateStrategy {
	
	//@Override
	public Double computePosterior(Double prior, Double likelihood) {
		//Compute Bayesian posterior
		return prior * likelihood;
	}

	@Override
	public ArrayList<Double> computePosteriors(ArrayList<Double> priors, ArrayList<Double> likelihoods) {
		return computePosteriors(priors, likelihoods, null);
	}	

	@Override
	public ArrayList<Double> computePosteriors(ArrayList<Double> priors, ArrayList<Double> likelihoods, ArrayList<Double> posteriors) {
		if(posteriors == null || posteriors.size() != priors.size()) {
			posteriors = ProbabilityUtils.createProbabilities_Double(priors.size(), 0d);
		}
		
		//Compute Bayesian posteriors		
		for(int i = 0; i<priors.size(); i++) {			
			posteriors.set(i, priors.get(i) * likelihoods.get(i));
		}

		//Normalize posteriors
		ProbabilityUtils.normalizeDecimalProbabilities(posteriors, posteriors);

		return posteriors;
	}

	@Override
	public int getStrategyType() {
		return ProbabilityUpdateStrategy.BAYESIAN;
	}
}