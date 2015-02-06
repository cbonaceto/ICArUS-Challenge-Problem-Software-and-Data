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
package org.mitre.icarus.cps.app.experiment.phase_05;

import java.util.ArrayList;
import java.util.List;

import org.mitre.icarus.cps.app.experiment.phase_05.ExamTiming;
import org.mitre.icarus.cps.app.experiment.phase_05.IcarusExamController_Phase05;
import org.mitre.icarus.cps.app.experiment.phase_05.TrialState.TrialType;
import org.mitre.icarus.cps.app.util.ProbabilityUtils;
import org.mitre.icarus.cps.app.widgets.phase_05.experiment.ConditionPanel_Phase05;
import org.mitre.icarus.cps.app.widgets.probability_entry.distinct_bars.BoxContainer.BoxOrientation;
import org.mitre.icarus.cps.assessment.score_computer.phase_05.ScoreComputer;
import org.mitre.icarus.cps.exam.base.IcarusExam.NormalizationMode;
import org.mitre.icarus.cps.exam.base.response.IcarusTrialResponse;
import org.mitre.icarus.cps.exam.base.testing.IcarusTestTrial;
import org.mitre.icarus.cps.exam.base.testing.IcarusTestTrial.TestTrialType;
import org.mitre.icarus.cps.exam.phase_05.IcarusExam_Phase05;
import org.mitre.icarus.cps.exam.phase_05.response.AssessmentTrialResponse;
import org.mitre.icarus.cps.exam.phase_05.response.IcarusTestPhaseResponse_Phase05;
import org.mitre.icarus.cps.exam.phase_05.response.IdentifyItemTrialResponse;
import org.mitre.icarus.cps.exam.phase_05.response.LayerData;
import org.mitre.icarus.cps.exam.phase_05.response.LocateItemTrialResponse;
import org.mitre.icarus.cps.exam.phase_05.response.SceneItemProbabilityResponseData;
import org.mitre.icarus.cps.exam.phase_05.response.SectorProbabilityResponseData;
import org.mitre.icarus.cps.exam.phase_05.testing.IcarusTestPhase_Phase05;
import org.mitre.icarus.cps.exam.phase_05.testing.assessment.AssessmentTrial;
import org.mitre.icarus.cps.exam.phase_05.testing.scene_presentation.LayerProbabilityData;
import org.mitre.icarus.cps.exam.phase_05.testing.scene_presentation.ScenePresentationTrial;
import org.mitre.icarus.cps.exam.phase_05.testing.scene_presentation.layer_presentation_types.LayerList;
import org.mitre.icarus.cps.exam.phase_05.testing.scene_presentation.layer_presentation_types.LayerPresentation;
import org.mitre.icarus.cps.exam.phase_05.testing.scene_presentation.layer_presentation_types.SequentialPresentation;
import org.mitre.icarus.cps.exam.phase_05.testing.scene_presentation.layer_presentation_types.UserChoicePresentation;
import org.mitre.icarus.cps.exam.phase_05.testing.scene_presentation.layer_presentation_types.LayerPresentation.PresentationType;
import org.mitre.icarus.cps.exam.phase_05.testing.scene_presentation.question_types.IdentifyItemQuestion;
import org.mitre.icarus.cps.exam.phase_05.testing.scene_presentation.question_types.LocateItemQuestion;
import org.mitre.icarus.cps.exam.phase_05.testing.scene_presentation.question_types.Question;

import org.mitre.icarus.cps.experiment_core.event.ConditionEvent;
import org.mitre.icarus.cps.experiment_core.event.SubjectActionEvent;
import org.mitre.icarus.cps.experiment_core.gui.INavButtonPanel.ButtonType;
import org.mitre.icarus.cps.experiment_core.subject_data.SubjectConditionData;

/**
 * Controller for the test phase in an exam.
 * NOTE: All GUI interactions must be done on the event dispatching thread.
 * 
 * @author Jing Hu and CBONACETO
 *
 */
@SuppressWarnings("deprecation")
public class TestPhaseController extends IcarusConditionController_Phase05<IcarusTestPhase_Phase05> {
	
	/** The set of all possible states the UI can take */
	private List<TrialState>[] trialStateArray;	
	
	/** The current phase in the current trial */
	private int trialPhase;
	
	/** Whether the subject is allowed to navigate back to a previous trial  */
	private boolean enableNavigationToPreviousTrial = false;
	
	/** Whether the subject is allowed to navigate back to a previous trial phase  */
	private boolean enableNavigationToPreviousPhase = false;
	
	/** The user has set a probability vector and it has been normalized  */
	private boolean confirmNormalized = false;   // eao	
	
	/** Whether we are in a user selected intel layer trial  */
	private boolean layerSelectTrialState = false;   // eao	
	
	public TestPhaseController(IcarusExam_Phase05 exam) {
		this.exam = exam;
	}
	
	@Override
	public IcarusExamController_Phase05 getExamController() {
		return examController;
	}		

	public boolean isEnableNavigationToPreviousTrial() {
		return enableNavigationToPreviousTrial;
	}

	public void setEnableNavigationToPreviousTrial(
			boolean enableNavigationToPreviousTrial) {
		this.enableNavigationToPreviousTrial = enableNavigationToPreviousTrial;
	}

	public boolean isEnableNavigationToPreviousPhase() {
		return enableNavigationToPreviousPhase;
	}

	public void setEnableNavigationToPreviousPhase(
			boolean enableNavigationToPreviousPhase) {
		this.enableNavigationToPreviousPhase = enableNavigationToPreviousPhase;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initializeConditionController(IcarusTestPhase_Phase05 condition, ConditionPanel_Phase05 conditionPanel) {		
		if(!(condition instanceof IcarusTestPhase_Phase05)) {
			throw new IllegalArgumentException("Condition must be type IcarusTestPhase!");
		}
		
		if(!(conditionPanel instanceof ConditionPanel_Phase05)) {
			throw new IllegalArgumentException("Condition panel must be type IcarusConditionPanel!");
		}
		
		this.condition = (IcarusTestPhase_Phase05)condition;
		this.conditionPanel = (ConditionPanel_Phase05) conditionPanel;
		trialStateArray = new List[condition.getNumTrials()];
		
		for (int i = 0; i < trialStateArray.length; i++) {
			trialStateArray[i] = createTrialStates(this.condition.getTestTrials().get(i), i+1);
		}
	}

	@Override
	public void startCondition(int firstTrial,
			IcarusExamController_Phase05 experimentController,
			SubjectConditionData subjectConditionData) {
		if(!(experimentController instanceof IcarusExamController_Phase05)) {
			throw new IllegalArgumentException("Experiment controller must be an Icarus Exam Controller!");
		}		
		conditionRunning = true;
		this.examController = (IcarusExamController_Phase05)experimentController;
		trialPhase = 0;
		
		if(examController != null) {
			examController.setNavButtonEnabled(ButtonType.Next, false);
		}
		
		setTrial(firstTrial, -1, -1);		
		
		//Pause before allowing a next button press
		pauseBeforeNextTrial();
	}
	
	/** Create set of normalized default probability values */
	private static ArrayList<Integer> createNormalizedDefaults(int size) {
		Integer val = 100/size;
		
		ArrayList<Integer> list = new ArrayList<Integer>(size);
		
		for (int i = 0; i < size; i++) {
			list.add(val);
		}
		
		return list;
	}
	
	/** Create set of default probability values */
	private static ArrayList<Integer> createDefaults(int size, int value) {
		ArrayList<Integer> list = new ArrayList<Integer>(size);
		
		for (int i = 0; i < size; i++) {
			list.add(value);
		}
		
		return list;
	}
	
	/**
	 * Sets the current trial.  Shows either a scene presentation or assessment trial. 
	 * Called whenever the trial changes.
	 */
	@Override
	public boolean setTrial(int trialNum, int trialPartNum, int numTrialParts) {
		currentTrial = trialNum;
		
		if(examController != null) {
			if(enableNavigationToPreviousTrial)  {
				examController.setNavButtonEnabled(ButtonType.Back, currentTrial > 0);					
			}
			else {
				examController.setNavButtonEnabled(ButtonType.Back, false);	
			}
		}
		
		IcarusTestTrial trial = condition.getTestTrials().get(currentTrial);
		if(trial instanceof ScenePresentationTrial) {
			ScenePresentationTrial scene = (ScenePresentationTrial)trial;
			conditionPanel.showScenePresentationTrial(scene, exam, trialStateArray[currentTrial].get(0));
		}
		else if(trial instanceof AssessmentTrial) {
			AssessmentTrial assessment = (AssessmentTrial)trial;
			conditionPanel.showAssessmentTrial(assessment, exam, trialStateArray[currentTrial].get(0));
		}				
		
		//Fire trial changed event
		return super.setTrial(trialNum, trialPartNum, numTrialParts);
	}

	/**
	 * Creates the set of states for a given trial
	 */
	private List<TrialState> createTrialStates(IcarusTestTrial trial, int trialNum) {
		if(trial.getTestTrialType() == TestTrialType.Assessment) {
			//Create states for an assessment trial
			AssessmentTrial assessment = (AssessmentTrial)trial;
			
			TrialState initialState = new TrialState(trialNum, 0, TrialType.Assessment);
			initialState.boxOrientation = BoxOrientation.HORIZONTAL_LINE;
			initialState.numBoxes = 1;
			if(assessment.getSceneItemsToProbe() != null && !assessment.getSceneItemsToProbe().isEmpty()) {
				initialState.numBoxes = assessment.getSceneItemsToProbe().size();
			}
			if(assessment.isProbabilitiesNormalized()) {
				initialState.setRawData(createNormalizedDefaults(initialState.numBoxes));
			}
			else {
				initialState.setRawData(createDefaults(initialState.numBoxes, 50));	
			}
			
			List<TrialState> trialStates = new ArrayList<TrialState>();
			trialStates.add(initialState);
			
			if(assessment.isProbabilitiesNormalized() &&
				exam.getNormalizationMode() == NormalizationMode.NormalizeAfterAndConfirm) {
				//Add a trial state to confirm normalized settings
				TrialState confirmNormalizedState = new TrialState(trialNum, 1, TrialType.ConfirmNormalized);
				confirmNormalizedState.setNumBoxes(initialState.numBoxes);
				confirmNormalizedState.setBoxOrientation(initialState.boxOrientation);
				confirmNormalizedState.setNormalizedData(new ArrayList<Integer>());			
				trialStates.add(confirmNormalizedState);
			}
			
			return trialStates;
		}		
		else if(trial.getTestTrialType() == TestTrialType.ScenePresentation) {
			//Create states for a scene presentation trial			
			ScenePresentationTrial scene = (ScenePresentationTrial)trial;			

			int phase = 0;
			TrialState initialState = new TrialState(trialNum, phase, TrialType.ScenePresentation);		
			//initialState.getSelectedLayers().addAll(currentScene.getBaseLayers());

			for (Integer i : scene.getBaseLayers()) {
				initialState.getSelectedLayers().add(new LayerData(i, PresentationType.Base));
			}

			Question question = scene.getQuestion();
			//BoxOrientation orientation = BoxOrientation.GRID;
			BoxOrientation orientation = BoxOrientation.HORIZONTAL_LINE;
			int numBoxes = 0;

			switch (question.getQuestionType()) {
			case IdentifyItem:

				IdentifyItemQuestion identify = (IdentifyItemQuestion) question;
				numBoxes = identify.getSceneItemsToProbe().size();
				//orientation = BoxOrientation.HORIZONTAL_LINE;

				break;
			case LocateItem:

				LocateItemQuestion locate = (LocateItemQuestion) question;				
				numBoxes = locate.getSectorsToProbe().size();

				break;
			default:
				System.err.println("Unknown Question Type");
			break;
			}

			initialState.setRawData(createNormalizedDefaults(numBoxes));
			initialState.setNumBoxes(numBoxes);
			initialState.setBoxOrientation(orientation);

			List<TrialState> trialStates = new ArrayList<TrialState>();
			trialStates.add(initialState);
			phase++;

			if(exam.getNormalizationMode() == NormalizationMode.NormalizeAfterAndConfirm) {
				//Add a trial state to confirm normalized settings
				TrialState confirmNormalizedState = new TrialState(trialNum, phase, TrialType.ConfirmNormalized);
				phase++;
				confirmNormalizedState.setNumBoxes(numBoxes);
				confirmNormalizedState.setBoxOrientation(orientation);
				confirmNormalizedState.setNormalizedData(new ArrayList<Integer>());			
				trialStates.add(confirmNormalizedState);
			}

			List<LayerPresentation> additionalLayers =
				scene.getAdditionalLayerPresentations();

			if (additionalLayers != null) {
				for (LayerPresentation lp : additionalLayers) {
					if (lp instanceof SequentialPresentation) {
						SequentialPresentation pres = (SequentialPresentation) lp;

						//pres.getSequentialLayers().size();
						for (LayerList l : pres.getSequentialLayers()) {
							TrialState state = new TrialState(trialNum, phase, TrialType.ScenePresentation);
							state.setNumBoxes(numBoxes);
							state.setBoxOrientation(orientation);

							for (Integer i : l.getLayers()) {
								state.getSelectedLayers().add(new LayerData(i, PresentationType.Sequential));
							}
							//state.setData(createDefaults(numBoxes));
							trialStates.add(state);
							phase++;

							if(exam.getNormalizationMode() == NormalizationMode.NormalizeAfterAndConfirm) {
								//Add a trial state to confirm normalized settings
								TrialState confirmNormalizedState = new TrialState(trialNum, phase, TrialType.ConfirmNormalized);
								phase++;
								confirmNormalizedState.setNumBoxes(numBoxes);
								confirmNormalizedState.setBoxOrientation(orientation);
								confirmNormalizedState.setNormalizedData(new ArrayList<Integer>());			
								trialStates.add(confirmNormalizedState);
							}
						}

					} else if (lp instanceof UserChoicePresentation) {
						UserChoicePresentation pres = (UserChoicePresentation) lp;					

						Integer numLayersToShow = pres.getNumOptionalLayersToShow();
						if(numLayersToShow == null) {
							//Show all optional layers if numOptionalLayersToShow is null
							numLayersToShow = pres.getOptionalLayers().getNumLayers();
						}

						for (int i = 0; i < numLayersToShow; i++) {
							LayerSelectTrialState state = new LayerSelectTrialState(trialNum, phase);
							state.setNumBoxes(numBoxes);

							for (Integer j : pres.getOptionalLayers().getLayers()) {
								state.getLayerChoices().add(new LayerData(j, PresentationType.UserChoice));
							}
							//state.setData(createDefaults(numBoxes));
							trialStates.add(state);
							phase++;

							TrialState postSelectState = new TrialState(trialNum, phase, TrialType.Other);
							postSelectState.setNumBoxes(numBoxes);
							//postSelectState.setData(createDefaults(numBoxes));

							trialStates.add(postSelectState);
							phase++;

							if(exam.getNormalizationMode() == NormalizationMode.NormalizeAfterAndConfirm) {
								//Add a trial state to confirm normalized settings
								TrialState confirmNormalizedState = new TrialState(trialNum, phase, TrialType.ConfirmNormalized);
								phase++;
								confirmNormalizedState.setNumBoxes(numBoxes);
								confirmNormalizedState.setBoxOrientation(orientation);
								confirmNormalizedState.setNormalizedData(new ArrayList<Integer>());			
								trialStates.add(confirmNormalizedState);
							}
						}
					}
				}
			}

			return trialStates;
		}

		return null;
	}

	@Override
	public void subjectActionPerformed(SubjectActionEvent event) {	
		//System.out.println("TestPhaseController.subjectActionPerfomed phase= " + IcarusExamController.currentConditionIndex);  // eao
		//System.out.println("TestPhaseController.subjecActionPerfomed trialPhase= " + trialPhase + " currentTrial= " + currentTrial);  // eao
		if (event.eventType == SubjectActionEvent.NEXT_BUTTON_PRESSED) {
			//System.out.println("TestPhaseController.subjectActionPerfomed NEXT_BUTTON_PRESSED");  // eao
			// if we have a NEXT_BUTTON_PRESSED after a confirmNoralized then set probability timing
			// unless the user has just selected an intel layer
			if(confirmNormalized){  // eao
				if(!layerSelectTrialState){
					IcarusExamController_Phase05.examTiming.setProbabilityTiming(IcarusExamController_Phase05.currentConditionIndex, currentTrial);
				}
				else {
					 layerSelectTrialState = false;
				}
				confirmNormalized = false;
			}
			
			// if this is the NEXT_BUTTON_PRESSED after an intel layer selection then stop the timing
			if(layerSelectTrialState){  // eao
				IcarusExamController_Phase05.examTiming.stopLayerSelectTiming(IcarusExamController_Phase05.currentConditionIndex, currentTrial);
				layerSelectTrialState = false;
			}
			
			// Increment the trial phase
			
			if(examController != null) {
				examController.setNavButtonEnabled(ButtonType.Next, false);
			}
			
			List<TrialState> trialStates = trialStateArray[currentTrial];
			TrialState currentState = trialStates.get(trialPhase);
			
			//Don't go to next state if a layer is not selected
			if(currentState instanceof LayerSelectTrialState && 
					((LayerSelectTrialState)currentState).getSelectedLayer() == null) {
				if(examController != null) {
					examController.setNavButtonEnabled(ButtonType.Next, true);
				}				
				return;
			}
			
			if(currentState.getTrialType() != TrialType.ConfirmNormalized) {
				//System.out.println("TestPhaseController.subjecActionPerfomed confirmNormalized");  // eao
				confirmNormalized = true;  // eao
				
				// set the time it took for the user to set the probability vector 
				//IcarusExamController.examTiming.setProbabilityTiming(IcarusExamController.currentConditionIndex, currentTrial);
				
				//Get the "raw" probability settings the subject entered
				currentState.setRawData(conditionPanel.getBoxes().getCurrentSettings());			

				if(exam.getNormalizationMode() == NormalizationMode.NormalizeAfter ||
						exam.getNormalizationMode() == NormalizationMode.NormalizeAfterAndConfirm) {
					//Get the normalized probability settings
					currentState.setNormalizedData(ProbabilityUtils.cloneProbabilities(currentState.getRawData()));	
					//System.out.println(currentState.getRawData());
					ProbabilityUtils.normalizePercentProbabilities(currentState.getRawData(), currentState.getNormalizedData());
				}
			}
			
			// set the new state 
			trialPhase++;
			if (trialPhase == trialStates.size()) {
				//System.out.println("TestPhaseController.subjecActionPerfomed end of trial");  // eao
				//We're at the end of the current trial or the end of the experiment
				if (currentTrial + 1 == trialStateArray.length) {	
					conditionRunning = false;
				
					examController.setNavButtonEnabled(ButtonType.Back, false);
					
					trialPhase = trialStates.size() - 1;					
					
					// Fire condition completed event
					fireConditionEvent(new ConditionEvent(ConditionEvent.CONDITION_COMPLETED, 
							currentTrial + 1, this));
				} else {
					//We're at the end of the current trial, go to the next trial
					trialPhase = 0;
					setTrial(currentTrial + 1, -1, -1);
					
					//Pause before allowing a next button press
					pauseBeforeNextTrial();		
				}
				//System.out.println("TestPhaseController.subjecActionPerfomed return after end of trial");  // eao
				return;
			}
			//System.out.println("TestPhaseController.subjecActionPerfomed go to next state");  // eao
			//Go to the next state in the current trial
			TrialState newState = trialStates.get(trialPhase);
			if (newState.getRawData().isEmpty() || newState instanceof LayerSelectTrialState) {
				newState.setRawData(conditionPanel.getBoxes().getCurrentSettings());
			}

			// special handling for user selectable layers
			if (currentState instanceof LayerSelectTrialState) {
				LayerSelectTrialState oldSelect = (LayerSelectTrialState) currentState;

				newState.getSelectedLayers().clear();
				LayerData selectedLayer = oldSelect.getSelectedLayer();
				if (selectedLayer != null) {
					newState.getSelectedLayers().add(selectedLayer);
				}
			}
			
			// update layers
			newState.getSelectedLayers().addAll(currentState.getSelectedLayers());
			
			//Special handling for confirming normalized settings
			if(newState.getTrialType() == TrialType.ConfirmNormalized) {
				examController.setNavButtonEnabled(ButtonType.Back, true);
				newState.setNormalizedData(currentState.getNormalizedData());
				if(trialPhase > 2) {
					updateConditionPanel(trialStates.get(trialPhase-2), newState);
				}
				else {
					updateConditionPanel(null, newState);
				}
			}			
			else {
				if(examController != null) {					
					examController.setNavButtonEnabled(ButtonType.Back,
							enableNavigationToPreviousPhase);
				}
				
				updateConditionPanel(currentState, newState);
			}		
			
			//Pause before allowing a next button press
			pauseBeforeNextTrial();	
			//System.out.println("TestPhaseController.subjecActionPerfomed end of next button pushed");  // eao
		} 
		else if (event.eventType == SubjectActionEvent.BACK_BUTTON_PRESSED) {
			//System.out.println("TestPhaseController.subjectActionPerfomed BACK_BUTTON_PRESSED");  // eao
			// Decrement the trial phase	
			if (--trialPhase == -1) {
				
				if (currentTrial == 0) {
					trialPhase = 0;
					return;
				}
				
				//Go to the previous trial			
				setTrial(currentTrial - 1, -1, -1);
				trialPhase = trialStateArray[currentTrial].size() - 1;
			}
			
			//Go to the previous state in the current trial
			List<TrialState> trialStates = trialStateArray[currentTrial];
			TrialState currentState = trialStates.get(trialPhase);
			
			// reset the selected layers lists
			if (trialPhase < trialStates.size() - 1) {
				TrialState oldState = trialStates.get(trialPhase + 1);
				oldState.getSelectedLayers().removeAll(currentState.getSelectedLayers());
			}
			
			//Special handling for confirming normalized settings
			if(currentState.getTrialType() == TrialType.ConfirmNormalized) {
				//newState.setNormalizedData(currentState.getNormalizedData());
				if(trialPhase > 2) {
					updateConditionPanel(trialStates.get(trialPhase-2), currentState);
				}
				else {
					updateConditionPanel(null, currentState);
				}
				examController.setNavButtonEnabled(ButtonType.Back, true);
			}			
			else {
				if(examController != null) {
					if((trialPhase == 0 && currentTrial > 0 && enableNavigationToPreviousTrial) ||
							(trialPhase > 0 && enableNavigationToPreviousPhase)) {												
						examController.setNavButtonEnabled(ButtonType.Back, true);
					}
					else {
						//Disable back button to prevent navigation to previous trial or trial phase	
						examController.setNavButtonEnabled(ButtonType.Back, false);
					}
				}
				updateConditionPanel(
						(trialPhase == 0) ? null : trialStates.get(trialPhase - 1),
						currentState);
			}			
		}
	}

	/**
	 * Updates the UI
	 */
	private void updateConditionPanel(TrialState previousState, TrialState newState) {	
		//System.out.println("TestPhaseController.updateConditionPanel");  // eao
		if (newState instanceof LayerSelectTrialState) {
			System.out.println("TestPhaseController.updateConditionPanel LayerSelectTrialState");  // eao
			layerSelectTrialState = true;  // eao
			IcarusExamController_Phase05.examTiming.startLayerSelectTiming(IcarusExamController_Phase05.currentConditionIndex, currentTrial);
			
			LayerSelectTrialState layerSelect = (LayerSelectTrialState) newState;
			// TODO remove already selected layers
			conditionPanel.showLayerSelect(layerSelect, exam.getNormalizationMode());
		} else {
			//System.out.println("TestPhaseController.updateConditionPanel new State");  // eao
			conditionPanel.showBoxes(newState,
					(previousState == null) ? null : (previousState.getNormalizedData() != null) ? previousState.getNormalizedData() : previousState.getRawData(),
							exam.getNormalizationMode());
		}
	}
	
	/**
	 * Generates response data from the trial states
	 */
	public IcarusTestPhaseResponse_Phase05 getResponseData(int phase, ExamTiming examTiming) {
		IcarusTestPhaseResponse_Phase05 responseData = new IcarusTestPhaseResponse_Phase05();
		responseData.setPhaseName(condition.getName());
		//ArrayList <TimingInstance> timings = examTiming.getExamTimings();  // eao
		
		ArrayList<IcarusTrialResponse> trials = new ArrayList<IcarusTrialResponse>();
		
		ArrayList<Double> trialScores = new ArrayList<Double>();
		
		for (int i = 0; i < trialStateArray.length; i++) {
			IcarusTestTrial trial = condition.getTestTrials().get(i);
			
			if(trial instanceof ScenePresentationTrial) {
				//Create response data for a scene presentation trial
				Question question = null;
				question = ((ScenePresentationTrial)trial).getQuestion();
				
				ArrayList<Double> phaseScores = new ArrayList<Double>();
				
				if (question instanceof IdentifyItemQuestion) {
					//System.out.println("getResponseData IdentifyItemQuestion");   // eao
					IdentifyItemQuestion id = (IdentifyItemQuestion) question;
					
					IdentifyItemTrialResponse response = new IdentifyItemTrialResponse();
					response.setTrialNum(i);
					response.setSectorID(id.getSectorToProbe());
					//TODO: Set time for the trial here
					response.setTrialTime_ms(examTiming.getTiming(phase,i));  // eao
					
					ArrayList<SceneItemProbabilityResponseData> probabilityData =
						new ArrayList<SceneItemProbabilityResponseData>(
							trialStateArray[i].size());
					
					Long layerSelectTime = null;					
					for (TrialState state : trialStateArray[i]) {						
						if (state.getTrialType() == TrialType.LayerSelect || 
								state.getTrialType() == TrialType.ConfirmNormalized) {
							if(state.getTrialType() == TrialType.LayerSelect) {
								//TODO: Set layer select time here
								layerSelectTime = examTiming.getLayerSelectTiming(phase,i);
							}
							else {
								//layerSelectTime = null;
							}
							continue;						
						}
						else {
							//layerSelectTime = null;
						}
						
						SceneItemProbabilityResponseData prob = new SceneItemProbabilityResponseData();

						// set the probability time for this trial state here

						prob.setProbabilityEntryTime_ms(examTiming.getProbabilityTiming(phase,i,0));  // eao	
						//System.out.println("getResponseData setProbability and set layerSelectionTime 637");  // eao
						
						prob.setLayerSelectionTime_ms(layerSelectTime);
						//prob.setLayerSelectionTime_ms(layerSelectTime);
						
						prob.setLayersShown(state.getSelectedLayers());
						
						prob.fromIntList(state.getNormalizedData(), state.getRawData());
						probabilityData.add(prob);
						
						//Compute score for the trial phase
						LayerProbabilityData normativeData = 
							((ScenePresentationTrial)trial).getLayerProbabilityData(state.getSelectedLayers());
						if(normativeData != null) {
							List<Integer> subjectProbs = null;						
							if(state.getNormalizedData() != null) {
								subjectProbs = state.getNormalizedData();
							}
							else {
								subjectProbs = state.getRawData();
							}
							
							Double score = ScoreComputer.computeScore(subjectProbs,
									normativeData.getProbabilityDataForSector(id.getSectorToProbe(), 
											id.getSceneItemsToProbe()), 0d);
							prob.setScore(score);
							if(score != null) {
								//System.out.println("computed phase score: " + score + ",  normative data: " + 
								//		normativeData.getProbabilityDataForSector(id.getSectorToProbe(), id.getSceneItemsToProbe()));
								phaseScores.add(score);
							}
						}
					}
					//Compute average score for the trial
					if(!phaseScores.isEmpty()) {
						response.setScore(ScoreComputer.computeAverageScore(phaseScores));
						//System.out.println("computed average trial score: " + response.getScore());
						trialScores.add(response.getScore());
					}
					
					response.setSceneItemProbabilityData(probabilityData);					
					trials.add(response);
				} else if (question instanceof LocateItemQuestion) {
					//System.out.println("getResponseData LocateItemQuestion");   // eao
					LocateItemQuestion locate = (LocateItemQuestion) question;
					
					LocateItemTrialResponse response = new LocateItemTrialResponse();
					response.setTrialNum(i);
					response.setSceneItemID(locate.getSceneItemToProbe());
					//TODO: Set time for the trial here
					response.setTrialTime_ms(examTiming.getTiming(phase,i));  // eao
					//response.setTrialTime_ms(0L);

					ArrayList<SectorProbabilityResponseData> probabilityData =
						new ArrayList<SectorProbabilityResponseData>(
								trialStateArray[i].size());
					
					Long layerSelectTime = null;
					int stateCnt = 0;  // eao
					for (TrialState state : trialStateArray[i]) {
						//System.out.println("eao0");
						if (state.getTrialType() == TrialType.LayerSelect|| 							
								state.getTrialType() == TrialType.ConfirmNormalized) {
							if(state.getTrialType() == TrialType.LayerSelect) {								
								layerSelectTime = examTiming.getLayerSelectTiming(phase,i);
								//System.out.println("eao1 layerSelectTime= " + layerSelectTime);
							}
							else {
								//System.out.println("eao2");
								//layerSelectTime = null;
							}
							continue;						
						}
						else {
							//System.out.println("eao3");
							//layerSelectTime = null;
						}
						//System.out.println("eao4");
						SectorProbabilityResponseData prob = new SectorProbabilityResponseData();
						//TODO: set time for this trial state here
						//System.out.println("getResponseData phase= " + phase + " trial= " + i);  // eao
						//System.out.println("getResponseData setProbability 716 ");  // eao
						prob.setProbabilityEntryTime_ms(examTiming.getProbabilityTiming(phase,i,stateCnt));  // eao	
						stateCnt++;
						//System.out.println("eao5 final layerSelectTime= " + layerSelectTime);
						prob.setLayerSelectionTime_ms(layerSelectTime);
						
						prob.setLayersShown(state.getSelectedLayers());
						
						prob.fromIntList(state.getNormalizedData(), state.getRawData());
						probabilityData.add(prob);
						
						//Compute score for the trial phase
						LayerProbabilityData normativeData = 
							((ScenePresentationTrial)trial).getLayerProbabilityData(state.getSelectedLayers());
						if(normativeData != null) {
							List<Integer> subjectProbs = null;						
							if(state.getNormalizedData() != null) {
								subjectProbs = state.getNormalizedData();
							}
							else {
								subjectProbs = state.getRawData();
							}
							
							Double score = ScoreComputer.computeScore(subjectProbs,
									normativeData.getProbabilityDataForSceneItem(locate.getSceneItemToProbe(), 
											locate.getSectorsToProbe()), 0d);
							prob.setScore(score);
							if(score != null) {
								phaseScores.add(score);
							}
						}
					}
					
					//Compute average score for the trial
					if(!phaseScores.isEmpty()) {
						response.setScore(ScoreComputer.computeAverageScore(phaseScores));
						//System.out.println("computed average trial score: " + response.getScore());
						trialScores.add(response.getScore());
					}
					
					response.setSectorProbabilityData(probabilityData);					
					trials.add(response);
				}
			}
			else if(trial instanceof AssessmentTrial) {
				//System.out.println("getResponseData AssessmentTrial");   // eao
				//Create response data for an assessment trial
				AssessmentTrial assessment = (AssessmentTrial)trial;				
				AssessmentTrialResponse response = new AssessmentTrialResponse();
				response.setEvidence(assessment.getEvidence());
				//TODO: Set time for the trial here
				//response.setTrialTime_ms(0L);
				response.setTrialTime_ms(examTiming.getTiming(phase,i));  // eao
				
				TrialState state = trialStateArray[i].get(0);
				
				SceneItemProbabilityResponseData prob = 
					new SceneItemProbabilityResponseData();
				
				prob.setLayersShown(state.getSelectedLayers());
				prob.fromIntList(state.getNormalizedData(), state.getRawData());
				
				response.setSceneItemProbabilities(prob.getSceneItemProbabilities());
				trials.add(response);
			}				
		}
		
		//Compute the average score for the phase
		if(!trialScores.isEmpty()) {
			responseData.setAverageScore(ScoreComputer.computeAverageScore(trialScores));
			//System.out.println("computed average test phase score: " + responseData.getAverageScore());
		}
		
		responseData.setTrialResponses(trials);
		return responseData;
	}
}