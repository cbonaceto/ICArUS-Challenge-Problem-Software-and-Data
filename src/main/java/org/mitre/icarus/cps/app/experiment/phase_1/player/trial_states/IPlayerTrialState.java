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
package org.mitre.icarus.cps.app.experiment.phase_1.player.trial_states;

import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.TrialData;

/**
 * @author CBONACETO
 *
 */
public interface IPlayerTrialState {
	
	/**
	 * @return
	 */
	public boolean isTrialComplete();
	
	/**
	 * @return
	 */
	public TrialData getParticipantTrialData();

	/**
	 * @return
	 */
	public TrialData getAvgHumanTrialData();
	
	/**
	 * Get the participant's probability score (S1) for the trial.
	 * 
	 * @return the probability score
	 */
	public Double getScore_s1();
	
	/**
	 * Get the participant's troop allocation score (S2) for the trial.
	 * 
	 * @return the troop allocation score
	 */
	public Double getScore_s2();
	
	/**
	 * @return
	 */
	public Double getAvgHumanS1_score();
	
	/**
	 * @return
	 */
	public Double getAvgHumanS2_score();
	
	/**
	 * @param participantTrialData
	 * @param avgHumanTrialData
	 */
	public void updateTrialData(TrialData participantTrialData, TrialData avgHumanTrialData);
}
