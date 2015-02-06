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
package org.mitre.icarus.cps.exam.phase_1.feedback;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.base.feedback.TestPhaseFeedback;
import org.mitre.icarus.cps.exam.phase_1.testing.IcarusTestTrial_Phase1;

/**
 * Contains feedback on each trial provided by the test harness to an ICARuS system after submitting
 * its responses to a task in an exam (e.g., a Task 3 phase). 
 * 
 * @author CBONACETO
 *
 */
@XmlRootElement(name="PhaseFeedback_CPD1", namespace="IcarusCPD_1")
@XmlType(name="PhaseFeedback_CPD1", namespace="IcarusCPD_1",
propOrder={"probabilitiesScore_s1", "troopAllocationScore_s2", "trialFeedback"})
public class TaskFeedback extends TestPhaseFeedback<IcarusTestTrial_Phase1, TrialFeedback_Phase1> {
	
	/** Feedback on each trial in the task */
	protected List<TrialFeedback_Phase1> trialFeedback;
	
	/** The average probabilities score (S1) for the task */
	protected Double probabilitiesScore_s1;
	
	/** The average troop allocation score (S2) for the task */
	protected Double troopAllocationScore_s2;

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.base.feedback.ExamPhaseFeedback#getTrialFeedback()
	 */
	@XmlElement(name="TrialFeedback")
	@Override
	public List<TrialFeedback_Phase1> getTrialFeedback() {
		return trialFeedback;
	}

	/**
	 * Set the feedback for each trial.
	 * 
	 * @param trialFeedback the feedback for each trial
	 */
	public void setTrialFeedback(List<TrialFeedback_Phase1> trialFeedback) {
		this.trialFeedback = trialFeedback;
	}

	/**
	 * Get the average probabilities score (S1).
	 * 
	 * @return the average probabilities score (S1)
	 */
	@XmlElement(name="ProbabilitiesScore_s1")
	public Double getProbabilitiesScore_s1() {
		return probabilitiesScore_s1;
	}

	/**
	 * Set the average probabilities score (S1).
	 * 
	 * @param probabilitiesScore_s1 the average probabilities score (S1)
	 */
	public void setProbabilitiesScore_s1(Double probabilitiesScore_s1) {
		this.probabilitiesScore_s1 = probabilitiesScore_s1;
	}

	/**
	 * Get the average troop allocation score (S2).
	 * 
	 * @return the average troop allocation score (S2)
	 */
	@XmlElement(name="TroopAllocationScore_s2")
	public Double getTroopAllocationScore_s2() {
		return troopAllocationScore_s2;
	}

	/**
	 * Set the average troop allocation score (S2).
	 * 
	 * @param troopAllocationScore_s2 the average troop allcoation score (S2)
	 */
	public void setTroopAllocationScore_s2(Double troopAllocationScore_s2) {
		this.troopAllocationScore_s2 = troopAllocationScore_s2;
	}
}