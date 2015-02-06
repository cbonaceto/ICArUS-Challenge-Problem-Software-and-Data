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
package org.mitre.icarus.cps.app.experiment.phase_2;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.mitre.icarus.cps.app.experiment.IcarusExamController;
import org.mitre.icarus.cps.app.experiment.IcarusSubjectData;
import org.mitre.icarus.cps.app.experiment.data_recorder.ISubjectDataRecorder;
import org.mitre.icarus.cps.app.widgets.WidgetConstants;
import org.mitre.icarus.cps.app.widgets.dialog.ErrorDlg;
import org.mitre.icarus.cps.app.widgets.phase_2.ImageManager_Phase2;
import org.mitre.icarus.cps.app.widgets.phase_2.experiment.ConditionPanel_Phase2;
import org.mitre.icarus.cps.app.widgets.phase_2.experiment.ExperimentPanel_Phase2;
import org.mitre.icarus.cps.app.widgets.progress_monitor.SwingProgressMonitor;
import org.mitre.icarus.cps.exam.base.IcarusExamPhase;
import org.mitre.icarus.cps.exam.base.pause.IcarusPausePhase;
import org.mitre.icarus.cps.exam.base.response.ResponseGeneratorData;
import org.mitre.icarus.cps.exam.base.testing.IcarusTestTrial;
import org.mitre.icarus.cps.exam.phase_2.IcarusExam_Phase2;
import org.mitre.icarus.cps.exam.phase_2.loader.IcarusExamLoader_Phase2;
import org.mitre.icarus.cps.exam.phase_2.testing.Mission;
import org.mitre.icarus.cps.exam.phase_2.testing.Mission_1_2_3;
import org.mitre.icarus.cps.exam.phase_2.testing.Mission_4_5_6;
import org.mitre.icarus.cps.exam.phase_2.training.IcarusExamTutorial_Phase2;
import org.mitre.icarus.cps.experiment_core.condition.InstructionsPage;
import org.mitre.icarus.cps.experiment_core.event.ConditionEvent;
import org.mitre.icarus.cps.experiment_core.event.SubjectActionEvent;
import org.mitre.icarus.cps.experiment_core.gui.InstructionsPanel;
import org.mitre.icarus.cps.experiment_core.gui.INavButtonPanel.ButtonType;
import org.mitre.icarus.cps.experiment_core.subject_data.SubjectData;

/**
 * Controller for the Phase 2 exam.
 * 
 * @author CBONACETO
 *
 */
public class IcarusExamController_Phase2 extends 
	IcarusExamController<IcarusExam_Phase2, Mission<?>, IcarusExamTutorial_Phase2,
	ExperimentPanel_Phase2, ConditionPanel_Phase2> implements HyperlinkListener {	
	
	/** Whether the exam tutorial has been shown */
	protected boolean examTutorialShown = false;
	
	/** The current mission being executed */
	protected Mission<?> currentCondition;
	
	/** Time the subject started the current mission or tutorial */
	protected long conditionStartTime = 0;
	
	/** The missions whose feature vector data has been initialized */
	protected Set<Mission<?>> initializedConditions;
    
	/** Tutorial controller */
	protected TutorialController_Phase2<IcarusExamController_Phase2, IcarusExam_Phase2> tutorialController;
	
    /** The current mission controller */
	protected MissionController<?, ?> currentConditionController;
	
	/** Controller for Missions 1-3 */
	protected Mission_1_2_3_Controller mission123Controller;
	
	/** Controller for Missions 4-6 */
	protected Mission_4_5_6_Controller mission456Controller;
	
	/** Whether to skip the next pause (if any) */
	protected boolean skipNextPause = false;
	
	/** Whether to clear response data at the start of each mission */
	private boolean clearResponseData = true;
	
	/** Random number generator for computing showdowns */
	private static Random random;
	
	/** 
	 * Constructor takes a score computer instance and data recorder instance.
	 * 
	 * @param scoreComputer the score computer to use to compute the subject's S1 and S2 scores
	 * @param dataRecorder the data recorder to use to record subject data
	 */
	public IcarusExamController_Phase2(
			ISubjectDataRecorder<IcarusExam_Phase2, Mission<?>, IcarusExamTutorial_Phase2> dataRecorder) {
		super(dataRecorder);
		tutorialController = new TutorialController_Phase2<IcarusExamController_Phase2, IcarusExam_Phase2>();
		mission123Controller = new Mission_1_2_3_Controller();
		mission456Controller = new Mission_4_5_6_Controller();
	}	
	
	@Override
	public void initializeExperimentController(IcarusExam_Phase2 experiment) {
		skipNextPause = false;
		examTutorialShown = false;
		
		//Stop the currently executing exam
		stopExperiment();

		//Initialize the experiment panel
		this.exam = experiment;		
		experimentPanel.setExperimentName(exam.getName());
		experimentPanel.removeAllInstructionPages();
		
		if(exam.getMissions() != null && !exam.getMissions().isEmpty()) {
			//Number the phases (some phases may not be counted)
			initializedConditions = new HashSet<Mission<?>>(exam.getMissions().size());			
			int currPhase = 1;
			for(Mission<?> phase : exam.getMissions()) {
				if(!phase.isCountCondition()) {
					phase.setConditionNum(-1);
				}
				else {
					phase.setConditionNum(currPhase);
					currPhase++;
				}
			}
			experimentPanel.setNumConditions(currPhase-1);
		} else {
			initializedConditions = new HashSet<Mission<?>>();
			experimentPanel.setNumConditions(0);
		}
	}


	@Override
	public void startExperiment(SubjectData sData) {
		if(exam == null || experimentPanel == null) {
			throw new IllegalArgumentException("Error starting experiment controller: exam and experiment panel must be set");
		}
		
		subjectData = (IcarusSubjectData)sData;
		
		//Stop the currently executing exam
		stopExperiment();	
		conditionPanel.showBlankPage();

		//Execute the experiment in a background thread		
		experimentWorker = new SwingWorker<Object, Object>() {				
			@Override
			protected void done() {
				try {
					if(currentConditionController != null) {
						experimentPanel.removeSubjectActionListener(currentConditionController);
						currentConditionController.removeConditionListener(IcarusExamController_Phase2.this);
						currentConditionController.stopCondition();
					}
				} catch(Exception ex) {}
			}

			@Override
			protected Object doInBackground() {
				try {
					// Wait until the previously executing experiment worker finishes executing (waits up to 5 seconds)
					long exitTime = System.currentTimeMillis() + 5000;
					while(!exitedWorker && !isCancelled()) {					
						try {
							Thread.sleep(50);
						} catch(InterruptedException ex) {}
						if(System.currentTimeMillis() > exitTime) {
							//Show an error dialog and exit
							ErrorDlg.showErrorDialog(experimentPanel.getParentWindow(), new Exception("Error, could not end currently executing mission."), false);
							exitedWorker = true;
							experimentRunning = false;
							return null;
						}
					}					
					
					experimentRunning = true;		
					exitedWorker = false;
					
					experimentPanel.setReviewTutorialButtonText("Exam Tutorial");
					if(subjectData != null) {						
						experimentPanel.setSubject(subjectData.toString());
						currentConditionIndex = subjectData.getCurrentCondition();
						currentTrial = subjectData.getCurrentTrial();
						if(currentConditionIndex < 0) { currentConditionIndex = 0; }
					} else {
						currentConditionIndex = 0;
						currentTrial = -1; //currentTrial = 1;
					}					
					currentCondition = null;					

					while(experimentRunning && currentConditionIndex < exam.getConditions().size() && !isCancelled()) {
						conditionPanel.getMapPanel().setLayerInstructionsWindowVisible(false);
						experimentPanel.clearCurrentConfiguration();
						if(experimentRunning && currentConditionIndex >= 0) {
							conditionRunning = false;
							SwingUtilities.invokeLater(new Runnable() {
								public void run() {
									experimentPanel.setNavButtonEnabled(ButtonType.Back, false);
									experimentPanel.setNavButtonEnabled(ButtonType.Next, false);
									experimentPanel.setReviewTutorialButtonVisible(false);
									experimentPanel.setNavButtonVisible(ButtonType.Help, false);
									experimentPanel.setReviewTutorialButtonVisible(false);
								}});
							
							if(currentConditionIndex == 0 && exam.getTutorial() != null && !examTutorialShown) {
								//Show the tutorial pages for the exam
								examTutorialShown = true;
								SwingUtilities.invokeLater(new Runnable() {
									public void run() {
										setNavButtonEnabled(ButtonType.Next, false);
										setNavButtonEnabled(ButtonType.Back, false);									
										experimentPanel.showContent(tutorialPanel);
										experimentPanel.setTrialDescriptor("Page");
										experimentPanel.setConditionNumber("Exam Tutorial", -1, exam.getTutorial().getNumTrials());
										experimentPanel.setTrialNumber(1);
									}
								});			

								initializeTutorialPhase(exam.getTutorial());
								tutorialController.setExam(exam);
								tutorialController.initializeConditionController(exam.getTutorial(), tutorialPanel);
								if(experimentRunning && !isCancelled()) {									
									conditionRunning = true;
									//Execute tutorial controller in the event dispatching thread
									SwingUtilities.invokeLater(new Runnable() {
										public void run() {
											experimentPanel.addSubjectActionListener(tutorialController);																			
											try {												
												tutorialController.addConditionListener(IcarusExamController_Phase2.this);
												tutorialController.startCondition(0, IcarusExamController_Phase2.this, null);
												conditionStartTime = System.currentTimeMillis();
											}
											catch (Exception ex) {
												ex.printStackTrace();
												ErrorDlg.showErrorDialog(experimentPanel.getParentWindow(), ex, true);
												try {
													tutorialController.stopCondition();
												} catch(Exception e) {}
												conditionRunning = false;
											}
										}});
								}
								//Wait for the tutorial to complete
								while(conditionRunning && experimentRunning && !isCancelled()) {try {Thread.sleep(50);} catch(Exception ex) {}}
								
								exam.getTutorial().setStartTime(new Date(conditionStartTime));
								exam.getTutorial().setEndTime(new Date(System.currentTimeMillis()));
								experimentPanel.removeSubjectActionListener(tutorialController);
								tutorialController.removeConditionListener(IcarusExamController_Phase2.this);
								SwingUtilities.invokeLater(new Runnable() {
									public void run() {
										experimentPanel.setNavButtonEnabled(ButtonType.Back, false);
										experimentPanel.setNavButtonEnabled(ButtonType.Next, false);										
									}});
								//Record the time spent on the tutorial
								if(experimentRunning && !isCancelled() && dataRecorder != null && dataRecorder.isEnabled()) {										
									boolean dataRecorded = false;
									boolean retry = false;
									do {											
										try {
											dataRecorder.recordTutorialPhaseData(subjectData, exam, exam.getTutorial(), 
													IcarusExamLoader_Phase2.examLoaderInstance);								
											dataRecorded = true;
										} catch (Exception e) {												
											//e.printStackTrace();
											ErrorDlg.showErrorDialog(experimentPanel.getParentWindow(), 
													new Exception("There was a problem recording your data", e), true);												
											retry = JOptionPane.showConfirmDialog(experimentPanel.getParentWindow(), 
													"<html>The network connection to the server may have been lost. Would you like to try to record your data again?<br>" +
															"If you select No, the experiment will end and you may try continuing at a later time.</html>", "", 
															JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
										}
									} while(experimentRunning && !isCancelled() && !dataRecorded && retry);
								}
							}
							
							if(!experimentRunning || isCancelled()) {
								experimentRunning = false;
								exitedWorker = true;
								return null;
							}

							currentCondition = exam.getMissions().get(currentConditionIndex);						

							//Load the feature vector data and any instructions pages for the task if it hasn't been loaded yet
							if(!initializedConditions.contains(currentCondition)) {
								try {
									IcarusExamLoader_Phase2.initializeMissionFeatureVectorData(currentCondition, 
											exam.getOriginalPath(),  
											new SwingProgressMonitor(experimentPanel, "Loading Feature Vector Data", 
													"Loading Feature Vector Data", 0, 100));
									initializedConditions.add(currentCondition);
									if(currentCondition.getInstructionPages() != null && !currentCondition.getInstructionPages().isEmpty()) {
										initializeExamPhaseTutorial(currentCondition);
									}
								} catch(Exception ex) {
									ex.printStackTrace();
									ErrorDlg.showErrorDialog(experimentPanel.getParentWindow(), ex, true);
								}
							}

							//Show a pause phase before starting the task if present
							if(currentCondition.getPausePhase() != null) {	
								if(!skipNextPause) {
									final IcarusPausePhase pause = currentCondition.getPausePhase();
									SwingUtilities.invokeLater(new Runnable() {
										public void run() {
											experimentPanel.setConditionNumber("Pause", -1, -1);
										}
									});
									if(!pause.isShowCountdown()) {
										//Show a non-timed pause phase before starting the task
										SwingUtilities.invokeLater(new Runnable() {
											public void run() {											
												experimentPanel.setInstructionText(InstructionsPanel.formatTextAsHTML(
														pause.getInstructionText(), "center", 
														WidgetConstants.FONT_INSTRUCTION_PANEL.getName(), 
														WidgetConstants.FONT_SIZE_HTML));					
												experimentPanel.showInternalInstructionPanel();
												experimentPanel.setNavButtonEnabled(ButtonType.Next, true);
											}
										});
										//Wait for the next button to be pressed
										nextButtonPressed = false;
										while(!nextButtonPressed && experimentRunning && !isCancelled()) {try {Thread.sleep(50);} catch(Exception ex) {}}
									} else {
										//Show a timed pause phase before starting the task
										SwingUtilities.invokeLater(new Runnable() {
											public void run() {
												conditionPanel.showBreakScreen(pause.isShowCountdown());
												conditionPanel.setBreakOver(false);
												if(currentCondition.getInstructionText() != null) {
													conditionPanel.setBreakText(pause.getInstructionText());
												}
												experimentPanel.showConditionPanel();
											}});						

										//Pause for the length of the break phase and show the countdown timer
										final long pauseLength = (pause.getLength_seconds()) * 1000;
										final long endTime = System.currentTimeMillis() + pauseLength;
										Runnable timerUpdate = new Runnable() {
											public void run() {
												long remainingTime = endTime - System.currentTimeMillis();
												if(remainingTime < 0) {
													remainingTime = 0;
												}
												conditionPanel.setBreakTimeRemaining(remainingTime);
											}
										};
										while(experimentRunning && System.currentTimeMillis() < endTime && !isCancelled()) {
											try {Thread.sleep(100);} catch(Exception ex) {}
											SwingUtilities.invokeLater(timerUpdate);								
										}							

										//Enable next button and wait for a next button press
										if(experimentRunning && !isCancelled()) {
											SwingUtilities.invokeLater(new Runnable() {
												public void run() {
													conditionPanel.setInstructionBannerVisible(true);
													conditionPanel.setBreakTimeRemaining(0);
													conditionPanel.setBreakOver(true);
													experimentPanel.setNavButtonEnabled(ButtonType.Next, true);
												}});
											nextButtonPressed = false;								
											while(!nextButtonPressed && experimentRunning && !isCancelled()) {try {Thread.sleep(50);} catch(Exception ex) {}}								
										}
									}
								}
							}							
							
							//Execute the mission
							if(experimentRunning && !isCancelled()) {
								//TODO: Pause before allowing next click
								skipNextPause = false;
								final boolean showMultiPageInstructions = currentCondition.isShowInstructionPage() &&
										currentCondition.getInstructionPages() != null && 
										currentCondition.getInstructionPages().size() > 1; 
								SwingUtilities.invokeLater(new Runnable() {
									public void run() {
										experimentPanel.setNavButtonEnabled(ButtonType.Next, true);
										experimentPanel.setReviewTutorialButtonVisible(false);
										if(showMultiPageInstructions) {
											experimentPanel.setTrialDescriptor("Page");
											experimentPanel.setConditionNumber(currentCondition.getName(),
													currentCondition.getConditionNum(), 
													currentCondition.getInstructionPages().size()+1);											
											experimentPanel.setTrialNumber(1);											
										} else {
											experimentPanel.setTrialDescriptor("Trial");
											experimentPanel.setConditionNumber(currentCondition.getName(),
													currentCondition.getConditionNum(), currentCondition.getNumTrials());											
										}
									}});								

								//First show any instructions for the task, or a screen with a link to the tutorial
								// (skip the instructions if going to a specific trial in the task)
								if(experimentRunning && !isCancelled() && currentTrial < 0 && currentCondition.isShowInstructionPage()) {									
									//First show the instruction pages if present
									long instructionsStartTime = System.currentTimeMillis();
									if(currentCondition.getInstructionPages() != null && !currentCondition.getInstructionPages().isEmpty()) {																				
										final ArrayList<InstructionsPage> pages = currentCondition.getInstructionPages(); 
										int pageIndex = 0;
										SwingUtilities.invokeLater(new Runnable() {
											public void run() {
												experimentPanel.showInternalInstructionPanel();
											}
										});
										while(pageIndex < (pages.size() + 1) && experimentRunning && !isCancelled()) {
											setNavButtonEnabled(ButtonType.Back, (pageIndex != 0));
											if(showMultiPageInstructions) {
												experimentPanel.setTrialNumber(pageIndex+1);
											}
											if(pageIndex == pages.size()) {
												//Show the last page with a link to open the tutorial or a default instructions page to begin the next task
												showExamPhaseInstructionsPage(currentCondition);
											} else {
												//Show the current instructions page
												showInstructionsPage(pages.get(pageIndex));
											}

											//Wait for the back or next button to be pressed
											backButtonPressed = false;
											nextButtonPressed = false;
											while((!backButtonPressed && !nextButtonPressed) && experimentRunning && !isCancelled()) {
												try {Thread.sleep(50);} catch(InterruptedException ex) {}
											}
											if(backButtonPressed) {
												pageIndex--;
											} else {
												pageIndex++;
											}	
										}
									} else {
										//Just show default instructions or a page with a link to open the tutorial										
										if(experimentRunning && !isCancelled()) { 
											showExamPhaseInstructionsPage(currentCondition);

											//Wait for the next button to be pressed
											nextButtonPressed = false;
											while(!nextButtonPressed && experimentRunning && !isCancelled()) {try {Thread.sleep(50);} catch(Exception ex) {}}
										}
									}			
									//Record the time spent reviewing the mission instructions
									currentCondition.setInstructionsTime_ms(System.currentTimeMillis() - instructionsStartTime);
								}
																
								if(experimentRunning && !isCancelled()) {
									//Start the current mission
									setNavButtonEnabled(ButtonType.Back, false);
									//if(showMultiPageInstructions) {
									SwingUtilities.invokeLater(new Runnable() {
										public void run() {
											experimentPanel.setTrialDescriptor("Trial");
											experimentPanel.setConditionNumber(currentCondition.getName(),
													currentCondition.getConditionNum(), currentCondition.getNumTrials());
											experimentPanel.setTrialNumber(1);
										}
									});
									//}
									if(currentConditionController != null) {
										experimentPanel.removeSubjectActionListener(currentConditionController);
										currentConditionController.removeConditionListener(IcarusExamController_Phase2.this);
									}							
									
									//Initialize the random number generator
									random = new Random(1L);

									try {
										if(currentCondition instanceof Mission_1_2_3) {
											mission123Controller.setRandom(random);
											mission123Controller.setExam(exam);
											mission123Controller.initializeConditionController((Mission_1_2_3)currentCondition, 
													conditionPanel, clearResponseData);
											currentConditionController = mission123Controller;
										} else if(currentCondition instanceof Mission_4_5_6) {								
											mission456Controller.setRandom(random);
											mission456Controller.setExam(exam);
											mission456Controller.initializeConditionController((Mission_4_5_6)currentCondition, 
													conditionPanel, clearResponseData);
											currentConditionController = mission456Controller;
										} else {
											currentConditionController = null;
										}
									} catch(Exception ex) {
										ex.printStackTrace();
										ErrorDlg.showErrorDialog(experimentPanel.getParentWindow(), ex, true);
									}

									if(currentConditionController != null && experimentRunning && !isCancelled()) {									
										conditionRunning = true;
										//Execute the mission controller in the event dispatching thread
										SwingUtilities.invokeLater(new Runnable() {
											public void run() {
												experimentPanel.setTrialNumber(1);
												experimentPanel.addSubjectActionListener(currentConditionController);																			
												try {
													currentConditionController.addConditionListener(IcarusExamController_Phase2.this);
													currentConditionController.startCondition(currentTrial, IcarusExamController_Phase2.this, null);
													conditionStartTime = System.currentTimeMillis();
													
													//Allow subject to review exam tutorial and the mission instructions and show the condition panel
													setExternalInstructionsVisible(false);
													experimentPanel.setReviewTutorialButtonVisible(exam.getTutorial() != null);
													experimentPanel.setNavButtonVisible(ButtonType.Help, 
															currentCondition.getInstructionPages() != null);
													experimentPanel.showConditionPanel();
												}
												catch (Exception ex) {
													ex.printStackTrace();
													ErrorDlg.showErrorDialog(experimentPanel.getParentWindow(), ex, true);
													try {
														currentConditionController.stopCondition();
													} catch(Exception e) {}
													conditionRunning = false;
												}
											}});											
									}
								}

								//Wait for the current mission to complete
								while(conditionRunning && experimentRunning && !isCancelled()) {try {Thread.sleep(50);} catch(Exception ex) {}}							

								setExternalInstructionsVisible(false);
								experimentPanel.removeSubjectActionListener(currentConditionController);
								if(currentConditionController != null) {
									currentConditionController.removeConditionListener(IcarusExamController_Phase2.this);
								}

								if(experimentRunning && !isCancelled() && currentConditionController != null) {						
									//Save mission response data
									//TODO: Show progress dialog									
									Mission<?> missionWithResponses = currentConditionController.getMissionWithUpdatedResponseData();																		
									missionWithResponses.setStartTime(new Date(conditionStartTime));
									missionWithResponses.setEndTime(new Date(System.currentTimeMillis()));
									missionWithResponses.setResponseGenerator(new ResponseGeneratorData(
											subjectData.getSite() != null ? subjectData.getSite().getTag() : null, 
											subjectData.getSubjectId(), true));
									boolean dataRecorded = true;
									if(experimentRunning && !isCancelled() && 
											missionWithResponses != null && dataRecorder != null && dataRecorder.isEnabled()) {										
										dataRecorded = false;
										boolean retry = false;
										do {											
											try {
												dataRecorder.recordExamPhaseData(subjectData, exam, missionWithResponses, 
														IcarusExamLoader_Phase2.examLoaderInstance);								
												dataRecorded = true;
											} catch (Exception e) {												
												//e.printStackTrace();
												ErrorDlg.showErrorDialog(experimentPanel.getParentWindow(), 
														new Exception("There was a problem recording your data", e), true);												
												retry = JOptionPane.showConfirmDialog(experimentPanel.getParentWindow(), 
															"<html>The network connection to the server may have been lost. Would you like to try to record your data again?<br>" +
																	"If you select No, the experiment will end and you may try continuing at a later time.</html>", "", 
																	JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
												}
										} while(experimentRunning && !isCancelled() && !dataRecorded && retry);
									}									
									if(!dataRecorded) {
										//Exit the experiment since data could not be recorded
										SwingUtilities.invokeLater(new Runnable() {
											public void run() {
												experimentPanel.setNavButtonEnabled(ButtonType.Back, false);
												experimentPanel.setNavButtonEnabled(ButtonType.Next, false);
												experimentPanel.setNavButtonVisible(ButtonType.Help, false);
												experimentPanel.showInternalInstructionPanel();
												experimentPanel.setInstructionText(
														InstructionsPanel.formatTextAsHTML("The experiment cannot continue since your data cannot be recorded. " +
																"You may exit the application now or return to the home screen."));	
											}
										});
										experimentRunning = false;
										exitedWorker = true;
										return null;
									}
									
									//Show a transition screen at the end of the mission and there is another mission
									if(experimentRunning && !isCancelled() && 
											(currentConditionIndex + 1) < exam.getConditions().size()) {
										SwingUtilities.invokeLater(new Runnable() {
											public void run() {
												experimentPanel.setNavButtonEnabled(ButtonType.Back, false);
												experimentPanel.setNavButtonEnabled(ButtonType.Next, true);
												experimentPanel.setReviewTutorialButtonVisible(false);
												experimentPanel.setNavButtonVisible(ButtonType.Help, false);
												experimentPanel.setInstructionText(
														InstructionsPanel.formatTextAsHTML("Mission " + Integer.toString(currentConditionIndex+1) + " is complete." +
																" Click <b>Next</b> to begin Mission " + Integer.toString(currentConditionIndex+2) + "."));										
												experimentPanel.showInternalInstructionPanel();
											}
										});										
										if(experimentRunning) {																					
											//Wait for the next button to be pressed
											nextButtonPressed = false;
											while(!nextButtonPressed && experimentRunning && !isCancelled()) {try {Thread.sleep(50);} catch(Exception ex) {}}
										}
									}
								}
							}
						}

						if(experimentRunning && !isCancelled()) {
							currentConditionIndex++;
						}
					} 

					if(experimentRunning && !isCancelled()) {
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								experimentPanel.setNavButtonEnabled(ButtonType.Back, false);
								experimentPanel.setNavButtonEnabled(ButtonType.Next, false);
								experimentPanel.setNavButtonEnabled(ButtonType.Help, false);
								experimentPanel.setReviewTutorialButtonEnabled(false);
								experimentPanel.showInternalInstructionPanel();
								experimentPanel.setInstructionText(
										InstructionsPanel.formatTextAsHTML("The experiment is complete.  Thank you for participating! " +
												"You may exit the application now or return to the home screen."));	
							}
						});
					}
					experimentRunning = false;
					exitedWorker = true;
					return null;
				} catch(Exception ex) {
					ex.printStackTrace();
					ErrorDlg.showErrorDialog(experimentPanel.getParentWindow(), 
							new Exception("An unexpected error occured.", ex), true);
					experimentRunning = false;
					exitedWorker = true;
					return null;
				}
			}
		};		
		
		experimentWorker.execute();
	}

	/**
	 * Get whether to clear response data at the start of each mission.
	 * 
	 * @return whether to clear response data at the start of each mission
	 */
	public boolean isClearResponseData() {
		return clearResponseData;
	}

	/**
	 * Set whether to clear response data at the start of each mission.
	 * 
	 * @param clearResponseData whether to clear response data at the start of each mission
	 */
	public void setClearResponseData(boolean clearResponseData) {
		this.clearResponseData = clearResponseData;
	}

	@Override
	public IcarusExamPhase getCurrentExamPhase() {
		return currentCondition;
	}
	
	@Override
	public List<? extends IcarusTestTrial> getCurrentTrials() {
		return currentCondition != null ? currentCondition.getTestTrials() : null;
	}

	@Override
	public void restartExperiment() {
		skipNextPause = false;
		stopExperiment();
		if(subjectData == null) {
			subjectData = new IcarusSubjectData(null, null, 0);
		} else {
			subjectData.setCurrentCondition(0);
		}
		examTutorialShown = false;		
		startExperiment(subjectData);
	}

	@Override
	public void stopExperiment() {
		experimentRunning = false;
		conditionRunning = false;
		currentConditionIndex = -1;
		
		try {
			if(tutorialController.isConditionRunning()) {
				tutorialController.stopCondition();
				experimentPanel.removeSubjectActionListener(tutorialController);
				tutorialController.removeConditionListener(this);
			} 
		} catch(Exception ex) {}

		try {
			if(currentConditionController != null) {
				currentConditionController.stopCondition();	
				experimentPanel.removeSubjectActionListener(currentConditionController);
				currentConditionController.removeConditionListener(this);
			}
		} catch(Exception ex) {}

		try {
			if(experimentWorker != null) {
				experimentWorker.cancel(true);
				experimentWorker = null;
			}
		} catch(Exception ex) {}
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment.IcarusExamController#skipToCondition(int)
	 */
	@Override
	public synchronized void skipToCondition(int conditionIndex) {
		//Restart the experiment at the current condition
		advanceToTrial(conditionIndex, -1, false);
		/*stopExperiment();
		if(subjectData == null) {
			//System.err.println("subject data was null!");
			subjectData = new IcarusSubjectData(null, null, conditionIndex);
		}		
		else {			
			subjectData.setCurrentCondition(conditionIndex);
		}
		skipNextPause = true;
		startExperiment(subjectData);*/
	}
	
	/**
	 * Go to a task.
	 * 
	 * @param taskId
	 */
	public void advanceToMission(String missionId) {
		advanceToTrial(getMissionIndex(missionId), -1, false);
	}
	
	/**
	 * Got to a trial in the current task.
	 * 
	 * @param trialNumber
	 */
	public void advanceToTrial(int trialNumber) {		
		advanceToTrial(currentConditionIndex, trialNumber, true);
	}
	
	/**
	 * Go to a trial in the given task.
	 * 
	 * @param taskId
	 * @param trialNumber
	 */
	public void advanceToTrial(String missionId, int trialNumber) {
		advanceToTrial(getMissionIndex(missionId), trialNumber, true);
	}
	
	/**
	 * Go to a trial in a task.
	 * 
	 * @param taskIndex
	 * @param trialNumber
	 * @param skipTaskInstructions
	 */
	protected void advanceToTrial(int missionIndex, int trialNumber, boolean skipTaskInstructions) {
		//Restart the experiment at the current condition
		if(exam != null && exam.getMissions() != null && missionIndex >=0 && 
				missionIndex < exam.getMissions().size()) {
			stopExperiment();
			if(subjectData == null) {
				subjectData = new IcarusSubjectData(null, null, missionIndex);
			} else {			
				subjectData.setCurrentCondition(missionIndex);
			}
			subjectData.setCurrentTrial(trialNumber);
			skipNextPause = true;
			startExperiment(subjectData);
		}		
	}
	
	protected int getMissionIndex(String missionId) {
		if(exam != null) {
			return exam.getMissionIndex(missionId);
		}
		return -1;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment.IcarusExamController#setExperimentPanel(org.mitre.icarus.cps.experiment_core.gui.IExperimentPanel)
	 */
	@Override
	public void setExperimentPanel(ExperimentPanel_Phase2 experimentPanel) {
		super.setExperimentPanel(experimentPanel);
		conditionPanel = this.experimentPanel.getConditionPanel();
		this.experimentPanel.removeSubjectActionListener(this);
		this.experimentPanel.addSubjectActionListener(this);
		this.experimentPanel.removeInstructionsHyperlinkListener(this);
		this.experimentPanel.addInstructionsHyperlinkListener(this);
	}	
	
	@Override
	public boolean isConditionRunning() {
		return conditionRunning && !tutorialController.isConditionRunning();
	}

	@Override
	public void conditionActionPerformed(ConditionEvent event) {
		if(event.eventType == ConditionEvent.TRIAL_CHANGED) {
			//Set the trial number in the status panel
			experimentPanel.setTrialNumber(event.trialNum+1, event.trialPartNum+1, event.numTrialParts);
		}	
		else if(event.eventType == ConditionEvent.CONDITION_COMPLETED) {			
			//The condition was completed			
			experimentPanel.setTrialNumber(-1, -1, -1);
			conditionRunning = false;			
		}
	}

	@Override
	public void subjectActionPerformed(SubjectActionEvent event) {
		if(event.eventType == SubjectActionEvent.BACK_BUTTON_PRESSED) {			
			backButtonPressed = true;			
		} else if(event.eventType == SubjectActionEvent.NEXT_BUTTON_PRESSED) {			
			nextButtonPressed = true;
		} else if(event.eventType == SubjectActionEvent.HELP_BUTTON_PRESSED) {
			//Show the tutorial for the current mission or a help page
			try {
				if(currentCondition.getInstructionPages() != null) {
					String pagesId = exam.getId() + "_" + currentCondition.getId();
					if(!pagesId.equals(experimentPanel.getInstructionsPagesId())) {
						experimentPanel.setInstructionsPages(currentCondition.getName() + " Instructions",
								pagesId, currentCondition.getInstructionPages());
					}
					experimentPanel.setExternalInstructionsVisible(true, "Mission Instructions",
							ImageManager_Phase2.getImage(ImageManager_Phase2.HELP_ICON));
				}
			} catch (Exception e) {
				e.printStackTrace();
				ErrorDlg.showErrorDialog(experimentPanel.getParentWindow(), 
						new Exception("The tutorial could not be opened", e), true);
			}
		} else if(event.eventType == SubjectActionEvent.CUSTOM_BUTTON_PRESSED) {
			//Show the exam tutorial
			try {
				if(exam.getTutorial() != null) {
					String pagesId = exam.getId();
					if(!pagesId.equals(experimentPanel.getInstructionsPagesId())) {
						experimentPanel.setInstructionsPages("Exam Tutorial", 
								pagesId, exam.getTutorial().getTutorialPages(),
								exam.getTutorial().getTutorialNavigationTree(), false);
					}
					experimentPanel.setExternalInstructionsVisible(true, "Exam Tutorial",
							ImageManager_Phase2.getImage(ImageManager_Phase2.HELP_ICON));
				}
			} catch (Exception e) {
				e.printStackTrace();
				ErrorDlg.showErrorDialog(experimentPanel.getParentWindow(), 
						new Exception("The tutorial could not be opened", e), true);
			}
		}
	}

	@Override
	public void hyperlinkUpdate(HyperlinkEvent e) {
		//Does nothing		
	}
}