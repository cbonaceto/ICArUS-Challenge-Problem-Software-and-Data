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
package org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.int_presentation;

import java.util.List;

import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.phase_2.testing.DatumType;

/**
 * A probe to present OSINT at one or more locations.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="OsintPresentationProbe", namespace="IcarusCPD_2")
public class OsintPresentationProbe extends IntPresentationProbe {
	
	/**
	 * Construct an empty OSINT presentation probe. 
	 */
	public OsintPresentationProbe() {}
	
	/**
	 * Construct an OSINT presentation probe with the given location IDs and indexes.
	 * 
	 * @param locationIds the IDs of each location to present OSINT at
	 * @param locationIndexes the indexes of each location to present OSINT at
	 */
	public OsintPresentationProbe(List<String> locationIds, List<Integer> locationIndexes) {
		super(locationIds, locationIndexes);
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.int_presentation.IntPresentationProbe#getIntDatumType()
	 */
	@Override
	public DatumType getIntDatumType() {
		return DatumType.OSINT;
	}
}