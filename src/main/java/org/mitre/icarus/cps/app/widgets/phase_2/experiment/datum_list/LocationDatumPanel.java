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
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.mitre.icarus.cps.app.widgets.phase_2.WidgetConstants_Phase2;
import org.mitre.icarus.cps.exam.phase_2.testing.DatumType;

/**
 * Contains and manages the datum list panel for a location. Also contains an optional location title label
 * at the top.
 * 
 * @author CBONACETO
 *
 */
public class LocationDatumPanel extends JPanel {
	private static final long serialVersionUID = -2233836192556287662L;
	
	//datum states/values: checked (observed/available), current, included, color, name (string), value (string), 
	//tool tip text (possibly), clickable property (possibly)
	
	/** The location name */
	protected String locationName;
	
	/** The location name label */
	protected JLabel locationLabel;
	
	/** Contains the datum labels for each datum type associated with the location */
	protected EnumMap<DatumType, DatumLabel> datumLabelMap;
	
	/** The datum list panel */
	protected DatumListPanel datumPanel;
	
	/** The current datum item (highlighted) */
	protected DatumLabel currentDatumItem;	
	
	/** Indent levels */
	protected int indentLevel1;
	protected int indentLevel2;
	
	/** Whether to show datum check boxes */
	protected boolean showDatumCheckBoxes;
	
	public LocationDatumPanel() {
		this(true, true, 5, 10);
	}
	
	public LocationDatumPanel(boolean showLocationName, boolean showDatumCheckBoxes,
			int indentLevel1, int indentLevel2) {
		this(null, null, showLocationName, showDatumCheckBoxes, indentLevel1, indentLevel2);
	}
	
	public LocationDatumPanel(String locationName, Collection<DatumListItem> datumItems) {
		this(locationName, datumItems, true, true, 5, 10);
	}
	
	public LocationDatumPanel(String locationName, Collection<DatumListItem> datumItems, 
			boolean showLocationName, boolean showDatumCheckBoxes,
			int indentLevel1, int indentLevel2) {
		super(new GridBagLayout());
		this.indentLevel1 = indentLevel1;
		this.indentLevel2 = indentLevel2;
		this.showDatumCheckBoxes = showDatumCheckBoxes;
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		//Add the location name label
		if(showLocationName) {
			locationLabel = new JLabel(locationName != null ? locationName : "");
			locationLabel.setFont(WidgetConstants_Phase2.FONT_LOCATION_NAME);
			gbc.insets.bottom = WidgetConstants_Phase2.DATUM_LIST_VERTICAL_SPACER;
			add(locationLabel, gbc);
			gbc.gridy++;
			gbc.insets.bottom = 0;
		}		
		
		//Add the datum list panel
		datumPanel = new DatumListPanel();
		datumPanel.setDatumLabels(datumItems != null ? createDatumLabels(datumItems) : null, true);
		gbc.weighty = 1;
		add(datumPanel, gbc);
	}
	
	public void setLocationNameFont(Font font) {
		if(locationLabel != null) {
			locationLabel.setFont(font);
		}
	}
	
	public void setDatumLabelFont(Font font) {
		for(DatumLabel datumLabel : datumLabelMap.values()) {
			datumLabel.setFont(font);
		}
	}
	
	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
		if(locationLabel != null) {
			locationLabel.setText(locationName);			
		}
	}
	
	/**
	 * @param datumItem
	 */
	public void addDatumItem(DatumListItem datumItem) {
		datumPanel.addDatumLabels(datumItem != null ? createDatumLabels(Collections.singleton(datumItem)) : null, true);
		revalidate();
		repaint();
	}	
	
	/**
	 * @param datumItems
	 */
	public void addDatumItems(Collection<DatumListItem> datumItems) {
		datumPanel.addDatumLabels(datumItems != null ? createDatumLabels(datumItems) : null, true);
		revalidate();
		repaint();
	}
	
	/**
	 * @param datumItems
	 */
	public void setDatumItems(Collection<DatumListItem> datumItems) {
		datumPanel.setDatumLabels(datumItems != null ? createDatumLabels(datumItems) : null, true);
		revalidate();
		repaint();
	}
	
	/**
	 * Creates a DatumLabel for each DatumType.
	 */
	protected List<DatumLabel> createDatumLabels(Collection<DatumListItem> datumItems) {
		if(datumLabelMap == null) {
			datumLabelMap = new EnumMap<DatumType, DatumLabel>(DatumType.class);
		}
		LinkedList<DatumLabel> datumLabels = new LinkedList<DatumLabel>();
		if(datumItems != null && !datumItems.isEmpty()) {
			for(DatumListItem datumItem : datumItems) {
				DatumLabel datumLabel = DatumLabelFactory.createDatumLabel(datumItem, showDatumCheckBoxes, 
						datumItem.isTitleLabel(), WidgetConstants_Phase2.LOCATION_LIST_DATUM_VALUE_ALIGNMENT);
				if(datumLabel != null) {
					datumLabel.setIndentLevel(datumItem.isReportedType() ? indentLevel2 : indentLevel1);
					datumLabels.add(datumLabel);
					datumLabelMap.put(datumItem.getDatumType(), datumLabel);
				}
			}
		}
		return datumLabels;
	}
	
	public boolean containsDatumItem(DatumType datumType) {
		return datumLabelMap.containsKey(datumType);
	}
	
	public String getDatumValue(DatumType datumType) {
		DatumLabel datumLabel = datumLabelMap.get(datumType);
		if(datumLabel != null) {
			return datumLabel.getDatumValue();
		}
		return null;
	}

	public void setDatumValue(DatumType datumType, String value) {
		DatumLabel datumLabel = datumLabelMap.get(datumType);
		if(datumLabel != null) {
			datumLabel.setDatumValue(value);
		}
	}
	
	public boolean isDatumChecked(DatumType datumType) {
		DatumLabel datumLabel = datumLabelMap.get(datumType);
		if(datumLabel != null) {
			return datumLabel.isChecked();
		}
		return false;
	}
	
	public void setDatumChecked(DatumType datumType, boolean checked) {
		DatumLabel datumLabel = datumLabelMap.get(datumType);
		if(datumLabel != null) {
			datumLabel.setChecked(checked);
		}
	}
	
	public void setCurrentDatumItem(DatumType datumType) {
		DatumLabel datumLabel = datumLabelMap.get(datumType);		
		if(datumLabel != null) {
			if(currentDatumItem != null) {
				currentDatumItem.setHighlighted(false);
				currentDatumItem = null;
			}			
			currentDatumItem = datumLabel;
			datumLabel.setHighlighted(true);
		}
	}
	
	@Override
	public void setOpaque(boolean isOpaque) {
		if(isOpaque != isOpaque()) {
			super.setOpaque(isOpaque);
			if(locationLabel != null) {
				locationLabel.setOpaque(isOpaque);
			}
			if(datumPanel != null) {
				datumPanel.setOpaque(isOpaque);
			}
		}
	}
	
	@Override
	public void setBackground(Color bg) {
		super.setBackground(bg);
		if(locationLabel != null) {
			locationLabel.setBackground(bg);
		}
		if(datumPanel != null) {
			datumPanel.setBackground(bg);
		}		
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("Test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		LocationDatumPanel comp = new LocationDatumPanel("Blue Location", 
				Arrays.asList(DatumListItemType.OSINT.getDatumListItem(),
						DatumListItemType.IMINT.getDatumListItem(), 
						DatumListItemType.P_PROPENSITY.getDatumListItem(), 
						DatumListItemType.HUMINT.getDatumListItem(),
						DatumListItemType.P_CAPABILITY_PROPENSITY.getDatumListItem(), 
						DatumListItemType.SIGINT.getDatumListItem(), 
						DatumListItemType.P_ACTIVITY.getDatumListItem(),
						DatumListItemType.P_ACTIVITY_CAPABILITY_PROPENSITY.getDatumListItem()));		
		//LocationDatumPanel comp = new LocationDatumPanel(null, null);
		frame.getContentPane().add(comp);
		frame.pack();
		frame.setVisible(true);
	}
}