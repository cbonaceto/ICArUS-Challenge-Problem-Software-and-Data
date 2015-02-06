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
package org.mitre.icarus.cps.app.widgets.phase_2.experiment;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;

import org.mitre.icarus.cps.app.experiment.phase_2.IcarusExamController_Phase2;
import org.mitre.icarus.cps.app.widgets.basic.BasicExperimentPanel;
import org.mitre.icarus.cps.app.widgets.basic.IcarusNavButtonPanel;
import org.mitre.icarus.cps.exam.phase_2.IcarusExam_Phase2;
import org.mitre.icarus.cps.exam.phase_2.testing.Mission;
import org.mitre.icarus.cps.experiment_core.gui.ConditionPanel.BannerOrientation;
import org.mitre.icarus.cps.experiment_core.gui.INavButtonPanel.ButtonType;

/**
 * Phase 2 specific experiment panel implementation contains a ConditionPanel_Phase2 panel.
 * 
 * @author CBONACETO
 *
 */ 
public class ExperimentPanel_Phase2 extends 
	BasicExperimentPanel<IcarusExamController_Phase2, IcarusExam_Phase2, Mission<?>, ConditionPanel_Phase2> {
	
	private static final long serialVersionUID = -6832203121221433791L;

	private boolean externalInstructionsPanelShown = false;

	/**
	 * @param parent
	 * @param conditionPanel
	 * @param showReturnHomeButton
	 */
	public ExperimentPanel_Phase2(Component parent, ConditionPanel_Phase2 conditionPanel, boolean showReturnHomeButton) {
		super(parent, conditionPanel, showReturnHomeButton);
		initialize();
	}
	
	/**
	 * @param parent
	 * @param conditionPanel
	 * @param showReturnHomeButton
	 * @param navButtonPanelOrientation
	 * @param statusPanelOrientation
	 */
	public ExperimentPanel_Phase2(Component parent, ConditionPanel_Phase2 conditionPanel, boolean showReturnHomeButton,
			BannerOrientation navButtonPanelOrientation, BannerOrientation statusPanelOrientation) {
		super(parent, conditionPanel, showReturnHomeButton, 
				navButtonPanelOrientation, statusPanelOrientation);
		initialize();
	}
	
	protected void initialize() {
		externalInstructionsPanel.setInstructionsPanelPreferredSize(new Dimension(843, 700));
		((IcarusNavButtonPanel)navButtonPanel).setReviewTutorialButtonText("Exam Tutorial");
		navButtonPanel.setButtonText(ButtonType.Help, "Mission Instructions");
	}

	@Override
	public void setExternalInstructionsVisible(boolean visible) {
		if(visible && !externalInstructionsPanelShown) {
			boolean navigationTreeVisible = externalInstructionsPanel.isTutorialNavPanelVisible();
			externalInstructionsPanel.setTutorialNavPanelVisible(true);
			super.setExternalInstructionsVisible(visible);
			externalInstructionsPanel.setTutorialNavPanelVisible(navigationTreeVisible);
			externalInstructionsPanelShown = true;
		} else {
			super.setExternalInstructionsVisible(visible);
		}
	}
	
	@Override
	public void setExternalInstructionsVisible(boolean visible, String windowTitle, Image windowIcon) {
		if(visible && !externalInstructionsPanelShown) {
			boolean navigationTreeVisible = externalInstructionsPanel.isTutorialNavPanelVisible();
			externalInstructionsPanel.setTutorialNavPanelVisible(true);
			super.setExternalInstructionsVisible(visible, windowTitle, windowIcon);
			externalInstructionsPanel.setTutorialNavPanelVisible(navigationTreeVisible);
			externalInstructionsPanelShown = true;
		} else {
			super.setExternalInstructionsVisible(visible, windowTitle, windowIcon);
		}
	}	
}