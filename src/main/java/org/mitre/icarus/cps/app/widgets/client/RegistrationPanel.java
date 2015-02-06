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

import org.mitre.icarus.cps.app.widgets.events.ButtonPressEvent;
import org.mitre.icarus.cps.app.widgets.events.ButtonPressListener;

/**
 * @author Eric Kappotis
 *
 */
public class RegistrationPanel extends JPanel {	
	private static final long serialVersionUID = -7566048940744019803L;
	
	private JTextField authCodeTextField;
	private JTextField userNameField;
	private JTextField siteTextField;
	private JPasswordField passwordField;
	private JPasswordField repasswordField;
	
	private JButton okButton;
	private JButton cancelButton;
	
	/** Listeners registered to listen for button press events */
	protected transient List<ButtonPressListener> buttonListeners = 
			Collections.synchronizedList(new LinkedList<ButtonPressListener>());
	
	public RegistrationPanel() {		
		Insets insets = GUIConstants.DEFAULT_INSETS;
		
		setLayout(new GridBagLayout());
		
		GridBagConstraints constraints = new GridBagConstraints();
		
		constraints.anchor = GridBagConstraints.NORTHEAST;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.insets.left = insets.left;
		constraints.insets.right = insets.right;
		constraints.insets.top = 5;
		constraints.insets.bottom = insets.bottom * 4;
		
		JLabel siteLocationLabel = new JLabel("Site:");
		add(siteLocationLabel, constraints);
		
		constraints.gridx = 1;
		constraints.gridy = 0;
		
		siteTextField = new JTextField();
		siteTextField.setPreferredSize(new Dimension(GUIConstants.COMPONENT_DIMENSION.width,
				siteTextField.getPreferredSize().height));
		
		add(siteTextField, constraints);
		
		constraints.gridx = 0;
		constraints.gridy = 1;
		
		JLabel authenticationLabel = new JLabel("Authentication Code:");
		add(authenticationLabel, constraints);
		
		constraints.gridx = 1;
		constraints.gridy = 1;
		
		authCodeTextField = new JTextField();
		authCodeTextField.setPreferredSize(new Dimension(GUIConstants.COMPONENT_DIMENSION.width,
				authCodeTextField.getPreferredSize().height));
		
		add(authCodeTextField, constraints);
		
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.insets.bottom = insets.bottom;
		
		JLabel userNameLabel = new JLabel("Email Address / User Name:");
		
		add(userNameLabel, constraints);
		
		constraints.gridx = 1;
		constraints.gridy = 2;
		
		userNameField = new JTextField();
		userNameField.setPreferredSize(new Dimension(GUIConstants.COMPONENT_DIMENSION.width,
				userNameField.getPreferredSize().height));
		
		add(userNameField, constraints);
		
		constraints.gridx = 0;
		constraints.gridy = 3;
		
		JLabel passwordLabel = new JLabel("Password:");
		
		add(passwordLabel, constraints);
		
		constraints.gridx = 1;
		constraints.gridy = 3;
		
		passwordField = new JPasswordField();
		passwordField.setPreferredSize(new Dimension(GUIConstants.COMPONENT_DIMENSION.width,
				passwordField.getPreferredSize().height));
		
		add(passwordField, constraints);
		
		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.insets.bottom = insets.bottom * 4;
		
		JLabel repasswordLabel = new JLabel("Re-Type Password:");
		
		add(repasswordLabel, constraints);
		
		constraints.gridx = 1;
		constraints.gridy = 4;
		
		repasswordField = new JPasswordField();
		repasswordField.setPreferredSize(new Dimension(GUIConstants.COMPONENT_DIMENSION.width,
				repasswordField.getPreferredSize().height));
		
		add(repasswordField, constraints);
		
		constraints.gridx = 0;
		constraints.gridy = 5;
		constraints.gridwidth = 2;
		constraints.insets.bottom = insets.bottom;
		constraints.anchor = GridBagConstraints.CENTER;
		
		add(createButtonPanel(), constraints);		
	}
	
	private JPanel createButtonPanel() {
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridBagLayout());
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.insets.right = GUIConstants.DEFAULT_INSETS.right;
		
		okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				fireButtonPressEvent(new ButtonPressEvent(this, ButtonPressEvent.BUTTON_OK));
			}			
		});		
		buttonPanel.add(okButton, constraints);
		
		constraints.gridx = 1;
		constraints.gridy = 0;
		
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				fireButtonPressEvent(new ButtonPressEvent(this, ButtonPressEvent.BUTTON_CANCEL));
			}			
		});		
		buttonPanel.add(cancelButton, constraints);
		
		okButton.setPreferredSize(cancelButton.getPreferredSize());
		
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
		authCodeTextField.setText("");
		userNameField.setText("");
		siteTextField.setText("");
		passwordField.setText("");
		repasswordField.setText("");
	}
	
	public void resetPasswordFields() {
		passwordField.setText("");
		repasswordField.setText("");
	}
	
	public String getAuthenticationCode() {
		return authCodeTextField.getText();
	}
	
	public String getSite() {
		return siteTextField.getText();
	}
	
	public String getUserName() {
		return userNameField.getText();
	}	
	
	public char[] getPassword() {
		return passwordField.getPassword();
	}	
	
	public char[] getRetypePassword() {
		return repasswordField.getPassword();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		RegistrationPanel panel = new RegistrationPanel();
		frame.getContentPane().add(panel);
		
		frame.pack();
		frame.setVisible(true);
	}
}