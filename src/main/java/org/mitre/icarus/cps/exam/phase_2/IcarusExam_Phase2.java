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
package org.mitre.icarus.cps.exam.phase_2;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.app.window.controller.ApplicationController;
import org.mitre.icarus.cps.exam.base.IcarusExam;
import org.mitre.icarus.cps.exam.phase_2.loader.IcarusExamLoader_Phase2;
import org.mitre.icarus.cps.exam.phase_2.testing.IcarusTestTrial_Phase2;
import org.mitre.icarus.cps.exam.phase_2.testing.Mission;
import org.mitre.icarus.cps.exam.phase_2.testing.MissionType;
import org.mitre.icarus.cps.exam.phase_2.testing.Mission_1_2_3;
import org.mitre.icarus.cps.exam.phase_2.testing.Mission_4_5_6_Trial;
import org.mitre.icarus.cps.exam.phase_2.testing.bluebook.BlueBook;
import org.mitre.icarus.cps.exam.phase_2.testing.bluebook.PayoffMatrix;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.BlueAction;
import org.mitre.icarus.cps.exam.phase_2.training.IcarusExamTutorial_Phase2;
import org.mitre.icarus.cps.feature_vector.phase_2.BlueLocation;
import org.mitre.icarus.cps.feature_vector.phase_2.int_datum.SigintReliability;

/**
 * Defines an ICArUS exam for the Phase 2 CPD format.  The exam consists of
 * an ordered list of missions, possibly preceded by a pause phase.
 * The pause phases are for human subject use only.
 *  
 * @author CBONACETO
 *
 */
@XmlRootElement(name="IcarusExam_Phase2", namespace="IcarusCPD_2")
@XmlType(name="IcarusExam_Phase2", namespace="IcarusCPD_2", 
		propOrder={"applicationVersion", "tutorial", "blueBook", 
		"payoffMatrix", "sigintReliability", "missions", "blueScore", "redScore"} )
public class IcarusExam_Phase2 extends IcarusExam<Mission<?>> {
	
	/** Application version information. FOR HUMAN SUBJECT USE ONLY. */
	protected String applicationVersion = ApplicationController.VERSION;
	
	/** The tutorial for the exam (if any). FOR HUMAN SUBJECT USE ONLY. */
	protected IcarusExamTutorial_Phase2 tutorial;
	
	/** The BlueBook */
	protected BlueBook blueBook;
	
	/** The payoff matrix */
	protected PayoffMatrix payoffMatrix;
	
	/** The SIGINT reliability (constant for the entire exam) */
	protected SigintReliability sigintReliability;
	
	/** Contains the ordered list of missions */
	protected List<Mission<?>> missions;
	
	/** The Blue player score for the exam */
	protected Double blueScore;
	
	/** The Red player score for the exam */
	protected Double redScore;
	
	/**
	 * Construct an exam with an empty list of missions. 
	 */
	public IcarusExam_Phase2() {
		/*missions = new ArrayList<Mission<?>>(3);
		for(int i=0; i<3; i++) {
			missions.add(null);
		}*/
	}

	/**
	 * Get the application version information.
	 * 
	 * @return the version information
	 */
	@XmlElement(name="ApplicationVersion")
	public String getApplicationVersion() {
		return applicationVersion;
	}

	/**
	 * Set the application version information.
	 * 
	 * @param applicationVersion the version information
	 */
	public void setApplicationVersion(String applicationVersion) {
		this.applicationVersion = applicationVersion;
	}

	/**
	 * Get the tutorial.	 
	 * FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @return the tutorial
	 */
	@XmlElement(name="Tutorial")
	public IcarusExamTutorial_Phase2 getTutorial() {
		return tutorial;
	}

	/**
	 * Set the tutorial.
	 * FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @param tutorial the tutorial
	 */
	public void setTutorial(IcarusExamTutorial_Phase2 tutorial) {
		this.tutorial = tutorial;
	}	

	/**
	 * Get the BLUEBOOK for the exam, which defines Red tactics.
	 * 
	 * @return the BLUEBOOK
	 */
	@XmlElement(name="BlueBook")
	public BlueBook getBlueBook() {
		return blueBook;
	}

	/**
	 * Set the BLUEBOOK for the exam, which defines Red tactics.
	 * 
	 * @param blueBook the BLUEBOOK
	 */
	public void setBlueBook(BlueBook blueBook) {
		this.blueBook = blueBook;
	}

	/**
	 * Get the payoff matrix for the exam.
	 * 
	 * @return the payoff matrix
	 */
	@XmlElement(name="PayoffMatrix")
	public PayoffMatrix getPayoffMatrix() {
		return payoffMatrix;
	}

	/**
	 * Set the payoff matrix for the exam.
	 * 
	 * @param payoffMatrix the payoff matrix
	 */
	public void setPayoffMatrix(PayoffMatrix payoffMatrix) {
		this.payoffMatrix = payoffMatrix;
	}

	/**
	 * Get the SIGINT reliabilities.
	 * 
	 * @return the SIGINT reliabilities.
	 */
	@XmlElement(name="SigintReliabilities")
	public SigintReliability getSigintReliability() {
		return sigintReliability;
	}

	/**
	 * Set the SIGINT reliabilities.
	 * 
	 * @param sigintReliability the SIGINT reliabilities.
	 */
	public void setSigintReliability(SigintReliability sigintReliability) {
		this.sigintReliability = sigintReliability;
	}

	/**
	 * Get the missions in the exam.
	 * 
	 * @return the missions
	 */
	@XmlElement(name="Mission")
	public List<Mission<?>> getMissions() {
		return missions;
	}	

	/**
	 * Set the missions in the exam.
	 * 
	 * @param missions the missions
	 */
	public void setMissions(List<Mission<?>> missions) {
		this.missions = missions;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment_core.Experiment#getConditions()
	 */
	@Override
	@XmlTransient
	public List<Mission<?>> getConditions() {
		return missions;
	}

	/**
	 * Get the overall Blue score across all missions in the exam.
	 * 
	 * @return the overall Blue score
	 */
	@XmlAttribute(name="BlueScore")
	public Double getBlueScore() {
		return blueScore;
	}

	/**
	 * Set the overall Blue score across all missions in the exam.
	 * 
	 * @param blueScore the overall Blue score
	 */
	public void setBlueScore(Double blueScore) {
		this.blueScore = blueScore;
	}

	/**
	 * Get the overall Red score across all missions in the exam.
	 * 
	 * @return the overall Red score
	 */
	@XmlAttribute(name="RedScore")
	public Double getRedScore() {
		return redScore;
	}

	/**
	 * Set the overall Red score across all missions in the exam.
	 * 
	 * @param redScore the overall Red score
	 */
	public void setRedScore(Double redScore) {
		this.redScore = redScore;
	}	
	
	/**
	 * Updates the cumulative Blue and Red scores by adding up the Blue and Red scores fore each Mission.
	 */
	public void updateExamScore() {
		blueScore = 0D;
		redScore = 0D;
		if(missions != null && !missions.isEmpty()) {
			for(Mission<?> mission : missions) {
				blueScore += mission.getBlueScore() != null ? mission.getBlueScore() : 0;
				redScore += mission.getRedScore() != null ? mission.getRedScore() : 0;
			}
		}
	}
	
	/**
	 * Clears the response data for all trials in all missions.
	 */
	public void clearResponseData() {
		startTime = null;
		endTime = null;
		blueScore = null;
		redScore = null;
		if(tutorial != null) {
			tutorial.setStartTime(null);
			tutorial.setEndTime(null);
		}
		if(missions != null && !missions.isEmpty()) {
			for(Mission<?> mission : missions) {
				mission.clearResponseData();
			}
		}
	}
	
	/**
	 * Get the index of the mission in the list of missions with the given ID.
	 * 
	 * @param missionId the mission ID
	 * @return the index of the mission in the list of missions, or -1 if the mission was not found
	 */
	public int getMissionIndex(String missionId) {
		if(missions != null && !missions.isEmpty()) {
			int index = 0;
			for(Mission<?> mission : missions) {
				if(mission.getId() != null && mission.getId().equals(mission)) {
					return index;
				}
				index++;
			}
		}
		return -1;
	}
	
	/**
	 * Return a report comparing the stimuli in two exams.
	 * 
	 * @param exam1 the first exam to compare
	 * @param exam2 the second exam to compare
	 * @return a report comparing the stimuli in exams 1 and 2
	 */
	public static ExamComparisonReport compareExams(IcarusExam_Phase2 exam1, 
			IcarusExam_Phase2 exam2) {
		ExamComparisonReport report = new ExamComparisonReport();
		report.examsEqual = true;
		report.examsContainSameMissionTypes = true;
		report.examsContainMissionsInSameOrder = true;
		if(exam1 != null) {
			if(exam2 != null) {				
				List<Mission<?>> exam1Missions = exam1.getMissions();
				List<Mission<?>> exam2Missions = exam2.getMissions();
				if(exam1Missions != null) {
					int exam1MissionIndex = 0;
					for(Mission<?> exam1Mission : exam1Missions) {
						Mission<?> exam2Mission = null;
						int exam2MissionIndex = 0;
						Iterator<Mission<?>> exam2MissionIter = exam2Missions.iterator();
						while(exam2MissionIter.hasNext() && exam2Mission == null) {
							Mission<?> mission = exam2MissionIter.next();
							if(mission.getMissionType() == exam1Mission.getMissionType()) {
								exam2Mission = mission;
							} else {
								exam2MissionIndex++;
							}
						}
						if(exam2Mission != null) {
							if(exam1MissionIndex != exam2MissionIndex) {
								report.examsContainMissionsInSameOrder = false;								
							}
							if(report.missionComparisonReports == null) {
								report.missionComparisonReports = new ArrayList<MissionComparisonReport>();
							}
							report.missionComparisonReports.add(compareMissions(exam1Mission, exam2Mission));
						} else {
							report.examsContainSameMissionTypes = false;
							report.examsContainMissionsInSameOrder = false;							
						}
						exam1MissionIndex++;
					}
				} else {
					report.examsEqual = exam2Missions == null;
					report.examsContainSameMissionTypes = report.examsEqual;
					report.examsContainMissionsInSameOrder = report.examsEqual;
				}
			} else {
				report.examsEqual = false;
				report.examsContainSameMissionTypes = false;
				report.examsContainMissionsInSameOrder = false;		
			}
		} else {			
			report.examsEqual = exam2 == null;
			report.examsContainSameMissionTypes = report.examsEqual;
			report.examsContainMissionsInSameOrder = report.examsEqual;
		}
		return report;
	}
	
	/**
	 * Return a report comparing the stimuli in two missions.
	 * 
	 * @param mission1 the first mission to compare
	 * @param mission2 the second mission to compare
	 * @return a report comparing the stimuli in Missions 1 and 2
	 */
	public static MissionComparisonReport compareMissions(Mission<?> mission1, 
			Mission<?> mission2) {
		MissionComparisonReport report = new MissionComparisonReport();
		report.missionsEqual = true;
		report.missionsContainSameNumberOfTrials = true;
		if(mission1 != null) {
			if(mission2 != null && mission1.getMissionType() == mission2.getMissionType()) {				
				if(mission1 instanceof Mission_1_2_3) {
					report.missionsEqual = 
							((Mission_1_2_3)mission1).getRedTactic() == ((Mission_1_2_3)mission2).getRedTactic(); 
				} /*else if(mission1 instanceof Mission_4_5_6) {
					report.missionsEqual = 
							((Mission_4_5_6)mission1).getMaxNumBatchPlots() == ((Mission_4_5_6)mission2).getMaxNumBatchPlots();
				}*/
				List<? extends IcarusTestTrial_Phase2> mission1Trials = mission1.getTestTrials();
				List<? extends IcarusTestTrial_Phase2> mission2Trials = mission2.getTestTrials();
				if(mission1Trials != null && !mission1Trials.isEmpty()) {
					if(mission2Trials != null && 
							mission1Trials.size() == mission2Trials.size()) {						
						Iterator<? extends IcarusTestTrial_Phase2> mission1TrialIter = mission1Trials.iterator();
						Iterator<? extends IcarusTestTrial_Phase2> mission2TrialIter = mission2Trials.iterator();
						int trialIndex = 0;
						while(mission1TrialIter.hasNext()) {
							IcarusTestTrial_Phase2 trial1 = mission1TrialIter.next();
							IcarusTestTrial_Phase2 trial2 = mission2TrialIter.next();							
							if(!compareTrialStimuli(trial1, trial2)) {								
								report.missionsEqual = false;
								if(report.mismatchedTrials == null) {
									report.mismatchedTrials = new ArrayList<Integer>();
								}
								report.mismatchedTrials.add(trialIndex);
							}
							trialIndex++;
						}
					} else {
						report.missionsEqual = false;
						report.missionsContainSameNumberOfTrials = false;
					}					
				} else {
					report.missionsEqual = mission2Trials == null || mission2Trials.isEmpty();
					report.missionsContainSameNumberOfTrials = report.missionsEqual;
				}
			} else {
				report.missionsEqual = false;
				report.missionsContainSameNumberOfTrials = false;				
			}
		} else {			
			report.missionsEqual = mission2 == null;
			report.missionsContainSameNumberOfTrials = report.missionsEqual;
		}
		return report;
	}
	
	/**
	 * Return whether the stimuli in trial1 and trial2 are the same.
	 * Checks the Blue locations, HUMINT, Blue actions (if provided to the participant),
	 * Red action, and Red tactics. 
	 * 
	 * @param trial1 the first trial to compare
	 * @param trial2 the second trial to compare
	 * @return whether the stimuli in trials 1 and 2 is the same
	 */
	public static boolean compareTrialStimuli(IcarusTestTrial_Phase2 trial1,
			IcarusTestTrial_Phase2 trial2) {		
		if(trial1 != null) {
			if(trial2 != null && trial1.getClass() == trial2.getClass()) {
				//Check the Blue locations
				if(trial1.getBlueLocations() != null && !trial1.getBlueLocations().isEmpty()) {
					if(trial2.getBlueLocations() != null && !trial2.getBlueLocations().isEmpty()) {
						Iterator<BlueLocation> locationIter1 = trial1.getBlueLocations().iterator();
						Iterator<BlueLocation> locationIter2 = trial2.getBlueLocations().iterator();
						while(locationIter1.hasNext()) {
							BlueLocation location1 = locationIter1.next();
							BlueLocation location2 = locationIter2.next();
							if(location1.getImint() != null) {
								if(location2.getImint() !=  null) {
									if(!location1.getImint().getRedOpportunity_U().equals(
											location2.getImint().getRedOpportunity_U())) {
										return false;
									}
								} else {
									return false;
								}
							} else if(location2.getImint() != null) {
								return false;
							}
							if(location1.getOsint() != null) {
								if(location2.getOsint() != null) {
									if(!location1.getOsint().getRedVulnerability_P().equals(
										location2.getOsint().getRedVulnerability_P())) {
										return false;
									}
								} else {
									return false;
								}								
							} else if(location2.getOsint() != null) { 
								return false;
							}
							if(location1.getSigint() != null) {
								if(location2.getSigint() != null) {
									if(!location1.getSigint().isRedActivityDetected().equals(
										location2.getSigint().isRedActivityDetected())) {
										return false;
									}
								} else {
									return false;
								}								
							} else if(location2.getSigint() != null) { 
								return false;
							}
						}
					} else {
						return false;
					}
				} else if(trial2.getBlueLocations() != null && !trial2.getBlueLocations().isEmpty()) {
					return false;
				}
				//System.out.println("Locations Matched");

				//Check HUMINT
				if(trial1.getHumint() != null) {
					if(trial2.getHumint() != null) {						
						if(!trial1.getHumint().getRedCapability_Pc().equals(
								trial2.getHumint().getRedCapability_Pc())) {
							return false;
						}
					} else {
						return false;
					}
				} else if(trial2.getHumint() != null) {
					return false;
				}
				//System.out.println("HUMINT Matched");

				//Check the Blue action selection (if provided to the participant)				
				if(trial1.getBlueActionSelection() != null && 
						trial1.getBlueActionSelection().isDataProvidedToParticipant() &&
						trial1.getBlueActionSelection().getBlueActions() != null
						&& !trial1.getBlueActionSelection().getBlueActions().isEmpty()) {
					if(trial2.getBlueActionSelection() != null && 
							trial2.getBlueActionSelection().isDataProvidedToParticipant() &&
							trial2.getBlueActionSelection().getBlueActions() != null
							&& trial2.getBlueActionSelection().getBlueActions().size() ==
								trial1.getBlueActionSelection().getBlueActions().size()) {
						Iterator<BlueAction> blueActionIter1 = 
								trial1.getBlueActionSelection().getBlueActions().iterator();
						Iterator<BlueAction> blueActionIter2 = 
								trial2.getBlueActionSelection().getBlueActions().iterator();
						while(blueActionIter1.hasNext()) {
							BlueAction blueAction1 = blueActionIter1.next();
							BlueAction blueAction2 = blueActionIter2.next();
							if(blueAction1.getAction() != blueAction2.getAction()) {
								return false;
							}
						}
					} else {
						return false;
					}
				} else if(trial2.getBlueActionSelection() != null && 
						trial2.getBlueActionSelection().isDataProvidedToParticipant() &&
						trial2.getBlueActionSelection().getBlueActions() != null
						&& !trial2.getBlueActionSelection().getBlueActions().isEmpty()) {
					return false;
				}
				//System.out.println("Blue Actions Matched");

				//Check the Red action selection
				if(trial1.getRedActionSelection() != null) {
					if(trial2.getRedActionSelection() != null) {
						if(trial1.getRedActionSelection().getRedAction().getAction() != 
								trial2.getRedActionSelection().getRedAction().getAction()) {
							return false;
						}
					} else {
						return false;
					}
				} else if(trial2.getRedActionSelection() != null) {
					return false;
				}

				//Check the Red tactic (Missions 4-6)
				if(trial1 instanceof Mission_4_5_6_Trial) {
					if(trial2 instanceof Mission_4_5_6_Trial) {
						Mission_4_5_6_Trial trial1Mission456 = (Mission_4_5_6_Trial)trial1;
						Mission_4_5_6_Trial trial2Mission456 = (Mission_4_5_6_Trial)trial2;
						if(trial1Mission456.getRedTactic() != trial2Mission456.getRedTactic()) {
							return false;
						}
					} else {
						return false;
					}
				} else if(trial2 instanceof Mission_4_5_6_Trial) {
					return false;
				}
				//System.out.println("Red Actions Matched");
				
				//If here, everything above matched up, so return true
				return true;
			} else {
				return false;
			}
		} else {
			return trial2 == null;
		}
	}
	
	/**
	 * Contains data on the comparison of the stimuli in two exams.
	 * 
	 * @author CBONACETO
	 *
	 */
	@XmlTransient
	public static class ExamComparisonReport {
		
		public boolean examsEqual;
		
		public boolean examsContainSameMissionTypes;
		
		public boolean examsContainMissionsInSameOrder;
		
		public List<MissionComparisonReport> missionComparisonReports;
	}
	
	/**
	 * Contains data on the comparison of the stimuli in two missions.
	 * 
	 * @author CBONACETO
	 *
	 */
	@XmlTransient
	public static class MissionComparisonReport {		
		
		public MissionType missionType;
		
		public boolean missionsEqual;
		
		public boolean missionsContainSameNumberOfTrials;
		
		public List<Integer> mismatchedTrials;
	}
	
	public static void main(String[] args) {
		String examName = "Sample-Exam-1";
		File exam1Folder = new File("data/Phase_2_CPD/exams/" + examName);
		File exam2Folder = new File("distrib/IcarusDeveloperTools_v2.1f/data/Phase_2_CPD/exams/" + examName);
		//URL examFolderUrl = examFolder.toURI().toURL();
		try {
			IcarusExam_Phase2 exam1 = IcarusExamLoader_Phase2.unmarshalExam(
					new File(exam1Folder, examName + ".xml").toURI().toURL());
			IcarusExam_Phase2 exam2 = IcarusExamLoader_Phase2.unmarshalExam(
					new File(exam2Folder, examName + ".xml").toURI().toURL());
			System.out.println("Exams same: " + compareExams(exam1, exam2).examsEqual);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}