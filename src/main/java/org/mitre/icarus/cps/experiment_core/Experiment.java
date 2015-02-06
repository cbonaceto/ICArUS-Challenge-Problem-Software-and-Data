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
package org.mitre.icarus.cps.experiment_core;

import java.util.List;

import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.experiment_core.condition.Condition;

/**
 * Defines an experiment (e.g., the conditions, # of trials in each condition, etc.)
 * 
 * @author cbonaceto
 *
 */
@XmlType(name="Experiment")
public abstract class Experiment<C extends Condition>  {
	
	/** Whether to show the subject's score at the end of the experiment.
	 * FOR HUMAN SUBJECT USE ONLY. */
	protected boolean showScore = true;
	
	public Experiment() {}
	
	/**
	 * Get the list of conditions/phases in the experiment.
	 * 
	 * @return the list of conditions/phases
	 */
	public abstract List<C> getConditions();	

	/**
	 * FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @return
	 */
	@XmlTransient	
	public boolean isShowScore() {
		return showScore;
	}

	/**
	 * FOR HUMAN SUBJECT USE ONLY. 
	 * 
	 * @param showScore
	 */
	public void setShowScore(boolean showScore) {
		this.showScore = showScore;
	}
}
