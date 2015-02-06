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
package org.mitre.icarus.cps.exam.phase_1;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.app.window.controller.ApplicationController;
import org.mitre.icarus.cps.exam.base.IcarusExam;
import org.mitre.icarus.cps.exam.base.response.ResponseGeneratorData;
import org.mitre.icarus.cps.exam.phase_1.loader.IcarusExamLoader_Phase1;
import org.mitre.icarus.cps.exam.phase_1.testing.TaskTestPhase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_1_Phase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_2_Phase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_3_Phase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_4_Phase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_5_Phase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_6_Phase;
import org.mitre.icarus.cps.exam.phase_1.testing.Task_7_Phase;
import org.mitre.icarus.cps.exam.phase_1.testing.probability_rules.ProbabilityRules;
import org.mitre.icarus.cps.exam.phase_1.training.ProbabilityRulesInstructions;
import org.mitre.icarus.cps.exam.phase_1.training.TutorialPhase;
import org.mitre.icarus.cps.feature_vector.phase_1.GridSize;
import org.mitre.icarus.cps.feature_vector.phase_1.Road;

/**
 * Defines an ICArUS exam for the Phase 1 CPD format.  The exam consists of
 * an ordered list of TaskTestPhases, possibly preceded by a pause phase.
 * The pause phases are for human subject use only.
 * 
 * Response data is also integrated with exam data in the Phase 1 CPD format.  There
 * is no longer a separate response schema.
 * 
 * @author CBONACETO
 *
 */
@XmlRootElement(name="IcarusEvaluation_CPD1", namespace="IcarusCPD_1")
@XmlType(name="IcarusEvaulation_CPD1", namespace="IcarusCPD_1", 
		propOrder={"applicationVersion", "tutorialUrl", "tutorial", "gridSize", 
		"probabilityInstructions", "probabilityRules", "tasks"} )
public class IcarusExam_Phase1 extends IcarusExam<TaskTestPhase<?>> {
	
	/** Application version information. FOR HUMAN SUBJECT USE ONLY. */
	protected String applicationVersion = ApplicationController.VERSION;
	
	/** The URL to the tutorial file for the exam (if any). FOR HUMAN SUBJECT USE ONLY. */
	protected String tutorialUrl;	
	
	/** The tutorial phase for the exam (if any). FOR HUMAN SUBJECT USE ONLY. */
	protected TutorialPhase tutorial;
	
	/** The grid size information (used to translate lat/lon coordinates to grid coordinates) */
	protected GridSize gridSize;
	
	/** The probability rule instructions (if any). FOR HUMAN SUBJECT USE ONLY. */
	protected ProbabilityRulesInstructions probabilityInstructions;
	
	/** The probability rule definitions (PROBS) 
	 * (FOR HUMAN SUBJECT USE ONLY) */	
	protected ProbabilityRules probabilityRules;	
	
	/** The tasks */
	protected ArrayList<TaskTestPhase<?>> tasks;	

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
	 * Get the exam tutorial file URL. FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @return the URL to the tutorial file
	 */
	@XmlElement(name="TutorialUrl")
	public String getTutorialUrl() {
		return tutorialUrl;
	}

	/**
	 * Set the exam tutorial file URL.
	 * FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @param tutorialUrl the URL to the tutorial file
	 */
	public void setTutorialUrl(String tutorialUrl) {
		this.tutorialUrl = tutorialUrl;
	}	
	
	/**
	 * Get the tutorial.	 
	 * FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @return the tutorial
	 */
	@XmlElement(name="Tutorial")
	public TutorialPhase getTutorial() {
		return tutorial;
	}

	/**
	 * Set the tutorial.
	 * FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @param tutorial the tutorial
	 */
	public void setTutorial(TutorialPhase tutorial) {
		this.tutorial = tutorial;
	}

	/**
	 * Get the grid size.
	 * 
	 * @return the grid size
	 */
	@XmlElement(name="GridSize")
	public GridSize getGridSize() {
		return gridSize;
	}

	/**
	 * Set the grid size.
	 * 
	 * @param gridSize the grid size
	 */
	public void setGridSize(GridSize gridSize) {
		this.gridSize = gridSize;
	}	

	/**
	 * Get the probability rule instructions. 
	 * FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @return the instructions
	 */
	@XmlElement(name="ProbabilityRulesInstructions")
	public ProbabilityRulesInstructions getProbabilityInstructions() {
		return probabilityInstructions;
	}

	/**
	 * Set the probability rule instructions. 
	 * FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @param probabilityInstructions the instructions
	 */
	public void setProbabilityInstructions(ProbabilityRulesInstructions probabilityInstructions) {
		this.probabilityInstructions = probabilityInstructions;
	}

	/**
	 * Get the probability rules.
	 * FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @return the probability rules
	 */
	@XmlElement(name="ProbabilityRules")
	public ProbabilityRules getProbabilityRules() {
		return probabilityRules;
	}

	/**
	 * Set the probability rules.
	 * FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @param probabilityRules the probability rules
	 */
	public void setProbabilityRules(ProbabilityRules probabilityRules) {
		this.probabilityRules = probabilityRules;
	}

	/**
	 * Get the tasks in the exam.
	 * 
	 * @return the tasks
	 */
	@XmlElement(name="ExamPhase")
	public ArrayList<TaskTestPhase<?>> getTasks() {
		return tasks;
	}
	
	/**
	 * Set the tasks in the exam.
	 * 
	 * @param tasks the tasks
	 */
	public void setTasks(ArrayList<TaskTestPhase<?>> tasks) {
		this.tasks = tasks;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment_core.Experiment#getConditions()
	 */
	@Override
	@XmlTransient
	public List<TaskTestPhase<?>> getConditions() {
		return tasks;
	}
	
	/**
	 * If the exam contains a Task_3_Phase instance, returns the roads
	 * associated with the first Task_3_Phase instance found.
	 * 
	 * @return
	 */
	public List<Road> getTask3Roads() {		
		if(tasks != null && !tasks.isEmpty()) {
			for(TaskTestPhase<?> task : tasks) {
				if(task instanceof Task_3_Phase) {
					return ((Task_3_Phase) task).getRoads();
				}
			}
		}
		return null;
	}
	
	/** Test main */
	public static void main(String[] args) {
		IcarusExam_Phase1 exam = createSampleExam(false);
		try {
			System.out.println(IcarusExamLoader_Phase1.marshalExam(exam));
			//System.out.println();
			//System.out.println(IcarusExamLoader_Phase1.marshalTask(exam.getTasks().get(0)));
		} catch (JAXBException e) {			
			e.printStackTrace();
		}
	}

	/**
	 * Create a sample exam
	 * 
	 * @param createSampleResponses
	 * @return
	 */
	protected static IcarusExam_Phase1 createSampleExam(boolean createSampleResponses) {
		IcarusExam_Phase1 exam = new IcarusExam_Phase1();
		Calendar currDate = Calendar.getInstance();
		exam.setName("Sample Exam");
		exam.setId("Sample_Exam");
		exam.setExamTimeStamp(currDate.getTime());		
		
		//Create response generator info
		if(createSampleResponses) {
			exam.setResponseGenerator(new ResponseGeneratorData("BED", "1", true));
			currDate.add(Calendar.HOUR, 1);
			exam.setStartTime(currDate.getTime());
			currDate.add(Calendar.HOUR, 3);
			exam.setEndTime(currDate.getTime());
		}		
		
		//Create grid size info
		GridSize gridSize = new GridSize();
		gridSize.setGridWidth(100);
		gridSize.setGridHeight(100);
		gridSize.setMilesPerGridUnit(0.2D);
		gridSize.setBottomLeftLat(0.D);
		gridSize.setBottomLeftLon(0.D);
		exam.setGridSize(gridSize);
		
		//Create sample probability rules
		exam.setProbabilityRules(ProbabilityRules.createDefaultProbabilityRules());
		
		//Create sample phases for Tasks 1-6
		exam.setTasks(new ArrayList<TaskTestPhase<?>>(6));
		if(createSampleResponses) {
			currDate.setTime(exam.getStartTime());
		}
		TaskTestPhase<?> phase = Task_1_Phase.createSampleTask_1_Phase(1, 10, createSampleResponses);
		phase.setExamId(exam.getId());
		if(createSampleResponses) {
			phase.setStartTime(currDate.getTime());
			currDate.add(Calendar.MINUTE, 30);
			phase.setEndTime(currDate.getTime());
		}
		exam.getTasks().add(phase);
		
		phase = Task_2_Phase.createSampleTask_2_Phase(1, 10, createSampleResponses);
		phase.setExamId(exam.getId());
		if(createSampleResponses) {
			phase.setStartTime(currDate.getTime());
			currDate.add(Calendar.MINUTE, 30);
			phase.setEndTime(currDate.getTime());
		}
		exam.getTasks().add(phase);
		
		phase = Task_3_Phase.createSampleTask_3_Phase(1, 10, createSampleResponses);
		phase.setExamId(exam.getId());
		if(createSampleResponses) {
			phase.setStartTime(currDate.getTime());
			currDate.add(Calendar.MINUTE, 30);
			phase.setEndTime(currDate.getTime());
		}
		exam.getTasks().add(phase);
		
		phase = Task_4_Phase.createSampleTask_4_Phase(1, createSampleResponses);
		phase.setExamId(exam.getId());
		if(createSampleResponses) {
			phase.setStartTime(currDate.getTime());
			currDate.add(Calendar.MINUTE, 30);
			phase.setEndTime(currDate.getTime());
		}
		exam.getTasks().add(phase);
		
		phase = Task_5_Phase.createSampleTask_5_Phase(1, createSampleResponses);
		phase.setExamId(exam.getId());
		if(createSampleResponses) {
			phase.setStartTime(currDate.getTime());
			currDate.add(Calendar.MINUTE, 30);
			phase.setEndTime(currDate.getTime());
		}
		exam.getTasks().add(phase);
		
		phase = Task_6_Phase.createSampleTask_6_Phase(1, createSampleResponses);
		phase.setExamId(exam.getId());
		if(createSampleResponses) {
			phase.setStartTime(currDate.getTime());
			currDate.add(Calendar.MINUTE, 30);
			phase.setEndTime(currDate.getTime());
		}
		exam.getTasks().add(phase);
		
		phase = Task_7_Phase.createSampleTask_7_Phase(1, createSampleResponses);
		phase.setExamId(exam.getId());
		if(createSampleResponses) {
			phase.setStartTime(currDate.getTime());
			currDate.add(Calendar.MINUTE, 30);
			phase.setEndTime(currDate.getTime());
		}
		exam.getTasks().add(phase);
		
		return exam;
	}
}