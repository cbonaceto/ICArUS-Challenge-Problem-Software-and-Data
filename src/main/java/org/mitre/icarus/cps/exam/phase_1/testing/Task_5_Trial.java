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
package org.mitre.icarus.cps.exam.phase_1.testing;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.phase_1.response.Task_5_6_TrialResponse;
import org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.Task_5_6_INTLayerPresentationProbe;
import org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.AttackLocationProbe_MultiGroup;
import org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.HumintReport;
import org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.SurpriseReportProbe;
import org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.TroopAllocationProbe_MultiGroup;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupAttack;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupCenter;
import org.mitre.icarus.cps.feature_vector.phase_1.TaskData;

/**
 * A Task 5 trial.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="Task_5_Trial", namespace="IcarusCPD_1",
		propOrder={"centersToAttackDistances", "attackLocationProbe_initial", "initialHumintReport", "intLayers", 
		"troopAllocationProbe", "groundTruth", "groundTruthSurpriseProbe", "trialResponse"})
public class Task_5_Trial extends Task_4_5_6_TrialBase {
	
	/** The group centers for each group being probed (from HUMINT data). 
	 * The group center locations also contain information about
	 * IMINT, MOVINT, and SIGINT at each location. */
	protected ArrayList<GroupCenter> groupCenters;
	
	/** The attack location (SIGACT) */
	protected GroupAttack attackLocation;
	
	/** The computed shortest path "cow-walk" distances from the attack location to each group center */
	protected ArrayList<Double> centersToAttackDistances;
	
	/** The initial attack location probe (before INT layers are presented).
	 * DEPRECATED - Maintained for backward compatibility with initial data collections. */
	@Deprecated 
	protected AttackLocationProbe_MultiGroup attackLocationProbe_initial;
	
	/** The initial group attack probabilities from HUMINT data
	 *  (before additional INT layers are presented) */
	protected HumintReport initialHumintReport;
	
	/** The ordered list of INT layer presentations 
	 * (In Task 6, they are selectable by the subject/model) */	 
	protected ArrayList<Task_5_6_INTLayerPresentationProbe> intLayers;
	
	/** The troop allocation probe */
	protected TroopAllocationProbe_MultiGroup troopAllocationProbe;	
	
	/** Subject/model response to the trial */
	protected Task_5_6_TrialResponse trialResponse;	
	
	/**
	 * Populates groupCenters and attackLocation from given taskData.
	 * 
	 * @param taskData taskData instance containing the group centers and attack location
	 */
	@Override
	public void setTaskData(TaskData taskData) {
		if(taskData != null) {
			if(taskData.getCenters() != null && !taskData.getCenters().isEmpty()) {
				groupCenters = taskData.getCenters();
			}
			if(taskData.getAttacks() != null && !taskData.getAttacks().isEmpty()) {
				attackLocation = taskData.getAttacks().get(0);
			}
			if(taskData.getDistances() != null && !taskData.getDistances().isEmpty()) {				
				centersToAttackDistances = taskData.getDistances();				
			}
		}
	}

	/**
	 * Get the group centers for each group being probed (from HUMINT). 
	 * The group center locations also contain information about
	 * IMINT, MOVINT, and SIGINT at each location. The group centers are populated
	 * from the feature vector.
	 * 
	 * @return the group centers
	 */
	@XmlTransient
	public ArrayList<GroupCenter> getGroupCenters() {
		return groupCenters;
	}

	/**
	 * Set the group centers for each group being probed (from HUMINT). 
	 * The group center locations also contain information about
	 * IMINT, MOVINT, and SIGINT at each location.
	 * 
	 * @param groupCenters the group centers
	 */
	public void setGroupCenters(ArrayList<GroupCenter> groupCenters) {
		this.groupCenters = groupCenters;
	}

	/**
	 * Get the attack location.
	 * 
	 * @return the attack location
	 */
	@XmlTransient
	public GroupAttack getAttackLocation() {
		return attackLocation;
	}

	/**
	 * Set the attack location.
	 * 
	 * @param attackLocation the attack location
	 */
	public void setAttackLocation(GroupAttack attackLocation) {
		this.attackLocation = attackLocation;
	}		
	
	/**
	 * Get the computed shortest path "cow-walk" distances from the attack location to each group center.
	 * 
	 * @return the distances in miles
	 */
	@XmlElement(name="CentersToAttackDistances")
	@XmlList
	public ArrayList<Double> getCentersToAttackDistances() {
		return centersToAttackDistances;
	}

	/**
	 * Set the computed shortest path "cow-walk" distances from the attack location to each group center.
	 * 
	 * @param centersToAttackDistances the distances in miles
	 */
	public void setCentersToAttackDistances(ArrayList<Double> centersToAttackDistances) {
		this.centersToAttackDistances = centersToAttackDistances;
	}

	/**
	 * Get the group probe before additional INT layers are presented.
	 * This method is deprecated and maintained for backward compatibility with
	 * initial data collections. Initial probabilities from HUMINT are now provided
	 * and not probed.
	 * 
	 * @return the group probe
	 */	
	@Deprecated
	@XmlElement(name="GroupProbe")
	public AttackLocationProbe_MultiGroup getAttackLocationProbe_initial() {
		return attackLocationProbe_initial;
	}

	/**
	 * Set the group probe before additional INT layers are presented.
	 * This method is deprecated and maintained for backward compatibility with
	 * initial data collections. Initial probabilities from HUMINT are now provided
	 * and not probed.
	 * 
	 * @param attackLocationProbe_initial the group probe
	 */
	public void setAttackLocationProbe_initial(AttackLocationProbe_MultiGroup attackLocationProbe_initial) {
		this.attackLocationProbe_initial = attackLocationProbe_initial;
	}	

	/**
	 * Get the initial group attack probabilities from HUMINT data 
	 * (before additional INT layers are presented).
	 * 
	 * @return the initial group probabilities from HUMINT
	 */	
	@XmlElement(name="InitialHumintReport")
	public HumintReport getInitialHumintReport() {
		return initialHumintReport;
	}

	/**
	 * Set the initial group attack probabilities from HUMINT data 
	 * (before additional INT layers are presented).
	 * 
	 * @param initialHumintReport the initial group probabilities from HUMINT
	 */
	public void setInitialHumintReport(HumintReport initialHumintReport) {
		this.initialHumintReport = initialHumintReport;
	}

	/**
	 * Get the INT layers to present or select.
	 * 
	 * @return the INT layers
	 */
	@XmlElementWrapper(name="INTLayers")
	@XmlElement(name="INTLayer")	
	public ArrayList<Task_5_6_INTLayerPresentationProbe> getIntLayers() {
		return intLayers;
	}	

	/**
	 * Set the INT layers to present or select.
	 * 
	 * @param intLayers the INT layers
	 */
	public void setIntLayers(ArrayList<Task_5_6_INTLayerPresentationProbe> intLayers) {
		this.intLayers = intLayers;
	}		
	
	/**
	 * Get the troop allocation probe.
	 * 
	 * @return the troop allocation probe
	 */
	@XmlElement(name="TroopAllocationProbe")
	public TroopAllocationProbe_MultiGroup getTroopAllocationProbe() {
		return troopAllocationProbe;
	}

	/**
	 * Set the troop allocation probe.
	 * 
	 * @param troopAllocationProbe the troop allocation probe
	 */
	public void setTroopAllocationProbe(TroopAllocationProbe_MultiGroup troopAllocationProbe) {
		this.troopAllocationProbe = troopAllocationProbe;
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_1.testing.Task_4_5_6_TrialBase#getGroundTruth()
	 */
	@XmlElement(name="GroundTruth")	
	@Override
	public GroundTruth getGroundTruth() {
		return super.getGroundTruth();
	}		

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_1.testing.Task_4_5_6_TrialBase#setGroundTruth(org.mitre.icarus.cps.exam.phase_1.testing.GroundTruth)
	 */
	@Override
	public void setGroundTruth(GroundTruth groundTruth) {		
		super.setGroundTruth(groundTruth);
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_1.testing.Task_4_5_6_TrialBase#getGroundTruthSurpriseProbe()
	 */
	@XmlElement(name="GroundTruthSurpriseProbe")
	@Override
	public SurpriseReportProbe getGroundTruthSurpriseProbe() {
		return super.getGroundTruthSurpriseProbe();
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_1.testing.Task_4_5_6_TrialBase#setGroundTruthSurpriseProbe(org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.SurpriseReportProbe)
	 */
	@Override
	public void setGroundTruthSurpriseProbe(SurpriseReportProbe groundTruthSurpriseProbe) {
		super.setGroundTruthSurpriseProbe(groundTruthSurpriseProbe);
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_1.testing.IcarusTestTrial_Phase1#getTrialResponse()
	 */
	@XmlElement(name="TrialResponse")
	@Override
	public Task_5_6_TrialResponse getTrialResponse() {
		return trialResponse;
	}

	/**
	 * Set the trial response.
	 * 
	 * @param trialResponse the trial response
	 */
	public void setTrialResponse(Task_5_6_TrialResponse trialResponse) {
		this.trialResponse = trialResponse;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.base.testing.IcarusTestTrial#getTestTrialType()
	 */
	@SuppressWarnings("deprecation")
	@Override
	public TestTrialType getTestTrialType() {
		return TestTrialType.Task_5_Trial;
	}
}