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
package org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.feature_vector.phase_1.GroupType;

/**
 * Contains a set of groups and initial attack probabilities for each group based 
 * on HUMINT data.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="HumintReport", namespace="IcarusCPD_1")
public class HumintReport {
	
	/** The groups the probabilities are for */
	protected ArrayList<GroupType> groups;
	
	/** The initial group attack probabilities from HUMINT data
	 *  (before additional INT layers are presented) */
	protected ArrayList<Double> humintProbabilities;
	
	/**
	 * Default constructor.
	 */
	public HumintReport() {}
	
	/**
	 * Constructor that takes the groups and initial attack probabilities from HUMINT data.
	 * 
	 * @param groups the groups 
	 * @param humintProbabilities the initial attack probabilities
	 */
	public HumintReport(ArrayList<GroupType> groups, ArrayList<Double> humintProbabilities) {
		this.groups = groups;
		this.humintProbabilities = humintProbabilities;
	}

	/**
	 * @return
	 */
	@XmlElement(name="Groups")
	@XmlList
	public ArrayList<GroupType> getGroups() {
		return groups;
	}

	/**
	 * @param groups
	 */
	public void setGroups(ArrayList<GroupType> groups) {
		this.groups = groups;
	}

	/**
	 * @return
	 */
	@XmlElement(name="Probabilities")
	@XmlList
	public ArrayList<Double> getHumintProbabilities() {
		return humintProbabilities;
	}

	/**
	 * @param humintProbabilities
	 */
	public void setHumintProbabilities(ArrayList<Double> humintProbabilities) {
		this.humintProbabilities = humintProbabilities;
	}	
}