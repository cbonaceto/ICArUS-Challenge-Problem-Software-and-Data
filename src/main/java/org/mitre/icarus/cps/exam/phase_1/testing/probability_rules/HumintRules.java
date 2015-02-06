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
package org.mitre.icarus.cps.exam.phase_1.testing.probability_rules;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.assessment.score_computer.phase_1.GaussianFunction.Gaussian1D;

/**
 * Defines parameters of Gaussian function used to compute attack likelihood
 * based on HUMINT distance-decay function.
 * 
 * Gaussian is computed as: 
 * peakHeight_a * e ^ (- (distance - peakCenter_b)^2 / 2*bellWidth_c^2 )
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="HumintRules", namespace="IcarusCPD_1")
public class HumintRules {
	
	/** Table of likelihood values at a series of distances */
	//protected ArrayList<LikelihoodAtDistance> likelihoodsAtDistances;
	
	/** The 1D Gaussian function used to compute attack likelihoods as a function of distance */
	@XmlTransient
	protected Gaussian1D gaussian;	
	
	/**
	 * Default constructor creates the default Gaussian function, with a curve height of 0.4 (a),
	 * a center point of 0 (b), and a sigma (standard deviation, c) of 10.
	 */
	public HumintRules() {
		gaussian = new Gaussian1D(0.4d, 0.d, 10.d);
	}
	
	public HumintRules(double peakHeight_a, double peakCenter_b, double sigma_c) {
		gaussian = new Gaussian1D(peakHeight_a, peakCenter_b, sigma_c);
	}

	@XmlAttribute(name="peakHeight_a")
	public double getPeakHeight_a() {
		return gaussian.getPeakHeight_a();
	}

	public void setPeakHeight_a(double peakHeight_a) {
		gaussian.setPeakHeight_a(peakHeight_a);
	}

	@XmlAttribute(name="peakCenter_b")
	public double getPeakCenter_b() {
		return gaussian.getPeakCenter_b();
	}

	public void setPeakCenter_b(double peakCenter_b) {
		gaussian.setPeakCenter_b(peakCenter_b);
	}

	@XmlAttribute(name="sigma_c")	
	public double getSigma_c() {
		return gaussian.getSigma_c();
	}

	public void setSigma_c(double sigma_c) {
		gaussian.setSigma_c(sigma_c);
	}

	/**
	 * Get the attack likelihood for the given distance in miles from a group center
	 * to an attack location using the 1D Gaussian distance decay function.
	 * 
	 * @param distance the distance in miles
	 * @return
	 */
	public double getAttackLikelihood(double distance) {
		return gaussian.getGuassianValue(distance);
		//return peakHeight_a * Math.exp(-((Math.pow(distance-peakCenter_b, 2)/(2 * stdDeviation_c * stdDeviation_c))));
	}	

	/*@XmlElement(name="LikelihoodAtDistance")
	public ArrayList<LikelihoodAtDistance> getLikelihoodsAtDistances() {
		return likelihoodsAtDistances;
	}

	public void setLikelihoodsAtDistances(
			ArrayList<LikelihoodAtDistance> likelihoodsAtDistances) {
		this.likelihoodsAtDistances = likelihoodsAtDistances;
	}*/
	
//	@XmlType(name="LikelihoodAtDistance", namespace="IcarusCPD_1",
//			propOrder={"attackLikelihood", "distance"})
//	public static class LikelihoodAtDistance {
//		/** The distance */
//		protected Double distance;
//		
//		/** The likelihood of group attack at that distance */
//		protected Double attackLikelihood;
//		
//		public LikelihoodAtDistance() {}
//		
//		public LikelihoodAtDistance(Double distance, Double attackLikelihood) {
//			this.distance = distance;
//			this.attackLikelihood = attackLikelihood;
//		}			
//		
//		@XmlAttribute(name="distance")
//		public Double getDistance() {
//			return distance;
//		}
//
//		public void setDistance(Double distance) {
//			this.distance = distance;
//		}
//	
//		@XmlAttribute(name="attackLikelihood")
//		public Double getAttackLikelihood() {
//			return attackLikelihood;
//		}
//
//		public void setAttackLikelihood(Double attackLikelihood) {
//			this.attackLikelihood = attackLikelihood;
//		}		
//	}	
}