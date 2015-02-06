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
package org.mitre.icarus.cps.app.experiment.phase_05;

import org.mitre.icarus.cps.app.experiment.IcarusConditionController;
import org.mitre.icarus.cps.app.widgets.phase_05.experiment.ConditionPanel_Phase05;
import org.mitre.icarus.cps.exam.base.IcarusExamPhase;
import org.mitre.icarus.cps.exam.phase_05.IcarusExam_Phase05;

/**
 * Abstract base class for ICArUS exam phase controllers, including the
 * test phase controller and training phase controller.
 * 
 * @author CBONACETO
 *
 */
public abstract class IcarusConditionController_Phase05<P extends IcarusExamPhase>
	extends IcarusConditionController<IcarusExamController_Phase05, IcarusExam_Phase05, P, ConditionPanel_Phase05> {
	@Override
	protected void performCleanup() {
		//Does nothing		
	}	
}