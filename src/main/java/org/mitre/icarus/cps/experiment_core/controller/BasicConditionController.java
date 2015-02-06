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

import java.util.LinkedList;

import org.mitre.icarus.cps.experiment_core.Experiment;
import org.mitre.icarus.cps.experiment_core.condition.Condition;
import org.mitre.icarus.cps.experiment_core.event.ConditionEvent;
import org.mitre.icarus.cps.experiment_core.event.ConditionListener;
import org.mitre.icarus.cps.experiment_core.gui.ConditionPanel;

/**
 * @author CBONACETO
 *
 * @param <C>
 * @param <CP>
 */
public abstract class BasicConditionController<
	EC extends IExperimentController<E, ?, ?>,
	E extends Experiment<?>,
	C extends Condition, 
	CP extends ConditionPanel> implements IConditionController<EC, E, C, CP> {
	
	/** List of listeners that have registered to receive condition events fired
	 * by this controller */
	protected transient LinkedList<ConditionListener> listeners;
	
	/** Whether or not the condition is executing */
	protected boolean conditionRunning;

	@Override
	public synchronized void addConditionListener(ConditionListener listener) {
		if(listeners == null) {
			listeners = new LinkedList<ConditionListener>();
		}
		if(!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	@Override
	public synchronized void removeConditionListener(ConditionListener listener) {
		if(listeners != null) {
			listeners.remove(listener);
		}
	}
	
	@Override
	public boolean isConditionRunning() {
		return conditionRunning;
	}	
	
	/** Set the trial number and trial part number and fire a trial changed event */
	protected boolean setTrial(int trialNum, int trialPartNum, int numTrialParts) {
		getCondition().setCurrentTrial(trialNum);
		if(trialNum > getCondition().getNumTrials()) {
			return true;
		}
		fireConditionEvent(new ConditionEvent(ConditionEvent.TRIAL_CHANGED, 
				trialNum, trialPartNum, numTrialParts, this));
		return false;
	}
	
	/** Increment the trial number and fire a trial changed event */
	protected boolean incrementTrial() {		
		int trialNum = getCondition().getCurrentTrial()+1;
		if(trialNum > getCondition().getNumTrials()) {
			return true;
		}
		getCondition().setCurrentTrial(trialNum);
		fireConditionEvent(new ConditionEvent(ConditionEvent.TRIAL_CHANGED, 
				trialNum, this));
		return false;
	}
	
	/** Fire a condition event to all registered listeners */
	protected synchronized void fireConditionEvent(ConditionEvent event) {
		if(listeners != null) {
			for(ConditionListener listener : listeners) {
				listener.conditionActionPerformed(event);
			}
		}
	}
}