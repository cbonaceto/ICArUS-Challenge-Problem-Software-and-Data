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
package org.mitre.icarus.cps.app.widgets.map.objects;

import java.awt.event.MouseEvent;

import org.mitre.icarus.cps.app.widgets.map.layers.ILayer;

/**
 * Interface specification for objects rendered on the map.
 * 
 * @author CBONACETO
 *
 */
public interface IMapObject {
	public abstract String getName();	
	
	public abstract String getId();	

	public boolean isVisible(); 
	public void setVisible(boolean visible);
	
	public boolean isSelectable();	
	public void setSelectable(boolean selectable);
	
	public boolean isEditable();
	public void setEditable(boolean editable);
	
	public boolean isEditing();
	public void setEditing(boolean editing);
	
	public float getTransparency();
	public void setTransparency(float transparency);	
	
	public ILayer<? extends IMapObject> getLayer();
	
	public void setMouseOverState(boolean mouseOver, MouseEvent event);	
}