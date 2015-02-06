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

import java.util.SortedSet;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.phase_2.testing.bluebook.RedTacticType;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbeType;

/**
 * A probe where the participant selects the most likely tactic (style) Red is playing with.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="MostLikelyRedTacticProbe", namespace="IcarusCPD_2",
		propOrder={"redTactics", "mostLikelyRedTactic"})
public class MostLikelyRedTacticProbe extends AbstractRedTacticsProbe {
	
	/** The Red tactics to choose from */
	protected SortedSet<RedTacticType> redTactics;
	
	/** The Red tactic chosen as most likely by the participant */
	protected RedTacticType mostLikelyRedTactic;
	
	/**
	 * Construct an empty MostLikelyRedTacticProbe.
	 */
	public MostLikelyRedTacticProbe() {
		type = TrialPartProbeType.MostLikelyRedTacticSelection;
	}
	
	/**
	 * Construct a MostLikelyRedTacticProbe with the given Red tactics to choose from.
	 * 
	 * @param redTactics the Red tactics to choose from
	 */
	public MostLikelyRedTacticProbe(SortedSet<RedTacticType> redTactics) {
		type = TrialPartProbeType.MostLikelyRedTacticSelection;
		this.redTactics = redTactics;
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbe#getType()
	 */
	@Override
	public TrialPartProbeType getType() {
		return TrialPartProbeType.MostLikelyRedTacticSelection;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbe#setType(org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbeType)
	 */
	@Override
	public void setType(TrialPartProbeType type) {
		if(type != TrialPartProbeType.MostLikelyRedTacticSelection) {
			throw new IllegalArgumentException("MostLikelyRedTacticProbe type must be MostLikelyRedTacticSelection.");
		}
		this.type = type;
	}

	/**
	 * Get the Red tactics to choose from.
	 * 
	 * @return the Red tactics to choose from.
	 */
	@XmlElement(name="RedTactics")
	@XmlList
	public SortedSet<RedTacticType> getRedTactics() {
		return redTactics;
	}

	/**
	 * Set the Red tactics to choose from.
	 * 
	 * @param redTactics the Red tactics to choose from.
	 */
	public void setRedTactics(SortedSet<RedTacticType> redTactics) {
		this.redTactics = redTactics;
	}

	/**
	 * Get the Red tactic chosen as most likely by the participant.
	 * Response data provided by the participant.
	 * 
	 * @return the most likely Red tactic selected
	 */
	@XmlElement(name="MostLikelyRedTactic")	
	public RedTacticType getMostLikelyRedTactic() {
		return mostLikelyRedTactic;
	}

	/**
	 * Set the Red tactic chosen as most likely by the participant.
	 * Response data provided by the participant.
	 * 
	 * @param mostLikelyRedTactic the most likely Red tactic selected
	 */
	public void setMostLikelyRedTactic(RedTacticType mostLikelyRedTactic) {
		this.mostLikelyRedTactic = mostLikelyRedTactic;
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbe#isResponsePresent()
	 */
	@Override
	public boolean isResponsePresent() {
		return (dataProvidedToParticipant == null || !dataProvidedToParticipant) 
				&& mostLikelyRedTactic != null;
	}
	
	@Override
	protected void copyAdditionalResponseData(TrialPartProbe trialPart) {
		super.copyAdditionalResponseData(trialPart);
		if(trialPart != null && trialPart instanceof MostLikelyRedTacticProbe) {
			mostLikelyRedTactic = ((MostLikelyRedTacticProbe)trialPart).mostLikelyRedTactic;
		}
	}

	@Override
	public void clearAdditionalResponseData() {
		super.clearAdditionalResponseData();
		if(dataProvidedToParticipant == null || !dataProvidedToParticipant) {
			mostLikelyRedTactic = null;
		}
	}	
}