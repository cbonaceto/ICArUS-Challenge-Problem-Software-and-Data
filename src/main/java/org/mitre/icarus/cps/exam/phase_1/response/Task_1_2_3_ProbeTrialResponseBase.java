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

import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.AttackLocationProbeResponse_MultiGroup;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.SurpriseReportProbeResponse;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.TroopSelectionResponse_MultiGroup;

/**
 * Abstract base class for responses to a Task 1, 2, or 3 probe trial.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="Task_1_2_3_ProbeTrialResponseBase", namespace="IcarusCPD_1")
@XmlSeeAlso({Task_1_ProbeTrialResponse.class, Task_2_ProbeTrialResponse.class, Task_3_ProbeTrialResponse.class})
public abstract class Task_1_2_3_ProbeTrialResponseBase extends IcarusTrialResponse_Phase1 {
	
	/** Response to the attack location probe */
	protected AttackLocationProbeResponse_MultiGroup attackLocationResponse;
	
	/** Troop selection probe response */
	protected TroopSelectionResponse_MultiGroup troopSelectionResponse;
	
	/** Surprise report (after seeing ground truth) */
	protected SurpriseReportProbeResponse groundTruthSurpriseResponse;	

	/**
	 * Get the response to the group probe where the subject/model indicates the probability that
	 * each group is responsible for an attack at a location.
	 * 
	 * @return the group probe response
	 */
	@XmlTransient	
	public AttackLocationProbeResponse_MultiGroup getAttackLocationResponse() {
		return attackLocationResponse;
	}

	/**
	 * Set the response to the group probe where the subject/model indicates the probability that
	 * each group is responsible for an attack at a location.
	 * 
	 * @param attackLocationResponse the group probe response
	 */
	public void setAttackLocationResponse(AttackLocationProbeResponse_MultiGroup attackLocationResponse) {
		this.attackLocationResponse = attackLocationResponse;
	}
	
	/**
	 * Get the troop selection response.
	 * 
	 * @return the troop selection response
	 */
	@XmlTransient
	public TroopSelectionResponse_MultiGroup getTroopSelectionResponse() {
		return troopSelectionResponse;
	}

	/**
	 * Set the troop selection response.
	 * 
	 * @param troopSelectionResponse the troop selection response
	 */
	public void setTroopSelectionResponse(TroopSelectionResponse_MultiGroup troopSelectionResponse) {
		this.troopSelectionResponse = troopSelectionResponse;
	}

	/**
	 * Get the surprise report response after ground truth is revealed.
	 * 
	 * @return the surprise report response
	 */
	@XmlTransient
	public SurpriseReportProbeResponse getGroundTruthSurpriseResponse() {
		return groundTruthSurpriseResponse;
	}

	/**
	 * Set the surprise report response after ground truth is revealed.
	 * 
	 * @param groundTruthSurpriseResponse the surprise report response
	 */
	public void setGroundTruthSurpriseResponse(
			SurpriseReportProbeResponse groundTruthSurpriseResponse) {
		this.groundTruthSurpriseResponse = groundTruthSurpriseResponse;
	}
}