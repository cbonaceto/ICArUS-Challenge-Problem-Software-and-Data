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

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

import org.mitre.icarus.cps.app.widgets.map.phase_1.RenderProperties;
import org.mitre.icarus.cps.feature_vector.phase_1.GridLocation2D;

public class CircleShape extends ShapeMapObject implements IAnnotationMapObject, Cloneable {	
	
	public static enum RadiusType {Radius_GridUnits, Radius_Pixels};
	
	/** The circle radius (in pixels) */
	protected double radius;
	
	protected RadiusType radiusType;
	
	/** Whether to show the control points */
	protected boolean controlPointsVisible;
	
	/** The circle shape renderable */
	protected Ellipse2D.Double circle;
	
	/** The control point renderable at the circle center */
	protected Ellipse2D.Double centerControlPoint;	
	
	/** The control point fill color */
	protected Color controlPointBackgroundColor = Color.gray;
	
	/** The control point border color */
	protected Color controlPointBorderColor = Color.black;
	
	/** The control point radius (in pixels) */
	protected static int controlPointRadius = 5;
	
	/** The total amount of time spent editing the circle */
	protected long editTime = 0;
	
	public CircleShape(double radius, RadiusType radiusType) {
		this(radius, radiusType, null);
	}
	
	public CircleShape(double radius, RadiusType radiusType, GridLocation2D centerLocation) {
		this.radius = radius;
		this.radiusType = radiusType;
		circle = new Ellipse2D.Double();
		selectable = true;
		renderShape = circle;
		centerControlPoint = new Ellipse2D.Double();		
		objectBoundsChanged = true;
		if(centerLocation != null) {
			setCenterLocation(centerLocation);
		}
	}
	
	/** Copy constructor */
	public CircleShape(CircleShape copy) {
		super(copy);
		this.radius = copy.radius;		
		this.radiusType = copy.radiusType;
		this.controlPointsVisible = copy.controlPointsVisible;
		if(copy.circle != null) {
			this.circle = new Ellipse2D.Double(copy.circle.x, copy.circle.y, 
					copy.circle.width, copy.circle.height);
		}
		if(copy.centerControlPoint != null) {
			this.centerControlPoint = new Ellipse2D.Double(copy.centerControlPoint.x, copy.centerControlPoint.y, 
					copy.centerControlPoint.width, copy.centerControlPoint.height);
		}
		this.controlPointBackgroundColor = copy.controlPointBackgroundColor;
		this.controlPointBorderColor = copy.controlPointBorderColor;
		this.editTime = copy.editTime;
	}

	@Override
	public double getArea_pixels() {
		if(circle != null) {
			return Math.PI * Math.pow(circle.getWidth()/2, 2);
		}	
		return 0;
	}	

	@Override
	public Point2D getCenterLocation_pixels() {
		if(circle != null) {
			return new Point2D.Double(circle.getCenterX(), circle.getCenterY());
		}
		return null;
	}

	public double getRadius() {
		return this.radius;
	}
	
	public double getRadius_pixels() {
		if(circle != null) {
			return circle.getWidth()/2;
		}
		return 0;
	}
	
	public void setRadius(double radius) {
		this.radius = radius;
		objectBoundsChanged = true;
		shapeEdited = true;
	}
	
	/**
	 * @return the radiusType
	 */
	public RadiusType getRadiusType() {
		return radiusType;
	}		

	public Ellipse2D getCircle() {
		return circle;
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

	public Color getControlPointBackgroundColor() {
		return controlPointBackgroundColor;
	}

	public void setControlPointBackgroundColor(Color controlPointBackgroundColor) {
		this.controlPointBackgroundColor = controlPointBackgroundColor;
	}

	public Color getControlPointBorderColor() {
		return controlPointBorderColor;
	}

	public void setControlPointBorderColor(Color controlPointBorderColor) {
		this.controlPointBorderColor = controlPointBorderColor;
	}

	public boolean centerControlPointContains(double x, double y) {
		if(centerControlPoint != null) {
			return centerControlPoint.contains(x, y);
		}
		return false;
	}	

	@Override
	public GridLocation2D getAnnotationLineStartLocation(AnnotationOrientation orientation) {		
		return null;
	}
	
	public long getEditTime() {
		return editTime;
	}

	public void setEditTime(long editTime) {
		this.editTime = editTime;
	}

	@Override
	public void render(Graphics2D g2d, RenderProperties renderData, boolean renderPropertiesChanged) {		
		if(radius <=0) {
			return;
		}
		
		Color origColor = g2d.getColor();
		Stroke origStroke = g2d.getStroke();
		
		if(objectBoundsChanged || renderPropertiesChanged || circle.getWidth() == 0) {			
			double radius_pixels = 0.0;		
			if(radiusType == RadiusType.Radius_Pixels) {
				radius_pixels = radius;
			}
			else {
				//the radius is internally stored as a grid unit and must be converted
				radius_pixels = radius * renderData.getPixelsPerGridUnit();
			}		
			Point2D centerLocation_pixels = renderData.translateToPixel(this.centerLocation);
			circle.setFrame(centerLocation_pixels.getX() - radius_pixels, 
					centerLocation_pixels.getY() - radius_pixels, 
					radius_pixels*2, radius_pixels*2);				
		} 
		
		//Set the transparency
		Composite origComposite = null;
		if(transparency < 1.f) {
			origComposite = g2d.getComposite();
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency));
		}
		
		if(backgroundColor != null) {
			g2d.setColor(backgroundColor);
			g2d.fill(circle);
		}
		
		if(borderLineStyle != null) {			
			if(borderColor == null) {
				borderColor = Color.black;
			}
			g2d.setColor(borderColor);
			g2d.setStroke(getBorderLineStyle().getLineStroke());
			g2d.draw(circle);
		}
		
		centerControlPoint.setFrame(circle.getCenterX() - controlPointRadius, 
				circle.getCenterY() - controlPointRadius, 
				controlPointRadius*2, controlPointRadius*2);	
		
		if(controlPointsVisible) {		
			if(controlPointBackgroundColor != null) {
				g2d.setColor(controlPointBackgroundColor);
				g2d.fill(centerControlPoint);
			}
			if(controlPointBorderColor != null) {
				g2d.setStroke(origStroke);
				g2d.setColor(controlPointBorderColor);
				g2d.draw(centerControlPoint);
			}
		}		
		
		g2d.setColor(origColor);
		g2d.setStroke(origStroke);
		if(origComposite != null) {
			g2d.setComposite(origComposite);
		}		
		objectBoundsChanged = false;
	}

	@Override
	public void setMouseOverState(boolean mouseOver, MouseEvent event) {}

	@Override
	protected CircleShape clone() throws CloneNotSupportedException {
		return new CircleShape(this);
	}
}