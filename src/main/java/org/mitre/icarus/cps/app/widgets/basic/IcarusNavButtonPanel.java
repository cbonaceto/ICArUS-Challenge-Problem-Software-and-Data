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
package org.mitre.icarus.cps.app.widgets.basic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import org.mitre.icarus.cps.app.widgets.WidgetConstants;
import org.mitre.icarus.cps.experiment_core.event.SubjectActionEvent;
import org.mitre.icarus.cps.experiment_core.gui.NavButtonPanel;

/**
 * Extends basic nav button panel and adds an additional button next
 * to help to review the exam tutorial.  When this button is pressed, it 
 * will fire a SubjectActionEvent.CUSTOM_BUTTON_PRESSED event.
 * 
 * @author CBONACETO
 *
 */
public class IcarusNavButtonPanel extends NavButtonPanel {

	private static final long serialVersionUID = 1L;
	
	protected JButton reviewTutorialButton;
	
	public IcarusNavButtonPanel() {
		super();		
		
		exitButton.setFocusable(false);
		
		reviewTutorialButton = new JButton("Review Tutorial");
		/*GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 3;
		gbc.anchor = GridBagConstraints.EAST;
		helpButtonPanel.add(reviewTutorialButton, gbc);*/		
		helpButtonPanel.add(reviewTutorialButton);
		reviewTutorialButton.setFocusable(false);
		reviewTutorialButton.setMargin(WidgetConstants.INSETS_CONTROL);
		reviewTutorialButton.setIcon(helpIcon);
		reviewTutorialButton.addActionListener(this);
		reviewTutorialButton.setVisible(false);		
		reviewTutorialButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fireButtonPressedEvent(SubjectActionEvent.CUSTOM_BUTTON_PRESSED);
			}
		});	
	}
	
	public void setReviewTutorialButtonText(String text) {
		reviewTutorialButton.setText(text);
		revalidate();
	}
	
	public void setReviewTutorialButtonEnabled(boolean enabled) {
		reviewTutorialButton.setEnabled(enabled);
	}

	public void setReviewTutorialButtonVisible(boolean visible) {
		if(visible != reviewTutorialButton.isVisible()) {
			reviewTutorialButton.setVisible(visible);
			revalidate();
		}
	}	
}