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

import java.awt.Point;
import java.util.ArrayList;

import org.mitre.icarus.cps.app.experiment.TrialPartState;
import org.mitre.icarus.cps.app.experiment.ui_study.exam.ItemProbability;
import org.mitre.icarus.cps.app.experiment.ui_study.exam.MultiBlockSpatialPresentationTrial;
import org.mitre.icarus.cps.app.experiment.ui_study.exam.MultiBlockTrialResponse;
import org.mitre.icarus.cps.app.experiment.ui_study.exam.ProbabilityResponse;
import org.mitre.icarus.cps.app.experiment.ui_study.exam.SpatialPresentationBlock;
import org.mitre.icarus.cps.app.experiment.ui_study.exam.UIStudyPhase.NormalizationMode;
import org.mitre.icarus.cps.app.util.ProbabilityUtils;

/**
 * @author CBONACETO
 *
 */
public class MultiBlockSpatialPresentationTrialState extends TrialState_UIStudy {
	
	/** The multi-block spatial presentation trial */
	protected MultiBlockSpatialPresentationTrial trial;
	
	/** The probability entry trial parts for each block */
	protected ArrayList<ProbabilityEntryTrialPartState> probabilityEntryTrialParts;
	
	/** The probability responses to each block */
	protected ArrayList<ProbabilityResponse> probabilityResponses;

	public MultiBlockSpatialPresentationTrialState(int trialNumber, MultiBlockSpatialPresentationTrial trial,
			NormalizationMode normalizationMode, ArrayList<ItemProbability> initialSettings, int defaultSetting) {
		super(trialNumber);	
		this.trial = trial;
		if(trial != null) {
			probabilityResponses = new ArrayList<ProbabilityResponse>(trial.getSpatialPresentationBlocks().size());
			for(int i = 0; i<trial.getSpatialPresentationBlocks().size(); i++) {
				probabilityResponses.add(new ProbabilityResponse());
			}
			createTrialPartStates(normalizationMode, initialSettings, defaultSetting);
		}
		else {
			trialParts = new ArrayList<TrialPartState>();
		}
	}	
	
	protected void createTrialPartStates(NormalizationMode normalizationMode, ArrayList<ItemProbability> initialSettings,
			int defaultSetting) {
		trialParts = new ArrayList<TrialPartState>();
		int trialPartNum = -1;	
		
		probabilityEntryTrialParts = new ArrayList<ProbabilityEntryTrialPartState>(trial.getSpatialPresentationBlocks().size());
		ArrayList<Integer> hitCountsInteger = null;
		ArrayList<Integer> normativeProbsInteger = null;
		ArrayList<Double> hitCountsDouble = null;
		ArrayList<Double> normativeProbsDouble = null;
		int index = 0;
		for(SpatialPresentationBlock currentBlock : trial.getSpatialPresentationBlocks()) {
			//Set the normative probabilities in the response data
			if(hitCountsInteger == null) {
				hitCountsInteger = new ArrayList<Integer>(currentBlock.getItemProbabilities().size());				
				hitCountsDouble = new ArrayList<Double>(currentBlock.getItemProbabilities().size());
				normativeProbsInteger = new ArrayList<Integer>(currentBlock.getItemProbabilities().size());
				normativeProbsDouble = new ArrayList<Double>(currentBlock.getItemProbabilities().size());
				for(ArrayList<Point> hitLocations : currentBlock.getHitLocations()) {
					hitCountsInteger.add(hitLocations.size());
					hitCountsDouble.add((double)hitLocations.size());
					normativeProbsInteger.add(0);
					normativeProbsDouble.add(0D);
				}
			} else {
				int i = 0;
				for(ArrayList<Point> hitLocations : currentBlock.getHitLocations()) {
					hitCountsInteger.set(i, hitCountsInteger.get(i) + hitLocations.size());
					hitCountsDouble.set(i, hitCountsDouble.get(i) + hitLocations.size());
					i++;
				}	
			}
			ProbabilityUtils.normalizePercentProbabilities_Double(hitCountsDouble, normativeProbsDouble);
			probabilityResponses.get(index).setNormativeProbabilities(ProbabilityUtils.cloneProbabilities_Double(normativeProbsDouble));			
			
			//Create timed hits presentation trial part state for current block
			TimedHitsDisplayTrialPartState hitsDisplayTrialPart = new TimedHitsDisplayTrialPartState(trialNumber, ++trialPartNum,
					currentBlock);
			trialParts.add(hitsDisplayTrialPart);

			//Create probability entry trial part state that follows the timed hits display
			ProbabilityEntryTrialPartState probabilityEntryTrialPart = new ProbabilityEntryTrialPartState(trialNumber, ++trialPartNum);
			probabilityEntryTrialParts.add(probabilityEntryTrialPart);
			ProbabilityUtils.normalizePercentProbabilities(hitCountsInteger, normativeProbsInteger);
			probabilityEntryTrialPart.setNormativeSettings(ProbabilityUtils.cloneProbabilities(normativeProbsInteger));
			if(initialSettings != null && !initialSettings.isEmpty()) {
				ArrayList<Integer> currentSettings = new ArrayList<Integer>(initialSettings.size());
				for(ItemProbability item : initialSettings) {
					currentSettings.add((int)(item.getProbability() * 100));
				}
				probabilityEntryTrialPart.setCurrentSettings(currentSettings);
				probabilityEntryTrialPart.setPreviousSettings(ProbabilityUtils.cloneProbabilities(currentSettings));					
			} else {
				probabilityEntryTrialPart.setCurrentSettings(ProbabilityUtils.createProbabilities(
						currentBlock.getItemProbabilities() != null ? currentBlock.getItemProbabilities().size() : 0, defaultSetting));
				probabilityEntryTrialPart.setPreviousSettings(ProbabilityUtils.createProbabilities(
						probabilityEntryTrialPart.getCurrentSettings().size(), defaultSetting));
			}
			trialParts.add(probabilityEntryTrialPart);

			//Add a state to confirm the normalized settings
			if(normalizationMode != null && normalizationMode == NormalizationMode.Delayed) {
				trialParts.add(new ConfirmSettingsTrialPartState(trialNumber, ++trialPartNum, probabilityEntryTrialPart));
			}	
			
			index++;
		}
	}
	
	@Override
	public MultiBlockSpatialPresentationTrial getTrial() {
		return trial;
	}

	@Override
	public void updateTrialResponseData() {	
		if(trial.getTrialResponse() == null) {
			trial.setTrialResponse(new MultiBlockTrialResponse());
		}
		trial.getTrialResponse().setProbabilityResponses(probabilityResponses);
		int i = 0;
		for(ProbabilityEntryTrialPartState probabilityEntryTrialPart : probabilityEntryTrialParts) {
			ProbabilityResponse probabilityResponse = probabilityResponses.get(i);
			probabilityResponse.setItemProbabilityResponses(
					probabilityEntryTrialPart.getProbabilitiyResponses(probabilityResponse.getItemProbabilityResponses(), 
							trial.getSpatialPresentationBlocks().get(i).getItemProbabilities()));
			probabilityResponse.setTime_ms(probabilityEntryTrialPart.getTrialPartTime_ms());
			i++;
		}
		updateTimingData(getTrial());
	}
}