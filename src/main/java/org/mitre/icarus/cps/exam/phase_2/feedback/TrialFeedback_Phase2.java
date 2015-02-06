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
package org.mitre.icarus.cps.exam.phase_2.feedback;

import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.base.feedback.TestTrialFeedback;
import org.mitre.icarus.cps.exam.phase_2.loader.IcarusExamLoader_Phase2;
import org.mitre.icarus.cps.exam.phase_2.testing.IcarusTestTrial_Phase2;
import org.mitre.icarus.cps.exam.phase_2.testing.PlayerType;
import org.mitre.icarus.cps.exam.phase_2.testing.ShowdownWinner;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.BlueAction;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.RedAction;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.BlueAction.BlueActionType;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.RedAction.RedActionType;

/**
 * Contains feedback provided by the test harness to an ICArUS model after submitting 
 * its response to a trial in a Phase 2 mission.
 * 
 * @author CBONACETO
 *
 */
@XmlRootElement(name="TrialFeedback_Phase2", namespace="IcarusCPD_2")
@XmlType(name="TrialFeedback_Phase2", namespace="IcarusCPD_2", propOrder={"blueActions", "redAction", 
		"showdownWinner", "bluePointsGained", "redPointsGained", "blueScore", "redScore", 
		"batchPlotsRemaining"})
public class TrialFeedback_Phase2 extends TestTrialFeedback<IcarusTestTrial_Phase2> {	
	
	/** The Blue action(s) taken */
	protected List<BlueAction> blueActions;
	
	/** The Red action taken */
	protected RedAction redAction;
	
	/** The showdown winner (if there was a showdown at a location) */
	protected ShowdownWinner showdownWinner;
	
	/** The Blue points acquired on the trial */
	protected Double bluePointsGained;
	
	/** The Red points acquired on the trial */
	protected Double redPointsGained;	
	
	/** The current Blue score */
	protected Double blueScore;
	
	/** The total current Red score */
	protected Double redScore;
	
	/** Whether a batch plot was created (Missions 4 - 5 only) */
	protected Boolean batchPlotCreated;
	
	/** The number of batch plots remaining (Missions 4 - 5 only) */
	protected Integer batchPlotsRemaining;		

	/**
	 * Get the Blue actions taken at each location (in order). 
	 * 
	 * @return the Blue actions
	 */
	@XmlElement(name="BlueAction")
	public List<BlueAction> getBlueActions() {
		return blueActions;
	}

	/**
	 * Set the Blue actions taken at each location (in order).
	 * 
	 * @param blueActions the Blue actions
	 */
	public void setBlueActions(List<BlueAction> blueActions) {
		this.blueActions = blueActions;
	}

	/**
	 * Get the Red action taken.
	 * 
	 * @return the Red action
	 */
	@XmlElement(name="RedAction")
	public RedAction getRedAction() {
		return redAction;
	}

	/**
	 * Set the Red action taken.
	 * 
	 * @param redAction the Red action
	 */
	public void setRedAction(RedAction redAction) {
		this.redAction = redAction;
	}
	
	/**
	 * Get the showdown winner (if there was a showdown at a location).
	 * 
	 * @return the showdown winner
	 */
	@XmlElement(name="ShowdownWinner")
	public ShowdownWinner getShowdownWinner() {
		return showdownWinner;
	}

	/**
	 * Set the showdown winner (if there was a showdown at a location).
	 * 
	 * @param showdownWinner the showdown winner
	 */
	public void setShowdownWinner(ShowdownWinner showdownWinner) {
		this.showdownWinner = showdownWinner;
	}

	/**
	 * Get the Blue points acquired on the trial.
	 * 
	 * @return the Blue points
	 */
	@XmlAttribute(name="bluePointsGained")
	public Double getBluePointsGained() {
		return bluePointsGained;
	}

	/**
	 * Set the Blue points acquired on the trial.
	 * 
	 * @param bluePointsGained the Blue points
	 */
	public void setBluePointsGained(Double bluePointsGained) {
		this.bluePointsGained = bluePointsGained;
	}

	/**
	 * Get the Red points acquired on the trial.
	 * 
	 * @return the Red points
	 */
	@XmlAttribute(name="redPointsGained")
	public Double getRedPointsGained() {
		return redPointsGained;
	}

	/**
	 * Set the Red points acquired on the trial.
	 * 
	 * @param redPointsGained the Red points
	 */
	public void setRedPointsGained(Double redPointsGained) {
		this.redPointsGained = redPointsGained;
	}

	/**
	 * Get the current Blue score (for the Mission).
	 * 
	 * @return the Blue score
	 */
	@XmlAttribute(name="blueScore")
	public Double getBlueScore() {
		return blueScore;
	}

	/**
	 * Set the current Blue score (for the Mission).
	 * 
	 * @param blueScore the Blue score
	 */
	public void setBlueScore(Double blueScore) {
		this.blueScore = blueScore;
	}

	/**
	 * Get the current Red score (for the Mission).
	 * 
	 * @return the Red score
	 */
	@XmlAttribute(name="redScore")
	public Double getRedScore() {
		return redScore;
	}

	/**
	 * Set the current Blue score (for the Mission).
	 * 
	 * @param redScore the Red score
	 */
	public void setRedScore(Double redScore) {
		this.redScore = redScore;
	}

	/**
	 * Get whether a batch plot was created (Missions 4-6 only).
	 * 
	 * @return whether a batch plot was created
	 */
	@XmlAttribute(name="batchPlotCreated")
	public Boolean isBatchPlotCreated() {
		return batchPlotCreated;
	}

	/**
	 * Set whether a batch plot was created (Missions 4-6 only).
	 * 
	 * @param batchPlotCreated whether a batch plot was created
	 */	
	public void setBatchPlotCreated(Boolean batchPlotCreated) {
		this.batchPlotCreated = batchPlotCreated;
	}

	/**
	 * Get the number of batch plots remaining in the Mission (Missions 4-6 only).
	 * 
	 * @return the number of batch plots remaining
	 */
	@XmlAttribute(name="batchPlotsRemaining")
	public Integer getBatchPlotsRemaining() {
		return batchPlotsRemaining;
	}

	/**
	 * Set the number of batch plots remaining in the Mission (Missions 4-6 only).
	 * 
	 * @param batchPlotsRemaining the number of batch plots remaining
	 */
	public void setBatchPlotsRemaining(Integer batchPlotsRemaining) {
		this.batchPlotsRemaining = batchPlotsRemaining;
	}	
	
	public static void main(String[] args) {
		TrialFeedback_Phase2 f = new TrialFeedback_Phase2();
		/*
		 * 
	protected List<BlueAction> blueActions;
	protected RedAction redAction;
	protected ShowdownWinner showdownWinner;
	protected Double bluePointsGained;
	protected Double redPointsGained;	
	protected Double blueScore;
	protected Double redScore;
	protected Boolean batchPlotCreated;
	protected Integer batchPlotsRemaining;		
		 */		
		f.setProgramPhaseId("2");
		f.setExamId("Sample-Exam-1");
		f.setPhaseId("Mission4");
		f.setTrialNum(1);
		f.setResponseWellFormed(true);
		f.setResponseGeneratorId("MITRE-1");
		f.setBlueActions(Collections.singletonList(new BlueAction("1-1", 0, BlueActionType.Divert)));
		f.setRedAction(new RedAction("1-1", 0, RedActionType.Attack));
		f.setBluePointsGained(4D);
		f.setRedPointsGained(-2D);
		f.setBlueScore(4D);
		f.setRedScore(-2D);
		f.setBatchPlotCreated(false);
		f.setBatchPlotsRemaining(3);		
		f.setShowdownWinner(new ShowdownWinner("1-1", 0, PlayerType.Blue));
		try {
			System.out.println(IcarusExamLoader_Phase2.marshalTrialFeedback(f));
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}