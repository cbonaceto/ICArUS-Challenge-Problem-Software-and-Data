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
package org.mitre.icarus.cps.app.window.controller;

import java.io.File;

import org.mitre.icarus.cps.app.experiment.data_recorder.DoNothingSubjectDataRecorder;
import org.mitre.icarus.cps.app.experiment.data_recorder.ISubjectDataRecorder;
import org.mitre.icarus.cps.app.experiment.data_recorder.LocalSubjectDataRecorder;
import org.mitre.icarus.cps.app.experiment.data_recorder.RemoteSubjectDataRecorder;
import org.mitre.icarus.cps.app.window.IApplicationWindow;
import org.mitre.icarus.cps.app.window.configuration.PhaseConfiguration;
import org.mitre.icarus.cps.app.window.configuration.PhaseConfigurationFactory;
import org.mitre.icarus.cps.app.window.phase_1.Phase_1_Configuration;
import org.mitre.icarus.cps.app.window.phase_1.Phase_1_Controller;
import org.mitre.icarus.cps.app.window.phase_2.Phase_2_Configuration;
import org.mitre.icarus.cps.app.window.phase_2.Phase_2_Controller;
import org.mitre.icarus.cps.exam.phase_1.IcarusExam_Phase1;
import org.mitre.icarus.cps.exam.phase_1.testing.TaskTestPhase;
import org.mitre.icarus.cps.exam.phase_1.training.TutorialPhase;
import org.mitre.icarus.cps.exam.phase_2.IcarusExam_Phase2;
import org.mitre.icarus.cps.exam.phase_2.testing.Mission;
import org.mitre.icarus.cps.exam.phase_2.training.IcarusExamTutorial_Phase2;
import org.mitre.icarus.cps.web.experiments.client.IDataSource;

/**
 * @author CBONACETO
 *
 */
public class PhaseControllerFactory {
	
	/**
	 * @param phaseId
	 * @param applicationWindow
	 * @param showReturnHomeButton
	 * @param dataCollectionEnabled
	 * @param remoteDataSourceEnabled
	 * @param remoteDataSource
	 * @param localDataFolder
	 * @return
	 * @throws IllegalArgumentException
	 */
	public static PhaseController<?, ?, ?, ?, ?, ?, ?, ?> createPhaseController(String phaseId,  IApplicationWindow applicationWindow,
			boolean showReturnHomeButton, boolean dataCollectionEnabled, boolean remoteDataSourceEnabled, 
			IDataSource remoteDataSource, File localDataFolder) throws IllegalArgumentException {
		if(phaseId != null) {
			return createPhaseController(PhaseConfigurationFactory.createPhaseConfiguration(phaseId), applicationWindow,
					showReturnHomeButton, dataCollectionEnabled, remoteDataSourceEnabled, remoteDataSource, localDataFolder);
		} else {
			throw new IllegalArgumentException("Phase must be specified, cannot create phase controller.");
		}
	}
	
	/**
	 * @param configuration
	 * @param applicationWindow
	 * @param showReturnHomeButton
	 * @param dataCollectionEnabled
	 * @param remoteDataSourceEnabled
	 * @param remoteDataSource
	 * @param localDataFolder
	 * @return
	 * @throws IllegalArgumentException
	 */
	public static PhaseController<?, ?, ?, ?, ?, ?, ?, ?> createPhaseController(PhaseConfiguration<?, ?, ?, ?, ?> configuration, 
			IApplicationWindow applicationWindow, boolean showReturnHomeButton, 
			boolean dataCollectionEnabled, boolean remoteDataSourceEnabled, 
			IDataSource remoteDataSource, File localDataFolder) throws IllegalArgumentException {
		if(configuration != null) {
			if(configuration instanceof Phase_1_Configuration) {
				ISubjectDataRecorder<IcarusExam_Phase1, TaskTestPhase<?>, TutorialPhase> dataRecorder = null;				
				if(dataCollectionEnabled) {
					if(remoteDataSourceEnabled && remoteDataSource != null) {
						dataRecorder = new RemoteSubjectDataRecorder<IcarusExam_Phase1, TaskTestPhase<?>, TutorialPhase>(
								remoteDataSource, localDataFolder);
						
					} else {
						dataRecorder = new LocalSubjectDataRecorder<IcarusExam_Phase1, TaskTestPhase<?>, TutorialPhase>(
								localDataFolder);
						
					}
				} else {
					dataRecorder = new DoNothingSubjectDataRecorder<IcarusExam_Phase1, TaskTestPhase<?>, TutorialPhase>();					 
				}
				return new Phase_1_Controller((Phase_1_Configuration)configuration, applicationWindow, 
						dataRecorder, showReturnHomeButton);
			} else if(configuration instanceof Phase_2_Configuration) {
				ISubjectDataRecorder<IcarusExam_Phase2, Mission<?>, IcarusExamTutorial_Phase2> dataRecorder = null;				
				if(dataCollectionEnabled) {
					if(remoteDataSourceEnabled && remoteDataSource != null) {
						dataRecorder = new RemoteSubjectDataRecorder<IcarusExam_Phase2, Mission<?>, IcarusExamTutorial_Phase2>(
								remoteDataSource, localDataFolder);						
					} else {
						dataRecorder = new LocalSubjectDataRecorder<IcarusExam_Phase2, Mission<?>, IcarusExamTutorial_Phase2>(
								localDataFolder);						
					}
				} else {
					dataRecorder = new DoNothingSubjectDataRecorder<IcarusExam_Phase2, Mission<?>, IcarusExamTutorial_Phase2>();					
				}
				return new Phase_2_Controller((Phase_2_Configuration)configuration, applicationWindow, 
						dataRecorder, showReturnHomeButton);
			} else {
				throw new IllegalArgumentException("Phase " + configuration.getPhaseId() + " is not supported by this version of the software.");
			}
		} else {
			throw new IllegalArgumentException("Phase configuration must be specified, cannot create phase controller.");
		}
	}
}