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

import java.awt.Font;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.mitre.icarus.cps.feature_vector.phase_1.GridLocation2D;

/**
 * Contains information required to render the map and provides methods
 * for performing coordinate translations.
 * 
 * @author CBONACETO
 *
 */
public class RenderProperties {
	
	/** The grid width */
	public final int width_gridUnits;
	
	/** The grid height */
	public final int height_gridUnits;
	
	/** The grid scale */
	public final double milesPerGridUnit;
	
	/** Bounds for rendering in pixel space */
	public final Rectangle2D.Double renderBounds;
	
	/** The number of pixels per grid unit  */
	private double pixelsPerGridUnit;	
	
	/** The placemark marker size (in grid units) */	
	private double placemarkSize_gridUnits;
	
	/** The placemark marker size (in pixels) */
	private double placemarkSize_pixels = MapConstants_Phase1.PLACEMARK_SIZE_PIXELS;
	
	/** The maximum width for a tool tip (in pixels) */
	public final int maxToolTipWidth = 200;
	
	/** The placemark font */
	public Font placemarkFont = MapConstants_Phase1.PLACEMARK_FONT;	
	
	public RenderProperties(int width_gridUnits, int height_gridUnits, double milesPerGridUnit) {
		this(width_gridUnits, height_gridUnits, milesPerGridUnit, new Rectangle2D.Double());
	}
	
	public RenderProperties(int width_gridUnits, int height_gridUnits, double milesPerGridUnit, Rectangle2D.Double renderBounds) {
		this.width_gridUnits = width_gridUnits;
		this.height_gridUnits = height_gridUnits;
		this.milesPerGridUnit = milesPerGridUnit;
		if(renderBounds == null) {
			this.renderBounds = new Rectangle2D.Double();
		}
		else {
			this.renderBounds = renderBounds;
		}
		//this.pixelsPerGridUnit = pixelsPerGridUnit;
		//setPlacemarkSize_pixels(MapConstants.PLACEMARK_SIZE_PIXELS);
	}	

	public double getMilesPerGridUnit() {
		return milesPerGridUnit;
	}

	public double getPixelsPerGridUnit() {
		return pixelsPerGridUnit;
	}

	private void setPixelsPerGridUnit(double pixelsPerGridUnit) {
		this.pixelsPerGridUnit = pixelsPerGridUnit;
		if(pixelsPerGridUnit != 0) {
			placemarkSize_gridUnits = placemarkSize_pixels / pixelsPerGridUnit;
		}
		else {
			placemarkSize_gridUnits = 0;
		}
	}

	public int getWidth_gridUnits() {
		return width_gridUnits;
	}

	public int getHeight_gridUnits() {
		return height_gridUnits;
	}
	
	public GridLocation2D getCenterGridLocation() {
		return new GridLocation2D(width_gridUnits/2, height_gridUnits/2);
	}

	public Rectangle2D getRenderBounds() {
		return renderBounds;
	}
	
	public void setRenderBounds(double x, double y, double width_pixels, double height_pixels) {		
		// Keep aspect ratio square
		if(width_pixels < height_pixels) {
			//Use the width
			setPixelsPerGridUnit(width_pixels/width_gridUnits);
			renderBounds.setFrame(x, (height_pixels - width_pixels)/2 + y, 
					width_pixels, width_pixels);
		}
		else {			
			//Use the height
			setPixelsPerGridUnit(height_pixels/height_gridUnits);
			renderBounds.setFrame((width_pixels - height_pixels)/2.d + x, y, 
					height_pixels, height_pixels);
		}		
		//System.out.println("Pixel width: " + renderBounds.getWidth() + ", last coordinate: " + (99*pixelsPerGridUnit));
	}

	public double getPlacemarkSize_gridUnits() {
		return placemarkSize_gridUnits;
	}

	public double getPlacemarkSize_pixels() {
		return placemarkSize_pixels;
	}

	public void setPlacemarkSize_pixels(double placemarkSize_pixels) {
		this.placemarkSize_pixels = placemarkSize_pixels;
		if(pixelsPerGridUnit != 0) {
			placemarkSize_gridUnits = placemarkSize_pixels / pixelsPerGridUnit;
		}
		else {
			placemarkSize_gridUnits = 0;
		}
	}

	public Font getPlacemarkFont() {
		return placemarkFont;
	}

	public void setPlacemarkFont(Font placemarkFont) {
		this.placemarkFont = placemarkFont;
	}
	
	public double translateToPixels(double gridUnits) {
		return gridUnits * pixelsPerGridUnit;
	}
	
	public double translateToGridUnits(double pixels) {
		return pixels / pixelsPerGridUnit;
	}

	public Point2D translateToPixel(GridLocation2D gridLocation) {		
		return new Point2D.Double((gridLocation.x) * pixelsPerGridUnit, 
				(height_gridUnits - gridLocation.y) * pixelsPerGridUnit);
	}
	
	public Point2D translateToOffsetPixel(GridLocation2D gridLocation) {		
		return new Point2D.Double((gridLocation.x) * pixelsPerGridUnit + renderBounds.x, 
				(height_gridUnits - gridLocation.y) * pixelsPerGridUnit + renderBounds.y);
	}
	
	public Point2D translateToPixel(double gridX, double gridY) {
		return new Point2D.Double((gridX) * pixelsPerGridUnit, (height_gridUnits - gridY) * pixelsPerGridUnit);
	}
	
	public GridLocation2D translateToGrid(double pixelX, double pixelY) {
		return new GridLocation2D(null, pixelX / pixelsPerGridUnit, height_gridUnits - pixelY / pixelsPerGridUnit);
	}
	
	public GridLocation2D translateToGrid(Point mousePoint) {
		translateMouseToPixel(mousePoint);
		return translateToGrid(mousePoint.x, mousePoint.y);
	}
	
	public Point2D translateMouseToPixel(int mouseX, int mouseY) {
		return new Point2D.Double(mouseX - renderBounds.x, mouseY - renderBounds.y);
	}
	
	public Point translateMouseToPixel(Point mousePoint) {
		mousePoint.setLocation(mousePoint.getX() - renderBounds.x, mousePoint.getY() - renderBounds.y);
		return mousePoint;
	}
}