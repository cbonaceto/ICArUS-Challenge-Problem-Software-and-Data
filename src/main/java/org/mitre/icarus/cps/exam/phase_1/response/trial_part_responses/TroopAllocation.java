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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.feature_vector.phase_1.GroupType;

/**
 * Subject/model allocation of troops against a group or at a location.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="TroopAllocation", namespace="IcarusCPD_1", 
propOrder={"group", "locationId", "allocation", "allocation_raw", "time_ms"})
public class TroopAllocation {
	
	/** The group to allocate troops against */
	protected GroupType group;
	
	/** The location to allocate troops against */
	protected String locationId;
	
	/** The normalized troop allocation (percent) */
	protected Double allocation;	
	
	/** The raw (non-normalized) troop allocation (percent) */
	protected Double allocation_raw;
	
	/** The time spent on this troop allocation (e.g., time subject spent adjusting troop allocation setting) (milliseconds) */
	protected Long time_ms;
	
	/**
	 * No arg constructor.
	 */
	public TroopAllocation() {}
	
	/**
	 * Constructor that takes the group and the allocation (percent).
	 * 
	 * @param group the group
	 * @param allocation the allocation (percent, e.g. 25)
	 */
	public TroopAllocation(GroupType group, Double allocation) {
		this(group, allocation, null);
	}	
	
	/**
	 * Constructor that takes the locationId and allocation (percent).
	 * 
	 * @param locationId the location ID
	 * @param allocation the allocation (percent)
	 */
	public TroopAllocation(String locationId, Double allocation) {
		this(locationId, allocation, null);
	}
	
	/**
	 * Constructor that takes the group, allocation (percent), and non-normalized allocation.
	 * 
	 * @param group the group
	 * @param allocation the allocation (percent)
	 * @param allocation_raw the non-normalized allocation
	 */
	public TroopAllocation(GroupType group, Double allocation, Double allocation_raw) {
		this.group = group;
		this.allocation = allocation;
		this.allocation_raw = allocation_raw;
	}
	
	/**
	 * Constructor that takes the locationId, allocation (percent), and non-normalized allocation.
	 * 
	 * @param locationId the location ID
	 * @param allocation the allocation (percent)
	 * @param allocation_raw the non-normalized allocation
	 */
	public TroopAllocation(String locationId, Double allocation, Double allocation_raw) {
		this.locationId = locationId;
		this.allocation = allocation;
		this.allocation_raw = allocation_raw;
	}

	/**
	 * Get the group.
	 * 
	 * @return - the group
	 */
	@XmlAttribute(name="group")
	public GroupType getGroup() {
		return group;
	}

	/**
	 * Set the group.
	 * 
	 * @param group - the group
	 */
	public void setGroup(GroupType group) {
		this.group = group;
	}
	
	/**
	 * Get the location ID.
	 * 
	 * @return the location ID
	 */
	@XmlAttribute(name="locationId")
	public String getLocationId() {
		return locationId;
	}

	/**
	 * Set the location ID.
	 * 
	 * @param locationId the location ID
	 */
	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	/**
	 * Get the normalized allocation (percent).
	 * 
	 * @return the normalized allocation
	 */
	@XmlAttribute(name="allocation")
	public Double getAllocation() {
		return allocation;
	}

	/**
	 * Set the normalized allocation (percent).
	 * 
	 * @param allocation the normalized allocation
	 */
	public void setAllocation(Double allocation) {
		this.allocation = allocation;
	}	
	
	/**
	 * Get the raw (non-normalized) allocation (percent).
	 * 
	 * @return the non-normalized allocation
	 */
	@XmlAttribute(name="allocation_raw")
	public Double getAllocation_raw() {
		return allocation_raw;
	}

	/**
	 * Set the raw (non-normalized) allocation (percent).
	 * 
	 * @param allocation_raw
	 */
	public void setAllocation_raw(Double allocation_raw) {
		this.allocation_raw = allocation_raw;
	}

	/**
	 * Get the time spent on this troop allocation (e.g., time subject spent adjusting troop allocation setting) (milliseconds).
	 * FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @return the time in milliseconds
	 */
	@XmlAttribute(name="time_ms")
	public Long getTime_ms() {
		return time_ms;
	}

	/**
	 * Set the time spent on this troop allocation (e.g., time subject spent adjusting troop allocation setting) (milliseconds).
	 * FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @param time_ms the time in milliseconds
	 */
	public void setTime_ms(Long time_ms) {
		this.time_ms = time_ms;
	}
}