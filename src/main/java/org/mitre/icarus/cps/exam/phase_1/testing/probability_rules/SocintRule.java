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
 * Defines attack likelihoods for a group for SOCINT.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="SocintRule", namespace="IcarusCPD_1", 
		propOrder={"group", "attackLikelihood_inRegion", "attackLikelihood_outsideRegion"})
public class SocintRule {
	
	/** The group the rule is for */
	protected GroupType group;

	/** If group attacks, likelihood of attack in own region */
	protected Double attackLikelihood_inRegion;
	
	/** If group attacks, likelihood of attack outside its region */
	protected Double attackLikelihood_outsideRegion;
	
	public SocintRule() {}
	
	public SocintRule(GroupType group, Double attackLikelihood_inRegion, 
			Double attackLikelihood_outsideRegion) {
		this.group = group;
		this.attackLikelihood_inRegion = attackLikelihood_inRegion;
		this.attackLikelihood_outsideRegion = attackLikelihood_outsideRegion;
	}
	
	@XmlAttribute(name="group")
	public GroupType getGroup() {
		return group;
	}

	public void setGroup(GroupType group) {
		this.group = group;
	}

	@XmlAttribute(name="attackLikelihood_inRegion")
	public Double getAttackLikelihood_inRegion() {
		return attackLikelihood_inRegion;
	}

	public void setAttackLikelihood_inRegion(Double attackLikelihood_inRegion) {
		this.attackLikelihood_inRegion = attackLikelihood_inRegion;
	}

	@XmlAttribute(name="attackLikelihood_outsideRegion")
	public Double getAttackLikelihood_outsideRegion() {
		return attackLikelihood_outsideRegion;
	}

	public void setAttackLikelihood_outsideRegion(
			Double attackLikelihood_outsideRegion) {
		this.attackLikelihood_outsideRegion = attackLikelihood_outsideRegion;
	}	
}