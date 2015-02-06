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
package org.mitre.icarus.cps.experiment_core.event;

import java.util.EventObject;

/**
 * Class for condition events fired by condition controllers.
 * 
 * @author CBONACETO
 *
 */
public class ConditionEvent extends EventObject {
	private static final long serialVersionUID = 1L;
	
	/** Condition event type constants */
	public static final int TRIAL_CHANGED = 0;
	public static final int CONDITION_COMPLETED = 1;	
	public static final int NEXT_EVENT = 2;
	
	/** The condition event type */
	public final int eventType;
	
	/** The current trial number */
	public final int trialNum;
	
	/** The current trial part number (or -1 if the trial consists of a single trial part) */
	public final int trialPartNum;
	
	/** The number of trial parts */
	public final int numTrialParts;
	
	/** User-defined data associated with the event */
	public final Object eventData;
	
	public ConditionEvent(int eventType, int trialNum, Object source) {
		this(eventType, trialNum, -1, -1, source, null);
	}
	
	public ConditionEvent(int eventType, int trialNum, int trialPartNum, int numTrialParts, Object source) {
		this(eventType, trialNum, trialPartNum, numTrialParts, source, null);
	}
	
	public ConditionEvent(int eventType, int trialNum, int trialPartNum, int numTrialParts,
			Object source, Object eventData) {
		super(source);
		this.eventType = eventType;
		this.trialNum = trialNum;
		this.trialPartNum = trialPartNum;
		this.numTrialParts = numTrialParts;
		this.eventData = eventData;
	}

	public int getEventType() {
		return eventType;
	}

	public int getTrialNum() {
		return trialNum;
	}

	public int getTrialPartNum() {
		return trialPartNum;
	}

	public Object getEventData() {
		return eventData;
	}
}