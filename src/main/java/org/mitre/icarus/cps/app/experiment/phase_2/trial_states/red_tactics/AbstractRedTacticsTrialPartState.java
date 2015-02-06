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
package org.mitre.icarus.cps.app.experiment.phase_2.trial_states.red_tactics;

import java.util.List;

import org.mitre.icarus.cps.app.experiment.phase_2.trial_states.TrialPartState_Phase2;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.BatchPlotProbe.BatchPlotProbeButtonType;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.red_tactics_probes.AbstractRedTacticsProbe;

/**
 * @author CBONACETO
 *
 */
public abstract class AbstractRedTacticsTrialPartState<T extends AbstractRedTacticsProbe> extends TrialPartState_Phase2 {
	
	/** The probe */
	protected T probe;
	
	/** Whether a batch plot was created */
	protected boolean batchPlotCreated;
	
	/** The number of previous trials reviewed if a batch plot was created */
	protected Integer numPreviousTrialsSelected;
	
	/** The time spent creating a batch plot (in milliseconds) */
	protected Long batchPlotTime_ms;
	
	/** The IDs of locations the participant clicked on to obtain more information in a batch plot */	
	protected List<String> batchPlotBlueLocationsClicked;
	
	/** The sequence of "Backward" and "Forward" button presses when creating a batch plot */	
	protected List<BatchPlotProbeButtonType> batchPlotButtonPressSequence;

	public AbstractRedTacticsTrialPartState(int trialNumber, int trialPartNumber) {
		super(trialNumber, trialPartNumber);
	}
	
	public AbstractRedTacticsTrialPartState(int trialNumber, int trialPartNumber, T probe) {
		super(trialNumber, trialPartNumber);
		if(probe != null) {
			setProbe(probe);
		}
	}

	@Override
	public T getProbe() {
		return probe;
	}
	
	public void setProbe(T probe) {
		this.probe = probe;
		if(probe != null && probe.getBatchPlotProbe() != null) {
			batchPlotCreated = probe.getBatchPlotProbe().isResponsePresent();
			numPreviousTrialsSelected = probe.getBatchPlotProbe().getNumPreviousTrialsSelected();
			batchPlotBlueLocationsClicked = probe.getBatchPlotProbe().getBlueLocationsClicked();
			batchPlotButtonPressSequence = probe.getBatchPlotProbe().getButtonPressSequence();
		} else {
			batchPlotCreated = false;
			numPreviousTrialsSelected = null;
			batchPlotBlueLocationsClicked = null;
			batchPlotButtonPressSequence = null;
		}
		probeChanged();
	}
	
	protected abstract void probeChanged();	

	public boolean isBatchPlotCreated() {
		return batchPlotCreated;
	}

	public void setBatchPlotCreated(boolean batchPlotCreated) {
		this.batchPlotCreated = batchPlotCreated;
	}

	public Integer getNumPreviousTrialsSelected() {
		return numPreviousTrialsSelected;
	}

	public void setNumPreviousTrialsSelected(Integer numPreviousTrialsSelected) {
		this.numPreviousTrialsSelected = numPreviousTrialsSelected;
	}

	public Long getBatchPlotTime_ms() {
		return batchPlotTime_ms;
	}

	public void setBatchPlotTime_ms(Long batchPlotTime_ms) {
		this.batchPlotTime_ms = batchPlotTime_ms;
	}
	
	public List<String> getBatchPlotBlueLocationsClicked() {
		return batchPlotBlueLocationsClicked;
	}

	public void setBatchPlotBlueLocationsClicked(List<String> batchPlotBlueLocationsClicked) {
		this.batchPlotBlueLocationsClicked = batchPlotBlueLocationsClicked;
	}

	public List<BatchPlotProbeButtonType> getBatchPlotButtonPressSequence() {
		return batchPlotButtonPressSequence;
	}

	public void setBatchPlotButtonPressSequence(
			List<BatchPlotProbeButtonType> batchPlotButtonPressSequence) {
		this.batchPlotButtonPressSequence = batchPlotButtonPressSequence;
	}

	public abstract String getProbeInstructions();

	@Override
	protected boolean validateAndUpdateProbeResponseData() {	
		if(probe != null && probe.getBatchPlotProbe() != null) {
			probe.getBatchPlotProbe().setTrialPartTime_ms(batchPlotTime_ms);
			if(batchPlotCreated) {
				probe.getBatchPlotProbe().setNumPreviousTrialsSelected(numPreviousTrialsSelected);
				probe.getBatchPlotProbe().setBlueLocationsClicked(batchPlotBlueLocationsClicked);
				probe.getBatchPlotProbe().setButtonPressSequence(batchPlotButtonPressSequence);								
			} else {
				probe.getBatchPlotProbe().setNumPreviousTrialsSelected(0);
				probe.getBatchPlotProbe().setBlueLocationsClicked(null);
				probe.getBatchPlotProbe().setButtonPressSequence(null);
			}
		}
		boolean responseValid = validateAndUpdateAdditionalProbeResponseData();		
		return responseValid;
	}

	protected abstract boolean validateAndUpdateAdditionalProbeResponseData();	
}