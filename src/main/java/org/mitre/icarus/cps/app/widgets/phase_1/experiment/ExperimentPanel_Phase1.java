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
package org.mitre.icarus.cps.app.widgets.phase_1.experiment;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;

import org.mitre.icarus.cps.app.experiment.phase_1.IcarusExamController_Phase1;
import org.mitre.icarus.cps.app.widgets.basic.BasicExperimentPanel;
import org.mitre.icarus.cps.app.widgets.basic.IcarusNavButtonPanel;
import org.mitre.icarus.cps.app.widgets.map.phase_1.MapConstants_Phase1;
import org.mitre.icarus.cps.exam.phase_1.IcarusExam_Phase1;
import org.mitre.icarus.cps.exam.phase_1.testing.TaskTestPhase;
import org.mitre.icarus.cps.experiment_core.gui.ExperimentStatusPanel;
import org.mitre.icarus.cps.experiment_core.gui.ConditionPanel.BannerOrientation;
import org.mitre.icarus.cps.experiment_core.gui.INavButtonPanel.ButtonType;

/**
 * ICArUS CPD 1 specific experiment panel implementation contains a ConditionPanel_Phase1 panel
 * and handles showing mission instructions in the external instructions window.
 * 
 * @author CBONACETO
 *
 */
public class ExperimentPanel_Phase1 extends 
	BasicExperimentPanel<IcarusExamController_Phase1, IcarusExam_Phase1, TaskTestPhase<?>, ConditionPanel_Phase1> {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * @param parent
	 * @param conditionPanel
	 * @param showReturnHomeButton
	 */
	public ExperimentPanel_Phase1(Component parent,	ConditionPanel_Phase1 conditionPanel, boolean showReturnHomeButton) {
		super(parent, conditionPanel, showReturnHomeButton);
		initialize();
	}
	
	/**
	 * @param parent
	 * @param conditionPanel
	 * @param navButtonPanel
	 * @param navButtonPanelOrientation
	 * @param statusPanel
	 * @param statusPanelOrientation
	 */
	protected ExperimentPanel_Phase1(Component parent, ConditionPanel_Phase1 conditionPanel, IcarusNavButtonPanel navButtonPanel, 
			BannerOrientation navButtonPanelOrientation, ExperimentStatusPanel statusPanel, 
			BannerOrientation statusPanelOrientation) {
		super(parent, conditionPanel, navButtonPanel, navButtonPanelOrientation, statusPanel, statusPanelOrientation);
		initialize();
	}
	
	protected void initialize() {
		externalInstructionsPanel.setInstructionsPanelPreferredSize(new Dimension(970, 700));
		((IcarusNavButtonPanel)navButtonPanel).setReviewTutorialButtonText("Review Exam Tutorial");
		navButtonPanel.setButtonText(ButtonType.Help, "Review Mission Instructions");
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setMaximumSize(java.awt.Dimension)
	 */
	@Override
	public void setMaximumSize(Dimension size) {
		Dimension mapSize = new Dimension(MapConstants_Phase1.PREFERRED_MAP_WIDTH, MapConstants_Phase1.PREFERRED_MAP_HEIGHT);
		conditionPanel.getMapPanel().setMapPreferredSize(mapSize, new Insets(1, 1, 1, 1));
		conditionPanel.getMapPanel().setMinimumSize(null);			
		int widthOver = getPreferredSize().width - size.width;
		int heightOver = getPreferredSize().height - size.height;
		if(widthOver > 0 || heightOver > 0) {
			int newWidth = (widthOver > 0) ? mapSize.width - widthOver : mapSize.width;
			if(newWidth < 20) {
				newWidth = 20;
			}
			int newHeight = (heightOver > 0) ? mapSize.height - heightOver : mapSize.height;
			if(newHeight < 20) {
				newHeight = 20;
			}
			conditionPanel.setPreferredSize(null);		
			contentPane.setPreferredSize(null);
			conditionPanel.getMapPanel().setMapPreferredSize(new Dimension(newWidth, newHeight), new Insets(1, 1, 1, 1));
			conditionPanel.getMapPanel().setMinimumSize(conditionPanel.getMapPanel().getPreferredSize());
			conditionPanel.updatePreferredSize();
		}
		contentPane.setPreferredSize(conditionPanel.getPreferredSize());
		instructionPanel.getComponent().setPreferredSize(conditionPanel.getPreferredSize());
		revalidate();
		super.setMaximumSize(size);
	}
}