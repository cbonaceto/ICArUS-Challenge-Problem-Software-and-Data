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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * A training annotation.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="Annotation", namespace="IcarusCPD_05", propOrder={"sectorId", "itemId"})
public class Annotation {
	/** The sector ID */
	protected Integer sectorId;
	
	/** The scene item ID */
	protected Integer itemId;

	/**
	 * Get the sector ID.
	 * 
	 * @return the sector ID
	 */
	@XmlElement(name="SectorId", required=true)
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
	 * Get the scene item ID.
	 * 
	 * @return the scene item ID
	 */
	@XmlElement(name="ItemId", required=true)
	public Integer getItemId() {
		return itemId;
	}

	/**
	 * Set the scene item ID.
	 * 
	 * @param itemId
	 */
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}	
}
