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
package org.mitre.icarus.cps.app.widgets.map.phase_2.layers;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.mitre.icarus.cps.app.widgets.map.layers.AbstractLayer;
import org.mitre.icarus.cps.app.widgets.map.objects.IMapObject;
import org.mitre.icarus.cps.app.widgets.map.phase_2.MapPanel_Phase2;
import org.mitre.icarus.cps.app.widgets.map.phase_2.RenderProperties;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.IMapObject_Phase2;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.annotation.IAnnotatableMapObject;

/**
 *  Basic implementation of an ILayer. Implements the render method and simply iterates over the objects contained
 *  in the layer and calls each of their render methods. Sub-classes may override this render method if required.
 * 
 * @author CBONACETO
 *
 * @param <T>
 */
public abstract class AbstractLayer_Phase2<T extends IMapObject_Phase2, M extends MapPanel_Phase2> extends AbstractLayer<T> implements ILayer_Phase2<T, M> {
	
	/** The map this layer is contained in */
	protected final M map;	
	
	/** The map objects in the layer */
	protected List<T> mapObjects;
		
	public AbstractLayer_Phase2(String id, M map) {
		super(id);
		this.map = map;
		mapObjects = new LinkedList<T>();
	}

	@Override
	public M getMap() {
		return map;
	}	

	@Override
	public void addMapObject(T mapObject) {
		if(mapObject != null && !mapObjects.contains(mapObject)) {			
			if(mapObject.getLayer() != this && mapObject.getLayer() != null) {
				//Remove the map object from its previous layer
				mapObject.getLayer().removeMapObject(mapObject);
			}
			mapObject.setLayer(this);
			mapObjects.add(mapObject);
		}
		/*if(mapObject.getLayer() != this) {
			if(mapObject.getLayer() != null) {			
				mapObject.getLayer().removeMapObject(mapObject);
			}
			mapObject.setLayer(this);
			mapObjects.add(mapObject);
		}*/
	}
	
	@Override
	public void moveMapObjectToBottomOfZOrder(T mapObject) {
		if(mapObjects != null && !mapObjects.isEmpty()) {
			int currentIndex = mapObjects.indexOf(mapObject);
			int lastIndex = mapObjects.size() - 1;
			if(currentIndex >= 0 && currentIndex != lastIndex) {
				Collections.swap(mapObjects, currentIndex, lastIndex);
			}
		}
	}

	@Override
	public void removeMapObject(Object mapObject) {
		if(mapObject != null && mapObjects.contains(mapObject)) {
			if(mapObject instanceof IMapObject) {
				//((IMapObject)mapObject).setVisible(false);
				((IMapObject_Phase2)mapObject).setLayer(null);
			}
			mapObjects.remove(mapObject);
		}
	}

	@Override
	public void removeAllMapObjects() {
		if(!mapObjects.isEmpty()) {
			for(T mapObject : mapObjects) {
				if(mapObject instanceof IAnnotatableMapObject) {
					//Remove any annotations associated with this map object
					((IAnnotatableMapObject)mapObject).removeAllAnnotations();
				}
				//mapObject.setVisible(false);
				mapObject.setLayer(null);
			}
		}
		mapObjects.clear();
	}

	@Override
	public Collection<T> getMapObjects() {
		return mapObjects;
	}	

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.gui.map.layers.ILayer#getFirstObjectAtLocation(java.awt.geom.Point2D)
	 */
	@Override
	public T getFirstObjectAtLocation(Point2D location) {		
		if(mapObjects == null || mapObjects.isEmpty()) {
			return null;
		}		
		for(T mapObject : mapObjects) {
			if(mapObject.containsPixelLocation(location)) {
				return mapObject;
			}
		}			
		return null;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.gui.map.layers.ILayer#getObjectsAtLocation(java.awt.geom.Point2D)
	 */
	@Override
	public Collection<T> getObjectsAtLocation(Point2D location) {		
		if(mapObjects == null || mapObjects.isEmpty()) {
			return null;
		}		
		Collection<T> objects  = null;		
		for(T mapObject : mapObjects) {
			if(mapObject.containsPixelLocation(location)) {
				if(objects == null) {
					objects =  new LinkedList<T>();
				}
				objects.add(mapObject);
			}
		}			
		return objects;
	}	
		
	@Override
	public void render(Graphics2D g, RenderProperties renderProperties, boolean renderPropertiesChanged) {		
		if(mapObjects == null || mapObjects.isEmpty()) {
			return;
		}
		for(T mapObject : mapObjects) {
			//TODO: Clip objects that aren't visible
			//TODO: There will be an issue if a map object isn't visible when the render properties change,
			//but then set to visible and rendered at a later time when the flag is false but the properties
			//have changed since the last rendering
			if(mapObject.isVisible()) {
				mapObject.render(g, renderProperties, renderPropertiesChanged);
			} else {
				//TODO: Just mark that render props changed
				
			}
		}
	}
}