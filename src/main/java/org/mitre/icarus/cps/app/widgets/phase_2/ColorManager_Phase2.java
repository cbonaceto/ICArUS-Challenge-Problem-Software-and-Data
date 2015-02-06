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
package org.mitre.icarus.cps.app.widgets.phase_2;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages colors for the Phase 2 CPD.
 * 
 * @author CBONACETO
 *
 */
public class ColorManager_Phase2 {	
	/** Singleton instance of the ColorManager */
	private static final ColorManager_Phase2 colorManager = new ColorManager_Phase2();	
	
	/** Blue player color key */
	public static final String BLUE_PLAYER = "BLUE";	
	
	/** Red player color key */
	public static final String RED_PLAYER = "RED";
	
	/** Red attacked color key for batch plots */
	public static final String RED_ATTACK = "RED_ATTACK";
	
	/** Red did not attack color key for batch plots */
	public static final String RED_NOT_ATTACK = "RED_NOT_ATTACK";
	
	/** INT color keys */
	public static final String BLUEBOOK = "BLUEBOOK";
	public static final String BLUEBOOK_UNHIGHLIGHTED = "BLUEBOOK_U";
	public static final String OSINT = "OSINT";
	public static final String IMINT = "IMINT";
	public static final String HUMINT = "HUMINT";
	public static final String SIGINT = "SIGINT";
	public static final String SIGINT_HIGHLIGHT_TEXT = "SIGINT_HIGHLIGHT";
	public static final String SIGINT_RELIABILITY_TABLE = "SIGINT_RELIABILITY";
	public static final String REPORT = "REPORT";
	
	/** Payoff matrix color keys */
	public static final String BLUE_ACTION_CELL = "BLUE_ACTION";
	public static final String RED_ACTION_CELL = "RED_ACTION";
	
	/** The colors */
	private Map<String, Color> colors;
	
	private ColorManager_Phase2() {
		colors = new HashMap<String, Color>(5);
		
		//Add Blue and Red player colors
		Color blueColor = new Color(0, 112, 192);
		colors.put(BLUE_PLAYER, blueColor);
		Color redColor = new Color(255, 0, 0);
		colors.put(RED_PLAYER, redColor);
		colors.put(RED_ATTACK, Color.BLACK);
		colors.put(RED_NOT_ATTACK, new Color(255, 192, 0));
		
		//Add INT colors
		colors.put(BLUEBOOK, new Color(0, 204, 255));
		colors.put(BLUEBOOK_UNHIGHLIGHTED, new Color(197, 244, 255));
		colors.put(OSINT, blueColor);
		colors.put(IMINT, new Color(0, 147, 66));
		colors.put(HUMINT, new Color(179, 45, 135));
		colors.put(SIGINT, new Color(230, 0, 0));
		colors.put(SIGINT_HIGHLIGHT_TEXT, new Color(255, 40, 40));
		colors.put(SIGINT_RELIABILITY_TABLE, new Color(255, 221, 221));
		colors.put(REPORT, Color.BLACK);
		
		//Add payoff matrix colors
		colors.put(BLUE_ACTION_CELL, new Color(233, 239, 247));
		colors.put(RED_ACTION_CELL, colors.get(SIGINT_RELIABILITY_TABLE));
	}
	
	public static Color getColor(String key) {
		return colorManager.colors.get(key);
	}
}