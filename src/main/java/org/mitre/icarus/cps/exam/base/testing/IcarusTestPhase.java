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

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.base.IcarusExamPhase;
import org.mitre.icarus.cps.exam.phase_05.testing.IcarusTestPhase_Phase05;
import org.mitre.icarus.cps.exam.phase_1.testing.TaskTestPhase;
import org.mitre.icarus.cps.exam.phase_2.testing.Mission;

/**
 * Abstract base class for test phases in an exam.
 * 
 * @author CBONACETO
 *
 */
@XmlRootElement(name="IcarusTestPhase", namespace="IcarusCPD_Base")
@XmlType(name="IcarusTestPhase", namespace="IcarusCPD_Base")
@XmlSeeAlso({IcarusTestPhase_Phase05.class, TaskTestPhase.class, Mission.class})
public abstract class IcarusTestPhase<T extends IcarusTestTrial> extends IcarusExamPhase {
	
	/**
	 * Get the test trials in the phase.
	 * 
	 * @return the test trials
	 */	
	public abstract ArrayList<T> getTestTrials();	
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment_core.condition.Condition#getNumTrials()
	 */
	@Override
	public int getNumTrials() {
		if(getTestTrials() != null) {
			return getTestTrials().size();
		}
		return 0;
	}	
}