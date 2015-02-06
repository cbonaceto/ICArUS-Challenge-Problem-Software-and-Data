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
package org.mitre.icarus.cps.app.experiment.data_recorder;

import java.io.File;

import org.mitre.icarus.cps.app.experiment.IcarusSubjectData;
import org.mitre.icarus.cps.exam.base.IcarusExam;
import org.mitre.icarus.cps.exam.base.IcarusExamPhase;
import org.mitre.icarus.cps.exam.base.loader.IcarusExamLoader;
import org.mitre.icarus.cps.exam.base.training.IcarusTutorialPhase;

/**
 * Subject data recorder implementation that does nothing.
 * 
 * @author CBONACETO
 *
 */
public class DoNothingSubjectDataRecorder<E extends IcarusExam<?>, P extends IcarusExamPhase, T extends IcarusTutorialPhase<?>> 
	implements ISubjectDataRecorder<E, P, T> {

	@Override
	public boolean isEnabled() {
		return false;
	}	

	@Override
	public File recordExamPhaseData(IcarusSubjectData subjectData, E exam,
			P examPhase, IcarusExamLoader<E, P, T> examLoader) throws Exception {	
		return null;
	}

	@Override
	public File recordTutorialPhaseData(IcarusSubjectData subjectData, E exam,
			T tutorialPhase, IcarusExamLoader<E, P, T> examLoader) throws Exception {
		return null;
	}
}