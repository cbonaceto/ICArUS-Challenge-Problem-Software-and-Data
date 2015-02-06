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

import java.util.LinkedList;

import javax.swing.JComponent;

import org.mitre.icarus.cps.app.widgets.WidgetConstants;
import org.mitre.icarus.cps.experiment_core.condition.InstructionsPage;
import org.mitre.icarus.cps.experiment_core.gui.InstructionsPanel;
import org.mitre.icarus.cps.experiment_core.gui.ConditionPanel;
import org.mitre.icarus.cps.experiment_core.gui.IConditionComponent;

/**
 * Exam tutorial panel. Contains a single InstructionsAndWidgetPanel instance.
 * 
 * @author CBONACETO
 *
 */
public class TutorialPanel extends ConditionPanel {
	
	private static final long serialVersionUID = 1L;
	
	protected InstructionsPanel instructionsAndWidgetPanel;

	public TutorialPanel() {
		super(false, null);
		setBackground(WidgetConstants.COLOR_COMPONENT_BACKGROUND);
		instructionsAndWidgetPanel = new InstructionsPanel("instructionsAndWidget");
		instructionsAndWidgetPanel.setBackground(WidgetConstants.COLOR_COMPONENT_BACKGROUND);
		
		LinkedList<IConditionComponent> components = new LinkedList<IConditionComponent>();
		components.add(instructionsAndWidgetPanel);		
		setConditionComponents(components);
	}	
	
	public JComponent getInstructionsWidget() {
		return instructionsAndWidgetPanel.getInstructionsWidget();
	}
		
	public void setInstructionsWidget(final JComponent widget) {
		instructionsAndWidgetPanel.setInstructionsWidget(widget);
	}	
	
	@Override
	public void setInstructionsPage(final InstructionsPage page) {		
		instructionsAndWidgetPanel.setInstructionsPage(page);
	}
	
	public void showInstructionsAndWidgetPanel() {
		setConditionComponent(instructionsAndWidgetPanel);
	}
}