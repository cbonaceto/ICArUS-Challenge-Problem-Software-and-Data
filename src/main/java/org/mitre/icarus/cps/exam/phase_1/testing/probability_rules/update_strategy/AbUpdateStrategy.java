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
 * AB-model probability update strategy that uses parameters A and B.
 * This model is Bayesian when a == b == 1.
 * Pab = Pa^a * Pb^b
 * 
 * @author CBONACETO
 *
 */
public class AbUpdateStrategy extends ProbabilityUpdateStrategy {
	
	/** A parameter is the prior discounting factor */
	protected Double a = 1.D;
	
	/** B parameter is the likelihood discounting factor */
	protected Double b = 1.D;
	
	/** The minimum probability threshold. The default is 0.01 (1%) */
	protected Double minProb = 0.01D;
	
	/** The maximum probability threshold. The default is 0.99 (99%). */
	protected Double maxProb = 0.99D;	
	
	public AbUpdateStrategy() {
	}
	
	public AbUpdateStrategy(Double a, Double b) {
		this.a = a;
		this.b = b;
	}

	public Double getA() {
		return a;
	}
	
	public void setA(Double a) {
		this.a = a;
	}

	public Double getB() {
		return b;
	}

	public void setB(Double b) {
		this.b = b;
	}
	
	public Double getMinProb() {
		return minProb;
	}

	public void setMinProb(Double minProb) {
		this.minProb = minProb;
	}

	public Double getMaxProb() {
		return maxProb;
	}

	public void setMaxProb(Double maxProb) {
		this.maxProb = maxProb;
	}

	//@Override
	public Double computePosterior(Double prior, Double likelihood) {
		//Impose minProb/maxProb constraint on both prior and likelihood
		if(prior < minProb) {
			System.err.println("found likelihood out of range: " + prior);
			prior = minProb;
		} else if(prior > maxProb) {
			prior = maxProb;
		}
		if(likelihood < minProb) {			
			likelihood = minProb;
		} else if(likelihood > maxProb) {			
			likelihood = maxProb;
		}
		
		//Compute posterior
		double prob =  Math.pow(prior, a) * Math.pow(likelihood, b);
		
		//Impose minProb/maxProb constraint on posterior
		if(prob < minProb) {
			return minProb;
		}
		if(prob > maxProb) {
			return maxProb;
		}
		return prob;
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
		
		//Impose minProb/maxProb constraint on both priors and likelihoods	
		ArrayList<Double> normalizedPriors = ProbabilityUtils.createProbabilities_Double(priors.size(), 0d);
		ProbabilityUtils.normalizeDecimalProbabilities(priors, normalizedPriors, minProb);
		ArrayList<Double> normalizedLikelihoods = ProbabilityUtils.createProbabilities_Double(likelihoods.size(), 0d);
		ProbabilityUtils.normalizeDecimalProbabilities(likelihoods, normalizedLikelihoods, minProb);
		
		//Compute posteriors
		for(int i = 0; i<normalizedPriors.size(); i++) {
			posteriors.set(i, Math.pow(normalizedPriors.get(i), a) * Math.pow(normalizedLikelihoods.get(i), b));
		}		

		//Impose minProb/maxProb constraint on posteriors
		ProbabilityUtils.normalizeDecimalProbabilities(posteriors, posteriors, minProb);	

		return posteriors;
	}

	@Override
	public int getStrategyType() {
		return ProbabilityUpdateStrategy.AB_MODEL;
	}

	@Override
	public String toString() {	
		return "A: " + Double.toString(a) + ", B: " + Double.toString(b);
	}	
}