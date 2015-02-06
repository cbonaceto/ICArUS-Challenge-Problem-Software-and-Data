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
package org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * Contains the index number and time stamp to indicate when an item (e.g., a probability entry) was
 * adjusted by the subject.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="ItemAdjustment", namespace="IcarusCPD_2")
public class ItemAdjustment {
	
	/** The 0-based index of the item that was adjusted in the list of items */
	protected Integer index;
	
	/** The time the items was adjusted (in milliseconds since 1970 format) */
	protected Long timeStamp;
	
	/**
	 * Empty constructor. 
	 */
	public ItemAdjustment() {}
	
	
	/**
	 * Constructor that takes an index and time stamp.
	 * 
	 * @param index the 0-based index of the item that was adjusted in the list of items
	 * @param timeStamp the time the item was adjusted (in milliseconds since 1970 format)
	 */
	public ItemAdjustment(Integer index, Long timeStamp) {
		this.index = index;
		this.timeStamp = timeStamp;
	}

	/**
	 * Get the 0-based index of the item that was adjusted in the list of items.
	 * 
	 * @return the 0-based index of the item that was adjusted in the list of items
	 */
	@XmlAttribute(name="index")
	public Integer getIndex() {
		return index;
	}

	/**
	 * Set the 0-based index of the item that was adjusted in the list of items.
	 * 
	 * @param index the 0-based index of the item that was adjusted in the list of items
	 */
	public void setIndex(Integer index) {
		this.index = index;
	}

	/**
	 * Get the time the item was adjusted (in milliseconds since 1970 format).
	 * 
	 * @return the time the item was adjusted (in milliseconds since 1970 format)
	 */
	@XmlAttribute(name="timeStamp")
	public Long getTimeStamp() {
		return timeStamp;
	}

	/**
	 * Set the time the item was adjusted (in milliseconds since 1970 format).
	 *  
	 * @param timeStamp the time the item was adjusted (in milliseconds since 1970 format)
	 */
	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}
}