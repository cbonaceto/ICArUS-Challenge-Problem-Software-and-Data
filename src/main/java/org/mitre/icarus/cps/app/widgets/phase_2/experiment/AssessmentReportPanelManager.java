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
package org.mitre.icarus.cps.app.widgets.phase_2.experiment;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;

import org.mitre.icarus.cps.app.experiment.phase_2.MissionControllerUtils;
import org.mitre.icarus.cps.app.widgets.WidgetConstants;
import org.mitre.icarus.cps.app.widgets.phase_2.experiment.CommandButton.CommandButtonType;
import org.mitre.icarus.cps.app.widgets.phase_2.experiment.datum_list.DatumListItem;
import org.mitre.icarus.cps.app.widgets.phase_2.experiment.datum_list.DatumListItemType;
import org.mitre.icarus.cps.app.widgets.phase_2.experiment.quad_chart.QuadChartPanelFactory;
import org.mitre.icarus.cps.app.widgets.phase_2.experiment.quad_chart.RedTacticParametersPanel;
import org.mitre.icarus.cps.app.widgets.phase_2.experiment.report_panel.AssessmentReportPanel;
import org.mitre.icarus.cps.app.widgets.phase_2.experiment.report_panel.ReportComponentContainer.Alignment;
import org.mitre.icarus.cps.app.widgets.probability_entry.ProbabilityContainerLayout;
import org.mitre.icarus.cps.app.widgets.probability_entry.ProbabilityContainerLayout.ProbabilityContainerLayoutType;
import org.mitre.icarus.cps.app.window.IcarusLookAndFeel;
import org.mitre.icarus.cps.exam.phase_2.testing.bluebook.RedTacticParameters;
import org.mitre.icarus.cps.exam.phase_2.testing.bluebook.RedTacticType;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.AttackProbability;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.BlueAction;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.NormalizationConstraint;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.RedAction.RedActionType;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.red_tactics_probes.RedTacticProbability;
import org.mitre.icarus.cps.experiment_core.gui.IConditionComponent;

/**
 * Manages Phase 2 interaction widgets in an assessment report panel.
 * 
 * @author CBONACETO
 *
 */
public class AssessmentReportPanelManager {	
	/** Command button orientations */
	public static enum CommandButtonOrientation {AboveInteractionComponent, BelowInteractionComponent};

	/** The assessment report panel */
	protected final AssessmentReportPanel assessmentReportPanel;
	
	/** The report panel components */
	protected final ArrayList<ReportComponent_Phase2> reportComponents;
	
	/** A red tactic selection panel */
	protected RedTacticSelectionPanel redTacticSelectionPanel;
	
	/** A red tactics probability entry panel */
	protected ProbabilityReportPanel redTacticsProbabilityReportPanel;
	
	/** A red tactic parameters pane */
	protected RedTacticParametersPanel redTacticParametersPanel;
	
	/** An attack probability entry panel */
	protected ProbabilityReportPanel attackProbabilityReportPanel;	
	
	/** A location selection panel */
	protected LocationSelectionPanel locationSelectionPanel;	
	
	/** A blue action selection panel */
	protected ActionSelectionPanel actionSelectionPanel;
	
	/** List of listeners registered to receive command button events */
	protected List<ActionListener> commandButtonListeners = Collections.synchronizedList(new LinkedList<ActionListener>());
	
	/** List containing all command buttons in all report panel components. Outer list contains the rows,
	 * inner list contains the buttons for each row. */
	protected List<List<CommandButton>> commandButtons;
	
	public AssessmentReportPanelManager(int numReportPanels) {
		this(new AssessmentReportPanel(WidgetConstants.COMPONENT_SPACER, null), numReportPanels);
	}
	
	public AssessmentReportPanelManager(AssessmentReportPanel assessmentReportPanel, int numReportPanels) {
		if(numReportPanels < 1 || numReportPanels > 6) {
			throw new IllegalArgumentException("Error creating AssessmentReportPanelManager: Must have between 1 and 6 report panels.");
		}
		if(assessmentReportPanel == null) {
			throw new IllegalArgumentException("Error creating AssessmentReportPanelManager: AssessmentReportPanel cannot be null.");
		}
		//Create the report panel components
		this.assessmentReportPanel = assessmentReportPanel;
		assessmentReportPanel.removeAllComponents();
		reportComponents = new ArrayList<ReportComponent_Phase2>(numReportPanels);
		commandButtons = new ArrayList<List<CommandButton>>(numReportPanels);
		for(int i=0; i<numReportPanels; i++) {
			ReportComponent_Phase2 reportComponent = new ReportComponent_Phase2("Report_" + Integer.toString(i));
			reportComponents.add(reportComponent);
			assessmentReportPanel.addComponentAtBottom(reportComponent, Alignment.Stretch, null, true, false, false, null);
			commandButtons.add(null);
		}
		assessmentReportPanel.setAllComponentsVisible(false);
	}
	
	public AssessmentReportPanel getAssessmentReportPanel() {
		return assessmentReportPanel;
	}
	
	public boolean isCommandButtonListenerPresent(ActionListener l) {
		synchronized(commandButtonListeners) {
			if(!commandButtonListeners.isEmpty()) {				
				for(ActionListener listener : commandButtonListeners) {
					if(l == listener) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public void addCommandButtonListener(ActionListener l) {
		synchronized(commandButtonListeners) {
			commandButtonListeners.add(l);
			for(List<CommandButton> buttons : commandButtons) {
				if(buttons != null && !buttons.isEmpty()) {
					for(CommandButton button : buttons) {
						button.addActionListener(l);
					}
				}
			}
			for(ReportComponent_Phase2 reportComponent : reportComponents) {
				reportComponent.addBatchPlotCommandButtonListener(l);
			}
		}
	}
	
	public void removeCommandButtonListener(ActionListener l) {
		synchronized(commandButtonListeners) {
			commandButtonListeners.remove(l);
			for(List<CommandButton> buttons : commandButtons) {
				if(buttons != null && !buttons.isEmpty()) {
					for(CommandButton button : buttons) {
						button.removeActionListener(l);
					}
				}
			}
			for(ReportComponent_Phase2 reportComponent : reportComponents) {
				reportComponent.removeBatchPlotCommandButtonListener(l);
			}
		}
	}
	
	public ReportComponent_Phase2 getReportComponentAtRow(int row) {
		if(row < 0 || row >= reportComponents.size()) {
			throw new IllegalArgumentException("Error getting report component in AssessmentReportPanelManager: row out of bounds.");
		}
		return reportComponents.get(row);
	}
	
	public void setInteractionComponentAtRow(IConditionComponent interactionComponent, int row, String title, boolean titleVisible) {
		setInteractionComponentAtRow(interactionComponent, row, title, titleVisible, Alignment.Stretch);
	}	
	
	public void setInteractionComponentAtRow(IConditionComponent interactionComponent, int row, String title, boolean titleVisible, 
			Alignment interactionComponentAlignment) {
		ReportComponent_Phase2 reportComponent = getReportComponentAtRow(row);
		if(reportComponent != null) {
			reportComponent.setInteractionComponent(interactionComponent, interactionComponentAlignment);
			if(interactionComponent != null) {
				reportComponent.setContentPanelVisible(true);
			}
			assessmentReportPanel.setComponentPropertiesAtRow(Alignment.Stretch, row, title, titleVisible, true, false, null);
		}
	}	
	
	/**
	 * @param row
	 * @return
	 */
	public IConditionComponent removeInteractionComponentAtRow(int row) {
		ReportComponent_Phase2 reportComponent = getReportComponentAtRow(row);
		if(reportComponent != null) {
			IConditionComponent comp = reportComponent.interactionComponent;
			reportComponent.setInteractionComponent(null, null);
			assessmentReportPanel.setComponentVisible(row, false);
			return comp;
		} 
		return null;
	}
	
	/**
	 * @param row
	 * @param visible
	 */
	public void setInteractionComponentVisibleAtRow(int row, boolean visible) {
		ReportComponent_Phase2 reportComponent = getReportComponentAtRow(row);
		if(reportComponent != null && visible != reportComponent.isContentPanelVisible()) {
			reportComponent.setContentPanelVisible(visible);
			assessmentReportPanel.revalidateContentPanel();
		}
	}
	
	/**
	 * @param row
	 * @return
	 */
	public boolean isInteractionComponentVisibleAtRow(int row) {
		ReportComponent_Phase2 reportComponent = getReportComponentAtRow(row);
		if(reportComponent != null) {
			return reportComponent.isContentPanelVisible();
		} else {
			return false;
		}
	}
	
	/**
	 * @param row
	 */
	public void removeDatumListAtRow(int row) {
		ReportComponent_Phase2 reportComponent = getReportComponentAtRow(row);
		reportComponent.clearDataumPanel();
		reportComponent.setDatumPanelVisible(false);
	}
	
	/**
	 * @param row
	 * @param datumItems
	 * @param datumValues
	 */
	public void setDatumListAtRow(int row, List<DatumListItem> datumItems, List<String> datumValues) {
		ReportComponent_Phase2 reportComponent = getReportComponentAtRow(row);
		reportComponent.setDatumPanelItems(datumItems, datumValues);
		reportComponent.setDatumPanelVisible(true);
	}
	
	public boolean isBatchPlotControlPanelAtRow(int row) {
		ReportComponent_Phase2 reportComponent = getReportComponentAtRow(row);
		return reportComponent.isBatchPlotControlPanelVisible();
	}
	
	/**
	 * @param row
	 */
	public void removeBatchPlotControlPanelAtRow(int row) {
		ReportComponent_Phase2 reportComponent = getReportComponentAtRow(row);
		reportComponent.setBatchPlotControlPanelVisible(false);
	}
	
	/**
	 * @param row
	 */
	public void addBatchPlotControlPanelAtRow(int row) {
		ReportComponent_Phase2 reportComponent = getReportComponentAtRow(row);
		reportComponent.setBatchPlotControlPanelVisible(true);
	}
	
	/**
	 * @param row
	 * @param batchPlotButtonEnabled
	 * @param message
	 */
	public void configureBatchPlotControlPanelAtRow(int row, boolean batchPlotButtonEnabled, String message) {
		ReportComponent_Phase2 reportComponent = getReportComponentAtRow(row);
		reportComponent.configureBatchPlotControlPanel(batchPlotButtonEnabled, message);
	}
	
	/**
	 * @param row
	 * @param showPreviousOutcomeButton
	 * @param previousOutcomeButtonEnabled
	 * @param showNextOutcomeButton
	 * @param nextOutcomeButtonEnabled
	 * @param message
	 */
	public void configureBatchPlotControlPanelAtRow(int row, boolean showPreviousOutcomeButton, 
			boolean previousOutcomeButtonEnabled, boolean showNextOutcomeButton, 
			boolean nextOutcomeButtonEnabled, String message) {
		ReportComponent_Phase2 reportComponent = getReportComponentAtRow(row);
		reportComponent.configureBatchPlotControlPanel(showPreviousOutcomeButton, previousOutcomeButtonEnabled, 
				showNextOutcomeButton, nextOutcomeButtonEnabled, message);
	}
	
	/**
	 * @param row
	 * @param previousOutcomeButtonEnabled
	 * @param nextOutcomeButtonEnabled
	 * @param message
	 */
	public void configureBatchPlotControlPanelAtRow(int row, boolean previousOutcomeButtonEnabled,
			boolean nextOutcomeButtonEnabled, String message) {
		ReportComponent_Phase2 reportComponent = getReportComponentAtRow(row);		
		reportComponent.configureBatchPlotControlPanel(previousOutcomeButtonEnabled,
				nextOutcomeButtonEnabled, message);
	}
	
	/**
	 * @param row
	 * @param message
	 */
	public void configureBatchPlotControlPanelAtRow(int row, String message) {
		ReportComponent_Phase2 reportComponent = getReportComponentAtRow(row);
		reportComponent.configureBatchPlotControlPanel(message);
	}
	
	/**
	 * @param row
	 */
	public void removeCommandButtonsAtRow(int row) {
		ReportComponent_Phase2 reportComponent = getReportComponentAtRow(row);
		reportComponent.clearLowerCommandButtons();
		reportComponent.setLowerCommandButtonPanelVisible(false);
		reportComponent.clearUpperCommandButtons();
		reportComponent.setUpperCommandButtonPanelVisible(false);
	}
	
	/**
	 * @param row
	 * @param buttonType
	 * @param buttonOrientation
	 */
	public void setCommandButtonAtRow(int row, CommandButtonType buttonType, 
			CommandButtonOrientation buttonOrientation) {
		setCommandButtonsAtRow(row, Collections.singleton(buttonType), 1, buttonOrientation);
	}
	
	/**
	 * @param row
	 * @param buttonTypes
	 * @param buttonOrientation
	 */
	public void setCommandButtonsAtRow(int row, Collection<CommandButtonType> buttonTypes, 
			int numColumns, CommandButtonOrientation buttonOrientation) {
		if(buttonTypes == null || buttonTypes.isEmpty()) {
			removeCommandButtonsAtRow(row);
		} else {
			ReportComponent_Phase2 reportComponent = getReportComponentAtRow(row);
			List<CommandButton> buttons = commandButtons.get(row);
			boolean createNewButtons = false;
			if(buttons != null && buttons.size() == buttonTypes.size()) {
				Iterator<CommandButtonType> buttonTypeIter = buttonTypes.iterator(); 
				for(CommandButton button : buttons) {
					if(button.getButtonType() != buttonTypeIter.next()) {
						createNewButtons = true;
						break;
					}
				}
			} else {
				createNewButtons = true;
			}
			if(createNewButtons) {
				//Create new buttons for the row
				buttons = new LinkedList<CommandButton>();
				commandButtons.set(row,  buttons);
				synchronized(commandButtonListeners) {
					for(CommandButtonType buttonType : buttonTypes) {
						CommandButton button = new CommandButton(buttonType); 
						buttons.add(button);
						if(!commandButtonListeners.isEmpty()) {
							for(ActionListener l : commandButtonListeners) {
								button.addActionListener(l);
							}
						}
					}
				}
			} else {
				//Just ensure the buttons for the row are enabled
				for(CommandButton button : buttons) {
					button.button.setEnabled(true);
				}
			}
			switch(buttonOrientation) {
			case BelowInteractionComponent:
				reportComponent.clearUpperCommandButtons();
				reportComponent.setUpperCommandButtonPanelVisible(false);
				reportComponent.setLowerCommandButtons(buttons, numColumns);				
				reportComponent.setLowerCommandButtonPanelVisible(true);
				break;
			case AboveInteractionComponent:
				reportComponent.clearLowerCommandButtons();
				reportComponent.setLowerCommandButtonPanelVisible(false);
				reportComponent.setUpperCommandButtons(buttons, numColumns);
				reportComponent.setUpperCommandButtonPanelVisible(true);
				break;
			}
		}
	}	
	
	
	
	/**
	 * @param row
	 * @param previousButtonType
	 * @param newButtonType
	 * @param showMessage
	 * @param message
	 */
	public void configureCommandButtonAtRow(int row, CommandButtonType previousButtonType,
			CommandButtonType newButtonType, boolean buttonEnabled, boolean showMessage, String message) {
		CommandButton button = getCommandButton(row, previousButtonType);
		if(button != null) {
			if(previousButtonType != newButtonType) {
				button.setButtonType(newButtonType);
			}
			if(showMessage != button.isButtonDescriptionVisible()) {
				button.setButtonDescriptionVisible(showMessage);
			}
			if(button.button.isEnabled() != buttonEnabled) {
				button.button.setEnabled(buttonEnabled);
			}
			button.setButtonDescription(message);
		}
	}
	
	/**
	 * @param row
	 * @param buttonType
	 * @param enabled
	 */
	public void setCommandButtonEnabledAtRow(int row, CommandButtonType buttonType, boolean enabled) {		
		CommandButton button = getCommandButton(row, buttonType);
		if(button != null) {
			button.setEnabled(enabled);
		}
	}
	
	/**
	 * @param row
	 * @param buttonType
	 * @param showMessage
	 * @param message
	 */
	public void setCommandButtonMessageAtRow(int row, CommandButtonType buttonType, 
			boolean buttonEnabled, boolean showMessage, String message) {
		CommandButton button = getCommandButton(row, buttonType);
		if(button != null) {
			if(showMessage != button.isButtonDescriptionVisible()) {
				button.setButtonDescriptionVisible(showMessage);
			}
			if(button.isEnabled() != buttonEnabled) {
				button.setEnabled(buttonEnabled);
			}
			button.setButtonDescription(message);
		}
	}
	
	/**
	 * @param row
	 * @param buttonType
	 * @return
	 */
	public boolean isCommandButtonAtRow(int row, CommandButtonType buttonType) {
		return getCommandButton(row, buttonType) != null;
	}
	
	protected CommandButton getCommandButton(int row, CommandButtonType buttonType) {
		List<CommandButton> buttons = commandButtons.get(row);
		if(buttons != null ) {
			for(CommandButton button : buttons) {
				if(button.getButtonType() == buttonType) {
					return button;
				}
			}
		}
		return null;
	}
	
	public void setAllReportComponentsVisible(boolean visible) { 
		assessmentReportPanel.setAllComponentsVisible(visible);
	}
	
	public boolean isReportComponentVisible(int row) {
		return assessmentReportPanel.isComponentVisible(row);
	}
	
	public void setReportComponentVisible(int row, boolean visible) {
		assessmentReportPanel.setComponentVisible(row, visible);
	}
	
	/**
	 * @param redTactics
	 * @return
	 */
	public RedTacticSelectionPanel configureRedTacticSelectionPanel(Collection<RedTacticType> redTactics) {
		if(redTacticSelectionPanel == null) {
			redTacticSelectionPanel = new RedTacticSelectionPanel("RedTacticSelectionPanel");
		}
		redTacticSelectionPanel.setRedTactics(redTactics);
		return redTacticSelectionPanel;
	}	
	
	public RedTacticSelectionPanel getRedTacticSelectionPanel() {
		return redTacticSelectionPanel;
	}
	
	/**
	 * @param probabilities
	 * @param title
	 * @param showTitle
	 * @param showSum
	 * @param normalizationErrorMessageVisible
	 * @param normalizationErrorMessage
	 * @param normalizationConstraint
	 * @param autoNormalize
	 * @return
	 */
	public ProbabilityReportPanel configureRedTacticsProbabilityReportPanel(List<RedTacticProbability> probabilities,
			String title, boolean showTitle, boolean showSum, boolean normalizationErrorMessageVisible, 
			String normalizationErrorMessage, NormalizationConstraint normalizationConstraint,
			boolean autoNormalize) {
		if(redTacticsProbabilityReportPanel == null) {
			redTacticsProbabilityReportPanel = new ProbabilityReportPanel("RedTacticsProbabilityReportPanel",
					new ProbabilityContainerLayout(ProbabilityContainerLayoutType.VERTICAL, 
							probabilities.size()));
		}
		redTacticsProbabilityReportPanel.configureProbabilityEntryContainer(
				createRedTacticsReportHypotheses(probabilities), 
				title, showTitle, showSum,  normalizationErrorMessageVisible, 
				normalizationErrorMessage, normalizationConstraint);
		redTacticsProbabilityReportPanel.getProbabilityEntryContainer().setAutoNormalize(autoNormalize);
		return redTacticsProbabilityReportPanel;
	}
	
	/** Create strings with the hypothesis title for each Red tactic type */
	protected List<String> createRedTacticsReportHypotheses(List<RedTacticProbability> probabilities) {
		if(probabilities != null && !probabilities.isEmpty()) {
			List<String> hypotheses = new ArrayList<String>(probabilities.size());
			for(RedTacticProbability probability : probabilities) {
				hypotheses.add("P(Red is " + probability.getRedTactic().getName() + ")");
			}
			return hypotheses;
		}
		return null;
	}

	public ProbabilityReportPanel getRedTacticsProbabilityReportPanel() {
		return redTacticsProbabilityReportPanel;
	}
	
	public RedTacticParametersPanel configureRedTacticParametersPanel(String title, RedTacticParameters redTactic,
			Integer min_U, Integer max_U) {
		if(redTacticParametersPanel == null) {
			redTacticParametersPanel = QuadChartPanelFactory.createRedTacticParametersPanel(title, redTactic, 
					min_U, max_U, false, false, true);
			redTacticParametersPanel.setComponentId("RedTacticParameters");
		} else {
			redTacticParametersPanel.setRedTacticParameters(redTactic, min_U, max_U);
		}
		return redTacticParametersPanel;
	}

	public RedTacticParametersPanel getRedTacticParametersPanel() {
		return redTacticParametersPanel;
	}

	/**
	 * @param probabilities
	 * @param title
	 * @param showTitle
	 * @param showSum
	 * @return
	 */
	public ProbabilityReportPanel configureAttackProbabilityReportPanel(List<AttackProbability> probabilities, 
			String title, boolean showTitle, boolean showSum, boolean normalizationErrorMessageVisible, 
			String normalizationErrorMessage, NormalizationConstraint normalizationConstraint) {
		if(attackProbabilityReportPanel == null) {
			attackProbabilityReportPanel = new ProbabilityReportPanel("AttackReportPanel",
					new ProbabilityContainerLayout(ProbabilityContainerLayoutType.VERTICAL, 
							probabilities.size()));
		}
		attackProbabilityReportPanel.configureProbabilityEntryContainer(
				createAttackReportHypotheses(probabilities), 
				title, showTitle, showSum,  normalizationErrorMessageVisible, 
				normalizationErrorMessage, normalizationConstraint);
		return attackProbabilityReportPanel;
	}	
	
	/**
	 * @param hypotheses
	 * @param title
	 * @param showTitle
	 * @param showSum
	 * @return
	 */
	public ProbabilityReportPanel configureAttackProbabilityReportPanel(Collection<String> hypotheses, String title, 
			boolean showTitle, boolean showSum, boolean normalizationErrorMessageVisible, 
			String normalizationErrorMessage, NormalizationConstraint normalizationConstraint) {
		if(attackProbabilityReportPanel == null) {
			attackProbabilityReportPanel = new ProbabilityReportPanel("AttackReportPanel",
				new ProbabilityContainerLayout(ProbabilityContainerLayoutType.VERTICAL, 
						hypotheses.size()));
		}
		attackProbabilityReportPanel.configureProbabilityEntryContainer(hypotheses, title, showTitle, showSum,
				normalizationErrorMessageVisible, normalizationErrorMessage, normalizationConstraint);
		return attackProbabilityReportPanel;
	}	
	
	/** Create strings with the hypothesis title for each attack probability report (e.g., Red Attacks Blue Location 1, etc.) */
	protected List<String> createAttackReportHypotheses(List<AttackProbability> probabilities) {
		if(probabilities != null && !probabilities.isEmpty()) {
			List<String> hypotheses = new ArrayList<String>(probabilities.size());
			for(int i=0; i<probabilities.size(); i++) {
				if(probabilities.size() > 1) {
					hypotheses.add("At " + 
					MissionControllerUtils.createBlueLocationProbabilityReportDisplayName(i, probabilities.size()));
				} else {
					hypotheses.add("");
				}
			}
			return hypotheses;
		}
		return null;
	}

	public ProbabilityReportPanel getAttackProbabilityReportPanel() {
		return attackProbabilityReportPanel;
	}
	
	public LocationSelectionPanel configureLocationSelectionPanel(Collection<String> locationIds, int numLocationsToSelect) {
		return configureLocationSelectionPanel(createLocationNames(locationIds), 
				locationIds, createLocationIndexes(locationIds), numLocationsToSelect);
	}

	public LocationSelectionPanel configureLocationSelectionPanel(Collection<String> locationNames, 
			Collection<String> locationIds, Collection<Integer> locationIndexes, int numLocationsToSelect) {
		if(locationSelectionPanel == null) {
			locationSelectionPanel = new LocationSelectionPanel("LocationSelectionPanel");
		}
		locationSelectionPanel.setLocations(locationNames, locationIds, locationIndexes, numLocationsToSelect);
		return locationSelectionPanel;
	}
	
	/** Create strings with the location name of each location to select (e.g., Blue Location 1, etc.) */
	protected List<String> createLocationNames(Collection<String> locationIds) {
		if(locationIds != null && !locationIds.isEmpty()) {
			List<String> locationNames = new ArrayList<String>(locationIds.size());
			for(int i=0; i<locationIds.size(); i++) {
				locationNames.add(MissionControllerUtils.createBlueLocationProbabilityReportDisplayName(i, locationIds.size()));
			}
			return locationNames;
		}
		return null;
	}
	
	protected List<Integer> createLocationIndexes(Collection<String> locationIds) {
		if(locationIds != null && !locationIds.isEmpty()) {
			List<Integer> locationIndexes = new ArrayList<Integer>(locationIds.size());
			for(int i=0; i<locationIds.size(); i++) {
				locationIndexes.add(i);
			}
			return locationIndexes;
		}
		return null;
	}

	public LocationSelectionPanel getLocationSelectionPanel() {
		return locationSelectionPanel;
	}

	/**
	 * @param blueActions
	 * @param selectBlueActions
	 * @return
	 */
	public ActionSelectionPanel configureActionSelectionPanel(Collection<BlueAction> blueActions, boolean selectBlueActions) {
		if(blueActions != null && !blueActions.isEmpty()) {
			ActionSelectionPanel panel =  configureActionSelectionPanel(
				createLocationNamesFromBlueActions(blueActions), createLocationIdsFromBlueActions(blueActions));
			if(selectBlueActions) {
				for(BlueAction blueAction : blueActions) {
					panel.setActionSelection(blueAction.getLocationId(), blueAction.getAction());
				}
			} 
		} else {
			configureActionSelectionPanel(null, null);
		}
		return actionSelectionPanel;
	}
	
	/**
	 * @param locationIds
	 * @return
	 */
	public ActionSelectionPanel configureActionSelectionPanel(Collection<String> locationIds) {
		return configureActionSelectionPanel(createLocationNames(locationIds), locationIds);
	}
	
	/**
	 * @param locationNames
	 * @param locationIds
	 * @return
	 */
	public ActionSelectionPanel configureActionSelectionPanel(Collection<String> locationNames, 
			Collection<String> locationIds) {
		if(actionSelectionPanel == null) {
			actionSelectionPanel = new ActionSelectionPanel("ActionSelectionPanel");
		}
		actionSelectionPanel.setLocations(locationNames, locationIds);
		return actionSelectionPanel;
	}

	protected List<String> createLocationNamesFromBlueActions(Collection<BlueAction> blueActions) {
		if(blueActions != null && !blueActions.isEmpty()) {
			List<String> locationNames = new ArrayList<String>(blueActions.size());
			for(int i=0; i<blueActions.size(); i++) {
				locationNames.add(MissionControllerUtils.createBlueLocationProbabilityReportDisplayName(i, blueActions.size()));
			}
			return locationNames;
		}
		return null;
	}
	
	protected List<String> createLocationIdsFromBlueActions(Collection<BlueAction> blueActions) {
		if(blueActions != null && !blueActions.isEmpty()) {
			List<String> locationIds = new ArrayList<String>(blueActions.size());
			for(BlueAction blueAction : blueActions) {
				locationIds.add(blueAction.getLocationId());
			}
			return locationIds;
		}
		return null;
	}
	
	/** Test main */
	public static void main(String[] args) {
		IcarusLookAndFeel.initializeICArUSLookAndFeel();
		JFrame frame = new JFrame("Test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		AssessmentReportPanel p = new AssessmentReportPanel(10, null);
		frame.getContentPane().add(p);
		p.setPreferredSize(new Dimension(400, 800));
		
		AssessmentReportPanelManager reportPanelManager = new AssessmentReportPanelManager(p, 6);
		String reportName = "P (Propensity)";
		List<AttackProbability> probabilities = Arrays.asList(new AttackProbability("1-1", 0, RedActionType.Attack)); 
		reportPanelManager.configureAttackProbabilityReportPanel(probabilities, reportName + " Report", true, true,
				true, "Sorry, Error", NormalizationConstraint.createDefaultNormalizationConstraint());
		List<DatumListItem> datumItems = Arrays.asList(
				new DatumListItem("Consider:", null, Color.black, false, true),
				DatumListItemType.OSINT.getDatumListItem(), 
				DatumListItemType.IMINT.getDatumListItem(),
				DatumListItemType.HUMINT.getDatumListItem(), 
				DatumListItemType.P_ACTIVITY.getDatumListItem(), 
				DatumListItemType.P_ACTIVITY_CAPABILITY_PROPENSITY.getDatumListItem());
		List<String> datumValues = Arrays.asList(null, "100%", "10", "100%", "100%", "100%");		
		reportPanelManager.setDatumListAtRow(0, datumItems, datumValues);
		reportPanelManager.setCommandButtonAtRow(0, CommandButtonType.ReviewBlueBook, 
			CommandButtonOrientation.AboveInteractionComponent);			
		reportPanelManager.setInteractionComponentAtRow(reportPanelManager.getAttackProbabilityReportPanel(), 
				0, reportName, true);
		reportPanelManager.setReportComponentVisible(0, true);		
		
		frame.pack();
		frame.setVisible(true);
	}
	
	public ActionSelectionPanel getActionSelectionPanel() {
		return actionSelectionPanel;
	}
}