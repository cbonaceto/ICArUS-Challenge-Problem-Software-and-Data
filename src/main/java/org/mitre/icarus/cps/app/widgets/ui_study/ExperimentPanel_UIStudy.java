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
package org.mitre.icarus.cps.app.widgets.ui_study;

import java.awt.Window;
import java.util.List;

import javax.swing.event.HyperlinkListener;

import org.mitre.icarus.cps.app.experiment.ui_study.UIStudyController;
import org.mitre.icarus.cps.app.experiment.ui_study.exam.UIStudyExam;
import org.mitre.icarus.cps.app.widgets.WidgetConstants;
import org.mitre.icarus.cps.app.widgets.basic.IcarusNavButtonPanel;
import org.mitre.icarus.cps.exam.base.IcarusExamPhase;
import org.mitre.icarus.cps.experiment_core.condition.InstructionsPage;
import org.mitre.icarus.cps.experiment_core.gui.ExperimentPanel;
import org.mitre.icarus.cps.experiment_core.gui.ExperimentStatusPanel;
import org.mitre.icarus.cps.experiment_core.gui.ConditionPanel.BannerOrientation;
import org.mitre.icarus.cps.experiment_core.gui.INavButtonPanel.ButtonType;

/**
 * @author CBONACETO
 *
 */
public class ExperimentPanel_UIStudy extends 
	ExperimentPanel<UIStudyController, UIStudyExam, IcarusExamPhase, ConditionPanel_UIStudy> {
	
	private static final long serialVersionUID = 1L;
	
	/** The condition panel */
	protected ConditionPanel_UIStudy conditionPanel;
	
	/** The instruction pages for the current task */
	private List<? extends InstructionsPage> taskInstructionPages;
	
	public ExperimentPanel_UIStudy(Window parent, ConditionPanel_UIStudy conditionPanel) {
		super(parent, null, new IcarusNavButtonPanel(), BannerOrientation.Top,
				new ExperimentStatusPanel(WidgetConstants.FONT_STATUS), BannerOrientation.Bottom);
		this.conditionPanel = conditionPanel;
		contentPane.setPreferredSize(conditionPanel.getPreferredSize());
		instructionPanel.getComponent().setPreferredSize(conditionPanel.getPreferredSize());				
		((IcarusNavButtonPanel)navButtonPanel).setReviewTutorialButtonText("Review Exam Tutorial");
		navButtonPanel.setButtonText(ButtonType.Help, "Review Task Tutorial");		
		statusPanel.setExperimentDescriptor("Exam");
		statusPanel.setComponentsVisible(true, true, true, true);	
		setShowStatusPanel(true);		
	}		
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setMaximumSize(java.awt.Dimension)
	 */
	/*@Override
	public void setMaximumSize(Dimension size) {
		Dimension mapSize = new Dimension(MapConstants.PREFERRED_MAP_WIDTH, MapConstants.PREFERRED_MAP_HEIGHT);
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
		instructionPanel.setPreferredSize(conditionPanel.getPreferredSize());
		revalidate();
		super.setMaximumSize(size);
	}*/

	public ConditionPanel_UIStudy getConditionPanel() {
		return conditionPanel;
	}

	public void setReviewTutorialButtonVisible(boolean visible) {
		((IcarusNavButtonPanel)navButtonPanel).setReviewTutorialButtonVisible(visible);
	}
	
	/** Show the condition panel */
	public void showConditionPanel() {
		showContent(conditionPanel);
	}

	public List<? extends InstructionsPage> getTaskInstructionPages() {
		return taskInstructionPages;
	}

	public void setTaskInstructionPages(List<? extends InstructionsPage> taskInstructionPages) {
		this.taskInstructionPages = taskInstructionPages;
	}

	/** Show the external help window with instruction pages for a task */
	@Override
	public void setExternalInstructionsVisible(boolean visible) {
		setExternalInstructionsVisible(visible, "Task Tutorial");
	}
	
	public void setExternalInstructionsVisible(boolean visible, String windowTitle) {
		super.setExternalInstructionsVisible(visible);
		if(visible && taskInstructionPages != null && !taskInstructionPages.isEmpty()) {
			externalInstructionsPanel.setInstructionsPages("Instructions", taskInstructionPages);
		}
		if(externalInstructionsWindow != null) {
			externalInstructionsWindow.setTitle(windowTitle);
		}
	}
	
	/** Add a hyperlink listener to the instruction panel */
	public void addInstructionsHyperlinkListener(HyperlinkListener listener) {
		instructionPanel.addHyperlinkListener(listener);
	}
	
	/** Remove a hyperlink listener from the instructions panel */
	public void removeInstructionsHyperlinkListener(HyperlinkListener listener) {
		instructionPanel.removeHyperlinkListener(listener);	
	}	
}