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
package org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.red_tactics_probes;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.BatchPlotProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbe;

/**
 * Abstract base class for probes on the tactic(s) Red may be playing with.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="AbstractRedTacticsProbe", namespace="IcarusCPD_2")
@XmlSeeAlso({MostLikelyRedTacticProbe.class, RedTacticsProbabilityReportProbe.class, RedTacticsChangesProbe.class})
public abstract class AbstractRedTacticsProbe extends TrialPartProbe {
	
	/** A probe to create batch plots of attacks. This may of not be an option to the participant 
	 * depending on how many batch plots they've created and the maximum number that they may
	 * create (Missions 4-6 only) */
	protected BatchPlotProbe batchPlotProbe;
	
	/**
	 * Get the probe to create a batch plot. This may of not be an option to the participant 
	 * depending on how many batch plots they've created and the maximum number that they may
	 * create (Missions 4-6 only).
	 * 
	 * @return the batch plot probe
	 */
	@XmlElement(name="BatchPlotProbe")
	public BatchPlotProbe getBatchPlotProbe() {
		return batchPlotProbe;
	}

	/**
	 * Set the probe to create a batch plot. This may of not be an option to the participant 
	 * depending on how many batch plots they've created and the maximum number that they may
	 * create (Missions 4-6 only)
	 * 
	 * @param batchPlotProbe the batch plot probe
	 */
	public void setBatchPlotProbe(BatchPlotProbe batchPlotProbe) {
		this.batchPlotProbe = batchPlotProbe;
	}	
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbe#copyAdditionalResponseData(org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbe)
	 */
	@Override
	protected void copyAdditionalResponseData(TrialPartProbe trialPart) {
		if(batchPlotProbe != null && trialPart != null && trialPart instanceof AbstractRedTacticsProbe) {			
			batchPlotProbe.copyResponseData(((AbstractRedTacticsProbe)trialPart).batchPlotProbe);
		}
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbe#clearAdditionalResponseData()
	 */
	@Override
	protected void clearAdditionalResponseData() {
		if((dataProvidedToParticipant == null || !dataProvidedToParticipant) && 
				batchPlotProbe != null) {
			batchPlotProbe.clearResponseData();
		}
	}	
}