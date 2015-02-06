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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.mitre.icarus.cps.app.experiment.phase_2.ExperimentConstants_Phase2;
import org.mitre.icarus.cps.app.experiment.phase_2.trial_states.red_tactics.MostLikelyRedTacticTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_2.trial_states.red_tactics.RedTacticParametersTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_2.trial_states.red_tactics.RedTacticsProbabilityTrialPartState;
import org.mitre.icarus.cps.app.util.ProbabilityUtils;
import org.mitre.icarus.cps.exam.base.IcarusExam.NormalizationMode;
import org.mitre.icarus.cps.exam.phase_2.testing.DatumType;
import org.mitre.icarus.cps.exam.phase_2.testing.Mission_4_5_6_Trial;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.red_tactics_probes.AbstractRedTacticsProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.red_tactics_probes.MostLikelyRedTacticProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.red_tactics_probes.RedTacticParametersProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.red_tactics_probes.RedTacticsProbabilityReportProbe;

/**
 * @author cbonaceto
 *
 */
@SuppressWarnings("deprecation")
public class Mission_4_5_6_TrialState extends TrialState_Phase2<Mission_4_5_6_Trial> {		

	public Mission_4_5_6_TrialState(int trialNumber, Mission_4_5_6_Trial trial, 
			NormalizationMode attackProbabilityReportNormaliationMode,
			NormalizationMode redTacticsProbabilityNormaliationMode,
			boolean showBeginTrialPart) {
		super(trialNumber);
		super.setTrial(trial, attackProbabilityReportNormaliationMode, 
				redTacticsProbabilityNormaliationMode, showBeginTrialPart);
	}
	
	@Override
	protected void createTrialPartStates(
			NormalizationMode attackProbabilityReportNormaliationMode, 
			NormalizationMode redTacticsProbabilityNormaliationMode,
			boolean showBeginTrialPart) {		
		trialParts = new ArrayList<TrialPartState_Phase2>();
		int trialPartNum = -1;
		if(trial != null) {
			//Add the most likely Red tactic probe, Red tactics probability probe, or Red tactic parameters probe
			if(trial.getRedTacticsProbe() != null) {
				AbstractRedTacticsProbe redTacticsProbe = trial.getRedTacticsProbe();
				if(redTacticsProbe instanceof MostLikelyRedTacticProbe) {
					trialPartNum = addMostLikelyRedTacticProbe((MostLikelyRedTacticProbe)redTacticsProbe, trialPartNum);
				} else if(redTacticsProbe instanceof RedTacticsProbabilityReportProbe) {
					trialPartNum = addRedTacticsProbabilityProbe((RedTacticsProbabilityReportProbe)redTacticsProbe, 
							redTacticsProbabilityNormaliationMode, trialPartNum);
				} else if(redTacticsProbe instanceof RedTacticParametersProbe) {
					trialPartNum = addRedTacticParametersProbe((RedTacticParametersProbe)redTacticsProbe, trialPartNum);
				}
			}
			
			//Add the begin trial part
			if(showBeginTrialPart) {
				trialPartNum = addBeginTrialPart(trial.getBlueLocations(), trialPartNum);
			}
			
			//Add the OSINT presentation probe(s)
			trialPartNum = addOsintPresentation(trial.getOsintPresentation(), trialPartNum);

			//Add the IMINT presentation probe(s)
			trialPartNum = addImintPresentation(trial.getImintPresentation(), trialPartNum);

			//Add the P(Propensity) report probe
			trialPartNum = addAttackProbabilityReportProbe(trial.getAttackPropensityProbe_Pp(), 
					DatumType.AttackProbabilityReport_Propensity, 
					attackProbabilityReportNormaliationMode, trialPartNum);
			
			//Add the HUMINT presentation probe(s)
			trialPartNum = addHumintPresentation(trial.getHumintPresentation(), trialPartNum);

			//Add the P(Capability, Propensity) report probe
			trialPartNum = addAttackProbabilityReportProbe(trial.getAttackProbabilityProbe_Ppc(), 
					DatumType.AttackProbabilityReport_Capability_Propensity, 
					attackProbabilityReportNormaliationMode, trialPartNum);

			//Add the SIGINT selection and/or presentation probe(s)
			if(trial.getSigintSelectionProbe() != null) {
				trialPartNum = addSigintPresentationOrSelection(trial.getSigintSelectionProbe(), 
						trial.getSigintPresentation(), trialPartNum);
			}

			//Add the P (Activity) report probe
			trialPartNum = addAttackProbabilityReportProbe(trial.getAttackProbabilityProbe_Pt(), 
					DatumType.AttackProbabilityReport_Activity, 
					attackProbabilityReportNormaliationMode, trialPartNum);

			//Add the P (activity, propensity, capability) report probe
			trialPartNum = addAttackProbabilityReportProbe(trial.getAttackProbabilityProbe_Ptpc(), 
					DatumType.AttackProbabilityReport_Activity_Capability_Propensity, 
					attackProbabilityReportNormaliationMode, trialPartNum);

			//Add the Blue action selection or presentation probe
			trialPartNum = addBlueActionSelection(trial.getBlueActionSelection(), trialPartNum);

			//Add the Red action presentation probe
			trialPartNum = addRedActionSelection(trial.getRedActionSelection(), trialPartNum);
		}		
	}	
	
	/**
	 * Add a most likely Red tactic probe.
	 * 
	 * @param probe
	 * @param trialPartNum
	 * @return
	 */
	protected int addMostLikelyRedTacticProbe(MostLikelyRedTacticProbe probe, int trialPartNum) {		
		if(probe != null && probe.getRedTactics() != null && !probe.getRedTactics().isEmpty()) {
			MostLikelyRedTacticTrialPartState mostLikelyRedTacticProbe = 
					new MostLikelyRedTacticTrialPartState(trialNumber, ++trialPartNum, probe);
			trialParts.add(mostLikelyRedTacticProbe);
		}
		return trialPartNum;
	}
	
	/**  
	 * Add a Red tactics probability probe.
	 * 
	 * @param probe
	 * @param redTacticsProbabilityNormaliationMode
	 * @param trialPartNum
	 * @return
	 */
	@SuppressWarnings("unused")
	protected int addRedTacticsProbabilityProbe(RedTacticsProbabilityReportProbe probe, 
			NormalizationMode redTacticsProbabilityNormaliationMode, int trialPartNum) {
		if(probe != null && probe.getProbabilities() != null && !probe.getProbabilities().isEmpty()) {
			RedTacticsProbabilityTrialPartState redTacticsProbabilityProbe = 
					new RedTacticsProbabilityTrialPartState(trialNumber, ++trialPartNum);
			trialParts.add(redTacticsProbabilityProbe);
			Integer setting = (ExperimentConstants_Phase2.NORMALIZE_INITIAL_PROBABILITIES && probe.getProbabilities() != null && 
					!probe.getProbabilities().isEmpty()) ? ProbabilityUtils.getDefaultInitialProbability(probe.getProbabilities().size()) :						
						ExperimentConstants_Phase2.DEFAULT_INITIAL_PROBABILITY;
			redTacticsProbabilityProbe.setProbe(probe, true, setting);
			
			//Add a trial part state to confirm normalized settings
			if(redTacticsProbabilityNormaliationMode == NormalizationMode.NormalizeAfter || 
					redTacticsProbabilityNormaliationMode == NormalizationMode.NormalizeAfterAndConfirm) {
				//Add a trial part state to confirm normalized settings
				List<ProbabilityReportTrialPartState> probabilityProbes = new LinkedList<ProbabilityReportTrialPartState>();
				probabilityProbes.add(redTacticsProbabilityProbe.getProbabilityReport());
				trialParts.add(createNormalizationTrialPart(++trialPartNum, 
						probe.getTargetSum(), probe.getNormalizationConstraint(),
						probabilityProbes));			
			}			
		}
		return trialPartNum;
	}
	
	/**
	 * Add a Red tactic parameters probe.
	 * 
	 * @param probe
	 * @param trialPartNum
	 * @return
	 */
	protected int addRedTacticParametersProbe(RedTacticParametersProbe probe, int trialPartNum) {
		if(probe != null) {
			RedTacticParametersTrialPartState redTacticParametersProbe = 
					new RedTacticParametersTrialPartState(trialNumber, ++trialPartNum);
			trialParts.add(redTacticParametersProbe);			
			redTacticParametersProbe.setProbe(probe, true, ExperimentConstants_Phase2.DEFAULT_INITIAL_PROBABILITY);			
		}
		return trialPartNum;
	}
}