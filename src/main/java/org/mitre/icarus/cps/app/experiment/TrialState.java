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
package org.mitre.icarus.cps.app.experiment;

import java.util.ArrayList;

import org.mitre.icarus.cps.app.experiment.TrialPartState;

/**
 * Abstract base class for trial states.  Trial states may contain one or more
 * trial part states for each trial part probe in the trial.
 * 
 * @author CBONACETO
 *
 */
public abstract class TrialState<T extends TrialPartState> {
	
	/** The trial number */
	protected final int trialNumber;	
	
	/** The states for each trial part */
	protected ArrayList<T> trialParts;
	
	/** The total time spent on the trial */
	protected long trialTime_ms;
	
	/**
	 * Constructor takes the trial number for this trial state.
	 * 
	 * @param trialNumber the trial number
	 */
	public TrialState(int trialNumber) {
		this.trialNumber = trialNumber;
	}	
	
	/**
	 * Populate the response object for this trial with data stored in the trial state.
	 */
	public abstract void updateTrialResponseData();

	/** Get the trial number.
	 * 
	 * @return the trial number
	 */
	public int getTrialNumber() {
		return trialNumber;
	}
	
	public int getNumTrialParts() {
		if(trialParts != null) {
			return trialParts.size();
		}
		return 0;
	}

	/**
	 * Get the trial part states for this trial.
	 * 
	 * @return the trial part states
	 */
	public ArrayList<T> getTrialParts() {
		return trialParts;
	}	

	/**
	 * Set the trial part states for this trial.
	 * 
	 * @param trialParts the trial part states
	 */
	public void setTrialParts(ArrayList<T> trialParts) {
		this.trialParts = trialParts;
	}

	/**
	 * Get the total time spent on this trial (in milliseconds).
	 * 
	 * @return the total time spent on this trial (in milliseconds).
	 */
	public long getTrialTime_ms() {
		return trialTime_ms;
	}

	/**
	 * Set the total time spent on this trial (in milliseconds).
	 * 
	 * @param trialTime_ms the total time spent on this trial (in milliseconds).
	 */
	public void setTrialTime_ms(long trialTime_ms) {
		this.trialTime_ms = trialTime_ms;
	}
}