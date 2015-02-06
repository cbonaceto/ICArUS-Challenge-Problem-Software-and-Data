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
package org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
//import javax.xml.bind.annotation.XmlTransient;



/**
 * @author CBONACETO
 *
 */
@Entity
public class OverallCFACPAMetric extends Metric implements Serializable {	
	private static final long serialVersionUID = -249063923331104735L;
	
	private int id;
	
	//@XmlTransient
	public Double creditsEarned;
	
	//@XmlTransient
	@ElementCollection
	public Set<Integer> tasks_present;	
	
	//@XmlTransient	
	@ElementCollection
	public Set<Integer> tasks_missing;
	
	//@XmlTransient
	public Integer trials_stages_present;	
	
	//@XmlTransient
	public Integer trials_stages_missing;	
	
	public OverallCFACPAMetric() {}
	
	public OverallCFACPAMetric(Double score, Set<Integer> tasks_present, Integer trials_stages_present) {
		this.score = score;
		this.trials_stages_present = trials_stages_present;
		this.tasks_present = tasks_present;
	}
	
	@Id @GeneratedValue
	@Column( name="overallCFACPAMetricId" )
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	/*public Double getScore() {
		return score;
	}
	
	public void setScore(Double score) {
		this.score = score;
	}

	public Double getCreditsEarned() {
		return creditsEarned;
	}

	public void setCreditsEarned(Double creditsEarned) {
		this.creditsEarned = creditsEarned;
	}

	@ElementCollection
	public Set<Integer> getTasks_present() {
		return tasks_present;
	}

	public void setTasks_present(Set<Integer> tasks_present) {
		this.tasks_present = tasks_present;
	}

	@ElementCollection
	public Set<Integer> getTasks_missing() {
		return tasks_missing;
	}

	public void setTasks_missing(Set<Integer> tasks_missing) {
		this.tasks_missing = tasks_missing;
	}

	public Integer getTrials_stages_present() {
		return trials_stages_present;
	}

	public void setTrials_stages_present(Integer trials_stages_present) {
		this.trials_stages_present = trials_stages_present;
	}

	public Integer getTrials_stages_missing() {
		return trials_stages_missing;
	}

	public void setTrials_stages_missing(Integer trials_stages_missing) {
		this.trials_stages_missing = trials_stages_missing;
	}*/
}