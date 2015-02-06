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
import org.mitre.icarus.cps.app.widgets.util.WidgetUtils;

/**
 * Dialog for showing an error message.
 * 
 * @author CBONACETO
 *
 */
public class ErrorDlg {
	public static void showErrorDialog(Component parent, Exception error, boolean showStackTrace) {
		//Print the stacktrace in the console
		if(showStackTrace) {
			error.printStackTrace();
		}
		
		//Show error dialog
		final JDialog errorDlg;
		if(parent instanceof Window) {
			errorDlg = new JDialog((Window)parent);
		} else {
			errorDlg = new JDialog();	
		}
		errorDlg.setTitle("Error Occured");
		errorDlg.setModal(true);
		errorDlg.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		//errorDlg.setResizable(false);
		
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
		String detailsString = "";
		if(showStackTrace) {
			detailsString = ", details";
		}
		if(error.getMessage() != null) {
			errorLabel.setText(WidgetUtils.formatMultilineString(error.getMessage() + detailsString, 100,
					WidgetUtils.LEFT));
		}
		else {
			errorLabel.setText("An error occured" + detailsString);
		}
		panel.add(errorLabel, gbc);
		
		if(showStackTrace) {
			//Format stack trace error message
			StringBuilder errorMessage = new StringBuilder();
			
			if(error.getCause() != null) {				
				errorMessage.append(error.getCause().toString());
				errorMessage.append("\n");
			}
			if(error.getStackTrace() != null) {
				for(StackTraceElement element : error.getStackTrace()) {
					errorMessage.append("     at ");
					errorMessage.append(element.toString());
					errorMessage.append("\n");
				}
			}
			if(error.getCause() != null) {
				Throwable cause = error.getCause();
				errorMessage.append("Caused by: ");
				errorMessage.append(cause.getMessage());
				errorMessage.append("\n");
				if(cause.getStackTrace() != null && cause.getStackTrace().length > 0) {
					errorMessage.append("     at " + cause.getStackTrace()[0].toString());
					errorMessage.append("\n");
					if(cause.getStackTrace().length > 1) {
						errorMessage.append("     ... " + (cause.getStackTrace().length-1) + " more");
						errorMessage.append("\n");
					}
				}
			}
			
			JTextArea errorDetails = new JTextArea();
			errorDetails.setForeground(Color.red);
			errorDetails.setRows(5);
			errorDetails.setEditable(false);
			errorDetails.setText(errorMessage.toString());
			JScrollPane scrollPane = new JScrollPane(errorDetails);
			scrollPane.setPreferredSize(new Dimension(600, 400));
			gbc.gridy++;
			gbc.insets.top = WidgetConstants.COMPONENT_SPACER;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			panel.add(scrollPane, gbc);
			gbc.fill = GridBagConstraints.NONE;
		}
		
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
		errorDlg.setMinimumSize(errorDlg.getPreferredSize());
		if(parent != null) {
			errorDlg.setLocationRelativeTo(parent);
		}
		errorDlg.setVisible(true);		
		//JOptionPane.showMessageDialog(parentComponent, errorMessage.toString(), "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	public static void main(String[] args) {
		ErrorDlg.showErrorDialog(null, new Exception("Test Error"), true);
	}
}
