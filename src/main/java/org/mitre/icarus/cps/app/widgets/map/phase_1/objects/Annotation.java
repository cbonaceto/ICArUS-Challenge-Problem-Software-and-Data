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
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import org.mitre.icarus.cps.app.widgets.map.LineStyle;
import org.mitre.icarus.cps.app.widgets.map.phase_1.MapConstants_Phase1;
import org.mitre.icarus.cps.app.widgets.map.phase_1.RenderProperties;
import org.mitre.icarus.cps.app.widgets.map.phase_1.layers.ILayer_Phase1;
import org.mitre.icarus.cps.app.widgets.map.phase_1.objects.IAnnotationMapObject.AnnotationOrientation;
import org.mitre.icarus.cps.feature_vector.phase_1.GridLocation2D;

/**
 * @author CBONACETO
 *
 */
public class Annotation extends AbstractMapObject_Phase1 {	
	
	/** The parent map object that is being annotated */
	protected final IAnnotatableMapObject parent;
	
	/** The Map object that is the annotation */
	protected final IAnnotationMapObject annotation;
	
	/** The orientation of the annotation relative to its parent map object */
	protected final AnnotationOrientation orientation;
	
	/** Whether to show the annotation line */
	protected boolean showAnnotationLine = true;
	
	/** The annotation line color */
	protected Color annotationLineColor = MapConstants_Phase1.ANNOTATION_LINE_COLOR;
	
	/** The annotation line style */
	protected LineStyle annotationLineStyle = MapConstants_Phase1.ANNOTATION_LINE_STYLE;
	
	/** The number of pixels to offset the annotation from its parent */
	protected int offset_pixels = MapConstants_Phase1.ANNOTATION_OFFSET_PIXELS;
	
	/** whether or not the map object is selectable */
	private boolean selectable = false;
	
	/** The layer this annotation is contained in */
	protected ILayer_Phase1<? super Annotation> layer;
	
	protected Double parentXLocation;
	protected Double parentYLocation;
	
	public Annotation(IAnnotatableMapObject parent, final IAnnotationMapObject annotation,
			final AnnotationOrientation orientation) {
		this.parent = parent;
		this.annotation = annotation;
		this.orientation = orientation;
		setId(annotation.getId());
	}	

	@Override
	public ILayer_Phase1<? super Annotation> getLayer() {
		return layer;
	}
	
	@SuppressWarnings("unchecked")
	public void setLayer(ILayer_Phase1<? extends IMapObject_Phase1> layer) {
		super.setLayer(layer);
		try {
			this.layer = (ILayer_Phase1<? super Annotation>) layer;
		} catch(Exception ex) {
			throw new IllegalArgumentException("Error, the annotation must be contained in an annotation layer");
		}
	}

	public IAnnotatableMapObject getParent() {
		return parent;
	}	

	public IAnnotationMapObject getAnnotation() {
		return annotation;
	}

	public AnnotationOrientation getOrientation() {
		return orientation;
	}	

	public boolean isShowAnnotationLine() {
		return showAnnotationLine;
	}

	public void setShowAnnotationLine(boolean showAnnotationLine) {
		this.showAnnotationLine = showAnnotationLine;
	}

	public Color getAnnotationLineColor() {
		return annotationLineColor;
	}

	public void setAnnotationLineColor(Color annotationLineColore) {
		this.annotationLineColor = annotationLineColore;
	}

	public LineStyle getAnnotationLineStyle() {
		return annotationLineStyle;
	}

	public void setAnnotationLineStyle(LineStyle annotationLineStyle) {
		this.annotationLineStyle = annotationLineStyle;
	}

	/**
	 * @return the offest_gridUnits
	 */
	public int getOffset_pixels() {
		return this.offset_pixels;
	}

	/**
	 * @param offest_gridUnits the offest_gridUnits to set
	 */
	public void setOffset_pixels(int offset_pixels) {
		this.offset_pixels = offset_pixels;
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
		if(annotation != null) {
			return annotation.contains(x, y);
		}
		return false;
	}
	
	public static double getHeadingRadians(AnnotationOrientation orientation) {
		double angle;
		
		if(orientation == AnnotationOrientation.East) {
			angle = 0.0;
		}
		else if(orientation == AnnotationOrientation.NorthEast) {
			angle = Math.PI / 4.0;
		}
		else if(orientation == AnnotationOrientation.North) {
			angle = Math.PI / 2.0;
		}
		else if(orientation == AnnotationOrientation.NorthWest) {
			angle = 3.0 * Math.PI / 4.0;
		}
		else if(orientation == AnnotationOrientation.West) {
			angle = Math.PI;
		}
		else if(orientation == AnnotationOrientation.SouthWest) {
			angle = 5.0 * Math.PI / 4.0;
		}
		else if(orientation == AnnotationOrientation.South) {
			angle = 3.0 * Math.PI / 2.0;
		}
		else {
			angle = 7.0 * Math.PI / 4.0;
		}
		
		return angle;
	}

	@Override
	public boolean isSelectable() {
		return selectable;
	}

	@Override
	public void setSelectable(boolean selectable) {
		this.selectable = selectable;
	}

	@Override
	public void setMouseOverState(boolean mouseOver, MouseEvent event) {		
	}

	@Override
	public void render(Graphics2D g2d, RenderProperties renderData, boolean renderPropertiesChanged) {
		
		//Don't render the annotation if its parent isn't visible
		if(!parent.isVisible() || (parent.getLayer() != null && !parent.getLayer().isVisible())) {
			return;
		}
		
		Color origColor = g2d.getColor();
		Stroke origStroke = g2d.getStroke();
		
		Double parentXLocation = parent.getCenterLocation().getX();
		Double parentYLocation = parent.getCenterLocation().getY();
		if(renderPropertiesChanged || this.parentXLocation == null || this.parentYLocation == null ||
				parentXLocation != this.parentXLocation || parentYLocation != this.parentYLocation) {
			//Update the annotation center location
			this.parentXLocation = parentXLocation;
			this.parentYLocation = parentYLocation;
			if(orientation == AnnotationOrientation.Center || orientation == AnnotationOrientation.Center_1) {
				// case where the annotation orientation is that the center
				annotation.setCenterLocation(new GridLocation2D(parentXLocation, parentYLocation));
			} else {			
				// case where the annotation orientation is not at the center
				double angle = getHeadingRadians(orientation);			
				double offset_gridUnits = renderData.translateToGridUnits(offset_pixels);			
				double annotationXLocation = parent.getCenterLocation().getX() 
						+ offset_gridUnits * Math.cos(angle);			
				double annotationYLocation = parent.getCenterLocation().getY() 
						+ offset_gridUnits * Math.sin(angle);
				annotation.setCenterLocation(new GridLocation2D(annotationXLocation, annotationYLocation));
			}
		}
		
		//Render the annotation
		annotation.render(g2d, renderData, renderPropertiesChanged);
		
		//Render the annotation line if the annotation is not at the center orientation
		if(showAnnotationLine && orientation != AnnotationOrientation.Center &&  orientation != AnnotationOrientation.Center_1) {
			Point2D startLocationPixels = renderData.translateToPixel(annotation.getAnnotationLineStartLocation(orientation));
			Point2D endLocationPixels = renderData.translateToPixel(parent.getAnnotationLineEndLocation(orientation));
			Line2D line = new Line2D.Double(startLocationPixels, endLocationPixels);

			if(getAnnotationLineStyle() != null) {
				g2d.setStroke(getAnnotationLineStyle().getLineStroke());
			}
			if(annotationLineColor == null) {
				annotationLineColor = Color.black;
			}
			g2d.setColor(annotationLineColor);
			g2d.draw(line);
		}
		
		g2d.setColor(origColor);
		g2d.setStroke(origStroke);
	}		
}