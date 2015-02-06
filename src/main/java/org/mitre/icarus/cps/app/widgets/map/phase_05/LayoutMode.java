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
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import org.mitre.icarus.cps.app.widgets.phase_05.ColorManager;

/**
 * List of layout modes for rendering buildings
 */
public enum LayoutMode {
	MATRIX {		
		private final Rectangle2D rect = new Rectangle2D.Double();
		
		@Override
		public void render(Graphics2D g, double tileWidth, double tileHeight, int[][] layout) {
			for (int i = 0; i < layout.length; i++) {
				
				int[] col = layout[i];
				int height = col.length;
				
				for (int j = 0; j < height; j++) {
					if (col[j] == 1) {
						// j and i are intentionally swapped here
						// otherwise the rendering is transposed
						rect.setRect(j*tileWidth, i*tileHeight, tileWidth, tileHeight);
						g.fill(rect);
					}
				}
			}
		}
	},
	
	LIST {	
		private Rectangle2D rect = new Rectangle2D.Double();
		private Line2D line = new Line2D.Double();

		@Override
		public void render(final Graphics2D g, final double tileWidth, final double tileHeight,
				final int[][] layout) {

			for (int i = 0; i < layout.length; i++) {
				
				final int x = layout[i][0];
				final int y = layout[i][1];
				
				rect.setRect(x*tileWidth-0.5, y*tileHeight-0.5,
						tileWidth+1, tileHeight+1);
				g.fill(rect);
			}
		}
		
		@Override
		public void drawAccessories(final Graphics2D g, final double tileWidth,
				final double tileHeight, final int[][] layout) {
			
			final double dWidth = (tileWidth < ACC_SIZE) ? 0.5*ACC_SIZE : tileWidth*0.5;
			final double dHeight = (tileHeight < ACC_SIZE) ? 0.5*ACC_SIZE : tileHeight*0.5;			

			for (int i = 0; i < layout.length; i++) {			
				final int x = layout[i][0];
				final int y = layout[i][1];
				
				g.setColor(ColorManager.get(ColorManager.ROOFTOP_HARDWARE));

				final double xMid = (x+0.5)*tileWidth;
				final double yMid = (y+0.5)*tileHeight;

				final double xMin = xMid - dWidth;
				final double yMin = yMid - dHeight;
				
				final double xMax = xMid + dWidth;
				final double yMax = yMid + dHeight;
				
				line.setLine(xMid, yMin, xMid, yMax);
				g.draw(line);
				
				line.setLine(xMin, yMid, xMax, yMid);
				g.draw(line);

				line.setLine(xMin, yMin, xMax, yMax);
				g.draw(line);

				line.setLine(xMax, yMin, xMin, yMax);
				g.draw(line);
			}
		}
	};

	/** Size of accessories (e.g., rooftop hardware) */
	public final static int ACC_SIZE = 6;
	
	public void drawAccessories(Graphics2D g,
			double tileWidth, double tileHeight, int[][] layout) {}
	
	public abstract void render(Graphics2D g,
			double tileWidth, double tileHeight, int[][] layout);
}