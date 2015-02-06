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
package org.mitre.icarus.cps.exam.phase_2.testing;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.phase_2.testing.bluebook.RedTacticType;

/**
 * Mission 1, 2, or 3.
 * 
 * @author CBONACETO
 *
 */
@XmlRootElement(name="Mission_1_2_3", namespace="IcarusCPD_2")
@XmlType(name="Mission_1_2_3", namespace="IcarusCPD_2")
public class Mission_1_2_3 extends Mission<Mission_1_2_3_Trial> {
	
	/** The tactic Red is playing with on the Mission (ground truth, not revealed to the participant in Mission 2) */
	protected RedTacticType redTactic;
	
	/** The trials */
	protected ArrayList<Mission_1_2_3_Trial> testTrials;
	
	/**
	 *  Get the tactic Red is playing with on the Mission (ground truth, not revealed to the participant in Mission 2).
	 * 
	 * @return the tactic Red is playing with
	 */
	@XmlElement(name="RedTactic")
	public RedTacticType getRedTactic() {
		return redTactic;
	}

	/**
	 * Set the tactic Red is playing with on the Mission (ground truth, not revealed to the participant in Mission 2).
	 * 
	 * @param redTactic the tactic Red is playing with
	 */
	public void setRedTactic(RedTacticType redTactic) {
		this.redTactic = redTactic;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.Mission#getTestTrials()
	 */
	@Override
	@XmlElement(name="Trial")
	public ArrayList<Mission_1_2_3_Trial> getTestTrials() {
		return testTrials;
	}

	/**
	 * Set the trials in the mission.
	 * 
	 * @param testTrials the trials in the mission
	 */
	public void setTestTrials(ArrayList<Mission_1_2_3_Trial> testTrials) {
		this.testTrials = testTrials;
	}	
}