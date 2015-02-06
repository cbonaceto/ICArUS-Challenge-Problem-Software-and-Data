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

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.mitre.icarus.cps.app.widgets.map.layers.AbstractLayer;
import org.mitre.icarus.cps.app.widgets.map.phase_1.RenderProperties;
import org.mitre.icarus.cps.app.widgets.map.phase_1.objects.IAnnotatableMapObject;
import org.mitre.icarus.cps.app.widgets.map.phase_1.objects.IMapObject_Phase1;

/**
 *  Basic implementation of an ILayer.
 * 
 * @author CBONACETO
 *
 * @param <T>
 */
public abstract class AbstractLayer_Phase1<T extends IMapObject_Phase1> extends AbstractLayer<T> implements ILayer_Phase1<T> {
	
	/** The map objects in the layer */
	protected List<T> mapObjects;
	
	public AbstractLayer_Phase1(String id) {
		super(id);
		mapObjects = new LinkedList<T>();
	}
	
	@Override
	public void addMapObject(T mapObject) {
		if(mapObject.getLayer() != this) {
			if(mapObject.getLayer() != null) {			
				mapObject.getLayer().removeMapObject(mapObject);
			}
			mapObject.setLayer(this);
			mapObjects.add(mapObject);
		}
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
			if(mapObject instanceof IAnnotatableMapObject) {
				//Remove any annotations associated with this map object
				((IAnnotatableMapObject)mapObject).removeAllAnnotations();
			}
			if(mapObject instanceof IMapObject_Phase1) {
				((IMapObject_Phase1)mapObject).setVisible(false);
				((IMapObject_Phase1)mapObject).setLayer(null);
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
				mapObject.setVisible(false);
				mapObject.setLayer(null);
			}
		}
		mapObjects.clear();
	}	

	@Override
	public Collection<T> getMapObjects() {
		return mapObjects;
	}	

	@Override
	public T getFirstObjectAtLocation(Point2D location) {		
		if(mapObjects == null || mapObjects.isEmpty()) {
			return null;
		}		
		for(T mapObject : mapObjects) {
			if(mapObject.contains(location)) {
				return mapObject;
			}
		}			
		return null;
	}

	@Override
	public Collection<T> getObjectsAtLocation(Point2D location) {		
		if(mapObjects == null || mapObjects.isEmpty()) {
			return null;
		}		
		Collection<T> objects  = null;		
		for(T mapObject : mapObjects) {
			if(mapObject.contains(location)) {
				if(objects == null) {
					objects =  new LinkedList<T>();
				}
				objects.add(mapObject);
			}
		}				
		return objects;
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.gui.feature_vector.phase_1.layers.ILayer#isSelectable()
	 */
	@Override
	public boolean isSelectable() {
		return selectable;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.gui.feature_vector.phase_1.layers.ILayer#setSelectable(boolean)
	 */
	@Override
	public void setSelectable(boolean selectable) {
		this.selectable = selectable;
	}
	
	@Override
	public void render(Graphics2D g2d, RenderProperties renderData, boolean renderPropertiesChanged) {
		if(mapObjects == null || mapObjects.isEmpty()) {
			return;
		}			
		for(T mapObject : mapObjects) {
			mapObject.render(g2d, renderData, renderPropertiesChanged);
		}
	}	

	@Override
	public String toString() {
		if(name != null) {
			return  name;
		}
		return id;
	}		
}