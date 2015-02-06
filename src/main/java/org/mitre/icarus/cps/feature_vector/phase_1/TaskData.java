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
package org.mitre.icarus.cps.feature_vector.phase_1;

import java.util.ArrayList;

/**
 * Contains task feature vector data (attack locations and group centers).
 * 
 * @author CBONACETO
 *
 */
public class TaskData {
	
	/** The attack locations for the task */
	private ArrayList<GroupAttack> attacks = new ArrayList<GroupAttack>();
	
	/** The group centers for the task */
	private ArrayList<GroupCenter> centers = new ArrayList<GroupCenter>();
	
	/** The distances for the task (from the group center to each attack location,
	 * or from the attack location to each group center)
	 */
	private ArrayList<Double> distances = new ArrayList<Double>();
	
	/**
	 * No arg constructor.
	 */
	public TaskData() {}
	
	/**
	 * Constructor that takes the attack locations and group centers.
	 * 
	 * @param attacks the attack locations
	 * @param centers the group centers
	 */
	public TaskData(ArrayList<GroupAttack> attacks, ArrayList<GroupCenter> centers) {
		this.attacks = attacks;
		this.centers = centers;
	}

	/**
	 * Get the attack locations.
	 * 
	 * @return the attack locations
	 */
	public ArrayList<GroupAttack> getAttacks() {
		return attacks;
	}

	/**
	 * Set the attack locations.
	 * 
	 * @param attacks the attack locations
	 */
	public void setAttacks(ArrayList<GroupAttack> attacks) {
		this.attacks = attacks;
	}

	/**
	 * Get the group centers.
	 * 
	 * @return the group centers
	 */
	public ArrayList<GroupCenter> getCenters() {
		return centers;
	}

	/**
	 * Set the group centers.
	 * 
	 * @param centers the group centers
	 */
	public void setCenters(ArrayList<GroupCenter> centers) {
		this.centers = centers;
	}

	/**
	 * Get the distances (from the group center to each attack location, or from the attack location to each group center). 
	 * 
	 * @return the distances
	 */
	public ArrayList<Double> getDistances() {
		return distances;
	}

	/**
	 * Set the distances (from the group center to each attack location, or from the attack location to each group center).
	 * 
	 * @param distances the distances
	 */
	public void setDistances(ArrayList<Double> distances) {
		this.distances = distances;
	}	
}