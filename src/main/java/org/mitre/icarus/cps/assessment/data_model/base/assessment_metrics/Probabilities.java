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
import java.util.Iterator;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Entity represent a list of probability values.
 * 
 * @author LWONG
 */
@Entity
public class Probabilities implements Serializable, Iterable<Double> {	
	private static final long serialVersionUID = -6655515800987794241L;
	
	private int probId;

	private List<Double> probs;
	
	public Probabilities(List<Double> probs) {
		this.probs = probs;
	}
	
	public Probabilities() {}
	
	@Id
	@GeneratedValue
	@Column( name="probabilitiesId" )
	public int getId() {
		return probId;
	}
	
	public void setId(int id) {
		this.probId = id;
	}
	
	@ElementCollection(fetch=FetchType.EAGER)
	public List<Double> getProbs() {
		return probs;
	}
	
	public void setProbs(List<Double> probs) {
		this.probs = probs;
	}

	@Override
	public Iterator<Double> iterator() {
		if(probs != null) {
			return probs.iterator();
		}
		return null;
	}	
}