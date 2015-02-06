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
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.mitre.icarus.cps.app.widgets.WidgetConstants;
import org.mitre.icarus.cps.app.widgets.phase_2.WidgetConstants_Phase2;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.BlueAction.BlueActionType;
import org.mitre.icarus.cps.experiment_core.gui.JPanelConditionComponent;

/**
 * Panel with controls for selecting a Blue action at one or more locations.
 * 
 * @author CBONACETO
 *
 */
public class ActionSelectionPanel extends JPanelConditionComponent {
	private static final long serialVersionUID = -3681949096941547621L;	

	/** The location IDs */
	protected Collection<String> locationIds;
	
	/** The action selection location panel(s) (mapped by location ID) */
	protected Map<String, ActionSelectionAtLocationPanel> locationPanels;	
	
	/** Whether the action selection panel(s) are editable (enabled) */
	protected boolean editable = true;
	
	public ActionSelectionPanel(String componentId) {
		super(componentId);
		setLayout(new GridBagLayout());
		setFont(WidgetConstants.FONT_DEFAULT);
	}
	
	public void setLocations(Collection<String> locationNames, Collection<String> locationIds) {		
		//Add an action selection location panel for each location
		if(locationPanels == null) {
			locationPanels = new HashMap<String, ActionSelectionAtLocationPanel>();
		} 
		this.locationIds = locationIds;
		if(locationNames != null && !locationNames.isEmpty() && locationIds != null && !locationIds.isEmpty()) {
			if(locationNames.size() != locationIds.size()) {
				throw new IllegalArgumentException("Error, length of location names and location ID lists must be the same size.");
			}
			Iterator<String> nameIter = locationNames.iterator();
			Iterator<String> idIter = locationIds.iterator();
			/*if(locationNames.size() == locationPanels.size()) {				
				List<ActionSelectionAtLocationPanel> tempPanelsList = new LinkedList<ActionSelectionAtLocationPanel>();
				for(ActionSelectionAtLocationPanel locationPanel : locationPanels.values()) {
					tempPanelsList.add(locationPanel);
					locationPanel.setLocationName(nameIter.next());
					locationPanel.setLocationId(idIter.next());
					locationPanel.clearActionSelection();
				}
				removeAll();
				locationPanels.clear();
				for(ActionSelectionAtLocationPanel locationPanel : tempPanelsList) {
					locationPanels.put(locationPanel.getLocationId(), locationPanel);

				}
			} else {*/
			removeAll();
			locationPanels.clear();
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.weighty = 0;
			gbc.anchor = GridBagConstraints.NORTHWEST;
			gbc.insets.bottom = 24; //WidgetConstants_Phase2.DATUM_LIST_VERTICAL_SPACER;
			Color background = getBackground();
			Font font = getFont();
			while(nameIter.hasNext()) {
				ActionSelectionAtLocationPanel locationPanel = new ActionSelectionAtLocationPanel(nameIter.next(), idIter.next());
				locationPanel.setBackground(background);
				locationPanel.setFont(font);
				locationPanel.setEnabled(editable);
				gbc.weightx = 0;
				if(gbc.gridy == locationNames.size() - 1) {
					gbc.weighty = 1;
					gbc.insets.bottom = 0;
				}
				add(locationPanel, gbc);
				locationPanels.put(locationPanel.getLocationId(), locationPanel);
				gbc.gridy++;				
			}		
			//}
		} else {
			removeAll();
			locationPanels.clear();
		}
	}

	public boolean isEditable() {
		return editable;
	}
	
	public void setEditable(boolean editable) {
		if(editable != this.editable) {
			this.editable = editable;
			if(locationPanels != null && !locationPanels.isEmpty()) {
				for(ActionSelectionAtLocationPanel locationPanel : locationPanels.values()) {
					locationPanel.setEnabled(editable);
				}
			}
		}
	}
	
	@Override
	public void setBackground(Color bg) {
		super.setBackground(bg);
		if(locationPanels != null && !locationPanels.isEmpty()) {
			for(ActionSelectionAtLocationPanel locationPanel : locationPanels.values()) {
				locationPanel.setBackground(bg);
			}
		}
	}	

	@Override
	public void setFont(Font font) {
		super.setFont(font);
		if(locationPanels != null && !locationPanels.isEmpty()) {
			for(ActionSelectionAtLocationPanel locationPanel : locationPanels.values()) {
				locationPanel.setFont(font);
			}
		}
	}

	public Collection<String> getLocationIds() {
		return locationIds;
	}
	
	public void clearActionSelections() {
		if(locationPanels != null && !locationPanels.isEmpty()) {
			for(ActionSelectionAtLocationPanel locationPanel : locationPanels.values()) {
				locationPanel.clearActionSelection();
			}
		}
	}
	
	/**
	 * @return
	 */
	public int getNumActionsSelected() {
		int numActionsSelected = 0;
		if(locationPanels != null && !locationPanels.isEmpty()) {
			for(ActionSelectionAtLocationPanel locationPanel : locationPanels.values()) {
				if(locationPanel.getActionSelection() != null) {
					numActionsSelected++;
				}
			}
		}		
		return numActionsSelected;		
	}
	
	public List<BlueActionType> getActionSelections() {
		if(locationPanels != null && !locationPanels.isEmpty()) {
			List<BlueActionType> actionSelections = new LinkedList<BlueActionType>();
			for(ActionSelectionAtLocationPanel locationPanel : locationPanels.values()) {
				actionSelections.add(locationPanel.getActionSelection());
			}
			return actionSelections;
		}
		return null;
	}

	public BlueActionType getActionSelection(String locationId) {
		if(locationPanels != null && !locationPanels.isEmpty()) {
			ActionSelectionAtLocationPanel locationPanel = locationPanels.get(locationId);
			return locationPanel != null ? locationPanel.getActionSelection() : null;
		}
		return null;
	}
	
	public void setActionSelection(String locationId, BlueActionType actionSelection) {
		if(locationPanels != null && !locationPanels.isEmpty()) {
			ActionSelectionAtLocationPanel locationPanel = locationPanels.get(locationId);
			if(locationPanel != null) {
				locationPanel.setActionSelection(actionSelection);
			}
		}
	}	
	
	public boolean isButtonActionListenerPresent(ActionListener l) {
		if(locationPanels != null && !locationPanels.isEmpty()) {
			ActionListener[] listeners = locationPanels.values().iterator().next().getActionListeners();
			if(listeners != null) {
				for(ActionListener listener : listeners) {
					if(l == listener) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public void addButtonActionListener(ActionListener l) {
		if(locationPanels != null && !locationPanels.isEmpty()) {
			for(ActionSelectionAtLocationPanel locationPanel : locationPanels.values()) {
				locationPanel.addActionListener(l);
			}
		}
	}
	
	public void removeButtonActionListener(ActionListener l) {
		if(locationPanels != null && !locationPanels.isEmpty()) {
			for(ActionSelectionAtLocationPanel locationPanel : locationPanels.values()) {
				locationPanel.removeActionListener(l);
			}
		}
	}
	
	/** Panel contains the location name label and the action selection radio buttons */
	protected static class ActionSelectionAtLocationPanel extends JPanel {
		private static final long serialVersionUID = 1L;

		/** The location ID this panel is for */
		private String locationId;
		
		/** The location name */
		protected String locationName;
		
		/** Location name label */
		protected JLabel locationNameLabel;
		
		/** The divert ratio button */
		protected JRadioButton divertButton;
		
		/** The do not divert ratio button */
		protected JRadioButton doNotDivertButton;
		
		/** The button group */
		protected ButtonGroup bg;
		
		public ActionSelectionAtLocationPanel(String locationName, String locationId) {
			super(new GridBagLayout());
			this.locationName = locationName;
			this.locationId = locationId;
			bg = new ButtonGroup();
			
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.insets.left = 2;
			gbc.insets.bottom = WidgetConstants_Phase2.DATUM_LIST_VERTICAL_SPACER;
			gbc.weightx = 1;
			gbc.weighty = 0;
			gbc.anchor = GridBagConstraints.NORTHWEST;
			
			//Add the location name label
			locationNameLabel = new JLabel("Blue action at " + locationName);
			add(locationNameLabel, gbc);
			
			//Add the Do Not Divert radio button
			doNotDivertButton = new JRadioButton("Not Divert");
			bg.add(doNotDivertButton);
			//doNotDivertButton.setActionCommand(locationId + "_" + BlueActionType.Do_Not_Divert);
			doNotDivertButton.setActionCommand(locationId);
			gbc.gridy++;
			gbc.insets.left = WidgetConstants_Phase2.DATUM_LIST_INDENT_LEVEL_2;
			add(doNotDivertButton, gbc);
			
			//Add the Divert radio button
			divertButton = new JRadioButton("Divert");
			bg.add(divertButton);
			//divertButton.setActionCommand(locationId + "_" + BlueActionType.Divert);
			divertButton.setActionCommand(locationId);
			gbc.gridy++;
			gbc.weighty = 1;
			gbc.insets.bottom = 0;
			add(divertButton, gbc);			
		}
		
		public void addActionListener(ActionListener l) {
			if(divertButton != null) {
				divertButton.addActionListener(l);
			}
			if(doNotDivertButton != null) {
				doNotDivertButton.addActionListener(l);
			}
		}
		
		public void removeActionListener(ActionListener l) {
			if(divertButton != null) {
				divertButton.removeActionListener(l);
			}
			if(doNotDivertButton != null) {
				doNotDivertButton.removeActionListener(l);
			}
		}
		
		public ActionListener[] getActionListeners() {
			return divertButton != null ? divertButton.getActionListeners() : null;
		}

		@Override
		public void setBackground(Color bg) {
			super.setBackground(bg);
			if(locationNameLabel != null) {
				locationNameLabel.setBackground(bg);
			}
			if(divertButton != null) {
				divertButton.setBackground(bg);
			}
			if(doNotDivertButton != null) {
				doNotDivertButton.setBackground(bg);
			}
		}				

		@Override
		public void setEnabled(boolean enabled) {
			super.setEnabled(enabled);
			if(divertButton != null) {
				divertButton.setEnabled(enabled);
			}
			if(doNotDivertButton != null) {
				doNotDivertButton.setEnabled(enabled);
			}
		}

		@Override
		public void setFont(Font font) {
			super.setFont(font);
			if(locationNameLabel != null) {
				locationNameLabel.setFont(font);
			}
			if(divertButton != null) {
				divertButton.setFont(font);
			}
			if(doNotDivertButton != null) {
				doNotDivertButton.setFont(font);
			}
		}
		
		public String getLocationName() {
			return locationName;
		}
		
		public void setLocationName(String locationName) {
			this.locationName = locationName;
			if(locationNameLabel != null) {
				locationNameLabel.setText("Blue action at " + locationName);
			}
		}
		
		public String getLocationId() {
			return locationId;
		}

		public void setLocationId(String locationId) {
			this.locationId = locationId;
			if(divertButton != null) {
				divertButton.setActionCommand(locationId);
			}
			if(doNotDivertButton != null) {
				doNotDivertButton.setActionCommand(locationId);
			}
		}
		
		public void clearActionSelection() {		
			bg.clearSelection();
			divertButton.setSelected(false);
			doNotDivertButton.setSelected(false);
		}
		
		public void setActionSelection(BlueActionType actionSelection) {
			bg.clearSelection();
			switch(actionSelection) {
			case Divert:
				divertButton.setSelected(true);
				doNotDivertButton.setSelected(false);
				break;
			case Do_Not_Divert:
				divertButton.setSelected(false);
				doNotDivertButton.setSelected(true);
				break;
			default:			
				divertButton.setSelected(false);
				doNotDivertButton.setSelected(false);
			}
		}

		/**
		 * @return
		 */
		public BlueActionType getActionSelection() {
			if(divertButton.isSelected()) {
				return BlueActionType.Divert;
			} else if(doNotDivertButton.isSelected()) {
				return BlueActionType.Do_Not_Divert;
			} else {
				return null;
			}
		}
	}
}