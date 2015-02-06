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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.mitre.icarus.cps.app.experiment.ui_study.exam.UIStudyExam;
import org.mitre.icarus.cps.app.experiment.ui_study.exam.UIStudyExamLoader;
import org.mitre.icarus.cps.app.experiment.ui_study.exam.UIStudyPhase;
import org.mitre.icarus.cps.experiment_core.subject_data.SubjectData;

public class SubjectDataRecorder implements ISubjectDataRecorder {

	@Override
	public boolean isEnabled() {
		return true;
	}
	
	@Override
	public void recordTaskData(SubjectData subjectData, UIStudyExam exam, UIStudyPhase<?> task)
		throws IOException, JAXBException {
		
		File responseDataFolder = new File("data/response_data");
		if(!responseDataFolder.exists()) {
			//Create the response data folder if it doesn't exist
			responseDataFolder.mkdir();
		}
		String fileName = "data/response_data/S" + subjectData.getSubjectId() + "_" +
		exam.getName() + "_" + task.getName() + ".xml";
	
		String xml = UIStudyExamLoader.marshalTask(task);
		BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
		out.write(xml);
		out.close();	
	}
}