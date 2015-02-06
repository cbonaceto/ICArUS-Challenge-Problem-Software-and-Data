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
package org.mitre.icarus.cps.app.widgets.dialog;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.mitre.icarus.cps.exam.base.testing.IcarusTestPhase;
import org.mitre.icarus.cps.exam.base.testing.IcarusTestTrial;

/**
 * Dialog to select a trial in a task to navigate to. 
 * Would like to eventually shows the completion status of each trial, but this is not currently implemented.
 * 
 * @author CBONACETO
 *
 */
public class TrialSelectionDlg extends JDialog {
	private static final long serialVersionUID = 1L;
	
	public static enum ButtonType {OK, Cancel};	

	/** The trial selection combo box */
	@SuppressWarnings("rawtypes")
	protected JComboBox trialComboBox;
	
	/** The button pressed to close the dialog */
	protected ButtonType buttonPressed = null;
	
	/** The selected trial */
	protected int trial = -1;	
	
	/**
	 * @param parent
	 * @param task
	 * @param currentTrial
	 */
	public TrialSelectionDlg(Component parent, IcarusTestPhase<?> task, int currentTrial) {
		this(parent, task != null ? task.getTestTrials() : null, currentTrial);
	}
	
	/**
	 * @param parent
	 * @param trials
	 * @param currentTrial
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })	
	public TrialSelectionDlg(Component parent, List<? extends IcarusTestTrial> trials, int currentTrial) {
		super(parent instanceof Window ? (Window)parent : null, "Advance to Trial");
		JPanel panel = new JPanel(new GridBagLayout());		
		panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
		
		JPanel skipToTrialPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(4,4,4,4);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.0;
		gbc.weighty = 0.0;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.fill = GridBagConstraints.NONE;
		skipToTrialPanel.add(new JLabel("Advance to Trial:"), gbc);
		gbc.gridx = 1;
		gbc.weightx = 1.0;
		String[] trialNames = null;
		if(trials != null && !trials.isEmpty()) {
			//List<? extends IcarusTestTrial> trials = task.getTestTrials();
			//if(trials != null && trials.size() > 0) {
			trialNames = new String[trials.size()];
			int i = 0;
			for(IcarusTestTrial trial : trials) {				
				StringBuilder sb = new StringBuilder();
				sb.append(trial.getTrialNum() != null ? trial.getTrialNum().toString() : Integer.toString(i+1));				
				trialNames[i] = sb.toString();
				i++;
			}
		}
		//}
		trialComboBox = new JComboBox(trialNames);
		if(currentTrial >= 0) {
			/*if(currentTrial >= task.getNumTrials()) {
				currentTrial = task.getNumTrials() - 1;
			}
			trialComboBox.setSelectedIndex(currentTrial);*/
			trialComboBox.setSelectedItem(Integer.toString(currentTrial));			
		}
		skipToTrialPanel.add(trialComboBox, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;
		gbc.anchor = GridBagConstraints.CENTER;
		panel.add(skipToTrialPanel, gbc);
		
		JPanel buttonPanel = new JPanel();
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				buttonPressed = ButtonType.OK;
				trial = trialComboBox.getSelectedIndex();
				dispose();
			}			
		});
		buttonPanel.add(okButton);
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				buttonPressed = ButtonType.Cancel;
				dispose();
			}			
		});
		buttonPanel.add(cancelButton);		 
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0.0;
		gbc.weighty = 0.0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.CENTER;
		panel.add(buttonPanel, gbc);
		
		setContentPane(panel);
		pack();
		setResizable(false);
		if(parent != null) {
			setLocationRelativeTo(parent);
		}
	}
	
	/**
	 * @param parent
	 * @param task
	 * @param currentTrial
	 * @return
	 */
	public static Integer showDialog(Component parent, IcarusTestPhase<?> task, int currentTrial) {
		return showDialog(parent, task != null ? task.getTestTrials() : null, currentTrial);
	}
	
	/**
	 * @param parent
	 * @param trials
	 * @param currentTrial
	 * @return
	 */
	public static Integer showDialog(Component parent, List<? extends IcarusTestTrial> trials, int currentTrial) {
		TrialSelectionDlg dlg = new TrialSelectionDlg(parent, trials, currentTrial);
		dlg.setModal(true);
		dlg.setVisible(true);
		dlg.setResizable(false);
		if(dlg.getButtonPressed() == ButtonType.OK) {
			return dlg.getTrial();
		} else {
			return null;
		}
	}
	

	/** Get the button pressed to close the dialog */
	public ButtonType getButtonPressed() {
		return buttonPressed;
	}
	
	/** Get the trial that was selected */
	public int getTrial() {
		return trial;
	}	
}