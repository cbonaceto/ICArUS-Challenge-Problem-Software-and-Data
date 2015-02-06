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
package org.mitre.icarus.cps.app.experiment.phase_1;

import java.awt.Desktop;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
import org.mitre.icarus.cps.app.widgets.map.phase_1.MapPanelContainer;
import org.mitre.icarus.cps.app.widgets.phase_1.ImageManager_Phase1;
import org.mitre.icarus.cps.app.widgets.phase_1.experiment.ConditionPanel_Phase1;
import org.mitre.icarus.cps.app.widgets.phase_1.experiment.ExperimentPanel_Phase1;
import org.mitre.icarus.cps.app.widgets.progress_monitor.SwingProgressMonitor;
import org.mitre.icarus.cps.app.widgets.video.VideoWindow;
import org.mitre.icarus.cps.assessment.score_computer.phase_1.IScoreComputer;
import org.mitre.icarus.cps.exam.base.IcarusExamPhase;
import org.mitre.icarus.cps.exam.base.pause.IcarusPausePhase;
import org.mitre.icarus.cps.exam.base.response.ResponseGeneratorData;
import org.mitre.icarus.cps.exam.base.testing.IcarusTestTrial;
import org.mitre.icarus.cps.exam.phase_1.IcarusExam_Phase1;
import org.mitre.icarus.cps.exam.phase_1.feedback.TaskFeedback;
import org.mitre.icarus.cps.exam.phase_1.loader.IcarusExamLoader_Phase1;
import org.mitre.icarus.cps.exam.phase_1.testing.TaskTestPhase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_1_2_3_PhaseBase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_4_5_6_PhaseBase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_4_Phase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_5_Phase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_6_Phase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_7_Phase;
import org.mitre.icarus.cps.exam.phase_1.testing.probability_rules.ProbabilityRules;
import org.mitre.icarus.cps.exam.phase_1.training.ProbabilityRulesPage;
import org.mitre.icarus.cps.exam.phase_1.training.TutorialPhase;
import org.mitre.icarus.cps.experiment_core.condition.InstructionsPage;
import org.mitre.icarus.cps.experiment_core.event.ConditionEvent;
import org.mitre.icarus.cps.experiment_core.event.SubjectActionEvent;
import org.mitre.icarus.cps.experiment_core.gui.InstructionsPanel;
import org.mitre.icarus.cps.experiment_core.gui.INavButtonPanel.ButtonType;
import org.mitre.icarus.cps.experiment_core.subject_data.SubjectData;
import org.mitre.icarus.cps.feature_vector.phase_1.IntType;

/**
 * Controller for the Phase 1 exam.
 * 
 * @author CBONACETO
 *
 */
public class IcarusExamController_Phase1 extends 
	IcarusExamController<IcarusExam_Phase1, TaskTestPhase<?>, TutorialPhase, 
	ExperimentPanel_Phase1, ConditionPanel_Phase1> implements HyperlinkListener {	
		
	/** Window where tutorial videos are shown */
	protected VideoWindow videoWindow;
	protected ExecutorService videoWindowThread;
	
	/** Whether the exam tutorial has been shown */
	protected boolean examTutorialShown = false;   
	
	/** The current task being executed */
	protected TaskTestPhase<?> currentCondition;
	
	/** Time the subject started the current task */
	private long conditionStartTime = 0;
	
	/** The tasks whose feature vector data has been initialized */
	protected Set<TaskTestPhase<?>> initializedConditions;
    
    /** The current task controller */
	protected TaskController<?> currentConditionController;	
	
	/** Tutorial controller */
	protected TutorialController_Phase1<IcarusExamController_Phase1, IcarusExam_Phase1> tutorialController;

	/** Controller for tasks 1-3 */
	protected Task_1_2_3_Controller task123Controller;
	
	/** Controller for task 4 */
	protected Task_4_Controller task4Controller;
	
	/** Controller for tasks 5-6 */
	protected Task_5_6_Controller task56Controller;
	
	/** Controller for task 7 */
	protected Task_7_Controller task7Controller;
	
	/** HUMINT rules for Tasks 4-7 */
	protected ProbabilityRulesPage humintRules;
	
	/** WAVEINT rules for Task 7 */
	protected ProbabilityRulesPage waveintRules;
	
	/** Whether to skip the next pause (if any) */
	protected boolean skipNextPause = false;
	
	/** The score computer to use */
	private IScoreComputer scoreComputer;
	
	/** Whether to use KML feature vector data if present */
	protected boolean useKmlIfPresent = false;	

	/** 
	 * Constructor takes a score computer instance and data recorder instance.
	 * 
	 * @param scoreComputer the score computer to use to compute the subject's S1 and S2 scores
	 * @param dataRecorder the data recorder to use to record subject data
	 */
	public IcarusExamController_Phase1(IScoreComputer scoreComputer, 
			ISubjectDataRecorder<IcarusExam_Phase1, TaskTestPhase<?>, TutorialPhase> dataRecorder) {
		super(dataRecorder);
		this.scoreComputer = scoreComputer;
		tutorialController = new TutorialController_Phase1<IcarusExamController_Phase1, IcarusExam_Phase1>();
		task123Controller = new Task_1_2_3_Controller();
		task4Controller = new Task_4_Controller();
		task56Controller = new Task_5_6_Controller();
		task7Controller = new Task_7_Controller();
	}	

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment_core.controller.IExperimentController#initializeExperimentController(org.mitre.icarus.cps.experiment_core.Experiment)
	 */
	@Override
	public synchronized void initializeExperimentController(IcarusExam_Phase1 experiment) {		
		skipNextPause = false;
		examTutorialShown = false;
		
		//Stop the currently executing exam
		stopExperiment();

		//Initialize the experiment panel
		this.exam = experiment;		
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
		
		if(exam.getConditions() != null && !exam.getConditions().isEmpty()) {
			//Number the phases (some phases may not be counted)
			initializedConditions = new HashSet<TaskTestPhase<?>>(exam.getTasks().size());			
			int currPhase = 1;
			for(TaskTestPhase<?> phase : exam.getConditions()) {
				if(!phase.isCountCondition()) {
					phase.setConditionNum(-1);
				}
				else {
					phase.setConditionNum(currPhase);
					currPhase++;
				}
			}			
			//experimentPanel.setNumConditions(exam.getConditions().size());
			experimentPanel.setNumConditions(currPhase-1);
		} else {
			initializedConditions = new HashSet<TaskTestPhase<?>>();
			experimentPanel.setNumConditions(0);
		}
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment_core.controller.IExperimentController#startExperiment(org.mitre.icarus.cps.experiment_core.subject_data.SubjectData)
	 */
	@Override
	public synchronized void startExperiment(SubjectData sData) {
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
						currentConditionController.removeConditionListener(IcarusExamController_Phase1.this);
						currentConditionController.stopCondition();
					}
				} catch(Exception ex) {}
			}

			@Override
			protected Object doInBackground() {
				try {
					/** Wait until the previously executing experiment worker finishes executing (waits up to 5 seconds) */
					long exitTime = System.currentTimeMillis() + 5000;
					while(!exitedWorker && !isCancelled()) {					
						try {
							Thread.sleep(50);
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
						experimentPanel.setSubject(subjectData.getSubjectId());
						currentConditionIndex = subjectData.getCurrentCondition();
					}
					else {
						currentConditionIndex = 0;	
					}
					currentTrial = 1;
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
									experimentPanel.setNavButtonVisible(ButtonType.Help, false);
									experimentPanel.setReviewTutorialButtonVisible(false);
								}});
							
							if(currentConditionIndex == 0 && exam.getTutorialUrl() != null && !examTutorialShown) {
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
									while(!nextButtonPressed && experimentRunning && !isCancelled()) {try {Thread.sleep(50);} catch(Exception ex) {}}
								}
							}
							else if(currentConditionIndex == 0 && exam.getTutorial() != null && !examTutorialShown) {
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
												tutorialController.addConditionListener(IcarusExamController_Phase1.this);
												tutorialController.startCondition(0, IcarusExamController_Phase1.this, null);
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
								experimentPanel.removeSubjectActionListener(tutorialController);
								tutorialController.removeConditionListener(IcarusExamController_Phase1.this);
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

							currentCondition = exam.getTasks().get(currentConditionIndex);						

							//Load the feature vector data and any instructions pages for the task if it hasn't been loaded yet
							if(!initializedConditions.contains(currentCondition)) {
								try {
									IcarusExamLoader_Phase1.initializeTaskFeatureVectorData(currentCondition, 
											exam.getOriginalPath(), exam.getGridSize(), useKmlIfPresent, 
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
									}					
									else {
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
							
							//Execute the task
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
								if(experimentRunning && !isCancelled() && currentCondition.isShowInstructionPage()) {									
									//First show the instruction pages if present
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
								}
																
								if(experimentRunning && !isCancelled()) {
									//Start the current task
									setNavButtonEnabled(ButtonType.Back, false);
									if(showMultiPageInstructions) {
										experimentPanel.setTrialDescriptor("Trial");
										experimentPanel.setConditionNumber(currentCondition.getName(),
												currentCondition.getConditionNum(), currentCondition.getNumTrials());
										experimentPanel.setTrialNumber(1);
									}
									if(currentConditionController != null) {
										experimentPanel.removeSubjectActionListener(currentConditionController);
										currentConditionController.removeConditionListener(IcarusExamController_Phase1.this);
									}														

									try {
										//Initialize the appropriate task controller
										if(waveintRules != null) {
											experimentPanel.getConditionPanel().getMapPanel().removeInstructionsPage(
													IntType.WAVEINT.toString());
										}
										if(currentCondition instanceof Task_1_2_3_PhaseBase) {
											task123Controller.setExam(exam);
											task123Controller.initializeConditionController((Task_1_2_3_PhaseBase<?>)currentCondition, conditionPanel);
											currentConditionController = task123Controller;
											experimentPanel.getConditionPanel().getMapPanel().setGroupCenterstLayerInstructions(humintRules, "");
										} else {
											if(humintRules != null) {
												experimentPanel.getConditionPanel().getMapPanel().setGroupCenterstLayerInstructions(humintRules, "PROBS");	
											}
											if(currentCondition instanceof Task_4_Phase) {
												task4Controller.setExam(exam);
												task4Controller.initializeConditionController((Task_4_Phase)currentCondition, conditionPanel);
												currentConditionController = task4Controller;
											} else if(currentCondition instanceof Task_5_Phase || currentCondition instanceof Task_6_Phase) {
												task56Controller.setExam(exam);
												task56Controller.initializeConditionController((Task_4_5_6_PhaseBase<?>)currentCondition, conditionPanel);
												currentConditionController = task56Controller;
											} else if(currentCondition instanceof Task_7_Phase) {
												task7Controller.setExam(exam);
												task7Controller.initializeConditionController((Task_7_Phase)currentCondition, conditionPanel);
												currentConditionController = task7Controller;
												if(waveintRules != null) {
													experimentPanel.getConditionPanel().getMapPanel().addInstructionsPage(
															IntType.WAVEINT.toString(), waveintRules);
												}
											} else {
												currentConditionController = null;
											}
										}
									} catch(Exception ex) {
										ex.printStackTrace();
										ErrorDlg.showErrorDialog(experimentPanel.getParentWindow(), ex, true);
									}

									if(currentConditionController != null && experimentRunning && !isCancelled()) {									
										conditionRunning = true;
										//Execute task controller in the event dispatching thread
										SwingUtilities.invokeLater(new Runnable() {
											public void run() {
												experimentPanel.setTrialNumber(1);
												experimentPanel.addSubjectActionListener(currentConditionController);																			
												try {
													currentConditionController.addConditionListener(IcarusExamController_Phase1.this);
													currentConditionController.startCondition(0, IcarusExamController_Phase1.this, null);
													conditionStartTime = System.currentTimeMillis();
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
										
										SwingUtilities.invokeLater(new Runnable() {
											public void run() {
												//Allow subject to review the task instructions or tutorial and show the condition panel
												setExternalInstructionsVisible(false);
												experimentPanel.setNavButtonVisible(ButtonType.Help, 
														(currentCondition.getTutorialUrl() != null || currentCondition.getInstructionPages() != null));
												experimentPanel.showConditionPanel();
											}});	
									}
								}

								//Wait for the current task to complete
								while(conditionRunning && experimentRunning && !isCancelled()) {try {Thread.sleep(50);} catch(Exception ex) {}}							

								setExternalInstructionsVisible(false);
								experimentPanel.removeSubjectActionListener(currentConditionController);
								if(currentConditionController != null) {
									currentConditionController.removeConditionListener(IcarusExamController_Phase1.this);
								}

								if(experimentRunning && !isCancelled() && currentConditionController != null) {									
									TaskTestPhase<?> taskWithResponses = currentConditionController.getTaskWithResponseData();
									try {
										//Compute normative data (Tasks 1-6) and scores (Tasks 1-6)
										if(scoreComputer != null && scoreComputer.isEnabled()) {
											if(exam.getProbabilityRules() == null) {
												exam.setProbabilityRules(ProbabilityRules.createDefaultProbabilityRules());
											}
											TaskFeedback feedback = scoreComputer.computeSolutionAndScoreForTask(currentCondition, 
													exam.getProbabilityRules(), exam.getGridSize(), true,
													new SwingProgressMonitor(experimentPanel.getParentWindow(), 
															"Computing score for task", "Computing score for task", 0, 100));
											if(feedback != null) {
												feedback.setTrialFeedback(null);
												currentCondition.setTaskFeedback(feedback);
												if(feedback.getErrors() != null && !feedback.getErrors().isEmpty()) {
													ErrorDlg.showErrorDialog(experimentPanel.getParentWindow(), new Exception("Error computing score: " + 
															feedback.getErrors()), false);
												}
											}		
										}
									} catch(Exception ex) {
										ex.printStackTrace();
										ErrorDlg.showErrorDialog(experimentPanel.getParentWindow(), ex, true);
									}

									//Save task response data
									boolean dataRecorded = true;
									if(experimentRunning && !isCancelled() && 
											taskWithResponses != null && dataRecorder != null && dataRecorder.isEnabled()) {
										dataRecorded = false;
										boolean retry = false;
										do {
											taskWithResponses.setStartTime(new Date(conditionStartTime));
											taskWithResponses.setEndTime(new Date(System.currentTimeMillis()));
											taskWithResponses.setResponseGenerator(new ResponseGeneratorData(
													subjectData.getSite() != null ? subjectData.getSite().getTag() : null, 
													subjectData.getSubjectId(), true));
											try {
												dataRecorder.recordExamPhaseData(subjectData, exam, taskWithResponses, IcarusExamLoader_Phase1.examLoaderInstance);								
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

									//Show average S1 and/or S2 scores for the task
									if(experimentRunning && !isCancelled() && currentCondition.isShowScore()) {										
										final Double scoreS1;
										final Double scoreS2;
										if(scoreComputer != null && scoreComputer.isEnabled() && 
												currentCondition.getTaskFeedback() != null) {
											scoreS1 = currentCondition.getTaskFeedback().getProbabilitiesScore_s1();
											scoreS2 = currentCondition.getTaskFeedback().getTroopAllocationScore_s2();
										}
										else {
											scoreS1 = -1D;
											scoreS2 = -1D;
										}
										if(scoreS1 != null || scoreS2 != null) {
											SwingUtilities.invokeLater(new Runnable() {
												public void run() {
													StringBuilder sb = createScoreText(scoreS1, scoreS2, currentCondition.getName(),
															currentConditionIndex+1, currentConditionIndex+1);
													sb.append("<br>Click Next to continue.");
													experimentPanel.setInstructionText(InstructionsPanel.formatTextAsHTML(sb.toString()));
													experimentPanel.showInternalInstructionPanel();
													experimentPanel.setNavButtonEnabled(ButtonType.Next, true);
												}
											});										
											if(experimentRunning) {																					
												//Wait for the next button to be pressed
												nextButtonPressed = false;
												while(!nextButtonPressed && experimentRunning && !isCancelled()) {try {Thread.sleep(50);} catch(Exception ex) {}}
											}
										}
										
										//Show the average S1 and S2 score over missions 1-6
										if(currentConditionIndex == 5) {
											Double avgScoreS1 = null;
											Double avgScoreS2 = null;
											if(scoreComputer != null && scoreComputer.isEnabled()) {
												double scoreSumS1 = 0;
												int numS1Scores = 0;
												double scoreSumS2 = 0;
												int numS2Scores = 0;
												for(TaskTestPhase<?> task : exam.getTasks()) {
													if(!(task instanceof Task_7_Phase) && task.getTaskFeedback() != null) {
														if(task.getTaskFeedback().getProbabilitiesScore_s1() != null) {
															scoreSumS1 += task.getTaskFeedback().getProbabilitiesScore_s1();
															numS1Scores++;
														}
														if(task.getTaskFeedback().getTroopAllocationScore_s2() != null) {
															scoreSumS2 += task.getTaskFeedback().getTroopAllocationScore_s2();
															numS2Scores++;
														}
													}
												}
												if(numS1Scores > 0) {
													avgScoreS1 = scoreSumS1/numS1Scores;
												}
												if(numS2Scores > 0) {
													avgScoreS2 = scoreSumS2/numS2Scores;
												}
											} else {
												avgScoreS1 = -1D;
												avgScoreS2 = -1D;
											}
											if(avgScoreS1 != null || avgScoreS2 != null) {
												final Double avgScoreS1Final = avgScoreS1;
												final Double avgScoreS2Final = avgScoreS2;
												if(experimentRunning) {
													SwingUtilities.invokeLater(new Runnable() {
														public void run() {
															StringBuilder sb = createScoreText(avgScoreS1Final, avgScoreS2Final, currentCondition.getName(),
																	1, 6);
															sb.append("<br>Click Next to continue.");
															experimentPanel.setInstructionText(InstructionsPanel.formatTextAsHTML(sb.toString()));
															experimentPanel.showInternalInstructionPanel();
															experimentPanel.setNavButtonEnabled(ButtonType.Next, true);
														}
													});										
													//Wait for the next button to be pressed
													nextButtonPressed = false;
													while(!nextButtonPressed && experimentRunning && !isCancelled()) {try {Thread.sleep(50);} catch(Exception ex) {}}
												}
											}
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
		stopExperiment();
		if(subjectData == null) {
			//System.err.println("subject data was null!");
			subjectData = new IcarusSubjectData(null, null, conditionIndex);
		}		
		else {			
			subjectData.setCurrentCondition(conditionIndex);
		}
		skipNextPause = true;
		startExperiment(subjectData);
	}	

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment.IcarusExamController#setExperimentPanel(org.mitre.icarus.cps.experiment_core.gui.IExperimentPanel)
	 */
	@Override
	public void setExperimentPanel(ExperimentPanel_Phase1 experimentPanel) {
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
	
	/**
	 * Get the score computer being used.
	 * 
	 * @return the score computer
	 */
	public IScoreComputer getScoreComputer() {
		return scoreComputer;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment_core.event.ConditionListener#conditionActionPerformed(org.mitre.icarus.cps.experiment_core.event.ConditionEvent)
	 */
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

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment_core.event.SubjectActionListener#subjectActionPerformed(org.mitre.icarus.cps.experiment_core.event.SubjectActionEvent)
	 */
	@Override
	public void subjectActionPerformed(SubjectActionEvent event) {
		if(event.eventType == SubjectActionEvent.BACK_BUTTON_PRESSED) {			
			backButtonPressed = true;			
		}
		else if(event.eventType == SubjectActionEvent.NEXT_BUTTON_PRESSED) {			
			nextButtonPressed = true;
		} else if(event.eventType == SubjectActionEvent.HELP_BUTTON_PRESSED) {
			//Show the tutorial for the current task or a help page
			try {
				if(currentCondition.getTutorialUrl() != null) {
					showTutorial(new URL(exam.getOriginalPath(), currentCondition.getTutorialUrl()),
							currentCondition.getName() + " Tutorial");
				}
				else if(currentCondition.getInstructionPages() != null) {
					String pagesId = exam.getId() + "_" + currentCondition.getId();
					if(!pagesId.equals(experimentPanel.getInstructionsPagesId())) {
						experimentPanel.setInstructionsPages(currentCondition.getName() + " Instructions",
								pagesId, currentCondition.getInstructionPages());
					}
					experimentPanel.setExternalInstructionsVisible(true, "Mission Instructions",
							ImageManager_Phase1.getImage(ImageManager_Phase1.HELP_ICON));
				}
			} catch (Exception e) {
				e.printStackTrace();
				ErrorDlg.showErrorDialog(experimentPanel.getParentWindow(), 
						new Exception("The tutorial could not be opened", e), true);
			}
		} else if(event.eventType == SubjectActionEvent.CUSTOM_BUTTON_PRESSED) {
			//Show the exam tutorial
			try {
				if(exam.getTutorialUrl() != null) {
					showTutorial(new URL(exam.getOriginalPath(), exam.getTutorialUrl()), "Exam Tutorial");
				}
				else if(exam.getTutorial() != null) {
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
	
	protected StringBuilder createScoreText(Double probabilitiesScore_S1, Double troopAllocationScore_S2, String taskName,
			int firstTask, int lastTask) {
		StringBuilder sb = new StringBuilder("You have now completed ");
		
		if(firstTask != lastTask) {
			sb.append("<b>Missions " + Integer.toString(firstTask) + " through " + Integer.toString(lastTask) + "</b>. ");
			sb.append("On average, over all<b> " + (lastTask-firstTask+1) + " Missions</b>, you scored as follows:<br><br>");
		} else {
			sb.append("<b>" + taskName + "</b>");
			sb.append(". On average, over all trials of<b> " + taskName + "</b>, you scored as follows:<br><br>");
		}
		
		if(scoreComputer == null || !scoreComputer.isEnabled()) {
			sb.append("The average troop allocation score is not computed in this version of the software.<br>");
			sb.append("The average probabilities score is not computed iin this version of the software.<br>");
		} else { 
			if(troopAllocationScore_S2 != null) {
				sb.append("<b>" + Math.round(troopAllocationScore_S2) + " </b>out of 100 points for allocating troops.<br>");
			}
			if(probabilitiesScore_S1 != null) {
				sb.append("<b>" + Math.round(probabilitiesScore_S1) + " </b>out of 100 points for estimating probabilities.<br>");
			}
		}
		return sb;
	}

	@Override
	public void hyperlinkUpdate(HyperlinkEvent e) {		
		if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			try {
				if(e.getDescription().equalsIgnoreCase("open_exam_tutorial")) {				
					showTutorial(new URL(exam.getOriginalPath(), exam.getTutorialUrl()),
					"Exam Tutorial");				
				} else if(e.getDescription().equalsIgnoreCase("open_task_tutorial")) {
					showTutorial(new URL(exam.getOriginalPath(), currentCondition.getTutorialUrl()),
							currentCondition.getName() + " Tutorial");
				} else if(e.getDescription().equalsIgnoreCase("probs")) {
					ArrayList<ProbabilityRulesPage> rulesPages = new ArrayList<ProbabilityRulesPage>();
					boolean showWaveintRules = currentCondition instanceof Task_7_Phase;
					for(ProbabilityRulesPage page : exam.getProbabilityInstructions().getProbabilityRulesPages()) {
						if(page.getIntType() == IntType.WAVEINT) {
							if(showWaveintRules) {
								rulesPages.add(page);
							}
						}
						else {
							rulesPages.add(page);
						}
					}
					experimentPanel.setInstructionsPages("PROBS", "PROBS_" + exam.getId(), rulesPages);
					experimentPanel.setExternalInstructionsVisible(true, "PROBS",
							ImageManager_Phase1.getImage(ImageManager_Phase1.HELP_ICON));
				}
			} catch (MalformedURLException ex) {
				ex.printStackTrace();
				ErrorDlg.showErrorDialog(experimentPanel.getParentWindow(), 
						new Exception("Error showing tutorial", ex), true);
			}
		}
	}	
	
	/**
	 * Show a page with a link to open the tutorial for the current task or a default instructions page to begin the task.
	 * 
	 * @param currentCondition the current task to show the instructions page for
	 */
	@Override
	protected void showExamPhaseInstructionsPage(final TaskTestPhase<?> currentCondition) {
		if(currentCondition.getTutorialUrl() != null) {
			//Show a page with a link to open the tutorial for the task
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					StringBuilder sb = new StringBuilder("<a href=\"open_task_tutorial\">Click here</a> to open the " + 
							currentCondition.getName() + " tutorial.<br>" +
							"You may review the mission tutorial at any time during the task by clicking the Review Mission Tutorial button.");
					if(currentCondition.getName() == null) {
						sb.append("<br><br>Click Next when you are ready to begin Mission " + Integer.toString(currentConditionIndex+1) + ".");
					} else {
						sb.append("<br><br>Click Next when you are ready to begin " + currentCondition.getName() + ".");
					}
					experimentPanel.setInstructionText(InstructionsPanel.formatTextAsHTML(sb.toString()));
					experimentPanel.showInternalInstructionPanel();
				}});
		} else {		
			//Show default instructions page												
			IcarusExamController_Phase1.super.showExamPhaseInstructionsPage(currentCondition);
		}
	}
	
	/**
	 * Show a tutorial document or video.
	 * 
	 * @param tutorialUrl the URL to the tutorial document or video
	 */
	protected void showTutorial(final URL tutorialUrl, String tutorialName) {	
		//System.out.println(tutorialUrl);
		String file = tutorialUrl.getFile();
		if(file.endsWith(".html") || file.endsWith(".htm")) {
			//Show a tutorial HTML page
			showTutorialHtml(tutorialUrl);
		} else if(file.endsWith(".mp4") || file.endsWith(".wmv") ||
			 file.endsWith(".avi") || file.endsWith(".mov") || file.endsWith(".mpg")) {
			//Show a tutorial video
			try {
				//First try to show the video in the default native video player
				showTutorialDocument(tutorialUrl, false);
			} catch(Exception ex) {
				ex.printStackTrace();
				try {
					//Show the tutorial in the embedded video player
					showTutorialVideo(tutorialUrl, tutorialName);
				} catch(final Exception e) { 
					ex.printStackTrace();
					//Lastly, try to show the video in a web browser
					//TODO: Add this
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							ErrorDlg.showErrorDialog(experimentPanel.getParentWindow(), 
									new Exception("The tutorial could not be opened", e), true);
						}});
				}			
			}
		} else {
			//Show a tutorial document
			try {
				showTutorialDocument(tutorialUrl, false);
			} catch(final Exception ex) { 
				ex.printStackTrace();
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						ErrorDlg.showErrorDialog(experimentPanel.getParentWindow(), 
								new Exception("The tutorial could not be opened", ex), true);
					}});
			}
		}
	}
	
	protected String createTutorialVideoHtmlPage(URL tutorialUrl, String tutorialName) {
		StringBuilder sb = new StringBuilder("<html><body>");
		sb.append("<OBJECT CLASSID=\"clsid:02BF25D5-8C17-4B23-BC80-D3488ABDDC6B\""); 
		sb.append("CODEBASE=\"http://www.apple.com/qtactivex/qtplugin.cab\" WIDTH=\"160\" HEIGHT=\"136\" >");
		sb.append("<PARAM NAME=\"src\" VALUE=\"" + tutorialUrl.toString() + "\">");
		sb.append("<PARAM NAME=\"autoplay\" VALUE=\"true\">");
		sb.append("<EMBED SRC=\"" + tutorialUrl.toString() + "\" TYPE=\"image/x-macpaint\""); 
		sb.append("PLUGINSPAGE=\"http://www.apple.com/quicktime/download\" WIDTH=\"160\" HEIGHT=\"136\" AUTOPLAY=\"true\"></EMBED>");
		sb.append("</OBJECT>");
		sb.append("</body></html>");
		System.out.println(sb.toString());
		return null;
	}
	
	/**
	 * Show a tutorial HTML page.
	 */
	protected void showTutorialHtml(final URL tutorialUrl) {
		//TODO: Implement this		
	}	
	
	/**  
	 * Show a tutorial video.
	 * 
	 * @param tutorialUrl the URL to the tutorial video
	 */
	protected void showTutorialVideo(final URL tutorialUrl, final String tutorialName) {
		if(videoWindowThread == null) {
			videoWindowThread = Executors.newSingleThreadExecutor();
		}
		videoWindowThread.execute(new Runnable() {
			public void run() {
				if(videoWindow != null) {
					videoWindow.disposeVideoResources();
					videoWindow.dispose();
				}		
				videoWindow = new VideoWindow(experimentPanel.getParentWindow());
				videoWindow.setLocationByPlatform(true);
				//videoWindow.setLocationRelativeTo(experimentPanel);
				videoWindow.setVideoTitle(tutorialName);
				int start = tutorialUrl.getFile().indexOf("data");
				if(start >= 0) {	
					//File string needs to be relative to the current path
					String fileStr = tutorialUrl.getFile().substring(start, tutorialUrl.getFile().length());
					//System.out.println("Opening video file: " + fileStr);
					videoWindow.playVideo(fileStr);
				}
				else {
					videoWindow.playVideo(tutorialUrl);
				}		
				if(!videoWindow.isVisible()) {
					videoWindow.setVisible(true);
				}
			}
		});
	}
	
	/** Show a tutorial document using the Destkop API */
	protected void showTutorialDocument(final URL tutorialUrl, boolean openInBrowser) throws Exception {
		//Open the tutorial
		Exception error = null;
		if(Desktop.isDesktopSupported()) {
			try {
				Desktop desktop = Desktop.getDesktop();
				if(openInBrowser) {					
					desktop.browse(tutorialUrl.toURI());					
				}
				else {
					desktop.open(new File(tutorialUrl.toURI()));	
				}											
			} catch(Exception ex) {
				error = ex;
				//ex.printStackTrace();
			}
		}
		else {
			error = new Exception("Desktop API not supported");
		}

		if(error != null) {
			throw(error);
		}					
	}	
}