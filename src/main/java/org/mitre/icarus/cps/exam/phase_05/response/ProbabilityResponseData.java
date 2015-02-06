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

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;


/**
 * Abstract base class for scene item or sector probability response data to an identify item or 
 * locate item trial.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="ProbabilityData", namespace="IcarusCPD_05")
public class ProbabilityResponseData {
	/** The layers that are currently shown (either selected or presented) */
	private List<LayerData> layersShown;
	
	/** The time spent selecting a layer in ms 
	 * (collected for human subjects only, only for user select layer presentations) */
	private Long layerSelectionTime_ms;
	
	/** The time spent entering probabilities in ms 
	 * (collected for human subjects only) */
	private Long probabilityEntryTime_ms;

	/**
	 * Get the layers that are currently shown (either selected or presented).
	 * 
	 * @return
	 */
	@XmlElementWrapper(name="LayersShown")
	@XmlElement(name="Layer")
	public List<LayerData> getLayersShown() {
		return layersShown;
	}

	/**
	 * Set the layers that are currently shown (either selected or presented).
	 * 
	 * @param layersShown
	 */
	public void setLayersShown(List<LayerData> layersShown) {
		this.layersShown = layersShown;
	}
	
	/**
	 * Get the time spent selecting a layer in ms (only for user select layer presentations), FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @return
	 */
	@XmlElement(name="LayerSelectionTime_ms")
	public Long getLayerSelectionTime_ms() {
		return layerSelectionTime_ms;
	}

	/**
	 * Set the time spent selecting a layer in ms (only for user select layer presentations), FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @param layerSelectionTime_ms
	 */
	public void setLayerSelectionTime_ms(Long layerSelectionTime_ms) {
		this.layerSelectionTime_ms = layerSelectionTime_ms;
	}

	/**
	 * Get the time spent entering probabilities in ms, FOR HUMAN SUBJECT USE ONLY. 
	 * 
	 * @return
	 */
	@XmlElement(name="ProbabilityEntryTime_ms")
	public Long getProbabilityEntryTime_ms() {
		return probabilityEntryTime_ms;
	}

	/**
	 * Set the time spent entering probabilities in ms, FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @param probabilityEntryTime_ms
	 */
	public void setProbabilityEntryTime_ms(Long probabilityEntryTime_ms) {
		this.probabilityEntryTime_ms = probabilityEntryTime_ms;
	}
}
