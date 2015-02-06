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
package org.mitre.icarus.cps.exam.phase_2.testing;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * Mission 4, 5, or 6.
 * 
 * @author CBONACETO
 *
 */
@XmlRootElement(name="Mission_4_5_6", namespace="IcarusCPD_2")
@XmlType(name="Mission_4_5_6", namespace="IcarusCPD_2")
public class Mission_4_5_6 extends Mission<Mission_4_5_6_Trial> {
	
	/** The maximum number of batch plots that the participant may create in the course of the Mission */
	protected Integer maxNumBatchPlots;	
	
	/** The number of batch plots that have been created */
	protected Integer numBatchPlotsCreated;
	
	/** The trials */
	protected ArrayList<Mission_4_5_6_Trial> testTrials;
	
	/**
	 * Get the maximum number of batch plots that the participant may create in the course of the mission.
	 * 
	 * @return the maximum number of batch plots
	 */
	@XmlAttribute(name="maxNumBatchPlots")
	public Integer getMaxNumBatchPlots() {
		return maxNumBatchPlots;
	}

	/**
	 * Set the maximum number of batch plots that the participant may create in the course of the mission.
	 * 
	 * @param maxNumBatchPlots the maximum number of batch plots
	 */
	public void setMaxNumBatchPlots(Integer maxNumBatchPlots) {
		this.maxNumBatchPlots = maxNumBatchPlots;
	}
	
	/**
	 * Get the number of batch plots the participant created.
	 * FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @return the number of batch plots the participant created
	 */
	@XmlTransient
	public Integer getNumBatchPlotsCreated() {
		return numBatchPlotsCreated;
	}

	/**
	 * Set the number of batch plots the participant created.
	 * FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @param numBatchPlotsCreated the number of batch plots the participant created
	 */
	public void setNumBatchPlotsCreated(Integer numBatchPlotsCreated) {
		this.numBatchPlotsCreated = numBatchPlotsCreated;
	}
	
	/**
	 * @return
	 */
	public Integer getNumBatchPlotsRemaining() {		
		if(maxNumBatchPlots != null && numBatchPlotsCreated != null) {
			return maxNumBatchPlots - numBatchPlotsCreated;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.Mission#getTestTrials()
	 */
	@Override
	@XmlElement(name="Trial")
	public ArrayList<Mission_4_5_6_Trial> getTestTrials() {
		return testTrials;
	}

	/**
	 * Set the trials in the mission.
	 * 
	 * @param testTrials the trials in the mission
	 */
	public void setTestTrials(ArrayList<Mission_4_5_6_Trial> testTrials) {
		this.testTrials = testTrials;
	}
}