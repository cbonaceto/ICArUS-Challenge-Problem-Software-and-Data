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

import java.util.HashMap;

import org.mitre.icarus.cps.app.widgets.map.objects.IMapObject;
import org.mitre.icarus.cps.app.widgets.map.phase_2.MapPanel_Phase2;

/**
 * @author CBONACETO
 *
 */
public class MapObjectEditorFactory {
	
	/** Editors that have been registered for each type of map object for which an editor exists */
	protected static final HashMap<Class<? extends IMapObject>, IMapObjectEditor<? extends IMapObject, ? extends MapPanel_Phase2>> editors = 
			new HashMap<Class<? extends IMapObject>, IMapObjectEditor<? extends IMapObject, ? extends MapPanel_Phase2>>();	
	
	public static IMapObjectEditor<? extends IMapObject, ? extends MapPanel_Phase2> createEditor(IMapObject mapObject) {
		return editors.get(mapObject.getClass()).createEditorInstance();
	}	
	
	public static void registerMapEditor(Class<? extends IMapObject> mapObjectClass,
			IMapObjectEditor<? extends IMapObject, ? extends MapPanel_Phase2> editor) {
		editors.put(mapObjectClass, editor);
	}
}