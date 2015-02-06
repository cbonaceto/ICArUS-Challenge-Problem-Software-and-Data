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
package org.mitre.icarus.cps.app.window.phase_2;

import java.io.File;
import java.util.Arrays;
import java.util.EnumSet;

import org.mitre.icarus.cps.app.experiment.phase_1.player.PlayerController;
import org.mitre.icarus.cps.app.experiment.phase_2.IcarusExamController_Phase2;
import org.mitre.icarus.cps.app.window.IApplicationWindow.WindowAlignment;
import org.mitre.icarus.cps.app.window.configuration.FileDescriptor;
import org.mitre.icarus.cps.app.window.configuration.FileLocation;
import org.mitre.icarus.cps.app.window.configuration.PhaseConfiguration;
import org.mitre.icarus.cps.app.window.configuration.ApplicationConfiguration.ExamControlOption;
import org.mitre.icarus.cps.app.window.configuration.ApplicationConfiguration.ExamOpenOption;
import org.mitre.icarus.cps.app.window.configuration.ApplicationConfiguration.FeatureVectorOpenOption;
import org.mitre.icarus.cps.exam.phase_1.playback.ExamPlaybackDataSource_Phase1;
import org.mitre.icarus.cps.exam.phase_2.IcarusExam_Phase2;
import org.mitre.icarus.cps.exam.phase_2.testing.Mission;

/**
 * @author CBONACETO
 *
 */
public class Phase_2_Configuration extends 
	PhaseConfiguration<IcarusExam_Phase2, Mission<?>, ExamPlaybackDataSource_Phase1, IcarusExamController_Phase2, PlayerController> {
	
	/** File type constants for exam and feature vector files */
	protected static final int EXAM_FILE_TYPE = 0;
	protected static final int EXAM_RESPONSE_FILE_TYPE = 1;
	protected static final int AVG_HUMAN_FILE_TYPE = 2;
	protected static final int BLUE_LOCATIONS_FEATURE_VECTOR_FILE_TYPE = 3;
	protected static final int AREA_OF_INTEREST_VECTOR_FILE_TYPE = 4;	
		
	public Phase_2_Configuration() {
		initialize();
	}
	
	protected void initialize() {
		//Configure the phase name
		phaseName = "2 (TACTICS)";
		
		//Configure the exam and feature vector file types for Phase 1
		examFileType = new FileDescriptor(EXAM_FILE_TYPE, "Exam Files (*.xml)", "Exam", "xml");		
		examPlaybackFileTypes = Arrays.asList(
				examFileType,
				new FileDescriptor(EXAM_RESPONSE_FILE_TYPE, "Exam Response Files (*.xml)", "Exam Response", "xml"),
				new FileDescriptor(AVG_HUMAN_FILE_TYPE, "Average Human Reponse Files (*.xml)", "Average Human Response", "xml"));		
		featureVectorFileTypes =  Arrays.asList(
				new FileDescriptor(AREA_OF_INTEREST_VECTOR_FILE_TYPE, "Area of Interest Feature Vector Files (*.xml)", 
						"Area of Interest Feature Vector", "xml"),
				new FileDescriptor(BLUE_LOCATIONS_FEATURE_VECTOR_FILE_TYPE, 
						"Blue Locations Feature Vector Files (*.xml)", "Blue Locations Feature Vector", "xml"));		
		
		defaultExamFile = new FileLocation("Final-Exam-1.xml", 
				"data/Phase_2_CPD/exams/Final-Exam-1/Final-Exam-1.xml", examFileType);
		defaultExamFileLocation = new FileLocation(null, "data/Phase_2_CPD/exams", examFileType);
		defaultPlaybackFileLocations = Arrays.asList(
				new FileLocation(new File(defaultExamFileLocation.getFileLocation()), examFileType),
				new FileLocation(new File("data/Phase_2_CPD/player_data/model_data"), examPlaybackFileTypes.get(1)),
				new FileLocation(new File("data/Phase_2_CPD/player_data/human_data"), examPlaybackFileTypes.get(2)));
		
		//Configure the exam open options currently supported in Phase 2
		examOpenOptions = EnumSet.of(
                                ExamOpenOption.Open_For_Demonstration, 
                                ExamOpenOption.Open_For_Training,
				ExamOpenOption.Open_For_Data_Collection,                                
                                ExamOpenOption.Open_For_Validation);	
		
		//Configure the feature vector open options supported in Phase 2
		featureVectorOpenOptions = EnumSet.of(FeatureVectorOpenOption.Open_For_Display, 
				FeatureVectorOpenOption.Open_For_Validation);
		
		//Configure the exam control options supported for Phase 2 for exams open for data collection
		examDataCollectionControlOptions = EnumSet.of(ExamControlOption.Advance_To_Phase, 
				ExamControlOption.Change_Particpant, ExamControlOption.Restart_Exam);
		
		//Configure the exam control options supported for Phase 2 for exams open for demonstration
		examDemonstrationControlOptions = EnumSet.of(
				ExamControlOption.Advance_To_Phase, ExamControlOption.Advance_To_Trial,
				ExamControlOption.Restart_Exam);
		
		//Configure the exam control options enabled for Phase 2 for exams open for play-back
		//examPlaybackControlOptions = EnumSet.of(ExamControlOption.Advance_To_Phase, ExamControlOption.Advance_To_Trial, ExamControlOption.Restart_Exam);
		examPlaybackControlOptions = null;
		
		//Configure exam window alignment
		examDataCollectionWindowAlignment = WindowAlignment.LEFT;
	}	
}