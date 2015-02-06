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
package org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states;

import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.SurpriseReportProbeResponse;
import org.mitre.icarus.cps.exam.phase_1.testing.GroundTruth;
import org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.IntLayer;
import org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.SurpriseReportProbe;

/**
 * A trial part state to indicate surprise after seeing ground truth or an INT layer.
 * 
 * @author CBONACETO
 *
 */
public class SurpriseProbeTrialPartState extends TrialPartState_Phase1 {	
	
	/** The surprise probe */
	protected SurpriseReportProbe probe;
	
	/** The participant response to the surprise probe */
	protected SurpriseReportProbeResponse response;
	
	/** The average human response to the surprise probe (used for the player only) */
	protected SurpriseReportProbeResponse avgHumanResponse;	
	
	/** The INT layer to add (if any) */
	protected IntLayer layerToAdd;
	
	/** The ground truth information (if any) */
	protected GroundTruth groundTruth;
	
	public SurpriseProbeTrialPartState(int trialNumber, int trialPartNumber) {
		this(trialNumber, trialPartNumber, null, null);
	}
	
	public SurpriseProbeTrialPartState(int trialNumber, int trialPartNumber, SurpriseReportProbe probe) {
		this(trialNumber, trialPartNumber, probe, null);
	}
	
	public SurpriseProbeTrialPartState(int trialNumber, int trialPartNumber, SurpriseReportProbe probe, 
			GroundTruth groundTruth) {
		super(trialNumber, trialPartNumber);
		this.probe = probe;
		this.groundTruth = groundTruth;		
		this.response = new SurpriseReportProbeResponse();
		this.avgHumanResponse = new SurpriseReportProbeResponse();
	}

	/**
	 * Get the current surprise setting.
	 * 
	 * @return the current surprise setting.
	 */
	public Integer getCurrentSurprise() {
		return response.getSurpriseVal();
	}

	/**
	 * Set the current surprise setting.
	 * 
	 * @param currentSurprise the current surprise setting.
	 */
	public void setCurrentSurprise(Integer currentSurprise) {
		response.setSurpriseVal(currentSurprise);
	}
	
	/**
	 * Get the average human surprise setting (used for the player only).
	 * 
	 * @return
	 */
	public Integer getAvgHumanSurpsie() {
		return avgHumanResponse.getSurpriseVal();
	}
	
	/**
	 * Set the average human surprise setting (used for the player only).
	 * 
	 * @param avgHumanSurprise
	 */
	public void setAvgHumanSurprise(Integer avgHumanSurprise) {
		avgHumanResponse.setSurpriseVal(avgHumanSurprise);
	}

	/**
	 * Get the INT layer to add (if any) before showing the probability probe.
	 * 
	 * @return the INT layer to add
	 */
	public IntLayer getLayerToAdd() {
		return layerToAdd;
	}

	/**
	 * Set the INT layer to add (if any) before showing the probability probe.
	 * 
	 * @param layerToAdd the INT layer to add
	 */
	public void setLayerToAdd(IntLayer layerToAdd) {
		this.layerToAdd = layerToAdd;
	}

	/**
	 * Get the ground truth data if this is a ground truth surprise probe.
	 * 
	 * @return the ground truth data.
	 */
	public GroundTruth getGroundTruth() {
		return groundTruth;
	}

	/**
	 * Set the ground truth data if this is a ground truth surprise probe.
	 * 
	 * @param groundTruth the ground truth data
	 */
	public void setGroundTruth(GroundTruth groundTruth) {
		this.groundTruth = groundTruth;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment.phase_1.trial_states.trial_part_states.TrialPartState#getProbe()
	 */
	@Override
	public SurpriseReportProbe getProbe() {
		return probe;
	}

	public void setProbe(SurpriseReportProbe probe) {
		this.probe = probe;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment.phase_1.trial_states.trial_part_states.TrialPartState#getResponse()
	 */
	@Override
	public SurpriseReportProbeResponse getResponse() {
		updateTimingData(response);
		return response;
	}	
}