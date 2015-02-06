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
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.mitre.icarus.cps.app.widgets.map.phase_2.MapPanel_Phase2;
import org.mitre.icarus.cps.app.widgets.map.phase_2.RenderProperties;
import org.mitre.icarus.cps.app.widgets.map.phase_2.layers.ILayer_Phase2;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.ControlPoint.ControlPointType;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.annotation.Annotation;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.annotation.AnnotationUtils;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.annotation.IAnnotatableMapObject;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.annotation.IAnnotationMapObject;
import org.mitre.icarus.cps.feature_vector.phase_2.coordinate.GeoCoordinate;

/**
 * A placemark consists of a marker shape (circle or square) and/or a marker icon.
 * 
 * @author CBONACETO
 *
 */
public class Placemark extends AbstractShape implements IAnnotatableMapObject, IAnnotationMapObject {
	
	/** Placemark shape types */
	public static enum PlacemarkShape {Circle, Square, None};
	
	/** If set, the placemark marker shape will be rendered at this size instead of the standard placemark size specified
	 * in render properties */
	protected Double placemarkPreferredSize_pixels;
	
	/** Whether to render the placemark name */
	protected boolean showName;	
	
	/** The placemark icon */
	protected Image markerIcon;	
	
	/** The placemark shape type (circle, square, none) */
	protected PlacemarkShape markerShape = PlacemarkShape.None;
	
	/** Whether to render the placemark marker shape */
	protected boolean showMarkerShape = true;
	
	/** The font for the text */
	protected Font textFont;
	
	/** Any annotations (usually placemarks themselves) associated with this placemark (mapped by their orientation) */
	protected Map<AnnotationOrientation, Annotation> annotations;
	
	/** The center location (in Geo coordinates) */
	protected GeoPosition center;
	
	/** The placemark shape (in Geo coordinates) */
	protected Shape renderShape;
	
	/** The placemark shape in pixel coordinates */
	protected Shape renderShape_pixels;
	
	/** The map object the placemark is annotating when it is being used as an annotation */
	protected IAnnotatableMapObject parent;	
	
	public Placemark() {
		this((GeoPosition)null, PlacemarkShape.None, true, null);
	}
	
	public Placemark(GeoCoordinate center, PlacemarkShape markerShape, boolean showMarkerShape, Image markerIcon) {
		this(center != null ? new GeoPosition(center.getLat(), center.getLon()) : null, 
				markerShape, showMarkerShape, markerIcon);
	}
	
	public Placemark(GeoPosition center, PlacemarkShape markerShape, boolean showMarkerShape, Image markerIcon) {
		selectable = true;
		this.center = center != null ? center : new GeoPosition(0, 0);
		this.markerShape = markerShape;
		this.showMarkerShape = showMarkerShape;
		this.markerIcon = markerIcon;
	}
	
	/*//@Override
	public double getArea_pixels() {
		if(markerShape == PlacemarkShape.Square) {
			if(placemarkBounds_pixels != null) {
				return placemarkBounds_pixels.getWidth() * placemarkBounds_pixels.getHeight();
			}
		} else if(markerShape == PlacemarkShape.Circle) {
			if(placemarkBounds_pixels != null) {
				return Math.PI * Math.pow(placemarkBounds_pixels.getWidth(), 2);
			}	
		}
		return 0;
	}*/
	
	/**
	 * @return
	 */
	public Double getPlacemarkPreferredSize_pixels() {
		return placemarkPreferredSize_pixels;
	}

	/**
	 * @param placemarkPreferredSize_pixels
	 */
	public void setPlacemarkPreferredSize_pixels(Double placemarkPreferredSize_pixels) {
		this.placemarkPreferredSize_pixels = placemarkPreferredSize_pixels;
		objectBoundsChanged = true;
	}

	@Override
	public Point2D getCenterPixelLocation() {
		if(renderShape_pixels != null) {
			Rectangle2D bounds = renderShape_pixels.getBounds2D();
			return new Point2D.Double(bounds.getCenterX(), bounds.getCenterY());
		}
		return null;
	}	

	@Override
	public Point2D getCenterGeoLocation() {
		if(center != null) {
			return new Point2D.Double(center.getLongitude(), center.getLatitude());
		}
		return null;
	}	
	
	@Override
	public void setCenterGeoLocation(Point2D centerGeoLocation) {
		if(centerGeoLocation != null) {
			setCenterGeoLocation(centerGeoLocation.getX(), centerGeoLocation.getY());
		}
	}	
	
	@Override
	public void setCenterGeoLocation(GeoPosition centerGeoLocation) {
		if(centerGeoLocation != null) {
			setCenterGeoLocation(centerGeoLocation.getLongitude(), centerGeoLocation.getLatitude());
		}
	}
	
	public void setCenterGeoLocation(double longitude, double latitude) {
		center = new GeoPosition(latitude, longitude);
		objectBoundsChanged = true;
	}

	@Override
	public Shape getGeoShape() {
		return renderShape;
	}

	@Override
	public Shape getPixelShape() {
		return renderShape_pixels;
	}

	@Override
	public Collection<? extends ControlPoint> getControlPoints() {
		if(renderShape_pixels != null) {
			Rectangle2D bounds = renderShape_pixels.getBounds2D();
			return Collections.singleton(new ControlPoint(bounds.getCenterX(), bounds.getCenterY(), 0, 
					ControlPointType.Center));
		}
		return null;
	}

	public boolean isShowName() {
		return showName;
	}

	public void setShowName(boolean showName) {
		this.showName = showName;
	}

	public Image getMarkerIcon() {
		return markerIcon;
	}

	public void setMarkerIcon(Image markerIcon) {
		this.markerIcon = markerIcon;
		if(markerShape == null || markerShape == PlacemarkShape.None) {
			objectBoundsChanged = true;
		}
	}

	public PlacemarkShape getMarkerShape() {
		return markerShape;
	}

	public void setMarkerShape(PlacemarkShape markerShape) {
		if(this.markerShape != markerShape) {
			this.markerShape = markerShape;
			objectBoundsChanged = true;
			//shapeEdited = true;
		}
	}

	public boolean isShowMarkerShape() {
		return showMarkerShape;
	}

	public void setShowMarkerShape(boolean showMarkerShape) {
		this.showMarkerShape = showMarkerShape;
	}

	/**
	 * @return the textFont
	 */
	public Font getTextFont() {
		return textFont;
	}

	/**
	 * @param textFont the textFont to set
	 */
	public void setTextFont(Font textFont) {
		this.textFont = textFont;
	}	

	public Long getEditTime() {
		return editTime;
	}

	public void setEditTime(Long editTime) {
		this.editTime = editTime;
	}
	
	@Override
	public Annotation addAnnotationAtOrientation(IAnnotationMapObject annotationObject, AnnotationOrientation orientation,
			ILayer_Phase2<? super Annotation, ? extends MapPanel_Phase2> annotationLayer) {
		if(annotations == null) {
			annotations = new HashMap<AnnotationOrientation, Annotation>();
		}
		Annotation annotation = new Annotation(this, annotationObject, orientation);
		annotationObject.setParent(this);
		if(annotationLayer != null) {
			annotationLayer.addMapObject(annotation);
		}
		annotations.put(orientation, annotation);
		return annotation;
	}

	@Override
	public boolean isAnnotationAtOrientation(AnnotationOrientation orientation) {
		if(annotations != null) {
			return annotations.containsKey(orientation);
		}
		return false;
	}

	@Override
	public void removeAnnotationAtOrientation(AnnotationOrientation orientation) {
		if(annotations != null) {
			Annotation annotation = annotations.get(orientation);
			if(annotation != null) {
				if(annotation.getLayer() != null) {
					annotation.getLayer().removeMapObject(annotation);
				}
				annotations.remove(orientation);
			}			
		}
	}

	@Override
	public void removeAllAnnotations() {
		if(annotations != null) {
			for(Annotation annotation : annotations.values()) {
				if(annotation.getLayer() != null) {
					annotation.getLayer().removeMapObject(annotation);					
				}				
			}
			annotations.clear();
		}		
	}	

	@Override
	public Point2D getAnnotationLineEndPixelLocation(AnnotationOrientation orientation) {
		return AnnotationUtils.computeAnnotationLineEndPixelLocation(orientation, 
				getCenterPixelLocation(), renderShape_pixels);
	}

	@Override
	public Point2D getAnnotationLineStartPixelLocation(AnnotationOrientation orientation) {
		return AnnotationUtils.computeAnnotationLineStartPixelLocation(orientation, 
				getCenterPixelLocation(), renderShape_pixels);
	}	

	@Override
	public IAnnotatableMapObject getParent() {
		return parent;
	}

	@Override
	public void setParent(IAnnotatableMapObject parent) {
		this.parent = parent;
	}

	@Override
	public void render(Graphics2D g, RenderProperties renderProperties, boolean renderPropertiesChanged) {		
		Color origColor = g.getColor();
		Stroke origStroke = g.getStroke();
		Font origFont = g.getFont();
		
		Point2D pixelLocation = null;
		if(objectBoundsChanged || renderPropertiesChanged || renderShape_pixels == null || renderShape == null) {
			pixelLocation = renderProperties.geoPositionToWorldPixel(center);
			//TODO: Double check that geo-based render shapes are correct
			if(markerShape == null || markerShape == PlacemarkShape.None) {
				if(markerIcon != null) {
					int width_pixels = markerIcon.getWidth(null);
					int height_pixels = markerIcon.getHeight(null);	
					double width_degrees = renderProperties.pixelsToDegrees(width_pixels);
					double height_degrees = renderProperties.pixelsToDegrees(height_pixels);
					if(renderShape == null || !(renderShape instanceof Rectangle2D)) {
						renderShape = new Rectangle2D.Double();
						renderShape_pixels = new Rectangle2D.Double();
					}
					((Rectangle2D)renderShape).setFrame(center.getLongitude() - (width_degrees / 2.0),
							center.getLatitude() - (height_degrees / 2.0),
							width_degrees, height_degrees);							
					((Rectangle2D)renderShape_pixels).setFrame(pixelLocation.getX() - (width_pixels / 2), 
							pixelLocation.getY() - (height_pixels / 2), 
							width_pixels, height_pixels);	
				} else {
					//Use the text bounds instead
					if(textFont == null) {
						textFont = renderProperties.getPlacemarkFont();
					}
					g.setFont(textFont);
					Rectangle2D stringBounds = g.getFontMetrics().getStringBounds(name != null ? name : " ", g);			
					renderShape_pixels = new Rectangle2D.Double(
							pixelLocation.getX() - stringBounds.getWidth() / 2.0, 
							pixelLocation.getY() - stringBounds.getHeight() / 2.0,
							stringBounds.getWidth(), stringBounds.getHeight());
					if(renderShape == null || !(renderShape instanceof Rectangle2D)) {
						renderShape = new Rectangle2D.Double();
					}						
					double width_degrees = renderProperties.pixelsToDegrees(renderShape_pixels.getBounds().getWidth());
					double height_degrees = renderProperties.pixelsToDegrees(renderShape_pixels.getBounds().getHeight());
					((Rectangle2D)renderShape).setFrame(center.getLongitude() - (width_degrees / 2.0),
							center.getLatitude() - (height_degrees / 2.0),
							width_degrees, height_degrees);
				}
			} else {
				double placemarkSize_pixels = placemarkPreferredSize_pixels != null && placemarkPreferredSize_pixels > 0 ? 
						placemarkPreferredSize_pixels : renderProperties.getPlacemarkSize_pixels();
				double placemarkSize_degrees = renderProperties.pixelsToDegrees(placemarkSize_pixels);				
				if(markerShape == PlacemarkShape.Square) {	
					if(renderShape == null || !(renderShape instanceof Rectangle2D)) {
						renderShape = new Rectangle2D.Double();
						renderShape_pixels = new Rectangle2D.Double();
					}
					((Rectangle2D)renderShape).setFrame(center.getLongitude() - (placemarkSize_degrees / 2.0),
							center.getLatitude() + (placemarkSize_degrees / 2.0),
							placemarkSize_degrees, placemarkSize_degrees);							
					((Rectangle2D)renderShape_pixels).setFrame(pixelLocation.getX() - (placemarkSize_pixels / 2), 
							pixelLocation.getY() - (placemarkSize_pixels / 2), 
							placemarkSize_pixels, placemarkSize_pixels);
				} else if(markerShape == PlacemarkShape.Circle) {
					if(renderShape == null || !(renderShape instanceof Ellipse2D)) {
						renderShape = new Ellipse2D.Double();
						renderShape_pixels = new Ellipse2D.Double();
					}
					((Ellipse2D)renderShape).setFrame(center.getLongitude() - (placemarkSize_degrees / 2.0),
							center.getLatitude() + (placemarkSize_degrees / 2.0),
							placemarkSize_degrees, placemarkSize_degrees);							
					((Ellipse2D)renderShape_pixels).setFrame(pixelLocation.getX() - (placemarkSize_pixels / 2), 
							pixelLocation.getY() - (placemarkSize_pixels / 2), 
							placemarkSize_pixels, placemarkSize_pixels);					
				} 
			}
			objectBoundsChanged = false;
		} else {
			pixelLocation = getCenterPixelLocation();
		}
		
		if(renderShape_pixels != null) {
			//Set the transparency
			Composite origComposite = null;
			if(transparency < 1.f) {
				origComposite = g.getComposite();
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency));
			}

			//Fill the marker shape or bounds
			//if(showMarkerShape && backgroundColor != null) {
			if(backgroundColor != null && (showMarkerShape || markerShape == null || markerShape == PlacemarkShape.None)) {
				g.setColor(backgroundColor);
				g.fill(renderShape_pixels);
			}

			//Draw the marker icon
			if(markerIcon != null) {
				int imageWidth = markerIcon.getWidth(null);
				int imageHeight = markerIcon.getHeight(null);				
				//Draw image so center of image is at the center of the placemark bounds
				g.drawImage(markerIcon, 
						(int)(pixelLocation.getX() - imageWidth / 2.0), 
						(int)(pixelLocation.getY() - imageHeight / 2.0),					 
						null);
			}		

			//Draw the marker shape, image border, or bounds border
			//if(showMarkerShape && borderLineStyle != null) {			
			if(borderLineStyle != null && (showMarkerShape || markerShape == null || markerShape == PlacemarkShape.None)) {
				if(borderColor == null) {
					borderColor = Color.black;
				}			
				g.setColor(borderColor);			
				g.setStroke(borderLineStyle.getLineStroke());
				g.draw(renderShape_pixels);
			}

			//Draw the name
			if(showName && name != null) {
				if(foregroundColor == null) {
					foregroundColor = Color.black;
				}
				g.setColor(foregroundColor);
				if(textFont == null) {
					textFont = renderProperties.getPlacemarkFont();
				}
				g.setFont(textFont);
				Rectangle2D stringBounds = g.getFontMetrics().getStringBounds(name, g);			
				Rectangle2D textRenderableBox = new Rectangle2D.Double(
						pixelLocation.getX() - stringBounds.getWidth() / 2.0, 
						pixelLocation.getY() - stringBounds.getHeight() / 2.0,
						stringBounds.getWidth(), stringBounds.getHeight());						
				g.drawString(getName(), Math.round(textRenderableBox.getX()) + 1, 
						Math.round(textRenderableBox.getY()) + g.getFontMetrics().getAscent());
			}

			g.setColor(origColor);
			g.setStroke(origStroke);
			g.setFont(origFont);
			if(origComposite != null) {
				g.setComposite(origComposite);
			}
			objectBoundsChanged = false;
		}
	}
}