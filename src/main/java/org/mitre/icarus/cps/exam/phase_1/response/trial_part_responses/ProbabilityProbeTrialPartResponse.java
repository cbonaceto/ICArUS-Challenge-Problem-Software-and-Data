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
package org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.phase_1.response.Task_1_ProbeTrialResponse;
import org.mitre.icarus.cps.exam.phase_1.response.Task_2_ProbeTrialResponse;
import org.mitre.icarus.cps.exam.phase_1.response.Task_3_ProbeTrialResponse;

/**
 * Abstract base class for responses to a group probe or location probe trial part
 * (AttackLocationProbeResponse_MultiGroup and AttackLocationProbeResponse_MultiLocation).
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="ProbabilityProbeTrialPartResponse", namespace="IcarusCPD_1")
@XmlSeeAlso({Task_1_ProbeTrialResponse.class, Task_2_ProbeTrialResponse.class, Task_3_ProbeTrialResponse.class})
public abstract class ProbabilityProbeTrialPartResponse extends TrialPartResponse {
	
	/** Score on accuracy of group or location probabilities */
	protected Double probabilitiesScore_s1;
	
	/** The normative probabilities for the group or location probe using the cumulative Bayesian computation */
	protected ArrayList<Double> normativeProbs_cumulativeBayesian;
	
	/** The normative probabilities for the group or location probe using the incremental Bayesian computation */
	protected ArrayList<Double> normativeProbs_incrementalBayesian;

	@XmlElement(name="ProbabilitiesScore_s1")
	public Double getProbabilitiesScore_s1() {
		return probabilitiesScore_s1;
	}

	public void setProbabilitiesScore_s1(Double probabilitiesScore_s1) {
		this.probabilitiesScore_s1 = probabilitiesScore_s1;
	}

	@XmlElement(name="NormativeProbsCumulative")
	@XmlList
	public ArrayList<Double> getNormativeProbs_cumulativeBayesian() {
		return normativeProbs_cumulativeBayesian;
	}

	public void setNormativeProbs_cumulativeBayesian(ArrayList<Double> normativeProbs_cumulativeBayesian) {
		this.normativeProbs_cumulativeBayesian = normativeProbs_cumulativeBayesian;
	}

	@XmlElement(name="NormativeProbsIncremental")
	@XmlList
	public ArrayList<Double> getNormativeProbs_incrementalBayesian() {
		return normativeProbs_incrementalBayesian;
	}

	public void setNormativeProbs_incrementalBayesian(ArrayList<Double> normativeProbs_incrementalBayesian) {
		this.normativeProbs_incrementalBayesian = normativeProbs_incrementalBayesian;
	}	
}