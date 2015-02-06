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
package org.mitre.icarus.cps.app.widgets.map.phase_1.layers;

import java.awt.geom.Point2D;
import java.util.Collection;

import org.mitre.icarus.cps.app.widgets.map.phase_1.IMapPanel_Phase1;
import org.mitre.icarus.cps.app.widgets.map.phase_1.editors.CircleEditor;
import org.mitre.icarus.cps.app.widgets.map.phase_1.editors.IMapObjectEditor;
import org.mitre.icarus.cps.app.widgets.map.phase_1.objects.CircleShape;
import org.mitre.icarus.cps.app.widgets.map.phase_1.objects.ShapeMapObject;

/**
 * Layer that contains shape map objects.
 * 
 * @author Eric Kappotis
 *
 */
public class ShapeLayer extends AbstractEditableLayer<ShapeMapObject> {
	
	public ShapeLayer(String id, IMapPanel_Phase1 map) {
		super(id, map);
		setSelectable(true);
	}
	
	/* Override default behavior to get the object whose bounds contain the location and whose center point is closest to the location.
	 * If center point is equi-distant for multiple objects whose bounds contain the location, the smallest object is returned.
	 * 
	 * (non-Javadoc)	  
	 * @see org.mitre.icarus.cps.gui.phase_1.feature_vector.layers.BasicLayer#getFirstObjectAtLocation(java.awt.geom.Point2D)
	 */
	@Override
	public ShapeMapObject getFirstObjectAtLocation(Point2D location) {		
		//First get all objects at the location
		Collection<ShapeMapObject> objects = super.getObjectsAtLocation(location);
		
		if(objects == null || objects.isEmpty()) {
			return null;
		} else if(objects.size() == 1) {
			//Return the first and only objects
			return objects.iterator().next();
		} else {
			//Return the object with the closest center point (break ties by returning the smallest object)
			ShapeMapObject closest = null;
			double minDistance = Double.MAX_VALUE;
			//ShapeMapObject smallest = null;
			//double minArea = Double.MAX_VALUE;			
			for(ShapeMapObject shape : objects) {
				Point2D center = shape.getCenterLocation_pixels();
				if(center != null) {
					double distance = computeDistance(center, location);
					//System.out.println("Distance to shape " + shape.getId() + ": " + distance);
					if(distance <= minDistance || closest == null) {
						if(distance == minDistance && closest != null) {
							if(shape.getArea_pixels() < closest.getArea_pixels()) {
								minDistance = distance;
								closest = shape;
							}
						} else {
							minDistance = distance;
							closest = shape;
						}
					}
				} else if(closest == null) {
					closest = shape;
				}
				/*Rectangle2D bounds = shape.getBounds();
				if(bounds != null) {
					double area = bounds.getWidth() * bounds.getHeight();
					if(area < minArea || smallest == null) {
						minArea = area;
						smallest = shape;
					}
				}
				else if(smallest == null) {
					smallest = shape;
				}*/
			}			
			//System.out.println(closest.getId() +  " is closest"); System.out.println();
			return closest;
		}
	}
	
	private double computeDistance(Point2D p1, Point2D p2) {
		return Math.pow(p1.getX() - p2.getX(), 2) +
				Math.pow(p1.getY() - p2.getY(), 2);
	}

	@Override
	public IMapObjectEditor<? extends ShapeMapObject> editObject(ShapeMapObject object) {
		
		if(object instanceof CircleShape) {
			
			CircleShape circleShape = (CircleShape)object;
			
			if(!editingObjects.containsKey(object)) {
				// create the editor
				IMapObjectEditor<CircleShape> objectEditor = new CircleEditor();

				editingObjects.put(object, new ObjectAndEditor<CircleShape>(circleShape, objectEditor));
				objectEditor.editMapObject(circleShape, this, map);
				
				return objectEditor;
			}
		}	
		return null;
	}	
}