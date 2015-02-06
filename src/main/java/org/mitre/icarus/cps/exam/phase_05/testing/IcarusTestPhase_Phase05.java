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
package org.mitre.icarus.cps.exam.phase_05.testing;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.base.testing.IcarusTestPhase;
import org.mitre.icarus.cps.exam.base.testing.IcarusTestTrial;


/**
 * Contains an ordered list of test trials defining a test phase in an exam.
 * 
 * @author CBONACETO
 *
 */
@XmlRootElement(name="TestPhase", namespace="IcarusCPD_05")
@XmlType(name="Test", namespace="IcarusCPD_05")
public class IcarusTestPhase_Phase05 extends IcarusTestPhase<IcarusTestTrial> {
	
	/** Ordered list of test trials */
	protected ArrayList<IcarusTestTrial> testTrials;	
	
	/**
	 * Get the test trials.
	 * 
	 * @return
	 */
	@XmlElement(name="Trial")
	@Override
	public ArrayList<IcarusTestTrial> getTestTrials() {
		return testTrials;
	}
	
	/**
	 * Set the test trials.
	 * 
	 * @param testTrials
	 */
	public void setTestTrials(ArrayList<IcarusTestTrial> testTrials) {
		this.testTrials = testTrials;
	}
}