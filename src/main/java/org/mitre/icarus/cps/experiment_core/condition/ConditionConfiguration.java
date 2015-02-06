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
package org.mitre.icarus.cps.experiment_core.condition;

import org.mitre.icarus.cps.experiment_core.Experiment;
import org.mitre.icarus.cps.experiment_core.controller.IConditionController;
import org.mitre.icarus.cps.experiment_core.controller.IExperimentController;
import org.mitre.icarus.cps.experiment_core.gui.ConditionPanel;
import org.mitre.icarus.cps.experiment_core.subject_data.ISubjectDataWriter;

public class ConditionConfiguration<
	EC extends IExperimentController<E, ?, ?>,
	E extends Experiment<?>,
	C extends Condition, 
	CP extends ConditionPanel> {	
	
	/** The condition ID */
	public final String conditionId;
	
	/** The condition */
	public final C condition;
	
	/** The controller for the condition */
	public final IConditionController<EC, E, C, CP> controller;
	
	/** The subject data writer for the condition */
	public final ISubjectDataWriter subjectDataWriter;
	
	/** The panel for the condition */
	public final ConditionPanel conditionPanel;
	
	public ConditionConfiguration(String conditionId, C condition, 
			IConditionController<EC, E, C, CP>	controller, ISubjectDataWriter subjectDataWriter, 
			ConditionPanel conditionPanel) {
		this.conditionId = conditionId;
		this.condition = condition;
		this.controller = controller;
		this.subjectDataWriter = subjectDataWriter;
		this.conditionPanel = conditionPanel;
	}

	public String getConditionId() {
		return conditionId;
	}

	public C getCondition() {
		return condition;
	}

	public IConditionController<EC, E, C, CP> getController() {
		return controller;
	}

	public ISubjectDataWriter getSubjectDataWriter() {
		return subjectDataWriter;
	}

	public ConditionPanel getConditionPanel() {
		return conditionPanel;
	}
}