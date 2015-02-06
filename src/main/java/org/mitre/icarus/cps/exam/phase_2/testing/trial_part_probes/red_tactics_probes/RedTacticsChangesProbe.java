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

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.phase_2.testing.bluebook.RedTacticParameters;
import org.mitre.icarus.cps.exam.phase_2.testing.bluebook.RedTacticParameters.RedTacticConsiderationData;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbeType;

/**
 * A probe where the participant assesses when (on which trials) the Red tactics changed, and provides
 * the parameters of the tactic at each change. The participant also provides the parameters of the
 * tactic Red plays with initially (at least on trial 1). Red may consider only "P", only "U", 
 * or "P" and "U".  
 * 
 * NOTE: This probe is used in Mission 6 and is notional at this point.
 * Additional information will be provided at a later date.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="RedTacticsChangesProbe", namespace="IcarusCPD_2",
	propOrder = {"dataConsidered", "initialRedTactics", "minNumRedTacticsChanges", 
		"maxNumRedTacticsChanges", "redTacticsChanges"})
public class RedTacticsChangesProbe extends AbstractRedTacticsProbe {
	
	/** The types of data Red considers (P_Only, U_Only, or P_And_U) */
	protected RedTacticConsiderationData dataConsidered;
	
	/** The Red tactics that Red initially plays with (starting on the first trial). 
	 * Provided by the participant. */
	protected RedTacticParameters initialRedTactics;
	
	/** The minimum number of times the Red tactics may change */
	protected Integer minNumRedTacticsChanges;
	
	/** The maximum number of times the Red tactics may change */
	protected Integer maxNumRedTacticsChanges;
	
	/** The Red tactics for each trial on which the Red tactics changed.
	 * Provided by the participant. */
	protected List<RedTacticsChange> redTacticsChanges;
	
	/**
	 * Construct an empty RedTacticsChangesProbe.
	 */
	public RedTacticsChangesProbe() {
		type = TrialPartProbeType.RedTacticsChangesReport;
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbe#getType()
	 */
	@Override
	public TrialPartProbeType getType() {
		return TrialPartProbeType.RedTacticsChangesReport;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbe#setType(org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbeType)
	 */
	@Override
	public void setType(TrialPartProbeType type) {
		if(type != TrialPartProbeType.RedTacticsChangesReport) {
			throw new IllegalArgumentException("RedTacticsChangesProbe type must be RedTacticsChangesReport.");
		}
		this.type = type;
	}
	
	/**
	 * Get the types of data Red considers (P_Only, U_Only, or P_And_U).
	 * 
	 * @return the types of data Red considers (P_Only, U_Only, or P_And_U)
	 */
	@XmlAttribute(name="dataConsidered")
	public RedTacticConsiderationData getDataConsidered() {
		return dataConsidered;
	}

	/**
	 * Set the types of data Red considers (P_Only, U_Only, or P_And_U).
	 * 
	 * @param dataConsidered the types of data Red considers (P_Only, U_Only, or P_And_U)
	 */
	public void setDataConsidered(RedTacticConsiderationData dataConsidered) {
		this.dataConsidered = dataConsidered;
	}

	/**
	 * Get the Red tactics that Red initially plays with (starting on the first trial). 
	 * Provided by the participant.
	 * 
	 * @return the Red tactics that Red initially plays with (starting on the first trial)
	 */
	@XmlElement(name="InitialRedTactics")
	public RedTacticParameters getInitialRedTactics() {
		return initialRedTactics;
	}

	/**
	 * Set the Red tactics that Red initially plays with (starting on the first trial). 
	 * Provided by the participant.
	 * 
	 * @param initialRedTactics the Red tactics that Red initially plays with (starting on the first trial)
	 */
	public void setInitialRedTactics(RedTacticParameters initialRedTactics) {
		this.initialRedTactics = initialRedTactics;
	}

	/**
	 * Get the minimum number of times the Red tactics may change.
	 * 
	 * @return the minimum number of times the Red tactics may change
	 */
	@XmlAttribute(name="minNumRedTacticsChanges")
	public Integer getMinNumRedTacticsChanges() {
		return minNumRedTacticsChanges;
	}

	/**
	 * Set the minimum number of times the Red tactics may change.
	 * 
	 * @param minNumRedTacticsChanges the minimum number of times the Red tactics may change
	 */
	public void setMinNumRedTacticsChanges(Integer minNumRedTacticsChanges) {
		this.minNumRedTacticsChanges = minNumRedTacticsChanges;
	}

	/**
	 * Get the maximum number of times the Red tactics may change.
	 * 
	 * @return the maximum number of times the Red tactics may change
	 */
	@XmlAttribute(name="maxNumRedTacticsChanges")
	public Integer getMaxNumRedTacticsChanges() {
		return maxNumRedTacticsChanges;
	}

	/**
	 * Set the maximum number of times the Red tactics may change.
	 * 
	 * @param maxNumRedTacticsChanges the maximum number of times the Red tactics may change
	 */
	public void setMaxNumRedTacticsChanges(Integer maxNumRedTacticsChanges) {
		this.maxNumRedTacticsChanges = maxNumRedTacticsChanges;
	}	

	/**
	 * Get the Red tactics for each trial on which the Red tactics changed.
	 * Provided by the participant.
	 * 
	 * @return the Red tactics for each trial on which the Red tactics changed
	 */
	@XmlElement(name="RedTacticsChange")
	public List<RedTacticsChange> getRedTacticsChanges() {
		return redTacticsChanges;
	}

	/**
	 * Set the Red tactics for each trial on which the Red tactics changed.
	 * Provided by the participant.
	 * 
	 * @param redTacticsChanges the Red tactics for each trial on which the Red tactics changed
	 */
	public void setRedTacticsChanges(List<RedTacticsChange> redTacticsChanges) {
		this.redTacticsChanges = redTacticsChanges;
	}	

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbe#isResponsePresent()
	 */
	@Override
	public boolean isResponsePresent() {
		boolean initialRedTacticsResponsePresent = initialRedTactics != null && 
				initialRedTactics.getAttackProbabilities() != null
				&& initialRedTactics.getAttackProbabilities().size() == 4;
		if(minNumRedTacticsChanges != null && minNumRedTacticsChanges > 0) {
			return initialRedTacticsResponsePresent && 
					//redTacticsChangeTrials != null && 
					//redTacticsChangeTrials.size() >= minNumRedTacticsChanges &&
					redTacticsChanges != null &&
					redTacticsChanges.size() >= minNumRedTacticsChanges;
		}
		return initialRedTacticsResponsePresent;
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.red_tactics_probes.AbstractRedTacticsProbe#copyAdditionalResponseData(org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbe)
	 */
	@Override
	protected void copyAdditionalResponseData(TrialPartProbe trialPart) {
		super.copyAdditionalResponseData(trialPart);
		if(trialPart != null && trialPart instanceof RedTacticsChangesProbe) {
			RedTacticsChangesProbe probe = (RedTacticsChangesProbe)trialPart;
			initialRedTactics = probe.initialRedTactics;
			//redTacticsChangeTrials = probe.redTacticsChangeTrials;
			redTacticsChanges = probe.redTacticsChanges;
		}
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.red_tactics_probes.AbstractRedTacticsProbe#clearAdditionalResponseData()
	 */
	@Override
	public void clearAdditionalResponseData() {
		super.clearAdditionalResponseData();
		if(dataProvidedToParticipant == null || !dataProvidedToParticipant) { 
			if(initialRedTactics != null) {
				initialRedTactics.setAttackProbabilities(null);
			}
			//if(redTacticsChangeTrials != null) {
			//	redTacticsChangeTrials.clear();
			//}
			if(redTacticsChanges != null) {
				redTacticsChanges.clear();
			}
		}
	}
}