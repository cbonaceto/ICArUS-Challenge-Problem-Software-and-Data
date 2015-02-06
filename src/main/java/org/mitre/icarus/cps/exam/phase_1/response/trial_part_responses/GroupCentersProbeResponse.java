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
package org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.feature_vector.phase_1.GridLocation2D;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupCenter;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupType;
import org.mitre.icarus.cps.feature_vector.phase_1.LocationIntelReport;

/**
 * Subject/model indication of the center of gravity location for each group along a road.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="GroupCentersProbeResponse", namespace="IcarusCPD_1")
public class GroupCentersProbeResponse extends TrialPartResponse {

	/** The subject/model indicated group centers of gravity */
	protected ArrayList<GroupCenterResponse> groupCenters;
	
	/**
	 * No arg constructor.
	 */
	public GroupCentersProbeResponse() {}
	
	/**
	 * Constructor that takes the group centers.
	 * 
	 * @param groupCenters the group centers
	 */
	public GroupCentersProbeResponse(ArrayList<GroupCenterResponse> groupCenters) {
		this.groupCenters = groupCenters;
	}

	/**
	 * Get the group centers.
	 * 
	 * @return the group centers
	 */
	@XmlElement(name="GroupCenter")
	public ArrayList<GroupCenterResponse> getGroupCenters() {
		return groupCenters;
	}

	/**
	 * Set the group centers.
	 * 
	 * @param groupCenters the group centers
	 */
	public void setGroupCenters(ArrayList<GroupCenterResponse> groupCenters) {
		this.groupCenters = groupCenters;
	}
	
	/**
	 * GroupCenter response class simply contains the group center and the time the subject spent adjusting
	 * the group center.
	 * 
	 * @author CBONACETO
	 *
	 */
	@XmlType(name="GroupCenterResponse", namespace="IcarusCPD_1")
	public static class GroupCenterResponse extends GroupCenter {		
		
		/** The time spent adjusting the group center (e.g., time subject spent dragging the center) (milliseconds) */
		protected Long time_ms;
		
		/**
		 * No arg constructor.
		 */
		public GroupCenterResponse() {}
		
		/**
		 * Constructor that takes the group center.
		 * 
		 * @param groupCenter the group center
		 */
		public GroupCenterResponse(GroupCenter groupCenter) {
			super(groupCenter.getGroup(), groupCenter.getLocation(), groupCenter.getIntelReport());
		}	
		
		/**
		 * Constructor that takes the group and its center location.
		 *
		 * @param group the group
		 * @param centerLocation the group center location
		 */
		public GroupCenterResponse(GroupType group, GridLocation2D centerLocation) {
			super(group, centerLocation);
		}
		
		/**
		 * Constructor that takes the group, its center location, and the intel report at that location.
		 *
		 * @param group the group
		 * @param centerLocation the group center location
		 * @param locationIntel the location intel report
		 */
		public GroupCenterResponse(GroupType group, GridLocation2D centerLocation, LocationIntelReport locationIntel) {
			super(group, centerLocation, locationIntel);
		}
		
		/**
		 * Get the time spent creating this group circle (e.g., time subject spent adjusting dragging center, sizing radius) (milliseconds).
		 * FOR HUMAN SUBJECT USE ONLY.
		 * 
		 * @return the time in milliseconds
		 */
		@XmlAttribute(name="time_ms")
		public Long getTime_ms() {
			return time_ms;
		}

		/**
		 * Set the time spent creating this group circle (e.g., time subject spent adjusting dragging center, sizing radius) (milliseconds).
		 * FOR HUMAN SUBJECT USE ONLY.
		 * 
		 * @param time_ms the time in milliseconds
		 */
		public void setTime_ms(Long time_ms) {
			this.time_ms = time_ms;
		}
	}
}