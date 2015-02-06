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
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.mitre.icarus.cps.app.widgets.map.LineStyle;
import org.mitre.icarus.cps.feature_vector.phase_1.GridLocation2D;

public abstract class ShapeMapObject extends AbstractMapObject_Phase1 {
	
	/** The shape center location (in grid units) */
	protected GridLocation2D centerLocation;
	
	/** The foreground color */
	protected Color foregroundColor = Color.black;
	
	/** The background color */
	protected Color backgroundColor;
	
	/** The border color */
	protected Color borderColor;
	
	/** The border line style */
	protected LineStyle borderLineStyle;
	
	protected Shape renderShape;
	
	protected boolean shapeEdited = false;
	
	protected ShapeMapObject() {}
	
	/** Copy constructor */
	protected ShapeMapObject(ShapeMapObject copy) {
		if(copy.centerLocation != null) {
			this.centerLocation = new GridLocation2D(copy.centerLocation);
		}
		this.foregroundColor = copy.foregroundColor;	
		this.backgroundColor = copy.backgroundColor;		
		this.borderColor = copy.borderColor;		
		this.borderLineStyle = copy.borderLineStyle;		
		this.renderShape = copy.renderShape;		
		this.shapeEdited = copy.shapeEdited;
	}

	public GridLocation2D getCenterLocation() {
		return centerLocation;
	}

	public void setCenterLocation(GridLocation2D centerLocation) {
		this.centerLocation = centerLocation;
		objectBoundsChanged = true;
		shapeEdited = true;
	}
	
	public boolean isShapeEdited() {
		return shapeEdited;
	}

	public void setShapeEdited(boolean shapeEdited) {
		this.shapeEdited = shapeEdited;
	}

	public void locationChanged() {
		objectBoundsChanged = true;
		shapeEdited = true;
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

	public void setBorderLineWidth(float lineWidth) {
		if(lineWidth <= 0.f) {
			borderLineStyle = null;
		} else {
			if(borderLineStyle == null) {
				borderLineStyle = new LineStyle(lineWidth);
			}
			else {
				borderLineStyle.setLineWidth(lineWidth);
			}
		}
	}	
	
	public abstract double getArea_pixels();
	
	public abstract Point2D getCenterLocation_pixels();
	
	public Rectangle2D getBounds() {
		if(renderShape != null) {
			return renderShape.getBounds2D();
		}
		return null;
	}
	
	public LineStyle getBorderLineStyle() {
		return borderLineStyle;
	}

	public void setBorderLineStyle(LineStyle borderLineStyle) {
		this.borderLineStyle = borderLineStyle;
	}	

	@Override
	public boolean contains(Point point) {
		return contains(point.x, point.y);
	}

	@Override
	public boolean contains(Point2D point) {
		return contains(point.getX(), point.getY());
	}

	@Override
	public boolean contains(double x, double y) {
		if(renderShape != null) {
			return renderShape.contains(x, y);
		}
		return false;
	}
}