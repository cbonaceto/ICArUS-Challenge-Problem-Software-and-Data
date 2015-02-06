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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Contains an ordered list of Task 2 trial blocks defining a Task 2 
 * phase in an exam.
 * 
 * @author CBONACETO
 *
 */
@XmlRootElement(name="Task_2_Phase", namespace="IcarusCPD_1")
@XmlType(name="Task_2_Phase", namespace="IcarusCPD_1")
public class Task_2_Phase extends Task_1_2_3_PhaseBase<Task_2_TrialBlock> {
	
	/** The trial blocks */
	protected ArrayList<Task_2_TrialBlock> trialBlocks;		

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_1.testing.Task_1_2_3_PhaseBase#getTrialBlocks()
	 */
	@XmlElement(name="TrialBlock")
	@Override
	public ArrayList<Task_2_TrialBlock> getTrialBlocks() {
		return trialBlocks;	
	}
	
	/**
	 * Set the trial blocks.
	 * 
	 * @param trialBlocks the trial blocks
	 */
	public void setTrialBlocks(ArrayList<Task_2_TrialBlock> trialBlocks) {
		this.trialBlocks = trialBlocks;
	}	
	
	/**
	 * Creates a sample Task_2_Phase.
	 * 
	 * @param numBlocks
	 * @param trialsPerBlock
	 * @param createSampleResponses
	 * @return the sample Task_2_Phase
	 */
	public static Task_2_Phase createSampleTask_2_Phase(int numBlocks,  int trialsPerBlock, boolean createSampleResponses) {
		Task_2_Phase phase = new Task_2_Phase();		
		phase.setName("Task_2_Phase");	
		phase.trialBlocks = new ArrayList<Task_2_TrialBlock>(numBlocks);
		
		for(int blockNum=1; blockNum<=numBlocks; blockNum++) {
			phase.trialBlocks.add(Task_2_TrialBlock.createSampleTask_2_TrialBlock(
					blockNum, trialsPerBlock, createSampleResponses));
		}
		
		return phase;
	}
}