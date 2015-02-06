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

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.mitre.icarus.cps.app.widgets.WidgetConstants;
import org.mitre.icarus.cps.app.widgets.phase_2.ImageManager_Phase2;


/**
 * Command button component. A command button styles its button based on its button type. It also has 
 * an optional message displayed below the button.
 * 
 * @author CBONACETO
 *
 */
public class CommandButton extends JPanel {	
	/** Command button types */
	public static enum CommandButtonType {ReviewBlueBook, ReviewSigintReliability, ReviewPayoffMatrix,
		CreateBatchPlot, DisplayPreviousOutcome, DisplayNextOutcome};
	
	private static final long serialVersionUID = 1L;	
	
	/** The command button type */
	private CommandButtonType buttonType;
	
	/** The button */
	protected JButton button;
	
	/** The button description label (if any) */
	protected JLabel descriptionLabel;
	
	protected static final Color COLOR_LABEL_ENABLED = new JLabel().getForeground();
	
	public CommandButton(CommandButtonType buttonType) {
		super(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		button = new JButton();
		add(button, gbc);
		setButtonType(buttonType);
	}
	
	public void addActionListener(ActionListener l) {
		button.addActionListener(l);
	}
	
	public void removeActionListener(ActionListener l) {
		button.removeActionListener(l);
	}

	public CommandButtonType getButtonType() {
		return buttonType;			
	}

	public void setButtonType(CommandButtonType buttonType) {
		if(buttonType != this.buttonType) {
			this.buttonType = buttonType;
			switch(buttonType) {
			case ReviewBlueBook:				
				button.setText("Review BLUEBOOK");
				button.setIcon(ImageManager_Phase2.getImageIcon(ImageManager_Phase2.BLUEBOOK_ICON));
				break;
			case ReviewSigintReliability:
				button.setText("Review SIGINT Reliability");
				button.setIcon(ImageManager_Phase2.getImageIcon(ImageManager_Phase2.SIGINT_RELIABILITY_ICON));
				break;
			case ReviewPayoffMatrix:
				button.setText("Review Payoff Matrix");
				button.setIcon(ImageManager_Phase2.getImageIcon(ImageManager_Phase2.SIGINT_RELIABILITY_ICON));
				break;
			case CreateBatchPlot:
				button.setText("Create Batch Plot");
				button.setIcon(ImageManager_Phase2.getImageIcon(ImageManager_Phase2.BATCH_PLOTS_ICON));
				break;
			case DisplayPreviousOutcome:
				button.setText("Backward");
				button.setIcon(ImageManager_Phase2.getImageIcon(ImageManager_Phase2.PREVIOUS_OUTCOME_ICON));
				break;
			case DisplayNextOutcome:
				button.setText("Forward");
				button.setHorizontalTextPosition(JButton.LEFT);
				button.setIcon(ImageManager_Phase2.getImageIcon(ImageManager_Phase2.NEXT_OUTCOME_ICON));
				break;
			}
			button.setActionCommand(buttonType.toString());
		}
	}
	
	public boolean isButtonDescriptionVisible() {
		return descriptionLabel != null && descriptionLabel.isVisible();
	}
	
	public void setButtonDescriptionVisible(boolean visible) {
		if(visible) {
			if(descriptionLabel == null) {
				descriptionLabel = new JLabel();
				GridBagConstraints gbc = new GridBagConstraints();
				gbc.gridx = 0;
				gbc.gridy = 1;
				gbc.insets.top = 4;
				add(descriptionLabel, gbc);
				revalidate();
				repaint();
			} else {
				if(visible != descriptionLabel.isVisible()) {
					descriptionLabel.setVisible(visible);
					revalidate();
					repaint();
				}
			}
		} else {
			if(descriptionLabel != null) {
				descriptionLabel.setVisible(false);
			}
		}
	}
	
	public boolean isEnabled() {
		return button.isEnabled();
	}
	
	public void setEnabled(boolean enabled) {
		if(button.isEnabled() != enabled) {
			button.setEnabled(enabled);
			if(descriptionLabel != null) {				
				descriptionLabel.setForeground(enabled ? 
					COLOR_LABEL_ENABLED : WidgetConstants.COLOR_LIGHT_GRAY);
			}
		}
	}
	
	public String getButtonDescription() {
		return descriptionLabel != null ? descriptionLabel.getText() : null;
	}
	
	public void setButtonDescription(String description) {
		if(descriptionLabel != null) {
			descriptionLabel.setText(description);
		}
	}
}