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
package org.mitre.icarus.cps.experiment_core.gui;

import java.awt.Component;
import java.net.URL;

import javax.swing.JComponent;

import org.mitre.icarus.cps.experiment_core.Experiment;
import org.mitre.icarus.cps.experiment_core.condition.Condition;
import org.mitre.icarus.cps.experiment_core.condition.ConditionConfiguration;
import org.mitre.icarus.cps.experiment_core.condition.InstructionsPage;
import org.mitre.icarus.cps.experiment_core.controller.IExperimentController;
import org.mitre.icarus.cps.experiment_core.event.SubjectActionListener;
import org.mitre.icarus.cps.experiment_core.gui.INavButtonPanel.ButtonType;

/**
 * Interface for experiment panel implementations.
 * 
 * @author CBONACETO
 *
 */
public interface IExperimentPanel<
	EC extends IExperimentController<E, ?, ?>,
	E extends Experiment<?>,
	C extends Condition, 
	CP extends ConditionPanel> {
	
	public Component getParentWindow();
	
	public JComponent getExperimentPanelComponent();
	
	public void setShowStatusPanel(boolean showStatusPanel);
	
	public boolean isShowStatusPanel();
	
	public void showCondition(String conditionId);
	
	public void clearCurrentConfiguration();
	
	public void showCondition(ConditionConfiguration<EC, E, C, CP> conditionConfiguration);
	
	/** Show content in the content pane */
	public void showContent(JComponent content);
	
	public void setExternalInstructionsVisible(boolean visible);
	
	public void showInternalInstructionPanel();
	
	public void setInstructionsPage(InstructionsPage page);
	
	public void setInstructionsURL(URL url);
	
	public void setInstructionText(String html);
	
	public void setNavButtonText(ButtonType button, String text);
	
	public void setNavButtonFocused(ButtonType button);
	
	public void setNavButtonVisible(ButtonType button, boolean visible);
	
	public boolean isNavButtonEnabled(ButtonType button);
	
	public void setNavButtonEnabled(ButtonType button, boolean enabled);
	
	public void setExperimentName(String experimentName);	
	
	public void setSubject(String subject);
	
	public void setNumConditions(int numConditions);
	
	public void setConditionNumber(int conditionNumber, int numTrials);
	
	public void setConditionNumber(String conditionName, int conditionNumber, int numTrials);
	
	public void setTrialDescriptor(String trialDescriptor);
	
	public void setTrialNumber(int trialNumber);	
	
	public void setTrialNumber(int trialNumber, int trialPartNumber, int numTrialParts);
	
	public boolean isSubjectActionListenerPresent(SubjectActionListener listener);

	/** Add a listener to be notified when a nav button is pressed */
	public void addSubjectActionListener(SubjectActionListener listener);
	
	/** Remove a nav button press listener */
	public void removeSubjectActionListener(SubjectActionListener listener);
}