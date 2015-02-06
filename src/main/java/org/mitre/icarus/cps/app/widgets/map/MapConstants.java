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
package org.mitre.icarus.cps.app.widgets.map;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;

import org.mitre.icarus.cps.app.widgets.WidgetConstants;
import org.mitre.icarus.cps.app.widgets.map.LineStyle;

/**
 * Constants class with default map settings.
 * 
 * @author CBONACETO
 *
 */
public class MapConstants {
	
	/** The default preferred map width */
	public static final int PREFERRED_MAP_WIDTH = 530;
	
	/** The default preferred map height */
	public static final int PREFERRED_MAP_HEIGHT = 530;
	
	/** The default small map width (used by the player) */
	public static final int SMALL_MAP_WIDTH = 460;
	
	/** The default small map height (used by the player) */
	public static final int SMALL_MAP_HEIGHT = 460;
	
	/** The default preferred map border insets (blank space drawn around map) */
	public static final Insets MAP_BORDER = new Insets(14, 14, 14, 14);	
	
	/** The default tool tip background color */	
	public static final Color TOOLTIP_BACKGROUND_COLOR = new Color(189, 222, 255);//new Color(153, 204, 255);
	
	/** The default tool tip border color */
	public static final Color TOOLTIP_BORDER_COLOR = Color.DARK_GRAY;
	
	/** The default placemark font */
	public static final Font PLACEMARK_FONT = WidgetConstants.FONT_DEFAULT.deriveFont(Font.BOLD, 13);
	
	/** The large placemark font */
	public static final Font PLACEMARK_FONT_LARGE = PLACEMARK_FONT.deriveFont(Font.BOLD, 14);
	
	/** The map highlight color */
	public static final Color HIGHLIGHT_COLOR = Color.GREEN;
	
	/** The scalebar font */
	public static final Font SCALEBAR_FONT = PLACEMARK_FONT.deriveFont(Font.PLAIN, 11);
	
	/** The default placemark size (in pixels) */
	public static final int PLACEMARK_SIZE_PIXELS = 22;
	
	/** The default placemark marker color */
	public static final Color PLACEMARK_MARKER_COLOR = Color.BLACK;	
	
	/** Default annotation offset (pixels) */
	public static final int ANNOTATION_OFFSET_PIXELS = 36; //Default is 40
	
	/** Default annotation line color */
	public static final Color ANNOTATION_LINE_COLOR = Color.DARK_GRAY;
	
	/** Default annotation line style is a dashed line */
	public static final LineStyle ANNOTATION_LINE_STYLE = new LineStyle(1.f, LineStyle.CAP_SQUARE, LineStyle.JOIN_MITER, 
			10.f, new float[] {2.f}, 0.f);	
}