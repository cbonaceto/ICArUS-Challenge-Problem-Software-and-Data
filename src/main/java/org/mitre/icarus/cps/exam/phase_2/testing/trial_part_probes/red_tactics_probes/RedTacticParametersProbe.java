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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.phase_2.testing.DatumIdentifier;
import org.mitre.icarus.cps.exam.phase_2.testing.bluebook.RedTacticParameters;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbe;

/**
 * A probe where the participant estimates the parameters of the tactic (style) Red is playing with.
 * The parameters indicate the probability that Red attacks based on values of vulnerability "P" and 
 * utility "U".
 * 
 * THIS PROBE IS DEPRECATED AND MAY BE REMOVED. IT WAS PREVIOUSLY USED IN MISSION 5, BUT MISSION 5 
 * HAS BEEN MODIFIED AND THIS PROBE IS NO LONGER USED.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="RedTacticParametersProbe", namespace="IcarusCPD_2",
	propOrder = {"datumList", "redTacticParameters"})
@Deprecated
public class RedTacticParametersProbe extends AbstractRedTacticsProbe {
	
	/** The datum to consider when reporting the attack probabilities */
	protected List<DatumIdentifier> datumList;
	
	/** The Red tactic parameters. The participant supplies the attack probabilities. */
	protected RedTacticParameters redTacticParameters;
	
	/**
	 * Construct an empty RedTacticParametersProbe.
	 */
	public RedTacticParametersProbe() {}
	
	/**
	 * Construct a RedTacticParametersProbe with the given datum list and Red tactic parameters.
	 * 
	 * @param datumList the datum to consider when reporting the attack probabilities
	 * @param redTacticParameters The Red tactic parameters. The participant supplies the attack probabilities.
	 */
	public RedTacticParametersProbe(List<DatumIdentifier> datumList, 
			RedTacticParameters redTacticParameters) {
		this.datumList = datumList;
		this.redTacticParameters = redTacticParameters;
	}
	
	/**
	 * Get the datum to consider when reporting the attack probabilities.
	 * 
	 * @return the datum to consider when reporting the attack probabilities.
	 */
	@XmlElement(name="Datum")
	public List<DatumIdentifier> getDatumList() {
		return datumList;
	}

	/**
	 * Set the datum to consider when reporting the attack probabilities.
	 * 
	 * @param datumList the datum to consider when reporting the attack probabilities.
	 */
	public void setDatumList(List<DatumIdentifier> datumList) {
		this.datumList = datumList;
	}

	/**
	 * Get the Red tactic parameters. The participant supplies the attack probabilities.
	 * 
	 * @return the Red tactic parameters
	 */
	@XmlElement(name="RedTacticParameters")
	public RedTacticParameters getRedTacticParameters() {
		return redTacticParameters;
	}

	/**
	 * Set the Red tactic parameters. The participant supplies the attack probabilities.
	 * 
	 * @param redTacticParameters the Red tactic parameters
	 */
	public void setRedTacticParameters(RedTacticParameters redTacticParameters) {
		this.redTacticParameters = redTacticParameters;
	}	

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbe#isResponsePresent()
	 */
	@Override
	public boolean isResponsePresent() {
		return (dataProvidedToParticipant == null || !dataProvidedToParticipant) 
				&& redTacticParameters != null && redTacticParameters.getAttackProbabilities() != null
				&& redTacticParameters.getAttackProbabilities().size() == 4;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.red_tactics_probes.AbstractRedTacticsProbe#copyAdditionalResponseData(org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbe)
	 */
	@Override
	protected void copyAdditionalResponseData(TrialPartProbe trialPart) {
		super.copyAdditionalResponseData(trialPart);
		if(trialPart != null && trialPart instanceof RedTacticParametersProbe) {
			RedTacticParametersProbe probe = (RedTacticParametersProbe)trialPart;
			redTacticParameters = probe.redTacticParameters;
		}
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.red_tactics_probes.AbstractRedTacticsProbe#clearAdditionalResponseData()
	 */
	@Override
	public void clearAdditionalResponseData() {
		super.clearAdditionalResponseData();
		if((dataProvidedToParticipant == null || !dataProvidedToParticipant) && 
				redTacticParameters != null) {
			redTacticParameters.setAttackProbabilities(null);
		}
	}
}