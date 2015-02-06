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
package org.mitre.icarus.cps.experiment_core.subject_data;

/**
 * Represents a single subject data element 
 * (e.g., elementName = roadAProb, elementValue = 55).
 * 
 * @author CBONACETO
 *
 */
public class SubjectDataElement {
	/** The data element name */
	public String elementName;
	
	/** The data element value */
	public Object elementValue;

	public SubjectDataElement() {}
	
	public SubjectDataElement(String elementName, Object elementValue) {
		this.elementName = elementName;
		this.elementValue = elementValue;
	}
	
	public void setNameAndValue(String elementName, Object elementValue) {
		this.elementName = elementName;
		this.elementValue = elementValue;
	}
	
	public String getElementName() {
		return elementName;
	}

	public void setElementName(String elementName) {
		this.elementName = elementName;
	}

	public Object getElementValue() {
		return elementValue;
	}

	public void setElementValue(Object elementValue) {
		this.elementValue = elementValue;
	}
}
