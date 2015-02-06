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
package org.mitre.icarus.cps.app.widgets.map.phase_05;

/**
 * Defines data that specifies how feature vector elements are rendered.
 * 
 * @author Jing Hu
 *
 */
public class RenderData {
	
	/** Tile width property (pixels per grid unit in the x dimension) */
	public final double tileWidth;
	
	/** Tile height property (pixels per grid unit in the y dimension) */
	public final double tileHeight;
	
	/** Width property */
	public final int width;
	
	/** Height property */
	public final int height;
	
	/** Row count property */
	public final int rowCount;
	
	/** Column count property */
	public final int colCount;
	
	public RenderData(double tileWidth, double tileHeight, int width,
			int height, int rowCount, int colCount) {
		
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		this.width = width;
		this.height = height;
		this.rowCount = rowCount;
		this.colCount = colCount;
	}

	public double getTileWidth() {
		return tileWidth;
	}

	public double getTileHeight() {
		return tileHeight;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getRowCount() {
		return rowCount;
	}

	public int getColCount() {
		return colCount;
	}
}