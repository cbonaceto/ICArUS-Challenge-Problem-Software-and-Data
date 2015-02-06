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

import java.awt.Graphics2D;

import org.mitre.icarus.cps.app.widgets.map.layers.ILayer;
import org.mitre.icarus.cps.app.widgets.map.phase_1.RenderProperties;
import org.mitre.icarus.cps.app.widgets.map.phase_1.objects.IMapObject_Phase1;

public interface ILayer_Phase1<T extends IMapObject_Phase1> extends ILayer<T> {
	
	/**
	 * Render the layer
	 * 
	 * @param g2d
	 * @param renderData
	 */
	public void render(Graphics2D g2d, RenderProperties renderData, boolean renderPropertiesChanged);
}
