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
package org.mitre.icarus.cps.app.widgets.map.phase_2.objects.area_grid;

import java.awt.Color;

/**
 * Contains the red, green, blue, and optional alpha value of a color. Has methods to 
 * convert the color to a single integer, which is useful for image rendering.
 * Also has static methods to convert a java.awt.Color or given red/green/blue/alpha
 * values to a single integer.
 * 
 * @author CBONACETO
 *
 */
public class ColorRGBA {
	
	protected final int red;
	
	protected final int green;
	
	protected final int blue;
	
	protected final int alpha;	
	
	/**
	 * @param red
	 * @param green
	 * @param blue
	 */
	public ColorRGBA(int red, int green, int blue) {
		this(red, green, blue, 255);
	}
	
	/**
	 * @param col
	 */
	public ColorRGBA(Color col) {
		this(col.getRed(), col.getGreen(), col.getBlue(), col.getAlpha());
	}
	
	/**
	 * @param red
	 * @param green
	 * @param blue
	 * @param alpha
	 */
	public ColorRGBA(int red, int green, int blue, int alpha) {
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = alpha;
	}
	
	/** Returns the int RGB value for the given alpha and r, g, b values 
	 *  (Bits 24-31 are alpha, 16-23 are red, 8-15 are green, 0-7 are blueBits 24-31 are alpha,
	 *  16-23 are red, 8-15 are green, 0-7 are blue) */	
	public int getRGBA() {		
		return (blue) | (green << 8) | (red << 16) | (alpha << 24);
	}
	
	/**
	 * @param red
	 * @param green
	 * @param blue
	 * @return
	 */
	public static int getRGBA(int red, int green, int blue) {
		return (blue) | (green << 8) | (red << 16) | (255 << 24);
	}
	
	/**
	 * @param red
	 * @param green
	 * @param blue
	 * @param alpha
	 * @return
	 */
	public static int getRGBA(int red, int green, int blue, int alpha) {
		return (blue) | (green << 8) | (red << 16) | (alpha << 24);
	}
	
	/**
	 * @param col
	 * @return
	 */
	public static int getRGBA(Color col) {
		return (col.getBlue()) | (col.getGreen() << 8) | (col.getRed() << 16) | (col.getAlpha() << 24);
	}
}