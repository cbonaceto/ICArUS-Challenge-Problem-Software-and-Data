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
package org.mitre.icarus.cps.exam.phase_05.testing.scene_presentation;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Contains the sector ID and name.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="Sector", namespace="IcarusCPD_05")
public class Sector {
	/** The sector ID */
	private Integer sectorID;
	
	/** The sector name */
	private String sectorName;

	/**
	 * Get the sector ID.
	 * 
	 * @return
	 */
	@XmlElement
	public Integer getSectorID() {
		return sectorID;
	}

	/**
	 * Set the sector ID.
	 * 
	 * @param sectorID
	 */
	public void setSectorID(Integer sectorID) {
		this.sectorID = sectorID;
	}

	/**
	 * Get the sector name.
	 * 
	 * @return
	 */
	@XmlElement
	public String getSectorName() {
		return sectorName;
	}

	/**
	 * Set the sector name.
	 * 
	 * @param sectorName
	 */
	public void setSectorName(String sectorName) {
		this.sectorName = sectorName;
	}
}
