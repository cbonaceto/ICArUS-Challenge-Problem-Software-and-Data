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
package org.mitre.icarus.cps.app.widgets.phase_05.dialog;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Dialog to select a single feature vector (and associated object palette)
 * to open. 
 * 
 * @author CBONACETO
 *
 */
public class FeatureVectorSelectionDlg {
	
	//private static final long serialVersionUID = 1L;
	
	public static final int OK = 1;
	
	public static final int CANCEL = 0;	
	
	protected JDialog dlg;
	
	protected int buttonPressed = CANCEL;
	
	protected JTextField featureVectorField;
	
	protected JTextField objectPaletteField;
	
	protected static JFileChooser featureFileChooser;
	
	protected static JFileChooser objectFileChooser;
	
	protected FeatureVectorSelectionDlg() {
		dlg = new JDialog();
		dlg.setTitle("Open Feature Vector");
		dlg.setModal(true);
		
		JPanel dlgPanel = new JPanel(new GridBagLayout());
		dlgPanel.setBorder(BorderFactory.createEmptyBorder(20, 5, 10, 5));
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets.right = 5;
		gbc.insets.bottom = 5;
		
		JLabel featureLabel = new JLabel("Feature Vector File:"); 
		dlgPanel.add(featureLabel, gbc);
		featureVectorField = new JTextField();
		featureVectorField.setPreferredSize(new Dimension(
				270, featureVectorField.getPreferredSize().height));
		gbc.gridx++;
		dlgPanel.add(featureVectorField, gbc);
		
		JButton featureBrowseButton = new JButton("Browse...");
		featureBrowseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(featureFileChooser == null) {
					featureFileChooser = new JFileChooser(".");
					featureFileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
					featureFileChooser.setDialogTitle("Choose Feature Vector File");
					featureFileChooser.setAcceptAllFileFilterUsed(false);
					featureFileChooser.setFileFilter(new FileNameExtensionFilter("Feature Vector Files (*.csv)", "csv"));
				}						
				if(featureFileChooser.showOpenDialog(dlg) == JFileChooser.APPROVE_OPTION) {
					File f = featureFileChooser.getSelectedFile();
					if(f != null) {
						featureVectorField.setText(f.getAbsolutePath());
					}
				}
			}
		});
		gbc.gridx++;
		gbc.insets.right = 0;		
		dlgPanel.add(featureBrowseButton, gbc);		
		
		gbc.gridy++;
		gbc.gridx = 0;
		gbc.insets.right = 5;	
		gbc.insets.bottom = 5;
		JLabel objectLabel = new JLabel("Object Palette File:");
		objectLabel.setPreferredSize(new Dimension(
				featureLabel.getPreferredSize().width, objectLabel.getPreferredSize().height));
		dlgPanel.add(objectLabel, gbc);
		objectPaletteField = new JTextField();
		objectPaletteField.setPreferredSize(new Dimension(
				270, objectPaletteField.getPreferredSize().height));
		gbc.gridx++;
		dlgPanel.add(objectPaletteField, gbc);		
		
		JButton objectBrowseButton = new JButton("Browse...");
		objectBrowseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(objectFileChooser == null) {
					objectFileChooser = new JFileChooser(".");
					objectFileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
					objectFileChooser.setDialogTitle("Choose Object Palette File");
					objectFileChooser.setAcceptAllFileFilterUsed(false);
					objectFileChooser.setFileFilter(new FileNameExtensionFilter("Object Palette Files (*.csv)", "csv"));
				}						
				if(objectFileChooser.showOpenDialog(dlg) == JFileChooser.APPROVE_OPTION) {
					File f = objectFileChooser.getSelectedFile();
					if(f != null) {
						objectPaletteField.setText(f.getAbsolutePath());
					}
				}
			}
		});
		gbc.gridx++;
		gbc.insets.right = 0;		
		dlgPanel.add(objectBrowseButton, gbc);
		
		JPanel buttonPanel = new JPanel();
		JButton okButton = new JButton("OK");	
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(validateFields()) {
					buttonPressed = OK;
					dlg.dispose();
				}
			}
		});
		buttonPanel.add(okButton, gbc);
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dlg.dispose();
			}
		});
		buttonPanel.add(cancelButton, gbc);
		
		gbc.gridy++;		
		gbc.gridx = 0;
		gbc.gridwidth = 3;
		gbc.insets.bottom = 0;
		dlgPanel.add(buttonPanel, gbc);
		
		dlg.getContentPane().add(dlgPanel);
		dlg.pack();
	}
	
	/** Make sure feature vector and object palette fields contain text */
	protected boolean validateFields() {		
		boolean featureFileInvalid = 
			(featureVectorField.getText() == null || featureVectorField.getText().isEmpty());
			
		boolean objectFileInvalid = 
			(objectPaletteField.getText() == null || objectPaletteField.getText().isEmpty());
		
		if(featureFileInvalid || objectFileInvalid) {
			
			String message = null;
			
			if(featureFileInvalid && objectFileInvalid) {
				message = "Please enter a Feature Vector File and an Object Palette File.";
			}
			else if(featureFileInvalid) {
				message = "Please enter a Feature Vector File.";
			}
			else {
				message = "Please enter an Object Palette File.";
			}
			
			JOptionPane.showMessageDialog(dlg, message, "Error Occured", JOptionPane.ERROR_MESSAGE);
			
			return false;
		}
		
		return true;
	}
	
	public static FeatureVectorFiles showDialog(Component parent) {
		return showDialog(parent, null, null);
	}
	
	public static FeatureVectorFiles showDialog(Component parent, File currentFeatureFile, File currentObjectFile) {
		FeatureVectorSelectionDlg dlg = new FeatureVectorSelectionDlg();		
		dlg.dlg.setLocationRelativeTo(parent);
		if(currentFeatureFile != null) {
			dlg.featureVectorField.setText(currentFeatureFile.getAbsolutePath());
		}
		if(currentObjectFile != null) {
			dlg.objectPaletteField.setText(currentObjectFile.getAbsolutePath());
		}
		
		dlg.dlg.setVisible(true);
		
		if(dlg.buttonPressed == OK) {
			return new FeatureVectorFiles(
					new File(dlg.featureVectorField.getText()),
					new File(dlg.objectPaletteField.getText()));
		}
		
		return null;		
	}
	
	public static void main(String[] args) {
		FeatureVectorSelectionDlg.showDialog(null);
	}
	
	public static class FeatureVectorFiles {
		public final File featureVectorFile;
		
		public final File objectPaletteFile;
		
		protected FeatureVectorFiles(File featureVectorFile,
				File objectPaletteFile) {
			this.featureVectorFile = featureVectorFile;
			this.objectPaletteFile = objectPaletteFile; 
		}

		public File getFeatureVectorFile() {
			return featureVectorFile;
		}

		public File getObjectPaletteFile() {
			return objectPaletteFile;
		}
	}
}