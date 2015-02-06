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
package org.mitre.icarus.cps.feature_vector.phase_1;

import javax.xml.bind.annotation.XmlType;

/**
 * The Enum SigintType.
 */
@XmlType(name="SigintType", namespace="IcarusCPD_1")
public enum SigintType {
	Silent,
	Chatter;

	// indexing into the items as they are ordered above
	/**
	 * Gets the sigint type by indexing into SigintType enum.  
	 * Parameter is CSV INT value:
	 * 1 = Silent, 2 = Chatter.
	 *
	 * @param i MOVINT value
	 * @return the sigint type
	 */
	public static SigintType getSigintType( int i ) {
		return SigintType.values()[i-1];
	}
}