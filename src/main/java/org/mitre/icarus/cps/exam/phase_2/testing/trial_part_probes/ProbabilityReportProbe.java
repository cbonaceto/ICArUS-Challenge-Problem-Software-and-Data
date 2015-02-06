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
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.phase_2.testing.DatumIdentifier;
import org.mitre.icarus.cps.exam.phase_2.testing.IDatum;

/**
 * Base class for probability report probes.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="ProbabilityReportProbe", namespace="IcarusCPD_2")
@XmlSeeAlso({AttackProbabilityReportProbe.class})
public abstract class ProbabilityReportProbe<T extends Probability> extends TrialPartProbe implements IDatum {
	
	/** The datum ID of this probability report */
	protected String id;
	
	/** The data to consider when reporting the probability or probabilities */
	protected List<DatumIdentifier> datumList;	
	
	/** The probabilities */
	protected List<T> probabilities;
	
	/** The target sum of the probabilities */
	protected Double targetSum;
	
	/** The normalization constraint with respect to the target sum */
	protected NormalizationConstraintType normalizationConstraint;
	
	/** The order in which each probability was adjusted by the subject (only captures the first time each 
	 * probability was adjusted) */
	protected List<ItemAdjustment> probabilityAdjustmentSequence;
	
	/**
	 * Construct an empty ProbabilityReportProbe.
	 */
	public ProbabilityReportProbe() {}
	
	/**
	 * Construct a ProbabilityReportProbe with the given datum ID, datum list, and probabilities.
	 * 
	 * @param id the datum ID of this probability report
	 * @param datumList the datum to consider when reporting the probability or probabilities
	 * @param probabilities the probabilities
	 */
	public ProbabilityReportProbe(String id, List<DatumIdentifier> datumList, List<T> probabilities) {
		this.id = id;
		this.datumList = datumList;
		this.probabilities = probabilities;
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.IDatum#getId()
	 */
	@Override
	@XmlAttribute(name="id")
	public String getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.IDatum#setId(java.lang.String)
	 */
	@Override
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Get the datum to consider when reporting the probability or probabilities.
	 * 
	 * @return the datum to consider when reporting the probability or probabilities
	 */
	@XmlElement(name="Datum")
	public List<DatumIdentifier> getDatumList() {
		return datumList;
	}

	/**
	 * Set the datum to consider when reporting the probability or probabilities.
	 * 
	 * @param datumList the datum to consider when reporting the probability or probabilities
	 */
	public void setDatumList(List<DatumIdentifier> datumList) {
		this.datumList = datumList;
	}
	
	/**
	 * Get the probabilities.
	 * 
	 * @return the probabilities
	 */
	@XmlTransient
	public List<T> getProbabilities() {
		return probabilities;
	}

	/**
	 * Set the probabilities.
	 * 
	 * @param probabilities the probabilities
	 */
	public void setProbabilities(List<T> probabilities) {
		this.probabilities = probabilities;
	}

	/**
	 * Get the target sum of the probabilities (e.g., 100%).
	 * 
	 * @return the target sum of the probabilities (e.g., 100%)
	 */
	@XmlAttribute(name="targetSum")
	public Double getTargetSum() {
		return targetSum;
	}

	/**
	 * Set the target sum of the probabilities (e.g., 100%).
	 * 
	 * @param targetSum the target sum of the probabilities (e.g., 100%)
	 */
	public void setTargetSum(Double targetSum) {
		this.targetSum = targetSum;
	}

	/**
	 * Get the normalization constraint with respect to the target sum (e.g., <=, =).
	 * 
	 * @return the normalization constraint with respect to the target sum (e.g., <=, =)
	 */
	@XmlAttribute(name="normalizationConstraint")
	public NormalizationConstraintType getNormalizationConstraint() {
		return normalizationConstraint;
	}

	/**
	 * Set the normalization constraint with respect to the target sum (e.g., <=, =).
	 * 
	 * @param normalizationConstraint the normalization constraint with respect to the target sum (e.g., <=, =)
	 */
	public void setNormalizationConstraint(NormalizationConstraintType normalizationConstraint) {
		this.normalizationConstraint = normalizationConstraint;
	}	
	
	/**
	 * Get the order in which each probability was adjusted by the subject (only captures the first time each 
	 * probability was adjusted).
	 * FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @return the order in which each probability was adjusted by the subject
	 */
	@XmlElement(name="AdjustmentSequence")
	public List<ItemAdjustment> getProbabilityAdjustmentSequence() {
		return probabilityAdjustmentSequence;
	}

	/**
	 * Set the order in which each probability was adjusted by the subject (only captures the first time each 
	 * probability was adjusted).	 * 
	 * FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @param probabilityAdjustmentSequence the order in which each probability was adjusted by the subject
	 */
	public void setProbabilityAdjustmentSequence(
			List<ItemAdjustment> probabilityAdjustmentSequence) {
		this.probabilityAdjustmentSequence = probabilityAdjustmentSequence;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbe#isResponsePresent()
	 */
	@Override
	public boolean isResponsePresent() {
		if((dataProvidedToParticipant == null || !dataProvidedToParticipant)
				&& probabilities != null && !probabilities.isEmpty()) {
			for(T probability : probabilities) {
				if(probability.getProbability() == null &&
					probability.getRawProbability() == null) {
					return false;
				}
			}
			return true;
			
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbe#copyAdditionalResponseData(org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbe)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	protected void copyAdditionalResponseData(TrialPartProbe trialPart) {
		if(trialPart != null && trialPart instanceof ProbabilityReportProbe) {			
			ProbabilityReportProbe probe = (ProbabilityReportProbe)trialPart;
			if(probabilities != null && !probabilities.isEmpty() && 
					probe.probabilities !=null && !probe.probabilities.isEmpty()) {
				int numProbs = Math.min(probabilities.size(), probe.probabilities.size());
				for(int i=0; i<numProbs; i++) {
					Probability trialProbability = probabilities.get(i);
					Probability probeProbability = (Probability)probe.probabilities.get(i);
					trialProbability.setProbability(probeProbability.getProbability());
					trialProbability.setRawProbability(probeProbability.getRawProbability());
					trialProbability.setTime_ms(probeProbability.getTime_ms());
				}
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbe#clearAdditionalResponseData()
	 */
	@Override
	protected void clearAdditionalResponseData() {
		if((dataProvidedToParticipant == null || !dataProvidedToParticipant) && 
				probabilities != null && !probabilities.isEmpty()) {
			for(T probability : probabilities) {
				probability.setProbability(null);
				probability.setRawProbability(null);
				probability.setTime_ms(null);
			}
		}
	}
}