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

import org.mitre.icarus.cps.exam.phase_1.response.Task_3_ProbeTrialResponse;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.AttackLocationProbeResponse_MultiGroup;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.GroupAttackProbabilityResponse;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.GroupCentersProbeResponse;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.GroupCentersProbeResponse.GroupCenterResponse;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.SurpriseReportProbeResponse;
import org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.AttackLocationProbe_MultiGroup;
import org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.GroupCentersProbe;
import org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.SurpriseReportProbe;
import org.mitre.icarus.cps.feature_vector.phase_1.FeatureVectorFileDescriptor;
import org.mitre.icarus.cps.feature_vector.phase_1.GridLocation2D;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupType;

/**
 * A block of trials for Task 3.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="Task_3_TrialBlock", namespace="IcarusCPD_1", 
		propOrder={"groupAttackPresentationTimes_ms", "probeTrial"})
public class Task_3_TrialBlock extends Task_1_2_3_TrialBlockBase {	
	
	/** The probe trial at the end of the block of attack presentations */
	protected Task_3_ProbeTrial probeTrial;	
	
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
	public Task_3_ProbeTrial getProbeTrial() {
		return probeTrial;
	}	
	
	/**
	 * Set the probe trial.
	 * 
	 * @param probeTrial the probe trial
	 */
	public void setProbeTrial(Task_3_ProbeTrial probeTrial) {
		this.probeTrial = probeTrial;
	}
	
	/**
	 * Create a sample Task_3_TrialBlock.
	 * 
	 * @param blockNum
	 * @param numTrials
	 * @param createSampleResponses
	 * @return
	 */
	public static Task_3_TrialBlock createSampleTask_3_TrialBlock(int blockNum, int numTrials, 
			boolean createSampleResponses) {
		
		Task_3_TrialBlock trialBlock = new Task_3_TrialBlock();
		trialBlock.setTrialBlockNum(blockNum);
		trialBlock.setNumPresentationTrials(numTrials-1);
		trialBlock.setFeatureVectorFile(new FeatureVectorFileDescriptor("task3" + "_" + blockNum + ".kml",
				"task3" + "_" + blockNum + ".csv"));
		Task_3_ProbeTrial probe = new Task_3_ProbeTrial();		
		trialBlock.setProbeTrial(probe);
		//probe.setTrialNum(numTrials);
		ArrayList<GroupType> groups = new ArrayList<GroupType>(Arrays.asList(GroupType.A, GroupType.B, 
				GroupType.C, GroupType.D));
		probe.setAttackLocationProbe(new AttackLocationProbe_MultiGroup(new GridLocation2D("1", 13, 37), groups));
		probe.setGroupCentersProbe(new GroupCentersProbe(groups));
		probe.setGroundTruth(new GroundTruth(GroupType.D));
		probe.setGroundTruthSurpriseProbe(new SurpriseReportProbe(0, 6, 1));		
		
		if(createSampleResponses) {			
			Task_3_ProbeTrialResponse response = new Task_3_ProbeTrialResponse();
			probe.setTrialResponse(response);
			response.setTrialTime_ms(3350L);
			AttackLocationProbeResponse_MultiGroup attackLocationResponse = new AttackLocationProbeResponse_MultiGroup();
			attackLocationResponse.setGroupAttackProbabilities(
					new ArrayList<GroupAttackProbabilityResponse>(Arrays.asList(
					new GroupAttackProbabilityResponse(GroupType.A, .25D),
					new GroupAttackProbabilityResponse(GroupType.B, .25D),
					new GroupAttackProbabilityResponse(GroupType.C, .25D),
					new GroupAttackProbabilityResponse(GroupType.D, .25D)
					)));
			attackLocationResponse.setTrialPartTime_ms(1808L);			
			response.setAttackLocationResponse(attackLocationResponse);
			response.setGroupCentersResponse(new GroupCentersProbeResponse(new ArrayList<GroupCenterResponse>(
					Arrays.asList(
							new GroupCenterResponse(GroupType.A, new GridLocation2D(null, 3, 4)),
							new GroupCenterResponse(GroupType.B, new GridLocation2D(null, 8, 5)),
							new GroupCenterResponse(GroupType.C, new GridLocation2D(null, 8, 12)),
							new GroupCenterResponse(GroupType.D, new GridLocation2D(null, 6, 4)))
							)));
			response.getGroupCentersResponse().setTrialPartTime_ms(1500L);
			response.setGroundTruthSurpriseResponse(new SurpriseReportProbeResponse(5));	
			response.getGroundTruthSurpriseResponse().setTrialPartTime_ms(1200L);
			
			trialBlock.setGroupAttackPresentationTimes_ms(new ArrayList<Long>(numTrials-1));
			for(int i=0; i<numTrials-1; i++) {
				trialBlock.getGroupAttackPresentationTimes_ms().add(1290L);
			}
		}
		
		return trialBlock;
	}
}