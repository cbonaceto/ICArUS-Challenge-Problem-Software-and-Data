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

import java.util.HashMap;
import java.util.Map;

import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.BlueAction;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.BlueAction.BlueActionType;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.BlueActionSelectionProbe;

/**
 * Trial part state to select the Blue action (divert, do not divert) at one or more Blue locations.
 * 
 * @author cbonaceto
 *
 */
public class BlueActionSelectionOrPresentationTrialPartState extends TrialPartState_Phase2 {
	
	/** The Blue action selection probe */
	protected BlueActionSelectionProbe probe;
	
	/** The Blue action selection at each location (maps location ID to the action selection at the location) */
	protected Map<String, BlueActionType> blueActionsMap;	

	public BlueActionSelectionOrPresentationTrialPartState(int trialNumber, int trialPartNumber) {
		super(trialNumber, trialPartNumber);
		blueActionsMap = new HashMap<String, BlueActionType>();
	}
	
	public BlueActionSelectionOrPresentationTrialPartState(int trialNumber, int trialPartNumber, 
			BlueActionSelectionProbe probe) {
		super(trialNumber, trialPartNumber);
		blueActionsMap = new HashMap<String, BlueActionType>();
		setProbe(probe);
	}

	@Override
	public BlueActionSelectionProbe getProbe() {
		return probe;
	}

	public void setProbe(BlueActionSelectionProbe probe) {
		this.probe = probe;
		blueActionsMap.clear();
		if(probe != null && probe.getBlueActions() != null && !probe.getBlueActions().isEmpty()) {
			for(BlueAction blueAction : probe.getBlueActions()) {
				blueActionsMap.put(blueAction.getLocationId(), blueAction.getAction());				
			}
		}	
	}
	
	/**
	 * @return
	 */
	public boolean isParticipantChoosesBlueActions() {
		return probe != null ? !probe.isDataProvidedToParticipant() : false;
	}
	
	/**
	 * @return
	 */
	public int getNumLocations() {
		return blueActionsMap.size();
	}
	
	public BlueActionType getBlueAction(String locationId) {
		return blueActionsMap.get(locationId);
	}
	
	public void setBlueAction(String locationId, BlueActionType blueAction) {
		blueActionsMap.put(locationId, blueAction);
	}	

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.app.experiment.phase_2.trial_states.TrialPartState_Phase2#validateAndUpdateProbeResponseData()
	 */
	@Override
	public boolean validateAndUpdateProbeResponseData() {
		if(probe != null && probe.getBlueActions() != null && !probe.getBlueActions().isEmpty()) {
			//Update the Blue action selections, mark the response as invalid if an action selection is missing at a location
			boolean responseValid = true;
			for(BlueAction blueAction : probe.getBlueActions()) {
				blueAction.setAction(blueActionsMap.get(blueAction.getLocationId()));
				if(blueAction.getAction() ==  null) {
					responseValid = false;
				}
			}
			return responseValid;
		}
		return true;
	}
	
	@Override
	public TrialPartType getTrialPartType() {
		return TrialPartType.BlueActionSelectionOrPresentation;
	}
}