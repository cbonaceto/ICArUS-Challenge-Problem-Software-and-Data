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
package org.mitre.icarus.cps.app.widgets.ui_study;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org.mitre.icarus.cps.app.experiment.ui_study.exam.UIStudyExam;
import org.mitre.icarus.cps.app.experiment.ui_study.exam.UIStudyPhase.DistributionType;
import org.mitre.icarus.cps.app.experiment.ui_study.exam.UIStudyPhase.ModalityType;
import org.mitre.icarus.cps.app.experiment.ui_study.exam.UIStudyPhase.NormalizationMode;
import org.mitre.icarus.cps.app.experiment.ui_study.exam.UIStudyPhase.WidgetType;
import org.mitre.icarus.cps.app.widgets.WidgetConstants;
import org.mitre.icarus.cps.app.widgets.basic.BreakPanel;
import org.mitre.icarus.cps.experiment_core.gui.ConditionPanel;
import org.mitre.icarus.cps.experiment_core.gui.IConditionComponent;
import org.mitre.icarus.cps.experiment_core.gui.JPanelConditionComponent;

/**
 * @author CBONACETO
 *
 */
public class ConditionPanel_UIStudy extends ConditionPanel {
	private static final long serialVersionUID = 1L;
	
	/** The current exam */
	protected UIStudyExam currentExam;
	
	/** Content panel contains all sub-panels */
	private JPanelConditionComponent contentPanel;	
	
	/** Panel containing hits panel or percent panel */
	protected JPanel probabilityDistributionPanel;
	
	/** Panel containing probability entry widgets */
	protected JPanel probabilityEntryPanelContainer;
	protected ProbabilityEntryPanel probabilityEntryPanel;	
	
	/** Panel containing hit panels for spatial presentation trials */
	protected HitsPanel hitsPanel;
	
	/** Panel containing percent panels for percentages presentation trial */
	protected PercentsPanel percentsPanel;
	
	/** Break screen panel */
	private BreakPanel breakPanel;	

	public ConditionPanel_UIStudy(Component parent) {
		this(parent, false, WidgetConstants.BANNER_ORIENTATION);
	}
	
	public ConditionPanel_UIStudy(Component parent, boolean showInstructionBanner, BannerOrientation bannerOrientation) {		
		super(showInstructionBanner, bannerOrientation);
		
		breakPanel = new BreakPanel();

		contentPanel = new JPanelConditionComponent("content");
		contentPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0; 
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;		
		
		//Add the probability distribution panel to the content panel
		probabilityDistributionPanel = new JPanel(new GridBagLayout());		
		probabilityDistributionPanel.setBorder(WidgetConstants.DEFAULT_BORDER);
		contentPanel.add(probabilityDistributionPanel, gbc);
		
		//Add the hits panel to the probability distribution panel
		hitsPanel = new HitsPanel(4, 2);
		hitsPanel.setVisible(false);
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.CENTER; //EAST
		probabilityDistributionPanel.add(hitsPanel, gbc);
		
		//Add the percents panel to the probability distribution panel
		percentsPanel = new PercentsPanel(4, 4);
		percentsPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		percentsPanel.setVisible(false);
		gbc.anchor = GridBagConstraints.EAST;
		probabilityDistributionPanel.add(percentsPanel, gbc);
		
		setProbabilityDistrubutionType(DistributionType.Spatial);
		//probabilityDistributionPanel.setPreferredSize(new Dimension(480, 520));
		probabilityDistributionPanel.setMinimumSize(probabilityDistributionPanel.getPreferredSize());
		
		//Add the probability entry panel to the content panel
		probabilityEntryPanelContainer = new JPanel(new GridBagLayout());
		probabilityEntryPanelContainer.setBorder(WidgetConstants.DEFAULT_BORDER);
		//GridBagConstraints probabilityPanelConstraints = new GridBagConstraints();
		probabilityEntryPanel = new ProbabilityEntryPanel(new ArrayList<String>(Arrays.asList("1", "2", "3", "4")), 
				"your percentages", true);
		probabilityEntryPanel.setSumVisible(true);
		//probabilityEntryPanel.setBackground(Color.blue);
		//probabilityEntryPanel.setBorder(GUIConstants.DEFAULT_BORDER);
		probabilityEntryPanelContainer.add(probabilityEntryPanel);
		gbc.gridx++;
		gbc.weightx = 0;
		//gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.BOTH;		
		//contentPanel.add(probabilityEntryPanel, gbc);
		contentPanel.add(probabilityEntryPanelContainer, gbc);
		
		contentPanel.setPreferredSize(computeMaxSize());		
		setConditionComponents(Collections.singleton((IConditionComponent) contentPanel));
		showBlankPage();
	}	

	public void updatePreferredSize() {		
		contentPanel.setPreferredSize(null);
		contentPanel.setPreferredSize(computeMaxSize());
		setConditionComponents(Collections.singleton((IConditionComponent) contentPanel));
	}

	/** Compute the maximum preferred size of the panel */
	private Dimension computeMaxSize() {
		ArrayList<String> titles = new ArrayList<String>(Arrays.asList(" 1 ", " 2 ", " 3 ", " 4 "));
		
		//Show the hits panel		
		setProbabilityDistrubutionType(DistributionType.Spatial);
	
		//Compute the size of the largest probability entry container
		Dimension maxPanelSize = new Dimension(1, 1);
		probabilityEntryPanelContainer.setPreferredSize(null);
		//probabilityEntryPanel.setPreferredSize(null);	
		probabilityEntryPanel.setProbabilityEntryTitles(titles);
		for(WidgetType widgetType : WidgetType.values()) {
			probabilityEntryPanel.setProbabilityEntryType(widgetType, ModalityType.Mouse, NormalizationMode.DynamicLocking, null);
			Dimension size = probabilityEntryPanel.getPreferredSize();
			if(size.width > maxPanelSize.width) {
				maxPanelSize.width = size.width;
			}
			if(size.height > maxPanelSize.height) {
				maxPanelSize.height = size.height;
			}
		}
		probabilityEntryPanelContainer.setPreferredSize(new Dimension(
				maxPanelSize.width + probabilityEntryPanelContainer.getInsets().left + probabilityEntryPanelContainer.getInsets().right, 
				maxPanelSize.height + probabilityEntryPanelContainer.getInsets().top + probabilityEntryPanelContainer.getInsets().bottom));
		//System.out.println("Probability entry panel computed size: " + probabilityEntryPanelContainer.getPreferredSize());
		//probabilityEntryPanel.setPreferredSize(maxPanelSize);		
		
		Dimension maxSize = contentPanel.getPreferredSize();
		return new Dimension(maxSize.width + 1, maxSize.height + 1);
	}
	
	public void showTaskScreen() {
		setConditionComponent(contentPanel);
	}
	
	public ProbabilityEntryPanel getProbabilityEntryPanel() {
		return probabilityEntryPanel;
	}

	public HitsPanel getHitsPanel() {
		return hitsPanel;
	}

	public PercentsPanel getPercentsPanel() {
		return percentsPanel;
	}
	
	public void setProbabilityDistrubutionType(DistributionType distributionType) {
		switch(distributionType) {
		case Spatial:
			if(!hitsPanel.isVisible()) {
				hitsPanel.setVisible(true);
				percentsPanel.setVisible(false);
				revalidate();
			}
			break;
		case Percent:
			if(!percentsPanel.isVisible()) {
				hitsPanel.setVisible(false);
				percentsPanel.setVisible(true);
				revalidate();
			}
			break;
		}
	}
	
	public void setProbabilityEntryType(WidgetType widgetType, ModalityType modalityType, 
			NormalizationMode normalizationType, Double looseNormalizationErrorThreshold) {
		probabilityEntryPanel.setProbabilityEntryType(widgetType, modalityType, normalizationType, looseNormalizationErrorThreshold);
	}

	public void setProbabilityDistributionPanelVisible(boolean visible) {
		if(visible != probabilityDistributionPanel.isVisible()) {
			probabilityDistributionPanel.setVisible(visible);
			contentPanel.revalidate();
		}
	}
	
	public void setProbabilityEntryPanelVisible(boolean visible) {
		if(visible != probabilityEntryPanel.isVisible()) {
			probabilityEntryPanel.setVisible(visible);
			contentPanel.revalidate();
		}
	}

	/** 
	 * Show the break screen 
	 */
	public void showBreakScreen(boolean showCountdown) {
		setInstructionBannerText("Please take a short break.  You may click next to continue when the break is over.");
		breakPanel.setCountdownVisible(showCountdown);
		setConditionComponent(breakPanel);
	}	
	
	/** 
	 * Update the break screen
	 */
	public void setBreakTimeRemaining(long remainingTime_ms) {
		breakPanel.setRemainingTime(remainingTime_ms);
	}	
	
	public void setBreakOver(boolean breakOver) {
		breakPanel.setClickNext(breakOver);
	}
	
	public void setBreakText(String text) {
		breakPanel.setInstructionText(text);
	}	
}