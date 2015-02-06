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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * A probe to select or see a Red action at a location. In Missions 1-6, the player
 * plays as Blue and the system selects all Red actions. There is only a single Red action
 * because Red only takes an action at at most one location on a trial.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="RedActionSelectionProbe", namespace="IcarusCPD_2")
public class RedActionSelectionProbe extends TrialPartProbe {	
	
	/** The Red action selection (to attack at a location, or to not attack at all) */
	protected RedAction redAction;
	
	/**
	 * Construct an empty RedActionSelectionProbe. The dataProvidedToParticipant flag is set to true.
	 */
	public RedActionSelectionProbe() {
		type = TrialPartProbeType.RedActionSelection;
		dataProvidedToParticipant = true;
	}
	
	/**
	 * Construct a RedActionSelectionProbe with the given Red action selection at a location. 
	 * The dataProvidedToParticipant flag is set to true.
	 * 
	 * @param redAction the Red action selection at a location (attack, not attack)
	 */
	public RedActionSelectionProbe(RedAction redAction) {
		type = TrialPartProbeType.RedActionSelection;
		this.redAction = redAction;
		dataProvidedToParticipant = true;
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbe#getType()
	 */
	@Override
	public TrialPartProbeType getType() {
		return TrialPartProbeType.RedActionSelection;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbe#setType(org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbeType)
	 */
	@Override
	public void setType(TrialPartProbeType type) {
		if(type != TrialPartProbeType.RedActionSelection) {
			throw new IllegalArgumentException("RedActionSelectionProbe type must be RedActionSelection.");
		}
		this.type = type;
	}

	/**
	 * Get the Red action selection at a location (attack, not attack).
	 * 
	 * @return the Red action selection at a location (attack, not attack)
	 */
	@XmlElement(name="RedAction")
	public RedAction getRedAction() {
		return redAction;
	}

	/**
	 * Set the Red action selection at a location (attack, not attack).
	 * 
	 * @param redAction the Red action selection at a location (attack, not attack)
	 */
	public void setRedAction(RedAction redAction) {
		this.redAction = redAction;
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbe#isResponsePresent()
	 */
	@Override
	public boolean isResponsePresent() {
		return (dataProvidedToParticipant == null || !dataProvidedToParticipant)
				&& redAction != null && redAction.getAction() != null;
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbe#copyAdditionalResponseData(org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbe)
	 */
	@Override
	protected void copyAdditionalResponseData(TrialPartProbe trialPart) {
		if(trialPart != null && trialPart instanceof RedActionSelectionProbe) {
			redAction = ((RedActionSelectionProbe)trialPart).redAction;
		}
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbe#clearAdditionalResponseData()
	 */
	@Override
	public void clearAdditionalResponseData() {
		//Does nothing, no response data
	}
}