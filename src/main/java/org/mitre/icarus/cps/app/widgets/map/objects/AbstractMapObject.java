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

/**
 * Abstract implementation of the IMapObject interface. 
 *  
 * @author CBONACETO
 *
 */
public abstract class AbstractMapObject implements IMapObject {
	/** The map object name	 */
	protected String name;
	
	/** The map object ID */
	protected String id;
	
	/** A user object associated with the map object */
	protected Object userObject;	
	
	/** Whether the map object is visible */
	protected boolean visible = true;

	/** Transparency value of the map object */
	protected float transparency = 1.f;
	
	/** Whether this map object is editable */
	protected boolean editable;
	
	/** Whether this map object is being edited */
	protected transient boolean editing;
	
	/** Whether the map object is selectable */
	protected boolean selectable;	
	
	/** Whether the bounds of the map object have changed such that its shape, path, or location needs
	 * to be recalculated */
	protected transient boolean objectBoundsChanged = false;
	
	@Override
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getId() {
		return id;
	}	
	
	public void setId(String id) {
		this.id = id;
	}

	public Object getUserObject() {
		return userObject;
	}

	public void setUserObject(Object userObject) {
		this.userObject = userObject;
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
	public float getTransparency() {
		return transparency;
	}

	@Override
	public void setTransparency(float transparency) {
		this.transparency = transparency;
	}

	@Override
	public boolean isEditable() {
		return editable;
	}

	@Override
	public void setEditable(boolean editable) {
		this.editable = editable;
	}	

	@Override
	public boolean isEditing() {
		return editing;
	}

	@Override
	public void setEditing(boolean editing) {
		this.editing = editing;
	}

	@Override
	public boolean isSelectable() {
		return selectable;
	}

	@Override
	public void setSelectable(boolean selectable) {
		this.selectable = selectable;
	}		

	public boolean isObjectBoundsChanged() {
		return objectBoundsChanged;
	}		
}