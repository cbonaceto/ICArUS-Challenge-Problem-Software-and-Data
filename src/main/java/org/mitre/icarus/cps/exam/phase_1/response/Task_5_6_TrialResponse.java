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
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.AttackLocationProbeResponse_MultiGroup;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.SurpriseReportProbeResponse;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.Task_5_6_AttackLocationProbeResponseAfterINT;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.TroopAllocationResponse_MultiGroup;

/**
 * Subject/model response to a Task 5 trial.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="Task_5_6_TrialResponse", namespace="IcarusCPD_1", 
		propOrder={"attackLocationResponse_initial", "initialProbabilities", "attackLocationResponses_afterINTs", 
		"troopAllocationResponse", "groundTruthSurpriseResponse"})
public class Task_5_6_TrialResponse extends IcarusTrialResponse_Phase1 {
	
	/** Initial probability response at beginning of trial 
	 * DEPRECATED - Maintained for backward compatibility with initial data collections. */
	@Deprecated
	protected AttackLocationProbeResponse_MultiGroup attackLocationResponse_initial;
	
	/** Initial probabilities at the beginning of the trial (from HUMINT) */
	protected ArrayList<Double> initialProbabilities;	
	
	/** Probability responses and surprise reports after each INT presentation */
	protected ArrayList<Task_5_6_AttackLocationProbeResponseAfterINT> attackLocationResponses_afterINTs;	
	
	/** Troop allocation response at the end of the trial */
	protected TroopAllocationResponse_MultiGroup troopAllocationResponse;
	
	/** Surprise report (after ground truth is shown) */
	protected SurpriseReportProbeResponse groundTruthSurpriseResponse;

	/**
	 * Get the response to the group probe before additional INT layers are presented.
	 * This method is deprecated and maintained for backward compatibility with
	 * initial data collections. Initial probabilities from HUMINT are now provided
	 * and not probed.
	 * 
	 * @return the group probe response
	 */
	@Deprecated
	@XmlElement(name="GroupResponse")
	public AttackLocationProbeResponse_MultiGroup getAttackLocationResponse_initial() {
		return attackLocationResponse_initial;
	}

	/**
	 * Set the response to the group probe before additional INT layers are presented.
	 * This method is deprecated and maintained for backward compatibility with
	 * initial data collections. Initial probabilities from HUMINT are now provided
	 * and not probed.
	 * 
	 * @param attackLocationResponse_initial the group probe response
	 */
	@Deprecated
	public void setAttackLocationResponse_initial(
			AttackLocationProbeResponse_MultiGroup attackLocationResponse_initial) {
		this.attackLocationResponse_initial = attackLocationResponse_initial;
	}
	
	/**
	 * Get the initial probabilities at the beginning of the trial (from HUMINT).
	 * 
	 * @return the initial probabilities
	 */
	@XmlElement(name="InitialProbabilities")
	@XmlList
	public ArrayList<Double> getInitialProbabilities() {
		return initialProbabilities;
	}

	/**
	 * Set the initial probabilities at the beginning of the trial (from HUMINT).
	 * 
	 * @param initialProbabilities the initial probabilities
	 */
	public void setInitialProbabilities(ArrayList<Double> initialProbabilities) {
		this.initialProbabilities = initialProbabilities;
	}

	/**
	 * Get the responses after each INT layer is presented.
	 * 
	 * @return the responses
	 */
	@XmlElement(name="GroupResponse_afterINT")
	public ArrayList<Task_5_6_AttackLocationProbeResponseAfterINT> getAttackLocationResponses_afterINTs() {
		return attackLocationResponses_afterINTs;
	}	

	/**
	 * Set the responses after each INT layer is presented.
	 * 
	 * @param attackLocationResponses_afterINTs the responses
	 */
	public void setAttackLocationResponses_afterINTs(
			ArrayList<Task_5_6_AttackLocationProbeResponseAfterINT> attackLocationResponses_afterINTs) {
		this.attackLocationResponses_afterINTs = attackLocationResponses_afterINTs;
	}

	/**
	 * Get the troop allocation response.
	 * 
	 * @return the troop allocation response
	 */
	@XmlElement(name="TroopAllocationResponse")
	public TroopAllocationResponse_MultiGroup getTroopAllocationResponse() {
		return troopAllocationResponse;
	}

	/**
	 * Set the troop allocation response.
	 * 
	 * @param troopAllocationResponse the troop allocation response
	 */
	public void setTroopAllocationResponse(TroopAllocationResponse_MultiGroup troopAllocationResponse) {
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