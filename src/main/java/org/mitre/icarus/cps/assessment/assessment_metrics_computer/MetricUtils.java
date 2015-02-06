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
package org.mitre.icarus.cps.assessment.assessment_metrics_computer;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.mitre.icarus.cps.app.util.ProbabilityUtils;

/**
 * Contains static methods used to compute various metrics.
 * 
 * @author CBONACETO
 */
public class MetricUtils {
    
    /**
     * Compute root mean squared error (RMSE) between two distributions.
     *
     * @param dist1 distribution 1
     * @param dist2 distribution 2
     * @return RMSE between the two distributions
     */
    public static Double computeRMSE(List<Double> dist1, List<Double> dist2) {
        double squaredSum = 0;
        for (int i = 0; i < dist1.size(); i++) {
            squaredSum += StrictMath.pow((dist1.get(i) - dist2.get(i)), 2);
        }
        return StrictMath.sqrt(squaredSum / dist1.size());
    } 
    
    public static int countNonNull(Collection<Double> dist) {
        int count = 0;
        for(Double val : dist) {
            if(val != null) {
                count++; 
            }
        }
        return count;
    }
    
    /**
     * @param sums
     * @param count
     */
    public static void mean(List<Double> sums, int count) {
        if (sums != null && !sums.isEmpty()) {
            for (int i = 0; i < sums.size(); i++) {
                sums.set(i, sums.get(i) / count);
            }
        }
    }
    
    /**
     *
     * @param sum
     * @param count
     * @return
     */
    public static double mean(double sum, int count) {
        return count > 0 ? sum/count : null;
    }
    
    /**
     *
     * @param dist
     * @return
     */
    public static Double mean(Collection<Double> dist) {        
        if(dist != null && !dist.isEmpty()) {
            double sum = 0d;
            int count = 0;
            for(Double val : dist) {
                if(val != null) {
                    sum += val;
                    count++;
                }
            }
            return count > 0 ? sum/count : null;
        }
        return null;
    }
    
    /**
     *
     * @param stdSum
     * @param count
     * @param populationStd
     * @return
     */
    public static double std(double stdSum, int count, boolean populationStd) {        
        return populationStd ? count > 0 ? Math.sqrt(stdSum/count) : null : 
                        count - 1 > 0 ? Math.sqrt(stdSum/(count-1)) : null;   
    }   
    
    /**
     *
     * @param dist
     * @param populationStd
     * @return
     */
    public static Double std(Collection<Double> dist, boolean populationStd) {
        return std(mean(dist), dist, populationStd);
    }

    /**
     *
     * @param mean
     * @param dist
     * @param populationStd
     * @return
     */
    public static Double std(Double mean, Collection<Double> dist, boolean populationStd) {
        if (dist != null && !dist.isEmpty()) {           
            mean = mean == null ? mean(dist) : mean;  //First compute the mean if it's null
            double sum = 0d;
            int count = 0;
            if (mean != null) {
                for (Double val : dist) {
                    if (val != null) {
                        sum += Math.pow(val - mean, 2);
                        count++;
                    }
                }
                return populationStd ? count > 0 ? Math.sqrt(sum / count) : null
                        : count - 1 > 0 ? Math.sqrt(sum / (count - 1)) : null;
            }
        }
        return null;
    }
    
    /**
     *
     * @param dist
     * @param populationStd
     * @return
     */
    public static DistributionStats distStats(Collection<Double> dist, boolean populationStd) {
        if(dist != null && !dist.isEmpty()) {
            DistributionStats stats = new DistributionStats();
            stats.mean = mean(dist);
            stats.count = countNonNull(dist);
            stats.std = std(stats.mean, dist, populationStd);
            return stats;
        }
        return null;
    }
    
    /**
     *
     * @param runningStats
     * @param populationStd
     * @return
     */
    public static DistributionStats distStats(DistributionStats runningStats, boolean populationStd) {
        if(runningStats != null && runningStats.count > 0) {
            runningStats.mean = runningStats.mean / runningStats.count;            
            runningStats.std = std(runningStats.mean, runningStats.dist, populationStd);
        }
        return runningStats;
    }
    
    /**
     *
     * @param runningStats
     * @param val
     * @param addValToDist
     * @return
     */
    public static DistributionStats updateRunningDistStats(DistributionStats runningStats, Double val, 
            boolean addValToDist) {
        if(runningStats == null) {
            runningStats = new DistributionStats();
        }
        if(val != null) {
            runningStats.mean += val;
            runningStats.count++;
            if(addValToDist) {
                if(runningStats.dist == null) {
                    runningStats.dist = new LinkedList<Double>();
                }
                runningStats.dist.add(val);
            }
        }
        return runningStats;
    }
    
    /**
     * @param data
     * @param desiredSize
     * @return
     */
    public static List<Double> initializeDoubleList(List<Double> data, int desiredSize) {
        if (data == null || data.size() != desiredSize) {
            data = ProbabilityUtils.createProbabilities_Double(desiredSize, 0d);
        } else {
            for (int i = 0; i < data.size(); i++) {
                data.set(i, 0d);
            }
        }
        return data;
    }

    /**
     * @param data
     * @param desiredSize
     * @return
     */
    public static List<Integer> initializeIntegerList(List<Integer> data, int desiredSize) {
        if (data == null || data.size() != desiredSize) {
            data = ProbabilityUtils.createProbabilities(desiredSize, 0);
        } else {
            for (int i = 0; i < data.size(); i++) {
                data.set(i, 0);
            }
        }
        return data;
    }
    
    public static class DistributionStats {
        public Double mean;
        
        public Double std;        
        
        public Integer count;
        
        public List<Double> dist;     
        
        public DistributionStats() {
            mean = 0d;
            count = 0;
        }
    }
}
