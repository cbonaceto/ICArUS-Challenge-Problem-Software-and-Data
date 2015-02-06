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

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Collection;

import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.mitre.icarus.cps.app.widgets.map.phase_2.RenderProperties;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.IMapObject_Phase2;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.annotation.IAnnotationMapObject.AnnotationOrientation;

/**
 * @author CBONACETO
 *
 */
public class AnnotationUtils {
	
	private AnnotationUtils() {}
	
	/**
	 * @param orientation
	 * @return
	 */
	public static double getHeadingRadians(AnnotationOrientation orientation) {
		switch(orientation) {
		case Center: case Center_1: case East:
			return 0.0;
		case North:
			return Math.PI / 2.0;
		case NorthEast:
			return Math.PI / 4.0;
		case NorthWest:
			return 3.0 * Math.PI / 4.0;
		case South:
			return 3.0 * Math.PI / 2.0;
		case SouthEast:
			return 7.0 * Math.PI / 4.0;
		case SouthWest:
			return 5.0 * Math.PI / 4.0;
		case West:
			return Math.PI;
		default:
			return 0.0;
		}
		/*
		 * double angle;		
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
		 */
	}
	
	public static Point2D computeAnnotationCenterLocation(AnnotationOrientation orientation, GeoPosition endLocation,
			int offset_degrees, Shape annotationShape, Point2D parentCenterLocation) {
		//TODO: Check this logic
		if(orientation == AnnotationOrientation.Center || orientation == AnnotationOrientation.Center_1) {
			//Case where the annotation orientation is that the center
			return parentCenterLocation;
		} else {
			//Case where the annotation orientation is not at the center
			Rectangle2D bounds = annotationShape != null ? annotationShape.getBounds2D() : null;
			Double angle = AnnotationUtils.getHeadingRadians(orientation);
			Double cos = Math.cos(angle);
			Double sin = Math.sin(angle);
			return new Point2D.Double(
				endLocation.getLongitude() + (offset_degrees * Math.cos(angle)) + (bounds != null ? bounds.getWidth()/2.0 * cos : 0),
				endLocation.getLatitude() + (offset_degrees * Math.sin(angle)) + (bounds != null ? bounds.getHeight()/2.0 * sin : 0));
			
			/*
			 * Double angle = AnnotationUtils.getHeadingRadians(orientation);
			Double cos = Math.cos(angle);
			Double sin = Math.sin(angle);
			return new Point2D.Double(endLocation_pixel.getX() + (offset_pixels * cos) + ((annotationWidth_pixels/2.0) * cos),
					endLocation_pixel.getY() - (offset_pixels * sin) - ((annotationHeight_pixels/2.0) * sin));
			 */
		}
	}
	
	/**
	 * @param orientation
	 * @param endLocation_pixel
	 * @param offset_pixels
	 * @param annotationWidth
	 * @param annotationHeight
	 * @return
	 */
	public static Point2D computeAnnotationCenterLocation_pixels(AnnotationOrientation orientation, Point2D endLocation_pixel,
			int offset_pixels, int annotationWidth_pixels, int annotationHeight_pixels) {		
		if(orientation == AnnotationOrientation.Center || orientation == AnnotationOrientation.Center_1) {
			//Case where the annotation orientation is that the center
			return endLocation_pixel;
		} else {
			//Case where the annotation orientation is not at the center
			//TODO: Check this logic
			Double angle = AnnotationUtils.getHeadingRadians(orientation);
			Double cos = Math.cos(angle);
			Double sin = Math.sin(angle);
			return new Point2D.Double(endLocation_pixel.getX() + (offset_pixels * cos) + ((annotationWidth_pixels/2.0) * cos),
					endLocation_pixel.getY() - (offset_pixels * sin) - ((annotationHeight_pixels/2.0) * sin));
			//return new Point2D.Double(endLocation_pixel.getX() + (offset_pixels * cos) + ((annotationWidth_pixels/2.0) * cos),
			//		endLocation_pixel.getY() - (offset_pixels * sin) - ((annotationHeight_pixels/2.0) * sin));
		}
	}
	
	/**
	 * @param orientation
	 * @param center_pixels
	 * @param renderShape_pixels
	 * @return
	 */
	public static Point2D computeAnnotationLineEndPixelLocation(AnnotationOrientation orientation, 
			Point2D center_pixels, Shape renderShape_pixels) {		
		if(renderShape_pixels != null) {
			Rectangle2D bounds_pixels = renderShape_pixels.getBounds2D();		
			if(renderShape_pixels instanceof Ellipse2D) {
				if(orientation == AnnotationOrientation.Center || orientation == AnnotationOrientation.Center_1) {
					return center_pixels;
				}
				double angle = AnnotationUtils.getHeadingRadians(orientation);
				double radius_pixels = bounds_pixels.getWidth() / 2.0;
				return new Point2D.Double(center_pixels.getX() + radius_pixels * Math.cos(angle), 
						center_pixels.getY() - radius_pixels * Math.sin(angle));
			} else {
				switch(orientation) {
				case Center: case Center_1: 
					return center_pixels;
				case East:
					return new Point2D.Double(bounds_pixels.getX() + bounds_pixels.getWidth(), center_pixels.getY());
				case NorthEast:
					return new Point2D.Double(bounds_pixels.getX() + bounds_pixels.getWidth(), bounds_pixels.getY());
				case North:
					return new Point2D.Double(center_pixels.getX(), bounds_pixels.getY());
				case NorthWest:
					return new Point2D.Double(bounds_pixels.getX(), bounds_pixels.getY());
				case West:
					return new Point2D.Double(bounds_pixels.getX(), center_pixels.getY());
				case SouthWest:
					return new Point2D.Double(bounds_pixels.getX(), bounds_pixels.getY() + bounds_pixels.getHeight());
				case South:
					return new Point2D.Double(center_pixels.getX(), bounds_pixels.getY() + bounds_pixels.getHeight());
				default:
					//SouthEast
					return new Point2D.Double(bounds_pixels.getX() + bounds_pixels.getWidth(), bounds_pixels.getY() + bounds_pixels.getHeight());
				}			
			}
		} else {
			return null;
		}
	}
	
	/**
	 * @param orientation
	 * @param center_pixels
	 * @param renderShape_pixels
	 * @return
	 */
	public static Point2D computeAnnotationLineStartPixelLocation(AnnotationOrientation orientation,
			Point2D center_pixels, Shape renderShape_pixels) {
		if(renderShape_pixels != null) {
			Rectangle2D bounds_pixels = renderShape_pixels.getBounds2D();		
			if(renderShape_pixels instanceof Ellipse2D) {
				if(orientation == AnnotationOrientation.Center || orientation == AnnotationOrientation.Center_1) {
					return center_pixels;
				}			
				double angle = AnnotationUtils.getHeadingRadians(orientation) + Math.PI;
				double radius_pixels = bounds_pixels.getWidth() / 2.0;
				return new Point2D.Double(center_pixels.getX() + radius_pixels * Math.cos(angle), 
						center_pixels.getY() - radius_pixels * Math.sin(angle));
			} else {				
				switch(orientation) {
				case Center: case Center_1: 
					return center_pixels;
				case East:
					return new Point2D.Double(bounds_pixels.getX(), center_pixels.getY());
				case NorthEast:					
					return new Point2D.Double(bounds_pixels.getX(), bounds_pixels.getY() + bounds_pixels.getHeight());
				case North:
					return new Point2D.Double(center_pixels.getX(), bounds_pixels.getY() + bounds_pixels.getHeight());
				case NorthWest:					
					return new Point2D.Double(bounds_pixels.getX() + bounds_pixels.getWidth(), bounds_pixels.getY() + bounds_pixels.getHeight());
				case West:					
					return new Point2D.Double(bounds_pixels.getX() + bounds_pixels.getWidth(), center_pixels.getY());
				case SouthWest:					
					return new Point2D.Double(bounds_pixels.getX() + bounds_pixels.getWidth(), bounds_pixels.getY());
				case South:					
					return new Point2D.Double(center_pixels.getX(), bounds_pixels.getY());
				default:
					//SouthEast
					return new Point2D.Double(bounds_pixels.getX(), bounds_pixels.getY());
				}					
			}
		} else {
			return null;
		}
	}
	
	/**
	 * Returns true if any part of the given bounds is outside of the viewport bounds or
	 * overlaps an object in the conflict objects set.
	 * 
	 * @param mapObjectBounds
	 * @param conflictObjects
	 * @param renderProperties
	 * @return
	 */
	public static boolean checkOverlapAndOutiseBoundsConflicts(IAnnotationMapObject mapObject,
			Rectangle mapObjectBounds, Collection<IMapObject_Phase2> conflictObjects,
			RenderProperties renderProperties) {
		if(mapObjectBounds != null) {
			//Shape pixelShape = mapObject.getPixelShape();
			
			//Determine if any part of the map object's bounds are outside of the viewport bounds
			boolean inBounds = renderProperties.isPixelShapeCompletelyInView(mapObjectBounds);
			if(!inBounds) {
				return true;
			}
			
			//Determine if the map object's bounds overlaps any of the objects in the conflict objects set
			//Rectangle mapObjectBounds = pixelShape.getBounds();
			if(conflictObjects != null && !conflictObjects.isEmpty()) {
				for(IMapObject_Phase2 object : conflictObjects) {
					if(object != null && object.isVisible() && object != mapObject && 
							object.getPixelShape() != null) {
						Rectangle bounds = object.getPixelShape().getBounds();
						if(bounds != null && mapObjectBounds.intersects(bounds)) {
							return true;
						}
					}	
				}
			}
		}
		return false;
	}
	
	/**
	 * @param mapObject
	 * @param conflictObjects
	 * @param renderProperties
	 * @return
	 */
	public static double computeOverlapAndOutsideBoundsArea(IAnnotationMapObject mapObject,
			Rectangle mapObjectBounds, Collection<IMapObject_Phase2> conflictObjects,
			RenderProperties renderProperties) {
		double area = 0d;
		if(mapObjectBounds != null) {
			//System.out.println("Map Object Bounds: " + mapObjectBounds);
			//Shape pixelShape = mapObject.getPixelShape();
			
			//Compute the area of the map object's bounds outside of the viewport bounds
			area += renderProperties.computePixelShapeAreaOutsideView(mapObjectBounds);
			
			//Sum up the total area of other map objects covered by the map object's bounds
			if(conflictObjects != null && !conflictObjects.isEmpty()) {
				for(IMapObject_Phase2 object : conflictObjects) {
					if(object != null && object.isVisible() && object != mapObject &&
							mapObject.getParent() != object && object.getPixelShape() != null) {
						Rectangle bounds = object.getPixelShape().getBounds();
						if(bounds != null) {
							Rectangle intersection = mapObjectBounds.intersection(bounds);
							//System.out.println("Conflict Object Bounds: " + bounds + ", Intersection: " + intersection);
							if(intersection != null && intersection.width > 0 && intersection.height > 0) {								
								area += (intersection.width * intersection.height); 
							}
						}
					}	
				}
			}
		}
		return area;
	}
}