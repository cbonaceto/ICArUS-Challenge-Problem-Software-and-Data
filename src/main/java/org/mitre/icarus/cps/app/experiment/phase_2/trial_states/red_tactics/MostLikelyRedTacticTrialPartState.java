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

import org.mitre.icarus.cps.exam.phase_2.testing.bluebook.RedTacticType;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.red_tactics_probes.MostLikelyRedTacticProbe;

/**
 * @author CBONACETO
 *
 */
public class MostLikelyRedTacticTrialPartState extends AbstractRedTacticsTrialPartState<MostLikelyRedTacticProbe> {//TrialPartState_Phase2 {
	
	/** The most likely Red tactic selected */
	protected RedTacticType mostLikelyRedTactic;

	public MostLikelyRedTacticTrialPartState(int trialNumber, int trialPartNumber) {
		super(trialNumber, trialPartNumber);
	}
	
	public MostLikelyRedTacticTrialPartState(int trialNumber, int trialPartNumber, MostLikelyRedTacticProbe probe) {
		super(trialNumber, trialPartNumber, probe);
	}
	
	@Override
	protected void probeChanged() {
		mostLikelyRedTactic = probe != null ? probe.getMostLikelyRedTactic() : null;
	}

	public RedTacticType getMostLikelyRedTactic() {
		return mostLikelyRedTactic;
	}	

	public void setMostLikelyRedTactic(RedTacticType mostLikelyRedTactic) {
		this.mostLikelyRedTactic = mostLikelyRedTactic;
	}
	
	@Override
	protected boolean validateAndUpdateAdditionalProbeResponseData() {
		if(probe != null) {
			probe.setMostLikelyRedTactic(mostLikelyRedTactic);
		}
		return mostLikelyRedTactic != null;
	}

	@Override
	public TrialPartType getTrialPartType() {
		return TrialPartType.MostLikelyRedTacticSelection;
	}

	@Override
	public String getProbeInstructions() {
		return "Red's most likely style";
	}
}