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
package org.mitre.icarus.cps.app.experiment.ui_study;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import org.mitre.icarus.cps.app.widgets.WidgetConstants;

public class UIStudyConstants {
	
	/** Default grid width for hit panels */
	public static final int GRID_WIDTH = 100;
	
	/** Default grid height for hit panels */
	public static final int GRID_HEIGHT = 100;
	
	/** Default hit panel size */
	public static final Dimension HIT_PANEL_SIZE = new Dimension(300, 300);
	
	/** Distinct bars current setting fill color */
	public static final Color COLOR_CURRENT_SETTING_FILL = new Color(125, 169, 223);
	
	/** Distinct bars current setting fill color when disabled */
	public static final Color COLOR_CURRENT_SETTING_FILL_DISABLED = Color.BLACK;
	
	/** Distinct bars size */
	public static final Dimension DISTINCT_BARS_BOX_SIZE = new Dimension(60, 200); //was (56, 100)
	
	/** Stacked bars size */
	public static final Dimension STACKED_BARS_SIZE = new Dimension (70, 200);
	
	/** Default font */
	public static final Font FONT_DEFAULT = WidgetConstants.FONT_DEFAULT.deriveFont(Font.BOLD, 14); 	
	
	/** Bold version of default font */
	public static final Font FONT_DEFAULT_BOLD = FONT_DEFAULT.deriveFont(Font.BOLD);
	
	/** Font for the percentages display */
	public static final Font FONT_PERCENTAGES = FONT_DEFAULT.deriveFont(Font.BOLD, 16);
	
	/** Font for the top title and sum in probability entry containers */
	public static final Font FONT_TITLE = FONT_DEFAULT.deriveFont(Font.BOLD, 16);
	
	/** Font for the probability display */
	public static final Font FONT_PROBABILITY = FONT_DEFAULT.deriveFont(Font.BOLD, 14);
	
	/** Font for the probability title */
	public static final Font FONT_PROBABILITY_TITLE = FONT_PROBABILITY;
	
	/** Hits font for Tasks 1-3, 5 */
	public static final Font FONT_HITS =  FONT_DEFAULT.deriveFont(Font.BOLD, 20);
	
	/** Hits font for Task 4 */
	public static final Font FONT_HITS_LARGE =  FONT_DEFAULT.deriveFont(Font.BOLD, 40);
}