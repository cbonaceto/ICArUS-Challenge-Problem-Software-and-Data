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

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.IMapObject_Phase2;

/**
 * InformationBalloonRenderer implementation that uses a JLabel.
 * 
 * @author CBONACETO
 *
 */
public class JLabelBalloonContentRenderer<T extends Object> extends JLabel implements InformationBalloonContentRenderer<T> {
	private static final long serialVersionUID = 3852221993502846526L;

	protected T content;
	
	public JLabelBalloonContentRenderer() {
		this(null);
	}
	
	public JLabelBalloonContentRenderer(T content) {
		setHorizontalAlignment(SwingConstants.CENTER);
		setContent(content);
	}
	
	@Override
	public T getContent() {
		return content;
	}

	@Override
	public void setContent(T content) {
		this.content = content;
		if(content != null) {
			if(content instanceof IMapObject_Phase2) {
				IMapObject_Phase2 mapObject = (IMapObject_Phase2)content;
				setText(mapObject.getInformationText() != null ? mapObject.getInformationText() : 
					mapObject.getName() != null ? mapObject.getName() : "");
			} else {
				setText(content.toString());
			}
		} else {
			setText("");
		}
	}

	@Override
	public void setText(String text) {
		super.setText(text);
		setPreferredSize(null);
	}	
}