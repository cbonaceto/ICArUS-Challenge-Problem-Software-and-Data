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
package org.mitre.icarus.cps.experiment_core.controller;

import java.util.ArrayList;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import org.mitre.icarus.cps.experiment_core.Experiment;
import org.mitre.icarus.cps.experiment_core.condition.Condition;
import org.mitre.icarus.cps.experiment_core.condition.ConditionConfiguration;
import org.mitre.icarus.cps.experiment_core.condition.IConditionConfigurer;
import org.mitre.icarus.cps.experiment_core.condition.InstructionsPage;
import org.mitre.icarus.cps.experiment_core.event.ConditionEvent;
import org.mitre.icarus.cps.experiment_core.event.SubjectActionEvent;
import org.mitre.icarus.cps.experiment_core.gui.ConditionPanel;
import org.mitre.icarus.cps.experiment_core.gui.IExperimentPanel;
import org.mitre.icarus.cps.experiment_core.gui.InstructionsPanel;
import org.mitre.icarus.cps.experiment_core.gui.INavButtonPanel.ButtonType;
import org.mitre.icarus.cps.experiment_core.subject_data.SubjectConditionData;
import org.mitre.icarus.cps.experiment_core.subject_data.SubjectData;

/**
 * Default IExperimentController implementation. 
 * This class loads, configures, and runs an experiment.
 * 
 * @author cbonaceto
 *
 */
public class BasicExperimentController<E extends Experiment<C>, C extends Condition, CP extends ConditionPanel> 
	implements IExperimentController<E, C, CP> {
	/** The experiment configuration (defines experiment conditions, numbers of trials, etc.) */
	protected E experiment;

	/** The condition configurer (configures each condition and the associated controller) */
	protected final IConditionConfigurer<IExperimentController<E, C, CP>, E, C, CP> conditionConfigurer;

	/** The subject data for the experiment */
	protected SubjectData subjectData;

	/** The experiment panel (i.e., the GUI) */
	protected IExperimentPanel<IExperimentController<E, C, CP>, E, C, CP> experimentPanel;

	/** Whether or not the experiment is currently executing */
	protected boolean experimentRunning = false;

	/** Whether or not a condition is running */
	private boolean conditionRunning = false;

	/** The condition configurations (include the controller and GUI for each condition) */
	protected ArrayList<ConditionConfiguration<IExperimentController<E, C, CP>, E, C, CP>> conditionConfigurations;

	/** The current configuration of the current condition */
	protected ConditionConfiguration<IExperimentController<E, C, CP>, E, C, CP> currentConditionConfiguration; 

	/** The index of the current condition */
	protected int currentConditionIndex;
	protected int currentConditionNum;

	/** The current trial in the current condition */
	protected int currentTrial;

	/** Swing worker object the experiment is executed in */
	private SwingWorker<Object, Object> experimentWorker;
	private boolean exitedWorker = true;

	private boolean backButtonPressed = false;
	private boolean nextButtonPressed = false;	
	
	public BasicExperimentController(IConditionConfigurer<IExperimentController<E, C, CP>, E, C, CP> conditionConfigurer) {
		this.conditionConfigurer = conditionConfigurer;
	}

	public BasicExperimentController(E experiment, IConditionConfigurer<IExperimentController<E, C, CP>, E, C, CP> conditionConfigurer) {
		this.conditionConfigurer = conditionConfigurer;
		initializeExperimentController(experiment);
	}

	public void initializeExperimentController(E experiment) {
		//Stop the currently executing experiment
		stopExperiment();

		this.experiment = experiment;

		//Initialize the conditions by loading their controllers and creating their GUIs
		conditionConfigurations = new ArrayList<ConditionConfiguration<IExperimentController<E, C, CP>, E, C, CP>>();
		for(C condition : experiment.getConditions()) {
			conditionConfigurations.add(conditionConfigurer.configureCondition(condition));
		}	
	}

	public SubjectData getSubjectData() {
		return subjectData;
	}

	public IExperimentPanel<IExperimentController<E, C, CP>, E, C, CP> getExperimentPanel() {
		return experimentPanel;
	}

	public void setExperimentPanel(IExperimentPanel<IExperimentController<E, C, CP>, E, C, CP> experimentPanel) {
		this.experimentPanel = experimentPanel;
		experimentPanel.addSubjectActionListener(this);
	}

	public boolean isExperimentRunning() {
		return experimentRunning;
	}

	public ArrayList<ConditionConfiguration<IExperimentController<E, C, CP>, E, C, CP>> getConditionConfigurations() {
		return conditionConfigurations;
	}	

	/** Start the experiment */
	public void startExperiment(final SubjectData subjectData) {
		if(experiment == null || experimentPanel == null) {
			throw new IllegalArgumentException("Error starting experiment controller: experiment and experiment panel must be set");
		}

		//Stop the currently executing experiment
		stopExperiment();		

		this.subjectData = subjectData;
		experimentPanel.setSubject(subjectData.getSubjectId());

		//Execute the experiment in a background thread		
		experimentWorker = new SwingWorker<Object, Object>() {
			@Override
			protected Object doInBackground() throws Exception {
				/** Wait until the previously executing experiment worker finishes executing */
				while(!exitedWorker) {
					try {
						Thread.sleep(50); //System.out.println("waiting for worker to exit...");
					} catch(InterruptedException ex) {}
				}

				experimentRunning = true;
				exitedWorker = false;
				//currentConditionNum = subjectData.getCurrentCondition();
				//currentConditionIndex = currentConditionNum;

				int startIndex = subjectData.getCurrentCondition();
				currentConditionIndex = 0;
				currentConditionNum = 0;
				currentTrial = 1;				

				//for(ConditionConfiguration conditionConfiguration : conditionConfigurations) {
				//for(currentConditionIndex = 0; currentConditionIndex < conditionConfigurations.size(); currentConditionIndex++) {
				while(experimentRunning && currentConditionIndex < conditionConfigurations.size()) {

					currentConditionConfiguration = conditionConfigurations.get(currentConditionIndex);
					if(!currentConditionConfiguration.condition.isCountCondition()) {
						if(currentConditionNum > 0) {
							currentConditionNum--;
						}
					}
					else {
						currentConditionNum++;
					}
					//System.out.println(experimentRunning + ", " + currentConditionIndex + ", " + startIndex);
					experimentPanel.clearCurrentConfiguration();

					if(experimentRunning && currentConditionIndex >= startIndex) {
						//System.out.println("starting at: " + startIndex);
						conditionRunning = false;
						Condition currentCondition = currentConditionConfiguration.condition;

						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								experimentPanel.setNavButtonEnabled(ButtonType.Back, false);
								experimentPanel.setNavButtonEnabled(ButtonType.Next, true);
								experimentPanel.setNavButtonVisible(ButtonType.Help, false);
								experimentPanel.setConditionNumber(currentConditionNum, 
										currentConditionConfiguration.condition.getNumTrials());							
							}});						

						if(experimentRunning && currentConditionConfiguration.condition.isShowInstructionPage()) {
							if(currentCondition.isShowInstructionPage()) {
								if(currentCondition.getInstructionPages() == null) {
									//Show default instructions if the pages are null
									SwingUtilities.invokeLater(new Runnable() {
										public void run() {
											experimentPanel.setInstructionText("<html><br><br><center><font face=\"arial\" size=\"6\">Click Next to begin " +
													currentConditionConfiguration.condition.getName() + "</font></center></html>");
											experimentPanel.showInternalInstructionPanel();
										}
									});

									//Wait for the next button to be pressed
									nextButtonPressed = false;
									while(!nextButtonPressed && experimentRunning) {try {Thread.sleep(50);} catch(Exception ex) {}}
								}
								else {
									//Show the instructions page(s)
									ArrayList<InstructionsPage> pages = currentCondition.getInstructionPages(); 
									int pageIndex = 0;
									SwingUtilities.invokeLater(new Runnable() {
										public void run() {
											experimentPanel.showInternalInstructionPanel();
										}
									});
									while(pageIndex < pages.size() && experimentRunning) {
										setNavButtonEnabled(ButtonType.Back, (pageIndex != 0));
										showInstructionsPage(pages, pageIndex);

										//Wait for the back or next button to be pressed
										backButtonPressed = false;
										nextButtonPressed = false;
										while((!backButtonPressed && !nextButtonPressed) && experimentRunning) {
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

						if(experimentRunning) {
							//Start the current condition
							conditionRunning = true;
							SwingUtilities.invokeLater(new Runnable() {
								public void run() {
									experimentPanel.setNavButtonVisible(ButtonType.Help, currentConditionConfiguration.condition.isShowInstructionPage());
									experimentPanel.setNavButtonEnabled(ButtonType.Help, currentConditionConfiguration.condition.isShowInstructionPage());
									if(currentConditionConfiguration.condition.isCountCondition()) {
										experimentPanel.setTrialNumber(1);
									}
									experimentPanel.showCondition(currentConditionConfiguration);
								}});

							experimentPanel.addSubjectActionListener(currentConditionConfiguration.controller);
							SubjectConditionData subjectConditionData = 
								subjectData.startCondition(currentConditionConfiguration.condition);
							currentConditionConfiguration.controller.addConditionListener(BasicExperimentController.this);					
							currentConditionConfiguration.controller.startCondition(1, BasicExperimentController.this,
									subjectConditionData);
						}

						//Wait for the condition to complete
						while(conditionRunning && experimentRunning) {try {Thread.sleep(50);} catch(Exception ex) {}}

						setExternalInstructionsVisible(false, false);

						if(experimentRunning) {
							experimentPanel.removeSubjectActionListener(currentConditionConfiguration.controller);
							currentConditionConfiguration.controller.removeConditionListener(BasicExperimentController.this);
						}
					}
					currentConditionIndex++;
				} //while

				if(experimentRunning) {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							experimentPanel.setNavButtonEnabled(ButtonType.Back, false);
							experimentPanel.setNavButtonEnabled(ButtonType.Next, false);
							experimentPanel.showInternalInstructionPanel();
							if(experiment.isShowScore()) {
								experimentPanel.setInstructionText(
										InstructionsPanel.formatTextAsHTML("The experiment is complete.  Your overall score was: " + Math.round(subjectData.getScore()) +  "%. Thank you for participating!"));
							}
							else {
								experimentPanel.setInstructionText(
										InstructionsPanel.formatTextAsHTML("The experiment is complete.  Thank you for participating!"));	
							}							
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

	/** Stop the experiment */
	public void stopExperiment() {
		if(experimentRunning && currentConditionConfiguration != null) {
			currentConditionConfiguration.controller.stopCondition();	
			experimentPanel.removeSubjectActionListener(currentConditionConfiguration.controller);
			currentConditionConfiguration.controller.removeConditionListener(this);
		}

		if(experimentWorker != null) {
			experimentWorker.cancel(true);
			experimentWorker = null;
		}

		experimentRunning = false;
		conditionRunning = false;
		currentConditionNum = -1;
		//currentConditionIndex = -1;
		currentConditionConfiguration = null;
	}

	/** Go to the beginning of the given condition */
	public void skipToCondition(int conditionIndex) {
		if(subjectData != null) {
			subjectData.setCurrentCondition(conditionIndex);
		}

		//if(experimentRunning) {
		//Restart the experiment at the current condition
		//System.out.println("skipping to: " + conditionIndex);
		stopExperiment();
		startExperiment(subjectData);
		//}
	}

	public void setExternalInstructionsVisible(final boolean visible, final boolean buttonEnabled) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {							
				experimentPanel.setExternalInstructionsVisible(visible);
				experimentPanel.setNavButtonEnabled(ButtonType.Help, buttonEnabled);
			}
		});
	}

	protected void showInstructionsPage(final ArrayList<InstructionsPage> pages, final int pageIndex) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {							
				experimentPanel.setInstructionsPage(pages.get(pageIndex));
			}
		});
	}

	public void setNavButtonVisible(final ButtonType button, final boolean visible) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				experimentPanel.setNavButtonVisible(button, visible);
			}
		});
	}

	public void setNavButtonEnabled(final ButtonType button, final boolean enabled) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				experimentPanel.setNavButtonEnabled(button, enabled);
			}
		});
	}

	@Override
	public void conditionActionPerformed(ConditionEvent event) {
		if(event.eventType == ConditionEvent.TRIAL_CHANGED) {
			//Set the trial number in the status panel
			experimentPanel.setTrialNumber(currentConditionConfiguration.condition.getCurrentTrial());
		}	
		else if(event.eventType == ConditionEvent.CONDITION_COMPLETED) {
			//The condition was completed
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
			experimentPanel.setExternalInstructionsVisible(true);
		}
	}	
}