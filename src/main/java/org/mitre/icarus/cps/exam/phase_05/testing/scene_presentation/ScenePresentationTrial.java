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
package org.mitre.icarus.cps.exam.phase_05.testing.scene_presentation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.phase_05.response.LayerData;
import org.mitre.icarus.cps.exam.phase_05.testing.IcarusTestTrial_Phase05;
import org.mitre.icarus.cps.exam.phase_05.testing.scene_presentation.layer_presentation_types.LayerList;
import org.mitre.icarus.cps.exam.phase_05.testing.scene_presentation.layer_presentation_types.LayerPresentation;
import org.mitre.icarus.cps.exam.phase_05.testing.scene_presentation.layer_presentation_types.SequentialPresentation;
import org.mitre.icarus.cps.exam.phase_05.testing.scene_presentation.layer_presentation_types.UserChoicePresentation;
import org.mitre.icarus.cps.exam.phase_05.testing.scene_presentation.layer_presentation_types.LayerPresentation.PresentationType;
import org.mitre.icarus.cps.exam.phase_05.testing.scene_presentation.question_types.IdentifyItemQuestion;
import org.mitre.icarus.cps.exam.phase_05.testing.scene_presentation.question_types.LocateItemQuestion;
import org.mitre.icarus.cps.exam.phase_05.testing.scene_presentation.question_types.Question;

/**
 * Scene presentation testing trial.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="ScenePresentation", namespace="IcarusCPD_05", 
		propOrder={"featureVectorUrl", "objectPaletteUrl",
		"sceneContraints", "question",
		"baseLayers", "additionalLayerPresentations",  
		"probabilityData", "sectorTruthData"})
public class ScenePresentationTrial extends IcarusTestTrial_Phase05 {

	/** URL to the features vector file for the scene */
	protected String featureVectorUrl;
	
	/** URL to the object palette file for the scene */
	protected String objectPaletteUrl;
	
	/** Scene constraints define the min/max possible number of scene items in the overall
	 * scene and in each sector */
	protected SceneConstraints sceneContraints;
	
	/** The question definition */
	protected Question question;

	/** The IDs of the base layers to present (base layers are always presented simultaneously) */
	protected List<Integer> baseLayers;
	
	/** Ordered list of additional layer presentations after the base layers are presented */
	protected List<LayerPresentation> additionalLayerPresentations; 
	
	/** Normative probability data for each layer indicating the probability of each
	 * type of facility, object, or event being present in each sector given the information 
	 * contained in the layer.
	 * FOR HUMAN SUBJECT USE ONLY. */
	protected ArrayList<LayerProbabilityData> probabilityData;
	
	/** Maps a sorted list of layers in string format (e.g., "1_2_3") to
	 * the normative probability data given that those layers have been presented.
	 * FOR HUMAN SUBJECT USE ONLY. */	
	protected HashMap<String, LayerProbabilityData> probabilityDataMap;
	
	/** Truth data for facilities, objects, and events actually present in each sector 
	 * FOR HUMAN SUBJECT USE ONLY.*/
	protected ArrayList<SectorTruthData> sectorTruthData;	
	
	/**
	 * Get the URL to the features vector file for the scene.
	 * 
	 * @return
	 */
	@XmlElement(name="FeatureVectorUrl")
	public String getFeatureVectorUrl() {
		return featureVectorUrl;
	}

	/**
	 * Sset the URL to the features vector file for the scene.
	 * 
	 * @param featureVectorUrl
	 */
	public void setFeatureVectorUrl(String featureVectorUrl) {
		this.featureVectorUrl = featureVectorUrl;
	}
	
	/**
	 * Get the URL to the object palette file for the scene.
	 * 
	 * @return
	 */
	@XmlElement(name="ObjectPaletteUrl")
	public String getObjectPaletteUrl() {
		return objectPaletteUrl;
	}

	/**
	 * Set the URL to the object palette file for the scene.
	 * 
	 * @param objectPaletteUrl
	 */
	public void setObjectPaletteUrl(String objectPaletteUrl) {
		this.objectPaletteUrl = objectPaletteUrl;
	}
	
	/**
	 * FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @return
	 */
	@XmlElement(name="SceneConstraints")
	public SceneConstraints getSceneContraints() {
		return sceneContraints;
	}

	/**
	 * FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @param sceneContraints
	 */
	public void setSceneContraints(SceneConstraints sceneContraints) {
		this.sceneContraints = sceneContraints;
	}

	/**
	 * Get the question definition.
	 * 
	 * @return
	 */
	@XmlElement(name="Question")
	public Question getQuestion() {
		return question;
	}

	/**
	 * Set the question definition.
	 * 
	 * @param question
	 */
	public void setQuestion(Question question) {
		this.question = question;
	}	
	
	/**
	 * Get the IDs of the base layers to present (base layers are always presented simultaneously).
	 * 
	 * @return
	 */
	@XmlElementWrapper(name="BaseLayers")
	@XmlElement(name="LayerId")
	public List<Integer> getBaseLayers() {
		return baseLayers;
	}

	/**
	 * Set the IDs of the base layers to present.
	 * 
	 * @param baseLayers
	 */
	public void setBaseLayers(List<Integer> baseLayers) {
		this.baseLayers = baseLayers;
	}	
	
	/**
	 * Get the Ordered list of additional layer presentations after the base layers are presented. 
	 * 
	 * @return
	 */
	@XmlElementWrapper(name="AdditionalLayers")
	@XmlElement(name="LayerPresentation")
	public List<LayerPresentation> getAdditionalLayerPresentations() {
		return additionalLayerPresentations;
	}

	/**
	 * Set the Ordered list of additional layer presentations after the base layers are presented.
	 * 
	 * @param additionalLayerPresentations
	 */
	public void setAdditionalLayerPresentations(
			List<LayerPresentation> additionalLayerPresentations) {
		this.additionalLayerPresentations = additionalLayerPresentations;
	}
	
	
	/**
	 * Get the normative probabilities for each sector and scene item given a list 
	 * of layers that have been shown. 
	 * 
	 * FOR HUMAN SUBJECT USE ONLY.
	 *
	 * @param layersShown
	 * @return
	 */
	public LayerProbabilityData getLayerProbabilityData(List<LayerData> layersShown) {
		if(probabilityDataMap == null) {
			//Initialize probabilityDataMap
			initProbabilityDataMap();
		}
		
		return probabilityDataMap.get(createLayersIdString(layersShown));
	}
	
	/** Initialize probabilityDataMap */
	protected void initProbabilityDataMap() {
		probabilityDataMap = new HashMap<String, LayerProbabilityData>();
		if(probabilityData != null) {
			for(LayerProbabilityData layerProbability : probabilityData) {				
				probabilityDataMap.put(createLayersIdString(layerProbability.getLayers()), 
						layerProbability);
			}
		}
	}
	
	/** Create a concatenated string for the sorted list of layer ids (e.g., "1_2_3") */
	protected String createLayersIdString(LayerList layers) {
		if(layers == null || layers.getLayers() == null || layers.getLayers().isEmpty()) {
			return "null";
		}		
		
		return createLayersIdStringForLayers(layers.getLayers());
	}	
	
	/** Create a concatenated string for the sorted list of layer ids (e.g., "1_2_3") */
	protected String createLayersIdString(List<LayerData> layers) {
		if(layers == null || layers.isEmpty()) {
			return "null";
		}
		
		List<Integer> layerIds = new LinkedList<Integer>();
		for(LayerData layer : layers) {
			layerIds.add(layer.getLayerID());
		}
		return createLayersIdStringForLayers(layerIds);
	}
	
	/** Create a concatenated string for the sorted list of layer ids (e.g., "1_2_3") */
	private String createLayersIdStringForLayers(List<Integer> layers) {
		Collections.sort(layers);
		StringBuilder sb = new StringBuilder();
		int i = 1;
		for(Integer layerId : layers) {
			sb.append(layerId.toString());
			if(i < layers.size()) {
				sb.append('_');
			}
			i++;
		}		
		//System.out.println(sb);
		return sb.toString();
	}

	/**
	 * Get the normative probability data for each layer indicating the probability of each
	 * type of facility, object, or event being present in each sector given the information 
	 * contained in the layer, FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @return
	 */
	@XmlElementWrapper(name="ProbabilityData")
	@XmlElement(name="LayerPresentation")
	public ArrayList<LayerProbabilityData> getProbabilityData() {
		return probabilityData;
	}	

	/**
	 * Set the normative probability data for each layer indicating the probability of each
	 * type of facility, object, or event being present in each sector given the information 
	 * contained in the layer, FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @param probabilityData
	 */
	public void setProbabilityData(ArrayList<LayerProbabilityData> probabilityData) {
		this.probabilityData = probabilityData;
		probabilityDataMap = null;
	}

	/**
	 * FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @return
	 */
	@XmlElementWrapper(name="SectorTruthData")
	@XmlElement(name="SectorItems")
	public List<SectorTruthData> getSectorTruthData() {
		return sectorTruthData;
	}

	/**
	 * FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @param sectorTruthData
	 */
	public void setSectorTruthData(ArrayList<SectorTruthData> sectorTruthData) {
		this.sectorTruthData = sectorTruthData;
	}
	
	/** Create a sample scene presentation instance with an identify item question */ 
	public static ScenePresentationTrial createSampleScenePresentation1(PresentationType presentationType) {
		ScenePresentationTrial scene = new ScenePresentationTrial();
		scene.setTrialNum(1);
		scene.featureVectorUrl = "file://data/Sample1_features.csv";
		scene.objectPaletteUrl = "file://data/Sample1_object_palette.csv";			
		IdentifyItemQuestion question = new IdentifyItemQuestion(); 
		question.setSceneItemsToProbe(Arrays.asList(1, 2, 3, 4));
		question.setSectorToProbe(1);
		scene.question = question;
		scene = createSampleLayerPresentation(scene, presentationType);		

		/*scene.layerProbabilityData = new ArrayList<LayerProbabilityData>();
		for(Layer layer : scene.layers) {
			LayerProbabilityData layerData = new LayerProbabilityData(layer);
			ArrayList<SectortProbabilityData> sectorProbabilityData = new ArrayList<SectortProbabilityData>();
			for(Sector sector : Sector.values()) {
				SectortProbabilityData sectorData = new SectortProbabilityData(sector);
				sectorData.setSceneItemProbabilityData(new ArrayList<SceneItemProbabilityData>(2));
				sectorData.getSceneItemProbabilityData().add(new SceneItemProbabilityData(1, .25));
				sectorData.getSceneItemProbabilityData().add(new SceneItemProbabilityData(2, .25));
				sectorProbabilityData.add(sectorData);
			}
			layerData.setSectorProbabilityData(sectorProbabilityData);
			scene.layerProbabilityData.add(layerData);
		}*/
		
		/*scene.sectorTruthData = new ArrayList<SectorTruthData>();
		for(Sector sector : Sector.values()) {
			SectorTruthData truthData = new SectorTruthData(sector);
			truthData.setSceneItemsPresent(new ArrayList<Integer>());
			if(sector == Sector.A) {
				truthData.getSceneItemsPresent().add(1);
			}
			else if(sector == Sector.C) {
				truthData.getSceneItemsPresent().add(2);
			}
			scene.sectorTruthData.add(truthData);
		}*/
		
		return scene;
	}
	
	protected static ScenePresentationTrial createSampleLayerPresentation(ScenePresentationTrial scene, PresentationType presentationType) {
		switch(presentationType) {
		case Simultaneous:
			scene.baseLayers = Arrays.asList(1, 2, 3);
			break;
		case Sequential:
			scene.baseLayers = Arrays.asList(1);
			scene.additionalLayerPresentations = new ArrayList<LayerPresentation>();
			SequentialPresentation sequentialPresentation = new SequentialPresentation();
			sequentialPresentation.setSequentialLayers(new ArrayList<LayerList>());
			sequentialPresentation.getSequentialLayers().add(new LayerList(Arrays.asList(1)));
			sequentialPresentation.getSequentialLayers().add(new LayerList(Arrays.asList(2)));
			scene.additionalLayerPresentations.add(sequentialPresentation);
			break;
		case UserChoice:
			scene.baseLayers = Arrays.asList(1);
			scene.additionalLayerPresentations = new ArrayList<LayerPresentation>();
			UserChoicePresentation choicePresentation = new UserChoicePresentation();
			choicePresentation.setOptionalLayers(new LayerList(Arrays.asList(1, 2)));
			choicePresentation.setNumOptionalLayersToShow(1);
			scene.additionalLayerPresentations.add(choicePresentation);
			break;
		default:
			break;
		}
		return scene;
	}
	
	/** Create a sample scene presentation instance with a locate item question */ 
	public static ScenePresentationTrial createSampleScenePresentation2(PresentationType presentationType) {
		ScenePresentationTrial scene = new ScenePresentationTrial();
		scene.setTrialNum(2);
		scene.featureVectorUrl = "Sample1_features.csv";
		scene.objectPaletteUrl = "Sample1_object_palette.csv";
		//scene.waterPaletteUrl = "Sample1_water_palette.csv";
		
		/*scene.sceneContraints = new SceneConstraints();
		scene.sceneContraints.minFacilitiesInScene = 1;
		scene.sceneContraints.maxFacilitiesInScene = 1;
		scene.sceneContraints.sectorConstraints = new ArrayList<SectorConstraints>(4);
		for(Sector sector : Sector.values()) {
			SectorConstraints sectorConstraints = new SectorConstraints(sector);
			sectorConstraints.minFacilitiesInSector = 0;
			sectorConstraints.maxFacilitiesInSector = 1;
			scene.sceneContraints.sectorConstraints.add(sectorConstraints);
		}*/		
		
		LocateItemQuestion question = new LocateItemQuestion();
		question.setSceneItemToProbe(1);
		question.setSectorsToProbe(Arrays.asList(1, 2, 3, 4));
		scene.question = question;		
		
		/*scene.presentationType = Presentation.LayerPresentation;
		scene.fixedLayers = Arrays.asList(Layer.IMINT);
		scene.optionalLayers = Arrays.asList(Layer.SIGINT, Layer.MASINT);*/
		scene = createSampleLayerPresentation(scene, presentationType);
		
		/*scene.layerProbabilityData = new ArrayList<LayerProbabilityData>();
		for(Layer layer : scene.layers) {
			LayerProbabilityData layerData = new LayerProbabilityData(layer);
			ArrayList<SectortProbabilityData> sectorProbabilityData = new ArrayList<SectortProbabilityData>();
			for(Sector sector : Sector.values()) {
				SectortProbabilityData sectorData = new SectortProbabilityData(sector);
				sectorData.setSceneItemProbabilityData(new ArrayList<SceneItemProbabilityData>(2));
				sectorData.getSceneItemProbabilityData().add(new SceneItemProbabilityData(1, .25));
				sectorData.getSceneItemProbabilityData().add(new SceneItemProbabilityData(2, .25));
				sectorProbabilityData.add(sectorData);
			}
			layerData.setSectorProbabilityData(sectorProbabilityData);
			scene.layerProbabilityData.add(layerData);
		}*/
		
		/*scene.sectorTruthData = new ArrayList<SectorTruthData>();
		for(Sector sector : Sector.values()) {
			SectorTruthData truthData = new SectorTruthData(sector);
			truthData.setSceneItemsPresent(new ArrayList<Integer>());
			if(sector == Sector.A) {
				truthData.getSceneItemsPresent().add(1);
			}
			else if(sector == Sector.C) {
				truthData.getSceneItemsPresent().add(2);
			}
			scene.sectorTruthData.add(truthData);
		}*/
		
		return scene;
	}

	@SuppressWarnings("deprecation")
	@Override
	public TestTrialType getTestTrialType() {
		return TestTrialType.ScenePresentation;
	}
}