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

import org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.ImintLayer;
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
 * Contains an ordered list of Task 6 trials defining a Task 6 phase in an exam.
 * 
 * @author CBONACETO
 *
 */
@XmlRootElement(name="Task_6_Phase", namespace="IcarusCPD_1")
@XmlType(name="Task_6_Phase", namespace="IcarusCPD_1")
public class Task_6_Phase extends Task_4_5_6_PhaseBase<Task_6_Trial> {
	
	/** The trials */
	protected ArrayList<Task_6_Trial> testTrials;		
	

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_1.testing.Task_4_5_6_PhaseBase#getTestTrials()
	 */
	@XmlElement(name="Trial")
	@Override	
	public ArrayList<Task_6_Trial> getTestTrials() {
		return testTrials;
	}
	
	/**
	 * Set the trials.
	 * 
	 * @param testTrials the trials
	 */
	public void setTestTrials(ArrayList<Task_6_Trial> testTrials) {
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
	 * Create a sample Task_6_Phase.
	 * 
	 * @param numTrials
	 * @param createSampleResponses
	 * @return
	 */
	public static Task_6_Phase createSampleTask_6_Phase(int numTrials, boolean createSampleResponses) {
		Task_6_Phase phase = new Task_6_Phase();		
		phase.setName("Task_6_Phase");		
		phase.testTrials = new ArrayList<Task_6_Trial>(numTrials);
		
		for(int trialNum=1; trialNum<=numTrials; trialNum++) {
			Task_6_Trial trial = new Task_6_Trial();
			phase.testTrials.add(trial);
			trial.setTrialNum(trialNum);			
			trial.setFeatureVectorFile(new FeatureVectorFileDescriptor("task6" + "_" + trialNum + ".kml",
					"task6" + "_" + trialNum + ".csv"));
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
			AttackLocationProbe_MultiGroup attackProbe = new AttackLocationProbe_MultiGroup(new GridLocation2D("3"), groups);
			SurpriseReportProbe surpriseProbe = new SurpriseReportProbe(0, 6, 1);
			trial.setNumLayersToShow(3);
			trial.setIntLayers(new ArrayList<Task_5_6_INTLayerPresentationProbe>(Arrays.asList(
					new Task_5_6_INTLayerPresentationProbe(new ImintLayer(), attackProbe),
					new Task_5_6_INTLayerPresentationProbe(new MovintLayer(), attackProbe),
					new Task_5_6_INTLayerPresentationProbe(
							new SigintLayer(GroupType.A), attackProbe),
					new Task_5_6_INTLayerPresentationProbe(
							new SigintLayer(GroupType.B), attackProbe),
					new Task_5_6_INTLayerPresentationProbe(
							new SigintLayer(GroupType.C), attackProbe),
					new Task_5_6_INTLayerPresentationProbe(
							new SigintLayer(GroupType.D), attackProbe),
					new Task_5_6_INTLayerPresentationProbe(new SocintLayer(), attackProbe))));
			trial.setTroopAllocationProbe(new TroopAllocationProbe_MultiGroup(groups));
			trial.setGroundTruth(new GroundTruth(GroupType.D));
			trial.setGroundTruthSurpriseProbe(surpriseProbe);			
			if(createSampleResponses) {
				trial.setTrialResponse(Task_5_Phase.createSampleTask_5_6_TrialResponse(true, 3, true));
			}
		}
		
		return phase;
	}
}