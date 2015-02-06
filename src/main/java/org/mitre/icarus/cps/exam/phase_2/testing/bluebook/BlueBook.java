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
package org.mitre.icarus.cps.exam.phase_2.testing.bluebook;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.phase_2.testing.AbstractDatum;
import org.mitre.icarus.cps.exam.phase_2.testing.DatumType;
import org.mitre.icarus.cps.exam.phase_2.testing.MissionType;
import org.mitre.icarus.cps.experiment_core.condition.InstructionsPage;

/**
 * The "BLUEBOOK" for the exam, which contains the Red tactics (or a set of possible Red tactics) for each mission.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="BlueBook", namespace="IcarusCPD_2")
public class BlueBook extends AbstractDatum {
	
	/** The Red tactic(s) for Mission 1 */
	protected List<RedTactic> mission_1_Tactics;
	
	/** The Red tactics instruction page for Mission 1 */
	protected InstructionsPage mission_1_TacticsInstructions;
	
	/** The Red tactic(s) for Mission 2 */
	protected List<RedTactic> mission_2_Tactics;
	
	/** The Red tactics instruction page for Mission 2 */
	protected InstructionsPage mission_2_TacticsInstructions;
	
	/** The Red tactic(s) for Mission 3 */	
	protected List<RedTactic> mission_3_Tactics;
	
	/** The Red tactics instruction page for Mission 3 */
	protected InstructionsPage mission_3_TacticsInstructions;
	
	/** The Red tactics for Mission 4 */	
	protected List<RedTactic> mission_4_Tactics;
	
	/** The Red tactics instruction page for Mission 4 */
	protected InstructionsPage mission_4_TacticsInstructions;
	
	/** The Red tactics for Mission 5 */	
	protected List<RedTactic> mission_5_Tactics;
	
	/** The Red tactics instruction page for Mission 5 */
	protected InstructionsPage mission_5_TacticsInstructions;
	
	/**
	 * Create the default BLUEBOOK with default Red tactics for each mission.
	 * 
	 * @return the default BLUEBOOK.
	 */
	public static BlueBook createDefaultBlueBook() {
		BlueBook blueBook = new BlueBook();
		blueBook.mission_1_Tactics = Arrays.asList(RedTacticType.Mission_1.tactic);
		blueBook.mission_2_Tactics = Arrays.asList(RedTacticType.Mission_2_Passive.tactic, 
				RedTacticType.Mission_2_Aggressive.tactic);
		blueBook.mission_3_Tactics = Arrays.asList(RedTacticType.Mission_3.tactic);
		blueBook.mission_4_Tactics = Arrays.asList(RedTacticType.Mission_4_Passive.tactic,
				RedTacticType.Mission_4_Aggressive.tactic);
		blueBook.mission_5_Tactics = Arrays.asList(RedTacticType.Mission_5_Psensitive.tactic, 
				RedTacticType.Mission_5_Usensitive.tactic);
		return blueBook;
	}
	
	/**
	 * Convenience method to get the tactics for the given mission type.
	 * 
	 * @param missionType a mission type
	 * @return the tactics for the mission type
	 */
	public List<RedTactic> getRedTaticsForMission(MissionType missionType) {
		switch(missionType) {
		case Mission_1:
			return mission_1_Tactics;
		case Mission_2:
			return mission_2_Tactics;
		case Mission_3:
			return mission_3_Tactics;
		case Mission_4:
			return mission_4_Tactics;
		case Mission_5:
			return mission_5_Tactics;
		default:
			return null;
		}
	}
	
	/**
	 * Create a list of the Red tactic parameters for the given Red tactics.
	 * 
	 * @param redTactics the Red tactics
	 * @return redTactics a list of the Red tactic parameters for the given Red tactics
	 */
	public static List<RedTacticParameters> extractRedTacticParameters(List<RedTactic> redTactics) {
		if(redTactics != null && !redTactics.isEmpty()) {
			List<RedTacticParameters> tacticParameters = 
					new ArrayList<RedTacticParameters>(redTactics.size());
			for(RedTactic redTactic : redTactics) {
				tacticParameters.add(redTactic.getTacticParameters());
			}
			return tacticParameters;
		}
		return null;
	}
        
	 /**
	 * Create a list of the Red tactic types for the given Red tactics.
	 * 
	 * @param redTactics the Red tactics
	 * @return redTactics a list of the Red tactic types for the given Red tactics
	 */
	public static List<RedTacticType> extractRedTacticTypes(List<RedTactic> redTactics) {
		if(redTactics != null && !redTactics.isEmpty()) {
			List<RedTacticType> tacticTypes = 
					new ArrayList<RedTacticType>(redTactics.size());
			for(RedTactic redTactic : redTactics) {
				tacticTypes.add(redTactic.getTacticType());
			}
			return tacticTypes;
		}
		return null;
	}
	
	/** Convenience method to get the tactics instructions for the given mission type */
	public InstructionsPage getRedTacticsInstructionsForMission(MissionType missionType) {
		switch(missionType) {
		case Mission_1:
			return mission_1_TacticsInstructions;
		case Mission_2:
			return mission_2_TacticsInstructions;
		case Mission_3:
			return mission_3_TacticsInstructions;
		case Mission_4:
			return mission_4_TacticsInstructions;
		case Mission_5:
			return mission_5_TacticsInstructions;
		default:
			return null;
		}
	}
	
	/**
	 * Get the Red tactics for Mission 1.
	 * 
	 * @return the Red tactics for Mission 1
	 */
	@XmlElement(name="Mission_1_Tactics")
	public List<RedTactic> getMission_1_Tactics() {
		return mission_1_Tactics;
	}

	/**
	 * Set the Red tactics for Mission 1.
	 * 
	 * @param mission_1_Tactics the Red tactics for Mission 1
	 */
	public void setMission_1_Tactics(List<RedTactic> mission_1_Tactics) {
		this.mission_1_Tactics = mission_1_Tactics;
	}

	/**
	 * Get the Red tactic instructions for Mission 1.
	 * FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @return the Red tactic instructions for Mission 1
	 */
	@XmlElement(name="Mission_1_TacticsInstructions")
	public InstructionsPage getMission_1_TacticsInstructions() {
		return mission_1_TacticsInstructions;
	}

	/**
	 * Set the Red tactic instructions for Mission 1.
	 * FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @param mission_1_TacticsInstructions the Red tactic instructions for Mission 1
	 */
	public void setMission_1_TacticsInstructions(InstructionsPage mission_1_TacticsInstructions) {
		this.mission_1_TacticsInstructions = mission_1_TacticsInstructions;
	}

	/**
	 * Get the Red tactics for Mission 2.
	 * 
	 * @return the Red tactics for Mission 2
	 */
	@XmlElement(name="Mission_2_Tactics")
	public List<RedTactic> getMission_2_Tactics() {
		return mission_2_Tactics;
	}

	/**
	 * Set the Red tactics for Mission 2.
	 * 
	 * @param mission_2_Tactics the Red tactics for Mission 2
	 */
	public void setMission_2_Tactics(List<RedTactic> mission_2_Tactics) {
		this.mission_2_Tactics = mission_2_Tactics;
	}
	
	/**
	 * Get the Red tactic instructions for Mission 2.
	 * FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @return the Red tactic instructions for Mission 2
	 */
	@XmlElement(name="Mission_2_TacticsInstructions")
	public InstructionsPage getMission_2_TacticsInstructions() {
		return mission_2_TacticsInstructions;
	}

	/**
	 * Set the Red tactic instructions for Mission 2.
	 * FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @param mission_2_TacticsInstructions the Red tactic instructions for Mission 2
	 */
	public void setMission_2_TacticsInstructions(
			InstructionsPage mission_2_TacticsInstructions) {
		this.mission_2_TacticsInstructions = mission_2_TacticsInstructions;
	}

	/**
	 * Get the Red tactics for Mission 3.
	 * 
	 * @return the Red tactics for Mission 3
	 */
	@XmlElement(name="Mission_3_Tactics")
	public List<RedTactic> getMission_3_Tactics() {
		return mission_3_Tactics;
	}

	/**
	 * Set the Red tactics for Mission 3.
	 * 
	 * @param mission_3_Tactics the Red tactics for Mission 3
	 */
	public void setMission_3_Tactics(List<RedTactic> mission_3_Tactics) {
		this.mission_3_Tactics = mission_3_Tactics;
	}

	/**
	 * Get the Red tactic instructions for Mission 3.
	 * FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @return the Red tactic instructions for Mission 3
	 */
	@XmlElement(name="Mission_3_TacticsInstructions")
	public InstructionsPage getMission_3_TacticsInstructions() {
		return mission_3_TacticsInstructions;
	}

	/**
	 * Set the Red tactic instructions for Mission 3.
	 * FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @param mission_3_TacticsInstructions the Red tactic instructions for Mission 3
	 */
	public void setMission_3_TacticsInstructions(
			InstructionsPage mission_3_TacticsInstructions) {
		this.mission_3_TacticsInstructions = mission_3_TacticsInstructions;
	}
	
	/**
	 * Get the Red tactics for Mission 4.
	 * 
	 * @return the Red tactics for Mission 4
	 */
	@XmlElement(name="Mission_4_Tactics")
	public List<RedTactic> getMission_4_Tactics() {
		return mission_4_Tactics;
	}

	/**
	 * Set the Red tactics for Mission 4.
	 * 
	 * @param mission_4_Tactics
	 */
	public void setMission_4_Tactics(List<RedTactic> mission_4_Tactics) {
		this.mission_4_Tactics = mission_4_Tactics;
	}

	/**
	 * Get the Red tactic instructions for Mission 4.
	 * FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @return the Red tactic instructions for Mission 4
	 */
	@XmlElement(name="Mission_4_TacticsInstructions")
	public InstructionsPage getMission_4_TacticsInstructions() {
		return mission_4_TacticsInstructions;
	}

	/**
	 * Set the Red tactic instructions for Mission 4.
	 * FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @param mission_4_TacticsInstructions the Red tactic instructions for Mission 4
	 */
	public void setMission_4_TacticsInstructions(
			InstructionsPage mission_4_TacticsInstructions) {
		this.mission_4_TacticsInstructions = mission_4_TacticsInstructions;
	}

	/**
	 * Get the Red tactics for Mission 5.
	 * 
	 * @return the Red tactics for Mission 5
	 */
	@XmlElement(name="Mission_5_Tactics")
	public List<RedTactic> getMission_5_Tactics() {
		return mission_5_Tactics;
	}

	/**
	 * Set the Red tactics for Mission 5.
	 * 
	 * @param mission_5_Tactics the Red tactics for Mission 5
	 */
	public void setMission_5_Tactics(List<RedTactic> mission_5_Tactics) {
		this.mission_5_Tactics = mission_5_Tactics;
	}

	/**
	 * Get the Red tactic instructions for Mission 5.
	 * FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @return the Red tactic instructions for Mission 5
	 */
	@XmlElement(name="Mission_5_TacticsInstructions")
	public InstructionsPage getMission_5_TacticsInstructions() {
		return mission_5_TacticsInstructions;
	}

	/**
	 * Set the Red tactic instructions for Mission 5.
	 * FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @param mission_5_TacticsInstructions the Red tactic instructions for Mission 5
	 */
	public void setMission_5_TacticsInstructions(
			InstructionsPage mission_5_TacticsInstructions) {
		this.mission_5_TacticsInstructions = mission_5_TacticsInstructions;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.IDatum#getDatumType()
	 */
	@Override
	public DatumType getDatumType() {
		return DatumType.BlueBook;
	}
}