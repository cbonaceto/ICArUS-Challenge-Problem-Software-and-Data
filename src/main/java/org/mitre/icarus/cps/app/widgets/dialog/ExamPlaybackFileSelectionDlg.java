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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.mitre.icarus.cps.app.window.configuration.FileDescriptor;
import org.mitre.icarus.cps.app.window.phase_1.Phase_1_Configuration;

/**
 * 
 * Dialog for selecting exam results files to play back.
 * 
 * @author CBONACETO
 *
 */
public class ExamPlaybackFileSelectionDlg {	
    
	public static final int OK = 1;	
	
	public static final int CANCEL = 0;	
	
	protected JDialog dlg;
	
	protected int buttonPressed = CANCEL;
	
	protected List<JTextField> fileFields;	
	
	protected List<JFileChooser> fileChoosers;
	
	protected List<FileDescriptor> examPlaybackFileTypes;
	
	protected ExamPlaybackFileSelectionDlg(String title, List<FileDescriptor> examPlaybackFileTypes) {
		this.examPlaybackFileTypes = examPlaybackFileTypes;
		createDlg(title);
	}	
	
	public static List<File> showDialog(Component parent, String title,
			List<FileDescriptor> examPlaybackFileTypes) {
		return showDialog(parent, title, examPlaybackFileTypes, null);
	}
	
	public static List<File> showDialog(Component parent, String title,
			List<FileDescriptor> examPlaybackFileTypes,			
			List<File> currentExamPlaybackFiles) {
		ExamPlaybackFileSelectionDlg dlg = new ExamPlaybackFileSelectionDlg(title, examPlaybackFileTypes);		
		dlg.dlg.setLocationRelativeTo(parent);
		if(currentExamPlaybackFiles != null && currentExamPlaybackFiles.size() == dlg.fileChoosers.size()) {
			int i = 0;
			for(File currentExamPlaybackFile : currentExamPlaybackFiles) {
				if(currentExamPlaybackFile != null) {
					dlg.fileChoosers.get(i).setCurrentDirectory(currentExamPlaybackFile);
					if(currentExamPlaybackFile.isFile()) {
						dlg.fileFields.get(i).setText(currentExamPlaybackFile.getAbsolutePath());
					}
				}
				i++;
			}
		}		
		dlg.dlg.setVisible(true);		
		if(dlg.buttonPressed == OK) {
			List<File> files = new ArrayList<File>(dlg.fileFields.size());
			for(JTextField fileField : dlg.fileFields) {
				files.add(new File(fileField.getText()));
			}
			return files;			
		}		
		return null;		
	}
	
	public void setCurrentFileFolder(File currentFolder, int fileIndex) {
		if(currentFolder != null && fileChoosers != null && fileIndex < fileChoosers.size()) {
			fileChoosers.get(fileIndex).setCurrentDirectory(currentFolder);
		}
	}
	
	public void setCurrentFileFolders(List<File> currentFolders) {
		if(currentFolders != null && fileChoosers != null && currentFolders.size() == fileChoosers.size()) {
			int i = 0;
			for(File currentFolder : currentFolders) {
				if(currentFolder != null) {
					fileChoosers.get(i).setCurrentDirectory(currentFolder);
				}
				i++;
			}				
		}
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
		
		fileChoosers = new ArrayList<JFileChooser>(examPlaybackFileTypes.size());
		fileFields = new ArrayList<JTextField>(examPlaybackFileTypes.size());
		for(FileDescriptor fileType : examPlaybackFileTypes) {
			final JFileChooser fileChooser = createFileChooser(fileType);
			fileChoosers.add(fileChooser);
			gbc.gridx = 0;
			gbc.insets.right = 5;
			JLabel label = new JLabel(fileType.getFileTypeTitle() + " File:"); 
			dlgPanel.add(label, gbc);
			final JTextField fileField = new JTextField();
			fileFields.add(fileField);
			fileField.setPreferredSize(new Dimension(270, fileField.getPreferredSize().height));
			gbc.gridx++;
			dlgPanel.add(fileField, gbc);
			JButton browseButton = new JButton("Browse...");
			browseButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//JFileChooser fileChooser = fileChoosers.get(fileChooserIndex);
					if(fileChooser.showOpenDialog(dlg) == JFileChooser.APPROVE_OPTION) {
						File f = fileChooser.getSelectedFile();
						if(f != null) {
							fileField.setText(f.getAbsolutePath());
						}
					}
				}
			});
			gbc.gridx++;
			gbc.insets.right = 0;		
			dlgPanel.add(browseButton, gbc);
			gbc.gridy++;
		}		
		
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
	
	private static JFileChooser createFileChooser(FileDescriptor fileType) {
		JFileChooser fileChooser = new JFileChooser(".");
		fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
		fileChooser.setDialogTitle("Choose " + fileType.getFileTypeTitle() + " File");
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setFileFilter(new FileNameExtensionFilter(fileType.getFileTypeDescription(), fileType.getExtenions()));
		return fileChooser;
	}	
	
	/** Make sure file fields for required files contain text */
	//TODO: Check to see if file is required
	protected boolean validateFields() {		
		boolean fieldsValid = true;
		StringBuilder message = new StringBuilder();
		int i = 0;
		for(JTextField fileField : fileFields) {
			if(fileField.getText() == null || fileField.getText().isEmpty()) {
				message.append("Please enter a(n) " + examPlaybackFileTypes.get(i).getFileTypeTitle() + " file name." + "\n");
				fieldsValid = false;
			}
			i++;
		}				
		if(!fieldsValid) {
			JOptionPane.showMessageDialog(dlg, message.toString(), "An Error Occured", JOptionPane.ERROR_MESSAGE);
		}		
		return fieldsValid;
	}	
	
	public static void main(String[] args) {
		ExamPlaybackFileSelectionDlg.showDialog(null, "Choose Exam Playback Files", 
				Phase_1_Configuration.createDefaultConfiguration().getExamPlaybackFileTypes());
	}
}