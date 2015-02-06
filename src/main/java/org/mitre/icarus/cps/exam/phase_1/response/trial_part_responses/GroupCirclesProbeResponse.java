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
import org.mitre.icarus.cps.feature_vector.phase_1.GroupType;


/**
 * Subject/model response to probe trial where the subject/model is asked to draw circles
 * representing the "two-to-one boundary" for a group.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="GroupCirclesProbeResponse", namespace="IcarusCPD_1")
public class GroupCirclesProbeResponse extends TrialPartResponse {
	
	/** The subject/model indicated group circles */
	protected ArrayList<GroupCircle> groupCircles;
	
	/**
	 * No arg constructor.
	 */
	public GroupCirclesProbeResponse() {}
	
	/**
	 * Constructor that takes the groupCircles.
	 * 
	 * @param groupCircles the group circles
	 */
	public GroupCirclesProbeResponse(ArrayList<GroupCircle> groupCircles) {
		this.groupCircles = groupCircles;
	}

	/**
	 * Get the group circles. 
	 * 
	 * @return the group circles
	 */
	@XmlElement(name="GroupCircle")
	public ArrayList<GroupCircle> getGroupCircles() {
		return groupCircles;
	}

	/**
	 * Set the group circles. 
	 * 
	 * @param groupCircles the group circles
	 */
	public void setGroupCircles(ArrayList<GroupCircle> groupCircles) {
		this.groupCircles = groupCircles;
	}

	/**
	 * GroupCircle class simply contains the group, the center location of the group circle,
	 * and the radius (in grid units) of the group circle.
	 * 
	 * @author CBONACETO
	 *
	 */
	@XmlType(name="GroupCircle", namespace="IcarusCPD_1")
	public static class GroupCircle {		
		
		/** The group */
		protected GroupType group;
		
		/** The circle center location */
		protected GridLocation2D centerLocation;
		
		/** The circle radius (in grid units) */
		protected Double radius;	
		
		/** The time spent creating this group circle (e.g., time subject spent adjusting dragging center, sizing radius) (milliseconds) */
		protected Long time_ms;
		
		/**
		 * No arg constructor.
		 */
		public GroupCircle() {}
		
		/**
		 * Constructor that takes the group, centerLocation, and radius.
		 * 
		 * @param group the group
		 * @param centerLocation the center location
		 * @param radius the radius in grid units
		 */
		public GroupCircle(GroupType group, GridLocation2D centerLocation, Double radius) {
			this.group = group;
			this.centerLocation = centerLocation;
			this.radius = radius;
		}

		/**
		 * Get the group.
		 * 
		 * @return the group
		 */
		@XmlAttribute(name="group")
		public GroupType getGroup() {
			return group;
		}

		/**
		 * Set the group.
		 * 
		 * @param group the group
		 */
		public void setGroup(GroupType group) {
			this.group = group;
		}

		/**
		 * Get the center location of the circle.
		 * 
		 * @return the center location
		 */
		@XmlElement(name="CenterLocation")
		public GridLocation2D getCenterLocation() {
			return centerLocation;
		}

		/**
		 * Set the center location of the circle.
		 * 
		 * @param centerLocation the center location
		 */
		public void setCenterLocation(GridLocation2D centerLocation) {
			this.centerLocation = centerLocation;
		}

		/**
		 * Get the radius of the circle in grid units.
		 * 
		 * @return the radius in grid units
		 */
		@XmlAttribute(name="radius")
		public Double getRadius() {
			return radius;
		}

		/**
		 * Set the radius of the circle in grid units.
		 * 
		 * @param radius the radius in grid units
		 */
		public void setRadius(Double radius) {
			this.radius = radius;
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