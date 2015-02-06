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
package org.mitre.icarus.cps.app.widgets.phase_1.experiment;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JPanel;

import org.mitre.icarus.cps.app.widgets.WidgetConstants;
import org.mitre.icarus.cps.app.widgets.ImageManager.IconSize;
import org.mitre.icarus.cps.app.widgets.phase_1.ColorManager_Phase1;
import org.mitre.icarus.cps.app.widgets.phase_1.ImageManager_Phase1;
import org.mitre.icarus.cps.app.widgets.phase_1.SurpriseEntryPanel;
import org.mitre.icarus.cps.app.widgets.probability_entry.IProbabilityEntryContainer;
import org.mitre.icarus.cps.app.widgets.probability_entry.ProbabilityEntryComponentFactory;
import org.mitre.icarus.cps.app.widgets.probability_entry.ProbabilityEntryConstants;
import org.mitre.icarus.cps.app.widgets.probability_entry.ProbabilityEntryComponentFactory.ProbabilityEntryComponentType;
import org.mitre.icarus.cps.experiment_core.gui.IConditionComponent;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupType;

/**
 * Panel on the right of the experiment panel that contains sub-panels for the subject to enter
 * data (e.g., group probe panels, location probe panels, etc.).
 * 
 * @author CBONACETO
 *
 */
public class TaskInputPanel extends JPanel {
	
	private static final long serialVersionUID = 8344856086580105910L;
	
	/** The sub-panels in the task input panel.  The sub-panels are arranged in a vertical column */
	protected ArrayList<JPanel> subPanels;
	
	protected GridBagLayout gbl;
	
	/** Whether probabilities or troop allocations should be auto-normalized */
	protected boolean autoNormalize = false;
	
	/** The probability entry component type */
	protected ProbabilityEntryComponentType probsComponentType;
	
	/** The previous settings component type */
	protected ProbabilityEntryComponentType previousSettingsComponentType;
	
	/** The troop allocation component type */
	protected ProbabilityEntryComponentType troopAllocationComponentType;
	
	/** The previous troop allocation component type */
	protected ProbabilityEntryComponentType previousTroopAllocationComponentType;
	
	/** The group probe component */
	protected IProbabilityEntryContainer groupProbeComponent;	
	
	/** The location probe component */
	protected IProbabilityEntryContainer locationProbeComponent;
	
	/** Component with previous location or group probe settings */
	protected IProbabilityEntryContainer previousSettingsComponent;
	
	/** The multi-group troop allocation component */
	protected IProbabilityEntryContainer troopAllocationMultiGroupComponent;
	
	/** The multi-location troop allocation component */
	protected IProbabilityEntryContainer troopAllocationMultiLocationComponent;
	
	/** Component with previous troop allocations */
	protected IProbabilityEntryContainer previousTroopAllocationComponent;
	
	/** The surprise probe component */
	protected SurpriseEntryPanel surpriseEntryComponent;
	
	/** The layer selection panel component */
	protected LayerSelectionPanel<?> layerSelectionPanel;
	
	/** A custom task input component */
	protected IConditionComponent customComponent;
	
	/** Contains all components (mapped by id) */
	protected HashMap<String, IConditionComponent> components;
	
	/**
	 * 	 * 
	 * @param numSubPanels
	 */
	public TaskInputPanel(int numSubPanels) {
		this(numSubPanels, false);
	}
	
	/**
	 * @param numSubPanels
	 * @param autoNormalize
	 */
	public TaskInputPanel(int numSubPanels, boolean autoNormalize) {
		super();
		if(numSubPanels <= 0) {
			throw new IllegalArgumentException("Error creating TaskInputPanel: Must have 1 or more sub panels");
		}
		setLayout(gbl = new GridBagLayout());
		components = new HashMap<String, IConditionComponent>(6);
		this.autoNormalize = autoNormalize;
		probsComponentType = ProbabilityEntryComponentType.Boxes;
		previousSettingsComponentType = ProbabilityEntryComponentType.Boxes;
		troopAllocationComponentType = ProbabilityEntryComponentType.Boxes;
		previousTroopAllocationComponentType = ProbabilityEntryComponentType.Boxes;
		
		//Create the sub panels
		subPanels = new ArrayList<JPanel>(numSubPanels);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(3, 3, 3, 3);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		for(int i=0; i<numSubPanels; i++) {
			JPanel subPanel = new JPanel(new BorderLayout());
			subPanels.add(subPanel);
			//if(i == (numSubPanels - 1)) {
			//	gbc.weighty = 1;
			//}
			add(subPanel, gbc);
			gbc.gridy++;
		}
	}	
	
	public boolean isSubPanelVisible(int panelIndex) {
		if(panelIndex >=0 && panelIndex < subPanels.size()) {
			return subPanels.get(panelIndex).isVisible();
		}
		return false;
	}	
	
	public void setSubPanelVisible(int panelIndex, boolean visible) {
		setSubPanelVisible(panelIndex, visible, GridBagConstraints.NORTH);
	}
	
	public void setSubPanelVisible(int panelIndex, boolean visible, int anchor) {		
		if(panelIndex >=0 && panelIndex < subPanels.size()) {
			JPanel subPanel = subPanels.get(panelIndex);
			if(visible != subPanel.isVisible()) {
				subPanel.setVisible(visible);	
				if(visible) {
					GridBagConstraints gbc = gbl.getConstraints(subPanel);
					if(gbc != null) {
						gbc.anchor = anchor;
						gbl.setConstraints(subPanel, gbc);
					}
				}
				revalidate();
				repaint();
			}
		}
	}
	
	public void setAllSubPanelsVisible(boolean visible) {
		for(JPanel panel : subPanels) {
			panel.setVisible(visible);
			if(!visible) {
				panel.removeAll();
			}
		}
		revalidate();
	}
	
	public void setSubPanelComponent(int panelIndex, IConditionComponent component) {
		setSubPanelComponent(panelIndex, component, BorderLayout.CENTER);
	}
	
	public void setSubPanelComponent(int panelIndex, IConditionComponent component, String alignment) {
		if(panelIndex >=0 && panelIndex < subPanels.size()) {
			JPanel subPanel =  subPanels.get(panelIndex);
			if(component == null) {
				if(subPanel.getComponentCount() > 0) {
					subPanel.removeAll();
					revalidate();
				}
			}
			else {
				if(subPanel.getComponentCount() == 0 || 			
					subPanel.getComponent(0) != component) {
					subPanel.removeAll();
					subPanel.add(component.getComponent(), alignment);										
				}
				subPanel.revalidate();
				subPanel.repaint();
				revalidate();
			}
		}
	}
	
	public int getNumSubPanels() {
		return subPanels.size();
	}
	
	public ProbabilityEntryComponentType getProbsComponentType() {
		return probsComponentType;
	}

	public void setProbsComponentType(ProbabilityEntryComponentType probsComponentType) {
		this.probsComponentType = probsComponentType;
	}

	public ProbabilityEntryComponentType getPreviousSettingsComponentType() {
		return previousSettingsComponentType;
	}

	public void setPreviousSettingsComponentType(ProbabilityEntryComponentType previousSettingsComponentType) {
		this.previousSettingsComponentType = previousSettingsComponentType;
	}

	public ProbabilityEntryComponentType getTroopAllocationComponentType() {
		return troopAllocationComponentType;
	}

	public void setTroopAllocationComponentType(ProbabilityEntryComponentType troopAllocationComponentType) {
		this.troopAllocationComponentType = troopAllocationComponentType;
	}

	public ProbabilityEntryComponentType getPreviousTroopAllocationComponentType() {
		return previousTroopAllocationComponentType;
	}

	public void setPreviousTroopAllocationComponentType(
			ProbabilityEntryComponentType previousTroopAllocationComponentType) {
		this.previousTroopAllocationComponentType = previousTroopAllocationComponentType;
	}

	public IProbabilityEntryContainer getGroupProbeComponent() {
		return groupProbeComponent;
	}

	public void setGroupProbeComponent(IProbabilityEntryContainer groupProbeComponent) {
		if(groupProbeComponent != null && groupProbeComponent != this.groupProbeComponent) {
			groupProbeComponent.setComponentId("gp");
			components.put(groupProbeComponent.getComponentId(), groupProbeComponent);
			JPanel containingPanel = getContainingPanelForComponent(this.groupProbeComponent);
			this.groupProbeComponent = groupProbeComponent;
			if(containingPanel != null) {
				containingPanel.removeAll();
				containingPanel.add(groupProbeComponent.getComponent());
				revalidate();
			}
		}
	}		
	
	public IProbabilityEntryContainer setGroupsForGroupProbeComponent(Collection<GroupType> groups) {
		groupProbeComponent = setGroupsForProbabilityEntryComponent(groups, groupProbeComponent, "gp", 
				probsComponentType, false, false);	
		return groupProbeComponent;
	}	
	
	public IProbabilityEntryContainer getLocationProbeComponent() {
		return locationProbeComponent;
	}

	public void setLocationProbeComponent(IProbabilityEntryContainer locationProbeComponent) {
		if(locationProbeComponent != null && locationProbeComponent != this.locationProbeComponent) {
			locationProbeComponent.setComponentId("lp");
			components.put(locationProbeComponent.getComponentId(), locationProbeComponent);
			JPanel containingPanel = getContainingPanelForComponent(this.locationProbeComponent);
			this.locationProbeComponent = locationProbeComponent;
			if(containingPanel != null) {
				containingPanel.removeAll();
				containingPanel.add(locationProbeComponent.getComponent());
				revalidate();
			}
		}
	}		
	
	public IProbabilityEntryContainer setLocationsForLocationProbeComponent(List<String> locations) {
		locationProbeComponent = setLocationsForProbabilityEntryComponent(locations, locationProbeComponent, "lp", 
				probsComponentType, false, false);
		return locationProbeComponent;
	}	
	
	public IProbabilityEntryContainer getPreviousSettingsComponent() {
		return previousSettingsComponent;
	}

	public void setPreviousSettingsComponent(IProbabilityEntryContainer previousSettingsComponent) {
		if(previousSettingsComponent != null && previousSettingsComponent != this.previousSettingsComponent) {
			previousSettingsComponent.setComponentId("ps");
			previousSettingsComponent.setTopTitle("your previous probabilities");
			previousSettingsComponent.setSumVisible(false);
			components.put(previousSettingsComponent.getComponentId(), previousSettingsComponent);
			JPanel containingPanel = getContainingPanelForComponent(this.previousSettingsComponent);
			this.previousSettingsComponent = previousSettingsComponent;
			if(containingPanel != null) {
				containingPanel.removeAll();
				containingPanel.add(previousSettingsComponent.getComponent());
				revalidate();
			}
		}
	}
	
	public IProbabilityEntryContainer setGroupsForPreviousSettingsComponent(Collection<GroupType> groups) {
		previousSettingsComponent = setGroupsForProbabilityEntryComponent(groups, previousSettingsComponent, "ps", 
				previousSettingsComponentType, true, false);
		if(previousSettingsComponent != null) {
			previousSettingsComponent.setComponentId("ps");
			previousSettingsComponent.setTopTitle("your previous probabilities");			
		}
		return previousSettingsComponent;
	}
	
	public IProbabilityEntryContainer setLocationsForPreviousSettingsComponent(List<String> locations) {
		previousSettingsComponent = setLocationsForProbabilityEntryComponent(locations, previousSettingsComponent, "ps", 
				previousSettingsComponentType, true, false);
		if(previousSettingsComponent != null) {
			for(int index = 0; index < locations.size(); index++) {
				previousSettingsComponent.setProbabilityEntryTitleColor(index, Color.BLACK);
				if(previousSettingsComponent.getProbabilityEntryTitleIcon(index) != null) {
					previousSettingsComponent.setProbabilityEntryTitleIcon(index, null);
				}
			}
			previousSettingsComponent.setComponentId("ps");
			previousSettingsComponent.setTopTitle("your previous probabilities");
			if(WidgetConstants.USE_GROUP_COLORS) {
				previousSettingsComponent.restoreDefaultProbabilityEntryColors();
			}
		}
		return previousSettingsComponent;
	}	
	
	public IProbabilityEntryContainer getTroopAllocationMultiGroupComponent() {
		return troopAllocationMultiGroupComponent;
	}

	public void setTroopAllocationMultiGroupComponent(IProbabilityEntryContainer troopAllocationMultiGroupComponent) {
		if(troopAllocationMultiGroupComponent != null && 
				troopAllocationMultiGroupComponent != this.troopAllocationMultiGroupComponent) {
			troopAllocationMultiGroupComponent.setComponentId("tp_mg");
			troopAllocationMultiGroupComponent.setTopTitle("your troop allocations");
			components.put(troopAllocationMultiGroupComponent.getComponentId(), troopAllocationMultiGroupComponent);
			JPanel containingPanel = getContainingPanelForComponent(this.troopAllocationMultiGroupComponent);
			this.troopAllocationMultiGroupComponent = troopAllocationMultiGroupComponent;			
			if(containingPanel != null) {
				containingPanel.removeAll();
				containingPanel.add(troopAllocationMultiGroupComponent.getComponent());
				revalidate();
			}
		}
	}

	public IProbabilityEntryContainer setGroupsForTroopAllocationMultiGroupComponent(Collection<GroupType> groups) {
		troopAllocationMultiGroupComponent = setGroupsForProbabilityEntryComponent(groups, troopAllocationMultiGroupComponent, "tp_mg", 
				troopAllocationComponentType, false, true);
		if(troopAllocationMultiGroupComponent != null) {
			troopAllocationMultiGroupComponent.setTopTitle("troop allocation");
		}
		return troopAllocationMultiGroupComponent;
	}
	
	public IProbabilityEntryContainer getTroopAllocationMultiLocationComponent() {
		return troopAllocationMultiLocationComponent;
	}

	public void setTroopAllocationMultiLocationComponent(IProbabilityEntryContainer troopAllocationMultiLocationComponent) {	
		if(troopAllocationMultiLocationComponent != null && 
				troopAllocationMultiLocationComponent != this.troopAllocationMultiLocationComponent) {
			troopAllocationMultiLocationComponent.setComponentId("tp_ml");
			troopAllocationMultiLocationComponent.setTopTitle("your troop allocations");
			components.put(troopAllocationMultiLocationComponent.getComponentId(), troopAllocationMultiLocationComponent);
			JPanel containingPanel = getContainingPanelForComponent(this.troopAllocationMultiLocationComponent);
			this.troopAllocationMultiLocationComponent = troopAllocationMultiLocationComponent;
			if(containingPanel != null) {
				containingPanel.removeAll();
				containingPanel.add(troopAllocationMultiLocationComponent.getComponent());
				revalidate();
			}
		}
	}

	public IProbabilityEntryContainer setLocationsForTroopAllocationMultiLocationComponent(List<String> locations) {
		troopAllocationMultiLocationComponent = setLocationsForProbabilityEntryComponent(locations, troopAllocationMultiLocationComponent, "tp_ml", 
				troopAllocationComponentType, false, true);		
		if(troopAllocationMultiLocationComponent != null) {
			troopAllocationMultiLocationComponent.setTopTitle("your troop allocations");
		}
		return troopAllocationMultiLocationComponent;
	}
	
	public IProbabilityEntryContainer getPreviousTroopAllocationComponent() {
		return previousTroopAllocationComponent;
	}
	
	public void setPreviousTroopAllocationComponent(IProbabilityEntryContainer previousTroopAllocationComponent) {
		if(previousTroopAllocationComponent != null && previousTroopAllocationComponent != this.previousTroopAllocationComponent) {
			previousTroopAllocationComponent.setComponentId("pta");
			previousTroopAllocationComponent.setTopTitle("your troop allocations");
			previousTroopAllocationComponent.setSumVisible(false);
			components.put(previousTroopAllocationComponent.getComponentId(), previousTroopAllocationComponent);
			JPanel containingPanel = getContainingPanelForComponent(this.previousTroopAllocationComponent);
			this.previousTroopAllocationComponent = previousTroopAllocationComponent;
			if(containingPanel != null) {
				containingPanel.removeAll();
				containingPanel.add(previousTroopAllocationComponent.getComponent());
				revalidate();
			}
		}
	}
	
	public IProbabilityEntryContainer setGroupsForPreviousTroopAllocationComponent(Collection<GroupType> groups) {
		previousTroopAllocationComponent = setGroupsForProbabilityEntryComponent(groups, previousTroopAllocationComponent, "pta", 
				previousTroopAllocationComponentType, true, true);		
		if(previousTroopAllocationComponent != null) {
			previousTroopAllocationComponent.setTopTitle("your troop allocations");
		}
		return previousTroopAllocationComponent;
	}
	
	public IProbabilityEntryContainer setLocationsForPreviousTroopAllocationComponent(List<String> locations) {
		previousTroopAllocationComponent = setLocationsForProbabilityEntryComponent(locations, previousTroopAllocationComponent, "pta", 
				previousTroopAllocationComponentType, true, true);		
		if(previousTroopAllocationComponent != null) {
			previousTroopAllocationComponent.setTopTitle("your troop allocations");
			for(int index = 0; index < locations.size(); index++) {
				previousTroopAllocationComponent.setProbabilityEntryTitleColor(index, Color.BLACK);
				if(previousTroopAllocationComponent.getProbabilityEntryTitleIcon(index) != null) {
					previousTroopAllocationComponent.setProbabilityEntryTitleIcon(index, null);
				}
			}	
			if(WidgetConstants.USE_GROUP_COLORS) {
				previousTroopAllocationComponent.restoreDefaultProbabilityEntryColors();
			}
		}		
		return previousTroopAllocationComponent;
	}
	
	public SurpriseEntryPanel getSurpriseEntryComponent() {
		return surpriseEntryComponent;
	}

	public void setSurpriseEntryComponent(SurpriseEntryPanel surpriseEntryComponent) {
		if(surpriseEntryComponent != null && surpriseEntryComponent != this.surpriseEntryComponent) {
			surpriseEntryComponent.setComponentId("se");
			components.put(surpriseEntryComponent.getComponentId(), surpriseEntryComponent);
			JPanel containingPanel = getContainingPanelForComponent(this.surpriseEntryComponent);
			this.surpriseEntryComponent = surpriseEntryComponent;
			if(containingPanel != null) {
				containingPanel.removeAll();
				containingPanel.add(surpriseEntryComponent.getComponent());
				revalidate();
			}
		}
	}
	
	public void clearSurpriseEntryComponentSelection() {
		if(surpriseEntryComponent != null) {
			surpriseEntryComponent.clearSelection();
		}
	}
	
	public SurpriseEntryPanel configureSurpriseEntryComponent(int minSurprise, int maxSurprise, int surpriseIncrement) {		
		if(surpriseEntryComponent == null) {
			surpriseEntryComponent = new SurpriseEntryPanel(minSurprise, maxSurprise, surpriseIncrement);
		}
		else if(surpriseEntryComponent.getMinSurprise() != minSurprise || 
				surpriseEntryComponent.getMaxSurprise() != maxSurprise ||
				surpriseEntryComponent.getSurpriseIncrement() != surpriseIncrement) {
			//We need to create a new surprise entry component
			JPanel containingPanel = getContainingPanelForComponent(surpriseEntryComponent);
			surpriseEntryComponent = new SurpriseEntryPanel(minSurprise, maxSurprise, surpriseIncrement);
			if(containingPanel != null) {
				//Add the surprise entry component back to its sub panel
				containingPanel.removeAll();
				containingPanel.add(surpriseEntryComponent);
				revalidate();
			}
		}
		surpriseEntryComponent.clearSelection();
		surpriseEntryComponent.setComponentId("se");
		components.put(surpriseEntryComponent.getComponentId(), surpriseEntryComponent);		
		return surpriseEntryComponent;
	}	

	public LayerSelectionPanel<?> getLayerSelectionPanel() {
		return layerSelectionPanel;
	}

	public void setLayerSelectionPanel(LayerSelectionPanel<?> layerSelectionPanel) {
		layerSelectionPanel.setComponentId("layers");
		components.put(layerSelectionPanel.getComponentId(), layerSelectionPanel);
		this.layerSelectionPanel = layerSelectionPanel;
	}

	public IConditionComponent getCustomComponent() {
		return customComponent;
	}

	public void setCustomComponent(IConditionComponent customComponent) {
		customComponent.setComponentId("cust");
		components.put(customComponent.getComponentId(), customComponent);
		this.customComponent = customComponent;
	}
	
	public Collection<IConditionComponent> getPanelComponents() {
		return components.values();
	}
	
	/**
	 * @param groups
	 * @param component
	 * @param componentId
	 * @param componentType
	 * @param previousSettingsComponent
	 * @param troopProbe
	 * @return
	 */
	protected IProbabilityEntryContainer setGroupsForProbabilityEntryComponent(Collection<GroupType> groups, 
			IProbabilityEntryContainer component, String componentId, ProbabilityEntryComponentType componentType,
			boolean previousSettingsComponent, boolean troopProbe) {
		if(groups != null && !groups.isEmpty()) {
			ArrayList<String> titles = new ArrayList<String>(groups.size());
			for(GroupType group : groups) {
				titles.add(group.toString());
			}
			component = setProbabilityEntryTitlesForComponent(component, titles, componentType, previousSettingsComponent, troopProbe);
			component.setComponentId(componentId);
			int index = 0;
			for(GroupType group : groups) {		
				//Set the title color to the group color
				component.setProbabilityEntryTitleColor(index, ColorManager_Phase1.getGroupCenterColor(group));
				if(WidgetConstants.USE_GROUP_SYMBOLS) {
					//Set the group symbol icon
					Icon icon = ImageManager_Phase1.getGroupSymbolImageIcon(group, IconSize.Large);
					if(icon != component.getProbabilityEntryTitleIcon(index)) {
						component.setProbabilityEntryTitleIcon(index, icon);
					}
				}
				if(WidgetConstants.USE_GROUP_COLORS) {
					component.setProbabilityEntryColor(index, ColorManager_Phase1.getGroupCenterColor(group));
				} 
				index++;
			}			
			components.put(component.getComponentId(), component);
		}
		return component;
	}
	
	/**
	 * @param locations
	 * @param component
	 * @param componentId
	 * @param componentType
	 * @param previousSettingsComponent
	 * @param troopProbe
	 * @return
	 */
	protected IProbabilityEntryContainer setLocationsForProbabilityEntryComponent(List<String> locations, IProbabilityEntryContainer component, 
			String componentId, ProbabilityEntryComponentType componentType,
			boolean previousSettingsComponent, boolean troopProbe) {
		if(locations != null && !locations.isEmpty()) {
			component = setProbabilityEntryTitlesForComponent(component, locations, componentType, previousSettingsComponent, troopProbe);
			component.setComponentId(componentId);
			components.put(component.getComponentId(), component);
		}
		return component;
	}
	
	/**
	 * @param component
	 * @param titles
	 * @param componentType
	 * @param previousSettingsComponent
	 * @param troopProbe
	 * @return
	 */
	protected IProbabilityEntryContainer setProbabilityEntryTitlesForComponent(IProbabilityEntryContainer component, 
			List<String> titles, ProbabilityEntryComponentType componentType, 
			boolean previousSettingsComponent, boolean troopProbe) {
		String minPercentLabel = troopProbe ? "0%" : ProbabilityEntryConstants.minPercentLabel;
		String maxPercentLabel = troopProbe ? "100%" : ProbabilityEntryConstants.maxPercentLabel;
		if(component == null) {						
			if(previousSettingsComponent) {
				component = ProbabilityEntryComponentFactory.creatProbabilityEntryComponent(componentType, 
						titles, autoNormalize, false, true, false, 
						minPercentLabel, maxPercentLabel);
				//component = ProbabilityEntryComponentFactory.createDefaultPreviousSettingsComponent(titles, minPercentLabel, maxPercentLabel);
			} else {
				component = ProbabilityEntryComponentFactory.creatProbabilityEntryComponent(componentType, 
						titles, autoNormalize, true, true, true, 
						minPercentLabel, maxPercentLabel);
				//component = ProbabilityEntryComponentFactory.createDefaultProbabilityEntryComponent(titles, autoNormalize,
				//		minPercentLabel, maxPercentLabel);
			}
		} else {
			List<String> currentTitles = component.getProbabilityEntryTitles();				
			if(currentTitles == null || currentTitles.size() != titles.size()) {
				//We need to create a new probability entry component
				JPanel containingPanel = this.getContainingPanelForComponent(component);
				if(previousSettingsComponent) {
					component = ProbabilityEntryComponentFactory.creatProbabilityEntryComponent(componentType, 
							titles, autoNormalize, false, true, false, 
							minPercentLabel, maxPercentLabel);
					//component = ProbabilityEntryComponentFactory.createDefaultPreviousSettingsComponent(titles, minPercentLabel, maxPercentLabel);
				} else {
					component = ProbabilityEntryComponentFactory.creatProbabilityEntryComponent(componentType, 
							titles, autoNormalize, true, true, true, 
							minPercentLabel, maxPercentLabel);
					//component = ProbabilityEntryComponentFactory.createDefaultProbabilityEntryComponent(titles, autoNormalize,
					//		minPercentLabel, maxPercentLabel);
				}
				if(containingPanel != null) {
					//Add the updated probability entry component back to its sub panel
					containingPanel.removeAll();
					containingPanel.add(component.getComponent());
					containingPanel.revalidate();
					containingPanel.repaint();
					revalidate();
				}
			}
			else {
				//We can update the existing probability entry component with the current titles
				component.setProbabilityEntryTitles(titles);
				if(previousSettingsComponent) {
					component.setSumVisible(false);
				}
			}
		}		
		return component;
	}
	
	protected JPanel getContainingPanelForComponent(IConditionComponent component) {
		if(component != null && component.getComponent() != null) {
			for(JPanel panel : subPanels) {
				if(panel.getComponentCount() > 0 && panel.getComponent(0) == component.getComponent()) {
					return panel;
				}
			}
		}
		return null;
	}
}