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
package org.mitre.icarus.cps.app.widgets.phase_2.experiment.datum_list;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JPanel;

import org.mitre.icarus.cps.app.widgets.WidgetConstants;
import org.mitre.icarus.cps.app.widgets.phase_2.WidgetConstants_Phase2;
import org.mitre.icarus.cps.exam.phase_2.testing.DatumType;

/**
 * @author CBONACETO
 *
 */
public class MultiLocationDatumPanel extends JPanel {	
	private static final long serialVersionUID = 122718207310939258L;
	
	/** Datum panels for each location (mapped by location ID) */
	protected Map<String, LocationDatumPanel> locationDatumPanels;
	
	/** Indent levels */
	protected int indentLevel1;
	protected int indentLevel2;
	
	/** Whether to show location names */
	protected boolean showLocationNames;
	
	/** Whether to show datum check boxes */
	protected boolean showDatumCheckBoxes;
	
	public MultiLocationDatumPanel() {
		this(true, true, WidgetConstants_Phase2.DATUM_LIST_INDENT_LEVEL_1, WidgetConstants_Phase2.DATUM_LIST_INDENT_LEVEL_2);
	}
	
	public MultiLocationDatumPanel(boolean showLocationName, boolean showDatumCheckBoxes,
			int indentLevel1, int indentLevel2) {
		this(null, null, null, showLocationName, showDatumCheckBoxes, indentLevel1, indentLevel2);
	}
	
	public MultiLocationDatumPanel(Collection<String> locationNames, Collection<String> locationIds, 
			Collection<DatumListItem> datumItems) {
		this(locationNames, locationIds, datumItems, true, true, 5, 10);
	}

	public MultiLocationDatumPanel(Collection<String> locationNames, Collection<String> locationIds, 
			Collection<DatumListItem> datumItems, boolean showLocationNames, boolean showDatumCheckBoxes,
			int indentLevel1, int indentLevel2) {
		super(new GridBagLayout());
		this.indentLevel1 = indentLevel1;
		this.indentLevel2 = indentLevel2;
		this.showLocationNames = showLocationNames;
		this.showDatumCheckBoxes = showDatumCheckBoxes;
		if(locationNames != null) {
			createLocationDatumPanels(locationNames, locationIds, datumItems);
		}
	}	
	
	/**
	 * @param locationNames
	 * @param locationIds
	 * @param datumItems
	 */
	public void setLocations(Collection<String> locationNames, Collection<String> locationIds, 
			Collection<DatumListItem> datumItems) {
		createLocationDatumPanels(locationNames, locationIds, datumItems);
	}
	
	/** Creates location datum panels for each location. */
	protected void createLocationDatumPanels(Collection<String> locationNames, Collection<String> locationIds, 
			Collection<DatumListItem> datumItems) {
		removeAll();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.insets = WidgetConstants.INSETS_DEFAULT;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		if(locationDatumPanels == null) {
			locationDatumPanels = new HashMap<String, LocationDatumPanel>(locationNames.size());
		}
		if(locationNames != null) {
			Iterator<String> nameIter = locationNames.iterator();
			Iterator<String> idIter = locationIds.iterator();
			while(nameIter.hasNext()) {
				LocationDatumPanel locationDatumPanel = new LocationDatumPanel(nameIter.next(), datumItems, 
						showLocationNames, showDatumCheckBoxes, indentLevel1, indentLevel2);
				locationDatumPanel.setOpaque(isOpaque());
				locationDatumPanel.setBackground(getBackground());
				locationDatumPanels.put(idIter.next(), locationDatumPanel);
				boolean atEnd = !nameIter.hasNext();
				gbc.insets.bottom = atEnd ? 0 : WidgetConstants.COMPONENT_SPACER;
				gbc.weighty = atEnd ? 1 : 0;
				add(locationDatumPanel, gbc);
				gbc.gridy++;
			}
		}
		revalidate();
		repaint();
	}
	
	/**
	 * @param datumItem
	 */
	public void addDatumItem(DatumListItem datumItem) {
		if(!locationDatumPanels.isEmpty()) {
			for(LocationDatumPanel datumPanel : locationDatumPanels.values()) {
				datumPanel.addDatumItem(datumItem);
			}
		}
	}
	
	/**
	 * @param datumItems
	 */
	public void addDatumItems(Collection<DatumListItem> datumItems) {
		if(!locationDatumPanels.isEmpty()) {
			for(LocationDatumPanel datumPanel : locationDatumPanels.values()) {
				datumPanel.addDatumItems(datumItems);
			}
		}
	}
	
	/**
	 * @param locationId
	 * @param datumType
	 * @return
	 */
	public boolean containsDatumItem(String locationId, DatumType datumType) {
		LocationDatumPanel datumPanel = locationDatumPanels.get(locationId);
		if(datumPanel != null) {
			return datumPanel.containsDatumItem(datumType);
		}
		return false;
	}
	
	public String getDatumValue(String locationId, DatumType datumType) {
		LocationDatumPanel datumPanel = locationDatumPanels.get(locationId);
		if(datumPanel != null) {
			return datumPanel.getDatumValue(datumType);
		}
		return null;
	}
	
	public void setDatumValue(String locationId, DatumType datumType, String value) {
		LocationDatumPanel datumPanel = locationDatumPanels.get(locationId);
		if(datumPanel != null) {
			datumPanel.setDatumValue(datumType, value);
		}
	}
	
	public boolean isDatumChecked(String locationId, DatumType datumType) {
		LocationDatumPanel datumPanel = locationDatumPanels.get(locationId);
		if(datumPanel != null) {
			return datumPanel.isDatumChecked(datumType);
		}
		return false;
	}
	
	public void setDatumChecked(String locationId, DatumType datumType, boolean checked) {
		LocationDatumPanel datumPanel = locationDatumPanels.get(locationId);
		if(datumPanel != null) {
			datumPanel.setDatumChecked(datumType, checked);
		}
	}
	
	public void setCurrentDatumItem(String locationId, DatumType datumType) {
		LocationDatumPanel datumPanel = locationDatumPanels.get(locationId);
		if(datumPanel != null) {
			datumPanel.setCurrentDatumItem(datumType);
		}
	}
	
	@Override
	public void setOpaque(boolean isOpaque) {
		if(isOpaque != isOpaque()) {
			super.setOpaque(isOpaque);
			if(locationDatumPanels != null && !locationDatumPanels.isEmpty()) {
				for(LocationDatumPanel datumPanel : locationDatumPanels.values()) {
					datumPanel.setOpaque(isOpaque);
				}
			}
		}
	}
	
	@Override
	public void setBackground(Color bg) {
		super.setBackground(bg);		
		if(locationDatumPanels != null && !locationDatumPanels.isEmpty()) {
			for(LocationDatumPanel datumPanel : locationDatumPanels.values()) {
				datumPanel.setBackground(bg);
			}
		}
	}
}