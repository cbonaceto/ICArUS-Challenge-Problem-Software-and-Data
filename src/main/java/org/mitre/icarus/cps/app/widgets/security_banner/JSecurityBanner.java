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
package org.mitre.icarus.cps.app.widgets.security_banner;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.mitre.icarus.cps.app.widgets.WidgetConstants;

/**
 * Security banner display.
 * 
 * @author CBONACETO
 *
 */
public class JSecurityBanner extends JPanel {
	private static final long serialVersionUID = 1L;
	
	public static final Character COPYRIGHT_CHAR = '\u00a9';
	
	/** Classification levels */
	public static enum Classification {None, Internal, FOUO, Unclassified, Secret, TopSecret};
	public static final String[] CLASSIFICATION_NAMES = {"", "Not For Public Release.", "For Official Use Only, Not For Public Release.", 
            "UNCLASSIFIED//FOUO", "SECRET", "TOP SECRET"};
	
	//public static final Color UNCLASS_BACKGROUND = new Color(0, 215, 0);
	public static final Color UNCLASS_BACKGROUND = new JPanel().getBackground();
	public static final Color UNCLASS_FOREGROUND = Color.black;
	
	public static final Color CLASS_BACKGROUND = new Color(215, 0, 0);
	public static final Color CLASS_FOREGROUND = Color.white;
	
	public JSecurityBanner(String copyrightText, Classification classification) {
		 if(classification == null) {
			 classification = Classification.None;
		 }
		 
		 JLabel bannerLabel = new JLabel(copyrightText + CLASSIFICATION_NAMES[classification.ordinal()]);
		 bannerLabel.setFont(WidgetConstants.FONT_DEFAULT_PLAIN);
		 bannerLabel.setHorizontalAlignment(JLabel.CENTER);
		 bannerLabel.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
		 bannerLabel.setOpaque(true);
		 if(classification == Classification.None || classification == Classification.FOUO ||
				 classification == Classification.Unclassified) {		 
			 bannerLabel.setBackground(UNCLASS_BACKGROUND);
			 bannerLabel.setForeground(UNCLASS_FOREGROUND);
		 }
		 else {
			 bannerLabel.setBackground(CLASS_BACKGROUND);
			 bannerLabel.setForeground(CLASS_FOREGROUND);
		 }		 
		 setLayout(new BorderLayout());		 
		 add(bannerLabel, BorderLayout.CENTER);
	}
}