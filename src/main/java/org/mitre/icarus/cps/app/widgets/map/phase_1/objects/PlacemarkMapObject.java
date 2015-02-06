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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;

import org.mitre.icarus.cps.app.widgets.map.layers.ILayer;
import org.mitre.icarus.cps.app.widgets.map.phase_1.RenderProperties;
import org.mitre.icarus.cps.feature_vector.phase_1.GridLocation2D;

/**
 * @author Craig Bonaceto
 *
 */
public class PlacemarkMapObject extends ShapeMapObject implements IAnnotatableMapObject, IAnnotationMapObject {
	
	/** Placemark shape types */
	public static enum PlacemarkShape {Circle, Square};
	
	/** Whether to render the placemark name */
	protected boolean showName;	
	
	/** The placemark icon */
	protected Image markerIcon;	
	
	/** The placemark shape type (circle or square) */
	protected PlacemarkShape markerShape;
	
	/** Whether to render the placemark marker */
	protected boolean showMarker = true;
	
	/** The font for the text */
	protected Font textFont;
	
	/** The tooltip text */
	protected String toolTipText;	
	
	/** Any annotation placemarks associated with this placemark (mapped by their orientation) */
	protected Map<AnnotationOrientation, Annotation> annotations;
	
	/** The placemark bounds (in grid units) */
	protected Rectangle2D placemarkBounds_gridUnits;
	
	/** The placemark bounds (in pixels) */
	protected Rectangle2D placemarkBounds_pixels;
	
	/** The total amount of time spent editing the placemark (e.g., dragging it) */
	protected long editTime = 0;
	
	public PlacemarkMapObject() {
		selectable = true;
	}
	
	@Override
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
	}

	@Override
	public Point2D getCenterLocation_pixels() {
		if(renderShape != null && renderShape.getBounds2D() != null) {
			Rectangle2D bounds = renderShape.getBounds2D();
			return new Point2D.Double(bounds.getCenterX(), bounds.getCenterY());
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
	}

	public PlacemarkShape getMarkerShape() {
		return markerShape;
	}

	public void setMarkerShape(PlacemarkShape markerShape) {
		if(this.markerShape != markerShape) {
			this.markerShape = markerShape;
			objectBoundsChanged = true;
			shapeEdited = true;
		}
	}

	public boolean isShowMarker() {
		return showMarker;
	}

	public void setShowMarker(boolean showMarker) {
		this.showMarker = showMarker;
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
	
	@Override
	public String getToolTipText() {
		return toolTipText;
	}

	@Override
	public void setToolTipText(String toolTipText) {
		this.toolTipText = toolTipText;
	}
	
	public Annotation addAnnotationAtOrientation(IAnnotationMapObject annotationObject, AnnotationOrientation orientation,
			ILayer<? super Annotation> annotationLayer) {
		if(annotations == null) {
			annotations = new HashMap<AnnotationOrientation, Annotation>();
		}
		Annotation annotation = new Annotation(this, annotationObject, orientation);		
		if(annotationLayer != null) {
			annotationLayer.addMapObject(annotation);
		}
		annotations.put(orientation, annotation);
		return annotation;
	}
	
	public boolean isAnnotationAtOrientation(AnnotationOrientation orientation) {
		if(annotations != null) {
			return annotations.containsKey(orientation);
		}
		return false;
	}
	
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
	public GridLocation2D getAnnotationLineEndLocation(AnnotationOrientation orientation) {		
		if(renderShape instanceof Ellipse2D) {
			if(orientation == AnnotationOrientation.Center || orientation == AnnotationOrientation.Center_1) {
				return new GridLocation2D(centerLocation.getX(), centerLocation.getY());
			}			
			double angle = Annotation.getHeadingRadians(orientation);
			double radiusGridUnits = placemarkBounds_gridUnits.getWidth() / 2.0;			
			return new GridLocation2D(centerLocation.getX() + radiusGridUnits * Math.cos(angle), centerLocation.getY() + radiusGridUnits * Math.sin(angle));
		} else {
			if(orientation == AnnotationOrientation.Center || orientation == AnnotationOrientation.Center_1) {
				return centerLocation;
			} else if(orientation == AnnotationOrientation.East) {
				return new GridLocation2D(placemarkBounds_gridUnits.getX() + placemarkBounds_gridUnits.getWidth(), centerLocation.getY());
			} else if(orientation == AnnotationOrientation.NorthEast) {
				return new GridLocation2D(placemarkBounds_gridUnits.getX() + placemarkBounds_gridUnits.getWidth(), 
						placemarkBounds_gridUnits.getY());
			} else if(orientation == AnnotationOrientation.North) {
				return new GridLocation2D(centerLocation.getX(), placemarkBounds_gridUnits.getY());
			} else if(orientation == AnnotationOrientation.NorthWest) {
				return new GridLocation2D(placemarkBounds_gridUnits.getX(), placemarkBounds_gridUnits.getY());
			} else if(orientation == AnnotationOrientation.West) {
				return new GridLocation2D(placemarkBounds_gridUnits.getX(), centerLocation.getY());
			} else if(orientation == AnnotationOrientation.SouthWest) {
				return new GridLocation2D(placemarkBounds_gridUnits.getX(), placemarkBounds_gridUnits.getY() -  placemarkBounds_gridUnits.getHeight());
			} else if(orientation == AnnotationOrientation.South) {
				return new GridLocation2D(centerLocation.getX(), placemarkBounds_gridUnits.getY() - placemarkBounds_gridUnits.getHeight());
			} else { 
				return new GridLocation2D(placemarkBounds_gridUnits.getX() + placemarkBounds_gridUnits.getWidth(), placemarkBounds_gridUnits.getY() - placemarkBounds_gridUnits.getHeight());
			}			
		}
	}

	@Override
	public GridLocation2D getAnnotationLineStartLocation (AnnotationOrientation orientation) {		
		if(renderShape instanceof Ellipse2D) {
			if(orientation == AnnotationOrientation.Center || orientation == AnnotationOrientation.Center_1) {
				return new GridLocation2D(centerLocation.getX(), centerLocation.getY());
			}			
			double angle = Annotation.getHeadingRadians(orientation) + Math.PI;
			double radiusGridUnits = placemarkBounds_gridUnits.getWidth() / 2.0;			
			return new GridLocation2D(centerLocation.getX() + radiusGridUnits * Math.cos(angle), centerLocation.getY() + radiusGridUnits * Math.sin(angle));
		} else {
			if(orientation == AnnotationOrientation.Center || orientation == AnnotationOrientation.Center_1) {
				return centerLocation;				
			} else if(orientation == AnnotationOrientation.East) {
				return new GridLocation2D(placemarkBounds_gridUnits.getX(), centerLocation.getY());
			} else if(orientation == AnnotationOrientation.NorthEast) {
				return new GridLocation2D(placemarkBounds_gridUnits.getX(), 
						placemarkBounds_gridUnits.getY() - placemarkBounds_gridUnits.getHeight());
			} else if(orientation == AnnotationOrientation.North) {
				return new GridLocation2D(centerLocation.getX(), placemarkBounds_gridUnits.getY() - placemarkBounds_gridUnits.getHeight());
			} else if(orientation == AnnotationOrientation.NorthWest) {
				return new GridLocation2D(placemarkBounds_gridUnits.getX() + placemarkBounds_gridUnits.getWidth(), 
						placemarkBounds_gridUnits.getY() - placemarkBounds_gridUnits.getHeight());
			} else if(orientation == AnnotationOrientation.West) {
				return new GridLocation2D(placemarkBounds_gridUnits.getX() + placemarkBounds_gridUnits.getWidth(), 
						centerLocation.getY());
			} else if(orientation == AnnotationOrientation.SouthWest) {
				return new GridLocation2D(placemarkBounds_gridUnits.getX() + placemarkBounds_gridUnits.getWidth(), 
						placemarkBounds_gridUnits.getY());
			} else if(orientation == AnnotationOrientation.South) {
				return new GridLocation2D(centerLocation.getX(), placemarkBounds_gridUnits.getY());
			} else { 
				return new GridLocation2D(placemarkBounds_gridUnits.getX(), placemarkBounds_gridUnits.getY());
			}			
		}
	}	

	public long getEditTime() {
		return editTime;
	}

	public void setEditTime(long editTime) {
		this.editTime = editTime;
	}

	@Override
	public void render(Graphics2D g2d, RenderProperties renderData, boolean renderPropertiesChanged) {			
		Color origColor = g2d.getColor();
		Stroke origStroke = g2d.getStroke();
		Font origFont = g2d.getFont();
		
		if(objectBoundsChanged || renderPropertiesChanged || placemarkBounds_gridUnits == null || renderShape == null) {
			Point2D pixelLocation = renderData.translateToPixel(centerLocation);
			double placemarkSize_gridUnits = renderData.getPlacemarkSize_gridUnits();

			// set the bounds of the placemark bounds in terms of grid units
			placemarkBounds_gridUnits = new Rectangle2D.Double(centerLocation.getX() - (placemarkSize_gridUnits / 2.0),
					centerLocation.getY() + (placemarkSize_gridUnits / 2.0),
					placemarkSize_gridUnits, placemarkSize_gridUnits);
			
			double placemarkSize_pixels = renderData.getPlacemarkSize_pixels();
			
			placemarkBounds_pixels = new Rectangle2D.Double(pixelLocation.getX() - (placemarkSize_pixels / 2), 
					pixelLocation.getY() - (placemarkSize_pixels / 2), 
					placemarkSize_pixels, placemarkSize_pixels);		
			
			if(markerShape == PlacemarkShape.Square) {
				renderShape = placemarkBounds_pixels;
			} else if(markerShape == PlacemarkShape.Circle) {
				Ellipse2D ellipse2D = new Ellipse2D.Double(placemarkBounds_pixels.getX(), 
						placemarkBounds_pixels.getY(), 
						placemarkBounds_pixels.getWidth(), 
						placemarkBounds_pixels.getHeight());
				renderShape = ellipse2D;
			} else {
				return;
			}	
		}	
		
		//Set the transparency
		Composite origComposite = null;
		if(transparency < 1.f) {
			origComposite = g2d.getComposite();
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency));
		}
		
		//Fill the marker shape
		if(showMarker && backgroundColor != null) {
			g2d.setColor(backgroundColor);
			g2d.fill(renderShape);
		}
	
		//Draw the marker icon
		if(markerIcon != null) {
			Dimension imageSize = new Dimension(markerIcon.getWidth(null), markerIcon.getHeight(null));
			
			// draw image so center of image is at the center of the placemark bounds
			g2d.drawImage(markerIcon, 
					(int)(placemarkBounds_pixels.getCenterX() - imageSize.width/2.0), 
					(int)(placemarkBounds_pixels.getCenterY() - imageSize.height/2.0),
					//(int)Math.round(placemarkBounds_pixels.getCenterX() - imageSize.width / 2.0), 
					//(int)Math.round(placemarkBounds_pixels.getCenterY() - imageSize.height / 2.0), 
					null);
		}		
		
		//Draw the marker shape border
		if(showMarker && getBorderLineStyle() != null) {			
			if(borderColor == null) {
				borderColor = Color.black;
			}			
			g2d.setColor(borderColor);
			
			g2d.setStroke(getBorderLineStyle().getLineStroke());
			g2d.draw(renderShape);
		}
		
		//Draw the name
		if(showName && name != null) {
			if(foregroundColor == null) {
				foregroundColor = Color.black;
			}
			g2d.setColor(foregroundColor);
			
			if(textFont == null) {
				textFont = renderData.getPlacemarkFont();
			}
			g2d.setFont(textFont);
			
			Rectangle2D stringBounds = g2d.getFontMetrics().getStringBounds(this.getName(), g2d);
			
			Rectangle2D textRenderableBox = new Rectangle2D.Double(
					placemarkBounds_pixels.getX() + placemarkBounds_pixels.getWidth() / 2.0 - stringBounds.getWidth() / 2.0, 
					placemarkBounds_pixels.getY() + placemarkBounds_pixels.getHeight() / 2.0 - stringBounds.getHeight() / 2.0,
					stringBounds.getWidth(), stringBounds.getHeight());			
			
			g2d.drawString(getName(), Math.round(textRenderableBox.getX()) + 1, 
					Math.round(textRenderableBox.getY()) + g2d.getFontMetrics().getAscent());
		}
		
		g2d.setColor(origColor);
		g2d.setStroke(origStroke);
		g2d.setFont(origFont);
		if(origComposite != null) {
			g2d.setComposite(origComposite);
		}
		objectBoundsChanged = false;
	}

	@Override
	public ToolTip createToolTip() {
		if(toolTipText == null) {
			return null;
		}		
		ToolTip toolTip = new ToolTip(this);
		//toolTip.setToolTipText(toolTipText);		
		return toolTip;
	}

	@Override
	public void setMouseOverState(boolean mouseOver, MouseEvent event) {
		//Does nothing		
	}
}