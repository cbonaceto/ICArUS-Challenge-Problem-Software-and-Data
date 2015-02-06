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

import java.awt.geom.Point2D;

import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.IMapObject_Phase2;

/**
 * Interface specification for map objects that can annotate IAnnotableMapObjects.
 * 
 * @author CBONACETO
 *
 */
public interface IAnnotationMapObject extends IMapObject_Phase2 {
	
	public static enum AnnotationOrientation {Center, Center_1, North, NorthEast, East, SouthEast, South, SouthWest, West, NorthWest};

	public void setCenterGeoLocation(Point2D centerLocation);
	public void setCenterGeoLocation(GeoPosition centerLocation);
	
	public IAnnotatableMapObject getParent();
	public void setParent(IAnnotatableMapObject parent); 
	
	public Point2D getAnnotationLineStartPixelLocation(AnnotationOrientation orientation);
}