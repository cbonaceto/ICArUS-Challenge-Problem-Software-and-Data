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
package org.mitre.icarus.cps.exam.phase_1.testing.probability_rules;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.feature_vector.phase_1.GroupType;

/**
 * Defines attack likelihoods for a group for WAVEINT. This is only used for Mission 7.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="WaveintRule", namespace="IcarusCPD_1", 
propOrder={"group", "attackLikelihood_responsibleForLastAttack", 
		"attackLikelihood_notResponsibleForLastAttack"})
public class WaveintRule {
	
	/** The group the rule is for */
	protected GroupType group;
	
	/** If group was responsible for last attack, likelihood group is responsible for current attack  */
	protected Double attackLikelihood_responsibleForLastAttack;
	
	/** If group was not responsible for last attack, likelihood group is responsible for current attack  */
	protected Double attackLikelihood_notResponsibleForLastAttack;
	
	public WaveintRule() {}
	
	public WaveintRule(GroupType group, Double attackLikelihood_responsibleForLastAttack, 
			Double attackLikelihood_notResponsibleForLastAttack) {
		this.group = group;
		this.attackLikelihood_responsibleForLastAttack = attackLikelihood_responsibleForLastAttack;
		this.attackLikelihood_notResponsibleForLastAttack = attackLikelihood_notResponsibleForLastAttack;
	}

	@XmlAttribute(name="group")
	public GroupType getGroup() {
		return group;
	}

	public void setGroup(GroupType group) {
		this.group = group;
	}

	@XmlAttribute(name="attackLikelihood_responsibleForLastAttack")
	public Double getAttackLikelihood_responsibleForLastAttack() {
		return attackLikelihood_responsibleForLastAttack;
	}

	public void setAttackLikelihood_responsibleForLastAttack(
			Double attackLikelihood_responsibleForLastAttack) {
		this.attackLikelihood_responsibleForLastAttack = attackLikelihood_responsibleForLastAttack;
	}

	@XmlAttribute(name="attackLikelihood_notResponsibleForLastAttack")
	public Double getAttackLikelihood_notResponsibleForLastAttack() {
		return attackLikelihood_notResponsibleForLastAttack;
	}

	public void setAttackLikelihood_notResponsibleForLastAttack(
			Double attackLikelihood_notResponsibleForLastAttack) {
		this.attackLikelihood_notResponsibleForLastAttack = attackLikelihood_notResponsibleForLastAttack;
	}
}