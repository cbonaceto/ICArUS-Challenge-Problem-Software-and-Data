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
package org.mitre.icarus.cps.app.experiment.phase_2.trial_states;

import java.util.List;

import org.mitre.icarus.cps.app.util.ProbabilityUtils;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.NormalizationConstraint;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.Probability;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.ItemAdjustment;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.ProbabilityReportProbe;

/**
 * @author cbonaceto
 *
 */
public abstract class ProbabilityReportTrialPartState extends TrialPartState_Phase2 {
	
	/** The current non-normalized participant probabilities */
	protected List<Integer> currentSettings;	
	
	/** The current normalized participant probabilities */
	protected List<Integer> currentNormalizedSettings;
	
	/** The previous non-normalized participant probabilities */
	protected List<Integer> previousSettings;
	
	/** The normalized normative probabilities (used for playback and demonstration modes) */
	protected List<Integer> normativeSettings;
	
	/** The normalized average human probabilities (used for playback and demonstration modes) */
	protected List<Integer> avgHumanSettings;
	
	/** The times spent interacting with each probability entry control */
	protected List<Long> interactionTimes;
	
	/** The sequence of probability adjustments */
	protected List<ItemAdjustment> interactionSequence;
	
	/** The normalization constraints. Used when the normalization mode is "NormalizeDuringManual" */
	protected NormalizationConstraint normalizationConstraint;

	public ProbabilityReportTrialPartState(int trialNumber, int trialPartNumber) {
		super(trialNumber, trialPartNumber);
	}
	
	/**
	 * @param setting
	 */
	protected void initializeCurrentAndPreviousSettings(int numProbs, Integer setting) {
		currentSettings = ProbabilityUtils.createProbabilities(numProbs, setting);
		previousSettings = ProbabilityUtils.createProbabilities(numProbs, setting);
	}	
	
	/**
	 * Get the number of probabilities.
	 * 
	 * @return
	 */
	public int getNumProbs() {
		return currentSettings != null ? currentSettings.size() : 0;
	}

	@Override
	public abstract ProbabilityReportProbe<?> getProbe();

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
	 * @return
	 */
	public int getCurrentSettingsSum() {
		int sum = 0;
		if(currentSettings != null && !currentSettings.isEmpty()) {
			for(Integer setting : currentSettings) {
				sum += setting != null ? setting : 0;
			}
		}
		return sum;
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
	 * @return
	 */
	public int getCurrentNormalizedSettingsSum() {
		int sum = 0;
		if(currentNormalizedSettings != null && !currentNormalizedSettings.isEmpty()) {
			for(Integer setting : currentNormalizedSettings) {
				sum += setting != null ? setting : 0;
			}
		}
		return sum;
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
	 * Get the sequence of probability adjustments.
	 * 
	 * @return the sequence of probability adjustments
	 */
	public List<ItemAdjustment> getInteractionSequence() {
		return interactionSequence;
	}

	/**
	 * Set the sequence of probability adjustments.
	 * 
	 * @param interactionSequence the sequence of probability adjustments
	 */
	public void setInteractionSequence(List<ItemAdjustment> interactionSequence) {
		this.interactionSequence = interactionSequence;
	}

	public NormalizationConstraint getNormalizationConstraint() {
		return normalizationConstraint;
	}

	public void setNormalizationConstraint(NormalizationConstraint normalizationConstraint) {
		this.normalizationConstraint = normalizationConstraint;
	}	

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.app.experiment.phase_2.trial_states.TrialPartState_Phase2#updateResponseData()
	 */
	@Override
	public boolean validateAndUpdateProbeResponseData() {
		ProbabilityReportProbe<?> probe = getProbe();
		if(probe != null && probe.getProbabilities() != null && !probe.getProbabilities().isEmpty()) {
			boolean responseValid = false;
			if(currentSettings != null && currentSettings.size() == probe.getProbabilities().size()) {
				responseValid = true;
				probe.setProbabilityAdjustmentSequence(interactionSequence);
				for(int i=0; i< currentSettings.size(); i++) {					
					Probability probability = probe.getProbabilities().get(i);
					if(probability != null) {
						Integer currentSetting = currentSettings.get(i);						
						Integer normalizedSetting = null;
						Long interactionTime = null;
						if(currentNormalizedSettings != null && i < currentNormalizedSettings.size()) {
							normalizedSetting = currentNormalizedSettings.get(i);
						}
						if(interactionTimes != null && i < interactionTimes.size()) {
							interactionTime = interactionTimes.get(i);
						}
						probability.setRawProbability(currentSetting != null ? currentSetting.doubleValue() : null);
						probability.setProbability(normalizedSetting != null ? normalizedSetting.doubleValue() : null);
						probability.setTime_ms(interactionTime);
						if(probability.getProbability() == null || probability.getProbability() < 0 || probability.getProbability() > 100) {
							responseValid = false;
						}
					}
				}
			}
			return responseValid;
		}
		return true;
	}
}