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
package org.mitre.icarus.cps.app.experiment.ui_study.trial_states;

import java.util.ArrayList;
import java.util.List;

import org.mitre.icarus.cps.app.experiment.TrialPartState;
import org.mitre.icarus.cps.app.experiment.ui_study.exam.ItemProbability;
import org.mitre.icarus.cps.app.experiment.ui_study.exam.ItemProbabilityResponse;
import org.mitre.icarus.cps.app.widgets.probability_entry.TimeData;

/**
 * Probability entry trial part state.
 * 
 * @author CBONACETO
 *
 */
public class ProbabilityEntryTrialPartState extends TrialPartState {
	
	/** The current non-normalized probabilities */
	protected List<Integer> currentSettings;	
	
	/** The current normalized probabilities */
	protected List<Integer> currentNormalizedSettings;
	
	/** The previous non-normalized probabilities */
	protected List<Integer> previousSettings;
	
	/** The normative probabilities */
	protected List<Integer> normativeSettings;
	
	/** The total times spent interacting with each probability entry control */
	protected List<Long> interactionTimes;
	
	protected List<TimeData> detailedTimeData;
	
	/** The trial part state to confirm normalized probabilities when using "Delayed" normalization mode */
	protected ConfirmSettingsTrialPartState confirmSettingsTrialPart;

	public ProbabilityEntryTrialPartState(int trialNumber, int trialPartNumber) {
		super(trialNumber, trialPartNumber);
	}	

	/**
	 * Get the current probability settings (raw).
	 * 
	 * @return the current probability settings.
	 */
	public List<Integer> getCurrentSettings() {
		return currentSettings;
	}

	/**
	 * Set the current probability settings (raw).
	 * 
	 * @param currentSettings the current probability settings (raw).
	 */
	public void setCurrentSettings(List<Integer> currentSettings) {
		this.currentSettings = currentSettings;
	}

	/**
	 * Get the current probability settings (normalized).
	 * 
	 * @return the current probability settings (normalized)
	 */
	public List<Integer> getCurrentNormalizedSettings() {
		return currentNormalizedSettings;
	}

	/**
	 * Set the current probability settings (normalized)
	 * 
	 * @param currentNormalizedSettings the current probability settings (normalized)
	 */
	public void setCurrentNormalizedSettings(List<Integer> currentNormalizedSettings) {
		this.currentNormalizedSettings = currentNormalizedSettings;
	}

	/**
	 * Get the previous probability settings.
	 * 
	 * @return the previous probability settings.
	 */
	public List<Integer> getPreviousSettings() {
		return previousSettings;
	}

	/**
	 * Set the previous probability settings.
	 * 
	 * @param previousSettings the previous probability settings.
	 */
	public void setPreviousSettings(List<Integer> previousSettings) {
		this.previousSettings = previousSettings;
	}
	
	public List<Integer> getNormativeSettings() {
		return normativeSettings;
	}

	public void setNormativeSettings(List<Integer> normativeSettings) {
		this.normativeSettings = normativeSettings;
	}

	/**
	 * Get the times spent interacting with each probability entry control.
	 * 
	 * @return the interaction times
	 */
	public List<Long> getInteractionTimes() {
		return interactionTimes;
	}

	/**
	 * Set the times spent interacting with each probability entry control.
	 * 
	 * @param interactionTimes the interaction times
	 */
	public void setInteractionTimes(List<Long> interactionTimes) {
		this.interactionTimes = interactionTimes;
	}

	public List<TimeData> getDetailedTimeData() {
		return detailedTimeData;
	}

	public void setDetailedTimeData(List<TimeData> detailedTimeData) {
		this.detailedTimeData = detailedTimeData;
	}

	/**
	 * When confirming normalized settings, get the trial part state to confirm the normalized
	 * settings.
	 * 
	 * @return
	 */
	public ConfirmSettingsTrialPartState getConfirmSettingsTrialPart() {
		return confirmSettingsTrialPart;
	}

	/**
	 * When confirming normalized settings, set the trial part state to confirm the normalized
	 * settings.
	 * 
	 * @param confirmSettingsTrialPart
	 */
	public void setConfirmSettingsTrialPart(ConfirmSettingsTrialPartState confirmSettingsTrialPart) {
		this.confirmSettingsTrialPart = confirmSettingsTrialPart;
	}
	
	protected List<ItemProbabilityResponse> getProbabilitiyResponses(
			List<ItemProbabilityResponse> probabilities, List<ItemProbability> items) {		
		if(probabilities == null || 
				(currentSettings != null && currentSettings.size() != probabilities.size())) {
			if(currentSettings != null) {
				probabilities = new ArrayList<ItemProbabilityResponse>(currentSettings.size());	
				for(int i=0; i<currentSettings.size(); i++) {
					probabilities.add(null);
				}
			}
			else {
				probabilities = new ArrayList<ItemProbabilityResponse>();
			}
		}
		
		if(currentSettings != null) {
			for(int i=0; i<currentSettings.size(); i++) {
				Integer currentSetting = currentSettings.get(i);
				Integer normalizedSetting = null;
				Long interactionTime = null;
				TimeData timeData = null;
				if(currentNormalizedSettings != null && i < currentNormalizedSettings.size()) {
					normalizedSetting = currentNormalizedSettings.get(i);
				}
				if(interactionTimes != null && i < interactionTimes.size()) {
					interactionTime = interactionTimes.get(i);
				}	
				if(detailedTimeData != null && i < detailedTimeData.size()) {
					timeData = detailedTimeData.get(i);
				}
				ItemProbabilityResponse probabilityResponse = new ItemProbabilityResponse(items.get(i).getItemId(),
						(normalizedSetting != null) ? normalizedSetting.doubleValue() : null,
								(currentSetting != null) ? currentSetting.doubleValue() : null);
				probabilityResponse.setTime_ms(interactionTime);
				probabilityResponse.setInteractionTimeData(timeData);
				probabilities.set(i, probabilityResponse);
			}			
		}	
		
		return probabilities;
	}	
}