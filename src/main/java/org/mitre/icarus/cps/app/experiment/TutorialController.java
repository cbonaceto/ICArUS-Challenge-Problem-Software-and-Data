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
package org.mitre.icarus.cps.app.experiment;

import java.util.List;

import org.mitre.icarus.cps.app.widgets.basic.tutorial.TutorialPanel;
import org.mitre.icarus.cps.exam.base.training.IcarusTutorialPhase;
import org.mitre.icarus.cps.experiment_core.Experiment;
import org.mitre.icarus.cps.experiment_core.condition.InstructionsPage;
import org.mitre.icarus.cps.experiment_core.event.ConditionEvent;
import org.mitre.icarus.cps.experiment_core.event.SubjectActionEvent;
import org.mitre.icarus.cps.experiment_core.gui.INavButtonPanel.ButtonType;
import org.mitre.icarus.cps.experiment_core.subject_data.SubjectConditionData;

/**
 * Base class for exam tutorial controllers. Allows subject to navigate through a list of tutorial pages.
 * 
 * @author CBONACETO
 *
 * @param <EC>
 * @param <E>
 * @param <I>
 * @param <T>
 */
public abstract class TutorialController<
	EC extends IcarusExperimentController<E, ?, ?, ?>,
	E extends Experiment<?>,
	I extends InstructionsPage,
	T extends IcarusTutorialPhase<I>>
	extends IcarusConditionController<EC, E, T, TutorialPanel> {

	/** The tutorial pages */
	private List<I> tutorialPages;	

	/** The current tutorial page */
	private I currentPage;
	
	/** Whether to pause between next button presses */
	private boolean pauseBeforeNextButtonPress = false;

	/**
	 * No-arg constructor.
	 */
	public TutorialController() {}

	/**
	 * Constructor that takes the tutorial and the tutorial panel.
	 * 
	 * @param tutorial the tutorial
	 * @param conditionPanel the tutorial panel
	 */
	public TutorialController(T tutorial, TutorialPanel conditionPanel) {
		initializeConditionController(tutorial, conditionPanel);	
	}	

	@Override
	public void initializeConditionController(T condition, TutorialPanel conditionPanel) {
		this.condition = condition;
		this.conditionPanel = conditionPanel;
		initializeTutorial();
	}
	
	public boolean isPauseBeforeNextButtonPress() {
		return pauseBeforeNextButtonPress;
	}

	public void setPauseBeforeNextButtonPress(boolean pauseBeforeNextButtonPress) {
		this.pauseBeforeNextButtonPress = pauseBeforeNextButtonPress;
	}

	/**
	 * Subclasses may override this method to perform additional initialization actions.
	 */
	protected abstract void initializeTutorial();

	@Override
	public void startCondition(int firstTrial, EC experimentController, SubjectConditionData subjectConditionData) {
		if(condition == null || conditionPanel == null) {
			throw new IllegalArgumentException("Error starting tutorial controller: tutorial condition panel must be set");
		}
		conditionRunning = true;	
		this.examController = experimentController;		

		tutorialPages = condition.getTutorialPages();
		currentTrial = 0;
		tutorialStarting();

		//Show the first tutorial page
		examController.setNavButtonEnabled(ButtonType.Next, true);
		examController.setNavButtonEnabled(ButtonType.Back, false);
		currentPage = tutorialPages.get(0);
		showCurrentPage();
		
		//Fire a trial changed event
		fireConditionEvent(new ConditionEvent(ConditionEvent.TRIAL_CHANGED, 
				currentTrial, TutorialController.this));
	}
	
	/**
	 * Subclasses may override this method to perform additional actions when the tutorial starts.
	 */
	protected abstract void tutorialStarting();

	@Override
	protected void performCleanup() {
		//Does nothing
	}

	@Override
	public void subjectActionPerformed(SubjectActionEvent event) {
		if(!conditionRunning) {return;}

		try {			
			tutorialPageChanging(currentPage);
			
			if(event.eventType == SubjectActionEvent.NEXT_BUTTON_PRESSED) {		
				currentTrial++;
			} else if(event.eventType == SubjectActionEvent.BACK_BUTTON_PRESSED) {
				currentTrial--;
			}

			if(currentTrial >= tutorialPages.size()) {
				//Fire a condition completed event
				conditionRunning = false;				
				fireConditionEvent(new ConditionEvent(ConditionEvent.CONDITION_COMPLETED, 
						currentTrial, TutorialController.this));
			} else {				
				//Show the current page page
				currentPage = tutorialPages.get(currentTrial);
				if(currentTrial == 0) {
					examController.setNavButtonEnabled(ButtonType.Back, false);
				} else {
					//examController.setNavButtonEnabled(ButtonType.Back, currentPage.isEnableBackButton());
					examController.setNavButtonEnabled(ButtonType.Back, true);
				}
				showCurrentPage();
				
				//Fire a trial changed event
				fireConditionEvent(new ConditionEvent(ConditionEvent.TRIAL_CHANGED, 
						currentTrial, TutorialController.this));
				
				//Pause before allowing a next button press
				if(pauseBeforeNextButtonPress) {
					pauseBeforeNextTrial();
				}
			} 
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Subclasses may override this method to perform additional actions before the 
	 * current tutorial page changes.
	 * 	
	 * @param currentPage the current tutorial page that will change
	 */
	protected abstract void tutorialPageChanging(I currentPage);

	/**
	 * Show the current tutorial page
	 * 
	 * @param tutorialPage
	 */
	private void showCurrentPage() {
		try {			
			conditionPanel.setInstructionsPage(currentPage);
			tutorialPageChanged(currentPage);
			conditionPanel.showInstructionsAndWidgetPanel();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Subclasses may override this method to perform additional actions when the tutorial page
	 * is changed.
	 * 
	 * @param newPage the new current tutorial page 
	 */
	protected abstract void tutorialPageChanged(I newPage);
}