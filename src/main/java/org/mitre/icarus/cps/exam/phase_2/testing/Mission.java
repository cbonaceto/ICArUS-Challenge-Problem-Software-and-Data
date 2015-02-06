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
package org.mitre.icarus.cps.exam.phase_2.testing;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.app.window.controller.ApplicationController;
import org.mitre.icarus.cps.exam.base.pause.IcarusPausePhase;
import org.mitre.icarus.cps.exam.base.testing.IcarusTestPhase;
import org.mitre.icarus.cps.feature_vector.phase_2.AreaOfInterest;
import org.mitre.icarus.cps.feature_vector.phase_2.BlueLocation;
import org.mitre.icarus.cps.feature_vector.phase_2.FeatureVectorFileDescriptor;

/**
 * Base class for Phase 2 missions.
 * 
 * @author CBONACETO
 *
 */
@XmlRootElement(name="Mission_Phase2", namespace="IcarusCPD_2")
@XmlType(name="Mission_Phase2", namespace="IcarusCPD_2",
	propOrder={"missionType", "applicationVersion", "pausePhase", "aoiFile", "blueLocationsFile"})
@XmlSeeAlso({Mission_1_2_3.class, Mission_4_5_6.class})
public abstract class Mission<T extends IcarusTestTrial_Phase2> extends IcarusTestPhase<T> {	
	
	/** Application version information. FOR HUMAN SUBJECT USE ONLY. */
	protected String applicationVersion = ApplicationController.VERSION;	
	
	/** Pause phase to show before the test phase (if any, for human subject use only) */
	protected IcarusPausePhase pausePhase;
	
	/** Feature vector file containing the area of interest for the Mission */
	protected FeatureVectorFileDescriptor aoiFile;
	
	/** Feature vector file containing all Blue locations for each trial in the Mission */
	protected FeatureVectorFileDescriptor blueLocationsFile;	
	
	/** The area of interest for the Mission */
	protected AreaOfInterest aoi;
	
	/** The Blue locations for the mission */
	protected List<BlueLocation> blueLocations;
	
	/** The mission type */
	protected MissionType missionType;
	
	/** The Blue player score for the mission */
	protected Double blueScore;
	
	/** The Red player score for the mission */
	protected Double redScore;
	
	/**
	 * Construct an empty mission. 
	 */
	public Mission() {}
	
	/**
	 * Construct a mission of the given mission type.
	 * 
	 * @param missionType the mission type
	 */
	public Mission(MissionType missionType) {
		this.missionType = missionType;
	}
	
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
	 * Get the area of interest feature vector file for the mission.
	 * 
	 * @return the area of interest feature vector file
	 */
	@XmlElement(name="AoiFile")
	public FeatureVectorFileDescriptor getAoiFile() {
		return aoiFile;
	}

	/**
	 * Set the area of interest feature vector file for the mission.
	 * 
	 * @param aoiFile the area of interest feature vector file
	 */
	public void setAoiFile(FeatureVectorFileDescriptor aoiFile) {
		this.aoiFile = aoiFile;
	}
	
	/**
	 * Get the Blue locations feature vector file containing all Blue locations for each trial in the Mission.
	 * 
	 * @return the Blue locations feature vector file
	 */
	@XmlElement(name="BlueLocationsFile")
	public FeatureVectorFileDescriptor getBlueLocationsFile() {
		return blueLocationsFile;
	}

	/**
	 * Set the Blue locations feature vector file containing all Blue locations for each trial in the Mission.
	 * 
	 * @param blueLocationsFile the Blue locations feature vector file
	 */
	public void setBlueLocationsFile(FeatureVectorFileDescriptor blueLocationsFile) {
		this.blueLocationsFile = blueLocationsFile;
	}

	/**
	 * Get the area of interest for the mission. Initialized from the area of interest feature vector file.
	 * 
	 * @return the area of interest for the mission
	 */
	@XmlTransient
	public AreaOfInterest getAoi() {
		return aoi;
	}

	/**
	 * Set the area of interest for the mission. Initialized from the area of interest feature vector file.
	 * 
	 * @param aoi the area of interest for the mission
	 */
	public void setAoi(AreaOfInterest aoi) {
		this.aoi = aoi;
	}

	/**
	 * Get the Blue locations for each trial in the mission. Initialized from the Blue locations feature vector file.
	 * 
	 * @return the Blue locations for each trial in the mission
	 */
	@XmlTransient
	public List<BlueLocation> getBlueLocations() {
		return blueLocations;
	}

	/**
	 * Set the Blue locations for each trial in the mission. Initialized from the Blue locations feature vector file.
	 * 
	 * @param blueLocations the Blue locations for each trial in the mission
	 */
	public void setBlueLocations(List<BlueLocation> blueLocations) {
		this.blueLocations = blueLocations;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.base.testing.IcarusTestPhase#getTestTrials()
	 */
	@Override
	public abstract ArrayList<T> getTestTrials();

	/**
	 * Get the type of mission (Mission 1, 2, 3, 4, 5, or 6).
	 * 
	 * @return the type of mission
	 */
	@XmlAttribute(name="missionType", required=true)
	public MissionType getMissionType() {
		return missionType;
	}

	/**
	 * Set the type of mission (Mission 1, 2, 3, 4, 5, or 6).
	 * 
	 * @param missionType the type of mission
	 */
	public void setMissionType(MissionType missionType) {
		this.missionType = missionType;
	}
	
	/**
	 * Get the overall Blue score for the mission.
	 * 
	 * @return the overall Blue score for the mission. 
	 */
	@XmlAttribute(name="blueScore")
	public Double getBlueScore() {
		return blueScore;
	}

	/**
	 * Set the overall Blue score for the mission.
	 * 
	 * @param blueScore the overall Blue score for the mission.
	 */
	public void setBlueScore(Double blueScore) {
		this.blueScore = blueScore;
	}

	/** 
	 * Get the overall Red score for the mission.
	 * 
	 * @return the overall Red score for the mission.
	 */
	@XmlAttribute(name="redScore")
	public Double getRedScore() {
		return redScore;
	}

	/**
	 * Set the overall Red score for the mission.
	 * 
	 * @param redScore the overall Red score for the mission.
	 */
	public void setRedScore(Double redScore) {
		this.redScore = redScore;
	}
	
	/**
	 * Clear the response data for all trials in the mission.
	 */
	public void clearResponseData() {
		startTime = null;
		endTime = null;
		blueScore = null;
		redScore = null;
		if(getTestTrials() != null) {
			for(T trial : getTestTrials()) {
				trial.clearResponseData();
			}
		}
	}
}