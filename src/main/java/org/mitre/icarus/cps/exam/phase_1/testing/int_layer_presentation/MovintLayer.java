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
package org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation;

import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.feature_vector.phase_1.IntType;

/**
 * The MOVINT layer type.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="MovintLayer", namespace="IcarusCPD_1")
public class MovintLayer extends IntLayer {

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.IntLayer#getLayerType()
	 */
	@Override
	public IntType getLayerType() {
		return IntType.MOVINT;
	}	
}