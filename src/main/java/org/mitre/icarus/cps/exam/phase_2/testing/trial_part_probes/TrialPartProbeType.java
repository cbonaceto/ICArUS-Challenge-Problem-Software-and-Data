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
package org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes;

import javax.xml.bind.annotation.XmlType;

/**
 * An enumeration of trial part probe types. Each probe type also has a unique ID.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="TrialPartProbeType", namespace="IcarusCPD_2")
public enum TrialPartProbeType {
	MostLikelyRedTacticSelection("red_tactics_most_likely"),
	RedTacticsProbabilityReport("red_tactics_probs"),
	RedTacticsChangesReport("red_tactics_changes"),
	BatchPlotProbe("batch_plot"),
	AttackProbabilityReport_Pp("probs_Pp"),
	AttackProbabilityReport_Ppc("probs_Ppc"),
	AttackProbabilityReport_Pt("probs_Pt"),
	AttackProbabilityReport_Ptpc("probs_Ptpc"),
	SigintSelection("sigint"),
	RedActionSelection("red_action"),
	BlueActionSelection("blue_action");
	
	/** The probe type ID */
	private final String id;	
	
	/**
	 * Constructor that takes the probe type ID.
	 * 
	 * @param id the probe type ID
	 */
	private TrialPartProbeType(String id) {
		this.id = id;
	}

	/**
	 * Get the probe type ID.
	 * 
	 * @return the probe type ID
	 */
	public String getId() {
		return id;
	}
}