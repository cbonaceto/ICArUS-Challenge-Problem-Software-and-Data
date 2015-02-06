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
import java.util.List;

import javax.swing.JComponent;
import javax.swing.event.HyperlinkListener;

import org.mitre.icarus.cps.exam.base.training.TutorialNavigationTree;
import org.mitre.icarus.cps.experiment_core.condition.InstructionsPage;

public interface IInstructionNavigationPanel {
	
	public String getInstructionsId();
	public void setInstructionsId(String instructionsId);
	
	public boolean isNavButtonPanelVisible();
	public void setNavButtonPanelVisible(boolean navButtonPanelVisible);
	
	public boolean isTutorialNavPanelVisible();		
	public void setTutorialNavPanelVisible(boolean tutorialNavPanelVisible);
	
	/** Add a hyperlink listener */
	public void addHyperlinkListener(HyperlinkListener listener);
	
	/** Remove a hyperlink listener */
	public void removeHyperlinkListener(HyperlinkListener listener);

	public boolean isDisplayNameAndPageNumber();
	public void setDisplayNameAndPageNumber(boolean displayNameAndPageNumber);
	
	public void expandNavigationTree();
	public void collapseNavigationTree();
	
	public void addInstructionsPages(String pagesName, List<? extends InstructionsPage> instructionsPages);
	public void addInstructionsPages(String pagesName, List<? extends InstructionsPage> instructionsPages,
			TutorialNavigationTree navigationTree, boolean showNameNode);
	public void setInstructionsPages(String pagesName, List<? extends InstructionsPage> instructionsPages);
	public void setInstructionsPages(String pagesName, List<? extends InstructionsPage> instructionsPages,
			TutorialNavigationTree navigationTree, boolean showNameNode);	
	//public void removeInstructionsPage(InstructionsPage page);
	public void removeAllInstructionsPages();
	
	public int getCurrentPageIndex();
	
	public void showFirstPage();
	public void showPage(int pageIndex);
	public void showNextPage();
	public void showPreviousPage();
	
	public Dimension getInstructionsPanelPreferredSize();
	public void setInstructionsPanelPreferredSize(Dimension size);
	
	public void setMessageLabelFont(Font font);
	
	public void setInstructionsPanelFont(Font font);	
	
	public JComponent getComponent();
}