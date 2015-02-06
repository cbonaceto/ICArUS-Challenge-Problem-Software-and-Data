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
package org.mitre.icarus.cps.app.experiment.phase_2.trial_states.red_tactics;

import java.util.List;

import org.mitre.icarus.cps.app.experiment.phase_2.trial_states.ProbabilityReportTrialPartState;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.NormalizationConstraint;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.NormalizationConstraintType;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.Probability;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.ProbabilityReportProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.red_tactics_probes.RedTacticsProbabilityReportProbe;

/**
 * @author CBONACETO
 *
 */
public class RedTacticsProbabilityTrialPartState extends AbstractRedTacticsTrialPartState<RedTacticsProbabilityReportProbe> {
	
	/** A probability report probe used to manage the Red tactics probabilities */
	protected RedTacticsProbabilityReportTrialPartState probabilityReport;
	
	public RedTacticsProbabilityTrialPartState(int trialNumber, int trialPartNumber) {
		this(trialNumber, trialPartNumber, null);
	}
	
	public RedTacticsProbabilityTrialPartState(int trialNumber, int trialPartNumber, 
			RedTacticsProbabilityReportProbe probe) {
		super(trialNumber, trialPartNumber);
		probabilityReport = new  RedTacticsProbabilityReportTrialPartState(trialNumber, trialPartNumber);
		setProbe(probe);
	}
	
	/**
	 * @param probe
	 * @param initializeSettings
	 * @param setting
	 */
	public void setProbe(RedTacticsProbabilityReportProbe probe, Boolean initializeSettings, Integer setting) {
		super.setProbe(probe);
		probeChanged(initializeSettings, setting);		
	}
	
	@Override
	protected void probeChanged() {
		probeChanged(false, null);
	}	
	
	/**
	 * @param initializeSettings
	 * @param setting
	 */
	protected void probeChanged(Boolean initializeSettings, Integer setting) {
		probabilityReport.setProbe(probe, initializeSettings, setting);
	}
	
	public RedTacticsProbabilityReportTrialPartState getProbabilityReport() {
		return probabilityReport;
	}

	/**
	 * Get the current probability settings (raw).
	 * 
	 * @return the current probability settings.
	 */
	public List<Integer> getCurrentSettings() {
		return probabilityReport.getCurrentSettings();
	}

	/**
	 * Set the current probability settings (raw).
	 * 
	 * @param currentSettings the current probability settings (raw).
	 */
	public void setCurrentSettings(List<Integer> currentSettings) {
		probabilityReport.setCurrentSettings(currentSettings);
	}
	
	/**
	 * @return
	 */
	public int getCurrentSettingsSum() {
		return probabilityReport.getCurrentSettingsSum();
	}

	/**
	 * Get the current probability settings (normalized).
	 * 
	 * @return the current probability settings (normalized)
	 */
	public List<Integer> getCurrentNormalizedSettings() {
		return probabilityReport.getCurrentNormalizedSettings();
	}

	/**
	 * Set the current probability settings (normalized)
	 * 
	 * @param currentNormalizedSettings the current probability settings (normalized)
	 */
	public void setCurrentNormalizedSettings(List<Integer> currentNormalizedSettings) {
		probabilityReport.setCurrentNormalizedSettings(currentNormalizedSettings);
	}
	
	/**
	 * @return
	 */
	public int getCurrentNormalizedSettingsSum() {
		return probabilityReport.getCurrentNormalizedSettingsSum();
	}

	/**
	 * Get the previous probability settings.
	 * 
	 * @return the previous probability settings.
	 */
	public List<Integer> getPreviousSettings() {
		return probabilityReport.getPreviousSettings();
	}

	/**
	 * Set the previous probability settings.
	 * 
	 * @param previousSettings the previous probability settings.
	 */
	public void setPreviousSettings(List<Integer> previousSettings) {
		probabilityReport.setPreviousSettings(previousSettings);
	}
	
	/**
	 * @return
	 */
	public boolean isNormativeSettingsPresent() {
		return probabilityReport.isNormativeSettingsPresent();
	}
	
	/**
	 *  Get normalized normative probabilities (used for the player only). 
	 * 
	 * @return
	 */
	public List<Integer> getNormativeSettings() {
		return probabilityReport.getNormativeSettings();
	}

	/**
	 * Set the normalized normative probabilities (used for the player only). 
	 * 
	 * @param normativeSettings
	 */
	public void setNormativeSettings(List<Integer> normativeSettings) {
		probabilityReport.setNormativeSettings(normativeSettings);
	}
	
	/**
	 * @return
	 */
	public boolean isAvgHumanSettingsPresent() {
		return probabilityReport.isAvgHumanSettingsPresent();
	}

	/**
	 * Get the normalized average human probabilities (used for the player only). 
	 * 
	 * @return
	 */
	public List<Integer> getAvgHumanSettings() {
		return probabilityReport.getAvgHumanSettings();
	}

	/**
	 * Set the normalized average human probabilities (used for the player only).
	 * 
	 * @param avgHumanSettings
	 */
	public void setAvgHumanSettings(List<Integer> avgHumanSettings) {
		probabilityReport.setAvgHumanSettings(avgHumanSettings);
	}

	/**
	 * Get the times spent interacting with each probability entry control.
	 * 
	 * @return the interaction times
	 */
	public List<Long> getInteractionTimes() {
		return probabilityReport.getInteractionTimes();
	}

	/**
	 * Set the times spent interacting with each probability entry control.
	 * 
	 * @param interactionTimes the interaction times
	 */
	public void setInteractionTimes(List<Long> interactionTimes) {
		probabilityReport.setInteractionTimes(interactionTimes);
	}
	
	public NormalizationConstraint getNormalizationConstraint() {
		return probabilityReport.getNormalizationConstraint();
	}

	public void setNormalizationConstraint(NormalizationConstraint normalizationConstraint) {
		probabilityReport.setNormalizationConstraint(normalizationConstraint);
	}	

	@Override
	protected boolean validateAndUpdateAdditionalProbeResponseData() {
		return probabilityReport.validateAndUpdateProbeResponseData();
	}

	@Override
	public TrialPartType getTrialPartType() {
		return TrialPartType.RedTacticsProbabilityProbe;
	}
	
	/**
	 * @author CBONACETO
	 *
	 */
	protected static class RedTacticsProbabilityReportTrialPartState extends ProbabilityReportTrialPartState {
		
		/** The Red tactics probability report probe */
		protected RedTacticsProbabilityReportProbe probe;
		
		public RedTacticsProbabilityReportTrialPartState(int trialNumber, int trialPartNumber) {
			super(trialNumber, trialPartNumber);
		}
		
		/**
		 * @param probe
		 * @param initializeSettings
		 * @param setting
		 */
		public void setProbe(RedTacticsProbabilityReportProbe probe, Boolean initializeSettings, Integer setting) {
			this.probe = probe;
			if(probe != null) {
				Double targetSum = probe.getTargetSum() != null ? probe.getTargetSum() : 100D;
				NormalizationConstraintType normalizationConstraintType = probe.getNormalizationConstraint() != null ? 
					probe.getNormalizationConstraint() : NormalizationConstraintType.LessThanOrEqualTo;
				normalizationConstraint = new NormalizationConstraint(targetSum, normalizationConstraintType);
				if(initializeSettings != null && initializeSettings && probe.getProbabilities() != null && 
						!probe.getProbabilities().isEmpty()) {
					//Initialize current and previous settings lists
					initializeCurrentAndPreviousSettings(probe.getProbabilities().size(), setting);
				}
			}
		}

		@Override
		public ProbabilityReportProbe<?> getProbe() {			
			return null;
		}	
		
		@Override
		public boolean validateAndUpdateProbeResponseData() {			
			if(probe != null && probe.getProbabilities() != null && !probe.getProbabilities().isEmpty()) {
				boolean responseValid = false;
				if(currentSettings != null && currentSettings.size() == probe.getProbabilities().size()) {
					responseValid = true;
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

		@Override
		public TrialPartType getTrialPartType() {			
			return null;
		}		
	}

	@Override
	public String getProbeInstructions() {
		return "the probability of each Red style";
	}
}