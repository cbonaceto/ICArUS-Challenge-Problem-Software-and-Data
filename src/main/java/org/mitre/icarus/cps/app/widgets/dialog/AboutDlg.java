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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import org.mitre.icarus.cps.app.widgets.ImageManager;
import org.mitre.icarus.cps.app.widgets.WidgetConstants;

/**
 * Dialog for showing the "About" text (typically accessed from the Help menu).
 * 
 * @author CBONACETO
 *
 */
public class AboutDlg {	
	
	/**
	 * @param parentComponent
	 */
	public static void showDefaultAboutDlg(Component parent, boolean fouo, boolean publicReleased, 
			String applicationName, String applicationVersion) {
		showAboutDlg(parent, "About " + applicationName, 
				createDefaultAboutText(fouo, publicReleased, applicationName, applicationVersion), 
				ImageManager.getImage(ImageManager.MITRE_LOGO));		
	}
	
	/**
	 * @param fouo
	 * @param publicReleased
	 * @param applicationName
	 * @param applicationVersion
	 * @return
	 */
	protected static String createDefaultAboutText(boolean fouo, boolean publicReleased, String applicationName, String applicationVersion) {
		StringBuilder sb = new StringBuilder("<html><font face=\"arial\" size=\"4\">");
		sb.append(applicationName + ", Version " + applicationVersion + "<br><br>");
		sb.append("This software was produced for the U. S. Government under<br>");
		sb.append("contract 2009-0917826-016, and is subject to the Rights in Data-General<br>");
		sb.append(" Clause 52.227-14 (JUNE 1987) or (DEC 2007).<br><br>");
		if(!publicReleased) {
			sb.append("No other use other than that granted to the U. S. Government, or to those acting<br>");
			sb.append(" on behalf of the U. S. Government under that Clause is authorized without the <br>");
			sb.append("express written permission of The MITRE Corporation.<br><br>");
			sb.append("For further information, please contact The MITRE Corporation, Contracts<br>");
			sb.append("Office, 7515 Colshire Drive, McLean, VA  22102-7539, (703) 983-6000.<br><br>");
		}
		sb.append("<b><u>Copyright ");
		sb.append(Integer.toString(Calendar.getInstance().get(Calendar.YEAR)));
		sb.append(" The MITRE Corporataion. All Rights Reserved.</b></u><br>");
		if(fouo) {
			if(!publicReleased) {
				sb.append("This software is <b>For Official Use Only and Not For Public Release.</b>");
			} else {
				sb.append("This software is <b>For Official Use Only.</b>");
			}
		} else if(!publicReleased) {
			sb.append("This software is <b>Not For Public Release.</b>");
		}
		sb.append("</html>");
		return sb.toString();
	}

	/**
	 * Show a modal "About" dialog with the given text and image.
	 * 
	 * @param parentComponent
	 * @param image
	 * @param text
	 */
	public static void showAboutDlg(Component parent, String title, String text, Image image) {
		final JDialog aboutDlg;
		if(parent instanceof Window) {
			aboutDlg = new JDialog((Window)parent);
		} else {
			aboutDlg = new JDialog();
		}
		aboutDlg.setTitle(title);
		aboutDlg.setModal(true);
		aboutDlg.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		aboutDlg.setResizable(false);
		
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBackground(Color.WHITE);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = WidgetConstants.INSETS_DEFAULT;
		
		//Add image if present
		if(image != null) {
			JLabel imageLabel = new JLabel(new ImageIcon(image));
			gbc.insets.left = 10;
			panel.add(imageLabel, gbc);
			gbc.insets.left = WidgetConstants.INSETS_DEFAULT.left;
		}
		
		//Add text block
		JEditorPane textPane = new JEditorPane();
		textPane.setContentType("text/html");
		textPane.setFont(WidgetConstants.FONT_INSTRUCTION_BANNER);
		textPane.setEditable(false);
		textPane.setText(text);
		gbc.gridx = 1;
		panel.add(textPane, gbc);
		
		//Add panel with image and text to dialog
		aboutDlg.getContentPane().setLayout(new GridBagLayout());
		gbc.gridx = 0;
		gbc.insets = new Insets(0, 0, 0, 0);
		aboutDlg.getContentPane().add(panel, gbc);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridy++;
		aboutDlg.getContentPane().add(new JSeparator(), gbc);
		
		//Add OK button to dialog
		final JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				aboutDlg.dispose();
			}
		});
		gbc.gridy++;
		gbc.insets.top = 10;
		gbc.insets.bottom = 5;
		gbc.fill = GridBagConstraints.NONE;		
		aboutDlg.getContentPane().add(okButton, gbc);		
		
		aboutDlg.pack();
		if(parent != null) {
			aboutDlg.setLocationRelativeTo(parent);
		}
		aboutDlg.setVisible(true);		
	}
	
	/** Test main */
	public static void main(String[] args) {
		 showDefaultAboutDlg(null, true, false, "ICArUS T&E GUI", "2.2a"); 
	}
}