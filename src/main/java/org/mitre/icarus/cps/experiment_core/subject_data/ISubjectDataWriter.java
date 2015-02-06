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
package org.mitre.icarus.cps.experiment_core.subject_data;

import java.io.File;
import java.util.List;

/**
 * Classes that implement this interface write subject trial data to the file system
 * 
 * @author cbonaceto
 *
 */
public interface ISubjectDataWriter {
	//public void recordSubjectTrialData(SubjectTrialData data, URL fileUrl, boolean append);	
	public void recordSubjectTrialData(File dataFile, String subjectID, 
			int trialNum, List<SubjectDataElement> data, boolean append);
}
