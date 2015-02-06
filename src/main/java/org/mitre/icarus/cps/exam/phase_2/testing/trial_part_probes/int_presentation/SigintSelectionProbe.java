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
package org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.int_presentation;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbeType;

/**
 * A probe to choose which location(s) to obtain SIGINT at.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="SigintSelectionProbe", namespace="IcarusCPD_2")
public class SigintSelectionProbe extends SigintPresentationProbe {
	
	/** The number of locations that SIGINT can be obtained from */
	protected Integer numSigintSelections;
	
	/** The ID(s) of the location(s) selected to obtain SIGINT at */
	protected List<String> selectedLocationIds;
	
	/** The normative location(s) to obtain SIGINT at based on the participant's Red attack probabilities */
	protected List<String> normativeParticipantLocationIds;
	
	/** The normative location(s) to obtain SIGINT at based on the Bayesian Red attack probabilities */
	protected List<String> normativeBayesianLocationIds;
	
	/**
	 * Construct an empty SIGINT selection probe. 
	 */
	public SigintSelectionProbe() {
		type = TrialPartProbeType.SigintSelection;
	}
	
	/**
	 * Construct a SIGINT selection probe with the given location IDs, indexes, and number of locations 
	 * to select SIGINT at.
	 * 
	 * @param locationIds
	 * @param locationIndexes
	 * @param numSigintSelections
	 */
	public SigintSelectionProbe(List<String> locationIds, 
			List<Integer> locationIndexes, Integer numSigintSelections) {
		super(locationIds, locationIndexes);
		type = TrialPartProbeType.SigintSelection;
		this.numSigintSelections = numSigintSelections;
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbe#getType()
	 */
	@Override
	public TrialPartProbeType getType() {
		return TrialPartProbeType.SigintSelection;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbe#setType(org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbeType)
	 */
	@Override
	public void setType(TrialPartProbeType type) {
		if(type != TrialPartProbeType.SigintSelection) {
			throw new IllegalArgumentException("SigintSelectionProbe type must be SigintSelection.");
		}
		this.type = type;
	}

	/**
	 * Get the number of locations to obtain SIGINT at.
	 * 
	 * @return the number of locations to obtain SIGINT at
	 */
	@XmlAttribute(name="numSigintSelections")
	public Integer getNumSigintSelections() {
		return numSigintSelections;
	}

	/**
	 * Set the number of locations to obtain SIGINT at.
	 * 
	 * @param numSigintSelections the number of locations to obtain SIGINT at
	 */
	public void setNumSigintSelections(Integer numSigintSelections) {
		this.numSigintSelections = numSigintSelections;
	}

	/**
	 * Get the ID(s) of the location(s) selected to obtain SIGINT at.
	 * Response data provided by the participant.
	 * 
	 * @return the ID(s) of the location(s) selected to obtain SIGINT at
	 */
	@XmlElement(name="SelectedLocationIds")
	@XmlList
	public List<String> getSelectedLocationIds() {
		return selectedLocationIds;
	}

	/**
	 * Set the ID(s) of the location(s) selected to obtain SIGINT at.
	 * Response data provided by the participant.
	 * 
	 * @param selectedLocationIds the ID(s) of the location(s) selected to obtain SIGINT at
	 */
	public void setSelectedLocationIds(List<String> selectedLocationIds) {
		this.selectedLocationIds = selectedLocationIds;
	}	
	
	/**
	 * Get the normative location(s) to obtain SIGINT at based on the participant's Red attack probabilities.
	 * Not provided by the participant.
	 * 
	 * @return the normative location(s) to obtain SIGINT at based on the participant's Red attack probabilities
	 */
	@XmlElement(name="NormativeParticipantLocationIds")
	@XmlList
	public List<String> getNormativeParticipantLocationIds() {
		return normativeParticipantLocationIds;
	}

	/**
	 * Set the normative location(s) to obtain SIGINT at based on the participant's Red attack probabilities.
	 * Not provided by the participant.
	 * 
	 * @param normativeParticipantLocationIds the normative location(s) to obtain SIGINT at based on the participant's Red attack probabilities
	 */
	public void setNormativeParticipantLocationIds(
			List<String> normativeParticipantLocationIds) {
		this.normativeParticipantLocationIds = normativeParticipantLocationIds;
	}

	/**
	 * Get the normative location(s) to obtain SIGINT at based on the Bayesian Red attack probabilities.
	 * Not provided by the participant.
	 * 
	 * @return the normative location(s) to obtain SIGINT at based on the Bayesian Red attack probabilities
	 */
	@XmlElement(name="NormativeBayesianLocationIds")
	@XmlList
	public List<String> getNormativeBayesianLocationIds() {
		return normativeBayesianLocationIds;
	}

	/**
	 * Set the normative location(s) to obtain SIGINT at based on the Bayesian Red attack probabilities.
	 * Not provided by the participant.
	 * 
	 * @param normativeBayesianLocationIds the normative location(s) to obtain SIGINT at based on the Bayesian Red attack probabilities
	 */
	public void setNormativeBayesianLocationIds(
			List<String> normativeBayesianLocationIds) {
		this.normativeBayesianLocationIds = normativeBayesianLocationIds;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.int_presentation.IntPresentationProbe#isResponsePresent()
	 */
	@Override
	public boolean isResponsePresent() {
		return (selectedLocationIds != null && !selectedLocationIds.isEmpty());
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.int_presentation.IntPresentationProbe#copyAdditionalResponseData(org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbe)
	 */
	@Override
	protected void copyAdditionalResponseData(TrialPartProbe trialPart) {
		if(trialPart != null && trialPart instanceof SigintSelectionProbe) {
			selectedLocationIds = ((SigintSelectionProbe)trialPart).selectedLocationIds;
		}
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.int_presentation.IntPresentationProbe#clearAdditionalResponseData()
	 */
	@Override
	protected void clearAdditionalResponseData() {
		if((dataProvidedToParticipant == null || !dataProvidedToParticipant)) {
			selectedLocationIds = null;
		}
	}	
}