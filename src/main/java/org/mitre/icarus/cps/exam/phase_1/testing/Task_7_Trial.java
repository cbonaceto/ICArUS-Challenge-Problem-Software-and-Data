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
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.phase_1.response.Task_7_TrialResponse;
import org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.Task_7_INTLayerPresentationProbe;
import org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.AttackLocationProbe_MultiGroup;
import org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.AttackLocationProbe_MultiLocation;
import org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.TroopAllocationProbe_MultiLocation;
import org.mitre.icarus.cps.feature_vector.phase_1.FeatureVectorFileDescriptor;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupAttack;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupCenter;
import org.mitre.icarus.cps.feature_vector.phase_1.TaskData;

/**
 * A Task 7 trial.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="Task_7_Trial", namespace="IcarusCPD_1",
		propOrder={"featureVectorFile", "attackLocationProbe_groups", "attackLocationProbe_locations",
		"troopAllocationProbe", "responsibleGroupShown", "groundTruth", "intLayers", "trialResponse"})
public class Task_7_Trial extends IcarusTestTrial_Phase1 {	
	
	/** Feature vector file containing the attack locations and SIGINT hits at the group centers */	
	protected FeatureVectorFileDescriptor featureVectorFile;	
	
	/** The group centers */
	/** (The group center locations stay the same for all trials in a phase, 
	 * but the SIGINT information may change on each trial) */
	protected ArrayList<GroupCenter> groupCenters;
	
	/** The possible attack location(s) being probed */
	protected ArrayList<GroupAttack> possibleAttackLocations;
	
	/** The actual (computed) "cow-walk" distances from the group centers to each attack location */
	protected ArrayList<ArrayList<Double>> centerToAttackDistances;
	
	/** The initial attack location probe (before the map is shown) on the likelihood
	 * of attack by each group*/
	protected AttackLocationProbe_MultiGroup attackLocationProbe_groups;
	
	/** The attack location probe (after the map is shown) on the likelihood
	 * of attack at each location */
	protected AttackLocationProbe_MultiLocation attackLocationProbe_locations;
	
	/** The troop allocation probe after the attack location probe */
	protected TroopAllocationProbe_MultiLocation troopAllocationProbe;
	
	/** Whether or not the responsible group is revealed on this trial.
	 * The actual attack location is revealed on every trial. */
	protected Boolean responsibleGroupShown;
	
	/** Ground truth information contains the actual attack location and responsible group */
	protected GroundTruth groundTruth;
	
	/** The INT layers available for purchase */
	protected ArrayList<Task_7_INTLayerPresentationProbe> intLayers;	
	
	/** Subject/model response to the trial */
	protected Task_7_TrialResponse trialResponse;
	
	/**
	 * Get the feature vector file information.
	 * 
	 * @return the feature vector file information
	 */
	@XmlElement(name="FeatureVectorFile")
	public FeatureVectorFileDescriptor getFeatureVectorFile() {
		return featureVectorFile;
	}

	/**
	 * Set the feature vector file information.
	 * 
	 * @param featureVectorFile the feature vector file information
	 */
	public void setFeatureVectorFile(FeatureVectorFileDescriptor featureVectorFile) {
		this.featureVectorFile = featureVectorFile;
	}
	
	/**
	 * Populates possibleAttackLocations from given taskData.
	 * 
	 * @param taskData taskData instance with the attack locations
	 */
	public void setTaskData(TaskData taskData) {		
		if(taskData != null && taskData.getCenters() != null && !taskData.getCenters().isEmpty()) {
			groupCenters = taskData.getCenters();
		}
		if(taskData != null && taskData.getAttacks() != null && !taskData.getAttacks().isEmpty()) {
			possibleAttackLocations = taskData.getAttacks();
		}
	}	

	/**
	 * Get the group centers. Note that the group center locations stay the same for all trials in a phase, 
	 * but the SIGINT information may change on each trial.  The group centers are populated from
	 * the feature vector.
	 * 
	 * @return the group centers
	 */
	@XmlTransient
	public ArrayList<GroupCenter> getGroupCenters() {
		return groupCenters;
	}

	/**
	 * Set the group centers. Note that the group center locations stay the same for all trials in a phase, 
	 * but the SIGINT information may change on each trial.  The group centers are populated from
	 * the feature vector.
	 * 
	 * @param groupCenters the group centers
	 */
	public void setGroupCenters(ArrayList<GroupCenter> groupCenters) {
		this.groupCenters = groupCenters;
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
	 * Get the distances in grid units from the group centers to each attack location.
	 * 
	 * @return the distances in grid units
	 */
	@XmlTransient
	public ArrayList<ArrayList<Double>> getCenterToAttackDistances() {
		return centerToAttackDistances;
	}	
	
	/**
	 * Set the distances in grid units from the group centers to each attack location.
	 * 
	 * @param centerToAttackDistances the distances in grid units
	 */
	public void setCenterToAttackDistances(
			ArrayList<ArrayList<Double>> centerToAttackDistances) {
		this.centerToAttackDistances = centerToAttackDistances;
	}	

	/**
	 * Get the group probe before the map is shown.
	 * 
	 * @return the group probe
	 */
	@XmlElement(name="GroupProbe")
	public AttackLocationProbe_MultiGroup getAttackLocationProbe_groups() {
		return attackLocationProbe_groups;
	}

	/**
	 * Set the group probe before the map is shown.
	 * 
	 * @param attackLocationProbe_groups the group probe
	 */
	public void setAttackLocationProbe_groups(
			AttackLocationProbe_MultiGroup attackLocationProbe_groups) {
		this.attackLocationProbe_groups = attackLocationProbe_groups;
	}

	/**
	 * Get the location probe after the map is shown and before additional INT layers are purchased.
	 * 
	 * @return the location probe
	 */
	@XmlElement(name="LocationProbe")
	public AttackLocationProbe_MultiLocation getAttackLocationProbe_locations() {
		return attackLocationProbe_locations;
	}

	/**
	 * Set the location probe after the map is shown and before additional INT layers are purchased.
	 * 
	 * @param attackLocationProbe_locations the location probe
	 */
	public void setAttackLocationProbe_locations(
			AttackLocationProbe_MultiLocation attackLocationProbe_locations) {
		this.attackLocationProbe_locations = attackLocationProbe_locations;
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
	
	/**
	 * Get whether the responsible group is shown on this trial.
	 * 
	 * @return whether the responsible group is shown
	 */
	@XmlElement(name="ResponsibleGroupShown")
	public Boolean isResponsibleGroupShown() {
		return responsibleGroupShown;
	}

	/**
	 * Set whether the responsible group is shown on this trial.
	 * 
	 * @param responsibleGroupShown whether the responsible group is shown
	 */
	public void setResponsibleGroupShown(Boolean responsibleGroupShown) {
		this.responsibleGroupShown = responsibleGroupShown;
	}

	/**
	 * Get the ground truth information (the responsible group and actual attack location).
	 * 
	 * @return the ground truth information
	 */
	@XmlElement(name="GroundTruth")
	public GroundTruth getGroundTruth() {
		return groundTruth;
	}

	/**
	 * Set the ground truth information (the responsible group and actual attack location).
	 * 
	 * @param groundTruth the ground truth information
	 */
	public void setGroundTruth(GroundTruth groundTruth) {
		this.groundTruth = groundTruth;
	}

	/**
	 * Get the INTLayers available for purchase.
	 * 
	 * @return the INT layers
	 */
	@XmlElementWrapper(name="INTLayers")
	@XmlElement(name="INTLayer")	
	public ArrayList<Task_7_INTLayerPresentationProbe> getIntLayers() {
		return intLayers;
	}

	/**
	 * Set the INTLayers available for purchase.
	 * 
	 * @param intLayers the INT layers
	 */
	public void setIntLayers(ArrayList<Task_7_INTLayerPresentationProbe> intLayers) {
		this.intLayers = intLayers;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_1.testing.IcarusTestTrial_Phase1#getTrialResponse()
	 */
	@XmlElement(name="TrialResponse")
	@Override
	public Task_7_TrialResponse getTrialResponse() {
		return trialResponse;
	}

	/**
	 * Set the trial response.
	 * 
	 * @param trialResponse the trial response
	 */
	public void setTrialResponse(Task_7_TrialResponse trialResponse) {
		this.trialResponse = trialResponse;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.base.testing.IcarusTestTrial#getTestTrialType()
	 */
	@SuppressWarnings("deprecation")
	@Override
	public TestTrialType getTestTrialType() {
		return TestTrialType.Task_7_Trial;
	}
}