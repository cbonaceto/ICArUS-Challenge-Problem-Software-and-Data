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
package org.mitre.icarus.cps.app.window.menu;

import org.mitre.icarus.cps.app.window.configuration.ApplicationConfiguration.ExamControlOption;

import org.mitre.icarus.cps.app.window.configuration.ApplicationConfiguration.ExamOpenOption;
import org.mitre.icarus.cps.app.window.configuration.ApplicationConfiguration.FeatureVectorControlOption;
import org.mitre.icarus.cps.app.window.configuration.ApplicationConfiguration.FeatureVectorOpenOption;
import org.mitre.icarus.cps.app.window.configuration.ApplicationConfiguration.FileOption;
import org.mitre.icarus.cps.app.window.configuration.FileDescriptor;

/**
 * Interface for implementations that .
 * 
 * @author CBONACETO
 *
 */
public interface ApplicationMenuListener {
	
	public void fileOptionSelected(FileOption fileOption);
	
	public void examOpenOptionSelected(ExamOpenOption openOption, String phaseId);
	
	public void featureVectorOpenOptionSelected(FeatureVectorOpenOption openOption, FileDescriptor featureVectorFileType, String phaseId);
	
	public void examControlOptionSelected(ExamControlOption controlOption);	
	
	public void featureVectorControlOptionSelected(FeatureVectorControlOption controlOption, FileDescriptor featureVectorFileType);
}