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
package org.mitre.icarus.cps.exam.phase_05.testing.assessment;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * A piece of evidence in an assessment trial (e.g., a building type, rooftop hardware, etc.).
 * 
 * FOR HUMAN SUBJECT USE ONLY.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="EvidenceElement", namespace="IcarusCPD_05")
public class EvidenceElement {
	@XmlType(namespace="IcarusCPD_05")
	public static enum ItemType {
		Building,
		Rooftop_Hardware,
		Water,
		SIGINT,
		MASINT_1,
		MASINT_2,
		Other
	}
	
	/** Item ID (if a specific item) */
	private Integer itemId;
	
	/** Item type */
	private ItemType itemType;	
	
	@XmlAttribute(name="ItemId")
	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	@XmlAttribute(name="ItemType")
	public ItemType getItemType() {
		return itemType;
	}

	public void setItemType(ItemType itemType) {
		this.itemType = itemType;
	}	
}
