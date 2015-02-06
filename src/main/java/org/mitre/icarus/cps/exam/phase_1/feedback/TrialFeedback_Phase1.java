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

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.base.feedback.TestTrialFeedback;
import org.mitre.icarus.cps.exam.phase_1.loader.IcarusExamLoader_Phase1;
import org.mitre.icarus.cps.exam.phase_1.testing.GroundTruth;
import org.mitre.icarus.cps.exam.phase_1.testing.IcarusTestTrial_Phase1;

/**
 * Contains feedback provided by the test harness to an ICArUS system after submitting 
 * its response to a trial in a task in an exam (e.g., a Task 3 trial). 
 * 
 * @author CBONACETO
 *
 */
@XmlRootElement(name="TrialFeedback_CPD1", namespace="IcarusCPD_1")
@XmlType(name="TrialFeedback_CPD1", namespace="IcarusCPD_1")
public class TrialFeedback_Phase1 extends TestTrialFeedback<IcarusTestTrial_Phase1> {
	
	/** The number of credits used on the trial (for Task 7 only) */
	protected Double numCreditsUsed;
	
	/** The number of credits awarded on the trial (for Task 7 only) */
	protected Double numCreditsAwarded;	
	
	/** The number of credits remaining (for Task 7 only) */
	protected Double numCreditsRemaining;
	
	/** Score on accuracy of group or location probabilities */
	protected Double probabilitiesScore_s1;
	
	/** Score on effectiveness of troop allocation percentages */
	protected Double troopAllocationScore_s2;
	
	/** Ground truth information for the trial */
	protected GroundTruth groundTruth;
	
	/** Whether the ground truth surprise response was present and well formed */
	protected Boolean groundTruthSurpriseWellFormed;	
		
	/**
	 * Get the number of credits used on the trial.
	 * 
	 * @return the number of credits used
	 */
	@XmlElement(name="NumCreditsUsed")
	public Double getNumCreditsUsed() {
		return numCreditsUsed;
	}

	/**
	 * Set the number of credits used on the trial.
	 * 
	 * @param numCreditsUsed the number of credits used
	 */
	public void setNumCreditsUsed(Double numCreditsUsed) {
		this.numCreditsUsed = numCreditsUsed;		
	}

	/**
	 * Get the number of credits awarded on the trial.
	 * 
	 * @return the number of credits awarded
	 */
	@XmlElement(name="NumCreditsAwarded")
	public Double getNumCreditsAwarded() {
		return numCreditsAwarded;
	}

	/**
	 * Set the number of credits awarded on the trial.
	 * 
	 * @param numCreditsAwarded the number of credits awarded
	 */
	public void setNumCreditsAwarded(Double numCreditsAwarded) {
		this.numCreditsAwarded = numCreditsAwarded;
	}

	/**
	 * Get the number of credits remaining.
	 * 
	 * @return the number of credits remaining
	 */
	@XmlElement(name="NumCreditsRemaining")
	public Double getNumCreditsRemaining() {
		return numCreditsRemaining;
	}

	/**
	 * Set the number of credits remaining.
	 * 
	 * @param numCreditsRemaining the number of credits remaining.
	 */
	public void setNumCreditsRemaining(Double numCreditsRemaining) {
		this.numCreditsRemaining = numCreditsRemaining;
		this.getPhaseId();
	}

	/**
	 * Get the score for assessing probabilities (s1).
	 * 
	 * @return the score
	 */
	@XmlElement(name="ProbabilitiesScore_s1")
	public Double getProbabilitiesScore_s1() {
		return probabilitiesScore_s1;
	}

	/**
	 * Set the score for assessing probabilities (s1).
	 * 
	 * @param probabilitiesScore_s1 the score
	 */
	public void setProbabilitiesScore_s1(Double probabilitiesScore_s1) {
		this.probabilitiesScore_s1 = probabilitiesScore_s1;
	}

	/**
	 * Get the score for allocating troops (s2).
	 * 
	 * @return the score
	 */
	@XmlElement(name="TroopAllocationScore_s2")
	public Double getTroopAllocationScore_s2() {
		return troopAllocationScore_s2;
	}

	/**
	 * Set the score for allocating troops (s2).
	 * 
	 * @param troopAllocationScore_s2 the score
	 */
	public void setTroopAllocationScore_s2(Double troopAllocationScore_s2) {
		this.troopAllocationScore_s2 = troopAllocationScore_s2;
	}
	
	/**
	 * Get the ground truth information for the trial.
	 * 
	 * @return the ground truth information
	 */
	@XmlElement(name = "GroundTruth")
	public GroundTruth getGroundTruth() {
		return groundTruth;
	}

	/**
	 * Set the ground truth information for the trial.
	 * 
	 * @param groundTruth the ground truth information
	 */
	public void setGroundTruth(GroundTruth groundTruth) {
		this.groundTruth = groundTruth;
	}	

	/**
	 * Get whether the ground truth surprise response was present and well formed.
	 * 
	 * @return whether the ground truth surprise response was present and well formed
	 */
	@XmlElement(name="GroundTruthSurpriseWellFormed")
	public Boolean getGroundTruthSurpriseWellFormed() {
		return groundTruthSurpriseWellFormed;
	}

	/**
	 * Set whether the ground truth surprise response was present and well formed.
	 * 
	 * @param groundTruthSurpriseWellFormed whether the ground truth surprise response was present and well formed
	 */
	public void setGroundTruthSurpriseWellFormed(Boolean groundTruthSurpriseWellFormed) {
		this.groundTruthSurpriseWellFormed = groundTruthSurpriseWellFormed;
	}

	/**
	 * Test main.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		TrialFeedback_Phase1 feedback = new TrialFeedback_Phase1();
		feedback.setExamId("Sample_Exam");
		feedback.setPhaseId("Task_7_Phase");
		feedback.setTrialNum(1);
		feedback.setResponseWellFormed(true);
		feedback.setErrors(null);
		feedback.setResponseGeneratorId("team1");
		feedback.setNumCreditsRemaining(9.2d);
		feedback.setNumCreditsAwarded(0.2d);
		feedback.setProbabilitiesScore_s1(78d);
		feedback.setTroopAllocationScore_s2(93d);
		
		try {
			System.out.println(IcarusExamLoader_Phase1.marshalTrialFeedback(feedback));
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
}