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
package org.mitre.icarus.cps.exam.phase_05.response;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.phase_05.testing.scene_presentation.layer_presentation_types.LayerPresentation.PresentationType;

/**
 * Simply contains a layer ID, how the layer was presented,
 * and whether the layer was selected by the human subject
 * or model.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="layerData", namespace="IcarusCPD_05")
public class LayerData {	
	/** The layer ID */
	private Integer layerID;
	
	/** How the layer was presented */	
	private PresentationType presentationType;
	
	/** Whether the layer was selected */
	private boolean selected = false;
	
	public LayerData() {}
	
	public LayerData(Integer layerId) {
		this(layerId, null);
	}
	
	public LayerData(Integer layerID, PresentationType presentationType) {
		this.layerID = layerID;
		this.presentationType = presentationType;
	}

	/**
	 * Get the layer ID.
	 * 
	 * @return
	 */
	@XmlAttribute(name="LayerId", required=true)
	public Integer getLayerID() {
		return layerID;
	}

	/**
	 * Set the layer ID.
	 * 
	 * @param layerID
	 */
	public void setLayerID(Integer layerID) {
		this.layerID = layerID;
	}	
	
	/**
	 * Get how the layer was presented.
	 * 
	 * @return
	 */
	@XmlAttribute(name="PresentationType")
	public PresentationType getPresentationType() {
		return presentationType;
	}

	/**
	 * Set how the layer was presented
	 * 
	 * @param presentationType
	 */
	public void setPresentationType(PresentationType presentationType) {
		this.presentationType = presentationType;
	}
	
	/**
	 * Get whether the layer was selected by the human subject or model.
	 * 
	 * @return
	 */
	@XmlTransient
	public boolean isSelected() {
		return selected;
	}

	/**
	 * Set whether the layer was selected by the human subject or model.
	 * 
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	@Override
	public String toString() {
		return layerID.toString();
	}
}
