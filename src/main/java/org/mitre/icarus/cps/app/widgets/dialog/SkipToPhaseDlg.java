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
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.mitre.icarus.cps.exam.base.pause.IcarusPausePhase;
import org.mitre.icarus.cps.exam.phase_05.testing.IcarusTestPhase_Phase05;
import org.mitre.icarus.cps.exam.phase_05.training.IcarusTrainingPhase;
import org.mitre.icarus.cps.experiment_core.condition.Condition;

/**
 * Dialog to select an exam phase to skip to.
 * Would like to eventually shows the completion status of each phase, but this is not currently implemented.
 * 
 * @author CBONACETO
 *
 */
public class SkipToPhaseDlg extends JDialog {
	public static enum ButtonType {OK, Cancel};
	
	private static final long serialVersionUID = 1L;

	/** The phase selection combo box */
	@SuppressWarnings("rawtypes")
	protected JComboBox phaseComboBox;
	
	/** The button pressed to close the dialog */
	protected ButtonType buttonPressed = null;
	
	/** The selected phase */
	protected int phaseIndex = -1;
	
	public SkipToPhaseDlg(Component parent, Collection<? extends Condition> phases, int currentPhaseIndex) {
		this(parent, phases, currentPhaseIndex, "Phase");
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public SkipToPhaseDlg(Component parent, Collection<? extends Condition> phases, int currentPhaseIndex,
			String phaseTitle) {
		super(parent instanceof Window ? (Window)parent : null, "Advance to " + phaseTitle);
		
		JPanel panel = new JPanel(new GridBagLayout());		
		panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
		
		JPanel skipToTaskPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(4,4,4,4);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.0;
		gbc.weighty = 0.0;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.fill = GridBagConstraints.NONE;
		skipToTaskPanel.add(new JLabel("Advance to " + phaseTitle + ":"), gbc);
		gbc.gridx = 1;
		gbc.weightx = 1.0;
		//gbc.fill = GridBagConstraints.HORIZONTAL;
		String[] phaseNames = null;
		if(phases != null && !phases.isEmpty()) {
			phaseNames = new String[phases.size()];
			int i = 0;
			for(Condition phase : phases) {
				//StringBuilder sb = new StringBuilder(Integer.toString(i+1));
				StringBuilder sb = new StringBuilder();
				if(phase.getConditionNum() >= 0) {
					sb.append(Integer.toString(phase.getConditionNum()) + ". ");
				}
				//sb.append(". ");
				if(phase.getName() != null) {
					sb.append(phase.getName());
				}
				else {
					if(phase instanceof IcarusTestPhase_Phase05) {
						sb.append("Test");
					}
					else if(phase instanceof IcarusTrainingPhase) {
						sb.append("Training");
					}
					else if(phase instanceof IcarusPausePhase) {
						sb.append("Break");
					}
				}				
				phaseNames[i] = sb.toString();
				i++;
			}
		}
		phaseComboBox = new JComboBox(phaseNames);
		if(currentPhaseIndex >= 0) {
			if(currentPhaseIndex >= phases.size()) {
				currentPhaseIndex = phases.size() - 1;
			}
			phaseComboBox.setSelectedIndex(currentPhaseIndex);
		}
		skipToTaskPanel.add(phaseComboBox, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;
		gbc.anchor = GridBagConstraints.CENTER;
		//gbc.fill = GridBagConstraints.HORIZONTAL;
		panel.add(skipToTaskPanel, gbc);
		
		JPanel buttonPanel = new JPanel();
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				buttonPressed = ButtonType.OK;
				phaseIndex = phaseComboBox.getSelectedIndex();
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
	 * @param phases
	 * @param currentPhaseIndex
	 * @param phaseTitle
	 * @return
	 */
	public static Integer showDialog(Component parent, Collection<? extends Condition> phases, 
			int currentPhaseIndex, String phaseTitle) {
		SkipToPhaseDlg dlg = new SkipToPhaseDlg(parent, phases, currentPhaseIndex, 
				phaseTitle != null ? phaseTitle : "Mission");
		dlg.setModal(true);
		dlg.setResizable(false);
		dlg.setVisible(true);		
		if(dlg.getButtonPressed() == ButtonType.OK) {
			return dlg.getPhaseIndex();
		} else {
			return null;
		}
	}

	/** Get the button pressed to close the dialog */
	public ButtonType getButtonPressed() {
		return buttonPressed;
	}
	
	/** Get the index of the phase that was selected */
	public int getPhaseIndex() {
		return phaseIndex;
	}	
}