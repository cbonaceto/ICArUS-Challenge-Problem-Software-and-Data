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

import java.util.HashMap;
import java.util.Map;

import org.mitre.icarus.cps.app.widgets.map.phase_1.IMapPanel_Phase1;
import org.mitre.icarus.cps.app.widgets.map.phase_1.editors.IMapObjectEditor;
import org.mitre.icarus.cps.app.widgets.map.phase_1.objects.IMapObject_Phase1;

/**
 * Basic implementation of an IEditableLayer.
 * 
 * @author Eric Kappotis
 *
 */
public abstract class AbstractEditableLayer<T extends IMapObject_Phase1> extends AbstractLayer_Phase1<T> implements IEditableLayer<T> {	
	
	/** A map of all objects currently being edited in this layer */
	protected Map<IMapObject_Phase1, ObjectAndEditor<? extends T>> editingObjects;
	
	/** The map this layer is contained in */
	protected final IMapPanel_Phase1 map;
	
	/** says whether or not the layer is editable at the moment */
	protected boolean editable;
	
	public AbstractEditableLayer(String id, IMapPanel_Phase1 map) {
		super(id);
		this.map = map;
		this.editingObjects = new HashMap<IMapObject_Phase1, ObjectAndEditor<? extends T>>();
	}

	@Override
	public void doneEditingObject(T object) {
		object.setEditing(false);
		ObjectAndEditor<? extends T> editor = editingObjects.get(object);
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
			for(ObjectAndEditor<? extends T> editor : editingObjects.values()) {
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
	protected static class ObjectAndEditor<T extends IMapObject_Phase1> {
		
		public final T object;
		
		public final IMapObjectEditor<T> editor;
		
		public ObjectAndEditor(T object, IMapObjectEditor<T> editor) {
			this.object = object;
			this.editor = editor;
		}

		public T getObject() {
			return object;
		}

		public IMapObjectEditor<T> getEditor() {
			return editor;
		}		
	}
}