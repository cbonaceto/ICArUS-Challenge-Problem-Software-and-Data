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
import org.mitre.icarus.cps.app.widgets.map.phase_2.editors.PlacemarkEditor;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.Placemark;

/**
 * Layer that contains placemark map objects.
 * 
 * @author CBONACETO
 *
 */
public class PlacemarkLayer<M extends MapPanel_Phase2> extends AbstractEditableLayer<Placemark, M> {

	public PlacemarkLayer(String id, M map) {
		super(id, map);
		selectable = true;
	}

	@Override
	public IMapObjectEditor<? extends IMapObject, ? extends MapPanel_Phase2> editObject(
			Placemark object) {
		if(!editingObjects.containsKey(object)) {
			//Create the placemark editor			
			PlacemarkEditor<M> editor = new PlacemarkEditor<M>();			
			editingObjects.put(object, new ObjectAndEditor<Placemark>(object, editor));
			editor.editMapObject(object, this, map);			
			return editor;
		} else {
			return editingObjects.get(object).getEditor();
		}
	}	
}