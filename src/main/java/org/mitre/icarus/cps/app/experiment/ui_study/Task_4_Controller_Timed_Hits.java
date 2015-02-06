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

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import org.mitre.icarus.cps.app.experiment.ui_study.exam.MultiBlockSpatialPresentationTrial;
import org.mitre.icarus.cps.app.experiment.ui_study.exam.SpatialPresentationBlock;
import org.mitre.icarus.cps.app.experiment.ui_study.exam.Task_4_Phase;
import org.mitre.icarus.cps.app.experiment.ui_study.exam.UIStudyPhase.DistributionType;
import org.mitre.icarus.cps.app.experiment.ui_study.exam.UIStudyPhase.NormalizationMode;
import org.mitre.icarus.cps.app.experiment.ui_study.trial_states.ConfirmSettingsTrialPartState;
import org.mitre.icarus.cps.app.experiment.ui_study.trial_states.MultiBlockSpatialPresentationTrialState;
import org.mitre.icarus.cps.app.experiment.ui_study.trial_states.ProbabilityEntryTrialPartState;
import org.mitre.icarus.cps.app.experiment.ui_study.trial_states.TimedHitsDisplayTrialPartState;
import org.mitre.icarus.cps.app.experiment.ui_study.trial_states.TrialState_UIStudy;
import org.mitre.icarus.cps.app.util.ProbabilityUtils;
import org.mitre.icarus.cps.app.widgets.probability_entry.ProbabilityEntryConstants;
import org.mitre.icarus.cps.app.widgets.ui_study.ConditionPanel_UIStudy;
import org.mitre.icarus.cps.app.widgets.ui_study.HitsPanel;
import org.mitre.icarus.cps.app.widgets.ui_study.ProbabilityEntryPanel;
import org.mitre.icarus.cps.experiment_core.event.ConditionEvent;
import org.mitre.icarus.cps.experiment_core.event.SubjectActionEvent;
import org.mitre.icarus.cps.experiment_core.gui.INavButtonPanel.ButtonType;
import org.mitre.icarus.cps.experiment_core.subject_data.SubjectConditionData;

/**
 * Controller for UI study task 4.
 * 
 * @author CBONACETO
 *
 */
public class Task_4_Controller_Timed_Hits extends TaskController<Task_4_Phase> {
	
	/** The trial states for each trial in the task */
	protected List<MultiBlockSpatialPresentationTrialState> trialStates;
	
	/** The trial state for the current trial */
	protected MultiBlockSpatialPresentationTrialState currentTrialState;
	
	/** The previous probability settings */
	protected List<Integer> previousSettings;
	
	/** The hits panel  */
	protected HitsPanel hitsPanel;
	
	/** The probability entry panel */
	protected ProbabilityEntryPanel probabilityEntryPanel;
	
	protected boolean waitingToShowHits = false;	
	
	@Override
	public void initializeTaskController(Task_4_Phase task,
			ConditionPanel_UIStudy conditionPanel) {
		this.task = task;
		hitsPanel = conditionPanel.getHitsPanel();		
		probabilityEntryPanel = conditionPanel.getProbabilityEntryPanel();

		//Create the trial states for each trial
		trialStates = new ArrayList<MultiBlockSpatialPresentationTrialState>();		

		if(task.getTestTrials() != null && !task.getTestTrials().isEmpty()) {
			//Create the trial states
			int trialNum = 0;
			for(MultiBlockSpatialPresentationTrial trial : task.getTestTrials()) {
				trialStates.add(new MultiBlockSpatialPresentationTrialState(trialNum, trial,
						task.getNormalizationMode(), null, 25));
				trialNum++;
			}			
		}
	}

	@Override
	public Task_4_Phase getTaskWithResponseData() {
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
	
		conditionPanel.setProbabilityDistrubutionType(DistributionType.Spatial);
		hitsPanel.setFont(UIStudyConstants.FONT_HITS_LARGE);
		conditionPanel.setProbabilityDistributionPanelVisible(true);

		//Configure the probability entry type widget
		probabilityEntryPanel.setProbabilityEntryType(task.getWidget(), task.getModality(), 
				task.getNormalizationMode(), task.getLooseNormalizationErrorThreshold());
		conditionPanel.setProbabilityEntryPanelVisible(false);

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
					if(task.getNormalizationMode() == NormalizationMode.Loose) {
						previousSettings = probabilityProbe.getCurrentSettings();
					} else {
						previousSettings = probabilityProbe.getCurrentNormalizedSettings();
					}
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
		if(currentTrialPart instanceof TimedHitsDisplayTrialPartState) {
			conditionPanel.getHitsPanel().clearAllHits();
			conditionPanel.setProbabilityEntryPanelVisible(false);
			StringBuilder sb = createTrialNumberString(currentTrial+1, trialPhase+1, 
					currentTrialState.getNumTrialParts(), "Observe Hits");
			if(trialPhase == 0) {
				sb.append("A new trial is beginning. ");
			} else {
				sb.append("The next part of the trial is beginning. ");
			}
			sb.append("Click Next to observe the distribution of hits across the four areas.");
			conditionPanel.setInstructionBannerText(sb.toString());
			SwingWorker<Object, Object> timedHitsWorker = new SwingWorker<Object, Object>() {
				@Override
				protected Object doInBackground() throws Exception {
					getExamController().setNavButtonEnabled(ButtonType.Next, true);
					//Wait for Next button press
					waitingToShowHits = true;
					while(waitingToShowHits && conditionRunning) {
						try {
							Thread.sleep(25);
						} catch(Exception ex) {}
					}

					if(!conditionRunning) {
						return null;
					}

					final SpatialPresentationBlock presentationBlock = 
							((TimedHitsDisplayTrialPartState)currentTrialPart).getSpatialPresentationBlock();
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							getExamController().setNavButtonEnabled(ButtonType.Next, false);							
							//Set hit locations in hits display
							conditionPanel.setInstructionBannerText("");
							ArrayList<String> titles = presentationBlock.getItemProbabilityIds();
							hitsPanel.setTitles(titles);
							probabilityEntryPanel.setProbabilityEntryTitles(titles);
						}
					});
					
					if(!conditionRunning) {
						return null;
					}

					//Show the timed sequence of hits					
					ArrayList<ArrayList<Point>> hitLocations = presentationBlock.getHitLocations();
					long sleepTime_ms = (long)(1.0/presentationBlock.getHitsPerSecond() * 1000);
					if(hitLocations != null) {
						int[] currentIndices = new int[hitLocations.size()];
						for(Integer quadrant : presentationBlock.getQuadrantVisitSequence()) {
							hitsPanel.addHit(hitLocations.get(quadrant).get(currentIndices[quadrant]),
									quadrant);
							try {
								Thread.sleep(sleepTime_ms);
							} catch(Exception ex) {}
							hitsPanel.clearAllHits();
							currentIndices[quadrant]++;
							if(!conditionRunning) {
								break;
							}
						}					
					}
					if(conditionRunning) {
						try {Thread.sleep(250);} catch(Exception ex) {}
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								conditionPanel.setInstructionBannerText("The hits display is complete. Click Next to continue");
								//Pause before allowing a next button press
								pauseBeforeNextTrial();
							}
						});					
					}
					return null;
				}
				@Override
				protected void done() {
					//getExamController().setNavButtonEnabled(ButtonType.Next, true);	
					//Go to the probability entry part
					//nextTrial();	
				}	
			};		
			try {
				timedHitsWorker.execute();
			} catch(Exception e) {
				getExamController().setNavButtonEnabled(ButtonType.Next, true);
			}					
		} else if(currentTrialPart instanceof ProbabilityEntryTrialPartState) {
			//Allow subject to input probabilities
			conditionPanel.setProbabilityEntryPanelVisible(true);
			ProbabilityEntryTrialPartState probabilityProbe = (ProbabilityEntryTrialPartState)currentTrialPart;
			StringBuilder sb = createTrialNumberString(currentTrial+1, trialPhase+1, 
					currentTrialState.getNumTrialParts(), "Enter Probabilities");
			if(!adjustingNormalizedSettings) {
				probabilityEntryPanel.resetInteractionTimes();
				probabilityEntryPanel.unlockAllProbabilities();
				probabilityEntryPanel.setSumVisible(true);
			}
			if(!adjustingNormalizedSettings && trialPhase > 1) {
				//Initialize probabilities to normative settings
				/*probabilityEntryPanel.setCurrentSettings(probabilityProbe.getNormativeSettings());
				probabilityEntryPanel.setPreviousSettings(probabilityProbe.getNormativeSettings());*/
				probabilityEntryPanel.setCurrentSettings(previousSettings);
				probabilityEntryPanel.setPreviousSettings(previousSettings);
			} else {
				probabilityEntryPanel.setCurrentSettings(probabilityProbe.getCurrentSettings());
				probabilityEntryPanel.setPreviousSettings(probabilityProbe.getPreviousSettings());
			}
			probabilityEntryPanel.showEditableProbabilities();
			sb.append("Input the the percentage of hit markers you observed across the four areas on the left since the start of the trial" +
					" into the designated areas on the right, and click Next to continue.");
			conditionPanel.setInstructionBannerText(sb.toString());		
			
			//Pause before allowing a next button press
			pauseBeforeNextTrial();
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
			
			//Pause before allowing a next button press
			pauseBeforeNextTrial();
		}		
	}	

	@Override
	public void subjectActionPerformed(SubjectActionEvent event) {
		if(event.eventType == SubjectActionEvent.NEXT_BUTTON_PRESSED) {
			//Advance to the next trial or trial part in the task
			if(!waitingToShowHits) {
				nextTrial();
			} else {
				waitingToShowHits = false;
			}
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