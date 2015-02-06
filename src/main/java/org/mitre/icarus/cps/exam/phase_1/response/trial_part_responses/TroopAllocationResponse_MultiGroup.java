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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Subject/model response to a troop allocation decision where troops are allocated
 * against one or more groups.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="TroopAllocationResponse_MultiGroup", namespace="IcarusCPD_1")
public class TroopAllocationResponse_MultiGroup extends TrialPartResponse {
	
	/** The troop allocations (percent) against each group */
	protected ArrayList<TroopAllocation> troopAllocations;
	
	/** The troop allocation score (S2) */
	protected Double troopAllocationScore_s2;
	
	/**
	 * No arg constructor.
	 */
	public TroopAllocationResponse_MultiGroup() {}
	
	/**
	 * Constructor that takes the troopAllocations.
	 * 
	 * @param troopAllocations the troop allocations
	 */
	public TroopAllocationResponse_MultiGroup(ArrayList<TroopAllocation> troopAllocations) {
		this.troopAllocations = troopAllocations;
	}
	
	/**
	 * Get the troop allocations against each group.
	 * 
	 * @return the troop allocations
	 */
	@XmlElement(name="TroopAllocation")
	public ArrayList<TroopAllocation> getTroopAllocations() {
		return troopAllocations;
	}

	/**
	 * Set the troop allocations against each group.
	 * 
	 * @param troopAllocations the troop allocations
	 */
	public void setTroopAllocations(ArrayList<TroopAllocation> troopAllocations) {
		this.troopAllocations = troopAllocations;
	}

	/**
	 * Get the troop allocation score (S2).
	 * 
	 * @return the troop allocation score
	 */
	@XmlElement(name="TroopAllocationScore_s2")
	public Double getTroopAllocationScore_s2() {
		return troopAllocationScore_s2;
	}

	/**
	 * Set the troop allocation score (S2).
	 * 
	 * @param troopAllocationScore_s2 the troop allocation score
	 */
	public void setTroopAllocationScore_s2(Double troopAllocationScore_s2) {
		this.troopAllocationScore_s2 = troopAllocationScore_s2;
	}	
}