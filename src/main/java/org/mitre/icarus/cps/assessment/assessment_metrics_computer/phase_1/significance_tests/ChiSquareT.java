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
package org.mitre.icarus.cps.assessment.assessment_metrics_computer.phase_1.significance_tests;

import org.apache.commons.math3.stat.inference.ChiSquareTest;

public class ChiSquareT {

	public static void main(String[] args) {
		long[][] counts1 = {
				{71, 27, 5, 10, 28, 5, 42, 14, 5, 3, 11, 3, 17, 12, 16, 1, 14, 11, 21, 12, 14},
				{91, 38, 10, 5, 20, 12, 23, 5, 2, 1, 11, 4, 2, 3, 20, 6, 13, 10, 14,  21,  25}
		};
		long[][] counts2 = {
				{71, 27, 5, 10, 28, 5, 42, 14, 5, 3, 11, 3, 17, 12, 16, 1, 14, 11, 21, 12, 14},
				{71, 27, 5, 10, 28, 5, 42, 14, 5, 3, 11, 3, 17, 12, 16, 1, 14, 11, 21, 12, 14}
		};
		long[][] counts3 = {
				{162, 65, 15, 15, 48, 17, 65, 19, 7, 4, 22, 7, 19, 15, 36, 7, 27, 21, 35, 33, 39, 24, 47, 41},		
				{151, 34, 6,  6,  15, 6,  39, 7,  4, 4, 6,  3, 11, 3,  18, 3, 3,   6, 19, 16, 13, 25, 28, 24}
		};
		    
		//System.out.println(counts.length);
		ChiSquareTest chi2 = new ChiSquareTest();
		System.out.println(chi2.chiSquare(counts1) + ", " + chi2.chiSquareTest(counts1, 0.05));		
		System.out.println(chi2.chiSquare(counts2) + ", " + chi2.chiSquareTest(counts2, 0.05));		
		System.out.println(chi2.chiSquare(counts3) + ", " + chi2.chiSquareTest(counts3, 0.05));
		//System.out.println(chi2.chiSquareDataSetsComparison(counts3[0], counts3[1]));
		//0.000379		.
	}
}
