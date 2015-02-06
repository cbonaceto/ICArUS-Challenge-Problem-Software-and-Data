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
import org.mitre.icarus.cps.experiment_core.subject_data.SubjectConditionData;

/**
 * Interface for condition controllers.  Condition controllers execute a condition 
 * (e.g., it advances from one trial to the next, records data, etc.).  The experiment
 * controller will notify a condition controller of a back/next button press by
 * calling its subjectActionPerformed method.
 * 
 * @author cbonaceto
 *
 */
public interface IConditionController<
	EC extends IExperimentController<E, ?, ?>,
	E extends Experiment<?>,
	C extends Condition, 
	CP extends ConditionPanel> extends SubjectActionListener {
	
	/** Set the condition data and condition panel to use */
	public void initializeConditionController(C condition, CP conditionPanel);
	
	public C getCondition();
	
	/** Start the condition at the given trial number */
	public void startCondition(int firstTrial, EC experimentController, SubjectConditionData subjectConditionData);
	
	/** Stop the condition */
	public void stopCondition();
	
	/** Return whether or not the condition is currently executing */
	public boolean isConditionRunning();
	
	/** Add a condition event listener */
	public void addConditionListener(ConditionListener listener);
	
	/** Remove a condition event listener */
	public void removeConditionListener(ConditionListener listener);	
}