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

import java.awt.Color;
import java.awt.Font;
//import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.mitre.icarus.cps.app.widgets.WidgetConstants;
import org.mitre.icarus.cps.experiment_core.gui.JPanelConditionComponent;

/**
 * Break Panel.  Shown during breaks in an exam.
 * 
 * @author Ed Overly
 *
 */
public class BreakPanel extends JPanelConditionComponent {
	private static final long serialVersionUID = 1L;
	
	/** Text area with any text to show during the break */
	private JEditorPane textArea;
	
	/** Label with the time remaining string */
	private JLabel timeLabel;
	
	/** Label with "click next to continue" instructions */
	private JLabel clickNextLabel;
	
	/** Panel with time label and click next label */
	private JPanel timePanel;
	
	public BreakPanel() {
		this(true);
	}
	
	public BreakPanel(boolean showCountdown) {
		super("BreakPanel");
		setOpaque(true);		
		setLayout(new GridBagLayout());
	
		Font font = new Font(WidgetConstants.FONT_DEFAULT.getName(), Font.BOLD, 30);
		setFont(font);
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.fill = GridBagConstraints.BOTH;
		
		textArea = new JEditorPane();
		textArea.setFont(WidgetConstants.FONT_INSTRUCTION_PANEL);
		textArea.setOpaque(false);
		gbc.weightx = 1;
		gbc.weighty = 2;
		gbc.insets.left = 10;
		gbc.insets.right = 10;
		gbc.insets.top = 15;
		add(textArea, gbc);
		
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.insets.top = 5;
		
		timePanel = new JPanel(new GridBagLayout());
		
		JLabel label = new JLabel("Break Time Remaining: ");
		label.setHorizontalAlignment(JLabel.LEFT);
		label.setFont(font);
		timePanel.add(label, gbc);
		
		timeLabel = new JLabel("00:00");
		timeLabel.setHorizontalAlignment(JLabel.LEFT);
		timeLabel.setFont(font);
		timeLabel.setPreferredSize(timeLabel.getPreferredSize());
		timeLabel.setMinimumSize(timeLabel.getPreferredSize());
		gbc.gridx++;
		timePanel.add(timeLabel, gbc);
		
		clickNextLabel = new JLabel("Please click Next to continue.");
		clickNextLabel.setHorizontalAlignment(JLabel.LEFT);
		clickNextLabel.setFont(font);
		clickNextLabel.setForeground(Color.red);
		clickNextLabel.setPreferredSize(clickNextLabel.getPreferredSize());
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.insets.top = 20;
		gbc.gridwidth = 2;
		timePanel.add(clickNextLabel, gbc);
		clickNextLabel.setText(" ");
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weighty = 1;
		add(timePanel, gbc);
	}
	
	public void setCountdownVisible(boolean visible) {
		
		timePanel.setVisible(visible);
	}
	
	public boolean isCountdownVisible() {
		return timePanel.isVisible();
	}
	
	public void setRemainingTime(long remainingTime_ms) {
		int minutes = (int)((remainingTime_ms/1000)/60);
		int seconds = (int)(remainingTime_ms/1000 - minutes*60);
		if(seconds < 0) {
			seconds = 0;
		}		
		StringBuilder sb = new StringBuilder();
		if(minutes < 10) {
			sb.append("0");
		}
		sb.append(Integer.toString(minutes));
		sb.append(":");
		if(seconds < 10) {
			sb.append("0");
		}
		sb.append(Integer.toString(seconds));
		timeLabel.setText(sb.toString());
	}

	public void setClickNext(boolean clickNext) {
		if(clickNext) {
			clickNextLabel.setText("Please click Next to continue.");
		}
		else {
			clickNextLabel.setText(" ");
		}
	}

	public void setInstructionText(String text) {
		textArea.setText(text);
	}
}
