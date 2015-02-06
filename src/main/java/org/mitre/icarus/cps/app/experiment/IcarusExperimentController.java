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

import java.awt.Component;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import org.mitre.icarus.cps.app.window.controller.ApplicationController;
import org.mitre.icarus.cps.exam.base.IcarusExamPhase;
import org.mitre.icarus.cps.exam.base.testing.IcarusTestTrial;
import org.mitre.icarus.cps.experiment_core.Experiment;
import org.mitre.icarus.cps.experiment_core.condition.Condition;
import org.mitre.icarus.cps.experiment_core.condition.InstructionsPage;
import org.mitre.icarus.cps.experiment_core.controller.IExperimentController;
import org.mitre.icarus.cps.experiment_core.gui.ConditionPanel;
import org.mitre.icarus.cps.experiment_core.gui.IExperimentPanel;
import org.mitre.icarus.cps.experiment_core.gui.INavButtonPanel.ButtonType;
import org.mitre.icarus.cps.web.model.Site;

/**
 * Abstract base class for ICArUS experiment controllers (exam and exam playback controllers).
 * 
 * @author CBONACETO
 *
 * @param <E>
 * @param <C>
 * @param <EP>
 * @param <CP>
 */
public abstract class IcarusExperimentController<	 
	E extends Experiment<?>, 
	C extends Condition,
	EP extends IExperimentPanel<?, ?, ?, ?>,
	CP extends ConditionPanel> implements IExperimentController<E, C, CP> {
	
	/** Reference to the application controller */
	protected ApplicationController appController;
	
	/** The exam */
	protected E exam;
	
	/** The experiment panel (container for condition panels, nav buttons, and status banner) */
	protected EP experimentPanel;
	
	/** The condition panel */
	protected CP conditionPanel;
	
	/** The subject data for the exam */
	protected IcarusSubjectData subjectData;
	
	/** Whether the experiment is currently executing */
	protected boolean experimentRunning = false;

	/** Whether a condition is running */
	protected boolean conditionRunning = false;
	
	/** The index of the current condition */
	static public int currentConditionIndex;

	/** The current trial in the current phase */
	protected int currentTrial;	

	/** Swing worker object the experiment is executed in */
	protected SwingWorker<Object, Object> experimentWorker;
	protected boolean exitedWorker = true;

	protected boolean backButtonPressed = false;
	
	protected boolean nextButtonPressed = false;		
    
    /** Whether or not we're not debugging */
    protected boolean debug = false;    
  
    public void setApplicationController(ApplicationController appController) {
		this.appController = appController;
	}

	public Component getParentWindow() {
    	if(experimentPanel != null) {
    		return experimentPanel.getParentWindow();
    	}
    	return null;
    }    
	
	/**
	 * Set the subject ID and of the current subject.
	 * 
	 * @param subjectId the subject ID
	 * @param site the site
	 */	
	public void setSubjectData(String subjectId, Site site) {
		if(subjectData == null) {
			subjectData = new IcarusSubjectData(subjectId, site, currentConditionIndex);
		}
		else {
			subjectData.setSite(site);
			subjectData.setSubjectId(subjectId);
		}
		if(experimentPanel != null) {
			experimentPanel.setSubject(subjectData.getSubjectId());
		}
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment_core.controller.IExperimentController#skipToCondition(int)
	 */
	@Override
	public void skipToCondition(int conditionIndex) {
		//Restart the experiment at the current condition
		stopExperiment();
		if(subjectData == null) {
			subjectData = new IcarusSubjectData(null, null, conditionIndex);
		}		
		else {
			subjectData.setCurrentCondition(conditionIndex);
		}
		startExperiment(subjectData);
	}
	
	/** Get the current phase */
	public abstract IcarusExamPhase getCurrentExamPhase();
	
	/** Get the trials in the current phase */
	public abstract List<? extends IcarusTestTrial> getCurrentTrials();
	
	/** Get the index of the current phase */
	public int getCurrentConditionIndex() {
		return currentConditionIndex;
	}	
	
	/** Get the number of the current trial */	
	public int getCurrentTrialNumber() {
		return currentTrial + 1;
	}
	
	/** Restart the exam from the beginning */
	public abstract void restartExperiment();
	
	/**
	 * Get whether a condition in the exam is currently running.
	 * 
	 * @return
	 */
	public boolean isConditionRunning() {
		return conditionRunning;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment_core.controller.IExperimentController#isExperimentRunning()
	 */
	@Override
	public boolean isExperimentRunning() {
		return experimentRunning;
	}
	
	@Override
	public EP getExperimentPanel() {
		return experimentPanel;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment_core.controller.IExperimentController#setExperimentPanel(org.mitre.icarus.cps.experiment_core.gui.IExperimentPanel)
	 */
	public void setExperimentPanel(EP experimentPanel) {
		this.experimentPanel = experimentPanel;
	}
	
	/**
	 * Show/hide the external instructions window.
	 * 
	 * @param visible whether the window is visible
	 */
	public void setExternalInstructionsVisible(final boolean visible) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {							
				experimentPanel.setExternalInstructionsVisible(visible);
				//experimentPanel.setNavButtonEnabled(ButtonType.Help, buttonEnabled);
			}
		});
	}
	
	/**
	 * Show an instructions page in the experiment panel.
	 * 
	 * @param page the page to show
	 */
	protected void showInstructionsPage(final InstructionsPage page) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {							
				experimentPanel.setInstructionsPage(page);
			}
		});
	}	

	/**
	 * Set whether the given navigation button is visible.
	 * 
	 * @param button the button type
	 * @param visible whether the button is visible
	 */
	public void setNavButtonVisible(final ButtonType button, final boolean visible) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				experimentPanel.setNavButtonVisible(button, visible);
			}
		});
	}
	
	/**
	 * Get whether the given navigation button is enabled.
	 * 
	 * @param button the button type
	 * @return whether the button is enabled
	 */
	public boolean isNavButtonEnabled(ButtonType button) {
		return experimentPanel.isNavButtonEnabled(button);
	}

	/**
	 * Set whether the given navigation button is enabled.
	 * 
	 * @param button the button type
	 * @param enabled whether the button is enabled
	 */
	public void setNavButtonEnabled(final ButtonType button, final boolean enabled) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				experimentPanel.setNavButtonEnabled(button, enabled);
			}
		});
	}
	
	/**
	 * Get the exam being controlled.
	 * 
	 * @return the exam
	 */
	public E getExam() {
		return exam;
	}		
}