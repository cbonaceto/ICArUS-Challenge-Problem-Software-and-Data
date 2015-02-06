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
import javax.swing.event.HyperlinkListener;

import org.mitre.icarus.cps.app.widgets.events.ButtonPressListener;

/**
 * Wraps the home panel in a dialog.
 * 
 * @author CBONACETO
 *
 */
public class HomeDialog extends JDialog {
	private static final long serialVersionUID = 1518208271655766191L;
	
	protected HomePanel homePanel;

	public HomeDialog(Frame owner, boolean modal) {
		super(owner, modal);
		initDialog();
	}
	
	public HomeDialog(Window owner) {
		super(owner);
		initDialog();
	}
	
	protected void initDialog() {
		setTitle("Home");
		setResizable(false);
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);		
		homePanel = new HomePanel();
		getContentPane().add(homePanel);
		pack();
	}
	
	@Override
	public void setBackground(Color bg) {
		super.setBackground(bg);
		if(homePanel != null) {
			homePanel.setBackground(bg);
		}
	}
	
	@Override
	public void setFont(Font font) {
		super.setFont(font);
		if(homePanel != null) {
			homePanel.setFont(font);
		}
	}
	
	public void setUser(String userName, String subjectId, boolean returningUser) {
		homePanel.setUser(userName, subjectId, returningUser);
		setTitle("Home - " + userName);
		pack();
	}
	
	/**
	 * Set the HTML options text.
	 * 
	 * @param text
	 */
	public void setOptionsText(String text) {
		homePanel.setOptionsText(text);
	}
	
	public void addButtonPressListener(ButtonPressListener listener) {
		homePanel.addButtonPressListener(listener);
	}
	
	public void removeButtonPressListener(ButtonPressListener listener) {
		homePanel.removeButtonPressListener(listener);
	}
	
	public void addHyperlinkListener(HyperlinkListener listener) {
		homePanel.addHyperlinkListener(listener);
	}
	
	public void removeHyperlinkListener(HyperlinkListener listener) {
		homePanel.removeHyperlinkListener(listener);
	}	
	
	public static void main(String[] args) {
		HomeDialog dlg = new HomeDialog(null);
		dlg.setOptionsText("Test Option 1<br>Test Option 2");
		dlg.setUser("cbonaceto@mitre.org", "1", true);
		dlg.setVisible(true);
		dlg.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dlg.setVisible(true);
	}
}