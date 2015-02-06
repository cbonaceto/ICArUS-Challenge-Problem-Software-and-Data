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
package org.mitre.icarus.cps.app.widgets.basic.tutorial;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.event.HyperlinkListener;

import org.mitre.icarus.cps.app.widgets.WidgetConstants;
import org.mitre.icarus.cps.app.widgets.phase_2.WidgetConstants_Phase2;
import org.mitre.icarus.cps.app.window.IcarusLookAndFeel;
import org.mitre.icarus.cps.exam.base.loader.IcarusExamLoader;
import org.mitre.icarus.cps.exam.base.training.TutorialNavigationTree;
import org.mitre.icarus.cps.exam.base.training.TutorialTreeNode;
import org.mitre.icarus.cps.exam.base.training.TutorialTreeParentNode;
import org.mitre.icarus.cps.exam.phase_2.IcarusExam_Phase2;
import org.mitre.icarus.cps.exam.phase_2.loader.IcarusExamLoader_Phase2;
import org.mitre.icarus.cps.exam.phase_2.testing.Mission;
import org.mitre.icarus.cps.experiment_core.condition.InstructionsPage;
import org.mitre.icarus.cps.experiment_core.event.SubjectActionEvent;
import org.mitre.icarus.cps.experiment_core.event.SubjectActionListener;
import org.mitre.icarus.cps.experiment_core.gui.InstructionsPanel;
import org.mitre.icarus.cps.experiment_core.gui.IInstructionNavigationPanel;
import org.mitre.icarus.cps.experiment_core.gui.IInstructionsPanel;
import org.mitre.icarus.cps.experiment_core.gui.NavButtonPanel;
import org.mitre.icarus.cps.experiment_core.gui.INavButtonPanel.ButtonType;

/**
 * Contains a nav button panel at the top, a tutorial navigation panel on the side, and a tutorial panel. Automatically
 * handles navigating to the correct tutorial page using the nav buttons and the tutorial navigation panel.
 * Also displays the current tutorial page number and tutorial section name (if available) in the nav button panel.
 * 
 * @author CBONACETO
 *
 */
//TODO: Pre-scale all images to make more responsive
public class TutorialNavigationAndNavButtonPanel extends JPanel implements IInstructionNavigationPanel {
	
	private static final long serialVersionUID = 1844398986230798444L;
	
	/** The ID for the instruction pages */
	protected String instructionsId;

	/** The navigation button panel **/
	protected NavButtonPanel navButtonPanel;
	
	/** The tutorial navigation panel **/
	protected TutorialNavigationPanel tutorialNavPanel;
	
	/** Whether to show the tutorial navigation panel */
	protected boolean showTutorialNavPanel;
	
	/** The panel where tutorial content is displayed */
	protected IInstructionsPanel instructionsPanel;
	
	/** List of all instructions pages in all tutorials in order **/
	protected List<InstructionsPage> instructionsPages;
	
	/** Ordered list containing tutorial names and their first and last page indexes in 
	 * the tutorial pages list */
	protected List<TutorialLocation> tutorialLocationsList;
	
	/** Maps tutorial names to their first and last page indexes in 
	 * the tutorial pages list */	
	protected Map<String, TutorialLocation> tutorialLocationsMap;
	
	/** The current tutorial index (in the tutorialLocationsList) */
	protected int currentTutorialIndex = -1;
	
	/** The current local tutorial page index (out of the current tutorial pages) */
	protected int currentTutorialPageIndex = -1;
	
	/** The current global tutorial page index (out of all the tutorial pages) */
	protected int currentPageIndex = -1;
	
	/** Whether to display the name of the current set tutorial name and the page number */
	protected boolean displayNameAndPageNumber = true;
	
	public TutorialNavigationAndNavButtonPanel() {
		this(true, true, WidgetConstants_Phase2.TUTORIAL_SIZING_TREES, 
				findLongestTutorialName(WidgetConstants_Phase2.TUTORIAL_SIZING_TREES));
	}
	
	/**
	 * @param showNavButtonPanel
	 * @param showTutorialNavigationPanel
	 * @param tutorialNavigationSizingTrees
	 * @param tutorialSizingName
	 */
	public TutorialNavigationAndNavButtonPanel(boolean showNavButtonPanel, boolean showTutorialNavPanel,
			List<TutorialNavigationTree> tutorialNavigationSizingTrees, String tutorialSizingName) {
		this(showNavButtonPanel, showTutorialNavPanel, tutorialNavigationSizingTrees,
				tutorialSizingName, null);
	}
	
	/**
	 * @param showNavButtonPanel
	 * @param showTutorialNavigationPanel
	 * @param tutorialNavigationSizingName
	 * @param instructionsPanel
	 */
	public TutorialNavigationAndNavButtonPanel(boolean showNavButtonPanel, boolean showTutorialNavPanel, 
			List<TutorialNavigationTree> tutorialNavigationSizingTrees, String tutorialSizingName,
			IInstructionsPanel instructionsPanel) {
		super(new GridBagLayout());
		instructionsPages = new LinkedList<InstructionsPage>();
		tutorialLocationsList = new LinkedList<TutorialLocation>();
		tutorialLocationsMap = new HashMap<String, TutorialLocation>();
		if(instructionsPanel == null) {
			instructionsPanel = new InstructionsPanel("instructions");
			instructionsPanel.getComponent().setBackground(WidgetConstants.COLOR_COMPONENT_BACKGROUND);
		}
		this.instructionsPanel = instructionsPanel;
		this.showTutorialNavPanel = showTutorialNavPanel;
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0; 
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 0;		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.CENTER;

		//Add the nav button panel at the top	
		navButtonPanel = new NavButtonPanel(false, true, true, false,
				SwingConstants.RIGHT, SwingConstants.CENTER,
				true, SwingConstants.RIGHT);
		navButtonPanel.setButtonEnabled(ButtonType.Next, false);
		navButtonPanel.setButtonEnabled(ButtonType.Back, false);
		navButtonPanel.setButtonEnabled(ButtonType.Help, false);
		navButtonPanel.setButtonVisible(ButtonType.Help, false);
		navButtonPanel.addSubjectActionListener(new SubjectActionListener() {
			@Override
			public void subjectActionPerformed(SubjectActionEvent event) {
				if(event.eventType == SubjectActionEvent.NEXT_BUTTON_PRESSED) {
					showNextPage();
				} else if(event.eventType == SubjectActionEvent.BACK_BUTTON_PRESSED) {
					showPreviousPage();
				}
			}
		});		
		add(navButtonPanel, gbc);
		gbc.gridy++;
		if(!showNavButtonPanel) {
			navButtonPanel.setVisible(false);
		}
		
		//Add a panel that will contain an optional tutorial navigation panel on the left
		//and the instructions and widget panel on the right
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBackground(Color.WHITE);
		gbc.weighty = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(new JSeparator(JSeparator.HORIZONTAL), gbc);
		gbc.gridy++;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		add(panel, gbc);		
		gbc.gridx = 0; 
		gbc.gridy = 0;		
		
		//Add the tutorial navigation panel on the left		
		gbc.weightx = 0;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.VERTICAL;
		//tutorialNavPanel = new TutorialNavigationPanel(instructionsPanel);
		tutorialNavPanel = new TutorialNavigationPanel(instructionsPanel); //new InstructionsAndWidgetPanel()
		tutorialNavPanel.setFont(WidgetConstants.FONT_INSTRUCTION_PANEL);
		gbc.insets.right = 10;
		panel.add(tutorialNavPanel, gbc);
		gbc.insets.right = 0;
		gbc.gridx = 1;			
		panel.add(new JSeparator(JSeparator.VERTICAL), gbc);
		gbc.gridx = 2;
		//Size the width of the tutorial navigation panel
		if(tutorialNavigationSizingTrees != null && !
				tutorialNavigationSizingTrees.isEmpty()) {
			for(TutorialNavigationTree tutorial : tutorialNavigationSizingTrees) {
				tutorialNavPanel.addTutorial(tutorial.getTutorialName(), null,
						tutorial, true);
			}
			tutorialNavPanel.expandAllTutorials();		
			tutorialNavPanel.setPreferredSize(new Dimension(
					tutorialNavPanel.getPreferredSize().width, 1));
			tutorialNavPanel.removeAllTutorials();
		} else {
			//Use the default width
			tutorialNavPanel.setPreferredSize(new Dimension(200, 1));
		}
		if(navButtonPanel != null && tutorialSizingName != null) {
			navButtonPanel.sizeMessageLabelToMessage(createNameAndPageNumberMessage(
					tutorialSizingName, 99, 99));
		}
		tutorialNavPanel.addTutorialTreeSelectionListener(new TutorialTreeSelectionListener() {
			@Override
			public void tutorialTreeItemSelected(TutorialTreeSelectionEvent event) {
				//Update tutorial name and page number
				TutorialLocation tutorialLocation = tutorialLocationsMap.get(event.tutorialName);
				if(tutorialLocation != null) {
					currentTutorialIndex = tutorialLocation.tutorialIndex;
					currentTutorialPageIndex = event.pageIndex;
					currentPageIndex = event.pageIndex + tutorialLocation.pageStartIndex;
					if(displayNameAndPageNumber) {
						displayNameAndPageNumberMessage(event.tutorialName, 
								currentTutorialPageIndex, tutorialLocation.numPages);
					}
				} else {
					currentTutorialIndex = 0;
					currentTutorialPageIndex = 0;
					currentPageIndex = 0;
					if(displayNameAndPageNumber) {
						displayNameAndPageNumberMessage(" ", 0, 0);
					}
				}
				updateNavigationButtonVisibility();
			}
		});
		if(!showTutorialNavPanel) {
			tutorialNavPanel.setVisible(false);
		}
		
		//Add the instructions and widget panel on the right
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		panel.add(instructionsPanel.getComponent(), gbc);		
	}
	
	/**
	 * @param tutorialNavigationSizingTrees
	 * @return
	 */
	protected static String findLongestTutorialName(List<TutorialNavigationTree> tutorialNavigationSizingTrees) {
		String longestName = null;
		int longestLength = 0;
		if(tutorialNavigationSizingTrees != null && !tutorialNavigationSizingTrees.isEmpty()) {
			for(TutorialNavigationTree tree : tutorialNavigationSizingTrees) {
				if(tree.getTutorialName() != null && tree.getTutorialName().length() > longestLength) {
					longestName = tree.getTutorialName();
					longestLength = tree.getTutorialName().length();
				}
			}
		}
		return longestName;
	}
	
	@Override
	public String getInstructionsId() {
		return instructionsId;
	}

	@Override
	public void setInstructionsId(String instructionsId) {
		this.instructionsId = instructionsId;
	}

	@Override
	public boolean isNavButtonPanelVisible() {
		return navButtonPanel.isVisible();
	}
	
	@Override
	public void setNavButtonPanelVisible(boolean navButtonPanelVisible) {
		if(navButtonPanel.isVisible() != navButtonPanelVisible) {
			navButtonPanel.setVisible(navButtonPanelVisible);
			revalidate();
		}
	}
	
	@Override
	public boolean isTutorialNavPanelVisible() {
		return tutorialNavPanel.isVisible();
	}
	
	@Override
	public void setTutorialNavPanelVisible(boolean tutorialNavPanelVisible) {
		//showTutorialNavPanel = tutorialNavPanelVisible;
		if(tutorialNavPanel.isVisible() != tutorialNavPanelVisible) {
			tutorialNavPanel.setVisible(tutorialNavPanelVisible);
			revalidate();
		}		
	}	
	
	@Override
	public void addHyperlinkListener(HyperlinkListener listener) {
		instructionsPanel.addHyperlinkListener(listener);
	}

	@Override
	public void removeHyperlinkListener(HyperlinkListener listener) {
		instructionsPanel.removeHyperlinkListener(listener);
	}

	@Override
	public boolean isDisplayNameAndPageNumber() {
		return displayNameAndPageNumber;
	}

	@Override
	public void setDisplayNameAndPageNumber(boolean displayNameAndPageNumber) {
		if(this.displayNameAndPageNumber != displayNameAndPageNumber) {
			this.displayNameAndPageNumber = displayNameAndPageNumber;
			if(displayNameAndPageNumber) {
				updateAndDisplayNameAndPageNumber(currentPageIndex);
			} else {
				navButtonPanel.setMessage("");
			}
		}
	}	
	
	@Override
	public void expandNavigationTree() {
		tutorialNavPanel.expandAllTutorials();
	}

	@Override
	public void collapseNavigationTree() {
		tutorialNavPanel.collapseAllTutorials();
	}

	@Override
	public int getCurrentPageIndex() {
		return currentPageIndex;
	}
	
	@Override
	public void addInstructionsPages(String pagesName, List<? extends InstructionsPage> currInstructionsPages) {
		addInstructionsPages(pagesName, currInstructionsPages, null, false);
	}
	
	@Override
	public void addInstructionsPages(String pagesName, 
			List<? extends InstructionsPage> currInstructionsPages,
			TutorialNavigationTree tutorialNavigationTree, 
			boolean showTutorialParentNode) {
		boolean appending = !instructionsPages.isEmpty();
		if(currInstructionsPages != null && !currInstructionsPages.isEmpty()) {
			TutorialLocation tutorialLocation = new TutorialLocation(pagesName,
					tutorialLocationsList.size(), this.instructionsPages.size(), 
					currInstructionsPages.size());
			tutorialLocationsList.add(tutorialLocation);
			tutorialLocationsMap.put(pagesName, tutorialLocation);
			instructionsPages.addAll(currInstructionsPages);
			if(tutorialNavPanel != null) {
				tutorialNavPanel.addTutorial(pagesName, currInstructionsPages,
						tutorialNavigationTree, showTutorialParentNode);
			}
			updateNavigationButtonVisibility();
			
			//Show or hide the tutorial navigation panel
			boolean tutorialNavPanelVisible = showTutorialNavPanel && !tutorialNavPanel.isTutorialTreeEmpty(); 
			if(tutorialNavPanel.isVisible() != tutorialNavPanelVisible) {
				tutorialNavPanel.setVisible(tutorialNavPanelVisible);
				revalidate();
			}
			tutorialNavPanel.expandTutorial(pagesName);
			
			//Show the first page
			if(currentPageIndex == -1 || !appending) {
				showFirstPage();
			}
		} else {
			updateNavigationButtonVisibility();
		}
	}	

	@Override
	public void setInstructionsPages(String pagesName, List<? extends InstructionsPage> currInstructionsPages) {
		setInstructionsPages(pagesName, currInstructionsPages, null, false);
	}
	
	@Override
	public void setInstructionsPages(String pagesName,
			List<? extends InstructionsPage> currInstructionsPages,
			TutorialNavigationTree navigationTree, boolean showNameNode) {
		clearAllTutorials();
		addInstructionsPages(pagesName, currInstructionsPages, navigationTree, showNameNode);
	}
	
	/**
	 * @param pagesName
	 */
	public void removeInstructionsPages(String pagesName) {
		if(tutorialLocationsMap.containsKey(pagesName)) {
			TutorialLocation tutorialLocation = tutorialLocationsMap.get(pagesName);
			if(tutorialLocation != null) {
				int tutorialIndex = tutorialLocation.tutorialIndex;
				int pageIndex = tutorialLocation.pageStartIndex;
				int numPages = tutorialLocation.numPages;
				if(pageIndex < instructionsPages.size()) {
					//Remove the tutorial pages for the tutorial
					for(int i=0; i<numPages; i++) {
						instructionsPages.remove(pageIndex);
					}					
					//Adjust the locations of the remaining tutorials after the removed tutorial (if any)
					for(int currTutorialIndex = tutorialIndex + 1; 
							currTutorialIndex < tutorialLocationsList.size(); currTutorialIndex++) {
						tutorialLocation = tutorialLocationsList.get(currTutorialIndex);
						tutorialLocation.tutorialIndex = currTutorialIndex - 1;
						tutorialLocation.pageStartIndex = tutorialLocation.pageStartIndex - numPages;
						tutorialLocation.pageEndIndex = tutorialLocation.pageEndIndex - numPages;
					}										
					//Remove the tutorial location from the tutorial locations list and map
					tutorialLocationsList.remove(tutorialIndex);
					tutorialLocationsMap.remove(pagesName);
				}
				
			}
		}
		if(tutorialNavPanel != null) {
			tutorialNavPanel.removeTutorial(pagesName);
		}
		updateNavigationButtonVisibility();
		showFirstPage();
	}	
	
	@Override
	public void removeAllInstructionsPages() {
		clearAllTutorials();
		updateNavigationButtonVisibility();
		showFirstPage();		
	}
	
	
	
	protected void clearAllTutorials() {
		tutorialLocationsList.clear();
		tutorialLocationsMap.clear();
		instructionsPages.clear();
		if(tutorialNavPanel != null) {
			tutorialNavPanel.removeAllTutorials();
		}
	}

	protected void updateNavigationButtonVisibility() {
		if(navButtonPanel != null) {
			if(instructionsPages == null || instructionsPages.size() < 2) {
				if(navButtonPanel.isVisible()) {
					navButtonPanel.setVisible(false);
					revalidate();
				}
				navButtonPanel.setButtonVisible(ButtonType.Back, false);
				navButtonPanel.setButtonVisible(ButtonType.Next, false);
			} else {
				if(!navButtonPanel.isVisible()) {
					navButtonPanel.setVisible(true);
					revalidate();
				}
				navButtonPanel.setButtonVisible(ButtonType.Back, true);
				navButtonPanel.setButtonVisible(ButtonType.Next, true);
				navButtonPanel.setButtonEnabled(ButtonType.Back, 
						(currentPageIndex > 0));
				navButtonPanel.setButtonEnabled(ButtonType.Next, 
						(currentPageIndex < instructionsPages.size()-1));
			}
		}
	}
	
	@Override
	public void showFirstPage() {
		currentPageIndex = -1;
		showNextPage();
	}
	
	public void showPage(String pagesName, int localPageIndex) {
		TutorialLocation tutorialLocation = tutorialLocationsMap.get(pagesName);
		if(tutorialLocation != null) {
			showPage(localPageIndex + tutorialLocation.pageStartIndex);
		}
	}	
	
	@Override
	public void showPage(int globalPageIndex) {
		if(globalPageIndex < 0 || instructionsPages == null || 
				globalPageIndex >= instructionsPages.size()) {
			return;
		}
		currentPageIndex = globalPageIndex - 1;
		showNextPage();
	}	
	
	@Override
	public void showNextPage() {
		if(instructionsPages == null || instructionsPages.isEmpty()) {
			instructionsPanel.setInstructionsPage(null);
			if(navButtonPanel != null) {
				navButtonPanel.setButtonEnabled(ButtonType.Back, false);
				navButtonPanel.setButtonEnabled(ButtonType.Next, false);
			}
		}
		else {
			currentPageIndex++;
			if(currentPageIndex < 0) {
				currentPageIndex = 0;
			} else if(currentPageIndex >= instructionsPages.size()) {
				currentPageIndex = instructionsPages.size() - 1;
			}
			if(displayNameAndPageNumber) {
				updateAndDisplayNameAndPageNumber(currentPageIndex);
			}
			instructionsPanel.setInstructionsPage(
					(InstructionsPage)instructionsPages.get(currentPageIndex));
			if(navButtonPanel != null) {
				navButtonPanel.setButtonEnabled(ButtonType.Back, 
						(currentPageIndex > 0));
				navButtonPanel.setButtonEnabled(ButtonType.Next, 
						(currentPageIndex < instructionsPages.size()-1));
			}
		}
	}
	
	@Override
	public void showPreviousPage() {
		if(instructionsPages == null || instructionsPages.isEmpty()) {
			instructionsPanel.setInstructionsPage(null);
			if(navButtonPanel != null) {
				navButtonPanel.setButtonEnabled(ButtonType.Back, false);
				navButtonPanel.setButtonEnabled(ButtonType.Next, false);
			}
		}
		else {
			currentPageIndex--;
			if(currentPageIndex < 0) {
				currentPageIndex = 0;
			}
			if(displayNameAndPageNumber) {
				updateAndDisplayNameAndPageNumber(currentPageIndex);
			}
			instructionsPanel.setInstructionsPage(
					(InstructionsPage)instructionsPages.get(currentPageIndex));
			if(navButtonPanel != null) {
				navButtonPanel.setButtonEnabled(ButtonType.Back,
						(currentPageIndex > 0));
				navButtonPanel.setButtonEnabled(ButtonType.Next, 
						(currentPageIndex  <instructionsPages.size()-1));
			}
		}
	}	
	
	protected void updateAndDisplayNameAndPageNumber(int globalPageIndex) {
		boolean tutorialFound = false;
		if(instructionsPages != null && globalPageIndex >= 0 && globalPageIndex < instructionsPages.size()) {
			TutorialLocation tutorialLocation = null;			
			Iterator<TutorialLocation> iter = tutorialLocationsList.iterator();
			int globalTutorialStartIndex = 0;
			while(iter.hasNext() && !tutorialFound) {
				tutorialLocation = iter.next();
				int tutorialPageIndex = globalPageIndex - globalTutorialStartIndex;
				if(tutorialPageIndex >= 0 && tutorialPageIndex < tutorialLocation.numPages) {
					tutorialFound = true;
					currentTutorialIndex = tutorialLocation.tutorialIndex;
					currentTutorialPageIndex = tutorialPageIndex;
					currentPageIndex = globalPageIndex;
					displayNameAndPageNumberMessage(tutorialLocation.tutorialName,
							tutorialPageIndex, tutorialLocation.numPages);
				} else {
					globalTutorialStartIndex += tutorialLocation.numPages;
				}
			}
			
		} 
		if(!tutorialFound) {
			currentTutorialIndex = 0;
			currentTutorialPageIndex = 0;
			displayNameAndPageNumberMessage(" ", 0, 0);
		}
	}
	
	protected void displayNameAndPageNumberMessage(String pagesName, int localPageIndex, int numPages) {
		navButtonPanel.setMessage(createNameAndPageNumberMessage(pagesName, localPageIndex, numPages));
	}
	
	protected String createNameAndPageNumberMessage(String pagesName, int localPageIndex, int numPages) {
		StringBuilder sb = new StringBuilder(pagesName + ": ");
		if(localPageIndex + 1 < 10) {
			sb.append(" ");
		}
		sb.append(localPageIndex + 1);
		sb.append("/");
		sb.append(numPages);
		return sb.toString();
	}
	
	@Override
	public Dimension getInstructionsPanelPreferredSize() {
		return instructionsPanel != null ? instructionsPanel.getComponent().getPreferredSize() : null;
	}
	
	@Override
	public void setInstructionsPanelPreferredSize(Dimension size) {
		if(instructionsPanel != null) {
			instructionsPanel.getComponent().setPreferredSize(size);
			revalidate();
		}
	}
	
	@Override
	public void setMessageLabelFont(Font font) {
		if(navButtonPanel != null) {
			navButtonPanel.setMessageLabelFont(font);
		}
	}	
	
	@Override
	public void setInstructionsPanelFont(Font font) {
		if(instructionsPanel != null) {
			instructionsPanel.getComponent().setFont(font);
		}
	}
	
	public void setTutorialNavigationPanelFont(Font font) {
		if(tutorialNavPanel != null) {
			tutorialNavPanel.setFont(font);
		}
	}
	
	@Override
	public JComponent getComponent() {
		return this;
	}

	/** Test main */
	public static void main(String[] args) {
		IcarusLookAndFeel.initializeICArUSLookAndFeel();
		JFrame frame = new JFrame("Test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		try {
			//Load the Phase 2 sample exam tutorial
			URL examUrl = new File("data/Phase_2_CPD/exams/Sample-Exam-1/Sample-Exam-1.xml").toURI().toURL();
			IcarusExam_Phase2 exam = IcarusExamLoader_Phase2.examLoaderInstance.unmarshalExamFromXml(
					examUrl, false);
			if(exam.getTutorial() != null) {
				IcarusExamLoader.initializeTutorialPhase(exam.getTutorial(), examUrl);
			}

			//Load the Mission tutorials
			if(exam.getMissions() != null) {
				for(Mission<?> mission : exam.getMissions()) {
					IcarusExamLoader.initializeExamPhaseTutorial(mission, examUrl);
				}
			}				

			//Create the tutorial navigation and nav button panel
			TutorialNavigationAndNavButtonPanel navPanel = 
					new TutorialNavigationAndNavButtonPanel(true, true, WidgetConstants_Phase2.TUTORIAL_SIZING_TREES,
							"Mission 6 Instructions");
			navPanel.setTutorialNavigationPanelFont(WidgetConstants.FONT_INSTRUCTION_PANEL);
			navPanel.setMessageLabelFont(WidgetConstants.FONT_STATUS);
			
			//Create the exam tutorial navigation tree
			if(exam.getTutorial() != null) {
				TutorialNavigationTree examTutorial = new TutorialNavigationTree("Exam Tutorial",
						Collections.unmodifiableList(Arrays.asList(
								new TutorialTreeParentNode(
										"Exam Tutorial", "Overview of the Task", 0),
								new TutorialTreeParentNode(
										"Exam Tutorial", "Scoring and Tactics", 8,
									Collections.unmodifiableList(Arrays.asList(
										new TutorialTreeNode("Exam Tutorial", "Showdown", 9),
										new TutorialTreeNode("Exam Tutorial", "Scoring", 10),
										new TutorialTreeNode("Exam Tutorial", "Tactics", 11)))),
								new  TutorialTreeParentNode(
									"Exam Tutorial", "Intelligence Reports", 12,
									Collections.unmodifiableList(Arrays.asList(
										new TutorialTreeNode("Exam Tutorial", "OSINT", 14),
										new TutorialTreeNode("Exam Tutorial", "IMINT", 16),
										new TutorialTreeNode("Exam Tutorial", "BLUEBOOK", 18),
										new TutorialTreeNode("Exam Tutorial", "HUMINT", 20),
										new TutorialTreeNode("Exam Tutorial", "SIGINT", 22),
										new TutorialTreeNode("Exam Tutorial", "Notation for P(Attack|INTS)", 24)))))));
				navPanel.addInstructionsPages("Exam Tutorial", exam.getTutorial().getTutorialPages(), 
						examTutorial, true);	
				//exam.getTutorial().setTutorialNavigationTree(examTutorial);
				//System.out.println(IcarusExamLoader_Phase2.marshalExam(exam));
			}			
			//Create the mission tutorial navigation trees
			if(exam.getMissions() != null) {
				int i = 1;
				for(Mission<?> mission : exam.getMissions()) {
					if(mission.getInstructionPages() != null) {
						navPanel.addInstructionsPages("Mission " + Integer.toString(i) + " Instructions", 
								mission.getInstructionPages(), null, true);
					}
					i++;
				}
			}
			navPanel.tutorialNavPanel.expandAllTutorials();
			navPanel.setInstructionsPanelPreferredSize(new Dimension(840, 650));
			navPanel.tutorialNavPanel.setTutorialEnabled("Exam Tutorial", true);
			//navPanel.setTutorialSectionEnabled(node, false);
			frame.getContentPane().add(navPanel);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		frame.pack();
		frame.setVisible(true);
	}
	
	protected static class TutorialLocation {
		
		public String tutorialName;
		
		public int tutorialIndex;
		
		public int pageStartIndex;
		
		public int numPages;
		
		public int pageEndIndex;		
		
		public TutorialLocation(String tutorialName, int tutorialIndex, int pageStartIndex, int numPages) {
			this.tutorialName = tutorialName;
			this.tutorialIndex = tutorialIndex;
			this.pageStartIndex = pageStartIndex;
			this.numPages = numPages;
			pageEndIndex = pageStartIndex + numPages - 1; 
		}		
	}
}