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
package org.mitre.icarus.cps.experiment_core.subject_data;

import java.util.List;

import org.mitre.icarus.cps.experiment_core.condition.Condition;

/**
 * @author CBONACETO
 *
 */
public abstract class SubjectData implements Comparable<SubjectData> {
	
	/** The id of the subject the data is for */
	protected String subjectId;
	
	/** The current condition */
	protected int currentCondition;
	
	/** The current trial in the current condition */
	protected int currentTrial;
	
	/** The subject condition data */
	protected List<? extends SubjectConditionData> subjectConditionData;
	
	public SubjectData() {}
	
	public SubjectData(String subjectId) {
		this.subjectId = subjectId;
	}	
	
	public SubjectData(String subjectId, int currentCondition) {
		this.subjectId = subjectId;
		this.currentCondition = currentCondition;
	}
	
	/** Returns a new subject condition data object for the given condition and adds it
	 * to the list of condition data list */
	public abstract SubjectConditionData startCondition(Condition condition);
	
	public String getSubjectId() {
		return subjectId;
	}	

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public int getCurrentCondition() {
		return currentCondition;
	}

	public void setCurrentCondition(int currentCondition) {
		this.currentCondition = currentCondition;
	}

	public int getCurrentTrial() {
		return currentTrial;
	}

	public void setCurrentTrial(int currentTrial) {
		this.currentTrial = currentTrial;
	}

	public List<? extends SubjectConditionData> getSubjectConditionData() {
		return subjectConditionData;
	}

	/** Return the subject's score for the experiment */
	public abstract double getScore();
	
	@Override
	public int compareTo(SubjectData o) {
		if(subjectId != null) {
			return subjectId.compareTo(o.subjectId);
		} else {
			return -1;
		}
	}

	@Override
	public boolean equals(Object obj) {
		//System.out.println("here");
		if(subjectId != null && obj instanceof SubjectData) {
			//System.out.println("Here comparing " + subjectId + " and " + ((SubjectData)obj).subjectId);
			return subjectId.equals(((SubjectData)obj).subjectId);
		} else {		
			return super.equals(obj);
		}
	}
}