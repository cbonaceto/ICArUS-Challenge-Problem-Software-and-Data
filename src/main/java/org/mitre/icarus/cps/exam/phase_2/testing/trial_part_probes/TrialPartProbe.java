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
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.TrialPartResponse;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.red_tactics_probes.AbstractRedTacticsProbe;

/**
 * Abstract Base class for probes in a trial (e.g., the surprise probe, troop allocation probe, etc.). In Phase 2,
 * the probes also contain the human/model response to the probe.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="TrialPartProbe", namespace="IcarusCPD_2")
@XmlSeeAlso({BlueActionSelectionProbe.class, AbstractRedTacticsProbe.class,
	ProbabilityReportProbe.class, RedActionSelectionProbe.class})
public abstract class TrialPartProbe extends TrialPartResponse {
	
	/** The probe title/name */
	protected String name;	
	
	/** The probe type */
	protected TrialPartProbeType type;
	
	/** Whether the probe data is reported by the participant or given (computed by the system or pre-specified) */
	protected Boolean dataProvidedToParticipant;
	
	/** Whether the probe data will be provided in the feedback returned to the participant */
	protected Boolean dataProvidedInFeedback;
	
	/** Whether the probe is optional to the participant (that is, it may be skipped or left at the current values) */
	protected Boolean optional;

	/**
	 * Get the probe title/name.
	 * 
	 * @return the probe title/name
	 */
	@XmlAttribute(name="name")
	public String getName() {
		return name;
	}

	/**
	 * Set the probe title/name.
	 * 
	 * @param name the probe title/name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Get the probe ID.
	 * 
	 * @return the probe ID
	 */	
	/*public String getId() {
		return type != null ? type.getId() : null;
	}*/

	/**
	 * Set the probe ID.
	 * 
	 * @param id the probe ID
	 */
	/*public void setId(String id) {
		this.id = id;
	}*/
	
	/**
	 * Get the probe type.  
	 * 
	 * @return the probe type
	 */
	@XmlAttribute(name="type")
	public TrialPartProbeType getType() {
		return type;
	}

	/**
	 * Set the probe type.
	 * 
         * @param type	 
	 */
	public void setType(TrialPartProbeType type) {
		this.type = type;
	}	

	/**
	 * Get whether the probe data is reported by the participant or given (computed by the system or pre-specified).
	 * 
	 * @return whether the probe data is reported by the participant or given
	 */
	@XmlAttribute(name="dataProvidedToParticipant")
	public Boolean isDataProvidedToParticipant() {
		return dataProvidedToParticipant;
	}	

	/**
	 * Set whether the probe data is reported by the participant or given (computed by the system or pre-specified).
	 * 
	 * @param dataProvidedToParticipant whether the probe data is reported by the participant or given
	 */
	public void setDataProvidedToParticipant(Boolean dataProvidedToParticipant) {
		this.dataProvidedToParticipant = dataProvidedToParticipant;
	}

	/**
	 * Get whether the probe data will be provided in the feedback returned to the participant (e.g., by the Test Harness).
	 * 
	 * @return whether the probe data will be provided in the feedback returned to the participant
	 */
	@XmlAttribute(name="dataProvidedInFeedback")
	public Boolean isDataProvidedInFeedback() {
		return dataProvidedInFeedback;
	}

	/**
	 * Set whether the probe data will be provided in the feedback returned to the participant (e.g., by the Test Harness).
	 * 
	 * @param dataProvidedInFeedback whether the probe data will be provided in the feedback returned to the participant
	 */
	public void setDataProvidedInFeedback(Boolean dataProvidedInFeedback) {
		this.dataProvidedInFeedback = dataProvidedInFeedback;
	}
	
	/**
	 * Get whether the probe is optional to the participant (that is, it may be skipped or left at the current values).
	 * 
	 * @return whether the probe is optional to the participant
	 */
	@XmlAttribute(name="optional")
	public Boolean isOptional() {
		return optional;
	}

	/**
	 * Set whether the probe is optional to the participant (that is, it may be skipped or left at the current values).
	 * 
	 * @param optional whether the probe is optional to the participant
	 */
	public void setOptional(Boolean optional) {
		this.optional = optional;
	}

	/**
	 * Get whether the participant has responded to the probe.
	 * 
	 * @return whether the participant has responded to the probe
	 */
	public abstract boolean isResponsePresent();
	
	/**
	 * Copy the response data in the given trial part probe to this trial part probe.
	 * 
	 * @param trialPart a trial part probe
	 */
	public void copyResponseData(TrialPartProbe trialPart) {
		if(trialPart != null) {
			trialPartTime_ms = trialPart.trialPartTime_ms;
			copyAdditionalResponseData(trialPart);
		}
	}
	
	/**
	 * Subclasses may override this method to copy any additional response data in the given 
	 * trial part probe to this trial part probe.
	 * 
	 * @param trialPart a trial part probe
	 */
	protected abstract void copyAdditionalResponseData(TrialPartProbe trialPart);
	
	/**
	 * Clears any participant response data to the trial part. 
	 */
	public void clearResponseData() {
		trialPartTime_ms = null;
		clearAdditionalResponseData();
	}
	
	/** Subclasses should override this method to clear any additional response data they contain. */
	protected abstract void clearAdditionalResponseData();
}