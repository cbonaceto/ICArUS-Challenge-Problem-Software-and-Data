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
package org.mitre.icarus.cps.exam.phase_05;

import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.base.IcarusExam;
import org.mitre.icarus.cps.exam.base.IcarusExamPhase;
import org.mitre.icarus.cps.exam.base.testing.IcarusTestTrial;
import org.mitre.icarus.cps.exam.phase_05.testing.IcarusTestPhase_Phase05;
import org.mitre.icarus.cps.exam.phase_05.testing.scene_presentation.FacilitySceneItem;
import org.mitre.icarus.cps.exam.phase_05.testing.scene_presentation.IncidentSceneItem;
import org.mitre.icarus.cps.exam.phase_05.testing.scene_presentation.ObjectSceneItem;
import org.mitre.icarus.cps.exam.phase_05.testing.scene_presentation.SceneItem;
import org.mitre.icarus.cps.exam.phase_05.testing.scene_presentation.ScenePresentationTrial;
import org.mitre.icarus.cps.exam.phase_05.testing.scene_presentation.layer_presentation_types.LayerPresentation.PresentationType;

/**
 * Defines an ICArUS exam for the Phase 0.5 CPD format.  The exam consists of an ordered list
 * of IcarusExamPhases, which may include training, test, and pause phases.  
 * Pause phases are only used in human subject experiments. 
 * 
 * @author CBONACETO
 *
 */
@XmlRootElement(name="IcarusEvaluation", namespace="IcarusCPD_05")
@XmlType(name="IcarusEvaulation", namespace="IcarusCPD_05", 
		propOrder={"tutorialUrl", "facilities", "objects", "incidents", "phases"})
public class IcarusExam_Phase05 extends IcarusExam<IcarusExamPhase> {		
	
	/** The URL to the tutorial file for the exam (if any). FOR HUMAN SUBJECT USE ONLY. */
	protected String tutorialUrl;	
	
	/** The master list of facilities possibly present in each scene */
	protected ArrayList<FacilitySceneItem> facilities;
	
	/** The master list of objects possibly present in each scene */
	protected ArrayList<ObjectSceneItem> objects;
	
	/** The master list of incidents possibly present in each scene*/
	protected ArrayList<IncidentSceneItem> incidents;
	
	/** Map containing all scene items (facilities, objects, events) mapped by 
	 * their item ID */
	private HashMap<Integer, SceneItem> sceneItemsMap;
	
	/** The ordered list of training phases, trial phases, and pauses */
	protected ArrayList<IcarusExamPhase> phases;	
	
	/**
	 * Get the facilities list.
	 * 
	 * @return
	 */
	@XmlElementWrapper(name="Facilities")
	@XmlElement(name="Facility")
	public ArrayList<FacilitySceneItem> getFacilities() {
		return facilities;
	}

	/**
	 * Set the facilities list.
	 * 
	 * @param facilities
	 */
	public void setFacilities(ArrayList<FacilitySceneItem> facilities) {
		this.facilities = facilities;
		sceneItemsMap = null;
	}

	/**
	 * Get the objects list.
	 * 
	 * @return
	 */
	@XmlElementWrapper(name="Objects")
	@XmlElement(name="Object")
	public ArrayList<ObjectSceneItem> getObjects() {
		return objects;
	}

	/**
	 * Set the objects list.
	 * 
	 * @param objects
	 */
	public void setObjects(ArrayList<ObjectSceneItem> objects) {
		this.objects = objects;
		sceneItemsMap = null;
	}	
		
	/**
	 * Get the incidents list.
	 * 
	 * @return
	 */
	@XmlElementWrapper(name="Incidents")
	@XmlElement(name="Incident")
	public ArrayList<IncidentSceneItem> getIncidents() {
		return incidents;
	}

	/**
	 * Set the incidents list.
	 * 
	 *  @param incidents
	 */
	public void setIncidents(ArrayList<IncidentSceneItem> incidents) {
		this.incidents = incidents;
	}

	/** Call after facilities, objects, and events lists have been set */
	protected void createSceneItemsMap() {
		sceneItemsMap = new HashMap<Integer, SceneItem>();
		if(facilities != null) {
			for(SceneItem item : facilities) {
				sceneItemsMap.put(item.getItemId(), item);
			}
		}
		if(objects != null) {
			for(SceneItem item : objects) {
				sceneItemsMap.put(item.getItemId(), item);
			}
		}
		if(incidents != null) {
			for(SceneItem item : incidents) {
				sceneItemsMap.put(item.getItemId(), item);
			}
		}
	}
	
	/**
	 * Get the scene item (facility, object, or event) with the given itemId.
	 * 
	 * @param itemId
	 * @return
	 */
	public SceneItem getSceneItem(Integer itemId) {
		if(sceneItemsMap == null) {
			createSceneItemsMap();
		}
		return sceneItemsMap.get(itemId);
	}	
	
	/**
	 * Get the exam phases.
	 * 
	 * @return
	 */
	@XmlElement(name="ExamPhase")
	public ArrayList<IcarusExamPhase> getPhases() {
		return phases;
	}

	/**
	 * Set the exam phases.
	 * 
	 * @param phases
	 */
	public void setPhases(ArrayList<IcarusExamPhase> phases) {
		this.phases = phases;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.experiment_core.Experiment#getConditions()
	 */
	@Override
	public ArrayList<IcarusExamPhase> getConditions() {
		return phases;
	}	
	
	/**
	 * Get the URL to the tutorial file. FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @return
	 */
	@XmlElement(name="TutorialUrl")
	public String getTutorialUrl() {
		return tutorialUrl;
	}

	/**
	 * Set the URL to the tutorial file. FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @param tutorialUrl
	 */
	public void setTutorialUrl(String tutorialUrl) {
		this.tutorialUrl = tutorialUrl;
	}

	/** Create a sample exam */
	protected static IcarusExam_Phase05 createSampleExam() {		
		IcarusExam_Phase05 exam = new IcarusExam_Phase05();
		exam.setName("scenario1");
		exam.setFacilities(new ArrayList<FacilitySceneItem>(4));
		exam.getFacilities().add(new FacilitySceneItem("Ketchup Gas Factory", 1)); 
		exam.getFacilities().add(new FacilitySceneItem("Neutrino Bomb Factory", 2));
		exam.getFacilities().add(new FacilitySceneItem("Slipper Factory", 3));
		exam.getFacilities().add(new FacilitySceneItem("Unknown Factory", 4));		
		exam.setObjects(new ArrayList<ObjectSceneItem>());		
		exam.setIncidents(new ArrayList<IncidentSceneItem>(1));
		exam.getIncidents().add(new IncidentSceneItem("Riot", 3));
		
		IcarusTestPhase_Phase05 testData = new IcarusTestPhase_Phase05();
		testData.setTestTrials(new ArrayList<IcarusTestTrial>());
		ScenePresentationTrial presentation1 = ScenePresentationTrial.createSampleScenePresentation1(PresentationType.Simultaneous);
		testData.getTestTrials().add(presentation1);
		
		ScenePresentationTrial presentation2 = ScenePresentationTrial.createSampleScenePresentation2(PresentationType.Sequential);
		presentation2.setFeatureVectorUrl("file://data/Sample2_features.csv");
		presentation2.setObjectPaletteUrl("file://data/Sample2_object_palette.csv");
		testData.getTestTrials().add(presentation2);
		
		ScenePresentationTrial presentation3 = ScenePresentationTrial.createSampleScenePresentation2(PresentationType.UserChoice);
		presentation2.setFeatureVectorUrl("file://data/Sample3_features.csv");
		presentation2.setObjectPaletteUrl("file://data/Sample3_object_palette.csv");
		testData.getTestTrials().add(presentation3);		
		
		exam.phases = new ArrayList<IcarusExamPhase>();
		exam.phases.add(testData);
		return exam;
	}
}
