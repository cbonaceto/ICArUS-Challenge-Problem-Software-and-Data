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
package org.mitre.icarus.cps.exam.phase_1.testing;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.phase_1.response.Task_1_2_3_ProbeTrialResponseBase;
import org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.AttackLocationProbe_MultiGroup;
import org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.SurpriseReportProbe;
import org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.TroopSelectionProbe_MultiGroup;

/**
 * Base class for probe trials after a block of group attack presentations
 * in Tasks 1, 2, and 3.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="Task_1_2_3_ProbeTrialBase", namespace="IcarusCPD_1")
@XmlSeeAlso({Task_1_ProbeTrial.class, Task_2_ProbeTrial.class, Task_3_ProbeTrial.class})
public abstract class Task_1_2_3_ProbeTrialBase extends IcarusTestTrial_Phase1 {
	
	/** The attack location probe (after seeing all group attacks )*/
	protected AttackLocationProbe_MultiGroup attackLocationProbe;
	
	/** The troop selection probe */
	protected TroopSelectionProbe_MultiGroup troopSelectionProbe;
	
	/** Ground truth information contains the group actually responsible for the attack */
	protected GroundTruth groundTruth;	
	
	/** The surprise probe (after seeing ground truth) */
	protected SurpriseReportProbe groundTruthSurpriseProbe;
	
	/** The dispersion parameters for each group for the set of attacks preceding this probe trial */
	protected ArrayList<Task_1_2_3_AttackDispersionParameters> attackDispersionParameters;

	/**
	 * Get the attack location probe (after seeing all group attack presentation trials).
	 * 
	 * @return the location probe
	 */
	@XmlTransient	
	public AttackLocationProbe_MultiGroup getAttackLocationProbe() {
		return attackLocationProbe;
	}

	/**
	 * Set the attack location probe (after seeing all group attack presentation trials).
	 * 
	 * @param attackLocationProbe the location probe
	 */
	public void setAttackLocationProbe(AttackLocationProbe_MultiGroup attackLocationProbe) {
		this.attackLocationProbe = attackLocationProbe;
	}	
	
	/**
	 * Get the troop selection probe.
	 * 
	 * @return the troop selection probe
	 */
	@XmlTransient
	public TroopSelectionProbe_MultiGroup getTroopSelectionProbe() {
		return troopSelectionProbe;
	}

	/**
	 * Set the troop selection probe.
	 * 
	 * @param troopSelectionProbe the troop selection probe
	 */
	public void setTroopSelectionProbe(TroopSelectionProbe_MultiGroup troopSelectionProbe) {
		this.troopSelectionProbe = troopSelectionProbe;
	}

	/**
	 * Get the ground truth information about the group actually responsible for the attack.
	 * 
	 * @return ground truth
	 */
	@XmlTransient	
	public GroundTruth getGroundTruth() {
		return groundTruth;
	}

	/**
	 * Set the ground truth information about the group actually responsible for the attack.
	 * 
	 * @param groundTruth ground truth
	 */
	public void setGroundTruth(GroundTruth groundTruth) {
		this.groundTruth = groundTruth;
	}
	
	/**
	 * Get the surprise probe after ground truth is revealed.
	 * 
	 * @return the surprise probe
	 */
	@XmlTransient	
	public SurpriseReportProbe getGroundTruthSurpriseProbe() {
		return groundTruthSurpriseProbe;
	}

	/**
	 * Set the surprise probe after ground truth is revealed.
	 * 
	 * @param groundTruthSurpriseProbe the surprise probe
	 */
	public void setGroundTruthSurpriseProbe(
			SurpriseReportProbe groundTruthSurpriseProbe) {
		this.groundTruthSurpriseProbe = groundTruthSurpriseProbe;
	}	

	/**
	 * Get the dispersion parameters for the set of attacks preceding this probe trial.
	 * 
	 * @return the dispersion parameters
	 */	
	@XmlTransient
	public ArrayList<Task_1_2_3_AttackDispersionParameters> getAttackDispersionParameters() {
		return attackDispersionParameters;
	}

	/**
	 * Set the dispersion parameters for the set of attacks preceding this probe trial.
	 * 
	 * @param attackDispersionParameters the dispersion parameters
	 */
	public void setAttackDispersionParameters(ArrayList<Task_1_2_3_AttackDispersionParameters> attackDispersionParameters) {
		this.attackDispersionParameters = attackDispersionParameters;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_1.testing.IcarusTestTrial_Phase1#getTrialResponse()
	 */
	@Override
	public abstract Task_1_2_3_ProbeTrialResponseBase getTrialResponse();
}