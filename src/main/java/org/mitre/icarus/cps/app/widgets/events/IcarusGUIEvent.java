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
package org.mitre.icarus.cps.app.widgets.events;

import java.util.EventObject;

/**
 * Base class for ICArUS GUI events.
 * 
 * @author CBONACETO
 *
 */
public class IcarusGUIEvent extends EventObject {
	private static final long serialVersionUID = 1L;
	
	/** Standard Event types */
	public static final int PROBABILITY_SUM_CHANGED = 0;
	
	public static final int SETTINGS_BOX_VALUE_CHANGED = 1;
	/*********/
	
	/** Next free event type ID */
	public static final int NEXT_EVENT_ID = 2;
	
	/** The event type */
	public final int eventType;
	
	/** The type of interaction component that was manipulated (if any) */
	public final InteractionComponentType interactionComponent;

	public IcarusGUIEvent(Object source, int eventType) {
		this(source, eventType, null);		
	}
	
	public IcarusGUIEvent(Object source, int eventType, 
			InteractionComponentType interactionComponent) {
		super(source);
		this.eventType = eventType;
		this.interactionComponent = interactionComponent;
	}

	public int getEventType() {
		return eventType;
	}

	public InteractionComponentType getInteractionComponent() {
		return interactionComponent;
	}
}