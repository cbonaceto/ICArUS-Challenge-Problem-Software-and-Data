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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Collection;

import org.mitre.icarus.cps.app.widgets.map.LineStyle;

/**
 * Base class for shape map objects (e.g., circles, polygons, lines, etc.). Shapes may have a border,
 * a foreground color, a background color, and zero or more "control points" (e.g., points at line segment
 * intersections or polygon vertices, a circle center) that may be rendered.
 * 
 * @author CBONACETO
 *
 */
public abstract class AbstractShape extends AbstractMapObject_Phase2 {
	
	/** The foreground color */
	protected Color foregroundColor = Color.black;
	
	/** The background color */
	protected Color backgroundColor;
	
	/** The border color */
	protected Color borderColor;
	
	/** The border line style */
	protected LineStyle borderLineStyle;
	
	/** Whether to show the control points */
	protected boolean controlPointsVisible;
	
	/** The control point fill color */
	protected Color controlPointBackgroundColor = Color.gray;
	
	/** The control point border color */
	protected Color controlPointBorderColor = Color.black;
	
	/** The control point radius for rendering control points (in pixels) */
	protected static int controlPointRadius = 5;
	
	/** The total amount of time spent editing the shape */
	protected Long editTime;
	
	protected AbstractShape() {
		objectBoundsChanged = true;
	}
	
	/** Copy constructor */
	protected AbstractShape(AbstractShape copy) {
		this.foregroundColor = copy.foregroundColor;	
		this.backgroundColor = copy.backgroundColor;		
		this.borderColor = copy.borderColor;		
		this.borderLineStyle = copy.borderLineStyle;
		objectBoundsChanged = true;
	}
	
	@Override
	public void setMouseOverState(boolean mouseOver, MouseEvent event) {
		//Does nothing
	}

	public Color getForegroundColor() {
		return foregroundColor;
	}

	public void setForegroundColor(Color foregroundColor) {
		this.foregroundColor = foregroundColor;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public Color getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
	}

	public float getBorderLineWidth() {
		if(borderLineStyle != null) {
			return borderLineStyle.getLineWidth();
		}
		return 0;
	}
	
	public LineStyle getBorderLineStyle() {
		return borderLineStyle;
	}

	public void setBorderLineStyle(LineStyle borderLineStyle) {
		this.borderLineStyle = borderLineStyle;
	}	

	public void setBorderLineWidth(float lineWidth) {
		if(lineWidth <= 0.f) {
			borderLineStyle = null;
		} else {
			if(borderLineStyle == null) {
				borderLineStyle = new LineStyle(lineWidth);
			} else {
				borderLineStyle.setLineWidth(lineWidth);
			}
		}
	}	
		
	//public abstract double getArea_pixels();
	
	/**
	 * @return
	 */
	@Override
	public abstract Shape getPixelShape();	
	
	public boolean isControlPointsVisible() {
		return controlPointsVisible;
	}

	public void setControlPointsVisible(boolean controlPointsVisible) {
		this.controlPointsVisible = controlPointsVisible;
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

	public static int getControlPointRadius() {
		return controlPointRadius;
	}

	/**
	 * Set the control point radius in pixels.
	 * 
	 * @param controlPointRadius
	 */
	public static void setControlPointRadius(int controlPointRadius) {
		AbstractShape.controlPointRadius = controlPointRadius;
	}

	public Long getEditTime() {
		return editTime;
	}

	public void setEditTime(Long editTime) {
		this.editTime = editTime;
	}

	/**
	 * @return
	 */
	public abstract Collection<? extends ControlPoint> getControlPoints();
	
	/**
	 * @param point
	 * @return
	 */
	public ControlPoint findControlPointContains(Point2D point) {
		return findControlPointContains(point.getX(), point.getY());
	}
	
	/**
	 * @param x
	 * @param y
	 * @return
	 */
	public ControlPoint findControlPointContains(double x, double y) {
		Collection<? extends ControlPoint> controlPoints = getControlPoints();
		if(controlPoints != null && !controlPoints.isEmpty()) {
			for(ControlPoint cp : controlPoints) {
				if(x >= cp.x - controlPointRadius && x <= cp.x + controlPointRadius &&
						y >= cp.y - controlPointRadius && y <= cp.y + controlPointRadius) {
					return cp;
				}
			}
		}
		return null;
	}
	
	protected void renderControlPoints(Graphics2D g) {
		Collection<? extends ControlPoint> controlPoints = getControlPoints();
		if(controlPoints != null && !controlPoints.isEmpty()) {
			for(ControlPoint cp : controlPoints) {
				renderControlPoint(g, cp.getX(), cp.getY());
			}
		}
	}
	
	protected void renderControlPoint(Graphics2D g, ControlPoint controlPoint) {
		renderControlPoint(g, controlPoint.getX(), controlPoint.getY());
	}
	
	protected void renderControlPoint(Graphics2D g, double x, double y) {
		x = x - controlPointRadius;
		y = y - controlPointRadius;
		if(controlPointBackgroundColor != null) {
			g.setColor(controlPointBackgroundColor);
			g.fillOval((int)x, (int)y, controlPointRadius*2, controlPointRadius*2);
		}
		if(controlPointBorderColor != null) {			
			g.setColor(controlPointBorderColor);
			g.drawOval((int)x, (int)y, controlPointRadius*2, controlPointRadius*2);
		}
	}
}