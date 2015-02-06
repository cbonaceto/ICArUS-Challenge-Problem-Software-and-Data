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
package org.mitre.icarus.cps.app.widgets.map.phase_2.objects.annotation;

import javax.swing.SwingConstants;

import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.IMapObject_Phase2;

/**
 * @author CBONACETO
 *
 */
public class ToolTipRenderer extends JLabelBalloonContentRenderer<IMapObject_Phase2> {

	private static final long serialVersionUID = 1L;
	
	public ToolTipRenderer(IMapObject_Phase2 content) {
		setHorizontalAlignment(SwingConstants.CENTER);
		setContent(content);
	}
	
	@Override
	public void setContent(IMapObject_Phase2 content) {
		this.content = content;
		if(content != null) {
			setText(content.getToolTipText());			
		} else {
			setText("");
		}
	}
}