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
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.mitre.icarus.cps.app.widgets.map.phase_2.RenderProperties;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.ControlPoint.ControlPointType;
import org.mitre.icarus.cps.feature_vector.phase_2.coordinate.GeoCoordinate;

/**
 * PolyLine map object. A polyline is a series of connected line segments.
 * 
 * @author CBONACETO
 *
 */
public class Polyline extends AbstractShape {
	
	/** The line segment vertices in Geo coordinates */
	protected List<GeoPosition> vertices;
	
	/** The line in Geo coordinates */
	protected Path2D polyline;
	
	/** The line in world pixel-space */
	protected Path2D polyline_pixels;
	
	/** The polyline control points (vertices) in world pixel space */
	protected List<ControlPoint> controlPoints;	
	
	/**
	 * @param vertices
	 */
	public Polyline(List<GeoCoordinate> vertices) {
		if(vertices == null || vertices.size() < 2) {
			throw new IllegalArgumentException("Error creating polyline: must have 2 or more vertices");
		}
		this.vertices = new ArrayList<GeoPosition>(vertices.size());
		polyline = new Path2D.Float();
		int i = 0;
		for(GeoCoordinate vertex : vertices) {
			this.vertices.add(new GeoPosition(vertex.getLat(), vertex.getLon()));
			if(i == 0) {
				polyline.moveTo(vertex.getLon(), vertex.getLat());
			} else {
				polyline.lineTo(vertex.getLon(), vertex.getLat());
			}
			i++;
		}
	}

	@Override
	public Point2D getCenterPixelLocation() {
		if(polyline_pixels != null) {
			Rectangle2D bounds = polyline.getBounds2D();
			return new Point2D.Double(bounds.getCenterX(), bounds.getCenterY()); 
		}
		return null;
	}

	@Override
	public Point2D getCenterGeoLocation() {
		if(polyline != null) {
			Rectangle2D bounds = polyline.getBounds2D();
			return new Point2D.Double(bounds.getCenterX(), bounds.getCenterY()); 
		}
		return null;
	}

	@Override
	public Path2D getGeoShape() {
		return polyline;
	}	

	@Override
	public Path2D getPixelShape() {
		return polyline_pixels;
	}
	
	/**
	 * @param renderProperties
	 * @param renderPropertiesChanged
	 * @return
	 */
	public Path2D getPixelShape(RenderProperties renderProperties, boolean renderPropertiesChanged) {
		if(objectBoundsChanged || renderPropertiesChanged || polyline_pixels == null) {
			//Create a new polyline in world coordinate pixel-space
			if(controlPoints == null) {
				controlPoints = new LinkedList<ControlPoint>();
			}
			polyline_pixels = createPixelShape(renderProperties, controlPoints);
			objectBoundsChanged = false;
		}
		return polyline_pixels;
	}
	
	protected Path2D createPixelShape(RenderProperties renderProperties, List<ControlPoint> controlPoints) {
		Path2D polyline_pixels = new Path2D.Float();
		if(controlPoints != null) {
			controlPoints.clear();
		}
		int i = 0;
		for(GeoPosition vertex : vertices) {
			Point2D currPixelLocation = renderProperties.geoPositionToWorldPixel(vertex);
			if(controlPoints != null) {
				controlPoints.add(new ControlPoint(currPixelLocation.getX(), currPixelLocation.getY(), i, ControlPointType.Vertex));
			}
			if(i == 0) {
				polyline_pixels.moveTo(currPixelLocation.getX(), currPixelLocation.getY());
			} else {
				polyline_pixels.lineTo(currPixelLocation.getX(), currPixelLocation.getY());
			}
			i++;
		}
		return polyline_pixels;
	}

	@Override
	public Collection<ControlPoint> getControlPoints() {
		return controlPoints;
	}
	
	@Override
	public void render(Graphics2D g, RenderProperties renderProperties, boolean renderPropertiesChanged) {		
		if(objectBoundsChanged || renderPropertiesChanged || polyline_pixels == null) {
			//Create a new polygon in world coordinate pixel-space
			if(controlPoints == null) {
				controlPoints = new LinkedList<ControlPoint>();
			}
			polyline_pixels = createPixelShape(renderProperties, controlPoints);
			objectBoundsChanged = false;
		}
		
		Color origColor = g.getColor();
		Stroke origStroke = g.getStroke();

		//Set the transparency
		Composite origComposite = null;
		if(transparency < 1.f) {
			origComposite = g.getComposite();
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency));
		}		

		//Draw the polyline
		if(borderLineStyle != null) {
			if(borderColor == null) {
				borderColor = Color.BLACK;
			}
			g.setColor(borderColor);
			g.setStroke(borderLineStyle.getLineStroke());
			g.draw(polyline_pixels);
		}

		//Draw the control points
		if(controlPointsVisible && controlPoints != null) {
			for(ControlPoint cp : controlPoints) {
				renderControlPoint(g, cp);
			}
		}

		g.setColor(origColor);
		g.setStroke(origStroke);
		if(origComposite != null) {
			g.setComposite(origComposite);
		}
	}
}