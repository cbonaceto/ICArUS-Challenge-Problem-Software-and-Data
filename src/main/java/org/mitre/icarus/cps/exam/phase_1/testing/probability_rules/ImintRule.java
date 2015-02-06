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
import org.mitre.icarus.cps.feature_vector.phase_1.ImintType;

/**
 * Defines attack likelihoods for a group for IMINT.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="ImintRule", namespace="IcarusCPD_1", 
		propOrder={"group", "attackLikelihood_government", "attackLikelihood_military"})
public class ImintRule {
	
	/** The group the rule is for */
	protected GroupType group;
	
	/** If group attacks, likelihood of attack at a government facility */
	protected Double attackLikelihood_government;
	
	/** If group attacks, likelihood of attack at a military facility */
	protected Double attackLikelihood_military;
	
	public ImintRule() {}
	
	public ImintRule(GroupType group, Double attackLikelihood_government,
			Double attackLikelihood_military) {
		this.group = group;
		this.attackLikelihood_government = attackLikelihood_government;
		this.attackLikelihood_military = attackLikelihood_military;
	}

	/**
	 * @return
	 */
	@XmlAttribute(name="group")
	public GroupType getGroup() {
		return group;
	}

	/**
	 * @param group
	 */
	public void setGroup(GroupType group) {
		this.group = group;
	}	
	
	/**
	 * Get the attack likelihood for the given IMINT type. 
	 * 
	 * @param imintType the IMINT type
	 * @return the attack likelihood
	 */
	public Double getAttackLikelihood(ImintType imintType) {
		if(imintType == ImintType.Government) {
			return attackLikelihood_government;
		}
		else if(imintType == ImintType.Military) {
			return attackLikelihood_military;
		}
		return 0.d;
	}

	@XmlAttribute(name="attackLikelihood_government")
	public Double getAttackLikelihood_government() {
		return attackLikelihood_government;
	}

	public void setAttackLikelihood_government(Double attackLikelihood_government) {
		this.attackLikelihood_government = attackLikelihood_government;
	}

	@XmlAttribute(name="attackLikelihood_military")
	public Double getAttackLikelihood_military() {
		return attackLikelihood_military;
	}

	public void setAttackLikelihood_military(Double attackLikelihood_military) {
		this.attackLikelihood_military = attackLikelihood_military;
	}		
}