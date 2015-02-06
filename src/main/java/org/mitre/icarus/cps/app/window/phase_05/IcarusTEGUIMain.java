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
package org.mitre.icarus.cps.app.window.phase_05;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.mitre.icarus.cps.app.experiment.IcarusSubjectData;
import org.mitre.icarus.cps.app.experiment.phase_05.IcarusExamController_Phase05;
import org.mitre.icarus.cps.app.util.CPSUtils;
import org.mitre.icarus.cps.app.widgets.WidgetConstants;
import org.mitre.icarus.cps.app.widgets.dialog.ErrorDlg;
import org.mitre.icarus.cps.app.widgets.dialog.ParticipantIdDlg;
import org.mitre.icarus.cps.app.widgets.dialog.ParticipantIdPanel;
import org.mitre.icarus.cps.app.widgets.dialog.SkipToPhaseDlg;
import org.mitre.icarus.cps.app.widgets.dialog.ValidationErrorsDlg;
import org.mitre.icarus.cps.app.widgets.map.phase_05.FeatureVector;
import org.mitre.icarus.cps.app.widgets.map.phase_05.FeatureVectorPanel;
import org.mitre.icarus.cps.app.widgets.phase_05.dialog.FeatureVectorSelectionDlg;
import org.mitre.icarus.cps.app.widgets.phase_05.dialog.FeatureVectorSelectionDlg.FeatureVectorFiles;
import org.mitre.icarus.cps.app.widgets.phase_05.experiment.ConditionPanel_Phase05;
import org.mitre.icarus.cps.app.widgets.phase_05.experiment.ExperimentPanel_Phase05;
import org.mitre.icarus.cps.app.window.phase_05.GUIOptions.OptionType;
import org.mitre.icarus.cps.exam.phase_05.IcarusExam_Phase05;
import org.mitre.icarus.cps.exam.phase_05.loader.IcarusExamLoader_Phase05;
import org.mitre.icarus.cps.experiment_core.gui.ConditionPanel.BannerOrientation;
import org.mitre.icarus.cps.feature_vector.phase_05.FeatureVectorManager;
import org.mitre.icarus.cps.web.model.Site;

/**
 * Main entry point for the Test & Evaluation GUI for Phase 05 Challenge
 * Problem Format (Facility Identification).
 * 
 * @author Jing Hu
 */
public class IcarusTEGUIMain {	

	/** Application frame */
	protected JFrame frame;

	/** File chooser for opening exam files */
	protected static JFileChooser examFileChooser;

	/** The current pilot study file (if any) */
	protected String currentPilotStudyFile = "data/PILOT_2/PilotStudy2.xml";

	/** The current exam */
	protected IcarusExam_Phase05 exam;

	/** The current participant ID */
	protected String participantId;

	/** The exam controller */
	protected IcarusExamController_Phase05 examController;

	/** URL to the current exam */
	protected URL examFileURL = null;

	/** Panel for showing a single feature vector */
	protected JPanel imagePanelContainer;
	protected FeatureVectorPanel imagePanel;

	/** Current feature vector file (when viewing a single feature vector) */
	protected File currentFeatureVectorFile;

	/** Current object palette file (when viewing a single feature vector) */
	protected File currentObjectPaletteFile;

	public IcarusTEGUIMain(final GUIOptions optionsEnabled) {
		//Create GUI in the event dispatch thread
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {				

				frame = new JFrame("ICArUS Test and Evaluation GUI");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
				if(optionsEnabled.isOptionEnabled(OptionType.Open_Pilot_Study)) {
					//Prompt for a participant ID and start the pilot study				
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
							initializeFrame(optionsEnabled);
						}
					});	
				}
				else {
					initializeFrame(optionsEnabled);
				}
			}
		});
	}
	
	protected void initializeFrame(GUIOptions optionsEnabled) {
		frame.setResizable(true);
		frame.setVisible(false);
		frame.getContentPane().removeAll();

		//Create menu bar with options menu
		frame.setJMenuBar(createMenuBar(optionsEnabled));

		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {				
				confirmExit();
			}
		});
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		boolean pilotOpened = false;
		if(optionsEnabled.isOptionEnabled(OptionType.Open_Pilot_Study)) {
			//Try to open and show the pilot study
			pilotOpened = openPilotStudy();
		}
		
		if(!pilotOpened) {
			//Show blank start-up screen
			JPanel blankPanel = new JPanel();
			blankPanel.setPreferredSize(new Dimension(580, 520));
			frame.getContentPane().removeAll();
			frame.getContentPane().add(blankPanel);
			frame.pack();
		}
		
		CPSUtils.centerFrameOnScreen(frame);
		frame.setVisible(true);
		// color test
		//ColorManager.main(args)
	}

	/** Create a menu bar with the options menu */
	protected JMenuBar createMenuBar(GUIOptions optionsEnabled) {
		JMenuBar menuBar = new JMenuBar();

		// Define and add the options menu to the menu bar
		JMenu optionsMenu = new JMenu("Options");
		optionsMenu.setMnemonic(KeyEvent.VK_O);
		menuBar.add(optionsMenu); 

		final JMenuItem skipToPhaseItem = new JMenuItem("Skip to Phase in Current Exam");

		//Create open current pilot study option
		if(optionsEnabled.isOptionEnabled(OptionType.Open_Pilot_Study)) {
			JMenuItem beginPilotItem = new JMenuItem("Open Current Pilot Exam");
			beginPilotItem.setMnemonic(KeyEvent.VK_P);
			optionsMenu.add(beginPilotItem);
			beginPilotItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if(openPilotStudy()) {
						skipToPhaseItem.setEnabled(true);
					}
				}	
			});  

			optionsMenu.addSeparator();
		}

		// Create open feature vector file menu item and add to options menu
		boolean separatorNeeded = false;
		if(optionsEnabled.isOptionEnabled(OptionType.Open_Feature_Vector)) {
			separatorNeeded = true;
			JMenuItem openFeatureVectorItem = new JMenuItem("Open Feature Vector File");
			openFeatureVectorItem.setMnemonic(KeyEvent.VK_F);
			optionsMenu.add(openFeatureVectorItem);
			openFeatureVectorItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					// Show feature vector selection dialog
					FeatureVectorFiles files = FeatureVectorSelectionDlg.showDialog(
							frame, currentFeatureVectorFile, currentObjectPaletteFile);
					if(files != null) {
						currentFeatureVectorFile = files.featureVectorFile;
						currentObjectPaletteFile = files.objectPaletteFile;
						if(openFeatureVector()) {
							skipToPhaseItem.setEnabled(false);
						}
					}				            	
				}
			});  
		}

		// Create open exam file menu item and add to options menu
		if(optionsEnabled.isOptionEnabled(OptionType.Open_Exam)) {
			separatorNeeded = true;
			JMenuItem openExamItem = new JMenuItem("Open Exam File");
			openExamItem.setMnemonic(KeyEvent.VK_E);
			optionsMenu.add(openExamItem);
			openExamItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if(examFileChooser == null) {				            		
						examFileChooser = new JFileChooser(new File("."));
						examFileChooser.setFileFilter(new FileNameExtensionFilter("Exam Files (*.xml)", "xml"));
					}

					// Show open dialog
					if(examFileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
						File selFile = examFileChooser.getSelectedFile();					            			               					                  
						try{
							examFileURL = selFile.toURI().toURL();
							if(openExam()) {
								skipToPhaseItem.setEnabled(true);
							}
						} catch (MalformedURLException e) {
							ErrorDlg.showErrorDialog(frame, new Exception("Error loading exam file", e), true);
							//System.err.println("Could not open file " + selFile);
							//e.printStackTrace(); 
						}
					}				            	
				}
			});  
		}

		if(separatorNeeded) {
			optionsMenu.addSeparator();
		}    	
		separatorNeeded = false;

		//Create validate exam file menu item
		if(optionsEnabled.isOptionEnabled(OptionType.Validate_Exam)) {
			separatorNeeded = true;
			JMenuItem validateExamItem = new JMenuItem("Validate Exam File");
			optionsMenu.add(validateExamItem);
			validateExamItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if(examFileChooser == null) {				            		
						examFileChooser = new JFileChooser(new File("."));
						examFileChooser.setFileFilter(new FileNameExtensionFilter("Exam Files (*.xml)", "xml"));
					}

					// Show open dialog
					if(examFileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
						File selFile = examFileChooser.getSelectedFile();					            			               					                  
						try{
							URL examFileURL = selFile.toURI().toURL();

							//Validate the exam file
							Exception e = null;
							try {
								IcarusExamLoader_Phase05.unmarshalExam(examFileURL, true);
							} catch(Exception e1) {
								e = e1;
							}    				

							if(e == null) {
								JOptionPane.showMessageDialog(frame,
										"The exam file was valid",
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
							ErrorDlg.showErrorDialog(frame, new Exception("Error loading exam file", e), true);
						}
					}				            	
				}
			});  
		}

		//Create validate response file menu item
		if(optionsEnabled.isOptionEnabled(OptionType.Validate_Response)) {
			separatorNeeded = true;
			JMenuItem validateResponseItem = new JMenuItem("Validate Exam Response File");
			optionsMenu.add(validateResponseItem);
			validateResponseItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if(examFileChooser == null) {				            		
						examFileChooser = new JFileChooser(new File("."));
						examFileChooser.setFileFilter(new FileNameExtensionFilter("Exam Response Files (*.xml)", "xml"));
					}

					// Show open dialog
					if(examFileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
						File selFile = examFileChooser.getSelectedFile();					            			               					                  
						try{
							URL responseFileURL = selFile.toURI().toURL();

							//Validate the exam response file
							Exception e = null;
							try {
								IcarusExamLoader_Phase05.unmarshalExamResponse(responseFileURL, true);
							} catch(Exception e1) {
								e = e1;
							}    				

							if(e == null) {
								JOptionPane.showMessageDialog(frame,
										"The exam response file was valid",
										"Exam Response Valid", 
										JOptionPane.INFORMATION_MESSAGE);
							}
							else {
								ValidationErrorsDlg.showErrorDialog(frame, 
										"Exam Response Not Valid", 
										"The exam response file contained errors: ",
										e.getMessage());
							}    					
						} catch (MalformedURLException e) {
							ErrorDlg.showErrorDialog(frame, new Exception("Error loading exam response file", e), true);
						}
					}				            	
				}
			});  
		}

		if(separatorNeeded) {
			optionsMenu.addSeparator();
		}
		separatorNeeded = false;

		//Create skip to phase menu item and add to options menu
		if(optionsEnabled.isOptionEnabled(OptionType.Skip_To_Phase)) {
			separatorNeeded = true;
			optionsMenu.add(skipToPhaseItem);
			skipToPhaseItem.setMnemonic(KeyEvent.VK_S);
			skipToPhaseItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//Show a skip to phase dialog
					if(exam != null) {
						SkipToPhaseDlg dlg = new SkipToPhaseDlg(frame, exam.getPhases(), 
								examController.getCurrentConditionIndex());
						dlg.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
						dlg.setModal(true);
						dlg.setLocationRelativeTo(frame);
						dlg.setVisible(true);
						if(dlg.getButtonPressed() == SkipToPhaseDlg.ButtonType.OK && 
								dlg.getPhaseIndex() >= 0) {
							//Skip to the phase the user selected
							examController.skipToCondition(dlg.getPhaseIndex());
						}
					}
				}
			}); 
		}

		//Create change participant ID option
		if(optionsEnabled.isOptionEnabled(OptionType.Change_Participant)) {
			separatorNeeded = true;
			JMenuItem changeParticipantItem = new JMenuItem("Change Participant ID");
			optionsMenu.add(changeParticipantItem);    	
			changeParticipantItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {    	
					//Show participant ID dialog
					IcarusSubjectData subjectData = ParticipantIdDlg.showDialog(frame, "Enter Participant ID",
							Arrays.asList(new Site("Bedford"), new Site("McLean")), null, "OK", true);

					if(subjectData != null) {
						participantId = subjectData.getSubjectId();
						if(examController != null) {
							examController.setSubjectData(participantId, null);
						}    				
					}
				}
			});
		}

		if(separatorNeeded) {
			optionsMenu.addSeparator();
		}

		//Create exit menu item and add to options menu    	
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.setMnemonic(KeyEvent.VK_X);
		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				confirmExit();
			}
		});
		optionsMenu.add(exitItem);

		return menuBar;
	}

	/** Load the pilot study and show in the GUI */
	protected boolean openPilotStudy() {
		File examFile = new File("data/Phase_05_CPD/PILOT 2/PilotStudy2.xml");
		try {
			examFileURL = examFile.toURI().toURL();
			exam = IcarusExamLoader_Phase05.unmarshalExam(examFileURL);
		} catch (Exception e1) {
			ErrorDlg.showErrorDialog(frame, new Exception("Error loading pilot study", e1), true);
			return false;
		}

		return openExam();
	}

	/** Load an exam and show in the GUI */
	protected boolean openExam() {
		if(examController == null) {
			ConditionPanel_Phase05 conditionPanel = new ConditionPanel_Phase05(frame, true, BannerOrientation.Top);
			examController = new IcarusExamController_Phase05(conditionPanel);
			ExperimentPanel_Phase05 experimentPanel = new ExperimentPanel_Phase05(frame, conditionPanel);
			examController.setExperimentPanel(experimentPanel);
		}			

		try {
			exam = IcarusExamLoader_Phase05.unmarshalExam(examFileURL);
		} catch (Exception e) {
			ErrorDlg.showErrorDialog(frame, new Exception("Error loading exam file", e), true);
			return false;
		}

		if(exam != null) {		
			exam.setOriginalPath(examFileURL);
			examController.initializeExperimentController(exam);

			frame.getContentPane().removeAll();
			frame.getContentPane().add(examController.getExperimentPanel());
			frame.pack();
			frame.getContentPane().validate();
			frame.getContentPane().repaint();
			frame.setMinimumSize(frame.getSize());		

			examController.startExperiment(new IcarusSubjectData(participantId, null, 0));
		}
		return true;
	}

	/** Load a single feature vector and show it in the GUI */
	protected boolean openFeatureVector() {
		if(imagePanelContainer == null) {
			imagePanelContainer  = new JPanel(new GridBagLayout());
			imagePanelContainer.setBackground(Color.WHITE);
			imagePanelContainer.setPreferredSize(new Dimension(580, 520));
			imagePanelContainer.setMinimumSize(imagePanelContainer.getPreferredSize());	
			imagePanelContainer.setBorder(WidgetConstants.DEFAULT_BORDER);
			imagePanel = new FeatureVectorPanel();
			imagePanel.setPreferredSize(new Dimension(510, 510));
			imagePanel.setMinimumSize(imagePanel.getPreferredSize());
			imagePanelContainer.add(imagePanel);		

			//Add resize listener to keep image panel square
			imagePanelContainer.addComponentListener(new ComponentAdapter() {
				@Override
				public void componentResized(ComponentEvent arg0) {
					int width = imagePanelContainer.getWidth();
					int height = imagePanelContainer.getHeight();
					if(width > height) {
						width = height;
					}
					imagePanel.setPreferredSize(new Dimension(width-10, width-10));
					imagePanelContainer.revalidate();
				}
			});		
		}

		FeatureVector featureVector = null;
		try {
			featureVector = FeatureVectorManager.getInstance().getFeatureVector(
					currentFeatureVectorFile.toURI().toURL(), 
					currentObjectPaletteFile.toURI().toURL());
		} catch (Exception e) {
			ErrorDlg.showErrorDialog(frame, new Exception("Error loading feature vector files", e), true);
			return false;
		}

		if(featureVector != null) {	
			imagePanel.setWorld(featureVector);
			frame.getContentPane().removeAll();
			frame.getContentPane().add(imagePanelContainer);
			frame.pack();
			frame.getContentPane().validate();
			frame.getContentPane().repaint();
			frame.setMinimumSize(frame.getSize());		
		}
		return true;
	}

	public static void main(String[] args) throws IOException {
		GUIOptions options = new GUIOptions();
		options.enableAllOptions();
		new IcarusTEGUIMain(options);
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
			if (file.exists() /*&& file.isDirectory()*/) {
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
