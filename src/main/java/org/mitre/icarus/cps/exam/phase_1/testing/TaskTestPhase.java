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
package org.mitre.icarus.cps.exam.phase_1.testing;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.app.window.controller.ApplicationController;
import org.mitre.icarus.cps.exam.base.pause.IcarusPausePhase;
import org.mitre.icarus.cps.exam.base.testing.IcarusTestPhase;
import org.mitre.icarus.cps.exam.phase_1.feedback.TaskFeedback;

/**
 * Abstract base class for Task 1-7 phases in an exam.
 * 
 * @author CBONACETO
 *
 */
@XmlRootElement(name="TaskTestPhase", namespace="IcarusCPD_1")
@XmlType(name="TaskTestPhase", namespace="IcarusCPD_1",
		propOrder={"applicationVersion", "tutorialUrl", "pausePhase", "taskFeedback"})
@XmlSeeAlso({Task_1_2_3_PhaseBase.class, Task_4_Phase.class, Task_5_Phase.class, Task_6_Phase.class, Task_7_Phase.class})
public abstract class TaskTestPhase<T extends IcarusTestTrial_Phase1> extends IcarusTestPhase<T> {	
	
	/** Application version information. FOR HUMAN SUBJECT USE ONLY. */	
	protected String applicationVersion = ApplicationController.VERSION;	
	
	/** The URL to the tutorial file for the task (if any). FOR HUMAN SUBJECT USE ONLY. */
	protected String tutorialUrl;	
	
	/** Pause phase to show before the test phase (if any, for human subject use only) */
	protected IcarusPausePhase pausePhase;		
	
	/** Feedback provided by the test harness on the subject/model response to the task */
	protected TaskFeedback taskFeedback;
	
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
	 * Get the task tutorial file URL (if any).
	 * FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @return the tutorial file URL 
	 */
	@XmlElement(name="TutorialUrl")
	public String getTutorialUrl() {
		return tutorialUrl;
	}

	/**
	 * Set the task tutorial file URL.
	 * FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @param tutorialUrl URL to the tutorial file
	 */
	public void setTutorialUrl(String tutorialUrl) {
		this.tutorialUrl = tutorialUrl;
	}

	/**
	 * Get the pause phase (if any).
	 * FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @return the pause phase
	 */
	@XmlElement(name="Pause")
	public IcarusPausePhase getPausePhase() {
		return pausePhase;
	}
	
	/**
	 * Set the pause phase.
	 * FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @param pausePhase the pause phase
	 */
	public void setPausePhase(IcarusPausePhase pausePhase) {
		this.pausePhase = pausePhase;
	}	
	
	
	/**
	 * Get the task feedback for the task.
	 * 
	 * @return the task feedback
	 */
	@XmlElement(name="TaskFeedback")
	public TaskFeedback getTaskFeedback() {
		return taskFeedback;
	}

	/**
	 * Set the task feedback for the task.
	 * 
	 * @param taskFeedback the task feedback
	 */
	public void setTaskFeedback(TaskFeedback taskFeedback) {
		this.taskFeedback = taskFeedback;
	}
}