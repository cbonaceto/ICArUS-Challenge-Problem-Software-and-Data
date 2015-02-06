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
package org.mitre.icarus.cps.app.experiment.ui_study.exam;

import java.util.ArrayList;

import org.mitre.icarus.cps.exam.base.testing.IcarusTestTrial;

/**
 * Base class for UI study trials.
 * 
 * @author CBONACETO
 *
 */

public abstract class UIStudyTrial extends IcarusTestTrial {
	
	/** The default initial probabilities ("anchors"). If not specified, initial probabilities
	 * will all start at 0. */
	protected ArrayList<ItemProbability> uiDefaults;
	
	public ArrayList<ItemProbability> getUiDefaults() {
		return uiDefaults;
	}

	public void setUiDefaults(ArrayList<ItemProbability> uiDefaults) {
		this.uiDefaults = uiDefaults;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public TestTrialType getTestTrialType() {
		return TestTrialType.Custom;
	}
	
	/**
	 * Get the response to the trial.
	 * 
	 * @return the response
	 */
	public abstract IcarusTrialResponse_UIStudy getTrialResponse();
}