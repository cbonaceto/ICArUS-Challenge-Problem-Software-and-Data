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
package org.mitre.icarus.cps.app.widgets.basic.tutorial;

import java.awt.Component;
import java.io.Serializable;

/**
 * @author CBONACETO
 *
 */
public class TutorialTreeSelectionEvent implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public final Component source;
	
	/** The name of the tutorial for the selection */
	public final String tutorialName;
	
	/** The name of the tutorial page or section for the selection */
	public final String pageOrSectionName;
	
	/** The page number of the tutorial page for the selected */
	public Integer pageIndex;
	
	//public final TutorialTreeNode node;

	public TutorialTreeSelectionEvent(Component source, String tutorialName,
			String pageOrSectionName, Integer pageIndex) {
		this.source = source;
		this.tutorialName = tutorialName;
		this.pageOrSectionName = pageOrSectionName;
		this.pageIndex = pageIndex;
	}

	public Component getSource() {
		return source;
	}

	public String getTutorialName() {
		return tutorialName;
	}

	public String getPageOrSectionName() {
		return pageOrSectionName;
	}

	public Integer getPageIndex() {
		return pageIndex;
	}
}