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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.List;



public class GridlinesLayer extends Layer {
	
	private final Line2D line = new Line2D.Float();
	private int gridSpacing = 32;

	public GridlinesLayer(int layerId) {
		super(layerId);
		setName("Gridlines");
	}
	
	@Override
	public void draw(Graphics2D g, RenderData r) {
		g.setColor(Color.BLACK);
		
		double x = r.tileWidth;			
		for (int i = 1; i < r.colCount; i += gridSpacing) {
			line.setLine(x, 0, x, r.height);
			g.draw(line);
			x += r.tileWidth * gridSpacing;
		}
		
		double y = r.tileHeight;
		for (int i = 1; i < r.rowCount; i += gridSpacing) {
			line.setLine(0, y, r.width, y);
			g.draw(line);
			y += r.tileHeight * gridSpacing;
		}
	}

	@Override
	public List<? extends Feature> getChildren() {
		return null;
	}

	@Override
	public LayerType getLayerType() {
		return LayerType.Gridlines;
	}
}
