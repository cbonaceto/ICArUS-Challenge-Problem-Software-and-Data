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
package org.mitre.icarus.cps.app.widgets.phase_1.player;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import org.mitre.icarus.cps.app.widgets.WidgetConstants;
import org.mitre.icarus.cps.app.widgets.basic.IcarusNavButtonPanel;
import org.mitre.icarus.cps.app.widgets.phase_1.ImageManager_Phase1;
import org.mitre.icarus.cps.experiment_core.event.SubjectActionEvent;

/**
 * Extends IcarusNavButtonPanel and adds "Skip to Trial" and "Skip to Mission" buttons.
 * 
 * @author CBONACETO
 *
 */
public class PlayerNavButtonPanel extends IcarusNavButtonPanel {
	private static final long serialVersionUID = -3245015777582424250L;
	
	public static final int SKIP_TO_TRIAL_BUTTON_PRESSED = SubjectActionEvent.NEXT_EVENT_INDEX;
	
	public static final int SKIP_TO_MISSION_BUTTON_PRESSED = SubjectActionEvent.NEXT_EVENT_INDEX + 1;
	
	protected JButton skipToTrialButton;
	
	protected JButton skipToMissionButton;
	
	public PlayerNavButtonPanel() {
		super();
		
		helpButtonPanel.remove(helpButton);
		exitButtonPanel.remove(exitButton);
		exitButtonPanel.add(helpButton);
		
		skipToTrialButton = new JButton("Advance To Trial");
		helpButtonPanel.add(skipToTrialButton);
		skipToTrialButton.setFocusable(false);
		skipToTrialButton.setMargin(WidgetConstants.INSETS_CONTROL);
		skipToTrialButton.setIcon(ImageManager_Phase1.getImageIcon(ImageManager_Phase1.CONTROL_FAST_FORWARD_ICON));
		skipToTrialButton.addActionListener(this);
		skipToTrialButton.setVisible(false);		
		skipToTrialButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fireButtonPressedEvent(SKIP_TO_TRIAL_BUTTON_PRESSED);
			}
		});
		
		skipToMissionButton = new JButton("Advance To Mission");
		helpButtonPanel.add(skipToMissionButton);
		skipToMissionButton.setFocusable(false);
		skipToMissionButton.setMargin(WidgetConstants.INSETS_CONTROL);
		skipToMissionButton.setIcon(ImageManager_Phase1.getImageIcon(ImageManager_Phase1.CONTROL_FAST_FORWARD_ICON));
		skipToMissionButton.addActionListener(this);
		skipToMissionButton.setVisible(false);		
		skipToMissionButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fireButtonPressedEvent(SKIP_TO_MISSION_BUTTON_PRESSED);
			}
		});
	}
	
	public void setSkipToTrialButtonText(String text) {
		skipToTrialButton.setText(text);
	}

	public void setSkipToTrialButtonVisible(boolean visible) {
		skipToTrialButton.setVisible(visible);
	}
	
	public void setSkipToTrialButtonEnabled(boolean enabled) {
		skipToTrialButton.setEnabled(enabled);
	}
	
	public void setSkipToMissionButtonText(String text) {
		skipToMissionButton.setText(text);
	}

	public void setSkipToMissionButtonVisible(boolean visible) {
		skipToMissionButton.setVisible(visible);
	}
	
	public void setSkipToMissionButtonEnabled(boolean enabled) {
		skipToMissionButton.setEnabled(enabled);
	}
}