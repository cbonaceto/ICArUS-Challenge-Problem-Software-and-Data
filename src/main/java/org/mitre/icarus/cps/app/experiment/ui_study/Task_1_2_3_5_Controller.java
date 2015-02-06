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
package org.mitre.icarus.cps.app.experiment.ui_study;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.mitre.icarus.cps.app.experiment.ui_study.exam.ItemProbability;
import org.mitre.icarus.cps.app.experiment.ui_study.exam.PercentagesPresentationTrial;
import org.mitre.icarus.cps.app.experiment.ui_study.exam.SingleBlockTrial;
import org.mitre.icarus.cps.app.experiment.ui_study.exam.SpatialPresentationTrial;
import org.mitre.icarus.cps.app.experiment.ui_study.exam.Task_1_2_3_5_PhaseBase;
import org.mitre.icarus.cps.app.experiment.ui_study.exam.Task_5_Phase;
import org.mitre.icarus.cps.app.experiment.ui_study.exam.UIStudyPhase.DistributionType;
import org.mitre.icarus.cps.app.experiment.ui_study.exam.UIStudyPhase.NormalizationMode;
import org.mitre.icarus.cps.app.experiment.ui_study.exam.UIStudyPhase.WidgetType;
import org.mitre.icarus.cps.app.experiment.ui_study.trial_states.ConfirmSettingsTrialPartState;
import org.mitre.icarus.cps.app.experiment.ui_study.trial_states.PercentagesPresentationTrialState;
import org.mitre.icarus.cps.app.experiment.ui_study.trial_states.ProbabilityEntryTrialPartState;
import org.mitre.icarus.cps.app.experiment.ui_study.trial_states.SingleBlockTrialState;
import org.mitre.icarus.cps.app.experiment.ui_study.trial_states.SpatialPresentationTrialState;
import org.mitre.icarus.cps.app.experiment.ui_study.trial_states.TrialState_UIStudy;
import org.mitre.icarus.cps.app.util.ProbabilityUtils;
import org.mitre.icarus.cps.app.widgets.probability_entry.ProbabilityEntryConstants;
import org.mitre.icarus.cps.app.widgets.ui_study.ConditionPanel_UIStudy;
import org.mitre.icarus.cps.app.widgets.ui_study.HitsPanel;
import org.mitre.icarus.cps.app.widgets.ui_study.PercentsPanel;
import org.mitre.icarus.cps.app.widgets.ui_study.ProbabilityEntryPanel;
import org.mitre.icarus.cps.experiment_core.event.ConditionEvent;
import org.mitre.icarus.cps.experiment_core.event.SubjectActionEvent;
import org.mitre.icarus.cps.experiment_core.gui.INavButtonPanel.ButtonType;
import org.mitre.icarus.cps.experiment_core.subject_data.SubjectConditionData;

/**
 * Controller for UI study tasks 1-3 and 5.
 * 
 * @author CBONACETO
 *
 */
public class Task_1_2_3_5_Controller extends TaskController<Task_1_2_3_5_PhaseBase<?>> {

	/** The trial states for each trial in the task */
	protected List<SingleBlockTrialState> trialStates;
	
	/** The trial state for the current trial */
	protected SingleBlockTrialState currentTrialState;
	
	/** The previous probability settings */
	protected List<Integer> previousSettings;
	
	/** The distribution type of the current task */
	protected DistributionType distributionType;
	
	/** The hits panel for spatial presentation trials */
	protected HitsPanel hitsPanel;
	
	/** The percent panel for percentages presentation trial */
	protected PercentsPanel percentsPanel;
	
	/** The probability entry panel */
	protected ProbabilityEntryPanel probabilityEntryPanel;
	
	@Override
	public void initializeTaskController(Task_1_2_3_5_PhaseBase<?> task, ConditionPanel_UIStudy conditionPanel) {
		this.task = task;
		hitsPanel = conditionPanel.getHitsPanel();
		percentsPanel = conditionPanel.getPercentsPanel();
		probabilityEntryPanel = conditionPanel.getProbabilityEntryPanel();

		//Create the trial states for each trial
		trialStates = new ArrayList<SingleBlockTrialState>();		

		if(task.getTestTrials() != null && !task.getTestTrials().isEmpty()) {
			ArrayList<ItemProbability> initialSettings = null;
			if(task instanceof Task_5_Phase) {
				initialSettings = ((Task_5_Phase)task).getUiDefaults();
			}
			int defaultSetting = 0;
			if(task.getWidget() == WidgetType.StackedBars) {
				defaultSetting = 25;
			}
			
			int trialNum = 0;
			for(SingleBlockTrial trial : task.getTestTrials()) {				
				//Create the presentation trial states
				trial.setTrialResponse(null);
				ArrayList<ItemProbability> trialInitialSettings = initialSettings;
				if(trial.getUiDefaults() != null) {
					trialInitialSettings = trial.getUiDefaults();
				}
				if(trial instanceof PercentagesPresentationTrial) {
					trialStates.add(new PercentagesPresentationTrialState(trialNum, (PercentagesPresentationTrial)trial,
							task.getNormalizationMode(), trialInitialSettings, defaultSetting));
				} else if(trial instanceof SpatialPresentationTrial) {
					trialStates.add(new SpatialPresentationTrialState(trialNum, (SpatialPresentationTrial)trial,
							task.getNormalizationMode(), trialInitialSettings, defaultSetting));
				}
				trialNum++;
			}			
		}
	}

	@Override
	public Task_1_2_3_5_PhaseBase<?> getTaskWithResponseData() {
		if(trialStates != null && !trialStates.isEmpty()) {
			for(TrialState_UIStudy trialState : trialStates) {
				trialState.updateTrialResponseData();
			}
		}		
		return task;
	}
	
	@Override
	public void startCondition(int firstTrial, UIStudyController experimentController,
			SubjectConditionData subjectConditionData) {
		conditionRunning = true;		
		this.examController = experimentController;
		adjustingNormalizedSettings = false;
		
		if(examController != null) {
			examController.setNavButtonEnabled(ButtonType.Back, false);
			examController.setNavButtonEnabled(ButtonType.Next, false);
		}
		
		//Configure the probability distribution type display (percent or spatial)
		this.distributionType = task.getDistributionType();		
		conditionPanel.setProbabilityDistrubutionType(distributionType);
		hitsPanel.setFont(UIStudyConstants.FONT_HITS);
		conditionPanel.setProbabilityDistributionPanelVisible(true);
		
		//Configure the probability entry type widget
		probabilityEntryPanel.setProbabilityEntryType(task.getWidget(), task.getModality(), 
				task.getNormalizationMode(), task.getLooseNormalizationErrorThreshold());
		conditionPanel.setProbabilityEntryPanelVisible(true);
		
		//Set the sum error threshold
		if(task.getNormalizationMode() == NormalizationMode.Exact) {
			sumErrorThreshold = ProbabilityEntryConstants.EXACT_NORMALIZATION_ERROR_THRESHOLD;
		} else if(task.getNormalizationMode() == NormalizationMode.Loose) {
			if(task.getLooseNormalizationErrorThreshold() != null) {
				sumErrorThreshold = task.getLooseNormalizationErrorThreshold().intValue();
			} else {
				sumErrorThreshold = ProbabilityEntryConstants.LOOSE_NORMALIZATION_ERROR_THRESHOLD;
			}
		} else {
			sumErrorThreshold = null;
		}	
		
		conditionPanel.showTaskScreen();
		
		//Load the first trial
		currentTrialState = null;
		currentTrialPart = null;
		currentTrial = -1;
		trialPhase = -1;
		nextTrial();		
	}
	
	/**
	 * Go to the next trial or trial part in the task.
	 */
	protected void nextTrial() {
		if(!conditionRunning) {
			return;
		}		
	
		examController.setNavButtonEnabled(ButtonType.Back, false);	
		examController.setNavButtonEnabled(ButtonType.Next, false);
		
		long currentTime = System.currentTimeMillis();
		
		//Validate and save data from the previous trial part
		boolean responseValid = true;
		if(currentTrialPart != null) {
			currentTrialPart.setTrialPartTime_ms(currentTrialPart.getTrialPartTime_ms() + 
					(currentTime - trialPartStartTime));
			if(currentTrialPart instanceof ProbabilityEntryTrialPartState) {
				ProbabilityEntryTrialPartState probabilityProbe = (ProbabilityEntryTrialPartState)currentTrialPart;
				//Make sure the probability sum is within the error threshold if applicable
				if(sumErrorThreshold != null && 
						Math.abs(ProbabilityUtils.computeSum(probabilityEntryPanel.getCurrentSettings())-100) > sumErrorThreshold) {
					conditionPanel.setInstructionBannerText("Please adjust your percentages until the sum is within " + sumErrorThreshold + 
							"% of 100%, and click Next to continue.");
					responseValid = false;
				} else {				
					probabilityProbe.setInteractionTimes(probabilityEntryPanel.getInteractionTimes());
					probabilityProbe.setDetailedTimeData(probabilityEntryPanel.getDetailedTimeData());
					if(!adjustingNormalizedSettings) {
						probabilityProbe.setPreviousSettings(probabilityProbe.getCurrentSettings());
					}
					probabilityProbe.setCurrentSettings(probabilityEntryPanel.getCurrentSettings());	
					if(probabilityProbe.getCurrentNormalizedSettings() == null || 
							probabilityProbe.getCurrentNormalizedSettings().size() != probabilityProbe.getCurrentSettings().size()) {
						probabilityProbe.setCurrentNormalizedSettings(
								ProbabilityUtils.createDefaultInitialProbabilities(probabilityProbe.getCurrentSettings().size()));
					}					
					ProbabilityUtils.normalizePercentProbabilities(probabilityProbe.getCurrentSettings(), 
							probabilityProbe.getCurrentNormalizedSettings(),
							probabilityEntryPanel.getLockSettings());					
					previousSettings = probabilityProbe.getCurrentNormalizedSettings();
				}
			}
		}
		
		if(!responseValid) {
			//Don't advance if the response wasn't valid
			examController.setNavButtonEnabled(ButtonType.Next, true);
			trialPartStartTime = currentTime;
			return;
		}
		
		//Advance the trial phase counter
		trialPhase++;
		trialPartStartTime = currentTime;

		//Advance the trial counter if we're at the first trial, the current trial doesn't have any trial parts,
		//or we're at the last trial part of the current trial.
		if(currentTrial == -1 || (currentTrialState != null && 
				(currentTrialState.getTrialParts() == null || trialPhase >= currentTrialState.getTrialParts().size()))) {
			if(currentTrialState != null) {
				currentTrialState.setTrialTime_ms(currentTime - trialStartTime);
			}
			currentTrial++;
			trialStartTime = currentTime;
			trialPhase = 0;			
			if(trialStates == null || currentTrial >= trialStates.size()) {
				//We're at the end of the task
				conditionRunning = false;				

				// Fire condition completed event
				fireConditionEvent(new ConditionEvent(ConditionEvent.CONDITION_COMPLETED, 
						currentTrial, this));
				return;
			}
			else {				
				//Set the current trial
				currentTrialState = trialStates.get(currentTrial);
				currentTrialPart = null;							
			}
		}

		//Fire a trial changed event
		super.setTrial(currentTrial, trialPhase, currentTrialState.getNumTrialParts());
 
		//Show the current trial part
		currentTrialPart = currentTrialState.getTrialParts().get(trialPhase);
		if(currentTrialPart instanceof ProbabilityEntryTrialPartState) {
			//Show the probability distribution and allow subject to input probabilities
			ProbabilityEntryTrialPartState probabilityProbe = (ProbabilityEntryTrialPartState)currentTrialPart;
			StringBuilder sb = createTrialNumberString(currentTrial+1, trialPhase+1, 
					currentTrialState.getNumTrialParts(), "Enter Probabilities");
			if(!adjustingNormalizedSettings) {
				ArrayList<String> titles = currentTrialState.getTrial().getItemProbabilityIds();
				if(distributionType == DistributionType.Percent) {
					//Set percents in percents display
					percentsPanel.setTitles(titles);
					percentsPanel.setPercents(
							TaskController.getItemProbabilitiesAsPercents(currentTrialState.getTrial().getItemProbabilities()));
				} else {
					//Set hit locations in hits display
					hitsPanel.setTitles(titles);
					ArrayList<ArrayList<Point>> hitLocations = ((SpatialPresentationTrial)currentTrialState.getTrial()).getHitLocations();
					if(hitLocations != null) {
						int i = 0;
						for(ArrayList<Point> hits : hitLocations) {
							hitsPanel.setHits(hits, i);
							i++;
						}
					}
				}				
				probabilityEntryPanel.setProbabilityEntryTitles(titles);
				probabilityEntryPanel.resetInteractionTimes();
				probabilityEntryPanel.unlockAllProbabilities();
				probabilityEntryPanel.setSumVisible(true);				
			}
			probabilityEntryPanel.setCurrentSettings(probabilityProbe.getCurrentSettings());
			probabilityEntryPanel.setPreviousSettings(probabilityProbe.getPreviousSettings());
			probabilityEntryPanel.showEditableProbabilities();
			if(distributionType == DistributionType.Percent) { 
				sb.append("Input the four percentages on the left into the designated areas on the right, and click Next to continue.");
			} else {
				sb.append("Input the the percentage of hit markers across the four areas on the left into the designated areas on the right, and click Next to continue.");
			}
			conditionPanel.setInstructionBannerText(sb.toString());			
		} else if(currentTrialPart instanceof ConfirmSettingsTrialPartState) {
			//Confirm normalized settings
			adjustingNormalizedSettings = false;
			ConfirmSettingsTrialPartState confirmSettingsProbe = (ConfirmSettingsTrialPartState)currentTrialPart;
			StringBuilder sb = createTrialNumberString(currentTrial+1, trialPhase+1, 
					currentTrialState.getNumTrialParts(), "Confirm Settings");
			ProbabilityEntryTrialPartState probabilityProbe = confirmSettingsProbe.getProbabiltyProbe();
			probabilityEntryPanel.setCurrentSettings(probabilityProbe.getCurrentNormalizedSettings());
			probabilityEntryPanel.showConfirmedProbabilities();
			sb.append("Percentages sum to 100%.  Click Back to adjust them or Next to continue.");
			conditionPanel.setInstructionBannerText(sb.toString());				
			examController.setNavButtonEnabled(ButtonType.Back, true);		
		}
		
		//Pause before allowing a next button press
		pauseBeforeNextTrial();
	}	

	@Override
	public void subjectActionPerformed(SubjectActionEvent event) {
		if(event.eventType == SubjectActionEvent.NEXT_BUTTON_PRESSED) {
			
			//Advance to the next trial or trial part in the task
			nextTrial();
		}	
		else if(event.eventType == SubjectActionEvent.BACK_BUTTON_PRESSED) {
			//Go to the previous trial part.  This is only allowed to adjust probabilities.
			if(currentTrialPart instanceof ConfirmSettingsTrialPartState) {
				adjustingNormalizedSettings = true;
				trialPhase -= 2;
				nextTrial();
			}
		}
	}
}