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
package org.mitre.icarus.cps.app.experiment.phase_1;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org.mitre.icarus.cps.app.experiment.IcarusExperimentController;
import org.mitre.icarus.cps.app.experiment.TutorialController;
import org.mitre.icarus.cps.app.util.ProbabilityUtils;
import org.mitre.icarus.cps.app.widgets.WidgetConstants;
import org.mitre.icarus.cps.app.widgets.ImageManager.IconSize;
import org.mitre.icarus.cps.app.widgets.basic.tutorial.TutorialPanel;
import org.mitre.icarus.cps.app.widgets.phase_1.ColorManager_Phase1;
import org.mitre.icarus.cps.app.widgets.phase_1.ImageManager_Phase1;
import org.mitre.icarus.cps.app.widgets.phase_1.SurpriseEntryPanel;
import org.mitre.icarus.cps.app.widgets.phase_1.experiment.GroupSelectionPanel;
import org.mitre.icarus.cps.app.widgets.probability_entry.IProbabilityEntryContainer;
import org.mitre.icarus.cps.app.widgets.probability_entry.ProbabilityEntryComponentFactory;
import org.mitre.icarus.cps.exam.phase_1.training.TutorialPage;
import org.mitre.icarus.cps.exam.phase_1.training.TutorialPhase;
import org.mitre.icarus.cps.exam.phase_1.training.TutorialPage.InputType;
import org.mitre.icarus.cps.experiment_core.Experiment;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupType;

/**
 * The exam tutorial controller. Allows subject to navigate through a list of tutorial pages.
 * Each tutorial page may also display a probability entry component for training.
 * 
 * @author CBONACETO
 *
 * @param <EC>
 * @param <E>
 *
 */
public class TutorialController_Phase1<
	EC extends IcarusExperimentController<E, ?, ?, ?>,
	E extends Experiment<?>>
	extends TutorialController<EC, E, TutorialPage, TutorialPhase> {			
	
	/** The probability entry panel */
	private IProbabilityEntryContainer probabilityEntryPanel;
	
	/** The troop allocation panel */
	private IProbabilityEntryContainer troopAllocationPanel;
	
	/** The troop selection panel */
	private TroopSelectionPanel troopSelectionPanel;
	
	/** The surprise entry panel */
	private SurpriseEntryPanel surprisePanel;
	
	private int numProbabilityEntries;
	
	private List<Integer> initialProbabilities;		 
	
	private List<Integer> currentProbabilities; 
	
	private List<Integer> normalizedProbabilities;			

	/**
	 * No-arg constructor.
	 */
	public TutorialController_Phase1() {}
	
	/**
	 * Constructor that takes the tutorial phase and the tutorial panel.
	 * 
	 * @param tutorial the tutorial phase
	 * @param conditionPanel the tutorial panel
	 */
	public TutorialController_Phase1(TutorialPhase tutorial, TutorialPanel conditionPanel) {
		super(tutorial, conditionPanel);	
	}	
	
	@Override
	protected void initializeTutorial() {
		if(condition.getProbabilityEntryGroups() != null) {
			numProbabilityEntries = condition.getProbabilityEntryGroups().size();
		} else {
			numProbabilityEntries = condition.getProbabilityEntryTitles().size();
		}		
		probabilityEntryPanel = null;
		troopAllocationPanel = null;
		surprisePanel = null;
	}	
	
	@Override
	protected void tutorialStarting() {		
		initialProbabilities = ProbabilityUtils.createDefaultInitialProbabilities(numProbabilityEntries);			 
		currentProbabilities = ProbabilityUtils.createDefaultInitialProbabilities(numProbabilityEntries);
		normalizedProbabilities = ProbabilityUtils.createDefaultInitialProbabilities(numProbabilityEntries);
	}
	
	@Override
	protected void tutorialPageChanging(TutorialPage currentPage) {
		if(currentPage != null && currentPage.isProbabilityPanelEditable()) {
			//Capture the current probabilities and update the normalized probabilities
			if(currentPage.getInputPanelType() == InputType.ProbabilityPanel) {
				if(probabilityEntryPanel != null) {
					probabilityEntryPanel.getCurrentSettings(currentProbabilities);
				}
			} else if(currentPage.getInputPanelType() == InputType.TroopAllocationPanel) {
				if(troopAllocationPanel != null) {
					troopAllocationPanel.getCurrentSettings(currentProbabilities);
				}
			}
			ProbabilityUtils.normalizePercentProbabilities(currentProbabilities, normalizedProbabilities);
		} 
	}
	
	@Override
	protected void tutorialPageChanged(TutorialPage newPage) {
		if(newPage.getInputPanelType() != null) {
			List<GroupType> groups = null;
			List<String> titles = null;
			if(newPage.getProbabilityEntryGroups() != null || newPage.getProbabilityEntryTitles() != null) {
				if(newPage.getProbabilityEntryGroups() != null) {
					groups = newPage.getProbabilityEntryGroups();
				} else {
					titles = newPage.getProbabilityEntryTitles();
				}
			} else {
				if(condition.getProbabilityEntryGroups() != null) {
					groups = condition.getProbabilityEntryGroups();
				} else {
					titles = condition.getProbabilityEntryTitles();
				}
			}	

			if(newPage.getInputPanelType() == InputType.ProbabilityPanel) {
				//Show a probability entry panel
				if(probabilityEntryPanel == null) {
					createProbabilityEntryPanel();
				}				
				if(groups != null) {
					setGroupsForProbabilityEntryPanel(groups, probabilityEntryPanel);					
				} else {
					probabilityEntryPanel.setProbabilityEntryTitles(titles);
					probabilityEntryPanel.restoreDefaultProbabilityEntryColors();
				}
				switch(newPage.getProbabilityType()) {
				case Initial:
					probabilityEntryPanel.setCurrentSettings(initialProbabilities);
					probabilityEntryPanel.setPreviousSettings(initialProbabilities);
					break;
				case Current:
					probabilityEntryPanel.setCurrentSettings(currentProbabilities);
					probabilityEntryPanel.setPreviousSettings(initialProbabilities);
					break;
				case Normalized:
					probabilityEntryPanel.setCurrentSettings(normalizedProbabilities);
					break;
				}
				if(newPage.isProbabilityPanelEditable()) {
					probabilityEntryPanel.showEditableProbabilities();
				} else {
					probabilityEntryPanel.showConfirmedProbabilities();
				}
				conditionPanel.setInstructionsWidget(probabilityEntryPanel.getComponent());
			} else if(newPage.getInputPanelType() == InputType.TroopAllocationPanel) {
				//Show a troop allocation panel				
				if(troopAllocationPanel == null) {
					createTroopAllocationPanel();
				}				
				if(groups != null) {
					setGroupsForProbabilityEntryPanel(groups, troopAllocationPanel);					
				} else {
					troopAllocationPanel.setProbabilityEntryTitles(titles);
					troopAllocationPanel.restoreDefaultProbabilityEntryColors();
				}
				switch(newPage.getProbabilityType()) {
				case Initial:
					troopAllocationPanel.setCurrentSettings(initialProbabilities);
					troopAllocationPanel.setPreviousSettings(initialProbabilities);
					break;
				case Current:
					troopAllocationPanel.setCurrentSettings(currentProbabilities);
					troopAllocationPanel.setPreviousSettings(initialProbabilities);
					break;
				case Normalized:
					troopAllocationPanel.setCurrentSettings(normalizedProbabilities);
					break;
				}
				if(newPage.isProbabilityPanelEditable()) {
					troopAllocationPanel.showEditableProbabilities();
				} else {
					troopAllocationPanel.showConfirmedProbabilities();
				}
				conditionPanel.setInstructionsWidget(troopAllocationPanel.getComponent());
			} else if(newPage.getInputPanelType() == InputType.TroopSelectionPanel) {
				//Show a "forced choice" troop selection panel
				if(troopSelectionPanel == null) {
					troopSelectionPanel = new TroopSelectionPanel(groups);						
				} else {
					troopSelectionPanel.setGroups(groups);						
				}
				conditionPanel.setInstructionsWidget(troopSelectionPanel);									
			} else if(newPage.getInputPanelType() == InputType.SurprisePanel) {
				//Show a surprise entry panel
				if(surprisePanel == null) {
					createSurpriseEntryPanel();
				}
				conditionPanel.setInstructionsWidget(surprisePanel);
			}
		} else {
			conditionPanel.setInstructionsWidget(null);
		}
	}
	
	private void createProbabilityEntryPanel() {
		List<String> titles = null;
		if(condition.getProbabilityEntryGroups() != null) {
			titles = new ArrayList<String>(condition.getProbabilityEntryGroups().size());
			for(GroupType group : condition.getProbabilityEntryGroups()) {
				titles.add(group.toString());
			}
		} else {
			titles = condition.getProbabilityEntryTitles();
		}
		probabilityEntryPanel = ProbabilityEntryComponentFactory.createDefaultProbabilityEntryComponent(
				titles, condition.isAutoNormalizeProbabilities());
		probabilityEntryPanel.setTopTitle("your probabilities");
		probabilityEntryPanel.getComponent().setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(4, 4, 12, 4), BorderFactory.createLineBorder(Color.black)));
		probabilityEntryPanel.getComponent().setBackground(Color.white);
	}
	
	private void setGroupsForProbabilityEntryPanel(List<GroupType> groups, IProbabilityEntryContainer probabilityEntryPanel) {
		List<String> groupNames = new ArrayList<String>(groups.size());
		for(GroupType group : groups) {
			groupNames.add(group.toString());
		}
		probabilityEntryPanel.setProbabilityEntryTitles(groupNames);
		int index = 0;
		for(GroupType group : groups) {
			probabilityEntryPanel.setProbabilityEntryTitleColor(index, ColorManager_Phase1.getGroupCenterColor(group));
			if(WidgetConstants.USE_GROUP_SYMBOLS) {
				probabilityEntryPanel.setProbabilityEntryTitleIcon(index, ImageManager_Phase1.getGroupSymbolImageIcon(group, IconSize.Large));
			}
			if(WidgetConstants.USE_GROUP_COLORS) {
				probabilityEntryPanel.setProbabilityEntryColor(index, ColorManager_Phase1.getGroupCenterColor(group));
			}
			index++;
		}
	}
	
	private void createTroopAllocationPanel() {
		List<String> titles = null;
		if(condition.getProbabilityEntryGroups() != null) {
			titles = new ArrayList<String>(condition.getProbabilityEntryGroups().size());
			for(GroupType group : condition.getProbabilityEntryGroups()) {
				titles.add(group.toString());
			}
		} else {
			titles = condition.getProbabilityEntryTitles();
		}
		troopAllocationPanel = ProbabilityEntryComponentFactory.createDefaultProbabilityEntryComponent(
				titles, condition.isAutoNormalizeProbabilities(), "0%", "100%");
		troopAllocationPanel.setTopTitle("troop allocation");
		troopAllocationPanel.getComponent().setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(4, 4, 12, 4), BorderFactory.createLineBorder(Color.black)));
		troopAllocationPanel.getComponent().setBackground(Color.white);		
	}
	
	private void createSurpriseEntryPanel() {
		surprisePanel = new SurpriseEntryPanel(0, 6, 1);
		surprisePanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(4, 4, 12, 4), BorderFactory.createLineBorder(Color.black)));
		surprisePanel.setBackground(Color.white);		
	}	
	
	@Override
	protected void performCleanup() {
		//Does nothing
	}

	/**
	 * Panel for showing a troop selection (forced choice) example.
	 * 
	 * @author CBONACETO
	 *
	 */
	protected class TroopSelectionPanel extends JPanel implements ActionListener {		
		private static final long serialVersionUID = 1L;

		GroupSelectionPanel groupSelectionPanel;
		
		IProbabilityEntryContainer troopAllocationsComponent;
		
		public TroopSelectionPanel(List<GroupType> groups) {
			super(new GridBagLayout());
			setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createEmptyBorder(4, 4, 12, 4), BorderFactory.createLineBorder(Color.black)));
			setBackground(Color.WHITE);
			
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx = 0; 
			gbc.gridy = 0;
			gbc.weightx = 1;
			groupSelectionPanel = new GroupSelectionPanel("troopSelection");
			groupSelectionPanel.setGroups(groups);			
			groupSelectionPanel.addButtonActionListener(this);
			groupSelectionPanel.setBackground(Color.white);		
			gbc.anchor = GridBagConstraints.WEST;			
			add(groupSelectionPanel, gbc);			
			
			List<String> titles = null;
			if(condition.getProbabilityEntryGroups() != null) {
				titles = new ArrayList<String>(condition.getProbabilityEntryGroups().size());
				for(GroupType group : condition.getProbabilityEntryGroups()) {
					titles.add(group.toString());
				}
			} else {
				titles = condition.getProbabilityEntryTitles();
			}
			troopAllocationsComponent = ProbabilityEntryComponentFactory.createDefaultPreviousSettingsComponent(titles, 
					"0%", "100%");
			troopAllocationsComponent.setCurrentSettings(ProbabilityUtils.createProbabilities(groups.size(), 0));
			troopAllocationsComponent.setTopTitle("your troop allocations");
			troopAllocationsComponent.getComponent().setBackground(Color.white);
			setGroupsForProbabilityEntryPanel(groups, troopAllocationsComponent);
			
			gbc.gridy++;
			gbc.insets.top = 15;
			gbc.anchor = GridBagConstraints.CENTER;
			add(troopAllocationsComponent.getComponent(), gbc);
		}
		
		public void setGroups(List<GroupType> groups) {
			groupSelectionPanel.removeButtonActionListener(this);
			groupSelectionPanel.setGroups(groups);			
			groupSelectionPanel.addButtonActionListener(this);
			if(groups != null) {
				setGroupsForProbabilityEntryPanel(groups, troopAllocationsComponent);
			} else {
				troopAllocationsComponent.setProbabilityEntryTitles(condition.getProbabilityEntryTitles());
			}	
			troopAllocationsComponent.setCurrentSettings(ProbabilityUtils.createProbabilities(groups.size(), 0));
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {			
			List<Integer> troopAllocations = troopAllocationsComponent.getCurrentSettings();
			GroupType selectedGroup = groupSelectionPanel.getSelectedGroup();			
			int selectedGroupIndex = -1;			
			if(selectedGroup != null) {
				selectedGroupIndex = groupSelectionPanel.getGroups().indexOf(selectedGroup);
			}
			for(int i=0; i<troopAllocations.size(); i++) {
				if(i == selectedGroupIndex) {
					troopAllocations.set(i, 100);
				} else {
					troopAllocations.set(i, 0);
				}
			}								
			troopAllocationsComponent.setCurrentSettings(troopAllocations);
		}
	}
}