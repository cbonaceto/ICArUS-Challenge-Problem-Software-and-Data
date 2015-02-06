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

import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.AttackLocationProbeResponse_MultiGroup;
import org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.AttackLocationProbe_MultiGroup;

/**
 * A trial part state to enter group probe probabilities.
 * 
 * @author CBONACETO
 *
 */
public class GroupProbeTrialPartState extends ProbabilityProbeTrialPartState {

	/** The group probe */
	protected AttackLocationProbe_MultiGroup probe;
	
	/** The participant group probe response */
	protected AttackLocationProbeResponse_MultiGroup response;	
	
	public GroupProbeTrialPartState(int trialNumber, int trialPartNumber) {
		this(trialNumber, trialPartNumber, null);
	}
	
	public GroupProbeTrialPartState(int trialNumber, int trialPartNumber,
			AttackLocationProbe_MultiGroup probe) {
		super(trialNumber, trialPartNumber);
		this.probe = probe;
		this.response = new AttackLocationProbeResponse_MultiGroup();
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment.phase_1.trial_states.trial_part_states.TrialPartState#getProbe()
	 */
	@Override
	public AttackLocationProbe_MultiGroup getProbe() {
		return probe;
	}

	/**
	 * Set the group probe.
	 * 
	 * @param probe the group probe
	 */
	public void setProbe(AttackLocationProbe_MultiGroup probe) {
		this.probe = probe;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment.phase_1.trial_states.trial_part_states.TrialPartState#getResponse()
	 */
	@Override
	public AttackLocationProbeResponse_MultiGroup getResponse() {
		if(probe != null && probe.getAttackLocation() != null) {
			response.setAttackLocationId(probe.getAttackLocation().getLocationId());
		}					
		response.setGroupAttackProbabilities(getProbabilitiyResponses(response.getGroupAttackProbabilities(), 
				null, (probe != null) ? probe.getGroups() : null));
		updateTimingData(response);
		return response;
	}	
}