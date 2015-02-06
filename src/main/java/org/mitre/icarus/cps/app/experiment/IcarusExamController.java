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

import javax.swing.SwingUtilities;

import org.mitre.icarus.cps.app.experiment.data_recorder.ISubjectDataRecorder;
import org.mitre.icarus.cps.app.widgets.basic.tutorial.TutorialPanel;
import org.mitre.icarus.cps.app.widgets.dialog.ErrorDlg;
import org.mitre.icarus.cps.exam.base.IcarusExam;
import org.mitre.icarus.cps.exam.base.IcarusExamPhase;
import org.mitre.icarus.cps.exam.base.loader.IcarusExamLoader;
import org.mitre.icarus.cps.exam.base.training.IcarusTutorialPhase;
import org.mitre.icarus.cps.experiment_core.condition.InstructionsPage;
import org.mitre.icarus.cps.experiment_core.gui.ConditionPanel;
import org.mitre.icarus.cps.experiment_core.gui.IExperimentPanel;
import org.mitre.icarus.cps.experiment_core.gui.InstructionsPanel;

/**
 * Abstract base class for ICArUS exam data collection controllers.
 * 
 * @author CBONACETO
 *
 */
public abstract class IcarusExamController<
	E extends IcarusExam<?>, 
	P extends IcarusExamPhase,
	T extends IcarusTutorialPhase<?>,
	EP extends IExperimentPanel<?, ?, ?, ?>,
	CP extends ConditionPanel> extends IcarusExperimentController<E, P, EP, CP> {	
	
	/** The tutorial panel where exam tutorial pages are shown */
	protected TutorialPanel tutorialPanel;
	
	/** The subject data recorder to use */
	protected ISubjectDataRecorder<E, P, T> dataRecorder;
	
	protected IcarusExamController() {
		this(null);
	}
	
	protected IcarusExamController(ISubjectDataRecorder<E, P, T> dataRecorder) {		
		this.dataRecorder = dataRecorder;
		this.tutorialPanel = new TutorialPanel();
	}
	
	protected void initializeTutorialPhase(IcarusTutorialPhase<?> tutorialPhase) {
		try {
			IcarusExamLoader.initializeTutorialPhase(tutorialPhase, exam.getOriginalPath());
		} catch(Exception ex) {
			ex.printStackTrace();
			ErrorDlg.showErrorDialog(experimentPanel.getParentWindow(), 
					new Exception("Error showing tutorial", ex), true);
		}
	}
	
	protected void initializeExamPhaseTutorial(IcarusExamPhase examPhase) {
		try {
			IcarusExamLoader.initializeExamPhaseTutorial(examPhase, exam.getOriginalPath());
		} catch(Exception ex) {
			ex.printStackTrace();
			ErrorDlg.showErrorDialog(experimentPanel.getParentWindow(), 
					new Exception("Error showing tutorial", ex), true);
		}
	}
	
	/**
	 * @param pages
	 */
	protected void initializeInstructionPages(List<? extends InstructionsPage> pages) {
		try {
			IcarusExamLoader.initializeTutorialPages(pages, exam.getOriginalPath());
		} catch(Exception ex) {
			ex.printStackTrace();
			ErrorDlg.showErrorDialog(experimentPanel.getParentWindow(), 
					new Exception("Error showing tutorial", ex), true);
		}		
		/*if(pages != null && !pages.isEmpty()) {
			for(InstructionsPage page : pages) {
				if(page.getImageURL() != null && page.getPageImage() == null) {
					//Load the image for the page
					try {
						page.setPageImage(
								ImageIO.read(new URL(exam.getOriginalPath(), page.getImageURL())));
					} catch(Exception ex) {
						ex.printStackTrace();
						ErrorDlg.showErrorDialog(experimentPanel.getParentWindow(), 
								new Exception("Error showing tutorial", ex), true);
					}
				}
			}
		}*/
	}
	
	/**
	 * Show a page an instructions page to begin the given exam phase.
	 * 
	 * @param currentCondition the current exam phase to show the instructions page for
	 */
	protected void showExamPhaseInstructionsPage(final P currentCondition) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {						
				//Show exam phase instructions page												
				if(currentCondition.getInstructionText() != null) {
					if(currentCondition.getName() == null) {
						experimentPanel.setInstructionText(
								InstructionsPanel.formatTextAsHTML(currentCondition.getInstructionText() + "<br><br>Click Next to begin Mission " + Integer.toString(currentConditionIndex+1) + "."));
					} else {
						experimentPanel.setInstructionText(
								InstructionsPanel.formatTextAsHTML(currentCondition.getInstructionText() + "<br><br>Click Next to begin " + currentCondition.getName() + "."));
					}													
				} else {
					if(currentCondition.getName() == null) {
						experimentPanel.setInstructionText(
								InstructionsPanel.formatTextAsHTML("Click Next to begin Mission " + Integer.toString(currentConditionIndex+1) + "."));
					} else {
						experimentPanel.setInstructionText(
								InstructionsPanel.formatTextAsHTML("Click Next to begin " + currentCondition.getName() + "."));
					}
				}									
				experimentPanel.showInternalInstructionPanel();											
			}});
	}	

	public ISubjectDataRecorder<E, P, T> getDataRecorder() {
		return dataRecorder;
	}

	public void setDataRecorder(ISubjectDataRecorder<E, P, T> dataRecorder) {
		this.dataRecorder = dataRecorder;
	}
}