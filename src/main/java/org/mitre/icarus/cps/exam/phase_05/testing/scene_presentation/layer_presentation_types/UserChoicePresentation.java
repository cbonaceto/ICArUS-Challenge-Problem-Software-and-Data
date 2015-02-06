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
 * User choice presentation type.  In a user choice presentation, all base layers are 
 * first presented, then the human or model selects layers to see next.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="UserChoicePresentation", namespace="IcarusCPD_05")
public class UserChoicePresentation extends LayerPresentation {
	/** The optional layers to choose from */
	private LayerList optionalLayers;

	/** The total number of optional layers to show.  If null, 
	 * all optional layers will be shown. */
	private Integer numOptionalLayersToShow;
	
	/**
	 * Get the IDs of the optional layers to choose from.
	 * 
	 * @return
	 */
	@XmlElement(name="OptionalLayers")
	public LayerList getOptionalLayers() {
		return optionalLayers;
	}

	/**
	 * Set the IDs of the optional layers to choose from.
	 * 
	 * @param optionalLayers
	 */
	public void setOptionalLayers(LayerList optionalLayers) {
		this.optionalLayers = optionalLayers;
	}

	/**
	 * Get the total number of optional layers to show.  If null, 
	 * all optional layers will be shown. 
	 * 
	 * @return
	 */
	public Integer getNumOptionalLayersToShow() {
		return numOptionalLayersToShow;
	}

	/**
	 * Set the total number of optional layers to show.  If null, 
	 * all optional layers will be shown. 
	 * 
	 * @param numOptionalLayersToShow
	 */
	public void setNumOptionalLayersToShow(Integer numOptionalLayersToShow) {
		this.numOptionalLayersToShow = numOptionalLayersToShow;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.testing.scene_presentation.layer_presentation_types.LayerPresentation#getPresentationType()
	 */
	@Override
	public PresentationType getPresentationType() {
		return PresentationType.UserChoice;
	}
}
