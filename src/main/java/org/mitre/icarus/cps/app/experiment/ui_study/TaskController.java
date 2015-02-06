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

import java.util.ArrayList;

import org.mitre.icarus.cps.app.experiment.IcarusConditionController;
import org.mitre.icarus.cps.app.experiment.TrialPartState;
import org.mitre.icarus.cps.app.experiment.ui_study.exam.ItemProbability;
import org.mitre.icarus.cps.app.experiment.ui_study.exam.UIStudyExam;
import org.mitre.icarus.cps.app.experiment.ui_study.exam.UIStudyPhase;
import org.mitre.icarus.cps.app.widgets.ui_study.ConditionPanel_UIStudy;

/**
 * @author CBONACETO
 *
 * @param <T>
 */
public abstract class TaskController<T extends UIStudyPhase<?>> extends 
	IcarusConditionController<UIStudyController, UIStudyExam, T, ConditionPanel_UIStudy> {		
		
	/** The UI Study task */
	protected T task;
		
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
	
	protected boolean adjustingNormalizedSettings = false;
	
	/** Whether to show the trial number and trial part number in the instruction banner text */
	protected static boolean showTrialInfoInBanner = false;
	
	/** The acceptable probability sum error threshold */
	protected Integer sumErrorThreshold;
		
	public abstract void initializeTaskController(T task, ConditionPanel_UIStudy conditionPanel);	
	
	@Override
	public UIStudyController getExamController() {
		return examController;
	}	
	
	@Override
	public void initializeConditionController(T condition, ConditionPanel_UIStudy conditionPanel) {
		try {
			this.task = condition;
			this.condition = condition;
			this.conditionPanel = conditionPanel;
			initializeTaskController((T)condition, (ConditionPanel_UIStudy)conditionPanel);
		} catch(Exception ex) {
			throw new IllegalArgumentException("Error initializing task controller: " + ex.toString());
		}		
	}

	@Override
	protected void performCleanup() {
		//Does nothing
	}

	public abstract T getTaskWithResponseData();
	
	public static boolean isShowTrialInfoInBanner() {
		return showTrialInfoInBanner;
	}

	public static void setShowTrialInfoInBanner(boolean showTrialInfoInBanner) {
		TaskController.showTrialInfoInBanner = showTrialInfoInBanner;
	}
	
	public static ArrayList<Integer> getItemProbabilitiesAsPercents(ArrayList<ItemProbability> itemProbabilities) {
		ArrayList<Integer> percents = new ArrayList<Integer>(itemProbabilities.size());
		for(ItemProbability item : itemProbabilities) {
			percents.add((int)(item.getProbability() * 100));
		}
		return percents;
	}
	
	public static ArrayList<Double> getItemProbabilitiesAsDoubles(ArrayList<ItemProbability> itemProbabilities) {
		ArrayList<Double> probs = new ArrayList<Double>(itemProbabilities.size());
		for(ItemProbability item : itemProbabilities) {
			probs.add(item.getProbability() * 100);
		}
		return probs;
	}

	protected StringBuilder createTrialNumberString(int trialNumber, int trialPartNumber, int numTrialParts, String trialPartName) {
		StringBuilder sb = new StringBuilder();
		if(showTrialInfoInBanner) {
			sb.append("Trial <b>");
			sb.append(Integer.toString(trialNumber) + "</b> [");
			if(numTrialParts > 1) {
				sb.append("Part <b>" + Integer.toString(trialPartNumber));
				sb.append("/" + Integer.toString(numTrialParts) + "</b>: ");
			}
			sb.append(trialPartName + "]: ");
		}
		return sb;
	}	
}