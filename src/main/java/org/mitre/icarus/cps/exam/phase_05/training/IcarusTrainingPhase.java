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
package org.mitre.icarus.cps.exam.phase_05.training;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.base.training.IcarusTutorialPhase;
import org.mitre.icarus.cps.experiment_core.condition.InstructionsPage;

/**
 * Contains an ordered list of training trials defining a training phase in an exam.
 * 
 * @author CBONACETO
 *
 */
@XmlRootElement(name="TrainingPhase", namespace="IcarusCPD_05")
@XmlType(name="Training", namespace="IcarusCPD_05")
public class IcarusTrainingPhase extends IcarusTutorialPhase<InstructionsPage> { //extends IcarusExamPhase {
	
	/** The training trials */	
	protected ArrayList<IcarusTrainingTrial> trainingTrials;
	
	/**
	 * Get the training trials.
	 * 
	 * @return
	 */
	@XmlElement(name="Training")
	public ArrayList<IcarusTrainingTrial> getTrainingTrials() {
		return trainingTrials;
	}

	/**
	 * Set the training trials.
	 * 
	 * @param trainingTrials
	 */
	public void setTrainingTrials(ArrayList<IcarusTrainingTrial> trainingTrials) {
		this.trainingTrials = trainingTrials;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment_core.condition.Condition#getNumTrials()
	 */
	@Override
	public int getNumTrials() {
		if(trainingTrials != null) {
			return trainingTrials.size();
		}
		return 0;
	}
}