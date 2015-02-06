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
import org.mitre.icarus.cps.feature_vector.phase_1.MovintType;

/**
 * Defines attack likelihoods for a group for MOVINT.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="MovintRule", namespace="IcarusCPD_1", 
		propOrder={"group", "attackLikelihood_denseTraffic", "attackLikelihood_sparseTraffic"})
public class MovintRule {
	
	/** The group the rule is for */
	protected GroupType group;
	
	/** If group attacks, likelihood of attack in dense traffic */
	protected Double attackLikelihood_denseTraffic;
	
	/** If group attacks, likelihood of attack in sparse traffic */
	protected Double attackLikelihood_sparseTraffic;
	
	public MovintRule() {}
	
	public MovintRule(GroupType group, Double attackLikelihood_denseTraffic,
			Double attackLikelihood_sparseTraffic) {
		this.group = group;
		this.attackLikelihood_denseTraffic = attackLikelihood_denseTraffic;
		this.attackLikelihood_sparseTraffic = attackLikelihood_sparseTraffic;
	}

	@XmlAttribute(name="group")
	public GroupType getGroup() {
		return group;
	}

	public void setGroup(GroupType group) {
		this.group = group;
	}
	
	/**
	 * Get the attack likelihood for the given MOVINT type. 
	 * 
	 * @param movintType the MOVINT type
	 * @return the attack likelihood
	 */
	public Double getAttackLikelihood(MovintType movintType) {
		if(movintType == MovintType.DenseTraffic) {
			return attackLikelihood_denseTraffic;
		}
		else if(movintType == MovintType.SparseTraffic) {
			return attackLikelihood_sparseTraffic;
		}
		return 0.d;
	}

	@XmlAttribute(name="attackLikelihood_denseTraffic")
	public Double getAttackLikelihood_denseTraffic() {
		return attackLikelihood_denseTraffic;
	}

	public void setAttackLikelihood_denseTraffic(
			Double attackLikelihood_denseTraffic) {
		this.attackLikelihood_denseTraffic = attackLikelihood_denseTraffic;
	}

	@XmlAttribute(name="attackLikelihood_sparseTraffic")
	public Double getAttackLikelihood_sparseTraffic() {
		return attackLikelihood_sparseTraffic;
	}

	public void setAttackLikelihood_sparseTraffic(
			Double attackLikelihood_sparseTraffic) {
		this.attackLikelihood_sparseTraffic = attackLikelihood_sparseTraffic;
	}	
}