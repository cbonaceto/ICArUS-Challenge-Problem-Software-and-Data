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

import org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.AttackLocationProbe_MultiGroup;
//import org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.SurpriseReportProbe;

/**
 * Contains the group probe after an INT layer is shown or selected in Task 5 or 6. 
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="Task_5_6_IntLayerPresentationProbe", namespace="IcarusCPD_1")
public class Task_5_6_INTLayerPresentationProbe extends INTLayerPresentationProbeBase {
	
	/** The attack location probe after the INT layer is presented */
	protected AttackLocationProbe_MultiGroup attackLocationProbe;
	
	/** The surprise report probe after the layer is presented */
	//protected SurpriseReportProbe surpriseProbe;	
	
	/**
	 * No arg constructor.
	 */
	public Task_5_6_INTLayerPresentationProbe() {}
	
	/**
	 * Constructor that takes the INT layer type, attackLocationProbe, and surpriseProbe.
	 * 
	 * @param layerType the INT layer type
	 * @param attackLocationProbe the group probe
	 * @param surpriseProbe the surprise probe
	 */
	public Task_5_6_INTLayerPresentationProbe(IntLayer layerType,
			AttackLocationProbe_MultiGroup attackLocationProbe) {
			//SurpriseReportProbe surpriseProbe) {		
		super(layerType);
		this.attackLocationProbe = attackLocationProbe;
		//this.surpriseProbe = surpriseProbe;
	}
	
	/**
	 * Get the group probe after the INT layer is presented.
	 * 
	 * @return the group probe
	 */
	@XmlElement(name="GroupProbe")
	public AttackLocationProbe_MultiGroup getAttackLocationProbe() {
		return attackLocationProbe;
	}
	
	/**
	 * Set the group probe after the INT layer is presented.
	 * 
	 * @param attackLocationProbe the group probe
	 */
	public void setAttackLocationProbe(AttackLocationProbe_MultiGroup attackLocationProbe) {
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