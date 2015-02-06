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
package org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlType;

/**
 * A probe to correct or confirm a normalized set of probabilities.
 * NO LONGER USED, WILL BE REMOVED.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="NormalizationProbe", namespace="IcarusCPD_2")
public class NormalizationProbe extends TrialPartProbe {
	
	/** The datum ID of each probability report containing the probabilities of each hypothesis being summed */
	protected List<String> probabilityReportIds;	
	
	/** The target sum of the probabilities of each hypothesis */
	protected Double sum;
	
	/** The normalization constraint with respect to the target sum */
	protected NormalizationConstraintType normalizationConstraint;
	
	public NormalizationProbe() {}
	
	public NormalizationProbe(List<String> probabilityReportIds, Double sum, 
			NormalizationConstraintType normalizationConstraint) {
		this.probabilityReportIds = probabilityReportIds;
		this.sum = sum;
		this.normalizationConstraint = normalizationConstraint;
	}

	@XmlElement(name="ProbabilityReportDatumIds")
	@XmlList
	public List<String> getProbabilityReportIds() {
		return probabilityReportIds;
	}

	public void setProbabilityReportIds(List<String> probabilityReportIds) {
		this.probabilityReportIds = probabilityReportIds;
	}

	@XmlAttribute(name="sum")
	public Double getSum() {
		return sum;
	}

	public void setSum(Double sum) {
		this.sum = sum;
	}

	@XmlAttribute(name="normalizationConstraint")
	public NormalizationConstraintType getNormalizationConstraint() {
		return normalizationConstraint;
	}

	public void setNormalizationConstraint(NormalizationConstraintType normalizationConstraint) {
		this.normalizationConstraint = normalizationConstraint;
	}
	
	@Override
	public boolean isResponsePresent() {
		//Always returns false, no response data
		return false;
	}
	
	@Override
	protected void copyAdditionalResponseData(TrialPartProbe trialPart) {
		//Does nothing, no response data
	}

	@Override
	public void clearAdditionalResponseData() {
		//Does nothing, no response data
	}
}