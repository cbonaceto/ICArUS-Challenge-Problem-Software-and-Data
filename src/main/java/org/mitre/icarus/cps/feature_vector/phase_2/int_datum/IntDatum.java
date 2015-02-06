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
package org.mitre.icarus.cps.feature_vector.phase_2.int_datum;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.phase_2.testing.AbstractDatum;

/**
 * Abstract base class for INT datum types, which include OSINT, IMINT, HUMINT, and SIGINT.
 * INTs are typically associated with a location (except HUMINT, which applies to all locations).
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="IntDatum", namespace="IcarusCPD_2")
@XmlSeeAlso({HumintDatum.class, ImintDatum.class, OsintDatum.class, SigintDatum.class})
public abstract class IntDatum extends AbstractDatum {	
	
	/** The ID of the location the INT datum is for (if location specified) */
	protected String locationId;

	/**
	 * Get the ID of the location the INT datum is for.
	 * 
	 * @return the location ID
	 */
	@XmlAttribute(name="locationId")
	public String getLocationId() {
		return locationId;
	}

	/**
	 * Set the ID of the location the INT datum is for.
	 * 
	 * @param locationId the location ID
	 */
	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}
}