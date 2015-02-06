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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.feature_vector.phase_1.FeatureVectorFileDescriptor;
import org.mitre.icarus.cps.feature_vector.phase_1.MovintType;
import org.mitre.icarus.cps.feature_vector.phase_1.Road;

/**
 * Contains an ordered list of Task 3 trial blocks defining a Task 3 
 * phase in an exam.
 * 
 * @author CBONACETO
 *
 */
@XmlRootElement(name="Task_3_Phase", namespace="IcarusCPD_1")
@XmlType(name="Task_3_Phase", namespace="IcarusCPD_1")
public class Task_3_Phase extends Task_1_2_3_PhaseBase<Task_3_TrialBlock> {
	
	/** The roads file */
	protected FeatureVectorFileDescriptor roadsFile;
	
	/** The roads */
	protected ArrayList<Road> roads;	
	
	/** The trial blocks */
	protected ArrayList<Task_3_TrialBlock> trialBlocks;	
	
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
	 * Set the roads feature vector file information. The roads feature vector contains the roads.
	 * 
	 * @param roadsFile the roads feature vector file information
	 */
	public void setRoadsFile(FeatureVectorFileDescriptor roadsFile) {
		this.roadsFile = roadsFile;
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

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_1.testing.Task_1_2_3_PhaseBase#getTrialBlocks()
	 */
	@XmlElement(name="TrialBlock")
	@Override
	public ArrayList<Task_3_TrialBlock> getTrialBlocks() {
		return trialBlocks;	
	}
	
	/**
	 * Set the trial blocks.
	 * 
	 * @param trialBlocks the trial blocks
	 */
	public void setTrialBlocks(ArrayList<Task_3_TrialBlock> trialBlocks) {
		this.trialBlocks = trialBlocks;
	}	
	
	/**
	 * Creates a sample Task_3_Phase.
	 * 
	 * @param numBlocks
	 * @param trialsPerBlock
	 * @param createSampleResponses
	 * @return
	 */
	public static Task_3_Phase createSampleTask_3_Phase(int numBlocks,  int trialsPerBlock, boolean createSampleResponses) {
		Task_3_Phase phase = new Task_3_Phase();
		phase.setName("Task_3_Phase");	
		
		phase.setRoadsFile(new FeatureVectorFileDescriptor("roads.kml", "roads.csv"));
		ArrayList<Road> roads = new ArrayList<Road>(Arrays.asList(
				new Road("1", MovintType.DenseTraffic),
				new Road("2", MovintType.SparseTraffic),
				new Road("3", MovintType.SparseTraffic),
				new Road("4", MovintType.DenseTraffic)));
		phase.setRoads(roads);
		
		phase.trialBlocks = new ArrayList<Task_3_TrialBlock>(numBlocks);		
		for(int blockNum=1; blockNum<=numBlocks; blockNum++) {
			phase.trialBlocks.add(Task_3_TrialBlock.createSampleTask_3_TrialBlock(
					blockNum, trialsPerBlock, createSampleResponses));
		}
		
		return phase;
	}
}