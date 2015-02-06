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
package org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states;

import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.TrialPartResponse;
import org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.HumintReport;
import org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.TrialPartProbe;

/**
 * A trial part state to show subjects initial probabilities set from HUMINT data
 * in Task 5.
 * 
 * @author CBONACETO
 *
 */
public class ShowHumintTrialPartState extends TrialPartState_Phase1 {
	
	/** The HUMINT probabilities */
	protected HumintReport humintReport;
	
	public ShowHumintTrialPartState(int trialNumber, int trialPartNumber, HumintReport humintReport) {
		super(trialNumber, trialPartNumber);
		this.humintReport = humintReport;
	}	

	public HumintReport getHumintReport() {
		return humintReport;
	}

	public void setHumintReport(HumintReport humintReport) {
		this.humintReport = humintReport;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment.phase_1.trial_states.trial_part_states.TrialPartState#getProbe()
	 */
	@Override
	public TrialPartProbe getProbe() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment.phase_1.trial_states.trial_part_states.TrialPartState#getResponse()
	 */
	@Override
	public TrialPartResponse getResponse() {
		return null;
	}
}