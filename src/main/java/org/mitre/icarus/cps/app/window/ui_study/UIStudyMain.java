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
package org.mitre.icarus.cps.app.window.ui_study;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Calendar;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.mitre.icarus.cps.app.experiment.IcarusSubjectData;
import org.mitre.icarus.cps.app.experiment.ui_study.UIStudyController;
import org.mitre.icarus.cps.app.experiment.ui_study.data_recorder.SubjectDataRecorder;
import org.mitre.icarus.cps.app.experiment.ui_study.exam.UIStudyExam;
import org.mitre.icarus.cps.app.experiment.ui_study.exam.UIStudyExamLoader;
import org.mitre.icarus.cps.app.util.CPSUtils;
import org.mitre.icarus.cps.app.widgets.ImageManager;
import org.mitre.icarus.cps.app.widgets.WidgetConstants;
import org.mitre.icarus.cps.app.widgets.dialog.AboutDlg;
import org.mitre.icarus.cps.app.widgets.dialog.ErrorDlg;
import org.mitre.icarus.cps.app.widgets.dialog.ParticipantIdDlg;
import org.mitre.icarus.cps.app.widgets.dialog.ParticipantIdPanel;
import org.mitre.icarus.cps.app.widgets.dialog.SkipToPhaseDlg;
import org.mitre.icarus.cps.app.widgets.security_banner.JSecurityBanner;
import org.mitre.icarus.cps.app.widgets.security_banner.JSecurityBanner.Classification;
import org.mitre.icarus.cps.app.widgets.ui_study.ConditionPanel_UIStudy;
import org.mitre.icarus.cps.app.widgets.ui_study.ExperimentPanel_UIStudy;
import org.mitre.icarus.cps.experiment_core.gui.ConditionPanel.BannerOrientation;
import org.mitre.icarus.cps.web.model.Site;

/**
 * Main entry point for the UI Study GUI.
 * 
 * @author CBONACETO
 *
 */
public class UIStudyMain {
	
	/** The application version information */
	public static final String VERSION = "1.0";
	
	/** The application name */
	public static final String APPLICATION_NAME = "ICArUS UI Study";
	
	/** Whether to show the security banner */
	public static final boolean SHOW_SECURITY_BANNER = false;
	
	/** Application frame */
	protected JFrame frame;
	
	/** The file menu */
	protected JMenu fileMenu;	
	
	/** The exam menu */
	protected JMenu examMenu;
	
	/** File chooser for opening UI exam files */
	protected static JFileChooser examFileChooser;
	
	/** The default UI study exam file (if any) */
	protected String defaultUIStudyFile = "data/UI Study/UIStudy.xml";
	
	/** URL to the current UI study exam */
	protected URL examFileURL = null;
	
	/** The current UI study exam */
	protected UIStudyExam exam;
	
	/** The current participant ID */
	protected String participantId = "TEST";
	
	/** The current participant site */
	protected Site participantSite = new Site("TEST", "TEST");

	/** The exam controller */
	protected UIStudyController examController;
	
	/** Contains all panels (aside from the security banner panel) */
	protected JPanel contentPanel;
	
	public UIStudyMain() {
		//Create GUI in the event dispatch thread
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				frame = new JFrame(APPLICATION_NAME);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				Image icon = ImageManager.getImage(ImageManager.ICARUS_LOGO);
				if(icon != null) {
					frame.setIconImage(icon);
				}				
				if(defaultUIStudyFile != null) {
					//Prompt for a participant ID and start the default UI study				
					final ParticipantIdPanel dlg = new ParticipantIdPanel(Arrays.asList(new Site("Bedford"), new Site("McLean")), 
							"Begin", false);
					frame.getContentPane().add(dlg);								
					frame.pack();
					frame.setResizable(false);
					CPSUtils.centerFrameOnScreen(frame);
					frame.setVisible(true);	

					dlg.addOkButtonActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							participantId = dlg.getSubjectId();
							participantSite = dlg.getSite();
							initializeFrame();
						}
					});	
				}
				else {
					initializeFrame();
				}
			}
		});
	}

	/** 
	 * Confirm whether or not to exit the application
	 */
	public void confirmExit() {
		if(JOptionPane.showConfirmDialog(frame, "Would you like to exit the application?", "", 
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}
	
	private void initializeFrame() {
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
		
		if(SHOW_SECURITY_BANNER) {
			//Add the security banner panel
			constraints.gridy++;
			constraints.fill = GridBagConstraints.HORIZONTAL;
			constraints.weighty = 0;
			frame.getContentPane().add(WidgetConstants.createDefaultSeparator(), constraints);
			constraints.gridy++;
			String year = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
			frame.getContentPane().add(new JSecurityBanner(JSecurityBanner.COPYRIGHT_CHAR + 
					" " + year + " The MITRE Corporation. All Rights Reserved. ", Classification.FOUO),
					constraints);			
		}

		//Create menu bar with file menu, exam menu, and help menu
		frame.setJMenuBar(createMenuBar());

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
		contentPanel.add(examController.getExperimentPanel());
		frame.pack();
		//Adjust size of experiment panel if frame size exceeds screen dimensions
		Dimension screenSize = frame.getToolkit().getScreenSize();
		int widthOver = frame.getWidth() - screenSize.width;
		int heightOver = frame.getHeight() - screenSize.height;
		if(widthOver > 0 || heightOver > 0) {
			Dimension experimentPanelSize = examController.getExperimentPanel().getPreferredSize();
			int newWidth = (widthOver > 0) ? experimentPanelSize.width - widthOver : experimentPanelSize.width;
			if(newWidth < 1) {
				newWidth = 1;
			}
			int newHeight = (heightOver > 0) ? experimentPanelSize.height - heightOver : experimentPanelSize.height;
			if(newHeight < 1) {
				newHeight = 1;
			}
			examController.getExperimentPanel().setMaximumSize(new Dimension(newWidth, newHeight));
			frame.pack();
		}		
		frame.setMinimumSize(frame.getSize());		
		
		boolean defaultUIStudyOpened = false;
		if(defaultUIStudyFile != null) {
			//Try to open and show the default UI study
			defaultUIStudyOpened = openDefaultUIStudy();
		}
		
		if(!defaultUIStudyOpened) {
			//Show blank start-up screen
			contentPanel.removeAll();
		}
		else {
			examMenu.setEnabled(true);	
		}
		
		CPSUtils.centerFrameOnScreen(frame);		
		frame.setVisible(true);		
		System.out.println("Window Size: " + frame.getSize());
	}
	
	private void createExamController() {
		if(examController == null) {
			ConditionPanel_UIStudy conditionPanel = new ConditionPanel_UIStudy(frame, true, BannerOrientation.Top);
			/*examController = new IcarusExamController_Phase1(conditionPanel, 
					new ScoreComputer(), 
					new SubjectDataRecorder());*/
			examController = new UIStudyController(conditionPanel, new SubjectDataRecorder());
			ExperimentPanel_UIStudy experimentPanel = new ExperimentPanel_UIStudy(frame, conditionPanel);
			examController.setExperimentPanel(experimentPanel);
		}			
	}
	
	/** Loads the default study and shows it in the GUI. */	
	private boolean openDefaultUIStudy() {
		try {
			examFileURL = new File(defaultUIStudyFile).toURI().toURL();
		} catch (Exception e1) {
			ErrorDlg.showErrorDialog(frame, new Exception("Error loading default UI study", e1), true);
			return false;
		}
		return openExam();
	}
	
	/** Shows a file chooser to open an exam and loads it in the GUI. Returns null if no file was chosen. */
	private File chooseExamFile() {
		if(examFileChooser == null) {				            		
			examFileChooser = new JFileChooser(new File("."));
			examFileChooser.setDialogTitle("Open UI Study");	
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

	/** Loads the current exam and shows it in the GUI. */
	private boolean openExam() {
		if(examController == null) {
			createExamController();
		}			
		try {
			exam = UIStudyExamLoader.unmarshalExam(examFileURL);
			frame.setTitle(APPLICATION_NAME + " - " + new File(examFileURL.toURI()).getName());
		} catch (Exception e) {
			ErrorDlg.showErrorDialog(frame, new Exception("Error loading UI study file", e), true);
			return false;
		}

		if(exam != null) {		
			exam.setOriginalPath(examFileURL);
			examController.initializeExperimentController(exam);

			contentPanel.removeAll();
			contentPanel.add(examController.getExperimentPanel());
			contentPanel.validate();
			//frame.pack();			
			contentPanel.repaint();
			//frame.setMinimumSize(frame.getSize());		

			examController.startExperiment(new IcarusSubjectData(participantId, participantSite, 0));
		}
		return true;
	}
	
	/** Create a menu bar with the file, current exam, and help menu options */	
	private JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		
		// Create the file menu  
		final JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		menuBar.add(fileMenu);		
		
		//Create the current exam menu
		examMenu = new JMenu("Exam Options");
		examMenu.setEnabled(false);
		examMenu.setMnemonic(KeyEvent.VK_E);
		menuBar.add(examMenu);
		
		//Create the help menu with the about item
		final JMenu helpMenu = new JMenu("Help");
		menuBar.add(helpMenu);
		helpMenu.setMnemonic(KeyEvent.VK_H);
		JMenuItem aboutMenuItem = new JMenuItem("About " + APPLICATION_NAME);
		aboutMenuItem.setMnemonic(KeyEvent.VK_A);
		helpMenu.add(aboutMenuItem);
		aboutMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Show the About dialog
				AboutDlg.showDefaultAboutDlg(frame, false, false, APPLICATION_NAME, VERSION);
			}			
		});
		
		//////////////////////// Create file menu items ///////////////////////////////////		
		//Create default UI study exam option
		if(defaultUIStudyFile != null) {
			JMenuItem beginPilotItem = new JMenuItem("Open Current UI Study");
			beginPilotItem.setMnemonic(KeyEvent.VK_C);
			fileMenu.add(beginPilotItem);
			beginPilotItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if(openDefaultUIStudy()) {
						examMenu.setEnabled(true);
					}
				}	
			});			
		}
		
		//Create open other UI Study exam option
		JMenuItem openExamItem = new JMenuItem("Open Other UI Study File");
		openExamItem.setMnemonic(KeyEvent.VK_E);
		fileMenu.add(openExamItem);
		openExamItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				File examFile = chooseExamFile();
				if(examFile != null) {
					try{
						examFileURL = examFile.toURI().toURL();
						if(openExam()) {
							examMenu.setEnabled(true);
						}					
					} catch (MalformedURLException e) {
						ErrorDlg.showErrorDialog(frame, new Exception("Error loading exam file", e), true);
					}				
				}
			}
		});  		
		
		fileMenu.addSeparator();

		//Create exit menu item and add to file menu    	
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.setMnemonic(KeyEvent.VK_X);
		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//confirmExit();
				System.exit(0);
			}
		});
		fileMenu.add(exitItem);
		//////////////////////// End create file menu items //////////////////////////////////		
		
		////////////////////////Create exam menu items /////////////////////////////////
		//Create skip to task menu item
		final JMenuItem skipToTaskItem = new JMenuItem("Skip to Task");
		examMenu.add(skipToTaskItem);
		skipToTaskItem.setMnemonic(KeyEvent.VK_S);
		skipToTaskItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Show a skip to phase dialog
				if(exam != null && examController != null) {
					SkipToPhaseDlg dlg = new SkipToPhaseDlg(frame, exam.getTasks(), 
							examController.getCurrentConditionIndex(), "Task");
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
		
		//Create change participant menu item
		JMenuItem changeParticipantItem = new JMenuItem("Change Participant ID");
		examMenu.add(changeParticipantItem);    	
		changeParticipantItem.setMnemonic(KeyEvent.VK_P);
		changeParticipantItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {    	
				//Show participant ID dialog
				IcarusSubjectData subjectData = ParticipantIdDlg.showDialog(frame, "Enter Participant ID",
						Arrays.asList(new Site("Bedford"), new Site("McLean")), participantSite, "OK", true);

				if(subjectData != null) {
					participantId = subjectData.getSubjectId();
					participantSite = subjectData.getSite();
					if(examController != null) {
						examController.setSubjectData(participantId, participantSite);
					}    				
				}
			}
		});

		//Create restart exam menu item
		JMenuItem restartExamItem = new JMenuItem("Restart Exam");
		examMenu.add(restartExamItem);    	
		restartExamItem.setMnemonic(KeyEvent.VK_R);
		restartExamItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(JOptionPane.showConfirmDialog(frame, "Would you like to restart the current study?", "", 
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
					if(examController != null) {
						examController.restartExperiment();
					}
				}					
			}
		});
		////////////////////////End create exam menu items /////////////////////////////////		

		return menuBar;
	}
	
	public static void main(String[] args) throws IOException {		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				//Use native L&F
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());					
				} catch (Exception e) { }
				
				//Change the default tree expansion images
				UIManager.put("Tree.expandedIcon", ImageManager.getImageIcon(ImageManager.MINUS_ICON));
				UIManager.put("Tree.collapsedIcon", ImageManager.getImageIcon(ImageManager.PLUS_ICON));
				
				//Change the default fonts
				WidgetConstants.setUIFont(new javax.swing.plaf.FontUIResource(WidgetConstants.FONT_DEFAULT));
				UIManager.put("TextArea.font", WidgetConstants.FONT_DEFAULT);
				UIManager.put("Tree.font", WidgetConstants.FONT_DEFAULT);
				UIManager.put("List.font", WidgetConstants.FONT_DEFAULT);
				UIManager.put("Table.font", WidgetConstants.FONT_DEFAULT);
				
				new UIStudyMain();
			}
		});		
	}
}