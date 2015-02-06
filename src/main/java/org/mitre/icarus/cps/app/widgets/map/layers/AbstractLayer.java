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
package org.mitre.icarus.cps.app.widgets.map.layers;

import java.awt.Image;

import org.mitre.icarus.cps.app.widgets.map.objects.IMapObject;

/**
 * Abstract implementation of the ILayer interface.
 * 
 * @author CBONACETO
 *
 */
public abstract class AbstractLayer<T extends IMapObject> implements ILayer<T> {
	
	/** The layer ID */
	protected String id;
	
	/** The layer name */
	protected String name;
	
	/** The layer icon (if any) */
	protected Image icon;
		
	/** The desired z order position of the layer */
	protected int zOrderPreference;
	
	/** Whether the layer is visible */
	protected boolean visible = true;
	
	/** Whether the layer is enabled */
	protected boolean enabled = true;
	
	/** The map objects in the layer */
	//protected List<T> mapObjects;	
	
	/** whether or not the layer contains selectable objects */
	protected boolean selectable;
	
	public AbstractLayer(String id) {
		this.id = id;
		//mapObjects = new LinkedList<T>();
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}	

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Image getIcon() {
		return icon;
	}

	@Override
	public void setIcon(Image icon) {
		this.icon = icon;
	}	

	@Override
	public int getzOrderPreference() {
		return zOrderPreference;
	}

	@Override
	public void setzOrderPreference(int zOrderPreference) {
		this.zOrderPreference = zOrderPreference;
	}

	@Override
	public boolean isVisible() {
		return visible;
	}

	@Override
	public void setVisible(boolean visible) {
		this.visible = visible;
	}	
	
	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/*@Override
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
			if(mapObject instanceof IMapObject) {
				((IMapObject)mapObject).setVisible(false);
				((IMapObject)mapObject).setLayer(null);
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
			if(mapObject.containsGeoLocation(location)) {
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
			if(mapObject.containsGeoLocation(location)) {
				if(objects == null) {
					objects =  new LinkedList<T>();
				}
				objects.add(mapObject);
			}
		}			
		return objects;
	}*/	
	
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
	public String toString() {
		if(name != null) {
			return  name;
		}
		return id;
	}
}