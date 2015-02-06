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
package org.mitre.icarus.cps.app.experiment.phase_1.trial_states.trial_part_states;

import java.util.Collection;

import org.mitre.icarus.cps.exam.phase_1.response.trial_part_responses.TrialPartResponse;
import org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.INTLayerPresentationProbeBase;
import org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.HumintReport;
import org.mitre.icarus.cps.exam.phase_1.testing.trial_part_probes.TrialPartProbe;

/**
 * @author CBONACETO
 *
 * @param <T>
 */
public class LayerSelectTrialPartState<T extends INTLayerPresentationProbeBase> extends TrialPartState_Phase1 {
	
	/** The HUMINT report before the layer select */
	protected HumintReport initialHumintReport;
	
	/** The INT layer(s) that was/were selected.  Multiple layers may be purchased on a single LayerSelect probe
	 * in Task 7 */
	protected Collection<T> selectedLayers;	
	
	/** The probability probe following the layer select */
	protected ProbabilityProbeTrialPartState probabilityProbe;
	
	/** The surprise probe following the layer select (if any) */
	//protected SurpriseProbeTrialPartState surpriseProbe;

	public LayerSelectTrialPartState(int trialNumber, int trialPartNumber) {
		super(trialNumber, trialPartNumber);
	}
	
	public HumintReport getInitialHumintReport() {
		return initialHumintReport;
	}

	public void setInitialHumintReport(HumintReport initialHumintReport) {
		this.initialHumintReport = initialHumintReport;
	}

	public Collection<T> getSelectedLayers() {
		return selectedLayers;
	}
	
	public T getFirstSelectedLayer() {
		if(selectedLayers != null && !selectedLayers.isEmpty()) {
			return selectedLayers.iterator().next();
		}
		return null;
	}

	public void setSelectedLayers(Collection<T> selectedLayers) {
		this.selectedLayers = selectedLayers;
	}

	public ProbabilityProbeTrialPartState getProbabilityProbe() {
		return probabilityProbe;
	}

	public void setProbabilityProbe(ProbabilityProbeTrialPartState probabilityProbe) {
		this.probabilityProbe = probabilityProbe;
	}

	/*public SurpriseProbeTrialPartState getSurpriseProbe() {
		return surpriseProbe;
	}

	public void setSurpriseProbe(SurpriseProbeTrialPartState surpriseProbe) {
		this.surpriseProbe = surpriseProbe;
	}*/

	@Override
	public TrialPartProbe getProbe() {
		return null;
	}

	@Override
	public TrialPartResponse getResponse() {
		return null;
	}
}