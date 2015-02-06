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
package org.mitre.icarus.cps.app.window.phase_1;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.mitre.icarus.cps.app.experiment.IcarusExamController;
import org.mitre.icarus.cps.app.experiment.IcarusSubjectData;
import org.mitre.icarus.cps.app.widgets.ImageManager;
import org.mitre.icarus.cps.app.widgets.WidgetConstants;
import org.mitre.icarus.cps.app.widgets.dialog.ErrorDlg;
import org.mitre.icarus.cps.app.widgets.security_banner.JSecurityBanner;
import org.mitre.icarus.cps.app.widgets.security_banner.JSecurityBanner.Classification;
import org.mitre.icarus.cps.exam.base.IcarusExam;
import org.mitre.icarus.cps.web.model.Site;

/**
 * @author CBONACETO
 *
 */
public abstract class AbstractIcarusTEGUI<
	EC extends IcarusExamController<E, ?, ?, ?, ?>,
	E extends IcarusExam<?>> {
	
	/** The application frame */
	protected JFrame frame;
	
	/** The application name */
	protected String applicationName;
	
	/** Contains all panels (aside from the security banner panel) */
	protected JPanel contentPanel;
	
	/** URL to the current exam */
	protected URL examFileURL = null;

	/** The current exam */
	protected E exam;
	
	/** The default exam */
	protected E defaultExam;
	
	/** The exam controller */	
	protected EC examController;	
	
	/**
	 * Create the application frame with the given title. Does not initialize the contentPanel (see initializeFrame). 
	 * 
	 * @param title
	 */
	protected void createFrame(String applicationName) {
		this.applicationName = applicationName;
		frame = new JFrame(applicationName);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Image icon = ImageManager.getImage(ImageManager.ICARUS_LOGO);
		if(icon != null) {
			frame.setIconImage(icon);
		}
	}
	
	/**
	 * @param showSecurityBanner
	 */
	protected void initializeFrame(boolean showSecurityBanner, boolean fouo) {
		frame.setResizable(true);
		frame.setVisible(false);
		frame.getContentPane().removeAll();
		
		frame.getContentPane().setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.NORTH;		
		constraints.gridy = 0;	
		constraints.weightx = 1;
		constraints.weighty = 1;
		
		//Add the content panel
		contentPanel = new JPanel(new BorderLayout());
		constraints.fill = GridBagConstraints.BOTH;
		frame.getContentPane().add(contentPanel, constraints);
		
		if(showSecurityBanner) {
			//Add the security banner panel
			constraints.gridy++;
			constraints.fill = GridBagConstraints.HORIZONTAL;
			constraints.weighty = 0;
			frame.getContentPane().add(WidgetConstants.createDefaultSeparator(), constraints);
			constraints.gridy++;
			String year = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
			frame.getContentPane().add(new JSecurityBanner(JSecurityBanner.COPYRIGHT_CHAR + 
					" " + year + " The MITRE Corporation. All Rights Reserved. ", 
					fouo ? Classification.FOUO : Classification.None), constraints);			
		}
	}
	
	/** 
	 * Confirm whether to exit the application
	 */
	protected void confirmExit() {		
		if(JOptionPane.showConfirmDialog(frame, "Would you like to exit the application?", "", 
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}
	
	/** Loads an exam. */
	protected boolean openExam(URL examFileURL) {
		try {
			exam = loadExam(examFileURL);
			if(exam != null) {		
				exam.setOriginalPath(examFileURL);
			}
		} catch (Exception e) {
			handleException(new Exception("Error loading exam file", e), true);
			return false;
		}		
		return true;
	}
	
	/** Loads the current default exam. */	
	protected boolean openDefaultExam() {
		if(defaultExam == null) {
			try {
				examFileURL = getDefaultExamURL();
			} catch (Exception e) {
				handleException(new Exception("Error loading current exam", e), true);
			}
			boolean success = openExam(examFileURL);
			defaultExam = exam;
			return success;
		}
		return (defaultExam != null);
	}
	
	protected abstract URL getDefaultExamURL() throws MalformedURLException;
	
	protected abstract E loadExam(URL examUrl) throws Exception;
	
	/** Shows an exam in the GUI */
	protected boolean showExam(E exam, String subjectId, Site site) {
		if(examController == null) {
			createExamController();
		}
		if(exam != null) {			
			try {
				frame.setTitle(applicationName + " - " + new File(examFileURL.toURI()).getName());
			} catch(Exception ex) {}
			examController.initializeExperimentController(exam);
			contentPanel.removeAll();
			contentPanel.add(examController.getExperimentPanel().getExperimentPanelComponent());
			contentPanel.validate();
			contentPanel.repaint();
			examController.startExperiment(new IcarusSubjectData(subjectId, site, 0));
			return true;
		}
		return false;
	}	
	
	protected abstract EC createExamController();	
	
	protected void handleException(Exception ex, boolean showStackTrace) {
		ErrorDlg.showErrorDialog(frame, ex, showStackTrace);
	}
	
	/**
	 * Finds the URL to a local file. Returns null if the file cannot be found. 
	 */
	public static URL loadFile(String path) {
		try {
			URL fileURL = ClassLoader.getSystemResource(path);
			if (fileURL != null) {
				return fileURL;
			} 
			File file = new File(path);
			if (file.exists()) {
				return file.toURI().toURL();
			}
		} catch (IllegalArgumentException e) {
			System.err.println("Could not find file " + path);
			e.printStackTrace();
		} catch (MalformedURLException e) {
			System.err.println("Could not open file " + path);
			e.printStackTrace();
		}
		return null;
	}
}