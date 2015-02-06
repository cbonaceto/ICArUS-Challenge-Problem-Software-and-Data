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
package org.mitre.icarus.cps.app.widgets.map.phase_1.objects;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import org.mitre.icarus.cps.app.widgets.map.MapConstants;
import org.mitre.icarus.cps.app.widgets.map.phase_1.RenderProperties;

/**
 * 
 * @author CBONACETO
 *
 */
public class ScaleBar extends ShapeMapObject {
	
	protected int[] scaleIncrements;
	
	protected int hashmarkHeight_pixels = 3;
	
	protected Font font = MapConstants.SCALEBAR_FONT;
	
	protected Point2D startLocation_pixel = new Point2D.Double(0,0);
	
	protected double endX;

	public ScaleBar() {
		scaleIncrements = new int[] {0, 5, 10};
		foregroundColor = Color.DARK_GRAY;
	}	
	
	@Override
	public double getArea_pixels() {
		return 0;
	}

	@Override
	public Point2D getCenterLocation_pixels() {
		return new Point2D.Double(startLocation_pixel.getX() + (endX - startLocation_pixel.getX())/2, 
				startLocation_pixel.getY());
	}

	public int[] getScaleIncrements() {
		return scaleIncrements;
	}

	public void setScaleIncrements(int[] scaleIncrements) {
		this.scaleIncrements = scaleIncrements;
	}	

	public Point2D getStartLocation_pixel() {
		return startLocation_pixel;
	}

	public void setStartLocation_pixels(Point2D startLocation_pixel) {
		this.startLocation_pixel = startLocation_pixel;
	}

	@Override
	public void setMouseOverState(boolean mouseOver, MouseEvent event) {
	}

	@Override
	public boolean contains(Point point) {
		return false;
	}

	@Override
	public boolean contains(Point2D point) {
		return false;
	}

	@Override
	public boolean contains(double x, double y) {
		return false;
	}

	@Override
	public void render(Graphics2D g, RenderProperties renderData, boolean renderPropertiesChanged) {
		Color origColor = g.getColor();
		Font origFont = g.getFont();
		
		double scaleWidth_gridUnits = scaleIncrements[scaleIncrements.length-1] / renderData.milesPerGridUnit;
		//Point2D startLocation_pixel = renderData.translateToPixel(centerLocation);		
		endX = startLocation_pixel.getX() + renderData.translateToPixels(scaleWidth_gridUnits);
		double currentX = startLocation_pixel.getX();
		double y = startLocation_pixel.getY();
		
		//Draw line from start x to end x
		if(foregroundColor == null) {
			foregroundColor = Color.DARK_GRAY;
		}
		g.setColor(foregroundColor);
		g.drawLine((int)currentX, (int)y,(int)endX, (int)y);
		
		//Draw hash marks and distance labels at each scale increment
		if(font == null) {
			font = MapConstants.SCALEBAR_FONT;
		}
		g.setFont(font);
		int labelHeight = g.getFontMetrics().getHeight();
		for(int scaleIncrement : scaleIncrements) {
			//Draw hash mark
			currentX = startLocation_pixel.getX() + 
				renderData.translateToPixels(scaleIncrement / renderData.milesPerGridUnit);
			g.drawLine((int)currentX, (int)(y - hashmarkHeight_pixels), (int)currentX, (int)(y + hashmarkHeight_pixels));
			
			//Draw label
			String label = Integer.toString(scaleIncrement);
			int labelWidth = g.getFontMetrics().stringWidth(label);			
			g.drawString(Integer.toString(scaleIncrement), (float)(currentX - labelWidth/2.f), 
					(float)(y + hashmarkHeight_pixels + labelHeight - 2));
		}
		
		//Draw units label above scale
		String label = "miles";
		int labelWidth = g.getFontMetrics().stringWidth(label);
		double lineWidth = endX - startLocation_pixel.getX();
		g.drawString(label, (float)(startLocation_pixel.getX() + (lineWidth - labelWidth)/2.f), 
				(float)(y + hashmarkHeight_pixels + labelHeight * 2 - 6));
		
		g.setColor(origColor);
		g.setFont(origFont);
	}
}