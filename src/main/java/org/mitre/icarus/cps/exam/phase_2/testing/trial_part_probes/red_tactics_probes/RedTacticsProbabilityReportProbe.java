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
package org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.red_tactics_probes;


import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.phase_2.testing.DatumIdentifier;
import org.mitre.icarus.cps.exam.phase_2.testing.DatumType;
import org.mitre.icarus.cps.exam.phase_2.testing.IDatum;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.ItemAdjustment;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.NormalizationConstraintType;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.Probability;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbeType;

/**
 * A probe where the participant estimates the probability that Red is playing with each tactic (style).
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="RedTacticProbabilityReportProbe", namespace="IcarusCPD_2",
	propOrder={"id", "datumList", "probabilityAdjustmentSequence", "probabilities", 
		"targetSum", "normalizationConstraint"})
public class RedTacticsProbabilityReportProbe extends AbstractRedTacticsProbe implements IDatum {
	
	/** The datum ID of this Red tactics probability report */
	protected String id;
	
	/** The datum to consider when reporting the probability or probabilities */
	protected List<DatumIdentifier> datumList;
	
	/** The probabilities */
	protected List<RedTacticProbability> probabilities;
	
	/** The target sum of the probabilities */
	protected Double targetSum;
	
	/** The normalization constraint with respect to the target sum */
	protected NormalizationConstraintType normalizationConstraint;
	
	/** The order in which each probability was adjusted by the subject (only captures the first time each 
	 * probability was adjusted) */
	protected List<ItemAdjustment> probabilityAdjustmentSequence;
	
	/**
	 * Construct an empty RedTacticsProbabilityReportProbe. 
	 */
	public RedTacticsProbabilityReportProbe() {
		type = TrialPartProbeType.RedTacticsProbabilityReport;
	}
	
	/**
	 * Construct a RedTacticsProbabilityReportProbe with the given datum list and probabilities.
	 * 
	 * @param datumList the datum to consider when reporting the probability or probabilities
	 * @param probabilities the probabilities
	 */
	public RedTacticsProbabilityReportProbe(List<DatumIdentifier> datumList, 
			List<RedTacticProbability> probabilities) {
		this(null, datumList, probabilities);
	}
	
	/**
	 * Construct a RedTacticsProbabilityReportProbe with the given datum ID, datum list, and probabilities.
	 * 
	 * @param id the datum ID of this Red tactics probability report
	 * @param datumList the datum to consider when reporting the probability or probabilities
	 * @param probabilities the probabilities
	 */
	public RedTacticsProbabilityReportProbe(String id, List<DatumIdentifier> datumList, 
			List<RedTacticProbability> probabilities) {
		type = TrialPartProbeType.RedTacticsProbabilityReport;
		this.id = id;
		this.datumList = datumList;
		this.probabilities = probabilities;
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbe#getType()
	 */
	@Override
	public TrialPartProbeType getType() {
		return TrialPartProbeType.RedTacticsProbabilityReport;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbe#setType(org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbeType)
	 */
	@Override
	public void setType(TrialPartProbeType type) {
		if(type != TrialPartProbeType.RedTacticsProbabilityReport) {
			throw new IllegalArgumentException("RedTacticsProbabilityReportProbe type must be RedTacticsProbabilityReport.");
		}
		this.type = type;
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
	@XmlElement(name="Probability")
	public List<RedTacticProbability> getProbabilities() {
		return probabilities;
	}

	/**
	 * Set the probabilities.
	 * 
	 * @param probabilities the probabilities
	 */
	public void setProbabilities(List<RedTacticProbability> probabilities) {
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
			for(RedTacticProbability probability : probabilities) {
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
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.red_tactics_probes.AbstractRedTacticsProbe#copyAdditionalResponseData(org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbe)
	 */
	@Override
	protected void copyAdditionalResponseData(TrialPartProbe trialPart) {		
		super.copyAdditionalResponseData(trialPart);
		if(trialPart != null && trialPart instanceof RedTacticsProbabilityReportProbe) {			
			RedTacticsProbabilityReportProbe probe = (RedTacticsProbabilityReportProbe)trialPart;
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
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.red_tactics_probes.AbstractRedTacticsProbe#clearAdditionalResponseData()
	 */
	@Override
	protected void clearAdditionalResponseData() {
		super.clearAdditionalResponseData();
		if((dataProvidedToParticipant == null || !dataProvidedToParticipant)
				&& probabilities != null && !probabilities.isEmpty()) {
			for(RedTacticProbability probability : probabilities) {
				probability.setProbability(null);
				probability.setRawProbability(null);
				probability.setTime_ms(null);
			}
		}
	}	

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.IDatum#getDatumType()
	 */
	@Override
	public DatumType getDatumType() {
		return DatumType.RedTacticProbabilityReport;
	}		
}