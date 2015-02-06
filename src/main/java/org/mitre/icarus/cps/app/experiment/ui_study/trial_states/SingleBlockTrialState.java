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
package org.mitre.icarus.cps.app.experiment.ui_study.trial_states;

import java.util.ArrayList;

import org.mitre.icarus.cps.app.experiment.TrialPartState;
import org.mitre.icarus.cps.app.experiment.ui_study.TaskController;
import org.mitre.icarus.cps.app.experiment.ui_study.exam.ItemProbability;
import org.mitre.icarus.cps.app.experiment.ui_study.exam.ProbabilityResponse;
import org.mitre.icarus.cps.app.experiment.ui_study.exam.SingleBlockTrial;
import org.mitre.icarus.cps.app.experiment.ui_study.exam.SingleBlockTrialResponse;
import org.mitre.icarus.cps.app.experiment.ui_study.exam.UIStudyPhase.NormalizationMode;
import org.mitre.icarus.cps.app.util.ProbabilityUtils;

/**
 * @author CBONACETO
 *
 */
public abstract class SingleBlockTrialState extends TrialState_UIStudy {
	
	/** The probability entry trial part */
	protected ProbabilityEntryTrialPartState probabilityEntryTrialPart;
	
	/** The probability response */
	protected ProbabilityResponse probabilityResponse;
	
	public SingleBlockTrialState(int trialNumber) {
		super(trialNumber);
		probabilityResponse = new ProbabilityResponse();
	}
	
	protected void createTrialPartStates(SingleBlockTrial trial, NormalizationMode normalizationMode,
			ArrayList<ItemProbability> initialSettings, int defaultSetting) {
		trialParts = new ArrayList<TrialPartState>();
		int trialPartNum = -1;
		
		//Set the normative probabilities in the response data
		probabilityResponse.setNormativeProbabilities(
				TaskController.getItemProbabilitiesAsDoubles(trial.getItemProbabilities()));
		
		//Create probability entry trial part state
		probabilityEntryTrialPart = new ProbabilityEntryTrialPartState(trialNumber, ++trialPartNum);
		probabilityEntryTrialPart.setNormativeSettings(TaskController.getItemProbabilitiesAsPercents(trial.getItemProbabilities()));
		if(initialSettings != null && !initialSettings.isEmpty()) {
			ArrayList<Integer> currentSettings = TaskController.getItemProbabilitiesAsPercents(initialSettings);
			probabilityEntryTrialPart.setCurrentSettings(currentSettings);
			probabilityEntryTrialPart.setPreviousSettings(ProbabilityUtils.cloneProbabilities(currentSettings));
		} else {
			probabilityEntryTrialPart.setCurrentSettings(ProbabilityUtils.createProbabilities(
					trial.getItemProbabilities() != null ? trial.getItemProbabilities().size() : 0, defaultSetting));
			probabilityEntryTrialPart.setPreviousSettings(ProbabilityUtils.createProbabilities(
					probabilityEntryTrialPart.getCurrentSettings().size(), defaultSetting));
		}
		trialParts.add(probabilityEntryTrialPart);

		//Add a state to confirm the normalized settings
		if(normalizationMode != null && normalizationMode == NormalizationMode.Delayed) {
			trialParts.add(new ConfirmSettingsTrialPartState(trialNumber, ++trialPartNum, probabilityEntryTrialPart));
		}		
	}
	
	@Override
	public abstract SingleBlockTrial getTrial();
	
	@Override
	public void updateTrialResponseData() {
		if(getTrial().getTrialResponse() == null) {
			getTrial().setTrialResponse(new SingleBlockTrialResponse());
		}
		getTrial().getTrialResponse().setProbabilityResponse(probabilityResponse);
		probabilityResponse.setItemProbabilityResponses(
				probabilityEntryTrialPart.getProbabilitiyResponses(probabilityResponse.getItemProbabilityResponses(), 
						getTrial().getItemProbabilities()));
		probabilityResponse.setTime_ms(probabilityEntryTrialPart.getTrialPartTime_ms());
		updateTimingData(getTrial());
	}
}