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
package org.mitre.icarus.cps.app.widgets.map.phase_2.editors;

import java.awt.event.KeyListener;

import javax.swing.event.MouseInputListener;

import org.mitre.icarus.cps.app.widgets.map.phase_2.MapPanel_Phase2;
import org.mitre.icarus.cps.app.widgets.map.phase_2.layers.IEditableLayer;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.IMapObject_Phase2;

/**
 * Interface for implementations that control the editing of map objects.
 * 
 * @author CBONACETO
 *
 * @param <T>
 */
public interface IMapObjectEditor<T extends IMapObject_Phase2, M extends MapPanel_Phase2> {
	
	/**
	 * @return
	 */
	public IMapObjectEditor<T, M> createEditorInstance();
	
	/**
	 * @return
	 */
	public T getEditingMapObject();
	
	/**
	 * @return
	 */
	public boolean isEditingMapObject();
	
	/** Return whether the editor is "armed", that is, the cursor is over a control point
	 * and the cursor shape has changed */
	public boolean isEditorArmed();
	
	/**
	 * @param object
	 * @param abstractEditableLayer
	 * @param map
	 */
	public void editMapObject(T mapObject, IEditableLayer<? super T, ? super M> layer, M map);		
	
	/**
	 * 
	 */
	public void doneEditingMapObject();
	
	/**
	 * @return
	 */
	public MouseInputListener getMouseListener();
	
	//public MouseWheelListener getMouseWheelListener();
	
	/**
	 * @return
	 */
	public KeyListener getKeyListener();	
}