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
package org.mitre.icarus.cps.app.widgets.map.phase_2;

import java.awt.Color;
import java.awt.Insets;

import org.mitre.icarus.cps.app.widgets.map.LineStyle;
import org.mitre.icarus.cps.app.widgets.map.MapConstants;

/**
 * Constants class with default map settings for the Phase 2 map.
 * 
 * @author CBONACETO
 *
 */
public class MapConstants_Phase2 extends MapConstants {
	/** The default preferred map width */
	public static final int PREFERRED_MAP_WIDTH = 521;
	
	/** The default preferred map height */
	public static final int PREFERRED_MAP_HEIGHT = 521;
	
	/** The default small map width (used by the player) */
	public static final int SMALL_MAP_WIDTH = 521;
	
	/** The default small map height (used by the player) */
	public static final int SMALL_MAP_HEIGHT = 521;
	
	/** A border of with all insets at 0 */
	public static final Insets MAP_BORDER_EMPTY = new Insets(0, 0, 0, 0);
	
	/** A border of with all insets at 2 */
	public static final Insets MAP_BORDER_NARROW = new Insets(1, 1, 1, 1);
	
	/** The default tool tip background color */	
	public static final Color TOOLTIP_BACKGROUND_COLOR = Color.WHITE; //new Color(189, 222, 255);
	
	/** The default tool tip border color */
	public static final Color TOOLTIP_BORDER_COLOR = Color.DARK_GRAY;
	
	/** Blue Location radius (in pixels) when the Blue Location is the focus of a trial */
	public static final int BLUE_PLACEMARK_SIZE_PIXELS = 18;
	
	/** Blue Location radius (in pixels) for locations shown in batch plots */
	public static final int BLUE_OUTCOME_PLACEMARK_SIZE_PIXELS = 12;
	
	/** Whether to fill the Blue Location circle when the Blue Location is the focus of the trial */
	public static final boolean FILL_BLUE_PLACEMARK = true;	
	
	/** Whether to show OSINT text at a Blue Location */
	public static final boolean SHOW_OSINT_TEXT = false;
	
	/** Whether to show OSINT lines */
	public static final boolean SHOW_OSINT_LINE = true;
	
	/** OSINT line style is a solid line */
	public static final LineStyle OSINT_LINE_STYLE = new LineStyle(2.f);	
	
	/** Whether to show IMINT text at a Blue Location */
	public static final boolean SHOW_IMINT_TEXT = false;
	
	/** Whether to show IMINT circles */
	public static final boolean SHOW_IMINT_CIRCLE = true;
	
	/** IMINT line style is a solid line */
	public static final LineStyle IMINT_LINE_STYLE = new LineStyle(2.f);
	
	/** Whether to use a fixed radius (in pixels) for IMINT circles, which is the placemark size in pixels/2 + 3 */
	public static final boolean IMINT_RADIUS_FIXED = false;
	
	/** The IMINT radius in pixels when using fixed IMINT radii */
	public static final int IMINT_RADIUS_PIXELS = MapConstants_Phase2.PLACEMARK_SIZE_PIXELS/2 + 3;
	
	/** Whether to show HUMINT text at a Blue Location */
	public static final boolean SHOW_HUMIMINT_TEXT = false;
	
	/** Whether to fill SIGINT circles */
	public static final boolean FILL_SIGINT_CIRCLE = true;
	
	/** SIGINT silent line style is a dotted line */
	public static final LineStyle SIGINT_SILENT_LINE_STYLE = new LineStyle(2.f, LineStyle.CAP_SQUARE, LineStyle.JOIN_MITER, 
			10.f, new float[] {3.f}, 0.f);	
	
	/** SIGINT chatter line style is a solid line */
	public static final LineStyle SIGINT_CHATTER_LINE_STYLE = new LineStyle(2.f);
	
	/** The alpha composite value to use for SIGINT silence circles */
	public static final float SIGINT_SILENCE_TRANSPARENCY = 0.65f;
	
	/** The alpha composite value to use for filled SIGINT chatter circles */
	public static final float SIGINT_CHATTER_TRANSPARENCY = 0.25f;
	
	/** Whether to use a fixed radius (in pixels) for SIGINT circles, which is the placemark size in pixels/2 + 6 */
	public static final boolean SIGINT_RADIUS_FIXED = false;	
	
	/** The SIGINT radius in pixels when using fixed SIGINT radii */
	public static final int SIGINT_RADIUS_PIXELS = MapConstants_Phase2.PLACEMARK_SIZE_PIXELS/2 + 4;
	
	/** Offset (in pixels) for INT annotation text (e.g., for OSINT, IMINT, and HUMINT) */
	@SuppressWarnings("unused")
	public static final int INT_ANNOTATION_OFFSET_PIXELS = SIGINT_RADIUS_FIXED || IMINT_RADIUS_FIXED ? 10 : 6;
	
	/** The blue region line style */
	public static final LineStyle BLUE_REGION_LINE_STYLE = new LineStyle(2.f);
	
	/** The alpha composite value to use for Blue regions */
	public static final float BLUE_REGION_TRANSPARENCY = 0.15f;
	
	/** Whether to fill Blue regions */
	public static final boolean FILL_BLUE_REGION = false;
}