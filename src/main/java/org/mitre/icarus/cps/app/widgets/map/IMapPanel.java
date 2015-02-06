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
package org.mitre.icarus.cps.app.widgets.map;

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import org.mitre.icarus.cps.app.widgets.map.layers.ILayer;
import org.mitre.icarus.cps.app.widgets.map.objects.IMapObject;

/**
 * Interface for CPD Phase 1 map GUI implementations.
 * 
 * @author CBONACETO
 *
 */
public interface IMapPanel {
	
	/**
	 * Enum containing mouse cursor types.
	 * 
	 * @author CBONACETO
	 *
	 */
	public static enum CursorType {POINTER, EXPAND_EAST_WEST, EXPAND_NORTH_SOUTH, MOVE, HAND};
	
	/**
	 * Redraw the map and all layers. Layers are drawn in the order in which they were added to the map.
	 */
	public void redraw();
	
	/**
	 * Only redraw the given layer that was modified.
	 * 
	 * @param modifiedLayer the layer that was modified
	 */
	public void redraw(ILayer<? extends IMapObject> modifiedLayer);	
	
	/**
	 * Add a listener to be notified of map mouse events.
	 * 
	 * @param l the mouse listener to add
	 */
	public void addMouseListener(MouseListener l);
	
	/**
	 * Remove mouse listener.
	 * 
	 * @param l the mouse listener to remove
	 */
	public void removeMouseListener(MouseListener l);
	
	/**
	 * Add a listener to be notified of map mouse motion events.
	 * 
	 * @param l
	 */
	public void addMouseMotionListener(MouseMotionListener l);
	
	/**
	 * Remove mouse motion listener.
	 * 
	 * @param l
	 */
	public void removeMouseMotionListener(MouseMotionListener l);	
	
	/**
	 * Set the current mouse cursor. The mouse cursor cannot be changed again until
	 * restoreCursor is called.
	 * 
	 * @param cursorType the cursor type of the cursor
	 */
	public void setCursor(CursorType cursorType);
	
	/**
	 * Restore the mouse cursor to the default cursor.
	 */
	public void restoreCursor();
}