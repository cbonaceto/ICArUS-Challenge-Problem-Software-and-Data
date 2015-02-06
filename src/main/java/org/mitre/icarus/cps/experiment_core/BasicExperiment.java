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

import java.util.Iterator;
import java.util.List;

import org.mitre.icarus.cps.experiment_core.condition.Condition;

public class BasicExperiment extends Experiment<Condition> implements Iterable<Condition> {

	/** The conditions in the experiment */
	protected List<Condition> conditions;

	/** Whether to show the subject's score at the end of the experiment */
	protected boolean showScore = true;

	public List<Condition> getConditions() {
		return conditions;
	}

	public void setConditions(List<Condition> conditions) {
		this.conditions = conditions;
	}

	@Override
	public Iterator<Condition> iterator() {
		if(conditions != null) {
			return conditions.iterator();
		}
		return null;
	}

	public boolean isShowScore() {
		return showScore;
	}

	public void setShowScore(boolean showScore) {
		this.showScore = showScore;
	}
}