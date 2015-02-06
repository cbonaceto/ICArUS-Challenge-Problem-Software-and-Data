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
package org.mitre.icarus.cps.exam.phase_05.training;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

/**
 * Annotation training trial.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="Annotations", namespace="IcarusCPD_05")
public class AnnotationTrainingTrial extends IcarusTrainingTrial {	
	
	/** URL to the feature vector file for the scene */
	protected String featureVectorUrl;
	
	/** URL to the object palette file for the scene */
	protected String objectPaletteUrl;
	
	/** The annotations */
	protected ArrayList<Annotation> annotations;	
	
	/** The IDs of each layers to show */
	protected List<Integer> baseLayers;
	
	@Override
	public TrainingTrialType getTrainingTrialType() {
		return TrainingTrialType.Annotation;
	}

	/**
	 * Get the URL to the feature vector file for the scene.
	 * 
	 * @return
	 */
	@XmlElement(name="FeatureVectorUrl")
	public String getFeatureVectorUrl() {
		return featureVectorUrl;
	}

	/**
	 * Set the URL to the feature vector file for the scene.
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
	 * Get the annotations.
	 * 
	 * @return
	 */
	@XmlElementWrapper(name="Annotations")
	@XmlElement(name="Annotation")
	public ArrayList<Annotation> getAnnotations() {
		return annotations;
	}

	/**
	 * Set the annotations.
	 * 
	 * @param annotations
	 */
	public void setAnnotations(ArrayList<Annotation> annotations) {
		this.annotations = annotations;
	}
	
	/**
	 * Get the IDs of each layer to show. 
	 * 
	 * @return
	 */
	@XmlElementWrapper(name="BaseLayers")
	@XmlElement(name="LayerId")
	public List<Integer> getBaseLayers() {
		return baseLayers;
	}

	/**
	 * Set the IDs of each layer to show. 
	 * 
	 * @param baseLayers
	 */
	public void setBaseLayers(List<Integer> baseLayers) {
		this.baseLayers = baseLayers;
	}
}
