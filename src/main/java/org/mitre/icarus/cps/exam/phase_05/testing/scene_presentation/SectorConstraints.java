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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Defines the min/max possible number of scene items in a sector.
 * 
 * FOR HUMAN SUBJECT USE ONLY.
 * 
 * @author CBONACETO
 *
 */
@XmlType(namespace="IcarusCPD_05")
public class SectorConstraints {
	/** The sector the constraints are for */
	protected Integer sectorID;
	
	/** The minimum number of facilities present in the sector */
	protected Integer minFacilitiesInSector;
	
	/** The maximum number of facilities present in the sector */
	protected Integer maxFacilitiesInSector;
	
	/** The minimum number of objects present in the sector */
	protected Integer minObjectsInSector;
	
	/** The maximum number of objects present in the sector */
	protected Integer maxObjectsInSector;
	
	/** The minimum number of events present in the sector */
	protected Integer minEventsInSector;
	
	/** The maximum number of events present in the sector */
	protected Integer maxEventsInSector;
	
	public SectorConstraints() {}
	
	public SectorConstraints(Integer sectorID) {
		this.sectorID = sectorID;
	}	
	
	@XmlAttribute(name="SectorId")	
	public Integer getSectorID() {
		return sectorID;
	}

	public void setSectorID(Integer sectorID) {
		this.sectorID = sectorID;
	}

	@XmlElement(name="MinFacilitiesInSector")
	public Integer getMinFacilitiesInSector() {
		return minFacilitiesInSector;
	}	
	
	public void setMinFacilitiesInSector(Integer minFacilitiesInSector) {
		this.minFacilitiesInSector = minFacilitiesInSector;
	}
	
	@XmlElement(name="MaxFacilitiesInSector")
	public Integer getMaxFacilitiesInSector() {
		return maxFacilitiesInSector;
	}
	
	public void setMaxFacilitiesInSector(Integer maxFacilitiesInSector) {
		this.maxFacilitiesInSector = maxFacilitiesInSector;
	}
	
	@XmlElement(name="MinObjectsInSector")
	public Integer getMinObjectsInSector() {
		return minObjectsInSector;
	}
	
	public void setMinObjectsInSector(Integer minObjectsInSector) {
		this.minObjectsInSector = minObjectsInSector;
	}
	
	@XmlElement(name="MaxObjectsInSector")
	public Integer getMaxObjectsInSector() {
		return maxObjectsInSector;
	}
	
	public void setMaxObjectsInSector(Integer maxObjectsInSector) {
		this.maxObjectsInSector = maxObjectsInSector;
	}
	
	@XmlElement(name="MinEventsInSector")
	public Integer getMinEventsInSector() {
		return minEventsInSector;
	}
	
	public void setMinEventsInSector(Integer minEventsInSector) {
		this.minEventsInSector = minEventsInSector;
	}
	
	@XmlElement(name="MaxEventsInSector")
	public Integer getMaxEventsInSector() {
		return maxEventsInSector;
	}
	
	public void setMaxEventsInSector(Integer maxEventsInSector) {
		this.maxEventsInSector = maxEventsInSector;
	}
}
