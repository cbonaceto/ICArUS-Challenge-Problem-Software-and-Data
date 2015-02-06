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
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.Collection;

import javax.swing.SwingConstants;

import org.mitre.icarus.cps.app.widgets.WidgetConstants;

/**
 * Text annotation.  Annotations are positioned within the bounds of a related feature
 * using the horizontal and vertical alignment settings. 
 * 
 * @author CBONACETO
 *
 */
public class AnnotationFeature extends Feature implements SwingConstants {	
	/** The annotation text */
	private String text;
	
	/** The Feature the annotation is for */
	private Feature feature;	
	
	/** Horizontal text position (default is center) */
	private int horizontalTextPosition = AnnotationFeature.CENTER;	
	
	/** Vertical text position (default is center) */
	private int vertialTextPosition = AnnotationFeature.CENTER;	
	
	/** The font to use */
	private Font font = WidgetConstants.FONT_DEFAULT;
	
	/** Text color */
	private Color foreground = Color.black;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Feature getFeature() {
		return feature;
	}

	public void setFeature(Feature feature) {
		this.feature = feature;
	}

	public int getHorizontalTextPosition() {
		return horizontalTextPosition;
	}

	public void setHorizontalTextPosition(int horizontalTextPosition) {
		this.horizontalTextPosition = horizontalTextPosition;
	}

	public int getVertialTextPosition() {
		return vertialTextPosition;
	}

	public void setVertialTextPosition(int vertialTextPosition) {
		this.vertialTextPosition = vertialTextPosition;
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}	

	public Color getForeground() {
		return foreground;
	}

	public void setForeground(Color foreground) {
		this.foreground = foreground;
	}

	@Override
	public void draw(Graphics2D g, RenderData r) {
		if(text != null && feature != null && feature.getBounds() != null) {
			g.setColor(foreground);
			Font origFont = g.getFont();		
			g.setFont(font);
			
			//Rectangle bounds = feature.getBounds();			
			Rectangle2D bounds = new Rectangle2D.Double();			
			Rectangle b = feature.getBounds();
			bounds.setFrame(0, 0,
					b.width*r.tileWidth, b.height*r.tileHeight);			
			Rectangle2D strBounds = g.getFontMetrics().getStringBounds(text, g);			
			
			//Position text horizontally
			double x = bounds.getX();
			switch(horizontalTextPosition) {
			/*case LEFT: 
				x = bounds.x;
				break;*/
			case RIGHT:
				x += bounds.getWidth() - strBounds.getWidth();
				break;
			case CENTER:
				x += (int)((bounds.getWidth() - strBounds.getWidth())/2.d);
				break;
			}
			
			//Position text vertically
			double y = bounds.getY();
			switch(vertialTextPosition) {
			case TOP:
				y -= g.getFontMetrics().getDescent();
			 	//y += strBounds.getHeight();
				break;
			case BOTTOM:
				//y += bounds.getHeight() - g.getFontMetrics().getDescent();
				y += bounds.getHeight() + strBounds.getHeight();
				break;
			case CENTER:
				y += (int)((bounds.getHeight() - strBounds.getHeight())/2.d) + strBounds.getHeight();
				break;
			}
			
			//Draw text
			g.drawString(text, (int)x, (int)y);
			
			g.setFont(origFont);
		}
	}

	@Override
	public Collection<? extends Feature> getChildren() {
		return null;
	}	
}