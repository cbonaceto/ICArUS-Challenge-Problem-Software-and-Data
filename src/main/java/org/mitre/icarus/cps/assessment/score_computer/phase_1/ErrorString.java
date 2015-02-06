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
package org.mitre.icarus.cps.assessment.score_computer.phase_1;

/**
 * @author CBONACETO
 *
 */
public class ErrorString {

	protected StringBuilder errors;
	
	protected int errorNum = 0;
	
	public void reset() {
		errors = null;
		errorNum = 0;
	}
	
	public void append(String error) {
		if(errors == null) {
			errors = new StringBuilder();
		} else {
			errors.append("; ");
		}
		errors.append(Integer.toString(errorNum+1) + ") ");
		errors.append(error);
		errorNum++;
	}
	
	public boolean isEmpty() {
		return errors == null || errors.toString().isEmpty();
	}
	
	public String getErrors() {
		if(errors != null) {
			return errors.toString();
		}
		return null;
	}

	@Override
	public String toString() {
		return getErrors();
	}
}