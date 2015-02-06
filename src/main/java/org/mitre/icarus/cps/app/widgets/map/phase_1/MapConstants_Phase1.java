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
package org.mitre.icarus.cps.app.widgets.map.phase_1;

import java.awt.Color;

import org.mitre.icarus.cps.app.widgets.map.LineStyle;
import org.mitre.icarus.cps.app.widgets.map.MapConstants;

/**
 * Constants class with default map settings for the Phase 1 map.
 * 
 * @author CBONACETO
 *
 */
public class MapConstants_Phase1 extends MapConstants {		
	/** The default road color */
	public static final Color ROAD_COLOR = Color.GRAY;
	
	/** Whether to attempt to make roads look "curvier" when rendering them */
	public static final boolean SMOOTH_ROADS = true;
	
	/** Whether to remove points that form right angles when rendering roads */
	public static final boolean PRUNE_ROADS = true;
	
	/** The default stroke width for the road */
	public static final float ROAD_STROKE_WIDTH = 1.5f;		
	
	/** SIGINT silent line style is a dotted line */
	public static final LineStyle SIGINT_SILENT_LINE_STYLE = new LineStyle(1.f, LineStyle.CAP_SQUARE, LineStyle.JOIN_MITER, 
			10.f, new float[] {3.f}, 0.f);	
	
	/** The alpha composite value to use for the SOCINT regions */
	public static final float REGION_TRANSPARENCY = 0.75f;
}