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

import javax.xml.bind.annotation.XmlType;

/**
 * An enumeration of datum types in Phase 2. Datum are information items (e.g., IMINT report of "U", OSINT report of "P") used
 * to make inferences and/or decisions. 
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="DatumType", namespace="IcarusCPD_2")
public enum DatumType {
	HUMINT,
	IMINT,
	OSINT,
	SIGINT,
	SIGINTReliability,
	AttackProbabilityReport,
	AttackProbabilityReport_Propensity,
	AttackProbabilityReport_Capability_Propensity,
	AttackProbabilityReport_Activity,
	AttackProbabilityReport_Activity_Capability_Propensity,
	BlueBook,
	PayoffMatrix,
	BatchPlots,
	RedTacticProbabilityReport;
	
	/**
	 * Return whether the given datum type is an INT datum type, which include
	 * HUMINT, IMINT, OSINT, and SIGINT.
	 * 
	 * @param datumType the datum type
	 * @return whether the datum type is HUMINT, IMINT, OSINT, or SIGINT
	 */
	public static boolean isIntDatumType(DatumType datumType) {
		return datumType == HUMINT || datumType == IMINT ||
				datumType == OSINT || datumType == SIGINT;
	}
}