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
package org.mitre.icarus.cps.app.widgets.map.phase_05;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;


/**
 * Layer containing buildings/water.
 * 
 * @author Jing Hu
 *
 */
public class IMINTLayer extends Layer {
	
	/** The buildings in the IMINT layer (buildings may also be rendered as water) */
	private List<Building> buildings = new ArrayList<Building>();

	public IMINTLayer(int layerId, int width, int height) {
		super(layerId);
		setBounds(new Rectangle(0,0,width,height));
		setName("IMINT");
	}
	
	public void draw(Graphics2D g, RenderData r) {}
	
	public int getWidth() {
		return getBounds().width;
	}
	
	public int getHeight() {
		return getBounds().height;
	}
	
	public List<Building> getChildren() {
		return buildings;
	}
	
	@Override
	public LayerType getLayerType() {
		return LayerType.IMINT;
	}
}