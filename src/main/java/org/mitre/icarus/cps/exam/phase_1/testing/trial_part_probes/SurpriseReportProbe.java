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
package org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * A probe where the subject/model is shown ground truth or a new INT layer and asked to 
 * report surprise on a Likert scale.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="SurpriseReportProbe", namespace="IcarusCPD_1", 
		propOrder={"minSurpriseValue", "maxSurpriseValue", "surpriseValueIncrement"})
public class SurpriseReportProbe extends TrialPartProbe {

	/** The minimum surprise value */
	protected Integer minSurpriseValue = 0;
	
	/** The maximum surprise value */
	protected Integer maxSurpriseValue = 6;
	
	/** The surprise value increment */
	protected Integer surpriseValueIncrement = 1;	
	
	/**
	 * No arg constructor.
	 */
	public SurpriseReportProbe() {}
	
	/**
	 * Constructor that takes the minSurpriseValue, maxSurpriseValue, and surpriseValueIncrement.
	 * 
	 * @param minSurpriseValue the minimum surprise value
	 * @param maxSurpriseValue the maximum surprise value
	 * @param surpriseValueIncrement the surprise value increment
	 */
	public SurpriseReportProbe(Integer minSurpriseValue, Integer maxSurpriseValue, 
			Integer surpriseValueIncrement) {
		this.minSurpriseValue = minSurpriseValue;
		this.maxSurpriseValue = maxSurpriseValue;
		this.surpriseValueIncrement = surpriseValueIncrement;
	}
	
	/**
	 * Get the minimum surprise value.
	 * 
	 * @return the minimum surprise value
	 */
	@XmlAttribute(name="minSurprise")
	public Integer getMinSurpriseValue() {
		return minSurpriseValue;
	}

	/**
	 * Set the minimum surprise value.
	 * 
	 * @param minSurpriseValue the minimum surprise value.
	 */
	public void setMinSurpriseValue(Integer minSurpriseValue) {
		this.minSurpriseValue = minSurpriseValue;
	}
	
	/**
	 * Get the maximum surprise value.
	 * 
	 * @return the maximum surprise value
	 */
	@XmlAttribute(name="maxSurprise")
	public Integer getMaxSurpriseValue() {
		return maxSurpriseValue;
	}

	/**	  
	 * Set the maximum surprise value.
	 * 
	 * @param maxSurpriseValue the maximum surprise value.
	 */
	public void setMaxSurpriseValue(Integer maxSurpriseValue) {
		this.maxSurpriseValue = maxSurpriseValue;
	}

	/**
	 * Get the surprise value increment.
	 * 
	 * @return the surprise value increment
	 */
	@XmlAttribute(name="increment")
	public Integer getSurpriseValueIncrement() {
		return surpriseValueIncrement;
	}

	/**
	 * Set the surprise value increment.
	 * 
	 * @param surpriseValueIncrement the surprise value increment.
	 */
	public void setSurpriseValueIncrement(Integer surpriseValueIncrement) {
		this.surpriseValueIncrement = surpriseValueIncrement;
	}	
}