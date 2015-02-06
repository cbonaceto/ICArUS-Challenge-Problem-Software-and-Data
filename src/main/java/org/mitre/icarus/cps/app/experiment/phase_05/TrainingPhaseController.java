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
package org.mitre.icarus.cps.app.experiment.phase_05;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.mitre.icarus.cps.app.experiment.phase_05.ExamTiming;
import org.mitre.icarus.cps.app.widgets.phase_05.experiment.ConditionPanel_Phase05;
import org.mitre.icarus.cps.exam.base.response.TrainingTrialResponse;
import org.mitre.icarus.cps.exam.phase_05.IcarusExam_Phase05;
import org.mitre.icarus.cps.exam.phase_05.response.IcarusTrainingPhaseResponse;
import org.mitre.icarus.cps.exam.phase_05.training.IcarusTrainingPhase;
import org.mitre.icarus.cps.experiment_core.event.ConditionEvent;
import org.mitre.icarus.cps.experiment_core.event.SubjectActionEvent;
import org.mitre.icarus.cps.experiment_core.gui.INavButtonPanel.ButtonType;
import org.mitre.icarus.cps.experiment_core.subject_data.SubjectConditionData;

/**
 * Controller for training phase of an exam.
 * 
 * @author CBONACETO
 *
 */
public class TrainingPhaseController extends IcarusConditionController_Phase05<IcarusTrainingPhase> {	
	
	/** Whether the subject is allowed to navigate back to a previous trial  */
	private boolean enableNavigationToPreviousTrial = true;
	
	public TrainingPhaseController(IcarusExam_Phase05 exam) {
		this.exam = exam;
	}
	
	public IcarusExam_Phase05 getExam() {
		return exam;
	}

	public void setExam(IcarusExam_Phase05 exam) {
		this.exam = exam;
	}
	
	@Override
	public IcarusExamController_Phase05 getExamController() {
		return examController;
	}
	
	@Override
	public IcarusTrainingPhase getCondition() {
		return condition;
	}	

	public boolean isEnableNavigationToPreviousTrial() {
		return enableNavigationToPreviousTrial;
	}

	public void setEnableNavigationToPreviousTrial(
			boolean enableNavigationToPreviousTrial) {
		this.enableNavigationToPreviousTrial = enableNavigationToPreviousTrial;
	}

	@Override
	public void initializeConditionController(IcarusTrainingPhase condition, ConditionPanel_Phase05 conditionPanel) {		
		this.condition = condition;
		this.conditionPanel = conditionPanel;
	}

	@Override
	public void startCondition(int firstTrial,
			IcarusExamController_Phase05 experimentController,
			SubjectConditionData subjectConditionData) {
		
		if(!(experimentController instanceof IcarusExamController_Phase05)) {
			throw new IllegalArgumentException("Experiment controller must be an Icarus Exam Controller!");
		}		
		conditionRunning = true;		
		this.examController = (IcarusExamController_Phase05)experimentController;
		
		if(examController != null) {
			examController.setNavButtonEnabled(ButtonType.Next, false);
		}
		
		setCurrentTrainingTrial(firstTrial);
		
		//Pause before allowing a next button press
		pauseBeforeNextTrial();		
	}
	
	/**
	 * Sets the training trial. Should be called whenever the trial changes
	 */
	private void setCurrentTrainingTrial(int trialNum) {
		
		currentTrial = trialNum;
		
		if(examController != null) {
			if(enableNavigationToPreviousTrial)  {
				examController.setNavButtonEnabled(ButtonType.Back, currentTrial > 0);					
			}
		}
		
		conditionPanel.showTrainingTrial(condition.getTrainingTrials().get(currentTrial), exam,
				currentTrial+1);
				
		//Fire trial changed event
		super.setTrial(trialNum, -1, -1);
	}

	@Override
	public void subjectActionPerformed(SubjectActionEvent event) {
		
		if (event.eventType == SubjectActionEvent.NEXT_BUTTON_PRESSED) {			
			if (currentTrial + 1 == condition.getNumTrials()) {
				//We're at the end of the training phase
				
				//TODO: May want to record time spent in training phase and on each trial
				
				//Confirm whether user wants to finish training phase
				if(JOptionPane.showConfirmDialog(conditionPanel, 
						"Would you like to leave training and continue to the next phase?", "Leave Training?", 
						JOptionPane.YES_NO_OPTION, 
						JOptionPane.QUESTION_MESSAGE) ==
					JOptionPane.YES_OPTION) {					
					//User chose to leave training phase
					
					conditionRunning = false;					
					examController.setNavButtonEnabled(ButtonType.Back, false);	

					// Fire condition completed event
					fireConditionEvent(new ConditionEvent(ConditionEvent.CONDITION_COMPLETED, 
							currentTrial + 1, this));
				}
			}
			else {
				if(examController != null) {
					examController.setNavButtonEnabled(ButtonType.Next, false);
				}
				
				//Go to the next training trial
				setCurrentTrainingTrial(currentTrial + 1);
				
				//Pause before allowing a next button press
				pauseBeforeNextTrial();				
			}					
		} 
		else if (event.eventType == SubjectActionEvent.BACK_BUTTON_PRESSED) {
			if(currentTrial > 0) {				
				//Go to the previous training trial
				setCurrentTrainingTrial(currentTrial - 1);
				
				if(enableNavigationToPreviousTrial)  {
					examController.setNavButtonEnabled(ButtonType.Back, currentTrial > 0);					
				}
			}
		}
	}	
	
	/**
	 * Generates response data from the training states
	 */
	public IcarusTrainingPhaseResponse getResponseData(int phase, int numTrials, ExamTiming examTiming) {
		long elapsedTime = 0;
		
		//System.out.println("TrainingPhaseController.getResponseData phase= " + phase + " numTrials= " + numTrials);
		ArrayList<TrainingTrialResponse> responses = new ArrayList<TrainingTrialResponse>(2);
		
		for(int i=0; i < numTrials; i++){
			TrainingTrialResponse trial = new TrainingTrialResponse();
			elapsedTime = examTiming.getTiming(phase, i);
			trial.setTrialNum(i);
			trial.setTrialTime_ms(elapsedTime);
			responses.add(trial);
		}
		IcarusTrainingPhaseResponse response = new IcarusTrainingPhaseResponse();
		response.setPhaseName("Training");
		response.setTrialResponses(responses);

		return response;
	}
}
