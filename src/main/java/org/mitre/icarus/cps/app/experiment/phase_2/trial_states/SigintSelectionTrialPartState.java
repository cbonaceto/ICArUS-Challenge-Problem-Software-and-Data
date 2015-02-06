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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.mitre.icarus.cps.app.widgets.phase_2.experiment.LocationDescriptor;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.int_presentation.SigintSelectionProbe;
import org.mitre.icarus.cps.feature_vector.phase_2.int_datum.SigintDatum;

/**
 * 
 * @author cbonaceto
 *
 */
public class SigintSelectionTrialPartState extends TrialPartState_Phase2 {
	
	/** The SIGINT selection probe */
	protected SigintSelectionProbe probe;
	
	/** The location(s) selected to obtain SIGINT on */
	protected List<LocationDescriptor> selectedLocations;
	
	/** The SIGINT datum at each location (maps location ID to the SIGINT datum for that location) */
	protected Map<String, SigintDatum> sigintDatumMap;	
	
	/** The SIGINT presentation following the SIGINT selection */
	protected List<IntPresentationTrialPartState<SigintDatum>> sigintPresentations;

	public SigintSelectionTrialPartState(int trialNumber, int trialPartNumber) {
		super(trialNumber, trialPartNumber);
		sigintDatumMap = new HashMap<String, SigintDatum>();
	}
	
	public SigintSelectionTrialPartState(int trialNumber, int trialPartNumber,
			SigintSelectionProbe probe) {
		super(trialNumber, trialPartNumber);
		sigintDatumMap = new HashMap<String, SigintDatum>();
		setProbe(probe);
	}

	@Override
	public SigintSelectionProbe getProbe() {
		return probe;
	}
	
	public void setProbe(SigintSelectionProbe probe) {
		this.probe = probe;		
		//selectedLocationIds = probe != null ? probe.getSelectedLocationIds() : null;
	}
	
	public int getNumLocationsToSelect() {
		return probe != null ? probe.getNumSigintSelections() : 0;
	}		

	public List<LocationDescriptor> getSelectedLocations() {
		return selectedLocations;
	}

	public void setSelectedLocations(List<LocationDescriptor> selectedLocations) {
		this.selectedLocations = selectedLocations;
	}

	public List<IntPresentationTrialPartState<SigintDatum>> getSigintPresentations() {
		return sigintPresentations;
	}

	public void setSigintPresentations(List<IntPresentationTrialPartState<SigintDatum>> sigintPresentations) {
		this.sigintPresentations = sigintPresentations;
	}
	
	public SigintDatum getSigintDatum(String locationId) {
		return sigintDatumMap.get(locationId);
	}
	
	public void setSigIntDatum(String locationId, SigintDatum sigintDatum) {
		sigintDatumMap.put(locationId, sigintDatum);
	}

	/* 
	 * Also updates the SIGINT presentation(s) (if any) to reflect the locations selected to obtain SIGINT at.
	 * 
	 * (non-Javadoc)
	 * @see org.mitre.icarus.cps.app.experiment.phase_2.trial_states.TrialPartState_Phase2#validateAndUpdateProbeResponseData()
	 */
	@Override
	public boolean validateAndUpdateProbeResponseData() {
		if(probe != null) {
			List<String> selectedLocationIds = new ArrayList<String>();
			List<Integer> selectedLocationIndexes = new ArrayList<Integer>();
			if(selectedLocations != null && !selectedLocations.isEmpty()) {
				for(LocationDescriptor location : selectedLocations) {
					selectedLocationIds.add(location.getLocationId());
					selectedLocationIndexes.add(location.getLocationIndex());
				}
			}			
			probe.setSelectedLocationIds(selectedLocationIds);
			//probe.setSelectedLocationIndexes(selectedLocationIndexes);
						
			//Update the SIGINT presentation(s) to reflect the location selection(s)
			if(sigintPresentations != null && !sigintPresentations.isEmpty() && selectedLocations != null
					&& !selectedLocations.isEmpty()) {
				//Iterator<LocationDescriptor> locationIter = selectedLocations.iterator();
				Iterator<IntPresentationTrialPartState<SigintDatum>> sigintPresentationIter = sigintPresentations.iterator();				
				if(sigintPresentations.size() == selectedLocations.size()) {
					Iterator<LocationDescriptor> locationIter = selectedLocations.iterator();
					while(sigintPresentationIter.hasNext()) {
						IntPresentationTrialPartState<SigintDatum> sigintPresentation = sigintPresentationIter.next();
						LocationDescriptor location = locationIter.next();
						sigintPresentation.getProbe().setLocationIds(Collections.singletonList(location.getLocationId()));
						sigintPresentation.getProbe().setLocationIndexes(Collections.singletonList(location.getLocationIndex()));
						sigintPresentation.setIntDatum(location.getLocationId(), sigintDatumMap.get(location.getLocationId()));
					}
				} else {
					IntPresentationTrialPartState<SigintDatum> sigintPresentation = sigintPresentationIter.next();
					sigintPresentation.getProbe().setLocationIds(selectedLocationIds);
					sigintPresentation.getProbe().setLocationIndexes(selectedLocationIndexes);
					for(String locationId : selectedLocationIds) {
						sigintPresentation.setIntDatum(locationId, sigintDatumMap.get(locationId));
					}
				}
			}			
			
			//Return whether the correct number of location(s) were selected
			return probe.getNumSigintSelections() == null || 
					(selectedLocationIds != null && selectedLocationIds.size() == probe.getNumSigintSelections()); 
		}
		return true;
	}
	
	@Override
	public TrialPartType getTrialPartType() {
		return TrialPartType.SigintSelection;
	}
}