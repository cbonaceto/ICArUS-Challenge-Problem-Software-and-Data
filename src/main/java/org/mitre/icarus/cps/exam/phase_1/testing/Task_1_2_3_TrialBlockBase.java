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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.feature_vector.phase_1.FeatureVectorFileDescriptor;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupAttack;
import org.mitre.icarus.cps.feature_vector.phase_1.TaskData;

/**
 * The base class for a Task 1, 2, and 3 trial blocks.  Contains the attack presentation trials
 * and abstract methods to get the probe trial and the subject/model response to the probe trial.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="Task_1_2_3_TrialBlockBase", namespace="IcarusCPD_1", 
		propOrder={"trialBlockNum", "numPresentationTrials", 
		"featureVectorFile"})
@XmlSeeAlso({Task_1_TrialBlock.class, Task_2_TrialBlock.class, Task_3_TrialBlock.class})
public abstract class Task_1_2_3_TrialBlockBase  {
	
	/** The trial block number */
	protected Integer trialBlockNum;
	
	/** Feature vector file defining each attack location */	
	protected FeatureVectorFileDescriptor featureVectorFile;
	
	/** The number of attack presentation trials in the feature vector */
	protected Integer numPresentationTrials;
	
	/** The attack presentation trials (loaded from the feature vector) */
	protected ArrayList<AttackLocationPresentationTrial> groupAttackPresentations;
	
	/** The timing data for the attack presentation trials (in ms, for human subject use only) */	
	protected ArrayList<Long> groupAttackPresentationTimes_ms;
	
	/**
	 * Get the trial block number.
	 * 
	 * @return - the trial block number
	 */
	@XmlAttribute(name="trialBlockNum")	
	public Integer getTrialBlockNum() {
		return trialBlockNum;
	}

	/**
	 * Set the trial block number.
	 * 
	 * @param trialBlockNum - the trial block number
	 */
	public void setTrialBlockNum(Integer trialBlockNum) {
		this.trialBlockNum = trialBlockNum;
	}

	/**
	 * Get the feature vector file information. The feature vector contains
	 * the group attack presentation trials.
	 * 
	 * @return - the feature vector file information
	 */
	@XmlElement(name="FeatureVectorFile")
	public FeatureVectorFileDescriptor getFeatureVectorFile() {
		return featureVectorFile;
	}

	/**
	 * Set the feature vector file information. The feature vector contains
	 * the group attack presentation trials.
	 * 
	 * @param featureVectorFile - the feature vector file information
	 */
	public void setFeatureVectorFile(FeatureVectorFileDescriptor featureVectorFile) {
		this.featureVectorFile = featureVectorFile;
	}
	
	/**
	 * Populates groupAttackPresentations from the given taskData.
	 * 
	 * @param taskData - contains the group attack data for each group attack presentation trial
	 */
	public void setTaskData(TaskData taskData) {
		if(taskData != null && taskData.getAttacks() != null && !taskData.getAttacks().isEmpty()) {
			groupAttackPresentations = new ArrayList<AttackLocationPresentationTrial>(taskData.getAttacks().size());
			for(GroupAttack attack : taskData.getAttacks()) {
				groupAttackPresentations.add(new AttackLocationPresentationTrial(attack));
			}
		}
	}
	
	/**
	 * Get the number of group attack presentation trials.
	 * 
	 * @return - the number of trials
	 */
	@XmlAttribute(name="numPresentationTrials")
	public Integer getNumPresentationTrials() {
		return numPresentationTrials;
	}
	
	/**
	 * Set the number of group attack presentation trials.
	 * 
	 * @param numPresentationTrials - the number of trials
	 */
	public void setNumPresentationTrials(Integer numPresentationTrials) {
		this.numPresentationTrials = numPresentationTrials;
	}

	/**
	 * Get the group attack presentation trials. The trials are populated from 
	 * the feature vector data.
	 * 
	 * @return - the group attack presentation trials
	 */
	@XmlTransient
	public ArrayList<AttackLocationPresentationTrial> getGroupAttackPresentations() {
		return groupAttackPresentations;
	}

	/**
	 * Set the group attack presentation trials. The trials are populated from 
	 * the feature vector data.
	 * 
	 * @param groupAttackPresentations - the group attack presentation trials
	 */
	public void setGroupAttackPresentations(ArrayList<AttackLocationPresentationTrial> groupAttackPresentations) {
		this.groupAttackPresentations = groupAttackPresentations;
	}

	/**
	 * Get the probe trial at the end of the block.
	 * 
	 * @return - the probe trial
	 */
	public abstract Task_1_2_3_ProbeTrialBase getProbeTrial();		
	
	/**
	 * Get the total number of trials.
	 * 
	 * @return - the number of trials
	 */
	@XmlTransient
	public int getNumTrials() {
		int numTrials = 0;
		if(groupAttackPresentations != null) {
			numTrials = groupAttackPresentations.size();
		}
		if(getProbeTrial() != null) {
			return numTrials + 1;
		}
		return numTrials;
	}
	
	/**
	 * Get the time the subject spent on each group attack presentation trial in milliseconds.
	 * FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @return - the time spent on each group attack presentation trial in milliseconds.
	 */	
	@XmlTransient
	public ArrayList<Long> getGroupAttackPresentationTimes_ms() {
		return groupAttackPresentationTimes_ms;
	}
	
	/**
	 * Set the time the subject spent on each group attack presentation trial in milliseconds.
	 * FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @param groupAttackPresentationTimes_ms - the time spent on each group attack presentation trial in milliseconds.
	 */
	public void setGroupAttackPresentationTimes_ms(ArrayList<Long> groupAttackPresentationTimes_ms) {
		this.groupAttackPresentationTimes_ms = groupAttackPresentationTimes_ms;
	}	
}