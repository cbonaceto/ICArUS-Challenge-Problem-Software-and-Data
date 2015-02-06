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

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.Collections;

import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.mitre.icarus.cps.app.widgets.map.phase_2.RenderProperties;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.ControlPoint.ControlPointType;
import org.mitre.icarus.cps.feature_vector.phase_2.coordinate.GeoCoordinate;
import org.mitre.icarus.cps.feature_vector.phase_2.util.GeoUtils;

/**
 * Circle map object. Circles can be drawn with a constant radius in pixels (when the radius type is RadiusType.Pixels)
 * regardless of the map zoom level, or they can have a radius in miles.
 * 
 * @author CBONACETO
 *
 */
public class Circle extends AbstractShape {
	public static enum RadiusType {Miles, Pixels};
	
	/** The center location in Geo coordinates */
	protected GeoPosition center;	
	
	/** The radius type */
	protected RadiusType radiusType;
	
	/** The radius (in miles or pixels) */
	protected double radius;
	
	/** The circle in Geo coordinates */
	protected Ellipse2D.Double circle;
	
	/** The circle in world pixel-space */
	protected Ellipse2D.Double circle_pixels;	
	
	/**
	 * @param center
	 * @param radius
	 * @param radiusType
	 */
	public Circle(GeoCoordinate center, double radius, RadiusType radiusType) {
		this(new GeoPosition(center.getLat(), center.getLon()), radius, radiusType);
	}
	
	/**
	 * @param center
	 * @param radius
	 * @param radiusType
	 */
	public Circle(GeoPosition center, double radius, RadiusType radiusType) {
		this.center = center;
		this.radiusType = radiusType;
		this.radius = radius;
		//double radius_degrees = radiusType == RadiusType.Degrees ? radius : 0d;
		double radius_degrees = radiusType == RadiusType.Miles ? computeRadiusDegreesLongitude(radius) : 0d;
		double radius_pixels = radiusType == RadiusType.Pixels ? radius : 0d;
		circle = new Ellipse2D.Double(center.getLongitude() - radius_degrees, center.getLatitude() - radius_degrees, 
				radius_degrees * 2, radius_degrees * 2);
		circle_pixels = new Ellipse2D.Double(center.getLongitude() - radius_pixels, center.getLatitude() - radius_pixels, 
				radius_pixels * 2, radius_pixels * 2);
		objectBoundsChanged = true;
	}
	
	/** Compute the circle radius in degrees of longitude given a radius in miles */
	protected double computeRadiusDegreesLongitude(double radius_miles) {
		return radius_miles / GeoUtils.metersToMiles(GeoUtils.computeLengthOfDegreeOfLongitude_meters(center.getLatitude()));
	}
	
	/**
	 * 
	 * @return the circle radius in degrees of longitude
	 */
	public double getRadius_degrees() {
		return circle.getWidth()/2;
	}
	
	/**
	 * 
	 * @return the circle radius in pixels
	 */
	public double getRadius_pixels() {
		return circle_pixels.getWidth()/2;
	}
	
	/**
	 * @return the radiusType
	 */
	public RadiusType getRadiusType() {
		return radiusType;
	}
	
	/**
	 * Get the radius.
	 * 
	 * @return the radius in miles or pixels
	 */
	public double getRadius() {
		return radius;
	}

	/**
	 * Set the radius.
	 * 
	 * @param radius the radius in miles or pixels
	 */
	public void setRadius(double radius) {
		this.radius = radius;
		//double radius_degrees = radiusType == RadiusType.Degrees ? radius : 0d;
		double radius_degrees = radiusType == RadiusType.Miles ? computeRadiusDegreesLongitude(radius) : 0d;
		double radius_pixels = radiusType == RadiusType.Pixels ? radius : 0d;
		circle.width = radius_degrees * 2;
		circle.height = circle.width;
		circle_pixels.width = radius_pixels * 2;
		circle_pixels.height = circle_pixels.width;
		objectBoundsChanged = true;
		//shapeEdited = true;
	}	
	
	@Override
	public void setEditable(boolean editable) {
		super.setEditable(editable);
		this.controlPointsVisible = editable;
	}

	@Override
	public void setEditing(boolean editing) {
		if(editing != this.editing) {
			super.setEditing(editing);
			if(editing) {
				setBorderLineWidth(getBorderLineWidth() + 2);
			} else {	
				setBorderLineWidth(getBorderLineWidth() - 2);
			}
		}
	}	
	
	/**
	 * @return
	 */
	public Point2D getCenterPixelLocation() {
		return new Point2D.Double(circle_pixels.x, circle_pixels.y);
	}

	@Override
	public Ellipse2D getPixelShape() {
		return circle_pixels;
	}

	@Override
	public Point2D getCenterGeoLocation() {
		return new Point2D.Double(center.getLongitude(), center.getLatitude());
	}
	
	public void setCenterGeoLocation(GeoPosition centerGeoLocation) {
		setCenterGeoLocation(centerGeoLocation.getLongitude(), centerGeoLocation.getLatitude());
	}
	
	public void setCenterGeoLocation(Point2D centerGeoLocation) {
		setCenterGeoLocation(centerGeoLocation.getX(), centerGeoLocation.getY());
	}	
	
	public void setCenterGeoLocation(double longitude, double latitude) {
		center = new GeoPosition(latitude, longitude);
		if(radiusType == RadiusType.Miles) {
			//Re-compute the radius in degrees at the new latitude
			double radius_degrees = computeRadiusDegreesLongitude(radius);
			circle.width = radius_degrees * 2;
			circle.height = circle.width;
		}
		objectBoundsChanged = true;
	}

	@Override
	public Ellipse2D getGeoShape() {
		return circle;
	}	

	@Override
	public Collection<ControlPoint> getControlPoints() {		
		return Collections.singleton(new ControlPoint(circle_pixels.getCenterX(), circle_pixels.getCenterY(), 0, 
				ControlPointType.Center));
	}
	
	/**
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean centerControlPointContainsPixelLocation(Point2D location) {
		return (location.getX() >= circle_pixels.getCenterX() - controlPointRadius && 
				location.getX() <= circle_pixels.getCenterX() + controlPointRadius &&
				location.getY() >= circle_pixels.getCenterY() - controlPointRadius &&
				location.getY() <= circle_pixels.getCenterY() + controlPointRadius);
	}
	
	@Override
	public void render(Graphics2D g, RenderProperties renderProperties, boolean renderPropertiesChanged) {
		Color origColor = g.getColor();
		Stroke origStroke = g.getStroke();		
		
		if(objectBoundsChanged || renderPropertiesChanged || circle_pixels.getWidth() == 0) {
			if(radiusType == RadiusType.Pixels) {
				//Update the radius (in degrees) of the Geo circle
				circle.width = renderProperties.pixelsToDegrees(circle_pixels.width);
				circle.height = circle.width;	
				circle.x = center.getLongitude() - circle.width / 2;
				circle.y = center.getLatitude() - circle.width / 2;
			} else {
				//the radius is in degrees and must be converted to pixels
				double diameter_pixels = renderProperties.degreesToPixels(circle.width);
				circle_pixels.width = diameter_pixels;
				circle_pixels.height = circle_pixels.width;
			}
			Point2D centerLocation_pixels = renderProperties.geoPositionToWorldPixel(center);
			double radius_pixels = circle_pixels.width / 2;
			circle_pixels.x = centerLocation_pixels.getX() - radius_pixels;
			circle_pixels.y = centerLocation_pixels.getY() - radius_pixels;
			objectBoundsChanged = false;
			//System.out.println("drawing circle " +  id + "  at: " + centerLocation_pixels + ", radius: " + radius_pixels);
		}		
		
		//Set the transparency
		Composite origComposite = null;
		if(transparency < 1.f) {
			origComposite = g.getComposite();
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency));
		}
		
		//Fill the circle
		if(backgroundColor != null) {
			g.setColor(backgroundColor);
			g.fill(circle_pixels);
		}
		
		//Draw the circle outline
		if(borderLineStyle != null) {
			if(borderColor == null) {
				borderColor = Color.BLACK;
			}
			g.setColor(borderColor);
			g.setStroke(borderLineStyle.getLineStroke());
			g.draw(circle_pixels);
		}
		
		//Draw the control point at the center of the circle
		if(controlPointsVisible) {
			g.setStroke(origStroke);
			renderControlPoint(g, circle_pixels.getCenterX(), circle_pixels.getCenterY());
		}
		
		g.setColor(origColor);
		g.setStroke(origStroke);
		if(origComposite != null) {
			g.setComposite(origComposite);
		}		
		objectBoundsChanged = false;
	}
}