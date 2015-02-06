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
import java.util.ArrayList;
import java.util.Calendar;

import org.mitre.icarus.cps.app.util.ProbabilityUtils;
import org.mitre.icarus.cps.assessment.score_computer.phase_1.ScoreComputer;
import org.mitre.icarus.cps.assessment.score_computer.phase_1.ScoreComputer.DispersionParameterType;
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
import org.mitre.icarus.cps.exam.phase_1.testing.probability_rules.ProbabilityRules;
import org.mitre.icarus.cps.examples.phase_1.ParsingExample;

/**
 * Computes parameters used in the calculation of normative solutions and creates the "gold" standard
 * version of an exam.
 * 
 * @author CBONACETO
 *
 */
public class ExamParametersComputer {
	
	public static void main(String[] args) {
		//String fileName = "data/Phase_1_CPD/Pilot_Experiment/PilotExam.xml";
		String fileName = "data/Phase_1_CPD/Final_Experiment/FinalExam.xml";		
		if(args != null && args.length > 0) {
			fileName = args[0];
		}
		computeExamParameters(fileName);
	}
	
	/**
	 * Computes the dispersion parameters for Tasks 1-3 and the initial probabilities for Tasks 5-6.
	 * 
	 * @param fileName the exam file name
	 */
	public static void computeExamParameters(String fileName) {		
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
		
		exam.setExamTimeStamp(Calendar.getInstance().getTime());
		
		//Compute the normative solution for each task. Dispersion parameters for Tasks 1-3 are computed dynamically.
		if(exam != null && exam.getTasks() != null) {
			if(exam.getProbabilityRules() == null) {
				exam.setProbabilityRules(ProbabilityRules.createDefaultProbabilityRules());
			}
			ScoreComputer scoreComputer = new ScoreComputer();
			scoreComputer.setDispersionType(DispersionParameterType.ComputeDynamically);
			
			for(TaskTestPhase<?> task : exam.getTasks()) {
				//Strip any dispersion parameters from Tasks 1-3, distances from Tasks 4-6, and HUMINT reports from Task 5-6 so they are re-computed
				if(task instanceof Task_1_Phase) {
					for(Task_1_TrialBlock trialBlock : ((Task_1_Phase)task).getTrialBlocks()) {
						trialBlock.getProbeTrial().setAttackDispersionParameters(null);
					}
				} else if(task instanceof Task_2_Phase) {
					for(Task_2_TrialBlock trialBlock : ((Task_2_Phase)task).getTrialBlocks()) {
						trialBlock.getProbeTrial().setAttackDispersionParameters(null);
					}					
				} else if(task instanceof Task_3_Phase) {
					for(Task_3_TrialBlock trialBlock : ((Task_3_Phase)task).getTrialBlocks()) {
						trialBlock.getProbeTrial().setCentersToAttackDistances(null);
						trialBlock.getProbeTrial().setAttackDispersionParameters(null);
					}
				} else if(task instanceof Task_4_Phase) {
					for(Task_4_Trial trial : ((Task_4_Phase)task).getTestTrials()) {
						trial.setCenterToAttackDistances(null);
					}
				} else if(task instanceof Task_5_Phase) {
					for(Task_5_Trial trial : ((Task_5_Phase)task).getTestTrials()) {					
						trial.setCentersToAttackDistances(null);
						trial.setInitialHumintReport(null);
					}
				} else if(task instanceof Task_6_Phase) {
					for(Task_6_Trial trial : ((Task_6_Phase)task).getTestTrials()) {
						trial.setCentersToAttackDistances(null);
						trial.setInitialHumintReport(null);
					}
				}
				
				//if(task instanceof Task_1_Phase) {		
				//System.out.println("Computing solution for " + task.getName());
				//ScoreComputer.DEBUG = true;
				scoreComputer.computeSolutionAndScoreForTask(task, exam.getProbabilityRules(), 
						exam.getGridSize(), false, null);		
				//Round the initial probabilities for the HUMINT report exactly as they will be presented in the GUI
				if(task instanceof Task_5_Phase) {
					for(Task_5_Trial trial : ((Task_5_Phase)task).getTestTrials()) {
						ArrayList<Integer> roundedProbs = 
								ProbabilityUtils.roundPercentProbabilities(trial.getInitialHumintReport().getHumintProbabilities());
						ArrayList<Double> roundedProbs_double = new ArrayList<Double>(roundedProbs.size());
						for(Integer prob : roundedProbs) {
							roundedProbs_double.add(prob.doubleValue());
						}
						trial.getInitialHumintReport().setHumintProbabilities(roundedProbs_double);						
					}
				} else if(task instanceof Task_6_Phase) {
					for(Task_6_Trial trial : ((Task_6_Phase)task).getTestTrials()) {
						ArrayList<Integer> roundedProbs = 
								ProbabilityUtils.roundPercentProbabilities(trial.getInitialHumintReport().getHumintProbabilities());
						ArrayList<Double> roundedProbs_double = new ArrayList<Double>(roundedProbs.size());
						for(Integer prob : roundedProbs) {
							roundedProbs_double.add(prob.doubleValue());
						}
						trial.getInitialHumintReport().setHumintProbabilities(roundedProbs_double);	
					}
				}
				//}
				
				//Strip the normative solutions
				if(task instanceof Task_1_Phase) {
					for(Task_1_TrialBlock trialBlock : ((Task_1_Phase)task).getTrialBlocks()) {
						trialBlock.getProbeTrial().setTrialResponse(null);
					}
				} else if(task instanceof Task_2_Phase) {
					for(Task_2_TrialBlock trialBlock : ((Task_2_Phase)task).getTrialBlocks()) {
						trialBlock.getProbeTrial().setTrialResponse(null);
					}					
				} else if(task instanceof Task_3_Phase) {
					for(Task_3_TrialBlock trialBlock : ((Task_3_Phase)task).getTrialBlocks()) {
						trialBlock.getProbeTrial().setTrialResponse(null);
					}
				} else if(task instanceof Task_4_Phase) {
					for(Task_4_Trial trial : ((Task_4_Phase)task).getTestTrials()) {						
						trial.setTrialResponse(null);						
					}
				} else if(task instanceof Task_5_Phase) {
					for(Task_5_Trial trial : ((Task_5_Phase)task).getTestTrials()) {
						trial.setTrialResponse(null);
					}
				} else if(task instanceof Task_6_Phase) {
					for(Task_6_Trial trial : ((Task_6_Phase)task).getTestTrials()) {
						trial.setTrialResponse(null);
					}
				}
			}	

			//Display the exam file with parameters
			System.out.println();
			System.out.println("Exam With Parameters:");
			try {
				System.out.println(IcarusExamLoader_Phase1.marshalExam(exam));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
	}
}