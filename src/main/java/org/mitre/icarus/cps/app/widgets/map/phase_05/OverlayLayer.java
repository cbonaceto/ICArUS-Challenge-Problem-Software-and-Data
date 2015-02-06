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

import java.awt.Color;
import java.awt.Graphics2D;
//import java.awt.image.BufferedImage;
import java.util.Collection;



/**
 * @author Jing Hu
 *
 */
public class OverlayLayer extends Layer {
	
	//private BufferedImage compass;
	
	public OverlayLayer(int layerId) {
		super(layerId);
		//compass = ImageManager.getImage("images/compass.png");
		setName("Overlay");
	}

	@Override
	public void draw(Graphics2D g, RenderData r) {
		
		// compass
		// eao - don't display compass rose
		//final int imageWidth = compass.getWidth();		
		//g.drawImage(compass, r.width - imageWidth, 0, null);
		
		// scale bar
		
		int y = r.height - 24;
		int x = r.width - 300;
		g.setColor(Color.BLACK);
		int scaleWidth = 256;
		g.drawLine(x, y, x + scaleWidth, y);
		
		int ticks = 4;
		for (int i = 0; i <= ticks; i++) {
			int x1 = x + i*scaleWidth/ticks;
			g.drawLine(x1, y+5, x1, y-5);
		}
	}

	@Override
	public Collection<? extends Feature> getChildren() {
		return null;
	}
	
	@Override
	public LayerType getLayerType() {
		return LayerType.Overlay;
	}
}