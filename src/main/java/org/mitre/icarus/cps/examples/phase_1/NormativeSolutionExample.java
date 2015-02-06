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
package org.mitre.icarus.cps.examples.phase_1;

import java.net.URL;

import org.mitre.icarus.cps.app.util.CPSFileUtils;
import org.mitre.icarus.cps.assessment.score_computer.phase_1.ScoreComputer;
import org.mitre.icarus.cps.exam.phase_1.IcarusExam_Phase1;
import org.mitre.icarus.cps.exam.phase_1.loader.IcarusExamLoader_Phase1;
import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.Task_5_6_AttackLocationProbeResponseAfterINT;
import org.mitre.icarus.cps.exam.phase_1.testing.TaskTestPhase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_5_Phase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_5_Trial;
import org.mitre.icarus.cps.exam.phase_1.testing.probability_rules.ProbabilityRules;

/**
 * Contains examples of computing normative solutions for Tasks 1-6.
 * 
 * @author CBONACETO
 *
 */
public class NormativeSolutionExample {
	
	/**
	 * Example main. Computes the normative solution for each trial in each task in the exam.
	 * 
	 * @param args Specify the exam file name in args[0]. If null, uses default name "data/Phase_1_CPD/exams/PilotExam-1-15/PilotExam-1-15.xml".
	 */
	public static void main(String[] args) {		
		//Flag to set whether to use CSV or KML feature vector files. If false, CSV files are used.
		boolean useKml = false;
		
		//Load the pilot exam or an exam passed through args
		String fileName = "data/Phase_1_CPD/exams/PilotExam-1-15/PilotExam-1-15.xml";
		if(args != null && args.length > 0) {
			fileName = args[0];
		}
		IcarusExam_Phase1 exam = null;
		URL examFileURL = null;
		try {
			examFileURL = CPSFileUtils.createFile(fileName).toURI().toURL();
			exam = ParsingExample.loadExamAndFeatureVectors(examFileURL, useKml);
		} catch(Exception ex) {
			ex.printStackTrace();
		}				
		
		//Compute the normative solution for each trial in each task. For Task 6, we choose layers with the highest expected utility.
		if(exam != null && exam.getTasks() != null) {
			if(exam.getProbabilityRules() == null) {
				exam.setProbabilityRules(ProbabilityRules.createDefaultProbabilityRules());
			}
			ScoreComputer scoreComputer = new ScoreComputer();
			for(TaskTestPhase<?> task : exam.getTasks()) {
				//if(task instanceof Task_2_Phase) {
				//	ScoreComputer.DEBUG = true;
				scoreComputer.computeSolutionAndScoreForTask(task, exam.getProbabilityRules(), 
						exam.getGridSize(), false, null);				

				//As an example, get the normative solution after each INT layer for the first trial in Task 5
				if(task instanceof Task_5_Phase && task.getTestTrials() != null && !task.getTestTrials().isEmpty()) {
					Task_5_Phase task5 = (Task_5_Phase)task;
					Task_5_Trial trial = task5.getTestTrials().get(0);
					System.out.println();
					System.out.println("Task 5, Trial 1 Normative Solution: ");
					System.out.println("Initial Probabilities (from HUMINT): " + trial.getInitialHumintReport().getHumintProbabilities());					
					for(Task_5_6_AttackLocationProbeResponseAfterINT probeAfterInt :
						trial.getTrialResponse().getAttackLocationResponses_afterINTs()) {						
						System.out.println("INT Layer Shown: " + 
								ScoreComputer.formatLayerName(probeAfterInt.getIntLayerShown().getLayerType()) + 
								", Updated Probabilities: " +
								probeAfterInt.getAttackLocationResponse().getNormativeProbs_cumulativeBayesian());
					}
				}
				//}
			}	

			//Display the exam file with normative solutions
			System.out.println();
			System.out.println("Exam With Normative Solutions:");
			try {
				System.out.println(IcarusExamLoader_Phase1.marshalExam(exam));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
		
		System.out.println("Press ENTER to close this window");
		try {
			System.in.read();
		} catch(Exception err) {}
	}
}