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
package org.mitre.icarus.cps.experiment_core.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.event.HyperlinkListener;

import org.mitre.icarus.cps.app.widgets.WidgetConstants;
import org.mitre.icarus.cps.exam.base.training.TutorialNavigationTree;
import org.mitre.icarus.cps.experiment_core.condition.InstructionsPage;
import org.mitre.icarus.cps.experiment_core.event.SubjectActionEvent;
import org.mitre.icarus.cps.experiment_core.event.SubjectActionListener;
import org.mitre.icarus.cps.experiment_core.gui.INavButtonPanel.ButtonType;

/**
 * A panel with instructions pages and buttons to navigate the pages.
 * This panel is used in an external instructions window.
 * 
 * @author CBONACETO
 *
 */
public class InstructionNavigationPanel extends JPanel implements IInstructionNavigationPanel {	
	private static final long serialVersionUID = 1L;
	
	/** The ID for the instruction pages */
	protected String instructionsId;

	/** The navigation button panel */
	protected NavButtonPanel navButtonPanel;
	
	/** The instructions panel where the instructions are displayed */
	protected IInstructionsPanel instructionsPanel;	
	
	/** The instructions pages */
	protected List<InstructionsPage> instructionsPages;
	
	/** The name of the set of instructions */
	protected String pagesName;
	
	/** Whether to display the name of the current set of instructions name and the page number */
	protected boolean displayNameAndPageNumber = true; 
	
	/** Current instructions page index */
	int currentPageIndex = -1;	
	
	public InstructionNavigationPanel() {
		this(true, new InstructionsPanel("instructions"));
	}
	
	public InstructionNavigationPanel(boolean showNavButtonPanel, IInstructionsPanel instructionsPanel) {
		super(new GridBagLayout());
		instructionsPages = new LinkedList<InstructionsPage>();

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0; 
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 0;		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.CENTER;

		//Add the nav button panel
		navButtonPanel = new NavButtonPanel();
		navButtonPanel.setButtonEnabled(ButtonType.Next, false);
		navButtonPanel.setButtonEnabled(ButtonType.Back, false);
		navButtonPanel.setButtonEnabled(ButtonType.Help, false);
		navButtonPanel.setButtonVisible(ButtonType.Help, false);
		navButtonPanel.addSubjectActionListener(new SubjectActionListener() {
			@Override
			public void subjectActionPerformed(SubjectActionEvent event) {
				if(event.eventType == SubjectActionEvent.NEXT_BUTTON_PRESSED) {
					showNextPage();
				}
				else if(event.eventType == SubjectActionEvent.BACK_BUTTON_PRESSED) {
					showPreviousPage();
				}
			}
		});		
		add(navButtonPanel, gbc);
		gbc.gridy++;
		//gbc.weighty = 1;
		if(!showNavButtonPanel) {
			navButtonPanel.setVisible(false);
		}

		//Add the instructions panel
		if(instructionsPanel == null) {
			instructionsPanel = new InstructionsPanel("instructions");
		}
		this.instructionsPanel = instructionsPanel;
		this.instructionsPanel.getComponent().setBackground(WidgetConstants.COLOR_COMPONENT_BACKGROUND);
		gbc.gridy++;
		gbc.weighty = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(new JSeparator(JSeparator.HORIZONTAL), gbc);

		gbc.gridy++;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		add(instructionsPanel.getComponent(), gbc);
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
		//Always returns false
		return false;
	}

	@Override
	public void setTutorialNavPanelVisible(boolean tutorialNavPanelVisible) {
		//Does nothing
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
				displayNameAndPageNumberMessage(pagesName, currentPageIndex,
						instructionsPages != null ? instructionsPages.size() : 0);
			} else {
				navButtonPanel.setMessage("");
			}
		}
	}
	
	@Override
	public void expandNavigationTree() {
		//Does Nothing
	}

	@Override
	public void collapseNavigationTree() {
		//Does Nothing
	}

	@Override
	public int getCurrentPageIndex() {
		return currentPageIndex;
	}
	
	public boolean containsInstructionsPage(InstructionsPage page) {
		return instructionsPages.contains(page);
	}
	
	public List<? extends InstructionsPage> getInstructionsPages() {
		return instructionsPages;
	}
	
	public void addInstructionsPage(InstructionsPage page) {
		if(page != null) {
			boolean appending = !instructionsPages.isEmpty();
			instructionsPages.add(page);
			updateNavigationButtonVisibility();		
			
			//Show the first page
			if(currentPageIndex == -1 || !appending) {
				showFirstPage();
			}
		}
	}
	
	@Override
	public void addInstructionsPages(String pagesName, List<? extends InstructionsPage> currInstructionsPages) {
		addInstructionsPages(pagesName, currInstructionsPages, null, false);
	}

	@Override
	public void addInstructionsPages(String pagesName, List<? extends InstructionsPage> currInstructionsPages,
			TutorialNavigationTree navigationTree, boolean showNameNode) {
		boolean appending = !instructionsPages.isEmpty();
		this.pagesName = pagesName;
		if(currInstructionsPages != null && !currInstructionsPages.isEmpty()) {			
			instructionsPages.addAll(currInstructionsPages);
			updateNavigationButtonVisibility();
			
			//Show the first page
			if(currentPageIndex == -1 || !appending) {
				//System.out.println("showing first page");
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
		instructionsPages.clear();
		addInstructionsPages(pagesName, currInstructionsPages, navigationTree, showNameNode);
	}

	public void removeInstructionsPage(InstructionsPage page) {
		if(instructionsPages != null && !instructionsPages.isEmpty()) {
			if(instructionsPages.remove(page)) {
				updateNavigationButtonVisibility();
				showPreviousPage();
			}
		}
	}
	
	@Override
	public void removeAllInstructionsPages() {
		instructionsPages.clear();
		updateNavigationButtonVisibility();
		showFirstPage();
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
	
	public boolean showPage(InstructionsPage page) {
		int pageIndex = instructionsPages.indexOf(page);
		if(pageIndex > -1 && pageIndex < instructionsPages.size()) {
			showPage(pageIndex);
			return true;
		}
		return false;
	}
	
	@Override
	public void showPage(int pageIndex) {
		if(pageIndex < 0 || instructionsPages == null || pageIndex >= instructionsPages.size()) {
			return;
		}
		currentPageIndex = pageIndex - 1;
		showNextPage();
	}
	
	@Override
	public void showNextPage() {
		if(instructionsPages == null || instructionsPages.isEmpty()) {
			instructionsPanel.setInstructionsPage(null);			
			navButtonPanel.setButtonEnabled(ButtonType.Back, false);
			navButtonPanel.setButtonEnabled(ButtonType.Next, false);
		}
		else {
			currentPageIndex++;
			if(currentPageIndex >= instructionsPages.size()) {
				currentPageIndex = instructionsPages.size()-1;
			}
			if(displayNameAndPageNumber) {
				displayNameAndPageNumberMessage(pagesName, currentPageIndex,
						instructionsPages != null ? instructionsPages.size() : 0);
			}
			instructionsPanel.setInstructionsPage((InstructionsPage)instructionsPages.get(currentPageIndex));
			navButtonPanel.setButtonEnabled(ButtonType.Back, (currentPageIndex > 0));
			navButtonPanel.setButtonEnabled(ButtonType.Next, 
					(currentPageIndex < instructionsPages.size()-1));
		}
	}
	
	@Override
	public void showPreviousPage() {
		if(instructionsPages == null || instructionsPages.isEmpty()) {
			instructionsPanel.setInstructionsPage(null);
			navButtonPanel.setButtonEnabled(ButtonType.Back, false);
			navButtonPanel.setButtonEnabled(ButtonType.Next, false);
		}
		else {
			currentPageIndex--;
			if(currentPageIndex < 0) {
				currentPageIndex = 0;
			}
			if(displayNameAndPageNumber) {
				displayNameAndPageNumberMessage(pagesName, currentPageIndex,
						instructionsPages != null ? instructionsPages.size() : 0);
			}
			instructionsPanel.setInstructionsPage((InstructionsPage)instructionsPages.get(currentPageIndex));
			navButtonPanel.setButtonEnabled(ButtonType.Back, (currentPageIndex > 0));
			navButtonPanel.setButtonEnabled(ButtonType.Next, 
					(currentPageIndex < instructionsPages.size()-1));
		}
	}
	
	protected void displayNameAndPageNumberMessage(String name, int pageIndex, int numPages) {
		navButtonPanel.setMessage(createNameAndPageNumberMessage(name, pageIndex, numPages));
	}
	
	protected String createNameAndPageNumberMessage(String name, int pageIndex, int numPages) {
		StringBuilder sb = new StringBuilder(name != null ? (name + ": ") : "Page: ");
		if(pageIndex + 1 < 10) {
			sb.append(" ");
		}
		sb.append(pageIndex + 1);
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
	
	@Override
	public JComponent getComponent() {
		return this;
	}
	
	/** Test main */
	public static void main(String[] args) {
		JFrame frame = new JFrame("Test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		InstructionNavigationPanel panel = new InstructionNavigationPanel();
		panel.setPreferredSize(new Dimension(500, 640));
		frame.getContentPane().add(panel);
		frame.pack();
		frame.setVisible(true);
	}	
}