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
import java.awt.Font;
import java.awt.Frame;
import java.awt.Window;

import javax.swing.JDialog;

import org.mitre.icarus.cps.app.widgets.events.ButtonPressListener;

/**
 * Wraps the registration panel in a dialog.
 * 
 * @author CBONACETO
 *
 */
public class RegistrationDialog extends JDialog {
	private static final long serialVersionUID = 6126341805602450934L;
	
	protected RegistrationPanel registrationPanel;

	public RegistrationDialog(Frame owner, boolean modal) {
		super(owner, modal);
		initDialog();
	}
	
	public RegistrationDialog(Window owner) {
		super(owner);
		initDialog();
	}
	
	protected void initDialog() {
		setTitle("Create New User");
		setResizable(false);
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);		
		registrationPanel = new RegistrationPanel();
		getContentPane().add(registrationPanel);
		pack();
	}
	
	@Override
	public void setBackground(Color bg) {
		super.setBackground(bg);
		if(registrationPanel != null) {
			registrationPanel.setBackground(bg);
		}
	}
	
	@Override
	public void setFont(Font font) {
		super.setFont(font);
		if(registrationPanel != null) {
			registrationPanel.setFont(font);
		}
	}	
	
	public void addButtonPressListener(ButtonPressListener listener) {
		registrationPanel.addButtonPressListener(listener);
	}
	
	public void removeButtonPressListener(ButtonPressListener listener) {
		registrationPanel.removeButtonPressListener(listener);
	}
	
	public void resetFields() {
		registrationPanel.resetFields();
	}
	
	public void resetPasswordFields() {
		registrationPanel.resetPasswordFields();
	}
	
	public String getAuthenticationCode() {
		return registrationPanel.getAuthenticationCode();
	}
	
	public String getUserName() {
		return registrationPanel.getUserName();
	}	
	
	public char[] getPassword() {
		return registrationPanel.getPassword();
	}	
	
	public char[] getRetypePassword() {
		return registrationPanel.getRetypePassword();
	}
	
	public String getSite() {
		return registrationPanel.getSite();
	}
}