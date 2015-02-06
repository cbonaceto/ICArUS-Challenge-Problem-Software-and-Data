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
 * The Enum ImintType.
 */
@XmlType(name="ImintType", namespace="IcarusCPD_1")
public enum ImintType {
	Government,
	Military;
	
	// indexes into the items as they are ordered above
	/**
	 * Gets the imint type by indexing into ImintType enum.  
	 * Parameter is CSV INT value:
	 * 1 = Government, 2 = Military.
	 *
	 * @param i IMINT value
	 * @return the imint type
	 */
	public static ImintType getImintType( int i ) {
		return ImintType.values()[i-1];
	}
}
