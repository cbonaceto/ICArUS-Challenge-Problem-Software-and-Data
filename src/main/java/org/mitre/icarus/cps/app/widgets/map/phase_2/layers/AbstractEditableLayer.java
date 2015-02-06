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

import java.util.HashMap;
import java.util.Map;

import org.mitre.icarus.cps.app.widgets.map.objects.IMapObject;
import org.mitre.icarus.cps.app.widgets.map.phase_2.MapPanel_Phase2;
import org.mitre.icarus.cps.app.widgets.map.phase_2.editors.IMapObjectEditor;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.IMapObject_Phase2;

/**
 * Basic implementation of an IEditableLayer.
 * 
 * @author CBONACETO
 *
 */
public abstract class AbstractEditableLayer<T extends IMapObject_Phase2, M extends MapPanel_Phase2> extends AbstractLayer_Phase2<T, M> implements IEditableLayer<T, M> {	
	
	/** A map of all objects currently being edited in this layer */
	protected Map<T, ObjectAndEditor<? extends IMapObject>> editingObjects;	
	
	/** Whether the layer is editable */
	protected boolean editable;
	
	public AbstractEditableLayer(String id, M map) {
		super(id, map);
		editingObjects = new HashMap<T, ObjectAndEditor<? extends IMapObject>>();
	}
	
	/*@Override
	public IMapObjectEditor<? extends IMapObject, ? extends IcarusMapViewer> editObject(T object) {
		if(!editingObjects.containsKey(object)) {
			//Create the editor
			IMapObjectEditor<? extends IMapObject, ? extends IcarusMapViewer> editor =
					MapObjectEditorFactory.createEditor(object);
			if(editor != null) {
				editingObjects.put(object, new ObjectAndEditor<T>(object, editor));
				editor.editMapObject(object, null, null);
				//editor.editMapObject(object, this, map);
			}
			return editor;
		}
		return null;
	}*/

	@Override
	public void doneEditingObject(T object) {
		object.setEditing(false);
		ObjectAndEditor<? extends IMapObject> editor = editingObjects.get(object);
		if(editor != null) {
			if(editor.editor.isEditingMapObject()) {
				editor.editor.doneEditingMapObject();
			}
			editingObjects.remove(object);
		}
	}

	@Override
	public void doneEditingAllObjects() {
		if(!mapObjects.isEmpty()) {
			for(T object : mapObjects) {
				object.setEditing(false);
			}
		}
		if(!editingObjects.isEmpty()) {
			for(ObjectAndEditor<? extends IMapObject> editor : editingObjects.values()) {
				if(editor.editor.isEditingMapObject()) {
					editor.editor.doneEditingMapObject();
				}
			}
			editingObjects.clear();
		}
	}

	@Override
	public boolean isEditingMapObject(T object) {
		return editingObjects.containsKey(object);
	}	

	/**
	 * @return the editable
	 */
	public boolean isEditable() {
		return editable;
	}

	/**
	 * @param editable the editable to set
	 */
	public void setEditable(boolean editable) {
		if(editable != this.editable) {			
			this.editable = editable;
			for(T object : mapObjects) {
				object.setEditable(editable);
			}
			if(!editable && !editingObjects.isEmpty()) {
				//Stop editing all objects
				doneEditingAllObjects();
			}
		}
	}
	
	/**
	 * Contains a map object and its editor
	 *  
	 *
	 */
	protected static class ObjectAndEditor<T extends IMapObject> {
		
		public final T object;
		
		public final IMapObjectEditor<? extends IMapObject, ? extends MapPanel_Phase2> editor;
		
		public ObjectAndEditor(T object, IMapObjectEditor<? extends IMapObject, ? extends MapPanel_Phase2> editor) {
			this.object = object;
			this.editor = editor;
		}

		public T getObject() {
			return object;
		}

		public IMapObjectEditor<? extends IMapObject, ? extends MapPanel_Phase2> getEditor() {
			return editor;
		}		
	}
}