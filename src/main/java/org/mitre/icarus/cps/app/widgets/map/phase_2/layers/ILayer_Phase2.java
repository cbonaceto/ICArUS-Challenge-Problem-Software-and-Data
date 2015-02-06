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

import java.awt.Graphics2D;

import org.mitre.icarus.cps.app.widgets.map.layers.ILayer;
import org.mitre.icarus.cps.app.widgets.map.phase_2.MapPanel_Phase2;
import org.mitre.icarus.cps.app.widgets.map.phase_2.RenderProperties;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.IMapObject_Phase2;

/**
 * Interface for map layers. Layers contain map objects that are rendered on the map.
 * 
 * @author CBONACETO
 *
 */
public interface ILayer_Phase2<T extends IMapObject_Phase2, M extends MapPanel_Phase2> extends ILayer<T> {		
	
	/**
	 * Get the map this layer is contained in.
	 * 
	 * @return
	 */
	public M getMap();		
	
	/**
	 * Render the layer
	 * 
	 * @param g2d
	 * @param map
	 * @param width
	 * @param height
	 * @param renderPropertiesChanged
	 */
	public void render(Graphics2D g, RenderProperties renderProperties, boolean renderPropertiesChanged);
}