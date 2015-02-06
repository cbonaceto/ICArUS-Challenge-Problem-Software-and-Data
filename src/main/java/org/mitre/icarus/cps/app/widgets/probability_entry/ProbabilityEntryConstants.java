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
package org.mitre.icarus.cps.app.widgets.probability_entry;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;

import org.mitre.icarus.cps.app.widgets.WidgetConstants;

public class ProbabilityEntryConstants {
	
	public static final String minPercentLabel = "0%"; // was "0.1%";
	
	public static final String maxPercentLabel = "100%"; //was 99.9%";
	
	public static final int EXACT_NORMALIZATION_ERROR_THRESHOLD = 5;
	
	public static final int LOOSE_NORMALIZATION_ERROR_THRESHOLD = 15;
	
	/** Default title for a probability entry panel */
	public static final String PROBABILITY_PANEL_TITLE = "your judgment";
	
	/** Default text to show after probabilities have been normalized */
	public static final String PROBABILITIES_NORMALIZED_MESSAGE = "Probabilities now sum to 100%.";
	
	/** Default probability box size */
	public static final Dimension BOX_SIZE = new Dimension(54, 160); //was (56, 100)
	
	/** Default probability box size for the "classic" boxes */
	public static final Dimension BOX_SIZE_CLASSIC = new Dimension(56, 100);
	
	/** Default probability box size for small boxes (used by the player) */
	public static final Dimension BOX_SIZE_SMALL = new Dimension(52, 100);
	
	/** Default stacked bars size */
	public static final Dimension STACKED_BARS_SIZE = new Dimension(70, 200);
	
	/** Default box border thickness */
	public static final int BOX_BORDER_THICKNESS = 2; //was 2
	
	/** Default font */
	public static final Font FONT_DEFAULT = WidgetConstants.FONT_DEFAULT;
	
	//public static final Font FONT_SPINNER = FONT_DEFAULT.deriveFont(Font.BOLD);
	
	/** Font for the probability display */
	public static final Font FONT_PROBABILITY = FONT_DEFAULT.deriveFont(Font.BOLD, 14); //was size 12
	
	/** Font for the top title and sum in probability entry containers */
	public static final Font FONT_TITLE = FONT_DEFAULT.deriveFont(Font.BOLD, 14);	
	
	/** Font for the probability title */
	public static final Font FONT_PROBABILITY_TITLE = FONT_PROBABILITY;
	
	public static final Color COLOR_TEXT_NORMAL = new JLabel().getForeground();
	
	public static final Color COLOR_TEXT_ERROR = Color.RED;
	
	public static final Color COLOR_SPINNER_UNSELECTED = Color.GRAY;
	
	public static final Color COLOR_SPINNER_MOUSEOVER = Color.BLACK;
	
	public static final Color COLOR_SPINNER_SELECTED = Color.BLACK;
	
	public static final Color COLOR_CURRENT_SETTING_LINE = Color.BLACK;
	
	public static final Color COLOR_SPINNER_HAS_FOCUS = new Color(210, 223, 238);// UIManager.getColor("Table.selectionBackground");
	
	public static final Color COLOR_CURRENT_SETTING_LINE_DISABLED = Color.BLACK;//new Color(100, 100, 100); //Color.BLACK; //Color.GRAY;
	
	public static final Color COLOR_CURRENT_SETTING_FILL_CLASSIC = Color.GRAY; //"Classic" current setting fill color
	
	public static final Color COLOR_CURRENT_SETTING_FILL = Color.GRAY; //new Color(125, 169, 223); //Updated current setting fill color
	
	public static final Color COLOR_CURRENT_SETTING_FILL_DISABLED = COLOR_CURRENT_SETTING_LINE_DISABLED; //Color.BLACK; //Color.GRAY;
	
	public static final Color COLOR_PREVIOUS_SETTING = Color.BLACK;	
	
	public static final Color COLOR_BOX_BACKGROUND = Color.WHITE; //Color.LIGHT_GRAY; //Color.WHITE;
	
	public static final Color COLOR_BOX_BACKGROUND_DISABLED = Color.WHITE; //new Color(220, 220, 220);//Color.WHITE; Color.GRAY;
	
	public static final Color COLOR_BOX_BORDER = Color.BLACK;
	
	public static final Color COLOR_BOX_BORDER_DISABLED = Color.BLACK; //Color.GRAY;	
}