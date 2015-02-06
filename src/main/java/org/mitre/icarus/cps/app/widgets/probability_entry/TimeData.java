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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * Contains detailed timing data about user interaction times with various probability entry modalities.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="TimeData", namespace="IcarusUIStudy")
public class TimeData {
	
	protected Long dragTime_ms;
	
	protected Long clickTime_ms;
	
	protected Long spinnerTime_ms;
	
	public TimeData() {}
	
	public TimeData(Long spinnerTime_ms) {
		this.spinnerTime_ms = spinnerTime_ms;
	}
	
	public TimeData(Long dragTime_ms, Long clickTime_ms, Long spinnerTime_ms) {
		this.dragTime_ms = dragTime_ms;
		this.clickTime_ms = clickTime_ms;
		this.spinnerTime_ms = spinnerTime_ms;
	}
	
	@XmlAttribute(name="dragTime_ms")
	public Long getDragTime_ms() {
		return dragTime_ms;
	}

	public void setDragTime_ms(Long dragTime_ms) {
		this.dragTime_ms = dragTime_ms;
	}

	@XmlAttribute(name="clickTime_ms")
	public Long getClickTime_ms() {
		return clickTime_ms;
	}

	public void setClickTime_ms(Long clickTime_ms) {
		this.clickTime_ms = clickTime_ms;
	}

	@XmlAttribute(name="spinnerTime_ms")
	public Long getSpinnerTime_ms() {
		return spinnerTime_ms;
	}

	public void setSpinnerTime_ms(Long spinnerTime_ms) {
		this.spinnerTime_ms = spinnerTime_ms;
	}	
}
