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
package org.mitre.icarus.cps.exam.phase_1.training;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Contains probability rules pages for each INT type.
 * FOR HUMAN SUBJECT USE ONLY.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="ProbabilityRulesInstructions", namespace="IcarusCPD_1")
public class ProbabilityRulesInstructions {

	/** The rules pages for each INT type */
	protected ArrayList<ProbabilityRulesPage> probabilityRulesPages;

	/**
	 * Get the probability rules pages
	 * 
	 * @return the probability rules pages
	 */	
	@XmlElement(name="ProbabilityRulesPage")
	public ArrayList<ProbabilityRulesPage> getProbabilityRulesPages() {
		return probabilityRulesPages;
	}

	/**
	 * Set the probability rules pages.
	 * 
	 * @param probabilityRulesPages the probability rules pages
	 */
	public void setProbabilityRulesPages(ArrayList<ProbabilityRulesPage> probabilityRulesPages) {
		this.probabilityRulesPages = probabilityRulesPages;
	}	
}