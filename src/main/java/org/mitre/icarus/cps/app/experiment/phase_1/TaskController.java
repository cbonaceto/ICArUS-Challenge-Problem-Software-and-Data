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
package org.mitre.icarus.cps.app.experiment.phase_1;

import java.util.Set;
import java.util.TreeSet;

import org.mitre.icarus.cps.app.experiment.IcarusConditionController;
import org.mitre.icarus.cps.app.experiment.TrialPartState;
import org.mitre.icarus.cps.app.widgets.phase_1.experiment.ConditionPanel_Phase1;
import org.mitre.icarus.cps.app.widgets.phase_1.experiment.TaskInputPanel;
import org.mitre.icarus.cps.exam.phase_1.IcarusExam_Phase1;
import org.mitre.icarus.cps.exam.phase_1.testing.TaskTestPhase;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupAttack;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupType;

/**
 *  Abstract base class for task controllers.
 * 
 * @author CBONACETO
 *
 * @param <T> the TaskTestPhase<?> the controller is for
 */
public abstract class TaskController<T extends TaskTestPhase<?>> 
	extends IcarusConditionController<IcarusExamController_Phase1, IcarusExam_Phase1, T, ConditionPanel_Phase1> {
	
	/** The task */
	protected T task;	
	
	/** The task input panel */
	protected TaskInputPanel taskInputPanel;
	
	/** Current trial start time */
	protected long trialStartTime;
	
	/** The current phase in the current trial */
	protected int trialPhase;
	
	/** The current trial part */
	protected TrialPartState currentTrialPart;
	
	/** Current trial part start time */
	protected long trialPartStartTime;
	
	/** Whether the subject is allowed to navigate back to a previous trial  */
	protected boolean enableNavigationToPreviousTrial = false;
	
	/** Whether the subject is allowed to navigate back to a trial part  */
	protected boolean enableNavigationToPreviousTrialPart = false;	
	
	/** Whether to show the score after each trial */
	protected boolean showScore = true;
	
	protected boolean adjustingNormalizedSettings = false;
	
	/** Whether to show the trial number and trial part number in the instruction banner text */
	protected static boolean showTrialInfoInBanner = false;
	
	/** All group center groups used in the task (used to populated legend at beginning of task) */
	protected Set<GroupType> groupCenterGroupsPresent = new TreeSet<GroupType>();
	
	/** All location used in the task (used to populate legend at beginning of task) */
	protected Set<GroupAttack> locationsPresent = new TreeSet<GroupAttack>();	

	@Override
	public void initializeConditionController(T condition, ConditionPanel_Phase1 conditionPanel) {		
		try {
			this.task = condition;
			this.condition = condition;
			this.conditionPanel = conditionPanel;
			this.taskInputPanel = conditionPanel != null ? conditionPanel.getTaskInputPanel() : null;
			initializeTask(condition);
			//initializeTaskController(condition, conditionPanel);
		} catch(Exception ex) {
			throw new IllegalArgumentException("Error initializing task controller: " + ex.toString());
		}		
	}
	
	protected abstract void initializeTask(T task);

	public abstract T getTaskWithResponseData();
	
	public static boolean isShowTrialInfoInBanner() {
		return showTrialInfoInBanner;
	}

	public static void setShowTrialInfoInBanner(boolean showTrialInfoInBanner) {
		TaskController.showTrialInfoInBanner = showTrialInfoInBanner;
	}

	@Override
	protected StringBuilder createTrialNumberString(int trialNumber, int trialPartNumber, int numTrialParts, String trialPartName) {
		return showTrialInfoInBanner ? 
				super.createTrialNumberString(trialNumber, trialPartNumber, numTrialParts, trialPartName) : new StringBuilder();		
	}	
}