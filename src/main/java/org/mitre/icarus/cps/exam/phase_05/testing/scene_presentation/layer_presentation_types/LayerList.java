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

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Contains a list of layers to present.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="LayerList", namespace="IcarusCPD_05")
public class LayerList {
	//private static final long serialVersionUID = 1L;
	
	private List<Integer> layers;
	
	public LayerList() {}
	
	public LayerList(Collection<Integer> layers) {
		this.layers = new LinkedList<Integer>(layers);
	}

	/**
	 * Get the IDs of each layer.
	 * 
	 * @return
	 */
	@XmlElement(name="LayerId")
	public List<Integer> getLayers() {		
		return layers;
	}

	/**
	 * Set the IDs of each layer.
	 * 
	 * @param layers
	 */
	public void setLayers(List<Integer> layers) {
		this.layers = layers;
	}
	
	/**
	 * Get the number of layers.
	 * 
	 * @return
	 */
	public int getNumLayers() {
		if(layers != null) {
			return layers.size();
		}
		return 0;
	}
}
