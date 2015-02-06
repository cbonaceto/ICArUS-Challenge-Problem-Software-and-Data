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
package org.mitre.icarus.cps.app.widgets.map.phase_1.objects;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;

import org.mitre.icarus.cps.app.widgets.map.objects.IMapObject;
import org.mitre.icarus.cps.app.widgets.map.phase_1.RenderProperties;
import org.mitre.icarus.cps.app.widgets.map.phase_1.layers.ILayer_Phase1;

/**
 * Interface specification for objects rendered on the map.
 * 
 * @author CBONACETO
 *
 */
public interface IMapObject_Phase1 extends IMapObject {	
	
	public ILayer_Phase1<? extends IMapObject_Phase1> getLayer();	
	public void setLayer(ILayer_Phase1<? extends IMapObject_Phase1> layer);	
	
	public boolean contains(Point point);	
	public boolean contains(Point2D point);	
	public boolean contains(double x, double y);	
	
	public void render(Graphics2D g, RenderProperties renderData, boolean renderPropertiesChanged);
}