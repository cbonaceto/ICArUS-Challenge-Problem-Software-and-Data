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
package org.mitre.icarus.cps.app.experiment.phase_2;

import org.mitre.icarus.cps.app.experiment.IcarusExperimentController;
import org.mitre.icarus.cps.app.experiment.TutorialController;
import org.mitre.icarus.cps.app.widgets.basic.tutorial.TutorialPanel;
import org.mitre.icarus.cps.exam.phase_2.training.IcarusExamTutorialPage_Phase2;
import org.mitre.icarus.cps.exam.phase_2.training.IcarusExamTutorial_Phase2;
import org.mitre.icarus.cps.experiment_core.Experiment;

/**
 * The exam tutorial controller for Phase 2.
 * 
 * @author CBONACETO
 *
 * @param <EC>
 * @param <E>
 *
 */
public class TutorialController_Phase2<
	EC extends IcarusExperimentController<E, ?, ?, ?>,
	E extends Experiment<?>>
	extends TutorialController<EC, E, IcarusExamTutorialPage_Phase2, IcarusExamTutorial_Phase2> {			
	
	/**
	 * No-arg constructor.
	 */
	public TutorialController_Phase2() {}
	
	/**
	 * Constructor that takes the tutorial and the tutorial panel.
	 * 
	 * @param tutorial the tutorial
	 * @param conditionPanel the tutorial panel
	 */
	public TutorialController_Phase2(IcarusExamTutorial_Phase2 tutorial, TutorialPanel conditionPanel) {
		super(tutorial, conditionPanel);	
	}		

	@Override
	protected void initializeTutorial() {
		//Does nothing
	}

	@Override
	protected void tutorialStarting() {
		//Does nothing
	}

	@Override
	protected void tutorialPageChanging(IcarusExamTutorialPage_Phase2 currentPage) {
		//Does nothing
	}

	@Override
	protected void tutorialPageChanged(IcarusExamTutorialPage_Phase2 newPage) {
		//Does nothing
	}
}