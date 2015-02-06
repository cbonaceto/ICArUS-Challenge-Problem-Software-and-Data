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
package org.mitre.icarus.cps.app.experiment.phase_2;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.swing.JFrame;

import org.mitre.icarus.cps.app.experiment.IcarusConditionController;
import org.mitre.icarus.cps.app.experiment.phase_2.trial_states.AttackProbabilityReportTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_2.trial_states.BeginTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_2.trial_states.BlueActionSelectionOrPresentationTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_2.trial_states.CorrectOrConfirmNormalizationTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_2.trial_states.IntPresentationTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_2.trial_states.ProbabilityReportTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_2.trial_states.RedActionPresentationTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_2.trial_states.SigintSelectionTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_2.trial_states.TrialPartState_Phase2;
import org.mitre.icarus.cps.app.experiment.phase_2.trial_states.TrialState_Phase2;
import org.mitre.icarus.cps.app.experiment.phase_2.trial_states.TrialPartState_Phase2.TrialPartType;
import org.mitre.icarus.cps.app.experiment.phase_2.trial_states.red_tactics.AbstractRedTacticsTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_2.trial_states.red_tactics.MostLikelyRedTacticTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_2.trial_states.red_tactics.RedTacticParametersTrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_2.trial_states.red_tactics.RedTacticsProbabilityTrialPartState;
import org.mitre.icarus.cps.app.util.ProbabilityUtils;
import org.mitre.icarus.cps.app.widgets.map.phase_2.MapConstants_Phase2;
import org.mitre.icarus.cps.app.widgets.map.phase_2.MapPanelContainer;
import org.mitre.icarus.cps.app.widgets.map.phase_2.event.MapEvent;
import org.mitre.icarus.cps.app.widgets.map.phase_2.event.MapEventListener;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.BlueLocationPlacemark;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.BlueLocationPlacemark.DisplayMode;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.BlueLocationPlacemark.DisplaySize;
import org.mitre.icarus.cps.app.widgets.phase_2.ColorManager_Phase2;
import org.mitre.icarus.cps.app.widgets.phase_2.ImageManager_Phase2;
import org.mitre.icarus.cps.app.widgets.phase_2.experiment.ActionSelectionPanel;
import org.mitre.icarus.cps.app.widgets.phase_2.experiment.AssessmentReportPanelManager;
import org.mitre.icarus.cps.app.widgets.phase_2.experiment.CommandButton.CommandButtonType;
import org.mitre.icarus.cps.app.widgets.phase_2.experiment.ConditionPanel_Phase2;
import org.mitre.icarus.cps.app.widgets.phase_2.experiment.LocationDescriptor;
import org.mitre.icarus.cps.app.widgets.phase_2.experiment.LocationSelectionPanel;
import org.mitre.icarus.cps.app.widgets.phase_2.experiment.ProbabilityReportPanel;
import org.mitre.icarus.cps.app.widgets.phase_2.experiment.RedTacticSelectionPanel;
import org.mitre.icarus.cps.app.widgets.phase_2.experiment.AssessmentReportPanelManager.CommandButtonOrientation;
import org.mitre.icarus.cps.app.widgets.phase_2.experiment.datum_list.DatumListItem;
import org.mitre.icarus.cps.app.widgets.phase_2.experiment.datum_list.DatumListItemType;
import org.mitre.icarus.cps.app.widgets.phase_2.experiment.datum_list.MultiLocationDatumPanel;
import org.mitre.icarus.cps.app.widgets.phase_2.experiment.quad_chart.BlueBookPanel;
import org.mitre.icarus.cps.app.widgets.phase_2.experiment.quad_chart.JLabelQuadChartComponent;
import org.mitre.icarus.cps.app.widgets.phase_2.experiment.quad_chart.QuadChartPanel;
import org.mitre.icarus.cps.app.widgets.phase_2.experiment.quad_chart.QuadChartPanelFactory;
import org.mitre.icarus.cps.app.widgets.phase_2.experiment.quad_chart.RedTacticParametersPanel;
import org.mitre.icarus.cps.app.widgets.phase_2.experiment.report_panel.ReportComponentContainer.Alignment;
import org.mitre.icarus.cps.app.widgets.probability_entry.IProbabilityEntryContainer;
import org.mitre.icarus.cps.app.widgets.probability_entry.ProbabilitySumChangeEvent;
import org.mitre.icarus.cps.app.widgets.probability_entry.ProbabilitySumChangeListener;
import org.mitre.icarus.cps.assessment.score_computer.phase_2.ScoreComputer_Phase2;
import org.mitre.icarus.cps.assessment.score_computer.phase_2.ScoreComputer_Phase2.CumulativeAndIncrementalRedAttackProbabilities;
import org.mitre.icarus.cps.assessment.score_computer.phase_2.ScoreComputer_Phase2.RedBluePayoff;
import org.mitre.icarus.cps.assessment.score_computer.phase_2.ScoreComputer_Phase2.RedBluePayoffAtEachLocation;
import org.mitre.icarus.cps.exam.base.IcarusExam.NormalizationMode;
import org.mitre.icarus.cps.exam.phase_2.IcarusExam_Phase2;
import org.mitre.icarus.cps.exam.phase_2.generation.BlueAgent;
import org.mitre.icarus.cps.exam.phase_2.generation.RedAgent;
import org.mitre.icarus.cps.exam.phase_2.testing.DatumIdentifier;
import org.mitre.icarus.cps.exam.phase_2.testing.DatumType;
import org.mitre.icarus.cps.exam.phase_2.testing.IcarusTestTrial_Phase2;
import org.mitre.icarus.cps.exam.phase_2.testing.Mission;
import org.mitre.icarus.cps.exam.phase_2.testing.MissionType;
import org.mitre.icarus.cps.exam.phase_2.testing.Mission_4_5_6;
import org.mitre.icarus.cps.exam.phase_2.testing.bluebook.RedTactic;
import org.mitre.icarus.cps.exam.phase_2.testing.bluebook.RedTacticType;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.AttackProbability;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.BatchPlotProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.BatchPlotProbe.BatchPlotProbeButtonType;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.BlueAction;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.ItemAdjustment;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.RedAction;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.BlueAction.BlueActionType;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.RedAction.RedActionType;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.int_presentation.SigintPresentationProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.red_tactics_probes.AbstractRedTacticsProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.red_tactics_probes.MostLikelyRedTacticProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.red_tactics_probes.RedTacticParametersProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.red_tactics_probes.RedTacticProbability;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.red_tactics_probes.RedTacticsProbabilityReportProbe;
import org.mitre.icarus.cps.experiment_core.condition.InstructionsPage;
import org.mitre.icarus.cps.experiment_core.event.ConditionEvent;
import org.mitre.icarus.cps.experiment_core.event.SubjectActionEvent;
import org.mitre.icarus.cps.experiment_core.gui.IInstructionNavigationPanel;
import org.mitre.icarus.cps.experiment_core.gui.InstructionNavigationPanel;
import org.mitre.icarus.cps.experiment_core.gui.INavButtonPanel.ButtonType;
import org.mitre.icarus.cps.experiment_core.subject_data.SubjectConditionData;
import org.mitre.icarus.cps.feature_vector.phase_2.BlueLocation;
import org.mitre.icarus.cps.feature_vector.phase_2.int_datum.ImintDatum;
import org.mitre.icarus.cps.feature_vector.phase_2.int_datum.IntDatum;

/**
 *  Base class for Phase 2 mission controllers. Since missions are all variations of the same task,
 *  nearly all controller functionality is handled by this class. Sub-classes may perform 
 *  additional mission initialization.
 * 
 * @author CBONACETO
 *
 * @param <M> the Mission the controller is for
 * C 
 */
@SuppressWarnings("deprecation")
public abstract class MissionController<M extends Mission<?>, T extends TrialState_Phase2<?>> extends 
	IcarusConditionController<IcarusExamController_Phase2, IcarusExam_Phase2, M, ConditionPanel_Phase2> {
	
	/** The mission */
	protected M mission;
	
	/** The map */
	protected MapPanelContainer mapPanel;
	
	/** The location datum panel */
	protected MultiLocationDatumPanel locationDatumPanel;	
	
	/** The reporting panel manager (contains the reporting panel) */
	protected AssessmentReportPanelManager reportPanelManager;
	
	/** The external instructions window where BlueBook tables, payoff tables, and SIGINT reliability tables are shown */
	protected JFrame externalInstructionsWindow;
	protected IInstructionNavigationPanel externalInstructionsPanel;
	
	/** The BLUEBOOK panel */
	protected BlueBookPanel blueBookPanel;
	
	/** The SIGINT reliabilities panel */
	protected QuadChartPanel<JLabelQuadChartComponent> sigintReliabilityPanel;
	
	/** The Payoff Matrix panel */
	protected QuadChartPanel<JLabelQuadChartComponent> payoffMatrixPanel;
	
	/** Current trial start time */
	protected long trialStartTime;
	
	/** The trial states for each trial in the mission */
	protected ArrayList<T> trialStates;
	
	/** The trial state for the current trial */
	protected T currentTrialState;
	
	/** The current phase in the current trial */
	protected int trialPhase;
	
	/** The current trial part */
	protected TrialPartState_Phase2 currentTrialPart;
	
	/** Current trial part start time */
	protected long trialPartStartTime;
	
	/** Whether the subject is allowed to navigate back to a previous trial  */
	protected boolean enableNavigationToPreviousTrial = false;
	
	/** Whether the subject is allowed to navigate back to a trial part  */
	protected boolean enableNavigationToPreviousTrialPart = false;	
	
	/** Whether to show the score after each trial */
	protected boolean showScore = true;
	
	/** Whether to automatically pop-up the BlueBook, SIGINT reliability, and payoff matrix */
	protected boolean popupInstructions = true;
	
	/** Whether to show the trial number and trial part number in the instruction banner text */
	protected static boolean showTrialInfoInBanner = false;
	
	protected boolean nextButtonPressed = false;
	
	protected boolean backButtonPressed = false;
	
	/** Blue location placemark(s) for the current trial */
	private Map<String, BlueLocationPlacemark> currentBlueLocationPlacemarks;
	
	/** Locations that INTs have been presented or selected at */
	private IntPresentationLocations intPresentationLocations = new IntPresentationLocations();
	
	/** Whether the controller is currently in batch plot mode */
	private boolean batchPlotMode = false;
	
	/** Whether the controller is currently in "batch plot exclusive" mode, where the user is creating
	 * a batch plot and a next click will show the Red tactics probe  */
	private boolean batchPlotExclusiveMode = false;
	
	/** When creating a batch plot, contains information about the current batch plot state */
	private BatchPlotState batchPlotState = new BatchPlotState();	
	
	/** When creating batch plots, the blue location placemarks from previous trials */
	private ArrayList<List<BlueLocationPlacemark>> batchPlotPreviousOutcomes;
	
	/** When creating a batch plot, the previous outcomes currently displayed on the map */
	private LinkedList<BlueLocationPlacemark> batchPlotPreviousOutcomesDisplayed;
	
	/** The last Red tactics probability trial part state */
	private RedTacticsProbabilityTrialPartState lastRedTacticsProbabilityTrialPart;
	
	/** The last Red tactics parameters trial part state */
	private RedTacticParametersTrialPartState lastRedTacticsParametersTrialPart;
	private RedTactic userRedTactic = null;
	
	/** Listener to update SIGINT location selections */
	private SigintLocationSelectionListener sigintSelectionListener = new SigintLocationSelectionListener();
	
	/** Listener to update Blue action selections at each location */
	private BlueActionSelectionListener blueActionListener = new BlueActionSelectionListener();
	
	/** Listener to enable the Next button when a Red strategy is selected */
	private RedTacticSelectionListener redTacticSelectionListener = new RedTacticSelectionListener();
	
	/** Command button listener */
	private CommandButtonListener commandButtonListener = new CommandButtonListener();
	
	/** Listener to record that a location was clicked in a batch plot */
	private BatchPlotLocationClickedListener batchPlotLocationClickedListener = 
			new BatchPlotLocationClickedListener();
	
	/** Probability panel sum change listener and entry sequence listener 
	 * (records the sequence in which each probability was adjusted for the first time) */
	private ProbabilitySumAndSequenceListener probabilitySumAndSequenceListener = 
			new ProbabilitySumAndSequenceListener();	 
	
	/** Random number generator to compute payoffs in a show-down */
	protected Random random;
	
	/** The score computer */
	protected ScoreComputer_Phase2 scoreComputer = new ScoreComputer_Phase2();
	
	/** Blue and Red agents used to select Blue and Red actions when they are missing and the user is skipping trials */
	protected BlueAgent blueAgent = new BlueAgent(scoreComputer);
	protected RedAgent redAgent = new RedAgent();
	
	public MissionController() {
		//Create the external instructions panel and size it to fit a two-style BLUEBOOK
		//Dimension size =  new Dimension(370, 580);
		//externalInstructionsPanel.setInstructionsPanelPreferredSize(size);
		externalInstructionsPanel = new InstructionNavigationPanel();
		BlueBookPanel sizingPanel = QuadChartPanelFactory.createBlueBookPanel(
				Arrays.asList(RedTacticType.Mission_2_Passive.createTactic(), 
						RedTacticType.Mission_2_Aggressive.createTactic()), false);
		externalInstructionsPanel.setInstructionsPages("", 
				Collections.singletonList(new InstructionsPage(sizingPanel)));
		Dimension preferredSize = externalInstructionsPanel.getInstructionsPanelPreferredSize();
		externalInstructionsPanel.setInstructionsPanelPreferredSize(
				new Dimension(preferredSize.width + 12, preferredSize.height + 12));
		externalInstructionsPanel.removeAllInstructionsPages();
	}
		
	public void setRandom(Random random) {
		this.random = random;
	}
	
	@Override
	protected StringBuilder createTrialNumberString(int trialNumber, int trialPartNumber, int numTrialParts, String trialPartName) {		
		return showTrialInfoInBanner ? 
				super.createTrialNumberString(trialNumber, trialPartNumber, numTrialParts, trialPartName) : new StringBuilder();		
	}

	@Override
	public void initializeConditionController(M condition, ConditionPanel_Phase2 conditionPanel) {
		initializeConditionController(condition, conditionPanel, true);		
	}
	
	public void initializeConditionController(M condition, ConditionPanel_Phase2 conditionPanel,
			boolean clearResponseData) {
		try {
			this.mission = condition;
			this.condition = condition;
			this.conditionPanel = conditionPanel;
			externalInstructionsPanel.setInstructionsId(null);
			externalInstructionsPanel.removeAllInstructionsPages();
			blueBookPanel = null;
			sigintReliabilityPanel = null;
			payoffMatrixPanel = null;
			mapPanel = conditionPanel != null ? conditionPanel.getMapPanel() : null;
			locationDatumPanel = conditionPanel != null ? conditionPanel.getLocationDatumPanel() : null;
			reportPanelManager = conditionPanel != null ? conditionPanel.getReportPanelManager() : null;
			int numLocations = condition.getNumTrials() * 
					(condition.getMissionType() != null ? condition.getMissionType().getLocationsPerTrial() : 1);
			batchPlotPreviousOutcomes = new ArrayList<List<BlueLocationPlacemark>>(numLocations);
			initializeMission(clearResponseData);
		} catch(Exception ex) {			
			throw new IllegalArgumentException("Error initializing mission controller: " + ex.toString(), ex);
		}
	}
	
	/** Subclasses may override this to intialize data structures after the mission is set. */
	protected abstract void initializeMission(boolean clearResponseData);	

	/**
	 * @return
	 */
	public M getMissionWithUpdatedResponseData() {
		if(trialStates != null && !trialStates.isEmpty()) {
			for(TrialState_Phase2<?> trialState : trialStates) {
				trialState.updateTrialResponseData();
			}
		}		
		return mission;
	}
	
	public static boolean isShowTrialInfoInBanner() {
		return showTrialInfoInBanner;
	}

	public static void setShowTrialInfoInBanner(boolean showTrialInfoInBanner) {
		MissionController.showTrialInfoInBanner = showTrialInfoInBanner;
	}	
	
	
	@Override
	public void startCondition(int firstTrial,
			IcarusExamController_Phase2 experimentController,
			SubjectConditionData subjectConditionData) {
		conditionRunning = true;		
		this.examController = experimentController;
		
		if(examController != null) {
			examController.setNavButtonEnabled(ButtonType.Back, false);
			examController.setNavButtonEnabled(ButtonType.Next, false);
		}
		
		//Clear the map and initialize the area of interest for the mission
		turnOffBatchPlotMode();
		conditionPanel.setMapPanelVisible(true);
		mapPanel.resetMap();
		mapPanel.setAOILayerEnabled(true);
		mapPanel.setBlueRegionLayerEnabled(true);
		mapPanel.setIntAnnotationTextLayerEnabled(MapConstants_Phase2.SHOW_OSINT_TEXT || 
				MapConstants_Phase2.SHOW_IMINT_TEXT);
		mapPanel.setOsintLineLayerEnabled(MapConstants_Phase2.SHOW_OSINT_LINE);
		mapPanel.setImintCircleLayerEnabled(MapConstants_Phase2.SHOW_IMINT_CIRCLE);
		mapPanel.setSigintLayerEnabled(true);
		mapPanel.setBlueLocationsLayerEnabled(true);		
		mapPanel.setAOI(mission.getAoi());
		mapPanel.setBlueRegion(mission.getAoi() != null ? mission.getAoi().getBlueRegion() : null);		
		
		//Clear and initialize the report panel
		conditionPanel.setReportPanelVisible(true);
		reportPanelManager.setAllReportComponentsVisible(false);
		if(!reportPanelManager.isCommandButtonListenerPresent(commandButtonListener)) {
			reportPanelManager.addCommandButtonListener(commandButtonListener);
		}			
		conditionPanel.showTaskScreen(true, false, false);
		
		//Initialize the Red and Blue player scores for the mission
		mission.setRedScore(0D);
		mission.setBlueScore(0D);
		conditionPanel.setScores(null, null, mission.getRedScore(), mission.getBlueScore());
		
		//Initialize the number of batch plots created to 0 for Missions 4 - 6
		if(mission instanceof Mission_4_5_6) {
			((Mission_4_5_6)mission).setNumBatchPlotsCreated(0);
		}
		
		//Load the first trial
		currentTrialState = null;
		currentTrialPart = null;
		//currentTrial = -1;		
		currentTrial = firstTrial <= 0 ? -1 : firstTrial;
		trialPhase = -1;
		lastRedTacticsProbabilityTrialPart = null;
		lastRedTacticsParametersTrialPart = null;
		userRedTactic = null;
		nextTrialOrTrialPart();
	}	
	
	@Override
	protected void performCleanup() {		
		if(batchPlotPreviousOutcomes != null) batchPlotPreviousOutcomes.clear();
		if(batchPlotPreviousOutcomesDisplayed != null) batchPlotPreviousOutcomesDisplayed.clear();
		if(currentBlueLocationPlacemarks != null) currentBlueLocationPlacemarks.clear();
		if(isExternalInstructionsVisible()) {
			setExternalInstructionsVisible(false, null, null);
		}
		if(reportPanelManager != null) {
			reportPanelManager.removeCommandButtonListener(commandButtonListener);
			if(reportPanelManager.getLocationSelectionPanel() != null) {
				reportPanelManager.getLocationSelectionPanel().removeButtonActionListener(sigintSelectionListener);
			}
			if(reportPanelManager.getActionSelectionPanel() != null) {
				reportPanelManager.getActionSelectionPanel().removeButtonActionListener(blueActionListener);
			}
			if(reportPanelManager.getAttackProbabilityReportPanel() != null) {
				reportPanelManager.getAttackProbabilityReportPanel().removeSumChangeListener(
						probabilitySumAndSequenceListener);
			}
			if(reportPanelManager.getRedTacticsProbabilityReportPanel() != null) {
				reportPanelManager.getRedTacticsProbabilityReportPanel().removeSumChangeListener(
						probabilitySumAndSequenceListener);
			}
		}
		if(mapPanel != null) {
			mapPanel.getMapPanel().removeMapEventListener(batchPlotLocationClickedListener);
		}
	}	

	/**
	 * Go to the next trial or trial part in the mission.
	 */
	protected void nextTrialOrTrialPart() {		
		if(!conditionRunning) {
			return;
		}		
	
		examController.setNavButtonEnabled(ButtonType.Back, false);	
		examController.setNavButtonEnabled(ButtonType.Next, false);
		
		long currentTime = System.currentTimeMillis();
		
		//Save data, validate data, and perform clean-up from the previous trial part
		if(isExternalInstructionsVisible()) {
			setExternalInstructionsVisible(false, null, null);
		}
		boolean responseValid = true;
		if(currentTrialPart != null) {
			//Update the trial part elapsed time
			currentTrialPart.setTrialPartTime_ms(currentTrialPart.getTrialPartTime_ms() + 
					(currentTime - trialPartStartTime));			
			//Validate the current trial part and update participant response data
			switch(currentTrialPart.getTrialPartType()) {
			case AttackProbabilityReport: case AttackProbabilityReport_Activity:				
				case AttackProbabilityReport_Activity_Capability_Propensity:
				case AttackProbabilityReport_Capability_Propensity:
				case AttackProbabilityReport_Propensity:
				responseValid = validateAndUpdateAttackProbabilityReportTrialPart((AttackProbabilityReportTrialPartState)currentTrialPart);
				break;
			case BlueActionSelectionOrPresentation:
				responseValid = validateAndUpdateBlueActionSelectionTrialPart((BlueActionSelectionOrPresentationTrialPartState)currentTrialPart);
				break;
			case IntPresentation:
				//Remove INT banner(s)
				updateImintPresentationTrialPart((IntPresentationTrialPartState<?>)currentTrialPart);
				mapPanel.removeAllIntBanners();
				break;
			case MostLikelyRedTacticSelection:
				if(batchPlotExclusiveMode) {
					//Record the time spent creating a batch plot, close the batch plot, and show the Red tactics to select from					
					endBatchPlotExclusiveMode((AbstractRedTacticsTrialPartState<?>)currentTrialPart, 
							currentTime - trialPartStartTime);					
					trialPartStartTime = currentTime;
					return;
				} else {
					responseValid = validateAndUpdateMostLikelyRedTacticTrialPart((MostLikelyRedTacticTrialPartState)currentTrialPart);
				}
				break;
			case RedTacticsProbabilityProbe:
				if(batchPlotExclusiveMode) {
					//Record the time spent creating a batch plot, close the batch plot, and show the Red tactic probabilities
					endBatchPlotExclusiveMode((AbstractRedTacticsTrialPartState<?>)currentTrialPart, 
							currentTime - trialPartStartTime);
					//Pause before allowing a next button press
					boolean nextButtonDisabledUntilDataValid = 
							(ExperimentConstants_Phase2.RED_TACTIC_PROBABILITY_NORMALIZATION_MODE == NormalizationMode.NormalizeDuringManual ||
							!probabilitySumAndSequenceListener.isNormalizationConstraintMet());
					if(!nextButtonDisabledUntilDataValid) {						
						pauseBeforeNextTrial();
					}
					trialPartStartTime = currentTime;
					return;
				} else {
					responseValid = validateAndUpdateRedTacticsProbabilityTrialPart(
							lastRedTacticsProbabilityTrialPart = (RedTacticsProbabilityTrialPartState)currentTrialPart);
				}
				break;
			case RedTacticParametersProbe:
				if(batchPlotExclusiveMode) {
					//Record the time spent creating a batch plot, close the batch plot, and show the Red tactic parameters
					endBatchPlotExclusiveMode((AbstractRedTacticsTrialPartState<?>)currentTrialPart, 
							currentTime - trialPartStartTime);
					//Pause before allowing a next button press											
					pauseBeforeNextTrial();
					trialPartStartTime = currentTime;
					return;
				} else {
					responseValid = validateAndUpdateRedTacticParametersTrialPart(
							lastRedTacticsParametersTrialPart = (RedTacticParametersTrialPartState)currentTrialPart);
				}
				break;
			case NormalizationCorrectionOrConfirmation:
				responseValid = validateAndUpdateNormalizationTrialPart((CorrectOrConfirmNormalizationTrialPartState)currentTrialPart);
				break;
			case SigintSelection:
				SigintSelectionTrialPartState sigintSelectionTrialPart = (SigintSelectionTrialPartState)currentTrialPart;
				responseValid = validateAndUpdateSigintSelectionTrialPart(sigintSelectionTrialPart);
				//Unhighlight location(s) SIGINT selected at				
				if(responseValid && sigintSelectionTrialPart.getSelectedLocations() != null &&
						!sigintSelectionTrialPart.getSelectedLocations().isEmpty()) {
					for(LocationDescriptor location : sigintSelectionTrialPart.getSelectedLocations()) {
						BlueLocationPlacemark blueLocation = currentBlueLocationPlacemarks.get(location.getLocationId());
						if(blueLocation != null) {
							blueLocation.setForegroundColor(Color.WHITE);
							//blueLocation.setHighlighted(false);
						}
					}
				}
				break;
			case RedActionPresentation:
				mapPanel.removeAllActionBanners();
			default:
				break;
			}
		}
		
		if(!responseValid) {
			//Don't advance if the response wasn't valid
			examController.setNavButtonEnabled(ButtonType.Next, true);
			trialPartStartTime = currentTime;
			return;
		}
		if(batchPlotMode) {			
			turnOffBatchPlotMode();
		}
		probabilitySumAndSequenceListener.setListenerActive(false);
		
		//Advance the trial phase counter
		trialPhase++;
		trialPartStartTime = currentTime;			
		
		//Advance the trial counter if we're at the first trial, the current trial doesn't have any trial parts,
		//or we're at the last trial part of the current trial
		boolean advancingAgain = false;
		do {
			if(currentTrial == -1 || (currentTrialState != null && 
					(currentTrialState.getTrialParts() == null || trialPhase >= currentTrialState.getTrialParts().size()))) {
				if(currentTrialState != null) {
					currentTrialState.setTrialTime_ms(currentTime - trialStartTime);
				}
				currentTrial++;
				trialStartTime = currentTime;
				trialPhase = 0;
				if(trialStates == null || currentTrial >= trialStates.size()) {
					//We're at the end of the mission
					//Fire condition completed event and exit
					performCleanup();
					conditionRunning = false;				
					fireConditionEvent(new ConditionEvent(ConditionEvent.CONDITION_COMPLETED, 
							currentTrial, this));				
					return;
				} else {
					//We're at the end of the current trial			
					//Remove the previous blue locations from the map
					mapPanel.removeAllBlueLocations();
					
					//Add the previous outcomes to the list of batch plot previous outcomes
					
					if(currentTrialState != null && currentTrialState.getTrial() != null &&							
							currentBlueLocationPlacemarks != null && !currentBlueLocationPlacemarks.isEmpty()) {
						populatePreviousBatchPlotOutcomes(currentBlueLocationPlacemarks);					
					}
					
					currentTrialState = null;
				}
			}
			
			//Initialize the current trial
			if(currentTrialState == null) {				
				if(currentTrial > 0) {
					//Add the batch plot previous outcomes if starting at a trial > 0
					populatePreviousBatchPlotOutcomes(null);
				}
				initializeCurrentTrial();
			}

			//Get the current trial part
			currentTrialPart = currentTrialState.getTrialParts().get(trialPhase);

			//Advance the trial phase counter again to skip a normalization correction if probabilities are already correctly normalized
			advancingAgain = false;
			if(currentTrialPart.getTrialPartType() == TrialPartType.NormalizationCorrectionOrConfirmation && 
					exam.getNormalizationMode() == NormalizationMode.NormalizeAfter && 
					!ExperimentConstants_Phase2.ALWAYS_CONFIRM_NORMALIZATION) {				
				trialPhase++;				
				advancingAgain = true;
			}
		} while(advancingAgain);
		
		//Fire a trial changed event
		super.setTrial(currentTrial, trialPhase, currentTrialState.getNumTrialParts());
		
		//Show the current trial part
		StringBuilder instructions = createTrialNumberString(currentTrial+1, trialPhase+1, 
				currentTrialState.getNumTrialParts(), currentTrialPart.getTrialPartProbeName());
		if(trialPhase == 0) {
			instructions.append("A new trial has begun. ");
		}
		boolean nextButtonDisabledUntilDataValid = false;
		boolean pauseBeforeNextTrialExtended = false;
		switch(currentTrialPart.getTrialPartType()) {
		case BeginTrial:
			showBeginTrialPart((BeginTrialPartState)currentTrialPart, instructions);
			break;
		case IntPresentation:
			IntPresentationTrialPartState<?> intPresentationTrialPart = (IntPresentationTrialPartState<?>)currentTrialPart; 
			showIntPresentationTrialPart(intPresentationTrialPart, currentTrialState.getTrial().getNumBlueLocations(), 
					intPresentationTrialPart.getProbe() instanceof SigintPresentationProbe, instructions);
			break;
		case SigintSelection:
			nextButtonDisabledUntilDataValid = true;
			SigintSelectionTrialPartState sigintSelectionTrialPart = (SigintSelectionTrialPartState)currentTrialPart;
			showSigintSelectionTrialPart(sigintSelectionTrialPart, instructions);
			//Add listener to highlight location(s) SIGINT is being selected at on map and
			//to enable next button only when all SIGINT selections made
			sigintSelectionListener.setTrialPartState(sigintSelectionTrialPart);
			if(!reportPanelManager.getLocationSelectionPanel().isButtonActionListenerPresent(sigintSelectionListener)) {
				reportPanelManager.getLocationSelectionPanel().addButtonActionListener(sigintSelectionListener);
			}
			break;
		case AttackProbabilityReport: case AttackProbabilityReport_Activity:				
			case AttackProbabilityReport_Activity_Capability_Propensity:
			case AttackProbabilityReport_Capability_Propensity:
			case AttackProbabilityReport_Propensity:
			AttackProbabilityReportTrialPartState attackReportTrialPart = 
				(AttackProbabilityReportTrialPartState)currentTrialPart;			
			pauseBeforeNextTrialExtended = true;
			showAttackProbabilityReportTrialPart(attackReportTrialPart, instructions);			
			//Add listener to update the probability displayed in the location datum panel 
			//and/or determine if the normalization constraints are met
			probabilitySumAndSequenceListener.initializeListener(attackReportTrialPart, 
					reportPanelManager.getAttackProbabilityReportPanel().getProbabilityEntryContainer(), 
					exam.getNormalizationMode());			
			nextButtonDisabledUntilDataValid = exam.getNormalizationMode() == NormalizationMode.NormalizeDuringManual
					|| !probabilitySumAndSequenceListener.isNormalizationConstraintMet();
			if(!reportPanelManager.getAttackProbabilityReportPanel().isSumChangeListenerPresent(
					probabilitySumAndSequenceListener)) {
				reportPanelManager.getAttackProbabilityReportPanel().addSumChangeListener(
						probabilitySumAndSequenceListener);
			}
			probabilitySumAndSequenceListener.setListenerActive(true);
			break;
		case MostLikelyRedTacticSelection:			
			showMostLikleyRedTacticTrialPart((MostLikelyRedTacticTrialPartState)currentTrialPart, instructions);
			nextButtonDisabledUntilDataValid = !batchPlotExclusiveMode;
			if(!reportPanelManager.getRedTacticSelectionPanel().isButtonActionListenerPresent(redTacticSelectionListener)) {
				reportPanelManager.getRedTacticSelectionPanel().addButtonActionListener(redTacticSelectionListener);
			}
			break;
		case RedTacticsProbabilityProbe:			
			RedTacticsProbabilityTrialPartState redTacticsTrialPart = (RedTacticsProbabilityTrialPartState)currentTrialPart;
			showRedTacticsProbabilityProbe(redTacticsTrialPart, instructions);
			//Add listener to determine if the normalization constraints are met
			probabilitySumAndSequenceListener.initializeListener(redTacticsTrialPart.getProbabilityReport(), 
					reportPanelManager.getRedTacticsProbabilityReportPanel().getProbabilityEntryContainer(), 
					ExperimentConstants_Phase2.RED_TACTIC_PROBABILITY_NORMALIZATION_MODE);			
			nextButtonDisabledUntilDataValid = !batchPlotExclusiveMode &&
					(ExperimentConstants_Phase2.RED_TACTIC_PROBABILITY_NORMALIZATION_MODE == NormalizationMode.NormalizeDuringManual ||
					!probabilitySumAndSequenceListener.isNormalizationConstraintMet());
			if(!reportPanelManager.getRedTacticsProbabilityReportPanel().isSumChangeListenerPresent(
					probabilitySumAndSequenceListener)) {
				reportPanelManager.getRedTacticsProbabilityReportPanel().addSumChangeListener(
						probabilitySumAndSequenceListener);
			}
			probabilitySumAndSequenceListener.setListenerActive(true);
			break;
		case RedTacticParametersProbe:
			showRedTacticParametersProbe((RedTacticParametersTrialPartState)currentTrialPart, instructions);
			break;		
		case BlueActionSelectionOrPresentation:
			BlueActionSelectionOrPresentationTrialPartState blueActionTrialPart = 
				(BlueActionSelectionOrPresentationTrialPartState)currentTrialPart;
			nextButtonDisabledUntilDataValid = blueActionTrialPart.isParticipantChoosesBlueActions();
			showBlueActionSelectionOrPresentationTrialPart(blueActionTrialPart, instructions);
			if(blueActionTrialPart.isParticipantChoosesBlueActions()) {
				//Add action listener to show the blue actions as they are selected and enable the Next button when an action
				//is selected at each location.
				blueActionListener.setTrialPartState(blueActionTrialPart);
				if(!reportPanelManager.getActionSelectionPanel().isButtonActionListenerPresent(blueActionListener)) {
					reportPanelManager.getActionSelectionPanel().addButtonActionListener(blueActionListener);
				}
			}
			break;
		case RedActionPresentation:
			showRedActionPresentationTrialPart((RedActionPresentationTrialPartState)currentTrialPart, 
					currentTrialState.getTrial().getBlueActionSelection() != null ? 
							currentTrialState.getTrial().getBlueActionSelection().getBlueActions() : null, 
					instructions);
			break;
		default:
			break;
		}
		
		//Pause before allowing a next button press
		if(!nextButtonDisabledUntilDataValid) {
			if(pauseBeforeNextTrialExtended) {				
				pauseBeforeNextTrial(pauseLength + 1000);
			} else {
				pauseBeforeNextTrial();
			}			
		} else if(debug) {
			pauseBeforeNextTrial();
		}
	}
	
	/**
	 * Initializes the current trial.
	 */
	protected void initializeCurrentTrial() {
		currentTrialState = trialStates.get(currentTrial);
		currentTrialPart = null;
		intPresentationLocations.clearAllIntPresentationAndSelections();									
		
		//Add the current Blue location(s) for the trial to the map
		if(currentTrialState.getTrial().getBlueLocations() != null) {						
			currentBlueLocationPlacemarks = mapPanel.addBlueLocations(currentTrialState.getTrial().getBlueLocations(),
					MissionControllerUtils.createBlueLocationMapDisplayNames(currentTrialState.getTrial().getBlueLocations().size()), 
					currentTrialState.getTrial().getBlueLocations().size() > 1,
					DisplayMode.StandardMode, DisplaySize.StandardSize);
			if(currentBlueLocationPlacemarks != null && !currentBlueLocationPlacemarks.isEmpty()) {
				for(BlueLocationPlacemark blueLocation : currentBlueLocationPlacemarks.values()) {
					blueLocation.setHumintDatum(currentTrialState.getTrial().getHumint());
				}
			}
		} else {
			currentBlueLocationPlacemarks = null;
		}

		//Initialize the location datum panel
		initializeLocationDatumPanel(currentTrialState.getTrial());
	}
	
	/** Create batch plot outcome placemarks for the previous trial or trials (if they are missing). */
	protected void populatePreviousBatchPlotOutcomes(
			Map<String, BlueLocationPlacemark> previousTrialBlueLocations) {
		Collection<DatumType> ints = Arrays.asList(DatumType.OSINT, DatumType.IMINT);		
		if(previousTrialBlueLocations != null && !previousTrialBlueLocations.isEmpty()) {
			//Add the previous trial Blue location placemarks
			List<BlueLocationPlacemark> batchPlotPlacemarks = createBatchPlotPlacemarks(
					previousTrialBlueLocations, ints, currentTrialState.getTrial(), currentTrial);
			batchPlotPreviousOutcomes.add(batchPlotPlacemarks);
				
		} else if(currentTrial > 0 && batchPlotPreviousOutcomes.isEmpty()) {
			//Create the previous Blue location placemarks			
			for(int trialNum = 0; trialNum < currentTrial; trialNum++) {
				T trialState = trialStates.get(trialNum);
				IcarusTestTrial_Phase2 trial = trialState.getTrial();
				Map<String, BlueLocationPlacemark> placemarks = mapPanel.addBlueLocations(trial.getBlueLocations(),
						MissionControllerUtils.createBlueLocationMapDisplayNames(trial.getBlueLocations().size()), 
						trial.getBlueLocations().size() > 1,
						DisplayMode.StandardMode, DisplaySize.StandardSize);
				mapPanel.removeAllBlueLocations();
				List<BlueLocationPlacemark> batchPlotPlacemarks = createBatchPlotPlacemarks(placemarks, ints, 
						trial, trialNum+1);				
				batchPlotPreviousOutcomes.add(batchPlotPlacemarks);												
			}
		}
	}
	
	/** Sets the given placemarks to be batch plot placemarks and adds them to a list that is returned. */
	private List<BlueLocationPlacemark> createBatchPlotPlacemarks(
			Map<String, BlueLocationPlacemark> placemarks, Collection<DatumType> ints, 
			IcarusTestTrial_Phase2 trial, int trialNum) {
		if(placemarks != null && !placemarks.isEmpty()) {
			List<BlueLocationPlacemark> batchPlotPlacemarks = 
					new ArrayList<BlueLocationPlacemark>(placemarks.size());
			boolean selectBlueActions = false;
			List<BlueAction> blueActions = trial.getBlueActionSelection() != null ? trial.getBlueActionSelection().getBlueActions() : null;
			if(blueActions != null && !blueActions.isEmpty()) {
				for(BlueAction blueAction : blueActions) {
					if(blueAction.getAction() == null) {
						selectBlueActions = true;
						break;
					}
				}
			} else {
				selectBlueActions = true;
			}			
			RedAction redAction = trial.getRedActionSelection() != null ? trial.getRedActionSelection().getRedAction() : null;
			if(selectBlueActions || redAction == null || redAction.getAction() == null) {				
				//Select blue actions
				if(selectBlueActions) {
					//System.out.println("Selecting blue actions for trial " + (trialNum+1));					
					CumulativeAndIncrementalRedAttackProbabilities attackProbs = scoreComputer.computeNormativeDataForTrial(
							mission, trial, exam.getSigintReliability(), exam.getBlueBook(), true, false, false, null);
					if(attackProbs != null && attackProbs.getCumulativeProbs() != null) {
						blueActions = blueAgent.selectBlueActions(trial.getBlueLocations(), 
								attackProbs.getCumulativeProbs());
						if(trial.getBlueActionSelection() != null) {
							trial.getBlueActionSelection().setBlueActions(blueActions);
						}
					}
				}				
				//Select the red action
				if(redAction == null || redAction.getAction() == null) {					
					redAction = redAgent.selectRedAction(trial.getBlueLocations(), 
							ScoreComputer_Phase2.getActualRedTacticType(trial, mission).getTacticParameters(), null);
					if(trial.getRedActionSelection() != null) {
						trial.getRedActionSelection().setRedAction(redAction);
					}
				}				
			}			
			if(blueActions != null && !blueActions.isEmpty() && redAction != null) {				
				Iterator<RedBluePayoff> payoffIter = null;
				boolean computePayoffs = false;
				for(BlueLocationPlacemark location : placemarks.values()) {
					if(location.getRedBluePayoff() == null) {
						//Compute the payoffs at each location
						computePayoffs = true;
						RedBluePayoffAtEachLocation payoffs = ScoreComputer_Phase2.computePayoffAtEachLocation(
								trial, mission.getRedScore(), mission.getBlueScore(),
								exam.getPayoffMatrix());
						payoffIter = payoffs.getPayoffAtLocations().iterator();
						break;
					}
				}

				for(BlueAction blueAction : blueActions) {
					BlueLocationPlacemark location = placemarks.get(blueAction.getLocationId());
					if(location != null) {
						batchPlotPlacemarks.add(location);						
						location.setVisible(true);
						if(location.getHumintDatum() == null) {
							location.setHumintDatum(trial.getHumint());
						}
						if(location.getRedAction() == null) {
							location.setRedAction(blueAction.getLocationId().equals(redAction.getLocationId()) ? 
									redAction.getAction() : RedActionType.Do_Not_Attack);
						}
						if(computePayoffs) {
							RedBluePayoff payoff = payoffIter.hasNext() ? payoffIter.next() : null;					
							location.setRedBluePayoff(payoff);
						}
						location.setName(blueActions.size() == 1 ? Integer.toString(trialNum) : blueAction.getLocationId());
						location.setShowName(ExperimentConstants_Phase2.SHOW_TRIAL_NUMBER_FOR_BATCH_PLOTS);
						location.setDisplayMode(DisplayMode.AttackOutcomeMode);
						location.setDisplaySize(DisplaySize.BatchPlotSize);
						location.configureToolTip("Trial: " + Integer.toString(trialNum), ints);
					}
				}
			}
			return batchPlotPlacemarks;
		}
		return null;
	}
	
	protected void updateImintPresentationTrialPart(IntPresentationTrialPartState<?> trialPartState) {
		trialPartState.getProbeWithUpdatedResponseData();
	}
	
	protected boolean validateAndUpdateMostLikelyRedTacticTrialPart(MostLikelyRedTacticTrialPartState trialPartState) {		
		updateBatchPlotTrialPart(trialPartState);
		trialPartState.setMostLikelyRedTactic(reportPanelManager.getRedTacticSelectionPanel().getSelectedRedTactic());
		return trialPartState.getProbeWithUpdatedResponseData().isResponseValid();
	}
	
	protected boolean validateAndUpdateRedTacticsProbabilityTrialPart(RedTacticsProbabilityTrialPartState trialPartState) {
		updateBatchPlotTrialPart(trialPartState);
		IProbabilityEntryContainer probabilityContainer =
				reportPanelManager.getRedTacticsProbabilityReportPanel().getProbabilityEntryContainer();
		trialPartState.setInteractionTimes(probabilityContainer.getInteractionTimes());
		trialPartState.setCurrentSettings(probabilityContainer.getCurrentSettings());
		if(trialPartState.getCurrentNormalizedSettings() == null || 
				trialPartState.getCurrentNormalizedSettings().size() != trialPartState.getCurrentSettings().size()) {
			trialPartState.setCurrentNormalizedSettings(
					ProbabilityUtils.createDefaultInitialProbabilities(trialPartState.getCurrentSettings().size()));
		}
		ProbabilityUtils.normalizePercentProbabilities(trialPartState.getCurrentSettings(), 
				trialPartState.getCurrentNormalizedSettings(),
				trialPartState.getNormalizationConstraint() != null && trialPartState.getNormalizationConstraint().getTargetSum() != null ? 
						trialPartState.getNormalizationConstraint().getTargetSum().intValue() : 100, 
						trialPartState.getNormalizationConstraint() != null ? 
								trialPartState.getNormalizationConstraint().getNormalizationConstraint() : null);
		return trialPartState.getProbeWithUpdatedResponseData().isResponseValid();
	}
	
	protected boolean validateAndUpdateRedTacticParametersTrialPart(RedTacticParametersTrialPartState trialPartState) {
		updateBatchPlotTrialPart(trialPartState);
		trialPartState.setCurrentSettings(reportPanelManager.getRedTacticParametersPanel().getAttackProbabilities());
		if(userRedTactic == null) {
			userRedTactic = new RedTactic();
			userRedTactic.setName("User-Created");
		}
		boolean responseValid = trialPartState.getProbeWithUpdatedResponseData().isResponseValid();
		trialPartState.updateRedTactic(userRedTactic);
		return responseValid;
	}
	
	private void updateBatchPlotTrialPart(AbstractRedTacticsTrialPartState<?> trialPartState) {		
		int numPreviousTrialsSelected = currentTrial - batchPlotState.getBatchPlotMinimumTrial();		
		//trialPartState.setBatchPlotCreated(numPreviousTrialsSelected > 0);
		trialPartState.setBatchPlotCreated(batchPlotState.isBatchPlotCreated());
		trialPartState.setNumPreviousTrialsSelected(numPreviousTrialsSelected);
		trialPartState.setBatchPlotBlueLocationsClicked(batchPlotState.getBlueLocationsClicked());
		trialPartState.setBatchPlotButtonPressSequence(batchPlotState.getButtonPressSequence());
		if(numPreviousTrialsSelected > 0 && mission instanceof Mission_4_5_6) {
			((Mission_4_5_6)mission).setNumBatchPlotsCreated(((Mission_4_5_6)mission).getNumBatchPlotsCreated()+1);
		}		
	}
	
	protected boolean validateAndUpdateAttackProbabilityReportTrialPart(AttackProbabilityReportTrialPartState trialPartState) {
		IProbabilityEntryContainer probabilityContainer = 
				reportPanelManager.getAttackProbabilityReportPanel().getProbabilityEntryContainer(); 
		trialPartState.setInteractionTimes(probabilityContainer.getInteractionTimes());
		trialPartState.setCurrentSettings(probabilityContainer.getCurrentSettings());
		//if(!adjustingNormalizedSettings) {
		//	trialPartState.setPreviousSettings(probabilityContainer.getCurrentSettings());
		//}
		if(trialPartState.getCurrentNormalizedSettings() == null || 
			trialPartState.getCurrentNormalizedSettings().size() != trialPartState.getCurrentSettings().size()) {
			trialPartState.setCurrentNormalizedSettings(
					ProbabilityUtils.createDefaultInitialProbabilities(trialPartState.getCurrentSettings().size()));
		}
		ProbabilityUtils.normalizePercentProbabilities(trialPartState.getCurrentSettings(), 
				trialPartState.getCurrentNormalizedSettings(),
				trialPartState.getNormalizationConstraint() != null && trialPartState.getNormalizationConstraint().getTargetSum() != null ? 
						trialPartState.getNormalizationConstraint().getTargetSum().intValue() : 100, 
				trialPartState.getNormalizationConstraint() != null ? 
						trialPartState.getNormalizationConstraint().getNormalizationConstraint() : null);
		return trialPartState.getProbeWithUpdatedResponseData().isResponseValid();
	}
	
	protected boolean validateAndUpdateNormalizationTrialPart(CorrectOrConfirmNormalizationTrialPartState trialPartState) {
		if(exam.getNormalizationMode() == NormalizationMode.NormalizeAfter) {
			//Normalization is done manually by the participant, confirm that it was done correctly
			IProbabilityEntryContainer probabilityContainer = reportPanelManager.getAttackProbabilityReportPanel().getProbabilityEntryContainer(); 
			List<Integer> currentSettings = probabilityContainer.getCurrentSettings();
			if(trialPartState.getProbabilityReports() != null && currentSettings != null) {
				Iterator<Integer> currentSettingIter = currentSettings.iterator();
				Iterator<ProbabilityReportTrialPartState> probabilityReportIter = trialPartState.getProbabilityReports().iterator();
				while(probabilityReportIter.hasNext() && currentSettingIter.hasNext()) {
					ProbabilityReportTrialPartState currentReport = probabilityReportIter.next();
					if(currentReport.getCurrentSettings() != null && !currentReport.getCurrentSettings().isEmpty()) {
						ListIterator<Integer> reportCurrentSettingIter = currentReport.getCurrentSettings().listIterator();
						while(reportCurrentSettingIter.hasNext() && currentSettingIter.hasNext()) {
							reportCurrentSettingIter.next();
							reportCurrentSettingIter.set(currentSettingIter.next());
						}
					}
				}
			}
			return trialPartState.getProbeWithUpdatedResponseData().isResponseValid();
		}
		return true;
	}
	
	protected boolean validateAndUpdateSigintSelectionTrialPart(SigintSelectionTrialPartState trialPartState) {
		trialPartState.setSelectedLocations(reportPanelManager.getLocationSelectionPanel().getSelectedLocations());
		intPresentationLocations.setSigintSelected(trialPartState.getSelectedLocations(), true);
		return trialPartState.getProbeWithUpdatedResponseData().isResponseValid();
	}
	
	protected boolean validateAndUpdateBlueActionSelectionTrialPart(BlueActionSelectionOrPresentationTrialPartState trialPartState) {
		if(trialPartState.isParticipantChoosesBlueActions() && trialPartState.getProbe().getBlueActions() != null) {
			ActionSelectionPanel actionPanel = reportPanelManager.getActionSelectionPanel();
			for(BlueAction action : trialPartState.getProbe().getBlueActions()) {
				trialPartState.setBlueAction(action.getLocationId(), 
						actionPanel.getActionSelection(action.getLocationId()));	
			}
			return trialPartState.getProbeWithUpdatedResponseData().isResponseValid();
		}
		return true;
	}
	
	protected void showBeginTrialPart(BeginTrialPartState trialPartState, StringBuilder instructions) {
		//Show beginning of trial message
		reportPanelManager.setAllReportComponentsVisible(false);
		instructions.append("OSINT has reported ");
		instructions.append(trialPartState.getNumBlueLocations() > 1 ? 
				Integer.toString(trialPartState.getNumBlueLocations()) + " Blue locations" : "a Blue location");		
		instructions.append(". Click <b>Next</b> to continue.");
		conditionPanel.setInstructionBannerText(instructions.toString());
	}
	
	/**
	 * @param trialPartState
	 * @param totalNumLocations
	 * @param showNoSignalForMissingSigintLocations
	 * @param instructions
	 */
	protected void showIntPresentationTrialPart(IntPresentationTrialPartState<?> trialPartState, int totalNumLocations, 
			boolean showNoSignalForMissingSigintLocations, StringBuilder instructions) {
		//Visit each location and show OSINT, IMINT, HUMINT, or SIGINT	
		reportPanelManager.setAllReportComponentsVisible(false);
		List<String> locationIds = trialPartState.getProbe().getLocationIds();
		List<Integer> locationIndexes = trialPartState.getProbe().getLocationIndexes();		
		if(locationIds != null && !locationIds.isEmpty()) {
			ListIterator<String> locationIdIter = locationIds.listIterator();
			ListIterator<Integer> locationIndexIter = locationIndexes != null ? locationIndexes.listIterator() : null;
			String locationId = locationIdIter.next();
			Integer locationIndex = locationIndexIter != null ? locationIndexIter.next() : 0;
			instructions = MissionControllerUtils.createINTPresentationInstructions(instructions, trialPartState, totalNumLocations, 
					locationIndex, locationId, locationIds.size() > 1);						
			instructions.append(" Click <b>Next</b> to continue.");
			conditionPanel.setInstructionBannerText(instructions.toString());
			while(locationId != null && conditionRunning) {
				IntDatum datum = trialPartState.getIntDatum(locationId);	
				if(datum != null) {
					//Mark that the INT was presented at the location, show the INT banner,
					//display the INT at the location (for OSINT, IMINT, and SIGINT)),
					//and update the location datum list
					showIntAtLocation(locationId, datum, true);					
				}
				locationId = locationIdIter.hasNext() ? locationIdIter.next() : null;
				locationIndex = locationIndexIter != null && locationIndexIter.hasNext() ? locationIndexIter.next() : 0;
			}
		} else {
			instructions.append(" Click <b>Next</b> to continue.");
			conditionPanel.setInstructionBannerText(instructions.toString());
		}
		if(showNoSignalForMissingSigintLocations && currentBlueLocationPlacemarks != null && 
				!currentBlueLocationPlacemarks.isEmpty()) {
			for(String locationId : currentBlueLocationPlacemarks.keySet()) {
				if(!intPresentationLocations.isIntPresented(DatumType.SIGINT, locationId)) {
					locationDatumPanel.setDatumValue(locationId, DatumType.SIGINT, "No Signal");
					locationDatumPanel.setDatumChecked(locationId, DatumType.SIGINT, true);
				}
			}
		}
	}
	
	/**
	 * @param locationId
	 * @param datum
	 * @param showIntBanner
	 */
	protected void showIntAtLocation(String locationId, IntDatum datum, boolean showIntBanner) {
		//Mark that the INT was presented at the location
		intPresentationLocations.setIntPresented(datum.getDatumType(), locationId);
		BlueLocationPlacemark blueLocation = currentBlueLocationPlacemarks != null ? 
				currentBlueLocationPlacemarks.get(locationId) : null;
		if(blueLocation != null) {
			if(showIntBanner) {
				//Show the INT banner at the location
				mapPanel.setIntBannerVisibleAtBlueLocation(blueLocation, true, datum);
			}
			//Display the INT at the location (for OSINT, IMINT, and SIGINT)
			mapPanel.addIntToBlueLocation(blueLocation, datum.getDatumType(), false, false);
		}					
		//Update the location datum list
		locationDatumPanel.setDatumValue(locationId, datum.getDatumType(),
				MissionControllerUtils.createIntDatumValueString(datum));
		locationDatumPanel.setDatumChecked(locationId, datum.getDatumType(), true);
	}
	
	/**
	 * Select the most likely red strategy
	 * 
	 * @param trialPartState
	 * @param instructions
	 */
	protected void showMostLikleyRedTacticTrialPart(MostLikelyRedTacticTrialPartState trialPartState, 
			StringBuilder instructions) {		
		reportPanelManager.setAllReportComponentsVisible(false);
		RedTacticSelectionPanel redTacticSelectionPanel = 
				reportPanelManager.configureRedTacticSelectionPanel(trialPartState.getProbe().getRedTactics());
		reportPanelManager.removeDatumListAtRow(0);
		reportPanelManager.setInteractionComponentAtRow(redTacticSelectionPanel, 0, "Red Style Selection", true, Alignment.West);
		
		configureReportPanelForRedTacticsProbe(0, true, trialPartState, instructions);
		conditionPanel.setInstructionBannerText(instructions.toString());		
		
		//Show the BlueBook
		if(popupInstructions) {
			setBlueBookVisible(true, false);
		}
	}
	
	/**
	 * Report the probability of each Red tactic.
	 * 
	 * @param trialPartState
	 * @param instructions
	 */
	protected void showRedTacticsProbabilityProbe(RedTacticsProbabilityTrialPartState trialPartState,
			StringBuilder instructions) {
		reportPanelManager.setAllReportComponentsVisible(false);
		boolean reviewBlueBook = false;		
		List<RedTacticProbability> probabilities = trialPartState.getProbe().getProbabilities();		
		if(probabilities != null && !probabilities.isEmpty()) {			
			if(trialPartState.getProbe().getDatumList() != null) {
				for(DatumIdentifier datum : trialPartState.getProbe().getDatumList()) {
					if(datum.getDatumType() == DatumType.BlueBook) {
						reviewBlueBook = true;
						break;
					}
				}
			}			
			NormalizationMode normalizationMode = 
					ExperimentConstants_Phase2.RED_TACTIC_PROBABILITY_NORMALIZATION_MODE; 
			ProbabilityReportPanel redTacticsProbabilityPanel = 
					reportPanelManager.configureRedTacticsProbabilityReportPanel(probabilities, null, false, 
							normalizationMode == NormalizationMode.NormalizeDuringManual || 
								normalizationMode == NormalizationMode.NormalizeAfter ||
								normalizationMode == NormalizationMode.NormalizeAfterAndConfirm, 
							normalizationMode == NormalizationMode.NormalizeDuringManual,
							"The sum must be 100%",
							trialPartState.getNormalizationConstraint(), 
							normalizationMode == NormalizationMode.NormalizeDuringInstaneous);
			reportPanelManager.removeDatumListAtRow(0);
			reportPanelManager.setInteractionComponentAtRow(redTacticsProbabilityPanel, 0, "Red Style Report", true);
			
			//Set the current settings to their previous values (if any), or initialize them to the default initial settings
			if(lastRedTacticsProbabilityTrialPart != null && 
					lastRedTacticsProbabilityTrialPart.getCurrentNormalizedSettings() != null &&
					!lastRedTacticsProbabilityTrialPart.getCurrentNormalizedSettings().isEmpty()) {
				redTacticsProbabilityPanel.setCurrentSettings(lastRedTacticsProbabilityTrialPart.getCurrentNormalizedSettings());
			} else {
				redTacticsProbabilityPanel.setCurrentSettings(ProbabilityUtils.createProbabilities(probabilities.size(), 
						ExperimentConstants_Phase2.DEFAULT_INITIAL_PROBABILITY));
			}					
		}		
		
		configureReportPanelForRedTacticsProbe(0, reviewBlueBook, trialPartState, instructions);
		conditionPanel.setInstructionBannerText(instructions.toString());		
		
		//Show the BlueBook
		if(reviewBlueBook && popupInstructions) {
			setBlueBookVisible(true, false);
		}
	}
	
	/**
	 * Report the parameters of the Red tactic
	 * 
	 * @param trialPartState
	 * @param instructions
	 */
	protected void showRedTacticParametersProbe(RedTacticParametersTrialPartState trialPartState,
			StringBuilder instructions) {		
		reportPanelManager.setAllReportComponentsVisible(false);
		RedTacticParametersPanel redTacticParametersPanel = 
				reportPanelManager.configureRedTacticParametersPanel("Probability of Red Attack", 
						trialPartState.getProbe().getRedTacticParameters(), 
						ImintDatum.MIN_U, ImintDatum.MAX_U);
		reportPanelManager.removeDatumListAtRow(0);	
		reportPanelManager.setInteractionComponentAtRow(redTacticParametersPanel, 0, "Red Style Report", true);
		
		//Set the current settings to their previous values (if any), or initialize them to the default initial settings
		if(lastRedTacticsParametersTrialPart != null && 
				lastRedTacticsParametersTrialPart.getCurrentSettings() != null &&
				!lastRedTacticsParametersTrialPart.getCurrentSettings().isEmpty()) {
			redTacticParametersPanel.setAttackProbabilities_Percent(lastRedTacticsParametersTrialPart.getCurrentSettings());
		} else {
			redTacticParametersPanel.setAttackProbabilities_Percent(ProbabilityUtils.createProbabilities(
					trialPartState.getProbe().getRedTacticParameters().getAttackProbabilities() != null ?
							trialPartState.getProbe().getRedTacticParameters().getAttackProbabilities().size() : 4, 
					ExperimentConstants_Phase2.DEFAULT_INITIAL_PROBABILITY));
		}
		
		configureReportPanelForRedTacticsProbe(0, false, trialPartState, instructions);
		conditionPanel.setInstructionBannerText(instructions.toString());				
	}		
	
	/**
	 * Configure the Batch Plot Control Panel and "Review BLUEBOOK" command button for Red tactics probes.
	 * 
	 * @param row
	 * @param reviewBlueBookEnabled
	 * @param trialPartState
	 * @param probeInstructions
	 * @param instructions
	 */
	protected void configureReportPanelForRedTacticsProbe(int row, boolean reviewBlueBookEnabled,
			AbstractRedTacticsTrialPartState<?> trialPartState, StringBuilder instructions) {				
		//Configure the Batch Plot Control Panel
		BatchPlotProbe batchPlotProbe = trialPartState.getProbe().getBatchPlotProbe();
		if(batchPlotProbe != null) {
			reportPanelManager.addBatchPlotControlPanelAtRow(row);
			if(batchPlotProbe.isOptional()) {
				Integer batchPlotsRemaining = mission instanceof Mission_4_5_6 ? 
						((Mission_4_5_6)mission).getNumBatchPlotsRemaining() : null;				
				if(batchPlotsRemaining != null) {
					reportPanelManager.configureBatchPlotControlPanelAtRow(row, batchPlotsRemaining > 0, 
							"Batch Plots Remaining: " + Integer.toString(batchPlotsRemaining));
				} else {
					reportPanelManager.configureBatchPlotControlPanelAtRow(row, true, "");
				}				
				turnOnBatchPlotMode(true, true, true, trialPartState.getProbe(), false);
				instructions.append("Before continuing, please report ");
				instructions.append(trialPartState.getProbeInstructions());
				if(batchPlotsRemaining == null || batchPlotsRemaining > 0) {
					instructions.append(". You may also create a batch plot to review outcomes of previous trials.");
				}
				instructions.append(" Click <b>Next</b> to continue.");
			} else {				
				turnOnBatchPlotMode(true, false, true, trialPartState.getProbe(), true);
				createBatchPlot(false);				
				instructions.delete(0, instructions.length());
				instructions.append("You are now on trial " + Integer.toString(currentTrial+1));
				instructions.append(", and you have an opportunity to make a batch plot.");
				instructions.append(" To do so click the Backward and Forward buttons as often as you wish.");
				instructions.append(" Click <b>Next</b> when you are finished with the batch plot and are ready to report ");
				instructions.append(trialPartState.getProbeInstructions() + ".");
				//instructions.append("to report the probability of each Red style.");				
				/*instructions.append(". Click the Backward and Forward buttons, as often as you wish, " +
						"to create and inspect a batch plot of the attack history.");
				instructions.append(" You may also click on any or all dots, one at a time, to view the P and U values " +
						"associated with the attack locations.");
				instructions.append(" After creating and inspecting a batch plot, ");
				instructions.append(probeInstructions);
				instructions.append(" and click <b>Next</b> to continue.");*/				
			}
		} else {
			reportPanelManager.removeBatchPlotControlPanelAtRow(row);
			turnOnBatchPlotMode(false, false, true, trialPartState.getProbe(), false);
			instructions.append("Before continuing, please report ");
			instructions.append(trialPartState.getProbeInstructions());
			instructions.append(" and click <b>Next</b> to continue.");
		}
		
		//Configure "Review BLUEBOOK" command button for Red tactics probes
		if(reviewBlueBookEnabled) {
			reportPanelManager.setCommandButtonAtRow(row, CommandButtonType.ReviewBlueBook, 
					CommandButtonOrientation.AboveInteractionComponent);
		} else {
			reportPanelManager.removeCommandButtonsAtRow(row);
		}		
		reportPanelManager.setReportComponentVisible(row, true);
	
	}	
	
	protected void turnOffBatchPlotMode() {
		setBatchPlotMode(false, false, false, false, null);
	}
	
	protected void turnOnBatchPlotMode(boolean showBatchPlotLegend, boolean hideMap, 
			boolean hideCurrentBlueLocations, AbstractRedTacticsProbe redTacticsProbe,
			boolean inBatchPlotExclusiveMode) {
		setBatchPlotMode(true, showBatchPlotLegend, hideMap, hideCurrentBlueLocations, 
				redTacticsProbe);
		if(inBatchPlotExclusiveMode) {
			reportPanelManager.setInteractionComponentVisibleAtRow(0, false);	
			batchPlotExclusiveMode = true;
		}
	}
	
	protected void endBatchPlotExclusiveMode(AbstractRedTacticsTrialPartState<?> trialPartState, long batchPlotTime_ms) {	
		if(batchPlotExclusiveMode) {
			//Set the instructions to the probe instructions
			StringBuilder instructions = new StringBuilder("Please report ");
			instructions.append(trialPartState.getProbeInstructions());
			instructions.append(" and click <b>Next</b> to continue.");
			conditionPanel.setInstructionBannerText(instructions.toString());
			//Record the time spent on the batch plot
			trialPartState.setBatchPlotTime_ms(batchPlotTime_ms);
			//Show the interaction component and remove the batch plot controls
			reportPanelManager.removeBatchPlotControlPanelAtRow(0);
			reportPanelManager.setInteractionComponentVisibleAtRow(0, true);
			//Remove any batch plot outcomes displayed
			if(batchPlotPreviousOutcomesDisplayed != null && !batchPlotPreviousOutcomesDisplayed.isEmpty()) {
				mapPanel.removeBlueLocations(batchPlotPreviousOutcomesDisplayed);
				batchPlotPreviousOutcomesDisplayed.clear();
			}
			mapPanel.setBatchPlotLegendItemVisible(false);
			batchPlotExclusiveMode = false;
		} 
	}
	
	/**
	 * @param inBatchPlotMode
	 */
	private void setBatchPlotMode(boolean inBatchPlotMode, boolean showBatchPlotLegend, 
			boolean hideMap, boolean hideCurrentBlueLocations,
			AbstractRedTacticsProbe redTacticsProbe) {		
		if(this.batchPlotMode != inBatchPlotMode) {			
			batchPlotMode = inBatchPlotMode;			
			if(inBatchPlotMode) {	
				//Configure Blue locations to be selectable and add listener for location clicks on the map
				if(!mapPanel.getMapPanel().isMapEventListenerPresent(batchPlotLocationClickedListener)) {
					mapPanel.getMapPanel().addMapEventListener(batchPlotLocationClickedListener);
				}				
				mapPanel.setBlueLocationsLayerSelectable(true);
				if(showBatchPlotLegend) {
					mapPanel.setBatchPlotLegendItemVisible(true);
				}
				mapPanel.setToolTipsEnabled(true);
				if(ExperimentConstants_Phase2.ENABLE_HOVER_TOOL_TIPS_FOR_BATCH_PLOTS) {
					mapPanel.setHoverToolTipsEnabled(true);
				}
				//Hide the location datum panel and map, but configure the map legend to be visible when the map is shown
				conditionPanel.configureTaskScreen(false, false, true);
				if(hideMap) {			
					conditionPanel.setMapPanelVisible(false);
				}
				batchPlotState.initialize(currentTrial, redTacticsProbe);				
				batchPlotPreviousOutcomesDisplayed = new LinkedList<BlueLocationPlacemark>();
				//Hide the Blue location(s) for the current trial
				if(hideCurrentBlueLocations &&
						currentBlueLocationPlacemarks != null && !currentBlueLocationPlacemarks.isEmpty()) {
					for(BlueLocationPlacemark blueLocation : currentBlueLocationPlacemarks.values()) {
						blueLocation.setVisible(false);
					}
					conditionPanel.getMapPanel().redrawMap();
				}
			} else {
				batchPlotExclusiveMode = false;
				mapPanel.getMapPanel().removeMapEventListener(batchPlotLocationClickedListener);
				//Remove any batch plot outcomes displayed
				if(batchPlotPreviousOutcomesDisplayed != null && !batchPlotPreviousOutcomesDisplayed.isEmpty()) {
					mapPanel.removeBlueLocations(batchPlotPreviousOutcomesDisplayed);
					batchPlotPreviousOutcomesDisplayed.clear();
				}
				//Show the Blue locations for the current trial				 
				if(currentBlueLocationPlacemarks != null && !currentBlueLocationPlacemarks.isEmpty()) {
					for(BlueLocationPlacemark blueLocation : currentBlueLocationPlacemarks.values()) {
						blueLocation.setVisible(true);
					}
					conditionPanel.getMapPanel().redrawMap();
				}
				mapPanel.setBlueLocationsLayerSelectable(false);
				mapPanel.setBatchPlotLegendItemVisible(false);
				mapPanel.setToolTipsEnabled(false);
				mapPanel.setHoverToolTipsEnabled(false);
				conditionPanel.configureTaskScreen(true, false, false);
				conditionPanel.setMapPanelVisible(true);				
			}
		}
	}	
	
	/** Create a batch plot. */
	protected void createBatchPlot(boolean updateInstructions) {
		batchPlotState.createBatchPlot();		
		//Update the instructions
		if(updateInstructions) {			
			StringBuilder instructions = new StringBuilder("Click the Backward and Forward buttons, as often as you wish, " +
					"to create and inspect a batch plot of the attack history.");
			instructions.append(" You may also click on any or all dots, one at a time, to view the P and U values " +
					"associated with the attack locations.");
			instructions.append(" After creating and inspecting a batch plot,");
			AbstractRedTacticsProbe redTacticsProbe = batchPlotState.getRedTacticsProbe();
			if(redTacticsProbe != null) {
				if(redTacticsProbe instanceof MostLikelyRedTacticProbe) {
					instructions.append(" please report Red's most likely style and");
				} else if(redTacticsProbe instanceof RedTacticsProbabilityReportProbe) {
					instructions.append(" please report the probability of each Red style and");
				} else if(redTacticsProbe instanceof RedTacticParametersProbe) {
					instructions.append(" please report the probabilities that define Red's style and");
				} 
			}
			instructions.append(" click <b>Next</b> to continue.");
			//instructions.append(" After reporting these probabilities, click <b>Next</b> to continue.");
			conditionPanel.setInstructionBannerText(instructions.toString());
		}
		
		//Show the map with the AOI and no Blue locations yet		
		conditionPanel.setMapPanelVisible(true);
		reportPanelManager.configureBatchPlotControlPanelAtRow(0, true, 
				batchPlotState.canGoBackward(), 
				ExperimentConstants_Phase2.SHOW_FORWARD_BUTTON_FOR_BATCH_PLOTS, 
				false, batchPlotState.createShowingTrialsMessage());
	}
	
	/** Display the next previous outcome in a batch plot. */
	protected void displayPreviousOutcome() {
		if(batchPlotState.canGoBackward()) {
			if(!batchPlotState.isBatchPlotGoingBackward()) {
				//Remove all outcomes
				mapPanel.removeBlueLocations(batchPlotPreviousOutcomesDisplayed);
				batchPlotPreviousOutcomesDisplayed.clear();
			}		
			if(batchPlotState.goBackward()) {
				//Show the next previous outcome(s)
				List<BlueLocationPlacemark> placemarks = batchPlotPreviousOutcomes.get(batchPlotState.getBatchPlotCurrentTrial());
				if(placemarks != null && !placemarks.isEmpty()) {
					batchPlotPreviousOutcomesDisplayed.addAll(placemarks);
					mapPanel.addBlueLocations(placemarks, true);
				}
			}
			reportPanelManager.configureBatchPlotControlPanelAtRow(0, batchPlotState.canGoBackward(), 
					batchPlotState.canGoForward() && currentTrial > 1, 
					batchPlotState.createShowingTrialsMessage());
		}
	}
	
	/** Display the next outcome in a batch plot. */
	protected void displayNextOutcome() {
		if(batchPlotState.canGoForward()) {
			if(batchPlotState.isBatchPlotGoingBackward()) {
				//Remove all outcomes
				mapPanel.removeBlueLocations(batchPlotPreviousOutcomesDisplayed);
				batchPlotPreviousOutcomesDisplayed.clear();
			}
			if(batchPlotState.goForward()) {
				//Show the next outcome(s)
				List<BlueLocationPlacemark> placemarks = batchPlotPreviousOutcomes.get(batchPlotState.getBatchPlotCurrentTrial());
				if(placemarks != null && !placemarks.isEmpty()) {
					batchPlotPreviousOutcomesDisplayed.addAll(placemarks);
					mapPanel.addBlueLocations(placemarks, true);
				}
			}
			reportPanelManager.configureBatchPlotControlPanelAtRow(0, batchPlotState.canGoBackward(), 
					batchPlotState.canGoForward() && currentTrial > 1, 
					batchPlotState.createShowingTrialsMessage());
		}
	}
	
	protected void showSigintSelectionTrialPart(SigintSelectionTrialPartState trialPartState, StringBuilder instructions) {	
		//Select one or more locations to obtain SIGINT at
		reportPanelManager.setAllReportComponentsVisible(false);
		if(trialPartState.getProbe().getNumSigintSelections() == 1) {
			instructions.append("Please choose one location to obtain SIGINT at and click <b>Next</b> to continue.");
		} else {
			instructions.append("Please choose the locations to obtain SIGINT at and click <b>Next</b> to continue.");
		}		
		conditionPanel.setInstructionBannerText(instructions.toString());
		LocationSelectionPanel locationSelectionPanel = 
				reportPanelManager.configureLocationSelectionPanel(trialPartState.getProbe().getLocationIds(), 
						trialPartState.getProbe().getNumSigintSelections());
		reportPanelManager.removeDatumListAtRow(0);
		reportPanelManager.removeBatchPlotControlPanelAtRow(0);
		reportPanelManager.setInteractionComponentAtRow(locationSelectionPanel, 0, "SIGINT Selection", true, Alignment.West);
		reportPanelManager.setCommandButtonAtRow(0, CommandButtonType.ReviewSigintReliability, 
				CommandButtonOrientation.AboveInteractionComponent);
		reportPanelManager.setReportComponentVisible(0, true);
		//Show the SIGINT reliabilities
		if(popupInstructions) {
			setSigintReliabilityVisible(true);
		}
	}
	
	/**
	 * @param trialPartState
	 * @param instructions
	 */
	protected void showAttackProbabilityReportTrialPart(AttackProbabilityReportTrialPartState trialPartState, 
			StringBuilder instructions) {
		//Report the probability of Red attack at each location
		reportPanelManager.setAllReportComponentsVisible(false);
		List<AttackProbability> probabilities = trialPartState.getProbe().getProbabilities();
		if(probabilities != null && !probabilities.isEmpty()) {			
			//Show the attack probability report probe(s)
			String probabilityTitle = DatumListItemType.getDatumListItemType(
					trialPartState.getAttackProbabilityReportDatumType()).getDatumListItem().getName();
			AttackProbability probability = probabilities.size() == 1 ? probabilities.get(0) : null;
			instructions = MissionControllerUtils.createProbabilityReportInstructions(instructions, trialPartState, 
					probabilityTitle, probabilities.size(), 
					probability != null ? probability.getLocationIndex() : 0, 
							probability != null ? probability.getLocationId() : null, probabilities.size() > 1);
			instructions.append(" Click <b>Next</b> to continue.");
			conditionPanel.setInstructionBannerText(instructions.toString());
			showAttackProbabilityReport(trialPartState, probabilities, trialPartState.getProbe().getDatumList(), 
					trialPartState.getTrialPartProbeName(), probabilityTitle, probabilities.size() > 1);
			for(AttackProbability prob : probabilities) {
				locationDatumPanel.setDatumChecked(prob.getLocationId(), 
						trialPartState.getAttackProbabilityReportDatumType(), true);
			}
		} else {
			instructions.append(" Click <b>Next</b> to continue.");
			conditionPanel.setInstructionBannerText(instructions.toString());
		}
	}
	
	protected void showAttackProbabilityReport(AttackProbabilityReportTrialPartState trialPartState, 
			List<AttackProbability> probabilities, List<DatumIdentifier> datum, String reportTitle,
			String probabilityEntryPanelTitle, boolean showSum) {		
		reportPanelManager.configureAttackProbabilityReportPanel(probabilities, 
				probabilityEntryPanelTitle, true, showSum,
				exam.getNormalizationMode() == NormalizationMode.NormalizeDuringManual,
				"<html>The sum must be &lt= 100% since<br>Red can only attack one location.</html>",
				trialPartState.getNormalizationConstraint());				
		reportPanelManager.getAttackProbabilityReportPanel().setCurrentSettings(trialPartState.getCurrentSettings());			
		//Be sure all relevant INTs and INT values are shown if they weren't presented previously (for missions 4-6)
		boolean considerSigint = false;
		for(DatumIdentifier datumItem : datum) {
			boolean participantSelectsSigint = currentTrialState.getTrial().getSigintSelectionProbe() != null;			
			if(DatumType.isIntDatumType(datumItem.getDatumType()) && datumItem.getLocationId() != null) {
				if(datumItem.getDatumType() == DatumType.SIGINT) considerSigint = true;
				if((datumItem.getDatumType() != DatumType.SIGINT || (!participantSelectsSigint || 
						intPresentationLocations.isSigintSelected(datumItem.getLocationId())))
					&& !intPresentationLocations.isIntPresented(datumItem.getDatumType(), datumItem.getLocationId())) {
					intPresentationLocations.setIntPresented(datumItem.getDatumType(), datumItem.getLocationId());					
					showIntAtLocation(datumItem.getLocationId(), 
							currentTrialState.getTrial().getInt(datumItem.getDatumType(),
									datumItem.getLocationId()), false);
				}
			}			
		}
		if(considerSigint && currentBlueLocationPlacemarks != null && !currentBlueLocationPlacemarks.isEmpty()) {
			for(String locationId : currentBlueLocationPlacemarks.keySet()) {
				if(!intPresentationLocations.isIntPresented(DatumType.SIGINT, locationId)) {
					locationDatumPanel.setDatumValue(locationId, DatumType.SIGINT, "No Signal");
					locationDatumPanel.setDatumChecked(locationId, DatumType.SIGINT, true);
				}
			}
		}
		//Configure the datum list and command buttons		
		reportPanelManager.removeBatchPlotControlPanelAtRow(0);
		ProbabilityReportDatumList datumList = MissionControllerUtils.createAttackProbabilityReportDatumList(
				currentTrialState.getTrial(), datum, intPresentationLocations, 
				probabilities != null ? probabilities.size() : 0);
		if(datumList != null) {			
			reportPanelManager.setDatumListAtRow(0, datumList.getDatumItems(), datumList.getDatumValues());
			if(datumList.isConsiderBlueBook()) {
				if(datumList.isConsiderSigintReliability()) {
					//Show both the review BLUEBOOK and review SIGINT reliabilities buttons
					reportPanelManager.setCommandButtonsAtRow(0, 
							Arrays.asList(CommandButtonType.ReviewBlueBook, CommandButtonType.ReviewSigintReliability), 
							1, CommandButtonOrientation.AboveInteractionComponent);
				} else {
					//Just show the review BLUEBOOK button
					reportPanelManager.setCommandButtonAtRow(0, CommandButtonType.ReviewBlueBook, 
							CommandButtonOrientation.AboveInteractionComponent);
				}				
				//Show the BlueBook
				if(popupInstructions) {
					setBlueBookVisible(true, ExperimentConstants_Phase2.HIGHLIGHT_BLUEBOOK_CELLS);
				}
			} else if(datumList.isConsiderSigintReliability()) {
				//Show the review SIGINT reliabilities button
				reportPanelManager.setCommandButtonAtRow(0, CommandButtonType.ReviewSigintReliability, 
						CommandButtonOrientation.AboveInteractionComponent);
				//Show the SIGINT Reliability panel
				if(popupInstructions) {
					setSigintReliabilityVisible(true);
				}
			} else {
				reportPanelManager.removeCommandButtonsAtRow(0);
			}
		} else {
			reportPanelManager.removeDatumListAtRow(0);
			reportPanelManager.removeCommandButtonsAtRow(0);
		}
		reportPanelManager.setInteractionComponentAtRow(reportPanelManager.getAttackProbabilityReportPanel(), 
				0, reportTitle, true);
		reportPanelManager.setReportComponentVisible(0, true);
	}	
	
	protected void showBlueActionSelectionOrPresentationTrialPart(BlueActionSelectionOrPresentationTrialPartState trialPartState,
			StringBuilder instructions) {
		//Show the Blue action or have the participant select the Blue action at each location
		reportPanelManager.setAllReportComponentsVisible(false);
		int numLocations = trialPartState.getProbe().getBlueActions() != null ? trialPartState.getProbe().getBlueActions().size() : 0;
		if(trialPartState.isParticipantChoosesBlueActions()) {		
			instructions.append("Please choose a Blue action");
			instructions.append(numLocations > 1 ? " at each location." : ".");
			instructions.append(" Your actions should be chosen to minimize Blue\'s expected losses.");		
			instructions.append(" Click <b>Next</b> to continue.");
		} else {
			instructions = MissionControllerUtils.createBlueActionSelectionsText(instructions, trialPartState);
			instructions.append(" Click <b>Next</b> to continue.");
		}		
		conditionPanel.setInstructionBannerText(instructions.toString());
		if(trialPartState.isParticipantChoosesBlueActions()) {
			//Have the participant select the Blue action at each location
			ActionSelectionPanel actionPanel = reportPanelManager.configureActionSelectionPanel(
					trialPartState.getProbe().getBlueActions(), false);
			reportPanelManager.removeDatumListAtRow(0);
			reportPanelManager.removeBatchPlotControlPanelAtRow(0);
			reportPanelManager.setInteractionComponentAtRow(actionPanel, 0, "Blue Action Selection", true, Alignment.West);
			//Show the Review Payoff Matrix button
			reportPanelManager.setCommandButtonAtRow(0, CommandButtonType.ReviewPayoffMatrix, 
					CommandButtonOrientation.AboveInteractionComponent);
			reportPanelManager.setReportComponentVisible(0, true);			
			//Show the Consider datum list
			ProbabilityReportDatumList datumList = MissionControllerUtils.createAttackProbabilityReportDatumList(
					currentTrialState.getTrial(), trialPartState.getProbe().getDatumList(),
					intPresentationLocations, trialPartState.getNumLocations());
			if(datumList != null) {
				reportPanelManager.setDatumListAtRow(0, datumList.getDatumItems(), datumList.getDatumValues());
			} else {
				reportPanelManager.removeDatumListAtRow(0);
			}			
			//Show the Payoff matrix
			if(popupInstructions) {
				setPayoffMatrixVisible(true);
			}
		} else {
			//Show the Blue action at each location
			if(numLocations > 0) {
				for(BlueAction action : trialPartState.getProbe().getBlueActions()) {
					BlueLocationPlacemark blueLocation = currentBlueLocationPlacemarks.get(action.getLocationId());
					if(blueLocation != null) {
						blueLocation.setBlueAction(action.getAction());
						mapPanel.setActionBannerVisibleAtBlueLocation(blueLocation, true, true, false, false, false);
					}
				}
			}
		}
	}
	
	protected void showRedActionPresentationTrialPart(RedActionPresentationTrialPartState trialPartState,
			List<BlueAction> blueActions, StringBuilder instructions) {
		//Show the red action and the payoff outcome
		reportPanelManager.setAllReportComponentsVisible(false);
		//Red has chosen to attack at the location. Since you chose to divert forces from the location, you defeated Red and have been awarded 3 points! Click Next to continue.
		RedAction redAction = trialPartState.getRedAction();
		if(redAction != null && blueActions != null && !blueActions.isEmpty()) {			
			//Compute the payoff at each location, show the payoff at each location, and update the total blue and red scores
			RedBluePayoffAtEachLocation payoffs = ScoreComputer_Phase2.computePayoffAtEachLocation(
					currentTrialState.getTrial(), mission.getRedScore(), mission.getBlueScore(),
					exam.getPayoffMatrix());
			mission.setRedScore(payoffs.getRedScore());
			mission.setBlueScore(payoffs.getBlueScore());
			conditionPanel.setScores(payoffs.getRedPointsGained(), payoffs.getBluePointsGained(),
				mission.getRedScore(), mission.getBlueScore());
			int numLocations = blueActions.size();
			Iterator<BlueAction> blueActionIter = blueActions.iterator();
			Iterator<RedBluePayoff> payoffIter = payoffs.getPayoffAtLocations().iterator();
			int i = 0;
			Integer showdownLocation = null;
			instructions.append("Red ");
			while(blueActionIter.hasNext()) {
				BlueAction blueAction = blueActionIter.next();
				//Show the Blue and Red action at the location			
				BlueLocationPlacemark blueLocation = currentBlueLocationPlacemarks.get(blueAction.getLocationId());
				if(blueLocation != null) {
					RedBluePayoff payoff = payoffIter.hasNext() ? payoffIter.next() : null;					
					blueLocation.setRedBluePayoff(payoff);
					blueLocation.setRedAction(blueAction.getLocationId().equals(redAction.getLocationId()) ? 
							redAction.getAction() : RedActionType.Do_Not_Attack);
					if(payoff != null && payoff.isShowDown()) {
						showdownLocation = i;
					}
					mapPanel.setActionBannerVisibleAtBlueLocation(blueLocation, true, true, true, true, true);
				}				
				//Update the instructions text
				if(i > 0) {
					instructions.append(i == numLocations - 1 ? " and " : ", ");
				}
				if(redAction.getLocationId() != null && redAction.getLocationId().equals(blueAction.getLocationId())) {
					instructions.append(redAction.getAction() != null && redAction.getAction() == RedActionType.Attack ?
							"<b>attacked</b>" : "<b>did not attack</b>");
				} else {
					instructions.append("<b>did not attack</b>");
				}
				if(numLocations > 1) {
					instructions.append(" at ");
					instructions.append(MissionControllerUtils.createBlueLocationProbabilityReportDisplayName(i, numLocations));
				}
				i++;	
			}	
			//Update the remainder of the instructions text
			if(showdownLocation != null) {
				if(numLocations > 1) {
					instructions.append(", and a showdown occurred at ");
					instructions.append(MissionControllerUtils.createBlueLocationProbabilityReportDisplayName(showdownLocation, numLocations));
				} else {
					instructions.append(", and a showdown occurred");
				}
				//TODO: Display showdown winner				
			}
			instructions.append(".");
			if(payoffs.getBluePointsGained() == 0 && payoffs.getRedPointsGained() == 0) {
				instructions.append(" Neither Blue nor Red won any points. ");
			} else {
				instructions.append(payoffs.getBluePointsGained() == 0 ? " Blue did not win any points" :
					payoffs.getBluePointsGained() > 0 ? " Blue won<b> " : " Blue lost<b> ");				
				if(payoffs.getBluePointsGained() != 0) {						
					instructions.append(conditionPanel.formatScore(
							payoffs.getBluePointsGained() >= 0 ? payoffs.getBluePointsGained() : -payoffs.getBluePointsGained()));
					instructions.append(Math.abs(payoffs.getBluePointsGained()) > 1 ? " points</b>" : " point</b>");
				}
				instructions.append(payoffs.getRedPointsGained() == 0 ? " and Red did not win any points" :
					payoffs.getRedPointsGained() > 0 ? " and Red won<b> " :
						" and Red lost<b> ");
				if(payoffs.getRedPointsGained() != 0) {						
					instructions.append(conditionPanel.formatScore(
						payoffs.getRedPointsGained() >= 0 ? payoffs.getRedPointsGained() : -payoffs.getRedPointsGained()));
					instructions.append(Math.abs(payoffs.getRedPointsGained()) > 1 ? " points</b>" : " point</b>");
				}
				instructions.append(". ");
			}
			instructions.append("Click <b>Next</b> to continue.");
		} else {
			instructions.append("Click <b>Next</b> to continue.");
		}
		conditionPanel.setInstructionBannerText(instructions.toString());
	}	
	
	
	@Override
	public void subjectActionPerformed(SubjectActionEvent event) {
		try {
			if(event.eventType == SubjectActionEvent.NEXT_BUTTON_PRESSED) {
				nextButtonPressed = true;
				//Advance to the next trial or trial part in the mission
				nextTrialOrTrialPart();
			}	
			else if(event.eventType == SubjectActionEvent.BACK_BUTTON_PRESSED) {
				backButtonPressed = true;
				//Go to the previous trial part.  This is only allowed to adjust probabilities.			
				if(currentTrialPart instanceof CorrectOrConfirmNormalizationTrialPartState) {
					trialPhase -= 2;
					nextTrialOrTrialPart();
				}
			}
		} catch(Exception ex) {
			handleException(ex, true);
		}
	}		
	
	/**
	 * Show the BlueBook for the current Mission in the external instructions window.
	 */
	protected void setBlueBookVisible(boolean visible, boolean highlightCells) {		
		if(visible) {
			if(exam.getBlueBook() != null) {			
				List<RedTactic> redTactics = null;
				boolean attackProbabailitiesUnknown = false;
				boolean redTacticParametersChanged = false;
				if(mission.getMissionType() == MissionType.Mission_6) {					
					redTactics = Collections.singletonList(userRedTactic != null ? userRedTactic : new RedTactic());
					attackProbabailitiesUnknown = userRedTactic == null;
					redTacticParametersChanged = true;
				} else {
					redTactics = exam.getBlueBook().getRedTaticsForMission(mission.getMissionType());					
				}
				if(redTactics != null && !redTactics.isEmpty()) {
					String id = "BLUEBOOK_" + mission.getId() + "_" + exam.getId(); 
					if(redTacticParametersChanged || blueBookPanel == null || !id.equals(blueBookPanel.getComponentId())) {
						if(mission.getMissionType().getLocationsPerTrial() > 1 && redTactics.size() == 1) {
							//Handle the BLUEBOOK for missions with > 1 location per trial (e.g., Mission 3)
							RedTactic redTactic = redTactics.get(0);
							List<RedTactic> redTacticsAtEachLocation = new ArrayList<RedTactic>(
									mission.getMissionType().getLocationsPerTrial());
							for(int i=0; i<mission.getMissionType().getLocationsPerTrial(); i++) {
								redTacticsAtEachLocation.add(new RedTactic(
										"Location " + (i+1), redTactic.getTacticType(), 
										redTactic.getTacticParameters()));
							}
							blueBookPanel = QuadChartPanelFactory.createBlueBookPanel(
									redTacticsAtEachLocation, attackProbabailitiesUnknown);
						} else {
							//Handle the BLUEBOOK for missions with 1 location per trial
							blueBookPanel = QuadChartPanelFactory.createBlueBookPanel(
									redTactics, attackProbabailitiesUnknown);
						}
						blueBookPanel.setComponentId(id);
					}
					if(highlightCells && currentTrialState.getTrial().getBlueLocations() != null && 
							!currentTrialState.getTrial().getBlueLocations().isEmpty()) {
						if(currentTrialState.getTrial().getBlueLocations().size() > 1) {
							blueBookPanel.highlightCells(currentTrialState.getTrial().getBlueLocations());
						} else {							
							blueBookPanel.highlightCells(currentTrialState.getTrial().getBlueLocations().get(0));
						}
					} else {
						blueBookPanel.unhighlightAllCells();
					}
					if(!id.equals(externalInstructionsPanel.getInstructionsId())) {						
						externalInstructionsPanel.setInstructionsId(id);
						externalInstructionsPanel.setInstructionsPages("BLUEBOOK", 
								Collections.singletonList(new InstructionsPage(blueBookPanel)));
					}
					setExternalInstructionsVisible(true, "BLUEBOOK",
							ImageManager_Phase2.getImage(ImageManager_Phase2.BLUEBOOK_ICON));
				}
				/*InstructionsPage tacticInstructions = exam.getBlueBook().getRedTacticsInstructionsForMission(
						mission.getMissionType());
				if(tacticInstructions != null) {
					String id = "BLUEBOOK_" + mission.getId() + "_" + exam.getId();
					if(!id.equals(externalInstructionsPanel.getInstructionsId())) {						
						externalInstructionsPanel.setInstructionsId(id);
						externalInstructionsPanel.setInstructionsPages("BLUEBOOK",
								Collections.singletonList(tacticInstructions));
					}
					setExternalInstructionsVisible(true, "BLUEBOOK",
							ImageManager_Phase2.getImage(ImageManager_Phase2.BLUEBOOK_ICON));
				}*/
			} 
		} else {
			setExternalInstructionsVisible(false, null, null);
		}
	}
	
	/**
	 * Show the SIGINT reliability table in the external instructions window. 
	 */
	protected void setSigintReliabilityVisible(boolean visible) {
		if(visible) {
			if(exam.getSigintReliability() != null) { 
					//&& exam.getSigintReliability().getSigintReliabilityInstructions() != null) {				
				String id = "SIGINT_" + exam.getId(); 
				if(sigintReliabilityPanel == null || !id.equals(sigintReliabilityPanel.getComponentId())) {
					sigintReliabilityPanel = QuadChartPanelFactory.createSigintReliabilityPanel(
							exam.getSigintReliability());
					sigintReliabilityPanel.setComponentId(id);
				}
				if(!id.equals(externalInstructionsPanel.getInstructionsId())) {						
					externalInstructionsPanel.setInstructionsId(id);
					externalInstructionsPanel.setInstructionsPages("Sigint Reliability", 
							Collections.singletonList(new InstructionsPage(sigintReliabilityPanel)));
				}
				setExternalInstructionsVisible(true, "Sigint Reliability",
						ImageManager_Phase2.getImage(ImageManager_Phase2.SIGINT_RELIABILITY_ICON));
				/*String id = "SIGINT_" + exam.getId();
				if(!id.equals(externalInstructionsPanel.getInstructionsId())) {
					externalInstructionsPanel.setInstructionsId(id);
					externalInstructionsPanel.setInstructionsPages("Sigint Reliability",
						Collections.singletonList(exam.getSigintReliability().getSigintReliabilityInstructions()));
				}
				setExternalInstructionsVisible(true, "Sigint Reliability",
						ImageManager_Phase2.getImage(ImageManager_Phase2.SIGINT_RELIABILITY_ICON));*/
			}
		} else {
			setExternalInstructionsVisible(false, null, null);
		}
	}
	
	/**
	 * Show the Payoff matrix in the external instructions window. 
	 */
	protected void setPayoffMatrixVisible(boolean visible) {
		if(visible) {
			if(exam.getPayoffMatrix() != null) { 
					//&& exam.getPayoffMatrix().getPayoffMatrixInstructions() != null) {
				String id = "PAYOFF_" + exam.getId(); 
				if(payoffMatrixPanel == null || !id.equals(payoffMatrixPanel.getComponentId())) {
					payoffMatrixPanel = QuadChartPanelFactory.createPayoffMatrixPanel(exam.getPayoffMatrix());
					payoffMatrixPanel.setComponentId(id);
				}
				if(!id.equals(externalInstructionsPanel.getInstructionsId())) {						
					externalInstructionsPanel.setInstructionsId(id);
					externalInstructionsPanel.setInstructionsPages("Payoff Matrix", 
							Collections.singletonList(new InstructionsPage(payoffMatrixPanel)));
				}
				setExternalInstructionsVisible(true, "Payoff Matrix",
						ImageManager_Phase2.getImage(ImageManager_Phase2.PAYOFF_MATRIX_ICON));
				/*String id = "PAYOFF_" + exam.getId();
				if(!id.equals(externalInstructionsPanel.getInstructionsId())) {
					externalInstructionsPanel.setInstructionsId(id);
					externalInstructionsPanel.setInstructionsPages("Payoff Matrix",
						Collections.singletonList(exam.getPayoffMatrix().getPayoffMatrixInstructions()));
				}
				setExternalInstructionsVisible(true, "Payoff Matrix",
						ImageManager_Phase2.getImage(ImageManager_Phase2.PAYOFF_MATRIX_ICON));*/
			}
		} else {
			setExternalInstructionsVisible(false, null, null);
		}
	}
	
	protected boolean isExternalInstructionsVisible() {
		return externalInstructionsWindow != null && externalInstructionsWindow.isVisible();
	}
	
	protected void setExternalInstructionsVisible(boolean visible, String windowTitle, Image windowIcon) {		
		if(externalInstructionsWindow == null && visible) {
			externalInstructionsWindow = new JFrame();
			externalInstructionsWindow.setResizable(true);
			externalInstructionsWindow.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			externalInstructionsWindow.getContentPane().add(externalInstructionsPanel.getComponent());
			externalInstructionsWindow.setAlwaysOnTop(true);
			externalInstructionsWindow.pack();	
			externalInstructionsWindow.setMinimumSize(externalInstructionsWindow.getSize());
			if(examController != null && examController.getParentWindow() != null) {
				externalInstructionsWindow.setLocationRelativeTo(examController.getParentWindow());
				int xLocation = externalInstructionsWindow.getLocation().x + 
						examController.getParentWindow().getWidth()/2 +	externalInstructionsWindow.getWidth()/2;
				int screenWidth = externalInstructionsWindow.getToolkit().getScreenSize().width;
				if(xLocation + externalInstructionsWindow.getWidth() > screenWidth) {
					//Move the external instructions window over so it doesn't go off-screen, 
					//but go not obscure the response panel
					/*xLocation = xLocation - externalInstructionsWindow.getWidth() - 
							conditionPanel.getReportPanel().getWidth() - 12;
					externalInstructionsWindow.setLocation(xLocation, 
							examController.getParentWindow().getLocation().y + 
							examController.getParentWindow().getHeight() - externalInstructionsWindow.getHeight());*/
					xLocation -= xLocation + externalInstructionsWindow.getWidth() - screenWidth;
					externalInstructionsWindow.setLocation(xLocation, externalInstructionsWindow.getLocation().y);
				} else {		
					externalInstructionsWindow.setLocation(xLocation, externalInstructionsWindow.getLocation().y);
				}
			}			
		}
		if(externalInstructionsWindow != null) {
			externalInstructionsWindow.setTitle(windowTitle != null ? windowTitle : "");
			if(windowIcon != null) {
				externalInstructionsWindow.setIconImage(windowIcon);
			}
			if(visible != externalInstructionsWindow.isVisible()) {
				externalInstructionsWindow.setVisible(visible);
			} else if(visible) {
				externalInstructionsWindow.toFront();
			}
		}
	}
	
	/**
	 * @param trial
	 */
	protected void initializeLocationDatumPanel(IcarusTestTrial_Phase2 trial) {
		if(trial != null && locationDatumPanel != null) {
			List<String> locationNames = new LinkedList<String>();
			List<String> locationIds = new LinkedList<String>();
			List<DatumListItem> datumItems = new LinkedList<DatumListItem>();
			if(trial.getBlueLocations() != null && !trial.getBlueLocations().isEmpty()) {
				if(trial.getBlueLocations().size() == 1) {
					locationNames.add("Location");
					locationIds.add(trial.getBlueLocations().iterator().next().getId());
				} else {
					int i = 1;
					for(BlueLocation location : trial.getBlueLocations()) {
						locationNames.add("Location " + Integer.toString(i));
						locationIds.add(location.getId());
						i++;
					}
				}
			}			
			if(trial.getOsintPresentation() != null || 
					trial.getAttackPropensityProbe_Pp() != null ||
					trial.getAttackProbabilityProbe_Ppc() != null ||
					trial.getAttackProbabilityProbe_Ptpc() != null) {
				datumItems.add(DatumListItemType.OSINT.getDatumListItem());
			}
			if(trial.getImintPresentation() != null || 
					trial.getAttackPropensityProbe_Pp() != null ||
					trial.getAttackProbabilityProbe_Ppc() != null ||
					trial.getAttackProbabilityProbe_Ptpc() != null) {
				datumItems.add(DatumListItemType.IMINT.getDatumListItem());
			}
			if(trial.getAttackPropensityProbe_Pp() != null) {
				datumItems.add(DatumListItemType.P_PROPENSITY.getDatumListItem());
			}
			if(trial.getHumintPresentation() != null || 
					trial.getAttackProbabilityProbe_Ppc() != null ||
					trial.getAttackProbabilityProbe_Ptpc() != null) {
				datumItems.add(DatumListItemType.HUMINT.getDatumListItem());
			}
			if(trial.getAttackProbabilityProbe_Ppc() != null) {
				datumItems.add(DatumListItemType.P_CAPABILITY_PROPENSITY.getDatumListItem());
			}
			if(trial.getSigintPresentation() != null || 
					trial.getSigintSelectionProbe() != null ||
					trial.getAttackProbabilityProbe_Ptpc() != null) {
				datumItems.add(DatumListItemType.SIGINT.getDatumListItem());
			}
			if(trial.getAttackProbabilityProbe_Pt() != null) {
				datumItems.add(DatumListItemType.P_ACTIVITY.getDatumListItem());
			}
			if(trial.getAttackProbabilityProbe_Ptpc() != null) {
				datumItems.add(DatumListItemType.P_ACTIVITY_CAPABILITY_PROPENSITY.getDatumListItem());
			}
			locationDatumPanel.setLocations(locationNames, locationIds, datumItems);
		}
	}
	
	/** Contains settings for the state of the current batch plot when creating a batch plot. */
	public static class BatchPlotState {
		/** The current trial */
		private int currentTrial;
		
		/** The Red tactics probe for the trial */
		protected AbstractRedTacticsProbe redTacticsProbe;
		
		/** Whether a batch plot was created */
		private boolean batchPlotCreated;
		
		/** When creating a batch plot, whether the participant is moving backward. If false, the participant
		 * is moving forward **/
		private boolean batchPlotGoingBackward;	
		
		/** When creating a batch plot, the current trial displayed */
		private int batchPlotCurrentTrial;
		
		/** When creating a batch plot, the minimum trial reviewed (farthest the subject went back) */
		private int batchPlotMinimumTrial;
		
		/** When creating a batch plot, the first trial displayed after going backward */
		private int batchPlotFirstPreviousTrial;
		
		/** When creating a batch plot, the first trial displayed after going forward */
		private int batchPlotFirstNextTrial;	
		
		/** When creating a batch plot, the Blue locations clicked on the map */
		private List<String> blueLocationsClicked;
		
		/** When creating a batch plot, the sequence of backward and forward button presses */
		private List<BatchPlotProbeButtonType> buttonPressSequence;
		
		public BatchPlotState() {
			//blueLocationsClicked = new LinkedList<String>();
			//buttonPressSequence = new LinkedList<BatchPlotProbeButtonType>();
		}
		
		public void initialize(int currentTrial, AbstractRedTacticsProbe redTacticsProbe) {
			this.currentTrial = currentTrial;
			this.redTacticsProbe = redTacticsProbe;
			batchPlotCreated = false;
			batchPlotGoingBackward = true;
			batchPlotCurrentTrial = currentTrial;
			batchPlotMinimumTrial = currentTrial;
			batchPlotFirstPreviousTrial = currentTrial - 1;
			batchPlotFirstNextTrial = -1;
			//blueLocationsClicked.clear();
			//buttonPressSequence.clear();
			blueLocationsClicked = new LinkedList<String>();
			buttonPressSequence = new LinkedList<BatchPlotProbeButtonType>();			
		}
		
		public void createBatchPlot() {
			batchPlotCreated = true;
		}
		
		public boolean canGoBackward() {
			return batchPlotCurrentTrial > 0;
		}
		
		public boolean goBackward() {
			if(batchPlotCurrentTrial > 0) {
				if(!batchPlotGoingBackward) {
					batchPlotFirstPreviousTrial = batchPlotCurrentTrial;
					batchPlotGoingBackward = true;
				} else {
					batchPlotCurrentTrial--;
				}
				if(batchPlotCurrentTrial < batchPlotMinimumTrial) {
					batchPlotMinimumTrial = batchPlotCurrentTrial;
				}
				buttonPressSequence.add(BatchPlotProbeButtonType.Backward);
				return true;
			} else {
				return false;
			}
		}
		
		public boolean canGoForward() {
			return batchPlotCurrentTrial < currentTrial - 1;
		}
		
		public boolean goForward() {
			if(batchPlotCurrentTrial < currentTrial - 1) {
				if(batchPlotGoingBackward) {
					batchPlotFirstNextTrial = batchPlotCurrentTrial;
					batchPlotGoingBackward = false;
				} else {
					batchPlotCurrentTrial++;
				}
				buttonPressSequence.add(BatchPlotProbeButtonType.Forward);
				return true;
			} else {
				return false;
			}
		}
		
		public void locationClicked(String locationId) {
			blueLocationsClicked.add(locationId);
		}
		
		public String createShowingTrialsMessage() {
			StringBuilder sb = new StringBuilder("Showing Outcomes for Trials: ");
			if(batchPlotGoingBackward) {
				if(batchPlotCurrentTrial < currentTrial) {
					sb.append(Integer.toString(batchPlotCurrentTrial+1));
					if(batchPlotCurrentTrial < batchPlotFirstPreviousTrial) {
						sb.append(" - ");
						sb.append(Integer.toString(batchPlotFirstPreviousTrial+1));
					}
				}
			} else {
				sb.append(Integer.toString(batchPlotFirstNextTrial+1));
				if(batchPlotCurrentTrial > batchPlotFirstNextTrial) {
					sb.append(" - ");
					sb.append(Integer.toString(batchPlotCurrentTrial+1));
				}
			}
			return sb.toString();
		}
		
		public int getCurrentTrial() {
			return currentTrial;
		}

		public AbstractRedTacticsProbe getRedTacticsProbe() {
			return redTacticsProbe;
		}

		public boolean isBatchPlotCreated() {
			return batchPlotCreated;
		}

		public boolean isBatchPlotGoingBackward() {
			return batchPlotGoingBackward;
		}

		public int getBatchPlotCurrentTrial() {
			return batchPlotCurrentTrial;
		}

		public int getBatchPlotMinimumTrial() {
			return batchPlotMinimumTrial;
		}

		public int getBatchPlotFirstPreviousTrial() {
			return batchPlotFirstPreviousTrial;
		}

		public int getBatchPlotFirstNextTrial() {
			return batchPlotFirstNextTrial;
		}

		public List<String> getBlueLocationsClicked() {
			return blueLocationsClicked;
		}

		public List<BatchPlotProbeButtonType> getButtonPressSequence() {
			return buttonPressSequence;
		}
	}
	
	/** Command button listener class to handle reviewing the BLUEBOOK, SIGINT reliabilities, and Payoff matrix,
	 *  and also to handle batch plot button presses. */
	protected class CommandButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {	
			if(e.getActionCommand().equals(CommandButtonType.ReviewBlueBook.toString())) {
				//Review the BlueBook				
				setBlueBookVisible(true, reportPanelManager.isBatchPlotControlPanelAtRow(0) ? false : 
					ExperimentConstants_Phase2.HIGHLIGHT_BLUEBOOK_CELLS);
			} else if(e.getActionCommand().equals(CommandButtonType.ReviewSigintReliability.toString())) {
				//Review SIGINT reliability
				setSigintReliabilityVisible(true);
			} else if(e.getActionCommand().equals(CommandButtonType.ReviewPayoffMatrix.toString())) {
				//Review the Payoff matrix
				setPayoffMatrixVisible(true);				
			} else if(e.getActionCommand().equals(CommandButtonType.CreateBatchPlot.toString())) {
				//Create a batch plot
				createBatchPlot(true);
			} else if(e.getActionCommand().equals(CommandButtonType.DisplayPreviousOutcome.toString())) {
				//Display the previous outcome in a batch plot
				displayPreviousOutcome();
			} else if(e.getActionCommand().equals(CommandButtonType.DisplayNextOutcome.toString())) {
				//Display the next outcome in a batch plot
				displayNextOutcome();
			}
		}		
	}
	
	/** Button action listener to enable the Next button when a Red tactic is selected */
	protected class RedTacticSelectionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			//Enable next button if a red tactic is selected
			examController.setNavButtonEnabled(ButtonType.Next, 
				reportPanelManager.getRedTacticSelectionPanel().getSelectedRedTactic() != null);
		}	
	}
	
	/** Button action listener when selecting locations to obtain SIGINT at */
	protected class SigintLocationSelectionListener implements ActionListener {
		
		protected SigintSelectionTrialPartState trialPartState;
		
		/** Currently selected SIGINT locations */
		protected Set<String> selectedLocationIds = new HashSet<String>();
		
		public SigintSelectionTrialPartState getTrialPartState() {
			return trialPartState;
		}

		public void setTrialPartState(SigintSelectionTrialPartState trialPartState) {
			this.trialPartState = trialPartState;
			selectedLocationIds.clear();
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			//Highlight SIGINT location(s) selected and unhighlight any previous selections
			List<LocationDescriptor> locations = reportPanelManager.getLocationSelectionPanel().getSelectedLocations();
			Set<String> currentSelectedLocations = new HashSet<String>();
			boolean mapNeedsRefresh = false;
			if(locations != null && !locations.isEmpty()) {
				for(LocationDescriptor location : locations) {
					if(!selectedLocationIds.contains(location.getLocationId())) {
						BlueLocationPlacemark blueLocation = currentBlueLocationPlacemarks.get(location.getLocationId());
						if(blueLocation != null) {
							//blueLocation.setHighlighted(true);
							blueLocation.setForegroundColor(ColorManager_Phase2.getColor(
									ColorManager_Phase2.SIGINT_HIGHLIGHT_TEXT));
							mapNeedsRefresh = true;
						}
						selectedLocationIds.add(location.getLocationId());
						currentSelectedLocations.add(location.getLocationId());
					}
				}
			}
			if(!selectedLocationIds.isEmpty()) {
				for(String locationId : selectedLocationIds.toArray(new String[selectedLocationIds.size()])) {
					if(!currentSelectedLocations.contains(locationId)) {
						BlueLocationPlacemark blueLocation = currentBlueLocationPlacemarks.get(locationId);
						if(blueLocation != null) {
							//blueLocation.setHighlighted(false);
							blueLocation.setForegroundColor(Color.WHITE);
							mapNeedsRefresh = true;
						}
						selectedLocationIds.remove(locationId);
					}
				}
			}
			if(mapNeedsRefresh) {
				mapPanel.redrawMap();
			}
			
			//Enable next button if all SIGINT selections made
			examController.setNavButtonEnabled(ButtonType.Next, 
					reportPanelManager.getLocationSelectionPanel().getNumLocationsSelected() == 
					trialPartState.getNumLocationsToSelect());
		}
	}
	
	/** Button action listener when selecting Blue actions */
	protected class BlueActionSelectionListener implements ActionListener {
		
		protected BlueActionSelectionOrPresentationTrialPartState trialPartState;		

		public BlueActionSelectionOrPresentationTrialPartState getTrialPartState() {
			return trialPartState;
		}

		public void setTrialPartState(BlueActionSelectionOrPresentationTrialPartState trialPartState) {
			this.trialPartState = trialPartState;
		}

		@Override
		public void actionPerformed(ActionEvent e) {						
			BlueLocationPlacemark blueLocation = currentBlueLocationPlacemarks.get(e.getActionCommand());
			if(blueLocation != null) {
				BlueActionType action = reportPanelManager.getActionSelectionPanel().getActionSelection(e.getActionCommand());
				if(action != null) {
					blueLocation.setBlueAction(action);
					mapPanel.setActionBannerVisibleAtBlueLocation(blueLocation, true, true, false, false, false);
				}
			}
			//Enable next button if action selected at all blue locations
			examController.setNavButtonEnabled(ButtonType.Next, 
				reportPanelManager.getActionSelectionPanel().getNumActionsSelected() == 
				trialPartState.getNumLocations());
		}	
	}
	
	/** Listener to update the probability displayed in the location datum panel as the probability is updated 
	 * by the participant and/or check whether the normalization constraints are met and the values entered are valid. */
	protected class ProbabilitySumAndSequenceListener implements ProbabilitySumChangeListener {
		
		/** The current probability report trial part state */
		protected ProbabilityReportTrialPartState trialPartState;		
		
		/** The current normalization mode */
		protected NormalizationMode normalizationMode;
		
		/** The current probability container */
		protected IProbabilityEntryContainer probabilityContainer;
		
		protected int currSequenceIndex;
		
		/** The index of the last probability control that was adjusted */
		protected int lastProbabilityIndex;
		
		/** Whether to check whether the values entered by the participant are valid */
		protected boolean checkDisplayedValuesValid = true;
		
		/** Whether this listener is currently active */
		protected boolean listenerActive = true;
		
		public void initializeListener(ProbabilityReportTrialPartState trialPartState,
				IProbabilityEntryContainer probabilityContainer,
				NormalizationMode normalizationMode) {
			this.trialPartState = trialPartState;
			this.probabilityContainer = probabilityContainer;
			this.normalizationMode = normalizationMode;			
			//Initialize the interaction sequence
			currSequenceIndex = 0;
			lastProbabilityIndex =  -1;			
			if(trialPartState != null && trialPartState.getNumProbs() > 0) {
				List<ItemAdjustment> interactionSequence = 
						new ArrayList<ItemAdjustment>(trialPartState.getNumProbs());
				for(int i=0; i<trialPartState.getNumProbs(); i++) {
					interactionSequence.add(new ItemAdjustment());
				}
				trialPartState.setInteractionSequence(interactionSequence);
			} else {
				trialPartState.setInteractionSequence(null);
			}
		}
		
		public ProbabilityReportTrialPartState getTrialPartState() {
			return trialPartState;
		}		

		public NormalizationMode getNormalizationMode() {
			return normalizationMode;
		}		

		public IProbabilityEntryContainer getProbabilityContainer() {
			return probabilityContainer;
		}
		
		public boolean isCheckDisplayedValuesValid() {
			return checkDisplayedValuesValid;
		}

		public void setCheckDisplayedValuesValid(boolean checkDisplayedValuesValid) {
			this.checkDisplayedValuesValid = checkDisplayedValuesValid;
		}

		public boolean isListenerActive() {
			return listenerActive;
		}

		public void setListenerActive(boolean listenerActive) {
			this.listenerActive = listenerActive;
		}

		@Override
		public void probabilitySumChanged(ProbabilitySumChangeEvent event) {
			if(listenerActive && trialPartState != null && probabilityContainer != null) {				
				
				//Update the interaction sequence
				if(currSequenceIndex < trialPartState.getNumProbs() &&
						trialPartState.getInteractionSequence() != null &&
						event.adjustedControlIndex != null) {
					long time = System.currentTimeMillis();							
					if(lastProbabilityIndex == -1 ||
							lastProbabilityIndex != event.adjustedControlIndex) {							
						//System.out.println("Control Adjustment " + (currSequenceIndex+1) + ": " + event.adjustedControlIndex);
						ItemAdjustment currAdjustment = 
								trialPartState.getInteractionSequence().get(currSequenceIndex);		
						currAdjustment.setIndex(event.adjustedControlIndex);
						currAdjustment.setTimeStamp(time);
						lastProbabilityIndex = event.adjustedControlIndex;
						currSequenceIndex++;				
					}
				}
				
				List<Integer> currentSettings = probabilityContainer.getCurrentSettings();				
				if(currentSettings != null && !currentSettings.isEmpty()) {
					//Update the location datum panel
					if(trialPartState instanceof AttackProbabilityReportTrialPartState && locationDatumPanel != null) {					
						List<AttackProbability> probabilities = 
								((AttackProbabilityReportTrialPartState)trialPartState).getProbe().getProbabilities();
						if(probabilities != null &&	currentSettings.size() == trialPartState.getProbe().getProbabilities().size()) {
							DatumType datumType = ((AttackProbabilityReportTrialPartState)trialPartState).getAttackProbabilityReportDatumType();
							Iterator<AttackProbability> probabilityIter = probabilities.iterator();
							Iterator<Integer> settingIter = currentSettings.iterator();
							while(probabilityIter.hasNext()) {
								locationDatumPanel.setDatumValue(probabilityIter.next().getLocationId(), 
										datumType, MissionControllerUtils.createProbabilityString(settingIter.next()));
							}
						}
					}

					//Enable the next button if the normalization constraints are met and the values are valid					
					if(normalizationMode == NormalizationMode.NormalizeDuringManual || checkDisplayedValuesValid ||							
							!examController.isNavButtonEnabled(ButtonType.Next)) {						
						examController.setNavButtonEnabled(ButtonType.Next, 
								isNormalizationConstraintMet(currentSettings) &&
								(!checkDisplayedValuesValid || isAllDisplayValuesValid()));
					}	
				}
			}
		}
		
		public boolean isAllDisplayValuesValid() {
			//System.out.println("All settings valid: " + probabilityContainer.isAllDisplayValuesValid());
			if(probabilityContainer != null) {
				return probabilityContainer.isAllDisplayValuesValid();
			} else {
				return true;
			}
		}
		
		public boolean isNormalizationConstraintMet() {
			if(probabilityContainer != null) {
				return this.isNormalizationConstraintMet(probabilityContainer.getCurrentSettings());				
			} else {
				return false;
			}	
		}
		
		private boolean isNormalizationConstraintMet(List<Integer> currentSettings) {									
			if(trialPartState.getNormalizationConstraint() != null) {
				int sum = currentSettings != null ? ProbabilityUtils.computeSum(currentSettings) : 0;
				return trialPartState.getNormalizationConstraint().isNormalizationConstraintMet((double)sum);							
			} else {
				return true;
			}
		}
	}
	
	protected class BatchPlotLocationClickedListener implements MapEventListener {
		@Override
		public void mapObjectClicked(MapEvent event) {
			if(event.getMapObject() != null && event.getMapObject() instanceof BlueLocationPlacemark) {
				//System.out.println("Blue location clicked: " + event.getMapObject().getId());
				batchPlotState.locationClicked(event.getMapObject().getId());
			}
		}
	}	
}