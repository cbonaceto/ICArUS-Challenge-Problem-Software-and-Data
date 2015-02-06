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
package org.mitre.icarus.cps.app.widgets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.UIManager;
//import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.border.Border;
import org.mitre.icarus.cps.exam.base.training.TutorialNavigationTree;
import org.mitre.icarus.cps.exam.base.training.TutorialTreeNode;
import org.mitre.icarus.cps.exam.base.training.TutorialTreeParentNode;

import org.mitre.icarus.cps.experiment_core.gui.ConditionPanel.BannerOrientation;

/**
 * Contains constant values that specify rendering and other properties of elements in the GUI.
 * 
 * @author CBONACETO
 *
 */
public class WidgetConstants {	
	/** Default instruction banner orientation */
	public static final BannerOrientation BANNER_ORIENTATION = BannerOrientation.Top;
	
	/** Default Insets **/
	public static final Insets INSETS_DEFAULT = new Insets(3, 3, 3, 3);
	
	/** Default Insets for buttons */
	public static final Insets INSETS_CONTROL = new Insets(1, 4, 1, 4);	
	
	public static final int VERTICAL_SPACER = 14;
	
	public static final int COMPONENT_SPACER = 5;
		
	/** Fonts */
	//public static final Font FONT_DEFAULT = new Font(Font.SANS_SERIF, Font.BOLD, 13); //new JLabel().getFont().deriveFont(13f);	
	public static final Font FONT_DEFAULT = new Font(Font.SANS_SERIF, Font.BOLD, 13); //new JLabel().getFont().deriveFont(13f);
	
	public static final Font FONT_DEFAULT_PLAIN = FONT_DEFAULT.deriveFont(Font.PLAIN);
	
	public static final Font FONT_DEFAULT_BOLD = FONT_DEFAULT.deriveFont(Font.BOLD);
	
	public static final Font FONT_STATUS = FONT_DEFAULT.deriveFont(Font.PLAIN, 16);	

	public static final Font FONT_INSTRUCTION_BANNER = FONT_DEFAULT.deriveFont(Font.PLAIN, 16);
	
	public static final Font FONT_INSTRUCTION_PANEL = FONT_DEFAULT.deriveFont(Font.PLAIN, 20);
	
	public static final Font FONT_PANEL_TITLE = FONT_DEFAULT.deriveFont(Font.BOLD, 14);
	
	public static final int FONT_SIZE_HTML = 5;
	
	public static final int FONT_SIZE_HTML_SMALL = 4;
	/*********/
	
	/** Colors */	
	public static final Color COLOR_BORDER = Color.GRAY; //Previously Color.GRAY
	
	public static final Color COLOR_LIGHT_GRAY = Color.LIGHT_GRAY;
	
	public static final Color COLOR_COMPONENT_BACKGROUND = Color.WHITE;
	
	public static final Color COLOR_COMPONENT_BACKGROUND_GRAY = new JLabel().getBackground();
	/*********/
	
	/** Whether to use symbols instead of letters for group identification */
	public static final boolean USE_GROUP_SYMBOLS = true;
	
	/** Whether to use the group colors for the probability entry components */
	public static final boolean USE_GROUP_COLORS = true;
	
	/** Default border */
	//public static final Border DEFAULT_BORDER = BorderFactory.createLineBorder(COLOR_BORDER);	
	public static final Border DEFAULT_BORDER = BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.WHITE), BorderFactory.createLineBorder(COLOR_BORDER));
	
	/** Creates a default JSeparator component. */
	public static JSeparator createDefaultSeparator() {
		JSeparator separator = new JSeparator();
		separator.setForeground(WidgetConstants.COLOR_BORDER);
		separator.setPreferredSize(new Dimension(0, 1));
		return separator;
	}
	
	/** Sets the default font for all Swing components. */
	public static void setUIFont(javax.swing.plaf.FontUIResource f) {
		java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();    
		while (keys.hasMoreElements()) {      
			Object key = keys.nextElement();     
			Object value = UIManager.get (key);     
			if (value instanceof javax.swing.plaf.FontUIResource)    
				UIManager.put(key, f);      
		}    
	}
        
        /** Tutorial tree used to size tutorial navigation trees */
	public static final List<TutorialNavigationTree> TUTORIAL_SIZING_TREES = 
		Collections.unmodifiableList(Arrays.asList(
		new TutorialNavigationTree("Exam Tutorial",
			Collections.unmodifiableList(Arrays.asList(
					new TutorialTreeParentNode(
							"Exam Tutorial", "Overview of the Task", 0),
					new TutorialTreeParentNode(
							"Exam Tutorial", "Scoring and Tactics", 8,
						Collections.unmodifiableList(Arrays.asList(
							new TutorialTreeNode("Exam Tutorial", "Showdown: P and U", 9),
							new TutorialTreeNode("Exam Tutorial", "Scoring: Blue and Red", 10),
							new TutorialTreeNode("Exam Tutorial", "Tactics: The BLUEBOOK", 11)))),
					new  TutorialTreeParentNode(
						"Exam Tutorial", "Intelligence Reports", 12,
						Collections.unmodifiableList(Arrays.asList(
							new TutorialTreeNode("Exam Tutorial", "OSINT", 14),
							new TutorialTreeNode("Exam Tutorial", "IMINT", 16),
							new TutorialTreeNode("Exam Tutorial", "BLUEBOOK", 18),
							new TutorialTreeNode("Exam Tutorial", "HUMINT", 20),
							new TutorialTreeNode("Exam Tutorial", "SIGINT", 22),
							new TutorialTreeNode("Exam Tutorial", "Notation for P(Attack|INTS)", 24))))))),
			new TutorialNavigationTree("Mission 1 Instructions"),
			new TutorialNavigationTree("Mission 2 Instructions"),
			new TutorialNavigationTree("Mission 3 Instructions"),
			new TutorialNavigationTree("Mission 4 Instructions"),
			new TutorialNavigationTree("Mission 5 Instructions"),
			new TutorialNavigationTree("Mission 6 Instructions"),
			new TutorialNavigationTree("Mission 7 Instructions")
	));		
}