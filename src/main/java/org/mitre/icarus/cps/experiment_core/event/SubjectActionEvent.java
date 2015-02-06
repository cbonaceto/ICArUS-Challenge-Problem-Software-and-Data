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

public class SubjectActionEvent extends EventObject {
	private static final long serialVersionUID = 1L;

	/** Subject action event constants */
	public static final int EXIT_BUTTON_PRESSED = 0;
	public static final int NEXT_BUTTON_PRESSED = 1;	
	public static final int BACK_BUTTON_PRESSED = 2;
	public static final int HELP_BUTTON_PRESSED = 3;
	public static final int CUSTOM_BUTTON_PRESSED = 4;
	public static final int NEXT_EVENT_INDEX = 5;
	
	/** The event type */
	public final int eventType;	
	
	/** User-defined data associated with the event */
	public final Object eventData;
	
	public SubjectActionEvent(int eventType, Object source) {
		this(eventType, source, null);
	}
	
	public SubjectActionEvent(int eventType, Object source, Object eventData) {
		super(source);
		this.eventType = eventType;
		this.eventData = eventData;
	}

	public int getEventType() {
		return eventType;
	}

	public Object getEventData() {
		return eventData;
	}
}
