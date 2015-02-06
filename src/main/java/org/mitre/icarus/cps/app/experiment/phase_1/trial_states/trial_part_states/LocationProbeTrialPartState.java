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

import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.AttackLocationProbeResponse_MultiLocation;
import org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.AttackLocationProbe_MultiLocation;

/**
 * A trial part state to enter location probe probabilities.
 * 
 * @author CBONACETO
 *
 */
public class LocationProbeTrialPartState extends ProbabilityProbeTrialPartState {
	
	/** The location probe */
	protected AttackLocationProbe_MultiLocation probe;
	
	/** The location probe response */
	protected AttackLocationProbeResponse_MultiLocation response;	
	
	public LocationProbeTrialPartState(int trialNumber, int trialPartNumber) {
		this(trialNumber, trialPartNumber, null);
	}
	
	public LocationProbeTrialPartState(int trialNumber, int trialPartNumber,
			AttackLocationProbe_MultiLocation probe) {
		super(trialNumber, trialPartNumber);
		this.probe = probe;
		this.response = new AttackLocationProbeResponse_MultiLocation();
	}	

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment.phase_1.trial_states.trial_part_states.TrialPartState#getProbe()
	 */
	@Override
	public AttackLocationProbe_MultiLocation getProbe() {
		return probe;
	}

	/**
	 * Set the location probe.
	 * 
	 * @param probe the location probe
	 */
	public void setProbe(AttackLocationProbe_MultiLocation probe) {
		this.probe = probe;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment.phase_1.trial_states.trial_part_states.TrialPartState#getResponse()
	 */
	@Override
	public AttackLocationProbeResponse_MultiLocation getResponse() {
		if(probe != null) {
			response.setGroup(probe.getAttackGroup());
		}
		response.setGroupAttackProbabilities(getProbabilitiyResponses(response.getGroupAttackProbabilities(), 
				(probe != null) ? probe.getLocations() : null, null));
		updateTimingData(response);		
		return response;
	}	
}