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
package org.mitre.icarus.cps.app.window.phase_05;

import java.util.EnumMap;

/**
 * Contains set of enabled options in the T & E GUI.
 * 
 * @author CBONACETO
 *
 */
public class GUIOptions {
	public static enum OptionType {
		Open_Pilot_Study,
		Open_Feature_Vector,
		Open_Exam,
		Validate_Exam,
		Validate_Response,
		Skip_To_Phase,
		Change_Participant
	}
	
	protected EnumMap<OptionType, Object> options = new EnumMap<OptionType, Object>(OptionType.class);
	
	public GUIOptions() {}
	
	public void enableAllOptions() {
		for(OptionType option : OptionType.values()) {
			options.put(option, option);
		}
	}
	
	public void disableAllOptions() {
		options.clear();
	}
	
	public boolean isOptionEnabled(OptionType option) {
		return options.containsKey(option);
	}
	
	public void setOptionEnabled(OptionType option) {
		options.put(option, option);
	}	
}
