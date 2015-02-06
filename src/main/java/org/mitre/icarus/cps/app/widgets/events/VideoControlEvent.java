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


/**
 * @author Eric Kappotis
 *
 */
public class VideoControlEvent extends IcarusGUIEvent {
	
	private static final long serialVersionUID = 8097189615966385640L;
	
	public static enum EventType {ForwardBegin, ForwardEnd, RewindBegin, RewindEnd, Pause};
	
	private EventType eventType;

	public VideoControlEvent(Object source, EventType eventType) {
		super(source, eventType.ordinal());
		this.eventType = eventType;
	}

	/**
	 * @return the eventType
	 */
	@Override
	public int getEventType() {
		return eventType.ordinal();
	}	
}