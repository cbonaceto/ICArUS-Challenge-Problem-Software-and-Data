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
package org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.AttackLocationProbe_MultiLocation;
//import org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.SurpriseReportProbe;

/**
 * Contains the location probe after an INT layer is shown in Task 4.
 * Note that only SOCINT is currently shown in Task 4, but we support adding additional
 * INTs in the future.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="Task_4_IntLayerPresentationProbe", namespace="IcarusCPD_1")
public class Task_4_INTLayerPresentationProbe extends INTLayerPresentationProbeBase {
	
	/** The attack location probe after the INT layer is presented */
	protected AttackLocationProbe_MultiLocation attackLocationProbe;
	
	/** The surprise report probe after the layer is presented */
	//protected SurpriseReportProbe surpriseProbe;	
	
	/**
	 * No arg constructor.
	 */
	public Task_4_INTLayerPresentationProbe() {}
	
	/**
	 * Constructor that takes the INT layerType and attackLocationProbe.
	 * 
	 * @param layerType the INT layer type
	 * @param attackLocationProbe the location probe
	 */
	public Task_4_INTLayerPresentationProbe(IntLayer layerType,
			AttackLocationProbe_MultiLocation attackLocationProbe) {		
		super(layerType);
		this.attackLocationProbe = attackLocationProbe;
	}

	/**
	 * Get the attack location probe after the INT layer is presented
	 * 
	 * @return the location probe
	 */
	@XmlElement(name="LocationProbe")
	public AttackLocationProbe_MultiLocation getAttackLocationProbe() {
		return attackLocationProbe;
	}

	/**
	 * Set the attack location probe after the INT layer is presented
	 * 
	 * @param attackLocationProbe the location probe
	 */
	public void setAttackLocationProbe(AttackLocationProbe_MultiLocation attackLocationProbe) {
		this.attackLocationProbe = attackLocationProbe;
	}
	
	/**
	 * Get the surprise probe after the INT layer is presented.
	 * 
	 * @return the surprise probe
	 */
	/*@XmlElement(name="SurpriseProbe")
	public SurpriseReportProbe getSurpriseProbe() {
		return surpriseProbe;
	}*/

	/**
	 * Set the surprise probe after the INT layer is presented.
	 * 
	 * @param surpriseProbe the surprise probe
	 */
	/*public void setSurpriseProbe(SurpriseReportProbe surpriseProbe) {
		this.surpriseProbe = surpriseProbe;
	}*/
}