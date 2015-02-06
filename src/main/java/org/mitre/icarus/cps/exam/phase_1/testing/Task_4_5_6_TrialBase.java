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

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.SurpriseReportProbe;
import org.mitre.icarus.cps.feature_vector.phase_1.FeatureVectorFileDescriptor;
import org.mitre.icarus.cps.feature_vector.phase_1.Road;
import org.mitre.icarus.cps.feature_vector.phase_1.SocintOverlay;
import org.mitre.icarus.cps.feature_vector.phase_1.SocintPolygon;
import org.mitre.icarus.cps.feature_vector.phase_1.TaskData;

/**
 * Abstract base class for Task 4, 5, and 6 trials.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="Task_4_5_6_TrialBase", namespace="IcarusCPD_1",
		propOrder={"featureVectorFile", "distanceVectorFile", "roadsFile", "regionsFile"})
public abstract class Task_4_5_6_TrialBase extends IcarusTestTrial_Phase1 {
	
	/** Feature vector file */	
	protected FeatureVectorFileDescriptor featureVectorFile;
	
	/** Distance vector file */
	protected FeatureVectorFileDescriptor distanceVectorFile;
	
	/** Roads file */
	protected FeatureVectorFileDescriptor roadsFile;
	
	/** Region boundaries file */
	protected FeatureVectorFileDescriptor regionsFile;
	
	/** The surprise report at the end of the trial (after ground truth is shown) */
	protected SurpriseReportProbe groundTruthSurpriseProbe;
	
	/** The roads */
	protected ArrayList<Road> roads;
	
	/** The SCOINT region boundaries for each group */
	protected ArrayList<SocintPolygon> regionBounds;
	
	/** The SOCINT regions overlay for all groups */
	protected SocintOverlay regionsOverlay;
	
	/** Ground truth information contains the actual attack location */
	protected GroundTruth groundTruth;	

	/**
	 * Get the feature vector file information.
	 * 
	 * @return the feature vector file information
	 */
	@XmlElement(name="FeatureVectorFile")
	public FeatureVectorFileDescriptor getFeatureVectorFile() {
		return featureVectorFile;
	}

	/**
	 * Set the feature vector file information.
	 * 
	 * @param featureVectorFile the feature vector file information
	 */
	public void setFeatureVectorFile(FeatureVectorFileDescriptor featureVectorFile) {
		this.featureVectorFile = featureVectorFile;
	}	
	
	/**
	 * Get the distance vector file information.
	 * 
	 * @return the distance vector file information
	 */
	@XmlElement(name="DistanceFile")
	public FeatureVectorFileDescriptor getDistanceVectorFile() {
		return distanceVectorFile;
	}

	/**
	 * Set the distance vector file information.
	 * 
	 * @param distanceVectorFile the distance vector file information
	 */
	public void setDistanceVectorFile(FeatureVectorFileDescriptor distanceVectorFile) {
		this.distanceVectorFile = distanceVectorFile;
	}

	/**
	 * Populates group centers and/or attack locations from the given taskData object.
	 * 
	 * @param taskData contains the group centers and/or attack locations
	 */
	public abstract void setTaskData(TaskData taskData);
	
	/**
	 * Get the roads feature vector file information. The roads feature vector contains the roads.
	 * 
	 * @return the roads feature vector file information
	 */
	@XmlElement(name="RoadsFile")
	public FeatureVectorFileDescriptor getRoadsFile() {
		return roadsFile;
	}

	/**
	 * Set the roads feature vector file information. The roads feature vector contains the roads.
	 * 
	 * @param roadsFile the roads feature vector file information
	 */
	public void setRoadsFile(FeatureVectorFileDescriptor roadsFile) {
		this.roadsFile = roadsFile;
	}	
	
	/**
	 * Get the regions feature vector file information. The regions feature vector contains the regions.
	 *  
	 * @return the regions feature vector file information
	 */
	@XmlElement(name="RegionsFile")
	public FeatureVectorFileDescriptor getRegionsFile() {
		return regionsFile;
	}

	/**
	 * Set the regions feature vector file information. The regions feature vector contains the regions.
	 * 
	 * @param regionsFile the regions feature vector file information
	 */
	public void setRegionsFile(FeatureVectorFileDescriptor regionsFile) {
		this.regionsFile = regionsFile;
	}	
	
	/**
	 * Get the roads.  The roads are populated from the roads feature vector file.
	 * 
	 * @return the roads
	 */
	@XmlTransient
	public ArrayList<Road> getRoads() {
		return roads;
	}	

	/**
	 * Set the roads.  The roads are populated from the roads feature vector file.
	 * 
	 * @param roads the roads
	 */
	public void setRoads(ArrayList<Road> roads) {
		this.roads = roads;
	}
	
	/**
	 * Get the SOCINT group region bounds. The region bounds are populated from the regions feature vector file.
	 * 
	 * @return the region bounds
	 */
	@XmlTransient
	public ArrayList<SocintPolygon> getRegionBounds() {
		return regionBounds;
	}

	/**
	 * Set the SOCINT group region bounds. The region bounds are populated from the regions feature vector file.
	 * 
	 * @param regionBounds the region bounds
	 */
	public void setRegionBounds(ArrayList<SocintPolygon> regionBounds) {
		this.regionBounds = regionBounds;
	}
	
	/**
	 * Set the SOCINT regions overlay for all groups. The SOCINT regions overlay is populated from the regions feature vector file.
	 * 
	 * @return - the SOCINT regions overlay 
	 */
	@XmlTransient
	public SocintOverlay getRegionsOverlay() {
		return regionsOverlay;
	}

	/**
	 * Set the SOCINT regions overlay for all groups. The SOCINT regions overlay is populated from the regions feature vector file.
	 * 
	 * @param regionsOverlay - the SOCINT regions overlay
	 */
	public void setRegionsOverlay(SocintOverlay regionsOverlay) {
		this.regionsOverlay = regionsOverlay;
	}

	/**
	 * Get the ground truth information (e.g., the responsible group or actual attack location).
	 * 
	 * @return the ground truth information
	 */
	@XmlTransient
	public GroundTruth getGroundTruth() {
		return groundTruth;
	}

	/**
	 * Set the ground truth information (e.g., the responsible group or actual attack location).
	 * 
	 * @param groundTruth the ground truth information
	 */
	public void setGroundTruth(GroundTruth groundTruth) {
		this.groundTruth = groundTruth;
	}	

	/**
	 * Get the surprise probe after ground truth is revealed.
	 * 
	 * @return the surprise probe
	 */
	@XmlTransient	
	public SurpriseReportProbe getGroundTruthSurpriseProbe() {
		return groundTruthSurpriseProbe;
	}

	/**
	 * Set the surprise probe after ground truth is revealed.
	 * 
	 * @param groundTruthSurpriseProbe the surprise probe
	 */
	public void setGroundTruthSurpriseProbe(
			SurpriseReportProbe groundTruthSurpriseProbe) {
		this.groundTruthSurpriseProbe = groundTruthSurpriseProbe;
	}		
}