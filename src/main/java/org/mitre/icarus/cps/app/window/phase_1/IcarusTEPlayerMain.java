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
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.mitre.icarus.cps.app.experiment.IcarusSubjectData;
import org.mitre.icarus.cps.app.experiment.phase_1.player.PlayerController;
import org.mitre.icarus.cps.app.util.CPSUtils;
import org.mitre.icarus.cps.app.widgets.WidgetConstants;
import org.mitre.icarus.cps.app.widgets.dialog.AboutDlg;
import org.mitre.icarus.cps.app.widgets.dialog.ErrorDlg;
import org.mitre.icarus.cps.app.widgets.phase_1.ImageManager_Phase1;
import org.mitre.icarus.cps.app.widgets.phase_1.dialog.ExamPlaybackFileSelectionDlg_Phase1;
import org.mitre.icarus.cps.app.widgets.phase_1.dialog.ExamPlaybackFileSelectionDlg_Phase1.ExamPlaybackFileSet;
import org.mitre.icarus.cps.app.widgets.phase_1.player.ConditionPanel_Player;
import org.mitre.icarus.cps.app.widgets.phase_1.player.ExperimentPanel_Player;
import org.mitre.icarus.cps.app.widgets.probability_entry.ProbabilityEntryComponentFactory;
import org.mitre.icarus.cps.app.widgets.probability_entry.ProbabilityEntryConstants;
import org.mitre.icarus.cps.app.widgets.progress_monitor.SwingProgressMonitor;
import org.mitre.icarus.cps.app.widgets.security_banner.JSecurityBanner;
import org.mitre.icarus.cps.app.widgets.security_banner.JSecurityBanner.Classification;
import org.mitre.icarus.cps.app.window.IcarusLookAndFeel;
import org.mitre.icarus.cps.assessment.data_model.phase_1.data_sets.AverageHumanDataSet_Phase1;
import org.mitre.icarus.cps.assessment.persistence.phase_1.xml.XMLCPADataPersister;
import org.mitre.icarus.cps.exam.phase_1.IcarusExam_Phase1;
import org.mitre.icarus.cps.exam.phase_1.loader.IcarusExamLoader_Phase1;
import org.mitre.icarus.cps.exam.phase_1.playback.ExamPlaybackDataSource_Phase1;
import org.mitre.icarus.cps.exam.phase_1.testing.TaskTestPhase;
import org.mitre.icarus.cps.web.model.Site;

/**
 * Main class for launching the player as an applet or stand-alone application.
 * 
 * This class is deprecated, the new main class for all Phases is 
 * org.mitre.icarus.cps.app.window.controller.ApplicationController.
 * 
 * @author CBONACETO
 *
 */
@Deprecated
public class IcarusTEPlayerMain extends JApplet {	
	private static final long serialVersionUID = 1557704725469224044L;

	public static enum ApplicationType {Applet, Application};
	
	/** The application/applet version information */
	public static final String VERSION = "1.0";
	
	/** The application name */
	public static final String APPLICATION_NAME = "ICArUS Test & Evaluation Player";
	
	/** The application type */
	protected ApplicationType applicationType;	
	
	/** Whether the application/applet has been initialized */
	protected boolean initialized;
	
	/** Application frame when running as an Application */
	protected JFrame frame;
	
	/** The file menu */
	protected JMenu fileMenu;
	
	/** Contains all panels (aside from the security banner panel) */
	protected JPanel contentPanel;	
	
	/** Exam file set file chooser dialog */
	protected ExamPlaybackFileSelectionDlg_Phase1 fileSelectionDlg;
	
	/** The exam player controller */
	protected PlayerController examPlayerController;
	
	/** The exam response to play back */
	protected ExamPlaybackDataSource_Phase1 examResponse;
	
	/** URL to the current exam file */
	protected URL examFileURL = null;
	
	/** URL to the current exam response file */
	protected URL examResponseFileURL = null;
	
	/** URL to the average human data set file */
	protected URL avgHumanDataSetFileURL = null;
	
	public IcarusTEPlayerMain() {
		initialized = false;
	}
	
	public IcarusTEPlayerMain(ApplicationType applicationType) {
		this(applicationType, true, Classification.FOUO);
	}
	
	private IcarusTEPlayerMain(ApplicationType applicationType, boolean showSecurityBanner, Classification classification) {
		if(applicationType == null) {
			throw new IllegalArgumentException("Application type must be one of Application or Applet");
		}
		initializeApplication(applicationType, showSecurityBanner, classification);
	}
	
	/**
	 * @param applicationType
	 */
	public void initializeApplication(final ApplicationType applicationType, final boolean showSecurityBanner, 
			final Classification classification) {
		if(!initialized) {			
			this.applicationType = applicationType;
			//Create the application/applet on the event dispatching thread
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {					
					//Make the probability bars smaller
					ProbabilityEntryComponentFactory.setDefaultBoxesSize(ProbabilityEntryConstants.BOX_SIZE_SMALL);
					
					if(applicationType == ApplicationType.Applet) {
						initializeApplet(showSecurityBanner, classification);						
					} else {						
						initializeFrame(showSecurityBanner, classification);
					}
					initialized = true;
				}
			});
		}
	}
	
	private void initializeApplet(boolean showSecurityBanner, Classification classification) {
		initializeContentPane(getContentPane(), showSecurityBanner, classification);
		createExamPlayerController();
		contentPanel.add(examPlayerController.getExperimentPanel());
		setSize(contentPanel.getPreferredSize());
		setPreferredSize(contentPanel.getPreferredSize());
		contentPanel.removeAll();
		validate();
		repaint();
	}
	
	private void initializeFrame(boolean showSecurityBanner, Classification classification) {
		frame = new JFrame(APPLICATION_NAME);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Image icon = ImageManager_Phase1.getImage(ImageManager_Phase1.ICARUS_LOGO);
		if(icon != null) {
			frame.setIconImage(icon);
		}
		frame.setResizable(true);
		frame.setVisible(false);
		
		//Initialize the content pane
		initializeContentPane(frame.getContentPane(), showSecurityBanner, classification);				
		
		//Create menu bar with file menu
		frame.setJMenuBar(createMenuBar(classification));
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Size the frame to fit the screen
		createExamPlayerController();
		contentPanel.add(examPlayerController.getExperimentPanel());
		frame.pack();
		//Adjust size of experiment panel by shrinking the map if frame size exceeds screen dimensions
		Dimension screenSize = frame.getToolkit().getScreenSize();
		int widthOver = frame.getWidth() - screenSize.width;
		int heightOver = frame.getHeight() - screenSize.height;
		if(widthOver > 0 || heightOver > 0) {
			Dimension experimentPanelSize = examPlayerController.getExperimentPanel().getPreferredSize();
			int newWidth = (widthOver > 0) ? experimentPanelSize.width - widthOver : experimentPanelSize.width;
			if(newWidth < 1) {
				newWidth = 1;
			}
			int newHeight = (heightOver > 0) ? experimentPanelSize.height - heightOver : experimentPanelSize.height;
			if(newHeight < 1) {
				newHeight = 1;
			}
			examPlayerController.getExperimentPanel().setMaximumSize(new Dimension(newWidth, newHeight));
			frame.pack();
		}		
		frame.setMinimumSize(frame.getSize());		
		CPSUtils.centerFrameOnScreen(frame);
		contentPanel.removeAll();
		frame.setVisible(true);		
		//System.out.println("Window Size: " + frame.getSize());
	}
	
	private void initializeContentPane(Container contentPane, boolean showSecurityBanner, Classification classification) {
		contentPane.removeAll();
		contentPane.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.NORTH;		
		constraints.gridy = 0;	
		constraints.weightx = 1;
		constraints.weighty = 1;
		
		//Add the content panel
		contentPanel = new JPanel(new BorderLayout());
		constraints.fill = GridBagConstraints.BOTH;
		contentPane.add(contentPanel, constraints);
		
		if(showSecurityBanner) {
			//Add the security banner panel
			constraints.gridy++;
			constraints.fill = GridBagConstraints.HORIZONTAL;
			constraints.weighty = 0;
			contentPane.add(WidgetConstants.createDefaultSeparator(), constraints);
			constraints.gridy++;
			String year = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
			contentPane.add(new JSecurityBanner(JSecurityBanner.COPYRIGHT_CHAR + 
					" " + year + " The MITRE Corporation. All Rights Reserved. ", 
					classification), constraints);			
		}
	}
	
	private void createExamPlayerController() {
		if(examPlayerController == null) {
			Component parent = applicationType == ApplicationType.Applet ? getContentPane() : frame;
			ConditionPanel_Player conditionPanel = new ConditionPanel_Player(parent);
			examPlayerController = new PlayerController();			
			ExperimentPanel_Player experimentPanel = new ExperimentPanel_Player(parent, conditionPanel);
			examPlayerController.setExperimentPanel(experimentPanel);
		}			
	}
	
	/** Create a menu bar with the file, feature vector, current exam, and help menu options */	
	protected JMenuBar createMenuBar(final Classification classification) {
		JMenuBar menuBar = new JMenuBar();

		// Create the file menu with options to open an exam file set and exit
		final JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		menuBar.add(fileMenu);
		
		final Component parent = applicationType == ApplicationType.Application ? frame : getContentPane();

		//Create open exam option
		JMenuItem openExamItem = new JMenuItem("Open Exam For Playback");
		openExamItem.setMnemonic(KeyEvent.VK_O);
		fileMenu.add(openExamItem);
		openExamItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ExamPlaybackFileSet examFiles = chooseExamPlaybackFiles();
				if(examFiles != null) {
					try{
						//TODO: Show progress dialog while files load and metrics are computed
						URL examFileURL = examFiles.getExamFile().toURI().toURL();
						URL examResponseFileURL = examFiles.getExamResponseFile().toURI().toURL();
						URL avgHumanDataSetFileURL = null;
						if(examFiles.getAvgHumanDataSetFile() != null) {
							avgHumanDataSetFileURL = examFiles.getAvgHumanDataSetFile().toURI().toURL();
						} 
						if(openExamResponse(examFileURL, examResponseFileURL, avgHumanDataSetFileURL)) {
							playExamResponse(examResponse);
						}
					} catch (MalformedURLException e) {
						ErrorDlg.showErrorDialog(parent, new Exception("Error loading exam response files", e), true);
					}				
				}
			}
		});  

		//Create exit menu item and add to file menu
		fileMenu.addSeparator();
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.setMnemonic(KeyEvent.VK_X);
		exitItem.setIcon(ImageManager_Phase1.getImageIcon(ImageManager_Phase1.EXIT_ICON));
		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		fileMenu.add(exitItem);

		//Create the exam options menu with options to skip to a trial or mission
		/*final JMenu examMenu = new JMenu("Options");
		examMenu.setEnabled(false);
		examMenu.setMnemonic(KeyEvent.VK_E);
		menuBar.add(examMenu);*/

		//Create the help menu with the about item
		final JMenu helpMenu = new JMenu("Help");
		menuBar.add(helpMenu);
		helpMenu.setMnemonic(KeyEvent.VK_H);
		JMenuItem aboutMenuItem = new JMenuItem("About " + APPLICATION_NAME);		
		aboutMenuItem.setMnemonic(KeyEvent.VK_A);
		//aboutMenuItem.setIcon(ImageManager.getImageIcon(ImageManager.ICARUS_LOGO));
		helpMenu.add(aboutMenuItem);
		aboutMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Show the About dialog
				AboutDlg.showDefaultAboutDlg(parent, classification == Classification.FOUO, false, 
						APPLICATION_NAME, VERSION);
			}			
		});

		return menuBar;
	}
	
	/** Shows a file chooser to open the exam response files. Returns null if no files were chosen. */
	protected ExamPlaybackFileSet chooseExamPlaybackFiles() {		
		try {
			return ExamPlaybackFileSelectionDlg_Phase1.showDialog(applicationType == ApplicationType.Application ? frame : getContentPane(),
					"Open Exam Response Files",	
					examFileURL != null ? new File(examFileURL.toURI()) : null, 
					examResponseFileURL != null ? new File(examResponseFileURL.toURI()) : null,
					avgHumanDataSetFileURL != null ? new File(avgHumanDataSetFileURL.toURI()) : null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/** Loads exam response files and create an examResponse instance. */
	protected boolean openExamResponse(URL examFileUrl, URL examResponseFileUrl, URL avgHumanDataSetUrl) {
		Component parent = applicationType == ApplicationType.Application ? frame : getContentPane();
		try {
			//Load the exam and initialize feature vector data
			SwingProgressMonitor progressMonitor = new SwingProgressMonitor(parent, 
					"Loading Feature Vector Data", 
					"Loading Feature Vector Data", 0, 100);
			IcarusExam_Phase1 exam = IcarusExamLoader_Phase1.unmarshalExam(examFileUrl);
			if(exam != null) {		
				exam.setOriginalPath(examFileUrl);
				if(exam.getTasks() != null && !exam.getTasks().isEmpty()) {
					for(TaskTestPhase<?> task : exam.getTasks()) {
						IcarusExamLoader_Phase1.initializeTaskFeatureVectorData(task, 
							exam.getOriginalPath(), exam.getGridSize(), false, progressMonitor);
					}
				}
			}
			
			//Load the exam response
			IcarusExam_Phase1 participantExamResponse = IcarusExamLoader_Phase1.unmarshalExam(examResponseFileUrl);
			
			//Load the average human data set
			AverageHumanDataSet_Phase1 avgHumanResponse = null;
			if(avgHumanDataSetUrl != null) {
				avgHumanResponse = XMLCPADataPersister.loadAverageHumanDataSet(avgHumanDataSetUrl);
			}
			
			if(examResponse == null) {
				examResponse = new ExamPlaybackDataSource_Phase1();
			}
			//Initialize the exam response object and compute metrics
			examResponse.initializeExamResponseData(exam, participantExamResponse, avgHumanResponse, 
					avgHumanResponse != null ? avgHumanResponse.getMetricsInfo() : null);
			this.examFileURL = examFileUrl;
			this.examResponseFileURL = examResponseFileUrl;
			this.avgHumanDataSetFileURL = avgHumanDataSetUrl;
		} catch (Exception e) {
			ErrorDlg.showErrorDialog(parent, new Exception("Error loading exam response files", e), true);
			return false;
		}		
		return true;
	}
	
	/**
	 * Shows an exam in the GUI.
	 * 
	 * @param examResponse
	 */
	public void playExamResponse(ExamPlaybackDataSource_Phase1 examResponse) {
		if(examResponse != null && examResponse.getExam() != null) {			
			try {
				if(applicationType == ApplicationType.Application) {
					frame.setTitle(APPLICATION_NAME + " - " + examResponse.getExam().getName());
				}
			} catch(Exception ex) {}
			examPlayerController.initializeExperimentController(examResponse);
			contentPanel.removeAll();
			contentPanel.add(examPlayerController.getExperimentPanel());
			contentPanel.validate();
			contentPanel.repaint();
			String subjectId = "?";
			String siteId = "?";
			if(examResponse.getParticipantExamMetrics() != null) {
				subjectId = examResponse.getParticipantExamMetrics().getResponse_generator_id() != null ? 
						examResponse.getParticipantExamMetrics().getResponse_generator_id() : subjectId;
				siteId = examResponse.getParticipantExamMetrics().getSite_id() != null ?
						examResponse.getParticipantExamMetrics().getSite_id() : siteId;
			}
			IcarusSubjectData subjectData = new IcarusSubjectData(subjectId, new Site(siteId, siteId), 0);
			subjectData.setCurrentTrial(-1);
			examPlayerController.startExperiment(subjectData);
		}
	}	

	/** Creates the applet version of the application */
	@Override
	public void init() {	
		initializeApplication(ApplicationType.Applet, true, Classification.FOUO);
	}	
	
	/**
	 * @return
	 */
	public boolean isShowIncompleteTrialWarnings() {
		return examPlayerController.isShowIncompleteTrialWarnings();
	}

	/**
	 * @param showIncompleteTrialWarnings
	 */
	public void setShowIncompleteTrialWarnings(boolean showIncompleteTrialWarnings) {
		examPlayerController.setShowIncompleteTrialWarnings(showIncompleteTrialWarnings);
	}
	
	/**
	 * @param taskId
	 * @param trialNumber
	 */
	public void advanceToTrial(String taskId, int trialNumber) {
		if(examPlayerController != null) {
			examPlayerController.advanceToTrial(taskId, trialNumber);
		}
	}		
	
	/**
	 * 
	 */
	public void advanceToNextStage() {		
		if(examPlayerController != null) {
			examPlayerController.advanceToNextStage();
		}		
	}
		
	/**
	 * 
	 */
	public void advanceToPreviousStage() {		
		if(examPlayerController != null) {
			examPlayerController.advanceToPreviousStage();
		}
	}
	
	/** Creates the JFrame version of the application */
	public static void main(String[] args) {		
		//STANDARD LAUNCH CODE
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {	
				IcarusLookAndFeel.initializeICArUSLookAndFeel();
				IcarusTEPlayerMain player = new IcarusTEPlayerMain(ApplicationType.Application, true, Classification.FOUO);
				try {										
					ExamPlaybackFileSelectionDlg_Phase1.setCurrentExamResponseFolder(
							new File("data/Phase_1_CPD/player_data/model_data"));
					player.examFileURL = new File("data/Phase_1_CPD/exams/Final-Exam-1/Final-Exam-1.xml").toURI().toURL();
					player.avgHumanDataSetFileURL = new File("data/Phase_1_CPD/player_data/human_data/Avg-Human_Final-Exam-1.xml").toURI().toURL();
				} catch(Exception ex) {}
			}
		});
		
		//DEBUG LUANCH CODE
		/*final IcarusTEPlayerMain player = new IcarusTEPlayerMain(ApplicationType.Application, true, Classification.FOUO);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {	
				try {
					player.openExamResponse(
							new File("data/player_data/exams/Final_Exam/FinalExam.xml").toURI().toURL(), 
							new File("data/player_data/model_data/BBN_Insight23_Final-Exam-1.xml").toURI().toURL(), 
							new File("data/player_data/human_data/Avg-Human_Final-Exam-1.xml").toURI().toURL());
					player.playExamResponse(player.examResponse);
				} catch(Exception ex) {
					ex.printStackTrace();
				}
			}});*/
		//END DEBUG LAUNCH CODE
	}

	public ApplicationType getApplicationType() {
		return applicationType;
	}

	public boolean isInitialized() {
		return initialized;
	}

	public PlayerController getExamPlayerController() {
		return examPlayerController;
	}
}