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
package org.mitre.icarus.cps.exam.phase_1.generation;

import java.io.File;
import java.net.URL;
import java.util.Calendar;

import org.mitre.icarus.cps.exam.phase_1.IcarusExam_Phase1;
import org.mitre.icarus.cps.exam.phase_1.loader.IcarusExamLoader_Phase1;
import org.mitre.icarus.cps.exam.phase_1.testing.TaskTestPhase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_1_Phase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_1_TrialBlock;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_2_Phase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_2_TrialBlock;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_3_Phase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_3_TrialBlock;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_4_Phase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_4_Trial;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_5_Phase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_5_Trial;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_6_Phase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_6_Trial;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_7_Phase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_7_Trial;
import org.mitre.icarus.cps.examples.phase_1.ParsingExample;

/**
 * Removes ground truth, center/sigma dispersion parameters, calculated distances, and normative solutions
 * from an exam to create the "sanitized" exam file.
 * 
 * @author CBONACETO
 *
 */
public class ExamSanitizer {
	public static void main(String[] args) {
		//String fileName = "data/Phase_1_CPD/Pilot_Experiment/PilotExam.xml";
		//String fileName = "data/Phase_1_CPD/Pilot_Experiment_TH_Test/PilotExam-TH-Test-Gold.xml";
		//String fileName = "data/Phase_1_CPD/Final_Experiment_1/Final-Exam-1-Half-1-Gold.xml";
		String fileName = "data/Phase_1_CPD/Final_Experiment/FinalExam.xml";
		if(args != null && args.length > 0) {
			fileName = args[0];
		}
		sanitizeExam(fileName, false);
	}
	
	/**
	 * Removes ground truth (if removeGroundTruth true), center/sigma dispersion parameters, calculated distances, and normative solutions
	 * from an exam to create the "sanitized" exam file that will be given to performers.
	 * 
	 * @param fileName the exam file name
	 * @param removeGroundTruth whether to remove ground truth from the exam
	 */
	public static void sanitizeExam(String fileName, boolean removeGroundTruth) {		
		//Flag to set whether to use CSV or KML feature vector files. If false, CSV files are used.
		boolean useKml = false;
		
		//Load the exam
		IcarusExam_Phase1 exam = null;
		URL examFileURL = null;
		try {
			examFileURL = new File(fileName).toURI().toURL();
			exam = ParsingExample.loadExamAndFeatureVectors(examFileURL, useKml);
		} catch(Exception ex) {
			ex.printStackTrace();
		}		
		
                if (exam != null) {
                exam.setExamTimeStamp(Calendar.getInstance().getTime());
                exam.setApplicationVersion(null);
                exam.setProbabilityRules(null);
                exam.setResponseGenerator(null);
                exam.setNormalizationMode(null);
                exam.setTutorial(null);
                exam.setTutorialUrl(null);
                exam.setProbabilityInstructions(null);
            }
		
		if(exam != null && exam.getTasks() != null) {			
			for(TaskTestPhase<?> task : exam.getTasks()) {
				task.setApplicationVersion(null);
				task.setCountCondition(null);
				task.setConditionNum(null);
				task.setInstructionPages(null);
				task.setInstructionText(null);
				task.setShowInstructionPage(null);
				task.setShowScore(null);
				task.setPausePhase(null);	
				
				//Strip any dispersion parameters from Tasks 1-3, distances from Tasks 4-6, and responses and ground truth			
				if(task instanceof Task_1_Phase) {
					for(Task_1_TrialBlock trialBlock : ((Task_1_Phase)task).getTrialBlocks()) {
						if(removeGroundTruth) {
							trialBlock.getProbeTrial().setGroundTruth(null);
						}
						trialBlock.getProbeTrial().setAttackDispersionParameters(null);
						trialBlock.getProbeTrial().setTrialResponse(null);						
					}
				} else if(task instanceof Task_2_Phase) {
					for(Task_2_TrialBlock trialBlock : ((Task_2_Phase)task).getTrialBlocks()) {
						if(removeGroundTruth) {
							trialBlock.getProbeTrial().setGroundTruth(null);
						}
						trialBlock.getProbeTrial().setAttackDispersionParameters(null);
						trialBlock.getProbeTrial().setTrialResponse(null);
					}					
				} else if(task instanceof Task_3_Phase) {
					for(Task_3_TrialBlock trialBlock : ((Task_3_Phase)task).getTrialBlocks()) {
						if(removeGroundTruth) {
							trialBlock.getProbeTrial().setGroundTruth(null);
						}
						trialBlock.getProbeTrial().setCentersToAttackDistances(null);
						trialBlock.getProbeTrial().setAttackDispersionParameters(null);
						trialBlock.getProbeTrial().setTrialResponse(null);
					}
				} else if(task instanceof Task_4_Phase) {
					for(Task_4_Trial trial : ((Task_4_Phase)task).getTestTrials()) {
						if(removeGroundTruth) {
							trial.setGroundTruth(null);
						}
						trial.setCenterToAttackDistances(null);
						trial.setTrialResponse(null);
					}
				} else if(task instanceof Task_5_Phase) {
					for(Task_5_Trial trial : ((Task_5_Phase)task).getTestTrials()) {
						if(removeGroundTruth) {
							trial.setGroundTruth(null);
						}
						trial.setCentersToAttackDistances(null);
						trial.setTrialResponse(null);
					}
				} else if(task instanceof Task_6_Phase) {
					for(Task_6_Trial trial : ((Task_6_Phase)task).getTestTrials()) {
						if(removeGroundTruth) {
							trial.setGroundTruth(null);
						}
						trial.setCentersToAttackDistances(null);
						trial.setTrialResponse(null);
					}
				} else if(task instanceof Task_7_Phase) {
					for(Task_7_Trial trial : ((Task_7_Phase)task).getTestTrials()) {
						if(removeGroundTruth) {
							trial.setGroundTruth(null);
						}
						trial.setCenterToAttackDistances(null);
						trial.setTrialResponse(null);
					}
				}				
			}	

			//Display the exam file with parameters
			System.out.println();
			System.out.println("Sanitized Exam:");
			try {
				System.out.println(IcarusExamLoader_Phase1.marshalExam(exam));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
	}
}