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
import java.util.Arrays;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.phase_1.response.Task_7_TrialResponse;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.AttackLocationProbeResponse_MultiGroup;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.AttackLocationProbeResponse_MultiLocation;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.GroupAttackProbabilityResponse;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.Task_7_INTLayerPurchase;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.TroopAllocation;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.TroopAllocationResponse_MultiLocation;
import org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.ImintLayer;
import org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.MovintLayer;
import org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.SigintLayer;
import org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.SocintLayer;
import org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.Task_7_INTLayerPresentationProbe;
import org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.AttackLocationProbe_MultiGroup;
import org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.AttackLocationProbe_MultiLocation;
import org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.TroopAllocationProbe_MultiLocation;
import org.mitre.icarus.cps.feature_vector.phase_1.FeatureVectorFileDescriptor;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupCenter;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupType;
import org.mitre.icarus.cps.feature_vector.phase_1.MovintType;
import org.mitre.icarus.cps.feature_vector.phase_1.Road;
import org.mitre.icarus.cps.feature_vector.phase_1.SocintOverlay;
import org.mitre.icarus.cps.feature_vector.phase_1.SocintPolygon;
import org.mitre.icarus.cps.feature_vector.phase_1.TaskData;

/*
 * 
Inputs Fixed for entire phase:
	Roads
	Group centers
	Distance-decay function from each group center to each attack location
	Initial number of credits
	Cost (in credits) for each INT layer
	Payoff (in credits) for correctly predicting the attack location	

Input for trial 1:
	The attacking group

Inputs for each trial:	
	SIGACT (actual attack location)
	Possible attack locations
	Attacking group and whether or not it is revealed on this trial
	INT layers available for purchase and cost of each layer:
		IMINT 
		MOVINT
		SIGINT

Outputs for each trial:
	P(attack) by each group (initial)
	P(attack) at each location (after fixed INTS presented)
	INT layers purchased
	P(attack) at each location (after each INT purchase)
	Troop allocation at each location

Feedback at end of trial:
	Shown actual attack location

Feedback at end of "wave"
	Attacking group revealed "pseduo-randomly" 1-4 trials before the end of the wave
	
Questions:

	Should base probabilities (P attack for each group) be specified, or are these built up 
	from the attack history? 
	
	When "purchasing" IMINT or MOVINT, will the subject/model receive data at all attack locations?
	
	When "purchasing" SIGINT, will the subject/model receive data only for a single group or for all groups?
 */

/** 
 * @author CBONACETO
 *
 */
@XmlRootElement(name="Task_7_Phase", namespace="IcarusCPD_1")
@XmlType(name="Task_7_Phase", namespace="IcarusCPD_1",
		propOrder={"featureVectorFile", "roadsFile", "regionsFile", "initialResponsibleGroup",
		"initialCredits", "correctPredictionCredits", "testTrials"})
public class Task_7_Phase extends TaskTestPhase<Task_7_Trial> {
	
	/** Feature vector file containing the group centers (group centers are fixed for the entire phase) */	
	protected FeatureVectorFileDescriptor featureVectorFile;
	
	/** Roads file (roads are fixed for the entire phase) */
	protected FeatureVectorFileDescriptor roadsFile;
	
	/** Regions file (regions are fixed for the entire phase) */
	protected FeatureVectorFileDescriptor regionsFile;	
	
	/** The responsible group shown at the beginning of the first trial */
	protected GroupType initialResponsibleGroup;
	
	/** The group centers for each group being probed (from HUMINT). 
	 * The group center locations also contain information about
	 * IMINT, MOVINT, SIGINT and SOCINT at each location. */
	protected ArrayList<GroupCenter> groupCenters;
	
	/** The roads */
	protected ArrayList<Road> roads;
	
	/** The SOCINT region boundaries for each group */
	protected ArrayList<SocintPolygon> regionBounds;
	
	/** The SOCINT regions overlay for all groups */
	protected SocintOverlay regionsOverlay;
	
	/** The starting number of credits */
	protected Integer initialCredits;
	
	/** The payoff for correctly predicting the attack location (the default is 1) */
	protected Integer correctPredictionCredits = 1;
	
	/** The trials */
	protected ArrayList<Task_7_Trial> testTrials;
	
	/** Whether to reverse the orders the layers are presented in */
	protected Boolean reverseLayerOrder;
	
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
	 * Populates groupCenters from given taskData.
	 * 
	 * @param taskData taskData object containing the group centers
	 */
	public void setTaskData(TaskData taskData) {
		if(taskData != null && taskData.getCenters() != null && !taskData.getCenters().isEmpty()) {
			groupCenters = taskData.getCenters();
		}
	}

	/**
	 * Get the roads feature vector file information. The roads feature vector contains the roads.
	 * 
	 * @return the roads feature vector file information
	 */
	@XmlElement(name="RoadsFile")
	public FeatureVectorFileDescriptor getRoadsFile() {
		return roadsFile;
	}

	/**
	 * Get the roads feature vector file information. The roads feature vector contains the roads.
	 * 
	 * @param roadsFile the roads feature vector file information
	 */
	public void setRoadsFile(FeatureVectorFileDescriptor roadsFile) {
		this.roadsFile = roadsFile;
	}
	
	/**
	 * Get the regions feature vector file information. The regions feature vector contains the regions.
	 *  
	 * @return the regions feature vector file information
	 */
	@XmlElement(name="RegionsFile")
	public FeatureVectorFileDescriptor getRegionsFile() {
		return regionsFile;
	}

	/**
	 * Set the regions feature vector file information. The regions feature vector contains the regions.
	 * 
	 * @param regionsFile the regions feature vector file information
	 */
	public void setRegionsFile(FeatureVectorFileDescriptor regionsFile) {
		this.regionsFile = regionsFile;
	}	
	
	/**
	 * Get the responsible group shown at the beginning of the first trial.
	 * 
	 * @return the group
	 */
	@XmlElement(name="InitialResponsibleGroup")
	public GroupType getInitialResponsibleGroup() {
		return initialResponsibleGroup;
	}

	/**
	 * Set the responsible group shown at the beginning of the first trial.
	 * 
	 * @param initialResponsibleGroup
	 */
	public void setInitialResponsibleGroup(GroupType initialResponsibleGroup) {
		this.initialResponsibleGroup = initialResponsibleGroup;
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
	 * Get the roads.  The roads are populated from the roads feature vector file.
	 * 
	 * @return the roads
	 */
	@XmlTransient
	public ArrayList<Road> getRoads() {
		return roads;
	}

	/**
	 * Set the roads.  The roads are populated from the roads feature vector file.
	 * 
	 * @param roads the roads
	 */
	public void setRoads(ArrayList<Road> roads) {
		this.roads = roads;
	}	

	/**
	 * @return
	 */
	@XmlTransient
	public ArrayList<SocintPolygon> getRegionBounds() {
		return regionBounds;
	}

	/**
	 * @param regionBounds
	 */
	public void setRegionBounds(ArrayList<SocintPolygon> regionBounds) {
		this.regionBounds = regionBounds;
	}

	/**
	 * Set the SOCINT regions overlay for all groups. The SOCINT regions overlay is populated from the regions feature vector file.
	 * 
	 * @return - the SOCINT regions overlay 
	 */
	@XmlTransient
	public SocintOverlay getRegionsOverlay() {
		return regionsOverlay;
	}

	/**
	 * Set the SOCINT regions overlay for all groups. The SOCINT regions overlay is populated from the regions feature vector file.
	 * 
	 * @param regionsOverlay - the SOCINT regions overlay
	 */
	public void setRegionsOverlay(SocintOverlay regionsOverlay) {
		this.regionsOverlay = regionsOverlay;
	}

	/**
	 * Get the starting number of credits.
	 * 
	 * @return the starting credits
	 */
	@XmlElement(name="InitialCredits")
	public Integer getInitialCredits() {
		return initialCredits;
	}

	/**
	 * Set the starting number of credits.
	 * 
	 * @param initialCredits the starting credits
	 */
	public void setInitialCredits(Integer initialCredits) {
		this.initialCredits = initialCredits;
	}

	/**
	 * Get the maximum number of credits awarded for a correct prediction.
	 * 
	 * @return the number of credits
	 */
	@XmlElement(name="CorrectPredictionCredits")
	public Integer getCorrectPredictionCredits() {
		return correctPredictionCredits;
	}

	/**
	 * Set the maximum number of credits awarded for a correct prediction.
	 * 
	 * @param correctPredictionCredits the number of credits
	 */
	public void setCorrectPredictionCredits(Integer correctPredictionCredits) {
		this.correctPredictionCredits = correctPredictionCredits;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_1.testing.TaskTestPhase#getTestTrials()
	 */
	@XmlElement(name="Trial")
	@Override	
	public ArrayList<Task_7_Trial> getTestTrials() {
		return testTrials;
	}

	/**
	 * Set the trials.
	 * 
	 * @param testTrials the trials
	 */
	public void setTestTrials(ArrayList<Task_7_Trial> testTrials) {
		this.testTrials = testTrials;
	}	
	
	/**
	 * Get whether to reverse the order the INT layers are displayed in in the GUI.
	 * 
	 * @return whether to reverse the order of the layers in the GUI
	 */
	@XmlAttribute(name="reverseLayerOrder")
	public Boolean isReverseLayerOrder() {
		return reverseLayerOrder;
	}

	/**
	 * Set whether to reverse the order the INT layers are displayed in in the GUI.
	 * 
	 * @param reverseLayerOrder whether to reverse the order of the layers in the GUI
	 */
	public void setReverseLayerOrder(Boolean reverseLayerOrder) {
		this.reverseLayerOrder = reverseLayerOrder;
	}
	
	/**
	 * Create a sample Task_7_Phase.
	 * 
	 * @param numTrials
	 * @param createSampleResponses
	 * @return
	 */
	public static Task_7_Phase createSampleTask_7_Phase(int numTrials, boolean createSampleResponses) {
		
		Task_7_Phase phase = new Task_7_Phase();		
		phase.setName("Task_7_Phase");		
		phase.testTrials = new ArrayList<Task_7_Trial>(numTrials);
		phase.setFeatureVectorFile(new FeatureVectorFileDescriptor("phase7.kml", "phase7.csv"));	
		phase.setRoadsFile(new FeatureVectorFileDescriptor("roads" + "_7" + ".kml",
				"roads" + "_7" + ".csv"));
		ArrayList<Road> roads = new ArrayList<Road>(Arrays.asList(
				new Road("1", MovintType.DenseTraffic),
				new Road("2", MovintType.SparseTraffic),
				new Road("3", MovintType.SparseTraffic),
				new Road("4", MovintType.DenseTraffic)));
		phase.setRoads(roads);
		phase.setInitialCredits(10);
		phase.setCorrectPredictionCredits(1);
		
		for(int trialNum=1; trialNum<=numTrials; trialNum++) {
			Task_7_Trial trial = new Task_7_Trial();			
			phase.testTrials.add(trial);
			trial.setTrialNum(trialNum);
			
			trial.setFeatureVectorFile(new FeatureVectorFileDescriptor("task7" + "_" + trialNum + ".kml",
					"task7" + "_" + trialNum + ".csv"));			
			
			ArrayList<GroupType> groups = new ArrayList<GroupType>(Arrays.asList(GroupType.A, GroupType.B, GroupType.C, GroupType.D));				
			ArrayList<String> locations = new ArrayList<String>(Arrays.asList("1", "2", "3", "4"));
			
			AttackLocationProbe_MultiGroup attackProbeGroups = new AttackLocationProbe_MultiGroup(null, groups);
			trial.setAttackLocationProbe_groups(attackProbeGroups);
			
			AttackLocationProbe_MultiLocation attackProbeLocations = new AttackLocationProbe_MultiLocation(null, locations);
			trial.setAttackLocationProbe_locations(attackProbeLocations);
			
			trial.setTroopAllocationProbe(new TroopAllocationProbe_MultiLocation(locations));
		
			trial.setIntLayers(new ArrayList<Task_7_INTLayerPresentationProbe>(Arrays.asList(			
					new Task_7_INTLayerPresentationProbe(new ImintLayer(), 1),
					new Task_7_INTLayerPresentationProbe(new MovintLayer(), 1),
					new Task_7_INTLayerPresentationProbe(
							new SigintLayer(GroupType.A), 2),
					new Task_7_INTLayerPresentationProbe(
							new SigintLayer(GroupType.B), 2),
					new Task_7_INTLayerPresentationProbe(
							new SigintLayer(GroupType.C), 2),
					new Task_7_INTLayerPresentationProbe(
							new SigintLayer(GroupType.D), 2),
					new Task_7_INTLayerPresentationProbe(new SocintLayer(), 2))));
			
			trial.setResponsibleGroupShown(false);
			trial.setGroundTruth(new GroundTruth(GroupType.B, "4"));						
			
			if(createSampleResponses) {
				//phase.setResponseGenerator(new ResponseGeneratorData("team1", false));
				Task_7_TrialResponse response = new Task_7_TrialResponse();
				trial.setTrialResponse(response);
				response.setTrialTime_ms(7850L);
				response.setAttackLocationResponse_groups(new AttackLocationProbeResponse_MultiGroup(
						new ArrayList<GroupAttackProbabilityResponse>(Arrays.asList(
								new GroupAttackProbabilityResponse(GroupType.A, 0.25D),
								new GroupAttackProbabilityResponse(GroupType.B, 0.25D),
								new GroupAttackProbabilityResponse(GroupType.C, 0.25D),
								new GroupAttackProbabilityResponse(GroupType.D, 0.25D))), 
								null));
				response.getAttackLocationResponse_groups().setTrialPartTime_ms(1056L);
				
				response.setAttackLocationResponse_locations(new AttackLocationProbeResponse_MultiLocation(
						new ArrayList<GroupAttackProbabilityResponse>(Arrays.asList(
								new GroupAttackProbabilityResponse("1", 0.25D),
								new GroupAttackProbabilityResponse("2", 0.25D),
								new GroupAttackProbabilityResponse("3", 0.25D),
								new GroupAttackProbabilityResponse("4", 0.25D))), 
								null));
				response.getAttackLocationResponse_locations().setTrialPartTime_ms(1705L);
				
				response.setTroopAllocationResponse(new TroopAllocationResponse_MultiLocation(
						new ArrayList<TroopAllocation>(Arrays.asList(
								new TroopAllocation("1", 0.25D),
								new TroopAllocation("2", 0.25D),
								new TroopAllocation("3", 0.25D),
								new TroopAllocation("4", 0.25D)))));
				response.getTroopAllocationResponse().setTrialPartTime_ms(2300L);
				
				response.setIntLayerPurchases(new ArrayList<Task_7_INTLayerPurchase>(Arrays.asList(
						new Task_7_INTLayerPurchase(new ImintLayer(), 1),
						new Task_7_INTLayerPurchase(new SigintLayer(GroupType.C), 2))));
			}
		}
		
		return phase;
	}
}