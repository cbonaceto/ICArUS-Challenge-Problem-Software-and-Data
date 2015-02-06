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

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

/**
 * Default experiment status panel implementation.
 * 
 * @author CBONACETO
 *
 */
public class ExperimentStatusPanel extends JPanel implements IExperimentStatusPanel {
	private static final long serialVersionUID = 1L;
	
	/** Label showing the experiment name */
	protected JLabel experimentLabel;
	
	/** Experiment label name (e.g., "Experiment", "Exam").
	 * Default is "Experiment" */
	protected String experimentDescriptor = "Experiment";

	/** Label showing the current condition number */
	protected JLabel conditionLabel;
	
	/** Condition label name (e.g., "Condition", "Task", "Phase").
	 * Default is "Phase" */
	protected String conditionDescriptor = "Phase";
	
	/** Label showing the current trial number */
	protected JLabel trialLabel;
	
	/** Trial label name. Default is "Trial" */
	protected String trialDescriptor = "Trial";
	
	/** Label showing the subject id */
	protected JLabel subjectLabel;
	
	/** Subject label name. Default is "Participant" */
	protected String subjectDescriptor = "Participant";
	
	/** Vertical separators */
	protected ArrayList<JSeparator> separators;
	
	public ExperimentStatusPanel(Font labelFont) {
		this(labelFont, false, true, true, true);
	}
	
	public ExperimentStatusPanel(Font labelFont, 
			boolean experimentLabelVisible, boolean conditionLabelVisible, 
			boolean trialLabelVisible, boolean subjectLabelVisible) {
		super();
		setBackground(Color.white);
		setBorder(BorderFactory.createEmptyBorder(6, 5, 6, 5));
		separators = new ArrayList<JSeparator>(5);
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;
		//gbc.fill = GridBagConstraints.VERTICAL;
		
		JSeparator separator = new JSeparator(JSeparator.VERTICAL);
		gbc.gridx = 0; gbc.gridy = 0;		
		add(separator, gbc);
		separators.add(separator);
		
		experimentLabel = new JLabel();
		setExperimentInfo("N/A");
		experimentLabel.setFont(labelFont);
		gbc.gridx++;
		gbc.insets.left = 10;
		add(experimentLabel, gbc);		
		
		separator = new JSeparator(JSeparator.VERTICAL);
		gbc.gridx++;
		add(separator, gbc);
		separators.add(separator);
		
		subjectLabel = new JLabel();
		setSubjectInfo("N/A");
		subjectLabel.setFont(labelFont);
		gbc.gridx++;	
		gbc.insets.right = 10;
		add(subjectLabel, gbc);
		
		separator = new JSeparator(JSeparator.VERTICAL);
		gbc.gridx++;
		add(separator, gbc);
		separators.add(separator);
		
		conditionLabel = new JLabel();
		setConditionInfo(0, 0);
		conditionLabel.setFont(labelFont);
		gbc.gridx++;
		gbc.insets.right= 0;
		add(conditionLabel, gbc);			
		
		separator = new JSeparator(JSeparator.VERTICAL);
		gbc.gridx++;
		add(separator, gbc);
		separators.add(separator);
		
		trialLabel = new JLabel();
		trialLabel.setFont(labelFont);	
		setTrialInfo(0, 0, 0, 1);			
		gbc.gridx++;	
		add(trialLabel, gbc);
		
		separator = new JSeparator(JSeparator.VERTICAL);
		gbc.gridx++;
		add(separator, gbc);
		separators.add(separator);
		
		setComponentsVisible(experimentLabelVisible, conditionLabelVisible, trialLabelVisible, subjectLabelVisible);
	}
	
	public String getExperimentDescriptor() {
		return experimentDescriptor;
	}

	public void setExperimentDescriptor(String experimentDescriptor) {
		this.experimentDescriptor = experimentDescriptor;
	}

	public String getConditionDescriptor() {
		return conditionDescriptor;
	}

	public void setConditionDescriptor(String conditionDescriptor) {
		this.conditionDescriptor = conditionDescriptor;
	}

	public String getTrialDescriptor() {
		return trialDescriptor;
	}

	public void setTrialDescriptor(String trialDescriptor) {
		this.trialDescriptor = trialDescriptor;
	}

	public String getSubjectDescriptor() {
		return subjectDescriptor;
	}

	public void setSubjectDescriptor(String subjectDescriptor) {
		this.subjectDescriptor = subjectDescriptor;
	}

	@Override
	public JComponent getExperimentStatusPanelComponent() {
		return this;
	}
	
	@Override
	public void setExperimentInfo(String experimentName) {
		experimentLabel.setText(experimentDescriptor + ": " + experimentName);
	}

	@Override
	public void setConditionInfo(int currentCondition, int numConditions) {
		setConditionInfo(currentCondition, numConditions, null);
	}
	
	@Override
	public void setConditionInfo(int currentCondition, int numConditions, String conditionName) {
		if(conditionName == null) {
			if(currentCondition >= 0) {
				conditionLabel.setText("<html>" + conditionDescriptor + ": <b>" + 
						Integer.toString(currentCondition) + "/" + Integer.toString(numConditions) + "</b></html>");
			}
			else {
				conditionLabel.setText("<html>" + conditionDescriptor + ": Break" + "</html>");
			}
		}
		else {
			if(currentCondition >= 0) {
				conditionLabel.setText("<html>" + conditionDescriptor + ": " + conditionName + 
						" <b>(" + Integer.toString(currentCondition) + "/" + Integer.toString(numConditions) + ")</b></html>");
			}
			else {
				conditionLabel.setText("<html>" + conditionDescriptor + ": " + conditionName + "</html>");
			}
		}
	}
	
	@Override
	public void setTrialInfo(int currentTrial, int numTrials) {
		if(currentTrial >=0 && numTrials >= 0) {
			trialLabel.setText("<html>" + trialDescriptor + ": <b>" + 
					Integer.toString(currentTrial) + "/" + Integer.toString(numTrials) + "</b></html>");
		}
		else {
			trialLabel.setText(" ");
		}
	}	
	
	@Override
	public void setTrialInfo(int currentTrial, int numTrials, int currentTrialPart, int numTrialParts) {
		if(currentTrial >=0 && numTrials >= 0) {
			StringBuilder sb = new StringBuilder("<html>" + trialDescriptor + ": <b>");
			sb.append(Integer.toString(currentTrial) + "/" + Integer.toString(numTrials) + "</b>");
			if(numTrialParts > 0) {
				sb.append(", Part: <b>");
				sb.append(Integer.toString(currentTrialPart) + "/" + Integer.toString(numTrialParts) + "</b>");
			}
			sb.append("</html>");
			trialLabel.setText(sb.toString());
		}
		else {
			trialLabel.setText(" ");
		}
	}

	@Override
	public void setSubjectInfo(String subject) {
		subjectLabel.setText(subjectDescriptor + ": " + subject);
	}
	
	@Override
	public void setComponentsVisible(boolean experimentLabelVisible, boolean conditionLabelVisible, 
			boolean trialLabelVisible, boolean subjectLabelVisible) {		

		separators.get(0).setVisible(experimentLabelVisible);		
		experimentLabel.setVisible(experimentLabelVisible);
		
		separators.get(1).setVisible(true);
		subjectLabel.setVisible(subjectLabelVisible);
	
		separators.get(2).setVisible(conditionLabelVisible);		
		conditionLabel.setVisible(conditionLabelVisible);
		separators.get(3).setVisible(subjectLabelVisible);

		separators.get(3).setVisible(trialLabelVisible);
		trialLabel.setVisible(trialLabelVisible);
		separators.get(4).setVisible(trialLabelVisible);
	}
}