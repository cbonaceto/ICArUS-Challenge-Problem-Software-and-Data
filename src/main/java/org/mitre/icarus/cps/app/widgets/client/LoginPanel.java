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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkEvent.EventType;
import javax.swing.event.HyperlinkListener;

import org.mitre.icarus.cps.app.widgets.ImageManager;
import org.mitre.icarus.cps.app.widgets.basic.HyperlinkPanel;
import org.mitre.icarus.cps.app.widgets.events.ButtonPressEvent;
import org.mitre.icarus.cps.app.widgets.events.ButtonPressListener;

/**
 * @author Eric Kappotis
 *
 */
public class LoginPanel extends JPanel {
	private static final long serialVersionUID = 8475604214355199862L;
	
	private JTextField userNameTextField;
	
	private JPasswordField passwordField;
	
	private JButton loginButton;
	
	private JButton exitButton;
	
	/** Listeners registered to listen for button press events */
	protected transient List<ButtonPressListener> buttonListeners = 
			Collections.synchronizedList(new LinkedList<ButtonPressListener>());

	public LoginPanel() {			
		setLayout(new GridBagLayout());
		
		Insets insets = GUIConstants.DEFAULT_INSETS;
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.EAST;
		constraints.insets.left = insets.left;
		constraints.insets.right = insets.right;
		constraints.insets.top = 5;
		constraints.insets.bottom = insets.bottom;
		
		JLabel userNameLabel = new JLabel("Email Address / User Name:");
		add(userNameLabel, constraints);
		
		constraints.gridx = 1;
		constraints.gridy = 0;
		
		userNameTextField = new JTextField();
		userNameTextField.setPreferredSize(new Dimension(GUIConstants.COMPONENT_DIMENSION.width, 
				userNameTextField.getPreferredSize().height));
		
		add(userNameTextField, constraints);
		
		constraints.gridx = 0;
		constraints.gridy = 1;		
		
		JLabel passwordLabel = new JLabel("Password:");
		
		add(passwordLabel, constraints);
		
		constraints.gridx = 1;
		constraints.gridy = 1;
		
		passwordField = new JPasswordField();
		passwordField.setPreferredSize(new Dimension(GUIConstants.COMPONENT_DIMENSION.width,
				passwordField.getPreferredSize().height));
		
		add(passwordField, constraints);
		
		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.WEST;
		
		HyperlinkPanel hyperlinkPanel = new HyperlinkPanel("Create a new user account");
		hyperlinkPanel.addHyperlinkListener(new HyperlinkListener() {
			@Override
			public void hyperlinkUpdate(HyperlinkEvent event) {
				if(event.getEventType() == EventType.ACTIVATED) {
					fireButtonPressEvent(new ButtonPressEvent(this, ButtonPressEvent.BUTTON_CREATE_USER));
				}
			}			
		});
		
		add(hyperlinkPanel, constraints);
		
		/*constraints.gridx = 1;
		constraints.gridy = 3;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets.top = GUIConstants.DEFAULT_INSETS.top * 2;*/
		
		constraints.gridx = 0;
		constraints.gridy++;	
		constraints.gridwidth = 2;
		constraints.weighty = 0;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets.left = 0; 
		constraints.insets.right = 0;
		constraints.insets.top = 10;
		add(org.mitre.icarus.cps.app.widgets.WidgetConstants.createDefaultSeparator(), constraints);
		
		constraints.gridx = 1;
		constraints.gridy++;
		constraints.gridwidth = 1;
		constraints.insets.top = 5;
		constraints.insets.bottom = 3;
		add(createButtonPanel(), constraints);
	}
	
	private JPanel createButtonPanel() {
		JPanel buttonPanel = new JPanel();
		
		buttonPanel.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.weightx = 1;
		constraints.anchor = GridBagConstraints.EAST;
		//constraints.anchor = GridBagConstraints.WEST;
		
		loginButton = new JButton("Log In");
		loginButton.setIcon(ImageManager.getImageIcon(ImageManager.LOGIN_ICON));
		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				fireButtonPressEvent(new ButtonPressEvent(this, ButtonPressEvent.BUTTON_LOGIN));
			}			
		});
		buttonPanel.add(loginButton, constraints);
		
		constraints.gridx = 1;
		
		constraints.gridy = 0;
		constraints.insets.right = 10;
		//constraints.weightx = 1.0;
		constraints.anchor = GridBagConstraints.EAST;
		
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
	
	public void resetFields() {
		userNameTextField.setText("");
		passwordField.setText("");
	}
	
	public String getUserName() {
		return userNameTextField.getText();
	}	
	
	public char[] getPassword() {
		return passwordField.getPassword();
	}	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		LoginPanel panel = new LoginPanel();
		frame.getContentPane().add(panel);
		
		frame.pack();
		frame.setVisible(true);
	}
}