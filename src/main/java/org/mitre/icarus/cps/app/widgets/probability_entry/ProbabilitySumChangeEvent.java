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
package org.mitre.icarus.cps.app.widgets.probability_entry;

import org.mitre.icarus.cps.app.widgets.events.IcarusGUIEvent;
import org.mitre.icarus.cps.app.widgets.events.InteractionComponentType;

/**
 * An ICArUS GUI event fired when the sum of a probability entry container changes. Also
 * contains the index and ID of the probability control that was adjusted by the user.
 * 
 * @author CBONACETO
 *
 */
public class ProbabilitySumChangeEvent extends IcarusGUIEvent {

	private static final long serialVersionUID = 595507707021283022L;

	/** The index of the probability control that was adjusted */
	public final Integer adjustedControlIndex;
	
	/** The ID of the probability control that was adjusted */
	public final Integer adjustedControlId;
	
	public ProbabilitySumChangeEvent(Object source,
			InteractionComponentType interactionComponent,
			Integer adjustedControlIndex, Integer adjustedControlId) {
		super(source, IcarusGUIEvent.PROBABILITY_SUM_CHANGED, interactionComponent);
		this.adjustedControlIndex = adjustedControlIndex;
		this.adjustedControlId = adjustedControlId;
	}

	public Integer getAdjustedControlIndex() {
		return adjustedControlIndex;
	}

	public Integer getAdjustedControlId() {
		return adjustedControlId;
	}
}