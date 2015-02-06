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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collection;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.mitre.icarus.cps.web.model.Site;

/**
 * Panel to enter a participant ID and select a site location.
 * 
 * @author CBONACETO
 *
 */
public class ParticipantIdPanel extends JPanel {
	protected static final long serialVersionUID = 1L;
	
	private final JTextField subjectIdField;
	
	@SuppressWarnings("rawtypes")
	private final JComboBox siteComboBox;
	
	private final JButton okButton;
	
	private final JButton cancelButton;
	
	/**
	 * @param sites
	 * @param okButtonText
	 * @param showCancelButton
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ParticipantIdPanel(Collection<Site> sites, String okButtonText, boolean showCancelButton) {
		super(new GridBagLayout());		
		JPanel gridPanel = new JPanel(new GridBagLayout());
		
		//Add subject ID field
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(4,4,4,4);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.0;
		gbc.weighty = 0.0;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets.bottom = 15;
		gbc.gridwidth = 2;
		gridPanel.add(new JLabel("<html>Enter a participant ID, select a site,<br> and click " + okButtonText + ":</html>"), gbc);
		
		gbc.insets.bottom = 4;
		gbc.gridwidth = 1;		
		gbc.gridy++;
		gridPanel.add(new JLabel("Participant ID:"), gbc);
		gbc.gridx = 1;
		gbc.weightx = 1.0;
		//gbc.fill = GridBagConstraints.HORIZONTAL;
		gridPanel.add(subjectIdField = new JTextField(10), gbc);		
		subjectIdField.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				checkId();
			}
			public void insertUpdate(DocumentEvent e) {
				checkId();
			}
			public void removeUpdate(DocumentEvent e) {
				checkId();
			}
		});
		
		//Add site selection combo box
		gbc.gridx = 0;		
		gbc.gridy++;
		gbc.weightx = 0.0;
		gridPanel.add(new JLabel("Site Location:"), gbc);
		if(sites == null || sites.isEmpty()) {
			siteComboBox = new JComboBox();
		} else {
			/*String[] siteNames = new String[sites.size()];
			int i = 0;
			for(Site site : sites) {
				siteNames[i] = site.getName();
				i++;
			}*/
			siteComboBox = new JComboBox(sites.toArray());
		}
		gbc.gridx = 1;
		gbc.weightx = 1.0;
		gridPanel.add(siteComboBox, gbc);			
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;
		gbc.anchor = GridBagConstraints.CENTER;
		//gbc.fill = GridBagConstraints.HORIZONTAL;
		add(gridPanel, gbc);
		
		//Add OK and Cancel buttons
		JPanel buttonPanel = new JPanel();		
		
		okButton = new JButton(okButtonText);
		okButton.setEnabled(false);
		buttonPanel.add(okButton);
		
		if(showCancelButton) {
			cancelButton = new JButton("Cancel");
			buttonPanel.add(cancelButton);
		}
		else {
			cancelButton = null;
		}
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0.0;
		gbc.weighty = 0.0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.CENTER;
		add(buttonPanel, gbc);
	}	
	
	private void checkId() {	
		okButton.setEnabled(subjectIdField.getText()!= null && !subjectIdField.getText().isEmpty());
	}	
	
	/**
	 * @param listener
	 */
	public void addOkButtonActionListener(ActionListener listener) {
		okButton.addActionListener(listener);
	}
	
	public void addCancelButtonActionListener(ActionListener listener) {
		if(cancelButton != null) {
			cancelButton.addActionListener(listener);
		}
	} 
	
	public String getSubjectId() {
		return subjectIdField.getText();
	}	
	
	public void setSubjectId(String subjectId) {
		subjectIdField.setText(subjectId);
	}
	
	public Site getSite() {
		if(siteComboBox.getSelectedItem() != null) {
			return (Site)siteComboBox.getSelectedItem();
		}
		return null;
	}
	
	public void setSite(Site site) {
		siteComboBox.setSelectedItem(site);
	}
	
	public static void main(String[] args) {
		JFrame app = new JFrame("Test Dlg");
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		app.getContentPane().add(new ParticipantIdPanel(Arrays.asList(new Site("Bedford"), new Site("McLean")),
				"Begin", false));
		app.pack();
		app.setVisible(true);
	}
}

