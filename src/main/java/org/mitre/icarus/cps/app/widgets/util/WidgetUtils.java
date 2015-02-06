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
package org.mitre.icarus.cps.app.widgets.util;

import java.awt.Color;

import javax.swing.SwingConstants;

public class WidgetUtils {
	
	public static final int LEFT = SwingConstants.LEFT;
	
	public static final int RIGHT = SwingConstants.RIGHT;
	
	public static final int CENTER = SwingConstants.CENTER;
	
	/**
	 * @param fontColor
	 * @return
	 */
	public static String createHtmlFontString(Color fontColor) {
		StringBuilder sb = new StringBuilder("<font color=\"rgb(");
		sb.append(Integer.toString(fontColor.getRed()) + ",");
		sb.append(Integer.toString(fontColor.getGreen()) + ",");
		sb.append(Integer.toString(fontColor.getBlue()));
		sb.append("\")>");
		return sb.toString();
	}
	
	/*public static void main(String[] args) {
		JFrame frame = new JFrame("Test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JLabel label = new JLabel();
		StringBuilder text = new StringBuilder("<html>");
		text.append(createHtmlFontString(Color.red));
		text.append("This is a test");
		text.append("</font></html>");
		label.setText(text.toString());
		frame.getContentPane().add(label);
		frame.pack();
		frame.setVisible(true);
		System.out.println(createHtmlFontString(Color.red));
	}*/
	
	/**
	 * Given a string and a line width in characters, return a string
	 * with line breaks such that the maximum number of characters
	 * on a line is lineWidth.  Formats the string as HTML and uses
	 * the br tag for line breaks.
	 * 
	 * @param str
	 * @param lineWidth
	 * @param alignment
	 * @return
	 */
	public static String formatMultilineString(String str, int lineWidth, int alignment) {
		StringBuilder sb = new StringBuilder("<html>");
		if(alignment == CENTER) {
			sb.append("<center>");
		}

		int startIndex = 0;
		int lastSpaceIndex = 0;
		int currCounter = 0;		
		
		for(int x = 0; x < str.length(); x++) {				
			currCounter++;								
			if(str.charAt(x) == ' '){
				lastSpaceIndex = x;
			}								
			if(currCounter >= lineWidth) {
				//Insert line break
				if(str.charAt(x) == ' ' || startIndex == lastSpaceIndex){
					sb.append(str.substring(startIndex, x));
					//if(startIndex != lastSpaceIndex) {
					sb.append("<br>");
					//}
				}
				else {					
					sb.append(str.substring(startIndex, lastSpaceIndex));
					sb.append("<br>");
					x = lastSpaceIndex;
				}				
				startIndex = x;
				currCounter = 0;
			}
		}	
		if(startIndex < str.length()) {
			if(str.charAt(startIndex) == ' ') {
				if(startIndex + 1 < str.length()) {
					sb.append(str.substring(startIndex+1, str.length()));
				}
			}
			else {
				sb.append(str.substring(startIndex, str.length()));
			}
		}
		if(alignment == CENTER) {
			sb.append("</center>");
		}
		sb.append("</html>");
		return sb.toString();
	}
}
