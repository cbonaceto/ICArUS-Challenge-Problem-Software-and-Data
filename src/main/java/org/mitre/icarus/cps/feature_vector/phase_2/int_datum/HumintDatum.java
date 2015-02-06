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
 * Contains HUMINT information, which is the probability that Red has the capability to attack based on
 * the number of trials since the last Red attack.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="HumintDatum", namespace="IcarusCPD_2")
public class HumintDatum extends IntDatum {
	
	/** The number of trials since the last Red attack */
	protected Integer numTrialsSinceLastAttack;

	/** The probability that Red has the capability to attack (Pc) */
	protected Double redCapability_Pc;
	
	/**
	 * Construct an empty HumintDatum.
	 */
	public HumintDatum() {}
	
	/**
	 * Construct a HumintDatum with the given number of trials since the last Red attack and probability that
	 * Red has the capability to attack (in decimal format).
	 * 
	 * @param numTrialsSinceLastAttack the number of trials since the last Red attack
	 * @param redCapability_Pc the probability that Red has the capability to attack
	 */
	public HumintDatum(Integer numTrialsSinceLastAttack, Double redCapability_Pc) {
		this.numTrialsSinceLastAttack = numTrialsSinceLastAttack;
		this.redCapability_Pc = redCapability_Pc;
	}

	/**
	 * Get the number of trials since the last Red attack.
	 * 
	 * @return the number of trials
	 */
	public Integer getNumTrialsSinceLastAttack() {
		return numTrialsSinceLastAttack;
	}

	/**
	 * Set the number of trials since the last Red attack.
	 * 
	 * @param numTrialsSinceLastAttack the number of trials
	 */
	public void setNumTrialsSinceLastAttack(Integer numTrialsSinceLastAttack) {
		this.numTrialsSinceLastAttack = numTrialsSinceLastAttack;
	}

	/**
	 * Get the probability that Red has the capability to attack.
	 * 
	 * @return the probability that Red has the capability to attack
	 */
	@XmlAttribute(name="redCapability_Pc")
	public Double getRedCapability_Pc() {
		return redCapability_Pc;
	}

	/** 
	 * Set the probability that Red has the capability to attack.
	 * 
	 * @param redCapability_Pc the probability that Red has the capability to attack
	 */
	public void setRedCapability_Pc(Double redCapability_Pc) {
		this.redCapability_Pc = redCapability_Pc;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.IDatum#getDatumType()
	 */
	@Override
	public DatumType getDatumType() {
		return DatumType.HUMINT;
	}
}