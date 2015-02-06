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
package org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.int_presentation;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.phase_2.testing.DatumType;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbe;

/**
 * Base class for probes that present INT datum (OSINT, IMINT, HUMINT, or SIGINT) at one or more locations.
 * 
 * @author cbonaceto
 *
 */
@XmlType(name="IntPresentationProbe", namespace="IcarusCPD_2")
@XmlSeeAlso({HumintPresentationProbe.class, ImintPresentationProbe.class, 
	OsintPresentationProbe.class,SigintPresentationProbe.class})
public abstract class IntPresentationProbe extends TrialPartProbe {	
	
	/** The ID of each locations to present the INT datum at */
	protected List<String> locationIds;
	
	/** The index of each location to present INT datum at */
	protected List<Integer> locationIndexes;		
	
	/**
	 * Construct an empty INT presentation probe.
	 */
	public IntPresentationProbe() {}
	
	/**
	 * Construct an INT presentation probe with the given location IDs and indexes.
	 * 
	 * @param locationIds the IDs of each location to present INT datum at
	 * @param locationIndexes the indexes of each location to present INT datum at
	 */
	public IntPresentationProbe(List<String> locationIds, List<Integer> locationIndexes) {
		this.locationIds = locationIds;
		this.locationIndexes = locationIndexes;
	}

	/**
	 * Get the IDs of each location to present INT datum at.
	 * 
	 * @return the IDs of each location to present INT datum at.
	 */
	@XmlElement(name="LocationIds")
	@XmlList
	public List<String> getLocationIds() {
		return locationIds;
	}

	/**
	 * Set the IDs of each location to present INT datum at.
	 * 
	 * @param locationIds the IDs of each location to present INT datum at.
	 */
	public void setLocationIds(List<String> locationIds) {
		this.locationIds = locationIds;
	}
	 
	/**
	 * Get the indexes of each location to present INT datum at.
	 * 
	 * @return the indexes of each location to present INT datum at
	 */
	@XmlElement(name="LocationIndexes")
	@XmlList
	public List<Integer> getLocationIndexes() {
		return locationIndexes;
	}

	/**
	 * Set the indexes of each location to present INT datum at.
	 * 
	 * @param locationIndexes the indexes of each location to present INT datum at
	 */
	public void setLocationIndexes(List<Integer> locationIndexes) {
		this.locationIndexes = locationIndexes;
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbe#isResponsePresent()
	 */
	@Override
	public boolean isResponsePresent() {
		//Always returns false, no response data
		return false;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbe#copyAdditionalResponseData(org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbe)
	 */
	@Override
	protected void copyAdditionalResponseData(TrialPartProbe trialPart) {
		//Does nothing
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbe#clearAdditionalResponseData()
	 */
	@Override
	protected void clearAdditionalResponseData() {
		//Does nothing
	}	
	
	/**
	 * Get the INT datum type.
	 * 
	 * @return the INT datum type
	 */
	public abstract DatumType getIntDatumType();
}