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

import org.mitre.icarus.cps.app.widgets.map.objects.IMapObject;
import org.mitre.icarus.cps.app.widgets.map.phase_2.MapPanel_Phase2;
import org.mitre.icarus.cps.app.widgets.map.phase_2.editors.IMapObjectEditor;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.IMapObject_Phase2;

/**
 * Interface for map layers that contain objects that can be edited.
 * 
 * @author CBONACETO
 *
 * @param <T>
 */
public interface IEditableLayer<T extends IMapObject_Phase2, M extends MapPanel_Phase2> extends ILayer_Phase2<T, M> {
	
	/**
	 * Edit the given map object contained in the layer. Returns the editor that will be used.
	 * 
	 * @param object the object to edit
	 * @return the object editor 
	 */
	public IMapObjectEditor<? extends IMapObject, ? extends MapPanel_Phase2> editObject(T object);
	
	/**
	 * Notify the layer that editing of the given object has been completed. 
	 * 
	 * @param object the object no longer being edited
	 */
	public void doneEditingObject(T object);
	
	/**
	 * Notify the layer that editing is complete for any objects that are being edited.
	 */
	public void doneEditingAllObjects();
	
	
	/**
	 * Get whether the given map object is being edited.
	 * 
	 * @param object
	 */
	public boolean isEditingMapObject(T object);
	
	/**
	 * Get whether editing is enabled for the layer.
	 * 
	 * @return
	 */
	public boolean isEditable();
	
	/**
	 * Set whether editing is enabled for the layer.
	 * 
	 * @param editable
	 */
	public void setEditable(boolean editable);	
}