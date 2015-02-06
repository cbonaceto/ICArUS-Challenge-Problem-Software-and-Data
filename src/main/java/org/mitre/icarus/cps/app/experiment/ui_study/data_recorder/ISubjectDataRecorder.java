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
package org.mitre.icarus.cps.app.experiment.ui_study.data_recorder;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.mitre.icarus.cps.app.experiment.ui_study.exam.UIStudyExam;
import org.mitre.icarus.cps.app.experiment.ui_study.exam.UIStudyPhase;
import org.mitre.icarus.cps.experiment_core.subject_data.SubjectData;

public interface ISubjectDataRecorder {
	/**
	 * Get whether the data recorder is enabled.
	 * 
	 * @return whether the data recorder is enabled
	 */
	public boolean isEnabled();
	
	/**
	 * Record subject task data to the file system in the default subject data folder.
	 * 
	 * @param subjectData
	 * @param exam
	 * @param task
	 * @throws IOException
	 */
	public void recordTaskData(SubjectData subjectData, UIStudyExam exam, UIStudyPhase<?> task)
		throws IOException, JAXBException;
}
