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
package org.mitre.icarus.cps.exam.base.testing;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.base.IcarusTrial;
import org.mitre.icarus.cps.exam.phase_05.testing.IcarusTestTrial_Phase05;
import org.mitre.icarus.cps.exam.phase_1.testing.IcarusTestTrial_Phase1;

/**
 * Abstract base class for test trials.
 * 
 * @author CBONACETO
 *
 */
@XmlRootElement(name="IcarusTestTrial", namespace="IcarusCPD_Base")
@XmlType(name="IcarusTestTrial", namespace="IcarusCPD_Base")
@XmlSeeAlso({IcarusTestTrial_Phase05.class, IcarusTestTrial_Phase1.class})
public abstract class IcarusTestTrial extends IcarusTrial {
	
	/** Test trial types */
	@Deprecated
	public static enum TestTrialType {ScenePresentation, AnalogicReasoning, Assessment,
		AttackPresentation, Task_1_Probe_Trial, Task_2_Probe_Trial,
		Task_3_Probe_Trial, Task_4_Trial, Task_5_Trial, Task_6_Trial,
		Task_7_Trial, Custom};
		
	/**
	 * Get the test trial type. This method should not longer be needed.
	 * 
	 * @return the test trial type
	 */
	@Deprecated
	public abstract TestTrialType getTestTrialType();
}