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
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.mitre.icarus.cps.app.util.CPSUtils;
import org.mitre.icarus.cps.app.widgets.WidgetConstants;
import org.mitre.icarus.cps.app.widgets.dialog.ErrorDlg;
import org.mitre.icarus.cps.app.widgets.dialog.ValidationErrorsDlg;
import org.mitre.icarus.cps.app.widgets.map.phase_05.FeatureVector;
import org.mitre.icarus.cps.app.widgets.map.phase_05.FeatureVectorPanel;
import org.mitre.icarus.cps.app.widgets.phase_05.dialog.FeatureVectorSelectionDlg;
import org.mitre.icarus.cps.app.widgets.phase_05.dialog.FeatureVectorSelectionDlg.FeatureVectorFiles;
import org.mitre.icarus.cps.exam.phase_05.loader.IcarusExamLoader_Phase05;
import org.mitre.icarus.cps.feature_vector.phase_05.FeatureVectorManager;

/**
 * Main entry point for the scaled-down GUI delivered to performers with options
 * to open a feature vector file and validate an exam XML file for the Phase 05
 * Challenge Problem Format (Facility Identification).
 * 
 * @author CBONACETO
 *
 */
public class IcarusDevToolGUIMain {
	
	/** Application frame */
	protected JFrame frame;

	/** File chooser for opening exam files */
	protected static JFileChooser examFileChooser;
	
	/** Panel for showing a single feature vector */
	protected JPanel imagePanelContainer;
	protected FeatureVectorPanel imagePanel;

	/** Current feature vector file (when viewing a single feature vector) */
	protected File currentFeatureVectorFile;

	/** Current object palette file (when viewing a single feature vector) */
	protected File currentObjectPaletteFile;
	
	/**
	 * Default constructor
	 */
	public IcarusDevToolGUIMain() {	
		//Create GUI in the event dispatch thread
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {		
				frame = new JFrame("ICArUS Test and Evaluation GUI");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
				initializeFrame();
			}
		});	
	}
	
	protected void initializeFrame() {
		frame.setResizable(true);
		frame.setVisible(false);
		frame.getContentPane().removeAll();

		//Create menu bar with options menu
		frame.setJMenuBar(createMenuBar());

		/*frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {				
				confirmExit();
			}
		});*/

		//Show blank start-up screen
		JPanel blankPanel = new JPanel();
		blankPanel.setPreferredSize(new Dimension(580, 520));
		frame.getContentPane().removeAll();
		frame.getContentPane().add(blankPanel);
		frame.pack();

		CPSUtils.centerFrameOnScreen(frame);
		frame.setVisible(true);
	}

	/** Create a menu bar with the options menu */
	protected JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		// Define and add the options menu to the menu bar
		JMenu optionsMenu = new JMenu("Options");
		optionsMenu.setMnemonic(KeyEvent.VK_O);
		menuBar.add(optionsMenu);

		// Create open feature vector file menu item and add to options menu
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
					openFeatureVector();
				}				            	
			}
		});  

		optionsMenu.addSeparator();

		//Create validate exam file menu item		
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

		//Create validate response file menu item
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

		optionsMenu.addSeparator();		

		//Create exit menu item and add to options menu    	
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.setMnemonic(KeyEvent.VK_X);
		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//confirmExit();
				System.exit(0);
			}
		});
		optionsMenu.add(exitItem);

		return menuBar;
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
	
	/** 
	 * Confirm whether or not to exit the application
	 */
	public void confirmExit() {
		if(JOptionPane.showConfirmDialog(frame, "Would you like to exit the application?", "", 
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}
	
	public static void main(String[] args) throws IOException {
		/*GUIOptions options = new GUIOptions();
		options.setOptionEnabled(OptionType.Open_Feature_Vector);
		options.setOptionEnabled(OptionType.Validate_Exam);
		options.setOptionEnabled(OptionType.Validate_Response);
		new IcarusTEGUIMain(options);*/
		new IcarusDevToolGUIMain();
	}
}
