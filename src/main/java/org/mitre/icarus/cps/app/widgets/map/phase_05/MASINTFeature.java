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

import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.List;

import org.mitre.icarus.cps.app.widgets.phase_05.ColorManager;
import org.mitre.icarus.cps.app.widgets.phase_05.WidgetConstants_Phase05;

/**
 * Draws a MASINT hit.
 * 
 * @author Jing Hu
 *
 */
public class MASINTFeature extends Feature {
	
	/** MASINT type */
	private int type;
	
	// cached rendering variables
	private Polygon shape = new Polygon();
	
	/*private int cols;	
	private int rows;
	private int firstRowCount;*/
	
	@Override
	public void draw(Graphics2D g, RenderData r) {
		
		double tileWidth = r.tileWidth;
		if (tileWidth < WidgetConstants_Phase05.INT_HIT_RENDER_SIZE) {
			tileWidth = WidgetConstants_Phase05.INT_HIT_RENDER_SIZE;
		}
		
		double tileHeight = r.tileHeight;
		if (tileHeight < WidgetConstants_Phase05.INT_HIT_RENDER_SIZE) {
			tileHeight = WidgetConstants_Phase05.INT_HIT_RENDER_SIZE;
		}
		
		if (type == 2) {
			g.setColor(ColorManager.get(ColorManager.MASINT1));
		} else {
			g.setColor(ColorManager.get(ColorManager.MASINT2));
		}
		
		final int triangleWidth = (int)Math.round(tileWidth*0.4);
		//final int triangleHeight = Math.round(triangleWidth * 1.1f);
		final int triangleHeight = triangleWidth;
		
		int topX = (int)Math.round(tileWidth/2);
		int topY = (int)Math.round((tileHeight-triangleHeight)/2);
		 
		shape.reset();
		shape.addPoint(topX, topY);
		shape.addPoint(topX - triangleWidth/2, topY + triangleHeight);
		shape.addPoint(topX + triangleWidth/2, topY + triangleHeight);				
		g.draw(shape);
	}
	
	/*@Override
	public void draw(final Graphics2D g, RenderData r) {
		
		double tileWidth = r.tileWidth;
		if (tileWidth < 16) {
			tileWidth = 16;
		}
		
		double tileHeight = r.tileHeight;
		if (tileHeight < 16) {
			tileHeight = 16;
		}

		final long dotWidth = Math.round(tileWidth*0.3);		
		final long dotHeight = Math.round(tileHeight*0.3);
		//final long dotHeight = dotWidth;
		
		if (type == 2) {
			g.setColor(ColorManager.get(ColorManager.MASINT1));
		} else {
			g.setColor(ColorManager.get(ColorManager.MASINT2));
		}

		double y = getStartOffset(tileHeight, dotHeight, rows);

		if (firstRowCount > 0) {
			
			final double x = getStartOffset(tileWidth, dotWidth, firstRowCount);
			y -= 0.5*dotHeight;
			
			drawDots(g, x, y, dotWidth, dotHeight, firstRowCount);			
			y += dotHeight;
		}

		final double x = getStartOffset(tileWidth, dotWidth, cols);
		for (int i = 0; i < rows; i++) {
			drawDots(g, x, y, dotWidth, dotHeight, cols);
			y += dotHeight;
		}
	}

	public void drawDots(final Graphics2D g, double x, double y,
			final double dotWidth, final double dotHeight, int dotCount) {
		
		while (dotCount > 0) {
			shape.setFrame(x, y, dotWidth, dotHeight);
			g.fill(shape);
			x+= dotWidth;
			dotCount--;
		}
	}

	private double getStartOffset(double tileSize, double dotSize, int dotCount) {
		return 0.5*(tileSize - dotSize*dotCount);
	}
	
	public void setMasCount(int masCount) {
		this.masCount = masCount;
		
		cols = (int) Math.ceil(Math.sqrt(masCount));
		rows = masCount/cols;
		firstRowCount = masCount % cols;
	}
	
	public int getMasCount() {
		return masCount;
	}*/
	
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}

	@Override
	public List<? extends Feature> getChildren() {
		return null;
	}
}
