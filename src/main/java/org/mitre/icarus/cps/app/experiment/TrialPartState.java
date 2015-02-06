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

import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.TrialPartResponse;

/**
 * Abstract base class for states corresponding to a trial part probe.
 * 
 * @author CBONACETO
 *
 */
public abstract class TrialPartState {
	
	/** Trial number */
	protected final int trialNumber;
	
	/** Trial part number */
	protected final int trialPartNumber;	
	
	/** Time spent on the trial part (in milliseconds) */
	protected long trialPartTime_ms = 0;
	
	public TrialPartState(int trialNumber, int trialPartNumber) {
		this.trialNumber = trialNumber;
		this.trialPartNumber = trialPartNumber;
	}	
	
	/**
	 * Get the time spent on the trial part (milliseconds).
	 * 
	 * @return the time spent on the trial part (milliseconds)
	 */
	public long getTrialPartTime_ms() {
		return trialPartTime_ms;
	}

	/**
	 * Set the time spent on the trial part (milliseconds).
	 * 
	 * @param trialPartTime_ms the time spent on the trial part (milliseconds)
	 */
	public void setTrialPartTime_ms(long trialPartTime_ms) {
		this.trialPartTime_ms = trialPartTime_ms;
	}
	
	/**
	 * Sets the trial part time in the response data object to the trial part time recorded in this
	 * trial part state.
	 * 
	 * @param response the response data object
	 */
	protected void updateTimingData(TrialPartResponse response) {
		if(response != null) {
			response.setTrialPartTime_ms(trialPartTime_ms);
		}
	}
}