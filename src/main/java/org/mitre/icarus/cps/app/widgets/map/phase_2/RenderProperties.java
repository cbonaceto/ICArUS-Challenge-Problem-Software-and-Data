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

import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.mapviewer.TileFactory;

/**
 * Translates geo locations to pixel locations on the map.
 * Does not currently handle horizontally wrapped viewports.
 * 
 * @author CBONACETO
 *
 */
public class RenderProperties {
	
	/** The current map tile factory */
	protected TileFactory tileFactory;
	
	/** The current map viewport bounds (visible area) */
	protected Rectangle viewportBounds;
	
	/** The current map zoom level */
	protected int zoom;	
	
	/** The default placemark marker size (in pixels) */
	protected double placemarkSize_pixels = MapConstants_Phase2.PLACEMARK_SIZE_PIXELS;
	
	/** The default placemark font */
	protected Font placemarkFont = MapConstants_Phase2.PLACEMARK_FONT;
	
	/** The maximum width for a tool tip (in pixels) */
	protected int maxToolTipWidth = 200;
	
	/** When geo translations are disabled, the map tile factory is not used to convert coordinates to
	 * pixel locations. The coordinates lat and lon are treated like pixel locations (y and x). */
	protected boolean geoTranslationsDisabled = false;
	
	public RenderProperties(TileFactory tileFactory) {
		this.tileFactory = tileFactory;
	}	
	
	public Rectangle getViewportBounds() {		
		return viewportBounds;
	}

	public int getZoom() {
		return zoom;
	}
	
	public double getPlacemarkSize_degrees() {
		return pixelsToDegrees(placemarkSize_pixels);		
		//return placemarkSize_degrees;
	}	

	public double getPlacemarkSize_pixels() {
		return placemarkSize_pixels;
	}

	public Font getPlacemarkFont() {
		return placemarkFont;
	}	

	public int getMaxToolTipWidth() {
		return maxToolTipWidth;
	}

	public boolean isGeoTranslationsDisabled() {
		return geoTranslationsDisabled;
	}

	protected void updateVisibleBounds() {
		//TODO: Create two viewports to handle wrapped viewports
	}
	
	public boolean isGeoPositionVisible(Point2D pos) {
		return viewportBounds.contains(geoPositionToWorldPixel(pos));
	}

	public boolean isGeoPositionVisible(GeoPosition pos) {
		return viewportBounds.contains(geoPositionToWorldPixel(pos));
	}
	
	public boolean isWorldPixelLocationVisible(Point2D loc) {
		return viewportBounds.contains(loc);
	}
	
	public boolean isGeoShapeVisible(Shape geoShape) {
		if(geoShape != null) {
			return viewportBounds.intersects(geoRectangleToWorldPixelRectangle(geoShape.getBounds2D()));
		}
		return false;
	}
	
	public boolean isGeoShapeCompletelyInView(Shape geoShape) {
		if(geoShape != null) {
			return viewportBounds.contains(geoRectangleToWorldPixelRectangle(geoShape.getBounds2D()));
		}
		return false;
	}
	
	/**
	 * @param pixelShape
	 * @return
	 */
	public boolean isPixelShapeVisible(Shape pixelShape) {	
		if(pixelShape != null) {
			return viewportBounds.intersects(pixelShape.getBounds());
		}
		return false;
	}
	
	/**
	 * Returns whether the given pixel shape is completely contained within the viewport bounds.
	 * 
	 * @param pixelShape
	 * @return
	 */
	public boolean isPixelShapeCompletelyInView(Shape pixelShape) {	
		if(pixelShape != null) {
			return viewportBounds.contains(pixelShape.getBounds());
		}
		return false;
	}
	
	/**
	 * Returns the area (in pixels^2) of the given pixel shape outside of the viewport bounds.
	 * 
	 * @param pixelShape
	 * @return
	 */
	public double computePixelShapeAreaOutsideView(Shape pixelShape) {
		if(pixelShape != null && pixelShape.getBounds() != null) {
			Rectangle bounds = pixelShape.getBounds();
			//Compute the intersection of the pixel shape and the bounds
			Rectangle intersection = viewportBounds.intersection(bounds);
			//System.out.println("Intersection: " + intersection);
			if(intersection != null && intersection.width > 0 && intersection.height > 0) {
				return (bounds.width * bounds.height) - (intersection.width * intersection.height); 
			} else {
				return bounds.width * bounds.height;			
			}		
		}		
		return 0d;
	}

	/**
	 * Converts a distance in degrees of longitude to a distance in pixels.
	 * 
	 * @param degrees
	 * @return
	 */
	public double degreesToPixels(double degrees) {	
		//System.out.println("longitudeWidth_pixels: " + tileFactory.getInfo().getLongitudeDegreeWidthInPixels(zoom));
		if(geoTranslationsDisabled) {
			return degrees;
		} else {
			return tileFactory.getInfo().getLongitudeDegreeWidthInPixels(zoom) * degrees;
		}
	}
	
	/**
	 * Converts a distance in pixels to a distance in degrees of longitude.
	 * 
	 * @param pixels
	 * @return
	 */
	public double pixelsToDegrees(double pixels) {
		if(geoTranslationsDisabled) {
			return pixels;
		} else { 
			return pixels / tileFactory.getInfo().getLongitudeDegreeWidthInPixels(zoom);
		}
	}
	
	/**
	 * @param pos
	 * @return
	 */
	public Point2D geoPositionToWorldPixel(Point2D pos) {
		return geoPositionToWorldPixel(new GeoPosition(pos.getY(), pos.getX()));
	}
	
	/**
	 * @param pos
	 * @return
	 */
	public Point2D geoPositionToWorldPixel(GeoPosition pos) {
		if(geoTranslationsDisabled) {
			return new Point2D.Double(pos.getLongitude(), pos.getLatitude());
		} else {
			return tileFactory.geoToPixel(pos, zoom);
		}
	}
	
	/**
	 * @param pos
	 * @return
	 */
	public Point2D geoPositionToViewportPixel(Point2D pos) {
		return geoPositionToViewportPixel(new GeoPosition(pos.getY(), pos.getX()));
	}
	
	/**
	 * @param pos
	 * @return
	 */
	public Point2D geoPositionToViewportPixel(GeoPosition pos) {
		Point2D p = geoTranslationsDisabled ? new Point2D.Double(pos.getLongitude(), pos.getLatitude()) : tileFactory.geoToPixel(pos, zoom);
		p.setLocation(p.getX() - viewportBounds.getX(), p.getY() - viewportBounds.getY());
		return p;
	}	
	
	/**
	 * @param worldPixel
	 * @return
	 */
	public Point2D worldPixelToViewportPixel(Point2D worldPixel) {
		return new Point2D.Double(worldPixel.getX() - viewportBounds.getX(), 
				worldPixel.getY() - viewportBounds.getY());
	}
	
	public GeoPosition worldPixelToGeoPosition(Point2D worldPixel) {
		if(geoTranslationsDisabled) {
			return new GeoPosition(worldPixel.getY(), worldPixel.getX());
		} else {
			return tileFactory.pixelToGeo(worldPixel, zoom);
		}
	}
	
	/**
	 * @param viewportPixel
	 * @return
	 */
	public Point2D viewportPixelToWorldPixel(Point2D viewportPixel) {
		//TODO: Double-check this
		return new Point2D.Double(viewportPixel.getX() + viewportBounds.getX(), 
				viewportPixel.getY() + viewportBounds.getY());
	}	
	
	public Rectangle2D geoRectangleToWorldPixelRectangle(Rectangle2D geoRect) {
		//TODO: Implement this		
		return null;
	}
	
	public Rectangle2D worldPixelRectangleToGeoRectangle(Rectangle2D worldPixelRect) {
		//TODO: return a Path2D instead
		if(worldPixelRect != null) {
			GeoPosition topLeft = worldPixelToGeoPosition(new Point2D.Double(worldPixelRect.getX(), worldPixelRect.getY()));
			return new Rectangle2D.Double(topLeft.getLongitude(), topLeft.getLatitude(),
					pixelsToDegrees(worldPixelRect.getWidth()), 
					pixelsToDegrees(worldPixelRect.getHeight()));
		}
		return null;
	}
}