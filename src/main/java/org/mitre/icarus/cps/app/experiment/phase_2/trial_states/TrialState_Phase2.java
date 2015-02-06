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
import java.util.LinkedList;
import java.util.List;

import org.mitre.icarus.cps.app.experiment.TrialPartState;
import org.mitre.icarus.cps.app.experiment.TrialState;
import org.mitre.icarus.cps.app.experiment.phase_2.ExperimentConstants_Phase2;
import org.mitre.icarus.cps.app.util.ProbabilityUtils;
import org.mitre.icarus.cps.exam.base.IcarusExam.NormalizationMode;
import org.mitre.icarus.cps.exam.phase_2.testing.DatumType;
import org.mitre.icarus.cps.exam.phase_2.testing.IcarusTestTrial_Phase2;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.AttackProbabilityReportProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.BlueActionSelectionProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.NormalizationConstraintType;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.RedActionSelectionProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.int_presentation.HumintPresentationProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.int_presentation.ImintPresentationProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.int_presentation.OsintPresentationProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.int_presentation.SigintPresentationProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.int_presentation.SigintSelectionProbe;
import org.mitre.icarus.cps.feature_vector.phase_2.BlueLocation;
import org.mitre.icarus.cps.feature_vector.phase_2.int_datum.HumintDatum;
import org.mitre.icarus.cps.feature_vector.phase_2.int_datum.ImintDatum;
import org.mitre.icarus.cps.feature_vector.phase_2.int_datum.OsintDatum;
import org.mitre.icarus.cps.feature_vector.phase_2.int_datum.SigintDatum;

/**
 * @author cbonaceto
 *
 */
public abstract class TrialState_Phase2<T extends IcarusTestTrial_Phase2> extends TrialState<TrialPartState_Phase2> {
	
	/** The trial */
	protected T trial;
	
	protected TrialState_Phase2(int trialNumber) {
		super(trialNumber);
		trialParts = new ArrayList<TrialPartState_Phase2>();
		/*if(trial != null) {
			createTrialPartStates(trial, attackProbabilityReportNormaliationMode, showBeginTrialPart);
		} else {
			trialParts = new ArrayList<TrialPartState_Phase2>();
		}*/
	}
	
	/**
	 * @return
	 */
	public T getTrial() {
		return trial;
	}

	/**
	 * @param trial
	 * @param attackProbabilityReportNormaliationMode
	 * @param showBeginTrialPart
	 */
	public void setTrial(T trial,
			NormalizationMode attackProbabilityReportNormaliationMode,
			NormalizationMode redTacticsProbabilityNormaliationMode,
			boolean showBeginTrialPart) {
		this.trial = trial;
		createTrialPartStates(attackProbabilityReportNormaliationMode, 
				redTacticsProbabilityNormaliationMode, showBeginTrialPart);
	}

	/**
	 * @param attackProbabilityReportNormaliationMode
	 * @param showBeginTrialPart
	 */
	protected abstract void createTrialPartStates(
			NormalizationMode attackProbabilityReportNormaliationMode,
			NormalizationMode redTacticsProbabilityNormaliationMode,
			boolean showBeginTrialPart);

	/**
	 * @param trial
	 * @param attackProbabilityReportNormaliationMode
	 * @param showBeginTrialPart
	 */
	protected int createStandardTrialPartStates(NormalizationMode attackProbabilityReportNormaliationMode, 
			boolean showBeginTrialPart) {		
		trialParts = new ArrayList<TrialPartState_Phase2>();
		int trialPartNum = -1;

		if(trial != null) {
			//Add the begin trial part
			if(showBeginTrialPart) {
				trialPartNum = addBeginTrialPart(trial.getBlueLocations(), trialPartNum);
			}

			//Add the OSINT presentation probe(s)
			trialPartNum = addOsintPresentation(trial.getOsintPresentation(), trialPartNum);

			//Add the IMINT presentation probe(s)
			trialPartNum = addImintPresentation(trial.getImintPresentation(), trialPartNum);

			//Add the P(Propensity) report probe
			trialPartNum = addAttackProbabilityReportProbe(trial.getAttackPropensityProbe_Pp(), 
					DatumType.AttackProbabilityReport_Propensity, 
					attackProbabilityReportNormaliationMode, trialPartNum);		

			//Add the HUMINT presentation probe(s)
			trialPartNum = addHumintPresentation(trial.getHumintPresentation(), trialPartNum);

			//Add the P(Capability, Propensity) report probe
			trialPartNum = addAttackProbabilityReportProbe(trial.getAttackProbabilityProbe_Ppc(), 
					DatumType.AttackProbabilityReport_Capability_Propensity, 
					attackProbabilityReportNormaliationMode, trialPartNum);

			//Add the SIGINT selection and/or presentation probe(s)
			trialPartNum = addSigintPresentationOrSelection(trial.getSigintSelectionProbe(), 
					trial.getSigintPresentation(), trialPartNum);

			//Add the P (Activity) report probe
			trialPartNum = addAttackProbabilityReportProbe(trial.getAttackProbabilityProbe_Pt(), 
					DatumType.AttackProbabilityReport_Activity, 
					attackProbabilityReportNormaliationMode, trialPartNum);

			//Add the P (activity, propensity, capability) report probe
			trialPartNum = addAttackProbabilityReportProbe(trial.getAttackProbabilityProbe_Ptpc(), 
					DatumType.AttackProbabilityReport_Activity_Capability_Propensity, 
					attackProbabilityReportNormaliationMode, trialPartNum);

			//Add the Blue action selection or presentation probe
			trialPartNum = addBlueActionSelection(trial.getBlueActionSelection(), trialPartNum);

			//Add the Red action presentation probe
			trialPartNum = this.addRedActionSelection(trial.getRedActionSelection(), trialPartNum);
		}
		return trialPartNum;
	}
	
	protected int addBeginTrialPart(List<BlueLocation> blueLocations, int trialPartNum) {
		//Add the begin trial part
		if(blueLocations != null && !blueLocations.isEmpty()) {
			trialParts.add(new BeginTrialPartState(
					trialNumber, ++trialPartNum, blueLocations.size()));
		}
		return trialPartNum;
	}

	protected int addOsintPresentation(List<OsintPresentationProbe> probe, int trialPartNum) {
		//Add the OSINT presentation probe(s)
		if(probe != null && !probe.isEmpty()) {
			for(OsintPresentationProbe osintProbe : probe) {
				IntPresentationTrialPartState<OsintDatum> osintPresentation = 
						new IntPresentationTrialPartState<OsintDatum>(
								trialNumber, ++trialPartNum, osintProbe);
				trialParts.add(osintPresentation);
				if(osintProbe.getLocationIds() != null && !osintProbe.getLocationIds().isEmpty()) {
					for(String locationId : osintProbe.getLocationIds()) {
						osintPresentation.setIntDatum(locationId, trial.getOsint(locationId));
					}
				}
			}			 
		}
		return trialPartNum;
	}

	protected int addImintPresentation(List<ImintPresentationProbe> probe, int trialPartNum) {
		//Add the IMINT presentation probe(s)
		if(probe != null && !probe.isEmpty()) {
			for(ImintPresentationProbe imintProbe : probe) {
				IntPresentationTrialPartState<ImintDatum> imintPresentation = 
						new IntPresentationTrialPartState<ImintDatum>(
								trialNumber, ++trialPartNum, imintProbe);
				trialParts.add(imintPresentation);
				if(imintProbe.getLocationIds() != null && !imintProbe.getLocationIds().isEmpty()) {
					for(String locationId : imintProbe.getLocationIds()) {
						imintPresentation.setIntDatum(locationId, trial.getImint(locationId));
					}
				}
			}			 
		}
		return trialPartNum;
	}

	protected int addHumintPresentation(List<HumintPresentationProbe> probe, int trialPartNum) {
		//Add the HUMINT presentation probe(s)
		if(probe != null && !probe.isEmpty()) {
			for(HumintPresentationProbe humintProbe : probe) {
				IntPresentationTrialPartState<HumintDatum> humintPresentation = 
						new IntPresentationTrialPartState<HumintDatum>(
								trialNumber, ++trialPartNum,humintProbe);
				trialParts.add(humintPresentation);
				if(humintProbe.getLocationIds() != null && !humintProbe.getLocationIds().isEmpty()) {
					for(String locationId : humintProbe.getLocationIds()) {
						humintPresentation.setIntDatum(locationId, trial.getHumint(locationId));
					}
				}
			}			 
		}
		return trialPartNum;
	}

	protected int addSigintPresentationOrSelection(SigintSelectionProbe sigintSelectionProbe,
			List<SigintPresentationProbe> sigintPresentation, int trialPartNum) {		
		if(sigintSelectionProbe != null && sigintSelectionProbe.getNumSigintSelections() != null &&
				sigintSelectionProbe.getNumSigintSelections() > 0) {	
			//Add the SIGINT selection probe
			int numSigintSelections = sigintSelectionProbe.getNumSigintSelections();
			SigintSelectionTrialPartState sigintSelection = new SigintSelectionTrialPartState(trialNumber, ++trialPartNum,
					sigintSelectionProbe);
			trialParts.add(sigintSelection);
			List<String> locationIds = sigintSelectionProbe.getLocationIds();
			if(locationIds != null && !locationIds.isEmpty()) {
				for(String locationId : locationIds) {
					sigintSelection.setSigIntDatum(locationId, trial.getSigint(locationId));
				}
			}
			//Add SIGINT presentations after the SIGINT selection probe
			if(sigintPresentation == null || sigintPresentation.isEmpty()) {
				sigintPresentation = Collections.singletonList(new SigintPresentationProbe());
				trial.setSigintPresentation(sigintPresentation);
			}
			if(sigintPresentation.size() == 1) {
				//Present SIGINT at each location simultaneously
				IntPresentationTrialPartState<SigintDatum> sigintPresentationState = 
						new IntPresentationTrialPartState<SigintDatum>(
						trialNumber, ++trialPartNum, sigintPresentation.iterator().next());
				trialParts.add(sigintPresentationState);
				sigintSelection.setSigintPresentations(Collections.singletonList(sigintPresentationState));
			} else {
				List<IntPresentationTrialPartState<SigintDatum>> sigintPresentations = 
						new ArrayList<IntPresentationTrialPartState<SigintDatum>>(numSigintSelections);
				sigintSelection.setSigintPresentations(sigintPresentations);
				for(int i=0; i<numSigintSelections; i++) {
					IntPresentationTrialPartState<SigintDatum> sigintPresentationState = 
							new IntPresentationTrialPartState<SigintDatum>(
									trialNumber, ++trialPartNum, trial.getSigintPresentation().get(i));
					trialParts.add(sigintPresentationState);
					sigintPresentations.add(sigintPresentationState);
				}
				sigintSelection.setSigintPresentations(sigintPresentations);
			}
		} else if(sigintPresentation != null && !sigintPresentation.isEmpty()) {
			//Add the SIGINT presentation probe(s)
			for(SigintPresentationProbe sigintProbe : sigintPresentation) {
				IntPresentationTrialPartState<SigintDatum> sigintPresentationState = 
						new IntPresentationTrialPartState<SigintDatum>(trialNumber, ++trialPartNum, sigintProbe);
				trialParts.add(sigintPresentationState);
				if(sigintProbe.getLocationIds() != null && !sigintProbe.getLocationIds().isEmpty()) {
					for(String locationId : sigintProbe.getLocationIds()) {
						sigintPresentationState.setIntDatum(locationId, trial.getSigint(locationId));
					}
				}
			}			 
		}
		return trialPartNum;
	}

	@SuppressWarnings("unused")
	protected int addAttackProbabilityReportProbe(AttackProbabilityReportProbe probe,
			DatumType probeDatumType, NormalizationMode attackProbabilityReportNormaliationMode, 
			int trialPartNum) {
		List<ProbabilityReportTrialPartState> probabilityProbes = new LinkedList<ProbabilityReportTrialPartState>();
		if(probe != null) {
			Integer setting = (ExperimentConstants_Phase2.NORMALIZE_INITIAL_PROBABILITIES && probe.getProbabilities() != null && 
					!probe.getProbabilities().isEmpty()) ? ProbabilityUtils.getDefaultInitialProbability(probe.getProbabilities().size()) :						
						ExperimentConstants_Phase2.DEFAULT_INITIAL_PROBABILITY;
					AttackProbabilityReportTrialPartState trialPartState = 
							new AttackProbabilityReportTrialPartState(trialNumber, ++trialPartNum, 
									probeDatumType, probe, true, setting);
					trialParts.add(trialPartState);
					probabilityProbes.add(trialPartState);

					if(attackProbabilityReportNormaliationMode == NormalizationMode.NormalizeAfter || 
							attackProbabilityReportNormaliationMode == NormalizationMode.NormalizeAfterAndConfirm) {
						//Add a trial part state to confirm normalized settings
						trialParts.add(createNormalizationTrialPart(++trialPartNum, 
								probe.getTargetSum(), probe.getNormalizationConstraint(),
								probabilityProbes));			
					}		
		}
		return trialPartNum;
	}

	protected int addBlueActionSelection(BlueActionSelectionProbe probe, int trialPartNum) {
		//Add the Blue action selection or presentation probe
		if(probe != null) {
			BlueActionSelectionOrPresentationTrialPartState blueActionSelection = 
					new BlueActionSelectionOrPresentationTrialPartState(trialNumber, ++trialPartNum, probe);
			trialParts.add(blueActionSelection);
		}
		return trialPartNum;
	}

	protected int addRedActionSelection(RedActionSelectionProbe probe, int trialPartNum) {
		//Add the Red action presentation probe
		if(probe != null) {
			RedActionPresentationTrialPartState redActionPresentation = 
					new RedActionPresentationTrialPartState(trialNumber, ++trialPartNum, probe);
			trialParts.add(redActionPresentation);
		}
		return trialPartNum;
	}

	protected CorrectOrConfirmNormalizationTrialPartState createNormalizationTrialPart(int trialPartNum, 
			Double targetSum, NormalizationConstraintType normalizationConstraint, 
			List<ProbabilityReportTrialPartState> probabilityProbes) {
		CorrectOrConfirmNormalizationTrialPartState normalizationTrialPart = 
				new CorrectOrConfirmNormalizationTrialPartState(trialNumber, ++trialPartNum, targetSum, normalizationConstraint);		
		if(probabilityProbes != null && !probabilityProbes.isEmpty()) {
			for(ProbabilityReportTrialPartState probabilityProbe : probabilityProbes) {
				normalizationTrialPart.setProbabilityReport(
						probabilityProbe.getProbe() != null ? probabilityProbe.getProbe().getId() : 
							Integer.toString(trialPartNum), 
							probabilityProbe);
			}
		}
		return normalizationTrialPart;
	}


	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.app.experiment.TrialState#updateTrialResponseData()
	 */
	@Override
	public void updateTrialResponseData() {
		validateAndUpdateTrialResponseData();
	}

	/**
	 * @return
	 */
	public boolean validateAndUpdateTrialResponseData() {
		updateTimingData(getTrial());
		if(trialParts != null && !trialParts.isEmpty()) {
			boolean responseValid = true;
			for(TrialPartState trialPart : trialParts) {
				if(trialPart instanceof TrialPartState_Phase2) {
					if(!((TrialPartState_Phase2)trialPart).validateAndUpdateProbeResponseData()) {
						responseValid = false;
					}
				}
			}
			return responseValid;
		}
		return true;
	}

	/**
	 * Set the overall trial time in the trial object to the time stored in this trial state.
	 * 
	 * @param trial the trial
	 */
	protected void updateTimingData(IcarusTestTrial_Phase2 trial) {
		if(trial != null) {
			trial.setTrialTime_ms(trialTime_ms);
		}
	}
}