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
package org.mitre.icarus.cps.app.widgets.basic;

import java.awt.Component;
import java.awt.Image;
import java.util.List;

import org.mitre.icarus.cps.app.widgets.ImageManager;
import org.mitre.icarus.cps.app.widgets.WidgetConstants;
import org.mitre.icarus.cps.app.widgets.basic.tutorial.TutorialNavigationAndNavButtonPanel;
import org.mitre.icarus.cps.exam.base.training.TutorialNavigationTree;
import org.mitre.icarus.cps.experiment_core.Experiment;
import org.mitre.icarus.cps.experiment_core.condition.Condition;
import org.mitre.icarus.cps.experiment_core.condition.InstructionsPage;
import org.mitre.icarus.cps.experiment_core.controller.IExperimentController;
import org.mitre.icarus.cps.experiment_core.gui.ConditionPanel;
import org.mitre.icarus.cps.experiment_core.gui.ExperimentPanel;
import org.mitre.icarus.cps.experiment_core.gui.ExperimentStatusPanel;
import org.mitre.icarus.cps.experiment_core.gui.ConditionPanel.BannerOrientation;
import org.mitre.icarus.cps.experiment_core.gui.INavButtonPanel.ButtonType;

/**
 * Base class for Phase 1-3 experiment panels. Handles showing mission instructions 
 * in the external instructions window.
 * 
 * @author CBONACETO
 *
 * @param <T>
 */
public class BasicExperimentPanel<
	EC extends IExperimentController<E, ?, ?>,
	E extends Experiment<?>,
	C extends Condition, 
	CP extends ConditionPanel> 
	extends ExperimentPanel<EC, E, C, CP> {	
	
	private static final long serialVersionUID = -1293576630929629036L;
	
	/** The condition panel */
	protected CP conditionPanel;
	
	/**
	 * @param parent
	 * @param conditionPanel
	 * @param showReturnHomeButton
	 */
	public BasicExperimentPanel(Component parent, CP conditionPanel, boolean showReturnHomeButton) {
		this(parent, conditionPanel, showReturnHomeButton, BannerOrientation.Top, BannerOrientation.Top);
	}
	
	public BasicExperimentPanel(Component parent, CP conditionPanel, boolean showReturnHomeButton,
			BannerOrientation navButtonPanelOrientation, BannerOrientation statusPanelOrientation) {
		this(parent, conditionPanel, new IcarusNavButtonPanel(), navButtonPanelOrientation, 
				new ExperimentStatusPanel(WidgetConstants.FONT_STATUS), statusPanelOrientation);
		((IcarusNavButtonPanel)navButtonPanel).setReviewTutorialButtonText("Review Exam Tutorial");
		navButtonPanel.setButtonText(ButtonType.Help, "Review Mission Instructions");
		if(showReturnHomeButton) {
			navButtonPanel.setButtonText(ButtonType.Exit, "Return Home");
			navButtonPanel.setButtonIcon(ButtonType.Exit, ImageManager.getImageIcon(ImageManager.HOME_ICON));
			navButtonPanel.setButtonEnabled(ButtonType.Exit, true);
			navButtonPanel.setButtonVisible(ButtonType.Exit, true);
		}
		statusPanel.setExperimentDescriptor("Exam");
		statusPanel.setComponentsVisible(true, true, true, true);
	}
	
	/**
	 * @param parent
	 * @param conditionPanel
	 * @param navButtonPanel
	 * @param navButtonPanelOrientation
	 * @param statusPanel
	 * @param statusPanelOrientation
	 */
	protected BasicExperimentPanel(Component parent, CP conditionPanel, IcarusNavButtonPanel navButtonPanel, 
			BannerOrientation navButtonPanelOrientation, ExperimentStatusPanel statusPanel, 
			BannerOrientation statusPanelOrientation) {
		super(parent, null, navButtonPanel, navButtonPanelOrientation, statusPanel, 
				statusPanelOrientation, new TutorialNavigationAndNavButtonPanel());
		this.conditionPanel = conditionPanel;
		contentPane.setPreferredSize(conditionPanel.getPreferredSize());
		instructionPanel.getComponent().setPreferredSize(conditionPanel.getPreferredSize());
		setShowStatusPanel(true);
	}
	
	public void setReviewTutorialButtonText(String text) {
		((IcarusNavButtonPanel)navButtonPanel).setReviewTutorialButtonText(text);
	}
	
	public void setReviewTutorialButtonEnabled(boolean enabled) {
		((IcarusNavButtonPanel)navButtonPanel).setReviewTutorialButtonEnabled(enabled);
	}
	
	public void setReviewTutorialButtonVisible(boolean visible) {
		((IcarusNavButtonPanel)navButtonPanel).setReviewTutorialButtonVisible(visible);
	}

	public CP getConditionPanel() {
		return conditionPanel;
	}	
	
	/** Show the condition panel */
	public void showConditionPanel() {
		showContent(conditionPanel);
	}
	
	/*public List<? extends InstructionsPage> getInstructionPages() {
	//return taskInstructionPages;
	
	}*/
	
	public String getInstructionsPagesId() {
		return externalInstructionsPanel != null ? externalInstructionsPanel.getInstructionsId() : null; 
	}
	
	public void addInstructionsPages(String pagesName, List<? extends InstructionsPage> instructionsPages) {
		addInstructionsPages(pagesName, instructionsPages, null, false);
	}
	
	public void addInstructionsPages(String pagesName, List<? extends InstructionsPage> instructionsPages,
			TutorialNavigationTree navigationTree, boolean showNameNode) {
		if(externalInstructionsPanel != null) {
			externalInstructionsPanel.setInstructionsId(null);
			externalInstructionsPanel.addInstructionsPages(pagesName, instructionsPages, navigationTree, 
					showNameNode);
		}
	}
	
	public void setInstructionsPages(String pagesName, String pagesId, List<? extends InstructionsPage> instructionsPages) {
		setInstructionsPages(pagesName, pagesId, instructionsPages, null, false);
	}	

	public void setInstructionsPages(String pagesName, String pagesId, List<? extends InstructionsPage> instructionsPages,
			TutorialNavigationTree navigationTree, boolean showNameNode) {
		//this.taskInstructionPages = taskInstructionPages;
		if(externalInstructionsPanel != null) {
			externalInstructionsPanel.setInstructionsId(pagesId);
			externalInstructionsPanel.setInstructionsPages(pagesName, instructionsPages, navigationTree, showNameNode);
			if(navigationTree != null) {
				externalInstructionsPanel.expandNavigationTree();
			}
		}
	}	

	/*public void removeInstructionsPage(InstructionsPage page) {
		if(externalInstructionsPanel != null) {
			
		}
	}*/
	
	public void removeAllInstructionPages() {
		if(externalInstructionsPanel != null) {
			externalInstructionsPanel.setInstructionsId(null);
			externalInstructionsPanel.removeAllInstructionsPages();
		}
	}	

	/** Show the external help window with instruction pages for a task */
	@Override
	public void setExternalInstructionsVisible(boolean visible) {
		setExternalInstructionsVisible(visible, "", null);
		//setExternalInstructionsVisible(visible, "Mission Instructions", null);
	}
	
	public void setExternalInstructionsVisible(boolean visible, String windowTitle, Image windowIcon) {
		super.setExternalInstructionsVisible(visible);
		/*if(visible && taskInstructionPages != null && !taskInstructionPages.isEmpty()) {
			externalInstructionsPanel.setInstructionsPages(taskInstructionPages);
		}*/
		if(externalInstructionsWindow != null) {
			if(visible) {
				externalInstructionsWindow.setTitle(windowTitle);
				if(windowIcon != null) {
					externalInstructionsWindow.setIconImage(windowIcon);
				}
			}
		}
	}	
}