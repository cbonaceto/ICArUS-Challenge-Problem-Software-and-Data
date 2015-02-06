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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.phase_1.response.Task_1_ProbeTrialResponse;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.AttackLocationProbeResponse_MultiGroup;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.GroupAttackProbabilityResponse;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.SurpriseReportProbeResponse;
import org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.AttackLocationProbe_MultiGroup;
import org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.SurpriseReportProbe;
import org.mitre.icarus.cps.feature_vector.phase_1.FeatureVectorFileDescriptor;
import org.mitre.icarus.cps.feature_vector.phase_1.GridLocation2D;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupType;

/**
 * A block of trials for Task 1.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="Task_1_TrialBlock", namespace="IcarusCPD_1",
		propOrder={"groupAttackPresentationTimes_ms", "probeTrial"})
public class Task_1_TrialBlock extends Task_1_2_3_TrialBlockBase {

	/** The probe trial at the end of the block of attack presentations */
	protected Task_1_ProbeTrial probeTrial;
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_1.testing.Task_1_2_3_TrialBlockBase#getGroupAttackPresentationTimes_ms()
	 */
	@XmlElement(name="GroupAttackPresentationTimes")
	@XmlList
	@Override
	public ArrayList<Long> getGroupAttackPresentationTimes_ms() {
		return super.getGroupAttackPresentationTimes_ms();
	}	
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_1.testing.Task_1_2_3_TrialBlockBase#setGroupAttackPresentationTimes_ms(java.util.ArrayList)
	 */
	@Override
	public void setGroupAttackPresentationTimes_ms(ArrayList<Long> groupAttackPresentationTimes_ms) {
		super.setGroupAttackPresentationTimes_ms(groupAttackPresentationTimes_ms);
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_1.testing.Task_1_2_3_TrialBlockBase#getProbeTrial()
	 */
	@XmlElement(name="ProbeTrial")
	@Override
	public Task_1_ProbeTrial getProbeTrial() {
		return probeTrial;
	}	
	
	/**
	 * Set the probe trial.
	 * 
	 * @param probeTrial the probe trial
	 */
	public void setProbeTrial(Task_1_ProbeTrial probeTrial) {
		this.probeTrial = probeTrial;
	}	
	
	/**
	 * Create a sample Task_1_TrialBlock.
	 * 
	 * @param blockNum
	 * @param numTrials
	 * @param createSampleResponses
	 * @return the sample Task_1_TrialBlock
	 */
	public static Task_1_TrialBlock createSampleTask_1_TrialBlock(int blockNum, int numTrials,
			boolean createSampleResponses) {
		
		Task_1_TrialBlock trialBlock = new Task_1_TrialBlock();
		trialBlock.setTrialBlockNum(blockNum);
		trialBlock.setNumPresentationTrials(numTrials-1);
		trialBlock.setFeatureVectorFile(new FeatureVectorFileDescriptor("task1" + "_" + blockNum + ".kml",
				"task1" + "_" + blockNum + ".csv"));
		Task_1_ProbeTrial probe = new Task_1_ProbeTrial();		
		trialBlock.setProbeTrial(probe);
		//probe.setTrialNum(numTrials);
		ArrayList<GroupType> groups =new ArrayList<GroupType>(Arrays.asList(GroupType.X, GroupType.O));
		probe.setAttackLocationProbe(new AttackLocationProbe_MultiGroup(new GridLocation2D("1", 25, 18), groups));
		probe.setGroundTruth(new GroundTruth(GroupType.X));
		probe.setGroundTruthSurpriseProbe(new SurpriseReportProbe(0, 6, 1));		
		
		if(createSampleResponses) {			
			Task_1_ProbeTrialResponse response = new Task_1_ProbeTrialResponse();
			probe.setTrialResponse(response);
			response.setTrialTime_ms(2340L);
			AttackLocationProbeResponse_MultiGroup attackLocationResponse = new AttackLocationProbeResponse_MultiGroup();
			attackLocationResponse.setGroupAttackProbabilities(
					new ArrayList<GroupAttackProbabilityResponse>(Arrays.asList(
					new GroupAttackProbabilityResponse(GroupType.X, .5D),
					new GroupAttackProbabilityResponse(GroupType.O, .5D)
					)));
			attackLocationResponse.setTrialPartTime_ms(1500L);			
			response.setAttackLocationResponse(attackLocationResponse);
			response.setGroundTruthSurpriseResponse(new SurpriseReportProbeResponse(5));	
			response.getGroundTruthSurpriseResponse().setTrialPartTime_ms(1500L);
			
			trialBlock.setGroupAttackPresentationTimes_ms(new ArrayList<Long>(numTrials-1));
			for(int i=0; i<numTrials-1; i++) {
				trialBlock.getGroupAttackPresentationTimes_ms().add(1283L);
			}
		}
		
		return trialBlock;
	}
}