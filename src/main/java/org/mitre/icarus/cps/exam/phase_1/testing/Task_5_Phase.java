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
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.phase_1.response.Task_5_6_TrialResponse;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.AttackLocationProbeResponse_MultiGroup;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.GroupAttackProbabilityResponse;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.INTLayerData;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.SurpriseReportProbeResponse;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.Task_5_6_AttackLocationProbeResponseAfterINT;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.TroopAllocation;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.TroopAllocationResponse_MultiGroup;
import org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.ImintLayer;
import org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.IntLayer;
import org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.MovintLayer;
import org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.SigintLayer;
import org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.SocintLayer;
import org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.Task_5_6_INTLayerPresentationProbe;
import org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.AttackLocationProbe_MultiGroup;
import org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.SurpriseReportProbe;
import org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.TroopAllocationProbe_MultiGroup;
import org.mitre.icarus.cps.feature_vector.phase_1.FeatureVectorFileDescriptor;
import org.mitre.icarus.cps.feature_vector.phase_1.GridLocation2D;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupType;
import org.mitre.icarus.cps.feature_vector.phase_1.MovintType;
import org.mitre.icarus.cps.feature_vector.phase_1.Road;


/**
 * Contains an ordered list of Task 5 trials defining a Task 5 phase in an exam.
 * 
 * @author CBONACETO
 *
 */
@XmlRootElement(name="Task_5_Phase", namespace="IcarusCPD_1")
@XmlType(name="Task_5_Phase", namespace="IcarusCPD_1")
public class Task_5_Phase extends Task_4_5_6_PhaseBase<Task_5_Trial> {
	
	/** The trials */
	protected ArrayList<Task_5_Trial> testTrials;	

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_1.testing.Task_4_5_6_PhaseBase#getTestTrials()
	 */
	@Override
	@XmlElement(name="Trial")
	public ArrayList<Task_5_Trial> getTestTrials() {
		return testTrials;
	}
	
	/**
	 * Set the trials.
	 * 
	 * @param testTrials the trials
	 */
	public void setTestTrials(ArrayList<Task_5_Trial> testTrials) {
		this.testTrials = testTrials;
	}	
	
	/**
	 * Get whether to reverse the order the INT layers are displayed in in the GUI.
	 * 
	 * @return whether to reverse the order of the layers in the GUI
	 */
	@XmlAttribute(name="reverseLayerOrder")
	@Override
	public Boolean isReverseLayerOrder() {
		return reverseLayerOrder;
	}

	/**
	 * Set whether to reverse the order the INT layers are displayed in in the GUI.
	 * 
	 * @param reverseLayerOrder whether to reverse the order of the layers in the GUI
	 */
	@Override
	public void setReverseLayerOrder(Boolean reverseLayerOrder) {
		this.reverseLayerOrder = reverseLayerOrder;
	}
	
	/**
	 * Create a sample Task_5_Phase.
	 * 
	 * @param numTrials
	 * @param createSampleResponses
	 * @return
	 */
	public static Task_5_Phase createSampleTask_5_Phase(int numTrials, boolean createSampleResponses) {
		Task_5_Phase phase = new Task_5_Phase();		
		phase.setName("Task_5_Phase");		
		phase.testTrials = new ArrayList<Task_5_Trial>(numTrials);
		
		for(int trialNum=1; trialNum<=numTrials; trialNum++) {
			Task_5_Trial trial = new Task_5_Trial();
			phase.testTrials.add(trial);
			trial.setTrialNum(trialNum);
			trial.setFeatureVectorFile(new FeatureVectorFileDescriptor("task5" + "_" + trialNum + ".kml",
					"task5" + "_" + trialNum + ".csv"));
			trial.setRoadsFile(new FeatureVectorFileDescriptor("roads" + "_" + trialNum + ".kml",
					"roads" + "_" + trialNum + ".csv"));
			trial.setRegionsFile(new FeatureVectorFileDescriptor("regions" + "_" + trialNum + ".kml",
					"regions" + "_" + trialNum + ".csv"));
			ArrayList<Road> roads = new ArrayList<Road>(Arrays.asList(
					new Road("1", MovintType.DenseTraffic),
					new Road("2", MovintType.SparseTraffic),
					new Road("3", MovintType.SparseTraffic),
					new Road("4", MovintType.DenseTraffic)));
			trial.setRoads(roads);
			ArrayList<GroupType> groups = new ArrayList<GroupType>(Arrays.asList(GroupType.A, GroupType.B, GroupType.C, GroupType.D));
			AttackLocationProbe_MultiGroup attackProbe = new AttackLocationProbe_MultiGroup(new GridLocation2D("1"), groups);
			SurpriseReportProbe surpriseProbe = new SurpriseReportProbe(0, 6, 1);
			trial.setIntLayers(new ArrayList<Task_5_6_INTLayerPresentationProbe>(Arrays.asList(
					new Task_5_6_INTLayerPresentationProbe(new ImintLayer(), attackProbe),
					new Task_5_6_INTLayerPresentationProbe(new MovintLayer(), attackProbe),
					new Task_5_6_INTLayerPresentationProbe(new SigintLayer(GroupType.A), attackProbe),
					new Task_5_6_INTLayerPresentationProbe(new SocintLayer(), attackProbe))));			
			trial.setTroopAllocationProbe(new TroopAllocationProbe_MultiGroup(groups));
			trial.setGroundTruth(new GroundTruth(GroupType.C));
			trial.setGroundTruthSurpriseProbe(surpriseProbe);			
			if(createSampleResponses) {
				trial.setTrialResponse(createSampleTask_5_6_TrialResponse(false, 4, false));
			}
		}
		
		return phase;
	}
	
	/**
	 * Create a sample response to a Task 5 or 6 trial.
	 * 
	 * @param userSelectLayers
	 * @param numLayers
	 * @param task6Response
	 * @return
	 */
	protected static Task_5_6_TrialResponse createSampleTask_5_6_TrialResponse(boolean userSelectLayers, int numLayers,
			boolean task6Response) {
		
		Task_5_6_TrialResponse response = new Task_5_6_TrialResponse();
		response.setTrialTime_ms(2863L);
		/*response.setAttackLocationResponse_initial(new AttackLocationProbeResponse_MultiGroup(
				new ArrayList<GroupAttackProbabilityResponse>(Arrays.asList(
						new GroupAttackProbabilityResponse(GroupType.A, 0.25D),
						new GroupAttackProbabilityResponse(GroupType.B, 0.25D),
						new GroupAttackProbabilityResponse(GroupType.C, 0.25D),
						new GroupAttackProbabilityResponse(GroupType.D, 0.25D))), 
						"1"));
		response.getAttackLocationResponse_initial().setTrialPartTime_ms(1000L);*/
		response.setAttackLocationResponses_afterINTs(
				new ArrayList<Task_5_6_AttackLocationProbeResponseAfterINT>(4));		
		SurpriseReportProbeResponse surpriseResponse = new SurpriseReportProbeResponse(3);
		surpriseResponse.setTrialPartTime_ms(1500L);
		for(int i=0; i<numLayers; i++) {
			IntLayer layerType = null;
			switch(i) {
			case 0: layerType = new ImintLayer(); break;
			case 1: layerType = new MovintLayer(); break;
			case 2: 
				if(task6Response) {
					layerType = new SigintLayer(GroupType.A);
				}
				else {
					layerType = new SigintLayer(GroupType.B);
				}
			break;
			case 3: layerType = new SocintLayer(); break;
			}		
			Task_5_6_AttackLocationProbeResponseAfterINT afterINTResponse = new Task_5_6_AttackLocationProbeResponseAfterINT(
					new INTLayerData(layerType, userSelectLayers, userSelectLayers ? 789L : null),
					new AttackLocationProbeResponse_MultiGroup(
							new ArrayList<GroupAttackProbabilityResponse>(Arrays.asList(
									new GroupAttackProbabilityResponse(GroupType.A, 0.25D),
									new GroupAttackProbabilityResponse(GroupType.B, 0.25D),
									new GroupAttackProbabilityResponse(GroupType.C, 0.25D),
									new GroupAttackProbabilityResponse(GroupType.D, 0.25D))), 
					"1"));
			afterINTResponse.getAttackLocationResponse().setTrialPartTime_ms(1700L);
			//afterINTResponse.getSurpriseReportResponse().setTrialPartTime_ms(1200L);
			response.getAttackLocationResponses_afterINTs().add(afterINTResponse);
		}
		response.setTroopAllocationResponse(new TroopAllocationResponse_MultiGroup(
				new ArrayList<TroopAllocation>(Arrays.asList(
						new TroopAllocation(GroupType.A, 0.25D),
						new TroopAllocation(GroupType.B, 0.25D),
						new TroopAllocation(GroupType.C, 0.25D),
						new TroopAllocation(GroupType.D, 0.25D)))));
		response.getTroopAllocationResponse().setTrialPartTime_ms(1800L);
		response.setGroundTruthSurpriseResponse(new SurpriseReportProbeResponse(3));
		response.getGroundTruthSurpriseResponse().setTrialPartTime_ms(1500L);
		
		return response;
	}
}