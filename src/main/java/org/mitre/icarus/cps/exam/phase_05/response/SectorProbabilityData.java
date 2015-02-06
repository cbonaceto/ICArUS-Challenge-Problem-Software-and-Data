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
import javax.xml.bind.annotation.XmlType;

/**
 * Contains 
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="SectorProbability", namespace="IcarusCPD_05", 
		propOrder={"sectorId", "probability"})
public class SectorProbabilityData {
	/** The sector ID */
	protected Integer sectorId;

	/** The probability the scene item probed is present
	 * in the sector (normalized) */
	protected Double probability;
	
	/** The probability the scene item probed is present
	 * in the sector (raw, not normalized) */
	protected Double rawProbability;
	
	public SectorProbabilityData() {}
	
	public SectorProbabilityData(Integer sectorId, Double probability) {
		this(sectorId, probability, null);
	}
	
	public SectorProbabilityData(Integer sectorId, Double probability, Double rawProbability) {
		this.sectorId = sectorId;
		this.probability = probability;
		this.rawProbability = rawProbability;
	}
	
	/**
	 * Get the sector ID.
	 * 
	 * @return
	 */
	@XmlAttribute(name="SectorId", required=true)
	public Integer getSectorId() {
		return sectorId;
	}

	/**
	 * Set the sector ID.
	 * 
	 * @param sectorId
	 */
	public void setSectorId(Integer sectorId) {
		this.sectorId = sectorId;
	}

	
	/**
	 * Get the probability that the the scene item probed is present
	 * in the sector (normalized).
	 * 
	 * @return
	 */
	@XmlAttribute(name="Probability", required=true)
	public Double getProbability() {
		return probability;
	}

	/**
	 * Set the probability that the the scene item probed is present
	 * in the sector (normalized).
	 * 
	 * @param probability
	 */
	public void setProbability(Double probability) {
		this.probability = probability;
	}

	/**
	 * Get the probability that the the scene item probed is present
	 * in the sector (raw, not normalized).
	 * 
	 * @return
	 */
	@XmlAttribute(name="RawProbability")
	public Double getRawProbability() {
		return rawProbability;
	}

	/**
	 * Set the probability that the the scene item probed is present
	 * in the sector (raw, not normalized).
	 * 
	 * @return
	 */
	public void setRawProbability(Double rawProbability) {
		this.rawProbability = rawProbability;
	}
}
