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
package org.mitre.icarus.cps.app.experiment.phase_05;

import java.io.BufferedWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * ExamTiming is used to store and calculate the timing of the different phases 
 * and trials within a phase for both training and test phases.  An ArrayList contains 
 * a Timing Instance for each phase and trial with the elapsed milliseconds.
 * 
 * @author EOVERLY
 *
 */
public class ExamTiming {
	  
    private ArrayList <TimingInstance> timings = new ArrayList<TimingInstance>();

    public void createTimingInstance(String phaseName, int currentPhase, int currentTrial){
    	//System.out.println("ExamTiming.createTimingInstance phase= " + currentPhase + " trial= " + currentTrial);
        TimingInstance ti = new TimingInstance();
        ti.startMillis = System.currentTimeMillis();
        ti.phase = currentPhase;
        ti.trial = currentTrial;
        ti.phaseName = phaseName;
        //System.out.println("startMillis= " + ti.startMillis + " phase= " + ti.phase + " trial= " + ti.trial);
        timings.add(ti);
        //System.out.println("examTiming.size= " + timings.size());
    }

    public void stopTimingInstance(int currentPhase, int currentTrial){
    	if(timings.size() == 0)
    		return;
    	//System.out.println("ExamTiming.stopTimingInstance phase= " + currentPhase + " trial= " + currentTrial);
        int i = timings.size();
        TimingInstance ti = (TimingInstance) timings.get(i-1);
        timings.remove(ti);
        long stopMillis = System.currentTimeMillis();
        ti.elapsedMillis = stopMillis - ti.startMillis;
        timings.add(ti);
        //System.out.println("timings.size= " + timings.size());
        //System.out.println("elapsed time= " + ti.elapsedMillis);
    }

    public ArrayList <TimingInstance> getExamTimings(){
        return timings;
    }

    public class TimingInstance {
        long startMillis;
        long elapsedMillis;
        long lastProbabilityMillis = 0L;
        /*
         * elapsed time for the user to set the probability
         * we can have several probability timings for a particular trial
         */
        ArrayList<Long> probabilityTimingArray = new ArrayList<Long>();
        int phase;
        int trial;
        String phaseName;
        
        /*
         * when the user is presented with a layer selection
         */
        long startLayerSelectMillis;
        
        /*
         * elapsed time from when the user was presented with a layer selection
         */
        long elapsedLayerSelectMillis;
    }
    
    /* 
     * Set the elapsed time it took the user to select the intel layer selection for a given phase and trial
     */  
    
    public void stopLayerSelectTiming(int currentPhase, int currentTrial){
    	if(timings.size() == 0)
    		return;
    	//System.out.println("ExamTiming.stopLayerSelectTiming phase= " + currentPhase + " trial= " + currentTrial);

    	TimingInstance ti = null;
		for(int i=0; i < timings.size(); i++){
			ti = timings.get(i);
			if((ti.phase == currentPhase) && (ti.trial == currentTrial)){  // find the right timing instance
				ti.elapsedLayerSelectMillis = System.currentTimeMillis() - ti.startLayerSelectMillis;
		    	System.out.println("ExamTiming.stopLayerSelectTiming timing= " + ti.elapsedLayerSelectMillis);
				return;
			}
		}
    }
    
    /* 
     * Set the time when the user was presented with an intel layer selection for a given phase and trial
     */  
    
    public void startLayerSelectTiming(int currentPhase, int currentTrial){
    	if(timings.size() == 0)
    		return;
    	//System.out.println("ExamTiming.startLayerSelectTiming phase= " + currentPhase + " trial= " + currentTrial);

    	TimingInstance ti = null;
		for(int i=0; i < timings.size(); i++){
			ti = timings.get(i);
			if((ti.phase == currentPhase) && (ti.trial == currentTrial)){  // find the right timing instance
				ti.startLayerSelectMillis = System.currentTimeMillis();
				return;
			}
		}
    }
    
    /* 
     * Return the elapsed time it took the user to select the intel layer selection for a given phase and trial
     */  
    
    public long getLayerSelectTiming(int currentPhase, int currentTrial){
    	if(timings.size() == 0)
    		return 0L;
    	//System.out.println("ExamTiming.getLayerSelectTiming phase= " + currentPhase + " trial= " + currentTrial);

    	TimingInstance ti = null;
		for(int i=0; i < timings.size(); i++){
			ti = timings.get(i);
			if((ti.phase == currentPhase) && (ti.trial == currentTrial)){  // find the right timing instance
				System.out.println("ExamTiming.getLayerSelectTiming returning timing=" + ti.elapsedLayerSelectMillis);
				return ti.elapsedLayerSelectMillis;
			}
		}
		return 0L;  // couldn't find the timing instance
    }
    
    
    /* 
     * Set the elapsed time it took for the user to set the probability vector in a test trial
     */  
    
    public void setProbabilityTiming(int currentPhase, int currentTrial){
    	if(timings.size() == 0)
    		return;
    	//System.out.println("setProbabilityTiming phase= " + currentPhase + " trial= " + currentTrial);
    	long tempMillis = 0L;
    	long systemTime = System.currentTimeMillis();
    	TimingInstance ti = null;
		for(int i=0; i < timings.size(); i++){
			ti = timings.get(i);
			if((ti.phase == currentPhase) && (ti.trial == currentTrial)){  // find the right timing instance
				if(ti.probabilityTimingArray.size() == 0){
					tempMillis = systemTime - ti.startMillis;
				} else {
					tempMillis = systemTime - ti.lastProbabilityMillis;
				}			
				ti.probabilityTimingArray.add(tempMillis);
				ti.lastProbabilityMillis = systemTime;
				return;
			}
		}
    }
    
    /* 
     * Set the elapsed time it took for the user to set the probability vector in a test trial
     */  
    
    public long getProbabilityTiming(int currentPhase, int currentTrial, int index){
    	if(timings.size() == 0)
    		return -1L;
    	//System.out.println("ExamTiming.getProbabilityTiming phase= " + currentPhase + " trial= " + currentTrial + " index= " + index);
 
    	TimingInstance ti = null;
		for(int i=0; i < timings.size(); i++){
			ti = timings.get(i);
			if((ti.phase == currentPhase) && (ti.trial == currentTrial)){  // find the right timing instance
				if((ti.probabilityTimingArray.size() == 0) || (index + 1 > ti.probabilityTimingArray.size())){
					System.err.println("ExamTiming.getProbabilityTiming index out of range, index= " + index);
					return -1L;
				} else {
					//System.out.println("ExamTiming.getProbabilityTiming array size= " + ti.probabilityTimingArray.size());
					return ti.probabilityTimingArray.get(index);
				}			
			}
		}
		return -1L;
    }
    
    /* 
     * Return the sum of the timings for a given phase and trial.
     * There might be more than one TimingInstance if the user hit the back button
     */ 
    
    public long getTiming(int phase, int trial){
    	//System.out.println("ExamTiming.getTiming phase= " + phase + " trial= " + trial);
    	
		TimingInstance ti = null;
		long elapsedTime = 0L;
		for(int i=0; i < timings.size(); i++){
			ti = timings.get(i);
			if((ti.phase == phase) && (ti.trial == trial)){
				elapsedTime = elapsedTime + ti.elapsedMillis;
			}
		}
		return elapsedTime;
    }
  
    /* write timing data to a text file */
    @Deprecated
    public void saveTimingToFile(String fileName){
    	TimingInstance ti = null;
    	String buf = "";
    	int size = timings.size();
    	if(size == 0)
    		return;
    	//System.out.println("saveTimingToFile ");
    	
    	try {
    		BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
    		for(int i=0; i < size; i++){
    			ti = (TimingInstance) timings.get(i);
    			buf = ti.phaseName + " phase " + ti.phase + " trial " + ti.trial + " elapsed time (seconds) " + ti.elapsedMillis/1000; 
        		out.write(buf);
        		out.newLine();
    		}
        	out.close();
		} catch (IOException e) {
			System.err.println("Error, could not write timing data to " + fileName);
			e.printStackTrace();
		} 	
    }
}

