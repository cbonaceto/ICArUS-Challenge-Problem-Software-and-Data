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
package org.mitre.icarus.cps.test_harness;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;

import org.mitre.icarus.cps.assessment.score_computer.phase_1.BatchScoreComputer;
import org.mitre.icarus.cps.assessment.score_computer.phase_1.ScoreComputer;
import org.mitre.icarus.cps.exam.phase_1.IcarusExam_Phase1;
import org.mitre.icarus.cps.exam.phase_1.feedback.TrialFeedback_Phase1;
import org.mitre.icarus.cps.exam.phase_1.loader.IcarusExamLoader_Phase1;
import org.mitre.icarus.cps.exam.phase_1.testing.AttackLocationPresentationTrial;
import org.mitre.icarus.cps.exam.phase_1.testing.IcarusTestTrial_Phase1;
import org.mitre.icarus.cps.exam.phase_1.testing.TaskTestPhase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_1_2_3_ProbeTrialBase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_3_Phase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_4_Trial;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_5_Trial;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_6_Trial;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_7_Phase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_7_Trial;
import org.mitre.icarus.cps.exam.phase_1.testing.probability_rules.ProbabilityRules;
import org.mitre.icarus.cps.feature_vector.phase_1.GridSize;
import org.mitre.icarus.cps.feature_vector.phase_1.road_network.RoadDistanceCalculator;

/**
 * Used by the test harness to compute and return feedback (e.g., scores, credits earned) to models 
 * for the Phase 1 Challenge Problem.
 * 
 * @author CBONACETO
 *
 */
public class TestHarnessScoreComputer_Phase1 extends TestHarnessScoreComputerBase {

	/** Phase 1 score computer instance */
	protected static final ScoreComputer scoreComputer = new ScoreComputer();	
	
	/** Sample use */
	public static void main(String[] args) {
		try {
			//Run the exam scoring example
			/*TrialFeedback_Phase1 trialFeedback = scoreExamOrTask(
					"data/Phase_1_CPD/exams/Final-Exam-1-Half-1/exam_with_responses_7_1.xml",
					"data/Phase_1_CPD/exams/Final-Exam-1-Half-1/Final-Exam-1-Half-1-Gold.xml");*/
			TrialFeedback_Phase1 trialFeedback = scoreExamOrTask(
					"data/Phase_1_CPD/exams/PilotExam-TH-Test/exam_with_response.xml",
					"data/Phase_1_CPD/exams/PilotExam-TH-Test/PilotExam-TH-Test-Gold.xml");
			//We can turn the feedback object into an XML string like this:
			if(trialFeedback != null) {
				System.out.println("Trial Feedback: ");
				try {
					System.out.println(IcarusExamLoader_Phase1.marshalTrialFeedback(trialFeedback));
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}	
			//examExample("data/Phase_1_CPD/Final_Experiment_1/exam_with_responses_4_1.xml",
			//		"data/Phase_1_CPD/Final_Experiment_1/Final-Exam-1-Half-1-Gold.xml");
			//examExample("data/Phase_1_CPD/Pilot_Experiment_TH_Test/exam_with_response.xml",
			//		"data/Phase_1_CPD/Pilot_Experiment_TH_Test/PilotExam-TH-Test-Gold.xml");
			//examExample("data/Phase_1_CPD/Examples/PilotExam_withResponses_Task1.xml",
			//		"data/Phase_1_CPD/Pilot_Experiment/PilotExam.xml");

			//Run the task scoring example
			//taskExample("data/Phase_1_CPD/Examples/Sample_Responses_Task3.xml", 
			//		"data/Phase_1_CPD/Pilot_Experiment/PilotExam.xml");		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	
	/**
	 * Scores the last trial with a response in an exam or task file.
	 * 
	 * @param fileName The exam or task file
	 * @param goldStandardExamFileName The "gold" standard exam file
	 * @return The feedback for the last trial with a response
	 * @throws Exception
	 */
	public static TrialFeedback_Phase1 scoreExamOrTask(String fileName, String goldStandardExamFileName) throws Exception {		
		IcarusExam_Phase1 examWithResponses = null;
		TaskTestPhase<?> taskWithResponses = null;
		try {
			//First try loading the file as an exam file
			examWithResponses = IcarusExamLoader_Phase1.unmarshalExam(new File(fileName).toURI().toURL(), false);			
		} catch(Exception ex) {
			//Now try loading the file as a task file
			try {
				taskWithResponses = IcarusExamLoader_Phase1.unmarshalTask(new File(fileName).toURI().toURL(), false);
				//This is the equivalent code to load the task from a String (taskXml): IcarusExamLoader_Phase1.unmarshalTask(taskXml, false);
			} catch(Exception ex1) {
				throw new Exception("Unable to load task or exam with responses: " + ex.toString(), ex);
			}
		}		
		if(taskWithResponses == null && examWithResponses == null) {
			throw new Exception("The task or exam file with responses was not loaded");
		}
		
		if(examWithResponses != null) {
			//Find the last task in the exam with a response
			taskWithResponses = getLastTaskWithResponses(examWithResponses);
			if(taskWithResponses == null) {
				TrialFeedback_Phase1 feedbackForTrial = new TrialFeedback_Phase1();
				feedbackForTrial.setErrors("No responses found");
				setFeedbackForTrialFields(feedbackForTrial, examWithResponses);
				return feedbackForTrial;
				//throw new Exception("No responses found");
			}
		}
		
		//The next step is to load the "gold standard" version of the Task that contains definitions for all trials in the task.
		//We do this because we cannot necessarily trust that the model has correctly populated this data in its response.
		//We will fetch the task from the "gold standard" exam and feature vector files. To speed up this process, we may want to
		//keep exam data cached and loaded in memory in an instance of this class running in the test harness.

		//First, we load the exam
		URL examFile = null; 
		IcarusExam_Phase1 exam = null; 
		try {
			//examFile =  new File("data/PilotExam.xml").toURI().toURL();
			examFile =  new File(goldStandardExamFileName).toURI().toURL();
			exam = IcarusExamLoader_Phase1.unmarshalExam(examFile);
		} catch(Exception ex) {
			ex.printStackTrace();
			TrialFeedback_Phase1 feedbackForTrial = new TrialFeedback_Phase1();
			feedbackForTrial.setErrors("An error was encountered loading the gold standard exam. Please verify that the correct examId attribute was used.");
			setFeedbackForTrialFields(feedbackForTrial, taskWithResponses);
			return feedbackForTrial;
			//throw new Exception("Unable to load the standard exam: " + ex.toString(), ex);
		}
		if(exam == null) {
			TrialFeedback_Phase1 feedbackForTrial = new TrialFeedback_Phase1();
			feedbackForTrial.setErrors("An error was encountered loading the gold standard exam. Please verify that the correct examId attribute was used.");
			setFeedbackForTrialFields(feedbackForTrial, taskWithResponses);
			return feedbackForTrial;
			//throw new Exception("The standard exam was not loaded");
		}

		//Next, we add the model responses to tasks in the "gold standard" exam so the data used to calculate parameters
		//comes from the gold standard exam
		taskWithResponses = BatchScoreComputer.addSubjectResponsesToExamTask(exam, taskWithResponses);
		if(taskWithResponses.getId() == null) {
			taskWithResponses.setId(taskWithResponses.getName());
		}

		//Finally, we initialize the feature vector data for the task
		GridSize gridSize = exam.getGridSize();
		if(gridSize == null) {
			gridSize = new GridSize();
		}
		try {
			IcarusExamLoader_Phase1.initializeTaskFeatureVectorData(taskWithResponses, examFile, gridSize, 
					false, null);
		} catch(Exception ex) {
			ex.printStackTrace();
			TrialFeedback_Phase1 feedbackForTrial = new TrialFeedback_Phase1();
			feedbackForTrial.setErrors("An error was encountered initializing feature vector data: " + ex.getMessage());
			setFeedbackForTrialFields(feedbackForTrial, taskWithResponses);
			return feedbackForTrial;
			//throw ex;
		}

		//We can now compute feedback for the last trial containing a response 
		ProbabilityRules rules = exam.getProbabilityRules();
		if(rules == null) {
			rules = ProbabilityRules.createDefaultProbabilityRules();
		}
		TrialFeedback_Phase1 trialFeedback = null;
		/*if(examWithResponses != null) {
			trialFeedback = scoreTrial(exam, examWithResponses, null);
		} else {*/
		trialFeedback = scoreTrial(taskWithResponses, gridSize, rules, 0d);
		//}
		
		return trialFeedback;
	}	
	
	/**
	 * Computes the normative solution and score for the last trial in the last task in the given exam containing a response. Previous trials with
	 * responses are ignored and assumed to have been scored already.
	 * 
	 * @param examWithResponses The exam with responses up to and including the response for the current trial in the current task
	 * @param startingCredits The number of credits going into the current trial (Task 7 only). If null, starting credits is computed from previous trials.
	 * @return The feedback for the last trial with a response
	 */
	public static TrialFeedback_Phase1 scoreTrial(IcarusExam_Phase1 examWithResponses, Double startingCredits) {
		return scoreTrial(null, null, examWithResponses, startingCredits);
	}
	
	/**
	 * Computes the normative solution and score for the last trial in the last task in the given exam containing a response. Previous trials with
	 * responses are ignored and assumed to have been scored already.
	 * 
	 * @param exam The "gold standard" exam. This is ignored if null and only examWithResponses is used.
	 * @param examWithResponses The exam with responses up to and including the response for the current trial in the current task
	 * @param startingCredits The number of credits going into the current trial (Task 7 only). If null, starting credits is computed from previous trials.
	 * @return The feedback for the last trial with a response
	 */
	public static TrialFeedback_Phase1 scoreTrial(IcarusExam_Phase1 exam, IcarusExam_Phase1 examWithResponses, 
			Double startingCredits) {
		return scoreTrial(exam, null, examWithResponses, startingCredits);
	}
	
	/**
	 * Computes the normative solution and score for the last trial in the last task in the given exam containing a response. Previous trials with
	 * responses are ignored and assumed to have been scored already.
	 * 
	 * @param exam The "gold standard" exam. This is ignored if null and only examWithResponses is used.
	 * @param examFileFolder The URL location of the exam for loading feature vector data. This is ignored if null.
	 * @param examWithResponses The exam with responses up to and including the response for the current trial in the current task
	 * @param startingCredits The number of credits going into the current trial (Task 7 only). If null, starting credits is computed from previous trials.
	 * @return The feedback for the last trial with a response
	 */
	public static TrialFeedback_Phase1 scoreTrial(IcarusExam_Phase1 exam, URL examFileFolder, IcarusExam_Phase1 examWithResponses, 
			Double startingCredits) {
		TrialFeedback_Phase1 feedbackForTrial = null;
		
		//Find the last task in the exam with a response
		TaskTestPhase<?> lastTaskWithResponses = getLastTaskWithResponses(examWithResponses);				
		if(lastTaskWithResponses == null) {
			feedbackForTrial = new TrialFeedback_Phase1();
			feedbackForTrial.setErrors("No responses found");
			setFeedbackForTrialFields(feedbackForTrial, examWithResponses);
			return feedbackForTrial;
		}
		
		if(lastTaskWithResponses.getResponseGenerator() == null) {
			lastTaskWithResponses.setResponseGenerator(examWithResponses.getResponseGenerator());
		}
		lastTaskWithResponses.setExamId(examWithResponses.getId());		
		
		return scoreTrial(exam, examFileFolder, lastTaskWithResponses, startingCredits);
	}	
	
	/** Get the last task in the exam with a trial containing a response */
	protected static TaskTestPhase<?> getLastTaskWithResponses(IcarusExam_Phase1 examWithResponses) {
		ArrayList<TaskTestPhase<?>> tasks = examWithResponses.getTasks();
		TaskTestPhase<?> lastTaskWithResponses = null;
		if(tasks != null && !tasks.isEmpty()) {
			for(TaskTestPhase<?> task : tasks) {
				int lastTrialWithResponseIndex = 0;
				boolean responseFound = false;
				if(task.getTestTrials() != null && !task.getTestTrials().isEmpty()) {
					for(IcarusTestTrial_Phase1 trial : task.getTestTrials()) {
						if(!(trial instanceof AttackLocationPresentationTrial)) {
							if(trial.getTrialResponse() != null) {
								lastTaskWithResponses = task;
								lastTrialWithResponseIndex++;
								responseFound = true;
							} else {
								break; //exit (for IcarusTestTrial_Phase1 trial : task.getTestTrials())
							}
						} else {
							lastTrialWithResponseIndex++;
						}
					}

				}
				if(responseFound && lastTrialWithResponseIndex < task.getTestTrials().size()) {
					break;
				}
			}	
		}
		return lastTaskWithResponses;
	}
	
	/**
	 * Computes the normative solution and score for the last trial in the given task containing a response. Previous trials with
	 * responses are ignored and assumed to have been scored already.
	 * 
	 * @param exam The "gold standard" exam. This is ignored if null and only taskWithResponses is used.
	 * @param taskWithResponses The task with responses up to and including the response for the current trial
	 * @param startingCredits The number of credits going into the current trial (Task 7 only). If null, starting credits is computed from previous trials.
	 * @return The feedback for the last trial with a response
	 */
	public static TrialFeedback_Phase1 scoreTrial(IcarusExam_Phase1 exam, TaskTestPhase<?> taskWithResponses,
			Double startingCredits) {
		return scoreTrial(exam, null, taskWithResponses, startingCredits);
	}
	
	/**
	 * Computes the normative solution and score for the last trial in the given task containing a response. Previous trials with
	 * responses are ignored and assumed to have been scored already.
	 * 
	 * @param exam The "gold standard exam containing the task that the trial is in. This is ignored if null and only taskWithResponses is used.
	 * @param examFileFolder The URL location of the exam for loading feature vector data. This is ignored if null.
	 * @param taskWithResponses  The task with responses up to and including the response for the current trial
	 * @param startingCredits The number of credits going into the current trial (Task 7 only). If null, starting credits is computed from previous trials.
	 * @return The feedback for the last trial with a response
	 */
	public static TrialFeedback_Phase1 scoreTrial(IcarusExam_Phase1 exam, URL examFileFolder, TaskTestPhase<?> taskWithResponses,
			Double startingCredits) {		
		TrialFeedback_Phase1 feedbackForTrial = null;
		GridSize gridSize = null;
		ProbabilityRules rules = null;
		if(exam != null) {
			gridSize = exam.getGridSize();
			rules = exam.getProbabilityRules();
		}
		if(gridSize == null) {
			gridSize = new GridSize();
		}
		if(rules == null) {
			rules = ProbabilityRules.createDefaultProbabilityRules();
		}
		
		//Add subject/model responses to tasks in the "gold standard" exam so the data used to calculate parameters
		//comes from the gold standard exam
		if(exam != null) {
			try {
				taskWithResponses = BatchScoreComputer.addSubjectResponsesToExamTask(exam, taskWithResponses);
			} catch(Exception ex) {
				ex.printStackTrace();
				feedbackForTrial = new TrialFeedback_Phase1();
				feedbackForTrial.setErrors("An error was encountered scoring the trial: " + ex.getMessage());
				setFeedbackForTrialFields(feedbackForTrial, taskWithResponses);
				return feedbackForTrial;
			}
			taskWithResponses.setExamId(exam.getId());
		}
		
		//Initialize the feature vector data for the task if it hasn't yet been initialized	
		if(examFileFolder != null) {
			try {
				IcarusExamLoader_Phase1.initializeTaskFeatureVectorData(taskWithResponses, examFileFolder, gridSize, 
						false, null);
			} catch(Exception ex) {
				ex.printStackTrace();
				feedbackForTrial = new TrialFeedback_Phase1();
				feedbackForTrial.setErrors("An error was encountered initializing feature vector data: " + ex.getMessage());
				setFeedbackForTrialFields(feedbackForTrial, taskWithResponses);
				return feedbackForTrial;
			}
		}
		
		//Score the last trial with a response		
		return scoreTrial(taskWithResponses, gridSize, rules, startingCredits);		
	}	
	
	/**
	 * Computes the normative solution and score for the last trial in the given task containing a response. Previous trials with
	 * responses are ignored and assumed to have been scored already.
	 * 
	 * @param taskWithResponses
	 * @param gridSize
	 * @param rules
	 * @param startingCredits
	 * @return
	 */
	public static TrialFeedback_Phase1 scoreTrial(TaskTestPhase<?> taskWithResponses, GridSize gridSize,
			ProbabilityRules rules, Double startingCredits) {	
		//Get the last trial with a response
		/*try {
			System.out.println(IcarusExamLoader_Phase1.marshalTask(taskWithResponses));
		} catch (JAXBException e) {
			e.printStackTrace();
		}*/
		IcarusTestTrial_Phase1 lastTrialWithResponse = null;	
		ArrayList<? extends IcarusTestTrial_Phase1> trials = taskWithResponses != null ? taskWithResponses.getTestTrials() : null;		
		if(trials == null || trials.isEmpty()) {
			TrialFeedback_Phase1 feedbackForTrial = new TrialFeedback_Phase1();
			feedbackForTrial.setErrors("Trials missing from task");
			setFeedbackForTrialFields(feedbackForTrial, taskWithResponses);
			return feedbackForTrial;
		}
		for(IcarusTestTrial_Phase1 trial : trials) {
			if(trial instanceof AttackLocationPresentationTrial) {
				//Do nothing
			} else if (trial.getTrialResponse() != null) {
				lastTrialWithResponse = trial;				
			} else {
				break;
			}
		}
		
		if(lastTrialWithResponse == null) {
			TrialFeedback_Phase1 feedbackForTrial = new TrialFeedback_Phase1();
			feedbackForTrial.setErrors("No responses found");
			setFeedbackForTrialFields(feedbackForTrial, taskWithResponses);
			return feedbackForTrial;
		} else {
			//Score the trial
			return scoreTrial(taskWithResponses, lastTrialWithResponse, gridSize, rules, startingCredits);
		}
	}
		
	/**
	 * Computes the normative solution and score for the given lastTrialWithResponse trial. 
	 * 
	 * @param taskWithResponses
	 * @param lastTrialWithResponse
	 * @param gridSize
	 * @param rules
	 * @param startingCredits
	 * @return
	 */
	public static TrialFeedback_Phase1 scoreTrial(TaskTestPhase<?> taskWithResponses, IcarusTestTrial_Phase1 lastTrialWithResponse, 
			GridSize gridSize, ProbabilityRules rules, Double startingCredits) {		
		TrialFeedback_Phase1 feedbackForTrial = null;		
		
		ArrayList<AttackLocationPresentationTrial> groupAttacks = new ArrayList<AttackLocationPresentationTrial>(); //Contains the group attacks seen for Tasks 1-3
		if(lastTrialWithResponse == null) {
			//Get the last trial with a response
			ArrayList<? extends IcarusTestTrial_Phase1> trials = taskWithResponses != null ? taskWithResponses.getTestTrials() : null;			
			if(trials == null || trials.isEmpty()) {
				feedbackForTrial = new TrialFeedback_Phase1();
				feedbackForTrial.setErrors("Trials missing from task");
				setFeedbackForTrialFields(feedbackForTrial, taskWithResponses);
				return feedbackForTrial;
			}	
			for(IcarusTestTrial_Phase1 trial : trials) {
				if(trial instanceof AttackLocationPresentationTrial) {
					//Add an attack to the attack history
					groupAttacks.add((AttackLocationPresentationTrial)trial);
				} else if (trial.getTrialResponse() != null) {
					lastTrialWithResponse = trial;				
				} else {
					break;
				}
			}	
		} else {
			//Get the group attack history
			ArrayList<? extends IcarusTestTrial_Phase1> trials = taskWithResponses != null ? taskWithResponses.getTestTrials() : null;
			if(trials != null && !trials.isEmpty()) {
				for(IcarusTestTrial_Phase1 trial : trials) {
					if(trial instanceof AttackLocationPresentationTrial) {
						//Add an attack to the attack history
						groupAttacks.add((AttackLocationPresentationTrial)trial);
					} else if (trial == lastTrialWithResponse) {
						break;
					}
				}
			}
		}
		
		if(lastTrialWithResponse == null) {
			feedbackForTrial = new TrialFeedback_Phase1();
			feedbackForTrial.setErrors("No responses found");
			setFeedbackForTrialFields(feedbackForTrial, taskWithResponses);
			return feedbackForTrial;
		}
		
		//Compute the starting credits for Task 7 if startingCredits is null
		int correctPredictionCredits = 1;
		if(taskWithResponses != null && taskWithResponses instanceof Task_7_Phase) {
			Task_7_Phase task7 = (Task_7_Phase)taskWithResponses;
			if(task7.getCorrectPredictionCredits() != null) {
				correctPredictionCredits = task7.getCorrectPredictionCredits();
			}
			if(startingCredits == null) {
				startingCredits = task7.getInitialCredits().doubleValue();
				for(Task_7_Trial trial : task7.getTestTrials()) {
					if(trial.getTrialResponse() != null && trial != lastTrialWithResponse) {
						//Update credits
						try {
							TrialFeedback_Phase1 feedback = scoreComputer.computeSolutionAndScoreForTrial((Task_7_Trial)trial, 
									startingCredits, correctPredictionCredits);
							startingCredits = feedback.getNumCreditsRemaining();
						} catch(Exception ex) {
							feedbackForTrial = new TrialFeedback_Phase1();
							feedbackForTrial.setErrors("An error was encountered scoring the trial: " + ex.getMessage());
							setFeedbackForTrialFields(feedbackForTrial, taskWithResponses);
							return feedbackForTrial;
						}
					} else {
						break;
					}
				}
			}
		} else {
			startingCredits = 0D;
		}

		//Score the last trial with a response if possible	
		try {			
			if(lastTrialWithResponse instanceof Task_1_2_3_ProbeTrialBase) {
				RoadDistanceCalculator distanceCalculator = null;
				if(taskWithResponses != null && taskWithResponses instanceof Task_3_Phase) {
					Task_3_Phase task3 = (Task_3_Phase)taskWithResponses;
					distanceCalculator = new RoadDistanceCalculator(task3.getRoads(), gridSize);
				}
				feedbackForTrial = scoreComputer.computeSolutionAndScoreForTrial((Task_1_2_3_ProbeTrialBase)lastTrialWithResponse, groupAttacks, 
						rules, gridSize, distanceCalculator, true);			
			} else if(lastTrialWithResponse instanceof Task_4_Trial) {				
				feedbackForTrial = scoreComputer.computeSolutionAndScoreForTrial((Task_4_Trial)lastTrialWithResponse, rules, gridSize, true);
			} else if(lastTrialWithResponse instanceof Task_6_Trial) {				
				feedbackForTrial = scoreComputer.computeSolutionAndScoreForTrial((Task_6_Trial)lastTrialWithResponse, rules, gridSize, true);
			} else if(lastTrialWithResponse instanceof Task_5_Trial) {
				feedbackForTrial = scoreComputer.computeSolutionAndScoreForTrial((Task_5_Trial)lastTrialWithResponse, rules, gridSize, true);
			} else if(lastTrialWithResponse instanceof Task_7_Trial) {				
				feedbackForTrial = scoreComputer.computeSolutionAndScoreForTrial((Task_7_Trial)lastTrialWithResponse, 
						startingCredits, correctPredictionCredits);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			if(feedbackForTrial == null) {
				feedbackForTrial = new TrialFeedback_Phase1();
			}				
			feedbackForTrial.setErrors("An error was encountered scoring the trial: " + ex.getMessage());
		}		
		if(feedbackForTrial == null) {
			feedbackForTrial = new TrialFeedback_Phase1();
			feedbackForTrial.setResponseWellFormed(false);				
		} else {
			feedbackForTrial.setResponseWellFormed(feedbackForTrial.getErrors() == null || feedbackForTrial.getErrors().isEmpty());
		}		
		setFeedbackForTrialFields(feedbackForTrial, taskWithResponses);
		feedbackForTrial.setTrialNum(lastTrialWithResponse.getTrialNum());	
		feedbackForTrial.setTrial(lastTrialWithResponse);
		return feedbackForTrial;
	}
}