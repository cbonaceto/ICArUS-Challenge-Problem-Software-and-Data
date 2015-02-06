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
package org.mitre.icarus.cps.app.experiment.phase_2.trial_states;

import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.RedAction;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.RedActionSelectionProbe;

/**
 * @author cbonaceto
 *
 */
public class RedActionPresentationTrialPartState extends TrialPartState_Phase2 {
	
	/** The Red action selection probe. It will contain the red action chosen by the Red agent (not the human subject or model) */
	protected RedActionSelectionProbe probe;
	
	/** The Red action selection (to attack at a location, or to not attack at all) */
	protected RedAction redAction;

	public RedActionPresentationTrialPartState(int trialNumber, int trialPartNumber) {
		super(trialNumber, trialPartNumber);
	}
	
	public RedActionPresentationTrialPartState(int trialNumber, int trialPartNumber,
			RedActionSelectionProbe probe) {
		super(trialNumber, trialPartNumber);
		setProbe(probe);
	}	

	@Override
	public RedActionSelectionProbe getProbe() {
		return probe;
	}
	
	public void setProbe(RedActionSelectionProbe probe) {
		this.probe = probe;
		redAction = probe != null ? probe.getRedAction() : null;
	}

	public RedAction getRedAction() {
		return redAction;
	}
	
	public void setRedAction(RedAction redAction) {
		this.redAction = redAction;
	}	

	@Override
	public boolean validateAndUpdateProbeResponseData() {
		if(probe != null) {
			probe.setRedAction(redAction);
		}
		return true;
	}
	
	@Override
	public TrialPartType getTrialPartType() {
		return TrialPartType.RedActionPresentation;
	}
}