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
package org.mitre.icarus.cps.app.widgets.phase_05.experiment;

import java.awt.Dimension;
import java.awt.Window;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.event.HyperlinkListener;

import org.mitre.icarus.cps.app.experiment.phase_05.IcarusExamController_Phase05;
import org.mitre.icarus.cps.app.widgets.WidgetConstants;
import org.mitre.icarus.cps.app.widgets.basic.BasicExperimentPanel;
import org.mitre.icarus.cps.app.widgets.basic.IcarusNavButtonPanel;
import org.mitre.icarus.cps.exam.base.IcarusExamPhase;
import org.mitre.icarus.cps.exam.phase_05.IcarusExam_Phase05;
import org.mitre.icarus.cps.exam.phase_05.training.IcarusTrainingPhase;
import org.mitre.icarus.cps.exam.phase_05.training.IcarusTrainingTrial;
import org.mitre.icarus.cps.exam.phase_05.training.Rule;
import org.mitre.icarus.cps.exam.phase_05.training.RuleTrainingTrial;
import org.mitre.icarus.cps.exam.phase_05.training.IcarusTrainingTrial.TrainingTrialType;
import org.mitre.icarus.cps.experiment_core.condition.InstructionsPage;
import org.mitre.icarus.cps.experiment_core.gui.ExperimentStatusPanel;
import org.mitre.icarus.cps.experiment_core.gui.InstructionNavigationPanel;
import org.mitre.icarus.cps.experiment_core.gui.ConditionPanel.BannerOrientation;
import org.mitre.icarus.cps.experiment_core.gui.INavButtonPanel.ButtonType;

/**
 * ICArUS specific experiment panel implementation simply contains an IcarusConditionPanel
 * and handles showing training rules in the external instructions window.
 * 
 * @author CBONACETO
 *
 */
public class ExperimentPanel_Phase05 extends
	BasicExperimentPanel<IcarusExamController_Phase05, IcarusExam_Phase05, IcarusExamPhase, ConditionPanel_Phase05> {
		
	private static final long serialVersionUID = 1L;	
	
	/** List of all rules that have been seen in training */
	private List<Rule> rules;
	
	/** Instructions page with rules from each training session */
	private ArrayList<InstructionsPage> rulesPages;
	
	public ExperimentPanel_Phase05(Window parentWindow, ConditionPanel_Phase05 conditionPanel) {
		super(parentWindow, conditionPanel, new IcarusNavButtonPanel(), BannerOrientation.Bottom,
				new ExperimentStatusPanel(WidgetConstants.FONT_STATUS), BannerOrientation.Top);
		contentPane.setPreferredSize(conditionPanel.getPreferredSize());
		instructionPanel.getComponent().setPreferredSize(conditionPanel.getPreferredSize());		
		rules = new LinkedList<Rule>();
		rulesPages = new ArrayList<InstructionsPage>();		
		navButtonPanel.setButtonText(ButtonType.Help, "Review Rules");		
		statusPanel.setExperimentDescriptor("Exam");
		statusPanel.setComponentsVisible(true, true, true, true);
		setShowStatusPanel(true);		
	}	
	
	public void setReviewTutorialButtonVisible(boolean visible) {
		((IcarusNavButtonPanel)navButtonPanel).setReviewTutorialButtonVisible(visible);
	}
	
	/** Show the condition panel */
	public void showConditionPanel() {
		showContent(conditionPanel);
	}
	
	/** Add any rules in the given collection of training trials to be shown 
	 * in the external help panel when the help button is clicked */
	public void addRulesFromTraining(IcarusTrainingPhase trainingPhase) {
		if(trainingPhase != null && trainingPhase.getTrainingTrials() != null) {
			List<Rule> newRules = new LinkedList<Rule>();
			for(IcarusTrainingTrial training : trainingPhase.getTrainingTrials()) {
				if(training.getTrainingTrialType() == TrainingTrialType.Rule) {
					List<Rule> rulesToAdd = ((RuleTrainingTrial)training).getRules();
					if(rulesToAdd != null) {
						for(Rule rule : rulesToAdd) {
							newRules.add(rule);
							rules.add(rule);
						}
					}
				}
			}
			if(!newRules.isEmpty()) {
				InstructionsPage newRulesPage = new InstructionsPage();
				newRulesPage.setPageText(createRulesPage(trainingPhase.getName() + " Rules", newRules));
				rulesPages.add(newRulesPage);
			}
		}
	}
	
	/** Format a set of rules as an HTML text block to show in an instructions panel */
	public static String createRulesPage(String title, Collection<Rule> rules) {
		StringBuilder sb = new StringBuilder("<html><br><br>");
		
		sb.append("<font face=\"");
		sb.append(WidgetConstants.FONT_INSTRUCTION_PANEL.getName() + "\" size=\"");
		sb.append(WidgetConstants.FONT_SIZE_HTML + "\">");
				
		if(title != null) {
			sb.append("<center>");
			sb.append(title);
			sb.append("</center><br><br>");
		}		
		sb.append("<left>");
		
		Integer i = 1;
		if(rules != null && !rules.isEmpty()) {
			for(Rule rule : rules) {
				sb.append("&nbsp&nbsp&nbsp&nbsp ");
				sb.append(i + ". ");
				sb.append(rule.getRuleText());
				sb.append("<br>");
				i++;
			}
		}
		
		sb.append("</left>");
		sb.append("</font></html>");
		//System.out.println(sb.toString());		
		return sb.toString();
	}
	
	/** Remove any rules that have been added */
	public void clearRules() {
		rules.clear();
		rulesPages.clear();
	}
	
	/** Return true if there are no rules */
	public boolean rulesEmpty() {
		return rules.isEmpty();
	}

	/** Show the external help window with rules that have been seen so far */
	@Override
	public void setExternalInstructionsVisible(boolean visible) {
		if(externalInstructionsWindow == null && visible) {
			externalInstructionsWindow = new JFrame("Rules");
			externalInstructionsWindow.setResizable(false);
			externalInstructionsWindow.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			externalInstructionsPanel = new InstructionNavigationPanel();
			Dimension size = instructionPanel.getComponent().getPreferredSize();			
			externalInstructionsPanel.getComponent().setPreferredSize(
					new Dimension(size.width - 100, size.height));
			externalInstructionsWindow.getContentPane().add(externalInstructionsPanel.getComponent());			
			externalInstructionsWindow.pack();	
			if(parentWindow != null) {
				externalInstructionsWindow.setLocationRelativeTo(parentWindow);
			}
		}		
		if(visible && !rulesPages.isEmpty()) {		
			externalInstructionsPanel.setInstructionsPages("", rulesPages);
		}
		if(externalInstructionsWindow != null) {
			externalInstructionsWindow.setVisible(visible);
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