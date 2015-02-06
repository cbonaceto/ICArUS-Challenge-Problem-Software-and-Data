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
package org.mitre.icarus.cps.app.widgets.dialog;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import org.mitre.icarus.cps.app.widgets.WidgetConstants;

/**
 * Dialog to display XML validation errors encountered when parsing XML files.
 * 
 * @author CBONACETO
 *
 */
public class ValidationErrorsDlg {
	
	public static void showErrorDialog(Component parent, String dialogTitle,
			String message, String validationErrors) {

		//Show error dialog
		final JDialog errorDlg;
		if(parent instanceof Window) {
			errorDlg = new JDialog((Window)parent);
		} else {
			errorDlg = new JDialog();	
		}
		errorDlg.setTitle(dialogTitle);
		errorDlg.setModal(true);
		errorDlg.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		errorDlg.setResizable(false);

		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.insets = WidgetConstants.INSETS_DEFAULT;
		gbc.insets.top = WidgetConstants.VERTICAL_SPACER;
		gbc.insets.bottom = 0;

		JLabel errorLabel = new JLabel();
		errorLabel.setIcon(UIManager.getIcon("OptionPane.errorIcon"));
		errorLabel.setForeground(Color.red);
		errorLabel.setText(message);
		panel.add(errorLabel, gbc);

		JTextArea errorDetails = new JTextArea();
		errorDetails.setForeground(Color.red);
		errorDetails.setRows(5);
		errorDetails.setEditable(false);
		errorDetails.setText(validationErrors);
		JScrollPane scrollPane = new JScrollPane(errorDetails);
		scrollPane.setPreferredSize(new Dimension(640, 480));
		gbc.gridy++;
		gbc.insets.top = WidgetConstants.COMPONENT_SPACER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel.add(scrollPane, gbc);
		gbc.fill = GridBagConstraints.NONE;

		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				errorDlg.dispose();
			}
		});
		gbc.gridy++;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets.top = WidgetConstants.COMPONENT_SPACER;
		gbc.insets.bottom = WidgetConstants.COMPONENT_SPACER;
		panel.add(okButton, gbc);

		errorDlg.setContentPane(panel);
		errorDlg.pack();
		if(parent != null) {
			errorDlg.setLocationRelativeTo(parent);
		}
		errorDlg.setVisible(true);		
	}
	
	public static void main(String[] args) {
		ErrorDlg.showErrorDialog(null, new Exception("Test Error"), true);
	}
}
