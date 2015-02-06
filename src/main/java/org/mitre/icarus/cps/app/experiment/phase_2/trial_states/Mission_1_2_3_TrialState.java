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

import org.mitre.icarus.cps.app.experiment.phase_2.trial_states.red_tactics.MostLikelyRedTacticTrialPartState;
import org.mitre.icarus.cps.exam.base.IcarusExam.NormalizationMode;
import org.mitre.icarus.cps.exam.phase_2.testing.DatumType;
import org.mitre.icarus.cps.exam.phase_2.testing.Mission_1_2_3_Trial;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.red_tactics_probes.MostLikelyRedTacticProbe;

/**
 * @author cbonaceto
 *
 */
public class Mission_1_2_3_TrialState extends TrialState_Phase2<Mission_1_2_3_Trial> {		

	public Mission_1_2_3_TrialState(int trialNumber, Mission_1_2_3_Trial trial, 
			NormalizationMode attackProbabilityReportNormaliationMode,
			boolean showBeginTrialPart) {
		super(trialNumber);
		super.setTrial(trial, attackProbabilityReportNormaliationMode, 
				null, showBeginTrialPart);		
	}
	
	@Override
	protected void createTrialPartStates(
			NormalizationMode attackProbabilityReportNormaliationMode,
			NormalizationMode redTacticsProbabilityNormaliationMode,
			boolean showBeginTrialPart) {
		trialParts = new ArrayList<TrialPartState_Phase2>();
		int trialPartNum = -1;
		if(trial != null) {
			//Add the most likely Red tactic probe
			if(trial.getMostLikelyRedTacticProbe() != null) {
				trialPartNum = addMostLikelyRedTacticProbe(trial.getMostLikelyRedTacticProbe(), trialPartNum);
			}
			
			//Add the begin trial part
			if(showBeginTrialPart) {
				trialPartNum = addBeginTrialPart(trial.getBlueLocations(), trialPartNum);
			}

			//Add the OSINT presentation probe(s)
			trialPartNum = addOsintPresentation(trial.getOsintPresentation(), trialPartNum);

			//Add the IMINT presentation probe(s)
			trialPartNum = addImintPresentation(trial.getImintPresentation(), trialPartNum);
			
			//Add the most likely Red tactic probe
			//trialPartNum = addMostLikelyRedTacticProbe(trial.getMostLikelyRedTacticProbe(), trialPartNum);

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
			trialPartNum = addSigintPresentationOrSelection(trial.getSigintSelectionProbe(), 
					trial.getSigintPresentation(), trialPartNum);

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
		
		/*//Create the base trial part states
		int trialPartNum = createStandardTrialPartStates(attackProbabilityReportNormaliationMode, showBeginTrialPart);
		
		//Create trial part states specific to Missions 1-3 (currently, only the most likely Red tactic probe)		
		//Add the most likely Red tactic probe
		if(trial.getMostLikelyRedTacticProbe() != null && trial.getMostLikelyRedTacticProbe().getRedTactics() != null
				&& !trial.getMostLikelyRedTacticProbe().getRedTactics().isEmpty()) {
			MostLikelyRedTacticTrialPartState mostLikelyRedTacticProbe = 
					new MostLikelyRedTacticTrialPartState(trialNumber, ++trialPartNum, trial.getMostLikelyRedTacticProbe());
			trialParts.add(mostLikelyRedTacticProbe);
		}*/		
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
}