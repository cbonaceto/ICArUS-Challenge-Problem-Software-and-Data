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
package org.mitre.icarus.cps.app.window.phase_1;

import java.io.File;
import java.util.Arrays;
import java.util.EnumSet;

import org.mitre.icarus.cps.app.experiment.phase_1.IcarusExamController_Phase1;
import org.mitre.icarus.cps.app.experiment.phase_1.player.PlayerController;
import org.mitre.icarus.cps.app.window.configuration.FileDescriptor;
import org.mitre.icarus.cps.app.window.configuration.FileLocation;
import org.mitre.icarus.cps.app.window.configuration.PhaseConfiguration;
import org.mitre.icarus.cps.app.window.configuration.ApplicationConfiguration.ExamControlOption;
import org.mitre.icarus.cps.app.window.configuration.ApplicationConfiguration.ExamOpenOption;
import org.mitre.icarus.cps.app.window.configuration.ApplicationConfiguration.FeatureVectorOpenOption;
import org.mitre.icarus.cps.exam.phase_1.IcarusExam_Phase1;
import org.mitre.icarus.cps.exam.phase_1.playback.ExamPlaybackDataSource_Phase1;
import org.mitre.icarus.cps.exam.phase_1.testing.TaskTestPhase;

/**
 * 
 * @author CBONACETO
 *
 */
public class Phase_1_Configuration extends 
	PhaseConfiguration<IcarusExam_Phase1, TaskTestPhase<?>, ExamPlaybackDataSource_Phase1, IcarusExamController_Phase1, PlayerController> {
	
	/** File type constants for exam and feature vector files */
	protected static final int EXAM_FILE_TYPE = 0;
	protected static final int EXAM_RESPONSE_FILE_TYPE = 1;
	protected static final int AVG_HUMAN_FILE_TYPE = 2;
	protected static final int TASK_FEATURE_VECTOR_FILE_TYPE = 3;
	protected static final int ROADS_FEATURE_VECTOR_FILE_TYPE = 4;
	protected static final int REGIONS_FEATURE_VECTOR_FILE_TYPE = 5;	
	
	public Phase_1_Configuration() {
		initialize();				
	}
	
	public static Phase_1_Configuration createDefaultConfiguration() {
		return new Phase_1_Configuration();
	}	
	
	protected void initialize() {
		//Configure the phase name
		phaseName = "1 (AHA)";
		
		//Configure the exam and feature vector file types for Phase 1
		examFileType = new FileDescriptor(EXAM_FILE_TYPE, "Exam Files (*.xml)", "Exam", "xml");		
		examPlaybackFileTypes = Arrays.asList(
				examFileType,
				new FileDescriptor(EXAM_RESPONSE_FILE_TYPE, "Exam Response Files (*.xml)", "Exam Response", "xml"),
				new FileDescriptor(AVG_HUMAN_FILE_TYPE, "Average Human Reponse Files (*.xml)", "Average Human Response", "xml"));		
		featureVectorFileTypes =  Arrays.asList(
				new FileDescriptor(TASK_FEATURE_VECTOR_FILE_TYPE, "Task Feature Vector Files (*.csv, *.kml)", "Task (1-7) Feature Vector", "csv", "kml"),
				new FileDescriptor(ROADS_FEATURE_VECTOR_FILE_TYPE, "Roads Feature Vector Files (*.csv, *.kml)", "Roads Feature Vector", "csv", "kml"),
				new FileDescriptor(REGIONS_FEATURE_VECTOR_FILE_TYPE, "Regions Feature Vector Files (*.csv, *.kml)", "Regions Feature Vector", "csv", "kml"));
		
		//Configure the default exam file and folder locations
		defaultExamFile = new FileLocation("Final-Exam-1.xml", 
				"data/Phase_1_CPD/exams/Final-Exam-1/Final-Exam-1.xml", examFileType);		
		defaultExamFileLocation = new FileLocation(null, "data/Phase_1_CPD/exams", examFileType);
		defaultPlaybackFileLocations = Arrays.asList(
				new FileLocation(new File(defaultExamFile.getFileLocation()), examFileType),
				new FileLocation(new File("data/Phase_1_CPD/player_data/model_data"), examPlaybackFileTypes.get(1)),
				new FileLocation(new File("data/Phase_1_CPD/player_data/human_data/Avg-Human_Final-Exam-1.xml"), examPlaybackFileTypes.get(2)));
		
		//Configure the exam open options supported in Phase 1
		examOpenOptions = EnumSet.allOf(ExamOpenOption.class);
		
		//Configure the feature vector open options supported in Phase 1
		featureVectorOpenOptions = EnumSet.of(FeatureVectorOpenOption.Open_For_Display);		
		
		//Configure the exam control options supported for Phase 1 for exams open for data collection and demonstration
		examDataCollectionControlOptions = EnumSet.of(ExamControlOption.Advance_To_Phase, ExamControlOption.Change_Particpant, 
				ExamControlOption.Restart_Exam);
		examDemonstrationControlOptions = EnumSet.of(ExamControlOption.Advance_To_Phase, ExamControlOption.Restart_Exam);
		
		//Configure the exam control options enabled for Phase 1 for exams open for play-back
		examPlaybackControlOptions = EnumSet.of(ExamControlOption.Advance_To_Phase, ExamControlOption.Advance_To_Trial, ExamControlOption.Restart_Exam);
		
		//Configure the feature vector control options enabled for Phase 1
		
	}
}