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
package org.mitre.icarus.cps.feature_vector.phase_2.int_datum;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.phase_2.testing.AbstractDatum;
import org.mitre.icarus.cps.exam.phase_2.testing.DatumType;
import org.mitre.icarus.cps.experiment_core.condition.InstructionsPage;

/**
 * Contains the parameters that define the reliability of SIGINT detections for a Phase 2 exam. Likelihood
 * values are specified in decimal and not percent format.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="SigintReliability", namespace="IcarusCPD_2")
public class SigintReliability extends AbstractDatum {
	
	/** The SIGINT reliability instructions page */
	protected InstructionsPage sigintReliabilityInstructions;	
	
	/** The likelihood that SIGINT reports chatter if Red has chosen to attack */
	protected Double chatterLikelihood_attack;
	
	/** The likelihood that SIGINT reports chatter if Red has not chosen to attack */
	protected Double chatterLikelihood_noAttack;
	
	/** The likelihood that SIGINT reports silence if Red has chosen to attack */
	protected Double silenceLikelihood_attack;
	
	/** The likelihood that SIGINT reports silence if Red has not chosen to attack */
	protected Double silenceLikelihood_noAttack;	
	
	/**
	 * Construct an empty SigintReliability.
	 */
	public SigintReliability() {}
	
	/**
	 * Construct a SigintReliability with the given likelihoods that SIGINT reports chatter given attack/not attack
	 * and when SIGINT reports silence given attack/not attack. Likelihoods should be in decimal format.
	 * 
	 * @param chatterLikelihood_attack the likelihood that SIGINT reports chatter if Red has chosen to attack
	 * @param chatterLikelihood_noAttack the likelihood that SIGINT reports chatter if Red has not chosen to attack
	 * @param silenceLikelihood_attack the likelihood that SIGINT reports silence if Red has chosen to attack
	 * @param silenceLikelihood_noAttack the likelihood that SIGINT reports silence if Red has not chosen to attack
	 */
	public SigintReliability(Double chatterLikelihood_attack, Double chatterLikelihood_noAttack,
			Double silenceLikelihood_attack, Double silenceLikelihood_noAttack) {
		this.chatterLikelihood_attack = chatterLikelihood_attack;
		this.chatterLikelihood_noAttack = chatterLikelihood_noAttack;
		this.silenceLikelihood_attack = silenceLikelihood_attack;
		this.silenceLikelihood_noAttack = silenceLikelihood_noAttack;
	}
	
	/**
	 * Construct a default SigintReliability with default likelihoods that SIGINT reports chatter given attack/not attack
	 * and when SIGINT reports silence given attack/not attack. Likelihoods are in decimal format.  Default values are:
	 * chatterLikelihood_attack = 0.6
	 * chatterLikelihood_noAttack = 0.2
	 * silenceLikelihood_attack = 0.4
	 * silenceLikelihood_noAttack = 0.8
	 * 
	 * @return the default SigintReliability
	 */
	public static SigintReliability createDefaultSigintReliability() {
		return new SigintReliability(0.6D, 0.2D, 0.4D, 0.8D);
	}	

	/**
	 * Get the SIGINT reliability instructions page shown to participants when reviewing the SIGINT reliability.
	 * FOR HUMAN SUBJECT USE ONLY.
	 * 
	 * @return the SIGINT reliability instructions page
	 */
	@XmlElement(name="SigintReliabilityInstructions")
	public InstructionsPage getSigintReliabilityInstructions() {
		return sigintReliabilityInstructions;
	}

	/**
	 * Set the SIGINT reliability instructions page shown to participants when reviewing the SIGINT reliability.
	 * 
	 * @param sigintReliabilityInstructions the SIGINT reliability instructions page
	 */
	public void setSigintReliabilityInstructions(InstructionsPage sigintReliabilityInstructions) {
		this.sigintReliabilityInstructions = sigintReliabilityInstructions;
	}

	/**
	 * Get the likelihood that SIGINT reports chatter if Red has chosen to attack. In decimal format.
	 * 
	 * @return the likelihood that SIGINT reports chatter if Red has chosen to attack
	 */
	@XmlAttribute(name="chatterLikelihood_attack")
	public Double getChatterLikelihood_attack() {
		return chatterLikelihood_attack;
	}

	/**
	 * Set the likelihood that SIGINT reports chatter if Red has chosen to attack. In decimal format.
	 * 
	 * @param chatterLikelihood_attack the likelihood that SIGINT reports chatter if Red has chosen to attack
	 */
	public void setChatterLikelihood_attack(Double chatterLikelihood_attack) {
		this.chatterLikelihood_attack = chatterLikelihood_attack;
	}

	/**
	 * Get the likelihood that SIGINT reports chatter if Red has not chosen to attack. In decimal format.
	 * 
	 * @return the likelihood that SIGINT reports chatter if Red has not chosen to attack
	 */
	@XmlAttribute(name="chatterLikelihood_noAttack")
	public Double getChatterLikelihood_noAttack() {
		return chatterLikelihood_noAttack;
	}
	
	/**
	 * Set the likelihood that SIGINT reports chatter if Red has not chosen to attack. In decimal format.
	 * 
	 * @param chatterLikelihood_noAttack the likelihood that SIGINT reports chatter if Red has not chosen to attack
	 */
	public void setChatterLikelihood_noAttack(Double chatterLikelihood_noAttack) {
		this.chatterLikelihood_noAttack = chatterLikelihood_noAttack;
	}

	/**
	 * Get the likelihood that SIGINT reports silence if Red has chosen to attack. In decimal format.
	 * 
	 * @return the likelihood that SIGINT reports silence if Red has chosen to attack
	 */
	@XmlAttribute(name="silenceLikelihood_attack")
	public Double getSilenceLikelihood_attack() {
		return silenceLikelihood_attack;
	}

	/**
	 * Set the likelihood that SIGINT reports silence if Red has chosen to attack. In decimal format.
	 * 
	 * @param silenceLikelihood_attack the likelihood that SIGINT reports silence if Red has chosen to attack
	 */
	public void setSilenceLikelihood_attack(Double silenceLikelihood_attack) {
		this.silenceLikelihood_attack = silenceLikelihood_attack;
	}

	/**
	 * Get the likelihood that SIGINT reports silence if Red has not chosen to attack. In decimal format.
	 * 
	 * @return the likelihood that SIGINT reports silence if Red has not chosen to attack
	 */
	@XmlAttribute(name="silenceLikelihood_noAttack")
	public Double getSilenceLikelihood_noAttack() {
		return silenceLikelihood_noAttack;
	}

	/**
	 * Set the likelihood that SIGINT reports silence if Red has not chosen to attack. In decimal format.
	 * 
	 * @param silenceLikelihood_noAttack the likelihood that SIGINT reports silence if Red has not chosen to attack
	 */
	public void setSilenceLikelihood_noAttack(Double silenceLikelihood_noAttack) {
		this.silenceLikelihood_noAttack = silenceLikelihood_noAttack;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.IDatum#getDatumType()
	 */
	@Override
	public DatumType getDatumType() {
		return DatumType.SIGINTReliability;
	}
}