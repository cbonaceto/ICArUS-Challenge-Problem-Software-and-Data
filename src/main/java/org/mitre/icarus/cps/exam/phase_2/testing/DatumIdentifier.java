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
package org.mitre.icarus.cps.exam.phase_2.testing;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * Contains a location ID, datum ID, and datum type to idenfity a datum item. One or more of these 
 * values may be set to uniquely identify a datum item. 
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="DatumIdentifier", namespace="IcarusCPD_2")
public class DatumIdentifier {
	
	/**
	 * The location ID that the datum is for.
	 */
	protected String locationId;
	
	/**
	 * The ID of the datum. 
	 */
	protected String datumId;
	
	/**
	 * The datum type of the datum.
	 */
	protected DatumType datumType;
	
	/**
	 * Construct an empty datum identifier.
	 */
	public DatumIdentifier() {}
	
	/**
	 * Construct a datum identifier with the given datum type.
	 * 
	 * @param datumType the datum type
	 */
	public DatumIdentifier(DatumType datumType) {
		this.datumType = datumType;
	}
	
	/**
	 * Construct a datum identifier with the given location ID, datum ID, and datum type.
	 * 
	 * @param locationId the location ID that the datum is for
	 * @param datumId the datum ID of the datum
	 * @param datumType the datum type of the datum
	 */
	public DatumIdentifier(String locationId, String datumId, DatumType datumType) {
		this.locationId = locationId;
		this.datumId = datumId;
		this.datumType = datumType;
	}

	/**
	 * Get the location ID that the datum is for.
	 * 
	 * @return the location ID
	 */
	@XmlAttribute(name="locationId")
	public String getLocationId() {
		return locationId;
	}

	/**
	 * Set the location ID that the datum is for.
	 * 
	 * @param locationId the location ID
	 */
	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	/**
	 * Get the ID of the datum. 
	 * 
	 * @return the datum ID
	 */
	@XmlAttribute(name="datumId")
	public String getDatumId() {
		return datumId;
	}

	/**
	 * Set the ID of the datum. 
	 * 
	 * @param datumId the datum ID
	 */
	public void setDatumId(String datumId) {
		this.datumId = datumId;
	}

	/**
	 * Get the datum type of the datum.
	 * 
	 * @return the datum type
	 */
	@XmlAttribute(name="datumType")
	public DatumType getDatumType() {
		return datumType;
	}

	/**
	 * Set the datum type of the datum.
	 * 
	 * @param datumType the datum type
	 */
	public void setDatumType(DatumType datumType) {
		this.datumType = datumType;
	}
}