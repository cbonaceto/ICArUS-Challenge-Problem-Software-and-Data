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
package org.mitre.icarus.cps.app.window.configuration;

import org.mitre.icarus.cps.app.window.phase_1.Phase_1_Configuration;
import org.mitre.icarus.cps.app.window.phase_2.Phase_2_Configuration;

/**
 * Contains static methods for creating the Phase 1 and Phase 2 configurations.
 * 
 * @author CBONACETO
 *
 */
public class PhaseConfigurationFactory {
	
	/**
	 * @param phaseId
	 * @return
	 */
	public static PhaseConfiguration<?, ?, ?, ?, ?> createPhaseConfiguration(String phaseId) {
		if(phaseId != null) {
			if(phaseId.equals("1")) {
				Phase_1_Configuration configuration = new Phase_1_Configuration();
				configuration.setPhaseId(phaseId);
				return configuration;
			} else if(phaseId.equals("2")) {
				Phase_2_Configuration configuration = new Phase_2_Configuration();
				configuration.setPhaseId(phaseId);
				return configuration;
			} else {
				throw new IllegalArgumentException("Phase " + phaseId + " is not supported by this version of the software.");
			}
		} else {
			throw new IllegalArgumentException("Phase must be specified, cannot create phase controller.");
		}
	}
}