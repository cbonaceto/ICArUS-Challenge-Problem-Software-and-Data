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

import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * Abstract base class for Task 1, 2, and 3 phases in an exam.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="Task_1_2_3_PhaseBase", namespace="IcarusCPD_1")
@XmlSeeAlso({Task_1_Phase.class, Task_2_Phase.class, Task_3_Phase.class})
public abstract class Task_1_2_3_PhaseBase<T extends Task_1_2_3_TrialBlockBase> extends TaskTestPhase<IcarusTestTrial_Phase1> {
	
	/**
	 * Get the trial blocks.
	 * 
	 * @return the trial blocks
	 */
	@XmlTransient
	public abstract ArrayList<T> getTrialBlocks();
	
	/**
	 * Get the number of trial blocks
	 * 
	 * @return the number of trial blocks in the task.
	 */
	public int getNumTrialBlocks() {
		if(getTrialBlocks() != null) {
			return getTrialBlocks().size();
		}
		return 0;
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_1.testing.TaskTestPhase#getTestTrials()
	 */
	@XmlTransient
	@Override
	public ArrayList<IcarusTestTrial_Phase1> getTestTrials() {
		//return getProbeTrials();
		return getAllTrials();
	}
	
	/**
	 * Get a list of all trials in order, including the attack presentation and probe trials.
	 * 
	 * @return all trials
	 */
	@XmlTransient
	public ArrayList<IcarusTestTrial_Phase1> getAllTrials() {
		ArrayList<IcarusTestTrial_Phase1> testTrials = null;		
		if(getTrialBlocks() != null && !getTrialBlocks().isEmpty()) {
			testTrials = new ArrayList<IcarusTestTrial_Phase1>();
			for(Task_1_2_3_TrialBlockBase trialBlock : getTrialBlocks()) {
				if(trialBlock.getGroupAttackPresentations() != null) {
					for(AttackLocationPresentationTrial groupAttackTrial : trialBlock.getGroupAttackPresentations()) {
						testTrials.add(groupAttackTrial);
					}
				}
				if(trialBlock.getProbeTrial() != null) {
					testTrials.add(trialBlock.getProbeTrial());
				}
			}
		}
		return testTrials;
	}
	
	/**
	 * Get a list of the probe trials in the task.
	 * 
	 * @return the probe trials
	 */
	@XmlTransient
	public ArrayList<IcarusTestTrial_Phase1> getProbeTrials() {
		ArrayList<IcarusTestTrial_Phase1> probeTrials = null;
		if(getTrialBlocks() != null && !getTrialBlocks().isEmpty()) {
			probeTrials = new ArrayList<IcarusTestTrial_Phase1>();
			for(Task_1_2_3_TrialBlockBase trialBlock : getTrialBlocks()) {
				probeTrials.add(trialBlock.getProbeTrial());
			}
		}
		return probeTrials;
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.base.testing.IcarusTestPhase#getNumTrials()
	 */
	@Override
	public int getNumTrials() {
		int numTrials = 0;
		if(getTrialBlocks() != null) {
			for(Task_1_2_3_TrialBlockBase trialBlock : getTrialBlocks()) {
				numTrials += trialBlock.getNumTrials();
			}
		}
		return numTrials;
	}
}