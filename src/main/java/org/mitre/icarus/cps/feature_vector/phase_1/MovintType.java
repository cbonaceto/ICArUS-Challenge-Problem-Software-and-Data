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
 * The Enum MovintType.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="MovintType", namespace="IcarusCPD_1")
public enum MovintType {
	SparseTraffic,
	DenseTraffic;
	
	// indexes into the items as they are ordered above
	/**
	 * Gets the movint type by indexing into MovintType enum.  
	 * Parameter is CSV INT value:
	 * 1 = SparseTraffic, 2 = DenseTraffic.
	 *
	 * @param i MOVINT value
	 * @return the movint type
	 */
	public static MovintType getMovintType( int i ) {
		return MovintType.values()[i-1];
	}
}