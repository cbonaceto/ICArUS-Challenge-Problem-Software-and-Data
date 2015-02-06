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

import java.util.ArrayList;

import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.TroopAllocation;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.TroopAllocationResponse_MultiGroup;
import org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.TroopAllocationProbe_MultiGroup;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupType;

/**
 * A trial part state to allocate troops against two or more groups.
 * 
 * @author CBONACETO
 *
 */
public class TroopAllocationMultiGroupTrialPartState extends ProbabilityProbeTrialPartState {
	
	/** The multi-group troop allocation probe */
	protected TroopAllocationProbe_MultiGroup probe;
	
	/** The response to the troop allocation probe */
	protected TroopAllocationResponse_MultiGroup response;
	
	/** The current participant troop allocations */
	protected ArrayList<TroopAllocation> troopAllocations;	
	
	/** The normative troop allocations (used for the player only) */
	//protected ArrayList<TroopAllocation> normativeTroopAllocations;	
	
	/** The average human troop allocations (used for the player only) */
	//protected ArrayList<TroopAllocation> avgHumanTroopAllocations;
	
	public TroopAllocationMultiGroupTrialPartState(int trialNumber, int trialPartNumber,
			TroopAllocationProbe_MultiGroup probe) {
		super(trialNumber, trialPartNumber);
		this.probe = probe;
		this.response = new TroopAllocationResponse_MultiGroup();
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment.phase_1.trial_states.trial_part_states.TrialPartState#getProbe()
	 */
	@Override
	public TroopAllocationProbe_MultiGroup getProbe() {
		return probe;
	}

	/**
	 * Set the troop allocation probe.
	 * 
	 * @param probe the troop allocation probe
	 */
	public void setProbe(TroopAllocationProbe_MultiGroup probe) {
		this.probe = probe;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment.phase_1.trial_states.trial_part_states.TrialPartState#getResponse()
	 */
	@Override
	public TroopAllocationResponse_MultiGroup getResponse() {
		if(troopAllocations == null || 
				(currentSettings != null && currentSettings.size() != troopAllocations.size())) {
			if(currentSettings != null) {
				troopAllocations = new ArrayList<TroopAllocation>(currentSettings.size());	
				for(int i=0; i<currentSettings.size(); i++) {
					troopAllocations.add(null);
				}
			}
			else {
				troopAllocations = new ArrayList<TroopAllocation>();
			}
		}		
		if(currentSettings != null) {
			ArrayList<GroupType> groups = probe.getGroups();
			for(int i=0; i<currentSettings.size(); i++) {
				Integer currentSetting = currentSettings.get(i);
				Integer normalizedSetting = null;
				Long interactionTime = null;
				if(currentNormalizedSettings != null && i < currentNormalizedSettings.size()) {
					normalizedSetting = currentNormalizedSettings.get(i);
				}
				if(interactionTimes != null && i < interactionTimes.size()) {
					interactionTime = interactionTimes.get(i);
				}
				GroupType group = null;		
				if(groups != null && i < groups.size()) {
					group = groups.get(i);
				}
				TroopAllocation troopAllocation = new TroopAllocation(group,
						(normalizedSetting != null) ? normalizedSetting.doubleValue() : null,
								(currentSetting != null) ? currentSetting.doubleValue() : null);
				troopAllocation.setTime_ms(interactionTime);
				troopAllocations.set(i, troopAllocation);
			}			
		}	
		response.setTroopAllocations(troopAllocations);
		updateTimingData(response);
		return response;
	}	
}