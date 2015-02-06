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

import javax.swing.JComponent;

/**
 * Interface for experiment status panel implementations.
 * 
 * Experiments status panels typically show the current condition, trial number, 
 * and subject ID.
 * 
 * @author CBONACETO
 *
 */
public interface IExperimentStatusPanel {
	public JComponent getExperimentStatusPanelComponent();
	
	public String getExperimentDescriptor();
	public void setExperimentDescriptor(String experimentDescriptor);

	public String getConditionDescriptor();
	public void setConditionDescriptor(String conditionDescriptor);

	public String getTrialDescriptor();
	public void setTrialDescriptor(String trialDescriptor);

	public String getSubjectDescriptor();
	public void setSubjectDescriptor(String subjectDescriptor);	
	
	public void setExperimentInfo(String experimentName);
	
	public void setConditionInfo(int currentCondition, int numConditions);
	
	public void setConditionInfo(int currentCondition, int numConditions, String conditionName);
	
	public void setTrialInfo(int currentTrial, int numTrials);
	
	public void setTrialInfo(int currentTrial, int numTrials, int currentTrialPart, int numTrialParts);
	
	public void setSubjectInfo(String subject);
	
	public void setComponentsVisible(
			boolean experimentLabelVisible, 
			boolean conditionLabelVisible, 
			boolean trialLabelVisible, 
			boolean subjectLabelVisible);
}
