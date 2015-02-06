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
package org.mitre.icarus.cps.app.experiment.phase_1.player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.mitre.icarus.cps.app.experiment.IcarusExperimentController;
import org.mitre.icarus.cps.app.experiment.IcarusSubjectData;
import org.mitre.icarus.cps.app.experiment.phase_1.TutorialController_Phase1;
import org.mitre.icarus.cps.app.widgets.basic.tutorial.TutorialPanel;
import org.mitre.icarus.cps.app.widgets.dialog.ErrorDlg;
import org.mitre.icarus.cps.app.widgets.dialog.SkipToPhaseDlg;
import org.mitre.icarus.cps.app.widgets.dialog.TrialSelectionDlg;
import org.mitre.icarus.cps.app.widgets.map.phase_1.MapPanelContainer;
import org.mitre.icarus.cps.app.widgets.phase_1.ImageManager_Phase1;
import org.mitre.icarus.cps.app.widgets.phase_1.player.ConditionPanel_Player;
import org.mitre.icarus.cps.app.widgets.phase_1.player.ExperimentPanel_Player;
import org.mitre.icarus.cps.app.widgets.phase_1.player.PlayerNavButtonPanel;
import org.mitre.icarus.cps.app.window.configuration.ApplicationConfiguration.ExamControlOption;
import org.mitre.icarus.cps.app.window.phase_1.PlayerUtils;
import org.mitre.icarus.cps.assessment.data_model.base.DataType;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.TaskMetrics;
import org.mitre.icarus.cps.exam.base.IcarusExamPhase;
import org.mitre.icarus.cps.exam.base.loader.IcarusExamLoader;
import org.mitre.icarus.cps.exam.base.playback.ExamPlaybackChangeListener;
import org.mitre.icarus.cps.exam.base.testing.IcarusTestTrial;
import org.mitre.icarus.cps.exam.phase_1.IcarusExam_Phase1;
import org.mitre.icarus.cps.exam.phase_1.playback.ExamPlaybackDataSource_Phase1;
import org.mitre.icarus.cps.exam.phase_1.playback.TaskResponseData_Phase1;
import org.mitre.icarus.cps.exam.phase_1.testing.TaskTestPhase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_1_2_3_PhaseBase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_4_5_6_PhaseBase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_4_Phase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_5_Phase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_6_Phase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_7_Phase;
import org.mitre.icarus.cps.exam.phase_1.training.ProbabilityRulesPage;
import org.mitre.icarus.cps.experiment_core.condition.InstructionsPage;
import org.mitre.icarus.cps.experiment_core.event.ConditionEvent;
import org.mitre.icarus.cps.experiment_core.event.SubjectActionEvent;
import org.mitre.icarus.cps.experiment_core.gui.InstructionsPanel;
import org.mitre.icarus.cps.experiment_core.gui.INavButtonPanel.ButtonType;
import org.mitre.icarus.cps.experiment_core.subject_data.SubjectData;
import org.mitre.icarus.cps.feature_vector.phase_1.IntType;

/**
 * Main controller for the ICArUS T&E Player for Phase 1.
 * 
 * @author CBONACETO
 *
 */
public class PlayerController extends 
	IcarusExperimentController<ExamPlaybackDataSource_Phase1, TaskResponseData_Phase1<?>,
		ExperimentPanel_Player,	ConditionPanel_Player> implements HyperlinkListener, ExamPlaybackChangeListener { 
			
	/** The tutorial panel where exam tutorial pages are shown */
	protected TutorialPanel tutorialPanel;
			
	/** The exam response data for the participant and possibly the average human */
	protected ExamPlaybackDataSource_Phase1 examResponse;
	
	/** The exam that the response data is for */
	protected IcarusExam_Phase1 exam;
	
	/** The current task data for the current task being executed */
	protected TaskTestPhase<?> currentTask;	
	
	/** The current task being executed */
	protected TaskResponseData_Phase1<? extends TaskTestPhase<?>> currentTaskResponse;
	
	/** The tasks whose feature vector data has been initialized */
	protected Set<TaskTestPhase<?>> initializedTasks;
	
	/** Whether the exam tutorial has been shown */
	protected boolean examTutorialShown = false;	
	
	/** Whether to show tutorials when playing back the exam (default is false) */
	protected boolean showTutorials = false;
	
	/** Whether to display warnings when a trial isn't complete because data is missing. */
	protected boolean showIncompleteTrialWarnings;
	
	/** Whether to skip the next pause (if any) */
	protected boolean skipNextPause = false;
	
	/** Tutorial controller */
	protected TutorialController_Phase1<PlayerController, ExamPlaybackDataSource_Phase1> tutorialController;
	
	 /** The current task controller */
	protected PlayerTaskController<?> currentTaskController;	

	/** Controller for tasks 1-3 */
	protected Task_1_2_3_PlayerController task123Controller;
	
	/** Controller for task 4 */
	protected Task_4_PlayerController task4Controller;
	
	/** Controller for tasks 5-6 */
	protected Task_5_6_PlayerController task56Controller;
	
	/** HUMINT rules for Tasks 4-7 */
	protected ProbabilityRulesPage humintRules;
	
	/** WAVEINT rules for Task 7 */
	protected ProbabilityRulesPage waveintRules;
	
	protected boolean advancedFromPreviousTask;
	
	protected boolean advancedToTrial;	
	
	/**
	 * Default constructor.
	 */
	public PlayerController() {
		this(false);
	}	
	
	/**
	 * @param showTutorials
	 */
	public PlayerController(boolean showTutorials) {		
		this.showTutorials = showTutorials;
		this.tutorialPanel = new TutorialPanel();
		tutorialController = new TutorialController_Phase1<PlayerController, ExamPlaybackDataSource_Phase1>();
		task123Controller = new Task_1_2_3_PlayerController();
		task4Controller = new Task_4_PlayerController();
		task56Controller = new Task_5_6_PlayerController();
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment_core.controller.IExperimentController#initializeExperimentController(org.mitre.icarus.cps.experiment_core.Experiment)
	 */
	@Override
	public synchronized void initializeExperimentController(ExamPlaybackDataSource_Phase1 experiment) {		
		skipNextPause = false;
		examTutorialShown = false;
		
		//Stop the currently executing exam
		stopExperiment();		
		if(examResponse != null) {
			examResponse.removeExamPlaybackChangeListener(this);
		}
				
		super.exam = experiment;
		this.examResponse = experiment;
		this.examResponse.addExamPlaybackChangeListener(this);
		this.exam = examResponse.getExam();		
		//examResponse = experiment;
		//examResponse.addExamPlaybackChangeListener(this);
		//exam = examResponse.getExam();

		//Initialize the experiment panel			
		experimentPanel.setExperimentName(exam.getName());
		experimentPanel.removeAllInstructionPages();
		MapPanelContainer mapPanel = experimentPanel.getConditionPanel().getMapPanel();
		mapPanel.setGridSize(exam.getGridSize());
		mapPanel.setSigactLayerInstructions(null, null);
		mapPanel.setGroupCenterstLayerInstructions(null, null);
		mapPanel.setImintLayerInstructions(null, null);
		mapPanel.setMovintLayerInstructions(null, null);
		mapPanel.setSigintLayerInstructions(null, null);
		mapPanel.setSocintRegionsLayerInstructions(null, null);
		//Initialize links to probability rules pages if present		
		if(exam.getProbabilityInstructions() != null && exam.getProbabilityInstructions().getProbabilityRulesPages() != null) {
			initializeInstructionPages(exam.getProbabilityInstructions().getProbabilityRulesPages());
			for(ProbabilityRulesPage rules : exam.getProbabilityInstructions().getProbabilityRulesPages()) {
				switch(rules.getIntType()) {
				case HUMINT:
					humintRules = rules;
					mapPanel.setGroupCenterstLayerInstructions(rules, "PROBS");
					break;
				case IMINT:
					mapPanel.setImintLayerInstructions(rules, "PROBS");
					break;
				case MOVINT:
					mapPanel.setMovintLayerInstructions(rules, "PROBS");
					break;
				case SIGINT:
					mapPanel.setSigintLayerInstructions(rules, "PROBS");
					break;
				case SOCINT:
					mapPanel.setSocintRegionsLayerInstructions(rules, "PROBS");
					break;
				case WAVEINT:
					//Only show the WAVEINT rules when Task 7 is being shown
					waveintRules = rules;					
					break;
				}
			}
		}		
		
		if(examResponse.getConditions() != null) {
			//Number the phases (some phases may not be counted)
			initializedTasks = new HashSet<TaskTestPhase<?>>(examResponse.getTestPhaseResponses().size());			
			int currPhase = 1;
			for(TaskResponseData_Phase1<? extends TaskTestPhase<?>> phase : examResponse.getTestPhaseResponses()) {
				if(phase.getTestPhase() instanceof Task_7_Phase || !phase.isCountCondition()) {
					phase.setConditionNum(-1);
				}
				else {
					phase.setConditionNum(currPhase);
					currPhase++;
				}
			}
			experimentPanel.setNumConditions(currPhase-1);
		}
		else {
			initializedTasks = new HashSet<TaskTestPhase<?>>();
			experimentPanel.setNumConditions(0);
		}
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment_core.controller.IExperimentController#startExperiment(org.mitre.icarus.cps.experiment_core.subject_data.SubjectData)
	 */
	@Override
	public synchronized void startExperiment(SubjectData sData) {
		if(examResponse == null || experimentPanel == null) {
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
					if(currentTaskController != null) {
						experimentPanel.removeSubjectActionListener(currentTaskController);
						currentTaskController.removeConditionListener(PlayerController.this);
						currentTaskController.stopCondition();
					}
				} catch(Exception ex) {}
			}

			@SuppressWarnings({ "unchecked"})
			@Override
			protected Object doInBackground() {
				try {
					/** Wait until the previously executing experiment worker finishes executing (waits up to 5 seconds) */
					long exitTime = System.currentTimeMillis() + 5000;
					while(!exitedWorker && !isCancelled()) {					
						try {
							Thread.sleep(25);
						} catch(InterruptedException ex) {}
						if(System.currentTimeMillis() > exitTime) {
							//Show an error dialog and exit
							ErrorDlg.showErrorDialog(experimentPanel.getParentWindow(), new Exception("Error, could not end currently executing task."), false);
							exitedWorker = true;
							experimentRunning = false;
							return null;
						}
					}					

					experimentRunning = true;		
					exitedWorker = false;

					if(subjectData != null) {
						experimentPanel.setSubject(subjectData.toString());
						currentConditionIndex = subjectData.getCurrentCondition();
						currentTrial = subjectData.getCurrentTrial();
						if(currentConditionIndex < 0) { currentConditionIndex = 0; }
					} else {
						currentConditionIndex = 0;
						currentTrial = -1;
					}
					boolean advanceToPreviousTask = false;
					boolean showingAssessmentResults = false;
					advancedFromPreviousTask = false;
					currentTaskResponse = null;

					while(experimentRunning && !isCancelled()) { //&& currentConditionIndex < examResponse.getTaskResponses().size()	
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								conditionPanel.getMapPanel().setLayerInstructionsWindowVisible(false);
								experimentPanel.clearCurrentConfiguration();
							}});

						if(experimentRunning && currentConditionIndex >= 0 && 
								!(examResponse.getTestPhaseResponses().get(currentConditionIndex).getTestPhase() instanceof Task_7_Phase)) {
							conditionRunning = false;

							SwingUtilities.invokeLater(new Runnable() {
								public void run() {
									experimentPanel.setNavButtonEnabled(ButtonType.Back, false);
									experimentPanel.setNavButtonEnabled(ButtonType.Next, false);
									experimentPanel.setSkipToTrialButtonEnabled(false);
									experimentPanel.setSkipToMissionButtonEnabled(true);
									experimentPanel.setNavButtonVisible(ButtonType.Help, false);
									experimentPanel.setReviewTutorialButtonVisible(false);
								}});

							if(showTutorials && currentConditionIndex == 0 && exam.getTutorialUrl() != null && !examTutorialShown) {
								//Show page with link to open the tutorial for the exam and wait for a next button press							
								if(experimentRunning) {
									examTutorialShown = true;
									SwingUtilities.invokeLater(new Runnable() {
										public void run() {
											experimentPanel.setConditionNumber("Tutorial", -1, -1);
											experimentPanel.setInstructionText(
													InstructionsPanel.formatTextAsHTML(
															"<a href=\"open_exam_tutorial\">Click here</a> to open the exam tutorial.<br>" +
																	"You may review the exam tutorial at any time by clicking the Review Exam Tutorial button." +
															"<br><br>Click Next when you are ready to begin the exam."));
											experimentPanel.showInternalInstructionPanel();
											experimentPanel.setNavButtonEnabled(ButtonType.Next, true);									
										}});
									nextButtonPressed = false;
									while(!nextButtonPressed && experimentRunning && !isCancelled()) {try {Thread.sleep(25);} catch(Exception ex) {}}
								}
							}
							else if(showTutorials && currentConditionIndex == 0 && exam.getTutorial() != null && !examTutorialShown) {
								//Show the tutorial pages for the exam
								examTutorialShown = true;
								SwingUtilities.invokeLater(new Runnable() {
									public void run() {
										setNavButtonEnabled(ButtonType.Next, false);
										setNavButtonEnabled(ButtonType.Back, false);									
										experimentPanel.showContent(tutorialPanel);
										//experimentPanel.setConditionNumber("Tutorial", -1, -1);
										experimentPanel.setTrialDescriptor("Page");
										experimentPanel.setConditionNumber("Exam Tutorial", -1, exam.getTutorial().getNumTrials());
										experimentPanel.setTrialNumber(1);
									}
								});
								initializeInstructionPages(exam.getTutorial().getTutorialPages());
								final int currPage;
								if(advanceToPreviousTask) {
									//Go to the last page in the tutorial if coming from the previous task
									currPage = exam.getTutorial().getNumTrials() - 1;									
								} else {
									currPage = 0;
								}
								advanceToPreviousTask = false;
								tutorialController.setExam(examResponse);
								tutorialController.initializeConditionController(exam.getTutorial(), tutorialPanel);
								if(experimentRunning && !isCancelled()) {								
									conditionRunning = true;									
									//Execute tutorial controller in the event dispatching thread
									SwingUtilities.invokeLater(new Runnable() {
										public void run() {
											experimentPanel.addSubjectActionListener(tutorialController);																			
											try {
												experimentPanel.setTrialNumber(currPage + 1);
												tutorialController.addConditionListener(PlayerController.this);
												tutorialController.startCondition(currPage, PlayerController.this, null);
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
								while(conditionRunning && experimentRunning && !isCancelled()) {try {Thread.sleep(25);} catch(Exception ex) {}}
								experimentPanel.removeSubjectActionListener(tutorialController);
								tutorialController.removeConditionListener(PlayerController.this);
								SwingUtilities.invokeLater(new Runnable() {
									public void run() {
										experimentPanel.setNavButtonEnabled(ButtonType.Back, false);
										experimentPanel.setNavButtonEnabled(ButtonType.Next, false);
									}});
							}

							if(!experimentRunning || isCancelled()) {
								experimentRunning = false;
								exitedWorker = true;
								return null;
							}

							try {
								currentTaskResponse = examResponse.getTestPhaseResponses().get(currentConditionIndex);
								currentTask = currentTaskResponse.getTestPhase();
							} catch(Exception ex) {}
							if(currentTaskResponse != null) {
								if(advanceToPreviousTask) {
									//Go to the last trial in the current task when advancing from the previous task
									currentTrial = (currentTask instanceof Task_1_2_3_PhaseBase) ? 
											((Task_1_2_3_PhaseBase<?>)currentTask).getNumTrialBlocks() - 1 :
												currentTask.getNumTrials() - 1;
								}
								boolean advanceToLastInstructionsPage = currentTrial < 0;

								//Load the feature vector data and any instructions pages for the task if it hasn't been loaded yet
								if(!initializedTasks.contains(currentTask)) {
									try {										
										if(currentTask.getInstructionPages() != null && !currentTask.getInstructionPages().isEmpty()) {
											initializeInstructionPages(currentTask.getInstructionPages());
										}
										initializedTasks.add(currentTask);									
									} catch(Exception ex) {
										ex.printStackTrace();
										ErrorDlg.showErrorDialog(experimentPanel.getParentWindow(), ex, true);
									}
								}
								
								boolean advanceToNextTask = false;
								if(advanceToPreviousTask && !showingAssessmentResults) {
									//First show a page with the overall task assessment results when advancing to the previous task
									if(experimentRunning && !isCancelled()) {
										//Show overall assessment metrics for the task	
										displayTaskAssessmentMetrics();

										if(experimentRunning) {																					
											//Wait for the back or next button to be pressed
											backButtonPressed = false;
											nextButtonPressed = false;
											while(!backButtonPressed && !nextButtonPressed && experimentRunning && !isCancelled()) {try {Thread.sleep(25);} catch(Exception ex) {}}
											advanceToNextTask = nextButtonPressed;
										}							
									}		
								}
								
								//Execute the task
								advanceToPreviousTask = false;
								showingAssessmentResults = false;
								if(!advanceToNextTask && experimentRunning && !isCancelled()) {
									final boolean showMultiPageInstructions = showTutorials && 
											currentTask.isShowInstructionPage() &&
											currentTask.getInstructionPages() != null && 
											currentTask.getInstructionPages().size() > 1; 
									SwingUtilities.invokeLater(new Runnable() {
										public void run() {
											experimentPanel.setNavButtonEnabled(ButtonType.Next, true);
											experimentPanel.setReviewTutorialButtonVisible(false);
											if(showMultiPageInstructions) {
												experimentPanel.setTrialDescriptor("Page");
												experimentPanel.setConditionNumber(currentTask.getName(),
														currentTask.getConditionNum(),
														currentTask.getInstructionPages().size()+1);											
												experimentPanel.setTrialNumber(1);
												
											} else {
												experimentPanel.setTrialDescriptor("Trial");
												experimentPanel.setConditionNumber(currentTask.getName(),
														currentTask.getConditionNum(), currentTask.getNumTrials());											
											}
										}});								

									//First show any instructions for the task, or a screen with a link to the tutorial
									// (skip the instructions if going to a specific trial in the task)
									if(experimentRunning && !isCancelled() && currentTrial < 0 && currentTask.isShowInstructionPage()) {									
										//First show the instruction pages if present
										if(showTutorials && currentTask.getInstructionPages() != null && !currentTask.getInstructionPages().isEmpty()) {									
											ArrayList<InstructionsPage> pages = currentTask.getInstructionPages(); 
											int pageIndex = 0;
											if(advanceToLastInstructionsPage) {
												pageIndex = pages.size() - 1;
											}
											SwingUtilities.invokeLater(new Runnable() {
												public void run() {
													experimentPanel.showInternalInstructionPanel();
												}
											});
											while(!advanceToPreviousTask && pageIndex < (pages.size() + 1) && experimentRunning && !isCancelled()) {											 
												setNavButtonEnabled(ButtonType.Back, currentConditionIndex > 0 || (pageIndex != 0));
												if(showMultiPageInstructions) {												
													experimentPanel.setTrialNumber(pageIndex+1);
												}
												if(pageIndex == pages.size()) {
													//Show the last page with a link to open the tutorial or a default instructions page to begin the next task
													showTaskInstructionsPage(currentTask);
												} else {
													//Show the current instructions page
													showInstructionsPage(pages.get(pageIndex));
												}

												//Wait for the back or next button to be pressed
												backButtonPressed = false;
												nextButtonPressed = false;
												while((!backButtonPressed && !nextButtonPressed) && experimentRunning && !isCancelled()) {
													try {Thread.sleep(25);} catch(InterruptedException ex) {}
												}
												if(backButtonPressed) {
													pageIndex--;
													if(pageIndex < 0) {
														advanceToPreviousTask = true;
													}
												} else {
													pageIndex++;
												}	
											}
										} else {
											//Just show default instructions or a page with a link to open the tutorial
											setNavButtonEnabled(ButtonType.Back, currentConditionIndex > 0);
											if(experimentRunning && !isCancelled()) { 
												showTaskInstructionsPage(currentTask);

												//Wait for the back or next button to be pressed
												backButtonPressed = false;
												nextButtonPressed = false;
												while((!backButtonPressed && !nextButtonPressed) && experimentRunning && !isCancelled()) {
													try {Thread.sleep(25);} catch(InterruptedException ex) {}
												}												
												if(backButtonPressed) {
													advanceToPreviousTask = true;
												}												
											}
										}			
									}

									if(!advanceToPreviousTask && experimentRunning && !isCancelled()) {
										//Start the current task
										//if(showMultiPageInstructions) {
										SwingUtilities.invokeLater(new Runnable() {
											public void run() {
												experimentPanel.setTrialDescriptor("Trial");
												experimentPanel.setConditionNumber(currentTask.getName(),
														currentTask.getConditionNum(), currentTask.getNumTrials());
												experimentPanel.setTrialNumber(1);
											}
										});
										//}
										if(currentTaskController != null) {
											experimentPanel.removeSubjectActionListener(currentTaskController);
											currentTaskController.removeConditionListener(PlayerController.this);
										}														

										try {
											//Initialize the appropriate task controller
											if(waveintRules != null) {
												experimentPanel.getConditionPanel().getMapPanel().removeInstructionsPage(
														IntType.WAVEINT.toString());
											}
											if(currentTask instanceof Task_1_2_3_PhaseBase) {
												task123Controller.setExam(examResponse);
												task123Controller.initializeConditionController((TaskResponseData_Phase1<Task_1_2_3_PhaseBase<?>>)currentTaskResponse, conditionPanel);
												currentTaskController = task123Controller;
												SwingUtilities.invokeLater(new Runnable() {
													public void run() {
														experimentPanel.getConditionPanel().getMapPanel().setGroupCenterstLayerInstructions(humintRules, "");
													}});
											} else {
												if(humintRules != null) {
													SwingUtilities.invokeLater(new Runnable() {
														public void run() {
															experimentPanel.getConditionPanel().getMapPanel().setGroupCenterstLayerInstructions(humintRules, "PROBS");
														}});
												} 
												if(currentTask instanceof Task_4_Phase) {
													task4Controller.setExam(examResponse);
													task4Controller.initializeConditionController((TaskResponseData_Phase1<Task_4_Phase>)currentTaskResponse, conditionPanel);
													currentTaskController = task4Controller;
												} else if(currentTask instanceof Task_5_Phase || currentTask instanceof Task_6_Phase) {
													task56Controller.setExam(examResponse);
													task56Controller.initializeConditionController((TaskResponseData_Phase1<Task_4_5_6_PhaseBase<?>>)currentTaskResponse, conditionPanel);
													currentTaskController = task56Controller;
												} else {
													currentTaskController = null;
												}
											}
										} catch(Exception ex) {
											ex.printStackTrace();
											ErrorDlg.showErrorDialog(experimentPanel.getParentWindow(), ex, true);
										}

										if(currentTaskController != null && experimentRunning && !isCancelled()) {
											conditionRunning = true;
											currentTaskController.setShowIncompleteTrialWarnings(showIncompleteTrialWarnings);											
											//Execute task controller in the event dispatching thread
											SwingUtilities.invokeLater(new Runnable() {
												public void run() {
													experimentPanel.setSkipToTrialButtonEnabled(true);
													experimentPanel.setTrialNumber(1);
													experimentPanel.addSubjectActionListener(currentTaskController);																		
													try {														
														currentTaskController.addConditionListener(PlayerController.this);
														currentTaskController.startCondition(currentTrial, advancedFromPreviousTask, 
																PlayerController.this, null,
																getParticipantLabel(examResponse.getParticipantDataType()));														
													} catch (Exception ex) {
														ex.printStackTrace();
														ErrorDlg.showErrorDialog(experimentPanel.getParentWindow(), ex, true);
														try {
															currentTaskController.stopCondition();
														} catch(Exception e) {}
														conditionRunning = false;
													}
												}});

											SwingUtilities.invokeLater(new Runnable() {
												public void run() {												
													//Allow subject to review the task instructions or tutorial and show the condition panel
													setExternalInstructionsVisible(false);
													experimentPanel.setNavButtonVisible(ButtonType.Help, 
															(currentTask.getTutorialUrl() != null || currentTask.getInstructionPages() != null));
													experimentPanel.showConditionPanel();												
												}});	
										}
									}

									//Wait for the current task to complete
									while(conditionRunning && experimentRunning && !isCancelled()) {try {Thread.sleep(25);} catch(Exception ex) {}}									
									
									setExternalInstructionsVisible(false);
									experimentPanel.removeSubjectActionListener(currentTaskController);
									if(currentTaskController != null) {
										currentTaskController.removeConditionListener(PlayerController.this);
									}									
									
									if(experimentRunning && !isCancelled() && currentTaskController != null && currentTrial > 0 && currentTask.isShowScore()) {
										//Show overall assessment metrics for the task	
										showingAssessmentResults = true;
										displayTaskAssessmentMetrics();

										if(experimentRunning) {
											//Wait for the back or next button to be pressed
											backButtonPressed = false;
											nextButtonPressed = false;
											while(!backButtonPressed && !nextButtonPressed && experimentRunning && !isCancelled()) {try {Thread.sleep(25);} catch(Exception ex) {}}
											advanceToPreviousTask = backButtonPressed;	
											if(advanceToPreviousTask) {
												currentConditionIndex++;
											}
										}							
									}							
								}
							}
						}

						if(experimentRunning && !isCancelled()) {
							if(advanceToPreviousTask || currentTrial < 0) {
								//Go to the previous condition or the current condition instructions/tutorial
								if(advanceToPreviousTask || !currentTask.isShowInstructionPage()) {
									//Go to the last part of the previous condition
									advanceToPreviousTask = true;
									advancedFromPreviousTask = true;
									currentConditionIndex--;
									if(currentConditionIndex < 0) currentConditionIndex = 0;
								}								
							} else {
								//Go to the next condition
								advanceToPreviousTask = false;
								advancedFromPreviousTask = false;
								currentConditionIndex++;
								currentTrial = 0;
							}
							
							if(currentConditionIndex >= examResponse.getTestPhaseResponses().size()) {
								showingAssessmentResults = false;
								if(experimentRunning && !isCancelled()) {
									SwingUtilities.invokeLater(new Runnable() {
										public void run() {
											experimentPanel.setNavButtonEnabled(ButtonType.Back, true);
											experimentPanel.setNavButtonEnabled(ButtonType.Next, false);
											experimentPanel.showInternalInstructionPanel();
											experimentPanel.setInstructionText(
													InstructionsPanel.formatTextAsHTML("The exam is complete. You may navigate back to a previous trial or mission, load another exam, or close the player."));	
										}
									});
									//Wait for a back button press, or for the user to skip to another mission
									backButtonPressed = false;
									while(!backButtonPressed && experimentRunning && !isCancelled()) {
										try {Thread.sleep(25);} catch(InterruptedException ex) {}
									}	
									if(backButtonPressed) {
										//Go to the last part of the previous condition
										advanceToPreviousTask = true;
										advancedFromPreviousTask = true;
										currentConditionIndex--;
										if(currentConditionIndex < 0) currentConditionIndex = 0;
									}
								}
							}
						}
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
	
	/** Display overall assessment metrics at the end of a Task. */
	private void displayTaskAssessmentMetrics() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				StringBuilder sb = createTaskAssessmentText(currentTaskResponse, 
						getParticipantLabel(examResponse.getParticipantDataType()));
				sb.append("<br>Click Next to continue.");
				experimentPanel.setInstructionText(InstructionsPanel.formatTextAsHTML(sb.toString()));
				experimentPanel.showInternalInstructionPanel();
				experimentPanel.setNavButtonEnabled(ButtonType.Back, true);
				experimentPanel.setNavButtonEnabled(ButtonType.Next, true);
			}
		});
	}
	
	/** Create a paragraph summarizing the overall assessment results for the task */
	protected StringBuilder createTaskAssessmentText(TaskResponseData_Phase1<? extends TaskTestPhase<?>> taskResponse, String participantLabel) {
		StringBuilder sb = new StringBuilder("<b>" + taskResponse.getName() + " </b>is now complete.<br>");
		sb.append("On average, over all trials of<b> " + taskResponse.getName() + "</b>, the " + 
				participantLabel + " scored as follows:<br><br>");
		
		TaskMetrics metrics = taskResponse.getParticipantTaskMetrics();
		if(metrics != null) {
			int taskNum = taskResponse.getConditionNum();
			if(taskResponse.getMetricsInfo().getRR_info().isAssessedForTask(taskNum) && metrics.getRR_avg_score() != null) {
				sb.append("Representativeness (RR): " + PlayerUtils.formatDoubleAsString(metrics.getRR_avg_score()) + "%<br>");
			}
			if(taskResponse.getMetricsInfo().getAI_info().isAssessedForTask(taskNum) && metrics.getAI_avg_score() != null) {
				sb.append("Anchoring & Adjustment (AA): " + PlayerUtils.formatDoubleAsString(metrics.getAI_avg_score()) + "%<br>");
			}
			if(taskResponse.getMetricsInfo().getPM_RMS_info().isAssessedForTask(taskNum) && metrics.getPM_RMS_avg_score() != null) {
				sb.append("Probability Matching (PM): " + PlayerUtils.formatDoubleAsString(metrics.getPM_RMS_avg_score()) + "%<br>");
			}
			if(taskResponse.getMetricsInfo().getCS_info().isAssessedForTask(taskNum) && metrics.getC_all_trials() != null) {
				sb.append("C (frequency with which SIGINT selected on group with highest probability): " + 
						PlayerUtils.formatDoubleAsString(metrics.getC_all_trials()) + "%<br>");
			}
			if(taskResponse.getMetricsInfo().getRSR_info().isAssessedForTask(taskNum) && metrics.getRSR_stage_avg() != null &&
					!metrics.getRSR_stage_avg().isEmpty()) {
				List<Double> rsrVals = metrics.getRSR_stage_avg();
				if(rsrVals.size() == 1) {
					sb.append("Relative Success Rate (RSR): " + 
							PlayerUtils.formatDoubleAsString(rsrVals.get(0)) + "%<br>");	
				} else {
					sb.append("Relative Success Rate (RSR):<br>");
					if(metrics.getRSR_avg() != null) {
						sb.append("All Stages: " + PlayerUtils.formatDoubleAsString(metrics.getRSR_avg()) + "<br>");
					}
					Integer stageNum = 1;
					for(Double rsr : rsrVals) {
						if(rsr != null) {
							sb.append("Stage " + stageNum.toString() + ": " + PlayerUtils.formatDoubleAsString(rsr) + "<br>");
						}
						stageNum++;
					}
				}
			}
			if(taskResponse.getMetricsInfo().getRMR_info().isAssessedForTask(taskNum) && metrics.getRMR_avg() != null) {
				sb.append("Relative Match Rate (RMR): " + 
						PlayerUtils.formatDoubleAsString(metrics.getRMR_avg()) + "%<br>");
			}
		}		
		return sb;
	}
	
	/**
	 * @param participantType
	 * @return
	 */
	protected String getParticipantLabel(DataType participantType) {
		if(participantType != null) {
			return participantType == DataType.Model_Single || participantType == DataType.Model_Avg || participantType == DataType.Model_Multiple ?
					"model" : "subject"; 
		}
		return "participant";
	}
	
	
	@Override
	public IcarusExamPhase getCurrentExamPhase() {
		if(currentTaskResponse != null) {
			return currentTaskResponse.getTestPhase();
		}
		return null;
	}	

	@Override
	public List<? extends IcarusTestTrial> getCurrentTrials() {		
		if(currentTaskResponse != null) {
			return currentTaskResponse.getTestPhase() instanceof Task_1_2_3_PhaseBase ?
					((Task_1_2_3_PhaseBase<?>)currentTaskResponse.getTestPhase()).getProbeTrials() :
						currentTaskResponse.getTestPhase().getTestTrials();
		}
		return null;
	}

	public synchronized void restartExperiment() {
		skipNextPause = false;
		stopExperiment();
		if(subjectData == null) {
			subjectData = new IcarusSubjectData(null, null, 0);
		}		
		else {
			subjectData.setCurrentCondition(0);
		}
		examTutorialShown = false;
		startExperiment(subjectData);
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment_core.controller.IExperimentController#stopExperiment()
	 */
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
			if(currentTaskController != null) {
				currentTaskController.stopCondition();	
				experimentPanel.removeSubjectActionListener(currentTaskController);
				currentTaskController.removeConditionListener(this);
			}
		} catch(Exception ex) {}

		try {
			if(experimentWorker != null) {
				experimentWorker.cancel(true);
				experimentWorker = null;
			}
		} catch(Exception ex) {}
	}
	
	public boolean isShowTutorials() {
		return showTutorials;
	}

	public void setShowTutorials(boolean showTutorials) {
		this.showTutorials = showTutorials;
	}

	public boolean isShowIncompleteTrialWarnings() {
		return showIncompleteTrialWarnings;
	}

	public void setShowIncompleteTrialWarnings(boolean showIncompleteTrialWarnings) {
		this.showIncompleteTrialWarnings = showIncompleteTrialWarnings;
		if(currentTaskController != null) {
			currentTaskController.setShowIncompleteTrialWarnings(showIncompleteTrialWarnings);
		}
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment.IcarusExamController#skipToCondition(int)
	 */
	@Override
	public synchronized void skipToCondition(int conditionIndex) {
		advanceToTrial(conditionIndex, -1, false);
	}
	
	/**
	 * Go to a task.
	 * 
	 * @param taskId
	 */
	public void advanceToTask(String taskId) {
		advanceToTrial(getTaskIndex(taskId), -1, false);
	}
	
	/**
	 * Got to a trial in the current task.
	 * 
	 * @param trialNumber
	 */
	public void advanceToTrial(int trialNumber) {
		//if(currentConditionIndex > 0) {
		advanceToTrial(currentConditionIndex, trialNumber, true);
		//}
	}
	
	/**
	 * Go to a trial in the given task.
	 * 
	 * @param taskId
	 * @param trialNumber
	 */
	public void advanceToTrial(String taskId, int trialNumber) {
		advanceToTrial(getTaskIndex(taskId), trialNumber, true);
	}
	
	/**
	 * Go to a trial in a task.
	 * 
	 * @param taskIndex
	 * @param trialNumber
	 * @param skipTaskInstructions
	 */
	protected void advanceToTrial(int taskIndex, int trialNumber, boolean skipTaskInstructions) {
		//Restart the experiment at the current condition
		if(exam != null && exam.getTasks() != null && taskIndex >=0 && taskIndex < exam.getTasks().size()) {
			stopExperiment();
			if(subjectData == null) {
				subjectData = new IcarusSubjectData(null, null, taskIndex);
			} else {			
				subjectData.setCurrentCondition(taskIndex);
			}
			subjectData.setCurrentTrial(trialNumber);
			skipNextPause = true;
			startExperiment(subjectData);
		}
	}	
	
	/**
	 * Advance to the next stage in a trial or to the next trial or task.
	 */
	public void advanceToNextStage() {
		if(experimentRunning) {		
			if(conditionRunning && currentTaskController != null) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						currentTaskController.subjectActionPerformed(
								new SubjectActionEvent(SubjectActionEvent.NEXT_BUTTON_PRESSED, this));
					}
				});				
			} else {				
				nextButtonPressed = true;
			}
		}		
	}	
	
	/**
	 * Advance to the previous stage in a trial or to the previous trial or task.
	 */
	public void advanceToPreviousStage() {		
		if(experimentRunning) {
			if(conditionRunning && currentTaskController != null) {		
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						currentTaskController.subjectActionPerformed(
								new SubjectActionEvent(SubjectActionEvent.BACK_BUTTON_PRESSED, this));
					}});
			} else {
				backButtonPressed = true;
			}
		}
	}	
	
	protected int getTaskIndex(String taskId) {
		if(examResponse != null) {
			return examResponse.getTestPhaseIndex(taskId);
		}
		return -1;
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment.IcarusExamController#setExperimentPanel(org.mitre.icarus.cps.experiment_core.gui.IExperimentPanel)
	 */
	@Override
	public void setExperimentPanel(ExperimentPanel_Player experimentPanel) {
		this.experimentPanel = experimentPanel;
		conditionPanel = this.experimentPanel.getConditionPanel();
		this.experimentPanel.removeSubjectActionListener(this);
		this.experimentPanel.addSubjectActionListener(this);
		this.experimentPanel.removeInstructionsHyperlinkListener(this);
		this.experimentPanel.addInstructionsHyperlinkListener(this);
	}	
	
	public boolean isConditionRunning() {
		return conditionRunning && !tutorialController.isConditionRunning();
	}	

	@Override
	public void examResponseChanged() {
		//Restart the exam since all response data has changed
		if(experimentRunning) {
			stopExperiment();
			if(subjectData == null) {
				subjectData = new IcarusSubjectData(null, null, 0);
			} else {			
				subjectData.setCurrentCondition(0);
			}
			subjectData.setCurrentTrial(0);
			skipNextPause = true;
			startExperiment(subjectData);
		}
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.player.phase_1.data_model.ExamResponseDataSource.ExamResponseChangeListener#trialResponseChanged(java.lang.String, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public void trialResponseChanged(String taskId, Integer taskNum, Integer trialNum) {
		//Notify the appropriate task controller that a trial response changed
		if(experimentRunning && examResponse != null) {			
			TaskResponseData_Phase1<?> task = examResponse.getTestPhaseResponse(taskId);
			if(task != null && task.getTestPhase() != null) {				
				if(task.getTestPhase() instanceof Task_1_2_3_PhaseBase) {					
					task123Controller.trialResponseChanged(trialNum);
				} else if(task.getTestPhase() instanceof Task_4_Phase) {
					task4Controller.trialResponseChanged(trialNum);					
				} else if(task.getTestPhase() instanceof Task_5_Phase || task.getTestPhase() instanceof Task_6_Phase) {
					task56Controller.trialResponseChanged(trialNum);
				} 
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment_core.event.ConditionListener#conditionActionPerformed(org.mitre.icarus.cps.experiment_core.event.ConditionEvent)
	 */
	@Override
	public void conditionActionPerformed(ConditionEvent event) {		
		if(event.eventType == ConditionEvent.TRIAL_CHANGED) {			
			//Set the trial number in the status panel
			experimentPanel.setTrialNumber(event.trialNum+1, event.trialPartNum+1, event.numTrialParts);
			currentTrial = event.trialNum;
		}	
		else if(event.eventType == ConditionEvent.CONDITION_COMPLETED) {			
			//The condition was completed, or the user pressed back to go to the previous condition			
			experimentPanel.setTrialNumber(-1, -1, -1);
			currentTrial = event.trialNum;
			conditionRunning = false;
		}
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment_core.event.SubjectActionListener#subjectActionPerformed(org.mitre.icarus.cps.experiment_core.event.SubjectActionEvent)
	 */
	@Override
	public void subjectActionPerformed(SubjectActionEvent event) {
		if(event.eventType == SubjectActionEvent.BACK_BUTTON_PRESSED) {			
			backButtonPressed = true;	
		} else if(event.eventType == SubjectActionEvent.NEXT_BUTTON_PRESSED) {			
			nextButtonPressed = true;
		} else if(event.eventType == PlayerNavButtonPanel.SKIP_TO_TRIAL_BUTTON_PRESSED) {
			if(experimentRunning && currentTaskResponse != null && currentTaskResponse.getTestPhase() != null) {
				if(appController != null) {
					appController.examControlOptionSelected(ExamControlOption.Advance_To_Trial);
				} else {
					Integer trial = TrialSelectionDlg.showDialog(getParentWindow(), 
							getCurrentTrials(), currentTrial+1);
					if(trial != null && experimentRunning) {
						advanceToTrial(currentConditionIndex, trial, true);
					}					
				}
				/*Integer trial = TrialSelectionDlg.showDialog(applicationWindow.getWindowComponent(), 
				(IcarusTestPhase<?>)examController.getCurrentExamPhase(), examController.getCurrentTrialNumber());
				if(trial != null) {
					advanceToExamTrialInCurrentPhase(trial);
				}*/
			}			
		} else if(event.eventType == PlayerNavButtonPanel.SKIP_TO_MISSION_BUTTON_PRESSED) {
			if(experimentRunning) {
				if(appController != null) {
					appController.examControlOptionSelected(ExamControlOption.Advance_To_Phase);
				} else {
					Integer phase = SkipToPhaseDlg.showDialog(getParentWindow(), 
							examResponse.getTestPhaseResponses(), 
							currentConditionIndex, "Mission");
					if(phase != null && phase >= 0) {
						skipToCondition(phase);
					}
				}
				/*Integer conditionIndex = MissionSelectionDlg.showDialog(getParentWindow(),						
						examResponse.getTestPhaseResponses(),
						currentConditionIndex, "Mission");
				if(conditionIndex != null && experimentRunning) {
					skipToCondition(conditionIndex);
				}*/
			}			
		} else if(event.eventType == SubjectActionEvent.HELP_BUTTON_PRESSED) {
			//Show the tutorial for the current task or a help page
			try {
				if(currentTask.getInstructionPages() != null) {
					String pagesId = exam.getId() + "_" + currentTask.getId();
					if(!pagesId.equals(experimentPanel.getInstructionsPagesId())) {
						experimentPanel.setInstructionsPages(currentTask.getName() + " Instructions",
								pagesId, currentTask.getInstructionPages());
					}
					experimentPanel.setExternalInstructionsVisible(true, "Mission Instructions",
							ImageManager_Phase1.getImage(ImageManager_Phase1.HELP_ICON));
				}
			} catch (Exception e) {
				e.printStackTrace();
				ErrorDlg.showErrorDialog(experimentPanel.getParentWindow(), 
						new Exception("The tutorial could not be opened", e), true);
			}
		}
		else if(event.eventType == SubjectActionEvent.CUSTOM_BUTTON_PRESSED) {
			//Show the exam tutorial
			try {
				if(exam.getTutorial() != null) {
					String pagesId = exam.getId();
					if(!pagesId.equals(experimentPanel.getInstructionsPagesId())) {
						experimentPanel.setInstructionsPages("Exam Tutorial", 
								pagesId, exam.getTutorial().getTutorialPages());
					}
					experimentPanel.setExternalInstructionsVisible(true, "Exam Tutorial",
							ImageManager_Phase1.getImage(ImageManager_Phase1.HELP_ICON));
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
		if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			try {
				if(e.getDescription().equalsIgnoreCase("probs")) {
					ArrayList<ProbabilityRulesPage> rulesPages = new ArrayList<ProbabilityRulesPage>();
					boolean showWaveintRules = currentTask instanceof Task_7_Phase;
					for(ProbabilityRulesPage page : exam.getProbabilityInstructions().getProbabilityRulesPages()) {
						if(page.getIntType() == IntType.WAVEINT) {
							if(showWaveintRules) {
								rulesPages.add(page);
							}
						} else {
							rulesPages.add(page);
						}
					}	
					experimentPanel.setInstructionsPages("PROBS", "PROBS_" + exam.getId(), rulesPages);
					experimentPanel.setExternalInstructionsVisible(true, "PROBS",
							ImageManager_Phase1.getImage(ImageManager_Phase1.HELP_ICON));
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				ErrorDlg.showErrorDialog(experimentPanel.getParentWindow(), 
						new Exception("Error showing tutorial", ex), true);
			}
		}
	}
	
	/**
	 * @param pages
	 */	
	protected void initializeInstructionPages(List<? extends InstructionsPage> pages) {
		try {
			IcarusExamLoader.initializeTutorialPages(pages, exam.getOriginalPath());
		} catch(Exception ex) {
			ex.printStackTrace();
			ErrorDlg.showErrorDialog(experimentPanel.getParentWindow(), 
					new Exception("Error showing tutorial", ex), true);
		}		
	}
	
	/**
	 * Show a page with a link to open the tutorial for the current task or a default instructions page to begin the task.
	 * 
	 * @param currentTask the current task to show the instructions page for
	 */	
	protected void showTaskInstructionsPage(final TaskTestPhase<?> currentTask) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				//Show a page with a link to open the tutorial for the task
				if(currentTask.getTutorialUrl() != null) {
					StringBuilder sb = new StringBuilder("<a href=\"open_task_tutorial\">Click here</a> to open the " + 
							currentTask.getName() + " tutorial.<br>" +
							"You may review the mission tutorial at any time during the task by clicking the Review Mission Tutorial button.");
					if(currentTask.getName() == null) {
						sb.append("<br><br>Click Next when you are ready to begin Mission " + Integer.toString(currentConditionIndex+1) + ".");
					} else {
						sb.append("<br><br>Click Next when you are ready to begin " + currentTask.getName() + ".");
					}
					experimentPanel.setInstructionText(InstructionsPanel.formatTextAsHTML(sb.toString()));
				} else {		
					//Show default instructions page												
					if(currentTask.getInstructionText() != null) {
						if(currentTask.getName() == null) {
							experimentPanel.setInstructionText(
									InstructionsPanel.formatTextAsHTML(currentTask.getInstructionText() + "<br><br>Click Next to begin Mission " + Integer.toString(currentConditionIndex+1) + "."));
						} else {
							experimentPanel.setInstructionText(
									InstructionsPanel.formatTextAsHTML(currentTask.getInstructionText() + "<br><br>Click Next to begin " + currentTask.getName() + "."));
						}													
					} else {
						if(currentTask.getName() == null) {
							experimentPanel.setInstructionText(
									InstructionsPanel.formatTextAsHTML("Click Next to begin Mission " + Integer.toString(currentConditionIndex+1) + "."));
						} else {
							experimentPanel.setInstructionText(
									InstructionsPanel.formatTextAsHTML("Click Next to begin " + currentTask.getName() + "."));
						}
					}
				}											
				experimentPanel.showInternalInstructionPanel();											
			}});
	}
}