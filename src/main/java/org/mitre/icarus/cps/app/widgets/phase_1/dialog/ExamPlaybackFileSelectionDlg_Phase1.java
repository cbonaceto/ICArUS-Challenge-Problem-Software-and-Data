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
package org.mitre.icarus.cps.app.widgets.phase_1.dialog;

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
 * 
 * Dialog to select exam response files to play back in Phase 1.
 * 
 * @author CBONACETO
 *
 */
public class ExamPlaybackFileSelectionDlg_Phase1 {
	
	public static final int OK = 1;	
	public static final int CANCEL = 0;	
	
	protected JDialog dlg;
	
	protected int buttonPressed = CANCEL;
	
	protected JTextField examFileField;	
	protected JTextField examResponseFileField;	
	protected JTextField avgHumanDataSetFileField;
	
	protected static JFileChooser examFileChooser;	
	protected static JFileChooser examResponseFileChooser;	
	protected static JFileChooser avgHumanDataSetFileChooser;
	
	protected ExamPlaybackFileSelectionDlg_Phase1(String title) {
		createDlg(title);
	}	
	
	public static ExamPlaybackFileSet showDialog(Component parent, String title) {
		return showDialog(parent, title, null, null, null);
	}
	
	public static ExamPlaybackFileSet showDialog(Component parent, String title,
			File currentExamFile, File currentExamResponseFile,
			File currentAvgHumanDataSetFile) {
		ExamPlaybackFileSelectionDlg_Phase1 dlg = new ExamPlaybackFileSelectionDlg_Phase1(title);		
		dlg.dlg.setLocationRelativeTo(parent);
		dlg.dlg.setResizable(false);
		if(currentExamFile != null) {
			examFileChooser.setCurrentDirectory(currentExamFile);
			dlg.examFileField.setText(currentExamFile.getAbsolutePath());			
		}
		if(currentExamResponseFile != null) {
			examResponseFileChooser.setCurrentDirectory(currentExamResponseFile);
			dlg.examResponseFileField.setText(currentExamResponseFile.getAbsolutePath());
		}
		if(currentAvgHumanDataSetFile != null) {
			avgHumanDataSetFileChooser.setCurrentDirectory(currentAvgHumanDataSetFile);
			dlg.avgHumanDataSetFileField.setText(currentAvgHumanDataSetFile.getAbsolutePath());
		}
		
		dlg.dlg.setVisible(true);
		
		if(dlg.buttonPressed == OK) {
			return new ExamPlaybackFileSet(
					new File(dlg.examFileField.getText()),
					new File(dlg.examResponseFileField.getText()),
					new File(dlg.avgHumanDataSetFileField.getText()));
		}
		
		return null;		
	}
	
	public static void setCurrentExamFolder(File currentExamFolder) {
		if(examFileChooser == null) {
			createExamFileChooser();
		}	
		examFileChooser.setCurrentDirectory(currentExamFolder);
	}
	
	public static void setCurrentExamResponseFolder(File currentExamResponseFolder) {
		if(examResponseFileChooser == null) {
			createExamResponseFileChooser();
		}	
		examResponseFileChooser.setCurrentDirectory(currentExamResponseFolder);
	}
	
	public static void setCurrentAvgHumanDataSetFolder(File currentAvgHumanDataSetFolder) {
		if(avgHumanDataSetFileChooser == null) {
			createAvgHumanDataSetFileChooser();
		}	
		avgHumanDataSetFileChooser.setCurrentDirectory(currentAvgHumanDataSetFolder);
	}
	
	protected void createDlg(String title) {
		dlg = new JDialog();
		dlg.setTitle(title);
		dlg.setModal(true);
		
		JPanel dlgPanel = new JPanel(new GridBagLayout());
		dlgPanel.setBorder(BorderFactory.createEmptyBorder(20, 5, 10, 5));
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.insets.right = 5;
		gbc.insets.bottom = 5;
		
		JLabel label = new JLabel("Exam File:"); 
		dlgPanel.add(label, gbc);
		examFileField = new JTextField();
		examFileField.setPreferredSize(new Dimension(270, examFileField.getPreferredSize().height));
		gbc.gridx++;
		dlgPanel.add(examFileField, gbc);
		
		if(examFileChooser == null) {
			createExamFileChooser();
		}	
		if(examResponseFileChooser == null) {
			createExamResponseFileChooser();
		}	
		if(avgHumanDataSetFileChooser == null) {
			createAvgHumanDataSetFileChooser();
		}	
		
		JButton browseButton = new JButton("Browse...");
		browseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {								
				if(examFileChooser.showOpenDialog(dlg) == JFileChooser.APPROVE_OPTION) {
					File f = examFileChooser.getSelectedFile();
					if(f != null) {
						examFileField.setText(f.getAbsolutePath());
					}
				}
			}
		});
		gbc.gridx++;
		gbc.insets.right = 0;		
		dlgPanel.add(browseButton, gbc);		
		
		gbc.gridy++;
		gbc.gridx = 0;
		gbc.insets.right = 5;	
		gbc.insets.bottom = 5;
		label = new JLabel("Exam Response File:");
		label.setPreferredSize(new Dimension(label.getPreferredSize().width, label.getPreferredSize().height));
		dlgPanel.add(label, gbc);
		examResponseFileField = new JTextField();
		examResponseFileField.setPreferredSize(new Dimension(270, examResponseFileField.getPreferredSize().height));
		gbc.gridx++;
		dlgPanel.add(examResponseFileField, gbc);		
		
		browseButton = new JButton("Browse...");
		browseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {									
				if(examResponseFileChooser.showOpenDialog(dlg) == JFileChooser.APPROVE_OPTION) {
					File f = examResponseFileChooser.getSelectedFile();
					if(f != null) {
						examResponseFileField.setText(f.getAbsolutePath());
					}
				}
			}
		});
		gbc.gridx++;
		gbc.insets.right = 0;		
		dlgPanel.add(browseButton, gbc);		
		
		gbc.gridy++;
		gbc.gridx = 0;
		gbc.insets.right = 5;	
		gbc.insets.bottom = 5;
		label = new JLabel("Average Human Response File (Optional):");
		label.setPreferredSize(new Dimension(label.getPreferredSize().width, label.getPreferredSize().height));
		dlgPanel.add(label, gbc);
		avgHumanDataSetFileField = new JTextField();
		avgHumanDataSetFileField.setPreferredSize(new Dimension(270, avgHumanDataSetFileField.getPreferredSize().height));
		gbc.gridx++;
		dlgPanel.add(avgHumanDataSetFileField, gbc);		
		
		browseButton = new JButton("Browse...");
		browseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {									
				if(avgHumanDataSetFileChooser.showOpenDialog(dlg) == JFileChooser.APPROVE_OPTION) {
					File f = avgHumanDataSetFileChooser.getSelectedFile();
					if(f != null) {
						avgHumanDataSetFileField.setText(f.getAbsolutePath());
					}
				}
			}
		});
		gbc.gridx++;
		gbc.insets.right = 0;		
		dlgPanel.add(browseButton, gbc);
		
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
		gbc.anchor = GridBagConstraints.CENTER;
		dlgPanel.add(buttonPanel, gbc);
		
		dlg.getContentPane().add(dlgPanel);
		dlg.pack();
	}
	
	private static void createExamFileChooser() {		
		examFileChooser = new JFileChooser(".");
		examFileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
		examFileChooser.setDialogTitle("Choose Exam File");
		examFileChooser.setAcceptAllFileFilterUsed(false);
		examFileChooser.setFileFilter(new FileNameExtensionFilter("Exam Files (*.xml)", "xml"));
	}
	
	private static void createExamResponseFileChooser() {		
		examResponseFileChooser = new JFileChooser(".");
		examResponseFileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
		examResponseFileChooser.setDialogTitle("Choose Exam Response File");
		examResponseFileChooser.setAcceptAllFileFilterUsed(false);
		examResponseFileChooser.setFileFilter(new FileNameExtensionFilter("Exam Response Files (*.xml)", "xml"));
	}
	
	private static void createAvgHumanDataSetFileChooser() {		
		avgHumanDataSetFileChooser = new JFileChooser(".");
		avgHumanDataSetFileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
		avgHumanDataSetFileChooser.setDialogTitle("Choose Average Human Response File");
		avgHumanDataSetFileChooser.setAcceptAllFileFilterUsed(false);
		avgHumanDataSetFileChooser.setFileFilter(new FileNameExtensionFilter("Average Human Response Files (*.xml)", "xml"));
	}
	
	/** Make sure exam and exam response fields contain text. Avg human field can be blank. */
	protected boolean validateFields() {		
		boolean examFileInvalid = 
			(examFileField.getText() == null || examFileField.getText().isEmpty());
			
		boolean examResponseFileInvalid = 
			(examResponseFileField.getText() == null || examResponseFileField.getText().isEmpty());
		
		if(examFileInvalid || examResponseFileInvalid) {			
			String message = null;
			
			if(examFileInvalid && examResponseFileInvalid) {
				message = "Please enter an exam file name and an exam response file name.";
			} else if(examFileInvalid) {
				message = "Please enter an exam file name.";
			} else {
				message = "Please enter an exam response file name.";
			}			
			
			JOptionPane.showMessageDialog(dlg, message, "Error Occured", JOptionPane.ERROR_MESSAGE);			
			return false;
		}		
		return true;
	}	
	
	public static void main(String[] args) {
		ExamPlaybackFileSelectionDlg_Phase1.showDialog(null, "Choose Exam Files");
	}
	
	public static class ExamPlaybackFileSet {
		public final File examFile;
		
		public final File examResponseFile;
		
		public final File avgHumanDataSetFile;
		
		protected ExamPlaybackFileSet(File examFile, File examResponseFile, File avgHumanDataSetFile) {
			this.examFile = examFile;
			this.examResponseFile = examResponseFile;
			this.avgHumanDataSetFile = avgHumanDataSetFile;
		}

		public File getExamFile() {
			return examFile;
		}

		public File getExamResponseFile() {
			return examResponseFile;
		}

		public File getAvgHumanDataSetFile() {
			return avgHumanDataSetFile;
		}
	}
}