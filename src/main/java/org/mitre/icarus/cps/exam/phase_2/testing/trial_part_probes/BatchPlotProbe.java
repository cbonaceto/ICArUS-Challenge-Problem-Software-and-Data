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
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.phase_2.testing.DatumType;
import org.mitre.icarus.cps.exam.phase_2.testing.IDatum;
import org.mitre.icarus.cps.feature_vector.phase_2.BlueLocation;

/**
 * A probe to create a batch plot of previous Red attacks in Missions 4-6. 
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="BatchPlotProbe", namespace="IcarusCPD_2")
public class BatchPlotProbe extends TrialPartProbe implements IDatum {
	
	/** Batch plot probe button types */
	@XmlType(name="BatchPlotProbeButtonType", namespace="IcarusCPD_2")
	public static enum BatchPlotProbeButtonType {Backward, Forward}
	
	/** The datum ID of this batch plot probe */
	protected String id;
	
	/** The trial numbers of the previous trials to review */
	protected List<Integer> previousTrials;	
	
	/** The number of Blue locations per trial */
	protected Integer blueLocationsPerTrial;
	
	/** The Blue locations for each previous trial to be reviewed */
	protected List<BlueLocation> blueLocations;	
	
	/** The number of previous trials the participant chose to review */
	protected Integer numPreviousTrialsSelected;
	
	/** The IDs of locations the participant clicked on to obtain more information */
	protected List<String> blueLocationsClicked;
	
	/** The sequence of "Backward" and "Forward" button presses for the participant */
	protected List<BatchPlotProbeButtonType> buttonPressSequence;
	
	/**
	 * Construct an empty BatchPlotProbe.
	 */
	public BatchPlotProbe() {
		type = TrialPartProbeType.BatchPlotProbe;
	}
	
	/**
	 * Construct a BatchPlotProbe with the given previous trials to review, number of Blue locations per trial, and
	 * whether creating the batch plot is optional.
	 * 
	 * @param previousTrials the trial numbers of the previous trials to review Red attacks on
	 * @param blueLocationsPerTrial the number of Blue locations per trial
	 * @param optional Whether creating the batch plot is optional. Typically true.
	 */
	public BatchPlotProbe(List<Integer> previousTrials, 
			Integer blueLocationsPerTrial, Boolean optional) {
		type = TrialPartProbeType.BatchPlotProbe;
		this.previousTrials = previousTrials;
		this.blueLocationsPerTrial = blueLocationsPerTrial;
		this.optional = optional;
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbe#getType()
	 */
	@Override
	public TrialPartProbeType getType() {
		return TrialPartProbeType.BatchPlotProbe;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbe#setType(org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbeType)
	 */
	@Override
	public void setType(TrialPartProbeType type) {
		if(type != TrialPartProbeType.BatchPlotProbe) {
			throw new IllegalArgumentException("BatchPlotProbe type must be BatchPlotProbe.");
		}
		this.type = type;
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.IDatum#getId()
	 */
	@XmlAttribute(name="id")
	@Override
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
	 * Get the trial numbers of the previous trials to review Red attacks on.
	 * 
	 * @return the trial numbers of the previous trials to review Red attacks on
	 */
	@XmlElement(name="PreviousTrials")
	@XmlList
	public List<Integer> getPreviousTrials() {
		return previousTrials;
	}

	/**
	 * Set the trial numbers of the previous trials to review Red attacks on.
	 * 
	 * @param previousTrials the trial numbers of the previous trials to review Red attacks on
	 */
	public void setPreviousTrials(List<Integer> previousTrials) {
		this.previousTrials = previousTrials;
	}

	/**
	 * Get the number of Blue locations per trial.
	 * 
	 * @return the number of Blue locations per trial
	 */
	@XmlAttribute(name="blueLocationsPerTrial")
	public Integer getBlueLocationsPerTrial() {
		return blueLocationsPerTrial;
	}

	/**
	 * Set the number of Blue locations per trial.
	 * 
	 * @param blueLocationsPerTrial the number of Blue locations per trial
	 */
	public void setBlueLocationsPerTrial(Integer blueLocationsPerTrial) {
		this.blueLocationsPerTrial = blueLocationsPerTrial;
	}

	/**
	 * Get the Blue locations for each previous trial to be reviewed.
	 * 
	 * @return the Blue locations for each previous trial to be reviewed
	 */
	@XmlTransient
	public List<BlueLocation> getBlueLocations() {
		return blueLocations;
	}

	/**
	 * Set the Blue locations for each previous trial to be reviewed.
	 * 
	 * @param blueLocations the Blue locations for each previous trial to be reviewed
	 */
	public void setBlueLocations(List<BlueLocation> blueLocations) {
		this.blueLocations = blueLocations;
	}

	/**
	 * Get the number of previous trials the participant chose to review.
	 * Response data provided by the participant.
	 * 
	 * @return the number of previous trials the participant chose to review
	 */
	@XmlAttribute(name="numPreviousTrialsSelected")
	public Integer getNumPreviousTrialsSelected() {
		return numPreviousTrialsSelected;
	}

	/**
	 * Set the number of previous trials the participant chose to review. 
	 * Response data provided by the participant.
	 * 
	 * @param numPreviousTrialsSelected the number of previous trials the participant chose to review
	 */
	public void setNumPreviousTrialsSelected(Integer numPreviousTrialsSelected) {
		this.numPreviousTrialsSelected = numPreviousTrialsSelected;
	}

	/**
	 * Get the IDs of locations the participant clicked on to obtain more information.
	 * FOR HUMAN SUBJECT USE ONLY
	 * 
	 * @return the IDs of locations the participant clicked on to obtain more information
	 */
	@XmlElement(name="BlueLocationsClicked")
	@XmlList
	public List<String> getBlueLocationsClicked() {
		return blueLocationsClicked;
	}

	/**
	 * Set the IDs of locations the participant clicked on to obtain more information. 
	 * FOR HUMAN SUBJECT USE ONLY
	 * 
	 * @param blueLocationsClicked the IDs of locations the participant clicked on to obtain more information
	 */
	public void setBlueLocationsClicked(List<String> blueLocationsClicked) {
		this.blueLocationsClicked = blueLocationsClicked;
	}

	/**
	 * Get the sequence of "Backward" and "Forward" button presses for the participant.
	 * FOR HUMAN SUBJECT USE ONLY
	 * 
	 * @return the sequence of "Backward" and "Forward" button presses for the participant
	 */
	@XmlElement(name="ButtonPressSequence")
	@XmlList
	public List<BatchPlotProbeButtonType> getButtonPressSequence() {
		return buttonPressSequence;
	}

	/**
	 * Set the sequence of "Backward" and "Forward" button presses for the participant.
	 * FOR HUMAN SUBJECT USE ONLY
	 * 
	 * @param buttonPressSequence the sequence of "Backward" and "Forward" button presses for the participant
	 */
	public void setButtonPressSequence(List<BatchPlotProbeButtonType> buttonPressSequence) {
		this.buttonPressSequence = buttonPressSequence;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbe#isResponsePresent()
	 */
	@Override
	public boolean isResponsePresent() {
		return (dataProvidedToParticipant == null || !dataProvidedToParticipant) 
				&& numPreviousTrialsSelected != null;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbe#copyAdditionalResponseData(org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbe)
	 */
	@Override
	protected void copyAdditionalResponseData(TrialPartProbe trialPart) {
		if(trialPart != null && trialPart instanceof BatchPlotProbe) {			
			numPreviousTrialsSelected = ((BatchPlotProbe)trialPart).numPreviousTrialsSelected;
		}	
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbe#clearAdditionalResponseData()
	 */
	@Override
	protected void clearAdditionalResponseData() {
		if(dataProvidedToParticipant == null || !dataProvidedToParticipant) {
			numPreviousTrialsSelected = null;
		}
	}	

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.IDatum#getDatumType()
	 */
	@Override
	public DatumType getDatumType() {
		return DatumType.BatchPlots;
	}
}