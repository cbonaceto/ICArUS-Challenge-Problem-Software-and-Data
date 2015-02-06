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
package org.mitre.icarus.cps.app.widgets.basic;

import java.awt.Component;

import org.mitre.icarus.cps.app.widgets.WidgetConstants;
import org.mitre.icarus.cps.experiment_core.gui.ConditionPanel;
import org.mitre.icarus.cps.experiment_core.gui.JPanelConditionComponent;

/**
 * Base class for Phase 1-3 experiment panels. Handles showing mission instructions 
 * in the external instructions window.
 * 
 * @author CBONACETO
 *
 */
public class BasicConditionPanel extends ConditionPanel {
	private static final long serialVersionUID = 1076063098056706059L;

	/** Content panel contains all sub-panels */
	protected JPanelConditionComponent contentPanel;	
	
	/** Break screen panel */
	protected BreakPanel breakPanel;	
	
	public BasicConditionPanel(Component parent) {
		this(parent, false, WidgetConstants.BANNER_ORIENTATION, 4);
	}
	
	public BasicConditionPanel(Component parent, boolean showInstructionBanner, BannerOrientation bannerOrientation) {		
		super(showInstructionBanner, bannerOrientation, 4);
	}	
	
	public BasicConditionPanel(Component parent, boolean showInstructionBanner, BannerOrientation bannerOrientation, 
			int instructionBannerTextHeight) {		
		super(showInstructionBanner, bannerOrientation, instructionBannerTextHeight);
	}	
	
	public void showContentPanel() {
		setConditionComponent(contentPanel);
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