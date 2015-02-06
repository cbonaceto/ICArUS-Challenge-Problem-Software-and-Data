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
 * Wraps the login panel in a dialog.
 * 
 * @author Eric Kappotis
 *
 */
public class LoginDialog extends JDialog {
	private static final long serialVersionUID = 6126341805602450934L;
	
	protected LoginPanel loginPanel;

	public LoginDialog(Frame owner, boolean modal) {
		super(owner, modal);
		initDialog();
	}
	
	public LoginDialog(Window owner) {
		super(owner);
		initDialog();
	}
	
	protected void initDialog() {
		setTitle("ICArUS Experiment Log In");
		setResizable(false);
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);		
		loginPanel = new LoginPanel();
		getContentPane().add(loginPanel);
		pack();
	}
	
	@Override
	public void setBackground(Color bg) {
		super.setBackground(bg);
		if(loginPanel != null) {
			loginPanel.setBackground(bg);
		}
	}
	
	@Override
	public void setFont(Font font) {
		super.setFont(font);
		if(loginPanel != null) {
			loginPanel.setFont(font);
		}
	}	
	
	public void addButtonPressListener(ButtonPressListener listener) {
		loginPanel.addButtonPressListener(listener);
	}
	
	public void removeButtonPressListener(ButtonPressListener listener) {
		loginPanel.removeButtonPressListener(listener);
	}	
	
	public void resetFields() {
		loginPanel.resetFields();
	}
	
	public String getUserName() {
		return loginPanel.getUserName();
	}
	
	public char[] getPassword() {
		return loginPanel.getPassword();
	}
}