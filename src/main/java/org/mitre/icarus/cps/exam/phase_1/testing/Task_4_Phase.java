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

import org.mitre.icarus.cps.exam.phase_1.response.Task_4_TrialResponse;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.AttackLocationProbeResponse_MultiLocation;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.GroupAttackProbabilityResponse;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.INTLayerData;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.SurpriseReportProbeResponse;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.Task_4_7_AttackLocationProbeResponseAfterINT;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.TroopAllocation;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.TroopAllocationResponse_MultiLocation;
import org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.SocintLayer;
import org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.Task_4_INTLayerPresentationProbe;
import org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.AttackLocationProbe_MultiLocation;
import org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.SurpriseReportProbe;
import org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.TroopAllocationProbe_MultiLocation;
import org.mitre.icarus.cps.feature_vector.phase_1.FeatureVectorFileDescriptor;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupType;
import org.mitre.icarus.cps.feature_vector.phase_1.MovintType;
import org.mitre.icarus.cps.feature_vector.phase_1.Road;

/**
 * Contains an ordered list of Task 4 trials defining a Task 4 phase in an exam.
 * 
 * @author CBONACETO
 *
 */
@XmlRootElement(name="Task_4_Phase", namespace="IcarusCPD_1")
@XmlType(name="Task_4_Phase", namespace="IcarusCPD_1")
public class Task_4_Phase extends Task_4_5_6_PhaseBase<Task_4_Trial> {
	
	/** The trials */
	protected ArrayList<Task_4_Trial> testTrials;	

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_1.testing.Task_4_5_6_PhaseBase#getTestTrials()
	 */
	@Override
	@XmlElement(name="Trial")
	public ArrayList<Task_4_Trial> getTestTrials() {
		return testTrials;
	}
	
	/**
	 * Set the trials.
	 * 
	 * @param testTrials the trials
	 */
	public void setTestTrials(ArrayList<Task_4_Trial> testTrials) {
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
	 * Create a sample Task_4_Phase.
	 * 
	 * @param numTrials
	 * @param createSampleResponses
	 * @return
	 */
	public static Task_4_Phase createSampleTask_4_Phase(int numTrials, boolean createSampleResponses) {
		Task_4_Phase phase = new Task_4_Phase();		
		phase.setName("Task_4_Phase");		
		phase.testTrials = new ArrayList<Task_4_Trial>(numTrials);
		
		for(int trialNum=1; trialNum<=numTrials; trialNum++) {
			Task_4_Trial trial = new Task_4_Trial();			
			phase.testTrials.add(trial);
			trial.setTrialNum(trialNum);			
			trial.setFeatureVectorFile(new FeatureVectorFileDescriptor("task4" + "_" + trialNum + ".kml",
					"task4" + "_" + trialNum + ".csv"));
			trial.setRoadsFile(new FeatureVectorFileDescriptor("roads" + "_" + trialNum + ".kml",
					"roads" + "_" + trialNum + ".csv"));
			trial.setRegionsFile(new FeatureVectorFileDescriptor("regions" + "_" + trialNum + ".kml",
					"regions" + "_" + trialNum + ".csv"));			
			ArrayList<Road> roads = new ArrayList<Road>(Arrays.asList(
					new Road("1", MovintType.DenseTraffic),
					new Road("2", MovintType.SparseTraffic),
					new Road("3", MovintType.SparseTraffic),
					new Road("4", MovintType.DenseTraffic)));
			ArrayList<String> locations = new ArrayList<String>(Arrays.asList("1", "2", "3", "4"));
			trial.setRoads(roads);
			AttackLocationProbe_MultiLocation attackProbe = new AttackLocationProbe_MultiLocation(GroupType.A, locations);
			trial.setAttackLocationProbe_initial(attackProbe);
			trial.setIntLayers(new ArrayList<Task_4_INTLayerPresentationProbe>(Arrays.asList(					
					new Task_4_INTLayerPresentationProbe(new SocintLayer(), attackProbe ))));
			trial.setTroopAllocationProbe(new TroopAllocationProbe_MultiLocation(locations));
			trial.setGroundTruth(new GroundTruth("3"));
			trial.setGroundTruthSurpriseProbe(new SurpriseReportProbe(0, 6, 1));			
			if(createSampleResponses) {
				Task_4_TrialResponse response = new Task_4_TrialResponse();
				trial.setTrialResponse(response);
				response.setTrialTime_ms(4402L);
				response.setAttackLocationResponse_initial(new AttackLocationProbeResponse_MultiLocation(
						new ArrayList<GroupAttackProbabilityResponse>(Arrays.asList(
								new GroupAttackProbabilityResponse("1", 0.25D),
								new GroupAttackProbabilityResponse("2", 0.25D),
								new GroupAttackProbabilityResponse("3", 0.25D),
								new GroupAttackProbabilityResponse("4", 0.25D))), 
								GroupType.A));
				response.getAttackLocationResponse_initial().setTrialPartTime_ms(1056L);
				response.setAttackLocationResponses_afterINTs(new ArrayList<Task_4_7_AttackLocationProbeResponseAfterINT>(Arrays.asList(
						new Task_4_7_AttackLocationProbeResponseAfterINT(
								new INTLayerData(new SocintLayer(), false),
								response.getAttackLocationResponse_initial()))));				
				response.setTroopAllocationResponse(new TroopAllocationResponse_MultiLocation(
						new ArrayList<TroopAllocation>(Arrays.asList(
								new TroopAllocation("1", 0.25D),
								new TroopAllocation("2", 0.25D),
								new TroopAllocation("3", 0.25D),
								new TroopAllocation("4", 0.25D)))));
				response.getTroopAllocationResponse().setTrialPartTime_ms(2300L);
				response.setGroundTruthSurpriseResponse(new SurpriseReportProbeResponse(3));
				response.getGroundTruthSurpriseResponse().setTrialPartTime_ms(1500L);
			}
		}
		
		return phase;
	}
}