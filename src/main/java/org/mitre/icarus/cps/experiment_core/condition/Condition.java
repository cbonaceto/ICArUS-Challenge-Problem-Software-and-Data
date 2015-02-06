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
package org.mitre.icarus.cps.experiment_core.condition;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * Abstract base class for a condition in an experiment.
 * 
 * @author CBONACETO
 *
 */
@XmlRootElement(name="Condition")
@XmlType(name="Condition")
public abstract class Condition {
	
	/** The condition name */
	protected String name;
	
	/** The condition number */
	protected Integer conditionNum;
	
	/** The current trial */
	protected int currentTrial;
	
	/** The instructions page(s) for the condition */
	protected ArrayList<InstructionsPage> instructionPages;
	
	/** The time spent reviewing the instructions page(s) (in milliseconds) */
	protected Long instructionsTime_ms;
	
	/** Whether or not to show the condition's instruction page(s)
	 * before the condition is started  */
	protected Boolean showInstructionPage = true;
	
	/** Whether or not to show the subject's score when the condition is finished */
	protected Boolean showScore = false;
	
	/** Whether or not to count the condition in the condition counter display */
	protected Boolean countCondition = true;

	/**
	 * Get the condition name.
	 * 
	 * @return
	 */
	@XmlAttribute(name="name")
	public String getName() {
		return name;
	}

	/**
	 * Set the condition name.
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the condition number.
	 * 
	 * @return
	 */
	@XmlElement(name="ConditionNum", required=false)
	public Integer getConditionNum() {
		return conditionNum;
	}

	/**
	 * Set the condition number.
	 * 
	 * @param conditionNum
	 */
	public void setConditionNum(Integer conditionNum) {
		this.conditionNum = conditionNum;
	}

	/**
	 * Get the number of trials in the condition.
	 * 
	 * @return the number of trials
	 */
	public abstract int getNumTrials();	

	/**
	 * Get the current trial being executed.
	 * 
	 * @return
	 */
	@XmlTransient
	public int getCurrentTrial() {
		return currentTrial;
	}

	/**
	 * Set the current trial being executed.
	 * 
	 * @param currentTrial
	 */
	public void setCurrentTrial(int currentTrial) {				
		this.currentTrial = currentTrial;
	}
	
	/**
	 * Increment the current trial by 1.
	 */
	public void incrementCurrentTrial() {
		currentTrial++;
	}

	/**
	 * Get the instructions page(s) for the condition. 
	 * FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @return the instructions page(s) for the condition
	 */
	@XmlElementWrapper(name="InstructionPages")
	@XmlElement(name="InstructionsPage")
	public ArrayList<InstructionsPage> getInstructionPages() {
		return instructionPages;
	}

	/**
	 * Set the instructions page(s) for the condition. 
	 * FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @param instructionPages the instructions page(s) for the condition
	 */
	public void setInstructionPages(ArrayList<InstructionsPage> instructionPages) {
		this.instructionPages = instructionPages;
	}
	
	/**
	 * Get the time spent reviewing the instructions page(s) (in milliseconds).
	 * FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @return the time spent reviewing the instructions page(s) (in milliseconds)
	 */
	@XmlAttribute(name="instructionsTime_ms")
	public Long getInstructionsTime_ms() {
		return instructionsTime_ms;
	}

	/**
	 * Set the time spent reviewing the instructions page(s) (in milliseconds).
	 * FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @param instructionsTime_ms the time spent reviewing the instructions page(s) (in milliseconds)
	 */
	public void setInstructionsTime_ms(Long instructionsTime_ms) {
		this.instructionsTime_ms = instructionsTime_ms;
	}	

	/**
	 * Get whether to show the condition's instruction page(s) before the condition is started. 
	 * FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @return
	 */
	@XmlElement(name="ShowInstructionsPage")
	public Boolean isShowInstructionPage() {
		return showInstructionPage;
	}

	/**
	 * Set whether to show the condition's instruction page(s) before the condition is started. 
	 * FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @param showInstructionPage whether to show the condition's instruction page(s) before the condition is started
	 */
	public void setShowInstructionPage(Boolean showInstructionPage) {
		this.showInstructionPage = showInstructionPage;
	}
	
	/**
	 * Get whether to show the subject's score when the condition is finished.
	 * FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @return whether to show the subject's score when the condition is finished
	 */
	@XmlElement(name="ShowScore")
	public Boolean isShowScore() {
		return showScore;
	}

	/**
	 * Set whether to show the subject's score when the condition is finished. 
	 * FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @param showScore whether to show the subject's score when the condition is finished
	 */
	public void setShowScore(Boolean showScore) {
		this.showScore = showScore;
	}

	/**
	 * Get whether to count the condition in the condition counter display.
	 * FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @return whether to count the condition in the condition counter display
	 */
	@XmlElement(name="CountCondition")
	public Boolean isCountCondition() {
		return countCondition;
	}

	/**
	 * Set whether to count the condition in the condition counter display.
	 * FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @param countCondition whether to count the condition in the condition counter display
	 */
	public void setCountCondition(Boolean countCondition) {
		this.countCondition = countCondition;
	}
}