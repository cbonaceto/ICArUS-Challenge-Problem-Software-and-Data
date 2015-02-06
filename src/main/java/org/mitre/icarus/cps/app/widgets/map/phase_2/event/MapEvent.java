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
package org.mitre.icarus.cps.app.widgets.map.phase_2.event;

import org.mitre.icarus.cps.app.widgets.map.phase_2.MapPanel_Phase2;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.IMapObject_Phase2;

/**
 * Map event class.
 * 
 * @author CBONACETO
 *
 */
public class MapEvent {
	
	/** The map that generated the event */
	public final MapPanel_Phase2 map;
	
	/** The map object associated with the event */
	public final IMapObject_Phase2 mapObject;

	public MapEvent(MapPanel_Phase2 map, IMapObject_Phase2 mapObject) {
		this.map = map;
		this.mapObject = mapObject;
	}

	public MapPanel_Phase2 getMap() {
		return map;
	}

	public IMapObject_Phase2 getMapObject() {
		return mapObject;
	}
}