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
package org.mitre.icarus.cps.exam.phase_1.response;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.AttackLocationProbeResponse_MultiGroup;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.AttackLocationProbeResponse_MultiLocation;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.Task_7_INTLayerPurchase;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.TroopAllocationResponse_MultiLocation;

/**
 * Subject/model response to a Task 7 trial.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="Task_7_TrialResponse", namespace="IcarusCPD_1", 
		propOrder={"attackLocationResponse_groups", "attackLocationResponse_locations", 
		"troopAllocationResponse", "intLayerPurchaseTime_ms", "intLayerPurchases"})
public class Task_7_TrialResponse extends IcarusTrialResponse_Phase1 {
		
	/** Response to the initial attack location probe (before the map is shown) on the likelihood
	 * of attack by each group*/
	protected AttackLocationProbeResponse_MultiGroup attackLocationResponse_groups;
	
	/** Response to the attack location probe (after the map is shown) on the likelihood
	 * of attack at each location */
	protected AttackLocationProbeResponse_MultiLocation attackLocationResponse_locations;
	
	/** The troop allocation probe after the attack location probe */
	protected TroopAllocationResponse_MultiLocation troopAllocationResponse;
	
	/** The total time spent purchasing INT layers (in milliseconds) */
	protected Long intLayerPurchaseTime_ms;
	
	/** INT layer purchases */
	protected ArrayList<Task_7_INTLayerPurchase> intLayerPurchases;	
	
	/**
	 * Get the response to the group probe before the map is shown.
	 * 
	 * @return the group probe response
	 */
	@XmlElement(name="GroupResponse")
	public AttackLocationProbeResponse_MultiGroup getAttackLocationResponse_groups() {
		return attackLocationResponse_groups;
	}

	/**
	 * Set the response to the group probe before the map is shown.
	 * 
	 * @param attackLocationResponse_groups the group probe response
	 */
	public void setAttackLocationResponse_groups(
			AttackLocationProbeResponse_MultiGroup attackLocationResponse_groups) {
		this.attackLocationResponse_groups = attackLocationResponse_groups;
	}

	/**
	 * Get the response to the location probe after the map is shown and before additional INT layers are purchased.
	 * 
	 * @return the location probe response
	 */
	@XmlElement(name="LocationResponse")
	public AttackLocationProbeResponse_MultiLocation getAttackLocationResponse_locations() {
		return attackLocationResponse_locations;
	}

	/**
	 * Set the response to the location probe after the map is shown and before additional INT layers are purchased.
	 * 
	 * @param attackLocationResponse_locations set the location probe response
	 */
	public void setAttackLocationResponse_locations(
			AttackLocationProbeResponse_MultiLocation attackLocationResponse_locations) {
		this.attackLocationResponse_locations = attackLocationResponse_locations;
	}

	/**
	 * Get the troop allocation response.
	 * 
	 * @return the troop allocation response
	 */
	@XmlElement(name="TroopAllocationResponse")
	public TroopAllocationResponse_MultiLocation getTroopAllocationResponse() {
		return troopAllocationResponse;
	}

	/**
	 * Set the troop allocation response.
	 * 
	 * @param troopAllocationResponse the troop allocation response
	 */
	public void setTroopAllocationResponse(TroopAllocationResponse_MultiLocation troopAllocationResponse) {
		this.troopAllocationResponse = troopAllocationResponse;
	}
	
	/**
	 * Get the total time spent purchasing INT layers (in milliseconds). FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @return the time in milliseconds.
	 */
	@XmlElement(name="LayerPurchaseTime_ms")
	public Long getIntLayerPurchaseTime_ms() {
		return intLayerPurchaseTime_ms;
	}

	/**
	 * Set the total time spent purchasing INT layers (in milliseconds). FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @param intLayerPurchaseTime_ms the time in milliseconds
	 */
	public void setIntLayerPurchaseTime_ms(Long intLayerPurchaseTime_ms) {
		this.intLayerPurchaseTime_ms = intLayerPurchaseTime_ms;
	}

	/**
	 * Get the INT layers that were purchased.
	 * 
	 * @return the INT layers
	 */
	@XmlElement(name="INTLayerPurchase")	
	public ArrayList<Task_7_INTLayerPurchase> getIntLayerPurchases() {
		return intLayerPurchases;
	}

	/**
	 * Set the INT layers that were purchased.
	 * 
	 * @param intLayerPurchases the INT layers
	 */
	public void setIntLayerPurchases(ArrayList<Task_7_INTLayerPurchase> intLayerPurchases) {
		this.intLayerPurchases = intLayerPurchases;
	}		
}