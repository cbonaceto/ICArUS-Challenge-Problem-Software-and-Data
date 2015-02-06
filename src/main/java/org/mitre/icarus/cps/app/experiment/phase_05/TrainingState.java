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

/**
 * @author Ed Overly
 *
 */
public class TrainingState {
	
	/** Time spent on the training trial */
	protected long trainingTrialTime_ms;

	public long getTrainingTrialTime_ms() {
		return trainingTrialTime_ms;
	}

	public void setTrainingTrialTime_ms(long trainingTrialTime_ms) {
		this.trainingTrialTime_ms = trainingTrialTime_ms;
	}
	
	/*protected ArrayList<String> trainingText = new ArrayList<String>(){{
		add("Training text for the first trail");
		add("Training text for the second trail");
		add("Training text for the third trail");
		add("Training text for the fourth trail");}
	};
	
	// The number of training states.  Default is 4 
	protected int numTrainingStates = 4;
	
	// The current training state.  Default is 0 
	protected int trainingState = 0;

	public String getTrainingText(int index) {
		return trainingText.get(index);
	}
	
	public void setTrainingText(int index, String text) {
		trainingText.add(index, text);
	}

	public int getNumTrainingStates() {
		return numTrainingStates;
	}

	public void setNumTrainingStates(int numTrainingStates) {
		this.numTrainingStates = numTrainingStates;
	}
	
	public int getTrainingState() {
		return trainingState;
	}

	public void setTrainingState(int state) {
		trainingState = state;
	}*/
}
