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

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.mitre.icarus.cps.app.widgets.probability_entry.IProbabilityEntryComponent;

/**
 * Implentation of a quad chart component that uses a probability component.
 * 
 * @author CBONACETO
 *
 */
public class ProbabilityQuadChartComponent implements IQuadChartComponent {
	
	/** The probability entry component */
	protected final IProbabilityEntryComponent probabilityEntryComponent;
	
	/** A JLabel to display any value */
	protected final JLabel labelComponent;	
	
	/**
	 * @param probabilityEntryComponent
	 */
	public ProbabilityQuadChartComponent(IProbabilityEntryComponent probabilityEntryComponent) {
		this.probabilityEntryComponent = probabilityEntryComponent;
		labelComponent = null;
	}
	
	/**
	 * @param font
	 */
	public ProbabilityQuadChartComponent(String text, Font font) {
		probabilityEntryComponent = null;
		labelComponent = new JLabel(text, SwingConstants.CENTER);
		labelComponent.setFont(font);
	}
	
	public IProbabilityEntryComponent getProbabilityEntryComponent() {
		return probabilityEntryComponent;
	}

	public JLabel getLabelComponent() {
		return labelComponent;
	}

	@Override
	public JComponent getComponent() {
		return labelComponent != null ? labelComponent : probabilityEntryComponent.getComponent();
	}
}