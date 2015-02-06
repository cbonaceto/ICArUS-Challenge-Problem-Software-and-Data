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
package org.mitre.icarus.cps.app.widgets.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkListener;

import org.mitre.icarus.cps.app.widgets.ImageManager;
import org.mitre.icarus.cps.app.widgets.WidgetConstants;
import org.mitre.icarus.cps.app.widgets.basic.HyperlinkPanel;
import org.mitre.icarus.cps.app.widgets.events.ButtonPressEvent;
import org.mitre.icarus.cps.app.widgets.events.ButtonPressListener;

/**
 * The home screen panel.
 * 
 * @author CBONACETO
 *
 */
public class HomePanel extends JPanel {
	private static final long serialVersionUID = -5661928500719509593L;
	
	protected JLabel messageLabel;
	
	protected JLabel participantLabel;
	
	protected JEditorPane optionsPane;
	
	protected JPanel buttonPanel;
	
	protected JButton logoutButton;
	
	protected JButton exitButton;
	
	/** Listeners registered to listen for button press events */
	protected transient List<ButtonPressListener> buttonListeners = 
			Collections.synchronizedList(new LinkedList<ButtonPressListener>());
	
	public HomePanel() {
		this(null, null, false);
	}

	public HomePanel(String userName, String subjectId, boolean returningUser) {
		super(new GridBagLayout());
		//setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		setBorder(WidgetConstants.DEFAULT_BORDER);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0; gbc.gridy = 0;
		gbc.weightx = 1; gbc.weighty = 0;
		gbc.insets.left = 3; 
		gbc.insets.right = 3;
		gbc.insets.top = 5;
		
		//Create top message label
		messageLabel = new JLabel();
		messageLabel.setHorizontalAlignment(JLabel.LEFT);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(messageLabel, gbc);
		
		//Create HTML panel to show menu options
		optionsPane = new JEditorPane();
		optionsPane.setEditable(false);		
		optionsPane.setBackground(getBackground());
		optionsPane.setContentType("text/html");		
		JScrollPane scrollPane = new JScrollPane(optionsPane);
		scrollPane.setBorder(null);
		scrollPane.setPreferredSize(new Dimension(460, 400));		
		gbc.gridy++;
		gbc.weighty = 1;
		gbc.insets.left = 20;
		gbc.insets.top = 15;
		gbc.fill = GridBagConstraints.BOTH;
		add(scrollPane, gbc);
		
		gbc.gridy++;
		gbc.weighty = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets.left = 0; 
		gbc.insets.right = 0;
		gbc.insets.top = 10;
		add(WidgetConstants.createDefaultSeparator(), gbc);
		
		//Add participant id display
		gbc.insets.top = 0;
		gbc.insets.bottom = 0;
		participantLabel = new JLabel(" Participant: 2");
		participantLabel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		participantLabel.setOpaque(true);
		participantLabel.setBackground(Color.WHITE);
		gbc.gridy++;
		add(participantLabel, gbc);
		
		gbc.gridy++;
		gbc.insets.bottom = 3;
		add(WidgetConstants.createDefaultSeparator(), gbc);
		
		//Add logout and exit buttons		
		gbc.gridy++;
		gbc.insets.top = 5;
		gbc.insets.bottom = 3;
		add(createButtonPanel(), gbc);
		
		setFont(messageLabel.getFont());
		setUser(userName, subjectId, returningUser);
	}
	
	private JPanel createButtonPanel() {
		buttonPanel = new JPanel();
		
		buttonPanel.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.weightx = 1;
		//constraints.anchor = GridBagConstraints.WEST;
		constraints.anchor = GridBagConstraints.EAST;
		//constraints.anchor = GridBagConstraints.CENTER;
		
		logoutButton = new JButton("Log Out");
		logoutButton.setIcon(ImageManager.getImageIcon(ImageManager.LOGOUT_ICON));
		logoutButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				fireButtonPressEvent(new ButtonPressEvent(this, ButtonPressEvent.BUTTON_LOGOUT));
			}			
		});
		buttonPanel.add(logoutButton, constraints);
		
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.insets.right = 10;
		//constraints.insets.left = 40;
		//constraints.weightx = 1.0;
		//constraints.anchor = GridBagConstraints.EAST;
		
		exitButton = new JButton("Exit");
		exitButton.setIcon(ImageManager.getImageIcon(ImageManager.EXIT_ICON));
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				fireButtonPressEvent(new ButtonPressEvent(this, ButtonPressEvent.BUTTON_EXIT));
			}			
		});
		buttonPanel.add(exitButton, constraints);
		
		return buttonPanel;
	}
	
	@Override
	public void setBackground(Color bg) {
		super.setBackground(bg);
		if(optionsPane != null) {
			optionsPane.setBackground(bg);
		}
		if(buttonPanel != null) {
			buttonPanel.setBackground(bg);
		}
	}
	
	@Override
	public void setFont(Font font) {
		super.setFont(font);
		if(messageLabel != null) {
			messageLabel.setFont(font);
		}
		if(participantLabel != null) {
			participantLabel.setFont(font);
		}
		if(optionsPane != null) {
			optionsPane.setFont(font);
		}
	}
	
	public synchronized void addButtonPressListener(ButtonPressListener listener) {
		buttonListeners.add(listener);
	}
	
	public synchronized void removeButtonPressListener(ButtonPressListener listener) {
		buttonListeners.remove(listener);
	}
	
	protected synchronized void fireButtonPressEvent(ButtonPressEvent event) {
		if(!buttonListeners.isEmpty()) {
			for(ButtonPressListener listener : buttonListeners) {
				listener.buttonPressed(event);
			}
		}
	}

	public void setUser(String userName, String subjectId, boolean returningUser) {
		if(userName != null) {			
			StringBuilder sb = new StringBuilder("<html>Welcome ");
			if(returningUser) {
				sb.append("back ");
			}
			sb.append("to the ICArUS experiment system, ");
			sb.append("<b>" + userName + "</b>.<br>");
			sb.append("Please select an option below:<br>");
			messageLabel.setText(sb.toString());
		} else {
			messageLabel.setText("<html>Welcome to the ICArUS experiment system!<br>Please select an option below:<br></html>");
		}
		if(subjectId != null) {
			participantLabel.setText("   Participant: " + subjectId);
		} else {
			participantLabel.setText("   Participant: None");
		}
	}
	
	/**
	 * Set the HTML options text.
	 * 
	 * @param text
	 */
	public void setOptionsText(String text) {
		optionsPane.setText(HyperlinkPanel.formatTextAsHTML(text, 
				"left", getFont().getName(), WidgetConstants.FONT_SIZE_HTML));
	}
	
	public void addHyperlinkListener(HyperlinkListener listener) {
		optionsPane.addHyperlinkListener(listener);
	}
	
	public void removeHyperlinkListener(HyperlinkListener listener) {
		optionsPane.removeHyperlinkListener(listener);
	}
	
	public static void main(String args[]) {
		JFrame frame = new JFrame("Test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		HomePanel hp = new HomePanel();
		//hp.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		hp.setOptionsText("<a href=\"start_where_leftoff\">Start Current Experiment Where I Left Off</a><br>");
		frame.getContentPane().add(hp);
		frame.pack();
		frame.setVisible(true);
	}
}