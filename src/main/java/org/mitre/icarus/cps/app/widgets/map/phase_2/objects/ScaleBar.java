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
package org.mitre.icarus.cps.app.widgets.map.phase_2.objects;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Collection;

import org.mitre.icarus.cps.app.widgets.map.MapConstants;
import org.mitre.icarus.cps.app.widgets.map.phase_2.RenderProperties;
import org.mitre.icarus.cps.feature_vector.phase_2.util.GeoUtils;

/**
 * Renders a scalebar for a map. Units currently fixed to miles.
 * 
 * @author CBONACETO
 *
 */
public class ScaleBar extends AbstractShape {
	/** Units label orientation (drawn at top or bottom of scale bar) */
	public static enum TextOrientation {Top, Bottom};
	
	protected int[] scaleIncrements;
	
	protected int hashmarkHeight_pixels = 3;
	
	protected Font font = MapConstants.SCALEBAR_FONT;
	
	/** Orientation of the units text (top or bottom of scale bar) */
	protected TextOrientation unitsLabelOrientation = TextOrientation.Top;
	
	protected String unitsName = "miles";
	
	/** The number of pixels to offset the start location of the scale bar from the bottom left corner of the viewport */
	protected Point2D startLocationOffset = new Point2D.Double(5, 5);
	
	/** The left coordinate of the scalebar (in world pixel space) */
	protected Point2D startLocation_worldPixel;
	
	/** The width of the scale bar in pixels */
	protected double scaleWidth_pixels;	


	public ScaleBar() {
		//scaleIncrements = new int[] {0, 5, 10};
		//scaleIncrements = new int[] {0, 1, 2};
		scaleIncrements = new int[] {0, 1};
		foregroundColor = Color.DARK_GRAY;
	}		

	public int[] getScaleIncrements() {
		return scaleIncrements;
	}

	public void setScaleIncrements(int[] scaleIncrements) {
		this.scaleIncrements = scaleIncrements;
	}	
	
	/**
	 * @return
	 */
	public TextOrientation getUnitsLabelOrientation() {
		return unitsLabelOrientation;
	}

	/**
	 * @param unitsLabelOrientation
	 */
	public void setUnitsLabelOrientation(TextOrientation unitsLabelOrientation) {
		this.unitsLabelOrientation = unitsLabelOrientation;
	}

	/**
	 * @return
	 */
	public Point2D getStartLocationOffset() {
		return startLocationOffset;
	}

	/**
	 * @param startLocationOffset
	 */
	public void setStartLocationOffset(Point2D startLocationOffset) {
		this.startLocationOffset = startLocationOffset;
	}	
	

	@Override
	public void setMouseOverState(boolean mouseOver, MouseEvent event) {}

	public Point2D getCenterPixelLocation() {
		return new Point2D.Double(startLocation_worldPixel.getX() + scaleWidth_pixels/2, 
				startLocation_worldPixel.getY());
	}	

	@Override
	public Point2D getCenterGeoLocation() {	
		return null;
	}

	@Override
	public Shape getGeoShape() {		
		return null;
	}

	@Override
	public Shape getPixelShape() {
		return null;
	}

	@Override
	public Collection<? extends ControlPoint> getControlPoints() {		
		return null;
	}

	@Override
	public void render(Graphics2D g, RenderProperties renderProperties, boolean renderPropertiesChanged) {
		Color origColor = g.getColor();
		Font origFont = g.getFont();
		Stroke origStroke = g.getStroke();
		
		//Calculate the start location of the scale in world pixel space
		startLocation_worldPixel = renderProperties.viewportPixelToWorldPixel(new Point2D.Double(
				startLocationOffset.getX(), renderProperties.getViewportBounds().getHeight() - startLocationOffset.getY()));		
		double startX = startLocation_worldPixel.getX();		
		double currentX = startLocation_worldPixel.getX();
		
		//Calculate the length of the scale in pixels
		double scaleWidth_miles = scaleIncrements[scaleIncrements.length-1];
		double latitude = renderProperties.worldPixelToGeoPosition(startLocation_worldPixel).getLatitude();
		double milesPerDegree = GeoUtils.metersToMiles(GeoUtils.computeLengthOfDegreeOfLongitude_meters(latitude));
		scaleWidth_pixels = renderProperties.degreesToPixels(scaleWidth_miles / milesPerDegree);
		/*System.out.println("milesPerDegree: " + milesPerDegree + ", scaleWidth_degrees: " + (scaleWidth_miles / milesPerDegree) + 
				", scaleWidth_pixels: " + scaleWidth_pixels + ", viewportWidth_degrees: " + 
				renderProperties.pixelsToDegrees(renderProperties.getViewportBounds().getWidth()) + 
				", viewportWidth_pixels: " + renderProperties.getViewportBounds().getWidth());*/
		double endX = startLocation_worldPixel.getX() + scaleWidth_pixels;		
		double y = startLocation_worldPixel.getY();
		
		//Draw line from start x to end x		
		if(foregroundColor == null) {
			foregroundColor = Color.DARK_GRAY;
		}
		g.setColor(foregroundColor);
		g.setStroke(new BasicStroke(1.5f));
		g.drawLine((int)currentX, (int)y,(int)endX, (int)y);
		
		//Draw hash marks and distance labels at each scale increment
		if(font == null) {
			font = MapConstants.SCALEBAR_FONT;
		}
		g.setFont(font);
		int labelHeight = g.getFontMetrics().getHeight();
		for(int scaleIncrement : scaleIncrements) {
			//Draw hash mark
			//currentX = startX + renderProperties.degreesToPixels(scaleIncrement / renderProperties.milesPerDegree);
			currentX = startX + scaleWidth_pixels * (scaleIncrement / scaleWidth_miles);
			g.drawLine((int)currentX, (int)(y - hashmarkHeight_pixels), (int)currentX, (int)(y + hashmarkHeight_pixels));
			
			//Draw label
			String label = Integer.toString(scaleIncrement);
			int labelWidth = g.getFontMetrics().stringWidth(label);			
			g.drawString(Integer.toString(scaleIncrement), (float)(currentX - labelWidth/2.f), 
					(float)(y + hashmarkHeight_pixels + labelHeight - 2));
		}
		
		//Draw units label above or below scale
		int labelWidth = g.getFontMetrics().stringWidth(unitsName);
		double lineWidth = endX - startX;
		if(unitsLabelOrientation == TextOrientation.Top) {
			g.drawString(unitsName, (float)(startX + (lineWidth - labelWidth)/2.f), 
					(float)(y - hashmarkHeight_pixels - 3));
		} else {
			g.drawString(unitsName, (float)(startX + (lineWidth - labelWidth)/2.f), 
					(float)(y + hashmarkHeight_pixels + labelHeight * 2 - 6));
		}
		
		g.setColor(origColor);
		g.setFont(origFont);
		g.setStroke(origStroke);
	}
}