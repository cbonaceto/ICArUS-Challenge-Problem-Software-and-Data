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

import javax.xml.bind.annotation.XmlType;

/**
 * Abstract base class for a Task 4, 5, and 6 phases in an exam.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="Task_4_5_6_PhaseBase", namespace="IcarusCPD_1")
public abstract class Task_4_5_6_PhaseBase<T extends Task_4_5_6_TrialBase> extends TaskTestPhase<T> {
	/** Whether to reverse the orders the layers are presented in */
	protected Boolean reverseLayerOrder;
	
	/**
	 * Get whether to reverse the order the INT layers are displayed in in the GUI.
	 * 
	 * @return whether to reverse the order of the layers in the GUI
	 */
	public Boolean isReverseLayerOrder() {
		return reverseLayerOrder;
	}

	/**
	 * Set whether to reverse the order the INT layers are displayed in in the GUI.
	 * 
	 * @param reverseLayerOrder whether to reverse the order of the layers in the GUI
	 */
	public void setReverseLayerOrder(Boolean reverseLayerOrder) {
		this.reverseLayerOrder = reverseLayerOrder;
	}
}