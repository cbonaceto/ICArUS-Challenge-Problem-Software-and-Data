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
package org.mitre.icarus.cps.app.window;

import javax.swing.UIManager;

import org.mitre.icarus.cps.app.widgets.ImageManager;
import org.mitre.icarus.cps.app.widgets.WidgetConstants;

/**
 * Contains a static method to initialize the ICArUS Look & Feel for Swing components.
 * 
 * @author CBONACETO
 *
 */
public class IcarusLookAndFeel {

	private static boolean initialized = false;

	/**
	 * Initialize the ICArUS Look and Feel for Swing components. This should be the first method called
	 * in a Swing application.
	 */
	public static void initializeICArUSLookAndFeel() {
		if(!initialized) {
			initialized = true;			
			//Use native L&F
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());					
			} catch (Exception e) { }

			//Change the default tree expansion images
			//System.out.println(ImageManager.getImage(ImageManager.MINUS_ICON));
			UIManager.put("Tree.expandedIcon", ImageManager.getImageIcon(ImageManager.MINUS_ICON));
			UIManager.put("Tree.collapsedIcon", ImageManager.getImageIcon(ImageManager.PLUS_ICON));

			//Change the default fonts
			WidgetConstants.setUIFont(new javax.swing.plaf.FontUIResource(WidgetConstants.FONT_DEFAULT));
			UIManager.put("TextArea.font", WidgetConstants.FONT_DEFAULT);
			UIManager.put("Tree.font", WidgetConstants.FONT_DEFAULT);
			UIManager.put("List.font", WidgetConstants.FONT_DEFAULT);
			UIManager.put("Table.font", WidgetConstants.FONT_DEFAULT);				
		}
	}
}
