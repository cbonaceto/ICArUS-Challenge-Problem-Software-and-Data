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
package org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.trial_part_data;

import java.util.List;

import org.mitre.icarus.cps.exam.phase_2.testing.bluebook.RedTacticType;

/**
 * @author CBONACETO
 *
 */
public class BluebookProbabilitiesData {
	
	/** The Red tactic*/
	protected RedTacticType redTactic;
	
	/** The Red attack probabilities at each location given the values of P and U at each location */
	protected List<Double> bluebookProbabilities;
	
	public BluebookProbabilitiesData() {}
	
	public BluebookProbabilitiesData(RedTacticType redTactic) {
		this.redTactic = redTactic;
	}

	public RedTacticType getRedTactic() {
		return redTactic;
	}

	public void setRedTactic(RedTacticType redTactic) {
		this.redTactic = redTactic;
	}

	public List<Double> getBluebookProbabilities() {
		return bluebookProbabilities;
	}

	public void setBluebookProbabilities(List<Double> bluebookProbabilities) {
		this.bluebookProbabilities = bluebookProbabilities;
	}
}