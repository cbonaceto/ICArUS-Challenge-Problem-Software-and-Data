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
import java.awt.Font;
import java.awt.Panel;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkEvent.EventType;
import javax.swing.event.HyperlinkListener;

import org.mitre.icarus.cps.app.widgets.WidgetConstants;

/**
 * @author Eric Kappotis
 *
 */
public class HyperlinkPanel extends Panel {	
	private static final long serialVersionUID = -5384111686109487842L;
	
	private JEditorPane editorPane;
	
	public HyperlinkPanel(String text) {
		editorPane = new JEditorPane();
		editorPane.setEditable(false);
		editorPane.setContentType("text/html");
		editorPane.setBackground(getBackground());		
		setFont(WidgetConstants.FONT_DEFAULT);
		setText(text);		
		add(editorPane);
	}
	
	public void setText(String text) {
		editorPane.setText(formatTextAsHTML("<a href=''>" + text + "</a>", 
				"left", getFont().getName(), WidgetConstants.FONT_SIZE_HTML_SMALL));
	}
	
	@Override
	public void setBackground(Color bg) {
		super.setBackground(bg);
		if(editorPane != null) {
			editorPane.setBackground(bg);
		}
	}
	
	@Override
	public void setFont(Font font) {
		super.setFont(font);
		if(editorPane != null) {
			editorPane.setFont(font);
			setText(editorPane.getText());
		}		
	}
	
	public void addHyperlinkListener(HyperlinkListener listener) {
		editorPane.addHyperlinkListener(listener);
	}
	
	public void removeHyperlinkListener(HyperlinkListener listener) {
		editorPane.removeHyperlinkListener(listener);
	}
	
	public static String formatTextAsHTML(String text, String orientation, String fontName, int fontSize) {
		StringBuilder html = new StringBuilder("<html>");
		
		html.append("<font face=\"");
		html.append(fontName + "\" size=\"");
		html.append(Integer.toString(fontSize) + "\">");
		
		html.append("<" + orientation + ">");		
		html.append(text);
		html.append("</" + orientation + ">");
		
		html.append("</font></html>");
		//System.out.println(html.toString());
		return html.toString();
	}	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		HyperlinkPanel hyperlinkPanel = new HyperlinkPanel("Test");
		hyperlinkPanel.addHyperlinkListener(new HyperlinkListener() {
			@Override
			public void hyperlinkUpdate(HyperlinkEvent event) {
				if(event.getEventType() == EventType.ACTIVATED) {
					System.out.println("Clicked");
				}
			}			
		});
		
		frame.getContentPane().add(hyperlinkPanel);
		
		frame.pack();
		frame.setVisible(true);
	}
}