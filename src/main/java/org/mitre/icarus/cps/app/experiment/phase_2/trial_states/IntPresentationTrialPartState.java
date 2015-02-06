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
package org.mitre.icarus.cps.app.experiment.phase_2.trial_states;

import java.util.HashMap;
import java.util.Map;

import org.mitre.icarus.cps.exam.phase_2.testing.DatumType;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.int_presentation.IntPresentationProbe;
import org.mitre.icarus.cps.feature_vector.phase_2.int_datum.IntDatum;

/**
 * A trial part to show the INT information at one or more locations.
 * 
 * @author cbonaceto
 *
 */
public class IntPresentationTrialPartState<T extends IntDatum> extends TrialPartState_Phase2 {
	
	/** The INT presentation probe */
	protected IntPresentationProbe probe;
	
	/** The INT datum for each location to show (maps location ID to the INT datum for that location) */
	protected Map<String, T> intDatumMap;

	public IntPresentationTrialPartState(int trialNumber, int trialPartNumber) {
		super(trialNumber, trialPartNumber);
		intDatumMap = new HashMap<String, T>();
	}
	
	public IntPresentationTrialPartState(int trialNumber, int trialPartNumber,
			IntPresentationProbe probe) {
		super(trialNumber, trialPartNumber);
		intDatumMap = new HashMap<String, T>();
		setProbe(probe);
	}

	@Override
	public IntPresentationProbe getProbe() {		
		return probe;
	}

	public void setProbe(IntPresentationProbe probe) {
		this.probe = probe;
	}
	
	public DatumType getIntDatumType() {
		return probe != null ? probe.getIntDatumType() : null;
	}
	
	public T getIntDatum(String locationId) {
		return intDatumMap.get(locationId);
	}
	
	public void setIntDatum(String locationId, T intDatum) {
		intDatumMap.put(locationId, intDatum);
	}
	
	@Override
	public boolean validateAndUpdateProbeResponseData() {
		//Does nothing, always returns true
		return true;
	}	
	
	@Override
	public TrialPartType getTrialPartType() {
		return TrialPartType.IntPresentation;
	}
}