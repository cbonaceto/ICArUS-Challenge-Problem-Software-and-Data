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

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;

import org.mitre.icarus.cps.app.experiment.IcarusConditionController;
import org.mitre.icarus.cps.app.experiment.ui_study.exam.TutorialPage;
import org.mitre.icarus.cps.app.experiment.ui_study.exam.TutorialPhase;
import org.mitre.icarus.cps.app.experiment.ui_study.exam.UIStudyExam;
import org.mitre.icarus.cps.app.util.ProbabilityUtils;
import org.mitre.icarus.cps.app.widgets.basic.tutorial.TutorialPanel;
import org.mitre.icarus.cps.app.widgets.ui_study.ProbabilityEntryPanel;
import org.mitre.icarus.cps.experiment_core.event.ConditionEvent;
import org.mitre.icarus.cps.experiment_core.event.SubjectActionEvent;
import org.mitre.icarus.cps.experiment_core.gui.INavButtonPanel.ButtonType;
import org.mitre.icarus.cps.experiment_core.subject_data.SubjectConditionData;

/**
 * @author CBONACETO
 *
 */
public class TutorialController extends 
	IcarusConditionController<UIStudyController, UIStudyExam, TutorialPhase, TutorialPanel> {	
	
	/** The tutorial pages */
	private List<TutorialPage> tutorialPages;
	
	/** The current tutorial page */
	private TutorialPage currentPage;
	
	/** The current tutorial page index */
	private int currentPageIndex;		
	
	/** The probability entry panel */
	private ProbabilityEntryPanel probabilityEntryPanel;
		
	private int numProbabilityEntries;
	
	private ArrayList<Integer> initialProbabilities; 			 
	
	private ArrayList<Integer> currentProbabilities; 
	
	private ArrayList<Integer> normalizedProbabilities;			

	/**
	 * No-arg constructor.
	 */
	public TutorialController() {}
	
	/**
	 * Constructor that takes the tutorial phase and the tutorial panel.
	 * 
	 * @param tutorial the tutorial phase
	 * @param conditionPanel the tutorial panel
	 */
	public TutorialController(TutorialPhase tutorial, TutorialPanel conditionPanel) {
		initializeConditionController(tutorial, conditionPanel);	
	}
	
	@Override
	public UIStudyController getExamController() {
		return examController;
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment_core.controller.IConditionController#getCondition()
	 */
	@Override
	public TutorialPhase getCondition() {
		return condition;
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment_core.controller.IConditionController#intializeConditionController(org.mitre.icarus.cps.experiment_core.condition.Condition, org.mitre.icarus.cps.experiment_core.gui.ConditionPanel)
	 */
	@Override
	public void initializeConditionController(TutorialPhase condition, TutorialPanel conditionPanel) {
		this.condition = condition;
		this.conditionPanel = conditionPanel;
		this.conditionPanel.addSubjectActionListener(this);		
		if(this.condition.getProbabilityEntryTitles() != null) {
			numProbabilityEntries = this.condition.getProbabilityEntryTitles().size();
		}
		probabilityEntryPanel = null;
	}	

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment_core.controller.IConditionController#startCondition(int, org.mitre.icarus.cps.experiment_core.controller.IExperimentController, org.mitre.icarus.cps.experiment_core.subject_data.SubjectConditionData)
	 */
	@Override
	public void startCondition(int firstTrial, UIStudyController experimentController,
			SubjectConditionData subjectConditionData) {
		if(condition == null || conditionPanel == null) {
			throw new IllegalArgumentException("Error starting condition controller: condition and condition panel must be set");
		}
		conditionRunning = true;	
		this.examController = experimentController;
		
		initialProbabilities = ProbabilityUtils.createDefaultInitialProbabilities(numProbabilityEntries);			 
		currentProbabilities = ProbabilityUtils.createDefaultInitialProbabilities(numProbabilityEntries);
		normalizedProbabilities = ProbabilityUtils.createDefaultInitialProbabilities(numProbabilityEntries);

		tutorialPages = condition.getTutorialPages();
		currentPageIndex = 0;

		//Show the first tutorial page
		examController.setNavButtonEnabled(ButtonType.Next, true);
		examController.setNavButtonEnabled(ButtonType.Back, false);
		currentPage = tutorialPages.get(0);
		showTutorialPage(currentPage);			
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment_core.event.SubjectActionListener#subjectActionPerformed(org.mitre.icarus.cps.experiment_core.event.SubjectActionEvent)
	 */
	@Override
	public void subjectActionPerformed(SubjectActionEvent event) {
		if(!conditionRunning) {return;}
		
		try {
			if(currentPage != null && currentPage.isProbabilityPanelEditable()) {
				//Capture the current probabilities and update the normalized probabilities
				probabilityEntryPanel.getCurrentSettings(currentProbabilities);				
				ProbabilityUtils.normalizePercentProbabilities(currentProbabilities, normalizedProbabilities);
			}

			if(event.eventType == SubjectActionEvent.NEXT_BUTTON_PRESSED) {		
				currentPageIndex++;
			}
			else if(event.eventType == SubjectActionEvent.BACK_BUTTON_PRESSED) {
				currentPageIndex--;
			}

			if(currentPageIndex >= tutorialPages.size()) {
				//Fire a condition completed event
				conditionRunning = false;				
				fireConditionEvent(new ConditionEvent(ConditionEvent.CONDITION_COMPLETED, 
						condition.getCurrentTrial(), TutorialController.this));
			}
			else {
				//Show the current page page
				currentPage = tutorialPages.get(currentPageIndex);
				if(currentPageIndex == 0) {
					examController.setNavButtonEnabled(ButtonType.Back, false);
				}
				else {
					examController.setNavButtonEnabled(ButtonType.Back, currentPage.isEnableBackButton());
				}	
				showTutorialPage(currentPage);
				//Pause before allowing a next button press
				pauseBeforeNextTrial();
			} 
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	protected void showTutorialPage(TutorialPage tutorialPage) {
		//Show the current tutorial page
		try {
			if(currentPage.getWidget() != null) {
				//Show the instructions and widget panel
				ArrayList<String> titles = null;
				if(currentPage.getProbabilityEntryTitles() != null) {
					titles = currentPage.getProbabilityEntryTitles();
				} else {
					titles = condition.getProbabilityEntryTitles();
				}
				//Show the probability entry panel
				if(probabilityEntryPanel == null) {
					createProbabilityEntryPanel();
				}
				probabilityEntryPanel.setProbabilityEntryType(currentPage.getWidget(), currentPage.getModality(), 
						currentPage.getNormalizationMode(), null);
				probabilityEntryPanel.setProbabilityEntryTitles(titles);
				conditionPanel.setInstructionsWidget(probabilityEntryPanel);
				switch(currentPage.getProbabilityType()) {
				case Initial:
					probabilityEntryPanel.setCurrentSettings(initialProbabilities);
					probabilityEntryPanel.setPreviousSettings(initialProbabilities);
					break;
				case Current:
					probabilityEntryPanel.setCurrentSettings(currentProbabilities);
					probabilityEntryPanel.setPreviousSettings(initialProbabilities);
					break;
				case Normalized:
					probabilityEntryPanel.setCurrentSettings(normalizedProbabilities);
					break;
				}
				if(currentPage.isProbabilityPanelEditable()) {
					probabilityEntryPanel.showEditableProbabilities();
				} else {
					probabilityEntryPanel.showConfirmedProbabilities();
				}				
				conditionPanel.setInstructionsPage(tutorialPage);
				conditionPanel.showInstructionsAndWidgetPanel();
			}	
			else {
				//Show a full page of instructions
				conditionPanel.setInstructionsPage(currentPage);
				conditionPanel.showInstructionsPage();
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	private void createProbabilityEntryPanel() {
		probabilityEntryPanel = new ProbabilityEntryPanel(condition.getProbabilityEntryTitles(), "your percents", true);
		probabilityEntryPanel.setTopTitle("your probabilities");
		probabilityEntryPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(4, 4, 12, 4), BorderFactory.createLineBorder(Color.black)));
		probabilityEntryPanel.setBackground(Color.white);
	}

	@Override
	protected void performCleanup() {
		//Does nothing
	}
}