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
 * Interface for implementations that record subject data.
 * 
 * @author CBONACETO
 *
 */
public interface ISubjectDataRecorder<E extends IcarusExam<?>, P extends IcarusExamPhase, 
	T extends IcarusTutorialPhase<?>> {
	
	/**
	 * Get whether the data recorder is enabled.
	 * 
	 * @return whether the data recorder is enabled
	 */
	public boolean isEnabled();
	
	/**
	 * Record subject exam phase response data to the file system in the default subject data folder.
	 * 
	 * @param subjectData the subject data contains the site ID and subject ID
	 * @param exam the exam containing the test phase to record
	 * @param examPhase the exam phase to record
	 * @return the file the data was recorded to
	 * @throws IOException
	 */	
	public File recordExamPhaseData(IcarusSubjectData subjectData, E exam, P examPhase, 
			IcarusExamLoader<E, P, T> examLoader) throws Exception;
	
	/**
	 * Record subject tutorial phase response data to the file system in the default subject data folder.
	 * 
	 * @param subjectData the subject data contains the site ID and subject ID
	 * @param exam the exam containing the test phase to record
	 * @param tutorialPhase the tutorial phase to record
	 * @return the file the data was recorded to
	 * @throws IOException
	 */
	public File recordTutorialPhaseData(IcarusSubjectData subjectData, E exam, T tutorialPhase, 
			IcarusExamLoader<E, P, T> examLoader) throws Exception;
}