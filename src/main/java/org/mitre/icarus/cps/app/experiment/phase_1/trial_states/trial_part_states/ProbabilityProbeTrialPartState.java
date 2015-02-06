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

import java.util.ArrayList;
import java.util.List;

import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.GroupAttackProbabilityResponse;
import org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.IntLayer;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupType;

/**
 * Abstract base class for group probe and location probe trial part states.
 * 
 * @author CBONACETO
 *
 */
public abstract class ProbabilityProbeTrialPartState extends TrialPartState_Phase1 {
	
	/** The INT layer to add (if any) */
	protected IntLayer layerToAdd;
	
	/** The current non-normalized participant probabilities */
	protected List<Integer> currentSettings;	
	
	/** The current normalized participant probabilities */
	protected List<Integer> currentNormalizedSettings;
	
	/** The previous non-normalized participant probabilities */
	protected List<Integer> previousSettings;
	
	/** The normalized normative probabilities (used for the player only) */
	protected List<Integer> normativeSettings;
	
	/** The normalized average human probabilities (used for the player only */
	protected List<Integer> avgHumanSettings;
	
	/** The times spent interacting with each probability entry control */
	protected List<Long> interactionTimes;
	
	/** The trial part state to confirm normalized probabilities */
	protected ConfirmSettingsTrialPartState confirmSettingsTrialPart;

	public ProbabilityProbeTrialPartState(int trialNumber, int trialPartNumber) {
		this(trialNumber, trialPartNumber, null);
	}
	
	public ProbabilityProbeTrialPartState(int trialNumber, int trialPartNumber, 
			IntLayer layerToAdd) {
		super(trialNumber, trialPartNumber);
		this.layerToAdd = layerToAdd;
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
	
	/**
	 * @return
	 */
	public boolean isNormativeSettingsPresent() {
		return normativeSettings != null && !normativeSettings.isEmpty();
	}
	
	/**
	 *  Get normalized normative probabilities (used for the player only). 
	 * 
	 * @return
	 */
	public List<Integer> getNormativeSettings() {
		return normativeSettings;
	}

	/**
	 * Set the normalized normative probabilities (used for the player only). 
	 * 
	 * @param normativeSettings
	 */
	public void setNormativeSettings(List<Integer> normativeSettings) {
		this.normativeSettings = normativeSettings;
	}
	
	/**
	 * @return
	 */
	public boolean isAvgHumanSettingsPresent() {
		return avgHumanSettings != null && !avgHumanSettings.isEmpty();
	}

	/**
	 * Get the normalized average human probabilities (used for the player only). 
	 * 
	 * @return
	 */
	public List<Integer> getAvgHumanSettings() {
		return avgHumanSettings;
	}

	/**
	 * Set the normalized average human probabilities (used for the player only).
	 * 
	 * @param avgHumanSettings
	 */
	public void setAvgHumanSettings(List<Integer> avgHumanSettings) {
		this.avgHumanSettings = avgHumanSettings;
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
	
	protected ArrayList<GroupAttackProbabilityResponse> getProbabilitiyResponses(
			ArrayList<GroupAttackProbabilityResponse> groupAttackProbabilities,
			ArrayList<String> locations, ArrayList<GroupType> groups) {
		
		if(groupAttackProbabilities == null || 
				(currentSettings != null && currentSettings.size() != groupAttackProbabilities.size())) {
			if(currentSettings != null) {
				groupAttackProbabilities = new ArrayList<GroupAttackProbabilityResponse>(currentSettings.size());	
				for(int i=0; i<currentSettings.size(); i++) {
					groupAttackProbabilities.add(null);
				}
			}
			else {
				groupAttackProbabilities = new ArrayList<GroupAttackProbabilityResponse>();
			}
		}
		
		if(currentSettings != null) {
			for(int i=0; i<currentSettings.size(); i++) {
				Integer currentSetting = currentSettings.get(i);
				Integer normalizedSetting = null;
				Long interactionTime = null;
				if(currentNormalizedSettings != null && i < currentNormalizedSettings.size()) {
					normalizedSetting = currentNormalizedSettings.get(i);
				}
				if(interactionTimes != null && i < interactionTimes.size()) {
					interactionTime = interactionTimes.get(i);
				}
				GroupType group = null;		
				if(groups != null && i < groups.size()) {
					group = groups.get(i);
				}
				String location = null;		
				//if(group != null && locations != null && i < locations.size()) {
				if(locations != null && i < locations.size()) {
					location = locations.get(i);
				}
				
				GroupAttackProbabilityResponse probabilityResponse = null;
				if(group != null) {
					probabilityResponse = 
							new GroupAttackProbabilityResponse(group,
									(normalizedSetting != null) ? normalizedSetting.doubleValue() : null,
									(currentSetting != null) ? currentSetting.doubleValue() : null);
				}
				else {
					probabilityResponse =  
						new GroupAttackProbabilityResponse(location,
								(normalizedSetting != null) ? normalizedSetting.doubleValue() : null,
								(currentSetting != null) ? currentSetting.doubleValue() : null);
				}
				probabilityResponse.setTime_ms(interactionTime);
				groupAttackProbabilities.set(i, probabilityResponse);
			}			
		}	
		
		return groupAttackProbabilities;
	}	
}