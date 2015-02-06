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
import java.util.ArrayList;
import java.util.List;


/**
 * Layer containing SIGINT hits.
 * 
 * @author Jing Hu
 *
 */
public class SIGINTLayer extends Layer {
	
	private List<SIGINTFeature> features = new ArrayList<SIGINTFeature>();
	
	public SIGINTLayer(int layerId) {
		super(layerId);
		setName("SIGINT");
	}
	
	@Override
	public void draw(Graphics2D g, RenderData r) {
	/*	g.setColor(ColorManager.get(ColorManager.SECTOR));
		Rectangle2D rect = new Rectangle2D.Double();
		
		Rectangle b = getBounds();
		rect.setFrame(0, 0,
				b.width*r.tileWidth, b.height*r.tileHeight);
		
		g.draw(rect);*/
	}
	
	@Override
	public List<SIGINTFeature> getChildren() {
		return features;
	}
	
	@Override
	public LayerType getLayerType() {
		return LayerType.SIGINT;
	}
}