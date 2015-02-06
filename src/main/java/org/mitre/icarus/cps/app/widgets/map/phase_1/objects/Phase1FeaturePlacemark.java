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
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.mitre.icarus.cps.app.widgets.map.layers.ILayer;
import org.mitre.icarus.cps.app.widgets.map.phase_1.MapConstants_Phase1;
import org.mitre.icarus.cps.app.widgets.map.phase_1.RenderProperties;
import org.mitre.icarus.cps.app.widgets.phase_1.ImageManager_Phase1;
import org.mitre.icarus.cps.feature_vector.phase_1.GridLocation2D;
import org.mitre.icarus.cps.feature_vector.phase_1.Phase1Feature;
import org.mitre.icarus.cps.feature_vector.phase_1.SigintType;

/**
 * @author CBONACETO
 *
 */
public class Phase1FeaturePlacemark<T extends Phase1Feature> extends PlacemarkMapObject {
	
	/** Whether placemark is highlighted */
	protected boolean highlighted = false;	
	
	/** The feature (group center or group attack location) */
	protected final T feature;
	
	/** The IMINT annotation */
	protected Annotation imintAnnotation;
	protected AnnotationOrientation imintAnnotationOrientation = AnnotationOrientation.NorthEast;
	
	/** The MOVINT annotation */
	protected Annotation movintAnnotation;
	protected AnnotationOrientation movintAnnotationOrientation = AnnotationOrientation.NorthWest;
	
	/** The SIGINT annotation */
	protected Annotation sigintAnnotationInnerCircle; //First ring for silent (dotted line) or chatter (solid line)
	protected Annotation sigintAnnotationOuterCircle; //Second ring for chatter (solid line)
	
	/** Preferred orientation of INTs (IMINT and MOVINT). May change based on proximity of placemark to map edge. */
	protected AnnotationOrientation intOrientationPreference = AnnotationOrientation.North;
	
	protected float previousLineWidth;
	
	protected Color previousBorderColor;
	
	public Phase1FeaturePlacemark(T feature) {
		this.feature = feature;
		centerLocation = feature.getLocation();
	}
	
	public T getFeature() {
		return feature;
	}	
	
	public boolean isImintAnnotationVisible() {
		return imintAnnotation != null && imintAnnotation.isVisible();
	}
	
	/**
	 * @param visible
	 * @param imintLayer
	 */
	public void setImintAnnotationVisible(boolean visible, ILayer<? super Annotation> imintLayer) {
		if(imintAnnotation == null) {
			PlacemarkMapObject imint = new PlacemarkMapObject();
			imint.setId("imint_" + id);
			imint.setMarkerShape(PlacemarkShape.Square);
			imint.setBackgroundColor(backgroundColor);
			imint.setBorderColor(Color.black);
			imint.setBorderLineWidth(1);
			if(feature.getIntelReport() != null && feature.getIntelReport().getImintInfo() != null) {
				imint.setMarkerIcon(ImageManager_Phase1.getImintImage(feature.getIntelReport().getImintInfo()));
			}			
			imintAnnotation = addAnnotationAtOrientation(imint, imintAnnotationOrientation, imintLayer); 	
		}
		imintAnnotation.setVisible(visible);		
	}
	
	public void removeImintAnnotation() {
		imintAnnotation = null;
		removeAnnotationAtOrientation(imintAnnotationOrientation);
	}
	
	public boolean isMovintAnnotationVisible() {
		return movintAnnotation != null && movintAnnotation.isVisible();
	}
	
	/**
	 * @param visible
	 * @param movintLayer
	 */
	public void setMovintAnnotationVisible(boolean visible, ILayer<? super Annotation> movintLayer) {
		if(movintAnnotation == null) {
			PlacemarkMapObject movint = new PlacemarkMapObject();
			movint.setId("movint_" + id);
			movint.setMarkerShape(PlacemarkShape.Square);
			movint.setBackgroundColor(backgroundColor);
			movint.setBorderColor(Color.black);
			movint.setBorderLineWidth(1);
			if(feature.getIntelReport() != null && feature.getIntelReport().getMovintInfo() != null) {
				movint.setMarkerIcon(ImageManager_Phase1.getMovintImage(feature.getIntelReport().getMovintInfo()));
			}
			movintAnnotation = addAnnotationAtOrientation(movint, movintAnnotationOrientation, movintLayer);
		}
		movintAnnotation.setVisible(visible);		
	}
	
	public void removeMovintAnnotation() {
		movintAnnotation = null;
		removeAnnotationAtOrientation(movintAnnotationOrientation);
	}
	
	public boolean isSigintAnnotationVisible() {
		return sigintAnnotationInnerCircle != null && sigintAnnotationInnerCircle.isVisible();
	}
	
	/**
	 * @param visible
	 * @param sigintLayer
	 */
	public void setSigintAnnotationVisible(boolean visible, ILayer<? super Annotation> sigintLayer) {
		SigintType sigintType = feature.getIntelReport() != null ? feature.getIntelReport().getSigintInfo() : null;
		if(visible && sigintAnnotationInnerCircle == null) {			
			CircleShape sigint = new CircleShape(MapConstants_Phase1.PLACEMARK_SIZE_PIXELS/2 + 6, CircleShape.RadiusType.Radius_Pixels);
			sigint.setId("sigint_" + id);
			sigint.setBorderColor(Color.black);						
			if(sigintType != null && sigintType == SigintType.Silent) {
				//Add a single dotted-line circle for silent
				sigint.setBorderLineWidth(MapConstants_Phase1.SIGINT_SILENT_LINE_STYLE.getLineWidth());
				sigint.setBorderLineStyle(MapConstants_Phase1.SIGINT_SILENT_LINE_STYLE);
			}	
			else {
				//Add a solid circle for chatter
				sigint.setBorderLineWidth(2.f);
			}			
			sigintAnnotationInnerCircle = addAnnotationAtOrientation(sigint, AnnotationOrientation.Center, sigintLayer);
		}
		if(visible && sigintType == SigintType.Chatter && sigintAnnotationOuterCircle == null) {
			//Add a second circle for chatter
			CircleShape sigint = new CircleShape(MapConstants_Phase1.PLACEMARK_SIZE_PIXELS/2 + 10, CircleShape.RadiusType.Radius_Pixels);
			sigint.setId("sigint_outer" + id);
			sigint.setBorderLineWidth(2.f); 
			sigintAnnotationOuterCircle = addAnnotationAtOrientation(sigint, AnnotationOrientation.Center_1, sigintLayer);
		}
		if(sigintAnnotationInnerCircle != null) {
			sigintAnnotationInnerCircle.setVisible(visible);
		}
		if(sigintAnnotationOuterCircle != null) {
			sigintAnnotationOuterCircle.setVisible(visible);			
		}		
	}
	
	public void removeSigintAnnotation() {
		sigintAnnotationInnerCircle = null;
		sigintAnnotationOuterCircle = null;
		removeAnnotationAtOrientation(AnnotationOrientation.Center);
		removeAnnotationAtOrientation(AnnotationOrientation.Center_1);
	}
	
	public boolean isHighlighted() {
		return highlighted;
	}

	public void setHighlighted(boolean highlighted) {
		if(this.highlighted != highlighted) {
			this.highlighted = highlighted;
			if(highlighted) {	
				previousBorderColor = borderColor;
				previousLineWidth = getBorderLineWidth();
				borderColor = MapConstants_Phase1.HIGHLIGHT_COLOR;
				setBorderLineWidth(3.f);
				//setTextFont(MapConstants.PLACEMARK_FONT.deriveFont(Font.BOLD));
				setTextFont(MapConstants_Phase1.PLACEMARK_FONT_LARGE);
			}
			else {
				borderColor = previousBorderColor;
				setBorderLineWidth(previousLineWidth);
				setTextFont(MapConstants_Phase1.PLACEMARK_FONT);
			}
		}
	}

	@Override
	public void render(Graphics2D g2d, RenderProperties renderData, boolean renderPropertiesChanged) {
		boolean updatingBounds = (objectBoundsChanged || renderPropertiesChanged || placemarkBounds_gridUnits == null || renderShape == null);
		super.render(g2d, renderData, renderPropertiesChanged);
		if(updatingBounds) {
			//Update the preferred INT orientation and re-locate existing INTs if necessary
			AnnotationOrientation orientationPreference = null;
			AnnotationOrientation newImintOrientation = null;
			AnnotationOrientation newMovintOrientation = null;
			Point2D annotationSize_pixels = new Point2D.Double(MapConstants_Phase1.PLACEMARK_SIZE_PIXELS, MapConstants_Phase1.PLACEMARK_SIZE_PIXELS);
			double offset_pixels = MapConstants_Phase1.ANNOTATION_OFFSET_PIXELS;			
			for(AnnotationOrientation orientation : ToolTip.orderedOrientationList) {
				switch(orientation) {
				case North:
					newImintOrientation = AnnotationOrientation.NorthEast;
					newMovintOrientation = AnnotationOrientation.NorthWest;
					break;
				case South:
					newImintOrientation = AnnotationOrientation.SouthEast;
					newMovintOrientation = AnnotationOrientation.SouthWest;
					break;
				case East:
					newImintOrientation = AnnotationOrientation.SouthEast;
					newMovintOrientation = AnnotationOrientation.NorthEast;
					break;
				case West:
					newImintOrientation = AnnotationOrientation.SouthWest;
					newMovintOrientation = AnnotationOrientation.NorthWest;
					break;
				default:
					newImintOrientation = AnnotationOrientation.NorthEast;
					newMovintOrientation = AnnotationOrientation.NorthWest;
					break;
				}
				Rectangle2D bounds1 = computeAnnotationBounds(newImintOrientation, annotationSize_pixels, offset_pixels, renderData);
				Rectangle2D bounds2 = computeAnnotationBounds(newMovintOrientation, annotationSize_pixels, offset_pixels, renderData);
				if(renderData.renderBounds.contains(bounds1) && renderData.renderBounds.contains(bounds2)) {
					orientationPreference = orientation;
					break;
				}
			}
			if(orientationPreference != null && orientationPreference != intOrientationPreference) {
				//Switch the preferred INT orientation
				//System.out.println("Switching preferred orientation to: " + orientationPreference);
				intOrientationPreference = orientationPreference;				
				if(imintAnnotation != null) {
					IAnnotationMapObject imint = imintAnnotation.getAnnotation();
					boolean visible = imintAnnotation.isVisible();
					ILayer<? super Annotation> layer = imintAnnotation.getLayer();
					removeImintAnnotation();
					imintAnnotation = addAnnotationAtOrientation(imint, newImintOrientation, layer);
					imintAnnotation.setVisible(visible);
				}
				if(movintAnnotation != null) {
					IAnnotationMapObject movint = movintAnnotation.getAnnotation();
					boolean visible = movintAnnotation.isVisible();
					ILayer<? super Annotation> layer = movintAnnotation.getLayer();
					removeMovintAnnotation();
					movintAnnotation = addAnnotationAtOrientation(movint, newMovintOrientation, layer);
					movintAnnotation.setVisible(visible);
				}
				imintAnnotationOrientation = newImintOrientation;
				movintAnnotationOrientation = newMovintOrientation;
			}
		}
	}	
	
	protected Rectangle2D computeAnnotationBounds(AnnotationOrientation orientation, Point2D annotationSize_pixels, 
			double offset_pixels, RenderProperties renderData) {		
		Point2D pixelLocation = null;		
		if(orientation == AnnotationOrientation.Center) {
			// case where the annotation orientation is that the center
			pixelLocation = renderData.translateToOffsetPixel(centerLocation);
		} else {			
			// case where the annotation orientation is not at the center
			double angle = Annotation.getHeadingRadians(orientation);			
			double offset_gridUnits = renderData.translateToGridUnits(offset_pixels);
			pixelLocation = renderData.translateToOffsetPixel(new GridLocation2D(
					centerLocation.getX() + offset_gridUnits * Math.cos(angle),
					centerLocation.getY() + offset_gridUnits * Math.sin(angle)));			
		}
		return new Rectangle2D.Double(pixelLocation.getX() - (annotationSize_pixels.getX() / 2), 
				pixelLocation.getY() - (annotationSize_pixels.getY() / 2), 
				annotationSize_pixels.getX(), annotationSize_pixels.getY());
	}
}