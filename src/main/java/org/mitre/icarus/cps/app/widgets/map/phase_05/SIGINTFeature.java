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
import java.awt.geom.Ellipse2D;
import java.awt.geom.RectangularShape;
import java.util.List;

import org.mitre.icarus.cps.app.widgets.phase_05.ColorManager;
import org.mitre.icarus.cps.app.widgets.phase_05.WidgetConstants_Phase05;

/**
 * Draws a SIGINT hit.
 * 
 * @author Jing Hu
 *
 */
public class SIGINTFeature extends Feature {
	
	private final RectangularShape shape = new Ellipse2D.Double();
	
	@Override
	public void draw(Graphics2D g, RenderData r) {
		g.setColor(ColorManager.get(ColorManager.SIGINT));
		double tileWidth = r.tileWidth;
		if (tileWidth < WidgetConstants_Phase05.INT_HIT_RENDER_SIZE) {
			tileWidth = WidgetConstants_Phase05.INT_HIT_RENDER_SIZE;
		}
		
		double tileHeight = r.tileHeight;
		if (tileHeight < WidgetConstants_Phase05.INT_HIT_RENDER_SIZE) {
			tileHeight = WidgetConstants_Phase05.INT_HIT_RENDER_SIZE;
		}
		
		final long circleWidth = Math.round(tileWidth*0.4);
		final long circleHeight = Math.round(tileHeight*0.4);
		
		//shape.setFrame(2, 2, tileWidth-4, tileHeight-4);
		shape.setFrame((tileWidth-circleWidth)/2.d, (tileHeight-circleHeight)/2.d, 
				circleWidth, circleHeight);		
		g.draw(shape);
	}

	@Override
	public List<? extends Feature> getChildren() {
		return null;
	}
}
