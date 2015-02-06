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
package org.mitre.icarus.cps.app.experiment.phase_05;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.mitre.icarus.cps.app.experiment.IcarusExamController;
import org.mitre.icarus.cps.app.experiment.IcarusSubjectData;
import org.mitre.icarus.cps.app.experiment.phase_05.ExamTiming;
import org.mitre.icarus.cps.app.widgets.WidgetConstants;
import org.mitre.icarus.cps.app.widgets.phase_05.experiment.ConditionPanel_Phase05;
import org.mitre.icarus.cps.app.widgets.phase_05.experiment.ExperimentPanel_Phase05;
import org.mitre.icarus.cps.exam.base.IcarusExamPhase;
import org.mitre.icarus.cps.exam.base.pause.IcarusPausePhase;
import org.mitre.icarus.cps.exam.base.testing.IcarusTestTrial;
import org.mitre.icarus.cps.exam.phase_05.IcarusExam_Phase05;
import org.mitre.icarus.cps.exam.phase_05.loader.IcarusExamLoader_Phase05;
import org.mitre.icarus.cps.exam.phase_05.response.IcarusTestPhaseResponse_Phase05;
import org.mitre.icarus.cps.exam.phase_05.response.IcarusTrainingPhaseResponse;
import org.mitre.icarus.cps.exam.phase_05.testing.IcarusTestPhase_Phase05;
import org.mitre.icarus.cps.exam.phase_05.training.IcarusTrainingPhase;
import org.mitre.icarus.cps.experiment_core.condition.InstructionsPage;
import org.mitre.icarus.cps.experiment_core.event.ConditionEvent;
import org.mitre.icarus.cps.experiment_core.event.SubjectActionEvent;
import org.mitre.icarus.cps.experiment_core.gui.InstructionsPanel;
import org.mitre.icarus.cps.experiment_core.gui.INavButtonPanel.ButtonType;
import org.mitre.icarus.cps.experiment_core.subject_data.SubjectData;

/**
 * Controller for entire exam. Shows blocks of training and test phases, as
 * well as pauses.  Training blocks are executed by a TrainingPhaseController, 
 * and test blocks are executed by a TestPhaseController.
 * 
 * @author CBONACETO
 *
 */
public class IcarusExamController_Phase05 extends 
	IcarusExamController<IcarusExam_Phase05, IcarusExamPhase, IcarusTrainingPhase,
	ExperimentPanel_Phase05, ConditionPanel_Phase05> implements HyperlinkListener {
	
	/** The current condition controller */
	protected IcarusConditionController_Phase05<? extends IcarusExamPhase> currentConditionController;
	
	private boolean backButtonPressed_timing = false;
	
	private boolean tutorialShown = false;
	
	/** The current phase in the exam */
	protected IcarusExamPhase currentPhase;
	
    /** The exam timing instance */
    static ExamTiming examTiming = new ExamTiming();
    
    /** The phase name (training or test) */
    private String phaseName = "";
    
    /** The number of trials of the current phase, used for timing purposes */
    private int numTrials = 0;
    
    /** The current trial number we are in, used for timing purposes */
    private int trialNum = 0;
    
    /** A count of the 'next' subject action events, used for timing purposes */
    private int testSAECnt = 0;
    
	public ExamTiming getExamTiming() {
		return examTiming;
	}

	public IcarusExamController_Phase05(ConditionPanel_Phase05 conditionPanel) {
		this.conditionPanel = conditionPanel;
	}

	@Override
	public void initializeExperimentController(IcarusExam_Phase05 experiment) {		
		tutorialShown = false;
		
		//Stop the currently executing exam
		stopExperiment();

		this.exam = experiment;
		experimentPanel.setExperimentName(exam.getName());		
		
		if(exam.getConditions() != null) {
			//Number the phases (some phases may not be counted)
			int currPhase = 0;
			for(IcarusExamPhase phase : exam.getConditions()) {
				if(!phase.isCountCondition() || phase instanceof IcarusPausePhase) {
					phase.setConditionNum(-1);
				}
				else {
					phase.setConditionNum(currPhase);
					currPhase++;
				}
			}			
			//experimentPanel.setNumConditions(exam.getConditions().size());
			experimentPanel.setNumConditions(currPhase-1);
		}
		else {
			experimentPanel.setNumConditions(0);
		}
	}

	@Override
	public void startExperiment(final SubjectData subjectData) {
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
				if(currentConditionController != null) {
					experimentPanel.removeSubjectActionListener(currentConditionController);
					currentConditionController.removeConditionListener(IcarusExamController_Phase05.this);
					currentConditionController.stopCondition();
				}
				//experimentRunning = false;		
				exitedWorker = true;
			}

			@Override
			protected Object doInBackground() throws Exception {
				/** Wait until the previously executing experiment worker finishes executing */
				//TODO: Add a timeout time before quitting
				while(!exitedWorker && !isCancelled()) {
					try {
						Thread.sleep(50); 
					} catch(InterruptedException ex) {}
				}

				IcarusExamController_Phase05.this.subjectData = (IcarusSubjectData)subjectData;
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
				currentPhase = null;
				
				//Create controllers for test and training phases
				TestPhaseController testController = new TestPhaseController(exam);
				TrainingPhaseController trainingController = new TrainingPhaseController(exam);				

				while(experimentRunning && currentConditionIndex < exam.getPhases().size() && !isCancelled()) {					
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
						
						if(currentConditionIndex == 0 && exam.getTutorialUrl() != null && !tutorialShown) {
							//Show the tutorial page and wait for a next button press							
							if(experimentRunning) {
								tutorialShown = true;
								SwingUtilities.invokeLater(new Runnable() {
									public void run() {
										experimentPanel.setConditionNumber("Tutorial", -1, 0);
										experimentPanel.setInstructionText(
												InstructionsPanel.formatTextAsHTML(
														"<a href=\"open_tutorial\">Click here</a> to open the tutorial.<br>" +
														"After you have read the tutorial, click Next to continue.<br>" +
														"You may review the tutorial at any time by clicking the Review Tutorial button."));
										experimentPanel.showInternalInstructionPanel();
										experimentPanel.setNavButtonEnabled(ButtonType.Next, true);									
									}});
								nextButtonPressed = false;
								while(!nextButtonPressed && experimentRunning && !isCancelled()) {try {Thread.sleep(50);} catch(Exception ex) {}}
							}
						}
						
						if(!experimentRunning || isCancelled()) {
							return null;
						}
						
						currentPhase = exam.getPhases().get(currentConditionIndex);
						
						if(currentPhase instanceof IcarusPausePhase) {							
							if(!((IcarusPausePhase)currentPhase).isShowCountdown()) {
								//Just show an instruction screen with instruction text from the phase
								SwingUtilities.invokeLater(new Runnable() {
									public void run() {
										//experimentPanel.setConditionNumber(currentPhase.getName(),
										//		currentConditionIndex+1, currentPhase.getNumTrials());
										experimentPanel.setConditionNumber(currentPhase.getName(),
												currentPhase.getConditionNum(), currentPhase.getNumTrials());
										experimentPanel.setInstructionText(InstructionsPanel.formatTextAsHTML(
												currentPhase.getInstructionText(), "center", 
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
								//Execute break phase
								SwingUtilities.invokeLater(new Runnable() {
									public void run() {										
										//experimentPanel.setConditionNumber(currentPhase.getName(),
										//		currentConditionIndex+1, currentPhase.getNumTrials());
										experimentPanel.setConditionNumber(currentPhase.getName(),
												currentPhase.getConditionNum(), currentPhase.getNumTrials());
										conditionPanel.showBreakScreen(((IcarusPausePhase)currentPhase).isShowCountdown());
										conditionPanel.setBreakOver(false);
										if(currentPhase.getInstructionText() != null) {
											conditionPanel.setBreakText(currentPhase.getInstructionText());
										}
										experimentPanel.showConditionPanel();
									}});						

								//Pause for the length of the break phase and show the countdown timer
								final long pauseLength = ((IcarusPausePhase)currentPhase).getLength_seconds()*1000;
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
						else {
							//Execute a test or training phase						
							//TODO: Pause before allowing next click
							//System.out.println("eao 1 num trials= " + currentPhase.getNumTrials());  // eao
							numTrials = currentPhase.getNumTrials();  // eao
							trialNum = 0;  // eao
							SwingUtilities.invokeLater(new Runnable() {
								public void run() {
									experimentPanel.setNavButtonEnabled(ButtonType.Next, true);
									if(tutorialShown) {
										experimentPanel.setReviewTutorialButtonVisible(true);
									}
									//experimentPanel.setConditionNumber(currentPhase.getName(),
									//		currentConditionIndex+1, currentPhase.getNumTrials());							
									experimentPanel.setConditionNumber(currentPhase.getName(),
											currentPhase.getConditionNum(), currentPhase.getNumTrials());
								}});						

							if(experimentRunning && currentPhase.isShowInstructionPage() && !isCancelled()) {
								if(currentPhase.isShowInstructionPage()) {
									if(currentPhase.getInstructionPages() == null) {
										//Show default instructions if the pages are null
										SwingUtilities.invokeLater(new Runnable() {
											public void run() {
												if(currentPhase.getInstructionText() != null) {
													if(currentPhase.getName() == null) {
														experimentPanel.setInstructionText(
																InstructionsPanel.formatTextAsHTML(currentPhase.getInstructionText() + "<br><br>Click Next to begin Phase " + Integer.toString(currentConditionIndex+1) + "."));
													}
													else {
														experimentPanel.setInstructionText(
																InstructionsPanel.formatTextAsHTML(currentPhase.getInstructionText() + "<br><br>Click Next to begin " + currentPhase.getName() + "."));
													}													
												}
												else {
													if(currentPhase.getName() == null) {
														experimentPanel.setInstructionText(
																InstructionsPanel.formatTextAsHTML("Click Next to begin Phase " + Integer.toString(currentConditionIndex+1) + "."));
													}
													else {
														experimentPanel.setInstructionText(
																InstructionsPanel.formatTextAsHTML("Click Next to begin " + currentPhase.getName() + "."));
													}
												}												
												experimentPanel.showInternalInstructionPanel();
											}
										});

										//Wait for the next button to be pressed
										nextButtonPressed = false;
										while(!nextButtonPressed && experimentRunning && !isCancelled()) {try {Thread.sleep(50);} catch(Exception ex) {}}
									}
									else {
										//Show the instructions page(s)
										ArrayList<InstructionsPage> pages = currentPhase.getInstructionPages(); 
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
											}
											else {
												pageIndex++;
											}	
										}
									}
								}
							}												

							if(experimentRunning && !isCancelled()) {
								//Start the current phase
								if(currentConditionController != null) {
									experimentPanel.removeSubjectActionListener(currentConditionController);
									currentConditionController.removeConditionListener(IcarusExamController_Phase05.this);
								}
								
								SwingUtilities.invokeLater(new Runnable() {
									public void run() {
										if(currentPhase instanceof IcarusTestPhase_Phase05 &&
												!experimentPanel.rulesEmpty()) {
											//Allow subject to review training rules
											experimentPanel.setNavButtonVisible(ButtonType.Help, currentPhase.isShowInstructionPage());
										}
										experimentPanel.setTrialNumber(1);
										experimentPanel.showConditionPanel();
									}});
								if(currentPhase instanceof IcarusTestPhase_Phase05) {
									phaseName = "TEST";
									currentConditionController = testController;
									testController.initializeConditionController((IcarusTestPhase_Phase05)currentPhase, conditionPanel);
								}
								else if(currentPhase instanceof IcarusTrainingPhase) {
									phaseName = "TRAINING";
									currentConditionController = trainingController;
									trainingController.initializeConditionController((IcarusTrainingPhase)currentPhase, conditionPanel);
									experimentPanel.addRulesFromTraining((IcarusTrainingPhase)currentPhase);
								}
								else {
									currentConditionController = null;
								}

								if(currentConditionController != null && experimentRunning && !isCancelled()) {
									conditionRunning = true;
									//Execute phase controller in the event dispatching thread
									SwingUtilities.invokeLater(new Runnable() {
										public void run() {
											experimentPanel.addSubjectActionListener(currentConditionController);
											try {												
												currentConditionController.addConditionListener(IcarusExamController_Phase05.this);
												currentConditionController.startCondition(0, IcarusExamController_Phase05.this, null);
											}
											catch (Exception ex) {
												ex.printStackTrace();
												try {
													currentConditionController.stopCondition();
												} catch(Exception e) {}
												conditionRunning = false;
											}
										}});
								}
							}

							//Wait for the current phase to complete
							while(conditionRunning && experimentRunning && !isCancelled()) {try {Thread.sleep(50);} catch(Exception ex) {}}
							
							setExternalInstructionsVisible(false);
							experimentPanel.removeSubjectActionListener(currentConditionController);
							currentConditionController.removeConditionListener(IcarusExamController_Phase05.this);
							
							//Save response data 							
							if(experimentRunning && !isCancelled() && currentConditionController != null &&     // eao
									currentConditionController == trainingController) {
								//Save training phase data								
								final IcarusTrainingPhaseResponse responseData = trainingController.getResponseData(currentConditionIndex, numTrials, examTiming);  // eao
								
								//String fileName = "data/response_data/S" + subjectData.getSubjectId() + "_" +
								//	exam.getName() + "_Phase" + (currentConditionIndex+1) + "_training.xml";
								String fileName = "data/response_data/S" + subjectData.getSubjectId() + "_" +
									exam.getName() + "_Phase" + (currentPhase.getConditionNum()) + "_training.xml";
								String xml = IcarusExamLoader_Phase05.marshalExamPhaseResponse(responseData);								
								
								try {
								    BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
								    out.write(xml);
								    out.close();
								} catch (IOException e) {
									System.err.println("Error, could not write response data to file");
									e.printStackTrace();
								}
							}
							
							if(experimentRunning && !isCancelled() && currentConditionController != null &&
									currentConditionController == testController) {
								//Save test phase data
								final IcarusTestPhaseResponse_Phase05 responseData = testController.getResponseData(currentConditionIndex,examTiming);  // eao
								//String fileName = "data/response_data/S" + subjectData.getSubjectId() + "_" +
								//	exam.getName() + "_Phase" + (currentConditionIndex+1) + ".xml";
								String fileName = "data/response_data/S" + subjectData.getSubjectId() + "_" +
									exam.getName() + "_Phase" + (currentPhase.getConditionNum()) + ".xml";
								String xml = IcarusExamLoader_Phase05.marshalExamPhaseResponse(responseData);								
								
								try {
								    BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
								    out.write(xml);
								    out.close();
								} catch (IOException e) {
									System.err.println("Error, could not write response data to file");
									e.printStackTrace();
								}
								
								//Save timing data  - eao
								//String timingFileName = "data/response_data/S" + subjectData.getSubjectId() + "_" +
								//exam.getName() + "_Phase" + (currentConditionIndex+1) + "timing";
								//examTiming.saveTimingToFile(timingFileName);
								
								if(currentPhase.isShowScore() && responseData.getAverageScore() != null) {
									//Show the score for the test phase
									//System.out.println("score: " + responseData.getAverageScore());
									if(experimentRunning) {
										SwingUtilities.invokeLater(new Runnable() {
											public void run() {
												experimentPanel.setInstructionText(
														InstructionsPanel.formatTextAsHTML("Your score on the last phase was " + Math.round(responseData.getAverageScore()) +  "%.<br><br>Click Next to continue."));
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
			}
		};		
		
		experimentWorker.execute();
	}	
	
	@Override
	public IcarusExamPhase getCurrentExamPhase() {
		return currentPhase;
	}

	@Override
	public List<? extends IcarusTestTrial> getCurrentTrials() {
		if(currentPhase != null && currentPhase instanceof IcarusTestPhase_Phase05) {
			return ((IcarusTestPhase_Phase05)currentPhase).getTestTrials();
		}
		return null;
	}

	@Override
	public void restartExperiment() {
		stopExperiment();
		if(subjectData == null) {
			subjectData = new IcarusSubjectData(null, null, 0);
		} else {
			subjectData.setCurrentCondition(0);
		}
		startExperiment(subjectData);
	}
	
	@Override
	public void skipToCondition(int conditionIndex) {
		//Restart the experiment at the current condition
		stopExperiment();
		if(subjectData == null) {
			subjectData = new IcarusSubjectData(null, null, conditionIndex);
		} else {
			subjectData.setCurrentCondition(conditionIndex);
		}
		startExperiment(subjectData);
	}	
	
	@Override
	public void stopExperiment() {
		experimentRunning = false;
		conditionRunning = false;
		currentConditionIndex = -1;
		
		if(currentConditionController != null) {
			currentConditionController.stopCondition();	
			experimentPanel.removeSubjectActionListener(currentConditionController);
			currentConditionController.removeConditionListener(this);
		}

		if(experimentWorker != null) {
			experimentWorker.cancel(true);
			experimentWorker = null;
		}
	}

	@Override
	public ExperimentPanel_Phase05 getExperimentPanel() {
		return experimentPanel;
	}	

	@Override
	public void setExperimentPanel(ExperimentPanel_Phase05 experimentPanel) {
		this.experimentPanel = experimentPanel;
		this.experimentPanel.removeSubjectActionListener(this);
		this.experimentPanel.addSubjectActionListener(this);
		this.experimentPanel.removeInstructionsHyperlinkListener(this);
		this.experimentPanel.addInstructionsHyperlinkListener(this);
	}
	
	@Override
	public void conditionActionPerformed(ConditionEvent event) {
		if(event.eventType == ConditionEvent.TRIAL_CHANGED) {
			if(debug) {
				System.out.println("IcarusExamController.conditionActPerformed: ConditionEvent.TRIAL_CHANGED");  // eao
				System.out.println("IcarusExamController.conditionActPerformed: phase " + getCurrentConditionIndex() + " event.trialNum= " + event.trialNum + " trialNum " + trialNum);  // eao
				System.out.println("IcarusExamController.conditionActPerformed: backbuttonpressed_timing= " + backButtonPressed_timing);
			}
			trialNum = event.trialNum;
			if((event.trialNum -1 != -1) && (backButtonPressed_timing != true)){
				//System.out.println("IcarusExamController.conditionActPerformed: stopTiming: trial= " + (event.trialNum - 1));
				examTiming.stopTimingInstance(getCurrentConditionIndex(), event.trialNum - 1);
				testSAECnt = 0;
			}
			examTiming.createTimingInstance(phaseName, getCurrentConditionIndex(), event.trialNum);
			backButtonPressed_timing = false;
			
			//Set the trial number in the status panel
			experimentPanel.setTrialNumber(event.trialNum+1);
		}	
		else if(event.eventType == ConditionEvent.CONDITION_COMPLETED) {
			if(debug) {
				System.out.println("IcarusExamController.conditionActPerformed: ConditionEvent.CONDITION_COMPLETED");  // eao
			}
			examTiming.stopTimingInstance(getCurrentConditionIndex(), trialNum);  // eao
			//The condition was completed
			conditionRunning = false;			
		}
	}

	@Override
	public void subjectActionPerformed(SubjectActionEvent event) {
		if(event.eventType == SubjectActionEvent.BACK_BUTTON_PRESSED) {
			if(debug) {
				System.out.println("IcarusExamController.subjectActionPerformed: -- back button phase " + getCurrentConditionIndex() + " trialNum= " + trialNum);  // eao
			}
			if(phaseName.equals("TRAINING")){
				examTiming.stopTimingInstance(getCurrentConditionIndex(), trialNum);  // eao
				backButtonPressed_timing = true;  // eao
			}
			backButtonPressed = true;
			
		}
		else if(event.eventType == SubjectActionEvent.NEXT_BUTTON_PRESSED) {
			if(debug) {
				System.out.println("IcarusExamController.subjectActionPerformed.NEXT_BUTTON_PRESSED"); // eao
				System.out.println("IcarusExamController.subjectActionPerformed trialNum= " + trialNum + " testSAECnt= " + testSAECnt);  // eao
				System.out.println("IcarusExamController.subjectActionPerformed: phase " + getCurrentConditionIndex() + " phaseName= " + phaseName);  // eao
				System.out.println("IcarusExamController.subjectActionPerformed numTrials= " + numTrials);  // eao
			}
			if(phaseName.equals("TEST") && (trialNum == numTrials -1)){
				if(testSAECnt == 1){
					//System.out.println("IcarusExamController.subjectActionPerformed stopTiming 1");
					examTiming.stopTimingInstance(getCurrentConditionIndex(), trialNum);
					testSAECnt = 0;
				}
				else{
					testSAECnt++;
				}
			}
			else if(phaseName.equals("TRAINING")){	
				if(trialNum == numTrials -1){
					examTiming.stopTimingInstance(getCurrentConditionIndex(), trialNum);
					trialNum = 0;
				}
			}
			
			nextButtonPressed = true;
		}
		else if(event.eventType == SubjectActionEvent.HELP_BUTTON_PRESSED) {
			experimentPanel.setExternalInstructionsVisible(true);
		}
		else if(event.eventType == SubjectActionEvent.CUSTOM_BUTTON_PRESSED) {
			showTutorial();
		}
	}

	@Override
	public void hyperlinkUpdate(HyperlinkEvent e) {
		if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			if(e.getDescription().equalsIgnoreCase("open_tutorial")) {
				showTutorial();
			}
		}
	}
	
	/** Open the tutorial using the Destkop API */
	protected void showTutorial() {
		SwingWorker<Object, Object> worker = new SwingWorker<Object, Object>() {
			@Override
			protected Object doInBackground() throws Exception {
				//Open the tutorial
				Exception error = null;
				if(Desktop.isDesktopSupported()) {
					try {
						Desktop desktop = Desktop.getDesktop();
						URL tutorialUrl = new URL(exam.getOriginalPath(), exam.getTutorialUrl());
						desktop.open(new File(tutorialUrl.toURI()));						
					} catch(Exception ex) {
						error = ex;
					}
				}
				else {
					error = new Exception("Desktop API not supported");
				}
				
				if(error != null) {
					error.printStackTrace();
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							JOptionPane.showMessageDialog(experimentPanel,
									"Error, the tutorial could not be opened.", 
									"Error Occured", 
									JOptionPane.ERROR_MESSAGE);
						}});
					
				}						
				return null;
			}					
		};	
		worker.execute();
	}
}