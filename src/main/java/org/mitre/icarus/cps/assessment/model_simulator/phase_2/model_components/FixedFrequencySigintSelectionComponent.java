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
package org.mitre.icarus.cps.assessment.model_simulator.phase_2.model_components;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import org.mitre.icarus.cps.feature_vector.phase_2.BlueLocation;

/**
 * A basic SIGINT selection model component implementation that selects SIGINT
 * at the location with highest P(Attack) with probability highestPaSelectionFreq.
 * 
 * @author CBONACETO
 */
public class FixedFrequencySigintSelectionComponent implements ISigintSelectionComponent {
    
    /** The random number generator to use */
    private Random rand;
    
    /** The frequency with which to select SIGINT at the location with the highest P(attack) 
     (default is 0.75, 75% of the time) */
    private double highestPaSelectionFreq;
    
    public FixedFrequencySigintSelectionComponent() {
        this(0.75d, new Random(1));
    }
    
    public FixedFrequencySigintSelectionComponent(double highestPaSelectionFreq) {
        this(highestPaSelectionFreq, new Random(1));
    }
    
    public FixedFrequencySigintSelectionComponent(double highestPaSelectionFreq , Random rand) {
        this.highestPaSelectionFreq = highestPaSelectionFreq;
        this.rand = rand == null ? new Random(1) : rand;
    }

    public double getHighestPaSelectionFreq() {
        return highestPaSelectionFreq;
    }

    public void setHighestPaSelectionFreq(double highestPaSelectionFreq) {
        this.highestPaSelectionFreq = highestPaSelectionFreq;
    }    
    
    @Override
    public String selectSigintLocation(List<BlueLocation> locations, 
            List<Double> attackProbabilities) {
        //Selects the location with the highest P(Attack) highestPaSelectionFreq % of the time        
        if (locations != null && !locations.isEmpty()
                && attackProbabilities != null && attackProbabilities.size() == locations.size()) {
            Double maxAttackProb = null;
            String bestLocation = null;
            String otherLocation = null;
            Iterator<BlueLocation> locationIter = locations.iterator();
            Iterator<Double> attackProbIter = attackProbabilities.iterator();
            while (locationIter.hasNext()) {
                Double attackProb = attackProbIter.next();
                String locationId = locationIter.next().getId();
                if (maxAttackProb == null || attackProb > maxAttackProb) {
                    otherLocation = bestLocation;
                    maxAttackProb = attackProb;                    
                    bestLocation = locationId;
                } else if(otherLocation == null) {
                    otherLocation = locationId;
                }
            }    
            //Double num = rand.nextDouble();
            //System.out.println(num + ", " + highestPaSelectionFreq);
            if (otherLocation == null || rand.nextDouble() <= highestPaSelectionFreq) {
                //Select the SIGINT location with the highest P(Attack) 
                //System.out.println("Selecting SIGINT at best location");
                return bestLocation;
            } else {
                //Select a SIGINT loccation other than the one with the highest P(Attack)
                //System.out.println("Selecting SIGINT at other location");
                return otherLocation;
            }
        }
        return null;
    }
}
