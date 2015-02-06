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
package org.mitre.icarus.cps.app.widgets.probability_entry.distinct_bars;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JComponent;

import org.mitre.icarus.cps.app.widgets.probability_entry.ProbabilityEntryConstants;

/**
 * A basic box component.
 * 
 * @author CBONACETO
 *
 */
public class Box extends JComponent {
	private static final long serialVersionUID = 1L;
	
	/** Box border thickness */
	protected int boxBorderThickness = ProbabilityEntryConstants.BOX_BORDER_THICKNESS;
	
	/** Box border color */
	protected Color boxBorderColor = ProbabilityEntryConstants.COLOR_BOX_BORDER;
	
	/** Box border color when disabled */
	protected Color boxBorderColorDisabled = ProbabilityEntryConstants.COLOR_BOX_BORDER_DISABLED;
	
	/** Box background fill color */
	protected Color boxFillColor = ProbabilityEntryConstants.COLOR_BOX_BACKGROUND;	
	
	/** Box background fill color when disabled */
	protected Color boxFillColorDisabled = ProbabilityEntryConstants.COLOR_BOX_BACKGROUND_DISABLED;
	
	/** Whether the box is enabled */
	protected boolean enabled = true;	
	
	public Box(Dimension boxSize) {
		super();		
		setPreferredSize(boxSize);
		setOpaque(true);
	}

	public int getBoxBorderThickness() {
		return boxBorderThickness;
	}

	public void setBoxBorderThickness(int boxBorderThickness) {
		this.boxBorderThickness = boxBorderThickness;
		repaint();
	}

	public Color getBoxBorderColor() {
		return boxBorderColor;
	}

	public void setBoxBorderColor(Color boxBorderColor) {
		this.boxBorderColor = boxBorderColor;
		repaint();
	}

	public Color getBoxFillColor() {
		return boxFillColor;
	}

	public void setBoxFillColor(Color boxFillColor) {
		this.boxFillColor = boxFillColor;
		repaint();
	}
	
	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		
		//Draw the box
		Rectangle bounds = getBounds();
		g2d.setColor(boxFillColor);
		//g2d.setColor(Color.black);
		g2d.fillRect(0, 0, bounds.width, bounds.height);
	
		//Draw the border
		drawBorder(g2d);
	}
	
	/** Draws the border */
	protected void drawBorder(Graphics2D g2d) {
		if(boxBorderThickness > 0) {
			Rectangle bounds = getBounds();
			g2d.setStroke(new BasicStroke(boxBorderThickness));
			if(enabled) {
				g2d.setColor(boxBorderColor);
			}
			else {
				g2d.setColor(boxBorderColorDisabled);
			}
			int halfBorderThickness = boxBorderThickness/2;
			g2d.drawRect(halfBorderThickness, halfBorderThickness, 
					bounds.width - boxBorderThickness, bounds.height - boxBorderThickness);
		}
	}	
}