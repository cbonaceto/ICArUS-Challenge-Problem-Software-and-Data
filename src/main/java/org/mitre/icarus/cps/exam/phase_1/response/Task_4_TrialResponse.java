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

import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.AttackLocationProbeResponse_MultiLocation;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.SurpriseReportProbeResponse;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.Task_4_7_AttackLocationProbeResponseAfterINT;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.TroopAllocationResponse_MultiLocation;

/**
 * Subject/model response to a Task 4 trial.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="Task_4_TrialResponse", namespace="IcarusCPD_1", 
		propOrder={"attackLocationResponse_initial", "attackLocationResponses_afterINTs", 
		"troopAllocationResponse", "groundTruthSurpriseResponse"})
public class Task_4_TrialResponse extends IcarusTrialResponse_Phase1 {
	
	/** Probability response at beginning of trial */
	protected AttackLocationProbeResponse_MultiLocation attackLocationResponse_initial;
	
	/** Probability responses after each INT presentation */
	protected ArrayList<Task_4_7_AttackLocationProbeResponseAfterINT> attackLocationResponses_afterINTs;
	
	/** Troop allocation response */
	protected TroopAllocationResponse_MultiLocation troopAllocationResponse;
	
	/** Surprise report (after ground truth is shown) */
	protected SurpriseReportProbeResponse groundTruthSurpriseResponse;

	/**
	 * Get the response to the location probe before additional INT layers are presented.
	 * 
	 * @return the location probe response
	 */
	@XmlElement(name="LocationResponse")
	public AttackLocationProbeResponse_MultiLocation getAttackLocationResponse_initial() {
		return attackLocationResponse_initial;
	}

	/**
	 * Set the response to the location probe before additional INT layers are presented.
	 * 
	 * @param attackLocationResponse_initial the location probe response
	 */
	public void setAttackLocationResponse_initial(
			AttackLocationProbeResponse_MultiLocation attackLocationResponse_initial) {
		this.attackLocationResponse_initial = attackLocationResponse_initial;
	}	

	/**
	 * Get the responses after each INT layer is presented.
	 * 
	 * @return the responses
	 */
	@XmlElement(name="LocationResponse_afterINT")
	public ArrayList<Task_4_7_AttackLocationProbeResponseAfterINT> getAttackLocationResponses_afterINTs() {
		return attackLocationResponses_afterINTs;
	}
	
	/**
	 * Set the responses after each INT layer is presented.
	 * 
	 * @param attackLocationResponses_afterINTs the responses
	 */
	public void setAttackLocationResponses_afterINTs(
			ArrayList<Task_4_7_AttackLocationProbeResponseAfterINT> attackLocationResponses_afterINTs) {
		this.attackLocationResponses_afterINTs = attackLocationResponses_afterINTs;
	}	

	/**
	 * Get the troop allocation response.
	 * 
	 * @return the troop allocation response.
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
	public void setTroopAllocationResponse(
			TroopAllocationResponse_MultiLocation troopAllocationResponse) {
		this.troopAllocationResponse = troopAllocationResponse;
	}

	/**
	 * Get the response to the surprise probe after ground truth is revealed.
	 * 
	 * @return the surprise response
	 */
	@XmlElement(name="GroundTruthSurpriseResponse")	
	public SurpriseReportProbeResponse getGroundTruthSurpriseResponse() {
		return groundTruthSurpriseResponse;
	}

	/**
	 * Set the response to the surprise probe after ground truth is revealed.
	 * 
	 * @param groundTruthSurpriseResponse the surprise response
	 */
	public void setGroundTruthSurpriseResponse(
			SurpriseReportProbeResponse groundTruthSurpriseResponse) {
		this.groundTruthSurpriseResponse = groundTruthSurpriseResponse;
	}	
}