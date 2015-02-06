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
package org.mitre.icarus.cps.app.experiment.ui_study.exam;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.base.IcarusExam;

/**
 * @author CBONACETO
 *
 */
@XmlRootElement(name="IcarusEvaluation", namespace="IcarusUIStudy")
@XmlType(name="IcarusEvaluation", namespace="IcarusUIStudy")
public class UIStudyExam extends IcarusExam<UIStudyPhase<?>> {
	
	/** The URL to the tutorial file for the exam (if any). */
	protected String tutorialUrl;	
	
	/** The tutorial phase for the exam (if any). */
	protected TutorialPhase tutorial;
	
	/** The UI study tasks */
	protected ArrayList<UIStudyPhase<?>> tasks;
	
	/**
	 * Get the exam tutorial file URL.
	 * 
	 * @return the URL to the tutorial file
	 */
	@XmlElement(name="TutorialUrl")
	public String getTutorialUrl() {
		return tutorialUrl;
	}

	/**
	 * Set the exam tutorial file URL.
	 * 
	 * @param tutorialUrl the URL to the tutorial file
	 */
	public void setTutorialUrl(String tutorialUrl) {
		this.tutorialUrl = tutorialUrl;
	}	
	
	/**
	 * Get the tutorial.
	 * 
	 * @return the tutorial
	 */
	@XmlElement(name="Tutorial")
	public TutorialPhase getTutorial() {
		return tutorial;
	}

	/**
	 * Set the tutorial.
	 * 
	 * @param tutorial the tutorial
	 */
	public void setTutorial(TutorialPhase tutorial) {
		this.tutorial = tutorial;
	}

	@XmlElement(name="ExamPhase")
	public ArrayList<UIStudyPhase<?>> getTasks() {
		return tasks;
	}

	public void setTasks(ArrayList<UIStudyPhase<?>> tasks) {
		this.tasks = tasks;
	}

	@Override
	public List<UIStudyPhase<?>> getConditions() {
		return tasks;
	}	
}