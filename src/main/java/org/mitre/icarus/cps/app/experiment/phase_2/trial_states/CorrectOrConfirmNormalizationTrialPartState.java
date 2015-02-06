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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.mitre.icarus.cps.app.experiment.phase_2.ExperimentConstants_Phase2;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.NormalizationConstraintType;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.NormalizationProbe;

/**
 * 
 * @author CBONACETO
 *
 */
public class CorrectOrConfirmNormalizationTrialPartState extends TrialPartState_Phase2 {
	
	/** The normalization probe (contains the normalization constraints) */
	protected NormalizationProbe probe;
	
	/** The probability report probes to confirm normalized settings for (mapped by report datum ID) */
	protected Map<String, ProbabilityReportTrialPartState> probabilityReportsMap;
	
	/** The target sum of the probabilities */
	protected Double targetSum;
	
	/** The normalization constraint with respect to the target sum */
	protected NormalizationConstraintType normalizationConstraint;
	
	/** Whether to always confirm probabilities even if they are already normalized */
	protected boolean alwaysConfirmNormalization = ExperimentConstants_Phase2.ALWAYS_CONFIRM_NORMALIZATION;

	public CorrectOrConfirmNormalizationTrialPartState(int trialNumber, int trialPartNumber) {
		super(trialNumber, trialPartNumber);
		probabilityReportsMap = new HashMap<String, ProbabilityReportTrialPartState>();
	}
	
	public CorrectOrConfirmNormalizationTrialPartState(int trialNumber, int trialPartNumber,
			NormalizationProbe probe) {
		this(trialNumber, trialPartNumber, probe != null ? probe.getSum() : 100D,
				probe != null ? probe.getNormalizationConstraint() : NormalizationConstraintType.EqualTo);
		this.probe = probe;
	}
	
	public CorrectOrConfirmNormalizationTrialPartState(int trialNumber, int trialPartNumber,
			Double targetSum, NormalizationConstraintType normalizationConstraint) {
		super(trialNumber, trialPartNumber);
		probabilityReportsMap = new HashMap<String, ProbabilityReportTrialPartState>();
		this.targetSum = targetSum;
		this.normalizationConstraint = normalizationConstraint;
		this.probe = new NormalizationProbe(null, targetSum, normalizationConstraint);
	}

	@Override
	public NormalizationProbe getProbe() {
		return probe;
	}

	public void setProbe(NormalizationProbe probe, boolean updateNormalizationConstraints) {
		this.probe = probe;
		if(updateNormalizationConstraints && probe != null) {
			this.targetSum = probe.getSum();
			this.normalizationConstraint = probe.getNormalizationConstraint();
		}
	}
	
	public Double getTargetSum() {
		return targetSum;
	}

	public void setTargetSum(Double targetSum) {
		this.targetSum = targetSum;
		if(probe != null) {
			probe.setSum(targetSum);
		}
	}

	public NormalizationConstraintType getNormalizationConstraint() {
		return normalizationConstraint;
	}

	public void setNormalizationConstraint(NormalizationConstraintType normalizationConstraint) {
		this.normalizationConstraint = normalizationConstraint;
		if(probe != null) {
			probe.setNormalizationConstraint(normalizationConstraint);
		}
	}
	
	public Collection<ProbabilityReportTrialPartState> getProbabilityReports() {
		return probabilityReportsMap.values();
	}
	
	public ProbabilityReportTrialPartState getProbabilityReport(String reportId) {
		return probabilityReportsMap.get(reportId);
	}
	
	public void setProbabilityReport(String reportId, ProbabilityReportTrialPartState probabilityReport) {
		probabilityReportsMap.put(reportId, probabilityReport);
	}

	public boolean isAlwaysConfirmNormalization() {
		return alwaysConfirmNormalization;
	}

	public void setAlwaysConfirmNormalization(boolean alwaysConfirmNormalization) {
		this.alwaysConfirmNormalization = alwaysConfirmNormalization;
	}

	@Override
	public boolean validateAndUpdateProbeResponseData() {
		//Check that the probability settings are valid given the normalization constraints
		if(probe != null && probe.getNormalizationConstraint() != null && probe.getSum() != null) {
			boolean responseValid = false;
			Double sum = 0d;
			if(!probabilityReportsMap.isEmpty()) {
				for(ProbabilityReportTrialPartState probabilityReport : probabilityReportsMap.values()) {
					sum += probabilityReport.getCurrentSettingsSum();
				}
				switch(normalizationConstraint) {
				case EqualTo:
					responseValid = sum == targetSum;
					break;
				case LessThanOrEqualTo:
					responseValid = sum <= targetSum;
					break;				
				}
			}
			return responseValid;
		}
		return true;
	}
	
	@Override
	public TrialPartType getTrialPartType() {
		return TrialPartType.NormalizationCorrectionOrConfirmation;
	}
}