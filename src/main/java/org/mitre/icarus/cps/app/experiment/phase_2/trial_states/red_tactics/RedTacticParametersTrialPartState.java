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

import java.util.ArrayList;
import java.util.List;

import org.mitre.icarus.cps.app.util.ProbabilityUtils;
import org.mitre.icarus.cps.exam.phase_2.testing.bluebook.RedTactic;
import org.mitre.icarus.cps.exam.phase_2.testing.bluebook.RedTacticParameters;
import org.mitre.icarus.cps.exam.phase_2.testing.bluebook.RedTacticParameters.RedTacticConsiderationData;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.red_tactics_probes.RedTacticParametersProbe;

/**
 * @author CBONACETO
 *
 */
@SuppressWarnings("deprecation")
public class RedTacticParametersTrialPartState extends AbstractRedTacticsTrialPartState<RedTacticParametersProbe> {
	
	/** The current attack probabilities */ 
	protected List<Integer> currentSettings;
	
	/** The previous attack probabilities */
	protected List<Integer> previousSettings;

	public RedTacticParametersTrialPartState(int trialNumber, int trialPartNumber) {
		this(trialNumber, trialPartNumber, null);
	}
	
	public RedTacticParametersTrialPartState(int trialNumber, int trialPartNumber, 
			RedTacticParametersProbe probe) {
		super(trialNumber, trialPartNumber, probe);
	}
	
	public List<Integer> getCurrentSettings() {
		return currentSettings;
	}

	public void setCurrentSettings(List<Integer> currentSettings) {
		this.currentSettings = currentSettings;
	}

	public List<Integer> getPreviousSettings() {
		return previousSettings;
	}

	public void setPreviousSettings(List<Integer> previousSettings) {
		this.previousSettings = previousSettings;
	}

	/**
	 * @param probe
	 * @param initializeSettings
	 * @param setting
	 */
	public void setProbe(RedTacticParametersProbe probe, Boolean initializeSettings, Integer setting) {
		super.setProbe(probe);
		probeChanged(initializeSettings, setting);		
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.app.experiment.phase_2.trial_states.red_tactics.AbstractRedTacticsTrialPartState#probeChanged()
	 */
	@Override
	protected void probeChanged() {
		probeChanged(false, null);
	}
	
	/**
	 * @param initializeSettings
	 * @param setting
	 */
	protected void probeChanged(Boolean initializeSettings, Integer setting) {
		if(probe != null) {
			if(initializeSettings != null && initializeSettings) {
				//Initialize current and previous settings lists
				currentSettings = ProbabilityUtils.createProbabilities(4, setting);
				previousSettings = ProbabilityUtils.createProbabilities(4, setting);
			}
		}
	}

	@Override
	protected boolean validateAndUpdateAdditionalProbeResponseData() {
		if(probe != null && probe.getRedTacticParameters() != null) {
			boolean responseValid = false;			
			if(currentSettings != null && currentSettings.size() == 4) {
				responseValid = true;
				List<Double> attackProbabilities = probe.getRedTacticParameters().getAttackProbabilities();
				if(attackProbabilities == null || attackProbabilities.size() != 4) {
					attackProbabilities = new ArrayList<Double>(4);
					probe.getRedTacticParameters().setAttackProbabilities(attackProbabilities);
					for(int i=0; i<4; i++) {
						attackProbabilities.add(0D);
					}					
				}
				for(int i=0; i<currentSettings.size(); i++) {
					Double prob = ProbabilityUtils.convertPercentProbToDecimalProb(currentSettings.get(i));
					attackProbabilities.set(i, prob);
					if(prob == null || prob < 0 || prob > 1) {
						responseValid = false;
					}
					/*	Long interactionTime = null;
						if(interactionTimes != null && i < interactionTimes.size()) {
							interactionTime = interactionTimes.get(i);
						}
					}*/
				}
			}
			return responseValid;
		}
		return true;
	}
	
	/**
	 * @param redTactic
	 * @return
	 */
	public RedTactic updateRedTactic(RedTactic redTactic) {
		if(redTactic == null) {
			redTactic = new RedTactic();
		}
		if(redTactic.getTacticParameters() == null) {
			redTactic.setTacticParameters(new RedTacticParameters( 
					RedTacticConsiderationData.P_And_U, null, 
					RedTacticParameters.DEFAULT_HIGH_P_THRESHOLD, 
					RedTacticParameters.DEFAULT_LARGE_U_THRESHOLD));
		}
		redTactic.getTacticParameters().setAttackProbabilities(
				ProbabilityUtils.convertPercentProbsToDecimalProbs(currentSettings));
		return redTactic;
	}

	@Override
	public TrialPartType getTrialPartType() {
		return TrialPartType.RedTacticParametersProbe;
	}

	@Override
	public String getProbeInstructions() {
		return "the probabilities that define Red's style";
	}
}