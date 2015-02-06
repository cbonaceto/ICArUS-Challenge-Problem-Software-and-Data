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
package org.mitre.icarus.cps.app.experiment.ui_study.trial_states;

import org.mitre.icarus.cps.app.experiment.TrialPartState;
import org.mitre.icarus.cps.app.experiment.ui_study.exam.SpatialPresentationBlock;

/**
 * @author CBONACETO
 *
 */
public class TimedHitsDisplayTrialPartState extends TrialPartState {
	
	protected SpatialPresentationBlock spatialPresentationBlock;
	
	/** The probability entry state to show after the timed hits display */
	protected ProbabilityEntryTrialPartState probabilityEntryTrialPart;

	public TimedHitsDisplayTrialPartState(int trialNumber, int trialPartNumber, 
			SpatialPresentationBlock spatialPresentationBlock) {
		super(trialNumber, trialPartNumber);
		this.spatialPresentationBlock = spatialPresentationBlock;
	}

	public SpatialPresentationBlock getSpatialPresentationBlock() {
		return spatialPresentationBlock;
	}

	public ProbabilityEntryTrialPartState getProbabilityEntryTrialPart() {
		return probabilityEntryTrialPart;
	}

	public void setProbabilityEntryTrialPart(
			ProbabilityEntryTrialPartState probabilityEntryTrialPart) {
		this.probabilityEntryTrialPart = probabilityEntryTrialPart;
	}
}