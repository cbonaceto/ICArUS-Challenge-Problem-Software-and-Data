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
import java.util.Iterator;
import java.util.List;

import org.mitre.icarus.cps.assessment.score_computer.phase_2.ScoreComputer_Phase2;
import org.mitre.icarus.cps.assessment.score_computer.phase_2.ScoreComputer_Phase2.RedBluePayoffAtEachLocation;
import org.mitre.icarus.cps.exam.phase_2.IcarusExam_Phase2;
import org.mitre.icarus.cps.exam.phase_2.feedback.TrialFeedback_Phase2;
import org.mitre.icarus.cps.exam.phase_2.loader.IcarusExamLoader_Phase2;
import org.mitre.icarus.cps.exam.phase_2.testing.IcarusTestTrial_Phase2;
import org.mitre.icarus.cps.exam.phase_2.testing.Mission;
import org.mitre.icarus.cps.exam.phase_2.testing.Mission_4_5_6;
import org.mitre.icarus.cps.exam.phase_2.testing.Mission_4_5_6_Trial;
import org.mitre.icarus.cps.exam.phase_2.testing.bluebook.PayoffMatrix;

/**
 * Used by the test harness to compute and return feedback (e.g., scores) to models
 * for the Phase 2 Challenge Problem.
 * 
 * @author CBONACETO
 *
 */
public class TestHarnessScoreComputer_Phase2 extends TestHarnessScoreComputerBase {

	/** Phase 2 score computer instance */
	protected static final ScoreComputer_Phase2 scoreComputer = new ScoreComputer_Phase2();	
	
	/** Sample use */
	public static void main(String[] args) {
		try {
			//Run the exam scoring example		
			TrialFeedback_Phase2 trialFeedback = scoreExamOrMission(
					"data/Phase_2_CPD/exams/Sample_Exam_1/Example_Response_Sample-Exam-1-Short.xml",
					"data/Phase_2_CPD/exams/Sample_Exam_1/Sample-Exam-1-Short.xml");
			//We can turn the feedback object into an XML string like this:
			if(trialFeedback != null) {
				System.out.println("Trial Feedback: ");
				try {
					System.out.println(IcarusExamLoader_Phase2.marshalTrialFeedback(trialFeedback));
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	
	/**
	 * Scores the last trial with a response in an exam or mission file.
	 * 
	 * @param fileName The exam or mission file
	 * @param goldStandardExamFileName The "gold" standard exam file
	 * @return The feedback for the last trial with a response
	 * @throws Exception
	 */
	public static TrialFeedback_Phase2 scoreExamOrMission(String fileName, String goldStandardExamFileName) throws Exception {		
		IcarusExam_Phase2 examWithResponses = null;
		Mission<?> missionWithResponses = null;
		try {
			//First try loading the file as an exam file
			examWithResponses = IcarusExamLoader_Phase2.unmarshalExam(new File(fileName).toURI().toURL(), false);			
		} catch(Exception ex) {
			//Now try loading the file as a mission file
			try {
				missionWithResponses = IcarusExamLoader_Phase2.getInstance().unmarshalExamPhase(new File(fileName).toURI().toURL(), false);
			} catch(Exception ex1) {
				throw new Exception("Unable to load mission or exam with responses: " + ex.toString(), ex);
			}
		}		
		if(missionWithResponses == null && examWithResponses == null) {
			throw new Exception("The mission or exam file with responses was not loaded");
		}
		
		if(examWithResponses != null) {
			//Find the last mission in the exam with a response
			missionWithResponses = getLastMissionWithResponses(examWithResponses);		
			if(missionWithResponses == null) {				
				TrialFeedback_Phase2 feedbackForTrial = new TrialFeedback_Phase2();
				feedbackForTrial.setErrors("No responses found");
				setFeedbackForTrialFields(feedbackForTrial, examWithResponses);
				return feedbackForTrial;
			}
		}
		
		//The next step is to load the "gold standard" version of the mission that contains definitions for all trials in the mission.
		//We do this because we cannot necessarily trust that the model has correctly populated this data in its response.
		//We will fetch the mission from the "gold standard" exam and feature vector files. To speed up this process, we may want to
		//keep exam data cached and loaded in memory in an instance of this class running in the test harness.

		//First, we load the exam
		URL examFile = null; 
		IcarusExam_Phase2 exam = null; 
		try {
			//examFile =  new File("data/PilotExam.xml").toURI().toURL();
			examFile =  new File(goldStandardExamFileName).toURI().toURL();
			exam = IcarusExamLoader_Phase2.unmarshalExam(examFile);
		} catch(Exception ex) {
			ex.printStackTrace();
			TrialFeedback_Phase2 feedbackForTrial = new TrialFeedback_Phase2();
			feedbackForTrial.setErrors("An error was encountered loading the gold standard exam. Please verify that the correct examId attribute was used.");
			setFeedbackForTrialFields(feedbackForTrial, missionWithResponses);
			return feedbackForTrial;
		}
		if(exam == null) {
			TrialFeedback_Phase2 feedbackForTrial = new TrialFeedback_Phase2();
			feedbackForTrial.setErrors("An error was encountered loading the gold standard exam. Please verify that the correct examId attribute was used.");
			setFeedbackForTrialFields(feedbackForTrial, missionWithResponses);
			return feedbackForTrial;
		}

		//Next, we add the participant/model responses to tasks in the "gold standard" exam so the data used to calculate parameters
		//comes from the gold standard exam
		missionWithResponses = ScoreComputer_Phase2.addParticipantResponsesToExamMission(exam, missionWithResponses);
		if(missionWithResponses.getId() == null) {
			missionWithResponses.setId(missionWithResponses.getName());
		}

		//Finally, we initialize the feature vector data for the mission
		try {
			IcarusExamLoader_Phase2.initializeMissionFeatureVectorData(missionWithResponses, examFile, false, true, null);			
		} catch(Exception ex) {
			ex.printStackTrace();
			TrialFeedback_Phase2 feedbackForTrial = new TrialFeedback_Phase2();
			feedbackForTrial.setErrors("An error was encountered initializing feature vector data: " + ex.getMessage());
			setFeedbackForTrialFields(feedbackForTrial, missionWithResponses);
			return feedbackForTrial;
		}

		//We can now compute feedback for the last trial containing a response
		TrialFeedback_Phase2 trialFeedback = null;
		if(examWithResponses != null) {
			trialFeedback = scoreTrial(exam, examWithResponses);
		} else {
			trialFeedback = scoreTrial(missionWithResponses, exam.getPayoffMatrix());
		}
		
		return trialFeedback;
	}	
	
	/**
	 * Example use that scores the last trial in the last mission in an exam with a response.
	 * 
	 * @param examFileName The name of the exam file with responses
	 * @param goldStandardExamFileName The name of the "gold standard" exam file
	 * @return The feedback for the last trial with a response
	 * @throws Exception
	 */
	public static TrialFeedback_Phase2 examExample(String examFileName, String goldStandardExamFileName) throws Exception {
		//Assume we just received a model's response to a trial in a mission. We'll either have a string containing the XML,
		//or we'll need to load the XML from a file. In this case, we'll load the XML from an example exam file.		
		IcarusExam_Phase2 examWithResponses = null;
		try {
			examWithResponses = IcarusExamLoader_Phase2.unmarshalExam(new File(examFileName).toURI().toURL(), false);
			//This is the equivalent code to load the mission from a String (taskXml): IcarusExamLoader_Phase2.unmarshalTask(taskXml, false);
		} catch(Exception ex) {
			throw new Exception("Unable to load exam with responses: " + ex.toString(), ex);
		}
		if(examWithResponses == null) {
			throw new Exception("The exam with responses was not loaded");
		}
		
		//Find the last mission in the exam with a response
		Mission<?> lastMissionWithResponses = getLastMissionWithResponses(examWithResponses);		
		if(lastMissionWithResponses == null) {
			throw new Exception("No responses found");
		}

		//The next step is to load the "gold standard" version of the mission that contains definitions for all trials in the mission.
		//We do this because we cannot necessarily trust that the model has correctly populated this data in its response.
		//We will fetch the mission from the "gold standard" exam and feature vector files. To speed up this process, we may want to
		//keep exam data cached and loaded in memory in an instance of this class running in the test harness.

		//First, we load the exam
		URL examFile = null; 
		IcarusExam_Phase2 exam = null; 
		try {			
			examFile =  new File(goldStandardExamFileName).toURI().toURL();
			exam = IcarusExamLoader_Phase2.unmarshalExam(examFile);
		} catch(Exception ex) {
			throw new Exception("Unable to load the gold standard exam: " + ex.toString(), ex);
		}
		if(exam == null) {
			System.err.println("The gold standard exam was not loaded");
			return null;
		}		

		//Next, we add the model responses to tasks in the "gold standard" exam so the data used to calculate parameters
		//comes from the gold standard exam
		lastMissionWithResponses = ScoreComputer_Phase2.addParticipantResponsesToExamMission(exam, lastMissionWithResponses);
		if(lastMissionWithResponses.getId() == null) {
			lastMissionWithResponses.setId(lastMissionWithResponses.getName());
		}

		//Finally, we initialize the feature vector data for the mission
		IcarusExamLoader_Phase2.initializeMissionFeatureVectorData(lastMissionWithResponses, examFile, false, true, null);		

		//We can now compute feedback for the last trial containing a response
		TrialFeedback_Phase2 trialFeedback = scoreTrial(exam, lastMissionWithResponses);
		return trialFeedback;
	}	
	
	/**
	 * Example use that scores the last trial in a mission with responses. 
	 * 
	 * @param taskFileName The name of the mission file with responses
	 * @param goldStandardExamFileName The name of the "gold standard" exam file
	 * @return The feedback for the last trial with a response
	 * @throws Exception
	 */
	public static TrialFeedback_Phase2 missionExample(String missionFileName, String goldStandardExamFileName) throws Exception {		
		//Assume we just received a model's response to a trial in a mission. We'll either have a string containing the XML,
		//or we'll need to load the XML from a file. In this case, we'll load the XML from an example mission file.		
		Mission<?> missionWithResponses = null;
		try {
			missionWithResponses = IcarusExamLoader_Phase2.getInstance().unmarshalExamPhase(
					new File(missionFileName).toURI().toURL(), false);
		} catch(Exception ex) {
			throw new Exception("Unable to load mission with responses: " + ex.toString(), ex);
		}		
		if(missionWithResponses == null) {
			throw new Exception("The mission with responses was not loaded");
		}

		//The next step is to load the "gold standard" version of the mission that contains definitions for all trials in the mission.
		//We do this because we cannot necessarily trust that the model has correctly populated this data in its response.
		//We will fetch the mission from the "gold standard" exam and feature vector files. To speed up this process, we may want to
		//keep exam data cached and loaded in memory in an instance of this class running in the test harness.

		//First, we load the exam
		URL examFile = null; 
		IcarusExam_Phase2 exam = null; 
		try {
			//examFile =  new File("data/PilotExam.xml").toURI().toURL();
			examFile =  new File(goldStandardExamFileName).toURI().toURL();
			exam = IcarusExamLoader_Phase2.unmarshalExam(examFile);
		} catch(Exception ex) {
			throw new Exception("Unable to load the standard exam: " + ex.toString(), ex);
		}
		if(exam == null) {
			throw new Exception("The standard exam was not loaded");
		}
		
		//Next, we add the model responses to tasks in the "gold standard" exam so the data used to calculate parameters
		//comes from the gold standard exam
		missionWithResponses = ScoreComputer_Phase2.addParticipantResponsesToExamMission(exam, missionWithResponses);
		if(missionWithResponses.getId() == null) {
			missionWithResponses.setId(missionWithResponses.getName());
		}
		
		//Finally, we initialize the feature vector data for the mission
		IcarusExamLoader_Phase2.initializeMissionFeatureVectorData(missionWithResponses, examFile, false, true, null);
		
		//We can now compute feedback for the last trial containing a response
		TrialFeedback_Phase2 trialFeedback = scoreTrial(missionWithResponses, exam.getPayoffMatrix()); 
		return trialFeedback;
	}	
	
	/**
	 * Computes the score for the last trial in the last mission in the given exam containing a response. Previous trials with
	 * responses are ignored and assumed to have been scored already.
	 * 
	 * @param examWithResponses The exam with responses up to and including the response for the current trial in the current mission
	 * @return The feedback for the last trial with a response
	 */
	public static TrialFeedback_Phase2 scoreTrial(IcarusExam_Phase2 examWithResponses) {
		return scoreTrial(null, null, examWithResponses);
	}
	
	/**
	 * Computes the score for the last trial in the last mission in the given exam containing a response. Previous trials with
	 * responses are ignored and assumed to have been scored already.
	 * 
	 * @param exam The "gold standard" exam. This is ignored if null and only examWithResponses is used.
	 * @param examWithResponses The exam with responses up to and including the response for the current trial in the current mission
	 * @return The feedback for the last trial with a response
	 */
	public static TrialFeedback_Phase2 scoreTrial(IcarusExam_Phase2 exam, IcarusExam_Phase2 examWithResponses) {
		return scoreTrial(exam, null, examWithResponses);
	}
	
	/**
	 * Computes the score for the last trial in the last mission in the given exam containing a response. Previous trials with
	 * responses are ignored and assumed to have been scored already.
	 * 
	 * @param exam The "gold standard" exam. This is ignored if null and only examWithResponses is used.
	 * @param examFileFolder The URL location of the exam for loading feature vector data. This is ignored if null.
	 * @param examWithResponses The exam with responses up to and including the response for the current trial in the current mission
	 * @return The feedback for the last trial with a response
	 */
	public static TrialFeedback_Phase2 scoreTrial(IcarusExam_Phase2 exam, URL examFileFolder, IcarusExam_Phase2 examWithResponses) {
		TrialFeedback_Phase2 feedbackForTrial = null;
		
		//Find the last mission in the exam with a response
		Mission<?> lastMissionWithResponses = getLastMissionWithResponses(examWithResponses);				
		if(lastMissionWithResponses == null) {
			feedbackForTrial = new TrialFeedback_Phase2();
			feedbackForTrial.setErrors("No responses found");
			setFeedbackForTrialFields(feedbackForTrial, examWithResponses);
			return feedbackForTrial;
		}
		
		if(lastMissionWithResponses.getResponseGenerator() == null) {
			lastMissionWithResponses.setResponseGenerator(examWithResponses.getResponseGenerator());
		}
		lastMissionWithResponses.setExamId(examWithResponses.getId());		
		
		return scoreTrial(exam, examFileFolder, lastMissionWithResponses);
	}	
	
	/** Get the last mission in the exam with a trial containing a response */
	protected static Mission<?> getLastMissionWithResponses(IcarusExam_Phase2 examWithResponses) {
		List<Mission<?>> missions = examWithResponses.getMissions();
		Mission<?> lastMissionWithResponses = null;
		if(missions != null && !missions.isEmpty()) {
			for(Mission<?> mission : missions) {
				int lastTrialWithResponseIndex = 0;
				boolean responseFound = false;
				if(mission.getTestTrials() != null && !mission.getTestTrials().isEmpty()) {
					for(IcarusTestTrial_Phase2 trial : mission.getTestTrials()) {						
						if(trial.isResponsePresent()) {
							lastMissionWithResponses = mission;
							lastTrialWithResponseIndex++;
							responseFound = true;
						} else {
							break;
						}						
					}

				}
				if(responseFound && lastTrialWithResponseIndex < mission.getTestTrials().size()) {
					break;
				}
			}	
		}
		return lastMissionWithResponses;
	}
	
	/**
	 * Computes the score for the last trial in the given mission containing a response. Previous trials with
	 * responses are ignored and assumed to have been scored already.
	 * 
	 * @param exam The "gold standard" exam. This is ignored if null and only missionWithResponses is used.
	 * @param missionWithResponses The mission with responses up to and including the response for the current trial	 
	 * @return The feedback for the last trial with a response
	 */
	public static TrialFeedback_Phase2 scoreTrial(IcarusExam_Phase2 exam, Mission<?> missionWithResponses) {
		return scoreTrial(exam, null, missionWithResponses);
	}
	
	/**
	 * Computes the score for the last trial in the given mission containing a response. Previous trials with
	 * responses are ignored and assumed to have been scored already.
	 * 
	 * @param exam The "gold standard exam containing the mission that the trial is in. This is ignored if null and only missionWithResponses is used.
	 * @param examFileFolder The URL location of the exam for loading feature vector data. This is ignored if null.
	 * @param missionWithResponses  The mission with responses up to and including the response for the current trial	 * 
	 * @return The feedback for the last trial with a response
	 */	
	public static TrialFeedback_Phase2 scoreTrial(IcarusExam_Phase2 exam, URL examFileFolder, Mission<?> missionWithResponses) {		
		//Add participant responses to tasks in the "gold standard" exam so the data used to calculate parameters
		//comes from the gold standard exam
		if(exam != null) {
			try {
				missionWithResponses = ScoreComputer_Phase2.addParticipantResponsesToExamMission(exam, missionWithResponses);
			} catch(Exception ex) {
				ex.printStackTrace();
				TrialFeedback_Phase2 feedbackForTrial = new TrialFeedback_Phase2();
				feedbackForTrial.setErrors("An error was encountered scoring the trial: " + ex.getMessage());
				setFeedbackForTrialFields(feedbackForTrial, missionWithResponses);
				return feedbackForTrial;
			}
			missionWithResponses.setExamId(exam.getId());
		}
		
		//Initialize the feature vector data for the mission if it hasn't yet been initialized	
		if(examFileFolder != null) {
			try {
				IcarusExamLoader_Phase2.initializeMissionFeatureVectorData(missionWithResponses, examFileFolder, false, true, null);
			} catch(Exception ex) {
				ex.printStackTrace();
				TrialFeedback_Phase2 feedbackForTrial = new TrialFeedback_Phase2();
				feedbackForTrial.setErrors("An error was encountered initializing feature vector data: " + ex.getMessage());
				setFeedbackForTrialFields(feedbackForTrial, missionWithResponses);
				return feedbackForTrial;
			}
		}
		
		//Score the last trial with a response		
		return scoreTrial(missionWithResponses, exam != null ? exam.getPayoffMatrix() : null);		
	}	
	
	/**
	 * Computes score for the last trial in the given Mission containing a response. Previous trials with
	 * responses are ignored and assumed to have been scored already.
	 *
	 * @param missionWithResponses
	 * @param payoffMatrix
	 * @return
	 */
	public static TrialFeedback_Phase2 scoreTrial(Mission<?> missionWithResponses, PayoffMatrix payoffMatrix) {	
		//Get the last trial with a response, and update the number of batch plots used for Missions 4-6
		IcarusTestTrial_Phase2 lastTrialWithResponse = null;		
		ArrayList<? extends IcarusTestTrial_Phase2> trials = missionWithResponses != null ? missionWithResponses.getTestTrials() : null;		
		if(trials == null || trials.isEmpty()) {
			TrialFeedback_Phase2 feedbackForTrial = new TrialFeedback_Phase2();
			feedbackForTrial.setErrors("Trials missing from mission");
			setFeedbackForTrialFields(feedbackForTrial, missionWithResponses);
			return feedbackForTrial;
		}
		//Find the last trial with a response
		//int batchPlotsUsed = 0;
		//boolean batchPlotsUsedOnLastTrialWithResponse = false;
		for(IcarusTestTrial_Phase2 trial : trials) {		
			if(trial.isResponsePresent()) {
				lastTrialWithResponse = trial;
				/*batchPlotsUsedOnLastTrialWithResponse = false;
				if(trial instanceof Mission_4_5_6_Trial) {
					Mission_4_5_6_Trial mission456Trial = (Mission_4_5_6_Trial)trial;
					if(mission456Trial.getRedTacticsProbe() != null && 
							mission456Trial.getRedTacticsProbe().getBatchPlotProbe() != null &&
							mission456Trial.getRedTacticsProbe().getBatchPlotProbe().isResponsePresent()) {
						batchPlotsUsed++;
						batchPlotsUsedOnLastTrialWithResponse = true;
					}
				}*/
			} else {
				break;
			}
		}
		
		if(lastTrialWithResponse == null) {			
			TrialFeedback_Phase2 feedbackForTrial = new TrialFeedback_Phase2();
			feedbackForTrial.setErrors("No responses found");
			setFeedbackForTrialFields(feedbackForTrial, missionWithResponses);
			return feedbackForTrial;
		} else {
			//Score the trial
			/*if(batchPlotsUsedOnLastTrialWithResponse) {
				batchPlotsUsed--;
			}*/
			return scoreTrial(missionWithResponses, lastTrialWithResponse, payoffMatrix);
		}
	}
		
	/**
	 * Computes the score for the given lastTrialWithResponse trial. 
	 *
	 * @param missionWithResponses
	 * @param lastTrialWithResponse
	 * @param payoffMatrix
	 * @return
	 */
	public static TrialFeedback_Phase2 scoreTrial(Mission<?> missionWithResponses,
			IcarusTestTrial_Phase2 lastTrialWithResponse, PayoffMatrix payoffMatrix) {		
		TrialFeedback_Phase2 feedbackForTrial = null;
		ArrayList<? extends IcarusTestTrial_Phase2> trials = missionWithResponses != null ? missionWithResponses.getTestTrials() : null;
		if(lastTrialWithResponse == null) {
			//Get the last trial with a response
			if(trials == null || trials.isEmpty()) {
				feedbackForTrial = new TrialFeedback_Phase2();
				feedbackForTrial.setErrors("Trials missing from mission");
				setFeedbackForTrialFields(feedbackForTrial, missionWithResponses);
				return feedbackForTrial;
			}			
			for(IcarusTestTrial_Phase2 trial : trials) {
				if(trial.isResponsePresent()) {
					lastTrialWithResponse = trial;				
				} else {
					break;
				}
			}	
		}
		
		if(lastTrialWithResponse == null) {
			feedbackForTrial = new TrialFeedback_Phase2();
			feedbackForTrial.setErrors("No responses found");
			setFeedbackForTrialFields(feedbackForTrial, missionWithResponses);
			return feedbackForTrial;
		}		

		//Score the last trial with a response if possible
		//Also compute the number of batch plots used		
		try {
			//First, compute the current Red and Blue scores for the Mission
			Integer numBatchPlotsUsed = 0;
			Double currentRedScore = 0D;
			Double currentBlueScore = 0D;
			if(trials != null && !trials.isEmpty()) {
				Iterator<? extends IcarusTestTrial_Phase2> trialIter = trials.iterator();
				boolean reachedLastTrialWithResponse = false;
				while(trialIter.hasNext() && !reachedLastTrialWithResponse) {
					IcarusTestTrial_Phase2 trial = trialIter.next();					
					if(trial == lastTrialWithResponse) {
						reachedLastTrialWithResponse = true;
					} else {
						if(trial instanceof Mission_4_5_6_Trial) {
							Mission_4_5_6_Trial mission456Trial = (Mission_4_5_6_Trial)trial;
							if(mission456Trial.getRedTacticsProbe() != null && 
									mission456Trial.getRedTacticsProbe().getBatchPlotProbe() != null &&
									mission456Trial.getRedTacticsProbe().getBatchPlotProbe().getNumPreviousTrialsSelected() != null
									&& mission456Trial.getRedTacticsProbe().getBatchPlotProbe().getNumPreviousTrialsSelected() > 0) {
								numBatchPlotsUsed++;
							}
						}
						RedBluePayoffAtEachLocation payoff = ScoreComputer_Phase2.computePayoffAtEachLocation(
								trial, currentRedScore, currentBlueScore, payoffMatrix);
						if(payoff != null) {
							currentRedScore = payoff.getRedScore() != null ? payoff.getRedScore() : currentRedScore;
							currentBlueScore = payoff.getBlueScore() != null ? payoff.getBlueScore() : currentBlueScore;
							//System.out.println("trial " + trialNum + ", red points gained: " + payoff.getRedPointsGained() + ", blue points gained: " + payoff.getBluePointsGained());
						}
						//trialNum++;
					}
				}	
			}
			//Score the trial
			feedbackForTrial = scoreComputer.computeScoreForTrial(lastTrialWithResponse, currentRedScore, 
					currentBlueScore, payoffMatrix, numBatchPlotsUsed, 
					missionWithResponses instanceof Mission_4_5_6 ? ((Mission_4_5_6)missionWithResponses).getMaxNumBatchPlots() : 0,
					true);			
		} catch(Exception ex) {
			ex.printStackTrace();
			if(feedbackForTrial == null) {
				feedbackForTrial = new TrialFeedback_Phase2();
			}				
			feedbackForTrial.setErrors("An error was encountered scoring the trial: " + ex.getMessage());
		}		
		if(feedbackForTrial == null) {
			feedbackForTrial = new TrialFeedback_Phase2();
			feedbackForTrial.setResponseWellFormed(false);				
		} else {
			feedbackForTrial.setResponseWellFormed(feedbackForTrial.getErrors() == null || feedbackForTrial.getErrors().isEmpty());
		}		
		setFeedbackForTrialFields(feedbackForTrial, missionWithResponses);
		feedbackForTrial.setTrialNum(lastTrialWithResponse.getTrialNum());	
		feedbackForTrial.setTrial(lastTrialWithResponse);
		return feedbackForTrial;
	}
}