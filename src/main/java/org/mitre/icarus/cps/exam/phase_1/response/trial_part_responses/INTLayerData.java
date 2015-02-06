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
package org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.IntLayer;

/**
 * Information about an INT layer that was shown or selected in a Task 5 or 6 trial.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="INTLayerData", namespace="IcarusCPD_1", 
		propOrder={"layerType", "userSelected", "layerSelectionTime_ms"})
public class INTLayerData {
	
	/** The INT layer type */
	private IntLayer layerType;	
	
	/** Whether the layer was selected or purchased by the subject/model (only true for Tasks 6-7) */
	private Boolean userSelected = false;
	
	/** The time to select or purchase the INT layer if it was user selected (Tasks 6-7 only) */
	protected Long layerSelectionTime_ms;	
	
	/**
	 * No arg constructor.
	 */
	public INTLayerData() {}
	
	/**
	 * Constructor that takes the layerTYpe and whether it was user selected.
	 * 
	 * @param layerType the layer type
	 * @param userSelected whether the layer was user selected
	 */
	public INTLayerData(IntLayer layerType, Boolean userSelected) {
		this(layerType, userSelected, null);
	}
	
	/**
	 * Constructor that takes the layerTYpe, whether it was user selected, and the time to select the layer.
	 * FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @param layerType the layer type
	 * @param userSelected whether the layer was user selected
	 * @param layerSelectionTime_ms the time to select the layer in milliseconds
	 */
	public INTLayerData(IntLayer layerType, Boolean userSelected, Long layerSelectionTime_ms) {
		this.layerType = layerType;
		this.userSelected = userSelected;
		this.layerSelectionTime_ms = layerSelectionTime_ms;
	}

	/**
	 * Get the layer type.
	 * 
	 * @return the layer type
	 */
	@XmlElement(name="LayerType")
	public IntLayer getLayerType() {
		return layerType;
	}

	/**
	 * Set the layer type.
	 * 
	 * @param layerType the layer type
	 */
	public void setLayerType(IntLayer layerType) {
		this.layerType = layerType;
	}

	/**
	 * Get whether the layer was user selected.
	 * 
	 * @return whether the layer was user selected
	 */
	@XmlAttribute(name="userSelected")
	public Boolean isUserSelected() {
		return userSelected;
	}

	/**
	 * Set whether the layer was user selected.
	 * 
	 * @param userSelected whether the layer was user selected
	 */
	public void setUserSelected(Boolean userSelected) {
		this.userSelected = userSelected;
	}	
	
	/**
	 * Get the layer selection time in milliseconds.
	 * 
	 * @return the layer selection time in milliseconds.
	 */
	@XmlAttribute(name="layerSelectionTime_ms")
	public Long getLayerSelectionTime_ms() {
		return layerSelectionTime_ms;
	}

	/**
	 * Set the layer selection time in milliseconds.
	 * 
	 * @param layerSelectionTime_ms the layer selection time in milliseconds.
	 */
	public void setLayerSelectionTime_ms(Long layerSelectionTime_ms) {
		this.layerSelectionTime_ms = layerSelectionTime_ms;
	}
}