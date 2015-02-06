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

import org.mitre.icarus.cps.app.widgets.map.phase_1.objects.RoadMapObject;

/**
 * Layer that contains roads.
 * 
 * @author Eric Kappotis
 *
 */
public class RoadLayer extends AbstractLayer_Phase1<RoadMapObject> {			
	
	public RoadLayer(String id) {
		super(id);
	}

	
	@Override
	public RoadMapObject getFirstObjectAtLocation(Point2D location) {
		
		if(mapObjects == null || mapObjects.isEmpty()) {
			return null;
		}
		
		//Rectangle2D rectangle = new Rectangle2D.Double(location.getX() - 1, location.getY() - 1, 3, 3);

		for(RoadMapObject roadMapObject : mapObjects) {
			if(roadMapObject.getRenderShape().contains(location)) {
				//if(roadMapObject.getRenderShape().intersects(rectangle)) {
				return roadMapObject;
			}
		}

		return null;
	}

	@Override
	public Collection<RoadMapObject> getObjectsAtLocation(Point2D location) {
		return null;
	}	
}