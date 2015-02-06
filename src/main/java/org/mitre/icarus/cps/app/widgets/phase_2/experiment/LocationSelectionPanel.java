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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;

import org.mitre.icarus.cps.app.widgets.WidgetConstants;
import org.mitre.icarus.cps.app.widgets.phase_2.WidgetConstants_Phase2;
import org.mitre.icarus.cps.experiment_core.gui.JPanelConditionComponent;

/**
 * Panel with controls for selecting one or more locations.
 * 
 * @author CBONACETO
 *
 */
public class LocationSelectionPanel extends JPanelConditionComponent {
	private static final long serialVersionUID = -3681949096941547621L;	

	/** The location IDs and associated indexes */
	protected Collection<LocationDescriptor> locations;
	
	/** The number of locations to select */
	protected int numLocationsToSelect;
	
	/** The radio buttons or check boxes for each location */
	protected List<LocationSelectionButton> locationButtons;
	
	/** The button group containing the buttons */
	protected ButtonGroup bg;
	
	/** Whether the buttons are editable (enabled) */
	protected boolean editable = true;
	
	public LocationSelectionPanel(String componentId) {
		super(componentId);
		setLayout(new GridBagLayout());
		setFont(WidgetConstants.FONT_DEFAULT);
	}
	
	public void setLocations(Collection<String> locationNames, Collection<String> locationIds, 
			Collection<Integer> locationIndexes, int numLocationsToSelect) {		
		//Add a radio button (single location select) or check box (multi-location select) for each location
		removeAll();
		if(locationButtons == null) {
			locationButtons = new ArrayList<LocationSelectionButton>();
		} else {
			locationButtons.clear();
		}
		bg = new ButtonGroup();
		//this.locationIds = locationIds;
		this.numLocationsToSelect = numLocationsToSelect;
		if(locationNames != null && !locationNames.isEmpty() && locationIds != null && !locationIds.isEmpty() &&
				locationIndexes != null && !locationIndexes.isEmpty()) {
			this.locations = new ArrayList<LocationDescriptor>(locationIds.size());
			if(locationNames.size() != locationIds.size() || locationNames.size() != locationIndexes.size()) {
				throw new IllegalArgumentException("Error, length of location names, location ID, and location index lists must be the same size.");
			}
			if(numLocationsToSelect < 1 || numLocationsToSelect > locationNames.size()) {
				throw new IllegalArgumentException("Error, the number of locations to select must be between 1 and the number of locations.");
			}
			LocationSelectionButton.ButtonType buttonType = numLocationsToSelect > 1 ? 
					LocationSelectionButton.ButtonType.CheckBox : LocationSelectionButton.ButtonType.Radio; 
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.weighty = 0;
			gbc.anchor = GridBagConstraints.NORTHWEST;
			gbc.insets.bottom = WidgetConstants_Phase2.DATUM_LIST_VERTICAL_SPACER;
			Color background = getBackground();
			Font font = getFont();
			Iterator<String> nameIter = locationNames.iterator();
			Iterator<String> idIter = locationIds.iterator();
			Iterator<Integer> indexIter = locationIndexes.iterator();
			while(nameIter.hasNext()) {
				LocationSelectionButton button = new LocationSelectionButton(nameIter.next(), idIter.next(), 
						indexIter.next(), buttonType);
				button.button.setBackground(background);
				button.button.setFont(font);
				button.button.setEnabled(editable);
				gbc.weightx = 0;
				if(gbc.gridy == locationNames.size() - 1) {
					gbc.weighty = 1;
					gbc.insets.bottom = 0;
				}
				add(button.button, gbc);
				bg.add(button.button);
				locationButtons.add(button);
				gbc.gridy++;				
			}			
		}	
	}
	
	public boolean isEditable() {
		return editable;
	}
	
	public void setEditable(boolean editable) {
		if(editable != this.editable) {
			this.editable = editable;
			if(locationButtons != null && !locationButtons.isEmpty()) {
				for(LocationSelectionButton button : locationButtons) {
					button.button.setEnabled(editable);
				}
			}
		}
	}
	
	@Override
	public void setBackground(Color bg) {
		super.setBackground(bg);
		if(locationButtons != null && !locationButtons.isEmpty()) {
			for(LocationSelectionButton button : locationButtons) {
				button.button.setBackground(bg);
			}
		}
	}	

	@Override
	public void setFont(Font font) {
		super.setFont(font);
		if(locationButtons != null && !locationButtons.isEmpty()) {
			for(LocationSelectionButton button : locationButtons) {
				button.button.setFont(font);
			}
		}
	}

	public Collection<LocationDescriptor> getLocations() {
		return locations;
	}

	public int getNumLocationsToSelect() {
		return numLocationsToSelect;
	}
	
	public int getNumLocationsSelected() {
		int numLocationsSelected = 0;
		if(locationButtons != null && !locationButtons.isEmpty()) {
			for(LocationSelectionButton button : locationButtons) {
				if(button.button.isSelected()) {
					numLocationsSelected++;
				}
			}
		}
		return numLocationsSelected;
	}
	
	public void clearLocationSelections() {
		bg.clearSelection();
		if(locationButtons != null && !locationButtons.isEmpty()) {
			for(LocationSelectionButton button : locationButtons) {
				button.button.setSelected(false);
			}
		}
	}

	public List<LocationDescriptor> getSelectedLocations() {
		if(locationButtons != null && !locationButtons.isEmpty()) {
			List<LocationDescriptor> locations = new LinkedList<LocationDescriptor>();
			for(LocationSelectionButton button : locationButtons) {
				if(button.button.isSelected()) {
					locations.add(button.getLocation());
				}
			}
			return locations;
		}
		return null;
	}
	
	public boolean isButtonActionListenerPresent(ActionListener l) {
		if(locationButtons != null && !locationButtons.isEmpty()) {
			ActionListener[] listeners = locationButtons.get(0).button.getActionListeners();
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
		if(locationButtons != null) {
			for(LocationSelectionButton button : locationButtons) {
				button.button.addActionListener(l);
			}
		}
	}
	
	public void removeButtonActionListener(ActionListener l) {
		if(locationButtons != null) {
			for(LocationSelectionButton button : locationButtons) {
				button.button.removeActionListener(l);
			}
		}
	}
	
	protected static class LocationSelectionButton {
		public static enum ButtonType {Radio, CheckBox};
		
		/** The location this button is for */
		protected LocationDescriptor location;		
		
		/** The button (radio button or check box) */
		protected JToggleButton button;
		
		public LocationSelectionButton(String locationName, String locationId, Integer locationIndex, ButtonType buttonType) {
			this.location = new LocationDescriptor(locationId, locationIndex);
			button = buttonType == ButtonType.Radio ? new JRadioButton(locationName) : new JCheckBox(locationName);
			button.setActionCommand(locationId);
		}

		public LocationDescriptor getLocation() {
			return location;
		}		
	}
}