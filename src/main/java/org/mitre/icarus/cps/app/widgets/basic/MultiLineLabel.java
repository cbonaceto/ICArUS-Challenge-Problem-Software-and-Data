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
package org.mitre.icarus.cps.app.widgets.basic;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JComponent;
import javax.swing.JFrame;

import org.mitre.icarus.cps.app.widgets.WidgetConstants;
import org.mitre.icarus.cps.app.widgets.renderers.TextRenderer;

/**
 * @author Eric Kappotis
 *
 */
public class MultiLineLabel extends JComponent {

	/**
	 *  The serialization ID
	 */
	private static final long serialVersionUID = -3408608914239627240L;
	
	/**
	 * the text of the label
	 */
	private String text;
	
	/**
	 * The justification of the text
	 */
	private TextRenderer.TextJustification justification;
	
	/** The maximum width of a line of text (pixels). Default is 55. */
	private int maxWidth = 65;
	
	private boolean preferredSizeSet = false;
	
	public MultiLineLabel() {
		this("", TextRenderer.TextJustification.Center, WidgetConstants.FONT_DEFAULT);
	}
	
	public MultiLineLabel(String text) {
		this(text, TextRenderer.TextJustification.Center, WidgetConstants.FONT_DEFAULT);
	}
	
	public MultiLineLabel(String text, TextRenderer.TextJustification justification, Font font) {
		setFont(font);
		setText(text);
		setJustification(justification);
	}
	
	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the justification
	 */
	public TextRenderer.TextJustification getJustification() {
		return justification;
	}

	/**
	 * @param justification the justification to set
	 */
	public void setJustification(TextRenderer.TextJustification justification) {
		this.justification = justification;
	}
	
	public int getMaxWidth() {
		return maxWidth;
	}

	public void setMaxWidth(int maxWidth) {
		this.maxWidth = maxWidth;
	}

	@Override
	public void setPreferredSize(Dimension preferredSize) {
		super.setPreferredSize(preferredSize);
		preferredSizeSet = true;
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#getPreferredSize()
	 */
	@Override
	public Dimension getPreferredSize() {
		if(!preferredSizeSet) {
			FontMetrics fontMetrics = getFontMetrics(getFont());		
			int height = (fontMetrics.getHeight() + 3) * 3;		
			return new Dimension(maxWidth, height);
		}
		return super.getPreferredSize();
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(Graphics graphics) {
		Graphics2D g2d = (Graphics2D)graphics;
		Color origColor = g2d.getColor();
		g2d.setFont(getFont());
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
	
		Rectangle drawingBounds = new Rectangle(0, 0, getSize().width, getSize().height);
		TextRenderer renderer = new TextRenderer();

		renderer.renderText(g2d, drawingBounds, this.getText(), justification);

		g2d.setColor(origColor);
	}	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		MultiLineLabel label = new MultiLineLabel("Test this label thingy out.", TextRenderer.TextJustification.Center,
				WidgetConstants.FONT_DEFAULT);
		
		frame.getContentPane().add(label);
		
		frame.pack();
		frame.setVisible(true);
	}

}
