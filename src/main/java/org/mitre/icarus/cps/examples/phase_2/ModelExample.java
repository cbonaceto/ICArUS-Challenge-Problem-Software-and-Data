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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.xml.bind.JAXBException;

import org.mitre.icarus.cps.app.util.CPSFileUtils;
import org.mitre.icarus.cps.app.util.ProbabilityUtils;
import org.mitre.icarus.cps.exam.phase_2.IcarusExam_Phase2;
import org.mitre.icarus.cps.exam.phase_2.loader.IcarusExamLoader_Phase2;
import org.mitre.icarus.cps.exam.phase_2.testing.IcarusTestTrial_Phase2;
import org.mitre.icarus.cps.exam.phase_2.testing.Mission;
import org.mitre.icarus.cps.exam.phase_2.testing.Mission_1_2_3_Trial;
import org.mitre.icarus.cps.exam.phase_2.testing.Mission_4_5_6;
import org.mitre.icarus.cps.exam.phase_2.testing.Mission_4_5_6_Trial;
import org.mitre.icarus.cps.exam.phase_2.testing.bluebook.RedTacticParameters;
import org.mitre.icarus.cps.exam.phase_2.testing.bluebook.RedTacticParameters.RedTacticConsiderationData;
import org.mitre.icarus.cps.exam.phase_2.testing.bluebook.RedTacticParameters.RedTacticQuadrant;
import org.mitre.icarus.cps.exam.phase_2.testing.bluebook.RedTacticType;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.AttackProbability;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.BatchPlotProbe.BatchPlotProbeButtonType;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.BlueAction;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.BlueAction.BlueActionType;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.NormalizationConstraintType;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.ItemAdjustment;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.red_tactics_probes.AbstractRedTacticsProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.red_tactics_probes.RedTacticProbability;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.red_tactics_probes.RedTacticsChange;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.red_tactics_probes.RedTacticsChangesProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.red_tactics_probes.RedTacticsProbabilityReportProbe;

/**
 * Contains examples of reading exam and feature vector files, generating random responses
 * to trials, and marshalling response data back to XML for the Phase 2 Challenge Problem.
 * Note that examples are shown for generating times and adjustment sequences (e.g., the order
 * in which probabilities were adjusted), but models are not required to report this data.
 * 
 * @author CBONACETO
 *
 */
public class ModelExample {
	
	/** Random number generator */
	private static final Random rand = new Random(1);

	/**
	 * Generates random responses to all trials in all missions in the given exam.
	 * 
	 * @param exam the exam
	 */
	public static void generateExamResponse(IcarusExam_Phase2 exam) {
		if(exam.getMissions() != null && !exam.getMissions().isEmpty()) {
			long currTime = System.currentTimeMillis();
			exam.setStartTime(new Date(currTime));
			for(Mission<?> mission : exam.getMissions()) {
				generateMissionResponse(mission, currTime);
				currTime = currTime + (mission.getEndTime().getTime() - currTime);
			}
			exam.setEndTime(new Date(currTime));
		}
	}
	
	/**
	 * Generate random responses to all trials in the given mission.
	 * 
	 * @param mission the mission
	 * @param currTime the current time (in milliseconds since 1970 format). Used to create time stamps, 
	 * but models are not required to report time. Shown for illustrative purposes as human data will contain times.
	 */
	public static void generateMissionResponse(Mission<?> mission, long currTime) {
		if(mission.getTestTrials() != null && !mission.getTestTrials().isEmpty()) {
			mission.setStartTime(new Date(currTime));
			int numBatchPlotsCreated = 0;
			Mission_4_5_6 mission456 = mission instanceof Mission_4_5_6 ? (Mission_4_5_6)mission : null;			
			for(IcarusTestTrial_Phase2 trial : mission.getTestTrials()) {
				if(generateTrialResponse(trial, mission.getTestTrials(),
						mission456 != null && mission456 .getMaxNumBatchPlots() != null ? 
						mission456.getMaxNumBatchPlots() : 0, 
						numBatchPlotsCreated, currTime)) {					
					numBatchPlotsCreated++;
				}
				currTime += trial.getTrialTime_ms();
			}
			mission.setEndTime(new Date(currTime));
		}
	}

	/**
	 * Generate random responses to the given trial.
	 * 
	 * @param trial the trial to generate a response for
	 * @pararm trialsInMission all trials in the mission
	 * @param maxNumBatchPlots the maximum number of batch plots that may be created in the mission containing the trial
	 * @param numBatchPlotsCreated the number of batch plots that have been created on previous trials in the mission.
	 * @param currTime the current time (in milliseconds since 1970 format). Used to create time stamps, 
	 * but models are not required to report time. Shown for illustrative purposes as human data will contain times.
	 * @return whether a batch plot was created
	 */
	public static boolean generateTrialResponse(IcarusTestTrial_Phase2 trial, 
			List<? extends IcarusTestTrial_Phase2> trialsInMission,
			int maxNumBatchPlots, int numBatchPlotsCreated, long currTime) {
		boolean isMission123Trial = trial instanceof Mission_1_2_3_Trial;
		Mission_1_2_3_Trial mission123Trial = isMission123Trial ? (Mission_1_2_3_Trial)trial : null;
		boolean isMission456Trial = trial instanceof Mission_4_5_6_Trial;
		Mission_4_5_6_Trial mission456Trial = isMission456Trial ? (Mission_4_5_6_Trial)trial : null;
		
		//NOTE: Times are not required to be reported by models, but are shown here as they will be recorded for humans
		long startTime = currTime;		
		
		//Generate a random response to the most likely red tactic probe (Mission 2)		
		if(isMission123Trial && mission123Trial.getMostLikelyRedTacticProbe() != null) {
			mission123Trial.getMostLikelyRedTacticProbe().setMostLikelyRedTactic(
					chooseRandomRedTactic(mission123Trial.getMostLikelyRedTacticProbe().getRedTactics()));
			long time = generateRandomTime(4000, 15000); currTime += time;
			mission123Trial.getMostLikelyRedTacticProbe().setTrialPartTime_ms(time);
		}
		
		//Generate a random response to the Red tactics probe in Missions 4 - 5
		boolean batchPlotCreated = false;
		if(isMission456Trial && mission456Trial.getRedTacticsProbe() != null) {
			AbstractRedTacticsProbe redTacticsProbe = mission456Trial.getRedTacticsProbe();			
			if(redTacticsProbe.getBatchPlotProbe() != null && numBatchPlotsCreated < maxNumBatchPlots) {
				//Choose to review a random number of previous trials in the batch plot				
				int numPreviousTrialsSelected = rand.nextInt(redTacticsProbe.getBatchPlotProbe().getPreviousTrials().size() + 1);
				if(numPreviousTrialsSelected > 0) {
					batchPlotCreated = true;
					redTacticsProbe.getBatchPlotProbe().setNumPreviousTrialsSelected(numPreviousTrialsSelected);
					//Specify sequence of Backward/Forward button clicks and select random locations to click on.
					//NOTE: This data is collected for human participants only and shown for illustrative purposes					
					redTacticsProbe.getBatchPlotProbe().setButtonPressSequence(
							ModelExample.generateBatchPlotButtonPressSequence(numPreviousTrialsSelected));
					redTacticsProbe.getBatchPlotProbe().setBlueLocationsClicked(
							ModelExample.clickRandomLocationsInBatchPlot(numPreviousTrialsSelected, 
									redTacticsProbe.getBatchPlotProbe().getPreviousTrials(), trialsInMission));					
				}
			}
			
			if(redTacticsProbe instanceof RedTacticsProbabilityReportProbe) {
				//Generate random probabilities that Red is playing with each tactic type (Missions 4 - 5)				
				RedTacticsProbabilityReportProbe redTacticsProbabilityProbe = (RedTacticsProbabilityReportProbe)redTacticsProbe;
				ArrayList<Double> probs = generateRandomPercentProbabilities(
						redTacticsProbabilityProbe.getProbabilities().size(), 
						redTacticsProbabilityProbe.getTargetSum(), 
						redTacticsProbabilityProbe.getNormalizationConstraint());
				long time = 0;			
				List<ItemAdjustment> sequence = new ArrayList<ItemAdjustment>();
				int i = 0;
				for(RedTacticProbability redTacticProb : redTacticsProbabilityProbe.getProbabilities()) {
					redTacticProb.setProbability(probs.get(i));
					long probTime = generateRandomTime(2000, 10000); time += probTime;
					redTacticProb.setTime_ms(probTime);
					sequence.add(new ItemAdjustment(i, currTime));
					currTime += probTime;
					i++;
				}
				redTacticsProbabilityProbe.setProbabilityAdjustmentSequence(sequence);
				redTacticsProbabilityProbe.setTrialPartTime_ms(time);
			} else if(redTacticsProbe instanceof RedTacticsChangesProbe) {
				//Specify random initial Red tactics and random Red tactics changes (Mission 6)
				//NOTE: Mission 6 is not currently part of the Phase 2 Challenge Problem and models are not expected to complete it.
				RedTacticsChangesProbe redTacticsChangesProbe = (RedTacticsChangesProbe)redTacticsProbe;
				redTacticsChangesProbe.setInitialRedTactics(new RedTacticParameters());
				RedTacticConsiderationData dataConsidered = redTacticsChangesProbe.getDataConsidered(); 
				redTacticsChangesProbe.getInitialRedTactics().setDataConsidered(dataConsidered);				
				redTacticsChangesProbe.getInitialRedTactics().setAttackProbabilities(
						generateRandomDecimalProbabilities(4, 1.D, NormalizationConstraintType.EqualTo));
				adjustAttackProbabilitiesBasedOnDataConsidered(dataConsidered, 
						redTacticsChangesProbe.getInitialRedTactics());
				int numChanges = redTacticsChangesProbe.getMinNumRedTacticsChanges() + 
						rand.nextInt(redTacticsChangesProbe.getMaxNumRedTacticsChanges() - 
								redTacticsChangesProbe.getMinNumRedTacticsChanges() + 1);
				List<RedTacticsChange> redTacticsChanges = new ArrayList<RedTacticsChange>(numChanges);
				redTacticsChangesProbe.setRedTacticsChanges(redTacticsChanges);
				List<Integer> changeTrials = new ArrayList<Integer>(numChanges);
				int numTrials = redTacticsProbe.getBatchPlotProbe().getPreviousTrials().size();
				for(int i=0; i<numChanges; i++) {
					Integer changeTrial = 2 + rand.nextInt(numTrials - 1);
					int numTries = 1;
					while(changeTrials.contains(changeTrial) && numTries < 100) {						
						changeTrial = rand.nextInt(numTrials + 1);
						numTries++;
					}
					changeTrials.add(changeTrial);
				}
				Collections.sort(changeTrials);
				for(Integer changeTrial : changeTrials) {
					RedTacticsChange tacticsChange = new RedTacticsChange();					
					tacticsChange.setTrialNum(changeTrial);
					tacticsChange.setDataConsidered(dataConsidered);
					tacticsChange.setAttackProbabilities(
							generateRandomDecimalProbabilities(4, 1.D, NormalizationConstraintType.EqualTo));
					adjustAttackProbabilitiesBasedOnDataConsidered(dataConsidered, tacticsChange);
					redTacticsChanges.add(tacticsChange);
				}
				long time = generateRandomTime(20000, 50000); currTime += time;
				redTacticsProbe.setTrialPartTime_ms(time);
			}
		}		
		
		//Generate a random response to the P(attack | IMINT, OSINT) probe (Missions 1 - 3)
		if(trial.getAttackPropensityProbe_Pp() != null) {
			ArrayList<Double> probs = generateRandomPercentProbabilities(
					trial.getAttackPropensityProbe_Pp().getProbabilities().size(), 
					trial.getAttackPropensityProbe_Pp().getTargetSum(), 
					trial.getAttackPropensityProbe_Pp().getNormalizationConstraint());			
			long time = 0;			
			List<ItemAdjustment> sequence = new ArrayList<ItemAdjustment>();
			int i = 0;
			for(AttackProbability attackProb : trial.getAttackPropensityProbe_Pp().getProbabilities()) {				
				attackProb.setProbability(probs.get(i));
				long probTime = generateRandomTime(2000, 10000); time += probTime;
				attackProb.setTime_ms(probTime);
				sequence.add(new ItemAdjustment(i, currTime));
				currTime += probTime;
				i++;
			}
			trial.getAttackPropensityProbe_Pp().setProbabilityAdjustmentSequence(sequence);
			trial.getAttackPropensityProbe_Pp().setTrialPartTime_ms(time);						
		}
		
		//Generate a random response to the P(attack | HUMINT, IMINT, OSINT) probe (Missions 1 - 3)
		if(trial.getAttackProbabilityProbe_Ppc() != null) {
			ArrayList<Double> probs = generateRandomPercentProbabilities(
					trial.getAttackProbabilityProbe_Ppc().getProbabilities().size(), 
					trial.getAttackProbabilityProbe_Ppc().getTargetSum(), 
					trial.getAttackProbabilityProbe_Ppc().getNormalizationConstraint());
			long time = 0;			
			List<ItemAdjustment> sequence = new ArrayList<ItemAdjustment>();
			int i = 0;
			for(AttackProbability attackProb : trial.getAttackProbabilityProbe_Ppc().getProbabilities()) {
				attackProb.setProbability(probs.get(i));
				long probTime = generateRandomTime(2000, 10000); time += probTime;
				attackProb.setTime_ms(probTime);
				sequence.add(new ItemAdjustment(i, currTime));
				currTime += probTime;
				i++;
			}
			trial.getAttackProbabilityProbe_Ppc().setProbabilityAdjustmentSequence(sequence);
			trial.getAttackProbabilityProbe_Ppc().setTrialPartTime_ms(time);			
		}
		
		//Generate a random SIGINT selection (Mission 3)
		if(trial.getSigintSelectionProbe() != null) {			
			int numLocations = trial.getSigintSelectionProbe().getLocationIds().size();
			List<String> selectedLocationIds = new ArrayList<String>(
					trial.getSigintSelectionProbe().getNumSigintSelections());
			trial.getSigintSelectionProbe().setSelectedLocationIds(selectedLocationIds);
			for(int i = 0; i < trial.getSigintSelectionProbe().getNumSigintSelections(); i++) {
				String locationId = null;
				do {
					locationId = trial.getSigintSelectionProbe().getLocationIds().get(rand.nextInt(numLocations));
				} while(selectedLocationIds.contains(locationId));
				selectedLocationIds.add(locationId);
			}
			long time = generateRandomTime(4000, 15000); currTime += time;
			trial.getSigintSelectionProbe().setTrialPartTime_ms(time);
		}
		
		//Generate a random response to the P(attack | SIGINT) probe (Mission 1)
		if(trial.getAttackProbabilityProbe_Pt() != null) {
			ArrayList<Double> probs = generateRandomPercentProbabilities(trial.getAttackProbabilityProbe_Pt().getProbabilities().size(), 
					trial.getAttackProbabilityProbe_Pt().getTargetSum(), 
					trial.getAttackProbabilityProbe_Pt().getNormalizationConstraint());
			long time = 0;			
			List<ItemAdjustment> sequence = new ArrayList<ItemAdjustment>();
			int i = 0;
			for(AttackProbability attackProb : trial.getAttackProbabilityProbe_Pt().getProbabilities()) {
				attackProb.setProbability(probs.get(i));
				long probTime = generateRandomTime(2000, 10000); time += probTime;
				attackProb.setTime_ms(probTime);
				sequence.add(new ItemAdjustment(i, currTime));
				currTime += probTime;
				i++;
			}
			trial.getAttackProbabilityProbe_Pt().setProbabilityAdjustmentSequence(sequence);
			trial.getAttackProbabilityProbe_Pt().setTrialPartTime_ms(time);			
		}
		
		//Generate a random response to the P(attack | SIGINT, HUMINT, IMINT, OSINT) probe (Missions 1 - 5)
		if(trial.getAttackProbabilityProbe_Ptpc() != null) {
			ArrayList<Double> probs = generateRandomPercentProbabilities(trial.getAttackProbabilityProbe_Ptpc().getProbabilities().size(), 
					trial.getAttackProbabilityProbe_Ptpc().getTargetSum(), 
					trial.getAttackProbabilityProbe_Ptpc().getNormalizationConstraint());
			long time = 0;			
			List<ItemAdjustment> sequence = new ArrayList<ItemAdjustment>();
			int i = 0;
			for(AttackProbability attackProb : trial.getAttackProbabilityProbe_Ptpc().getProbabilities()) {
				attackProb.setProbability(probs.get(i));
				long probTime = generateRandomTime(2000, 10000); time += probTime;
				attackProb.setTime_ms(probTime);
				sequence.add(new ItemAdjustment(i, currTime));
				currTime += probTime;
				i++;
			}
			trial.getAttackProbabilityProbe_Ptpc().setProbabilityAdjustmentSequence(sequence);
			trial.getAttackProbabilityProbe_Ptpc().setTrialPartTime_ms(time);
		}
		
		//Generate a random Blue action selection (Missions 2 - 5)
		if(trial.getBlueActionSelection() != null && !trial.getBlueActionSelection().isDataProvidedToParticipant()) {			
			for(BlueAction blueAction : trial.getBlueActionSelection().getBlueActions()) {
				blueAction.setAction(BlueActionType.values()[rand.nextInt(BlueActionType.values().length)]);
			}
			long time = generateRandomTime(4000, 15000); currTime += time;
			trial.getBlueActionSelection().setTrialPartTime_ms(time);
		}
		
		trial.setTrialTime_ms(currTime - startTime);
		return batchPlotCreated;
	}
	
	/**
	 * Choose a random Red tactic from the collection of Red tactics.
	 * 
	 * @param redTactics a collection of Red tactics
	 * @return a randomly chosen Red tactic
	 */
	protected static RedTacticType chooseRandomRedTactic(Collection<RedTacticType> redTactics) {
		int tacticIndex = rand.nextInt(redTactics.size());
		Iterator<RedTacticType> iter = redTactics.iterator();
		for(int i = 0; i < tacticIndex; i++) {
			iter.next();
		}
		return iter.next();
	}
	
	/**
	 * Generate random percent probabilities in the range [0 100).
	 * 
	 * @param numProbs
	 * @param targetSum
	 * @param normalizationConstraint
	 * @return
	 */
	protected static ArrayList<Double> generateRandomPercentProbabilities(int numProbs, Double targetSum, 
			NormalizationConstraintType normalizationConstraint) {
		ArrayList<Double> probs = new ArrayList<Double>(numProbs);
		for(int i = 0; i < numProbs; i++) {
			probs.add(rand.nextDouble() * 100);
		}
		if(targetSum != null && normalizationConstraint != null) {
				ProbabilityUtils.normalizePercentProbabilities_Double(probs, probs, 
						targetSum, normalizationConstraint);			
		} else {
			ProbabilityUtils.normalizePercentProbabilities_Double(probs, probs);
		}
		return probs;
	}
	
	/**
	 * Generate random decimal probabilities in the range [0 1).
	 * 
	 * @param numProbs
	 * @param targetSum
	 * @param normalizationConstraint
	 * @return
	 */
	protected static ArrayList<Double> generateRandomDecimalProbabilities(int numProbs, Double targetSum, 
			NormalizationConstraintType normalizationConstraint) {
		ArrayList<Double> probs = ModelExample.generateRandomPercentProbabilities(
				numProbs, targetSum, normalizationConstraint);
		if(probs != null && !probs.isEmpty()) {
			int i = 0;
			for(Double prob : probs) {
				probs.set(i, prob/100.D);
				i++;
			}
		}
		return probs;
	}
	
	/**
	 * Adjusts attack probabilities in a RedTacticParameters instance based on the dataConsidered type.
	 * For example, if P_Only considered, uses the Low_P_Large_U value for low P and the High_P_Large_U
	 * value for high P.
	 * 
	 * @param dataConsidered the dataConsidered type
	 * @param parameters the RedTacticParameters instance
	 */
	protected static void adjustAttackProbabilitiesBasedOnDataConsidered(
			RedTacticConsiderationData dataConsidered, RedTacticParameters parameters) {
		if(dataConsidered == RedTacticConsiderationData.P_Only) {
			Double lowPprob = parameters.getAttackProbability(RedTacticQuadrant.Low_P_Large_U);
			Double highPprob = parameters.getAttackProbability(RedTacticQuadrant.High_P_Large_U);			
			parameters.setAttackProbabilities(new ArrayList<Double>(Arrays.asList(
					lowPprob, lowPprob, highPprob, highPprob)));
		} else if(dataConsidered == RedTacticConsiderationData.U_Only) {
			Double smallUprob = parameters.getAttackProbability(RedTacticQuadrant.High_P_Small_U);
			Double largeUprob = parameters.getAttackProbability(RedTacticQuadrant.High_P_Large_U);			
			parameters.setAttackProbabilities(new ArrayList<Double>(Arrays.asList(
					smallUprob, largeUprob, smallUprob, largeUprob)));
		}
	}	
	
	/**
	 * Generates a random elapsed time (in milliseconds) in the range [minTime, maxTime).
	 * 
	 * @param minTime the minimum time to generate (inclusive of this time)
	 * @param maxTime the maximum time to generate (exclusive of this time)
	 * @return a random time elapsed (in milliseconds) in the range [minTime, maxTime).
	 */
	protected static long generateRandomTime(int minTime, int maxTime) {
		if(minTime < 0 || minTime >= maxTime) {
			throw new IllegalArgumentException("minTime must be >= 0 and < maxTime");
		}
		return rand.nextInt(maxTime - minTime) + minTime;
	}
	
	/**
	 * Generates random locations to click when creating a batch plot. Only 
	 * human subject participants will have this data.
	 * 
	 * @param numPreviousTrialsSelected the number of trials reviewed in the batch plot
	 * @param previousTrials the previous trials to review
	 * @param trials the trials in the mission
	 * @return a list of random locations that were clicked
	 */
	protected static List<String> clickRandomLocationsInBatchPlot(int numPreviousTrialsSelected,
			List<Integer> previousTrials, List<? extends IcarusTestTrial_Phase2> trials) {
		List<String> locations = null;
		int numLocationsClicked = rand.nextInt(numPreviousTrialsSelected+1);
		if(numLocationsClicked > 0) {
			locations = new LinkedList<String>();
			for(int i = 0; i < numLocationsClicked; i++) {
				int locationIndex = rand.nextInt(previousTrials.size());
				locations.add(trials.get(previousTrials.get(locationIndex)-1).getBlueLocations().get(0).getId());
			}
		}
		return locations;
	}
	
	/**
	 * Generates a sequence of Backward and Forward button presses when creating a batch plot. Only
	 * human subject participants will have this data.
	 * 
	 * @param numPreviousTrialsSelected the number of trials reviewed in the batch plot
	 * @return a sequence of Backward and Foward button presses
	 */
	protected static List<BatchPlotProbeButtonType> generateBatchPlotButtonPressSequence(
			int numPreviousTrialsSelected) {
		List<BatchPlotProbeButtonType> buttonSequence = new LinkedList<BatchPlotProbeButtonType>();
		for(int i=0; i<numPreviousTrialsSelected; i++) {
			buttonSequence.add(BatchPlotProbeButtonType.Backward);
		}
		if(numPreviousTrialsSelected > 2) {			
			buttonSequence.add(BatchPlotProbeButtonType.Forward);
		}
		return buttonSequence;
	}
	
	/**
	 * Example main that generates random responses to an exam.
	 * 
	 * @param args Specify the exam file name in args[0]. If null, uses default name "data/Phase_2_CPD/exams/Sample-Exam-DG/Sample-Exam-DG.xml".
	 */
	public static void main(String[] args) {
		String fileName = "data/Phase_2_CPD/exams/Sample-Exam-DG/Sample-Exam-DG.xml"; 
		if(args != null && args.length > 0) {
			fileName = args[0];
		}

		//Load the exam 
		IcarusExam_Phase2 exam = null;
		URL examFileURL = null;
		try {
			examFileURL = CPSFileUtils.createFile(fileName).toURI().toURL();			
			exam = ParsingExample.loadExamAndFeatureVectors(examFileURL);
		} catch(Exception ex) {
			ex.printStackTrace();
		}

		if(exam != null) {
			//Generate random responses to all trials in all missions in the exam
			generateExamResponse(exam);
			
			//Display the exam with responses
			System.out.println("Exam with random responses:");
			try {
				System.out.println(IcarusExamLoader_Phase2.marshalExam(exam));
			} catch (JAXBException e) {
				e.printStackTrace();
			}
		}

		System.out.println("Press ENTER to close this window");
		try {
			System.in.read();
		} catch(Exception err) {}
	}
}