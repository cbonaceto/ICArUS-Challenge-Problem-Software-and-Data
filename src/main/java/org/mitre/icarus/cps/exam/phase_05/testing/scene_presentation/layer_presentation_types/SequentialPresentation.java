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
package org.mitre.icarus.cps.exam.phase_05.testing.scene_presentation.layer_presentation_types;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Sequential presentation type.  In a sequential presentation, all base layers are 
 * first presented, then each sequential layer or layers is presented in order.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="SequentialPresentation", namespace="IcarusCPD_05")
public class SequentialPresentation extends LayerPresentation {
	
	/** Layers to present in sequence */
	private List<LayerList> sequentialLayers;
	
	
	/**
	 * Get the layers to present in sequence.
	 * 
	 * @return
	 */
	@XmlElement(name="Layers")
	public List<LayerList> getSequentialLayers() {
		return sequentialLayers;
	}

	/**
	 * Set the layers to present in sequence.
	 * 
	 * @param sequentialLayers
	 */
	public void setSequentialLayers(List<LayerList> sequentialLayers) {
		this.sequentialLayers = sequentialLayers;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.testing.scene_presentation.layer_presentation_types.LayerPresentation#getPresentationType()
	 */
	@Override
	public PresentationType getPresentationType() {
		return PresentationType.Sequential;
	}
}
