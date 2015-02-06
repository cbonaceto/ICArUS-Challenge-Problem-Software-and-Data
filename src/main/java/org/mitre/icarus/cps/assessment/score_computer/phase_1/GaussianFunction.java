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
package org.mitre.icarus.cps.assessment.score_computer.phase_1;

import java.awt.geom.Point2D;
import java.util.Random;

/**
 * Contains 1D and 2D Gaussian functions.
 * 
 * @author CBONACETO
 *
 */
public class GaussianFunction {	
	
	/**
	 * 1D Gaussian function class. Contains parameters for a (peak height), b (peak center), and c (sigma). Computes
	 * Gaussian function value given a value.
	 * 
	 * @author CBONACETO
	 *
	 */
	public static class Gaussian1D {
		/** "a" parameter (curve peak height) of the 1D Gaussian function */
		protected double peakHeight_a = 0.4d; 
		
		/** "b" parameter (mean of normal Gaussian, curve center) of the 1D Guassian function */
		protected double peakCenter_b = 0.d;
		
		/** "c" parameter (standard deviation of normal Gaussian, "bell" width) of the 1D Guassian function */
		protected double sigma_c = 10.d;
		
		protected boolean haveNextNextGaussian = false;
		
		protected double nextNextGaussian;
		
		public Gaussian1D() {}
		
		public Gaussian1D(double peakHeight_a, double peakCenter_b, double sigma_c) {
			this.peakHeight_a = peakHeight_a;
			this.peakCenter_b = peakCenter_b;
			this.sigma_c = sigma_c;
		}

		public double getPeakHeight_a() {
			return peakHeight_a;
		}

		public void setPeakHeight_a(double peakHeight_a) {
			this.peakHeight_a = peakHeight_a;
		}

		public double getPeakCenter_b() {
			return peakCenter_b;
		}

		public void setPeakCenter_b(double peakCenter_b) {
			this.peakCenter_b = peakCenter_b;
		}

		public double getSigma_c() {
			return sigma_c;
		}

		public void setSigma_c(double sigma_c) {
			this.sigma_c = sigma_c;
		}
		
		public double getGuassianValue(double x) {
			return peakHeight_a * StrictMath.exp(-((StrictMath.pow(x-peakCenter_b, 2)/(2 * sigma_c * sigma_c))));
		}
		
		/**
		 * @param rand
		 * @return
		 */
		public double getNextGaussian(Random rand) {
			if(haveNextNextGaussian) {
				haveNextNextGaussian = false;
				return peakCenter_b + sigma_c * nextNextGaussian;
			} else {
				double u, v, s;
				do {
					u = rand.nextDouble() * 2 - 1; //between -1.0 and 1.0
					v = rand.nextDouble() * 2 - 1; //between -1.0 and 1.0
					s = u * u + v * v;
				} while (s >= 1 || s == 0);
				nextNextGaussian = v * StrictMath.sqrt(-2.0 * StrictMath.log(s) / s);
				haveNextNextGaussian = true;
				return peakCenter_b + sigma_c * u * StrictMath.sqrt(-2.0 * StrictMath.log(s) / s);
			}		
		}
	}
	
	/** Test main */
	public static void main(String[] args) {
		//Gaussian1D gaussian = new Gaussian1D(0.4d, 0.d, 10.d);
		Gaussian1D gaussian = new Gaussian1D(0.4d, 0.d, 10.d);
		System.out.println("0: " + gaussian.getGuassianValue(0));
		System.out.println("5: " + gaussian.getGuassianValue(5));
		System.out.println("10: " + gaussian.getGuassianValue(10));
		System.out.println("20: " + gaussian.getGuassianValue(20));
		System.out.println("40: " + gaussian.getGuassianValue(40));
		System.out.println();
		
		double x0 = 30; double y0 = 30;
		double x = 28; double y = 40;
		double sigmaX = 10; double sigmaY = sigmaX;
		Gaussian2D gaussian2D = new Gaussian2D(x, y, sigmaX, sigmaY, 0);
		System.out.println(gaussian2D.getGuassianValue(x0, y0));
		System.out.println((1/(2*StrictMath.PI*sigmaX*sigmaY)) * 
				StrictMath.exp(-((StrictMath.pow(x-x0, 2)/(2*sigmaX*sigmaX)) +
						(StrictMath.pow(y-y0, 2)/(2*sigmaY*sigmaY)))));
	}
	
	/**
	 * 2D Gaussian function class. Contains parameters for x0, y0, sigmaX, sigmaY, and theta. Computes
	 * Gaussian function value given an x,y.
	 * 
	 * @author CBONACETO
	 *
	 */
	public static class Gaussian2D {		
		
		/** X coordinate of center*/
		protected double x0 = 0;
		
		/** Y coordinate of center*/
		protected double y0 = 0;
		
		/** Sigma for X */
		protected double sigmaX;
		
		/** Sigma for Y */
		protected double sigmaY;
		
		/** Rotation in degrees */
		protected double theta;
		
		/** Peak height */
		protected double amplitude;
		
		protected double a;
		
		protected double b;
		
		protected double c;		
		
		public Gaussian2D() {}
		
		public Gaussian2D(double x0, double y0, double sigmaX, double sigmaY, double theta) {
			this.x0 = x0;
			this.y0 = y0;
			setParameters(sigmaX, sigmaY, theta);
		}
		
		public void setParameters(double sigmaX, double sigmaY, double theta) {
			this.sigmaX = sigmaX;
			this.sigmaY = sigmaY;
			this.theta = theta;
			amplitude = 1 / (2*StrictMath.PI*sigmaX*sigmaY);
			a = StrictMath.pow(StrictMath.cos(theta), 2)/2/StrictMath.pow(sigmaX, 2) + 
					StrictMath.pow(StrictMath.sin(theta), 2)/2/StrictMath.pow(sigmaY, 2);
			b = -StrictMath.sin(2*theta)/4/StrictMath.pow(sigmaX, 2) + 
					StrictMath.sin(2*theta)/4/StrictMath.pow(sigmaY, 2);
			c = StrictMath.pow(StrictMath.sin(theta), 2)/2/StrictMath.pow(sigmaX, 2) +
					StrictMath.pow(StrictMath.cos(theta), 2)/2/StrictMath.pow(sigmaY, 2);
			//System.out.println("amplitude: " + amplitude);
		}
		
		public double getX0() {
			return x0;
		}
		
		public void setX0(double x0) {
			this.x0 = x0;
		}

		public double getY0() {
			return y0;
		}
		
		public void setY0(double y0) {
			this.y0 = y0;
		}

		public double getGuassianValue(double x, double y) {
			return amplitude * StrictMath.exp(-(a * StrictMath.pow(x - x0, 2) + 
					2 * b * (x - x0) * (y - y0) + 
					c * StrictMath.pow(y - y0, 2)));
		}
		
		/**
		 * @param rand
		 * @return
		 */
		public Point2D getNextGaussian(Random rand) {
			//TODO: Test this
			double u, v, s;
			do {
				u = rand.nextDouble() * 2 - 1; //between -1.0 and 1.0
				v = rand.nextDouble() * 2 - 1; //between -1.0 and 1.0
				s = u * u + v * v;
			} while (s >= 1 || s == 0);
			return new Point2D.Double(x0 + sigmaX * u,  y0 + sigmaY * v);
		}
	}	
}