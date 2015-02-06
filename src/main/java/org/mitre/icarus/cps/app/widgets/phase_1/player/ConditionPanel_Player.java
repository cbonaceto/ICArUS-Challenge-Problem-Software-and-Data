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
package org.mitre.icarus.cps.app.widgets.phase_1.player;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.mitre.icarus.cps.app.widgets.basic.BreakPanel;
import org.mitre.icarus.cps.app.widgets.map.phase_1.MapConstants_Phase1;
import org.mitre.icarus.cps.app.widgets.map.phase_1.MapPanelContainer;
import org.mitre.icarus.cps.app.widgets.phase_1.experiment.ConditionPanel_Phase1;
import org.mitre.icarus.cps.app.widgets.phase_1.experiment.GroupSelectionPanel;
import org.mitre.icarus.cps.experiment_core.gui.IConditionComponent;
import org.mitre.icarus.cps.feature_vector.phase_1.GridSize;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupType;

/**
 * @author CBONACETO
 *
 */
public class ConditionPanel_Player extends ConditionPanel_Phase1 {	
	private static final long serialVersionUID = 3206413947943800526L;
	
	/** The task response panel */
	protected TaskResponsePanel taskResponsePanel;

	public ConditionPanel_Player(Component parent) {
		super(parent, true, BannerOrientation.Top, 6, createMapPanel_Player(parent), 
				new TaskResponsePanel(4), new BreakPanel());
		taskResponsePanel = (TaskResponsePanel)taskInputPanel;
	}
	
	protected static MapPanelContainer createMapPanel_Player(Component parent) {
		MapPanelContainer mapPanel = new MapPanelContainer(parent, new GridSize(), false, true);
		mapPanel.setMapPreferredSize(new Dimension(MapConstants_Phase1.SMALL_MAP_WIDTH, 
				MapConstants_Phase1.SMALL_MAP_HEIGHT), new Insets(1, 1, 1, 1));
		mapPanel.setMinimumSize(mapPanel.getPreferredSize());		
		return mapPanel;
	}
	
	public void updatePreferredSize() {		
		contentPanel.setPreferredSize(null);
		contentPanel.setPreferredSize(computeMaxSize());
		setConditionComponents(Collections.singleton((IConditionComponent) contentPanel));
	}
	
	/** Compute the maximum preferred size of the panel */
	protected Dimension computeMaxSize() {
		//Compute the size of the largest task input sub-panel as it would be configured with the current probs, 
		//the previous probs, the normative probs, and the avg human probs showing.
		Dimension maxPanelSize = new Dimension(1, 1);
		ArrayList<GroupType> groups = new ArrayList<GroupType>(Arrays.asList(GroupType.A, GroupType.B, GroupType.C, GroupType.D));
		ArrayList<String> locations = new ArrayList<String>(Arrays.asList("1", "2", "3", "4"));
		if(taskResponsePanel == null) {
			taskResponsePanel = (TaskResponsePanel)taskInputPanel;
		}
		taskResponsePanel.setPreferredSize(null);
		taskResponsePanel.setGroupsForPreviousSettingsComponent(groups);
		taskResponsePanel.setGroupsForPreviousTroopAllocationComponent(groups);
		taskResponsePanel.setGroupsForGroupProbeComponent(groups);
		taskResponsePanel.setGroupsForNormativeProbsComponent(groups);
		taskResponsePanel.setGroupsForAvgHumanProbsComponent(groups);		
		taskResponsePanel.setGroupsForTroopAllocationMultiGroupComponent(groups);
		taskResponsePanel.setLocationsForLocationProbeComponent(locations);
		taskResponsePanel.setLocationsForTroopAllocationMultiLocationComponent(locations);		
		taskResponsePanel.configureSurpriseEntryComponent(0, 6, 1);		
		for(IConditionComponent component : taskResponsePanel.getPanelComponents()) {
			if(component != null && component.getComponent() != null) {				
				Dimension size = component.getComponent().getPreferredSize();
				//System.out.println(component.getComponentId() + ", " + size);
				if(size.width > maxPanelSize.width) {
					maxPanelSize.width = size.width;
				}
				if(size.height > maxPanelSize.height) {
					maxPanelSize.height = size.height;
				}
			}
		}
		
		//Show the current probs, previous probs, normative probs, and avg human probs components
		GroupSelectionPanel groupSelectionPanel = new GroupSelectionPanel("groups");
		groupSelectionPanel.setGroups(groups);		
		taskResponsePanel.setSubPanelComponent(0, taskResponsePanel.getGroupProbeComponent());
		taskResponsePanel.setSubPanelComponent(1, taskResponsePanel.getPreviousSettingsComponent());
		taskResponsePanel.setSubPanelComponent(2, taskResponsePanel.getAvgHumanProbsComponent());
		taskResponsePanel.setSubPanelComponent(3, taskResponsePanel.getNormativeProbsComponent());
		taskResponsePanel.setSubPanelVisible(0, true);
		taskResponsePanel.setSubPanelVisible(1, true);
		taskResponsePanel.setSubPanelVisible(2, true);		
		taskResponsePanel.setSubPanelVisible(3, true);
		
		int maxWidth = contentPanel.getPreferredSize().width;
		int taskResponsePanelWidth = taskResponsePanel.getPreferredSize().width;
		Dimension maxSize = new Dimension(maxWidth, contentPanel.getPreferredSize().height);
		taskResponsePanel.setPreferredSize(new Dimension(taskResponsePanelWidth, 
				maxSize.height));
		taskResponsePanel.setAllSubPanelsVisible(false);
		//System.out.println("Max Size: (" + maxSize.width + "," + maxSize.height + ")");
		return new Dimension(maxSize.width + 1, maxSize.height + 1);
	}
	
	public TaskResponsePanel getTaskResponsePanel() {
		return taskResponsePanel;
	}

	public void setTaskResponsePanelVisible(boolean visible) {
		super.setTaskInputPanelVisible(visible);
	}
}