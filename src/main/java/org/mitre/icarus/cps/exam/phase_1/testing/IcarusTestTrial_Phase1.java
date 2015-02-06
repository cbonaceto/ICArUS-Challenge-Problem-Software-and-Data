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
package org.mitre.icarus.cps.exam.phase_1.testing;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.base.testing.IcarusTestTrial;
import org.mitre.icarus.cps.exam.phase_1.response.IcarusTrialResponse_Phase1;

/**
 * Abstract base class for test trials for Phase 1 Tasks.  Phase 1 test trials
 * now contain inline subject/model response data.
 * 
 * @author CBONACETO
 *
 */
@XmlRootElement(name="IcarusTestTrial_CPD1", namespace="IcarusCPD_1")
@XmlType(name="IcarusTestTrial_CPD1", namespace="IcarusCPD_1")
@XmlSeeAlso({Task_1_2_3_ProbeTrialBase.class, Task_4_Trial.class, 
	Task_5_Trial.class, Task_6_Trial.class, Task_7_Trial.class})
public abstract class IcarusTestTrial_Phase1 extends IcarusTestTrial {
	
	/**
	 * Get the subject/model response to the trial.
	 * 
	 * @return the response
	 */
	public abstract IcarusTrialResponse_Phase1 getTrialResponse();
}