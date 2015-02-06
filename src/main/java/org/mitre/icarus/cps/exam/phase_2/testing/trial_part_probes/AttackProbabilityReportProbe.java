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

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.phase_2.testing.DatumIdentifier;
import org.mitre.icarus.cps.exam.phase_2.testing.DatumType;


/**
 * A probe where the participant estimates the probability that Red will attack (or not attack) at one or more locations.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="AttackProbabilityReportProbe", namespace="IcarusCPD_2")
public class AttackProbabilityReportProbe extends ProbabilityReportProbe<AttackProbability> {
	
	/**
	 * Construct an empty AttackProbabilityReportProbe.
	 */
	public AttackProbabilityReportProbe() {}
	
	/**
	 * Construct an AttackProbabilityReportProbe with the given datum ID, datum list, and probabilities.
	 * 
	 * @param id the datum ID of this probability report
	 * @param datumList the datum to consider when reporting the probability or probabilities
	 * @param probabilities the probabilities
	 */
	public AttackProbabilityReportProbe(TrialPartProbeType type, String id, 
			List<DatumIdentifier> datumList, List<AttackProbability> probabilities) {
		super(id, datumList, probabilities);
		setType(type);
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbe#setType(org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbeType)
	 */
	@Override
	public void setType(TrialPartProbeType type) {
		if(type != TrialPartProbeType.AttackProbabilityReport_Pp && 
				type != TrialPartProbeType.AttackProbabilityReport_Ppc &&
				type != TrialPartProbeType.AttackProbabilityReport_Pt &&
				type != TrialPartProbeType.AttackProbabilityReport_Ptpc) {
			throw new IllegalArgumentException("AttackProbabilityReportProbe type must be one of" +
					"AttackProbabilityReport_Pp, AttackProbabilityReport_Ppc, AttackProbabilityReport_Pt," +
					" or AttackProbabilityReport_Ptpc.");
		}
		this.type = type;
	}
	
	/**
	 * Convenience method to get the attack probability at a given location.
	 * 
	 * @param locationId the ID of the location
	 * @return the attack probability at the location
	 */
	public AttackProbability getProbabilityAtLocation(String locationId) {		
		if(probabilities != null && !probabilities.isEmpty()) {
			for(AttackProbability probability : probabilities) {
				if(locationId.equals(probability.getLocationId())) {
					return probability;
				}
			}
		}
		return null;
	}	
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.ProbabilityReportProbe#getProbabilities()
	 */
	@Override
	@XmlElement(name = "Probability")
	public List<AttackProbability> getProbabilities() {		
		return super.getProbabilities();
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.ProbabilityReportProbe#setProbabilities(java.util.List)
	 */
	@Override
	public void setProbabilities(List<AttackProbability> probabilities) {		
		super.setProbabilities(probabilities);
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.IDatum#getDatumType()
	 */
	@Override
	public DatumType getDatumType() {
		return DatumType.AttackProbabilityReport;
	}
}