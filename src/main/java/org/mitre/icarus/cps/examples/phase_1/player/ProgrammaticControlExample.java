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
package org.mitre.icarus.cps.examples.phase_1.player;

import java.net.URL;

import org.mitre.icarus.cps.app.util.CPSFileUtils;
import org.mitre.icarus.cps.app.window.IcarusLookAndFeel;
import org.mitre.icarus.cps.app.window.phase_1.IcarusTEPlayerMain;
import org.mitre.icarus.cps.app.window.phase_1.IcarusTEPlayerMain.ApplicationType;
import org.mitre.icarus.cps.assessment.data_model.phase_1.data_sets.AverageHumanDataSet_Phase1;
import org.mitre.icarus.cps.assessment.persistence.phase_1.xml.XMLCPADataPersister;
import org.mitre.icarus.cps.exam.base.response.ResponseGeneratorData;
import org.mitre.icarus.cps.exam.phase_1.IcarusExam_Phase1;
import org.mitre.icarus.cps.exam.phase_1.loader.IcarusExamLoader_Phase1;
import org.mitre.icarus.cps.exam.phase_1.playback.ExamPlaybackDataSource_Phase1;
import org.mitre.icarus.cps.exam.phase_1.testing.TaskTestPhase;

/**
 * Demonstrates programmatically controlled playback of an exam as it is completed by a model in real-time.
 * 
 * @author CBONACETO
 *
 */
@SuppressWarnings("deprecation")
public class ProgrammaticControlExample {
	
	/**
	 * Main method launches the player and executes the sample A-B model.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {		
		//Create the player application	
		IcarusLookAndFeel.initializeICArUSLookAndFeel();
		IcarusTEPlayerMain player = new IcarusTEPlayerMain(ApplicationType.Application);
		
		//Wait for the player to initialize
		while(!player.isInitialized()) {
			try {Thread.sleep(25);} catch(Exception ex) {}
		}
		
		//Turn off warnings for incomplete trials
		player.setShowIncompleteTrialWarnings(false);
		
		try {
			//Create an exam response data source for the final exam
			String examFileName = "data/Phase_1_CPD/exams/Final-Exam-1/Final-Exam-1.xml";
			String avgHumanFileName = "data/Phase_1_CPD/player_data/human_data/Avg-Human_Final-Exam-1.xml";
			if(args != null && args.length > 0) {
				examFileName = args[0];
				if(args.length > 1) {
					avgHumanFileName = args[1];
				}
			}
			ExamPlaybackDataSource_Phase1 examResponse = initializeExamResponse(
					CPSFileUtils.createFile(examFileName).toURI().toURL(),
					CPSFileUtils.createFile(avgHumanFileName).toURI().toURL());
			
			//Run the AB Model simulator and play through the exam
			player.playExamResponse(examResponse);		
			AbModelExample model = new AbModelExample(player.getExamPlayerController(), examResponse);
			model.performExam();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Loads an exam file and an average human results file and creates an ExamResponseDataSource instance.
	 * 
	 * @param examFileUrl
	 * @param avgHumanDataSetUrl
	 * @return
	 * @throws Exception
	 */
	protected static ExamPlaybackDataSource_Phase1 initializeExamResponse(URL examFileUrl, URL avgHumanDataSetUrl) throws Exception {
		//Load the exam and initialize feature vector data
		IcarusExam_Phase1 exam = IcarusExamLoader_Phase1.unmarshalExam(examFileUrl);
		if(exam != null) {		
			exam.setOriginalPath(examFileUrl);
			if(exam.getTasks() != null && !exam.getTasks().isEmpty()) {
				for(TaskTestPhase<?> task : exam.getTasks()) {
					IcarusExamLoader_Phase1.initializeTaskFeatureVectorData(task, 
						exam.getOriginalPath(), exam.getGridSize(), false, null);
				}
			}
		}
		
		//Load the average human data set
		AverageHumanDataSet_Phase1 avgHumanResponse = null;
		if(avgHumanDataSetUrl != null) {
			avgHumanResponse = XMLCPADataPersister.loadAverageHumanDataSet(avgHumanDataSetUrl);
		}		
		
		ExamPlaybackDataSource_Phase1 examResponse = new ExamPlaybackDataSource_Phase1();
		exam.setResponseGenerator(new ResponseGeneratorData("MITRE", "AB-Model", false));
		examResponse.initializeExamResponseData(exam, exam, avgHumanResponse, 
				avgHumanResponse != null ? avgHumanResponse.getMetricsInfo() : null);
		return examResponse;
	}
}