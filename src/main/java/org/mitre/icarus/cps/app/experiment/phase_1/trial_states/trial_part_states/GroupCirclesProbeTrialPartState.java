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

import org.mitre.icarus.cps.app.widgets.map.phase_1.objects.CircleShape;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.GroupCirclesProbeResponse;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.GroupCirclesProbeResponse.GroupCircle;
import org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.GroupCirclesProbe;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupType;

/**
 * A trial part state where the subject draws group circles for each group (Task 3).
 * 
 * @author CBONACETO
 *
 */
public class GroupCirclesProbeTrialPartState extends TrialPartState_Phase1 {
	
	/** The group circles for each group */
	protected List<GroupCircleShape> groupCircles;
	
	/** The participant group circles for each group (used for the player only) */
	protected List<GroupCircle> participantGroupCircles;	
	
	/** The normative group circles for each group (used for the player only) */
	protected List<GroupCircle> normativeGroupCircles;	
	
	/** The average human group circles for each group (used for the player only) */
	protected List<GroupCircle> avgHumanGroupCircles;

	/** The group circles probe */
	protected GroupCirclesProbe probe;	
	
	/** The group circles probe response */
	protected GroupCirclesProbeResponse response;	
	
	/**
	 * Constructor that takes the trial number, trial part number, and group circles probe.
	 * 
	 * @param trialNumber
	 * @param trialPartNumber
	 * @param probe
	 */
	public GroupCirclesProbeTrialPartState(int trialNumber, int trialPartNumber,
			GroupCirclesProbe probe) {
		super(trialNumber, trialPartNumber);
		this.probe = probe;
		this.response = new GroupCirclesProbeResponse();
	}

	/**
	 * @return
	 */
	public List<GroupCircleShape> getGroupCircles() {
		return groupCircles;
	}

	/**
	 * @param groupCircles
	 */
	public void setGroupCircles(List<GroupCircleShape> groupCircles) {
		this.groupCircles = groupCircles;
	}

	/**
	 * @return
	 */
	public List<GroupCircle> getParticipantGroupCircles() {
		return participantGroupCircles;
	}

	/**
	 * @param participantGroupCircles
	 */
	public void setParticipantGroupCircles(List<GroupCircle> participantGroupCircles) {
		this.participantGroupCircles = participantGroupCircles;
	}
	
	/**
	 * @return
	 */
	public boolean isNormativeGroupCirclesPresent() {
		return normativeGroupCircles != null && !normativeGroupCircles.isEmpty();
	}

	/**
	 * @return
	 */
	public List<GroupCircle> getNormativeGroupCircles() {
		return normativeGroupCircles;
	}

	/**
	 * @param normativeGroupCircles
	 */
	public void setNormativeGroupCircles(List<GroupCircle> normativeGroupCircles) {
		this.normativeGroupCircles = normativeGroupCircles;
	}

	/**
	 * @return
	 */
	public boolean isAvgHumanGroupCirclesPresent() {
		return avgHumanGroupCircles != null && !avgHumanGroupCircles.isEmpty();
	}
	
	/**
	 * @return
	 */
	public List<GroupCircle> getAvgHumanGroupCircles() {
		return avgHumanGroupCircles;
	}

	/**
	 * @param avgHumanGroupCircles
	 */
	public void setAvgHumanGroupCircles(List<GroupCircle> avgHumanGroupCircles) {
		this.avgHumanGroupCircles = avgHumanGroupCircles;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment.phase_1.trial_states.trial_part_states.TrialPartState#getProbe()
	 */
	@Override
	public GroupCirclesProbe getProbe() {
		return probe;
	}

	/**
	 * @param probe
	 */
	public void setProbe(GroupCirclesProbe probe) {
		this.probe = probe;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment.phase_1.trial_states.trial_part_states.TrialPartState#getResponse()
	 */
	@Override
	public GroupCirclesProbeResponse getResponse() {
		ArrayList<GroupCircle> circles = null;
		if(groupCircles != null && !groupCircles.isEmpty()) {
			circles = new ArrayList<GroupCircle>(groupCircles.size());
			for(GroupCircleShape groupCircle : groupCircles) {
				GroupCircle circle = new GroupCircle(groupCircle.group, groupCircle.circle.getCenterLocation(), 
						groupCircle.circle.getRadius());
				circle.setTime_ms(groupCircle.circle.getEditTime());
				circles.add(circle);
			}
		}
		response.setGroupCircles(circles);
		updateTimingData(response);
		return response;
	}
	
	/**
	 * Contains the group and the group circle for the group (a CircleShape map object).
	 * 
	 * @author CBONACETO
	 *
	 */
	public static class GroupCircleShape implements Cloneable {
		/** The group the circle is for */
		public final GroupType group;
		
		/** The circle */
		public final CircleShape circle;
		
		public GroupCircleShape(GroupType group, CircleShape circle) {
			if(group == null) {
				throw new IllegalArgumentException("Group cannot be null");
			}
			if(circle == null) {
				throw new IllegalArgumentException("Circle cannot be null");
			}
			this.group = group;
			this.circle = circle;
		}
		
		/** Copy constructor */
		public GroupCircleShape(GroupCircleShape copy) {
			this(copy.group, new CircleShape(copy.circle));
		}

		public GroupType getGroup() {
			return group;
		}

		public CircleShape getCircle() {
			return circle;
		}

		@Override
		protected GroupCircleShape clone() throws CloneNotSupportedException {
			return new GroupCircleShape(this);
		}	
	}
}