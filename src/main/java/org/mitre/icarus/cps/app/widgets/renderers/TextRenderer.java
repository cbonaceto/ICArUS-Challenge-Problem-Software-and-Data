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
package org.mitre.icarus.cps.app.widgets.renderers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class TextRenderer {
	
	private String[] words;
	private String currentWord;
	private Point currLocation;
	
	public static enum TextJustification {Left, Right, Center};
	
	public void renderText(Graphics2D g2d, Rectangle bounds, String text) {
		renderText(g2d, bounds, text, TextJustification.Left);
	}
	
	public void renderText(Graphics2D g2d, Rectangle bounds, String text, TextJustification justification){
		ArrayList<String> textLines = calculateStringInformation(g2d, bounds, text, justification);
	
		ArrayList<String> processedList = getFinalArrayList(textLines, bounds, g2d);
		
		Point currentLocation = new Point(bounds.x, bounds.y);
		for(String currentLine : processedList) {
			
			int xLocation = 0;

			if(justification == TextJustification.Left) {
				xLocation = currentLocation.x;					
			}
			else if(justification == TextJustification.Center) {
				int textWidth = getStringBounds(currentLine.toString(), g2d).width;
				xLocation = bounds.x + (bounds.width - textWidth) / 2;
			}
			else if(justification == TextJustification.Right) {
				int textWidth = getStringBounds(currentLine.toString(), g2d).width;
				xLocation = bounds.x + bounds.width - textWidth;
			}
			
			//System.out.println(currentLine);
			Rectangle stringBounds = g2d.getFontMetrics().getStringBounds(currentLine, g2d).getBounds();
			g2d.drawString(currentLine, xLocation, currentLocation.y + g2d.getFontMetrics().getAscent());
			currentLocation.y += stringBounds.height;
		}
	}
	
	private ArrayList<String> getFinalArrayList(ArrayList<String> textLines, Rectangle bounds, Graphics2D g2d) {
		ArrayList<String> finalProcessedList = new ArrayList<String>();
		
		Point location = new Point(bounds.x, bounds.y);
		
		int lineCounter = 0;
		for(String line : textLines) {			
			Rectangle stringBounds = g2d.getFontMetrics().getStringBounds(line, g2d).getBounds();
			if((location.y + stringBounds.height) >= (bounds.y + bounds.height)) {
				// if there is a previous line, end it with three dots
				if(lineCounter > 0 && !finalProcessedList.isEmpty()) {
					String lastLine = textLines.get(lineCounter - 1);

					finalProcessedList.set(lineCounter - 1, abbreviateString(lastLine));
					break;
				}
			}
			else {
				finalProcessedList.add(line);
			}
			
			location.y += stringBounds.height;
			
			//g2d.setColor(Color.BLACK);
			
			lineCounter++;
		}
		
		return finalProcessedList;
	}
	
	private ArrayList<String> calculateStringInformation(Graphics2D g2d, Rectangle bounds, String text, TextJustification justification) {
		
		ArrayList<String> stringList = new ArrayList<String>();
		
		if(justification != TextJustification.Left && 
				justification != TextJustification.Center && 
				justification != TextJustification.Right) {
			
			System.err.println("No Justification Provided");
			throw new IllegalArgumentException("Invalid Justification Provided");			
		}
		
		currLocation = new Point(bounds.x, bounds.y);
		words = text.split(" ");
		StringBuilder tempLine = null;
		StringBuilder textLine = null;
		//String tempLine = "";
		
		for(int x = 0; x < words.length; x++){
			currentWord = words[x];
			
			if(x == 0){
				tempLine = new StringBuilder(currentWord);
			}
			// we do add a space because it is not a newline
			else {
				tempLine.append(" ");
				tempLine.append(currentWord);
			}
			// if this happens we hit a newline so render the line, increment the spot for the next line, and start a new text line
			Rectangle stringBounds = getStringBounds(tempLine.toString(), g2d);
			Point textExtent = new Point((int)stringBounds.getWidth(), (int)stringBounds.getHeight());
			
			if(textExtent.x > bounds.width){
				// render the line where it belongs				
				if(textLine != null) {
					stringList.add(textLine.toString());
				}
				
				// the beginning of a new line
				textLine = new StringBuilder(currentWord);
				tempLine = new StringBuilder(currentWord);
				
				// update the current Location for next time through
				currLocation.y += textExtent.y;
			}
			// this word fits into this line so append it onto the current text line
			else {
				// the first time through this will not be set
				if(x == 0){
					textLine = new StringBuilder(currentWord);
				}
				else{
					textLine.append(" ");
					textLine.append(currentWord);
				}				
			}
		}		
		stringList.add(textLine.toString());
		
		return stringList;
	}
	
	private Rectangle getStringBounds(String text, Graphics2D g2d) {
		return g2d.getFontMetrics().getStringBounds(text, g2d).getBounds();
	}
	
	private String abbreviateString(String text) {
		
		char[] charArray = text.toCharArray();
		
		for(int x = text.toCharArray().length - 1; x >=0; x--) {
			if(charArray[x] != ' ') {
				charArray[x] = '.';
				charArray[x - 1] = '.';
				charArray[x - 2] = '.';
				break;
			}
		}
		return new String(charArray);
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JComponent testRenderComponent = new JComponent() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1719360506812650150L;

			/* (non-Javadoc)
			 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
			 */
			@Override
			protected void paintComponent(Graphics graphics) {
				super.paintComponent(graphics);
				
				Graphics2D g2d = (Graphics2D)graphics;
				Color previousColor = g2d.getColor();
				
				g2d.setColor(Color.CYAN);
				g2d.fill(getBounds());
				
				g2d.setColor(Color.BLACK);
				new TextRenderer().renderText(g2d, getBounds(), "The quick brown fox jumps over the lazy dog.  The quick brown fox jumps over the lazy dog.", TextJustification.Center);
				
				g2d.setColor(previousColor);
			}

			/* (non-Javadoc)
			 * @see javax.swing.JComponent#getPreferredSize()
			 */
			@Override
			public Dimension getPreferredSize() {
				return new Dimension(150, 50);
			}		
		};
		
		frame.getContentPane().add(testRenderComponent);
		
		frame.pack();
		frame.setVisible(true);
	}
}
