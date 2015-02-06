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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.mitre.icarus.cps.app.widgets.WidgetConstants;
import org.mitre.icarus.cps.app.widgets.basic.BasicConditionPanel;
import org.mitre.icarus.cps.app.widgets.basic.BreakPanel;
import org.mitre.icarus.cps.app.widgets.map.phase_1.MapConstants_Phase1;
import org.mitre.icarus.cps.app.widgets.map.phase_1.MapPanelContainer;
import org.mitre.icarus.cps.experiment_core.gui.IConditionComponent;
import org.mitre.icarus.cps.experiment_core.gui.JPanelConditionComponent;
import org.mitre.icarus.cps.feature_vector.phase_1.GridSize;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupType;

/**
 * Condition panel for task phases in the exam.
 * 
 * @author Craig Bonaceto
 */
public class ConditionPanel_Phase1 extends BasicConditionPanel {
	private static final long serialVersionUID = 1L;	
	
	/** The map panel (contains layers panel, legend panel, and map) */
	protected MapPanelContainer mapPanel;
		
	/** The task input panel */
	protected TaskInputPanel taskInputPanel;	

	public ConditionPanel_Phase1(Component parent) {
		this(parent, false, WidgetConstants.BANNER_ORIENTATION, 4);
	}
	
	public ConditionPanel_Phase1(Component parent, boolean showInstructionBanner, BannerOrientation bannerOrientation) {		
		super(parent, showInstructionBanner, bannerOrientation);
		createPanel(createMapPanel(parent), new TaskInputPanel(4), new BreakPanel());
	}	
	
	public ConditionPanel_Phase1(Component parent, boolean showInstructionBanner, BannerOrientation bannerOrientation,
			int instructionBannerTextHeight) {		
		super(parent, showInstructionBanner, bannerOrientation, instructionBannerTextHeight);
		createPanel(createMapPanel(parent), new TaskInputPanel(4), new BreakPanel());
	}
	
	protected ConditionPanel_Phase1(Component parent, boolean showInstructionBanner, BannerOrientation bannerOrientation,
			int instructionBannerTextHeight, MapPanelContainer mapPanel, TaskInputPanel taskInputPanel, BreakPanel breakPanel) {
		super(parent, showInstructionBanner, bannerOrientation, instructionBannerTextHeight);
		createPanel(mapPanel, taskInputPanel, breakPanel);
	}
	
	protected MapPanelContainer createMapPanel(Component parent) {
		MapPanelContainer mapPanel = new MapPanelContainer(parent, new GridSize(), true, true);		
		mapPanel.setMapPreferredSize(new Dimension(MapConstants_Phase1.PREFERRED_MAP_WIDTH, 
				MapConstants_Phase1.PREFERRED_MAP_HEIGHT), new Insets(1, 1, 1, 1));
		mapPanel.setMinimumSize(mapPanel.getPreferredSize());
		return mapPanel;
	}
	
	protected void createPanel(MapPanelContainer mapPanel, TaskInputPanel taskInputPanel, BreakPanel breakPanel) {
		this.breakPanel = breakPanel;

		contentPanel = new JPanelConditionComponent("content");
		contentPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0; 
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;		
		
		//Add the map panel
		this.mapPanel = mapPanel;
		if(mapPanel != null) {
			contentPanel.add(mapPanel, gbc);
		}
		
		//Add the task input panel
		this.taskInputPanel = taskInputPanel;
		if(taskInputPanel != null) {
			taskInputPanel.setBorder(WidgetConstants.DEFAULT_BORDER);
			gbc.weightx = 0;
			gbc.gridx++;
			contentPanel.add(taskInputPanel, gbc);
		}
		
		contentPanel.setPreferredSize(computeMaxSize());		
		setConditionComponents(Collections.singleton((IConditionComponent) contentPanel));		
		
		showBlankPage();
	}

	public void updatePreferredSize() {
		//System.out.println("Previous content panel preferred size: " + contentPanel.getPreferredSize() + ", previous size: " + getPreferredSize());
		contentPanel.setPreferredSize(null);
		contentPanel.setPreferredSize(computeMaxSize());
		setConditionComponents(Collections.singleton((IConditionComponent) contentPanel));
		//System.out.println("New content panel preferred size: " + contentPanel.getPreferredSize() + ", new size: " + getPreferredSize());
	}

	/** Compute the maximum preferred size of the panel */
	protected Dimension computeMaxSize() {
		//Compute the size of the largest task input sub-panel as it would be configured for Task 2 or 3
		// (a troop selection probe, previous probabilities showing)
		Dimension maxPanelSize = new Dimension(1, 1);
		ArrayList<GroupType> groups = new ArrayList<GroupType>(Arrays.asList(GroupType.A, GroupType.B, GroupType.C, GroupType.D));
		ArrayList<String> locations = new ArrayList<String>(Arrays.asList("1", "2", "3", "4"));
		taskInputPanel.setPreferredSize(null);
		taskInputPanel.setGroupsForPreviousSettingsComponent(groups);
		taskInputPanel.setGroupsForPreviousTroopAllocationComponent(groups);
		taskInputPanel.setGroupsForGroupProbeComponent(groups);
		taskInputPanel.setGroupsForTroopAllocationMultiGroupComponent(groups);
		taskInputPanel.setLocationsForLocationProbeComponent(locations);
		taskInputPanel.setLocationsForTroopAllocationMultiLocationComponent(locations);
		taskInputPanel.configureSurpriseEntryComponent(0, 6, 1);
		for(IConditionComponent component : taskInputPanel.getPanelComponents()) {
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
		
		//Show a troop selection probe and the previous settings, and compute the size
		GroupSelectionPanel groupSelectionPanel = new GroupSelectionPanel("groups");
		groupSelectionPanel.setGroups(groups);		
		taskInputPanel.setSubPanelComponent(0, groupSelectionPanel, BorderLayout.CENTER);
		taskInputPanel.setSubPanelComponent(1, taskInputPanel.getPreviousTroopAllocationComponent());
		taskInputPanel.setSubPanelComponent(2, taskInputPanel.getPreviousSettingsComponent());
		taskInputPanel.setSubPanelVisible(0, true);
		taskInputPanel.setSubPanelVisible(1, true);
		taskInputPanel.setSubPanelVisible(2, true);				
		int maxWidth = contentPanel.getPreferredSize().width;
		int taskInputPanelWidth = taskInputPanel.getPreferredSize().width;
		Dimension maxSize = new Dimension(maxWidth, contentPanel.getPreferredSize().height);
		taskInputPanel.setPreferredSize(new Dimension(taskInputPanelWidth, 
				maxSize.height));
		taskInputPanel.setAllSubPanelsVisible(false);
		//System.out.println("Max Size: (" + maxSize.width + "," + maxSize.height + ")");
		return new Dimension(maxSize.width + 1, maxSize.height + 1);
	}
	
	public void showTaskScreen() {
		setConditionComponent(contentPanel);
	}
	
	public MapPanelContainer getMapPanel() {
		return mapPanel;
	}
	
	public void setMapPanelVisible(boolean visible) {
		if(visible != mapPanel.isVisible()) {
			mapPanel.setVisible(visible);
			contentPanel.revalidate();
		}
	}

	public TaskInputPanel getTaskInputPanel() {
		return taskInputPanel;
	}
	
	public void setTaskInputPanelVisible(boolean visible) {
		if(visible != taskInputPanel.isVisible()) {
			taskInputPanel.setVisible(visible);
			contentPanel.revalidate();
		}
	}	
}