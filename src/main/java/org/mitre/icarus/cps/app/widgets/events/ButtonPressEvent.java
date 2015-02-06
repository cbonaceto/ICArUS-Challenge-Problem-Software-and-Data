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

public class ButtonPressEvent extends ClientGUIEvent {
	private static final long serialVersionUID = 1L;
	
	/** Button type event constants */	
	public static final int BUTTON_OK = 0;
	public static final int BUTTON_CANCEL = 1;
	public static final int BUTTON_CREATE_USER = 2;
	public static final int BUTTON_EXIT = 3;
	public static final int BUTTON_LOGIN = 4;
	public static final int BUTTON_LOGOUT = 5;
	public static final int NEXT_BUTTON_ID = 6;
	
	/** Id of button that was pressed */
	protected final int buttonId;
	
	public ButtonPressEvent(Object source, int buttonId) {
		super(source, buttonId);
		this.buttonId = buttonId;
	}

	public int getButtonId() {
		return buttonId;
	}	
}