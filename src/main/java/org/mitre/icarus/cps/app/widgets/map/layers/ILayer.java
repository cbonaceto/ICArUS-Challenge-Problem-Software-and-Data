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
import java.awt.geom.Point2D;
import java.util.Collection;

import org.mitre.icarus.cps.app.widgets.map.objects.IMapObject;

/**
 * Interface for map layers. Layer contain map objects that are rendered on the map.
 * 
 * @author Eric Kappotis
 *
 */
public interface ILayer<T extends IMapObject> {
	
	/**
	 * Get the layer Id
	 * 
	 * @return
	 */
	public String getId();
	
	/**
	 * Set the layer Id
	 * 
	 * @param id
	 */
	public void setId(String id);
	
	/**
	 * Get the layer name
	 * 
	 * @return
	 */
	public String getName();
	
	/**
	 * Set the layer name
	 * 
	 * @param name
	 */
	public void setName(String name);
	
	/**
	 * Get the icon for the layer
	 * 
	 * @return
	 */
	public Image getIcon();
	
	/**
	 * Set the icon for the layer
	 * 
	 * @param icon
	 */
	public void setIcon(Image icon);
	
	/**
	 * @return
	 */
	public int getzOrderPreference();
	
	/**
	 * @param zOrder
	 */
	public void setzOrderPreference(int zOrder);
	
	/**
	 * Add a map object to the layer
	 * 
	 * @param mapObject
	 */
	public void addMapObject(T mapObject);
	
	/**
	 * 
	 * @param mapObject
	 */
	public void moveMapObjectToBottomOfZOrder(T mapObject);	
	
	/**
	 * Remove a map object from the layer
	 * 
	 * @param mapObject
	 */
	public void removeMapObject(Object mapObject);
	
	/**
	 * Remove a map object with the given Id from the layer
	 * 
	 * @param id
	 */
	//public void removeMapObject(String id);
	
	/**
	 * Remove all map objects from the layer.
	 */
	public void removeAllMapObjects();		
	
	/**
	 * Get all the map objects in the layer
	 * 
	 * @return
	 */
	public Collection<T> getMapObjects();	
	
	/**
	 * Get whether the layer is visible
	 * 
	 * @return
	 */
	public boolean isVisible();
	
	/**
	 * Set whether the layer is visible
	 * 
	 * @param visible
	 */
	public void setVisible(boolean visible);
	
	/**
	 * Get whether the layer is enabled.
	 * 
	 * @return
	 */
	public boolean isEnabled();
	
	/**
	 * Set whether the layer is enabled.
	 * 
	 * @param enabled
	 */
	public void setEnabled(boolean enabled);
	
	/**
	 * @param point
	 * @return
	 */
	public T getFirstObjectAtLocation(Point2D location);
	
	/**
	 * @param point
	 * @return
	 */
	public Collection<T> getObjectsAtLocation(Point2D location);	
	
	/**
	 * Get whether layer objects may be selected with the mouse.
	 * 
	 * @return
	 */
	public boolean isSelectable();
	
	/**
	 * Set whether layer objects may be selected with the mouse.
	 * 
	 * @param selectable
	 */
	public void setSelectable(boolean selectable);	
}