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
package org.mitre.icarus.cps.exam.phase_05.testing.scene_presentation;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * Scene item base class.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="SceneItem", namespace="IcarusCPD_05", propOrder={"itemId", "itemName"})
public abstract class SceneItem {
	
	/** Scene item types */
	@XmlType(namespace="IcarusCPD_05")
	public static enum SceneItemType {Facility, Object, Incident};
	
	/** Scene item ID */
	protected Integer itemId;
	
	/** Scene item name */
	protected String itemName;	
	
	public SceneItem() {}
	
	public SceneItem(String itemName, int itemId) {
		this.itemName = itemName;
		this.itemId = itemId;
	}	

	/**
	 * Get the scene item ID.
	 * 
	 * @return the scene item ID
	 */
	@XmlAttribute(name="ItemId", required=true)
	public Integer getItemId() {
		return itemId;
	}

	/**
	 * Set the scene item ID.
	 * 
	 * @param itemId
	 */
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	/**
	 * Get the scene item name.
	 * 
	 * @return the scene item name
	 */
	@XmlAttribute(name="ItemName", required=true)
	public String getItemName() {
		return itemName;
	}

	/**
	 * Set the scene item name.
	 * 
	 * @param itemName
	 */
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	/**
	 * Get the scene item type (Facility, Object, or Event).
	 * 
	 * @return the scene item type
	 */
	public abstract SceneItemType getItemType();	
}
