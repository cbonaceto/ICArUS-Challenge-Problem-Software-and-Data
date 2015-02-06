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
package org.mitre.icarus.cps.examples.phase_1;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.mitre.icarus.cps.app.util.CPSFileUtils;
import org.mitre.icarus.cps.app.util.ProbabilityUtils;
import org.mitre.icarus.cps.exam.phase_1.IcarusExam_Phase1;
import org.mitre.icarus.cps.exam.phase_1.loader.IcarusExamLoader_Phase1;
import org.mitre.icarus.cps.exam.phase_1.response.Task_3_ProbeTrialResponse;
import org.mitre.icarus.cps.exam.phase_1.response.Task_5_6_TrialResponse;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.AttackLocationProbeResponse_MultiGroup;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.GroupAttackProbabilityResponse;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.GroupCentersProbeResponse;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.GroupCentersProbeResponse.GroupCenterResponse;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.INTLayerData;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.SurpriseReportProbeResponse;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.Task_5_6_AttackLocationProbeResponseAfterINT;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.TroopAllocation;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.TroopAllocationResponse_MultiGroup;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.TroopSelectionResponse_MultiGroup;
import org.mitre.icarus.cps.exam.phase_1.testing.AttackLocationPresentationTrial;
import org.mitre.icarus.cps.exam.phase_1.testing.IcarusTestTrial_Phase1;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_3_Phase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_3_ProbeTrial;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_5_Phase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_5_Trial;
import org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.Task_5_6_INTLayerPresentationProbe;
import org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.SurpriseReportProbe;
import org.mitre.icarus.cps.feature_vector.phase_1.GridLocation2D;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupAttack;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupType;

/**
 * Contains examples of reading exam and feature vector files, generating random responses
 * to trials, and marshalling response data back to XML. Currently has examples
 * for Tasks 3 and 5.
 * 
 * @author CBONACETO
 *
 */
public class ModelExample {
	
	/**
	 * Example for Task 3. Generates random responses for all trials in Task 3.
	 * 
	 * @param exam The exam containing Task 3.
	 */
	public static void task3Example(IcarusExam_Phase1 exam) {		
		//Get Task 3 from the exam
		Task_3_Phase task3 = (Task_3_Phase)exam.getTasks().get(2);

		//Initialize a list of the attacks we've "observed"
		List<GroupAttack> attacksObserved = new LinkedList<GroupAttack>();

		//Iterate over the trials in Task 3 and generate random responses to each probe trial
		Random rand = new Random();
		for(IcarusTestTrial_Phase1 trial : task3.getTestTrials()) {
			if(trial instanceof AttackLocationPresentationTrial) {
				//"Observe" an attack by adding it to the list of attacks we've seen
				attacksObserved.add(((AttackLocationPresentationTrial)trial).getGroupAttack());
			}			
			if(trial instanceof Task_3_ProbeTrial) {
				//Generate a random response to the current probe trial based on the attacks we've seen
				Task_3_ProbeTrial probeTrial = (Task_3_ProbeTrial)trial;
				probeTrial.setTrialResponse(generateRandomTask3Response(probeTrial, attacksObserved, rand));
				 
				//"Observe" ground truth and add the attack to the list of attacks we've observed				
				attacksObserved.add(new GroupAttack(probeTrial.getGroundTruth().getResponsibleGroup(), 
						probeTrial.getAttackLocationProbe().getAttackLocation()));
			}
		}		

		//Marshall the task with the responses to XML and display the result
		try {
			System.out.println("Task 3 with Responses:");
			System.out.println(IcarusExamLoader_Phase1.marshalTask(task3));
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}	
	
	/**
	 * Generates a random response to a Task 3 probe trial.
	 * 
	 * @param probeTrial The task 3 probe trial	 
	 * @param attacksObserved The list of attacks that have been previously observed
	 * @param rand Random number generator to use
	 * @return A random response to the trial
	 */
	public static Task_3_ProbeTrialResponse generateRandomTask3Response(Task_3_ProbeTrial probeTrial,
			List<GroupAttack> attacksObserved, Random rand) {
		Task_3_ProbeTrialResponse trialResponse = new Task_3_ProbeTrialResponse();
		probeTrial.setTrialResponse(trialResponse);

		//First generate a response to the group centers probe
		GroupCentersProbeResponse groupCentersResponse = new GroupCentersProbeResponse();
		trialResponse.setGroupCentersResponse(groupCentersResponse);
		ArrayList<GroupType> groups = probeTrial.getGroupCentersProbe().getGroups();
		ArrayList<GroupCenterResponse> groupCenters = new ArrayList<GroupCenterResponse>(groups.size());
		groupCentersResponse.setGroupCenters(groupCenters);
		for(GroupType group : groups) {
			//Generate a random point for the group center
			groupCenters.add(new GroupCenterResponse(group, 
					new GridLocation2D(rand.nextDouble() * 100, rand.nextDouble() * 100)));
		}

		//Next, generate a response to the group probe
		AttackLocationProbeResponse_MultiGroup attackLocationResponse = new AttackLocationProbeResponse_MultiGroup();
		trialResponse.setAttackLocationResponse(attackLocationResponse);				
		//Get the attack location being probed
		GridLocation2D attackLocation = probeTrial.getAttackLocationProbe().getAttackLocation();
		attackLocationResponse.setAttackLocationId(attackLocation.getLocationId());
		//Generate random probabilities for that each group is responsible for the attack at the location
		groups = probeTrial.getAttackLocationProbe().getGroups();
		ArrayList<Double> groupProbs = new ArrayList<Double>(groups.size());				
		for(int i=0; i<groups.size(); i++) {
			groupProbs.add(rand.nextDouble() * 100);
		}				
		//Normalize the probabilities and set them in the response
		ProbabilityUtils.normalizePercentProbabilities_Double(groupProbs, groupProbs);
		ArrayList<GroupAttackProbabilityResponse> groupAttackProbabilities = new ArrayList<GroupAttackProbabilityResponse>(groups.size());
		attackLocationResponse.setGroupAttackProbabilities(groupAttackProbabilities);
		int i = 0;
		for(GroupType group : groups) {
			groupAttackProbabilities.add(new GroupAttackProbabilityResponse(group, groupProbs.get(i)));
			i++;
		}				

		//Generate a random response to the troop allocation probe
		TroopSelectionResponse_MultiGroup troopSelectionResponse = new TroopSelectionResponse_MultiGroup();
		trialResponse.setTroopSelectionResponse(troopSelectionResponse);
		groups = probeTrial.getTroopSelectionProbe().getGroups();
		troopSelectionResponse.setGroup(groups.get(rand.nextInt(groups.size())));		

		//Generate a random response to the surprise probe after observing ground truth
		SurpriseReportProbe surpriseProbe = probeTrial.getGroundTruthSurpriseProbe();
		trialResponse.setGroundTruthSurpriseResponse(new SurpriseReportProbeResponse(
				surpriseProbe.getMinSurpriseValue() + 
				rand.nextInt(surpriseProbe.getMaxSurpriseValue() - surpriseProbe.getMinSurpriseValue() + 1)));		
		return null;
	}
	
	/**
	 * Example for Task 5. Generates random responses to all trials in Task 5.
	 * 
	 * @param exam The exam containing Task 5.
	 */
	public static void task5Example(IcarusExam_Phase1 exam) {
		//Get Task 5 from the exam
		Task_5_Phase task5 = (Task_5_Phase)exam.getTasks().get(4);		

		//Iterate over the trials in Task 5 and generate random responses to each trial
		Random rand = new Random();
		for(Task_5_Trial trial : task5.getTestTrials()) {			
			//Generate a response to the current trial
			trial.setTrialResponse(generateRandomTask5Response(trial, rand));		
		}

		//Marshall the task with the responses to XML and display the result
		try {
			System.out.println("Task 5 with Responses:");
			System.out.println(IcarusExamLoader_Phase1.marshalTask(task5));
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}	
	
	/**
	 * Generate a random response to a Task 5 trial.
	 * 
	 * @param trial The Task 5 trial
	 * @param rand Random number generator to use
	 * @return A random response to the trial
	 */
	public static Task_5_6_TrialResponse generateRandomTask5Response(Task_5_Trial trial, Random rand) {
		Task_5_6_TrialResponse trialResponse = new Task_5_6_TrialResponse();
		trial.setTrialResponse(trialResponse);

		//Generate a response to the group probe after each INT presentation
		ArrayList<Task_5_6_AttackLocationProbeResponseAfterINT> intLayerResponses = 
				new ArrayList<Task_5_6_AttackLocationProbeResponseAfterINT>(trial.getIntLayers().size());
		trialResponse.setAttackLocationResponses_afterINTs(intLayerResponses);
		for(Task_5_6_INTLayerPresentationProbe intLayerProbe : trial.getIntLayers()) {		
			Task_5_6_AttackLocationProbeResponseAfterINT intLayerResponse = new Task_5_6_AttackLocationProbeResponseAfterINT();
			intLayerResponse.setIntLayerShown(new INTLayerData(intLayerProbe.getLayerType(), false));
			intLayerResponses.add(intLayerResponse);
			
			//Generate a random response to the group probe after observing the INT layer
			AttackLocationProbeResponse_MultiGroup attackLocationResponse = new AttackLocationProbeResponse_MultiGroup();
			intLayerResponse.setAttackLocationResponse(attackLocationResponse);				
			//Get the attack location being probed
			GridLocation2D attackLocation = intLayerProbe.getAttackLocationProbe().getAttackLocation();
			attackLocationResponse.setAttackLocationId(attackLocation.getLocationId());
			//Generate random probabilities that each group is responsible for the attack at the location
			ArrayList<GroupType> groups = intLayerProbe.getAttackLocationProbe().getGroups();
			ArrayList<Double> groupProbs = new ArrayList<Double>(groups.size());				
			for(int i=0; i<groups.size(); i++) {
				groupProbs.add(rand.nextDouble() * 100);
			}				
			//Normalize the probabilities and set them in the response
			ProbabilityUtils.normalizePercentProbabilities_Double(groupProbs, groupProbs);
			ArrayList<GroupAttackProbabilityResponse> groupAttackProbabilities = new ArrayList<GroupAttackProbabilityResponse>(groups.size());
			attackLocationResponse.setGroupAttackProbabilities(groupAttackProbabilities);
			int i = 0;
			for(GroupType group : groups) {
				groupAttackProbabilities.add(new GroupAttackProbabilityResponse(group, groupProbs.get(i)));
				i++;
			}					
		}						

		//Generate a random response to the troop allocation probe after observing all INT layers
		TroopAllocationResponse_MultiGroup troopAllocationResponse = new TroopAllocationResponse_MultiGroup();
		trialResponse.setTroopAllocationResponse(troopAllocationResponse);
		ArrayList<GroupType> groups = trial.getTroopAllocationProbe().getGroups();
		ArrayList<Double> allocations = new ArrayList<Double>(groups.size());				
		for(int i=0; i<groups.size(); i++) {
			allocations.add(rand.nextDouble() * 100);
		}	
		//Normalize the troop allocations and set them in the response
		ProbabilityUtils.normalizePercentProbabilities_Double(allocations, allocations);
		ArrayList<TroopAllocation> troopAllocations = new ArrayList<TroopAllocation>(groups.size());
		troopAllocationResponse.setTroopAllocations(troopAllocations);
		int i = 0;
		for(GroupType group : groups) {
			troopAllocations.add(new TroopAllocation(group, allocations.get(i)));
			i++;
		}							

		//Generate a random response to the surprise probe after observing ground truth
		SurpriseReportProbe surpriseProbe = trial.getGroundTruthSurpriseProbe();
		trialResponse.setGroundTruthSurpriseResponse(new SurpriseReportProbeResponse(
				surpriseProbe.getMinSurpriseValue() + 
				rand.nextInt(surpriseProbe.getMaxSurpriseValue() - surpriseProbe.getMinSurpriseValue() + 1)));	
		return trialResponse;
	}
	
	/**
	 * Example main. Runs both the Task 3 and Task 5 example.
	 * 
	 * @param args Specify the exam file name in args[0]. If null, uses default name "data/Phase_1_CPD/exams/PilotExam-1-15/PilotExam-1-15.xml".
	 */
	public static void main(String[] args) {	
		//Flag to set whether to use CSV or KML feature vector files. If false, CSV files are used.
		boolean useKml = true;
		
		String fileName = "data/Phase_1_CPD/exams/PilotExam-1-15/PilotExam-1-15.xml"; 
		if(args != null && args.length > 0) {
			fileName = args[0];
		}

		//Load the exam 
		IcarusExam_Phase1 exam = null;
		URL examFileURL = null;
		try {
			examFileURL = CPSFileUtils.createFile(fileName).toURI().toURL();
			exam = ParsingExample.loadExamAndFeatureVectors(examFileURL, useKml);
		} catch(Exception ex) {
			ex.printStackTrace();
		}

		if(exam != null) {
			//Run the Task 3 example
			task3Example(exam);

			//Run the Task 5 example
			task5Example(exam);
		}

		System.out.println("Press ENTER to close this window");
		try {
			System.in.read();
		} catch(Exception err) {}
	}
}