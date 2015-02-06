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
package org.mitre.icarus.cps.app.experiment.ui_study;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import org.mitre.icarus.cps.app.experiment.IcarusExamController;
import org.mitre.icarus.cps.app.experiment.IcarusSubjectData;
import org.mitre.icarus.cps.app.experiment.ui_study.data_recorder.ISubjectDataRecorder;
import org.mitre.icarus.cps.app.experiment.ui_study.exam.ItemProbability;
import org.mitre.icarus.cps.app.experiment.ui_study.exam.MultiBlockSpatialPresentationTrial;
import org.mitre.icarus.cps.app.experiment.ui_study.exam.SpatialPresentationBlock;
import org.mitre.icarus.cps.app.experiment.ui_study.exam.SpatialPresentationTrial;
import org.mitre.icarus.cps.app.experiment.ui_study.exam.Task_1_2_3_5_PhaseBase;
import org.mitre.icarus.cps.app.experiment.ui_study.exam.Task_2_3_5_PhaseBase;
import org.mitre.icarus.cps.app.experiment.ui_study.exam.Task_4_Phase;
import org.mitre.icarus.cps.app.experiment.ui_study.exam.TutorialPhase;
import org.mitre.icarus.cps.app.experiment.ui_study.exam.UIStudyExam;
import org.mitre.icarus.cps.app.experiment.ui_study.exam.UIStudyPhase;
import org.mitre.icarus.cps.app.widgets.WidgetConstants;
import org.mitre.icarus.cps.app.widgets.basic.tutorial.TutorialPanel;
import org.mitre.icarus.cps.app.widgets.dialog.ErrorDlg;
import org.mitre.icarus.cps.app.widgets.ui_study.ConditionPanel_UIStudy;
import org.mitre.icarus.cps.app.widgets.ui_study.ExperimentPanel_UIStudy;
import org.mitre.icarus.cps.exam.base.IcarusExamPhase;
import org.mitre.icarus.cps.exam.base.pause.IcarusPausePhase;
import org.mitre.icarus.cps.exam.base.response.ResponseGeneratorData;
import org.mitre.icarus.cps.exam.base.testing.IcarusTestTrial;
import org.mitre.icarus.cps.experiment_core.condition.InstructionsPage;
import org.mitre.icarus.cps.experiment_core.event.ConditionEvent;
import org.mitre.icarus.cps.experiment_core.event.SubjectActionEvent;
import org.mitre.icarus.cps.experiment_core.gui.InstructionsPanel;
import org.mitre.icarus.cps.experiment_core.gui.INavButtonPanel.ButtonType;
import org.mitre.icarus.cps.experiment_core.subject_data.SubjectData;

/**
 * @author CBONACETO
 *
 */
public class UIStudyController extends 
	IcarusExamController<UIStudyExam, UIStudyPhase<?>, TutorialPhase, 
	ExperimentPanel_UIStudy, ConditionPanel_UIStudy> {	
	
	/** The tutorial panel where exam tutorial pages are shown */
	protected TutorialPanel tutorialPanel;
	
	/** Whether the exam tutorial has been shown */
	protected boolean examTutorialShown = false;	
		
	/** The current task being executed */
	protected UIStudyPhase<?> currentTask;
	
	/** Time the subject started the current task */
	private long taskStartTime = 0;	
	
    /** The current task controller */
	protected TaskController<?> currentTaskController;
	
	/** Tutorial controller */
	protected TutorialController tutorialController;	
	
	/** Controller for tasks 1, 2, 3, and 5 */
	protected Task_1_2_3_5_Controller task1235Controller;
	
	/** Controller for task 4 */
	protected Task_4_Controller task4Controller;		
	
	/** Whether to skip the next pause (if any) */
	protected boolean skipNextPause = false;
	
	/** The data recorder to use */
	private final ISubjectDataRecorder dataRecorder;
	
	/** 
	 * Constructor takes the parent window (if any) and the condition panel (contained in the parent window).
	 * 
	 * @param parent the parent window
	 * @param conditionPanel the condition panel
	 * @param scoreComputer the score computer to use to compute the subject's S1 and S2 scores
	 * @param dataRecorder the data recorder to use to record subject data
	 */
	public UIStudyController(ConditionPanel_UIStudy conditionPanel, ISubjectDataRecorder dataRecorder) {
		this.conditionPanel = conditionPanel;
		this.dataRecorder = dataRecorder;
		tutorialPanel = new TutorialPanel();
		tutorialController = new TutorialController();
		task1235Controller = new Task_1_2_3_5_Controller();
		task4Controller = new Task_4_Controller();		
	}	

	@Override
	public void initializeExperimentController(UIStudyExam experiment) {		
		skipNextPause = false;
		examTutorialShown = false;
		
		//Stop the currently executing exam
		stopExperiment();

		//Initialize the experiment panel
		this.exam = (UIStudyExam)experiment;		
		experimentPanel.setExperimentName(exam.getName());		
		
		if(exam.getConditions() != null) {
			//Number the phases (some phases may not be counted)
			//initializedTasks = new HashSet<TaskTestPhase>(exam.getTasks().size());			
			int currPhase = 1;
			//Seed a random number generator to generate hits
			Random random = new Random(1); 
			Dimension gridSize = new Dimension(UIStudyConstants.GRID_WIDTH, UIStudyConstants.GRID_HEIGHT);
			for(UIStudyPhase<?> phase : exam.getConditions()) {
				if(!phase.isCountCondition()) {
					phase.setConditionNum(-1);
				}
				else {
					phase.setConditionNum(currPhase);
					currPhase++;
				}
				
				//Generate hits for Tasks 2 - 5
				if(phase.getTestTrials() != null) {
					if(phase instanceof Task_2_3_5_PhaseBase) {
						for(SpatialPresentationTrial trial : ((Task_2_3_5_PhaseBase)phase).getTestTrials()) {
							trial.setHitLocations(generateHitLocations(random, trial.getHits(), gridSize,
									trial.getItemProbabilities()));
						}
					} else if(phase instanceof Task_4_Phase) {
						for(MultiBlockSpatialPresentationTrial trial : ((Task_4_Phase)phase).getTestTrials()) {
							if(trial.getSpatialPresentationBlocks() != null) {
								for(SpatialPresentationBlock block : trial.getSpatialPresentationBlocks()) {
									block.setHitLocations(generateHitLocations(random, block.getHits(), gridSize,
											block.getItemProbabilities()));
									//block.setQuadrantVisitSequence(generateQuadrantVisitSequence(random,
									//		block.getHitLocations()));
								}
							}
						}
					}
				}
			}			
			//experimentPanel.setNumConditions(exam.getConditions().size());
			experimentPanel.setNumConditions(currPhase-1);			
		}
		else {
			//initializedTasks = new HashSet<TaskTestPhase>();
			experimentPanel.setNumConditions(0);
		}
	}	
	
	/** Generates random hit locations */
	protected ArrayList<ArrayList<Point>> generateHitLocations(Random random, int numHits, Dimension gridSize,
			ArrayList<ItemProbability> itemProbabilities) {
		ArrayList<ArrayList<Point>> hitLocations = new ArrayList<ArrayList<Point>>(itemProbabilities.size());
		int i = 0;
		int totalHits = 0;
		for(ItemProbability item : itemProbabilities) {			
			int hits = 0;
			if(i == itemProbabilities.size() - 1) {
				hits = numHits - totalHits;
			} else {
				hits = (int)(numHits * item.getProbability());	
				totalHits += hits;
			} 
			ArrayList<Point> locations = new ArrayList<Point>(hits);
			for(int j=0; j<hits; j++) {
				//Generate random point at least 2 grid units away from other points and at least 5 grid units from an edge
				int x = 0; int y = 0;
				boolean hitValid = false;
				int iterations = 0;
				while(!hitValid && iterations < 20) {
					x = random.nextInt(gridSize.width-9) + 5; //5-95
					y = random.nextInt(gridSize.height-9) + 5; //5-95					
					hitValid = true;
					for(Point location : locations) {
						if(Math.abs(location.x - x) <= 2 && Math.abs(location.y - y) <= 2) {
							hitValid = false;
							break;
						}
					}
					iterations++;
				}
				locations.add(new Point(x, y));
			}
			hitLocations.add(locations);
			i++;
		}
		return hitLocations;
	}
	
	/** Generate random sequence to visit each hit quadrant for Task 4 */
	protected LinkedList<Integer> generateQuadrantVisitSequence(Random random, ArrayList<ArrayList<Point>> hitLocations) {
		LinkedList<Integer> sequence = new LinkedList<Integer>();
		int quadrant = 0;
		for(ArrayList<Point> locations : hitLocations) {
			for(int i=0; i<locations.size(); i++) {
				sequence.add(quadrant);
			}
			quadrant++;
		}
		Collections.shuffle(sequence, random);
		return sequence;
	}

	@Override
	public ExperimentPanel_UIStudy getExperimentPanel() {
		return experimentPanel;
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment.IcarusExamController#setExperimentPanel(org.mitre.icarus.cps.experiment_core.gui.IExperimentPanel)
	 */
	@Override
	public void setExperimentPanel(ExperimentPanel_UIStudy experimentPanel) {		
		this.experimentPanel = (ExperimentPanel_UIStudy)experimentPanel;
		this.experimentPanel.removeSubjectActionListener(this);
		this.experimentPanel.addSubjectActionListener(this);
		
		//Add key listener to advance to next trial whenever enter is pressed
		//this.experimentPanel.removeKeyListener(this);
		/*this.experimentPanel.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				System.out.println("key pressed");
			}
		});*/
	}		

	@Override
	public void startExperiment(final SubjectData sData) {
		if(exam == null || experimentPanel == null) {
			throw new IllegalArgumentException("Error starting experiment controller: exam and experiment panel must be set");
		}

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
						currentTaskController.removeConditionListener(UIStudyController.this);
						currentTaskController.stopCondition();
					}
				} catch(Exception ex) {}		
				//exitedWorker = true;
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

					UIStudyController.this.subjectData = (IcarusSubjectData)sData;
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
					currentTask = null;

					while(experimentRunning && currentConditionIndex < exam.getTasks().size() && !isCancelled()) {
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
										experimentPanel.setConditionNumber("Tutorial", -1, -1);
									}
								});								

								initializeInstructionPages(exam.getTutorial().getTutorialPages());
								tutorialController.setExam(exam);
								tutorialController.initializeConditionController(exam.getTutorial(), tutorialPanel);
								if(experimentRunning && !isCancelled()) {									
									conditionRunning = true;
									//Execute tutorial controller in the event dispatching thread
									SwingUtilities.invokeLater(new Runnable() {
										public void run() {
											experimentPanel.addSubjectActionListener(tutorialController);																			
											try {
												tutorialController.addConditionListener(UIStudyController.this);
												tutorialController.startCondition(0, UIStudyController.this, null);
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
								tutorialController.removeConditionListener(UIStudyController.this);
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

							currentTask = exam.getTasks().get(currentConditionIndex);						

							//Load the feature vector data and any instructions pages for the task if it hasn't been loaded yet
							/*if(!initializedTasks.contains(currentTask)) {
								try {
									IcarusExamLoader_Phase1.initializeTaskFeatureVectorData(currentTask, 
											exam.getOriginalPath(), exam.getGridSize(), useKmlIfPresent, 
											new SwingProgressMonitor(experimentPanel, "Loading Feature Vector Data", 
													"Loading Feature Vector Data", 0, 100));
									initializedTasks.add(currentTask);
									if(currentTask.getInstructionPages() != null && !currentTask.getInstructionPages().isEmpty()) {
										initializeInstructionPages(currentTask.getInstructionPages());
									}
								} catch(Exception ex) {
									ex.printStackTrace();
									ErrorDlg.showErrorDialog(experimentPanel.getParentWindow(), ex, true);
								}
							}*/

							//Show a pause phase before starting the task if present
							if(currentTask.getPausePhase() != null) {	
								if(!skipNextPause) {
									final IcarusPausePhase pause = currentTask.getPausePhase();
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
												experimentPanel.setNavButtonFocused(ButtonType.Next);
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
												if(currentTask.getInstructionText() != null) {
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
								SwingUtilities.invokeLater(new Runnable() {
									public void run() {
										experimentPanel.setNavButtonEnabled(ButtonType.Next, true);
										experimentPanel.setNavButtonFocused(ButtonType.Next);
										experimentPanel.setReviewTutorialButtonVisible(false);//examTutorialShown);
										experimentPanel.setConditionNumber(currentTask.getName(),
												currentTask.getConditionNum(), currentTask.getNumTrials());
									}});								

								//First show any instructions for the task, or a screen with a link to the tutorial	
								if(experimentRunning && !isCancelled() && currentTask.isShowInstructionPage()) {									
									//First show the instruction pages if present
									if(currentTask.getInstructionPages() != null && !currentTask.getInstructionPages().isEmpty()) {									
										ArrayList<InstructionsPage> pages = currentTask.getInstructionPages(); 
										int pageIndex = 0;
										SwingUtilities.invokeLater(new Runnable() {
											public void run() {
												experimentPanel.showInternalInstructionPanel();
											}
										});
										while(pageIndex < pages.size() && experimentRunning && !isCancelled()) {
											setNavButtonEnabled(ButtonType.Back, (pageIndex != 0));
											showInstructionsPage(pages.get(pageIndex));

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
									}

									//Now show default instructions or a page with a link to open the tutorial
									setNavButtonEnabled(ButtonType.Back, false);
									if(experimentRunning && !isCancelled()) { 
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

										//Wait for the next button to be pressed
										nextButtonPressed = false;
										while(!nextButtonPressed && experimentRunning && !isCancelled()) {try {Thread.sleep(50);} catch(Exception ex) {}}
									}
								}														
								
								if(experimentRunning && !isCancelled()) {
									//Start the current task
									if(currentTaskController != null) {
										experimentPanel.removeSubjectActionListener(currentTaskController);
										currentTaskController.removeConditionListener(UIStudyController.this);
									}		
									try {
										//Initialize the appropriate task controller
										if(currentTask instanceof Task_1_2_3_5_PhaseBase) {
											task1235Controller.setExam(exam);
											task1235Controller.initializeTaskController((Task_1_2_3_5_PhaseBase<?>)currentTask, conditionPanel);
											currentTaskController = task1235Controller;
										}
										else if(currentTask instanceof Task_4_Phase) {
											task4Controller.setExam(exam);
											task4Controller.initializeTaskController((Task_4_Phase)currentTask, conditionPanel);
											currentTaskController = task4Controller;
										}
										else {
											currentTaskController = null;
										}
									} catch(Exception ex) {
										ex.printStackTrace();
										ErrorDlg.showErrorDialog(experimentPanel.getParentWindow(), ex, true);
									}

									if(currentTaskController != null && experimentRunning && !isCancelled()) {									
										conditionRunning = true;
										//Execute task controller in the event dispatching thread
										SwingUtilities.invokeLater(new Runnable() {
											public void run() {
												experimentPanel.setTrialNumber(1);
												experimentPanel.addSubjectActionListener(currentTaskController);																			
												try {													
													currentTaskController.addConditionListener(UIStudyController.this);
													currentTaskController.startCondition(0, UIStudyController.this, null);
													taskStartTime = System.currentTimeMillis();
												}
												catch (Exception ex) {
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
								while(conditionRunning && experimentRunning && !isCancelled()) {try {Thread.sleep(50);} catch(Exception ex) {}}							

								setExternalInstructionsVisible(false);
								experimentPanel.removeSubjectActionListener(currentTaskController);

								if(experimentRunning && !isCancelled() && currentTaskController != null) {
									currentTaskController.removeConditionListener(UIStudyController.this);
									
									//Save task response data
									UIStudyPhase<?> taskWithResponses = currentTaskController.getTaskWithResponseData();									
									if(taskWithResponses != null && dataRecorder != null && dataRecorder.isEnabled()) {
										taskWithResponses.setStartTime(new Date(taskStartTime));
										taskWithResponses.setEndTime(new Date(System.currentTimeMillis()));
										taskWithResponses.setResponseGenerator(new ResponseGeneratorData(
												subjectData.getSite() != null ? subjectData.getSite().getTag() : null, 
														subjectData.getSubjectId(), true)); 
										try {
											dataRecorder.recordTaskData(subjectData, exam, taskWithResponses);										
										} catch (Exception e) {
											System.err.println("Error, could not write response data to file");
											e.printStackTrace();
											ErrorDlg.showErrorDialog(experimentPanel.getParentWindow(), 
													new Exception("Error, could not write response data to file", e), true);
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
								experimentPanel.showInternalInstructionPanel();
								experimentPanel.setInstructionText(
										InstructionsPanel.formatTextAsHTML("The experiment is complete.  Thank you for participating!"));	
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
	
	protected void initializeInstructionPages(ArrayList<? extends InstructionsPage> pages) {
		if(pages != null && !pages.isEmpty()) {
			for(InstructionsPage page : pages) {
				if(page.getImageURL() != null && page.getPageImage() == null) {
					//Load the image for the page
					try {
						page.setPageImage(
								ImageIO.read(new URL(exam.getOriginalPath(), page.getImageURL())));
					} catch(Exception ex) {
						ex.printStackTrace();
						ErrorDlg.showErrorDialog(experimentPanel.getParentWindow(), 
								new Exception("Error showing tutorial", ex), true);
					}
				}
			}
		}
	}
	
	@Override
	public IcarusExamPhase getCurrentExamPhase() {
		return currentTask;
	}
	
	@Override
	public List<? extends IcarusTestTrial> getCurrentTrials() {
		return currentTask != null ? currentTask.getTestTrials() : null;
	}

	public void restartExperiment() {
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
		}
		else if(event.eventType == SubjectActionEvent.NEXT_BUTTON_PRESSED) {			
			nextButtonPressed = true;
		}
		else if(event.eventType == SubjectActionEvent.HELP_BUTTON_PRESSED) {
			//Show the tutorial for the current task or a help page
			try {
				if(currentTask.getTutorialUrl() != null) {
					showTutorial(new URL(exam.getOriginalPath(), currentTask.getTutorialUrl()),
							currentTask.getName() + " Tutorial");
				}
				else if(currentTask.getInstructionPages() != null) {
					experimentPanel.setTaskInstructionPages(currentTask.getInstructionPages());
					experimentPanel.setExternalInstructionsVisible(true, "Mission Tutorial");
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
				if(exam.getTutorialUrl() != null) {
					showTutorial(new URL(exam.getOriginalPath(), exam.getTutorialUrl()),
					"Exam Tutorial");
				}
				else if(exam.getTutorial() != null) {					
					experimentPanel.setTaskInstructionPages(exam.getTutorial().getTutorialPages());
					experimentPanel.setExternalInstructionsVisible(true, "Exam Tutorial");
				}
			} catch (Exception e) {
				e.printStackTrace();
				ErrorDlg.showErrorDialog(experimentPanel.getParentWindow(), 
						new Exception("The tutorial could not be opened", e), true);
			}
		}
	}
	
	/**
	 * Show a tutorial document.
	 * 
	 * @param tutorialUrl the URL to the tutorial document
	 */
	protected void showTutorial(final URL tutorialUrl, String tutorialName) throws Exception {	
		//Open the tutorial
		Exception error = null;
		boolean openInBrowser = false;
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