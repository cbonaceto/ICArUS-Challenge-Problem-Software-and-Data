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
package org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes;

import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

/**
 * Abstract Base class for probes in a trial (e.g., the surprise probe, troop allocation probe, etc.).
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="TrialPartProbe", namespace="IcarusCPD_1")
@XmlSeeAlso({MultiGroupProbe.class, MultiLocationProbe.class, 
	SurpriseReportProbe.class, TroopAllocationProbe_MultiGroup.class})
public abstract class TrialPartProbe {		
}