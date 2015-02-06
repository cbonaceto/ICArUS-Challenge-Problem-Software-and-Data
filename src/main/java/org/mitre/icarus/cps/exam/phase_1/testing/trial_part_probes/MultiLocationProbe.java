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
package org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * Abstract base class for probes that elicit a response about one or more locations.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="MultiLocationProbe", namespace="IcarusCPD_1")
@XmlSeeAlso({AttackLocationProbe_MultiLocation.class, TroopAllocationProbe_MultiLocation.class})
public abstract class MultiLocationProbe extends TrialPartProbe {
	
	/** The IDs of the locations to probe */
	protected ArrayList<String> locations;
	
	/**
	 * No arg constructor.
	 */
	protected MultiLocationProbe() {}
	
	/**
	 * Constructor that takes the IDS of each location to probe
	 * 
	 * @param locations the IDs of each location to probe
	 */
	protected MultiLocationProbe(ArrayList<String> locations) {
		this.locations = locations;
	}	
	
	/**
	 * Get the IDs of each location to probe.
	 * 
	 * @return - the IDs of each location to probe.
	 */
	@XmlTransient
	public ArrayList<String> getLocations() {
		return locations;
	}

	/**
	 * Set the IDs of each location to probe.
	 * 
	 * @param locations - the IDs of each location to probe.
	 */
	public void setLocations(ArrayList<String> locations) {
		this.locations = locations;
	}	

	/**
	 * @author CBONACETO
	 *
	 */
	protected static class LocationList {
		/** The ID for this location list */
		protected String listId;
		
		/** The location IDs */
		protected ArrayList<String> locationIds;
		
		public LocationList() {}
		
		public LocationList(String listId) {
			this.listId = listId;
		}
		
		public LocationList(ArrayList<String> locationIds) {
			this.locationIds = locationIds;
		}
		
		public LocationList(String listId, ArrayList<String> locationIds) {
			this.listId = listId;
			this.locationIds = locationIds;
		}

		@XmlAttribute(name="ListId")		
		public String getListId() {
			return listId;
		}

		public void setListId(String listId) {
			this.listId = listId;
		}
		
		@XmlElement(name="LocationId")
		public ArrayList<String> getLocationIds() {
			return locationIds;
		}

		public void setLocationIds(ArrayList<String> locationIds) {
			this.locationIds = locationIds;
		}		
	}
}