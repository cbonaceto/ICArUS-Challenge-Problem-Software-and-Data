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
import org.mitre.icarus.cps.feature_vector.phase_1.SigintType;

/**
 * Defines attack likelihoods for SIGINT.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="SigintRule", namespace="IcarusCPD_1", 
		propOrder={"group", "attackLikelihoodProbed_chatter", "attackLikelihoodProbed_silent",
		"attackLikelihoodNotProbed_chatter", "attackLikelihoodNotProbed_silent"})
public class SigintRule {	
	
	/** The group the rule is for */
	protected GroupType group;
	
	/** When this group is probed for SIGINT, likelihood of attack given chatter */
	protected Double attackLikelihoodProbed_chatter;
	
	/** When this group is probed for SIGINT, likelihood of attack given silence */
	protected Double attackLikelihoodProbed_silent;
	
	/** When this group is not the group probed for SIGINT, likelihood of attack given that 
	 * the probed group reported chatter */
	protected Double attackLikelihoodNotProbed_chatter;
	
	/** When this group is not the group probed for SIGINT, likelihood of attack given that 
	 * the probed group reported silent */
	protected Double attackLikelihoodNotProbed_silent;
	
	public SigintRule() {}
	
	public SigintRule(GroupType group, Double attackLikelihoodProbed_chatter, Double attackLikelihoodProbed_silent,
			Double attackLikelihoodNotProbed_chatter, Double attackLikelihoodNotProbed_silent) {
		this.group = group;
		this.attackLikelihoodProbed_chatter = attackLikelihoodProbed_chatter;
		this.attackLikelihoodProbed_silent = attackLikelihoodProbed_silent;
		this.attackLikelihoodNotProbed_chatter = attackLikelihoodNotProbed_chatter;
		this.attackLikelihoodNotProbed_silent = attackLikelihoodNotProbed_silent;
	}
	
	@XmlAttribute(name="group")
	public GroupType getGroup() {
		return group;
	}

	public void setGroup(GroupType group) {
		this.group = group;
	}
	
	/**
	 * Get the attack likelihood for the given SIGINT type and whether the group 
	 * this rule is for was the group probed for SIGINT. 
	 * 
	 * @param sigintType the SIINT type
	 * @return the attack likelihood
	 */
	public Double getAttackLikelihood(SigintType sigintType, boolean probed) {
		if(probed) {
			if(sigintType == SigintType.Chatter) {
				return attackLikelihoodProbed_chatter;
			}
			else if(sigintType == SigintType.Silent) {
				return attackLikelihoodProbed_silent;
			}
		}
		else {
			if(sigintType == SigintType.Chatter) {
				return attackLikelihoodNotProbed_chatter;
			}
			else if(sigintType == SigintType.Silent) {
				return attackLikelihoodNotProbed_silent;
			}
		}
		return 0.d;
	}	

	@XmlAttribute(name="attackLikelihoodProbed_chatter")
	public Double getAttackLikelihoodProbed_chatter() {
		return attackLikelihoodProbed_chatter;
	}

	public void setAttackLikelihoodProbed_chatter(Double attackLikelihoodProbed_chatter) {
		this.attackLikelihoodProbed_chatter = attackLikelihoodProbed_chatter;
	}

	@XmlAttribute(name="attackLikelihoodProbed_silent")
	public Double getAttackLikelihoodProbed_silent() {
		return attackLikelihoodProbed_silent;
	}

	public void setAttackLikelihoodProbed_silent(Double attackLikelihoodProbed_silent) {
		this.attackLikelihoodProbed_silent = attackLikelihoodProbed_silent;
	}

	@XmlAttribute(name="attackLikelihoodNotProbed_chatter")
	public Double getAttackLikelihoodNotProbed_chatter() {
		return attackLikelihoodNotProbed_chatter;
	}
	
	public void setAttackLikelihoodNotProbed_chatter(Double attackLikelihoodNotProbed_chatter) {
		this.attackLikelihoodNotProbed_chatter = attackLikelihoodNotProbed_chatter;
	}

	@XmlAttribute(name="attackLikelihoodNotProbed_silent")
	public Double getAttackLikelihoodNotProbed_silent() {
		return attackLikelihoodNotProbed_silent;
	}

	public void setAttackLikelihoodNotProbed_silent(Double attackLikelihoodNotProbed_silent) {
		this.attackLikelihoodNotProbed_silent = attackLikelihoodNotProbed_silent;
	}		
}