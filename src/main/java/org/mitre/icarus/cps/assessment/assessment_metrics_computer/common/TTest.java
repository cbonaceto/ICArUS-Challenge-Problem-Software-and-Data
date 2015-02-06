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
package org.mitre.icarus.cps.assessment.assessment_metrics_computer.common;

import org.apache.commons.math3.distribution.TDistribution;

/**
 * Contains implementations of one and two sample T-tests using Student's t distribution.
 * 
 * @author CBONACETO
 */
public class TTest {       
    
    /** Tail types */
    public static enum TailType {
        Both, //Test the alternative hypothesis that the population means are not equal.
        Right, //Test the alternative hypothesis that population mean 1 is greater than population mean 2.
        Left //Test the alternative hypothesis that population mean 1 is less than the population mean 2.
    }
    
    /** Variance types */
    public static enum VarianceType {
        Equal, //Equal variance
        Unequal //Unequal variance
    }

    /**
     * Performs a one sample t-test.
     * 
     * @param mean1
     * @param s1
     * @param n1
     * @param u
     * @param tailType
     * @return for TailType.Both, p value that mean1 is not equal to u
     */
    public static Double oneSampleTTest(Double mean1, Double s1, int n1, Double u, 
            TailType tailType) {        
        double t = (mean1 - u)/(s1/Math.sqrt(n1));
        int df = Math.max(n1 - 1, 0);
        TDistribution tdist = new TDistribution(df);    
        tailType = tailType == null ? TailType.Both : tailType;
        switch(tailType) {
            case Both:
                //Two-tailed test
                return 2 * tdist.cumulativeProbability(-Math.abs(t));
            case Right:
                //One-tailed test, right
                return tdist.cumulativeProbability(-t);
            case Left:
                //One-tailed test, left
                return tdist.cumulativeProbability(t);
            default:
                return 1.d;
        }
        
    }

    /**
     * Perform a two-sample, independent t-test.
     * 
     * @param mean1 the mean of population 1
     * @param s1 the standard deviation of population 1
     * @param n1
     * @param mean2
     * @param s2
     * @param n2
     * @param tailType
     * @param varType
     * @return
     */
    public static Double twoSampleTTest(Double mean1, Double s1, int n1, 
            Double mean2, Double s2, int n2, TailType tailType, 
            VarianceType varType) {        
        varType = varType == null ? VarianceType.Equal : varType;        
        double t;
        int df = Math.max(n1 + n2 - 2, 0);
        if (varType == VarianceType.Equal) {
            if (n1 == n2) {
                //Equal sample sizes, equal variance
                t = (mean1 - mean2) / (Math.sqrt(0.5 * (s1 * s1 + s2 * s2)) * Math.sqrt(2.d / n1));
            } else {
                //Unequal sample sizes, equal variance
                t = (mean1 - mean2) / (Math.sqrt(((n1 - 1) * s1 * s1 + (n2 - 1) * s2 * s2) / df) * Math.sqrt(1.d / n1 + 1.d / n2));
            }
        } else {
            //Equal or unequal sample sizes, unequal variance
            t = (mean1 - mean2)/(Math.sqrt((s1*s1)/n1 + (s2*s2)/n2));            
        }
        TDistribution tdist = new TDistribution(df);    
        tailType = tailType == null ? TailType.Both : tailType;
        switch(tailType) {
            case Both:
                //Two-tailed test
                return 2 * tdist.cumulativeProbability(-Math.abs(t));
            case Right:
                //One-tailed test, right
                return tdist.cumulativeProbability(-t);
            case Left:
                //One-tailed test, left
                return tdist.cumulativeProbability(t);
            default:
                return 1.d;
        }
    }
    
    public static void main(String[] args) {        
        //System.out.println(TTest.oneSampleTTest(75.0083, 8.6838, 120, 74.d, TailType.Both));
        System.out.println(TTest.oneSampleTTest(99.9083, 8.6838, 120, 100.d, TailType.Left));
        System.out.println(TTest.twoSampleTTest(73.0083, 8.6838, 120, 74.9917,  6.5147, 120, 
                TailType.Left, VarianceType.Equal));
        
    }
}
