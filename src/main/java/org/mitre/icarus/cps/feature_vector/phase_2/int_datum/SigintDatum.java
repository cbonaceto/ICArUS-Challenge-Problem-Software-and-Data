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
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.phase_2.testing.DatumType;

/**
 * Contains SIGINT information at a location, which is whether Red activity was detected at the location. May 
 * also contain the probability of Red attack based on the SIGINT information.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="SigintDatum", namespace="IcarusCPD_2")
public class SigintDatum extends IntDatum {
	/**
	 * An enumeration of SIGINT detection types (Chatter or Silence)
	 * 
	 * @author CBONACETO
	 *
	 */
	public static enum SigintType {Chatter, Silence};
	
	/** The SIGINT detection radius about the location (in miles) */
	protected Double radius_miles;
	
	/** Whether Red activity was detected at the location */
	protected Boolean redActivityDetected;
	
	/** The probability that Red is actually planning an attack at the location based on the SIGINT detection */	
	protected Double redAttackProbability_Pt;
	
	/**
	 * Construct an empty SigintDatum. 
	 */
	public SigintDatum() {}
	
	/**
	 * Construct a SigintDatum with the given SIGINT detection type (Chatter or Silence) at the location. If sigintType is Chatter,
	 * redActivityDetected is set to true. If sigintType is Silence, redActivityDetected is set to false.
	 * 
	 * @param sigintType the SIGINT detection type (Chatter or Silence)
	 */
	public SigintDatum(SigintType sigintType) {
		redActivityDetected = sigintType != null && sigintType == SigintType.Chatter ? true : false;
	}
	
	/**
	 * Construct a SigintDatum with the given boolean indicating whether or not Red activity was detected at the location.
	 * 
	 * @param redActivityDetected whether Red activity was detected at the location
	 */
	public SigintDatum(Boolean redActivityDetected) {
		this.redActivityDetected = redActivityDetected;
	}
	
	/**
	 * Construct a SigintDatum with the given boolean indicating whether or not Red activity was detected at the location,
	 * and the SIGINT detection radius (in miles) around the location.
	 * 
	 * @param redActivityDetected whether Red activity was detected at the location
	 * @param radius_miles the SIGINT detection radius (in miles) around the location
	 */
	public SigintDatum(Boolean redActivityDetected, Double radius_miles) {
		this.redActivityDetected = redActivityDetected;
		this.radius_miles = radius_miles;
	}

	/**
	 * Get the SIGINT detection radius (in miles) around the location.
	 * 
	 * @return the SIGINT detection radius (in miles)
	 */
	@XmlAttribute(name="radius_miles")
	public Double getRadius_miles() {
		return radius_miles;
	}

	/**
	 * Set the SIGINT detection radius (in miles) around the location.
	 * 
	 * @param radius_miles the SIGINT detection radius (in miles)
	 */
	public void setRadius_miles(Double radius_miles) {
		this.radius_miles = radius_miles;
	}

	/**
	 * Get whether Red activity was detected at the location.
	 * 
	 * @return whether Red activity was detected at the location
	 */
	@XmlAttribute(name="redActivityDetected")
	public Boolean isRedActivityDetected() {
		return redActivityDetected;
	}

	/**
	 * Set whether Red activity was detected at the location.
	 * 
	 * @param redActivityDetected whether Red activity was detected at the location
	 */
	public void setRedActivityDetected(Boolean redActivityDetected) {
		this.redActivityDetected = redActivityDetected;
	}
	
	/**
	 * Convenience method to get the SIGINT detection type (Chatter or Silence) based on whether
	 * Red activity was detected at the location (indicated by the redActivityDetected member variable).
	 * Returns Chatter if redActivityDetected is true, and Silence if redActivityDetected is null or false.
	 * 
	 * @return
	 */
	public SigintType getSigintType() {
		return redActivityDetected != null && redActivityDetected ? SigintType.Chatter : SigintType.Silence;
	}

	/**
	 * Get the probability that Red is actually planning an attack at the location based on the SIGINT detection
	 * and SIGINT detection reliabilities.
	 * 
	 * @return the probability that Red is actually planning an attack at the location (in decimal format)
	 */
	@XmlAttribute(name="redAttackProbability")
	public Double getRedAttackProbability_Pt() {
		return redAttackProbability_Pt;
	}

	/**
	 * Set the probability that Red is actually planning an attack at the location based on the SIGINT detection
	 * and SIGINT detection reliabilities. 
	 * 
	 * @param redAttackProbability_Pt the probability that Red is actually planning an attack at the location (in decimal format)
	 */
	public void setRedAttackProbability_Pt(Double redAttackProbability_Pt) {
		this.redAttackProbability_Pt = redAttackProbability_Pt;
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.IDatum#getDatumType()
	 */
	@Override
	public DatumType getDatumType() {
		return DatumType.SIGINT;
	}
}