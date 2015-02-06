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
package org.mitre.icarus.cps.experiment_core.controller;

import org.mitre.icarus.cps.experiment_core.Experiment;
import org.mitre.icarus.cps.experiment_core.condition.Condition;
import org.mitre.icarus.cps.experiment_core.event.ConditionListener;
import org.mitre.icarus.cps.experiment_core.event.SubjectActionListener;
import org.mitre.icarus.cps.experiment_core.gui.ConditionPanel;
import org.mitre.icarus.cps.experiment_core.gui.IExperimentPanel;
import org.mitre.icarus.cps.experiment_core.subject_data.SubjectData;

/**
 * Interface for ExperimentController implementations that load, 
 * configure, and run an experiment.
 * 
 * @author cbonaceto
 *
 */
public interface IExperimentController<
	E extends Experiment<?>, 
	C extends Condition, 
	CP extends ConditionPanel> extends ConditionListener, SubjectActionListener {	
	
	public void initializeExperimentController(E experiment);
	
	public IExperimentPanel<?, ?, ?, ?> getExperimentPanel();
	//public void setExperimentPanel(IExperimentPanel<C> experimentPanel);

	public boolean isExperimentRunning();

	/** Start the experiment */
	public void startExperiment(SubjectData subjectData);

	/** Stop the experiment */
	public void stopExperiment();

	/** Go to the beginning of the given condition */
	public void skipToCondition(int conditionIndex);
}
