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
import java.util.List;

import org.mitre.icarus.cps.app.widgets.map.phase_1.objects.GroupCenterPlacemark;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.GroupCentersProbeResponse;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.GroupCentersProbeResponse.GroupCenterResponse;
import org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.GroupCentersProbe;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupCenter;

/**
 * A trial part state where the subject draws group circles for each group (Task 3).
 * 
 * @author CBONACETO
 *
 */
public class GroupCentersProbeTrialPartState extends TrialPartState_Phase1 {
	
	/** The current participant group center placemarks for each group */
	protected List<GroupCenterPlacemark> groupCenters;
	
	/** The current participant group centers (used for the player only) */
	protected List<GroupCenter> participantGroupCenters;
	
	/** The normative group centers for each group (used for the player only) */
	protected List<GroupCenter> normativeGroupCenters;
	
	/** The average human group centers for each group (used for the player only) */
	protected List<GroupCenter> avgHumanGroupCenters;
	
	/** The group centers probe */
	protected GroupCentersProbe probe;
	
	/** The group centers probe response */
	protected GroupCentersProbeResponse response;
	
	/**
	 * Constructor takes the trial number, the trial part number, and the group centers probe.
	 * 
	 * @param trialNumber the trial number
	 * @param trialPartNumber the trial part number
	 * @param probe the group centers probe
	 */
	public GroupCentersProbeTrialPartState(int trialNumber, int trialPartNumber,
			GroupCentersProbe probe) {
		super(trialNumber, trialPartNumber);
		this.probe = probe;
		this.response = new GroupCentersProbeResponse();
	}

	/**
	 * Get the subject-created group centers.
	 * 
	 * @return the group centers
	 */
	public List<GroupCenterPlacemark> getGroupCenters() {
		return groupCenters;
	}

	/**
	 * Set the subject-created group centers.
	 * 
	 * @param groupCenters the group centers
	 */
	public void setGroupCenters(List<GroupCenterPlacemark> groupCenters) {
		this.groupCenters = groupCenters;
	}

	/**
	 * @return
	 */
	public List<GroupCenter> getParticipantGroupCenters() {
		return participantGroupCenters;
	}

	/**
	 * @param participantGroupCenters
	 */
	public void setParticipantGroupCenters(List<GroupCenter> participantGroupCenters) {
		this.participantGroupCenters = participantGroupCenters;
	}
	
	/**
	 * @return
	 */
	public boolean isNormativeGroupCentersPresent() {
		return normativeGroupCenters != null && !normativeGroupCenters.isEmpty();
	}

	/**
	 * @return
	 */
	public List<GroupCenter> getNormativeGroupCenters() {
		return normativeGroupCenters;
	}

	/**
	 * @param normativeGroupCenters
	 */
	public void setNormativeGroupCenters(List<GroupCenter> normativeGroupCenters) {
		this.normativeGroupCenters = normativeGroupCenters;
	}
	
	/**
	 * @return
	 */
	public boolean isAvgHumanGroupCentersPresent() {
		return avgHumanGroupCenters != null && !avgHumanGroupCenters.isEmpty();
	}

	/**
	 * @return
	 */
	public List<GroupCenter> getAvgHumanGroupCenters() {
		return avgHumanGroupCenters;
	}

	/**
	 * @param avgHumanGroupCenters
	 */
	public void setAvgHumanGroupCenters(List<GroupCenter> avgHumanGroupCenters) {
		this.avgHumanGroupCenters = avgHumanGroupCenters;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment.phase_1.trial_states.trial_part_states.TrialPartState#getProbe()
	 */
	@Override
	public GroupCentersProbe getProbe() {
		return probe;
	}

	/**
	 * Set the group centers probe.
	 * 
	 * @param probe the group centers probe
	 */
	public void setProbe(GroupCentersProbe probe) {
		this.probe = probe;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment.phase_1.trial_states.trial_part_states.TrialPartState#getResponse()
	 */
	@Override
	public GroupCentersProbeResponse getResponse() {
		ArrayList<GroupCenterResponse> centers = null;
		if(groupCenters != null && !groupCenters.isEmpty()) {
			centers = new ArrayList<GroupCenterResponse>(groupCenters.size());
			for(GroupCenterPlacemark groupCenter : groupCenters) {
				GroupCenterResponse centerResponse = new GroupCenterResponse(groupCenter.getFeature().getGroup(), 
						groupCenter.getCenterLocation());
				centerResponse.setTime_ms(groupCenter.getEditTime());
				centers.add(centerResponse);
			}
		}
		response.setGroupCenters(centers);
		updateTimingData(response);
		return response;
	}		
}