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
package org.mitre.icarus.cps.app.widgets.phase_2;

import java.awt.Font;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.SwingConstants;

import org.mitre.icarus.cps.app.widgets.WidgetConstants;
import org.mitre.icarus.cps.exam.base.training.TutorialNavigationTree;
import org.mitre.icarus.cps.exam.base.training.TutorialTreeNode;
import org.mitre.icarus.cps.exam.base.training.TutorialTreeParentNode;

/**
 * Contains constant values that specify rendering and other properties of elements specific to the Phase 2 GUI.
 * 
 * @author CBONACETO
 *
 */
public class WidgetConstants_Phase2 {
	
	/** Font for location names rendered in the LocationDatumPanel */
	public static final Font FONT_LOCATION_NAME = WidgetConstants.FONT_DEFAULT; //FONT_PANEL_TITLE
	
	/** Font for datum labels rendered in DatumListPanels */
	public static final Font FONT_DATUM_LABEL = WidgetConstants.FONT_DEFAULT;
	
	/** Font for text in the score banner */
	public static final Font FONT_SCORE_BANNER = WidgetConstants.FONT_PANEL_TITLE;
	
	/** The datum value horizontal alignment for the location datum list (on left) */
	public static final int LOCATION_LIST_DATUM_VALUE_ALIGNMENT = SwingConstants.RIGHT;
	
	/** The datum value horizontal alignment for the consider datum list (on right) */
	public static final int CONSIDER_LIST_DATUM_VALUE_ALIGNMENT = SwingConstants.RIGHT;
	
	/** Vertical insets between items in a datum list */
	public static final int DATUM_LIST_VERTICAL_SPACER = 3;
	
	/** Horizontal indent level 1 for location datum lists */
	public static final int DATUM_LIST_INDENT_LEVEL_1 = 5;
	
	/** Horizontal indent level 2 for location datum lists */
	public static final int DATUM_LIST_INDENT_LEVEL_2 = 10;
	
	/** The number of report panel slots to create in the Assessment Report Panel */
	public static final int NUM_REPORT_PANELS = 2;
	
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