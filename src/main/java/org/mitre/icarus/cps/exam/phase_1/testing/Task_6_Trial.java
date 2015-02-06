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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * A Task 6 trial.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="Task_6_Trial", namespace="IcarusCPD_1")
public class Task_6_Trial extends Task_5_Trial {
	
	/** The total number of INT layers to show */
	protected Integer numLayersToShow;		
	
	/**
	 * Get the total number of layers to show.
	 * 
	 * @return the number of layers to show
	 */
	@XmlAttribute(name="numLayersToShow")
	public Integer getNumLayersToShow() {
		return numLayersToShow;
	}

	/**
	 * Set the total number of layers to show.
	 * 
	 * @param numLayersToShow the number of layers to show
	 */
	public void setNumLayersToShow(Integer numLayersToShow) {
		this.numLayersToShow = numLayersToShow;
	}	

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_1.testing.Task_5_Trial#getTestTrialType()
	 */
	@SuppressWarnings("deprecation")
	@Override
	public TestTrialType getTestTrialType() {
		return TestTrialType.Task_6_Trial;
	}
}