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

import org.mitre.icarus.cps.app.widgets.map.phase_2.MapPanel_Phase2;
import org.mitre.icarus.cps.app.widgets.map.phase_2.layers.ILayer_Phase2;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.IMapObject_Phase2;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.annotation.IAnnotationMapObject.AnnotationOrientation;

/**
 * Interface specification for map objects that can be annotated with an IAnnotationMapObject.
 * 
 * @author CBONACETO
 *
 */
public interface IAnnotatableMapObject extends IMapObject_Phase2 {
	public Annotation addAnnotationAtOrientation(IAnnotationMapObject annotationObject, AnnotationOrientation orientation,
			ILayer_Phase2<? super Annotation, ? extends MapPanel_Phase2> annotationLayer);
	
	public boolean isAnnotationAtOrientation(AnnotationOrientation orientation);
	
	public void removeAnnotationAtOrientation(AnnotationOrientation orientation);	
	
	public void removeAllAnnotations();
	
	public Point2D getAnnotationLineEndPixelLocation(AnnotationOrientation orientation);	
}