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
package org.mitre.icarus.cps.examples.phase_2;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.mitre.icarus.cps.app.util.CPSFileUtils;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.SubjectSigintProbabilities;
import org.mitre.icarus.cps.assessment.score_computer.phase_2.ScoreComputer_Phase2;
import org.mitre.icarus.cps.exam.phase_2.IcarusExam_Phase2;
import org.mitre.icarus.cps.exam.phase_2.loader.IcarusExamLoader_Phase2;
import org.mitre.icarus.cps.exam.phase_2.testing.IcarusTestTrial_Phase2;
import org.mitre.icarus.cps.exam.phase_2.testing.Mission;
import org.mitre.icarus.cps.exam.phase_2.testing.Mission_1_2_3;
import org.mitre.icarus.cps.exam.phase_2.testing.Mission_4_5_6;
import org.mitre.icarus.cps.exam.phase_2.testing.Mission_4_5_6_Trial;
import org.mitre.icarus.cps.exam.phase_2.testing.bluebook.BlueBook;
import org.mitre.icarus.cps.exam.phase_2.testing.bluebook.RedTactic;
import org.mitre.icarus.cps.exam.phase_2.testing.bluebook.RedTacticType;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.AttackProbability;
import org.mitre.icarus.cps.feature_vector.phase_2.int_datum.SigintReliability;

/**
 * Contains examples of computing normative solutions for Phase 2. Normative solutions are currently computed for
 * Mission 1-5.
 * 
 * @author CBONACETO
 *
 */
public class NormativeSolutionExample {
	
	/**
	 * Example main. Computes the normative solution (Red attack probabilities) for each trial in each mission in the exam.
	 * 
	 * @param args Specify the exam file name in args[0]. If null, uses default name "data/Phase_2_CPD/exams/Sample-Exam-2/Sample-Exam-2.xml".
	 */
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//Load the sample exam or an exam passed through args
		String fileName = "data/Phase_2_CPD/exams/Sample-Exam-2/Sample-Exam-2.xml";
		if(args != null && args.length > 0) {
			fileName = args[0];
		}
		IcarusExam_Phase2 exam = null;
		URL examFileURL = null;
		try {
			examFileURL = CPSFileUtils.createFile(fileName).toURI().toURL();
			exam = ParsingExample.loadExamAndFeatureVectors(examFileURL);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		boolean displayAttackProbabilities = true; // Whether to output the Red attack probabilities to the console
		boolean computeRedTacticProbs = true; //For Missions with multiple possible Red tactics, whether to compute the probability
		//that Red is playing with each tactic. If false, uses the actual Red tactic (ground truth) to compute Red attack probabilities.
		if(args != null && args.length > 1) {
			try {
				displayAttackProbabilities = Boolean.parseBoolean(args[1]);
			} catch(Exception ex) {}
		}
		
		//Compute the normative (cumulative and incremental Bayesian) Red attack probabilities for each trial in each mission
		if(exam != null && exam.getMissions() != null && !exam.getMissions().isEmpty()) {
			ModelExample.generateExamResponse(exam);
			computeNormativeSolutionForExam(exam, computeRedTacticProbs, displayAttackProbabilities, 75.d, 33.d);

			//Display the exam file with normative solutions
			System.out.println();
			System.out.println("Exam With Normative Solutions");
			System.out.println("(Note: Probabilites are shown in decimal format but should be reported in percent format):");
			try {
				System.out.println(IcarusExamLoader_Phase2.marshalExam(exam));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
		
		System.out.println("Press ENTER to close this window");
		try {
			System.in.read();
		} catch(Exception err) {}
	}
	
	/**
	 * Computes the normative Red attack probabilities for each trial in each mission in the given exam.
	 * 
	 * @param exam the exam, which contains the missions
	 * @param computeRedTacticProbs whether to compute the probability that Red is playing with each tactic
	 * in Missions 2, 4, and 5. If false, the actual (ground truth) tactic is used (if present)
	 * @param displayAttackProbabilities whether to display attack probabilities to the console as they are computed
	 * @param ptChatter participant value of P(Attack|Chatter) to use in Missions 2-3 for computing the incremental Bayesian solution
	 * @param ptSilent participant value of P(Attack|Silent) to use in Missions 2-3 for computing the incremental Bayesian solution
	 */
	public static void computeNormativeSolutionForExam(IcarusExam_Phase2 exam, boolean computeRedTacticProbs,
			boolean displayAttackProbabilities, Double ptChatter, Double ptSilent) {
		//Compute the normative Red attack probabilities for each trial in each mission
		if(exam != null && exam.getMissions() != null && !exam.getMissions().isEmpty()) {
			if(exam.getBlueBook() == null) {
				exam.setBlueBook(BlueBook.createDefaultBlueBook());
			}			
			if(exam.getSigintReliability() == null) {
				exam.setSigintReliability(SigintReliability.createDefaultSigintReliability());
			}
			ScoreComputer_Phase2 scoreComputer = new ScoreComputer_Phase2();
			for(Mission<?> mission : exam.getMissions()) {				
				List<RedTacticType> redTactics = null;
				List<RedTactic> possibleRedTactics = exam.getBlueBook().getRedTaticsForMission(
						mission.getMissionType());
				if(computeRedTacticProbs && possibleRedTactics != null && 
						possibleRedTactics.size() > 1) {
					//Get the tactics Red may be playing with in the mission
					redTactics = BlueBook.extractRedTacticTypes(possibleRedTactics);
				} else {
					//Get the actual (ground truth) tactic Red is playing with for each trial in the mission
					if(mission instanceof Mission_1_2_3) {
						Mission_1_2_3 mission123 = (Mission_1_2_3)mission;						
						redTactics = ((Mission_1_2_3) mission).getRedTactic() != null ?
								Collections.singletonList(mission123.getRedTactic()) : null;
					} else {
						boolean nullTacticsFound = false;
						Mission_4_5_6 mission456 = (Mission_4_5_6)mission;
						if(mission456.getTestTrials() != null && !mission456.getTestTrials().isEmpty()) {
							redTactics = new ArrayList<RedTacticType>(mission456.getTestTrials().size());
							for(Mission_4_5_6_Trial trial : mission456.getTestTrials()) {
								if(trial.getRedTactic() != null) {
									redTactics.add(trial.getRedTactic());
								} else {
									nullTacticsFound = true;
								}
							}
							if(nullTacticsFound) {
								redTactics = null;
							}
						}
					}
				}				
				if(redTactics != null) {
					//System.out.println("Computing normative Red attack probabilities for " + mission.getName());					
					scoreComputer.computeNormativeRedAttackProbabilitiesForMission(mission, redTactics, true,
							exam.getSigintReliability(), new SubjectSigintProbabilities(ptChatter, ptSilent), null);					
					//Display the Red attack probabilities for each trial in the mission
					if(displayAttackProbabilities && mission.getTestTrials() != null &&
							!mission.getTestTrials().isEmpty()) {
						System.out.println();
						System.out.println("Normative attack probabilities for " + mission.getName() + ":");
						int trialNum = 1;
						for(IcarusTestTrial_Phase2 trial : mission.getTestTrials()) {
							System.out.println("Trial " + trialNum + ":");
							if(trial.getAttackPropensityProbe_Pp() != null) {
								System.out.println(trial.getAttackPropensityProbe_Pp().getName() + ": " + 
										normativeProbabilitiesToString(trial.getAttackPropensityProbe_Pp().getProbabilities()));
							}
							if(trial.getAttackProbabilityProbe_Ppc() != null) {
								System.out.println(trial.getAttackProbabilityProbe_Ppc().getName() + ": " + 
										normativeProbabilitiesToString(trial.getAttackProbabilityProbe_Ppc().getProbabilities()));
							}
							if(trial.getAttackProbabilityProbe_Pt() != null) {
								System.out.println(trial.getAttackProbabilityProbe_Pt().getName() + ": " + 
										normativeProbabilitiesToString(trial.getAttackProbabilityProbe_Pt().getProbabilities()));
							}
							if(trial.getAttackProbabilityProbe_Ptpc() != null) {
								System.out.println(trial.getAttackProbabilityProbe_Ptpc().getName() + ": " + 
										normativeProbabilitiesToString(trial.getAttackProbabilityProbe_Ptpc().getProbabilities()));
							}
							trialNum++;
						}
					}
				}
			}
		}		
	}
	
	/**
	 * Create a string consisting of comma-separated list of the normative probabilities
	 * from the given list of attack probabilities.
	 * 
	 * @param probabilities a list of attack probabilities
	 * @return a string containing the normative probabilities
	 */
	public static String normativeProbabilitiesToString(
			List<AttackProbability> probabilities) {
		StringBuilder sb = new StringBuilder();
		if(probabilities != null && !probabilities.isEmpty()) {
			sb.append("Cumulative Bayesian: ");
			int i = 0;
			for(AttackProbability probability : probabilities) {
				sb.append(probability.getNormativeProbability());
				if(i < probabilities.size() - 1) {
					sb.append(", ");
				}
				i++;
			}
			sb.append("; Incremental Bayesian: ");
			i = 0;
			for(AttackProbability probability : probabilities) {
				sb.append(probability.getNormativeIncrementalProbability());
				if(i < probabilities.size() - 1) {
					sb.append(", ");
				}
				i++;
			}
		}
		return sb.toString();
	}
}