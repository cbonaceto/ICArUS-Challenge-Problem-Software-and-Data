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
package org.mitre.icarus.cps.exam.phase_2.training;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.base.training.IcarusTutorialPhase;

/**
 * Tutorial for a Phase 2 exam.
 * 
 * @author CBONACETO
 *
 */
@XmlRootElement(name="TutorialPhase_Phase2", namespace="IcarusCPD_2")
@XmlType(name="TutorialPhase_Phase2", namespace="IcarusCPD_2")
public class IcarusExamTutorial_Phase2 extends IcarusTutorialPhase<IcarusExamTutorialPage_Phase2> {
	
	/**
	 * Get the tutorial pages.
	 * 
	 * @return the tutorial pages
	 */
	@Override
	@XmlElementWrapper(name="TutorialPages")
	@XmlElement(name="TutorialPage")
	public List<IcarusExamTutorialPage_Phase2> getTutorialPages() {
		return tutorialPages;
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.base.training.IcarusTutorialPhase#setTutorialPages(java.util.List)
	 */
	@Override
	public void setTutorialPages(List<IcarusExamTutorialPage_Phase2> tutorialPages) {
		this.tutorialPages = tutorialPages;
	}
}