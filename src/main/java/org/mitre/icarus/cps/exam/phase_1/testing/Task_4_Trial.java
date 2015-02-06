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

import org.mitre.icarus.cps.exam.phase_1.response.Task_4_TrialResponse;
import org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.Task_4_INTLayerPresentationProbe;
import org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.AttackLocationProbe_MultiLocation;
import org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.SurpriseReportProbe;
import org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.TroopAllocationProbe_MultiLocation;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupAttack;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupCenter;
import org.mitre.icarus.cps.feature_vector.phase_1.TaskData;

/**
 * A Task 4 trial.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="Task_4_Trial", namespace="IcarusCPD_1", 
		propOrder={"centerToAttackDistances", "attackLocationProbe_initial", "intLayers", 
		"troopAllocationProbe", "groundTruth", "groundTruthSurpriseProbe", "trialResponse"})
public class Task_4_Trial extends Task_4_5_6_TrialBase {
	
	/** The group center for the group being probed (from HUMINT) */
	protected GroupCenter groupCenter;
	
	/** The possible attack location(s) being probed */
	protected ArrayList<GroupAttack> possibleAttackLocations;
	
	/** The computed shortest path "cow-walk" distances from the group center to each attack location */
	protected ArrayList<Double> centerToAttackDistances;
	
	/** The initial attack location probe (before INT layers are presented) */
	protected AttackLocationProbe_MultiLocation attackLocationProbe_initial;
	
	/** The ordered list of INT layer presentations (by default, they include HUMINT and SOCINT) */
	protected ArrayList<Task_4_INTLayerPresentationProbe> intLayers;
	
	/** The troop allocation probe */
	protected TroopAllocationProbe_MultiLocation troopAllocationProbe;
	
	/** Subject/model response to the trial */
	protected Task_4_TrialResponse trialResponse;
	
	/**
	 * Populates groupCenter and possibleAttackLocations from given taskData.
	 * 
	 * @param taskData the taskDataObject containing the group center and attack locations
	 */
	@Override
	public void setTaskData(TaskData taskData) {
		if(taskData != null) {
			if(taskData.getCenters() != null && !taskData.getCenters().isEmpty()) {
				groupCenter = taskData.getCenters().get(0);
			}

			if(taskData.getAttacks() != null && !taskData.getAttacks().isEmpty()) {
				possibleAttackLocations = taskData.getAttacks();
			}
			if(taskData.getDistances() != null && !taskData.getDistances().isEmpty()) {				
				centerToAttackDistances = taskData.getDistances();
			}
		}
	}

	/**
	 * Get the group center for the group being probed. The group center is populated from the feature vector. 
	 * 
	 * @return the group center
	 */
	@XmlTransient
	public GroupCenter getGroupCenter() {
		return groupCenter;
	}

	/**
	 * Set the group center for the group being probed.  The group center is populated from the feature vector.
	 * 
	 * @param groupCenter the group center
	 */
	public void setGroupCenter(GroupCenter groupCenter) {
		this.groupCenter = groupCenter;
	}

	/**
	 * Get the attack locations being probed.
	 * 
	 * @return the attack locations
	 */
	@XmlTransient
	public ArrayList<GroupAttack> getPossibleAttackLocations() {
		return possibleAttackLocations;
	}

	/**
	 * Set the attack locations being probed.
	 * 
	 * @param possibleAttackLocations the attack locations
	 */
	public void setPossibleAttackLocations(ArrayList<GroupAttack> possibleAttackLocations) {
		this.possibleAttackLocations = possibleAttackLocations;
	}

	/**
	 * Get the computed shortest path "cow-walk" distances from the group center to each attack location.
	 * 
	 * @return the distances in miles
	 */
	@XmlElement(name="CenterToAttackDistances")
	@XmlList
	public ArrayList<Double> getCenterToAttackDistances() {
		return centerToAttackDistances;
	}
	
	/**
	 * Set the computed shortest path "cow-walk" distances from the group center to each attack location.
	 * 
	 * @param centerToAttackDistances the distances in miles
	 */
	public void setCenterToAttackDistances(ArrayList<Double> centerToAttackDistances) {
		this.centerToAttackDistances = centerToAttackDistances;
	}	

	/**
	 * Get the location probe before additional INT layers are presented.
	 * 
	 * @return the location probe
	 */
	@XmlElement(name="LocationProbe")
	public AttackLocationProbe_MultiLocation getAttackLocationProbe_initial() {
		return attackLocationProbe_initial;
	}

	/**
	 * Set the location probe before additional INT layers are presented.
	 * 
	 * @param attackLocationProbe_initial the location probe
	 */
	public void setAttackLocationProbe_initial(
			AttackLocationProbe_MultiLocation attackLocationProbe_initial) {
		this.attackLocationProbe_initial = attackLocationProbe_initial;
	}

	/**
	 * Get the INT layers to present.
	 * 
	 * @return the INT layers
	 */
	@XmlElementWrapper(name="INTLayers")
	@XmlElement(name="INTLayer")	
	public ArrayList<Task_4_INTLayerPresentationProbe> getIntLayers() {
		return intLayers;
	}

	/**
	 * Set the INT layers to present.
	 * 
	 * @param intLayers the INT layers
	 */
	public void setIntLayers(ArrayList<Task_4_INTLayerPresentationProbe> intLayers) {
		this.intLayers = intLayers;
	}
	
	/**
	 * Get the troop allocation probe.
	 * 
	 * @return the troop allocation probe
	 */
	@XmlElement(name="TroopAllocationProbe")
	public TroopAllocationProbe_MultiLocation getTroopAllocationProbe() {
		return troopAllocationProbe;
	}

	/**
	 * Set the troop allocation probe.
	 * 
	 * @param troopAllocationProbe the troop allocation probe
	 */
	public void setTroopAllocationProbe(TroopAllocationProbe_MultiLocation troopAllocationProbe) {
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
	public Task_4_TrialResponse getTrialResponse() {
		return trialResponse;
	}

	/**
	 * Set the trial response.
	 * 
	 * @param trialResponse the trial response
	 */
	public void setTrialResponse(Task_4_TrialResponse trialResponse) {
		this.trialResponse = trialResponse;
	}

	@SuppressWarnings("deprecation")
	@Override
	public TestTrialType getTestTrialType() {
		return TestTrialType.Task_4_Trial;
	}
}