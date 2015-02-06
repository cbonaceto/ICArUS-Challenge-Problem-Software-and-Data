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

import org.mitre.icarus.cps.exam.phase_2.testing.DatumType;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.AttackProbabilityReportProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.NormalizationConstraint;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.NormalizationConstraintType;

/**
 * Trial part state to report the Red attack probability at one or more Blue locations.
 * 
 * @author cbonaceto
 *
 */
public class AttackProbabilityReportTrialPartState extends ProbabilityReportTrialPartState {
	
	/** The attack probability report probe */
	protected AttackProbabilityReportProbe probe;
	
	/** The attack probability report type */
	protected final TrialPartType attackProbabilityReportType;
	
	/*** The attack probability report datum type */
	protected final DatumType attackProbabilityReportDatumType;
	
	public AttackProbabilityReportTrialPartState(int trialNumber, int trialPartNumber, 
			DatumType attackProbabilityReportType) {
		this(trialNumber, trialPartNumber, attackProbabilityReportType, null, false, null);
	}
	
	public AttackProbabilityReportTrialPartState(int trialNumber, int trialPartNumber,
			DatumType attackProbabilityReportDatumType, AttackProbabilityReportProbe probe, 
			boolean initializeSettings, Integer setting) {
		super(trialNumber, trialPartNumber);
		this.attackProbabilityReportDatumType = attackProbabilityReportDatumType;
		switch(attackProbabilityReportDatumType) {
		case AttackProbabilityReport_Activity:
			this.attackProbabilityReportType = TrialPartType.AttackProbabilityReport_Activity;
			break;
		case AttackProbabilityReport_Activity_Capability_Propensity:
			this.attackProbabilityReportType = TrialPartType.AttackProbabilityReport_Activity_Capability_Propensity;
			break;
		case AttackProbabilityReport_Capability_Propensity:
			this.attackProbabilityReportType = TrialPartType.AttackProbabilityReport_Capability_Propensity;
			break;
		case AttackProbabilityReport_Propensity:
			this.attackProbabilityReportType = TrialPartType.AttackProbabilityReport_Propensity;
			break;
		default:
			this.attackProbabilityReportType = TrialPartType.AttackProbabilityReport;
			break;		
		}
		if(probe != null) {
			setProbe(probe, initializeSettings, setting);
		}
	}

	@Override
	public AttackProbabilityReportProbe getProbe() {
		return probe;
	}
	
	/**
	 * @param probe
	 */
	public void setProbe(AttackProbabilityReportProbe probe) {
		this.probe = probe;
	}

	/**
	 * @param probe
	 * @param initializeSettings
	 * @param setting
	 */
	public void setProbe(AttackProbabilityReportProbe probe, Boolean initializeSettings, Integer setting) {
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

	/**
	 * @return
	 */
	public DatumType getAttackProbabilityReportDatumType() {
		return attackProbabilityReportDatumType;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.app.experiment.phase_2.trial_states.TrialPartState_Phase2#getTrialPartType()
	 */
	@Override
	public TrialPartType getTrialPartType() {
		return attackProbabilityReportType;
	}	
}