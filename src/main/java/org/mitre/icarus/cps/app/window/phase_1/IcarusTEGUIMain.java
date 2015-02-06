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

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.mitre.icarus.cps.app.experiment.IcarusSubjectData;
import org.mitre.icarus.cps.app.experiment.data_recorder.LocalSubjectDataRecorder;
import org.mitre.icarus.cps.app.experiment.data_recorder.RemoteSubjectDataRecorder;
import org.mitre.icarus.cps.app.experiment.phase_1.IcarusExamController_Phase1;
import org.mitre.icarus.cps.app.util.CPSUtils;
import org.mitre.icarus.cps.app.widgets.WidgetConstants;
import org.mitre.icarus.cps.app.widgets.client.HomeDialog;
import org.mitre.icarus.cps.app.widgets.client.LoginDialog;
import org.mitre.icarus.cps.app.widgets.client.RegistrationDialog;
import org.mitre.icarus.cps.app.widgets.dialog.AboutDlg;
import org.mitre.icarus.cps.app.widgets.dialog.ParticipantIdDlg;
import org.mitre.icarus.cps.app.widgets.dialog.ParticipantIdPanel;
import org.mitre.icarus.cps.app.widgets.dialog.SkipToPhaseDlg;
import org.mitre.icarus.cps.app.widgets.dialog.ValidationErrorsDlg;
import org.mitre.icarus.cps.app.widgets.events.ButtonPressEvent;
import org.mitre.icarus.cps.app.widgets.events.ButtonPressListener;
import org.mitre.icarus.cps.app.widgets.map.phase_1.MapPanelContainer;
import org.mitre.icarus.cps.app.widgets.map.phase_1.PlacemarkFactory.AttackLocationType;
import org.mitre.icarus.cps.app.widgets.phase_1.ImageManager_Phase1;
import org.mitre.icarus.cps.app.widgets.phase_1.dialog.GridSizeDlg;
import org.mitre.icarus.cps.app.widgets.phase_1.experiment.ConditionPanel_Phase1;
import org.mitre.icarus.cps.app.widgets.phase_1.experiment.ExperimentPanel_Phase1;
import org.mitre.icarus.cps.app.window.IcarusLookAndFeel;
import org.mitre.icarus.cps.app.window.phase_1.ApplicationOptions.ExamOption;
import org.mitre.icarus.cps.app.window.phase_1.ApplicationOptions.FeatureVectorOption;
import org.mitre.icarus.cps.app.window.phase_1.ApplicationOptions.FileOption;
import org.mitre.icarus.cps.app.window.phase_1.ApplicationOptions.Menu;
import org.mitre.icarus.cps.app.window.phase_1.Phase_1_Controller.GroupAttackComparator;
import org.mitre.icarus.cps.assessment.score_computer.phase_1.ScoreComputer;
import org.mitre.icarus.cps.exam.phase_1.IcarusExam_Phase1;
import org.mitre.icarus.cps.exam.phase_1.loader.IcarusExamLoader_Phase1;
import org.mitre.icarus.cps.exam.phase_1.testing.TaskTestPhase;
import org.mitre.icarus.cps.exam.phase_1.training.TutorialPhase;
import org.mitre.icarus.cps.experiment_core.condition.Condition;
import org.mitre.icarus.cps.experiment_core.event.SubjectActionEvent;
import org.mitre.icarus.cps.experiment_core.event.SubjectActionListener;
import org.mitre.icarus.cps.experiment_core.gui.ConditionPanel.BannerOrientation;
import org.mitre.icarus.cps.feature_vector.phase_1.FeatureType;
import org.mitre.icarus.cps.feature_vector.phase_1.FeatureVectorManager;
import org.mitre.icarus.cps.feature_vector.phase_1.GridSize;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupAttack;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupCenter;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupType;
import org.mitre.icarus.cps.feature_vector.phase_1.Road;
import org.mitre.icarus.cps.feature_vector.phase_1.SocintOverlay;
import org.mitre.icarus.cps.feature_vector.phase_1.TaskData;
import org.mitre.icarus.cps.web.experiments.client.DataSourceFactory;
import org.mitre.icarus.cps.web.experiments.client.IDataSource;
import org.mitre.icarus.cps.web.experiments.client.exceptions.ClientException;
import org.mitre.icarus.cps.web.model.Role;
import org.mitre.icarus.cps.web.model.Site;
import org.mitre.icarus.cps.web.model.User;

//import org.mitre.icarus.cps.experiment.phase_1.data_recorder.DoNothingSubjectDataRecorder;

/**
 * Main entry point for the Phase 1 Test & Evaluation GUI. 
 * 
 * This class is deprecated, the new main class for all Phases is 
 * org.mitre.icarus.cps.app.window.controller.ApplicationController.
 * 
 * @author CBONACETO
 *
 */
@Deprecated
public class IcarusTEGUIMain extends AbstractIcarusTEGUI<IcarusExamController_Phase1, IcarusExam_Phase1> 
	implements ButtonPressListener, HyperlinkListener, SubjectActionListener {
	
	protected static enum ApplicationType {Full, DeveloperTools, Experimenter, ExperimentClient, WebStartExperimentClient};
	
	/** The application version information */
	public static final String VERSION = "1.16";
	
	/** The application type */
	//private static final ApplicationType APPLICATION_TYPE = ApplicationType.Full;
	//private static final ApplicationType APPLICATION_TYPE = ApplicationType.ExperimentClient;
	private static final ApplicationType APPLICATION_TYPE = ApplicationType.WebStartExperimentClient;
	//private static final ApplicationType APPLICATION_TYPE = ApplicationType.DeveloperTools;
	
	/** The application name */		
	//public static final String APPLICATION_NAME = "ICArUS Test & Evaluation Suite";
	//public static final String APPLICATION_NAME = "ICArUS Test & Evaluation Suite - PSU Version";
	public static final String APPLICATION_NAME = "ICArUS Experiment Client";
	//public static final String APPLICATION_NAME = "ICArUS Developer Tools";
	
	/** The file menu */
	protected JMenu fileMenu;
	
	/** The feature vector menu */
	protected JMenu featureVectorMenu;
	
	/** The exam menu */
	protected JMenu examMenu;

	/** File chooser for opening exam files */
	protected static JFileChooser examFileChooser;
	
	/** File chooser for opening feature vector files */
	protected static JFileChooser featureVectorFileChooser;

	/** The current default exam file (if any) */
	//protected String defaultExamFile = "data/Phase_1_CPD/Pilot_Experiment/PilotExam.xml";
	protected String defaultExamFile = "data/Phase_1_CPD/Final_Experiment/FinalExam.xml";
	//protected String defaultExamFile = "data/Phase_1_CPD/Final_Experiment/FinalExam_RL.xml";
	//protected boolean openDefaultExamFromJarFile = true;

	/** A map panel for viewing feature vector data */
	protected MapPanelContainer mapPanel;
	
	/** The grid size to use when viewing feature vector data */
	protected GridSize gridSize;		
	
	protected String taskFileName = "";
	
	protected String roadFileName = "";	
	
	protected String regionFileName = "";	
	
	/** Contains the application options that are enabled. */
	private ApplicationOptions applicationOptions;
	
	/** The login screen */
	protected LoginDialog loginDlg;
	
	/** Dialog to create a new subject/user account */
	protected RegistrationDialog registrationDlg;
	
	/** The home screen */
	protected HomeDialog homeDlg;		
	
	/** The current site */	
	protected Site site = new Site("TEST", "TEST");
	
	/** The current subject ID */
	protected String subjectId = "TEST";	
	
	/** The current subject user name */
	protected String subjectName = "TEST";
	
	/** Roles for the current subject when running as a client */
	protected List<Role> subjectRoles;
	
	protected List<String> completedTasks;
	
	/** Remote server connection when running as a client */
	protected IDataSource dataSourceConnection;
	
	/**
	 * Constructs a new ICArUS Test & Evaluation application with the appropriate options enabled.
	 */
	public IcarusTEGUIMain() {
		this(createApplicationOptions());
	}
	
	/**
	 * Constructor that takes the GUI options that are enabled.
	 */
	private IcarusTEGUIMain(ApplicationOptions options) {
		this.applicationOptions = options;
		gridSize = new GridSize();
		if(options.isClientMode()) {
			dataSourceConnection = DataSourceFactory.createDefaultDataSource();
		}
		//Create GUI in the event dispatch thread
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createFrame(APPLICATION_NAME);
				
				if(applicationOptions.isFileOptionEnabled(FileOption.Open_Pilot_Exam)) {
					//Prompt for a participant ID and site and start the current pilot study				
					final ParticipantIdPanel dlg = new ParticipantIdPanel(applicationOptions.getSites(),"Begin", false);
					frame.getContentPane().add(dlg);								
					frame.pack();
					frame.setResizable(false);
					CPSUtils.centerFrameOnScreen(frame);
					frame.setVisible(true);	

					dlg.addOkButtonActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							subjectId = dlg.getSubjectId();
							site = dlg.getSite();
							initializeFrame(applicationOptions);
						}
					});	
				}
				else {
					initializeFrame(applicationOptions);
				}
			}
		});
	}
	
	private static ApplicationOptions createApplicationOptions() {
		switch(APPLICATION_TYPE) {
		case Full:
			return ApplicationOptions.createInstanceAllOptions();
		case DeveloperTools: 
			return ApplicationOptions.createInstanceDeveloperToolsOptions();
		case Experimenter:
			return ApplicationOptions.createInstanceExperimenterOptions();
		case ExperimentClient: case WebStartExperimentClient :
			return ApplicationOptions.createInstanceExperimentClientOptions();
		}
		return null;
	}
	
	private void initializeFrame(ApplicationOptions options) {
		super.initializeFrame(options.isShowSecurityBanner(), options.isFouo());

		//Create menu bar with file menu, feature vector menu, exam menu, and help menu
		frame.setJMenuBar(createMenuBar(options));

		//Add listener to show warning dialog before closing the app
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {				
				confirmExit();
			}
		});
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		//Size the frame to fit the screen
		createExamController();
		contentPanel.add(examController.getExperimentPanel().getExperimentPanelComponent());
		frame.pack();
		//Adjust size of experiment panel by shrinking the map if frame size exceeds screen dimensions
		Dimension screenSize = frame.getToolkit().getScreenSize();
		//Dimension screenSize = new Dimension(1024, 768);
		int widthOver = frame.getWidth() - screenSize.width;
		int heightOver = frame.getHeight() - screenSize.height;
		if(widthOver > 0 || heightOver > 0) {
			Dimension experimentPanelSize = examController.getExperimentPanel().getExperimentPanelComponent().getPreferredSize();
			int newWidth = (widthOver > 0) ? experimentPanelSize.width - widthOver : experimentPanelSize.width;
			if(newWidth < 1) {
				newWidth = 1;
			}
			int newHeight = (heightOver > 0) ? experimentPanelSize.height - heightOver : experimentPanelSize.height;
			if(newHeight < 1) {
				newHeight = 1;
			}
			examController.getExperimentPanel().getExperimentPanelComponent().setMaximumSize(new Dimension(newWidth, newHeight));
			frame.pack();
		}		
		frame.setMinimumSize(frame.getSize());		
		
		boolean pilotOpened = false;
		if(options.isFileOptionEnabled(FileOption.Open_Pilot_Exam)) {
			//Try to open and show the default study
			pilotOpened = openDefaultExam();
		}
		
		if(!pilotOpened) {
			contentPanel.removeAll();
		} else {
			showExam(defaultExam, subjectId, site);	
		}
		
		CPSUtils.centerFrameOnScreen(frame);		
		frame.setVisible(true);		
		
		if(options.isClientMode()) {
			//Show the login dialog	
			loginDlg = new LoginDialog(frame, true);
			loginDlg.setIconImage(ImageManager_Phase1.getImage(ImageManager_Phase1.ICARUS_LOGO));
			loginDlg.addButtonPressListener(this);
			registrationDlg = new RegistrationDialog(frame, true);
			registrationDlg.setIconImage(ImageManager_Phase1.getImage(ImageManager_Phase1.ICARUS_LOGO));
			registrationDlg.addButtonPressListener(this);
			homeDlg = new HomeDialog(frame, true);
			homeDlg.setIconImage(ImageManager_Phase1.getImage(ImageManager_Phase1.HOME_ICON));			
			homeDlg.setFont(WidgetConstants.FONT_DEFAULT.deriveFont(Font.PLAIN, 14));
			homeDlg.addButtonPressListener(this);
			homeDlg.addHyperlinkListener(this);
			loginDlg.setLocationRelativeTo(frame);
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					loginDlg.setVisible(true);
				}
			});
		}
		//System.out.println("Window Size: " + frame.getSize());
	}		
	
	/**
	 * Log into the client application. 
	 */
	private void login() {		
		try {			
			boolean credentialsEntered = true;
			if(loginDlg.getUserName() == null || loginDlg.getUserName().isEmpty()) {				
				handleException(new Exception("Please enter a user name."), false);
				credentialsEntered = false;
			} else if(loginDlg.getPassword() == null || loginDlg.getPassword().length == 0) {
				handleException(new Exception("Please enter a password."), false);
				credentialsEntered = false;
			}
			if(credentialsEntered) {
				subjectName = loginDlg.getUserName();
				subjectRoles = dataSourceConnection.validateLogin(
						subjectName, new String(loginDlg.getPassword()));			
				if(subjectRoles == null || subjectRoles.isEmpty()) {
					//The login was not valid
					handleException(new Exception("Your login was not valid. Please re-type your user name and password"), false);										
				} else {					
					//System.out.println(subjectRoles + ", " + subjectName);
					//The login was valid, go to the  screen
					loginDlg.setVisible(false);
					registrationDlg.setVisible(false);				
					//subjectId = Long.toString(dataSourceConnection.getSubject(subjectName).getId());
					User subject = dataSourceConnection.getUser(subjectName);
					if(subject != null) {
						site = subject.getSite();
						if(subject.getId() != null) {
							subjectId = subject.getId().toString();
						} 
					}
					//System.out.println("site ID: " + siteId);
					//TODO: Display site in  dialog
					homeDlg.setUser(subjectName, subjectId, false);
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							returnHome();
						}});
				}	
			}
		} catch(ClientException exception) {
			//An exception occured			
			handleException(exception, false);		
			loginDlg.setVisible(true);
		}
	}
	
	/**
	 * Log out of the client application. 
	 */
	private void logout() {
		boolean logout = true;
		if(applicationOptions.isWarnIfDataLost() && isUnsavedData()) {
			//Warn if unsaved data
			logout = JOptionPane.showConfirmDialog(frame, "<html>The current mission is not complete, and you will have to restart it at the " +
					"beginning if you log out.<br>Would you like to log out?</html>", "", 
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
		}	
		if(logout) {
			//Logout
			if(examController != null) {
				examController.stopExperiment();
			}
			contentPanel.removeAll();
			contentPanel.revalidate();
			contentPanel.repaint();
			subjectId = null;
			subjectName = null;
			homeDlg.setVisible(false);
			registrationDlg.setVisible(false);
			loginDlg.resetFields();
			loginDlg.setLocationRelativeTo(frame);
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					loginDlg.setVisible(true);
				}
			});
		}
	}
	
	/**
	 * Create a new user account and login as that user.
	 */
	private void createUser() {
		try {			
			boolean credentialsEntered = true;
			if(registrationDlg.getSite() == null || registrationDlg.getSite().length() == 0) {
				handleException(new Exception("Please enter a valid site."), false);
				credentialsEntered = false;
			} else if(registrationDlg.getAuthenticationCode() == null || registrationDlg.getAuthenticationCode().length() == 0) {
				handleException(new Exception("Please enter an authentication code."), false);
				credentialsEntered = false;
			} else if(registrationDlg.getUserName() == null || registrationDlg.getUserName().isEmpty()) {
				handleException(new Exception("Please enter a user name."), false);
				credentialsEntered = false;
			} else if(registrationDlg.getPassword() == null || registrationDlg.getPassword().length == 0) {
				handleException(new Exception("Please enter a password."), false);
				credentialsEntered = false;
			} else if(!(new String(registrationDlg.getPassword()).equals(new String(registrationDlg.getRetypePassword())))) {
				handleException(new Exception("The passwords you entered do not match. Please re-type the passwords."), false);
				registrationDlg.resetPasswordFields();
				credentialsEntered = false;
			}
			if(credentialsEntered) {
				subjectName = registrationDlg.getUserName();
				subjectRoles = dataSourceConnection.createNewUser(subjectName, new String(registrationDlg.getPassword()), registrationDlg.getSite(),
						new String(registrationDlg.getAuthenticationCode()));		
				if(subjectRoles == null || subjectRoles.isEmpty()) {
					//The user was not created
					handleException(new Exception("Your user account could not be created, please try again."), false);
				}
				else {
					//The user was created, go to the  screen
					loginDlg.setVisible(false);
					registrationDlg.setVisible(false);
					User subject = dataSourceConnection.getUser(subjectName);
					//subjectId = Long.toString(dataSourceConnection.getSubject(subjectName).getId());
					if(subject != null) {
						site = subject.getSite();
						if(subject.getId() != null) {
							subjectId = subject.getId().toString();
						} 
					}
					homeDlg.setUser(subjectName, subjectId, false);
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							returnHome();
						}});
				}	
			}
		} catch(ClientException exception) {
			//An exception occured
			handleException(exception, false);
		}
	}
	
	/**
	 * Return to the  screen.
	 */
	private void returnHome() {
		boolean returnHome = true;
		if(applicationOptions.isWarnIfDataLost() && isUnsavedData()) {
			//Warn if unsaved data
			returnHome = JOptionPane.showConfirmDialog(frame, "<html>The current mission is not complete, and you will have to restart it at the " +
					"beginning if you return .<br>Would you like to return ?</html>", "", 
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
		}		

		if(returnHome) {
			if(examController != null) {
				examController.stopExperiment();
			}
			contentPanel.removeAll();
			contentPanel.revalidate();
			contentPanel.repaint();

			//Update  screen options
			homeDlg.setOptionsText(createHomeOptions());

			//Show  screen
			homeDlg.setLocationRelativeTo(frame);
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					homeDlg.setVisible(true);;
				}
			});
		}
	}

	/**
	 * Create the list of options to choose from on the  screen.
	 * 
	 * @return
	 */
	private String createHomeOptions() {
		StringBuilder options = new StringBuilder();
		openDefaultExam();
		completedTasks = null;
		try {
			//System.out.println(subjectId + ", " + exam.getName());
			//System.out.println(subjectId + "," + exam.getName());
			completedTasks = dataSourceConnection.getCompletedExamTasks(subjectId, exam.getName());
		} catch(ClientException ex) {
			ex.printStackTrace();			
			handleException(new Exception("Error, could not retrieve your experiment history", ex), false);
		}		
		//System.out.println("Completed Task: " + completedTasks);
		if(completedTasks != null && !completedTasks.isEmpty()) {			
			if(!isCurrentExamComplete(completedTasks)) {				
				options.append("<a href=\"start_where_leftoff\">Start Current Experiment Where I Left Off</a><br>");
			}
			options.append("<a href=\"start_at_beginning\">Restart Current Experiment At Beginning</a><br>");
		} else {
			options.append("<a href=\"start_at_beginning\">Start Current Experiment</a><br>");
		}
		return options.toString();
	}
	
	/**
	 * @return
	 */
	private boolean isUnsavedData() {
		if(examController != null) {
			return examController.isConditionRunning();
		}
		return false;
	}	
	
	/**
	 * @param completedTasks
	 * @return
	 */
	private Integer getCurrenTaskInCurrentExam(List<String> completedTasks) {
		if(exam != null && completedTasks != null && !completedTasks.isEmpty()) {			
			int taskIndex = -1;
			//System.out.println("Completed tasks: " + completedTasks);
			if(exam.getConditions() != null && !exam.getConditions().isEmpty() && !completedTasks.isEmpty()) {				
				for(String task : completedTasks) {
					int index = 0;
					int testIndex = -1;					
					for(Condition condition : exam.getConditions()) {
						//System.out.println("Comparing " + condition.getName() + " to " + task);
						if(condition.getName().equalsIgnoreCase(task)) {
							testIndex = index;
							break;
						}
						index++;
					}
					if(testIndex > taskIndex) {
						taskIndex = testIndex;
					}
				}
			}
			if(taskIndex > -1) {
				return taskIndex+1;
			} else {
				return 0;
			}
		}
		return null;
	}
	
	/**
	 * @param completedTasks
	 * @return
	 */
	private boolean isCurrentExamComplete(List<String> completedTasks) {
		if(exam != null && completedTasks != null) {
			if(exam.getConditions() != null) {
				for(Condition condition : exam.getConditions()) {
					if(!completedTasks.contains(condition.getName())) {
						return false;
					}
				}
			}
			return true;			
		}
		return false;
	}

	@Override
	public void buttonPressed(ButtonPressEvent event) {
		switch(event.getButtonId()) {
		case ButtonPressEvent.BUTTON_OK:
			//Try to create a new user
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					createUser();
				}});
			break;
		case ButtonPressEvent.BUTTON_CANCEL:
			//Cancel create new user
			registrationDlg.setVisible(false);
			break;
		case ButtonPressEvent.BUTTON_CREATE_USER:
			registrationDlg.setLocationRelativeTo(frame);
			registrationDlg.resetFields();
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					registrationDlg.setVisible(true);
				}
			});
			break;
		case ButtonPressEvent.BUTTON_EXIT:
			//Exit
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					confirmExit();
				}});
			break;
		case ButtonPressEvent.BUTTON_LOGIN:
			//Login
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					login();
				}});
			break;
		case ButtonPressEvent.BUTTON_LOGOUT:
			//Logout
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					logout();
				}});
			break;
		}		
	}

	@Override
	public void hyperlinkUpdate(HyperlinkEvent e) {
		if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			//Process a selection on the  screen
			if(e.getDescription().equalsIgnoreCase("start_at_beginning")) {
				//Start the current experiment at the beginning
				boolean startExam = true;
				if(completedTasks != null && !completedTasks.isEmpty()) {
					//Warn that previous data will be lost if the exam is restarted
					startExam = JOptionPane.showConfirmDialog(frame, "<html>You must redo all missions if you restart the experiment." +
							"<br>Would you still like to restart the experiment?</html>", "",
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
					if(startExam) {
						//TODO: Delete data from previously completed tasks
					}
				}
				if(startExam) {
					homeDlg.setVisible(false);
					boolean pilotOpened = openDefaultExam();
					if(pilotOpened) {
						//System.out.println("subjectId: " + subjectId);
						showExam(defaultExam, subjectId, site);					
					} else {
						homeDlg.setLocationRelativeTo(frame);
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								homeDlg.setVisible(true);
							}
						});
					}
				}
			}
			else if(e.getDescription().equalsIgnoreCase("start_where_leftoff")) {
				//Start the current experiment where the subject left off
				homeDlg.setVisible(false);
				boolean pilotOpened = false;
				//System.out.println("Completed Tasks: " + completedTasks.size());
				Integer currentTaskNumber = getCurrenTaskInCurrentExam(completedTasks);
				pilotOpened = openDefaultExam();
				if(pilotOpened) {
					//System.out.println("subjectId: " + subjectId);
					showExam(defaultExam, subjectId, site);
					if(currentTaskNumber != null && 
							currentTaskNumber > 0 && currentTaskNumber < defaultExam.getConditions().size()) {
						examController.skipToCondition(currentTaskNumber);
					}
				} else {
					homeDlg.setLocationRelativeTo(frame);
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							homeDlg.setVisible(true);
						}
					});
				}
			}
		}
	}	

	@Override
	public void subjectActionPerformed(SubjectActionEvent event) {
		if(event.eventType == SubjectActionEvent.EXIT_BUTTON_PRESSED) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					returnHome();
				}});;
		}
	}	

	/** Create a menu bar with the file, feature vector, current exam, and help menu options */	
	private JMenuBar createMenuBar(ApplicationOptions options) {
		JMenuBar menuBar = new JMenuBar();
		
		// Create the file menu.  The file menu will always at least contain an exit item.
		final JMenu fileMenu = new JMenu(options.getMenuName(Menu.File));
		fileMenu.setMnemonic(KeyEvent.VK_F);
		menuBar.add(fileMenu);
		
		//Create the feature vector menu if any feature vector options are enabled
		featureVectorMenu = new JMenu(options.getMenuName(Menu.FeatureVectorOptions));
		featureVectorMenu.setEnabled(false);
		if(options.getFeatureVectorOptions() != null && !options.getFeatureVectorOptions().isEmpty()) {
			featureVectorMenu.setMnemonic(KeyEvent.VK_V);
			menuBar.add(featureVectorMenu);
		}
		
		//Create the current exam menu if any exam options are enabled
		examMenu = new JMenu(options.getMenuName(Menu.ExamOptions));
		examMenu.setEnabled(false);
		if(options.getExamOptions() != null && !options.getExamOptions().isEmpty()) {
			examMenu.setMnemonic(KeyEvent.VK_E);
			menuBar.add(examMenu);
		}	
		
		//Create the help menu with the about item
		final JMenu helpMenu = new JMenu(options.getMenuName(Menu.Help));
		menuBar.add(helpMenu);
		helpMenu.setMnemonic(KeyEvent.VK_H);
		JMenuItem aboutMenuItem = new JMenuItem("About " + APPLICATION_NAME);
		aboutMenuItem.setMnemonic(KeyEvent.VK_A);
		//aboutMenuItem.setIcon(ImageManager.getImageIcon(ImageManager.ICARUS_LOGO));
		helpMenu.add(aboutMenuItem);		
		aboutMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Show the About dialog
				AboutDlg.showDefaultAboutDlg(frame, applicationOptions.isFouo(), 
						false, APPLICATION_NAME, VERSION);
			}			
		});
		
		//////////////////////// Create file menu items ///////////////////////////////////
		boolean separatorNeeded = false;		
		
		//Create open current exam option
		if(options.isFileOptionEnabled(FileOption.Open_Pilot_Exam)) {
			separatorNeeded = true;
			JMenuItem beginPilotItem = new JMenuItem("Open Current Experiment Exam File");
			beginPilotItem.setMnemonic(KeyEvent.VK_C);
			fileMenu.add(beginPilotItem);
			beginPilotItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if(openDefaultExam()) {
						showExam(defaultExam, subjectId, site);
					}
				}	
			});			
		}
		
		//Create open other exam option
		if(options.isFileOptionEnabled(FileOption.Open_Other_Exam)) {
			separatorNeeded = true;			
			JMenuItem openExamItem = new JMenuItem("Open Exam File");
			if(options.isFileOptionEnabled(FileOption.Open_Pilot_Exam)) {
				openExamItem.setText("Open Other Exam File");				
			} 
			openExamItem.setMnemonic(KeyEvent.VK_O);
			fileMenu.add(openExamItem);
			openExamItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					File examFile = chooseExamFile();
					if(examFile != null) {
						try{
							examFileURL = examFile.toURI().toURL();
							if(openExam(examFileURL)) {
								showExam(exam, subjectId, site);
							}
						} catch (MalformedURLException e) {
							handleException(new Exception("Error loading exam file", e), true);
						}				
					}
				}
			});  
		}

		//Create validate exam file menu item
		if(options.isFileOptionEnabled(FileOption.Validate_Exam)) {
			separatorNeeded = true;
			JMenuItem validateExamItem = new JMenuItem("Validate Exam or Exam Response File");
			validateExamItem.setMnemonic(KeyEvent.VK_V);
			fileMenu.add(validateExamItem);
			validateExamItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					File examFile = chooseExamFile();
					if(examFile != null) {
						validateExam(examFile);
					}
				}
			});
		}
		
		if(separatorNeeded) {
			fileMenu.addSeparator();
			separatorNeeded = false;
		}
		
		//Create open task feature vector file option
		final JMenuItem openTaskItem = new JMenuItem("Open Task (1-7) Feature Vector File");
		if(options.isFileOptionEnabled(FileOption.Open_Task)) {
			separatorNeeded = true;
			openTaskItem.setMnemonic(KeyEvent.VK_T);
			fileMenu.add(openTaskItem);
		}				
		
		//Create open roads feature vector file option
		final JMenuItem openRoadsItem = new JMenuItem("Open Roads Feature Vector File");
		if(options.isFileOptionEnabled(FileOption.Open_Roads)) {
			separatorNeeded = true;
			openRoadsItem.setMnemonic(KeyEvent.VK_R);
			fileMenu.add(openRoadsItem);		
		}
		
		//Create open regions feature vector file option
		final JMenuItem openRegionsItem = new JMenuItem("Open Regions Feature Vector File");
		if(options.isFileOptionEnabled(FileOption.Open_Regions)) {
			separatorNeeded = true;
			openRegionsItem.setMnemonic(KeyEvent.VK_G);
			fileMenu.add(openRegionsItem);		
		}
		
		ActionListener featureVectorListener = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				FeatureType type = FeatureType.Task;
				if(event.getSource() == openRoadsItem) {
					type = FeatureType.Road;
				}
				else if(event.getSource() == openRegionsItem) {
					type = FeatureType.Region;
				}
				File featureVectorFile = chooseFeatureVectorFile(type);

				if(featureVectorFile != null) {
					//Load the feature vector file
					try {
						URL featureVectorFileURL = featureVectorFile.toURI().toURL();						
						if(openFeatureVector(featureVectorFileURL, type)) {
							examMenu.setEnabled(false);
							featureVectorMenu.setEnabled(true);							
						}
					} catch (MalformedURLException e) {
						handleException(new Exception("Error loading feature vector file", e), true);
					}
				}
			}};		
		openTaskItem.addActionListener(featureVectorListener);
		openRoadsItem.addActionListener(featureVectorListener);
		openRegionsItem.addActionListener(featureVectorListener);
		
		if(separatorNeeded) {
			fileMenu.addSeparator();
			separatorNeeded = false;
		}
		
		//Create return  menu item and add to file menu
		if(options.isFileOptionEnabled(FileOption.Return_Home)) {
			separatorNeeded = true;
			JMenuItem homeItem = new JMenuItem("Return ");
			homeItem.setMnemonic(KeyEvent.VK_H);
			homeItem.setIcon(ImageManager_Phase1.getImageIcon(ImageManager_Phase1.HOME_ICON));
			homeItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {			
					returnHome();
				}
			});
			fileMenu.add(homeItem);
		}
		
		//Create logout menu item and add to file menu
		if(options.isFileOptionEnabled(FileOption.Logout)) {
			separatorNeeded = true;
			JMenuItem logoutItem = new JMenuItem("Log Out");
			logoutItem.setMnemonic(KeyEvent.VK_L);
			logoutItem.setIcon(ImageManager_Phase1.getImageIcon(ImageManager_Phase1.LOGOUT_ICON));
			logoutItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					logout();
				}
			});
			fileMenu.add(logoutItem);
		}

		//Create exit menu item and add to file menu
		if(options.isFileOptionEnabled(FileOption.Exit)) {
			if(separatorNeeded) {
				fileMenu.addSeparator();
			}
			JMenuItem exitItem = new JMenuItem("Exit");
			exitItem.setMnemonic(KeyEvent.VK_X);
			exitItem.setIcon(ImageManager_Phase1.getImageIcon(ImageManager_Phase1.EXIT_ICON));
			final boolean confirmExit = options.isFileOptionEnabled(FileOption.Logout); 
			exitItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(confirmExit) {
						confirmExit();
					} else {
						System.exit(0);
					}
				}
			});
			fileMenu.add(exitItem);
		}
		//////////////////////// End create file menu items //////////////////////////////////
		
		
		////////////////////////Create feature vector menu items /////////////////////////////////
		//Create set grid size menu item
		if(options.isFeatureVectorOptionEnabled(FeatureVectorOption.Set_Grid_Size)) {
			JMenuItem gridSizeItem = new JMenuItem("Set Grid Size");
			gridSizeItem.setMnemonic(KeyEvent.VK_G);
			featureVectorMenu.add(gridSizeItem);
			gridSizeItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					//Show grid size dialog
					GridSize newGridSize = GridSizeDlg.showDialog(frame, 
							"Set Grid Size", gridSize); 
					if(newGridSize != null) {
						gridSize = newGridSize;
						if(mapPanel != null) {
							mapPanel.setGridSize(gridSize);
						}
					}
				}
			});
			featureVectorMenu.addSeparator();
		}		
		
		//Create clear task data menu item
		if(options.isFeatureVectorOptionEnabled(FeatureVectorOption.Clear_Task)) {
			JMenuItem clearTaskItem = new JMenuItem("Clear Task Data");
			clearTaskItem.setMnemonic(KeyEvent.VK_T);
			featureVectorMenu.add(clearTaskItem);
			clearTaskItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					clearTaskData();
				}
			});
		}
		
		//Create clear road data menu item
		if(options.isFeatureVectorOptionEnabled(FeatureVectorOption.Clear_Roads)) {
			JMenuItem clearRoadsItem = new JMenuItem("Clear Road Data");
			clearRoadsItem.setMnemonic(KeyEvent.VK_R);
			featureVectorMenu.add(clearRoadsItem);
			clearRoadsItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					clearRoadData();
				}
			});
		}
		
		//Create clear region data menu item
		if(options.isFeatureVectorOptionEnabled(FeatureVectorOption.Clear_Regions)) {
			JMenuItem clearRegionsItem = new JMenuItem("Clear Region Data");
			clearRegionsItem.setMnemonic(KeyEvent.VK_G);
			featureVectorMenu.add(clearRegionsItem);
			clearRegionsItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					clearRegionData();
				}
			});
		}
		
		//Create clear all map data menu item
		if(options.isFeatureVectorOptionEnabled(FeatureVectorOption.Clear_All)) {
			JMenuItem clearAllItem = new JMenuItem("Clear All Map Data");
			clearAllItem.setMnemonic(KeyEvent.VK_A);
			featureVectorMenu.add(clearAllItem);
			clearAllItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					taskFileName = "";
					roadFileName = "";
					regionFileName = "";
					frame.setTitle(APPLICATION_NAME);
					mapPanel.resetMap();
				}
			});
		}		
		////////////////////////End create feature vector menu items /////////////////////////////////		
		
		
		////////////////////////Create exam menu items /////////////////////////////////
		//Create skip to task menu item
		if(options.isExamOptionEnabled(ExamOption.Skip_To_Task)) {
			final JMenuItem skipToTaskItem = new JMenuItem("Skip to Mission");
			examMenu.add(skipToTaskItem);
			skipToTaskItem.setMnemonic(KeyEvent.VK_S);
			skipToTaskItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//Show a skip to phase dialog
					if(exam != null && examController != null) {
						SkipToPhaseDlg dlg = new SkipToPhaseDlg(frame, exam.getTasks(), 
								examController.getCurrentConditionIndex(), "Mission");
						dlg.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
						dlg.setModal(true);
						dlg.setLocationRelativeTo(frame);
						dlg.setVisible(true);
						if(dlg.getButtonPressed() == SkipToPhaseDlg.ButtonType.OK && 
								dlg.getPhaseIndex() >= 0) {
							//Skip to the task the user selected
							examController.skipToCondition(dlg.getPhaseIndex());
						}
					}
				}
			}); 
		}
		
		//Create change participant menu item
		if(options.isExamOptionEnabled(ExamOption.Change_Particpant)) {
			JMenuItem changeParticipantItem = new JMenuItem("Change Participant ID");
			examMenu.add(changeParticipantItem);    	
			changeParticipantItem.setMnemonic(KeyEvent.VK_P);
			changeParticipantItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {    	
					//Show participant ID dialog
					IcarusSubjectData subjectData = ParticipantIdDlg.showDialog(frame, "Enter Participant ID",
							applicationOptions.getSites(), site, "OK", true);

					if(subjectData != null) {
						subjectId = subjectData.getSubjectId();
						site = subjectData.getSite();
						if(examController != null) {
							examController.setSubjectData(subjectId, site);
						}    				
					}
				}
			});
		}
		
		//Create restart exam menu item
		if(options.isExamOptionEnabled(ExamOption.Restart_Exam)) {
			JMenuItem restartExamItem = new JMenuItem("Restart Exam");
			examMenu.add(restartExamItem);    	
			restartExamItem.setMnemonic(KeyEvent.VK_R);
			restartExamItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(JOptionPane.showConfirmDialog(frame, "Would you like to restart the current exam?", "", 
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
						if(examController != null) {
							examController.restartExperiment();
						}
					}					
				}
			});
		}
		////////////////////////End create exam menu items /////////////////////////////////		

		return menuBar;
	}	
	
	/** Shows a file chooser to open an exam. Returns null if no file was chosen. */
	protected File chooseExamFile() {
		if(examFileChooser == null) {				            		
			examFileChooser = new JFileChooser(new File("."));
			examFileChooser.setDialogTitle("Open Exam");	
			examFileChooser.setFileFilter(new FileNameExtensionFilter("Exam Files (*.xml)", "xml"));
			if(examFileURL != null) {
				examFileChooser.setCurrentDirectory(new File(examFileURL.getFile()));
			}
		}
		// Show open dialog
		if(examFileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
			return examFileChooser.getSelectedFile();
		}
		return null;
	}
	
	@Override
	protected URL getDefaultExamURL() throws MalformedURLException {
		URL url = null;
		if(APPLICATION_TYPE == ApplicationType.WebStartExperimentClient) {
			url = getClass().getClassLoader().getResource(defaultExamFile);
		} else {
			url = new File(defaultExamFile).toURI().toURL();
		}
		return url;
	}	

	@Override
	protected IcarusExam_Phase1 loadExam(URL examUrl) throws Exception {
		return IcarusExamLoader_Phase1.unmarshalExam(examUrl);
	}

	/** Shows an exam in the GUI */
	@Override
	protected boolean showExam(IcarusExam_Phase1 exam, String subjectId, Site site) {
		if(super.showExam(exam, subjectId, site)) {
			examMenu.setEnabled(true);
			featureVectorMenu.setEnabled(false);
			return true;
		}
		return false;
	}	
	
	@Override
	protected IcarusExamController_Phase1 createExamController() {
		if(examController == null) {
			ConditionPanel_Phase1 conditionPanel = new ConditionPanel_Phase1(frame, true, BannerOrientation.Top);			
			File outputFolder = null;
			if(APPLICATION_TYPE == ApplicationType.WebStartExperimentClient) { 
				//System.out.println("current directory: " + new File(".").getAbsolutePath());
				String homeDir= System.getProperty("user.");		
				if(homeDir == null) {
					homeDir = new File("").getAbsolutePath();
				}
				//Create local data folder if it doesn't exist
				outputFolder = new File(homeDir + "/ICArUS_Data");
				if(!outputFolder.exists()) {										
					outputFolder.mkdir();
				}
			} else {
				outputFolder = new File("data/response_data");
			}
			//examController = new IcarusExamController_Phase1(new ScoreComputer(), new DoNothingSubjectDataRecorder<IcarusExam_Phase2, Mission>());
			examController = new IcarusExamController_Phase1(new ScoreComputer(), 
					applicationOptions.isClientMode() ? 
						new RemoteSubjectDataRecorder<IcarusExam_Phase1, TaskTestPhase<?>, TutorialPhase>(
								dataSourceConnection, new File("data/response_data")) : 
						new LocalSubjectDataRecorder<IcarusExam_Phase1, TaskTestPhase<?>, TutorialPhase>(
								outputFolder));
			ExperimentPanel_Phase1 experimentPanel = new ExperimentPanel_Phase1(frame, conditionPanel, applicationOptions.isClientMode());
			examController.setExperimentPanel(experimentPanel);
			experimentPanel.addSubjectActionListener(this);
		}
		return examController;
	}
	
	/** Validates an exam XML file. */
	protected void validateExam(File examFile) {	               					                  
		try{
			URL examFileURL = examFile.toURI().toURL();

			//Validate the exam file
			Exception e = null;
			try {
				IcarusExamLoader_Phase1.unmarshalExam(examFileURL, true);
			} catch(Exception e1) {
				e = e1;
			}    				

			if(e == null) {
				JOptionPane.showMessageDialog(frame,
						"The exam file was valid.",
						"Exam Valid", 
						JOptionPane.INFORMATION_MESSAGE);
			}
			else {
				ValidationErrorsDlg.showErrorDialog(frame, 
						"Exam Not Valid", 
						"The exam file contained errors: ",
						e.getMessage());
			}    					
		} catch (MalformedURLException e) {
			handleException(new Exception("Error loading exam file", e), true);
		}
	}
	
	protected File chooseFeatureVectorFile(FeatureType type) {
		if(featureVectorFileChooser == null) {				            		
			featureVectorFileChooser = new JFileChooser(new File("."));
			featureVectorFileChooser.setFileFilter(new FileNameExtensionFilter("Feature Vector Files (*.csv, *.kml)", "csv", "kml"));
		}		
		switch(type) {
		case Task:
			featureVectorFileChooser.setDialogTitle("Open Task Feature Vector File");	
			break;
		case Road:
			featureVectorFileChooser.setDialogTitle("Open Roads Feature Vector File");
			break;
		case Region: case Socint :
			featureVectorFileChooser.setDialogTitle("Open Regions Feature Vector File");
			break;
		}

		// Show open dialog
		if(featureVectorFileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
			return featureVectorFileChooser.getSelectedFile();
		}
		return null;
	}

	/** Load a single feature vector and show it in the GUI */
	private boolean openFeatureVector(URL featureVectorFileUrl, FeatureType type) {
		if(mapPanel == null) {
			mapPanel = new MapPanelContainer(frame, gridSize, true, true);
		}
		
		Exception e = null;
		//Only show scale bar in Tasks 3-7
		boolean showScaleBar = true;
		TaskData taskData = null;
		ArrayList<Road> roads = null;
		SocintOverlay regions = null;
		try {
			String fileName = new File(featureVectorFileUrl.toURI()).getName();
			switch(type) {
			case Task:
				taskData = FeatureVectorManager.getInstance().getTaskData(featureVectorFileUrl, gridSize);
				taskFileName = fileName;
				showScaleBar = (taskFileName.contains("3_") || taskFileName.contains("4_") || taskFileName.contains("5_") ||
						taskFileName.contains("6_") || taskFileName.contains("7_") ||
						taskData.getAttacks() == null || taskData.getAttacks().size() <= 4);				
				break;
			case Road:	
				roads = FeatureVectorManager.getInstance().getRoads(featureVectorFileUrl, gridSize);			
				roadFileName = fileName;
				break;
			case Region: case Socint :
				regions = FeatureVectorManager.getInstance().getRegionsOverlay(featureVectorFileUrl, gridSize);
				regionFileName = fileName;
				break;
			}			
			updateFrameTitleForFeatureVectorData();
		} catch(Exception e1) {
			e = e1;
		}    				

		if(e != null) {
			ValidationErrorsDlg.showErrorDialog(frame, 
					"Feature Vector Not Valid", 
					"The feature vector file file contained errors: ",
					e.getMessage());							
			return false;
		}
		else {
			mapPanel.setShowScale(showScaleBar);
			if(taskData != null) {
				boolean imintFound = false;
				boolean movintFound = false;
				boolean sigintFound = false;
				Set<GroupAttack> locationsFound = new TreeSet<GroupAttack>(new GroupAttackComparator());
				Set<GroupType> groupsFound = new TreeSet<GroupType>();

				if(taskData.getAttacks() != null && !taskData.getAttacks().isEmpty()) {
					mapPanel.setSigactLayerEnabled(true);
					mapPanel.setSigactLocations(taskData.getAttacks(), AttackLocationType.GROUP_ATTACK, true);
					for(GroupAttack attack : taskData.getAttacks()) {
						locationsFound.add(attack);
						if(attack.getIntelReport() != null) {
							if(attack.getIntelReport().getImintInfo() != null) {
								imintFound = true;
							}
							if(attack.getIntelReport().getMovintInfo() != null) {
								movintFound = true;
							}
							if(attack.getIntelReport().getSigintInfo() != null) {
								sigintFound = true;
							}
						}
					}
					mapPanel.setSigactLocationsInLegend(locationsFound);
					mapPanel.setSigactsLegendItemVisible(true);
				}
				else {
					mapPanel.setSigactLayerEnabled(false);
					mapPanel.setSigactsLegendItemVisible(false);
				}

				if(taskData.getCenters() != null && !taskData.getCenters().isEmpty()) {
					mapPanel.setGroupCentersLayerEnabled(true);
					mapPanel.setGroupCenters(taskData.getCenters(), true);
					for(GroupCenter groupCenter : taskData.getCenters()) {
						groupsFound.add(groupCenter.getGroup());
						if(groupCenter.getIntelReport() != null) {
							if(groupCenter.getIntelReport().getImintInfo() != null) {
								imintFound = true;
							}
							if(groupCenter.getIntelReport().getMovintInfo() != null) {
								movintFound = true;
							}
							if(groupCenter.getIntelReport().getSigintInfo() != null) {
								sigintFound = true;
							}
						}
					}
					mapPanel.setGroupCenterGroupsForLegend(groupsFound);
					mapPanel.setGroupCentersLegendItemVisible(true);									
				}
				else {
					mapPanel.setGroupCentersLayerEnabled(false);
					mapPanel.setGroupCentersLegendItemVisible(false);
				}								

				mapPanel.setImintLayerEnabled(imintFound);
				mapPanel.setImintLegendItemVisible(imintFound);

				mapPanel.setMovintLayerEnabled(movintFound);
				mapPanel.setMovintLegendItemVisible(movintFound);

				mapPanel.setSigintLayerEnabled(sigintFound);
				mapPanel.setSigintLegendItemVisible(sigintFound);
			}

			if(roads != null) {
				mapPanel.setRoadLayerEnabled(true);								
				mapPanel.setRoads(roads);
				mapPanel.setRoadsLegendItemVisible(true);
			}

			if(regions != null) {
				mapPanel.setSocintRegionsLayerEnabled(true);
				mapPanel.setSocintRegionsOverlay(regions);
				mapPanel.setSocintGroupsForLegend(regions.getGroups());								
				mapPanel.setSocintLegendItemVisible(true);
			}

			mapPanel.redrawMap();

			contentPanel.removeAll();
			contentPanel.add(mapPanel);
			contentPanel.validate();
			contentPanel.repaint();
			
			return true;
		}
	}
	
	public void clearTaskData() {
		taskFileName = "";
		updateFrameTitleForFeatureVectorData();
		mapPanel.removeAllSigactLocations();
		mapPanel.setSigactLayerEnabled(false);
		mapPanel.setSigactLocationsInLegend(null);
		mapPanel.setSigactsLegendItemVisible(false);
		
		mapPanel.removeAllGroupCenters();					
		mapPanel.setGroupCentersLayerEnabled(false);
		mapPanel.setGroupCenterGroupsForLegend(null);
		mapPanel.setGroupCentersLegendItemVisible(false);
		
		mapPanel.setImintLayerEnabled(false);
		mapPanel.setImintLegendItemVisible(false);
		
		mapPanel.setMovintLayerEnabled(false);
		mapPanel.setMovintLegendItemVisible(false);
		
		mapPanel.setSigintLayerEnabled(false);
		mapPanel.setSigintLegendItemVisible(false);		
		mapPanel.redrawMap();
	}
	
	public void clearRoadData() {
		roadFileName = "";
		updateFrameTitleForFeatureVectorData();
		mapPanel.removeAllRoads();
		mapPanel.setRoadLayerEnabled(false);
		mapPanel.setRoadsLegendItemVisible(false);
		mapPanel.redrawMap();
	}
	
	public void clearRegionData() {
		regionFileName = "";
		updateFrameTitleForFeatureVectorData();
		mapPanel.removeAllSocintRegions();
		mapPanel.setSocintRegionsLayerEnabled(false);
		mapPanel.setSocintGroupsForLegend(null);
		mapPanel.setSocintLegendItemVisible(false);
		mapPanel.redrawMap();
	}
	
	protected void updateFrameTitleForFeatureVectorData() {
		StringBuilder title = new StringBuilder(APPLICATION_NAME);
		if(!taskFileName.isEmpty() || !roadFileName.isEmpty() || !regionFileName.isEmpty()) {
			title.append(" - ");
			boolean commaNeeded = false;
			if(!taskFileName.isEmpty()) {
				title.append(taskFileName);
				commaNeeded = true;
			}
			if(!roadFileName.isEmpty()) {
				if(commaNeeded) {
					title.append(", ");
				}
				title.append(roadFileName);
				commaNeeded = true;
			}
			if(!regionFileName.isEmpty()) {
				if(commaNeeded) {
					title.append(", ");
				}
				title.append(regionFileName);
			}
		}
		frame.setTitle(title.toString());
	}
	
	public static void main(String[] args) throws IOException {
		//Configure library path for VLC dlls
		System.setProperty("jna.library.path", "lib");
		System.setProperty("java.library.path", "lib");
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				IcarusLookAndFeel.initializeICArUSLookAndFeel();				
				new IcarusTEGUIMain();
			}
		});		
	}
}