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
package org.mitre.icarus.cps.app.widgets.map.phase_2.objects.annotation;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import org.mitre.icarus.cps.app.widgets.map.LineStyle;
import org.mitre.icarus.cps.app.widgets.map.phase_2.MapConstants_Phase2;
import org.mitre.icarus.cps.app.widgets.map.phase_2.MapPanel_Phase2;
import org.mitre.icarus.cps.app.widgets.map.phase_2.RenderProperties;
import org.mitre.icarus.cps.app.widgets.map.phase_2.layers.ILayer_Phase2;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.AbstractMapObject_Phase2;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.IMapObject_Phase2;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.annotation.IAnnotationMapObject.AnnotationOrientation;

/**
 * Contains a reference to an annotatable map object and its annotation. Manages rendering of the annotation for the
 * parent annotatable map object.
 * 
 * @author CBONACETO
 *
 */
public class Annotation extends AbstractMapObject_Phase2 {	
	
	/** The parent map object that is being annotated */
	protected final IAnnotatableMapObject parent;
	
	/** The Map object that is the annotation */
	protected final IAnnotationMapObject annotation;
	
	/** The orientation of the annotation relative to its parent map object */
	protected final AnnotationOrientation orientation;
	
	/** Whether to show the annotation line */
	protected boolean showAnnotationLine = true;
	
	/** The annotation line color */
	protected Color annotationLineColor = MapConstants_Phase2.ANNOTATION_LINE_COLOR;
	
	/** The annotation line style */
	protected LineStyle annotationLineStyle = MapConstants_Phase2.ANNOTATION_LINE_STYLE;
	
	/** The number of pixels to offset the annotation from its parent */
	protected int offset_pixels = MapConstants_Phase2.ANNOTATION_OFFSET_PIXELS;
	
	/** The layer this annotation is contained in */
	protected ILayer_Phase2<? super Annotation, ? extends MapPanel_Phase2> layer;
	
	/** The center location of the parent map object (in geo coordinates) */
	protected Point2D parentLocation;
	//protected Double parentXLocation;
	//protected Double parentYLocation;
	
	public Annotation(IAnnotatableMapObject parent, IAnnotationMapObject annotation,
			AnnotationOrientation orientation) {
		this.parent = parent;
		this.annotation = annotation;
		this.orientation = orientation;
		setId(annotation.getId());
	}	

	@Override
	public ILayer_Phase2<? super Annotation, ? extends MapPanel_Phase2> getLayer() {
		return layer;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void setLayer(ILayer_Phase2<? extends IMapObject_Phase2, ? extends MapPanel_Phase2> layer) {
		super.setLayer(layer);
		try {
			this.layer = (ILayer_Phase2<? super Annotation, ? extends MapPanel_Phase2>) layer;
		} catch(Exception ex) {
			throw new IllegalArgumentException("Error, the annotation must be contained in an annotation layer");
		}
	}
	
	@Override
	public Point2D getCenterPixelLocation() {
		if(annotation != null) {
			return annotation.getCenterPixelLocation();
		}
		return null;
	}

	@Override
	public Point2D getCenterGeoLocation() {
		if(annotation != null) {
			return annotation.getCenterGeoLocation();
		}
		return null;
	}

	@Override
	public Shape getGeoShape() {
		if(annotation != null) {
			return annotation.getGeoShape();
		}
		return null;
	}

	@Override
	public Shape getPixelShape() {
		return annotation != null ? annotation.getPixelShape() : null;
	}

	@Override
	public boolean containsPixelLocation(Point2D location) {
		if(annotation != null) {
			return annotation.containsPixelLocation(location);
		}
		return false;
	}

	@Override
	public boolean containsPixelLocation(double x, double y) {
		if(annotation != null) {
			return annotation.containsPixelLocation(x, y);
		}
		return false;
	}

	@Override
	public void setMouseOverState(boolean mouseOver, MouseEvent event) {}

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
	
		

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.gui.map.phase_2.objects.IMapObject_Phase2#render(java.awt.Graphics2D, org.mitre.icarus.cps.gui.map.phase_2.RenderProperties, boolean)
	 */
	@Override
	public void render(Graphics2D g, RenderProperties renderProperties, boolean renderPropertiesChanged) {		
		//Don't render the annotation if its parent isn't visible
		if(!parent.isVisible() || (parent.getLayer() != null && !parent.getLayer().isVisible())) {
			return;
		}
		
		Color origColor = g.getColor();
		Stroke origStroke = g.getStroke();
		
		//Position the annotation map object based on its orientation to the parent map object
		Point2D parentLocation = parent.getCenterGeoLocation();
		Point2D endLocationPixels = parent.getAnnotationLineEndPixelLocation(orientation);
		if(renderPropertiesChanged || this.parentLocation == null ||
				parentLocation.getX() != this.parentLocation.getX() || 
				parentLocation.getY() != this.parentLocation.getY()) {
			//Update the annotation center location
			this.parentLocation = parentLocation;			
			if(orientation == AnnotationOrientation.Center || orientation == AnnotationOrientation.Center_1) {
				//Case where the annotation orientation is that the center				
				annotation.setCenterGeoLocation(parentLocation);	
			} else {	
				//Case where the annotation orientation is not at the center
				//TODO: Test this
				annotation.setCenterGeoLocation(new Point2D.Double(0D, 0D));
				annotation.render((Graphics2D)new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).getGraphics(), 
						renderProperties, true);
				Rectangle bounds_pixels = annotation.getPixelShape() != null ? annotation.getPixelShape().getBounds() : null;
				Point2D center_pixels = AnnotationUtils.computeAnnotationCenterLocation_pixels(
						orientation, endLocationPixels, offset_pixels, 
						bounds_pixels != null ? (int)bounds_pixels.getWidth() : 0, 
						bounds_pixels != null ? (int)bounds_pixels.getHeight() : 0); 
				annotation.setCenterGeoLocation(renderProperties.worldPixelToGeoPosition(center_pixels));
				/*annotation.setCenterGeoLocation(
						AnnotationUtils.computeAnnotationCenterLocation(orientation, 
								renderProperties.worldPixelToGeoPosition(endLocationPixels), 
								offset_pixels, annotation.getGeoShape(), parentLocation));*/
			}
		}
		
		//Render the annotation
		annotation.render(g, renderProperties, renderPropertiesChanged);
		
		//Render the annotation line if the annotation is not at the center orientation
		if(showAnnotationLine && orientation != AnnotationOrientation.Center && orientation != AnnotationOrientation.Center_1) {
			Point2D startLocationPixels = annotation.getAnnotationLineStartPixelLocation(orientation);
			//Point2D endLocationPixels = parent.getAnnotationLineEndPixelLocation(orientation);
			Line2D line = new Line2D.Double(startLocationPixels, endLocationPixels);

			if(getAnnotationLineStyle() != null) {
				g.setStroke(getAnnotationLineStyle().getLineStroke());
			}
			if(annotationLineColor == null) {
				annotationLineColor = Color.black;
			}
			g.setColor(annotationLineColor);
			g.draw(line);
		}
		
		g.setColor(origColor);
		g.setStroke(origStroke);
	}		
}