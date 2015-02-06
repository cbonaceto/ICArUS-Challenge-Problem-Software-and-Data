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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Simultaneous presentation type. In a simultaneous presentation, all layers are 
 * presented at once.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="SimultaneousPresentation", namespace="IcarusCPD_05")
public class SimultaneousPresentation extends LayerPresentation {
	/** The IDs of the layers to present simultaneously */
	private LayerList simultaneousLayers;

	/**
	 * Get the IDs of the layers to present simultaneously.
	 * 
	 * @return
	 */
	@XmlElement(name="SimultaneousLayers")
	public LayerList getSimultaneousLayers() {
		return simultaneousLayers;
	}

	/**
	 * Set the IDs of the layers to present simultaneously.
	 * 
	 * @param simultaneousLayers
	 */
	public void setSimultaneousLayers(LayerList simultaneousLayers) {
		this.simultaneousLayers = simultaneousLayers;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.testing.scene_presentation.layer_presentation_types.LayerPresentation#getPresentationType()
	 */
	@Override
	public PresentationType getPresentationType() {
		return PresentationType.Simultaneous;
	}	
}
