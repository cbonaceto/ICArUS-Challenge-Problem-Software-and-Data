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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.experiment_core.condition.InstructionsPage;
import org.mitre.icarus.cps.feature_vector.phase_1.IntType;

/**
 * An instructions page with rules about probabilities for an INT layer.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="ProbabilityRulesPage", namespace="IcarusCPD_1")
public class ProbabilityRulesPage extends InstructionsPage {
	
	/** The INT type this rules page is for */
	protected IntType intType;

	@XmlAttribute(name="intType")
	public IntType getIntType() {
		return intType;
	}

	public void setIntType(IntType intType) {
		this.intType = intType;
	}
}