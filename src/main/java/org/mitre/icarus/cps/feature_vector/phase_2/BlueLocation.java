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
package org.mitre.icarus.cps.feature_vector.phase_2;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.feature_vector.phase_2.int_datum.ImintDatum;
import org.mitre.icarus.cps.feature_vector.phase_2.int_datum.OsintDatum;
import org.mitre.icarus.cps.feature_vector.phase_2.int_datum.SigintDatum;

import de.micromata.opengis.kml.v_2_2_0.Data;
import de.micromata.opengis.kml.v_2_2_0.ExtendedData;
import de.micromata.opengis.kml.v_2_2_0.Placemark;

/**
 * A Blue location (at a point on the earth). Contains the OSINT, IMINT, and SIGINT at the location.
 * 
 * @author CBONACETO
 *
 */
@XmlRootElement(name="BlueLocation", namespace="IcarusCPD_2")
@XmlType(name="BlueLocation", namespace="IcarusCPD_2", propOrder={"name", "trialNumber", "osint",
		"imint", "sigint"})
public class BlueLocation extends Point implements Comparable<BlueLocation> {
	
	/** The location name */
	protected String name;
	
	/** The trial number */
	protected Integer trialNumber;	
	
	/** OSINT datum at the location */
	protected OsintDatum osint;
	
	/** IMINT datum at the location */
	protected ImintDatum imint;		
	
	/** SIGINT datum at the location */
	protected SigintDatum sigint;

	/**
	 * Get the location name.
	 * 
	 * @return the location name
	 */
	@XmlAttribute(name="name")
	public String getName() {
		return name;
	}

	/**
	 * Set the location name.
	 * 
	 * @param name the location name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the trial number the location is for.
	 * 
	 * @return the trial number
	 */
	@XmlAttribute(name="trialNumber")
	public Integer getTrialNumber() {
		return trialNumber;
	}

	/**
	 * Set the trial number the location is for.
	 * 
	 * @param trialNumber the trial number
	 */
	public void setTrialNumber(Integer trialNumber) {
		this.trialNumber = trialNumber;
	}

	/**
	 * Get the OSINT datum at the location.
	 * 
	 * @return the OSINT datum
	 */
	@XmlElement(name="Osint")
	public OsintDatum getOsint() {
		return osint;
	}

	/**
	 * Set the OSINT datum at the location.
	 * 
	 * @param osint the OSINT datum
	 */
	public void setOsint(OsintDatum osint) {
		this.osint = osint;
	}

	/**
	 * Get the IMINT datum at the location.
	 * 
	 * @return the IMINT datum
	 */
	@XmlElement(name="Imint")
	public ImintDatum getImint() {
		return imint;
	}

	/**
	 * Set the OSINT datum at the location.
	 * 
	 * @param imint the IMINT datum
	 */
	public void setImint(ImintDatum imint) {
		this.imint = imint;
	}

	/**
	 * Get the SIGINT datum at the location.
	 * 
	 * @return the SIGINT datum
	 */
	@XmlElement(name="Sigint")
	public SigintDatum getSigint() {
		return sigint;
	}

	/**
	 * Set the SIGINT datum at the location.
	 * 
	 * @param sigint the SIGINT datum
	 */
	public void setSigint(SigintDatum sigint) {
		this.sigint = sigint;
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.feature_vector.phase_2.Point#getKMLRepresentation()
	 */
	@Override
	public Placemark getKMLRepresentation() {
		Placemark placemark = super.getKMLRepresentation();
		placemark.setName(name);
		ExtendedData ed = new ExtendedData();
		if(trialNumber != null) {
			Data d = ed.createAndAddData(trialNumber.toString());
			d.setName("trialNumber");
		}
		if(osint != null && osint.getRedVulnerability_P() != null) {
			Data d = ed.createAndAddData(osint.getRedVulnerability_P().toString());
			d.setName("osint_P");
			
		}
		if(imint != null && imint.getRedOpportunity_U() != null) {
			Data d = ed.createAndAddData(imint.getRedOpportunity_U().toString());
			d.setName("imint_U");
			if(imint.getRadius_miles() != null) {
				d = ed.createAndAddData(osint.getRadius().toString());
				d.setName("imint_radius_miles");
			}
			
		}
		if(sigint != null && sigint.isRedActivityDetected() != null) {
			Data d = ed.createAndAddData(sigint.getSigintType().toString());
			d.setName("sigint_activity");
			if(sigint.getRadius_miles() != null) {
				d = ed.createAndAddData(sigint.getRadius_miles().toString());
				d.setName("sigint_radius_miles");
			}
		}
		if(ed.getData() != null && !ed.getData().isEmpty()) {
			placemark.setExtendedData(ed);
		}
		return placemark;
	}

	/**
	 * Northernmost locations are at the front of the queue.
	 */
	@Override
	public int compareTo(BlueLocation other) {
		if(this.location.getLat() < other.getLocation().getLat()) {
			return 1;
		} else if(this.location.getLat() == other.getLocation().getLat()) {
			return 0;
		} 
		return -1;
	}
}