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

import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.TroopSelectionResponse_MultiGroup;
import org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.TroopSelectionProbe_MultiGroup;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupType;

/**
 * A trial part state to indicate which group to send troops against.
 * 
 * @author CBONACETO
 *
 */
public class TroopSelectionMultiGroupTrialPartState extends TrialPartState_Phase1 {
	
	/** The troop selection probe */
	protected TroopSelectionProbe_MultiGroup probe;
	
	/** The response to the troop selection probe */
	protected TroopSelectionResponse_MultiGroup response;	

	public TroopSelectionMultiGroupTrialPartState(int trialNumber,	int trialPartNumber) {
		this(trialNumber, trialPartNumber, null);
	}
	
	public TroopSelectionMultiGroupTrialPartState(int trialNumber,	int trialPartNumber, TroopSelectionProbe_MultiGroup probe) {
		super(trialNumber, trialPartNumber);
		this.probe = probe;
		this.response = new TroopSelectionResponse_MultiGroup();
	}	
	
	@Override
	public TroopSelectionProbe_MultiGroup getProbe() {
		return probe;
	}

	public void setProbe(TroopSelectionProbe_MultiGroup probe) {
		this.probe = probe;
	}

	public GroupType getTroopSelectionGroup() {
		return response.getGroup();
	}
	
	public void setTroopSelectionGroup(GroupType group) {
		response.setGroup(group);
	}

	@Override
	public TroopSelectionResponse_MultiGroup getResponse() {		
		updateTimingData(response);
		return response;
	}
}