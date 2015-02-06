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
package org.mitre.icarus.cps.exam.phase_05.training;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.base.IcarusTrial;

/**
 * Training trial base class.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="IcarusTrainingTrial", namespace="IcarusCPD_05")
@XmlSeeAlso({AnnotationTrainingTrial.class, AnnotationGridTrainingTrial.class, RuleTrainingTrial.class})
public abstract class IcarusTrainingTrial extends IcarusTrial {
	
	/** Training trial types */
	public static enum TrainingTrialType {Annotation, AnnotationGrid, Rule};
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.IcarusTrial#getTrialNum()
	 */
	@Override
	@XmlAttribute(name = "TrainingNum")
	public Integer getTrialNum() {		
		return super.getTrialNum();
	}	
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.IcarusTrial#setTrialNum(java.lang.Integer)
	 */
	@Override
	public void setTrialNum(Integer trialNum) {
		super.setTrialNum(trialNum);
	}

	/**
	 * Get the training type.
	 * 
	 * @return
	 */
	public abstract TrainingTrialType getTrainingTrialType();
}
