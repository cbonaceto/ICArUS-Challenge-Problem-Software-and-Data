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

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.mitre.icarus.cps.app.widgets.phase_2.experiment.CommandButton.CommandButtonType;

/**
 * A panel with buttons to create a batch plot, display a previous outcome, and display a next outcome.
 * May also display an optional message below the buttons.
 * 
 * @author CBONACETO
 *
 */
public class BatchPlotControlPanel extends JPanel {

	private static final long serialVersionUID = -6405476201248855951L;
	
	/** The create batch plot button */
	protected CommandButton createBatchPlotButton;	
	
	/** The display previous outcome button */
	protected CommandButton previousOutcomeButton;
	
	/** The display next outcome button */
	protected CommandButton nextOutcomeButton;
	
	/** Panel containing the previous and next outcome buttons */
	protected JPanel previousNextButtonPanel;
	
	/** The message label */
	protected JLabel messageLabel;
	
	public BatchPlotControlPanel() {
		super(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		
		createBatchPlotButton = new CommandButton(CommandButtonType.CreateBatchPlot);
		createBatchPlotButton.setVisible(false);
		add(createBatchPlotButton, gbc);
		
		previousNextButtonPanel = new JPanel();
		previousNextButtonPanel.setVisible(false);
		previousOutcomeButton = new CommandButton(CommandButtonType.DisplayPreviousOutcome);
		previousNextButtonPanel.add(previousOutcomeButton);		
		nextOutcomeButton = new CommandButton(CommandButtonType.DisplayNextOutcome);		
		previousNextButtonPanel.add(nextOutcomeButton);
		gbc.gridy = 1;
		add(previousNextButtonPanel, gbc);
		
		messageLabel = new JLabel(" ");
		messageLabel.setHorizontalAlignment(JLabel.CENTER);
		gbc.gridy = 2;
		gbc.weighty = 1;
		gbc.insets.top = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(messageLabel, gbc);
	}	
	
	public void addCommandButtonListener(ActionListener l) {		
		createBatchPlotButton.addActionListener(l);
		previousOutcomeButton.addActionListener(l);
		nextOutcomeButton.addActionListener(l);
	}
	
	public void removeCommandButtonListener(ActionListener l) {
		createBatchPlotButton.removeActionListener(l);
		previousOutcomeButton.removeActionListener(l);
		nextOutcomeButton.removeActionListener(l);
	}
	
	public void showCreateBatchPlotButton(boolean batchPlotButtonEnabled, String message) {
		if(!createBatchPlotButton.isVisible()) {
			createBatchPlotButton.setVisible(true);
			previousNextButtonPanel.setVisible(false);
			revalidate();
			repaint();
		}
		createBatchPlotButton.setEnabled(batchPlotButtonEnabled);
		setMessage(message);
	}
	
	public void showDisplayOutcomeButtons(boolean showPreviousOutcomeButton, boolean previousOutcomeButtonEnabled,
			boolean showNextOutcomeButton, boolean nextOutcomeButtonEnabled,
			String message) {
		if(createBatchPlotButton.isVisible()) {
			createBatchPlotButton.setVisible(false);
			previousNextButtonPanel.setVisible(true);
			revalidate();
			repaint();
		} else if(!previousNextButtonPanel.isVisible()) {
			previousNextButtonPanel.setVisible(true);
			revalidate();
			repaint();
		}
		previousOutcomeButton.setEnabled(previousOutcomeButtonEnabled);
		nextOutcomeButton.setEnabled(nextOutcomeButtonEnabled);
		setMessage(message);
	}
	
	public void setCreateBatchPlotButtonEnabled(boolean enabled) {
		createBatchPlotButton.setEnabled(enabled);
	}
	
	public void setPreviousOutcomeButtonEnabled(boolean enabled) {
		previousOutcomeButton.setEnabled(enabled);
	}
	
	public void setNextOutcomeButtonEnabled(boolean enabled) {
		nextOutcomeButton.setEnabled(enabled);
	}
	
	public void setMessageVisible(boolean visible) {
		if(messageLabel.isVisible() != visible) {
			messageLabel.setVisible(visible);
			revalidate();
			repaint();
		}
	}
	
	public void setMessageFont(Font font) {
		messageLabel.setFont(font);
	}
	
	public void setMessage(String message) {
		messageLabel.setText(message);
	}
}