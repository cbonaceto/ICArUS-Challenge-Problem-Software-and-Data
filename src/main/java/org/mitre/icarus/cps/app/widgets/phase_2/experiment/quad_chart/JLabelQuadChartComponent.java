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
package org.mitre.icarus.cps.app.widgets.phase_2.experiment.quad_chart;

import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * JLabel implementation of a quad chart component.
 * 
 * @author CBONACETO
 *
 */
public class JLabelQuadChartComponent extends JLabel implements IQuadChartComponent {
	
	private static final long serialVersionUID = 1885360554618205325L;
	
	public JLabelQuadChartComponent() {
		super("", SwingConstants.CENTER);
	}
	
	public JLabelQuadChartComponent(String text) {
		super(text, SwingConstants.CENTER);
	}
	
	public JLabelQuadChartComponent(String text, Font font) {
		super(text, SwingConstants.CENTER);
		setFont(font);
	}

	@Override
	public JLabel getComponent() {
		return this;
	}
}