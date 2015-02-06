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
package org.mitre.icarus.cps.exam.phase_1.generation;

import java.util.Random;

import org.mitre.icarus.cps.assessment.score_computer.phase_1.GaussianFunction.Gaussian1D;

/**
 * @author CBONACETO
 *
 */
public class DistanceGenerator {	
	
	/**
	 * Generates a set of distances in the range [minDistance, maxDistance] sampled from the given
	 * Gaussian distribution.
	 * 
	 * @param numDistances
	 * @param minDistance
	 * @param maxDistance
	 * @param gaussian
	 * @param rand
	 * @return
	 */
	public static double[] generateDistances(int numDistances, double minDistance, double maxDistance, 
			double minSeparation, Gaussian1D gaussian, Random rand) {
		double distances[] = new double[numDistances];		
		int currentDistanceIndex = 0;
		do {
			//Generate a uniformly distributed random number between minDistance and maxDistance (the candidate distance)
			double dist = rand.nextDouble() * (maxDistance-minDistance) + minDistance; //between minDistance to maxDistance			
			
			//Compute p(candidate distance) using the Gaussian
			double pDist = gaussian.getGuassianValue(dist);
			
			//Generate another uniformly distributed random number between 0-1. 
			//If it is <= pDist, then use the candidate distance if it is at least minSeparation units apart
			//System.out.println(pDist + ", " + !distanceViolatesMinSeparation(distances, dist, minSeparation));
			if(rand.nextDouble() < pDist && 
					!distanceViolatesMinSeparation(distances, dist, minSeparation)) {
				distances[currentDistanceIndex] = dist;
				currentDistanceIndex++;
			}
		} while(currentDistanceIndex < distances.length);
		return distances;
	}
	
	/**
	 * Return whether candidateDistance <= minSeparation units from any value in the distances array.
	 * 
	 * @param distances
	 * @param candidateDistance
	 * @param minSeparation
	 * @return
	 */
	protected static boolean distanceViolatesMinSeparation(double distances[], double candidateDistance, double minSeparation) {
		if(distances != null && distances.length > 0) {
			for(double distance : distances) { 
				if(Math.abs(distance - candidateDistance) <= minSeparation) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Compute the minimum difference between candidateDistance and the values in the distances array. 
	 * 
	 * @param distances
	 * @param candidateDistance
	 * @return
	 */
	protected static double computeMinSeparation(double[] distances, double candidateDistance) {
		double minSeparation = Double.MAX_VALUE;
		if(distances != null && distances.length > 0) {
			for(double distance : distances) {
				double separation = Math.abs(distance - candidateDistance); 
				if(separation < minSeparation) {
					minSeparation = separation;
				}
			}
		}
		return minSeparation;
	}

	/** Test main */
	public static void main(String[] args) {
		
		//Test the Gaussian generator using the HUMINT likelihood function
		Gaussian1D gaussian = new Gaussian1D(0.4d, 1.d, 10.d);
		int numVals = 1000;
		Random rand = new Random(1);
		//double[] vals = new double[numVals];
		
		//First, use the generateDistances method to generate distances
		//Compute mean and sigma of distances
		long startTime = System.currentTimeMillis();
		double[] vals = generateDistances(numVals, 0, 30, -1, gaussian, rand);
		System.out.println("elapsed time (seconds): " + (System.currentTimeMillis() - startTime)/1000);
		double mean = computeMean(vals);
		double sigma = computeSigma(vals, mean);
		System.out.println("mean 1: " + mean + ", sigma 1: " + sigma);
		
		/*//First, generate all values from the HUMINT Gaussian
		double x = numVals/2 * -.01;
		System.out.println(x);
		for(int i=0; i<numVals; i++) {
			//vals[i] = gaussian.getGuassianValue(i);
			vals[i] = gaussian.getGuassianValue(x);
			x += 0.01;
		}
		System.out.println(x);
		//Compute mean and sigma of values
		double mean = computeMean(vals);
		double sigma = computeSigma(vals, mean);
		System.out.println("mean 1: " + mean + ", sigma 1: " + sigma);*/

		//Now, generate random values from the HUMINT Gaussian
		for(int i=0; i<numVals; i++) {
			vals[i] = Math.abs(gaussian.getNextGaussian(rand));
			//do {
			//vals[i] = gaussian.getNextGaussian(rand);
			//} while(vals[i] < 0);
		} 		
		//Compute mean and sigma of values
		mean = computeMean(vals);
		sigma = computeSigma(vals, mean);
		System.out.println("mean 2: " + mean + ", sigma 2: " + sigma);
	}
	
	public static double computeMean(double[] vals) {
		double mean = 0;		
		for(int i=0; i<vals.length; i++) {
			mean += vals[i];
		}
		return mean/vals.length;
	}
	
	public static double computeSigma(double[] vals, double mean) {
		double sigma = 0;
		for(int i=0; i<vals.length; i++) {
			sigma += StrictMath.pow(vals[i] - mean, 2);
		}
		return StrictMath.sqrt(sigma/vals.length);		
	}
}