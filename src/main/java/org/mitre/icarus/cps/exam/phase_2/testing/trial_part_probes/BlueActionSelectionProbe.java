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
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.phase_2.testing.DatumIdentifier;

/**
 * A probe to select a Blue action (or see a Blue action in Mission 1) at one or more locations.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="BlueActionSelectionProbe", namespace="IcarusCPD_2",
	propOrder = {"datumList", "blueActions", "expectedUtility", 
		"normativeParticipantBlueActions", "normativeParticipantExpectedUtility",
		"normativeBayesianBlueActions", "normativeBayesianExpectedUtility"})
public class BlueActionSelectionProbe extends TrialPartProbe {
	
	/** The datum to consider when choosing a Blue action */
	protected List<DatumIdentifier> datumList;
	
	/** The Blue action selections at each location */
	protected List<BlueAction> blueActions;
	
	/** The expected utility of the Blue action selections */
	protected Double expectedUtility;
	
	/** The normative Blue actions at each location based on the participant attack probabilities */
	protected List<BlueAction> normativeParticipantBlueActions;
	
	/** The expected utility of the normative Blue action selections based on the participant attack probabilities */
	protected Double normativeParticipantExpectedUtility;
	
	/** The normative Blue actions at each location based on the normative attack probabilities */
	protected List<BlueAction> normativeBayesianBlueActions;
	
	/** The expected utility of the normative Blue action selections based on the normative attack probabilities */
	protected Double normativeBayesianExpectedUtility;
	
	/**
	 * Construct an empty BlueActionSelectionProbe.
	 */
	public BlueActionSelectionProbe() {
		type = TrialPartProbeType.BlueActionSelection;
	}	
	
	/**
	 * Construct a BlueActionSelectionProbe with the given Blue action selections at each location. 
	 * 
	 * @param blueActions the Blue action selections at each location
	 */
	public BlueActionSelectionProbe(List<BlueAction> blueActions) {
		type = TrialPartProbeType.BlueActionSelection;
		this.blueActions = blueActions;
	}	
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbe#getType()
	 */
	@Override
	public TrialPartProbeType getType() {
		return TrialPartProbeType.BlueActionSelection;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbe#setType(org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbeType)
	 */
	@Override
	public void setType(TrialPartProbeType type) {
		if(type != TrialPartProbeType.BlueActionSelection) {
			throw new IllegalArgumentException("BlueActionSelectionProbe type must be BlueActionSelection.");
		}
		this.type = type;
	}

	/**
	 * Get the datum to consider when choosing a Blue action.
	 * 
	 * @return the datum to consider when choosing a Blue action
	 */
	@XmlElement(name="Datum")
	public List<DatumIdentifier> getDatumList() {
		return datumList;
	}

	/**
	 * Set the datum to consider when choosing a Blue action.
	 * 
	 * @param datumList the datum to consider when choosing a Blue action
	 */
	public void setDatumList(List<DatumIdentifier> datumList) {
		this.datumList = datumList;
	}
	
	/**
	 * Get the Blue action selections at each location.
	 * 
	 * @return the Blue action selections at each location
	 */
	@XmlElement(name="BlueAction")
	public List<BlueAction> getBlueActions() {
		return blueActions;
	}

	/**
	 * Set the Blue action selections at each location.
	 * 
	 * @param blueActions the Blue action selections at each location
	 */
	public void setBlueActions(List<BlueAction> blueActions) {
		this.blueActions = blueActions;
	}	
	
	/**
	 * Get the expected utility of the participant Blue action selections.
	 * 
	 * @return the expected utility of the participant Blue action selections
	 */
	@XmlAttribute(name="expectedUtility")
	public Double getExpectedUtility() {
		return expectedUtility;
	}

	/**
	 * Set the expected utility of the participant Blue action selections.
	 * 
	 * @param blueActionsExpectedUtility the expected utility of the participant Blue action selections
	 */
	public void setExpectedUtility(Double expectedUtility) {
		this.expectedUtility = expectedUtility;
	}	

	/**
	 * Get the normative Blue actions at each location based on the normative participant attack probabilities.
	 * This data is not provided by the participant.
	 * 
	 * @return the normative participant Blue actions at each location
	 */
	@XmlElement(name="NormativeParticipantBlueAction")
	public List<BlueAction> getNormativeParticipantBlueActions() {
		return normativeParticipantBlueActions;
	}

	/**
	 * Set the normative Blue actions at each location based on the normative participant attack probabilities.
	 * This data is not provided by the participant.
	 * 
	 * @param normativeParticipantBlueActions the normative participant Blue actions at each location
	 */
	public void setNormativeParticipantBlueActions(List<BlueAction> normativeParticipantBlueActions) {
		this.normativeParticipantBlueActions = normativeParticipantBlueActions;
	}

	/**
	 * Get the expected utility of the normative participant Blue actions.
	 * 
	 * @return the expected utility of the normative participant Blue actions.
	 */
	@XmlAttribute(name="normativeParticipantExpectedUtility")
	public Double getNormativeParticipantExpectedUtility() {
		return normativeParticipantExpectedUtility;
	}

	/**
	 * Set the expected utility of the normative participant Blue actions.
	 * 
	 * @param normativeParticipantExpectedUtility the expected utility of the normative participant Blue actions.
	 */
	public void setNormativeParticipantExpectedUtility(Double normativeParticipantExpectedUtility) {
		this.normativeParticipantExpectedUtility = normativeParticipantExpectedUtility;
	}

	/**
	 * Get the normative Blue actions at each location based on the normative Bayesian attack probabilities.
	 * This data is not provided by the participant.
	 * 
	 * @return the normative Bayesian Blue actions at each location
	 */
	@XmlElement(name="NormativeBayesianBlueAction")
	public List<BlueAction> getNormativeBayesianBlueActions() {
		return normativeBayesianBlueActions;
	}

	/**
	 * Set the normative Blue actions at each location based on the normative Bayesian attack probabilities.
	 * This data is not provided by the participant.
	 * 
	 * @param normativeBlueActions the normative Bayesian Blue actions at each location
	 */	
	public void setNormativeBayesianBlueActions(List<BlueAction> normativeBayesianBlueActions) {
		this.normativeBayesianBlueActions = normativeBayesianBlueActions;
	}
	
	/**
	 * Get the expected utility of the normative Bayesian Blue actions.
	 * 
	 * @return the expected utility of the normative Bayesian Blue actions.
	 */
	@XmlAttribute(name="normativeBayesianExpectedUtility")
	public Double getNormativeBayesianExpectedUtility() {
		return normativeBayesianExpectedUtility;
	}

	/**
	 * Set the expected utility of the normative Bayesian Blue actions.
	 * 
	 * @param normativeBlueActionsExpectedUtility the expected utility of the normative Blue actions.
	 */
	public void setNormativeBayesianExpectedUtility(Double normativeBayesianExpectedUtility) {
		this.normativeBayesianExpectedUtility = normativeBayesianExpectedUtility;
	}	

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbe#isResponsePresent()
	 */
	@Override
	public boolean isResponsePresent() {
		if((dataProvidedToParticipant == null || !dataProvidedToParticipant)
				&& blueActions != null && !blueActions.isEmpty()) {
			for(BlueAction blueAction : blueActions) {
				if(blueAction.getAction() == null) {
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
	@Override
	protected void copyAdditionalResponseData(TrialPartProbe trialPart) {
		//TODO: Fix this, don't replace all blue actions, just find the ones at each location and set the action
		if(trialPart != null && trialPart instanceof BlueActionSelectionProbe) {			
			blueActions = ((BlueActionSelectionProbe)trialPart).blueActions;
		}
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbe#clearAdditionalResponseData()
	 */
	@Override
	protected void clearAdditionalResponseData() {
		if((dataProvidedToParticipant == null || !dataProvidedToParticipant) &&
				blueActions != null && !blueActions.isEmpty()) {
			for(BlueAction blueAction : blueActions) {
				blueAction.setAction(null);
			}
		}
	}
}